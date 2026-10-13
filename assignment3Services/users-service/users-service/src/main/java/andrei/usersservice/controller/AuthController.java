package andrei.usersservice.controller;

import andrei.usersservice.dto.UserDto;
import andrei.usersservice.security.JwtUtil;
import andrei.usersservice.service.UserQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class AuthController {

    private final UserQueryService userQueryService;
    private final JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<UserDto.LoginResponse> login(@RequestBody UserDto.UserLoginRequest request) {
        return userQueryService.authenticate(request.username(), request.password())
                .map(user -> ResponseEntity.ok(new UserDto.LoginResponse(jwtUtil.generateToken(user))))
                .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }
}
