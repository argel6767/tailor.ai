package com.argel6767.tailor.ai.user;

import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User addProfession(String email, String profession) {
        User user = getUserByEmail(email);
        user.setProfession(profession);
        return userRepository.save(user);
    }

    public Boolean hasSetProfession(String email) {
        User user = getUserByEmail(email);
        String profession = user.getProfession();
        return user.getProfession() != null;
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found" + email));
    }
}
