package springboot.studyproject.global.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

import javax.crypto.SecretKey;
import java.util.Date;

/**
 * Access Token & Refresh Token 발급 & 검증하는 service
 * accessToken 15분, refreshToken 14일
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Getter
public class JWTUtil {

    @Value("${jwt.secret}")
    private String secretKey;

    private final long accessTokenValidity = 1000 * 60 * 15;
    private final long refreshTokenValidity = 1000 * 60 * 60 * 24 * 14;

    /**
     * Base64로 인코딩 된 문자열을 디코딩해서 서명 키 생성하기
     * @return 서명키
     */
    private SecretKey getSigningKey(){
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * AccessToken 생성 메소드
     * @param userEmail 사용자 식별자
     * @param sessionId
     * @return jwt 토큰
     */
    private String createAccessToken(String userEmail, String sessionId) {
        return Jwts.builder()
                .setSubject(userEmail)
                .claim("sessionId", sessionId)
                .setIssuedAt(new Date())    //발급 시간
                .setExpiration(new Date(System.currentTimeMillis() + accessTokenValidity))  //만료시간
                .signWith(SignatureAlgorithm.HS256, secretKey)  //secret key로 서명
                .compact(); //최종적으로 토큰(문자열) 생성
    }

    /**
     * AccessToken 생성 메소드
     * @param userEmail 사용자 식별자
     * @param sessionId
     * @return jwt 토큰
     */
    private String createRefreshToken(String userEmail, String sessionId) {
        return Jwts.builder()
                .setSubject(userEmail)
                .claim("sessionId", sessionId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + refreshTokenValidity))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    /**
     * JWT의 유효성 검사(서명 & 만료 시간 검증)
     * @param token 유효성 검사할 token(string 형식)
     * @return 유효하다면 true, 아니면 false
     */
    public boolean isTokenValid(String token){
        try{
            Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(token);
            return true;
        } catch (JwtException e){
            return false;
        }
    }

    /**
     * Token에서 사용자 식별 데이터인 사용자 이메일 추출
     * 예외처리
     *  만료된 토큰인 경우에도 subject는 꺼낼 수 있음
     *  JwtException - 서명이 조작되거나 일치하지 않을 때, jwt 형식 자체가 잘못 되었을 때 에러 발생
     * @param token 이메일을 추출할 사용자의 토큰
     * @return 사용자 이메일
     */
    public String getUserName(String token){
        try{
            return Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .getSubject();
        }catch (ExpiredJwtException e){
            return e.getClaims().getSubject();
        }catch (JwtException e){
            throw new IllegalArgumentException("유효하지 않은 토큰입니다.", e);
        }
    }

    /**
     * Token에서 session Id 추출
     * 예외처리
     *  만료된 토큰인 경우에도 sesison Id는 꺼낼 수 있음
     *  JwtException - 서명이 조작되거나 일치하지 않을 때, jwt 형식 자체가 잘못 되었을 때 에러 발생
     * @param token sessionId를 추출할 사용자의 토큰
     * @return session Id
     */
    public String getSessionId(String token){
        try{
            Object sessionId = Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .get("sessionId");

            if (sessionId == null) {
                return null;
            }
            return sessionId.toString();

        }catch (ExpiredJwtException e){
            Object sessionId = e.getClaims().get("sessionId");
            if (sessionId == null) {
                return null;
            }
            return sessionId.toString();

        }catch (JwtException e){
            throw new IllegalArgumentException("유효하지 않은 JWT 토큰입니다.", e);
        }
    }
}
