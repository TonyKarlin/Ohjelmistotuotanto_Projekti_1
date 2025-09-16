package service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

// Interface jota pystyy hyödyntää kaikissa service luokan api pyynnöissä.

public interface ApiClient {

    ObjectMapper objectMapper = new ObjectMapper();


    //Method needs only a right url and object as parameters
    default HttpResponse sendPostRequest(String urlString, Object body) throws IOException {

        // Opens connection to the backend endpoint
        URL url = new URL(urlString);
        HttpURLConnection http = (HttpURLConnection) url.openConnection();

        // POST request
        http.setRequestMethod("POST");
        http.setDoOutput(true);
        http.setRequestProperty("Content-Type", "application/json");

        // Convert our Java  object into a JSON string
        String json = objectMapper.writeValueAsString(body);

        //    Write the JSON to the request body and
        //    OutputStream sends data to the server
        try (OutputStream os = http.getOutputStream()) {
            os.write(json.getBytes("utf-8"));
        }
        int code = http.getResponseCode();
        String responseBody = readResponse(http);
        return new HttpResponse(code, responseBody);
    }

    default String sendGetRequest(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection http = (HttpURLConnection) url.openConnection();
        http.setRequestMethod("GET");
        return readResponse(http);
    }

//    default String sendGetRequestById() {
//        URL url = new URL(urlString);
//        HttpURLConnection http = (HttpURLConnection) url.openConnection();
//        http.setRequestMethod("GET");
//        return readResponse(http);
//    }

    default String readResponse(HttpURLConnection http) throws IOException {
        // Get the HTTP response code
        int code = http.getResponseCode();
        InputStream stream;

        // checks the response code
        if (code >= 200 && code < 300) {
            stream = http.getInputStream();
        } else {
            stream = http.getErrorStream();
            if (stream == null) {
                return "Error: Server returned HTTP " + code + " but no message body";
            }
        }

        //  Read the server response (body)
        //  InputStream reads data sent back from the server
        try (BufferedReader br = new BufferedReader(new InputStreamReader(stream, "utf-8"))) {
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                response.append(line.trim());
            }
            return response.toString();
        }
    }


}
