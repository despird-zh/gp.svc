package com.gp.dao;

import java.util.List;

import com.gp.info.FlatColLocator;
import com.gp.info.MeasureInfo;

public interface MeasureDAO {

	public MeasureInfo queryObjectByTraceId(Long traceid, FlatColLocator ... columns);
	
	public List<MeasureInfo> queryListByTraceId(Long traceid, FlatColLocator ... columns);
	
	
}
