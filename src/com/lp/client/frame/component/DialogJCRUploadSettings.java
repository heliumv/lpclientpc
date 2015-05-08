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
package com.lp.client.frame.component;

import java.awt.Container;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDialog;
import javax.swing.JLabel;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.system.jcr.service.DokumentbelegartDto;
import com.lp.server.system.jcr.service.DokumentgruppierungDto;
import com.lp.server.system.jcr.service.JCRDocDto;
import com.lp.server.system.jcr.service.JCRDocFac;

public class DialogJCRUploadSettings extends JDialog implements ActionListener {
	
	private static final long serialVersionUID = 1620557508408339022L;

	private JCRDocDto jcr;

	private WrapperLabel wlaBelegart = new WrapperLabel(LPMain
			.getTextRespectUISPr("lp.belegart"));
	private WrapperComboBox wcbBelegart = new WrapperComboBox();
	private WrapperLabel wlaSchlagworte = new WrapperLabel(LPMain
			.getTextRespectUISPr("lp.schlagworte"));
	private WrapperTextField wtfSchlagworte = new WrapperTextField();
	private WrapperLabel wlaSicherheitsstufe = new WrapperLabel(LPMain
			.getTextRespectUISPr("lp.sicherheitsstufe"));
	private WrapperComboBox wcbSicherheitsstufe = new WrapperComboBox();
	private WrapperLabel wlaGruppierung = new WrapperLabel(LPMain
			.getTextRespectUISPr("label.gruppierung"));
	private WrapperComboBox wcbGruppierung = new WrapperComboBox();
	private WrapperButton wbuSpeichern = new WrapperButton(LPMain.getTextRespectUISPr("lp.speichern"));
	
	/**
	 * Zeigt einen Dialog zum Einstellen der Schlagworte, Sicherheitsstufe, etc. eines JCRDocDto.
	 * Die Eigenschaften werden direkt in die uebergebene <code>vorlage</code> zurueckgeschrieben.
	 * @param vorlage Das als Vorlage dienende JCRDocDto
	 * @throws Throwable 
	 * @throws ExceptionLP 
	 */
	public DialogJCRUploadSettings(Frame owner, JCRDocDto vorlage) throws ExceptionLP, Throwable {
		super(owner, LPMain.getTextRespectUISPr("lp.dokumente.eigenschaften"));
		setModal(true);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setLocationRelativeTo(owner);
		setSize(400, 150);
		setResizable(false);
		jcr = vorlage;
		jbInit();
		dto2Components();
		setVisible(true);
	}
	
	private void dto2Components() {
		wcbBelegart.setSelectedItem(jcr.getsBelegart());
		wtfSchlagworte.setText(jcr.getsSchlagworte());
		wcbSicherheitsstufe.setSelectedItem(jcr.getlSicherheitsstufe());
		wcbGruppierung.setSelectedItem(jcr.getsGruppierung());
	}
	
	private void components2Dto() {
		jcr.setsBelegart((String)wcbBelegart.getSelectedItem());
		jcr.setsSchlagworte(wtfSchlagworte.getText());
		jcr.setlSicherheitsstufe((Long)wcbSicherheitsstufe.getSelectedItem());
		jcr.setsGruppierung((String)wcbGruppierung.getSelectedItem());
	}
	
	private void jbInit() throws ExceptionLP, Throwable {
		
		DokumentbelegartDto[] dokumentbelegartDto = DelegateFactory
				.getInstance()
				.getJCRDocDelegate()
				.dokumentbelegartfindbyMandant(
						LPMain.getTheClient().getMandant());
		for (int i = 0; i < dokumentbelegartDto.length; i++) {
			if (!JCRDocFac.DEFAULT_ARCHIV_BELEGART
					.equals(dokumentbelegartDto[i].getCNr()))
				wcbBelegart.addItem(dokumentbelegartDto[i].getCNr());
		}
		DokumentgruppierungDto[] dokumentgruppierungDto = DelegateFactory
				.getInstance()
				.getJCRDocDelegate()
				.dokumentgruppierungfindbyMandant(
						LPMain.getTheClient().getMandant());
		for (int i = 0; i < dokumentgruppierungDto.length; i++) {
			if (!JCRDocFac.DEFAULT_ARCHIV_GRUPPE
					.equals(dokumentgruppierungDto[i].getCNr())
					|| !JCRDocFac.DEFAULT_KOPIE_GRUPPE
							.equals(dokumentgruppierungDto[i].getCNr())
					|| !JCRDocFac.DEFAULT_VERSANDAUFTRAG_GRUPPE
							.equals(dokumentgruppierungDto[i].getCNr())) {
				wcbGruppierung.addItem(dokumentgruppierungDto[i].getCNr());
			}
		}
		boolean bHatStufe0 = DelegateFactory.getInstance().getTheJudgeDelegate()
				.hatRecht(RechteFac.RECHT_DOKUMENTE_SICHERHEITSSTUFE_0_CU);
		boolean bHatStufe1 = DelegateFactory.getInstance().getTheJudgeDelegate()
				.hatRecht(RechteFac.RECHT_DOKUMENTE_SICHERHEITSSTUFE_1_CU);
		boolean bHatStufe2 = DelegateFactory.getInstance().getTheJudgeDelegate()
				.hatRecht(RechteFac.RECHT_DOKUMENTE_SICHERHEITSSTUFE_2_CU);
		boolean bHatStufe3 = DelegateFactory.getInstance().getTheJudgeDelegate()
				.hatRecht(RechteFac.RECHT_DOKUMENTE_SICHERHEITSSTUFE_3_CU);
		if (bHatStufe0) {
			wcbSicherheitsstufe.addItem(JCRDocFac.SECURITY_NONE);
		}
		if (bHatStufe1) {
			wcbSicherheitsstufe.addItem(JCRDocFac.SECURITY_LOW);
		}
		if (bHatStufe2) {
			wcbSicherheitsstufe.addItem(JCRDocFac.SECURITY_MEDIUM);
		}
		if (bHatStufe3) {
			wcbSicherheitsstufe.addItem(JCRDocFac.SECURITY_HIGH);
		}
		wcbSicherheitsstufe.setSelectedIndex(wcbSicherheitsstufe.getItemCount()-1); //Hoechste Stufe ist Standard
		
		wbuSpeichern.addActionListener(this);
		Container content = getContentPane();
		content.setLayout(new GridLayout(5, 2));
		content.add(wlaSchlagworte);
		content.add(wtfSchlagworte);
		content.add(wlaSicherheitsstufe);
		content.add(wcbSicherheitsstufe);
		content.add(wlaBelegart);
		content.add(wcbBelegart);
		content.add(wlaGruppierung);
		content.add(wcbGruppierung);
		content.add(new JLabel());
		content.add(wbuSpeichern);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		components2Dto();
		dispose();
	}
}
