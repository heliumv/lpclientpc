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

import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.LockStateValue;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQuery;
import com.lp.client.frame.component.PanelSplit;
import com.lp.client.frame.component.TabbedPane;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperMenuBar;
import com.lp.client.pc.LPMain;
import com.lp.client.util.fastlanereader.gui.QueryType;
import com.lp.server.system.service.LockMeDto;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;

@SuppressWarnings("static-access")
/**
 * <p>Ueberschrift: </p>
 * <p>Beschreibung: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Organisation: </p>
 * @author unbekannt
 * @version $Revision: 1.5 $
 */
public class TabbedPaneGesperrt extends TabbedPane {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private PanelQuery panelQueryTheJudge = null;

	private final static int IDX_LOCKME = 0;
	private final static int IDX_THECLIENT = 1;

	private PanelQuery panelLockMeQP1 = null;
	private PanelLockMe panelLockMeBottomD1 = null;
	private PanelSplit panelLockMeSP1 = null;

	private PanelQuery panelTheClientQP2 = null;
	private PanelTheClient panelTheClientBottomD2 = null;
	private PanelSplit panelTheClientSP2 = null;

	private WrapperCheckBox wcbNurAngemeldete = null ;
	
	public TabbedPaneGesperrt(InternalFrame internalFrameI) throws Throwable {
		super(internalFrameI, LPMain.getInstance().getTextRespectUISPr(
				"pers.benutzer.gesperrt"));
		jbInit();
		initComponents();
	}

	private void jbInit() throws Throwable {
		// Tab 1: Rechte
		insertTab(
				LPMain.getInstance().getTextRespectUISPr(
						"pers.benutzer.gesperrt"),
				null,
				null,
				LPMain.getInstance().getTextRespectUISPr(
						"pers.benutzer.gesperrt"), IDX_LOCKME);
		insertTab(LPMain.getInstance().getTextRespectUISPr("lp.loggedin"),
				null, null,
				LPMain.getInstance().getTextRespectUISPr("lp.loggedin"),
				IDX_THECLIENT);
		setSelectedComponent(getPanelQueryTheJudge());
		// refresh
		getPanelQueryTheJudge().eventYouAreSelected(false);
		// damit gleich eine selektiert ist
		ItemChangedEvent it = new ItemChangedEvent(getPanelQueryTheJudge(),
				ItemChangedEvent.ITEM_CHANGED);
		lPEventItemChanged(it);
		// Listener
		addChangeListener(this);
		getInternalFrame().addItemChangedListener(this);
	}

	private PanelQuery getPanelQueryTheJudge() throws Throwable {
		if (panelQueryTheJudge == null) {
			String[] aWhichButtonIUseQPTheJudge = { PanelBasis.ACTION_REFRESH };

			QueryType[] qtTheJudge = null;
			FilterKriterium[] filtersTheJudge = null;

			panelQueryTheJudge = new PanelQuery(qtTheJudge, filtersTheJudge,
					QueryParameters.UC_ID_THEJUDGE, aWhichButtonIUseQPTheJudge,
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("pers.benutzer.gesperrt"),
					true);
			this.setComponentAt(IDX_LOCKME, panelQueryTheJudge);
		}
		return panelQueryTheJudge;
	}

	public void lPEventItemChanged(ItemChangedEvent eI) throws Throwable {

		ItemChangedEvent e = (ItemChangedEvent) eI;

		if (e.getID() == ItemChangedEvent.ITEM_CHANGED) {
			if (e.getSource() == panelLockMeQP1) {
				Object key = (LockMeDto) panelLockMeQP1.getSelectedId();
				// key 1; IF
				getInternalFrame().setKeyWasForLockMe(key + "");
				// key2; PB
				panelLockMeBottomD1.setKeyWhenDetailPanel(key);
				panelLockMeBottomD1.eventYouAreSelected(false);
				panelLockMeQP1.updateButtons();
			} else if (e.getSource() == panelTheClientQP2) {
				Object key = panelTheClientQP2.getSelectedId();
				getInternalFrame().setKeyWasForLockMe(key + "");
				panelTheClientBottomD2.setKeyWhenDetailPanel(key);
				panelTheClientBottomD2.eventYouAreSelected(false);
				panelTheClientQP2.updateButtons();
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_GOTO_MY_DEFAULT_QP) {
			if (e.getSource() == panelLockMeBottomD1) {
				Object oKey = panelLockMeQP1.getSelectedId();
				// holt sich alten key wieder
				getInternalFrame().setKeyWasForLockMe(
						oKey == null ? null : oKey + "");
				panelLockMeSP1.eventYouAreSelected(false); // refresh auf das
															// gesamte 1:n panel
			} else if (e.getSource() == panelTheClientBottomD2) {
				Object oKey = panelTheClientQP2.getSelectedId();
				// holt sich alten key wieder
				getInternalFrame().setKeyWasForLockMe(
						oKey == null ? null : oKey + "");
				panelTheClientSP2.eventYouAreSelected(false); // refresh auf das
																// gesamte 1:n
																// panel
			}
		} else if (eI.getID() == ItemChangedEvent.ACTION_UPDATE) {
			// hier kommt man nach upd im D bei einem 1:n hin.
			if (eI.getSource() == panelLockMeBottomD1) {
				// 2 im QP die Buttons in den Zustand neu setzen.
				panelLockMeQP1.updateButtons(new LockStateValue(
						PanelBasis.LOCK_FOR_NEW));
				;
			} else if (eI.getSource() == panelTheClientBottomD2) {
				panelTheClientQP2.updateButtons(new LockStateValue(
						PanelBasis.LOCK_FOR_NEW));
				;
			}
		} else if (eI.getID() == ItemChangedEvent.ACTION_PRINT) {

			if (e.getSource().equals(panelTheClientQP2)) {
				getInternalFrame().showReportKriterien(
						new ReportBenutzerstatistik(getInternalFrame(), ""));
			}
		}
	}

	public void lPEventObjectChanged(ChangeEvent e) throws Throwable {
		super.lPEventObjectChanged(e);
		int selectedIndex = this.getSelectedIndex();

		switch (selectedIndex) {
		case IDX_LOCKME: {
			refreshLockMeSP1();
			panelLockMeQP1.eventYouAreSelected(false);

			// im QP die Buttons setzen.
			panelLockMeQP1.updateButtons();

			break;
		}

		case IDX_THECLIENT: {
			refreshTheClientSP2();
			panelTheClientQP2.eventYouAreSelected(false);

			// im QP die Buttons setzen.
			panelTheClientQP2.updateButtons();

			break;
		}
		}
	}

	protected void lPActionEvent(java.awt.event.ActionEvent e) {
		if (e.getSource() == wcbNurAngemeldete) {
			if(null == panelTheClientQP2 || null == panelTheClientSP2) return ;
			
			try {
				panelTheClientQP2.setDefaultFilter(isNurAngemeldete() ?
					SystemFilterFactory.getInstance().createFKPanelOnlyLoggedIn() :
					SystemFilterFactory.getInstance().createFKPanelGesperrt()) ;	
				panelTheClientSP2.eventYouAreSelected(false) ;
			} catch(Throwable t) {}
		}
	}

	private void refreshLockMeSP1() throws Throwable {
		if (panelLockMeSP1 == null) {
			panelLockMeQP1 = new PanelQuery(null, null,
					QueryParameters.UC_ID_THEJUDGE, null, getInternalFrame(),
					LPMain.getInstance().getTextRespectUISPr(
							"pers.benutzer.gesperrt"), true); // liste refresh
																// wenn lasche
																// geklickt
																// wurde

			panelLockMeBottomD1 = new PanelLockMe(getInternalFrame(), "", null);

			panelLockMeSP1 = new PanelSplit(getInternalFrame(),
					panelLockMeBottomD1, panelLockMeQP1, 200);
			setComponentAt(IDX_LOCKME, panelLockMeSP1);

			// liste soll sofort angezeigt werden
			panelLockMeQP1.eventYouAreSelected(true);
		}
	}

	private WrapperCheckBox getNurAngemeldeteCheckbox() {
		if (null == wcbNurAngemeldete) {
			wcbNurAngemeldete = new WrapperCheckBox(
					LPMain.getTextRespectUISPr("lp.nurangemeldete"));
			wcbNurAngemeldete.setSelected(false);
			wcbNurAngemeldete.setActivatable(true);
			wcbNurAngemeldete.setEnabled(true);
			Dimension d = new Dimension(
					150, Defaults.getInstance().getControlHeight());
			wcbNurAngemeldete.setPreferredSize(d);
			wcbNurAngemeldete.setMinimumSize(d);
			wcbNurAngemeldete.setMnemonic('A');

			wcbNurAngemeldete.addActionListener(this);
		}

		return wcbNurAngemeldete;
	}
	
	private void createLoggedInButton(PanelQuery queryPanel) {
		try {
			JPanel toolsPanel = queryPanel.getToolBar().getToolsPanelCenter();
			toolsPanel.add(getNurAngemeldeteCheckbox());

			toolsPanel.validate();
		} catch (Exception e) {

		}
	}

	private boolean isNurAngemeldete() {
		return getNurAngemeldeteCheckbox().isSelected() ;
	}
	
	private void refreshTheClientSP2() throws Throwable {
		if (panelTheClientSP2 == null) {

			String[] aWhichButtonIUse = { PanelBasis.ACTION_PRINT };
			panelTheClientQP2 = new PanelQuery(null, 
					isNurAngemeldete() ?
							SystemFilterFactory.getInstance().createFKPanelOnlyLoggedIn() :
							SystemFilterFactory.getInstance().createFKPanelGesperrt(),
					QueryParameters.UC_ID_THECLIENT, aWhichButtonIUse,
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("lp.loggedin"), true); // liste
																		// refresh
																		// wenn
																		// lasche
																		// geklickt
																		// wurde

			createLoggedInButton(panelTheClientQP2) ;
			
			panelTheClientBottomD2 = new PanelTheClient(getInternalFrame(), "",
					null);

			panelTheClientSP2 = new PanelSplit(getInternalFrame(),
					panelTheClientBottomD2, panelTheClientQP2, 200);
			setComponentAt(IDX_THECLIENT, panelTheClientSP2);

			// liste soll sofort angezeigt werden
			panelTheClientQP2.eventYouAreSelected(true);
		}
	}

	protected javax.swing.JMenuBar getJMenuBar() throws Throwable {
		return new WrapperMenuBar(this);
	}
}
