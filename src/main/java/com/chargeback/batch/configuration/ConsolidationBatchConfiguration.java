package com.chargeback.batch.configuration;

import java.util.Date;
import java.util.List;

import javax.inject.Provider;

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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import com.chargeback.batch.processor.ConsolidationBatchProcessor;
import com.chargeback.batch.processor.ConsolidationBatchReader;
import com.chargeback.batch.processor.ConsolidationBatchWriter;
import com.chargeback.batch.processor.NotificationListener;
import com.chargeback.batch.rest.client.ChargeBackApiClient;
import com.chargeback.batch.vo.ChargeBackUsage;
import com.chargeback.batch.vo.ChargeBackUsageSummary;

@Configuration
@EnableBatchProcessing
public class ConsolidationBatchConfiguration {
	@Autowired
    private SimpleJobLauncher jobLauncher;
	
	@Autowired
    public JobBuilderFactory jobBuilderFactory;

	private static final String DUMMY_EXPRESSION = null;
	
    @Autowired
    public StepBuilderFactory stepBuilderFactory;
    
    @Autowired 
    Provider<ChargeBackApiClient> clientFactory; 
    
    @Scheduled(cron = "0 0 01 * * *")
    public void runConsolidation() throws Exception {

        System.out.println("Job Started at :" + new Date());
    	 final List<String> orgList = clientFactory.get().getOrgList();
    	 for(final String orgName : orgList){
    		 JobParameters param = new JobParametersBuilder().addString("JobID",
    	                String.valueOf(System.currentTimeMillis())).addString("orgName", orgName).toJobParameters();
    	        JobExecution execution = jobLauncher.run(processConsolidationJob(), param);
    	        System.out.println("Job finished with status :" + execution.getStatus());
    	 }
    	 
        
       
    }

    @Bean
    public Job processConsolidationJob() {
        return jobBuilderFactory.get("processConsolidationJob")
                .incrementer(new RunIdIncrementer())
                .listener(listener())
                .flow(consolidationStep())
                .end()
                .build();
    }

    @Bean
    public Step consolidationStep() {
        return stepBuilderFactory.get("consolidationStep")
                .<List<ChargeBackUsage>, ChargeBackUsageSummary> chunk(1)
                .reader(consolidationReader(DUMMY_EXPRESSION))
                .processor(consolidationProcessor())
                .writer(consolidationWriter())
                .build();
    }

    @Bean
    @StepScope
    public ConsolidationBatchReader consolidationReader(@Value("#{jobParameters['orgName']}") final String orgName) {
     return new ConsolidationBatchReader(orgName);
    }
    
    @Bean
    public ConsolidationBatchWriter consolidationWriter() {
        return new ConsolidationBatchWriter();
    }

    
    @Bean
    @StepScope
    public ConsolidationBatchProcessor consolidationProcessor() {
        return new ConsolidationBatchProcessor();
    }
   @Bean
    public JobExecutionListener listener() {
        return new NotificationListener();
    }
}
