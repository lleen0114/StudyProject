package springboot.studyproject.domain.user.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.CurrentTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "user_nickname")
    private String userNickname;

    @Column(name = "user_email")
    private String userEmail;

    @Column(name = "reg_date")
    @CreationTimestamp
    private LocalDateTime regDate;

    @Column(name = "del_date")
    private LocalDateTime delDate;

    @Builder
    public Users(String userEmail, String userNickname){
        this.userEmail = userEmail;
        this.userNickname = userNickname;
    }

    //새로운 유저 만드는 메소드
    public static Users createNewUser(String userEmail, String userNickname){
        Users user = new Users();
        user.userNickname = userNickname;
        user.userEmail = userEmail;

        return user;
    }

    //닉네임 변경 메소드
    public void modifyNickname(String newNickname){
        this.userNickname = newNickname;
    }
}
