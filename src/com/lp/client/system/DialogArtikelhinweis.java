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
package com.lp.client.system;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.JDialog;
import javax.swing.JPanel;

import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.MultipleImageViewer;
import com.lp.client.frame.component.WrapperBildField;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperDateField;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperSNRField;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.util.Helper;

@SuppressWarnings("static-access")
public class DialogArtikelhinweis extends JDialog implements ActionListener, KeyListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel panel1 = new JPanel();
	private GridBagLayout gridBagLayout1 = new GridBagLayout();
	private InternalFrame internalFrame = null;

	private WrapperButton wbuSpeichern = new WrapperButton();

	private MultipleImageViewer wmcBild = null;

	public DialogArtikelhinweis(ArrayList<byte[]> bilder, InternalFrame internalFrame)
			throws Throwable {
		super(LPMain.getInstance().getDesktop(), "", true);

		this.internalFrame = internalFrame;
		try {
			jbInit();
			wmcBild.setImages(bilder);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		this.setSize(800, 600);

		setTitle(LPMain.getInstance().getTextRespectUISPr("lp.hinweis"));

		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				setVisible(false);
				dispose();
			}
		});

	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(wbuSpeichern)) {

			setVisible(false);

		}

	}

	public void keyTyped(KeyEvent e) {

	}

	public void keyPressed(KeyEvent e) {

	}

	public void keyReleased(KeyEvent e) {
		if (e.getSource() instanceof WrapperSNRField) {

			
		}

	}
	private void jbInit() throws Throwable {
		panel1.setLayout(gridBagLayout1);

		wmcBild = new MultipleImageViewer(null);

		wbuSpeichern.setText(LPMain.getInstance().getTextRespectUISPr(
				"button.ok"));

		wbuSpeichern.addActionListener(this);

		add(panel1);

		panel1.add(wmcBild, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						0, 0, 0, 0), 150, 0));

		panel1.add(wbuSpeichern, new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(
						0, 0, 0, 0), 100, 0));

		setContentPane(panel1);

		// Handle window closing correctly.
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				/*
				 * Instead of directly closing the window, we're going to change
				 * the JOptionPane's value property.
				 */
				setVisible(false);
			}
		});

	}

	public void clearAndHide() {
		setVisible(false);
	}

}
