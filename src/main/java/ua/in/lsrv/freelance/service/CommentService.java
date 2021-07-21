package ua.in.lsrv.freelance.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.in.lsrv.freelance.dto.CommentDto;
import ua.in.lsrv.freelance.entity.Comment;
import ua.in.lsrv.freelance.entity.Job;
import ua.in.lsrv.freelance.entity.User;
import ua.in.lsrv.freelance.exception.CommentNotFoundException;
import ua.in.lsrv.freelance.exception.JobNotFoundException;
import ua.in.lsrv.freelance.repository.CommentRepository;
import ua.in.lsrv.freelance.repository.JobRepository;
import ua.in.lsrv.freelance.util.UserPrincipalUtil;

import java.security.Principal;
import java.util.List;

@Service
public class CommentService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CommentService.class.getName());

    private CommentRepository commentRepository;
    private JobRepository jobRepository;
    private UserPrincipalUtil userPrincipalUtil;

    @Autowired
    public CommentService(CommentRepository commentRepository, JobRepository jobRepository, UserPrincipalUtil userPrincipalUtil) {
        this.commentRepository = commentRepository;
        this.jobRepository = jobRepository;
        this.userPrincipalUtil = userPrincipalUtil;
    }


    public Comment createComment(long jobId, CommentDto commentDto, Principal principal) {
        User user = userPrincipalUtil.getUserByPrincipal(principal);
        Job job = jobRepository.findJobById(jobId)
                .orElseThrow(() -> new JobNotFoundException("Cannot find job with id " + jobId));

        Comment comment = new Comment();
        comment.setJob(job);
        comment.setUser(user);
        comment.setMessage(commentDto.getMessage());

        LOGGER.info("Saving comment for Job: " + job.getId());
        return commentRepository.save(comment);
    }

    public List<Comment> getAllJobComments(long jobId) {
        Job job = jobRepository.findJobById(jobId)
                .orElseThrow(() -> new JobNotFoundException("Cannot find job with id " + jobId));
        return commentRepository.findAllByJob(job);
    }

    public void deleteComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException("Cannot find comment with id " + commentId));
        commentRepository.delete(comment);
    }
}
