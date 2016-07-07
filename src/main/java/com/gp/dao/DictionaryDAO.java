package com.gp.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;

import com.gp.common.FlatColumns;
import com.gp.common.IdKey;
import com.gp.info.DictionaryInfo;
import com.gp.info.FlatColLocator;
import com.gp.info.InfoId;

public interface DictionaryDAO extends BaseDAO<DictionaryInfo>{


	public static RowMapper<DictionaryInfo> DictionaryMapper = new RowMapper<DictionaryInfo>(){

		@Override
		public DictionaryInfo mapRow(ResultSet rs, int arg1) throws SQLException {
			DictionaryInfo info = new DictionaryInfo();
			InfoId<Long> id = IdKey.DICTIONARY.getInfoId(rs.getLong("dict_id"));
			info.setInfoId(id);

			info.setGroup(rs.getString("dict_group"));
			info.setKey(rs.getString("dict_key"));
			info.setValue(rs.getString("dict_value"));
			info.setDefaultLang(rs.getString("default_lang"));
		
			Map<FlatColLocator, String> labelMap = new HashMap<FlatColLocator, String>();
			labelMap.put(FlatColumns.DICT_DE_DE, rs.getString(FlatColumns.DICT_DE_DE.getColumn()));
			labelMap.put(FlatColumns.DICT_EN_US, rs.getString(FlatColumns.DICT_EN_US.getColumn()));
			labelMap.put(FlatColumns.DICT_FR_FR, rs.getString(FlatColumns.DICT_ZH_CN.getColumn()));
			labelMap.put(FlatColumns.DICT_ZH_CN, rs.getString(FlatColumns.DICT_ZH_CN.getColumn()));
			labelMap.put(FlatColumns.DICT_RU_RU, rs.getString(FlatColumns.DICT_RU_RU.getColumn()));
			
			info.setLabelMap(labelMap);
			
			info.setModifier(rs.getString("modifier"));
			info.setModifyDate(rs.getTimestamp("last_modified"));
			
			return info;
		}
	};
}
