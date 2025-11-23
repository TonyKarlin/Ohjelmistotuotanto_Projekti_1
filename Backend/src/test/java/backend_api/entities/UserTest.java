package backend_api.entities;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

class UserTest {

    @Test
    void prePersist() {
        User user = new User("test", "test", "test@test.com");
        assertNull(user.getCreatedAt());
        user.prePersist();
        assertNotNull(user.getCreatedAt());
        assertTrue(user.getCreatedAt().isBefore(LocalDateTime.now().plusSeconds(1)));
    }

    @Test
    void getId() {
        User user = new User("test", "test", "test@test.com");
        user.setId(1L);
        assertEquals(1L, user.getId());
    }

    @Test
    void setId() {
        User user = new User("test", "test", "test@test.com");
        user.setId(1L);
        assertEquals(1L, user.getId());
    }

    @Test
    void getUsername() {
        User user = new User("test", "test", "test@test.com");
        user.setUsername("newUsername");
        assertEquals("newUsername", user.getUsername());
    }

    @Test
    void setUsername() {
        User user = new User("test", "test", "test@test.com");
        user.setUsername("newUsername");
        assertEquals("newUsername", user.getUsername());
    }

    @Test
    void getPassword() {
        User user = new User("test", "test", "test@test.com");
        user.setPassword("Test1234");
        assertEquals("Test1234", user.getPassword());
    }

    @Test
    void setPassword() {
        User user = new User("test", "test", "test@test.com");
        user.setPassword("Test1234");
        assertEquals("Test1234", user.getPassword());
    }

    @Test
    void getEmail() {
        User user = new User("test", "test", "test@test.com");
        user.setEmail("Test1234@test.com");
        assertEquals("Test1234@test.com", user.getEmail());
    }

    @Test
    void setEmail() {
        User user = new User("test", "test", "test@test.com");
        user.setEmail("Test1234@test.com");
        assertEquals("Test1234@test.com", user.getEmail());
    }

    @Test
    void testCreatedAt() {
        User user = new User("test", "test", "test@test.com");
        LocalDateTime now = LocalDateTime.now();
        user.setCreatedAt(now);
        assertEquals(now, user.getCreatedAt());
    }
}
