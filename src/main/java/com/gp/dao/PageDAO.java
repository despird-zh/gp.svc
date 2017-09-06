package com.gp.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.lang.StringUtils;
import org.springframework.jdbc.core.RowMapper;

import com.gp.common.IdKey;
import com.gp.common.IdKeys;
import com.gp.info.FlatColumn;
import com.gp.info.InfoId;
import com.gp.dao.info.PageInfo;

public interface PageDAO extends BaseDAO<PageInfo>{
	
	public static final int COLUMN_COUNT = 20;
	
	
	public static RowMapper<PageInfo> PAGE_MAPPER = new RowMapper<PageInfo>(){

		@Override
		public PageInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
			PageInfo page = new PageInfo();
			
			InfoId<Integer> mid = IdKeys.getInfoId(IdKey.GP_PAGES, rs.getInt("page_id"));
			page.setInfoId(mid);
			
			page.setDescription(rs.getString("descr"));
			page.setModule(rs.getString("module"));
			page.setPageName(rs.getString("page_name"));
			page.setPageAbbr(rs.getString("page_abbr"));
			page.setPageUrl(rs.getString("page_url"));
			
			for(int i = 1; i <= COLUMN_COUNT ; i++){
				String val = rs.getString("act_abbr_"+i);
				if(StringUtils.isNotBlank(val)){
					FlatColumn col = new FlatColumn("act_abbr_", i);
					page.putColValue(col, val);
				}
				
			}
			
			page.setModifier(rs.getString("modifier"));
			page.setModifyDate(rs.getTimestamp("last_modified"));
			return page;
		}
		
	};

}
