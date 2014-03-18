package com.san.api.easydb.example;

import com.san.api.easydb.ConnectionManager;
import com.san.api.easydb.Entity;

public class MyEntityClass extends Entity {

	//No need of ID field. Implicitly its available.
	
	private String title;
	private String subTitle;
	private String tag;
	
	static{
		ConnectionManager.registerEntity(MyEntityClass.class);
	}

	
	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	/**
	 * @return the subTitle
	 */
	public String getSubTitle() {
		return subTitle;
	}
	/**
	 * @param subTitle the subTitle to set
	 */
	public void setSubTitle(String subTitle) {
		this.subTitle = subTitle;
	}
	/**
	 * @return the tag
	 */
	public String getTag() {
		return tag;
	}
	/**
	 * @param tag the tag to set
	 */
	public void setTag(String tag) {
		this.tag = tag;
	}
	
}
