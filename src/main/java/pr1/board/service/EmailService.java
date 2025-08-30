package pr1.board.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendAuthCode(String toEmail, String code) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setFrom("codnrud99@gmail.com");
        message.setSubject("비밀번호 재설정 인증번호");
        message.setText("인증번호: " + code + "\n유효시간: 5분");

        mailSender.send(message);
    }
}
