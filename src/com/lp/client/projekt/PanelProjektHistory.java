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
package com.lp.client.projekt;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.Timestamp;
import java.util.EventObject;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.KeyStroke;

import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.DialogEmailHeader;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelPositionenHtmlTexteingabe;
import com.lp.client.frame.component.PanelPositionenTexteingabe;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperHtmlField;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.component.WrapperTimestampField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.frame.report.PanelReportKriterien;
import com.lp.client.partner.ReportKurzbrief;
import com.lp.client.pc.LPMain;
import com.lp.client.system.SystemFilterFactory;
import com.lp.editor.util.LpEditorMessages;
import com.lp.editor.util.TextBlockOverflowException;
import com.lp.server.partner.service.KurzbriefDto;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.personal.service.PersonalDto;
import com.lp.server.projekt.service.HistoryDto;
import com.lp.server.projekt.service.HistoryartDto;
import com.lp.server.projekt.service.ProjektDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MailtextDto;
import com.lp.server.system.service.MandantFac;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.util.Helper;

/**
 * <p>
 * Diese Klasse kuemmert sich ...
 * </p>
 * 
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2008
 * </p>
 * 
 * <p>
 * Erstellung: Vorname Nachname; dd.mm.06
 * </p>
 * 
 * <p>
 * 
 * @author $Author: christian $
 *         </p>
 * 
 * @version not attributable Date $Date: 2012/05/09 09:29:31 $
 */
public class PanelProjektHistory extends PanelBasis implements
		PropertyChangeListener {
	
	private static final long serialVersionUID = 5243100664274910691L;
	private final InternalFrameProjekt intFrame;
	private final TabbedPaneProjekt tpProjekt;
	private HistoryDto historyDto = null;
	private PanelPositionenTexteingabe panelPlainText = null;
	private WrapperTimestampField wdfWann = null;

	private WrapperLabel wlaTitel = new WrapperLabel();
	private WrapperTextField wtfTitel = new WrapperTextField();

	private WrapperButton wbuHistoryart = new WrapperButton();
	private WrapperTextField wtfHistoryart = new WrapperTextField();

	private PanelQueryFLR panelQueryFLRHistoryart = null;
	private PanelPositionenHtmlTexteingabe panelHtmlText = null ;
	private PanelBasis panelText = null ;
	private int panelTextRow = 0 ;

	static final public String ACTION_SPECIAL_HISTORYART_FROM_LISTE = "ACTION_SPECIAL_HISTORYART_FROM_LISTE";
	private static final String ACTION_SPECIAL_EMAIL = "action_special_"
			+ ALWAYSENABLED + "reportkriterien_email";

	private String[] toolbarButtonsPlain = { PanelBasis.ACTION_UPDATE,
			PanelBasis.ACTION_SAVE, PanelBasis.ACTION_DISCARD};
	private String[] toolbarButtonsHtml =  { PanelBasis.ACTION_UPDATE,
			PanelBasis.ACTION_SAVE, PanelBasis.ACTION_DISCARD, ACTION_SPECIAL_EMAIL};

	/**
	 * Konstruktor.
	 * 
	 * @param internalFrame
	 *            der InternalFrame auf dem das Panel sitzt
	 * @param add2TitleI
	 *            der default Titel des Panels
	 * @param key
	 *            PK des Projektes
	 * @throws java.lang.Throwable
	 *             Ausnahme
	 */
	public PanelProjektHistory(InternalFrame internalFrame, String add2TitleI,
			Object key) throws Throwable {
		super(internalFrame, add2TitleI, key);
		intFrame = (InternalFrameProjekt) internalFrame;
		tpProjekt = intFrame.getTabbedPaneProjekt();
		jbInitPanel();
		initComponents();
	}

	private void jbInitPanel() throws Throwable {
		boolean hasEmail = false ;
		if (LPMain.getInstance().getDesktop().darfAnwenderAufZusatzfunktionZugreifen(
						MandantFac.ZUSATZFUNKTION_EMAIL_CLIENT)) {
			createAndSaveButton(
					"/com/lp/client/res/mail.png",
					LPMain.getTextRespectUISPr("lp.drucken.alsemailversenden"),
					ACTION_SPECIAL_EMAIL, KeyStroke.getKeyStroke('E',
							java.awt.event.InputEvent.CTRL_MASK), null);
			hasEmail = true ;
		}
		enableToolsPanelButtons(hasEmail ? toolbarButtonsHtml : toolbarButtonsPlain) ;
		enableToolsPanelButtons(false, ACTION_SPECIAL_EMAIL);
//		enableToolsPanelButtons(toolbarButtonsPlain);

		// das Aussenpanel hat immer das Gridbaglayout und einen Rahmen nach
		// innen
		setLayout(new GridBagLayout());
		setBorder(BorderFactory.createEmptyBorder(3, 3, 10, 3));

		getInternalFrame().addItemChangedListener(this);

		Timestamp datCurrentDate = new Timestamp(System.currentTimeMillis());
		wdfWann = new WrapperTimestampField();
		wdfWann.setMandatoryField(true);
		wdfWann.setTimestamp(datCurrentDate);
		// wdfWann.setMinimumValue(datCurrentDate);
		wdfWann.getWdfDatum().getDisplay().addPropertyChangeListener(this);
		wlaTitel.setText(LPMain.getTextRespectUISPr("lp.titel"));
		wbuHistoryart.setText(LPMain.getTextRespectUISPr("lp.art")+"...");
		wbuHistoryart.setActionCommand(ACTION_SPECIAL_HISTORYART_FROM_LISTE);
		wbuHistoryart.addActionListener(this);
		wtfTitel.setColumnsMax(80);
		wtfHistoryart.setActivatable(false);

		// Zeile - die Toolbar
		add(getToolsPanel(), new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
				new Insets(0, 0, 0, 0), 0, 0));
		add(wdfWann, new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
				new Insets(6, 0, 0, 0), 200, 0));
		iZeile++;
		this.add(wlaTitel, new GridBagConstraints(0, iZeile, 1, 1, 0.2, 0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						2, 0, 2, 2), 0, 0));
		this.add(wtfTitel, new GridBagConstraints(1, iZeile, 1, 1, 1.0, 0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						2, 0, 2, 0), 0, 0));
		iZeile++;
		this.add(wbuHistoryart, new GridBagConstraints(0, iZeile, 1, 1, 0,
				0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 0, 2, 0), 0, 0));
		this.add(wtfHistoryart, new GridBagConstraints(1, iZeile, 1, 1, 0,
				0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 0, 2, 0), 0, 0));

		iZeile++;

		panelPlainText = new PanelPositionenTexteingabe(getInternalFrame(),
				LocaleFac.POSITIONSART_TEXTEINGABE, getKeyWhenDetailPanel());
		
		if(hasEmail) {
			panelHtmlText = new PanelPositionenHtmlTexteingabe(getInternalFrame(),
					LocaleFac.POSITIONSART_TEXTEINGABE, getKeyWhenDetailPanel());

		}
		panelTextRow = iZeile ;
//		panelText = panelPlainText ;
		panelText = null ;
		addTextPanelToGrid(false); 
		
		// Statusbar an den unteren Rand des Panels haengen
		iZeile++;
		this.add(getPanelStatusbar(), new GridBagConstraints(0, iZeile, 2, 1, 1.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
	}

	private void addTextPanelToGrid(boolean isHtml) {
		if(panelText != null) {
			if(isHtml && panelText.equals(panelHtmlText)) return ;
			if(!isHtml && panelText.equals(panelPlainText)) return ;
			
	 		this.remove(panelText) ;			
		}
		
		panelText = isHtml ? panelHtmlText : panelPlainText ;
		this.add(panelText,
				new GridBagConstraints(0, panelTextRow, 2, 1, 1.0, 1.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						0, 0, 0, 0), 0, 0));
	}
	
	protected String getLockMeWer() {
		return HelperClient.LOCKME_PROJEKT;
	}

	private void setDefaults() throws Throwable {
		historyDto = new HistoryDto();
		leereAlleFelder(this);
		wdfWann.setTimestamp(new java.sql.Timestamp(System.currentTimeMillis()));
	}

	private void dto2Components() throws Throwable {
		wdfWann.setTimestamp(historyDto.getTBelegDatum());
		wtfTitel.setText(historyDto.getCTitel());

		if (historyDto.getHistoryartIId() != null) {
			HistoryartDto historyartDto = DelegateFactory.getInstance()
					.getProjektServiceDelegate()
					.historyartFindByPrimaryKey(historyDto.getHistoryartIId());
			wtfHistoryart.setText(historyartDto.getCBez());
		} else {
			wtfHistoryart.setText(null);
		}
		if(isHtmlEnabled()) {
			panelHtmlText.setText(historyDto.getXText());
		} else {
			panelPlainText.setText(historyDto.getXText());
		}
		if(historyDto.getIId() != null && DelegateFactory.getInstance()
				.getEmailMediaDelegate().hasHistoryEmailReferenz(
				tpProjekt.getProjektDto().getIId(), historyDto.getIId())) {
			getToolBar().enableToolsPanelButtons(false, ACTION_UPDATE, ACTION_DELETE);
		}
	}

	public void eventActionNew(EventObject eventObject, boolean bLockMeI,
			boolean bNeedNoNewI) throws Throwable {
		super.eventActionNew(eventObject, true, false);

		boolean htmlModeRequired = isHtmlEnabled() ;
		switchTextPanels(htmlModeRequired) ;
		if(!bNeedNoNewI) {
			historyDto = new HistoryDto() ;
			historyDto.setBHtml(Helper.boolean2Short(htmlModeRequired));
		}
		setDefaults();
		clearStatusbar();
	}

	private JDialog emailDialog ;
	
	
	private JDialog createEmailDialog() throws Throwable {
		ProjektDto projektDto = intFrame.getTabbedPaneProjekt().getProjektDto() ;
		KurzbriefDto kurzbriefDto = new KurzbriefDto() ;
		kurzbriefDto.setPartnerIId(projektDto.getPartnerIId());
		kurzbriefDto.setAnsprechpartnerIId(projektDto.getAnsprechpartnerIId());
		kurzbriefDto.setBHtml(historyDto.getBHtml());
		kurzbriefDto.setCBetreff(historyDto.getCTitel());
		kurzbriefDto.setXText(historyDto.getXText());
		kurzbriefDto.setPersonalIIdAendern(LPMain.getTheClient().getIDPersonal());
		kurzbriefDto.setPersonalIIdAnlegen(LPMain.getTheClient().getIDPersonal());

		MailtextDto mailtextDto = new MailtextDto() ;
		mailtextDto.setMailAnprechpartnerIId(kurzbriefDto.getAnsprechpartnerIId());
		mailtextDto.setMailBetreff(kurzbriefDto.getCBetreff());
		mailtextDto.setMailPartnerIId(kurzbriefDto.getPartnerIId());
		mailtextDto.setMailText(kurzbriefDto.getXText());
		mailtextDto.setMailBelegnummer(projektDto.getCNr());

		PersonalDto mailBearbeiterDto = DelegateFactory.getInstance().getPersonalDelegate()
				.personalFindByPrimaryKey(kurzbriefDto.getPersonalIIdAnlegen()) ;
		mailtextDto.setMailVertreter(mailBearbeiterDto);
		
		PartnerDto partnerDto = DelegateFactory.getInstance().getPartnerDelegate()
				.partnerFindByPrimaryKey(projektDto.getPartnerIId()) ;

		ReportKurzbrief reportKurzbrief = new ReportKurzbrief(getInternalFrame(), 
				kurzbriefDto, kurzbriefDto.getCBetreff(), kurzbriefDto.getPartnerIId());

		PanelReportKriterien panelReportKriterien = new PanelReportKriterien(getInternalFrame(), reportKurzbrief,
				"Titel Kriterien", partnerDto, kurzbriefDto.getAnsprechpartnerIId(), true,true, false) ;			

		WrapperHtmlField cloneHtmlField = new WrapperHtmlField(
				getInternalFrame(), LPMain.getTextRespectUISPr("lp.bemerkung"), true) ;
		cloneHtmlField.setText(historyDto.getXText());
		DialogEmailHeader emailDialog = new DialogEmailHeader(
				getInternalFrame(), partnerDto, cloneHtmlField, panelReportKriterien, mailtextDto) ;
		return emailDialog ;
	}
	
	
	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		if (e.getActionCommand().equals(ACTION_SPECIAL_HISTORYART_FROM_LISTE)) {
			String[] aWhichButtonIUse = { PanelBasis.ACTION_REFRESH,
					PanelBasis.ACTION_FILTER, PanelBasis.ACTION_LEEREN };

			panelQueryFLRHistoryart = new PanelQueryFLR(null, null,
					QueryParameters.UC_ID_HISTORYART, aWhichButtonIUse,
					getInternalFrame(),
					LPMain.getTextRespectUISPr("proj.historyart"));
			panelQueryFLRHistoryart.befuellePanelFilterkriterienDirekt(
					SystemFilterFactory.getInstance().createFKDBezeichnung(),
					null);
			if (historyDto.getHistoryartIId() != null) {
				panelQueryFLRHistoryart.setSelectedId(historyDto
						.getHistoryartIId());
			}

			new DialogQuery(panelQueryFLRHistoryart);
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_EMAIL)) {
			emailDialog = createEmailDialog() ;
			emailDialog.setVisible(true) ;
		}
	}

	private boolean isTextPanelFilled() throws Throwable {
		return isHtmlEnabled() ? panelHtmlText.hasContent() : panelPlainText.hasContent() ;
	}
	
	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {
		if (allMandatoryFieldsSetDlg()) {
			try {
				if (!isTextPanelFilled()) {
					showDialogPflichtfelderAusfuellen();
					return;
				}
			} catch (TextBlockOverflowException ex) {
				showDialogTextZuLang();
				return;
			}
			if (historyDto.getIId() == null) {
				components2Dto();
				Integer historyIId = DelegateFactory.getInstance()
						.getProjektDelegate().createHistory(historyDto);
				historyDto = DelegateFactory.getInstance().getProjektDelegate()
						.historyFindByPrimaryKey(historyIId);
				setKeyWhenDetailPanel(historyIId);
			} else {
				components2Dto();
				DelegateFactory.getInstance().getProjektDelegate()
						.updateHistory(historyDto);
			}
			super.eventActionSave(e, true);
			eventYouAreSelected(false);
		}
	}

	private void showDialogTextZuLang() {
		DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"),
			LpEditorMessages.getInstance().getString("Status.BufferOverflow"));
	}
	protected void eventActionDelete(ActionEvent e,
			boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {
		DelegateFactory.getInstance().getProjektDelegate()
				.removeHistory(historyDto);
		this.setKeyWhenDetailPanel(null);
		super.eventActionDelete(e, false, false);
	}

	private void components2Dto() throws Throwable {
		historyDto.setProjektIId(tpProjekt.getProjektDto().getIId());
		if(isHtmlEnabled()) {
			String theText = panelHtmlText.getText() ;
			historyDto.setXText(theText) ;					
		} else {
			historyDto.setXText(panelPlainText.getText());			
		}

		historyDto.setTBelegDatum(wdfWann.getTimestamp());
		historyDto.setCTitel(wtfTitel.getText());

	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;
		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			if (e.getSource() == panelQueryFLRHistoryart) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				HistoryartDto historyartDto = DelegateFactory.getInstance()
						.getProjektServiceDelegate()
						.historyartFindByPrimaryKey((Integer) key);
				wtfHistoryart.setText(historyartDto.getCBez());
				historyDto.setHistoryartIId(historyartDto.getIId());
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_LEEREN) {
			if (e.getSource() == panelQueryFLRHistoryart) {
				wtfHistoryart.setText(null);
				historyDto.setHistoryartIId(null);
			}
		}
		aktualisiereStatusbar();
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {
		super.eventYouAreSelected(false);
		Object key = getKeyWhenDetailPanel();
		if (key == null || key.equals(LPMain.getLockMeForNew())) {
			// Neu.
			leereAlleFelder(this);
			panelPlainText.removeContent();
			clearStatusbar();
			wdfWann.setTimestamp(new java.sql.Timestamp(System
					.currentTimeMillis()));
		} else {
			// Update.
			historyDto = DelegateFactory.getInstance().getProjektDelegate()
					.historyFindByPrimaryKey((Integer) key);
			dto2Components();
		}
		switchTextPanels(isHtmlEnabled()) ;
		tpProjekt.setTitleProjekt(LPMain
				.getTextRespectUISPr("proj.projekt.details"));
		aktualisiereStatusbar();
	}

	private void aktualisiereStatusbar() throws Throwable {
		setStatusbarPersonalIIdAnlegen(tpProjekt.getProjektDto()
				.getPersonalIIdAnlegen());
		setStatusbarTAnlegen(tpProjekt.getProjektDto().getTAnlegen());
		setStatusbarPersonalIIdAendern(tpProjekt.getProjektDto()
				.getPersonalIIdAendern());
		setStatusbarTAendern(tpProjekt.getProjektDto().getTAendern());
		if(historyDto != null) {
			setStatusbarPersonalIIdAendern(historyDto.getPersonalIId()) ;
			setStatusbarTAendern(historyDto.getTBelegDatum()) ;
		}
		
		setStatusbarStatusCNr(tpProjekt.getProjektDto().getStatusCNr());
	}

	public void propertyChange(PropertyChangeEvent evt) {
	}
	
	public void bePlain() {
		if(historyDto != null) {
			historyDto.setBHtml(Helper.getShortFalse()); 
		}
		try {
			enableToolsPanelButtons(false, ACTION_SPECIAL_EMAIL);
			// enableToolsPanelButtons(toolbarButtonsPlain);
		} catch(Exception e) {}
	}
	
	
	public void beHtml() {
		if(historyDto != null) {
			historyDto.setBHtml(Helper.getShortTrue());
		}
		try {
			enableToolsPanelButtons(true, ACTION_SPECIAL_EMAIL);
//			enableToolsPanelButtons(toolbarButtonsHtml);
		} catch(Exception e) {}
	}
	
	public void enableHtml(boolean enable) {
		if(historyDto != null) {
			historyDto.setBHtml(Helper.boolean2Short(enable));
		}
		
		try {
			enableToolsPanelButtons(enable, ACTION_SPECIAL_EMAIL);
//			enableToolsPanelButtons(enable ? toolbarButtonsHtml : toolbarButtonsPlain);
		} catch(Exception e) {}
	}
	
	public boolean isHtmlEnabled() {
		if(historyDto == null) return false ;
		return Helper.short2boolean(historyDto.getBHtml()) ;
	}

	public void beEditMode(boolean htmlMode) {
		if(htmlMode && isHtmlEnabled()) {
			switchTextPanels(htmlMode);
			if(historyDto != null) {
				historyDto.setBHtml(Helper.boolean2Short(htmlMode));
			}
			panelHtmlText.startEditing() ;
		}
 	}
	
	private void switchTextPanels(boolean isHtml) {
		addTextPanelToGrid(isHtml) ; 
		try {
			enableToolsPanelButtons(isHtml, ACTION_SPECIAL_EMAIL);
		} catch(Exception e) {}
	}
}
