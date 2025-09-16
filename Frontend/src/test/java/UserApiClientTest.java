import dto.LoginResponse;
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
        userApiClient = new UserApiClient();
    }

    @Test
    public void postUserTest() {
        user.setUsername("Testopipp");
        user.setEmail("test@hotmail.fi");
        user.setPassword("1234");
        userApiClient.registerUser(user);

    }

    @Test
    public void loginUserTest() {
        loginRequest = new LoginRequest("test", "1234");

        User user = userApiClient.loginUser(loginRequest);

        System.out.println("Username: " + user.getUsername());
        System.out.println("Id: " + user.getId());
        System.out.println("JWT Token: " + user.getToken());
    }



}
