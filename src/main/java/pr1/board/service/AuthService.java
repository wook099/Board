package pr1.board.service;

import java.security.SecureRandom;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import pr1.board.entity.User;
import pr1.board.exceptionhandler.exception.AuthCodeSendException;
import pr1.board.exceptionhandler.exception.AuthCodeExpiredException;
import pr1.board.repository.UserRepository;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final StringRedisTemplate redisTemplate;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public String generateAuthCode(String email) {//인증번호 생성 5분
        userRepository.findByEmail(email)
                .orElseThrow(() -> new AuthCodeSendException("등록되지 않은 이메일입니다."));

        try {
            SecureRandom random = new SecureRandom();
            String code = String.format("%06d", random.nextInt(1000000)); // 6자리 숫자
            redisTemplate.opsForValue().set("pwd_reset:" + email, code, 5, TimeUnit.MINUTES);
            return code;
        } catch (Exception e) {
            throw new AuthCodeSendException("인증번호 생성 실패: " + e.getMessage());
        }
    }

    public void verifyAuthCode(String email, String inputCode) {// 인증번호 검증

        String savedCode = redisTemplate.opsForValue().get("pwd_reset:" + email);//이메일 키값 레디스에 가져옴

        if (savedCode == null) {
            throw new AuthCodeExpiredException("인증번호가 만료되었거나 존재하지 않습니다.");
        }

        if (!savedCode.equals(inputCode)) {
            throw new IllegalArgumentException("잘못된 인증번호입니다.");
        }

        redisTemplate.delete("pwd_reset:" + email);
    }

    public String generateTempToken(String email) {// 임시 토큰생성
        String token = java.util.UUID.randomUUID().toString();
        redisTemplate.opsForValue().set("pwd_reset_token:" + token, email, 5, TimeUnit.MINUTES);
        return token;
    }

    public String verifyTempToken(String token) {//토큰 검증
        String email = redisTemplate.opsForValue().get("pwd_reset_token:" + token);

        if (email == null) {
            throw new AuthCodeExpiredException("임시 토큰이 만료되었거나 유효하지 않습니다.");
        }
        redisTemplate.delete("pwd_reset_token:" + token); // 재사용 방지
        return email;
    }

    public void updatePassword(String email, String newPassword) {// 비번 업데이트
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        String encodedPassword = passwordEncoder.encode(newPassword); // 암호화
        user.setPassword(encodedPassword);

        userRepository.save(user);
    }
}

