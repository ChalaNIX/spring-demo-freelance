package ua.in.lsrv.freelance.facade;

import org.springframework.stereotype.Component;
import ua.in.lsrv.freelance.dto.JobDto;
import ua.in.lsrv.freelance.entity.Job;

@Component
public class JobFacade {
    public JobDto jobToDto(Job job) {
        JobDto jobDto = new JobDto();
        jobDto.setId(job.getId());
        jobDto.setTitle(job.getTitle());
        jobDto.setDescription(job.getDescription());
        jobDto.setPrice(job.getPrice());

        return jobDto;
    }
}
