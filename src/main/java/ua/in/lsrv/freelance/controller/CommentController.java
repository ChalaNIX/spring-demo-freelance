package ua.in.lsrv.freelance.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ua.in.lsrv.freelance.dto.CommentDto;
import ua.in.lsrv.freelance.entity.Comment;
import ua.in.lsrv.freelance.facade.CommentFacade;
import ua.in.lsrv.freelance.payload.MessageResponse;
import ua.in.lsrv.freelance.service.CommentService;
import ua.in.lsrv.freelance.validation.ResponseErrorValidator;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
@RequestMapping("/api/comment")
public class CommentController {
    private CommentService commentService;
    private CommentFacade commentFacade;
    private ResponseErrorValidator responseErrorValidator;

    public CommentController(CommentService commentService, CommentFacade commentFacade, ResponseErrorValidator responseErrorValidator) {
        this.commentService = commentService;
        this.commentFacade = commentFacade;
        this.responseErrorValidator = responseErrorValidator;
    }

    @PostMapping("/{jobId}/create")
    public ResponseEntity<Object> createComment(@Valid @RequestBody CommentDto commentDto,
                                                @PathVariable String jobId,
                                                BindingResult bindingResult, Principal principal) {
        ResponseEntity<Object> errors = responseErrorValidator.mapValidationService(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) {
            return errors;
        }

        Comment comment = commentService.createComment(Long.parseLong(jobId), commentDto, principal);
        CommentDto createdCommentDto = commentFacade.commentToDto(comment);
        return ResponseEntity.ok(createdCommentDto);
    }

    @GetMapping("/{jobId}/all")
    public ResponseEntity<List<CommentDto>> getAllCommentsForJob(@PathVariable String jobId) {
        List<CommentDto> commentDtoList = commentService.getAllJobComments(Long.parseLong(jobId))
                .stream().map(commentFacade::commentToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(commentDtoList);
    }

    @PostMapping("/{commentId}/delete")
    public ResponseEntity<MessageResponse> deleteComment(@PathVariable String commentId) {
        commentService.deleteComment(Long.parseLong(commentId));
        return ResponseEntity.ok(new MessageResponse("Comment was deleted"));
    }
}
