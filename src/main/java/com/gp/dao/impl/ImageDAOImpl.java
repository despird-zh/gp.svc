package com.gp.dao.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;

import javax.sql.DataSource;

import com.gp.common.IdKey;
import com.gp.common.Images;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.AbstractLobCreatingPreparedStatementCallback;
import org.springframework.jdbc.core.support.AbstractLobStreamingResultSetExtractor;
import org.springframework.jdbc.support.lob.DefaultLobHandler;
import org.springframework.jdbc.support.lob.LobCreator;
import org.springframework.jdbc.support.lob.LobHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

import com.gp.common.FlatColumns;
import com.gp.common.FlatColumns.FilterMode;
import com.gp.config.ServiceConfigurer;
import com.gp.dao.ImageDAO;
import com.gp.info.FlatColLocator;
import com.gp.dao.info.ImageInfo;
import com.gp.info.InfoId;

@Component("imageDAO")
public class ImageDAOImpl extends DAOSupport implements ImageDAO{

	static Logger LOGGER = LoggerFactory.getLogger(ImageDAOImpl.class);
	
	@Autowired
	public ImageDAOImpl(@Qualifier(ServiceConfigurer.DATA_SRC)DataSource dataSource) {
		setDataSource(dataSource);
	}
	
	@Override
	public int create( final ImageInfo info) {
		
		final File binaryFile = info.getDataFile();
		String INS_SQL = "INSERT into gp_images (image_id,image_name, image_format,persist_type,category, image_link, modifier, last_modified) VALUES (?, ?, ?, ?,?, ?, ?, ?)";
		String UPD_SQL = "UPDATE gp_images SET image_data = ? WHERE image_id = ? ";
	
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate(JdbcTemplate.class);
		final LobHandler lobHandler = new DefaultLobHandler();

		InputStream is = null;
		Object[] params = new Object[]{
				info.getInfoId().getId(),
				info.getImageName(),
				info.getFormat(),
				info.getPersist(),
				info.getCategory(),
				info.getLink(),
				info.getModifier(),
				info.getModifyDate()
		};
		int cnt = 0;
		try {
			if(LOGGER.isDebugEnabled()){
				LOGGER.debug("SQL : {} / PARAMS : {}", INS_SQL, Arrays.toString(params));
			}
			cnt = jdbcTemplate.update(INS_SQL, params);
			
			if(cnt > 0 && binaryFile.exists()){
			is = new FileInputStream(binaryFile);
				final InputStream fis = is;
				if(LOGGER.isDebugEnabled()){
					LOGGER.debug("SQL : {} / PARAMS : {}", UPD_SQL, Arrays.toString(params));
				}
				jdbcTemplate.execute(UPD_SQL, new AbstractLobCreatingPreparedStatementCallback(lobHandler) {
						protected void setValues(PreparedStatement pstmt, LobCreator lobCreator) throws SQLException{
							lobCreator.setBlobAsBinaryStream(pstmt, 1, fis, (int) binaryFile.length());					
							pstmt.setLong(2, info.getInfoId().getId());
						}
					});
			}
		} catch (FileNotFoundException e) {
			// ignroe
			LOGGER.error("error - not found file", e);
		}finally{
			try {
				if(null != is)
					is.close();
			} catch (IOException e) {
				// ignore	
				LOGGER.error("error - io fail", e);
			}
		}
		
		return cnt;
	}

	@Override
	public int delete( InfoId<?> id) {
		StringBuffer SQL = new StringBuffer();
		SQL.append("delete from gp_images ")
			.append("where image_id = ?");
		
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		Object[] params = new Object[]{
			id.getId()
		};
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("SQL : {} / PARAMS : {}", SQL, Arrays.toString(params));
		}
		int rtv = jtemplate.update(SQL.toString(), params);
		return rtv;
	}

	@Override
	public int update(final ImageInfo info,FilterMode mode, FlatColLocator ...exclcols) {
		Set<String> colset = FlatColumns.toColumnSet(exclcols);
		List<Object> params = new ArrayList<Object>();
		final File binaryFile = info.getDataFile();
		StringBuffer SQL = new StringBuffer();
		SQL.append("UPDATE gp_images SET ");
		
		if(columnCheck(mode, colset, "image_name")){
			SQL.append("image_name = ?, ");
			params.add(info.getImageName());
		}
		
		if(columnCheck(mode, colset, "category")){
			SQL.append("category = ?, ");
			params.add(info.getCategory());
		}
		if(columnCheck(mode, colset, "persist_type")){
			SQL.append("persist_type = ?, ");
			params.add(info.getPersist());
		}
		
		if(null != binaryFile && binaryFile.exists()){
			if(columnCheck(mode, colset, "image_format")){
				SQL.append("image_format =? ,");
				params.add(info.getFormat());
			}
			if(columnCheck(mode, colset, "image_link")){
				SQL.append("image_link = ?, ");
				params.add(info.getLink());
			}
		}
		
		SQL.append("modifier = ?, last_modified = ? ")
			.append("WHERE image_id = ?");
		params.add(info.getModifier());
		params.add(info.getModifyDate());
		params.add(info.getInfoId().getId());
		
		String UPD_SQL = "UPDATE gp_images SET image_data = ? WHERE image_id = ? ";
		
		final LobHandler lobHandler = new DefaultLobHandler();
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		InputStream is = null;
		int cnt =0;
		try{
			if(LOGGER.isDebugEnabled()){
				LOGGER.debug("SQL : {} / PARAMS : {}", SQL, params);
			}
			cnt = jtemplate.update(SQL.toString(), params.toArray());
			if(cnt > 0 && null != binaryFile && binaryFile.exists()){
			is = new FileInputStream(binaryFile);
				final InputStream fis = is;
				if(LOGGER.isDebugEnabled()){
					LOGGER.debug("SQL : {} / PARAMS : {}", UPD_SQL, params);
				}
				jtemplate.execute(UPD_SQL, new AbstractLobCreatingPreparedStatementCallback(lobHandler) {
					protected void setValues(PreparedStatement pstmt, LobCreator lobCreator) throws SQLException{
						lobCreator.setBlobAsBinaryStream(pstmt, 1, fis, (int) binaryFile.length());						
						pstmt.setLong(2, info.getInfoId().getId());
					}
				});
			}
			return cnt;
		}catch (FileNotFoundException e) {
			// ignore
			e.printStackTrace();
		}finally{
			IOUtils.closeQuietly(is);
		}
		
		return 0;
	}

	@Override
	public ImageInfo query( final InfoId<?> id) {
		
		String SQL = "SELECT image_id, image_name, image_format, image_link, persist_type, category, modifier, last_modified FROM gp_images "
				+ "WHERE image_id = ? ";
		
		String SQL_FILE = "SELECT image_data FROM gp_images WHERE image_id = ? ";
		Object[] params = new Object[]{				
				id.getId()
			};

		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("SQL : {} / PARAMS : {}", SQL, Arrays.toString(params));
		}
		List<ImageInfo> list = jtemplate.query(SQL, params, ImageMapper);
		
		if(list != null && list.size()>0 ){
						
			final ImageInfo imginfo = list.get(0);
			File parent = new File(imginfo.getParentPath());
			if(parent.exists()){
				if(LOGGER.isDebugEnabled()){
					LOGGER.debug("SQL : {} / PARAMS : {}", SQL_FILE, Arrays.toString(params));
				}
				final LobHandler lobHandler = new DefaultLobHandler();
				jtemplate.query(SQL_FILE, params, new AbstractLobStreamingResultSetExtractor<Object>(){  
					@Override
					protected void streamData(ResultSet rs) throws SQLException, IOException, DataAccessException {
																
						OutputStream os=new FileOutputStream(imginfo.getDataFile());
						FileCopyUtils.copy(lobHandler.getBlobAsBinaryStream(rs,1),os); 
				        os.close();
					}  
				});
			}
		}
		return CollectionUtils.isEmpty(list) ? null : list.get(0);
	}

	@Override
	protected void initialJdbcTemplate(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}


	@Override
	public ImageInfo query(InfoId<Long> infoid, String parentPath) {
		
		String SQL = "SELECT image_id, image_name, image_format, image_link, persist_type, category, modifier, last_modified FROM gp_images "
				+ "WHERE image_id = ? ";
		
		String SQL_FILE = "SELECT image_data FROM gp_images WHERE image_id = ? ";
		Object[] params = new Object[]{				
				infoid.getId()
			};

		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("SQL : {} / PARAMS : {}", SQL, Arrays.toString(params));
		}
		List<ImageInfo> list = jtemplate.query(SQL, params, ImageMapper);
		
		if(list != null && list.size()>0 ){
						
			final ImageInfo imginfo = list.get(0);
			imginfo.setParentPath(parentPath);
			File parent = new File(imginfo.getParentPath());
			
			if(parent.exists()){
				if(LOGGER.isDebugEnabled()){
					LOGGER.debug("SQL : {} / PARAMS : {}", SQL_FILE, Arrays.toString(params));
				}
				final LobHandler lobHandler = new DefaultLobHandler();
				jtemplate.query(SQL_FILE, params, new AbstractLobStreamingResultSetExtractor<Object>(){  
					@Override
					protected void streamData(ResultSet rs) throws SQLException, IOException, DataAccessException {
											
						OutputStream os=new FileOutputStream(imginfo.getDataFile());
						FileCopyUtils.copy(lobHandler.getBlobAsBinaryStream(rs,1),os); 
				        os.close();
					}  
				});
			}
		}
		return CollectionUtils.isEmpty(list) ? null : list.get(0);
	}


	/**
	 * Parse the image info out of the image path string, it only process 
	 * local database persist type image
	 * 
	 * @param  imagePath the path of image file
	 **/
	public static ImageInfo parseLocalImageInfo(String imagePath){

		String filename = FilenameUtils.getName(imagePath);
		Long imgid = Images.parseImageId(filename);
		Date createDate = Images.parseTouchDate(filename);
		String extension = FilenameUtils.getExtension(filename);

		ImageInfo imginfo = new ImageInfo(imagePath.substring(0, imagePath.lastIndexOf(File.separator) + 1));
		imginfo.setModifyDate(createDate);
		imginfo.setInfoId(IdKey.IMAGE.getInfoId( imgid));
		imginfo.setDataFile(new File(imagePath));
		imginfo.setLink(filename);
		imginfo.setPersist(Images.Persist.DATABASE.name());
		imginfo.setFormat(extension);

		return imginfo;
	}
}
