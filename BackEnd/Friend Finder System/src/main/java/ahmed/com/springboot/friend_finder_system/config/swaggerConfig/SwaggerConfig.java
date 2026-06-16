package ahmed.com.springboot.friend_finder_system.config.swaggerConfig;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Friend Finder System Application",
                version = "1.0",
                description = "Friend Finder System REST APIs",
                contact = @Contact(
                        name = "Ahmed Mohamed El-Bahiry",
                        email = "ahmed.mohammed.swe1@gmail.com",
                        url = "https://www.linkedin.com/in/ahmed-el-bahiry/"
                ),
                license = @License(
                        name = "Friend Finder",
                        url = "http://localhost:4200"
                )
        )
)
public class SwaggerConfig {
}
