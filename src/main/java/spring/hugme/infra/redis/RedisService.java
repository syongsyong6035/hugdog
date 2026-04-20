package spring.hugme.infra.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisService {

    private final StringRedisTemplate redisTemplate;

    // ========================
    // Redis Key 상수
    // ========================
    private static final String REFRESH_TOKEN_PREFIX = "auth:refresh:";

    // userId -> refreshToken 저장
    public void saveRefreshToken(String userId, String refreshToken) {
        redisTemplate.opsForValue()
            .set(buildKey(userId), refreshToken, 7, TimeUnit.DAYS);
    }

    public String getRefreshToken(String userId) {
        return redisTemplate.opsForValue().get(buildKey(userId));
    }

    public void deleteRefreshToken(String userId) {
        redisTemplate.delete(buildKey(userId));
    }

    // ========================
    // Key 생성 헬퍼
    // ========================
    private String buildKey(String userId) {
        return REFRESH_TOKEN_PREFIX + userId;
    }

}
