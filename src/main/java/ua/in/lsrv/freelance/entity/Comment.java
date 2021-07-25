package ua.in.lsrv.freelance.entity;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@NoArgsConstructor
@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @ManyToOne(targetEntity = Job.class, fetch = FetchType.LAZY)
    @ToString.Exclude
    private Job job;

    @Column(columnDefinition = "text", nullable = false)
    private String message;

    @ManyToOne(targetEntity = User.class)
    private User user;

    @Column(updatable = false)
    private LocalDateTime createDate;

    @PrePersist
    private void onCreate() {
        this.createDate = LocalDateTime.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Comment comment = (Comment) o;

        return Objects.equals(id, comment.id);
    }
}
