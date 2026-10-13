package andrei.usersservice.controller;

import andrei.usersservice.dto.UserDto;
import andrei.usersservice.model.UserRole;
import andrei.usersservice.service.UserQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserQueryController {

    private final UserQueryService userQueryService;

    @GetMapping("/findById/{id}")
    public UserDto.UserResponse getUserById(@PathVariable Long id) {
        return UserDto.UserResponse.from(userQueryService.findById(id));
    }

    @GetMapping("/findAll")
    public List<UserDto.UserResponse> getAllUsers(
            @RequestParam(required = false) String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {
        if (sortBy != null) {
            return userQueryService.findAllSorted(sortBy, direction).stream()
                    .map(UserDto.UserResponse::from).toList();
        }
        return userQueryService.findAll().stream()
                .map(UserDto.UserResponse::from).toList();
    }

    @GetMapping("/findByUsername/{username}")
    public UserDto.UserResponse getUserByUsername(@PathVariable String username) {
        return UserDto.UserResponse.from(userQueryService.findByUsername(username));
    }

    @GetMapping("/findByEmail")
    public UserDto.UserResponse getUserByEmail(@RequestParam String email) {
        return UserDto.UserResponse.from(userQueryService.findByEmail(email));
    }

    @GetMapping("/findByRole")
    public List<UserDto.UserResponse> getUsersByRole(
            @RequestParam String role,
            @RequestParam(required = false) String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {
        UserRole userRole = UserRole.valueOf(role.toUpperCase());
        if (sortBy != null) {
            return userQueryService.findByRoleSorted(userRole, sortBy, direction).stream()
                    .map(UserDto.UserResponse::from).toList();
        }
        return userQueryService.findByRole(userRole).stream()
                .map(UserDto.UserResponse::from).toList();
    }
}
