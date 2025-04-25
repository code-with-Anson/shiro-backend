package com.shiro.main.utils.VerificationCode;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
public class InMemoryVerificationCodeStore implements VerificationCodeStore {
    private static final ConcurrentHashMap<String, VerifyCodeInfo> verifyCodeMap = new ConcurrentHashMap<>();
    // 定义Redis key的前缀，用于命名空间隔离
    private static final String VERIFY_CODE_PREFIX = "verify_code:";
    private static final String USER_ID_PREFIX = "verify_user:";
    // 验证码有效期5分钟
    private static final Duration VERIFY_CODE_TTL = Duration.ofMinutes(5);
    // 注入StringRedisTemplate来操作Redis
    private final StringRedisTemplate stringRedisTemplate;

    public void storeCode(String email, Long userId, String code) {
        // 使用hash结构存储验证码信息，方便存取
        String verifyCodeKey = VERIFY_CODE_PREFIX + email;
        String userIdKey = USER_ID_PREFIX + email;

        // 存储验证码和用户ID
        stringRedisTemplate.opsForValue().set(verifyCodeKey, code, VERIFY_CODE_TTL);
        stringRedisTemplate.opsForValue().set(userIdKey, String.valueOf(userId), VERIFY_CODE_TTL);
    }

    public boolean verifyAndRemove(String email, String code) {
        String verifyCodeKey = VERIFY_CODE_PREFIX + email;

        // 获取存储的验证码
        String storedCode = stringRedisTemplate.opsForValue().get(verifyCodeKey);

        // 验证码不存在或不匹配
        if (storedCode == null || !storedCode.equals(code)) {
            return false;
        }

        // 验证成功后删除验证码和关联的用户ID
        String userIdKey = USER_ID_PREFIX + email;
        stringRedisTemplate.delete(Arrays.asList(verifyCodeKey, userIdKey));

        return true;
    }

    public Long getUserIdByEmail(String email) {
        String userIdKey = USER_ID_PREFIX + email;
        String userId = stringRedisTemplate.opsForValue().get(userIdKey);
        return userId != null ? Long.valueOf(userId) : null;
    }

    @Data
    @AllArgsConstructor
    private static class VerifyCodeInfo {
        private Long userId;
        private String code;
        private Long expireTime;
    }
}