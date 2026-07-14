package ahmed.com.springboot.friend_finder_system.repo;

import ahmed.com.springboot.friend_finder_system.models.Comments;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface Comment_Repo extends JpaRepository<Comments, Long> {

    Optional<List<Comments>> findByPost_Id(Long postId);

    Page<Comments> findByPost_IdAndParentCommentIsNull(Long postId, Pageable pageable);

    Page<Comments> findByParentCommentId(Long commentId, Pageable pageable);


    @Query("""
       SELECT c.post.id, COUNT(c)
       FROM Comments c
       WHERE c.post.id IN :postIds
       GROUP BY c.post.id
       """)
    List<Object[]> countCommentsByPostIds(@Param("postIds") List<Long> postIds);
}
