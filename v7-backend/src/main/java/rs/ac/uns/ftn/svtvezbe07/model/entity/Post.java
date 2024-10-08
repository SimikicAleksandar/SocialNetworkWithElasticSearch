package rs.ac.uns.ftn.svtvezbe07.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "postsTable")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true, name = "postId")
    private Long id;
    @Column
    private String title;
    @Column(nullable = false)
    private Long user;
    @Column(nullable = false, name = "content")
    private String content;

    @Column(nullable = false, name = "creationDate")
    private LocalDateTime creationDate;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private Set<Reaction> reactions = new HashSet<Reaction>();
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private Set<Comment> comments = new HashSet<Comment>();

    @Column(nullable = true)
    private Boolean deleted;

    @OneToOne(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private File file;
}
