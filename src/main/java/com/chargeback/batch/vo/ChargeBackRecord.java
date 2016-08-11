package com.chargeback.batch.vo;


public class ChargeBackRecord {

	
	private String diskQuota;

	private String name;
	
	private ChargeBackUsage usage;
	
	private String memQuota;
	

		public String getMemQuota() {
		return memQuota;
	}

	public void setMemQuota(String memQuota) {
		this.memQuota = memQuota;
	}

		public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDiskQuota() {
		return diskQuota;
	}

	public void setDiskQuota(String diskQuota) {
		this.diskQuota = diskQuota;
	}

	public ChargeBackUsage getUsage() {
		return usage;
	}

	public void setUsage(ChargeBackUsage usage) {
		this.usage = usage;
	}
}
