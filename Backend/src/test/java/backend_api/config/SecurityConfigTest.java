package backend_api.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Answers.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;

@SpringBootTest
class SecurityConfigTest {

    @Autowired
    private ApplicationContext context;

    @Test
    void testPasswordEncoder() {
        PasswordEncoder encoder = context.getBean(PasswordEncoder.class);
        assertNotNull(encoder);
        String pass = "test";
        String encoded = encoder.encode(pass);
        assertTrue(encoder.matches(pass, encoded));
    }

    @Test
    void filterChain() throws Exception {
        SecurityConfig config = new SecurityConfig();
        HttpSecurity http = mock(HttpSecurity.class, RETURNS_DEEP_STUBS);
        SecurityFilterChain filterChain = config.filterChain(http);
        assertNotNull(filterChain);
    }
}