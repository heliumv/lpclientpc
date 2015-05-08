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
package com.lp.client.reklamation;

import javax.swing.event.ChangeEvent;

import com.lp.client.frame.LockStateValue;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQuery;
import com.lp.client.frame.component.PanelSplit;
import com.lp.client.frame.component.TabbedPane;
import com.lp.client.frame.component.WrapperMenuBar;
import com.lp.client.pc.LPMain;
import com.lp.client.stueckliste.InternalFrameStueckliste;
import com.lp.client.stueckliste.StuecklisteFilterFactory;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;

@SuppressWarnings("static-access")
/*
 * <p>&UUml;berschrift: </p> <p>Beschreibung: </p> <p>Copyright: Copyright (c)
 * 2004</p> <p>Organisation: </p>
 * 
 * @author Christian Kollmann
 * 
 * @version $Revision: 1.10 $
 */
public class TabbedPaneReklamationGrunddaten extends TabbedPane {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private PanelQuery panelQueryFehler = null;
	private PanelBasis panelSplitFehler = null;
	private PanelBasis panelBottomFehler = null;

	private PanelQuery panelQueryFehlerangabe = null;
	private PanelBasis panelSplitFehlerangabe = null;
	private PanelBasis panelBottomFehlerangabe = null;

	private PanelQuery panelQueryMassnahme = null;
	private PanelBasis panelSplitMassnahme = null;
	private PanelBasis panelBottomMassnahme = null;

	private PanelQuery panelQueryAufnahmeart = null;
	private PanelBasis panelSplitAufnahmeart = null;
	private PanelBasis panelBottomAufnahmeart = null;

	private PanelQuery panelQuerySchwere = null;
	private PanelBasis panelSplitSchwere = null;
	private PanelBasis panelBottomSchwere = null;

	private PanelQuery panelQueryBeurteilung = null;
	private PanelBasis panelSplitBeurteilung = null;
	private PanelBasis panelBottomBeurteilung = null;

	private PanelQuery panelQueryTermintreue = null;
	private PanelBasis panelSplitTermintreue = null;
	private PanelBasis panelBottomTermintreue = null;

	private PanelQuery panelQueryWirksamkeit = null;
	private PanelBasis panelSplitWirksamkeit = null;
	private PanelBasis panelBottomWirksamkeit = null;

	private static int IDX_PANEL_FEHLER = 0;
	private static int IDX_PANEL_FEHLERANGABE = 1;
	private static int IDX_PANEL_MASSNAHME = 2;
	private static int IDX_PANEL_AUFNAHMEART = 3;
	private static int IDX_PANEL_SCHWERE = 4;
	private static int IDX_PANEL_BEURTEILUNG = 5;
	private static int IDX_PANEL_TERMINTREUE = 6;
	private static int IDX_PANEL_WIRKSAMKEIT = 7;

	private WrapperMenuBar wrapperMenuBar = null;

	public TabbedPaneReklamationGrunddaten(InternalFrame internalFrameI)
			throws Throwable {
		super(internalFrameI, LPMain.getInstance().getTextRespectUISPr(
				"pers.title.tab.grunddaten"));

		jbInit();
		initComponents();
	}

	public InternalFrameStueckliste getInternalFrameStueckliste() {
		return (InternalFrameStueckliste) getInternalFrame();
	}

	private void createFehler() throws Throwable {
		if (panelSplitFehler == null) {
			String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW };
			panelQueryFehler = new PanelQuery(null, null,
					QueryParameters.UC_ID_FEHLER, aWhichButtonIUse,
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("rekla.fehler"), true);

			panelBottomFehler = new PanelFehler(getInternalFrame(), LPMain
					.getInstance().getTextRespectUISPr("rekla.fehler"), null);
			panelSplitFehler = new PanelSplit(getInternalFrame(),
					panelBottomFehler, panelQueryFehler, 350);

			panelQueryFehler.befuellePanelFilterkriterienDirekt(
					ReklamationFilterFactory.getInstance()
							.createFKDBezeichnungMitAlias("fehler"), null);

			setComponentAt(IDX_PANEL_FEHLER, panelSplitFehler);
		}
	}

	private void createSchwere() throws Throwable {
		if (panelSplitSchwere == null) {
			String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW };
			panelQuerySchwere = new PanelQuery(null, null,
					QueryParameters.UC_ID_SCHWERE, aWhichButtonIUse,
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("rekla.schwere"), true);

			panelBottomSchwere = new PanelSchwere(getInternalFrame(), LPMain
					.getInstance().getTextRespectUISPr("rekla.schwere"), null);
			panelSplitSchwere = new PanelSplit(getInternalFrame(),
					panelBottomSchwere, panelQuerySchwere, 350);

			panelQuerySchwere.befuellePanelFilterkriterienDirekt(
					StuecklisteFilterFactory.getInstance()
							.createFKDBezeichnungAllgemein(), null);

			setComponentAt(IDX_PANEL_SCHWERE, panelSplitSchwere);
		}
	}
	private void createTermintreue() throws Throwable {
		if (panelSplitTermintreue == null) {
			String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW };
			panelQueryTermintreue = new PanelQuery(null, null,
					QueryParameters.UC_ID_TERMINTREUE, aWhichButtonIUse,
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("rekla.termintreue"), true);

			panelBottomTermintreue = new PanelTermintreue(getInternalFrame(), LPMain
					.getInstance().getTextRespectUISPr("rekla.termintreue"), null);
			panelSplitTermintreue = new PanelSplit(getInternalFrame(),
					panelBottomTermintreue, panelQueryTermintreue, 350);


			setComponentAt(IDX_PANEL_TERMINTREUE, panelSplitTermintreue);
		}
	}

	private void createWirksamkeit() throws Throwable {
		if (panelSplitWirksamkeit == null) {
			String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW };
			panelQueryWirksamkeit = new PanelQuery(null, null,
					QueryParameters.UC_ID_WIRKSAMKEIT, aWhichButtonIUse,
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("rekla.wirksamkeit"), true);

			panelBottomWirksamkeit = new PanelWirksamkeit(getInternalFrame(), LPMain
					.getInstance().getTextRespectUISPr("rekla.wirksamkeit"), null);
			panelSplitWirksamkeit = new PanelSplit(getInternalFrame(),
					panelBottomWirksamkeit, panelQueryWirksamkeit, 350);
			panelQueryWirksamkeit.befuellePanelFilterkriterienDirekt(
					ReklamationFilterFactory.getInstance()
					.createFKDBezeichnungMitAlias("wirksamkeit"), null);

			setComponentAt(IDX_PANEL_WIRKSAMKEIT, panelSplitWirksamkeit);
		}
	}

	private void createBehandlung() throws Throwable {
		if (panelSplitBeurteilung == null) {
			String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW };
			panelQueryBeurteilung = new PanelQuery(null, null,
					QueryParameters.UC_ID_BEHANDLUNG, aWhichButtonIUse,
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("rekla.behandlung"), true);

			panelBottomBeurteilung = new PanelBehandlung(getInternalFrame(),
					LPMain.getInstance().getTextRespectUISPr("rekla.behandlung"),
					null);
			panelSplitBeurteilung = new PanelSplit(getInternalFrame(),
					panelBottomBeurteilung, panelQueryBeurteilung, 350);

			panelQueryBeurteilung.befuellePanelFilterkriterienDirekt(
					StuecklisteFilterFactory.getInstance()
							.createFKDBezeichnungAllgemein(), null);

			setComponentAt(IDX_PANEL_BEURTEILUNG, panelSplitBeurteilung);
		}
	}

	private void createMassnahme() throws Throwable {
		if (panelSplitMassnahme == null) {
			String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW };
			panelQueryMassnahme = new PanelQuery(null, null,
					QueryParameters.UC_ID_MASSNAHME, aWhichButtonIUse,
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("rekla.massnahme"), true);

			panelBottomMassnahme = new PanelMassnahme(
					getInternalFrame(),
					LPMain.getInstance().getTextRespectUISPr("rekla.massnahme"),
					null);
			panelSplitMassnahme = new PanelSplit(getInternalFrame(),
					panelBottomMassnahme, panelQueryMassnahme, 350);
			panelQueryMassnahme.befuellePanelFilterkriterienDirekt(
					ReklamationFilterFactory.getInstance()
					.createFKDBezeichnungMitAlias("massnahme"), null);

			setComponentAt(IDX_PANEL_MASSNAHME, panelSplitMassnahme);
		}
	}

	private void createAufnahmeart() throws Throwable {
		if (panelSplitAufnahmeart == null) {
			String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW };
			panelQueryAufnahmeart = new PanelQuery(null, null,
					QueryParameters.UC_ID_AUFNAHMEART, aWhichButtonIUse,
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("rekla.aufnahmeart"), true);

			panelBottomAufnahmeart = new PanelAufnahmeart(getInternalFrame(),
					LPMain.getInstance().getTextRespectUISPr(
							"rekla.aufnahmeart"), null);
			panelSplitAufnahmeart = new PanelSplit(getInternalFrame(),
					panelBottomAufnahmeart, panelQueryAufnahmeart, 350);

			panelQueryAufnahmeart.befuellePanelFilterkriterienDirekt(
					ReklamationFilterFactory.getInstance()
					.createFKDBezeichnungMitAlias("aufnahmeart"), null);

			setComponentAt(IDX_PANEL_AUFNAHMEART, panelSplitAufnahmeart);
		}
	}

	private void createFehlerangabe() throws Throwable {
		if (panelSplitFehlerangabe == null) {
			String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW };

			panelQueryFehlerangabe = new PanelQuery(null, null,
					QueryParameters.UC_ID_FEHLERANGABE, aWhichButtonIUse,
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("rekla.fehlerangabe"), true);
			panelBottomFehlerangabe = new PanelFehlerangabe(getInternalFrame(),
					LPMain.getInstance().getTextRespectUISPr(
							"rekla.fehlerangabe"), null);

			panelSplitFehlerangabe = new PanelSplit(getInternalFrame(),
					panelBottomFehlerangabe, panelQueryFehlerangabe, 350);
			panelQueryFehlerangabe.befuellePanelFilterkriterienDirekt(
					ReklamationFilterFactory.getInstance()
					.createFKDBezeichnungMitAlias("fehlerangabe"), null);

			setComponentAt(IDX_PANEL_FEHLERANGABE, panelSplitFehlerangabe);
		}
	}

	private void jbInit() throws Throwable {
		// Kollektiv

		// 1 tab oben: QP1 PartnerFLR; lazy loading
		insertTab(LPMain.getInstance().getTextRespectUISPr("rekla.fehler"),
				null, null, LPMain.getInstance().getTextRespectUISPr(
						"rekla.fehler"), IDX_PANEL_FEHLER);

		insertTab(LPMain.getInstance()
				.getTextRespectUISPr("rekla.fehlerangabe"), null, null, LPMain
				.getInstance().getTextRespectUISPr("rekla.fehlerangabe"),
				IDX_PANEL_FEHLERANGABE);
		insertTab(LPMain.getInstance().getTextRespectUISPr("rekla.massnahme"),
				null, null, LPMain.getInstance().getTextRespectUISPr(
						"rekla.massnahme"), IDX_PANEL_MASSNAHME);
		insertTab(
				LPMain.getInstance().getTextRespectUISPr("rekla.aufnahmeart"),
				null, null, LPMain.getInstance().getTextRespectUISPr(
						"rekla.aufnahmeart"), IDX_PANEL_AUFNAHMEART);
		insertTab(LPMain.getInstance().getTextRespectUISPr("rekla.schwere"),
				null, null, LPMain.getInstance().getTextRespectUISPr(
						"rekla.schwere"), IDX_PANEL_SCHWERE);
		insertTab(LPMain.getInstance().getTextRespectUISPr("rekla.behandlung"),
				null, null, LPMain.getInstance().getTextRespectUISPr(
						"rekla.behandlung"), IDX_PANEL_BEURTEILUNG);
		insertTab(LPMain.getInstance().getTextRespectUISPr("rekla.termintreue"),
				null, null, LPMain.getInstance().getTextRespectUISPr(
						"rekla.termintreue"), IDX_PANEL_TERMINTREUE);
		insertTab(LPMain.getInstance().getTextRespectUISPr("rekla.wirksamkeit"),
				null, null, LPMain.getInstance().getTextRespectUISPr(
						"rekla.wirksamkeit"), IDX_PANEL_WIRKSAMKEIT);
		createFehler();

		// Itemevents an MEIN Detailpanel senden kann.
		refreshTitle();
		this.addChangeListener(this);
		getInternalFrame().addItemChangedListener(this);

	}

	public void lPEventItemChanged(ItemChangedEvent e) throws Throwable {

		if (e.getID() == ItemChangedEvent.ITEM_CHANGED) {
			if (e.getSource() == panelQueryFehler) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				panelBottomFehler.setKeyWhenDetailPanel(key);
				panelBottomFehler.eventYouAreSelected(false);
				panelQueryFehler.updateButtons();
			} else if (e.getSource() == panelQueryFehlerangabe) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				panelBottomFehlerangabe.setKeyWhenDetailPanel(key);
				panelBottomFehlerangabe.eventYouAreSelected(false);
				panelQueryFehlerangabe.updateButtons();
			} else if (e.getSource() == panelQueryMassnahme) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				panelBottomMassnahme.setKeyWhenDetailPanel(key);
				panelBottomMassnahme.eventYouAreSelected(false);
				panelQueryMassnahme.updateButtons();
			} else if (e.getSource() == panelQueryAufnahmeart) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				panelBottomAufnahmeart.setKeyWhenDetailPanel(key);
				panelBottomAufnahmeart.eventYouAreSelected(false);
				panelQueryAufnahmeart.updateButtons();
			} else if (e.getSource() == panelQuerySchwere) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				panelBottomSchwere.setKeyWhenDetailPanel(key);
				panelBottomSchwere.eventYouAreSelected(false);
				panelQuerySchwere.updateButtons();
			} else if (e.getSource() == panelQueryBeurteilung) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				panelBottomBeurteilung.setKeyWhenDetailPanel(key);
				panelBottomBeurteilung.eventYouAreSelected(false);
				panelQueryBeurteilung.updateButtons();
			} else if (e.getSource() == panelQueryTermintreue) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				panelBottomTermintreue.setKeyWhenDetailPanel(key);
				panelBottomTermintreue.eventYouAreSelected(false);
				panelQueryTermintreue.updateButtons();
			}else if (e.getSource() == panelQueryWirksamkeit) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				panelBottomWirksamkeit.setKeyWhenDetailPanel(key);
				panelBottomWirksamkeit.eventYouAreSelected(false);
				panelQueryWirksamkeit.updateButtons();
			}

		} else if (e.getID() == ItemChangedEvent.ACTION_YOU_ARE_SELECTED) {
			refreshTitle();
			panelSplitFehler.eventYouAreSelected(false);
		} else if (e.getID() == ItemChangedEvent.ACTION_UPDATE) {
			if (e.getSource() == panelBottomFehler) {
				panelQueryFehler.updateButtons(new LockStateValue(
						PanelBasis.LOCK_FOR_NEW));
				;
			} else if (e.getSource() == panelBottomFehlerangabe) {
				panelQueryFehlerangabe.updateButtons(new LockStateValue(
						PanelBasis.LOCK_FOR_NEW));
				;
			} else if (e.getSource() == panelBottomMassnahme) {
				panelQueryMassnahme.updateButtons(new LockStateValue(
						PanelBasis.LOCK_FOR_NEW));
				;
			} else if (e.getSource() == panelBottomAufnahmeart) {
				panelQueryAufnahmeart.updateButtons(new LockStateValue(
						PanelBasis.LOCK_FOR_NEW));
				;
			} else if (e.getSource() == panelBottomSchwere) {
				panelQuerySchwere.updateButtons(new LockStateValue(
						PanelBasis.LOCK_FOR_NEW));
				;
			} else if (e.getSource() == panelBottomBeurteilung) {
				panelQueryBeurteilung.updateButtons(new LockStateValue(
						PanelBasis.LOCK_FOR_NEW));
				;
			}else if (e.getSource() == panelBottomTermintreue) {
				panelQueryTermintreue.updateButtons(new LockStateValue(
						PanelBasis.LOCK_FOR_NEW));
				;
			}else if (e.getSource() == panelBottomWirksamkeit) {
				panelQueryWirksamkeit.updateButtons(new LockStateValue(
						PanelBasis.LOCK_FOR_NEW));
				;
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_POSITION_VONNNACHNMINUS1) {

		} else if (e.getID() == ItemChangedEvent.ACTION_POSITION_VONNNACHNPLUS1) {

		} else if (e.getID() == ItemChangedEvent.ACTION_POSITION_VORPOSITIONEINFUEGEN) {
		}

		else if (e.getID() == ItemChangedEvent.ACTION_NEW) {
			if (e.getSource() == panelQueryFehler) {
				panelBottomFehler.eventActionNew(e, true, false);
				panelBottomFehler.eventYouAreSelected(false);
			}

			else if (e.getSource() == panelQueryFehlerangabe) {
				panelBottomFehlerangabe.eventActionNew(e, true, false);
				panelBottomFehlerangabe.eventYouAreSelected(false);
			} else if (e.getSource() == panelQueryMassnahme) {
				panelBottomMassnahme.eventActionNew(e, true, false);
				panelBottomMassnahme.eventYouAreSelected(false);
			} else if (e.getSource() == panelQueryAufnahmeart) {
				panelBottomAufnahmeart.eventActionNew(e, true, false);
				panelBottomAufnahmeart.eventYouAreSelected(false);
			} else if (e.getSource() == panelQuerySchwere) {
				panelBottomSchwere.eventActionNew(e, true, false);
				panelBottomSchwere.eventYouAreSelected(false);
			} else if (e.getSource() == panelQueryBeurteilung) {
				panelBottomBeurteilung.eventActionNew(e, true, false);
				panelBottomBeurteilung.eventYouAreSelected(false);
			}else if (e.getSource() == panelQueryTermintreue) {
				panelBottomTermintreue.eventActionNew(e, true, false);
				panelBottomTermintreue.eventYouAreSelected(false);
			}else if (e.getSource() == panelQueryWirksamkeit) {
				panelBottomWirksamkeit.eventActionNew(e, true, false);
				panelBottomWirksamkeit.eventYouAreSelected(false);
			}

		} else if (e.getID() == ItemChangedEvent.ACTION_DISCARD) {
			if (e.getSource() == panelBottomFehler) {
				panelSplitFehler.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomFehlerangabe) {
				panelSplitFehlerangabe.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomMassnahme) {
				panelSplitMassnahme.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomAufnahmeart) {
				panelSplitAufnahmeart.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomSchwere) {
				panelSplitSchwere.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomBeurteilung) {
				panelSplitBeurteilung.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomTermintreue) {
				panelSplitTermintreue.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomWirksamkeit) {
				panelSplitWirksamkeit.eventYouAreSelected(false);
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_SAVE) {
			if (e.getSource() == panelBottomFehler) {
				Object oKey = panelBottomFehler.getKeyWhenDetailPanel();
				panelQueryFehler.eventYouAreSelected(false);
				panelQueryFehler.setSelectedId(oKey);
				panelSplitFehler.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomFehlerangabe) {
				Object oKey = panelBottomFehlerangabe.getKeyWhenDetailPanel();
				panelQueryFehlerangabe.eventYouAreSelected(false);
				panelQueryFehlerangabe.setSelectedId(oKey);
				panelSplitFehlerangabe.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomMassnahme) {
				Object oKey = panelBottomMassnahme.getKeyWhenDetailPanel();
				panelQueryMassnahme.eventYouAreSelected(false);
				panelQueryMassnahme.setSelectedId(oKey);
				panelSplitMassnahme.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomAufnahmeart) {
				Object oKey = panelBottomAufnahmeart.getKeyWhenDetailPanel();
				panelQueryAufnahmeart.eventYouAreSelected(false);
				panelQueryAufnahmeart.setSelectedId(oKey);
				panelSplitAufnahmeart.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomSchwere) {
				Object oKey = panelBottomSchwere.getKeyWhenDetailPanel();
				panelQuerySchwere.eventYouAreSelected(false);
				panelQuerySchwere.setSelectedId(oKey);
				panelSplitSchwere.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomBeurteilung) {
				Object oKey = panelBottomBeurteilung.getKeyWhenDetailPanel();
				panelQueryBeurteilung.eventYouAreSelected(false);
				panelQueryBeurteilung.setSelectedId(oKey);
				panelSplitBeurteilung.eventYouAreSelected(false);
			}else if (e.getSource() == panelBottomTermintreue) {
				Object oKey = panelBottomTermintreue.getKeyWhenDetailPanel();
				panelQueryTermintreue.eventYouAreSelected(false);
				panelQueryTermintreue.setSelectedId(oKey);
				panelSplitTermintreue.eventYouAreSelected(false);
			}else if (e.getSource() == panelBottomWirksamkeit) {
				Object oKey = panelBottomWirksamkeit.getKeyWhenDetailPanel();
				panelQueryWirksamkeit.eventYouAreSelected(false);
				panelQueryWirksamkeit.setSelectedId(oKey);
				panelSplitWirksamkeit.eventYouAreSelected(false);
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_GOTO_MY_DEFAULT_QP) {
			if (e.getSource() == panelBottomFehler) {
				Object oKey = panelQueryFehler.getSelectedId();
				if (oKey != null) {
					getInternalFrame().setKeyWasForLockMe(oKey.toString());
				} else {
					getInternalFrame().setKeyWasForLockMe(null);
				}
				if (panelBottomFehler.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryFehler
							.getId2SelectAfterDelete();
					panelQueryFehler.setSelectedId(oNaechster);
				}
				panelSplitFehler.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomFehlerangabe) {
				Object oKey = panelQueryFehlerangabe.getSelectedId();
				if (oKey != null) {
					getInternalFrame().setKeyWasForLockMe(oKey.toString());
				} else {
					getInternalFrame().setKeyWasForLockMe(null);
				}
				if (panelBottomFehlerangabe.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryFehlerangabe
							.getId2SelectAfterDelete();
					panelQueryFehlerangabe.setSelectedId(oNaechster);
				}

				panelSplitFehlerangabe.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomMassnahme) {
				Object oKey = panelQueryMassnahme.getSelectedId();
				if (oKey != null) {
					getInternalFrame().setKeyWasForLockMe(oKey.toString());
				} else {
					getInternalFrame().setKeyWasForLockMe(null);
				}
				if (panelBottomMassnahme.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryMassnahme
							.getId2SelectAfterDelete();
					panelQueryMassnahme.setSelectedId(oNaechster);
				}

				panelSplitMassnahme.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomAufnahmeart) {
				Object oKey = panelQueryAufnahmeart.getSelectedId();
				if (oKey != null) {
					getInternalFrame().setKeyWasForLockMe(oKey.toString());
				} else {
					getInternalFrame().setKeyWasForLockMe(null);
				}
				if (panelBottomAufnahmeart.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryAufnahmeart
							.getId2SelectAfterDelete();
					panelQueryAufnahmeart.setSelectedId(oNaechster);
				}

				panelSplitAufnahmeart.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomSchwere) {
				Object oKey = panelQuerySchwere.getSelectedId();
				if (oKey != null) {
					getInternalFrame().setKeyWasForLockMe(oKey.toString());
				} else {
					getInternalFrame().setKeyWasForLockMe(null);
				}
				if (panelBottomSchwere.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQuerySchwere
							.getId2SelectAfterDelete();
					panelQuerySchwere.setSelectedId(oNaechster);
				}

				panelSplitSchwere.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomBeurteilung) {
				Object oKey = panelQueryBeurteilung.getSelectedId();
				if (oKey != null) {
					getInternalFrame().setKeyWasForLockMe(oKey.toString());
				} else {
					getInternalFrame().setKeyWasForLockMe(null);
				}
				if (panelBottomBeurteilung.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryBeurteilung
							.getId2SelectAfterDelete();
					panelQueryBeurteilung.setSelectedId(oNaechster);
				}

				panelSplitBeurteilung.eventYouAreSelected(false);
			}else if (e.getSource() == panelBottomTermintreue) {
				Object oKey = panelQueryTermintreue.getSelectedId();
				if (oKey != null) {
					getInternalFrame().setKeyWasForLockMe(oKey.toString());
				} else {
					getInternalFrame().setKeyWasForLockMe(null);
				}
				if (panelBottomTermintreue.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryTermintreue
							.getId2SelectAfterDelete();
					panelQueryTermintreue.setSelectedId(oNaechster);
				}

				panelSplitTermintreue.eventYouAreSelected(false);
			}else if (e.getSource() == panelBottomWirksamkeit) {
				Object oKey = panelQueryWirksamkeit.getSelectedId();
				if (oKey != null) {
					getInternalFrame().setKeyWasForLockMe(oKey.toString());
				} else {
					getInternalFrame().setKeyWasForLockMe(null);
				}
				if (panelBottomWirksamkeit.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryWirksamkeit
							.getId2SelectAfterDelete();
					panelQueryWirksamkeit.setSelectedId(oNaechster);
				}

				panelSplitWirksamkeit.eventYouAreSelected(false);
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

		if (selectedIndex == IDX_PANEL_FEHLER) {
			createFehler();
			panelSplitFehler.eventYouAreSelected(false);
			panelQueryFehler.updateButtons();
		}

		else if (selectedIndex == IDX_PANEL_FEHLERANGABE) {
			createFehlerangabe();
			panelSplitFehlerangabe.eventYouAreSelected(false);
			panelQueryFehlerangabe.updateButtons();
		} else if (selectedIndex == IDX_PANEL_MASSNAHME) {
			createMassnahme();
			panelSplitMassnahme.eventYouAreSelected(false);
			panelQueryMassnahme.updateButtons();
		} else if (selectedIndex == IDX_PANEL_AUFNAHMEART) {
			createAufnahmeart();
			panelSplitAufnahmeart.eventYouAreSelected(false);
			panelQueryAufnahmeart.updateButtons();
		} else if (selectedIndex == IDX_PANEL_SCHWERE) {
			createSchwere();
			panelSplitSchwere.eventYouAreSelected(false);
			panelQuerySchwere.updateButtons();
		} else if (selectedIndex == IDX_PANEL_BEURTEILUNG) {
			createBehandlung();
			panelSplitBeurteilung.eventYouAreSelected(false);
		}else if (selectedIndex == IDX_PANEL_TERMINTREUE) {
			createTermintreue();
			panelSplitTermintreue.eventYouAreSelected(false);
		}else if (selectedIndex == IDX_PANEL_WIRKSAMKEIT) {
			createWirksamkeit();
			panelSplitWirksamkeit.eventYouAreSelected(false);
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
