package com.springbatchpractice.config;

import com.springbatchpractice.listener.FirstJobListener;
import com.springbatchpractice.service.SecondTasklet;
import lombok.AllArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.parameters.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.StepContribution;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.infrastructure.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@AllArgsConstructor
public class SampleJob {

    private JobRepository jobRepository;
    private PlatformTransactionManager transactionManager;
    private SecondTasklet secondTasklet;
    private FirstJobListener firstJobListener;

    @Bean
    public Job firstJob() {
        return new JobBuilder("firstJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(firstStep())
                .next(secondStep())
                .listener(firstJobListener)
                .build();
    }

    @Bean
    public Step firstStep(){
        return new StepBuilder("firstStep", jobRepository)
                .tasklet(firstTask(), transactionManager)
                .build();
    }

    @Bean
    public Step secondStep(){
        return new StepBuilder("secondStep", jobRepository)
                .tasklet(secondTasklet, transactionManager)
                .build();
    }

    private Tasklet firstTask(){
        return new Tasklet() {
            @Override
            public @Nullable RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                System.out.println("This is the first tasklet step");
                return RepeatStatus.FINISHED;
            }
        };
    }

//    private Tasklet secondTask(){
//        return new Tasklet() {
//            @Override
//            public @Nullable RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
//                System.out.println("This is the second tasklet step");
//                return RepeatStatus.FINISHED;
//            }
//        };
//    }

}
