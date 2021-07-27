package ua.in.lsrv.freelance.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.in.lsrv.freelance.entity.Comment;
import ua.in.lsrv.freelance.entity.Job;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment>  findAllByJobOrderByCreateDateDesc(Job job);
    Optional<Comment> findById(long id);
}
