package com.gp.dao;

import com.gp.common.IdKey;
import com.gp.dao.info.ProcStepInfo;
import com.gp.info.InfoId;
import com.gp.info.KVPair;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by garydiao on 8/19/16.
 */
public interface ProcStepDAO extends BaseDAO<ProcStepInfo>{

	/**
	 *  find all the attendees of process flow
	 *  @param procId the id of process
	 */
	public List<String> queryProcAttendees(InfoId<Long> procId);
	
	public List<KVPair<String,Integer>> queryStepStateCounts(InfoId<Long> procId);
	
	/**
	 * the process step row mapper 
	 **/
    public static RowMapper<ProcStepInfo> ProcStepRowMapper = new RowMapper<ProcStepInfo>() {
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

            info.setModifier(rs.getString("modifier"));
            info.setModifyDate(rs.getTimestamp("last_modified"));
            return info;
        }
    };
}
