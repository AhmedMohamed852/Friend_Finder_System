package ahmed.com.springboot.friend_finder_system.Controller;

import ahmed.com.springboot.friend_finder_system.dto.DtoSimble.UpdateProfileDto;
import ahmed.com.springboot.friend_finder_system.dto.UserDto;
import ahmed.com.springboot.friend_finder_system.service.User_Service;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    //TODO: Declare Service Methods

    private final User_Service user_Service;




    //TODO:_______________ Implement Service Methods ____________________________



    //TODO:_______________ Create New Account ____________________________




    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody @Valid UserDto userDto) {
        user_Service.register(userDto);
        return ResponseEntity.noContent().build();
    }



    //TODO:_______________ Login To My Account ____________________________

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestParam String username,@RequestParam String password) {
        user_Service.login(username, password);
        return ResponseEntity.noContent().build();
    }


    //TODO:_______________ Update My Profile ____________________________

    @PutMapping ("/updateProfile")
    public ResponseEntity<Void> updateProfile(@RequestBody @Valid UpdateProfileDto userDto) {
        user_Service.updateProfile(userDto);
        return ResponseEntity.noContent().build();
    }




    //TODO:_______________ Delete My Account ____________________________
    @DeleteMapping ("/deleteAccount/{id}")
    public ResponseEntity<Void> updateProfile(@PathVariable Long id) {
        user_Service.deleteAccount(id);
        return ResponseEntity.noContent().build();
    }




    //TODO:_______________ Show My Profile ____________________________
    @GetMapping ("/profile/{id}")
    public ResponseEntity<UserDto> profile(@PathVariable Long id) {

        return ResponseEntity.ok(user_Service.profile(id));
    }















}
