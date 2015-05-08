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
package com.lp.client.zeiterfassung;

import java.awt.event.ActionEvent;

import javax.swing.event.ChangeEvent;

import com.lp.client.frame.LockStateValue;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQuery;
import com.lp.client.frame.component.PanelSplit;
import com.lp.client.frame.component.TabbedPane;
import com.lp.client.frame.component.WrapperMenuBar;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.personal.PanelZeitmodell;
import com.lp.client.personal.PanelZeitmodelltag;
import com.lp.client.personal.PersonalFilterFactory;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;

@SuppressWarnings("static-access")
public class TabbedPaneMaschinenzeitmodell extends TabbedPane {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private PanelQuery panelQueryZeitmodell = null;
	private PanelBasis panelDetailZeitmodelll = null;

	private PanelQuery panelQueryZeitmodelltage = null;
	private PanelBasis panelBottomZeitmodelltage = null;
	private PanelSplit panelSplitZeitmodelltage = null;

	private final static int IDX_PANEL_AUSWAHL = 0;
	private final static int IDX_PANEL_DETAIL = 1;
	private final static int IDX_PANEL_TAGE = 2;

	private WrapperMenuBar wrapperMenuBar = null;

	public TabbedPaneMaschinenzeitmodell(InternalFrame internalFrameI)
			throws Throwable {
		super(internalFrameI, LPMain.getInstance().getTextRespectUISPr(
				"pers.title.tab.zeitmodell"));

		jbInit();
		initComponents();
	}

	public PanelQuery getPanelQueryZeitmodell() {
		return panelQueryZeitmodell;
	}

	private void jbInit() throws Throwable {

		// Zeitmodellauswahlliste

		FilterKriterium[] kriterien = new FilterKriterium[1];
		kriterien[0] = new FilterKriterium("maschinenzm.mandant_c_nr", true, "'"
				+ LPMain.getInstance().getTheClient().getMandant() + "'",
				FilterKriterium.OPERATOR_EQUAL, false);

		String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW };
		panelQueryZeitmodell = new PanelQuery(null, kriterien,
				QueryParameters.UC_ID_MASCHINEZM, aWhichButtonIUse,
				getInternalFrame(), LPMain.getInstance().getTextRespectUISPr(
						"auft.title.panel.auswahl"), true);

		panelQueryZeitmodell.befuellePanelFilterkriterienDirekt(
				PersonalFilterFactory.getInstance()
						.createFKDMaschinenzeitmodellBezeichnung(),null);

		addTab(LPMain.getInstance().getTextRespectUISPr(
				"auft.title.panel.auswahl"), panelQueryZeitmodell);
		panelQueryZeitmodell.eventYouAreSelected(false);

		if ((Integer) panelQueryZeitmodell.getSelectedId() != null) {
			getInternalFramePersonal().setMaschinenzmDto(
					DelegateFactory
							.getInstance()
							.getMaschineDelegate()
							.maschinezmFindByPrimaryKey(
									(Integer) panelQueryZeitmodell
											.getSelectedId()));
		}

		// Zeitmodelldetail
		panelDetailZeitmodelll = new PanelMaschinenzeitmodell(getInternalFrame(), LPMain
				.getInstance().getTextRespectUISPr("lp.detail"),
				panelQueryZeitmodell.getSelectedId());
		addTab(LPMain.getInstance().getTextRespectUISPr("lp.detail"),
				panelDetailZeitmodelll);

		// Zeitmodelltag
		panelQueryZeitmodelltage = new PanelQuery(null, PersonalFilterFactory
				.getInstance().createFKMaschinezmtagesart(
						(Integer) panelQueryZeitmodell.getSelectedId()),
				QueryParameters.UC_ID_MASCHINENZMTAGESART, aWhichButtonIUse,
				getInternalFrame(), LPMain.getInstance().getTextRespectUISPr(
						"pers.title.tab.zeitmodelltag"), true);
		panelBottomZeitmodelltage = new PanelMaschinenzmtagesart(getInternalFrame(),
				LPMain.getInstance().getTextRespectUISPr(
						"pers.title.tab.zeitmodelltag"), null);

		panelSplitZeitmodelltage = new PanelSplit(getInternalFrame(),
				panelBottomZeitmodelltage, panelQueryZeitmodelltage, 350);
		addTab(LPMain.getInstance().getTextRespectUISPr(
				"pers.title.tab.zeitmodelltag"), panelSplitZeitmodelltage);

		panelQueryZeitmodell.eventYouAreSelected(false);

		// Itemevents an MEIN Detailpanel senden kann.
		getInternalFrame().addItemChangedListener(this);
		refreshTitle();
		this.addChangeListener(this);

	}

	public void setKeyWasForLockMe() {
		Object oKey = panelQueryZeitmodell.getSelectedId();

		if (oKey != null) {
			getInternalFrame().setKeyWasForLockMe(oKey.toString());
		} else {
			getInternalFrame().setKeyWasForLockMe(null);
		}
	}

	private void refreshTitle() {

		getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_OHRWASCHLUNTEN,
				LPMain.getInstance().getTextRespectUISPr("lp.zeitmodell"));
		getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_OHRWASCHLOBEN,
				((PanelBasis) this.getSelectedComponent()).getAdd2Title());
		if (getInternalFramePersonal().getMaschinenzmDto() != null) {
			getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_AS_I_LIKE,
					getInternalFramePersonal().getMaschinenzmDto().getCBez());
		}

	}

	public InternalFrameZeiterfassung getInternalFramePersonal() {
		return (InternalFrameZeiterfassung) getInternalFrame();
	}

	protected void lPActionEvent(ActionEvent e) {
	}

	public void lPEventItemChanged(ItemChangedEvent e) throws Throwable {

		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			if (e.getSource() == panelQueryZeitmodell) {
				Integer iId = (Integer) panelQueryZeitmodell.getSelectedId();
				if (iId != null) {
					setSelectedComponent(panelDetailZeitmodelll);
				}
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_DISCARD) {
			if (e.getSource() == panelBottomZeitmodelltage) {
				panelSplitZeitmodelltage.eventYouAreSelected(false);
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_UPDATE) {
			if (e.getSource() == panelBottomZeitmodelltage) {
				panelQueryZeitmodelltage.updateButtons(new LockStateValue(
						PanelBasis.LOCK_FOR_NEW));
			}
		}

		else if (e.getID() == ItemChangedEvent.ACTION_SAVE) {

			if (e.getSource() == panelDetailZeitmodelll) {
				panelQueryZeitmodell.clearDirektFilter();
				Object oKey = panelDetailZeitmodelll.getKeyWhenDetailPanel();

				panelQueryZeitmodell.setSelectedId(oKey);
			}

			else if (e.getSource() == panelBottomZeitmodelltage) {

				panelQueryZeitmodelltage.setDefaultFilter(PersonalFilterFactory
						.getInstance().createFKMaschinezmtagesart(
								getInternalFramePersonal().getMaschinenzmDto()
										.getIId()));
				Object oKey = panelBottomZeitmodelltage.getKeyWhenDetailPanel();

				panelQueryZeitmodelltage.eventYouAreSelected(false);
				panelQueryZeitmodelltage.setSelectedId(oKey);
				panelSplitZeitmodelltage.eventYouAreSelected(false);

			}

		} else if (e.getID() == ItemChangedEvent.ITEM_CHANGED) {
			if (e.getSource() == panelQueryZeitmodell) {
				if (panelQueryZeitmodell.getSelectedId() != null) {
					getInternalFramePersonal().setKeyWasForLockMe(
							panelQueryZeitmodell.getSelectedId() + "");

					// Dto-setzen

					getInternalFramePersonal().setMaschinenzmDto(
							DelegateFactory
									.getInstance()
									.getMaschineDelegate()
									.maschinezmFindByPrimaryKey(
											(Integer) panelQueryZeitmodell
													.getSelectedId()));
					getInternalFrame().setLpTitle(
							InternalFrame.TITLE_IDX_AS_I_LIKE,
							getInternalFramePersonal().getMaschinenzmDto()
									.getCBez());

					// Default- Filter vorbesetzten
					panelQueryZeitmodelltage
							.setDefaultFilter(PersonalFilterFactory
									.getInstance().createFKMaschinezmtagesart(
											getInternalFramePersonal()
													.getMaschinenzmDto()
													.getIId()));

					if (panelQueryZeitmodell.getSelectedId() == null) {
						getInternalFrame().enableAllOberePanelsExceptMe(this,
								IDX_PANEL_AUSWAHL, false);
					} else {
						getInternalFrame().enableAllOberePanelsExceptMe(this,
								IDX_PANEL_AUSWAHL, true);
					}

				} else {
					getInternalFrame().enableAllOberePanelsExceptMe(this,
							IDX_PANEL_AUSWAHL, false);
				}

			} else if (e.getSource() == panelQueryZeitmodelltage) {

				Integer iId = (Integer) panelQueryZeitmodelltage
						.getSelectedId();
				panelBottomZeitmodelltage.setKeyWhenDetailPanel(iId);
				panelBottomZeitmodelltage.eventYouAreSelected(false);
				panelQueryZeitmodelltage.updateButtons();
			}

		}

		else if (e.getID() == ItemChangedEvent.ACTION_GOTO_MY_DEFAULT_QP) {
			// aktiviere ein QP ...
			if (e.getSource() == panelDetailZeitmodelll) {
				this.setSelectedComponent(panelQueryZeitmodell);
				setKeyWasForLockMe();
				panelQueryZeitmodell.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomZeitmodelltage) {
				setKeyWasForLockMe();
				if (panelBottomZeitmodelltage.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryZeitmodelltage
							.getId2SelectAfterDelete();
					panelQueryZeitmodelltage.setSelectedId(oNaechster);
				}
				panelSplitZeitmodelltage.eventYouAreSelected(false);
			}

		} else if (e.getID() == ItemChangedEvent.ACTION_YOU_ARE_SELECTED) {
			refreshTitle();
			super.lPEventItemChanged(e);
		} else if (e.getID() == ItemChangedEvent.ACTION_NEW) {
			if (e.getSource() == panelQueryZeitmodell) {
				if (panelQueryZeitmodell.getSelectedId() == null) {
					getInternalFrame().enableAllPanelsExcept(true);
				}
				panelDetailZeitmodelll.eventActionNew(null, true, false);
				setSelectedComponent(panelDetailZeitmodelll);
			} else if (e.getSource() == panelQueryZeitmodelltage) {
				panelBottomZeitmodelltage.eventActionNew(e, true, false);
				panelBottomZeitmodelltage.eventYouAreSelected(false);
				this.setSelectedComponent(panelSplitZeitmodelltage);

			}
		}

	}

	public void lPEventObjectChanged(ChangeEvent e) throws Throwable {
		super.lPEventObjectChanged(e);

		int selectedIndex = this.getSelectedIndex();

		if (selectedIndex == IDX_PANEL_AUSWAHL) {
			panelQueryZeitmodell.eventYouAreSelected(false);
			if (panelQueryZeitmodell.getSelectedId() == null) {
				getInternalFrame().enableAllOberePanelsExceptMe(this,
						IDX_PANEL_AUSWAHL, false);
			}
			panelQueryZeitmodell.updateButtons();
		} else if (selectedIndex == IDX_PANEL_DETAIL) {
			panelDetailZeitmodelll.eventYouAreSelected(false);
		} else if (selectedIndex == IDX_PANEL_TAGE) {
			if (getInternalFramePersonal().getMaschinenzmDto() != null) {
				panelQueryZeitmodelltage.setDefaultFilter(PersonalFilterFactory
						.getInstance().createFKMaschinezmtagesart(
								getInternalFramePersonal().getMaschinenzmDto()
										.getIId()));
			}

			panelSplitZeitmodelltage.eventYouAreSelected(false);
			panelQueryZeitmodelltage.updateButtons();

		}
		refreshTitle();

	}

	public javax.swing.JMenuBar getJMenuBar() throws Throwable {
		if (wrapperMenuBar == null) {
			wrapperMenuBar = new WrapperMenuBar(this);
		}
		return wrapperMenuBar;
	}

}
