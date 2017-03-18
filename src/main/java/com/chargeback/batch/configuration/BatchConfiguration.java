package com.chargeback.batch.configuration;

import java.util.Date;
import java.util.List;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import com.chargeback.batch.processor.ConsolidationBatchProcessor;
import com.chargeback.batch.processor.ConsolidationBatchReader;
import com.chargeback.batch.processor.ConsolidationBatchWriter;
import com.chargeback.batch.processor.NotificationListener;
import com.chargeback.batch.processor.PollingJobReader;
import com.chargeback.batch.processor.PollingJobWriter;
import com.chargeback.batch.rest.client.ChargeBackApiClient;
import com.chargeback.batch.vo.ChargeBackUsage;
import com.chargeback.batch.vo.ChargeBackUsageSummary;

@Configuration
public class BatchConfiguration {

	@Autowired
	public JobBuilderFactory jobBuilderFactory;

	@Autowired
	public StepBuilderFactory stepBuilderFactory;

	@Autowired
	ObjectFactory<ChargeBackApiClient> clientFactory;

	@Autowired
	private JobLauncher jobLauncher;
	

	@Scheduled(fixedRate=50000)
	public void perform() throws Exception {

		System.out.println("Job Started at :" + new Date());
		JobParameters param = new JobParametersBuilder().addString("JobID", String.valueOf(System.currentTimeMillis()))
				.toJobParameters();
		JobExecution execution = jobLauncher.run(processPollingJob(), param);
		System.out.println("Job finished with status :" + execution.getStatus());
	}

	@Bean
	public Job processPollingJob() {
		return jobBuilderFactory.get("processPollingJob").incrementer(new RunIdIncrementer()).listener(listener())
				.flow(pollStep()).end().build();
	}

	@Bean
	public Step pollStep() {
		return stepBuilderFactory.get("processStep").<ChargeBackUsage, ChargeBackUsage>chunk(1).reader(reader())
				.writer(writer()).build();
	}

	@Bean
	@StepScope
	public PollingJobReader reader() {
		ChargeBackApiClient chargeBackApiClient = clientFactory.getObject();
		return new PollingJobReader(chargeBackApiClient);
	}

	@Bean
	public PollingJobWriter writer() {
		return new PollingJobWriter();
	}

	@Bean
	public JobExecutionListener listener() {
		return new NotificationListener();
	}

	@Scheduled(fixedRate=100000)
	public void runConsolidation() throws Exception {

		System.out.println("Job Started at :" + new Date());
		final List<String> orgList = clientFactory.getObject().getOrgList();
		for (final String orgName : orgList) {
			JobParameters param = new JobParametersBuilder()
					.addString("JobID", String.valueOf(System.currentTimeMillis())).addString("orgName", orgName)
					.toJobParameters();

			JobExecution execution = jobLauncher.run(processConsolidationJob(), param);
			System.out.println("Job finished with status :" + execution.getStatus());
		}

	}

	@Bean
	public Job processConsolidationJob() {
		return jobBuilderFactory.get("processConsolidationJob").incrementer(new RunIdIncrementer()).listener(listener())
				.flow(consolidationStep()).end().build();
	}

	@Bean
	public Step consolidationStep() {
		return stepBuilderFactory.get("consolidationStep").<List<ChargeBackUsage>, ChargeBackUsageSummary>chunk(1)
				.reader(consolidationReader(DUMMY_EXPRESSION)).processor(consolidationProcessor())
				.writer(consolidationWriter()).build();
	}

	@Bean
	@StepScope
	public ConsolidationBatchReader consolidationReader(@Value("#{jobParameters['orgName']}") final String orgName) {
		ChargeBackApiClient chargeBackApiClient = clientFactory.getObject();

		return new ConsolidationBatchReader(orgName, chargeBackApiClient);
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

	private static final String DUMMY_EXPRESSION = null;

}
