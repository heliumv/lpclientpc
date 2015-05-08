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
package com.lp.client.eingangsrechnung;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JDialog;

import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.WrapperDateField;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperSelectField;
import com.lp.client.frame.component.WrapperSpinner;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.eingangsrechnung.service.EingangsrechnungDto;

@SuppressWarnings("static-access")
public class DialogMahnstufeDatum extends JDialog implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private GridBagLayout gridBagLayout2 = new GridBagLayout();
	private JButton btnOK = new JButton();
	private JButton btnAbbrechen = new JButton();

	private WrapperLabel wlaMahndmatum = new WrapperLabel();
	private WrapperDateField wdfMahndatum = new WrapperDateField();

	private WrapperSelectField wsfMahnstufe = null;
	private Integer eingangsrechnungIId = null;

	private TabbedPaneEingangsrechnung tabbedpaneEingangsrechnung = null;

	public DialogMahnstufeDatum(Integer eingangsrechnungIId,
			TabbedPaneEingangsrechnung internalFrame) throws Throwable {
		super(LPMain.getInstance().getDesktop(), LPMain
				.getTextRespectUISPr("rech.extras.zahlungsplan"), true);
		this.tabbedpaneEingangsrechnung = internalFrame;
		this.eingangsrechnungIId = eingangsrechnungIId;
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		jbInit();
		HelperClient.memberVariablenEinerKlasseBenennen(this);

		this.setSize(400, 200);

	}

	public void actionPerformed(ActionEvent e) {

		if (e.getSource().equals(btnAbbrechen)) {

			this.setVisible(false);
		} else if (e.getSource().equals(btnOK)) {

			try {
				if (wdfMahndatum.getTimestamp() == null
						|| wsfMahnstufe.getIKey() == null) {
					DelegateFactory
							.getInstance()
							.getEingangsrechnungDelegate()
							.updateEingangsrechnungMahndaten(
									eingangsrechnungIId, null, null);

				} else {
					DelegateFactory
							.getInstance()
							.getEingangsrechnungDelegate()
							.updateEingangsrechnungMahndaten(
									eingangsrechnungIId,

									wsfMahnstufe.getIKey(),
									wdfMahndatum.getTimestamp());

				}

				this.setVisible(false);
			} catch (Throwable e1) {
				tabbedpaneEingangsrechnung.handleException(e1, true);
			}

		} else {
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

		wlaMahndmatum.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.mahndatum"));

		wsfMahnstufe = new WrapperSelectField(WrapperSelectField.MAHNSTUFE,
				tabbedpaneEingangsrechnung.getInternalFrame(), true);

		EingangsrechnungDto reDto = DelegateFactory.getInstance()
				.getEingangsrechnungDelegate()
				.eingangsrechnungFindByPrimaryKey(eingangsrechnungIId);

		wsfMahnstufe.setKey(reDto.getMahnstufeIId());
		wdfMahndatum.setTimestamp(reDto.getTMahndatum());

		this.getContentPane().setLayout(gridBagLayout2);

		int iZeile = 0;

		iZeile++;
		this.getContentPane().add(
				wlaMahndmatum,
				new GridBagConstraints(0, iZeile, 1, 1, 1, 0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5),
						0, 0));
		this.getContentPane().add(
				wdfMahndatum,
				new GridBagConstraints(1, iZeile, 1, 1, 1, 0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5),
						0, 0));

		iZeile++;
		this.getContentPane().add(
				wsfMahnstufe.getWrapperButton(),
				new GridBagConstraints(0, iZeile, 1, 1, 1, 0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5),
						0, 0));
		this.getContentPane().add(
				wsfMahnstufe.getWrapperTextField(),
				new GridBagConstraints(1, iZeile, 1, 1, 1, 0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5),
						0, 0));

		iZeile++;

		this.getContentPane().add(
				btnOK,
				new GridBagConstraints(0, iZeile, 1, 1, 0, 0,
						GridBagConstraints.EAST, GridBagConstraints.NONE,
						new Insets(5, 5, 5, 5), 0, 0));
		this.getContentPane().add(
				btnAbbrechen,
				new GridBagConstraints(1, iZeile, 1, 1, 0, 0,
						GridBagConstraints.WEST, GridBagConstraints.NONE,
						new Insets(5, 5, 5, 5), 0, 0));

	}

}
