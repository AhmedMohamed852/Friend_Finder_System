package ahmed.com.springboot.friend_finder_system.Controller;

import ahmed.com.springboot.friend_finder_system.dto.InterestsDto;
import ahmed.com.springboot.friend_finder_system.service.Interest_Service;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/interests")
@RequiredArgsConstructor
@Tag(
        name = "Interests Controller",
        description = "APIs for managing user interests"
)
public class InterestsController {

    private final Interest_Service interest_Service;

    //TODO:_______________ Get All Interests ____________________________

    @Operation(
            summary = "Get All Interests",
            description = "Retrieve all available interests in the system"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Interests retrieved successfully",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(
                                    schema = @Schema(implementation = InterestsDto.class)
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized"
            )
    })
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/getAllInterests")
    public ResponseEntity<List<InterestsDto>> getAllInterests() {
        return ResponseEntity.ok(interest_Service.getAllInterests());
    }


    //TODO:_______________ Get User Interests ____________________________

    @Operation(
            summary = "Get User Interests",
            description = "Retrieve interests for a specific user by user ID"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "User interests retrieved successfully",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(
                                    schema = @Schema(implementation = InterestsDto.class)
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized"
            )
    })
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/getUserInterests/{id}")
    public ResponseEntity<List<InterestsDto>> getUserInterests(
            @Parameter(
                    description = "User ID",
                    example = "1",
                    required = true
            )
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(interest_Service.getInterestsById(id));
    }


    //TODO:_______________ Set List Of Interests ____________________________

    @Operation(
            summary = "Set User Interests",
            description = "Assign a list of interests to a user"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Interests saved successfully"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request data"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized"
            )
    })
    @PreAuthorize("hasRole('USER')")
    @PostMapping("/setListInterests")
    public ResponseEntity<Void> setListInterests(

            @Parameter(
                    description = "User ID",
                    example = "1",
                    required = true
            )
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "List of interests to assign to user",
                    required = true
            )
            @RequestBody @Valid List<InterestsDto> interestsDto
    ) {
        interest_Service.setListInterests(interestsDto);
        return ResponseEntity.noContent().build();
    }
}