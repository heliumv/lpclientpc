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
package com.lp.client.lieferschein;

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
 * <p><I>Diese Klasse kuemmert sich um die Wartung der Grunddaten des Lieferscheins.</I> </p>
 *
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 *
 * <p>Erstellungsdatum <I>20.07.05</I></p>
 *
 * <p> </p>
 *
 * @author Uli Walch
 * @version $Revision: 1.5 $
 */
public class TabbedPaneLieferscheinGrunddaten extends TabbedPane {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private PanelQuery panelLieferscheintextTopQP;
	private PanelSplit panelLieferscheintextSP;
	private PanelStammdatenCRUD panelLieferscheintextBottomD;

	private PanelQuery panelLieferscheinartTopQP;
	private PanelSplit panelLieferscheinartSP;
	private PanelStammdatenCRUD panelLieferscheinartBottomD;

	private PanelQuery panelLieferscheinpositionartTopQP;
	private PanelSplit panelLieferscheinpositionartSP;
	private PanelStammdatenCRUD panelLieferscheinpositionartBottomD;

	private PanelQuery panelBegruendungTopQP;
	private PanelSplit panelBegruendungSP;
	private PanelBegruendung panelBegruendungBottomD;

	private static final int IDX_PANEL_LIEFERSCHEINTEXT = 0;
	private static final int IDX_PANEL_LIEFERSCHEINART = 1;
	private static final int IDX_PANEL_LIEFERSCHEINPOSITIONART = 2;
	private static final int IDX_PANEL_BEGRUENDUNG = 3;

	public TabbedPaneLieferscheinGrunddaten(InternalFrame internalFrameI)
			throws Throwable {
		super(internalFrameI, LPMain.getTextRespectUISPr("lp.grunddaten"));
		jbInit();
		initComponents();
	}

	public InternalFrameLieferschein getInternalFrameLieferschein() {
		return (InternalFrameLieferschein) getInternalFrame();
	}

	private void jbInit() throws Throwable {
		insertTab(
				LPMain.getInstance().getTextRespectUISPr("lp.lieferscheintext"),
				null,
				null,
				LPMain.getInstance().getTextRespectUISPr("lp.lieferscheintext"),
				IDX_PANEL_LIEFERSCHEINTEXT);

		insertTab(
				LPMain.getInstance().getTextRespectUISPr(
						"label.lieferscheinart"),
				null,
				null,
				LPMain.getInstance().getTextRespectUISPr(
						"label.lieferscheinart"), IDX_PANEL_LIEFERSCHEINART);

		insertTab(
				LPMain.getInstance().getTextRespectUISPr(
						"ls.lieferscheinpositionart"),
				null,
				null,
				LPMain.getInstance().getTextRespectUISPr(
						"ls.lieferscheinpositionart"),
				IDX_PANEL_LIEFERSCHEINPOSITIONART);

		insertTab(LPMain.getInstance().getTextRespectUISPr("ls.begruendung"),
				null, null,
				LPMain.getInstance().getTextRespectUISPr("ls.begruendung"),
				IDX_PANEL_BEGRUENDUNG);

		// default
		refreshPanelLieferscheintext();

		setSelectedComponent(panelLieferscheintextSP);

		panelLieferscheintextTopQP.eventYouAreSelected(false);

		getInternalFrameLieferschein().getLieferscheintextDto().setIId(
				(Integer) panelLieferscheintextTopQP.getSelectedId());

		addChangeListener(this);
		getInternalFrame().addItemChangedListener(this);
	}

	public void lPEventItemChanged(ItemChangedEvent eI) throws Throwable {

		ItemChangedEvent e = (ItemChangedEvent) eI;

		if (e.getID() == ItemChangedEvent.ITEM_CHANGED) {
			if (eI.getSource() == panelLieferscheintextTopQP) {
				Integer iId = (Integer) panelLieferscheintextTopQP
						.getSelectedId();
				getInternalFrame().setKeyWasForLockMe(iId + "");
				panelLieferscheintextBottomD.setKeyWhenDetailPanel(iId);
				panelLieferscheintextBottomD.eventYouAreSelected(false);

				panelLieferscheintextTopQP
						.updateButtons(panelLieferscheintextBottomD
								.getLockedstateDetailMainKey());
			} else if (eI.getSource() == panelLieferscheinartTopQP) {
				String cNr = (String) panelLieferscheinartTopQP.getSelectedId();
				getInternalFrame().setKeyWasForLockMe(cNr);
				panelLieferscheinartBottomD.setKeyWhenDetailPanel(cNr);
				panelLieferscheinartBottomD.eventYouAreSelected(false);

				panelLieferscheinartTopQP
						.updateButtons(panelLieferscheinartBottomD
								.getLockedstateDetailMainKey());
			} else if (eI.getSource() == panelLieferscheinpositionartTopQP) {
				String cNr = (String) panelLieferscheinpositionartTopQP
						.getSelectedId();
				getInternalFrame().setKeyWasForLockMe(cNr);
				panelLieferscheinpositionartBottomD.setKeyWhenDetailPanel(cNr);
				panelLieferscheinpositionartBottomD.eventYouAreSelected(false);

				panelLieferscheinpositionartTopQP
						.updateButtons(panelLieferscheinpositionartBottomD
								.getLockedstateDetailMainKey());
			} else if (eI.getSource() == panelBegruendungTopQP) {
				Integer iId = (Integer) panelBegruendungTopQP.getSelectedId();
				getInternalFrame().setKeyWasForLockMe(iId + "");
				panelBegruendungBottomD.setKeyWhenDetailPanel(iId);
				panelBegruendungBottomD.eventYouAreSelected(false);

				panelBegruendungTopQP.updateButtons(panelBegruendungBottomD
						.getLockedstateDetailMainKey());
			}

		} else if (eI.getID() == ItemChangedEvent.ACTION_UPDATE) {
			// hier kommt man nach upd im D bei einem 1:n hin.
			if (eI.getSource() == panelLieferscheintextBottomD) {
				// im QP die Buttons in den Zustand neu setzen.
				panelLieferscheintextTopQP.updateButtons(new LockStateValue(
						PanelBasis.LOCK_FOR_NEW));
				;
			} else if (eI.getSource() == panelLieferscheinartBottomD) {
				// im QP die Buttons in den Zustand neu setzen.
				panelLieferscheinartTopQP.updateButtons(new LockStateValue(
						PanelBasis.LOCK_FOR_NEW));
				;
			} else if (eI.getSource() == panelLieferscheinpositionartBottomD) {
				// im QP die Buttons in den Zustand neu setzen.
				panelLieferscheinpositionartTopQP
						.updateButtons(new LockStateValue(
								PanelBasis.LOCK_FOR_NEW));
				;
			} else if (eI.getSource() == panelBegruendungBottomD) {
				// im QP die Buttons in den Zustand neu setzen.
				panelBegruendungTopQP.updateButtons(new LockStateValue(
						PanelBasis.LOCK_FOR_NEW));
				;
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_DISCARD) {
			if (e.getSource() == panelLieferscheintextBottomD) {
				panelLieferscheintextSP.eventYouAreSelected(false);
			} else if (e.getSource() == panelLieferscheinartBottomD) {
				panelLieferscheinartSP.eventYouAreSelected(false);
			} else if (e.getSource() == panelLieferscheinpositionartBottomD) {
				panelLieferscheinpositionartSP.eventYouAreSelected(false);
			} else if (e.getSource() == panelBegruendungBottomD) {
				panelBegruendungSP.eventYouAreSelected(false);
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_SAVE) {
			if (e.getSource() == panelLieferscheintextBottomD) {
				Object oKey = panelLieferscheintextBottomD
						.getKeyWhenDetailPanel();
				panelLieferscheintextTopQP.eventYouAreSelected(false);
				panelLieferscheintextTopQP.setSelectedId(oKey);
				panelLieferscheintextSP.eventYouAreSelected(false);
			} else if (e.getSource() == panelLieferscheinartBottomD) {
				Object oKey = panelLieferscheinartBottomD
						.getKeyWhenDetailPanel();
				panelLieferscheinartTopQP.eventYouAreSelected(false);
				panelLieferscheinartTopQP.setSelectedId(oKey);
				panelLieferscheinartSP.eventYouAreSelected(false);
			} else if (e.getSource() == panelLieferscheinpositionartBottomD) {
				Object oKey = panelLieferscheinpositionartBottomD
						.getKeyWhenDetailPanel();
				panelLieferscheinpositionartTopQP.eventYouAreSelected(false);
				panelLieferscheinpositionartTopQP.setSelectedId(oKey);
				panelLieferscheinpositionartSP.eventYouAreSelected(false);
			} else if (e.getSource() == panelBegruendungBottomD) {
				Object oKey = panelBegruendungBottomD.getKeyWhenDetailPanel();
				panelBegruendungTopQP.eventYouAreSelected(false);
				panelBegruendungTopQP.setSelectedId(oKey);
				panelBegruendungSP.eventYouAreSelected(false);
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_GOTO_MY_DEFAULT_QP) {
			if (e.getSource() == panelLieferscheintextBottomD) {
				panelLieferscheintextSP.eventYouAreSelected(false); // refresh
																	// auf das
																	// gesamte
																	// 1:n panel
			} else if (e.getSource() == panelLieferscheinartBottomD) {
				panelLieferscheinartSP.eventYouAreSelected(false); // refresh
																	// auf das
																	// gesamte
																	// 1:n panel
			} else if (e.getSource() == panelLieferscheinpositionartBottomD) {
				panelLieferscheinpositionartSP.eventYouAreSelected(false); // refresh
																			// auf
																			// das
																			// gesamte
																			// 1:n
																			// panel
			} else if (e.getSource() == panelBegruendungBottomD) {
				panelBegruendungSP.eventYouAreSelected(false); // refresh
				// auf
				// das
				// gesamte
				// 1:n
				// panel
			}
		} else if (eI.getID() == ItemChangedEvent.ACTION_NEW) {
			if (eI.getSource() == panelLieferscheintextTopQP) {
				if (panelLieferscheintextTopQP.getSelectedId() == null) {
					getInternalFrame().enableAllPanelsExcept(true);
				}
				panelLieferscheintextBottomD.eventActionNew(eI, true, false);
				panelLieferscheintextBottomD.eventYouAreSelected(false);
				setSelectedComponent(panelLieferscheintextSP);
			} else if (eI.getSource() == panelLieferscheinartTopQP) {
				if (panelLieferscheinartTopQP.getSelectedId() == null) {
					getInternalFrame().enableAllPanelsExcept(true);
				}
				panelLieferscheinartBottomD.eventActionNew(eI, true, false);
				panelLieferscheinartBottomD.eventYouAreSelected(false);
				setSelectedComponent(panelLieferscheinartSP);
			} else if (eI.getSource() == panelLieferscheinpositionartTopQP) {
				if (panelLieferscheinpositionartTopQP.getSelectedId() == null) {
					getInternalFrame().enableAllPanelsExcept(true);
				}
				panelLieferscheinpositionartBottomD.eventActionNew(eI, true,
						false);
				panelLieferscheinpositionartBottomD.eventYouAreSelected(false);
				setSelectedComponent(panelLieferscheinpositionartSP);
			} else if (eI.getSource() == panelBegruendungTopQP) {
				if (panelBegruendungTopQP.getSelectedId() == null) {
					getInternalFrame().enableAllPanelsExcept(true);
				}
				panelBegruendungBottomD.eventActionNew(eI, true, false);
				panelBegruendungBottomD.eventYouAreSelected(false);
				setSelectedComponent(panelBegruendungSP);
			}
		}
	}

	public void lPEventObjectChanged(ChangeEvent e) throws Throwable {
		super.lPEventObjectChanged(e);

		int selectedIndex = this.getSelectedIndex();

		switch (selectedIndex) {
		case IDX_PANEL_LIEFERSCHEINTEXT:
			refreshPanelLieferscheintext();

			panelLieferscheintextTopQP
					.setDefaultFilter(LieferscheinFilterFactory.getInstance()
							.createFKLieferscheintext());
			panelLieferscheintextTopQP.eventYouAreSelected(false);
			setTitle(LPMain.getInstance().getTextRespectUISPr(
					"lp.lieferscheintext"));

			panelLieferscheintextTopQP.updateButtons();

			// wenn es fuer das tabbed pane noch keinen Eintrag gibt,
			// die restlichen oberen Laschen deaktivieren, ausser ...
			if (panelLieferscheintextTopQP.getSelectedId() == null) {
				getInternalFrame().enableAllOberePanelsExceptMe(this,
						IDX_PANEL_LIEFERSCHEINTEXT, false);
			}
			break;

		case IDX_PANEL_LIEFERSCHEINART: {
			refreshPanelLieferscheinart();

			panelLieferscheinartTopQP.eventYouAreSelected(false);
			setTitle(LPMain.getInstance().getTextRespectUISPr(
					"label.lieferscheinart"));

			panelLieferscheinartTopQP.updateButtons(panelLieferscheinartBottomD
					.getLockedstateDetailMainKey());

			// wenn es fuer das tabbed pane noch keinen Eintrag gibt,
			// die restlichen oberen Laschen deaktivieren, ausser ...
			if (panelLieferscheinartTopQP.getSelectedId() == null) {
				getInternalFrame().enableAllOberePanelsExceptMe(this,
						IDX_PANEL_LIEFERSCHEINART, false);
			}
			break;
		}

		case IDX_PANEL_LIEFERSCHEINPOSITIONART: {
			refreshPanelLieferscheinpositionart();

			panelLieferscheinpositionartTopQP.eventYouAreSelected(false);
			setTitle(LPMain.getInstance().getTextRespectUISPr(
					"ls.lieferscheinpositionart"));

			panelLieferscheinpositionartTopQP
					.updateButtons(panelLieferscheinpositionartBottomD
							.getLockedstateDetailMainKey());

			// wenn es fuer das tabbed pane noch keinen Eintrag gibt,
			// die restlichen oberen Laschen deaktivieren, ausser ...
			if (panelLieferscheinpositionartTopQP.getSelectedId() == null) {
				getInternalFrame().enableAllOberePanelsExceptMe(this,
						IDX_PANEL_LIEFERSCHEINPOSITIONART, false);
			}
			break;
		}

		case IDX_PANEL_BEGRUENDUNG: {
			refreshPanelBegruendung();

			panelBegruendungTopQP.eventYouAreSelected(false);
			setTitle(LPMain.getInstance().getTextRespectUISPr("ls.begruendung"));

			panelBegruendungTopQP.updateButtons(panelBegruendungBottomD
					.getLockedstateDetailMainKey());

		
			break;
		}

		}
	}

	protected void lPActionEvent(java.awt.event.ActionEvent e) {
	}

	private void refreshPanelLieferscheintext() throws Throwable {
		if (panelLieferscheintextSP == null) {
			// der Kopftext muss in der Konzerndatensprache hinterlegt sein oder
			// werden
			DelegateFactory
					.getInstance()
					.getLieferscheinServiceDelegate()
					.lieferscheintextFindByMandantLocaleCNr(
							LPMain.getInstance().getTheClient()
									.getLocUiAsString(),
							MediaFac.MEDIAART_KOPFTEXT);
			// der Fusstext muss in der Konzerndatensprache hinterlegt sein oder
			// werden
			DelegateFactory
					.getInstance()
					.getLieferscheinServiceDelegate()
					.lieferscheintextFindByMandantLocaleCNr(
							LPMain.getInstance().getTheClient()
									.getLocUiAsString(),
							MediaFac.MEDIAART_FUSSTEXT);
			String[] aWhichStandardButtonIUse = null;

			panelLieferscheintextTopQP = new PanelQuery(null, null,
					QueryParameters.UC_ID_LIEFERSCHEINTEXT,
					aWhichStandardButtonIUse, getInternalFrame(), LPMain
							.getInstance().getTextRespectUISPr(
									"lp.lieferscheintext"), true);

			panelLieferscheintextBottomD = new PanelStammdatenCRUD(
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("lp.lieferscheintext"), null,
					HelperClient.SCRUD_LIEFERSCHEINTEXT_FILE,
					getInternalFrameLieferschein(),
					HelperClient.LOCKME_LIEFERSCHEINTEXT);

			panelLieferscheintextSP = new PanelSplit(getInternalFrame(),
					panelLieferscheintextBottomD, panelLieferscheintextTopQP,
					400 - ((PanelStammdatenCRUD) panelLieferscheintextBottomD)
							.getAnzahlControls() * 30);

			setComponentAt(IDX_PANEL_LIEFERSCHEINTEXT, panelLieferscheintextSP);
		}
	}

	private void refreshPanelLieferscheinart() throws Throwable {
		if (panelLieferscheinartSP == null) {
			String[] aWhichStandardButtonIUse = null;

			panelLieferscheinartTopQP = new PanelQuery(null, null,
					QueryParameters.UC_ID_LIEFERSCHEINART,
					aWhichStandardButtonIUse, getInternalFrame(), LPMain
							.getInstance().getTextRespectUISPr(
									"label.lieferscheinart"), true);

			panelLieferscheinartBottomD = new PanelStammdatenCRUD(
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("label.lieferscheinart"),
					null, HelperClient.SCRUD_LIEFERSCHEINART_FILE,
					getInternalFrameLieferschein(),
					HelperClient.LOCKME_LIEFERSCHEINART);

			panelLieferscheinartSP = new PanelSplit(getInternalFrame(),
					panelLieferscheinartBottomD, panelLieferscheinartTopQP,
					400 - ((PanelStammdatenCRUD) panelLieferscheinartBottomD)
							.getAnzahlControls() * 30);

			setComponentAt(IDX_PANEL_LIEFERSCHEINART, panelLieferscheinartSP);
		}
	}

	private void refreshPanelLieferscheinpositionart() throws Throwable {
		if (panelLieferscheinpositionartSP == null) {
			String[] aWhichStandardButtonIUse = null;

			panelLieferscheinpositionartTopQP = new PanelQuery(null, null,
					QueryParameters.UC_ID_LIEFERSCHEINPOSITIONART,
					aWhichStandardButtonIUse, getInternalFrame(), LPMain
							.getInstance().getTextRespectUISPr(
									"ls.lieferscheinpositionart"), true);

			panelLieferscheinpositionartBottomD = new PanelStammdatenCRUD(
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("ls.lieferscheinpositionart"),
					null, HelperClient.SCRUD_LIEFERSCHEINPOSITIONART_FILE,
					getInternalFrameLieferschein(),
					HelperClient.LOCKME_LIEFERSCHEINPOSITIONART);

			panelLieferscheinpositionartSP = new PanelSplit(
					getInternalFrame(),
					panelLieferscheinpositionartBottomD,
					panelLieferscheinpositionartTopQP,
					400 - ((PanelStammdatenCRUD) panelLieferscheinpositionartBottomD)
							.getAnzahlControls() * 30);

			setComponentAt(IDX_PANEL_LIEFERSCHEINPOSITIONART,
					panelLieferscheinpositionartSP);
		}
	}

	private void refreshPanelBegruendung() throws Throwable {
		if (panelBegruendungSP == null) {
			String[] aWhichStandardButtonIUse = {
			          PanelBasis.ACTION_NEW};

			panelBegruendungTopQP = new PanelQuery(null, null,
					QueryParameters.UC_ID_BEGRUENDUNG,
					aWhichStandardButtonIUse, getInternalFrame(), LPMain
							.getInstance()
							.getTextRespectUISPr("ls.begruendung"), true);

			panelBegruendungBottomD = new PanelBegruendung(getInternalFrame(),
					LPMain.getInstance().getTextRespectUISPr("ls.begruendung"),
					null);

			panelBegruendungSP = new PanelSplit(getInternalFrame(),
					panelBegruendungBottomD, panelBegruendungTopQP, 350);

			setComponentAt(IDX_PANEL_BEGRUENDUNG, panelBegruendungSP);
		}
	}

	private void setTitle(String cTitleI) {
		getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_OHRWASCHLOBEN,
				cTitleI);
		getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_AS_I_LIKE, "");
	}

	public javax.swing.JMenuBar getJMenuBar() throws Throwable {
		return new WrapperMenuBar(this);
	}
}
