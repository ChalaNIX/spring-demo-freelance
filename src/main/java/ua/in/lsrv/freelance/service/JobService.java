package ua.in.lsrv.freelance.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.in.lsrv.freelance.dto.JobDto;
import ua.in.lsrv.freelance.entity.Job;
import ua.in.lsrv.freelance.entity.User;
import ua.in.lsrv.freelance.exception.JobNotFoundException;
import ua.in.lsrv.freelance.repository.JobRepository;
import ua.in.lsrv.freelance.util.UserPrincipalUtil;

import java.security.Principal;
import java.util.List;

@Service
public class JobService {
    private static final Logger LOGGER = LoggerFactory.getLogger(JobService.class.getName());

    private JobRepository jobRepository;
    private UserPrincipalUtil userPrincipalUtil;

    @Autowired
    public JobService(JobRepository jobRepository, UserPrincipalUtil userPrincipalUtil) {
        this.jobRepository = jobRepository;
        this.userPrincipalUtil = userPrincipalUtil;
    }

    public Job createJob(JobDto jobDto, Principal principal) {
        User user = userPrincipalUtil.getUserByPrincipal(principal);

        Job job = new Job();
        job.setTitle(jobDto.getTitle());
        job.setDescription(jobDto.getDescription());
        job.setPrice(jobDto.getPrice());
        job.setUser(user);

        LOGGER.info("Saving new job for user " + user.getUsername());
        return jobRepository.save(job);
    }

    public List<Job> getAllJobs() {
        return jobRepository.findAllByOrderByCreateDateDesc();
    }

    public List<Job> getAllJobsForUser(Principal principal) {
        User user = userPrincipalUtil.getUserByPrincipal(principal);
        return jobRepository.findAllByUserOrderByCreateDateDesc(user);
    }

    public Job getJobById(long jobId) {
        Job job = jobRepository.findJobById(jobId)
                .orElseThrow(() -> new JobNotFoundException("Cannot find job with id" + jobId));
        return job;
    }

    public void deleteJob(long jobId, Principal principal) {
        User user = userPrincipalUtil.getUserByPrincipal(principal);
        Job job = jobRepository.findJobByIdAndUser(jobId, user)
                .orElseThrow(() -> new JobNotFoundException("Cannot find job with id" + jobId+ " for user " + user.getUsername()));
        jobRepository.delete(job);
    }
}
