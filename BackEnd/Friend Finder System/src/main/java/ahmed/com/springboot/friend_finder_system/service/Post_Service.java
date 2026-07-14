package ahmed.com.springboot.friend_finder_system.service;

import ahmed.com.springboot.friend_finder_system.Vm.Post_Response_Vm;
import ahmed.com.springboot.friend_finder_system.dto.PostDto;
import ahmed.com.springboot.friend_finder_system.models.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface Post_Service {

    void creatPost(PostDto postDto);

    void updatePost(PostDto postDto);

    void deletePost(Long id);

    PostDto getPostById(Long id);


    //Page<Post> getListOfPosts(List<Long> ids , Pageable pageable);

    Post_Response_Vm getPostsById(Long id, int pageNumber);

    void savePost(PostDto postDto);

    Post_Response_Vm findHomeFeed( int pageNumber);

 //  List<PostDto> getAllPosts(Pageable pageable);

 //  void likePost(Long postId);

 //  boolean isLiked(Long postId);

 //  List<LikeDto> getLikes(Long postId);


}
