/**
 * 
 */
package com.gmail.shannonmcin.utils;

import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;

/**
 * @author Shannon McInnis
 *
 */
public class IconButton extends JButton {
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 * @param btnText: Text displayed on the button.
	 * @param img: The image displayed on the button.
	 * @param toolTipText: The tooltip text displayed.
	 */
	public IconButton(String btnText, ImageIcon img, String toolTipText) {
		super(btnText, img);
		setToolTipText(toolTipText);
		setFocusPainted(false);
		setBorder(BorderFactory.createRaisedBevelBorder());
		setBorderPainted(false);
		setMargin(new Insets(0, 0, 0, 0));
		setContentAreaFilled(false);
		setOpaque(false);
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {
				setBorderPainted(true);
			}

			@Override
			public void mouseExited(MouseEvent evt) {
				setBorderPainted(false);
			}
		});
	}
}
