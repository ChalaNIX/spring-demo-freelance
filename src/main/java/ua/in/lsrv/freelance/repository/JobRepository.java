package ua.in.lsrv.freelance.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.in.lsrv.freelance.entity.Job;
import ua.in.lsrv.freelance.entity.User;

import java.util.List;
import java.util.Optional;

public interface JobRepository extends JpaRepository<Job, Long> {
    List<Job> findAllByUserOrderByCreateDateDesc(User user);

    Optional<Job> findJobByIdAndUser(long id, User user);

    Optional<Job> findJobById(long id);

    List<Job> findAllByOrderByCreateDateDesc();
}
