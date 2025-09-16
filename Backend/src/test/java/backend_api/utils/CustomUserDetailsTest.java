package backend_api.utils;

import backend_api.entities.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CustomUserDetailsTest {

    @Test
    void getId() {
        User user = new User();
        user.setId(1L);

        CustomUserDetails userDetails = new CustomUserDetails(user);
        assertEquals(1L, userDetails.getId());
    }

    @Test
    void getUsername() {
        User user = new User();
        user.setUsername("test");
        CustomUserDetails userDetails = new CustomUserDetails(user);
        assertEquals("test", userDetails.getUsername());
    }

    @Test
    void getPassword() {
        User user = new User();
        user.setPassword("password");
        CustomUserDetails userDetails = new CustomUserDetails(user);
        assertEquals("password", userDetails.getPassword());
    }

    @Test
    void getAuthorities() {
        User user = new User();
        CustomUserDetails userDetails = new CustomUserDetails(user);
        assertNull(userDetails.getAuthorities());
    }

    @Test
    void isAccountNonExpired() {
        User user = new User();
        CustomUserDetails userDetails = new CustomUserDetails(user);
        assertTrue(userDetails.isAccountNonExpired());
    }

    @Test
    void isAccountNonLocked() {
        User user = new User();
        CustomUserDetails userDetails = new CustomUserDetails(user);
        assertTrue(userDetails.isAccountNonLocked());
    }

    @Test
    void isCredentialsNonExpired() {
        User user = new User();
        CustomUserDetails userDetails = new CustomUserDetails(user);
        assertTrue(userDetails.isCredentialsNonExpired());
    }

    @Test
    void isEnabled() {
        User user = new User();
        CustomUserDetails userDetails = new CustomUserDetails(user);
        assertTrue(userDetails.isEnabled());
    }
}