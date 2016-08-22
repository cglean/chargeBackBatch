package com.chargeback.batch.configuration;

import java.util.Date;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.Scheduled;

import com.chargeback.batch.processor.NotificationListener;
import com.chargeback.batch.processor.PollingJobReader;
import com.chargeback.batch.processor.PollingJobWriter;
import com.chargeback.batch.vo.ChargeBackUsage;

@Configuration
@EnableBatchProcessing
@Import({BatchScheduler.class})
public class BatchConfiguration {

	@Autowired
    private SimpleJobLauncher jobLauncher;
	
	@Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;
    
    
    
    @Scheduled(cron = "0 0/10 * * * ?")
    public void perform() throws Exception {

        System.out.println("Job Started at :" + new Date());

        JobParameters param = new JobParametersBuilder().addString("JobID",
                String.valueOf(System.currentTimeMillis())).toJobParameters();

        JobExecution execution = jobLauncher.run(processPollingJob(), param);

        System.out.println("Job finished with status :" + execution.getStatus());
    }
    
    
    
    @Bean
    public Job processPollingJob() {
        return jobBuilderFactory.get("processPollingJob")
                .incrementer(new RunIdIncrementer())
                .listener(listener())
                .flow(orderStep())
                .end()
                .build();
    }
    
   

    @Bean
    public Step orderStep() {
        return stepBuilderFactory.get("processStep")
                .<ChargeBackUsage, ChargeBackUsage> chunk(1)
                .reader(reader())
                .writer(writer())
                .build();
    }
    
 

    @Bean
    @StepScope
    public PollingJobReader reader() {
     return new PollingJobReader();
    }
    
    @Bean
    public PollingJobWriter writer() {
        return new PollingJobWriter();
    }

   @Bean
    public JobExecutionListener listener() {
        return new NotificationListener();
    }
}
