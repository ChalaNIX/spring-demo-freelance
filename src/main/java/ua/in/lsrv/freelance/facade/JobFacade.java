package ua.in.lsrv.freelance.facade;

import org.springframework.stereotype.Component;
import ua.in.lsrv.freelance.dto.JobDto;
import ua.in.lsrv.freelance.entity.Comment;
import ua.in.lsrv.freelance.entity.Job;

import java.util.List;

@Component
public class JobFacade {
    public JobDto jobToDto(Job job) {
        JobDto jobDto = new JobDto();
        jobDto.setId(job.getId());
        jobDto.setTitle(job.getTitle());
        jobDto.setDescription(job.getDescription());
        jobDto.setPrice(job.getPrice());
        jobDto.setUser(job.getUser().getName() + " " + job.getUser().getLastname());
        List<Comment> commentList = job.getCommentList();
        jobDto.setNoOfComments(commentList != null ? commentList.size() : 0);

        return jobDto;
    }
}
