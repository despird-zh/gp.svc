/*******************************************************************************
 * Copyright 2016 Gary Diao - gary.diao@outlook.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.gp.common;

import com.gp.info.FlatColLocator;

public enum Measures implements FlatColLocator {
	
	WG_MEAS_FILE(1),
	WG_MEAS_POST(2),
	WG_MEAS_MEMBER(3),
	WG_MEAS_EXT_MBR(4),
	WG_MEAS_SUB_GRP(5),
	NODE_MEAS_MEMBER(1),
	NODE_MEAS_GROUP(2),
	NODE_MEAS_TOPIC(3),
	NODE_MEAS_FILE(4),
	NODE_MEAS_POINT(5),
	NODE_MEAS_EXPERT(6),
	;
	// measure type of work group summary
	public static String MEAS_TYPE_WG_SUM = "wg_summary";
	public static String MEAS_TYPE_NODE_SUM = "node_summary";
	
	private static String colPrefix = "measure_data_";
	
	private int colIndex;
	
	private Measures(int colIndex){
		this.colIndex = colIndex;
	}
	
	public Integer getColIndex() {
		
		return colIndex;
	}

	public String getColPrefix() {
		
		return colPrefix;
	}

	@Override
	public String getColumn() {
		
		return colPrefix + colIndex;
	}
	
}
