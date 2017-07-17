package org.jumutang.caranswer.model;

import java.io.Serializable;
/**
 * 用户来源
 * @author YuanYu
 *
 */
public class Source extends BaseModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6112198594141524925L;

	//来源主键
	private String sourceID;
	//来源名称
	private String name;

	public String getSourceID() {
		return sourceID;
	}

	public Source setSourceID(String sourceID) {
		this.sourceID = sourceID;
		return this;
	}

	public String getName() {
		return name;
	}

	public Source setName(String name) {
		this.name = name;
		return this;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
