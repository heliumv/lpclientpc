/*******************************************************************************
 * HELIUM V, Open Source ERP software for sustained success
 * at small and medium-sized enterprises.
 * Copyright (C) 2004 - 2014 HELIUM V IT-Solutions GmbH
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
package com.lp.client.pc;

import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextArea;

public class DialogBrowseError extends JDialog {
	private static final long serialVersionUID = 3218661512339277668L;

	public DialogBrowseError(Frame frame, String error, final String url) {
		super(frame);
		setModal(true);
		setLocationByPlatform(true);
		setTitle(LPMain.getTextRespectUISPr("lp.error"));
		setSize(450, 150);
		getContentPane().setLayout(new GridBagLayout());
		JTextArea errorText = new JTextArea((error == null ? "" : error + "\n")
				+ LPMain.getTextRespectUISPr("lp.browse.linkkopieren") + "\n\n"
				+ url);
		errorText.setWrapStyleWord(true);
		errorText.setLineWrap(true);
		errorText.setForeground(new JLabel().getForeground());
		errorText.setBackground(new JLabel().getBackground());
		errorText.setEditable(false);
		getContentPane().add(
				errorText,
				new GridBagConstraints(1, 1, 2, 1, 1.0, 1.0,
						GridBagConstraints.PAGE_START, GridBagConstraints.BOTH,
						new Insets(5, 5, 5, 5), 0, 0));
		// getContentPane().add(new
		// JTextArea(LPMain.getTextRespectUISPr("lp.browse.linkkopieren")));
		// getContentPane().add(new JTextArea(url));

		JButton copy = new JButton(
				LPMain.getTextRespectUISPr("lp.inzwischenablagekopieren"));
		copy.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				StringSelection ss = new StringSelection(url);
				Toolkit.getDefaultToolkit().getSystemClipboard()
						.setContents(ss, null);
			}
		});
		getContentPane().add(
				copy,
				new GridBagConstraints(1, 2, 1, 1, 1.0, 0.0,
						GridBagConstraints.PAGE_START,
						GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5),
						0, 0));
		JButton ok = new JButton(LPMain.getTextRespectUISPr("button.ok"));
		ok.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				close();
			}
		});
		getContentPane().add(
				ok,
				new GridBagConstraints(2, 2, 1, 1, 1.0, 0.0,
						GridBagConstraints.PAGE_START,
						GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5),
						0, 0));
		setResizable(false);
		setVisible(true);
	}

	private void close() {
		this.dispose();
	}

}
