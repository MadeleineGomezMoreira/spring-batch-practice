package com.springbatchpractice.config;

import com.springbatchpractice.listener.FirstJobListener;
import com.springbatchpractice.listener.FirstStepListener;
import com.springbatchpractice.processor.FirstItemProcessor;
import com.springbatchpractice.reader.FirstItemReader;
import com.springbatchpractice.service.SecondTasklet;
import com.springbatchpractice.writer.FirstItemWriter;
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
    private FirstStepListener firstStepListener;
    private FirstItemReader firstItemReader;
    private FirstItemProcessor firstItemProcessor;
    private FirstItemWriter firstItemWriter;

    //By commenting the @Bean annotation, the job does not run
    //@Bean
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
                .listener(firstStepListener)
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
                System.out.println("SEC " + chunkContext.getStepContext());
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

    @Bean
    public Job secondJob() {
        return new JobBuilder("secondJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(firstChunkStep())
                .next(secondStep())
                .build();
    }

    private Step firstChunkStep(){
        return new StepBuilder("First Chunk Step", jobRepository)
                .<Integer,Long>chunk(3)
                //ItemReader MUST always be provided in Chunk oriented steps
                .reader(firstItemReader)
                //the processor is necessary when the reader output and the writer input do not match
                //otherwise it is optional
                .processor(firstItemProcessor)
                //ItemWriter MUST always be provided in Chunk oriented steps
                .writer(firstItemWriter)
                .build();
    }
}
