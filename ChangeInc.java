/**
 * 
 */
package com.gmail.shannonmcin.JDialogs;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import net.miginfocom.swing.MigLayout;

import javax.swing.JTextField;
import javax.swing.event.CaretListener;
import javax.swing.event.ChangeEvent;

import com.gmail.shannonmcin.graphs.Graph;
import com.gmail.shannonmcin.graphs.XYGraph;
import com.gmail.shannonmcin.main.Application;

import javax.swing.event.CaretEvent;
import javax.swing.JTextPane;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JSlider;
import javax.swing.event.ChangeListener;
import javax.swing.Box;
import javax.swing.JCheckBox;

/**
 * @author Shannon McInnis
 *
 */
public class ChangeInc extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField txtInc;
	private JLabel lblDirections, lblInc;
	private JButton okButton, cancelButton;
	private JTextPane txtError;

	private String currentError = "";

	private enum XError { 
		EMPTY_TEXTBOX("Please enter a value for your x-axis increment.\n"), 
		INVALID_CHARACTER("Please enter a valid value for your x-axis increment. You may use only whole numbers or decimals.\n"), 
		NO_ERROR(""); 

		private String error; 
		private XError(String newError) { 
			this.error = newError; 
		} 

		@Override 
		public String toString(){ 
			return error; 
		} 
	}

	private enum YError { 
		EMPTY_TEXTBOX("Please enter a value for your y-axis increment.\n"), 
		INVALID_CHARACTER("Please enter a valid value for your y-axis increment. You may use only whole numbers or decimals.\n"), 
		NO_ERROR(""); 

		private String error; 
		private YError(String newError) { 
			this.error = newError; 
		} 

		@Override 
		public String toString(){ 
			return error; 
		} 
	}

	private XError xError;
	private YError yError;
	private JSlider slider;
	private JTextField txtMax;
	private JLabel lblMax;
	private Component verticalStrut;
	private JLabel lblUseaxisDefault;
	private Component verticalStrut_1;
	private Component horizontalStrut;
	private JCheckBox chkbxDefault;

	private void setXError(XError error, JComponent comp) {
		currentError = error.toString();
		txtError.setText(currentError);

		System.out.println("current error is " + error.name());
		try {
			if(("EMPTY_TEXTBOX".equals(error.name()) || ("INVALID_CHARACTER".equals(error.name())))) {
				txtInc.setBackground(Color.RED);
				okButton.setEnabled(false);
			} else {
				txtInc.setBackground(Color.WHITE);
				okButton.setEnabled(true);
				if(comp instanceof JSlider) {
					txtInc.setText(((JSlider) comp).getValue() + "");
				} else if(comp instanceof JTextField) {
					slider.setValue(Integer.parseInt(txtInc.getText()));
				}
			}
		} catch (IllegalStateException e) {

		}
	}

	private void setYError(YError error, JComponent comp) {
		currentError = error.toString();
		txtError.setText(currentError);

		try {
			if(("EMPTY_TEXTBOX".equals(error.name()) || ("INVALID_CHARACTER".equals(error.name())))) {
				txtInc.setBackground(Color.RED);
				okButton.setEnabled(false);
			} else {
				txtInc.setBackground(Color.WHITE);
				okButton.setEnabled(true);
				if(comp instanceof JSlider) {
					txtInc.setText(((JSlider) comp).getValue() + "");
				} else if(comp instanceof JTextField) {
					slider.setValue(Integer.parseInt(txtInc.getText()));
				}
			}
		} catch(IllegalStateException e) {

		}
	}

	/**
	 * Create the dialog.
	 */
	public ChangeInc(char axis) {
		setBounds(100, 100, 450, 300);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new MigLayout("", "[200px:200:200px,grow][grow]", "[14px][][][][][][][grow]"));

		lblDirections = new JLabel("Please enter or select the desired " + axis + "-axis increment, or use the default " + axis + "-axis increment.");
		contentPanel.add(lblDirections, "cell 0 0 2 1,alignx left,aligny top");
		
		verticalStrut = Box.createVerticalStrut(20);
		contentPanel.add(verticalStrut, "cell 0 1");

		lblInc = new JLabel(axis + "-Axis Increment:");
		contentPanel.add(lblInc, "cell 0 2,alignx trailing");

		txtInc = new JTextField();
		txtInc.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent e) {
				String currentText = txtInc.getText();
				if(axis == 'x') {
					if("".equals(currentText)) {
						setXError(XError.EMPTY_TEXTBOX, txtInc);
					} else {
						double text = -1.0;
						try {
							text  = Double.parseDouble(currentText);
							System.out.println("currenttext: " + currentText + ", double: " + text);
							setXError(XError.NO_ERROR, txtInc);
						} catch (NumberFormatException e2) {
							if(text == -1) {
								setXError(XError.INVALID_CHARACTER, txtInc);
							}
						}
					}
				} else if(axis == 'y') {
					if("".equals(currentText)) {
						setYError(YError.EMPTY_TEXTBOX, txtInc);
					} else {
						double text = -1.0;
						try {
							text  = Double.parseDouble(currentText);
							setYError(YError.NO_ERROR, txtInc);
						} catch (NumberFormatException e2) {
							if(text == -1) {
								setYError(YError.INVALID_CHARACTER, txtInc);
							}
						}
					}
				}
			}
		});
		contentPanel.add(txtInc, "cell 1 2,growx");
		txtInc.setColumns(10);

		slider = new JSlider();
		slider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				int currentValue = slider.getValue();
				if(axis == 'x') {
					if(currentValue == 0) {
						setXError(XError.EMPTY_TEXTBOX, slider);
					} else {
						setXError(XError.NO_ERROR, slider);
					}
				} else if(axis == 'y') {
					if(currentValue == 0) {
						setYError(YError.EMPTY_TEXTBOX, slider);
					} else {
						setYError(YError.NO_ERROR, slider);
					}
				}
			} 
		});

		contentPanel.add(slider, "cell 1 3");

		lblMax = new JLabel(axis + "-axis Maximum:");
		contentPanel.add(lblMax, "cell 0 4,alignx trailing");

		txtMax = new JTextField();
		txtMax.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent arg0) {
				String currentText = txtInc.getText();
				if(axis == 'x') {
					if("".equals(currentText)) {
						setXError(XError.EMPTY_TEXTBOX, txtInc);
					} else {
						double text = -1.0;
						try {
							text  = Double.parseDouble(currentText);
							System.out.println("currenttext: " + currentText + ", double: " + text);
							setXError(XError.NO_ERROR, txtInc);
						} catch (NumberFormatException e2) {
							if(text == -1) {
								setXError(XError.INVALID_CHARACTER, txtInc);
							}
						}
					}
				} else if(axis == 'y') {
					if("".equals(currentText)) {
						setYError(YError.EMPTY_TEXTBOX, txtInc);
					} else {
						double text = -1.0;
						try {
							text  = Double.parseDouble(currentText);
							setYError(YError.NO_ERROR, txtInc);
						} catch (NumberFormatException e2) {
							if(text == -1) {
								setYError(YError.INVALID_CHARACTER, txtInc);
							}
						}
					}
				}
			}
		});
		contentPanel.add(txtMax, "cell 1 4,growx,aligny top");
		txtMax.setColumns(10);
		
		lblUseaxisDefault = new JLabel("Use default " + axis + "-axis increment?");
		contentPanel.add(lblUseaxisDefault, "cell 0 5,alignx right");
		
		horizontalStrut = Box.createHorizontalStrut(20);
		contentPanel.add(horizontalStrut, "flowx,cell 1 5");
		
		verticalStrut_1 = Box.createVerticalStrut(15);
		contentPanel.add(verticalStrut_1, "cell 0 6");

		txtError = new JTextPane();
		txtError.setEditable(false);
		txtError.setOpaque(false);
		contentPanel.add(txtError, "cell 0 7,grow");
		
		chkbxDefault = new JCheckBox("");
		chkbxDefault.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(chkbxDefault.isSelected()) {
					txtInc.setText("");
					txtInc.setEnabled(false);
					slider.setValue(1);
					if(axis == 'x') setXError(XError.NO_ERROR, txtInc);
					if(axis == 'y') setYError(YError.NO_ERROR, txtInc);
				} else {
					txtInc.setText("");
					txtInc.setEnabled(true);
					slider.setValue(slider.getMaximum()/2);
				}
			}
		});
		contentPanel.add(chkbxDefault, "cell 1 5");

		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);

		okButton = new JButton("Change Increment");
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(axis == 'x') {
					Graph.setxAxisInc(Double.parseDouble(txtInc.getText()));
				} else if(axis == 'y') {
					Graph.setyAxisInc(Double.parseDouble(txtInc.getText()));
				}
				dispose();
			}
		});
		okButton.setActionCommand("OK");
		buttonPane.add(okButton);

		cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		cancelButton.setActionCommand("Cancel");
		buttonPane.add(cancelButton);
		getRootPane().setDefaultButton(cancelButton);

		okButton.setEnabled(false);
	}
	
	public boolean isDefaultTrue() {
		return chkbxDefault.isSelected();
	}
}
