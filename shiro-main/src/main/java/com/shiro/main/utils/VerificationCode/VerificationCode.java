package com.shiro.main.utils.VerificationCode;

import com.shiro.main.utils.EmailSender;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Random;

// 验证码管理服务
@Component
@RequiredArgsConstructor
public class VerificationCode {
    private final VerificationCodeStore codeStore;
    private final EmailSender emailSender;

    public String generateAndSendCode(String email, Long userId) {
        String code = generateVerifyCode();
        codeStore.storeCode(email, userId, code);
        emailSender.sendVerifyCode(email, code);
        return code;
    }

    public boolean verifyCode(String email, String code) {
        return codeStore.verifyAndRemove(email, code);
    }

    private String generateVerifyCode() {
        Random random = new Random();
        return String.format("%06d", random.nextInt(1000000));
    }
}