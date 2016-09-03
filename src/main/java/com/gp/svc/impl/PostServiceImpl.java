package com.gp.svc.impl;

import com.gp.common.*;
import com.gp.config.ServiceConfigurer;
import com.gp.dao.*;
import com.gp.dao.info.*;
import com.gp.exception.ServiceException;
import com.gp.info.CombineInfo;
import com.gp.info.FlatColumn;
import com.gp.info.InfoId;
import com.gp.pagination.PageQuery;
import com.gp.pagination.PageWrapper;
import com.gp.pagination.PaginationHelper;
import com.gp.pagination.PaginationInfo;
import com.gp.svc.CommonService;
import com.gp.svc.PostService;
import com.gp.svc.SecurityService;
import com.gp.svc.info.PostExt;
import com.gp.svc.info.UserLite;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by garydiao on 7/22/16.
 */

@Service("postService")
public class PostServiceImpl implements PostService{

    static Logger LOGGER = LoggerFactory.getLogger(PostService.class);

    @Autowired
    GroupDAO groupdao;

    @Autowired
    GroupUserDAO groupuserdao;

    @Autowired
    PostDAO postdao;

    @Autowired
    CommonService idService;

    @Autowired
    PseudoDAO pseudodao;

    @Autowired
    PostCommentDAO commentdao;

    @Autowired
    VoteDAO votedao;

    /**
     * Create a new post
     **/
    @Transactional(ServiceConfigurer.TRNS_MGR)
    @Override
    public boolean newPost(ServiceContext svcctx, PostInfo postinfo, String[] attendees) throws ServiceException {

        try{

            InfoId<Long> postid = postinfo.getInfoId();

            svcctx.setTraceInfo(postinfo);
            // create a new group for post
            GroupInfo group = new GroupInfo();
            InfoId<Long> grpid = idService.generateId( IdKey.GROUP, Long.class);
            group.setInfoId(grpid);
            group.setGroupName("Post's Attendee Group");
            group.setGroupType(GroupUsers.GroupType.POST_MBR.name());
            group.setManageId(postid.getId());
            postinfo.setMemberGroupId(grpid.getId());
            svcctx.setTraceInfo(group);
            groupdao.create(group);

            // create group user record
            GroupUserInfo mbrinfo= new GroupUserInfo();
            InfoId<Long> guid = idService.generateId(IdKey.GROUP_USER, Long.class);
            mbrinfo.setInfoId(guid);
            mbrinfo.setAccount(postinfo.getOwner());
            mbrinfo.setGroupId(grpid.getId());
            mbrinfo.setRole(GroupUsers.PostMemberRole.HOST.name());
            svcctx.setTraceInfo(mbrinfo);
            groupuserdao.create(mbrinfo);

            // create post attendee
            if(ArrayUtils.isNotEmpty(attendees)){
                for(String attendee: attendees){

                    mbrinfo= new GroupUserInfo();
                    guid = idService.generateId(IdKey.GROUP_USER, Long.class);
                    mbrinfo.setInfoId(guid);
                    mbrinfo.setAccount(attendee);
                    mbrinfo.setGroupId(grpid.getId());
                    mbrinfo.setRole(GroupUsers.PostMemberRole.ATTENDEE.name());
                    svcctx.setTraceInfo(mbrinfo);
                    groupuserdao.create(mbrinfo);
                }
            }

            postinfo.setMemberGroupId(grpid.getId());
            return postdao.create(postinfo) > 0;

        }catch(DataAccessException dae){

            throw new ServiceException("excp.create", dae, "post");
        }

    }

    @Transactional(ServiceConfigurer.TRNS_MGR)
    @Override
    public void addPostAttendee(ServiceContext svcctx, InfoId<Long> postId, String attendee) throws ServiceException {

        try{
            Long mbrid = pseudodao.query(postId, FlatColumns.MBR_GRP_ID, Long.class);
            GroupUserInfo mbrinfo= new GroupUserInfo();
            InfoId<Long> guid = idService.generateId(IdKey.GROUP_USER, Long.class);
            mbrinfo.setInfoId(guid);
            mbrinfo.setAccount(attendee);
            mbrinfo.setGroupId(mbrid);
            mbrinfo.setRole(GroupUsers.PostMemberRole.ATTENDEE.name());
            svcctx.setTraceInfo(mbrinfo);
            groupuserdao.create(mbrinfo);

        }catch(DataAccessException dae){
            throw new ServiceException("excp.create", dae,"post attendee");
        }
    }

    @Transactional(ServiceConfigurer.TRNS_MGR)
    @Override
    public void removePostAttendee(ServiceContext svcctx, InfoId<Long> postId, String attendee) throws ServiceException {
        try{
            Long mbrid = pseudodao.query(postId, FlatColumns.MBR_GRP_ID, Long.class);
            InfoId<Long> grpid = IdKey.GROUP.getInfoId(mbrid);
            groupuserdao.deleteByAccount(grpid, attendee);

        }catch(DataAccessException dae){
            throw new ServiceException("excp.delete", dae, "post attendee");
        }
    }

    @Transactional(value = ServiceConfigurer.TRNS_MGR, readOnly = true)
    @Override
    public List<UserLite> getPostAttendees(ServiceContext svcctx, InfoId<Long> postId) throws ServiceException {

        List<UserLite> result = new ArrayList<>();

        StringBuffer SQL_SEL = new StringBuffer();
        StringBuffer SQL_1 = new StringBuffer();
        SQL_SEL.append("select usr.user_id,");
        SQL_SEL.append("usr.account, ");
        SQL_SEL.append("usr.full_name,");
        SQL_SEL.append("usr.email,");
        SQL_SEL.append("src.source_id,");
        SQL_SEL.append("src.source_name, ");
        SQL_SEL.append("img.image_id,");
        SQL_SEL.append("img.image_link ");
        SQL_SEL.append("from gp_users usr ");
        SQL_SEL.append("left join (select image_id, image_link, persist_type from gp_images) img on usr.avatar_id = img.image_id ");
        SQL_SEL.append("left join (select source_id, source_name from gp_sources) src on usr.source_id = src.source_id ");

        SQL_1.append("WHERE usr.account IN (SELECT account from gp_group_user WHERE group_id = ?)");

        StringBuffer SQL_2 = new StringBuffer();
        SQL_2.append("WHERE usr.account IN (select owner from gp_post_comments where post_id = ?) ");

        JdbcTemplate jtemplate = pseudodao.getJdbcTemplate(JdbcTemplate.class);
        try{
            Long grpid = pseudodao.query(postId, FlatColumns.MBR_GRP_ID, Long.class);
            Object[] params = new Object[]{grpid};
            if(grpid > 0){

                SQL_SEL.append(SQL_1);
            }else{

                SQL_SEL.append(SQL_2);
            }

            if(LOGGER.isDebugEnabled()){
                LOGGER.debug("SQL : {} / PARAMS : {}", SQL_SEL.toString(), ArrayUtils.toString(params));
            }
            result = jtemplate.query(SQL_SEL.toString(), params, SecurityService.USER_LITE_ROW_MAPPER);
        }catch(DataAccessException dae){

            throw new ServiceException("excp.query", dae, "post attendee");
        }
        return result;
    }

    @Transactional(value = ServiceConfigurer.TRNS_MGR, readOnly = true)
    @Override
    public PageWrapper<CombineInfo<PostInfo, PostExt>> getPersonalPosts(ServiceContext svcctx, String account,
                                                                        String state,
                                                                        String type,
                                                                        String scope,
                                                                        PageQuery pagequery) throws ServiceException {

        final List<CombineInfo<PostInfo, PostExt>> result = new ArrayList<CombineInfo<PostInfo, PostExt>>();

        StringBuffer SQL_COLS = new StringBuffer("SELECT * ");
        StringBuffer SQL_COUNT_COLS = new StringBuffer("SELECT count(post_id) ");

        StringBuffer SQL = new StringBuffer();
        SQL.append("FROM gp_personal_posts ");
        SQL.append("WHERE workgroup_id = :wgroup_id AND owner = :owner ");

        Map<String,Object> params = new HashMap<String,Object>();
        params.put("wgroup_id", Long.valueOf(GeneralConstants.PERSONAL_WORKGROUP));
        params.put("owner", account);

        if(StringUtils.isNotBlank(state)){
            SQL.append(" AND state = :state ");
            params.put("state", state);
        }
        if(StringUtils.isNotBlank(type)){
            SQL.append(" AND post_type = :postType ");
            params.put("postType", type);
        }
        if(StringUtils.isNotBlank(scope)){

            SQL.append(" AND scope = :scope ");
            params.put("scope", scope);
        }
        
        SQL.append(" ORDER BY last_modified desc");
        
        NamedParameterJdbcTemplate jtemplate = pseudodao.getJdbcTemplate(NamedParameterJdbcTemplate.class);
        PageWrapper<CombineInfo<PostInfo, PostExt>> pwrapper = new PageWrapper<>();

        if(pagequery.isTotalCountEnable()){
            int totalrow = pseudodao.queryRowCount(jtemplate, SQL_COUNT_COLS.append(SQL).toString(), params);
            // calculate pagination information, the page menu number is 5
            PaginationInfo pagination = new PaginationHelper(totalrow,
                    pagequery.getPageNumber(),
                    pagequery.getPageSize(), 5).getPaginationInfo();

            pwrapper.setPagination(pagination);
        }
        // get page query sql
        String pagesql = pseudodao.getPageQuerySql(SQL_COLS.append(SQL).toString(), pagequery);

        if(LOGGER.isDebugEnabled()){

            LOGGER.debug("SQL : {} / PARAMS : {}", pagesql, params.toString());
        }

        try {
            jtemplate.query(pagesql, params, new RowCallbackHandler() {
                @Override
                public void processRow(ResultSet rs) throws SQLException {
                    CombineInfo<PostInfo, PostExt> row = new CombineInfo<PostInfo, PostExt>();
                    PostInfo base = PostDAO.PostMapper.mapRow(rs, result.size());
                    PostExt ext = POST_EXT_ROW_MAPPER.mapRow(rs, result.size());
                    row.setPrimary(base);
                    row.setExtended(ext);

                    result.add(row);
                }
            });

        }catch(DataAccessException dae){
            throw new ServiceException("excp.query",dae, "personal's posts");
        }
        pwrapper.setRows(result);

        return pwrapper;
    }

    @Transactional(value = ServiceConfigurer.TRNS_MGR, readOnly = true)
    @Override
    public List<CombineInfo<PostInfo, PostExt>> getPersonalJoinedPosts(ServiceContext svcctx, String account, String state, String type, String scope) throws ServiceException {

        final List<CombineInfo<PostInfo, PostExt>> result = new ArrayList<CombineInfo<PostInfo, PostExt>>();
        List<Object> paramlist = new ArrayList<Object>();

        StringBuffer SQL = new StringBuffer();
        SQL.append("SELECT pts.*, ");
        SQL.append(" src.source_name,");
        SQL.append(" usrs.full_name as user_name, ");
        SQL.append(" grp.workgroup_name ");
        SQL.append("FROM gp_posts pts ");
        SQL.append(" LEFT JOIN (SELECT source_id, source_name from gp_sources) src on src.source_id = pts.source_id ");
        SQL.append(" LEFT JOIN (SELECT workgroup_id, workgroup_name from gp_workgroups ) grp on grp.workgroup_id = pts.workgroup_id ");
        SQL.append(" LEFT JOIN (SELECT account, full_name from gp_users) usrs on usrs.account = pts.owner ");
        SQL.append("WHERE pts.owner != ? ");
        SQL.append("AND (EXISTS (SELECT 1 from gp_group_user gusr where gusr.group_id = pts.mbr_group_id and gusr.account = ?) OR ");
        SQL.append("        (SELECT 1 from gp_post_comments cmts where cmts.post_id = pts.post_id and cmts.author = ?) ) ");
        paramlist.add(account);
        paramlist.add(account);
        paramlist.add(account);

        if(StringUtils.isNotBlank(state)){
            SQL.append(" AND pts.state = ? ");
            paramlist.add(type);
        }

        if(StringUtils.isNotBlank(type)){
            SQL.append(" AND pts.post_type = ? ");
            paramlist.add(type);
        }
        if(StringUtils.isNotBlank(scope)){

            SQL.append(" AND pts.scope = ? ");
            paramlist.add(scope);
        }

        JdbcTemplate jtemplate = pseudodao.getJdbcTemplate(JdbcTemplate.class);
        if(LOGGER.isDebugEnabled()){

            LOGGER.debug("SQL : {} / PARAMS : {}", SQL.toString(), paramlist.toString());
        }

        try {
            jtemplate.query(SQL.toString(), paramlist.toArray(), new RowCallbackHandler() {
                @Override
                public void processRow(ResultSet rs) throws SQLException {
                    CombineInfo<PostInfo, PostExt> row = new CombineInfo<PostInfo, PostExt>();
                    PostInfo base = PostDAO.PostMapper.mapRow(rs, result.size());
                    PostExt ext = POST_EXT_ROW_MAPPER.mapRow(rs, result.size());
                    row.setPrimary(base);
                    row.setExtended(ext);

                    result.add(row);
                }
            });

            return result;
        }catch(DataAccessException dae){
            throw new ServiceException("excp.query", dae,"personal joined posts");
        }
    }

    @Transactional(value = ServiceConfigurer.TRNS_MGR, readOnly = true)
    @Override
    public List<CombineInfo<PostInfo, PostExt>> getWorkgroupPosts(ServiceContext svcctx, InfoId<Long> wid, String state, String type, String scope) throws ServiceException {
        final List<CombineInfo<PostInfo, PostExt>> result = new ArrayList<CombineInfo<PostInfo, PostExt>>();
        List<Object> paramlist = new ArrayList<Object>();
        StringBuffer SQL = new StringBuffer();
        SQL.append("SELECT * FROM gp_wrokgroup_posts ");
        SQL.append("WHERE workgroup_id = ? ");
        paramlist.add(Long.valueOf(wid.getId()));

        if(StringUtils.isNotBlank(state)){
            SQL.append(" AND state = ? ");
            paramlist.add(type);
        }
        if(StringUtils.isNotBlank(type)){
            SQL.append(" AND post_type = ? ");
            paramlist.add(type);
        }
        if(StringUtils.isNotBlank(scope)){

            SQL.append(" AND scope = ?");
            paramlist.add(scope);
        }

        JdbcTemplate jtemplate = pseudodao.getJdbcTemplate(JdbcTemplate.class);
        if(LOGGER.isDebugEnabled()){

            LOGGER.debug("SQL : {} / PARAMS : {}", SQL.toString(), paramlist.toString());
        }

        try {
            jtemplate.query(SQL.toString(), paramlist.toArray(), new RowCallbackHandler() {
                @Override
                public void processRow(ResultSet rs) throws SQLException {
                    CombineInfo<PostInfo, PostExt> row = new CombineInfo<PostInfo, PostExt>();
                    PostInfo base = PostDAO.PostMapper.mapRow(rs, result.size());
                    PostExt ext = POST_EXT_ROW_MAPPER.mapRow(rs, result.size());
                    row.setPrimary(base);
                    row.setExtended(ext);

                    result.add(row);
                }
            });

            return result;
        }catch(DataAccessException dae){
            throw new ServiceException("excp.query", dae,"workgroup's posts");
        }
    }

    @Transactional(value = ServiceConfigurer.TRNS_MGR, readOnly = true)
    @Override
    public List<CombineInfo<PostInfo, PostExt>> getSquarePosts(ServiceContext svcctx, String state, String type, String scope) throws ServiceException {
        final List<CombineInfo<PostInfo, PostExt>> result = new ArrayList<CombineInfo<PostInfo, PostExt>>();
        List<Object> paramlist = new ArrayList<Object>();
        StringBuffer SQL = new StringBuffer();
        SQL.append("SELECT * FROM gp_square_posts ");
        SQL.append("WHERE 1=1 ");

        if(StringUtils.isNotBlank(state)){
            SQL.append(" AND state = ? ");
            paramlist.add(type);
        }
        if(StringUtils.isNotBlank(type)){
            SQL.append(" AND post_type = ? ");
            paramlist.add(type);
        }
        if(StringUtils.isNotBlank(scope)){

            SQL.append(" AND scope = ?");
            paramlist.add(scope);
        }

        JdbcTemplate jtemplate = pseudodao.getJdbcTemplate(JdbcTemplate.class);
        if(LOGGER.isDebugEnabled()){

            LOGGER.debug("SQL : {} / PARAMS : {}", SQL.toString(), paramlist.toString());
        }

        try {
            jtemplate.query(SQL.toString(), paramlist.toArray(), new RowCallbackHandler() {
                @Override
                public void processRow(ResultSet rs) throws SQLException {
                    CombineInfo<PostInfo, PostExt> row = new CombineInfo<PostInfo, PostExt>();
                    PostInfo base = PostDAO.PostMapper.mapRow(rs, result.size());
                    PostExt ext = POST_EXT_ROW_MAPPER.mapRow(rs, result.size());
                    row.setPrimary(base);
                    row.setExtended(ext);

                    result.add(row);
                }
            });

            return result;
        }catch(DataAccessException dae){
            throw new ServiceException("excp.query",dae, "square's posts");
        }
    }

    @Transactional(value = ServiceConfigurer.TRNS_MGR, readOnly = true)
    @Override
    public List<PostCommentInfo> getPostComments(ServiceContext svcctx, InfoId<Long> postid, String owner, String state) throws ServiceException {

        List<Object> paramlist = new ArrayList<>();

        StringBuffer SQL = new StringBuffer();
        SQL.append("select * from gp_post_comments ");
        SQL.append("where 1=1 ");
        if(InfoId.isValid(postid)) {

            SQL.append("AND post_id = ? ");
            paramlist.add(postid.getId());
        }
        if(StringUtils.isNotBlank(owner)){
            SQL.append("AND owner = ? ");
            paramlist.add(owner);
        }
        if(StringUtils.isNotBlank(state)){
            SQL.append("AND state = ? ");
            paramlist.add(state);
        }

        JdbcTemplate jtemplate = pseudodao.getJdbcTemplate(JdbcTemplate.class);
        if(LOGGER.isDebugEnabled()){

            LOGGER.debug("SQL : {} / PARAMS : {}", SQL.toString(), paramlist.toString());
        }

        try {

            return jtemplate.query(SQL.toString(), paramlist.toArray(), PostCommentDAO.PostCommentMapper);

        }catch(DataAccessException dae){
            throw new ServiceException("excp.query",dae, "post comments");
        }
    }

    @Transactional(ServiceConfigurer.TRNS_MGR)
    @Override
    public boolean newComment(ServiceContext svcctx, PostCommentInfo commentinfo) throws ServiceException {

        try{
        	svcctx.setTraceInfo(commentinfo);
            return commentdao.create(commentinfo) > 0;
        }catch(DataAccessException dae){
            throw new ServiceException("excp.create", dae, "comment");
        }

    }

    @Override
    public boolean addPostLike(ServiceContext svcctx, InfoId<Long> postId, String voter) throws ServiceException {

        VoteInfo vote = null;
        try{
        	int cnt = 0;
        	vote = votedao.queryByAccount(postId, voter);
        	if(null == vote){
        		vote = new VoteInfo();
                InfoId<Long> vid = idService.generateId(IdKey.VOTE, Long.class);
                vote.setInfoId(vid);
                vote.setOpinion(TagVotes.VoteOpinion.LIKE.name());
                vote.setVoter(voter);
                vote.setResourceId(postId.getId());
                vote.setResourceType(postId.getIdKey());

                Long wid = idService.query(postId, FlatColumns.WORKGROUP_ID, Long.class);
                vote.setWorkgroupId(wid);

                svcctx.setTraceInfo(vote);
                cnt = votedao.create(vote);
        	}else{
        		
        		FlatColumn[] cols = new FlatColumn[]{FlatColumns.OPINION, FlatColumns.MODIFIER, FlatColumns.MODIFY_DATE};
        		Object[] vals = new Object[]{TagVotes.VoteOpinion.LIKE.name(), svcctx.getPrincipal().getAccount(), new Date(System.currentTimeMillis())};
        		cnt = pseudodao.update(vote.getInfoId(), cols, vals);
        	}
           
            return cnt >0 ;

        }catch(DataAccessException dae){

            throw new ServiceException("excp.create", dae, "like voting");
        }

    }

    @Override
    public boolean addPostDislike(ServiceContext svcctx, InfoId<Long> postId, String voter) throws ServiceException {

        VoteInfo vote = null;
       
        try{
        	int cnt = 0;
        	vote = votedao.queryByAccount(postId, voter);
        	if( null == vote){
        		vote = new VoteInfo();
        		InfoId<Long> vid = idService.generateId(IdKey.VOTE, Long.class);
		        vote.setInfoId(vid);
		        vote.setOpinion(TagVotes.VoteOpinion.DISLIKE.name());
		        vote.setVoter(voter);
		        vote.setResourceId(postId.getId());
		        vote.setResourceType(postId.getIdKey());
		
		        Long wid = idService.query(postId, FlatColumns.WORKGROUP_ID, Long.class);
		        vote.setWorkgroupId(wid);
		
		        svcctx.setTraceInfo(vote);
	            cnt = votedao.create(vote);
	           
        	}else{
        		
        		FlatColumn[] cols = new FlatColumn[]{FlatColumns.OPINION, FlatColumns.MODIFIER, FlatColumns.MODIFY_DATE};
        		Object[] vals = new Object[]{TagVotes.VoteOpinion.DISLIKE.name(), svcctx.getPrincipal().getAccount(), new Date(System.currentTimeMillis())};
        		cnt = pseudodao.update(vote.getInfoId(), cols, vals);
        		
        	} 
        	return cnt >0 ;
        }catch(DataAccessException dae){

            throw new ServiceException("excp.create", dae, "dislike voting");
        }
    }

	@Override
	public boolean publicPost(ServiceContext svcctx, InfoId<Long> postId) throws ServiceException {
		
		try{
			int cnt = pseudodao.update(postId, FlatColumns.SCOPE, Posts.Scope.SQUARE.name());
			return cnt > 0;
		}catch(DataAccessException dae){
			throw new ServiceException("excp.update.with", dae, "post scope", postId.toString());
		}
	}

}
