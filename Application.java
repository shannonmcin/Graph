/**
 * 
 */
package com.gmail.shannonmcin.main;

import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JToolBar;
import javax.swing.UIManager;

import com.gmail.shannonmcin.JDialogs.ChangeInc;
import com.gmail.shannonmcin.JDialogs.CreateNewGraph;
import com.gmail.shannonmcin.graphs.Graph;
//import com.gmail.shannonmcin.graphs.TimeGraph;
import com.gmail.shannonmcin.graphs.XYGraph;
import com.gmail.shannonmcin.utils.GraphPoint;
import com.gmail.shannonmcin.utils.IconButton;
import com.gmail.shannonmcin.utils.Scalr;

import javax.imageio.ImageIO;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JDialog;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.image.ImagingOpException;
import java.io.IOException;
import java.util.ArrayList;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JPanel;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;
import test.DBConnection;

import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JList;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;

/**
 * @author Shannon McInnis
 *
 */
public class Application {

	private static JFrame frame;
	private static JToolBar toolBar;
	private static Graph panel;
	private static JPanel commandPanel;
	private static JLabel lblXcoordinates;
	private static JLabel lblYcoordinate;
	private static JTextField txtXCoord;
	private static JTextField txtYCoord;
	private static JButton btnAddPoint, btnDeletePoint;

	private static ImageIcon addNew, remove, lineColor, pointColor, xInc, yInc, gridLines, pointsOff, linesOff, previous, next;
	private static IconButton btnAddGraph, btnRemoveGraph, btnChangeLineColor, btnChangePointColor, btnChangeXInc, btnChangeYInc, btnToggleGridLines, btnTogglePoints, btnToggleConLines;
	private static IconButton btnPrevGraph;
	private static IconButton btnNextGraph;

	private static DefaultListModel<Object> pointListModel;
	private static JList<Object> pointList;
	private static JScrollPane scrollPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Application window = new Application();
					Application.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Application() {
		initialize();
	}

	private static void updateCoordList() {
		if(panel != null) {
			ArrayList<GraphPoint> points = Graph.getPointList();
			points.sort(null);
			pointList.clearSelection();
			pointListModel.clear();
			pointListModel.addElement("<html><strong> Current points: </strong></html>");
			String currentPoints = "";
			for(int i = 0; i < points.size(); i++ ) {
				currentPoints = points.get(i).toString();
				pointListModel.addElement(currentPoints);
			}
		}
	}

	private void initImageIcons() {
		try {
			addNew = new ImageIcon(Scalr.resize(ImageIO.read(getClass().getResource("/resource/plus.png")), 25));
			remove = new ImageIcon(Scalr.resize(ImageIO.read(getClass().getResource("/resource/remove.png")), 25));

			lineColor = new ImageIcon(Scalr.resize(ImageIO.read(getClass().getResource("/resource/colorful line.png")), 25));
			pointColor = new ImageIcon(Scalr.resize(ImageIO.read(getClass().getResource("/resource/colorful circle.png")), 25));
			xInc = new ImageIcon(Scalr.resize(ImageIO.read(getClass().getResource("/resource/x++.png")), 25));
			yInc = new ImageIcon(Scalr.resize(ImageIO.read(getClass().getResource("/resource/y++.png")), 25));
			gridLines = new ImageIcon(Scalr.resize(ImageIO.read(getClass().getResource("/resource/gridlines.png")), 25));
			pointsOff = new ImageIcon(Scalr.resize(ImageIO.read(getClass().getResource("/resource/black circle.png")), 25));
			linesOff = new ImageIcon(Scalr.resize(ImageIO.read(getClass().getResource("/resource/black diagonal line.png")), 25));

			previous = new ImageIcon(Scalr.resize(ImageIO.read(getClass().getResource("/resource/previous.png")), 25));
			next = new ImageIcon(Scalr.resize(ImageIO.read(getClass().getResource("/resource/next.png")), 25));
		} catch (IllegalArgumentException | ImagingOpException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void updatePrevNextBtns() {
		if(panel != null) {
			System.out.println("current id is " + panel.getID());
			try {
				Object nextID = DBConnection.getIdOfNextXYGraph(panel.getID());
				System.out.println("nextid is " + nextID);
				btnNextGraph.setEnabled(true);
			} catch(IllegalArgumentException e) {
				btnNextGraph.setEnabled(false);
			}
			try {
				Object prevID = DBConnection.getIdOfPrevXYGraph(panel.getID()); 
				System.out.println("previd is " + prevID);
				btnPrevGraph.setEnabled(true);
			} catch(IllegalArgumentException e) {
				btnPrevGraph.setEnabled(false);
			}
		}
	}

	private void initCommandPnl() {
		lblXcoordinates = new JLabel("X-Coordinate:");
		commandPanel.add(lblXcoordinates, "cell 0 0,alignx trailing");

		txtXCoord = new JTextField();
		commandPanel.add(txtXCoord, "cell 1 0,growx,aligny top");
		txtXCoord.setColumns(10);

		lblYcoordinate = new JLabel("Y-Coordinate:");
		commandPanel.add(lblYcoordinate, "cell 0 1,alignx trailing");

		txtYCoord = new JTextField();
		commandPanel.add(txtYCoord, "cell 1 1,growx");
		txtYCoord.setColumns(10);

		btnAddPoint = new JButton("Add Point");
		btnAddPoint.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				double x = Double.parseDouble(txtXCoord.getText());
				double y = Double.parseDouble(txtYCoord.getText());
				GraphPoint newPoint = new GraphPoint(x, y);
				panel.addPoint(newPoint);
				updateCoordList();
			}
		});
		commandPanel.add(btnAddPoint, "cell 1 2");
		
		btnDeletePoint = new JButton("Delete Point");
		btnDeletePoint.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(pointList.getSelectedValue() != null) {
					String pointStr = (String) pointList.getSelectedValue();
					String woParent = pointStr.substring(1, pointStr.length()-1);
					String x = woParent.substring(0, woParent.indexOf(','));
					String y = woParent.substring(woParent.indexOf(',') + 1);
					double xVal = Double.parseDouble(x);
					double yVal = Double.parseDouble(y);
					panel.removePoint(new GraphPoint(xVal, yVal));
					Graph.setClickedPoint(null);
					updateCoordList();
					panel.repaint();
				}
			}
		});
		commandPanel.add(btnDeletePoint, "cell 1 4");

		btnPrevGraph = new IconButton(null, previous, "Go to the previous graph");
		btnPrevGraph.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Object currentID = panel.getID();
				Object prevID = DBConnection.getIdOfPrevXYGraph(currentID);
				panel = new XYGraph(prevID);
				Graph.setClickedPoint(null);
				updateCoordList();
				updatePrevNextBtns();
			}
		});
		commandPanel.add(btnPrevGraph, "cell 0 5,alignx center,aligny center");

		pointListModel = new DefaultListModel<Object>();
		pointList = new JList<Object>(pointListModel);
		pointList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent arg0) {
				if(pointList.getSelectedValue() != null) {
					String pointStr = (String) pointList.getSelectedValue();
					String woParent = pointStr.substring(1, pointStr.length()-1);
					String x = woParent.substring(0, woParent.indexOf(','));
					String y = woParent.substring(woParent.indexOf(',') + 1);
					double xVal = Double.parseDouble(x);
					double yVal = Double.parseDouble(y);
					Graph.setClickedPoint(new GraphPoint(xVal, yVal));
					btnDeletePoint.setEnabled(true);
				}
			}
		});

		scrollPane = new JScrollPane();
		commandPanel.add(scrollPane, "cell 0 3 2 1,grow");
		scrollPane.setViewportView(pointList);

		btnNextGraph = new IconButton(null, next, "Go to the next graph");
		btnNextGraph.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Object currentID = panel.getID();
				Object nextID = DBConnection.getIdOfNextXYGraph(currentID);
				panel = new XYGraph(nextID);
				Graph.setClickedPoint(null);
				updateCoordList();
				updatePrevNextBtns();
			}
		});
		commandPanel.add(btnNextGraph, "cell 1 5,alignx center,aligny center");
	}

	private void initToolbar() {
		btnAddGraph = new IconButton(null, addNew, "Add a graph to the database");
		btnAddGraph.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				EventQueue.invokeLater(new Runnable() {
					public void run() {
						try {
							CreateNewGraph add = new CreateNewGraph();
							add.setVisible(true);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
			}
		});

		btnRemoveGraph = new IconButton(null, remove, "Remove this graph from the database (PERMANENT)");
		btnRemoveGraph.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Object id = panel.getID();
				DBConnection.removeXYGraph(id);
				panel = new XYGraph(DBConnection.getFirstXYGraph());
				updatePrevNextBtns();
				updateCoordList();
			}
		});

		btnChangeLineColor = new IconButton(null, lineColor, "Change the color of the lines connecting the points");
		btnChangeLineColor.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Color initialColor = Graph.getLineColor();
				Color newColor = JColorChooser.showDialog(null, "Line Color", initialColor);
				if (newColor != null) {
					Graph.setLineColor(newColor);
				}
			}
		});

		btnChangePointColor = new IconButton(null, pointColor, "Change the color of the points");
		btnChangePointColor.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Color initialColor = Graph.getPointColor();
				Color newColor = JColorChooser.showDialog(null, "Point Color", initialColor);
				if (newColor != null) {
					Graph.setPointColor(newColor);
				}
			}
		});

		btnChangeXInc = new IconButton(null, xInc, "Change the increment of the x-axis");
		btnChangeXInc.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					ChangeInc dialog = new ChangeInc('x');
					dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
					dialog.setVisible(true);
					boolean is = dialog.isDefaultTrue();
					System.out.println("is is " + is);
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		});

		btnChangeYInc = new IconButton(null, yInc, "Change the increment of the y-axis");
		btnChangeYInc.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					ChangeInc dialog = new ChangeInc('y');
					dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
					dialog.setVisible(true);
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		});

		btnToggleGridLines = new IconButton(null, gridLines, "Toggle the grid lines on or off");
		btnToggleGridLines.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean grid = Graph.isGridLinesVisible();
				if(grid) {
					Graph.setGridLinesVisible(false);
				} else {
					Graph.setGridLinesVisible(true);
				}
			}
		});

		btnTogglePoints = new IconButton(null, pointsOff, "Toggle the graphical points on or off");
		btnTogglePoints.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				boolean points = Graph.isPointsVisible();
				if(points) {
					Graph.setPointsVisible(false);
				} else {
					Graph.setPointsVisible(true);
				}
			}
		});

		btnToggleConLines = new IconButton(null, linesOff, "Toggle the lines connecting the points on or off");
		btnToggleConLines.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				boolean conLines = Graph.isConLinesVisible();
				if(conLines) {
					Graph.setConLinesVisible(false);
				} else {
					Graph.setConLinesVisible(true);
				}
			}
		});

		toolBar.add(btnAddGraph);
		toolBar.add(btnRemoveGraph);
		toolBar.addSeparator();
		toolBar.add(btnChangeLineColor);
		toolBar.add(btnChangePointColor);
		toolBar.addSeparator();
		toolBar.add(btnChangeXInc);
		toolBar.add(btnChangeYInc);
		toolBar.addSeparator();
		toolBar.add(btnToggleGridLines);
		toolBar.add(btnTogglePoints);
		toolBar.add(btnToggleConLines);
	}


	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		initImageIcons();
		try {
			// Set cross-platform Java L&F (also called "Metal")
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch(Exception e) {
			e.printStackTrace();
		}

		frame = new JFrame();
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{1362, 150, 0};
		gridBagLayout.rowHeights = new int[]{27, 0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		frame.getContentPane().setLayout(gridBagLayout);

		toolBar = new JToolBar();
		GridBagConstraints gbc_toolBar = new GridBagConstraints();
		gbc_toolBar.gridwidth = 2;
		gbc_toolBar.insets = new Insets(0, 0, 5, 5);
		gbc_toolBar.anchor = GridBagConstraints.NORTH;
		gbc_toolBar.fill = GridBagConstraints.HORIZONTAL;
		gbc_toolBar.gridx = 0;
		gbc_toolBar.gridy = 0;
		initToolbar();
		frame.getContentPane().add(toolBar, gbc_toolBar);

		panel = new XYGraph(DBConnection.getLastGraph());
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.insets = new Insets(0, 0, 0, 5);
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 1;
		if(panel != null) frame.getContentPane().add(panel, gbc_panel);

		commandPanel = new JPanel();
		GridBagConstraints gbc_commandPanel = new GridBagConstraints();
		gbc_commandPanel.fill = GridBagConstraints.BOTH;
		gbc_commandPanel.gridx = 1;
		gbc_commandPanel.gridy = 1;
		frame.getContentPane().add(commandPanel, gbc_commandPanel);
		commandPanel.setLayout(new MigLayout("", "[grow][grow]", "[][][][grow][][27:n:30px]"));
		initCommandPnl();

		updateCoordList();

		btnDeletePoint.setEnabled(false);
		btnPrevGraph.setEnabled(false);
		btnNextGraph.setEnabled(false);
		updatePrevNextBtns();
	}

	/**
	 * @return the panel
	 */
	public static JPanel getPanel() {
		return panel;
	}

	public static void setSelectedItem(String item) {
		pointList.setSelectedValue(item,  true);
	}

	public static void setPanelXY(Object id) {
		panel = new XYGraph(id);
		updateCoordList();
		updatePrevNextBtns();
	}

	public static void setDeleteBtnEnabled(boolean on) {
		btnDeletePoint.setEnabled(on);
	}

	public static void setPanelTime(Object id) {
		//		panel = new TimeGraph(id);
	}

}
