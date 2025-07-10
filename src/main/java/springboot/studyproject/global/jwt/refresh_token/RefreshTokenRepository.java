package springboot.studyproject.global.jwt.refresh_token;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Integer> {
    Optional<RefreshToken> findByUserEmailAndSessionId(String userEmail, String sessionId);
    void deleteByUserEmailAndSessionId(String userEmail, String sessionId);
}
