package ahmed.com.springboot.friend_finder_system.service;

import ahmed.com.springboot.friend_finder_system.dto.DtoSimble.User_Simple_Dto;
import ahmed.com.springboot.friend_finder_system.dto.MatchDto;
import ahmed.com.springboot.friend_finder_system.models.User;

import java.util.List;
import java.util.Set;

public interface Match_Service {

    List<User_Simple_Dto> findPotentialFriends(Long currentUser );

 //   Set<Long> getCurrentFriendIds (Long currentUser);


   // Set<Long> getPendingRequestIds (Long currentUser);

  //  MatchDto calculateMatch(User currentUser , User otherUser);


}
