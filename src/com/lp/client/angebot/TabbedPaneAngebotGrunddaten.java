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
package com.lp.client.angebot;

import javax.swing.event.ChangeEvent;

import com.lp.client.frame.HelperClient;
import com.lp.client.frame.LockStateValue;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQuery;
import com.lp.client.frame.component.PanelSplit;
import com.lp.client.frame.component.TabbedPane;
import com.lp.client.frame.component.WrapperMenuBar;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.stammdatencrud.PanelStammdatenCRUD;
import com.lp.client.pc.LPMain;
import com.lp.server.system.service.MediaFac;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;

@SuppressWarnings("static-access")
/**
 * <p><I>Diese Klasse kuemmert sich um die Wartung der Grunddaten des Angebots.</I> </p>
 *
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 *
 * <p>Erstellungsdatum <I>04.07.05</I></p>
 *
 * <p> </p>
 *
 * @author Uli Walch
 * @version $Revision: 1.4 $
 */
public class TabbedPaneAngebotGrunddaten extends TabbedPane {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private PanelQuery panelAngebotartTopQP1 = null;
	private PanelSplit panelAngebotartSP1 = null;
	private PanelBasis panelAngebotartBottomD1 = null;

	private PanelQuery panelAngebotpositionartTopQP1 = null;
	private PanelSplit panelAngebotpositionartSP1 = null;
	private PanelBasis panelAngebotpositionartBottomD1 = null;

	private PanelQuery panelAngeboterledigungsgrundTopQP1 = null;
	private PanelSplit panelAngeboterledigungsgrundSP1 = null;
	private PanelBasis panelAngeboterledigungsgrundBottomD1 = null;

	private PanelQuery panelAngebottextTopQP;
	private PanelSplit panelAngebottextSP;
	private PanelStammdatenCRUD panelAngebottextBottomD;

	private static final int IDX_PANEL_ANGEBOTART = 0;
	private static final int IDX_PANEL_ANGEBOTPOSITIONART = 1;
	private static final int IDX_PANEL_ANGEBOTERLEDIGUNGSGRUND = 2;
	private static final int IDX_PANEL_ANGEBOTTEXT = 3;

	public TabbedPaneAngebotGrunddaten(InternalFrame internalFrameI)
			throws Throwable {
		super(internalFrameI, LPMain.getTextRespectUISPr("lp.grunddaten"));
		jbInit();
		initComponents();
	}

	public InternalFrameAngebot getInternalFrameAngebot() {
		return (InternalFrameAngebot) getInternalFrame();
	}

	private void jbInit() throws Throwable {
		insertTab(LPMain.getInstance().getTextRespectUISPr("angb.angebotart"),
				null, null,
				LPMain.getInstance().getTextRespectUISPr("angb.angebotart"),
				IDX_PANEL_ANGEBOTART);

		insertTab(
				LPMain.getInstance().getTextRespectUISPr(
						"angb.angebotpositionart"),
				null,
				null,
				LPMain.getInstance().getTextRespectUISPr(
						"angb.angebotpositionart"),
				IDX_PANEL_ANGEBOTPOSITIONART);

		insertTab(
				LPMain.getInstance().getTextRespectUISPr(
						"angb.angeboterledigungsgrund"),
				null,
				null,
				LPMain.getInstance().getTextRespectUISPr(
						"angb.angeboterledigungsgrund"),
				IDX_PANEL_ANGEBOTERLEDIGUNGSGRUND);

		insertTab(
				LPMain.getInstance().getTextRespectUISPr("angb.angebotstext"),
				null, null,
				LPMain.getInstance().getTextRespectUISPr("angb.angebotstext"),
				IDX_PANEL_ANGEBOTTEXT);

		// default
		refreshPanelAngebotart();

		setSelectedComponent(panelAngebotartSP1);

		panelAngebotartTopQP1.eventYouAreSelected(false);

		getInternalFrameAngebot().getAngebotartDto().setCNr(
				panelAngebotartTopQP1.getSelectedId() + "");

		addChangeListener(this);
		getInternalFrame().addItemChangedListener(this);
	}

	public void lPEventItemChanged(ItemChangedEvent eI) throws Throwable {

		ItemChangedEvent e = (ItemChangedEvent) eI;

		if (e.getID() == ItemChangedEvent.ITEM_CHANGED) {
			if (e.getSource() == panelAngebotartTopQP1) {
				String cNr = (String) panelAngebotartTopQP1.getSelectedId();
				getInternalFrame().setKeyWasForLockMe(cNr);
				panelAngebotartBottomD1.setKeyWhenDetailPanel(cNr);
				panelAngebotartBottomD1.eventYouAreSelected(false);

				panelAngebotartTopQP1.updateButtons(panelAngebotartBottomD1
						.getLockedstateDetailMainKey());
			} else if (e.getSource() == panelAngebotpositionartTopQP1) {
				String cNr = (String) panelAngebotpositionartTopQP1
						.getSelectedId();
				getInternalFrame().setKeyWasForLockMe(cNr);
				panelAngebotpositionartBottomD1.setKeyWhenDetailPanel(cNr);
				panelAngebotpositionartBottomD1.eventYouAreSelected(false);

				panelAngebotpositionartTopQP1.updateButtons();
			} else if (e.getSource() == panelAngeboterledigungsgrundTopQP1) {
				String cNr = (String) panelAngeboterledigungsgrundTopQP1
						.getSelectedId();
				getInternalFrame().setKeyWasForLockMe(cNr);
				panelAngeboterledigungsgrundBottomD1.setKeyWhenDetailPanel(cNr);
				panelAngeboterledigungsgrundBottomD1.eventYouAreSelected(false);

				panelAngeboterledigungsgrundTopQP1
						.updateButtons(panelAngeboterledigungsgrundBottomD1
								.getLockedstateDetailMainKey());
			} else if (eI.getSource() == panelAngebottextTopQP) {
				Integer iId = (Integer) panelAngebottextTopQP.getSelectedId();
				getInternalFrame().setKeyWasForLockMe(iId + "");
				panelAngebottextBottomD.setKeyWhenDetailPanel(iId);
				panelAngebottextBottomD.eventYouAreSelected(false);

				panelAngebottextTopQP.updateButtons(panelAngebottextBottomD
						.getLockedstateDetailMainKey());
			}
		}

		else if (eI.getID() == ItemChangedEvent.ACTION_UPDATE) {
			// hier kommt man nach upd im D bei einem 1:n hin.
			if (eI.getSource() == panelAngebotartBottomD1) {
				panelAngebotartTopQP1.updateButtons(new LockStateValue(
						PanelBasis.LOCK_FOR_NEW));
				;
			} else if (eI.getSource() == panelAngebotpositionartBottomD1) {
				panelAngebotpositionartTopQP1.updateButtons(new LockStateValue(
						PanelBasis.LOCK_FOR_NEW));
				;
			} else if (eI.getSource() == panelAngeboterledigungsgrundBottomD1) {
				panelAngeboterledigungsgrundTopQP1
						.updateButtons(new LockStateValue(
								PanelBasis.LOCK_FOR_NEW));
				;
			} else if (eI.getSource() == panelAngebottextBottomD) {
				panelAngebottextTopQP.updateButtons(new LockStateValue(
						PanelBasis.LOCK_FOR_NEW));
				;
			}
		}

		else if (e.getID() == ItemChangedEvent.ACTION_DISCARD) {
			if (e.getSource() == panelAngebotartBottomD1) {
				panelAngebotartSP1.eventYouAreSelected(false);
			} else if (e.getSource() == panelAngebotpositionartBottomD1) {
				panelAngebotpositionartSP1.eventYouAreSelected(false);
			} else if (e.getSource() == panelAngeboterledigungsgrundBottomD1) {
				panelAngeboterledigungsgrundSP1.eventYouAreSelected(false);
			} else if (e.getSource() == panelAngebottextBottomD) {
				panelAngebottextSP.eventYouAreSelected(false);
			}
		}

		else if (e.getID() == ItemChangedEvent.ACTION_SAVE) {
			if (e.getSource() == panelAngebotartBottomD1) {
				Object oKey = panelAngebotartBottomD1.getKeyWhenDetailPanel();
				panelAngebotartTopQP1.eventYouAreSelected(false);
				panelAngebotartTopQP1.setSelectedId(oKey);
				panelAngebotartSP1.eventYouAreSelected(false);
			} else if (e.getSource() == panelAngebotpositionartBottomD1) {
				Object oKey = panelAngebotpositionartBottomD1
						.getKeyWhenDetailPanel();
				panelAngebotpositionartTopQP1.eventYouAreSelected(false);
				panelAngebotpositionartTopQP1.setSelectedId(oKey);
				panelAngebotpositionartSP1.eventYouAreSelected(false);
			} else if (e.getSource() == panelAngeboterledigungsgrundBottomD1) {
				Object oKey = panelAngeboterledigungsgrundBottomD1
						.getKeyWhenDetailPanel();
				panelAngeboterledigungsgrundTopQP1.eventYouAreSelected(false);
				panelAngeboterledigungsgrundTopQP1.setSelectedId(oKey);
				panelAngeboterledigungsgrundSP1.eventYouAreSelected(false);
			} else if (e.getSource() == panelAngebottextBottomD) {
				Object oKey = panelAngebottextBottomD.getKeyWhenDetailPanel();
				panelAngebottextTopQP.eventYouAreSelected(false);
				panelAngebottextTopQP.setSelectedId(oKey);
				panelAngebottextSP.eventYouAreSelected(false);
			}
		}

		else if (e.getID() == ItemChangedEvent.ACTION_GOTO_MY_DEFAULT_QP) {
			if (e.getSource() == panelAngebotartBottomD1) {
				panelAngebotartSP1.eventYouAreSelected(false); // refresh auf
																// das gesamte
																// 1:n panel
			} else if (e.getSource() == panelAngebotpositionartBottomD1) {
				panelAngebotpositionartSP1.eventYouAreSelected(false); // refresh
																		// auf
																		// das
																		// gesamte
																		// 1:n
																		// panel
			} else if (e.getSource() == panelAngeboterledigungsgrundBottomD1) {
				panelAngeboterledigungsgrundSP1.eventYouAreSelected(false); // refresh
																			// auf
																			// das
																			// gesamte
																			// 1:n
																			// panel
			} else if (e.getSource() == panelAngebottextBottomD) {
				panelAngebottextSP.eventYouAreSelected(false); // refresh auf
																// das gesamte
																// 1:n panel
			}
		}

		else if (eI.getID() == ItemChangedEvent.ACTION_NEW) {
			if (eI.getSource() == panelAngebotartTopQP1) {
				if (panelAngebotartTopQP1.getSelectedId() == null) {
					getInternalFrame().enableAllPanelsExcept(true);
				}
				panelAngebotartBottomD1.eventActionNew(eI, true, false);
				panelAngebotartBottomD1.eventYouAreSelected(false);
				setSelectedComponent(panelAngebotartSP1);
			} else if (eI.getSource() == panelAngebotpositionartTopQP1) {
				if (panelAngebotpositionartTopQP1.getSelectedId() == null) {
					getInternalFrame().enableAllPanelsExcept(true);
				}
				panelAngebotpositionartBottomD1.eventActionNew(eI, true, false);
				panelAngebotpositionartBottomD1.eventYouAreSelected(false);
				setSelectedComponent(panelAngebotpositionartSP1);
			} else if (eI.getSource() == panelAngeboterledigungsgrundTopQP1) {
				if (panelAngeboterledigungsgrundTopQP1.getSelectedId() == null) {
					getInternalFrame().enableAllPanelsExcept(true);
				}
				panelAngeboterledigungsgrundBottomD1.eventActionNew(eI, true,
						false);
				panelAngeboterledigungsgrundBottomD1.eventYouAreSelected(false);
				setSelectedComponent(panelAngeboterledigungsgrundSP1);
			} else if (eI.getSource() == panelAngebottextTopQP) {
				if (panelAngebottextTopQP.getSelectedId() == null) {
					getInternalFrame().enableAllPanelsExcept(true);
				}
				panelAngebottextBottomD.eventActionNew(eI, true, false);
				panelAngebottextBottomD.eventYouAreSelected(false);
				setSelectedComponent(panelAngebottextSP);
			}
		}
	}

	public void lPEventObjectChanged(ChangeEvent e) throws Throwable {
		super.lPEventObjectChanged(e);

		int selectedIndex = this.getSelectedIndex();

		switch (selectedIndex) {
		case IDX_PANEL_ANGEBOTART:
			refreshPanelAngebotart();
			panelAngebotartTopQP1.eventYouAreSelected(false);
			setTitle(LPMain.getInstance()
					.getTextRespectUISPr("angb.angebotart"));

			panelAngebotartTopQP1.updateButtons(panelAngebotartBottomD1
					.getLockedstateDetailMainKey());

			// wenn es fuer das tabbed pane noch keinen Eintrag gibt,
			// die restlichen oberen Laschen deaktivieren, ausser ...
			if (panelAngebotartTopQP1.getSelectedId() == null) {
				getInternalFrame().enableAllOberePanelsExceptMe(this,
						IDX_PANEL_ANGEBOTART, false);
			}
			break;

		case IDX_PANEL_ANGEBOTPOSITIONART:
			refreshPanelAngebotpositionart();
			panelAngebotpositionartTopQP1.eventYouAreSelected(false);
			setTitle(LPMain.getInstance().getTextRespectUISPr(
					"angb.angebotpositionart"));

			panelAngebotpositionartTopQP1
					.updateButtons(panelAngebotpositionartBottomD1
							.getLockedstateDetailMainKey());

			// wenn es fuer das tabbed pane noch keinen Eintrag gibt,
			// die restlichen oberen Laschen deaktivieren, ausser ...
			if (panelAngebotpositionartTopQP1.getSelectedId() == null) {
				getInternalFrame().enableAllOberePanelsExceptMe(this,
						IDX_PANEL_ANGEBOTPOSITIONART, false);
			}
			break;

		case IDX_PANEL_ANGEBOTERLEDIGUNGSGRUND:
			refreshPanelAngeboterledigungsgrund();
			panelAngeboterledigungsgrundTopQP1.eventYouAreSelected(false);
			setTitle(LPMain.getInstance().getTextRespectUISPr(
					"angb.angeboterledigungsgrund"));

			panelAngeboterledigungsgrundTopQP1
					.updateButtons(panelAngeboterledigungsgrundBottomD1
							.getLockedstateDetailMainKey());

			// wenn es fuer das tabbed pane noch keinen Eintrag gibt,
			// die restlichen oberen Laschen deaktivieren, ausser ...
			if (panelAngeboterledigungsgrundTopQP1.getSelectedId() == null) {
				getInternalFrame().enableAllOberePanelsExceptMe(this,
						IDX_PANEL_ANGEBOTERLEDIGUNGSGRUND, false);
			}
			break;

		case IDX_PANEL_ANGEBOTTEXT:
			refreshPanelAngebottext();

			panelAngebottextTopQP.setDefaultFilter(AngebotFilterFactory
					.getInstance().createFKAngebottext());
			panelAngebottextTopQP.eventYouAreSelected(false);
			setTitle(LPMain.getInstance().getTextRespectUISPr(
					"angb.angebotstext"));

			panelAngebottextTopQP.updateButtons(panelAngebottextBottomD
					.getLockedstateDetailMainKey());

			// wenn es fuer das tabbed pane noch keinen Eintrag gibt,
			// die restlichen oberen Laschen deaktivieren, ausser ...
			if (panelAngebottextTopQP.getSelectedId() == null) {
				getInternalFrame().enableAllOberePanelsExceptMe(this,
						IDX_PANEL_ANGEBOTTEXT, false);
			}
			break;
		}
	}

	protected void lPActionEvent(java.awt.event.ActionEvent e) {
	}

	private void refreshPanelAngebotart() throws Throwable {
		String[] aWhichStandardButtonIUse = {};

		if (panelAngebotartSP1 == null) {
			panelAngebotartTopQP1 = new PanelQuery(null, null,
					QueryParameters.UC_ID_ANGEBOTART, aWhichStandardButtonIUse,
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("angb.angebotart"), true);

			panelAngebotartBottomD1 = new PanelStammdatenCRUD(
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("angb.angebotart"), null,
					HelperClient.SCRUD_ANGEBOTART_FILE,
					getInternalFrameAngebot(), HelperClient.LOCKME_ANGEBOTART);

			panelAngebotartSP1 = new PanelSplit(getInternalFrame(),
					panelAngebotartBottomD1, panelAngebotartTopQP1, 200);

			setComponentAt(IDX_PANEL_ANGEBOTART, panelAngebotartSP1);
		}
	}

	private void refreshPanelAngebotpositionart() throws Throwable {
		String[] aWhichStandardButtonIUse = {};

		if (panelAngebotpositionartSP1 == null) {
			panelAngebotpositionartTopQP1 = new PanelQuery(null, null,
					QueryParameters.UC_ID_ANGEBOTPOSITIONART,
					aWhichStandardButtonIUse, getInternalFrame(), LPMain
							.getInstance().getTextRespectUISPr(
									"angb.angebotpositionart"), true);

			panelAngebotpositionartBottomD1 = new PanelStammdatenCRUD(
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("angb.angebotpositionart"),
					null, HelperClient.SCRUD_ANGEBOTPOSITIONART_FILE,
					getInternalFrameAngebot(),
					HelperClient.LOCKME_ANGEBOTPOSITIONART);

			panelAngebotpositionartSP1 = new PanelSplit(getInternalFrame(),
					panelAngebotpositionartBottomD1,
					panelAngebotpositionartTopQP1, 200);

			setComponentAt(IDX_PANEL_ANGEBOTPOSITIONART,
					panelAngebotpositionartSP1);

			// liste soll sofort angezeigt werden
			panelAngebotpositionartTopQP1.eventYouAreSelected(true);
		}
	}

	private void refreshPanelAngeboterledigungsgrund() throws Throwable {
		String[] aWhichStandardButtonIUse = { PanelBasis.ACTION_NEW };

		if (panelAngeboterledigungsgrundSP1 == null) {
			panelAngeboterledigungsgrundTopQP1 = new PanelQuery(null,
					AngebotFilterFactory.getInstance()
							.createFKAngeboterledigungsgrund(),
					QueryParameters.UC_ID_ANGEBOTERLEDIGUNGSGRUND,
					aWhichStandardButtonIUse, getInternalFrame(), LPMain
							.getInstance().getTextRespectUISPr(
									"angb.angeboterledigungsgrund"), true);

			panelAngeboterledigungsgrundBottomD1 = new PanelStammdatenCRUD(
					getInternalFrame(),
					LPMain.getInstance().getTextRespectUISPr(
							"angb.angeboterledigungsgrund"), null,
					HelperClient.SCRUD_ANGEBOTERLEDIGUNGSGRUND_FILE,
					getInternalFrameAngebot(),
					HelperClient.LOCKME_ANGEBOTERLEDIGUNGSGRUND);

			panelAngeboterledigungsgrundSP1 = new PanelSplit(
					getInternalFrame(), panelAngeboterledigungsgrundBottomD1,
					panelAngeboterledigungsgrundTopQP1, 200);

			setComponentAt(IDX_PANEL_ANGEBOTERLEDIGUNGSGRUND,
					panelAngeboterledigungsgrundSP1);
		}
	}

	private void refreshPanelAngebottext() throws Throwable {
		if (panelAngebottextSP == null) {

			// der Kopftext muss in der Konzerndatensprache hinterlegt sein oder
			// werden
			DelegateFactory
					.getInstance()
					.getAngebotServiceDelegate()
					.angebottextFindByMandantCNrLocaleCNrCNr(
							LPMain.getInstance().getTheClient()
									.getLocUiAsString(),
							MediaFac.MEDIAART_KOPFTEXT);
			// der Fusstext muss in der Konzerndatensprache hinterlegt sein oder
			// werden
			DelegateFactory
					.getInstance()
					.getAngebotServiceDelegate()
					.angebottextFindByMandantCNrLocaleCNrCNr(
							LPMain.getInstance().getTheClient()
									.getLocUiAsString(),
							MediaFac.MEDIAART_FUSSTEXT);

			String[] aWhichStandardButtonIUse = null;

			panelAngebottextTopQP = new PanelQuery(null, null,
					QueryParameters.UC_ID_ANGEBOTTEXT,
					aWhichStandardButtonIUse, getInternalFrame(), LPMain
							.getInstance().getTextRespectUISPr(
									"angb.angebotstext"), true);

			panelAngebottextBottomD = new PanelStammdatenCRUD(
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("angb.angebotstext"), null,
					HelperClient.SCRUD_ANGEBOTTEXT_FILE,
					getInternalFrameAngebot(), HelperClient.LOCKME_ANGEBOTEXT);

			panelAngebottextSP = new PanelSplit(getInternalFrame(),
					panelAngebottextBottomD, panelAngebottextTopQP,
					400 - ((PanelStammdatenCRUD) panelAngebottextBottomD)
							.getAnzahlControls() * 30);

			setComponentAt(IDX_PANEL_ANGEBOTTEXT, panelAngebottextSP);
		}
	}

	private void setTitle(String cTitleI) {
		getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_OHRWASCHLOBEN,
				cTitleI);
		getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_AS_I_LIKE, "");
	}

	protected javax.swing.JMenuBar getJMenuBar() throws Throwable {
		return new WrapperMenuBar(this);
	}
}
