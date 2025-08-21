package pr1.board.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pr1.board.config.jwt.JwtUtil;
import pr1.board.dto.LoginRequestDto;
import pr1.board.dto.SignupRequestDto;
import pr1.board.entity.User;
import pr1.board.service.UserDetailsImpl;
import pr1.board.service.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupRequestDto signupRequestDto){

        User user = userService.signup(signupRequestDto);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequestDto loginRequestDto) {

        User user = userService.login(loginRequestDto);
        String token = jwtUtil.generateToken(user.getUsername());

        return ResponseEntity.ok(token); // 로그인 성공 시 User 정보 반환
    }
}
