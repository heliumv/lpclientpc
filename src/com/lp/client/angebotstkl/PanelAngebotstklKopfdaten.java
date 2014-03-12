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
package com.lp.client.angebotstkl;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.Calendar;
import java.util.EventObject;
import java.util.GregorianCalendar;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperComboBox;
import com.lp.client.frame.component.WrapperDateField;
import com.lp.client.frame.component.WrapperGotoButton;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperTextArea;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.partner.PartnerFilterFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.angebotstkl.service.AgstklDto;
import com.lp.server.partner.service.AnsprechpartnerDto;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.partner.service.PartnerFac;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.system.service.WaehrungDto;
import com.lp.server.util.Facade;

@SuppressWarnings("static-access")
public class PanelAngebotstklKopfdaten extends PanelBasis {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private AgstklDto agstklDto = null;
	private KundeDto kundeDto = null;
	private InternalFrameAngebotstkl internalFrameAngebotstkl = null;
	private JPanel jpaWorkingOn = new JPanel();
	private JPanel jpaButtonAction = new JPanel();
	private Border border = null;
	private GridBagLayout gridBagLayoutWorkingPanel = new GridBagLayout();
	private GridBagLayout gridBagLayoutAll = new GridBagLayout();

	private PanelQueryFLR panelQueryFLRKunde = null;
	private PanelQueryFLR panelQueryFLRAnsprechpartner = null;

	private WrapperTextArea wtaAngebot = new WrapperTextArea();

	private ButtonGroup buttonGroup1 = new ButtonGroup();
	private WrapperGotoButton jpaKunde = new WrapperGotoButton(
			WrapperGotoButton.GOTO_KUNDE_AUSWAHL);
	private WrapperTextField wtfKunde = new WrapperTextField();

	private WrapperButton wbuAnsprechpartner = new WrapperButton();
	private WrapperTextField wtfAnsprechpartner = new WrapperTextField();

	private WrapperLabel wlaProjekt = new WrapperLabel();
	private WrapperTextField wtfProjekt = new WrapperTextField();

	private WrapperLabel wlaWaehrung = new WrapperLabel();
	private WrapperComboBox wcoWaehrung = new WrapperComboBox();

	private WrapperLabel wlaKurs = new WrapperLabel();

	private WrapperLabel wlaAngebote = new WrapperLabel();

	private WrapperLabel wlaBelegdatum = new WrapperLabel();
	private WrapperDateField wdfBelegdatum = new WrapperDateField();

	private WrapperNumberField wnfKurs = new WrapperNumberField();

	static final public String ACTION_SPECIAL_KUNDE_FROM_LISTE = "action_kunde_from_liste";
	public static final String ACTION_SPECIAL_ANSPRECHPARTNER_KUNDE = "action_special_ansprechpartner_kunde";
	Integer partnerIId;

	public InternalFrameAngebotstkl getInternalFrameAngebotstkl() {
		return internalFrameAngebotstkl;
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return jpaKunde;
	}

	public PanelAngebotstklKopfdaten(InternalFrame internalFrame,
			String add2TitleI, Object pk) throws Throwable {
		super(internalFrame, add2TitleI, pk);
		internalFrameAngebotstkl = (InternalFrameAngebotstkl) internalFrame;
		jbInit();
		setDefaults();
		initComponents();
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {

		super.eventYouAreSelected(false);

		Object key = getInternalFrameAngebotstkl().getAgstklDto().getIId();

		if (key != null && !key.equals(LPMain.getLockMeForNew())) {

			agstklDto = DelegateFactory
					.getInstance()
					.getAngebotstklDelegate()
					.agstklFindByPrimaryKey(
							getInternalFrameAngebotstkl().getAgstklDto()
									.getIId());

			dto2Components();

			String cBez = "";
			if (getInternalFrameAngebotstkl().getAgstklDto() != null) {
				if (getInternalFrameAngebotstkl().getAgstklDto().getCBez() != null) {
					cBez = getInternalFrameAngebotstkl().getAgstklDto()
							.getCBez();
				}
			}
			getInternalFrame().setLpTitle(
					InternalFrame.TITLE_IDX_AS_I_LIKE,
					getInternalFrameAngebotstkl().getAgstklDto().getCNr()
							+ ", " + cBez);

		} else {
			leereAlleFelder(this);
			wdfBelegdatum
					.setDate(new java.sql.Date(System.currentTimeMillis()));
			wcoWaehrung.setKeyOfSelectedItem(LPMain.getInstance()
					.getTheClient().getSMandantenwaehrung());
		}

	}

	private void setVerwendeteAngebote() throws Throwable {
		String s = DelegateFactory
				.getInstance()
				.getAngebotstklDelegate()
				.getAngeboteDieBestimmteAngebotsstuecklisteVerwenden(
						agstklDto.getIId());
		wtaAngebot.setText(s);
	}

	/**
	 * dto2Components
	 * 
	 * @throws Throwable
	 */
	protected void dto2Components() throws Throwable {

		wtfProjekt.setText(agstklDto.getCBez());
		wcoWaehrung.setKeyOfSelectedItem(agstklDto.getWaehrungCNr());
		wnfKurs.setDouble(agstklDto
				.getFWechselkursmandantwaehrungzuagstklwaehrung());

		kundeDto = DelegateFactory.getInstance().getKundeDelegate()
				.kundeFindByPrimaryKey(agstklDto.getKundeIId());
		// Goto Kunde Ziel setzen
		jpaKunde.setOKey(kundeDto.getIId());
		wtfKunde.setText(kundeDto.getPartnerDto().formatTitelAnrede());
		partnerIId = kundeDto.getPartnerDto().getIId();
		if (agstklDto.getAnsprechpartnerIIdKunde() != null) {
			AnsprechpartnerDto ansprechpartnerDto = DelegateFactory
					.getInstance()
					.getAnsprechpartnerDelegate()
					.ansprechpartnerFindByPrimaryKey(
							agstklDto.getAnsprechpartnerIIdKunde());
			wtfAnsprechpartner.setText(ansprechpartnerDto.getPartnerDto()
					.formatTitelAnrede());
		} else {
			wtfAnsprechpartner.setText(null);
		}

		setVerwendeteAngebote();

		wdfBelegdatum.setTimestamp(agstklDto.getTBelegdatum());
		this.setStatusbarPersonalIIdAendern(agstklDto.getPersonalIIdAendern());
		this.setStatusbarPersonalIIdAnlegen(agstklDto.getPersonalIIdAnlegen());
		this.setStatusbarTAnlegen(agstklDto.getTAnlegen());
		this.setStatusbarTAendern(agstklDto.getTAendern());

	}

	void dialogQueryKundeFromListe(ActionEvent e) throws Throwable {
		panelQueryFLRKunde = PartnerFilterFactory.getInstance()
				.createPanelFLRKunde(getInternalFrame(), true, false,
						agstklDto.getKundeIId());
		new DialogQuery(panelQueryFLRKunde);
	}

	void dialogQueryAnsprechartnerFromListe(ActionEvent e) throws Throwable {

		if (partnerIId != null) {
			panelQueryFLRAnsprechpartner = PartnerFilterFactory.getInstance()
					.createPanelFLRAnsprechpartner(getInternalFrame(),
							partnerIId, agstklDto.getAnsprechpartnerIIdKunde(),
							true, true);
			new DialogQuery(panelQueryFLRAnsprechpartner);
		} else {
			DialogFactory.showModalDialog(LPMain.getInstance()
					.getTextRespectUISPr("lp.error"), LPMain.getInstance()
					.getTextRespectUISPr("lp.error.kundenichtgewaehlt")); // UW->
			// CK
			// Konstante
		}
	}

	private void jbInit() throws Throwable {
		border = BorderFactory.createEmptyBorder(0, 0, 0, 0);
		setBorder(border);
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
		jpaKunde.setText(LPMain.getInstance().getTextRespectUISPr(
				"button.kunde"));

		wtfKunde.setMandatoryField(true);
		wtfKunde.setColumnsMax(PartnerFac.MAX_NAME);
		wtfKunde.setActivatable(false);
		wtfKunde.setColumnsMax(Facade.MAX_UNBESCHRAENKT);

		wlaBelegdatum = new WrapperLabel(LPMain.getInstance()
				.getTextRespectUISPr("label.belegdatum"));

		wnfKurs.setFractionDigits(6);
		wnfKurs.setActivatable(false);
		wnfKurs.setMandatoryField(true);

		wlaProjekt.setText(LPMain.getInstance().getTextRespectUISPr(
				"label.projekt"));

		wlaAngebote.setText(LPMain.getInstance().getTextRespectUISPr(
				"as.agstkl.verwendeteangebote"));
		wlaAngebote.setHorizontalAlignment(SwingConstants.LEFT);

		jpaKunde.setActionCommand(this.ACTION_SPECIAL_KUNDE_FROM_LISTE);
		jpaKunde.addActionListener(this);

		wbuAnsprechpartner.setText(LPMain.getInstance().getTextRespectUISPr(
				"button.ansprechpartner"));
		wbuAnsprechpartner
				.setActionCommand(this.ACTION_SPECIAL_ANSPRECHPARTNER_KUNDE);
		wbuAnsprechpartner.addActionListener(this);

		wtfAnsprechpartner.setActivatable(false);

		wlaWaehrung.setText(LPMain.getInstance().getTextRespectUISPr(
				"label.waehrung"));
		wcoWaehrung.setMandatoryField(true);
		wcoWaehrung
				.addActionListener(new PanelAngebotstklKopfdaten_wcoWaehrung_actionAdapter(
						this));

		wdfBelegdatum.setMandatoryField(true);
		ParametermandantDto parametermandantDtoTagen = DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getMandantparameter(
						LPMain.getInstance().getTheClient().getMandant(),
						ParameterFac.KATEGORIE_ALLGEMEIN,
						ParameterFac.PARAMETER_BEWEGUNGSMODULE_ANLEGEN_BIS_ZUM);
		int iTagen = ((Integer) parametermandantDtoTagen.getCWertAsObject())
				.intValue();
		// Parameter anlegen bis zum beruecksichtigen
		GregorianCalendar gc = new GregorianCalendar();
		// Auf Vormonat setzen
		gc.set(Calendar.MONTH, gc.get(Calendar.MONTH) - 1);
		// Tag setzen
		gc.set(Calendar.DATE, iTagen);
		wdfBelegdatum.setMinimumValue(new Date(gc.getTimeInMillis()));

		wtaAngebot.setActivatable(false);

		wlaKurs.setText(LPMain.getInstance().getTextRespectUISPr("label.kurs"));

		this.add(jpaButtonAction, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0,
						0, 0, 0), 0, 0));
		this.add(jpaWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
				GridBagConstraints.NORTHEAST, GridBagConstraints.BOTH,
				new Insets(-9, 0, 9, 0), 0, 0));
		this.add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 1.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));

		jpaWorkingOn.add(wlaBelegdatum, new GridBagConstraints(0, 0, 1, 1, 0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(20, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wdfBelegdatum, new GridBagConstraints(1, 0, 1, 1,
				0.35, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(jpaKunde, new GridBagConstraints(0, 1, 1, 1, 0.4, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfKunde, new GridBagConstraints(1, 1, 3, 1, 0.1, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wbuAnsprechpartner, new GridBagConstraints(0, 2, 1, 1,
				0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfAnsprechpartner, new GridBagConstraints(1, 2, 3, 1,
				0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wlaProjekt, new GridBagConstraints(0, 3, 1, 1, 0.1,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfProjekt, new GridBagConstraints(1, 3, 3, 1, 0.1,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaWaehrung, new GridBagConstraints(0, 4, 1, 1, 0.1,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wcoWaehrung, new GridBagConstraints(1, 4, 1, 1, 0.1,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wlaKurs, new GridBagConstraints(2, 4, 1, 1, 0.1, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfKurs, new GridBagConstraints(3, 4, 1, 1, 0.1, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wlaAngebote, new GridBagConstraints(0, 5, 4, 1, 0.1,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(50, 2, 2, 2), 0, 0));

		JScrollPane scrollPane = new JScrollPane(wtaAngebot);

		jpaWorkingOn.add(scrollPane, new GridBagConstraints(0, 6, 4, 1, 0.1,
				1.0, GridBagConstraints.NORTH, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));

		String[] aWhichButtonIUse = { ACTION_UPDATE, ACTION_SAVE,
				ACTION_DELETE, ACTION_DISCARD, };

		enableToolsPanelButtons(aWhichButtonIUse);

	}

	public void eventActionNew(EventObject eventObject, boolean bLockMeI,
			boolean bNeedNoNewI) throws Throwable {

		super.eventActionNew(eventObject, true, false);

		agstklDto = new AgstklDto();
		kundeDto = new KundeDto();
		partnerIId = null;
		getInternalFrameAngebotstkl().setAgstklDto(agstklDto);
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
		return HelperClient.LOCKME_AGSTKL;
	}

	protected void setDefaults() throws Throwable {
		wcoWaehrung.setMap(DelegateFactory.getInstance().getLocaleDelegate()
				.getAllWaehrungen());

	}

	protected void components2Dto() throws ExceptionLP {
		agstklDto.setCBez(wtfProjekt.getText());
		agstklDto.setTBelegdatum(wdfBelegdatum.getTimestamp());
		agstklDto.setWaehrungCNr((String) wcoWaehrung.getSelectedItem());
	}

	protected void eventActionDelete(ActionEvent e,
			boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {
		DelegateFactory.getInstance().getAngebotstklDelegate().removeAgstkl(
				agstklDto);
		super.eventActionDelete(e, true, true);
	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {
		if (allMandatoryFieldsSetDlg()) {
			components2Dto();
			if (agstklDto.getIId() == null) {
				agstklDto.setIId(DelegateFactory.getInstance()
						.getAngebotstklDelegate().createAgstkl(agstklDto));
				setKeyWhenDetailPanel(agstklDto.getIId());
				agstklDto = DelegateFactory.getInstance()
						.getAngebotstklDelegate().agstklFindByPrimaryKey(
								agstklDto.getIId());
				kundeDto = DelegateFactory.getInstance().getKundeDelegate()
						.kundeFindByPrimaryKey(agstklDto.getKundeIId());
				internalFrameAngebotstkl.setAgstklDto(agstklDto);
			} else {
				DelegateFactory.getInstance().getAngebotstklDelegate()
						.updateAgstkl(agstklDto);
			}
			super.eventActionSave(e, true);

			if (getInternalFrame().getKeyWasForLockMe() == null) {
				getInternalFrame().setKeyWasForLockMe(
						agstklDto.getIId().toString());
			}

			eventYouAreSelected(false);

			agstklDto = DelegateFactory.getInstance().getAngebotstklDelegate()
					.agstklFindByPrimaryKey(agstklDto.getIId());
		}
	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;
		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			if (e.getSource() == panelQueryFLRKunde) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				kundeDto = DelegateFactory.getInstance().getKundeDelegate()
						.kundeFindByPrimaryKey((Integer) key);

				AnsprechpartnerDto ansprechpartnerDto = null;
				// Ansprechpartner vorbesetzen?
				ParametermandantDto parameter = (ParametermandantDto) DelegateFactory
						.getInstance()
						.getParameterDelegate()
						.getParametermandant(
								ParameterFac.PARAMETER_ANGEBOTSTKL_ANSP_VORBESETZEN,
								ParameterFac.KATEGORIE_ANGEBOTSSTUECKLISTE,
								LPMain.getInstance().getTheClient()
										.getMandant());
				if ((Boolean) parameter.getCWertAsObject()) {
					ansprechpartnerDto = DelegateFactory
							.getInstance()
							.getAnsprechpartnerDelegate()
							.ansprechpartnerFindErstenEinesPartnersOhneExc(
									kundeDto.getPartnerIId());
				}
				if (ansprechpartnerDto != null) {
					wtfAnsprechpartner.setText(ansprechpartnerDto
							.getPartnerDto().formatTitelAnrede());
					agstklDto.setAnsprechpartnerIIdKunde(ansprechpartnerDto
							.getIId());
				} else {
					wtfAnsprechpartner.setText("");
					agstklDto.setAnsprechpartnerIIdKunde(null);
				}
				agstklDto.setWaehrungCNr(kundeDto.getCWaehrung());
				wcoWaehrung.setKeyOfSelectedItem(kundeDto.getCWaehrung());

				wtfKunde.setText(kundeDto.getPartnerDto().formatTitelAnrede());
				partnerIId = kundeDto.getPartnerDto().getIId();

				agstklDto.setKundeIId(kundeDto.getIId());
				if (kundeDto.getCWaehrung() != null) {
					BigDecimal fKurs = DelegateFactory
							.getInstance()
							.getLocaleDelegate()
							.getWechselkurs2(
									DelegateFactory
											.getInstance()
											.getMandantDelegate()
											.mandantFindByPrimaryKey(
													LPMain.getInstance()
															.getTheClient()
															.getMandant())
											.getWaehrungCNr(),
									kundeDto.getCWaehrung());

					wnfKurs.setBigDecimal(fKurs);
					if (fKurs != null) {
						agstklDto
								.setFWechselkursmandantwaehrungzuagstklwaehrung(new Double(
										fKurs.doubleValue()));
					} else {
						agstklDto
								.setFWechselkursmandantwaehrungzuagstklwaehrung(null);

					}
				}
			} else if (e.getSource() == panelQueryFLRAnsprechpartner) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				AnsprechpartnerDto ansprechpartnerDto = DelegateFactory
						.getInstance().getAnsprechpartnerDelegate()
						.ansprechpartnerFindByPrimaryKey((Integer) key);

				wtfAnsprechpartner.setText(ansprechpartnerDto.getPartnerDto()
						.formatTitelAnrede());
				agstklDto.setAnsprechpartnerIIdKunde(ansprechpartnerDto
						.getIId());
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_LEEREN) {

		}
	}

	public void wcoWaehrung_actionPerformed(ActionEvent e) {
		try {
			if (agstklDto != null) {
				WaehrungDto waehrungDto = DelegateFactory
						.getInstance()
						.getLocaleDelegate()
						.waehrungFindByPrimaryKey(
								(String) wcoWaehrung.getSelectedItem());

				BigDecimal fKurs = DelegateFactory
						.getInstance()
						.getLocaleDelegate()
						.getWechselkurs2(
								DelegateFactory
										.getInstance()
										.getMandantDelegate()
										.mandantFindByPrimaryKey(
												LPMain.getInstance()
														.getTheClient()
														.getMandant())
										.getWaehrungCNr(), waehrungDto.getCNr());

				wnfKurs.setBigDecimal(fKurs);
				if (fKurs != null) {
					agstklDto
							.setFWechselkursmandantwaehrungzuagstklwaehrung(new Double(
									fKurs.doubleValue()));
				} else {
					agstklDto
							.setFWechselkursmandantwaehrungzuagstklwaehrung(null);

				}
			}
		} catch (Throwable ex) {
			handleException(ex, false);
		}
	}

}

class PanelAngebotstklKopfdaten_wcoWaehrung_actionAdapter implements
		ActionListener {
	private PanelAngebotstklKopfdaten adaptee;

	PanelAngebotstklKopfdaten_wcoWaehrung_actionAdapter(
			PanelAngebotstklKopfdaten adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		adaptee.wcoWaehrung_actionPerformed(e);
	}
}
