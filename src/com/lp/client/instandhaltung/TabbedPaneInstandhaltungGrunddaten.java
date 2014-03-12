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
package com.lp.client.instandhaltung;

import javax.swing.event.ChangeEvent;

import com.lp.client.artikel.ArtikelFilterFactory;
import com.lp.client.artikel.PanelHersteller;
import com.lp.client.frame.LockStateValue;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQuery;
import com.lp.client.frame.component.PanelSplit;
import com.lp.client.frame.component.TabbedPane;
import com.lp.client.frame.component.WrapperMenuBar;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.stueckliste.StuecklisteFilterFactory;
import com.lp.client.system.SystemFilterFactory;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;

@SuppressWarnings("static-access")
/*
 * <p>&UUml;berschrift: </p> <p>Beschreibung: </p> <p>Copyright: Copyright (c)
 * 2004</p> <p>Organisation: </p>
 * 
 * @author Christian Kollmann
 * 
 * @version $Revision: 1.7 $
 */
public class TabbedPaneInstandhaltungGrunddaten extends TabbedPane {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private PanelQuery panelQueryHalle = null;
	private PanelBasis panelSplitHalle = null;
	private PanelHalle panelBottomHalle = null;

	private PanelQuery panelQueryAnlage = null;
	private PanelBasis panelSplitAnlage = null;
	private PanelBasis panelBottomAnlage = null;

	private PanelQuery panelQueryMaschine = null;
	private PanelBasis panelSplitMaschine = null;
	private PanelBasis panelBottomMaschine = null;

	private PanelQuery panelQueryGeraetetyp = null;
	private PanelBasis panelSplitGeraetetyp = null;
	private PanelBasis panelBottomGeraetetyp = null;

	private PanelQuery panelQueryKategorie = null;
	private PanelBasis panelSplitKategorie = null;
	private PanelBasis panelBottomKategorie = null;

	private PanelQuery panelQueryGewerk = null;
	private PanelBasis panelSplitGewerk = null;
	private PanelBasis panelBottomGewerk = null;

	private PanelQuery panelQueryHersteller = null;
	private PanelBasis panelSplitHersteller = null;
	private PanelBasis panelBottomHersteller = null;

	private static int IDX_PANEL_HALLE = 0;
	private static int IDX_PANEL_ANLAGE = 1;
	private static int IDX_PANEL_MASCHINE = 2;
	private static int IDX_PANEL_GERAETETYP = 3;
	private static int IDX_PANEL_ISKATEGORIE = 4;
	private static int IDX_PANEL_GEWERK = 5;
	private static int IDX_PANEL_HERSTELLER = 6;

	private WrapperMenuBar wrapperMenuBar = null;

	public TabbedPaneInstandhaltungGrunddaten(InternalFrame internalFrameI)
			throws Throwable {
		super(internalFrameI, LPMain.getInstance().getTextRespectUISPr(
				"pers.title.tab.grunddaten"));

		jbInit();
		initComponents();
	}

	private void createHalle() throws Throwable {
		if (panelSplitHalle == null) {
			String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW };
			panelQueryHalle = new PanelQuery(null, null,
					QueryParameters.UC_ID_HALLE, aWhichButtonIUse,
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("is.halle"), true);

			panelBottomHalle = new PanelHalle(getInternalFrame(), LPMain
					.getInstance().getTextRespectUISPr("is.halle"), null);
			panelSplitHalle = new PanelSplit(getInternalFrame(),
					panelBottomHalle, panelQueryHalle, 350);

			panelQueryHalle.befuellePanelFilterkriterienDirekt(
					StuecklisteFilterFactory.getInstance()
							.createFKDBezeichnungAllgemein(), null);

			setComponentAt(IDX_PANEL_HALLE, panelSplitHalle);
		}
	}

	private void createHersteller() throws Throwable {
		if (panelSplitHersteller == null) {
			String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW };
			panelQueryHersteller = new PanelQuery(null, null,
					QueryParameters.UC_ID_ARTIKELHERSTELLER, aWhichButtonIUse,
					getInternalFrame(),
					LPMain.getInstance().getTextRespectUISPr(
							"artikel.title.tab.hersteller"), true);
			panelQueryHersteller.befuellePanelFilterkriterienDirekt(
					ArtikelFilterFactory.getInstance().createFKDHersteller(),
					ArtikelFilterFactory.getInstance()
							.createFKDHerstellerPartner());

			panelBottomHersteller = new PanelHersteller(getInternalFrame(),
					LPMain.getInstance().getTextRespectUISPr(
							"artikel.title.tab.hersteller"), null);

			panelSplitHersteller = new PanelSplit(getInternalFrame(),
					panelBottomHersteller, panelQueryHersteller, 380);

			setComponentAt(IDX_PANEL_HERSTELLER, panelSplitHersteller);
		}
	}

	private void createMaschine() throws Throwable {
		if (panelSplitMaschine == null) {
			String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW };
			panelQueryMaschine = new PanelQuery(null, null,
					QueryParameters.UC_ID_ISMASCHINE, aWhichButtonIUse,
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("is.maschine"), true);

			panelBottomMaschine = new PanelIsmaschine(getInternalFrame(),
					LPMain.getInstance().getTextRespectUISPr("is.maschine"),
					null);
			panelSplitMaschine = new PanelSplit(getInternalFrame(),
					panelBottomMaschine, panelQueryMaschine, 350);

			panelQueryMaschine.befuellePanelFilterkriterienDirekt(
					StuecklisteFilterFactory.getInstance()
							.createFKDBezeichnungAllgemein(), null);

			setComponentAt(IDX_PANEL_MASCHINE, panelSplitMaschine);
		}
	}

	private void createGeraetetyp() throws Throwable {
		if (panelSplitGeraetetyp == null) {
			String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW };
			panelQueryGeraetetyp = new PanelQuery(null, null,
					QueryParameters.UC_ID_GERAETETYP, aWhichButtonIUse,
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("is.geraetetyp"), true);

			panelBottomGeraetetyp = new PanelGeraetetyp(getInternalFrame(),
					LPMain.getInstance().getTextRespectUISPr("is.geraetetyp"),
					null);
			panelSplitGeraetetyp = new PanelSplit(getInternalFrame(),
					panelBottomGeraetetyp, panelQueryGeraetetyp, 350);

			setComponentAt(IDX_PANEL_GERAETETYP, panelSplitGeraetetyp);
		}
	}

	private void createKategorie() throws Throwable {
		if (panelSplitKategorie == null) {
			String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW };
			panelQueryKategorie = new PanelQuery(null, SystemFilterFactory
					.getInstance().createFKMandantCNr(),
					QueryParameters.UC_ID_ISKATEGORIE, aWhichButtonIUse,
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("is.kategorie"), true);

			panelBottomKategorie = new PanelIskategorie(getInternalFrame(),
					LPMain.getInstance().getTextRespectUISPr("is.kategorie"),
					null);
			panelSplitKategorie = new PanelSplit(getInternalFrame(),
					panelBottomKategorie, panelQueryKategorie, 350);

			setComponentAt(IDX_PANEL_ISKATEGORIE, panelSplitKategorie);
		}
	}

	private void createGewerk() throws Throwable {
		if (panelSplitGewerk == null) {
			String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW };
			panelQueryGewerk = new PanelQuery(null, SystemFilterFactory
					.getInstance().createFKMandantCNr(),
					QueryParameters.UC_ID_GEWERK, aWhichButtonIUse,
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("is.gewerk"), true);

			panelBottomGewerk = new PanelGewerk(getInternalFrame(), LPMain
					.getInstance().getTextRespectUISPr("is.gewerk"), null);
			panelSplitGewerk = new PanelSplit(getInternalFrame(),
					panelBottomGewerk, panelQueryGewerk, 350);

			setComponentAt(IDX_PANEL_GEWERK, panelSplitGewerk);
		}
	}

	private void createAnlage() throws Throwable {
		if (panelSplitAnlage == null) {
			String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW };

			panelQueryAnlage = new PanelQuery(null, null,
					QueryParameters.UC_ID_ANLAGE, aWhichButtonIUse,
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("is.anlage"), true);
			panelBottomAnlage = new PanelAnlage(getInternalFrame(), LPMain
					.getInstance().getTextRespectUISPr("is.anlage"), null);

			panelSplitAnlage = new PanelSplit(getInternalFrame(),
					panelBottomAnlage, panelQueryAnlage, 300);
			panelQueryAnlage.befuellePanelFilterkriterienDirekt(
					StuecklisteFilterFactory.getInstance()
							.createFKDBezeichnungAllgemein(), null);

			setComponentAt(IDX_PANEL_ANLAGE, panelSplitAnlage);
		}
	}

	private void jbInit() throws Throwable {
		// Kollektiv

		// 1 tab oben: QP1 PartnerFLR; lazy loading
		insertTab(LPMain.getInstance().getTextRespectUISPr("is.halle"), null,
				null, LPMain.getInstance().getTextRespectUISPr("is.halle"),
				IDX_PANEL_HALLE);

		insertTab(LPMain.getInstance().getTextRespectUISPr("is.anlage"), null,
				null, LPMain.getInstance().getTextRespectUISPr("is.anlage"),
				IDX_PANEL_ANLAGE);
		insertTab(LPMain.getInstance().getTextRespectUISPr("is.maschine"),
				null, null,
				LPMain.getInstance().getTextRespectUISPr("is.maschine"),
				IDX_PANEL_MASCHINE);
		insertTab(LPMain.getInstance().getTextRespectUISPr("is.geraetetyp"),
				null, null,
				LPMain.getInstance().getTextRespectUISPr("is.geraetetyp"),
				IDX_PANEL_GERAETETYP);
		insertTab(LPMain.getInstance().getTextRespectUISPr("is.kategorie"),
				null, null,
				LPMain.getInstance().getTextRespectUISPr("is.kategorie"),
				IDX_PANEL_ISKATEGORIE);
		insertTab(LPMain.getInstance().getTextRespectUISPr("is.gewerk"), null,
				null, LPMain.getInstance().getTextRespectUISPr("is.gewerk"),
				IDX_PANEL_GEWERK);
		insertTab(
				LPMain.getInstance().getTextRespectUISPr(
						"artikel.title.tab.hersteller"),
				null,
				null,
				LPMain.getInstance().getTextRespectUISPr(
						"artikel.title.tab.hersteller"), IDX_PANEL_HERSTELLER);

		createHalle();

		// Itemevents an MEIN Detailpanel senden kann.
		refreshTitle();
		this.addChangeListener(this);
		getInternalFrame().addItemChangedListener(this);

	}

	public void lPEventItemChanged(ItemChangedEvent e) throws Throwable {

		if (e.getID() == ItemChangedEvent.ITEM_CHANGED) {
			if (e.getSource() == panelQueryHalle) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				panelBottomHalle.setKeyWhenDetailPanel(key);
				panelBottomHalle.eventYouAreSelected(false);
				panelQueryHalle.updateButtons();
			} else if (e.getSource() == panelQueryAnlage) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				panelBottomAnlage.setKeyWhenDetailPanel(key);
				panelBottomAnlage.eventYouAreSelected(false);
				panelQueryAnlage.updateButtons();
			} else if (e.getSource() == panelQueryMaschine) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				panelBottomMaschine.setKeyWhenDetailPanel(key);
				panelBottomMaschine.eventYouAreSelected(false);
				panelQueryMaschine.updateButtons();
			} else if (e.getSource() == panelQueryHersteller) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				panelBottomHersteller.setKeyWhenDetailPanel(key);
				panelBottomHersteller.eventYouAreSelected(false);
				panelQueryHersteller.updateButtons();

			} else if (e.getSource() == panelQueryGeraetetyp) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				panelBottomGeraetetyp.setKeyWhenDetailPanel(key);
				panelBottomGeraetetyp.eventYouAreSelected(false);
				panelQueryGeraetetyp.updateButtons();
			} else if (e.getSource() == panelQueryKategorie) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				panelBottomKategorie.setKeyWhenDetailPanel(key);
				panelBottomKategorie.eventYouAreSelected(false);
				panelQueryKategorie.updateButtons();
			} else if (e.getSource() == panelQueryGewerk) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				panelBottomGewerk.setKeyWhenDetailPanel(key);
				panelBottomGewerk.eventYouAreSelected(false);
				panelQueryGewerk.updateButtons();
			}

		} else if (e.getID() == ItemChangedEvent.ACTION_YOU_ARE_SELECTED) {
			refreshTitle();
			panelSplitHalle.eventYouAreSelected(false);
		} else if (e.getID() == ItemChangedEvent.ACTION_UPDATE) {
			if (e.getSource() == panelBottomHalle) {
				panelQueryHalle.updateButtons(new LockStateValue(
						PanelBasis.LOCK_FOR_NEW));
			} else if (e.getSource() == panelBottomAnlage) {
				panelQueryAnlage.updateButtons(new LockStateValue(
						PanelBasis.LOCK_FOR_NEW));
			} else if (e.getSource() == panelBottomMaschine) {
				panelQueryMaschine.updateButtons(new LockStateValue(
						PanelBasis.LOCK_FOR_NEW));
			} else if (e.getSource() == panelBottomGeraetetyp) {
				panelQueryGeraetetyp.updateButtons(new LockStateValue(
						PanelBasis.LOCK_FOR_NEW));
			} else if (e.getSource() == panelBottomKategorie) {
				panelQueryKategorie.updateButtons(new LockStateValue(
						PanelBasis.LOCK_FOR_NEW));
			} else if (e.getSource() == panelBottomGewerk) {
				panelQueryGewerk.updateButtons(new LockStateValue(
						PanelBasis.LOCK_FOR_NEW));
			} else if (e.getSource() == panelBottomHersteller) {
				panelQueryHersteller.updateButtons(new LockStateValue(
						PanelBasis.LOCK_FOR_NEW));
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_POSITION_VONNNACHNMINUS1) {
			int iPos = panelQueryHalle.getTable().getSelectedRow();

			// wenn die Position nicht die erste ist
			if (iPos > 0) {
				Integer iIdPosition = (Integer) panelQueryHalle.getSelectedId();

				Integer iIdPositionMinus1 = (Integer) panelQueryHalle
						.getTable().getValueAt(iPos - 1, 0);

				DelegateFactory.getInstance().getKuecheDelegate()
						.vertauscheKassaartikel(iIdPosition, iIdPositionMinus1);

				// die Liste neu anzeigen und den richtigen Datensatz markieren
				panelQueryHalle.setSelectedId(iIdPosition);
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_POSITION_VONNNACHNPLUS1) {
			if (e.getSource() == panelQueryHalle) {
				int iPos = panelQueryHalle.getTable().getSelectedRow();

				// wenn die Position nicht die letzte ist
				if (iPos < panelQueryHalle.getTable().getRowCount() - 1) {
					Integer iIdPosition = (Integer) panelQueryHalle
							.getSelectedId();

					Integer iIdPositionPlus1 = (Integer) panelQueryHalle
							.getTable().getValueAt(iPos + 1, 0);

					DelegateFactory
							.getInstance()
							.getKuecheDelegate()
							.vertauscheKassaartikel(iIdPosition,
									iIdPositionPlus1);

					// die Liste neu anzeigen und den richtigen Datensatz
					// markieren
					panelQueryHalle.setSelectedId(iIdPosition);
				}
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_POSITION_VORPOSITIONEINFUEGEN) {
		}

		else if (e.getID() == ItemChangedEvent.ACTION_NEW) {
			if (e.getSource() == panelQueryHalle) {
				panelBottomHalle.eventActionNew(e, true, false);
				panelBottomHalle.eventYouAreSelected(false);
			}

			else if (e.getSource() == panelQueryAnlage) {
				panelBottomAnlage.eventActionNew(e, true, false);
				panelBottomAnlage.eventYouAreSelected(false);
			} else if (e.getSource() == panelQueryMaschine) {
				panelBottomMaschine.eventActionNew(e, true, false);
				panelBottomMaschine.eventYouAreSelected(false);
			} else if (e.getSource() == panelQueryGeraetetyp) {
				panelBottomGeraetetyp.eventActionNew(e, true, false);
				panelBottomGeraetetyp.eventYouAreSelected(false);
			} else if (e.getSource() == panelQueryKategorie) {
				panelBottomKategorie.eventActionNew(e, true, false);
				panelBottomKategorie.eventYouAreSelected(false);
			} else if (e.getSource() == panelQueryGewerk) {
				panelBottomGewerk.eventActionNew(e, true, false);
				panelBottomGewerk.eventYouAreSelected(false);
			} else if (e.getSource() == panelQueryHersteller) {
				panelBottomHersteller.eventActionNew(e, true, false);
				panelBottomHersteller.eventYouAreSelected(false);
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_DISCARD) {
			if (e.getSource() == panelBottomHalle) {
				panelSplitHalle.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomAnlage) {
				panelSplitAnlage.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomMaschine) {
				panelSplitMaschine.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomGeraetetyp) {
				panelSplitGeraetetyp.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomKategorie) {
				panelSplitKategorie.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomGewerk) {
				panelSplitGewerk.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomHersteller) {
				panelSplitHersteller.eventYouAreSelected(false);
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_SAVE) {
			if (e.getSource() == panelBottomHalle) {
				Object oKey = panelBottomHalle.getKeyWhenDetailPanel();
				panelQueryHalle.eventYouAreSelected(false);
				panelQueryHalle.setSelectedId(oKey);
				panelSplitHalle.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomAnlage) {
				Object oKey = panelBottomAnlage.getKeyWhenDetailPanel();
				panelQueryAnlage.eventYouAreSelected(false);
				panelQueryAnlage.setSelectedId(oKey);
				panelSplitAnlage.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomMaschine) {
				Object oKey = panelBottomMaschine.getKeyWhenDetailPanel();
				panelQueryMaschine.eventYouAreSelected(false);
				panelQueryMaschine.setSelectedId(oKey);
				panelSplitMaschine.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomGeraetetyp) {
				Object oKey = panelBottomGeraetetyp.getKeyWhenDetailPanel();
				panelQueryGeraetetyp.eventYouAreSelected(false);
				panelQueryGeraetetyp.setSelectedId(oKey);
				panelSplitGeraetetyp.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomKategorie) {
				Object oKey = panelBottomKategorie.getKeyWhenDetailPanel();
				panelQueryKategorie.eventYouAreSelected(false);
				panelQueryKategorie.setSelectedId(oKey);
				panelSplitKategorie.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomGewerk) {
				Object oKey = panelBottomGewerk.getKeyWhenDetailPanel();
				panelQueryGewerk.eventYouAreSelected(false);
				panelQueryGewerk.setSelectedId(oKey);
				panelSplitGewerk.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomHersteller) {
				Object oKey = panelBottomHersteller.getKeyWhenDetailPanel();
				panelQueryHersteller.eventYouAreSelected(false);
				panelQueryHersteller.setSelectedId(oKey);
				panelSplitHersteller.eventYouAreSelected(false);
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_GOTO_MY_DEFAULT_QP) {
			if (e.getSource() == panelBottomHalle) {
				Object oKey = panelQueryHalle.getSelectedId();
				if (oKey != null) {
					getInternalFrame().setKeyWasForLockMe(oKey.toString());
				} else {
					getInternalFrame().setKeyWasForLockMe(null);
				}
				if (panelBottomHalle.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryHalle
							.getId2SelectAfterDelete();
					panelQueryHalle.setSelectedId(oNaechster);
				}
				panelSplitHalle.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomHersteller) {
				Object oKey = panelQueryHersteller.getSelectedId();
				if (oKey != null) {
					getInternalFrame().setKeyWasForLockMe(oKey.toString());
				} else {
					getInternalFrame().setKeyWasForLockMe(null);
				}
				if (panelBottomHersteller.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryHersteller
							.getId2SelectAfterDelete();
					panelQueryHersteller.setSelectedId(oNaechster);
				}
				panelSplitHersteller.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomAnlage) {
				Object oKey = panelQueryAnlage.getSelectedId();
				if (oKey != null) {
					getInternalFrame().setKeyWasForLockMe(oKey.toString());
				} else {
					getInternalFrame().setKeyWasForLockMe(null);
				}
				if (panelBottomAnlage.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryAnlage
							.getId2SelectAfterDelete();
					panelQueryAnlage.setSelectedId(oNaechster);
				}

				panelSplitAnlage.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomMaschine) {
				Object oKey = panelQueryMaschine.getSelectedId();
				if (oKey != null) {
					getInternalFrame().setKeyWasForLockMe(oKey.toString());
				} else {
					getInternalFrame().setKeyWasForLockMe(null);
				}
				if (panelBottomMaschine.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryMaschine
							.getId2SelectAfterDelete();
					panelQueryMaschine.setSelectedId(oNaechster);
				}

				panelSplitMaschine.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomGeraetetyp) {
				Object oKey = panelQueryGeraetetyp.getSelectedId();
				if (oKey != null) {
					getInternalFrame().setKeyWasForLockMe(oKey.toString());
				} else {
					getInternalFrame().setKeyWasForLockMe(null);
				}
				if (panelBottomGeraetetyp.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryGeraetetyp
							.getId2SelectAfterDelete();
					panelQueryGeraetetyp.setSelectedId(oNaechster);
				}

				panelSplitGeraetetyp.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomKategorie) {
				Object oKey = panelQueryKategorie.getSelectedId();
				if (oKey != null) {
					getInternalFrame().setKeyWasForLockMe(oKey.toString());
				} else {
					getInternalFrame().setKeyWasForLockMe(null);
				}
				if (panelBottomKategorie.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryKategorie
							.getId2SelectAfterDelete();
					panelQueryKategorie.setSelectedId(oNaechster);
				}

				panelSplitKategorie.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomGewerk) {
				Object oKey = panelQueryGewerk.getSelectedId();
				if (oKey != null) {
					getInternalFrame().setKeyWasForLockMe(oKey.toString());
				} else {
					getInternalFrame().setKeyWasForLockMe(null);
				}
				if (panelBottomGewerk.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryGewerk
							.getId2SelectAfterDelete();
					panelQueryGewerk.setSelectedId(oNaechster);
				}

				panelSplitGewerk.eventYouAreSelected(false);
			}
		}

	}

	private void refreshTitle() {

		getInternalFrame().setLpTitle(
				InternalFrame.TITLE_IDX_OHRWASCHLUNTEN,
				LPMain.getInstance().getTextRespectUISPr(
						"pers.title.tab.grunddaten"));
		getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_OHRWASCHLOBEN,
				((PanelBasis) this.getSelectedComponent()).getAdd2Title());
		getInternalFrame().setLpTitle(3, "");
		getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_AS_I_LIKE, "");
	}

	public void lPEventObjectChanged(ChangeEvent e) throws Throwable {
		super.lPEventObjectChanged(e);
		int selectedIndex = this.getSelectedIndex();

		if (selectedIndex == IDX_PANEL_HALLE) {
			createHalle();
			panelSplitHalle.eventYouAreSelected(false);
			panelQueryHalle.updateButtons();
		} else if (selectedIndex == IDX_PANEL_ANLAGE) {
			createAnlage();
			panelSplitAnlage.eventYouAreSelected(false);
			panelQueryAnlage.updateButtons();
		} else if (selectedIndex == IDX_PANEL_MASCHINE) {
			createMaschine();
			panelSplitMaschine.eventYouAreSelected(false);
			panelQueryMaschine.updateButtons();
		} else if (selectedIndex == IDX_PANEL_GERAETETYP) {
			createGeraetetyp();
			panelSplitGeraetetyp.eventYouAreSelected(false);
			panelQueryGeraetetyp.updateButtons();
		} else if (selectedIndex == IDX_PANEL_ISKATEGORIE) {
			createKategorie();
			panelSplitKategorie.eventYouAreSelected(false);
			panelQueryKategorie.updateButtons();
		} else if (selectedIndex == IDX_PANEL_GEWERK) {
			createGewerk();
			panelSplitGewerk.eventYouAreSelected(false);
			panelQueryGewerk.updateButtons();
		} else if (selectedIndex == IDX_PANEL_HERSTELLER) {
			createHersteller();
			panelSplitHersteller.eventYouAreSelected(false);
			panelQueryHersteller.updateButtons();
		}
	}

	protected void lPActionEvent(java.awt.event.ActionEvent e) {

	}

	public javax.swing.JMenuBar getJMenuBar() throws Throwable {
		if (wrapperMenuBar == null) {
			wrapperMenuBar = new WrapperMenuBar(this);
		}
		return wrapperMenuBar;
	}

}
