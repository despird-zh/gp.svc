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
		if((privilege & BROWSE.value)>0)
			buffer.append(BROWSE).append(GeneralConstants.VALUES_SEPARATOR);
		else if((privilege & READ.value) > 0)
			buffer.append(READ).append(GeneralConstants.VALUES_SEPARATOR);
		else if((privilege & WRITE.value)>0)
			buffer.append(WRITE).append(GeneralConstants.VALUES_SEPARATOR);
		else if((privilege & DELETE.value)>0)
			buffer.append(DELETE).append(GeneralConstants.VALUES_SEPARATOR);
		else if((privilege & EXEC.value)>0)
			buffer.append(EXEC).append(GeneralConstants.VALUES_SEPARATOR);
		else
			buffer.append(GeneralConstants.VALUES_SEPARATOR);
		
		// need to remove the last VALUES_SEPARATOR
		return buffer.deleteCharAt(buffer.length() - 1).toString();
	}
}
