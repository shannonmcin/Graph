/**
 * 
 */
package com.gmail.shannonmcin.utils;

/**
 * @author Shannon McInnis
 *
 */
public class GraphPoint implements Comparable<GraphPoint> {

	private String date;
	private Double x;
	private Double y;

	public GraphPoint(double x, double y) {
		this.date=null;
		this.x = x;
		this.y = y;
	}

	public GraphPoint(String date, double y) {
		this.date = date;
		this.y = y;
		this.x = null;
	}

	public String getDate() {
		return this.date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public double getX() {
		return this.x;
	}
	public void setX(double x) {
		this.x = x;
	}
	public double getY() {
		return this.y;
	}
	public void setY(double y) {
		this.y = y;
	}

	@Override
	public String toString() {
		String point = null;
		if(this.x != null) {
			point = "(" + this.getX() + ", " + this.getY() + ")";
		} else {
			point = "(" + this.getDate() + ", " + this.getY() + ")";
		}
		return point;
	}

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(GraphPoint other) {
		// TODO Auto-generated method stub
		double compareX = other.getX();

		if (this.getX() > compareX) return 1;
		else if(this.getX() < compareX) return -1;
		else return 0;
	}
}
