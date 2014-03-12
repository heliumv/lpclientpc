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
package com.lp.client.eingangsrechnung;

import java.awt.Color;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.math.BigDecimal;
import java.util.Calendar;

import javax.persistence.EntityExistsException;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import javax.swing.event.ChangeEvent;

import com.lp.client.bestellung.BestellungFilterFactory;
import com.lp.client.frame.Defaults;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.LockStateValue;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelDialogKriterien;
import com.lp.client.frame.component.PanelQuery;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.PanelQueryFLRGoto;
import com.lp.client.frame.component.PanelSplit;
import com.lp.client.frame.component.PanelTabelle;
import com.lp.client.frame.component.TabbedPane;
import com.lp.client.frame.component.WrapperMenuBar;
import com.lp.client.frame.component.WrapperMenuItem;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.delegate.WareneingangDelegate;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPButtonAction;
import com.lp.client.pc.LPMain;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.bestellung.service.BestellpositionDto;
import com.lp.server.bestellung.service.BestellungDto;
import com.lp.server.bestellung.service.BestellungFac;
import com.lp.server.bestellung.service.WareneingangDto;
import com.lp.server.bestellung.service.WareneingangspositionDto;
import com.lp.server.eingangsrechnung.service.EingangsrechnungDto;
import com.lp.server.eingangsrechnung.service.EingangsrechnungFac;
import com.lp.server.inserat.service.InseratDto;
import com.lp.server.inserat.service.InseratartikelDto;
import com.lp.server.inserat.service.InseraterDto;
import com.lp.server.partner.service.LieferantDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MwstsatzDto;
import com.lp.server.system.service.WechselkursDto;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.FilterKriteriumDirekt;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

/**
 * <p>
 * Diese Klasse kuemmert sich um die Panels der Eingangsrechnungsverwaltung
 * </p>
 * 
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2008
 * </p>
 * 
 * @author Martin Bluehweis
 * @version $Revision: 1.45 $
 */
public class TabbedPaneEingangsrechnung extends TabbedPane {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private WareneingangDto wareneingangDto = null;

	public Object[] inseratIIds = null;

	public Object[] getInseratIIds() {
		return inseratIIds;
	}

	public void setInseratIIds(Object[] inseratIIds) {
		this.inseratIIds = inseratIIds;
	}

	private PanelQuery panelQueryEingangsrechnung = null;
	private PanelEingangsrechnungKopfdaten panelEingangsrechnungKopfdaten = null;
	private PanelEingangsrechnungKopfdatenZuordnung panelEingangsrechnungKopfdatenZuordnung = null;
	private PanelSplit panelSplitAuftrag = null;
	private PanelQuery panelQueryAuftrag = null;
	private PanelEingangsrechnungAuftragszuordnung panelAuftrag = null;
	private PanelSplit panelSplitInserat = null;
	private PanelQuery panelQueryInserat = null;
	private PanelEingangsrechnungInseratzuordnung panelInserat = null;
	private PanelSplit panelSplitKontierung = null;
	private PanelQuery panelQueryKontierung = null;
	private PanelEingangsrechnungKontierung panelKontierung = null;
	private PanelBasis panelSplitZahlung = null;
	private PanelQuery panelQueryZahlung = null;

	private PanelEingangsrechnungZahlung panelZahlung = null;

	private PanelQuery panelQueryWareneingaenge = null;

	private JLabel lblweDiff = new JLabel();

	private PanelTabelleEingangsrechnungUebersicht panelUebersicht = null;
	private PanelDialogKriterienEingangsrechnungUebersicht panelKriterienUebersicht = null;

	public int IDX_EINGANGSRECHNUNGEN = -1;
	public int iDX_KOPFDATEN = -1;
	public int iDX_ZUORDNUNG = -1;
	public int iDX_AUFTRAGSZUORDNUNG = -1;
	public int iDX_INSERATZUORDNUNG = -1;
	public int iDX_KONTIERUNG = -1;
	public int iDX_ZAHLUNGEN = -1;
	public int iDX_WARENEINGAENGE = -1;
	public int iDX_UEBERSICHT = -1;

	private final static String MENU_ACTION_ALLE = "menu_action_er_alle";
	private final static String MENU_ACTION_FEHLENDE_ZOLLPAPIERE = "menu_action_fehlende_zollpapiere";
	private final static String MENU_ACTION_ERFASSTE_ZOLLPAPIERE = "menu_action_erfasste_zollpapiere";
	private final static String MENU_ACTION_OFFENE = "menu_action_er_offene";
	private final static String MENU_ACTION_ZAHLUNG = "menu_action_er_zahlung";
	private final static String MENU_ACTION_KONTIERUNG = "menu_action_er_kontierung";
	private final static String MENUE_ACTION_BEARBEITEN_MANUELL_ERLEDIGEN = "MENU_ACTION_BEARBEITEN_MANUELL_ERLEDIGEN";
	private final static String MENUE_ACTION_DRUCKEN = "menu_action_er_drucken";
	private final static String MENUE_ACTION_BEARBEITEN_BELEGUEBERNAHME = "MENUE_ACTION_BEARBEITEN_BELEGUEBERNAHME";
	private final static String MENUE_ACTION_BEARBEITEN_MAHNDATEN = "MENUE_ACTION_BEARBEITEN_MAHNDATEN";
	private final static String MENUE_ACTION_BEARBEITEN_WIEDERHOLENDE_ZUSATZKOSTEN_ANLEGEN = "MENUE_ACTION_WIEDERHOLENDE_ZUSATZKOSTEN_ANLEGEN";

	private JLabel inseraterSumme = new JLabel();

	private EingangsrechnungDto eingangsrechnungDto = null;
	private LieferantDto lieferantDto = null;

	private static final String ACTION_SPECIAL_SCAN = "action_special_scan";

	private final static String BUTTON_SCAN = PanelBasis.ACTION_MY_OWN_NEW
			+ ACTION_SPECIAL_SCAN;
	public final static String EXTRA_NEU_AUS_BESTELLUNG = "aus_bestellung";

	public final static String MY_OWN_NEW_AUS_BESTELLUNG = PanelBasis.ACTION_MY_OWN_NEW
			+ EXTRA_NEU_AUS_BESTELLUNG;

	private PanelQueryFLR panelQueryFLRBestellungauswahl = null;
	private boolean bZusatzkosten = false;

	public boolean isBZusatzkosten() {
		return bZusatzkosten;
	}

	public TabbedPaneEingangsrechnung(InternalFrame internalFrameI,
			boolean bZusatzkosten, String title) throws Throwable {
		super(internalFrameI, title);
		this.bZusatzkosten = bZusatzkosten;
		jbInit();
		initComponents();
	}

	protected EingangsrechnungDto getEingangsrechnungDto() {
		return eingangsrechnungDto;
	}

	protected LieferantDto getLieferantDto() {
		return lieferantDto;
	}

	protected Integer getSelectedIIdER() throws Throwable {
		return (Integer) getPanelQueryEingangsrechnung(true).getSelectedId();
	}

	protected void setEingangsrechnungDto(
			EingangsrechnungDto eingangsrechnungDto) throws Throwable {
		this.eingangsrechnungDto = eingangsrechnungDto;
		refreshFilterKontierung();
		refreshFilterZahlung();
		if (bZusatzkosten == false) {
			refreshFilterAuftragszuordnung();
			refreshFilterWareneingaenge();
		}

		if (LPMain.getInstance().getDesktop()
				.darfAnwenderAufModulZugreifen(LocaleFac.BELEGART_INSERAT)) {
			refreshFilterInseratzuordnung();
		}

		// Lieferant holen
		if (getEingangsrechnungDto() != null) {
			// aber nur dann, wenn ich den nicht schon hab
			if (getLieferantDto() == null
					|| !getLieferantDto().getIId().equals(
							eingangsrechnungDto.getLieferantIId())) {
				setLieferantDto(DelegateFactory
						.getInstance()
						.getLieferantDelegate()
						.lieferantFindByPrimaryKey(
								eingangsrechnungDto.getLieferantIId()));
			}
			StringBuffer sTitle = new StringBuffer();
			if (getEingangsrechnungDto().getCNr() != null) {
				sTitle.append(getEingangsrechnungDto().getCNr());
				sTitle.append(" ");
			}
			sTitle.append(getLieferantDto().getPartnerDto()
					.formatFixTitelName1Name2());
			getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_AS_I_LIKE,
					sTitle.toString());
		} else {
			getInternalFrame()
					.setLpTitle(InternalFrame.TITLE_IDX_AS_I_LIKE, "");
		}
		enablePanels();
	}

	protected void setLieferantDto(LieferantDto lieferantDto) {
		this.lieferantDto = lieferantDto;
	}

	private void jbInit() throws Throwable {
		int tabIndex = 0;
		IDX_EINGANGSRECHNUNGEN = tabIndex;

		// Tab 1: Kassenbuecher
		insertTab(LPMain.getTextRespectUISPr("lp.auswahl"), null, null,
				LPMain.getTextRespectUISPr("lp.auswahl"),
				IDX_EINGANGSRECHNUNGEN);
		// Tab 2: Kopfdaten
		tabIndex++;
		iDX_KOPFDATEN = tabIndex;
		insertTab(LPMain.getTextRespectUISPr("lp.kopfdaten"), null, null,
				LPMain.getTextRespectUISPr("lp.kopfdaten"), iDX_KOPFDATEN);
		// Tab 2: Auftragszuordnung
		if (bZusatzkosten == false) {
			tabIndex++;
			iDX_ZUORDNUNG = tabIndex;
			insertTab(LPMain.getTextRespectUISPr("er.zuordnung"), null, null,
					LPMain.getTextRespectUISPr("er.zuordnung"), iDX_ZUORDNUNG);
			// Tab 2: Auftragszuordnung
			// SP1120 -> wenn kein Auftrag, dann abschalten
			if (LPMain.getInstance().getDesktop()
					.darfAnwenderAufModulZugreifen(LocaleFac.BELEGART_AUFTRAG)) {
				tabIndex++;
				iDX_AUFTRAGSZUORDNUNG = tabIndex;
				insertTab(
						LPMain.getTextRespectUISPr("er.tab.oben.auftragszuordnung.title"),
						null,
						null,
						LPMain.getTextRespectUISPr("er.tab.oben.auftragszuordnung.tooltip"),
						iDX_AUFTRAGSZUORDNUNG);
			}
		}

		if (LPMain.getInstance().getDesktop()
				.darfAnwenderAufModulZugreifen(LocaleFac.BELEGART_INSERAT)) {
			tabIndex++;
			iDX_INSERATZUORDNUNG = tabIndex;
			insertTab(
					LPMain.getTextRespectUISPr("er.tab.oben.inseratzuordnung.title"),
					null,
					null,
					LPMain.getTextRespectUISPr("er.tab.oben.inseratzuordnung.title"),
					iDX_INSERATZUORDNUNG);
		}
		// Tab 3: Kontierung
		tabIndex++;
		iDX_KONTIERUNG = tabIndex;
		insertTab(LPMain.getTextRespectUISPr("er.tab.oben.kontierung.title"),
				null, null,
				LPMain.getTextRespectUISPr("er.tab.oben.kontierung.tooltip"),
				iDX_KONTIERUNG);
		// Tab 4: Zahlungen
		tabIndex++;
		iDX_ZAHLUNGEN = tabIndex;
		insertTab(LPMain.getTextRespectUISPr("er.tab.oben.zahlungen.title"),
				null, null,
				LPMain.getTextRespectUISPr("er.tab.oben.zahlungen.tooltip"),
				iDX_ZAHLUNGEN);
		if (bZusatzkosten == false) {
			// Tab 5: Wareneingaenge
			tabIndex++;
			iDX_WARENEINGAENGE = tabIndex;
			insertTab(
					LPMain.getTextRespectUISPr("er.tab.oben.wareneingaenge.title"),
					null,
					null,
					LPMain.getTextRespectUISPr("er.tab.oben.wareneingaenge.tooltip"),
					iDX_WARENEINGAENGE);
		
		}
		
		// Tab 6: Uebersicht
		tabIndex++;
		iDX_UEBERSICHT = tabIndex;
		insertTab(LPMain.getTextRespectUISPr("lp.umsatzuebersicht"), null,
				null, LPMain.getTextRespectUISPr("lp.umsatzuebersicht"),
				iDX_UEBERSICHT);
		setSelectedComponent(getPanelQueryEingangsrechnung(true));
		addChangeListener(this);
		getInternalFrame().addItemChangedListener(this);
	}

	/**
	 * getPanelQueryEingangsrechnung mit extrem lazy loading.
	 * 
	 * @return PanelQuery
	 * @param bNeedInstantiationIfNull
	 *            boolean
	 * @throws Throwable
	 */
	protected PanelQuery getPanelQueryEingangsrechnung(
			boolean bNeedInstantiationIfNull) throws Throwable {
		if (panelQueryEingangsrechnung == null && bNeedInstantiationIfNull) {
			String[] aWhichButtonIUseEingangsrechnung = { PanelBasis.ACTION_NEW };
			FilterKriterium[] filtersEingangsrechnung = EingangsrechnungFilterFactory
					.getInstance().createFKEingangsrechnungAuswahl(
							bZusatzkosten);

			if (bZusatzkosten == true) {
				panelQueryEingangsrechnung = new PanelQuery(null,
						filtersEingangsrechnung,
						QueryParameters.UC_ID_ZUSATZKOSTEN,
						aWhichButtonIUseEingangsrechnung, getInternalFrame(),
						LPMain.getTextRespectUISPr("lp.auswahl"), true);
			} else {
				panelQueryEingangsrechnung = new PanelQuery(null,
						filtersEingangsrechnung,
						QueryParameters.UC_ID_EINGANGSRECHNUNG,
						aWhichButtonIUseEingangsrechnung, getInternalFrame(),
						LPMain.getTextRespectUISPr("lp.auswahl"), true);
			}

			FilterKriteriumDirekt fkDirekt1 = EingangsrechnungFilterFactory
					.getInstance().createFKDEingangsrechnungnummer();
			FilterKriteriumDirekt fkDirekt2 = EingangsrechnungFilterFactory
					.getInstance().createFKDLieferantname();
			panelQueryEingangsrechnung.befuellePanelFilterkriterienDirekt(
					fkDirekt1, fkDirekt2);
			panelQueryEingangsrechnung
					.addDirektFilter(EingangsrechnungFilterFactory
							.getInstance().createFKDTextSuchen());

			panelQueryEingangsrechnung
					.befuelleFilterkriteriumSchnellansicht(EingangsrechnungFilterFactory
							.getInstance()
							.createFKEingangsrechnungSchnellansicht());
			panelQueryEingangsrechnung.addStatusBar();
			setComponentAt(IDX_EINGANGSRECHNUNGEN, panelQueryEingangsrechnung);
			// Button Eingangsrechnung aus Bestellung anlegen
			// Falsch verstandenes Projekt... vll mal zu verwenden (SK)
			/*
			 * panelQueryEingangsrechnung.createAndSaveAndShowButton(
			 * "/com/lp/client/res/shoppingcart_full16x16.png",
			 * LPMain.getTextRespectUISPr
			 * ("lp.tooltip.datenausbestehenderbestellung"),
			 * MY_OWN_NEW_AUS_BESTELLUNG
			 * ,RechteFac.RECHT_ER_EINGANGSRECHNUNG_CUD);
			 */

		}
		return panelQueryEingangsrechnung;
	}

	/**
	 * getPanelQueryAuftrag mit extrem lazy loading.
	 * 
	 * @return PanelQuery
	 * @param bNeedInstantiationIfNull
	 *            boolean
	 * @throws Throwable
	 */
	private PanelQuery getPanelQueryAuftrag(boolean bNeedInstantiationIfNull)
			throws Throwable {
		if (panelQueryAuftrag == null && bNeedInstantiationIfNull) {
			String[] aWhichButtonIUseAuftrag = { PanelBasis.ACTION_NEW };
			panelQueryAuftrag = new PanelQuery(
					null,
					null,
					QueryParameters.UC_ID_EINGANGSRECHNUNG_AUFTRAGSZUORDNUNG,
					aWhichButtonIUseAuftrag,
					getInternalFrame(),
					LPMain.getTextRespectUISPr("er.tab.oben.auftragszuordnung.title"),
					true);
		}
		return panelQueryAuftrag;
	}

	private PanelQuery getPanelQueryInserat(boolean bNeedInstantiationIfNull)
			throws Throwable {
		if (panelQueryInserat == null && bNeedInstantiationIfNull) {
			String[] aWhichButtonIUseAuftrag = { PanelBasis.ACTION_NEW };
			panelQueryInserat = new PanelQuery(
					null,
					null,
					QueryParameters.UC_ID_INSERATER,
					aWhichButtonIUseAuftrag,
					getInternalFrame(),
					LPMain.getTextRespectUISPr("er.tab.oben.inseratzuordnung.title"),
					true);

			panelQueryInserat.getToolBar().getToolsPanelCenter()
					.add(inseraterSumme);

		}
		if (getEingangsrechnungDto() != null
				&& getEingangsrechnungDto().getNBetrag() != null
				&& getEingangsrechnungDto().getNUstBetrag() != null) {
			BigDecimal erBetragNetto = getEingangsrechnungDto().getNBetrag();

			if (!Helper.short2boolean(getEingangsrechnungDto()
					.getBReversecharge())) {
				erBetragNetto = erBetragNetto.subtract(getEingangsrechnungDto()
						.getNUstBetrag());
			}

			BigDecimal bdwertInserate = new BigDecimal(0);
			InseraterDto[] inseraterDtos = DelegateFactory
					.getInstance()
					.getInseratDelegate()
					.inseraterFindByEingangsrechnungIId(
							getEingangsrechnungDto().getIId());
			for (int i = 0; i < inseraterDtos.length; i++) {
				bdwertInserate = bdwertInserate.add(inseraterDtos[i]
						.getNBetrag());

				// SP1387
				bdwertInserate = bdwertInserate.add(DelegateFactory
						.getInstance()
						.getInseratDelegate()
						.berechneWerbeabgabeLFEinesInserates(
								inseraterDtos[i].getInseratIId()));

			}

			inseraterSumme.setText(LPMain
					.getTextRespectUISPr("iv.inserate.summe")
					+ " = "
					+ Helper.formatZahl(bdwertInserate, Defaults.getInstance()
							.getIUINachkommastellenPreiseEK(), LPMain
							.getTheClient().getLocUi()));

			if (erBetragNetto.doubleValue() != bdwertInserate.doubleValue()) {
				inseraterSumme.setForeground(Color.RED);
			} else {
				inseraterSumme.setForeground(Color.BLACK);
			}
		}

		return panelQueryInserat;
	}

	/**
	 * getPanelQueryAuftrag mit extrem lazy loading.
	 * 
	 * @return PanelQuery
	 * @param bNeedInstantiationIfNull
	 *            boolean
	 * @throws Throwable
	 */
	private PanelQuery getPanelQueryKontierung(boolean bNeedInstantiationIfNull)
			throws Throwable {
		if (panelQueryKontierung == null && bNeedInstantiationIfNull) {
			String[] aWhichButtonIUseKontierung = { PanelBasis.ACTION_NEW };
			panelQueryKontierung = new PanelQuery(null, null,
					QueryParameters.UC_ID_EINGANGSRECHNUNG_KONTIERUNG,
					aWhichButtonIUseKontierung, getInternalFrame(),
					LPMain.getTextRespectUISPr("er.tab.oben.kontierung.title"),
					true);
		}
		return panelQueryKontierung;
	}

	/**
	 * getPanelQueryZahlung mit extrem lazy loading.
	 * 
	 * @return PanelQuery
	 * @param bNeedInstantiationIfNull
	 *            boolean
	 * @throws Throwable
	 */
	private PanelQuery getPanelQueryZahlung(boolean bNeedInstantiationIfNull)
			throws Throwable {
		if (panelQueryZahlung == null && bNeedInstantiationIfNull) {
			String[] aWhichButtonIUseZahlung = new String[] { PanelBasis.ACTION_NEW };
			// if
			// (eingangsrechnungDto.getEingangsrechnungartCNr().equals(EingangsrechnungFac.
			// EINGANGSRECHNUNGART_GUTSCHRIFT)) {
			// aWhichButtonIUseZahlung = new String[]{};
			// }
			panelQueryZahlung = new PanelQuery(null, null,
					QueryParameters.UC_ID_EINGANGSRECHNUNG_ZAHLUNG,
					aWhichButtonIUseZahlung, getInternalFrame(),
					LPMain.getTextRespectUISPr("er.tab.oben.zahlungen.title"),
					true);
		}
		if (eingangsrechnungDto != null) {
			if (EingangsrechnungFac.EINGANGSRECHNUNGART_GUTSCHRIFT
					.equals(eingangsrechnungDto.getEingangsrechnungartCNr())) {
				LPButtonAction item = (LPButtonAction) panelQueryZahlung
						.getHmOfButtons().get(PanelBasis.ACTION_NEW);
				item.getButton().setEnabled(false);
			} else {
				LPButtonAction item = (LPButtonAction) panelQueryZahlung
						.getHmOfButtons().get(PanelBasis.ACTION_NEW);
				item.getButton().setEnabled(true);
			}
		}
		return panelQueryZahlung;
	}

	public void lPEventItemChanged(ItemChangedEvent e) throws Throwable {
		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			// da will jemand die Buchungen ansehen
			if (e.getSource() == getPanelQueryEingangsrechnung(false)) {
				Object key = getPanelQueryEingangsrechnung(true)
						.getSelectedId();
				holeEingangsrechnungDto(key);
				getInternalFrame().setKeyWasForLockMe(key + "");
				// nur wechseln wenns auch einen gibt
				if (key != null) {
					setSelectedComponent(getPanelEingangsrechnungKopfdaten(true));
					getPanelEingangsrechnungKopfdaten(true)
							.eventYouAreSelected(false);
				}
			}
			if (e.getSource() == panelQueryFLRBestellungauswahl) {
				Integer iIDBestellungChoosen = (Integer) ((ISourceEvent) e
						.getSource()).getIdSelected();
				if (iIDBestellungChoosen != null) {
					Integer eingangsrechnungIId = DelegateFactory
							.getInstance()
							.getBestellungDelegate()
							.erzeugeEingangsrechnungAusBestellung(
									iIDBestellungChoosen, null);
					getInternalFrame().setKeyWasForLockMe(
							eingangsrechnungIId + "");
					PanelQuery pQHelper = getPanelQueryEingangsrechnung(true);
					holeEingangsrechnungDto(eingangsrechnungIId);
					pQHelper.setSelectedId(eingangsrechnungIId);
					PanelEingangsrechnungKopfdaten pEKHelper = getPanelEingangsrechnungKopfdaten(false);
					setSelectedComponent(pEKHelper);
				}
			}
		} else if (e.getID() == ItemChangedEvent.ITEM_CHANGED) {
			if (e.getSource() == getPanelQueryEingangsrechnung(false)) {
				Object key = getPanelQueryEingangsrechnung(true)
						.getSelectedId();
				if (key == null) {
					getInternalFrame().enableAllOberePanelsExceptMe(this,
							IDX_EINGANGSRECHNUNGEN, false);
				} else {
					getInternalFrame().enableAllOberePanelsExceptMe(this,
							IDX_EINGANGSRECHNUNGEN, true);
				}
				holeEingangsrechnungDto(key);
				getPanelQueryEingangsrechnung(true).updateButtons();
			} else if (e.getSource() == getPanelQueryAuftrag(false)) {
				// hole key
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				getPanelAuftrag(true).setKeyWhenDetailPanel(key);
				getPanelAuftrag(true).eventYouAreSelected(false);
				getPanelQueryAuftrag(true).updateButtons();
			} else if (e.getSource() == getPanelQueryInserat(false)) {
				// hole key
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				getPanelInserat(true).setKeyWhenDetailPanel(key);
				getPanelInserat(true).eventYouAreSelected(false);
				getPanelQueryInserat(true).updateButtons();
			} else if (e.getSource() == getPanelQueryKontierung(false)) {
				// hole key
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				getPanelKontierung(true).setKeyWhenDetailPanel(key);
				getPanelKontierung(true).eventYouAreSelected(false);
				getPanelQueryKontierung(true).updateButtons();
			} else if (e.getSource() == getPanelQueryZahlung(false)) {
				// hole key
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				getPanelZahlung(true).setKeyWhenDetailPanel(key);
				getPanelZahlung(true).eventYouAreSelected(false);
				getPanelQueryZahlung(true).updateButtons();
			} else if (e.getSource() == getPanelQueryWareneingaenge(false)) {
				getPanelQueryWareneingaenge(true).updateButtons();

				// PJ 16469

				lblweDiff.setText("");

				WareneingangDto[] weDtos = DelegateFactory
						.getInstance()
						.getWareneingangDelegate()
						.wareneingangFindByEingangsrechnungIId(
								getEingangsrechnungDto().getIId());
				BigDecimal wertWE = new BigDecimal(0);
				for (int i = 0; i < weDtos.length; i++) {
					wertWE = wertWE.add(DelegateFactory
							.getInstance()
							.getWareneingangDelegate()
							.berechneWertDesWareneingangsInBestellungswaehrung(
									weDtos[i].getIId()));

				}
				BigDecimal nettowertER = Helper.rundeKaufmaennisch(
						getEingangsrechnungDto().getNBetragfw().subtract(
								getEingangsrechnungDto().getNUstBetragfw()), 2);

				lblweDiff.setText(LPMain.getTextRespectUISPr("er.nettowert")
						+ " "
						+ Helper.formatZahl(nettowertER, 2, LPMain
								.getTheClient().getLocUi()));

				if (nettowertER.doubleValue() != Helper.rundeKaufmaennisch(
						wertWE, 2).doubleValue()) {
					lblweDiff.setForeground(Color.RED);
				} else {
					lblweDiff.setForeground(Color.BLACK);
				}

			}
		} else if (e.getID() == ItemChangedEvent.ACTION_UPDATE) {
			if (e.getSource() == getPanelAuftrag(true)) {
				getPanelQueryAuftrag(true).updateButtons(
						new LockStateValue(PanelBasis.LOCK_FOR_NEW));
			} else if (e.getSource() == getPanelInserat(true)) {
				getPanelQueryInserat(true).updateButtons(
						new LockStateValue(PanelBasis.LOCK_FOR_NEW));
			} else if (e.getSource() == panelKontierung) {
				getPanelQueryKontierung(true).updateButtons(
						new LockStateValue(PanelBasis.LOCK_FOR_NEW));
			} else if (e.getSource() == getPanelZahlung(false)) {
				getPanelQueryZahlung(true).updateButtons(
						new LockStateValue(PanelBasis.LOCK_FOR_NEW));
			} else if (e.getSource() instanceof PanelQueryFLRGoto) {
				Integer iIdEingangsrechnung = (Integer) ((ISourceEvent) e
						.getSource()).getIdSelected();
				this.setSelectedIndex(1);
				getPanelQueryEingangsrechnung(true).setSelectedId(
						iIdEingangsrechnung);
				getPanelQueryEingangsrechnung(true).eventYouAreSelected(false);
				getPanelEingangsrechnungKopfdaten(true).eventYouAreSelected(
						false);
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_NEW) {
			if (e.getSource() == getPanelQueryEingangsrechnung(false)) {
				if (getPanelQueryEingangsrechnung(true).getSelectedId() == null) {
					getInternalFrame().enableAllPanelsExcept(true);
				}
				getPanelEingangsrechnungKopfdaten(true).eventActionNew(null,
						true, false);
				setSelectedComponent(getPanelEingangsrechnungKopfdaten(true));
			} else if (e.getSource() == getPanelQueryAuftrag(false)) {
				getPanelAuftrag(true).eventActionNew(e, true, false);
				getPanelAuftrag(true).eventYouAreSelected(false);
				// locknew: 2 den Panels den richtigen lockstatus geben

				LockStateValue lockstateValue = new LockStateValue(null, null,
						PanelBasis.LOCK_FOR_NEW);
				getPanelAuftrag(true).updateButtons(lockstateValue);
				getPanelQueryAuftrag(true).updateButtons(lockstateValue);
			} else if (e.getSource() == getPanelQueryInserat(false)) {
				getPanelInserat(true).eventActionNew(e, true, false);
				getPanelInserat(true).eventYouAreSelected(false);
				// locknew: 2 den Panels den richtigen lockstatus geben

				LockStateValue lockstateValue = new LockStateValue(null, null,
						PanelBasis.LOCK_FOR_NEW);
				getPanelInserat(true).updateButtons(lockstateValue);
				getPanelQueryInserat(true).updateButtons();
			} else if (e.getSource() == getPanelQueryKontierung(false)) {
				EingangsrechnungDto reDto = getEingangsrechnungDto();

				if (reDto.getStatusCNr().equals(
						EingangsrechnungFac.STATUS_TEILBEZAHLT)
						|| reDto.getStatusCNr().equals(
								EingangsrechnungFac.STATUS_ERLEDIGT)) {
					boolean b = DialogFactory
							.showModalJaNeinDialog(
									getInternalFrame(),
									LPMain.getTextRespectUISPr("er.eingangsrechnungistbereitserledigt.wirklichaendern"));

					if (b == false) {
						return;
					}
				}

				getPanelKontierung(true).eventActionNew(e, true, false);
				getPanelKontierung(true).eventYouAreSelected(false);

				LockStateValue lockstateValue = new LockStateValue(null, null,
						PanelBasis.LOCK_FOR_NEW);
				getPanelKontierung(true).updateButtons(lockstateValue);
				getPanelQueryAuftrag(true).updateButtons();
			} else if (e.getSource() == getPanelQueryZahlung(false)) {
				EingangsrechnungDto reDto = getEingangsrechnungDto();
				if (reDto.getStatusCNr().equalsIgnoreCase(
						EingangsrechnungFac.STATUS_STORNIERT)) {
					DialogFactory
							.showModalDialog(
									LPMain.getTextRespectUISPr("lp.hint"),
									LPMain.getTextRespectUISPr("er.hint.eingangsrechnungstorniertkeinezahlungen"));
					gotoAuswahl();
					return;
				}
				if (reDto.getStatusCNr().equalsIgnoreCase(
						EingangsrechnungFac.STATUS_ERLEDIGT)) {
					DialogFactory
							.showModalDialog(
									LPMain.getTextRespectUISPr("lp.hint"),
									LPMain.getTextRespectUISPr("er.hint.eingangsrechnungerledigtkeinezahlungen"));
					gotoAuswahl();
					return;
				}
				if (reDto.getEingangsrechnungartCNr().equalsIgnoreCase(
						EingangsrechnungFac.EINGANGSRECHNUNGART_GUTSCHRIFT)) {
					DialogFactory
							.showModalDialog(
									LPMain.getTextRespectUISPr("lp.hint"),
									LPMain.getTextRespectUISPr("er.hint.eingangsgutschriftkeinezahlung"));
					gotoAuswahl();
					return;
				}
				try {
					getPanelZahlung(true).eventActionNew(e, true, false);
					getPanelZahlung(true).eventYouAreSelected(false);
					LockStateValue lockstateValue = new LockStateValue(null,
							null, PanelBasis.LOCK_FOR_NEW);
					getPanelZahlung(true).updateButtons(lockstateValue);
				} catch (Exception ex) {
					if (ex.getCause() instanceof EntityExistsException) {
						DialogFactory
								.showModalDialog(
										LPMain.getTextRespectUISPr("lp.hinweis"),
										LPMain.getTextRespectUISPr("finanz.error.ergesperrt"));
						System.out.println("");
					} else {
						throw ex;
					}
				}
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_DISCARD) {
			if (e.getSource() == getPanelEingangsrechnungKopfdaten(false)) {
				// nix
			} else if (e.getSource() == getPanelAuftrag(false)) {
				getPanelSplitAuftrag(true).eventYouAreSelected(false);
				reloadEingangsrechnungDto();
			} else if (e.getSource() == getPanelInserat(false)) {
				getPanelSplitInserat(true).eventYouAreSelected(false);
				reloadEingangsrechnungDto();
			} else if (e.getSource() == panelKontierung) {
				// getPanelSplitKontierung(true).eventYouAreSelected(false);
				panelSplitKontierung.eventYouAreSelected(false);
				reloadEingangsrechnungDto();
			} else if (e.getSource() == getPanelZahlung(false)) {
				getPanelSplitZahlung(true).eventYouAreSelected(false);
				reloadEingangsrechnungDto();
			}

		} else if (e.getID() == ItemChangedEvent.ACTION_SAVE) {
			if (e.getSource() == getPanelAuftrag(false)) {
				Object key = getPanelAuftrag(true).getKeyWhenDetailPanel();
				getPanelSplitAuftrag(true).eventYouAreSelected(false);
				getPanelQueryAuftrag(true).setSelectedId(key);
				getPanelSplitAuftrag(true).eventYouAreSelected(false);
			} else if (e.getSource() == getPanelInserat(false)) {
				Object key = getPanelInserat(true).getKeyWhenDetailPanel();
				getPanelSplitInserat(true).eventYouAreSelected(false);
				getPanelQueryInserat(true).setSelectedId(key);
				getPanelSplitInserat(true).eventYouAreSelected(false);
			} else if (e.getSource() == panelKontierung) {
				Object key = getPanelKontierung(true).getKeyWhenDetailPanel();
				panelSplitKontierung.eventYouAreSelected(false);
				getPanelQueryKontierung(true).setSelectedId(key);
				panelSplitKontierung.eventYouAreSelected(false);
			} else if (e.getSource() == getPanelZahlung(false)) {
				Object key = getPanelZahlung(true).getKeyWhenDetailPanel();
				getPanelSplitZahlung(true).eventYouAreSelected(false);
				getPanelQueryZahlung(true).setSelectedId(key);
				getPanelSplitZahlung(true).eventYouAreSelected(false);
			} else if (e.getSource() == getPanelEingangsrechnungKopfdaten(false)) {
				getPanelQueryEingangsrechnung(true).clearDirektFilter();
				Object key = getPanelEingangsrechnungKopfdaten(true)
						.getKeyWhenDetailPanel();
				getPanelQueryEingangsrechnung(true).eventYouAreSelected(false);
				getPanelQueryEingangsrechnung(true).setSelectedId(key);
				getPanelQueryEingangsrechnung(true).eventYouAreSelected(false);
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_GOTO_MY_DEFAULT_QP) {
			if (e.getSource() == getPanelEingangsrechnungKopfdaten(false)) {
				setEingangsrechnungDto(null);
				panelQueryEingangsrechnung.eventYouAreSelected(false);
				this.setSelectedComponent(getPanelQueryEingangsrechnung(true));
			} else if (e.getSource() == getPanelAuftrag(false)) {
				setKeyWasForLockMe();
				// selectafterdelete: wenn der key null ist, den logisch
				// naechsten selektieren
				if (getPanelAuftrag(true).getKeyWhenDetailPanel() == null) {
					Object oNaechster = getPanelQueryAuftrag(true)
							.getId2SelectAfterDelete();
					getPanelQueryAuftrag(true).setSelectedId(oNaechster);
				}
				getPanelSplitAuftrag(true).eventYouAreSelected(false);
			} else if (e.getSource() == getPanelInserat(false)) {
				setKeyWasForLockMe();
				// selectafterdelete: wenn der key null ist, den logisch
				// naechsten selektieren
				if (getPanelInserat(true).getKeyWhenDetailPanel() == null) {
					Object oNaechster = getPanelQueryInserat(true)
							.getId2SelectAfterDelete();
					getPanelQueryInserat(true).setSelectedId(oNaechster);
				}
				getPanelSplitInserat(true).eventYouAreSelected(false);
			} else if (e.getSource() == panelKontierung) {
				setKeyWasForLockMe();
				if (getPanelKontierung(true).getKeyWhenDetailPanel() == null) {
					Object oNaechster = getPanelQueryKontierung(true)
							.getId2SelectAfterDelete();
					getPanelQueryKontierung(true).setSelectedId(oNaechster);
				}
				panelSplitKontierung.eventYouAreSelected(false);
			} else if (e.getSource() == getPanelZahlung(false)) {
				setKeyWasForLockMe();
				if (getPanelZahlung(true).getKeyWhenDetailPanel() == null) {
					Object oNaechster = getPanelQueryZahlung(true)
							.getId2SelectAfterDelete();
					getPanelQueryZahlung(true).setSelectedId(oNaechster);
				}
				getPanelSplitZahlung(true).eventYouAreSelected(false);
			}

		}
		// der OK Button in einem PanelDialog wurde gedrueckt
		else if (e.getID() == ItemChangedEvent.ACTION_KRITERIEN_HAVE_BEEN_SELECTED) {
			if (e.getSource() == getPanelKriterienUebersicht()) {
				// die Kriterien fuer PanelTabelle abholen
				FilterKriterium[] aAlleKriterien = getPanelKriterienUebersicht()
						.getAlleFilterKriterien();

				// die Kriterien dem PanelTabelle setzen
				getPanelUebersicht().setDefaultFilter(aAlleKriterien);

				setComponentAt(iDX_UEBERSICHT, getPanelUebersicht());
				// die Uebersicht aktualisieren @todo redundant, wenn man dann
				// ohnehin wechselt PJ 5228
				getPanelUebersicht().eventYouAreSelected(false);
				getPanelUebersicht().updateButtons(
						new LockStateValue(null, null,
								PanelBasis.LOCK_IS_NOT_LOCKED));

				// man steht auf alle Faelle auf der Uebersicht
				// setSelectedComponent(getPanelUebersicht());
			}
		}
		if (e.getID() == ItemChangedEvent.ACTION_POSITION_VONNNACHNMINUS1) {

		} else if (e.getID() == ItemChangedEvent.ACTION_POSITION_VONNNACHNPLUS1) {

		} else if (e.getID() == ItemChangedEvent.ACTION_POSITION_VORPOSITIONEINFUEGEN) {

		}

		else if (e.getID() == ItemChangedEvent.ACTION_MY_OWN_NEW) {
			String sAspectInfo = ((ISourceEvent) e.getSource()).getAspect();
			if (sAspectInfo.endsWith(MY_OWN_NEW_AUS_BESTELLUNG)) {
				// Der Benutzer muss eine Bestellung auswaehlen
				dialogQueryBestellungFromListe(null);
			}

		}
	}

	private void dialogQueryBestellungFromListe(ActionEvent e) throws Throwable {
		// Filterkriterien fuer Bestellungen einstellen
		FilterKriterium fk[] = new FilterKriterium[5];
		// Nur gelieferte und bestaetigte Bestellungen sollen angezeigt werden.
		FilterKriterium fkAbgerufen = new FilterKriterium(
				BestellungFac.FLR_BESTELLUNG_BESTELLUNGSTATUS_C_NR, true, "'"
						+ BestellungFac.BESTELLSTATUS_ABGERUFEN + "'",
				FilterKriterium.OPERATOR_NOT_EQUAL, false);

		FilterKriterium fkAngelegt = new FilterKriterium(
				BestellungFac.FLR_BESTELLUNG_BESTELLUNGSTATUS_C_NR, true, "'"
						+ BestellungFac.BESTELLSTATUS_ANGELEGT + "'",
				FilterKriterium.OPERATOR_NOT_EQUAL, false);

		FilterKriterium fkErledigt = new FilterKriterium(
				BestellungFac.FLR_BESTELLUNG_BESTELLUNGSTATUS_C_NR, true, "'"
						+ BestellungFac.BESTELLSTATUS_ERLEDIGT + "'",
				FilterKriterium.OPERATOR_NOT_EQUAL, false);

		FilterKriterium fkOffen = new FilterKriterium(
				BestellungFac.FLR_BESTELLUNG_BESTELLUNGSTATUS_C_NR, true, "'"
						+ BestellungFac.BESTELLSTATUS_OFFEN + "'",
				FilterKriterium.OPERATOR_NOT_EQUAL, false);

		FilterKriterium fkStorniert = new FilterKriterium(
				BestellungFac.FLR_BESTELLUNG_BESTELLUNGSTATUS_C_NR, true, "'"
						+ BestellungFac.BESTELLSTATUS_STORNIERT + "'",
				FilterKriterium.OPERATOR_NOT_EQUAL, false);

		fk[0] = fkAbgerufen;
		fk[1] = fkAngelegt;
		fk[2] = fkErledigt;
		fk[3] = fkOffen;
		fk[4] = fkStorniert;
		// erstellen und anzeigen des Bestellungspanels
		panelQueryFLRBestellungauswahl = BestellungFilterFactory.getInstance()
				.createPanelFLRBestellung(getInternalFrame(), true, false, fk,
						null);
		new DialogQuery(panelQueryFLRBestellungauswahl);

	}

	/**
	 * Dto updaten, va.a um den status zu aktualisieren laden
	 * 
	 * @throws Throwable
	 */
	protected void refreshEingangsrechnungDto() throws Throwable {
		if (getEingangsrechnungDto() != null) {
			holeEingangsrechnungDto(getEingangsrechnungDto().getIId());
		}
	}

	/**
	 * hole EingangsrechnungDto.
	 * 
	 * @param key
	 *            Object
	 * @throws Throwable
	 */
	private void holeEingangsrechnungDto(Object key) throws Throwable {
		if (key != null) {
			setEingangsrechnungDto(DelegateFactory.getInstance()
					.getEingangsrechnungDelegate()
					.eingangsrechnungFindByPrimaryKey((Integer) key));
			if (getEingangsrechnungDto().getTGedruckt() != null) {
				getPanelQueryEingangsrechnung(true).setStatusbarSpalte4(
						LPMain.getTextRespectUISPr("er.gedruckt")
								+ ":"
								+ Helper.formatDatum(getEingangsrechnungDto()
										.getTGedruckt(), LPMain.getTheClient()
										.getLocUi()));
			} else {
				getPanelQueryEingangsrechnung(true).setStatusbarSpalte4("");
			}

			getInternalFrame().setKeyWasForLockMe(key.toString());
			if (getPanelEingangsrechnungKopfdaten(false) != null) {
				getPanelEingangsrechnungKopfdaten(true).setKeyWhenDetailPanel(
						key);
			}
		}
	}

	protected void reloadEingangsrechnungDto() throws Throwable {
		if (getEingangsrechnungDto() != null) {
			holeEingangsrechnungDto(getEingangsrechnungDto().getIId());
		}
	}

	public void lPEventObjectChanged(ChangeEvent e) throws Throwable {
		super.lPEventObjectChanged(e);

		int index = this.getSelectedIndex();

		if (index == IDX_EINGANGSRECHNUNGEN) {
			getPanelQueryEingangsrechnung(true).eventYouAreSelected(false);
		} else if (index == iDX_KOPFDATEN) {
			if (this.getEingangsrechnungDto() != null) {
				getPanelEingangsrechnungKopfdaten(true).setKeyWhenDetailPanel(
						this.getEingangsrechnungDto().getIId());
			}
			getPanelEingangsrechnungKopfdaten(true).eventYouAreSelected(false);

		} else if (index == iDX_ZUORDNUNG) {
			getPanelEingangsrechnungKopfdatenZuordnung(true)
					.setKeyWhenDetailPanel(
							this.getEingangsrechnungDto().getIId());
			getPanelEingangsrechnungKopfdatenZuordnung(true)
					.eventYouAreSelected(false);
		} else if (index == iDX_AUFTRAGSZUORDNUNG) {
			getPanelSplitAuftrag(true).eventYouAreSelected(false);
			// wenn ich aus der Uebersicht komme, verlier ich den Titel
			this.setEingangsrechnungDto(this.getEingangsrechnungDto());
		} else if (index == iDX_INSERATZUORDNUNG) {
			getPanelSplitInserat(true).eventYouAreSelected(false);
			// wenn ich aus der Uebersicht komme, verlier ich den Titel
			this.setEingangsrechnungDto(this.getEingangsrechnungDto());
		} else if (index == iDX_KONTIERUNG) {
			getPanelQueryKontierung(true).eventYouAreSelected(false);
			getPanelQueryKontierung(true).updateButtons();
			getPanelKontierung(true).eventYouAreSelected(false); // sonst werden
																	// die
																	// buttons
																	// nicht
																	// richtig
																	// gesetzt!
			refreshPanelSplitKontierung();
			// getPanelSplitKontierung(true).eventYouAreSelected(false);
			// wenn ich aus der Uebersicht komme, verlier ich den Titel
			this.setEingangsrechnungDto(this.getEingangsrechnungDto());
		} else if (index == iDX_ZAHLUNGEN) {
			getPanelSplitZahlung(true).eventYouAreSelected(false);
			// wenn ich aus der Uebersicht komme, verlier ich den Titel
			this.setEingangsrechnungDto(this.getEingangsrechnungDto());
		} else if (index == iDX_WARENEINGAENGE) {
			getPanelQueryWareneingaenge(true).eventYouAreSelected(false);
			// wenn ich aus der Uebersicht komme, verlier ich den Titel
			this.setEingangsrechnungDto(this.getEingangsrechnungDto());
		} else if (index == iDX_UEBERSICHT) {
			getPanelUebersicht();
			// bevor man an die Uebersicht kommt, muss man die Kriterien waehlen
			getInternalFrame().showPanelDialog(getPanelKriterienUebersicht());
		}

	}

	/**
	 * getPanelEingangsrechnungKopfdaten mit extrem lazy loading.
	 * 
	 * @param bNeedInstantiationIfNull
	 *            boolean
	 * @return PanelEingangsrechnungKopfdaten
	 * @throws Throwable
	 */
	protected PanelEingangsrechnungKopfdaten getPanelEingangsrechnungKopfdaten(
			boolean bNeedInstantiationIfNull) throws Throwable {
		if (panelEingangsrechnungKopfdaten == null && bNeedInstantiationIfNull) {
			panelEingangsrechnungKopfdaten = new PanelEingangsrechnungKopfdaten(
					getInternalFrame(),
					LPMain.getTextRespectUISPr("lp.kopfdaten"), null, this);
			this.setComponentAt(iDX_KOPFDATEN, panelEingangsrechnungKopfdaten);
		}
		return panelEingangsrechnungKopfdaten;
	}

	protected PanelEingangsrechnungKopfdatenZuordnung getPanelEingangsrechnungKopfdatenZuordnung(
			boolean bNeedInstantiationIfNull) throws Throwable {
		if (panelEingangsrechnungKopfdatenZuordnung == null
				&& bNeedInstantiationIfNull) {
			panelEingangsrechnungKopfdatenZuordnung = new PanelEingangsrechnungKopfdatenZuordnung(
					getInternalFrame(),
					LPMain.getTextRespectUISPr("er.zuordnung"), null, this);
			this.setComponentAt(iDX_ZUORDNUNG,
					panelEingangsrechnungKopfdatenZuordnung);
		}
		return panelEingangsrechnungKopfdatenZuordnung;
	}

	/**
	 * getPanelAuftrag mit extrem lazy loading.
	 * 
	 * @param bNeedInstantiationIfNull
	 *            boolean
	 * @return PanelEingangsrechnungAuftragszuordnung
	 * @throws Throwable
	 */
	private PanelEingangsrechnungAuftragszuordnung getPanelAuftrag(
			boolean bNeedInstantiationIfNull) throws Throwable {
		if (panelAuftrag == null && bNeedInstantiationIfNull) {
			panelAuftrag = new PanelEingangsrechnungAuftragszuordnung(
					getInternalFrame(),
					LPMain.getTextRespectUISPr("er.tab.oben.auftragszuordnung.title"),
					null, this);
		}
		return panelAuftrag;
	}

	private PanelEingangsrechnungInseratzuordnung getPanelInserat(
			boolean bNeedInstantiationIfNull) throws Throwable {
		if (panelInserat == null && bNeedInstantiationIfNull) {
			panelInserat = new PanelEingangsrechnungInseratzuordnung(
					getInternalFrame(),
					LPMain.getTextRespectUISPr("er.tab.oben.inseratzuordnung.title"),
					null, this);
		}
		return panelInserat;
	}

	/**
	 * getPanelKontierung mit extrem lazy loading.
	 * 
	 * @param bNeedInstantiationIfNull
	 *            boolean
	 * @return PanelEingangsrechnungAuftragszuordnung
	 * @throws Throwable
	 */

	private PanelEingangsrechnungKontierung getPanelKontierung(
			boolean bNeedInstantiationIfNull) throws Throwable {
		if (panelKontierung == null && bNeedInstantiationIfNull) {
			panelKontierung = new PanelEingangsrechnungKontierung(
					getInternalFrame(),
					LPMain.getTextRespectUISPr("er.tab.oben.kontierung.title"),
					null, this);
		}
		return panelKontierung;
	}

	/**
	 * getPanelZahlungen mit extrem lazy loading.
	 * 
	 * @param bNeedInstantiationIfNull
	 *            boolean
	 * @return PanelEingangsrechnungAuftragszuordnung
	 * @throws Throwable
	 */
	private PanelEingangsrechnungZahlung getPanelZahlung(
			boolean bNeedInstantiationIfNull) throws Throwable {
		if (panelZahlung == null && bNeedInstantiationIfNull) {
			panelZahlung = new PanelEingangsrechnungZahlung(getInternalFrame(),
					LPMain.getTextRespectUISPr("er.tab.oben.zahlungen.title"),
					this);
		}
		return panelZahlung;
	}

	private PanelBasis getPanelSplitZahlung(boolean bNeedInstantiationIfNull)
			throws Throwable {
		if (panelSplitZahlung == null && bNeedInstantiationIfNull) {
			// if
			// (eingangsrechnungDto.getEingangsrechnungartCNr().equals(EingangsrechnungFac.
			// EINGANGSRECHNUNGART_GUTSCHRIFT)) {
			// panelSplitZahlung = getPanelQueryZahlung(true);
			// }
			// else {
			panelSplitZahlung = new PanelSplit(getInternalFrame(),
					getPanelZahlung(true), getPanelQueryZahlung(true), 165);
			// }
			this.setComponentAt(iDX_ZAHLUNGEN, panelSplitZahlung);
		}
		return panelSplitZahlung;
	}

	private PanelSplit getPanelSplitAuftrag(boolean bNeedInstantiationIfNull)
			throws Throwable {
		if (panelSplitAuftrag == null && bNeedInstantiationIfNull) {
			panelSplitAuftrag = new PanelSplit(getInternalFrame(),
					getPanelAuftrag(true), getPanelQueryAuftrag(true), 200);
			this.setComponentAt(iDX_AUFTRAGSZUORDNUNG, panelSplitAuftrag);
		}
		return panelSplitAuftrag;
	}

	private PanelSplit getPanelSplitInserat(boolean bNeedInstantiationIfNull)
			throws Throwable {
		if (panelSplitInserat == null && bNeedInstantiationIfNull) {
			panelSplitInserat = new PanelSplit(getInternalFrame(),
					getPanelInserat(true), getPanelQueryInserat(true), 200);
			this.setComponentAt(iDX_INSERATZUORDNUNG, panelSplitInserat);
		}
		return panelSplitInserat;
	}

	/*
	 * private PanelSplit getPanelSplitKontierung(boolean
	 * bNeedInstantiationIfNull) throws Throwable { if (panelSplitKontierung ==
	 * null && bNeedInstantiationIfNull) { panelSplitKontierung = new
	 * PanelSplit(getInternalFrame(), getPanelKontierung(true),
	 * getPanelQueryKontierung(true), 200); this.setComponentAt(iDX_KONTIERUNG,
	 * panelSplitKontierung); } return panelSplitKontierung; }
	 */
	private PanelSplit refreshPanelSplitKontierung() throws Throwable {

		if (panelSplitKontierung == null) {
			panelKontierung = getPanelKontierung(true);
			panelQueryKontierung = getPanelQueryKontierung(true);
			panelSplitKontierung = new PanelSplit(getInternalFrame(),
					panelKontierung, panelQueryKontierung, 200);
			setComponentAt(iDX_KONTIERUNG, panelSplitKontierung);
		}

		return panelSplitKontierung;
	}

	private PanelTabelle getPanelUebersicht() throws Throwable {
		if (panelUebersicht == null) {
			panelUebersicht = new PanelTabelleEingangsrechnungUebersicht(
					QueryParameters.UC_ID_EINGANGSRECHNUNG_UMSATZ,
					LPMain.getTextRespectUISPr("lp.umsatzuebersicht"),
					getInternalFrame());
		}
		return panelUebersicht;
	}

	private PanelDialogKriterien getPanelKriterienUebersicht()
			throws HeadlessException, Throwable {
		if (panelKriterienUebersicht == null) {
			panelKriterienUebersicht = new PanelDialogKriterienEingangsrechnungUebersicht(
					getInternalFrame(),
					LPMain.getTextRespectUISPr("lp.kriterienumsatzuebersicht"),
					this);
		}
		return panelKriterienUebersicht;
	}

	protected void lPActionEvent(java.awt.event.ActionEvent e) throws Throwable {
		if (e.getActionCommand().equals(MENU_ACTION_ALLE)) {
			try {
				getInternalFrame().showReportKriterien(
						new ReportEingangsrechnungAlleEingangsrechnungen(this,
								"Alle Eingangsrechnungen"));
			} catch (Throwable ex) {
				throw new ExceptionLP(EJBExceptionLP.FEHLER_BEIM_DRUCKEN, ex);
			}
		} else if (e.getActionCommand().equals(MENUE_ACTION_DRUCKEN)) {
			print();
		} else if (e.getActionCommand().equals(MENU_ACTION_OFFENE)) {
			getInternalFrame().showReportKriterien(
					new ReportEingangsrechnungOffene(this,
							"Offene Eingangsrechnungen"));
		} else if (e.getActionCommand()
				.equals(MENU_ACTION_FEHLENDE_ZOLLPAPIERE)) {

			getInternalFrame()
					.showReportKriterien(
							new ReportFehlendeZollpapiere(
									this,
									LPMain.getTextRespectUISPr("er.journal.fehlendezollpapiere")));
		} else if (e.getActionCommand()
				.equals(MENU_ACTION_ERFASSTE_ZOLLPAPIERE)) {

			getInternalFrame()
					.showReportKriterien(
							new ReportErfassteZollimportpapiere(
									this,
									LPMain.getTextRespectUISPr("er.report.erfasste.zollimportpapiere")));
		} else if (e.getActionCommand().equals(MENU_ACTION_ZAHLUNG)) {
			getInternalFrame().showReportKriterien(
					new ReportEingangsrechnungZahlungsjournal(this,
							"Zahlungsjournal"));
		} else if (e.getActionCommand().equals(MENU_ACTION_KONTIERUNG)) {
			getInternalFrame().showReportKriterien(
					new ReportEingangsrechnungKontierung(this,
							"Kontierungsjournal"));
		} else if (e.getActionCommand().equals(
				MENUE_ACTION_BEARBEITEN_BELEGUEBERNAHME)) {

			DelegateFactory
					.getInstance()
					.getEingangsrechnungDelegate()
					.verbucheEingangsrechnungNeu(
							this.getEingangsrechnungDto().getIId());

		} else if (e.getActionCommand().equals(
				MENUE_ACTION_BEARBEITEN_MANUELL_ERLEDIGEN)) {
			if (!getPanelEingangsrechnungKopfdaten(true).isLockedDlg()) {
				reloadEingangsrechnungDto();
				if (getEingangsrechnungDto() != null) {
					if (getEingangsrechnungDto().getStatusCNr().equals(
							EingangsrechnungFac.STATUS_ANGELEGT)
							|| getEingangsrechnungDto().getStatusCNr().equals(
									EingangsrechnungFac.STATUS_TEILBEZAHLT)) {
						if (DialogFactory
								.showMeldung(
										LPMain.getTextRespectUISPr("er.hint.erledigtstatusauferledigt"),
										LPMain.getTextRespectUISPr("lp.hint"),
										javax.swing.JOptionPane.YES_NO_OPTION) == javax.swing.JOptionPane.YES_OPTION) {
							DelegateFactory
									.getInstance()
									.getEingangsrechnungDelegate()
									.manuellErledigen(
											getEingangsrechnungDto().getIId());

							getEingangsrechnungDto().setStatusCNr(
									EingangsrechnungFac.STATUS_ERLEDIGT);
							// Panel aktualisieren
							this.lPEventObjectChanged(null);
						}
					} else if (getEingangsrechnungDto().getStatusCNr().equals(
							EingangsrechnungFac.STATUS_ERLEDIGT)) {
						if (DialogFactory
								.showMeldung(
										LPMain.getTextRespectUISPr("er.hint.erledigtstatuszuruecknehmen"),
										LPMain.getTextRespectUISPr("lp.hint"),
										javax.swing.JOptionPane.YES_NO_OPTION) == javax.swing.JOptionPane.YES_OPTION) {
							DelegateFactory
									.getInstance()
									.getEingangsrechnungDelegate()
									.manuellErledigen(
											getEingangsrechnungDto().getIId());
							// Panel aktualisieren
							this.lPEventObjectChanged(null);
						}
					} else {
						DialogFactory
								.showModalDialog(
										LPMain.getTextRespectUISPr("lp.hint"),
										LPMain.getTextRespectUISPr("er.hint.erkannnichtmanuellerledigtwerden"));
					}
				} else {
					DialogFactory
							.showModalDialog(
									LPMain.getTextRespectUISPr("lp.hint"),
									LPMain.getTextRespectUISPr("er.hint.keineeingangsrechnunggewaehlt"));
				}
			}
		} else if (e.getActionCommand().equals(
				MENUE_ACTION_BEARBEITEN_WIEDERHOLENDE_ZUSATZKOSTEN_ANLEGEN)) {
			DelegateFactory.getInstance().getEingangsrechnungDelegate()
					.wiederholendeZusatzkostenAnlegen();
		} else if (e.getActionCommand().equals(
				MENUE_ACTION_BEARBEITEN_MAHNDATEN)) {

			if (getEingangsrechnungDto() != null
					&& getEingangsrechnungDto().getIId() != null) {
				DialogMahnstufeDatum d = new DialogMahnstufeDatum(
						getEingangsrechnungDto().getIId(), this);
				LPMain.getInstance().getDesktop()
						.platziereDialogInDerMitteDesFensters(d);
				d.setVisible(true);
			}

		}

	}

	public void print() throws Throwable {

		getInternalFrame().showReportKriterien(
				new ReportEingangsrechnung(getInternalFrame(),
						getEingangsrechnungDto(), LPMain
								.getTextRespectUISPr("er.eingangsrechnung")));
	}

	private void refreshFilterAuftragszuordnung() throws Throwable {
		if (getEingangsrechnungDto() != null) {
			FilterKriterium[] krit = EingangsrechnungFilterFactory
					.getInstance().createFKAuftragszuordnung(
							getEingangsrechnungDto().getIId());
			getPanelQueryAuftrag(true).setDefaultFilter(krit);
		}
	}

	private void refreshFilterInseratzuordnung() throws Throwable {
		if (getEingangsrechnungDto() != null) {
			FilterKriterium[] krit = EingangsrechnungFilterFactory
					.getInstance().createFKInseratzuordnung(
							getEingangsrechnungDto().getIId());
			getPanelQueryInserat(true).setDefaultFilter(krit);
		}
	}

	private void refreshFilterKontierung() throws Throwable {
		if (getEingangsrechnungDto() != null) {
			FilterKriterium[] krit = EingangsrechnungFilterFactory
					.getInstance().createFKKontierung(
							getEingangsrechnungDto().getIId());
			getPanelQueryKontierung(true).setDefaultFilter(krit);
		}
	}

	protected void enablePanels() throws Throwable {
		setVisibilityKontierung();
	}

	private void setVisibilityKontierung() throws Throwable {
		if (getEingangsrechnungDto() != null) {
			// Wenn eine ER nicht auf Mehrfachkontierung gesetzt ist, wird der
			// Tab ausgegraut
			PanelBasis p = (PanelBasis) this.getSelectedComponent();
			Object key;
			if (p == null) {
				key = null;
			} else {
				key = p.getKeyWhenDetailPanel();
			}
			if (key == null || !key.equals(LPMain.getLockMeForNew())) {
				// kontierung evtl. ausgrauen
				if (eingangsrechnungDto.getKostenstelleIId() == null) {
					this.setEnabledAt(iDX_KONTIERUNG, true);
				} else {
					this.setEnabledAt(iDX_KONTIERUNG, false);
				}
			}
		}
	}

	private void refreshFilterZahlung() throws Throwable {
		if (getEingangsrechnungDto() != null) {
			FilterKriterium[] krit = EingangsrechnungFilterFactory
					.getInstance().createFKZahlungen(
							getEingangsrechnungDto().getIId());
			getPanelQueryZahlung(true).setDefaultFilter(krit);
		}
	}

	private void refreshFilterWareneingaenge() throws Throwable {
		if (getEingangsrechnungDto() != null) {
			FilterKriterium[] krit = BestellungFilterFactory.getInstance()
					.createFKWareneingangER(getEingangsrechnungDto().getIId());
			getPanelQueryWareneingaenge(true).setDefaultFilter(krit);
		}
	}

	/**
	 * Diese Methode setzt der aktuellen ER aus der Auswahlliste als den zu
	 * lockenden Auftrag.
	 * 
	 * @throws Throwable
	 */
	private void setKeyWasForLockMe() throws Throwable {
		Object oKey = getPanelQueryEingangsrechnung(true).getSelectedId();

		if (oKey != null) {
			holeEingangsrechnungDto(oKey);

			getInternalFrame().setKeyWasForLockMe(oKey.toString());
		} else {
			getInternalFrame().setKeyWasForLockMe(null);
		}
	}

	protected void setSelectedEingangsrechnungIId() throws Throwable {
		if (this.getEingangsrechnungDto() != null
				&& this.getEingangsrechnungDto().getIId() != null) {
			getPanelQueryEingangsrechnung(true).setSelectedId(
					getEingangsrechnungDto().getIId());
		}
	}

	protected void gotoAuswahl() throws Throwable {
		// tabelletitel: zur auswahl
		this.setSelectedIndex(IDX_EINGANGSRECHNUNGEN);
		getPanelQueryEingangsrechnung(true).eventYouAreSelected(false);
	}

	protected javax.swing.JMenuBar getJMenuBar() throws Throwable {
		WrapperMenuBar wmb = new WrapperMenuBar(this);

		// Menue Datei
		JMenu jmModul = (JMenu) wmb.getComponent(WrapperMenuBar.MENU_MODUL);

		jmModul.add(new JSeparator(), 0);

		JMenuItem menuItemDateiBestellung = new JMenuItem(
				LPMain.getTextRespectUISPr("bes.menu.datei.bestellung"));
		menuItemDateiBestellung.addActionListener(this);
		menuItemDateiBestellung.setActionCommand(MENUE_ACTION_DRUCKEN);
		jmModul.add(menuItemDateiBestellung, 0);

		// Menue Bearbeiten
		JMenu jmBearbeiten = (JMenu) wmb
				.getComponent(WrapperMenuBar.MENU_BEARBEITEN);

		if (!isBZusatzkosten()) {

			WrapperMenuItem menuItemBearbeitenMahndaten = new WrapperMenuItem(
					LPMain.getTextRespectUISPr("lp.mahnstufe"),
					RechteFac.RECHT_ER_EINGANGSRECHNUNG_CUD);
			menuItemBearbeitenMahndaten.addActionListener(this);
			menuItemBearbeitenMahndaten
					.setActionCommand(MENUE_ACTION_BEARBEITEN_MAHNDATEN);
			jmBearbeiten.add(menuItemBearbeitenMahndaten, 0);
		}

		if (isBZusatzkosten()) {

			WrapperMenuItem menuItemBearbeitenZusatzkostenAnlegen = new WrapperMenuItem(
					LPMain.getTextRespectUISPr("er.zusatzkosten.wiederholendeanlegen"),
					RechteFac.RECHT_ER_EINGANGSRECHNUNG_CUD);
			menuItemBearbeitenZusatzkostenAnlegen.addActionListener(this);
			menuItemBearbeitenZusatzkostenAnlegen
					.setActionCommand(MENUE_ACTION_BEARBEITEN_WIEDERHOLENDE_ZUSATZKOSTEN_ANLEGEN);
			jmBearbeiten.add(menuItemBearbeitenZusatzkostenAnlegen, 0);
		}

		WrapperMenuItem menueItemBeleguebernahme = null;
		menueItemBeleguebernahme = new WrapperMenuItem(
				LPMain.getTextRespectUISPr("finanz.beleguebernahme"),
				RechteFac.RECHT_FB_CHEFBUCHHALTER);
		menueItemBeleguebernahme.addActionListener(this);
		menueItemBeleguebernahme
				.setActionCommand(MENUE_ACTION_BEARBEITEN_BELEGUEBERNAHME);
		jmBearbeiten.add(menueItemBeleguebernahme, 0);
		if (!LPMain
				.getInstance()
				.getDesktop()
				.darfAnwenderAufModulZugreifen(
						LocaleFac.BELEGART_FINANZBUCHHALTUNG)) {

			WrapperMenuItem menuItemBearbeitenManuellErledigen = new WrapperMenuItem(
					LPMain.getTextRespectUISPr("lp.menu.menuellerledigen"),
					RechteFac.RECHT_ER_EINGANGSRECHNUNG_CUD);
			menuItemBearbeitenManuellErledigen.addActionListener(this);
			menuItemBearbeitenManuellErledigen
					.setActionCommand(MENUE_ACTION_BEARBEITEN_MANUELL_ERLEDIGEN);
			jmBearbeiten.add(menuItemBearbeitenManuellErledigen, 0);
		}

		JMenu journal = (JMenu) wmb.getComponent(WrapperMenuBar.MENU_JOURNAL);
		WrapperMenuItem menuItemAlle = new WrapperMenuItem(
				LPMain.getTextRespectUISPr("er.menu.alle"), null);
		WrapperMenuItem menuItemOffene = new WrapperMenuItem(
				LPMain.getTextRespectUISPr("er.menu.offenezumstichtag"), null);
		WrapperMenuItem menuItemZahlung = new WrapperMenuItem(
				LPMain.getTextRespectUISPr("er.menu.zahlungsausgang"), null);
		WrapperMenuItem menuItemKontierung = new WrapperMenuItem(
				LPMain.getTextRespectUISPr("er.menu.kontierung"), null);

		WrapperMenuItem menuItemFehlendeZollpapiere = new WrapperMenuItem(
				LPMain.getTextRespectUISPr("er.journal.fehlendezollpapiere"),
				null);

		menuItemAlle.addActionListener(this);
		menuItemAlle.setActionCommand(MENU_ACTION_ALLE);

		menuItemFehlendeZollpapiere.addActionListener(this);
		menuItemFehlendeZollpapiere
				.setActionCommand(MENU_ACTION_FEHLENDE_ZOLLPAPIERE);

		WrapperMenuItem menuItemErfassteZollpapiere = new WrapperMenuItem(
				LPMain.getTextRespectUISPr("er.report.erfasste.zollimportpapiere"),
				null);
		menuItemErfassteZollpapiere.addActionListener(this);
		menuItemErfassteZollpapiere
				.setActionCommand(MENU_ACTION_ERFASSTE_ZOLLPAPIERE);

		menuItemOffene.addActionListener(this);
		menuItemOffene.setActionCommand(MENU_ACTION_OFFENE);
		menuItemZahlung.addActionListener(this);
		menuItemZahlung.setActionCommand(MENU_ACTION_ZAHLUNG);
		menuItemKontierung.addActionListener(this);
		menuItemKontierung.setActionCommand(MENU_ACTION_KONTIERUNG);
		journal.add(menuItemAlle);
		journal.add(menuItemOffene);
		journal.add(menuItemZahlung);
		journal.add(menuItemKontierung);
		if (bZusatzkosten == false) {
			journal.add(menuItemFehlendeZollpapiere);
			journal.add(menuItemErfassteZollpapiere);
		}

		return wmb;
	}

	public Object getInseratDto() {
		return eingangsrechnungDto;
	}

	/**
	 * getPanelQueryAuftrag mit extrem lazy loading.
	 * 
	 * @return PanelQuery
	 * @param bNeedInstantiationIfNull
	 *            boolean
	 * @throws Throwable
	 */
	private PanelQuery getPanelQueryWareneingaenge(
			boolean bNeedInstantiationIfNull) throws Throwable {
		if (panelQueryWareneingaenge == null && bNeedInstantiationIfNull) {
			String[] aWhichButtonIUseAuftrag = {};
			panelQueryWareneingaenge = new PanelQuery(
					null,
					null,
					QueryParameters.UC_ID_EINGANGSRECHNUNG_WARENEINGANG,
					aWhichButtonIUseAuftrag,
					getInternalFrame(),
					LPMain.getTextRespectUISPr("er.tab.oben.wareneingaenge.title"),
					true);

			panelQueryWareneingaenge.getToolBar().getToolsPanelCenter()
					.add(lblweDiff);
			// , new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
			// GridBagConstraints.NORTHWEST,
			// GridBagConstraints.NONE, new Insets(4, 0, 0, 40),
			// 200, 0));

			setComponentAt(iDX_WARENEINGAENGE, panelQueryWareneingaenge);
		}
		return panelQueryWareneingaenge;
	}

	public WareneingangDto getWareneingangDto() {
		return this.wareneingangDto;
	}

	public void setWareneingangDto(WareneingangDto wareneingangDto) {
		this.wareneingangDto = wareneingangDto;
	}

	public void erstelleEingangsrechnungausInseraten(Object[] inseratIIds)
			throws ExceptionLP, Throwable {

		if (inseratIIds != null && inseratIIds.length > 0) {

			this.inseratIIds = inseratIIds;

			EingangsrechnungDto eingangsrechnungDto = new EingangsrechnungDto();

			InseratDto inseratDtoErstes = DelegateFactory.getInstance()
					.getInseratDelegate()
					.inseratFindByPrimaryKey((Integer) inseratIIds[0]);

			LieferantDto lieferantDto = DelegateFactory
					.getInstance()
					.getLieferantDelegate()
					.lieferantFindByPrimaryKey(
							inseratDtoErstes.getLieferantIId());
			MwstsatzDto mwst = DelegateFactory
					.getInstance()
					.getMandantDelegate()
					.mwstsatzFindByMwstsatzbezIIdAktuellster(
							lieferantDto.getMwstsatzbezIId());
			if (mwst == null) {
				DialogFactory.showModalDialog(LPMain
						.getTextRespectUISPr("lp.error"), LPMain
						.getTextRespectUISPr("lp.error.mwstsatzlieferant"));
			} else {
				Calendar cal = Calendar.getInstance();
				eingangsrechnungDto.setMwstsatzIId(mwst.getIId());
				eingangsrechnungDto.setMandantCNr(lieferantDto.getMandantCNr());

				eingangsrechnungDto.setLieferantIId(lieferantDto.getIId());

				eingangsrechnungDto.setDBelegdatum(Helper
						.cutDate(new java.sql.Date(cal.getTimeInMillis())));
				eingangsrechnungDto.setWaehrungCNr(LPMain.getTheClient()
						.getSMandantenwaehrung());

				eingangsrechnungDto.setNKurs(new BigDecimal(1));

				BigDecimal bdBetrag = new BigDecimal(0);
				for (int i = 0; i < inseratIIds.length; i++) {

					InseratDto inseratDto = DelegateFactory.getInstance()
							.getInseratDelegate()
							.inseratFindByPrimaryKey((Integer) inseratIIds[i]);

					BigDecimal bdBetraggEinesInserates = inseratDto
							.getNNettoeinzelpreisEk().multiply(
									inseratDto.getNMenge());

					double dRabattsatz = 100.0;
					if (inseratDto.getFKdRabatt() != null) {
						dRabattsatz = dRabattsatz - inseratDto.getFLFRabatt();
					}
					if (inseratDto.getFKdZusatzrabatt() != null) {
						double dFaktor2 = 100.0 - inseratDto
								.getFLfZusatzrabatt();
						dRabattsatz = dRabattsatz * dFaktor2 / 100.0;
					}
					if (inseratDto.getFKdNachlass() != null) {
						double dFaktor3 = 100.0 - inseratDto.getFLfNachlass();
						dRabattsatz = dRabattsatz * dFaktor3 / 100.0;
					}

					Double fRabattsatz = new Double(100.0 - dRabattsatz);
					BigDecimal bdRabattbetrag = Helper.getProzentWert(
							bdBetraggEinesInserates,
							new BigDecimal(fRabattsatz), 2);

					bdBetrag = bdBetrag.add(bdBetraggEinesInserates
							.subtract(bdRabattbetrag));

					InseratartikelDto[] inseratartikelDto = DelegateFactory
							.getInstance()
							.getInseratDelegate()
							.inseratartikelFindByInseratIId(inseratDto.getIId());
					for (int j = 0; j < inseratartikelDto.length; j++) {
						bdBetrag = bdBetrag.add(inseratartikelDto[j]
								.getNMenge().multiply(
										inseratartikelDto[j]
												.getNNettoeinzelpreisEk()));
					}

				}

				bdBetrag = bdBetrag.add(Helper.getProzentWert(bdBetrag,
						new BigDecimal(mwst.getFMwstsatz()), 4));

				eingangsrechnungDto.setNBetragfw(bdBetrag);
				eingangsrechnungDto.setNBetrag(bdBetrag
						.multiply(eingangsrechnungDto.getNKurs()));
				eingangsrechnungDto.setMwstsatzIId(lieferantDto
						.getMwstsatzbezIId());
				eingangsrechnungDto
						.setNUstBetragfw(Helper.getMehrwertsteuerBetrag(
								bdBetrag, mwst.getFMwstsatz()));
				eingangsrechnungDto.setNUstBetrag(eingangsrechnungDto
						.getNUstBetragfw().multiply(
								eingangsrechnungDto.getNKurs()));
				eingangsrechnungDto
						.setEingangsrechnungartCNr(EingangsrechnungFac.EINGANGSRECHNUNGART_EINGANGSRECHNUNG);
				eingangsrechnungDto.setDFreigabedatum(Helper
						.cutDate(new java.sql.Date(cal.getTimeInMillis())));
				eingangsrechnungDto.setZahlungszielIId(lieferantDto
						.getZahlungszielIId());
				eingangsrechnungDto.setKontoIId(lieferantDto
						.getKontoIIdWarenkonto());
				eingangsrechnungDto.setKostenstelleIId(lieferantDto
						.getIIdKostenstelle());
				if (DelegateFactory
						.getInstance()
						.getMandantDelegate()
						.darfAnwenderAufModulZugreifen(
								LocaleFac.BELEGART_FINANZBUCHHALTUNG)) {
					eingangsrechnungDto
							.setBIgErwerb(Helper.boolean2Short(DelegateFactory
									.getInstance()
									.getFinanzServiceDelegate()
									.istIgErwerb(
											lieferantDto
													.getKontoIIdKreditorenkonto())));
				}

				PanelEingangsrechnungKopfdaten pErKHelper = getPanelEingangsrechnungKopfdaten(true);
				pErKHelper.eventActionNew(null, true, false);
				setSelectedComponent(pErKHelper);
				pErKHelper.setMyComponents(eingangsrechnungDto);
				pErKHelper.eventActionRefresh(null, false);
				pErKHelper.wlaWaehrung1.setText(eingangsrechnungDto
						.getWaehrungCNr());
				pErKHelper.wlaWaehrung2.setText(eingangsrechnungDto
						.getWaehrungCNr());
			}
		}
	}

	public void erstelleEingangsrechnungausBestellung(Integer iBestellungIId,
			WareneingangDto wareneingangDto) throws ExceptionLP, Throwable {
		this.wareneingangDto = wareneingangDto;
		EingangsrechnungDto eingangsrechnungDto = new EingangsrechnungDto();
		BestellungDto bestellungDto = DelegateFactory.getInstance()
				.getBestellungDelegate()
				.bestellungFindByPrimaryKey(iBestellungIId);
		LieferantDto lieferantDto = DelegateFactory
				.getInstance()
				.getLieferantDelegate()
				.lieferantFindByPrimaryKey(
						bestellungDto.getLieferantIIdBestelladresse());
		MwstsatzDto mwst = DelegateFactory
				.getInstance()
				.getMandantDelegate()
				.mwstsatzFindByMwstsatzbezIIdAktuellster(
						lieferantDto.getMwstsatzbezIId());
		if (mwst == null) {
			DialogFactory.showModalDialog(
					LPMain.getTextRespectUISPr("lp.error"),
					LPMain.getTextRespectUISPr("lp.error.mwstsatzlieferant"));
		} else {
			Calendar cal = Calendar.getInstance();
			eingangsrechnungDto.setMwstsatzIId(mwst.getIId());
			eingangsrechnungDto.setMandantCNr(bestellungDto.getMandantCNr());
			if (bestellungDto.getLieferantIIdRechnungsadresse() != null) {
				eingangsrechnungDto.setLieferantIId(bestellungDto
						.getLieferantIIdRechnungsadresse());
			} else {
				eingangsrechnungDto.setLieferantIId(bestellungDto
						.getLieferantIIdBestelladresse());
			}

			eingangsrechnungDto.setDBelegdatum(Helper
					.cutDate(new java.sql.Date(cal.getTimeInMillis())));
			eingangsrechnungDto.setWaehrungCNr(bestellungDto.getWaehrungCNr());
			/*
			 * AD: ge&auml;ndert auf Kurs zu Datum eingangsrechnungDto
			 * .setNKurs(Helper.getKehrwert(new BigDecimal( bestellungDto
			 * .getDWechselkursMandantwaehrungBestellungswaehrung())));
			 */
			WechselkursDto kursDto = DelegateFactory
					.getInstance()
					.getLocaleDelegate()
					.getKursZuDatum(eingangsrechnungDto.getWaehrungCNr(),
							LPMain.getTheClient().getSMandantenwaehrung(),
							eingangsrechnungDto.getDBelegdatum());
			eingangsrechnungDto.setNKurs(kursDto.getNKurs());

			WareneingangspositionDto[] wareneingangsposDto = new WareneingangDelegate()
					.wareneingangspositionFindByWareneingangIId(wareneingangDto
							.getIId());
			BigDecimal bdBetrag = BigDecimal.ZERO;
			for (int i = 0; i < wareneingangsposDto.length; i++) {
				// PJ 17308
				BestellpositionDto bestposDto = DelegateFactory
						.getInstance()
						.getBestellungDelegate()
						.bestellpositionFindByPrimaryKey(
								wareneingangsposDto[i].getBestellpositionIId());
				if (bestposDto.getNFixkostengeliefert() != null) {
					bdBetrag = bdBetrag
							.add(bestposDto.getNFixkostengeliefert());
				}

				BigDecimal bdEinstandspreisUngerundet = DelegateFactory
						.getInstance()
						.getWareneingangDelegate()
						.getBerechnetenEinstandspreisEinerWareneingangsposition(
								wareneingangsposDto[i].getIId());

				if (bdEinstandspreisUngerundet != null) {
					bdBetrag = bdBetrag.add(bdEinstandspreisUngerundet
							.multiply(wareneingangsposDto[i]
									.getNGeliefertemenge()));
				} else {
					bdBetrag = bdBetrag.add(wareneingangsposDto[i]
							.getNGelieferterpreis().multiply(
									wareneingangsposDto[i]
											.getNGeliefertemenge()));
				}

			}

			// PJ 15885 MWST hinzufuegen:
			bdBetrag = bdBetrag.add(Helper.getProzentWert(bdBetrag,
					new BigDecimal(mwst.getFMwstsatz()), 4));

			eingangsrechnungDto.setNBetragfw(bdBetrag);
			eingangsrechnungDto.setNBetrag(bdBetrag
					.multiply(eingangsrechnungDto.getNKurs()));
			eingangsrechnungDto
					.setMwstsatzIId(lieferantDto.getMwstsatzbezIId());
			eingangsrechnungDto.setNUstBetragfw(Helper.getMehrwertsteuerBetrag(
					bdBetrag, mwst.getFMwstsatz()));
			eingangsrechnungDto
					.setNUstBetrag(eingangsrechnungDto.getNUstBetragfw()
							.multiply(eingangsrechnungDto.getNKurs()));
			eingangsrechnungDto
					.setEingangsrechnungartCNr(EingangsrechnungFac.EINGANGSRECHNUNGART_EINGANGSRECHNUNG);
			eingangsrechnungDto.setDFreigabedatum(Helper
					.cutDate(new java.sql.Date(cal.getTimeInMillis())));
			eingangsrechnungDto.setZahlungszielIId(lieferantDto
					.getZahlungszielIId());
			eingangsrechnungDto.setKontoIId(lieferantDto
					.getKontoIIdWarenkonto());
			eingangsrechnungDto.setKostenstelleIId(lieferantDto
					.getIIdKostenstelle());
			if (DelegateFactory
					.getInstance()
					.getMandantDelegate()
					.darfAnwenderAufModulZugreifen(
							LocaleFac.BELEGART_FINANZBUCHHALTUNG)) {
				eingangsrechnungDto
						.setBIgErwerb(Helper.boolean2Short(DelegateFactory
								.getInstance()
								.getFinanzServiceDelegate()
								.istIgErwerb(
										lieferantDto
												.getKontoIIdKreditorenkonto())));
			}

			PanelEingangsrechnungKopfdaten pErKHelper = getPanelEingangsrechnungKopfdaten(true);
			pErKHelper.eventActionNew(null, true, false);
			setSelectedComponent(pErKHelper);
			pErKHelper.setMyComponents(eingangsrechnungDto);
			pErKHelper.eventActionRefresh(null, false);
			pErKHelper.wlaWaehrung1.setText(eingangsrechnungDto
					.getWaehrungCNr());
			pErKHelper.wlaWaehrung2.setText(eingangsrechnungDto
					.getWaehrungCNr());

		}
	}
}
