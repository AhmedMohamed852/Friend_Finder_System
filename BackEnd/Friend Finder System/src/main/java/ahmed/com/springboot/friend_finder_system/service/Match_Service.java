package ahmed.com.springboot.friend_finder_system.service;

import ahmed.com.springboot.friend_finder_system.dto.DtoSimble.User_Simple_Dto;
import ahmed.com.springboot.friend_finder_system.dto.MatchDto;
import ahmed.com.springboot.friend_finder_system.dto.PostDto;
import ahmed.com.springboot.friend_finder_system.models.Post;
import ahmed.com.springboot.friend_finder_system.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;

public interface Match_Service {

    List<User_Simple_Dto> findPotentialFriends();

    Page<Post> findHomeFeed( Pageable pageable);


}
