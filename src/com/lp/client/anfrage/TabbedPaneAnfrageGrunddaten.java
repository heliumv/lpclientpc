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
package com.lp.client.anfrage;

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
import com.lp.client.system.SystemFilterFactory;
import com.lp.server.anfrage.service.AnfrageServiceFac;
import com.lp.server.angebot.service.AngebotServiceFac;
import com.lp.server.system.service.MandantFac;
import com.lp.server.system.service.MediaFac;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;

@SuppressWarnings("static-access")
/*
 * <p><I>Diese Klasse kuemmert sich um die Wartung der Grunddaten der
 * Anfrage.</I> </p>
 * 
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 * 
 * <p>Erstellungsdatum <I>13.07.05</I></p>
 * 
 * <p> </p>
 * 
 * @author Uli Walch
 * 
 * @version $Revision: 1.6 $
 */
public class TabbedPaneAnfrageGrunddaten extends TabbedPane {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private PanelQuery panelQueryAnfragetext;
	private PanelSplit panelSplitAnfragetext;
	private PanelStammdatenCRUD panelBottomAnfragetext;

	private PanelQuery panelQueryAnfragepositionart;
	private PanelSplit panelSplitAnfragepositionart;
	private PanelStammdatenCRUD panelBottomAnfragepositionart;

	private PanelQuery panelAnfrageartTopQP1 = null;
	private PanelSplit panelAnfrageartSP1 = null;
	private PanelBasis panelAnfrageartBottomD1 = null;

	private PanelQuery panelErledigungsgrundTopQP1 = null;
	private PanelSplit panelErledigungsgrundSP1 = null;
	private PanelBasis panelErledigungsgrundBottomD1 = null;

	private PanelQuery panelZertifikatartTopQP1 = null;
	private PanelSplit panelZertifikatartSP1 = null;
	private PanelBasis panelZertifikatartBottomD1 = null;

	private static final int IDX_PANEL_ANFRAGETEXT = 0;
	private static final int IDX_PANEL_ANFRAGEPOSITIONART = 1;
	private static final int IDX_PANEL_ANFRAGEART = 2;
	private static final int IDX_PANEL_ERLEDIGUNGSGRUND = 3;
	private static final int IDX_PANEL_ZERTIFIKATART = 4;

	public TabbedPaneAnfrageGrunddaten(InternalFrame internalFrameI)
			throws Throwable {
		super(internalFrameI, LPMain.getTextRespectUISPr("lp.grunddaten"));
		jbInit();
		initComponents();
	}

	public InternalFrameAnfrage getInternalFrameAnfrage() {
		return (InternalFrameAnfrage) getInternalFrame();
	}

	private void jbInit() throws Throwable {
		insertTab(LPMain.getInstance().getTextRespectUISPr("lp.anfragetext"),
				null, null,
				LPMain.getInstance().getTextRespectUISPr("lp.anfragetext"),
				IDX_PANEL_ANFRAGETEXT);

		insertTab(
				LPMain.getInstance().getTextRespectUISPr(
						"anf.anfragepositionart"),
				null,
				null,
				LPMain.getInstance().getTextRespectUISPr(
						"anf.anfragepositionart"), IDX_PANEL_ANFRAGEPOSITIONART);

		insertTab(LPMain.getInstance().getTextRespectUISPr("anf.anfrageart"),
				null, null,
				LPMain.getInstance().getTextRespectUISPr("anf.anfrageart"),
				IDX_PANEL_ANFRAGEART);

		insertTab(
				LPMain.getInstance()
						.getTextRespectUISPr("anf.erledigungsgrund"), null,
				null,
				LPMain.getInstance()
						.getTextRespectUISPr("anf.erledigungsgrund"),
				IDX_PANEL_ERLEDIGUNGSGRUND);

		if (LPMain
				.getInstance()
				.getDesktop()
				.darfAnwenderAufZusatzfunktionZugreifen(
						MandantFac.ZUSATZFUNKTION_ZERTIFIKATART)) {

			insertTab(
					LPMain.getInstance().getTextRespectUISPr(
							"anf.zertifikatart"),
					null,
					null,
					LPMain.getInstance().getTextRespectUISPr(
							"anf.zertifikatart"), IDX_PANEL_ZERTIFIKATART);
		}

		// default
		refreshPanelAnfragetext();

		setSelectedComponent(panelSplitAnfragetext);

		panelQueryAnfragetext.eventYouAreSelected(false);

		getInternalFrameAnfrage().getAnfragetextDto().setIId(
				(Integer) panelQueryAnfragetext.getSelectedId());

		addChangeListener(this);
		getInternalFrame().addItemChangedListener(this);
	}

	public void lPEventItemChanged(ItemChangedEvent eI) throws Throwable {

		ItemChangedEvent e = (ItemChangedEvent) eI;

		if (e.getID() == ItemChangedEvent.ITEM_CHANGED) {
			if (eI.getSource() == panelQueryAnfragetext) {
				Integer iId = (Integer) panelQueryAnfragetext.getSelectedId();
				getInternalFrame().setKeyWasForLockMe(iId + "");
				panelBottomAnfragetext.setKeyWhenDetailPanel(iId);
				panelBottomAnfragetext.eventYouAreSelected(false);

				panelQueryAnfragetext.updateButtons(panelBottomAnfragetext
						.getLockedstateDetailMainKey());
			} else if (eI.getSource() == panelQueryAnfragepositionart) {
				String positionsartCNr = (String) panelQueryAnfragepositionart
						.getSelectedId();
				getInternalFrame().setKeyWasForLockMe(positionsartCNr + "");
				panelBottomAnfragepositionart
						.setKeyWhenDetailPanel(positionsartCNr);
				panelBottomAnfragepositionart.eventYouAreSelected(false);

				panelQueryAnfragepositionart
						.updateButtons(panelBottomAnfragepositionart
								.getLockedstateDetailMainKey());
			} else if (eI.getSource() == panelAnfrageartTopQP1) {
				String anfrageartCNr = (String) panelAnfrageartTopQP1
						.getSelectedId();
				getInternalFrame().setKeyWasForLockMe(anfrageartCNr + "");
				panelAnfrageartBottomD1.setKeyWhenDetailPanel(anfrageartCNr);
				panelAnfrageartBottomD1.eventYouAreSelected(false);

				panelAnfrageartTopQP1.updateButtons(panelAnfrageartBottomD1
						.getLockedstateDetailMainKey());
			} else if (eI.getSource() == panelErledigungsgrundTopQP1) {
				Integer erledigungsgrundIId = (Integer) panelErledigungsgrundTopQP1
						.getSelectedId();
				getInternalFrame().setKeyWasForLockMe(erledigungsgrundIId + "");
				panelErledigungsgrundBottomD1
						.setKeyWhenDetailPanel(erledigungsgrundIId);
				panelErledigungsgrundBottomD1.eventYouAreSelected(false);

				panelErledigungsgrundTopQP1
						.updateButtons(panelErledigungsgrundBottomD1
								.getLockedstateDetailMainKey());
			} else if (eI.getSource() == panelZertifikatartTopQP1) {
				Integer id = (Integer) panelZertifikatartTopQP1.getSelectedId();
				getInternalFrame().setKeyWasForLockMe(id + "");
				panelZertifikatartBottomD1.setKeyWhenDetailPanel(id);
				panelZertifikatartBottomD1.eventYouAreSelected(false);

				panelZertifikatartTopQP1
						.updateButtons(panelZertifikatartBottomD1
								.getLockedstateDetailMainKey());
			}
		} else if (eI.getID() == ItemChangedEvent.ACTION_UPDATE) {
			// hier kommt man nach upd im D bei einem 1:n hin.
			if (eI.getSource() == panelBottomAnfragetext) {
				// im QP die Buttons in den Zustand neu setzen.
				panelQueryAnfragetext.updateButtons(new LockStateValue(
						PanelBasis.LOCK_FOR_NEW));
			} else if (eI.getSource() == panelBottomAnfragepositionart) {
				// im QP die Buttons in den Zustand neu setzen.
				panelQueryAnfragepositionart.updateButtons(new LockStateValue(
						PanelBasis.LOCK_FOR_NEW));
			} else if (eI.getSource() == panelAnfrageartBottomD1) {
				// im QP die Buttons in den Zustand neu setzen.
				panelAnfrageartTopQP1.updateButtons(new LockStateValue(
						PanelBasis.LOCK_FOR_NEW));
			} else if (eI.getSource() == panelErledigungsgrundBottomD1) {
				// im QP die Buttons in den Zustand neu setzen.
				panelErledigungsgrundTopQP1.updateButtons(new LockStateValue(
						PanelBasis.LOCK_FOR_NEW));
			} else if (eI.getSource() == panelZertifikatartBottomD1) {
				// im QP die Buttons in den Zustand neu setzen.
				panelZertifikatartTopQP1.updateButtons(new LockStateValue(
						PanelBasis.LOCK_FOR_NEW));
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_DISCARD) {
			if (eI.getSource() == panelBottomAnfragetext) {
				panelSplitAnfragetext.eventYouAreSelected(false);
			} else if (eI.getSource() == panelBottomAnfragepositionart) {
				panelSplitAnfragepositionart.eventYouAreSelected(false);
			} else if (eI.getSource() == panelAnfrageartBottomD1) {
				panelAnfrageartSP1.eventYouAreSelected(false);
			} else if (eI.getSource() == panelErledigungsgrundBottomD1) {
				panelErledigungsgrundSP1.eventYouAreSelected(false);
			} else if (eI.getSource() == panelZertifikatartBottomD1) {
				panelZertifikatartSP1.eventYouAreSelected(false);
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_SAVE) {
			if (eI.getSource() == panelBottomAnfragetext) {
				Object oKey = panelBottomAnfragetext.getKeyWhenDetailPanel();
				panelQueryAnfragetext.eventYouAreSelected(false);
				panelQueryAnfragetext.setSelectedId(oKey);
				panelSplitAnfragetext.eventYouAreSelected(false);
			} else if (eI.getSource() == panelBottomAnfragepositionart) {
				Object oKey = panelBottomAnfragepositionart
						.getKeyWhenDetailPanel();
				panelQueryAnfragepositionart.eventYouAreSelected(false);
				panelQueryAnfragepositionart.setSelectedId(oKey);
				panelSplitAnfragepositionart.eventYouAreSelected(false);
			} else if (eI.getSource() == panelAnfrageartBottomD1) {
				Object oKey = panelAnfrageartBottomD1.getKeyWhenDetailPanel();
				panelAnfrageartTopQP1.eventYouAreSelected(false);
				panelAnfrageartTopQP1.setSelectedId(oKey);
				panelAnfrageartSP1.eventYouAreSelected(false);
			} else if (eI.getSource() == panelErledigungsgrundBottomD1) {
				Object oKey = panelErledigungsgrundBottomD1
						.getKeyWhenDetailPanel();
				panelErledigungsgrundTopQP1.eventYouAreSelected(false);
				panelErledigungsgrundTopQP1.setSelectedId(oKey);
				panelErledigungsgrundSP1.eventYouAreSelected(false);
			} else if (eI.getSource() == panelZertifikatartBottomD1) {
				Object oKey = panelZertifikatartBottomD1
						.getKeyWhenDetailPanel();
				panelZertifikatartTopQP1.eventYouAreSelected(false);
				panelZertifikatartTopQP1.setSelectedId(oKey);
				panelZertifikatartSP1.eventYouAreSelected(false);
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_GOTO_MY_DEFAULT_QP) {
			if (eI.getSource() == panelBottomAnfragetext) {
				panelSplitAnfragetext.eventYouAreSelected(false);
			} else if (eI.getSource() == panelBottomAnfragepositionart) {
				panelSplitAnfragepositionart.eventYouAreSelected(false);
			} else if (eI.getSource() == panelAnfrageartBottomD1) {
				panelAnfrageartSP1.eventYouAreSelected(false);
			} else if (eI.getSource() == panelErledigungsgrundBottomD1) {
				panelErledigungsgrundSP1.eventYouAreSelected(false);
			} else if (eI.getSource() == panelZertifikatartBottomD1) {
				panelZertifikatartSP1.eventYouAreSelected(false);
			}
		} else if (eI.getID() == ItemChangedEvent.ACTION_NEW) {
			if (eI.getSource() == panelQueryAnfragetext) {
				if (panelQueryAnfragetext.getSelectedId() == null) {
					getInternalFrame().enableAllPanelsExcept(true);
				}
				panelBottomAnfragetext.eventActionNew(eI, true, false);
				panelBottomAnfragetext.eventYouAreSelected(false);
				setSelectedComponent(panelSplitAnfragetext);
			} else if (eI.getSource() == panelQueryAnfragepositionart) {
				if (panelQueryAnfragepositionart.getSelectedId() == null) {
					getInternalFrame().enableAllPanelsExcept(true);
				}
				panelBottomAnfragepositionart.eventActionNew(eI, true, false);
				panelBottomAnfragepositionart.eventYouAreSelected(false);
				setSelectedComponent(panelSplitAnfragepositionart);
			} else if (eI.getSource() == panelAnfrageartTopQP1) {
				if (panelAnfrageartTopQP1.getSelectedId() == null) {
					getInternalFrame().enableAllPanelsExcept(true);
				}
				panelAnfrageartBottomD1.eventActionNew(eI, true, false);
				panelAnfrageartBottomD1.eventYouAreSelected(false);
				setSelectedComponent(panelAnfrageartSP1);
			} else if (eI.getSource() == panelErledigungsgrundTopQP1) {
				if (panelErledigungsgrundTopQP1.getSelectedId() == null) {
					getInternalFrame().enableAllPanelsExcept(true);
				}
				panelErledigungsgrundBottomD1.eventActionNew(eI, true, false);
				panelErledigungsgrundBottomD1.eventYouAreSelected(false);
				setSelectedComponent(panelErledigungsgrundSP1);
			} else if (eI.getSource() == panelZertifikatartTopQP1) {
				if (panelZertifikatartTopQP1.getSelectedId() == null) {
					getInternalFrame().enableAllPanelsExcept(true);
				}
				panelZertifikatartBottomD1.eventActionNew(eI, true, false);
				panelZertifikatartBottomD1.eventYouAreSelected(false);
				setSelectedComponent(panelZertifikatartSP1);
			}
		}
	}

	public void lPEventObjectChanged(ChangeEvent e) throws Throwable {
		super.lPEventObjectChanged(e);

		int selectedIndex = this.getSelectedIndex();

		switch (selectedIndex) {
		case IDX_PANEL_ANFRAGETEXT:
			refreshPanelAnfragetext();

			panelQueryAnfragetext.setDefaultFilter(AnfrageFilterFactory
					.getInstance().createFKAnfragetext());
			panelQueryAnfragetext.eventYouAreSelected(false);
			setTitle(LPMain.getInstance().getTextRespectUISPr("lp.anfragetext"));

			panelQueryAnfragetext.updateButtons(panelBottomAnfragetext
					.getLockedstateDetailMainKey());

			// wenn es fuer das tabbed pane noch keinen Eintrag gibt,
			// die restlichen oberen Laschen deaktivieren, ausser ...
			if (panelQueryAnfragetext.getSelectedId() == null) {
				getInternalFrame().enableAllOberePanelsExceptMe(this,
						IDX_PANEL_ANFRAGETEXT, false);
			}
			break;

		case IDX_PANEL_ANFRAGEPOSITIONART:
			refreshPanelAnfragepositionart();
			panelQueryAnfragepositionart.eventYouAreSelected(false);
			setTitle(LPMain.getInstance().getTextRespectUISPr(
					"anf.anfragepositionart"));

			panelQueryAnfragepositionart
					.updateButtons(panelBottomAnfragepositionart
							.getLockedstateDetailMainKey());

			// wenn es fuer das tabbed pane noch keinen Eintrag gibt,
			// die restlichen oberen Laschen deaktivieren, ausser ...
			if (panelQueryAnfragepositionart.getSelectedId() == null) {
				getInternalFrame().enableAllOberePanelsExceptMe(this,
						IDX_PANEL_ANFRAGEPOSITIONART, false);
			}
			break;

		case IDX_PANEL_ANFRAGEART:
			refreshPanelAnfrageart();
			panelAnfrageartTopQP1.eventYouAreSelected(false);
			setTitle(LPMain.getInstance().getTextRespectUISPr("anf.anfrageart"));

			panelAnfrageartTopQP1.updateButtons(panelAnfrageartBottomD1
					.getLockedstateDetailMainKey());

			// wenn es fuer das tabbed pane noch keinen Eintrag gibt,
			// die restlichen oberen Laschen deaktivieren, ausser ...
			if (panelAnfrageartTopQP1.getSelectedId() == null) {
				getInternalFrame().enableAllOberePanelsExceptMe(this,
						IDX_PANEL_ANFRAGEART, false);
			}
			break;

		case IDX_PANEL_ERLEDIGUNGSGRUND:
			refreshPanelErledigungsgrund();
			panelErledigungsgrundTopQP1.eventYouAreSelected(false);
			setTitle(LPMain.getInstance().getTextRespectUISPr(
					"anf.erledigungsgrund"));

			panelErledigungsgrundTopQP1
					.updateButtons(panelErledigungsgrundBottomD1
							.getLockedstateDetailMainKey());

			// wenn es fuer das tabbed pane noch keinen Eintrag gibt,
			// die restlichen oberen Laschen deaktivieren, ausser ...
			if (panelErledigungsgrundTopQP1.getSelectedId() == null) {
				getInternalFrame().enableAllOberePanelsExceptMe(this,
						IDX_PANEL_ERLEDIGUNGSGRUND, false);
			}
			break;

		case IDX_PANEL_ZERTIFIKATART:
			refreshPanelZertifikatart();
			panelZertifikatartTopQP1.eventYouAreSelected(false);
			setTitle(LPMain.getInstance().getTextRespectUISPr(
					"anf.zertifikatart"));

			panelZertifikatartTopQP1.updateButtons(panelZertifikatartBottomD1
					.getLockedstateDetailMainKey());

			// wenn es fuer das tabbed pane noch keinen Eintrag gibt,
			// die restlichen oberen Laschen deaktivieren, ausser ...
			if (panelZertifikatartTopQP1.getSelectedId() == null) {
				getInternalFrame().enableAllOberePanelsExceptMe(this,
						IDX_PANEL_ZERTIFIKATART, false);
			}
			break;

		}
	}

	protected void lPActionEvent(java.awt.event.ActionEvent e) {
	}

	private void refreshPanelAnfragetext() throws Throwable {
		if (panelSplitAnfragetext == null) {
			DelegateFactory
					.getInstance()
					.getAnfrageServiceDelegate()
					.anfragetextFindByMandantLocaleCNr(
							LPMain.getInstance().getTheClient()
									.getLocUiAsString(),
							MediaFac.MEDIAART_KOPFTEXT);

			DelegateFactory
					.getInstance()
					.getAnfrageServiceDelegate()
					.anfragetextFindByMandantLocaleCNr(
							LPMain.getInstance().getTheClient()
									.getLocUiAsString(),
							MediaFac.MEDIAART_FUSSTEXT);

			String[] aWhichStandardButtonIUse = null;

			panelQueryAnfragetext = new PanelQuery(null, null,
					QueryParameters.UC_ID_ANFRAGETEXT,
					aWhichStandardButtonIUse, getInternalFrame(), LPMain
							.getInstance()
							.getTextRespectUISPr("lp.anfragetext"), true);

			panelBottomAnfragetext = new PanelStammdatenCRUD(
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("lp.anfragetext"), null,
					HelperClient.SCRUD_ANFRAGETEXT_FILE,
					getInternalFrameAnfrage(), HelperClient.LOCKME_ANFRAGETEXT);

			panelSplitAnfragetext = new PanelSplit(getInternalFrame(),
					panelBottomAnfragetext, panelQueryAnfragetext,
					400 - ((PanelStammdatenCRUD) panelBottomAnfragetext)
							.getAnzahlControls() * 30);

			setComponentAt(IDX_PANEL_ANFRAGETEXT, panelSplitAnfragetext);
		}
	}

	private void refreshPanelAnfragepositionart() throws Throwable {
		String[] aWhichStandardButtonIUse = {};

		if (panelSplitAnfragepositionart == null) {
			panelQueryAnfragepositionart = new PanelQuery(null, null,
					QueryParameters.UC_ID_ANFRAGEPOSITIONART,
					aWhichStandardButtonIUse, getInternalFrame(), LPMain
							.getInstance().getTextRespectUISPr(
									"anf.anfragepositionart"), true);

			panelBottomAnfragepositionart = new PanelStammdatenCRUD(
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("anf.anfragepositionart"),
					null, HelperClient.SCRUD_ANFRAGEPOSITIONART_FILE,
					getInternalFrameAnfrage(),
					HelperClient.LOCKME_ANFRAGEPOSITIONART);

			panelSplitAnfragepositionart = new PanelSplit(getInternalFrame(),
					panelBottomAnfragepositionart,
					panelQueryAnfragepositionart, 200);

			setComponentAt(IDX_PANEL_ANFRAGEPOSITIONART,
					panelSplitAnfragepositionart);

			// liste soll sofort angezeigt werden
			panelQueryAnfragepositionart.eventYouAreSelected(true);
		}
	}

	private void refreshPanelAnfrageart() throws Throwable {
		String[] aWhichStandardButtonIUse = {};

		if (panelAnfrageartTopQP1 == null) {
			panelAnfrageartTopQP1 = new PanelQuery(null, null,
					QueryParameters.UC_ID_ANFRAGEART, aWhichStandardButtonIUse,
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("anf.anfrageart"), true);

			panelAnfrageartBottomD1 = new PanelStammdatenCRUD(
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("anf.anfrageart"), null,
					HelperClient.SCRUD_ANFRAGEART_FILE,
					getInternalFrameAnfrage(), HelperClient.LOCKME_ANFRAGEART);

			panelAnfrageartSP1 = new PanelSplit(getInternalFrame(),
					panelAnfrageartBottomD1, panelAnfrageartTopQP1, 200);

			setComponentAt(IDX_PANEL_ANFRAGEART, panelAnfrageartSP1);

			// liste soll sofort angezeigt werden
			panelAnfrageartTopQP1.eventYouAreSelected(true);
		}
	}

	private void refreshPanelErledigungsgrund() throws Throwable {

		if (panelErledigungsgrundTopQP1 == null) {

			String[] aWhichStandardButtonIUse = { PanelBasis.ACTION_NEW };

			FilterKriterium[] krit = new FilterKriterium[2];

			krit[0] = new FilterKriterium("c_bez", true,
					"('" + AnfrageServiceFac.ANFRAGEERLEDIGUNGSGRUND_BESTELLT
							+ "')", FilterKriterium.OPERATOR_NOT_IN, false);
			krit[1] = SystemFilterFactory.getInstance().createFKMandantCNr()[0];

			panelErledigungsgrundTopQP1 = new PanelQuery(null, krit,
					QueryParameters.UC_ID_ANFRAGEERLEDIGUNGSGRUND,
					aWhichStandardButtonIUse, getInternalFrame(), LPMain
							.getInstance().getTextRespectUISPr(
									"anf.erledigungsgrund"), true);

			panelErledigungsgrundBottomD1 = new PanelAnfrageerledigungsgrund(
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("anf.erledigungsgrund"), null);

			panelErledigungsgrundSP1 = new PanelSplit(getInternalFrame(),
					panelErledigungsgrundBottomD1, panelErledigungsgrundTopQP1,
					200);

			setComponentAt(IDX_PANEL_ERLEDIGUNGSGRUND, panelErledigungsgrundSP1);

			// liste soll sofort angezeigt werden
			panelErledigungsgrundTopQP1.eventYouAreSelected(true);
		}
	}

	private void refreshPanelZertifikatart() throws Throwable {
		String[] aWhichStandardButtonIUse = { PanelBasis.ACTION_NEW };

		if (panelZertifikatartTopQP1 == null) {

			panelZertifikatartTopQP1 = new PanelQuery(null, SystemFilterFactory
					.getInstance().createFKMandantCNr(),
					QueryParameters.UC_ID_ZERTIFIKATART,
					aWhichStandardButtonIUse, getInternalFrame(), LPMain
							.getInstance().getTextRespectUISPr(
									"anf.zertifikatart"), true);

			panelZertifikatartBottomD1 = new PanelZertifikatart(
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("anf.zertifikatart"), null);

			panelZertifikatartSP1 = new PanelSplit(getInternalFrame(),
					panelZertifikatartBottomD1, panelZertifikatartTopQP1, 200);

			setComponentAt(IDX_PANEL_ZERTIFIKATART, panelZertifikatartSP1);

			// liste soll sofort angezeigt werden
			panelZertifikatartTopQP1.eventYouAreSelected(true);
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
