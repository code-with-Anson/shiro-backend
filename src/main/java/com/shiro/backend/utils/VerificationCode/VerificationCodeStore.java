package com.shiro.backend.utils.VerificationCode;

public interface VerificationCodeStore {
    void storeCode(String email, Long userId, String code);

    boolean verifyAndRemove(String email, String code);

    Long getUserIdByEmail(String email);
}