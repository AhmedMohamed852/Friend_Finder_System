package ahmed.com.springboot.friend_finder_system.dto.DtoSimble;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class FriendShipRequestsDto {

    private Long Friendship_Id;

    private Long userSenderId; // not me

    private String profilePicture;

    private String firstName;
    private String last_Name;
}
