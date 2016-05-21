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
