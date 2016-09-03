package com.chargeback.batch.processor;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

import org.springframework.batch.item.ItemProcessor;

import com.chargeback.batch.vo.ChargeBackUsage;
import com.chargeback.batch.vo.ChargeBackUsageSummary;

public class ConsolidationBatchProcessor implements ItemProcessor<List<ChargeBackUsage> , ChargeBackUsageSummary>{

	@Override
	public ChargeBackUsageSummary process(final List<ChargeBackUsage> chargebackUsageList) throws Exception {
	
		final Function<List<ChargeBackUsage>, ChargeBackUsageSummary> consolidateUsage = new Function<List<ChargeBackUsage>, ChargeBackUsageSummary>() {
			
			@Override
			public ChargeBackUsageSummary apply(List<ChargeBackUsage> chargebackUsageList) {
			ChargeBackUsageSummary chargeBackUsageSummary = new ChargeBackUsageSummary();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			chargeBackUsageSummary.setCpu(chargebackUsageList.stream()
					.mapToDouble(chargeBackUsage -> Double.valueOf(chargeBackUsage.getCpu())).sum());
			chargeBackUsageSummary.setDisk(chargebackUsageList.stream().mapToLong(chargeBackUsage -> Long.valueOf(chargeBackUsage.getDisk())).sum());
			chargeBackUsageSummary.setMemory(chargebackUsageList.stream().mapToLong(chargeBackUsage -> Long.valueOf(chargeBackUsage.getMemory())).sum());
			chargeBackUsageSummary.setAppname(chargebackUsageList.get(0).getAppname());
			chargeBackUsageSummary.setOrgName(chargebackUsageList.get(0).getOrgName());
			chargeBackUsageSummary.setSpaceName(chargebackUsageList.get(0).getSpaceName());
			chargeBackUsageSummary.setInstanceIndex(Integer.valueOf(chargebackUsageList.get(0).getInstanceIndex()));
			chargeBackUsageSummary.setFromDate(dateFormat.format(new Date()));
			chargeBackUsageSummary.setToDate(dateFormat.format(new Date()));

				return chargeBackUsageSummary;
			}
		};
		
		return consolidateUsage.apply(chargebackUsageList);
	}
}
