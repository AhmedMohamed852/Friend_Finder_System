package ahmed.com.springboot.friend_finder_system.dto;

import ahmed.com.springboot.friend_finder_system.dto.DtoSimble.User_Simple_Dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MatchDto {

    private User_Simple_Dto user;

    private Double matchScore;

    private Set<String> commonInterests;

    private Integer mutualFriendsCount;

    private Boolean sameCity;

}
