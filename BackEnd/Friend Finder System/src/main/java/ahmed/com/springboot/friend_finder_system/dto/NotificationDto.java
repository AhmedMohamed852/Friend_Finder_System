package ahmed.com.springboot.friend_finder_system.dto;

import ahmed.com.springboot.friend_finder_system.dto.DtoSimble.User_Simple_Dto;
import ahmed.com.springboot.friend_finder_system.eNum.NotificationType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class NotificationDto {

    private Long id;


    @NotBlank(message = "Content is required")
    private String content;


    private boolean isRead;

    @NotNull(message = "Type is required")
    private NotificationType type;

    //______________relations_______________________________

    @NotBlank(message = "triggered by id is required")
    private User_Simple_Dto triggeredBy;

/*    @NotBlank(message = "user id is required")
    private User_Simple_Dto user;*/


}
