package ahmed.com.springboot.friend_finder_system.service.impl;


import ahmed.com.springboot.friend_finder_system.GlobalExService.CommentEx;
import ahmed.com.springboot.friend_finder_system.GlobalExService.PaginationEx;
import ahmed.com.springboot.friend_finder_system.Vm.CommentRequest_Vm;
import ahmed.com.springboot.friend_finder_system.Vm.UpdateCommentRequest_Vm;
import ahmed.com.springboot.friend_finder_system.dto.CommentDto;
import ahmed.com.springboot.friend_finder_system.globalCurrentUserId.CurrentUser;
import ahmed.com.springboot.friend_finder_system.mapper.CommentMapper;
import ahmed.com.springboot.friend_finder_system.mapper.PostMapper;
import ahmed.com.springboot.friend_finder_system.mapper.UserMapper;
import ahmed.com.springboot.friend_finder_system.models.Comments;
import ahmed.com.springboot.friend_finder_system.models.Post;
import ahmed.com.springboot.friend_finder_system.models.User;
import ahmed.com.springboot.friend_finder_system.repo.Comment_Repo;
import ahmed.com.springboot.friend_finder_system.service.Comment_Service;
import ahmed.com.springboot.friend_finder_system.service.Post_Service;
import ahmed.com.springboot.friend_finder_system.service.User_Service;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements Comment_Service {



    //TODO: Declare Service Methods

    private final Comment_Repo commentRepo;
    private final CommentMapper commentMapper;
    private final Post_Service postService;
    private final PostMapper postMapper;
    private final User_Service userService;
    private final UserMapper userMapper;




    //TODO:_______________ Implement Service Methods ____________________________


    //TODO:_______________ Create Comment ____________________________
    @Override
    public Void createComment(CommentRequest_Vm commentRequestVm) {
        Post post = postMapper.toEntity(postService.getPostById(commentRequestVm.getPostId()));

        User author = userMapper.toEntity(userService.getUserById(CurrentUser.currentUserId()));

        Comments comment1 = new Comments();
        comment1.setPost(post);
        comment1.setAuthor(author);
        comment1.setContent(commentRequestVm.getContent());


        if(post.getCountComments() == null)
        {
            post.setCountComments(1);
        }else{
            post.setCountComments(post.getCountComments()+1);
            postService.savePost(postMapper.toDto(post));
        }

        commentRepo.save(comment1);
        return null;
    }


    //TODO:_______________ Delete Comment ____________________________
    @Override
    public void deleteComment(Long commentId) {

        if(Objects.isNull(commentId))
        {
            throw CommentEx.commentIdRequired();
        }
       Optional<Comments> comments = commentRepo.findById(commentId);
        if(!(comments.get().getAuthor().getId().equals(CurrentUser.currentUserId()) || comments.get().getPost().getAuthor().getId().equals(CurrentUser.currentUserId())))
        {
            throw CommentEx.unauthorizedDelete();
        }

        commentRepo.deleteById(commentId);

    }

    //TODO:_______________ Update Comment ____________________________

    @Override
    public void updateComment(UpdateCommentRequest_Vm commentRequestVm) {

        Optional<Comments> comment = Optional.of(commentRepo.findById(commentRequestVm.getCommentId()).
                orElseThrow(CommentEx::commentNotFound));

        comment.get().setContent(commentRequestVm.getContent());

        commentRepo.save(comment.get());

    }

    //TODO:_______________ Reply To Comment ____________________________
    @Override
    public void replyToComment(UpdateCommentRequest_Vm commentRequestVm) {

        Optional<Comments> comment = Optional.of(commentRepo.findById(commentRequestVm.getCommentId()).
                orElseThrow(CommentEx::commentNotFound));

        Post post = postMapper.toEntity(postService.getPostById(comment.get().getPost().getId()));
        User author = userMapper.toEntity(userService.getUserById(CurrentUser.currentUserId()));
        Comments replyComment = new Comments();

        replyComment.setPost(post);
        replyComment.setAuthor(author);
        replyComment.setContent(commentRequestVm.getContent());
        replyComment.setParentComment(comment.get());
        comment.get().getReplies().add(replyComment);

        if(post.getCountComments() == null)
        {
            post.setCountComments(1);
        }else{
            post.setCountComments(post.getCountComments()+1);
            postService.savePost(postMapper.toDto(post));
        }

        commentRepo.save(comment.get());
    }


    //TODO:_______________ Get Comments ____________________________
    @Override
    public List<CommentDto> getComments(Long postId, int pageNumber) {


        validatePageNumberAndSize(pageNumber);

        Pageable pageable = PageRequest.of(pageNumber - 1, 3);

        Page<Comments> comments = commentRepo.findByPost_IdAndParentCommentIsNull(postId, pageable);

        if (comments.isEmpty()) {
            throw CommentEx.commentNotFound();
        }

        Post post = postMapper.toEntity(postService.getPostById(postId));

        List<CommentDto> commentDtos = commentMapper.toDtoList(comments.getContent());

        commentDtos.forEach(commentDto -> {
            commentDto.setPostId(postId);
            commentDto.setLikedIs(post.getCountLikes());
            commentDto.setCountComments(post.getCountComments());
        });

        return commentDtos;
    }

    @Override
    public List<CommentDto> getReplies(Long commentId, int pageNumber) {

        validatePageNumberAndSize(pageNumber);

        if(!commentRepo.existsById(commentId))
        {
            throw CommentEx.commentNotFound();
        }

        Pageable pageable = PageRequest.of(pageNumber - 1 , 3);

        Page<Comments> replies = commentRepo.findByParentCommentId(commentId , pageable);

        return commentMapper.toDtoList(replies.getContent());
    }



    //TODO _________________validatePageNumberAndSize______________________
//TODO ________________________________________________________________
    boolean validatePageNumberAndSize(int pageNumber)
    {
        if (pageNumber < 1 )
        {
            PaginationEx.invalidPageNumber();
        }
        return true;
    }

}
