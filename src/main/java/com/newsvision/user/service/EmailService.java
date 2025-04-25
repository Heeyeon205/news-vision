package com.newsvision.user.service;

import com.newsvision.global.exception.CustomException;
import com.newsvision.global.exception.ErrorCode;
import com.newsvision.user.entity.EmailVerification;
import com.newsvision.user.repository.EmailVerificationRepository;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;
    private final EmailVerificationRepository repository;

    // 인증 코드 생성 -> DB 저장 -> 이메일 전송
    public void sendVerificationCode(String email) {
        String code = generateCode();
        EmailVerification verification = new EmailVerification(email, code, 10);
        repository.save(verification);

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(email);
            helper.setFrom("procross27@gmail.com");
            helper.setSubject("[NewsVision] 이메일 인증 코드");
            helper.setText("인증 코드는 다음과 같습니다: " + code, false);

            mailSender.send(message);
        } catch (Exception e) {
            System.out.println("메일 전송 실패: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // 인증 코드 생성
    private String generateCode() {
        return String.valueOf((int) (Math.random() * 900000) + 100000);  // 6자리 숫자
    }

    // 입력 코드 검증 -> 성공 시 DB 에서 코드 삭제
    public void verifyCode(String email, String inputCode) {
        EmailVerification verification = repository.findById(email)
                .orElseThrow(() -> new CustomException(ErrorCode.VERIFICATION_NOT_FOUND));

        if (verification.isExpired()) {
            throw new CustomException(ErrorCode.VERIFICATION_EXPIRED);
        }

        if (!verification.matchCode(inputCode)) {
            throw new CustomException(ErrorCode.INVALID_VERIFICATION_CODE);
        }

        // 인증 성공 처리 (예: 이후 회원가입에서 사용 or 인증 상태 업데이트)
        repository.delete(verification); // 인증 완료 후 제거
    }

    // 인증 기록이 삭제되었는지 확인 (삭제 되었으면 인증 완료)
    public boolean isVerified(String email) {
        return repository.findById(email).isEmpty(); // ture 면 삭제 완료
    }
}
