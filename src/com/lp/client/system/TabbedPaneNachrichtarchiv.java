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
package com.lp.client.system;

import javax.jms.JMSException;
import javax.naming.NamingException;
import javax.swing.JOptionPane;
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
import com.lp.server.util.fastlanereader.service.query.QueryParameters;

@SuppressWarnings("static-access")
/*
 * <p>&UUml;berschrift: </p> <p>Beschreibung: </p> <p>Copyright: Copyright (c)
 * 2004</p> <p>Organisation: </p>
 * 
 * @author Christian Kollmann
 * 
 * @version $Revision: 1.6 $
 */
public class TabbedPaneNachrichtarchiv extends TabbedPane {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private PanelQuery panelQueryNachrichtenarchiv = null;
	private PanelSplit panelSplitNachrichtenarchiv = null;
	private PanelBasis panelBottomNachrichtenarchiv = null;

	public static int IDX_PANEL_NACHRICHTARCHIV = -1;

	private static final String ACTION_SPECIAL_NACHRICHT_AN_ALLE = "action_special_nachricht_an_alle";
	private final String BUTTON_NACHRICHT_AN_ALLE = PanelBasis.ACTION_MY_OWN_NEW
			+ ACTION_SPECIAL_NACHRICHT_AN_ALLE;

	private WrapperMenuBar wrapperMenuBar = null;

	public TabbedPaneNachrichtarchiv(InternalFrame internalFrameI)
			throws Throwable {
		super(internalFrameI, LPMain.getInstance().getTextRespectUISPr(
				"ben.nachrichtenarchiv"));
		jbInit();
		initComponents();
	}

	public InternalFrameSystem getInternalFramePersonal() {
		return (InternalFrameSystem) getInternalFrame();
	}

	public PanelQuery getPanelNachrichtenarchiv() {
		return panelQueryNachrichtenarchiv;
	}

	private void createNachrichtart() throws Throwable {
		if (panelSplitNachrichtenarchiv == null) {

			panelQueryNachrichtenarchiv = new PanelQuery(
					null,
					SystemFilterFactory.getInstance().createFKNachrichtarchiv(),
					QueryParameters.UC_ID_NACHRICHTARCHIV, null,
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("ben.nachrichtenarchiv"), true);

			panelQueryNachrichtenarchiv.createAndSaveAndShowButton(
					"/com/lp/client/res/note_add16x16.png", LPMain
							.getInstance()
							.getTextRespectUISPr("jms.info.titel"),
					BUTTON_NACHRICHT_AN_ALLE, null);

			panelQueryNachrichtenarchiv
					.befuellePanelFilterkriterienDirektUndVersteckte(
							SystemFilterFactory.getInstance().createFKDNachrichtarchivNachricht(), null,
							null,LPMain
							.getInstance()
							.getTextRespectUISPr("system.alle.nachrichtenanzeigen"));
			

			panelBottomNachrichtenarchiv = new PanelNachrichtarchiv(
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("ben.nachrichtenarchiv"));

			panelSplitNachrichtenarchiv = new PanelSplit(getInternalFrame(),
					panelBottomNachrichtenarchiv, panelQueryNachrichtenarchiv,
					300);

			panelQueryNachrichtenarchiv.befuelleFilterkriteriumSchnellansicht(SystemFilterFactory.getInstance().createFKSchnellansichtNachrichtarchiv());
			
			setComponentAt(IDX_PANEL_NACHRICHTARCHIV,
					panelSplitNachrichtenarchiv);
		}
	}

	private void jbInit() throws Throwable {
		int tabIndex = 0;
		IDX_PANEL_NACHRICHTARCHIV = tabIndex;

		insertTab(LPMain.getInstance().getTextRespectUISPr(
				"ben.nachrichtenarchiv"), null, null, LPMain.getInstance()
				.getTextRespectUISPr("ben.nachrichtenarchiv"),
				IDX_PANEL_NACHRICHTARCHIV);
		tabIndex++;

		createNachrichtart();
		// Itemevents an MEIN Detailpanel senden kann.
		getInternalFrame().addItemChangedListener(this);
		// refreshTitle();
		this.addChangeListener(this);

	}

	public void lPEventItemChanged(ItemChangedEvent e) throws Throwable {

		if (e.getID() == ItemChangedEvent.ITEM_CHANGED) {
			if (e.getSource() == panelQueryNachrichtenarchiv) {
				Integer iId = (Integer) panelQueryNachrichtenarchiv
						.getSelectedId();
				getInternalFrame().setKeyWasForLockMe(iId + "");
				panelBottomNachrichtenarchiv.setKeyWhenDetailPanel(iId);
				panelBottomNachrichtenarchiv.eventYouAreSelected(false);
				panelQueryNachrichtenarchiv.updateButtons();
			}

		} else if (e.getID() == ItemChangedEvent.ACTION_YOU_ARE_SELECTED) {
			refreshTitle();
			panelSplitNachrichtenarchiv.eventYouAreSelected(false);
		} else if (e.getID() == ItemChangedEvent.ACTION_MY_OWN_NEW) {
			String sAspectInfo = ((ISourceEvent) e.getSource()).getAspect();

			if (sAspectInfo.endsWith(ACTION_SPECIAL_NACHRICHT_AN_ALLE)) {
				try {
					String meldung = JOptionPane
							.showInputDialog("Meldung eingeben:");
					if (meldung.length() > 0)
						LPMain.getInstance().getInfoTopic().send2AllUser(
								meldung);
				} catch (Throwable e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}

		} else if (e.getID() == ItemChangedEvent.ACTION_NEW) {
			if (e.getSource() == panelQueryNachrichtenarchiv) {
				panelBottomNachrichtenarchiv.eventActionNew(e, true, false);
				panelBottomNachrichtenarchiv.eventYouAreSelected(false);
			}

		} else if (e.getID() == ItemChangedEvent.ACTION_SAVE) {
			if (e.getSource() == panelBottomNachrichtenarchiv) {
				Object oKey = panelBottomNachrichtenarchiv
						.getKeyWhenDetailPanel();
				panelQueryNachrichtenarchiv.eventYouAreSelected(false);
				panelQueryNachrichtenarchiv.setSelectedId(oKey);
				panelSplitNachrichtenarchiv.eventYouAreSelected(false);

			}
		} else if (e.getID() == ItemChangedEvent.ACTION_DISCARD) {
			if (e.getSource() == panelBottomNachrichtenarchiv) {
				panelSplitNachrichtenarchiv.eventYouAreSelected(false);
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_UPDATE) {
			if (e.getSource() == panelBottomNachrichtenarchiv) {
				panelQueryNachrichtenarchiv.updateButtons(new LockStateValue(
						PanelBasis.LOCK_FOR_NEW));
			}
		}

		else if (e.getID() == ItemChangedEvent.ACTION_GOTO_MY_DEFAULT_QP) {
			if (e.getSource() == panelBottomNachrichtenarchiv) {
				setKeyWasForLockMe();
				if (panelBottomNachrichtenarchiv.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryNachrichtenarchiv
							.getId2SelectAfterDelete();
					panelQueryNachrichtenarchiv.setSelectedId(oNaechster);
				}

				panelSplitNachrichtenarchiv.eventYouAreSelected(false);
			}
		}

	}

	public void setKeyWasForLockMe() {
		Object oKey = panelQueryNachrichtenarchiv.getSelectedId();

		if (oKey != null) {
			getInternalFrame().setKeyWasForLockMe(oKey.toString());
		} else {
			getInternalFrame().setKeyWasForLockMe(null);
		}
	}

	private void refreshTitle() {

		getInternalFrame().setLpTitle(
				InternalFrame.TITLE_IDX_OHRWASCHLUNTEN,
				LPMain.getInstance().getTextRespectUISPr(
						"ben.nachrichtenarchiv"));
		getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_OHRWASCHLOBEN,
				((PanelBasis) this.getSelectedComponent()).getAdd2Title());
		getInternalFrame().setLpTitle(3, "");

	}

	public void lPEventObjectChanged(ChangeEvent e) throws Throwable {
		super.lPEventObjectChanged(e);
		int selectedIndex = this.getSelectedIndex();

		if (selectedIndex == IDX_PANEL_NACHRICHTARCHIV) {
			createNachrichtart();
			panelSplitNachrichtenarchiv.eventYouAreSelected(false);
			panelQueryNachrichtenarchiv.updateButtons();
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
