package com.hbsd.rjxy.miaomiao.entity;

import java.util.Date;

/**
 * @Description  
 * @Author  
 * @Date 2020-04-22 
 */

public class MultiInfor {

	private Integer id;

	private Integer type;

	private Integer catId;

	private Integer userId;

	private String contentPath;

	private String multiInforContent;

	private Integer multiInforVisited;

	private Integer deleted;

	private String multiInforCommentCount;

	private String multiInforFormat;

	private int multiInforHot;

	private String multiInforAddress;

	private String multiInforCover;

	private Double multiInforRecommended;

	private String multiInforTag;

	private Date updateTime;

	private Date createTime;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getCatId() {
		return catId;
	}

	public void setCatId(Integer catId) {
		this.catId = catId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getContentPath() {
		return contentPath;
	}

	public void setContentPath(String contentPath) {
		this.contentPath = contentPath;
	}

	public String getMultiInforContent() {
		return multiInforContent;
	}

	public void setMultiInforContent(String multiInforContent) {
		this.multiInforContent = multiInforContent;
	}

	public Integer getMultiInforVisited() {
		return multiInforVisited;
	}

	public void setMultiInforVisited(Integer multiInforVisited) {
		this.multiInforVisited = multiInforVisited;
	}

	public Integer getDeleted() {
		return deleted;
	}

	public void setDeleted(Integer deleted) {
		this.deleted = deleted;
	}

	public String getMultiInforCommentCount() {
		return multiInforCommentCount;
	}

	public void setMultiInforCommentCount(String multiInforCommentCount) {
		this.multiInforCommentCount = multiInforCommentCount;
	}

	public String getMultiInforFormat() {
		return multiInforFormat;
	}

	public void setMultiInforFormat(String multiInforFormat) {
		this.multiInforFormat = multiInforFormat;
	}

	public int getMultiInforHot() {
		return multiInforHot;
	}

	public void setMultiInforHot(int multiInforHot) {
		this.multiInforHot = multiInforHot;
	}

	public String getMultiInforAddress() {
		return multiInforAddress;
	}

	public void setMultiInforAddress(String multiInforAddress) {
		this.multiInforAddress = multiInforAddress;
	}

	public String getMultiInforCover() {
		return multiInforCover;
	}

	public void setMultiInforCover(String multiInforCover) {
		this.multiInforCover = multiInforCover;
	}

	public Double getMultiInforRecommended() {
		return multiInforRecommended;
	}

	public void setMultiInforRecommended(Double multiInforRecommended) {
		this.multiInforRecommended = multiInforRecommended;
	}

	public String getMultiInforTag() {
		return multiInforTag;
	}

	public void setMultiInforTag(String multiInforTag) {
		this.multiInforTag = multiInforTag;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
}
