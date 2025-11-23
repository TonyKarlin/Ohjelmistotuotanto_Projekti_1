package backend_api.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;

import backend_api.dto.user.UserDTO;
import backend_api.dto.user.UserWithTokenDTO;
import backend_api.entities.User;

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
