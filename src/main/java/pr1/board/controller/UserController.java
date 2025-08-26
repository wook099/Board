package pr1.board.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
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
    public ResponseEntity<String> login(@RequestBody LoginRequestDto loginRequestDto, HttpServletResponse response) {

        User user = userService.login(loginRequestDto);
        String token = jwtUtil.generateToken(user.getUsername());

//        ResponseCookie cookie = ResponseCookie.from("JWT", token)
//                .httpOnly(true)
//                .secure(false)  // 개발용
//                .path("/")
//                .maxAge(3600)
//                .sameSite("Lax") // ✅ Spring Framework ResponseCookie 지원
//                .build();
//        response.setHeader("Set-Cookie", cookie.toString());

        return ResponseEntity.ok(token);
    }
}
