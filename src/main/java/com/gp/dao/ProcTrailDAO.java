package com.gp.dao;

import com.gp.common.IdKey;
import com.gp.common.IdKeys;
import com.gp.dao.info.ProcTrailInfo;
import com.gp.info.InfoId;
import com.gp.info.KVPair;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;


/**
 * Created by garydiao on 10/4/16.
 */
public interface ProcTrailDAO extends BaseDAO<ProcTrailInfo>{


    /**
     *  Query all the attendees of process flow
     *  @param procId the id of process
     */
    public List<String> queryProcAttendees(InfoId<Long> procId);

    /**
     * Query the counts of states in a step
     **/
    public List<KVPair<String,Integer>> queryOpinionCounts(InfoId<Long> stepId);

    /**
     * Update the opinion of executor in a step
     **/
    public int updateOpinion(InfoId<Long> stepId, String executor, String opinion, String comment);

    static RowMapper<ProcTrailInfo> TrailMapper = new RowMapper<ProcTrailInfo>() {

        @Override
        public ProcTrailInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
            ProcTrailInfo info = new ProcTrailInfo();
            InfoId<Long> id = IdKeys.getInfoId(IdKey.PROC_TRAIL,rs.getLong("trail_id"));
            info.setInfoId(id);

            info.setProcId(rs.getLong("proc_id"));
            info.setStepId(rs.getLong("step_id"));
            info.setOpinion(rs.getString("opinion"));
            info.setExecutor(rs.getString("executor"));
            info.setComment(rs.getString("comment"));
            info.setExecuteTime(rs.getTimestamp("execute_time"));

            info.setModifier(rs.getString("modifier"));
            info.setModifyDate(rs.getTimestamp("last_modified"));
            return info;
        }
    };
}
