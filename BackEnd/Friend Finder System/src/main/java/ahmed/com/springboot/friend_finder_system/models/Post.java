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
    @OneToMany(mappedBy = "post"  , cascade = CascadeType.ALL)
    @Column(name = "comments")
    private Set<Comments> comments = new HashSet<>();

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "post"  , cascade = CascadeType.ALL)
    private Set<Like> likes = new HashSet<>();




    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "post", cascade =  CascadeType.ALL)
    private Set<Media> media = new HashSet<>();




}
