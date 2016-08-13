package com.chargeback.batch.processor;

import java.util.List;

import org.springframework.batch.item.ItemWriter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.chargeback.batch.vo.ChargeBackUsage;

public class PollingJobWriter implements ItemWriter<List<ChargeBackUsage>> {
	
	private static final String  postURL =  "http://metricsfetchdemo-unflaming-overcensoriousness.cfapps.io/metrics/submit";

	@Override
	public void write(List<? extends List<ChargeBackUsage>> usageList) throws Exception {
		RestTemplate restTemplate = new RestTemplate();
		for (List<ChargeBackUsage> usageReq : usageList) {
			ResponseEntity<Boolean> respEntity = restTemplate.postForEntity(postURL, usageReq, Boolean.class);
		}

		
	}

	
}
