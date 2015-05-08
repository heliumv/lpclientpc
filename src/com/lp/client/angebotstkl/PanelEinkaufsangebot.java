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
package com.lp.client.angebotstkl;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.EventObject;

import javax.swing.JComponent;
import javax.swing.JPanel;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperDateField;
import com.lp.client.frame.component.WrapperGotoButton;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.partner.PartnerFilterFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.angebotstkl.service.EinkaufsangebotDto;
import com.lp.server.partner.service.AnsprechpartnerDto;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.partner.service.PartnerFac;
import com.lp.server.util.Facade;

@SuppressWarnings("static-access")
public class PanelEinkaufsangebot extends PanelBasis {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private EinkaufsangebotDto einkaufsangebotDto = null;
	private KundeDto kundeDto = null;
	private InternalFrameAngebotstkl internalFrameAngebotstkl = null;
	private JPanel jpaWorkingOn = new JPanel();
	private JPanel jpaButtonAction = new JPanel();
	private GridBagLayout gridBagLayoutWorkingPanel = new GridBagLayout();
	private GridBagLayout gridBagLayoutAll = new GridBagLayout();

	private PanelQueryFLR panelQueryFLRKunde = null;
	private PanelQueryFLR panelQueryFLRAnsprechpartner = null;

	private WrapperGotoButton wbuKunde = new WrapperGotoButton(
			WrapperGotoButton.GOTO_KUNDE_AUSWAHL);
	private WrapperTextField wtfKunde = new WrapperTextField();

	private WrapperButton wbuAnsprechpartner = new WrapperButton();
	private WrapperTextField wtfAnsprechpartner = new WrapperTextField();

	private WrapperLabel wlaProjekt = new WrapperLabel();
	private WrapperTextField wtfProjekt = new WrapperTextField();

	private WrapperLabel wlaMenge1 = new WrapperLabel();
	private WrapperNumberField wnfMenge1 = new WrapperNumberField();
	private WrapperLabel wlaMenge2 = new WrapperLabel();
	private WrapperNumberField wnfMenge2 = new WrapperNumberField();
	private WrapperLabel wlaMenge3 = new WrapperLabel();
	private WrapperNumberField wnfMenge3 = new WrapperNumberField();
	private WrapperLabel wlaMenge4 = new WrapperLabel();
	private WrapperNumberField wnfMenge4 = new WrapperNumberField();
	private WrapperLabel wlaMenge5 = new WrapperLabel();
	private WrapperNumberField wnfMenge5 = new WrapperNumberField();

	private WrapperLabel wlaBelegdatum = new WrapperLabel();
	private WrapperDateField wdfBelegdatum = new WrapperDateField();

	static final public String ACTION_SPECIAL_KUNDE_FROM_LISTE = "action_kunde_from_liste";
	public static final String ACTION_SPECIAL_ANSPRECHPARTNER_KUNDE = "action_special_ansprechpartner_kunde";
	Integer partnerIId;

	public InternalFrameAngebotstkl getInternalFrameAngebotstkl() {
		return internalFrameAngebotstkl;
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wbuKunde;
	}

	public PanelEinkaufsangebot(InternalFrame internalFrame, String add2TitleI,
			Object pk) throws Throwable {
		super(internalFrame, add2TitleI, pk);
		internalFrameAngebotstkl = (InternalFrameAngebotstkl) internalFrame;
		jbInit();
		setDefaults();
		initComponents();
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {

		super.eventYouAreSelected(false);

		Object key = getInternalFrameAngebotstkl().getEinkaufsangebotDto()
				.getIId();

		if (key != null && !key.equals(LPMain.getLockMeForNew())) {

			einkaufsangebotDto = DelegateFactory
					.getInstance()
					.getAngebotstklDelegate()
					.einkaufsangebotFindByPrimaryKey(
							getInternalFrameAngebotstkl()
									.getEinkaufsangebotDto().getIId());

			dto2Components();

			String cBez = "";
			if (getInternalFrameAngebotstkl().getEinkaufsangebotDto() != null) {
				if (getInternalFrameAngebotstkl().getEinkaufsangebotDto()
						.getCProjekt() != null) {
					cBez = getInternalFrameAngebotstkl()
							.getEinkaufsangebotDto().getCProjekt();
				}
			}
			getInternalFrame().setLpTitle(
					InternalFrame.TITLE_IDX_AS_I_LIKE,
					getInternalFrameAngebotstkl().getEinkaufsangebotDto()
							.getCNr() + ", " + cBez);

		} else {
			leereAlleFelder(this);
			wdfBelegdatum
					.setDate(new java.sql.Date(System.currentTimeMillis()));
			wnfMenge1.setInteger(0);
			wnfMenge2.setInteger(0);
			wnfMenge3.setInteger(0);
			wnfMenge4.setInteger(0);
			wnfMenge5.setInteger(0);
		}

	}

	/**
	 * dto2Components
	 * 
	 * @throws Throwable
	 */
	protected void dto2Components() throws Throwable {

		wtfProjekt.setText(einkaufsangebotDto.getCProjekt());
		wnfMenge1.setBigDecimal(einkaufsangebotDto.getNMenge1());
		wnfMenge2.setBigDecimal(einkaufsangebotDto.getNMenge2());
		wnfMenge3.setBigDecimal(einkaufsangebotDto.getNMenge3());
		wnfMenge4.setBigDecimal(einkaufsangebotDto.getNMenge4());
		wnfMenge5.setBigDecimal(einkaufsangebotDto.getNMenge5());

		kundeDto = DelegateFactory.getInstance().getKundeDelegate()
				.kundeFindByPrimaryKey(einkaufsangebotDto.getKundeIId());
		// Goto Kunde Ziel setzen
		wbuKunde.setOKey(kundeDto.getIId());
		wtfKunde.setText(kundeDto.getPartnerDto().formatTitelAnrede());
		partnerIId = kundeDto.getPartnerDto().getIId();
		if (einkaufsangebotDto.getAnsprechpartnerIId() != null) {
			AnsprechpartnerDto ansprechpartnerDto = DelegateFactory
					.getInstance()
					.getAnsprechpartnerDelegate()
					.ansprechpartnerFindByPrimaryKey(
							einkaufsangebotDto.getAnsprechpartnerIId());
			wtfAnsprechpartner.setText(ansprechpartnerDto.getPartnerDto()
					.formatTitelAnrede());
		} else {
			wtfAnsprechpartner.setText(null);
		}

		wdfBelegdatum.setTimestamp(einkaufsangebotDto.getTBelegdatum());
		this.setStatusbarPersonalIIdAendern(einkaufsangebotDto
				.getPersonalIIdAendern());
		this.setStatusbarPersonalIIdAnlegen(einkaufsangebotDto
				.getPersonalIIdAnlegen());
		this.setStatusbarTAnlegen(einkaufsangebotDto.getTAnlegen());
		this.setStatusbarTAendern(einkaufsangebotDto.getTAendern());

	}

	void dialogQueryKundeFromListe(ActionEvent e) throws Throwable {
		panelQueryFLRKunde = PartnerFilterFactory.getInstance()
				.createPanelFLRKunde(getInternalFrame(), true, false,
						einkaufsangebotDto.getKundeIId());
		new DialogQuery(panelQueryFLRKunde);
	}

	void dialogQueryAnsprechartnerFromListe(ActionEvent e) throws Throwable {

		if (partnerIId != null) {
			panelQueryFLRAnsprechpartner = PartnerFilterFactory.getInstance()
					.createPanelFLRAnsprechpartner(getInternalFrame(),
							partnerIId,
							einkaufsangebotDto.getAnsprechpartnerIId(), true,
							true);
			new DialogQuery(panelQueryFLRAnsprechpartner);
		} else {
			DialogFactory.showModalDialog(LPMain.getInstance()
					.getTextRespectUISPr("lp.error"), LPMain.getInstance()
					.getTextRespectUISPr("lp.error.kundenichtgewaehlt")); // UW->CK
																			// Konstante
		}
	}

	private void jbInit() throws Throwable {
		// das Aussenpanel hat immer das Gridbaglayout.
		gridBagLayoutAll = new GridBagLayout();
		this.setLayout(gridBagLayoutAll);
		getInternalFrame().addItemChangedListener(this);

		// Actionpanel von Oberklasse holen und anhaengen.
		jpaButtonAction = getToolsPanel();
		this.setActionMap(null);
		jpaWorkingOn = new JPanel();
		gridBagLayoutWorkingPanel = new GridBagLayout();
		jpaWorkingOn.setLayout(gridBagLayoutWorkingPanel);
		wbuKunde.setText(LPMain.getInstance().getTextRespectUISPr(
				"button.kunde"));

		wtfKunde.setMandatoryField(true);
		wtfKunde.setColumnsMax(PartnerFac.MAX_NAME);
		wtfKunde.setActivatable(false);
		wtfKunde.setColumnsMax(Facade.MAX_UNBESCHRAENKT);

		wlaBelegdatum = new WrapperLabel(LPMain.getInstance()
				.getTextRespectUISPr("label.belegdatum"));

		wnfMenge1.setMandatoryField(true);
		wnfMenge2.setMandatoryField(true);
		wnfMenge3.setMandatoryField(true);
		wnfMenge4.setMandatoryField(true);
		wnfMenge5.setMandatoryField(true);

		wlaProjekt.setText(LPMain.getInstance().getTextRespectUISPr(
				"label.projekt"));

		wbuKunde.setActionCommand(this.ACTION_SPECIAL_KUNDE_FROM_LISTE);
		wbuKunde.addActionListener(this);

		wbuAnsprechpartner.setText(LPMain.getInstance().getTextRespectUISPr(
				"button.ansprechpartner"));
		wbuAnsprechpartner
				.setActionCommand(this.ACTION_SPECIAL_ANSPRECHPARTNER_KUNDE);
		wbuAnsprechpartner.addActionListener(this);

		wtfAnsprechpartner.setActivatable(false);

		wdfBelegdatum.setMandatoryField(true);

		wlaMenge1.setText(LPMain.getInstance().getTextRespectUISPr(
				"label.menge")
				+ " 1");
		wlaMenge2.setText(LPMain.getInstance().getTextRespectUISPr(
				"label.menge")
				+ " 2");
		wlaMenge3.setText(LPMain.getInstance().getTextRespectUISPr(
				"label.menge")
				+ " 3");
		wlaMenge4.setText(LPMain.getInstance().getTextRespectUISPr(
				"label.menge")
				+ " 4");
		wlaMenge5.setText(LPMain.getInstance().getTextRespectUISPr(
				"label.menge")
				+ " 5");

		this.add(jpaButtonAction, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0,
						0, 0, 0), 0, 0));
		this.add(jpaWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
				GridBagConstraints.NORTHEAST, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
		this.add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 1.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wlaBelegdatum, new GridBagConstraints(0, iZeile, 1, 1,
				0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wdfBelegdatum, new GridBagConstraints(1, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wbuKunde, new GridBagConstraints(0, iZeile, 1, 1, 0.1,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfKunde, new GridBagConstraints(1, iZeile, 3, 1, 0.3,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;

		jpaWorkingOn.add(wbuAnsprechpartner, new GridBagConstraints(0, iZeile,
				1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfAnsprechpartner, new GridBagConstraints(1, iZeile,
				3, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wlaProjekt, new GridBagConstraints(0, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfProjekt, new GridBagConstraints(1, iZeile, 3, 1,
				0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wlaMenge1, new GridBagConstraints(0, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfMenge1, new GridBagConstraints(1, iZeile, 1, 1, 0,
				0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;

		jpaWorkingOn.add(wlaMenge2, new GridBagConstraints(0, iZeile, 1, 1,
				0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfMenge2, new GridBagConstraints(1, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		jpaWorkingOn.add(wlaMenge3, new GridBagConstraints(0, iZeile, 1, 1,
				0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfMenge3, new GridBagConstraints(1, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		jpaWorkingOn.add(wlaMenge4, new GridBagConstraints(0, iZeile, 1, 1,
				0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfMenge4, new GridBagConstraints(1, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		jpaWorkingOn.add(wlaMenge5, new GridBagConstraints(0, iZeile, 1, 1,
				0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfMenge5, new GridBagConstraints(1, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(2, 2, 2, 2), 0, 0));

		String[] aWhichButtonIUse = { ACTION_UPDATE, ACTION_SAVE,
				ACTION_DISCARD, };

		enableToolsPanelButtons(aWhichButtonIUse);

	}

	public void eventActionNew(EventObject eventObject, boolean bLockMeI,
			boolean bNeedNoNewI) throws Throwable {

		super.eventActionNew(eventObject, true, false);

		einkaufsangebotDto = new EinkaufsangebotDto();
		kundeDto = new KundeDto();
		partnerIId = null;
		getInternalFrameAngebotstkl().setEinkaufsangebotDto(einkaufsangebotDto);
		leereAlleFelder(this);

	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		if (e.getActionCommand().equals(ACTION_SPECIAL_KUNDE_FROM_LISTE)) {
			dialogQueryKundeFromListe(e);
		} else if (e.getActionCommand().equals(
				ACTION_SPECIAL_ANSPRECHPARTNER_KUNDE)) {
			dialogQueryAnsprechartnerFromListe(e);
		}
	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_EINKAUFSANGEBOT;
	}

	protected void setDefaults() throws Throwable {

	}

	protected void components2Dto() throws ExceptionLP {
		einkaufsangebotDto.setCProjekt(wtfProjekt.getText());
		einkaufsangebotDto.setTBelegdatum(wdfBelegdatum.getTimestamp());
		einkaufsangebotDto.setNMenge1(wnfMenge1.getBigDecimal());
		einkaufsangebotDto.setNMenge2(wnfMenge2.getBigDecimal());
		einkaufsangebotDto.setNMenge3(wnfMenge3.getBigDecimal());
		einkaufsangebotDto.setNMenge4(wnfMenge4.getBigDecimal());
		einkaufsangebotDto.setNMenge5(wnfMenge5.getBigDecimal());
	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {
		if (allMandatoryFieldsSetDlg()) {
			components2Dto();
			if (einkaufsangebotDto.getIId() == null) {
				einkaufsangebotDto.setIId(DelegateFactory.getInstance()
						.getAngebotstklDelegate()
						.createEinkaufsangebot(einkaufsangebotDto));
				setKeyWhenDetailPanel(einkaufsangebotDto.getIId());
				einkaufsangebotDto = DelegateFactory
						.getInstance()
						.getAngebotstklDelegate()
						.einkaufsangebotFindByPrimaryKey(
								einkaufsangebotDto.getIId());
				kundeDto = DelegateFactory
						.getInstance()
						.getKundeDelegate()
						.kundeFindByPrimaryKey(einkaufsangebotDto.getKundeIId());
				internalFrameAngebotstkl
						.setEinkaufsangebotDto(einkaufsangebotDto);
			} else {
				DelegateFactory.getInstance().getAngebotstklDelegate()
						.updateEinkaufsangebot(einkaufsangebotDto);
			}
			super.eventActionSave(e, true);

			if (getInternalFrame().getKeyWasForLockMe() == null) {
				getInternalFrame().setKeyWasForLockMe(
						einkaufsangebotDto.getIId().toString());
			}

			eventYouAreSelected(false);

			einkaufsangebotDto = DelegateFactory
					.getInstance()
					.getAngebotstklDelegate()
					.einkaufsangebotFindByPrimaryKey(
							einkaufsangebotDto.getIId());
		}
	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;
		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			if (e.getSource() == panelQueryFLRKunde) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				kundeDto = DelegateFactory.getInstance().getKundeDelegate()
						.kundeFindByPrimaryKey((Integer) key);

				wtfKunde.setText(kundeDto.getPartnerDto().formatTitelAnrede());
				partnerIId = kundeDto.getPartnerDto().getIId();

				einkaufsangebotDto.setKundeIId(kundeDto.getIId());
			} else if (e.getSource() == panelQueryFLRAnsprechpartner) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				AnsprechpartnerDto ansprechpartnerDto = DelegateFactory
						.getInstance().getAnsprechpartnerDelegate()
						.ansprechpartnerFindByPrimaryKey((Integer) key);

				wtfAnsprechpartner.setText(ansprechpartnerDto.getPartnerDto()
						.formatTitelAnrede());
				einkaufsangebotDto.setAnsprechpartnerIId(ansprechpartnerDto
						.getIId());
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_LEEREN) {

		}
	}

}
