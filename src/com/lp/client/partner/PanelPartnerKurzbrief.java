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
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.border.Border;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.DialogEmailHeader;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperDateField;
import com.lp.client.frame.component.WrapperEditorField;
import com.lp.client.frame.component.WrapperHtmlField;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.report.PanelReportKriterien;
import com.lp.client.frame.report.PanelVersand;
import com.lp.client.frame.report.PanelVersandEmail;
import com.lp.client.pc.LPMain;
import com.lp.client.personal.InternalFramePersonal;
import com.lp.server.partner.service.AnsprechpartnerDto;
import com.lp.server.partner.service.KurzbriefDto;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.personal.service.PersonalDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MailtextDto;
import com.lp.server.system.service.MandantFac;
import com.lp.server.system.service.VersandauftragDto;
import com.lp.util.Helper;

/**
 * <p> Diese Klasse kuemmert sich um den Kurzbrief.</p>
 *
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 *
 * <p>Erstellung: Josef Ornetsmueller; 16.11.05</p>
 *
 * <p>@author $Author: christian $</p>
 *
 * @version not attributable Date $Date: 2012/09/06 14:16:08 $
 */
public class PanelPartnerKurzbrief extends PanelBasis {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel jpaWorkingOn = null;
	private Border border = null;
	private JPanel panelButtonAction = null;
	private GridBagLayout gridBagLayoutAll = null;
	private GridBagLayout gridBagLayout = null;

	private WrapperButton wbuAnsprechpartner = null;
	private WrapperTextField wtfAnsprechpartner = null;
	private PanelQueryFLR panelQueryFLRAnsprechpartnerAuswahl = null;

	private WrapperLabel wlaBetreff = null;
	private WrapperTextField wtfBetreff = null;

	private WrapperLabel wlaDatum = null;
	private WrapperDateField wdfDatum = null;

	private WrapperEditorField wefText = null;
	private String belegartCNr = null;
	private KurzbriefDto kurzbriefDto = null;
	
	private WrapperHtmlField whtmlText = null ;
	private PanelVersandEmail panelVersandEmail = null;
	

	static final private String ACTION_SPECIAL_FLR_ANSPRECHPARTNER = "action_special_flr_ansprechpartner";
	public static final String ACTION_SPECIAL_WEITERLEITEN = "action_special_weiterleiten";
	private static final String ACTION_SPECIAL_EMAIL = "action_special_"
			+ ALWAYSENABLED + "reportkriterien_email";

	public PanelPartnerKurzbrief(InternalFrame internalFrame,
			String add2TitleI, Object keyI) throws Throwable {

		super(internalFrame, add2TitleI, keyI);

		jbInit();
		initComponents();
	}

	private KurzbriefDto getKurzbriefDto() {
		if(kurzbriefDto == null) {
			kurzbriefDto = new KurzbriefDto() ;
			kurzbriefDto.setBHtml(Helper.boolean2Short(false));
		}
		return kurzbriefDto ;
	}
	
	public void bePlain() {
		getKurzbriefDto().setBHtml(Helper.boolean2Short(false));
	}
	
	public void beHtml() {
		getKurzbriefDto().setBHtml(Helper.boolean2Short(true));
	}
	
	public void enableHtml(boolean enable) {
		getKurzbriefDto().setBHtml(Helper.boolean2Short(enable));
	}
	
	public boolean isHtmlEnabled() {
		if(kurzbriefDto == null) return false ;
		return Helper.short2boolean(kurzbriefDto.getBHtml()) ;
	}
	
	public void beEditMode(boolean htmlMode) {
		if(htmlMode && isHtmlEnabled()) {
			switchTextPanels(htmlMode);
			kurzbriefDto.setBHtml(Helper.boolean2Short(htmlMode));
			whtmlText.startEditing();
		} 
	}
	
	private PartnerDto getPartnerDto() {

		if (getInternalFrame() instanceof InternalFramePartner) {
			belegartCNr = LocaleFac.BELEGART_PARTNER;
			return ((InternalFramePartner) getInternalFrame()).getTpPartner()
					.getPartnerDto();
		} else if (getInternalFrame() instanceof InternalFrameKunde) {
			belegartCNr = LocaleFac.BELEGART_KUNDE;
			return ((InternalFrameKunde) getInternalFrame()).getKundeDto()
					.getPartnerDto();
		} else if (getInternalFrame() instanceof InternalFrameLieferant) {
			belegartCNr = LocaleFac.BELEGART_LIEFERANT;
			return ((InternalFrameLieferant) getInternalFrame())
					.getLieferantDto().getPartnerDto();
		} else if (getInternalFrame() instanceof InternalFramePersonal) {
			belegartCNr = LocaleFac.BELEGART_PERSONAL;
			return ((InternalFramePersonal) getInternalFrame())
					.getPersonalDto().getPartnerDto();
		}
		return null;
	}

	protected void eventActionPrint(ActionEvent e) throws Throwable {
		String sTitel = DelegateFactory
				.getInstance()
				.getPartnerDelegate()
				.formatFixAnredeTitelName2Name1(
						getPartnerDto(),
						Helper.string2Locale(getPartnerDto()
								.getLocaleCNrKommunikation()))
				+ " | " + kurzbriefDto.getCBetreff();

		ReportKurzbrief reportKurzbrief = new ReportKurzbrief(
				getInternalFrame(), kurzbriefDto, sTitel, getPartnerDto()
						.getIId());
		/**
		 * @todo MR->MR Achtung Logo setzen uebergeben!!
		 */
		// reportKurzbrief.setBPrintLogo(false);

		getInternalFrame().showReportKriterien(reportKurzbrief,
				getPartnerDto(), kurzbriefDto.getAnsprechpartnerIId(), false,
				false);
	}

	/**
	 * jbInit
	 * 
	 * @throws Throwable
	 */
	private void jbInit() throws Throwable {
		getInternalFrame().addItemChangedListener(this);

		boolean hasEmail = false ;
		
		if (LPMain.getInstance().getDesktop()
				.darfAnwenderAufZusatzfunktionZugreifen(
						MandantFac.ZUSATZFUNKTION_EMAIL_CLIENT)) {
			createAndSaveButton(
					"/com/lp/client/res/mail.png",
					LPMain.getTextRespectUISPr("lp.drucken.alsemailversenden"),
					ACTION_SPECIAL_EMAIL, KeyStroke.getKeyStroke('E',
							java.awt.event.InputEvent.CTRL_MASK), null);
			hasEmail = true ;
		}

		
		// Buttons.
		String[] aButton = { PanelBasis.ACTION_UPDATE, PanelBasis.ACTION_SAVE,
				PanelBasis.ACTION_DELETE, PanelBasis.ACTION_DISCARD,
				PanelBasis.ACTION_PRINT };
		
		
		if(hasEmail) {
			aButton = new String[] { PanelBasis.ACTION_UPDATE, PanelBasis.ACTION_SAVE,
					PanelBasis.ACTION_DELETE, PanelBasis.ACTION_DISCARD,
					PanelBasis.ACTION_PRINT, ACTION_SPECIAL_EMAIL } ;
		}
		
		// createAndSaveAndShowButton(
		// "/com/lp/client/res/mail_forward.png",
		// LPMain.getInstance().getTextRespectUISPr(
		// "lp.weiterleiten"),
		// PanelBasis.ACTION_MY_OWN_NEW + ACTION_SPECIAL_WEITERLEITEN);

		enableToolsPanelButtons(aButton);

		border = BorderFactory.createEmptyBorder(10, 10, 10, 10);
		setBorder(border);

		// Das Aussenpanel hat immer das Gridbaglayout.
		gridBagLayoutAll = new GridBagLayout();
		setLayout(gridBagLayoutAll);

		// Actionpanel von Oberklasse holen und einhaengen.
		panelButtonAction = getToolsPanel();
		add(panelButtonAction, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
				new Insets(0, 0, 0, 0), 0, 0));

		jpaWorkingOn = new JPanel();
		gridBagLayout = new GridBagLayout();
		jpaWorkingOn.setLayout(gridBagLayout);
		add(jpaWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
				GridBagConstraints.SOUTH, GridBagConstraints.BOTH, new Insets(
						0, 0, 0, 0), 0, 0));

		// Statusbar an den unteren Rand des Panels haengen.
		add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						0, 0, 0, 0), 0, 0));

		wbuAnsprechpartner = new WrapperButton();
		wbuAnsprechpartner.setText(LPMain.getTextRespectUISPr(
				"button.ansprechpartner.long"));
		wbuAnsprechpartner.setActionCommand(ACTION_SPECIAL_FLR_ANSPRECHPARTNER);
		wbuAnsprechpartner.addActionListener(this);

		wtfAnsprechpartner = new WrapperTextField();
		wtfAnsprechpartner.setActivatable(false);

		wlaBetreff = new WrapperLabel(LPMain.getTextRespectUISPr(
				"label.betreff"));
		wtfBetreff = new WrapperTextField(80);

		wlaDatum = new WrapperLabel(LPMain.getTextRespectUISPr(
				"lp.datum"));

		wdfDatum = new WrapperDateField();
		wdfDatum.setMandatoryField(true);

		wefText = new WrapperEditorField(getInternalFrame(), LPMain
				.getTextRespectUISPr("lp.bemerkung"));

		// Ab hier einhaengen.
		// Zeile
		iZeile++;
		jpaWorkingOn.add(wbuAnsprechpartner, new GridBagConstraints(0, iZeile,
				1, 1, 0.3, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfAnsprechpartner, new GridBagConstraints(1, iZeile,
				1, 1, 0.7, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		// Zeile
		iZeile++;
		jpaWorkingOn.add(wlaBetreff, new GridBagConstraints(0, iZeile, 1, 1, 0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfBetreff, new GridBagConstraints(1, iZeile, 1, 1, 0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wlaDatum, new GridBagConstraints(0, iZeile, 1, 1, 0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wdfDatum, new GridBagConstraints(1, iZeile, 1, 1, 0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));

		// Zeile
		iZeile++;
		jpaWorkingOn.add(wefText, new GridBagConstraints(0, iZeile, 2, 1, 0.0,
				0.5, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		
		iZeile++ ;
		whtmlText = new WrapperHtmlField(getInternalFrame(), LPMain.getTextRespectUISPr("lp.bemerkung"), true);
		jpaWorkingOn.add(whtmlText, new GridBagConstraints(0, iZeile, 2, 1, 0.0,
				0.5, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));		
	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {

		if (e.getActionCommand().equals(ACTION_SPECIAL_FLR_ANSPRECHPARTNER)) {

			panelQueryFLRAnsprechpartnerAuswahl = PartnerFilterFactory
					.getInstance().createPanelFLRAnsprechpartner(
							getInternalFrame(), getPartnerDto().getIId(),
							kurzbriefDto.getAnsprechpartnerIId(), true, true);
			new DialogQuery(panelQueryFLRAnsprechpartnerAuswahl);

		}

		else if (e.getActionCommand().equals(
				PanelBasis.ACTION_MY_OWN_NEW + ACTION_SPECIAL_WEITERLEITEN)) {

			// String sTitel =
			// getInternalFramePartner().getKundeDto().getPartnerDto().formatFixTitelName1Name2()
			// + " | " +
			// getInternalFramePartner().getKurzbriefDto().getCBetreff();
			//
			// getInternalFramePartner().showReportKriterien(
			// new ReportKurzbrief(getInternalFramePartner(),
			// getInternalFramePartner().getKurzbriefDto(),
			// sTitel,
			// getInternalFramePartner().getKundeDto().getIId()),
			// getInternalFrame().getAnsprechpartnerDelegate().
			// ansprechpartnerFindByPrimaryKey(getInternalFramePartner().getKurzbriefDto().
			// getAnsprechpartnerIId()).getPartnerDto(), true);
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_EMAIL)) {
			emailDialog = createEmailDialog() ;
			emailDialog.setVisible(true) ;
		}
		// Versandauftrag senden
		else if (e.getActionCommand().equals(PanelVersand.ACTION_SPECIAL_SENDEN)) {
			VersandauftragDto dto = panelEmail.getVersandauftragDto() ;
			dto.setCText(whtmlText.getHtmlText());
			DelegateFactory.getInstance().getVersandDelegate().updateVersandauftrag(
					dto, false) ;
//			emailDialog.setVisible(false);
		}		
	}

	private JDialog emailDialog ;
	private PanelVersandEmail panelEmail ;
	
	private JDialog createEmailDialog() throws Throwable {
		MailtextDto mailtextDto = new MailtextDto() ;
		mailtextDto.setMailAnprechpartnerIId(kurzbriefDto.getAnsprechpartnerIId());
		mailtextDto.setMailBetreff(kurzbriefDto.getCBetreff());
		mailtextDto.setMailPartnerIId(kurzbriefDto.getPartnerIId());
		mailtextDto.setMailText(kurzbriefDto.getXText());

		PersonalDto mailBearbeiterDto = DelegateFactory.getInstance().getPersonalDelegate()
				.personalFindByPrimaryKey(kurzbriefDto.getPersonalIIdAnlegen()) ;
		mailtextDto.setMailVertreter(mailBearbeiterDto);
		
		
		ReportKurzbrief reportKurzbrief = new ReportKurzbrief(getInternalFrame(), 
				kurzbriefDto, kurzbriefDto.getCBetreff(), getPartnerDto().getIId());

		PanelReportKriterien panelReportKriterien = new PanelReportKriterien(getInternalFrame(), reportKurzbrief,
				"Titel Kriterien", getPartnerDto(), null, true,true, false) ;			

		WrapperHtmlField cloneHtmlField = new WrapperHtmlField(getInternalFrame(), LPMain.getTextRespectUISPr("lp.bemerkung"), true) ;
		cloneHtmlField.setText(kurzbriefDto.getXText());
		DialogEmailHeader emailDialog = new DialogEmailHeader(
				getInternalFrame(), getPartnerDto(), cloneHtmlField, panelReportKriterien, mailtextDto) ;
		return emailDialog ;
	}
	
	
	protected void components2Dto() throws Throwable {
		kurzbriefDto.setBelegartCNr(belegartCNr);
		if (wtfBetreff.getText() != null) {
			kurzbriefDto.setCBetreff(wtfBetreff.getText());
		} else {
			kurzbriefDto.setCBetreff("");
		}
		if(Helper.short2boolean(kurzbriefDto.getBHtml())) {
			String theText = whtmlText.getText() ;
			kurzbriefDto.setXText(theText) ;					
		} else {
			kurzbriefDto.setXText(wefText.getText());			
		}
		
		kurzbriefDto.setPartnerIId(getPartnerDto().getIId());
		kurzbriefDto.setTAendern(wdfDatum.getTimestamp());
	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {

		if (allMandatoryFieldsSetDlg()) {

			components2Dto();

			if (kurzbriefDto.getIId() == null) {
				// create
				Integer key = DelegateFactory.getInstance()
						.getPartnerDelegate().createKurzbrief(kurzbriefDto);
				// diesem panel den key setzen.
				setKeyWhenDetailPanel(key);
				kurzbriefDto.setIId(key);
			} else {
				// update
				DelegateFactory.getInstance().getPartnerDelegate()
						.updateKurzbrief(kurzbriefDto);
			}

			super.eventActionSave(e, true);

			if (getInternalFrame().getKeyWasForLockMe() == null) {
				// der erste eintrag wurde angelegt
				getInternalFrame().setKeyWasForLockMe(
						getKeyWhenDetailPanel() + "");
			}
			eventYouAreSelected(false);
			getInternalFrame().setKeyWasForLockMe(getKeyWhenDetailPanel() + "");

			dto2Components();
		}
	}

	protected void eventItemchanged(EventObject eI) throws ExceptionLP,
			Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;

		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			if (e.getSource() == panelQueryFLRAnsprechpartnerAuswahl) {
				Integer iId = (Integer) ((ISourceEvent) e.getSource())
						.getIdSelected();
				if (iId != null) {
					kurzbriefDto.setAnsprechpartnerIId(iId);
					wtfAnsprechpartner.setText(DelegateFactory.getInstance()
							.getAnsprechpartnerDelegate()
							.ansprechpartnerFindByPrimaryKey(iId)
							.getPartnerDto().formatTitelAnrede());
				}
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_LEEREN) {
			if (e.getSource() == panelQueryFLRAnsprechpartnerAuswahl) {
				kurzbriefDto.setAnsprechpartnerIId(null);
				wtfAnsprechpartner.setText(null);
			}
		}
	}

	private void switchTextPanels(boolean isHtml) {
		if(isHtml) {
			whtmlText.setEnabled(true);
			whtmlText.setVisible(true);			
			wefText.setEnabled(false);
			wefText.setVisible(false);			
		} else {
			whtmlText.setEnabled(false);
			whtmlText.setVisible(false);			
			wefText.setEnabled(true);
			wefText.setVisible(true);			
		}		
	}
	
	
	public void eventActionNew(EventObject eventObject, boolean bLockMeI,
			boolean bNeedNoNewI) throws Throwable {

		super.eventActionNew(eventObject, true, false);
		boolean htmlModeRequired = isHtmlEnabled() ;
		switchTextPanels(htmlModeRequired) ;

		if (!bNeedNoNewI) {
			kurzbriefDto = new KurzbriefDto();
			kurzbriefDto.setBHtml(Helper.boolean2Short(htmlModeRequired));
			setDefaults();
		}
	}

	protected void setDefaults() throws Throwable {
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {

		super.eventYouAreSelected(true);

		if (!bNeedNoYouAreSelectedI) {
			Object key = getKeyWhenDetailPanel();

			String sTitel = "";
			if (key == null
					|| (key != null && key.equals(LPMain.getLockMeForNew()))) {
				leereAlleFelder(this);
				setDefaults();
				clearStatusbar();
				wdfDatum.setTimestamp(new java.sql.Timestamp(System
						.currentTimeMillis()));
				switchTextPanels(false);
			} else {
				kurzbriefDto = DelegateFactory.getInstance()
						.getPartnerDelegate()
						.kurzbriefFindByPrimaryKey((Integer) key);
				sTitel = " | " + kurzbriefDto.getCBetreff();

				switchTextPanels(isHtmlEnabled()) ;
				dto2Components();
			}
			wefText.getLpEditor().setLocaleAsStringFuerSignatur(getPartnerDto().getLocaleCNrKommunikation());
			getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_AS_I_LIKE,
					getPartnerDto().formatFixTitelName1Name2() + sTitel);

//			switchTextPanels(isHtmlEnabled()) ;
			
			if(kurzbriefDto != null && kurzbriefDto.getIId() != null && DelegateFactory.getInstance()
					.getEmailMediaDelegate().hasKurzbriefEmailReferenz(
					kurzbriefDto.getIId(), kurzbriefDto.getPartnerIId())) {
				getToolBar().enableToolsPanelButtons(false, ACTION_UPDATE, ACTION_DELETE);
			}
		}
	}

	protected void dto2Components() throws Throwable {
		wtfBetreff.setText(kurzbriefDto.getCBetreff());
		wdfDatum.setTimestamp(kurzbriefDto.getTAendern());
		if(isHtmlEnabled()) {
			whtmlText.setText(kurzbriefDto.getXText()); 
		} else {
			wefText.setText(kurzbriefDto.getXText());			
		}

		if (kurzbriefDto.getAnsprechpartnerIId() != null) {
			AnsprechpartnerDto anspDto = DelegateFactory
					.getInstance()
					.getAnsprechpartnerDelegate()
					.ansprechpartnerFindByPrimaryKey(
							kurzbriefDto.getAnsprechpartnerIId());
			wtfAnsprechpartner.setText(anspDto.getPartnerDto()
					.formatTitelAnrede());
		} else {
			wtfAnsprechpartner.setText(null);
		}

		setStatusbarPersonalIIdAnlegen(kurzbriefDto.getPersonalIIdAnlegen());
		setStatusbarTAnlegen(kurzbriefDto.getTAnlegen());
	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_PARTNER;
	}

	protected void eventActionDelete(ActionEvent e,
			boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {
		DelegateFactory.getInstance().getPartnerDelegate()
				.removeKurzbrief(kurzbriefDto.getIId());

		kurzbriefDto = new KurzbriefDto();
		this.setKeyWhenDetailPanel(null);
		super.eventActionDelete(e, false, false);
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wtfBetreff;
	}
}
