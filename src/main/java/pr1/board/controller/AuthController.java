package pr1.board.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pr1.board.dto.Auth.EmailRequestDto;
import pr1.board.dto.Auth.ResetPasswordRequestDto;
import pr1.board.dto.Auth.TempTokenResponseDto;
import pr1.board.dto.Auth.VerifyCodeRequestDto;
import pr1.board.service.AuthService;
import pr1.board.service.EmailService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final EmailService emailService;

    @PostMapping("/send-code")
    public ResponseEntity<String> sendCode(@RequestBody EmailRequestDto request) {
        String code = authService.generateAuthCode(request.getEmail());
        emailService.sendAuthCode(request.getEmail(), code);
        return ResponseEntity.ok("인증번호 전송 완료");
    }


    @PostMapping("/verify-code")
    public ResponseEntity<TempTokenResponseDto> verifyCode(@RequestBody VerifyCodeRequestDto request) {
        authService.verifyAuthCode(request.getEmail(), request.getCode());
        String tempToken = authService.generateTempToken(request.getEmail());// 토큰 생성

        return ResponseEntity.ok(new TempTokenResponseDto(tempToken));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordRequestDto request) {
        String email = authService.verifyTempToken(request.getTempToken());//토큰으로 이메일 가져오고 토큰삭제
        authService.updatePassword(email, request.getNewPassword());

        return ResponseEntity.ok("비밀번호 재설정 완료");
    }
}
