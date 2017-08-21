package com.gp.dao;

import com.gp.common.IdKey;
import com.gp.common.IdKeys;
import com.gp.dao.info.QuickFlowInfo;
import com.gp.info.InfoId;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by garydiao on 8/19/16.
 */
public interface QuickFlowDAO extends BaseDAO<QuickFlowInfo>{

    public static RowMapper<QuickFlowInfo> QUICK_FLOW_ROWMAPPER = new RowMapper<QuickFlowInfo>() {
        @Override
        public QuickFlowInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
            QuickFlowInfo info = new QuickFlowInfo();

            InfoId<Long> id = IdKeys.getInfoId(IdKey.QUICK_FLOW, rs.getLong("flow_id"));
            info.setInfoId(id);

            info.setFlowName(rs.getString("flow_name"));
            info.setCategory(rs.getString("category"));
            info.setDuration(rs.getInt("duration"));
            info.setCustomProcess(rs.getString("cust_process"));

            info.setModifier(rs.getString("modifier"));
            info.setModifyDate(rs.getTimestamp("last_modified"));
            return info;
        }
    };
}
