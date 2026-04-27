package ahmed.com.springboot.friend_finder_system.models;

import ahmed.com.springboot.friend_finder_system.eNum.PostPrivacy;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "posts")
public class Post extends BaseEntity {


    @Column(name = "content" , nullable = false)
    private String content;

    @Column(name = "image_or_video")
    private String imageOrVideo;

    @Column(name = "count_likes")
    private Integer countLikes;

    @Column(name = "count_comments")
    private Integer countComments;

    @Column(name = "privacy")
    @Enumerated(EnumType.STRING)
    private PostPrivacy privacy;

    // _______________relations__________________________________

    @ManyToOne
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @jakarta.persistence.JoinColumn(name = "author_id")
    private User author;



    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "post")
    @Column(name = "comments")
    private Set<Comment> comments = new HashSet<>();

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "post")
    private Set<Like> likes = new HashSet<>();

}
