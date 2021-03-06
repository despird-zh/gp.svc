package com.gp.svc;

import java.util.List;

import com.gp.common.ServiceContext;
import com.gp.exception.ServiceException;
import com.gp.dao.info.ImageInfo;
import com.gp.info.InfoId;

public interface ImageService {

	/**
	 * Get the image information from database 
	 **/
	public ImageInfo getImage(ServiceContext svcctx,InfoId<Long> id) throws ServiceException;

	public List<ImageInfo> getImages(ServiceContext svcctx, String format, String category) throws ServiceException;

	public ImageInfo getImage(ServiceContext svcctx,InfoId<Long> id, String parentPath) throws ServiceException;

	public boolean newImage(ServiceContext svcctx, ImageInfo info) throws ServiceException;

	public boolean updateImage(ServiceContext svcctx, ImageInfo info) throws ServiceException;

	public boolean removeImage(ServiceContext svcctx, InfoId<Long> id) throws ServiceException;

	public String getImageName(ServiceContext svcctx,InfoId<Long> id) throws ServiceException;
}
