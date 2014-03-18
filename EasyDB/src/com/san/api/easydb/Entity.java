package com.san.api.easydb;

import java.io.Serializable;

@SuppressWarnings("serial")
public abstract class Entity implements Serializable{
	protected int id;

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
}
