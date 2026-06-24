package ahmed.com.springboot.friend_finder_system.repo;

import ahmed.com.springboot.friend_finder_system.models.Comments;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface Comment_Repo extends JpaRepository<Comments, Long> {

    Optional<List<Comments>> findByPost_Id(Long postId);

    Page<Comments> findByPost_Id(Long postId, Pageable pageable);
    Page<Comments> findByParentCommentId(Long commentId, Pageable pageable);
}
