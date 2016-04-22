package com.gp.dao;

import com.gp.info.ImageInfo;
import com.gp.info.InfoId;

public interface ImageDAO extends BaseDAO<ImageInfo>{

	public ImageInfo query(InfoId<Long> infoid, String parentPath);
	
}
