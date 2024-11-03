package com.argel6767.tailor.ai.user;

import com.argel6767.tailor.ai.user.requests.AddProfessionRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
