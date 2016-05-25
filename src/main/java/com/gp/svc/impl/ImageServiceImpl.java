package com.gp.svc.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gp.common.IdKey;
import com.gp.common.ServiceContext;
import com.gp.config.ServiceConfigurator;
import com.gp.dao.ImageDAO;
import com.gp.dao.PseudoDAO;
import com.gp.exception.ServiceException;
import com.gp.info.ImageInfo;
import com.gp.info.InfoId;
import com.gp.svc.ImageService;

@Service("imageService")
public class ImageServiceImpl implements ImageService{

	Logger LOGGER = LoggerFactory.getLogger(ImageServiceImpl.class);
	
	@Autowired
	PseudoDAO pseudodao;
	
	@Autowired
	ImageDAO imagedao;
	
	@Transactional(value = ServiceConfigurator.TRNS_MGR, readOnly = true)
	@Override
	public ImageInfo getImage(ServiceContext<?> svcctx, InfoId<Long> id) throws ServiceException {

		try{
			return imagedao.query(id);
		}catch(DataAccessException dae){
			
			throw new ServiceException("Fail query ",dae);
		}
	}

	@Transactional(value = ServiceConfigurator.TRNS_MGR, readOnly = true)
	@Override
	public List<ImageInfo> getImages(ServiceContext<?> svcctx, String format) throws ServiceException {
		
		List<ImageInfo> result = null;
		String QUERY_SQL = "SELECT * FROM gp_images WHERE image_format LIKE ? ";
		
		JdbcTemplate jtemplate = pseudodao.getJdbcTemplate(JdbcTemplate.class);
		Object[] params = new Object[]{
				format+"%"
		};
		try{
			
			if(LOGGER.isDebugEnabled()){
				LOGGER.debug("SQL : {} / PARAMS : {}", QUERY_SQL, Arrays.toString(params));
			}
			result = jtemplate.query(QUERY_SQL, params, new RowMapper<ImageInfo>(){

				@Override
				public ImageInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
					ImageInfo info = new ImageInfo("");
					InfoId<Long> id = IdKey.IMAGE.getInfoId(rs.getLong("image_id"));
					info.setInfoId(id);
					info.setTouchTime(rs.getTimestamp("touch_time"));
					info.setImageName(rs.getString("image_name"));
					info.setFormat(rs.getString("image_format"));
					info.setExtension(rs.getString("image_ext"));
					
					info.setModifier(rs.getString("modifier"));
					info.setModifyDate(rs.getTimestamp("last_modified"));
					
					return info;
				}});
			
		}catch(DataAccessException dae){
			
			throw new ServiceException("Fail query ",dae);
		}
		
		return result;
	}

	@Transactional(value = ServiceConfigurator.TRNS_MGR, readOnly = true)
	@Override
	public ImageInfo getImage(ServiceContext<?> svcctx, InfoId<Long> id, String parentPath)
			throws ServiceException {
				
		try{
			
			return imagedao.query(id, parentPath);
		}catch(DataAccessException dae){
			
			throw new ServiceException("Fail query ",dae);
		}
	}

	@Transactional(value = ServiceConfigurator.TRNS_MGR)
	@Override
	public boolean newImage(ServiceContext<?> svcctx, ImageInfo info) throws ServiceException {
		try{
			
			svcctx.setTraceInfo(info);
			return imagedao.create(info) > 0;
		}catch(DataAccessException dae){
			
			throw new ServiceException("Fail create ",dae);
		}
	}

	@Transactional(value = ServiceConfigurator.TRNS_MGR)
	@Override
	public boolean updateImage(ServiceContext<?> svcctx, ImageInfo info) throws ServiceException {
		try{
			svcctx.setTraceInfo(info);
			int cnt = imagedao.update(info);
			return cnt > 0;
			
		}catch(DataAccessException dae){
			
			throw new ServiceException("Fail update ",dae);
		}
	}

	@Transactional(value = ServiceConfigurator.TRNS_MGR)
	@Override
	public boolean removeImage(ServiceContext<?> svcctx, InfoId<Long> id) throws ServiceException {
		try{
			
			return imagedao.delete(id) > 0;
		}catch(DataAccessException dae){
			
			throw new ServiceException("Fail delete ",dae);
		}
	}

	@Transactional(value = ServiceConfigurator.TRNS_MGR, readOnly = true)
	@Override
	public String getImageFileName(ServiceContext<?> svcctx, InfoId<Long> id) throws ServiceException {
		try{
			// force not to retrieve the binary data : parent = ""
			ImageInfo info = imagedao.query(id, "");
			if(null == info)
				return null;
			
			return info.getFileName();
		}catch(DataAccessException dae){
			
			throw new ServiceException("Fail query ",dae);
		}
	}

}
