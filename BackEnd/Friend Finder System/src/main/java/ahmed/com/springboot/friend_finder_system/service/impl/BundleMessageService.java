package ahmed.com.springboot.friend_finder_system.service.impl;

import ahmed.com.springboot.friend_finder_system.helper.MessageResponse;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@NoArgsConstructor
public class BundleMessageService {


    private ResourceBundleMessageSource bundleMessageSource;

    @Autowired
    BundleMessageService(ResourceBundleMessageSource bundleMessageSource)
    {
        this.bundleMessageSource = bundleMessageSource;
    }


    public String getMessage_ar(String key){return bundleMessageSource.getMessage(key ,null ,new Locale("ar"));}
    public String getMessage_en(String key){return bundleMessageSource.getMessage(key ,null ,new Locale("en"));}

    public MessageResponse getMessageResponse(String key)
    {
        MessageResponse messageResponse = new MessageResponse();
        messageResponse.setMessage_ar(getMessage_ar(key));
        messageResponse.setMessage_en(getMessage_en(key));
        return messageResponse;
    }
}
