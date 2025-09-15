package backend_api.controller;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.springframework.http.ResponseEntity;

import backend_api.entities.User;
import backend_api.services.UserService;

class UserControllerTest {

    @Test
    void getAllUsers_returnsListOfUsers() {
        UserService userService = mock(UserService.class);
        UserController controller = new UserController(userService);
        List<User> users = Arrays.asList(new User(), new User());
        when(userService.getAllUsers()).thenReturn(users);
        ResponseEntity<List<User>> response = controller.getAllUsers();
        assertEquals(2, response.getBody().size());
        assertEquals(200, response.getStatusCodeValue());
    }
}
