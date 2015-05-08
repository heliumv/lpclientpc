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
package com.lp.client.inserat;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;
import javax.swing.KeyStroke;
import javax.swing.event.ChangeEvent;

import com.lp.client.artikel.ArtikelFilterFactory;
import com.lp.client.artikel.PanelInventurliste;
import com.lp.client.auftrag.PanelTabelleSichtLSRE;
import com.lp.client.bestellung.BestellungFilterFactory;
import com.lp.client.bestellung.InternalFrameBestellung;
import com.lp.client.bestellung.TabbedPaneBestellung;
import com.lp.client.eingangsrechnung.InternalFrameEingangsrechnung;
import com.lp.client.eingangsrechnung.TabbedPaneEingangsrechnung;
import com.lp.client.fertigung.FertigungFilterFactory;
import com.lp.client.frame.Command2IFNebeneinander;
import com.lp.client.frame.CommandCreateIF;
import com.lp.client.frame.CommandSetFocus;
import com.lp.client.frame.Defaults;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.LockStateValue;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQuery;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.PanelQueryFLRGoto;
import com.lp.client.frame.component.PanelSplit;
import com.lp.client.frame.component.TabbedPane;
import com.lp.client.frame.component.WrapperMenu;
import com.lp.client.frame.component.WrapperMenuBar;
import com.lp.client.frame.component.WrapperMenuItem;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.partner.PartnerFilterFactory;
import com.lp.client.partner.ReportLieferantenstammblatt;
import com.lp.client.pc.LPMain;
import com.lp.client.personal.PersonalFilterFactory;
import com.lp.client.rechnung.InternalFrameRechnung;
import com.lp.client.rechnung.PanelDialogNeuDatum;
import com.lp.client.rechnung.RechnungFilterFactory;
import com.lp.client.system.SystemFilterFactory;
import com.lp.server.auftrag.service.AuftragpositionDto;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.bestellung.service.BestellungDto;
import com.lp.server.inserat.service.InseratDto;
import com.lp.server.inserat.service.InseratFac;
import com.lp.server.inserat.service.InseratartikelDto;
import com.lp.server.inserat.service.InseratrechnungDto;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.util.Facade;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.FilterKriteriumDirekt;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.service.BelegpositionDto;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

@SuppressWarnings("static-access")
/*
 * <p>Tabbed pane fuer Komponente Auftrag.</p> <p>Copyright Logistik Pur
 * Software GmbH (c) 2004-2008</p> <p>Erstellungsdatum 2004-10-28</p> <p> </p>
 * 
 * @author Uli Walch
 * 
 * @version $Revision: 1.61 $
 */
public class TabbedPaneInserat extends TabbedPane {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private PanelQuery inseratAuswahl = null; // FLR Liste
	private PanelInseratKopfdaten inseratKopfdaten = null;

	private PanelSplit panelSplitInseratrechnung = null;
	private PanelQuery panelQueryInseratrechnung = null;
	private PanelInseratKunden panelBottomInseratrechnung = null;

	public java.sql.Date neuDatum = null;

	private PanelQuery panelQueryInserater = null;

	private PanelSplit panelSplitInseratartikel = null;
	private PanelQuery panelQueryInseratartikel = null;
	private PanelInseratArtikel panelBottomInseratartikel = null;

	private Integer inseratIId;

	public static final int IDX_PANEL_INSERATAUSWAHL = 0;
	public static final int IDX_PANEL_INSERATKOPFDATEN = 1;
	public static final int IDX_PANEL_INSERATRECHNUNG = 2;
	public static final int IDX_PANEL_INSERATER = 3;
	public static final int IDX_PANEL_INSERATARTIKEL = 4;

	protected final static String MENUE_ACTION_NEU_DATUM = "MENUE_ACTION_NEU_DATUM";
	protected final static String MENUE_ACTION_VERTRETER_AENDERN = "MENUE_ACTION_VERTRETER_AENDERN";

	private final String MENUE_BEARBEITEN_ER_ERFASSEN = "MENUE_BEARBEITEN_ER_ERFASSEN";
	private final String MENUE_BEARBEITEN_BESTELLUNGEN_AUSLOSEN = "MENUE_BEARBEITEN_BESTELLUNGEN_AUSLOSEN";
	private final String MENUE_BEARBEITEN_RECHNUNGEN_AUSLOSEN = "MENUE_BEARBEITEN_RECHNUNGEN_AUSLOSEN";

	private final String MENUE_BEARBEITEN_MANUELL_ERLEDIGEN = "MENUE_BEARBEITEN_MANUELL_ERLEDIGEN";

	private final String MENUE_JOURNAL_ZU_VERRECHNEN = "MENUE_JOURNAL_ZU_VERRECHNEN";
	private final String MENUE_JOURNAL_ALLE = "MENUE_JOURNAL_ALLE";
	private final String MENUE_JOURNAL_OFFENE = "MENUE_JOURNAL_OFFENE";
	private final String MENUE_JOURNAL_DBAUSWERTUNG = "MENUE_JOURNAL_DBAUSWERTUNG";

	private final String MENUE_INSERAT_DRUCKEN = "MENUE_INSERAT_DRUCKEN";

	private String MY_OWN_NEW_TOGGLE_ERSCHIENEN = PanelBasis.ACTION_MY_OWN_NEW
			+ "MY_OWN_NEW_TOGGLE_ERSCHIENEN";
	private String MY_OWN_NEW_STOPPEN = PanelBasis.ACTION_MY_OWN_NEW
			+ "MY_OWN_NEW_STOPPEN";

	private String MY_OWN_NEW_VERRECHENBAR_MARKIEREN = PanelBasis.ACTION_MY_OWN_NEW
			+ "MY_OWN_NEW_VERRECHENBAR_MARKIEREN";

	private String MY_OWN_NEW_INSERAT_KOPIEREN = PanelBasis.ACTION_MY_OWN_NEW
			+ "MY_OWN_NEW_INSERAT_KOPIEREN";
	private String MY_OWN_NEW_BESTELLUNG_FUER_SELEKTIERTEN_KUNDEN = PanelBasis.ACTION_MY_OWN_NEW
			+ "MY_OWN_NEW_BESTELLUNG_FUER_SELEKTIERTEN_KUNDEN";

	private JLabel inseraterSumme = null;

	PanelQueryFLRGoto panelQueryFLRLieferant = null;
	PanelQueryFLRGoto panelQueryFLRKunde = null;
	PanelQueryFLRGoto panelQueryFLRLieferantERAnlegen = null;
	PanelQueryFLR panelQueryFLRInserateOhneER = null;

	public Timestamp tZuletztVerwendeterTermin = null;

	private PanelQueryFLR panelQueryFLRPersonal = null;

	Integer kundeIIdFuerBestellungenAusloesenSelektiert = null;

	private InseratDto inseratDto = null;

	public TabbedPaneInserat(InternalFrame internalFrameI) throws Throwable {
		super(internalFrameI, LPMain.getInstance().getTextRespectUISPr(
				"iv.inserat.modulname"));

		jbInit();
		initComponents();
	}

	public InternalFrameInserat getInternalFrameInserat() throws Throwable {
		return (InternalFrameInserat) getInternalFrame();
	}

	public PanelQuery getAuftragAuswahl() {
		return inseratAuswahl;
	}

	public PanelBasis getAuftragKopfdaten() {
		return inseratKopfdaten;
	}

	public InseratDto getDto() {
		return inseratDto;
	}

	public void setInseratDto(InseratDto dto) {
		inseratDto = dto;
	}

	private void jbInit() throws Throwable {
		// dafuer sorgen, dass die Dtos != null sind
		resetDtos();

		// die Liste der Auftraege aufbauen
		refreshAuswahl();

		// den aktuell gewaehlten Auftrag hinterlegen und den Lock setzen
		setKeyWasForLockMe();

		insertTab(LPMain.getInstance()
				.getTextRespectUISPr("iv.inserat.auswahl"), null,
				inseratAuswahl,
				LPMain.getInstance().getTextRespectUISPr("iv.inserat.auswahl"),
				IDX_PANEL_INSERATAUSWAHL);

		// die restlichen Panels erst bei Bedarf laden
		insertTab(
				LPMain.getInstance()
						.getTextRespectUISPr("iv.inserat.kopfdaten"), null,
				null,
				LPMain.getInstance()
						.getTextRespectUISPr("iv.inserat.kopfdaten"),
				IDX_PANEL_INSERATKOPFDATEN);

		insertTab(
				LPMain.getInstance().getTextRespectUISPr(
						"iv.inserat.inseratkunden"),
				null,
				null,
				LPMain.getInstance().getTextRespectUISPr(
						"iv.inserat.inseratkunden"), IDX_PANEL_INSERATRECHNUNG);
		insertTab(
				LPMain.getInstance()
						.getTextRespectUISPr("iv.inserat.inserater"), null,
				null,
				LPMain.getInstance()
						.getTextRespectUISPr("iv.inserat.inserater"),
				IDX_PANEL_INSERATER);
		insertTab(
				LPMain.getInstance().getTextRespectUISPr("iv.inseratartikel"),
				null, null,
				LPMain.getInstance().getTextRespectUISPr("iv.inseratartikel"),
				IDX_PANEL_INSERATARTIKEL);

		addChangeListener(this);

		getInternalFrame().addItemChangedListener(this);
	}

	private void refreshAuswahl() throws Throwable {
		if (inseratAuswahl == null) {

			FilterKriterium[] filterAuswahl = SystemFilterFactory.getInstance()
					.createFKMandantCNr();
			String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW };

			inseratAuswahl = new PanelQuery(null, filterAuswahl,
					QueryParameters.UC_ID_INSERAT, aWhichButtonIUse,
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("iv.inserat.auswahl"), true);

			inseratAuswahl.createAndSaveAndShowButton(
					"/com/lp/client/res/news16x16.png", LPMain
							.getTextRespectUISPr("iv.inserat.kopieren"),
					MY_OWN_NEW_INSERAT_KOPIEREN, KeyStroke.getKeyStroke('K',
							java.awt.event.InputEvent.CTRL_MASK),
					RechteFac.RECHT_IV_INSERAT_CUD);

			inseratAuswahl.createAndSaveAndShowButton(
					"/com/lp/client/res/status_erschienen.png", LPMain
							.getTextRespectUISPr("iv.button.erschienen"),
					MY_OWN_NEW_TOGGLE_ERSCHIENEN, KeyStroke.getKeyStroke('E',
							java.awt.event.InputEvent.CTRL_MASK),
					RechteFac.RECHT_IV_INSERAT_CUD);

			inseratAuswahl.createAndSaveAndShowButton(
					"/com/lp/client/res/status_gestoppt.png", LPMain
							.getTextRespectUISPr("iv.inserat.stoppen"),
					MY_OWN_NEW_STOPPEN, KeyStroke.getKeyStroke('S',
							java.awt.event.InputEvent.CTRL_MASK),
					RechteFac.RECHT_IV_INSERAT_CUD);

			inseratAuswahl
					.createAndSaveAndShowButton(
							"/com/lp/client/res/shoppingcart_full16x16.png",
							LPMain.getTextRespectUISPr("iv.inserat.bestellung.fuer.selektiertenkunden"),
							MY_OWN_NEW_BESTELLUNG_FUER_SELEKTIERTEN_KUNDEN,
							KeyStroke.getKeyStroke('B',
									java.awt.event.InputEvent.CTRL_MASK),
							RechteFac.RECHT_IV_INSERAT_CUD);

			inseratAuswahl
					.createAndSaveAndShowButton(
							"/com/lp/client/res/calculator16x16.png",
							LPMain.getTextRespectUISPr("iv.inserat.toggle.verrechenbar"),
							MY_OWN_NEW_VERRECHENBAR_MARKIEREN,
							RechteFac.RECHT_IV_INSERAT_CUD);

			FilterKriteriumDirekt fkDirekt1 = InseratFilterFactory
					.getInstance().createFKDInseratnummer();

			inseratAuswahl.befuellePanelFilterkriterienDirekt(fkDirekt1,
					InseratFilterFactory.getInstance().createFKDStichwort());
			FilterKriteriumDirekt fkDirekt3 = InseratFilterFactory
					.getInstance().createFKDKunde();
			inseratAuswahl.addDirektFilter(fkDirekt3);
			FilterKriteriumDirekt fkDirekt2 = InseratFilterFactory
					.getInstance().createFKDLieferant();
			inseratAuswahl.addDirektFilter(fkDirekt2);

			Map mStati = new LinkedHashMap();
			mStati.put(LocaleFac.STATUS_ANGELEGT, LocaleFac.STATUS_ANGELEGT);
			mStati.put(LocaleFac.STATUS_BESTELLT, LocaleFac.STATUS_BESTELLT);
			mStati.put(LocaleFac.STATUS_ERSCHIENEN, LocaleFac.STATUS_ERSCHIENEN);
			mStati.put(LocaleFac.STATUS_VERRECHENBAR,
					LocaleFac.STATUS_VERRECHENBAR);
			mStati.put(LocaleFac.STATUS_ERLEDIGT, LocaleFac.STATUS_ERLEDIGT);
			mStati.put(LocaleFac.STATUS_GESTOPPT, LocaleFac.STATUS_GESTOPPT);
			mStati.put(LocaleFac.STATUS_VERRECHNET, LocaleFac.STATUS_VERRECHNET);

			mStati.put(LocaleFac.STATUS_STORNIERT, LocaleFac.STATUS_STORNIERT);

			inseratAuswahl.setFilterComboBox(mStati, new FilterKriterium(
					InseratFac.FLR_INSERAT_STATUS_C_NR, true, "" + "",
					FilterKriterium.OPERATOR_EQUAL, false, true), false, LPMain
					.getTextRespectUISPr("lp.alle"));

			inseratAuswahl
					.befuelleFilterkriteriumSchnellansicht(InseratFilterFactory
							.getInstance().createFKSchnellansicht());

		}
	}

	/**
	 * AGILPRO CHANGES Changed visiblity from protected to public
	 * 
	 * @author Lukas Lisowski
	 * @param e
	 *            ChangeEvent
	 * @throws Throwable
	 */
	public void lPEventObjectChanged(ChangeEvent e) throws Throwable {
		super.lPEventObjectChanged(e);
		inseratIId = inseratDto.getIId();
		// dtos hinterlegen
		initializeDtos(inseratIId);

		int selectedIndex = getSelectedIndex();

		switch (selectedIndex) {
		case IDX_PANEL_INSERATAUSWAHL:
			setTitleAuftrag(LPMain.getInstance().getTextRespectUISPr(
					"auft.title.panel.auswahl"));
			refreshAuswahl();
			inseratAuswahl.eventYouAreSelected(false);
			inseratAuswahl.updateButtons();

			if (getAuftragAuswahl().getSelectedId() == null) {
				getInternalFrame().enableAllOberePanelsExceptMe(this,
						IDX_PANEL_INSERATAUSWAHL, false);
			}
			break;

		case IDX_PANEL_INSERATKOPFDATEN:
			refreshKopfdaten();
			inseratKopfdaten.eventYouAreSelected(false); // sonst werden die
			// buttons nicht
			// richtig gesetzt!
			break;
		case IDX_PANEL_INSERATRECHNUNG:

			createInseratrechnung(getDto().getIId());
			panelSplitInseratrechnung.eventYouAreSelected(false);
			panelQueryInseratrechnung.updateButtons();

			break;
		case IDX_PANEL_INSERATARTIKEL:

			createInseratartikel(getDto().getIId());
			panelSplitInseratartikel.eventYouAreSelected(false);
			panelQueryInseratartikel.updateButtons();

			break;
		case IDX_PANEL_INSERATER:

			createInserater(getDto().getIId());
			panelQueryInserater.eventYouAreSelected(false);

			setSelectedComponent(panelQueryInserater);

			panelQueryInserater.updateButtons(new LockStateValue(null, null,
					PanelBasis.LOCK_IS_NOT_LOCKED));

			break;

		}

		setTitleAuftrag("");
	}

	private void createInserater(Integer key) throws Throwable {
		FilterKriterium[] filters = InseratFilterFactory.getInstance()
				.createFKInserater(key);
		if (panelQueryInserater == null) {

			String[] aWhichButtonIUse = {};

			panelQueryInserater = new PanelQuery(null, filters,
					QueryParameters.UC_ID_EINGANGSRECHNUNGEN_EINES_INSERATES,
					aWhichButtonIUse, getInternalFrame(),
					LPMain.getTextRespectUISPr("auft.title.panel.lose"), true);

			inseraterSumme = new JLabel();
			panelQueryInserater.getToolBar().getToolsPanelCenter()
					.add(inseraterSumme);
			this.setComponentAt(IDX_PANEL_INSERATER, panelQueryInserater);
		}

		BigDecimal bdSumme = Helper.rundeGeldbetrag(DelegateFactory
				.getInstance().getInseratDelegate()
				.getZuEingangsrechnungenZugeordnetenWert(getDto().getIId()));

		// Preise berechnen
		InseratartikelDto[] inseratartikelDtos = DelegateFactory.getInstance()
				.getInseratDelegate()
				.inseratartikelFindByInseratIId(inseratDto.getIId());

		BigDecimal preisZusatzEK = new BigDecimal(0);

		for (int i = 0; i < inseratartikelDtos.length; i++) {
			preisZusatzEK = preisZusatzEK.add(inseratartikelDtos[i].getNMenge()
					.multiply(inseratartikelDtos[i].getNNettoeinzelpreisEk()));

		}

		BigDecimal bdWertAusKopfdaten = Helper
				.rundeGeldbetrag(inseratDto
						.getErrechneterWertEK(
								Defaults.getInstance()
										.getIUINachkommastellenPreiseVK()).add(
								preisZusatzEK));

		inseraterSumme.setText(LPMain.getTextRespectUISPr("iv.er.summe")
				+ " = "
				+ Helper.formatZahl(bdSumme, 2, LPMain.getTheClient()
						.getLocUi()));

		if (bdWertAusKopfdaten.doubleValue() != bdSumme.doubleValue()) {
			inseraterSumme.setForeground(Color.RED);
		} else {
			inseraterSumme.setForeground(Color.BLACK);
		}

		panelQueryInserater.setDefaultFilter(filters);
	}

	private void createInseratrechnung(Integer key) throws Throwable {

		if (panelQueryInseratrechnung == null) {
			String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW,
					PanelBasis.ACTION_POSITION_VONNNACHNMINUS1,
					PanelBasis.ACTION_POSITION_VONNNACHNPLUS1 };

			FilterKriterium[] filters = InseratFilterFactory.getInstance()
					.createFKInseratrechnung(key);

			panelQueryInseratrechnung = new PanelQuery(null, filters,
					QueryParameters.UC_ID_INSERATRECHNUNG, aWhichButtonIUse,
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("iv.inserat.inseratkunden"),
					true);

			panelBottomInseratrechnung = new PanelInseratKunden(
					getInternalFrameInserat(), LPMain.getInstance()
							.getTextRespectUISPr("iv.inserat.inseratkunden"),
					null);

			panelSplitInseratrechnung = new PanelSplit(getInternalFrame(),
					panelBottomInseratrechnung, panelQueryInseratrechnung, 280);
			setComponentAt(IDX_PANEL_INSERATRECHNUNG, panelSplitInseratrechnung);
		} else {
			// filter refreshen.
			panelQueryInseratrechnung.setDefaultFilter(InseratFilterFactory
					.getInstance().createFKInseratrechnung(key));
		}
	}

	private void createInseratartikel(Integer key) throws Throwable {

		if (panelQueryInseratartikel == null) {
			String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW };

			FilterKriterium[] filters = InseratFilterFactory.getInstance()
					.createFKInseratartikel(key);

			panelQueryInseratartikel = new PanelQuery(null, filters,
					QueryParameters.UC_ID_INSERATARTIKEL, aWhichButtonIUse,
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("iv.inseratartikel"), true);

			panelBottomInseratartikel = new PanelInseratArtikel(
					getInternalFrameInserat(), LPMain.getInstance()
							.getTextRespectUISPr("iv.inseratartikel"), null);

			panelSplitInseratartikel = new PanelSplit(getInternalFrame(),
					panelBottomInseratartikel, panelQueryInseratartikel, 280);
			setComponentAt(IDX_PANEL_INSERATARTIKEL, panelSplitInseratartikel);
		} else {
			// filter refreshen.
			panelQueryInseratartikel.setDefaultFilter(InseratFilterFactory
					.getInstance().createFKInseratartikel(key));
		}
	}

	public boolean darfInseratGeaendertWerden() {

		boolean bEsDarfGeaendertWerden = true;
		if (getDto().getStatusCNr().equals(LocaleFac.STATUS_VERRECHNET)
				|| getDto().getStatusCNr().equals(LocaleFac.STATUS_TEILBEZAHLT)
				|| getDto().getStatusCNr().equals(LocaleFac.STATUS_BEZAHLT)) {
			DialogFactory
					.showModalDialog(
							LPMain.getInstance()
									.getTextRespectUISPr("lp.error"),
							LPMain.getInstance().getTextRespectUISPr(
									"iv.inseratkunde.updatenichtmoeglich"));
			bEsDarfGeaendertWerden = false;
		}
		return bEsDarfGeaendertWerden;
	}

	public void lPEventItemChanged(ItemChangedEvent e) throws Throwable {

		if (e.getID() == ItemChangedEvent.ITEM_CHANGED) {
			handleItemChanged(e);
		} else if (e.getID() == ItemChangedEvent.ACTION_ESCAPE) {
			setSelectedComponent(getAuftragAuswahl());
		} else if (e.getID() == ItemChangedEvent.ACTION_UPDATE) {
			// hier kommt man nach upd im D bei einem 1:n hin.
			if (e.getSource() == panelBottomInseratrechnung) {
				panelQueryInseratrechnung.updateButtons(new LockStateValue(
						PanelBasis.LOCK_FOR_NEW));
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_YOU_ARE_SELECTED) {
			setTitleAuftrag("");

			super.lPEventItemChanged(e);
		}

		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			handleGotoDetailPanel(e);
		} else if (e.getID() == ItemChangedEvent.ACTION_MY_OWN_NEW) {
			String sAspectInfo = ((ISourceEvent) e.getSource()).getAspect();

			if (sAspectInfo.equals(MY_OWN_NEW_TOGGLE_ERSCHIENEN)) {
				if (getDto() != null && getDto().getIId() != null) {

					if (getDto().getStatusCNr().equals(
							LocaleFac.STATUS_BESTELLT)
							|| getDto().getStatusCNr().equals(
									LocaleFac.STATUS_ERSCHIENEN)) {

						DelegateFactory.getInstance().getInseratDelegate()
								.toggleErschienen(getDto().getIId());
						inseratAuswahl.eventYouAreSelected(false);
						inseratAuswahl.setSelectedId(getDto().getIId());
						initializeDtos(getDto().getIId());
					} else {
						DialogFactory
								.showModalDialog(
										LPMain.getTextRespectUISPr("lp.hinweis"),
										LPMain.getTextRespectUISPr("iv.erschienen.nichtmoeglich"));

						return;
					}
				}

			} else if (sAspectInfo.equals(MY_OWN_NEW_STOPPEN)) {
				if (getDto() != null && getDto().getIId() != null) {
					if (getDto().getStatusCNr().equals(
							LocaleFac.STATUS_VERRECHENBAR)
							|| getDto().getStatusCNr().equals(
									LocaleFac.STATUS_GESTOPPT)) {

						String begruendung = getDto().getCGestoppt();
						if (getDto().getStatusCNr().equals(
								LocaleFac.STATUS_VERRECHENBAR)) {
							begruendung = (String) JOptionPane.showInputDialog(
									getInternalFrame(), "Begr???ndung");
						}

						DelegateFactory.getInstance().getInseratDelegate()
								.toggleGestoppt(getDto().getIId(), begruendung);
						inseratAuswahl.eventYouAreSelected(false);
						inseratAuswahl.setSelectedId(getDto().getIId());
					} else {

						DialogFactory
								.showModalDialog(
										LPMain.getTextRespectUISPr("lp.hinweis"),
										LPMain.getTextRespectUISPr("iv.stoppen.nichtmoeglich"));

						return;
					}
					initializeDtos(getDto().getIId());
				}

			} else if (sAspectInfo.equals(MY_OWN_NEW_VERRECHENBAR_MARKIEREN)) {
				if (getDto() != null && getDto().getIId() != null) {
					if (getDto().getStatusCNr().equals(
							LocaleFac.STATUS_ERSCHIENEN)
							|| getDto().getStatusCNr().equals(
									LocaleFac.STATUS_VERRECHENBAR)) {

						DelegateFactory.getInstance().getInseratDelegate()
								.toggleVerrechenbar(getDto().getIId());

						inseratAuswahl.eventYouAreSelected(false);
					} else {

						DialogFactory
								.showModalDialog(
										LPMain.getTextRespectUISPr("lp.hinweis"),
										LPMain.getTextRespectUISPr("iv.verrechnbar.nichtmoeglich"));
						return;
					}
				}

			} else if (sAspectInfo.equals(MY_OWN_NEW_INSERAT_KOPIEREN)) {
				inseratKopieren();
			} else if (sAspectInfo
					.equals(MY_OWN_NEW_BESTELLUNG_FUER_SELEKTIERTEN_KUNDEN)) {
				if (inseratDto != null) {
					Integer kundeIId = inseratDto.getInseratrechnungDto()
							.getKundeIId();
					kundeIIdFuerBestellungenAusloesenSelektiert = kundeIId;
					bestellungenAusloesenKunde(kundeIId);
				}
			}

		} else if (e.getID() == ItemChangedEvent.ACTION_NEW) {
			if (e.getSource() == inseratAuswahl) {
				// um mit Auftraegen arbeiten zu koennen, muss das Hauptlager
				// des Mandanten definiert sein
				DelegateFactory.getInstance().getLagerDelegate()
						.getHauptlagerDesMandanten();

				// emptytable: 1 wenn es bisher keinen eintrag gibt, die
				// restlichen
				// panels aktivieren
				if (inseratAuswahl.getSelectedId() == null) {
					getInternalFrame().enableAllOberePanelsExceptMe(this,
							IDX_PANEL_INSERATAUSWAHL, true);
				}

				refreshKopfdaten().eventActionNew(e, true, false);
				setSelectedComponent(inseratKopfdaten);
			} else if (e.getSource() == panelQueryInseratrechnung) {
				panelBottomInseratrechnung.eventActionNew(e, true, false);
				panelBottomInseratrechnung.eventYouAreSelected(false);
				this.setSelectedComponent(panelSplitInseratrechnung);
			} else if (e.getSource() == panelQueryInseratartikel) {
				panelBottomInseratartikel.eventActionNew(e, true, false);
				panelBottomInseratartikel.eventYouAreSelected(false);
				this.setSelectedComponent(panelSplitInseratartikel);
			}
		} else

		// wir landen hier bei der Abfolge Button Aendern -> xx -> Button
		// Discard
		if (e.getID() == ItemChangedEvent.ACTION_DISCARD) {
			if (e.getSource() == inseratKopfdaten) {
				// aenderewaehrung: 6 die Kopfdaten muessen mit den
				// urspruenglichen Daten befuellt werden
				inseratKopfdaten.eventYouAreSelected(false);
				inseratKopfdaten.updateButtons(inseratKopfdaten
						.getLockedstateDetailMainKey());
			} else if (e.getSource() == panelBottomInseratrechnung) {
				panelSplitInseratrechnung.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomInseratartikel) {
				panelSplitInseratartikel.eventYouAreSelected(false);
			}

		} else
		// Wir landen hier nach Button Save
		if (e.getID() == ItemChangedEvent.ACTION_SAVE) {
			handleActionSave(e);
		} else
		// wir landen hier nach der Abfolge Button Neu -> xx -> Button Discard
		if (e.getID() == ItemChangedEvent.ACTION_GOTO_MY_DEFAULT_QP) {
			if (e.getSource() == inseratKopfdaten) {
				// btndiscard: 2 die Liste neu laden, falls sich etwas geaendert
				// hat
				inseratAuswahl.eventYouAreSelected(false);

				// btndiscard: 3 nach einem Discard ist der aktuelle Key nicht
				// mehr gesetzt
				setKeyWasForLockMe();

				// btndiscard: 4 der Key der Kopfdaten steht noch auf new|...
				inseratKopfdaten.setKeyWhenDetailPanel(inseratAuswahl
						.getSelectedId());

				// btndiscard: 5 auf die Auswahl schalten
				setSelectedComponent(inseratAuswahl);

				// wenn es fuer das tabbed pane noch keinen eintrag gibt, die
				// restlichen panel deaktivieren
				if (inseratAuswahl.getSelectedId() == null) {
					getInternalFrame().enableAllOberePanelsExceptMe(this,
							IDX_PANEL_INSERATAUSWAHL, false);
				}

				inseratKopfdaten.updateButtons(inseratKopfdaten
						.getLockedstateDetailMainKey());

			} else if (e.getSource() == panelBottomInseratrechnung) {
				setKeyWasForLockMe();
				panelSplitInseratrechnung.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomInseratartikel) {
				setKeyWasForLockMe();
				panelSplitInseratartikel.eventYouAreSelected(false);
			}
		} else
		// der OK Button in einem PanelDialog wurde gedrueckt
		if (e.getID() == ItemChangedEvent.ACTION_KRITERIEN_HAVE_BEEN_SELECTED) {

		} else if (e.getID() == ItemChangedEvent.ACTION_POSITION_VONNNACHNMINUS1) {
			if (e.getSource() == panelQueryInseratrechnung) {
				int iPos = panelQueryInseratrechnung.getTable()
						.getSelectedRow();

				// wenn die Position nicht die erste ist
				if (iPos > 0) {
					Integer iIdPosition = (Integer) panelQueryInseratrechnung
							.getSelectedId();

					Integer iIdPositionMinus1 = (Integer) panelQueryInseratrechnung
							.getTable().getValueAt(iPos - 1, 0);
					DelegateFactory
							.getInstance()
							.getInseratDelegate()
							.vertauscheInseratrechnung(iIdPosition,
									iIdPositionMinus1);

					// die Liste neu anzeigen und den richtigen Datensatz
					// markieren

					panelQueryInseratrechnung.setSelectedId(iIdPosition);
				}
			}
		} else

		if (e.getID() == ItemChangedEvent.ACTION_POSITION_VONNNACHNPLUS1) {
			if (e.getSource() == panelQueryInseratrechnung) {
				int iPos = panelQueryInseratrechnung.getTable()
						.getSelectedRow();

				// wenn die Position nicht die letzte ist
				if (iPos < panelQueryInseratrechnung.getTable().getRowCount() - 1) {
					Integer iIdPosition = (Integer) panelQueryInseratrechnung
							.getSelectedId();

					Integer iIdPositionPlus1 = (Integer) panelQueryInseratrechnung
							.getTable().getValueAt(iPos + 1, 0);
					DelegateFactory
							.getInstance()
							.getInseratDelegate()
							.vertauscheInseratrechnung(iIdPosition,
									iIdPositionPlus1);

					// die Liste neu anzeigen und den richtigen Datensatz
					// markieren
					panelQueryInseratrechnung.setSelectedId(iIdPosition);
				}
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_POSITION_VORPOSITIONEINFUEGEN) {

		} else if (e.getID() == ItemChangedEvent.ACTION_KOPIEREN) {

		}

	}

	public void inseratKopieren() throws ExceptionLP, Throwable {
		Integer inseratIId = (Integer) inseratAuswahl.getSelectedId();
		if (inseratIId != null) {
			// PJ17996 Inserat kopieren
			setSelectedComponent(inseratKopfdaten);

			InseratDto inseratDto = DelegateFactory.getInstance()
					.getInseratDelegate().inseratFindByPrimaryKey(inseratIId);
			inseratDto.setStatusCNr(LocaleFac.STATUS_ANGELEGT);
			inseratDto.setBestellpositionIId(null);
			inseratDto.setTErschienen(null);
			inseratDto.setTGestoppt(null);
			inseratDto.setCGestoppt(null);
			inseratDto.setTManuellerledigt(null);
			inseratDto.setPersonalIIdGestoppt(null);
			inseratDto.setPersonalIIdErschienen(null);
			inseratDto.setPersonalIIdManuellerledigt(null);
			inseratDto.setCNr(null);
			inseratDto.setIId(null);
			inseratDto.setXAnhangLf(null);
			inseratDto.setTBelegdatum(Helper
					.cutTimestamp(new java.sql.Timestamp(System
							.currentTimeMillis())));
			inseratDto.setTVerrechnen(null);
			inseratDto.setPersonalIIdVerrechnen(null);
			inseratDto.setTManuellverrechnen(null);
			inseratDto.setPersonalIIdManuellverrechnen(null);
			inseratDto.inseratIId_Kopiertvon = inseratIId;

			if (inseratDto.getXAnhang() != null
					&& inseratDto.getXAnhang().length() > 0) {
				boolean b = DialogFactory
						.showModalJaNeinDialog(
								getInternalFrame(),
								LPMain.getTextRespectUISPr("iv.inserkopieren.mitkundenanhang"));
				if (b == false) {
					inseratDto.setXAnhang(null);
				}
			}
			inseratKopfdaten.eventActionNew(null, false, false);
			inseratKopfdaten.eventYouAreSelected(false);
			setInseratDto(inseratDto);
			setTitleAuftrag("");
			inseratKopfdaten.dto2Components();
			inseratKopfdaten.setzeArtikelvorhandenLabelSichtbar(inseratIId);

			// SP1820
			if (inseratDto.getInseratrechnungDto() != null
					&& inseratDto.getInseratrechnungDto().getKundeIId() != null) {
				DelegateFactory
						.getInstance()
						.getKundeDelegate()
						.pruefeKunde(
								inseratDto.getInseratrechnungDto()
										.getKundeIId(), null,
								getInternalFrame());
			}
			if (inseratDto.getLieferantIId() != null) {
				DelegateFactory
						.getInstance()
						.getLieferantDelegate()
						.pruefeLieferant(inseratDto.getLieferantIId(), null,
								getInternalFrame());
			}

		}
	}

	public void bestellungenAusloesenKunde(Integer kundeIId)
			throws ExceptionLP, Throwable {
		ArrayList<Integer> lieferantIIds = DelegateFactory.getInstance()
				.getInseratDelegate()
				.getAllLieferantIIdsAusInseratenOhneBestellung(kundeIId);

		if (lieferantIIds == null || lieferantIIds.size() == 0) {
			// Fehler- Keine Inserate ohne Bestellungen vorhanden
			DialogFactory
					.showModalDialog(
							LPMain.getTextRespectUISPr("lp.info"),
							LPMain.getTextRespectUISPr("iv.info.keineinserateohnebestellungvorhanden"));
			return;
		} else {

			if (lieferantIIds.size() == 1) {
				Integer lieferantIId = lieferantIIds.iterator().next();
				ArrayList<Integer> bestellungIIds = DelegateFactory
						.getInstance().getInseratDelegate()
						.bestellungenAusloesen(lieferantIId, kundeIId);
				kundeIIdFuerBestellungenAusloesenSelektiert = null;
				inseratAuswahl.eventYouAreSelected(false);

				for (int i = 0; i < bestellungIIds.size(); i++) {
					bestellungDrucken(bestellungIIds.get(i));
				}

			} else {

				String[] aWhichButtonIUse = SystemFilterFactory.getInstance()
						.createButtonArray(false, false);

				FilterKriterium[] fk = InseratFilterFactory.getInstance()
						.createFKLieferantenOhneBestellungen(lieferantIIds);
				panelQueryFLRLieferant = new PanelQueryFLRGoto(
						null,
						fk,
						QueryParameters.UC_ID_LIEFERANTEN,
						aWhichButtonIUse,
						getInternalFrame(),
						LocaleFac.BELEGART_LIEFERANT,
						LPMain.getTextRespectUISPr("title.lieferantenauswahlliste"),
						null);
				new DialogQuery(panelQueryFLRLieferant);
			}
		}
	}

	private void handleActionSave(ItemChangedEvent e) throws Throwable {
		if (e.getSource() == inseratKopfdaten) {
			Object pkAuftrag = inseratKopfdaten.getKeyWhenDetailPanel();
			initializeDtos((Integer) pkAuftrag);
			getInternalFrame().setKeyWasForLockMe(pkAuftrag.toString());

			inseratAuswahl.clearDirektFilter();
			inseratAuswahl.eventYouAreSelected(false);
			inseratAuswahl.setSelectedId(pkAuftrag);
			inseratAuswahl.eventYouAreSelected(false);

		} else if (e.getSource() == panelBottomInseratrechnung) {
			Object oKey = panelBottomInseratrechnung.getKeyWhenDetailPanel();
			panelQueryInseratrechnung.eventYouAreSelected(false);
			panelQueryInseratrechnung.setSelectedId(oKey);
			panelSplitInseratrechnung.eventYouAreSelected(false);

		} else if (e.getSource() == panelBottomInseratartikel) {
			Object oKey = panelBottomInseratartikel.getKeyWhenDetailPanel();
			panelQueryInseratartikel.eventYouAreSelected(false);
			panelQueryInseratartikel.setSelectedId(oKey);
			panelSplitInseratartikel.eventYouAreSelected(false);

		}

	}

	/**
	 * Verarbeitung von ItemChangedEvent.GOTO_DETAIL_PANEL.
	 * 
	 * @param e
	 *            ItemChangedEvent
	 * @throws Throwable
	 *             Ausnahme
	 */

	private void bestellungDrucken(Integer bestellungIId) throws Throwable {
		if (LPMain.getInstance().getDesktop()
				.darfAnwenderAufModulZugreifen(LocaleFac.BELEGART_BESTELLUNG)
				&& bestellungIId != null) {
			InternalFrameBestellung ifAB = (InternalFrameBestellung) LPMain
					.getInstance().getDesktop()
					.holeModul(LocaleFac.BELEGART_BESTELLUNG);
			ifAB.geheZu(InternalFrameBestellung.IDX_PANE_BESTELLUNG,
					TabbedPaneBestellung.IDX_PANEL_AUSWAHL, bestellungIId,
					null, BestellungFilterFactory.getInstance()
							.createFKBestellungKey((Integer) bestellungIId));
			ifAB.getTabbedPaneBestellung().printBestellung();
		}
	}

	private void handleGotoDetailPanel(ItemChangedEvent e) throws Throwable {
		if (e.getSource() == inseratAuswahl) {
			Integer auftragIId = (Integer) inseratAuswahl.getSelectedId();
			initializeDtos(auftragIId); // befuellt den Auftrag und den Kunden
			getInternalFrame().setKeyWasForLockMe(auftragIId + "");
			if (auftragIId != null) {
				setSelectedComponent(inseratKopfdaten);
			}

		} else if (e.getSource() == panelQueryFLRKunde) {
			kundeIIdFuerBestellungenAusloesenSelektiert = (Integer) panelQueryFLRKunde
					.getSelectedId();
			bestellungenAusloesenKunde(kundeIIdFuerBestellungenAusloesenSelektiert);
		} else if (e.getSource() == panelQueryFLRPersonal) {
			Integer personalId_Vertreter = (Integer) panelQueryFLRPersonal
					.getSelectedId();
			DelegateFactory
					.getInstance()
					.getInseratDelegate()
					.inseratVertreterAendern(getDto().getIId(),
							personalId_Vertreter);
			inseratAuswahl.eventYouAreSelected(false);
		} else if (e.getSource() == panelQueryFLRLieferant) {
			Integer lieferantIId = (Integer) panelQueryFLRLieferant
					.getSelectedId();
			ArrayList<Integer> bestellungIIds = DelegateFactory
					.getInstance()
					.getInseratDelegate()
					.bestellungenAusloesen(lieferantIId,
							kundeIIdFuerBestellungenAusloesenSelektiert);

			kundeIIdFuerBestellungenAusloesenSelektiert = null;
			inseratAuswahl.eventYouAreSelected(false);

			for (int i = 0; i < bestellungIIds.size(); i++) {
				bestellungDrucken(bestellungIIds.get(i));
			}

		} else if (e.getSource() == panelQueryFLRLieferantERAnlegen) {
			Integer lieferantIId = (Integer) panelQueryFLRLieferantERAnlegen
					.getSelectedId();

			// Nun Liste der Inserate Ohne ER
			String[] aWhichButtonIUse = SystemFilterFactory.getInstance()
					.createButtonArray(false, false);

			FilterKriterium[] fk = InseratFilterFactory.getInstance()
					.createFKInserateEinesLieferantenOhneER(lieferantIId);
			panelQueryFLRInserateOhneER = new PanelQueryFLR(null, fk,
					QueryParameters.UC_ID_INSERATE_OHNE_ER, aWhichButtonIUse,
					getInternalFrame(),
					LPMain.getTextRespectUISPr("iv.inserat.inserateohneer"),
					null, null);
			panelQueryFLRInserateOhneER.setMultipleRowSelectionEnabled(true);
			new DialogQuery(panelQueryFLRInserateOhneER);

		} else if (e.getSource() == panelQueryFLRInserateOhneER) {
			Object[] inserateIId = (Object[]) panelQueryFLRInserateOhneER
					.getSelectedIds();
			// Nun Liste der Inserate Ohne ER

			CommandCreateIF createIF = new CommandCreateIF(
					LocaleFac.BELEGART_EINGANGSRECHNUNG);
			LPMain.getInstance().execute(createIF);
			InternalFrame intFrameTo = LPMain.getInstance().getDesktop()
					.getLPModul(LocaleFac.BELEGART_EINGANGSRECHNUNG);

			Command2IFNebeneinander command2IFNebeneinander = new Command2IFNebeneinander(
					LocaleFac.BELEGART_EINGANGSRECHNUNG);
			command2IFNebeneinander.setSInternalFrame2I(getInternalFrame()
					.getBelegartCNr());
			LPMain.getInstance().execute(command2IFNebeneinander);
			CommandSetFocus setFocusCmd = new CommandSetFocus(
					getInternalFrame().getBelegartCNr());

			InternalFrameEingangsrechnung intFrameEr = (InternalFrameEingangsrechnung) intFrameTo;
			TabbedPaneEingangsrechnung tbEr = intFrameEr
					.getTabbedPaneEingangsrechnung();

			tbEr.erstelleEingangsrechnungausInseraten(inserateIId);

			LPMain.getInstance().execute(setFocusCmd);

			inseratAuswahl.eventYouAreSelected(false);
		}
	}

	/**
	 * Ein ItemChangedEvent mit code ITEM_CHANGED ist eingelangt.
	 * 
	 * @param e
	 *            das ItemChangedEvent
	 * @throws Throwable
	 */
	private void handleItemChanged(ItemChangedEvent e) throws Throwable {
		if (e.getSource() == inseratAuswahl) {
			Integer iIdAuftrag = (Integer) inseratAuswahl.getSelectedId();
			initializeDtos(iIdAuftrag);
			setKeyWasForLockMe();
			refreshKopfdaten().setKeyWhenDetailPanel(iIdAuftrag);

			setTitleAuftrag(LPMain.getInstance().getTextRespectUISPr(
					"auft.title.panel.auswahl"));

			// fuer die leere Tabelle
			if (iIdAuftrag == null) {
				getInternalFrame().enableAllOberePanelsExceptMe(this,
						IDX_PANEL_INSERATAUSWAHL, false);
			} else {
				getInternalFrame().enableAllOberePanelsExceptMe(this,
						IDX_PANEL_INSERATAUSWAHL, true);
			}
		} else if (e.getSource() == panelQueryInseratrechnung) {
			Object key = ((ISourceEvent) e.getSource()).getIdSelected();
			panelBottomInseratrechnung.setKeyWhenDetailPanel(key);
			panelBottomInseratrechnung.eventYouAreSelected(false);
			panelQueryInseratrechnung.updateButtons();
		} else if (e.getSource() == panelQueryInseratartikel) {
			Object key = ((ISourceEvent) e.getSource()).getIdSelected();
			panelBottomInseratartikel.setKeyWhenDetailPanel(key);
			panelBottomInseratartikel.eventYouAreSelected(false);
			panelQueryInseratartikel.updateButtons();
		}

	}

	private void initializeDtos(Integer inseratIId) throws Throwable {
		if (inseratIId != null) {
			inseratDto = DelegateFactory.getInstance().getInseratDelegate()
					.inseratFindByPrimaryKey(inseratIId);

		}
	}

	private PanelBasis refreshKopfdaten() throws Throwable {
		Integer iIdAuftrag = null;

		if (inseratKopfdaten == null) {
			// typkeyfordetail: Der Auftrag hat einen Key vom Typ Integer
			if (getInternalFrame().getKeyWasForLockMe() != null) {
				iIdAuftrag = new Integer(Integer.parseInt(getInternalFrame()
						.getKeyWasForLockMe()));
			}

			inseratKopfdaten = new PanelInseratKopfdaten(getInternalFrame(),
					LPMain.getInstance().getTextRespectUISPr(
							"auft.title.panel.kopfdaten"), iIdAuftrag); // empty
			// bei
			// leerer
			// Liste

			setComponentAt(IDX_PANEL_INSERATKOPFDATEN, inseratKopfdaten);
		}

		return inseratKopfdaten;
	}

	/**
	 * Den Text der Titelleiste ueberschreiben.
	 * 
	 * @param panelTitle
	 *            der Title des aktuellen panel
	 * @throws Exception
	 */
	public void setTitleAuftrag(String panelTitle) throws Throwable {
		// aktuelle auftragnummer bestimmen
		StringBuffer auftragnummer = new StringBuffer("");
		if (inseratDto == null || inseratDto.getIId() == null) {
			auftragnummer.append(LPMain.getInstance().getTextRespectUISPr(
					"iv.title.neuesinserat"));
		} else {

			auftragnummer
					.append(LPMain.getInstance().getTextRespectUISPr(
							"iv.inserat.modulname")).append(" ")
					.append(inseratDto.getCNr());

		}

		// dem Panel die Titel setzen
		getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_OHRWASCHLOBEN,
				panelTitle);
		getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_AS_I_LIKE,
				auftragnummer.toString());
	}

	public void setTitleAuftragOhneAuftragnummer(String panelTitle)
			throws Exception {
		// dem Panel die Titel setzen
		getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_OHRWASCHLOBEN,
				panelTitle);
		getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_AS_I_LIKE, "");
	}

	public void resetDtos() {
		inseratDto = new InseratDto();

	}

	public boolean pruefeAktuellenAuftrag() {
		boolean bIstGueltig = true;

		if (inseratDto == null || inseratDto.getIId() == null) {
			bIstGueltig = false;
			DialogFactory.showModalDialog(LPMain.getInstance()
					.getTextRespectUISPr("lp.warning"), LPMain.getInstance()
					.getTextRespectUISPr("auft.warning.keinauftrag"));
		}

		return bIstGueltig;
	}

	public boolean aktuellerAuftragHatPositionen() throws Throwable {
		boolean bHatPositionen = true;

		if (DelegateFactory.getInstance().getAuftragpositionDelegate()
				.getAnzahlMengenbehafteteAuftragpositionen(inseratDto.getIId()) <= 0) {
			bHatPositionen = false;
			DialogFactory.showModalDialog(LPMain.getInstance()
					.getTextRespectUISPr("lp.warning"), LPMain.getInstance()
					.getTextRespectUISPr("auft.warning.keinepositionen"));
		}

		return bHatPositionen;
	}

	protected boolean handleOwnException(ExceptionLP exfc) {
		boolean bErrorErkannt = true;
		int code = exfc.getICode();

		switch (code) {
		case EJBExceptionLP.FEHLER_LAGER_HAUPTLAGERDESMANDANTEN_NICHT_ANGELEGT:
			DialogFactory.showModalDialog(LPMain.getInstance()
					.getTextRespectUISPr("lp.error"), LPMain.getInstance()
					.getTextRespectUISPr("auft.mandant.hauptlager_fehlt"));
			break;

		default:
			bErrorErkannt = false;
			break;
		}

		return bErrorErkannt;
	}

	/**
	 * Pruefen, ob zu dem aktuellen Auftrag Artikelpositionen erfasst sind.
	 * 
	 * @return boolean true, wenn es Artikelpositionen zu diesem Auftrag gibt
	 * @throws java.lang.Throwable
	 */
	public boolean aktuellerAuftragHatArtikelpositionen() throws Throwable {
		boolean bHatArtikelpositionen = true;

		if (DelegateFactory
				.getInstance()
				.getAuftragpositionDelegate()
				.berechneAnzahlArtikelpositionenMitStatus(inseratDto.getIId(),
						null) <= 0) {
			bHatArtikelpositionen = false;
			DialogFactory
					.showModalDialog(
							LPMain.getInstance().getTextRespectUISPr(
									"lp.warning"),
							LPMain.getInstance().getTextRespectUISPr(
									"auft.warning.keineartikelpositionen"));
		}

		return bHatArtikelpositionen;
	}

	protected void lPActionEvent(ActionEvent e) throws Throwable {
		if (e.getActionCommand().equals(MENUE_BEARBEITEN_BESTELLUNGEN_AUSLOSEN)) {

			ArrayList<Integer> kundeIIds = DelegateFactory.getInstance()
					.getInseratDelegate()
					.getAllKundeIIdsAusInseratenOhneBestellung();

			if (kundeIIds == null || kundeIIds.size() == 0) {
				// Fehler- Keine Inserate ohne Bestellungen vorhanden
				DialogFactory
						.showModalDialog(
								LPMain.getTextRespectUISPr("lp.info"),
								LPMain.getTextRespectUISPr("iv.info.keineinserateohnebestellungvorhanden"));
				return;
			} else {
				String[] aWhichButtonIUse = SystemFilterFactory.getInstance()
						.createButtonArray(false, false);
				FilterKriterium[] fk = InseratFilterFactory.getInstance()
						.createFKLieferantenOhneBestellungen(kundeIIds);
				panelQueryFLRKunde = new PanelQueryFLRGoto(null, fk,
						QueryParameters.UC_ID_KUNDE2, aWhichButtonIUse,
						getInternalFrame(), LocaleFac.BELEGART_KUNDE,
						LPMain.getTextRespectUISPr("title.kundenauswahlliste"),
						null);

				panelQueryFLRKunde
						.befuellePanelFilterkriterienDirektUndVersteckte(
								PartnerFilterFactory
										.getInstance()
										.createFKDKundePartnerName(
												LPMain.getTextRespectUISPr("lp.firma")),
								PartnerFilterFactory.getInstance()
										.createFKDKundePartnerOrt(),
								PartnerFilterFactory.getInstance()
										.createFKVKunde());

				new DialogQuery(panelQueryFLRKunde);
			}
			// -------------

		} else if (e.getActionCommand().equals(
				MENUE_BEARBEITEN_RECHNUNGEN_AUSLOSEN)) {

			if (getDto() != null) {
				JPanel panel = new JPanel();
				panel.setLayout(new GridLayout(3, 1));

				ButtonGroup bg = new ButtonGroup();

				JRadioButton rbAlle = new JRadioButton(
						LPMain.getTextRespectUISPr("iv.rechnungen.ausloesen.alle"));
				rbAlle.setSelected(true);
				bg.add(rbAlle);

				KundeDto kundeDto = DelegateFactory
						.getInstance()
						.getKundeDelegate()
						.kundeFindByPrimaryKey(
								getDto().getInseratrechnungDto().getKundeIId());

				JRadioButton rbKunde = new JRadioButton(
						LPMain.getTextRespectUISPr("iv.rechnungen.ausloesen.kunde")
								+ " " + kundeDto.getPartnerDto().getCKbez());
				bg.add(rbKunde);

				JRadioButton rbInserat = new JRadioButton(
						LPMain.getTextRespectUISPr("iv.rechnungen.ausloesen.inserat")
								+ " " + getDto().getCNr());
				bg.add(rbInserat);

				panel.add(rbAlle);
				panel.add(rbKunde);
				panel.add(rbInserat);
				int i = JOptionPane.showOptionDialog(null, panel, LPMain
						.getTextRespectUISPr("iv.rechnungen.ausloesen.title"),
						JOptionPane.OK_CANCEL_OPTION,
						JOptionPane.QUESTION_MESSAGE, null, null, null);

				if (i != JOptionPane.CANCEL_OPTION) {

					int iAnzahlAngelegt = 0;
					if (rbAlle.isSelected()) {
						iAnzahlAngelegt = DelegateFactory.getInstance()
								.getInseratDelegate()
								.rechnungenAusloesen(null, null, neuDatum);
					} else if (rbInserat.isSelected()) {
						iAnzahlAngelegt = DelegateFactory
								.getInstance()
								.getInseratDelegate()
								.rechnungenAusloesen(null, getDto().getIId(),
										neuDatum);
					} else if (rbKunde.isSelected()) {
						if (getDto().getInseratrechnungDto() != null
								&& getDto().getInseratrechnungDto()
										.getKundeIId() != null) {
							iAnzahlAngelegt = DelegateFactory
									.getInstance()
									.getInseratDelegate()
									.rechnungenAusloesen(
											getDto().getInseratrechnungDto()
													.getKundeIId(), null,
											neuDatum);
						}
					}

					DialogFactory
							.showModalDialog(
									LPMain.getTextRespectUISPr("lp.info"),
									"Es wurden " + iAnzahlAngelegt
											+ " Rechnungen angelegt!");
					inseratAuswahl.eventYouAreSelected(false);
				}
			}
		} else if (e.getActionCommand().equals(MENUE_BEARBEITEN_ER_ERFASSEN)) {

			ArrayList<Integer> liferantIIds = DelegateFactory.getInstance()
					.getInseratDelegate()
					.getAllLieferantIIdsAusOffenenInseraten();

			if (liferantIIds == null || liferantIIds.size() == 0) {
				// Fehler keine Inserate ohne Eingangsrechnung vorhanden
				DialogFactory
						.showModalDialog(
								LPMain.getTextRespectUISPr("lp.info"),
								LPMain.getTextRespectUISPr("iv.info.keineinserateohneervorhanden"));
				return;

			} else {
				String[] aWhichButtonIUse = SystemFilterFactory.getInstance()
						.createButtonArray(false, false);

				FilterKriterium[] fk = InseratFilterFactory.getInstance()
						.createFKAlleLieferantenOffeneInserate(liferantIIds);
				panelQueryFLRLieferantERAnlegen = new PanelQueryFLRGoto(
						null,
						fk,
						QueryParameters.UC_ID_LIEFERANTEN,
						aWhichButtonIUse,
						getInternalFrame(),
						LocaleFac.BELEGART_LIEFERANT,
						LPMain.getTextRespectUISPr("title.lieferantenauswahlliste"),
						null);
				panelQueryFLRLieferantERAnlegen
						.befuellePanelFilterkriterienDirektUndVersteckte(
								PartnerFilterFactory.getInstance()
										.createFKDLieferantPartnerName(),
								PartnerFilterFactory.getInstance()
										.createFKDLieferantPartnerOrt(),
								PartnerFilterFactory.getInstance()
										.createFKVLieferant());

				panelQueryFLRLieferantERAnlegen
						.addDirektFilter(PartnerFilterFactory.getInstance()
								.createFKDPartnerErweiterteSuche());

				new DialogQuery(panelQueryFLRLieferantERAnlegen);
			}

		} else if (e.getActionCommand().equals(
				MENUE_BEARBEITEN_MANUELL_ERLEDIGEN)) {

			if (getDto().getTManuellerledigt() == null) {

				if (getDto().getStatusCNr().equals(LocaleFac.STATUS_ERSCHIENEN)
						|| getDto().getStatusCNr().equals(
								LocaleFac.STATUS_VERRECHENBAR)
						|| getDto().getStatusCNr().equals(
								LocaleFac.STATUS_GESTOPPT)
						|| getDto().getStatusCNr().equals(
								LocaleFac.STATUS_VERRECHNET)) {

					boolean b = DialogFactory
							.showModalJaNeinDialog(
									getInternalFrameInserat(),
									LPMain.getTextRespectUISPr("iv.frage.wirklich.manuellerledigen"));
					if (b == true) {
						DelegateFactory.getInstance().getInseratDelegate()
								.toggleManuellerledigt(getDto().getIId());
					}
				} else {
					DialogFactory.showModalDialog(LPMain
							.getTextRespectUISPr("lp.info"), LPMain
							.getTextRespectUISPr("iv.info.manuellerledigen"));
				}
			} else {
				boolean b = DialogFactory
						.showModalJaNeinDialog(
								getInternalFrameInserat(),
								LPMain.getTextRespectUISPr("iv.frage.wirklich.manuellerledigen.aufheben"));
				if (b == true) {
					DelegateFactory.getInstance().getInseratDelegate()
							.toggleManuellerledigt(getDto().getIId());
				}
			}

			inseratAuswahl.eventYouAreSelected(false);

		}

		else if (e.getActionCommand().equals(MENUE_JOURNAL_ZU_VERRECHNEN)) {
			String add2Title = LPMain.getInstance().getTextRespectUISPr(
					"iv.journal.zuverrechnen");
			getInternalFrame().showReportKriterien(
					new ReportInserateZuverrechen(getInternalFrameInserat(),
							add2Title));
		} else if (e.getActionCommand().equals(MENUE_JOURNAL_ALLE)) {
			String add2Title = LPMain.getInstance().getTextRespectUISPr(
					"iv.journal.alle");
			getInternalFrame()
					.showReportKriterien(
							new ReportAlleInserate(getInternalFrameInserat(),
									add2Title));
		} else if (e.getActionCommand().equals(MENUE_JOURNAL_DBAUSWERTUNG)) {
			String add2Title = LPMain.getInstance().getTextRespectUISPr(
					"iv.journal.dbauswertung");
			getInternalFrame().showReportKriterien(
					new ReportDBAuswertungInserate(getInternalFrameInserat(),
							add2Title));
		} else if (e.getActionCommand().equals(MENUE_JOURNAL_OFFENE)) {
			String add2Title = LPMain.getInstance().getTextRespectUISPr(
					"iv.journal.offene");
			getInternalFrame().showReportKriterien(
					new ReportOffeneInserate(getInternalFrameInserat(),
							add2Title));
		} else if (e.getActionCommand().equals(MENUE_INSERAT_DRUCKEN)) {
			if (getDto() != null) {
				String add2Title = LPMain.getInstance().getTextRespectUISPr(
						"iv.inserat.modulname");
				getInternalFrame().showReportKriterien(
						new ReportInserat(getInternalFrameInserat(), getDto()
								.getIId(), add2Title));
			}
		} else if (e.getActionCommand().equals(MENUE_ACTION_NEU_DATUM)) {
			getInternalFrame().showPanelDialog(
					new PanelDialogNeuDatum(getInternalFrame(), LPMain
							.getTextRespectUISPr("iv.menu.extras.neudatum"),
							neuDatum));
		} else if (e.getActionCommand().equals(MENUE_ACTION_VERTRETER_AENDERN)) {
			panelQueryFLRPersonal = PersonalFilterFactory.getInstance()
					.createPanelFLRPersonal(getInternalFrame(), true, false,
							getDto().getPersonalIIdVertreter());
			new DialogQuery(panelQueryFLRPersonal);
		}

	}

	/**
	 * reloadpanel: 0 Diese Methode dient zur Aktualisierung der Daten im fuer
	 * den Benutzer aktuell sichtbaren Panel. <br>
	 * Beim Aufruf muss das aktuelle Panel initialisiert sein.
	 * 
	 * @throws Throwable
	 *             Ausnahme
	 */
	public void aktuellesPanelAktuslisieren() throws Throwable {
		initializeDtos(inseratDto.getIId());

		int selectedIndex = getSelectedIndex();

		switch (selectedIndex) {
		case IDX_PANEL_INSERATAUSWAHL:
			inseratAuswahl.eventYouAreSelected(false);
			break;

		case IDX_PANEL_INSERATKOPFDATEN:
			inseratKopfdaten.eventYouAreSelected(false);
			break;

		}

	}

	/**
	 * Diese Methode setzt des aktuellen Auftrag aus der Auswahlliste als den zu
	 * lockenden Auftrag.
	 * 
	 * @throws java.lang.Throwable
	 *             Ausnahme
	 */
	public void setKeyWasForLockMe() throws Throwable {
		Object oKey = inseratAuswahl.getSelectedId();

		if (oKey != null) {
			initializeDtos((Integer) oKey);

			getInternalFrame().setKeyWasForLockMe(oKey.toString());
		} else {
			getInternalFrame().setKeyWasForLockMe(null);
		}
	}

	protected javax.swing.JMenuBar getJMenuBar() throws Throwable {

		WrapperMenuBar wrapperMenuBar = new WrapperMenuBar(this);

		// Menue Datei; ein neuer Eintrag wird immer oben im Menue eingetragen
		JMenu jmModul = (JMenu) wrapperMenuBar
				.getComponent(WrapperMenuBar.MENU_MODUL);
		jmModul.add(new JSeparator(), 0);
		JMenuItem menuItemInseratDrucken = new JMenuItem(LPMain.getInstance()
				.getTextRespectUISPr("lp.menu.drucken"));
		menuItemInseratDrucken.addActionListener(this);
		menuItemInseratDrucken.setActionCommand(MENUE_INSERAT_DRUCKEN);
		jmModul.add(menuItemInseratDrucken, 0);

		// Menue Bearbeiten

		JMenu jmBearbeiten = (JMenu) wrapperMenuBar
				.getComponent(WrapperMenuBar.MENU_BEARBEITEN);
		JMenuItem menuItemBestellungenAusloesen = new JMenuItem(LPMain
				.getInstance().getTextRespectUISPr(
						"iv.inserat.bestellungenausloesen"));
		menuItemBestellungenAusloesen.addActionListener(this);
		menuItemBestellungenAusloesen
				.setActionCommand(MENUE_BEARBEITEN_BESTELLUNGEN_AUSLOSEN);
		jmBearbeiten.add(menuItemBestellungenAusloesen);
		/*
		 * JMenuItem menuItemErErfassen = new JMenuItem(LPMain.getInstance()
		 * .getTextRespectUISPr("iv.inserat.ererfassen"));
		 * menuItemErErfassen.addActionListener(this);
		 * menuItemErErfassen.setActionCommand(MENUE_BEARBEITEN_ER_ERFASSEN);
		 * jmBearbeiten.add(menuItemErErfassen);
		 */

		JMenuItem menuItemRechnungenAusloesen = new JMenuItem(LPMain
				.getInstance().getTextRespectUISPr(
						"iv.inserat.rechnungenausloesen"));
		menuItemRechnungenAusloesen.addActionListener(this);
		menuItemRechnungenAusloesen
				.setActionCommand(MENUE_BEARBEITEN_RECHNUNGEN_AUSLOSEN);
		jmBearbeiten.add(menuItemRechnungenAusloesen);

		JMenuItem menuItemManuellErledigen = new JMenuItem(LPMain.getInstance()
				.getTextRespectUISPr("iv.menu.bearbeiten.manuellerledigen"));
		menuItemManuellErledigen.addActionListener(this);
		menuItemManuellErledigen
				.setActionCommand(MENUE_BEARBEITEN_MANUELL_ERLEDIGEN);
		jmBearbeiten.add(menuItemManuellErledigen);

		WrapperMenuItem menuItemVertreterAendern = new WrapperMenuItem(LPMain
				.getInstance().getTextRespectUISPr(
						"iv.inserat.vertreter.aendern"),
				RechteFac.RECHT_FB_CHEFBUCHHALTER);
		menuItemVertreterAendern.addActionListener(this);
		menuItemVertreterAendern
				.setActionCommand(MENUE_ACTION_VERTRETER_AENDERN);
		jmBearbeiten.add(menuItemVertreterAendern);

		// Menue Journal
		JMenu jmJournal = (JMenu) wrapperMenuBar
				.getComponent(WrapperMenuBar.MENU_JOURNAL);
		JMenuItem menuItemZuverrechnen = new JMenuItem(LPMain.getInstance()
				.getTextRespectUISPr("iv.journal.zuverrechnen"));
		menuItemZuverrechnen.addActionListener(this);
		menuItemZuverrechnen.setActionCommand(MENUE_JOURNAL_ZU_VERRECHNEN);
		jmJournal.add(menuItemZuverrechnen);

		WrapperMenuItem menuItemAlle = new WrapperMenuItem(LPMain.getInstance()
				.getTextRespectUISPr("iv.journal.alle"), null);
		menuItemAlle.addActionListener(this);
		menuItemAlle.setActionCommand(MENUE_JOURNAL_ALLE);
		jmJournal.add(menuItemAlle);

		WrapperMenuItem menuItemDBAuswertung = new WrapperMenuItem(LPMain
				.getInstance().getTextRespectUISPr("iv.journal.dbauswertung"),
				RechteFac.RECHT_LP_FINANCIAL_INFO_TYP_1);
		menuItemDBAuswertung.addActionListener(this);
		menuItemDBAuswertung.setActionCommand(MENUE_JOURNAL_DBAUSWERTUNG);
		jmJournal.add(menuItemDBAuswertung);

		JMenuItem menuItemOffene = new JMenuItem(LPMain.getInstance()
				.getTextRespectUISPr("iv.journal.offene"));
		menuItemOffene.addActionListener(this);
		menuItemOffene.setActionCommand(MENUE_JOURNAL_OFFENE);
		jmJournal.add(menuItemOffene);

		// Extras
		JMenu extras = new WrapperMenu("iv.menu.extras", this);
		WrapperMenuItem menuItemNeuDatum = new WrapperMenuItem(
				LPMain.getTextRespectUISPr("iv.menu.extras.neudatum"),
				RechteFac.RECHT_IV_INSERAT_CUD);
		menuItemNeuDatum.addActionListener(this);
		menuItemNeuDatum.setActionCommand(MENUE_ACTION_NEU_DATUM);
		extras.add(menuItemNeuDatum);

		wrapperMenuBar.add(extras);

		return wrapperMenuBar;
	}

	/**
	 * 
	 * @return com.lp.client.frame.component.PanelQuery
	 */
	public PanelQuery getPanelAuswahl() {
		this.setSelectedIndex(IDX_PANEL_INSERATAUSWAHL);
		this.getInternalFrame().enableAllPanelsExcept(false);
		return inseratAuswahl;
	}

	/**
	 * 
	 * @return com.lp.client.frame.component.PanelBasis
	 */
	public PanelBasis getPanelKopfdaten() {
		this.setSelectedIndex(IDX_PANEL_INSERATKOPFDATEN);
		this.getInternalFrame().enableAllPanelsExcept(false);
		return inseratKopfdaten;
	}

}
