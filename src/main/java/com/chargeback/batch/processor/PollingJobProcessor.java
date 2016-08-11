package com.chargeback.batch.processor;

import java.util.List;

import org.springframework.batch.item.ItemProcessor;

import com.chargeback.batch.vo.ChargeBackRequest;
import com.chargeback.batch.vo.ChargeBackUsageReq;
import com.chargeback.batch.vo.UsageReq;

public class PollingJobProcessor implements ItemProcessor<List<ChargeBackRequest>, ChargeBackUsageReq>{

	@Override
	public ChargeBackUsageReq process(List<ChargeBackRequest> chargeBackRequests) throws Exception {
		chargeBackRequests.stream().map(chrgbckReq -> chrgbckReq.getRecords()).flatMap(records -> records.stream()).
		return null;
	}
	
	

}
