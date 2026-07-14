package ahmed.com.springboot.friend_finder_system.service;


import ahmed.com.springboot.friend_finder_system.dto.DtoSimble.FriendShipRequestsDto;
import ahmed.com.springboot.friend_finder_system.dto.DtoSimble.User_Simple_Dto;
import ahmed.com.springboot.friend_finder_system.dto.FriendshipDto;

import java.util.List;
import java.util.Set;

public interface Friendship_Service {



    void sendFriendRequest(Long user2);

    List<FriendShipRequestsDto> getFriendshipsByUser1Id();

    List<FriendShipRequestsDto> getSentFriendships();


    List<FriendShipRequestsDto> getMyFriends();

    void acceptFriendRequest(Long Friendship_Id);

    void rejectFriendRequest(Long Friendship_Id);

    void cancelFriendRequest(Long Friendship_Id);

    void unfriend(Long friendship_Id);

    Set<User_Simple_Dto> search (String key);


    /*    void blockUser(Long friendId);*/




}
