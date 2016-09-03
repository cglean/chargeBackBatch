package com.chargeback.batch.processor;

import java.util.List;

import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.chargeback.batch.rest.client.ChargeBackApiClient;
import com.chargeback.batch.vo.ChargeBackUsageSummary;

public class ConsolidationBatchWriter implements ItemWriter<ChargeBackUsageSummary>{

	@Autowired ChargeBackApiClient chargeBackApiClient;

	private static final String  postURL =  "http://localhost:8081/metrics/submit/summary";
	@Override
	public void write(List<? extends ChargeBackUsageSummary> chargeBackUsageSummaryList) throws Exception {
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<Boolean> respEntity = restTemplate.postForEntity(postURL, chargeBackUsageSummaryList, Boolean.class);
	}
	
}
