package ua.in.lsrv.freelance.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.in.lsrv.freelance.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findUserByUsername(String username);

    Optional<User> findUserById(long id);

}
