package service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import com.fasterxml.jackson.databind.ObjectMapper;
import model.User;

public class UserApiClient {

    URL userUrl = new URL("http://localhost:8080/api/users/register");

    public UserApiClient() throws MalformedURLException {

    }

    public void registerUser(User user) {
        try {
            // Opens connection to the backend endpoint
            URLConnection con = userUrl.openConnection();
            HttpURLConnection http = (HttpURLConnection) con;

            // POST request
            http.setRequestMethod("POST");
            http.setDoOutput(true);
            http.setRequestProperty("Content-Type", "application/json");

            // Convert our Java User object into a JSON string
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(user);
            System.out.println("Sending JSON: " + json);

            //    Write the JSON to the request body and
            //    OutputStream sends data to the server
            try (OutputStream os = http.getOutputStream()) {
                os.write(json.getBytes("utf-8"));
            }

            // Get the HTTP response code
            int code = http.getResponseCode();
            System.out.println("Response code: " + code);


            //  Read the server response (body)
            //  InputStream reads data sent back from the server
            try (BufferedReader br = new BufferedReader(new InputStreamReader(http.getInputStream(), "utf-8"))) {
                String responseLine;
                StringBuilder response = new StringBuilder();

                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }

                System.out.println("Response: " + response.toString());
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
