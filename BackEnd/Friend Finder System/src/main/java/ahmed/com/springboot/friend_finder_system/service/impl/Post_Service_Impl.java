package ahmed.com.springboot.friend_finder_system.service.impl;

import ahmed.com.springboot.friend_finder_system.GlobalExService.PaginationEx;
import ahmed.com.springboot.friend_finder_system.GlobalExService.PostsEx;
import ahmed.com.springboot.friend_finder_system.Vm.Post_Response_Vm;
import ahmed.com.springboot.friend_finder_system.dto.PostDto;
import ahmed.com.springboot.friend_finder_system.dto.UserDto;
import ahmed.com.springboot.friend_finder_system.globalCurrentUserId.CurrentUser;
import ahmed.com.springboot.friend_finder_system.mapper.MediaMapper;
import ahmed.com.springboot.friend_finder_system.mapper.PostMapper;
import ahmed.com.springboot.friend_finder_system.mapper.UserMapper;
import ahmed.com.springboot.friend_finder_system.models.Media;
import ahmed.com.springboot.friend_finder_system.models.Post;
import ahmed.com.springboot.friend_finder_system.models.User;
import ahmed.com.springboot.friend_finder_system.repo.Comment_Repo;
import ahmed.com.springboot.friend_finder_system.repo.Like_Repo;
import ahmed.com.springboot.friend_finder_system.repo.Post_Repo;
import ahmed.com.springboot.friend_finder_system.service.Match_Service;
import ahmed.com.springboot.friend_finder_system.service.Post_Service;
import ahmed.com.springboot.friend_finder_system.service.User_Service;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class Post_Service_Impl implements Post_Service {

    //TODO: Declare Service Methods

    private final Post_Repo post_Repo;
    private final PostMapper postMapper;
    private final MediaMapper mediaMapper;
    private final User_Service user_Service;
    private final UserMapper userMapper;
    private final Comment_Repo commentRepo;
    @Lazy
    private final Match_Service matchService;
    private final Like_Repo likeRepo;




    //TODO:_______________ Implement Service Methods ____________________________



    //TODO:_______________ Create New Post ____________________________
    @Override
    public void creatPost(PostDto postDto) {

        Post post = postMapper.toEntity(postDto);

        if(Objects.nonNull(postDto.getMedia()))
        {
            Set<Media> media = postDto.getMedia().stream().map(mediaa ->
            {
                Media newMedia = mediaMapper.toEntity(mediaa);
                newMedia.setPost(post);
                return newMedia;
            }).collect(Collectors.toSet());
            post.setMedia(media);
        }

        User author = userMapper.toEntity(user_Service.getUserById(CurrentUser.currentUserId()));
        post.setAuthor(author);
        post_Repo.save(post);
    }


    //TODO:_______________ Update Post ____________________________
    @Override
    public void updatePost(PostDto postDto) {

        if(postDto.getId() == null)
        {
            throw PostsEx.postIdRequired();
        }
         post_Repo.findById(postDto.getId()).orElseThrow(PostsEx::postNotFound);

        Post post = postMapper.toEntity(postDto);

        if(Objects.nonNull(postDto.getMedia()))
        {
            Set<Media> media = postDto.getMedia().stream().map(mediaa ->
            {
                Media newMedia = mediaMapper.toEntity(mediaa);
                newMedia.setPost(post);
                return newMedia;
            }).collect(Collectors.toSet());
            post.setMedia(media);
        }

        User author = userMapper.toEntity(user_Service.getUserById(CurrentUser.currentUserId()));
        post.setAuthor(author);
        post_Repo.save(post);
    }



    //TODO:_______________ Add Like To Post ____________________________
    @Override
    public void savePost(PostDto postDto) {
        Post post = postMapper.toEntity(postDto);
        post_Repo.save(post);
    }


    //TODO:_______________ Delete Post ____________________________
    @Override
    public void deletePost(Long id) {

        Post post = post_Repo.findById(id).orElseThrow(PostsEx::postNotFound);
        post_Repo.delete(post);

        System.out.println("DELETE");
    }



    //TODO:_______________ Get Post By ID ____________________________
    @Override
    public PostDto getPostById(Long id) {

        if(id == null)
        {
            throw PostsEx.postIdRequired();
        }
        if(!post_Repo.existsById(id))
        {
            throw PostsEx.postNotFound();
        }
        Post post = post_Repo.findById(id).orElseThrow(PostsEx::postNotFound);
        List<PostDto> postDto = List.of(postMapper.toDto(post));

        List<Long> postIds = postDto.stream().map(PostDto::getId).toList();
        Map<Long, Long> commentsCountMap = CountComments(postIds);

        postDto.stream().forEach(dto ->
                dto.setCountComments(commentsCountMap.getOrDefault(dto.getId(), 0L).intValue()));

        return postDto.get(0);

    }


    //TODO:_______________ Get Posts By ID ____________________________
    @Override
    public Post_Response_Vm getPostsById(Long id , int pageNumber) {

        if(!post_Repo.existsByAuthorId(id))
        {
            throw PostsEx.postNotFound();
        }

        validatePageNumberAndSize(pageNumber, 5);

        Pageable pageable = PageRequest.of(pageNumber - 1, 5);

        Page<Post> posts = post_Repo.findAllByAuthorId(id,pageable);

        if (posts.getContent().isEmpty())
        {
            throw PostsEx.postNotFound();
        }

        posts.getContent().stream().forEach(post -> post.getAuthor().setPassword(null));

        List<PostDto> postDtoList = postMapper.toDtoList(posts.getContent());

        List<Long> postIds = postDtoList.stream().map(PostDto::getId).toList();
        Set<Long> likedPostIds = likeRepo.findLikedPostIds( CurrentUser.currentUserId() , postIds);

        postDtoList.forEach(dto -> dto.setLikedIs(likedPostIds.contains(dto.getId())));

        return new Post_Response_Vm(postDtoList, posts.getTotalElements());
    }


    //TODO:_______________ Get User Posts ____________________________
    @Override
    public Post_Response_Vm findHomeFeed(int pageNumber) {

        validatePageNumberAndSize(pageNumber, 5);

        Pageable pageable = PageRequest.of(pageNumber - 1, 5);

        Page<Post> posts = matchService.findHomeFeed(pageable);

        if (posts.getContent().isEmpty()) {
            throw PostsEx.postNotFound();
        }

        posts.getContent().forEach(post -> post.getAuthor().setPassword(null));

        List<PostDto> postDtoList = postMapper.toDtoList(posts.getContent());

        List<Long> postIds = postDtoList.stream().map(PostDto::getId).toList();
        Set<Long> likedPostIds = likeRepo.findLikedPostIds(CurrentUser.currentUserId(), postIds);

        Map<Long, Long> commentsCountMap = CountComments(postIds);

        postDtoList.forEach(dto -> {
            dto.setLikedIs(likedPostIds.contains(dto.getId()));
            dto.setCountComments(
                    commentsCountMap.getOrDefault(dto.getId(), 0L).intValue()
            );
        });
        return new Post_Response_Vm(postDtoList, posts.getTotalElements());
    }



//TODO _________________validatePageNumberAndSize______________________
//TODO ________________________________________________________________
    boolean validatePageNumberAndSize(int pageNumber, int pageSize)
    {
        if (pageNumber < 1 || pageSize <= 0)
        {
            throw PaginationEx.invalidPageNumber();
        }

        return true;
    }



    public Map<Long, Long> CountComments(List<Long> postIds)
    {
        Map<Long, Long> commentsCountMap = new HashMap<>();

        List<Object[]> result = commentRepo.countCommentsByPostIds(postIds);

        for (Object[] row : result) {
            commentsCountMap.put(
                    (Long) row[0],
                    (Long) row[1]
            );
        }
        return commentsCountMap;

    }
}
