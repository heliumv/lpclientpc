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
import java.util.EventObject;

import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperDateField;
import com.lp.client.frame.component.WrapperDateRangeController;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperRadioButton;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.report.PanelReportIfJRDS;
import com.lp.client.frame.report.PanelReportKriterien;
import com.lp.client.pc.LPMain;
import com.lp.client.util.fastlanereader.gui.QueryType;
import com.lp.server.finanz.service.FinanzReportFac;
import com.lp.server.finanz.service.FinanzServiceFac;
import com.lp.server.finanz.service.KontoDto;
import com.lp.server.finanz.service.PrintKontoblaetterModel;
import com.lp.server.finanz.service.ReportSaldenlisteKriterienDto;
import com.lp.server.system.service.MailtextDto;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.FilterKriteriumDirekt;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
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
 * @author $Author: robert $
 *         </p>
 * 
 * @version not attributable Date $Date: 2012/10/19 13:19:04 $
 */
public class ReportKontoblaetter extends PanelBasis implements
		PanelReportIfJRDS {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private WrapperRadioButton wrbSachkonten = null;
	private WrapperRadioButton wrbDebitoren = null;
	private WrapperRadioButton wrbKreditoren = null;
	private WrapperRadioButton wrbSelektiertesKonto = null;
	private ButtonGroup jbgKontotyp = null;
	private KontoDto kontoDto = null;

	private ButtonGroup buttonGroupSortierung = new ButtonGroup();

	private WrapperLabel wlaSortierung = new WrapperLabel();
	private WrapperRadioButton wrbSortDatum = new WrapperRadioButton();
	private WrapperRadioButton wrbSortAuszugsnummer = new WrapperRadioButton();

	private WrapperRadioButton wrbSortBeleg = new WrapperRadioButton();

	private WrapperButton wlaKtonrVon = new WrapperButton();
	private WrapperButton wlaKtonrBis = new WrapperButton();

	static final public String ACTION_SPECIAL_KONTOVON_FROM_LISTE = "ACTION_SPECIAL_KONTOVON_FROM_LISTE";
	static final public String ACTION_SPECIAL_KONTOBIS_FROM_LISTE = "ACTION_SPECIAL_KONTOBIS_FROM_LISTE";
	private PanelQueryFLR panelQueryFLRKonto_Von = null;
	private PanelQueryFLR panelQueryFLRKonto_Bis = null;

	private Integer kontoIId_Von = null;
	private Integer kontoIId_Bis = null;

	private WrapperTextField wtfKtonrVon = new WrapperTextField();
	private WrapperTextField wtfKtonrBis = new WrapperTextField();

	private WrapperLabel wlaGeschaeftsjahr = new WrapperLabel() ;
	
	private WrapperLabel wlaVon = new WrapperLabel();
	private WrapperDateField wdfVon = new WrapperDateField();
	private WrapperLabel wlaBis = new WrapperLabel();
	private WrapperDateField wdfBis = new WrapperDateField();
	private WrapperDateRangeController wdrBereich = null;

//	private WrapperComboBox wcbSortOrder = new WrapperComboBox() ;
	private WrapperCheckBox wcbSortiereNachAZK = new WrapperCheckBox() ;
	private WrapperCheckBox wchkEnableSaldoVorperiode = new WrapperCheckBox() ;
	
	private WrapperCheckBox wcbPrintInMandantenWaehrung = new WrapperCheckBox();
	
	private InternalFrameFinanz ifF = null;

	private final static int BREITE_SPALTE2 = 150;

	protected JPanel jpaWorkingOn;

	public ReportKontoblaetter(InternalFrameFinanz internalFrame,
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

		// this.setVisible(false);
		invalidate() ;
	}

	
	protected KontoDto getKontoDto() {
		return kontoDto ;
	}

	protected WrapperDateField getWdfVon() {
		return wdfVon ;
	}
	
	protected WrapperDateField getWdfBis() {
		return wdfBis ;
	}
	
	protected void jbInit() throws Throwable {
		
		jpaWorkingOn = new JPanel(new MigLayout("wrap 4", "[25%,fill|25%,fill|25%,fill|25%,fill]"));
		this.setLayout(new GridBagLayout());
		this.add(jpaWorkingOn, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
				GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0,
						0, 0, 0), 0, 0));

		if (kontoDto != null) {
			wrbSelektiertesKonto = new WrapperRadioButton("nur Konto "
					+ kontoDto.getCNr());
		} else {
			wrbSelektiertesKonto = new WrapperRadioButton("nur Konto");
		}

		wlaSortierung.setText(LPMain.getTextRespectUISPr("lp.sortierung"));
		wrbSortDatum.setText(LPMain.getTextRespectUISPr("lp.datum"));
		wrbSortAuszugsnummer
				.setText(LPMain.getTextRespectUISPr("label.auszug"));
		wrbSortBeleg.setText(LPMain.getTextRespectUISPr("lp.belegsortieren"));

//		wcbSortOrder.addItem(wrbSortDatum.getText()) ;
//		wcbSortOrder.addItem(wrbSortAuszugsnummer.getText()) ;
//		wcbSortOrder.addItem(wrbSortBeleg.getText()) ;
		
		wrbSachkonten = new WrapperRadioButton("Sachkonten");
		wrbDebitoren = new WrapperRadioButton("Debitoren");
		wrbKreditoren = new WrapperRadioButton("Kreditoren");

		wchkEnableSaldoVorperiode.setText(LPMain.getTextRespectUISPr("lp.mitVorperiodenSaldo")) ;
		wcbSortiereNachAZK.setText(LPMain.getTextRespectUISPr("fb.report.sortierenach.azk"));
		
		wcbPrintInMandantenWaehrung.setText(LPMain.getTextRespectUISPr("finanz.label.druckinmandantenwaehrung"));
		
		wrbSachkonten.setPreferredSize(new Dimension(BREITE_SPALTE2, Defaults
				.getInstance().getControlHeight()));
		wrbDebitoren.setPreferredSize(new Dimension(BREITE_SPALTE2, Defaults
				.getInstance().getControlHeight()));
		wrbKreditoren.setPreferredSize(new Dimension(BREITE_SPALTE2, Defaults
				.getInstance().getControlHeight()));
		wrbSelektiertesKonto.setPreferredSize(new Dimension(BREITE_SPALTE2,
				Defaults.getInstance().getControlHeight()));

		jbgKontotyp = new ButtonGroup();
		jbgKontotyp.add(wrbSachkonten);
		jbgKontotyp.add(wrbDebitoren);
		jbgKontotyp.add(wrbKreditoren);
		jbgKontotyp.add(wrbSelektiertesKonto);

		wrbSachkonten.addActionListener(this);
		wrbDebitoren.addActionListener(this);
		wrbKreditoren.addActionListener(this);
		wrbSelektiertesKonto.addActionListener(this);

		buttonGroupSortierung.add(wrbSortAuszugsnummer);
		buttonGroupSortierung.add(wrbSortDatum);
		buttonGroupSortierung.add(wrbSortBeleg);
		wrbSortDatum.setSelected(true);

		wlaGeschaeftsjahr = new WrapperLabel(LPMain.getTextRespectUISPr("label.geschaeftsjahr")
				+ " " + ((InternalFrameFinanz) getInternalFrame()).getAktuellesGeschaeftsjahr());
				
		wlaVon.setText(LPMain.getTextRespectUISPr("lp.von"));
		wlaBis.setText(LPMain.getTextRespectUISPr("lp.bis"));
		wlaKtonrVon.setText(LPMain.getTextRespectUISPr("fb.kontoblaetter.von"));

		wlaKtonrVon.setActionCommand(ACTION_SPECIAL_KONTOVON_FROM_LISTE);
		wlaKtonrVon.addActionListener(this);

		wlaKtonrBis.setText(LPMain.getTextRespectUISPr("lp.bis"));
		wlaKtonrBis.setActionCommand(ACTION_SPECIAL_KONTOBIS_FROM_LISTE);
		wlaKtonrBis.addActionListener(this);

		wdfVon.setMandatoryField(true);
		wdfBis.setMandatoryField(true);

		try {
			setupVonBisDatum() ;
			setupCheckBoxMitVorperiodenSaldo() ;
		} catch (Throwable e) {
			//
		}

		wdrBereich = new WrapperDateRangeController(wdfVon, wdfBis);

		getInternalFrame().addItemChangedListener(this);

		jpaWorkingOn.add(wrbSelektiertesKonto);
		jpaWorkingOn.add(wrbSachkonten);
		jpaWorkingOn.add(wrbDebitoren);
		jpaWorkingOn.add(wrbKreditoren);
		
		jpaWorkingOn.add(wlaKtonrVon);
		jpaWorkingOn.add(wtfKtonrVon);
		jpaWorkingOn.add(wlaKtonrBis);
		jpaWorkingOn.add(wtfKtonrBis);

		jpaWorkingOn.add(wlaGeschaeftsjahr);
		jpaWorkingOn.add(wlaVon);
		jpaWorkingOn.add(wdfVon, "split 2, w pref:pref:max, grow 0");
		jpaWorkingOn.add(wlaBis);
		jpaWorkingOn.add(wdfBis, "split, w pref::, grow 0");
		jpaWorkingOn.add(wdrBereich, "growy, wrap");
		
		jpaWorkingOn.add(wlaSortierung);
		jpaWorkingOn.add(wrbSortDatum);
		jpaWorkingOn.add(wcbSortiereNachAZK, "span");
		
		jpaWorkingOn.add(wrbSortAuszugsnummer, "skip");
		jpaWorkingOn.add(wchkEnableSaldoVorperiode, "span");
		
		jpaWorkingOn.add(wrbSortBeleg, "skip");
		jpaWorkingOn.add(wcbPrintInMandantenWaehrung, "span");
	}


	private void setupCheckBoxMitVorperiodenSaldo() {
		boolean withSaldo = false ;

		if (wrbSelektiertesKonto.isSelected()) {
			Integer kontoIId = kontoDto.getIId();
			
			try {
				withSaldo = DelegateFactory.getInstance()
						.getFinanzDelegate().isKontoMitSaldo(kontoIId) ;			
			} catch(Throwable t) {				
			}
		}
		wchkEnableSaldoVorperiode.setSelected(withSaldo) ;
	}


	protected void setupVonBisDatum() throws ExceptionLP, Throwable {
		Timestamp[] d = getGJTimestamps();
		if(null == d) return ;
		
		wdfVon.setTimestamp(d[0]);
		wdfVon.setMinimumValue(d[0]) ;
		wdfVon.setMaximumValue(d[1]) ;

		wdfBis.setTimestamp(d[1]);
		wdfBis.setMinimumValue(d[0]) ;
		wdfBis.setMaximumValue(d[1]) ;
	}


	protected Timestamp[] getGJTimestamps() throws Throwable, ExceptionLP {
		Integer geschaeftsjahr = ((InternalFrameFinanz) getInternalFrame()).getIAktuellesGeschaeftsjahr();
		Timestamp[] d = DelegateFactory.getInstance().getBuchenDelegate().getDatumVonBisGeschaeftsjahr(geschaeftsjahr);
		return d;
	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;
		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {

			if (e.getSource() == panelQueryFLRKonto_Von) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				KontoDto kontoDto = DelegateFactory.getInstance()
						.getFinanzDelegate()
						.kontoFindByPrimaryKey((Integer) key);
				kontoIId_Von = kontoDto.getIId();
				wtfKtonrVon.setText(kontoDto.getCNr());
			} else if (e.getSource() == panelQueryFLRKonto_Bis) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				KontoDto kontoDto = DelegateFactory.getInstance()
						.getFinanzDelegate()
						.kontoFindByPrimaryKey((Integer) key);
				kontoIId_Bis = kontoDto.getIId();
				wtfKtonrBis.setText(kontoDto.getCNr());
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_LEEREN) {
			if (e.getSource() == panelQueryFLRKonto_Von) {
				wtfKtonrVon.setText(null);
				kontoIId_Von = null;
			} else if (e.getSource() == panelQueryFLRKonto_Bis) {
				wtfKtonrBis.setText(null);
				kontoIId_Bis = null;
			}
		}
	}

	void dialogQueryKontoFromListe_Von(ActionEvent e) throws Throwable {

		String title = null;
		FilterKriterium[] filters = null;
		if (wrbSachkonten.isSelected()) {
			filters = FinanzFilterFactory.getInstance().createFKSachkontenInklMitlaufende();
			title = LPMain.getTextRespectUISPr("finanz.liste.sachkonten");
		}

		else if (wrbDebitoren.isSelected()) {
			filters = FinanzFilterFactory.getInstance()
					.createFKDebitorenkonten();
			title = LPMain
					.getTextRespectUISPr("finanz.tab.unten.debitorenkonten.title");
		}

		else if (wrbKreditoren.isSelected()) {
			filters = FinanzFilterFactory.getInstance()
					.createFKKreditorenkonten();
			title = LPMain
					.getTextRespectUISPr("finanz.tab.unten.kreditorenkonten.title");
		}

		QueryType[] qt = null;
		// nur Sachkonten dieses Mandanten
		panelQueryFLRKonto_Von = new PanelQueryFLR(qt, filters,
				QueryParameters.UC_ID_FINANZKONTEN,
				new String[] { PanelBasis.ACTION_LEEREN }, getInternalFrame(),
				title);
		FilterKriteriumDirekt fkDirekt1 = FinanzFilterFactory.getInstance()
				.createFKDKontonummer();
		FilterKriteriumDirekt fkDirekt2 = FinanzFilterFactory.getInstance()
				.createFKDKontobezeichnung();
		FilterKriterium fkVersteckt = FinanzFilterFactory.getInstance()
				.createFKVKonto();
		panelQueryFLRKonto_Von.befuellePanelFilterkriterienDirektUndVersteckte(
				fkDirekt1, fkDirekt2, fkVersteckt);
		if (kontoIId_Von != null) {
			panelQueryFLRKonto_Von.setSelectedId(kontoIId_Von);
		}

		new DialogQuery(panelQueryFLRKonto_Von);
	}

	void dialogQueryKontoFromListe_Bis(ActionEvent e) throws Throwable {

		String title = null;
		FilterKriterium[] filters = null;
		if (wrbSachkonten.isSelected()) {
			filters = FinanzFilterFactory.getInstance().createFKSachkontenInklMitlaufende();
			title = LPMain.getTextRespectUISPr("finanz.liste.sachkonten");
		}

		else if (wrbDebitoren.isSelected()) {
			filters = FinanzFilterFactory.getInstance()
					.createFKDebitorenkonten();
			title = LPMain
					.getTextRespectUISPr("finanz.tab.unten.debitorenkonten.title");
		}

		else if (wrbKreditoren.isSelected()) {
			filters = FinanzFilterFactory.getInstance()
					.createFKKreditorenkonten();
			title = LPMain
					.getTextRespectUISPr("finanz.tab.unten.kreditorenkonten.title");
		}

		QueryType[] qt = null;
		// nur Sachkonten dieses Mandanten
		panelQueryFLRKonto_Bis = new PanelQueryFLR(qt, filters,
				QueryParameters.UC_ID_FINANZKONTEN,
				new String[] { PanelBasis.ACTION_LEEREN }, getInternalFrame(),
				title);
		FilterKriteriumDirekt fkDirekt1 = FinanzFilterFactory.getInstance()
				.createFKDKontonummer();
		FilterKriteriumDirekt fkDirekt2 = FinanzFilterFactory.getInstance()
				.createFKDKontobezeichnung();
		FilterKriterium fkVersteckt = FinanzFilterFactory.getInstance()
				.createFKVKonto();
		panelQueryFLRKonto_Bis.befuellePanelFilterkriterienDirektUndVersteckte(
				fkDirekt1, fkDirekt2, fkVersteckt);
		if (kontoIId_Bis != null) {
			panelQueryFLRKonto_Bis.setSelectedId(kontoIId_Bis);
		}

		new DialogQuery(panelQueryFLRKonto_Bis);
	}

	private void initPanel() throws Throwable {
		wrbSelektiertesKonto.setVisible(true);
		wrbSachkonten.setVisible(true);
		wrbDebitoren.setVisible(true);
		wrbKreditoren.setVisible(true);
		eventActionSpecial(new ActionEvent(wrbSelektiertesKonto, 1, ""));
	}

	private void setDefaults() {
		wrbSelektiertesKonto.setSelected(true);

		if(kontoDto.getCsortierung() != null) {
			if(kontoDto.getCsortierung().equals("Beleg")) {
				wrbSortBeleg.setSelected(true);
			} else if(kontoDto.getCsortierung().equals("Datum")) {
				wrbSortDatum.setSelected(true);
			} else if(kontoDto.getCsortierung().equals("Auszug")) {
				wrbSortAuszugsnummer.setSelected(true);
			}
		} else {
			wrbSortDatum.setSelected(true);
		}		
	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		if (e.getActionCommand().equals(ACTION_SPECIAL_KONTOVON_FROM_LISTE)) {
			dialogQueryKontoFromListe_Von(e);
		} else if (e.getActionCommand().equals(
				ACTION_SPECIAL_KONTOBIS_FROM_LISTE)) {
			dialogQueryKontoFromListe_Bis(e);
		} else if (e.getSource().equals(wrbSelektiertesKonto)) {
			wtfKtonrVon.setText(null);
			wtfKtonrBis.setText(null);
			wtfKtonrVon.setVisible(false);
			wtfKtonrBis.setVisible(false);
			wlaKtonrVon.setVisible(false);
			wlaKtonrBis.setVisible(false);
			
			setupCheckBoxMitVorperiodenSaldo() ;
		} else if (e.getSource().equals(wrbSachkonten)
				|| e.getSource().equals(wrbDebitoren)
				|| e.getSource().equals(wrbKreditoren)) {
			wtfKtonrVon.setVisible(true);
			wtfKtonrBis.setVisible(true);
			wlaKtonrVon.setVisible(true);
			wlaKtonrBis.setVisible(true);

			setupCheckBoxMitVorperiodenSaldo() ;
		}
	}

	public String getModul() {
		return FinanzReportFac.REPORT_MODUL;
	}

	public String getReportname() {
		return FinanzReportFac.REPORT_KONTOBLATT;
	}

	public boolean getBErstelleReportSofort() {
		return false;
	}

	public JasperPrintLP getReport(String sDrucktype) throws Throwable {

		String kontoartCNr = "";

		if (wrbSachkonten.isSelected())
			kontoartCNr = FinanzServiceFac.KONTOTYP_SACHKONTO;
		else if (wrbDebitoren.isSelected())
			kontoartCNr = FinanzServiceFac.KONTOTYP_DEBITOR;
		else if (wrbKreditoren.isSelected())
			kontoartCNr = FinanzServiceFac.KONTOTYP_KREDITOR;

		Integer kontoIId = null;
		if (wrbSelektiertesKonto.isSelected()) {
			kontoIId = kontoDto.getIId();
		}

		PrintKontoblaetterModel model = new PrintKontoblaetterModel(
				wdfVon.getTimestamp(),
				wdfBis.getTimestamp(), 
				((InternalFrameFinanz) getInternalFrame()).getAktuellesGeschaeftsjahr()) ;
		model.setKontoIId(kontoIId) ;
		model.setVonKontoNr(wtfKtonrVon.getText()) ;
		model.setBisKontoNr(wtfKtonrBis.getText()) ;
		model.setSortOrder(wrbSortDatum.isSelected(), wrbSortBeleg.isSelected()) ;
		model.setKontotypCNr(kontoartCNr) ;
		model.setEnableSaldo(wchkEnableSaldoVorperiode.isSelected()) ;
		model.setDruckInMandantenWaehrung(wcbPrintInMandantenWaehrung.isSelected());
		model.setSortiereNachAZK(wcbSortiereNachAZK.isSelected());
		return DelegateFactory.getInstance().getFinanzReportDelegate().printKontoblaetter(model) ;
	}

	public MailtextDto getMailtextDto() throws Throwable {
		MailtextDto mailtextDto = PanelReportKriterien
				.getDefaultMailtextDto(this);
		return mailtextDto;
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return PanelBasis.NO_VALUE_THATS_OK_JCOMPONENT;
	}

	protected ReportSaldenlisteKriterienDto getKriterien() {
		ReportSaldenlisteKriterienDto krit = new ReportSaldenlisteKriterienDto();

		if (wrbSachkonten.isSelected())
			krit.setKontotypCNr(FinanzServiceFac.KONTOTYP_SACHKONTO);
		else if (wrbDebitoren.isSelected())
			krit.setKontotypCNr(FinanzServiceFac.KONTOTYP_DEBITOR);
		else if (wrbKreditoren.isSelected())
			krit.setKontotypCNr(FinanzServiceFac.KONTOTYP_KREDITOR);

		return krit;
	}

}
