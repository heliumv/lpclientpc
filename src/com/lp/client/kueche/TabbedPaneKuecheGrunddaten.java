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
package com.lp.client.kueche;

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
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.reklamation.PanelFehler;
import com.lp.client.reklamation.PanelFehlerangabe;
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
 * @version $Revision: 1.5 $
 */
public class TabbedPaneKuecheGrunddaten extends TabbedPane {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private PanelQuery panelQueryKassaartikel = null;
	private PanelBasis panelSplitKassaartikel = null;
	private PanelBasis panelBottomKassaartikel = null;

	private PanelQuery panelQueryTageslos = null;
	private PanelBasis panelSplitTageslos = null;
	private PanelBasis panelBottomTageslos = null;

	private PanelQuery panelQueryUmrechnung = null;
	private PanelBasis panelSplitUmrechnung = null;
	private PanelBasis panelBottomUmrechnung = null;
	
	private PanelQuery panelQueryBedienerlager = null;
	private PanelBasis panelSplitBedienerlager = null;
	private PanelBasis panelBottomBedienerlager = null;

	private static int IDX_PANEL_KASSAARTIKEL = 0;
	private static int IDX_PANEL_TAGESLOS = 1;
	private static int IDX_PANEL_KUECHEUMRECHNUNG = 2;
	private static int IDX_PANEL_BEDIENERLAGER = 3;

	private WrapperMenuBar wrapperMenuBar = null;

	public TabbedPaneKuecheGrunddaten(InternalFrame internalFrameI)
			throws Throwable {
		super(internalFrameI, LPMain.getInstance().getTextRespectUISPr(
				"pers.title.tab.grunddaten"));

		jbInit();
		initComponents();
	}

	public InternalFrameStueckliste getInternalFrameStueckliste() {
		return (InternalFrameStueckliste) getInternalFrame();
	}

	private void createKassaartikel() throws Throwable {
		if (panelSplitKassaartikel == null) {
			String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW,
					PanelBasis.ACTION_POSITION_VONNNACHNMINUS1,
					PanelBasis.ACTION_POSITION_VONNNACHNPLUS1 };
			panelQueryKassaartikel = new PanelQuery(null, null,
					QueryParameters.UC_ID_KASSAARTIKEL, aWhichButtonIUse,
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("kue.kassaartikel"), true);

			panelBottomKassaartikel = new PanelKassaartikel(getInternalFrame(),
					LPMain.getInstance()
							.getTextRespectUISPr("kue.kassaartikel"), null);
			panelSplitKassaartikel = new PanelSplit(getInternalFrame(),
					panelBottomKassaartikel, panelQueryKassaartikel, 350);

			panelQueryKassaartikel.befuellePanelFilterkriterienDirekt(
					StuecklisteFilterFactory.getInstance()
							.createFKDBezeichnungAllgemein(), null);

			setComponentAt(IDX_PANEL_KASSAARTIKEL, panelSplitKassaartikel);
		}
	}

	private void createUmrechnung() throws Throwable {
		if (panelSplitUmrechnung == null) {
			String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW };
			panelQueryUmrechnung = new PanelQuery(null, null,
					QueryParameters.UC_ID_KUECHEUMRECHNUNG, aWhichButtonIUse,
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("kue.umrechnung"), true);

			panelBottomUmrechnung = new PanelKuecheumrechnung(getInternalFrame(),
					LPMain.getInstance().getTextRespectUISPr("kue.umrechnung"),
					null);
			panelSplitUmrechnung = new PanelSplit(getInternalFrame(),
					panelBottomUmrechnung, panelQueryUmrechnung, 350);

			panelQueryUmrechnung.befuellePanelFilterkriterienDirekt(
					StuecklisteFilterFactory.getInstance()
							.createFKDBezeichnungAllgemein(), null);

			setComponentAt(IDX_PANEL_KUECHEUMRECHNUNG, panelSplitUmrechnung);
		}
	}

	
	private void createBedienerlager() throws Throwable {
		if (panelSplitBedienerlager == null) {
			String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW };
			panelQueryBedienerlager = new PanelQuery(null, null,
					QueryParameters.UC_ID_BEDIENERLAGER, aWhichButtonIUse,
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("kue.bedienerlager"), true);

			panelBottomBedienerlager = new PanelBedienerlager(getInternalFrame(),
					LPMain.getInstance().getTextRespectUISPr("kue.bedienerlager"),
					null);
			panelSplitBedienerlager = new PanelSplit(getInternalFrame(),
					panelBottomBedienerlager, panelQueryBedienerlager, 350);


			setComponentAt(IDX_PANEL_BEDIENERLAGER, panelSplitBedienerlager);
		}
	}

	
	private void createTageslos() throws Throwable {
		if (panelSplitTageslos == null) {
			String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW };

			panelQueryTageslos = new PanelQuery(null, null,
					QueryParameters.UC_ID_TAGESLOS, aWhichButtonIUse,
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("kue.tageslos"), true);
			panelBottomTageslos = new PanelTageslos(getInternalFrame(), LPMain
					.getInstance().getTextRespectUISPr("kue.tageslos"), null);

			panelSplitTageslos = new PanelSplit(getInternalFrame(),
					panelBottomTageslos, panelQueryTageslos, 300);
			panelQueryTageslos.befuellePanelFilterkriterienDirekt(
					StuecklisteFilterFactory.getInstance()
							.createFKDBezeichnungAllgemein(), null);

			setComponentAt(IDX_PANEL_TAGESLOS, panelSplitTageslos);
		}
	}

	private void jbInit() throws Throwable {
		// Kollektiv

		// 1 tab oben: QP1 PartnerFLR; lazy loading
		insertTab(LPMain.getInstance().getTextRespectUISPr("kue.kassaartikel"),
				null, null, LPMain.getInstance().getTextRespectUISPr(
						"kue.kassaartikel"), IDX_PANEL_KASSAARTIKEL);

		insertTab(LPMain.getInstance().getTextRespectUISPr("kue.tageslos"),
				null, null, LPMain.getInstance().getTextRespectUISPr(
						"kue.tageslos"), IDX_PANEL_TAGESLOS);
		insertTab(LPMain.getInstance().getTextRespectUISPr("kue.umrechnung"),
				null, null, LPMain.getInstance().getTextRespectUISPr(
						"kue.tageslos"), IDX_PANEL_KUECHEUMRECHNUNG);
		insertTab(LPMain.getInstance().getTextRespectUISPr("kue.bedienerlager"),
				null, null, LPMain.getInstance().getTextRespectUISPr(
						"kue.bedienerlager"), IDX_PANEL_BEDIENERLAGER);

		createKassaartikel();

		// Itemevents an MEIN Detailpanel senden kann.
		refreshTitle();
		this.addChangeListener(this);
		getInternalFrame().addItemChangedListener(this);

	}

	public void lPEventItemChanged(ItemChangedEvent e) throws Throwable {

		if (e.getID() == ItemChangedEvent.ITEM_CHANGED) {
			if (e.getSource() == panelQueryKassaartikel) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				panelBottomKassaartikel.setKeyWhenDetailPanel(key);
				panelBottomKassaartikel.eventYouAreSelected(false);
				panelQueryKassaartikel.updateButtons();
			} else if (e.getSource() == panelQueryTageslos) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				panelBottomTageslos.setKeyWhenDetailPanel(key);
				panelBottomTageslos.eventYouAreSelected(false);
				panelQueryTageslos.updateButtons();
			} else if (e.getSource() == panelQueryUmrechnung) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				panelBottomUmrechnung.setKeyWhenDetailPanel(key);
				panelBottomUmrechnung.eventYouAreSelected(false);
				panelQueryUmrechnung.updateButtons();
			}else if (e.getSource() == panelQueryBedienerlager) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				panelBottomBedienerlager.setKeyWhenDetailPanel(key);
				panelBottomBedienerlager.eventYouAreSelected(false);
				panelQueryBedienerlager.updateButtons();
			}

		} else if (e.getID() == ItemChangedEvent.ACTION_YOU_ARE_SELECTED) {
			refreshTitle();
			panelSplitKassaartikel.eventYouAreSelected(false);
		} else if (e.getID() == ItemChangedEvent.ACTION_UPDATE) {
			if (e.getSource() == panelBottomKassaartikel) {
				panelQueryKassaartikel.updateButtons(new LockStateValue(
						PanelBasis.LOCK_FOR_NEW));
			} else if (e.getSource() == panelBottomTageslos) {
				panelQueryTageslos.updateButtons(new LockStateValue(
						PanelBasis.LOCK_FOR_NEW));
			} else if (e.getSource() == panelBottomUmrechnung) {
				panelQueryUmrechnung.updateButtons(new LockStateValue(
						PanelBasis.LOCK_FOR_NEW));
			}else if (e.getSource() == panelBottomBedienerlager) {
				panelQueryBedienerlager.updateButtons(new LockStateValue(
						PanelBasis.LOCK_FOR_NEW));
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_POSITION_VONNNACHNMINUS1) {
			int iPos = panelQueryKassaartikel.getTable().getSelectedRow();

			// wenn die Position nicht die erste ist
			if (iPos > 0) {
				Integer iIdPosition = (Integer) panelQueryKassaartikel
						.getSelectedId();

				Integer iIdPositionMinus1 = (Integer) panelQueryKassaartikel
						.getTable().getValueAt(iPos - 1, 0);

				DelegateFactory.getInstance().getKuecheDelegate()
						.vertauscheKassaartikel(iIdPosition, iIdPositionMinus1);

				// die Liste neu anzeigen und den richtigen Datensatz markieren
				panelQueryKassaartikel.setSelectedId(iIdPosition);
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_POSITION_VONNNACHNPLUS1) {
			if (e.getSource() == panelQueryKassaartikel) {
				int iPos = panelQueryKassaartikel.getTable().getSelectedRow();

				// wenn die Position nicht die letzte ist
				if (iPos < panelQueryKassaartikel.getTable().getRowCount() - 1) {
					Integer iIdPosition = (Integer) panelQueryKassaartikel
							.getSelectedId();

					Integer iIdPositionPlus1 = (Integer) panelQueryKassaartikel
							.getTable().getValueAt(iPos + 1, 0);

					DelegateFactory.getInstance().getKuecheDelegate()
							.vertauscheKassaartikel(iIdPosition,
									iIdPositionPlus1);

					// die Liste neu anzeigen und den richtigen Datensatz
					// markieren
					panelQueryKassaartikel.setSelectedId(iIdPosition);
				}
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_POSITION_VORPOSITIONEINFUEGEN) {
		}

		else if (e.getID() == ItemChangedEvent.ACTION_NEW) {
			if (e.getSource() == panelQueryKassaartikel) {
				panelBottomKassaartikel.eventActionNew(e, true, false);
				panelBottomKassaartikel.eventYouAreSelected(false);
			}

			else if (e.getSource() == panelQueryTageslos) {
				panelBottomTageslos.eventActionNew(e, true, false);
				panelBottomTageslos.eventYouAreSelected(false);
			} else if (e.getSource() == panelQueryUmrechnung) {
				panelBottomUmrechnung.eventActionNew(e, true, false);
				panelBottomUmrechnung.eventYouAreSelected(false);
			}else if (e.getSource() == panelQueryBedienerlager) {
				panelBottomBedienerlager.eventActionNew(e, true, false);
				panelBottomBedienerlager.eventYouAreSelected(false);
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_DISCARD) {
			if (e.getSource() == panelBottomKassaartikel) {
				panelSplitKassaartikel.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomTageslos) {
				panelSplitTageslos.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomUmrechnung) {
				panelSplitUmrechnung.eventYouAreSelected(false);
			}else if (e.getSource() == panelBottomBedienerlager) {
				panelSplitBedienerlager.eventYouAreSelected(false);
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_SAVE) {
			if (e.getSource() == panelBottomKassaartikel) {
				Object oKey = panelBottomKassaartikel.getKeyWhenDetailPanel();
				panelQueryKassaartikel.eventYouAreSelected(false);
				panelQueryKassaartikel.setSelectedId(oKey);
				panelSplitKassaartikel.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomTageslos) {
				Object oKey = panelBottomTageslos.getKeyWhenDetailPanel();
				panelQueryTageslos.eventYouAreSelected(false);
				panelQueryTageslos.setSelectedId(oKey);
				panelSplitTageslos.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomUmrechnung) {
				Object oKey = panelBottomUmrechnung.getKeyWhenDetailPanel();
				panelQueryUmrechnung.eventYouAreSelected(false);
				panelQueryUmrechnung.setSelectedId(oKey);
				panelSplitUmrechnung.eventYouAreSelected(false);
			}else if (e.getSource() == panelBottomBedienerlager) {
				Object oKey = panelBottomBedienerlager.getKeyWhenDetailPanel();
				panelQueryBedienerlager.eventYouAreSelected(false);
				panelQueryBedienerlager.setSelectedId(oKey);
				panelSplitBedienerlager.eventYouAreSelected(false);
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_GOTO_MY_DEFAULT_QP) {
			if (e.getSource() == panelBottomKassaartikel) {
				Object oKey = panelQueryKassaartikel.getSelectedId();
				if (oKey != null) {
					getInternalFrame().setKeyWasForLockMe(oKey.toString());
				} else {
					getInternalFrame().setKeyWasForLockMe(null);
				}
				if (panelBottomKassaartikel.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryKassaartikel
							.getId2SelectAfterDelete();
					panelQueryKassaartikel.setSelectedId(oNaechster);
				}
				panelSplitKassaartikel.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomTageslos) {
				Object oKey = panelQueryTageslos.getSelectedId();
				if (oKey != null) {
					getInternalFrame().setKeyWasForLockMe(oKey.toString());
				} else {
					getInternalFrame().setKeyWasForLockMe(null);
				}
				if (panelBottomTageslos.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryTageslos
							.getId2SelectAfterDelete();
					panelQueryTageslos.setSelectedId(oNaechster);
				}

				panelSplitTageslos.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomUmrechnung) {
				Object oKey = panelQueryUmrechnung.getSelectedId();
				if (oKey != null) {
					getInternalFrame().setKeyWasForLockMe(oKey.toString());
				} else {
					getInternalFrame().setKeyWasForLockMe(null);
				}
				if (panelBottomUmrechnung.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryUmrechnung
							.getId2SelectAfterDelete();
					panelQueryUmrechnung.setSelectedId(oNaechster);
				}

				panelSplitUmrechnung.eventYouAreSelected(false);
			}else if (e.getSource() == panelBottomBedienerlager) {
				Object oKey = panelQueryBedienerlager.getSelectedId();
				if (oKey != null) {
					getInternalFrame().setKeyWasForLockMe(oKey.toString());
				} else {
					getInternalFrame().setKeyWasForLockMe(null);
				}
				if (panelBottomBedienerlager.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryBedienerlager
							.getId2SelectAfterDelete();
					panelQueryBedienerlager.setSelectedId(oNaechster);
				}

				panelSplitBedienerlager.eventYouAreSelected(false);
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

		if (selectedIndex == IDX_PANEL_KASSAARTIKEL) {
			createKassaartikel();
			panelSplitKassaartikel.eventYouAreSelected(false);
			panelQueryKassaartikel.updateButtons();
		} else if (selectedIndex == IDX_PANEL_TAGESLOS) {
			createTageslos();
			panelSplitTageslos.eventYouAreSelected(false);
			panelQueryTageslos.updateButtons();
		} else if (selectedIndex == IDX_PANEL_KUECHEUMRECHNUNG) {
			createUmrechnung();
			panelSplitUmrechnung.eventYouAreSelected(false);
			panelQueryUmrechnung.updateButtons();
		} else if (selectedIndex == IDX_PANEL_BEDIENERLAGER) {
			createBedienerlager();
			panelSplitBedienerlager.eventYouAreSelected(false);
			panelQueryBedienerlager.updateButtons();
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
