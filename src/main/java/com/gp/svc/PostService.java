package com.gp.svc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.gp.common.ServiceContext;
import com.gp.dao.info.PostCommentInfo;
import com.gp.exception.ServiceException;
import com.gp.info.CombineInfo;
import com.gp.info.InfoId;
import com.gp.dao.info.PostInfo;
import com.gp.dao.info.UserInfo;
import com.gp.svc.info.PostExt;
import org.springframework.jdbc.core.RowMapper;


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
	 * Find the combined post base and ext information of personal
	 * @param account the user account
	 * @param state the state of post
	 * @param type the type of post
	 * @param scope the scope of post
	 **/
	public List<CombineInfo<PostInfo, PostExt>> getPersonalPosts(ServiceContext svcctx, 
			String account, 
			String state, 
			String type,
			String scope) throws ServiceException;

	/**
	 * Find the combined post base and ext information of personal
	 * @param account the user account
	 * @param state the state of post
	 * @param type the type of post
	 * @param scope the scope of post
	 **/
	public List<CombineInfo<PostInfo, PostExt>> getPersonalJoinedPosts(ServiceContext svcctx,
																 String account,
																 String state,
																 String type,
																 String scope) throws ServiceException;

	/**
	 * Find the combined post data(base and ext) of a work group
	 * @param wid the id of workgroup
	 * @param state the state of post
	 * @param type the type of post
	 * @param scope the scope of post
	 **/
	public List<CombineInfo<PostInfo, PostExt>> getWorkgroupPosts(ServiceContext svcctx,
																 InfoId<Long> wid,
																 String state,
																 String type,
																 String scope) throws ServiceException;

	/**
	 * Find the combined post data(base and ext) to be presented in square
	 * @param state the state of post
	 * @param type the type of post
	 * @param scope the scope of post
	 **/
	public List<CombineInfo<PostInfo, PostExt>> getSquarePosts(ServiceContext svcctx,
																  String state,
																  String type,
																  String scope) throws ServiceException;


	/**
	 * Find the comments related with post
	 **/
	public List<PostCommentInfo> getPostComments(ServiceContext svcctx,
												 InfoId<Long> postid,
												 String owner,
												 String state) throws ServiceException;

	public boolean newComment(ServiceContext svcctx,PostCommentInfo commentinfo) throws ServiceException;

	public static RowMapper<PostExt> POST_EXT_ROW_MAPPER = new RowMapper<PostExt>(){
		@Override
		public PostExt mapRow(ResultSet rs, int num) throws SQLException {
			PostExt info = new PostExt();

			info.setOwnerName(rs.getString("user_name"));
			info.setWorkgroupName(rs.getString("workgroup_name"));
			info.setSourceName(rs.getString("source_name"));

			return info;
		}
	};
}
