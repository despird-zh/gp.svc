package com.gp.common;

import com.gp.info.InfoId;

public class Cabinets {

	public static enum CabinetType{
		
		PUBLISH,
		NETDISK
	}
	
	public static enum FolderState{
		
		READY,
		COPY,
		MOVE,
		DELETE
	}
	
	public static enum FileState{
		
		READY,
		COPY,
		MOVE,
		DELETE
	}
	
	/**
	 * InfoId of certain cabinet root 
	 **/
	public static InfoId<Long> ROOT_FOLDER = IdKey.CAB_FOLDER.getInfoId(GeneralConstants.FOLDER_ROOT);
}
