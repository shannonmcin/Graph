//package com.gmail.shannonmcin.graphs;
//
//import java.awt.BasicStroke;
//import java.awt.Color;
//import java.awt.FontMetrics;
//import java.awt.Graphics;
//import java.awt.Graphics2D;
//import java.awt.RenderingHints;
//import java.awt.Stroke;
//
//import java.util.ArrayList;
//
//import javax.swing.BorderFactory;
//import javax.swing.JPanel;
//
//import test.DBConnection;
//
///**
// * @author Shannon McInnis
// *
// */
//public class TimeGraph extends Graph {
//
//	private ArrayList<Double> xList;
//	private ArrayList<Double> yList;
//
//	private Color lineColor;
//	private Color pointColor;
//	private boolean pointsVisible, conLinesOn, gridLinesOn;
//	private double xAxisInc, yAxisInc;
//	private double xMax, yMax;
//	private boolean xAxisDefault, yAxisDefault;
//
//	private Object id;
//
//	/**
//	 * Create the panel.
//	 */
//	public TimeGraph(Object ident) {
//		try {
//			DBConnection.addNewTimeGraph(ident);
//		} catch (IllegalArgumentException e) {
//			e.printStackTrace();
//		}
//		setID(ident);
//		setBorder(BorderFactory.createLineBorder(Color.BLACK));
//		setLineColor(Color.BLACK);
//		setPointColor(Color.BLACK);
//		setPointsVisible(true);
//		setConLinesOn(true);
//		setGridLinesOn(true);
//		setxAxisDefault(true);
//		setyAxisDefault(true);
//	}
//
//
//	@Override
//	public void paintComponent (Graphics g) {
//		super.paintComponent(g);
//		Graphics2D g2 = (Graphics2D) g;
//		drawAxes(g2);
//		drawPoints(g2);
//	}
//
//	private void drawDashedLine(Graphics g, int x1, int y1, int x2, int y2){
//		Graphics2D g2d = (Graphics2D) g.create(); 
//		Stroke dashed = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{4}, 0);
//		g2d.setStroke(dashed);
//		g2d.setColor(Color.LIGHT_GRAY);
//		g2d.drawLine(x1, y1, x2, y2);
//		g2d.dispose();
//	}
//
//	private void drawConnectingLine(Graphics g, int x1, int y1, int x2, int y2) {
//		Graphics2D g2d = (Graphics2D) g.create(); 
//		g2d.setColor(lineColor);
//		g2d.drawLine(x1, y1, x2, y2);
//		g2d.dispose();
//	}
//
//	private void drawCenteredCircle(Graphics g, int x, int y, int radius) {
//		Graphics2D g2 = (Graphics2D) g.create();
//		g2.setColor(pointColor);
//		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//		g2.fillOval(x-radius, y-radius, 2*radius, 2*radius);
//		g2.dispose();
//	}
//
//	private void drawAxes(Graphics2D g) {
//		Graphics2D g2 = (Graphics2D) g.create();
//		// create x and y axes 
//		g2.drawLine(BORDER_GAP, getHeight() - BORDER_GAP, BORDER_GAP, BORDER_GAP);
//		g2.drawLine(BORDER_GAP, getHeight() - BORDER_GAP, getWidth() - BORDER_GAP, getHeight() - BORDER_GAP);
//
//		System.out.println("current id is " + id + " and xmax is " + biggestValue(xList) + " and ymax is " + biggestValue(yList));
//		if(yList.size() != 0) {
//			if(yAxisDefault) {
//				if(biggestValue(yList) != 0) {
//					double whole = biggestValue(yList) / 50;
//					whole = Math.ceil(whole);
//					yAxisInc = whole;
//				} else {
//					yAxisInc = 1;
//				}
//			}
//
//			if(biggestValue(yList) == 0) {
//				yMax = 1;
//			} else {
//				yMax = smallestDivisible(Math.ceil(biggestValue(yList)), yAxisInc);
//			}
//
//			// create hatch marks for y axis. 
//			for (int i = 0; i <= yMax; i+=yAxisInc) {
//				int x0 = BORDER_GAP;
//				int x1 = 5 + BORDER_GAP;
//				int y0 = (int) (getHeight() - (((i) * (getHeight() - BORDER_GAP * 2)) / yMax + BORDER_GAP));
//				int y1 = y0;
//				g2.drawLine(x0, y0, x1, y1);
//				if((gridLinesOn) && (i != 0)) drawDashedLine(g2, x0 + 5, y0, getWidth() - BORDER_GAP, y0);
//				String yLabel = i + "";
//				FontMetrics metrics = g2.getFontMetrics();
//				int labelWidth = metrics.stringWidth(yLabel);
//				g2.drawString(yLabel, x0 - labelWidth - 5, y0 + (metrics.getHeight() / 2) - 3);
//			}
//		}
//
//		if(xList.size() > 0) {
//			// and for x axis
//			if(xList.size() != 0) {
//				if(xAxisDefault) {
//					if(biggestValue(xList) != 0) {
//						double whole = biggestValue(xList) / 50;
//						whole = Math.ceil(whole);
//						xAxisInc = whole;
//					} else {
//						xAxisInc = 1;
//					}
//				}
//			}
//
//			if(biggestValue(xList) == 0) {
//				xMax = 1;
//			} else {
//				xMax = smallestDivisible(Math.ceil(biggestValue(xList)), xAxisInc);
//			}
//
//			for (int i = 0; i <= xMax; i+=xAxisInc) {
//				int x0 = (int) (i * (getWidth() - BORDER_GAP * 2) / xMax + BORDER_GAP);
//				int x1 = x0;
//				int y0 = getHeight() - BORDER_GAP;
//				int y1 = y0 - 5;
//				g2.drawLine(x0, y0, x1, y1);
//				if((gridLinesOn) && (i != 0)) drawDashedLine(g2, x0, y0 + 5, x0, BORDER_GAP);
//				if(i != 0) {
//					String xLabel = i + "";
//					FontMetrics metrics = g2.getFontMetrics();
//					int labelWidth = metrics.stringWidth(xLabel);
//					g2.drawString(xLabel, x0 - labelWidth / 2, y0 + metrics.getHeight() + 3);
//				}
//			}
//		}
//		g2.dispose();
//		repaint();
//	}
//
//	private void drawPoints(Graphics2D g) {
//		Graphics2D g2 = (Graphics2D) g.create();
//		xList.sort(null);
//		yList.sort(null);
//		for(int i = 0; i <xList.size(); i++) {
//			int finX = (int) getXCoord(xList.get(i));
//			int finY = (int) getYCoord(yList.get(i));
//			if(pointsVisible) drawCenteredCircle(g2, finX, finY, 2);
//			if((i > 0) && (i < xList.size()) && (conLinesOn)) {
//				int initX = (int) getXCoord(xList.get(i-1));
//				int initY = (int) getYCoord(yList.get(i-1));
//				drawConnectingLine(g2, initX, initY, finX, finY);
//			}
//		}
//		g2.dispose();
//		repaint();
//	}
//
//	private double biggestValue(ArrayList<Double> yList2) {
//		double biggest = 0;
//		for(int i = 0; i <yList2.size(); i++) {
//			double currentDouble = yList2.get(i);
//			if (currentDouble > biggest) biggest = currentDouble;
//		}
//		return biggest;
//	}
//
//	private double smallestDivisible(double dividend, double divisor) {
//		double remain = (double) dividend % divisor;
//		double add = remain/2;
//		while(remain != 0) {
//			dividend += add;
//			remain = (double) dividend % divisor;
//		}
//		return dividend;
//	}
//
//	public void addPoint(double x, double y) {
//		DBConnection.addXYPoint(this.id, x, y);
////		xList = DBConnection.getXYxVals(this.id);
////		yList = DBConnection.getXYyVals(this.id);
//		repaint();
//	}
//
//	private double getXCoord(double graphX) {
//		double coord = -1;
//		if(graphX != 0) {
//			coord = graphX * (getWidth() - BORDER_GAP * 2) / xMax + BORDER_GAP;
//		} else {
//			coord = BORDER_GAP;
//		}
//		return coord;
//	}
//
//	private double getYCoord(double graphY) {
//		double coord = -1;
//		if(graphY >0) {
//			coord = getHeight() - (((graphY) * (getHeight() - BORDER_GAP * 2)) / yMax + BORDER_GAP);
//		} else {
//			coord = getHeight() - BORDER_GAP;
//		}
//		return coord;
//	}
//
//	//Getters and setters
//	/**
//	 * 
//	 * @param double - The x-coordinate of the point on the graph.
//	 * @return The x-coordinate of the point on the JPanel.
//	 */
//
//	public Color getLineColor() {
//		return lineColor;
//	}
//
//	public void setLineColor(Color lineColor) {
//		this.lineColor = lineColor;
//	}
//
//	public Color getPointColor() {
//		return pointColor;
//	}
//
//	public void setPointColor(Color pointColor) {
//		this.pointColor = pointColor;
//	}
//
//	public boolean isPointsVisible() {
//		return pointsVisible;
//	}
//
//	public void setPointsVisible(boolean pointsVisible) {
//		this.pointsVisible = pointsVisible;
//	}
//
//	public boolean isConLinesOn() {
//		return conLinesOn;
//	}
//
//	public void setConLinesOn(boolean linesOn) {
//		this.conLinesOn = linesOn;
//	}
//
//	public double getxAxisInc() {
//		return xAxisInc;
//	}
//
//	public void setxAxisInc(double xAxisInc) {
//		this.xAxisInc = xAxisInc;
//		this.xAxisDefault = false;
//	}
//
//	public double getyAxisInc() {
//		return yAxisInc;
//	}
//
//	public void setyAxisInc(double yAxisInc) {
//		this.yAxisInc = yAxisInc;
//		this.yAxisDefault = false;
//	}
//
//	public ArrayList<Double> getxList() {
//		return xList;
//	}
//
//	public ArrayList<Double> getyList() {
//		return yList;
//	}
//
//	public boolean isxAxisDefault() {
//		return xAxisDefault;
//	}
//
//	public void setxAxisDefault(boolean xAxisDefault) {
//		this.xAxisDefault = xAxisDefault;
//	}
//
//	public boolean isyAxisDefault() {
//		return yAxisDefault;
//	}
//
//	public void setyAxisDefault(boolean yAxisDefault) {
//		this.yAxisDefault = yAxisDefault;
//	}
//
//	public boolean isGridLinesOn() {
//		return gridLinesOn;
//	}
//
//	public void setGridLinesOn(boolean gridLinesOn) {
//		this.gridLinesOn = gridLinesOn;
//	}
//
//	public Object getID() {
//		return id;
//	}
//	
//	private void setID(Object newId) {
//		this.id = newId;
//	}
//	
//	private void setXList() {
//		
//	}
//
//}
