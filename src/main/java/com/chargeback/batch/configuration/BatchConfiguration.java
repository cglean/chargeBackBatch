package com.chargeback.batch.configuration;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

	@Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;
    
    
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
    public PollingItemProcessor processor() {
      //  return new OrderItemProcessor();
    }
    
    @Bean
    public ItemWriter<SvcReq> writer() {
        return new OrderSvcInvoker();
    }

   @Bean
    public JobExecutionListener listener() {
        return new JobCompletionNotificationListener();
    }
}
