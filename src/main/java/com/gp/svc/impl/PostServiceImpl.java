package com.gp.svc.impl;

import com.gp.common.*;
import com.gp.config.ServiceConfigurer;
import com.gp.dao.*;
import com.gp.dao.info.GroupInfo;
import com.gp.dao.info.GroupUserInfo;
import com.gp.dao.info.PostInfo;
import com.gp.dao.info.UserInfo;
import com.gp.exception.ServiceException;
import com.gp.info.CombineInfo;
import com.gp.info.InfoId;
import com.gp.svc.CommonService;
import com.gp.svc.PostService;
import com.gp.svc.info.PostExt;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
            throw new ServiceException("", dae);
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
            throw new ServiceException("", dae);
        }
    }

    @Transactional(value = ServiceConfigurer.TRNS_MGR, readOnly = true)
    @Override
    public List<UserInfo> getPostAttendees(ServiceContext svcctx, InfoId<Long> postId) throws ServiceException {

        List<UserInfo> result = new ArrayList<>();
        StringBuffer SQL = new StringBuffer();
        SQL.append("SELECT * FROM gp_users ");
        SQL.append("WHERE account IN (SELECT gp_group_user WHERE group_id = ?)");

        JdbcTemplate jtemplate = pseudodao.getJdbcTemplate(JdbcTemplate.class);
        try{
            Long grpid = pseudodao.query(postId, FlatColumns.MBR_GRP_ID, Long.class);
            Object[] params = new Object[]{grpid};

            result = jtemplate.query(SQL.toString(), params, UserDAO.UserMapper);
        }catch(DataAccessException dae){

            throw new ServiceException("", dae);
        }
        return result;
    }

    @Transactional(value = ServiceConfigurer.TRNS_MGR, readOnly = true)
    @Override
    public List<CombineInfo<PostInfo, PostExt>> getPersonalPosts(ServiceContext svcctx, String account, 
			String state, 
			String type,
			String scope) throws ServiceException {

        final List<CombineInfo<PostInfo, PostExt>> result = new ArrayList<CombineInfo<PostInfo, PostExt>>();
        List<Object> paramlist = new ArrayList<Object>();
        StringBuffer SQL = new StringBuffer();
        SQL.append("SELECT * FROM gp_personal_posts ");
        SQL.append("WHERE workgroup_id = ? AND owner = ? ");
        paramlist.add(Long.valueOf(GeneralConstants.PERSON_WORKGROUP));
        paramlist.add(account);

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
            throw new ServiceException("",dae);
        }
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
            throw new ServiceException("", dae);
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
            throw new ServiceException("", dae);
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
            throw new ServiceException("",dae);
        }
    }


}
