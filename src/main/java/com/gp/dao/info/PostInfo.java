package com.gp.dao.info;

import com.gp.info.TraceableInfo;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.util.Date;

public class PostInfo extends TraceableInfo<Long> {
	
	private static final long serialVersionUID = 1L;

	private int sourceId;

	@NotNull
	private Long workgroupId;

	private Long memberGroupId;

	private String hashCode;

	@NotEmpty
	private String owner;

	@NotEmpty
	private String content;
	
	private String excerpt;

	@NotEmpty
	private String subject;

	@NotEmpty
	private String scope ;

	@NotEmpty
	private String state;
	
	private Long owm;
	
	private boolean commentOn;

	@NotEmpty
	private String postType;
	
	private int commentCount;
	
	private int upvoteCount;
	
	private Date postDate;

	@NotEmpty
	private String classification;

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	private int priority;

	public String getClassification() {
		return classification;
	}

	public void setClassification(String classification) {
		this.classification = classification;
	}

	public Long getWorkgroupId() {
		return workgroupId;
	}

	public void setWorkgroupId(Long workgroupId) {
		this.workgroupId = workgroupId;
	}

	public Long getMemberGroupId() {
		return memberGroupId;
	}

	public void setMemberGroupId(Long memberGroupId) {
		this.memberGroupId = memberGroupId;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getExcerpt() {
		return excerpt;
	}

	public void setExcerpt(String excerpt) {
		this.excerpt = excerpt;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public boolean isCommentOn() {
		return commentOn;
	}

	public void setCommentOn(boolean commentOn) {
		this.commentOn = commentOn;
	}

	public String getPostType() {
		return postType;
	}

	public void setPostType(String postType) {
		this.postType = postType;
	}

	public int getCommentCount() {
		return commentCount;
	}

	public void setCommentCount(int commentCount) {
		this.commentCount = commentCount;
	}

	public int getUpvoteCount() {
		return upvoteCount;
	}

	public void setUpvoteCount(int upvoteCount) {
		this.upvoteCount = upvoteCount;
	}

	public Date getPostDate() {
		return postDate;
	}

	public void setPostDate(Date postDate) {
		this.postDate = postDate;
	}

	public String getHashCode() {
		return hashCode;
	}

	public void setHashCode(String hashCode) {
		this.hashCode = hashCode;
	}

	public Long getOwm() {
		return owm;
	}

	public void setOwm(Long owm) {
		this.owm = owm;
	}

	public int getSourceId() {
		return sourceId;
	}

	public void setSourceId(int sourceId) {
		this.sourceId = sourceId;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}
	
	
}
