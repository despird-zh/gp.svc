package com.gp.quickflow;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

public class FlowLayout {

	private Table<Integer, Integer, FlowNode> flowGraph = HashBasedTable.create();
	
	public FlowLayout(){
		
	}
}
