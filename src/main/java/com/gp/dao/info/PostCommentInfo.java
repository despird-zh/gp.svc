package com.gp.dao.info;

import com.gp.info.TraceableInfo;

import java.util.Date;

/**
 * This class wrap the comment information, the comment might be anonymous or name.
 * anyway, the owner always be non-empty, the author might be [Anonymous].
 **/
public class PostCommentInfo extends TraceableInfo<Long> {
	
	private static final long serialVersionUID = 1L;

	private int sourceId;
	
	private Long workgroupId;
	
	private Long parentId;
	
	private Long postId;
	
	private String hashCode;
	
	private String author;
	
	private String owner;
	
	private String content;
	
	private String state;
	
	private Long owm;
	
	private Date commentDate;

	public Long getWorkgroupId() {
		return workgroupId;
	}

	public void setWorkgroupId(Long workgroupId) {
		this.workgroupId = workgroupId;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
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

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public Date getCommentDate() {
		return commentDate;
	}

	public void setCommentDate(Date commentDate) {
		this.commentDate = commentDate;
	}

	public Long getPostId() {
		return postId;
	}

	public void setPostId(Long postId) {
		this.postId = postId;
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
	
	
}
