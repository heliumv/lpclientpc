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

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.event.ChangeEvent;

import com.lp.client.frame.LockStateValue;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQuery;
import com.lp.client.frame.component.PanelSplit;
import com.lp.client.frame.component.TabbedPane;
import com.lp.client.frame.component.WrapperMenu;
import com.lp.client.frame.component.WrapperMenuBar;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.system.SystemFilterFactory;
import com.lp.client.zeiterfassung.ReportZeiterfassungZeitdaten;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;

public class TabbedPaneFahrzeug extends TabbedPane {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private PanelQuery panelQueryFahrzeug = null;
	private PanelBasis panelDetailFahrzeug = null;

	private PanelQuery panelQueryKosten = null;
	private PanelBasis panelSplitKosten = null;
	private PanelBasis panelDetailKosten = null;

	private final static int IDX_PANEL_AUSWAHL = 0;
	private final static int IDX_PANEL_DETAIL = 1;
	private final static int IDX_PANEL_KOSTEN = 2;
	
	private final static String MENUE_ACTION_FAHZEUGE = "MENUE_ACTION_FAHZEUGE";
	

	public TabbedPaneFahrzeug(InternalFrame internalFrameI) throws Throwable {
		super(internalFrameI, LPMain.getTextRespectUISPr("pers.fahrzeug"));

		jbInit();
		initComponents();
	}

	public PanelQuery getPanelQueryMaterial() {
		return panelQueryFahrzeug;
	}

	private void createAuswahl() throws Throwable {
		if (panelQueryFahrzeug == null) {
			String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW };
			panelQueryFahrzeug = new PanelQuery(null,PersonalFilterFactory.getInstance().createFKFahrzeug(),
					QueryParameters.UC_ID_FAHRZEUG, aWhichButtonIUse,
					getInternalFrame(),
					LPMain.getTextRespectUISPr("lp.auswahl"), true);

			panelQueryFahrzeug.befuellePanelFilterkriterienDirekt(
					SystemFilterFactory.getInstance().createFKDBezeichnung(),
					null);

			setComponentAt(IDX_PANEL_AUSWAHL, panelQueryFahrzeug);

		}
	}

	private void createDetail(Integer key) throws Throwable {
		if (panelDetailFahrzeug == null) {
			panelDetailFahrzeug = new PanelFahrzeug(getInternalFrame(),
					LPMain.getTextRespectUISPr("lp.detail"), key);
			setComponentAt(IDX_PANEL_DETAIL, panelDetailFahrzeug);
		}
	}

	private void createKosten(Integer key) throws Throwable {

		if (panelQueryKosten == null) {
			String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW };

			FilterKriterium[] filters = PersonalFilterFactory.getInstance()
					.createFKFahrzeugkosten(key);

			panelQueryKosten = new PanelQuery(null, filters,
					QueryParameters.UC_ID_FAHRZEUGKOSTEN, aWhichButtonIUse,
					getInternalFrame(),
					LPMain.getTextRespectUISPr("pers.fahrzeug.kosten"), true);

			panelDetailKosten = new PanelFahrzeugkosten(getInternalFrame(),
					LPMain.getTextRespectUISPr("pers.fahrzeug.kosten"), null);

			panelSplitKosten = new PanelSplit(getInternalFrame(),
					panelDetailKosten, panelQueryKosten, 350);

			setComponentAt(IDX_PANEL_KOSTEN, panelSplitKosten);
		} else {
			// filter refreshen.
			panelQueryKosten.setDefaultFilter(PersonalFilterFactory
					.getInstance().createFKFahrzeugkosten(key));
		}
	}

	private void jbInit() throws Throwable {

		insertTab(LPMain.getTextRespectUISPr("lp.auswahl"), null, null,
				LPMain.getTextRespectUISPr("lp.auswahl"), IDX_PANEL_AUSWAHL);

		insertTab(LPMain.getTextRespectUISPr("lp.detail"), null, null,
				LPMain.getTextRespectUISPr("lp.detail"), IDX_PANEL_DETAIL);
		insertTab(LPMain.getTextRespectUISPr("pers.fahrzeug.kosten"), null,
				null, LPMain.getTextRespectUISPr("pers.fahrzeug.kosten"),
				IDX_PANEL_KOSTEN);

		createAuswahl();

		panelQueryFahrzeug.eventYouAreSelected(false);
		if ((Integer) panelQueryFahrzeug.getSelectedId() != null) {
			getInternalFramePersonal().setFahrzeugDto(
					DelegateFactory
							.getInstance()
							.getPersonalDelegate()
							.fahrzeugFindByPrimaryKey(
									(Integer) panelQueryFahrzeug
											.getSelectedId()));
		}
		// damit D2 einen aktuellen hat.
		ItemChangedEvent it = new ItemChangedEvent(panelQueryFahrzeug,
				ItemChangedEvent.ITEM_CHANGED);
		lPEventItemChanged(it);

		this.addChangeListener(this);
		this.getInternalFrame().addItemChangedListener(this);

	}

	public void lPEventItemChanged(ItemChangedEvent e) throws Throwable {

		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			if (e.getSource() == panelQueryFahrzeug) {
				Integer iId = (Integer) panelQueryFahrzeug.getSelectedId();
				if (iId != null) {
					setSelectedComponent(panelDetailFahrzeug);
				}
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_DISCARD) {
			if (e.getSource() == panelDetailKosten) {
				panelDetailKosten.eventYouAreSelected(false);
			} else if (e.getSource() == panelDetailFahrzeug) {
				panelDetailFahrzeug.eventYouAreSelected(false);

			}
		} else if (e.getID() == ItemChangedEvent.ACTION_UPDATE) {
			if (e.getSource() == panelDetailKosten) {
				panelQueryKosten.updateButtons(new LockStateValue(
						PanelBasis.LOCK_FOR_NEW));
				;
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_SAVE) {

			if (e.getSource() == panelDetailFahrzeug) {
				panelQueryFahrzeug.clearDirektFilter();
				Object oKey = panelDetailFahrzeug.getKeyWhenDetailPanel();
				panelQueryFahrzeug.setSelectedId(oKey);
			}

			if (e.getSource() == panelDetailKosten) {
				Object oKey = panelDetailKosten.getKeyWhenDetailPanel();
				panelQueryKosten.eventYouAreSelected(false);
				panelQueryKosten.setSelectedId(oKey);
				panelSplitKosten.eventYouAreSelected(false);

			}
		} else if (e.getID() == ItemChangedEvent.ITEM_CHANGED) {
			if (e.getSource() == panelQueryKosten) {
				Integer iId = (Integer) panelQueryKosten.getSelectedId();
				panelDetailKosten.setKeyWhenDetailPanel(iId);
				panelDetailKosten.eventYouAreSelected(false);
				panelQueryKosten.updateButtons();
			} else if (e.getSource() == panelQueryFahrzeug) {
				if (panelQueryFahrzeug.getSelectedId() != null) {
					Object key = ((ISourceEvent) e.getSource()).getIdSelected();

					getInternalFramePersonal().setKeyWasForLockMe(key + "");

					getInternalFramePersonal().setFahrzeugDto(
							DelegateFactory.getInstance().getPersonalDelegate()
									.fahrzeugFindByPrimaryKey((Integer) key));

					if (panelQueryFahrzeug.getSelectedId() == null) {
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
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_YOU_ARE_SELECTED) {
			refreshTitle();
		} else if (e.getID() == ItemChangedEvent.ACTION_GOTO_MY_DEFAULT_QP) {
			// aktiviere ein QP ...
			if (e.getSource() == panelDetailFahrzeug) {
				getInternalFrame().enableAllPanelsExcept(true);
				this.setSelectedComponent(panelQueryFahrzeug);
				setKeyWasForLockMe();
				panelQueryFahrzeug.eventYouAreSelected(false);
			} else if (e.getSource() == panelDetailKosten) {
				setKeyWasForLockMe();
				if (panelDetailKosten.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryKosten
							.getId2SelectAfterDelete();
					panelQueryKosten.setSelectedId(oNaechster);
				}
				panelSplitKosten.eventYouAreSelected(false);
			}

		} else if (e.getID() == ItemChangedEvent.ACTION_NEW) {
			if (e.getSource() == panelQueryFahrzeug) {

				createDetail((Integer) panelQueryFahrzeug.getSelectedId());
				if (panelQueryFahrzeug.getSelectedId() == null) {
					getInternalFrame().enableAllPanelsExcept(true);
				}
				panelDetailFahrzeug.eventActionNew(null, true, false);
				setSelectedComponent(panelDetailFahrzeug);
			} else if (e.getSource() == panelQueryKosten) {
				panelDetailKosten.eventActionNew(e, true, false);
				panelDetailKosten.eventYouAreSelected(false);
				this.setSelectedComponent(panelSplitKosten);
			}

		}

	}

	public InternalFramePersonal getInternalFramePersonal() {
		return (InternalFramePersonal) getInternalFrame();
	}

	public void setKeyWasForLockMe() {
		Object oKey = panelQueryFahrzeug.getSelectedId();

		if (oKey != null) {
			getInternalFrame().setKeyWasForLockMe(oKey.toString());
		} else {
			getInternalFrame().setKeyWasForLockMe(null);
		}
	}

	private void refreshTitle() {

		getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_OHRWASCHLUNTEN,
				LPMain.getTextRespectUISPr("pers.fahrzeug"));
		getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_OHRWASCHLOBEN,
				((PanelBasis) this.getSelectedComponent()).getAdd2Title());

		if (getInternalFramePersonal().getFahrzeugDto() != null
				&& getInternalFramePersonal().getFahrzeugDto().getCBez() != null) {
			getInternalFrame().setLpTitle(
					InternalFrame.TITLE_IDX_AS_I_LIKE,
					LPMain.getTextRespectUISPr("pers.fahrzeug")
							+ ": "
							+ getInternalFramePersonal().getFahrzeugDto()
									.getCBez());
		}

	}

	public void lPEventObjectChanged(ChangeEvent e) throws Throwable {
		super.lPEventObjectChanged(e);

		int selectedIndex = this.getSelectedIndex();

		if (selectedIndex == IDX_PANEL_AUSWAHL) {
			createAuswahl();
			panelQueryFahrzeug.eventYouAreSelected(false);
			if (panelQueryFahrzeug.getSelectedId() == null) {
				getInternalFrame().enableAllOberePanelsExceptMe(this,
						IDX_PANEL_AUSWAHL, false);
			}

			panelQueryFahrzeug.updateButtons();
		}

		else if (selectedIndex == IDX_PANEL_DETAIL) {
			Integer key = null;
			if (getInternalFramePersonal().getFahrzeugDto() != null) {
				key = getInternalFramePersonal().getFahrzeugDto().getIId();
			}
			createDetail(key);
			panelDetailFahrzeug.eventYouAreSelected(false);

		} else if (selectedIndex == IDX_PANEL_KOSTEN) {
			createKosten(getInternalFramePersonal().getFahrzeugDto().getIId());
			panelSplitKosten.eventYouAreSelected(false);
			panelQueryKosten.updateButtons();
		}

		refreshTitle();
	}

	protected void lPActionEvent(java.awt.event.ActionEvent e) throws Throwable  {
		if (e.getActionCommand().equals(MENUE_ACTION_FAHZEUGE)) {
			String add2Title = LPMain
					.getTextRespectUISPr("pers.fahrzeug");
			getInternalFrame().showReportKriterien(
					new ReportFahrzeug(getInternalFrame(),getInternalFramePersonal().getFahrzeugDto(), add2Title));

		}
	}

	public javax.swing.JMenuBar getJMenuBar() throws Throwable {
		WrapperMenuBar menuBarFahrzeug = new WrapperMenuBar(this);
		JMenu menuInfo = new WrapperMenu("lp.info", this);
		
		
		JMenuItem menuItemFahrzeuge = new JMenuItem(
				LPMain.getTextRespectUISPr("pers.fahrzeug"));
		menuItemFahrzeuge.addActionListener(this);
		menuItemFahrzeuge
				.setActionCommand(MENUE_ACTION_FAHZEUGE);
		menuInfo.add(menuItemFahrzeuge, 0);

		menuBarFahrzeug.add(menuInfo);
		return menuBarFahrzeug;
	}

}
