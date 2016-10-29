/**
 * 
 */
package com.gmail.shannonmcin.graphs;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import com.gmail.shannonmcin.main.Application;
import com.gmail.shannonmcin.utils.GraphPoint;

import test.DBConnection;
import java.awt.event.MouseAdapter;

/**
 * @author Shannon McInnis
 *
 */
public class Graph extends JPanel {
	private static final long serialVersionUID = 1L;
	
	
	protected static ArrayList<GraphPoint> pointList;
	protected static GraphPoint matchPoint = null, clickedPoint = null;

	protected static final int BORDER_GAP = 30; 

	protected static Object ID;
	protected static boolean pointsVisible, conLinesVisible, gridLinesVisible;
	protected static Color lineColor, pointColor;
	protected static double xAxisInc;
	protected static double yAxisInc;
	protected static double xMax, xMin, yMax, yMin;
	protected static  boolean xAxisDefault;
	protected static boolean yAxisDefault;

	protected static boolean pointClicked;
	protected static boolean mouseMatch;
	protected static boolean aMatch;
	protected static int mouseX;
	protected static int mouseY;
	protected static double panelX;
	protected static double panelY;


	/**
	 * Create the panel.
	 */

	public Graph() {
	}

	public Graph(Object ID) {
		if(ID != null) {
			try {
				addGraph(ID);
			} catch (IllegalArgumentException e) {

			}
			DBConnection.setLastGraph(ID);
			Graph.ID = ID;
			setBorder(BorderFactory.createLineBorder(Color.BLACK));
			lineColor = DBConnection.getLineColor(ID);
			pointColor = DBConnection.getPointColor(ID);
			pointsVisible = DBConnection.isPointsVisible(ID);
			conLinesVisible = DBConnection.isConLinesVisible(ID);
			gridLinesVisible = DBConnection.isGridLinesVisible(ID);
			xAxisDefault = DBConnection.isXAxisDefault(ID);
			yAxisDefault = DBConnection.isYAxisDefault(ID);
			xMax = DBConnection.getXMax(ID);
			xMin = DBConnection.getXMin(ID);
			yMax = DBConnection.getYMax(ID);
			yMin = DBConnection.getYMin(ID);

			addMouseMotionListener(new MouseMotionListener() {
				@Override
				public void mouseMoved(MouseEvent e) {
					aMatch = false;
					for(int i =0; i<pointList.size(); i++) {
						mouseX = e.getX();
						mouseY = e.getY();
						panelX = getPanelXCoord(pointList.get(i).getX());
						panelY  = getPanelYCoord(pointList.get(i).getY());
						if((mouseX - panelX < 5) && (mouseX - panelX > -5)) {
							if((mouseY - panelY < 5) && (mouseY - panelY > -5)) {
								aMatch = true;
								matchPoint = pointList.get(i);
								repaint();
								break;
							}
						}
					}
					if(!aMatch) matchPoint = null;
				}

				@Override
				public void mouseDragged(MouseEvent e) {
					// TODO Auto-generated method stub
				}
			});
			
			addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					for(int i =0; i<pointList.size(); i++) {
						mouseX = e.getX();
						mouseY = e.getY();
						panelX = getPanelXCoord(pointList.get(i).getX());
						panelY  = getPanelYCoord(pointList.get(i).getY());
						if((mouseX - panelX < 5) && (mouseX - panelX > -5)) {
							if((mouseY - panelY < 5) && (mouseY - panelY > -5)) {
								aMatch = true;
								Application.setSelectedItem(pointList.get(i).toString());
								Application.setDeleteBtnEnabled(true);
								break;
							}
						}
					}
				}
			});
		} else {
			throw new IllegalArgumentException("A graph with a null ID cannot be created.");
		}
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g.create();
		drawAxes(g2);
		selectPoint(g2, matchPoint);
		selectPoint(g2, clickedPoint);
	}

	protected void addGraph(Object ID) {
		//TODO do stuff
	}

	protected void drawXHatchMarks(Graphics2D g, double xMax, double xInc) {
		Graphics2D g2 = (Graphics2D) g.create();
		g2.setFont(new Font("timesroman", Font.PLAIN, 12));
		if(xInc != 0) {
			for (double i = 0/*smallestXValue(pointList)*/; i <= xMax+xMin; i+=xInc) {
				if(i!=0) {
//					int x0 = (int) Math.ceil(getPanelXCoord(i));
					int x0 = (int) Math.ceil(getPanelXCoord(i));
					int x1 = x0;
					int y0 = getHeight() - BORDER_GAP;
					int y1 = y0-5;
					g2.drawLine(x0, y0, x1, y1);
					if((gridLinesVisible) && (i != 0)) drawDashedLine(g2, x0, y1, x0, BORDER_GAP);
					double current = pointList.get(0).getX() + (i-xInc);
					String xLabel = current + "";
					FontMetrics metrics = g2.getFontMetrics();
					int labelWidth = metrics.stringWidth(xLabel);
					g2.drawString(xLabel, x0 - labelWidth/2 + 1, y0 + metrics.getHeight());
				}
			}
		}
		g2.dispose();
	}

	protected void drawYHatchMarks(Graphics2D g, double yMax, double yInc) {
		Graphics2D g2 = (Graphics2D) g.create();
		g2.setFont(new Font("timesroman", Font.PLAIN, 12));
		System.out.println("yinc is " + yInc);
		if(yInc != 0) {
			for (double i = smallestYValue(pointList); i <= yMax; i+=yInc) {
				System.out.println("i is " + i);
				int x0 = BORDER_GAP;
				int x1 = 5 + BORDER_GAP;
				
				int y0 = (int) Math.ceil(getPanelYCoord(i));
				int y1 = y0;
				System.out.println("y0, y1, x0, x1: " + y0 + " " + y1 + " " + x0 + " " + x1);
				g2.drawLine(x0, y0, x1, y1);
				if((gridLinesVisible) && (i != 0)) drawDashedLine(g2, x0 + 5, y0, getWidth() - BORDER_GAP, y0);
				double current = pointList.get(0).getY() + (i-yInc);
				String yLabel = current + "";
				FontMetrics metrics = g2.getFontMetrics();
				int labelWidth = metrics.stringWidth(yLabel);
				g2.drawString(yLabel, x0 - labelWidth - 5, y0 + (metrics.getHeight() / 2) - 3);
			}
		}
		g2.dispose();
	}

	private double getPanelXCoord(double graphX) {
		double numDivisions = (xMax - xMin) / xAxisInc;
		double coord = BORDER_GAP + graphX * ((getWidth() - BORDER_GAP * 2) / numDivisions);
		return coord;
	}

	private double getGraphXCoord(double panelX) {
		double coord = -1;
		if((panelX <= getWidth()-BORDER_GAP*2) && (panelX >= BORDER_GAP)) {
			coord = (panelX - BORDER_GAP) / ((getWidth()-BORDER_GAP*2) / xMax);
		}
		return coord;
	}

	private double getPanelYCoord(double graphY) {
		//TODO fix calculations for here and panelxcoord to work with negative numbers
		pointList.sort(null);
		int place = pointList.indexOf(graphY);
		double coord = (getHeight() - BORDER_GAP) - (Math.abs(graphY)/*place*/ * ((getHeight() - BORDER_GAP * 2) / (yMax - yMin)));
		return coord;
	}

	private double getGraphYCoord(double panelY) {
		double coord = -1;
		if((panelY <=getHeight() - BORDER_GAP*2) && (panelY >= BORDER_GAP)) {
			coord = -((panelY-getHeight() + BORDER_GAP)/((getHeight()-BORDER_GAP*2)/yMax));
		}
		return coord;
	}
	
	protected double biggestXValue(ArrayList<GraphPoint> pointList) {
		double biggest = 0;
		for(int i = 0; i <pointList.size(); i++) {
			double currentDouble = pointList.get(i).getX();
			if (currentDouble > biggest) biggest = currentDouble;
		}
		return biggest;
	}
	
	protected double smallestXValue(ArrayList<GraphPoint> pointList) {
		double smallest = 0;
		for(int i = 0; i <pointList.size(); i++) {
			double currentDouble = pointList.get(i).getX();
			if (currentDouble < smallest) smallest = currentDouble;
		}
		return smallest;
	}

	protected  double biggestYValue(ArrayList<GraphPoint> pointList) {
		double biggest = 0;
		for(int i = 0; i < pointList.size(); i++) {
			double currentDouble = pointList.get(i).getY();
			if (currentDouble > biggest) biggest = currentDouble;
		}
		return biggest;
	}
	
	protected double smallestYValue(ArrayList<GraphPoint> pointList) {
		double smallest = 0;
		for(int i = 0; i <pointList.size(); i++) {
			double currentDouble = pointList.get(i).getY();
			if (currentDouble < smallest) smallest = currentDouble;
		}
		System.out.println("the smallest y value is " + smallest);
		return smallest;
	}


	protected void drawAxes(Graphics2D g) {
		Graphics2D g2 = (Graphics2D) g.create();
		String title = ID.toString();
		Font currentFont = g.getFont();
		Font titleFont = currentFont.deriveFont(currentFont.getSize() * 1.4F);
		g2.setFont(titleFont);
		FontMetrics metric = g2.getFontMetrics();
		int titleWidth = metric.stringWidth(title);
		int titleHeight = metric.getHeight();
		g2.drawString(title, (getWidth() - titleWidth)/2, titleHeight);
		g2.setFont(currentFont);

		// create x and y axes 
		g2.drawLine(BORDER_GAP, getHeight() - BORDER_GAP, BORDER_GAP, BORDER_GAP);
		g2.drawLine(BORDER_GAP, getHeight() - BORDER_GAP, getWidth() - BORDER_GAP, getHeight() - BORDER_GAP);

		g2.dispose();
		repaint();
	}

	protected  void drawDashedLine(Graphics2D g, int x1, int y1, int x2, int y2){
		Graphics2D g2d = (Graphics2D) g.create(); 
		Stroke dashed = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{4}, 0);
		g2d.setStroke(dashed);
		g2d.setColor(Color.LIGHT_GRAY);
		g2d.drawLine(x1, y1, x2, y2);
		g2d.dispose();
	}

	protected  void drawConnectingLine(Graphics g, int x1, int y1, int x2, int y2) {
		Graphics2D g2d = (Graphics2D) g.create(); 
		g2d.setColor(lineColor);
		g2d.drawLine(x1, y1, x2, y2);
		g2d.dispose();
	}

	protected  void drawCenteredCircle(Graphics g, int x, int y, int radius) {
		Graphics2D g2 = (Graphics2D) g.create();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.fillOval(x-radius, y-radius, 2*radius, 2*radius);
		g2.dispose();
	}

	protected void selectPoint(Graphics g, GraphPoint point) {
		if(point != null) {
			Graphics2D g2 = (Graphics2D) g.create();
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2.setColor(new Color(135, 200, 250, 230));
			int ix = (int) Math.round(getPanelXCoord(point.getX()));
			int iy = (int) Math.round(getPanelYCoord(point.getY()));
			g2.fillOval(ix-5, iy-5, 10, 10);
			g2.dispose();
		}
	}
	
	public void addPoint(GraphPoint point) {
		
	}
	
	public void removePoint(GraphPoint point) {
		
	}

	/**
	 * MUST BE OVERRIDDEN IN SUBCLASSES
	 * @return The default x-axis increment if the program were allowed to calculate it.
	 */
	public double getDefaultXInc() {
		return -1;
	}

	public double getDefaultYInc() {
		return -1;
	}
	
	/**
	 * GETTERS AND SETTERS
	 */

	public static GraphPoint getMatchPoint() {
		return matchPoint;
	}

	public static Color getLineColor() {
		return lineColor;
	}

	public static void setLineColor(Color lineColor) {
		DBConnection.setLineColor(ID, lineColor);
		Graph.lineColor = lineColor;
	}

	public static Color getPointColor() {
		return pointColor;
	}

	public static void setPointColor(Color pointColor) {
		DBConnection.setPointColor(ID, pointColor);
		Graph.pointColor = pointColor;
	}

	public static boolean isPointsVisible() {
		return pointsVisible;
	}

	public static void setPointsVisible(boolean pointsVisible) {
		DBConnection.setPointsVisible(ID, pointsVisible);
		Graph.pointsVisible = pointsVisible;
	}

	public static boolean isConLinesVisible() {
		return conLinesVisible;
	}

	public static void setConLinesVisible(boolean linesOn) {
		DBConnection.setConLinesVisible(ID, linesOn);
		Graph.conLinesVisible = linesOn;
	}

	public static double getxAxisInc() {
		return xAxisInc;
	}

	public static void setxAxisInc(double xAxisInc) {
		DBConnection.setXAxisInc(ID, xAxisInc);
		DBConnection.setXAxisDefault(ID,  false);
		Graph.xAxisInc = xAxisInc;
		Graph.xAxisDefault = false;
	}

	public static double getyAxisInc() {
		return yAxisInc;
	}

	public static void setyAxisInc(double yAxisInc) {
		DBConnection.setYAxisInc(ID, yAxisInc);
		DBConnection.setYAxisDefault(ID,  false);
		Graph.yAxisInc = yAxisInc;
		Graph.yAxisDefault = false;
	}

	public static ArrayList<GraphPoint> getPointList() {
		return pointList;
	}

	public static boolean isxAxisDefault() {
		return xAxisDefault;
	}

	public static void setxAxisDefault(boolean xAxisDefault) {
		DBConnection.setXAxisDefault(ID, xAxisDefault);
		Graph.xAxisDefault = xAxisDefault;
	}

	public static boolean isyAxisDefault() {
		return yAxisDefault;
	}

	public static void setyAxisDefault(boolean yAxisDefault) {
		DBConnection.setYAxisDefault(ID, yAxisDefault);
		Graph.yAxisDefault = yAxisDefault;
	}

	public static boolean isGridLinesVisible() {
		return gridLinesVisible;
	}

	public static void setGridLinesVisible(boolean gridLinesVisible) {
		DBConnection.setGridLinesVisible(ID, gridLinesVisible);
		Graph.gridLinesVisible = gridLinesVisible;
	}

	public static void setClickedPoint(GraphPoint point) {
		clickedPoint = point;
	}

	public Object getID() {
		return ID;
	}
}
