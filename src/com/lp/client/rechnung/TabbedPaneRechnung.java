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
package com.lp.client.rechnung;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JMenu;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;
import javax.swing.event.ChangeEvent;

import net.sf.jasperreports.engine.JRException;

import com.lp.client.auftrag.AuftragFilterFactory;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.LockStateValue;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQuery;
import com.lp.client.frame.component.PanelQueryAuftraege;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.PanelSplit;
import com.lp.client.frame.component.PanelTabelle;
import com.lp.client.frame.component.WrapperMenu;
import com.lp.client.frame.component.WrapperMenuBar;
import com.lp.client.frame.component.WrapperMenuItem;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.frame.report.PanelReportKriterien;
import com.lp.client.lieferschein.LieferscheinFilterFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.system.SystemFilterFactory;
import com.lp.server.auftrag.service.AuftragDto;
import com.lp.server.auftrag.service.AuftragServiceFac;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.rechnung.service.RechnungDto;
import com.lp.server.rechnung.service.RechnungFac;
import com.lp.server.rechnung.service.RechnungSichtAuftragDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.FilterKriteriumDirekt;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

/**
 * <p>
 * Diese Klasse kuemmert sich um Panels des Rechnungsmoduls; um Rechnungen des
 * Typs Rechnung
 * </p>
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2008
 * </p>
 * <p>
 * Erstellungsdatum 20.11.2004
 * </p>
 * <p>
 * </p>
 *
 * @author Martin Bluehweis
 * @version $Revision: 1.36 $
 */
public class TabbedPaneRechnung extends TabbedPaneRechnungAll {

	private static final long serialVersionUID = 1L;
	// die folgenden Panels werden optional eingehaengt
	private int iIDX_SICHTLIEFERSCHEIN = -1;
	private int iIDX_KONDITIONEN = -1;
	private int iIDX_UMSATZ = -1;
	private int iIDX_MAHNUNGEN = -1;
	private int iIDX_RECHNUNGAUFTRAEGE = -1;
	private int iIDX_SICHT_AUFTRAG = -1;

	private static final String MENUE_ACTION_DATEI_MAHNEN = "MENU_ACTION_DATEI_MAHNEN";
	private static final String MENUE_ACTION_DATEI_ANGELEGTE_DRUCKEN_ALLE = "MENU_ACTION_DATEI_ANGELEGTE_DRUCKEN_ALLE";
	private static final String MENUE_ACTION_DATEI_ANGELEGTE_DRUCKEN_KST = "MENU_ACTION_DATEI_ANGELEGTE_DRUCKEN_KST";
	private static final String MENUE_ACTION_BEARBEITEN_MAHNSPERRE_SETZEN = "MENUE_ACTION_BEARBEITEN_MAHNSPERRE_SETZEN";
	private static final String MENUE_ACTION_BEARBEITEN_BELEGUEBERNAHME = "MENUE_ACTION_BEARBEITEN_BELEGUEBERNAHME";
	private static final String MENUE_ACTION_ALLE_RECHNUNGEN = "MENUE_ACTION_ALLE_RECHNUNGEN";
	private static final String MENUE_ACTION_ZM = "MENUE_ACTION_ZM";
	private static final String MENUE_ACTION_OFFENE_RECHNUNGEN = "MENUE_ACTION_OFFENE_RECHNUNGEN";
	private static final String MENUE_ACTION_ZAHLUNGSJOURNAL = "MENUE_ACTION_ZAHLUNGSJOURNAL";
	private static final String MENUE_ACTION_UMSATZ = "MENUE_ACTION_UMSATZ";
	private static final String MENUE_ACTION_WARENAUSGANGSJOURNAL_EXPORT = "MENUE_ACTION_WARENAUSGANGSJOURNAL_EXPORT";
	private static final String MENUE_ACTION_EINZELRECHNUNG_EXPORT = "MENUE_ACTION_EINZELRECHNUNG_EXPORT";
	private static final String MENUE_ACTION_EINZELRECHNUNG_EXPORT_VERDICHTET = "MENUE_ACTION_EINZELRECHNUNG_EXPORT_VERDICHTET";
	private static final String MENUE_ACTION_WIEDERHOLENDE_AUFTRAEGE = "MENUE_ACTION_WIEDERHOLENDE_AUFTRAEGE";
	private static final String MENUE_ACTION_WARENAUSGANSJOURNAL = "MENUE_ACTION_WARENAUSGANGSJOURNAL";
	private static final String MENUE_ACTION_MONATSRECHNUNGEN_ERSTELLEN = "MENUE_ACTION_MONATSRECHNUNGEN_ERSTELLEN";

	private PanelRechnungKonditionen panelDetailKonditionen = null;
	private PanelQuery panelQueryMahnungen = null;

	private PanelDialogKriterienRechnungUmsatz pdKriterienRechnungUmsatz = null;
	private PanelTabelleRechnungUmsatz ptRechnungUmsatz = null;

	private PanelQuery lsPositionenTop = null; // FLR 1:n Liste
	private PanelRechnungSichtLieferschein lsPositionenBottom = null;
	private PanelSplit lsPositionen = null;
	private PanelQuery lsPositionenSichtAuftragTop = null; // FLR 1:n Liste
	private PanelRechnungPositionenSichtAuftrag lsPositionenSichtAuftragBottom = null;
	private PanelSplit lsPositionenSichtAuftrag = null;

	private PanelQueryFLR panelQueryFLREizelrechnungsexport = null;
	private PanelQueryFLR panelQueryFLREizelrechnungsexportVerdichtet = null;
	private PanelQueryAuftraege lsAuftraege = null; // FLR 1:n Liste

	private PanelQueryFLR panelQueryFLRKostenstelle = null;
	private PanelQueryFLR panelQueryFLRAuftragauswahlZusatz = null;

	private AuftragDto auftragDtoSichtAuftrag = new AuftragDto();

	protected AuftragDto getAuftragDtoSichtAuftrag() {
		return auftragDtoSichtAuftrag;
	}

	private void setAuftragDtoSichtAuftrag(AuftragDto auftragDtoSichtAuftrag) {
		this.auftragDtoSichtAuftrag = auftragDtoSichtAuftrag;
	}

	public TabbedPaneRechnung(InternalFrame internalFrame) throws Throwable {
		super(internalFrame, RechnungFac.RECHNUNGTYP_RECHNUNG, LPMain
				.getTextRespectUISPr("rechnung.tab.unten.rechnung.title"));
		jbInitTP();
		initComponents();
	}

	private void jbInitTP() throws Throwable {
		if (getRechnungstyp().equals(RechnungFac.RECHNUNGART_RECHNUNG)) {
			// 4 Sicht Lieferschein
			iIDX_SICHTLIEFERSCHEIN = this.getTabCount();
			insertTab(
					LPMain.getTextRespectUISPr("rechnung.tab.oben.sichtlieferschein.title"),
					null,
					null,
					LPMain.getTextRespectUISPr("rechnung.tab.oben.sichtlieferschein.tooltip"),
					iIDX_SICHTLIEFERSCHEIN);
			if (bAuftragRechnung) {
				// 5 Auftraege
				iIDX_RECHNUNGAUFTRAEGE = this.getTabCount();
				insertTab(
						LPMain.getTextRespectUISPr("rechnung.tab.oben.auftraege.title"),
						null,
						null,
						LPMain.getTextRespectUISPr("rechnung.tab.oben.auftraege.tooltip"),
						iIDX_RECHNUNGAUFTRAEGE);
				// 6 Sicht Auftrag
				iIDX_SICHT_AUFTRAG = this.getTabCount();
				insertTab(
						LPMain.getTextRespectUISPr("rechnung.tab.oben.sichtauftrag.title"),
						null,
						null,
						LPMain.getTextRespectUISPr("rechnung.tab.oben.sichtauftrag.tooltip"),
						iIDX_SICHT_AUFTRAG);
			}
			// 7 Konditionen
			iIDX_KONDITIONEN = this.getTabCount();
			insertTab(
					LPMain.getTextRespectUISPr("rechnung.tab.oben.konditionen.title"),
					null,
					null,
					LPMain.getTextRespectUISPr("rechnung.tab.oben.konditionen.tooltip"),
					iIDX_KONDITIONEN);
			// 8 Zahlungen
			iIDX_ZAHLUNGEN = this.getTabCount();
			insertTab(
					LPMain.getTextRespectUISPr("rechnung.tab.oben.zahlungen.title"),
					null,
					null,
					LPMain.getTextRespectUISPr("rechnung.tab.oben.zahlungen.tooltip"),
					iIDX_ZAHLUNGEN);
			// 9 Kontierung
			iDX_KONTIERUNG = this.getTabCount();
			insertTab(
					LPMain.getTextRespectUISPr("rechnung.tab.oben.kontierung.title"),
					null,
					null,
					LPMain.getTextRespectUISPr("rechnung.tab.oben.kontierung.tooltip"),
					iDX_KONTIERUNG);
			// 10 Umsatzuebersicht
			iIDX_UMSATZ = this.getTabCount();
			insertTab(LPMain.getTextRespectUISPr("lp.umsatzuebersicht"), null,
					null, LPMain.getTextRespectUISPr("lp.umsatzuebersicht"),
					iIDX_UMSATZ);
			// 11 Mahnung
			iIDX_MAHNUNGEN = this.getTabCount();
			// insertTab(LPMain.getInstance().getTextRespectUISPr(
			// "rechnung.tab.oben.mahnungen.title"),
			// null,
			// null,
			// LPMain.getInstance().getTextRespectUISPr(
			// "rechnung.tab.oben.mahnungen.tooltip"),
			// IDX_MAHNUNGEN);
		}
	}

	public void lPEventItemChanged(ItemChangedEvent e) throws Throwable {
		super.lPEventItemChanged(e);
		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			if (e.getSource() == panelQueryFLRKostenstelle) {
				Integer key = (Integer) ((ISourceEvent) e.getSource())
						.getIdSelected();
				if (key != null) {
					printAlleAngelegtenRechnungen(key);
				}
			} else if (e.getSource() == panelQueryFLREizelrechnungsexport) {
				Object[] ids = panelQueryFLREizelrechnungsexport
						.getSelectedIds();
				for (int i = 0; i < ids.length; i++) {
					if (this.getRechnungDto().getStatusCNr()
							.equals(LocaleFac.STATUS_ANGELEGT)
							|| this.getRechnungDto().getStatusCNr()
									.equals(LocaleFac.STATUS_STORNIERT)) {
						DialogFactory
								.showModalDialog(
										LPMain.getTextRespectUISPr("lp.error"),
										LPMain.getTextRespectUISPr("rech.einzelrechnungsexport.error"));

					} else {
						ParametermandantDto parameter = (ParametermandantDto) DelegateFactory
								.getInstance()
								.getParameterDelegate()
								.getParametermandant(
										ParameterFac.PARAMETER_EINZELRECHNUNG_EXPORTPFAD,
										ParameterFac.KATEGORIE_RECHNUNG,
										LPMain.getTheClient().getMandant());
						DelegateFactory
								.getInstance()
								.getRechnungDelegate()
								.erstelleEinzelexport((Integer) ids[i],
										parameter.getCWert(), false);
					}
				}
			} else if (e.getSource() == panelQueryFLREizelrechnungsexportVerdichtet) {
				Object[] ids = panelQueryFLREizelrechnungsexportVerdichtet
						.getSelectedIds();
				for (int i = 0; i < ids.length; i++) {
					if (this.getRechnungDto().getStatusCNr()
							.equals(LocaleFac.STATUS_ANGELEGT)
							|| this.getRechnungDto().getStatusCNr()
									.equals(LocaleFac.STATUS_STORNIERT)) {
						DialogFactory
								.showModalDialog(
										LPMain.getTextRespectUISPr("lp.error"),
										LPMain.getTextRespectUISPr("rech.einzelrechnungsexport.error"));

					} else {
						ParametermandantDto parameter = (ParametermandantDto) DelegateFactory
								.getInstance()
								.getParameterDelegate()
								.getParametermandant(
										ParameterFac.PARAMETER_EINZELRECHNUNG_EXPORTPFAD,
										ParameterFac.KATEGORIE_RECHNUNG,
										LPMain.getTheClient().getMandant());
						DelegateFactory
								.getInstance()
								.getRechnungDelegate()
								.erstelleEinzelexport((Integer) ids[i],
										parameter.getCWert(), true);
					}
				}
			} else if (e.getSource() == panelQueryFLRAuftragauswahlZusatz
					|| e.getSource() == lsAuftraege) {
				Integer iIdAuftrag;
				if (e.getSource() == panelQueryFLRAuftragauswahlZusatz) {
					iIdAuftrag = (Integer) panelQueryFLRAuftragauswahlZusatz
							.getSelectedId();
				} else {
					iIdAuftrag = (Integer) lsAuftraege.getSelectedId();
				}
				if (iIdAuftrag != null) {
					AuftragDto auftragDto = DelegateFactory.getInstance()
							.getAuftragDelegate()
							.auftragFindByPrimaryKey(iIdAuftrag);
					this.setAuftragDtoSichtAuftrag(auftragDto);
					lsPositionenSichtAuftragTop
							.setDefaultFilter(RechnungFilterFactory
									.getInstance()
									.createFKRechnungSichtAuftrag(
											getRechnungDto(),
											getAuftragDtoSichtAuftrag()
													.getIId()));
					setSelectedComponent(getPanelSplitSichtAuftrag());
				}
			}
		} else if (e.getID() == ItemChangedEvent.ITEM_CHANGED) {
			if (e.getSource() == lsPositionenTop) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				lsPositionenBottom.setKeyWhenDetailPanel(key);
				lsPositionenBottom.eventYouAreSelected(false);
				// im QP die Buttons in den Zustand nolocking/save setzen.
				lsPositionenTop.updateButtons(lsPositionenBottom
						.getLockedstateDetailMainKey());
			} else if (e.getSource() == lsPositionenSichtAuftragTop) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				lsPositionenSichtAuftragBottom.setKeyWhenDetailPanel(key);
				lsPositionenSichtAuftragBottom.eventYouAreSelected(false);

				// im QP die Buttons in den Zustand nolocking/save setzen.
				lsPositionenSichtAuftragTop
						.updateButtons(lsPositionenSichtAuftragBottom
								.getLockedstateDetailMainKey());
			} else if (e.getSource() == lsAuftraege) {
				Integer iIdAuftrag = (Integer) lsAuftraege.getSelectedId();
				if (iIdAuftrag != null) {
					AuftragDto auftragDto = DelegateFactory.getInstance()
							.getAuftragDelegate()
							.auftragFindByPrimaryKey(iIdAuftrag);
					this.setAuftragDtoSichtAuftrag(auftragDto);
					getPanelSplitSichtAuftrag();
					lsPositionenSichtAuftragTop
							.setDefaultFilter(RechnungFilterFactory
									.getInstance()
									.createFKRechnungSichtAuftrag(
											getRechnungDto(),
											getAuftragDtoSichtAuftrag()
													.getIId()));
					lsAuftraege.setAuftragSelectedOnTable(true);
				}
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_NEW) {
			if (e.getSource() == lsAuftraege) {
				dialogQueryAuftragFromListeZusatz();
				getPanelQueryAuftraege().updateButtons();
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_DISCARD) {

		} else if (e.getID() == ItemChangedEvent.ACTION_SAVE) {
			if (e.getSource() == lsPositionenBottom) {
				// Spezialfall: es kann sein, dass die position serverseitig
				// geloescht und
				// neu angelegt wird - daher hol ich die id aus der tabelle
				int selectedRow = lsPositionenTop.getTable().getSelectedRow();
				lsPositionenTop.eventYouAreSelected(false);
				Object oKey = lsPositionenTop.getTable().getValueAt(
						selectedRow, 0);
				lsPositionenTop.setSelectedId(oKey);
				lsPositionen.eventYouAreSelected(false);
			} else if (e.getSource() == lsPositionenSichtAuftragBottom) {
				Object oNaechster = lsPositionenSichtAuftragTop
						.getId2SelectAfterDelete();
				lsPositionenSichtAuftrag.eventYouAreSelected(false);

				lsPositionenSichtAuftragTop.setSelectedId(oNaechster);
				lsPositionenSichtAuftrag.eventYouAreSelected(false);

			}
		} else if (e.getID() == ItemChangedEvent.ACTION_GOTO_MY_DEFAULT_QP) {

		}
		// der OK Button in einem PanelDialog wurde gedrueckt
		else if (e.getID() == ItemChangedEvent.ACTION_KRITERIEN_HAVE_BEEN_SELECTED) {
			if (e.getSource() == getPanelDialogRechnungUmsatz(false)) {
				// die Kriterien fuer PanelTabelle abholen
				FilterKriterium[] aAlleKriterien = getPanelDialogRechnungUmsatz(
						true).getAlleFilterKriterien();
				// die Kriterien dem PanelTabelle setzen
				setComponentAt(iIDX_UMSATZ, getPanelTabelleRechnungUmsatz(true));
				getPanelTabelleRechnungUmsatz(true).setDefaultFilter(
						aAlleKriterien);
				// die Uebersicht aktualisieren
				getPanelTabelleRechnungUmsatz(true).eventYouAreSelected(false);
				// man steht auf alle Faelle auf der Uebersicht
				setSelectedComponent(getPanelTabelleRechnungUmsatz(true));
				getPanelTabelleRechnungUmsatz(true).updateButtons(
						new LockStateValue(null, null,
								PanelBasis.LOCK_IS_NOT_LOCKED));
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_MY_OWN_NEW) {
			String sAspectInfo = ((ISourceEvent) e.getSource()).getAspect();
			if (e.getSource() == lsPositionenSichtAuftragTop) {
				if (sAspectInfo
						.equals(MY_OWN_NEW_ALLE_POSITIONEN_AUS_AUFTRAG_UEBERNEHMEN)) {
					lsPositionenSichtAuftragBottom.eventActionNew(e, true,
							false);
					// lsPositionenSichtAuftrag.eventYouAreSelected(false); //
					// das ganze 1:n Panel aktualisieren
					getPanelDetailPositionenSichtAuftrag(true)
							.eventYouAreSelected(false);
					lsPositionenSichtAuftragTop.eventYouAreSelected(false);
				}
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_TABLE_SELECTION_CHANGED) {

			if(bAuftragRechnung && e.getSource() == getPanelQueryAuftraege().getTable()) {
				enableTabSichtAuftrag();
			} else if(e.getSource() == getPanelQueryPositionen(true).getTable()) {
				enableTabSichtLieferschein();
			}
		}

	}

	protected void holeRechnungDto(Object key) throws Throwable {
//		super.holeRechnungDto(key);
		RechnungSichtAuftragDto dto = DelegateFactory.getInstance()
				.getRechnungServiceDelegate().rechnungFindByPrimaryKey((Integer) key) ;

		if( bAuftragRechnung 
				&& (getRechnungDto() == null || getRechnungDto().getIId() == null 
				|| !getRechnungDto().getIId().equals(dto.getRechnungDto().getIId())) ) {
			lsAuftraege.setAuftragSelectedOnTable(dto.getAuftragDto() != null && !dto.isMehrAlsHauptAuftrag());
		}

		setRechnungDto(dto.getRechnungDto());
		setKeyInternalFrame(key) ;
		setAuftragDtoSichtAuftrag(dto.getAuftragDto());

		enableTabSichtLieferschein();
		enableTabAuftraege();
		enableTabSichtAuftrag();

		if (bAuftragRechnung) {
			// Filter auf die Auftraege
			FilterKriterium[] filtersPos = RechnungFilterFactory.getInstance()
					.createFKAuftraegeEinerRechnung(
							(getRechnungDto() != null) ? getRechnungDto()
									.getIId() : -1);
			getPanelQueryAuftraege().setDefaultFilter(filtersPos);
		}
	}

	public void lPEventObjectChanged(ChangeEvent e) throws Throwable {
		super.lPEventObjectChanged(e);
		int index = this.getSelectedIndex();

		if (index == IDX_RECHNUNGEN) {
			// SP855
			setAuftragDtoSichtAuftrag(new AuftragDto());
		} else if (index == iIDX_KONDITIONEN) {
			getPanelDetailKonditionen(true).setKeyWhenDetailPanel(
					getRechnungDto().getIId());
			getPanelDetailKonditionen(true).eventYouAreSelected(false);
			// wenn ich aus dem umsatz komme, verlier ich den titel

			//TODO ghp. was macht das?
			this.setRechnungDto(this.getRechnungDto());
		} else if (index == iIDX_ZAHLUNGEN) {
			getPanelSplitZahlungen(true).eventYouAreSelected(false);
			// wenn ich aus dem umsatz komme, verlier ich den titel
			this.setRechnungDto(this.getRechnungDto());
		} else if (index == iIDX_MAHNUNGEN) {
			getPanelQueryMahnungen(true).eventYouAreSelected(false);
			// wenn ich aus dem umsatz komme, verlier ich den titel
			this.setRechnungDto(this.getRechnungDto());
		} else if (index == iIDX_UMSATZ) {
			getPanelTabelleRechnungUmsatz(true);
			getInternalFrame().showPanelDialog(
					getPanelDialogRechnungUmsatz(true));
		} else if (index == iIDX_SICHTLIEFERSCHEIN) {
			// Es muss ein Lieferschein gewaehlt sein
			if (pruefePositionIstEinLieferschein()) {
				getInternalFrame().setLpTitle(
						InternalFrame.TITLE_IDX_AS_I_LIKE, getSichtLieferscheinTitle());
				getPanelSplitSichtLieferschein();
				// filter aktualisieren
				FilterKriterium[] filtersPositionen = LieferscheinFilterFactory
						.getInstance().createFKFlrlieferscheiniid(
								getLieferscheinDto().getIId());
				lsPositionenTop.setDefaultFilter(filtersPositionen);
				lsPositionen.eventYouAreSelected(false);
				lsPositionenTop.updateButtons(lsPositionenBottom
						.getLockedstateDetailMainKey());
			} else {
				// Fehlermeldung zeigen und ins Positionenpanel schalten
				DialogFactory
						.showModalDialog(
								LPMain.getTextRespectUISPr("lp.hint"),
								LPMain.getTextRespectUISPr("rech.warning.positionistkeinlieferschein"));
				gotoPositionen();
			}
		} else if (index == iIDX_SICHT_AUFTRAG) {
			// getPanelSplitSichtAuftrag().eventYouAreSelected(false);
			if (getAuftragDtoSichtAuftrag().getIId() == null
					|| lsPositionenSichtAuftragTop == null) {
				DialogFactory.showModalDialog(
						LPMain.getTextRespectUISPr("lp.warning"),
						LPMain.getTextRespectUISPr("ls.auftragwaehlen"));
				this.setSelectedComponent(getPanelQueryAuftraege());
			} else {

				getInternalFrame().setLpTitle(
						InternalFrame.TITLE_IDX_AS_I_LIKE, getSichtAuftragTitle());
				lsPositionenSichtAuftragTop
						.setDefaultFilter(RechnungFilterFactory.getInstance()
								.createFKRechnungSichtAuftrag(getRechnungDto(),
										getAuftragDtoSichtAuftrag().getIId()));
				// lsPositionenSichtAuftrag.eventYouAreSelected(false);

				getPanelSplitSichtAuftrag().eventYouAreSelected(false);
				lsPositionenSichtAuftragTop
						.updateButtons(lsPositionenSichtAuftragBottom
								.getLockedstateDetailMainKey());
			}
		} else if (index == iIDX_RECHNUNGAUFTRAEGE) {
			getInternalFrame().setLpTitle(
					InternalFrame.TITLE_IDX_AS_I_LIKE, getTitle());
			// Filter auf die Auftraege
			FilterKriterium[] filtersPos = RechnungFilterFactory.getInstance()
					.createFKAuftraegeEinerRechnung(getRechnungDto().getIId());
			getPanelQueryAuftraege().setDefaultFilter(filtersPos);
			getPanelQueryAuftraege().eventYouAreSelected(false);
			getPanelQueryAuftraege().updateButtons();
		}
		setEnabledAt(iIDX_UMSATZ, bDarfPreiseSehen);
	}

	protected void printZahlschein() throws Throwable {
		getInternalFrame().showReportKriterien(
				new ReportRechnungZahlschein(getInternalFrame(),
						getRechnungDto(), LPMain
								.getTextRespectUISPr("rech.zahlschein")));
	}

	protected void print() throws Throwable {
		if (getRechnungDto() != null) {
			if (pruefeKonditionen(getRechnungDto())) {
				if (DelegateFactory.getInstance().getRechnungDelegate()
						.hatRechnungPositionen(getRechnungDto().getIId())
						.booleanValue()) {
					if (getRechnungDto().getStatusCNr().equals(
							RechnungFac.STATUS_ANGELEGT)) {
						// Zur uebersichtlichen Darstellung die Positionen nach
						// Auftrag sortieren.
						// falls erforderlich und gewuenscht. nur im Status
						// "Angelegt".
						boolean bPositionenSortiert = DelegateFactory
								.getInstance()
								.getRechnungDelegate()
								.pruefePositionenAufSortierungNachAuftrag(
										getRechnungDto().getIId());
						if (!bPositionenSortiert) {
							boolean bAnswer = DialogFactory
									.showModalJaNeinDialog(
											getInternalFrame(),
											LPMain.getTextRespectUISPr("rechnung.frage.positionennachauftragsortieren"),
											LPMain.getTextRespectUISPr("lp.frage"));
							if (bAnswer) {
								DelegateFactory
										.getInstance()
										.getRechnungDelegate()
										.sortierePositionenNachAuftrag(
												getRechnungDto().getIId());
							}
						}
					}

					// Aktivieren findet jetzt im ReportRechnung statt
					if (this.getSelectedIndex() == TabbedPaneRechnungAll.IDX_POSITIONEN) {
						super.getPanelPositionen().eventYouAreSelected(false);
					}
					getInternalFrame().showReportKriterien(
							new ReportRechnung(getInternalFrame(), getAktuellesPanel(),
									getRechnungDto(), getKundeDto(),
									super.getTitle()),
							super.getKundeDto().getPartnerDto(),
							super.getRechnungDto().getAnsprechpartnerIId(),
							false);
				} else {
					DialogFactory.showModalDialog(
							LPMain.getTextRespectUISPr("lp.hint"),
							LPMain.getTextRespectUISPr("rech.keinepositionen"));
				}
			}
		}
	}

	protected javax.swing.JMenuBar getJMenuBar() throws Throwable {
		WrapperMenuBar wmb = new WrapperMenuBar(this);
		JMenu modul = (JMenu) wmb.getComponent(WrapperMenuBar.MENU_MODUL);
		modul.add(new JSeparator(), 0);
		JMenu journal = (JMenu) wmb.getComponent(WrapperMenuBar.MENU_JOURNAL);
		JMenu extras = new WrapperMenu("rechnung.menu.extras", this);
		WrapperMenuItem menuItemNeuDatum = new WrapperMenuItem(
				LPMain.getTextRespectUISPr("rechnung.menu.extras.neudatum"),
				RechteFac.RECHT_RECH_RECHNUNG_CUD);
		menuItemNeuDatum.addActionListener(this);
		menuItemNeuDatum.setActionCommand(MENUE_ACTION_NEU_DATUM);
		extras.add(menuItemNeuDatum);

		WrapperMenuItem menuItemWiederholenedeAuftraege = new WrapperMenuItem(
				LPMain.getTextRespectUISPr("rechnung.menu.extras.wiederholendeauftraegeverrechnen"),
				RechteFac.RECHT_RECH_RECHNUNG_CUD);
		menuItemWiederholenedeAuftraege.addActionListener(this);
		menuItemWiederholenedeAuftraege
				.setActionCommand(MENUE_ACTION_WIEDERHOLENDE_AUFTRAEGE);
		extras.add(menuItemWiederholenedeAuftraege);

		WrapperMenuItem menuItemMonatsrechnungen = new WrapperMenuItem(
				LPMain.getTextRespectUISPr("rech.menu.monatsrechnungenerstellen"),
				RechteFac.RECHT_RECH_RECHNUNG_CUD);
		menuItemMonatsrechnungen.addActionListener(this);
		menuItemMonatsrechnungen
				.setActionCommand(MENUE_ACTION_MONATSRECHNUNGEN_ERSTELLEN);
		extras.add(menuItemMonatsrechnungen);

		if (DelegateFactory.getInstance().getTheJudgeDelegate()
				.hatRecht(RechteFac.RECHT_FB_CHEFBUCHHALTER)) {

			WrapperMenuItem menuItemZahlungsplan = new WrapperMenuItem(
					LPMain.getTextRespectUISPr("rech.extras.zahlungsplan"),
					RechteFac.RECHT_RECH_RECHNUNG_CUD);
			menuItemZahlungsplan.addActionListener(this);
			menuItemZahlungsplan.setActionCommand(MENUE_ACTION_ZAHLUNGSPLAN);
			extras.add(menuItemZahlungsplan);
		}

		wmb.add(extras, 3);

		WrapperMenuItem menuItemDateiDrucken = new WrapperMenuItem(
				LPMain.getTextRespectUISPr("lp.menu.drucken"), null);
		menuItemDateiDrucken.addActionListener(this);
		menuItemDateiDrucken.setActionCommand(MENUE_ACTION_DATEI_DRUCKEN);
		modul.add(menuItemDateiDrucken, 0);

		WrapperMenuItem menuItemDateiZahlscheinDrucken = new WrapperMenuItem(
				LPMain.getTextRespectUISPr("lp.menu.drucken.zahlschein"), null);
		menuItemDateiZahlscheinDrucken.addActionListener(this);
		menuItemDateiZahlscheinDrucken
				.setActionCommand(MENUE_ACTION_DATEI_ZAHLSCHEIN_DRUCKEN);
		modul.add(menuItemDateiZahlscheinDrucken, 1);
		// Alle drucken
		JMenu menuDateiAngelegteDrucken = new WrapperMenu(
				"rech.alleangelegtendrucken", this);

		WrapperMenuItem menuItemDateiAngelegteDruckenAlle = new WrapperMenuItem(
				LPMain.getTextRespectUISPr("label.alle"), null);
		menuItemDateiAngelegteDruckenAlle.addActionListener(this);
		menuItemDateiAngelegteDruckenAlle
				.setActionCommand(MENUE_ACTION_DATEI_ANGELEGTE_DRUCKEN_ALLE);
		menuDateiAngelegteDrucken.add(menuItemDateiAngelegteDruckenAlle);
		WrapperMenuItem menuItemDateiAngelegteDruckenKst = new WrapperMenuItem(
				LPMain.getTextRespectUISPr("lp.einekostenstelle"), null);
		menuItemDateiAngelegteDruckenKst.addActionListener(this);
		menuItemDateiAngelegteDruckenKst
				.setActionCommand(MENUE_ACTION_DATEI_ANGELEGTE_DRUCKEN_KST);
		menuDateiAngelegteDrucken.add(menuItemDateiAngelegteDruckenKst);

		modul.add(menuDateiAngelegteDrucken, 2);
		modul.add(new JSeparator(), 3);

		WrapperMenuItem menuItemDateiMahnen = new WrapperMenuItem(
				LPMain.getTextRespectUISPr("rech.mahnen"),
				RechteFac.RECHT_RECH_RECHNUNG_CUD);
		menuItemDateiMahnen.addActionListener(this);
		menuItemDateiMahnen.setActionCommand(MENUE_ACTION_DATEI_MAHNEN);
		modul.add(menuItemDateiMahnen, 4);

		JMenu jMenuExport = new WrapperMenu("rech.export", this);
		modul.add(jMenuExport, 5);

		// Menue Bearbeiten
		JMenu jmBearbeiten = (JMenu) wmb
				.getComponent(WrapperMenuBar.MENU_BEARBEITEN);
		WrapperMenuItem menueItemVertreterAendern = null;
		menueItemVertreterAendern = new WrapperMenuItem(
				LPMain.getTextRespectUISPr("rech.vertreteraendern"),
				RechteFac.RECHT_RECH_RECHNUNG_CUD);
		menueItemVertreterAendern.addActionListener(this);
		menueItemVertreterAendern
				.setActionCommand(MENUE_ACTION_BEARBEITEN_VERTRETER_AENDERN);
		jmBearbeiten.add(menueItemVertreterAendern, 0);

		WrapperMenuItem menueItemBeleguebernahme = null;
		menueItemBeleguebernahme = new WrapperMenuItem(
				LPMain.getTextRespectUISPr("finanz.beleguebernahme"),
				RechteFac.RECHT_FB_CHEFBUCHHALTER);
		menueItemBeleguebernahme.addActionListener(this);
		menueItemBeleguebernahme
				.setActionCommand(MENUE_ACTION_BEARBEITEN_BELEGUEBERNAHME);
		jmBearbeiten.add(menueItemBeleguebernahme, 0);

		WrapperMenuItem menuItemBearbeitenMahnsperreSetzen = new WrapperMenuItem(
				LPMain.getTextRespectUISPr("lp.mahnsperrebis"),
				RechteFac.RECHT_RECH_RECHNUNG_CUD);
		menuItemBearbeitenMahnsperreSetzen.addActionListener(this);
		menuItemBearbeitenMahnsperreSetzen
				.setActionCommand(MENUE_ACTION_BEARBEITEN_MAHNSPERRE_SETZEN);
		jmBearbeiten.add(menuItemBearbeitenMahnsperreSetzen, 0);
		if (!LPMain
				.getInstance()
				.getDesktop()
				.darfAnwenderAufModulZugreifen(
						LocaleFac.BELEGART_FINANZBUCHHALTUNG)) {

			WrapperMenuItem menuItemBearbeitenManuellErledigen = new WrapperMenuItem(
					LPMain.getTextRespectUISPr("lp.menu.menuellerledigen"),
					RechteFac.RECHT_RECH_RECHNUNG_CUD);
			menuItemBearbeitenManuellErledigen.addActionListener(this);
			menuItemBearbeitenManuellErledigen
					.setActionCommand(MENUE_ACTION_BEARBEITEN_MANUELL_ERLEDIGEN);
			jmBearbeiten.add(menuItemBearbeitenManuellErledigen, 0);
		}

		WrapperMenuItem menuItemDateiWAExport = new WrapperMenuItem(
				LPMain.getTextRespectUISPr("rech.warenausgang"),
				RechteFac.RECHT_RECH_RECHNUNG_CUD);
		menuItemDateiWAExport.addActionListener(this);
		menuItemDateiWAExport
				.setActionCommand(MENUE_ACTION_WARENAUSGANGSJOURNAL_EXPORT);
		jMenuExport.add(menuItemDateiWAExport);

		ParametermandantDto parameter = (ParametermandantDto) DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getParametermandant(
						ParameterFac.PARAMETER_EINZELRECHNUNG_EXPORTPFAD,
						ParameterFac.KATEGORIE_RECHNUNG,
						LPMain.getTheClient().getMandant());

		if (parameter.getCWert() != null
				&& parameter.getCWert().trim().length() > 0) {
			WrapperMenuItem menuItemDateiEinzelrechungsexport = new WrapperMenuItem(
					LPMain.getTextRespectUISPr("rech.einzelrechnungsexport"),
					RechteFac.RECHT_RECH_RECHNUNG_CUD);
			menuItemDateiEinzelrechungsexport.addActionListener(this);
			menuItemDateiEinzelrechungsexport
					.setActionCommand(MENUE_ACTION_EINZELRECHNUNG_EXPORT);
			jMenuExport.add(menuItemDateiEinzelrechungsexport);

			WrapperMenuItem menuItemDateiEinzelrechungsexportVerdichtet = new WrapperMenuItem(
					LPMain.getTextRespectUISPr("rech.einzelrechnungsexport.verdichtet"),
					RechteFac.RECHT_RECH_RECHNUNG_CUD);
			menuItemDateiEinzelrechungsexportVerdichtet.addActionListener(this);
			menuItemDateiEinzelrechungsexportVerdichtet
					.setActionCommand(MENUE_ACTION_EINZELRECHNUNG_EXPORT_VERDICHTET);
			jMenuExport.add(menuItemDateiEinzelrechungsexportVerdichtet);

		}

		WrapperMenuItem menuItemAlle = new WrapperMenuItem(
				LPMain.getTextRespectUISPr("rech.allerechnungen"),
				RechteFac.RECHT_LP_FINANCIAL_INFO_TYP_1);
		menuItemAlle.addActionListener(this);
		menuItemAlle.setActionCommand(MENUE_ACTION_ALLE_RECHNUNGEN);
		journal.add(menuItemAlle);

		WrapperMenuItem menuItemNOffene = new WrapperMenuItem(
				LPMain.getTextRespectUISPr("rech.offenerechnungen"),
				RechteFac.RECHT_LP_FINANCIAL_INFO_TYP_1);
		menuItemNOffene.addActionListener(this);
		menuItemNOffene.setActionCommand(MENUE_ACTION_OFFENE_RECHNUNGEN);
		journal.add(menuItemNOffene);

		WrapperMenuItem menuItemZahlungsjournal = new WrapperMenuItem(
				LPMain.getTextRespectUISPr("rech.zahlungseingang"),
				RechteFac.RECHT_LP_FINANCIAL_INFO_TYP_1);
		menuItemZahlungsjournal.addActionListener(this);
		menuItemZahlungsjournal.setActionCommand(MENUE_ACTION_ZAHLUNGSJOURNAL);
		journal.add(menuItemZahlungsjournal);

		WrapperMenuItem menuItemUmsatz = new WrapperMenuItem(
				LPMain.getTextRespectUISPr("rech.umsatz"),
				RechteFac.RECHT_LP_FINANCIAL_INFO_TYP_1);
		menuItemUmsatz.addActionListener(this);
		menuItemUmsatz.setActionCommand(MENUE_ACTION_UMSATZ);
		journal.add(menuItemUmsatz);
		menuItemUmsatz.setEnabled(bDarfPreiseSehen);

		WrapperMenuItem menuItemWarenausgang = new WrapperMenuItem(
				LPMain.getTextRespectUISPr("rech.warenausgang"),
				RechteFac.RECHT_LP_FINANCIAL_INFO_TYP_1);
		menuItemWarenausgang.addActionListener(this);
		menuItemWarenausgang.setActionCommand(MENUE_ACTION_WARENAUSGANSJOURNAL);
		journal.add(menuItemWarenausgang);

		WrapperMenuItem menuItemZM = new WrapperMenuItem(
				LPMain.getTextRespectUISPr("rech.rechnung.report.zm"),
				RechteFac.RECHT_LP_FINANCIAL_INFO_TYP_1);
		menuItemZM.addActionListener(this);
		menuItemZM.setActionCommand(MENUE_ACTION_ZM);
		journal.add(menuItemZM);

		return wmb;
	}

	protected void lPActionEvent(ActionEvent e) throws Throwable {
		super.lPActionEvent(e);
		if (e.getActionCommand().equals(MENUE_ACTION_NEU_DATUM)) {
			getInternalFrame()
					.showPanelDialog(
							new PanelDialogNeuDatum(
									getInternalFrame(),
									LPMain.getTextRespectUISPr("rechnung.menu.extras.neudatum"),
									((InternalFrameRechnung) getInternalFrame()).neuDatum));
		}
		if (e.getActionCommand().equals(MENUE_ACTION_ZAHLUNGSPLAN)) {
			if (getRechnungDto() != null && getRechnungDto().getIId() != null) {
				DialogMtlZahlbetrag d = new DialogMtlZahlbetrag(
						getRechnungDto().getIId(), this);
				LPMain.getInstance().getDesktop()
						.platziereDialogInDerMitteDesFensters(d);
				d.setVisible(true);
			}
		}
		if (e.getActionCommand().equals(MENUE_ACTION_WIEDERHOLENDE_AUFTRAEGE)) {
			verrechneWiederholendeAuftraege(((InternalFrameRechnung) getInternalFrame())
					.getNeuDatum());
		} else if (e.getActionCommand().equals(
				MENUE_ACTION_MONATSRECHNUNGEN_ERSTELLEN)) {
			DelegateFactory.getInstance().getRechnungDelegate()
					.createMonatsrechnungen();
		} else if (e.getActionCommand().equals(MENUE_ACTION_DATEI_MAHNEN)) {
			if (this.getRechnungDto() != null) {
				if (this.getRechnungDto().getStatusCNr()
						.equals(RechnungFac.STATUS_ANGELEGT)) {
					DialogFactory.showModalDialog(
							LPMain.getTextRespectUISPr("lp.hint"),
							"Die Rechnung ist noch nicht aktiviert");
				} else if (this.getRechnungDto().getStatusCNr()
						.equals(RechnungFac.STATUS_BEZAHLT)) {
					DialogFactory.showModalDialog(
							LPMain.getTextRespectUISPr("lp.hint"),
							"Die Rechnung ist bereits bezahlt");
				} else if (this.getRechnungDto().getStatusCNr()
						.equals(RechnungFac.STATUS_STORNIERT)) {
					DialogFactory.showModalDialog(
							LPMain.getTextRespectUISPr("lp.hint"),
							"Die Rechnung ist storniert");
				} else {
					getInternalFrame().showReportKriterien(
							new ReportMahnung(getInternalFrame(), getAktuellesPanel(), "Mahnen: "
									+ getTitle(), getRechnungDto(), super
									.getPanelQueryRechnung()));
				}
			}
		} else if (e.getActionCommand().equals(MENUE_ACTION_ALLE_RECHNUNGEN)) {
			getInternalFrame().showReportKriterien(
					new ReportRechnungAlleRechnungen(getInternalFrame(), LPMain
							.getTextRespectUISPr("rech.allerechnungen")));
		} else if (e.getActionCommand().equals(MENUE_ACTION_ZM)) {
			getInternalFrame()
					.showReportKriterien(
							new ReportZusammenfassendeMeldung(
									getInternalFrame(),
									LPMain.getTextRespectUISPr("rech.rechnung.report.zm")));
		} else if (e.getActionCommand()
				.equals(MENUE_ACTION_WARENAUSGANSJOURNAL)) {
			getInternalFrame().showReportKriterien(
					new ReportWarenausgangsjournal(getInternalFrame(), LPMain
							.getTextRespectUISPr("rech.warenausgangsjournal")));
		} else if (e.getActionCommand().equals(MENUE_ACTION_OFFENE_RECHNUNGEN)) {
			getInternalFrame().showReportKriterien(
					new ReportRechnungOffene(getInternalFrame(), LPMain
							.getTextRespectUISPr("rech.offenerechnungen")));
		} else if (e.getActionCommand().equals(MENUE_ACTION_ZAHLUNGSJOURNAL)) {
			getInternalFrame()
					.showReportKriterien(
							new ReportRechnungZahlungsjournal(
									getInternalFrame(),
									LPMain.getTextRespectUISPr("rech.zahlungseingang")));
		} else if (e.getActionCommand().equals(MENUE_ACTION_UMSATZ)) {
			getInternalFrame().showReportKriterien(
					new ReportRechnungUmsatz(getInternalFrame(), LPMain
							.getTextRespectUISPr("rech.umsatz")));
		} else if (e.getActionCommand().equalsIgnoreCase(
				MENUE_ACTION_WARENAUSGANGSJOURNAL_EXPORT)) {
			getInternalFrame().showPanelDialog(
					new PanelDialogWAExport(getInternalFrame(),
							"Warenausgang Export"));
		} else if (e.getActionCommand().equalsIgnoreCase(
				MENUE_ACTION_EINZELRECHNUNG_EXPORT)) {
			dialogQueryEinzelrechnungsexportFromListe(false);
		} else if (e.getActionCommand().equalsIgnoreCase(
				MENUE_ACTION_EINZELRECHNUNG_EXPORT_VERDICHTET)) {
			dialogQueryEinzelrechnungsexportFromListe(true);
		} else if (e.getActionCommand().equals(
				MENUE_ACTION_BEARBEITEN_MAHNSPERRE_SETZEN)) {
			if (getRechnungDto() != null) {
				getInternalFrame().showPanelDialog(
						new PanelDialogMahnsperreBis(getInternalFrame(), LPMain
								.getTextRespectUISPr("lp.mahnsperrebis"), this
								.getRechnungDto()));
			}
		} else if (e.getActionCommand().equals(
				MENUE_ACTION_BEARBEITEN_BELEGUEBERNAHME)) {
			DelegateFactory.getInstance().getRechnungDelegate()
					.verbucheRechnungNeu(this.getRechnungDto().getIId());

		} else if (e.getActionCommand().equals(
				MENUE_ACTION_DATEI_ANGELEGTE_DRUCKEN_ALLE)) {
			printAlleAngelegtenRechnungen(null);
		} else if (e.getActionCommand().equals(
				MENUE_ACTION_DATEI_ANGELEGTE_DRUCKEN_KST)) {
			// zuerst die Kostenstelle waehlen
			dialogQueryKostenstelle();
		}
	}

	private void dialogQueryEinzelrechnungsexportFromListe(boolean bVerdichtet)
			throws Throwable {

		FilterKriterium[] filters = new FilterKriterium[4];

		filters[0] = SystemFilterFactory.getInstance().createFKMandantCNr()[0];
		filters[1] = new FilterKriterium(
				RechnungFac.FLR_RECHNUNG_FLRRECHNUNGART + "."
						+ RechnungFac.FLR_RECHNUNGART_RECHNUNGTYP_C_NR, true,
				"'" + RechnungFac.RECHNUNGTYP_RECHNUNG + "'",
				FilterKriterium.OPERATOR_EQUAL, false);
		filters[2] = new FilterKriterium(RechnungFac.FLR_RECHNUNG_STATUS_C_NR,
				true, "('" + RechnungFac.STATUS_STORNIERT + "','"
						+ RechnungFac.STATUS_ANGELEGT + "')",
				FilterKriterium.OPERATOR_NOT_IN, false);
		filters[3] = new FilterKriterium(
				RechnungFac.FLR_RECHNUNG_T_FIBUUEBERNAHME, true, "",
				FilterKriterium.OPERATOR_IS + " "
						+ FilterKriterium.OPERATOR_NULL, false);

		if (bVerdichtet) {
			panelQueryFLREizelrechnungsexportVerdichtet = new PanelQueryFLR(
					null, filters, QueryParameters.UC_ID_RECHNUNG, null,
					getInternalFrame(), "Liste der Rechnungen");

			panelQueryFLREizelrechnungsexportVerdichtet
					.befuellePanelFilterkriterienDirekt(RechnungFilterFactory
							.getInstance().createFKDRechnungnummer(),
							RechnungFilterFactory.getInstance()
									.createFKDKundename());
			panelQueryFLREizelrechnungsexportVerdichtet
					.addDirektFilter(RechnungFilterFactory.getInstance()
							.createFKDKundestatistikadresse());
			panelQueryFLREizelrechnungsexportVerdichtet
					.setMultipleRowSelectionEnabled(true);
			panelQueryFLREizelrechnungsexportVerdichtet
					.setSelectedId(getRechnungDto().getIId());
			new DialogQuery(panelQueryFLREizelrechnungsexportVerdichtet);
		} else {
			panelQueryFLREizelrechnungsexport = new PanelQueryFLR(null,
					filters, QueryParameters.UC_ID_RECHNUNG, null,
					getInternalFrame(), "Liste der Rechnungen");

			panelQueryFLREizelrechnungsexport
					.befuellePanelFilterkriterienDirekt(RechnungFilterFactory
							.getInstance().createFKDRechnungnummer(),
							RechnungFilterFactory.getInstance()
									.createFKDKundename());
			panelQueryFLREizelrechnungsexport
					.addDirektFilter(RechnungFilterFactory.getInstance()
							.createFKDKundestatistikadresse());
			panelQueryFLREizelrechnungsexport
					.setMultipleRowSelectionEnabled(true);
			panelQueryFLREizelrechnungsexport.setSelectedId(getRechnungDto()
					.getIId());
			new DialogQuery(panelQueryFLREizelrechnungsexport);
		}

	}

	/**
	 * Alle angelegten RE drucken.
	 *
	 * @param kostenstelleIId
	 *            Einschraenkung auf eine Kostenstelle. wenn null, dann alle.
	 * @throws Throwable
	 */
	private void printAlleAngelegtenRechnungen(Integer kostenstelleIId)
			throws Throwable {
		// alle angelegten Rechnungen holen
		ArrayList<Integer> a = DelegateFactory.getInstance()
				.getRechnungDelegate().getAngelegteRechnungen();
		// SP1660
		boolean bKundenMitMonatsrechnungBeruecksichtigen = DialogFactory
				.showModalJaNeinDialog(
						getInternalFrame(),
						LPMain.getTextRespectUISPr("rech.angelegtedrucken.monatsrechnungberuecksichtigen"),
						LPMain.getTextRespectUISPr("lp.frage"));

		try {
			for (Iterator<Integer> iter = a.iterator(); iter.hasNext();) {
				Integer item = (Integer) iter.next();
				RechnungDto reDto = DelegateFactory.getInstance()
						.getRechnungDelegate().rechnungFindByPrimaryKey(item);

				// wenn Alle Kst oder nur eine bestimmte
				if (kostenstelleIId == null
						|| reDto.getKostenstelleIId().equals(kostenstelleIId)) {
					// Kunde holen
					KundeDto kdDto = DelegateFactory.getInstance()
							.getKundeDelegate()
							.kundeFindByPrimaryKey(reDto.getKundeIId());

					if (bKundenMitMonatsrechnungBeruecksichtigen == true
							&& Helper.short2boolean(kdDto.getBMonatsrechnung()) == true) {
						continue;
					}

					// Konditionen pruefen
					if (pruefeKonditionen(reDto)) {
						// Die Rechnung muss Positionen haben
						if (DelegateFactory.getInstance().getRechnungDelegate()
								.hatRechnungPositionen(reDto.getIId())
								.booleanValue()) {
							// Aktivieren
							PanelReportKriterien krit = null;
							try {
								// DruckPanel instantiieren
								krit = new PanelReportKriterien(
										getInternalFrame(),
										new ReportRechnung(
												getInternalFrame(), iter.hasNext()?null:getAktuellesPanel(),
												reDto, kdDto, ""), "",
										kdDto.getPartnerDto(),
										reDto.getAnsprechpartnerIId(), false,
										false, false);
								// jetzt das tatsaechliche Drucken
								krit.druckeArchiviereUndSetzeVersandstatusEinesBelegs();
							} catch (JRException ex) {
								int iChoice = JOptionPane.YES_OPTION;
								myLogger.error(ex.getLocalizedMessage(), ex);
								String sMsg = LPMain
										.getTextRespectUISPr("lp.drucken.reportkonntenichtgedrucktwerden")
										+ " " + reDto.getCNr();
								Object[] options = {
										LPMain.getTextRespectUISPr("lp.druckerror.wiederholen"),
										LPMain.getTextRespectUISPr("lp.druckerror.\u00FCberspringen"),
										LPMain.getTextRespectUISPr("lp.abbrechen"), };
								iChoice = DialogFactory.showModalDialog(
										this.getInternalFrame(), sMsg,
										LPMain.getTextRespectUISPr("lp.error"),
										options, options[0]);
								if (iChoice == 0) {
									Thread.sleep(5000);
									krit.druckeArchiviereUndSetzeVersandstatusEinesBelegs();
								} else if (iChoice == 1) {
									DelegateFactory
											.getInstance()
											.getRechnungDelegate()
											.setRechnungStatusAufAngelegt(
													reDto.getIId());
								} else if (iChoice == 2) {
									DelegateFactory
											.getInstance()
											.getRechnungDelegate()
											.setRechnungStatusAufAngelegt(
													reDto.getIId());
									break;
								}
							} catch (Throwable ex) {
								// Falls es beim Drucken zu einem Fehler kommt
								// -> Aktivierung zuruecknehmen
								DelegateFactory
										.getInstance()
										.getRechnungDelegate()
										.setRechnungStatusAufAngelegt(
												reDto.getIId());
							}
						} else {
							DialogFactory
									.showModalDialog(
											LPMain.getTextRespectUISPr("lp.hint"),
											LPMain.getTextRespectUISPr("rech.keinepositionen")
													+ "\n" + reDto.getCNr());
						}
					}
				}
			}
		}
		// Danach immer ein refresh
		finally {
			getPanelAuswahl().eventYouAreSelected(false);
		}
	}

	private void verrechneWiederholendeAuftraege(java.sql.Date dNeuDatum)
			throws Throwable {
		AuftragDto[] auftraege = DelegateFactory
				.getInstance()
				.getAuftragDelegate()
				.auftragFindByMandantCNrAuftragartCNrStatusCNr(
						LPMain.getTheClient().getMandant(),
						AuftragServiceFac.AUFTRAGART_WIEDERHOLEND,
						AuftragServiceFac.AUFTRAGSTATUS_OFFEN);
		if (auftraege == null || auftraege.length == 0) {
			DialogFactory
					.showModalDialog(
							LPMain.getTextRespectUISPr("lp.hint"),
							LPMain.getTextRespectUISPr("rechnung.hinweis.keinewiederholendenauftraege"));
		} else {
			boolean bAnswer = DialogFactory
					.showModalJaNeinDialog(
							getInternalFrame(),
							LPMain.getTextRespectUISPr("rechnung.frage.wiederholendeauftraege"),
							LPMain.getTextRespectUISPr("lp.frage"));

			java.sql.Date dDatum = DialogFactory
					.showDatumseingabeDefaultHeute(LPMain
							.getTextRespectUISPr("auft.wiederholende.vorfaelligkeit"));
			if (dDatum != null
					&& dDatum.getTime() < Helper.cutTimestamp(
							new java.sql.Timestamp(System.currentTimeMillis()))
							.getTime()) {
				DialogFactory
						.showModalDialog(
								LPMain.getTextRespectUISPr("lp.error"),
								LPMain.getTextRespectUISPr("auft.wiederholende.vorfaelligkeit.fehler"));
				return;
			}

			if (bAnswer && dDatum != null) {
				// fuer jeden Auftrag die Rechnungen anlegen
				int iAnzahl = 0;
				for (int i = 0; i < auftraege.length; i++) {

					try {
						iAnzahl += DelegateFactory
								.getInstance()
								.getRechnungDelegate()
								.createRechnungenAusWiederholungsauftrag(
										auftraege[i].getIId(), dDatum);
					} catch (ExceptionLP ex) {
						StringBuffer sb = new StringBuffer("");
						if (ex.getAlInfoForTheClient() != null) {
							for (int j = 0; j < ex.getAlInfoForTheClient()
									.size(); j++) {
								sb.append(ex.getAlInfoForTheClient().get(j)
										+ "\n");
							}
						}

						// spezifische Fehler hier behandeln, damits weiter
						// geht
						if (ex.getICode() == EJBExceptionLP.FEHLER_RECHNUNG_VERRECHNUNGSBEGINN_NICHT_DEFINIERT) {
							DialogFactory
									.showModalDialog(
											LPMain.getTextRespectUISPr("lp.error"),
											LPMain.getTextRespectUISPr("rechnung.error.verrechnungsbeginnnichtdefiniert")
													+ sb.toString()
													+ LPMain.getTextRespectUISPr("rechnung.error.fuerdiesenauftragkeinerechnungenangelegt"));
						} else if (ex.getICode() == EJBExceptionLP.FEHLER_RECHNUNG_WIEDERHOLUNGSINTERVALL_NICHT_DEFINIERT) {
							DialogFactory
									.showModalDialog(
											LPMain.getTextRespectUISPr("lp.error"),
											LPMain.getTextRespectUISPr("rechnung.error.whintervallnichtdefiniert")
													+ sb.toString()
													+ LPMain.getTextRespectUISPr("rechnung.error.fuerdiesenauftragkeinerechnungenangelegt"));
						} else if (ex.getICode() == EJBExceptionLP.FEHLER_RECHNUNG_WH_AUFTRAG_ENTHAELT_SNR_BEHAFTETE_ARTIKEL) {
							DialogFactory
									.showModalDialog(
											LPMain.getTextRespectUISPr("lp.error"),
											LPMain.getTextRespectUISPr("rechnung.error.auftragenthaeltsnrartikel")
													+ sb.toString()
													+ LPMain.getTextRespectUISPr("rechnung.error.fuerdiesenauftragkeinerechnungenangelegt"));
						} else if (ex.getICode() == EJBExceptionLP.FEHLER_ZUWENIG_AUF_LAGER) {
							DialogFactory
									.showModalDialog(
											LPMain.getTextRespectUISPr("lp.error"),
											LPMain.getInstance().getMsg(ex)
													+ LPMain.getTextRespectUISPr("auft.auftrag")
													+ " "
													+ auftraege[i].getCNr()
													+ "\n"
													+ LPMain.getTextRespectUISPr("rechnung.error.fuerdiesenauftragkeinerechnungenangelegt"));
						}
						// aller andere weiter werfen
						else {
							throw ex;
						}
					}

				}
				// auf erstes Panel umschalten und dieses aktalisieren
				setSelectedComponent(getPanelAuswahl());
				getPanelAuswahl().eventYouAreSelected(false);
				// Dialog anzeigen: ... Rechnungen angelegt
				DialogFactory
						.showModalDialog(
								LPMain.getTextRespectUISPr("lp.hint"),
								iAnzahl
										+ " "
										+ LPMain.getTextRespectUISPr("rech.rechnungenangelegt"));
			}
		}
	}

	private PanelQuery getPanelQueryMahnungen(boolean bNeedInstantiationIfNull)
			throws Throwable {
		if (panelQueryMahnungen == null && bNeedInstantiationIfNull) {
			String[] aWhichButtonIUseMahnungen = {};

			panelQueryMahnungen = new PanelQuery(
					null,
					null,
					QueryParameters.UC_ID_MAHNUNG,
					aWhichButtonIUseMahnungen,
					getInternalFrame(),
					LPMain.getTextRespectUISPr("rechnung.tab.oben.mahnungen.title"),
					true);
			this.setComponentAt(iIDX_MAHNUNGEN, panelQueryMahnungen);
		}
		return panelQueryMahnungen;
	}

	private PanelRechnungKonditionen getPanelDetailKonditionen(
			boolean bNeedInstantiationIfNull) throws Throwable {
		if (panelDetailKonditionen == null && bNeedInstantiationIfNull) {
			panelDetailKonditionen = new PanelRechnungKonditionen(
					getInternalFrame(),
					LPMain.getTextRespectUISPr("rechnung.tab.oben.konditionen.title"),
					null, this);
			this.setComponentAt(iIDX_KONDITIONEN, panelDetailKonditionen);
		}
		return panelDetailKonditionen;
	}

	private PanelTabelle getPanelTabelleRechnungUmsatz(
			boolean bNeedInstantiationIfNull) throws Throwable {
		if (ptRechnungUmsatz == null && bNeedInstantiationIfNull) {
			ptRechnungUmsatz = new PanelTabelleRechnungUmsatz(
					QueryParameters.UC_ID_RECHNUNG_UMSATZ,
					LPMain.getTextRespectUISPr("lp.umsatzuebersicht"),
					getInternalFrame());
		}
		return ptRechnungUmsatz;
	}

	private PanelDialogKriterienRechnungUmsatz getPanelDialogRechnungUmsatz(
			boolean bNeedInstantiationIfNull) throws Throwable {
		if (pdKriterienRechnungUmsatz == null && bNeedInstantiationIfNull) {
			pdKriterienRechnungUmsatz = new PanelDialogKriterienRechnungUmsatz(
					getInternalFrame(),
					LPMain.getTextRespectUISPr("lp.kriterienumsatzuebersicht"),
					(TabbedPaneRechnung) this);
		}
		return pdKriterienRechnungUmsatz;
	}

	protected void setRechnungDto(RechnungDto rechnungDto) throws Throwable {
		super.setRechnungDto(rechnungDto);

		if (getPanelDetailKonditionen(false) != null
				&& getRechnungDto() != null) {
			getPanelDetailKonditionen(true).setKeyWhenDetailPanel(
					getRechnungDto().getIId());
		}
	}

	private PanelSplit getPanelSplitSichtLieferschein() throws Throwable {
		if (lsPositionen == null) {
			lsPositionenBottom = new PanelRechnungSichtLieferschein(
					getInternalFrame(),
					LPMain.getTextRespectUISPr("ls.title.panel.positionen"),
					null, this); // default belegung, eventuell gibt es noch
			// keine position

			FilterKriterium[] filtersPos = LieferscheinFilterFactory
					.getInstance().createFKFlrlieferscheiniid(
							getLieferscheinDto().getIId());

			String[] aWhichButtonIUse = {};

			lsPositionenTop = new PanelQuery(null, filtersPos,
					QueryParameters.UC_ID_LIEFERSCHEINPOSITION,
					aWhichButtonIUse, getInternalFrame(),
					LPMain.getTextRespectUISPr("ls.title.panel.positionen"),
					true);

			lsPositionen = new PanelSplit(getInternalFrame(),
					lsPositionenBottom, lsPositionenTop, 150);

			setComponentAt(iIDX_SICHTLIEFERSCHEIN, lsPositionen);
		}
		return lsPositionen;
	}

	private void dialogQueryKostenstelle() throws Throwable {
		panelQueryFLRKostenstelle = SystemFilterFactory.getInstance()
				.createPanelFLRKostenstelle(getInternalFrame(), false, false);
		new DialogQuery(panelQueryFLRKostenstelle);
	}

	private PanelQuery getPanelQueryAuftraege() throws Throwable {
		if (bAuftragRechnung && lsAuftraege == null) {
			FilterKriterium[] filtersPos = RechnungFilterFactory.getInstance()
					.createFKAuftraegeEinerRechnung(
							(getRechnungDto() != null) ? getRechnungDto()
									.getIId() : -1);

			String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW };

			lsAuftraege = new PanelQueryAuftraege(null, filtersPos,
					QueryParameters.UC_ID_AUFTRAEGE_EINER_RECHNUNG,
					aWhichButtonIUse, getInternalFrame(),
					LPMain.getTextRespectUISPr("ls.title.panel.auftraege"),
					true);
			setComponentAt(iIDX_RECHNUNGAUFTRAEGE, lsAuftraege);
		}
		return lsAuftraege;
	}

	private void dialogQueryAuftragFromListeZusatz() throws Throwable {
		// Der muss die gleichen Adressen haben wie der Hauptauftrag
		if (getRechnungDto().getAuftragIId() != null) {
			AuftragDto auftragDtoHA = DelegateFactory.getInstance()
					.getAuftragDelegate()
					.auftragFindByPrimaryKey(getRechnungDto().getAuftragIId());
			// die Auftraege, die schon in der Tabelle stehen, sollen
			// rausgefiltert werden
			Integer[] auftragIIdNA = new Integer[getPanelQueryAuftraege()
					.getTable().getRowCount() + 1];
			// Auch der Kopfauftrag
			auftragIIdNA[0] = getRechnungDto().getAuftragIId();
			// alle auftrag-ids aus der Tabelle
			for (int i = 0; i < getPanelQueryAuftraege().getTable()
					.getRowCount(); i++) {
				auftragIIdNA[i + 1] = (Integer) getPanelQueryAuftraege()
						.getTable().getValueAt(i, 0);
			}
			FilterKriterium[] fk = LieferscheinFilterFactory.getInstance()
					.createFKPanelQueryFLRAuftragAuswahl(
							auftragDtoHA.getKundeIIdLieferadresse(),
							auftragDtoHA.getKundeIIdRechnungsadresse(),
							auftragDtoHA.getKundeIIdAuftragsadresse(),
							auftragIIdNA);
			panelQueryFLRAuftragauswahlZusatz = AuftragFilterFactory
					.getInstance().createPanelFLRAuftrag(getInternalFrame(),
							true, false, fk);
			new DialogQuery(panelQueryFLRAuftragauswahlZusatz);
		}
	}

	protected final PanelRechnungPositionenSichtAuftrag getPanelDetailPositionenSichtAuftrag(
			boolean bNeedInstantiationIfNull) throws Throwable {
		if (bAuftragRechnung && lsPositionenSichtAuftragBottom == null
				&& bNeedInstantiationIfNull) {
			lsPositionenSichtAuftragBottom = new PanelRechnungPositionenSichtAuftrag(
					getInternalFrame(),
					LPMain.getTextRespectUISPr("ls.title.panel.sichtauftrag"),
					null, getRechnungDto().getLagerIId(), this);
		}
		return lsPositionenSichtAuftragBottom;
	}

	protected final PanelQuery getPanelQueryPositionenSichtAuftrag(
			boolean bNeedInstantiationIfNull) throws Throwable {
		if (bAuftragRechnung && lsPositionenSichtAuftragTop == null
				&& bNeedInstantiationIfNull) {
			FilterKriterium[] filtersPos = RechnungFilterFactory.getInstance()
					.createFKRechnungSichtAuftrag(getRechnungDto(),
							getAuftragDtoSichtAuftrag().getIId());

			lsPositionenSichtAuftragTop = new PanelQuery(null, filtersPos,
					QueryParameters.UC_ID_LIEFERSCHEINPOSITIONSICHTAUFTRAG,
					null, getInternalFrame(),
					LPMain.getTextRespectUISPr("ls.title.panel.sichtauftrag"),
					true);
			lsPositionenSichtAuftragTop
					.befuelleFilterkriteriumSchnellansicht(RechnungFilterFactory
							.getInstance().createFKSichtAuftragOffeneAnzeigen());
			FilterKriteriumDirekt fkDirekt1 = LieferscheinFilterFactory
					.getInstance().createFKDArtikelnummer();

			lsPositionenSichtAuftragTop.befuellePanelFilterkriterienDirekt(
					fkDirekt1, null);

			lsPositionenSichtAuftragTop
					.getCbSchnellansicht()
					.setText(
							LPMain.getTextRespectUISPr("ls.sichtauftrag.offeneanzeigen"));
			lsPositionenSichtAuftragTop.createAndSaveAndShowButton(
					"/com/lp/client/res/auftrag16x16.png",
					LPMain.getTextRespectUISPr("ls.allepositionenausauftrag"),
					MY_OWN_NEW_ALLE_POSITIONEN_AUS_AUFTRAG_UEBERNEHMEN,
					RechteFac.RECHT_RECH_RECHNUNG_CUD);
		}
		return lsPositionenSichtAuftragTop;
	}

	private PanelSplit getPanelSplitSichtAuftrag() throws Throwable {
		if (bAuftragRechnung && lsPositionenSichtAuftrag == null) {
			lsPositionenSichtAuftrag = new PanelSplit(getInternalFrame(),
					getPanelDetailPositionenSichtAuftrag(true),
					getPanelQueryPositionenSichtAuftrag(true), 140);
			setComponentAt(iIDX_SICHT_AUFTRAG, lsPositionenSichtAuftrag);
		}
		return lsPositionenSichtAuftrag;
	}

	private String getSichtAuftragTitle() {
		String titel = getTitle() + " | " + getAuftragDtoSichtAuftrag().getCNr() ;

		String projekt = getAuftragDtoSichtAuftrag().getCBezProjektbezeichnung() ;
		String bestellung  = getAuftragDtoSichtAuftrag().getCBestellnummer() ;

		if(!Helper.isStringEmpty(projekt)) {
			titel += " " + projekt ;
		}
		if(!Helper.isStringEmpty(bestellung)) {
			if(!Helper.isStringEmpty(projekt)) {
				titel += " ," ;
			}
			titel += " " + bestellung ;
		}

		return titel ;
	}

	private String getSichtLieferscheinTitle() {
		String titel = getTitle() + " | " + getLieferscheinDto().getCNr();

		return titel;
	}

	/**
	 * Aktiviert das Tab Auftr&auml;ge, wenn der Hauptauftrag vorhanden ist
	 *
	 */
	public void enableTabAuftraege() {

		if(bAuftragRechnung) {
			setEnabledAt(iIDX_RECHNUNGAUFTRAEGE, getRechnungDto().getAuftragIId() != null);
		}
	}

	/**
	 * Aktiviert das Tab Sicht Auftrag, wenn der Hauptauftrag vorhanden ist
	 * Bei mehr Auftr&auml;gen muss zuerst einer vom User selektiert worden sein
	 *
	 * @throws Throwable
	 */
	public void enableTabSichtAuftrag() throws Throwable {

		if(bAuftragRechnung) {
			RechnungSichtAuftragDto dto = DelegateFactory.getInstance()
					.getRechnungServiceDelegate().rechnungFindByPrimaryKey(getRechnungDto().getIId());

			setEnabledAt(iIDX_SICHT_AUFTRAG, getRechnungDto().getAuftragIId() != null
					&& (!dto.isMehrAlsHauptAuftrag()
					|| getPanelQueryAuftraege().getTable().getSelectedRow() >= 0));
		}
	}

	/**
	 * Aktiviert das Tab Sicht Lieferschein, wenn unter Positionen ein Lieferschein
	 * ausgew&auml;hlt wurde
	 *
	 * @throws Throwable
	 */
	public void enableTabSichtLieferschein() throws Throwable {

		setEnabledAt(iIDX_SICHTLIEFERSCHEIN, pruefePositionIstEinLieferschein());
	}

	@Override
	protected void panelDetailPositionenSelected(Object key) throws Throwable {
		super.panelDetailPositionenSelected(key);

		enableTabSichtLieferschein();
	}

}
