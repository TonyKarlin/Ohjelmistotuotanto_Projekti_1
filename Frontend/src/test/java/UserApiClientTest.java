import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import request.LoginRequest;
import service.UserApiClient;


import java.net.MalformedURLException;

public class UserApiClientTest {

    private User user;
    private LoginRequest loginRequest;
    private UserApiClient userApiClient;


    @BeforeEach
    public void setUp() throws MalformedURLException {
        user = new User();
        loginRequest = new LoginRequest();
        userApiClient = Mockito.spy(new UserApiClient());
    }

    @Test
    public void postUserTest() {
        user.setUsername("Test");
        user.setEmail("test@hotmail.fi");
        user.setPassword("1234");
        userApiClient.registerUser(user);


    }



}
