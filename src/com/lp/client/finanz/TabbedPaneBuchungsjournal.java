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

import javax.swing.JMenu;
import javax.swing.event.ChangeEvent;

import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQuery;
import com.lp.client.frame.component.TabbedPane;
import com.lp.client.frame.component.WrapperMenuBar;
import com.lp.client.frame.component.WrapperMenuItem;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.util.fastlanereader.gui.QueryType;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.finanz.service.BuchungDto;
import com.lp.server.finanz.service.FinanzFac;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;

@SuppressWarnings("static-access")
/**
 * <p> Diese Klasse kuemmert sich um die Panels der Buchungsjournale</p>
 *
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 *
 * <p>Erstellung: Martin Bluehweis; xx.xx.05</p>
 *
 * <p>@author $Author: christian $</p>
 *
 * @version not attributable Date $Date: 2012/12/03 13:08:24 $
 */
public class TabbedPaneBuchungsjournal extends TabbedPane {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private PanelQuery panelTop1QueryBuchungen = null;
	private PanelQuery panelTop2QueryBuchungdetails = null;
	private PanelQuery panelTop3QueryBuchungdetailliert = null;
	private BuchungDto buchungDto = null;
	private static final int IDX_BUCHUNGEN = 0;
	private static final int IDX_BUCHUNGDETAILS = 1;
	private static final int IDX_BUCHUNGENDETAILLIERT = 2;
	
	private static final String MENU_ACTION_EXPORT = "MENU_ACTION_EXPORT";
	// private static final String MENU_ACTION_GESCHAEFTSJAHR_AENDERUNG =
	// "MENU_ACTION_GESCHAEFTSJAHR_AENDERUNG";

	private WrapperMenuBar mainMenuBar = null;
	private IGeschaeftsjahrViewController geschaeftsJahrViewController = null;

	public TabbedPaneBuchungsjournal(InternalFrame internalFrameI,
			IGeschaeftsjahrViewController viewController) throws Throwable {
		super(internalFrameI, LPMain.getInstance().getTextRespectUISPr(
				"finanz.tab.unten.buchungsjournal.title"));
		geschaeftsJahrViewController = viewController;

		jbInit();
		initComponents();
	}

	private void jbInit() throws Throwable {
		// Tab 1: Buchungsjournale
		insertTab(LPMain.getInstance().getTextRespectUISPr("finanz.buchungen"),
				null, null,
				LPMain.getInstance().getTextRespectUISPr("finanz.buchungen"),
				IDX_BUCHUNGEN);
		insertTab(
				LPMain.getInstance().getTextRespectUISPr(
						"finanz.buchungen.details"),
				null,
				null,
				LPMain.getInstance().getTextRespectUISPr(
						"finanz.buchungen.details"), IDX_BUCHUNGDETAILS);
		insertTab(
				LPMain.getInstance().getTextRespectUISPr(
						"fb.buchungjournal.detailliert"),
				null,
				null,
				LPMain.getInstance().getTextRespectUISPr(
						"fb.buchungjournal.detailliert"),
				IDX_BUCHUNGENDETAILLIERT);

		// Default
		setSelectedComponent(getPanelTop1QueryBuchungsjournale());
		// refresh
		getPanelTop1QueryBuchungsjournale().setDefaultFilter(
				buildFiltersBuchungsjournale());
		getPanelTop1QueryBuchungsjournale().eventYouAreSelected(false);

		// damit gleich einer selektiert ist
		ItemChangedEvent it = new ItemChangedEvent(
				getPanelTop1QueryBuchungsjournale(),
				ItemChangedEvent.ITEM_CHANGED);
		lPEventItemChanged(it);
		// Listener
		addChangeListener(this);
		getInternalFrame().addItemChangedListener(this);
	}

	public void setBuchungDto(BuchungDto buchungDto) {
		this.buchungDto = buchungDto;
	}

	public BuchungDto getBuchungDto() {
		return buchungDto;
	}

	/**
	 * 4 Verarbeite unsere eigenen Itemevents die von anderen Panels, Dialogen,
	 * ... kommen.
	 * 
	 * @param e
	 *            ItemChangedEvent
	 * @throws Throwable
	 */
	public void lPEventItemChanged(ItemChangedEvent e) throws Throwable {
		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			// da will jemand die Buchungen ansehen
			if (e.getSource() == panelTop1QueryBuchungen) {
				Object key = panelTop1QueryBuchungen.getSelectedId();
				// nur wechseln wenns auch einen gibt
				if (key != null) {
					BuchungDto buchungDto = DelegateFactory.getInstance()
							.getBuchenDelegate()
							.buchungFindByPrimaryKey((Integer) key);
					setBuchungDto(buchungDto);
					setSelectedComponent(getPanelTop2QueryBuchungen());
				}
			}
		} else if (e.getID() == ItemChangedEvent.ITEM_CHANGED) {
			if (e.getSource() == panelTop1QueryBuchungen) {
				Object key = panelTop1QueryBuchungen.getSelectedId();
				if (key != null) {
					BuchungDto buchungsjournalDto = DelegateFactory
							.getInstance().getBuchenDelegate()
							.buchungFindByPrimaryKey((Integer) key);
					setBuchungDto(buchungsjournalDto);
					getInternalFrame().enableAllOberePanelsExceptMe(this,
							IDX_BUCHUNGEN, true);
				} else {
					getInternalFrame().enableAllOberePanelsExceptMe(this,
							IDX_BUCHUNGEN, false);
				}
				panelTop1QueryBuchungen.updateButtons();
			} else if (e.getSource() == panelTop2QueryBuchungdetails) {
				panelTop2QueryBuchungdetails.updateButtons();
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_PRINT) {
			if (e.getSource() == getPanelTop1QueryBuchungsjournale()) {
				if (this.getBuchungDto() != null) {
					getInternalFrame().showReportKriterien(
							new ReportBuchungsjournal(getInternalFrame(), this
									.getBuchungDto().getIId(),
									getPanelTop1QueryBuchungsjournale(),
									"Buchungsjournal"));
				}
			} else if (e.getSource() == getPanelTop3QueryBuchungenDetailliert()) {
				if (this.getBuchungDto() != null) {
					getInternalFrame().showReportKriterien(
							new ReportBuchungsjournal(getInternalFrame(), this
									.getBuchungDto().getIId(),
									getPanelTop3QueryBuchungenDetailliert(),
									"Buchungsjournal"));
				}
			} else if (e.getSource() == getPanelTop2QueryBuchungen()) {
				getInternalFrame().showReportKriterien(
						new ReportBuchungenInBuchungsjournal(
								getInternalFrame(), this.getBuchungDto()
										.getIId(),
								"Buchungen im Buchungsjournal"));
			}
		}
	}

	public void lPEventObjectChanged(ChangeEvent e) throws Throwable {
		super.lPEventObjectChanged(e);
		int selectedIndex = this.getSelectedIndex();

		if (selectedIndex == IDX_BUCHUNGEN) {
			panelTop1QueryBuchungen.eventYouAreSelected(false);
		} else if (selectedIndex == IDX_BUCHUNGDETAILS) {
			getPanelTop2QueryBuchungen().eventYouAreSelected(false);
		} else if (selectedIndex == IDX_BUCHUNGENDETAILLIERT) {
			getPanelTop3QueryBuchungenDetailliert().eventYouAreSelected(false);
		}
	}

	/*
	 * private void initPanelTop3QueryBuchungen() {
	 * 
	 * QueryType[] qtBuchungen = buildQueryTypesBuchungen(); String[]
	 * aWhichButtonIUseBuchungen = { PanelBasis.ACTION_FILTER,
	 * PanelBasis.ACTION_REFRESH, PanelBasis.ACTION_GOTO,
	 * PanelBasis.ACTION_PRINT}; FilterKriterium[] filtersBuchungen =
	 * buildFiltersBuchungen();
	 * 
	 * panelTop3QueryBuchungen = new PanelQuery( qtBuchungen, filtersBuchungen,
	 * QueryParameters.UC_ID_BUCHUNGDETAIL, aWhichButtonIUseBuchungen,
	 * getInternalFrame(),
	 * LPMain.getInstance().getTextRespectUISPr("finanz.buchungen"),true); }
	 */

	protected void lPActionEvent(java.awt.event.ActionEvent e) throws Throwable {
		if(e.getActionCommand().equals(MENU_ACTION_EXPORT)) {
			new DialogBuchungsjournalExport(LPMain.getInstance().getDesktop()).setVisible(true);
		} else {
			String selectedGJ = geschaeftsJahrViewController
					.getSelectedGeschaeftsjahr(e.getActionCommand());
			if (null != selectedGJ) {
				if (panelTop1QueryBuchungen != null) {
					panelTop1QueryBuchungen.updateButtons();
					initPanelTop3QueryBuchungen();
					panelTop1QueryBuchungen
							.setDefaultFilter(buildFiltersBuchungsjournale());
					panelTop1QueryBuchungen.eventYouAreSelected(false);
				}
				if (panelTop3QueryBuchungdetailliert != null) {
					panelTop3QueryBuchungdetailliert.updateButtons();
	
					panelTop3QueryBuchungdetailliert
							.setDefaultFilter(buildFiltersBuchungsjournaleDetailliert());
					panelTop3QueryBuchungdetailliert.eventYouAreSelected(false);
				}
			}
		}
	}

	private void initPanelTop3QueryBuchungen() throws Throwable {
		if (panelTop1QueryBuchungen == null) {
			QueryType[] qtBuchungsjournale = FinanzFilterFactory.getInstance()
					.createQTBuchungsjournale();
			String[] aWhichButtonIUseBuchungsjournale = {
					PanelBasis.ACTION_REFRESH, PanelBasis.ACTION_FILTER,
					PanelBasis.ACTION_PRINT };
			FilterKriterium[] filtersBuchungen = buildFiltersBuchungsjournale();

			panelTop1QueryBuchungen = new PanelQuery(qtBuchungsjournale,
					filtersBuchungen, QueryParameters.UC_ID_BUCHUNG,
					aWhichButtonIUseBuchungsjournale, getInternalFrame(),
					LPMain.getInstance().getTextRespectUISPr("lp.auswahl"),
					true);
			setComponentAt(IDX_BUCHUNGEN, panelTop1QueryBuchungen);

			panelTop1QueryBuchungen
					.befuellePanelFilterkriterienDirektUndVersteckte(
							FinanzFilterFactory.getInstance()
									.createFKDBelegnummer(), null,
							FinanzFilterFactory.getInstance()
									.createFKVBuchungStorno(), LPMain
									.getTextRespectUISPr("lp.plusstornierte"));

		}
		// Filter updaten
		panelTop1QueryBuchungen
				.setDefaultFilter(buildFiltersBuchungsjournale());
	}

	private FilterKriterium[] buildFiltersBuchungsjournale() throws Throwable {
		FilterKriterium[] filtersAll = new FilterKriterium[2];
		filtersAll[0] = ((InternalFrameFinanz) getInternalFrame())
				.getFKforAktuellesGeschaeftsjahr();

		filtersAll[1] = new FilterKriterium(
				FinanzFac.FLR_BUCHUNG_FLRKOSTENSTELLE + ".mandant_c_nr", true,
				"'" + LPMain.getInstance().getTheClient().getMandant() + "'",
				FilterKriterium.OPERATOR_EQUAL, false);
		return filtersAll;
	}

	private FilterKriterium[] buildFiltersBuchungsjournaleDetailliert()
			throws Throwable {
		FilterKriterium[] filtersAll = new FilterKriterium[2];
		filtersAll[0] = ((InternalFrameFinanz) getInternalFrame())
				.getFKforAktuellesGeschaeftsjahrBuchungsjournalDetailliert();

		filtersAll[1] = new FilterKriterium(
				FinanzFac.FLR_BUCHUNGDETAIL_FLRBUCHUNG + "."
						+ FinanzFac.FLR_BUCHUNG_FLRKOSTENSTELLE
						+ ".mandant_c_nr", true, "'"
						+ LPMain.getInstance().getTheClient().getMandant()
						+ "'", FilterKriterium.OPERATOR_EQUAL, false);
		return filtersAll;
	}

	private PanelQuery getPanelTop1QueryBuchungsjournale() throws Throwable {
		if (panelTop1QueryBuchungen == null) {
			String[] aWhichButtonIUseBuchungsjournale = {
					PanelBasis.ACTION_FILTER, PanelBasis.ACTION_PRINT };
			QueryType[] qtBuchungsjournale = FinanzFilterFactory.getInstance()
					.createQTBuchungsjournale();

			panelTop1QueryBuchungen = new PanelQuery(qtBuchungsjournale, null,
					QueryParameters.UC_ID_BUCHUNG,
					aWhichButtonIUseBuchungsjournale, getInternalFrame(),
					LPMain.getInstance().getTextRespectUISPr("lp.auswahl"),
					true);
			setComponentAt(IDX_BUCHUNGEN, panelTop1QueryBuchungen);

			panelTop1QueryBuchungen
					.befuellePanelFilterkriterienDirektUndVersteckte(
							FinanzFilterFactory.getInstance()
									.createFKDBelegnummer(),
							FinanzFilterFactory.getInstance()
									.createFKDBuchungsjournalText(),
							FinanzFilterFactory.getInstance().createFKVStorno(),
							LPMain.getInstance().getTextRespectUISPr(
									"lp.plusstornierte"));

		}
		return panelTop1QueryBuchungen;
	}

	private PanelQuery getPanelTop2QueryBuchungen() throws Throwable {

		Integer buchungIId = null;
		if (getBuchungDto() != null) {
			buchungIId = getBuchungDto().getIId();
		}

		if (panelTop2QueryBuchungdetails == null) {
			String[] aWhichButtonIUseBuchung = { PanelBasis.ACTION_REFRESH,
					PanelBasis.ACTION_FILTER /* , PanelBasis.ACTION_PRINT */};
			QueryType[] qtBuchung = FinanzFilterFactory.getInstance()
					.createQTBuchungDetail();

			panelTop2QueryBuchungdetails = new PanelQuery(qtBuchung,
					FinanzFilterFactory.getInstance().createFKBuchungsdetail(
							buchungIId),
					QueryParameters.UC_ID_BUCHUNGDETAILBUCHUNGSJOURNAL,
					aWhichButtonIUseBuchung, getInternalFrame(), LPMain
							.getInstance().getTextRespectUISPr(
									"finanz.buchungen.details"), true);
			setComponentAt(IDX_BUCHUNGDETAILS, panelTop2QueryBuchungdetails);
		} else {
			// filter refreshen.
			panelTop2QueryBuchungdetails.setDefaultFilter(FinanzFilterFactory
					.getInstance().createFKBuchungsdetail(buchungIId));
		}
		return panelTop2QueryBuchungdetails;
	}

	private PanelQuery getPanelTop3QueryBuchungenDetailliert() throws Throwable {

		if (panelTop3QueryBuchungdetailliert == null) {
			String[] aWhichButtonIUseBuchung = { PanelBasis.ACTION_REFRESH,
					PanelBasis.ACTION_FILTER, PanelBasis.ACTION_PRINT };
			QueryType[] qtBuchung = FinanzFilterFactory.getInstance()
					.createQTBuchungDetail();

			panelTop3QueryBuchungdetailliert = new PanelQuery(qtBuchung,
					buildFiltersBuchungsjournaleDetailliert(),
					QueryParameters.UC_ID_BUCHUNGDETAILLIERT,
					aWhichButtonIUseBuchung, getInternalFrame(), LPMain
							.getInstance().getTextRespectUISPr(
									"finanz.buchungen.details"), true);

			panelTop3QueryBuchungdetailliert
					.befuellePanelFilterkriterienDirektUndVersteckte(
							FinanzFilterFactory.getInstance()
									.createFKDBelegnummer(),
							FinanzFilterFactory.getInstance()
									.createFKDBuchungsjournalDetailliertText(),
							FinanzFilterFactory.getInstance()
									.createFKVStornoDetailliert(),
							LPMain.getInstance().getTextRespectUISPr(
									"lp.plusstornierte"));

			panelTop3QueryBuchungdetailliert
					.addDirektFilter(FinanzFilterFactory.getInstance()
							.createFKDBetragssuche());

			setComponentAt(IDX_BUCHUNGENDETAILLIERT,
					panelTop3QueryBuchungdetailliert);

		}
		return panelTop3QueryBuchungdetailliert;
	}

	protected javax.swing.JMenuBar getJMenuBar() throws Throwable {
		if (null != mainMenuBar)
			return mainMenuBar;

		mainMenuBar = new WrapperMenuBar(this);
		JMenu jmBearbeiten = (JMenu) mainMenuBar
				.getComponent(WrapperMenuBar.MENU_BEARBEITEN);

		JMenu menuePeriode = geschaeftsJahrViewController
				.getGeschaeftsJahreMenue(this, this);
		jmBearbeiten.add(menuePeriode);
		
		JMenu jmModul = (JMenu) mainMenuBar
				.getComponent(WrapperMenuBar.MENU_MODUL);
		WrapperMenuItem menueItemExport = new WrapperMenuItem(
				LPMain.getTextRespectUISPr("fb.buchungsjournal.export"),
				RechteFac.RECHT_FB_FINANZ_CUD);
		
		menueItemExport.addActionListener(this);
		menueItemExport
				.setActionCommand(MENU_ACTION_EXPORT);
		jmModul.add(menueItemExport, 0);

		return mainMenuBar;
	}

}
