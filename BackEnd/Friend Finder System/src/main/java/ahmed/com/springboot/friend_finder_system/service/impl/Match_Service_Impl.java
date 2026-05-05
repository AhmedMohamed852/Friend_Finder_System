package ahmed.com.springboot.friend_finder_system.service.impl;

import ahmed.com.springboot.friend_finder_system.dto.DtoSimble.User_Simple_Dto;
import ahmed.com.springboot.friend_finder_system.dto.MatchDto;
import ahmed.com.springboot.friend_finder_system.eNum.FriendshipStatus;
import ahmed.com.springboot.friend_finder_system.models.Friendship;
import ahmed.com.springboot.friend_finder_system.models.Interests;
import ahmed.com.springboot.friend_finder_system.models.User;
import ahmed.com.springboot.friend_finder_system.repo.FriendShip_Repo;
import ahmed.com.springboot.friend_finder_system.repo.User_Repo;
import ahmed.com.springboot.friend_finder_system.service.Match_Service;
import ahmed.com.springboot.friend_finder_system.service.User_Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class Match_Service_Impl implements Match_Service {


    //TODO: Declare Service Methods

    private final User_Service user_Service;

    private final User_Repo user_Repo;

    private final FriendShip_Repo friendShip_Repo;





    //TODO:_______________ Implement Service Methods ____________________________





    //TODO:_______________ Find Potential Friends ____________________________
    //TODO:___________________________________________________________________


    @Override
    public List<User_Simple_Dto> findPotentialFriends(Long currentUserId) {

        // Get Current User Info
        User current_user = user_Repo.findById(currentUserId)
                .orElseThrow(() -> new RuntimeException("error.user.not.found"));


        Set<Long> currentFriendIds = getCurrentFriendIds(current_user.getId());


        Set<Long> pendingRequestIds = getPendingRequestIds(current_user.getId());


        List<User> allUsers = user_Repo.findAll();


        List<MatchDto> matchresult = allUsers.stream().
                filter(user -> !user.getId().equals(currentUserId))

                .filter(user -> !currentFriendIds.contains(user.getId()))

                .filter(user -> !pendingRequestIds.contains(user.getId()))

                .map(user -> calculateMatch(current_user , user))

                .filter(match -> match.getMatchScore() > 10)

                .sorted((m1 ,m2) -> Double.compare(m2.getMatchScore() , m1.getMatchScore()))

                .limit(2).collect(Collectors.toList());

        List<User_Simple_Dto> user_Simple_Dto = matchresult.stream().map(match -> match.getUser()).collect(Collectors.toList());

        return user_Simple_Dto;
    }











    //TODO:_______________ Get Current Friends ____________________________
    //TODO:_________________________________________________________________

    public Set<Long> getCurrentFriendIds(Long currentUser) {

        List<Friendship> friendships = friendShip_Repo
                .findByUser1_IdOrUser2_IdAndStatus(currentUser , currentUser , FriendshipStatus.ACCEPTED);

        Set<Long> friendIds = new HashSet<>();

        friendships.forEach(friendship -> {
           if(friendship.getUser1().getId().equals(currentUser)) {
               friendIds.add(friendship.getUser2().getId());
           }else{
               friendIds.add(friendship.getUser1().getId());
           }
        });



        return friendIds;
    }





    //TODO:_______________ Get Pending Request Ids ____________________________
    //TODO:_____________________________________________________________________


    public Set<Long> getPendingRequestIds(Long currentUser)
    {
        List<Friendship> friendships = friendShip_Repo
                .findByUser1_IdOrUser2_IdAndStatus(currentUser ,currentUser , FriendshipStatus.PENDING);

        Set<Long> friendIds = friendships.stream().map(friendship -> {
            Long friendId = 1L;
            if(friendship.getUser1().getId().equals(currentUser)) {
                friendId = (friendship.getUser2().getId());
            }else{
                friendId = (friendship.getUser1().getId());
            }

            return friendId;
        }).collect(Collectors.toSet());

        return friendIds;
    }



    //TODO:_______________ Calculate Match ____________________________
    //TODO:_____________________________________________________________
    public MatchDto calculateMatch(User currentUser, User otherUser)
    {
        Set<Interests> currentUserInterests = currentUser.getInterests();
        Set<Interests> otherUserInterests = otherUser.getInterests();

        Set<Interests> commonInterests = new HashSet<>(currentUserInterests);
        commonInterests.retainAll(otherUserInterests);

        double interestScour = 0.0;

        if(!commonInterests.isEmpty())
        {
            interestScour = (commonInterests.size() * 100.0) / commonInterests.size();

        }


        boolean sameCity = false;
        double cityScour = 0.0;

        if(currentUser.getCity() !=  null && otherUser.getCity() !=  null)
        {
            sameCity = currentUser.getCity().toString().equals(otherUser.getCity().toString());
            if(sameCity)
            {
                cityScour = 20.0;
            }
        }


        Set<Long> currentFriends = getCurrentFriendIds(currentUser.getId());
        Set<Long> otherFriends = getCurrentFriendIds(otherUser.getId());

        Set<Long> mutualFriends = new HashSet<>(currentFriends);
        mutualFriends.retainAll(otherFriends);

        int mutualFriendsCount = mutualFriends.size();

        double mutualFriendsScour = mutualFriendsCount * 5.0;

        if(mutualFriendsScour > 20.0)
        {
            mutualFriendsScour = 20.0;
        }


        double totalScour = (interestScour * 0.6) + (cityScour * 0.2) + (mutualFriendsScour*0.2);


        Set<String> commonInterestNames = commonInterests.stream().
                map(interest -> interest.getCategory().toString()).collect(Collectors.toSet());


        User_Simple_Dto user_Simple_Dto = user_Service.simple_User(otherUser.getId());

        MatchDto matchDto = new MatchDto();

        matchDto.setUser(user_Simple_Dto);
        matchDto.setMatchScore(totalScour);
        matchDto.setCommonInterests(commonInterestNames);
        matchDto.setMutualFriendsCount(mutualFriendsCount);
        matchDto.setSameCity(sameCity);

        return matchDto;
    }


}
