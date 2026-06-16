package ahmed.com.springboot.friend_finder_system.service;

import ahmed.com.springboot.friend_finder_system.Vm.Post_Response_Vm;
import ahmed.com.springboot.friend_finder_system.dto.PostDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface Post_Service {

    void creatPost(PostDto postDto);

    void updatePost(PostDto postDto);

    void deletePost(Long id);

    PostDto getPostById(Long id);

    void savePost(PostDto postDto);


    Post_Response_Vm getUserPosts( int pageNumber);

 //  List<PostDto> getAllPosts(Pageable pageable);

 //  void likePost(Long postId);

  // void unlikePost(Long postId);

 //  boolean isLiked(Long postId);

 //  List<LikeDto> getLikes(Long postId);


}
