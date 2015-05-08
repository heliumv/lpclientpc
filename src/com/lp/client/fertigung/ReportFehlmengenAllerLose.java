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
package com.lp.client.fertigung;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JComponent;
import javax.swing.JPanel;

import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.report.PanelReportIfJRDS;
import com.lp.client.frame.report.PanelReportKriterien;
import com.lp.client.pc.LPMain;
import com.lp.server.fertigung.service.FertigungReportFac;
import com.lp.server.system.service.MailtextDto;
import com.lp.server.util.report.JasperPrintLP;

@SuppressWarnings("static-access")
/*
 * <p> Diese Klasse kuemmert sich um den Druck der Aufloesbaren Fehlmengen
 * Liste</p>
 * 
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 * 
 * <p>Erstellung: Martin Bluehweis; 19.01.06</p>
 * 
 * <p>@author $Author: christian $</p>
 * 
 * @version not attributable Date $Date: 2010/02/15 14:00:14 $
 */
public class ReportFehlmengenAllerLose extends PanelBasis implements
		PanelReportIfJRDS {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel jpaWorkingOn = null;

	private WrapperCheckBox wcbAlleAusserEigengefertigteArtikel = null;
	private WrapperCheckBox wcbNurEigengefertigtenStuecklisten = null;

	public ReportFehlmengenAllerLose(InternalFrame internalFrame,
			String sAdd2Title) throws Throwable {
		super(internalFrame, sAdd2Title);
		jbInit();
		setDefaults();
		initComponents();
	}

	private void jbInit() {
		jpaWorkingOn = new JPanel(new GridBagLayout());
		wcbNurEigengefertigtenStuecklisten = new WrapperCheckBox();
		wcbNurEigengefertigtenStuecklisten.setText(LPMain.getInstance()
				.getTextRespectUISPr("fert.report.fehlmengenallerlose.nureigengefertigte"));
		wcbAlleAusserEigengefertigteArtikel = new WrapperCheckBox();
		wcbAlleAusserEigengefertigteArtikel.setText(LPMain.getInstance()
				.getTextRespectUISPr("fert.report.fehlmengenallerlose.alleaussereigengefertigte"));

		wcbAlleAusserEigengefertigteArtikel.setSelected(true);
		
		this.add(jpaWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						0, 0, 0, 0), 0, 0));
	
		iZeile++;

		jpaWorkingOn.add(wcbAlleAusserEigengefertigteArtikel,
				new GridBagConstraints(1, iZeile, 1, 1, 1.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(2, 2, 2, 2), 350, 0));
		iZeile++;

		jpaWorkingOn.add(wcbNurEigengefertigtenStuecklisten,
				new GridBagConstraints(1, iZeile, 1, 1, 1.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(2, 2, 2, 2), 350, 0));
	}

	private void setDefaults() {
		// Default Sortierung nach Ident

	}

	public String getModul() {
		return FertigungReportFac.REPORT_MODUL;
	}

	public String getReportname() {
		return FertigungReportFac.REPORT_FEHLMENGEN_ALLER_LOSE;
	}

	public boolean getBErstelleReportSofort() {
		return false;
	}

	public JasperPrintLP getReport(String sDrucktype) throws Throwable {
		return DelegateFactory.getInstance().getFertigungDelegate()
				.printFehlmengenAllerLose(
						wcbNurEigengefertigtenStuecklisten.isSelected(),wcbAlleAusserEigengefertigteArtikel.isSelected());
	}

	public MailtextDto getMailtextDto() throws Throwable {
		MailtextDto mailtextDto = PanelReportKriterien
				.getDefaultMailtextDto(this);
		return mailtextDto;
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return null;
	}
}
