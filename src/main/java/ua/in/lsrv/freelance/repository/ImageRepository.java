package ua.in.lsrv.freelance.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.in.lsrv.freelance.entity.Image;

import java.util.Optional;

public interface ImageRepository extends JpaRepository<Image, Long> {
    Optional<Image> findByUserId(long id);
}
