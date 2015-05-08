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
package com.lp.client.frame.report;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EventObject;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.lp.client.bestellung.ReportAbholauftrag;
import com.lp.client.frame.Defaults;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperEditorField;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.lieferschein.ReportLieferschein;
import com.lp.client.partner.PartnerFilterFactory;
import com.lp.client.pc.LPMain;
import com.lp.editor.LpEditor;
import com.lp.server.bestellung.service.BestellungDto;
import com.lp.server.bestellung.service.BestellungReportFac;
import com.lp.server.lieferschein.service.LieferscheinDto;
import com.lp.server.partner.service.AnsprechpartnerDto;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.partner.service.PartnerFac;
import com.lp.server.personal.service.PersonalDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MailtextDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.system.service.ReportvarianteDto;
import com.lp.server.system.service.VersandFac;
import com.lp.server.system.service.VersandauftragDto;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

/*
 * <p><I>Diese Klasse kuemmert sich ...</I> </p>
 * 
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 * 
 * <p>Erstellungsdatum <I>dd.mm.05</I></p>
 * 
 * <p> </p>
 * 
 * @author not attributable
 * 
 * @version $Revision: 1.18 $
 */
public class PanelVersandEmail extends PanelVersand {
	public WrapperCheckBox getWcbDokumenteAnhaengen() {
		return wcbDokumenteAnhaengen;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private WrapperLabel wlaCCEmpfaenger = null;
	private WrapperTextField wtfCCEmpfaenger = null;
	private WrapperLabel wlaAbsender = null;
	private WrapperTextField wtfAbsender = null;
	private WrapperLabel wlaText = null;
	private WrapperEditorField wefText = null;
	private MailtextDto mailtextDto = null;
	private WrapperCheckBox wcbEmpfangsbestaetigung = null;
	private WrapperCheckBox wcbDokumenteAnhaengen = null;
	private PanelReportIfJRDS jpaPanelReportIf = null;
	private WrapperButton wbuEmpfaengerCC = null;
	private WrapperButton wbuEmpfaengerAnspCC = null;
	private JTextField jtfAnhaenge = null;
	private WrapperButton wbuAnhangWaehlen = null;
	private WrapperButton wbuAnhangLoeschen = null;
	private WrapperLabel wlaAnhaenge = null;
	private PanelReportKriterien panelReportKriterien = null;
	public static final String ACTION_SPECIAL_ATTACHMENT = "ACTION_SPECIAL_ATTACHMENT";
	public static final String ACTION_SPECIAL_REMOVE_ATTACHMENT = "ACTION_SPECIAL_REMOVE_ATTACHMENT";
	protected static final String ACTION_SPECIAL_PARTNERCC = "action_special_partnercc";
	protected static final String ACTION_SPECIAL_ANSPRECHPARTNERCC = "action_special_ansprechpartnercc";
	protected PanelQueryFLR panelQueryFLRPartnerCC = null;
	protected PanelQueryFLR panelQueryFLRAnsprechpartnerCC = null;
	protected PartnerDto partnerDtoEmpfaengerCC = null;
	protected Integer ansprechpartnerIIdCC = null;
	public boolean bUebersteuerterEmpaengerVorschlagGesetzt = false;
	public boolean bUebersteuerterEmpaengerVorschlagCCGesetzt = false;

	public PanelVersandEmail(InternalFrame internalFrame,
			MailtextDto mailtextDto, String belegartCNr, Integer belegIId,
			PanelReportIfJRDS jpaPanelReportIf,
			PanelReportKriterien panelReportKriterien,
			PartnerDto partnerDtoEmpfaenger) throws Throwable {
		super(internalFrame, belegartCNr, belegIId, partnerDtoEmpfaenger);
		this.mailtextDto = mailtextDto;
		this.jpaPanelReportIf = jpaPanelReportIf;
		this.panelReportKriterien = panelReportKriterien;
		jbInitPanel();
		setDefaultsPanel();
	}

	protected void setDefaultText() throws Throwable {
		String sDefaulttext = DelegateFactory.getInstance()
				.getVersandDelegate().getDefaultTextForBelegEmail(mailtextDto);
		wefText.setText(sDefaulttext);
		wefText.setDefaultText(sDefaulttext);
		setupEditorProperties();
	}

	protected void setDefaultBetreff() throws Throwable {
		String betreff = DelegateFactory
				.getInstance()
				.getVersandDelegate()
				.getDefaultBetreffForBelegEmail(mailtextDto, getbelegartCNr(),
						getbelegIId());
		wtfBetreff.setText(betreff);
	}

	protected void setDefaultsPanel() throws Throwable {
		PersonalDto personalDto = DelegateFactory
				.getInstance()
				.getPersonalDelegate()
				.personalFindByPrimaryKey(LPMain.getTheClient().getIDPersonal());

		if (personalDto.getCEmail() != null) {
			wtfAbsender.setText(personalDto.getCEmail());
		}
		setDefaultText();
		setDefaultBetreff();
		setupEditorProperties();

		// PJ18083
		if (jpaPanelReportIf.getReportname().equals(
				BestellungReportFac.REPORT_BESTELLUNG)) {

			boolean bEmpfangsbestaetigung = (Boolean) DelegateFactory
					.getInstance()
					.getParameterDelegate()
					.getMandantparameter(LPMain.getTheClient().getMandant(),
							ParameterFac.KATEGORIE_BESTELLUNG,
							ParameterFac.PARAMETER_DEFAULT_EMPFANGSBESTAETIGUNG)
					.getCWertAsObject();
			if (bEmpfangsbestaetigung) {
				wcbEmpfangsbestaetigung.setSelected(true);
			}
		}

	}

	private void setupEditorProperties() {
		LpEditor wefEditor = wefText.getLpEditor();
		wefEditor.disableStyledText();

		wefEditor.showMenu(false);
		wefEditor.showTableItems(false);
		wefEditor.showTabRuler(false);
	}

	public MailtextDto getMailtextDto() {
		if (mailtextDto == null) {
			mailtextDto = new MailtextDto();
		}
		return mailtextDto;
	}

	public void setMailtextDto(MailtextDto mailtextDto) {
		if (mailtextDto != null) {
			this.mailtextDto = mailtextDto;
		}
	}

	private void jbInitPanel() throws Throwable {
		wlaCCEmpfaenger = new WrapperLabel(
				LPMain.getTextRespectUISPr("label.cc"));
		wtfCCEmpfaenger = new WrapperTextField(VersandFac.MAX_EMPFAENGER);
		wlaAbsender = new WrapperLabel(
				LPMain.getTextRespectUISPr("label.absender"));
		wtfAbsender = new WrapperTextField(VersandFac.MAX_EMPFAENGER);
		wlaText = new WrapperLabel(LPMain.getTextRespectUISPr("label.text"));
		wefText = new WrapperEditorField(getInternalFrame(),
				LPMain.getTextRespectUISPr("lp.emailtext"));
		wcbEmpfangsbestaetigung = new WrapperCheckBox(
				LPMain.getTextRespectUISPr("lp.empfangsbestaetigung"));

		wcbDokumenteAnhaengen = new WrapperCheckBox(
				LPMain.getTextRespectUISPr("lp.dokumenteanhaengen"));

		wbuEmpfaengerCC = new WrapperButton(
				LPMain.getTextRespectUISPr("lp.versand.partner"));
		wbuEmpfaengerCC.setActionCommand(ACTION_SPECIAL_PARTNERCC);
		wbuEmpfaengerCC.addActionListener(this);

		wbuEmpfaengerAnspCC = new WrapperButton(
				LPMain.getTextRespectUISPr("lp.versand.ansprechpartner"));
		wbuEmpfaengerAnspCC.setActionCommand(ACTION_SPECIAL_ANSPRECHPARTNERCC);
		wbuEmpfaengerAnspCC.addActionListener(this);

		wtfAbsender.setMandatoryField(true);
		wlaAnhaenge = new WrapperLabel(
				LPMain.getTextRespectUISPr("label.anhang"));
		jtfAnhaenge = new JTextField();
		jtfAnhaenge.setText("");
		jtfAnhaenge.setEditable(false);
		wbuAnhangWaehlen = new WrapperButton(
				LPMain.getTextRespectUISPr("label.hinzufuegen"));
		wbuAnhangWaehlen.setActionCommand(ACTION_SPECIAL_ATTACHMENT);
		wbuAnhangLoeschen = new WrapperButton("");
		wbuAnhangLoeschen.setActionCommand(ACTION_SPECIAL_REMOVE_ATTACHMENT);
		wbuAnhangLoeschen.setIcon(new ImageIcon(getClass().getResource(
				"/com/lp/client/res/leeren.png")));
		wbuAnhangLoeschen
				.setMinimumSize(new Dimension(Defaults.getInstance()
						.getControlHeight(), Defaults.getInstance()
						.getControlHeight()));
		wbuAnhangLoeschen
				.setPreferredSize(new Dimension(Defaults.getInstance()
						.getControlHeight(), Defaults.getInstance()
						.getControlHeight()));

		getInternalFrame().addItemChangedListener(this);
		ImageIcon imageIcon = new ImageIcon(getClass().getResource(
				"/com/lp/client/res/mail.png"));
		wbuSenden.setIcon(imageIcon);

		jpaWorkingOn.add(wbuEmpfaengerCC, new GridBagConstraints(0, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(2, 2, 2, 2), 70, 0));

		if (partnerDtoEmpfaenger != null) {

			jpaWorkingOn.add(wbuEmpfaengerAnspCC, new GridBagConstraints(0,
					iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
					GridBagConstraints.NONE, new Insets(2, 85, 2, 2), 70, 0));
		}
		jpaWorkingOn.add(wlaCCEmpfaenger, new GridBagConstraints(0, iZeile, 1,
				1, 0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(2, 150, 2, 2), 30, 0));

		jpaWorkingOn.add(wtfCCEmpfaenger, new GridBagConstraints(1, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wcbEmpfangsbestaetigung, new GridBagConstraints(2,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		jpaWorkingOn.add(wlaAbsender, new GridBagConstraints(0, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfAbsender, new GridBagConstraints(1, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		if (getbelegartCNr() != null
				&& getbelegartCNr().equals(LocaleFac.BELEGART_LIEFERSCHEIN)) {

			ParametermandantDto parameter = (ParametermandantDto) DelegateFactory
					.getInstance()
					.getParameterDelegate()
					.getParametermandant(
							ParameterFac.PARAMETER_DEFAULT_DOKUMENTE_ANHAENGEN,
							ParameterFac.KATEGORIE_ALLGEMEIN,
							LPMain.getTheClient().getMandant());

			if ((Boolean) parameter.getCWertAsObject()) {
				wcbDokumenteAnhaengen.setSelected(true);
			}

			jpaWorkingOn.add(wcbDokumenteAnhaengen, new GridBagConstraints(2,
					iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		}
		iZeile++;
		jpaWorkingOn.add(wlaBetreff, new GridBagConstraints(0, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfBetreff, new GridBagConstraints(1, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		JPanel attachPanel = new JPanel(new GridBagLayout());
		attachPanel.add(wlaAnhaenge, new GridBagConstraints(0, 0, 2, 1, 2.0,
				0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		attachPanel.add(jtfAnhaenge, new GridBagConstraints(2, 0, 2, 1, 5.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		attachPanel.add(wbuAnhangLoeschen, new GridBagConstraints(4, 0, 1, 1, 0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 0, 2, 0), 0, 0));
		attachPanel.add(wbuAnhangWaehlen, new GridBagConstraints(5, 0, 2, 1,
				2.0, 0.0, GridBagConstraints.EAST,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(attachPanel, new GridBagConstraints(2, iZeile, 3, 1,
				5.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

		iZeile++;
		jpaWorkingOn.add(wlaText, new GridBagConstraints(0, iZeile, 1, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wefText, new GridBagConstraints(1, iZeile, 4, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
	}

	public VersandauftragDto getVersandauftragDto() throws Throwable {
		VersandauftragDto dto = super.getVersandauftragDto();
		dto.setCCcempfaenger(wtfCCEmpfaenger.getText());
		dto.setCAbsenderadresse(wtfAbsender.getText());
		dto.setCText(wefText.getPlainText());
		dto.setBEmpfangsbestaetigung(Helper
				.boolean2Short(wcbEmpfangsbestaetigung.isSelected()));
		// Empfaengeradresse pruefen
		boolean bValid = Helper.validateEmailadresse(super.wtfEmpfaenger
				.getText());
		if (bValid) {
			// falls es einen CC gibt, auch den pruefen
			if (wtfCCEmpfaenger.getText() != null
					&& !wtfCCEmpfaenger.getText().trim().equals("")) {
				if (Helper.validateEmailadresse(wtfCCEmpfaenger.getText())) {
					// gut
				} else {
					bValid = false;
				}
			}
		}

		// Auch Absender pruefen
		if (bValid) {
			bValid = Helper.validateEmailadresse(wtfAbsender.getText());
		}

		// wenn eine ungueltige Adresse dabei ist -> fehler
		if (!bValid) {
			throw new ExceptionLP(
					EJBExceptionLP.FEHLER_UNGUELTIGE_EMAILADRESSE,
					new Exception("Ung\u00FCltige Emailadresse"));
		}
		return dto;
	}

	/**
	 * Vorschlagswert fuer den Empfaenger setzen.
	 * 
	 * @throws Throwable
	 */
	protected void setVorschlag() throws Throwable {
		if (partnerDtoEmpfaenger != null) {

			String report = jpaPanelReportIf.getReportname();

			if (panelReportKriterien.getPanelStandardDrucker().getVariante() != null) {
				ReportvarianteDto varDto = DelegateFactory
						.getInstance()
						.getDruckerDelegate()
						.reportvarianteFindByPrimaryKey(
								panelReportKriterien.getPanelStandardDrucker()
										.getVariante());
				report = varDto.getCReportnamevariante();
			}

			String ubersteuerterEmpfaenger = DelegateFactory
					.getInstance()
					.getAnsprechpartnerDelegate()
					.getUebersteuerteEmpfaenger(partnerDtoEmpfaenger, report,
							true);
			if (bUebersteuerterEmpaengerVorschlagGesetzt == false
					&& ubersteuerterEmpfaenger != null
					&& ubersteuerterEmpfaenger.length() > 0) {
				setEmpfaenger(ubersteuerterEmpfaenger);
				bUebersteuerterEmpaengerVorschlagGesetzt = true;
			} else {

				String eMailAusSpediteur = null;

				if (jpaPanelReportIf instanceof ReportLieferschein) {
					LieferscheinDto lsDto = ((ReportLieferschein) jpaPanelReportIf)
							.getLieferscheinDto();
					if (lsDto != null && lsDto.getSpediteurIId() != null) {
						eMailAusSpediteur = DelegateFactory
								.getInstance()
								.getMandantDelegate()
								.spediteurFindByPrimaryKey(
										lsDto.getSpediteurIId()).getCEmail();
					}
				}

				else if (jpaPanelReportIf instanceof ReportAbholauftrag) {

					BestellungDto bestellungDto = DelegateFactory
							.getInstance()
							.getBestellungDelegate()
							.bestellungFindByPrimaryKey(
									((ReportAbholauftrag) jpaPanelReportIf)
											.getBestellungIId());

					if (bestellungDto != null
							&& bestellungDto.getSpediteurIId() != null) {
						eMailAusSpediteur = DelegateFactory
								.getInstance()
								.getMandantDelegate()
								.spediteurFindByPrimaryKey(
										bestellungDto.getSpediteurIId())
								.getCEmail();
					}
				}

				if (bUebersteuerterEmpaengerVorschlagGesetzt == false
						&& eMailAusSpediteur != null) {
					setEmpfaenger(eMailAusSpediteur);
					bUebersteuerterEmpaengerVorschlagGesetzt = true;
				} else {
					Integer partnerIIdAnsprechpartner = null;
					if (ansprechpartnerIId != null) {
						partnerIIdAnsprechpartner = DelegateFactory
								.getInstance()
								.getAnsprechpartnerDelegate()
								.ansprechpartnerFindByPrimaryKey(
										ansprechpartnerIId)
								.getPartnerIIdAnsprechpartner();
					}
					String p = DelegateFactory
							.getInstance()
							.getPartnerDelegate()
							.partnerkommFindOhneExec(
									partnerDtoEmpfaenger.getIId(),
									partnerIIdAnsprechpartner,
									PartnerFac.KOMMUNIKATIONSART_EMAIL,
									LPMain.getTheClient().getMandant());
					if (p != null) {
						setEmpfaenger(p);
					}

				}
			}

		}
	}

	protected void setVorschlagCC() throws Throwable {
		if (partnerDtoEmpfaengerCC != null) {

			String report = jpaPanelReportIf.getReportname();

			if (panelReportKriterien.getPanelStandardDrucker().getVariante() != null) {
				ReportvarianteDto varDto = DelegateFactory
						.getInstance()
						.getDruckerDelegate()
						.reportvarianteFindByPrimaryKey(
								panelReportKriterien.getPanelStandardDrucker()
										.getVariante());
				report = varDto.getCReportnamevariante();
			}

			String ubersteuerterEmpfaenger = DelegateFactory
					.getInstance()
					.getAnsprechpartnerDelegate()
					.getUebersteuerteEmpfaenger(partnerDtoEmpfaengerCC, report,
							true);

			if (bUebersteuerterEmpaengerVorschlagCCGesetzt == false
					&& ubersteuerterEmpfaenger != null
					&& ubersteuerterEmpfaenger.length() > 0) {
				setEmpfaengerCC(ubersteuerterEmpfaenger);
				bUebersteuerterEmpaengerVorschlagCCGesetzt = true;
			} else {
				Integer partnerIIdAnsprechpartner = null;
				if (ansprechpartnerIIdCC != null) {
					partnerIIdAnsprechpartner = DelegateFactory
							.getInstance()
							.getAnsprechpartnerDelegate()
							.ansprechpartnerFindByPrimaryKey(
									ansprechpartnerIIdCC)
							.getPartnerIIdAnsprechpartner();
				}
				String p = DelegateFactory
						.getInstance()
						.getPartnerDelegate()
						.partnerkommFindOhneExec(
								partnerDtoEmpfaengerCC.getIId(),
								partnerIIdAnsprechpartner,
								PartnerFac.KOMMUNIKATIONSART_EMAIL,
								LPMain.getTheClient().getMandant());
				if (p != null) {
					setEmpfaengerCC(p);
				}

			}

		}
	}

	public void setEmpfaengerCC(String sEmpfaenger) {
		wtfCCEmpfaenger.setText(sEmpfaenger);
	}

	public void setBetreff(String betreff) {
		wtfBetreff.setText(betreff);
	}

	public void setEditorFieldVisible(boolean bVisible) {
		wefText.setVisible(bVisible);
		wlaText.setVisible(bVisible);
	}

	protected WrapperButton getwbuAnhangWaehlen() {
		return wbuAnhangWaehlen;
	}
	protected WrapperButton getwbuAnhangLoeschen() {
		return wbuAnhangLoeschen;
	}

	protected WrapperTextField getwtfCCEmpfaenger() {
		return wtfCCEmpfaenger;
	}

	public void setjtfAnhaengeText(String text) {
		jtfAnhaenge.setText(text);
	}

	public String getjtfAnhaengeText() {
		return jtfAnhaenge.getText();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals(PanelVersand.ACTION_SPECIAL_PARTNER)) {
			try {
				panelQueryFLRPartner = PartnerFilterFactory.getInstance()
						.createPanelFLRPartner(getInternalFrame());
				new DialogQuery(panelQueryFLRPartner);
			} catch (Throwable e1) {
				e1.printStackTrace();
			}
		}
		if (e.getActionCommand().equals(ACTION_SPECIAL_PARTNERCC)) {
			try {
				panelQueryFLRPartnerCC = PartnerFilterFactory.getInstance()
						.createPanelFLRPartner(getInternalFrame());
				new DialogQuery(panelQueryFLRPartnerCC);
			} catch (Throwable e1) {
				e1.printStackTrace();
			}
		} else if (e.getActionCommand().equals(
				PanelVersand.ACTION_SPECIAL_ANSPRECHPARTNER)) {
			try {
				if (partnerDtoEmpfaenger != null) {
					ansprechpartnerIId = null;

					panelQueryFLRAnsprechpartner = PartnerFilterFactory
							.getInstance().createPanelFLRAnsprechpartner(
									getInternalFrame(),
									partnerDtoEmpfaenger.getIId(), null, false,
									true);

					new DialogQuery(panelQueryFLRAnsprechpartner);

				}
			} catch (Throwable e1) {
				e1.printStackTrace();
			}
		} else if (e.getActionCommand()
				.equals(ACTION_SPECIAL_ANSPRECHPARTNERCC)) {
			try {
				if (partnerDtoEmpfaengerCC != null) {
					ansprechpartnerIIdCC = null;

					panelQueryFLRAnsprechpartnerCC = PartnerFilterFactory
							.getInstance().createPanelFLRAnsprechpartner(
									getInternalFrame(),
									partnerDtoEmpfaengerCC.getIId(), null,
									false, true);

					new DialogQuery(panelQueryFLRAnsprechpartnerCC);

				} else {
					if (partnerDtoEmpfaenger != null) {
						panelQueryFLRAnsprechpartnerCC = PartnerFilterFactory
								.getInstance().createPanelFLRAnsprechpartner(
										getInternalFrame(),
										partnerDtoEmpfaenger.getIId(), null,
										false, true);

						new DialogQuery(panelQueryFLRAnsprechpartnerCC);
					}
				}

			} catch (Throwable e1) {
				e1.printStackTrace();
			}
		}

	}

	@Override
	public void changed(EventObject eI) {
		ItemChangedEvent e = (ItemChangedEvent) eI;
		try {
			if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {

				if (e.getSource() == panelQueryFLRPartner) {
					wtfEmpfaenger.setText(null);
					ansprechpartnerIId = null;
					Integer key = (Integer) ((ISourceEvent) e.getSource())
							.getIdSelected();

					partnerDtoEmpfaenger = DelegateFactory.getInstance()
							.getPartnerDelegate().partnerFindByPrimaryKey(key);

					AnsprechpartnerDto[] dtos = DelegateFactory.getInstance()
							.getAnsprechpartnerDelegate()
							.ansprechpartnerFindByPartnerIIdOhneExc(key);

					if (dtos != null && dtos.length > 0) {
						panelQueryFLRAnsprechpartner = PartnerFilterFactory
								.getInstance().createPanelFLRAnsprechpartner(
										getInternalFrame(), key, null, false,
										true);

						new DialogQuery(panelQueryFLRAnsprechpartner);

					} else {
						setVorschlag();
					}

				} else if (e.getSource() == panelQueryFLRPartnerCC) {
					wtfCCEmpfaenger.setText(null);
					ansprechpartnerIIdCC = null;
					Integer key = (Integer) ((ISourceEvent) e.getSource())
							.getIdSelected();

					partnerDtoEmpfaengerCC = DelegateFactory.getInstance()
							.getPartnerDelegate().partnerFindByPrimaryKey(key);

					AnsprechpartnerDto[] dtos = DelegateFactory.getInstance()
							.getAnsprechpartnerDelegate()
							.ansprechpartnerFindByPartnerIIdOhneExc(key);

					if (dtos != null && dtos.length > 0) {
						panelQueryFLRAnsprechpartnerCC = PartnerFilterFactory
								.getInstance().createPanelFLRAnsprechpartner(
										getInternalFrame(), key, null, false,
										true);

						new DialogQuery(panelQueryFLRAnsprechpartnerCC);

					} else {
						setVorschlagCC();
					}

				} else if (e.getSource() == panelQueryFLRAnsprechpartner) {

					wtfEmpfaenger.setText(null);
					Integer key = (Integer) ((ISourceEvent) e.getSource())
							.getIdSelected();
					ansprechpartnerIId = key;

					setVorschlag();
				} else if (e.getSource() == panelQueryFLRAnsprechpartnerCC) {
					wtfCCEmpfaenger.setText(null);
					Integer key = (Integer) ((ISourceEvent) e.getSource())
							.getIdSelected();
					ansprechpartnerIIdCC = key;

					partnerDtoEmpfaengerCC = DelegateFactory
							.getInstance()
							.getPartnerDelegate()
							.partnerFindByPrimaryKey(
									DelegateFactory
											.getInstance()
											.getAnsprechpartnerDelegate()
											.ansprechpartnerFindByPrimaryKey(
													ansprechpartnerIIdCC)
											.getPartnerIId());

					setVorschlagCC();
				}

			}

			else if (e.getID() == ItemChangedEvent.ACTION_LEEREN) {
				if (e.getSource() == panelQueryFLRAnsprechpartner) {
					setVorschlag();
				}
				if (e.getSource() == panelQueryFLRAnsprechpartnerCC) {
					setVorschlagCC();
				}
			}
		} catch (Throwable e1) {
			e1.printStackTrace();
		}
	}

	public void installActionListeners(ActionListener l) {
		getWbuSenden().addActionListener(l);
		getwbuAnhangWaehlen().addActionListener(l);
		getwbuAnhangLoeschen().addActionListener(l);
	}
}
