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
package com.lp.client.system;

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
import com.lp.client.frame.stammdatencrud.PanelStammdatenCRUD;
import com.lp.client.pc.LPMain;
import com.lp.client.util.fastlanereader.gui.QueryType;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.system.service.MandantFac;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.util.Helper;

@SuppressWarnings("static-access")
/*
 * <p>Diese Klasse kuemmert sich um alle Mandantbelange.</p>
 * 
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 * 
 * <p>Erstellung: Josef Ornetsmueller; 08.03.05</p>
 * 
 * @author $Author: christian $
 * 
 * @version $Revision: 1.12 $
 */
public class TabbedPaneMandant extends TabbedPane {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static int IDX_PANEL_QP1 = -1;
	private static int IDX_PANEL_KOPFDATEN_D2 = -1;
	private static int IDX_PANEL_VORBELEGUNGEN = -1;
	private static int IDX_PANEL_VORBELEGUNGEN2 = -1;
	private static int IDX_PANEL_SPEDITEUR_SP4 = -1;
	private static int IDX_PANEL_ZAHLUNGSZIEL_SP5 = -1;
	private static int IDX_PANEL_KOSTENSTELLE_SP6 = -1;
	private static int IDX_PANEL_LIEFERART7 = -1;
	private static int IDX_PANEL_MWST_SP8 = -1;
	private static int IDX_PANEL_MWSTBEZ_SP9 = -1;
	private static int IDX_PANEL_DOKUMENTENLINK = -1;
	private static int IDX_PANEL_KOSTENTRAEGER = -1;

	private PanelQuery panelMandantQP1 = null;

	private PanelBasis panelMandantkopfdatenD2 = null;

	private PanelBasis panelMandantKonditionenD3 = null;

	private PanelBasis panelMandantVorbelegungen2 = null;

	private PanelQuery panelSpediteurQP4 = null;
	private PanelSplit panelSpediteurSP4 = null;
	private PanelSpediteur panelSpediteurBottomD4 = null;

	private PanelQuery panelZahlungszielQP5 = null;
	private PanelZahlungsziel panelZahlungszielBottomD5 = null;
	private PanelSplit panelZahlungszielSP5 = null;

	private PanelQuery panelMwstQP6 = null;
	private PanelStammdatenCRUD panelMwstBottomD6 = null;
	private PanelSplit panelMwstSP6 = null;
	private PanelMehrwertsteuer panelMwstBottom = null;

	private PanelQuery panelKostenstelleQP7 = null;
	private PanelSplit panelKostenstelleSP7 = null;
	private PanelKostenstelle panelKostenstelleBottomD7 = null;

	private PanelQuery panelQueryLieferart = null;
	private PanelBasis panelSplitLieferart = null;
	private PanelBasis panelBottomLieferart = null;

	private PanelQuery panelMwstbezQP = null;
	private PanelMwstsatzbez panelMwstbezBottom = null;
	private PanelSplit panelMwstbezSP = null;

	private PanelQuery panelQueryDokumentenlink = null;
	private PanelBasis panelBottomDokumentenlink = null;
	private PanelSplit panelSplitDokumentenlink = null;

	private PanelQuery panelQueryKostentraeger = null;
	private PanelBasis panelBottomKostentraeger = null;
	private PanelSplit panelSplitKostentraeger = null;

	private String mandantCNr = null;
	private boolean warnungEingeloggterMandant = true;

	public TabbedPaneMandant(InternalFrame internalFrameI) throws Throwable {
		super(internalFrameI, LPMain.getInstance().getTextRespectUISPr(
				"lp.mandant"));
		jbInit();
		initComponents();
	}

	private void jbInit() throws Throwable {
		int tabIndex = 0;
		IDX_PANEL_QP1 = tabIndex;
		// 1 tab oben: QP1 MandantFLR; lazy loading
		insertTab(LPMain.getInstance().getTextRespectUISPr("lp.auswahl"), null,
				null, LPMain.getInstance().getTextRespectUISPr("lp.auswahl"),
				IDX_PANEL_QP1);
		tabIndex++;
		IDX_PANEL_KOPFDATEN_D2 = tabIndex;
		// 2 tab oben: D2 Mandantkopfdaten; lazy loading
		insertTab(LPMain.getInstance().getTextRespectUISPr("lp.kopfdaten"),
				null, null,
				LPMain.getInstance().getTextRespectUISPr("lp.kopfdaten"),
				IDX_PANEL_KOPFDATEN_D2);
		tabIndex++;
		IDX_PANEL_VORBELEGUNGEN = tabIndex;
		// 3 tab oben: D3 Mandantdetail; lazy loading
		insertTab(
				LPMain.getInstance().getTextRespectUISPr(
						"sys.panel.vorbelegungen"),
				null,
				null,
				LPMain.getInstance().getTextRespectUISPr(
						"sys.panel.vorbelegungen"), IDX_PANEL_VORBELEGUNGEN);
		tabIndex++;
		IDX_PANEL_VORBELEGUNGEN2 = tabIndex;
		// 3 tab oben: D3 Mandantdetail; lazy loading
		insertTab(
				LPMain.getInstance().getTextRespectUISPr(
						"sys.panel.vorbelegungen")
						+ " 2", null, null, LPMain.getInstance()
						.getTextRespectUISPr("sys.panel.vorbelegungen") + " 2",
				IDX_PANEL_VORBELEGUNGEN2);
		tabIndex++;
		IDX_PANEL_SPEDITEUR_SP4 = tabIndex;
		// 4 tab oben: SP4 Spediteur; lazy loading
		insertTab(LPMain.getInstance().getTextRespectUISPr("lp.spediteur"),
				null, null,
				LPMain.getInstance().getTextRespectUISPr("lp.spediteur"),
				IDX_PANEL_SPEDITEUR_SP4);
		tabIndex++;
		IDX_PANEL_ZAHLUNGSZIEL_SP5 = tabIndex;
		// 5 tab oben: SP5 Zahlungsziel; lazy loading
		insertTab(LPMain.getInstance()
				.getTextRespectUISPr("label.zahlungsziel"), null, null, LPMain
				.getInstance().getTextRespectUISPr("label.zahlungsziel"),
				IDX_PANEL_ZAHLUNGSZIEL_SP5);

		if (LPMain
				.getInstance()
				.getDesktop()
				.darfAnwenderAufZusatzfunktionZugreifen(
						MandantFac.ZUSATZFUNKTION_KOSTENSTELLEN)) {
			tabIndex++;
			IDX_PANEL_KOSTENSTELLE_SP6 = tabIndex;
			// 7 tab oben: SP7 Kostenstelle; lazy loading
			insertTab(
					LPMain.getInstance().getTextRespectUISPr(
							"label.kostenstelle"),
					null,
					null,
					LPMain.getInstance().getTextRespectUISPr(
							"label.kostenstelle"), IDX_PANEL_KOSTENSTELLE_SP6);
		}
		tabIndex++;
		IDX_PANEL_LIEFERART7 = tabIndex;
		insertTab(LPMain.getInstance().getTextRespectUISPr("label.lieferart"),
				null, null,
				LPMain.getInstance().getTextRespectUISPr("label.lieferart"),
				IDX_PANEL_LIEFERART7);
		tabIndex++;
		IDX_PANEL_MWST_SP8 = tabIndex;
		insertTab(LPMain.getInstance().getTextRespectUISPr("lp.mwstshort"),
				null, null,
				LPMain.getInstance().getTextRespectUISPr("lp.mwstshort"),
				IDX_PANEL_MWST_SP8);
		tabIndex++;
		IDX_PANEL_MWSTBEZ_SP9 = tabIndex;
		insertTab(LPMain.getInstance()
				.getTextRespectUISPr("lp.mwstbezeichnung"), null, null, LPMain
				.getInstance().getTextRespectUISPr("lp.mwstbezeichnung"),
				IDX_PANEL_MWSTBEZ_SP9);
		tabIndex++;
		IDX_PANEL_DOKUMENTENLINK = tabIndex;
		insertTab(
				LPMain.getInstance().getTextRespectUISPr(
						"system.dokumentenlink"),
				null,
				null,
				LPMain.getInstance().getTextRespectUISPr(
						"system.dokumentenlink"), IDX_PANEL_DOKUMENTENLINK);

		if (LPMain
				.getInstance()
				.getDesktop()
				.darfAnwenderAufZusatzfunktionZugreifen(
						MandantFac.ZUSATZFUNKTION_KOSTENTRAEGER)) {
			tabIndex++;
			IDX_PANEL_KOSTENTRAEGER = tabIndex;
			insertTab(
					LPMain.getInstance()
							.getTextRespectUISPr("lp.kostentraeger"), null,
					null,
					LPMain.getInstance()
							.getTextRespectUISPr("lp.kostentraeger"),
					IDX_PANEL_KOSTENTRAEGER);
		}

		// Defaults ...
		// QP1 ist default.
		refreshMandantQP1();
		setSelectedComponent(panelMandantQP1);
		panelMandantQP1.eventYouAreSelected(false);
		// Beim ersten Einstieg auf diesen Tab sollte immer der aktuelle
		// Mandante selektiert sein
		panelMandantQP1.setSelectedId(LPMain.getInstance().getTheClient()
				.getMandant());

		// damit D2 einen aktuellen hat.
		ItemChangedEvent it = new ItemChangedEvent(panelMandantQP1,
				ItemChangedEvent.ITEM_CHANGED);
		lPEventItemChanged(it);

		addChangeListener(this);
		getInternalFrame().addItemChangedListener(this);
	}

	private void setMandantCNr(String mandantCNr) {
		this.mandantCNr = mandantCNr;
	}

	private String getMandantCNr() {
		return mandantCNr;
	}

	public void setWarnungEingeloggterMandant(boolean warnungEingeloggterMandant) {
		this.warnungEingeloggterMandant = warnungEingeloggterMandant;
	}

	public void lPEventItemChanged(ItemChangedEvent eI) throws Throwable {

		if (eI.getID() == ItemChangedEvent.ITEM_CHANGED) {
			if (eI.getSource() == panelMandantQP1) {
				String cNr = (String) panelMandantQP1.getSelectedId();
				this.setMandantCNr(cNr);
				this.setWarnungEingeloggterMandant(true);
				getInternalFrameSystem().getMandantDto().setCNr(cNr);
				getInternalFrame().setKeyWasForLockMe(cNr);

				getInternalFrame().setLpTitle(
						InternalFrame.TITLE_IDX_AS_I_LIKE,
						getInternalFrameSystem().getMandantDto().getCNr());

				// im QP die Buttons in den Zustand nolocking/save setzen.
				panelMandantQP1.updateButtons(panelMandantQP1
						.getLockedstateDetailMainKey());
			} else if (eI.getSource() == panelSpediteurQP4) {
				Integer iId = (Integer) panelSpediteurQP4.getSelectedId();
				getInternalFrame().setKeyWasForLockMe(iId + "");
				panelSpediteurBottomD4.setKeyWhenDetailPanel(iId);
				panelSpediteurBottomD4.eventYouAreSelected(false);
				// wenn Mandanten ungleich werden alle Buttons ausser refresh
				// weggeschalten
				if (LPMain.getInstance().getTheClient().getMandant()
						.equals(this.getMandantCNr())) {
					// im QP die Buttons setzen.
					this.panelSpediteurQP4
							.updateButtons(this.panelSpediteurBottomD4
									.getLockedstateDetailMainKey());
				} else {
					// im QP die Buttons setzen.
					LockStateValue l = new LockStateValue(
							PanelBasis.LOCK_ENABLE_REFRESHANDPRINT_ONLY);
					panelSpediteurQP4.updateButtons(l);
				}
			} else if (eI.getSource() == panelZahlungszielQP5) {
				Integer iId = (Integer) panelZahlungszielQP5.getSelectedId();
				getInternalFrame().setKeyWasForLockMe(iId + "");
				panelZahlungszielBottomD5.setKeyWhenDetailPanel(iId);
				panelZahlungszielBottomD5.eventYouAreSelected(false);
				// wenn Mandanten ungleich werden alle Buttons ausser refresh
				// weggeschalten
				if (LPMain.getInstance().getTheClient().getMandant()
						.equals(this.getMandantCNr())) {
					// im QP die Buttons in den Zustand nolocking/save setzen.
					panelZahlungszielQP5
							.updateButtons(panelZahlungszielBottomD5
									.getLockedstateDetailMainKey());
				} else {
					// im QP die Buttons setzen.
					LockStateValue l = new LockStateValue(
							PanelBasis.LOCK_ENABLE_REFRESHANDPRINT_ONLY);
					panelZahlungszielQP5.updateButtons(l);
				}
			} else if (eI.getSource() == panelMwstQP6) {
				Integer iId = (Integer) panelMwstQP6.getSelectedId();
				getInternalFrame().setKeyWasForLockMe(iId + "");
				panelMwstBottom.setKeyWhenDetailPanel(iId);
				panelMwstBottom.eventYouAreSelected(false);
				// wenn Mandanten ungleich werden alle Buttons ausser refresh
				// weggeschalten
				if (LPMain.getInstance().getTheClient().getMandant()
						.equals(this.getMandantCNr())) {
					// 1 im QP die Buttons in den Zustand nolocking/save setzen.
					panelMwstQP6.updateButtons(panelMwstBottom
							.getLockedstateDetailMainKey());
				} else {
					// im QP die Buttons setzen.
					LockStateValue l = new LockStateValue(
							PanelBasis.LOCK_ENABLE_REFRESHANDPRINT_ONLY);
					panelMwstQP6.updateButtons(l);
				}
			} else if (eI.getSource() == panelMwstbezQP) {
				Integer iId = (Integer) panelMwstbezQP.getSelectedId();
				getInternalFrame().setKeyWasForLockMe(iId + "");
				panelMwstbezBottom.setKeyWhenDetailPanel(iId);
				panelMwstbezBottom.eventYouAreSelected(false);
				// wenn Mandanten ungleich werden alle Buttons ausser refresh
				// weggeschalten
				if (LPMain.getInstance().getTheClient().getMandant()
						.equals(this.getMandantCNr())) {
					// 1 im QP die Buttons in den Zustand nolocking/save setzen.
					panelMwstbezQP.updateButtons(panelMwstbezBottom
							.getLockedstateDetailMainKey());
				} else {
					// im QP die Buttons setzen.
					LockStateValue l = new LockStateValue(
							PanelBasis.LOCK_ENABLE_REFRESHANDPRINT_ONLY);
					panelMwstbezQP.updateButtons(l);
				}
			}

			else if (eI.getSource() == panelKostenstelleQP7) {
				Integer iId = (Integer) panelKostenstelleQP7.getSelectedId();
				getInternalFrame().setKeyWasForLockMe(iId + "");
				panelKostenstelleBottomD7.setKeyWhenDetailPanel(iId);
				panelKostenstelleBottomD7.eventYouAreSelected(false);
				// wenn Mandanten ungleich werden alle Buttons ausser refresh
				// weggeschalten
				if (LPMain.getInstance().getTheClient().getMandant()
						.equals(this.getMandantCNr())) {
					// im QP die Buttons in den Zustand nolocking/save setzen.
					panelKostenstelleQP7
							.updateButtons(panelKostenstelleBottomD7
									.getLockedstateDetailMainKey());
				} else {
					// im QP die Buttons setzen.
					LockStateValue l = new LockStateValue(
							PanelBasis.LOCK_ENABLE_REFRESHANDPRINT_ONLY);
					panelKostenstelleQP7.updateButtons(l);
				}
			} else if (eI.getSource() == panelQueryLieferart) {
				Integer iId = (Integer) panelQueryLieferart.getSelectedId();
				getInternalFrame().setKeyWasForLockMe(iId + "");
				panelBottomLieferart.setKeyWhenDetailPanel(iId);
				panelBottomLieferart.eventYouAreSelected(false);

				// wenn Mandanten ungleich werden alle Buttons ausser refresh
				// weggeschalten
				if (LPMain.getInstance().getTheClient().getMandant()
						.equals(this.getMandantCNr())) {

					// im QP die Buttons in den Zustand nolocking/save setzen.
					panelQueryLieferart.updateButtons(panelBottomLieferart
							.getLockedstateDetailMainKey());
				} else {
					// im QP die Buttons setzen.
					LockStateValue l = new LockStateValue(
							PanelBasis.LOCK_ENABLE_REFRESHANDPRINT_ONLY);
					panelQueryLieferart.updateButtons(l);
				}
			} else if (eI.getSource() == panelQueryDokumentenlink) {
				Integer iId = (Integer) panelQueryDokumentenlink
						.getSelectedId();
				getInternalFrame().setKeyWasForLockMe(iId + "");
				panelBottomDokumentenlink.setKeyWhenDetailPanel(iId);
				panelBottomDokumentenlink.eventYouAreSelected(false);

				// wenn Mandanten ungleich werden alle Buttons ausser refresh
				// weggeschalten
				if (LPMain.getInstance().getTheClient().getMandant()
						.equals(this.getMandantCNr())) {

					// im QP die Buttons in den Zustand nolocking/save setzen.
					panelQueryDokumentenlink
							.updateButtons(panelBottomDokumentenlink
									.getLockedstateDetailMainKey());
				} else {
					// im QP die Buttons setzen.
					LockStateValue l = new LockStateValue(
							PanelBasis.LOCK_ENABLE_REFRESHANDPRINT_ONLY);
					panelQueryDokumentenlink.updateButtons(l);
				}
			} else if (eI.getSource() == panelQueryKostentraeger) {
				Integer iId = (Integer) panelQueryKostentraeger.getSelectedId();
				getInternalFrame().setKeyWasForLockMe(iId + "");
				panelBottomKostentraeger.setKeyWhenDetailPanel(iId);
				panelBottomKostentraeger.eventYouAreSelected(false);

				// wenn Mandanten ungleich werden alle Buttons ausser refresh
				// weggeschalten
				if (LPMain.getInstance().getTheClient().getMandant()
						.equals(this.getMandantCNr())) {

					// im QP die Buttons in den Zustand nolocking/save setzen.
					panelQueryKostentraeger
							.updateButtons(panelBottomKostentraeger
									.getLockedstateDetailMainKey());
				} else {
					// im QP die Buttons setzen.
					LockStateValue l = new LockStateValue(
							PanelBasis.LOCK_ENABLE_REFRESHANDPRINT_ONLY);
					panelQueryKostentraeger.updateButtons(l);
				}
			}
		}

		else if (eI.getID() == ItemChangedEvent.ACTION_UPDATE) {
			// hier kommt man nach upd im D bei einem 1:n hin.
			if (eI.getSource() == panelMwstBottom) {
				// 2 im QP die Buttons in den Zustand neu setzen.
				panelMwstQP6.updateButtons(new LockStateValue(
						PanelBasis.LOCK_FOR_NEW));
				;
			}
			if (eI.getSource() == panelMwstbezBottom) {
				// 2 im QP die Buttons in den Zustand neu setzen.
				panelMwstbezQP.updateButtons(new LockStateValue(
						PanelBasis.LOCK_FOR_NEW));
				;
			} else if (eI.getSource() == this.panelZahlungszielBottomD5) {
				// im QP die Buttons in den Zustand neu setzen.
				this.panelZahlungszielQP5.updateButtons(new LockStateValue(
						PanelBasis.LOCK_FOR_NEW));
			} else if (eI.getSource() == panelMandantkopfdatenD2) {
				// im QP die Buttons in den Zustand neu setzen.
				panelMandantQP1.updateButtons(new LockStateValue(
						PanelBasis.LOCK_FOR_NEW));
				;
			} else if (eI.getSource() == panelBottomLieferart) {
				// im QP die Buttons in den Zustand neu setzen.
				panelQueryLieferart.updateButtons(new LockStateValue(
						PanelBasis.LOCK_FOR_NEW));
				;
			} else if (eI.getSource() == panelBottomDokumentenlink) {
				// im QP die Buttons in den Zustand neu setzen.
				panelQueryDokumentenlink.updateButtons(new LockStateValue(
						PanelBasis.LOCK_FOR_NEW));
				;
			} else if (eI.getSource() == panelKostenstelleBottomD7) {
				// im QP die Buttons in den Zustand neu setzen.
				panelKostenstelleQP7.updateButtons(new LockStateValue(
						PanelBasis.LOCK_FOR_NEW));
				;
			} else if (eI.getSource() == panelSpediteurBottomD4) {
				// im QP die Buttons in den Zustand neu setzen.
				panelSpediteurQP4.updateButtons(new LockStateValue(
						PanelBasis.LOCK_FOR_NEW));
				;
			} else if (eI.getSource() == panelBottomKostentraeger) {
				// im QP die Buttons in den Zustand neu setzen.
				panelQueryKostentraeger.updateButtons(new LockStateValue(
						PanelBasis.LOCK_FOR_NEW));
				;
			}
		}

		else if (eI.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			if (eI.getSource() == panelMandantQP1) {
				setSelectedComponent(panelMandantkopfdatenD2);
			}
		}

		else if (eI.getID() == ItemChangedEvent.ACTION_GOTO_MY_DEFAULT_QP) {
			if (eI.getSource() == panelMandantkopfdatenD2) {
				panelMandantQP1.eventYouAreSelected(false);
				setKeyWasForLockMe();
				setSelectedComponent(panelMandantQP1);
				// Wenn es fuer das tabbed pane noch keinen Eintrag gibt, die
				// restlichen Panel deaktivieren.
				if (panelMandantQP1.getSelectedId() == null) {
					getInternalFrame().enableAllPanelsExcept(false);
				}
			} else if (eI.getSource() == panelSpediteurBottomD4) {
				panelSpediteurSP4.eventYouAreSelected(false);
			} else if (eI.getSource() == panelZahlungszielBottomD5) {
				panelZahlungszielSP5.eventYouAreSelected(false);
			} else if (eI.getSource() == panelMwstBottom) {
				panelMwstSP6.eventYouAreSelected(false);
			} else if (eI.getSource() == panelMwstbezBottom) {
				panelMwstbezSP.eventYouAreSelected(false);
			}

			else if (eI.getSource() == panelKostenstelleBottomD7) {
				panelKostenstelleSP7.eventYouAreSelected(false);
			} else if (eI.getSource() == panelBottomLieferart) {
				panelSplitLieferart.eventYouAreSelected(false); // refresh auf
				// das gesamte
				// 1:n panel
			} else if (eI.getSource() == panelBottomDokumentenlink) {
				panelSplitDokumentenlink.eventYouAreSelected(false);
			} else if (eI.getSource() == panelBottomKostentraeger) {
				panelSplitKostentraeger.eventYouAreSelected(false);
			}
		}

		else if (eI.getID() == ItemChangedEvent.ACTION_NEW) {
			// New ...
			if (eI.getSource() == panelMandantQP1) {
				refreshMandantkopfdatenD2(null);
				panelMandantkopfdatenD2.eventActionNew(eI, true, false);
				setSelectedComponent(panelMandantkopfdatenD2);
			} else if (eI.getSource() == panelSpediteurQP4) {
				panelSpediteurBottomD4.eventActionNew(eI, true, false);
				panelSpediteurBottomD4.eventYouAreSelected(false);
				setSelectedComponent(panelSpediteurSP4);
			} else if (eI.getSource() == panelZahlungszielQP5) {
				panelZahlungszielBottomD5.eventActionNew(eI, true, false);
				panelZahlungszielBottomD5.eventYouAreSelected(false);
				setSelectedComponent(panelZahlungszielSP5);
			} else if (eI.getSource() == panelMwstQP6) {
				panelMwstBottom.eventActionNew(eI, true, false);
				panelMwstBottom.eventYouAreSelected(false);
				setSelectedComponent(panelMwstSP6);
			} else if (eI.getSource() == panelMwstbezQP) {
				panelMwstbezBottom.eventActionNew(eI, true, false);
				panelMwstbezBottom.eventYouAreSelected(false);
				setSelectedComponent(panelMwstbezSP);
			} else if (eI.getSource() == panelKostenstelleQP7) {
				panelKostenstelleBottomD7.eventActionNew(eI, true, false);
				panelKostenstelleBottomD7.eventYouAreSelected(false);
				setSelectedComponent(panelKostenstelleSP7);
			} else if (eI.getSource() == panelQueryDokumentenlink) {
				panelBottomDokumentenlink.eventActionNew(eI, true, false);
				panelBottomDokumentenlink.eventYouAreSelected(false);
				setSelectedComponent(panelSplitDokumentenlink);
			} else if (eI.getSource() == panelQueryKostentraeger) {
				panelBottomKostentraeger.eventActionNew(eI, true, false);
				panelBottomKostentraeger.eventYouAreSelected(false);
				setSelectedComponent(panelSplitKostentraeger);
			} else if (eI.getSource() == panelQueryLieferart) {
				if (panelQueryLieferart.getSelectedId() == null) {
					getInternalFrame().enableAllPanelsExcept(true);
				}
				panelBottomLieferart.eventActionNew(eI, true, false);
				panelBottomLieferart.eventYouAreSelected(false);
				setSelectedComponent(panelSplitLieferart);
			}
		}

		else if (eI.getID() == ItemChangedEvent.ACTION_SAVE) {
			if (eI.getSource() == panelMandantkopfdatenD2) {
				Object oKey = panelMandantkopfdatenD2.getKeyWhenDetailPanel();
				panelMandantQP1.setSelectedId(oKey);
			} else if (eI.getSource() == panelMandantKonditionenD3) {
				Object oKey = panelMandantKonditionenD3.getKeyWhenDetailPanel();
				panelMandantQP1.setSelectedId(oKey);
			} else if (eI.getSource() == panelMandantVorbelegungen2) {
				Object oKey = panelMandantVorbelegungen2
						.getKeyWhenDetailPanel();
				panelMandantQP1.setSelectedId(oKey);
			} else if (eI.getSource() == panelBottomLieferart) {
				Object oKey = panelBottomLieferart.getKeyWhenDetailPanel();
				panelQueryLieferart.eventYouAreSelected(false);
				panelQueryLieferart.setSelectedId(oKey);
				panelSplitLieferart.eventYouAreSelected(false);
			} else if (eI.getSource() == panelBottomDokumentenlink) {
				Object oKey = panelBottomDokumentenlink.getKeyWhenDetailPanel();
				panelQueryDokumentenlink.eventYouAreSelected(false);
				panelQueryDokumentenlink.setSelectedId(oKey);
				panelSplitDokumentenlink.eventYouAreSelected(false);
			} else if (eI.getSource() == panelBottomKostentraeger) {
				Object oKey = panelBottomKostentraeger.getKeyWhenDetailPanel();
				panelQueryKostentraeger.eventYouAreSelected(false);
				panelQueryKostentraeger.setSelectedId(oKey);
				panelSplitKostentraeger.eventYouAreSelected(false);
			} else if (eI.getSource() == panelSpediteurBottomD4) {
				Object oKey = panelSpediteurBottomD4.getKeyWhenDetailPanel();
				panelSpediteurQP4.eventYouAreSelected(false);
				panelSpediteurQP4.setSelectedId(oKey);
				panelSpediteurQP4.eventYouAreSelected(false);
			} else if (eI.getSource() == panelZahlungszielBottomD5) {
				Object oKey = panelZahlungszielBottomD5.getKeyWhenDetailPanel();
				panelZahlungszielQP5.eventYouAreSelected(false);
				panelZahlungszielQP5.setSelectedId(oKey);
				panelZahlungszielQP5.eventYouAreSelected(false);
			} else if (eI.getSource() == panelMwstBottom) {
				Object oKey = panelMwstBottom.getKeyWhenDetailPanel();
				panelMwstQP6.eventYouAreSelected(false);
				panelMwstQP6.setSelectedId(oKey);
				panelMwstQP6.eventYouAreSelected(false);
			} else if (eI.getSource() == panelMwstbezBottom) {
				Object oKey = panelMwstbezBottom.getKeyWhenDetailPanel();
				panelMwstbezQP.eventYouAreSelected(false);
				panelMwstbezQP.setSelectedId(oKey);
				panelMwstbezQP.eventYouAreSelected(false);
			} else if (eI.getSource() == panelKostenstelleBottomD7) {
				Object oKey = panelKostenstelleBottomD7.getKeyWhenDetailPanel();
				panelKostenstelleQP7.eventYouAreSelected(false);
				panelKostenstelleQP7.setSelectedId(oKey);
				panelKostenstelleQP7.eventYouAreSelected(false);
			}
		}

		else if (eI.getID() == ItemChangedEvent.ACTION_DISCARD) {
			if (eI.getSource() == panelMandantkopfdatenD2) {
				String cNr = getInternalFrameSystem().getMandantDto().getCNr();
				refreshMandantkopfdatenD2(cNr);
				panelMandantkopfdatenD2.eventYouAreSelected(false);
			} else if (eI.getSource() == panelMandantKonditionenD3) {
				String cNr = getInternalFrameSystem().getMandantDto().getCNr();
				refreshMandantKonditionenD3(cNr);
				panelMandantKonditionenD3.eventYouAreSelected(false);
			} else if (eI.getSource() == panelMandantVorbelegungen2) {
				String cNr = getInternalFrameSystem().getMandantDto().getCNr();
				refreshMandantVorbelegungen2(cNr);
				panelMandantVorbelegungen2.eventYouAreSelected(false);
			} else if (eI.getSource() == panelSpediteurBottomD4) {
				panelSpediteurSP4.eventYouAreSelected(false);
			} else if (eI.getSource() == panelZahlungszielBottomD5) {
				panelZahlungszielSP5.eventYouAreSelected(false);
			} else if (eI.getSource() == panelMwstBottom) {
				panelMwstSP6.eventYouAreSelected(false);
			} else if (eI.getSource() == panelMwstbezBottom) {
				panelMwstbezSP.eventYouAreSelected(false);
			} else if (eI.getSource() == panelKostenstelleBottomD7) {
				panelKostenstelleSP7.eventYouAreSelected(false);
			} else if (eI.getSource() == panelBottomLieferart) {
				panelSplitLieferart.eventYouAreSelected(false);
			} else if (eI.getSource() == panelBottomDokumentenlink) {
				panelSplitDokumentenlink.eventYouAreSelected(false);
			} else if (eI.getSource() == panelBottomKostentraeger) {
				panelSplitKostentraeger.eventYouAreSelected(false);
			}
		}
	}

	public void setKeyWasForLockMe() {
		Object oKey = panelMandantQP1.getSelectedId();

		if (oKey != null) {
			getInternalFrame().setKeyWasForLockMe(oKey.toString());
		} else {
			getInternalFrame().setKeyWasForLockMe(null);
		}
	}

	public void lPEventObjectChanged(ChangeEvent e) throws Throwable {
		super.lPEventObjectChanged(e);
		int selectedCur = getSelectedIndex();

		if (selectedCur == IDX_PANEL_QP1) {
			refreshMandantQP1();
			panelMandantQP1.eventYouAreSelected(false);

			// im QP die Buttons setzen.
			panelMandantQP1.updateButtons(panelMandantQP1
					.getLockedstateDetailMainKey());
		}

		else if (selectedCur == IDX_PANEL_KOPFDATEN_D2) {
			String cNrMandant = getInternalFrameSystem().getMandantDto()
					.getCNr();
			refreshMandantkopfdatenD2(cNrMandant);
			panelMandantkopfdatenD2.eventYouAreSelected(false);
		}

		else if (selectedCur == IDX_PANEL_VORBELEGUNGEN) {
			String cNrMandant = getInternalFrameSystem().getMandantDto()
					.getCNr();
			refreshMandantKonditionenD3(cNrMandant);
			panelMandantKonditionenD3.eventYouAreSelected(false);
		}

		else if (selectedCur == IDX_PANEL_VORBELEGUNGEN2) {
			String cNrMandant = getInternalFrameSystem().getMandantDto()
					.getCNr();
			refreshMandantVorbelegungen2(cNrMandant);
			panelMandantVorbelegungen2.eventYouAreSelected(false);
		}

		else if (selectedCur == IDX_PANEL_SPEDITEUR_SP4) {
			refreshSpediteurSP4();
			panelSpediteurSP4.eventYouAreSelected(false);

			// wenn Mandanten ungleich werden alle Buttons ausser refresh
			// weggeschalten
			if (LPMain.getInstance().getTheClient().getMandant()
					.equals(this.getMandantCNr())) {

				// im QP die Buttons setzen.
				this.panelSpediteurQP4
						.updateButtons(this.panelSpediteurBottomD4
								.getLockedstateDetailMainKey());
			} else {
				// im QP die Buttons setzen.
				LockStateValue l = new LockStateValue(
						PanelBasis.LOCK_ENABLE_REFRESHANDPRINT_ONLY);
				panelSpediteurQP4.updateButtons(l);

			}

		}

		else if (selectedCur == IDX_PANEL_ZAHLUNGSZIEL_SP5) {
			refreshZahlungszielSP5();
			panelZahlungszielSP5.eventYouAreSelected(false);

			// wenn Mandanten ungleich werden alle Buttons ausser refresh
			// weggeschalten
			if (LPMain.getInstance().getTheClient().getMandant()
					.equals(this.getMandantCNr())) {

				// im QP die Buttons setzen.
				this.panelZahlungszielQP5
						.updateButtons(this.panelZahlungszielBottomD5
								.getLockedstateDetailMainKey());

			} else {
				// im QP die Buttons setzen.
				LockStateValue l = new LockStateValue(
						PanelBasis.LOCK_ENABLE_REFRESHANDPRINT_ONLY);
				panelZahlungszielQP5.updateButtons(l);
			}
		}

		else if (selectedCur == IDX_PANEL_MWST_SP8) {
			refreshMwstSP6();
			panelMwstSP6.eventYouAreSelected(false);

			// wenn Mandanten ungleich werden alle Buttons ausser refresh
			// weggeschalten
			if (LPMain.getInstance().getTheClient().getMandant()
					.equals(this.getMandantCNr())) {
				// im QP die Buttons setzen.
				panelMwstQP6.updateButtons(panelMwstBottom
						.getLockedstateDetailMainKey());
			} else {
				// im QP die Buttons setzen.
				LockStateValue l = new LockStateValue(
						PanelBasis.LOCK_ENABLE_REFRESHANDPRINT_ONLY);
				panelMwstQP6.updateButtons(l);
			}
		} else if (selectedCur == IDX_PANEL_MWSTBEZ_SP9) {
			refreshMwstbezSP9();
			panelMwstbezSP.eventYouAreSelected(false);

			// wenn Mandanten ungleich werden alle Buttons ausser refresh
			// weggeschalten
			if (LPMain.getInstance().getTheClient().getMandant()
					.equals(this.getMandantCNr())) {
				// im QP die Buttons setzen.
				panelMwstbezQP.updateButtons(panelMwstbezBottom
						.getLockedstateDetailMainKey());
			} else {
				// im QP die Buttons setzen.
				LockStateValue l = new LockStateValue(
						PanelBasis.LOCK_ENABLE_REFRESHANDPRINT_ONLY);
				panelMwstbezQP.updateButtons(l);
			}
		}

		else if (selectedCur == IDX_PANEL_KOSTENSTELLE_SP6) {
			refreshKostenstelleSP7();
			panelKostenstelleSP7.eventYouAreSelected(false);

			// wenn Mandanten ungleich werden alle Buttons ausser refresh
			// weggeschalten
			if (LPMain.getInstance().getTheClient().getMandant()
					.equals(this.getMandantCNr())) {
				// im QP die Buttons setzen.
				panelKostenstelleQP7.updateButtons(panelKostenstelleBottomD7
						.getLockedstateDetailMainKey());
			} else {
				// im QP die Buttons setzen.
				LockStateValue l = new LockStateValue(
						PanelBasis.LOCK_ENABLE_REFRESHANDPRINT_ONLY);
				panelKostenstelleQP7.updateButtons(l);
			}
		}

		else if (selectedCur == IDX_PANEL_LIEFERART7) {
			refreshPanelLieferart();
			panelQueryLieferart.eventYouAreSelected(false);

			// // wenn es fuer das tabbed pane noch keinen Eintrag gibt,
			// // die restlichen oberen Laschen deaktivieren, ausser ...
			// if (panelQueryLieferart.getSelectedId() == null) {
			// getInternalFrame().enableAllOberePanelsExceptMe(this,
			// IDX_PANEL_LIEFERART, false);
			// }

			// wenn Mandanten ungleich werden alle Buttons ausser refresh
			// weggeschalten
			if (LPMain.getInstance().getTheClient().getMandant()
					.equals(this.getMandantCNr())) {
				// im QP die Buttons setzen.
				panelQueryLieferart.updateButtons(panelQueryLieferart
						.getLockedstateDetailMainKey());

			} else {
				// im QP die Buttons setzen.
				LockStateValue l = new LockStateValue(
						PanelBasis.LOCK_ENABLE_REFRESHANDPRINT_ONLY);
				panelQueryLieferart.updateButtons(l);
			}
		} else if (selectedCur == IDX_PANEL_DOKUMENTENLINK) {
			refreshPanelDokumentenlink();
			panelQueryDokumentenlink.eventYouAreSelected(false);

			// wenn Mandanten ungleich werden alle Buttons ausser refresh
			// weggeschalten
			if (LPMain.getInstance().getTheClient().getMandant()
					.equals(this.getMandantCNr())) {
				// im QP die Buttons setzen.
				panelQueryDokumentenlink.updateButtons(panelQueryDokumentenlink
						.getLockedstateDetailMainKey());

			} else {
				// im QP die Buttons setzen.
				LockStateValue l = new LockStateValue(
						PanelBasis.LOCK_ENABLE_REFRESHANDPRINT_ONLY);
				panelQueryDokumentenlink.updateButtons(l);
			}
		} else if (selectedCur == IDX_PANEL_KOSTENTRAEGER) {
			refreshPanelKostentraeger();
			panelQueryKostentraeger.eventYouAreSelected(false);

			// wenn Mandanten ungleich werden alle Buttons ausser refresh
			// weggeschalten
			if (LPMain.getInstance().getTheClient().getMandant()
					.equals(this.getMandantCNr())) {
				// im QP die Buttons setzen.
				panelQueryKostentraeger.updateButtons(panelQueryKostentraeger
						.getLockedstateDetailMainKey());

			} else {
				// im QP die Buttons setzen.
				LockStateValue l = new LockStateValue(
						PanelBasis.LOCK_ENABLE_REFRESHANDPRINT_ONLY);
				panelQueryKostentraeger.updateButtons(l);
			}
		}
	}

	private void refreshPanelLieferart() throws Throwable {
		if (panelSplitLieferart == null) {
			String[] aWhichStandardButtonIUse = { PanelBasis.ACTION_NEW };

			panelQueryLieferart = new PanelQuery(null, null,
					QueryParameters.UC_ID_LIEFERART, aWhichStandardButtonIUse,
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("label.lieferart"), true);

			panelBottomLieferart = new PanelSCRUDMandant(
					getInternalFrame(),
					LPMain.getInstance().getTextRespectUISPr("label.lieferart"),
					null, HelperClient.SCRUD_LIEFERART_FILE,
					getInternalFrame(), HelperClient.LOCKME_LIEFERART);

			panelSplitLieferart = new PanelSplit(getInternalFrame(),
					panelBottomLieferart, panelQueryLieferart,
					400 - ((PanelStammdatenCRUD) panelBottomLieferart)
							.getAnzahlControls() * 30);
			setComponentAt(IDX_PANEL_LIEFERART7, panelSplitLieferart);

			panelQueryLieferart
					.befuellePanelFilterkriterienDirektUndVersteckte(
							SystemFilterFactory.getInstance()
									.createFKDKennung(), null,
							SystemFilterFactory.getInstance()
									.createFKVLieferart());

		}
		// filter refreshen
		panelQueryLieferart.setDefaultFilter(SystemFilterFactory.getInstance()
				.createFKMandantCNr(getMandantCNr()));

		// liste soll sofort angezeigt werden
		panelQueryLieferart.eventYouAreSelected(true);
	}

	private void refreshPanelDokumentenlink() throws Throwable {
		if (panelSplitDokumentenlink == null) {
			String[] aWhichStandardButtonIUse = { PanelBasis.ACTION_NEW };

			panelQueryDokumentenlink = new PanelQuery(null, null,
					QueryParameters.UC_ID_DOKUMENTENLINK,
					aWhichStandardButtonIUse, getInternalFrame(), LPMain
							.getInstance().getTextRespectUISPr(
									"system.dokumentenlink"), true);

			panelBottomDokumentenlink = new PanelDokumentenlink(
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("system.dokumentenlink"), null);

			panelSplitDokumentenlink = new PanelSplit(getInternalFrame(),
					panelBottomDokumentenlink, panelQueryDokumentenlink, 270);
			setComponentAt(IDX_PANEL_DOKUMENTENLINK, panelSplitDokumentenlink);
		}
		// filter refreshen
		panelQueryDokumentenlink.setDefaultFilter(SystemFilterFactory
				.getInstance().createFKMandantCNr(getMandantCNr()));

		// liste soll sofort angezeigt werden
		panelQueryDokumentenlink.eventYouAreSelected(true);
	}

	private void refreshPanelKostentraeger() throws Throwable {
		if (panelSplitKostentraeger == null) {
			String[] aWhichStandardButtonIUse = { PanelBasis.ACTION_NEW };

			panelQueryKostentraeger = new PanelQuery(null, null,
					QueryParameters.UC_ID_KOSTENTRAEGER,
					aWhichStandardButtonIUse, getInternalFrame(), LPMain
							.getInstance().getTextRespectUISPr(
									"lp.kostentraeger"), true);

			panelBottomKostentraeger = new PanelKostentraeger(
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("lp.kostentraeger"), null);

			panelSplitKostentraeger = new PanelSplit(getInternalFrame(),
					panelBottomKostentraeger, panelQueryKostentraeger, 270);
			setComponentAt(IDX_PANEL_KOSTENTRAEGER, panelSplitKostentraeger);
		}
		// filter refreshen
		panelQueryKostentraeger.setDefaultFilter(SystemFilterFactory
				.getInstance().createFKMandantCNr(getMandantCNr()));

		// liste soll sofort angezeigt werden
		panelQueryKostentraeger.eventYouAreSelected(true);
	}

	private void refreshMwstSP6() throws Throwable {
		if (panelMwstSP6 == null) {

			// Buttons.
			String[] aButton = { PanelBasis.ACTION_NEW };
			panelMwstQP6 = new PanelQuery(null, SystemFilterFactory
					.getInstance().createFKMandantCNr(),
					QueryParameters.UC_ID_MWSTSATZ, aButton,
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("lp.mwst"), true); // liste
			// refresh
			// wenn
			// lasche
			// geklickt
			// wurde

			panelMwstBottom = new PanelMehrwertsteuer(getInternalFrame(),
					LPMain.getInstance().getTextRespectUISPr("lp.mwst"), null);

			panelMwstSP6 = new PanelSplit(getInternalFrame(), panelMwstBottom,
					panelMwstQP6, 200);
			setComponentAt(IDX_PANEL_MWST_SP8, panelMwstSP6);
		}
		// filter refreshen
		// panelMwstQP6.setDefaultFilter(SystemFilterFactory.getInstance().
		// createFKMandantCNr(getMandantCNr()));

		// liste soll sofort angezeigt werden
		panelMwstQP6.eventYouAreSelected(true);
	}

	private void refreshMwstbezSP9() throws Throwable {
		if (panelMwstbezSP == null) {

			// Buttons.
			String[] aButton = { PanelBasis.ACTION_NEW };
			panelMwstbezQP = new PanelQuery(null, SystemFilterFactory
					.getInstance().createFKMandantCNr(),
					QueryParameters.UC_ID_MWSTSATZBEZ, aButton,
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("lp.mwst"), true); // liste
			// refresh
			// wenn
			// lasche
			// geklickt
			// wurde

			panelMwstbezBottom = new PanelMwstsatzbez(getInternalFrame(),
					LPMain.getInstance().getTextRespectUISPr("lp.mwst"), null);

			panelMwstbezSP = new PanelSplit(getInternalFrame(),
					panelMwstbezBottom, panelMwstbezQP, 200);
			setComponentAt(IDX_PANEL_MWSTBEZ_SP9, panelMwstbezSP);
		}
		// filter refreshen
		// panelMwstQP6.setDefaultFilter(SystemFilterFactory.getInstance().
		// createFKMandantCNr(getMandantCNr()));

		// liste soll sofort angezeigt werden
		panelMwstbezQP.eventYouAreSelected(true);
	}

	private void refreshKostenstelleSP7() throws Throwable {
		if (panelKostenstelleSP7 == null) {
			// Buttons.
			String[] aButton = { PanelBasis.ACTION_NEW };
			panelKostenstelleQP7 = new PanelQuery(null, SystemFilterFactory
					.getInstance().createFKMandantCNr(),
					QueryParameters.UC_ID_KOSTENSTELLE, aButton,
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("label.kostenstelle"), true); // liste
			// refresh
			// wenn
			// lasche
			// geklickt
			// wurde

			panelKostenstelleBottomD7 = new PanelKostenstelle(
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("label.kostenstelle"), null);

			panelKostenstelleQP7
					.befuellePanelFilterkriterienDirektUndVersteckte(
							SystemFilterFactory.getInstance()
									.createFKDKennung(), SystemFilterFactory
									.getInstance().createFKDBezeichnung(),
							SystemFilterFactory.getInstance()
									.createFKVKostenstelle());

			panelKostenstelleSP7 = new PanelSplit(getInternalFrame(),
					panelKostenstelleBottomD7, panelKostenstelleQP7, 200);
			setComponentAt(IDX_PANEL_KOSTENSTELLE_SP6, panelKostenstelleSP7);
		}

		// filter refreshen
		panelKostenstelleQP7.setDefaultFilter(SystemFilterFactory.getInstance()
				.createFKMandantCNr(getMandantCNr()));

		// liste soll sofort angezeigt werden
		panelKostenstelleQP7.eventYouAreSelected(true);
	}

	protected void lPActionEvent(java.awt.event.ActionEvent e) {
		// nothing here.
	}

	private void refreshMandantQP1() throws Throwable {

		if (panelMandantQP1 == null) {

			String[] aWhichStandardButtonIUse = null;
			if (LPMain.getInstance().isLPAdmin() || Helper.isAbbot(LPMain.getTheClient())) {
				aWhichStandardButtonIUse = new String[1];
				aWhichStandardButtonIUse[0] = PanelBasis.ACTION_NEW;
			}

			QueryType[] querytypes = null;

			FilterKriterium[] filters = null;

			panelMandantQP1 = new PanelQuery(querytypes, filters,
					QueryParameters.UC_ID_MANDANT, aWhichStandardButtonIUse,
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("lp.uebersicht.detail"), true);

			setComponentAt(IDX_PANEL_QP1, panelMandantQP1);
		}
	}

	private void refreshMandantkopfdatenD2(String cNrMandantI) throws Throwable {
		if (panelMandantkopfdatenD2 == null) {
			panelMandantkopfdatenD2 = new PanelMandantkopfdaten(
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("lp.kopfdaten"), cNrMandantI);
			setComponentAt(IDX_PANEL_KOPFDATEN_D2, panelMandantkopfdatenD2);
		}
	}

	private void refreshMandantKonditionenD3(String cNrMandantI)
			throws Throwable {
		if (panelMandantKonditionenD3 == null) {
			panelMandantKonditionenD3 = new PanelMandantKonditionen(
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("sys.panel.vorbelegungen"),
					cNrMandantI);
			setComponentAt(IDX_PANEL_VORBELEGUNGEN, panelMandantKonditionenD3);
		}
	}

	private void refreshMandantVorbelegungen2(String cNrMandantI)
			throws Throwable {
		if (panelMandantVorbelegungen2 == null) {
			panelMandantVorbelegungen2 = new PanelMandantVorbelegungen2(
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("sys.panel.vorbelegungen")
							+ " 2", cNrMandantI);
			setComponentAt(IDX_PANEL_VORBELEGUNGEN2, panelMandantVorbelegungen2);
		}
	}

	private void refreshSpediteurSP4() throws Throwable {

		if (panelSpediteurSP4 == null) {

			// Buttons.
			String[] aButton = { PanelBasis.ACTION_NEW };
			panelSpediteurQP4 = new PanelQuery(null, SystemFilterFactory
					.getInstance().createFKMandantCNr(),
					QueryParameters.UC_ID_SPEDITEUR, aButton,
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("lp.spediteur"), true); // liste
			// refresh
			// wenn
			// lasche
			// geklickt
			// wurde

			panelSpediteurBottomD4 = new PanelSpediteur(getInternalFrame(),
					LPMain.getInstance().getTextRespectUISPr("lp.spediteur"),
					null);

			panelSpediteurSP4 = new PanelSplit(getInternalFrame(),
					panelSpediteurBottomD4, panelSpediteurQP4, 200);

			panelSpediteurQP4.befuellePanelFilterkriterienDirektUndVersteckte(
					null, null, SystemFilterFactory.getInstance()
							.createFKVSpediteur());

			setComponentAt(IDX_PANEL_SPEDITEUR_SP4, panelSpediteurSP4);

		}
		// filter refreshen.
		panelSpediteurQP4.setDefaultFilter(SystemFilterFactory.getInstance()
				.createFKMandantCNr(getMandantCNr()));

		// liste soll sofort angezeigt werden
		panelSpediteurQP4.eventYouAreSelected(true);
	}

	private void refreshZahlungszielSP5() throws Throwable {

		if (panelZahlungszielSP5 == null) {

			// Buttons.
			String[] aButton = { PanelBasis.ACTION_NEW };
			panelZahlungszielQP5 = new PanelQuery(null, SystemFilterFactory
					.getInstance().createFKMandantCNr(),
					QueryParameters.UC_ID_ZAHLUNGSZIEL, aButton,
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("label.zahlungsziel"), true); // liste
			// refresh
			// wenn
			// lasche
			// geklickt
			// wurde

			panelZahlungszielBottomD5 = new PanelZahlungsziel(
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("label.zahlungsziel"), null);

			panelZahlungszielSP5 = new PanelSplit(getInternalFrame(),
					panelZahlungszielBottomD5, panelZahlungszielQP5, 180);
			panelZahlungszielQP5
					.befuellePanelFilterkriterienDirektUndVersteckte(
							SystemFilterFactory.getInstance()
									.createFKDBezeichnung(), null,
							SystemFilterFactory.getInstance()
									.createFKVZahlungsziel());

			setComponentAt(IDX_PANEL_ZAHLUNGSZIEL_SP5, panelZahlungszielSP5);
		}

		// filter refreshen.
		panelZahlungszielQP5.setDefaultFilter(SystemFilterFactory.getInstance()
				.createFKMandantCNr(getMandantCNr()));

		// liste soll sofort angezeigt werden
		panelZahlungszielQP5.eventYouAreSelected(true);

	}

	protected PartnerDto getPartnerDto() {
		return getInternalFrameSystem().getMandantDto().getPartnerDto();
	}

	/**
	 * getInternalframeSystem
	 * 
	 * @return InternalFrameSystem
	 */
	private InternalFrameSystem getInternalFrameSystem() {
		return (InternalFrameSystem) getInternalFrame();
	}

	protected void setPartnerDto(PartnerDto partnerDto) {
		getInternalFrameSystem().getMandantDto().setPartnerDto(partnerDto);
	}

	protected javax.swing.JMenuBar getJMenuBar() throws Throwable {
		return new WrapperMenuBar(this);
	}
}
