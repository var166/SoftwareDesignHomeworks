package andrei.usersservice.dto;

import andrei.usersservice.model.User;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class UserDto {
    public record UserLoginRequest(String username, String password) {}
    public record UserSignUpRequest(String username, String email, String password) {}
    public record LoginResponse(String token) {}
    public record UserResponse(long id, String username, String email, String userRole) {
        public static UserResponse from(User user) {
            return new UserResponse(user.getId(), user.getUsername(), user.getEmail(), user.getUserRole().toString());
        }
    }
}
