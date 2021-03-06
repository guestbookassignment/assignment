package com.guestbook.entity;

import java.util.Date;

import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Embeddable
public class Audit implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdDate;
	private String createdUser;
	@Temporal(TemporalType.TIMESTAMP)
	private Date updatedDate;
	private String updatedUser;
	public Date getCreatedDate() {
		return createdDate;
	}
	public String getCreatedUser() {
		return createdUser;
	}
	public Date getUpdatedDate() {
		return updatedDate;
	}
	public String getUpdatedUser() {
		return updatedUser;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public void setCreatedUser(String createdUser) {
		this.createdUser = createdUser;
	}
	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}
	public void setUpdatedUser(String updatedUser) {
		this.updatedUser = updatedUser;
	}
}
