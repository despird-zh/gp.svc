package com.gp.common;

import com.gp.info.FlatColLocator;

public enum Measures implements FlatColLocator {
	
	WORKGROUP_KPI1(1),
	WORKGROUP_KPI2(2),
	WORKGROUP_KPI3(3),
	WORKGROUP_KPI4(4)
	;

	private static String colPrefix = "measure_data_";
	
	private int colIndex;
	
	private Measures(int colIndex){
		this.colIndex = colIndex;
	}
	
	@Override
	public int getColIndex() {
		
		return colIndex;
	}

	@Override
	public String getColPrefix() {
		
		return colPrefix;
	}

	@Override
	public String getColumn() {
		
		return colPrefix + colIndex;
	}
	
}
