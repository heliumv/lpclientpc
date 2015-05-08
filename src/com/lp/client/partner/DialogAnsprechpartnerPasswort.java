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
package com.lp.client.partner;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperPasswordField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.auftrag.service.AuftragDto;
import com.lp.server.partner.service.AnsprechpartnerDto;
import com.lp.util.Helper;

@SuppressWarnings("static-access")
public class DialogAnsprechpartnerPasswort extends JDialog implements
		ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel panel1 = new JPanel();
	private GridBagLayout gridBagLayout1 = new GridBagLayout();

	private AnsprechpartnerDto ansprechpartnerDto = null;

	private WrapperLabel wlaKennwortBestaetigen = new WrapperLabel();
	private WrapperPasswordField wpfKennwortBestaetigen = new WrapperPasswordField();
	private WrapperLabel wlaKennwort = new WrapperLabel();
	private WrapperPasswordField wpfKennwort = new WrapperPasswordField();

	private WrapperButton wbuSpeichern = new WrapperButton();
	private WrapperButton wbuLoeschen = new WrapperButton();

	public DialogAnsprechpartnerPasswort(AnsprechpartnerDto ansprechpartnerDto) {
		super(LPMain.getInstance().getDesktop(), "", true);

		this.ansprechpartnerDto = ansprechpartnerDto;
		try {
			jbInit();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		this.setSize(300, 200);

		setTitle(LPMain.getInstance().getTextRespectUISPr(
				"part.ansprechpartner.kennwortdialog"));

		if (ansprechpartnerDto.getCKennwort() != null) {
			wpfKennwort.setText("**********");
			wpfKennwortBestaetigen.setText("**********");
		}

		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				setVisible(false);
				dispose();
			}
		});

	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(wbuSpeichern)) {

			if (wpfKennwort.getPassword().length == 0
					|| wpfKennwortBestaetigen.getPassword().length == 0) {

				DialogFactory.showModalDialog(LPMain
						.getTextRespectUISPr("lp.error"), LPMain
						.getTextRespectUISPr("lp.pflichtfelder.ausfuellen"));
				return;
			}

			if (!new String(wpfKennwort.getPassword()).equals(new String(
					wpfKennwortBestaetigen.getPassword()))) {
				DialogFactory.showModalDialog(
						LPMain.getInstance().getTextRespectUISPr("lp.error"),
						LPMain.getInstance().getTextRespectUISPr(
								"pers.error.kennwoerterstimmennichtueberein"));
				return;
			}

			if (!new String(wpfKennwort.getPassword()).equals("**********")) {
				ansprechpartnerDto.setCKennwort(Helper.getMD5Hash(
						new String(wpfKennwort.getPassword())).toString());
			}

			setVisible(false);

		} else if (e.getSource().equals(wbuLoeschen)) {
			ansprechpartnerDto.setCKennwort(null);
			this.setVisible(false);
		}

	}

	private void jbInit() throws Exception {
		panel1.setLayout(gridBagLayout1);

		wlaKennwort.setText(LPMain.getInstance().getTextRespectUISPr(
				"pers.benutzer.kennwort"));

		wlaKennwortBestaetigen.setText(LPMain.getInstance()
				.getTextRespectUISPr("pers.benutzer.kennwortbestaetigen"));

		wbuSpeichern.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.report.save"));
		wbuLoeschen.setText(LPMain.getInstance().getTextRespectUISPr(
				"part.ansprechpartner.kennwortdialog.loeschen"));

		wpfKennwort.setMandatoryField(true);
		wpfKennwortBestaetigen.setMandatoryField(true);

		wbuSpeichern.addActionListener(this);
		wbuLoeschen.addActionListener(this);

		add(panel1);

		panel1.add(wlaKennwort, new GridBagConstraints(0, 3, 1, 1, 1.0, 0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						2, 2, 2, 2), 50, 0));
		panel1.add(wpfKennwort, new GridBagConstraints(1, 3, 1, 1, 1.0, 0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						2, 2, 2, 2), 100, 0));

		panel1.add(wlaKennwortBestaetigen, new GridBagConstraints(0, 4, 1, 1,
				1.0, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		panel1.add(wpfKennwortBestaetigen, new GridBagConstraints(1, 4, 1, 1,
				1.0, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));

		panel1.add(wbuSpeichern, new GridBagConstraints(0, 5, 1, 1, 1.0, 0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						2, 2, 2, 2), 0, 0));
		panel1.add(wbuLoeschen, new GridBagConstraints(1, 5, 1, 1, 1.0, 0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						2, 2, 2, 2), 0, 0));

		setContentPane(panel1);

		// Handle window closing correctly.
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {

				setVisible(false);
			}
		});

	}

	public void clearAndHide() {
		setVisible(false);
	}

}
