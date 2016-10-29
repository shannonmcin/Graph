/**
 * 
 */
package com.gmail.shannonmcin.utils;

import java.awt.Dimension;
import java.awt.image.ImagingOpException;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;

import net.miginfocom.swing.MigLayout;

/**
 * @author Shannon McInnis
 *
 */
public class ControlPanel extends JPanel {

	/**
	 * Create the panel.
	 */
	public ControlPanel() {
		init();		
	}
	
	public JLabel getLblXcoordinates() {
		return lblXcoordinates;
	}

	public JLabel getLblYcoordinate() {
		return lblYcoordinate;
	}

	public JTextField getTxtXCoord() {
		return txtXCoord;
	}

	public JTextField getTxtYCoord() {
		return txtYCoord;
	}

	public JButton getBtnAdd() {
		return btnAdd;
	}

	public IconButton getBtnPrevGraph() {
		return btnPrevGraph;
	}

	public IconButton getBtnNextGraph() {
		return btnNextGraph;
	}

	public JScrollPane getScrollPane() {
		return scrollPane;
	}

	public JTextPane getTextPane() {
		return textPane;
	}

	public ImageIcon getPrevious() {
		return previous;
	}

	public ImageIcon getNext() {
		return next;
	}

	private JLabel lblXcoordinates, lblYcoordinate;
	private JTextField txtXCoord, txtYCoord;
	private JButton btnAdd;
	private IconButton btnPrevGraph, btnNextGraph;
	private JScrollPane scrollPane;
	private JTextPane textPane;
	
	private ImageIcon previous, next;

	private void initImageIcons() {
		try {
			next = new ImageIcon(Scalr.resize(ImageIO.read(getClass().getResource("/resource/next.png")), 25));
			previous = new ImageIcon(Scalr.resize(ImageIO.read(getClass().getResource("/resource/previous.png")), 25));
		} catch (IllegalArgumentException | ImagingOpException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void init() {
		initImageIcons();
		
		setMinimumSize(new Dimension(200, 200));
//		setPreferredSize(new Dimension(200, getHeight()));
		setMaximumSize(new Dimension(200, Integer.MAX_VALUE));
		
		setLayout(new MigLayout("", "[100px:n][100px:n]", "[][][][grow][27:n:30px]"));

		lblXcoordinates = new JLabel("X-Coordinate:");
		add(lblXcoordinates, "cell 0 0,alignx trailing");

		txtXCoord = new JTextField();
		add(txtXCoord, "cell 1 0,growx,aligny top");
		txtXCoord.setColumns(10);

		lblYcoordinate = new JLabel("Y-Coordinate:");
		add(lblYcoordinate, "cell 0 1,alignx trailing");

		txtYCoord = new JTextField();
		add(txtYCoord, "cell 1 1,growx");
		txtYCoord.setColumns(10);

		btnAdd = new JButton("Add Point");
		add(btnAdd, "cell 1 2,alignx center,aligny center");
		
		scrollPane = new JScrollPane();
		add(scrollPane, "cell 0 3 2 1,grow");
		
		textPane = new JTextPane();
		scrollPane.setViewportView(textPane);
		textPane.setEditable(false);
		textPane.setOpaque(false);
		
		btnPrevGraph = new IconButton(null, previous, "Go to the previous graph");
		add(btnPrevGraph, "cell 0 4,alignx center,aligny center");
		
		btnNextGraph = new IconButton(null, next, "Go to the next graph");
		add(btnNextGraph, "cell 1 4,alignx center,aligny center");
	}
}
