package com.argel6767.tailor.ai.user;

import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User addProfession(String email, String profession) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found" + email));
        user.setProfession(profession);
        return userRepository.save(user);
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found" + email));
    }
}
