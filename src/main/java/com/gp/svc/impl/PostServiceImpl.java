package com.gp.svc.impl;

import com.gp.common.GroupUsers;
import com.gp.common.IdKey;
import com.gp.common.ServiceContext;
import com.gp.config.ServiceConfigurer;
import com.gp.dao.GroupDAO;
import com.gp.dao.GroupUserDAO;
import com.gp.dao.PostDAO;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by garydiao on 7/22/16.
 */

@Service("postService")
public class PostServiceImpl implements PostService{

    @Autowired
    GroupDAO groupdao;

    @Autowired
    GroupUserDAO groupuserdao;

    @Autowired
    PostDAO postdao;

    @Autowired
    CommonService idService;

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

    @Override
    public void addPostAttendee(ServiceContext svcctx, InfoId<Long> postKey, String attendee) throws ServiceException {


    }

    @Override
    public void removePostAttendee(ServiceContext svcctx, InfoId<Long> postKey, String attendee) throws ServiceException {

    }

    @Override
    public List<UserInfo> getPostAttendees(ServiceContext svcctx, InfoId<Long> postKey) throws ServiceException {
        return null;
    }

    @Override
    public List<CombineInfo<PostInfo, PostExt>> getPersonalPosts(ServiceContext svcctx, InfoId<Long> wgroupId) throws ServiceException {

        StringBuffer SQL = new StringBuffer();
        SQL.append("SELECT * FROM ")
        return null;
    }


}
