package com.gp.acl;

import com.gp.common.GeneralConstants;

public enum AcePrivilege {
	
	BROWSE(1),
	READ(2),
	WRITE(4),
	DELETE(8),
	EXEC(16);
	
	public final int value;
	
	private AcePrivilege(int value){
		
		this.value = value;
	}
	
	public static String toString(int privilege){
		StringBuffer buffer = new StringBuffer();
		if((privilege & READ.value) > 0)
			buffer.append(READ).append(GeneralConstants.VALUES_SEPARATOR);
		else if((privilege & WRITE.value)>0)
			buffer.append(WRITE).append(GeneralConstants.VALUES_SEPARATOR);
		else if((privilege & DELETE.value)>0)
			buffer.append(DELETE).append(GeneralConstants.VALUES_SEPARATOR);
		else if((privilege & EXEC.value)>0)
			buffer.append(EXEC).append(GeneralConstants.VALUES_SEPARATOR);
		else
			buffer.append(GeneralConstants.VALUES_SEPARATOR);
		
		return buffer.deleteCharAt(buffer.length()).toString();
	}
}
