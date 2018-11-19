package com.itcasthd.mobilesafe.Utils;

public class PointDouble {
	double x, y;

	public PointDouble(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public double getX() {
		return x;
		
	}
	
	public double getY() {
		
		return y;
	}
	public String toString() {
		return "x=" + x + ", y=" + y;
	}
}
