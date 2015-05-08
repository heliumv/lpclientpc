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
 *******************************************************************************/
package com.lp.client.frame.component;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.regex.PatternSyntaxException;

import javax.swing.JDialog;

import net.miginfocom.swing.MigLayout;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.frame.report.PanelReportKriterien;
import com.lp.client.frame.report.PanelVersand;
import com.lp.client.frame.report.PanelVersandEmail;
import com.lp.client.frame.report.PanelVersandHtmlEmail;
import com.lp.client.pc.LPMain;
import com.lp.editor.util.TextBlockOverflowException;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MailtextDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.VersandanhangDto;
import com.lp.server.system.service.VersandauftragDto;
import com.lp.util.Helper;

public class DialogEmailHeader extends JDialog implements ActionListener {
	private static final long serialVersionUID = 3658438666700459228L;
	private PanelVersandEmail panelEmail ;
	private WrapperHtmlField htmlField ;
	private InternalFrame internalFrame ;
	private PartnerDto partnerDto ;
	private PanelReportKriterien panelReportKriterien ;
	private MailtextDto mailtextDto ;
	
	public DialogEmailHeader(InternalFrame internalFrame, PartnerDto partnerDto,
			WrapperHtmlField htmlField, PanelReportKriterien panelReportKriterien) throws TextBlockOverflowException, Throwable {
		this(internalFrame, partnerDto, htmlField, panelReportKriterien, new MailtextDto()) ;
	}
	
	public DialogEmailHeader(InternalFrame internalFrame, PartnerDto partnerDto,
			WrapperHtmlField htmlField, PanelReportKriterien panelReportKriterien, MailtextDto mailtextDto) throws TextBlockOverflowException, Throwable{
		super(LPMain.getInstance().getDesktop(), true) ;
		
		this.internalFrame = internalFrame ;
		this.partnerDto = partnerDto ;
		this.htmlField = htmlField ;
		this.panelReportKriterien = panelReportKriterien ;
		this.mailtextDto = mailtextDto ;
		panelEmail = createPanelVersandEmail(panelReportKriterien, mailtextDto) ;
		
		setupView() ;		
	}

	private void setupView() {
		Container container = getContentPane() ;
		container.setLayout(new MigLayout("wrap1", "[fill, grow]", "[fill][fill, grow]"));
		container.add(panelEmail) ;
		container.add(htmlField) ;	
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		pack();
		Dimension d = Defaults.getInstance().bySizeFactor(600, 400) ;
		setMinimumSize(d);
		setPreferredSize(d);
	    LPMain.getInstance().getDesktop().platziereDialogInDerMitteDesFensters(this);
	}
	
	private PanelVersandEmail createPanelVersandEmail(
			PanelReportKriterien panelReportKriterien, MailtextDto mtDto) throws TextBlockOverflowException, Throwable {
		if (panelEmail == null) {
			String belegartCNr = LocaleFac.BELEGART_PARTNER ;
			Integer belegIId = null;

//			mtDto.setMailText(htmlField.getText()) ;
			mtDto.setParamMandantCNr(LPMain.getTheClient().getMandant());
			mtDto.setParamXslFile(""); 
			mtDto.setParamLocale(LPMain.getTheClient().getLocUi());

//			if (mtDto != null) {
//				if (panelStandardDrucker.getVariante() != null) {
//					ReportvarianteDto varDto = DelegateFactory
//							.getInstance()
//							.getDruckerDelegate()
//							.reportvarianteFindByPrimaryKey(
//									panelStandardDrucker.getVariante());
//					mtDto.setParamXslFile(varDto.getCReportnamevariante());
//				}
//			}

			panelEmail = new PanelVersandHtmlEmail(getInternalFrame(), mtDto, belegartCNr, belegIId,
					panelReportKriterien.getPanelReportIfJRDS(), panelReportKriterien,
					getPartnerDto(), getHtmlField());
			panelEmail.setDefaultAbsender(getPartnerDto(), mtDto.getMailAnprechpartnerIId());
			// Direktversand
			panelEmail.setEditorFieldVisible(false);
			panelEmail.installActionListeners(this);
		}

		return panelEmail;
	}
	
	private InternalFrame getInternalFrame() {
		return internalFrame ;
	}
	
	private PartnerDto getPartnerDto() {
		return partnerDto ;
	}
	
	public WrapperHtmlField getHtmlField() {
		return htmlField ;
	}

	public PanelReportKriterien getPanelReportKriterien() {
		return panelReportKriterien ;
	}
	
	private boolean addAttachments(VersandauftragDto versandauftragDto, String[] attachments) throws 
		ExceptionLP, FileNotFoundException, Throwable {
		int size = 0;
		for (String attachment : attachments) {
			File file = new File(attachment);
			size += file.length() / 1024; 			
		}
			
		int maxsize = Integer.parseInt(DelegateFactory.getInstance()
				.getParameterDelegate().getMandantparameter(
						LPMain.getTheClient().getMandant(),
								"ALLGEMEIN", ParameterFac.PARAMETER_SIZE_E_MAILANHANG)
						.getCWert());
		if (maxsize <= size) {
			DialogFactory
					.showModalDialog(
							LPMain.getTextRespectUISPr("lp.error"),
							LPMain.getTextRespectUISPr("lp.drucken.anhangzugros")
									+ " (" + maxsize + "KB)");
			return false ;
		}
		
		VersandanhangDto versandanhangDto = new VersandanhangDto();
		for (String attachment : attachments) {
			File f = new File(attachment);
			versandanhangDto.setVersandauftragIId(versandauftragDto.getIId());
			versandanhangDto.setCDateiname(f.getName());
			FileInputStream fiStream = new FileInputStream(f);
			byte[] fileData = new byte[(int) f.length()];
			fiStream.read(fileData);
			fiStream.close();
			versandanhangDto.setOInhalt(fileData);
			DelegateFactory.getInstance()
					.getVersandDelegate()
					.createVersandanhang(versandanhangDto);
		}
		
		return true ;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			if (e.getActionCommand().equals(PanelVersand.ACTION_SPECIAL_SENDEN)) {
				VersandauftragDto dto = panelEmail.getVersandauftragDto() ;
				String mailText = htmlField.getHtmlText() ;
				mailText = mailText.replaceFirst("<head>", 
						"<head>" +
								"<meta name=\"Generator\" content=\"HELIUM V 5 11 8140\">" +
								"<meta name=\"ReferenceId\" content=\"" + mailtextDto.getMailBelegnummer() + "\"") ;
				dto.setCText(mailText);
				
				String[] attachments = null ;
				String attachmentText = panelEmail.getjtfAnhaengeText() ;
				if(!Helper.isStringEmpty(attachmentText)) {
					try {
						attachments = attachmentText.split(";") ;
					} catch(PatternSyntaxException e1) {}
				}
				
				/*
				 * TODO: Das ist doch Quatsch mit den Attachments?! Versandauftrag erzeugt, aber 
				 * moeglicherweise Fehler beim Attach. Dann ist doch der Versandauftrag inkomplett
				 */				
				boolean hasAttachments = (attachments != null) && attachments.length > 0 ; 
				VersandauftragDto versandauftragDto = DelegateFactory.getInstance()
						.getVersandDelegate().updateVersandauftrag(dto, hasAttachments) ;
				if(hasAttachments) {
					addAttachments(versandauftragDto, attachments) ;
				}
				setVisible(false);
				dispose();
			} else if (e.getActionCommand().equals(
					PanelVersandEmail.ACTION_SPECIAL_ATTACHMENT)) {
				String sAttachments = panelEmail.getjtfAnhaengeText();
				File[] files = HelperClient.chooseFile(this, "*.*", true);
				if (files != null && files.length > 0) {
					for (int i = 0; i < files.length; i++) {
						sAttachments += files[i].getAbsolutePath() + ";";
					}
					panelEmail.setjtfAnhaengeText(sAttachments);
				}
			}
		} catch(Throwable t) {
			System.out.println("Throwable t " + t.getMessage()) ;
		}		
	}
}	
