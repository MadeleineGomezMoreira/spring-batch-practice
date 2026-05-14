package com.springbatchpractice.config;

import com.springbatchpractice.listener.FirstJobListener;
import com.springbatchpractice.listener.FirstStepListener;
import com.springbatchpractice.model.StudentCsv;
import com.springbatchpractice.model.StudentJdbc;
import com.springbatchpractice.model.StudentJson;
import com.springbatchpractice.model.StudentXml;
import com.springbatchpractice.processor.FirstItemProcessor;
import com.springbatchpractice.processor.StudentProcessor;
import com.springbatchpractice.reader.FirstItemReader;
import com.springbatchpractice.service.SecondTasklet;
import com.springbatchpractice.service.StudentService;
import com.springbatchpractice.writer.FirstItemWriter;
import com.springbatchpractice.writer.JdbcItemWriter;
import com.springbatchpractice.writer.JsonItemWriter;
import com.springbatchpractice.writer.XmlItemWriter;
import lombok.AllArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.parameters.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.StepContribution;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.infrastructure.item.adapter.ItemReaderAdapter;
import org.springframework.batch.infrastructure.item.database.JdbcCursorItemReader;
import org.springframework.batch.infrastructure.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.batch.infrastructure.item.file.FlatFileFooterCallback;
import org.springframework.batch.infrastructure.item.file.FlatFileHeaderCallback;
import org.springframework.batch.infrastructure.item.file.FlatFileItemReader;
import org.springframework.batch.infrastructure.item.file.FlatFileItemWriter;
import org.springframework.batch.infrastructure.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.infrastructure.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.infrastructure.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.infrastructure.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.infrastructure.item.file.mapping.FieldSetMapper;
import org.springframework.batch.infrastructure.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.infrastructure.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.infrastructure.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.infrastructure.item.file.transform.LineTokenizer;
import org.springframework.batch.infrastructure.item.json.JacksonJsonObjectMarshaller;
import org.springframework.batch.infrastructure.item.json.JacksonJsonObjectReader;
import org.springframework.batch.infrastructure.item.json.JsonFileItemWriter;
import org.springframework.batch.infrastructure.item.json.JsonItemReader;
import org.springframework.batch.infrastructure.item.json.builder.JsonFileItemWriterBuilder;
import org.springframework.batch.infrastructure.item.json.builder.JsonItemReaderBuilder;
import org.springframework.batch.infrastructure.item.xml.StaxEventItemReader;
import org.springframework.batch.infrastructure.item.xml.StaxEventItemWriter;
import org.springframework.batch.infrastructure.item.xml.builder.StaxEventItemReaderBuilder;
import org.springframework.batch.infrastructure.item.xml.builder.StaxEventItemWriterBuilder;
import org.springframework.batch.infrastructure.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.util.Date;

@Configuration

public class SampleJob {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private PlatformTransactionManager transactionManager;
    @Autowired
    private SecondTasklet secondTasklet;

    @Autowired
    private FirstJobListener firstJobListener;

    @Autowired
    private FirstStepListener firstStepListener;

    @Autowired
    private FirstItemReader firstItemReader;

    @Autowired
    private FirstItemProcessor firstItemProcessor;

    @Autowired
    private StudentProcessor studentProcessor;

    @Autowired
    private FirstItemWriter firstItemWriter;

    @Autowired
    private JsonItemWriter jsonItemWriter;

    @Autowired
    private XmlItemWriter xmlItemWriter;

    @Autowired
    private JdbcItemWriter jdbcItemWriter;

    @Autowired
    @Qualifier(value = "uniDatasource")
    private DataSource uniDatasource;

    @Autowired
    @Qualifier(value = "datasource")
    private DataSource dataSource;

    @Autowired
    private StudentService studentService;


    //By commenting the @Bean annotation, the job does not run
    @Bean
    public Job firstJob() {
        return new JobBuilder("firstJob", jobRepository)
                //Comment this part so that when calling the GET method from the controller,
                //the params set there are not overridden
//                .incrementer(new RunIdIncrementer())
                .start(firstStep())
                .next(secondStep())
                .listener(firstJobListener)
                .build();
    }

    @Bean
    public Step firstStep() {
        return new StepBuilder("firstStep", jobRepository)
                .tasklet(firstTask(), transactionManager)
                .listener(firstStepListener)
                .build();
    }

    @Bean
    public Step secondStep() {
        return new StepBuilder("secondStep", jobRepository)
                .tasklet(secondTasklet, transactionManager)
                .build();
    }

    private Tasklet firstTask() {
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
                //Comment this part so that when calling the GET method from the controller,
                //the params set there are not overridden
//                .incrementer(new RunIdIncrementer())
                .start(firstChunkStep())
                .next(secondStep())
                .build();
    }

    @Bean
    public Job chunkJob() {
        return new JobBuilder("chunkJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(firstChunkStep())
                .build();
    }

    private Step firstChunkStep() {
        return new StepBuilder("First Chunk Step", jobRepository)
                .<StudentJdbc, StudentJdbc>chunk(3)
                //ItemReader MUST always be provided in Chunk oriented steps
                //.reader(flatFileItemReader())
                //.reader(xmlItemReader())
                .reader(jdbcItemReader())
                //.reader(itemReaderAdapter())
                //the processor is necessary when the reader output and the writer input do not match
                //otherwise it is optional
                //.processor(firstItemProcessor)
                //.processor(studentProcessor)
                //ItemWriter MUST always be provided in Chunk oriented steps
                //.writer(flatFileItemWriter())
                //.writer(jsonFileItemWriter())
                //.writer(firstItemWriter)
                .writer(staxEventItemWriter())
                .build();
    }

    @Bean
    public FlatFileItemReader<StudentCsv> flatFileItemReader() {
        return new FlatFileItemReaderBuilder<StudentCsv>()
                .name("studentCsvReader")
                .resource(new FileSystemResource(
                        "C:\\dev\\spring-batch-practice\\inputFiles\\students.csv"
                ))
                .linesToSkip(1)
                .delimited()
                .names("id", "firstName", "lastName", "email")
                .fieldSetMapper(new BeanWrapperFieldSetMapper<>() {{
                    setTargetType(StudentCsv.class);
                }})
                .build();
    }

    @StepScope
    @Bean
    public JsonItemReader<StudentJson> jsonItemReader() {
        return new JsonItemReaderBuilder<StudentJson>()
                .name("StudentJsonReader")
                .resource(new FileSystemResource(
                        "C:\\dev\\spring-batch-practice\\inputFiles\\students.json"
                ))
                .jsonObjectReader(new JacksonJsonObjectReader<>(StudentJson.class))
                //This configures the maximum amount of items you want to be read by the reader (default is max)
                .maxItemCount(8)
                //This skips the first two
                .currentItemCount(2)
                .build();
    }

    public StaxEventItemReader<StudentXml> xmlItemReader(){
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setClassesToBeBound(StudentXml.class);

        return new StaxEventItemReaderBuilder<StudentXml>()
                .name("StudentXmlReader")
                .resource(new FileSystemResource(
                        "C:\\dev\\spring-batch-practice\\inputFiles\\students.xml"
                ))
                .addFragmentRootElements("student")
                .unmarshaller(marshaller)
                .build();
    }

    public JdbcCursorItemReader<StudentJdbc> jdbcItemReader(){
        return new JdbcCursorItemReaderBuilder<StudentJdbc>()
                .name("StudentJdbcReader")
                .dataSource(uniDatasource)
                .sql("""
                        SELECT id, first_name as firstName, last_name as lastName, email
                        FROM student
                        """)
                .rowMapper(new BeanPropertyRowMapper<>(StudentJdbc.class))
                .build();
    }

    public ItemReaderAdapter<StudentJson> itemReaderAdapter(){
        ItemReaderAdapter<StudentJson> itemReaderAdapter = new ItemReaderAdapter<>();
        itemReaderAdapter.setTargetObject(studentService);
        itemReaderAdapter.setTargetMethod("getStudent");
        itemReaderAdapter.setArguments(new Object[] {1L, "Test"});
        return itemReaderAdapter;
    }

    @StepScope
    @Bean
    public FlatFileItemWriter<StudentJdbc> flatFileItemWriter(
    ){
        FlatFileHeaderCallback headerCallback = new FlatFileHeaderCallback() {
            @Override
            public void writeHeader(Writer writer) throws IOException {
                writer.write("Id, First Name, Last Name, Email");
            }
        };

        BeanWrapperFieldExtractor<StudentJdbc> fieldExtractor = new BeanWrapperFieldExtractor<>();
        fieldExtractor.setNames(new String[] {"id", "firstName", "lastName", "email"});

        DelimitedLineAggregator<StudentJdbc> lineAggregator = new DelimitedLineAggregator<>();
        lineAggregator.setFieldExtractor(fieldExtractor);

        FlatFileFooterCallback footerCallback = writer -> writer.write("Created @ " + new Date());

        return new FlatFileItemWriterBuilder<StudentJdbc>()
                .name("CsvFlatFileItemWriter")
                .resource(new FileSystemResource(
                        "C:\\dev\\spring-batch-practice\\outputFiles\\students.csv"
                ))
                .headerCallback(headerCallback)
                .lineAggregator(lineAggregator)
                .footerCallback(footerCallback)
                .build();
    }

    public JsonFileItemWriter<StudentJson> jsonFileItemWriter(){

        JacksonJsonObjectMarshaller<StudentJson> marshaller = new JacksonJsonObjectMarshaller<>();

        return new JsonFileItemWriterBuilder<StudentJson>()
                .name("jsonFileItemWriter")
                .resource(new FileSystemResource(
                        "C:\\dev\\spring-batch-practice\\outputFiles\\students.json"
                ))
                .jsonObjectMarshaller(marshaller)
                .build();
    }

    public StaxEventItemWriter<StudentJdbc> staxEventItemWriter(){

        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setClassesToBeBound(StudentJdbc.class);

        return new StaxEventItemWriterBuilder<StudentJdbc>()
                .name("XmlItemWriter")
                .resource(new FileSystemResource(
                        "C:\\dev\\spring-batch-practice\\outputFiles\\students.xml"
                ))
                .rootTagName("student")
                .marshaller(marshaller)
                .build();
    }
}
