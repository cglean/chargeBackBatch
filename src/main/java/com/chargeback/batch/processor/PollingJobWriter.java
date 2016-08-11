package com.chargeback.batch.processor;

import java.util.List;

import org.springframework.batch.item.ItemWriter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.chargeback.batch.vo.ChargeBackUsageReq;

public class PollingJobWriter implements ItemWriter<ChargeBackUsageReq> {

	
	@Override
	public void write(List<? extends ChargeBackUsageReq> usage) throws Exception {

		RestTemplate restTemplate = new RestTemplate();
		for (ChargeBackUsageReq usageReq : usage) {
			ResponseEntity<Boolean> respEntity = restTemplate.postForEntity("url", usageReq, Boolean.class);
		}

	}

}
