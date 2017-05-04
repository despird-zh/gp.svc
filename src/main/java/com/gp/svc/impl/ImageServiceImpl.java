package com.gp.svc.impl;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gp.common.FlatColumns;
import com.gp.common.ServiceContext;
import com.gp.common.FlatColumns.FilterMode;
import com.gp.config.ServiceConfigurer;
import com.gp.dao.ImageDAO;
import com.gp.dao.PseudoDAO;
import com.gp.exception.ServiceException;
import com.gp.dao.info.ImageInfo;
import com.gp.info.InfoId;
import com.gp.svc.ImageService;

@Service
public class ImageServiceImpl implements ImageService{

	Logger LOGGER = LoggerFactory.getLogger(ImageServiceImpl.class);
	
	@Autowired
	PseudoDAO pseudodao;
	
	@Autowired
	ImageDAO imagedao;
	
	@Transactional(value = ServiceConfigurer.TRNS_MGR, readOnly = true)
	@Override
	public ImageInfo getImage(ServiceContext svcctx, InfoId<Long> id) throws ServiceException {

		try{
			return imagedao.query(id);
		}catch(DataAccessException dae){
			
			throw new ServiceException("excp.query.with",dae,"image", id);
		}
	}

	@Transactional(value = ServiceConfigurer.TRNS_MGR, readOnly = true)
	@Override
	public List<ImageInfo> getImages(ServiceContext svcctx, String format, String category) throws ServiceException {
		
		List<ImageInfo> result = null;
		String QUERY_SQL = "SELECT * FROM gp_images WHERE image_format LIKE ? and category LIKE ?";
		
		JdbcTemplate jtemplate = pseudodao.getJdbcTemplate(JdbcTemplate.class);
		Object[] params = new Object[]{
			format + "%", category + "%"
		};
		try{
			
			if(LOGGER.isDebugEnabled()){
				LOGGER.debug("SQL : {} / PARAMS : {}", QUERY_SQL, Arrays.toString(params));
			}
			result = jtemplate.query(QUERY_SQL, params, ImageDAO.ImageMapper);
			
		}catch(DataAccessException dae){
			
			throw new ServiceException("excp.query.with",dae,"Image","format = "+format);
		}
		
		return result;
	}

	@Transactional(value = ServiceConfigurer.TRNS_MGR, readOnly = true)
	@Override
	public ImageInfo getImage(ServiceContext svcctx, InfoId<Long> id, String parentPath)
			throws ServiceException {
				
		try{
			
			return imagedao.query(id, parentPath);
		}catch(DataAccessException dae){
			
			throw new ServiceException("excp.query.with", dae, "image", id);
		}
	}

	@Transactional(value = ServiceConfigurer.TRNS_MGR)
	@Override
	public boolean newImage(ServiceContext svcctx, ImageInfo info) throws ServiceException {
		try{
			
			info.setModifier(svcctx.getPrincipal().getAccount());
			return imagedao.create(info) > 0;
		}catch(DataAccessException dae){
			
			throw new ServiceException("excp.create",dae, "image");
		}
	}

	@Transactional(value = ServiceConfigurer.TRNS_MGR)
	@Override
	public boolean updateImage(ServiceContext svcctx, ImageInfo info) throws ServiceException {
		try{
			info.setModifier(svcctx.getPrincipal().getAccount());
			int cnt = imagedao.update(info,FilterMode.NONE);
			return cnt > 0;
			
		}catch(DataAccessException dae){
			
			throw new ServiceException("excp.update",dae, "image");
		}
	}

	@Transactional(value = ServiceConfigurer.TRNS_MGR)
	@Override
	public boolean removeImage(ServiceContext svcctx, InfoId<Long> id) throws ServiceException {
		try{
			
			return imagedao.delete(id) > 0;
		}catch(DataAccessException dae){
			
			throw new ServiceException("excp.delete.with", dae, "image", id);
		}
	}

	@Transactional(value = ServiceConfigurer.TRNS_MGR, readOnly = true)
	@Override
	public String getImageName(ServiceContext svcctx, InfoId<Long> id) throws ServiceException {
		try{
			// force not to retrieve the binary data : parent = ""
			String name = pseudodao.query(id, FlatColumns.IMG_NAME, String.class);
		
			return name;
		}catch(DataAccessException dae){
			
			throw new ServiceException("excp.query.with",dae, "image name", id);
		}
	}

}
