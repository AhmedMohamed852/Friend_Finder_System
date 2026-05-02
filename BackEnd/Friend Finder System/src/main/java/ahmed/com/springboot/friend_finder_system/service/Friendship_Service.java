package ahmed.com.springboot.friend_finder_system.service;


import ahmed.com.springboot.friend_finder_system.dto.DtoSimble.FriendShipRequestsDto;
import ahmed.com.springboot.friend_finder_system.dto.FriendshipDto;

import java.util.List;

public interface Friendship_Service {



    void sendFriendRequest(FriendshipDto friendshipDto);

    List<FriendShipRequestsDto> getFriendshipsByUser1Id(Long user1Id);

    void acceptFriendRequest(Long Friendship_Id);

    void rejectFriendRequest(Long Friendship_Id);

    void cancelFriendRequest(Long Friendship_Id);

/*
    void unfriend(Long friendId);
*/

/*    void blockUser(Long friendId);*/




}
