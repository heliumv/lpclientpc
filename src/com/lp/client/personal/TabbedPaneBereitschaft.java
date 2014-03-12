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
package com.lp.client.personal;

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
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;

@SuppressWarnings("static-access")
public class TabbedPaneBereitschaft extends TabbedPane {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private PanelQuery panelQueryBereitschaftart = null;
	private PanelBasis panelDetailBereitschaftart = null;

	private PanelQuery panelQueryBereitschafttag = null;
	private PanelBasis panelBottomBereitschafttag = null;
	private PanelSplit panelSplitBereitschafttag = null;

	private final static int IDX_PANEL_AUSWAHL = 0;
	private final static int IDX_PANEL_DETAIL = 1;
	private final static int IDX_PANEL_TAGE = 2;

	private WrapperMenuBar wrapperMenuBar = null;

	public TabbedPaneBereitschaft(InternalFrame internalFrameI)
			throws Throwable {
		super(internalFrameI, LPMain.getInstance().getTextRespectUISPr(
				"pers.bereitschaft"));

		jbInit();
		initComponents();
	}

	public PanelQuery getPanelQueryBereitschaft() {
		return panelQueryBereitschaftart;
	}

	private void jbInit() throws Throwable {

		// Bereitschaftartauswahlliste

		FilterKriterium[] kriterien = new FilterKriterium[1];
		kriterien[0] = new FilterKriterium("mandant_c_nr", true, "'"
				+ LPMain.getInstance().getTheClient().getMandant() + "'",
				FilterKriterium.OPERATOR_EQUAL, false);

		String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW };
		panelQueryBereitschaftart = new PanelQuery(null, kriterien,
				QueryParameters.UC_ID_BEREITSCHAFTART, aWhichButtonIUse,
				getInternalFrame(), LPMain.getInstance().getTextRespectUISPr(
						"auft.title.panel.auswahl"), true);

		panelQueryBereitschaftart.befuellePanelFilterkriterienDirekt(
				PersonalFilterFactory.getInstance()
						.createFKDBereitschaftartBezeichnung(),null);

		addTab(LPMain.getInstance().getTextRespectUISPr(
				"auft.title.panel.auswahl"), panelQueryBereitschaftart);
		panelQueryBereitschaftart.eventYouAreSelected(false);

		if ((Integer) panelQueryBereitschaftart.getSelectedId() != null) {
			getInternalFramePersonal().setBereitschaftartDto(
					DelegateFactory
							.getInstance()
							.getPersonalDelegate()
							.bereitschaftartFindByPrimaryKey(
									(Integer) panelQueryBereitschaftart
											.getSelectedId()));
		}

		// Bereitschaftdetail
		panelDetailBereitschaftart = new PanelBereitschaftart(getInternalFrame(), LPMain
				.getInstance().getTextRespectUISPr("lp.detail"),
				panelQueryBereitschaftart.getSelectedId());
		addTab(LPMain.getInstance().getTextRespectUISPr("lp.detail"),
				panelDetailBereitschaftart);

		// Bereitschafttag
		panelQueryBereitschafttag = new PanelQuery(null, PersonalFilterFactory
				.getInstance().createFKBereitschafttag(
						(Integer) panelQueryBereitschaftart.getSelectedId()),
				QueryParameters.UC_ID_BEREITSCHAFTTAG, aWhichButtonIUse,
				getInternalFrame(), LPMain.getInstance().getTextRespectUISPr(
						"pers.bereitschafttag"), true);
		panelBottomBereitschafttag = new PanelBereitschafttag(getInternalFrame(),
				LPMain.getInstance().getTextRespectUISPr(
						"pers.bereitschafttag"), null);

		panelSplitBereitschafttag = new PanelSplit(getInternalFrame(),
				panelBottomBereitschafttag, panelQueryBereitschafttag, 185);
		addTab(LPMain.getInstance().getTextRespectUISPr(
				"pers.bereitschafttag"), panelSplitBereitschafttag);

		panelQueryBereitschaftart.eventYouAreSelected(false);

		// Itemevents an MEIN Detailpanel senden kann.
		getInternalFrame().addItemChangedListener(this);
		refreshTitle();
		this.addChangeListener(this);

	}

	public void setKeyWasForLockMe() {
		Object oKey = panelQueryBereitschaftart.getSelectedId();

		if (oKey != null) {
			getInternalFrame().setKeyWasForLockMe(oKey.toString());
		} else {
			getInternalFrame().setKeyWasForLockMe(null);
		}
	}

	private void refreshTitle() {

		getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_OHRWASCHLUNTEN,
				LPMain.getInstance().getTextRespectUISPr("pers.bereitschaft"));
		getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_OHRWASCHLOBEN,
				((PanelBasis) this.getSelectedComponent()).getAdd2Title());
		if (getInternalFramePersonal().getBereitschaftartDto() != null) {
			getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_AS_I_LIKE,
					getInternalFramePersonal().getBereitschaftartDto().getCBez());
		}

	}

	public InternalFramePersonal getInternalFramePersonal() {
		return (InternalFramePersonal) getInternalFrame();
	}

	protected void lPActionEvent(ActionEvent e) {
	}

	public void lPEventItemChanged(ItemChangedEvent e) throws Throwable {

		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			if (e.getSource() == panelQueryBereitschaftart) {
				Integer iId = (Integer) panelQueryBereitschaftart.getSelectedId();
				if (iId != null) {
					setSelectedComponent(panelDetailBereitschaftart);
				}
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_DISCARD) {
			if (e.getSource() == panelBottomBereitschafttag) {
				panelSplitBereitschafttag.eventYouAreSelected(false);
			}

		} else if (e.getID() == ItemChangedEvent.ACTION_UPDATE) {
			if (e.getSource() == panelBottomBereitschafttag) {
				panelQueryBereitschafttag.updateButtons(new LockStateValue(
						PanelBasis.LOCK_FOR_NEW));
			}

		}

		else if (e.getID() == ItemChangedEvent.ACTION_SAVE) {

			if (e.getSource() == panelDetailBereitschaftart) {
				panelQueryBereitschaftart.clearDirektFilter();
				Object oKey = panelDetailBereitschaftart.getKeyWhenDetailPanel();

				panelQueryBereitschaftart.setSelectedId(oKey);
			}

			else if (e.getSource() == panelBottomBereitschafttag) {

				panelQueryBereitschafttag.setDefaultFilter(PersonalFilterFactory
						.getInstance().createFKBereitschafttag(
								getInternalFramePersonal().getBereitschaftartDto()
										.getIId()));
				Object oKey = panelBottomBereitschafttag.getKeyWhenDetailPanel();

				panelQueryBereitschafttag.eventYouAreSelected(false);
				panelQueryBereitschafttag.setSelectedId(oKey);
				panelSplitBereitschafttag.eventYouAreSelected(false);

			}

		} else if (e.getID() == ItemChangedEvent.ITEM_CHANGED) {
			if (e.getSource() == panelQueryBereitschaftart) {
				if (panelQueryBereitschaftart.getSelectedId() != null) {
					getInternalFramePersonal().setKeyWasForLockMe(
							panelQueryBereitschaftart.getSelectedId() + "");

					// Dto-setzen

					getInternalFramePersonal().setBereitschaftartDto(
							DelegateFactory
									.getInstance()
									.getPersonalDelegate()
									.bereitschaftartFindByPrimaryKey(
											(Integer) panelQueryBereitschaftart
													.getSelectedId()));
					getInternalFrame().setLpTitle(
							InternalFrame.TITLE_IDX_AS_I_LIKE,
							getInternalFramePersonal().getBereitschaftartDto()
									.getCBez());

					// Default- Filter vorbesetzten
					panelQueryBereitschafttag
							.setDefaultFilter(PersonalFilterFactory
									.getInstance().createFKBereitschafttag(
											getInternalFramePersonal()
													.getBereitschaftartDto()
													.getIId()));
					if (panelQueryBereitschaftart.getSelectedId() == null) {
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

			} else if (e.getSource() == panelQueryBereitschafttag) {

				Integer iId = (Integer) panelQueryBereitschafttag
						.getSelectedId();
				panelBottomBereitschafttag.setKeyWhenDetailPanel(iId);
				panelBottomBereitschafttag.eventYouAreSelected(false);
				panelQueryBereitschafttag.updateButtons();
			}

		}

		else if (e.getID() == ItemChangedEvent.ACTION_GOTO_MY_DEFAULT_QP) {
			// aktiviere ein QP ...
			if (e.getSource() == panelDetailBereitschaftart) {
				this.setSelectedComponent(panelQueryBereitschaftart);
				setKeyWasForLockMe();
				panelQueryBereitschaftart.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomBereitschafttag) {
				setKeyWasForLockMe();
				if (panelBottomBereitschafttag.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryBereitschafttag
							.getId2SelectAfterDelete();
					panelQueryBereitschafttag.setSelectedId(oNaechster);
				}
				panelSplitBereitschafttag.eventYouAreSelected(false);
			}

		} else if (e.getID() == ItemChangedEvent.ACTION_YOU_ARE_SELECTED) {
			refreshTitle();
			super.lPEventItemChanged(e);
		} else if (e.getID() == ItemChangedEvent.ACTION_NEW) {
			if (e.getSource() == panelQueryBereitschaftart) {
				if (panelQueryBereitschaftart.getSelectedId() == null) {
					getInternalFrame().enableAllPanelsExcept(true);
				}
				panelDetailBereitschaftart.eventActionNew(null, true, false);
				setSelectedComponent(panelDetailBereitschaftart);
			} else if (e.getSource() == panelQueryBereitschafttag) {
				panelBottomBereitschafttag.eventActionNew(e, true, false);
				panelBottomBereitschafttag.eventYouAreSelected(false);
				this.setSelectedComponent(panelSplitBereitschafttag);

			}

		}

	}

	public void lPEventObjectChanged(ChangeEvent e) throws Throwable {
		super.lPEventObjectChanged(e);

		int selectedIndex = this.getSelectedIndex();

		if (selectedIndex == IDX_PANEL_AUSWAHL) {
			panelQueryBereitschaftart.eventYouAreSelected(false);
			if (panelQueryBereitschaftart.getSelectedId() == null) {
				getInternalFrame().enableAllOberePanelsExceptMe(this,
						IDX_PANEL_AUSWAHL, false);
			}
			panelQueryBereitschaftart.updateButtons();
		} else if (selectedIndex == IDX_PANEL_DETAIL) {
			panelDetailBereitschaftart.eventYouAreSelected(false);
		} else if (selectedIndex == IDX_PANEL_TAGE) {
			if (getInternalFramePersonal().getZeitmodellDto() != null) {
				panelQueryBereitschafttag.setDefaultFilter(PersonalFilterFactory
						.getInstance().createFKBereitschafttag(
								getInternalFramePersonal().getBereitschaftartDto()
										.getIId()));
			}

			panelSplitBereitschafttag.eventYouAreSelected(false);
			panelQueryBereitschafttag.updateButtons();
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
