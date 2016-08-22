package com.chargeback.batch.processor;

import java.util.List;

import org.springframework.batch.item.ItemWriter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.chargeback.batch.vo.ChargeBackUsage;

public class PollingJobWriter implements ItemWriter<ChargeBackUsage> {
	
	private static final String  postURL =  "http://localhost:8081/metrics/submit";

	@Override
	public void write(List<? extends ChargeBackUsage> usageReq) throws Exception {
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<Boolean> respEntity = restTemplate.postForEntity(postURL, usageReq, Boolean.class);		
	}

}
