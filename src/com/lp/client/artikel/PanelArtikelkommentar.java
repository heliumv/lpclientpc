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
import java.util.EventObject;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.border.Border;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperEditorField;
import com.lp.client.frame.component.WrapperEditorFieldKommentar;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperMediaControl;
import com.lp.client.frame.component.WrapperMediaControlKommentar;
import com.lp.client.frame.component.WrapperRadioButton;
import com.lp.client.frame.component.WrapperSelectField;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPButtonAction;
import com.lp.client.pc.LPMain;
import com.lp.server.artikel.service.ArtikelFac;
import com.lp.server.artikel.service.ArtikelkommentarDto;
import com.lp.server.artikel.service.ArtikelkommentarFac;
import com.lp.server.artikel.service.ArtikelkommentardruckDto;
import com.lp.server.artikel.service.ArtikelkommentarsprDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MediaFac;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;

@SuppressWarnings("static-access")
public class PanelArtikelkommentar extends PanelBasis {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// von hier ...
	private GridBagLayout gridBagLayoutAll = null;
	private JPanel jpaWorkingOn = new JPanel();
	private JPanel jpaButtonAction = null;
	private Border border = null;
	private GridBagLayout gridBagLayoutWorkingPanel = null;
	private InternalFrameArtikel internalFrameArtikel = null;
	private WrapperLabel wlaLagerplatz = new WrapperLabel();
	private WrapperTextField wtfLagerplatz = new WrapperTextField();
	private ArtikelkommentarDto artikelkommentarDto = null;

	private WrapperSelectField wsfKommentarart = new WrapperSelectField(
			WrapperSelectField.ARTIKELKOMMENTARART, getInternalFrame(), false);

	private WrapperCheckBox wcbAngebot = new WrapperCheckBox();
	private WrapperCheckBox wcbAnfrage = new WrapperCheckBox();
	private WrapperCheckBox wcbAuftrag = new WrapperCheckBox();
	private WrapperCheckBox wcbBestellung = new WrapperCheckBox();
	private WrapperCheckBox wcbLieferschein = new WrapperCheckBox();
	private WrapperCheckBox wcbRechnung = new WrapperCheckBox();
	private WrapperCheckBox wcbFertigung = new WrapperCheckBox();
	private WrapperCheckBox wcbStuecklisten = new WrapperCheckBox();
	private WrapperCheckBox wcbWareneingang = new WrapperCheckBox();
	private WrapperCheckBox wcbEingangsrechnung = new WrapperCheckBox();

	private WrapperRadioButton wrbHinweis = new WrapperRadioButton();
	private WrapperRadioButton wrbMitzudrucken = new WrapperRadioButton();
	private WrapperRadioButton wrbAnhang = new WrapperRadioButton();

	public WrapperEditorField wefText = null;

	private ButtonGroup bg = new ButtonGroup();

	private WrapperMediaControl wmcBild = new WrapperMediaControlKommentar(
			getInternalFrame(), "", true);

	public PanelArtikelkommentar(InternalFrame internalFrame,
			String add2TitleI, Object pk) throws Throwable {
		super(internalFrame, add2TitleI, pk);
		internalFrameArtikel = (InternalFrameArtikel) internalFrame;
		jbInit();
		setDefaults();
		initComponents();
		enableAllComponents(this, false);
	}

	protected void setDefaults() {
		wefText.getLpEditor().setText(null);
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wsfKommentarart;
	}

	public void eventActionText(ActionEvent e) throws Throwable {

		if (!wmcBild.getMimeType().startsWith("image")) {
			DialogFactory
					.showModalDialog(
							LPMain.getTextRespectUISPr("lp.error"),
							LPMain.getTextRespectUISPr("artikel.texteingabezuartikelbild.error"));
			return;
		}

		super.eventActionText(e);
		if (getLockedstateDetailMainKey().getIState() == PanelBasis.LOCK_IS_LOCKED_BY_ME) {
			// Editor auf Read Only schalten

		}

		getInternalFrame().showPanelEditor(wefText, this.getAdd2Title(),
				wefText.getLpEditor().getText(),
				getLockedstateDetailMainKey().getIState());

	}

	public void eventActionNew(EventObject eventObject, boolean bLockMeI,
			boolean bNeedNoNewI) throws Throwable {
		super.eventActionNew(eventObject, true, false);
		leereAlleFelder(this);

		wmcBild.setOMediaImage(null);
		wmcBild.setDateiname(null);
		wefText.getLpEditor().setText(null);

		artikelkommentarDto = new ArtikelkommentarDto();
		artikelkommentarDto
				.setArtikelkommentarsprDto(new ArtikelkommentarsprDto());
	}

	protected void dto2Components() throws Throwable {

		this.setStatusbarPersonalIIdAendern(null);
		this.setStatusbarTAendern(null);
		wefText.getLpEditor().setText(null);
		wcbAnfrage.setSelected(false);
		wcbAngebot.setSelected(false);
		wcbAuftrag.setSelected(false);
		wcbBestellung.setSelected(false);
		wcbWareneingang.setSelected(false);
		wcbRechnung.setSelected(false);
		wcbFertigung.setSelected(false);
		wcbLieferschein.setSelected(false);
		wcbStuecklisten.setSelected(false);
		wcbEingangsrechnung.setSelected(false);

		if (artikelkommentarDto.getIArt() == ArtikelkommentarFac.ARTIKELKOMMENTARART_HINWEIS) {
			wrbHinweis.setSelected(true);
		} else if (artikelkommentarDto.getIArt() == ArtikelkommentarFac.ARTIKELKOMMENTARART_MITDRUCKEN) {
			wrbMitzudrucken.setSelected(true);
		} else {
			wrbAnhang.setSelected(true);
		}

		wsfKommentarart.setKey(artikelkommentarDto.getArtikelkommentarartIId());

		wmcBild.setMimeType(artikelkommentarDto.getDatenformatCNr());

		if (artikelkommentarDto.getArtikelkommentarsprDto() != null) {

			this.setStatusbarPersonalIIdAendern(artikelkommentarDto
					.getArtikelkommentarsprDto().getPersonalIIdAendern());
			this.setStatusbarTAendern(artikelkommentarDto
					.getArtikelkommentarsprDto().getTAendern());

			if (artikelkommentarDto.getDatenformatCNr().equals(
					MediaFac.DATENFORMAT_MIMETYPE_TEXT_HTML)) {
				wmcBild.setOMediaText(artikelkommentarDto
						.getArtikelkommentarsprDto().getXKommentar());
			} else {
				if (artikelkommentarDto.getArtikelkommentarsprDto() != null) {
					wmcBild.setOMediaImage(artikelkommentarDto
							.getArtikelkommentarsprDto().getOMedia());
					wmcBild.setDateiname(artikelkommentarDto
							.getArtikelkommentarsprDto().getCDateiname());
					wmcBild.setDefaultbildFeld(artikelkommentarDto
							.getBDefaultbild());

					wefText.getLpEditor().setText(
							artikelkommentarDto.getArtikelkommentarsprDto()
									.getXKommentar());

				} else {
					wmcBild.setOMediaImage(null);
					wmcBild.setDateiname(null);
					wmcBild.setDefaultbildFeld(artikelkommentarDto
							.getBDefaultbild());

				}
			}
		}

		ArtikelkommentardruckDto[] dtos = artikelkommentarDto
				.getArtikelkommentardruckDto();
		if (dtos != null) {
			for (int i = 0; i < dtos.length; i++) {
				if (dtos[i].getBelegartCNr().equals(LocaleFac.BELEGART_ANFRAGE)) {
					wcbAnfrage.setSelected(true);
				} else if (dtos[i].getBelegartCNr().equals(
						LocaleFac.BELEGART_ANGEBOT)) {
					wcbAngebot.setSelected(true);
				} else if (dtos[i].getBelegartCNr().equals(
						LocaleFac.BELEGART_AUFTRAG)) {
					wcbAuftrag.setSelected(true);
				} else if (dtos[i].getBelegartCNr().equals(
						LocaleFac.BELEGART_BESTELLUNG)) {
					wcbBestellung.setSelected(true);
				} else if (dtos[i].getBelegartCNr().equals(
						LocaleFac.BELEGART_WARENEINGANG)) {
					wcbWareneingang.setSelected(true);
				} else if (dtos[i].getBelegartCNr().equals(
						LocaleFac.BELEGART_EINGANGSRECHNUNG)) {
					wcbEingangsrechnung.setSelected(true);
				} else if (dtos[i].getBelegartCNr().equals(
						LocaleFac.BELEGART_LIEFERSCHEIN)) {
					wcbLieferschein.setSelected(true);
				} else if (dtos[i].getBelegartCNr().equals(
						LocaleFac.BELEGART_RECHNUNG)) {
					wcbRechnung.setSelected(true);
				} else if (dtos[i].getBelegartCNr().equals(
						LocaleFac.BELEGART_LOS)) {
					wcbFertigung.setSelected(true);
				} else if (dtos[i].getBelegartCNr().equals(
						LocaleFac.BELEGART_STUECKLISTE)) {
					wcbStuecklisten.setSelected(true);
				}
			}
		}

	}

	protected void components2Dto() throws Throwable {
		artikelkommentarDto.setArtikelIId(internalFrameArtikel.getArtikelDto()
				.getIId());

		artikelkommentarDto.setDatenformatCNr(wmcBild.getMimeType());

		artikelkommentarDto.setBDefaultbild(wmcBild.getDefaultbildFeld());

		artikelkommentarDto
				.setArtikelkommentarartIId(wsfKommentarart.getIKey());

		if (wrbHinweis.isSelected()) {
			artikelkommentarDto
					.setIArt(ArtikelkommentarFac.ARTIKELKOMMENTARART_HINWEIS);
		} else if (wrbMitzudrucken.isSelected()) {
			artikelkommentarDto
					.setIArt(ArtikelkommentarFac.ARTIKELKOMMENTARART_MITDRUCKEN);
		} else {
			artikelkommentarDto
					.setIArt(ArtikelkommentarFac.ARTIKELKOMMENTARART_ANHANG);
		}

		if (artikelkommentarDto.getDatenformatCNr().equals(
				MediaFac.DATENFORMAT_MIMETYPE_TEXT_HTML)) {
			if (artikelkommentarDto.getArtikelkommentarsprDto() != null) {
				artikelkommentarDto.getArtikelkommentarsprDto().setXKommentar(
						wmcBild.getOMediaText());
			} else {
				// PJ SP382
				artikelkommentarDto
						.setArtikelkommentarsprDto(new ArtikelkommentarsprDto());
				artikelkommentarDto.getArtikelkommentarsprDto().setXKommentar(
						wmcBild.getOMediaText());
			}
		} else {

			artikelkommentarDto.getArtikelkommentarsprDto().setOMedia(
					wmcBild.getOMediaImage());
			artikelkommentarDto.getArtikelkommentarsprDto().setCDateiname(
					wmcBild.getDateiname());
			artikelkommentarDto.getArtikelkommentarsprDto().setXKommentar(
					wefText.getText());

		}

		int anzahl = wcbAnfrage.getShort().shortValue()
				+ wcbAngebot.getShort().shortValue()
				+ wcbAuftrag.getShort().shortValue()
				+ wcbBestellung.getShort().shortValue()
				+ wcbLieferschein.getShort().shortValue()
				+ wcbRechnung.getShort().shortValue()
				+ wcbFertigung.getShort().shortValue()
				+ wcbStuecklisten.getShort().shortValue()
				+ wcbWareneingang.getShort().shortValue()
				+ wcbEingangsrechnung.getShort().shortValue();

		ArtikelkommentardruckDto[] dtos = new ArtikelkommentardruckDto[anzahl];

		int iLaufend = 0;

		if (wcbAnfrage.isSelected()) {
			ArtikelkommentardruckDto dto = new ArtikelkommentardruckDto();
			dto.setBelegartCNr(LocaleFac.BELEGART_ANFRAGE);
			dtos[iLaufend] = dto;
			iLaufend++;
		}
		if (wcbAngebot.isSelected()) {
			ArtikelkommentardruckDto dto = new ArtikelkommentardruckDto();
			dto.setBelegartCNr(LocaleFac.BELEGART_ANGEBOT);
			dtos[iLaufend] = dto;
			iLaufend++;
		}
		if (wcbAuftrag.isSelected()) {
			ArtikelkommentardruckDto dto = new ArtikelkommentardruckDto();
			dto.setBelegartCNr(LocaleFac.BELEGART_AUFTRAG);
			dtos[iLaufend] = dto;
			iLaufend++;
		}
		if (wcbBestellung.isSelected()) {
			ArtikelkommentardruckDto dto = new ArtikelkommentardruckDto();
			dto.setBelegartCNr(LocaleFac.BELEGART_BESTELLUNG);
			dtos[iLaufend] = dto;
			iLaufend++;
		}
		if (wcbWareneingang.isSelected()) {
			ArtikelkommentardruckDto dto = new ArtikelkommentardruckDto();
			dto.setBelegartCNr(LocaleFac.BELEGART_WARENEINGANG);
			dtos[iLaufend] = dto;
			iLaufend++;
		}
		if (wcbEingangsrechnung.isSelected()) {
			ArtikelkommentardruckDto dto = new ArtikelkommentardruckDto();
			dto.setBelegartCNr(LocaleFac.BELEGART_EINGANGSRECHNUNG);
			dtos[iLaufend] = dto;
			iLaufend++;
		}
		if (wcbLieferschein.isSelected()) {
			ArtikelkommentardruckDto dto = new ArtikelkommentardruckDto();
			dto.setBelegartCNr(LocaleFac.BELEGART_LIEFERSCHEIN);
			dtos[iLaufend] = dto;
			iLaufend++;
		}
		if (wcbRechnung.isSelected()) {
			ArtikelkommentardruckDto dto = new ArtikelkommentardruckDto();
			dto.setBelegartCNr(LocaleFac.BELEGART_RECHNUNG);
			dtos[iLaufend] = dto;
			iLaufend++;
		}
		if (wcbFertigung.isSelected()) {
			ArtikelkommentardruckDto dto = new ArtikelkommentardruckDto();
			dto.setBelegartCNr(LocaleFac.BELEGART_LOS);
			dtos[iLaufend] = dto;
			iLaufend++;
		}
		if (wcbStuecklisten.isSelected()) {
			ArtikelkommentardruckDto dto = new ArtikelkommentardruckDto();
			dto.setBelegartCNr(LocaleFac.BELEGART_STUECKLISTE);
			dtos[iLaufend] = dto;
			iLaufend++;
		}
		artikelkommentarDto.setArtikelkommentardruckDto(dtos);
	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;
		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {

		}
	}

	private void jbInit() throws Throwable {
		// von hier ...
		border = BorderFactory.createEmptyBorder(10, 10, 10, 10);
		setBorder(border);
		// das Aussenpanel hat immer das Gridbaglayout.
		gridBagLayoutAll = new GridBagLayout();
		this.setLayout(gridBagLayoutAll);

		// Actionpanel von Oberklasse holen und anhaengen.
		jpaButtonAction = getToolsPanel();
		this.setActionMap(null);

		wlaLagerplatz.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.lagerplatz"));

		wefText = new WrapperEditorFieldKommentar(getInternalFrame(),
				LPMain.getTextRespectUISPr("label.text"));

		/*
		 * wbuLagerplatz.setActionCommand(this.
		 * ACTION_SPECIAL_LAGERPLATZ_FROM_LISTE);
		 * wbuLagerplatz.addActionListener(this);
		 */
		wtfLagerplatz.setColumnsMax(ArtikelFac.MAX_LAGERPLATZ_NAME);
		wtfLagerplatz.setMandatoryField(true);
		getInternalFrame().addItemChangedListener(this);

		wsfKommentarart.setMandatoryField(true);

		wrbMitzudrucken.setText(LPMain.getInstance().getTextRespectUISPr(
				"artikel.mitzudruckenbei"));

		wrbHinweis.setText(LPMain.getInstance().getTextRespectUISPr(
				"artikel.hinweis.auswahl"));

		wrbAnhang.setText(LPMain.getInstance().getTextRespectUISPr(
				"label.anhang"));

		wcbAnfrage.setText(LPMain.getInstance().getTextRespectUISPr(
				"anf.anfrage"));
		wcbAngebot.setText(LPMain.getInstance().getTextRespectUISPr(
				"angb.angebot"));
		wcbAuftrag.setText(LPMain.getInstance().getTextRespectUISPr(
				"auft.auftrag"));
		wcbBestellung.setText(LPMain.getInstance().getTextRespectUISPr(
				"best.title.bestellung"));
		wcbLieferschein.setText(LPMain.getInstance().getTextRespectUISPr(
				"label.lieferschein"));
		wcbRechnung.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.rechnung.modulname"));
		wcbFertigung.setText(LPMain.getInstance().getTextRespectUISPr(
				"fert.modulname"));
		wcbStuecklisten.setText(LPMain.getInstance().getTextRespectUISPr(
				"stkl.stueckliste"));
		wcbWareneingang.setText(LPMain.getInstance().getTextRespectUISPr(
				"bes.title.panel.wareneingang"));
		wcbEingangsrechnung.setText(LPMain.getInstance().getTextRespectUISPr(
				"er.modulname"));

		bg.add(wrbHinweis);
		bg.add(wrbMitzudrucken);
		bg.add(wrbAnhang);
		wrbMitzudrucken.setSelected(true);

		this.add(jpaButtonAction, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0,
						0, 0, 0), 0, 0));

		// jetzt meine felder
		jpaWorkingOn = new JPanel();
		gridBagLayoutWorkingPanel = new GridBagLayout();
		jpaWorkingOn.setLayout(gridBagLayoutWorkingPanel);
		this.add(jpaWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
				GridBagConstraints.SOUTHEAST, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
		this.add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 1.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
		jpaWorkingOn.add(wsfKommentarart, new GridBagConstraints(0, 1, 1, 1,
				0.08, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wsfKommentarart.getWrapperTextField(),
				new GridBagConstraints(1, 1, 2, 1, 0.1, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
						0, 0));

		jpaWorkingOn.add(wrbMitzudrucken, new GridBagConstraints(0, 3, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wrbHinweis, new GridBagConstraints(0, 4, 1, 1, 0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wrbAnhang, new GridBagConstraints(0, 5, 1, 1, 0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wcbAnfrage, new GridBagConstraints(2, 3, 1, 1, 0.1,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wcbAngebot, new GridBagConstraints(1, 3, 1, 1, 0.1,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wcbLieferschein, new GridBagConstraints(1, 5, 1, 1,
				0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wcbWareneingang, new GridBagConstraints(2, 5, 1, 1,
				0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wcbRechnung, new GridBagConstraints(1, 6, 1, 1, 0.1,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wcbBestellung, new GridBagConstraints(2, 4, 1, 1, 0.1,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wcbAuftrag, new GridBagConstraints(1, 4, 1, 1, 0.1,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wcbStuecklisten, new GridBagConstraints(3, 3, 1, 1,
				0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wcbFertigung, new GridBagConstraints(3, 4, 1, 1, 0.1,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wcbEingangsrechnung, new GridBagConstraints(2, 6, 1,
				1, 0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wmcBild, new GridBagConstraints(0, 7, 4, 1, 1, 1,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						2, 2, 2, 2), 0, 0));

		String[] aWhichButtonIUse = { ACTION_UPDATE, ACTION_SAVE,
				ACTION_DELETE, ACTION_DISCARD, ACTION_TEXT };

		enableToolsPanelButtons(aWhichButtonIUse);

		// PJ18549
		LPButtonAction lpba = getHmOfButtons().get(ACTION_TEXT);
		lpba.getButton().setToolTipText(
				LPMain.getInstance().getTextRespectUISPr(
						"artikel.texteingabezuartikelbild"));

	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_ARTIKEL;
	}

	protected void eventActionDelete(ActionEvent e,
			boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {

		boolean b = DelegateFactory
				.getInstance()
				.getArtikelkommentarDelegate()
				.gibtEsKommentareInAnderenSprachen(artikelkommentarDto.getIId());

		if (b == true) {
			boolean fortfahren = DialogFactory
					.showModalJaNeinDialog(
							getInternalFrame(),
							LPMain.getInstance()
									.getTextRespectUISPr(
											"artikel.error.kommentareinanderensprachenwerdengeloescht"),
							LPMain.getInstance()
									.getTextRespectUISPr("lp.frage"));

			if (fortfahren == false) {
				return;
			}

		}

		DelegateFactory.getInstance().getArtikelkommentarDelegate()
				.removeArtikelkommentar(artikelkommentarDto);
		this.setKeyWhenDetailPanel(null);
		super.eventActionDelete(e, false, false);

	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {

	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {
		super.eventYouAreSelected(false);
		Object key = getKeyWhenDetailPanel();

		if (key == null || (key.equals(LPMain.getLockMeForNew()))) {
			leereAlleFelder(this);
			clearStatusbar();
			if (key == null) {
				wmcBild.setOMediaImage(null);
				wmcBild.setDateiname("");
			}

			if (key != null && key.equals(LPMain.getLockMeForNew())) {

				// Default MWST-Satz setzen
				ParametermandantDto parameter = (ParametermandantDto) DelegateFactory
						.getInstance()
						.getParameterDelegate()
						.getParametermandant(
								ParameterFac.PARAMETER_DEFAULT_ARTIKELKOMMENTAR_IST_HINWEIS,
								ParameterFac.KATEGORIE_ARTIKEL,
								LPMain.getTheClient().getMandant());

				if ((Boolean) parameter.getCWertAsObject()) {
					wrbHinweis.setSelected(true);
				}

			}

		} else {
			artikelkommentarDto = DelegateFactory.getInstance()
					.getArtikelkommentarDelegate()
					.artikelkommentarFindByPrimaryKey((Integer) key);
			dto2Components();
		}
	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {
		if (allMandatoryFieldsSetDlg()) {
			components2Dto();

			// PJ 17092
			if (wrbMitzudrucken.isSelected() || wrbHinweis.isSelected()) {
				if (artikelkommentarDto.getDatenformatCNr().equals(
						MediaFac.DATENFORMAT_MIMETYPE_TEXT_HTML)
						|| artikelkommentarDto.getDatenformatCNr().equals(
								MediaFac.DATENFORMAT_MIMETYPE_IMAGE_GIF)
						|| artikelkommentarDto.getDatenformatCNr().equals(
								MediaFac.DATENFORMAT_MIMETYPE_IMAGE_JPEG)
						|| artikelkommentarDto.getDatenformatCNr().equals(
								MediaFac.DATENFORMAT_MIMETYPE_IMAGE_PNG)
						|| artikelkommentarDto.getDatenformatCNr().equals(
								MediaFac.DATENFORMAT_MIMETYPE_IMAGE_TIFF)) {

					// IST OK
				} else {

					if (wrbMitzudrucken.isSelected()
							&& artikelkommentarDto
									.getArtikelkommentardruckDto().length == 1
							&& artikelkommentarDto
									.getArtikelkommentardruckDto()[0]
									.getBelegartCNr().equals(
											LocaleFac.BELEGART_LOS)
							&& artikelkommentarDto.getDatenformatCNr().equals(
									MediaFac.DATENFORMAT_MIMETYPE_APP_PDF)) {

						// SEIT PJ18377 darf bei Fertigung PDF verwendet werden
					} else {

						DialogFactory
								.showModalDialog(
										LPMain.getInstance()
												.getTextRespectUISPr(
														"lp.hinweis"),
										LPMain.getInstance()
												.getTextRespectUISPr(
														"artikel.aritkelkommentar.mitzudruckenbei.falschertyp"));
						return;
					}
				}
			}

			/*
			 * PJ 15045
			 * 
			 * // Wenn Hinweis, dann darf nur TEXT/HTML ausgew&auml;hlt werden
			 * if (wrbHinweis.isSelected() &&
			 * !artikelkommentarDto.getDatenformatCNr().equals(
			 * MediaFac.DATENFORMAT_MIMETYPE_TEXT_HTML)) {
			 * com.lp.client.frame.dialog.DialogFactory.showModalDialog(LPMain
			 * .getInstance().getTextRespectUISPr("lp.error"), LPMain
			 * .getInstance().getTextRespectUISPr(
			 * "artikel.artikelkommentar.error"));
			 * 
			 * return; }
			 */

			if (artikelkommentarDto.getDatenformatCNr().equals(
					Defaults.getInstance().getSComboboxEmptyEntry())) {
				String s = "Die Art darf nicht "
						+ Defaults.getInstance().getSComboboxEmptyEntry()
						+ " sein.";

				DialogFactory.showModalDialog(LPMain.getInstance()
						.getTextRespectUISPr("lp.error"), s);
				return;

			}

			if (artikelkommentarDto.getIId() == null) {
				artikelkommentarDto.setArtikelIId(internalFrameArtikel
						.getArtikelDto().getIId());
				artikelkommentarDto.setIId(DelegateFactory.getInstance()
						.getArtikelkommentarDelegate()
						.createArtikelkommentar(artikelkommentarDto));
				setKeyWhenDetailPanel(artikelkommentarDto.getIId());
			} else {
				DelegateFactory.getInstance().getArtikelkommentarDelegate()
						.updateArtikelkommentar(artikelkommentarDto);
			}
			super.eventActionSave(e, true);

			if (getInternalFrame().getKeyWasForLockMe() == null) {
				getInternalFrame().setKeyWasForLockMe(
						internalFrameArtikel.getArtikelDto().getIId()
								.toString());
			}
			eventYouAreSelected(false);
		}
	}
}
