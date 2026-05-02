package ahmed.com.springboot.friend_finder_system.Controller;

import ahmed.com.springboot.friend_finder_system.dto.NotificationDto;
import ahmed.com.springboot.friend_finder_system.service.Notification_Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationsController {

    //TODO: Declare Service Methods

    private final Notification_Service notification_Service;


    //TODO:_______________ Implement Service Methods ____________________________



    //TODO:_______________ Get User Notification ____________________________

    @GetMapping("/getUserNotifications/{id}")
    public ResponseEntity<List<NotificationDto>> getUserNotifications(@PathVariable Long id)
    {
        return ResponseEntity.ok(notification_Service.getUserNotifications(id));
    }
}
