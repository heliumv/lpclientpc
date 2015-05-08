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
package com.lp.client.finanz;

import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.WrapperComboBox;
import com.lp.client.frame.component.WrapperComboBoxPeriode;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.report.PanelReportIfJRDS;
import com.lp.client.frame.report.PanelReportKriterien;
import com.lp.client.pc.LPMain;
import com.lp.server.finanz.service.FinanzFac;
import com.lp.server.finanz.service.FinanzReportFac;
import com.lp.server.finanz.service.FinanzamtDto;
import com.lp.server.finanz.service.ReportUvaKriterienDto;
import com.lp.server.system.service.MailtextDto;
import com.lp.server.util.report.JasperPrintLP;

public class ReportUstVerprobung extends PanelBasis implements
		PanelReportIfJRDS {
	
	private static final long serialVersionUID = -6930117069386086423L;
	
	private String geschaeftsjahr;
	private FinanzamtDto[] faDtos;
	
	private WrapperTextField wtfGeschaeftsjahr;
	private WrapperComboBox wcoFinanzamt;
	private WrapperComboBoxPeriode wcoPeriode;
	
	public ReportUstVerprobung(InternalFrame internalFrame, String sAdd2Title, String geschaeftsjahr) throws Throwable {
	    // reporttitel: das PanelReport kriegt einen Titel, der wird vom Framework hergenommen
	    super(internalFrame, sAdd2Title);
	    this.geschaeftsjahr = geschaeftsjahr;
	    jbInit();
	}
	
	private void jbInit() throws Throwable {
		this.setLayout(new MigLayout("wrap 1", "[fill]", "[fill]"));
		JPanel p = new JPanel(new MigLayout("wrap 4", "[25%,fill|25%,fill|25%,fill|25%,fill]"));
		this.add(p, "pushx");
		wtfGeschaeftsjahr = new WrapperTextField();
		wtfGeschaeftsjahr.setActivatable(false);
		wtfGeschaeftsjahr.setText(geschaeftsjahr);
		wcoFinanzamt = new WrapperComboBox();
		wcoPeriode = new WrapperComboBoxPeriode(geschaeftsjahr);
		faDtos = DelegateFactory.getInstance().getFinanzDelegate().finanzamtFindAllByMandantCNr(LPMain.getTheClient());
		if (faDtos != null) {
			for (int i=0; i<faDtos.length; i++) {
				wcoFinanzamt.addItem(faDtos[i].getPartnerDto().formatName());
			}
		}
		
		p.add(new WrapperLabel(LPMain.getTextRespectUISPr("label.finanzamt")));
		p.add(wcoFinanzamt);
		
		p.add(new WrapperLabel(LPMain.getTextRespectUISPr("label.geschaeftsjahr")));
		p.add(wtfGeschaeftsjahr, "wrap");
		
		p.add(new WrapperLabel(LPMain.getTextRespectUISPr("label.periode")), "skip 2");
		p.add(wcoPeriode, "wrap");
	}

	@Override
	public String getModul() {
	    return FinanzReportFac.REPORT_MODUL;
	}

	@Override
	public String getReportname() {
	    return FinanzReportFac.REPORT_USTVERPROBUNG;
	}

	@Override
	public boolean getBErstelleReportSofort() {
		return false;
	}

	@Override
	public MailtextDto getMailtextDto() throws Throwable {
		return PanelReportKriterien.getDefaultMailtextDto(this);
	}

	@Override
	public JasperPrintLP getReport(String sDrucktype) throws Throwable {
		ReportUvaKriterienDto krit = new ReportUvaKriterienDto();
	    krit.setFinanzamtIId(faDtos[wcoFinanzamt.getSelectedIndex()].getPartnerIId());
	    krit.setIGeschaeftsjahr(Integer.parseInt(geschaeftsjahr));
	    krit.setIPeriode(wcoPeriode.getPeriode());
	    krit.setSPeriode(wcoPeriode.getSelectedItem().toString());
	    krit.setSAbrechnungszeitraum(FinanzFac.UVA_ABRECHNUNGSZEITRAUM_MONAT);
		return DelegateFactory.getInstance().getFinanzReportDelegate().printUstVerprobung(krit);
	}

}
