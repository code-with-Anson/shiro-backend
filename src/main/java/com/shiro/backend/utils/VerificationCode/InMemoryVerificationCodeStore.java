package com.shiro.backend.utils.VerificationCode;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

@Component
public class InMemoryVerificationCodeStore implements VerificationCodeStore {
    private static final ConcurrentHashMap<String, VerifyCodeInfo> verifyCodeMap = new ConcurrentHashMap<>();
    private static final long VERIFY_CODE_TTL = 5 * 60 * 1000; // 5分钟

    public void storeCode(String email, Long userId, String code) {
        long expireTime = System.currentTimeMillis() + VERIFY_CODE_TTL;
        verifyCodeMap.put(email, new VerifyCodeInfo(userId, code, expireTime));
        scheduleCleanup(email, expireTime);
    }

    public boolean verifyAndRemove(String email, String code) {
        VerifyCodeInfo info = verifyCodeMap.get(email);
        if (info == null || System.currentTimeMillis() > info.getExpireTime()) {
            verifyCodeMap.remove(email);
            return false;
        }

        if (!code.equals(info.getCode())) {
            return false;
        }

        verifyCodeMap.remove(email);
        return true;
    }

    public Long getUserIdByEmail(String email) {
        VerifyCodeInfo info = verifyCodeMap.get(email);
        return info != null ? info.getUserId() : null;
    }

    private void scheduleCleanup(String email, long expireTime) {
        new Thread(() -> {
            try {
                Thread.sleep(VERIFY_CODE_TTL);
                VerifyCodeInfo info = verifyCodeMap.get(email);
                if (info != null && info.getExpireTime() <= expireTime) {
                    verifyCodeMap.remove(email);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();
    }

    @Data
    @AllArgsConstructor
    private static class VerifyCodeInfo {
        private Long userId;
        private String code;
        private Long expireTime;
    }
}