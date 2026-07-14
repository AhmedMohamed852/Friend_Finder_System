package ahmed.com.springboot.friend_finder_system.repo;

import ahmed.com.springboot.friend_finder_system.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface User_Repo extends JpaRepository<User, Long> {

    boolean existsByUsername(String username);
    boolean existsByEmail(String username);
    boolean existsByPassword(String password);

    @Query("select u from User u join fetch u.roles where u.username = :username")
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);


    @Query("""
    SELECT u
    FROM User u
    WHERE LOWER(u.firstName) LIKE LOWER(CONCAT(:keyword, '%'))
       OR LOWER(CONCAT(u.firstName, ' ', u.lastName))
       LIKE LOWER(CONCAT( :keyword, '%'))
""")
    Page<User> search(@Param("keyword") String keyword, Pageable pageable);

    @Query("""
    SELECT COUNT(u) > 0
    FROM User u
    WHERE 
        LOWER(u.firstName) LIKE LOWER(CONCAT(:keyword, '%'))
       OR LOWER(CONCAT(u.firstName, ' ', u.lastName))
            LIKE LOWER(CONCAT( :keyword, '%'))
""")
    boolean existsByKeyword(@Param("keyword") String keyword);

}
