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
package com.lp.client.auftrag;

import javax.swing.event.ChangeEvent;

import com.lp.client.artikel.ArtikelFilterFactory;
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
import com.lp.client.lieferschein.PanelBegruendung;
import com.lp.client.pc.LPMain;
import com.lp.client.system.SystemFilterFactory;
import com.lp.server.system.service.MandantFac;
import com.lp.server.system.service.MediaFac;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;

@SuppressWarnings("static-access")
/*
 * <p><I>Diese Klasse kuemmert sich um die Wartung der Grunddaten des
 * Auftrags.</I> </p>
 * 
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 * 
 * <p>Erstellungsdatum <I>19.07.05</I></p>
 * 
 * <p> </p>
 * 
 * @author Uli Walch
 * 
 * @version $Revision: 1.5 $
 */
public class TabbedPaneAuftragGrunddaten extends TabbedPane {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private PanelQuery panelAuftragtextTopQP;
	private PanelSplit panelAuftragtextSP;
	private PanelStammdatenCRUD panelAuftragtextBottomD;

	private PanelQuery panelAuftragartTopQP;
	private PanelSplit panelAuftragartSP;
	private PanelStammdatenCRUD panelAuftragartBottomD;

	private PanelQuery panelMeilensteinTopQP;
	private PanelSplit panelMeilensteinSP;
	private PanelStammdatenCRUD panelMeilensteinBottomD;

	private PanelQuery panelAuftragpositionartTopQP;
	private PanelSplit panelAuftragpositionartSP;
	private PanelStammdatenCRUD panelAuftragpositionartBottomD;

	private PanelQuery panelAuftragDokumentQP;
	private PanelSplit panelAuftragDokumentSP;
	private PanelBasis panelAuftragDokumentD;

	private PanelQuery panelBegruendungTopQP;
	private PanelSplit panelBegruendungSP;
	private PanelAuftragbegruendung panelBegruendungBottomD;

	private static final int IDX_PANEL_AUFTRAGTEXT = 0;
	private static final int IDX_PANEL_AUFTRAGART = 1;
	private static final int IDX_PANEL_AUFTRAGPOSITIONART = 2;
	private static final int IDX_PANEL_AUFTRAGDOKUMENTE = 3;
	private static final int IDX_PANEL_BEGRUENDUNG = 4;
	private static final int IDX_PANEL_MEILENSTEIN = 5;

	public TabbedPaneAuftragGrunddaten(InternalFrame internalFrameI)
			throws Throwable {
		super(internalFrameI, LPMain.getTextRespectUISPr("lp.grunddaten"));
		jbInit();
		initComponents();
	}

	public InternalFrameAuftrag getInternalFrameAuftrag() {
		return (InternalFrameAuftrag) getInternalFrame();
	}

	private void jbInit() throws Throwable {
		insertTab(LPMain.getInstance().getTextRespectUISPr("lp.auftragtext"),
				null, null,
				LPMain.getInstance().getTextRespectUISPr("lp.auftragtext"),
				IDX_PANEL_AUFTRAGTEXT);

		insertTab(
				LPMain.getInstance().getTextRespectUISPr(
						"detail.label.auftragart"),
				null,
				null,
				LPMain.getInstance().getTextRespectUISPr(
						"detail.label.auftragart"), IDX_PANEL_AUFTRAGART);

		insertTab(
				LPMain.getInstance().getTextRespectUISPr(
						"auft.auftragpositionart"),
				null,
				null,
				LPMain.getInstance().getTextRespectUISPr(
						"auft.auftragpositionart"),
				IDX_PANEL_AUFTRAGPOSITIONART);

		insertTab(
				LPMain.getInstance()
						.getTextRespectUISPr("auft.auftragdokument"), null,
				null,
				LPMain.getInstance()
						.getTextRespectUISPr("auft.auftragdokument"),
				IDX_PANEL_AUFTRAGDOKUMENTE);
		insertTab(LPMain.getInstance().getTextRespectUISPr("ls.begruendung"),
				null, null,
				LPMain.getInstance().getTextRespectUISPr("ls.begruendung"),
				IDX_PANEL_BEGRUENDUNG);

		if (LPMain
				.getInstance()
				.getDesktop()
				.darfAnwenderAufZusatzfunktionZugreifen(
						MandantFac.ZUSATZFUNKTION_ERWEITERTE_PROJEKTSTEUERUNG)) {

			insertTab(
					LPMain.getInstance()
							.getTextRespectUISPr("auft.meilenstein"), null,
					null,
					LPMain.getInstance()
							.getTextRespectUISPr("auft.meilenstein"),
					IDX_PANEL_MEILENSTEIN);
		}
		// default
		refreshPanelAuftragtext();

		setSelectedComponent(panelAuftragtextSP);

		panelAuftragtextTopQP.eventYouAreSelected(false);

		getInternalFrameAuftrag().getAuftragtextDto().setIId(
				(Integer) panelAuftragtextTopQP.getSelectedId());

		addChangeListener(this);
		getInternalFrame().addItemChangedListener(this);
	}

	public void lPEventItemChanged(ItemChangedEvent eI) throws Throwable {

		ItemChangedEvent e = (ItemChangedEvent) eI;

		if (e.getID() == ItemChangedEvent.ITEM_CHANGED) {
			if (eI.getSource() == panelAuftragtextTopQP) {
				Integer iId = (Integer) panelAuftragtextTopQP.getSelectedId();
				getInternalFrame().setKeyWasForLockMe(iId + "");
				panelAuftragtextBottomD.setKeyWhenDetailPanel(iId);
				panelAuftragtextBottomD.eventYouAreSelected(false);

				// im QP die Buttons in den Zustand nolocking/save setzen.
				panelAuftragtextTopQP.updateButtons(panelAuftragtextBottomD
						.getLockedstateDetailMainKey());
			} else if (eI.getSource() == panelAuftragartTopQP) {
				String cNr = (String) panelAuftragartTopQP.getSelectedId();
				getInternalFrame().setKeyWasForLockMe(cNr);
				panelAuftragartBottomD.setKeyWhenDetailPanel(cNr);
				panelAuftragartBottomD.eventYouAreSelected(false);

				// im QP die Buttons in den Zustand nolocking/save setzen.
				panelAuftragartTopQP.updateButtons(panelAuftragartBottomD
						.getLockedstateDetailMainKey());
			} else if (eI.getSource() == panelAuftragpositionartTopQP) {
				String cNr = (String) panelAuftragpositionartTopQP
						.getSelectedId();
				getInternalFrame().setKeyWasForLockMe(cNr);
				panelAuftragpositionartBottomD.setKeyWhenDetailPanel(cNr);
				panelAuftragpositionartBottomD.eventYouAreSelected(false);

				// im QP die Buttons in den Zustand nolocking/save setzen.
				panelAuftragpositionartTopQP
						.updateButtons(panelAuftragpositionartBottomD
								.getLockedstateDetailMainKey());
			} else if (eI.getSource() == panelAuftragDokumentQP) {
				Integer iId = (Integer) panelAuftragDokumentQP.getSelectedId();
				getInternalFrame().setKeyWasForLockMe(iId + "");
				panelAuftragDokumentD.setKeyWhenDetailPanel(iId);
				panelAuftragDokumentD.eventYouAreSelected(false);

				// im QP die Buttons in den Zustand nolocking/save setzen.
				panelAuftragDokumentQP.updateButtons(panelAuftragDokumentD
						.getLockedstateDetailMainKey());
			} else if (eI.getSource() == panelBegruendungTopQP) {
				Integer iId = (Integer) panelBegruendungTopQP.getSelectedId();
				getInternalFrame().setKeyWasForLockMe(iId + "");
				panelBegruendungBottomD.setKeyWhenDetailPanel(iId);
				panelBegruendungBottomD.eventYouAreSelected(false);

				panelBegruendungTopQP.updateButtons(panelBegruendungBottomD
						.getLockedstateDetailMainKey());
			} else if (eI.getSource() == panelMeilensteinTopQP) {
				Integer iId = (Integer) panelMeilensteinTopQP.getSelectedId();
				getInternalFrame().setKeyWasForLockMe(iId + "");
				panelMeilensteinBottomD.setKeyWhenDetailPanel(iId);
				panelMeilensteinBottomD.eventYouAreSelected(false);

				panelMeilensteinTopQP.updateButtons(panelMeilensteinBottomD
						.getLockedstateDetailMainKey());
			}
		} else if (eI.getID() == ItemChangedEvent.ACTION_UPDATE) {
			// hier kommt man nach upd im D bei einem 1:n hin.
			if (eI.getSource() == panelAuftragtextBottomD) {
				// im QP die Buttons in den Zustand neu setzen.
				panelAuftragtextTopQP.updateButtons(new LockStateValue(
						PanelBasis.LOCK_FOR_NEW));
				;
			} else if (eI.getSource() == panelAuftragartBottomD) {
				// im QP die Buttons in den Zustand neu setzen.
				panelAuftragartTopQP.updateButtons(new LockStateValue(
						PanelBasis.LOCK_FOR_NEW));
				;
			} else if (eI.getSource() == panelAuftragpositionartBottomD) {
				// im QP die Buttons in den Zustand neu setzen.
				panelAuftragpositionartTopQP.updateButtons(new LockStateValue(
						PanelBasis.LOCK_FOR_NEW));
				;
			} else if (eI.getSource() == panelAuftragDokumentD) {
				// im QP die Buttons in den Zustand neu setzen.
				panelAuftragDokumentQP.updateButtons(new LockStateValue(
						PanelBasis.LOCK_FOR_NEW));
				;
			} else if (eI.getSource() == panelBegruendungBottomD) {
				// im QP die Buttons in den Zustand neu setzen.
				panelBegruendungTopQP.updateButtons(new LockStateValue(
						PanelBasis.LOCK_FOR_NEW));
				;
			} else if (eI.getSource() == panelMeilensteinBottomD) {
				// im QP die Buttons in den Zustand neu setzen.
				panelMeilensteinTopQP.updateButtons(new LockStateValue(
						PanelBasis.LOCK_FOR_NEW));
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_DISCARD) {
			if (e.getSource() == panelAuftragtextBottomD) {
				panelAuftragtextSP.eventYouAreSelected(false);
			} else if (e.getSource() == panelAuftragartBottomD) {
				panelAuftragartSP.eventYouAreSelected(false);
			} else if (e.getSource() == panelAuftragpositionartBottomD) {
				panelAuftragpositionartSP.eventYouAreSelected(false);
			} else if (e.getSource() == panelAuftragDokumentD) {
				panelAuftragDokumentSP.eventYouAreSelected(false);
			} else if (e.getSource() == panelBegruendungBottomD) {
				panelBegruendungSP.eventYouAreSelected(false);
			} else if (e.getSource() == panelMeilensteinTopQP) {
				panelMeilensteinSP.eventYouAreSelected(false);
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_SAVE) {
			if (e.getSource() == panelAuftragtextBottomD) {
				Object oKey = panelAuftragtextBottomD.getKeyWhenDetailPanel();
				panelAuftragtextTopQP.eventYouAreSelected(false);
				panelAuftragtextTopQP.setSelectedId(oKey);
				panelAuftragtextSP.eventYouAreSelected(false);
			} else if (e.getSource() == panelAuftragartBottomD) {
				Object oKey = panelAuftragartBottomD.getKeyWhenDetailPanel();
				panelAuftragartTopQP.eventYouAreSelected(false);
				panelAuftragartTopQP.setSelectedId(oKey);
				panelAuftragartSP.eventYouAreSelected(false);
			} else if (e.getSource() == panelAuftragpositionartBottomD) {
				Object oKey = panelAuftragpositionartBottomD
						.getKeyWhenDetailPanel();
				panelAuftragpositionartTopQP.eventYouAreSelected(false);
				panelAuftragpositionartTopQP.setSelectedId(oKey);
				panelAuftragpositionartSP.eventYouAreSelected(false);
			} else if (e.getSource() == panelAuftragDokumentD) {
				Object oKey = panelAuftragDokumentD.getKeyWhenDetailPanel();
				panelAuftragDokumentQP.eventYouAreSelected(false);
				panelAuftragDokumentQP.setSelectedId(oKey);
				panelAuftragDokumentSP.eventYouAreSelected(false);
			} else if (e.getSource() == panelBegruendungBottomD) {
				Object oKey = panelBegruendungBottomD.getKeyWhenDetailPanel();
				panelBegruendungTopQP.eventYouAreSelected(false);
				panelBegruendungTopQP.setSelectedId(oKey);
				panelBegruendungSP.eventYouAreSelected(false);
			} else if (e.getSource() == panelMeilensteinBottomD) {
				Object oKey = panelMeilensteinBottomD.getKeyWhenDetailPanel();
				panelMeilensteinTopQP.eventYouAreSelected(false);
				panelMeilensteinTopQP.setSelectedId(oKey);
				panelMeilensteinSP.eventYouAreSelected(false);
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_GOTO_MY_DEFAULT_QP) {
			if (e.getSource() == panelAuftragtextBottomD) {
				panelAuftragtextSP.eventYouAreSelected(false); // refresh auf
				// das gesamte
				// 1:n panel
			} else if (e.getSource() == panelAuftragartBottomD) {
				panelAuftragartSP.eventYouAreSelected(false); // refresh auf das
				// gesamte 1:n
				// panel
			} else if (e.getSource() == panelAuftragpositionartBottomD) {
				panelAuftragpositionartSP.eventYouAreSelected(false); // refresh
				// auf
				// das
				// gesamte
				// 1:n
				// panel
			}

			else if (e.getSource() == panelAuftragDokumentD) {
				panelAuftragDokumentSP.eventYouAreSelected(false); // refresh
				// auf das
				// gesamte
				// 1:n panel
			} else if (e.getSource() == panelBegruendungBottomD) {
				panelBegruendungSP.eventYouAreSelected(false); // refresh
				// auf
				// das
				// gesamte
				// 1:n
				// panel
			} else if (e.getSource() == panelMeilensteinBottomD) {
				panelMeilensteinSP.eventYouAreSelected(false);
			}
		} else if (eI.getID() == ItemChangedEvent.ACTION_NEW) {
			if (eI.getSource() == panelAuftragtextTopQP) {
				if (panelAuftragtextTopQP.getSelectedId() == null) {
					getInternalFrame().enableAllPanelsExcept(true);
				}
				panelAuftragtextBottomD.eventActionNew(eI, true, false);
				panelAuftragtextBottomD.eventYouAreSelected(false);
				setSelectedComponent(panelAuftragtextSP);
			} else if (eI.getSource() == panelAuftragartTopQP) {
				if (panelAuftragartTopQP.getSelectedId() == null) {
					getInternalFrame().enableAllPanelsExcept(true);
				}
				panelAuftragartBottomD.eventActionNew(eI, true, false);
				panelAuftragartBottomD.eventYouAreSelected(false);
				setSelectedComponent(panelAuftragartSP);
			} else if (eI.getSource() == panelAuftragpositionartTopQP) {
				if (panelAuftragpositionartTopQP.getSelectedId() == null) {
					getInternalFrame().enableAllPanelsExcept(true);
				}
				panelAuftragpositionartBottomD.eventActionNew(eI, true, false);
				panelAuftragpositionartBottomD.eventYouAreSelected(false);
				setSelectedComponent(panelAuftragpositionartSP);
			} else if (eI.getSource() == panelAuftragDokumentQP) {
				if (panelAuftragDokumentQP.getSelectedId() == null) {
					getInternalFrame().enableAllPanelsExcept(true);
				}
				panelAuftragDokumentD.eventActionNew(eI, true, false);
				panelAuftragDokumentD.eventYouAreSelected(false);
				setSelectedComponent(panelAuftragDokumentSP);
			} else if (eI.getSource() == panelBegruendungTopQP) {
				if (panelBegruendungTopQP.getSelectedId() == null) {
					getInternalFrame().enableAllPanelsExcept(true);
				}
				panelBegruendungBottomD.eventActionNew(eI, true, false);
				panelBegruendungBottomD.eventYouAreSelected(false);
				setSelectedComponent(panelBegruendungSP);
			} else if (eI.getSource() == panelMeilensteinTopQP) {
				if (panelMeilensteinTopQP.getSelectedId() == null) {
					getInternalFrame().enableAllPanelsExcept(true);
				}
				panelMeilensteinBottomD.eventActionNew(eI, true, false);
				panelMeilensteinBottomD.eventYouAreSelected(false);
				setSelectedComponent(panelMeilensteinSP);
			}
		}
	}

	public void lPEventObjectChanged(ChangeEvent e) throws Throwable {
		super.lPEventObjectChanged(e);

		int selectedIndex = this.getSelectedIndex();

		switch (selectedIndex) {
		case IDX_PANEL_AUFTRAGTEXT: {
			refreshPanelAuftragtext();
			panelAuftragtextTopQP.setDefaultFilter(AuftragFilterFactory
					.getInstance().createFKAuftragtext());
			panelAuftragtextTopQP.eventYouAreSelected(false);
			setTitle(LPMain.getInstance().getTextRespectUISPr("lp.auftragtext"));

			// im QP die Buttons setzen.
			panelAuftragtextTopQP.updateButtons(panelAuftragtextBottomD
					.getLockedstateDetailMainKey());

			// wenn es fuer das tabbed pane noch keinen Eintrag gibt,
			// die restlichen oberen Laschen deaktivieren, ausser ...
			if (panelAuftragtextTopQP.getSelectedId() == null) {
				getInternalFrame().enableAllOberePanelsExceptMe(this,
						IDX_PANEL_AUFTRAGTEXT, false);
			}
			break;
		}

		case IDX_PANEL_AUFTRAGART: {
			refreshPanelAuftragart();
			panelAuftragartTopQP.eventYouAreSelected(false);
			setTitle(LPMain.getInstance().getTextRespectUISPr(
					"detail.label.auftragart"));

			// im QP die Buttons setzen.
			panelAuftragartTopQP.updateButtons(panelAuftragartBottomD
					.getLockedstateDetailMainKey());

			// wenn es fuer das tabbed pane noch keinen Eintrag gibt,
			// die restlichen oberen Laschen deaktivieren, ausser ...
			if (panelAuftragartTopQP.getSelectedId() == null) {
				getInternalFrame().enableAllOberePanelsExceptMe(this,
						IDX_PANEL_AUFTRAGART, false);
			}
			break;
		}
		case IDX_PANEL_MEILENSTEIN: {
			refreshPanelMeilenstein();
			panelMeilensteinTopQP.eventYouAreSelected(false);
			setTitle(LPMain.getInstance().getTextRespectUISPr(
					"auft.meilenstein"));

			// im QP die Buttons setzen.
			panelMeilensteinTopQP.updateButtons(panelMeilensteinBottomD
					.getLockedstateDetailMainKey());

			// wenn es fuer das tabbed pane noch keinen Eintrag gibt,
			// die restlichen oberen Laschen deaktivieren, ausser ...
			if (panelMeilensteinTopQP.getSelectedId() == null) {
				getInternalFrame().enableAllOberePanelsExceptMe(this,
						IDX_PANEL_MEILENSTEIN, false);
			}
			break;
		}

		case IDX_PANEL_AUFTRAGPOSITIONART: {
			refreshPanelAuftragpositionart();
			panelAuftragpositionartTopQP.eventYouAreSelected(false);
			setTitle(LPMain.getInstance().getTextRespectUISPr(
					"auft.auftragpositionart"));

			// im QP die Buttons setzen.
			panelAuftragpositionartTopQP
					.updateButtons(panelAuftragpositionartBottomD
							.getLockedstateDetailMainKey());

			// wenn es fuer das tabbed pane noch keinen Eintrag gibt,
			// die restlichen oberen Laschen deaktivieren, ausser ...
			if (panelAuftragpositionartTopQP.getSelectedId() == null) {
				getInternalFrame().enableAllOberePanelsExceptMe(this,
						IDX_PANEL_AUFTRAGPOSITIONART, false);
			}
			break;
		}
		case IDX_PANEL_AUFTRAGDOKUMENTE: {
			refreshPanelAuftragdokument();
			panelAuftragDokumentQP.eventYouAreSelected(false);
			setTitle(LPMain.getInstance().getTextRespectUISPr(
					"auft.auftragdokument"));

			// im QP die Buttons setzen.
			panelAuftragDokumentQP.updateButtons(panelAuftragDokumentD
					.getLockedstateDetailMainKey());

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

	private void refreshPanelBegruendung() throws Throwable {
		if (panelBegruendungSP == null) {
			String[] aWhichStandardButtonIUse = { PanelBasis.ACTION_NEW };

			panelBegruendungTopQP = new PanelQuery(null, null,
					QueryParameters.UC_ID_AUFTRAGBEGRUENDUNG,
					aWhichStandardButtonIUse, getInternalFrame(), LPMain
							.getInstance()
							.getTextRespectUISPr("ls.begruendung"), true);

			panelBegruendungBottomD = new PanelAuftragbegruendung(
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("ls.begruendung"), null);

			panelBegruendungSP = new PanelSplit(getInternalFrame(),
					panelBegruendungBottomD, panelBegruendungTopQP, 350);

			setComponentAt(IDX_PANEL_BEGRUENDUNG, panelBegruendungSP);
		}
	}

	protected void lPActionEvent(java.awt.event.ActionEvent e) {
	}

	private void refreshPanelAuftragtext() throws Throwable {
		if (panelAuftragtextSP == null) {
			// der Kopftext muss in der Konzerndatensprache hinterlegt sein oder
			// werden
			DelegateFactory
					.getInstance()
					.getAuftragServiceDelegate()
					.auftragtextFindByMandantLocaleCNr(
							LPMain.getInstance().getTheClient()
									.getLocUiAsString(),
							MediaFac.MEDIAART_KOPFTEXT);
			// der Fusstext muss in der Konzerndatensprache hinterlegt sein oder
			// werden
			DelegateFactory
					.getInstance()
					.getAuftragServiceDelegate()
					.auftragtextFindByMandantLocaleCNr(
							LPMain.getInstance().getTheClient()
									.getLocUiAsString(),
							MediaFac.MEDIAART_FUSSTEXT);
			String[] aWhichStandardButtonIUse = null;

			panelAuftragtextTopQP = new PanelQuery(null, null,
					QueryParameters.UC_ID_AUFTRAGTEXT,
					aWhichStandardButtonIUse, getInternalFrame(), LPMain
							.getInstance()
							.getTextRespectUISPr("lp.auftragtext"), true);

			panelAuftragtextBottomD = new PanelStammdatenCRUD(
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("lp.auftragtext"), null,
					HelperClient.SCRUD_AUFTRAGTEXT_FILE,
					getInternalFrameAuftrag(), HelperClient.LOCKME_AUFTRAGTEXT);

			panelAuftragtextSP = new PanelSplit(getInternalFrame(),
					panelAuftragtextBottomD, panelAuftragtextTopQP,
					400 - ((PanelStammdatenCRUD) panelAuftragtextBottomD)
							.getAnzahlControls() * 30);

			setComponentAt(IDX_PANEL_AUFTRAGTEXT, panelAuftragtextSP);
		}
	}

	private void refreshPanelAuftragart() throws Throwable {
		if (panelAuftragartSP == null) {
			String[] aWhichStandardButtonIUse = null;

			panelAuftragartTopQP = new PanelQuery(null, null,
					QueryParameters.UC_ID_AUFTRAGART, aWhichStandardButtonIUse,
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("detail.label.auftragart"),
					true);

			panelAuftragartBottomD = new PanelStammdatenCRUD(
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("detail.label.auftragart"),
					null, HelperClient.SCRUD_AUFTRAGART_FILE,
					getInternalFrameAuftrag(), HelperClient.LOCKME_AUFTRAGART);

			panelAuftragartSP = new PanelSplit(getInternalFrame(),
					panelAuftragartBottomD, panelAuftragartTopQP,
					400 - ((PanelStammdatenCRUD) panelAuftragartBottomD)
							.getAnzahlControls() * 30);

			setComponentAt(IDX_PANEL_AUFTRAGART, panelAuftragartSP);
		}
	}

	private void refreshPanelMeilenstein() throws Throwable {
		if (panelMeilensteinSP == null) {
			String[] aWhichStandardButtonIUse = { PanelBasis.ACTION_NEW };

			panelMeilensteinTopQP = new PanelQuery(null, SystemFilterFactory
					.getInstance().createFKMandantCNr(),
					QueryParameters.UC_ID_MEILENSTEIN,
					aWhichStandardButtonIUse, getInternalFrame(), LPMain
							.getInstance().getTextRespectUISPr(
									"auft.meilenstein"), true);

			panelMeilensteinBottomD = new PanelStammdatenCRUD(
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("auft.meilenstein"), null,
					HelperClient.SCRUD_MEILENSTEIN_FILE,
					getInternalFrameAuftrag(), HelperClient.LOCKME_MEILENSTEIN);

			panelMeilensteinSP = new PanelSplit(getInternalFrame(),
					panelMeilensteinBottomD, panelMeilensteinTopQP,
					400 - ((PanelStammdatenCRUD) panelMeilensteinBottomD)
							.getAnzahlControls() * 30);

			setComponentAt(IDX_PANEL_MEILENSTEIN, panelMeilensteinSP);
		}
	}

	private void refreshPanelAuftragpositionart() throws Throwable {
		if (panelAuftragpositionartSP == null) {
			String[] aWhichStandardButtonIUse = null;

			panelAuftragpositionartTopQP = new PanelQuery(null, null,
					QueryParameters.UC_ID_AUFTRAGPOSITIONART,
					aWhichStandardButtonIUse, getInternalFrame(), LPMain
							.getInstance().getTextRespectUISPr(
									"auft.auftragpositionart"), true);

			panelAuftragpositionartBottomD = new PanelStammdatenCRUD(
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("auft.auftragpositionart"),
					null, HelperClient.SCRUD_AUFTRAGPOSITIONART_FILE,
					getInternalFrameAuftrag(),
					HelperClient.LOCKME_AUFTRAGPOSITIONART);

			panelAuftragpositionartSP = new PanelSplit(
					getInternalFrame(),
					panelAuftragpositionartBottomD,
					panelAuftragpositionartTopQP,
					400 - ((PanelStammdatenCRUD) panelAuftragpositionartBottomD)
							.getAnzahlControls() * 30);

			setComponentAt(IDX_PANEL_AUFTRAGPOSITIONART,
					panelAuftragpositionartSP);
		}
	}

	private void refreshPanelAuftragdokument() throws Throwable {
		if (panelAuftragDokumentSP == null) {
			String[] aWhichStandardButtonIUse = { PanelBasis.ACTION_NEW };

			panelAuftragDokumentQP = new PanelQuery(null, null,
					QueryParameters.UC_ID_AUFTRAGDOKUMENT,
					aWhichStandardButtonIUse, getInternalFrame(), LPMain
							.getInstance().getTextRespectUISPr(
									"auft.auftragdokument"), true);

			panelAuftragDokumentQP
					.befuellePanelFilterkriterienDirektUndVersteckte(
							AuftragFilterFactory.getInstance()
									.createFKDAuftragdokumentKennung(), null,
							AuftragFilterFactory.getInstance()
									.createFKVAuftragdokument());

			panelAuftragDokumentD = new PanelAuftragdokument(
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("auft.auftragdokument"), null);

			panelAuftragDokumentSP = new PanelSplit(getInternalFrame(),
					panelAuftragDokumentD, panelAuftragDokumentQP, 300);

			setComponentAt(IDX_PANEL_AUFTRAGDOKUMENTE, panelAuftragDokumentSP);
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
