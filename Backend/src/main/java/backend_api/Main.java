package backend_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
        System.out.println("Server running at: http://localhost:8080");
        System.out.println("User endpoint at: http://localhost:8080/api/users");
    }
}
