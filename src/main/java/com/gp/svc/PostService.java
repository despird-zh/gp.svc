package com.gp.svc;

import java.util.List;

import com.gp.common.ServiceContext;
import com.gp.exception.ServiceException;
import com.gp.info.InfoId;
import com.gp.info.PostInfo;
import com.gp.info.UserInfo;

public interface PostService {

	public InfoId<Long> newPost(ServiceContext<?> svcctx, PostInfo postinfo, String[] attendees) throws ServiceException;
	
	public void addPostUser(ServiceContext<?> svcctx, InfoId<Long> postKey, String attendee) throws ServiceException;
	
	public void removePostUser(ServiceContext<?> svcctx, InfoId<Long> postKey, String attendee) throws ServiceException;
	
	public List<UserInfo> getPostUsers(ServiceContext<?> svcctx, InfoId<Long> postKey) throws ServiceException;
	
	
}
