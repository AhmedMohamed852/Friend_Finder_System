package ahmed.com.springboot.friend_finder_system.bundleMessages;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ResourceBundleMessageSource;

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
}
