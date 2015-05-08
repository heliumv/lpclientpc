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
package com.lp.client.frame.component;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.KeyEvent;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.EventObject;
import java.util.Map;

import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.lp.client.anfrage.InternalFrameAnfrage;
import com.lp.client.angebotstkl.InternalFrameAngebotstkl;
import com.lp.client.artikel.ArtikelFilterFactory;
import com.lp.client.auftrag.PanelAuftragPositionen2;
import com.lp.client.bestellung.InternalFrameBestellung;
import com.lp.client.frame.Defaults;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.artikel.fastlanereader.generated.service.WwArtikellagerPK;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikelFac;
import com.lp.server.artikel.service.ArtikellieferantDto;
import com.lp.server.artikel.service.LagerDto;
import com.lp.server.stueckliste.service.StuecklisteDto;
import com.lp.server.stueckliste.service.StuecklisteFac;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MandantFac;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.system.service.SystemFac;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

/**
 * <p>
 * Hier werden Zeilen fuer Panels mit Artikelpositionen zur Verfuegung gestellt.
 * </p>
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2008
 * </p>
 * <p>
 * Erstellungsdatum 2005-02-11
 * </p>
 * <p>
 * </p>
 * 
 * @author Uli Walch
 * @version $Revision: 1.42 $
 */
public abstract class PanelPositionenPreiseingabe extends PanelBasis implements
		FocusListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// in Unterklassen muss entschieden werden, ob darfPreiseSehen Einkauf oder
	// Verkaufsrecht gilt
	public final boolean bRechtDarfPreiseSehen;
	public final boolean bRechtDarfPreiseAendern;
	public boolean bVkpreiseingabeNurNetto = false;

	// optionaler Block fuer die Artikelauswahl und Anzeige
	public WrapperIdentField wifArtikelauswahl = null;
	public WrapperGotoButton wbuArtikelauswahl = null;
	protected WrapperTextField wtfArtikel = null;

	private WrapperLabel wlaBezeichnung = null;
	public WrapperTextField wtfBezeichnung = null;
	public WrapperTextField wtfZusatzbezeichnung = null;

	// optionale Zeile mit Formatierung in acht Spalten
	public WrapperLabel wlaMenge = null;
	public WrapperNumberField wnfMenge = null;
	private WrapperLabel wlaEinheit = null;
	public WrapperComboBox wcoEinheit = null;
	public WrapperLabel wlaEinzelpreis = null;
	public WrapperNumberField wnfEinzelpreis = null;
	private WrapperLabel wlaZielwaehrung0 = null;
	private WrapperLabel wlaEmpty = null;

	public WrapperComboBox wcoVerleih = null;

	// optionale Zeile ohne Formatierung mit acht Spalten
	private WrapperLabel wlaRabattsatz = null;
	public WrapperNumberField wnfRabattsatz = null;
	public WrapperLabel wlaProzent1 = null;
	public WrapperLabel wlaProzentAufschlag = null;
	protected WrapperLabel wlaMinus = null;

	// public WrapperNumberField wnfRabattsumme = null;
	public WrapperFixableNumberField wnfRabattsumme = null;
	private WrapperLabel wlaZielwaehrung1 = null;

	// optionale Zeile ohne Formatierung mit acht Spalten
	public WrapperLabel wlaZusatzrabattsatz = null;
	public WrapperNumberField wnfZusatzrabattsatz = null;
	public WrapperLabel wlaProzentZusatzrabattsatz = null;
	public WrapperLabel wlaMinusZusatzrabattsumme = null;
	public WrapperNumberField wnfZusatzrabattsumme = null;
	private WrapperLabel wlaZielwaehrungZusatzrabattsumme = null;

	// Materialzuschlag optional
	public WrapperLabel wlaMaterialzuschlag = null;
	public WrapperNumberField wnfMaterialzuschlag = null;
	private WrapperLabel wlaZielwaehrungMaterialzuschlag = null;
	public WrapperLabel wlaPlusMaterialzuschlag = null;

	private WrapperLabel wlaAufschlag = null;
	public WrapperNumberField wnfAufschlagProzent = null;
	public WrapperFixableNumberField wnfAufschlagBetrag = null;
	public WrapperFixableNumberField wnfGesamtpreisMitAufschlag = null;
	protected WrapperLabel wlaPlusAufschlag = null;
	private WrapperLabel wlaWaehrungAufschlag1 = null;
	private WrapperLabel wlaWaehrungAufschlag2 = null;

	// optional Zeile ohne Formatierung mit acht Spalten
	public WrapperLabel wlaNettopreis = null;
	// public WrapperNumberField wnfNettopreis = null;
	public WrapperFixableNumberField wnfNettopreis = null;
	private WrapperLabel wlaZielwaehrung2 = null;

	public WrapperLabel wlaGestpreis = null;
	public WrapperNumberField wnfGestpreis = null;

	// optionale Zeile ohne Formatierung mit acht Spalten
	private Map<?, ?> tmpMwst = null;
	public Timestamp tBelegdatumMwstsatz;
	public WrapperLabel wlaMwstsatz = null;
	public WrapperComboBox wcoMwstsatz = null;
	public WrapperLabel wlaProzent2 = null;
	public WrapperLabel wlaPlus = null;
	public WrapperNumberField wnfMwstsumme = null;
	private WrapperLabel wlaZielwaehrung3 = null;

	// optionale Zeile ohne Formatierung mit acht Spalten
	public WrapperLabel wlaBruttopreis = null;
	public WrapperNumberField wnfBruttopreis = null;
	private WrapperLabel wlaZielwaehrungBruttopreis = null;

	// optionale Zeile ohne Formatierung mit acht Spalten
	public WrapperLabel wlaLieferterminPosition = null;
	public WrapperDateField wdfLieferterminPosition = null;

	public WrapperSelectField wsfKostentraeger = new WrapperSelectField(
			WrapperSelectField.KOSTENTRAEGER, getInternalFrame(), false);

	public WrapperSelectField wsfLieferant = new WrapperSelectField(
			WrapperSelectField.LIEFERANT, getInternalFrame(), true);

	public WrapperLabel wlaLVPosition = new WrapperLabel();
	public WrapperTextField wtfLVPosition = new WrapperTextField();

	public WrapperButton wbuLager = new WrapperButton(
			LPMain.getTextRespectUISPr("button.lager"));
	public WrapperTextField wtfLager = new WrapperTextField();
	public PanelQueryFLR panelQueryFLRArtikellager = null;
	static final public String ACTION_SPECIAL_LAGER_FROM_LISTE = "action_lager_from_liste";
	public Integer selectedlagerIId = null;

	public void setUebersteuertesLagerIId(Integer selectedlagerIId)
			throws Throwable {
		this.selectedlagerIId = selectedlagerIId;
		if (selectedlagerIId != null) {
			LagerDto lagerDto = DelegateFactory.getInstance()
					.getLagerDelegate().lagerFindByPrimaryKey(selectedlagerIId);
			wtfLager.setText(lagerDto.getCNr());
			this.selectedlagerIId = lagerDto.getIId();
		} else {
			wtfLager.setText(null);
		}

	}

	public ButtonGroup buttonGroupFixable = null;
	public ButtonGroup buttonGroupFixableAufschlag = null;

	public int iIdMwstsatz = -1;

	public int iZeileNettopreis = -1;

	private int iPreiseUINachkommastellen;
	private final int iMengeUINachkommastellen;

	protected final int iSpaltenbreite1;

	/** Den gewaehlten Artikel hinterlegen. */
	private ArtikelDto artikelDto = null;
	// MB letzteArtikelNr:
	private String letzteArtikelNr = null;

	private final String sLockMeWer;

	/**
	 * Ad Vorbelegung Mwstsatz: Es gibt einen Mandantenparameter
	 * ParameterFac.PARAMETER_KUNDEN_POSITIONSKONTIERUNG vom Typ boolean. 'true'
	 * = Der Vorschlagswert fuer alle Positionen kommt aus dem jeweiligen
	 * Artikel. 'false' = Der Vorschlagswert fuer alle Positionen kommt aus dem
	 * Kunden des Belegs.
	 */
	private boolean bDefaultMwstsatzAusArtikel = true;

	/**
	 * Konstruktor.
	 * 
	 * @param internalFrame
	 *            der InternalFrame auf dem das Panel sitzt
	 * @param add2TitleI
	 *            der default Titel des Panels
	 * @param key
	 *            PK der Position
	 * @param sLockMeWer
	 *            String
	 * @param bDarfPreiseSehenI
	 *            boolean
	 * @param iSpaltenbreite1I
	 *            die Breite der ersten Spalte
	 * @throws Throwable
	 */
	public PanelPositionenPreiseingabe(InternalFrame internalFrame,
			String add2TitleI, Object key, String sLockMeWer,
			boolean bDarfPreiseSehenI, boolean bDarfPreiseAendernI,
			int iSpaltenbreite1I) throws Throwable {
		super(internalFrame, add2TitleI, key);

		this.iSpaltenbreite1 = iSpaltenbreite1I;
		this.sLockMeWer = sLockMeWer;
		this.bRechtDarfPreiseSehen = bDarfPreiseSehenI;
		this.bRechtDarfPreiseAendern = bDarfPreiseAendernI;

		// Mandantenparameter fuer Positionskontierung bestimmen
		ParametermandantDto parameterPositionskontierung = DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getMandantparameter(LPMain.getTheClient().getMandant(),
						ParameterFac.KATEGORIE_KUNDEN,
						ParameterFac.PARAMETER_KUNDEN_POSITIONSKONTIERUNG);
		bDefaultMwstsatzAusArtikel = (Boolean) parameterPositionskontierung
				.getCWertAsObject();

		parameterPositionskontierung = DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getMandantparameter(LPMain.getTheClient().getMandant(),
						ParameterFac.KATEGORIE_ALLGEMEIN,
						ParameterFac.PARAMETER_VKPREISEINGABE_NUR_NETTO);
		bVkpreiseingabeNurNetto = (Boolean) parameterPositionskontierung
				.getCWertAsObject();

		// Mandantenparameter fuer Preis Nachkommastellen
		iPreiseUINachkommastellen = Defaults.getInstance()
				.getIUINachkommastellenPreiseAllgemein();

		if (this instanceof PanelPositionenArtikelVerkauf) {
			iPreiseUINachkommastellen = Defaults.getInstance()
					.getIUINachkommastellenPreiseVK();
		} else if (this instanceof PanelPositionenArtikelEinkauf) {
			iPreiseUINachkommastellen = Defaults.getInstance()
					.getIUINachkommastellenPreiseEK();
		} else if (this instanceof PanelPositionenHandeingabe) {
			if (internalFrame instanceof InternalFrameBestellung
					|| internalFrame instanceof InternalFrameAnfrage) {
				iPreiseUINachkommastellen = Defaults.getInstance()
						.getIUINachkommastellenPreiseEK();
			} else {
				iPreiseUINachkommastellen = Defaults.getInstance()
						.getIUINachkommastellenPreiseVK();
			}
		} else if (this instanceof PanelPositionenBestellvorschlag) {
			iPreiseUINachkommastellen = Defaults.getInstance()
					.getIUINachkommastellenPreiseEK();
		}

		// Mandantenparameter fuer Mengen Nachkommastellen
		iMengeUINachkommastellen = Defaults.getInstance()
				.getIUINachkommastellenMenge();
		buttonGroupFixable = new ButtonGroup();
	}

	void dialogQueryLagerFromListe(ActionEvent e) throws Throwable {

		boolean bSetartikel = false;
		StuecklisteDto stklDto = DelegateFactory
				.getInstance()
				.getStuecklisteDelegate()
				.stuecklisteFindByMandantCNrArtikelIIdOhneExc(
						artikelDto.getIId());
		if (stklDto != null
				&& stklDto.getStuecklisteartCNr().equals(
						StuecklisteFac.STUECKLISTEART_SETARTIKEL)) {
			bSetartikel = true;
		}

		if (artikelDto != null && artikelDto.getIId() != null
				&& bSetartikel == false) {

			panelQueryFLRArtikellager = new PanelQueryFLR(null,
					ArtikelFilterFactory.getInstance().createFKArtikellager(
							artikelDto.getIId()),
					QueryParameters.UC_ID_ARTIKELLAGER, null,
					getInternalFrame(),
					LPMain.getTextRespectUISPr("label.lager"));
			panelQueryFLRArtikellager
					.befuellePanelFilterkriterienDirektUndVersteckte(null,
							null, ArtikelFilterFactory.getInstance()
									.createFKVLager());
			if (artikelDto.getIId() != null && selectedlagerIId != null) {
				panelQueryFLRArtikellager.setSelectedId(new WwArtikellagerPK(
						artikelDto.getIId(), selectedlagerIId));
			}
			new DialogQuery(panelQueryFLRArtikellager);

		} else {
			// Wenn noch kein Artikel Ausgewaehlt ist, dann normale Lagerliste
			// anzeigen
			panelQueryFLRArtikellager = ArtikelFilterFactory.getInstance()
					.createPanelFLRLager(getInternalFrame(), selectedlagerIId);

			new DialogQuery(panelQueryFLRArtikellager);
		}
	}

	public void addArtikelblock(JPanel oPanelI, int iZeileI) throws Throwable {
		// wegen Dialogauswahl auf FLR events hoeren
		getInternalFrame().addItemChangedListener(this);

		wifArtikelauswahl = new WrapperIdentField(getInternalFrame(), this);
		wbuArtikelauswahl = wifArtikelauswahl.getWbuArtikel();
		wtfArtikel = wifArtikelauswahl.getWtfIdent();
		wlaBezeichnung = wifArtikelauswahl.getWlaBezeichnumg();
		wtfBezeichnung = wifArtikelauswahl.getWtfBezeichnung();
		wtfBezeichnung.setActivatable(true);
		wtfZusatzbezeichnung = wifArtikelauswahl.getWtfZusatzBezeichnung();
		wtfZusatzbezeichnung.setActivatable(true);

		wbuLager.addActionListener(this);
		wbuLager.setActionCommand(ACTION_SPECIAL_LAGER_FROM_LISTE);
		wtfLager.setActivatable(false);
		// Zeile 1 des Artikelsblocks

		// Im VK-Panel
		if (this instanceof PanelPositionenArtikelVerkauf) {

			oPanelI.add(wifArtikelauswahl.getKundenidentnummerButton(),
					new GridBagConstraints(0, iZeileI, 1, 1, 0, 0.0,
							GridBagConstraints.WEST, GridBagConstraints.NONE,
							new Insets(1, 2, 1, 2), 10, 0));
			oPanelI.add(wbuArtikelauswahl, new GridBagConstraints(0, iZeileI,
					1, 1, 0.0, 0.0, GridBagConstraints.NORTH,
					GridBagConstraints.HORIZONTAL, new Insets(1, 22, 1, 2), 0,
					0));
		} else {
			oPanelI.add(wbuArtikelauswahl,
					new GridBagConstraints(0, iZeileI, 1, 1, 0.0, 0.0,
							GridBagConstraints.NORTH,
							GridBagConstraints.HORIZONTAL, new Insets(1, 2, 1,
									2), 0, 0));
		}

		oPanelI.add(wtfArtikel, new GridBagConstraints(1, iZeileI, 1, 1, 0.0,
				0.0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
				new Insets(1, 2, 1, 2), 0, 0));

		oPanelI.add(wlaBezeichnung, new GridBagConstraints(2, iZeileI, 1, 1,
				0.0, 0.0, GridBagConstraints.NORTH,
				GridBagConstraints.HORIZONTAL, new Insets(1, 2, 1, 2), 0, 0));
		oPanelI.add(wtfBezeichnung, new GridBagConstraints(3, iZeileI, 5, 1,
				0.0, 0.0, GridBagConstraints.NORTH,
				GridBagConstraints.HORIZONTAL, new Insets(1, 2, 1, 2), 0, 0));

		// Zeile 2 des Artikelblocks
		iZeileI++;

		ParametermandantDto parametermandantDto = DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getMandantparameter(LPMain.getTheClient().getMandant(),
						ParameterFac.KATEGORIE_ALLGEMEIN,
						ParameterFac.PARAMETER_LV_POSITION);

		boolean bMitLVPositionen = ((Boolean) parametermandantDto
				.getCWertAsObject()).booleanValue();

		if (bMitLVPositionen == true) {
			wlaLVPosition.setText(LPMain.getTextRespectUISPr("lp.lvposition"));
			oPanelI.add(wlaLVPosition,
					new GridBagConstraints(0, iZeileI, 1, 1, 0.0, 0.0,
							GridBagConstraints.NORTH,
							GridBagConstraints.HORIZONTAL, new Insets(1, 2, 1,
									2), 0, 0));
			oPanelI.add(wtfLVPosition,
					new GridBagConstraints(1, iZeileI, 1, 1, 0.0, 0.0,
							GridBagConstraints.NORTH,
							GridBagConstraints.HORIZONTAL, new Insets(1, 2, 1,
									2), 0, 0));
		} else {

			if (getInternalFrame().getBelegartCNr().equals(
					LocaleFac.BELEGART_LIEFERSCHEIN)) {

				oPanelI.add(wbuLager, new GridBagConstraints(0, iZeileI, 1, 1,
						0.0, 0.0, GridBagConstraints.NORTH,
						GridBagConstraints.HORIZONTAL, new Insets(1, 2, 1, 2),
						0, 0));
				oPanelI.add(wtfLager, new GridBagConstraints(1, iZeileI, 1, 1,
						0.0, 0.0, GridBagConstraints.NORTH,
						GridBagConstraints.HORIZONTAL, new Insets(1, 2, 1, 2),
						0, 0));
			}
		}

		oPanelI.add(wtfZusatzbezeichnung, new GridBagConstraints(3, iZeileI, 5,
				1, 0.0, 0.0, GridBagConstraints.NORTH,
				GridBagConstraints.HORIZONTAL, new Insets(1, 2, 1, 2), 0, 0));
	}

	/**
	 * Diese Zeile formatiert ein Panel in drei Fluchten und bietet die Basis
	 * fuer ein Panel mit einer Preisfindung. <br>
	 * Die folgenden Felder werden in dieser Zeile angezeigt: <br>
	 * Label Menge - Menge - Label Einheit - Einheit - leer - Label Einzelpreis
	 * - Einzelpreis - Waehrung
	 * 
	 * @param oPanelI
	 *            JPanel
	 * @param iZeileI
	 *            int
	 * @throws java.lang.Throwable
	 *             Ausnahme
	 */
	public void addFormatierungszeileNettoeinzelpreis(JPanel oPanelI,
			int iZeileI) throws Throwable {
		wlaMenge = new WrapperLabel(LPMain.getTextRespectUISPr("label.menge"));
		HelperClient.setDefaultsToComponent(wlaMenge, iSpaltenbreite1);

		wnfMenge = new WrapperNumberField();
		wnfMenge.setFractionDigits(iMengeUINachkommastellen);
		HelperClient.setDefaultsToComponent(wnfMenge, 90);
		wnfMenge.addFocusListener(new PanelPositionenPreiseingabe_wnfMenge_focusAdapter(
				this));
		wnfMenge.addKeyListener(new PanelPositionenPreiseingabe_wnfMenge_keyAdapter(
				this));

		wlaEinheit = new WrapperLabel(
				LPMain.getTextRespectUISPr("label.einheit"));
		HelperClient.setDefaultsToComponent(wlaEinheit, 40);

		wcoEinheit = new WrapperComboBox();
		HelperClient.setDefaultsToComponent(wcoEinheit, 90);
		wcoEinheit.setMandatoryFieldDB(true);
		wcoEinheit.setMap(DelegateFactory.getInstance().getSystemDelegate()
				.getAllEinheiten());

		wlaEmpty = new WrapperLabel();
		HelperClient.setDefaultsToComponent(wlaEmpty, 12);

		wlaEinzelpreis = new WrapperLabel(
				LPMain.getTextRespectUISPr("label.einzelpreis"));
		HelperClient.setDefaultsToComponent(wlaEinzelpreis, 50);

		wnfEinzelpreis = new WrapperNumberField();
		wnfEinzelpreis.setDependenceField(true);
		wnfEinzelpreis.setMandatoryField(true);
		wnfEinzelpreis
				.addFocusListener(new PanelPositionenPreiseingabe_wnfEinzelpreis_focusAdapter(
						this));

		wlaZielwaehrung0 = new WrapperLabel();
		HelperClient.setDefaultsToComponent(wlaZielwaehrung0, 20);
		wlaZielwaehrung0.setHorizontalAlignment(SwingConstants.LEADING);

		// Sichtbarkeit
		if (!bRechtDarfPreiseSehen) {
			wlaEinzelpreis.setVisible(false);
			wnfEinzelpreis.setVisible(false);
			wlaZielwaehrung0.setVisible(false);
		}

		oPanelI.add(wlaMenge, new GridBagConstraints(0, iZeileI, 1, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(1, 2, 1, 2), 0, 0));

		if (LPMain
				.getInstance()
				.getDesktop()
				.darfAnwenderAufZusatzfunktionZugreifen(
						MandantFac.ZUSATZFUNKTION_VERLEIH)
				&& this instanceof PanelPositionenArtikelVerkauf
				&& !(getInternalFrame() instanceof InternalFrameAngebotstkl)) {

			wcoVerleih = new WrapperComboBox();
			wcoVerleih.setMinimumSize(new Dimension(20, Defaults.getInstance()
					.getControlHeight()));
			wcoVerleih.setPreferredSize(new Dimension(20, Defaults
					.getInstance().getControlHeight()));

			wcoVerleih.setMap(DelegateFactory.getInstance()
					.getArtikelDelegate().getAllVerleih());
			oPanelI.add(wcoVerleih, new GridBagConstraints(1, iZeileI, 1, 1,
					0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
					new Insets(1, 2, 1, 2), 50, 0));
			oPanelI.add(wnfMenge, new GridBagConstraints(1, iZeileI, 1, 1, 0.0,
					0.0, GridBagConstraints.EAST,
					GridBagConstraints.HORIZONTAL, new Insets(1, 75, 1, 2), 0,
					0));
		} else {
			oPanelI.add(wnfMenge,
					new GridBagConstraints(1, iZeileI, 1, 1, 0.0, 0.0,
							GridBagConstraints.CENTER,
							GridBagConstraints.HORIZONTAL, new Insets(1, 2, 1,
									2), 0, 0));
		}

		oPanelI.add(wlaEinheit, new GridBagConstraints(2, iZeileI, 1, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(1, 2, 1, 2), 0, 0));
		oPanelI.add(wcoEinheit, new GridBagConstraints(3, iZeileI, 1, 1, 0.1,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(1, 2, 1, 2), 0, 0));
		oPanelI.add(wlaEmpty, new GridBagConstraints(4, iZeileI, 1, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(1, 2, 1, 2), 0, 0));
		oPanelI.add(wlaEinzelpreis, new GridBagConstraints(5, iZeileI, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(1, 2, 1, 2), 0, 0));
		oPanelI.add(wnfEinzelpreis, new GridBagConstraints(6, iZeileI, 1, 1,
				0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(1, 2, 1, 2), 0, 0));
		oPanelI.add(wlaZielwaehrung0, new GridBagConstraints(7, iZeileI, 1, 1,
				0.1, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(1, 2, 1, 2), 0, 0));
	}

	/**
	 * Diese Zeile enthaelt keine Formatierung und bietet die Basis fuer ein
	 * Panel mit einer Preisfindung, das ueber
	 * addFormatierungszeileNettoeinzelpreis() formtiert wurde. <br>
	 * Die folgenden Felder werden in dieser Zeile angezeigt: <br>
	 * leer - leer - leer - Rabattsatz - Label Prozent - Label Minus -
	 * Rabattsumme - Waehrung
	 * 
	 * @param oPanelI
	 *            JPanel
	 * @param iZeileI
	 *            int
	 * @throws java.lang.Throwable
	 *             Ausnahme
	 */
	public void addZeileRabattsumme(JPanel oPanelI, int iZeileI)
			throws Throwable {
		wlaRabattsatz = new WrapperLabel(
				LPMain.getTextRespectUISPr("label.rabatt"));
		wnfRabattsatz = new WrapperNumberField(new BigDecimal(
				SystemFac.MIN_N_NUMBER), new BigDecimal(SystemFac.MAX_N_NUMBER));
		wnfRabattsatz.setDependenceField(true);
		wnfRabattsatz.setMandatoryField(true);
		wnfRabattsatz
				.addFocusListener(new PanelPositionenPreiseingabe_wnfRabattsatz_focusAdapter(
						this));

		wlaProzent1 = new WrapperLabel(
				LPMain.getTextRespectUISPr("label.prozent"));
		wlaProzent1.setHorizontalAlignment(SwingConstants.LEFT);
		wlaProzent1.setHorizontalTextPosition(SwingConstants.LEFT);
		wlaProzent1.setMaximumSize(new Dimension(12, Defaults.getInstance()
				.getControlHeight()));
		wlaProzent1.setMinimumSize(new Dimension(12, Defaults.getInstance()
				.getControlHeight()));
		wlaProzent1.setPreferredSize(new Dimension(12, Defaults.getInstance()
				.getControlHeight()));

		wlaMinus = new WrapperLabel(LPMain.getTextRespectUISPr("lp.minus"));

		wnfRabattsumme = new WrapperFixableNumberField();
		wnfRabattsumme.setDependenceField(true);

		buttonGroupFixable.add(wnfRabattsumme.getWrbFixNumber());

		wlaZielwaehrung1 = new WrapperLabel();
		wlaZielwaehrung1.setHorizontalAlignment(SwingConstants.LEADING);

		// Sichtbarkeit
		if (!bRechtDarfPreiseSehen) {
			wlaRabattsatz.setVisible(false);
			wnfRabattsatz.setVisible(false);
			wlaProzent1.setVisible(false);
			wlaMinus.setVisible(false);
			wnfRabattsumme.setVisible(false);
			wlaZielwaehrung1.setVisible(false);
		}

		oPanelI.add(wlaRabattsatz, new GridBagConstraints(2, iZeileI, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(1, 2, 1, 2), 0, 0));
		oPanelI.add(wnfRabattsatz, new GridBagConstraints(3, iZeileI, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(1, 2, 1, 2), 0, 0));
		oPanelI.add(wlaProzent1, new GridBagConstraints(4, iZeileI, 1, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(1, 2, 1, 2), 0, 0));
		oPanelI.add(wlaMinus, new GridBagConstraints(5, iZeileI, 1, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(1, 2, 1, 2), 0, 0));
		oPanelI.add(wnfRabattsumme, new GridBagConstraints(6, iZeileI, 1, 1,
				0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
				new Insets(1, 2, 1, 2), 0, 0));
		oPanelI.add(wlaZielwaehrung1, new GridBagConstraints(7, iZeileI, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(1, 2, 1, 2), 0, 0));
	}

	public void addZeileAufschlag(JPanel oPanelI, int iZeileI) throws Throwable {

		wlaAufschlag = new WrapperLabel(
				LPMain.getTextRespectUISPr("as.aufschlag"));

		wnfAufschlagProzent = new WrapperNumberField();
		wnfAufschlagProzent.setMandatoryField(true);

		wnfAufschlagProzent
				.addFocusListener(new PanelPositionenPreiseingabe_wnfAufschlag_focusAdapter(
						this));

		buttonGroupFixableAufschlag = new ButtonGroup();

		wnfAufschlagBetrag = new WrapperFixableNumberField();

		wnfAufschlagBetrag.setFractionDigits(iPreiseUINachkommastellen);
		wnfAufschlagBetrag.setActivatable(false);
		wnfAufschlagBetrag.setDependenceField(true);
		wnfAufschlagBetrag.setMandatoryField(true);

		wnfGesamtpreisMitAufschlag = new WrapperFixableNumberField();
		wnfGesamtpreisMitAufschlag.setMandatoryField(true);
		wnfGesamtpreisMitAufschlag.setDependenceField(true);
		wnfGesamtpreisMitAufschlag
				.getWrapperWrapperNumberField()
				.addFocusListener(
						new PanelPositionenPreiseingabe_wnfGesamtpreisMitAufschlag_focusAdapter(
								this));

		buttonGroupFixableAufschlag.add(wnfAufschlagBetrag.getWrbFixNumber());
		buttonGroupFixableAufschlag.add(wnfGesamtpreisMitAufschlag
				.getWrbFixNumber());

		wlaProzentAufschlag = new WrapperLabel(
				LPMain.getTextRespectUISPr("label.prozent"));
		wlaPlusAufschlag = new WrapperLabel(
				LPMain.getTextRespectUISPr("lp.plus"));

		wlaWaehrungAufschlag1 = new WrapperLabel();
		wlaWaehrungAufschlag2 = new WrapperLabel();

		oPanelI.add(wlaAufschlag, new GridBagConstraints(1, iZeileI, 2, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(1, 2, 1, 2), 0, 0));
		oPanelI.add(wnfAufschlagProzent, new GridBagConstraints(3, iZeileI, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(1, 2, 1, 2), 0, 0));
		oPanelI.add(wlaProzentAufschlag, new GridBagConstraints(4, iZeileI, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(1, 2, 1, 2), 0, 0));
		oPanelI.add(wlaPlusAufschlag, new GridBagConstraints(5, iZeileI, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(1, 2, 1, 2), 0, 0));
		oPanelI.add(wnfAufschlagBetrag, new GridBagConstraints(6, iZeileI, 1,
				1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
				new Insets(1, 2, 1, 2), 0, 0));
		oPanelI.add(wlaWaehrungAufschlag1, new GridBagConstraints(7, iZeileI,
				1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(1, 2, 1, 2), 0, 0));

		iZeileI++;

		oPanelI.add(
				new WrapperLabel(LPMain.getTextRespectUISPr("agstkl.label.nettovkpreis")),
				new GridBagConstraints(5, iZeileI, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(1, 2, 1, 2), 0, 0));

		oPanelI.add(wnfGesamtpreisMitAufschlag, new GridBagConstraints(6,
				iZeileI, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.BOTH, new Insets(1, 2, 1, 2), 0, 0));
		oPanelI.add(wlaWaehrungAufschlag2, new GridBagConstraints(7, iZeileI,
				1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(1, 2, 1, 2), 0, 0));

	}

	/**
	 * Diese Zeile enthaelt keine Formatierung und bietet die Basis fuer ein
	 * Panel mit einer Preisfindung, das ueber
	 * addFormatierungszeileNettoeinzelpreis() formtiert wurde. <br>
	 * Die folgenden Felder werden in dieser Zeile angezeigt: <br>
	 * leer - leer - leer - Zusatzrabattsatz - Label Prozent - Label Minus -
	 * Rabattsumme - Waehrung
	 * 
	 * @param oPanelI
	 *            JPanel
	 * @param iZeileI
	 *            int
	 * @throws java.lang.Throwable
	 *             Ausnahme
	 */
	public void addZeileZusatzrabattsumme(JPanel oPanelI, int iZeileI)
			throws Throwable {
		wlaZusatzrabattsatz = new WrapperLabel(
				LPMain.getTextRespectUISPr("detail.label.sonderrabatt"));
		wnfZusatzrabattsatz = new WrapperNumberField(new BigDecimal(
				SystemFac.MIN_N_NUMBER), new BigDecimal(SystemFac.MAX_N_NUMBER));
		wnfZusatzrabattsatz.setDependenceField(true);
		wnfZusatzrabattsatz.setMandatoryField(true);
		wnfZusatzrabattsatz
				.addFocusListener(new PanelPositionenPreiseingabe_wnfZusatzrabattsatz_focusAdapter(
						this));

		wlaProzentZusatzrabattsatz = new WrapperLabel(
				LPMain.getTextRespectUISPr("label.prozent"));
		wlaProzentZusatzrabattsatz.setHorizontalAlignment(SwingConstants.LEFT);
		wlaProzentZusatzrabattsatz
				.setHorizontalTextPosition(SwingConstants.LEFT);
		HelperClient.setDefaultsToComponent(wlaProzentZusatzrabattsatz, 12);

		wlaMinusZusatzrabattsumme = new WrapperLabel(
				LPMain.getTextRespectUISPr("lp.minus"));

		wnfZusatzrabattsumme = new WrapperNumberField();
		wnfZusatzrabattsumme.setDependenceField(true);

		wlaZielwaehrungZusatzrabattsumme = new WrapperLabel();
		wlaZielwaehrungZusatzrabattsumme
				.setHorizontalAlignment(SwingConstants.LEADING);

		// Sichtbarkeit
		if (!bRechtDarfPreiseSehen) {
			wlaZusatzrabattsatz.setVisible(false);
			wnfZusatzrabattsatz.setVisible(false);
			wlaProzentZusatzrabattsatz.setVisible(false);
			wlaMinusZusatzrabattsumme.setVisible(false);
			wnfZusatzrabattsumme.setVisible(false);
			wlaZielwaehrungZusatzrabattsumme.setVisible(false);
		}

		oPanelI.add(wlaZusatzrabattsatz, new GridBagConstraints(2, iZeileI, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(1, 2, 1, 2), 0, 0));
		oPanelI.add(wnfZusatzrabattsatz, new GridBagConstraints(3, iZeileI, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(1, 2, 1, 2), 0, 0));
		oPanelI.add(wlaProzentZusatzrabattsatz, new GridBagConstraints(4,
				iZeileI, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(1, 2, 1, 2), 0, 0));
		oPanelI.add(wlaMinusZusatzrabattsumme, new GridBagConstraints(5,
				iZeileI, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(1, 2, 1, 2), 0, 0));
		oPanelI.add(wnfZusatzrabattsumme, new GridBagConstraints(6, iZeileI, 1,
				1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
				new Insets(1, 2, 1, 2), 0, 0));
		oPanelI.add(wlaZielwaehrungZusatzrabattsumme, new GridBagConstraints(7,
				iZeileI, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(1, 2, 1, 2), 0, 0));
	}

	/**
	 * Diese Zeile enthaelt keine Formatierung und bietet die Basis fuer ein
	 * Panel mit einer Preisfindung, das ueber
	 * addFormatierungszeileNettoeinzelpreis() formtiert wurde. <br>
	 * Die folgenden Felder werden in dieser Zeile angezeigt: <br>
	 * leer - leer - leer - Zusatzrabattsatz - Label Prozent - Label Minus -
	 * Rabattsumme - Waehrung
	 * 
	 * @param oPanelI
	 *            JPanel
	 * @param iZeileI
	 *            int
	 * @throws java.lang.Throwable
	 *             Ausnahme
	 */
	public void addZeileMaterialzuschlag(JPanel oPanelI, int iZeileI)
			throws Throwable {
		wlaMaterialzuschlag = new WrapperLabel(
				LPMain.getTextRespectUISPr("lp.materialzuschlag"));
		wnfMaterialzuschlag = new WrapperNumberField(new BigDecimal(
				SystemFac.MIN_N_NUMBER), new BigDecimal(SystemFac.MAX_N_NUMBER));
		wnfMaterialzuschlag.setDependenceField(true);
		wnfMaterialzuschlag.setActivatable(false);
		wnfMaterialzuschlag
				.addFocusListener(new PanelPositionenPreiseingabe_wnfMaterialzuschlag_focusAdapter(
						this));

		wlaPlusMaterialzuschlag = new WrapperLabel("+");

		wlaZielwaehrungMaterialzuschlag = new WrapperLabel();
		wlaZielwaehrungMaterialzuschlag
				.setHorizontalAlignment(SwingConstants.LEADING);

		// Sichtbarkeit
		if (!bRechtDarfPreiseSehen) {
			wlaMaterialzuschlag.setVisible(false);
			wlaPlusMaterialzuschlag.setVisible(false);
			wnfMaterialzuschlag.setVisible(false);
			wlaZielwaehrungMaterialzuschlag.setVisible(false);
		}

		oPanelI.add(wlaMaterialzuschlag, new GridBagConstraints(3, iZeileI, 2,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(1, 2, 1, 2), 0, 0));

		oPanelI.add(wlaPlusMaterialzuschlag, new GridBagConstraints(5, iZeileI,
				1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(1, 2, 1, 2), 0, 0));
		oPanelI.add(wnfMaterialzuschlag, new GridBagConstraints(6, iZeileI, 1,
				1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
				new Insets(1, 2, 1, 2), 0, 0));
		oPanelI.add(wlaZielwaehrungMaterialzuschlag, new GridBagConstraints(7,
				iZeileI, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(1, 2, 1, 2), 0, 0));
	}

	public void setVisibleZeileRabattsumme(boolean bVisible) {
		wlaRabattsatz.setVisible(bVisible);
		wnfRabattsatz.setVisible(bVisible);
		wlaProzent1.setVisible(bVisible);
		wlaMinus.setVisible(bVisible);
		wnfRabattsumme.setVisible(bVisible);
		wlaZielwaehrung1.setVisible(bVisible);
	}

	public void setVisibleZeileZusatzrabattsumme(boolean bVisible) {
		wlaZusatzrabattsatz.setVisible(bVisible);
		wnfZusatzrabattsatz.setVisible(bVisible);
		wlaProzentZusatzrabattsatz.setVisible(bVisible);
		wlaMinusZusatzrabattsumme.setVisible(bVisible);
		wnfZusatzrabattsumme.setVisible(bVisible);
		wlaZielwaehrungZusatzrabattsumme.setVisible(bVisible);
	}

	protected void addZeileNettogesamtpreis(JPanel oPanelI, int iYGridBagNext,
			boolean bMitKostentraeger) throws Throwable {
		// CK->UW Hier Gestehungspreis eingefuegt
		wlaGestpreis = new WrapperLabel();
		wlaGestpreis.setText(LPMain.getTextRespectUISPr("lp.gestehungspreis"));
		wnfGestpreis = new WrapperNumberField();
		wlaNettopreis = new WrapperLabel();
		wlaNettopreis.setText(LPMain.getTextRespectUISPr("label.nettopreis"));

		wnfNettopreis = new WrapperFixableNumberField();
		buttonGroupFixable.add(wnfNettopreis.getWrbFixNumber());
		wnfNettopreis.setDependenceField(true);
		wnfNettopreis.setMandatoryField(true);
		wnfNettopreis
				.getWrapperWrapperNumberField()
				.addFocusListener(
						new PanelPositionenPreiseingabe_wnfNettopreis_focusAdapter(
								this));
		wnfNettopreis.getWrapperWrapperNumberField().addKeyListener(
				new PanelPositionenPreiseingabe_wnfNettopreis_keyAdapter(this));

		wlaZielwaehrung2 = new WrapperLabel();
		wlaZielwaehrung2.setHorizontalAlignment(SwingConstants.LEADING);

		// Sichtbarkeit
		if (!bRechtDarfPreiseSehen) {
			wlaGestpreis.setVisible(false);
			wnfGestpreis.setVisible(false);
			wlaNettopreis.setVisible(false);
			wnfNettopreis.setVisible(false);
			wlaZielwaehrung2.setVisible(false);
		}

		iZeileNettopreis = iYGridBagNext;

		if (LPMain
				.getInstance()
				.getDesktop()
				.darfAnwenderAufZusatzfunktionZugreifen(
						MandantFac.ZUSATZFUNKTION_KOSTENTRAEGER)
				&& bMitKostentraeger) {
			wsfKostentraeger.setMandatoryField(true);
			add(wsfKostentraeger.getWrapperButton(), new GridBagConstraints(0,
					iYGridBagNext, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.BOTH, new Insets(1, 2, 1, 2), 0, 0));
			add(wsfKostentraeger.getWrapperTextField(), new GridBagConstraints(
					1, iYGridBagNext, 3, 1, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH,
					new Insets(1, 2, 1, 2), 0, 0));
		} else {
			// PJ18312
			if (this instanceof PanelPositionenArtikelVerkauf
					&& getInternalFrame().getBelegartCNr() != null
					&& (getInternalFrame().getBelegartCNr().equals(
							LocaleFac.BELEGART_ANGEBOT) || getInternalFrame()
							.getBelegartCNr()
							.equals(LocaleFac.BELEGART_AUFTRAG))) {

				ParametermandantDto parameterDto = DelegateFactory
						.getInstance()
						.getParameterDelegate()
						.getMandantparameter(
								LPMain.getTheClient().getMandant(),
								ParameterFac.KATEGORIE_ANGEBOT,
								ParameterFac.PARAMETER_LIEFERANT_ANGEBEN);
				boolean bLieferantAngeben = (Boolean) parameterDto
						.getCWertAsObject();
				if (bLieferantAngeben == true) {
					add(wsfLieferant.getWrapperButton(),
							new GridBagConstraints(0, iYGridBagNext, 1, 1, 0.0,
									0.0, GridBagConstraints.CENTER,
									GridBagConstraints.BOTH, new Insets(1, 2,
											1, 2), 0, 0));
					add(wsfLieferant.getWrapperTextField(),
							new GridBagConstraints(1, iYGridBagNext, 3, 1, 0.0,
									0.0, GridBagConstraints.CENTER,
									GridBagConstraints.BOTH, new Insets(1, 2,
											1, 2), 0, 0));
				}
			}
		}

		oPanelI.add(wlaGestpreis, new GridBagConstraints(1, iYGridBagNext, 2,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(1, 2, 1, 2), 0, 0));
		oPanelI.add(wnfGestpreis, new GridBagConstraints(3, iYGridBagNext, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(1, 2, 1, 2), 0, 0));

		oPanelI.add(wlaNettopreis, new GridBagConstraints(4, iYGridBagNext, 2,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(1, 2, 1, 2), 0, 0));
		oPanelI.add(wnfNettopreis, new GridBagConstraints(6, iYGridBagNext, 1,
				1, 3.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(1, 2, 1, 2), 0, 0));
		oPanelI.add(wlaZielwaehrung2, new GridBagConstraints(7, iYGridBagNext,
				1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(1, 2, 1, 2), 0, 0));

		wlaGestpreis.setVisible(false);
		wnfGestpreis.setVisible(false);

	}

	public void setVisibleZeileMaterialzuschlag(boolean bVisible) {
		if (wlaMaterialzuschlag != null) {
			wlaMaterialzuschlag.setVisible(bVisible);
			wnfMaterialzuschlag.setVisible(bVisible);
			wlaZielwaehrungMaterialzuschlag.setVisible(bVisible);
			wlaPlusMaterialzuschlag.setVisible(bVisible);
		}
	}

	public void setVisibleZeileNettogesamtpreis(boolean bVisible) {
		wlaNettopreis.setVisible(bVisible);
		wnfNettopreis.setVisible(bVisible);
		wlaZielwaehrung2.setVisible(bVisible);
	}

	protected void addZeileMwstsumme(JPanel oPanelI, int iYGridBagNext)
			throws Throwable {
		wlaMwstsatz = new WrapperLabel(
				LPMain.getTextRespectUISPr("lp.mwstshort"));

		wcoMwstsatz = new WrapperComboBox();
		wcoMwstsatz.setDependenceField(true);
		wcoMwstsatz.setMandatoryFieldDB(true);
		wcoMwstsatz
				.addItemListener(new PanelPositionenPreiseingabe_jComboBoxMwstsatz_itemAdapter(
						this));

		wlaProzent2 = new WrapperLabel(
				LPMain.getTextRespectUISPr("label.prozent"));
		wlaProzent2.setHorizontalAlignment(SwingConstants.LEFT);
		wlaProzent2.setHorizontalTextPosition(SwingConstants.LEFT);

		wlaPlus = new WrapperLabel(LPMain.getTextRespectUISPr("lp.plus"));

		wnfMwstsumme = new WrapperNumberField();
		wnfMwstsumme.setDependenceField(true);

		wlaZielwaehrung3 = new WrapperLabel();
		wlaZielwaehrung3.setHorizontalAlignment(SwingConstants.LEADING);

		// Sichtbarkeit
		if (!bRechtDarfPreiseSehen) {
			wlaMwstsatz.setVisible(false);
			wcoMwstsatz.setVisible(false);
			wlaProzent2.setVisible(false);
			wlaPlus.setVisible(false);
			wnfMwstsumme.setVisible(false);
			wlaZielwaehrung3.setVisible(false);
		}

		oPanelI.add(wlaMwstsatz, new GridBagConstraints(0, iYGridBagNext, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(1, 2, 1, 2), 0, 0));
		oPanelI.add(wcoMwstsatz, new GridBagConstraints(1, iYGridBagNext, 3, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(1, 2, 1, 2), 0, 0));
		oPanelI.add(wlaProzent2, new GridBagConstraints(4, iYGridBagNext, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(1, 2, 1, 2), 0, 0));
		add(wlaPlus, new GridBagConstraints(5, iYGridBagNext, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						1, 2, 1, 2), 0, 0));
		oPanelI.add(wnfMwstsumme, new GridBagConstraints(6, iYGridBagNext, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(1, 2, 1, 2), 0, 0));
		oPanelI.add(wlaZielwaehrung3, new GridBagConstraints(7, iYGridBagNext,
				1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(1, 2, 1, 2), 0, 0));
	}

	public void setVisibleZeileMwstsumme(boolean bVisible) {
		wlaMwstsatz.setVisible(bVisible);
		wcoMwstsatz.setVisible(bVisible);
		wlaProzent2.setVisible(bVisible);
		wlaPlus.setVisible(bVisible);
		wnfMwstsumme.setVisible(bVisible);
		wlaZielwaehrung3.setVisible(bVisible);
	}

	public void setVisibleGestehungspreis(boolean bVisible) {
		// CK->UW
		wlaGestpreis.setVisible(bVisible);
		wnfGestpreis.setVisible(bVisible);
	}

	protected void addZeileBruttogesamtpreis(JPanel oPanelI, int iYGridBagNext)
			throws Throwable {
		wlaBruttopreis = new WrapperLabel(
				LPMain.getTextRespectUISPr("label.bruttopreis"));
		wnfBruttopreis = new WrapperNumberField();
		wnfBruttopreis.setDependenceField(true);

		wlaZielwaehrungBruttopreis = new WrapperLabel();
		wlaZielwaehrungBruttopreis
				.setHorizontalAlignment(SwingConstants.LEADING);

		// Sichtbarkeit
		if (!bRechtDarfPreiseSehen) {
			wlaBruttopreis.setVisible(false);
			wnfBruttopreis.setVisible(false);
			wlaZielwaehrungBruttopreis.setVisible(false);
		}

		add(wlaBruttopreis, new GridBagConstraints(4, iYGridBagNext, 2, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(1, 2, 1, 2), 0, 0));
		add(wnfBruttopreis, new GridBagConstraints(6, iYGridBagNext, 1, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(1, 2, 1, 2), 0, 0));
		add(wlaZielwaehrungBruttopreis, new GridBagConstraints(7,
				iYGridBagNext, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(1, 2, 1, 2), 0, 0));
	}

	protected void addZeileLieferterminposition(JPanel oPanelI,
			int iYGridBagNext) throws Throwable {
		wlaLieferterminPosition = new WrapperLabel();
		wlaLieferterminPosition.setText(LPMain
				.getTextRespectUISPr("auft.label.postitionstermin"));
		wdfLieferterminPosition = new WrapperDateField();

		add(wlaLieferterminPosition, new GridBagConstraints(0, iYGridBagNext,
				1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(10, 2, 2, 2), 0, 0));
		add(wdfLieferterminPosition, new GridBagConstraints(1, iYGridBagNext,
				1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(10, 2, 2, 2), 0, 0));

	}

	public void setVisibleZeileBruttogesamtpreis(boolean bVisible) {
		wlaBruttopreis.setVisible(bVisible);
		wnfBruttopreis.setVisible(bVisible);
		wlaZielwaehrungBruttopreis.setVisible(bVisible);
	}

	public void setVisibleZeileLieferterminposition(boolean bVisible) {
		wlaLieferterminPosition.setVisible(bVisible);
		wdfLieferterminPosition.setVisible(bVisible);
	}

	protected void setDefaults() throws Throwable {
		if (wnfMenge != null && wnfMenge.getMaximumValue().doubleValue() >= 1) {
			wnfMenge.setBigDecimal(new BigDecimal(1));
		}
		if (wcoVerleih != null) {
			wcoVerleih.setKeyOfSelectedItem(null);
		}
		if (wcoEinheit != null) {
			wcoEinheit.setKeyOfSelectedItem(SystemFac.EINHEIT_STUECK);
		}
		/**
		 * @todo MB->MB performance = katastrophal. wir muessen das wieder am
		 *       client cachen. belegposition refresh mit gecachten mwst ~400ms,
		 *       ohne ~900ms
		 */
		if (wcoMwstsatz != null /* && !wcoMwstsatz.isMapSet() */) {
			wcoMwstsatz.setMap(DelegateFactory
					.getInstance()
					.getMandantDelegate()
					.getAllMwstsatz(LPMain.getTheClient().getMandant(),
							getTBelegdatumMwstsatz()));
		}

		if (wnfRabattsatz != null) {
			wnfRabattsatz.setDouble(0.0);
		}

		if (wnfZusatzrabattsatz != null) {
			wnfZusatzrabattsatz.setDouble(0.0);
		}
		if (wnfMaterialzuschlag != null) {
			wnfMaterialzuschlag.setDouble(0.0);
			wnfMaterialzuschlag.setFractionDigits(iPreiseUINachkommastellen);
		}

		if (wnfEinzelpreis != null) {
			wnfEinzelpreis.setBigDecimal(new BigDecimal(0));
			wnfEinzelpreis.setFractionDigits(iPreiseUINachkommastellen);
		}

		if (wnfRabattsumme != null) {
			wnfRabattsumme.setBigDecimal(new BigDecimal(0));
			wnfRabattsumme.setActivatable(false);
			wnfRabattsumme.setFractionDigits(iPreiseUINachkommastellen);
			wnfRabattsumme.getWrbFixNumber().setSelected(true);
		}

		if (wnfZusatzrabattsumme != null) {
			wnfZusatzrabattsumme.setBigDecimal(new BigDecimal(0));
			wnfZusatzrabattsumme.setActivatable(false);
			wnfZusatzrabattsumme.setFractionDigits(iPreiseUINachkommastellen);
		}

		if (wnfNettopreis != null) {
			wnfNettopreis.setBigDecimal(new BigDecimal(0));
			wnfNettopreis.setFractionDigits(iPreiseUINachkommastellen);
		}

		if (wnfMwstsumme != null) {
			wnfMwstsumme.setBigDecimal(new BigDecimal(0));
			wnfMwstsumme.setActivatable(false);
			wnfMwstsumme.setFractionDigits(iPreiseUINachkommastellen);
		}

		if (wnfBruttopreis != null) {
			wnfBruttopreis.setBigDecimal(new BigDecimal(0));
			wnfBruttopreis.setActivatable(false);
			wnfBruttopreis.setFractionDigits(iPreiseUINachkommastellen);
		}

		if (wtfZusatzbezeichnung != null) {
			wtfZusatzbezeichnung.setActivatable(true);
		}

		if (wbuArtikelauswahl != null) {
			wbuArtikelauswahl.setOKey(null);
		}
		setArtikelDto(new ArtikelDto());

		wsfKostentraeger.setKey(getInternalFrame().letzteKostentraegerIId);

		if (wnfAufschlagBetrag != null) {
			wnfAufschlagBetrag.setBigDecimal(new BigDecimal(0));
			wnfAufschlagBetrag.setFractionDigits(iPreiseUINachkommastellen);
		}
		if (wnfGesamtpreisMitAufschlag != null) {
			wnfGesamtpreisMitAufschlag.setBigDecimal(new BigDecimal(0));
			wnfGesamtpreisMitAufschlag
					.setFractionDigits(iPreiseUINachkommastellen);
		}

	}

	/**
	 * Wenn dieses Feld verlassen wird, muessen die Preisfelder neu berechnet
	 * werden.
	 * 
	 * @param e
	 *            Ereignis
	 * @throws java.lang.Throwable
	 *             Ausnahme
	 */
	protected void wnfEinzelpreis_focusLost(FocusEvent e) throws Throwable {
		if (wnfEinzelpreis.getBigDecimal() != null
				&& wnfRabattsatz.getDouble() != null) {
			berechneNettogesamtpreis();
		}
	}

	protected void wnfGesamtpreisMitAufschlag_focusLost(FocusEvent e)
			throws Throwable {
		berechneNettogesamtpreis();
	}

	protected void wnfAufschlag_focusLost(FocusEvent e) throws Throwable {
		if (wnfEinzelpreis.getBigDecimal() != null
				&& wnfRabattsatz.getDouble() != null) {
			berechneAufschlag();
		}
	}

	/**
	 * Nettopreis und Einzelpreis bestimmen den Rabattsatz. <br>
	 * Hier muss mit 4 Stellen gerechnet werden, sonst stimmt das Rueckrechnen
	 * nicht mehr.
	 * 
	 * @param e
	 *            Ereignis
	 * @throws java.lang.Throwable
	 *             Ausnahme
	 */
	void wnfNettopreis_focusLost(FocusEvent e) throws Throwable {
		berechneNettogesamtpreis();

		/*
		 * final int iAnzahlNachkommastellen = 4;
		 * 
		 * if (wnfEinzelpreis.getBigDecimal() != null &&
		 * wnfNettopreis.getBigDecimal() != null) { if
		 * (wnfEinzelpreis.getBigDecimal().compareTo(new BigDecimal(0)) != 0) {
		 * 
		 * BigDecimal nBetragVonEinzelpreisMinusRabattbetrag = null; if
		 * (wnfMaterialzuschlag != null && wnfMaterialzuschlag.getBigDecimal()
		 * != null) { nBetragVonEinzelpreisMinusRabattbetrag = wnfNettopreis
		 * .getBigDecimal(); } else { nBetragVonEinzelpreisMinusRabattbetrag =
		 * wnfNettopreis .getBigDecimal(); }
		 * 
		 * // die gesamte Rabattsumme ist eventuell zwischen Rabatt und //
		 * Zusatzrabatt // aufgeteilt; der Zusatzrabattsatz wird nicht
		 * veraendert if (wnfZusatzrabattsatz != null) { BigDecimal
		 * nZusatzRabattsatz = wnfZusatzrabattsatz .getBigDecimal();
		 * 
		 * if (nZusatzRabattsatz != null && nZusatzRabattsatz.doubleValue() !=
		 * 0) { BigDecimal nProzentsatzVonEinzelpreisMinusRabattbetrag = new
		 * BigDecimal( 100).subtract(nZusatzRabattsatz);
		 * 
		 * nBetragVonEinzelpreisMinusRabattbetrag = new BigDecimal( 100)
		 * .multiply(wnfNettopreis.getBigDecimal())
		 * .divide(nProzentsatzVonEinzelpreisMinusRabattbetrag,
		 * iAnzahlNachkommastellen, BigDecimal.ROUND_HALF_EVEN); // 2 // Stellen
		 * // nicht // ausreichend
		 * 
		 * wnfZusatzrabattsumme.setBigDecimal(wnfNettopreis .getBigDecimal()
		 * .subtract( nBetragVonEinzelpreisMinusRabattbetrag) .negate()); } }
		 * 
		 * // der Einzelpreis muss immer existieren BigDecimal bdRabattsumme =
		 * wnfEinzelpreis.getBigDecimal().subtract(
		 * nBetragVonEinzelpreisMinusRabattbetrag);
		 * 
		 * 
		 * BigDecimal bdRabattsatz = bdRabattsumme.divide(
		 * wnfEinzelpreis.getBigDecimal(), iAnzahlNachkommastellen,
		 * BigDecimal.ROUND_HALF_EVEN). // 2 // Stellen // nicht // ausreichend
		 * movePointRight(2);
		 * 
		 * wnfRabattsatz.setBigDecimal(bdRabattsatz);
		 * wnfRabattsumme.setBigDecimal(bdRabattsumme);
		 * 
		 * berechneMwst(); } }
		 */
	}

	/**
	 * Wenn dieses Feld verlassen wird, muessen die Preisfelder neu berechnet
	 * werden.
	 * 
	 * @param e
	 *            Ereignis
	 * @throws java.lang.Throwable
	 *             Ausnahme
	 */
	public void wnfRabattsatz_focusLost(FocusEvent e) throws Throwable {
		if (wnfEinzelpreis.getBigDecimal() != null
				&& wnfRabattsatz.getDouble() != null) {
			berechneNettogesamtpreis();
		}
	}

	/**
	 * Wenn dieses Feld verlassen wird, muessen die Preisfelder neu berechnet
	 * werden.
	 * 
	 * @param e
	 *            Ereignis
	 * @throws java.lang.Throwable
	 *             Ausnahme
	 */
	protected void wnfZusatzrabattsatz_focusLost(FocusEvent e) throws Throwable {
		if (wnfEinzelpreis.getBigDecimal() != null
				&& wnfRabattsatz.getDouble() != null) {
			berechneNettogesamtpreis();
		}
	}

	protected void wnfMaterialzuschlag_focusLost(FocusEvent e) throws Throwable {
		if (wnfEinzelpreis.getBigDecimal() != null
				&& wnfRabattsatz.getDouble() != null) {
			berechneNettogesamtpreis();
		}
	}

	/**
	 * Wenn das Feld "Menge" verlassen wird.
	 * 
	 * @param e
	 *            Ereignis
	 */
	protected void wnfMenge_focusLost(FocusEvent e) {
		// im Moment hier nix. ACHTUNG spezialbehandlung in den subklassen.
	}

	protected void wnfMenge_inputVerifier() {
		// im Moment hier nix. ACHTUNG spezialbehandlung in den subklassen.
	}

	protected void wnfNettopreis_inputVerifier() {
		// im Moment hier nix. ACHTUNG spezialbehandlung in den subklassen.
	}

	/**
	 * Es wurde ein neuer Mwsatsatz gewaehlt.
	 * 
	 * @param e
	 *            enthaelt den neuen Mwstsatz
	 * @throws java.lang.Throwable
	 *             Ausnahme
	 */
	protected void jComboBoxMwstsatz_itemStateChanged(ItemEvent e)
			throws Throwable {
		// erst dann berechnen, wenn die Preisfelder nicht mehr leer sind
		if (wnfEinzelpreis.getBigDecimal() != null) {
			berechneMwst();
		}
	}

	public void berechneAufschlag() throws Throwable {
		if (wnfAufschlagBetrag != null && wnfAufschlagProzent != null
				&& wnfGesamtpreisMitAufschlag != null) {

			if (wnfAufschlagBetrag.getWrbFixNumber().isSelected()) {
				// Wenn Aufschlag(Prozent) fixiert, dann Gesamtpreis und
				// Rabattsumme berechnen

				if (wnfNettopreis.getBigDecimal() != null
						&& wnfAufschlagProzent.getBigDecimal() != null) {

					BigDecimal aufschlagBetrag = Helper.getProzentWert(
							wnfNettopreis.getBigDecimal(),
							wnfAufschlagProzent.getBigDecimal(),
							iPreiseUINachkommastellen);

					wnfAufschlagBetrag.setBigDecimal(aufschlagBetrag);

					wnfGesamtpreisMitAufschlag.setBigDecimal(wnfNettopreis
							.getBigDecimal().add(aufschlagBetrag));
				}

			} else {
				// Wenn Gesamtsumme fixiert, dann Rabatt berechnen

				if (wnfGesamtpreisMitAufschlag.getBigDecimal() != null
						&& wnfNettopreis.getBigDecimal() != null) {

					BigDecimal aufschlagBetrag = wnfGesamtpreisMitAufschlag
							.getBigDecimal().subtract(
									wnfNettopreis.getBigDecimal());

					wnfAufschlagBetrag.setBigDecimal(aufschlagBetrag);

					double satz = (aufschlagBetrag.doubleValue() / (wnfNettopreis
							.getBigDecimal().doubleValue())) * 100;

					wnfAufschlagProzent.setDouble(satz);
				}

			}

		}

	}

	/**
	 * Einzelpreis, Rabattsatz und Zusatzrabattsatz bestimmen den Nettopreis.
	 * 
	 * @throws Throwable
	 */
	private void berechneNettogesamtpreis() throws Throwable {

		BigDecimal nRabattsumme = wnfEinzelpreis.getBigDecimal().multiply(
				new BigDecimal(wnfRabattsatz.getDouble().doubleValue())
						.movePointLeft(2));

		BigDecimal nZusatzRabattsumme = new BigDecimal(0);

		if (wnfNettopreis.getWrbFixNumber().isSelected()) {

			if (wnfZusatzrabattsatz != null) {

				if (wnfZusatzrabattsatz.getDouble() == 100) {
					nZusatzRabattsumme = wnfEinzelpreis.getBigDecimal()
							.subtract(wnfRabattsumme.getBigDecimal());
				} else {

					if (wnfMaterialzuschlag != null
							&& wnfMaterialzuschlag.getBigDecimal() != null) {
						nZusatzRabattsumme = wnfNettopreis
								.getBigDecimal()
								.subtract(wnfMaterialzuschlag.getBigDecimal())
								.divide(new BigDecimal(
										1 - ((wnfZusatzrabattsatz.getDouble()
												.doubleValue() / 100))), 4,
										BigDecimal.ROUND_HALF_EVEN)
								.multiply(
										new BigDecimal(
												wnfZusatzrabattsatz.getDouble()
														.doubleValue() / 100));
					} else {
						nZusatzRabattsumme = wnfNettopreis
								.getBigDecimal()
								.divide(new BigDecimal(
										1 - ((wnfZusatzrabattsatz.getDouble()
												.doubleValue() / 100))), 4,
										BigDecimal.ROUND_HALF_EVEN)
								.multiply(
										new BigDecimal(
												wnfZusatzrabattsatz.getDouble()
														.doubleValue() / 100));
					}

				}
			}

			if (wnfMaterialzuschlag != null
					&& wnfMaterialzuschlag.getBigDecimal() != null) {

				nRabattsumme = wnfEinzelpreis.getBigDecimal().subtract(
						wnfNettopreis.getBigDecimal().subtract(
								wnfMaterialzuschlag.getBigDecimal()));
			} else {
				nRabattsumme = wnfEinzelpreis.getBigDecimal().subtract(
						wnfNettopreis.getBigDecimal());
			}
		} else {
			nRabattsumme = wnfEinzelpreis.getBigDecimal().multiply(
					new BigDecimal(wnfRabattsatz.getDouble().doubleValue())
							.movePointLeft(2));
			if (wnfZusatzrabattsatz != null
					&& wnfZusatzrabattsatz.getDouble() != null) {
				nZusatzRabattsumme = wnfEinzelpreis
						.getBigDecimal()
						.subtract(nRabattsumme)
						.multiply(
								new BigDecimal(wnfZusatzrabattsatz.getDouble()
										.doubleValue()).movePointLeft(2));
			}
		}

		nZusatzRabattsumme = Helper.rundeKaufmaennisch(nZusatzRabattsumme, 4);

		if (wnfNettopreis.getWrbFixNumber().isSelected()) {
			wnfRabattsumme.setBigDecimal(nRabattsumme
					.subtract(nZusatzRabattsumme));
			nRabattsumme = nRabattsumme.subtract(nZusatzRabattsumme);
		} else {
			wnfRabattsumme.setBigDecimal(nRabattsumme);

		}

		BigDecimal nEinzelpreisMinusRabattsumme = wnfEinzelpreis
				.getBigDecimal().subtract(nRabattsumme)
				.subtract(nZusatzRabattsumme);
		if (wnfZusatzrabattsumme != null) {

			wnfZusatzrabattsumme.setBigDecimal(nZusatzRabattsumme);
		}
		// UW->WH Zusatzrabatt auf Einzelpreis - Rabattsumme
		if (!wnfNettopreis.getWrbFixNumber().isSelected()) {
			// Zusatzrabatt beruecksichtigen, wenn vorhanden
			if (wnfZusatzrabattsatz != null
					&& wnfZusatzrabattsatz.getBigDecimal() != null) {

				if (wnfMaterialzuschlag != null
						&& wnfMaterialzuschlag.getBigDecimal() != null) {
					wnfNettopreis.setBigDecimal(nEinzelpreisMinusRabattsumme
							.add(wnfMaterialzuschlag.getBigDecimal()));
				} else {
					wnfNettopreis.setBigDecimal(nEinzelpreisMinusRabattsumme);
				}

			} else {
				if (wnfMaterialzuschlag != null
						&& wnfMaterialzuschlag.getBigDecimal() != null) {
					wnfNettopreis.setBigDecimal(nEinzelpreisMinusRabattsumme
							.add(wnfMaterialzuschlag.getBigDecimal()));
				} else {
					wnfNettopreis.setBigDecimal(nEinzelpreisMinusRabattsumme);
				}
			}
		} else {

			// PJ 15092
			if (wnfNettopreis.getWrbFixNumber().isSelected()) {

				// Wenn RadioButton-Nettopreis selektiert, dann muss der
				// Einzelpreis
				// zurueckgerechnet werden

				if (wnfNettopreis.getBigDecimal() != null) {
					BigDecimal einzelpreis = wnfNettopreis.getBigDecimal();

					if (wnfMaterialzuschlag != null
							&& wnfMaterialzuschlag.getBigDecimal() != null) {
						einzelpreis = einzelpreis.subtract(wnfMaterialzuschlag
								.getBigDecimal());
					}

					if (wnfRabattsumme.getBigDecimal() != null) {
						einzelpreis = einzelpreis.add(wnfRabattsumme
								.getBigDecimal());
					}
					if (wnfZusatzrabattsumme != null
							&& wnfZusatzrabattsumme.getBigDecimal() != null) {
						einzelpreis = einzelpreis.add(wnfZusatzrabattsumme
								.getBigDecimal());
					}

					wnfEinzelpreis.setBigDecimal(einzelpreis);

					double satz = 0;

					if (wnfEinzelpreis.getBigDecimal().doubleValue() != 0) {

						satz = (nRabattsumme.doubleValue() / (wnfEinzelpreis
								.getBigDecimal().doubleValue())) * 100;
					}

					wnfRabattsatz.setDouble(satz);

				}
			} else {
				// Rabatt berechnen / Zusatzrabatt darf nicht beruehrt werden

				if (wnfNettopreis.getBigDecimal() != null
						&& wnfEinzelpreis.getBigDecimal() != null
						&& wnfEinzelpreis.getBigDecimal().doubleValue() != 0) {

					BigDecimal rabatt = null;

					if (wnfMaterialzuschlag != null
							&& wnfMaterialzuschlag.getBigDecimal() != null) {
						rabatt = wnfEinzelpreis.getBigDecimal().subtract(
								wnfNettopreis.getBigDecimal().subtract(
										wnfMaterialzuschlag.getBigDecimal()));
					} else {
						rabatt = wnfEinzelpreis.getBigDecimal().subtract(
								wnfNettopreis.getBigDecimal());
					}

					if (wnfZusatzrabattsumme != null) {

						rabatt = rabatt.subtract(wnfZusatzrabattsumme
								.getBigDecimal());
					}
					double satz = (rabatt.doubleValue() / (wnfEinzelpreis
							.getBigDecimal().doubleValue())) * 100;

					wnfRabattsatz.setDouble(satz);
					wnfRabattsatz_focusLost(null);
				}
			}

		}
		berechneMwst();
		berechneAufschlag();
	}

	/**
	 * Neubrechnung der Mwst aus den vorhandenen Feldern.
	 * 
	 * @throws Throwable
	 */
	private void berechneMwst() throws Throwable {
		if (wcoMwstsatz != null && wcoMwstsatz.getKeyOfSelectedItem() != null) {
			Double ddMwstsatz = DelegateFactory
					.getInstance()
					.getMandantDelegate()
					.mwstsatzFindByPrimaryKey(
							(Integer) wcoMwstsatz.getKeyOfSelectedItem())
					.getFMwstsatz();

			wnfMwstsumme.setBigDecimal(wnfNettopreis.getBigDecimal().multiply(
					new BigDecimal(ddMwstsatz.doubleValue()).movePointLeft(2)));

			wnfBruttopreis.setBigDecimal(wnfNettopreis.getBigDecimal().add(
					wnfMwstsumme.getBigDecimal()));
		}
	}

	public Timestamp getTBelegdatumMwstsatz() {
		return tBelegdatumMwstsatz;
	}

	public void setTBelegdatumMwstsatz(Timestamp tBelegdatumMwstsatz) {
		this.tBelegdatumMwstsatz = tBelegdatumMwstsatz;
	}

	public Map<?, ?> getTmpMwst() {
		return tmpMwst;
	}

	public void setTmpMwst(Map<?, ?> tMwstsatz) {
		this.tmpMwst = tMwstsatz;
	}

	/**
	 * Behandle Ereignis Neu.
	 * 
	 * @param eventObject
	 *            Ereignis
	 * @param bLockMeI
	 *            legt fest, ob keyForLockMe ueberschreiben
	 * @param bNeedNoNewI
	 *            boolean
	 * @throws Throwable
	 *             Ausnahme
	 */
	public void eventActionNew(EventObject eventObject, boolean bLockMeI,
			boolean bNeedNoNewI) throws Throwable {
		super.eventActionNew(eventObject, true, false); // LockMeForNew setzen

		setDefaults();

		setArtikelEingabefelderEditable(false);
	}

	public void setArtikelEingabefelderEditable(boolean bEditable) {
		if (wtfBezeichnung != null) {
			wtfBezeichnung.setEditable(bEditable);
		}
		if (wtfZusatzbezeichnung != null) {
			wtfZusatzbezeichnung.setEditable(bEditable);
		}

		if (wnfMenge != null) {

			if (this instanceof PanelPositionenArtikelVerkaufSNR) {
				if (getArtikelDto() != null
						&& getArtikelDto().getBSeriennrtragend() != null
						&& getArtikelDto().getBChargennrtragend() != null
						&& (Helper.short2boolean(getArtikelDto()
								.getBSeriennrtragend()) || Helper
								.short2boolean(getArtikelDto()
										.getBChargennrtragend()))) {

					wnfMenge.setEditable(false);
				} else {
					wnfMenge.setEditable(bEditable);
				}

			} else {
				wnfMenge.setEditable(bEditable);
			}

		}

		// PJ17059
		if (bRechtDarfPreiseAendern == false && bEditable == true) {
			bEditable = false;
		}

		if (wnfEinzelpreis != null) {

			if (bVkpreiseingabeNurNetto == false) {
				wnfEinzelpreis.setEditable(bEditable);
			} else {
				wnfEinzelpreis.setEditable(false);
			}

		}

		if (wnfRabattsatz != null) {
			if (bVkpreiseingabeNurNetto == false) {
				wnfRabattsatz.setEditable(bEditable);
			} else {
				wnfRabattsatz.setEditable(false);
			}
		}

		if (wnfZusatzrabattsatz != null) {
			if (bVkpreiseingabeNurNetto == false) {
				wnfZusatzrabattsatz.setEditable(bEditable);
			} else {
				wnfZusatzrabattsatz.setEditable(false);
			}
		}
		if (wcoMwstsatz != null) {
			wcoMwstsatz.setEnabled(bEditable);
		}
		if (wnfNettopreis != null) {
			wnfNettopreis.setEditable(bEditable);
		}

		if (wnfGesamtpreisMitAufschlag != null) {
			wnfGesamtpreisMitAufschlag.setEditable(bEditable);
		}
		if (bVkpreiseingabeNurNetto == true) {
			wnfRabattsumme.getWrbFixNumber().setActivatable(false);
			wnfRabattsumme.getWrbFixNumber().setEnabled(false);
			wnfNettopreis.getWrbFixNumber().setSelected(true);
		}

	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;

		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			if (e.getSource() == wifArtikelauswahl) {
				if (wifArtikelauswahl.getArtikelDto() != null) {

					if (getInternalFrame().getBelegartCNr().equals(
							LocaleFac.BELEGART_RECHNUNG)
							|| getInternalFrame().getBelegartCNr().equals(
									LocaleFac.BELEGART_LIEFERSCHEIN)) {
						if (Helper.short2boolean(wifArtikelauswahl
								.getArtikelDto().getBKalkulatorisch())) {
							// SP1467 Kalkulatorischer Artikel in Rechnung nicht
							// erlaubt

							DialogFactory
									.showModalDialog(
											LPMain.getTextRespectUISPr("lp.info"),
											LPMain.getTextRespectUISPr("lp.kalkulatorischer.artikel.darfin.rels.nichtverwendetwerden"));
							return;
						}
					}

					setArtikelEingabefelderEditable(true);
					setArtikelDto(wifArtikelauswahl.getArtikelDto());
					artikelDto2components();
					getPanelStatusbar().clearStatusbar();

					if (this instanceof PanelPositionenArtikelVerkaufSNR) {

						if (getParent() instanceof PanelPositionen2) {
							((PanelPositionen2) getParent())
									.setStatusbarSpalte5(((PanelPositionenArtikelVerkaufSNR) this)
											.getLagerstandFuerStatusbarSpalte5());
						}
					}

					// PJ18117
					if (this instanceof PanelPositionenArtikelEingabeSNR) {
						if (getParent() instanceof PanelAuftragPositionen2) {

							((PanelAuftragPositionen2) getParent())
									.aktualisiereStatusbar();
						}
					}

				}
			} else if (e.getSource() == panelQueryFLRArtikellager) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();

				// PJ 17798
				if (key instanceof WwArtikellagerPK) {
					key = ((WwArtikellagerPK) key).getLager_i_id();
				}

				LagerDto lagerDto = DelegateFactory.getInstance()
						.getLagerDelegate()
						.lagerFindByPrimaryKey((Integer) key);
				wtfLager.setText(lagerDto.getCNr());
				selectedlagerIId = lagerDto.getIId();
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_LEEREN) {
			if (e.getSource() == panelQueryFLRArtikellager) {
				selectedlagerIId = null;
				wtfLager.setText(null);

			}
		}
	}

	/**
	 * Die Eigenschaften des Artikels zur Anzeige bringen.
	 * 
	 * @throws java.lang.Throwable
	 *             Ausnahme
	 */
	public void artikelDto2components() throws Throwable {
		// wtfArtikel.setText(getArtikelDto().getCNr());
		wifArtikelauswahl.setArtikelDto(getArtikelDto());
		if (getArtikelDto().getArtikelsprDto() != null) {
			// MB->UW Einheit eingebaut
			if (getArtikelDto().getEinheitCNr() != null) {
				wcoEinheit
						.setKeyOfSelectedItem(getArtikelDto().getEinheitCNr());
			}
			// CK->UW Gestehungspreis vorschlagen
			if (wnfGestpreis.isVisible()
					&& getArtikelDto().getArtikelartCNr() != null) {

				if (getArtikelDto().getArtikelartCNr().equals(
						ArtikelFac.ARTIKELART_ARBEITSZEIT)) {
					try {
						ArtikellieferantDto artikellieferantDto = DelegateFactory
								.getInstance()
								.getArtikelDelegate()
								.getArtikelEinkaufspreis(
										getArtikelDto().getIId(),
										wnfMenge.getBigDecimal(),
										wlaZielwaehrung2.getText());

						if (artikellieferantDto != null) {
							wnfGestpreis.setBigDecimal(artikellieferantDto
									.getLief1Preis());

						} else {
							wnfGestpreis.setBigDecimal(null);
						}
					} catch (Throwable t) {
						// nothing here
					}
				} else {
					try {
						BigDecimal gestpreis = DelegateFactory
								.getInstance()
								.getLagerDelegate()
								.getGemittelterGestehungspreisEinesLagers(
										getArtikelDto().getIId(),
										DelegateFactory.getInstance()
												.getLagerDelegate()
												.getHauptlagerDesMandanten()
												.getIId());

						if (gestpreis != null) {
							gestpreis = DelegateFactory
									.getInstance()
									.getLocaleDelegate()
									.rechneUmInAndereWaehrung(
											gestpreis,
											LPMain.getTheClient()
													.getSMandantenwaehrung(),
											wlaZielwaehrung2.getText());
							wnfGestpreis.setBigDecimal(gestpreis);
						}
					} catch (Throwable t) {
						// nothing here
					}
				}
			}
		}

		// PJ15459 Wenn Materialzuschlag vorhanden, dann Zeile anzeigen
		setVisibleZeileMaterialzuschlag(false);
		if (getArtikelDto().getMaterialIId() != null) {
			if (DelegateFactory
					.getInstance()
					.getMaterialDelegate()
					.materialzuschlagFindAktuellenzuschlag(
							getArtikelDto().getMaterialIId()) != null) {
				setVisibleZeileMaterialzuschlag(true);
			}
		}

	}

	/**
	 * Hier kommen die events meiner speziellen Buttons an.
	 * 
	 * @param e
	 *            ActionEvent
	 * @throws Throwable
	 */
	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		if (e.getActionCommand().equals(ACTION_SPECIAL_LAGER_FROM_LISTE)) {
			dialogQueryLagerFromListe(e);
		}
	}

	public WrapperNumberField getWnfRabattsatz() {
		return wnfRabattsatz;
	}

	public WrapperNumberField getWnfZusatzrabattsatz() {
		return wnfZusatzrabattsatz;
	}

	public WrapperNumberField getWnfZusatzrabattsumme() {
		return wnfZusatzrabattsumme;
	}

	public boolean getBDefaultMwstsatzAusArtikel() {
		return this.bDefaultMwstsatzAusArtikel;
	}

	/**
	 * Nochmaliges Durchrechnen der Preisfelder vor dem Abspeichern mit Strg-S.
	 * 
	 * @throws Throwable
	 */
	protected final void calculateFields() throws Throwable {
		// UW 25.04.06 Wenn hier eine ungueltige Artikelnummer steht...
		if (wifArtikelauswahl != null
				&& wifArtikelauswahl.getWtfIdent() != null
				&& getArtikelDto().getIId() == null) {
			// todo: Validate macht bereits einen Teil des nachfolgenden. Besser
			// kombinieren.
			// wifArtikelauswahl.validate();
			if (wifArtikelauswahl.getWtfIdent().getText() == null) {
				// keine ident eingegeben -> passt
			} else {
				ArtikelDto aDto = null;
				try {
					aDto = DelegateFactory.getInstance().getArtikelDelegate()
							.artikelFindByCNr(wtfArtikel.getText());
				} catch (ExceptionLP ex) {
					switch (ex.getICode()) {
					case EJBExceptionLP.FEHLER_BEI_FIND: {
						// nothing here
					}
						break;
					default: {
						throw ex;
					}
					}
				}
				if (aDto != null) {
					// MB 09.05.06 Die Artikeldaten muessen (duerfen) nur dann
					// neu angezeigt werden ...
					boolean bRefreshArtikeldaten = false;
					// wenn a) vorher noch kein artikel ausgewaehlt war
					if (getArtikelDto() == null) {
						bRefreshArtikeldaten = true;
					} else {
						// wenn b) ein anderer artikel ausgewaehlt wurde
						if (getArtikelDto().getIId() == null
								|| !getArtikelDto().getIId().equals(
										aDto.getIId())) {
							bRefreshArtikeldaten = true;
						}
					}
					setArtikelDto(aDto);
					if (bRefreshArtikeldaten) {
						artikelDto2components();
					}
				} else {
					wtfArtikel.setText(null);
				}
			}
		}

		wnfEinzelpreis_focusLost(null);
		wnfRabattsatz_focusLost(null);
		// wnfNettopreis_focusLost(null);
		wnfZusatzrabattsatz_focusLost(null);
		jComboBoxMwstsatz_itemStateChanged(null);
	}

	public void focusGained(FocusEvent e) {
	}

	public void focusLost(FocusEvent e) {
	}

	public void setWaehrungCNr(String waehrungCNr) {
		if (wlaZielwaehrung0 != null) {
			wlaZielwaehrung0.setText(waehrungCNr);
		}
		if (wlaZielwaehrung1 != null) {
			wlaZielwaehrung1.setText(waehrungCNr);
		}
		if (wlaZielwaehrung2 != null) {
			wlaZielwaehrung2.setText(waehrungCNr);
		}
		if (wlaZielwaehrung3 != null) {
			wlaZielwaehrung3.setText(waehrungCNr);
		}
		if (wlaZielwaehrungZusatzrabattsumme != null) {
			wlaZielwaehrungZusatzrabattsumme.setText(waehrungCNr);
		}
		if (wlaZielwaehrungMaterialzuschlag != null) {
			wlaZielwaehrungMaterialzuschlag.setText(waehrungCNr);
		}
		if (wlaZielwaehrungBruttopreis != null) {
			wlaZielwaehrungBruttopreis.setText(waehrungCNr);
		}
		if (wlaWaehrungAufschlag1 != null) {
			wlaWaehrungAufschlag1.setText(waehrungCNr);
		}
		if (wlaWaehrungAufschlag2 != null) {
			wlaWaehrungAufschlag2.setText(waehrungCNr);
		}

	}

	public String getWaehrungCNr() {
		return wlaZielwaehrung0.getText();
	}

	public final void setLetzteArtikelCNr() {
		wtfArtikel.setText(this.letzteArtikelNr);
	}

	public final ArtikelDto getArtikelDto() {
		return artikelDto;
	}

	public void setArtikelDto(ArtikelDto artikelDto) throws Throwable {
		this.artikelDto = artikelDto;
		if (artikelDto != null) {
			// als zuletzt verwendeten Artikel merken.
			this.letzteArtikelNr = artikelDto.getCNr();
			// Artikelnummer
			if (this.wtfArtikel != null) {
				this.wtfArtikel.setText(artikelDto.getCNr());
			}
		} else {
			if (this.wtfArtikel != null) {
				this.wtfArtikel.setText(null);
			}
		}
	}

	public WrapperTextField getWtfArtikel() {
		return wtfArtikel;
	}

	/**
	 * Den Enable Zustand der Einheit steueren.
	 * 
	 * @throws java.lang.Throwable
	 *             Ausnahme
	 */
	public void setzeEinheitAenderbar() throws Throwable {
		if (wcoEinheit != null) {
			wcoEinheit.setEnabled(false);
		}
	}

	protected final String getLockMeWer() throws Exception {
		return sLockMeWer;
	}
}

class PanelPositionenPreiseingabe_wnfEinzelpreis_focusAdapter extends
		java.awt.event.FocusAdapter {
	private PanelPositionenPreiseingabe adaptee;

	PanelPositionenPreiseingabe_wnfEinzelpreis_focusAdapter(
			PanelPositionenPreiseingabe adaptee) {
		this.adaptee = adaptee;
	}

	public void focusLost(FocusEvent e) {
		try {
			adaptee.wnfEinzelpreis_focusLost(e);
		} catch (Throwable t) {
			DialogFactory
					.showModalDialog(
							LPMain.getTextRespectUISPr("lp.error"),
							LPMain.getTextRespectUISPr("lp.error.preisberechnungfehlgeschlagen"));
		}
	}
}

class PanelPositionenPreiseingabe_wnfGesamtpreisMitAufschlag_focusAdapter
		extends java.awt.event.FocusAdapter {
	private PanelPositionenPreiseingabe adaptee;

	PanelPositionenPreiseingabe_wnfGesamtpreisMitAufschlag_focusAdapter(
			PanelPositionenPreiseingabe adaptee) {
		this.adaptee = adaptee;
	}

	public void focusLost(FocusEvent e) {
		try {
			adaptee.wnfGesamtpreisMitAufschlag_focusLost(e);
		} catch (Throwable t) {
			DialogFactory
					.showModalDialog(
							LPMain.getTextRespectUISPr("lp.error"),
							LPMain.getTextRespectUISPr("lp.error.preisberechnungfehlgeschlagen"));
		}
	}
}

class PanelPositionenPreiseingabe_wnfAufschlag_focusAdapter extends
		java.awt.event.FocusAdapter {
	private PanelPositionenPreiseingabe adaptee;

	PanelPositionenPreiseingabe_wnfAufschlag_focusAdapter(
			PanelPositionenPreiseingabe adaptee) {
		this.adaptee = adaptee;
	}

	public void focusLost(FocusEvent e) {
		try {
			adaptee.wnfAufschlag_focusLost(e);
		} catch (Throwable t) {
			DialogFactory
					.showModalDialog(
							LPMain.getTextRespectUISPr("lp.error"),
							LPMain.getTextRespectUISPr("lp.error.preisberechnungfehlgeschlagen"));
		}
	}
}

class PanelPositionenPreiseingabe_wnfNettopreis_focusAdapter extends
		java.awt.event.FocusAdapter {
	private PanelPositionenPreiseingabe adaptee;

	PanelPositionenPreiseingabe_wnfNettopreis_focusAdapter(
			PanelPositionenPreiseingabe adaptee) {
		this.adaptee = adaptee;
	}

	public void focusLost(FocusEvent e) {
		try {
			adaptee.wnfNettopreis_focusLost(e);
		} catch (Throwable t) {
			DialogFactory
					.showModalDialog(
							LPMain.getTextRespectUISPr("lp.error"),
							LPMain.getTextRespectUISPr("lp.error.preisberechnungfehlgeschlagen"));
		}
	}
}

class PanelPositionenPreiseingabe_wnfRabattsatz_focusAdapter extends
		java.awt.event.FocusAdapter {
	private PanelPositionenPreiseingabe adaptee;

	PanelPositionenPreiseingabe_wnfRabattsatz_focusAdapter(
			PanelPositionenPreiseingabe adaptee) {
		this.adaptee = adaptee;
	}

	public void focusLost(FocusEvent e) {
		try {
			adaptee.wnfRabattsatz_focusLost(e);
		} catch (Throwable t) {
			DialogFactory
					.showModalDialog(
							LPMain.getTextRespectUISPr("lp.error"),
							LPMain.getTextRespectUISPr("lp.error.preisberechnungfehlgeschlagen"));
		}
	}
}

class PanelPositionenPreiseingabe_wnfZusatzrabattsatz_focusAdapter extends
		java.awt.event.FocusAdapter {
	private PanelPositionenPreiseingabe adaptee;

	PanelPositionenPreiseingabe_wnfZusatzrabattsatz_focusAdapter(
			PanelPositionenPreiseingabe adaptee) {
		this.adaptee = adaptee;
	}

	public void focusLost(FocusEvent e) {
		try {
			adaptee.wnfZusatzrabattsatz_focusLost(e);
		} catch (Throwable t) {
			DialogFactory.showModalDialog(
					LPMain.getTextRespectUISPr("lp.error"),
					LPMain.getTextRespectUISPr("vkpf.error.zusatzrabattsatz"));
			LPMain.getInstance().exitFrame(adaptee.getInternalFrame());
		}
	}
}

class PanelPositionenPreiseingabe_wnfMaterialzuschlag_focusAdapter extends
		java.awt.event.FocusAdapter {
	private PanelPositionenPreiseingabe adaptee;

	PanelPositionenPreiseingabe_wnfMaterialzuschlag_focusAdapter(
			PanelPositionenPreiseingabe adaptee) {
		this.adaptee = adaptee;
	}

	public void focusLost(FocusEvent e) {
		try {
			adaptee.wnfMaterialzuschlag_focusLost(e);
		} catch (Throwable t) {
			DialogFactory.showModalDialog(
					LPMain.getTextRespectUISPr("lp.error"),
					LPMain.getTextRespectUISPr("vkpf.error.zusatzrabattsatz"));
			LPMain.getInstance().exitFrame(adaptee.getInternalFrame());
		}
	}
}

class PanelPositionenPreiseingabe_wnfMenge_focusAdapter extends
		java.awt.event.FocusAdapter {
	private PanelPositionenPreiseingabe adaptee;

	PanelPositionenPreiseingabe_wnfMenge_focusAdapter(
			PanelPositionenPreiseingabe adaptee) {
		this.adaptee = adaptee;
	}

	public void focusLost(FocusEvent e) {
		adaptee.wnfMenge_focusLost(e);
	}
}

class PanelPositionenPreiseingabe_wnfMenge_keyAdapter implements
		java.awt.event.KeyListener {
	private PanelPositionenPreiseingabe adaptee;

	PanelPositionenPreiseingabe_wnfMenge_keyAdapter(
			PanelPositionenPreiseingabe adaptee) {
		this.adaptee = adaptee;
	}

	public void keyTyped(KeyEvent e) {
	}

	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_S && e.isControlDown()) {
			adaptee.wnfMenge_inputVerifier();

		}
	}

	public void keyReleased(KeyEvent e) {
	}
}

class PanelPositionenPreiseingabe_wnfNettopreis_keyAdapter implements
		java.awt.event.KeyListener {
	private PanelPositionenPreiseingabe adaptee;

	PanelPositionenPreiseingabe_wnfNettopreis_keyAdapter(
			PanelPositionenPreiseingabe adaptee) {
		this.adaptee = adaptee;
	}

	public void keyTyped(KeyEvent e) {
	}

	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_S && e.isControlDown()) {
			adaptee.wnfNettopreis_inputVerifier();

		}
	}

	public void keyReleased(KeyEvent e) {
	}
}

class PanelPositionenPreiseingabe_jComboBoxMwstsatz_itemAdapter implements
		java.awt.event.ItemListener {
	private PanelPositionenPreiseingabe adaptee;

	PanelPositionenPreiseingabe_jComboBoxMwstsatz_itemAdapter(
			PanelPositionenPreiseingabe adaptee) {
		this.adaptee = adaptee;
	}

	public void itemStateChanged(ItemEvent e) {
		try {
			adaptee.jComboBoxMwstsatz_itemStateChanged(e);
		} catch (Throwable t) {
			DialogFactory
					.showModalDialog(
							LPMain.getTextRespectUISPr("lp.error"),
							LPMain.getTextRespectUISPr("lp.error.preisberechnungfehlgeschlagen"));
		}
	}
}
