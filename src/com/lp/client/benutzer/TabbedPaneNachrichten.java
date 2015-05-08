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
package com.lp.client.benutzer;

import javax.swing.event.ChangeEvent;

import com.lp.client.frame.LockStateValue;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQuery;
import com.lp.client.frame.component.PanelSplit;
import com.lp.client.frame.component.TabbedPane;
import com.lp.client.frame.component.WrapperMenuBar;
import com.lp.client.pc.LPMain;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;

@SuppressWarnings("static-access")
/*
 * <p>&UUml;berschrift: </p> <p>Beschreibung: </p> <p>Copyright: Copyright (c)
 * 2004</p> <p>Organisation: </p>
 * 
 * @author Christian Kollmann
 * 
 * @version $Revision: 1.3 $
 */
public class TabbedPaneNachrichten extends TabbedPane {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private PanelQuery panelQueryNachrichtenart = null;
	private PanelBasis panelSplitNachrichtenart = null;
	private PanelBasis panelBottomNachrichtenart = null;

	private PanelQuery panelQueryThema = null;
	private PanelBasis panelSplitThema = null;
	private PanelBasis panelBottomThema = null;

	private PanelQuery panelQueryThemarolle = null;
	private PanelBasis panelSplitThemarolle = null;
	private PanelBasis panelBottomThemarolle = null;

	private static int IDX_PANEL_NACHRICHTART = -1;
	private static int IDX_PANEL_THEMAROLLE = -1;
	private static int IDX_PANEL_THEMA = -1;

	private WrapperMenuBar wrapperMenuBar = null;

	public TabbedPaneNachrichten(InternalFrame internalFrameI) throws Throwable {
		super(internalFrameI, LPMain.getInstance().getTextRespectUISPr(
				"ben.nachrichten"));
		jbInit();
		initComponents();
	}

	public InternalFrameBenutzer getInternalFramePersonal() {
		return (InternalFrameBenutzer) getInternalFrame();
	}

	private void createNachrichtart() throws Throwable {
		if (panelSplitNachrichtenart == null) {
			
			panelQueryNachrichtenart = new PanelQuery(null, null,
					QueryParameters.UC_ID_NACHRICHTART, null,
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("ben.nachrichtenart"), true);

			panelQueryNachrichtenart.befuellePanelFilterkriterienDirekt(
					BenutzerFilterFactory.getInstance()
							.createFKDNachrichtartKennung(), null);

			panelBottomNachrichtenart = new PanelNachrichtart(
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("ben.nachrichtenart"),
					panelQueryNachrichtenart.getSelectedId());

			panelSplitNachrichtenart = new PanelSplit(getInternalFrame(),
					panelBottomNachrichtenart, panelQueryNachrichtenart, 300);

			setComponentAt(IDX_PANEL_NACHRICHTART, panelSplitNachrichtenart);
		}
	}

	private void createThemarolle() throws Throwable {
		if (panelSplitThemarolle == null) {
			String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW };
			panelQueryThemarolle = new PanelQuery(null, null,
					QueryParameters.UC_ID_THEMAROLLE, aWhichButtonIUse,
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("ben.themarolle"), true);

			panelQueryThemarolle.befuellePanelFilterkriterienDirekt(
					BenutzerFilterFactory.getInstance()
							.createFKDThemarolle(), null);

			panelBottomThemarolle = new PanelThemarolle(getInternalFrame(),
					LPMain.getInstance().getTextRespectUISPr("ben.themarolle"),
					panelQueryThemarolle.getSelectedId());

			panelSplitThemarolle = new PanelSplit(getInternalFrame(),
					panelBottomThemarolle, panelQueryThemarolle, 300);

			setComponentAt(IDX_PANEL_THEMAROLLE, panelSplitThemarolle);
		}
	}

	private void createThema() throws Throwable {
		if (panelSplitThema == null) {
			
			panelQueryThema = new PanelQuery(null, null,
					QueryParameters.UC_ID_THEMA, null,
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("ben.thema"), true);

			panelQueryThema.befuellePanelFilterkriterienDirekt(
					BenutzerFilterFactory.getInstance()
							.createFKDNachrichtartKennung(), null);

			panelBottomThema = new PanelThema(getInternalFrame(), LPMain
					.getInstance().getTextRespectUISPr("ben.thema"),
					panelQueryThema.getSelectedId());

			panelSplitThema = new PanelSplit(getInternalFrame(),
					panelBottomThema, panelQueryThema, 300);

			setComponentAt(IDX_PANEL_THEMA, panelSplitThema);
		}
	}

	private void jbInit() throws Throwable {
		int tabIndex = 0;
		IDX_PANEL_NACHRICHTART = tabIndex;

		insertTab(LPMain.getInstance()
				.getTextRespectUISPr("ben.nachrichtenart"), null, null, LPMain
				.getInstance().getTextRespectUISPr("ben.nachrichtenart"),
				IDX_PANEL_NACHRICHTART);
		tabIndex++;
		IDX_PANEL_THEMAROLLE = tabIndex;
		insertTab(LPMain.getInstance().getTextRespectUISPr("ben.themarolle"),
				null, null, LPMain.getInstance().getTextRespectUISPr(
						"ben.themarolle"), IDX_PANEL_THEMAROLLE);
		tabIndex++;
		IDX_PANEL_THEMA = tabIndex;
		insertTab(LPMain.getInstance().getTextRespectUISPr("ben.thema"), null,
				null, LPMain.getInstance().getTextRespectUISPr("ben.thema"),
				IDX_PANEL_THEMA);

		createNachrichtart();
		// Itemevents an MEIN Detailpanel senden kann.
		getInternalFrame().addItemChangedListener(this);
		// refreshTitle();
		this.addChangeListener(this);

	}

	public void lPEventItemChanged(ItemChangedEvent e) throws Throwable {

		if (e.getID() == ItemChangedEvent.ITEM_CHANGED) {
			if (e.getSource() == panelQueryNachrichtenart) {
				Integer iId = (Integer) panelQueryNachrichtenart
						.getSelectedId();
				getInternalFrame().setKeyWasForLockMe(iId + "");
				panelBottomNachrichtenart.setKeyWhenDetailPanel(iId);
				panelBottomNachrichtenart.eventYouAreSelected(false);
				panelQueryNachrichtenart.updateButtons();
			} else if (e.getSource() == panelQueryThemarolle) {
				Integer iId = (Integer) panelQueryThemarolle.getSelectedId();
				getInternalFrame().setKeyWasForLockMe(iId + "");
				panelBottomThemarolle.setKeyWhenDetailPanel(iId);
				panelBottomThemarolle.eventYouAreSelected(false);
				panelQueryThemarolle.updateButtons();
			} else if (e.getSource() == panelQueryThema) {
				String cNr = (String) panelQueryThema.getSelectedId();
				getInternalFrame().setKeyWasForLockMe(cNr + "");
				panelBottomThema.setKeyWhenDetailPanel(cNr);
				panelBottomThema.eventYouAreSelected(false);
				panelQueryThema.updateButtons();
			}

		} else if (e.getID() == ItemChangedEvent.ACTION_YOU_ARE_SELECTED) {
			refreshTitle();
			panelSplitNachrichtenart.eventYouAreSelected(false);
		} else if (e.getID() == ItemChangedEvent.ACTION_NEW) {
			if (e.getSource() == panelQueryNachrichtenart) {
				panelBottomNachrichtenart.eventActionNew(e, true, false);
				panelBottomNachrichtenart.eventYouAreSelected(false);
			} else if (e.getSource() == panelQueryThemarolle) {
				panelBottomThemarolle.eventActionNew(e, true, false);
				panelBottomThemarolle.eventYouAreSelected(false);
			} else if (e.getSource() == panelQueryThema) {
				panelBottomThema.eventActionNew(e, true, false);
				panelBottomThema.eventYouAreSelected(false);
			}

		} else if (e.getID() == ItemChangedEvent.ACTION_SAVE) {
			if (e.getSource() == panelBottomNachrichtenart) {
				Object oKey = panelBottomNachrichtenart.getKeyWhenDetailPanel();
				panelQueryNachrichtenart.eventYouAreSelected(false);
				panelQueryNachrichtenart.setSelectedId(oKey);
				panelSplitNachrichtenart.eventYouAreSelected(false);

			} else if (e.getSource() == panelBottomThemarolle) {
				Object oKey = panelBottomThemarolle.getKeyWhenDetailPanel();
				panelQueryThemarolle.eventYouAreSelected(false);
				panelQueryThemarolle.setSelectedId(oKey);
				panelSplitThemarolle.eventYouAreSelected(false);

			} else if (e.getSource() == panelBottomThema) {
				Object oKey = panelBottomThema.getKeyWhenDetailPanel();
				panelQueryThema.eventYouAreSelected(false);
				panelQueryThema.setSelectedId(oKey);
				panelSplitThema.eventYouAreSelected(false);

			}
		} else if (e.getID() == ItemChangedEvent.ACTION_DISCARD) {
			if (e.getSource() == panelBottomNachrichtenart) {
				panelSplitNachrichtenart.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomThemarolle) {
				panelSplitThemarolle.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomThema) {
				panelSplitThema.eventYouAreSelected(false);
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_UPDATE) {
			if (e.getSource() == panelBottomNachrichtenart) {
				panelQueryNachrichtenart.updateButtons(new LockStateValue(
						PanelBasis.LOCK_FOR_NEW));
			} else if (e.getSource() == panelBottomThemarolle) {
				panelQueryThemarolle.updateButtons(new LockStateValue(
						PanelBasis.LOCK_FOR_NEW));
			} else if (e.getSource() == panelBottomThema) {
				panelQueryThema.updateButtons(new LockStateValue(
						PanelBasis.LOCK_FOR_NEW));
			}
		}

		else if (e.getID() == ItemChangedEvent.ACTION_GOTO_MY_DEFAULT_QP) {
			if (e.getSource() == panelBottomNachrichtenart) {
				setKeyWasForLockMe();
				if (panelBottomNachrichtenart.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryNachrichtenart
							.getId2SelectAfterDelete();
					panelQueryNachrichtenart.setSelectedId(oNaechster);
				}

				panelSplitNachrichtenart.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomThemarolle) {
				setKeyWasForLockMe();
				if (panelBottomThemarolle.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryThemarolle
							.getId2SelectAfterDelete();
					panelQueryThemarolle.setSelectedId(oNaechster);
				}

				panelSplitThemarolle.eventYouAreSelected(false);
			}
		}

	}

	public void setKeyWasForLockMe() {
		Object oKey = panelQueryNachrichtenart.getSelectedId();

		if (oKey != null) {
			getInternalFrame().setKeyWasForLockMe(oKey.toString());
		} else {
			getInternalFrame().setKeyWasForLockMe(null);
		}
	}

	private void refreshTitle() {

		getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_OHRWASCHLUNTEN,
				LPMain.getInstance().getTextRespectUISPr("ben.nachrichten"));
		getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_OHRWASCHLOBEN,
				((PanelBasis) this.getSelectedComponent()).getAdd2Title());
		getInternalFrame().setLpTitle(3, "");

	}

	public void lPEventObjectChanged(ChangeEvent e) throws Throwable {
		super.lPEventObjectChanged(e);
		int selectedIndex = this.getSelectedIndex();

		if (selectedIndex == IDX_PANEL_NACHRICHTART) {
			createNachrichtart();
			panelSplitNachrichtenart.eventYouAreSelected(false);
			panelQueryNachrichtenart.updateButtons();
		} else if (selectedIndex == IDX_PANEL_THEMAROLLE) {
			createThemarolle();
			panelSplitThemarolle.eventYouAreSelected(false);
			panelQueryThemarolle.updateButtons();
		} else if (selectedIndex == IDX_PANEL_THEMA) {
			createThema();
			panelSplitThema.eventYouAreSelected(false);
			panelQueryThema.updateButtons();
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
