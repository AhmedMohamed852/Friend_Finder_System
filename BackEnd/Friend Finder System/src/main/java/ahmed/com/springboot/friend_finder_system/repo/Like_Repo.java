package ahmed.com.springboot.friend_finder_system.repo;

import ahmed.com.springboot.friend_finder_system.models.Comments;
import ahmed.com.springboot.friend_finder_system.models.Like;
import ahmed.com.springboot.friend_finder_system.models.Post;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface Like_Repo extends JpaRepository<Like, Long> {



    // boolean existsByUserIdAndPost_id(Long userId, Long postId);
    boolean existsByUserIdAndPostId(Long userId, Long postId);

    void deleteByUserIdAndPostId(Long userId, Long postId);

    Optional<Like> findByUserIdAndPostId(Long userId, Long postId);

    boolean existsByPostIdAndUserId(Long postId, Long aLong);

    @Query("SELECT l.post.id FROM Like l WHERE l.user.id = :userId AND l.post.id IN :postIds")
    Set<Long> findLikedPostIds(@Param("userId") Long userId, @Param("postIds") List<Long> postIds);



/*
    @Query("""
       SELECT c
       FROM Comment c
       WHERE c.post.id IN :postIds
       ORDER BY c.createdAt ASC
       """)
    Set<Long> findByPostIds(@Param("postIds") List<Long> postIds);
*/


}
