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

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.EventObject;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.border.Border;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelFilterKriteriumDirekt;
import com.lp.client.frame.component.PanelFilterKriteriumDirektTyped;
import com.lp.client.frame.component.PanelQuery;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperDateField;
import com.lp.client.frame.component.WrapperDateRangeController;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperRadioButton;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.report.PanelReportIfJRDS;
import com.lp.client.frame.report.PanelReportKriterien;
import com.lp.client.pc.LPMain;
import com.lp.server.finanz.service.BuchungsjournalReportParameter;
import com.lp.server.finanz.service.FinanzFac;
import com.lp.server.finanz.service.FinanzReportFac;
import com.lp.server.system.service.MailtextDto;
import com.lp.server.util.report.JasperPrintLP;

/**
 * <p>
 * Diese Klasse kuemmert sich um den Druck des Buchungsjournals
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
 * 
 * @author $Author: christian $
 *         </p>
 * 
 * @version not attributable Date $Date: 2012/12/03 13:08:24 $
 */
public class ReportBuchungsjournal extends PanelBasis implements
		PanelReportIfJRDS {
	/**
	 * 
	 */

	private Border border1;
	private Insets defaultInset ;
	protected JPanel jpaWorkingOn = new JPanel();

	private WrapperLabel wlaVon = new WrapperLabel();
	private WrapperDateField wdfVon = new WrapperDateField();
	private WrapperLabel wlaBis = new WrapperLabel();
	private WrapperDateField wdfBis = new WrapperDateField();

	private WrapperRadioButton wrbBuchungsdatum = new WrapperRadioButton();
	private WrapperRadioButton wrbGebuchtam = new WrapperRadioButton();
	private ButtonGroup buttonGroupSortierung = new ButtonGroup();

	private WrapperDateRangeController wdrBereich = null;

	PanelQuery panelQuery = null;

	private WrapperLabel wlaStorniert = null;
	private WrapperCheckBox wcbStorniert = null;

	private static final long serialVersionUID = 1L;
	private Integer buchungsjournalIId = null;

	private PanelFilterKriteriumDirektTyped panelFKBelegnummer ;
	private PanelFilterKriteriumDirektTyped panelFKTextsuche ;
	private PanelFilterKriteriumDirektTyped panelFKBetrag ;
	private PanelFilterKriteriumDirektTyped panelFKKonto ;
	private PanelFilterKriteriumDirektTyped panelFKBuchungsart ;
	private PanelFilterKriteriumDirektTyped panelFKBelegart ;
	
	private DirektFilterHandler filterHandler ;
	
	public ReportBuchungsjournal(InternalFrame internalFrame,
			Integer buchungsjournalIId, PanelQuery panelQuery, String sAdd2Title)
			throws Throwable {
		super(internalFrame, sAdd2Title);
		this.panelQuery = panelQuery;
		this.buchungsjournalIId = buchungsjournalIId;

		filterHandler = new DirektFilterHandler() ;

		jbInit();
		setDefaults();
		initComponents();
	}

	protected void jbInit() throws Throwable {
		border1 = BorderFactory.createEmptyBorder(10, 10, 10, 10);
		defaultInset = new Insets(2, 2, 2, 2);

		this.setLayout(new GridBagLayout());
		this.add(jpaWorkingOn, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
				GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0,
						0, 0, 0), 0, 0));
		jpaWorkingOn.setLayout(new GridBagLayout());
		jpaWorkingOn.setBorder(border1);

		wlaStorniert = new WrapperLabel(
				LPMain.getTextRespectUISPr("label.storniert"));
		wcbStorniert = new WrapperCheckBox();
		wcbStorniert.setSelected(false);

		wlaVon.setText(LPMain.getTextRespectUISPr("lp.von"));
		wlaBis.setText(LPMain.getTextRespectUISPr("lp.bis"));

		wrbBuchungsdatum
				.setText(LPMain
						.getTextRespectUISPr("fb.buchungsjournal.datumsfilter.buchungsdatum"));
		wrbGebuchtam
				.setText(LPMain
						.getTextRespectUISPr("fb.buchungsjournal.datumsfilter.gebuchtam"));

		panelFKBelegnummer = new PanelFilterKriteriumDirektTyped( 
				FinanzFilterFactory.getInstance().createFKDBelegnummer()) ;
		panelFKTextsuche = new PanelFilterKriteriumDirektTyped( 
				FinanzFilterFactory.getInstance().createFKDBuchungsjournalDetailliertText()) ; 
		panelFKBetrag = new PanelFilterKriteriumDirektTyped( 
				FinanzFilterFactory.getInstance().createFKDBetragssuche()) ;	
		panelFKKonto = new PanelFilterKriteriumDirektTyped( 
				FinanzFilterFactory.getInstance().createFKDKontonummer()) ;
		panelFKBuchungsart = new PanelFilterKriteriumDirektTyped( 
				FinanzFilterFactory.getInstance().createFKDBuchungsart()) ;
		panelFKBelegart = new PanelFilterKriteriumDirektTyped(FinanzFilterFactory.getInstance().createFKDBelegart()) ;
		
		// Mit Filtern vorbesetzen
		if (panelQuery != null) {
			Iterator<Integer> it = panelQuery.getHmDirektFilter().keySet()
					.iterator();
			while (it.hasNext()) {
				PanelFilterKriteriumDirekt panelFkd = (PanelFilterKriteriumDirekt) panelQuery
						.getHmDirektFilter().get(it.next());
				if (panelFkd.fkd.kritName.equals("c_belegnummer")) {
					panelFKBelegnummer = new PanelFilterKriteriumDirektTyped(panelFkd) ; 
					continue ;
				}
				if(panelFkd.fkd.kritName.equals("flrbuchung.c_text")) {
					panelFKTextsuche = new PanelFilterKriteriumDirektTyped(panelFkd) ;
					continue ;
				}
				if(panelFkd.fkd.kritName.equals("n_betrag")) {
					panelFKBetrag = new PanelFilterKriteriumDirektTyped(panelFkd) ;
					continue ;
				}
				if(panelFkd.fkd.kritName.equals("flrkonto.c_nr")) {
					panelFKKonto = new PanelFilterKriteriumDirektTyped(panelFkd) ;
					continue ;
				}
				if(panelFkd.fkd.kritName.equals(FinanzFac.FLR_BUCHUNGDETAIL_BUCHUNGART)) {
					panelFKBuchungsart = new PanelFilterKriteriumDirektTyped(panelFkd) ;
					continue ;
				}
				if(panelFkd.fkd.kritName.equals(FinanzFac.FLR_BUCHUNGDETAIL_BELEGART)) {
					panelFKBelegart = new PanelFilterKriteriumDirektTyped(panelFkd) ;
					continue ;
				}
			}
		}

		wdfVon.setMandatoryField(true);
		wdfBis.setMandatoryField(true);

		Calendar c = Calendar.getInstance();
		c.set(Calendar.MONTH, Calendar.JANUARY);
		c.set(Calendar.DAY_OF_MONTH, 1);
		wdfVon.setDate(c.getTime());
		c.set(Calendar.MONTH, Calendar.DECEMBER);
		c.set(Calendar.DAY_OF_MONTH, 31);
		wdfBis.setDate(c.getTime());

		buttonGroupSortierung.add(wrbBuchungsdatum);
		buttonGroupSortierung.add(wrbGebuchtam);
		wrbGebuchtam.setSelected(true);

		wdrBereich = new WrapperDateRangeController(wdfVon, wdfBis);

		getInternalFrame().addItemChangedListener(this);
		
		iZeile++;
		jpaWorkingOn.add(wlaVon, new GridBagConstraints(0, iZeile, 1, 1, 0.1,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				defaultInset, 0, 0));
		jpaWorkingOn.add(wdfVon, new GridBagConstraints(1, iZeile, 1, 1, 0.1,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				defaultInset, 0, 0));
		jpaWorkingOn.add(wlaBis, new GridBagConstraints(2, iZeile, 1, 1, 0.1,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				defaultInset, 0, 0));
		jpaWorkingOn.add(wdfBis, new GridBagConstraints(3, iZeile, 1, 1, 0.1,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				defaultInset, 0, 0));
		jpaWorkingOn.add(wdrBereich, new GridBagConstraints(4, iZeile, 1, 1,
				0.1, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				defaultInset, 0, 0));
		iZeile++;

		jpaWorkingOn
				.add(new WrapperLabel(LPMain
						.getTextRespectUISPr("fb.buchungsjournal.datumsfilter")),
						new GridBagConstraints(0, iZeile, 1, 1, 0.1, 0.0,
								GridBagConstraints.CENTER,
								GridBagConstraints.BOTH,
								defaultInset, 0, 0));
		jpaWorkingOn.add(wrbGebuchtam, new GridBagConstraints(1, iZeile, 1, 1,
				0.1, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				defaultInset, 0, 0));
		jpaWorkingOn.add(wrbBuchungsdatum, new GridBagConstraints(2, iZeile, 1,
				1, 0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, defaultInset, 0, 0));

		jpaWorkingOn.add(wlaStorniert, new GridBagConstraints(3, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
				defaultInset, 60, 0));
		jpaWorkingOn.add(wcbStorniert, new GridBagConstraints(4, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
				defaultInset, 50, 0));

		++iZeile ;		
		addPanelFilterKriteriumDirekt(jpaWorkingOn, iZeile, 0, panelFKBelegnummer);
		addPanelFilterKriteriumDirekt(jpaWorkingOn, iZeile, 2, panelFKTextsuche);

		++iZeile ;
		addPanelFilterKriteriumDirekt(jpaWorkingOn, iZeile, 0, panelFKBetrag);
		addPanelFilterKriteriumDirekt(jpaWorkingOn, iZeile, 2, panelFKKonto);
		
		++iZeile ;
		addPanelFilterKriteriumDirekt(jpaWorkingOn, iZeile, 0, panelFKBuchungsart);
		addPanelFilterKriteriumDirekt(jpaWorkingOn, iZeile, 2, panelFKBelegart);		
	}

	private void addPanelFilterKriteriumDirekt(JPanel p, int row, int startCol, PanelFilterKriteriumDirektTyped panelFkd) {
		p.add(panelFkd.getLabel(), 
				new GridBagConstraints(startCol++, row, 1, 1,
						0.1, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, defaultInset, 0, 0)) ;
		p.add(panelFkd.getWrapperField(), 
				new GridBagConstraints(startCol++, row, 1, 1, 
						0.1, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, defaultInset, 0, 0)) ;
		
		filterHandler.register(panelFkd);
	}
	

	private void setDefaults() throws ExceptionLP, Throwable {
		try {
			Integer geschaeftsjahr = ((InternalFrameFinanz) getInternalFrame())
					.getIAktuellesGeschaeftsjahr();
			Timestamp[] d = DelegateFactory.getInstance().getBuchenDelegate()
					.getDatumVonBisGeschaeftsjahr(geschaeftsjahr);
			wdfVon.setTimestamp(d[0]);
			wdfBis.setTimestamp(d[1]);
		} catch (Throwable e) {
			//
		}
	}

	public String getModul() {
		return FinanzReportFac.REPORT_MODUL;
	}

	public String getReportname() {
		return FinanzReportFac.REPORT_BUCHUNGSJOURNAL;
	}

	public boolean getBErstelleReportSofort() {
		return false;
	}

	public JasperPrintLP getReport(String sDrucktype) throws Throwable {
//		return DelegateFactory
//				.getInstance()
//				.getFinanzReportDelegate()
//				.printBuchungsjournal(buchungsjournalIId, wdfVon.getDate(),
//						wdfBis.getDate(), wcbStorniert.isSelected(),
//						wrbBuchungsdatum.isSelected(), wtfText.getText(),
//						wtfBeleg.getText(), wnfBetrag.getBigDecimal(), wtfKontonummer.getText());
		
		BuchungsjournalReportParameter params = new BuchungsjournalReportParameter(buchungsjournalIId) ;
		params.setdVon(wdfVon.getDate());
		params.setdBis(wdfBis.getDate());
		params.setStorniert(wcbStorniert.isSelected());
		params.setbDatumsfilterIstBuchungsdatum(wrbBuchungsdatum.isSelected());
		params.setBuchungsText(panelFKTextsuche.asTextField().getText());
		params.setBelegnummer(panelFKBelegnummer.asTextField().getText());
		params.setBetrag(panelFKBetrag.asNumberField().getBigDecimal());
		params.setKontonummer(panelFKKonto.asTextField().getText());
		params.setBuchungsart(panelFKBuchungsart.asTextField().getText());
		params.setBelegart(panelFKBelegart.asTextField().getText()) ;
		return DelegateFactory.getInstance()
				.getFinanzReportDelegate().printBuchungsjournal(params) ;
//				.printBuchungsjournal(buchungsjournalIId, wdfVon.getDate(),
//						wdfBis.getDate(), wcbStorniert.isSelected(),
//						wrbBuchungsdatum.isSelected(), 
//						panelFKTextsuche.asTextField().getText(), 
//						panelFKBelegnummer.asTextField().getText(), 
//						panelFKBetrag.asNumberField().getBigDecimal(), 
//						panelFKKonto.asTextField().getText());
	}

	public MailtextDto getMailtextDto() throws Throwable {
		MailtextDto mailtextDto = PanelReportKriterien
				.getDefaultMailtextDto(this);
		return mailtextDto;
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return PanelBasis.NO_VALUE_THATS_OK_JCOMPONENT;
	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		// ItemChangedEvent e = (ItemChangedEvent) eI;
		// int id = e.getID() ;
	}

}
