package com.hbsd.rjxy.miaomiao.entity;

import java.util.Date;

/**
 * @Description  
 * @Author  
 * @Date 2020-04-22 
 */


public class Cat {


	private Integer id;

	private Integer userId;

	private Integer cityId;

	private String catName;

	private Date catBirthday;

	private String catSex;

	private String breedId;

	private Double catWeight;

	private String catToy;

	private String catHead;

	private int catHot;

	private String catIntro;

	private String catSource;

	private String catFood;

	private Integer isSte;

	private Integer version;

	private Integer deleted;

	private Date createTime;

	private Date updateTime;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getCityId() {
		return cityId;
	}

	public void setCityId(Integer cityId) {
		this.cityId = cityId;
	}

	public String getCatName() {
		return catName;
	}

	public void setCatName(String catName) {
		this.catName = catName;
	}

	public Date getCatBirthday() {
		return catBirthday;
	}

	public void setCatBirthday(Date catBirthday) {
		this.catBirthday = catBirthday;
	}

	public String getCatSex() {
		return catSex;
	}

	public void setCatSex(String catSex) {
		this.catSex = catSex;
	}

	public String getBreedId() {
		return breedId;
	}

	public void setBreedId(String breedId) {
		this.breedId = breedId;
	}

	public Double getCatWeight() {
		return catWeight;
	}

	public void setCatWeight(Double catWeight) {
		this.catWeight = catWeight;
	}

	public String getCatToy() {
		return catToy;
	}

	public void setCatToy(String catToy) {
		this.catToy = catToy;
	}

	public String getCatHead() {
		return catHead;
	}

	public void setCatHead(String catHead) {
		this.catHead = catHead;
	}

	public int getCatHot() {
		return catHot;
	}

	public void setCatHot(int catHot) {
		this.catHot = catHot;
	}

	public String getCatIntro() {
		return catIntro;
	}

	public void setCatIntro(String catIntro) {
		this.catIntro = catIntro;
	}

	public String getCatSource() {
		return catSource;
	}

	public void setCatSource(String catSource) {
		this.catSource = catSource;
	}

	public String getCatFood() {
		return catFood;
	}

	public void setCatFood(String catFood) {
		this.catFood = catFood;
	}

	public Integer getIsSte() {
		return isSte;
	}

	public void setIsSte(Integer isSte) {
		this.isSte = isSte;
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
}
