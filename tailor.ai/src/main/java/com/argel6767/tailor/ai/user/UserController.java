package com.argel6767.tailor.ai.user;

import com.argel6767.tailor.ai.user.requests.AddProfessionRequest;
import com.argel6767.tailor.ai.user.requests.EmailObjectRequest;
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
        User user = userService.addProfession(addProfessionRequest.getEmail(), addProfessionRequest.getProfession());
        return ResponseEntity.ok(user);
    }

    @GetMapping("/profession/{email}")
    public ResponseEntity<Boolean> getProfessionStatus(@PathVariable String email) {
        Boolean status = userService.hasSetProfession(email);
        return ResponseEntity.ok(status);
    }

    @GetMapping("/{email}")
    public ResponseEntity<User> getUser(@PathVariable String email) {
        try {
            User user = userService.getUserByEmail(email);
            return ResponseEntity.ok(user);
        }
        catch(UsernameNotFoundException unfe) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("{email}")
    public ResponseEntity<String> deleteUser(@PathVariable String email) {
        try {
            return ResponseEntity.ok(userService.deleteUser(email));
        }
        catch(UsernameNotFoundException unfe) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }
    }


    //TODO possibly log out?
}
