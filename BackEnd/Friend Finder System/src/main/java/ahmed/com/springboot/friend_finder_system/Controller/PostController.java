package ahmed.com.springboot.friend_finder_system.Controller;

import ahmed.com.springboot.friend_finder_system.dto.PostDto;
import ahmed.com.springboot.friend_finder_system.service.Post_Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/post")
@RequiredArgsConstructor
public class PostController {

    //TODO: Declare Service Methods

    private final Post_Service post_Service;


    //TODO:_______________ Implement Service Methods ____________________________

    @PostMapping("creatPost/{id}")
    public ResponseEntity<Void> creatPost(@RequestBody PostDto postDto , @PathVariable Long id)
    {
        post_Service.creatPost(postDto , id);
        return ResponseEntity.noContent().build();
    }


    //TODO:_______________ Create New Account ____________________________
}
