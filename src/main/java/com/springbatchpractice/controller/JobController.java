package com.springbatchpractice.controller;

import com.springbatchpractice.request.JobParamsRequest;
import com.springbatchpractice.service.JobService;
import lombok.AllArgsConstructor;
import org.springframework.batch.core.job.JobExecution;
import org.springframework.batch.core.job.JobInstance;
import org.springframework.batch.core.job.parameters.JobParameters;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/job")
public class JobController {

    private JobOperator jobOperator;
    private JobRepository jobRepository;
    private JobService jobService;

    @GetMapping("/start/{jobName}")
    public String startJob(@PathVariable String jobName, @RequestBody List<JobParamsRequest> jobParamsRequestList) {
        jobService.startJob(jobName, jobParamsRequestList);
        return "Job Started...";
    }

    @GetMapping("/stop/{jobExecutionId}")
    public String stopJob(@PathVariable long jobExecutionId) {

        try{
            JobExecution jobExecution = jobRepository.getJobExecution(jobExecutionId);
            assert jobExecution != null;
            jobOperator.stop(jobExecution);
        } catch(Exception e){
            System.out.println("Exception while stopping job");
        }
        return "Job Stopped...";
    }
}
