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
package com.lp.client.artikel;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.math.BigDecimal;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperRadioButton;
import com.lp.client.frame.component.WrapperSelectField;
import com.lp.client.frame.component.WrapperSnrChnrField;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikelfehlmengeDto;
import com.lp.server.artikel.service.LagerDto;
import com.lp.server.artikel.service.LagerFac;
import com.lp.server.artikel.service.SeriennrChargennrMitMengeDto;
import com.lp.server.fertigung.service.LosDto;
import com.lp.server.fertigung.service.LossollarbeitsplanDto;
import com.lp.server.fertigung.service.LossollmaterialDto;
import com.lp.server.system.service.LocaleFac;

@SuppressWarnings("static-access")
public class DialogLagercockpitUmbuchen extends JDialog implements
		ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private GridBagLayout gridBagLayout2 = new GridBagLayout();

	private JButton btnOK = new JButton();
	private JButton btnAbbrechen = new JButton();

	private WrapperRadioButton wrbLos = new WrapperRadioButton();
	private WrapperRadioButton wrbLager = new WrapperRadioButton();

	private ButtonGroup bgOption = new ButtonGroup();

	private WrapperLabel wlaMenge = new WrapperLabel();
	private WrapperNumberField wnfMenge = new WrapperNumberField();

	private WrapperLabel wlaLos = new WrapperLabel();
	private WrapperTextField wtfLos = new WrapperTextField();

	private WrapperSelectField wbuLager = null;

	WrapperSnrChnrField wtfSnrChnr = null;

	private InternalFrameArtikel internalFrame = null;
	private ArtikelDto artikelDto = null;
	private Integer fehlmengeIId = null;
	private Integer lossollmaterialIId = null;
	private Integer lagerIId_Wareneingang = null;

	public DialogLagercockpitUmbuchen(Integer artikelIId, Integer fehlmengeIId,
			InternalFrameArtikel internalFrame) throws Throwable {
		super(LPMain.getInstance().getDesktop(), LPMain.getInstance()
				.getTextRespectUISPr("ww.lagercockpit.material.umbuchen"), true);
		this.internalFrame = internalFrame;
		artikelDto = DelegateFactory.getInstance().getArtikelDelegate()
				.artikelFindByPrimaryKey(artikelIId);
		this.fehlmengeIId = fehlmengeIId;

		LagerDto lagerDtoWareneingang = DelegateFactory
				.getInstance()
				.getLagerDelegate()
				.lagerFindByMandantCNrLagerartCNr(
						LPMain.getTheClient().getMandant(),
						LagerFac.LAGERART_WARENEINGANG);
		this.lagerIId_Wareneingang = lagerDtoWareneingang.getIId();
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		jbInit();
		HelperClient.memberVariablenEinerKlasseBenennen(this);

		this.setSize(400, 250);

	}

	public void actionPerformed(ActionEvent e) {

		if (e.getSource().equals(btnAbbrechen)) {

			this.setVisible(false);
		} else if (e.getSource().equals(wrbLager)) {
			wbuLager.setEnabled(true);
			wbuLager.setMandatoryField(true);
			wtfLos.setMandatoryField(false);

			try {
				LagerDto lDto = DelegateFactory
						.getInstance()
						.getLagerDelegate()
						.getLagerDesErstenArtikellagerplatzes(
								artikelDto.getIId());

				if (lDto != null) {
					wbuLager.setKey(lDto.getIId());
				}
			} catch (Throwable e1) {
				internalFrame.handleException(e1, false);
			}

		} else if (e.getSource().equals(wrbLos)) {
			wbuLager.setEnabled(false);
			wbuLager.setMandatoryField(false);
			wtfLos.setMandatoryField(true);
		} else if (e.getSource().equals(btnOK)) {
			try {
				if (wnfMenge.getBigDecimal() == null) {
					DialogFactory
							.showModalDialog(
									LPMain.getTextRespectUISPr("lp.error"),
									LPMain.getTextRespectUISPr("lp.pflichtfelder.ausfuellen"));
					return;
				}

				List<SeriennrChargennrMitMengeDto> l = null;
				if (artikelDto.istArtikelSnrOderchargentragend()) {
					l = wtfSnrChnr.getSeriennummern();
				}

				if (wrbLager.isSelected()) {

					if (wbuLager.getIKey() == null) {
						DialogFactory
								.showModalDialog(
										LPMain.getTextRespectUISPr("lp.error"),
										LPMain.getTextRespectUISPr("lp.pflichtfelder.ausfuellen"));
						return;
					}

					DelegateFactory
							.getInstance()
							.getLagerDelegate()
							.bucheUm(
									artikelDto.getIId(),
									lagerIId_Wareneingang,
									artikelDto.getIId(),
									wbuLager.getIKey(),
									wnfMenge.getBigDecimal(),
									l,
									"Umbuchung Lagercockpit",
									DelegateFactory
											.getInstance()
											.getLagerDelegate()
											.getGemittelterGestehungspreisEinesLagers(
													artikelDto.getIId(),

													lagerIId_Wareneingang));
				} else if (wrbLos.isSelected() && lossollmaterialIId != null) {

					if (wtfLos.getText() == null) {
						DialogFactory
								.showModalDialog(
										LPMain.getTextRespectUISPr("lp.error"),
										LPMain.getTextRespectUISPr("lp.pflichtfelder.ausfuellen"));
						return;
					}

					DelegateFactory
							.getInstance()
							.getFertigungDelegate()
							.vonQuelllagerUmbuchenUndAnsLosAusgeben(
									lagerIId_Wareneingang, lossollmaterialIId,
									wnfMenge.getBigDecimal(), l);

					// -am Server umbuchen und ausgeben
				}
			} catch (Throwable e1) {
				internalFrame.handleException(e1, false);
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

		bgOption.add(wrbLos);
		bgOption.add(wrbLager);

		wrbLos.addActionListener(this);
		wrbLager.addActionListener(this);
		wbuLager = new WrapperSelectField(WrapperSelectField.LAGER,
				internalFrame, false);

		BigDecimal lagertandWELager = DelegateFactory.getInstance()
				.getLagerDelegate()
				.getLagerstand(artikelDto.getIId(), lagerIId_Wareneingang);

		wnfMenge.setMandatoryField(true);

		if (fehlmengeIId != null) {
			wrbLos.setSelected(true);
			wbuLager.setEnabled(false);

			ArtikelfehlmengeDto fmDto = DelegateFactory.getInstance()
					.getFehlmengeDelegate()
					.artikelfehlmengeFindByPrimaryKey(fehlmengeIId);
			if (fmDto.getCBelegartnr().equals(LocaleFac.BELEGART_LOS)) {
				LossollmaterialDto lossollmaterialDto = DelegateFactory
						.getInstance()
						.getFertigungDelegate()
						.lossollmaterialFindByPrimaryKey(
								fmDto.getIBelegartpositionid());
				lossollmaterialIId = lossollmaterialDto.getIId();
				LosDto losDto = DelegateFactory.getInstance()
						.getFertigungDelegate()
						.losFindByPrimaryKey(lossollmaterialDto.getLosIId());

				String losnummerProjekt = losDto.getCNr();
				if (losDto.getCProjekt() != null) {
					losnummerProjekt += " " + losDto.getCProjekt();
				}
				wtfLos.setText(losnummerProjekt);

				if (fmDto.getNMenge().doubleValue() > lagertandWELager
						.doubleValue()) {
					wnfMenge.setBigDecimal(lagertandWELager);
				} else {
					wnfMenge.setBigDecimal(fmDto.getNMenge());
				}
			}

		} else {
			wrbLos.setEnabled(false);
			wrbLager.setSelected(true);
			wbuLager.setMandatoryField(true);
			wnfMenge.setBigDecimal(lagertandWELager);

			LagerDto lDto = DelegateFactory.getInstance().getLagerDelegate()
					.getLagerDesErstenArtikellagerplatzes(artikelDto.getIId());

			if (lDto != null) {
				wbuLager.setKey(lDto.getIId());
			}

		}

		wlaMenge.setText(LPMain.getInstance().getTextRespectUISPr("lp.menge"));

		wlaLos.setText(LPMain.getInstance().getTextRespectUISPr(
				"fert.tab.unten.los.title"));

		wtfSnrChnr = new WrapperSnrChnrField(internalFrame,false);
		wtfSnrChnr.setWnfBelegMenge(wnfMenge);

		wtfLos.setActivatable(false);

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
				wlaMenge,
				new GridBagConstraints(1, iZeile, 1, 1, 0, 0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 5, 2, 5),
						0, 0));
		this.getContentPane().add(
				wnfMenge,
				new GridBagConstraints(2, iZeile, 1, 1, 0, 0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 5, 2, 5),
						0, 0));
		if (artikelDto.istArtikelSnrOderchargentragend()) {
			wtfSnrChnr.setMandatoryField(true);
			wtfSnrChnr.setArtikelIdLagerId(artikelDto, lagerIId_Wareneingang);
			iZeile++;
			this.getContentPane().add(
					wtfSnrChnr.getButtonSnrAuswahl(),
					new GridBagConstraints(1, iZeile, 1, 1, 0, 0,
							GridBagConstraints.CENTER,
							GridBagConstraints.HORIZONTAL, new Insets(2, 5, 2,
									5), 0, 0));
			this.getContentPane().add(
					wtfSnrChnr,
					new GridBagConstraints(2, iZeile, 1, 1, 0, 0,
							GridBagConstraints.CENTER,
							GridBagConstraints.HORIZONTAL, new Insets(2, 5, 2,
									5), 0, 0));
		}
		iZeile++;

		this.getContentPane().add(
				new WrapperLabel(""),
				new GridBagConstraints(0, iZeile, 1, 1, 0, 0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 5, 2, 5),
						0, 0));
		iZeile++;
		this.getContentPane().add(
				wrbLager,
				new GridBagConstraints(0, iZeile, 1, 1, 0, 0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 5, 2, 5),
						0, 0));
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
		iZeile++;
		this.getContentPane().add(
				wrbLos,
				new GridBagConstraints(0, iZeile, 1, 1, 0, 0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 5, 2, 5),
						0, 0));
		this.getContentPane().add(
				wlaLos,
				new GridBagConstraints(1, iZeile, 1, 1, 0, 0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 5, 2, 5),
						0, 0));
		this.getContentPane().add(
				wtfLos,
				new GridBagConstraints(2, iZeile, 1, 1, 0, 0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 5, 2, 5),
						0, 0));

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
