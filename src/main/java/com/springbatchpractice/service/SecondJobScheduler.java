package com.springbatchpractice.service;

import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.JobExecution;
import org.springframework.batch.core.job.parameters.JobParameter;
import org.springframework.batch.core.job.parameters.JobParameters;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

//@Service
public class SecondJobScheduler {

    @Autowired
    private JobOperator jobOperator;

    @Autowired
    @Qualifier("secondJob")
    private Job secondJob;


    //@Scheduled(cron = "0/1 0/1 0 ? * *")
    public void secondJobScheduler(){
        Set<JobParameter<?>> params = new HashSet<>();
        params.add(new JobParameter<>("currentTime", System.currentTimeMillis(), Long.class));

        JobParameters jobParams = new JobParameters(params);

        try {
            JobExecution jobExecution = jobOperator.start(secondJob, jobParams);
            System.out.println("jobExecution ID: " + jobExecution.getJobInstanceId());
        } catch (Exception e) {
            System.out.println("Exception while starting job");
        }

    }

}
