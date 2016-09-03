package com.chargeback.batch.processor;

import java.util.List;

import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import com.chargeback.batch.rest.client.ChargeBackApiClient;
import com.chargeback.batch.vo.ChargeBackUsage;

public class PollingJobWriter implements ItemWriter<ChargeBackUsage> {
	
	@Autowired ChargeBackApiClient chargeBackApiClient;

	@Override
	public void write(List<? extends ChargeBackUsage> usageReq) throws Exception {
		chargeBackApiClient.submitData(usageReq);
	}

}
