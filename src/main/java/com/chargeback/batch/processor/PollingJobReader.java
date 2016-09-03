package com.chargeback.batch.processor;

import java.util.List;

import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.chargeback.batch.vo.ChargeBackUsage;


public class PollingJobReader implements ItemReader<ChargeBackUsage> {

	private List<ChargeBackUsage> chargeBackUsageList;
    private int nextUsageIndex;
	
	public PollingJobReader() {
		super();
		
		final String METRICS_URL = "http://localhost:8081/metrics/getInstanceMetrics";
		RestTemplate restTemplate = new RestTemplate();
		final ResponseEntity<List<ChargeBackUsage>> response = restTemplate.exchange(METRICS_URL, HttpMethod.GET, HttpEntity.EMPTY,
				new ParameterizedTypeReference<List<ChargeBackUsage>>() {
				});
		
		chargeBackUsageList= response.getBody();
		nextUsageIndex=0;
	}

	@Override
	public ChargeBackUsage read()
			throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
		ChargeBackUsage chargeBackUsage = null;
		
		if (nextUsageIndex < chargeBackUsageList.size()) {
			chargeBackUsage = chargeBackUsageList.get(nextUsageIndex);
			nextUsageIndex++;
        }
		return chargeBackUsage;
 
	}

	
	
}
