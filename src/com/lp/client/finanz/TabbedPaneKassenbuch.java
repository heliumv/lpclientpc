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

import javax.swing.JMenu;
import javax.swing.event.ChangeEvent;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.LockStateValue;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQuery;
import com.lp.client.frame.component.PanelSplit;
import com.lp.client.frame.component.TabbedPane;
import com.lp.client.frame.component.WrapperMenuBar;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.delegate.FinanzDelegate;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.system.SystemFilterFactory;
import com.lp.client.util.fastlanereader.gui.QueryType;
import com.lp.server.finanz.service.FinanzServiceFac;
import com.lp.server.finanz.service.KassenbuchDto;
import com.lp.server.finanz.service.KontoDto;
import com.lp.server.finanz.service.PrintKontoblaetterModel;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.FilterKriteriumDirekt;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.util.Helper;

@SuppressWarnings("static-access")
/**
 * <p>Diese Klasse kuemmert sich um die Panels der Kassenbuecher in der FiBu</p>
 *
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 * @author  Martin Bluehweis
 * @version $Revision: 1.12 $
 */
public class TabbedPaneKassenbuch extends TabbedPane {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private PanelQuery panelQueryKassenbuch = null;
	private PanelFinanzKassenbuchKopfdaten panelDetailKassenbuchKopfdaten = null;
	private PanelSplit panelSplitBuchungen = null;
	private PanelQuery panelQueryBuchungen = null;
	private PanelFinanzBuchungDetailsKassenbuch panelDetailBuchung = null;

	private final static int IDX_KASSENBUECHER = 0;
	private final static int IDX_KOPFDATEN = 1;
	private final static int IDX_BUCHUNGEN = 2;

	private KassenbuchDto kassenbuchDto = null;
	private IGeschaeftsjahrViewController geschaeftsjahrViewController = null;
	private WrapperMenuBar mainMenuBar = null;

	public TabbedPaneKassenbuch(InternalFrame internalFrameI,
			IGeschaeftsjahrViewController viewController) throws Throwable {
		super(internalFrameI, LPMain.getInstance().getTextRespectUISPr(
				"finanz.tab.unten.kassenbuch.title"));

		geschaeftsjahrViewController = viewController;
		jbInit();
		initComponents();
	}

	protected KassenbuchDto getKassenbuchDto() {
		return kassenbuchDto;
	}

	protected InternalFrameFinanz getInternalFrameFinanz() {
		return (InternalFrameFinanz) getInternalFrame();
	}

	void setKassenbuchDto(KassenbuchDto kassenbuchDto) {
		this.kassenbuchDto = kassenbuchDto;
		String sTitle;
		if (getKassenbuchDto() != null) {
			sTitle = getKassenbuchDto().getCBez();
		} else {
			sTitle = "";
		}
		getInternalFrame()
				.setLpTitle(InternalFrame.TITLE_IDX_AS_I_LIKE, sTitle);
	}

	private void jbInit() throws Throwable {
		// Tab 1: Kassenbuecher
		insertTab(
				LPMain.getInstance().getTextRespectUISPr("finanz.kassenbuch"),
				null, null,
				LPMain.getInstance().getTextRespectUISPr("finanz.kassenbuch"),
				IDX_KASSENBUECHER);
		// Tab 2: Kopfdaten
		insertTab(LPMain.getInstance().getTextRespectUISPr("lp.kopfdaten"),
				null, null,
				LPMain.getInstance().getTextRespectUISPr("lp.kopfdaten"),
				IDX_KOPFDATEN);
		// Tab 3: Buchungen im Kassenbuch
		if (getInternalFrameFinanz().getBVollversion()) {
			insertTab(
					LPMain.getInstance()
							.getTextRespectUISPr("finanz.buchungen"), null,
					null,
					LPMain.getInstance()
							.getTextRespectUISPr("finanz.buchungen"),
					IDX_BUCHUNGEN);
		}
		setSelectedComponent(getPanelQueryKassenbuch(true));
		// refresh
		getPanelQueryKassenbuch(true).eventYouAreSelected(false);
		// damit gleich eine selektiert ist
		ItemChangedEvent it = new ItemChangedEvent(
				getPanelQueryKassenbuch(true), ItemChangedEvent.ITEM_CHANGED);
		lPEventItemChanged(it);
		// Listener
		addChangeListener(this);
		getInternalFrame().addItemChangedListener(this);
	}

	/**
	 * getPanelQueryKassenbuch mit extrem lazy loading.
	 * 
	 * @return PanelQuery
	 * @param bNeedInstantiationIfNull
	 *            boolean
	 * @throws Throwable
	 */
	private PanelQuery getPanelQueryKassenbuch(boolean bNeedInstantiationIfNull)
			throws Throwable {
		if (panelQueryKassenbuch == null && bNeedInstantiationIfNull) {
			String[] aWhichButtonIUseKassenbuch = { PanelBasis.ACTION_NEW };
			QueryType[] qtKassenbuch = null;
			FilterKriterium[] filtersKassenbuch = SystemFilterFactory
					.getInstance().createFKMandantCNr();

			panelQueryKassenbuch = new PanelQuery(qtKassenbuch,
					filtersKassenbuch, QueryParameters.UC_ID_KASSENBUCH,
					aWhichButtonIUseKassenbuch, getInternalFrame(), LPMain
							.getInstance().getTextRespectUISPr(
									"finanz.kassenbuch"), true);
			FilterKriteriumDirekt fkDirekt1 = FinanzFilterFactory.getInstance()
					.createFKDKassenbuchbezeichnung();
			panelQueryKassenbuch.befuellePanelFilterkriterienDirekt(fkDirekt1,
					null);
			setComponentAt(IDX_KASSENBUECHER, panelQueryKassenbuch);
		}
		return panelQueryKassenbuch;
	}

	public void lPEventItemChanged(ItemChangedEvent e) throws Throwable {
		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			// da will jemand die Buchungen ansehen
			if (e.getSource() == getPanelQueryKassenbuch(false)) {
				Object key = getPanelQueryKassenbuch(true).getSelectedId();
				holeKassenbuchDto(key);
				// nur wechseln wenns auch einen gibt
				if (key != null) {
					setSelectedComponent(getPanelSplitBuchungen(true));
					getPanelSplitBuchungen(true).eventYouAreSelected(false);
				}
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_DISCARD
				|| e.getID() == ItemChangedEvent.ACTION_DELETE) {
			if (e.getSource() == panelDetailBuchung) {
				panelSplitBuchungen.eventYouAreSelected(false);
			}
		} else if (e.getID() == ItemChangedEvent.ITEM_CHANGED) {
			if (e.getSource() == getPanelQueryKassenbuch(false)) {
				Object key = getPanelQueryKassenbuch(true).getSelectedId();
				holeKassenbuchDto(key);
				if (key == null) {
					getInternalFrame().enableAllOberePanelsExceptMe(this,
							IDX_KASSENBUECHER, false);
				} else {
					getInternalFrame().enableAllOberePanelsExceptMe(this,
							IDX_KASSENBUECHER, true);
				}
				getPanelQueryKassenbuch(true).updateButtons();
			} else if (e.getSource() == panelQueryBuchungen) {
				Integer iId = (Integer) panelQueryBuchungen.getSelectedId();
				panelDetailBuchung.setKeyWhenDetailPanel(iId);
				panelDetailBuchung.eventYouAreSelected(false);
				panelQueryBuchungen.updateButtons();
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_NEW) {
			if (e.getSource() == getPanelQueryKassenbuch(false)) {
				if (getPanelQueryKassenbuch(true).getSelectedId() == null) {
					getInternalFrame().enableAllPanelsExcept(true);
				}
				getPanelDetailKassenbuchKopfdaten(true).eventActionNew(null,
						true, false);
				setSelectedComponent(getPanelDetailKassenbuchKopfdaten(true));
			} else if (e.getSource() == panelQueryBuchungen) {
				panelDetailBuchung.eventActionNew(e, true, false);
				panelDetailBuchung.eventYouAreSelected(false);
				this.setSelectedComponent(panelSplitBuchungen);
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_UPDATE) {
			if (e.getSource() == panelDetailBuchung) {
				panelQueryBuchungen.updateButtons(new LockStateValue(
						PanelBasis.LOCK_FOR_NEW));
				;
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_PRINT) {
			if (e.getSource() == panelQueryBuchungen) {
				printKontoblatt();
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_GOTO_MY_DEFAULT_QP) {
			if (e.getSource() == getPanelDetailKassenbuchKopfdaten(false)) {
				panelQueryKassenbuch.eventYouAreSelected(false);
				this.setSelectedComponent(getPanelQueryKassenbuch(true));
			} else if (e.getSource() == panelDetailBuchung) {
				setKeyWasForLockMe();
				if (panelDetailBuchung.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryBuchungen
							.getId2SelectAfterDelete();
					panelQueryBuchungen.setSelectedId(oNaechster);
				}
				panelSplitBuchungen.eventYouAreSelected(false);
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_SAVE) {
			if (e.getSource() == getPanelDetailKassenbuchKopfdaten(false)) {
				getPanelQueryKassenbuch(true).clearDirektFilter();
				Object key = getPanelDetailKassenbuchKopfdaten(true)
						.getKeyWhenDetailPanel();
				getPanelQueryKassenbuch(true).eventYouAreSelected(false);
				getPanelQueryKassenbuch(true).setSelectedId(key);
				getPanelQueryKassenbuch(true).eventYouAreSelected(false);
			} else if (e.getSource() == panelDetailBuchung) {
				// panelQueryBuchungen.eventYouAreSelected(false);
				// panelQueryBuchungen.setSelectedId(oKey);
				// panelSplitBuchungen.eventYouAreSelected(false);

				Object detailKey = panelDetailBuchung.getKeyWhenDetailPanel();
				getPanelQueryBuchungen(true).eventActionRefresh(null, false);

				panelQueryBuchungen.setSelectedId(detailKey);
				panelSplitBuchungen.eventYouAreSelected(false);
			}
		}
	}

	public void setKeyWasForLockMe() {
		Object oKey = panelQueryKassenbuch.getSelectedId();

		getInternalFrame().setKeyWasForLockMe(
				oKey == null ? null : oKey.toString());
		// if (oKey != null) {
		// getInternalFrame().setKeyWasForLockMe(oKey.toString());
		// } else {
		// getInternalFrame().setKeyWasForLockMe(null);
		// }
	}

	/**
	 * hole KassenbuchDto.
	 * 
	 * @param key
	 *            Object
	 * @throws Throwable
	 */
	private void holeKassenbuchDto(Object key) throws Throwable {
		if (key != null) {
			KassenbuchDto kassenbuchDto = getFinanzDelegate()
					.kassenbuchFindByPrimaryKey((Integer) key);
			setKassenbuchDto(kassenbuchDto);
			getInternalFrame().setKeyWasForLockMe(key.toString());
			getPanelDetailKassenbuchKopfdaten(true).setKeyWhenDetailPanel(key);
		} else {
			setKassenbuchDto(null);
		}
	}

	public void lPEventObjectChanged(ChangeEvent e) throws Throwable {
		super.lPEventObjectChanged(e);
		refreshSelectedPanel();
	}

	protected void refreshSelectedPanel() throws Throwable {
		int selectedIndex = this.getSelectedIndex();
		if (selectedIndex == IDX_KASSENBUECHER) {
			getPanelQueryKassenbuch(true).eventYouAreSelected(false);
		} else if (selectedIndex == IDX_KOPFDATEN) {
			getPanelDetailKassenbuchKopfdaten(true).eventYouAreSelected(false);
		} else if (selectedIndex == IDX_BUCHUNGEN) {
			getPanelSplitBuchungen(true);
			getPanelQueryBuchungen(true).setDefaultFilter(
					buildFiltersBuchungen());
			getPanelQueryBuchungen(true).eventYouAreSelected(false);
			getPanelSplitBuchungen(true).eventYouAreSelected(false);
		}
	}

	private FilterKriterium[] buildFiltersBuchungen() throws Throwable {
		FilterKriterium[] filtersAll = null;
		if (getKassenbuchDto() != null) {
			filtersAll = Helper.copyFilterKriterium(
					FinanzFilterFactory.getInstance().createFKBuchungDetail(
							getKassenbuchDto().getKontoIId()), 2);

			filtersAll[filtersAll.length - 2] = FinanzFilterFactory
					.getInstance().createFKVBuchungStorno();
			filtersAll[filtersAll.length - 1] = getInternalFrameFinanz()
					.getFKforAktuellesGeschaeftsjahrInDetails();
		}
		return filtersAll;
	}

	private PanelQuery getPanelQueryBuchungen(boolean bNeedInstantiationIfNull)
			throws Throwable {
		if (panelQueryBuchungen == null && bNeedInstantiationIfNull) {
			QueryType[] qtBuchungen = FinanzFilterFactory.getInstance()
					.createQTBuchungDetail();
			String[] aWhichButtonIUseBuchungen = { PanelBasis.ACTION_NEW,
					PanelBasis.ACTION_PRINT };
			FilterKriterium[] filtersBuchungen = buildFiltersBuchungen();

			panelQueryBuchungen = new PanelQuery(qtBuchungen, filtersBuchungen,
					QueryParameters.UC_ID_BUCHUNGDETAILKASSENBUCH,
					aWhichButtonIUseBuchungen, getInternalFrame(), LPMain
							.getInstance().getTextRespectUISPr(
									"finanz.buchungen"), true);
		}
		return panelQueryBuchungen;
	}

	/**
	 * Drucken des Kontoblattes.
	 * 
	 * @throws Throwable
	 */
	protected void printKontoblatt() throws Throwable {
		if (getKassenbuchDto() != null) {
			// PrintKontoblaetterModel model = new PrintKontoblaetterModel(
			// null,
			// null,
			// getInternalFrameFinanz().getAktuellesGeschaeftsjahr()) ;
			// model.setKontoIId(getKassenbuchDto().getKontoIId()) ;
			// model.setSortOrder(PrintKontoblaetterModel.EnumSortOrder.SORT_NACH_DATUM)
			// ;
			// model.setKontotypCNr(FinanzServiceFac.KONTOTYP_SACHKONTO) ;
			// model.setEnableSaldo(true) ;
			// DelegateFactory.getInstance().getFinanzReportDelegate().printKontoblaetter(model)
			// ;

			KontoDto kontoDto = getFinanzDelegate().kontoFindByPrimaryKey(
					getKassenbuchDto().getKontoIId());
			String sTitle = LPMain.getInstance().getTextRespectUISPr(
					"finanz.buchungen");

			PrintKontoblaetterModel model = new PrintKontoblaetterModel(null,
					null, getInternalFrameFinanz().getAktuellesGeschaeftsjahr());
			model.setKontoIId(getKassenbuchDto().getKontoIId());
			model.setSortOrder(PrintKontoblaetterModel.EnumSortOrder.SORT_NACH_DATUM);
			model.setKontotypCNr(FinanzServiceFac.KONTOTYP_SACHKONTO);
			model.setEnableSaldo(true);
			getInternalFrame().showReportKriterien(
					new ReportPeriodeBase(getInternalFrameFinanz(), sTitle,
							model));
		} else {
			DialogFactory.showModalDialog(LPMain.getInstance()
					.getTextRespectUISPr("lp.error"), "Keine Daten zu drucken");
		}
	}

	/**
	 * getPanelDetailKassenbuchKopfdaten mit extrem lazy loading.
	 * 
	 * @param bNeedInstantiationIfNull
	 *            boolean
	 * @return PanelFinanzKassenbuchKopfdaten
	 * @throws Throwable
	 */
	private PanelFinanzKassenbuchKopfdaten getPanelDetailKassenbuchKopfdaten(
			boolean bNeedInstantiationIfNull) throws Throwable {
		if (panelDetailKassenbuchKopfdaten == null && bNeedInstantiationIfNull) {
			panelDetailKassenbuchKopfdaten = new PanelFinanzKassenbuchKopfdaten(
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("lp.kopfdaten"), null, this);
			this.setComponentAt(IDX_KOPFDATEN, panelDetailKassenbuchKopfdaten);
		}
		return panelDetailKassenbuchKopfdaten;
	}

	/**
	 * getPanelSplitBuchungen mit extrem lazy loading.
	 * 
	 * @param bNeedInstantiationIfNull
	 *            boolean
	 * @return PanelSplit
	 * @throws Throwable
	 */
	private PanelSplit getPanelSplitBuchungen(boolean bNeedInstantiationIfNull)
			throws Throwable {
		if (panelSplitBuchungen == null && bNeedInstantiationIfNull) {
			panelSplitBuchungen = new PanelSplit(getInternalFrame(),
					getPanelDetailBuchung(true), getPanelQueryBuchungen(true),
					170);
			this.setComponentAt(IDX_BUCHUNGEN, panelSplitBuchungen);
		}
		return panelSplitBuchungen;
	}

	private PanelFinanzBuchungDetailsKassenbuch getPanelDetailBuchung(
			boolean bNeedInstantiationIfNull) throws Throwable {
		if (panelDetailBuchung == null && bNeedInstantiationIfNull) {
			panelDetailBuchung = new PanelFinanzBuchungDetailsKassenbuch(
					getInternalFrame(), this);
		}
		return panelDetailBuchung;
	}

	private FinanzDelegate getFinanzDelegate() throws ExceptionLP {
		return DelegateFactory.getInstance().getFinanzDelegate();
	}

	protected void lPActionEvent(java.awt.event.ActionEvent e) throws Throwable {
		String selectedGJMenuItem = geschaeftsjahrViewController
				.getSelectedGeschaeftsjahr(e.getActionCommand());
		if (null != selectedGJMenuItem) {
			// String s = selectedGJMenuItem ;
			getPanelQueryBuchungen(true).setDefaultFilter(
					buildFiltersBuchungen());

			refreshSelectedPanel();
		}
	}

	protected javax.swing.JMenuBar getJMenuBar() throws Throwable {
		// return new WrapperMenuBar(this);

		if (null != mainMenuBar)
			return mainMenuBar;

		mainMenuBar = new WrapperMenuBar(this);
		JMenu jmBearbeiten = (JMenu) mainMenuBar
				.getComponent(WrapperMenuBar.MENU_BEARBEITEN);

		JMenu menuePeriode = geschaeftsjahrViewController
				.getGeschaeftsJahreMenue(this, this);
		jmBearbeiten.add(menuePeriode);

		return mainMenuBar;
	}

	public Object getDto() {
		return kassenbuchDto;
	}
}
