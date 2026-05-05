package ahmed.com.springboot.friend_finder_system.Controller;

import ahmed.com.springboot.friend_finder_system.dto.DtoSimble.User_Simple_Dto;
import ahmed.com.springboot.friend_finder_system.dto.MatchDto;
import ahmed.com.springboot.friend_finder_system.service.Match_Service;
import ahmed.com.springboot.friend_finder_system.service.impl.Match_Service_Impl;
import jakarta.validation.constraints.Past;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/match")
@RequiredArgsConstructor
public class MatchController {


    private final Match_Service match_Service;





    @GetMapping("/findMatch/{id}")
    public ResponseEntity<List<User_Simple_Dto>> findMatch(@PathVariable Long id ) {
        return ResponseEntity.ok(match_Service.findPotentialFriends(id));
    }

}
