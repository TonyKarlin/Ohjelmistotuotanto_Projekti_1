package backend_api.DTOs;

import backend_api.DTOs.user.UserDTO;
import backend_api.DTOs.user.UserWithTokenDTO;
import backend_api.entities.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserWithTokenDTOTest {

    @Test
    void getToken() {
        UserWithTokenDTO response = new UserWithTokenDTO("Token", new UserDTO(new User()));
        assertEquals("Token", response.token());
    }

    @Test
    void getUser() {
        UserWithTokenDTO response = new UserWithTokenDTO("Token", new UserDTO(new User()));
        assertNotNull(response.user());
    }
}