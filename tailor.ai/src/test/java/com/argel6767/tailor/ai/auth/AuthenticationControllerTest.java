package com.argel6767.tailor.ai.auth;

import com.argel6767.tailor.ai.auth.requests.AuthenticateUserDto;
import com.argel6767.tailor.ai.auth.requests.ResendEmailDto;
import com.argel6767.tailor.ai.auth.requests.VerifyUserDto;
import com.argel6767.tailor.ai.auth.responses.LoginResponse;
import com.argel6767.tailor.ai.jwt.JwtService;
import com.argel6767.tailor.ai.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AuthenticationControllerTest {

    @Mock
    private AuthenticationService authenticationService;

    @Mock
    private JwtService jwtService;

    private AuthenticationController authenticationController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        authenticationController = new AuthenticationController(authenticationService, jwtService);
    }

    @Test
    void testRegisterSuccessful() {
        // Arrange
        AuthenticateUserDto request = new AuthenticateUserDto("test@example.com", "password123");
        User expectedUser = new User("test@example.com", "encodedPassword123");
        when(authenticationService.signUp(request)).thenReturn(expectedUser);
        // Act
        ResponseEntity<User> response = authenticationController.register(request);
        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(expectedUser, response.getBody());
        verify(authenticationService).signUp(request);
    }

    @Test
    void testLoginSuccessful() {
        // Arrange
        AuthenticateUserDto request = new AuthenticateUserDto("test@example.com", "password123");
        User user = new User("test@example.com", "encodedPassword123");
        String token = "jwt.token.here";
        long expirationTime = 3600000L;
        when(authenticationService.authenticateUser(request)).thenReturn(user);
        when(jwtService.generateToken(user)).thenReturn(token);
        when(jwtService.getExpirationTime()).thenReturn(expirationTime);
        // Act
        ResponseEntity<LoginResponse> response = authenticationController.login(request);
        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(token, response.getBody().getToken());
        assertEquals(expirationTime, response.getBody().getExpiresIn());
        verify(authenticationService).authenticateUser(request);
        verify(jwtService).generateToken(user);
    }

    @Test
    void testVerifySuccessful() {
        // Arrange
        VerifyUserDto request = new VerifyUserDto("test@example.com", "123456");
        doNothing().when(authenticationService).verifyUser(request);
        // Act
        ResponseEntity<?> response = authenticationController.verify(request);
        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("User verified!", response.getBody());
        verify(authenticationService).verifyUser(request);
    }

    @Test
    void testVerifyFailure() {
        // Arrange
        VerifyUserDto request = new VerifyUserDto("test@example.com", "123456");
        String errorMessage = "Invalid verification code";
        doThrow(new RuntimeException(errorMessage)).when(authenticationService).verifyUser(request);
        // Act
        ResponseEntity<?> response = authenticationController.verify(request);
        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(errorMessage, response.getBody());
        verify(authenticationService).verifyUser(request);
    }

    @Test
    void testResendVerificationEmailSuccessful() {
        // Arrange
        String email = "test@example.com";
        ResendEmailDto emailDto = new ResendEmailDto(email);
        doNothing().when(authenticationService).resendVerificationEmail(email);
        // Act
        ResponseEntity<?> response = authenticationController.resend(emailDto);
        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Verification code resent!", response.getBody());
        verify(authenticationService).resendVerificationEmail(email);
    }

    @Test
    void testResendVerificationEmailFailure() {
        // Arrange
        String email = "test@example.com";
        ResendEmailDto emailDto = new ResendEmailDto(email);
        String errorMessage = "Email already verified";
        doThrow(new RuntimeException(errorMessage)).when(authenticationService).resendVerificationEmail(email);
        // Act
        ResponseEntity<?> response = authenticationController.resend(emailDto);
        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(errorMessage, response.getBody());
        verify(authenticationService).resendVerificationEmail(email);
    }

    @Test
    void testLoginFailureInvalidCredentials() {
        // Arrange
        AuthenticateUserDto request = new AuthenticateUserDto("test@example.com", "wrongpassword");
        when(authenticationService.authenticateUser(request))
                .thenThrow(new RuntimeException("Invalid credentials"));
        // Act & Assert
        assertThrows(RuntimeException.class, () ->
                authenticationController.login(request)
        );
        verify(authenticationService).authenticateUser(request);
        verify(jwtService, never()).generateToken(any());
    }

    @Test
    void testRegisterFailureEmailTaken() {
        // Arrange
        AuthenticateUserDto request = new AuthenticateUserDto("taken@example.com", "password123");
        when(authenticationService.signUp(request))
                .thenThrow(new RuntimeException("Email already taken"));
        // Act & Assert
        assertThrows(RuntimeException.class, () ->
                authenticationController.register(request)
        );
        verify(authenticationService).signUp(request);
    }

}