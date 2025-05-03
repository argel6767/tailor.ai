package com.argel6767.tailor.ai.user;

import com.argel6767.tailor.ai.jwt.JwtUtils;
import com.argel6767.tailor.ai.user.requests.AddProfessionRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/user")
@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PutMapping("/profession")
    public ResponseEntity<User> updateProfession(@RequestBody AddProfessionRequest addProfessionRequest) {
        String email = JwtUtils.getCurrentUserEmail();
        User user = userService.addProfession(email, addProfessionRequest.getProfession());
        return ResponseEntity.ok(user);
    }

    @GetMapping("/profession")
    public ResponseEntity<Boolean> getProfessionStatus() {
        String email = JwtUtils.getCurrentUserEmail();
        Boolean status = userService.hasSetProfession(email);
        return ResponseEntity.ok(status);
    }

    @GetMapping()
    public ResponseEntity<User> getUser() {
        try {
            String email = JwtUtils.getCurrentUserEmail();
            User user = userService.getUserByEmail(email);
            return ResponseEntity.ok(user);
        }
        catch(UsernameNotFoundException unfe) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping()
    public ResponseEntity<String> deleteUser() {
        try {
            String email = JwtUtils.getCurrentUserEmail();
            return ResponseEntity.ok(userService.deleteUser(email));
        }
        catch(UsernameNotFoundException unfe) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }
    }


    //TODO possibly log out?
}
