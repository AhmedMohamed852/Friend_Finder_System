package ahmed.com.springboot.friend_finder_system.Controller;

import ahmed.com.springboot.friend_finder_system.dto.StoriesDto;
import ahmed.com.springboot.friend_finder_system.service.Friendship_Service;
import ahmed.com.springboot.friend_finder_system.service.Stories_Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/stories")
@RequiredArgsConstructor
@RestController
public class Stories_Controller {


    //TODO: Declare Service Methods

    private final Stories_Service storiesService;



    //TODO:_______________ Implement Service Methods ____________________________





//TODO:_______________ Send Friend Request ____________________________





    //TODO:_______________ new Story ____________________________
    @PreAuthorize("hasRole('USER')")
    @PostMapping("/newStory")
    public ResponseEntity<Void> newStory(@RequestBody StoriesDto storiesDto) {
        storiesService.newStory(storiesDto);
        return ResponseEntity.noContent().build();
    }



    //TODO:_______________ Get My Story ____________________________
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/getStories")
    public ResponseEntity<List<StoriesDto>> getMyStory() {
        return ResponseEntity.ok(storiesService.getStories());
    }



}
