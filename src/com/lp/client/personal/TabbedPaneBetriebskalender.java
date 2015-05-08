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

import com.lp.client.artikel.ArtikelFilterFactory;
import com.lp.client.artikel.PanelLager;
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
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.system.SystemFilterFactory;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;

@SuppressWarnings("static-access")
/**
 * <p>&UUml;berschrift: </p>
 * <p>Beschreibung: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Organisation: </p>
 * @author Christian Kollmann
 * @version 1.0
 */
public class TabbedPaneBetriebskalender extends TabbedPane {

	private final String BUTTON_FEIERTAGE_EINTRAGEN = PanelBasis.ACTION_MY_OWN_NEW
			+ "BUTTON_FEIERTAGE_EINTRAGEN";

	private static final long serialVersionUID = 1L;
	private PanelQuery panelQueryBetriebskalender = null;
	private PanelBasis panelSplitBetriebskalender = null;
	private PanelBasis panelBottomBetriebskalender = null;

	private PanelQuery panelQueryFeiertag = null;
	private PanelBasis panelSplitFeiertag = null;
	private PanelBasis panelBottomFeiertag = null;

	private int IDX_PANEL_BETRIEBSKALENDER = 0;
	private int IDX_PANEL_FEIERTAGE = 1;

	private WrapperMenuBar wrapperMenuBar = null;

	public TabbedPaneBetriebskalender(InternalFrame internalFrameI)
			throws Throwable {
		super(internalFrameI, LPMain.getInstance().getTextRespectUISPr(
				"pers.title.tab.betriebskalender"));

		jbInit();
		initComponents();
	}

	public InternalFramePersonal getInternalFramePersonal() {
		return (InternalFramePersonal) getInternalFrame();
	}

	private void jbInit() throws Throwable {
		// Betriebskalender
		String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW,
				PanelBasis.ACTION_FILTER };

		panelQueryBetriebskalender = new PanelQuery(PersonalFilterFactory
				.getInstance().createQTBetriebskalender(), SystemFilterFactory
				.getInstance().createFKMandantCNr(),
				QueryParameters.UC_ID_BETRIEBSKALENDER, aWhichButtonIUse,
				getInternalFrame(), LPMain.getInstance().getTextRespectUISPr(
						"pers.title.tab.betriebskalender"), true);
		panelQueryBetriebskalender.eventYouAreSelected(false);

		panelQueryBetriebskalender.befuellePanelFilterkriterienDirekt(
				PersonalFilterFactory.getInstance()
						.createFKDBetriebskalenderBezeichnung(), null);

		panelBottomBetriebskalender = new PanelBetriebskalender(
				getInternalFrame(), LPMain.getInstance().getTextRespectUISPr(
						"pers.title.tab.betriebskalender"), null);
		panelSplitBetriebskalender = new PanelSplit(getInternalFrame(),
				panelBottomBetriebskalender, panelQueryBetriebskalender, 330);

		insertTab(
				LPMain.getInstance().getTextRespectUISPr(
						"pers.title.tab.betriebskalender"),
				null,
				panelSplitBetriebskalender,
				LPMain.getInstance().getTextRespectUISPr(
						"pers.title.tab.betriebskalender"),
				IDX_PANEL_BETRIEBSKALENDER);

		insertTab(
				LPMain.getInstance().getTextRespectUISPr(
						"pers.feiertag.vorlage"),
				null,
				null,
				LPMain.getInstance().getTextRespectUISPr(
						"pers.feiertag.vorlage"), IDX_PANEL_FEIERTAGE);

		// Itemevents an MEIN Detailpanel senden kann.
		getInternalFrame().addItemChangedListener(this);
		this.addChangeListener(this);
	}

	public void lPEventItemChanged(ItemChangedEvent e) throws Throwable {

		if (e.getID() == ItemChangedEvent.ITEM_CHANGED) {
			if (e.getSource() == panelQueryBetriebskalender) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				panelBottomBetriebskalender.setKeyWhenDetailPanel(key);
				panelBottomBetriebskalender.eventYouAreSelected(false);
				panelQueryBetriebskalender.updateButtons();
			}

			if (e.getSource() == panelQueryFeiertag) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				panelBottomFeiertag.setKeyWhenDetailPanel(key);
				panelBottomFeiertag.eventYouAreSelected(false);
				panelQueryFeiertag.updateButtons();
			}

		} else if (e.getID() == ItemChangedEvent.ACTION_YOU_ARE_SELECTED) {
			refreshTitle();
			panelSplitBetriebskalender.eventYouAreSelected(false);
		} else if (e.getID() == ItemChangedEvent.ACTION_NEW) {
			if (e.getSource() == panelQueryBetriebskalender) {

				panelBottomBetriebskalender.eventActionNew(e, true, false);
				panelBottomBetriebskalender.eventYouAreSelected(false);

			} else if (e.getSource() == panelQueryFeiertag) {

				panelBottomFeiertag.eventActionNew(e, true, false);
				panelBottomFeiertag.eventYouAreSelected(false);

			}
		} else if (e.getID() == ItemChangedEvent.ACTION_DISCARD) {
			if (e.getSource() == panelBottomBetriebskalender) {
				panelSplitBetriebskalender.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomFeiertag) {
				panelSplitFeiertag.eventYouAreSelected(false);
			}

		} else if (e.getID() == ItemChangedEvent.ACTION_UPDATE) {
			if (e.getSource() == panelBottomBetriebskalender) {
				panelQueryBetriebskalender.updateButtons(new LockStateValue(
						PanelBasis.LOCK_FOR_NEW));

			} else if (e.getSource() == panelBottomFeiertag) {
				panelQueryFeiertag.updateButtons(new LockStateValue(
						PanelBasis.LOCK_FOR_NEW));

			}
		} else if (e.getID() == ItemChangedEvent.ACTION_SAVE) {
			if (e.getSource() == panelBottomBetriebskalender) {
				Object oKey = panelBottomBetriebskalender
						.getKeyWhenDetailPanel();
				panelQueryBetriebskalender.eventYouAreSelected(false);
				panelQueryBetriebskalender.setSelectedId(oKey);
				panelSplitBetriebskalender.eventYouAreSelected(false);

			} else if (e.getSource() == panelBottomFeiertag) {
				Object oKey = panelBottomFeiertag.getKeyWhenDetailPanel();
				panelQueryFeiertag.eventYouAreSelected(false);
				panelQueryFeiertag.setSelectedId(oKey);
				panelSplitFeiertag.eventYouAreSelected(false);

			}

		} else if (e.getID() == ItemChangedEvent.ACTION_GOTO_MY_DEFAULT_QP) {
			if (e.getSource() == panelBottomBetriebskalender) {
				setKeyWasForLockMe();
				if (panelBottomBetriebskalender.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryBetriebskalender
							.getId2SelectAfterDelete();
					panelQueryBetriebskalender.setSelectedId(oNaechster);
				}
				panelSplitBetriebskalender.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomFeiertag) {
				setKeyWasForLockMe();
				if (panelBottomFeiertag.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryFeiertag
							.getId2SelectAfterDelete();
					panelQueryFeiertag.setSelectedId(oNaechster);
				}
				panelSplitFeiertag.eventYouAreSelected(false);
			}

		} else if (e.getID() == ItemChangedEvent.ACTION_MY_OWN_NEW) {
			String sAspectInfo = ((ISourceEvent) e.getSource()).getAspect();
			if (sAspectInfo.endsWith(BUTTON_FEIERTAGE_EINTRAGEN)) {
				// Jahr eingeben

				Integer iJahr = DialogFactory.showJahreseingabe();
				if (iJahr != null) {
					DelegateFactory.getInstance().getPersonalDelegate()
							.feiertageAusVorlageFuerJahrEintragen(iJahr);
				}

			}
		}

	}

	public void setKeyWasForLockMe() {
		Object oKey = panelQueryBetriebskalender.getSelectedId();

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
						"pers.title.tab.betriebskalender"));
		getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_OHRWASCHLOBEN,
				((PanelBasis) this.getSelectedComponent()).getAdd2Title());
		getInternalFrame().setLpTitle(3, "");
	}

	public void lPEventObjectChanged(ChangeEvent e) throws Throwable {

		super.lPEventObjectChanged(e);

		int selectedIndex = this.getSelectedIndex();

		if (selectedIndex == IDX_PANEL_BETRIEBSKALENDER) {

			panelSplitBetriebskalender.eventYouAreSelected(false);
			panelQueryBetriebskalender.updateButtons();
		} else if (selectedIndex == IDX_PANEL_FEIERTAGE) {
			createFeiertage();
			panelSplitFeiertag.eventYouAreSelected(false);
			panelQueryFeiertag.updateButtons();
		}

	}

	private void createFeiertage() throws Throwable {
		if (panelSplitFeiertag == null) {
			String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW };

			panelQueryFeiertag = new PanelQuery(null, SystemFilterFactory
					.getInstance().createFKMandantCNr(),
					QueryParameters.UC_ID_FEIERTAG, aWhichButtonIUse,
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("pers.feiertag.vorlage"), true);

			panelBottomFeiertag = new PanelFeiertag(getInternalFrame(),
					LPMain.getInstance().getTextRespectUISPr(
							"pers.feiertag.vorlage"), null);
			
			panelQueryFeiertag.createAndSaveAndShowButton(
					"/com/lp/client/res/JDateChooserIcon.png", LPMain.getInstance()
					.getTextRespectUISPr("pers.feiertage.fuerjahreintragen"),
					BUTTON_FEIERTAGE_EINTRAGEN, null);

			panelSplitFeiertag = new PanelSplit(getInternalFrame(),
					panelBottomFeiertag, panelQueryFeiertag, 280);

			setComponentAt(IDX_PANEL_FEIERTAGE, panelSplitFeiertag);
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
