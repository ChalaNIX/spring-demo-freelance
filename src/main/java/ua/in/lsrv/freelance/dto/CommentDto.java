package ua.in.lsrv.freelance.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
public class CommentDto {
    private long id;
    @NotBlank
    private String message;
    private String username;
    private LocalDateTime commentDate;
}
