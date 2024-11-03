package com.argel6767.tailor.ai.auth;

import com.argel6767.tailor.ai.auth.requests.AuthenticateUserDto;
import com.argel6767.tailor.ai.auth.requests.VerifyUserDto;
import com.argel6767.tailor.ai.email.EmailService;
import com.argel6767.tailor.ai.email.EmailVerificationException;
import com.argel6767.tailor.ai.user.User;
import com.argel6767.tailor.ai.user.UserRepository;
import jakarta.mail.MessagingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AuthenticationServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private EmailService emailService;

    private AuthenticationService authenticationService;

    private User successfulUser = new User();



    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        authenticationService = new AuthenticationService(
                userRepository,
                authenticationManager,
                passwordEncoder,
                emailService
        );

        successfulUser.setEmail("test@example.com");
        successfulUser.setPasswordHash("encodedPassword123");
        successfulUser.setVerificationCode("123456");
        successfulUser.setCodeExpiry(LocalDateTime.now().plusMinutes(10));

    }

    @Test
    void testSignUpSuccessful() throws MessagingException {
        // Arrange
        String email = "test@example.com";
        String password = "password123";
        String encodedPassword = "encodedPassword123";
        AuthenticateUserDto request = new AuthenticateUserDto(email, password);
        when(passwordEncoder.encode(password)).thenReturn(encodedPassword);
        when(userRepository.save(any(User.class))).thenReturn(successfulUser);
        // Act
        User result = authenticationService.signUp(request);
        // Assert
        assertNotNull(result);
        assertEquals(email, result.getUsername());
        assertEquals(encodedPassword, result.getPassword());
        assertNotNull(result.getVerificationCode());
        assertNotNull(result.getCodeExpiry());
        verify(emailService).sendEmail(any(), anyString(), anyString());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void testAuthenticateUserSuccessful() {
        // Arrange
        String email = "test@example.com";
        String password = "password123";
        AuthenticateUserDto request = new AuthenticateUserDto(email, password);
        User user = new User(email, password);
        user.setIsEmailVerified(true);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(authenticationService.authenticateUser(request)).thenReturn(user);
        // Act
        User result = authenticationService.authenticateUser(request);
        result.setEmail(email);
        // Assert
        assertNotNull(result);
        assertEquals(email, result.getEmail());
    }

    @Test
    void testAuthenticateUserEmailNotVerified() {
        // Arrange
        String email = "test@example.com";
        String password = "password123";
        AuthenticateUserDto request = new AuthenticateUserDto(email, password);
        User user = new User(email, password);
        user.setIsEmailVerified(false);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        // Act & Assert
        assertThrows(EmailVerificationException.class, () ->
                authenticationService.authenticateUser(request)
        );
        verify(authenticationManager, never()).authenticate(any());
    }

    @Test
    void testVerifyUserSuccessful() {
        // Arrange
        String email = "test@example.com";
        String code = "123456";
        User user = new User(email, "password");
        user.setVerificationCode(code);
        user.setCodeExpiry(LocalDateTime.now().plusMinutes(10));

        VerifyUserDto request = new VerifyUserDto(email, code);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        // Act
        authenticationService.verifyUser(request);

        // Assert
        assertTrue(user.isEnabled());
        assertNull(user.getCodeExpiry());
    }

    @Test
    void testVerifyUserExpiredCode() {
        // Arrange
        String email = "test@example.com";
        String code = "123456";
        User user = new User(email, "password");
        user.setVerificationCode(code);
        user.setCodeExpiry(LocalDateTime.now().minusMinutes(10));

        VerifyUserDto request = new VerifyUserDto(email, code);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        // Act & Assert
        assertThrows(RuntimeException.class, () ->
                authenticationService.verifyUser(request)
        );
        assertFalse(user.isEnabled());
    }

    @Test
    void testResendVerificationEmailSuccessful() throws MessagingException {
        // Arrange
        String email = "test@example.com";
        User user = new User(email, "password");
        user.setIsEmailVerified(false);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);
        // Act
        authenticationService.resendVerificationEmail(email);
        // Assert
        verify(emailService).sendEmail(any(), any(), any());
        verify(userRepository).save(user);
        assertNotNull(user.getVerificationCode());
        assertNotNull(user.getCodeExpiry());
    }

    @Test
    void testResendVerificationEmailAlreadyVerified() throws MessagingException {
        // Arrange
        String email = "test@example.com";
        User user = new User(email, "password");
        user.setIsEmailVerified(true);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        // Act & Assert
        assertThrows(EmailVerificationException.class, () ->
                authenticationService.resendVerificationEmail(email)
        );
        verify(emailService, never()).sendEmail(anyString(), anyString(), anyString());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testVerifyUserInvalidCode() {
        // Arrange
        String email = "test@example.com";
        String code = "123456";
        String wrongCode = "654321";
        User user = new User(email, "password");
        user.setVerificationCode(code);
        user.setCodeExpiry(LocalDateTime.now().plusMinutes(10));

        VerifyUserDto request = new VerifyUserDto(email, wrongCode);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        // Act & Assert
        assertThrows(RuntimeException.class, () ->
                authenticationService.verifyUser(request)
        );
        assertFalse(user.isEnabled());
    }

    @Test
    void testGenerateVerificationCodeFormat() {
        // Arrange
        String email = "test@example.com";
        String password = "password123";
        AuthenticateUserDto request = new AuthenticateUserDto(email, password);
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArguments()[0]);

        // Act
        User result = authenticationService.signUp(request);

        // Assert
        String code = result.getVerificationCode();
        assertNotNull(code);
        assertTrue(code.matches("\\d{6}")); // Verify it's a 6-digit number
    }
}