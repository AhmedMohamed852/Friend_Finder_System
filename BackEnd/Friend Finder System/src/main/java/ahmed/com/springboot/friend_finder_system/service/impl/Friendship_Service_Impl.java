package ahmed.com.springboot.friend_finder_system.service.impl;

import ahmed.com.springboot.friend_finder_system.dto.DtoSimble.FriendShipRequestsDto;
import ahmed.com.springboot.friend_finder_system.dto.FriendshipDto;
import ahmed.com.springboot.friend_finder_system.eNum.FriendshipStatus;
import ahmed.com.springboot.friend_finder_system.mapper.FriendshipMapper;
import ahmed.com.springboot.friend_finder_system.mapper.UserMapper;
import ahmed.com.springboot.friend_finder_system.models.Friendship;
import ahmed.com.springboot.friend_finder_system.models.User;
import ahmed.com.springboot.friend_finder_system.repo.FriendShip_Repo;
import ahmed.com.springboot.friend_finder_system.service.Friendship_Service;
import ahmed.com.springboot.friend_finder_system.service.Notification_Service;
import ahmed.com.springboot.friend_finder_system.service.User_Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class Friendship_Service_Impl implements Friendship_Service {


    //TODO: Declare Service Methods

    private final FriendShip_Repo friendShip_Repo;
    //private final FriendshipMapper friendshipMapper;

    private final User_Service user_Service;
    private final UserMapper userMapper;
    private final Notification_Service notification_Service;





    //TODO:_______________ Implement Service Methods ____________________________





    //TODO:_______________ Send Friend Request ____________________________
    @Override
    public void sendFriendRequest(FriendshipDto friendshipDto)
    {
        if(!user_Service.existsById(friendshipDto.getUser1() ) || !user_Service.existsById(friendshipDto.getUser2()))
        {
            throw new RuntimeException("error.user.not.found");
        }

        if(friendShip_Repo.existsByUser1IdAndUser2_Id(friendshipDto.getUser1(),friendshipDto.getUser2()))
        {
            throw new RuntimeException("error.friendship.already.exists");
        }

        User userSender = userMapper.toEntity(user_Service.profile(friendshipDto.getUser1()));
        User user2 = userMapper.toEntity(user_Service.profile(friendshipDto.getUser2()));

        Friendship friendship = new Friendship();
        friendship.setUser1(userSender);
        friendship.setUser2(user2);
        friendship.setStatus(FriendshipStatus.PENDING);
        friendship.setRequestedAt(LocalDateTime.now());

        friendShip_Repo.save(friendship);

        notification_Service.createFriendRequestNotification(userSender.getId() , user2.getId());

    }


    //TODO:_______________ Get Friendship Requests ____________________________
    @Override
    public List<FriendShipRequestsDto> getFriendshipsByUser1Id(Long user2Id) {

        Optional<List<Friendship>> friendshipDtoList = friendShip_Repo.findAllByUser2_IdAndStatus(user2Id ,FriendshipStatus.PENDING);

        if(friendshipDtoList.isEmpty() || Objects.isNull(friendshipDtoList.get())) {
            throw new RuntimeException("error.friendships.not.exists");
        }



        List<FriendShipRequestsDto> result = friendshipDtoList.orElseThrow(() ->
                new RuntimeException("No Data")) .stream().map( friendship ->
        {
             FriendShipRequestsDto  friendShipRequestsDto = new FriendShipRequestsDto();

                friendShipRequestsDto.setFriendship_Id(friendship.getId());
                friendShipRequestsDto.setUserSenderId(friendship.getUser1().getId());
                friendShipRequestsDto.setFirstName(friendship.getUser1().getFirstName());
                friendShipRequestsDto.setLast_Name(friendship.getUser1().getLastName());
                friendShipRequestsDto.setProfilePicture(friendship.getUser1().getProfilePicture());

                return friendShipRequestsDto;

        }).toList();


        return result;
    }

    //TODO:_______________ Accept Friendship Request ____________________________
    @Override
    public void acceptFriendRequest(Long friendship_Id) {

        if(friendshipExists(friendship_Id))
        {
            throw new RuntimeException("error.friendships.not.exists");
        }

        Friendship friendship = friendShip_Repo.findById(friendship_Id).orElseThrow(() -> new  RuntimeException("error.friendships.not.exists"));

        friendship.setStatus(FriendshipStatus.ACCEPTED);
        friendship.setRespondedAt(LocalDateTime.now());

        friendShip_Repo.save(friendship);

        notification_Service.createFriendAcceptNotification(  friendship.getUser2().getId() , friendship.getUser1().getId());

    }



    //TODO:_______________ Rejected Friendship Request ____________________________
    @Override
    public void rejectFriendRequest(Long friendship_Id) {

        if(friendshipExists(friendship_Id))
        {
            throw new RuntimeException("error.friendships.not.exists");
        }

        Friendship friendship = friendShip_Repo.findById(friendship_Id).orElseThrow(() -> new  RuntimeException("error.friendships.not.exists"));

        friendship.setStatus(FriendshipStatus.REJECTED);
        friendship.setRespondedAt(LocalDateTime.now());

        friendShip_Repo.save(friendship);
    }


    //TODO:_______________ Cancel Friendship Request ____________________________
    @Override
    public void cancelFriendRequest(Long friendship_Id)
    {
        if(friendshipExists(friendship_Id))
        {
            throw new RuntimeException("error.friendships.not.exists");
        }

        Friendship friendship = friendShip_Repo.findById(friendship_Id).orElseThrow(() -> new  RuntimeException("error.friendships.not.exists"));

        friendShip_Repo.delete(friendship);
    }




    //TODO:_______________ Chick if friendshipExists ____________________________
    private boolean friendshipExists(Long friendship_Id) {
        if(Objects.isNull(friendship_Id))
        {
            return true;
        }
        return false;
    }
}
