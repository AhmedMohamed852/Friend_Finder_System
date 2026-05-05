package ahmed.com.springboot.friend_finder_system.service.impl;

import ahmed.com.springboot.friend_finder_system.dto.PostDto;
import ahmed.com.springboot.friend_finder_system.mapper.MediaMapper;
import ahmed.com.springboot.friend_finder_system.mapper.PostMapper;
import ahmed.com.springboot.friend_finder_system.mapper.UserMapper;
import ahmed.com.springboot.friend_finder_system.models.Media;
import ahmed.com.springboot.friend_finder_system.models.Post;
import ahmed.com.springboot.friend_finder_system.models.User;
import ahmed.com.springboot.friend_finder_system.repo.Media_Repo;
import ahmed.com.springboot.friend_finder_system.repo.Post_Repo;
import ahmed.com.springboot.friend_finder_system.service.Post_Service;
import ahmed.com.springboot.friend_finder_system.service.User_Service;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class Post_Service_Impl implements Post_Service {

    //TODO: Declare Service Methods

    private final Post_Repo post_Repo;
    private final PostMapper postMapper;
    private final Media_Repo media_Repo;
    private final MediaMapper mediaMapper;
    private final User_Service user_Service;
    private final UserMapper userMapper;




    //TODO:_______________ Implement Service Methods ____________________________



    //TODO:_______________ Create New Post ____________________________
    @Override
    public void creatPost(PostDto postDto, Long userId) {

        if(Objects.isNull(postDto))
        {
            throw new RuntimeException("error.post.not.found");
        }

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

        User author = userMapper.toEntity(user_Service.getUserById(userId));
        post.setAuthor(author);
        post_Repo.save(post);
    }

    @Override
    public void updatePost(PostDto postDto) {

    }

    @Override
    public void deletePost(Long id) {

    }

    @Override
    public PostDto getPostById(Long id) {
        return null;
    }

    @Override
    public PostDto getPostByUserId(Long userId) {
        return null;
    }

    @Override
    public List<PostDto> getUserPosts(Long userId, Pageable pageable) {
        return List.of();
    }
}
