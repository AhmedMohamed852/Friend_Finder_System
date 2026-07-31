package ahmed.com.springboot.friend_finder_system.service.impl;

import ahmed.com.springboot.friend_finder_system.GlobalExService.FriendshipEx;
import ahmed.com.springboot.friend_finder_system.dto.DtoSimble.FriendShipRequestsDto;
import ahmed.com.springboot.friend_finder_system.dto.DtoSimble.User_Simple_Dto;
import ahmed.com.springboot.friend_finder_system.eNum.FriendshipStatus;
import ahmed.com.springboot.friend_finder_system.globalCurrentUserId.CurrentUser;
import ahmed.com.springboot.friend_finder_system.mapper.UserMapper;
import ahmed.com.springboot.friend_finder_system.mapper.UserSimpleMapper;
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
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class Friendship_Service_Impl implements Friendship_Service {


    //TODO: Declare Service Methods

    private final FriendShip_Repo friendShip_Repo;
    private final User_Service user_Service;
    private final UserMapper userMapper;
    private final UserSimpleMapper userSimpleMapper;
    private final Notification_Service notification_Service;





    //TODO:_______________ Implement Service Methods ____________________________





    //TODO:_______________ Send Friend Request ____________________________
    @Override
    public void sendFriendRequest(Long User_Received)
    {
        user_Service.existsById(User_Received);

        if(friendShip_Repo.existsByUser1IdAndUser2_Id(CurrentUser.currentUserId(),User_Received))
        {
            throw FriendshipEx.alreadyExists();
        }

        User userSender = userMapper.toEntity(user_Service.getUserById(CurrentUser.currentUserId()));
        User user2 = userMapper.toEntity(user_Service.getUserById(User_Received));

        Friendship friendship = new Friendship();
        friendship.setUser1(userSender);
        friendship.setUser2(user2);
        friendship.setStatus(FriendshipStatus.PENDING);
        friendship.setRequestedAt(LocalDateTime.now());

        friendShip_Repo.save(friendship);

        notification_Service.createFriendRequestNotification(user2.getId());

    }

    @Override
    public Boolean if_heHasAnyFriends() {
       return friendShip_Repo.existsByUser1IdOrUser2_Id(CurrentUser.currentUserId() , CurrentUser.currentUserId()) ? true : false;
    }


    //TODO:_______________ Get Friendship Requests ____________________________
    @Override
    public List<FriendShipRequestsDto> getFriendshipsByUser1Id() {

        Optional<List<Friendship>> friendshipDtoList = friendShip_Repo.findAllByUser2_IdAndStatus(CurrentUser.currentUserId() ,FriendshipStatus.PENDING);

        if(friendshipDtoList.isEmpty()) {
            throw FriendshipEx.notExists();
        }

       return friendshipDtoList.orElseThrow(() ->
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

    }


    //TODO:_______________ Get Sent Friendship Requests ____________________________
    @Override
    public List<FriendShipRequestsDto> getSentFriendships() {

        Optional<List<Friendship>> friendshipDtoList = friendShip_Repo.findAllByUser1_IdAndStatus(CurrentUser.currentUserId() ,FriendshipStatus.PENDING);

        if(friendshipDtoList.isEmpty()) {
            throw FriendshipEx.notExists();
        }



        return friendshipDtoList.get().stream().map( friendship ->
        {
            FriendShipRequestsDto  friendShipRequestsDto = new FriendShipRequestsDto();

            friendShipRequestsDto.setFriendship_Id(friendship.getId());
            friendShipRequestsDto.setUserSenderId(friendship.getUser2().getId());
            friendShipRequestsDto.setFirstName(friendship.getUser2().getFirstName());
            friendShipRequestsDto.setLast_Name(friendship.getUser2().getLastName());
            friendShipRequestsDto.setProfilePicture(friendship.getUser2().getProfilePicture());

            return friendShipRequestsDto;

        }).toList();


    }

    //TODO:_______________ Get My Friends ____________________________
    @Override
    public List<FriendShipRequestsDto> getMyFriends() {

        Optional<List<Friendship>> friendshipDtoList = friendShip_Repo.findByStatusAndUser1_IdOrStatusAndUser2_Id(
                FriendshipStatus.ACCEPTED, CurrentUser.currentUserId(), FriendshipStatus.ACCEPTED, CurrentUser.currentUserId());

        if (friendshipDtoList.isEmpty()) {
            throw FriendshipEx.notExists();
        }

        List<FriendShipRequestsDto> result = friendshipDtoList.get().stream().map(friendship -> {

            FriendShipRequestsDto friendShipRequestsDto = new FriendShipRequestsDto();

            User otherUser = CurrentUser.currentUserId().equals(friendship.getUser1().getId())
                    ? friendship.getUser2()
                    : friendship.getUser1();

            friendShipRequestsDto.setFriendship_Id(friendship.getId());
            friendShipRequestsDto.setUserSenderId(otherUser.getId());
            friendShipRequestsDto.setFirstName(otherUser.getFirstName());
            friendShipRequestsDto.setLast_Name(otherUser.getLastName());
            friendShipRequestsDto.setProfilePicture(otherUser.getProfilePicture());

            return friendShipRequestsDto;

        }).toList();

       return result.stream().toList();
    }

    //TODO:_______________ Accept Friendship Request ____________________________
    @Override
    public void acceptFriendRequest(Long friendship_Id) {

        if(friendshipExists(friendship_Id))
        {
            throw FriendshipEx.notExists();
        }

        Friendship friendship = friendShip_Repo.findById(friendship_Id).orElseThrow(FriendshipEx::notExists);

        friendship.setStatus(FriendshipStatus.ACCEPTED);
        friendship.setRespondedAt(LocalDateTime.now());

        friendShip_Repo.save(friendship);

        notification_Service.createFriendAcceptNotification(friendship.getUser1().getId());

    }




    //TODO:_______________ search Of Conversation ____________________________
    @Override
    public Set<User_Simple_Dto> search (String key){

        if(key == null)
        {
            throw FriendshipEx.searchKeyRequired();
        }


        List<Friendship> friendships = friendShip_Repo.search(CurrentUser.currentUserId(), key);

        return friendships.stream()
                .map(friendship -> friendship.getUser1().getId().equals(CurrentUser.currentUserId())
                        ? userSimpleMapper.toDto(friendship.getUser2())
                        : userSimpleMapper.toDto(friendship.getUser1()))
                .collect(Collectors.toSet());

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

        notification_Service.createFriendRejectNotification(friendship.getUser1().getId());
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


    //TODO:_______________ Un Friend ____________________________

    @Override
    public void unfriend(Long friendship_Id) {

        if(!friendShip_Repo.existsById(friendship_Id))
        {
            throw new RuntimeException("error.friendships.not.exists");
        }

       friendShip_Repo.deleteById(friendship_Id);

    }


    //TODO:_______________ Chick if friendshipExists ____________________________
    private boolean friendshipExists(Long friendship_Id) {
        return Objects.isNull(friendship_Id);
    }


}
