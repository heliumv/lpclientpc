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
import com.lp.server.partner.service.PartnerServicesFac;
import com.lp.server.partner.service.PartnerkommentarDto;
import com.lp.server.partner.service.PartnerkommentardruckDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MediaFac;
import com.lp.util.Helper;

@SuppressWarnings("static-access")
public class PanelKundeLieferantKommentar extends PanelBasis {

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

	private PartnerkommentarDto partnerkommentarDto = null;

	private WrapperSelectField wsfKommentarart = new WrapperSelectField(
			WrapperSelectField.PARTNERKOMMENTARART, getInternalFrame(), false);

	private WrapperCheckBox wcbAngebot = new WrapperCheckBox();
	private WrapperCheckBox wcbAnfrage = new WrapperCheckBox();
	private WrapperCheckBox wcbAuftrag = new WrapperCheckBox();
	private WrapperCheckBox wcbBestellung = new WrapperCheckBox();
	private WrapperCheckBox wcbLieferschein = new WrapperCheckBox();
	private WrapperCheckBox wcbRechnung = new WrapperCheckBox();

	private WrapperCheckBox wcbGutschrift = new WrapperCheckBox();
	private WrapperCheckBox wcbReklamation = new WrapperCheckBox();
	private WrapperCheckBox wcbEingangsrechnung = new WrapperCheckBox();

	private WrapperRadioButton wrbHinweis = new WrapperRadioButton();
	private WrapperRadioButton wrbMitzudrucken = new WrapperRadioButton();
	private WrapperRadioButton wrbAnhang = new WrapperRadioButton();

	public WrapperEditorField wefText = null;

	private ButtonGroup bg = new ButtonGroup();

	private WrapperMediaControl wmcBild = new WrapperMediaControlKommentar(
			getInternalFrame(), "", false);

	private boolean bKunde = true;

	public PanelKundeLieferantKommentar(InternalFrame internalFrame,
			String add2TitleI, Object pk, boolean bKunde) throws Throwable {
		super(internalFrame, add2TitleI, pk);
		this.bKunde = bKunde;
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

	public Integer getPartnerIId() {

		Integer partnerIId = null;
		if (getInternalFrame() instanceof InternalFrameKunde) {
			partnerIId = ((InternalFrameKunde) getInternalFrame())
					.getKundeDto().getPartnerIId();
		} else if (getInternalFrame() instanceof InternalFrameLieferant) {
			partnerIId = ((InternalFrameLieferant) getInternalFrame())
					.getLieferantDto().getPartnerIId();
		}

		return partnerIId;
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

		partnerkommentarDto = new PartnerkommentarDto();

	}

	protected void dto2Components() throws Throwable {

		this.setStatusbarPersonalIIdAendern(null);
		this.setStatusbarTAendern(null);
		wefText.getLpEditor().setText(null);
		wcbAnfrage.setSelected(false);
		wcbAngebot.setSelected(false);
		wcbAuftrag.setSelected(false);
		wcbBestellung.setSelected(false);
		wcbReklamation.setSelected(false);
		wcbRechnung.setSelected(false);

		wcbLieferschein.setSelected(false);
		wcbGutschrift.setSelected(false);
		wcbEingangsrechnung.setSelected(false);

		if (partnerkommentarDto.getIArt() == PartnerServicesFac.PARTNERKOMMENTARART_HINWEIS) {
			wrbHinweis.setSelected(true);
		} else if (partnerkommentarDto.getIArt() == PartnerServicesFac.PARTNERKOMMENTARART_MITDRUCKEN) {
			wrbMitzudrucken.setSelected(true);
		} else {
			wrbAnhang.setSelected(true);
		}

		wsfKommentarart.setKey(partnerkommentarDto.getPartnerkommentarartIId());

		wmcBild.setMimeType(partnerkommentarDto.getDatenformatCNr());

		this.setStatusbarPersonalIIdAendern(partnerkommentarDto
				.getPersonalIIdAendern());
		this.setStatusbarTAendern(partnerkommentarDto.getTAendern());

		if (partnerkommentarDto.getDatenformatCNr().equals(
				MediaFac.DATENFORMAT_MIMETYPE_TEXT_HTML)) {
			wmcBild.setOMediaText(partnerkommentarDto.getXKommentar());
		} else {

			wmcBild.setOMediaImage(partnerkommentarDto.getOMedia());
			wmcBild.setDateiname(partnerkommentarDto.getCDateiname());

			wefText.getLpEditor().setText(partnerkommentarDto.getXKommentar());

		}

		PartnerkommentardruckDto[] dtos = partnerkommentarDto
				.getPartnerkommentardruckDto();
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
						LocaleFac.BELEGART_REKLAMATION)) {
					wcbReklamation.setSelected(true);
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
						LocaleFac.BELEGART_GUTSCHRIFT)) {
					wcbGutschrift.setSelected(true);
				}
			}
		}

	}

	protected void components2Dto() throws Throwable {

		partnerkommentarDto.setPartnerIId(getPartnerIId());

		partnerkommentarDto.setDatenformatCNr(wmcBild.getMimeType());
		partnerkommentarDto.setBKunde(Helper.boolean2Short(bKunde));

		partnerkommentarDto
				.setPartnerkommentarartIId(wsfKommentarart.getIKey());

		if (wrbHinweis.isSelected()) {
			partnerkommentarDto
					.setIArt(PartnerServicesFac.PARTNERKOMMENTARART_HINWEIS);
		} else if (wrbMitzudrucken.isSelected()) {
			partnerkommentarDto
					.setIArt(PartnerServicesFac.PARTNERKOMMENTARART_MITDRUCKEN);
		} else {
			partnerkommentarDto
					.setIArt(PartnerServicesFac.PARTNERKOMMENTARART_ANHANG);
		}

		if (partnerkommentarDto.getDatenformatCNr().equals(
				MediaFac.DATENFORMAT_MIMETYPE_TEXT_HTML)) {

			partnerkommentarDto.setXKommentar(wmcBild.getOMediaText());

		} else {

			partnerkommentarDto.setOMedia(wmcBild.getOMediaImage());
			partnerkommentarDto.setCDateiname(wmcBild.getDateiname());
			partnerkommentarDto.setXKommentar(wefText.getText());

		}

		int anzahl = wcbAnfrage.getShort().shortValue()
				+ wcbAngebot.getShort().shortValue()
				+ wcbAuftrag.getShort().shortValue()
				+ wcbBestellung.getShort().shortValue()
				+ wcbLieferschein.getShort().shortValue()
				+ wcbRechnung.getShort().shortValue()
				+ wcbGutschrift.getShort().shortValue()
				+ wcbReklamation.getShort().shortValue()
				+ wcbEingangsrechnung.getShort().shortValue();

		PartnerkommentardruckDto[] dtos = new PartnerkommentardruckDto[anzahl];

		int iLaufend = 0;

		if (wcbAnfrage.isSelected()) {
			PartnerkommentardruckDto dto = new PartnerkommentardruckDto();
			dto.setBelegartCNr(LocaleFac.BELEGART_ANFRAGE);
			dtos[iLaufend] = dto;
			iLaufend++;
		}
		if (wcbAngebot.isSelected()) {
			PartnerkommentardruckDto dto = new PartnerkommentardruckDto();
			dto.setBelegartCNr(LocaleFac.BELEGART_ANGEBOT);
			dtos[iLaufend] = dto;
			iLaufend++;
		}
		if (wcbAuftrag.isSelected()) {
			PartnerkommentardruckDto dto = new PartnerkommentardruckDto();
			dto.setBelegartCNr(LocaleFac.BELEGART_AUFTRAG);
			dtos[iLaufend] = dto;
			iLaufend++;
		}
		if (wcbBestellung.isSelected()) {
			PartnerkommentardruckDto dto = new PartnerkommentardruckDto();
			dto.setBelegartCNr(LocaleFac.BELEGART_BESTELLUNG);
			dtos[iLaufend] = dto;
			iLaufend++;
		}
		if (wcbReklamation.isSelected()) {
			PartnerkommentardruckDto dto = new PartnerkommentardruckDto();
			dto.setBelegartCNr(LocaleFac.BELEGART_REKLAMATION);
			dtos[iLaufend] = dto;
			iLaufend++;
		}
		if (wcbEingangsrechnung.isSelected()) {
			PartnerkommentardruckDto dto = new PartnerkommentardruckDto();
			dto.setBelegartCNr(LocaleFac.BELEGART_EINGANGSRECHNUNG);
			dtos[iLaufend] = dto;
			iLaufend++;
		}
		if (wcbLieferschein.isSelected()) {
			PartnerkommentardruckDto dto = new PartnerkommentardruckDto();
			dto.setBelegartCNr(LocaleFac.BELEGART_LIEFERSCHEIN);
			dtos[iLaufend] = dto;
			iLaufend++;
		}
		if (wcbRechnung.isSelected()) {
			PartnerkommentardruckDto dto = new PartnerkommentardruckDto();
			dto.setBelegartCNr(LocaleFac.BELEGART_RECHNUNG);
			dtos[iLaufend] = dto;
			iLaufend++;
		}
		if (wcbGutschrift.isSelected()) {
			PartnerkommentardruckDto dto = new PartnerkommentardruckDto();
			dto.setBelegartCNr(LocaleFac.BELEGART_GUTSCHRIFT);
			dtos[iLaufend] = dto;
			iLaufend++;
		}
		partnerkommentarDto.setPartnerkommentardruckDto(dtos);
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

		wefText = new WrapperEditorFieldKommentar(getInternalFrame(),
				LPMain.getTextRespectUISPr("label.text"));

		getInternalFrame().addItemChangedListener(this);

		wsfKommentarart.setMandatoryField(true);

		wrbMitzudrucken.setText(LPMain.getInstance().getTextRespectUISPr(
				"artikel.mitzudruckenbei"));

		wrbHinweis.setText(LPMain.getInstance().getTextRespectUISPr(
				"partner.hinweis.auswahl"));

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
		wcbGutschrift.setText(LPMain.getInstance().getTextRespectUISPr(
				"rechnung.gutschrift"));
		wcbReklamation.setText(LPMain.getInstance().getTextRespectUISPr(
				"rekla.modulname"));
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
		/*
		 * jpaWorkingOn.add(wrbAnhang, new GridBagConstraints(0, 5, 1, 1, 0,
		 * 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new
		 * Insets(2, 2, 2, 2), 0, 0));
		 */

		if (bKunde) {
			jpaWorkingOn.add(wcbAngebot,
					new GridBagConstraints(1, 3, 1, 1, 0.1, 0.0,
							GridBagConstraints.CENTER,
							GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2,
									2), 0, 0));
			jpaWorkingOn.add(wcbAuftrag,
					new GridBagConstraints(2, 3, 1, 1, 0.1, 0.0,
							GridBagConstraints.CENTER,
							GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2,
									2), 0, 0));

			jpaWorkingOn.add(wcbLieferschein,
					new GridBagConstraints(1, 4, 1, 1, 0, 0.0,
							GridBagConstraints.CENTER,
							GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2,
									2), 0, 0));
			jpaWorkingOn.add(wcbRechnung,
					new GridBagConstraints(2, 4, 1, 1, 0, 0.0,
							GridBagConstraints.CENTER,
							GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2,
									2), 0, 0));
			jpaWorkingOn.add(wcbReklamation,
					new GridBagConstraints(1, 5, 1, 1, 0, 0.0,
							GridBagConstraints.CENTER,
							GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2,
									2), 0, 0));
			jpaWorkingOn.add(wcbGutschrift,
					new GridBagConstraints(2, 5, 1, 1, 0, 0.0,
							GridBagConstraints.CENTER,
							GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2,
									2), 0, 0));
		} else {
			jpaWorkingOn.add(wcbAnfrage,
					new GridBagConstraints(1, 3, 1, 1, 0.1, 0.0,
							GridBagConstraints.CENTER,
							GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2,
									2), 0, 0));
			jpaWorkingOn.add(wcbBestellung,
					new GridBagConstraints(2, 3, 1, 1, 0.1, 0.0,
							GridBagConstraints.CENTER,
							GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2,
									2), 0, 0));

			jpaWorkingOn.add(wcbEingangsrechnung,
					new GridBagConstraints(1, 4, 1, 1, 0, 0.0,
							GridBagConstraints.CENTER,
							GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2,
									2), 0, 0));
			jpaWorkingOn.add(wcbReklamation,
					new GridBagConstraints(2, 4, 1, 1, 0, 0.0,
							GridBagConstraints.CENTER,
							GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2,
									2), 0, 0));
			
			jpaWorkingOn.add(wcbLieferschein,
					new GridBagConstraints(1, 5, 1, 1, 0, 0.0,
							GridBagConstraints.CENTER,
							GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2,
									2), 0, 0));
			

		}

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
				.gibtEsKommentareInAnderenSprachen(partnerkommentarDto.getIId());

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

		DelegateFactory.getInstance().getPartnerServicesDelegate()
				.removePartnerkommentar(partnerkommentarDto);
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

		} else {
			partnerkommentarDto = DelegateFactory.getInstance()
					.getPartnerServicesDelegate()
					.partnerkommentarFindByPrimaryKey((Integer) key);
			dto2Components();
		}

		Integer partnerIId = getPartnerIId();
		String title = "";
		if (partnerIId != null) {
			title = DelegateFactory.getInstance().getPartnerDelegate()
					.partnerFindByPrimaryKey(partnerIId).formatFixName1Name2();
		}
		getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_AS_I_LIKE, title);

	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {
		if (allMandatoryFieldsSetDlg()) {
			components2Dto();


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

			if (partnerkommentarDto.getDatenformatCNr().equals(
					Defaults.getInstance().getSComboboxEmptyEntry())) {
				String s = "Die Art darf nicht "
						+ Defaults.getInstance().getSComboboxEmptyEntry()
						+ " sein.";

				DialogFactory.showModalDialog(LPMain.getInstance()
						.getTextRespectUISPr("lp.error"), s);
				return;

			}

			if (partnerkommentarDto.getIId() == null) {

				partnerkommentarDto.setIId(DelegateFactory.getInstance()
						.getPartnerServicesDelegate()
						.createPartnerkommentar(partnerkommentarDto));
				setKeyWhenDetailPanel(partnerkommentarDto.getIId());
			} else {
				DelegateFactory.getInstance().getPartnerServicesDelegate()
						.updatePartnerkommentar(partnerkommentarDto);
			}
			super.eventActionSave(e, true);

			/*
			 * if (getInternalFrame().getKeyWasForLockMe() == null) {
			 * getInternalFrame().setKeyWasForLockMe(
			 * internalFrameArtikel.getArtikelDto().getIId() .toString()); }
			 */
			eventYouAreSelected(false);
		}
	}
}
