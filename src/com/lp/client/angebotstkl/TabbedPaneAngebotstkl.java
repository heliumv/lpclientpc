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
package com.lp.client.angebotstkl;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.math.BigDecimal;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import javax.swing.event.ChangeEvent;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.ICopyPaste;
import com.lp.client.frame.LockStateValue;
import com.lp.client.frame.assistent.view.AssistentView;
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
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.stueckliste.StuecklisteFilterFactory;
import com.lp.client.stueckliste.importassistent.StklImportController;
import com.lp.client.system.SystemFilterFactory;
import com.lp.client.util.fastlanereader.gui.QueryType;
import com.lp.server.angebotstkl.service.AgstklarbeitsplanDto;
import com.lp.server.angebotstkl.service.AgstklpositionDto;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.LagerDto;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MandantFac;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.service.BelegpositionDto;
import com.lp.service.POSDocument2POSDto;
import com.lp.service.StklImportSpezifikation;
import com.lp.util.Helper;

@SuppressWarnings("static-access")
public class TabbedPaneAngebotstkl extends TabbedPane implements ICopyPaste {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private PanelQuery panelQueryAgstkl = null;
	private PanelAngebotstklKopfdaten panelDetailAgstkl = null;
	private PanelBasis panelDetailAgstklAufschlaege = null;

	private PanelQuery panelQueryAngStklPos = null;
	private PanelBasis panelDetailAngStklPos = null;
	private PanelSplit panelSplitAngStk = null; // FLR 1:n Liste

	private PanelQuery panelQueryArbeitsplan = null;
	private PanelBasis panelDetailArbeitsplan = null;
	private PanelSplit panelSplitArbeitsplan = null; // FLR 1:n Liste

	private PanelQuery panelQueryMengenstaffel = null;
	private PanelBasis panelDetailMengenstaffel = null;
	private PanelSplit panelSplitMengenstaffel = null; // FLR 1:n Liste

	public static int IDX_PANEL_AUSWAHL = -1;
	private static int IDX_PANEL_KOPFDATEN = -1;
	private static int IDX_PANEL_POSITIONEN = -1;
	private static int IDX_PANEL_ARBEITSPLAN = -1;
	private static int IDX_PANEL_MENGENSTAFFEL = -1;
	private static int IDX_PANEL_AUFSCHLAEGE = -1;

	private PanelQueryFLR panelStueckliste = null;

	private PanelQueryFLR panelAgstkl = null;

	private static final String ACTION_SPECIAL_IMPORT = "action_special_import";
	private static final String ACTION_SPECIAL_IMPORT_STKL = "action_special_import_stkl";
	private static final String ACTION_SPECIAL_IMPORT_STKL_ARBEITSPLAN = "action_special_import_stkl_arbeitsplan";
	private static final String ACTION_SPECIAL_PREISE_NEU_RECHNEN_KALK = "action_special_preise_neu_rechnen";
	private static final String ACTION_SPECIAL_INTELLIGENTERCSVIMPORT_POSITIONEN = "ACTION_SPECIAL_INTELLIGENTERCSVIMPORT_POSITIONEN";

	private final String BUTTON_IMPORTAGSTUECKLISTEPOSITIONEN = PanelBasis.ACTION_MY_OWN_NEW
			+ ACTION_SPECIAL_IMPORT;

	private static final String ACTION_SPECIAL_IMPORT_ARBEITSPLAN = "action_special_import_arbeitsplan";
	private final String BUTTON_IMPORTAGSTUECKLISTEARBEITSPLAN = PanelBasis.ACTION_MY_OWN_NEW
			+ ACTION_SPECIAL_IMPORT_ARBEITSPLAN;

	private final String BUTTON_IMPORTSTUECKLISTEPOSITIONEN = PanelBasis.ACTION_MY_OWN_NEW
			+ ACTION_SPECIAL_IMPORT_STKL;

	private final String BUTTON_IMPORTSTUECKLISTEARBEITSLPAN = PanelBasis.ACTION_MY_OWN_NEW
			+ ACTION_SPECIAL_IMPORT_STKL_ARBEITSPLAN;

	private final String BUTTON_PREISE_NEU_RECHNEN = PanelBasis.ACTION_MY_OWN_NEW
			+ ACTION_SPECIAL_PREISE_NEU_RECHNEN_KALK;

	private final String BUTTON_INTELLIGENTERIMPORTCSV_AGSTKLPOSITIONEN = PanelBasis.ACTION_MY_OWN_NEW
			+ ACTION_SPECIAL_INTELLIGENTERCSVIMPORT_POSITIONEN;

	private final String MENU_DATEI_DRUCKEN = "MENU_DATEI_DRUCKEN";

	private WrapperMenuBar wrapperMenuBar = null;

	private boolean bPositionen = true;

	public TabbedPaneAngebotstkl(InternalFrame internalFrameI) throws Throwable {

		super(internalFrameI, LPMain.getInstance().getTextRespectUISPr(
				"lp.angebotsstueckliste"));

		jbInit();
		initComponents();
	}

	public PanelQuery getPanelQueryPersonal() {
		return panelQueryAgstkl;
	}

	public PanelBasis getAngebotstklPositionenBottom() {
		return panelDetailAngStklPos;
	}

	private void dialogAgstkl(ItemChangedEvent e) throws Throwable {
		panelAgstkl = AngebotstklFilterFactory.getInstance()
				.createPanelFLRAgstkl(getInternalFrame(), true, false);
		new DialogQuery(panelAgstkl);
	}

	public PanelQuery getAngebotstklPositionenTop() {
		return panelQueryAngStklPos;
	}

	public PanelQuery getAngebotstklArbeitsplanTop() {
		return panelQueryArbeitsplan;
	}

	private void refreshAuswahl() throws Throwable {
		if (panelQueryAgstkl == null) {

			String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW,
					PanelBasis.ACTION_FILTER };
			panelQueryAgstkl = new PanelQuery(AngebotstklFilterFactory
					.getInstance().createQTAgstkl(), AngebotstklFilterFactory
					.getInstance().createFKAgstklMandantCNr(),
					QueryParameters.UC_ID_AGSTKL, aWhichButtonIUse,
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("auft.title.panel.auswahl"),
					true);

			panelQueryAgstkl
					.befuellePanelFilterkriterienDirektUndVersteckte(
							AngebotstklFilterFactory.getInstance()
									.createFKDAgstklbelegnumer(),
							AngebotstklFilterFactory.getInstance()
									.createFKDKundeName(), null);

			panelQueryAgstkl.addDirektFilter(AngebotstklFilterFactory
					.getInstance().createFKDAgstklprojekt());

			panelQueryAgstkl.addDirektFilter(AngebotstklFilterFactory
					.getInstance().createFKDAgstklAngebote());

			setComponentAt(IDX_PANEL_AUSWAHL, panelQueryAgstkl);
		}
	}

	private void createKopfdaten(Integer key) throws Throwable {
		if (panelDetailAgstkl == null) {
			panelDetailAgstkl = new PanelAngebotstklKopfdaten(
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("lp.kopfdaten"), key);
			setComponentAt(IDX_PANEL_KOPFDATEN, panelDetailAgstkl);
		}
	}

	private void createAufschlaege(Integer key) throws Throwable {
		if (panelDetailAgstklAufschlaege != null) {
			panelDetailAgstklAufschlaege = null;
		}
		panelDetailAgstklAufschlaege = new PanelAngebotstklAufschlaege(
				getInternalFrame(), LPMain.getInstance().getTextRespectUISPr(
						"as.aufschlag"), key);
		setComponentAt(IDX_PANEL_AUFSCHLAEGE, panelDetailAgstklAufschlaege);

	}

	private PanelBasis getAngebotstklKopfdaten() throws Throwable {
		Integer iIdAngebot = null;

		if (panelDetailAgstkl == null) {

			// Die Angebot hat einen Key vom Typ Integer
			if (getInternalFrame().getKeyWasForLockMe() != null) {
				iIdAngebot = new Integer(Integer.parseInt(getInternalFrame()
						.getKeyWasForLockMe()));
			}

			panelDetailAgstkl = new PanelAngebotstklKopfdaten(
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("angb.panel.kopfdaten"),
					iIdAngebot); // empty bei leerer angebotsliste

			setComponentAt(IDX_PANEL_KOPFDATEN, panelDetailAgstkl);
		}

		return panelDetailAgstkl;
	}

	public void printAngebotstkl() throws Throwable {
		if (!getAngebotstklKopfdaten().isLockedDlg()) {
			if (getInternalFrameAngebotstkl().getAgstklDto() != null) {

				// das Angebot drucken

				if (LPMain
						.getInstance()
						.getDesktop()
						.darfAnwenderAufZusatzfunktionZugreifen(
								MandantFac.ZUSATZFUNKTION_AGSTKL_ARBEITSPLAN)) {

					getInternalFrame().showReportKriterien(
							new ReportAngebotstklmengenstaffel(
									getInternalFrame(),
									getInternalFrameAngebotstkl()
											.getAgstklDto().getIId(),
									getInternalFrameAngebotstkl()
											.getAgstklDto().getCNr()));
				} else {
					getInternalFrame().showReportKriterien(
							new ReportAngebotstkl(getInternalFrame(),
									getInternalFrameAngebotstkl()
											.getAgstklDto().getIId(),
									getInternalFrameAngebotstkl()
											.getAgstklDto().getCNr()));
				}

			} else {
				// es ist keine Angbotsstueckliste ausgewaehlt
				DialogFactory.showModalDialog(LPMain.getInstance()
						.getTextRespectUISPr("lp.error"), LPMain.getInstance()
						.getTextRespectUISPr("as.warning.keineas"));
			}
		}
	}

	/**
	 * jbInit
	 * 
	 * @throws Throwable
	 */
	private void jbInit() throws Throwable {

		int tabIndex = 0;
		IDX_PANEL_AUSWAHL = tabIndex;

		insertTab(LPMain.getInstance().getTextRespectUISPr("lp.auswahl"), null,
				null, LPMain.getInstance().getTextRespectUISPr("lp.auswahl"),
				IDX_PANEL_AUSWAHL);

		tabIndex++;
		IDX_PANEL_KOPFDATEN = tabIndex;

		insertTab(LPMain.getInstance().getTextRespectUISPr("lp.kopfdaten"),
				null, null,
				LPMain.getInstance().getTextRespectUISPr("lp.kopfdaten"),
				IDX_PANEL_KOPFDATEN);
		tabIndex++;
		IDX_PANEL_POSITIONEN = tabIndex;
		insertTab(LPMain.getInstance().getTextRespectUISPr("lp.positionen"),
				null, null,
				LPMain.getInstance().getTextRespectUISPr("lp.positionen"),
				IDX_PANEL_POSITIONEN);

		if (LPMain
				.getInstance()
				.getDesktop()
				.darfAnwenderAufZusatzfunktionZugreifen(
						MandantFac.ZUSATZFUNKTION_AGSTKL_ARBEITSPLAN)) {
			tabIndex++;
			IDX_PANEL_ARBEITSPLAN = tabIndex;
			insertTab(
					LPMain.getInstance().getTextRespectUISPr(
							"as.agstkl.arbeitsplan"),
					null,
					null,
					LPMain.getInstance().getTextRespectUISPr(
							"as.agstkl.arbeitsplan"), IDX_PANEL_ARBEITSPLAN);

			tabIndex++;
			IDX_PANEL_MENGENSTAFFEL = tabIndex;
			insertTab(
					LPMain.getInstance().getTextRespectUISPr(
							"as.agstkl.mengenstaffel"),
					null,
					null,
					LPMain.getInstance().getTextRespectUISPr(
							"as.agstkl.mengenstaffel"), IDX_PANEL_MENGENSTAFFEL);

		}

		if (getInternalFrameAngebotstkl().iKalkulationsart == 2
				|| getInternalFrameAngebotstkl().iKalkulationsart == 3) {
			if (!LPMain
					.getInstance()
					.getDesktop()
					.darfAnwenderAufZusatzfunktionZugreifen(
							MandantFac.ZUSATZFUNKTION_AGSTKL_ARBEITSPLAN)) {
				tabIndex++;
				IDX_PANEL_AUFSCHLAEGE = tabIndex;

				insertTab(
						LPMain.getInstance()
								.getTextRespectUISPr("as.aufschlag"), null,
						null,
						LPMain.getInstance()
								.getTextRespectUISPr("as.aufschlag"),
						IDX_PANEL_AUFSCHLAEGE);
			}
		}

		refreshAuswahl();

		panelQueryAgstkl.eventYouAreSelected(false);

		if ((Integer) panelQueryAgstkl.getSelectedId() != null) {
			getInternalFrameAngebotstkl()
					.setAgstklDto(
							DelegateFactory
									.getInstance()
									.getAngebotstklDelegate()
									.agstklFindByPrimaryKey(
											(Integer) panelQueryAgstkl
													.getSelectedId()));
		}

		if ((Integer) panelQueryAgstkl.getSelectedId() != null) {
			getInternalFrameAngebotstkl()
					.setAgstklDto(
							DelegateFactory
									.getInstance()
									.getAngebotstklDelegate()
									.agstklFindByPrimaryKey(
											(Integer) panelQueryAgstkl
													.getSelectedId()));
		}

		// damit D2 einen aktuellen hat.
		ItemChangedEvent it = new ItemChangedEvent(panelQueryAgstkl,
				ItemChangedEvent.ITEM_CHANGED);
		lPEventItemChanged(it);

		this.addChangeListener(this);
		this.getInternalFrame().addItemChangedListener(this);

	}

	public void setKeyWasForLockMe() {
		Object oKey = panelQueryAgstkl.getSelectedId();

		if (oKey != null) {
			getInternalFrame().setKeyWasForLockMe(oKey.toString());
		} else {
			getInternalFrame().setKeyWasForLockMe(null);
		}
	}

	public void refreshTitle() throws Throwable {

		getInternalFrame().setLpTitle(
				InternalFrame.TITLE_IDX_OHRWASCHLUNTEN,
				LPMain.getInstance().getTextRespectUISPr(
						"lp.angebotsstueckliste"));
		getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_OHRWASCHLOBEN,
				((PanelBasis) this.getSelectedComponent()).getAdd2Title());

		if (getInternalFrameAngebotstkl().getAgstklDto() != null) {
			String sBezeichnung = "";

			if (getInternalFrameAngebotstkl().getAgstklDto().getKundeIId() != null) {

				KundeDto kundeDto = DelegateFactory
						.getInstance()
						.getKundeDelegate()
						.kundeFindByPrimaryKey(
								getInternalFrameAngebotstkl().getAgstklDto()
										.getKundeIId());

				sBezeichnung = kundeDto.getPartnerDto()
						.formatFixTitelName1Name2();
			}

			if (getInternalFrameAngebotstkl().getAgstklDto().getCBez() != null) {
				sBezeichnung += " "
						+ getInternalFrameAngebotstkl().getAgstklDto()
								.getCBez();
			}
			getInternalFrame().setLpTitle(
					InternalFrame.TITLE_IDX_AS_I_LIKE,
					getInternalFrameAngebotstkl().getAgstklDto().getCNr()
							+ ", " + sBezeichnung);
		}
	}

	public void erstelleAgstklAusProjekt(Integer projektIId) throws Throwable {

		ItemChangedEvent e = new ItemChangedEvent(this, -99);

		panelQueryAgstkl.eventActionNew(e, true, false);
		panelDetailAgstkl.eventYouAreSelected(false);
		// Nun noch Kunde/Ansprechpartner/Vertreter/Projekt/ProjektBezeichnung
		// setzen
		panelDetailAgstkl.setDefaultsAusProjekt(projektIId);
	}

	public InternalFrameAngebotstkl getInternalFrameAngebotstkl() {
		return (InternalFrameAngebotstkl) getInternalFrame();
	}

	protected void lPActionEvent(ActionEvent e) throws Throwable {
		if (e.getActionCommand().equals(MENU_DATEI_DRUCKEN)) {
			printAngebotstkl();
		}

	}

	public void lPEventItemChanged(ItemChangedEvent eI) throws Throwable {

		if (eI.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			if (eI.getSource() == panelQueryAgstkl) {
				refreshAngebotstklPositionen();
				setSelectedComponent(panelSplitAngStk);
				// panelDetailAgstkl.eventYouAreSelected(false);
			} else if (eI.getSource() == panelAgstkl) {

				if (bPositionen == true) {

					DelegateFactory
							.getInstance()
							.getAngebotstklpositionDelegate()
							.kopiereAgstklPositionen(
									(Integer) panelAgstkl.getSelectedId(),
									getInternalFrameAngebotstkl()
											.getAgstklDto().getIId());
					panelSplitAngStk.eventYouAreSelected(false);
				} else {
					DelegateFactory
							.getInstance()
							.getAngebotstklDelegate()
							.kopiereAgstklArbeitsplan(
									(Integer) panelAgstkl.getSelectedId(),
									getInternalFrameAngebotstkl()
											.getAgstklDto().getIId());
					panelSplitArbeitsplan.eventYouAreSelected(false);
				}
			} else if (eI.getSource() == panelStueckliste) {

				if (bPositionen == true) {

					DelegateFactory
							.getInstance()
							.getAngebotstklDelegate()
							.kopierePositionenAusStueckliste(
									(Integer) panelStueckliste.getSelectedId(),
									getInternalFrameAngebotstkl()
											.getAgstklDto().getIId());
					// PJ18725
					if (!LPMain
							.getInstance()
							.getDesktop()
							.darfAnwenderAufZusatzfunktionZugreifen(
									MandantFac.ZUSATZFUNKTION_AGSTKL_ARBEITSPLAN)) {
						DelegateFactory
								.getInstance()
								.getAngebotstklDelegate()
								.kopiereArbeitsplanAusStuecklisteInPositionen(
										(Integer) panelStueckliste
												.getSelectedId(),
										getInternalFrameAngebotstkl()
												.getAgstklDto().getIId());
					}

					panelSplitAngStk.eventYouAreSelected(false);
				} else {
					DelegateFactory
							.getInstance()
							.getAngebotstklDelegate()
							.kopiereArbeitsplanAusStuecklisteInArbeitsplan(
									(Integer) panelStueckliste.getSelectedId(),
									getInternalFrameAngebotstkl()
											.getAgstklDto().getIId());
					panelSplitArbeitsplan.eventYouAreSelected(false);
				}
			}

		} else if (eI.getID() == ItemChangedEvent.ACTION_MY_OWN_NEW) {
			String sAspectInfo = ((ISourceEvent) eI.getSource()).getAspect();
			if (sAspectInfo.endsWith(ACTION_SPECIAL_IMPORT)) {
				bPositionen = true;
				dialogAgstkl(eI);
			} else if (sAspectInfo.endsWith(ACTION_SPECIAL_IMPORT_ARBEITSPLAN)) {
				bPositionen = false;
				dialogAgstkl(eI);
			} else if (sAspectInfo
					.endsWith(ACTION_SPECIAL_IMPORT_STKL_ARBEITSPLAN)) {
				bPositionen = false;
				dialogStueckliste(eI);
			} else if (sAspectInfo.endsWith(ACTION_SPECIAL_IMPORT_STKL)) {
				bPositionen = true;
				dialogStueckliste(eI);
			} else if (sAspectInfo
					.endsWith(ACTION_SPECIAL_PREISE_NEU_RECHNEN_KALK)) {

				boolean b = DialogFactory.showModalJaNeinDialog(
						getInternalFrame(),
						LPMain.getInstance().getTextRespectUISPr(
								"agstkl.warnung.neuberechnen"));

				if (b == true) {
					DelegateFactory
							.getInstance()
							.getAngebotstklpositionDelegate()
							.preiseGemaessKalkulationsartUpdaten(
									getInternalFrameAngebotstkl()
											.getAgstklDto().getIId());
					panelQueryAngStklPos.eventYouAreSelected(false);
				}
			} else if (sAspectInfo.equals(PanelBasis.ACTION_EINFUEGEN_LIKE_NEW)) {
				if (eI.getSource().equals(panelQueryAngStklPos)) {
					einfuegenHV();
				} else if (eI.getSource().equals(panelQueryArbeitsplan)) {
					einfuegenHV();
				}
			} else if (LPMain
					.getInstance()
					.getDesktop()
					.darfAnwenderAufZusatzfunktionZugreifen(
							MandantFac.ZUSATZFUNKTION_INTELLIGENTER_STUECKLISTENIMPORT)
					&& sAspectInfo
							.endsWith(ACTION_SPECIAL_INTELLIGENTERCSVIMPORT_POSITIONEN)) {

				if (LPMain
						.getInstance()
						.getDesktop()
						.darfAnwenderAufZusatzfunktionZugreifen(
								MandantFac.ZUSATZFUNKTION_KUNDESONDERKONDITIONEN)) {
					AssistentView av = new AssistentView(
							getInternalFrameAngebotstkl(),
							LPMain.getTextRespectUISPr("stkl.intelligenterstklimport"),
							new StklImportController(
									getInternalFrameAngebotstkl()
											.getAgstklDto().getIId(),
									StklImportSpezifikation.SpezifikationsTyp.ANGEBOTSSTKL_SPEZ,
									getInternalFrame()));
					getInternalFrame().showPanelDialog(av);
				} else {
					DialogFactory
							.showModalDialog(
									LPMain.getTextRespectUISPr("lp.hint"),
									LPMain.getTextRespectUISPr("stkl.error.keinekundesokoberechtigung"));
				}
			}
		}

		else if (eI.getID() == ItemChangedEvent.ACTION_SAVE) {
			if (eI.getSource() == panelDetailAgstkl) {
				panelQueryAgstkl.clearDirektFilter();
				Object oKey = panelDetailAgstkl.getKeyWhenDetailPanel();

				panelQueryAgstkl.setSelectedId(oKey);
			} else if (eI.getSource() == panelDetailAngStklPos) {
				// den Key des Datensatzes merken
				Object oKey = panelDetailAngStklPos.getKeyWhenDetailPanel();

				// wenn der neue Datensatz wirklich angelegt wurde (Abbruch
				// moeglich durch Pruefung auf Unterpreisigkeit)
				if (!oKey.equals(LPMain.getLockMeForNew())) {
					// die Liste neu aufbauen
					panelQueryAngStklPos.eventYouAreSelected(false);

					// den Datensatz in der Liste selektieren
					panelQueryAngStklPos.setSelectedId(oKey);
				}

				// im Detail den selektierten anzeigen
				panelSplitAngStk.eventYouAreSelected(false);
			} else if (eI.getSource() == panelDetailArbeitsplan) {
				Object oKey = panelDetailArbeitsplan.getKeyWhenDetailPanel();
				if (!oKey.equals(LPMain.getLockMeForNew())) {
					panelQueryArbeitsplan.eventYouAreSelected(false);
					panelQueryArbeitsplan.setSelectedId(oKey);
				}
				panelSplitArbeitsplan.eventYouAreSelected(false);
			} else if (eI.getSource() == panelDetailMengenstaffel) {
				Object oKey = panelDetailMengenstaffel.getKeyWhenDetailPanel();
				if (!oKey.equals(LPMain.getLockMeForNew())) {
					panelQueryMengenstaffel.eventYouAreSelected(false);
					panelQueryMengenstaffel.setSelectedId(oKey);
				}
				panelSplitMengenstaffel.eventYouAreSelected(false);
			}
		}
		if (eI.getID() == ItemChangedEvent.ACTION_POSITION_VONNNACHNMINUS1) {
			if (eI.getSource() == panelQueryAngStklPos) {
				int iPos = panelQueryAngStklPos.getTable().getSelectedRow();

				// wenn die Position nicht die erste ist
				if (iPos > 0) {
					Integer iIdPosition = (Integer) panelQueryAngStklPos
							.getSelectedId();

					Integer iIdPositionMinus1 = (Integer) panelQueryAngStklPos
							.getTable().getValueAt(iPos - 1, 0);

					DelegateFactory
							.getInstance()
							.getAngebotstklpositionDelegate()
							.vertauscheAgstklpositionen(iIdPosition,
									iIdPositionMinus1);

					// die Liste neu anzeigen und den richtigen Datensatz
					// markieren
					panelQueryAngStklPos.setSelectedId(iIdPosition);
				}
			}
		} else if (eI.getID() == ItemChangedEvent.ACTION_POSITION_VONNNACHNPLUS1) {
			if (eI.getSource() == panelQueryAngStklPos) {
				int iPos = panelQueryAngStklPos.getTable().getSelectedRow();

				// wenn die Position nicht die letzte ist
				if (iPos < panelQueryAngStklPos.getTable().getRowCount() - 1) {
					Integer iIdPosition = (Integer) panelQueryAngStklPos
							.getSelectedId();

					Integer iIdPositionPlus1 = (Integer) panelQueryAngStklPos
							.getTable().getValueAt(iPos + 1, 0);

					DelegateFactory
							.getInstance()
							.getAngebotstklpositionDelegate()
							.vertauscheAgstklpositionen(iIdPosition,
									iIdPositionPlus1);

					// die Liste neu anzeigen und den richtigen Datensatz
					// markieren
					panelQueryAngStklPos.setSelectedId(iIdPosition);
				}
			}
		} else if (eI.getID() == ItemChangedEvent.ACTION_POSITION_VORPOSITIONEINFUEGEN) {
			if (eI.getSource() == panelQueryAngStklPos) {
				panelDetailAngStklPos.eventActionNew(eI, true, false);
				panelDetailAngStklPos.eventYouAreSelected(false); // Buttons
																	// schalten
			}
		}

		else if (eI.getID() == ItemChangedEvent.ITEM_CHANGED) {
			if (eI.getSource() == panelQueryAgstkl) {

				if (panelQueryAgstkl.getSelectedId() != null) {
					getInternalFrameAngebotstkl().setKeyWasForLockMe(
							panelQueryAgstkl.getSelectedId() + "");
					getInternalFrameAngebotstkl().setAgstklDto(
							DelegateFactory
									.getInstance()
									.getAngebotstklDelegate()
									.agstklFindByPrimaryKey(
											(Integer) panelQueryAgstkl
													.getSelectedId()));

					if (getInternalFrameAngebotstkl().getAgstklDto() != null) {

						String sBezeichnung = "";

						if (getInternalFrameAngebotstkl().getAgstklDto()
								.getKundeIId() != null) {

							KundeDto kundeDto = DelegateFactory
									.getInstance()
									.getKundeDelegate()
									.kundeFindByPrimaryKey(
											getInternalFrameAngebotstkl()
													.getAgstklDto()
													.getKundeIId());

							sBezeichnung = kundeDto.getPartnerDto()
									.formatFixTitelName1Name2();
						}

						if (getInternalFrameAngebotstkl().getAgstklDto()
								.getCBez() != null) {
							sBezeichnung += " "
									+ getInternalFrameAngebotstkl()
											.getAgstklDto().getCBez();
						}

						getInternalFrame().setLpTitle(
								InternalFrame.TITLE_IDX_AS_I_LIKE,
								getInternalFrameAngebotstkl().getAgstklDto()
										.getCNr() + ", " + sBezeichnung);
					}
				} else {

					getInternalFrame().enableAllOberePanelsExceptMe(this,
							IDX_PANEL_AUSWAHL, (false));
					getInternalFrame().setLpTitle(
							InternalFrame.TITLE_IDX_AS_I_LIKE, "");

				}
			} else if (eI.getSource() == panelQueryAngStklPos) {
				Object key = ((ISourceEvent) eI.getSource()).getIdSelected();
				panelDetailAngStklPos.setKeyWhenDetailPanel(key);
				panelDetailAngStklPos.eventYouAreSelected(false);

				// im QP die Buttons in den Zustand nolocking/save setzen.
				panelQueryAngStklPos.updateButtons(panelDetailAngStklPos
						.getLockedstateDetailMainKey());
			} else if (eI.getSource() == panelQueryArbeitsplan) {
				Object key = ((ISourceEvent) eI.getSource()).getIdSelected();
				panelDetailArbeitsplan.setKeyWhenDetailPanel(key);
				panelDetailArbeitsplan.eventYouAreSelected(false);
				panelQueryArbeitsplan.updateButtons(panelDetailArbeitsplan
						.getLockedstateDetailMainKey());
			} else if (eI.getSource() == panelQueryMengenstaffel) {
				Object key = ((ISourceEvent) eI.getSource()).getIdSelected();
				panelDetailMengenstaffel.setKeyWhenDetailPanel(key);
				panelDetailMengenstaffel.eventYouAreSelected(false);
				panelQueryMengenstaffel.updateButtons(panelDetailMengenstaffel
						.getLockedstateDetailMainKey());
			}
		} else if (eI.getID() == ItemChangedEvent.ACTION_UPDATE) {
			// hier kommt man nach upd im D bei einem 1:n hin.
			if (eI.getSource() == panelDetailAngStklPos) {
				// im QP die Buttons in den Zustand neu setzen.
				panelQueryAngStklPos.updateButtons(new LockStateValue(
						PanelBasis.LOCK_FOR_NEW));
			} else if (eI.getSource() == panelDetailArbeitsplan) {
				panelQueryArbeitsplan.updateButtons(new LockStateValue(
						PanelBasis.LOCK_FOR_NEW));
			} else if (eI.getSource() == panelDetailMengenstaffel) {
				panelQueryMengenstaffel.updateButtons(new LockStateValue(
						PanelBasis.LOCK_FOR_NEW));
			}
		} else if (eI.getID() == ItemChangedEvent.ACTION_GOTO_MY_DEFAULT_QP) {
			// aktiviere ein QP ...
			if (eI.getSource() == panelDetailAgstkl) {

				panelQueryAgstkl.eventYouAreSelected(false);
				getInternalFrameAngebotstkl().getAgstklDto().setIId(
						(Integer) panelQueryAgstkl.getSelectedId());
				panelDetailAgstkl.setKeyWhenDetailPanel(panelQueryAgstkl
						.getSelectedId());
				this.setSelectedComponent(panelQueryAgstkl);
				if (panelQueryAgstkl.getSelectedId() == null) {
					getInternalFrame().enableAllOberePanelsExceptMe(this,
							IDX_PANEL_AUSWAHL, false);
				}

			} else if (eI.getSource() == panelDetailAngStklPos) {
				// bei einem Neu im 1:n Panel wurde der KeyForLockMe
				// ueberschrieben
				setKeyWasForLockMe();
				if (panelDetailAngStklPos.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryAngStklPos
							.getId2SelectAfterDelete();
					panelQueryAngStklPos.setSelectedId(oNaechster);
				}
				panelSplitAngStk.eventYouAreSelected(false); // refresh auf das
																// gesamte 1:n
																// panel
			} else if (eI.getSource() == panelDetailArbeitsplan) {
				setKeyWasForLockMe();
				if (panelDetailArbeitsplan.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryArbeitsplan
							.getId2SelectAfterDelete();
					panelQueryArbeitsplan.setSelectedId(oNaechster);
				}
				panelSplitArbeitsplan.eventYouAreSelected(false);
			} else if (eI.getSource() == panelDetailMengenstaffel) {
				setKeyWasForLockMe();
				if (panelDetailMengenstaffel.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryMengenstaffel
							.getId2SelectAfterDelete();
					panelQueryMengenstaffel.setSelectedId(oNaechster);
				}
				panelSplitMengenstaffel.eventYouAreSelected(false);
			}
		} else if (eI.getID() == ItemChangedEvent.ACTION_YOU_ARE_SELECTED) {
			if (eI.getSource() instanceof AssistentView) {
				getAktuellesPanel().eventYouAreSelected(false);
			}
			refreshTitle();
			super.lPEventItemChanged(eI);
		} else if (eI.getID() == ItemChangedEvent.ACTION_DISCARD) {
			if (eI.getSource() == panelDetailAngStklPos) {
				panelSplitAngStk.eventYouAreSelected(false); // das 1:n Panel
																// neu aufbauen
			}
		} else if (eI.getID() == ItemChangedEvent.ACTION_NEW) {
			if (eI.getSource() == panelQueryAgstkl) {
				createKopfdaten(null);
				// wenn es bisher keinen eintrag gibt, die restlichen
				// panels aktivieren
				if (panelQueryAgstkl.getSelectedId() == null) {
					getInternalFrame().enableAllOberePanelsExceptMe(this,
							IDX_PANEL_AUSWAHL, true);
				}

				panelDetailAgstkl.eventActionNew(eI, true, false);
				setSelectedComponent(panelDetailAgstkl);

			} else if (eI.getSource() == panelQueryAngStklPos) {
				panelDetailAngStklPos.eventActionNew(eI, true, false);
				panelDetailAngStklPos.eventYouAreSelected(false);
				setSelectedComponent(panelSplitAngStk); // ui
			} else if (eI.getSource() == panelQueryArbeitsplan) {
				panelDetailArbeitsplan.eventActionNew(eI, true, false);
				panelDetailArbeitsplan.eventYouAreSelected(false);
				setSelectedComponent(panelSplitArbeitsplan); // ui
			} else if (eI.getSource() == panelQueryMengenstaffel) {
				panelDetailMengenstaffel.eventActionNew(eI, true, false);
				panelDetailMengenstaffel.eventYouAreSelected(false);
				setSelectedComponent(panelSplitMengenstaffel); // ui
			}
		} else if (eI.getID() == ItemChangedEvent.ACTION_KOPIEREN) {
			if (eI.getSource().equals(panelQueryAngStklPos)) {
				copyHV();
			} else if (eI.getSource().equals(panelQueryArbeitsplan)) {
				copyHV();
			}
		}
	}

	/**
	 * einfuegenHV
	 * 
	 * @throws IOException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws Throwable
	 */
	public void einfuegenHV() throws IOException, ParserConfigurationException,
			SAXException, Throwable {

		Object o = LPMain.getInstance().getPasteBuffer()
				.readObjectFromPasteBuffer();
		int selectedPanelIndex = this.getSelectedIndex();
		if (o instanceof BelegpositionDto[]) {
			if (selectedPanelIndex == IDX_PANEL_POSITIONEN) {

				AgstklpositionDto[] agstklpositionDtos = DelegateFactory
						.getInstance()
						.getBelegpostionkonvertierungDelegate()
						.konvertiereNachAgstklpositionDto(
								(BelegpositionDto[]) o,
								getInternalFrameAngebotstkl().getAgstklDto()
										.getIId());

				if (agstklpositionDtos != null) {
					Integer iId = null;
					Boolean b = positionAmEndeEinfuegen();
					if (b != null) {
						for (int i = 0; i < agstklpositionDtos.length; i++) {
							AgstklpositionDto agstklpositionDto = agstklpositionDtos[i];
							try {
								agstklpositionDto.setIId(null);

								agstklpositionDto
										.setBelegIId(getInternalFrameAngebotstkl()
												.getAgstklDto().getIId());
								if (b == false) {

									// Zusatzbezeichnung
									if (agstklpositionDto.getArtikelIId() != null) {
										ArtikelDto artikelDto = DelegateFactory
												.getInstance()
												.getArtikelDelegate()
												.artikelFindByPrimaryKey(
														agstklpositionDto
																.getArtikelIId());
										if (artikelDto.getArtikelsprDto() != null) {
											agstklpositionDto
													.setCZusatzbez(artikelDto
															.getArtikelsprDto()
															.getCZbez());
										}
									}

									Integer iIdAktuellePosition = (Integer) panelQueryAngStklPos
											.getSelectedId();

									// erstepos: 0 die erste Position steht an
									// der Stelle 1
									Integer iSortAktuellePosition = new Integer(
											1);

									// erstepos: 1 die erste Position steht an
									// der Stelle 1
									if (iIdAktuellePosition != null) {
										iSortAktuellePosition = DelegateFactory
												.getInstance()
												.getAngebotstklpositionDelegate()
												.agstklpositionFindByPrimaryKey(
														iIdAktuellePosition)
												.getISort();

										// Die bestehenden Positionen muessen
										// Platz fuer die neue schaffen
										DelegateFactory
												.getInstance()
												.getAngebotstklpositionDelegate()
												.sortierungAnpassenBeiEinfuegenEinerPositionVorPosition(
														getInternalFrameAngebotstkl()
																.getAgstklDto()
																.getIId(),
														iSortAktuellePosition
																.intValue());

										// Die neue Position wird an frei
										// gemachte
										// Position gesetzt
										agstklpositionDto
												.setISort(iSortAktuellePosition);
									} else {
										agstklpositionDto.setISort(null);
									}
									// wir legen eine neue position an

								} else {
									agstklpositionDto.setISort(null);
								}

								if (iId == null) {
									iId = DelegateFactory
											.getInstance()
											.getAngebotstklpositionDelegate()
											.createAgstklposition(
													agstklpositionDto);
								} else {
									DelegateFactory
											.getInstance()
											.getAngebotstklpositionDelegate()
											.createAgstklposition(
													agstklpositionDto);
								}

							} catch (Throwable t) {
								// nur loggen!
								myLogger.error(t.getMessage(), t);
							}
						}
					}
					// die Liste neu aufbauen
					panelQueryAngStklPos.eventYouAreSelected(false);
					// den Datensatz in der Liste selektieren
					panelQueryAngStklPos.setSelectedId(iId);
					// im Detail den selektierten anzeigen
					panelSplitAngStk.eventYouAreSelected(false);
				}
			} else if (selectedPanelIndex == IDX_PANEL_ARBEITSPLAN) {

				AgstklarbeitsplanDto[] positionDtos = DelegateFactory
						.getInstance()
						.getBelegpostionkonvertierungDelegate()
						.konvertiereNachAgstklarbeitsplanDto(
								(BelegpositionDto[]) o);

				int iInserted = 0;
				if (positionDtos != null) {
					Integer iId = null;
					for (int i = 0; i < positionDtos.length; i++) {
						AgstklarbeitsplanDto positionDto = positionDtos[i];
						try {
							positionDto.setIId(null);
							positionDto
									.setAgstklIId(getInternalFrameAngebotstkl()
											.getAgstklDto().getIId());

							// wir legen eine neue position an
							iId = DelegateFactory.getInstance()
									.getAngebotstklDelegate()
									.createAgstklarbeitsplan(positionDto);
							iInserted++;
						} catch (Throwable t) {
							// nur loggen!
							myLogger.error(t.getMessage(), t);
						}
					}

					// den Datensatz in der Liste selektieren
					panelQueryArbeitsplan.setSelectedId(iId);

					// die Liste neu aufbauen
					panelQueryArbeitsplan.eventYouAreSelected(false);

					// im Detail den selektierten anzeigen
					panelSplitArbeitsplan.eventYouAreSelected(false);
				}
			}
		}

	}

	/**
	 * Behandle ChangeEvent; zB Tabwechsel oben.
	 * 
	 * @param e
	 *            ChangeEvent
	 * @throws Throwable
	 */
	public void lPEventObjectChanged(ChangeEvent e) throws Throwable {

		super.lPEventObjectChanged(e);
		int selectedIndex = this.getSelectedIndex();

		if (selectedIndex == IDX_PANEL_AUSWAHL) {
			refreshAuswahl();
			panelQueryAgstkl.eventYouAreSelected(false);
			panelQueryAgstkl.updateButtons();
			if (panelQueryAgstkl.getSelectedId() == null) {
				getInternalFrame().enableAllOberePanelsExceptMe(this,
						IDX_PANEL_AUSWAHL, false);
			}

		} else if (selectedIndex == IDX_PANEL_KOPFDATEN) {
			Integer key = null;
			if (getInternalFrameAngebotstkl().getAgstklDto() != null) {
				key = getInternalFrameAngebotstkl().getAgstklDto().getIId();
			}
			createKopfdaten(key);
			panelDetailAgstkl.eventYouAreSelected(false);
		} else if (selectedIndex == IDX_PANEL_AUFSCHLAEGE) {
			Integer key = null;
			if (getInternalFrameAngebotstkl().getAgstklDto() != null) {
				key = getInternalFrameAngebotstkl().getAgstklDto().getIId();
			}
			createAufschlaege(key);
			panelDetailAgstklAufschlaege.eventYouAreSelected(false);
		} else if (selectedIndex == IDX_PANEL_ARBEITSPLAN) {

			refreshAngebotstklarbeitsplan();

			Integer key = null;
			if (getInternalFrameAngebotstkl().getAgstklDto() != null) {
				key = getInternalFrameAngebotstkl().getAgstklDto().getIId();

				FilterKriterium[] defaultfk = AngebotstklFilterFactory
						.getInstance().createFKAgstkl(key);
				panelQueryArbeitsplan.setDefaultFilter(defaultfk);
			}

			panelSplitArbeitsplan.eventYouAreSelected(false);

			panelQueryArbeitsplan.updateButtons(panelDetailArbeitsplan
					.getLockedstateDetailMainKey());
		} else if (selectedIndex == IDX_PANEL_MENGENSTAFFEL) {

			refreshAngebotstklmengenstaffel();

			Integer key = null;
			if (getInternalFrameAngebotstkl().getAgstklDto() != null) {
				key = getInternalFrameAngebotstkl().getAgstklDto().getIId();

				FilterKriterium[] defaultfk = AngebotstklFilterFactory
						.getInstance().createFKAgstkl(key);
				panelQueryMengenstaffel.setDefaultFilter(defaultfk);
			}

			panelSplitMengenstaffel.eventYouAreSelected(false);

			panelQueryMengenstaffel.updateButtons(panelDetailMengenstaffel
					.getLockedstateDetailMainKey());
		} else if (selectedIndex == IDX_PANEL_POSITIONEN) {
			refreshAngebotstklPositionen();

			Integer key = null;
			if (getInternalFrameAngebotstkl().getAgstklDto() != null) {
				key = getInternalFrameAngebotstkl().getAgstklDto().getIId();

				FilterKriterium[] defaultfk = AngebotstklFilterFactory
						.getInstance().createFKAgstkl(key);
				panelQueryAngStklPos.setDefaultFilter(defaultfk);
			}

			panelSplitAngStk.eventYouAreSelected(false);

			panelQueryAngStklPos.updateButtons(panelDetailAngStklPos
					.getLockedstateDetailMainKey());
		}
	}

	/**
	 * kopiere die selektierten positionen in den pastebuffer.
	 * 
	 * @throws ExceptionLP
	 * @throws Throwable
	 */
	public void copyHV() throws ExceptionLP, Throwable {

		int selectedPanelIndex = this.getSelectedIndex();

		if (selectedPanelIndex == IDX_PANEL_POSITIONEN) {

			Object aoIIdPosition[] = panelQueryAngStklPos.getSelectedIds();

			if (aoIIdPosition != null && (aoIIdPosition.length > 0)) {
				AgstklpositionDto[] dtos = new AgstklpositionDto[aoIIdPosition.length];
				Integer aIIdPosition[] = new Integer[aoIIdPosition.length];
				for (int i = 0; i < aIIdPosition.length; i++) {
					aIIdPosition[i] = (Integer) aoIIdPosition[i];
					dtos[i] = DelegateFactory
							.getInstance()
							.getAngebotstklpositionDelegate()
							.agstklpositionFindByPrimaryKey(
									(Integer) aoIIdPosition[i]);
				}
				LPMain.getInstance().getPasteBuffer()
						.writeObjectToPasteBuffer(dtos);
			}
		} else if (selectedPanelIndex == IDX_PANEL_ARBEITSPLAN) {
			Object aoIIdPosition[] = panelQueryArbeitsplan.getSelectedIds();

			if (aoIIdPosition != null && (aoIIdPosition.length > 0)) {
				AgstklarbeitsplanDto[] dtos = new AgstklarbeitsplanDto[aoIIdPosition.length];
				Integer aIIdPosition[] = new Integer[aoIIdPosition.length];
				for (int i = 0; i < aIIdPosition.length; i++) {
					aIIdPosition[i] = (Integer) aoIIdPosition[i];
					dtos[i] = DelegateFactory
							.getInstance()
							.getAngebotstklDelegate()
							.agstklarbeitsplanFindByPrimaryKey(
									(Integer) aoIIdPosition[i]);
				}
				LPMain.getInstance().getPasteBuffer()
						.writeObjectToPasteBuffer(dtos);
			}
		}

	}

	public javax.swing.JMenuBar getJMenuBar() throws Throwable {
		if (wrapperMenuBar == null) {
			wrapperMenuBar = new WrapperMenuBar(this);

			// Menue Datei; ein neuer Eintrag wird immer oben im Menue
			// eingetragen
			JMenu jmModul = (JMenu) wrapperMenuBar
					.getComponent(WrapperMenuBar.MENU_MODUL);

			jmModul.add(new JSeparator(), 0);

			JMenuItem menuItemDateiDrucken = new JMenuItem(LPMain.getInstance()
					.getTextRespectUISPr("lp.menu.drucken"));
			menuItemDateiDrucken.addActionListener(this);
			menuItemDateiDrucken.setActionCommand(MENU_DATEI_DRUCKEN);
			jmModul.add(menuItemDateiDrucken, 0);

		}

		return wrapperMenuBar;
	}

	private void dialogStueckliste(ItemChangedEvent e) throws Throwable {
		panelStueckliste = StuecklisteFilterFactory.getInstance()
				.createPanelFLRStueckliste(getInternalFrame(), null, false);
		new DialogQuery(panelStueckliste);
	}

	private PanelSplit refreshAngebotstklPositionen() throws Throwable {
		if (panelSplitAngStk == null) {

			if (getInternalFrameAngebotstkl().iKalkulationsart == 2
					|| getInternalFrameAngebotstkl().iKalkulationsart == 3) {
				panelDetailAngStklPos = new PanelAgstklPositionenEinkauf(
						getInternalFrame(), LPMain.getInstance()
								.getTextRespectUISPr("angb.panel.positionen"),
						null); // eventuell
								// gibt
								// es
								// noch
								// keine
								// position

			} else {
				panelDetailAngStklPos = new PanelAngebotstklPositionen(
						getInternalFrame(), LPMain.getInstance()
								.getTextRespectUISPr("angb.panel.positionen"),
						null); // eventuell
								// gibt
								// es
								// noch
								// keine
								// position
			}

			QueryType[] qtPositionen = null;
			FilterKriterium[] filtersPositionen = AngebotstklFilterFactory
					.getInstance().createFKAgstkl(
							getInternalFrameAngebotstkl().getAgstklDto()
									.getIId());

			String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW,
					PanelBasis.ACTION_POSITION_VONNNACHNMINUS1,
					PanelBasis.ACTION_POSITION_VONNNACHNPLUS1,
					PanelBasis.ACTION_POSITION_VORPOSITIONEINFUEGEN,
					PanelBasis.ACTION_KOPIEREN,
					PanelBasis.ACTION_EINFUEGEN_LIKE_NEW, };

			panelQueryAngStklPos = new PanelQuery(qtPositionen,
					filtersPositionen, QueryParameters.UC_ID_AGSTKLPOSITION,
					aWhichButtonIUse, getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("angb.panel.positionen"), true); // flag
																					// ,
																					// damit
																					// flr
																					// erst
																					// beim
																					// aufruf
																					// des
																					// panels
																					// loslaeuft

			panelQueryAngStklPos.createAndSaveAndShowButton(
					"/com/lp/client/res/text_code_colored16x16.png",
					LPMain.getInstance().getTextRespectUISPr(
							"stkl.positionen.importausandererstueckliste"),
					BUTTON_IMPORTSTUECKLISTEPOSITIONEN, null);

			panelQueryAngStklPos.createAndSaveAndShowButton(
					"/com/lp/client/res/calculator16x16.png",
					LPMain.getInstance().getTextRespectUISPr(
							"agstkl.preiseneurechnen.kalk2"),
					BUTTON_PREISE_NEU_RECHNEN, null);

			panelQueryAngStklPos.createAndSaveAndShowButton(
					"/com/lp/client/res/note_add16x16.png",
					LPMain.getInstance().getTextRespectUISPr(
							"as.positionen.importausandererstueckliste"),
					BUTTON_IMPORTAGSTUECKLISTEPOSITIONEN, null);

			if (LPMain
					.getInstance()
					.getDesktop()
					.darfAnwenderAufZusatzfunktionZugreifen(
							MandantFac.ZUSATZFUNKTION_INTELLIGENTER_STUECKLISTENIMPORT)) {
				panelQueryAngStklPos.createAndSaveAndShowButton(
						"/com/lp/client/res/document_into.png",
						LPMain.getInstance().getTextRespectUISPr(
								"stkl.intelligenterstklimport"),
						BUTTON_INTELLIGENTERIMPORTCSV_AGSTKLPOSITIONEN, null);
			}

			panelQueryAngStklPos.setMultipleRowSelectionEnabled(true);

			panelSplitAngStk = new PanelSplit(getInternalFrame(),
					panelDetailAngStklPos, panelQueryAngStklPos, 180);

			setComponentAt(IDX_PANEL_POSITIONEN, panelSplitAngStk);
		}

		return panelSplitAngStk;
	}

	private PanelSplit refreshAngebotstklarbeitsplan() throws Throwable {
		if (panelSplitArbeitsplan == null) {

			panelDetailArbeitsplan = new PanelAgstklarbeitsplan(
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("angb.panel.positionen"), null);

			QueryType[] qtPositionen = null;
			FilterKriterium[] filtersPositionen = AngebotstklFilterFactory
					.getInstance().createFKAgstkl(
							getInternalFrameAngebotstkl().getAgstklDto()
									.getIId());

			String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW,
					PanelBasis.ACTION_KOPIEREN,
					PanelBasis.ACTION_EINFUEGEN_LIKE_NEW };

			panelQueryArbeitsplan = new PanelQuery(qtPositionen,
					filtersPositionen, QueryParameters.UC_ID_AGSTKLARBEITSPLAN,
					aWhichButtonIUse, getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("angb.panel.positionen"), true);

			panelQueryArbeitsplan.createAndSaveAndShowButton(
					"/com/lp/client/res/text_code_colored16x16.png",
					LPMain.getInstance().getTextRespectUISPr(
							"agstkl.positionen.importausanderem.arbeitsplan"),
					BUTTON_IMPORTSTUECKLISTEARBEITSLPAN, null);

			panelQueryArbeitsplan.createAndSaveAndShowButton(
					"/com/lp/client/res/note_add16x16.png",
					LPMain.getInstance().getTextRespectUISPr(
							"as.positionen.importausandererstueckliste"),
					BUTTON_IMPORTAGSTUECKLISTEARBEITSPLAN, null);
			panelQueryArbeitsplan.setMultipleRowSelectionEnabled(true);
			panelSplitArbeitsplan = new PanelSplit(getInternalFrame(),
					panelDetailArbeitsplan, panelQueryArbeitsplan, 180);

			setComponentAt(IDX_PANEL_ARBEITSPLAN, panelSplitArbeitsplan);
		}

		return panelSplitArbeitsplan;
	}

	private PanelSplit refreshAngebotstklmengenstaffel() throws Throwable {
		if (panelSplitMengenstaffel == null) {

			panelDetailMengenstaffel = new PanelAgstklmengenstaffel(
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("as.agstkl.mengenstaffel"),
					null, this);

			QueryType[] qtPositionen = null;
			FilterKriterium[] filtersPositionen = AngebotstklFilterFactory
					.getInstance().createFKAgstkl(
							getInternalFrameAngebotstkl().getAgstklDto()
									.getIId());

			String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW };

			panelQueryMengenstaffel = new PanelQuery(qtPositionen,
					filtersPositionen,
					QueryParameters.UC_ID_AGSTKLMENGENSTAFFEL,
					aWhichButtonIUse, getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("as.agstkl.mengenstaffel"),
					true);

			panelSplitMengenstaffel = new PanelSplit(getInternalFrame(),
					panelDetailMengenstaffel, panelQueryMengenstaffel, 280);

			setComponentAt(IDX_PANEL_MENGENSTAFFEL, panelSplitMengenstaffel);
		}

		return panelSplitMengenstaffel;
	}

	public void fillMustFields(BelegpositionDto belegposDtoI, int xalOfBelegPosI)
			throws Throwable {
		AgstklpositionDto angStklPosDto = (AgstklpositionDto) belegposDtoI;

		String sPosArt = angStklPosDto.getPositionsartCNr();
		if (!LocaleFac.POSITIONSART_IDENT.startsWith(sPosArt)
				&& !LocaleFac.POSITIONSART_HANDEINGABE.startsWith(sPosArt)) {
			String sMsg = "LocaleFac.POSITIONSART_IDENT.startsWith(sPosArt) "
					+ "&& !LocaleFac.POSITIONSART_HANDEINGABE.startsWith(sPosArt)";
			throw new IllegalArgumentException(sMsg);
		}

		angStklPosDto.setBelegIId(getInternalFrameAngebotstkl().getAgstklDto()
				.getIId());
		angStklPosDto.setISort(xalOfBelegPosI + 1000);

		if (angStklPosDto.getNGestehungspreis() == null) {
			// WH: 2007-01-15: Gestpreis aus Hauptlager des Mandanten
			// uebernehmen
			if (angStklPosDto.getArtikelIId() != null) {
				LagerDto lagerDto = DelegateFactory.getInstance()
						.getLagerDelegate().getHauptlagerDesMandanten();

				BigDecimal preis = DelegateFactory
						.getInstance()
						.getLagerDelegate()
						.getGemittelterGestehungspreisEinesLagers(
								angStklPosDto.getArtikelIId(),
								lagerDto.getIId());
				angStklPosDto.setNGestehungspreis(preis);
			} else {
				angStklPosDto.setNGestehungspreis(new BigDecimal(0));
			}
		}
		if (angStklPosDto.getBDrucken() == null) {
			angStklPosDto.setBDrucken(Helper.boolean2Short(true));
		}
		if (angStklPosDto.getBRabattsatzuebersteuert() == null) {
			angStklPosDto.setBRabattsatzuebersteuert(Helper
					.boolean2Short(false));
		}
		if (angStklPosDto.getNNettoeinzelpreis() == null) {
			angStklPosDto.setNNettoeinzelpreis(new BigDecimal(0));
		}
		if (angStklPosDto.getNNettogesamtpreis() == null) {
			angStklPosDto.setNNettogesamtpreis(new BigDecimal(0));
		}
		if (angStklPosDto.getFRabattsatz() == null) {
			angStklPosDto.setFRabattsatz(0.0);
		}
		if (angStklPosDto.getBArtikelbezeichnunguebersteuert() == null) {
			angStklPosDto.setBArtikelbezeichnunguebersteuert(Helper
					.boolean2Short(false));
		}

	}

	public Integer saveBelegPosAsTextpos(
			POSDocument2POSDto pOSDocument2POSDtoI, int xalOfBelegPosI)
			throws ExceptionLP, Throwable {
		return null;
	}

}
