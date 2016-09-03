package com.chargeback.batch.processor;

import java.util.List;

import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.beans.factory.annotation.Autowired;

import com.chargeback.batch.rest.client.ChargeBackApiClient;
import com.chargeback.batch.vo.ChargeBackUsage;


public class PollingJobReader implements ItemReader<ChargeBackUsage> {

	private List<ChargeBackUsage> chargeBackUsageList;
    private int nextUsageIndex;
	
	 ChargeBackApiClient chargeBackApiClient;

	public PollingJobReader(ChargeBackApiClient chargeBackApiClient) {
		this.chargeBackApiClient=chargeBackApiClient;
		chargeBackUsageList= chargeBackApiClient.getAllApplicationInstanceData();
		nextUsageIndex=0;
	}

	@Override
	public ChargeBackUsage read()
			throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
		ChargeBackUsage chargeBackUsage = null;
		
		if (null!=chargeBackUsageList && nextUsageIndex < chargeBackUsageList.size()) {
			chargeBackUsage = chargeBackUsageList.get(nextUsageIndex);
			nextUsageIndex++;
        }
		return chargeBackUsage;
 
	}

	
	
}
