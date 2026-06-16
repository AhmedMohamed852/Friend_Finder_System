package ahmed.com.springboot.friend_finder_system.repo;

import ahmed.com.springboot.friend_finder_system.models.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Post_Repo extends JpaRepository<Post, Long> {

    Page<Post> findAllByAuthorId(Long userId, Pageable pageable);}
