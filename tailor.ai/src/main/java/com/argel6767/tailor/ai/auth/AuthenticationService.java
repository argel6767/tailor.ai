package com.argel6767.tailor.ai.auth;

import com.argel6767.tailor.ai.auth.requests.AuthenticateUserDto;
import com.argel6767.tailor.ai.auth.requests.VerifyUserDto;
import com.argel6767.tailor.ai.email.EmailService;
import com.argel6767.tailor.ai.email.EmailVerificationException;
import com.argel6767.tailor.ai.user.User;
import com.argel6767.tailor.ai.user.UserRepository;
import jakarta.mail.MessagingException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

/**
 * holds the business logic for authenticating users and sending emails for verification codes
 */
@Service
public class AuthenticationService {
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    public AuthenticationService(UserRepository userRepository, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder,
                                 EmailService emailService) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    /*
     * signs up user to app, will fail if the email is taken as they need to be unique
     */
    public User signUp(AuthenticateUserDto request) {
        User user = new User();
        user.setEmail(request.getUsername());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        setVerificationCodeAndSendIt(user);
        return userRepository.save(user);
    }

    /*
     * holds the verification code setting and sending for reduced repeat code
     */
    private void setVerificationCodeAndSendIt(User user) {
        user.setVerificationCode(generateVerificationCode());
        user.setCodeExpiry(LocalDateTime.now().plusMinutes(30));
        sendVerificationEmail(user);
    }

    /*
     * authenticates user, usually when they are logging in
     * will throw an exception if the email is not tied to any user or the email has not been verified
     */
    public User authenticateUser(AuthenticateUserDto request) {
        User user = userRepository.findByEmail(request.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException(request.getUsername()));
        if (!user.isEnabled()) {
            throw new EmailVerificationException("Email not verified");
        }
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        user.setLastLogin(LocalDateTime.now());
        return userRepository.save(user);
    }

    /*
     * verifies user by checking if request token is equal to the one in db
     * if so then the codeExpiry is set to null
     * and isEmailVerified to true
     */
    public void verifyUser(VerifyUserDto request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException(request.getEmail()));
        if (user.getCodeExpiry().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Verification code expired");
        }
        if (request.getVerificationToken().equals(user.getVerificationCode())) {
            user.setCodeExpiry(null);
            user.setIsEmailVerified(true);
            userRepository.save(user);
        }
        else {
            throw new RuntimeException("Invalid verification token");
        }

    }

    /*
     * resends verification email to user but with new code
     * can be used if their last code expired
     */
    public void resendVerificationEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(email));
        if (user.isEnabled()) {
            throw new EmailVerificationException("Email is already verified");
        }
        setVerificationCodeAndSendIt(user);
        userRepository.save(user);
    }

    /*
     * formats and generates the verification email that will contain the verification code to the user
     */
    private void sendVerificationEmail(User user) {
        String to = user.getUsername();
        String subject = "Verification Email";
        String code = user.getVerificationCode();
        String body = String.format("""
            <html>
                <h1> Welcome to Tailor.ai!</h1>
                <p> Enter the verification code below! </p>
                <p> %s </p>
                <a href="https://site.com"> Here! </a>
            </<html>""", code);
        sendEmail(to, subject, body);
    }

    /*
     * holds the try catch logic of sending the email
     */
    private void sendEmail(String to, String subject, String body) {
        try{
            emailService.sendEmail(to, subject, body);
        } catch (MessagingException e) {
            throw new RuntimeException("Error sending verification email", e);
        }
    }

    /*
     * generates a random 6-digit number to be used as a verification code
     */
    private String generateVerificationCode() {
        Random random = new Random();
        return String.valueOf(random.nextInt(900000) + 100000); //guaranteed 6-digit number
    }

}
