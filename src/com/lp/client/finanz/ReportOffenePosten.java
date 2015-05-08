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
package com.lp.client.finanz;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.EventObject;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.WrapperDateField;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperRadioButton;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.report.PanelReportIfJRDS;
import com.lp.client.frame.report.PanelReportKriterien;
import com.lp.client.pc.LPMain;
import com.lp.server.finanz.service.FinanzReportFac;
import com.lp.server.finanz.service.FinanzServiceFac;
import com.lp.server.finanz.service.KontoDto;
import com.lp.server.system.service.MailtextDto;
import com.lp.server.util.report.JasperPrintLP;

/**
 * <p>
 * Diese Klasse kuemmert sich um den Druck der Saldenliste
 * </p>
 * 
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2008
 * </p>
 * 
 * <p>
 * Erstellung: Martin Bluehweis; 15.06.05
 * </p>
 * 
 * <p>
 * 
 * @author $Author: Gerold $
 *         </p>
 * 
 * @version not attributable Date $Date: 2012/01/10 10:52:28 $
 */
public class ReportOffenePosten extends PanelBasis implements PanelReportIfJRDS {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Border border1;

	private WrapperRadioButton wrbDebitoren = null;
	private WrapperRadioButton wrbKreditoren = null;
	private WrapperRadioButton wrbSelektiertesKonto = null;
	private ButtonGroup jbgKontotyp = null;
	private KontoDto kontoDto = null;
	
	private WrapperLabel wlaSortierung = null;
	private WrapperRadioButton wrbSortKontoNr = null;
	private WrapperRadioButton wrbSortAlphab = null;
	private ButtonGroup bgSort = null;

	private WrapperLabel wlaStichtag = new WrapperLabel();
	private WrapperDateField wdfStichtag = new WrapperDateField();
	private WrapperLabel wlaGeschaeftsjahr = new WrapperLabel();
	
	private InternalFrameFinanz ifF = null;

	private final static int BREITE_SPALTE2 = 150;

	protected JPanel jpaWorkingOn = new JPanel();

	public ReportOffenePosten(InternalFrameFinanz internalFrame,
			KontoDto kontoDto, String sAdd2Title) throws Throwable {
		// reporttitel: das PanelReport kriegt einen Titel, der wird vom
		// Framework hergenommen
		super(internalFrame, sAdd2Title);
		ifF = internalFrame;
		this.kontoDto = kontoDto;
		if (ifF.getTabbedPaneRoot().getSelectedComponent() instanceof TabbedPaneKonten) {
			this.kontoDto = ((TabbedPaneKonten) ifF.getTabbedPaneRoot()
					.getSelectedComponent()).getKontoDto();
		}
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

		if (kontoDto != null) {
			wrbSelektiertesKonto = new WrapperRadioButton("nur Konto "
					+ kontoDto.getCNr());
		} else {
			wrbSelektiertesKonto = new WrapperRadioButton("nur Konto");
		}

		wrbDebitoren = new WrapperRadioButton("Debitoren");
		wrbKreditoren = new WrapperRadioButton("Kreditoren");

		wrbDebitoren.setPreferredSize(new Dimension(BREITE_SPALTE2, Defaults
				.getInstance().getControlHeight()));
		wrbKreditoren.setPreferredSize(new Dimension(BREITE_SPALTE2, Defaults
				.getInstance().getControlHeight()));
		wrbSelektiertesKonto.setPreferredSize(new Dimension(BREITE_SPALTE2,
				Defaults.getInstance().getControlHeight()));
		
		wlaSortierung = new WrapperLabel(LPMain.getTextRespectUISPr("label.sortierung"));
		wlaSortierung.setHorizontalAlignment(SwingConstants.LEFT);
		wrbSortAlphab = new WrapperRadioButton(LPMain.getTextRespectUISPr("label.kunde"));
		wrbSortKontoNr = new WrapperRadioButton(LPMain.getTextRespectUISPr("finanz.kontonummer"));

		jbgKontotyp = new ButtonGroup();
		jbgKontotyp.add(wrbDebitoren);
		jbgKontotyp.add(wrbKreditoren);
		jbgKontotyp.add(wrbSelektiertesKonto);

		bgSort = new ButtonGroup();
		bgSort.add(wrbSortAlphab);
		bgSort.add(wrbSortKontoNr);
		
		wrbDebitoren.addActionListener(this);
		wrbKreditoren.addActionListener(this);
		wrbSelektiertesKonto.addActionListener(this);

		wlaStichtag.setText(LPMain.getTextRespectUISPr("lp.stichtag"));
		wdfStichtag.setMandatoryField(true);

		Integer geschaeftsjahr = ((InternalFrameFinanz) getInternalFrame()).getIAktuellesGeschaeftsjahr() ;
		Timestamp[] dtVonBis = DelegateFactory.getInstance().getBuchenDelegate().getDatumVonBisGeschaeftsjahr(geschaeftsjahr);
		wdfStichtag.setMinimumValue(dtVonBis[0]);
		wdfStichtag.setMaximumValue(dtVonBis[1]);
		Calendar c = Calendar.getInstance();
		if (c.after(dtVonBis[0]) && c.before(dtVonBis[1])) {
			wdfStichtag.setDate(c.getTime());
		} else {
			wdfStichtag.setDate(dtVonBis[1]);
		}
				
		wlaGeschaeftsjahr = new WrapperLabel(LPMain.getTextRespectUISPr("label.geschaeftsjahr")
				+ " " + ((InternalFrameFinanz) getInternalFrame()).getAktuellesGeschaeftsjahr() );
		
		getInternalFrame().addItemChangedListener(this);

		iZeile++;
		jpaWorkingOn.add(wlaGeschaeftsjahr, new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0, 
				GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaStichtag, new GridBagConstraints(2, iZeile, 1, 1, 0.0, 0.0, 
				GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wdfStichtag, new GridBagConstraints(3, iZeile, 1, 1, 0.0, 0.0, 
				GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wrbSelektiertesKonto, new GridBagConstraints(1, iZeile, 1, 1, 0.1, 0.0, 
				GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wrbDebitoren, new GridBagConstraints(2, iZeile, 1, 1, 0.1, 0.0, 
				GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wrbKreditoren, new GridBagConstraints(3, iZeile, 1, 1,	0.1, 0.0, 
				GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wlaSortierung, new GridBagConstraints(1, iZeile, 1, 1, 0.1, 0.0, 
				GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wrbSortKontoNr, new GridBagConstraints(2, iZeile, 1, 1, 0.1, 0.0, 
				GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wrbSortAlphab, new GridBagConstraints(3, iZeile, 1, 1,	0.1, 0.0, 
				GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		
	}

	private void initPanel() throws Throwable {
		wrbSelektiertesKonto.setVisible(true);
		wrbDebitoren.setVisible(true);
		wrbKreditoren.setVisible(true);
		eventActionSpecial(new ActionEvent(wrbSelektiertesKonto, 1, ""));
	}

	private void setDefaults() {
		wrbSelektiertesKonto.setSelected(true);
		wrbSortKontoNr.setSelected(true);
	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		if(e.getSource() == wrbDebitoren
				|| e.getSource() == wrbKreditoren
				|| e.getSource() == wrbSelektiertesKonto) {
			wrbSortAlphab.setText(LPMain.getTextRespectUISPr(wrbDebitoren.isSelected()?"label.kunde":"label.lieferant"));
			wrbSortAlphab.setEnabled(!wrbSelektiertesKonto.isSelected());
			wrbSortKontoNr.setEnabled(!wrbSelektiertesKonto.isSelected());
		}
	}

	public String getModul() {
		return FinanzReportFac.REPORT_MODUL;
	}

	public String getReportname() {
		return FinanzReportFac.REPORT_OFFENEPOSTEN;
	}

	public boolean getBErstelleReportSofort() {
		return false;
	}

	public JasperPrintLP getReport(String sDrucktype) throws Throwable {

		String kontotypCNr = "";

		if (wrbDebitoren.isSelected())
			kontotypCNr = FinanzServiceFac.KONTOTYP_DEBITOR;
		else if (wrbKreditoren.isSelected())
			kontotypCNr = FinanzServiceFac.KONTOTYP_KREDITOR;

		Integer kontoIId = null;
		if (wrbSelektiertesKonto.isSelected()) {
			kontoIId = kontoDto.getIId();
		}
		
//		Integer geschaeftsjahr = new Integer(((InternalFrameFinanz) getInternalFrame()).aktuellesGeschaeftsjahr);
		Integer geschaeftsjahr = ((InternalFrameFinanz) getInternalFrame()).getIAktuellesGeschaeftsjahr() ;
		Timestamp tStichtag = wdfStichtag.getTimestamp();
		return DelegateFactory.getInstance().getFinanzReportDelegate()
				.printOffenPosten(kontotypCNr, geschaeftsjahr, kontoIId, tStichtag, wrbSortAlphab.isSelected());
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
