package com.springbatchpractice.listener;

import org.springframework.batch.core.job.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListener;
import org.springframework.stereotype.Component;

@Component
public class FirstJobListener implements JobExecutionListener {
    @Override
    public void beforeJob(JobExecution jobExecution){
        System.out.println("Before Job: " + jobExecution.getJobInstance().getJobName());
        System.out.println("Job Parameters: " + jobExecution.getJobParameters());
        System.out.println("Execution Context: " + jobExecution.getExecutionContext());

        jobExecution.getExecutionContext().put("jec", "jec value");
    }

    @Override
    public void afterJob(JobExecution jobExecution){
        System.out.println("After Job: " + jobExecution.getJobInstance().getJobName());
        System.out.println("Job Parameters: " + jobExecution.getJobParameters());
        System.out.println("Execution Context: " + jobExecution.getExecutionContext());
    }
}
