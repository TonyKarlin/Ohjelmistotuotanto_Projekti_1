
// import java.net.MalformedURLException;

// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;

// import model.User;
// import request.LoginRequest;
// import service.UserApiClient;

// public class UserApiClientTest {

//     private User user;
//     private LoginRequest loginRequest;
//     private UserApiClient userApiClient;

//     @BeforeEach
//     public void setUp() throws MalformedURLException {
//         user = new User();
//         loginRequest = new LoginRequest();
//         userApiClient = new UserApiClient();
//     }

//     @Test
//     public void postUserTest() {
//         user.setUsername("Testopipp");
//         user.setEmail("test@hotmail.fi");
//         user.setPassword("1234");
//         userApiClient.registerUser(user);

//     }

//     @Test
//     public void loginUserTest() {
//         loginRequest = new LoginRequest("pekkaaa", "1234");
//         User loggedInUser = userApiClient.loginUser(loginRequest);
//         System.out.println("Username: " + loggedInUser.getUsername());
//         System.out.println("Id: " + loggedInUser.getId());
//         System.out.println("JWT Token: " + loggedInUser.getToken());
//     }

// }
