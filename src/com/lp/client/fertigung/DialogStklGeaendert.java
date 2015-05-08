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
package com.lp.client.fertigung;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.pc.LPMain;

@SuppressWarnings("static-access")
public class DialogStklGeaendert extends JDialog implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private GridBagLayout gridBagLayout2 = new GridBagLayout();

	private JLabel wlaMeldung = null;
	private JLabel wlaLose = null;
	private JButton btnOK = new JButton();
	private JButton btnAbbrechen = new JButton();
	
	JScrollPane jScrollPane = new JScrollPane();
	JTextPane textPanelLog = new JTextPane();
	String lose=null;
	public boolean bOK=false;

	
	public DialogStklGeaendert( String lose)
			throws Throwable {
		super(LPMain.getInstance().getDesktop(), "", true);
		
		this.lose=lose;
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		jbInit();
		HelperClient.memberVariablenEinerKlasseBenennen(this);

		this.setSize(900, 400);

	}

	public void actionPerformed(ActionEvent e) {

		if (e.getSource().equals(btnAbbrechen)) {
			this.setVisible(false);
		} else {
			bOK=true;
			this.setVisible(false);
		}

	}

	protected void processWindowEvent(WindowEvent e) {
		if (e.getID() == WindowEvent.WINDOW_CLOSING) {
			this.dispose();
		}
	}

	private void jbInit() throws Throwable {

		btnOK.setText(LPMain.getInstance().getTextRespectUISPr("button.ok"));
		btnAbbrechen.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.abbrechen"));
		btnOK.addActionListener(this);
		btnAbbrechen.addActionListener(this);

		wlaMeldung = new JLabel(LPMain.getTextRespectUISPr("fert.mehrereloseausgeben.stklgeaendert"));
		wlaLose = new JLabel(lose);
		textPanelLog.setContentType("text/html");
		
		
		textPanelLog.setText(lose);
		textPanelLog.setFont(Font.decode("Courier bold 12"));
		jScrollPane.getViewport().add(textPanelLog);
		this.getContentPane().setLayout(gridBagLayout2);

		int iZeile = 0;
		
		this.getContentPane().add(
				wlaMeldung,
				new GridBagConstraints(0, iZeile, 3, 1, 0, 0,
						GridBagConstraints.NORTH,
						GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5),
						0, 0));

		iZeile++;

		this.getContentPane().add(
				jScrollPane,
				new GridBagConstraints(0, iZeile, 3, 1, 1, 1,
						GridBagConstraints.NORTH,
						GridBagConstraints.BOTH, new Insets(5, 5, 5, 5),
						0, 0));

		iZeile++;

		this.getContentPane().add(
				btnOK,
				new GridBagConstraints(1, iZeile, 1, 1, 0, 0,
						GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
						new Insets(5, 250, 5, 5), 0, 0));
		this.getContentPane().add(
				btnAbbrechen,
				new GridBagConstraints(2, iZeile, 1, 1, 0, 0,
						GridBagConstraints.WEST, GridBagConstraints.NONE,
						new Insets(5, 5, 5, 5), 0, 0));

	}

}
