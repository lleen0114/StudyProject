package springboot.studyproject.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import springboot.studyproject.domain.user.entity.Users;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Users, Integer> {
    Optional<Users> findByUserEmail(String userEmail);
}
