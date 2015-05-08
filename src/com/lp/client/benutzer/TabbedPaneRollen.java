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
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQuery;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.PanelSplit;
import com.lp.client.frame.component.TabbedPane;
import com.lp.client.frame.component.WrapperMenuBar;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.util.Facade;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.FilterKriteriumDirekt;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;

@SuppressWarnings("static-access")
/*
 * <p>&UUml;berschrift: </p> <p>Beschreibung: </p> <p>Copyright: Copyright (c)
 * 2004</p> <p>Organisation: </p>
 * 
 * @author Christian Kollmann
 * 
 * @version $Revision: 1.9 $
 */
public class TabbedPaneRollen extends TabbedPane {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private PanelQuery panelQuerySystemrolle = null;
	private PanelQuery panelQueryRollerecht = null;
	private PanelBasis panelDetailSystemrolle = null;
	private PanelBasis panelSplit = null;
	private PanelBasis panelDetailRollerecht = null;

	private PanelBasis panelSplitLagerrolle = null;
	private PanelQuery panelQueryLagerrolle = null;
	private PanelBasis panelDetailLagerrolle = null;

	private PanelBasis panelSplitFertigungsgrupperolle = null;
	private PanelQuery panelQueryFertigungsgrupperolle = null;
	private PanelBasis panelDetailFertigungsgrupperolle = null;

	private WrapperMenuBar wrapperMenuBar = null;

	private final static int IDX_PANEL_AUSWAHL = 0;
	private final static int IDX_PANEL_DETAIL = 1;
	private final static int IDX_PANEL_RECHTE = 2;
	private final static int IDX_PANEL_LAGER = 3;
	private final static int IDX_PANEL_FERTIGUNGSGRUPPE = 4;

	private static final String ACTION_SPECIAL_REST = "action_special_rest";

	private final String BUTTON_RESTLICHERECHTESPEICHERN = PanelBasis.ACTION_MY_OWN_NEW
			+ ACTION_SPECIAL_REST;

	
	private static final String ACTION_SPECIAL_LAGERRECHTE_REST = "action_special_lagerrechte_rest";

	private final String BUTTON_RESTLICHELAGERRECHTESPEICHERN = PanelBasis.ACTION_MY_OWN_NEW
			+ ACTION_SPECIAL_LAGERRECHTE_REST;

	
	
	private PanelQueryFLR panelRolle = null;
	private PanelQueryFLR panelRolleFuerLagerrechte = null;

	public TabbedPaneRollen(InternalFrame internalFrameI) throws Throwable {
		super(internalFrameI, LPMain.getInstance().getTextRespectUISPr(
				"lp.systemrolle"));

		jbInit();
		initComponents();
	}

	private void jbInit() throws Throwable {
		// 1 Materialauswahlliste
		String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW };
		panelQuerySystemrolle = new PanelQuery(null, null,
				QueryParameters.UC_ID_SYSTEMROLLE, aWhichButtonIUse,
				getInternalFrame(), LPMain.getInstance().getTextRespectUISPr(
						"benutzer.title.tab.systemrolle"), true);

		addTab(LPMain.getInstance().getTextRespectUISPr(
				"auft.title.panel.auswahl"), panelQuerySystemrolle);
		panelQuerySystemrolle.eventYouAreSelected(false);

		if ((Integer) panelQuerySystemrolle.getSelectedId() != null) {
			getInternalFrameBenutzer().setSystemrolleDto(
					DelegateFactory
							.getInstance()
							.getBenutzerDelegate()
							.systemrolleFindByPrimaryKey(
									(Integer) panelQuerySystemrolle
											.getSelectedId()));
		}

		// 2 SystemrolleBezeichnung/Kennung
		panelDetailSystemrolle = new PanelSystemrolle(getInternalFrame(),
				LPMain.getInstance().getTextRespectUISPr("lp.kopfdaten"),
				panelQuerySystemrolle.getSelectedId());
		addTab(LPMain.getInstance().getTextRespectUISPr("lp.kopfdaten"),
				panelDetailSystemrolle);
		String[] aWhichButtonIUse2 = { PanelBasis.ACTION_NEW };
		// 3 Rechte
		panelQueryRollerecht = new PanelQuery(null, null,
				QueryParameters.UC_ID_ROLLERECHT, aWhichButtonIUse2,
				getInternalFrame(), LPMain.getInstance().getTextRespectUISPr(
						"lp.rechte"), true);

		panelQueryRollerecht.befuellePanelFilterkriterienDirekt(
				new FilterKriteriumDirekt("flrrecht.c_nr", "",
						FilterKriterium.OPERATOR_LIKE, LPMain.getInstance()
								.getTextRespectUISPr("lp.rechte"),
						FilterKriteriumDirekt.PROZENT_TRAILING, true, true,
						Facade.MAX_UNBESCHRAENKT), null);

		panelDetailRollerecht = new PanelRollerecht(getInternalFrame(), LPMain
				.getInstance().getTextRespectUISPr("lp.rechte"), null);

		panelQueryRollerecht.createAndSaveAndShowButton(
				"/com/lp/client/res/goto.png",
				LPMain.getInstance().getTextRespectUISPr(
						"pers.systemrolle.restlicherechtespeichern"),
				BUTTON_RESTLICHERECHTESPEICHERN, null);

		panelSplit = new PanelSplit(getInternalFrame(), panelDetailRollerecht,
				panelQueryRollerecht, 350);
		addTab(LPMain.getInstance().getTextRespectUISPr("lp.rechte"),
				panelSplit);

		// 4 Lager
		panelQueryLagerrolle = new PanelQuery(null, null,
				QueryParameters.UC_ID_LAGERROLLE, aWhichButtonIUse2,
				getInternalFrame(), LPMain.getInstance().getTextRespectUISPr(
						"lp.system.lagerrolle"), true);
		
		panelQueryLagerrolle.createAndSaveAndShowButton(
				"/com/lp/client/res/goto.png",
				LPMain.getInstance().getTextRespectUISPr(
						"pers.systemrolle.restlichelagerrechtespeichern"),
				BUTTON_RESTLICHELAGERRECHTESPEICHERN, null);
		

		panelDetailLagerrolle = new PanelLagerrolle(getInternalFrame(), LPMain
				.getInstance().getTextRespectUISPr("lp.system.lagerrolle"),
				null);

		panelSplitLagerrolle = new PanelSplit(getInternalFrame(),
				panelDetailLagerrolle, panelQueryLagerrolle, 350);
		addTab(LPMain.getInstance().getTextRespectUISPr("lp.system.lagerrolle"),
				panelSplitLagerrolle);
		if (LPMain.getInstance().getDesktop()
				.darfAnwenderAufModulZugreifen(LocaleFac.BELEGART_STUECKLISTE)) {
			// 4 Lager
			panelQueryFertigungsgrupperolle = new PanelQuery(null, null,
					QueryParameters.UC_ID_FERTIGUNGSGRUPPEROLLE,
					aWhichButtonIUse2, getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr(
									"lp.system.fertigungsgrupperolle"), true);

			panelDetailFertigungsgrupperolle = new PanelFertigungsgrupperolle(
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr(
									"lp.system.fertigungsgrupperolle"), null);

			panelSplitFertigungsgrupperolle = new PanelSplit(
					getInternalFrame(), panelDetailFertigungsgrupperolle,
					panelQueryFertigungsgrupperolle, 350);
			addTab(LPMain.getInstance().getTextRespectUISPr(
					"lp.system.fertigungsgrupperolle"),
					panelSplitFertigungsgrupperolle);
		}

		// Itemevents an MEIN Detailpanel senden kann.
		getInternalFrame().addItemChangedListener(this);
		refreshTitle();
		this.addChangeListener(this);
	}

	void dialogQuerySystemrolleFromListe(ItemChangedEvent e) throws Throwable {
		String[] aWhichButtonIUse = { PanelBasis.ACTION_REFRESH };

		panelRolle = new PanelQueryFLR(null, null,
				QueryParameters.UC_ID_SYSTEMROLLE, aWhichButtonIUse,
				getInternalFrame(), LPMain.getInstance().getTextRespectUISPr(
						"title.systemrolleauswahlliste"));

		new DialogQuery(panelRolle);
	}

	void dialogQuerySystemrolleFuerLagerrechteFromListe(ItemChangedEvent e) throws Throwable {
		String[] aWhichButtonIUse = { PanelBasis.ACTION_REFRESH };

		panelRolleFuerLagerrechte = new PanelQueryFLR(null, null,
				QueryParameters.UC_ID_SYSTEMROLLE, aWhichButtonIUse,
				getInternalFrame(), LPMain.getInstance().getTextRespectUISPr(
						"title.systemrolleauswahlliste"));

		new DialogQuery(panelRolleFuerLagerrechte);
	}

	public void lPEventItemChanged(ItemChangedEvent e) throws Throwable {

		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			if (e.getSource() == panelQuerySystemrolle) {
				setSelectedComponent(panelDetailSystemrolle);
			}

			if (e.getSource() == panelRolle) {
				DelegateFactory
						.getInstance()
						.getRechteDelegate()
						.kopiereRechteEinerRolle(
								(Integer) panelRolle.getSelectedId(),
								getInternalFrameBenutzer().getSystemrolleDto()
										.getIId());
				panelSplit.eventYouAreSelected(false);
			}
			if (e.getSource() == panelRolleFuerLagerrechte) {
				DelegateFactory
						.getInstance()
						.getBenutzerDelegate()
						.kopiereLagerRechteEinerRolle(
								(Integer) panelRolleFuerLagerrechte.getSelectedId(),
								getInternalFrameBenutzer().getSystemrolleDto()
										.getIId());
				panelSplitLagerrolle.eventYouAreSelected(false);
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_DISCARD) {
			if (e.getSource() == panelDetailLagerrolle) {
				panelDetailLagerrolle.eventYouAreSelected(false);
			} else if (e.getSource() == panelDetailRollerecht) {
				panelDetailRollerecht.eventYouAreSelected(false);
			} else if (e.getSource() == panelDetailSystemrolle) {
				panelDetailSystemrolle.eventYouAreSelected(false);

			} else if (e.getSource() == panelDetailFertigungsgrupperolle) {
				panelDetailFertigungsgrupperolle.eventYouAreSelected(false);

			}

		}

		else if (e.getID() == ItemChangedEvent.ACTION_SAVE) {
			if (e.getSource() == panelDetailSystemrolle) {
				panelQuerySystemrolle.clearDirektFilter();
				Object oKey = panelDetailSystemrolle.getKeyWhenDetailPanel();
				panelQuerySystemrolle.setSelectedId(oKey);
			} else if (e.getSource() == panelDetailRollerecht) {
				Object oKey = panelDetailRollerecht.getKeyWhenDetailPanel();
				panelQueryRollerecht.eventYouAreSelected(false);
				panelQueryRollerecht.setSelectedId(oKey);
				panelSplit.eventYouAreSelected(false);

			} else if (e.getSource() == panelDetailLagerrolle) {
				Object oKey = panelDetailLagerrolle.getKeyWhenDetailPanel();
				panelQueryLagerrolle.eventYouAreSelected(false);
				panelQueryLagerrolle.setSelectedId(oKey);
				panelSplitLagerrolle.eventYouAreSelected(false);

			} else if (e.getSource() == panelDetailFertigungsgrupperolle) {
				Object oKey = panelDetailFertigungsgrupperolle
						.getKeyWhenDetailPanel();
				panelQueryFertigungsgrupperolle.eventYouAreSelected(false);
				panelQueryFertigungsgrupperolle.setSelectedId(oKey);
				panelSplitFertigungsgrupperolle.eventYouAreSelected(false);

			}
		} else if (e.getID() == ItemChangedEvent.ACTION_UPDATE) {
			if (e.getSource() == panelDetailRollerecht) {
				panelQueryRollerecht.updateButtons(new LockStateValue(
						PanelBasis.LOCK_FOR_NEW));
			} else if (e.getSource() == panelDetailLagerrolle) {
				panelQueryLagerrolle.updateButtons(new LockStateValue(
						PanelBasis.LOCK_FOR_NEW));
			} else if (e.getSource() == panelDetailFertigungsgrupperolle) {
				panelQueryFertigungsgrupperolle
						.updateButtons(new LockStateValue(
								PanelBasis.LOCK_FOR_NEW));
			}
		}

		else if (e.getID() == ItemChangedEvent.ITEM_CHANGED) {
			if (e.getSource() == panelQueryRollerecht) {
				Integer iId = (Integer) panelQueryRollerecht.getSelectedId();
				panelDetailRollerecht.setKeyWhenDetailPanel(iId);
				panelDetailRollerecht.eventYouAreSelected(false);
				panelQueryRollerecht.updateButtons();
			} else if (e.getSource() == panelQueryLagerrolle) {
				Integer iId = (Integer) panelQueryLagerrolle.getSelectedId();
				panelDetailLagerrolle.setKeyWhenDetailPanel(iId);
				panelDetailLagerrolle.eventYouAreSelected(false);
				panelQueryLagerrolle.updateButtons();
			} else if (e.getSource() == panelQueryFertigungsgrupperolle) {
				Integer iId = (Integer) panelQueryFertigungsgrupperolle
						.getSelectedId();
				panelDetailFertigungsgrupperolle.setKeyWhenDetailPanel(iId);
				panelDetailFertigungsgrupperolle.eventYouAreSelected(false);
				panelQueryFertigungsgrupperolle.updateButtons();
			} else if (e.getSource() == panelQuerySystemrolle) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();

				getInternalFrameBenutzer().setKeyWasForLockMe(
						panelQuerySystemrolle.getSelectedId() + "");

				getInternalFrameBenutzer().setSystemrolleDto(
						DelegateFactory.getInstance().getBenutzerDelegate()
								.systemrolleFindByPrimaryKey((Integer) key));

				getInternalFrame().setLpTitle(
						InternalFrame.TITLE_IDX_AS_I_LIKE,
						LPMain.getInstance().getTextRespectUISPr(
								"lp.systemrolle")
								+ ": "
								+ getInternalFrameBenutzer()
										.getSystemrolleDto().getCBez());
			}
		}

		else if (e.getID() == ItemChangedEvent.ACTION_YOU_ARE_SELECTED) {
			refreshTitle();
			super.lPEventItemChanged(e);
			refreshTitle();
		}

		else if (e.getID() == ItemChangedEvent.ACTION_GOTO_MY_DEFAULT_QP) {
			// aktiviere ein QP ...
			if (e.getSource() == panelDetailSystemrolle) {
				getInternalFrame().enableAllPanelsExcept(true);
				this.setSelectedComponent(panelQuerySystemrolle);
				setKeyWasForLockMe();
				panelQuerySystemrolle.eventYouAreSelected(false);
			} else if (e.getSource() == panelDetailRollerecht) {
				setKeyWasForLockMe();
				if (panelDetailRollerecht.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryRollerecht
							.getId2SelectAfterDelete();
					panelQueryRollerecht.setSelectedId(oNaechster);
				}

				panelSplit.eventYouAreSelected(false);
			} else if (e.getSource() == panelDetailLagerrolle) {
				setKeyWasForLockMe();
				if (panelDetailLagerrolle.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryLagerrolle
							.getId2SelectAfterDelete();
					panelQueryLagerrolle.setSelectedId(oNaechster);
				}

				panelSplitLagerrolle.eventYouAreSelected(false);
			} else if (e.getSource() == panelDetailFertigungsgrupperolle) {
				setKeyWasForLockMe();
				if (panelDetailFertigungsgrupperolle.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryFertigungsgrupperolle
							.getId2SelectAfterDelete();
					panelQueryFertigungsgrupperolle.setSelectedId(oNaechster);
				}

				panelSplitFertigungsgrupperolle.eventYouAreSelected(false);
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_MY_OWN_NEW) {
			String sAspectInfo = ((ISourceEvent) e.getSource()).getAspect();

			if (sAspectInfo.endsWith(ACTION_SPECIAL_REST)) {
				dialogQuerySystemrolleFromListe(e);
			}
			if (sAspectInfo.endsWith(ACTION_SPECIAL_LAGERRECHTE_REST)) {
				dialogQuerySystemrolleFuerLagerrechteFromListe(e);
			}

		} else if (e.getID() == ItemChangedEvent.ACTION_NEW) {
			if (e.getSource() == panelQuerySystemrolle) {
				getInternalFrameBenutzer().setSystemrolleDto(null);
				panelDetailSystemrolle.eventActionNew(e, true, false);
				this.setSelectedComponent(panelDetailSystemrolle);
			} else if (e.getSource() == panelQueryRollerecht) {
				panelDetailRollerecht.eventActionNew(e, true, false);
				panelDetailRollerecht.eventYouAreSelected(false);
				this.setSelectedComponent(panelSplit);
			} else if (e.getSource() == panelQueryLagerrolle) {
				panelDetailLagerrolle.eventActionNew(e, true, false);
				panelDetailLagerrolle.eventYouAreSelected(false);
				this.setSelectedComponent(panelSplitLagerrolle);
			} else if (e.getSource() == panelQueryFertigungsgrupperolle) {
				panelDetailFertigungsgrupperolle.eventActionNew(e, true, false);
				panelDetailFertigungsgrupperolle.eventYouAreSelected(false);
				this.setSelectedComponent(panelSplitFertigungsgrupperolle);
			}
		}
		disableChangingIfUserCount();
	}

	public InternalFrameBenutzer getInternalFrameBenutzer() {
		return (InternalFrameBenutzer) getInternalFrame();
	}

	public void setKeyWasForLockMe() {
		Object oKey = panelQuerySystemrolle.getSelectedId();

		if (oKey != null) {
			getInternalFrame().setKeyWasForLockMe(oKey.toString());
		} else {
			getInternalFrame().setKeyWasForLockMe(null);
		}
	}

	private void refreshTitle() {

		getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_OHRWASCHLUNTEN,
				LPMain.getInstance().getTextRespectUISPr("lp.systemrolle"));
		getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_OHRWASCHLOBEN,
				((PanelBasis) this.getSelectedComponent()).getAdd2Title());
		if (getInternalFrameBenutzer().getSystemrolleDto() != null) {

			getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_AS_I_LIKE,
					getInternalFrameBenutzer().getSystemrolleDto().getCBez());
		}

	}

	public void lPEventObjectChanged(ChangeEvent e) throws Throwable {
		super.lPEventObjectChanged(e);

		int selectedIndex = this.getSelectedIndex();

		if (selectedIndex == IDX_PANEL_AUSWAHL) {
			panelQuerySystemrolle.eventYouAreSelected(false);
			panelQuerySystemrolle.updateButtons();
		} else if (selectedIndex == IDX_PANEL_DETAIL) {
			panelDetailSystemrolle.eventYouAreSelected(false);
		} else if (selectedIndex == IDX_PANEL_RECHTE) {
			panelQueryRollerecht.setDefaultFilter(BenutzerFilterFactory
					.getInstance().createFKRollerecht(
							getInternalFrameBenutzer().getSystemrolleDto()
									.getIId()));

			panelSplit.eventYouAreSelected(false);
			panelQueryRollerecht.updateButtons();
		} else if (selectedIndex == IDX_PANEL_LAGER) {
			panelQueryLagerrolle.setDefaultFilter(BenutzerFilterFactory
					.getInstance().createFKRollerecht(
							getInternalFrameBenutzer().getSystemrolleDto()
									.getIId()));

			panelSplitLagerrolle.eventYouAreSelected(false);
			panelQueryLagerrolle.updateButtons();
		}else if (selectedIndex == IDX_PANEL_FERTIGUNGSGRUPPE) {
			panelQueryFertigungsgrupperolle.setDefaultFilter(BenutzerFilterFactory
					.getInstance().createFKRollerecht(
							getInternalFrameBenutzer().getSystemrolleDto()
									.getIId()));

			panelSplitFertigungsgrupperolle.eventYouAreSelected(false);
			panelQueryFertigungsgrupperolle.updateButtons();
		}
		disableChangingIfUserCount();
		refreshTitle();
	}
	
	protected void disableChangingIfUserCount() {
		if(getInternalFrameBenutzer().getSystemrolleDto() != null) {
			if(getInternalFrameBenutzer().getSystemrolleDto().getIMaxUsers() != null) {
				panelDetailSystemrolle.enableToolsPanelButtons(false, PanelBasis.ACTION_DELETE, PanelBasis.ACTION_UPDATE);
				panelDetailRollerecht.enableToolsPanelButtons(false, PanelBasis.ACTION_DELETE, PanelBasis.ACTION_UPDATE, PanelBasis.ACTION_REFRESH);
				panelQueryRollerecht.enableToolsPanelButtons(false, PanelBasis.ACTION_NEW, BUTTON_RESTLICHERECHTESPEICHERN);
				if(panelQueryFertigungsgrupperolle != null) {
					panelQueryFertigungsgrupperolle.enableToolsPanelButtons(false, PanelBasis.ACTION_NEW, BUTTON_RESTLICHERECHTESPEICHERN);
					panelQueryFertigungsgrupperolle.enableToolsPanelButtons(false, PanelBasis.ACTION_NEW);
					panelDetailFertigungsgrupperolle.enableToolsPanelButtons(false, PanelBasis.ACTION_UPDATE, PanelBasis.ACTION_DELETE, PanelBasis.ACTION_REFRESH);
				}
				panelQueryLagerrolle.enableToolsPanelButtons(false, PanelBasis.ACTION_NEW, BUTTON_RESTLICHELAGERRECHTESPEICHERN);
				panelDetailLagerrolle.enableToolsPanelButtons(false, PanelBasis.ACTION_UPDATE, PanelBasis.ACTION_DELETE, PanelBasis.ACTION_REFRESH);
			}
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
