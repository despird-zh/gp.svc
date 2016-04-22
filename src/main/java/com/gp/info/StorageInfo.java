package com.gp.info;

import javax.validation.constraints.Min;

import org.hibernate.validator.constraints.NotBlank;

public class StorageInfo extends TraceableInfo<Integer>{

	private static final long serialVersionUID = -3497001738457833757L;
	@NotBlank
	private String storageName;
	@Min(0)
	private Long capacity ;
	
	private Long used;
	@NotBlank
	private String settingJson;
	@NotBlank
	private String storageType;
	@NotBlank
	private String state;
	
	private String description;
	
	public String getStorageName() {
		return storageName;
	}

	public void setStorageName(String storageName) {
		this.storageName = storageName;
	}

	public Long getCapacity() {
		return capacity;
	}

	public void setCapacity(Long capacity) {
		this.capacity = capacity;
	}

	public Long getUsed() {
		return used;
	}

	public void setUsed(Long used) {
		this.used = used;
	}

	public String getSettingJson() {
		return settingJson;
	}

	public void setSettingJson(String settingJson) {
		this.settingJson = settingJson;
	}

	public String getStorageType() {
		return storageType;
	}

	public void setStorageType(String storageType) {
		this.storageType = storageType;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
}
