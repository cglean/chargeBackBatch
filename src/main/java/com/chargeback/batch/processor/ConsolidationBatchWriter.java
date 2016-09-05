package com.chargeback.batch.processor;

import java.util.List;

import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import com.chargeback.batch.rest.client.ChargeBackApiClient;
import com.chargeback.batch.vo.ChargeBackUsageSummary;

public class ConsolidationBatchWriter implements ItemWriter<ChargeBackUsageSummary>{

	@Autowired ChargeBackApiClient chargeBackApiClient;

	@Override
	public void write(List<? extends ChargeBackUsageSummary> chargeBackUsageSummaryList) throws Exception {
		chargeBackApiClient.saveConsolidatedData(chargeBackUsageSummaryList);
	}
	
}
