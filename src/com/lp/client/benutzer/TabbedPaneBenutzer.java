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
 * @version $Revision: 1.4 $
 */
public class TabbedPaneBenutzer extends TabbedPane {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private PanelQuery panelQueryBenutzer = null;
	private PanelBasis panelSplitBenutzer = null;
	private PanelBasis panelBottomBenutzer = null;

	private WrapperMenuBar wrapperMenuBar = null;

	public TabbedPaneBenutzer(InternalFrame internalFrameI) throws Throwable {
		super(internalFrameI, LPMain.getInstance().getTextRespectUISPr(
				"lp.benutzer"));

		jbInit();
		initComponents();
	}

	public InternalFrameBenutzer getInternalFramePersonal() {
		return (InternalFrameBenutzer) getInternalFrame();
	}

	private void jbInit() throws Throwable {
		// Benutzer
		String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW };
		panelQueryBenutzer = new PanelQuery(null, BenutzerFilterFactory.getInstance().createFKOhneLpwebappzemecs(),
				QueryParameters.UC_ID_BENUZTER, aWhichButtonIUse,
				getInternalFrame(), LPMain.getInstance().getTextRespectUISPr(
						"benutzer.modulname"), true);
		panelBottomBenutzer = new PanelBenutzer(getInternalFrame(), LPMain
				.getInstance().getTextRespectUISPr("benutzer.modulname"), null);
		panelSplitBenutzer = new PanelSplit(getInternalFrame(),
				panelBottomBenutzer, panelQueryBenutzer, 250);
		addTab(LPMain.getInstance().getTextRespectUISPr("benutzer.modulname"),
				panelSplitBenutzer);

		panelQueryBenutzer.befuellePanelFilterkriterienDirekt(
				BenutzerFilterFactory.getInstance().createFKDBenutzerkennung(),
				null);

		// Damit D2 einen Aktuellen hat.
		ItemChangedEvent it = new ItemChangedEvent(panelQueryBenutzer,
				ItemChangedEvent.ITEM_CHANGED);
		lPEventItemChanged(it);

		// Itemevents an MEIN Detailpanel senden kann.
		getInternalFrame().addItemChangedListener(this);
		refreshTitle();
		this.addChangeListener(this);

	}

	public void setKeyWasForLockMe() {
		Object oKey = panelQueryBenutzer.getSelectedId();

		if (oKey != null) {
			getInternalFrame().setKeyWasForLockMe(oKey.toString());
		} else {
			getInternalFrame().setKeyWasForLockMe(null);
		}
	}

	public void lPEventItemChanged(ItemChangedEvent e) throws Throwable {

		if (e.getID() == ItemChangedEvent.ITEM_CHANGED) {
			if (e.getSource() == panelQueryBenutzer) {
				Integer iId = (Integer) panelQueryBenutzer.getSelectedId();
				getInternalFrame().setKeyWasForLockMe(iId + "");
				panelBottomBenutzer.setKeyWhenDetailPanel(iId);
				panelBottomBenutzer.eventYouAreSelected(false);
				panelQueryBenutzer.updateButtons();
			}

		} else if (e.getID() == ItemChangedEvent.ACTION_YOU_ARE_SELECTED) {
			refreshTitle();
			panelSplitBenutzer.eventYouAreSelected(false);
		} else if (e.getID() == ItemChangedEvent.ACTION_NEW) {
			if (e.getSource() == panelQueryBenutzer) {

				panelBottomBenutzer.eventActionNew(e, true, false);
				panelBottomBenutzer.eventYouAreSelected(false);
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_DISCARD) {
			if (e.getSource() == panelBottomBenutzer) {
				panelSplitBenutzer.eventYouAreSelected(false);
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_UPDATE) {
			if (e.getSource() == panelBottomBenutzer) {
				panelQueryBenutzer.updateButtons(new LockStateValue(
						PanelBasis.LOCK_FOR_NEW));
			}
		}

		else if (e.getID() == ItemChangedEvent.ACTION_SAVE) {
			if (e.getSource() == panelBottomBenutzer) {
				Object oKey = panelBottomBenutzer.getKeyWhenDetailPanel();
				panelQueryBenutzer.eventYouAreSelected(false);
				panelQueryBenutzer.setSelectedId(oKey);
				panelSplitBenutzer.eventYouAreSelected(false);

			}
		} else if (e.getID() == ItemChangedEvent.ACTION_GOTO_MY_DEFAULT_QP) {
			if (e.getSource() == panelBottomBenutzer) {
				Object oKey = panelQueryBenutzer.getSelectedId();
				if (oKey != null) {
					getInternalFrame().setKeyWasForLockMe(oKey.toString());
				} else {
					getInternalFrame().setKeyWasForLockMe(null);
				}
				if (panelBottomBenutzer.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryBenutzer
							.getId2SelectAfterDelete();
					panelQueryBenutzer.setSelectedId(oNaechster);
				}

				panelSplitBenutzer.eventYouAreSelected(false);
			}
		}

	}

	private void refreshTitle() {

		getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_OHRWASCHLUNTEN,
				LPMain.getInstance().getTextRespectUISPr("lp.benutzer"));
		getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_OHRWASCHLOBEN,
				((PanelBasis) this.getSelectedComponent()).getAdd2Title());
		getInternalFrame().setLpTitle(3, "");

	}

	public void lPEventObjectChanged(ChangeEvent e) throws Throwable {
		super.lPEventObjectChanged(e);
		panelSplitBenutzer.eventYouAreSelected(false);
		panelQueryBenutzer.updateButtons();
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
