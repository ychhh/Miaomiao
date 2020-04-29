package com.hbsd.rjxy.miaomiao.entity;

import java.util.Date;

/**
 * @Description  
 * @Author  
 * @Date 2020-04-22 
 */

public class SubscriptionRecord {

	private Integer id;

	private Integer catId;

	private Integer userId;

	private Integer deleted;//0 表示不关注 1表示关注

	private Integer version;

	private Date createTime;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public Integer getDeleted() {
		return deleted;
	}

	public void setDeleted(Integer deleted) {
		this.deleted = deleted;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
}
