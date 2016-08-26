package com.gp.dao;

import com.gp.common.IdKey;
import com.gp.dao.info.ProcStepInfo;
import com.gp.info.InfoId;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by garydiao on 8/19/16.
 */
public interface ProcStepDAO extends BaseDAO<ProcStepInfo>{

	public List<String> queryProcAttendees(InfoId<Long> procId);
	
    public static RowMapper<ProcStepInfo> PROC_STEP_ROWMAPPER = new RowMapper<ProcStepInfo>() {
        @Override
        public ProcStepInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
            ProcStepInfo info = new ProcStepInfo();

            InfoId<Long> id = IdKey.PROC_STEP.getInfoId(rs.getLong("step_id"));
            info.setInfoId(id);

            info.setProcId(rs.getLong("proc_id"));
            info.setNodeId(rs.getLong("node_id"));
            info.setOpinion(rs.getString("opinion"));
            info.setState(rs.getString("state"));
            info.setComment(rs.getString("comment"));
            info.setCreateTime(rs.getTimestamp("create_time"));
            info.setExecuteTime(rs.getTimestamp("exec_time"));
            info.setState(rs.getString("state"));
            info.setExecutor(rs.getString("executor"));
            info.setPrevStep(rs.getLong("prev_step"));
            info.setNextStep(rs.getLong("next_step"));

            info.setModifier(rs.getString("modifier"));
            info.setModifyDate(rs.getTimestamp("last_modified"));
            return info;
        }
    };
}
