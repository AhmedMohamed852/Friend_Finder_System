package ahmed.com.springboot.friend_finder_system.handelMessages;

import ahmed.com.springboot.friend_finder_system.helper.MessageResponse;
import ahmed.com.springboot.friend_finder_system.service.impl.BundleMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class HandelMessages {

    private BundleMessageService bundleMessageService;

    @Autowired
    HandelMessages(BundleMessageService bundleMessageService)
    {
        this.bundleMessageService = bundleMessageService;
    }



    @ExceptionHandler(Exception.class)
    public ResponseEntity<MessageResponse> handleException(Exception e){
        return ResponseEntity.badRequest().body(bundleMessageService.getMessageResponse(e.getMessage()));
    }



    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<MessageResponse>> handleRunTimeException(MethodArgumentNotValidException exception){


        List<MessageResponse> errors = new ArrayList<>();

        exception.getBindingResult().getFieldErrors().stream().forEach(e ->
        {
            String message = e.getDefaultMessage();
            errors.add(bundleMessageService.getMessageResponse(message));
        });


        return ResponseEntity.badRequest().body(errors) ;
    }
}
