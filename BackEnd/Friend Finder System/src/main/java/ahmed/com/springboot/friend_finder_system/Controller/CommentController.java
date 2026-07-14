package ahmed.com.springboot.friend_finder_system.Controller;


import ahmed.com.springboot.friend_finder_system.Vm.CommentRequest_Vm;
import ahmed.com.springboot.friend_finder_system.Vm.UpdateCommentRequest_Vm;
import ahmed.com.springboot.friend_finder_system.dto.CommentDto;
import ahmed.com.springboot.friend_finder_system.service.Comment_Service;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(
        name = "Comment Controller",
        description = "APIs for managing comments requests"
)
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    //TODO: Declare Service Methods

    private final Comment_Service commentService;



    //TODO:_______________ Implement Service Methods ____________________________

    //TODO:_______________ Get Comments ____________________________
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/CommentsByPostId/{postId}")
    public ResponseEntity<List<CommentDto>> commentsByPostId(@PathVariable Long postId, @RequestParam int pageNumber) {

        return ResponseEntity.ok(commentService.getComments(postId, pageNumber));
    }

/*
    //TODO:_______________ Get Comments  ____________________________
    @PreAuthorize("hasRole('USER')")
    @GetMapping("CommentsByPostId/{postId}")
    public ResponseEntity<List<CommentDto>> commentsByPostId(@PathVariable Long postId){
        return ResponseEntity.ok(commentService.getComments(postId));
    }
*/



    //TODO:_______________ New Comment ____________________________
    @PreAuthorize("hasRole('USER')")
    @PostMapping("/newComment")
    public ResponseEntity<Void> newComment(@RequestBody @Valid CommentRequest_Vm commentRequestVm){
        return ResponseEntity.ok(commentService.createComment(commentRequestVm));
    }

    //TODO:_______________ Update Comment ____________________________
    @PreAuthorize("hasRole('USER')")
    @PutMapping("/updateComment")
    public ResponseEntity<Void> updateComment(
            @RequestBody UpdateCommentRequest_Vm commentRequestVm) {

        commentService.updateComment(commentRequestVm);
        return ResponseEntity.ok().build();
    }


    //TODO:_______________ Reply To Comment ____________________________
    @PreAuthorize("hasRole('USER')")
    @PostMapping("/replyToComment")
    public ResponseEntity<Void> replyToComment(
            @RequestBody UpdateCommentRequest_Vm commentRequestVm) {

        commentService.replyToComment(commentRequestVm);
        return ResponseEntity.ok().build();
    }


    //TODO:_______________ Delete Comment ____________________________
    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/deleteComment/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable Long commentId) {

        commentService.deleteComment(commentId);
        return ResponseEntity.ok().build();
    }


    //TODO:_______________ Get Replies ____________________________
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/replies/{commentId}")
    public ResponseEntity<List<CommentDto>> getReplies(
            @PathVariable Long commentId,
            @RequestParam(defaultValue = "1") int pageNumber) {

        return ResponseEntity.ok(
                commentService.getReplies(commentId, pageNumber)
        );
    }


}
