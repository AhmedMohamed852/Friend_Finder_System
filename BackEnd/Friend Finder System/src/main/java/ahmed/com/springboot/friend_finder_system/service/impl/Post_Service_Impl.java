package ahmed.com.springboot.friend_finder_system.service.impl;

import ahmed.com.springboot.friend_finder_system.Vm.Post_Response_Vm;
import ahmed.com.springboot.friend_finder_system.dto.PostDto;
import ahmed.com.springboot.friend_finder_system.dto.UserDto;
import ahmed.com.springboot.friend_finder_system.mapper.MediaMapper;
import ahmed.com.springboot.friend_finder_system.mapper.PostMapper;
import ahmed.com.springboot.friend_finder_system.mapper.UserMapper;
import ahmed.com.springboot.friend_finder_system.models.Media;
import ahmed.com.springboot.friend_finder_system.models.Post;
import ahmed.com.springboot.friend_finder_system.models.User;
import ahmed.com.springboot.friend_finder_system.repo.Post_Repo;
import ahmed.com.springboot.friend_finder_system.service.Post_Service;
import ahmed.com.springboot.friend_finder_system.service.User_Service;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;



import java.util.Objects;
import java.util.Set;
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

        User author = userMapper.toEntity(user_Service.getUserById(getUserId()));
        post.setAuthor(author);
        post_Repo.save(post);
    }


    //TODO:_______________ Update Post ____________________________
    @Override
    public void updatePost(PostDto postDto) {

        if(postDto.getId() == null)
        {
            throw new RuntimeException("error.post.id.is.required");
        }
         post_Repo.findById(postDto.getId()).orElseThrow(() -> new RuntimeException("error.post.not.found"));

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

        User author = userMapper.toEntity(user_Service.getUserById(getUserId()));
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

        Post post = post_Repo.findById(id).orElseThrow(() -> new RuntimeException("error.post.not.found"));
        post_Repo.delete(post);
    }



    //TODO:_______________ Get Post By ID ____________________________
    @Override
    public PostDto getPostById(Long id) {

        if(id == null)
        {
            throw new RuntimeException("error.post.id.is.required");
        }
        if(!post_Repo.existsById(id))
        {
            throw new RuntimeException("error.post.not.found");
        }
        Post post = post_Repo.findById(id).orElseThrow(() -> new RuntimeException("error.post.not.found"));
        return postMapper.toDto(post);

    }



    //TODO:_______________ Get User Posts ____________________________
    @Override
    public Post_Response_Vm getUserPosts(int pageNumber) {


        validatePageNumberAndSize(pageNumber, 5);

        Pageable pageable = PageRequest.of(pageNumber - 1, 5);

        Page<Post> posts = post_Repo.findAllByAuthorId(getUserId(),pageable);


        if (posts.getContent().isEmpty())
        {
           throw new RuntimeException("error.post.not.found");
        }

        return new Post_Response_Vm(postMapper.toDtoList(posts.getContent()),posts.getTotalElements());

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



    public Long getUserId()
    {
        UserDto currentUser = (UserDto) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = currentUser.getId();

        return userId;
    }
}
