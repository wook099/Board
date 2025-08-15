package pr1.board.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pr1.board.dto.LoginRequestDto;
import pr1.board.dto.SignupRequestDto;
import pr1.board.entity.User;
import pr1.board.service.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupRequestDto signupRequestDto){

        User user = userService.signup(signupRequestDto);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody LoginRequestDto loginRequestDto) {
        User user = userService.login(loginRequestDto);
        return ResponseEntity.ok(user); // 로그인 성공 시 User 정보 반환
    }
}
