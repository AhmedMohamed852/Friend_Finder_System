package ahmed.com.springboot.friend_finder_system.dto.DtoSimble;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class User_Simple_Dto {


    private Long id; // not me

    private String profilePicture;

    private String firstName;
    private String lastName;
}
