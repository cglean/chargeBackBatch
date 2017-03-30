package com.chargeback.batch.rest.client;

import java.util.List;
import java.util.Map;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.chargeback.batch.vo.ChargeBackUsage;
import com.chargeback.batch.vo.ChargeBackUsageSummary;

@FeignClient("CHARGEBACK-API")
public interface ChargeBackApiClient {

	@RequestMapping(value = "/metrics/getInstanceMetrics", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<ChargeBackUsage> getAllApplicationInstanceData();

	@RequestMapping(value="/metrics/getHistorical/{fromDate}/{toDate}/{orgName}",method=RequestMethod.GET , produces=MediaType.APPLICATION_JSON_VALUE)
	public Map<String, List<ChargeBackUsage>> getHistoricalData(@PathVariable("fromDate") final String fromDate, @PathVariable("toDate") final String toDate, @PathVariable("orgName") final String orgName);
	
	
	@RequestMapping(value="/metrics/submit",method=RequestMethod.POST , consumes=MediaType.APPLICATION_JSON_VALUE)
	public void submitData(@RequestBody List<? extends ChargeBackUsage> usageList);	
	
	@RequestMapping(value = "/metrics/getOrgList", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<String> getOrgList();
	
	@RequestMapping(value = "/metrics/submit/summary", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
	public List<String> saveConsolidatedData(@RequestBody List<? extends ChargeBackUsageSummary> chargeBackUsageSummaryList);

}
