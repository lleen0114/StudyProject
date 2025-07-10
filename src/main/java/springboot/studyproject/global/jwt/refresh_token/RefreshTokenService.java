package springboot.studyproject.global.jwt.refresh_token;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    /**
     * refresh token 저장
     * @param userEmail 유저 이메일
     * @param sessionId 세션 id
     * @param refreshToken 저장할 refreshToken
     * @param ttlMillis 만료 시간
     */
    public void save(String userEmail, String sessionId, String refreshToken, long ttlMillis){
        LocalDateTime expiresAt = LocalDateTime
                .now()
                .plus(Duration.ofMillis(ttlMillis));

        refreshTokenRepository.save(
                RefreshToken.builder()
                        .userEmail(userEmail)
                        .sessionId(sessionId)
                        .refreshToken(refreshToken)
                        .expiresAt(expiresAt)
                        .build()
        );
    }

    /**
     * userEmail과 sessionId 조합으로 refreshToken 조회
     * @param userEmail token을 찾기 위한 userEmail
     * @param sessionId token을 찾기 위한 sessionId
     * @return 조회한 refreshToken
     */
    public String getRefreshToken(String userEmail, String sessionId){
        return refreshTokenRepository.findByUserEmailAndSessionId(userEmail, sessionId)
                .filter(token -> token.getExpiresAt().isAfter(LocalDateTime.now()))
                .map(RefreshToken::getRefreshToken)
                .orElse(null);
    }

    /**
     * 재발급을 위한 refreshToken 삭제
     * @param userEmail 삭제할 token을 찾기 위한 userEmail
     * @param sessionId 삭제할 token을 찾기 위한 sessionId
     */
    public void deleteRefreshToken(String userEmail, String sessionId){
        refreshTokenRepository.deleteByUserEmailAndSessionId(userEmail, sessionId);
    }

}
