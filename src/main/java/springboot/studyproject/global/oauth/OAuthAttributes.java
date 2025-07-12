package springboot.studyproject.global.oauth;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

@Getter
@AllArgsConstructor
public class OAuthAttributes {

    private final String userEmail;
    private final String userName;
    private final String provider;

    public static OAuthAttributes loginToGoogle(Map<String, Object> attributes){
        return new OAuthAttributes(
                (String) attributes.get("email"),
                (String) attributes.get("name"),
                "google"
        );
    }
}
