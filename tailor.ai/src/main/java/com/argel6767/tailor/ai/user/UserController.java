package com.argel6767.tailor.ai.user;

import com.argel6767.tailor.ai.user.requests.AddProfessionRequest;
import com.argel6767.tailor.ai.user.requests.EmailObjectRequest;
import org.springframework.http.ResponseEntity;
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
        catch(RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    //TODO add change password, possibly log out?
}
