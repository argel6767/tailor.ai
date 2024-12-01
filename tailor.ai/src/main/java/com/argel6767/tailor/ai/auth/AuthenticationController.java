package com.argel6767.tailor.ai.auth;

import com.argel6767.tailor.ai.auth.requests.AuthenticateUserDto;
import com.argel6767.tailor.ai.auth.requests.ChangePasswordDto;
import com.argel6767.tailor.ai.auth.requests.ResendEmailDto;
import com.argel6767.tailor.ai.auth.requests.VerifyUserDto;
import com.argel6767.tailor.ai.auth.responses.LoginResponse;
import com.argel6767.tailor.ai.email.EmailVerificationException;
import com.argel6767.tailor.ai.jwt.JwtService;
import com.argel6767.tailor.ai.user.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

/**
 * holds auth endpoints that can be accessed without a JWT token
 */
@RequestMapping("/auth")
@RestController
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final JwtService jwtService;

    public AuthenticationController(AuthenticationService authenticationService, JwtService jwtService) {
        this.authenticationService = authenticationService;
        this.jwtService = jwtService;
    }

    /*
     * register user endpoint
     */
    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody AuthenticateUserDto request) {
        try {
            User registeredUser = authenticationService.signUp(request);
            return ResponseEntity.ok(registeredUser);
        }
        catch (AuthenticationException e) {
            return new ResponseEntity<>(null, HttpStatus.CONFLICT);
        }

    }

    /*
     * login user endpoint
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody AuthenticateUserDto request) {
        try {
            User user = authenticationService.authenticateUser(request);
            String token = jwtService.generateToken(user);
            LoginResponse response = new LoginResponse(token, jwtService.getExpirationTime());
            return ResponseEntity.ok(response);
        }
        catch (UsernameNotFoundException enfe) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        catch (EmailVerificationException eve) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }

    }

    /*
     * verify user endpoint via the code they input
     */
    @PostMapping("/verify")
    public ResponseEntity<?> verify(@RequestBody VerifyUserDto request) {
        try {
            authenticationService.verifyUser(request);
            return ResponseEntity.ok("User verified!");
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /*
     * resend verification email endpoint
     */
    @PostMapping("/resend")
    public ResponseEntity<?> resend(@RequestBody ResendEmailDto email) {
        try {
            authenticationService.resendVerificationEmail(email.getEmail());
            return ResponseEntity.ok("Verification code resent!");
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/password")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordDto request) {
        try {
            return ResponseEntity.ok(authenticationService.changePassword(request));
        }
        catch (UsernameNotFoundException unfe) {
            return ResponseEntity.notFound().build();
        }
        catch (RuntimeException re) {
            return new ResponseEntity<>(re, HttpStatus.UNAUTHORIZED);
        }
    }

}
