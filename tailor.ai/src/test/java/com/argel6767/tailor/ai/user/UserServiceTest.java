package com.argel6767.tailor.ai.user;

import com.argel6767.tailor.ai.user.requests.AddProfessionRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    private UserService userService;
    private final String EMAIL = "test@test.com";
    private final String PROFESSION = "Software Engineer";
    private final AddProfessionRequest addProfessionRequest = new AddProfessionRequest(PROFESSION);
    private User user = new User();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userService = new UserService(userRepository);
        user.setEmail(EMAIL);
        user.setProfession(PROFESSION);
    }

    @Test
    void testAddProfession() {
        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(user));
        when(userRepository.save(any())).thenReturn(user);
        User professionUser = userService.addProfession(EMAIL, addProfessionRequest.getProfession());
        assertNotNull(professionUser);
        assertEquals(PROFESSION, professionUser.getProfession());
        verify(userRepository, times(1)).findByEmail(EMAIL);
        verify(userRepository, times(1)).save(user);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void testProfessionHasNotBeenSet() {
        user.setProfession(null);
        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(user));
        when(userRepository.save(any())).thenReturn(user);
        Boolean status = userService.hasSetProfession(EMAIL);
        assertFalse(status);
    }

    @Test
    void testProfessionHasBeenSet() {
        user.setProfession("Software Engineer");
        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(user));
        when(userRepository.save(any())).thenReturn(user);
        Boolean status = userService.hasSetProfession(EMAIL);
        assertTrue(status);
    }

    @Test
    void testDeleteUserWithValidEmail() {
        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(user));
        String message = userService.deleteUser(EMAIL);
        verify(userRepository, times(1)).delete(user);
        assertEquals("User successfully deleted", message);
    }

    @Test
    void testDeleteUserWithInvalidEmail() {
        when(userRepository.findByEmail(EMAIL)).thenThrow(UsernameNotFoundException.class);
        assertThrows(UsernameNotFoundException.class, () -> userService.deleteUser(EMAIL));
    }

}
