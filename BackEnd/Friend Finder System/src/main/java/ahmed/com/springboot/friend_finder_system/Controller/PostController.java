package ahmed.com.springboot.friend_finder_system.Controller;

import ahmed.com.springboot.friend_finder_system.Vm.Post_Response_Vm;
import ahmed.com.springboot.friend_finder_system.dto.PostDto;
import ahmed.com.springboot.friend_finder_system.service.Post_Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/post/")
@RequiredArgsConstructor
public class PostController {

    //TODO: Declare Service Methods

    private final Post_Service post_Service;


    //TODO:_______________ Implement Service Methods ____________________________


    //TODO:_______________ Create New Account ____________________________
    @PostMapping("creatPost/{id}")
    public ResponseEntity<Void> creatPost(@RequestBody PostDto postDto , @PathVariable Long id)
    {
        post_Service.creatPost(postDto , id);
        return ResponseEntity.noContent().build();
    }


    //TODO:_______________ Update Post ____________________________

    @PutMapping("updatePost/{id}")
    public ResponseEntity<Void> updatePost(@RequestBody PostDto postDto , @PathVariable Long id)
    {
        post_Service.updatePost(postDto ,id);
        return ResponseEntity.noContent().build();
    }


    //TODO:_______________ Delete Post ____________________________

    @DeleteMapping ("deletePost/{id}")
    public ResponseEntity<Void> deletePost( @PathVariable Long id)
    {
        post_Service.deletePost(id);
        return ResponseEntity.noContent().build();
    }


    //TODO:_______________ Get User Posts ____________________________


    @GetMapping ("getUserPosts/{id}")
    public ResponseEntity<Post_Response_Vm> getUserPosts(@PathVariable Long id)
    {
       return ResponseEntity.ok(post_Service.getUserPosts(id , 1));
    }


    //TODO:_______________ Get Post  ____________________________
    @GetMapping ("getPost/{id}")
    public ResponseEntity<PostDto> getPost(@PathVariable Long id)
    {
       return ResponseEntity.ok(post_Service.getPostById(id));
    }

}
