package com.argel6767.tailor.ai.auth;

import com.argel6767.tailor.ai.auth.exceptions.AuthenticationException;
import com.argel6767.tailor.ai.auth.exceptions.ExpiredVerificationCodeException;
import com.argel6767.tailor.ai.auth.requests.AuthenticateUserDto;
import com.argel6767.tailor.ai.auth.requests.ChangePasswordDto;
import com.argel6767.tailor.ai.auth.requests.ForgotPasswordDto;
import com.argel6767.tailor.ai.auth.requests.VerifyUserDto;
import com.argel6767.tailor.ai.email.EmailService;
import com.argel6767.tailor.ai.email.EmailVerificationException;
import com.argel6767.tailor.ai.user.User;
import com.argel6767.tailor.ai.user.UserRepository;
import jakarta.mail.MessagingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.function.Consumer;

/**
 * holds the business logic for authenticating users and sending emails for verification codes
 */
@Service
public class AuthenticationService {
    private static final Logger log = LoggerFactory.getLogger(AuthenticationService.class);
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
        if (userRepository.findByEmail(request.getUsername()).isPresent()) {
            throw new AuthenticationException("Email is already in use");
        }
        log.info("Creating new user {}", request.getUsername());
        User user = new User(request.getUsername(), passwordEncoder.encode(request.getPassword()));
        setVerificationCodeAndSendIt(user, this::sendVerificationEmail);
        return userRepository.save(user);
    }

    /*
     * holds the verification code setting and sending for reduced repeat code
     */
    private void setVerificationCodeAndSendIt(User user, Consumer<User> sendEmail) {
        user.setVerificationCode(generateVerificationCode());
        user.setCodeExpiry(LocalDateTime.now().plusMinutes(30));
        sendEmail.accept(user);
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
        User user = getUser(request.getEmail());
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

    private User getUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(email));
    }

    /*
     * resends verification email to user but with new code
     * can be used if their last code expired
     */
    public void resendVerificationEmail(String email) {
        User user = getUser(email);
        if (user.isEnabled()) {
            throw new EmailVerificationException("Email is already verified");
        }
        setVerificationCodeAndSendIt(user, this::sendVerificationEmail);
        userRepository.save(user);
    }

    /*
     * changes user's password to new one, only if:
     * they exist and if they send their correct current password
     */
    public User changePassword(ChangePasswordDto request) {
        User user = getUser(request.getEmail());
        String oldPasswordHash = user.getPasswordHash();
        if (!passwordEncoder.matches(request.getOldPassword(), oldPasswordHash)) {
            throw new RuntimeException("Invalid password");
        }
        user.setPasswordHash(passwordEncoder.encode(request.getNewPassword()));
        return userRepository.save(user);
    }

    /*
     * sends a user a verification code for changing one's, should they forget it.
     */
    public void sendForgottenPasswordVerificationCode(String email) {
        User user = getUser(email);
        setVerificationCodeAndSendIt(user, this::sendResetPasswordEmail);
        userRepository.save(user);
    }

    /*
     * resets a user's password, only if:
     * code is not expired
     * and verification code is correct one in db
     */
    public User resetPassword(ForgotPasswordDto request) {
        User user = getUser(request.getEmail());
        if (user.getCodeExpiry().isBefore(LocalDateTime.now())) {
            throw new ExpiredVerificationCodeException("Verification code expired, request another one");
        }
        if (!user.getVerificationCode().equals(request.getVerificationCode())) {
            throw new AuthenticationException("Invalid verification code");
        }
        user.setCodeExpiry(null);
        user.setVerificationCode(null);
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        return userRepository.save(user);
    }

    /*
     * formats and generates the verification email that will contain the verification code to the user
     */
    private void sendVerificationEmail(User user) {
        String to = user.getUsername();
        String subject = "Verification Email";
        String code = user.getVerificationCode();
        String body = String.format("""
            <!DOCTYPE html>
            <html>
            <head>
                <style>
                    body {
                        font-family: Arial, sans-serif;
                        background-color: #f9f9f9;
                        color: #333333;
                        margin: 0;
                        padding: 0;
                    }

                    .email-container {
                        max-width: 600px;
                        margin: 50px auto;
                        background: #ffffff;
                        border-radius: 8px;
                        box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
                        overflow: hidden;
                    }

                    .header {
                        background-color: #0C7C59;
                        padding: 20px;
                        text-align: center;
                        color: white;
                    }

                    .header h1 {
                        margin: 0;
                        font-size: 24px;
                        font-weight: 600;
                    }

                    .content {
                        padding: 30px;
                        text-align: center;
                    }

                    .content p {
                        margin: 20px 0;
                        font-size: 16px;
                        line-height: 1.6;
                    }

                    .verification-code {
                        display: inline-block;
                        background-color: #0C7C59;
                        color: white;
                        font-weight: bold;
                        padding: 10px 20px;
                        border-radius: 4px;
                        font-size: 20px;
                        letter-spacing: 1.5px;
                        text-transform: uppercase;
                    }

                    .footer {
                        background-color: #f1f1f1;
                        padding: 10px;
                        text-align: center;
                        font-size: 12px;
                        color: #666666;
                    }

                    .footer a {
                        color: #4A90E2;
                        text-decoration: none;
                    }
                </style>
            </head>
            <body>
                <div class="email-container">
                    <div class="header">
                        <h1>Welcome to Tailor.AI!</h1>
                    </div>
                    <div class="content">
                        <p>We're excited to have you on board! Please use the verification code below to complete your sign-up process:</p>
                        <p class="verification-code">%s</p>
                        <p>If you didn't request this code, please ignore this email or contact our support team.</p>
                    </div>
                    <div class="footer">
                        <p>&copy; 2024 Tailor.AI. All rights reserved.</p>
                    </div>
                </div>
            </body>
            </html>""", code);
        sendEmail(to, subject, body);
    }

    private void sendResetPasswordEmail(User user) {
        String to = user.getUsername();
        String subject = "Reset Password";
        String code = user.getVerificationCode();
        String body = String.format("""
            <!DOCTYPE html>
            <html>
            <head>
                <style>
                    body {
                        font-family: Arial, sans-serif;
                        background-color: #f9f9f9;
                        color: #333333;
                        margin: 0;
                        padding: 0;
                    }

                    .email-container {
                        max-width: 600px;
                        margin: 50px auto;
                        background: #ffffff;
                        border-radius: 8px;
                        box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
                        overflow: hidden;
                    }

                    .header {
                        background-color: #0C7C59;
                        padding: 20px;
                        text-align: center;
                        color: white;
                    }

                    .header h1 {
                        margin: 0;
                        font-size: 24px;
                        font-weight: 600;
                    }

                    .content {
                        padding: 30px;
                        text-align: center;
                    }

                    .content p {
                        margin: 20px 0;
                        font-size: 16px;
                        line-height: 1.6;
                    }

                    .verification-code {
                        display: inline-block;
                        background-color: #0C7C59;
                        color: white;
                        font-weight: bold;
                        padding: 10px 20px;
                        border-radius: 4px;
                        font-size: 20px;
                        letter-spacing: 1.5px;
                        text-transform: uppercase;
                    }

                    .footer {
                        background-color: #f1f1f1;
                        padding: 10px;
                        text-align: center;
                        font-size: 12px;
                        color: #666666;
                    }

                    .footer a {
                        color: #4A90E2;
                        text-decoration: none;
                    }
                </style>
            </head>
            <body>
                <div class="email-container">
                    <div class="header">
                        <h1>Forgot your password?</h1>
                    </div>
                    <div class="content">
                        <p>Reset your password with the verification code below:</p>
                        <p class="verification-code">%s</p>
                        <p>If you didn't request this code, please ignore this email or contact our support team.</p>
                    </div>
                    <div class="footer">
                        <p>&copy; 2024 Tailor.AI. All rights reserved.</p>
                    </div>
                </div>
            </body>
            </html>""", code);
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
