package com.argel6767.tailor.ai.user;

import com.argel6767.tailor.ai.user.requests.AddProfessionRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class UserControllerTest {

    @Mock
    private UserService userService;
    private UserController userController;
    private final String EMAIL = "test@test.com";
    private final String PROFESSION = "Software Engineer";
    private final AddProfessionRequest addProfessionRequest = new AddProfessionRequest(EMAIL, PROFESSION);
    private User user = new User();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userController = new UserController(userService);
        user.setEmail(EMAIL);
        user.setProfession(PROFESSION);
    }

    @Test
    void testUpdateProfession() {
        when(userService.addProfession(EMAIL, PROFESSION)).thenReturn(user);
        ResponseEntity<User> response = userController.updateProfession(addProfessionRequest);
        assertNotNull(response);

    }
}