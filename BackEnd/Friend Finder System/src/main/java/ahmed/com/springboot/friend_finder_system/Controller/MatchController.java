package ahmed.com.springboot.friend_finder_system.Controller;

import ahmed.com.springboot.friend_finder_system.dto.DtoSimble.User_Simple_Dto;
import ahmed.com.springboot.friend_finder_system.dto.UserDto;
import ahmed.com.springboot.friend_finder_system.service.Match_Service;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/matches")
@RequiredArgsConstructor
@Tag(
        name = "Match Controller",
        description = "APIs for finding potential friends based on interests"
)
public class MatchController {

    private final Match_Service match_Service;

    // ===================== FIND MATCH =====================

    @Operation(
            summary = "Find Potential Friends",
            description = "Returns a list of users that match current user's interests"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Matches found successfully",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(
                                    schema = @Schema(implementation = User_Simple_Dto.class)
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized"
            )
    })
    @GetMapping("/find")
    public ResponseEntity<List<User_Simple_Dto>> findMatch() {
        return ResponseEntity.ok(match_Service.findPotentialFriends());
    }


}