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

import java.io.Serializable;

/** 
 * Wrap the user permission on page.
 * 
 * @author gary diao 
 * @version 0.1 2015-10-11
 **/
public class ActionPerm implements Serializable{

	private static final long serialVersionUID = -7722023132744828488L;

	private String pageAbbr;
	
	private String actionAbbr;
	
	private Boolean enable = true;

	public ActionPerm(String pageAbbr, String actionAbbr){
		this.pageAbbr = pageAbbr;
		this.actionAbbr = actionAbbr;
	}
	
	public String getPageAbbr() {
		return pageAbbr;
	}

	public void setPageAbbr(String pageAbbr) {
		this.pageAbbr = pageAbbr;
	}

	public String getActionAbbr() {
		return actionAbbr;
	}

	public void setActionAbbr(String actionAbbr) {
		this.actionAbbr = actionAbbr;
	}

	public Boolean getEnable() {
		return enable;
	}

	public void setEnable(Boolean enable) {
		this.enable = enable;
	}
	
}
