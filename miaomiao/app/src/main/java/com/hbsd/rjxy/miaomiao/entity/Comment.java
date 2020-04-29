package com.hbsd.rjxy.miaomiao.entity;

import java.util.Date;

/**
 * @Description  
 * @Author  
 * @Date 2020-04-22 
 */


public class Comment {


	private Integer id;

	private Integer multiInfoId;

	private Integer commentLike;

	private Integer userId;

	private Integer version;

	private Integer deleted;

	private Date createTime;

	private Date updateTime;

   	private String commentContent;

	private String userHead;

	private String userName;

   	public void setCreateTime(String creatTime){
   		this.createTime=createTime;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getMultiInfoId() {
		return multiInfoId;
	}

	public void setMultiInfoId(Integer multiInfoId) {
		this.multiInfoId = multiInfoId;
	}

	public Integer getCommentLike() {
		return commentLike;
	}

	public void setCommentLike(Integer commentLike) {
		this.commentLike = commentLike;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public Integer getDeleted() {
		return deleted;
	}

	public void setDeleted(Integer deleted) {
		this.deleted = deleted;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getCommentContent() {
		return commentContent;
	}

	public void setCommentContent(String commentContent) {
		this.commentContent = commentContent;
	}

	public String getUserHead() {
		return userHead;
	}

	public void setUserHead(String userHead) {
		this.userHead = userHead;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
}
