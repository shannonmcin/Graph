package com.gmail.shannonmcin.graphs;

import java.awt.Graphics;
import java.awt.Graphics2D;

import java.util.ArrayList;

import com.gmail.shannonmcin.utils.GraphPoint;

import test.DBConnection;

/**
 * @author Shannon McInnis
 *
 */
public class XYGraph extends Graph {

	/**
	 * Create the panel.
	 */
	public XYGraph(Object ident) {
		super(ident);
		if(ident != null) {
			pointList = DBConnection.getXYPoints(ident);
		} else {
			throw new IllegalArgumentException ("You cannot create an XYGraph with a null ID.");
		}
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g.create();
		drawPoints(g2);
	}

	@Override
	protected void addGraph(Object ID) {
		DBConnection.addNewXYGraph(ID);
	}

	@Override
	public void addPoint(GraphPoint point) {
		System.out.println("point being added is " + point);
		DBConnection.addXYPoint(ID, point);
		pointList = DBConnection.getXYPoints(ID);
		if(xAxisDefault) {
			DBConnection.setXMax(ID, biggestXValue(pointList));
			if(smallestXValue(pointList) >= 0) DBConnection.setXMin(ID,  0);
			else DBConnection.setXMin(ID, smallestXValue(pointList));
		}
		if(yAxisDefault) {
			DBConnection.setYMax(ID, biggestYValue(pointList));
			if(smallestYValue(pointList) >= 0) DBConnection.setYMin(ID, 0);
			else DBConnection.setYMin(ID, smallestYValue(pointList));
		}
		
		xMax = DBConnection.getXMax(ID);
		xMin = DBConnection.getXMin(ID);
		yMax = DBConnection.getYMax(ID);
		yMin = DBConnection.getYMin(ID);
		repaint();
	}

	@Override
	public void removePoint(GraphPoint point) {
		DBConnection.deleteXYPoint(ID, point);
		pointList = DBConnection.getXYPoints(ID);
		if(xAxisDefault) {
			DBConnection.setXMax(ID, biggestXValue(pointList));
			if(smallestXValue(pointList) >= 0) DBConnection.setXMin(ID,  0);
			else DBConnection.setXMin(ID, smallestXValue(pointList));
		}
		if(yAxisDefault) {
			System.out.println("biggest y value is " + biggestYValue(pointList));
			DBConnection.setYMax(ID, biggestYValue(pointList));
			if(smallestYValue(pointList) >= 0) DBConnection.setYMin(ID, 0);
			else DBConnection.setYMin(ID, smallestYValue(pointList));
		}
		
		xMax = DBConnection.getXMax(ID);
		xMin = DBConnection.getXMin(ID);
		yMax = DBConnection.getYMax(ID);
		yMin = DBConnection.getYMin(ID);
		repaint();
	}

	@Override
	protected void drawAxes(Graphics2D g) {
		super.drawAxes(g);
		getXData();
		getYData();
		super.drawXHatchMarks(g, xMax, xAxisInc);
		super.drawYHatchMarks(g, yMax, yAxisInc);
	}

	private void drawPoints(Graphics2D g) {
		Graphics2D g2 = (Graphics2D) g.create();
		g2.setColor(pointColor);
		pointList.sort(null);
		for(int i = 0; i < pointList.size(); i++) {
			int finX = (int) getXCoord(pointList.get(i).getX());
			int finY = (int) getYCoord(pointList.get(i).getY());
			if((i > 0) && (i < pointList.size()) && (conLinesVisible)) {
				int initX = (int) getXCoord(pointList.get(i-1).getX());
				int initY = (int) getYCoord(pointList.get(i-1).getY());
				drawConnectingLine(g2, initX, initY, finX, finY);
			}
			if(pointsVisible) drawCenteredCircle(g2, finX, finY, 2);
		}
		repaint();
	}

	private  void getXData() {
		if(pointList.size() > 0) {
			if(xAxisDefault) {
				xAxisInc = getDefaultXInc();
			} else  {
				xAxisInc = DBConnection.getXAxisInc(ID);
			}

			if(biggestXValue(pointList) == 0) {
				xMax = 1;
			} else {
				xMax = smallestDivisible(Math.ceil(biggestXValue(pointList)), xAxisInc);
			}
		} else {
			xAxisInc = 0;
			xMax = 0;
		}
	}

	private  void getYData() {
		if(pointList.size() > 0) {
			if(yAxisDefault) {
				yAxisInc = getDefaultYInc();
			} else {
				yAxisInc = DBConnection.getYAxisInc(ID);
			}

			if(biggestYValue(pointList) == 0) {
				yMax = 1;
			} else {
				yMax = smallestDivisible(Math.ceil(biggestYValue(pointList)), yAxisInc);
			}
		} else {
			yAxisInc = 0;
			yMax = 0;
		}
	}

	@Override
	public double getDefaultXInc() {
		double inc = 1;
		if(biggestXValue(pointList) != 0) {
			double whole = biggestXValue(pointList) / 50;
			inc = Math.ceil(whole);
		}
		return inc;
	}

	@Override
	public double getDefaultYInc() {
		double inc = 1;
		if(biggestYValue(pointList) != 0) {
			double whole = biggestYValue(pointList) / 50;
			inc = Math.ceil(whole);
		}
		return inc;
	}

	private  double smallestDivisible(double dividend, double divisor) {
		double remain = (double) dividend % divisor;
		double add = remain/2;
		while(remain != 0) {
			dividend += add;
			remain = (double) dividend % divisor;
		}
		return dividend;
	}

	private double getXCoord(double graphX) {
		double coord = -1;
		if(graphX != 0) {
			coord = BORDER_GAP + graphX * ((getWidth() - BORDER_GAP * 2) / xMax);
		} else {
			coord = BORDER_GAP;
		}
		return coord;
	}

	private double getYCoord(double graphY) {
		double coord = -1;
		if(graphY >0) {
			coord = (getHeight() - BORDER_GAP) - (graphY * ((getHeight() - BORDER_GAP * 2)/yMax));
		} else {
			coord = getHeight() - BORDER_GAP;
		}
		return coord;
	}

	//GETTERS AND SETTERS

	public void setID(Object newId) {
		Object currentID = XYGraph.ID;
		XYGraph.ID = newId;
		//		DBConnection.setID(currentID, newId);
	}
}
