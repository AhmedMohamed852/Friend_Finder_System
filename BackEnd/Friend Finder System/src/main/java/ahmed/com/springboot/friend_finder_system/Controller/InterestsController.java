package ahmed.com.springboot.friend_finder_system.Controller;

import ahmed.com.springboot.friend_finder_system.dto.InterestsDto;
import ahmed.com.springboot.friend_finder_system.service.Interest_Service;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/interests")
@RequiredArgsConstructor
public class InterestsController {

    //TODO: Declare Service Methods

    private final Interest_Service interest_Service;



    //TODO:_______________ Implement Service Methods ____________________________



    //TODO:_______________ Get All Interests  ____________________________

    @GetMapping("/getAllInterests")
    public ResponseEntity<List<InterestsDto>> getAllInterests() {
        return ResponseEntity.ok(interest_Service.getAllInterests());
    }


    //TODO:_______________ Get List Of Interests For User ____________________________
    @GetMapping("/getUserInterests/{id}")
    public ResponseEntity<List<InterestsDto>> getUserInterests(@PathVariable Long id) {
        return ResponseEntity.ok(interest_Service.getInterestsById(id));
    }



    //TODO:_______________ Set List Of Interests For User ____________________________
    @PostMapping("/setListInterests/{id}")
    public ResponseEntity<Void> setListInterests(@RequestBody @Valid List<InterestsDto> interestsDto , @PathVariable Long id) {
       interest_Service.setListInterests(interestsDto ,id );
       return ResponseEntity.noContent().build();
    }
}
