package com.argel6767.tailor.ai.auth;

import com.argel6767.tailor.ai.auth.requests.AuthenticateUserDto;
import com.argel6767.tailor.ai.auth.requests.ResendEmailDto;
import com.argel6767.tailor.ai.auth.requests.VerifyUserDto;
import com.argel6767.tailor.ai.auth.responses.LoginResponse;
import com.argel6767.tailor.ai.jwt.JwtService;
import com.argel6767.tailor.ai.user.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        User registeredUser = authenticationService.signUp(request);
        return ResponseEntity.ok(registeredUser);
    }

    /*
     * login user endpoint
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody AuthenticateUserDto request) {
        User user = authenticationService.authenticateUser(request);
        String token = jwtService.generateToken(user);
        LoginResponse response = new LoginResponse(token, jwtService.getExpirationTime());
        return ResponseEntity.ok(response);
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

}
