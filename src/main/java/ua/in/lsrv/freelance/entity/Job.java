package ua.in.lsrv.freelance.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
public class Job {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "text")
    private String description;

    @Column
    private double price;

    @Column(updatable = false)
    private LocalDateTime createDate;

    @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER, mappedBy = "job", orphanRemoval = true)
    private List<Comment> commentList;

    @PrePersist
    private void onCreate() {
        this.createDate = LocalDateTime.now();
    }
}
