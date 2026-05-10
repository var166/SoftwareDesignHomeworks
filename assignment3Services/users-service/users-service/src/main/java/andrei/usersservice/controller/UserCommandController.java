package andrei.usersservice.controller;

import andrei.usersservice.dto.UserDto;
import andrei.usersservice.service.UserCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserCommandController {

    private final UserCommandService userCommandService;

    @PostMapping
    public void register(@RequestBody UserDto.UserSignUpRequest userSignUpRequest) {
        userCommandService.register(
                userSignUpRequest.username(),
                userSignUpRequest.email(),
                userSignUpRequest.password()
        );
    }

    @PutMapping("/updateUserRole")
    public void updateUserRole(@RequestParam Long id, @RequestParam String userRole) {
        userCommandService.updateUserRole(id, userRole.toUpperCase());
    }

    @PutMapping("/updatePassword")
    public void updatePassword(@RequestParam Long id, @RequestParam String password) {
        userCommandService.updatePassword(id, password);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteById(@PathVariable Long id) {
        userCommandService.delete(id);
    }
}