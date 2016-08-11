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

import com.chargeback.batch.vo.ChargeBackRequest;

public class PollingJobReader implements ItemReader<List<ChargeBackRequest>> {

	@Override
	public List<ChargeBackRequest> read()
			throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
		
		final String METRICS_URL = "http://metricsfetchdemo-unflaming-overcensoriousness.cfapps.io/metrics/getmetrics";
		RestTemplate restTemplate = new RestTemplate();
		final ResponseEntity<List<ChargeBackRequest>> response = restTemplate.exchange(METRICS_URL, HttpMethod.GET, HttpEntity.EMPTY,
				new ParameterizedTypeReference<List<ChargeBackRequest>>() {
				});
		return response.getBody();
	}

	
	
}
