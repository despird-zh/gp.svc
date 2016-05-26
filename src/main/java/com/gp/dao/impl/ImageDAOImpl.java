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
import java.util.Arrays;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.AbstractLobCreatingPreparedStatementCallback;
import org.springframework.jdbc.core.support.AbstractLobStreamingResultSetExtractor;
import org.springframework.jdbc.support.lob.DefaultLobHandler;
import org.springframework.jdbc.support.lob.LobCreator;
import org.springframework.jdbc.support.lob.LobHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

import com.gp.common.IdKey;
import com.gp.config.ServiceConfigurer;
import com.gp.dao.ImageDAO;
import com.gp.info.ImageInfo;
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
		
		final File binaryFile = info.getImageFile();
		String INS_SQL = "INSERT into gp_images (image_id,image_name, image_format, image_ext, modifier, last_modified) VALUES (?, ?, ?, ?, ?, ?)";
		String UPD_SQL = "UPDATE gp_images SET image_data = ? , touch_time = ? WHERE image_id = ? ";
	
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate(JdbcTemplate.class);
		final LobHandler lobHandler = new DefaultLobHandler();

		InputStream is = null;
		Object[] params = new Object[]{
				info.getInfoId().getId(),
				info.getImageName(),
				info.getFormat(),
				info.getExtension(),
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
							pstmt.setTimestamp(2, new Timestamp(info.getTouchTime().getTime()));						
							pstmt.setLong(3, info.getInfoId().getId());
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
	public int update(final ImageInfo info) {
		
		final File binaryFile = info.getImageFile();
		StringBuffer SQL = new StringBuffer();
		SQL.append("UPDATE gp_images SET ")
			.append("image_name = ?, ");
		if(null != binaryFile && binaryFile.exists()){
			SQL.append("image_format =? , image_ext = ?,");
		}
		SQL.append("modifier = ?, last_modified = ? ")
			.append("WHERE image_id = ?");
		
		String UPD_SQL = "UPDATE gp_images SET image_data = ? , touch_time = ? WHERE image_id = ? ";
		
		Object[] params = null;
		if(null != binaryFile && binaryFile.exists()){
			params = new Object[]{	
					info.getImageName(),
					info.getFormat(),
					info.getExtension(),
					info.getModifier(),
					info.getModifyDate(),
					info.getInfoId().getId()
			};
		}else{
			params = new Object[]{	
					info.getImageName(),
					info.getModifier(),
					info.getModifyDate(),
					info.getInfoId().getId()
			};
		}
		final LobHandler lobHandler = new DefaultLobHandler();
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		InputStream is = null;
		int cnt =0;
		try{
			if(LOGGER.isDebugEnabled()){
				LOGGER.debug("SQL : {} / PARAMS : {}", SQL, Arrays.toString(params));
			}
			cnt = jtemplate.update(SQL.toString(), params);
			if(cnt > 0 && null != binaryFile && binaryFile.exists()){
			is = new FileInputStream(binaryFile);
				final InputStream fis = is;
				if(LOGGER.isDebugEnabled()){
					LOGGER.debug("SQL : {} / PARAMS : {}", UPD_SQL, Arrays.toString(params));
				}
				jtemplate.execute(UPD_SQL, new AbstractLobCreatingPreparedStatementCallback(lobHandler) {
					protected void setValues(PreparedStatement pstmt, LobCreator lobCreator) throws SQLException{
						lobCreator.setBlobAsBinaryStream(pstmt, 1, fis, (int) binaryFile.length());
						pstmt.setTimestamp(2, new Timestamp(info.getTouchTime().getTime()));						
						pstmt.setLong(3, info.getInfoId().getId());
					}
				});
			}
			return cnt;
		}catch (FileNotFoundException e) {
			// ignore
			e.printStackTrace();
		}finally{
			try {
				if(null != is)
					is.close();
			} catch (IOException e) {
				// ignore	
			}
		}
		
		return 0;
	}

	@Override
	public ImageInfo query( final InfoId<?> id) {
		
		String SQL = "SELECT image_id, image_name, image_format, image_ext,touch_time, modifier, last_modified FROM gp_images "
				+ "WHERE image_id = ? ";
		
		String SQL_FILE = "SELECT image_data, touch_time FROM gp_images WHERE image_id = ? ";
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
						
						imginfo.setTouchTime(rs.getTimestamp("touch_time"));												
						OutputStream os=new FileOutputStream(imginfo.getImageFile());
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

	public static RowMapper<ImageInfo> ImageMapper = new RowMapper<ImageInfo>(){
		
		@Override
		public ImageInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
			ImageInfo info = new ImageInfo();
			InfoId<Long> id = IdKey.IMAGE.getInfoId(rs.getLong("image_id"));
			info.setInfoId(id);
			
			info.setImageName(rs.getString("image_name"));
			info.setFormat(rs.getString("image_format"));
			info.setExtension(rs.getString("image_ext"));
			info.setTouchTime(rs.getTimestamp("touch_time"));
			info.setModifier(rs.getString("modifier"));
			info.setModifyDate(rs.getTimestamp("last_modified"));
			
			return info;
		}
	};
	
	@Override
	public RowMapper<ImageInfo> getRowMapper() {
		
		return ImageMapper;
	}

	@Override
	public ImageInfo query(InfoId<Long> infoid, String parentPath) {
		
		String SQL = "SELECT image_id, image_name, image_format, image_ext,touch_time, modifier, last_modified FROM gp_images "
				+ "WHERE image_id = ? ";
		
		String SQL_FILE = "SELECT image_data, touch_time FROM gp_images WHERE image_id = ? ";
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
						
						imginfo.setTouchTime(rs.getTimestamp("touch_time"));												
						OutputStream os=new FileOutputStream(imginfo.getImageFile());
						FileCopyUtils.copy(lobHandler.getBlobAsBinaryStream(rs,1),os); 
				        os.close();
					}  
				});
			}
		}
		return CollectionUtils.isEmpty(list) ? null : list.get(0);
	}
}
