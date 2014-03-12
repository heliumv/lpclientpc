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
package com.lp.client.bestellung;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.math.BigDecimal;
import java.sql.Date;

import javax.swing.JButton;
import javax.swing.JDialog;

import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.WrapperDateField;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperSelectField;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.artikel.service.LagerDto;
import com.lp.server.bestellung.service.WareneingangDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.system.service.WechselkursDto;

@SuppressWarnings("static-access")
public class DialogNeuerWareneingang extends JDialog implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private GridBagLayout gridBagLayout2 = new GridBagLayout();

	private JButton btnOK = new JButton();
	private JButton btnAbbrechen = new JButton();

	private WrapperLabel wlaLSNummer = new WrapperLabel();
	private WrapperTextField wtfLSNummer = new WrapperTextField();

	private WrapperLabel wlaLSDatum = new WrapperLabel();
	private WrapperDateField wdfLSDatum = new WrapperDateField();

	private WrapperLabel wlaWEDatum = new WrapperLabel();
	private WrapperDateField wdfWEDatum = new WrapperDateField();

	private WrapperSelectField wbuLager = null;

	TabbedPaneBestellung tpBestellung = null;

	public DialogNeuerWareneingang(TabbedPaneBestellung tpBestellung)
			throws Throwable {
		super(LPMain.getInstance().getDesktop(),
				LPMain.getInstance().getTextRespectUISPr(
						"bes.bestellung.neuerwareneingang.auswep"), true);
		this.tpBestellung = tpBestellung;

		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		jbInit();
		HelperClient.memberVariablenEinerKlasseBenennen(this);

		this.setSize(400, 250);

	}

	public void actionPerformed(ActionEvent e) {

		if (e.getSource().equals(btnAbbrechen)) {

			this.setVisible(false);
		} else if (e.getSource().equals(btnOK)) {
			try {
				if (wtfLSNummer.getText() == null
						|| wdfLSDatum.getDate() == null
						|| wdfWEDatum.getDate() == null
						|| wbuLager.getOKey() == null) {
					DialogFactory
							.showModalDialog(
									LPMain.getTextRespectUISPr("lp.error"),
									LPMain.getTextRespectUISPr("lp.pflichtfelder.ausfuellen"));
					return;
				}

				// Neuen Wareneingang anlegen und selektieren

				WareneingangDto weDto = new WareneingangDto();

				ParametermandantDto pm = DelegateFactory
						.getInstance()
						.getParameterDelegate()
						.getMandantparameter(
								LPMain.getTheClient().getMandant(),
								ParameterFac.KATEGORIE_ALLGEMEIN,
								ParameterFac.PARAMETER_GEMEINKOSTENFAKTOR);

				Double gkdouble = new Double(pm.getCWert());

				weDto.setCLieferscheinnr(wtfLSNummer.getText());
				weDto.setTLieferscheindatum(wdfLSDatum.getTimestamp());
				weDto.setTWareneingangsdatum(wdfWEDatum.getTimestamp());
				weDto.setBestellungIId(tpBestellung.getBesDto().getIId());
				weDto.setDGemeinkostenfaktor(gkdouble);
				weDto.setFRabattsatz(0D);
				weDto.setLagerIId(wbuLager.getIKey());

				String sMandantWaehrung = LPMain.getTheClient()
						.getSMandantenwaehrung();
				if (tpBestellung.getBesDto().getWaehrungCNr()
						.equals(sMandantWaehrung)) {
					// gleich wie Mandantenwaehrung -> Kurs = 1.
					weDto.setNWechselkurs(new BigDecimal(1));
				} else {
					// aktuellen Kurs zur Mandantenwaehrung holen.
					WechselkursDto wechselkursDto = DelegateFactory
							.getInstance()
							.getLocaleDelegate()
							.getKursZuDatum(
									sMandantWaehrung,
									tpBestellung.getBesDto().getWaehrungCNr(),
									new Date(tpBestellung.getWareneingangDto()
											.getTWareneingangsdatum().getTime()));
					weDto.setNWechselkurs(wechselkursDto.getNKurs());
				}

				Integer pkPosition = DelegateFactory.getInstance()
						.getWareneingangDelegate()
						.createWareneingang(weDto);
				tpBestellung.setWareneingangDto(DelegateFactory.getInstance()
						.getWareneingangDelegate()
						.wareneingangFindByPrimaryKey(pkPosition));
				tpBestellung.refreshBestellungWareneingang(tpBestellung
						.getBesDto().getIId());
				tpBestellung.getWareneingangTop().eventYouAreSelected(false);
				tpBestellung.getWareneingangTop().setSelectedId(pkPosition);

				tpBestellung.getPanelWareneingang().eventYouAreSelected(false);
				tpBestellung.refreshBestellungWEP(tpBestellung
						.getBesDto().getIId());
				tpBestellung.setSelectedIndex(tpBestellung.IDX_PANEL_WARENEINGANGSPOSITIONEN);
				

			} catch (Throwable e1) {
				tpBestellung.getInternalFrame().handleException(e1, false);
			}
			this.setVisible(false);
		}

	}

	protected void processWindowEvent(WindowEvent e) {
		if (e.getID() == WindowEvent.WINDOW_CLOSING) {

			this.dispose();
		}
	}

	private void jbInit() throws Throwable {

		wbuLager = new WrapperSelectField(WrapperSelectField.LAGER,
				tpBestellung.getInternalFrame(), false);

		wtfLSNummer.setMandatoryField(true);
		wdfLSDatum.setMandatoryField(true);
		wdfWEDatum.setMandatoryField(true);
		wbuLager.setMandatoryField(true);

		wdfLSDatum.setDate(new java.sql.Date(System.currentTimeMillis()));
		wdfWEDatum.setDate(new java.sql.Date(System.currentTimeMillis()));

		LagerDto lagerDto = DelegateFactory
				.getInstance()
				.getLagerDelegate()
				.lagerFindByPrimaryKey(
						tpBestellung.getLieferantDto()
								.getLagerIIdZubuchungslager());
		wbuLager.setKey(lagerDto.getIId());

		wlaLSNummer.setText(LPMain.getInstance().getTextRespectUISPr(
				"bes.lieferscheinnummer"));
		wlaLSDatum.setText(LPMain.getInstance().getTextRespectUISPr(
				"bes.lieferscheindatum"));
		wlaWEDatum.setText(LPMain.getInstance().getTextRespectUISPr(
				"bes.wareneingangsdatum"));

		btnOK.setText(LPMain.getInstance().getTextRespectUISPr("button.ok"));
		btnAbbrechen.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.abbrechen"));

		btnOK.addActionListener(this);
		btnAbbrechen.addActionListener(this);

		this.getContentPane().setLayout(gridBagLayout2);

		int iZeile = 0;

		// --
		iZeile++;
		this.getContentPane().add(
				wlaLSNummer,
				new GridBagConstraints(1, iZeile, 1, 1, 0, 0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 5, 2, 5),
						0, 0));
		this.getContentPane().add(
				wtfLSNummer,
				new GridBagConstraints(2, iZeile, 1, 1, 0, 0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 5, 2, 5),
						0, 0));
		iZeile++;
		this.getContentPane().add(
				wlaLSDatum,
				new GridBagConstraints(1, iZeile, 1, 1, 0, 0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 5, 2, 5),
						0, 0));
		this.getContentPane().add(
				wdfLSDatum,
				new GridBagConstraints(2, iZeile, 1, 1, 0, 0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 5, 2, 5),
						0, 0));
		iZeile++;
		this.getContentPane().add(
				wlaWEDatum,
				new GridBagConstraints(1, iZeile, 1, 1, 0, 0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 5, 2, 5),
						0, 0));
		this.getContentPane().add(
				wdfWEDatum,
				new GridBagConstraints(2, iZeile, 1, 1, 0, 0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 5, 2, 5),
						0, 0));

		iZeile++;

		this.getContentPane().add(
				new WrapperLabel(""),
				new GridBagConstraints(0, iZeile, 1, 1, 0, 0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 5, 2, 5),
						0, 0));
		iZeile++;

		this.getContentPane().add(
				wbuLager.getWrapperButton(),
				new GridBagConstraints(1, iZeile, 1, 1, 0, 0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 5, 2, 5),
						100, 0));
		this.getContentPane().add(
				wbuLager.getWrapperTextField(),
				new GridBagConstraints(2, iZeile, 1, 1, 0, 0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 5, 2, 5),
						200, 0));

		// --------------------------

		// --------------------------

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
