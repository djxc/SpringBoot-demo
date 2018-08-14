package com.dj.SpringBoot.Domain;

import java.io.Serializable;
import java.util.List;

public class Watershed implements Serializable {
	private static final long serialVersionUID = 6392376146163510146L;
	private int id;
	private double width;
	private double area;
	private double slope;
	private double impermeate;
	private double rainflow;
	private double lwd;
	private double xcl;
	private double yssd;
	private double tspz;
	private double xsc;
	private List<Double> runoff;
	
	public Watershed() {
		
	}
	public Watershed(int id, double width, double area, double slope, double impermeate) {
		this.id = id;
		this.width = width;
		this.area = area;
		this.slope = slope;
		this.impermeate = impermeate;
	}
	
	public Watershed(int id, double width, double area, double slope, double impermeate,
			double lwd, double xcl, double yssd, double tspz, double xsc) {
		this.id = id;
		this.width = width;
		this.area = area;
		this.slope = slope;
		this.impermeate = impermeate;
		this.lwd = lwd;
		this.xcl = xcl;
		this.yssd = yssd;
		this.tspz = tspz;
		this.xsc = xsc;
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
	public double getRainflow() {
		return rainflow;
	}
	public void setRainflow(double rainflow) {
		this.rainflow = rainflow;
	}
	public double getLwd() {
		return lwd;
	}
	public void setLwd(double lwd) {
		this.lwd = lwd;
	}
	public double getXcl() {
		return xcl;
	}
	public void setXcl(double xcl) {
		this.xcl = xcl;
	}
	public double getYssd() {
		return yssd;
	}
	public void setYssd(double yssd) {
		this.yssd = yssd;
	}
	public double getTspz() {
		return tspz;
	}
	public void setTspz(double tspz) {
		this.tspz = tspz;
	}
	public double getXsc() {
		return xsc;
	}
	public void setXsc(double xsc) {
		this.xsc = xsc;
	}
	public List<Double> getRunoff() {
		return runoff;
	}
	public void setRunoff(List<Double> runoff) {
		this.runoff = runoff;
	}	
	
	
}
