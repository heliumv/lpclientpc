/*******************************************************************************
 * HELIUM V, Open Source ERP software for sustained success
 * at small and medium-sized enterprises.
 * Copyright (C) 2004 - 2015 HELIUM V IT-Solutions GmbH
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published 
 * by the Free Software Foundation, either version 3 of theLicense, or 
 * (at your option) any later version.
 * 
 * According to sec. 7 of the GNU Affero General Public License, version 3, 
 * the terms of the AGPL are supplemented with the following terms:
 * 
 * "HELIUM V" and "HELIUM 5" are registered trademarks of 
 * HELIUM V IT-Solutions GmbH. The licensing of the program under the 
 * AGPL does not imply a trademark license. Therefore any rights, title and
 * interest in our trademarks remain entirely with us. If you want to propagate
 * modified versions of the Program under the name "HELIUM V" or "HELIUM 5",
 * you may only do so if you have a written permission by HELIUM V IT-Solutions 
 * GmbH (to acquire a permission please contact HELIUM V IT-Solutions
 * at trademark@heliumv.com).
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contact: developers@heliumv.com
 ******************************************************************************/
package com.lp.client.frame.component;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class MultipleImageViewer extends JPanel implements ActionListener {

	private static final long serialVersionUID = -5497917944187794785L;

	JButton nextButton, prevButton;

	JLabel counter = new JLabel();
	JLabel textpdf_vorhanden = new JLabel(new ImageIcon(getClass().getResource(
			"/com/lp/client/res/notebook.png")));

	JPanel buttonPanel;
	JPanel imagePanel;
	GridBagLayout layout;
	String param = "";

	ArrayList<byte[]> images = null;

	int numImages;
	int imageCounter = 0;

	ImageViewer[] pi = null;

	public MultipleImageViewer(byte[] image) throws IOException {
		super();
		init(image);
	}

	public void setImage(byte[] image) throws IOException {
		imagePanel.removeAll();
		if (image != null) {
			imageCounter = 0;

			counter.setText("1/1");

			pi = new ImageViewer[1];
			pi[0] = new ImageViewer(image);
			imagePanel.add(pi[0], new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH,
					new Insets(2, 2, 2, 2), 0, 0));
		} else {
			counter.setText("0/0");
		}
	}

	public void setImages(ArrayList<byte[]> image) throws IOException {
		images = image;
		imageCounter = 0;
		imagePanel.removeAll();
		if (image != null && image.size() > 0) {
			imagePanel.removeAll();
			counter.setText("1/" + image.size());

			pi = new ImageViewer[image.size()];

			for (int i = 0; i < image.size(); i++) {
				pi[i] = new ImageViewer(image.get(i));
			}
			imagePanel.add(pi[0], new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH,
					new Insets(2, 2, 2, 2), 0, 0));
		} else {
			counter.setText("0/0");

			pi = new ImageViewer[0];

		}
	}

	public void setTextPDFVorhandenVisible(boolean bVisible){
		textpdf_vorhanden.setVisible(bVisible);
	}
	
	public void init(byte[] image) throws IOException {

		setLayout(new BorderLayout());

		buttonPanel = new JPanel();
		prevButton = new JButton("<<");
		defineFont(prevButton);
		prevButton.addActionListener(this);
		buttonPanel.add(prevButton);

		nextButton = new JButton(">>");
		defineFont(nextButton);
		nextButton.addActionListener(this);
		buttonPanel.add(nextButton);

		buttonPanel.add(counter);
		buttonPanel.add(textpdf_vorhanden);
		
		setTextPDFVorhandenVisible(false);

		add(BorderLayout.NORTH, buttonPanel);

		imagePanel = new JPanel();
		layout = new GridBagLayout();
		imagePanel.setLayout(layout);
		add(BorderLayout.CENTER, imagePanel);

		// setSize(getPreferredSize());
	}

	void defineFont(Component c) {
		c.setFont(new Font("SansSerif", Font.PLAIN, 12));
	}

	public void actionPerformed(ActionEvent e) {
		if (pi != null && pi.length > 0) {
			if (e.getSource().equals(nextButton)) {
				imageCounter++;

				if (imageCounter >= pi.length) {
					imageCounter = pi.length - 1;
				}
				imagePanel.removeAll();
				imagePanel.add(pi[imageCounter], new GridBagConstraints(0, 0,
						1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
						GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
				imagePanel.repaint();
			} else if (e.getSource().equals(prevButton)) {
				imageCounter--;

				if (imageCounter < 0) {
					imageCounter = 0;
				}
				imagePanel.removeAll();
				imagePanel.add(pi[imageCounter], new GridBagConstraints(0, 0,
						1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
						GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
				imagePanel.repaint();
			}

			counter.setText((imageCounter + 1) + "/" + pi.length);

		}
	}

	public void itemStateChanged(ItemEvent e) {
		if (e.getStateChange() == ItemEvent.SELECTED) {

		}

	}

}

/*
 * package com.lp.client.frame.component;
 * 
 * import java.awt.BorderLayout; import java.awt.CardLayout; import
 * java.awt.Dimension; import java.awt.GridBagConstraints; import
 * java.awt.GridBagLayout; import java.awt.Insets; import
 * java.awt.event.ActionEvent; import java.awt.event.ActionListener; import
 * java.io.IOException;
 * 
 * import javax.swing.ImageIcon; import javax.swing.JButton; import
 * javax.swing.JPanel;
 * 
 * public class MultipleImageViewer extends JPanel implements ActionListener {
 * 
 * 
 * private static final long serialVersionUID = 1L;
 * 
 * private CardLayout layout = new CardLayout();
 * 
 * private JButton next; private JButton prev; private JPanel imagePanel=null;
 * 
 * public MultipleImageViewer(byte[] image) throws IOException { super();
 * jbInit(); setImage(image); }
 * 
 * public MultipleImageViewer(byte[][] image) throws IOException { super();
 * jbInit(); setImages(image); }
 * 
 * final public void actionPerformed(ActionEvent e) { if
 * (e.getSource().equals(next)) { layout.next(imagePanel); } else if
 * (e.getSource().equals(prev)) { layout.previous(imagePanel); } }
 * 
 * public void setImage(byte[] image) throws IOException { layout = new
 * CardLayout(); if (image != null) { imagePanel.setLayout(layout);
 * layout.addLayoutComponent(new ImageViewer(image), 0 + "");
 * 
 * }
 * 
 * }
 * 
 * public void setImages(byte[][] image) throws IOException { layout = new
 * CardLayout(); if (image != null) { for (int i = 0; i < image.length; i++) {
 * imagePanel.setLayout(layout); layout.addLayoutComponent(new
 * ImageViewer(image[i]), "" + i); } } }
 * 
 * private void jbInit() { imagePanel = new JPanel() { public Dimension
 * getPreferredSize() { return new Dimension(380, 380); } };
 * 
 * 
 * ImageIcon ii = new ImageIcon(getClass().getResource(
 * "/com/lp/client/res/navigate_left.png"));
 * 
 * prev = new JButton(ii); prev.addActionListener(this);
 * 
 * ii = new ImageIcon(getClass().getResource(
 * "/com/lp/client/res/navigate_right.png"));
 * 
 * next = new JButton(ii); next.addActionListener(this);
 * 
 * setLayout(new BorderLayout());
 * 
 * 
 * 
 * add(BorderLayout.NORTH, imagePanel); add(BorderLayout.SOUTH, prev);
 * 
 * 
 * 
 * } }
 */
