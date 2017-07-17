package org.jumutang.caranswer.model;

import java.io.Serializable;
import java.util.Date;

/**
 * 活动
 * @author YuanYu
 *
 */
public class Activity extends BaseModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -210921663650205395L;

	//活动主键
	private String activityID;
	//活动名称
	private String name;
	//活动开始时间
	private Date startTime;
	//活动结束时间
	private Date endTime;
	//活动创建时间
	private Date createTime;
	//活动修改时间
	private Date updateTime;
	//活动删除时间
	private Date deleteTime;

	public String getActivityID() {
		return activityID;
	}

	public Activity setActivityID(String activityID) {
		this.activityID = activityID;
		return this;
	}

	public String getName() {
		return name;
	}

	public Activity setName(String name) {
		this.name = name;
		return this;
	}

	public Date getStartTime() {
		return startTime;
	}

	public Activity setStartTime(Date startTime) {
		this.startTime = startTime;
		return this;
	}

	public Date getEndTime() {
		return endTime;
	}

	public Activity setEndTime(Date endTime) {
		this.endTime = endTime;
		return this;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public Activity setCreateTime(Date createTime) {
		this.createTime = createTime;
		return this;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public Activity setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
		return this;
	}

	public Date getDeleteTime() {
		return deleteTime;
	}

	public Activity setDeleteTime(Date deleteTime) {
		this.deleteTime = deleteTime;
		return this;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
