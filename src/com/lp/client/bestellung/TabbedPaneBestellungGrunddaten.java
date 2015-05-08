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
package com.lp.client.bestellung;

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
import com.lp.server.bestellung.service.BSMahnstufeDto;
import com.lp.server.bestellung.service.BSMahntextDto;
import com.lp.server.bestellung.service.BSMahnwesenFac;
import com.lp.server.bestellung.service.BestellungsartDto;
import com.lp.server.bestellung.service.BestellungtextDto;
import com.lp.server.system.service.MediaFac;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;

@SuppressWarnings("static-access")
/*
 * <p><I>Diese Klasse kuemmert sich ...</I> </p>
 * 
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 * 
 * <p>Erstellungsdatum <I>10.03.05</I></p>
 * 
 * <p> </p>
 * 
 * @author Christian Winhart
 * 
 * @version $Revision: 1.7 $
 */
public class TabbedPaneBestellungGrunddaten extends TabbedPane {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private BestellungsartDto bestellungartDto = null;
	private PanelQuery panelBestellungsartTopQP1 = null;
	private PanelSplit panelBestellungsartSP1 = null;
	private PanelBasis panelBestellungsartBottomD1 = null;

	private PanelQuery panelQueryBestellungtext;
	private PanelSplit panelSplitBestellungtext;
	private PanelStammdatenCRUD panelBottomBestellungtext;

	private PanelQuery panelBestellungstatusTopQP3 = null;
	private PanelSplit panelBestellungstatusSP3 = null;
	private PanelBasis panelBestellungstatusBottomD3 = null;

	private PanelQuery panelBestellungpositionsartTopQP4 = null;
	private PanelSplit panelBestellungpositionsartSP4 = null;
	private PanelBasis panelBestellungpositionsartBottomD4 = null;

	private PanelQuery panelMahngruppeTopQP4 = null;
	private PanelSplit panelMahngruppeSP4 = null;
	private PanelBasis panelMahngruppeBottomD4 = null;

	private PanelQuery panelQueryMahntext;
	private PanelStammdatenCRUD panelBottomMahntext;
	private PanelSplit panelSplitMahntext;

	private PanelQuery panelQueryMahnstufe = null;
	private PanelStammdatenCRUD panelBottomMahnstufe = null;
	private PanelSplit panelSplitMahnstufe = null;

	private static final int IDX_PANEL_BESTELLUNGART = 0;
	private static final int IDX_PANEL_BESTELLUNGSTATUS = 1;
	private static final int IDX_PANEL_BESTELLUNGTEXT = 2;
	private static final int IDX_PANEL_BESTELLPOSITONSART = 3;
	private static final int IDX_PANEL_MAHNTEXT = 4;
	private final static int IDX_PANEL_MAHNSTUFE = 5;
	private final static int IDX_PANEL_MAHNGRUPPE = 6;

	public TabbedPaneBestellungGrunddaten(InternalFrame internalFrameI)
			throws Throwable {
		super(internalFrameI, LPMain.getTextRespectUISPr("lp.grunddaten"));
		jbInit();
		initComponents();
	}

	public InternalFrameBestellung getInternalFrameBestellung() {
		return (InternalFrameBestellung) getInternalFrame();
	}

	/**
	 * jbInit
	 * 
	 * @throws Throwable
	 */
	private void jbInit() throws Throwable {
		insertTab(
				LPMain.getInstance().getTextRespectUISPr("lp.bestellungart"),
				null,
				null,
				LPMain.getInstance().getTextRespectUISPr(
						"lp.tooltip.bestellungart"), IDX_PANEL_BESTELLUNGART);

		insertTab(
				LPMain.getInstance().getTextRespectUISPr("lp.bestellungstatus"),
				null,
				null,
				LPMain.getInstance().getTextRespectUISPr(
						"lp.tooltip.bestellungstatus"),
				IDX_PANEL_BESTELLUNGSTATUS);

		insertTab(
				LPMain.getInstance().getTextRespectUISPr("lp.bestellungtext"),
				null, null,
				LPMain.getInstance().getTextRespectUISPr("lp.bestellungtext"),
				IDX_PANEL_BESTELLUNGTEXT);

		insertTab(
				LPMain.getInstance().getTextRespectUISPr(
						"lp.bestellungpositionsart"),
				null,
				null,
				LPMain.getInstance().getTextRespectUISPr(
						"lp.bestellungpositionsart"),
				IDX_PANEL_BESTELLPOSITONSART);

		insertTab(LPMain.getInstance().getTextRespectUISPr("lp.mahntext"),
				null, null,
				LPMain.getInstance().getTextRespectUISPr("lp.mahntext"),
				IDX_PANEL_MAHNTEXT);

		insertTab(LPMain.getInstance().getTextRespectUISPr("bes.mahnstufe"),
				null, null,
				LPMain.getInstance().getTextRespectUISPr("bes.mahnstufe"),
				IDX_PANEL_MAHNSTUFE);

		insertTab(LPMain.getInstance().getTextRespectUISPr("bes.mahngruppe"),
				null, null,
				LPMain.getInstance().getTextRespectUISPr("bes.mahnstufe"),
				IDX_PANEL_MAHNGRUPPE);

		refreshPanelBestellungsart();
		addChangeListener(this);
		getInternalFrame().addItemChangedListener(this);
	}

	/**
	 * changed
	 * 
	 * @param eI
	 *            ItemChangedEvent
	 * @throws Throwable
	 */
	public void lPEventItemChanged(ItemChangedEvent eI) throws Throwable {

		ItemChangedEvent e = (ItemChangedEvent) eI;

		if (e.getID() == ItemChangedEvent.ITEM_CHANGED) {
			if (e.getSource() == panelBestellungsartTopQP1) {
				String cNr = (String) panelBestellungsartTopQP1.getSelectedId();
				getInternalFrame().setKeyWasForLockMe(cNr);
				panelBestellungsartBottomD1.setKeyWhenDetailPanel(cNr);
				panelBestellungsartBottomD1.eventYouAreSelected(false);

				// im QP die Buttons in den Zustand nolocking/save setzen.
				panelBestellungsartTopQP1
						.updateButtons(panelBestellungsartBottomD1
								.getLockedstateDetailMainKey());

				getInternalFrame().setLpTitle(
						InternalFrame.TITLE_IDX_AS_I_LIKE, cNr);
			} else if (e.getSource() == this.panelBestellungstatusTopQP3) {
				String cNr = (String) panelBestellungstatusTopQP3
						.getSelectedId();
				getInternalFrame().setKeyWasForLockMe(cNr);
				panelBestellungstatusBottomD3.setKeyWhenDetailPanel(cNr);
				panelBestellungstatusBottomD3.eventYouAreSelected(false);

				// im QP die Buttons in den Zustand nolocking/save setzen.
				panelBestellungstatusTopQP3
						.updateButtons(panelBestellungstatusBottomD3
								.getLockedstateDetailMainKey());

				getInternalFrame().setLpTitle(
						InternalFrame.TITLE_IDX_AS_I_LIKE, cNr);
			} else if (e.getSource() == this.panelMahngruppeTopQP4) {
				Integer artgruIId = (Integer) panelMahngruppeTopQP4
						.getSelectedId();
				getInternalFrame().setKeyWasForLockMe(artgruIId+"");
				panelMahngruppeBottomD4.setKeyWhenDetailPanel(artgruIId);
				panelMahngruppeBottomD4.eventYouAreSelected(false);

				// im QP die Buttons in den Zustand nolocking/save setzen.
				panelMahngruppeTopQP4
						.updateButtons(panelMahngruppeBottomD4
								.getLockedstateDetailMainKey());

				
			} else if (eI.getSource() == panelQueryBestellungtext) {
				Integer iId = (Integer) panelQueryBestellungtext
						.getSelectedId();
				getInternalFrame().setKeyWasForLockMe(iId + "");
				panelBottomBestellungtext.setKeyWhenDetailPanel(iId);
				panelBottomBestellungtext.eventYouAreSelected(false);

				// im QP die Buttons in den Zustand nolocking/save setzen.
				panelQueryBestellungtext
						.updateButtons(panelBottomBestellungtext
								.getLockedstateDetailMainKey());

				if (iId != null) {
					BestellungtextDto bestellungTextDto = DelegateFactory
							.getInstance().getBestellungServiceDelegate()
							.bestellungtextFindByPrimaryKey(iId);

					if (bestellungTextDto != null
							&& bestellungTextDto.getCNr() != null) {
						getInternalFrame().setLpTitle(
								InternalFrame.TITLE_IDX_AS_I_LIKE,
								bestellungTextDto.getCNr());
					}
				}
			} else if (eI.getSource() == panelBestellungpositionsartTopQP4) {
				String cNr = (String) panelBestellungpositionsartTopQP4
						.getSelectedId();
				getInternalFrame().setKeyWasForLockMe(cNr + "");
				panelBestellungpositionsartBottomD4.setKeyWhenDetailPanel(cNr);
				panelBestellungpositionsartBottomD4.eventYouAreSelected(false);

				// im QP die Buttons in den Zustand nolocking/save setzen.
				panelBestellungpositionsartTopQP4
						.updateButtons(panelBestellungpositionsartBottomD4
								.getLockedstateDetailMainKey());

				getInternalFrame().setLpTitle(
						InternalFrame.TITLE_IDX_AS_I_LIKE, cNr);
			} else if (eI.getSource() == panelQueryMahnstufe) {
				Integer iId = (Integer) panelQueryMahnstufe.getSelectedId();
				getInternalFrame().setKeyWasForLockMe(iId + "");
				panelBottomMahnstufe.setKeyWhenDetailPanel(iId);
				panelBottomMahnstufe.eventYouAreSelected(false);

				// im QP die Buttons in den Zustand nolocking/save setzen.
				panelQueryMahnstufe.updateButtons(panelBottomMahnstufe
						.getLockedstateDetailMainKey());

				// getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_AS_I_LIKE
				// , iId);
			} else if (eI.getSource() == panelQueryMahntext) {
				Integer iId = (Integer) panelQueryMahntext.getSelectedId();
				getInternalFrame().setKeyWasForLockMe(iId + "");
				panelBottomMahntext.setKeyWhenDetailPanel(iId);
				panelBottomMahntext.eventYouAreSelected(false);

				// im QP die Buttons in den Zustand nolocking/save setzen.
				panelQueryMahntext.updateButtons(panelBottomMahntext
						.getLockedstateDetailMainKey());

				// getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_AS_I_LIKE
				// , iId);
			}
		}

		else if (eI.getID() == ItemChangedEvent.ACTION_UPDATE) {
			// hier kommt man nach upd im D bei einem 1:n hin.
			if (eI.getSource() == panelBottomBestellungtext) {
				// im QP die Buttons in den Zustand neu setzen.
				panelQueryBestellungtext.updateButtons(new LockStateValue(
						PanelBasis.LOCK_FOR_NEW));
			} else if (eI.getSource() == panelBestellungstatusBottomD3) {
				// im QP die Buttons in den Zustand neu setzen.
				panelBestellungstatusTopQP3.updateButtons(new LockStateValue(
						PanelBasis.LOCK_FOR_NEW));
			} else if (eI.getSource() == this.panelBestellungsartBottomD1) {
				// im QP die Buttons in den Zustand neu setzen.
				this.panelBestellungsartTopQP1
						.updateButtons(new LockStateValue(
								PanelBasis.LOCK_FOR_NEW));
			} else if (eI.getSource() == this.panelMahngruppeBottomD4) {
				// im QP die Buttons in den Zustand neu setzen.
				this.panelMahngruppeTopQP4
						.updateButtons(new LockStateValue(
								PanelBasis.LOCK_FOR_NEW));
			} else if (eI.getSource() == this.panelBestellungpositionsartBottomD4) {
				// im QP die Buttons in den Zustand neu setzen.
				this.panelBestellungpositionsartTopQP4
						.updateButtons(new LockStateValue(
								PanelBasis.LOCK_FOR_NEW));
			} else if (eI.getSource() == this.panelBottomMahnstufe) {
				// im QP die Buttons in den Zustand neu setzen.
				this.panelQueryMahnstufe.updateButtons(new LockStateValue(
						PanelBasis.LOCK_FOR_NEW));
			} else if (eI.getSource() == this.panelBottomMahntext) {
				// im QP die Buttons in den Zustand neu setzen.
				this.panelQueryMahntext.updateButtons(new LockStateValue(
						PanelBasis.LOCK_FOR_NEW));
			}
		}

		else if (e.getID() == ItemChangedEvent.ACTION_DISCARD) {
			if (e.getSource() == panelBestellungsartBottomD1) {
				panelBestellungsartSP1.eventYouAreSelected(false);
			} else if (e.getSource() == panelBestellungstatusBottomD3) {
				panelBestellungstatusSP3.eventYouAreSelected(false);
			} else if (eI.getSource() == panelBottomBestellungtext) {
				panelSplitBestellungtext.eventYouAreSelected(false);
			} else if (eI.getSource() == panelBestellungpositionsartBottomD4) {
				panelBestellungpositionsartSP4.eventYouAreSelected(false);
			} else if (eI.getSource() == panelBottomMahnstufe) {
				panelSplitMahnstufe.eventYouAreSelected(false);
			} else if (eI.getSource() == panelBottomMahntext) {
				panelSplitMahntext.eventYouAreSelected(false);
			}else if (eI.getSource() == panelMahngruppeBottomD4) {
				panelMahngruppeSP4.eventYouAreSelected(false);
			}
		}

		else if (e.getID() == ItemChangedEvent.ACTION_SAVE) {
			if (e.getSource() == panelBestellungsartBottomD1) {
				Object oKey = panelBestellungsartBottomD1
						.getKeyWhenDetailPanel();
				panelBestellungsartTopQP1.eventYouAreSelected(false);
				panelBestellungsartTopQP1.setSelectedId(oKey);
				panelBestellungsartSP1.eventYouAreSelected(false);
			} else if (e.getSource() == panelBestellungstatusBottomD3) {
				Object oKey = panelBestellungstatusBottomD3
						.getKeyWhenDetailPanel();
				panelBestellungstatusTopQP3.eventYouAreSelected(false);
				panelBestellungstatusTopQP3.setSelectedId(oKey);
				panelBestellungstatusSP3.eventYouAreSelected(false);
			} else if (eI.getSource() == panelBottomBestellungtext) {
				Object oKey = panelBottomBestellungtext.getKeyWhenDetailPanel();
				panelQueryBestellungtext.eventYouAreSelected(false);
				panelQueryBestellungtext.setSelectedId(oKey);
				panelSplitBestellungtext.eventYouAreSelected(false);
			} else if (eI.getSource() == panelMahngruppeBottomD4) {
				Object oKey = panelMahngruppeBottomD4.getKeyWhenDetailPanel();
				panelMahngruppeTopQP4.eventYouAreSelected(false);
				panelMahngruppeTopQP4.setSelectedId(oKey);
				panelMahngruppeSP4.eventYouAreSelected(false);
			} else if (e.getSource() == panelBestellungpositionsartBottomD4) {
				Object oKey = panelBestellungpositionsartBottomD4
						.getKeyWhenDetailPanel();
				panelBestellungpositionsartTopQP4.eventYouAreSelected(false);
				panelBestellungpositionsartTopQP4.setSelectedId(oKey);
				panelBestellungpositionsartSP4.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomMahnstufe) {
				Object oKey = panelBottomMahnstufe.getKeyWhenDetailPanel();
				panelQueryMahnstufe.eventYouAreSelected(false);
				panelQueryMahnstufe.setSelectedId(oKey);
				panelSplitMahnstufe.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomMahntext) {
				Object oKey = panelBottomMahntext.getKeyWhenDetailPanel();
				panelQueryMahntext.eventYouAreSelected(false);
				panelQueryMahntext.setSelectedId(oKey);
				panelSplitMahntext.eventYouAreSelected(false);
			}
		}

		else if (e.getID() == ItemChangedEvent.ACTION_GOTO_MY_DEFAULT_QP) {
			if (e.getSource() == panelBestellungsartBottomD1) {
				panelBestellungsartSP1.eventYouAreSelected(false); // refresh
																	// auf das
																	// gesamte
																	// 1:n panel
			} else if (e.getSource() == panelBestellungstatusBottomD3) {
				panelBestellungstatusSP3.eventYouAreSelected(false); // refresh
																		// auf
																		// das
																		// gesamte
																		// 1:n
																		// panel
			} else if (eI.getSource() == panelBottomBestellungtext) {
				panelSplitBestellungtext.eventYouAreSelected(false);
			} else if (eI.getSource() == panelBestellungpositionsartBottomD4) {
				panelBestellungpositionsartSP4.eventYouAreSelected(false);
			} else if (eI.getSource() == panelMahngruppeBottomD4) {
				panelMahngruppeSP4.eventYouAreSelected(false);
			} else if (eI.getSource() == panelBottomMahnstufe) {
				if (panelBottomMahnstufe.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryMahnstufe
							.getId2SelectAfterDelete();
					panelQueryMahnstufe.setSelectedId(oNaechster);
				}
				panelSplitMahnstufe.eventYouAreSelected(false);
			} else if (eI.getSource() == panelBottomMahntext) {
				if (panelBottomMahntext.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryMahntext
							.getId2SelectAfterDelete();
					panelQueryMahntext.setSelectedId(oNaechster);
				}
				panelSplitMahntext.eventYouAreSelected(false);
			}
		}

		else if (eI.getID() == ItemChangedEvent.ACTION_NEW) {
			if (eI.getSource() == panelBestellungsartTopQP1) {
				/** @todo JO->JE wenn beans nach CW-FAQ dann test! PJ 5209 */
				panelBestellungsartBottomD1.eventActionNew(e, true, false);
				panelBestellungsartBottomD1.eventYouAreSelected(false);
				setSelectedComponent(panelBestellungsartSP1);
			} else if (eI.getSource() == panelBestellungstatusTopQP3) {
				/** @todo JO->JE wenn beans nach CW-FAQ dann test! PJ 5209 */
				panelBestellungstatusBottomD3.eventActionNew(e, true, false);
				panelBestellungstatusBottomD3.eventYouAreSelected(false);
				setSelectedComponent(panelBestellungstatusSP3);
			} else if (eI.getSource() == panelQueryBestellungtext) {
				if (panelQueryBestellungtext.getSelectedId() == null) {
					getInternalFrame().enableAllPanelsExcept(true);
				}
				// eI.setAcceptFromThisSrc(panelQueryBestellungtext);
				panelBottomBestellungtext.eventActionNew(eI, true, false);
				panelBottomBestellungtext.eventYouAreSelected(false);
				setSelectedComponent(panelSplitBestellungtext);
				// eI.setAcceptFromThisSrc(null);
			} else if (eI.getSource() == panelBestellungpositionsartTopQP4) {
				if (panelBestellungpositionsartTopQP4.getSelectedId() == null) {
					getInternalFrame().enableAllPanelsExcept(true);
				}
				// eI.setAcceptFromThisSrc(panelBestellungpositionsartTopQP4);
				panelBestellungpositionsartBottomD4.eventActionNew(eI, true,
						false);
				panelBestellungpositionsartBottomD4.eventYouAreSelected(false);
				setSelectedComponent(panelBestellungpositionsartSP4);
				// eI.setAcceptFromThisSrc(null);
			} else if (eI.getSource() == panelQueryMahnstufe) {
				if (panelQueryMahnstufe.getSelectedId() == null) {
					getInternalFrame().enableAllPanelsExcept(true);
				}
				// eI.setAcceptFromThisSrc(panelQueryMahnstufe);
				panelBottomMahnstufe.eventActionNew(eI, true, false);
				panelBottomMahnstufe.eventYouAreSelected(false);
				setSelectedComponent(panelSplitMahnstufe);
				// eI.setAcceptFromThisSrc(null);
			} else if (eI.getSource() == panelMahngruppeTopQP4) {
				if (panelMahngruppeTopQP4.getSelectedId() == null) {
					getInternalFrame().enableAllPanelsExcept(true);
				}
				// eI.setAcceptFromThisSrc(panelQueryMahnstufe);
				panelMahngruppeBottomD4.eventActionNew(eI, true, false);
				panelMahngruppeBottomD4.eventYouAreSelected(false);
				setSelectedComponent(panelMahngruppeSP4);
				// eI.setAcceptFromThisSrc(null);
			} else if (eI.getSource() == panelQueryMahntext) {
				if (panelQueryMahntext.getSelectedId() == null) {
					getInternalFrame().enableAllPanelsExcept(true);
				}
				// eI.setAcceptFromThisSrc(panelQueryMahntext);
				panelBottomMahntext.eventActionNew(eI, true, false);
				panelBottomMahntext.eventYouAreSelected(false);
				setSelectedComponent(panelSplitMahntext);
				// eI.setAcceptFromThisSrc(null);
			}
		}
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
		case IDX_PANEL_BESTELLUNGART:
			refreshPanelBestellungsart();
			panelBestellungsartTopQP1.eventYouAreSelected(false);

			// im QP die Buttons setzen.
			panelBestellungsartTopQP1.updateButtons(panelBestellungsartTopQP1
					.getLockedstateDetailMainKey());

			// wenn es fuer das tabbed pane noch keinen Eintrag gibt,
			// die restlichen oberen Laschen deaktivieren, ausser ...
			if (panelBestellungsartTopQP1.getSelectedId() == null) {
				getInternalFrame().enableAllOberePanelsExceptMe(this,
						IDX_PANEL_BESTELLUNGART, false);
			}
			panelBestellungsartTopQP1.updateButtons(panelBestellungsartBottomD1
					.getLockedstateDetailMainKey());
			break;
		}
		switch (selectedIndex) {
		case IDX_PANEL_BESTELLUNGSTATUS:
			this.refreshPanelBestellungstatus();
			panelBestellungstatusTopQP3.eventYouAreSelected(false);

			panelBestellungstatusTopQP3
					.updateButtons(panelBestellungstatusBottomD3
							.getLockedstateDetailMainKey());
			break;
		}
		switch (selectedIndex) {
		case IDX_PANEL_BESTELLUNGTEXT:
			refreshPanelBestellungtext();

			panelQueryBestellungtext.setDefaultFilter(BestellungFilterFactory
					.getInstance().createFKBestellungtext());
			panelQueryBestellungtext.eventYouAreSelected(false);

			// wenn es fuer das tabbed pane noch keinen Eintrag gibt,
			// die restlichen oberen Laschen deaktivieren, ausser ...
			if (panelQueryBestellungtext.getSelectedId() == null) {
				getInternalFrame().enableAllOberePanelsExceptMe(this,
						IDX_PANEL_BESTELLUNGTEXT, false);
			}

			panelQueryBestellungtext.updateButtons(panelBottomBestellungtext
					.getLockedstateDetailMainKey());
			break;
		}
		switch (selectedIndex) {
		case IDX_PANEL_BESTELLPOSITONSART:
			refreshBestellpositionsart();
			panelBestellungpositionsartTopQP4.eventYouAreSelected(false);
			panelBestellungpositionsartTopQP4
					.updateButtons(panelBestellungpositionsartBottomD4
							.getLockedstateDetailMainKey());

			// wenn es fuer das tabbed pane noch keinen Eintrag gibt,
			// die restlichen oberen Laschen deaktivieren, ausser ...
			if (panelBestellungpositionsartTopQP4.getSelectedId() == null) {
				getInternalFrame().enableAllOberePanelsExceptMe(this,
						IDX_PANEL_BESTELLPOSITONSART, false);
			}
			break;
		}

		switch (selectedIndex) {
		case IDX_PANEL_MAHNTEXT:
			refreshPanelMahntext();
			panelQueryMahntext.setDefaultFilter(BestellungFilterFactory
					.getInstance().createFKMahntext());
			panelQueryMahntext.eventYouAreSelected(false);

			// wenn es fuer das tabbed pane noch keinen Eintrag gibt,
			// die restlichen oberen Laschen deaktivieren, ausser ...
			if (panelQueryMahntext.getSelectedId() == null) {
				getInternalFrame().enableAllOberePanelsExceptMe(this,
						IDX_PANEL_MAHNTEXT, false);
			}
			break;
		}
		switch (selectedIndex) {
		case IDX_PANEL_MAHNGRUPPE:
			refreshPanelMahngruppe();
			panelMahngruppeTopQP4.setDefaultFilter(BestellungFilterFactory
					.getInstance().createFKMahngruppe());
			panelMahngruppeTopQP4.eventYouAreSelected(false);

			
			break;
		}
		switch (selectedIndex) {
		case IDX_PANEL_MAHNSTUFE:
			refreshMahnstufe();
			panelSplitMahnstufe.eventYouAreSelected(false);
			break;
		}
	}

	protected void lPActionEvent(java.awt.event.ActionEvent e) {
	}

	private void refreshPanelBestellungsart() throws Throwable {
		String[] aWhichStandardButtonIUse = {};

		if (panelBestellungsartSP1 == null) {
			panelBestellungsartTopQP1 = new PanelQuery(null, null,
					QueryParameters.UC_ID_BESTELLUNGART,
					aWhichStandardButtonIUse, getInternalFrame(), LPMain
							.getInstance().getTextRespectUISPr(
									"lp.bestellungart"), true);

			panelBestellungsartBottomD1 = new PanelStammdatenCRUD(
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("lp.bestellungart"), null,
					HelperClient.SCRUD_BESTELLUNGART_FILE,
					getInternalFrameBestellung(),
					HelperClient.LOCKME_BESTELLUNGART);

			/** @todo JO QS PJ 5210 */
			panelBestellungsartSP1 = new PanelSplit(getInternalFrame(),
					panelBestellungsartBottomD1, panelBestellungsartTopQP1, 200);

			setComponentAt(IDX_PANEL_BESTELLUNGART, panelBestellungsartSP1);
		}
	}

	private void refreshPanelMahngruppe() throws Throwable {
		String[] aWhichStandardButtonIUse = { PanelBasis.ACTION_NEW };

		if (panelMahngruppeSP4 == null) {
			panelMahngruppeTopQP4 = new PanelQuery(null, null,
					QueryParameters.UC_ID_MAHNGRUPPE,
					aWhichStandardButtonIUse, getInternalFrame(), LPMain
							.getInstance().getTextRespectUISPr(
									"lp.bestellungart"), true);

			panelMahngruppeBottomD4 = new PanelMahngruppe(
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("bes.mahngruppe"), null);

			panelMahngruppeSP4 = new PanelSplit(getInternalFrame(),
					panelMahngruppeBottomD4, panelMahngruppeTopQP4, 200);

			setComponentAt(IDX_PANEL_MAHNGRUPPE, panelMahngruppeSP4);
		}
	}

	private void refreshPanelBestellungtext() throws Throwable {
		if (panelSplitBestellungtext == null) {
			// der Kopftext muss in der Konzerndatensprache hinterlegt sein oder
			// werden
			DelegateFactory
					.getInstance()
					.getBestellungServiceDelegate()
					.bestellungtextFindByMandantLocaleCNr(
							LPMain.getInstance().getTheClient()
									.getLocUiAsString(),
							MediaFac.MEDIAART_KOPFTEXT);
			// der Fusstext muss in der Konzerndatensprache hinterlegt sein oder
			// werden
			DelegateFactory
					.getInstance()
					.getBestellungServiceDelegate()
					.bestellungtextFindByMandantLocaleCNr(
							LPMain.getInstance().getTheClient()
									.getLocUiAsString(),
							MediaFac.MEDIAART_FUSSTEXT);

			String[] aWhichStandardButtonIUse = null;

			panelQueryBestellungtext = new PanelQuery(null, null,
					QueryParameters.UC_ID_BESTELLUNGTEXT,
					aWhichStandardButtonIUse, getInternalFrame(), LPMain
							.getInstance().getTextRespectUISPr(
									"lp.bestellungtext"), true);

			panelBottomBestellungtext = new PanelStammdatenCRUD(
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("lp.bestellungtext"), null,
					HelperClient.SCRUD_BESTELLUNGTEXT_FILE,
					getInternalFrameBestellung(),
					HelperClient.LOCKME_BESTELLUNGTEXT);

			panelSplitBestellungtext = new PanelSplit(getInternalFrame(),
					panelBottomBestellungtext, panelQueryBestellungtext, 200);

			setComponentAt(IDX_PANEL_BESTELLUNGTEXT, panelSplitBestellungtext);
		}
	}

	private void refreshBestellpositionsart() throws Throwable {
		if (panelBestellungpositionsartSP4 == null) {
			panelBestellungpositionsartTopQP4 = new PanelQuery(null, null,
					QueryParameters.UC_ID_BESTELLPOSITIONART, null,
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("lp.bestellungpositionsart"),
					true);

			panelBestellungpositionsartBottomD4 = new PanelStammdatenCRUD(
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("lp.bestellungpositionsart"),
					null, HelperClient.SCRUD_BESTELLPOSITIONART_FILE,
					getInternalFrameBestellung(),
					HelperClient.LOCKME_BESTELLPOSITIONART);

			panelBestellungpositionsartSP4 = new PanelSplit(getInternalFrame(),
					panelBestellungpositionsartBottomD4,
					panelBestellungpositionsartTopQP4, 200);

			setComponentAt(IDX_PANEL_BESTELLPOSITONSART,
					panelBestellungpositionsartSP4);
		}
	}

	private void refreshPanelBestellungstatus() throws Throwable {
		if (panelBestellungstatusSP3 == null) {
			panelBestellungstatusTopQP3 = new PanelQuery(null, null,
					QueryParameters.UC_ID_BESTELLUNGSTATUS, null,
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("lp.bestellungstatus"), true);

			panelBestellungstatusBottomD3 = new PanelStammdatenCRUD(
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("lp.bestellungstatus"), null,
					HelperClient.SCRUD_BESTELLUNGSTATUS_FILE,
					getInternalFrameBestellung(),
					HelperClient.LOCKME_BESTELLUNGSTATUS);

			panelBestellungstatusSP3 = new PanelSplit(getInternalFrame(),
					panelBestellungstatusBottomD3, panelBestellungstatusTopQP3,
					200);

			setComponentAt(IDX_PANEL_BESTELLUNGSTATUS, panelBestellungstatusSP3);
		}
	}

	public void setBestellungartDto(BestellungsartDto bestellungartDtoI) {
		this.bestellungartDto = bestellungartDtoI;
	}

	public BestellungsartDto getBestellungartDto() {
		return bestellungartDto;
	}

	private void refreshPanelMahntext() throws Throwable {
		if (panelSplitMahntext == null) {
			BSMahntextDto bsmahntextDto1 = DelegateFactory
					.getInstance()
					.getBSMahnwesenDelegate()
					.bsmahntextFindByMandantLocaleCNr(
							LPMain.getInstance().getTheClient()
									.getLocUiAsString(),
							new Integer(BSMahnwesenFac.MAHNSTUFE_1));
			if (bsmahntextDto1 == null) {
				DelegateFactory
						.getInstance()
						.getBSMahnwesenDelegate()
						.createDefaultBSMahntext(
								new Integer(BSMahnwesenFac.MAHNSTUFE_1),
								BSMahnwesenFac.DEFAULT_TEXT_MAHNSTUFE_1);
			}
			BSMahntextDto bsmahntextDto2 = DelegateFactory
					.getInstance()
					.getBSMahnwesenDelegate()
					.bsmahntextFindByMandantLocaleCNr(
							LPMain.getInstance().getTheClient()
									.getLocUiAsString(),
							new Integer(BSMahnwesenFac.MAHNSTUFE_2));
			if (bsmahntextDto2 == null) {
				DelegateFactory
						.getInstance()
						.getBSMahnwesenDelegate()
						.createDefaultBSMahntext(
								new Integer(BSMahnwesenFac.MAHNSTUFE_2),
								BSMahnwesenFac.DEFAULT_TEXT_MAHNSTUFE_2);
			}
			BSMahntextDto bsmahntextDto3 = DelegateFactory
					.getInstance()
					.getBSMahnwesenDelegate()
					.bsmahntextFindByMandantLocaleCNr(
							LPMain.getInstance().getTheClient()
									.getLocUiAsString(),
							new Integer(BSMahnwesenFac.MAHNSTUFE_3));
			if (bsmahntextDto3 == null) {
				DelegateFactory
						.getInstance()
						.getBSMahnwesenDelegate()
						.createDefaultBSMahntext(
								new Integer(BSMahnwesenFac.MAHNSTUFE_3),
								BSMahnwesenFac.DEFAULT_TEXT_MAHNSTUFE_3);
			}
			BSMahntextDto bsmahntextDto0 = DelegateFactory
					.getInstance()
					.getBSMahnwesenDelegate()
					.bsmahntextFindByMandantLocaleCNr(
							LPMain.getInstance().getTheClient()
									.getLocUiAsString(),
							new Integer(BSMahnwesenFac.MAHNSTUFE_0));
			if (bsmahntextDto0 == null) {
				DelegateFactory
						.getInstance()
						.getBSMahnwesenDelegate()
						.createDefaultBSMahntext(
								new Integer(BSMahnwesenFac.MAHNSTUFE_0),
								BSMahnwesenFac.DEFAULT_TEXT_MAHNSTUFE_0);
			}
			String[] aWhichStandardButtonIUse = { PanelBasis.ACTION_NEW };

			panelQueryMahntext = new PanelQuery(null, null,
					QueryParameters.UC_ID_BESMAHNTEXT,
					aWhichStandardButtonIUse, getInternalFrame(), LPMain
							.getInstance().getTextRespectUISPr("lp.mahntext"),
					true);
			panelQueryMahntext.setDefaultFilter(SystemFilterFactory
					.getInstance().createFKMandantCNr());

			panelBottomMahntext = new PanelStammdatenCRUD(getInternalFrame(),
					LPMain.getInstance().getTextRespectUISPr("lp.status"),
					null, HelperClient.SCRUD_BSMAHNTEXT_FILE,
					(InternalFrameBestellung) getInternalFrame(),
					HelperClient.LOCKME_BESTELLUNG_MAHNTEXT);

			panelSplitMahntext = new PanelSplit(getInternalFrame(),
					panelBottomMahntext, panelQueryMahntext, 200);

			setComponentAt(IDX_PANEL_MAHNTEXT, panelSplitMahntext);
		}
	}

	private void refreshMahnstufe() throws Throwable {
		if (panelSplitMahnstufe == null) {
			BSMahnstufeDto bsmahnstufeDto1 = DelegateFactory
					.getInstance()
					.getBSMahnwesenDelegate()
					.bsmahnstufeFindByPrimaryKey(
							new Integer(BSMahnwesenFac.MAHNSTUFE_1));
			if (bsmahnstufeDto1 == null) {
				BSMahnstufeDto bsmahnstufeDto = new BSMahnstufeDto();
				bsmahnstufeDto.setIId(new Integer(BSMahnwesenFac.MAHNSTUFE_1));
				bsmahnstufeDto.setITage(new Integer(10));
				DelegateFactory.getInstance().getBSMahnwesenDelegate()
						.createBSMahnstufe(bsmahnstufeDto);
			}
			BSMahnstufeDto bsmahnstufeDto2 = DelegateFactory
					.getInstance()
					.getBSMahnwesenDelegate()
					.bsmahnstufeFindByPrimaryKey(
							new Integer(BSMahnwesenFac.MAHNSTUFE_2));
			if (bsmahnstufeDto2 == null) {
				BSMahnstufeDto bsmahnstufeDto = new BSMahnstufeDto();
				bsmahnstufeDto.setIId(new Integer(BSMahnwesenFac.MAHNSTUFE_2));
				bsmahnstufeDto.setITage(new Integer(10));
				DelegateFactory.getInstance().getBSMahnwesenDelegate()
						.createBSMahnstufe(bsmahnstufeDto);
			}
			BSMahnstufeDto bsmahnstufeDto3 = DelegateFactory
					.getInstance()
					.getBSMahnwesenDelegate()
					.bsmahnstufeFindByPrimaryKey(
							new Integer(BSMahnwesenFac.MAHNSTUFE_3));
			if (bsmahnstufeDto3 == null) {
				BSMahnstufeDto bsmahnstufeDto = new BSMahnstufeDto();
				bsmahnstufeDto.setIId(new Integer(BSMahnwesenFac.MAHNSTUFE_3));
				bsmahnstufeDto.setITage(new Integer(10));
				DelegateFactory.getInstance().getBSMahnwesenDelegate()
						.createBSMahnstufe(bsmahnstufeDto);
			}
			BSMahnstufeDto bsmahnstufeDto99 = DelegateFactory
					.getInstance()
					.getBSMahnwesenDelegate()
					.bsmahnstufeFindByPrimaryKey(
							new Integer(BSMahnwesenFac.MAHNSTUFE_0));
			if (bsmahnstufeDto99 == null) {
				BSMahnstufeDto bsmahnstufeDto = new BSMahnstufeDto();
				bsmahnstufeDto.setIId(new Integer(BSMahnwesenFac.MAHNSTUFE_0));
				bsmahnstufeDto.setITage(new Integer(10));
				DelegateFactory.getInstance().getBSMahnwesenDelegate()
						.createBSMahnstufe(bsmahnstufeDto);
			}

			BSMahnstufeDto bsmahnstufeDtoMinus1 = DelegateFactory
					.getInstance()
					.getBSMahnwesenDelegate()
					.bsmahnstufeFindByPrimaryKey(
							new Integer(BSMahnwesenFac.MAHNSTUFE_MINUS1));
			if (bsmahnstufeDtoMinus1 == null) {
				BSMahnstufeDto bsmahnstufeDto = new BSMahnstufeDto();
				bsmahnstufeDto.setIId(new Integer(
						BSMahnwesenFac.MAHNSTUFE_MINUS1));
				bsmahnstufeDto.setITage(new Integer(-2));
				DelegateFactory.getInstance().getBSMahnwesenDelegate()
						.createBSMahnstufe(bsmahnstufeDto);
			}
			String[] aWhichStandardButtonIUse = { PanelBasis.ACTION_NEW };
			panelQueryMahnstufe = new PanelQuery(null, null,
					QueryParameters.UC_ID_BESMAHNSTUFE,
					aWhichStandardButtonIUse, getInternalFrame(),
					LPMain.getInstance().getTextRespectUISPr("bes.mahnstufe"),
					true); // liste refresh wenn lasche geklickt wurde
			panelBottomMahnstufe = new PanelStammdatenCRUD(getInternalFrame(),
					LPMain.getInstance().getTextRespectUISPr("lp.detail"),
					null, HelperClient.SCRUD_BSMAHNSTUFE_FILE,
					(InternalFrameBestellung) getInternalFrame(),
					HelperClient.LOCKME_BESTELLUNG_MAHNSTUFE);
			panelSplitMahnstufe = new PanelSplit(getInternalFrame(),
					panelBottomMahnstufe, panelQueryMahnstufe, 200);
			setComponentAt(IDX_PANEL_MAHNSTUFE, panelSplitMahnstufe);
			panelQueryMahnstufe.setDefaultFilter(SystemFilterFactory
					.getInstance().createFKMandantCNr());
			// liste soll sofort angezeigt werden
			panelQueryMahnstufe.eventYouAreSelected(true);
		}
	}

	protected javax.swing.JMenuBar getJMenuBar() throws Throwable {
		return new WrapperMenuBar(this);
	}

	public Object getDto() {
		return bestellungartDto;
	}
}
