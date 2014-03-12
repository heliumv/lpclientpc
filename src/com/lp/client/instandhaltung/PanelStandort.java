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
package com.lp.client.instandhaltung;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.EventObject;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.border.Border;

import com.lp.client.artikel.PanelHersteller;
import com.lp.client.auftrag.AuftragFilterFactory;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperEditorField;
import com.lp.client.frame.component.WrapperEditorFieldTexteingabe;
import com.lp.client.frame.component.WrapperGotoButton;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperTelefonField;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.partner.PartnerFilterFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.system.SystemFilterFactory;
import com.lp.server.auftrag.service.AuftragDto;
import com.lp.server.instandhaltung.service.StandortDto;
import com.lp.server.partner.service.AnsprechpartnerDto;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.partner.service.PartnerFac;
import com.lp.server.util.Facade;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;

public class PanelStandort extends PanelBasis {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private InternalFrameInstandhaltung internalFrameInstandhaltung = null;
	private JPanel jpaWorkingOn = new JPanel();
	private JPanel jpaButtonAction = new JPanel();
	private Border border = null;
	private GridBagLayout gridBagLayoutWorkingPanel = new GridBagLayout();
	private GridBagLayout gridBagLayoutAll = new GridBagLayout();

	private WrapperGotoButton wbuPartner = new WrapperGotoButton(
			WrapperGotoButton.GOTO_PARTNER_AUSWAHL);
	private WrapperTextField wtfPartner = new WrapperTextField();
	private PanelQueryFLR panelQueryFLRPartner = null;
	static final public String ACTION_SPECIAL_PARTNER_FROM_LISTE = "action_partner_from_liste";

	private WrapperGotoButton wbuAuftrag = new WrapperGotoButton(
			WrapperGotoButton.GOTO_AUFTRAG_AUSWAHL);
	private WrapperTextField wtfAuftrag = new WrapperTextField();
	private PanelQueryFLR panelQueryFLRAuftrag = null;
	static final public String ACTION_SPECIAL_AUFTRAG_FROM_LISTE = "action_auftrag_from_liste";

	static final private String ACTION_SPECIAL_FLR_ANSPRECHPARTNER = "action_special_flr_ansprechpartner";
	private WrapperLabel wlaBemerkung = new WrapperLabel();
	private WrapperEditorField wtfBemerkung = new WrapperEditorFieldTexteingabe(
			getInternalFrame(), "");
	private WrapperCheckBox wcbVersteckt = new WrapperCheckBox();

	private WrapperTelefonField wtfDurchwahl = null;
	private WrapperTelefonField wtfHandy = null;

	private WrapperButton wbuAnsprechpartner = null;
	private WrapperTextField wtfAnsprechpartner = null;
	private PanelQueryFLR panelQueryFLRAnsprechpartnerAuswahl = null;

	private WrapperButton wbuFileDialog = new WrapperButton();
	private WrapperTextField wtfPfad = new WrapperTextField();
	private JButton wbuGoto = new JButton("->");
	static final private String ACTION_SPECIAL_DOKUMENT = "ACTION_SPECIAL_DOKUMENT";
	static final private String ACTION_SPECIAL_GOTO = "ACTION_SPECIAL_GOTO";

	public InternalFrameInstandhaltung getInternalFrameInstandhaltung() {
		return internalFrameInstandhaltung;
	}

	public PanelStandort(InternalFrame internalFrame, String add2TitleI,
			Object pk) throws Throwable {
		super(internalFrame, add2TitleI, pk);
		internalFrameInstandhaltung = (InternalFrameInstandhaltung) internalFrame;

		jbInit();
		setDefaults();
		initComponents();
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return null;
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {

		super.eventYouAreSelected(false);

		// Object key = getInternalFrameReklamation().getTabbedPaneKueche().
		// getPanelQuerySpeiseplan().getSelectedId();
		Object key = getKeyWhenDetailPanel();

		if (key == null || key.equals(LPMain.getLockMeForNew())) {

			clearStatusbar();

		} else {
			internalFrameInstandhaltung.getTabbedPaneInstandhaltung()
					.setStandortDto(
							DelegateFactory.getInstance()
									.getInstandhaltungDelegate()
									.standortFindByPrimaryKey((Integer) key));

			dto2Components();

		}

	}

	protected void dto2Components() throws Throwable {

		PartnerDto partnerDto = DelegateFactory
				.getInstance()
				.getPartnerDelegate()
				.partnerFindByPrimaryKey(
						internalFrameInstandhaltung
								.getTabbedPaneInstandhaltung().getStandortDto()
								.getPartnerIId());
		wtfPartner.setText(partnerDto.formatAnrede());
		wbuPartner.setOKey(partnerDto.getIId());

		wtfPfad.setText(internalFrameInstandhaltung
				.getTabbedPaneInstandhaltung().getStandortDto()
				.getCDokumentenlink());

		AuftragDto auftragDto = DelegateFactory
				.getInstance()
				.getAuftragDelegate()
				.auftragFindByPrimaryKey(
						internalFrameInstandhaltung
								.getTabbedPaneInstandhaltung().getStandortDto()
								.getAuftragIId());
		wtfAuftrag.setText(auftragDto.getCNr());

		wbuAuftrag
				.setOKey(internalFrameInstandhaltung
						.getTabbedPaneInstandhaltung().getStandortDto()
						.getAuftragIId());
		wtfBemerkung
				.setText(internalFrameInstandhaltung
						.getTabbedPaneInstandhaltung().getStandortDto()
						.getXBemerkung());

		wcbVersteckt
				.setShort(internalFrameInstandhaltung
						.getTabbedPaneInstandhaltung().getStandortDto()
						.getBVersteckt());
		wtfDurchwahl.setPartnerKommunikationDto(partnerDto, null);
		if (internalFrameInstandhaltung.getTabbedPaneInstandhaltung()
				.getStandortDto().getAnsprechpartnerIId() != null) {

			AnsprechpartnerDto anspDto = DelegateFactory
					.getInstance()
					.getAnsprechpartnerDelegate()
					.ansprechpartnerFindByPrimaryKey(
							internalFrameInstandhaltung
									.getTabbedPaneInstandhaltung()
									.getStandortDto().getAnsprechpartnerIId());

			wtfAnsprechpartner.setText(anspDto.getPartnerDto()
					.formatTitelAnrede());

		

			if (anspDto.getCTelefon() != null) {
			
				wtfDurchwahl.setTextDurchwahl(DelegateFactory
						.getInstance()
						.getPartnerDelegate()
						.enrichNumber(
								internalFrameInstandhaltung
										.getTabbedPaneInstandhaltung()
										.getStandortDto().getPartnerIId(),
								PartnerFac.KOMMUNIKATIONSART_TELEFON,
								anspDto.getCTelefon(),
								false));

			} else {
				wtfDurchwahl.setPartnerKommunikationDto(partnerDto, null);
			}

			if (anspDto.getCHandy() != null) {
				wtfHandy.setPartnerKommunikationDto(anspDto.getPartnerDto(),
						anspDto.getCHandy());
			} else {
				wtfHandy.setPartnerKommunikationDto(null, null);
			}

		} else {
			wtfAnsprechpartner.setText(null);
			wtfHandy.setPartnerKommunikationDto(null, null);
		}

	}

	private void jbInit() throws Throwable {
		border = BorderFactory.createEmptyBorder(10, 10, 0, 10);
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
		wcbVersteckt.setText(LPMain.getTextRespectUISPr("lp.versteckt"));

		wbuAnsprechpartner = new WrapperButton();
		wbuAnsprechpartner.setText(LPMain
				.getTextRespectUISPr("button.ansprechpartner.long"));
		wbuAnsprechpartner.setActionCommand(ACTION_SPECIAL_FLR_ANSPRECHPARTNER);
		wbuAnsprechpartner.addActionListener(this);

		wtfAnsprechpartner = new WrapperTextField();
		wtfAnsprechpartner.setActivatable(false);

		wlaBemerkung.setText(LPMain.getTextRespectUISPr("lp.bemerkung"));

		wbuPartner.setText(LPMain.getTextRespectUISPr("part.partner") + "...");
		wbuPartner
				.setActionCommand(PanelHersteller.ACTION_SPECIAL_PARTNER_FROM_LISTE);
		wbuPartner.addActionListener(this);

		wbuAuftrag.setText(LPMain.getTextRespectUISPr("auft.auftrag") + "...");
		wbuAuftrag.setActionCommand(ACTION_SPECIAL_AUFTRAG_FROM_LISTE);
		wbuAuftrag.addActionListener(this);
		wtfAuftrag.setActivatable(false);
		wtfAuftrag.setColumnsMax(Facade.MAX_UNBESCHRAENKT);
		wtfAuftrag.setMandatoryField(true);

		wtfPartner.setText("");
		wtfPartner.setColumnsMax(Facade.MAX_UNBESCHRAENKT);
		wtfPartner.setMandatoryField(true);
		wtfPartner.setActivatable(false);

		wtfHandy = new WrapperTelefonField(PartnerFac.MAX_KOMMART_INHALT);
		wtfHandy.setActivatable(false);
		wtfDurchwahl = new WrapperTelefonField(PartnerFac.MAX_KOMMART_INHALT);
		wtfDurchwahl.setActivatable(false);

		wbuFileDialog.setText(LPMain
				.getTextRespectUISPr("is.standort.dokument"));

		wtfPfad.setColumnsMax(Facade.MAX_UNBESCHRAENKT);
		wtfPfad.setActivatable(false);

		wbuFileDialog.setActionCommand(ACTION_SPECIAL_DOKUMENT);
		wbuFileDialog.addActionListener(this);

		wbuGoto.setActionCommand(ACTION_SPECIAL_GOTO);
		wbuGoto.addActionListener(this);

		this.add(jpaButtonAction, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0,
						0, 0, 0), 0, 0));
		this.add(jpaWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
				GridBagConstraints.NORTHEAST, GridBagConstraints.BOTH,
				new Insets(-9, 0, 9, 0), 0, 0));
		this.add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 1.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));

		iZeile++;
		jpaWorkingOn.add(wbuPartner, new GridBagConstraints(0, iZeile, 1, 1,
				0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfPartner, new GridBagConstraints(1, iZeile, 1, 1,
				0.3, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfDurchwahl, new GridBagConstraints(2, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 200, 0));
		iZeile++;
		jpaWorkingOn.add(wbuAnsprechpartner, new GridBagConstraints(0, iZeile,
				1, 1, 0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfAnsprechpartner, new GridBagConstraints(1, iZeile,
				1, 1, 0.3, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfHandy, new GridBagConstraints(2, iZeile, 1, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 200, 0));
		iZeile++;
		jpaWorkingOn.add(wbuAuftrag, new GridBagConstraints(0, iZeile, 1, 1,
				0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfAuftrag, new GridBagConstraints(1, iZeile, 2, 1,
				0.3, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wlaBemerkung, new GridBagConstraints(0, iZeile, 1, 1,
				0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfBemerkung, new GridBagConstraints(1, iZeile, 3, 1,
				0.3, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 50));
		iZeile++;
		jpaWorkingOn.add(wbuFileDialog, new GridBagConstraints(0, iZeile, 1, 1,
				0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfPfad, new GridBagConstraints(1, iZeile, 1, 1, 0.3,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wbuGoto, new GridBagConstraints(2, iZeile, 1, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wcbVersteckt, new GridBagConstraints(2, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		String[] aWhichButtonIUse = { ACTION_UPDATE, ACTION_SAVE,
				ACTION_DELETE, ACTION_DISCARD };

		enableToolsPanelButtons(aWhichButtonIUse);

	}

	private void dialogQueryAuftrag() throws Throwable {
		FilterKriterium[] fk = SystemFilterFactory.getInstance()
				.createFKMandantCNr();

		panelQueryFLRAuftrag = AuftragFilterFactory.getInstance()
				.createPanelFLRAuftrag(
						getInternalFrame(),
						true,
						true,
						fk,
						internalFrameInstandhaltung
								.getTabbedPaneInstandhaltung().getStandortDto()
								.getAuftragIId());
		new DialogQuery(panelQueryFLRAuftrag);
	}

	public void eventActionNew(EventObject eventObject, boolean bLockMeI,
			boolean bNeedNoNewI) throws Throwable {

		super.eventActionNew(eventObject, true, false);
		leereAlleFelder(this);

		internalFrameInstandhaltung.getTabbedPaneInstandhaltung()
				.setStandortDto(new StandortDto());

	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		if (e.getActionCommand().equals(ACTION_SPECIAL_FLR_ANSPRECHPARTNER)) {

			if (internalFrameInstandhaltung.getTabbedPaneInstandhaltung()
					.getStandortDto().getPartnerIId() != null) {
				panelQueryFLRAnsprechpartnerAuswahl = PartnerFilterFactory
						.getInstance().createPanelFLRAnsprechpartner(
								getInternalFrame(),
								internalFrameInstandhaltung
										.getTabbedPaneInstandhaltung()
										.getStandortDto().getPartnerIId(),
								internalFrameInstandhaltung
										.getTabbedPaneInstandhaltung()
										.getStandortDto()
										.getAnsprechpartnerIId(), true, true);
				new DialogQuery(panelQueryFLRAnsprechpartnerAuswahl);
			}

		} else if (e.getActionCommand().equals(
				ACTION_SPECIAL_PARTNER_FROM_LISTE)) {
			dialogQueryPartnerFromListe(e);
		} else if (e.getActionCommand().equals(
				ACTION_SPECIAL_AUFTRAG_FROM_LISTE)) {
			dialogQueryAuftrag();
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_DOKUMENT)) {
			JFileChooser fc = new JFileChooser();
			fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			if (wtfPfad.getText() != null) {
				fc.setCurrentDirectory(new File(wtfPfad.getText()));
			}

			int returnVal = fc.showOpenDialog(null);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				wtfPfad.setText(fc.getSelectedFile().getAbsolutePath());
			}

		} else if (e.getActionCommand().equals(ACTION_SPECIAL_GOTO)) {
			if (wtfPfad.getText() != null) {
				try {

					java.io.File f = new File(wtfPfad.getText());
					java.awt.Desktop.getDesktop().open(f);
				} catch (java.lang.IllegalArgumentException e1) {
					DialogFactory.showModalDialog(
							LPMain.getTextRespectUISPr("lp.error"),
							e1.getMessage());

				}
			}
		}
	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_INSTANDHALTUNG;
	}

	protected void setDefaults() throws Throwable {

	}

	protected void components2Dto() throws Throwable {

		internalFrameInstandhaltung
				.getTabbedPaneInstandhaltung()
				.getStandortDto()
				.setInstandhaltungIId(
						internalFrameInstandhaltung
								.getTabbedPaneInstandhaltung()
								.getInstandhaltungDto().getIId());
		internalFrameInstandhaltung.getTabbedPaneInstandhaltung()
				.getStandortDto().setBVersteckt(wcbVersteckt.getShort());

		internalFrameInstandhaltung.getTabbedPaneInstandhaltung()
				.getStandortDto().setXBemerkung(wtfBemerkung.getText());
		internalFrameInstandhaltung.getTabbedPaneInstandhaltung()
				.getStandortDto().setCDokumentenlink(wtfPfad.getText());

	}

	protected void eventActionDelete(ActionEvent e,
			boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {
		DelegateFactory
				.getInstance()
				.getInstandhaltungDelegate()
				.removeStandort(
						internalFrameInstandhaltung
								.getTabbedPaneInstandhaltung().getStandortDto());
		super.eventActionDelete(e, true, true);
	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {
		if (allMandatoryFieldsSetDlg()) {

			components2Dto();

			if (internalFrameInstandhaltung.getTabbedPaneInstandhaltung()
					.getStandortDto().getIId() == null) {
				internalFrameInstandhaltung
						.getTabbedPaneInstandhaltung()
						.getStandortDto()
						.setIId(DelegateFactory
								.getInstance()
								.getInstandhaltungDelegate()
								.createStandort(
										internalFrameInstandhaltung
												.getTabbedPaneInstandhaltung()
												.getStandortDto()));
				setKeyWhenDetailPanel(internalFrameInstandhaltung
						.getTabbedPaneInstandhaltung().getStandortDto()
						.getIId());

			} else {

				DelegateFactory
						.getInstance()
						.getInstandhaltungDelegate()
						.updateStandort(
								internalFrameInstandhaltung
										.getTabbedPaneInstandhaltung()
										.getStandortDto());

			}
			super.eventActionSave(e, true);

			if (getInternalFrame().getKeyWasForLockMe() == null) {
				getInternalFrame().setKeyWasForLockMe(
						internalFrameInstandhaltung
								.getTabbedPaneInstandhaltung().getStandortDto()
								.getIId().toString());
			}
			eventYouAreSelected(false);

		}
	}

	void dialogQueryPartnerFromListe(ActionEvent e) throws Throwable {
		panelQueryFLRPartner = PartnerFilterFactory.getInstance()
				.createPanelFLRPartner(
						getInternalFrame(),
						internalFrameInstandhaltung
								.getTabbedPaneInstandhaltung().getStandortDto()
								.getPartnerIId(), false);
		new DialogQuery(panelQueryFLRPartner);
	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;
		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			if (e.getSource() == panelQueryFLRPartner) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				PartnerDto partnerDto = DelegateFactory.getInstance()
						.getPartnerDelegate()
						.partnerFindByPrimaryKey((Integer) key);
				wtfPartner.setText(partnerDto.formatAnrede());
				internalFrameInstandhaltung.getTabbedPaneInstandhaltung()
						.getStandortDto().setPartnerIId(partnerDto.getIId());
				wtfAnsprechpartner.setText(null);
				internalFrameInstandhaltung.getTabbedPaneInstandhaltung()
						.getStandortDto().setAnsprechpartnerIId(null);
			}

			else if (e.getSource() == panelQueryFLRAnsprechpartnerAuswahl) {
				Integer iId = (Integer) ((ISourceEvent) e.getSource())
						.getIdSelected();
				if (iId != null) {
					internalFrameInstandhaltung.getTabbedPaneInstandhaltung()
							.getStandortDto().setAnsprechpartnerIId(iId);
					wtfAnsprechpartner.setText(DelegateFactory.getInstance()
							.getAnsprechpartnerDelegate()
							.ansprechpartnerFindByPrimaryKey(iId)
							.getPartnerDto().formatTitelAnrede());
				}
			} else if (e.getSource() == panelQueryFLRAuftrag) {
				Integer iId = (Integer) ((ISourceEvent) e.getSource())
						.getIdSelected();

				AuftragDto auftragDto = DelegateFactory.getInstance()
						.getAuftragDelegate().auftragFindByPrimaryKey(iId);

				KundeDto kdDto = DelegateFactory
						.getInstance()
						.getKundeDelegate()
						.kundeFindByPrimaryKey(
								auftragDto.getKundeIIdAuftragsadresse());

				PartnerDto partnerDto = DelegateFactory.getInstance()
						.getPartnerDelegate()
						.partnerFindByPrimaryKey(kdDto.getPartnerIId());
				wtfPartner.setText(partnerDto.formatAnrede());
				internalFrameInstandhaltung.getTabbedPaneInstandhaltung()
						.getStandortDto().setPartnerIId(partnerDto.getIId());
				wtfAnsprechpartner.setText(null);
				internalFrameInstandhaltung.getTabbedPaneInstandhaltung()
						.getStandortDto().setAnsprechpartnerIId(null);

				internalFrameInstandhaltung.getTabbedPaneInstandhaltung()
						.getStandortDto().setAuftragIId(iId);
				wtfAuftrag.setText(auftragDto.getCNr());
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_LEEREN) {
			if (e.getSource() == panelQueryFLRAnsprechpartnerAuswahl) {
				internalFrameInstandhaltung.getTabbedPaneInstandhaltung()
						.getStandortDto().setAnsprechpartnerIId(null);
				wtfAnsprechpartner.setText(null);
			}
		}
	}

}
