package com.springbatchpractice.service;

import com.springbatchpractice.request.JobParamsRequest;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.JobExecution;
import org.springframework.batch.core.job.parameters.JobParameter;
import org.springframework.batch.core.job.parameters.JobParameters;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class JobService {

    @Autowired
    private JobOperator jobOperator;

    @Autowired
    @Qualifier("firstJob")
    private Job firstJob;

    @Autowired
    @Qualifier("secondJob")
    private Job secondJob;

    @Async
    public void startJob(String jobName, List<JobParamsRequest> jobParamsRequestList) {
        Set<JobParameter<?>> params = new HashSet<>();
        params.add(new JobParameter<>("currentTime", System.currentTimeMillis(), Long.class));

        jobParamsRequestList.forEach(jobParamsRequest -> {
            params.add(new JobParameter<>(jobParamsRequest.getParamKey(), jobParamsRequest.getParamValue(), String.class));
        });

        JobParameters jobParams = new JobParameters(params);

        try {
            JobExecution jobExecution;
            if ("firstJob".equals(jobName)) {
                jobExecution = jobOperator.start(firstJob, jobParams);
            } else {
                jobExecution = jobOperator.start(secondJob, jobParams);
            }
            System.out.println("jobExecution ID: " + jobExecution.getJobInstanceId());
        } catch (Exception e) {
            System.out.println("Exception while starting job");
        }
    }
}
