/**
 * 
 */
package com.gmail.shannonmcin.JDialogs;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import net.miginfocom.swing.MigLayout;
import test.DBConnection;

import javax.swing.JLabel;
import javax.swing.JRadioButton;
import java.awt.Component;
import javax.swing.Box;
import javax.swing.JTextPane;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.event.CaretListener;

import com.gmail.shannonmcin.main.Application;

import javax.swing.event.CaretEvent;

/**
 * @author Shannon McInnis
 *
 */
public class CreateNewGraph extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JPanel buttonPanel;
	private JTextField txtFldName;
	private Font directionFont = new Font("TimesRoman", Font.PLAIN, 15);
	private JTextPane txtPaneTypeError, txtPaneNameError, txtPaneEnterName;
	private JButton okButton, cancelButton;
	private JRadioButton rdbtnXYGraph, rdbtnTimeGraph;
	private ButtonGroup kind;
	private JLabel lblWhichKind, lblName;

	private void setErrors() {
		txtPaneNameError.setOpaque(true);
		txtPaneTypeError.setOpaque(true);
		String id = txtFldName.getText();
		try {
			DBConnection.getXYGraph(id);
			txtPaneNameError.setText("Graph Name Error: This name is already in use, please choose a different one.");
			txtPaneNameError.setBackground(Color.RED);
			if(kind.getSelection() == null) {
				txtPaneTypeError.setText("Graph Type Error: Please choose which kind of graph you would like to create.");
				txtPaneTypeError.setBackground(Color.RED);
			} else {
				txtPaneTypeError.setText("Graph Type Error: None");
				txtPaneTypeError.setBackground(Color.GREEN);
			}
			okButton.setEnabled(false);
		} catch(IllegalArgumentException e2) {
			if(!id.equals("")) {
				txtPaneNameError.setText("Graph Name Error: None");
				txtPaneNameError.setBackground(Color.GREEN);
				if(kind.getSelection() == null) {
					txtPaneTypeError.setText("Graph Type Error: Please choose which kind of graph you would like to create.");
					txtPaneTypeError.setBackground(Color.RED);
					okButton.setEnabled(false);
				} else {
					txtPaneTypeError.setText("Graph Type Error: None");
					txtPaneTypeError.setBackground(Color.GREEN);
					okButton.setEnabled(true);
				}
			} else {
				txtPaneNameError.setText("Graph Name Error: You must enter a name for your graph.");
				txtPaneNameError.setBackground(Color.RED);
				if(kind.getSelection() == null) {
					txtPaneTypeError.setText("Graph Type Error: Please choose which kind of graph you would like to create.");
					txtPaneTypeError.setBackground(Color.RED);
				} else {
					txtPaneTypeError.setText("Graph Type Error: None");
					txtPaneTypeError.setBackground(Color.GREEN);
				}
				okButton.setEnabled(false);
			}
		}
	}

	/**
	 * Create the dialog.
	 */
	public CreateNewGraph() {
		setTitle("Add a New Graph");
		setModalityType(ModalityType.APPLICATION_MODAL);
		setModal(true);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());

		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new MigLayout("", "[grow]", "[][][][][][][grow][grow]"));

		lblWhichKind = new JLabel("What kind of graph would you like to add? You may select only one.");
		lblWhichKind.setFont(directionFont);
		contentPanel.add(lblWhichKind, "cell 0 0");

		kind = new ButtonGroup();

		Component strutRadioBtns = Box.createHorizontalStrut(20);
		contentPanel.add(strutRadioBtns, "flowx,cell 0 1");

		rdbtnXYGraph = new JRadioButton("XY Graph");
		rdbtnXYGraph.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setErrors();
			}
		});
		contentPanel.add(rdbtnXYGraph, "cell 0 1");
		kind.add(rdbtnXYGraph);

		rdbtnTimeGraph = new JRadioButton("Time Graph");
		rdbtnTimeGraph.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setErrors();
			}
		});
		contentPanel.add(rdbtnTimeGraph, "cell 0 1");
		kind.add(rdbtnTimeGraph);

		Component verticalStrut = Box.createVerticalStrut(10);
		contentPanel.add(verticalStrut, "cell 0 2");

		txtPaneEnterName = new JTextPane();
		txtPaneEnterName.setText("What would you like to name this graph? It must be a unique name (you may not use one you have already used.)");
		txtPaneEnterName.setFont(directionFont);
		txtPaneEnterName.setEditable(false);
		txtPaneEnterName.setOpaque(false);
		contentPanel.add(txtPaneEnterName, "cell 0 3,grow");

		Component horizontalStrut = Box.createHorizontalStrut(20);
		contentPanel.add(horizontalStrut, "flowx,cell 0 4");

		lblName = new JLabel("Name:");
		contentPanel.add(lblName, "cell 0 4");

		txtFldName = new JTextField();
		txtFldName.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent e) {
				setErrors();
			}
		});
		contentPanel.add(txtFldName, "cell 0 4");
		txtFldName.setColumns(10);

		Component verticalStrut_1 = Box.createVerticalStrut(10);
		contentPanel.add(verticalStrut_1, "cell 0 5");

		txtPaneTypeError = new JTextPane();
		txtPaneTypeError.setText("Graph Type Error: ");
		txtPaneTypeError.setEditable(false);
		txtPaneTypeError.setOpaque(false);
		contentPanel.add(txtPaneTypeError, "cell 0 6,grow");

		txtPaneNameError = new JTextPane();
		txtPaneNameError.setText("Graph Name Error: ");
		txtPaneNameError.setEditable(false);
		txtPaneNameError.setOpaque(false);
		contentPanel.add(txtPaneNameError, "cell 0 7,grow");

		buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPanel, BorderLayout.SOUTH);

		okButton = new JButton("OK");
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(rdbtnXYGraph.isSelected()) {
					String id = txtFldName.getText();
					Application.setPanelXY(id);
					dispose();
				}
			}
		});
		okButton.setActionCommand("OK");
		buttonPanel.add(okButton);

		cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
		buttonPanel.add(cancelButton);
		getRootPane().setDefaultButton(cancelButton);
	}
}
