package ahmed.com.springboot.friend_finder_system.Controller;

import ahmed.com.springboot.friend_finder_system.Vm.LoginRequestVM;
import ahmed.com.springboot.friend_finder_system.Vm.LoginResponseVM;
import ahmed.com.springboot.friend_finder_system.dto.UserDto;
import ahmed.com.springboot.friend_finder_system.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

@RestController
@RequestMapping("/api/auth/")
@RequiredArgsConstructor
public class AuthController {
    //TODO: Declare Service Methods

    private final AuthService authService;




    //TODO:_______________ Implement Service Methods ____________________________



    //TODO:_______________ Create New Account ____________________________


    @PostMapping("register")
    public ResponseEntity<Void> register(@RequestBody @Valid UserDto userDto) throws URISyntaxException {
        authService.register(userDto);
        URI uri = new URI("/api/auth/register");
        return ResponseEntity.created(uri).build();
    }



    //TODO:_______________ Login To My Account ____________________________

    @PostMapping("login")
    public ResponseEntity<LoginResponseVM> login(@RequestBody @Valid LoginRequestVM loginRequestVM) throws URISyntaxException {

        return ResponseEntity.ok().body(authService.login(loginRequestVM));
    }
}
