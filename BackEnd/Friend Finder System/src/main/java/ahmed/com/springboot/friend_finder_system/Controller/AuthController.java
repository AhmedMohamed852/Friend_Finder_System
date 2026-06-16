package ahmed.com.springboot.friend_finder_system.Controller;

import ahmed.com.springboot.friend_finder_system.Vm.LoginRequestVM;
import ahmed.com.springboot.friend_finder_system.Vm.LoginResponseVM;
import ahmed.com.springboot.friend_finder_system.dto.UserDto;
import ahmed.com.springboot.friend_finder_system.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

@RestController
@RequestMapping("/api/auth/")
@RequiredArgsConstructor
@Tag(
        name = "Authentication",
        description = "Authentication APIs for registration and login"
)
public class AuthController {

    private final AuthService authService;

    @Operation(
            summary = "Register a new user",
            description = "Creates a new account in the Friend Finder system."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "User registered successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "409", description = "Email or username already exists")
    })
    @PostMapping("register")
    public ResponseEntity<Void> register(
            @RequestBody(description = "User registration data", required = true)
            @org.springframework.web.bind.annotation.RequestBody
            @Valid UserDto userDto
    ) throws URISyntaxException {

        authService.register(userDto);

        URI uri = new URI("/api/auth/register");

        return ResponseEntity.created(uri).build();
    }

    @Operation(
            summary = "Login",
            description = "Authenticates a user and returns JWT access token."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Login successful"),
            @ApiResponse(responseCode = "400", description = "Invalid credentials format"),
            @ApiResponse(responseCode = "401", description = "Invalid email or password")
    })
    @PostMapping("login")
    public ResponseEntity<LoginResponseVM> login(
            @RequestBody(description = "User login credentials", required = true)
            @org.springframework.web.bind.annotation.RequestBody
            @Valid LoginRequestVM loginRequestVM
    ) {

        return ResponseEntity.ok(authService.login(loginRequestVM));
    }
}