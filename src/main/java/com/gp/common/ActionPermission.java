package com.gp.common;

import java.io.Serializable;

/** 
 * Wrap the user permission on page.
 * 
 * @author gary diao 
 * @version 0.1 2015-10-11
 **/
public class ActionPermission implements Serializable{

	private static final long serialVersionUID = -7722023132744828488L;

	private String pageAbbr;
	
	private String actionAbbr;
	
	private Boolean enable = true;

	public ActionPermission(String pageAbbr, String actionAbbr){
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
