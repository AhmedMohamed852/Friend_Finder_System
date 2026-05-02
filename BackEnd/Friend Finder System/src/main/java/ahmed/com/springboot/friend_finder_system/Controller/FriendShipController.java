package ahmed.com.springboot.friend_finder_system.Controller;


import ahmed.com.springboot.friend_finder_system.dto.DtoSimble.FriendShipRequestsDto;
import ahmed.com.springboot.friend_finder_system.dto.FriendshipDto;
import ahmed.com.springboot.friend_finder_system.service.Friendship_Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/friendship")
@RequiredArgsConstructor
public class FriendShipController {

    //TODO: Declare Service Methods

    private final Friendship_Service friendship_service;



    //TODO:_______________ Implement Service Methods ____________________________





    //TODO:_______________ Send Friend Request ____________________________

    @PostMapping("/send-FriendRequest")
    public ResponseEntity<Void> sendFriendRequest(@RequestBody FriendshipDto friendshipDto)
    {
        friendship_service.sendFriendRequest(friendshipDto);
        return ResponseEntity.noContent().build();
    }

    //TODO:_______________ Get My Friendship Requests ____________________________
    @GetMapping("/show_FriendshipRequests/{Friendship_Id}")
    public ResponseEntity<List<FriendShipRequestsDto>> showFriendshipRequests(@PathVariable Long Friendship_Id)
    {
        return ResponseEntity.ok(friendship_service.getFriendshipsByUser1Id(Friendship_Id));
    }




    //TODO:_______________ Accept Friendship Request ____________________________
    @PutMapping("/accept-FriendRequest/{friendship_Id}")
    public ResponseEntity<Void> acceptFriendRequest(@PathVariable Long friendship_Id)
    {
        friendship_service.acceptFriendRequest(friendship_Id);
        return ResponseEntity.noContent().build();
    }




    //TODO:_______________ Rejected Friendship Request ____________________________
    @PutMapping("/reject-FriendRequest/{friendship_Id}")
    public ResponseEntity<Void> rejectFriendRequest(@PathVariable Long friendship_Id)
    {
        friendship_service.rejectFriendRequest(friendship_Id);
        return ResponseEntity.noContent().build();
    }





    //TODO:_______________ Cancel Friendship Request ____________________________
    @DeleteMapping("/cancel-FriendRequest/{friendship_Id}")
    public ResponseEntity<Void> cancelFriendRequest(@PathVariable Long friendship_Id)
    {
        friendship_service.cancelFriendRequest(friendship_Id);
        return ResponseEntity.noContent().build();
    }




}
