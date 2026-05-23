package ahmed.com.springboot.friend_finder_system.bundleMessages;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

public class BundleMessages {

    @Value("${spring.messages.basename}")
    String baseName;


    @Bean
    public ResourceBundleMessageSource getMessageSource(){
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();

        messageSource.setBasenames(baseName);
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

    @Configuration
    public static class SecurityConfig {




        @Bean
        public PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }

        @Bean
        public SecurityFilterChain securityFilterChain (HttpSecurity http) throws Exception {

            http.csrf(csrf -> csrf.disable());
            http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
            http.formLogin(form -> form.disable());

            http.httpBasic(Customizer.withDefaults());
            http.authorizeHttpRequests(authorize -> authorize.requestMatchers("/api/auth/**").permitAll().anyRequest().authenticated());


            return http.build();
        }
    }
}
