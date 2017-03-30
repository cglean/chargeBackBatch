package com.chargeback.batch.processor;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.chargeback.batch.rest.client.ChargeBackApiClient;
import com.chargeback.batch.vo.ChargeBackUsage;

@StepScope
public class ConsolidationBatchReader implements ItemReader<List<ChargeBackUsage>> {


	
	List<List<ChargeBackUsage>> listOfUsageList = new ArrayList<>();
	int index;
	
	@Autowired
	public ConsolidationBatchReader(@Value("#{jobParameters['orgName']}") final String  orgName, ChargeBackApiClient chargeBackApiClient) {
		/*Consolidation would happen for Previous Day*/
		/*Consolidation Frequency is daily*/
		final Calendar prevDateCal = Calendar.getInstance();
		prevDateCal.roll(Calendar.DAY_OF_YEAR, -1);
		final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		final String fromDate = dateFormat.format(prevDateCal.getTime());
		final String toDate = dateFormat.format(prevDateCal.getTime());
		listOfUsageList = new ArrayList<>(chargeBackApiClient.getHistoricalData(fromDate, toDate, orgName).values());
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
