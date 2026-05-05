package ahmed.com.springboot.friend_finder_system.dto;

import ahmed.com.springboot.friend_finder_system.eNum.InterestCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class InterestsDto {


    private Long id;

    @NotNull(message = "Name is required")
    private InterestCategory category;


    private String icon;


    // _______________relations__________________________________

}
