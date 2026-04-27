package ahmed.com.springboot.friend_finder_system.service;

import ahmed.com.springboot.friend_finder_system.dto.CommentDto;

import java.util.List;

public interface Comment_Service {

    void createComment(Long postId, String comment);

    void deleteComment(Long commentId);

    void updateComment(Long commentId, String newComment);

    void replyToComment(Long commentId, String reply);

    List<CommentDto> getComments(Long postId);

    List<CommentDto> getReplies(Long commentId);

}
