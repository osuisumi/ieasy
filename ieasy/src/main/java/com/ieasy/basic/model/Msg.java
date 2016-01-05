package com.ieasy.basic.model;

public class Msg {

	/** 是否成功 */
	private boolean status = false;
	
	/** 提示信息 */
	private String msg = "";
	
	/** 其他信息 */
	private Object obj = null;
	
	public Msg() {}
	public Msg(boolean status) {
		this.status = status ;
	}
	public Msg(boolean status, String msg) {
		this.status = status ;
		this.msg = msg ;
	}
	public Msg(boolean status, String msg, Object obj) {
		this.status = status ;
		this.msg = msg ;
		this.obj = obj ;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Object getObj() {
		return obj;
	}

	public void setObj(Object obj) {
		this.obj = obj;
	}
	
}
