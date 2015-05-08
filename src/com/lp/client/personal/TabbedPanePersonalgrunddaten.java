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
package com.lp.client.personal;

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
import com.lp.client.system.SystemFilterFactory;
import com.lp.server.personal.service.PersonalFac;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;

@SuppressWarnings("static-access")
/**
 * <p>&UUml;berschrift: </p>
 * <p>Beschreibung: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Organisation: </p>
 * @author Christian Kollmann
 * @version $Revision: 1.6 $
 */
public class TabbedPanePersonalgrunddaten extends TabbedPane {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private PanelQuery panelQueryBeruf = null;
	private PanelBasis panelSplitBeruf = null;
	private PanelBasis panelBottomBeruf = null;

	private PanelQuery panelQueryPendlerpauschale = null;
	private PanelBasis panelSplitPendlerpauschale = null;
	private PanelBasis panelBottomPendlerpauschale = null;

	private PanelQuery panelQueryLohngruppe = null;
	private PanelBasis panelSplitLohngruppe = null;
	private PanelBasis panelBottomLohngruppe = null;

	private PanelQuery panelQueryReligion = null;
	private PanelBasis panelSplitReligion = null;
	private PanelBasis panelBottomReligion = null;

	private PanelQuery panelQueryTagesart = null;
	private PanelBasis panelSplitTagesart = null;
	private PanelBasis panelBottomTagesart = null;

	private PanelQuery panelQueryZulage = null;
	private PanelBasis panelSplitZulage = null;
	private PanelBasis panelBottomZulage = null;

	private PanelQuery panelQueryLohnart = null;
	private PanelBasis panelSplitLohnart = null;
	private PanelBasis panelBottomLohnart = null;

	private PanelQuery panelQueryLohnartStundenfaktor = null;
	private PanelBasis panelSplitLohnartStundenfaktor = null;
	private PanelBasis panelBottomLohnartStundenfaktor = null;

	private final static int IDX_PANEL_BERUF = 0;
	private final static int IDX_PANEL_PENDLERPAUSCHALE = 1;
	private final static int IDX_PANEL_LOHNGRUPPE = 2;
	private final static int IDX_PANEL_RELIGION = 3;
	private final static int IDX_PANEL_TAGESART = 4;
	private final static int IDX_PANEL_ZULAGE = 5;
	private final static int IDX_PANEL_LOHNART = 6;
	private final static int IDX_PANEL_LOHNARTSTUNDENFAKTOR = 7;

	private WrapperMenuBar wrapperMenuBar = null;

	public TabbedPanePersonalgrunddaten(InternalFrame internalFrameI)
			throws Throwable {
		super(internalFrameI, LPMain.getInstance().getTextRespectUISPr(
				"pers.title.tab.grunddaten"));

		jbInit();
		initComponents();
	}

	public InternalFramePersonal getInternalFramePersonal() {
		return (InternalFramePersonal) getInternalFrame();
	}

	private void createZulage() throws Throwable {
		if (panelSplitZulage == null) {
			String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW };

			panelQueryZulage = new PanelQuery(null, null,
					QueryParameters.UC_ID_ZULAGE, aWhichButtonIUse,
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("pers.zulage"), true);
			panelQueryZulage.befuellePanelFilterkriterienDirekt(
					SystemFilterFactory.getInstance().createFKDBezeichnung(),
					null);

			panelBottomZulage = new PanelZulage(getInternalFrame(), LPMain
					.getInstance().getTextRespectUISPr("pers.zulage"), null);
			panelSplitZulage = new PanelSplit(getInternalFrame(),
					panelBottomZulage, panelQueryZulage, 400);

			setComponentAt(IDX_PANEL_ZULAGE, panelSplitZulage);
		}
	}

	private void createLohnart() throws Throwable {
		if (panelSplitLohnart == null) {
			String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW };

			panelQueryLohnart = new PanelQuery(null, null,
					QueryParameters.UC_ID_LOHNART, aWhichButtonIUse,
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("pers.zulage"), true);
			panelQueryLohnart.befuellePanelFilterkriterienDirekt(
					SystemFilterFactory.getInstance().createFKDBezeichnung(),
					null);

			panelBottomLohnart = new PanelLohnart(getInternalFrame(), LPMain
					.getInstance().getTextRespectUISPr("pers.lohnart"), null);
			panelSplitLohnart = new PanelSplit(getInternalFrame(),
					panelBottomLohnart, panelQueryLohnart, 300);

			setComponentAt(IDX_PANEL_LOHNART, panelSplitLohnart);
		}
	}

	private void createLohnartStundenfaktor() throws Throwable {
		if (panelSplitLohnartStundenfaktor == null) {
			String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW };

			panelQueryLohnartStundenfaktor = new PanelQuery(null, null,
					QueryParameters.UC_ID_LOHNARTSTUNDENFAKTOR,
					aWhichButtonIUse, getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("pers.zulage"), true);
			panelQueryLohnartStundenfaktor.befuellePanelFilterkriterienDirekt(
					PersonalFilterFactory.getInstance().createFKDLohnart(),
					null);

			panelBottomLohnartStundenfaktor = new PanelLohnartstundenfaktor(
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("pers.lohnartstunden"), null);
			panelSplitLohnartStundenfaktor = new PanelSplit(getInternalFrame(),
					panelBottomLohnartStundenfaktor,
					panelQueryLohnartStundenfaktor, 300);

			setComponentAt(IDX_PANEL_LOHNARTSTUNDENFAKTOR,
					panelSplitLohnartStundenfaktor);
		}
	}

	private void createTagesart() throws Throwable {
		if (panelSplitTagesart == null) {
			String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW };

			panelQueryTagesart = new PanelQuery(null, PersonalFilterFactory
					.getInstance().createFKZusaetzlicheTagesarten(),
					QueryParameters.UC_ID_TAGESART, aWhichButtonIUse,
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("pers.title.tab.tagesart"),
					true);

			panelQueryTagesart.befuellePanelFilterkriterienDirekt(
					SystemFilterFactory.getInstance().createFKDKennung(),
					SystemFilterFactory.getInstance()
							.createFKDSprTabelleBezeichnung(
									PersonalFac.FLR_TAGESART_TAGESARTSPRSET));

			panelBottomTagesart = new PanelTagesart(getInternalFrame(), LPMain
					.getInstance().getTextRespectUISPr(
							"pers.title.tab.tagesart"), null);

			panelSplitTagesart = new PanelSplit(getInternalFrame(),
					panelBottomTagesart, panelQueryTagesart, 380);

			setComponentAt(IDX_PANEL_TAGESART, panelSplitTagesart);
		}
	}

	private void createReligion() throws Throwable {
		if (panelSplitReligion == null) {
			String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW };

			panelQueryReligion = new PanelQuery(null, null,
					QueryParameters.UC_ID_RELIGION, aWhichButtonIUse,
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("pers.title.tab.religion"),
					true);

			panelQueryReligion.befuellePanelFilterkriterienDirekt(
					SystemFilterFactory.getInstance().createFKDKennung(),
					SystemFilterFactory.getInstance()
							.createFKDSprTabelleBezeichnung(
									PersonalFac.FLR_RELIGION_RELIGIONSPRSET));

			panelBottomReligion = new PanelReligion(getInternalFrame(), LPMain
					.getInstance().getTextRespectUISPr(
							"pers.title.tab.religion"), null);

			panelSplitReligion = new PanelSplit(getInternalFrame(),
					panelBottomReligion, panelQueryReligion, 400);

			setComponentAt(IDX_PANEL_RELIGION, panelSplitReligion);
		}
	}

	private void createBeruf() throws Throwable {
		if (panelSplitBeruf == null) {
			String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW };

			panelQueryBeruf = new PanelQuery(null, null,
					QueryParameters.UC_ID_BERUF, aWhichButtonIUse,
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("pers.title.tab.beruf"), true);
			panelQueryBeruf.befuellePanelFilterkriterienDirekt(
					SystemFilterFactory.getInstance().createFKDBezeichnung(),
					null);

			panelBottomBeruf = new PanelBeruf(getInternalFrame(), LPMain
					.getInstance().getTextRespectUISPr("pers.title.tab.beruf"),
					null);

			panelSplitBeruf = new PanelSplit(getInternalFrame(),
					panelBottomBeruf, panelQueryBeruf, 400);

			setComponentAt(IDX_PANEL_BERUF, panelSplitBeruf);
		}
	}

	private void createLohngruppe() throws Throwable {
		if (panelSplitLohngruppe == null) {
			String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW };
			panelQueryLohngruppe = new PanelQuery(null, null,
					QueryParameters.UC_ID_LOHNGRUPPE, aWhichButtonIUse,
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("pers.title.tab.lohngruppe"),
					true);
			panelQueryLohngruppe.befuellePanelFilterkriterienDirekt(
					SystemFilterFactory.getInstance().createFKDBezeichnung(),
					null);

			panelBottomLohngruppe = new PanelLohngruppe(getInternalFrame(),
					LPMain.getInstance().getTextRespectUISPr(
							"pers.title.tab.lohngruppe"), null);

			panelSplitLohngruppe = new PanelSplit(getInternalFrame(),
					panelBottomLohngruppe, panelQueryLohngruppe, 400);

			setComponentAt(IDX_PANEL_LOHNGRUPPE, panelSplitLohngruppe);
		}
	}

	private void createPendlerpauschale() throws Throwable {
		if (panelSplitPendlerpauschale == null) {
			String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW };

			panelQueryPendlerpauschale = new PanelQuery(null, null,
					QueryParameters.UC_ID_PENDLERPAUSCHALE, aWhichButtonIUse,
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr(
									"pers.title.tab.pendlerpauschale"), true);
			panelQueryPendlerpauschale.befuellePanelFilterkriterienDirekt(
					SystemFilterFactory.getInstance().createFKDBezeichnung(),
					null);

			panelBottomPendlerpauschale = new PanelPendlerpauschale(
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr(
									"pers.title.tab.pendlerpauschale"), null);

			panelSplitPendlerpauschale = new PanelSplit(getInternalFrame(),
					panelBottomPendlerpauschale, panelQueryPendlerpauschale,
					400);

			setComponentAt(IDX_PANEL_PENDLERPAUSCHALE,
					panelSplitPendlerpauschale);
		}
	}

	private void jbInit() throws Throwable {
		// Kollektiv

		// 1 tab oben: QP1 PartnerFLR; lazy loading

		insertTab(
				LPMain.getInstance()
						.getTextRespectUISPr("pers.title.tab.beruf"), null,
				null,
				LPMain.getInstance()
						.getTextRespectUISPr("pers.title.tab.beruf"),
				IDX_PANEL_BERUF);
		insertTab(
				LPMain.getInstance().getTextRespectUISPr(
						"pers.title.tab.pendlerpauschale"),
				null,
				null,
				LPMain.getInstance().getTextRespectUISPr(
						"pers.title.tab.pendlerpauschale"),
				IDX_PANEL_PENDLERPAUSCHALE);
		insertTab(
				LPMain.getInstance().getTextRespectUISPr(
						"pers.title.tab.lohngruppe"),
				null,
				null,
				LPMain.getInstance().getTextRespectUISPr(
						"pers.title.tab.lohngruppe"), IDX_PANEL_LOHNGRUPPE);
		insertTab(
				LPMain.getInstance().getTextRespectUISPr(
						"pers.title.tab.religion"),
				null,
				null,
				LPMain.getInstance().getTextRespectUISPr(
						"pers.title.tab.religion"), IDX_PANEL_RELIGION);

		insertTab(
				LPMain.getInstance().getTextRespectUISPr(
						"pers.title.tab.tagesart"),
				null,
				null,
				LPMain.getInstance().getTextRespectUISPr(
						"pers.title.tab.tagesart"), IDX_PANEL_TAGESART);

		insertTab(LPMain.getInstance().getTextRespectUISPr("pers.zulage"),
				null, null,
				LPMain.getInstance().getTextRespectUISPr("pers.zulage"),
				IDX_PANEL_ZULAGE);

		insertTab(LPMain.getInstance().getTextRespectUISPr("pers.lohnart"),
				null, null,
				LPMain.getInstance().getTextRespectUISPr("pers.lohnart"),
				IDX_PANEL_LOHNART);
		insertTab(
				LPMain.getInstance().getTextRespectUISPr("pers.lohnartstunden"),
				null,
				null,
				LPMain.getInstance().getTextRespectUISPr("pers.lohnartstunden"),
				IDX_PANEL_LOHNARTSTUNDENFAKTOR);

		createBeruf();

		// Itemevents an MEIN Detailpanel senden kann.
		this.addChangeListener(this);
		getInternalFrame().addItemChangedListener(this);

	}

	public void lPEventItemChanged(ItemChangedEvent e) throws Throwable {

		if (e.getID() == ItemChangedEvent.ITEM_CHANGED) {
			if (e.getSource() == panelQueryBeruf) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				panelBottomBeruf.setKeyWhenDetailPanel(key);
				panelBottomBeruf.eventYouAreSelected(false);
				panelQueryBeruf.updateButtons();

			} else if (e.getSource() == panelQueryLohngruppe) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				panelBottomLohngruppe.setKeyWhenDetailPanel(key);
				panelBottomLohngruppe.eventYouAreSelected(false);
				panelQueryLohngruppe.updateButtons();

			} else if (e.getSource() == panelQueryPendlerpauschale) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				panelBottomPendlerpauschale.setKeyWhenDetailPanel(key);
				panelBottomPendlerpauschale.eventYouAreSelected(false);
				panelQueryPendlerpauschale.updateButtons();

			} else if (e.getSource() == panelQueryReligion) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				panelBottomReligion.setKeyWhenDetailPanel(key);
				panelBottomReligion.eventYouAreSelected(false);
				panelQueryReligion.updateButtons();

			} else if (e.getSource() == panelQueryTagesart) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				panelBottomTagesart.setKeyWhenDetailPanel(key);
				panelBottomTagesart.eventYouAreSelected(false);
				panelQueryTagesart.updateButtons();

			} else if (e.getSource() == panelQueryZulage) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				panelBottomZulage.setKeyWhenDetailPanel(key);
				panelBottomZulage.eventYouAreSelected(false);
				panelQueryZulage.updateButtons();

			} else if (e.getSource() == panelQueryLohnart) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				panelBottomLohnart.setKeyWhenDetailPanel(key);
				panelBottomLohnart.eventYouAreSelected(false);
				panelQueryLohnart.updateButtons();

			} else if (e.getSource() == panelQueryLohnartStundenfaktor) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				panelBottomLohnartStundenfaktor.setKeyWhenDetailPanel(key);
				panelBottomLohnartStundenfaktor.eventYouAreSelected(false);
				panelQueryLohnartStundenfaktor.updateButtons();

			}

		} else if (e.getID() == ItemChangedEvent.ACTION_YOU_ARE_SELECTED) {
			refreshTitle();
			panelSplitBeruf.eventYouAreSelected(false);
		} else if (e.getID() == ItemChangedEvent.ACTION_NEW) {
			if (e.getSource() == panelQueryLohngruppe) {
				panelBottomLohngruppe.eventActionNew(e, true, false);
				panelBottomLohngruppe.eventYouAreSelected(false);
			} else if (e.getSource() == panelQueryBeruf) {
				panelBottomBeruf.eventActionNew(e, true, false);
				panelBottomBeruf.eventYouAreSelected(false);
			} else if (e.getSource() == panelQueryPendlerpauschale) {
				panelBottomPendlerpauschale.eventActionNew(e, true, false);
				panelBottomPendlerpauschale.eventYouAreSelected(false);
			} else if (e.getSource() == panelQueryReligion) {
				panelBottomReligion.eventActionNew(e, true, false);
				panelBottomReligion.eventYouAreSelected(false);
			} else if (e.getSource() == panelQueryTagesart) {
				panelBottomTagesart.eventActionNew(e, true, false);
				panelBottomTagesart.eventYouAreSelected(false);
			} else if (e.getSource() == panelQueryZulage) {
				panelBottomZulage.eventActionNew(e, true, false);
				panelBottomZulage.eventYouAreSelected(false);
			} else if (e.getSource() == panelQueryLohnart) {
				panelBottomLohnart.eventActionNew(e, true, false);
				panelBottomLohnart.eventYouAreSelected(false);
			} else if (e.getSource() == panelQueryLohnartStundenfaktor) {
				panelBottomLohnartStundenfaktor.eventActionNew(e, true, false);
				panelBottomLohnartStundenfaktor.eventYouAreSelected(false);
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_UPDATE) {
			if (e.getSource() == panelBottomBeruf) {
				panelQueryBeruf.updateButtons(new LockStateValue(
						PanelBasis.LOCK_FOR_NEW));
			} else if (e.getSource() == panelBottomLohngruppe) {
				panelQueryLohngruppe.updateButtons(new LockStateValue(
						PanelBasis.LOCK_FOR_NEW));
			} else if (e.getSource() == panelBottomPendlerpauschale) {
				panelQueryPendlerpauschale.updateButtons(new LockStateValue(
						PanelBasis.LOCK_FOR_NEW));
				;
			} else if (e.getSource() == panelBottomReligion) {
				panelQueryReligion.updateButtons(new LockStateValue(
						PanelBasis.LOCK_FOR_NEW));
			} else if (e.getSource() == panelBottomTagesart) {
				panelQueryTagesart.updateButtons(new LockStateValue(
						PanelBasis.LOCK_FOR_NEW));
			} else if (e.getSource() == panelBottomZulage) {
				panelQueryZulage.updateButtons(new LockStateValue(
						PanelBasis.LOCK_FOR_NEW));
			} else if (e.getSource() == panelBottomLohnart) {
				panelQueryLohnart.updateButtons(new LockStateValue(
						PanelBasis.LOCK_FOR_NEW));
			} else if (e.getSource() == panelBottomLohnartStundenfaktor) {
				panelQueryLohnartStundenfaktor
						.updateButtons(new LockStateValue(
								PanelBasis.LOCK_FOR_NEW));
			}
		}

		else if (e.getID() == ItemChangedEvent.ACTION_DISCARD) {
			if (e.getSource() == panelBottomBeruf) {
				panelSplitBeruf.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomLohngruppe) {
				panelSplitLohngruppe.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomPendlerpauschale) {
				panelSplitPendlerpauschale.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomReligion) {
				panelSplitReligion.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomTagesart) {
				panelSplitTagesart.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomZulage) {
				panelSplitZulage.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomLohnart) {
				panelSplitLohnart.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomLohnartStundenfaktor) {
				panelSplitLohnartStundenfaktor.eventYouAreSelected(false);
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_SAVE) {
			if (e.getSource() == panelBottomBeruf) {
				Object oKey = panelBottomBeruf.getKeyWhenDetailPanel();
				panelQueryBeruf.eventYouAreSelected(false);
				panelQueryBeruf.setSelectedId(oKey);
				panelSplitBeruf.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomLohngruppe) {
				Object oKey = panelBottomLohngruppe.getKeyWhenDetailPanel();
				panelQueryLohngruppe.eventYouAreSelected(false);
				panelQueryLohngruppe.setSelectedId(oKey);
				panelSplitLohngruppe.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomPendlerpauschale) {
				Object oKey = panelBottomPendlerpauschale
						.getKeyWhenDetailPanel();
				panelQueryPendlerpauschale.eventYouAreSelected(false);
				panelQueryPendlerpauschale.setSelectedId(oKey);
				panelSplitPendlerpauschale.eventYouAreSelected(false);

			} else if (e.getSource() == panelBottomReligion) {
				Object oKey = panelBottomReligion.getKeyWhenDetailPanel();
				panelQueryReligion.eventYouAreSelected(false);
				panelQueryReligion.setSelectedId(oKey);
				panelSplitReligion.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomTagesart) {
				Object oKey = panelBottomTagesart.getKeyWhenDetailPanel();
				panelQueryTagesart.eventYouAreSelected(false);
				panelQueryTagesart.setSelectedId(oKey);
				panelSplitTagesart.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomZulage) {
				Object oKey = panelBottomZulage.getKeyWhenDetailPanel();
				panelQueryZulage.eventYouAreSelected(false);
				panelQueryZulage.setSelectedId(oKey);
				panelSplitZulage.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomLohnart) {
				Object oKey = panelBottomLohnart.getKeyWhenDetailPanel();
				panelQueryLohnart.eventYouAreSelected(false);
				panelQueryLohnart.setSelectedId(oKey);
				panelSplitLohnart.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomLohnartStundenfaktor) {
				Object oKey = panelBottomLohnartStundenfaktor
						.getKeyWhenDetailPanel();
				panelQueryLohnartStundenfaktor.eventYouAreSelected(false);
				panelQueryLohnartStundenfaktor.setSelectedId(oKey);
				panelSplitLohnartStundenfaktor.eventYouAreSelected(false);
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_GOTO_MY_DEFAULT_QP) {
			if (e.getSource() == panelBottomBeruf) {
				Object oKey = panelQueryBeruf.getSelectedId();
				if (oKey != null) {
					getInternalFrame().setKeyWasForLockMe(oKey.toString());
				} else {
					getInternalFrame().setKeyWasForLockMe(null);
				}
				if (panelBottomBeruf.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryBeruf
							.getId2SelectAfterDelete();
					panelQueryBeruf.setSelectedId(oNaechster);
				}
				panelSplitBeruf.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomLohngruppe) {
				Object oKey = panelQueryLohngruppe.getSelectedId();
				if (oKey != null) {
					getInternalFrame().setKeyWasForLockMe(oKey.toString());
				} else {
					getInternalFrame().setKeyWasForLockMe(null);
				}
				if (panelBottomLohngruppe.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryLohngruppe
							.getId2SelectAfterDelete();
					panelQueryLohngruppe.setSelectedId(oNaechster);
				}
				panelSplitLohngruppe.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomPendlerpauschale) {
				Object oKey = panelQueryPendlerpauschale.getSelectedId();
				if (oKey != null) {
					getInternalFrame().setKeyWasForLockMe(oKey.toString());
				} else {
					getInternalFrame().setKeyWasForLockMe(null);
				}
				if (panelBottomPendlerpauschale.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryPendlerpauschale
							.getId2SelectAfterDelete();
					panelQueryPendlerpauschale.setSelectedId(oNaechster);
				}
				panelSplitPendlerpauschale.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomReligion) {
				Object oKey = panelQueryReligion.getSelectedId();
				if (oKey != null) {
					getInternalFrame().setKeyWasForLockMe(oKey.toString());
				} else {
					getInternalFrame().setKeyWasForLockMe(null);
				}
				if (panelBottomReligion.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryReligion
							.getId2SelectAfterDelete();
					panelQueryReligion.setSelectedId(oNaechster);
				}
				panelSplitReligion.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomTagesart) {
				Object oKey = panelQueryTagesart.getSelectedId();
				if (oKey != null) {
					getInternalFrame().setKeyWasForLockMe(oKey.toString());
				} else {
					getInternalFrame().setKeyWasForLockMe(null);
				}
				if (panelBottomTagesart.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryTagesart
							.getId2SelectAfterDelete();
					panelQueryTagesart.setSelectedId(oNaechster);
				}
				panelSplitTagesart.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomZulage) {
				Object oKey = panelQueryZulage.getSelectedId();
				if (oKey != null) {
					getInternalFrame().setKeyWasForLockMe(oKey.toString());
				} else {
					getInternalFrame().setKeyWasForLockMe(null);
				}
				if (panelBottomZulage.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryZulage
							.getId2SelectAfterDelete();
					panelQueryZulage.setSelectedId(oNaechster);
				}
				panelSplitZulage.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomLohnart) {
				Object oKey = panelQueryLohnart.getSelectedId();
				if (oKey != null) {
					getInternalFrame().setKeyWasForLockMe(oKey.toString());
				} else {
					getInternalFrame().setKeyWasForLockMe(null);
				}
				if (panelBottomLohnart.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryLohnart
							.getId2SelectAfterDelete();
					panelQueryLohnart.setSelectedId(oNaechster);
				}
				panelSplitLohnart.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomLohnartStundenfaktor) {
				Object oKey = panelQueryLohnartStundenfaktor.getSelectedId();
				if (oKey != null) {
					getInternalFrame().setKeyWasForLockMe(oKey.toString());
				} else {
					getInternalFrame().setKeyWasForLockMe(null);
				}
				if (panelBottomLohnartStundenfaktor.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryLohnartStundenfaktor
							.getId2SelectAfterDelete();
					panelQueryLohnartStundenfaktor.setSelectedId(oNaechster);
				}
				panelSplitLohnartStundenfaktor.eventYouAreSelected(false);
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

	}

	public void lPEventObjectChanged(ChangeEvent e) throws Throwable {
		super.lPEventObjectChanged(e);
		int selectedIndex = this.getSelectedIndex();

		if (selectedIndex == IDX_PANEL_BERUF) {
			createBeruf();
			panelSplitBeruf.eventYouAreSelected(false);
			panelQueryBeruf.updateButtons();
		}

		else if (selectedIndex == IDX_PANEL_LOHNGRUPPE) {
			createLohngruppe();
			panelSplitLohngruppe.eventYouAreSelected(false);
			panelQueryLohngruppe.updateButtons();
		} else if (selectedIndex == IDX_PANEL_PENDLERPAUSCHALE) {
			createPendlerpauschale();
			panelSplitPendlerpauschale.eventYouAreSelected(false);
			panelQueryPendlerpauschale.updateButtons();
		} else if (selectedIndex == IDX_PANEL_RELIGION) {
			createReligion();
			panelSplitReligion.eventYouAreSelected(false);
			panelQueryReligion.updateButtons();
		} else if (selectedIndex == IDX_PANEL_TAGESART) {
			createTagesart();
			panelSplitTagesart.eventYouAreSelected(false);
			panelQueryTagesart.updateButtons();
		} else if (selectedIndex == IDX_PANEL_ZULAGE) {
			createZulage();
			panelSplitZulage.eventYouAreSelected(false);
			panelQueryZulage.updateButtons();
		}else if (selectedIndex == IDX_PANEL_LOHNART) {
			createLohnart();
			panelSplitLohnart.eventYouAreSelected(false);
			panelQueryLohnart.updateButtons();
		}else if (selectedIndex == IDX_PANEL_LOHNARTSTUNDENFAKTOR) {
			createLohnartStundenfaktor();
			panelSplitLohnartStundenfaktor.eventYouAreSelected(false);
			panelQueryLohnartStundenfaktor.updateButtons();
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
