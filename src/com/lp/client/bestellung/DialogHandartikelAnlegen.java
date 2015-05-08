package com.lp.client.bestellung;

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

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikelFac;
import com.lp.server.artikel.service.ArtikelsprDto;
import com.lp.server.system.service.SystemFac;

@SuppressWarnings("static-access")
public class DialogHandartikelAnlegen extends JDialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JPanel panelUrlaubsanspruch = new JPanel();
	GridBagLayout gridBagLayout1 = new GridBagLayout();
	GridBagLayout gridBagLayout2 = new GridBagLayout();
	WrapperLabel wlaBezeichnung = new WrapperLabel();
	WrapperTextField wtfBezeichnung = new WrapperTextField();
	WrapperLabel wlaZusatzBezeichnung = new WrapperLabel();
	JTextField wtfZusatzBezeichnung = new JTextField();

	JButton wbuFertig = new JButton();

	ArtikelDto letzter_gebuchter_artikelDto = null;
	ArtikelDto artikelDto = null;

	PanelBasis panelBasis = null;

	public Integer artikelIId = null;

	public DialogHandartikelAnlegen(PanelBasis panelBasis) throws Throwable {
		super(LPMain.getInstance().getDesktop(), LPMain.getInstance()
				.getTextRespectUISPr("bes.wep.handartikel.anlegen"), true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.panelBasis = panelBasis;

		jbInit();
		pack();
		wtfBezeichnung.requestFocus();
	}

	private void jbInit() throws Throwable {
		panelUrlaubsanspruch.setLayout(gridBagLayout1);

		wlaBezeichnung.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.bezeichnung"));

		wlaZusatzBezeichnung.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.zusatzbezeichnung"));

		wtfBezeichnung.setMandatoryField(true);

		wbuFertig.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.speichern"));
		wbuFertig
				.addActionListener(new DialogHandartikelAnlegen_wbuFertig_actionAdapter(
						this));
		this.getContentPane().setLayout(gridBagLayout2);

		this.getContentPane().add(
				panelUrlaubsanspruch,
				new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(0, 0, 0, 0), 250, 50));

		panelUrlaubsanspruch.add(wtfBezeichnung, new GridBagConstraints(2, 1,
				1, 1, 0.2, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		panelUrlaubsanspruch.add(wlaBezeichnung, new GridBagConstraints(0, 1,
				1, 1, 0.15, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		panelUrlaubsanspruch.add(wtfZusatzBezeichnung, new GridBagConstraints(
				2, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		panelUrlaubsanspruch.add(wlaZusatzBezeichnung, new GridBagConstraints(
				0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		panelUrlaubsanspruch.add(wbuFertig, new GridBagConstraints(2, 5, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

	}

	public void wbuFertig_actionPerformed(ActionEvent e) {

		// Artikel anlegen
		try {

			if (wtfBezeichnung.getText()== null) {

				DialogFactory
						.showModalDialog(
								LPMain.getTextRespectUISPr("lp.error"),
								LPMain.getTextRespectUISPr("lp.pflichtfelder.ausfuellen"));
				return;
			}
			
			ArtikelDto artikelDto = new ArtikelDto();
			artikelDto.setArtikelartCNr(ArtikelFac.ARTIKELART_HANDARTIKEL);
			artikelDto.setEinheitCNr(SystemFac.EINHEIT_STUECK);

			ArtikelsprDto artikelsprDto = new ArtikelsprDto();

			artikelsprDto.setCBez(wtfBezeichnung.getText());
			artikelsprDto.setCZbez(wtfZusatzBezeichnung.getText());

			artikelDto.setArtikelsprDto(artikelsprDto);

			artikelIId = DelegateFactory.getInstance().getArtikelDelegate()
					.createArtikel(artikelDto);

		} catch (Throwable ex) {
			panelBasis.handleException(ex, true);
		}

		this.dispose();
	}

}

class DialogHandartikelAnlegen_wbuFertig_actionAdapter implements
		ActionListener {
	private DialogHandartikelAnlegen adaptee;

	DialogHandartikelAnlegen_wbuFertig_actionAdapter(
			DialogHandartikelAnlegen adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		adaptee.wbuFertig_actionPerformed(e);
	}
}
