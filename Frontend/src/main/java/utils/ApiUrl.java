package utils;

public class ApiUrl {

    private final static String API_URL = System.getenv("BACKEND_URL") != null
            ? System.getenv("BACKEND_URL") + "/api"
            : "http://localhost:8081/api";

    public static String getApiUrl() {
        return API_URL;
    }

}
