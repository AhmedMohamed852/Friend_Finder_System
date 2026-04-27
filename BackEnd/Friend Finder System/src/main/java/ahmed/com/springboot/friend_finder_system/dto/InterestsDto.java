package ahmed.com.springboot.friend_finder_system.dto;

import ahmed.com.springboot.friend_finder_system.eNum.InterestCategory;
import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class InterestsDto {


    private Long id;


    private InterestCategory category;


    private String icon;


    // _______________relations__________________________________

}
