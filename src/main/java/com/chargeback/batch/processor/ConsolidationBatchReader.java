package com.chargeback.batch.processor;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.chargeback.batch.vo.ChargeBackUsage;

@StepScope
public class ConsolidationBatchReader implements ItemReader<List<ChargeBackUsage>> {


	
	List<List<ChargeBackUsage>> listOfUsageList = new ArrayList<>();
	int index;
	
	private String orgName;
	
	
	
	@Autowired
	public ConsolidationBatchReader(@Value("#{jobParameters['orgName']}") final String  orgName) {
		/*Consolidation would happen for Current Day*/
		/*Consolidation Frequency is daily*/
		this.orgName = orgName;
		final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		final String fromDate = dateFormat.format(new Date());
		final String toDate = dateFormat.format(new Date());
		/*This can be controlled in a fine grained manner more precisely*/
		final String HISTORICAL_URL = "http://localhost:8081/metrics/getHistorical/"+fromDate+"/"+toDate + "/" + this.orgName;
		final RestTemplate restTemplate = new RestTemplate();
		final ResponseEntity<Map<String, List<ChargeBackUsage>>> response = restTemplate.exchange(HISTORICAL_URL, HttpMethod.GET, HttpEntity.EMPTY,
				new ParameterizedTypeReference<Map<String, List<ChargeBackUsage>>>() {
				});
		
		listOfUsageList = new ArrayList<>(response.getBody().values());
		index = 0;
	}

	@Override
	public List<ChargeBackUsage> read()
			throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
		List<ChargeBackUsage> chargebackUsageList = null;
		if (index < listOfUsageList.size()) {
			chargebackUsageList = listOfUsageList.get(index);
			index++;
        }
		return chargebackUsageList;
	}
}
