package com.gp.dao;

import java.util.Date;
import java.util.List;

import com.gp.info.FlatColLocator;
import com.gp.info.InfoId;
import com.gp.info.MeasureInfo;

public interface MeasureDAO extends BaseDAO<MeasureInfo>{
	
	/**
	 * Query the measure information by trace id and measure type 
	 **/
	public MeasureInfo queryObjectByTraceId(InfoId<?> traceid,String measureType, FlatColLocator ... columns);
	
	/**
	 * Query the measure information list by trace id and measure type 
	 **/
	public List<MeasureInfo> queryListByTraceId(InfoId<?> traceid,String measureType, FlatColLocator ... columns);
	
	/**
	 * Query the measure information list by trace id and measure type 
	 **/
	public List<MeasureInfo> queryListBefore(InfoId<?> traceid,String measureType,Date before, FlatColLocator ... columns);
	
	/**
	 * Query the measure information list by trace id and measure type 
	 **/
	public List<MeasureInfo> queryListAfter(InfoId<?> traceid,String measureType,Date after, FlatColLocator ... columns);
	
	/**
	 * Query the measure information list by trace id and measure type 
	 **/
	public List<MeasureInfo> queryListRange(InfoId<?> traceid,String measureType,Date before, Date after, FlatColLocator ... columns);
	
	
}
