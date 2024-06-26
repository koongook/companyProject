package com.com.com.project;

import java.sql.Timestamp;

public class EPHistoryVO {
	
	private int h_seq;
	private Timestamp reg_date;
	private String name;
	private String wait;
	
	public int getH_seq() {
		return h_seq;
	}
	public void setH_seq(int h_seq) {
		this.h_seq = h_seq;
	}
	public Timestamp getReg_date() {
		return reg_date;
	}
	public void setReg_date(Timestamp reg_date) {
		this.reg_date = reg_date;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getWait() {
		return wait;
	}
	public void setWait(String wait) {
		this.wait = wait;
	}
	

}
