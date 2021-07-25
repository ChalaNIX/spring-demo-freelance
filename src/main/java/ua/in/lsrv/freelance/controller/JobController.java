package ua.in.lsrv.freelance.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ua.in.lsrv.freelance.dto.JobDto;
import ua.in.lsrv.freelance.entity.Job;
import ua.in.lsrv.freelance.facade.JobFacade;
import ua.in.lsrv.freelance.payload.MessageResponse;
import ua.in.lsrv.freelance.service.JobService;
import ua.in.lsrv.freelance.validation.ResponseErrorValidator;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
@RequestMapping("/api/job")
public class JobController {
    private final JobService jobService;
    private final JobFacade jobFacade;
    private final ResponseErrorValidator responseErrorValidator;

    @Autowired
    public JobController(JobService jobService, JobFacade jobFacade, ResponseErrorValidator responseErrorValidator) {
        this.jobService = jobService;
        this.jobFacade = jobFacade;
        this.responseErrorValidator = responseErrorValidator;
    }

    @PostMapping("/create")
    public ResponseEntity<Object> createJob(@Valid @RequestBody JobDto jobDto, BindingResult bindingResult, Principal principal) {
        ResponseEntity<Object> errors = responseErrorValidator.mapValidationService(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) {
            return errors;
        }

        Job job = jobService.createJob(jobDto, principal);
        JobDto createdJobDto = jobFacade.jobToDto(job);
        return ResponseEntity.ok(createdJobDto);
    }

    @GetMapping("/all")
    public ResponseEntity<List<JobDto>> getAllJobs() {
        List<JobDto> allJobs = jobService.getAllJobs()
                .stream().map(jobFacade::jobToDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(allJobs);
    }

    @GetMapping("user/jobs")
    public ResponseEntity<List<JobDto>> getAllJobsForUser(Principal principal) {
        List<JobDto> allUserJobs = jobService.getAllJobsForUser(principal)
                .stream().map(jobFacade::jobToDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(allUserJobs);
    }

    @PostMapping("/delete/{jobId}")
    public ResponseEntity<MessageResponse> deleteJob(@PathVariable String jobId, Principal principal) {
        jobService.deleteJob(Long.parseLong(jobId), principal);
        return ResponseEntity.ok(new MessageResponse("Job is deleted"));
    }

    @GetMapping("/{jobId}")
    public ResponseEntity<JobDto> getJobById(@PathVariable String jobId) {
        Job job = jobService.getJobById(Long.parseLong(jobId));
        JobDto jobDto = jobFacade.jobToDto(job);
        return ResponseEntity.ok(jobDto);
    }
}
