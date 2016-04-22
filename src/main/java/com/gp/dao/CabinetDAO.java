package com.gp.dao;

import java.util.List;

import com.gp.info.CabinetInfo;
import com.gp.info.InfoId;

public interface CabinetDAO extends BaseDAO<CabinetInfo>{
	/**
	 * Update the cabinet capacity
	 **/
	public int updateCabCapacity(InfoId<Long> cabinet, Long capacity);
	
	/**
	 * Update the cabinet storage 
	 **/
	public int changeStorage(InfoId<Long> cabinet, InfoId<Integer> storageId);
	
	/**
	 * Get cabinets by workgroupId 
	 **/
	public List<CabinetInfo> queryByWorkgroupId(InfoId<Long> wgroupkey);
}
