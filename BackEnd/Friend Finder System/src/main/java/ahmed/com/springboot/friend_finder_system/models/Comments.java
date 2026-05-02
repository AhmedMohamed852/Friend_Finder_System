package ahmed.com.springboot.friend_finder_system.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "comments")
public class Comments extends BaseEntity{

    @Column(name = "content" , nullable = false)
    private String content;

    //______________ Relationships _________________

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User author;


    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;



    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne
    @JoinColumn(name = "parent_comment_id")
    private Comments parentComment;


    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "parentComment")
    @Column(name = "replies")
    private Set<Comments> replies = new HashSet<>();
}
