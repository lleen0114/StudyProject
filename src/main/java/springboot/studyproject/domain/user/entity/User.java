package springboot.studyproject.domain.user.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "user_nickname")
    private String userNickname;

    @Column(name = "user_email")
    private String userEmail;

    @Column(name = "reg_date")
    @CreationTimestamp
    private LocalDateTime regDate;

    @Column(name = "del_date")
    private LocalDateTime delDate;

    @Column(name = "provider")
    private String provide;

    @Builder
    public User(String userEmail, String userNickname){
        this.userEmail = userEmail;
        this.userNickname = userNickname;
    }

    //새로운 유저 만드는 메소드
    public static User createNewUser(String userEmail, String userNickname){
        User user = new User();
        user.userNickname = userNickname;
        user.userEmail = userEmail;

        return user;
    }

    //닉네임 변경 메소드
    public void modifyNickname(String newNickname){
        this.userNickname = newNickname;
    }
}
