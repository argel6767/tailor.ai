package com.argel6767.tailor.ai.user;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * UserService that houses business logic of user fields
 */
@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /*
     * add user profession
     */
    public User addProfession(String email, String profession) {
        User user = getUserByEmail(email);
        user.setProfession(profession);
        return userRepository.save(user);
    }

    /*
     * checks if user has set profession
     * for routing purposes on frontend
     */
    public Boolean hasSetProfession(String email) {
        User user = getUserByEmail(email);
        return user.getProfession() != null;
    }

    /*
     * get a user by their email, aka unique/ identifier
     */
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found" + email));
    }

    public String deleteUser(String email) {
        User user = getUserByEmail(email);
        userRepository.delete(user);
        return "User successfully deleted";
    }
}
