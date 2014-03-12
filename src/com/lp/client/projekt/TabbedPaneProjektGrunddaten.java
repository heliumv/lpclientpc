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
package com.lp.client.projekt;

import java.awt.event.ActionEvent;

import javax.swing.event.ChangeEvent;

import com.lp.client.frame.HelperClient;
import com.lp.client.frame.LockStateValue;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQuery;
import com.lp.client.frame.component.PanelSplit;
import com.lp.client.frame.component.TabbedPane;
import com.lp.client.frame.component.WrapperMenuBar;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.stammdatencrud.PanelStammdatenCRUD;
import com.lp.client.pc.LPMain;
import com.lp.client.system.SystemFilterFactory;
import com.lp.client.util.fastlanereader.gui.QueryType;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;

@SuppressWarnings("static-access")
/**
 * <p> Diese Klasse kuemmert sich ...</p>
 *
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 *
 * <p>Erstellung: Vorname Nachname; dd.mm.06</p>
 *
 * <p>@author $Author: christian $</p>
 *
 * @version not attributable Date $Date: 2013/01/17 09:02:25 $
 */
public class TabbedPaneProjektGrunddaten extends TabbedPane {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final int IDX_PANEL_KATEGORIE = 0;
	private static final int IDX_PANEL_PROJEKTTYP = 1;
	private static final int IDX_PANEL_STATUS = 2;
	private static final int IDX_PANEL_HISTORYART = 3;
	private static final int IDX_PANEL_ERLEDIGUNGSGRUND = 4;
	private static final int IDX_PANEL_BEREICH = 5;

	private PanelQuery panelQueryKategorie = null;
	private PanelBasis panelSplitKategorie = null;
	private PanelBasis panelBottomKategorie = null;

	private PanelQuery panelQueryProjekttyp = null;
	private PanelBasis panelSplitProjekttyp = null;
	private PanelBasis panelBottomProjekttyp = null;

	private PanelQuery panelQueryStatus = null;
	private PanelBasis panelSplitStatus = null;
	private PanelBasis panelBottomStatus = null;

	private PanelQuery panelQueryHistoryart = null;
	private PanelBasis panelSplitHistoryart = null;
	private PanelBasis panelBottomHistoryart = null;

	private PanelQuery panelQueryErledigungsgrund = null;
	private PanelBasis panelSplitErledigungsgrund = null;
	private PanelBasis panelBottomErledigungsgrund = null;

	private PanelQuery panelQueryBereich = null;
	private PanelBasis panelSplitBereich = null;
	private PanelBasis panelBottomBereich = null;

	public TabbedPaneProjektGrunddaten(InternalFrame internalFrameI)
			throws Throwable {

		super(internalFrameI, LPMain.getInstance().getTextRespectUISPr(
				"lp.grunddaten"));
		jbInit();
		initComponents();
	}

	/**
	 * jbInit
	 * 
	 * @throws Throwable
	 */
	private void jbInit() throws Throwable {

		insertTab(LPMain.getInstance().getTextRespectUISPr("lp.kategorie"),
				null, null,
				LPMain.getInstance().getTextRespectUISPr("lp.kategorie"),
				IDX_PANEL_KATEGORIE);
		refreshPanelKategorie();

		insertTab(
				LPMain.getInstance().getTextRespectUISPr(
						"proj.projekt.label.typ"),
				null,
				null,
				LPMain.getInstance().getTextRespectUISPr(
						"proj.projekt.label.typ"), IDX_PANEL_PROJEKTTYP);
		refreshPanelTyp();

		insertTab(LPMain.getInstance().getTextRespectUISPr("lp.status"), null,
				null, LPMain.getInstance().getTextRespectUISPr("lp.status"),
				IDX_PANEL_STATUS);
		refreshPanelStatus();

		insertTab(LPMain.getInstance().getTextRespectUISPr("proj.historyart"),
				null, null,
				LPMain.getInstance().getTextRespectUISPr("proj.historyart"),
				IDX_PANEL_HISTORYART);
		refreshPanelHistoryart();

		insertTab(
				LPMain.getInstance().getTextRespectUISPr(
						"proj.erledigungsgrund"),
				null,
				null,
				LPMain.getInstance().getTextRespectUISPr(
						"proj.erledigungsgrund"), IDX_PANEL_ERLEDIGUNGSGRUND);
		refreshPanelErledigungsgrund();
		insertTab(LPMain.getInstance().getTextRespectUISPr("proj.bereich"),
				null, null,
				LPMain.getInstance().getTextRespectUISPr("proj.bereich"),
				IDX_PANEL_BEREICH);
		refreshPanelBereich();

		setSelectedComponent(panelSplitKategorie);
		panelQueryKategorie.eventYouAreSelected(false);
		if (getInternalFrameProjekt().getKategorieDto() != null) {

			getInternalFrameProjekt().getKategorieDto().setCNr(
					panelQueryKategorie.getSelectedId() + "");
		}
		addChangeListener(this);
		getInternalFrame().addItemChangedListener(this);

	}

	private void refreshPanelKategorie() throws Throwable {

		if (panelSplitKategorie == null) {
			String[] aWhichStandardButtonIUse = { PanelBasis.ACTION_NEW };
			FilterKriterium[] filterAuswahl = ProjektFilterFactory
					.getInstance().createFKMandantCNr(); // die Filterkriterien
															// aendern sich
															// nicht
			panelQueryKategorie = new PanelQuery(null, filterAuswahl,
					QueryParameters.UC_ID_KATEGORIE, aWhichStandardButtonIUse,
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("lp.kategorie"), true);

			panelBottomKategorie = new PanelStammdatenCRUD(getInternalFrame(),
					LPMain.getInstance().getTextRespectUISPr("lp.kategorie"),
					null, HelperClient.SCRUD_KATEGORIE_FILE,
					getInternalFrameProjekt(), HelperClient.LOCKME_PROJEKT);

			panelSplitKategorie = new PanelSplit(getInternalFrame(),
					panelBottomKategorie, panelQueryKategorie,
					450 - ((PanelStammdatenCRUD) panelBottomKategorie)
							.getAnzahlControls() * 30);

			setComponentAt(IDX_PANEL_KATEGORIE, panelSplitKategorie);
		}
	}

	private void refreshPanelTyp() throws Throwable {
		if (panelSplitProjekttyp == null) {
			String[] aWhichStandardButtonIUse = { PanelBasis.ACTION_NEW };
			FilterKriterium[] filterAuswahl = ProjektFilterFactory
					.getInstance().createFKMandantCNr(); // die Filterkriterien
															// aendern sich
															// nicht
			panelQueryProjekttyp = new PanelQuery(null, filterAuswahl,
					QueryParameters.UC_ID_TYP, aWhichStandardButtonIUse,
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("proj.projekt.label.typ"),
					true);

			panelBottomProjekttyp = new PanelStammdatenCRUD(getInternalFrame(),
					LPMain.getInstance().getTextRespectUISPr(
							"proj.projekt.label.typ"), null,
					HelperClient.SCRUD_TYP_FILE, getInternalFrameProjekt(),
					HelperClient.LOCKME_PROJEKT);

			panelSplitProjekttyp = new PanelSplit(getInternalFrame(),
					panelBottomProjekttyp, panelQueryProjekttyp,
					450 - ((PanelStammdatenCRUD) panelBottomProjekttyp)
							.getAnzahlControls() * 30);

			setComponentAt(IDX_PANEL_PROJEKTTYP, panelSplitProjekttyp);
		}
	}

	private void refreshPanelStatus() throws Throwable {
		if (panelSplitStatus == null) {
			String[] aWhichStandardButtonIUse = { PanelBasis.ACTION_NEW };
			FilterKriterium[] filterAuswahl = ProjektFilterFactory
					.getInstance().createFKMandantCNr(); // die Filterkriterien
															// aendern sich
															// nicht
			panelQueryStatus = new PanelQuery(null, filterAuswahl,
					QueryParameters.UC_ID_PROJEKTSTATUS,
					aWhichStandardButtonIUse, getInternalFrame(), LPMain
							.getInstance().getTextRespectUISPr("lp.status"),
					true);

			panelBottomStatus = new PanelStammdatenCRUD(getInternalFrame(),
					LPMain.getInstance().getTextRespectUISPr("lp.status"),
					null, HelperClient.SCRUD_PROJEKTSTATUS_FILE,
					getInternalFrameProjekt(), HelperClient.LOCKME_PROJEKT);

			panelSplitStatus = new PanelSplit(getInternalFrame(),
					panelBottomStatus, panelQueryStatus,
					450 - ((PanelStammdatenCRUD) panelBottomStatus)
							.getAnzahlControls() * 30);

			setComponentAt(IDX_PANEL_STATUS, panelSplitStatus);
		}
	}

	private void refreshPanelHistoryart() throws Throwable {
		if (panelSplitHistoryart == null) {
			String[] aWhichStandardButtonIUse = { PanelBasis.ACTION_NEW };
			panelQueryHistoryart = new PanelQuery(null, null,
					QueryParameters.UC_ID_HISTORYART, aWhichStandardButtonIUse,
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("proj.historyart"), true);

			panelBottomHistoryart = new PanelHistoryart(
					getInternalFrame(),
					LPMain.getInstance().getTextRespectUISPr("proj.historyart"),
					null);

			panelSplitHistoryart = new PanelSplit(getInternalFrame(),
					panelBottomHistoryart, panelQueryHistoryart, 350);

			setComponentAt(IDX_PANEL_HISTORYART, panelSplitHistoryart);
		}
	}

	private void refreshPanelBereich() throws Throwable {
		if (panelSplitBereich == null) {
			String[] aWhichStandardButtonIUse = { PanelBasis.ACTION_NEW,
					PanelBasis.ACTION_POSITION_VONNNACHNMINUS1,
					PanelBasis.ACTION_POSITION_VONNNACHNPLUS1 };
			panelQueryBereich = new PanelQuery(null, SystemFilterFactory
					.getInstance().createFKMandantCNr(),
					QueryParameters.UC_ID_BEREICH, aWhichStandardButtonIUse,
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("proj.bereich"), true);

			panelBottomBereich = new PanelBereich(getInternalFrame(), LPMain
					.getInstance().getTextRespectUISPr("proj.bereich"), null);

			panelSplitBereich = new PanelSplit(getInternalFrame(),
					panelBottomBereich, panelQueryBereich, 350);

			setComponentAt(IDX_PANEL_BEREICH, panelSplitBereich);
		}
	}

	private void refreshPanelErledigungsgrund() throws Throwable {
		if (panelSplitErledigungsgrund == null) {
			String[] aWhichStandardButtonIUse = { PanelBasis.ACTION_NEW };
			
			
			
			panelQueryErledigungsgrund = new PanelQuery(null, ProjektFilterFactory.getInstance().createFKMandantCNr(),
					QueryParameters.UC_ID_PROJEKTERLEDIGUNGSGRUND,
					aWhichStandardButtonIUse, getInternalFrame(), LPMain
							.getInstance().getTextRespectUISPr(
									"proj.erledigungsgrund"), true);

			panelBottomErledigungsgrund = new PanelProjekterledigungsgrund(
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("proj.erledigungsgrund"), null);

			panelSplitErledigungsgrund = new PanelSplit(getInternalFrame(),
					panelBottomErledigungsgrund, panelQueryErledigungsgrund,
					350);

			setComponentAt(IDX_PANEL_ERLEDIGUNGSGRUND,
					panelSplitErledigungsgrund);
		}
	}

	public InternalFrameProjekt getInternalFrameProjekt() {
		return (InternalFrameProjekt) getInternalFrame();
	}

	/**
	 * changed
	 * 
	 * @param eI
	 *            ItemChangedEvent
	 * @throws Throwable
	 */
	public void lPEventItemChanged(ItemChangedEvent eI) throws Throwable {
		if (eI.getID() == ItemChangedEvent.ITEM_CHANGED) {
			if (eI.getSource() == panelQueryKategorie) {
				String cNr = (String) panelQueryKategorie.getSelectedId();
				getInternalFrame().setKeyWasForLockMe(cNr + "");
				panelBottomKategorie.setKeyWhenDetailPanel(cNr);
				panelBottomKategorie.eventYouAreSelected(false);
				// im QP die Buttons in den Zustand nolocking/save setzen.

				panelQueryKategorie.updateButtons(panelBottomKategorie
						.getLockedstateDetailMainKey());
			} else if (eI.getSource() == panelQueryProjekttyp) {
				String cNr = (String) panelQueryProjekttyp.getSelectedId();
				getInternalFrame().setKeyWasForLockMe(cNr + "");
				panelBottomProjekttyp.setKeyWhenDetailPanel(cNr);
				panelBottomProjekttyp.eventYouAreSelected(false);
				// im QP die Buttons in den Zustand nolocking/save setzen.

				panelQueryProjekttyp.updateButtons(panelBottomProjekttyp
						.getLockedstateDetailMainKey());
			} else if (eI.getSource() == panelQueryStatus) {
				String cNr = (String) panelQueryStatus.getSelectedId();
				getInternalFrame().setKeyWasForLockMe(cNr + "");
				panelBottomStatus.setKeyWhenDetailPanel(cNr);
				panelBottomStatus.eventYouAreSelected(false);
				// im QP die Buttons in den Zustand nolocking/save setzen.
				panelQueryStatus.updateButtons(panelBottomStatus
						.getLockedstateDetailMainKey());
			} else if (eI.getSource() == panelQueryHistoryart) {
				Integer iId = (Integer) panelQueryHistoryart.getSelectedId();
				getInternalFrame().setKeyWasForLockMe(iId + "");
				panelBottomHistoryart.setKeyWhenDetailPanel(iId);
				panelBottomHistoryart.eventYouAreSelected(false);
				// im QP die Buttons in den Zustand nolocking/save setzen.
				panelQueryHistoryart.updateButtons(panelBottomHistoryart
						.getLockedstateDetailMainKey());
			} else if (eI.getSource() == panelQueryBereich) {
				Integer iId = (Integer) panelQueryBereich.getSelectedId();
				getInternalFrame().setKeyWasForLockMe(iId + "");
				panelBottomBereich.setKeyWhenDetailPanel(iId);
				panelBottomBereich.eventYouAreSelected(false);
				// im QP die Buttons in den Zustand nolocking/save setzen.
				panelQueryBereich.updateButtons(panelBottomBereich
						.getLockedstateDetailMainKey());
			} else if (eI.getSource() == panelQueryErledigungsgrund) {
				Integer iId = (Integer) panelQueryErledigungsgrund
						.getSelectedId();
				getInternalFrame().setKeyWasForLockMe(iId + "");
				panelBottomErledigungsgrund.setKeyWhenDetailPanel(iId);
				panelBottomErledigungsgrund.eventYouAreSelected(false);
				// im QP die Buttons in den Zustand nolocking/save setzen.
				panelQueryErledigungsgrund
						.updateButtons(panelBottomErledigungsgrund
								.getLockedstateDetailMainKey());
			}
		}

		else if (eI.getID() == ItemChangedEvent.ACTION_DISCARD) {
			// wir landen hier bei der Abfolge Button Aendern -> xx -> Button
			// Discard
			if (eI.getSource() == panelBottomKategorie) {
				panelSplitKategorie.eventYouAreSelected(false); // das 1:n Panel
																// neu aufbauen
			} else if (eI.getSource() == panelBottomProjekttyp) {
				panelSplitProjekttyp.eventYouAreSelected(false); // das 1:n
																	// Panel neu
																	// aufbauen
			} else if (eI.getSource() == panelBottomStatus) {
				panelSplitStatus.eventYouAreSelected(false); // das 1:n Panel
																// neu aufbauen
			} else if (eI.getSource() == panelBottomHistoryart) {
				panelSplitHistoryart.eventYouAreSelected(false); // das 1:n
																	// Panel
				// neu aufbauen
			} else if (eI.getSource() == panelBottomErledigungsgrund) {
				panelSplitErledigungsgrund.eventYouAreSelected(false); // das
																		// 1:n
				// Panel
				// neu aufbauen
			} else if (eI.getSource() == panelBottomBereich) {
				panelSplitBereich.eventYouAreSelected(false); // das
				// 1:n
				// Panel
				// neu aufbauen
			}

		}

		else if (eI.getID() == ItemChangedEvent.ACTION_SAVE) {
			if (eI.getSource() == panelBottomKategorie) {
				Object oKey = panelBottomKategorie.getKeyWhenDetailPanel();
				panelQueryKategorie.eventYouAreSelected(false);
				panelQueryKategorie.setSelectedId(oKey);
				panelSplitKategorie.eventYouAreSelected(false);
			} else if (eI.getSource() == panelBottomProjekttyp) {
				Object oKey = panelBottomProjekttyp.getKeyWhenDetailPanel();
				panelQueryProjekttyp.eventYouAreSelected(false);
				panelQueryProjekttyp.setSelectedId(oKey);
				panelSplitProjekttyp.eventYouAreSelected(false);
			} else if (eI.getSource() == panelBottomStatus) {
				Object oKey = panelBottomStatus.getKeyWhenDetailPanel();
				panelQueryStatus.eventYouAreSelected(false);
				panelQueryStatus.setSelectedId(oKey);
				panelSplitStatus.eventYouAreSelected(false);
			} else if (eI.getSource() == panelBottomHistoryart) {
				Object oKey = panelBottomHistoryart.getKeyWhenDetailPanel();
				panelQueryHistoryart.eventYouAreSelected(false);
				panelQueryHistoryart.setSelectedId(oKey);
				panelSplitHistoryart.eventYouAreSelected(false);
			} else if (eI.getSource() == panelBottomErledigungsgrund) {
				Object oKey = panelBottomErledigungsgrund
						.getKeyWhenDetailPanel();
				panelQueryErledigungsgrund.eventYouAreSelected(false);
				panelQueryErledigungsgrund.setSelectedId(oKey);
				panelSplitErledigungsgrund.eventYouAreSelected(false);
			} else if (eI.getSource() == panelBottomBereich) {
				Object oKey = panelBottomBereich.getKeyWhenDetailPanel();
				panelQueryBereich.eventYouAreSelected(false);
				panelQueryBereich.setSelectedId(oKey);
				panelSplitBereich.eventYouAreSelected(false);
			}

		}

		// wir landen hier nach der Abfolge Button Neu -> xx -> Button Discard
		else if (eI.getID() == ItemChangedEvent.ACTION_GOTO_MY_DEFAULT_QP) {
			if (eI.getSource() == panelBottomKategorie) {
				panelSplitKategorie.eventYouAreSelected(false); // refresh auf
																// das gesamte
																// 1:n panel
			} else if (eI.getSource() == panelBottomProjekttyp) {
				panelSplitProjekttyp.eventYouAreSelected(false); // refresh auf
																	// das
																	// gesamte
																	// 1:n panel
			} else if (eI.getSource() == panelBottomStatus) {
				panelSplitStatus.eventYouAreSelected(false); // refresh auf das
																// gesamte 1:n
																// panel
			} else if (eI.getSource() == panelBottomHistoryart) {
				panelSplitHistoryart.eventYouAreSelected(false); // refresh auf
																	// das
				// gesamte 1:n
				// panel
			} else if (eI.getSource() == panelBottomErledigungsgrund) {
				panelSplitErledigungsgrund.eventYouAreSelected(false); // refresh
																		// auf
				// das
				// gesamte 1:n
				// panel
			} else if (eI.getSource() == panelBottomBereich) {
				panelSplitBereich.eventYouAreSelected(false); // refresh
				// auf
				// das
				// gesamte 1:n
				// panel
			}

		}

		else if (eI.getID() == ItemChangedEvent.ACTION_NEW) {
			// hier wird reingesprungen wenn new button gedrueckt wird (hier 1:n
			// panel)
			// New ...
			if (eI.getSource() == panelQueryKategorie) {
				if (panelQueryKategorie.getSelectedId() == null) {
					getInternalFrame().enableAllPanelsExcept(true);
				}
				panelBottomKategorie.eventActionNew(eI, true, false);
				panelBottomKategorie.eventYouAreSelected(false);
				setSelectedComponent(panelSplitKategorie);
			} else if (eI.getSource() == panelQueryProjekttyp) {
				if (panelQueryProjekttyp.getSelectedId() == null) {
					getInternalFrame().enableAllPanelsExcept(true);
				}
				panelBottomProjekttyp.eventActionNew(eI, true, false);
				panelBottomProjekttyp.eventYouAreSelected(false);
				setSelectedComponent(panelSplitProjekttyp);
			} else if (eI.getSource() == panelQueryStatus) {
				if (panelQueryStatus.getSelectedId() == null) {
					getInternalFrame().enableAllPanelsExcept(true);
				}
				panelBottomStatus.eventActionNew(eI, true, false);
				panelBottomStatus.eventYouAreSelected(false);
				setSelectedComponent(panelSplitStatus);
			} else if (eI.getSource() == panelQueryHistoryart) {
				if (panelQueryHistoryart.getSelectedId() == null) {
					getInternalFrame().enableAllPanelsExcept(true);
				}
				panelBottomHistoryart.eventActionNew(eI, true, false);
				panelBottomHistoryart.eventYouAreSelected(false);
				setSelectedComponent(panelSplitHistoryart);
			} else if (eI.getSource() == panelQueryErledigungsgrund) {
				if (panelQueryErledigungsgrund.getSelectedId() == null) {
					getInternalFrame().enableAllPanelsExcept(true);
				}
				panelBottomErledigungsgrund.eventActionNew(eI, true, false);
				panelBottomErledigungsgrund.eventYouAreSelected(false);
				setSelectedComponent(panelSplitErledigungsgrund);
			} else if (eI.getSource() == panelQueryBereich) {
				if (panelQueryBereich.getSelectedId() == null) {
					getInternalFrame().enableAllPanelsExcept(true);
				}
				panelBottomBereich.eventActionNew(eI, true, false);
				panelBottomBereich.eventYouAreSelected(false);
				setSelectedComponent(panelSplitBereich);
			}

		} else if (eI.getID() == ItemChangedEvent.ACTION_UPDATE) {
			// hier kommt man nach upd im D bei einem 1:n hin.
			if (eI.getSource() == panelBottomKategorie) {
				// im QP die Buttons in den Zustand neu setzen.
				panelQueryKategorie.updateButtons(new LockStateValue(
						PanelBasis.LOCK_FOR_NEW));

			} else if (eI.getSource() == panelBottomProjekttyp) {
				// im QP die Buttons in den Zustand neu setzen.
				panelQueryProjekttyp.updateButtons(new LockStateValue(
						PanelBasis.LOCK_FOR_NEW));
			} else if (eI.getSource() == panelBottomStatus) {
				// im QP die Buttons in den Zustand neu setzen.
				panelQueryStatus.updateButtons(new LockStateValue(
						PanelBasis.LOCK_FOR_NEW));
			} else if (eI.getSource() == panelBottomHistoryart) {
				// im QP die Buttons in den Zustand neu setzen.
				panelQueryHistoryart.updateButtons(new LockStateValue(
						PanelBasis.LOCK_FOR_NEW));
			} else if (eI.getSource() == panelBottomErledigungsgrund) {
				// im QP die Buttons in den Zustand neu setzen.
				panelQueryErledigungsgrund.updateButtons(new LockStateValue(
						PanelBasis.LOCK_FOR_NEW));
			} else if (eI.getSource() == panelBottomBereich) {
				// im QP die Buttons in den Zustand neu setzen.
				panelQueryBereich.updateButtons(new LockStateValue(
						PanelBasis.LOCK_FOR_NEW));
			}

		}

		else if (eI.getID() == ItemChangedEvent.ACTION_DELETE) {
			if (eI.getSource() == panelBottomKategorie) {
				panelSplitKategorie.eventYouAreSelected(false);
			} else if (eI.getSource() == panelBottomProjekttyp) {
				panelSplitProjekttyp.eventYouAreSelected(false);
			} else if (eI.getSource() == panelBottomStatus) {
				panelSplitStatus.eventYouAreSelected(false);
			} else if (eI.getSource() == panelBottomHistoryart) {
				panelSplitHistoryart.eventYouAreSelected(false);
			} else if (eI.getSource() == panelBottomErledigungsgrund) {
				panelSplitErledigungsgrund.eventYouAreSelected(false);
			} else if (eI.getSource() == panelBottomBereich) {
				panelSplitBereich.eventYouAreSelected(false);
			}

		} else if (eI.getID() == ItemChangedEvent.ACTION_POSITION_VONNNACHNMINUS1) {
			if (eI.getSource() == panelQueryBereich) {
				int iPos = panelQueryBereich.getTable().getSelectedRow();

				// wenn die Position nicht die erste ist
				if (iPos > 0) {
					Integer iIdPosition = (Integer) panelQueryBereich
							.getSelectedId();

					Integer iIdPositionMinus1 = (Integer) panelQueryBereich
							.getTable().getValueAt(iPos - 1, 0);

					DelegateFactory.getInstance().getProjektServiceDelegate()
							.vertauscheBereich(iIdPosition, iIdPositionMinus1);

					// die Liste neu anzeigen und den richtigen Datensatz
					// markieren
					panelQueryBereich.setSelectedId(iIdPosition);
				}
			}
		} else if (eI.getID() == ItemChangedEvent.ACTION_POSITION_VONNNACHNPLUS1) {
			if (eI.getSource() == panelQueryBereich) {
				int iPos = panelQueryBereich.getTable().getSelectedRow();

				// wenn die Position nicht die letzte ist
				if (iPos < panelQueryBereich.getTable().getRowCount() - 1) {
					Integer iIdPosition = (Integer) panelQueryBereich
							.getSelectedId();

					Integer iIdPositionPlus1 = (Integer) panelQueryBereich
							.getTable().getValueAt(iPos + 1, 0);

					DelegateFactory.getInstance().getProjektServiceDelegate()
							.vertauscheBereich(iIdPosition, iIdPositionPlus1);

					// die Liste neu anzeigen und den richtigen Datensatz
					// markieren
					panelQueryBereich.setSelectedId(iIdPosition);
				}
			}
		}

	}

	/**
	 * getJMenuBar
	 * 
	 * @return JMenuBar
	 * @throws Throwable
	 */

	protected javax.swing.JMenuBar getJMenuBar() throws Throwable {
		return new WrapperMenuBar(this);
	}

	/**
	 * Behandle ActionEvent; zB Menue-Klick oben.
	 * 
	 * @param e
	 *            ActionEvent
	 * @throws Throwable
	 */
	protected void lPActionEvent(ActionEvent e) throws Throwable {
	}

	/**
	 * eventStateChanged
	 * 
	 * @param e
	 *            ChangeEvent
	 * @throws Throwable
	 */
	public void lPEventObjectChanged(ChangeEvent e) throws Throwable {
		super.lPEventObjectChanged(e);
		int selectedIndex = this.getSelectedIndex();

		switch (selectedIndex) {
		case IDX_PANEL_KATEGORIE: {
			refreshPanelKategorie();
			panelQueryKategorie.eventYouAreSelected(false);

			// im QP die Buttons setzen.
			panelQueryKategorie.updateButtons();
			break;
		}
		case IDX_PANEL_PROJEKTTYP: {
			refreshPanelTyp();
			panelQueryProjekttyp.eventYouAreSelected(false);

			// im QP die Buttons setzen.
			panelQueryProjekttyp.updateButtons();
			break;
		}
		case IDX_PANEL_STATUS: {
			refreshPanelStatus();
			panelQueryStatus.eventYouAreSelected(false);

			// im QP die Buttons setzen.
			panelQueryStatus.updateButtons();
			break;
		}
		case IDX_PANEL_HISTORYART: {
			refreshPanelStatus();
			panelQueryHistoryart.eventYouAreSelected(false);

			// im QP die Buttons setzen.
			panelQueryHistoryart.updateButtons();
			break;
		}
		case IDX_PANEL_ERLEDIGUNGSGRUND: {
			refreshPanelErledigungsgrund();
			panelQueryErledigungsgrund.eventYouAreSelected(false);

			// im QP die Buttons setzen.
			panelQueryErledigungsgrund.updateButtons();
			break;
		}
		case IDX_PANEL_BEREICH: {
			refreshPanelBereich();
			panelQueryBereich.eventYouAreSelected(false);

			// im QP die Buttons setzen.
			panelQueryBereich.updateButtons();
			break;
		}

		}
	}

}
