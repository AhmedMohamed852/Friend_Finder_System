package ahmed.com.springboot.friend_finder_system.Controller;

import ahmed.com.springboot.friend_finder_system.dto.DtoSimble.UpdateProfileDto;
import ahmed.com.springboot.friend_finder_system.dto.DtoSimble.User_Simple_Dto;
import ahmed.com.springboot.friend_finder_system.dto.StoriesDto;
import ahmed.com.springboot.friend_finder_system.dto.UserDto;
import ahmed.com.springboot.friend_finder_system.helper.MessageResponse;
import ahmed.com.springboot.friend_finder_system.service.User_Service;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(
        name = "User Controller",
        description = "APIs for managing user profiles and accounts"
)
public class UserController {

    //TODO: Declare Service Methods

    private final User_Service user_Service;

    //TODO:_______________ Implement Service Methods ____________________________



    //TODO:_______________ Update My Profile ____________________________

    // TODO -----------> SWAGGER {
    @Operation(
            summary = "Update My Profile",
            description = "API To Update Authenticated User Profile",

            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Profile Updated Successfully"
                    ),

                    @ApiResponse(
                            responseCode = "400",
                            description = "{user.invalid.profile.data}",
                            content = @Content(
                                    schema = @Schema(implementation = MessageResponse.class)
                            )
                    )
            }
    )
    // TODO               SWAGGER }     <---------------------

    @PreAuthorize("hasRole('USER')")
    @PutMapping("/updateProfile")
    public ResponseEntity<Void> updateProfile(
            @RequestBody @Valid UpdateProfileDto userDto
    ) {
        user_Service.updateProfile(userDto);
        return ResponseEntity.noContent().build();
    }




    //TODO:_______________ Delete My Account ____________________________

    // TODO -----------> SWAGGER {
    @Operation(
            summary = "Delete My Account",
            description = "API To Delete User Account",

            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Account Deleted Successfully"
                    ),

                    @ApiResponse(
                            responseCode = "404",
                            description = "{user.not.found}",
                            content = @Content(
                                    schema = @Schema(implementation = MessageResponse.class)
                            )
                    )
            }
    )
    // TODO               SWAGGER }     <---------------------

    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/deleteAccount")
    public ResponseEntity<Void> deleteAccount(


    ) {
        user_Service.deleteAccount();
        return ResponseEntity.noContent().build();
    }






    //TODO:_______________ Show My Profile ____________________________

    // TODO -----------> SWAGGER {
    @Operation(
            summary = "Show User Profile",
            description = "API To Retrieve User Profile Information",

            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Profile Retrieved Successfully",
                            content = @Content(
                                    schema = @Schema(implementation = UserDto.class)
                            )
                    ),

                    @ApiResponse(
                            responseCode = "404",
                            description = "{user.not.found}",
                            content = @Content(
                                    schema = @Schema(implementation = MessageResponse.class)
                            )
                    )
            }
    )
    // TODO               SWAGGER }     <---------------------

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/user/{id}")
    public ResponseEntity<UserDto> getUserBuId(

            @Parameter(
                    description = "User ID",
                    example = "1",
                    required = true
            )
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(user_Service.getUserById(id));
    }




    @PreAuthorize("hasRole('USER')")
    @GetMapping("/profile")
    public ResponseEntity<UserDto> profile(

    ) {

        return ResponseEntity.ok(user_Service.profile());
    }



    @PreAuthorize("hasRole('USER')")
    @GetMapping("/simpleProfile")
    public ResponseEntity<User_Simple_Dto> simpleProfile(
    ) {

        return ResponseEntity.ok(user_Service.simpleProfile());
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/search/{key}/{pageNumber}")
    public ResponseEntity<List<User_Simple_Dto>> search(@PathVariable String key,@PathVariable int pageNumber
    ) {

        return ResponseEntity.ok(user_Service.search(key , pageNumber));
    }

}