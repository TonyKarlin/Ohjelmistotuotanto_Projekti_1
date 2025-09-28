package backend_api.DTOs.user;

public record UserWithTokenDTO(String token, UserDTO user) {
}
