package com.chargeback.batch.vo;

import java.util.List;

public class ChargeBackRequest {
	private List<ChargeBackRecord> records;

	public List<ChargeBackRecord> getRecords() {
		return records;
	}

	public void setRecords(List<ChargeBackRecord> records) {
		this.records = records;
	}
}
