package ahmed.com.springboot.friend_finder_system.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MessagesDto {

    private Long id;

    private String content;

    private boolean isRead;


    //______________relations_______________________________



}
