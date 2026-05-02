package ahmed.com.springboot.friend_finder_system.dto;

import ahmed.com.springboot.friend_finder_system.eNum.FriendshipStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class FriendshipDto {

    private Long id;

    @NotNull(message = "User1 Id is required")
    private Long user1;

    @NotNull(message = "User2 Id is required")
    private Long user2;


//    @NotNull(message = "Status is required")
    private FriendshipStatus status;



}
