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
package com.lp.client.finanz;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.border.Border;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperRadioButton;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.report.PanelReportIfJRDS;
import com.lp.client.frame.report.PanelReportKriterien;
import com.lp.client.pc.LPMain;
import com.lp.server.finanz.service.FinanzReportFac;
import com.lp.server.system.service.MailtextDto;
import com.lp.server.util.report.JasperPrintLP;

/**
 * <p>
 * Diese Klasse kuemmert sich um den Druck aller Konten
 * </p>
 * 
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2008
 * </p>
 * 
 * <p>
 * Erstellung: Martin Bluehweis; 07.09.05
 * </p>
 * 
 * <p>
 * @author $Author: christian $
 * </p>
 * 
 * @version not attributable Date $Date: 2012/05/29 08:28:26 $
 */
public class ReportKonten extends PanelBasis implements PanelReportIfJRDS {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Border border1;

	private WrapperRadioButton wrbSachkonten = null;
	private WrapperRadioButton wrbDebitoren = null;
	private WrapperRadioButton wrbKreditoren = null;
	private ButtonGroup jbgKontotyp = null;
	private WrapperCheckBox wcbMitVersteckten = new WrapperCheckBox();

	private final static int BREITE_SPALTE2 = 80;

	protected JPanel jpaWorkingOn = new JPanel();

	String kontotypCNr = null;

	public ReportKonten(InternalFrame internalFrame, String kontotypCNr,
			String sAdd2Title) throws Throwable {
		super(internalFrame, sAdd2Title);
		this.kontotypCNr = kontotypCNr;
		jbInit();
		setDefaults();
		initPanel();
		initComponents();
	}

	protected void jbInit() throws Throwable {
		border1 = BorderFactory.createEmptyBorder(10, 10, 10, 10);
		this.setLayout(new GridBagLayout());
		this.add(jpaWorkingOn, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
				GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0,
						0, 0, 0), 0, 0));
		jpaWorkingOn.setLayout(new GridBagLayout());
		jpaWorkingOn.setBorder(border1);

		wcbMitVersteckten.setText(LPMain.getTextRespectUISPr("lp.versteckte"));

		wrbSachkonten = new WrapperRadioButton("Sachkonten");
		wrbDebitoren = new WrapperRadioButton("Debitoren");
		wrbKreditoren = new WrapperRadioButton("Kreditoren");

		wrbSachkonten.setPreferredSize(new Dimension(BREITE_SPALTE2, Defaults
				.getInstance().getControlHeight()));
		wrbDebitoren.setPreferredSize(new Dimension(BREITE_SPALTE2, Defaults
				.getInstance().getControlHeight()));
		wrbKreditoren.setPreferredSize(new Dimension(BREITE_SPALTE2, Defaults
				.getInstance().getControlHeight()));

		jbgKontotyp = new ButtonGroup();
		jbgKontotyp.add(wrbSachkonten);
		jbgKontotyp.add(wrbDebitoren);
		jbgKontotyp.add(wrbKreditoren);

		iZeile++;
		jpaWorkingOn.add(wrbSachkonten, new GridBagConstraints(1, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wrbDebitoren, new GridBagConstraints(2, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wrbKreditoren, new GridBagConstraints(3, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wcbMitVersteckten, new GridBagConstraints(1, iZeile, 2, 1,
				0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));

		// this.setEinschraenkungDatumBelegnummerSichtbar(false);
	}

	private void initPanel() {
		wrbSachkonten.setVisible(true);
		wrbDebitoren.setVisible(true);
		wrbKreditoren.setVisible(true);
	}

	private void setDefaults() {

		wrbSachkonten.setSelected(true);

	}

	public String getModul() {
		return FinanzReportFac.REPORT_MODUL;
	}

	public String getReportname() {
		return FinanzReportFac.REPORT_ALLE_KONTEN;
	}

	public boolean getBErstelleReportSofort() {
		return true;
	}

	public JasperPrintLP getReport(String sDrucktype) throws Throwable {
		if (wrbSachkonten.isSelected()) {
			return DelegateFactory
					.getInstance()
					.getFinanzReportDelegate()
					.printAlleKonten("Sachkonto",
							wcbMitVersteckten.isSelected());
		} else if (wrbDebitoren.isSelected()) {
			return DelegateFactory
					.getInstance()
					.getFinanzReportDelegate()
					.printAlleKonten("Debitorenkonto",
							wcbMitVersteckten.isSelected());
		} else {
			return DelegateFactory
					.getInstance()
					.getFinanzReportDelegate()
					.printAlleKonten("Kreditorenkonto",
							wcbMitVersteckten.isSelected());
		}
	}

	public MailtextDto getMailtextDto() throws Throwable {
		MailtextDto mailtextDto = PanelReportKriterien
				.getDefaultMailtextDto(this);
		return mailtextDto;
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return PanelBasis.NO_VALUE_THATS_OK_JCOMPONENT;
	}
}
