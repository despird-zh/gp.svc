package com.gp.svc.impl;

import com.gp.common.ServiceContext;
import com.gp.dao.info.PostInfo;
import com.gp.dao.info.UserInfo;
import com.gp.exception.ServiceException;
import com.gp.info.InfoId;
import com.gp.svc.PostService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by garydiao on 7/22/16.
 */

@Service("postService")
public class PostServiceImpl implements PostService{

    @Override
    public InfoId<Long> newPost(ServiceContext svcctx, PostInfo postinfo, String[] attendees) throws ServiceException {
        return null;
    }

    @Override
    public void addPostUser(ServiceContext svcctx, InfoId<Long> postKey, String attendee) throws ServiceException {

    }

    @Override
    public void removePostUser(ServiceContext svcctx, InfoId<Long> postKey, String attendee) throws ServiceException {

    }

    @Override
    public List<UserInfo> getPostUsers(ServiceContext svcctx, InfoId<Long> postKey) throws ServiceException {
        return null;
    }
}
