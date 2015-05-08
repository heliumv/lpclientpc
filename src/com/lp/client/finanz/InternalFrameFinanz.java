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

import java.awt.event.ActionEvent;
import java.util.EventObject;

import javax.swing.ImageIcon;
import javax.swing.JTabbedPane;

import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.WrapperMenuBar;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.finanz.service.FinanzFac;
import com.lp.server.finanz.service.KontoartDto;
import com.lp.server.finanz.service.LaenderartDto;
import com.lp.server.finanz.service.UvaartDto;
import com.lp.server.system.service.KostenstelleDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MandantDto;
import com.lp.server.system.service.MandantFac;
import com.lp.server.system.service.ModulberechtigungDto;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;

/**
 * <p>
 * Diese Klasse kuemmert sich um das Modul Finanzbuchhaltung<br/>
 * </p>
 * 
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2008
 * </p>
 * 
 * @author Martin Bluehweis
 * @version $Revision: 1.12 $
 */
public class InternalFrameFinanz extends InternalFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// Vollversion der Fibu?
	private boolean bVollversion = false;

	private TabbedPaneKontenSachkonten tabbedPaneSachKonten = null;
	private TabbedPaneBankverbindung tabbedPaneBankverbindung = null;
	private TabbedPaneWaehrung tabbedPaneWaehrung = null;
	private TabbedPaneKontenDebitorenkonten tabbedPaneDebitorenKonten = null;
	private TabbedPaneKontenKreditorenkonten tabbedPaneKreditorenKonten = null;
	private TabbedPaneKassenbuch tabbedPaneKassenbuch = null;
	private TabbedPaneBuchungsjournal tabbedPaneBuchungsjournal = null;
	private TabbedPaneMahnwesen tabbedPaneMahnwesen = null;
	private TabbedPaneErgebnisgruppen tabbedPaneErgebnisgruppen = null;
	private TabbedPaneErgebnisgruppen tabbedPaneBilanzgruppen = null;
	private TabbedPaneFinanzamt tabbedPaneFinanzamt = null;
	private TabbedPaneGrunddaten tabbedPaneGrunddaten = null;
	private TabbedPaneExport tabbedPaneExport = null;
	private TabbedPaneZahlungsvorschlag tabbedPaneZahlungsvorschlag = null;
	private TabbedPaneIntrastat tabbedPaneIntrastat = null;

	public WrapperMenuBar wmbKonten = null;
	private String aktuellesGeschaeftsjahr = null;
	public String sRechtModulweit = null;

	public int IDX_TABBED_PANE_SACHKONTEN = -1;
	public int IDX_TABBED_PANE_BANKVERBINDUNG = -1;
	public int IDX_TABBED_PANE_WAEHRUNG = -1;
	public int IDX_TABBED_PANE_KASSENBUCH = -1;
	public int IDX_TABBED_PANE_DEBITORENKONTEN = -1;
	public int IDX_TABBED_PANE_KREDITORENKONTEN = -1;
	public int IDX_TABBED_PANE_BUCHUNGSJOURNAL = -1;
	public int IDX_TABBED_PANE_MAHNWESEN = -1;
	public int IDX_TABBED_PANE_ERGEBNISGRUPPEN = -1;
	public int IDX_TABBED_PANE_BILANZGRUPPEN = -1;
	public int IDX_TABBED_PANE_FINANZAMT = -1;
	public int IDX_TABBED_PANE_GRUNDDATEN = -1;
	public int IDX_TABBED_PANE_EXPORT = -1;
	public int IDX_TABBED_PANE_ZAHLUNGSVORSCHLAG = -1;
	public int IDX_TABBED_PANE_INTRASTAT = -1;

	private KontoartDto kontoartDto = null;
	private UvaartDto uvaartDto = null;
	private LaenderartDto laenderartDto = null;

	private IGeschaeftsjahrViewController geschaeftsjahrViewController = null;

	private TabbedPaneKonten selectedKontoPane ;
	
	public InternalFrameFinanz(String title, String belegartCNr,
			String sRechtModulweitI) throws Throwable {
		super(title, belegartCNr, sRechtModulweitI);
		sRechtModulweit = sRechtModulweitI;
		geschaeftsjahrViewController = new GeschaeftsjahrViewController(this,
				LPMain.getTheClient());
		jbInit();
		initComponents();
	}

	public boolean getBVollversion() {
		return bVollversion;
	}

	public void setAktuellesGeschaeftsjahr(String newAktuellesGeschaeftsjahr) {
		aktuellesGeschaeftsjahr = newAktuellesGeschaeftsjahr;
	}

	public String getAktuellesGeschaeftsjahr() {
		return aktuellesGeschaeftsjahr;
	}

	public Integer getIAktuellesGeschaeftsjahr() throws Throwable {
		return aktuellesGeschaeftsjahr == null ? DelegateFactory.getInstance()
				.getParameterDelegate().getGeschaeftsjahr() : new Integer(
				aktuellesGeschaeftsjahr);
	}

	/**
	 * Ein Filterkriterium erzeugen welches die FLR_BUCHUNGDETAIL_FLBUCHUNG auf
	 * das aktuelle Geschaeftsjahr einschr&auml;nkt
	 * 
	 * @return Filterkriterium FLR_BUCHUNG f&uuml;r das aktuelle Geschaeftsjahr
	 */
	public FilterKriterium getFKforAktuellesGeschaeftsjahrInDetails() {
		return new FilterKriterium(FinanzFac.FLR_BUCHUNGDETAIL_FLRBUCHUNG + "."
				+ FinanzFac.FLR_BUCHUNG_GESCHAEFTSJAHR_I_GESCHAEFTSJAHR, true,
				"'" + getAktuellesGeschaeftsjahr() + "'",
				FilterKriterium.OPERATOR_EQUAL, false);
	}

	public FilterKriterium getFKforAktuellesGeschaeftsjahr() {
		return new FilterKriterium(
				FinanzFac.FLR_BUCHUNG_GESCHAEFTSJAHR_I_GESCHAEFTSJAHR, true,
				"'" + getAktuellesGeschaeftsjahr() + "'",
				FilterKriterium.OPERATOR_EQUAL, false);
	}

	public FilterKriterium getFKforAktuellesGeschaeftsjahrBuchungsjournalDetailliert() {
		return new FilterKriterium(FinanzFac.FLR_BUCHUNGDETAIL_FLRBUCHUNG + "."
				+ FinanzFac.FLR_BUCHUNG_GESCHAEFTSJAHR_I_GESCHAEFTSJAHR, true,
				"'" + getAktuellesGeschaeftsjahr() + "'",
				FilterKriterium.OPERATOR_EQUAL, false);
	}

	private void jbInit() throws Throwable {
		// Berechtigung pruefen
		ModulberechtigungDto[] modulberechtigungDtos = DelegateFactory
				.getInstance().getMandantDelegate()
				.modulberechtigungFindByMandantCNr();
		for (int i = 0; i < modulberechtigungDtos.length; i++) {
			if (modulberechtigungDtos[i].getBelegartCNr().equals(
					LocaleFac.BELEGART_FINANZBUCHHALTUNG)) {
				bVollversion = true;
			}
		}
		// Zahlungsvorschlag?
		final boolean bZahlungsvorschlag = LPMain
				.getInstance()
				.getDesktop()
				.darfAnwenderAufZusatzfunktionZugreifen(
						MandantFac.ZUSATZFUNKTION_ZAHLUNGSVORSCHLAG);
		// Zahlungsvorschlag?
		final boolean bIntrastat = LPMain
				.getInstance()
				.getDesktop()
				.darfAnwenderAufZusatzfunktionZugreifen(
						MandantFac.ZUSATZFUNKTION_INTRASTAT);

		int index = 0;

		// 1 tab unten: Konten; lazy loading
		IDX_TABBED_PANE_SACHKONTEN = index;
		tabbedPaneRoot
				.insertTab(
						LPMain.getTextRespectUISPr("finanz.tab.unten.sachkonten.title"),
						null,
						null,
						LPMain.getTextRespectUISPr("finanz.tab.unten.sachkonten.tooltip"),
						IDX_TABBED_PANE_SACHKONTEN);
		index++;
		if (bVollversion) {
			// 5 tab unten: Konten; lazy loading
			IDX_TABBED_PANE_DEBITORENKONTEN = index;
			tabbedPaneRoot
					.insertTab(
							LPMain.getTextRespectUISPr("finanz.tab.unten.debitorenkonten.title"),
							null,
							null,
							LPMain.getTextRespectUISPr("finanz.tab.unten.debitorenkonten.tooltip"),
							IDX_TABBED_PANE_DEBITORENKONTEN);
			index++;
			// 6 tab unten: Konten; lazy loading
			IDX_TABBED_PANE_KREDITORENKONTEN = index;
			tabbedPaneRoot
					.insertTab(
							LPMain.getTextRespectUISPr("finanz.tab.unten.kreditorenkonten.title"),
							null,
							null,
							LPMain.getTextRespectUISPr("finanz.tab.unten.kreditorenkonten.tooltip"),
							IDX_TABBED_PANE_KREDITORENKONTEN);
			index++;

			// TODO: ghp workaround JTabbedPane.setComponentAt() dynamisch hat
			// moeglicherweise
			// ein Probleme mit neu instanzierten Panels die via setComponentAt
			// gesetzt werden, weil
			// dann mehrere Panels gleichzeitig "visible" waeren. Siehe auch
			// BugId "6285072" von Sun/Oracle
			// Wenn jetzt das Lazy-Loading nicht durchgefuehrt wird, dann klappt
			// das.
			getTabbedPaneDebitorenKonten();
			getTabbedPaneKreditorenKonten();
		}
		// 2 tab unten: Bankverbindung; lazy loading
		IDX_TABBED_PANE_BANKVERBINDUNG = index;
		tabbedPaneRoot
				.insertTab(
						LPMain.getTextRespectUISPr("finanz.tab.unten.bankverbindungen.title"),
						null,
						null,
						LPMain.getTextRespectUISPr("finanz.tab.unten.bankverbindungen.tooltip"),
						IDX_TABBED_PANE_BANKVERBINDUNG);
		index++;
		// 3 tab unten: Waehrungen; lazy loading
		IDX_TABBED_PANE_WAEHRUNG = index;
		tabbedPaneRoot
				.insertTab(
						LPMain.getTextRespectUISPr("finanz.tab.unten.waehrung.title"),
						null,
						null,
						LPMain.getTextRespectUISPr("finanz.tab.unten.waehrung.tooltip"),
						IDX_TABBED_PANE_WAEHRUNG);
		index++;
		// 4 tab unten: Kassenbuecher; lazy loading
		IDX_TABBED_PANE_KASSENBUCH = index;
		tabbedPaneRoot
				.insertTab(
						LPMain.getTextRespectUISPr("finanz.tab.unten.kassenbuch.title"),
						null,
						null,
						LPMain.getTextRespectUISPr("finanz.tab.unten.kassenbuch.tooltip"),
						IDX_TABBED_PANE_KASSENBUCH);
		index++;
		if (bVollversion) {
			// 7 tab unten: Buchungsjournal; lazy loading
			IDX_TABBED_PANE_BUCHUNGSJOURNAL = index;
			tabbedPaneRoot
					.insertTab(
							LPMain.getTextRespectUISPr("finanz.tab.unten.buchungsjournal.title"),
							null,
							null,
							LPMain.getTextRespectUISPr("finanz.tab.unten.buchungsjournal.tooltip"),
							IDX_TABBED_PANE_BUCHUNGSJOURNAL);
			index++;
			// 8 tab unten: Mahnwesen ; lazy loading
			IDX_TABBED_PANE_MAHNWESEN = index;
			tabbedPaneRoot
					.insertTab(
							LPMain.getTextRespectUISPr("finanz.tab.unten.mahnwesen.title"),
							null,
							null,
							LPMain.getTextRespectUISPr("finanz.tab.unten.mahnwesen.tooltip"),
							IDX_TABBED_PANE_MAHNWESEN);
			index++;
			// 10 tab unten: Ergebnisgruppen; lazy loading
			IDX_TABBED_PANE_ERGEBNISGRUPPEN = index;
			tabbedPaneRoot
					.insertTab(
							LPMain.getTextRespectUISPr("finanz.tab.unten.ergebnisgruppen.title"),
							null,
							null,
							LPMain.getTextRespectUISPr("finanz.tab.unten.ergebnisgruppen.tooltip"),
							IDX_TABBED_PANE_ERGEBNISGRUPPEN);
			index++;
			// 10 tab unten: Ergebnisgruppen; lazy loading
			IDX_TABBED_PANE_BILANZGRUPPEN = index;
			tabbedPaneRoot
					.insertTab(
							LPMain.getTextRespectUISPr("finanz.tab.unten.bilanzgruppen.title"),
							null,
							null,
							LPMain.getTextRespectUISPr("finanz.tab.unten.bilanzgruppen.tooltip"),
							IDX_TABBED_PANE_BILANZGRUPPEN);
			index++;
		}

		if (DelegateFactory.getInstance().getTheJudgeDelegate()
				.hatRecht(RechteFac.RECHT_FB_CHEFBUCHHALTER)) {

			// 11 tab unten: Finanzaemter; lazy loading
			IDX_TABBED_PANE_FINANZAMT = index;
			tabbedPaneRoot
					.insertTab(
							LPMain.getTextRespectUISPr("finanz.tab.unten.finanzamt.title"),
							null,
							null,
							LPMain.getTextRespectUISPr("finanz.tab.unten.finanzamt.tooltip"),
							IDX_TABBED_PANE_FINANZAMT);
			index++;
		}
		if (!bVollversion) {
			// Der Export steht nur in der fibu-losen version zu
			// 12 tab unten: Export; lazy loading
			IDX_TABBED_PANE_EXPORT = index;
			tabbedPaneRoot
					.insertTab(
							LPMain.getTextRespectUISPr("finanz.tab.unten.export.title"),
							null,
							null,
							LPMain.getTextRespectUISPr("finanz.tab.unten.export.tooltip"),
							IDX_TABBED_PANE_EXPORT);
			index++;
		}

		if (bZahlungsvorschlag) {
			// 13 tab unten: Zahlungsvorschlag
			IDX_TABBED_PANE_ZAHLUNGSVORSCHLAG = index;
			tabbedPaneRoot
					.insertTab(
							LPMain.getTextRespectUISPr("finanz.tab.unten.zahlungsvorschlag.title"),
							null,
							null,
							LPMain.getTextRespectUISPr("finanz.tab.unten.zahlungsvorschlag.tooltip"),
							IDX_TABBED_PANE_ZAHLUNGSVORSCHLAG);
			index++;
		}

		if (bIntrastat) {
			// 13 tab unten: Zahlungsvorschlag
			IDX_TABBED_PANE_INTRASTAT = index;
			tabbedPaneRoot
					.insertTab(
							LPMain.getTextRespectUISPr("finanz.tab.unten.intrastat.title"),
							null,
							null,
							LPMain.getTextRespectUISPr("finanz.tab.unten.intrastat.tooltip"),
							IDX_TABBED_PANE_INTRASTAT);
			index++;
		}

		if (bVollversion) {
			// 14 tab unten: Grunddaten; lazy loading
			// nur anzeigen wenn Benutzer Recht dazu hat
			if (DelegateFactory.getInstance().getTheJudgeDelegate()
					.hatRecht(RechteFac.RECHT_LP_DARF_GRUNDDATEN_SEHEN)) {
				IDX_TABBED_PANE_GRUNDDATEN = index;
				tabbedPaneRoot
						.insertTab(
								LPMain.getTextRespectUISPr("finanz.tab.unten.grunddaten.title"),
								null,
								null,
								LPMain.getTextRespectUISPr("finanz.tab.unten.grunddaten.tooltip"),
								IDX_TABBED_PANE_GRUNDDATEN);
			}
			index++;
		}
		// dem frame das icon setzen
		ImageIcon iicon = new javax.swing.ImageIcon(getClass().getResource(
				"/com/lp/client/res/books16x16.png"));
		setFrameIcon(iicon);

		registerChangeListeners();
		tabbedPaneRoot.setSelectedComponent(getTabbedPaneSachKonten());
		selectedKontoPane = getTabbedPaneSachKonten() ;

		tabbedPaneSachKonten.lPEventObjectChanged(null);
	}

	public void lPStateChanged(EventObject e) throws Throwable {
		int selectedCur = tabbedPaneRoot.getSelectedIndex();
		setRechtModulweit(sRechtModulweit);
		selectedKontoPane = null ;		
		if (selectedCur == IDX_TABBED_PANE_SACHKONTEN) {
			// Info an Tabbedpane, bist selektiert worden.
			getTabbedPaneSachKonten().lPEventObjectChanged(null);
			selectedKontoPane = getTabbedPaneSachKonten() ;
		}
		if (selectedCur == IDX_TABBED_PANE_DEBITORENKONTEN) {
			// Info an Tabbedpane, bist selektiert worden.
			getTabbedPaneDebitorenKonten().lPEventObjectChanged(null);
			selectedKontoPane = getTabbedPaneDebitorenKonten() ;
		}
		if (selectedCur == IDX_TABBED_PANE_KREDITORENKONTEN) {
			// Info an Tabbedpane, bist selektiert worden.
			getTabbedPaneKreditorenKonten().lPEventObjectChanged(null);
			selectedKontoPane = getTabbedPaneKreditorenKonten() ;
		} else if (selectedCur == IDX_TABBED_PANE_KASSENBUCH) {
			// Info an Tabbedpane, bist selektiert worden.
			getTabbedPaneKassenbuch().lPEventObjectChanged(null);
		} else if (selectedCur == IDX_TABBED_PANE_BUCHUNGSJOURNAL) {
			// Info an Tabbedpane, bist selektiert worden.
			getTabbedPaneBuchungsjournal().lPEventObjectChanged(null);
		} else if (selectedCur == IDX_TABBED_PANE_BANKVERBINDUNG) {
			// Info an Tabbedpane, bist selektiert worden.
			getTabbedPaneBankverbindung().lPEventObjectChanged(null);
		} else if (selectedCur == IDX_TABBED_PANE_MAHNWESEN) {
			// Info an Tabbedpane, bist selektiert worden.
			getTabbedPaneMahnwesen().lPEventObjectChanged(null);
		} else if (selectedCur == IDX_TABBED_PANE_ZAHLUNGSVORSCHLAG) {
			// Info an Tabbedpane, bist selektiert worden.
			getTabbedPaneZahlungsvorschlag().lPEventObjectChanged(null);
		} else if (selectedCur == IDX_TABBED_PANE_ERGEBNISGRUPPEN) {
			// Info an Tabbedpane, bist selektiert worden.
			getTabbedPaneErgebnisgruppen().lPEventObjectChanged(null);
		} else if (selectedCur == IDX_TABBED_PANE_BILANZGRUPPEN) {
			// Info an Tabbedpane, bist selektiert worden.
			getTabbedPaneBilanzgruppen().lPEventObjectChanged(null);
		} else if (selectedCur == IDX_TABBED_PANE_FINANZAMT) {

			setRechtModulweit(RechteFac.RECHT_MODULWEIT_READ);
			boolean hatRecht = DelegateFactory.getInstance()
					.getTheJudgeDelegate()
					.hatRecht(RechteFac.RECHT_FB_CHEFBUCHHALTER);
			if (hatRecht == true) {
				setRechtModulweit(RechteFac.RECHT_MODULWEIT_UPDATE);
			}

			// Info an Tabbedpane, bist selektiert worden.
			getTabbedPaneFinanzamt().lPEventObjectChanged(null);
		} else if (selectedCur == IDX_TABBED_PANE_WAEHRUNG) {
			// Info an Tabbedpane, bist selektiert worden.
			getTabbedPaneWaehrung().lPEventObjectChanged(null);
		} else if (selectedCur == IDX_TABBED_PANE_GRUNDDATEN) {
			getTabbedPaneGrunddaten();
			tabbedPaneRoot.setSelectedComponent(tabbedPaneGrunddaten);
			// Info an Tabbedpane, bist selektiert worden.
			tabbedPaneGrunddaten.lPEventObjectChanged(null);
		} else if (selectedCur == IDX_TABBED_PANE_EXPORT) {
			// Info an Tabbedpane, bist selektiert worden.
			getTabbedPaneExport().lPEventObjectChanged(null);
		} else if (selectedCur == IDX_TABBED_PANE_INTRASTAT) {
			// Info an Tabbedpane, bist selektiert worden.
			getTabbedPaneIntrastat().lPEventObjectChanged(null);
		}
	}

	@Override
	public JTabbedPane getSelectedTabbedPane() {
		return selectedKontoPane ; 
	}
	
	private TabbedPaneKontenSachkonten getTabbedPaneSachKonten()
			throws Throwable {
		if (tabbedPaneSachKonten == null) {
			// lazy loading
			tabbedPaneSachKonten = new TabbedPaneKontenSachkonten(this,
					geschaeftsjahrViewController);
			tabbedPaneRoot.setComponentAt(IDX_TABBED_PANE_SACHKONTEN,
					tabbedPaneSachKonten);
			initComponents();
		}
		return tabbedPaneSachKonten;
	}

	private TabbedPaneKontenDebitorenkonten getTabbedPaneDebitorenKonten()
			throws Throwable {
		if (tabbedPaneDebitorenKonten == null) {
			// lazy loading
			tabbedPaneDebitorenKonten = new TabbedPaneKontenDebitorenkonten(
					this, geschaeftsjahrViewController);
			tabbedPaneRoot.setComponentAt(IDX_TABBED_PANE_DEBITORENKONTEN,
					tabbedPaneDebitorenKonten);
			initComponents();
		}
		return tabbedPaneDebitorenKonten;
	}

	private TabbedPaneKontenKreditorenkonten getTabbedPaneKreditorenKonten()
			throws Throwable {
		if (tabbedPaneKreditorenKonten == null) {
			// lazy loading
			tabbedPaneKreditorenKonten = new TabbedPaneKontenKreditorenkonten(
					this, geschaeftsjahrViewController);
			tabbedPaneRoot.setComponentAt(IDX_TABBED_PANE_KREDITORENKONTEN,
					tabbedPaneKreditorenKonten);
			initComponents();
		}
		return tabbedPaneKreditorenKonten;
	}

	private TabbedPaneKassenbuch getTabbedPaneKassenbuch() throws Throwable {
		if (tabbedPaneKassenbuch == null) {
			// lazy loading
			tabbedPaneKassenbuch = new TabbedPaneKassenbuch(this,
					geschaeftsjahrViewController);
			tabbedPaneRoot.setComponentAt(IDX_TABBED_PANE_KASSENBUCH,
					tabbedPaneKassenbuch);
			initComponents();
		}
		return tabbedPaneKassenbuch;
	}

	private TabbedPaneBuchungsjournal getTabbedPaneBuchungsjournal()
			throws Throwable {
		if (tabbedPaneBuchungsjournal == null) {
			// lazy loading
			tabbedPaneBuchungsjournal = new TabbedPaneBuchungsjournal(this,
					geschaeftsjahrViewController);
			tabbedPaneRoot.setComponentAt(IDX_TABBED_PANE_BUCHUNGSJOURNAL,
					tabbedPaneBuchungsjournal);
			initComponents();
		}
		return tabbedPaneBuchungsjournal;
	}

	private TabbedPaneBankverbindung getTabbedPaneBankverbindung()
			throws Throwable {
		if (tabbedPaneBankverbindung == null) {
			// lazy loading
			tabbedPaneBankverbindung = new TabbedPaneBankverbindung(this);
			tabbedPaneRoot.setComponentAt(IDX_TABBED_PANE_BANKVERBINDUNG,
					tabbedPaneBankverbindung);
			initComponents();
		}
		return tabbedPaneBankverbindung;
	}

	public KostenstelleDto getDefaultKostenstelle() throws Throwable {
		MandantDto mDto = DelegateFactory.getInstance().getMandantDelegate()
				.mandantFindByPrimaryKey(LPMain.getTheClient().getMandant());

		if (mDto.getKostenstelleIIdFibu() == null) {
			return null;
		} else {
			return DelegateFactory
					.getInstance()
					.getSystemDelegate()
					.kostenstelleFindByPrimaryKey(mDto.getKostenstelleIIdFibu());
		}

	}

	
	private TabbedPaneExport getTabbedPaneExport() throws Throwable {
		if (tabbedPaneExport == null) {
			// lazy loading
			tabbedPaneExport = new TabbedPaneExport(this);
			tabbedPaneRoot.setComponentAt(IDX_TABBED_PANE_EXPORT,
					tabbedPaneExport);
			initComponents();
		}
		return tabbedPaneExport;
	}

	private TabbedPaneIntrastat getTabbedPaneIntrastat() throws Throwable {
		if (tabbedPaneIntrastat == null) {
			// lazy loading
			tabbedPaneIntrastat = new TabbedPaneIntrastat(this);
			tabbedPaneRoot.setComponentAt(IDX_TABBED_PANE_INTRASTAT,
					tabbedPaneIntrastat);
			initComponents();
		}
		return tabbedPaneIntrastat;
	}

	private TabbedPaneZahlungsvorschlag getTabbedPaneZahlungsvorschlag()
			throws Throwable {
		if (tabbedPaneZahlungsvorschlag == null) {
			// lazy loading
			tabbedPaneZahlungsvorschlag = new TabbedPaneZahlungsvorschlag(this);
			tabbedPaneRoot.setComponentAt(IDX_TABBED_PANE_ZAHLUNGSVORSCHLAG,
					tabbedPaneZahlungsvorschlag);
			initComponents();
		}
		return tabbedPaneZahlungsvorschlag;
	}

	private TabbedPaneMahnwesen getTabbedPaneMahnwesen() throws Throwable {
		if (tabbedPaneMahnwesen == null) {
			// lazy loading
			tabbedPaneMahnwesen = new TabbedPaneMahnwesen(this);
			tabbedPaneRoot.setComponentAt(IDX_TABBED_PANE_MAHNWESEN,
					tabbedPaneMahnwesen);
			initComponents();
		}
		return tabbedPaneMahnwesen;
	}

	private TabbedPaneErgebnisgruppen getTabbedPaneErgebnisgruppen()
			throws Throwable {
		if (tabbedPaneErgebnisgruppen == null) {
			// lazy loading

			tabbedPaneErgebnisgruppen = new TabbedPaneErgebnisgruppen(
					this,
					LPMain.getTextRespectUISPr("finanz.tab.unten.ergebnisgruppen.title"),
					false);
			tabbedPaneRoot.setComponentAt(IDX_TABBED_PANE_ERGEBNISGRUPPEN,
					tabbedPaneErgebnisgruppen);
			initComponents();
		}
		return tabbedPaneErgebnisgruppen;
	}

	private TabbedPaneErgebnisgruppen getTabbedPaneBilanzgruppen()
			throws Throwable {
		if (tabbedPaneBilanzgruppen == null) {
			// lazy loading
			tabbedPaneBilanzgruppen = new TabbedPaneErgebnisgruppen(
					this,
					LPMain.getTextRespectUISPr("finanz.tab.unten.bilanzgruppen.title"),
					true);
			tabbedPaneRoot.setComponentAt(IDX_TABBED_PANE_BILANZGRUPPEN,
					tabbedPaneBilanzgruppen);
			initComponents();
		}
		return tabbedPaneBilanzgruppen;
	}

	private TabbedPaneFinanzamt getTabbedPaneFinanzamt() throws Throwable {
		if (tabbedPaneFinanzamt == null) {
			// lazy loading
			tabbedPaneFinanzamt = new TabbedPaneFinanzamt(this);
			tabbedPaneRoot.setComponentAt(IDX_TABBED_PANE_FINANZAMT,
					tabbedPaneFinanzamt);
			initComponents();
		}
		return tabbedPaneFinanzamt;
	}

	private TabbedPaneWaehrung getTabbedPaneWaehrung() throws Throwable {
		if (tabbedPaneWaehrung == null) {
			// lazy loading
			tabbedPaneWaehrung = new TabbedPaneWaehrung(this);
			tabbedPaneRoot.setComponentAt(IDX_TABBED_PANE_WAEHRUNG,
					tabbedPaneWaehrung);
			initComponents();

		}
		return tabbedPaneWaehrung;
	}

	private TabbedPaneGrunddaten getTabbedPaneGrunddaten() throws Throwable {
		if (tabbedPaneGrunddaten == null) {
			// lazy loading
			tabbedPaneGrunddaten = new TabbedPaneGrunddaten(this);
			tabbedPaneRoot.setComponentAt(IDX_TABBED_PANE_GRUNDDATEN,
					tabbedPaneGrunddaten);
			initComponents();
		}
		return tabbedPaneGrunddaten;
	}

	public KontoartDto getKontoartDto() {
		return kontoartDto;
	}

	public void setKontoartDto(KontoartDto kontoartDto) {
		this.kontoartDto = kontoartDto;
	}

	public UvaartDto getUvaartDto() {
		return uvaartDto;
	}

	public void setUvaartDto(UvaartDto uvaartDto) {
		this.uvaartDto = uvaartDto;
	}

	public LaenderartDto getLaenderartDto() {
		return laenderartDto;
	}

	public void setLaenderartDto(LaenderartDto laenderartDto) {
		this.laenderartDto = laenderartDto;
	}

	protected void menuActionPerformed(ActionEvent e) throws Throwable {
	}
}
