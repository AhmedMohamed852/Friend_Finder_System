package ahmed.com.springboot.friend_finder_system.service;

import ahmed.com.springboot.friend_finder_system.Vm.CommentRequest_Vm;
import ahmed.com.springboot.friend_finder_system.Vm.UpdateCommentRequest_Vm;
import ahmed.com.springboot.friend_finder_system.dto.CommentDto;

import java.util.List;

public interface Comment_Service {

    Void createComment(CommentRequest_Vm commentRequestVm);

    void deleteComment(Long commentId);

    void updateComment(UpdateCommentRequest_Vm CommentRequestVm);

    void replyToComment(UpdateCommentRequest_Vm commentRequestVm);

    List<CommentDto> getComments(Long postId, int pageNumber);

    List<CommentDto> getReplies(Long commentId , int pageNumber);

}
