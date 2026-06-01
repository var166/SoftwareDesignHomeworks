package andrei.usersservice.controller;

import andrei.usersservice.command.*;
import andrei.usersservice.dto.UserDto;
import andrei.usersservice.service.IUserCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserCommandController {

    private final IUserCommandService userCommandService;
    private final CommandInvoker commandInvoker;

    @PostMapping
    public void register(@RequestBody UserDto.UserSignUpRequest userSignUpRequest) {
        commandInvoker.execute(new RegisterUserCommand(
                userCommandService,
                userSignUpRequest.username(),
                userSignUpRequest.email(),
                userSignUpRequest.password()
        ));
    }

    @PutMapping("/updateUserRole")
    public void updateUserRole(@RequestParam Long id, @RequestParam String userRole) {
        commandInvoker.execute(new UpdateUserRoleCommand(userCommandService, id, userRole.toUpperCase()));
    }

    @PutMapping("/updatePassword")
    public void updatePassword(@RequestParam Long id, @RequestParam String password) {
        commandInvoker.execute(new UpdateUserPasswordCommand(userCommandService, id, password));
    }

    @DeleteMapping("/delete/{id}")
    public void deleteById(@PathVariable Long id) {
        commandInvoker.execute(new DeleteUserCommand(userCommandService, id));
    }
}
