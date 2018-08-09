package com.dj.SpringBoot.Domain;

public class Watershed {
	private int id;
	private double width;
	private double area;
	private double slope;
	private double impermeate;
	
	public Watershed() {
		
	}
	public Watershed(int id, double width, double area, double slope, double impermeate) {
		this.id = id;
		this.width = width;
		this.area = area;
		this.slope = slope;
		this.impermeate = impermeate;
	}

	public double getWidth() {
		return width;
	}

	public void setWidth(double width) {
		this.width = width;
	}

	public double getArea() {
		return area;
	}

	public void setArea(double area) {
		this.area = area;
	}

	public double getSlope() {
		return slope;
	}

	public void setSlope(double slope) {
		this.slope = slope;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public double getImpermeate() {
		return impermeate;
	}
	public void setImpermeate(double impermeate) {
		this.impermeate = impermeate;
	}	
	
}
