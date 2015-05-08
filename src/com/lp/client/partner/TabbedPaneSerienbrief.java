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
package com.lp.client.partner;

import java.awt.event.ActionEvent;
import java.util.EventObject;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
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
import com.lp.client.stueckliste.PanelDialogStuecklisteKommentar;
import com.lp.client.util.fastlanereader.gui.QueryType;
import com.lp.server.partner.fastlanereader.generated.service.FLRSerienbriefselektionPK;
import com.lp.server.partner.fastlanereader.generated.service.FLRSerienbriefselektionnegativPK;
import com.lp.server.partner.service.PASelektionDto;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.partner.service.SerienbriefDto;
import com.lp.server.partner.service.SerienbriefselektionDto;
import com.lp.server.partner.service.SerienbriefselektionnegativDto;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;

@SuppressWarnings("static-access")
/**
 * <p> Diese Klasse kuemmert sich den Serienbrief.</p>
 *
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 *
 * <p>Erstellung: Josef Ornetsmueller; 16.11.05</p>
 *
 * <p>@author $Author: christian $</p>
 *
 * @version not attributable Date $Date: 2012/10/19 09:32:02 $
 */
public class TabbedPaneSerienbrief extends TabbedPane {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private PartnerDto partnerDto = new PartnerDto();
	private SerienbriefDto serienbriefDto = new SerienbriefDto();
	private SerienbriefselektionDto serienbriefselektionDto = new SerienbriefselektionDto();
	private SerienbriefselektionnegativDto serienbriefselektionnegativDto = new SerienbriefselektionnegativDto();
	private PASelektionDto pASelektionDto = new PASelektionDto();

	private final static int IDX_PANEL_SERIENBRIEF_QP1 = 0;
	private final static int IDX_PANEL_SERIENBRIEF_KOPFDATEN_D2 = 1;
	private final static int IDX_PANEL_SERIENBRIEF_SELEKTION_SP3 = 2;
	private final static int IDX_PANEL_SERIENBRIEF_SELEKTIONNEGATIV_SP3 = 3;

	private final String MENUE_ACTION_EMPFAENGERLISTE = "MENUE_ACTION_EMPFAENGERLISTE";

	private final String MENU_BEARBEITEN_MAILTEXT = "MENU_BEARBEITEN_MAILTEXT";

	private PanelQuery panelSerienbriefQP1 = null;

	private WrapperMenuBar wrapperMenuBar = null;

	private PanelSerienbriefKopfdaten panelSerienbriefKopfdatenD2 = null;

	private PanelQuery panelSerienbriefSelektionTopQP3 = null;
	private PanelPartnerSerienbriefSelektion panelSerienbriefSelektionBottomD3 = null;
	private PanelSplit panelSerienbriefSelektionSP3 = null;

	private PanelQuery panelSerienbriefSelektionnegativTopQP3 = null;
	private PanelPartnerSerienbriefselektionnegativ panelSerienbriefSelektionnegativBottomD3 = null;
	private PanelSplit panelSerienbriefSelektionnegativSP3 = null;

	public TabbedPaneSerienbrief(InternalFrame internalFrameI) throws Throwable {
		super(internalFrameI, LPMain.getTextRespectUISPr("lp.serienbrief"));
		jbInit();
		initComponents();
	}

	/**
	 * jbInit
	 * 
	 * @throws Throwable
	 */
	private void jbInit() throws Throwable {
		// 1 tab oben: QP1 PartnerFLR; lazy loading
		insertTab(LPMain.getInstance().getTextRespectUISPr("lp.auswahl"), null,
				null, LPMain.getInstance().getTextRespectUISPr("lp.auswahl"),
				IDX_PANEL_SERIENBRIEF_QP1);

		// 2 tab oben: D2 Serienbrief Kopfdaten; lazy loading
		insertTab(
				LPMain.getInstance().getTextRespectUISPr("anf.panel.kopfdaten"),
				null,
				null,
				LPMain.getInstance().getTextRespectUISPr("anf.panel.kopfdaten"),
				IDX_PANEL_SERIENBRIEF_KOPFDATEN_D2);

		// 3 tab oben; Splitpane Serienbrief; lazy loading
		insertTab(LPMain.getInstance().getTextRespectUISPr("lp.selektion"),
				null, null,
				LPMain.getInstance().getTextRespectUISPr("lp.selektion"),
				IDX_PANEL_SERIENBRIEF_SELEKTION_SP3);

		// 4 tab oben; Splitpane Serienbriefnegativ; lazy loading
		insertTab(
				LPMain.getInstance().getTextRespectUISPr(
						"part.negative.selektion"),
				null,
				null,
				LPMain.getInstance().getTextRespectUISPr(
						"part.negative.selektion"),
				IDX_PANEL_SERIENBRIEF_SELEKTIONNEGATIV_SP3);

		// defaults
		// QP1 ist default.
		setSelectedComponent(panelSerienbriefQP1);
		refreshSerienbriefeQP1();

		getPartnerDto().setIId((Integer) panelSerienbriefQP1.getSelectedId());

		if ((Integer) panelSerienbriefQP1.getSelectedId() != null) {
			setSerienbriefDto(DelegateFactory
					.getInstance()
					.getPartnerServicesDelegate()
					.serienbriefFindByPrimaryKey(
							(Integer) panelSerienbriefQP1.getSelectedId()));
		}

		// damit D2 einen aktuellen hat.
		ItemChangedEvent it = new ItemChangedEvent(panelSerienbriefQP1,
				ItemChangedEvent.ITEM_CHANGED);
		lPEventItemChanged(it);

		addChangeListener(this);
		getInternalFrame().addItemChangedListener(this);

		getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_OHRWASCHLUNTEN,
				LPMain.getInstance().getTextRespectUISPr("part.partner"));
	}

	protected JMenuBar getJMenuBar() throws Throwable {

		if (wrapperMenuBar == null) {
			wrapperMenuBar = new WrapperMenuBar(this);

			JMenuItem menuItemEmpfaenger = new JMenuItem(LPMain.getInstance()
					.getTextRespectUISPr("partner.report.empfaengerliste"));
			menuItemEmpfaenger.addActionListener(this);
			menuItemEmpfaenger.setActionCommand(MENUE_ACTION_EMPFAENGERLISTE);

			JMenu menuInfo = new WrapperMenu("lp.info", this);
			menuInfo.add(menuItemEmpfaenger);

			wrapperMenuBar.addJMenuItem(menuInfo);

			JMenu jmBearbeiten = (JMenu) wrapperMenuBar
					.getComponent(WrapperMenuBar.MENU_BEARBEITEN);

			JMenuItem menuItemBearbeitenMailtext = new JMenuItem(LPMain
					.getInstance().getTextRespectUISPr("lp.emailtext"));
			menuItemBearbeitenMailtext.addActionListener(this);
			menuItemBearbeitenMailtext
					.setActionCommand(MENU_BEARBEITEN_MAILTEXT);
			jmBearbeiten.add(menuItemBearbeitenMailtext, 0);

		}
		return wrapperMenuBar;
	}

	/**
	 * Behandle ActionEvent; zB Menue-Klick oben.
	 * 
	 * @param e
	 *            ActionEvent
	 * @throws Throwable
	 */
	protected void lPActionEvent(ActionEvent e) throws Throwable {
		if (e.getActionCommand().equals(MENUE_ACTION_EMPFAENGERLISTE)) {
			String add2Title = LPMain.getInstance().getTextRespectUISPr(
					"partner.report.empfaengerliste");
			getInternalFrame().showReportKriterien(
					new ReportEmpfaengerliste(getInternalFrame(),
							getSerienbriefDto().getIId(), add2Title));
		} else if (e.getActionCommand().equals(MENU_BEARBEITEN_MAILTEXT)) {

			if (panelSerienbriefKopfdatenD2 == null
					|| !panelSerienbriefKopfdatenD2.isLockedDlg()) {
				getInternalFrame().showPanelDialog(
						new PanelDialogMailtext(getInternalFrame(), this,
								LPMain.getInstance().getTextRespectUISPr(
										"lp.emailtext")));

			}
		}

	}

	private void refreshTitle() {

		getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_OHRWASCHLUNTEN,
				LPMain.getInstance().getTextRespectUISPr("part.partner"));
		getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_OHRWASCHLOBEN,
				((PanelBasis) this.getSelectedComponent()).getAdd2Title());
		String sBezeichnung = "";

		if (getSerienbriefDto() != null) {
			sBezeichnung = getSerienbriefDto().getCBez();
		}
		getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_AS_I_LIKE,
				sBezeichnung);

	}

	public void lPEventObjectChanged(ChangeEvent e) throws Throwable {
		super.lPEventObjectChanged(e);

		int selectedIndex = this.getSelectedIndex();

		if (selectedIndex == IDX_PANEL_SERIENBRIEF_QP1) {
			refreshSerienbriefeQP1();
			panelSerienbriefQP1.eventYouAreSelected(false);
			panelSerienbriefQP1.updateButtons();
		}

		else if (selectedIndex == IDX_PANEL_SERIENBRIEF_KOPFDATEN_D2) {
			Integer iId = (Integer) panelSerienbriefQP1.getSelectedId();
			refreshSerienbriefKopfdatenD2(iId);
			panelSerienbriefKopfdatenD2.eventYouAreSelected(false);
		}

		else if (selectedIndex == IDX_PANEL_SERIENBRIEF_SELEKTION_SP3) {
			Integer iId = (Integer) panelSerienbriefQP1.getSelectedId();
			refreshSerienbriefSelektionSP3(iId);
			panelSerienbriefSelektionSP3.eventYouAreSelected(false);
			panelSerienbriefSelektionTopQP3.updateButtons();
		}

		else if (selectedIndex == IDX_PANEL_SERIENBRIEF_SELEKTIONNEGATIV_SP3) {
			Integer iId = (Integer) panelSerienbriefQP1.getSelectedId();
			refreshSerienbriefSelektionnegativSP3(iId);
			panelSerienbriefSelektionnegativSP3.eventYouAreSelected(false);
			panelSerienbriefSelektionnegativTopQP3.updateButtons();
		}

		refreshTitle();
	}

	/**
	 * refreshSerienbriefSelektionSP2
	 * 
	 * @throws Throwable
	 * @param iIdSerienbriefI
	 *            Integer
	 */
	private void refreshSerienbriefSelektionSP3(Integer iIdSerienbriefI)
			throws Throwable {

		if (panelSerienbriefSelektionTopQP3 == null) {
			String[] aWhichStandardButtonIUse = { PanelBasis.ACTION_NEW };

			QueryType[] querytypes = null;
			FilterKriterium[] filters = PartnerFilterFactory.getInstance()
					.createFKSerienbrief(iIdSerienbriefI);
			panelSerienbriefSelektionTopQP3 = new PanelQuery(querytypes,
					filters, QueryParameters.UC_ID_SERIENBRIEFSELEKTION,
					aWhichStandardButtonIUse, getInternalFrame(), LPMain
							.getInstance().getTextRespectUISPr(
									"lp.uebersicht.detail"), true);

			panelSerienbriefSelektionBottomD3 = new PanelPartnerSerienbriefSelektion(
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("lp.selektion"), null, this);

			panelSerienbriefSelektionSP3 = new PanelSplit(getInternalFrame(),
					panelSerienbriefSelektionBottomD3,
					panelSerienbriefSelektionTopQP3, 200);
			setComponentAt(IDX_PANEL_SERIENBRIEF_SELEKTION_SP3,
					panelSerienbriefSelektionSP3);
		} else {
			// filter refreshen.
			panelSerienbriefSelektionTopQP3
					.setDefaultFilter(PartnerFilterFactory.getInstance()
							.createFKSerienbriefSelektion(iIdSerienbriefI));
		}

	}

	private void refreshSerienbriefSelektionnegativSP3(Integer iIdSerienbriefI)
			throws Throwable {

		if (panelSerienbriefSelektionnegativTopQP3 == null) {
			String[] aWhichStandardButtonIUse = { PanelBasis.ACTION_NEW };

			QueryType[] querytypes = null;
			FilterKriterium[] filters = PartnerFilterFactory.getInstance()
					.createFKSerienbrief(iIdSerienbriefI);
			panelSerienbriefSelektionnegativTopQP3 = new PanelQuery(querytypes,
					filters, QueryParameters.UC_ID_SERIENBRIEFSELEKTIONNEGATIV,
					aWhichStandardButtonIUse, getInternalFrame(), LPMain
							.getInstance().getTextRespectUISPr(
									"lp.uebersicht.detail"), true);

			panelSerienbriefSelektionnegativBottomD3 = new PanelPartnerSerienbriefselektionnegativ(
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("part.negative.selektion"),
					null, this);

			panelSerienbriefSelektionnegativSP3 = new PanelSplit(
					getInternalFrame(),
					panelSerienbriefSelektionnegativBottomD3,
					panelSerienbriefSelektionnegativTopQP3, 200);
			setComponentAt(IDX_PANEL_SERIENBRIEF_SELEKTIONNEGATIV_SP3,
					panelSerienbriefSelektionnegativSP3);
		} else {
			// filter refreshen.
			panelSerienbriefSelektionnegativTopQP3
					.setDefaultFilter(PartnerFilterFactory.getInstance()
							.createFKSerienbriefSelektion(iIdSerienbriefI));
		}

	}

	public void lPEventItemChanged(ItemChangedEvent eI) throws Throwable {

		if (eI.getID() == ItemChangedEvent.ITEM_CHANGED) {
			if (eI.getSource() == panelSerienbriefQP1) {
				Object key = ((ISourceEvent) eI.getSource()).getIdSelected();

				if (key != null) {
					serienbriefDto = DelegateFactory.getInstance()
							.getPartnerServicesDelegate()
							.serienbriefFindByPrimaryKey((Integer) key);
					getInternalFrame().setKeyWasForLockMe(key + "");
					getInternalFrame().enableAllOberePanelsExceptMe(this,
							IDX_PANEL_SERIENBRIEF_QP1, (key != null));
				} else {

					getInternalFrame().enableAllOberePanelsExceptMe(this,
							IDX_PANEL_SERIENBRIEF_QP1, false);
				}

				if (getSerienbriefDto() != null) {
					getInternalFrame().setLpTitle(
							InternalFrame.TITLE_IDX_AS_I_LIKE,
							getSerienbriefDto().getCBez());
				}

			} else if (eI.getSource() == panelSerienbriefSelektionTopQP3) {
				FLRSerienbriefselektionPK fLRSerienbriefselektionPK = (FLRSerienbriefselektionPK) panelSerienbriefSelektionTopQP3
						.getSelectedId();
				panelSerienbriefSelektionBottomD3
						.setKeyWhenDetailPanel(fLRSerienbriefselektionPK);
				panelSerienbriefSelektionBottomD3.eventYouAreSelected(false);
				panelSerienbriefSelektionTopQP3.updateButtons();
			} else if (eI.getSource() == panelSerienbriefSelektionnegativTopQP3) {
				FLRSerienbriefselektionnegativPK fLRSerienbriefselektionnegativPK = (FLRSerienbriefselektionnegativPK) panelSerienbriefSelektionnegativTopQP3
						.getSelectedId();
				panelSerienbriefSelektionnegativBottomD3
						.setKeyWhenDetailPanel(fLRSerienbriefselektionnegativPK);
				panelSerienbriefSelektionnegativBottomD3
						.eventYouAreSelected(false);
				panelSerienbriefSelektionnegativTopQP3.updateButtons();
			}
		} else if (eI.getID() == ItemChangedEvent.ACTION_YOU_ARE_SELECTED) {
			refreshTitle();
			super.lPEventItemChanged(eI);
		} else if (eI.getID() == ItemChangedEvent.ACTION_NEW) {
			if (eI.getSource() == panelSerienbriefQP1) {
				refreshSerienbriefKopfdatenD2(null);
				panelSerienbriefKopfdatenD2.eventActionNew(eI, true, false);
				setSelectedComponent(panelSerienbriefKopfdatenD2);
			} else if (eI.getSource() == panelSerienbriefSelektionTopQP3) {
				// refreshSerienbriefSelektionSP3();
				panelSerienbriefSelektionBottomD3.eventActionNew(eI, true,
						false);
				panelSerienbriefSelektionBottomD3.eventYouAreSelected(false);
				setSelectedComponent(panelSerienbriefSelektionSP3);
			} else if (eI.getSource() == panelSerienbriefSelektionnegativTopQP3) {
				// refreshSerienbriefSelektionSP3();
				panelSerienbriefSelektionnegativBottomD3.eventActionNew(eI,
						true, false);
				panelSerienbriefSelektionnegativBottomD3
						.eventYouAreSelected(false);
				setSelectedComponent(panelSerienbriefSelektionnegativSP3);
			}
		}

		else if (eI.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			if (eI.getSource() == panelSerienbriefQP1) {
				// jetzt ab zu D2.
				setSelectedComponent(panelSerienbriefKopfdatenD2);
			}
		}

		else if (eI.getID() == ItemChangedEvent.ACTION_GOTO_MY_DEFAULT_QP) {
			if (eI.getSource() == panelSerienbriefKopfdatenD2) {
				panelSerienbriefQP1.eventYouAreSelected(false);
				getSerienbriefDto().setIId(
						(Integer) panelSerienbriefQP1.getSelectedId());
				getInternalFrame().setKeyWasForLockMe(
						panelSerienbriefQP1.getSelectedId() + "");
				setSelectedComponent(panelSerienbriefQP1);
			} else if (eI.getSource() == panelSerienbriefSelektionBottomD3) {
				Integer iId = (Integer) panelSerienbriefQP1.getSelectedId();
				refreshSerienbriefSelektionSP3(iId);
				getInternalFrame().setKeyWasForLockMe(iId + "");
				panelSerienbriefSelektionSP3.eventYouAreSelected(false);
			} else if (eI.getSource() == panelSerienbriefSelektionnegativBottomD3) {
				Integer iId = (Integer) panelSerienbriefQP1.getSelectedId();
				refreshSerienbriefSelektionnegativSP3(iId);
				getInternalFrame().setKeyWasForLockMe(iId + "");
				panelSerienbriefSelektionnegativSP3.eventYouAreSelected(false);
			}
		}

		else if (eI.getID() == ItemChangedEvent.ACTION_DISCARD) {
			if (eI.getSource() == panelSerienbriefKopfdatenD2) {
				panelSerienbriefKopfdatenD2.eventYouAreSelected(false);
			} else if (eI.getSource() == panelSerienbriefSelektionBottomD3) {
				panelSerienbriefSelektionSP3.eventYouAreSelected(false); // refresh
																			// auf
																			// das
																			// gesamte
																			// 1:n
																			// panel
			} else if (eI.getSource() == panelSerienbriefSelektionnegativBottomD3) {
				panelSerienbriefSelektionnegativSP3.eventYouAreSelected(false); // refresh
				// auf
				// das
				// gesamte
				// 1:n
				// panel
			}
		}

		else if (eI.getID() == ItemChangedEvent.ACTION_UPDATE) {
			// hier kommt man nach upd im D bei einem 1:n hin.
			if (eI.getSource() == panelSerienbriefSelektionBottomD3) {
				panelSerienbriefSelektionTopQP3
						.updateButtons(new LockStateValue(
								PanelBasis.LOCK_FOR_NEW));
			} else if (eI.getSource() == panelSerienbriefSelektionnegativBottomD3) {
				panelSerienbriefSelektionnegativTopQP3
						.updateButtons(new LockStateValue(
								PanelBasis.LOCK_FOR_NEW));
			}
		}

		else if (eI.getID() == ItemChangedEvent.ACTION_GOTO_MY_DEFAULT_QP) {
			if (eI.getSource() == panelSerienbriefKopfdatenD2) {
				panelSerienbriefQP1.eventYouAreSelected(false);
				getSerienbriefDto().setIId(
						(Integer) panelSerienbriefQP1.getSelectedId());
				getInternalFrame().setKeyWasForLockMe(
						panelSerienbriefQP1.getSelectedId() + "");
				setSelectedComponent(panelSerienbriefQP1);
			} else if (eI.getSource() == panelSerienbriefSelektionTopQP3) {
				Integer iIdSerienbrief = (Integer) panelSerienbriefQP1
						.getSelectedId();
				refreshSerienbriefSelektionSP3(iIdSerienbrief);
				getInternalFrame().setKeyWasForLockMe(iIdSerienbrief + "");
				panelSerienbriefSelektionBottomD3.eventYouAreSelected(false);
			} else if (eI.getSource() == panelSerienbriefSelektionnegativTopQP3) {
				Integer iIdSerienbrief = (Integer) panelSerienbriefQP1
						.getSelectedId();
				refreshSerienbriefSelektionnegativSP3(iIdSerienbrief);
				getInternalFrame().setKeyWasForLockMe(iIdSerienbrief + "");
				panelSerienbriefSelektionnegativBottomD3
						.eventYouAreSelected(false);
			}
		}

		else if (eI.getID() == ItemChangedEvent.ACTION_SAVE) {
			if (eI.getSource() == panelSerienbriefKopfdatenD2) {
				panelSerienbriefQP1.clearDirektFilter();
				Object oKey = panelSerienbriefKopfdatenD2
						.getKeyWhenDetailPanel();

				panelSerienbriefQP1.setSelectedId(oKey);
			}

			else if (eI.getSource() == panelSerienbriefSelektionBottomD3) {
				Object oKey = panelSerienbriefSelektionBottomD3
						.getKeyWhenDetailPanel();
				panelSerienbriefSelektionTopQP3.eventYouAreSelected(false);
				panelSerienbriefSelektionTopQP3.setSelectedId(oKey);
				panelSerienbriefSelektionSP3.eventYouAreSelected(false);
			} else if (eI.getSource() == panelSerienbriefSelektionnegativBottomD3) {
				Object oKey = panelSerienbriefSelektionnegativBottomD3
						.getKeyWhenDetailPanel();
				panelSerienbriefSelektionnegativTopQP3
						.eventYouAreSelected(false);
				panelSerienbriefSelektionnegativTopQP3.setSelectedId(oKey);
				panelSerienbriefSelektionnegativSP3.eventYouAreSelected(false);
			}
		}

	}

	private void refreshSerienbriefeQP1() throws Throwable {

		if (panelSerienbriefQP1 == null) {
			String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW };
			panelSerienbriefQP1 = new PanelQuery(null, null,
					QueryParameters.UC_ID_PARTNERSERIENBRIEF, aWhichButtonIUse,
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("lp.auswahl"), true);
			setComponentAt(IDX_PANEL_SERIENBRIEF_QP1, panelSerienbriefQP1);
		}
	}

	private void refreshSerienbriefKopfdatenD2(Integer key) throws Throwable {

		if (panelSerienbriefKopfdatenD2 == null) {
			panelSerienbriefKopfdatenD2 = new PanelSerienbriefKopfdaten(
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("anf.panel.kopfdaten"), key,
					this);
			setComponentAt(IDX_PANEL_SERIENBRIEF_KOPFDATEN_D2,
					panelSerienbriefKopfdatenD2);
		}
	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
	}

	public PartnerDto getPartnerDto() {
		return partnerDto;
	}

	public void setPartnerDto(PartnerDto partnerDto) {
		this.partnerDto = partnerDto;
	}

	public SerienbriefDto getSerienbriefDto() {
		return serienbriefDto;
	}

	public void setSerienbriefDto(SerienbriefDto serienbriefDto) {
		this.serienbriefDto = serienbriefDto;
	}

	public SerienbriefselektionDto getSerienbriefselektionDto() {
		return serienbriefselektionDto;
	}
	

	public SerienbriefselektionnegativDto getSerienbriefselektionnegativDto() {
		return serienbriefselektionnegativDto;
	}

	public void setSerienbriefselektionnegativDto(
			SerienbriefselektionnegativDto serienbriefselektionnegativDto) {
		this.serienbriefselektionnegativDto = serienbriefselektionnegativDto;
	}

	public void setSerienbriefselektionDto(
			SerienbriefselektionDto serienbriefselektionDto) {
		this.serienbriefselektionDto = serienbriefselektionDto;
	}

	public PASelektionDto getPASelektionDto() {
		return pASelektionDto;
	}

	public void setPASelektionDto(PASelektionDto pASelektionDto) {
		this.pASelektionDto = pASelektionDto;
	}

	public Object getDto() {
		return serienbriefDto;
	}

}
