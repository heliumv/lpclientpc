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
package com.lp.client.stueckliste;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;

import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperIdentField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.system.service.KeyvalueDto;
import com.lp.server.system.service.SystemServicesFac;

@SuppressWarnings("static-access")
public class DialogArtikelErsetzen extends JDialog implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private GridBagLayout gridBagLayout2 = new GridBagLayout();

	private WrapperIdentField wifArtikelAlt = null;
	private WrapperIdentField wifArtikelNeu = null;
	private JButton btnOK = new JButton();
	private JButton btnAbbrechen = new JButton();

	InternalFrameStueckliste internalFrame = null;

	public DialogArtikelErsetzen(InternalFrameStueckliste internalFrame)
			throws Throwable {
		super(LPMain.getInstance().getDesktop(), LPMain.getInstance()
				.getTextRespectUISPr("stkl.artikel.ersetztendurch"), true);
		this.internalFrame = internalFrame;
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		jbInit();
		HelperClient.memberVariablenEinerKlasseBenennen(this);

		// Gespeicherte Werte setzen
		setKeyValueDtos(DelegateFactory
				.getInstance()
				.getSystemDelegate()
				.keyvalueFindyByCGruppe(
						SystemServicesFac.KEYVALUE_ARTIKEL_KOPIEREN));
		this.setSize(500, 200);

	}

	public void actionPerformed(ActionEvent e) {

		if (e.getSource().equals(btnAbbrechen)) {
			this.setVisible(false);
		} else {

			// Artikel ersetzen

			if (wifArtikelAlt.getArtikelDto() == null
					|| wifArtikelNeu.getArtikelDto() == null) {
				DialogFactory.showModalDialog(LPMain
						.getTextRespectUISPr("lp.error"), LPMain
						.getTextRespectUISPr("lp.pflichtfelder.ausfuellen"));
				return;
			}
			try {
				DelegateFactory
						.getInstance()
						.getStuecklisteDelegate()
						.artikelErsetzten(
								wifArtikelAlt.getArtikelDto().getIId(),
								wifArtikelNeu.getArtikelDto().getIId());
			} catch (Throwable e1) {
				internalFrame.getTabbedPaneStueckliste().handleException(e1,
						true);
			}
			this.setVisible(false);
		}

	}

	private void setKeyValueDtos(KeyvalueDto[] dtos) throws Throwable {

		for (int z = 0; z < dtos.length; z++) {

			for (int i = 0; i < this.getContentPane().getComponents().length; ++i) {

				{

					if (this.getContentPane().getComponents()[i].getName() != null
							&& this.getContentPane().getComponents()[i]
									.getName().equals(dtos[z].getCKey())) {
						if (this.getContentPane().getComponents()[i] instanceof WrapperCheckBox) {

							WrapperCheckBox wcb = (WrapperCheckBox) this
									.getContentPane().getComponents()[i];
							wcb.setShort(new Short(dtos[z].getCValue()));

						}
					}
				}
			}
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

		wifArtikelAlt = new WrapperIdentField(internalFrame, null);
		wifArtikelNeu = new WrapperIdentField(internalFrame, null);

		wifArtikelAlt.getWtfIdent().setMandatoryField(true);
		wifArtikelNeu.getWtfIdent().setMandatoryField(true);

		this.getContentPane().setLayout(gridBagLayout2);

		int iZeile = 0;
		this.getContentPane().add(new JLabel(LPMain.getInstance().getTextRespectUISPr("stkl.artikel.ersetztendurch.quelle")),
				new GridBagConstraints(0, iZeile, 1, 1, 0, 0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5),
						100, 0));
		iZeile++;
		this.getContentPane().add(
				wifArtikelAlt.getWbuArtikel(),
				new GridBagConstraints(0, iZeile, 1, 1, 0, 0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5),
						100, 0));
		this.getContentPane().add(
				wifArtikelAlt.getWtfIdent(),
				new GridBagConstraints(1, iZeile, 1, 1, 0, 0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5),
						100, 0));
		this.getContentPane().add(
				wifArtikelAlt.getWtfBezeichnung(),
				new GridBagConstraints(2, iZeile, 1, 1, 0, 0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5),
						150, 0));
		iZeile++;
		this.getContentPane().add(new JLabel(LPMain.getInstance().getTextRespectUISPr("stkl.artikel.ersetztendurch.ziel")),
				new GridBagConstraints(0, iZeile, 1, 1, 0, 0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5),
						100, 0));
		
		iZeile++;

		this.getContentPane().add(
				wifArtikelNeu.getWbuArtikel(),
				new GridBagConstraints(0, iZeile, 1, 1, 0, 0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5),
						0, 0));
		this.getContentPane().add(
				wifArtikelNeu.getWtfIdent(),
				new GridBagConstraints(1, iZeile, 1, 1, 0, 0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5),
						0, 0));
		this.getContentPane().add(
				wifArtikelNeu.getWtfBezeichnung(),
				new GridBagConstraints(2, iZeile, 1, 1, 0, 0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5),
						0, 0));

		iZeile++;

		this.getContentPane().add(
				btnOK,
				new GridBagConstraints(1, iZeile, 1, 1, 0, 0,
						GridBagConstraints.CENTER, GridBagConstraints.NONE,
						new Insets(5, 5, 5, 5), 0, 0));
		this.getContentPane().add(
				btnAbbrechen,
				new GridBagConstraints(2, iZeile, 1, 1, 0, 0,
						GridBagConstraints.CENTER, GridBagConstraints.NONE,
						new Insets(5, 5, 5, 5), 0, 0));

	}

}
