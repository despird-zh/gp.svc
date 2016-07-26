package com.gp.svc;

import java.security.Provider;
import java.util.List;

import com.gp.common.ServiceContext;
import com.gp.exception.ServiceException;
import com.gp.info.CombineInfo;
import com.gp.info.InfoId;
import com.gp.dao.info.PostInfo;
import com.gp.dao.info.UserInfo;
import com.gp.svc.info.PostExt;

public interface PostService {

	/**
	 * Create a new post in workgroup
	 * @param  postinfo the post information
	 * @param  attendees the attendees in discussion
	 **/
	public boolean newPost(ServiceContext svcctx, PostInfo postinfo, String[] attendees) throws ServiceException;

	/**
	 * Add attendee to post
	 * @param postKey the post id
	 * @param attendee the attendee account
	 **/
	public void addPostAttendee(ServiceContext svcctx, InfoId<Long> postKey, String attendee) throws ServiceException;

	/**
	 * Remove the attendee from post
	 * @param postKey the post id
	 * @param attendee the attendee account
	 **/
	public void removePostAttendee(ServiceContext svcctx, InfoId<Long> postKey, String attendee) throws ServiceException;

	/**
	 * Get the post attendees
	 * @param  postKey the post id
	 **/
	public List<UserInfo> getPostAttendees(ServiceContext svcctx, InfoId<Long> postKey) throws ServiceException;

	/**
	 * Find the combined post base and ext information
	 **/
	public List<CombineInfo<PostInfo, PostExt>> getPersonalPosts(ServiceContext svcctx, InfoId<Long> wgroupId) throws ServiceException;
}
