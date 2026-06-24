package ahmed.com.springboot.friend_finder_system.service.impl;


import ahmed.com.springboot.friend_finder_system.Vm.CommentRequest_Vm;
import ahmed.com.springboot.friend_finder_system.Vm.UpdateCommentRequest_Vm;
import ahmed.com.springboot.friend_finder_system.dto.CommentDto;
import ahmed.com.springboot.friend_finder_system.dto.DtoSimble.User_Simple_Dto;
import ahmed.com.springboot.friend_finder_system.dto.UserDto;
import ahmed.com.springboot.friend_finder_system.mapper.CommentMapper;
import ahmed.com.springboot.friend_finder_system.mapper.PostMapper;
import ahmed.com.springboot.friend_finder_system.mapper.UserMapper;
import ahmed.com.springboot.friend_finder_system.mapper.UserSimpleMapper;
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

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
    private final UserSimpleMapper userSimpleMapper;




    //TODO:_______________ Implement Service Methods ____________________________






    //TODO:_______________ Create Comment ____________________________
    @Override
    public Void createComment(CommentRequest_Vm commentRequestVm) {
        Post post = postMapper.toEntity(postService.getPostById(commentRequestVm.getPostId()));
        User author = userMapper.toEntity(userService.getUserById(currentUser()));

        Comments comment1 = new Comments();
        comment1.setPost(post);
        comment1.setAuthor(author);
        comment1.setContent(commentRequestVm.getContent());

        commentRepo.save(comment1);
        return null;
    }


    //TODO:_______________ Delete Comment ____________________________
    @Override
    public void deleteComment(Long commentId) {

        if(Objects.isNull(commentId))
        {
            throw new RuntimeException("");
        }
       Optional<Comments> comments = commentRepo.findById(commentId);
        if(!(comments.get().getAuthor().getId() == currentUser() || comments.get().getPost().getAuthor().getId() == currentUser()))
        {
            throw new RuntimeException("cant.delete.this.comment");
        }

        commentRepo.deleteById(commentId);

    }

    //TODO:_______________ Update Comment ____________________________

    @Override
    public void updateComment(UpdateCommentRequest_Vm commentRequestVm) {

        Optional<Comments> comment = Optional.of(commentRepo.findById(commentRequestVm.getCommentId()).
                orElseThrow(() -> new RuntimeException("comment.not.found")));

        comment.get().setContent(commentRequestVm.getContent());

        commentRepo.save(comment.get());

    }

    //TODO:_______________ Reply To Comment ____________________________
    @Override
    public void replyToComment(UpdateCommentRequest_Vm commentRequestVm) {

        Optional<Comments> comment = Optional.of(commentRepo.findById(commentRequestVm.getCommentId()).
                orElseThrow(() -> new RuntimeException("comment.not.found")));

        Post post = postMapper.toEntity(postService.getPostById(comment.get().getPost().getId()));
        User author = userMapper.toEntity(userService.getUserById(currentUser()));
        Comments replyComment = new Comments();

        replyComment.setPost(post);
        replyComment.setAuthor(author);
        replyComment.setContent(commentRequestVm.getContent());
        replyComment.setParentComment(comment.get());
        comment.get().getReplies().add(replyComment);

        commentRepo.save(comment.get());
    }


    //TODO:_______________ Get Comments ____________________________
    @Override
    public List<CommentDto> getComments(Long postId, int pageNumber) {


        validatePageNumberAndSize(pageNumber, 3);

        Pageable pageable = PageRequest.of(pageNumber - 1, 3);

        Page<Comments> commentsPage =
                commentRepo.findByPost_Id(postId, pageable);

        if (commentsPage.isEmpty()) {
            throw new RuntimeException("comments.not.found");
        }

        Post post = postMapper.toEntity(postService.getPostById(postId));

        List<CommentDto> commentDtos = commentMapper.toDtoList(commentsPage.getContent());

        commentDtos.forEach(commentDto -> {
            commentDto.setPostId(postId);
            commentDto.setLikedIs(post.getCountLikes());
            commentDto.setCountComments(post.getCountComments());
        });

        return commentDtos;
    }

    @Override
    public List<CommentDto> getReplies(Long commentId, int pageNumber) {

        validatePageNumberAndSize(pageNumber , 3);

        if(!commentRepo.existsById(commentId))
        {
            throw new RuntimeException("comment.not.found");
        }

        Pageable pageable = PageRequest.of(pageNumber - 1 , 3);

        Page<Comments> replies = commentRepo.findByParentCommentId(commentId , pageable);


        return commentMapper.toDtoList(replies.getContent());
    }



    //TODO _________________validatePageNumberAndSize______________________
//TODO ________________________________________________________________
    boolean validatePageNumberAndSize(int pageNumber, int pageSize)
    {
        if (pageNumber < 1 || pageSize <= 0)
        {
            throw new IllegalArgumentException("page.number.invalid");
        }


        return true;
    }



    //TODO:_______________ Get CurrentUser ____________________________
    public Long currentUser()
    {
        UserDto currentUser = (UserDto) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = currentUser.getId();

        return userId;
    }
}
