package ahmed.com.springboot.friend_finder_system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditAwareImpl")
public class FriendFinderSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(FriendFinderSystemApplication.class, args);
    }

}
