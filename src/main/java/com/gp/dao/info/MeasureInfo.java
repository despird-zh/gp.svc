package com.gp.dao.info;

import com.gp.info.FlatColLocator;
import com.gp.info.TraceableInfo;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MeasureInfo extends TraceableInfo<Long> {

	private static final long serialVersionUID = -2120667693331640563L;

	private Date measureTime;
	
	private String measureType;
	
	private Long traceSourceId;
	
	private Map<FlatColLocator, String> flatColMap = new HashMap<FlatColLocator, String>();

	public Date getMeasureTime() {
		return measureTime;
	}

	public void setMeasureTime(Date measureTime) {
		this.measureTime = measureTime;
	}

	public String getMeasureType() {
		return measureType;
	}

	public void setMeasureType(String measureType) {
		this.measureType = measureType;
	}

	public Long getTraceSourceId() {
		return traceSourceId;
	}

	public void setTraceSourceId(Long traceSourceId) {
		this.traceSourceId = traceSourceId;
	}

	public Map<FlatColLocator, String> getFlatColMap() {
		return flatColMap;
	}

	public void setFlatColMap(Map<FlatColLocator, String> flatColMap) {
		this.flatColMap = flatColMap;
	}
	
	public String getColValue(FlatColLocator col){
		return flatColMap.get(col);
	}
	
	public void putColValue(FlatColLocator col, String value){
		flatColMap.put(col, value);
	}
}
