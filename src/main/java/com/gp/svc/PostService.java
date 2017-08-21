package com.gp.svc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.gp.common.IdKey;
import com.gp.common.IdKeys;
import com.gp.common.ServiceContext;
import com.gp.dao.info.PostCommentInfo;
import com.gp.exception.ServiceException;
import com.gp.info.InfoId;
import com.gp.dao.info.PostInfo;
import com.gp.pagination.PageQuery;
import com.gp.pagination.PageWrapper;
import com.gp.svc.info.PostExt;
import com.gp.svc.info.UserLiteInfo;
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
	public List<UserLiteInfo> getPostAttendees(ServiceContext svcctx, InfoId<Long> postKey) throws ServiceException;

	/**
	 * Find the combined post base and ext information of personal
	 * @param account the user account
	 * @param state the state of post
	 * @param type the type of post
	 * @param scope the scope of post
	 **/
	public PageWrapper<PostExt> getPersonalPosts(ServiceContext svcctx,
																		String account,
																		String state,
																		String type,
																		String scope,
																		PageQuery pageQuery) throws ServiceException;

	/**
	 * Find the combined post base and ext information of personal
	 * @param account the user account
	 * @param state the state of post
	 * @param type the type of post
	 * @param scope the scope of post
	 **/
	public List<PostExt> getPersonalJoinedPosts(ServiceContext svcctx,
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
	 * @param mode the data query mode :  ALL/MEMBER/SQUARE
	 **/
	public PageWrapper<PostExt> getWorkgroupPosts(ServiceContext svcctx,
																 InfoId<Long> wid,
																 String mode,
																 String state,
																 String type,
																 PageQuery pageQuery) throws ServiceException;

	/**
	 * Find the combined post data(base and ext) to be presented in square
	 * @param state the state of post
	 * @param type the type of post
	 * @param scope the scope of post
	 **/
	public PageWrapper<PostExt> getSquarePosts(ServiceContext svcctx,
																  String state,
																  String type,
																  String scope,
																  PageQuery pageQuery) throws ServiceException;

	/**
	 * Find the comments related with post
	 *
	 * @param postid the id of post
	 * @param owner the owner of post
	 * @param state the state of post
	 **/
	public List<PostCommentInfo> getPostComments(ServiceContext svcctx,
												 InfoId<Long> postid,
												 String owner,
												 String state) throws ServiceException;

	/**
	 * create post comment information
	 **/
	public boolean newComment(ServiceContext svcctx, PostCommentInfo commentinfo) throws ServiceException;

	/**
	 * Add like to post
	 * @return the like vote count
	 **/
	public int addPostLike(ServiceContext svcctx, InfoId<Long> postId, String voter) throws ServiceException;

	/**
	 * Remove like of post
	 * @return the like vote count
	 **/
	public int addPostDislike(ServiceContext svcctx, InfoId<Long> postId, String voter) throws ServiceException;

	/**
	 * public the post 
	 **/
	public boolean publicPost(ServiceContext svcctx, InfoId<Long> postId) throws ServiceException;
	
	/**
	 * Mapper for post extend information
	 **/
	public static RowMapper<PostExt> POST_EXT_ROW_MAPPER = new RowMapper<PostExt>(){
		@Override
		public PostExt mapRow(ResultSet rs, int num) throws SQLException {
			PostExt info = new PostExt();

			InfoId<Long> id = IdKeys.getInfoId(IdKey.POST, rs.getLong("post_id"));
			
			info.setInfoId(id);
			info.setSourceId(rs.getInt("source_id"));
			info.setWorkgroupId(rs.getLong("workgroup_id"));
			info.setOwner(rs.getString("owner"));
			info.setContent(rs.getString("content"));
			info.setExcerpt(rs.getString("excerpt"));
			info.setSubject(rs.getString("subject"));
			info.setState(rs.getString("state"));
			info.setScope(rs.getString("scope"));
			info.setCommentOn(rs.getBoolean("comment_on"));
			info.setPostType(rs.getString("post_type"));
			info.setCommentCount(rs.getInt("comment_count"));
			info.setUpvoteCount(rs.getInt("upvote_count"));
			info.setPostDate(rs.getDate("post_time"));
			info.setHashCode(rs.getString("hash_code"));
			info.setOwm(rs.getLong("owm"));
			info.setPriority(rs.getInt("priority"));
			info.setClassification(rs.getString("classification"));

			info.setModifier(rs.getString("modifier"));
			info.setModifyDate(rs.getTimestamp("last_modified"));
			
			info.setOwnerName(rs.getString("user_name"));
			info.setWorkgroupName(rs.getString("workgroup_name"));
			info.setSourceName(rs.getString("source_name"));

			return info;
		}
	};

	/**
	 * Remove the post 
	 **/
	public int removePost(ServiceContext svcctx, InfoId<Long> postid) throws ServiceException;
}
