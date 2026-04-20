package spring.hugme.infra.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import java.util.UUID;
import javax.crypto.SecretKey;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import spring.hugme.global.error.exceptions.AuthApiException;
import spring.hugme.global.response.ResponseCode;

@Component
@RequiredArgsConstructor
public class JwtProvider {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.access-token-validity}")
    private long accessTokenValidity;

    @Value("${jwt.refresh-token-validity}")
    private long refreshTokenValidity;

    /**
     * 전역 서명 키 조회
     */
    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Access Token 생성
     */
    public String generateAccessToken(String userId) {
        return generateToken(userId, accessTokenValidity);
    }

    /**
     * Refresh Token 생성
     */
    public String generateRefreshToken(String userId) {
        return generateToken(userId, refreshTokenValidity);
    }

    /**
     * 공통 토큰 생성 로직
     */
    private String generateToken(String userId, long validityInMilliseconds) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()
            .subject(userId)
            .issuedAt(now)
            .expiration(validity)
            .id(UUID.randomUUID().toString())
            .signWith(getSigningKey(), Jwts.SIG.HS256)
            .compact();
    }

    /**
     * Access Token 검증 및 userId 반환
     */
    public String validateAccessToken(String token) {
        try {
            return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();

        } catch (ExpiredJwtException e) {
            throw new AuthApiException(ResponseCode.TOKEN_EXPIRED);
        } catch (JwtException e) {
            throw new AuthApiException(ResponseCode.INVALID_ACCESS_TOKEN);
        }
    }

    /**
     * Refresh Token 검증 및 userId 반환
     */
    public String validateRefreshToken(String token) {
        try {
            return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();

        } catch (ExpiredJwtException e) {
            throw new AuthApiException(ResponseCode.REFRESH_TOKEN_EXPIRED);
        } catch (JwtException e) {
            throw new AuthApiException(ResponseCode.INVALID_TOKEN);
        }
    }
}
