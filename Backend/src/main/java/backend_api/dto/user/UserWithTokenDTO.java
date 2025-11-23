package backend_api.dto.user;

public record UserWithTokenDTO(String token, UserDTO user) {

}
