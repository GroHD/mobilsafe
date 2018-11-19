package com.itcasthd.mobilesafe.db.dao.domin;

import android.graphics.drawable.Drawable;

public class ProcessInfo {
	private String name;
	private Drawable icon;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Drawable getIcon() {
		return icon;
	}
	public void setIcon(Drawable icon) {
		this.icon = icon;
	}
	public long getMemSize() {
		return memSize;
	}
	public void setMemSize(long memSize) {
		this.memSize = memSize;
	}
	public boolean isCheck() {
		return isCheck;
	}
	public void setCheck(boolean isCheck) {
		this.isCheck = isCheck;
	}
	public boolean isSystem() {
		return isSystem;
	}
	public void setSystem(boolean isSystem) {
		this.isSystem = isSystem;
	}
	public String getPackageName() {
		return packageName;
	}
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	private long memSize;
	private boolean isCheck;
	private boolean isSystem;
	private String packageName;// 如果应用服务没有名称，则将其所在应用的包名作为名称
}
