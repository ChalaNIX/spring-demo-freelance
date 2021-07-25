package ua.in.lsrv.freelance.facade;

import org.springframework.stereotype.Component;
import ua.in.lsrv.freelance.dto.CommentDto;
import ua.in.lsrv.freelance.entity.Comment;

@Component
public class CommentFacade {
    public CommentDto commentToDto(Comment comment) {
        CommentDto commentDto = new CommentDto();
        commentDto.setMessage(comment.getMessage());
        commentDto.setUsername(comment.getUser().getUsername());
        commentDto.setCommentDate(comment.getCreateDate().toString());

        return commentDto;
    }
}
