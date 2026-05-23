package ahmed.com.springboot.friend_finder_system.helper;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@Getter
@Setter
@ConfigurationProperties(prefix = "token")
public class JwtToken {

    private String secret;
    private Duration expiration;
}
