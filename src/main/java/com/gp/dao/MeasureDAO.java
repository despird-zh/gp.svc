package com.gp.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.gp.info.FlatColLocator;
import com.gp.info.InfoId;
import com.gp.dao.info.MeasureInfo;

public interface MeasureDAO extends BaseDAO<MeasureInfo>{
	
	/**
	 * Query the measure information by trace id and measure type 
	 **/
	public MeasureInfo queryLatest(InfoId<?> traceid,String measureType, FlatColLocator ... columns);
	
	/**
	 * Query the measure information list by trace id and measure type 
	 **/
	public List<MeasureInfo> query(InfoId<?> traceid,String measureType, FlatColLocator ... columns);
	
	/**
	 * Query the measure information list by trace id and measure type 
	 **/
	public List<MeasureInfo> queryBefore(InfoId<?> traceid,String measureType,Date before, FlatColLocator ... columns);
	
	/**
	 * Query the measure information list by trace id and measure type 
	 **/
	public List<MeasureInfo> queryAfter(InfoId<?> traceid,String measureType,Date after, FlatColLocator ... columns);
	
	/**
	 * Query the measure information list by trace id and measure type 
	 **/
	public List<MeasureInfo> queryRange(InfoId<?> traceid,String measureType,Date before, Date after, FlatColLocator ... columns);
	
	/**
	 * Update measure information on specified column
	 **/
	public int updateByTraceId(InfoId<?> traceid,String measureType, FlatColLocator flatcol, String value);
	
	/**
	 * Update measure information on specified column
	 **/
	public int updateByTraceId(InfoId<?> traceid,String measureType, Map<FlatColLocator, String> colmap);
	
}
