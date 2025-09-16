package backend_api.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

    @Test
    void constructor() throws Exception {
        Constructor<JwtUtil> constructor = JwtUtil.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        InvocationTargetException thrown = assertThrows(InvocationTargetException.class, constructor::newInstance);
        assertTrue(thrown.getCause() instanceof IllegalStateException);
        assertEquals("Utility class", thrown.getCause().getMessage());
    }

    @Test
    void generateToken() {
        String token = JwtUtil.generateToken("testUser");
        assertNotNull(token);
    }

    @Test
    void getUsernameFromToken() {
        String username = "test";
        String token = JwtUtil.generateToken(username);
        String parsedUsername = JwtUtil.getUsernameFromToken(token);
        assertEquals(username, parsedUsername);
    }

    @Test
    void isTokenValid() throws InterruptedException {
        String token = JwtUtil.generateToken("test");
        assertTrue(JwtUtil.isTokenValid(token));

        String invalidToken = token + "invalid";
        assertFalse(JwtUtil.isTokenValid(invalidToken));

        Thread.sleep(1);
        assertTrue(JwtUtil.isTokenValid(token));

    }

    @Test
    void isTokenValidExpired() {
        String expiredToken = Jwts.builder()
                .setSubject("test")
                .setIssuedAt(new Date(System.currentTimeMillis() - 10000))
                .setExpiration(new Date(System.currentTimeMillis() - 5000))
                .signWith(Keys.hmacShaKeyFor("JABNGFIJBNADIPGBNIADBNGIPBADIHGBIAPDNFANJSODJOIA!#%¤!#%&/()=?`´+*~^<>".getBytes()), SignatureAlgorithm.HS256)
                .compact();

        assertFalse(JwtUtil.isTokenValid(expiredToken));
    }

    @Test
    void isTokenValidFormed() {
        String invalidToken = "invalid.token.value";
        assertFalse(JwtUtil.isTokenValid(invalidToken));
    }

}