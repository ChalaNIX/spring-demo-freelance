package ua.in.lsrv.freelance.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @ManyToOne(targetEntity = Job.class, fetch = FetchType.LAZY)
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
}
