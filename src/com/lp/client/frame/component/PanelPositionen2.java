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
package com.lp.client.frame.component;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;

import com.lp.client.anfrage.PanelPositionenArtikelAnfrage;
import com.lp.client.angebot.InternalFrameAngebot;
import com.lp.client.auftrag.InternalFrameAuftrag;
import com.lp.client.bestellung.PanelPositionenArtikelBestellung;
import com.lp.client.frame.Defaults;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.lieferschein.InternalFrameLieferschein;
import com.lp.client.pc.LPButtonAction;
import com.lp.client.pc.LPMain;
import com.lp.client.rechnung.InternalFrameRechnung;
import com.lp.client.rechnung.PanelPositionenArtikelGutschrift;
import com.lp.client.rechnung.TabbedPaneGutschrift;
import com.lp.client.rechnung.TabbedPaneProformarechnung;
import com.lp.client.rechnung.TabbedPaneRechnung;
import com.lp.server.anfrage.service.AnfragepositionDto;
import com.lp.server.angebotstkl.service.AgstklpositionDto;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikelkommentarDto;
import com.lp.server.artikel.service.ArtikelsprDto;
import com.lp.server.artikel.service.VerkaufspreisDto;
import com.lp.server.lieferschein.service.LieferscheinpositionDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MediaFac;
import com.lp.server.system.service.MwstsatzDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.service.BelegpositionDto;
import com.lp.service.BelegpositionVerkaufDto;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

/**
 * <p>
 * Basisfenster fuer Belegpositionen.
 * </p>
 * <p>
 * Wenn man ein Panel fuer Belegpositionen mit EKPF (= Einkaufspreisfindung)
 * oder VKPF (=Verkaufspreisfindung) braucht, leitet man von PanelPositionen2
 * ab. <br>
 * Allen Belegpositionen gemeinsam sind die optionalen Positionstypen
 * <ul>
 * <li>Texteingabe
 * <li>Betreff
 * <li>Handeingabe
 * <li>Textbaustein
 * <li>Ursprung
 * <li>Leerzeile
 * <li>Seitenumbruch
 * <li>Preiseingabe
 * <li>Zwischensumme
 * <li>Lieferschein
 * <li>Angebotstueckliste
 * <li>Endsumme
 * </ul>
 * <br>
 * Die Komplexitaet liegt im Positionstypen Preiseingabe. Welches Panel und
 * damit welches Layout und welche Funktionalitaet verwendet wird, wird durch
 * den Parameter iTypPanelPreiseingabeI im Konstruktor von PanelPositionen2
 * gesteuert.
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
 * @version $Revision: 1.41 $
 */
public abstract class PanelPositionen2 extends PanelBasis {

	private static final long serialVersionUID = 1L;

	protected JPanel jPanelPositionsarten = null;

	protected WrapperLabel wlaPositionsart = null;
	protected WrapperComboBox wcoPositionsart = null;

	public WrapperComboBox wcoPositiontyp = null;
	public WrapperLabel wlaPositiontyp = null;

	public WrapperComboBox wcoPositionZBez = null;

	public WrapperLabel wlaPositionBez = null;
	public WrapperTextField wtfPositionBez = null;
	public WrapperEditorField wefText = null;

	public WrapperLabel wlaPauschalposition = null;
	public WrapperNumberField wnfPauschalpositionpreis = null;

	// die Panels fuer die optionalen Positionstypen
	protected PanelPositionenTexteingabe panelTexteingabe = null;
	protected PanelPositionenBetreff panelBetreff = null;
	protected PanelPositionenHandeingabe panelHandeingabe = null;
	public PanelPositionenPreiseingabe panelArtikel = null;
	protected PanelPositionenTextbaustein panelTextbaustein = null;
	protected PanelPositionenLeerzeile panelLeerzeile = null;
	protected PanelPositionenSeitenumbruch panelSeitenumbruch = null;
	protected PanelPositionenUrsprung panelUrsprung = null;
	protected PanelPositionenZwischensumme panelZwischensumme = null;
	protected PanelPositionenLieferschein panelLieferschein = null;
	protected PanelPositionenAngebotstueckliste panelAGStueckliste = null;
	protected PanelPositionenEndsumme panelEndsumme = null;
	protected PanelPositionenIntelligenteZwischensumme panelIntZwischensumme = null;

	public static final int TYP_PANELPREISEINGABE_ARTIKELEINKAUF = 0;
	public static final int TYP_PANELPREISEINGABE_ARTIKELVERKAUF = 1;
	public static final int TYP_PANELPREISEINGABE_ARTIKELVERKAUFSNR = 2;
	public static final int TYP_PANELPREISEINGABE_ARTIKELANFRAGE = 3;
	public static final int TYP_PANELPREISEINGABE_ARTIKELBESTELLUNG = 4;
	public static final int TYP_PANELPREISEINGABE_ARTIKELGUTSCHRIFT = 5;
	public static final int TYP_PANELPREISEINGABE_ARTIKELEINGABESNR = 6;

	/** Welchen Typ hat das Panel fuer die Preiseingabe. */
	private int iTypPanelPreiseingabe = -1;

	/** Die Breite der ersten Spalte im Panel kann ueberschrieben werden. */
	protected int iSpaltenbreiteArtikelMitGoto = 100;

	/**
	 * poseinfuegen: 0 Soll eine neue Position vor der aktuell selektierten
	 * eingefuegt werden.
	 */
	protected boolean bFuegeNeuePositionVorDerSelektiertenEin = false;
	// protected Integer artikelSetIIdFuerNeuePosition = null ;

	public Timestamp tBelegdatumMwstsatz;

	protected IntelligenteZwischensummeController zwController;

	private ArtikelsetViewController artikelSetViewController;

	/**
	 * Konstruktor.
	 * 
	 * @param internalFrame
	 *            der InternalFrame auf dem das Panel sitzt
	 * @param add2TitleI
	 *            der default Titel des Panels
	 * @param key
	 *            PK der Position
	 * @param iTypPanelPreiseingabeI
	 *            handelt es sich um eine Belegart mit Einkaufspreisfindung
	 * @throws Throwable
	 */
	public PanelPositionen2(InternalFrame internalFrame, String add2TitleI,
			Object key, int iTypPanelPreiseingabeI) throws Throwable {
		super(internalFrame, add2TitleI, key);

		iTypPanelPreiseingabe = iTypPanelPreiseingabeI;
		zwController = new IntelligenteZwischensummeController();
		artikelSetViewController = new ArtikelsetViewController();

		jbInit();
		initComponents();
	}

	public PanelPositionenPreiseingabe getPanelArtikel() {
		return this.panelArtikel;
	}

	public void setArtikeSetIIdForNewPosition(Integer artikelSetIId) {
		artikelSetViewController
				.setArtikelSetIIdFuerNeuePosition(artikelSetIId);
	}

	public void resetArtikelsetIIdForNewPosition() {
		artikelSetViewController.resetArtikelSetIIdFuerNeuePosition();
	}

	public ArtikelsetViewController getArtikelsetViewController() {
		return artikelSetViewController;
	}

	/**
	 * VF 15.05.2006 IMS 2054 In Spalte 5 der Statusbar wird der neben dem Lager
	 * auch der Lagerstand angezeigt, wenn dafuer genug Information vorhanden
	 * ist.
	 * 
	 * @return String
	 * @param iIdLager
	 *            Integer
	 * @throws Throwable
	 */
	protected String getLagerstandFuerStatusbarSpalte5(Integer iIdLager)
			throws Throwable {
		String sLagerinfoO = null;
		sLagerinfoO = DelegateFactory.getInstance().getLagerDelegate()
				.lagerFindByPrimaryKey(iIdLager).getCNr();

		if (Helper.short2boolean(panelArtikel.getArtikelDto()
				.getBLagerbewirtschaftet())) {
			if (panelArtikel.getArtikelDto() != null
					&& panelArtikel.getArtikelDto().getIId() != null) {
				BigDecimal ddMenge = DelegateFactory
						.getInstance()
						.getLagerDelegate()
						.getLagerstand(panelArtikel.getArtikelDto().getIId(),
								iIdLager);

				sLagerinfoO += ": ";
				sLagerinfoO += ddMenge;
			}
		}

		return sLagerinfoO;
	}

	private void jbInit() throws Throwable {
		// das Aussenpanel hat immer das Gridbaglayout und einen Rahmen nach
		// innen
		setLayout(new GridBagLayout());
		setBorder(BorderFactory.createEmptyBorder(3, 3, 10, 3));

		// Zeile - die Toolbar
		add(getToolsPanel(), new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
				new Insets(0, 0, 0, 0), 0, 0));

		// zusaetzliche Buttons setzen
		String[] aWhichButtonIUse = { ACTION_UPDATE, ACTION_SAVE,
				ACTION_DELETE, ACTION_DISCARD };
		enableToolsPanelButtons(aWhichButtonIUse);

		wefText = new WrapperEditorFieldKommentar(getInternalFrame(),
				LPMain.getTextRespectUISPr("label.text"));

		// Zeile - Panel mit den Positionsarten
		iZeile++;
		createPanelPositionsarten();
		add(jPanelPositionsarten, new GridBagConstraints(0, iZeile, 1, 1, 1.0,
				0.0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
				new Insets(0, 0, 0, 0), 0, 0));

		// Zeile
		iZeile++;
		panelTexteingabe = new PanelPositionenTexteingabe(getInternalFrame(),
				LocaleFac.POSITIONSART_TEXTEINGABE, getKeyWhenDetailPanel());

		add(panelTexteingabe, new GridBagConstraints(0, iZeile, 1, 1, 1.0, 1.0,
				GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(
						0, 0, 0, 0), 0, 0));

		// Zeile
		iZeile++;
		panelBetreff = new PanelPositionenBetreff(getInternalFrame(),
				iSpaltenbreiteArtikelMitGoto);
		add(panelBetreff, new GridBagConstraints(0, iZeile, 1, 1, 1.0, 1.0,
				GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
				new Insets(0, 0, 0, 0), 0, 0));

		// Zeile
		iZeile++;

		if (iTypPanelPreiseingabe == TYP_PANELPREISEINGABE_ARTIKELEINKAUF) {
			panelHandeingabe = new PanelPositionenHandeingabe(
					getInternalFrame(), LocaleFac.POSITIONSART_HANDEINGABE,
					getKeyWhenDetailPanel(), this.getLockMeWer(),
					iSpaltenbreiteArtikelMitGoto,
					getInternalFrame().bRechtDarfPreiseSehenEinkauf,
					getInternalFrame().bRechtDarfPreiseAendernEinkauf);
			panelArtikel = new PanelPositionenArtikelEinkauf(
					getInternalFrame(), "$Artikelposition$",
					getKeyWhenDetailPanel(), this.getLockMeWer(),
					iSpaltenbreiteArtikelMitGoto);
		} else if (iTypPanelPreiseingabe == TYP_PANELPREISEINGABE_ARTIKELVERKAUF) {
			panelHandeingabe = new PanelPositionenHandeingabe(
					getInternalFrame(), LocaleFac.POSITIONSART_HANDEINGABE,
					getKeyWhenDetailPanel(), this.getLockMeWer(),
					iSpaltenbreiteArtikelMitGoto,
					getInternalFrame().bRechtDarfPreiseSehenVerkauf,
					getInternalFrame().bRechtDarfPreiseAendernVerkauf);
			panelArtikel = new PanelPositionenArtikelVerkauf(
					getInternalFrame(), "$Artikelposition$",
					getKeyWhenDetailPanel(), this.getLockMeWer(),
					iSpaltenbreiteArtikelMitGoto);
		} else if (iTypPanelPreiseingabe == TYP_PANELPREISEINGABE_ARTIKELEINGABESNR) {
			panelHandeingabe = new PanelPositionenHandeingabe(
					getInternalFrame(), LocaleFac.POSITIONSART_HANDEINGABE,
					getKeyWhenDetailPanel(), this.getLockMeWer(),
					iSpaltenbreiteArtikelMitGoto,
					getInternalFrame().bRechtDarfPreiseSehenVerkauf,
					getInternalFrame().bRechtDarfPreiseAendernVerkauf);
			panelArtikel = new PanelPositionenArtikelEingabeSNR(
					getInternalFrame(), "$Artikelposition$",
					getKeyWhenDetailPanel(), this.getLockMeWer(),
					iSpaltenbreiteArtikelMitGoto);
		} else if (iTypPanelPreiseingabe == TYP_PANELPREISEINGABE_ARTIKELVERKAUFSNR) {
			panelHandeingabe = new PanelPositionenHandeingabe(
					getInternalFrame(), LocaleFac.POSITIONSART_HANDEINGABE,
					getKeyWhenDetailPanel(), this.getLockMeWer(),
					iSpaltenbreiteArtikelMitGoto,
					getInternalFrame().bRechtDarfPreiseSehenVerkauf,
					getInternalFrame().bRechtDarfPreiseAendernVerkauf);
			panelArtikel = new PanelPositionenArtikelVerkaufSNR(
					getInternalFrame(), "$Artikelposition$",
					getKeyWhenDetailPanel(), this.getLockMeWer(),
					iSpaltenbreiteArtikelMitGoto);
		} else if (iTypPanelPreiseingabe == TYP_PANELPREISEINGABE_ARTIKELANFRAGE) {
			panelHandeingabe = new PanelPositionenHandeingabe(
					getInternalFrame(), LocaleFac.POSITIONSART_HANDEINGABE,
					getKeyWhenDetailPanel(), this.getLockMeWer(),
					iSpaltenbreiteArtikelMitGoto,
					getInternalFrame().bRechtDarfPreiseSehenEinkauf,
					getInternalFrame().bRechtDarfPreiseAendernEinkauf);
			panelArtikel = new PanelPositionenArtikelAnfrage(
					getInternalFrame(), "$Artikelposition$",
					getKeyWhenDetailPanel(), this.getLockMeWer(),
					iSpaltenbreiteArtikelMitGoto);
		} else if (iTypPanelPreiseingabe == TYP_PANELPREISEINGABE_ARTIKELBESTELLUNG) {
			panelHandeingabe = new PanelPositionenHandeingabe(
					getInternalFrame(), LocaleFac.POSITIONSART_HANDEINGABE,
					getKeyWhenDetailPanel(), this.getLockMeWer(),
					iSpaltenbreiteArtikelMitGoto,
					getInternalFrame().bRechtDarfPreiseSehenEinkauf,
					getInternalFrame().bRechtDarfPreiseAendernEinkauf);
			panelArtikel = new PanelPositionenArtikelBestellung(
					getInternalFrame(), "$Artikelposition$",
					getKeyWhenDetailPanel(), this.getLockMeWer(),
					iSpaltenbreiteArtikelMitGoto);
		} else if (iTypPanelPreiseingabe == TYP_PANELPREISEINGABE_ARTIKELGUTSCHRIFT) {
			panelHandeingabe = new PanelPositionenHandeingabe(
					getInternalFrame(), LocaleFac.POSITIONSART_HANDEINGABE,
					getKeyWhenDetailPanel(), this.getLockMeWer(),
					iSpaltenbreiteArtikelMitGoto,
					getInternalFrame().bRechtDarfPreiseSehenVerkauf,
					getInternalFrame().bRechtDarfPreiseAendernVerkauf);
			panelArtikel = new PanelPositionenArtikelGutschrift(
					getInternalFrame(), "$Artikelposition$",
					getKeyWhenDetailPanel(), this.getLockMeWer(),
					iSpaltenbreiteArtikelMitGoto);
		}

		add(panelHandeingabe, new GridBagConstraints(0, iZeile, 1, 1, 1.0, 1.0,
				GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
				new Insets(0, 0, 0, 0), 0, 0));
		// Zeile
		iZeile++;
		add(panelArtikel, new GridBagConstraints(0, iZeile, 1, 1, 1.0, 1.0,
				GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
				new Insets(0, 0, 0, 0), 0, 0));

		// Zeile
		iZeile++;
		panelTextbaustein = new PanelPositionenTextbaustein(getInternalFrame(),
				"$Textbaustein$", getKeyWhenDetailPanel(),
				iSpaltenbreiteArtikelMitGoto);
		add(panelTextbaustein, new GridBagConstraints(0, iZeile, 1, 1, 1.0,
				1.0, GridBagConstraints.NORTH, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
		// Zeile
		iZeile++;
		panelLeerzeile = new PanelPositionenLeerzeile(getInternalFrame());
		add(panelLeerzeile, new GridBagConstraints(0, iZeile, 1, 1, 1.0, 1.0,
				GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
				new Insets(0, 0, 0, 0), 0, 0));

		// Zeile
		iZeile++;
		panelSeitenumbruch = new PanelPositionenSeitenumbruch(
				getInternalFrame());
		add(panelSeitenumbruch, new GridBagConstraints(0, iZeile, 1, 1, 1.0,
				1.0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
				new Insets(0, 0, 0, 0), 0, 0));

		// Zeile
		iZeile++;
		panelUrsprung = new PanelPositionenUrsprung(getInternalFrame(),
				iSpaltenbreiteArtikelMitGoto);
		add(panelUrsprung, new GridBagConstraints(0, iZeile, 1, 1, 1.0, 1.0,
				GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
				new Insets(0, 0, 0, 0), 0, 0));
		// Zeile
		iZeile++;
		panelZwischensumme = new PanelPositionenZwischensumme(
				getInternalFrame());
		add(panelZwischensumme, new GridBagConstraints(0, iZeile, 1, 1, 1.0,
				1.0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
				new Insets(0, 0, 0, 0), 0, 0));

		// Zeile
		iZeile++;
		panelLieferschein = new PanelPositionenLieferschein(getInternalFrame(),
				iSpaltenbreiteArtikelMitGoto);

		add(panelLieferschein, new GridBagConstraints(0, iZeile, 1, 1, 1.0,
				1.0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
				new Insets(0, 0, 0, 0), 0, 0));

		// Zeile
		iZeile++;
		panelAGStueckliste = new PanelPositionenAngebotstueckliste(
				getInternalFrame(), "$Angebotstueckliste$",
				getKeyWhenDetailPanel(), this.getLockMeWer(),
				iSpaltenbreiteArtikelMitGoto);

		add(panelAGStueckliste, new GridBagConstraints(0, iZeile, 1, 1, 1.0,
				1.0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
				new Insets(0, 0, 0, 0), 0, 0));

		iZeile++;
		panelEndsumme = new PanelPositionenEndsumme(getInternalFrame());
		add(panelEndsumme, new GridBagConstraints(0, iZeile, 1, 1, 1.0, 1.0,
				GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
				new Insets(0, 0, 0, 0), 0, 0));

		iZeile++;
		panelIntZwischensumme = new PanelPositionenIntelligenteZwischensumme(
				getInternalFrame(), "$IntelligenteZwischensumme$",
				getKeyWhenDetailPanel(), zwController);
		add(panelIntZwischensumme, new GridBagConstraints(0, iZeile, 1, 1, 1.0,
				1.0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
				new Insets(0, 0, 0, 0), 0, 0));
	}

	protected void setDefaults() throws Throwable {
		panelTexteingabe.setDefaults();
		panelHandeingabe.setDefaults();
		panelArtikel.setDefaults();
		panelArtikel.setArtikelEingabefelderEditable(false);
		panelTextbaustein.setDefaults();
		panelAGStueckliste.setDefaults();
		panelIntZwischensumme.setDefaults();
		wefText.getLpEditor().setText(null);
	}

	/**
	 * Auswahl der Positionsart ist erfolgt.
	 */
	protected final void jComboBoxPositionsart_actionPerformed() {
		String currentArt = (String) wcoPositionsart.getKeyOfSelectedItem();
		LPButtonAction item = (LPButtonAction) getHmOfButtons().get(
				PanelBasis.ACTION_TEXT);
		/**
		 * @todo MB->MB Code unuebersichtlich und schlecht wartbar -> was tun
		 */

		if (currentArt.equals(LocaleFac.POSITIONSART_BETRIFFT)) {
			panelTexteingabe.setVisible(false);

			panelBetreff.setVisible(true);

			panelHandeingabe.setVisible(false);
			panelHandeingabe.wnfMenge.setMandatoryField(false);
			panelHandeingabe.wnfEinzelpreis.setMandatoryField(false);
			panelHandeingabe.getWnfRabattsatz().setMandatoryField(false);
			panelHandeingabe.getWnfZusatzrabattsatz().setMandatoryField(false);
			panelHandeingabe.wnfNettopreis.setMandatoryField(false);
			panelHandeingabe.wtfBezeichnung.setMandatoryField(false);

			panelArtikel.setVisible(false);
			panelArtikel.wtfArtikel.setMandatoryField(false);
			panelArtikel.wnfMenge.setMandatoryField(false);
			panelArtikel.wnfEinzelpreis.setMandatoryField(false);

			if (panelArtikel.getWnfRabattsatz() != null) {
				panelArtikel.getWnfRabattsatz().setMandatoryField(false);
			}

			if (panelArtikel.getWnfZusatzrabattsatz() != null) {
				panelArtikel.getWnfZusatzrabattsatz().setMandatoryField(false);
			}

			if (panelArtikel.wnfNettopreis != null) {
				panelArtikel.wnfNettopreis.setMandatoryField(false);
			}

			// IMS 1389 @todo UW->JO geht das anders als mit instanceof PJ 5028
			if (panelArtikel instanceof PanelPositionenArtikelVerkaufSNR) {
				((PanelPositionenArtikelVerkaufSNR) panelArtikel).wtfSerienchargennummer
						.setMandatoryField(false);
			} else if (panelArtikel instanceof PanelPositionenArtikelGutschrift) {
				((PanelPositionenArtikelGutschrift) panelArtikel).wtfSerienchargennummer
						.setMandatoryField(false);
			}

			panelTextbaustein.setVisible(false);
			panelTextbaustein.wtfTextmodul.setMandatoryField(false);

			panelLeerzeile.setVisible(false);
			panelEndsumme.setVisible(false);

			panelSeitenumbruch.setVisible(false);

			panelUrsprung.setVisible(false);

			panelZwischensumme.setVisible(false);
			panelIntZwischensumme.setVisible(false);

			panelLieferschein.setVisible(false);

			panelAGStueckliste.setVisible(false);
			panelAGStueckliste.wnfMenge.setMandatoryField(false);
			panelAGStueckliste.wnfEinzelpreis.setMandatoryField(false);
			panelAGStueckliste.getWnfRabattsatz().setMandatoryField(false);
			panelAGStueckliste.getWnfZusatzrabattsatz()
					.setMandatoryField(false);
			panelAGStueckliste.wnfNettopreis.setMandatoryField(false);
			panelAGStueckliste.wtfAgstkl.setMandatoryField(false);

			wlaPositiontyp.setVisible(false);
			wcoPositiontyp.setVisible(false);
			wlaPositionBez.setVisible(false);
			wtfPositionBez.setVisible(false);
			wcoPositionZBez.setVisible(false);
			wlaPauschalposition.setVisible(false);
			wnfPauschalpositionpreis.setVisible(false);
			item.getButton().setVisible(false);
		} else if (currentArt.equals(LocaleFac.POSITIONSART_TEXTEINGABE)) {
			panelTexteingabe.setVisible(true);
			panelTexteingabe.requestFocus();

			panelBetreff.setVisible(false);

			panelHandeingabe.setVisible(false);
			panelHandeingabe.wnfMenge.setMandatoryField(false);
			panelHandeingabe.wnfEinzelpreis.setMandatoryField(false);
			panelHandeingabe.getWnfRabattsatz().setMandatoryField(false);
			panelHandeingabe.getWnfZusatzrabattsatz().setMandatoryField(false);
			panelHandeingabe.wnfNettopreis.setMandatoryField(false);
			panelHandeingabe.wtfBezeichnung.setMandatoryField(false);

			panelArtikel.setVisible(false);
			panelArtikel.wtfArtikel.setMandatoryField(false);
			panelArtikel.wnfMenge.setMandatoryField(false);
			panelArtikel.wnfEinzelpreis.setMandatoryField(false);

			if (panelArtikel.getWnfRabattsatz() != null) {
				panelArtikel.getWnfRabattsatz().setMandatoryField(false);
			}

			if (panelArtikel.getWnfZusatzrabattsatz() != null) {
				panelArtikel.getWnfZusatzrabattsatz().setMandatoryField(false);
			}

			if (panelArtikel.wnfNettopreis != null) {
				panelArtikel.wnfNettopreis.setMandatoryField(false);
			}

			if (panelArtikel instanceof PanelPositionenArtikelVerkaufSNR) {
				((PanelPositionenArtikelVerkaufSNR) panelArtikel).wtfSerienchargennummer
						.setMandatoryField(false);
			} else if (panelArtikel instanceof PanelPositionenArtikelGutschrift) {
				((PanelPositionenArtikelGutschrift) panelArtikel).wtfSerienchargennummer
						.setMandatoryField(false);
			}

			panelTextbaustein.setVisible(false);
			panelTextbaustein.wtfTextmodul.setMandatoryField(false);

			panelLeerzeile.setVisible(false);
			panelEndsumme.setVisible(false);

			panelSeitenumbruch.setVisible(false);

			panelUrsprung.setVisible(false);

			panelZwischensumme.setVisible(false);
			panelIntZwischensumme.setVisible(false);

			panelLieferschein.setVisible(false);

			panelAGStueckliste.setVisible(false);
			panelAGStueckliste.wnfMenge.setMandatoryField(false);
			panelAGStueckliste.wnfEinzelpreis.setMandatoryField(false);
			panelAGStueckliste.getWnfRabattsatz().setMandatoryField(false);
			panelAGStueckliste.getWnfZusatzrabattsatz()
					.setMandatoryField(false);
			panelAGStueckliste.wnfNettopreis.setMandatoryField(false);
			panelAGStueckliste.wtfAgstkl.setMandatoryField(false);

			wlaPositiontyp.setVisible(false);
			wcoPositiontyp.setVisible(false);
			wlaPositionBez.setVisible(false);
			wtfPositionBez.setVisible(false);
			wcoPositionZBez.setVisible(false);
			wlaPauschalposition.setVisible(false);
			wnfPauschalpositionpreis.setVisible(false);
			item.getButton().setVisible(false);
		} else if (currentArt.equals(LocaleFac.POSITIONSART_HANDEINGABE)) {
			panelTexteingabe.setVisible(false);

			panelBetreff.setVisible(false);

			panelHandeingabe.setVisible(true);
			panelHandeingabe.wnfMenge.setMandatoryField(true);
			panelHandeingabe.wnfEinzelpreis.setMandatoryField(true);
			panelHandeingabe.getWnfRabattsatz().setMandatoryField(true);
			panelHandeingabe.wnfRabattsumme.getWrbFixNumber().setSelected(true);
			// UW 27.04.06 Hotifx
			try {
				if (panelHandeingabe.getWnfZusatzrabattsatz().getBigDecimal() != null) {
					panelHandeingabe.getWnfZusatzrabattsatz()
							.setMandatoryField(true);
				}
			} catch (ExceptionLP ex) {
				LPMain.getInstance().exitFrame(getInternalFrame());
			}
			panelHandeingabe.wnfNettopreis.setMandatoryField(true);
			panelHandeingabe.wtfBezeichnung.setMandatoryField(true);

			panelArtikel.setVisible(false);
			panelArtikel.wtfArtikel.setMandatoryField(false);
			panelArtikel.wnfMenge.setMandatoryField(false);
			panelArtikel.wnfEinzelpreis.setMandatoryField(false);

			// CK->UW
			if (panelHandeingabe.wnfGestpreis.isVisible()) {
				panelHandeingabe.wnfGestpreis.setMandatoryField(true);
				panelArtikel.wnfGestpreis.setMandatoryField(false);
			}
			if (panelArtikel.getWnfRabattsatz() != null) {
				panelArtikel.getWnfRabattsatz().setMandatoryField(false);
			}

			if (panelArtikel.getWnfZusatzrabattsatz() != null) {
				panelArtikel.getWnfZusatzrabattsatz().setMandatoryField(false);
			}

			if (panelArtikel.wnfNettopreis != null) {
				panelArtikel.wnfNettopreis.setMandatoryField(false);
			}

			if (panelArtikel instanceof PanelPositionenArtikelVerkaufSNR) {
				((PanelPositionenArtikelVerkaufSNR) panelArtikel).wtfSerienchargennummer
						.setMandatoryField(false);
			} else if (panelArtikel instanceof PanelPositionenArtikelGutschrift) {
				((PanelPositionenArtikelGutschrift) panelArtikel).wtfSerienchargennummer
						.setMandatoryField(false);
			}

			panelTextbaustein.setVisible(false);
			panelTextbaustein.wtfTextmodul.setMandatoryField(false);

			panelLeerzeile.setVisible(false);
			panelEndsumme.setVisible(false);

			panelSeitenumbruch.setVisible(false);

			panelUrsprung.setVisible(false);

			panelZwischensumme.setVisible(false);
			panelIntZwischensumme.setVisible(false);

			panelLieferschein.setVisible(false);

			panelAGStueckliste.setVisible(false);
			panelAGStueckliste.wnfMenge.setMandatoryField(false);
			panelAGStueckliste.wnfEinzelpreis.setMandatoryField(false);
			panelAGStueckliste.getWnfRabattsatz().setMandatoryField(false);
			panelAGStueckliste.getWnfZusatzrabattsatz()
					.setMandatoryField(false);
			panelAGStueckliste.wnfNettopreis.setMandatoryField(false);
			panelAGStueckliste.wtfAgstkl.setMandatoryField(false);

			wlaPositiontyp.setVisible(false);
			wcoPositiontyp.setVisible(false);
			wlaPositionBez.setVisible(false);
			wtfPositionBez.setVisible(false);
			wcoPositionZBez.setVisible(false);
			wlaPauschalposition.setVisible(false);
			wnfPauschalpositionpreis.setVisible(false);
			item.getButton().setVisible(false);
		} else if (currentArt.equals(LocaleFac.POSITIONSART_IDENT)) {
			panelTexteingabe.setVisible(false);

			panelBetreff.setVisible(false);

			panelHandeingabe.setVisible(false);
			panelHandeingabe.wnfMenge.setMandatoryField(false);
			panelHandeingabe.wnfEinzelpreis.setMandatoryField(false);
			panelHandeingabe.getWnfRabattsatz().setMandatoryField(false);
			panelHandeingabe.getWnfZusatzrabattsatz().setMandatoryField(false);
			panelHandeingabe.wnfNettopreis.setMandatoryField(false);
			panelHandeingabe.wtfBezeichnung.setMandatoryField(false);

			panelArtikel.setVisible(true);
			panelArtikel.wtfArtikel.setMandatoryField(true);
			panelArtikel.wnfMenge.setMandatoryField(true);
			panelArtikel.wnfEinzelpreis.setMandatoryField(true);
			panelArtikel.wnfRabattsumme.getWrbFixNumber().setSelected(true);
			// CK->UW
			if (panelHandeingabe.wnfGestpreis.isVisible()) {
				panelArtikel.wnfGestpreis.setMandatoryField(true);
				panelHandeingabe.wnfGestpreis.setMandatoryField(false);
			}

			if (panelArtikel.getWnfRabattsatz() != null) {
				panelArtikel.getWnfRabattsatz().setMandatoryField(true);
			}

			if (panelArtikel.getWnfZusatzrabattsatz() != null) {
				panelArtikel.getWnfZusatzrabattsatz().setMandatoryField(true);
			}

			if (panelArtikel.wnfNettopreis != null) {
				panelArtikel.wnfNettopreis.setMandatoryField(true);
			}

			panelTextbaustein.setVisible(false);
			panelTextbaustein.wtfTextmodul.setMandatoryField(false);

			panelLeerzeile.setVisible(false);
			panelEndsumme.setVisible(false);

			panelSeitenumbruch.setVisible(false);

			panelUrsprung.setVisible(false);

			panelZwischensumme.setVisible(false);
			panelIntZwischensumme.setVisible(false);

			panelLieferschein.setVisible(false);

			panelAGStueckliste.setVisible(false);
			panelAGStueckliste.wnfMenge.setMandatoryField(false);
			panelAGStueckliste.wnfEinzelpreis.setMandatoryField(false);
			panelAGStueckliste.getWnfRabattsatz().setMandatoryField(false);
			panelAGStueckliste.getWnfZusatzrabattsatz()
					.setMandatoryField(false);
			panelAGStueckliste.wnfNettopreis.setMandatoryField(false);
			panelAGStueckliste.wtfAgstkl.setMandatoryField(false);

			wlaPositiontyp.setVisible(false);
			wcoPositiontyp.setVisible(false);
			wlaPositionBez.setVisible(false);
			wtfPositionBez.setVisible(false);
			wcoPositionZBez.setVisible(false);
			wlaPauschalposition.setVisible(false);
			wnfPauschalpositionpreis.setVisible(false);
			item.getButton().setVisible(true);
		} else if (currentArt.equals(LocaleFac.POSITIONSART_LEERZEILE)) {
			panelTexteingabe.setVisible(false);

			panelBetreff.setVisible(false);

			panelHandeingabe.setVisible(false);
			panelHandeingabe.wnfMenge.setMandatoryField(false);
			panelHandeingabe.wnfEinzelpreis.setMandatoryField(false);
			panelHandeingabe.getWnfRabattsatz().setMandatoryField(false);
			panelHandeingabe.getWnfZusatzrabattsatz().setMandatoryField(false);
			panelHandeingabe.wnfNettopreis.setMandatoryField(false);
			panelHandeingabe.wtfBezeichnung.setMandatoryField(false);

			panelArtikel.setVisible(false);
			panelArtikel.wtfArtikel.setMandatoryField(false);
			panelArtikel.wnfMenge.setMandatoryField(false);
			panelArtikel.wnfEinzelpreis.setMandatoryField(false);

			if (panelArtikel.getWnfRabattsatz() != null) {
				panelArtikel.getWnfRabattsatz().setMandatoryField(false);
			}

			if (panelArtikel.getWnfZusatzrabattsatz() != null) {
				panelArtikel.getWnfZusatzrabattsatz().setMandatoryField(false);
			}

			if (panelArtikel.wnfNettopreis != null) {
				panelArtikel.wnfNettopreis.setMandatoryField(false);
			}

			if (panelArtikel instanceof PanelPositionenArtikelVerkaufSNR) {
				((PanelPositionenArtikelVerkaufSNR) panelArtikel).wtfSerienchargennummer
						.setMandatoryField(false);
			} else if (panelArtikel instanceof PanelPositionenArtikelGutschrift) {
				((PanelPositionenArtikelGutschrift) panelArtikel).wtfSerienchargennummer
						.setMandatoryField(false);
			}

			panelTextbaustein.setVisible(false);
			panelTextbaustein.wtfTextmodul.setMandatoryField(false);

			panelLeerzeile.setVisible(true);

			panelSeitenumbruch.setVisible(false);

			panelUrsprung.setVisible(false);

			panelZwischensumme.setVisible(false);
			panelIntZwischensumme.setVisible(false);

			panelLieferschein.setVisible(false);

			panelAGStueckliste.setVisible(false);
			panelAGStueckliste.wnfMenge.setMandatoryField(false);
			panelAGStueckliste.wnfEinzelpreis.setMandatoryField(false);
			panelAGStueckliste.getWnfRabattsatz().setMandatoryField(false);
			panelAGStueckliste.getWnfZusatzrabattsatz()
					.setMandatoryField(false);
			panelAGStueckliste.wnfNettopreis.setMandatoryField(false);
			panelAGStueckliste.wtfAgstkl.setMandatoryField(false);

			panelEndsumme.setVisible(false);

			wlaPositiontyp.setVisible(false);
			wcoPositiontyp.setVisible(false);
			wlaPositionBez.setVisible(false);
			wtfPositionBez.setVisible(false);
			wcoPositionZBez.setVisible(false);
			wlaPauschalposition.setVisible(false);
			wnfPauschalpositionpreis.setVisible(false);
			item.getButton().setVisible(false);
		} else if (currentArt.equals(LocaleFac.POSITIONSART_ENDSUMME)) {
			panelTexteingabe.setVisible(false);

			panelBetreff.setVisible(false);

			panelHandeingabe.setVisible(false);
			panelHandeingabe.wnfMenge.setMandatoryField(false);
			panelHandeingabe.wnfEinzelpreis.setMandatoryField(false);
			panelHandeingabe.getWnfRabattsatz().setMandatoryField(false);
			panelHandeingabe.getWnfZusatzrabattsatz().setMandatoryField(false);
			panelHandeingabe.wnfNettopreis.setMandatoryField(false);
			panelHandeingabe.wtfBezeichnung.setMandatoryField(false);

			panelArtikel.setVisible(false);
			panelArtikel.wtfArtikel.setMandatoryField(false);
			panelArtikel.wnfMenge.setMandatoryField(false);
			panelArtikel.wnfEinzelpreis.setMandatoryField(false);

			if (panelArtikel.getWnfRabattsatz() != null) {
				panelArtikel.getWnfRabattsatz().setMandatoryField(false);
			}

			if (panelArtikel.getWnfZusatzrabattsatz() != null) {
				panelArtikel.getWnfZusatzrabattsatz().setMandatoryField(false);
			}

			if (panelArtikel.wnfNettopreis != null) {
				panelArtikel.wnfNettopreis.setMandatoryField(false);
			}

			if (panelArtikel instanceof PanelPositionenArtikelVerkaufSNR) {
				((PanelPositionenArtikelVerkaufSNR) panelArtikel).wtfSerienchargennummer
						.setMandatoryField(false);
			} else if (panelArtikel instanceof PanelPositionenArtikelGutschrift) {
				((PanelPositionenArtikelGutschrift) panelArtikel).wtfSerienchargennummer
						.setMandatoryField(false);
			}

			panelTextbaustein.setVisible(false);
			panelTextbaustein.wtfTextmodul.setMandatoryField(false);

			panelLeerzeile.setVisible(false);

			panelSeitenumbruch.setVisible(false);

			panelUrsprung.setVisible(false);

			panelZwischensumme.setVisible(false);
			panelIntZwischensumme.setVisible(false);

			panelLieferschein.setVisible(false);

			panelAGStueckliste.setVisible(false);
			panelAGStueckliste.wnfMenge.setMandatoryField(false);
			panelAGStueckliste.wnfEinzelpreis.setMandatoryField(false);
			panelAGStueckliste.getWnfRabattsatz().setMandatoryField(false);
			panelAGStueckliste.getWnfZusatzrabattsatz()
					.setMandatoryField(false);
			panelAGStueckliste.wnfNettopreis.setMandatoryField(false);
			panelAGStueckliste.wtfAgstkl.setMandatoryField(false);

			panelEndsumme.setVisible(true);

			wlaPositiontyp.setVisible(false);
			wcoPositiontyp.setVisible(false);
			wlaPositionBez.setVisible(false);
			wtfPositionBez.setVisible(false);
			wcoPositionZBez.setVisible(false);
			wlaPauschalposition.setVisible(false);
			wnfPauschalpositionpreis.setVisible(false);
			item.getButton().setVisible(false);
		} else if (currentArt.equals(LocaleFac.POSITIONSART_SEITENUMBRUCH)) {
			panelTexteingabe.setVisible(false);

			panelBetreff.setVisible(false);

			panelHandeingabe.setVisible(false);
			panelHandeingabe.wnfMenge.setMandatoryField(false);
			panelHandeingabe.wnfEinzelpreis.setMandatoryField(false);
			panelHandeingabe.getWnfRabattsatz().setMandatoryField(false);
			panelHandeingabe.getWnfZusatzrabattsatz().setMandatoryField(false);
			panelHandeingabe.wnfNettopreis.setMandatoryField(false);
			panelHandeingabe.wtfBezeichnung.setMandatoryField(false);

			panelArtikel.setVisible(false);
			panelArtikel.wtfArtikel.setMandatoryField(false);
			panelArtikel.wnfMenge.setMandatoryField(false);
			panelArtikel.wnfEinzelpreis.setMandatoryField(false);

			if (panelArtikel.getWnfRabattsatz() != null) {
				panelArtikel.getWnfRabattsatz().setMandatoryField(false);
			}

			if (panelArtikel.getWnfZusatzrabattsatz() != null) {
				panelArtikel.getWnfZusatzrabattsatz().setMandatoryField(false);
			}

			if (panelArtikel.wnfNettopreis != null) {
				panelArtikel.wnfNettopreis.setMandatoryField(false);
			}

			if (panelArtikel instanceof PanelPositionenArtikelVerkaufSNR) {
				((PanelPositionenArtikelVerkaufSNR) panelArtikel).wtfSerienchargennummer
						.setMandatoryField(false);
			} else if (panelArtikel instanceof PanelPositionenArtikelGutschrift) {
				((PanelPositionenArtikelGutschrift) panelArtikel).wtfSerienchargennummer
						.setMandatoryField(false);
			}

			panelTextbaustein.setVisible(false);
			panelTextbaustein.wtfTextmodul.setMandatoryField(false);

			panelLeerzeile.setVisible(false);
			panelEndsumme.setVisible(false);

			panelSeitenumbruch.setVisible(true);

			panelUrsprung.setVisible(false);

			panelZwischensumme.setVisible(false);
			panelIntZwischensumme.setVisible(false);

			panelLieferschein.setVisible(false);

			panelAGStueckliste.setVisible(false);
			panelAGStueckliste.wnfMenge.setMandatoryField(false);
			panelAGStueckliste.wnfEinzelpreis.setMandatoryField(false);
			panelAGStueckliste.getWnfRabattsatz().setMandatoryField(false);
			panelAGStueckliste.getWnfZusatzrabattsatz()
					.setMandatoryField(false);
			panelAGStueckliste.wnfNettopreis.setMandatoryField(false);
			panelAGStueckliste.wtfAgstkl.setMandatoryField(false);

			wlaPositiontyp.setVisible(false);
			wcoPositiontyp.setVisible(false);
			wlaPositionBez.setVisible(false);
			wtfPositionBez.setVisible(false);
			wcoPositionZBez.setVisible(false);
			wlaPauschalposition.setVisible(false);
			wnfPauschalpositionpreis.setVisible(false);
			item.getButton().setVisible(false);
		} else if (currentArt.equals(LocaleFac.POSITIONSART_TEXTBAUSTEIN)) {
			panelTexteingabe.setVisible(false);

			panelBetreff.setVisible(false);

			panelHandeingabe.setVisible(false);
			panelHandeingabe.wnfMenge.setMandatoryField(false);
			panelHandeingabe.wnfEinzelpreis.setMandatoryField(false);
			panelHandeingabe.getWnfRabattsatz().setMandatoryField(false);
			panelHandeingabe.getWnfZusatzrabattsatz().setMandatoryField(false);
			panelHandeingabe.wnfNettopreis.setMandatoryField(false);
			panelHandeingabe.wtfBezeichnung.setMandatoryField(false);

			panelArtikel.setVisible(false);
			panelArtikel.wtfArtikel.setMandatoryField(false);
			panelArtikel.wnfMenge.setMandatoryField(false);
			panelArtikel.wnfEinzelpreis.setMandatoryField(false);

			if (panelArtikel.getWnfRabattsatz() != null) {
				panelArtikel.getWnfRabattsatz().setMandatoryField(false);
			}

			if (panelArtikel.getWnfZusatzrabattsatz() != null) {
				panelArtikel.getWnfZusatzrabattsatz().setMandatoryField(false);
			}

			if (panelArtikel.wnfNettopreis != null) {
				panelArtikel.wnfNettopreis.setMandatoryField(false);
			}

			if (panelArtikel instanceof PanelPositionenArtikelVerkaufSNR) {
				((PanelPositionenArtikelVerkaufSNR) panelArtikel).wtfSerienchargennummer
						.setMandatoryField(false);
			} else if (panelArtikel instanceof PanelPositionenArtikelGutschrift) {
				((PanelPositionenArtikelGutschrift) panelArtikel).wtfSerienchargennummer
						.setMandatoryField(false);
			}

			panelTextbaustein.setVisible(true);
			panelTextbaustein.wtfTextmodul.setMandatoryField(true);

			panelLeerzeile.setVisible(false);
			panelEndsumme.setVisible(false);

			panelSeitenumbruch.setVisible(false);

			panelUrsprung.setVisible(false);

			panelZwischensumme.setVisible(false);
			panelIntZwischensumme.setVisible(false);

			panelLieferschein.setVisible(false);

			panelAGStueckliste.setVisible(false);
			panelAGStueckliste.wnfMenge.setMandatoryField(false);
			panelAGStueckliste.wnfEinzelpreis.setMandatoryField(false);
			panelAGStueckliste.getWnfRabattsatz().setMandatoryField(false);
			panelAGStueckliste.getWnfZusatzrabattsatz()
					.setMandatoryField(false);
			panelAGStueckliste.wnfNettopreis.setMandatoryField(false);
			panelAGStueckliste.wtfAgstkl.setMandatoryField(false);

			wlaPositiontyp.setVisible(false);
			wcoPositiontyp.setVisible(false);
			wlaPositionBez.setVisible(false);
			wtfPositionBez.setVisible(false);
			wcoPositionZBez.setVisible(false);
			wlaPauschalposition.setVisible(false);
			wnfPauschalpositionpreis.setVisible(false);
			item.getButton().setVisible(false);
		} else if (currentArt.equals(LocaleFac.POSITIONSART_URSPRUNGSLAND)) {
			panelTexteingabe.setVisible(false);

			panelBetreff.setVisible(false);

			panelHandeingabe.setVisible(false);
			panelHandeingabe.wnfMenge.setMandatoryField(false);
			panelHandeingabe.wnfEinzelpreis.setMandatoryField(false);
			panelHandeingabe.getWnfRabattsatz().setMandatoryField(false);
			panelHandeingabe.getWnfZusatzrabattsatz().setMandatoryField(false);
			panelHandeingabe.wnfNettopreis.setMandatoryField(false);
			panelHandeingabe.wtfBezeichnung.setMandatoryField(false);

			panelArtikel.setVisible(false);
			panelArtikel.wtfArtikel.setMandatoryField(false);
			panelArtikel.wnfMenge.setMandatoryField(false);
			panelArtikel.wnfEinzelpreis.setMandatoryField(false);

			if (panelArtikel.getWnfRabattsatz() != null) {
				panelArtikel.getWnfRabattsatz().setMandatoryField(false);
			}

			if (panelArtikel.getWnfZusatzrabattsatz() != null) {
				panelArtikel.getWnfZusatzrabattsatz().setMandatoryField(false);
			}

			if (panelArtikel.wnfNettopreis != null) {
				panelArtikel.wnfNettopreis.setMandatoryField(false);
			}

			if (panelArtikel instanceof PanelPositionenArtikelVerkaufSNR) {
				((PanelPositionenArtikelVerkaufSNR) panelArtikel).wtfSerienchargennummer
						.setMandatoryField(false);
			} else if (panelArtikel instanceof PanelPositionenArtikelGutschrift) {
				((PanelPositionenArtikelGutschrift) panelArtikel).wtfSerienchargennummer
						.setMandatoryField(false);
			}

			panelTextbaustein.setVisible(false);
			panelTextbaustein.wtfTextmodul.setMandatoryField(false);

			panelLeerzeile.setVisible(false);
			panelEndsumme.setVisible(false);

			panelSeitenumbruch.setVisible(false);

			panelUrsprung.setVisible(true);

			panelZwischensumme.setVisible(false);
			panelIntZwischensumme.setVisible(false);

			panelLieferschein.setVisible(false);

			panelAGStueckliste.setVisible(false);
			panelAGStueckliste.wnfMenge.setMandatoryField(false);
			panelAGStueckliste.wnfEinzelpreis.setMandatoryField(false);
			panelAGStueckliste.getWnfRabattsatz().setMandatoryField(false);
			panelAGStueckliste.getWnfZusatzrabattsatz()
					.setMandatoryField(false);
			panelAGStueckliste.wnfNettopreis.setMandatoryField(false);
			panelAGStueckliste.wtfAgstkl.setMandatoryField(false);

			wlaPositiontyp.setVisible(false);
			wcoPositiontyp.setVisible(false);
			wlaPositionBez.setVisible(false);
			wtfPositionBez.setVisible(false);
			wcoPositionZBez.setVisible(false);
			wlaPauschalposition.setVisible(false);
			wnfPauschalpositionpreis.setVisible(false);
			item.getButton().setVisible(false);
		} else if (currentArt.equals(LocaleFac.POSITIONSART_ZWISCHENSUMME)) {
			panelTexteingabe.setVisible(false);

			panelBetreff.setVisible(false);

			panelHandeingabe.setVisible(false);
			panelHandeingabe.wnfMenge.setMandatoryField(false);
			panelHandeingabe.wnfEinzelpreis.setMandatoryField(false);
			panelHandeingabe.getWnfRabattsatz().setMandatoryField(false);
			panelHandeingabe.getWnfZusatzrabattsatz().setMandatoryField(false);
			panelHandeingabe.wnfNettopreis.setMandatoryField(false);
			panelHandeingabe.wtfBezeichnung.setMandatoryField(false);

			panelArtikel.setVisible(false);
			panelArtikel.wtfArtikel.setMandatoryField(false);
			panelArtikel.wnfMenge.setMandatoryField(false);
			panelArtikel.wnfEinzelpreis.setMandatoryField(false);

			if (panelArtikel.getWnfRabattsatz() != null) {
				panelArtikel.getWnfRabattsatz().setMandatoryField(false);
			}

			if (panelArtikel.getWnfZusatzrabattsatz() != null) {
				panelArtikel.getWnfZusatzrabattsatz().setMandatoryField(false);
			}

			if (panelArtikel.wnfNettopreis != null) {
				panelArtikel.wnfNettopreis.setMandatoryField(false);
			}

			if (panelArtikel instanceof PanelPositionenArtikelVerkaufSNR) {
				((PanelPositionenArtikelVerkaufSNR) panelArtikel).wtfSerienchargennummer
						.setMandatoryField(false);
			} else if (panelArtikel instanceof PanelPositionenArtikelGutschrift) {
				((PanelPositionenArtikelGutschrift) panelArtikel).wtfSerienchargennummer
						.setMandatoryField(false);
			}

			panelTextbaustein.setVisible(false);
			panelTextbaustein.wtfTextmodul.setMandatoryField(false);

			panelLeerzeile.setVisible(false);
			panelEndsumme.setVisible(false);
			panelIntZwischensumme.setVisible(false);

			panelSeitenumbruch.setVisible(false);

			panelUrsprung.setVisible(false);

			panelZwischensumme.setVisible(true);

			panelLieferschein.setVisible(false);

			panelAGStueckliste.setVisible(false);
			panelAGStueckliste.wnfMenge.setMandatoryField(false);
			panelAGStueckliste.wnfEinzelpreis.setMandatoryField(false);
			panelAGStueckliste.getWnfRabattsatz().setMandatoryField(false);
			panelAGStueckliste.getWnfZusatzrabattsatz()
					.setMandatoryField(false);
			panelAGStueckliste.wnfNettopreis.setMandatoryField(false);
			panelAGStueckliste.wtfAgstkl.setMandatoryField(false);

			wlaPositiontyp.setVisible(false);
			wcoPositiontyp.setVisible(false);
			wlaPositionBez.setVisible(false);
			wtfPositionBez.setVisible(false);
			wcoPositionZBez.setVisible(false);
			wlaPauschalposition.setVisible(false);
			wnfPauschalpositionpreis.setVisible(false);
			item.getButton().setVisible(false);
		} else if (currentArt.equals(LocaleFac.POSITIONSART_LIEFERSCHEIN)) {
			panelTexteingabe.setVisible(false);

			panelBetreff.setVisible(false);

			panelHandeingabe.setVisible(false);
			panelHandeingabe.wnfMenge.setMandatoryField(false);
			panelHandeingabe.wnfEinzelpreis.setMandatoryField(false);
			panelHandeingabe.getWnfRabattsatz().setMandatoryField(false);
			panelHandeingabe.getWnfZusatzrabattsatz().setMandatoryField(false);
			panelHandeingabe.wnfNettopreis.setMandatoryField(false);
			panelHandeingabe.wtfBezeichnung.setMandatoryField(false);

			panelArtikel.setVisible(false);
			panelArtikel.wtfArtikel.setMandatoryField(false);
			panelArtikel.wnfMenge.setMandatoryField(false);
			panelArtikel.wnfEinzelpreis.setMandatoryField(false);

			if (panelArtikel.getWnfRabattsatz() != null) {
				panelArtikel.getWnfRabattsatz().setMandatoryField(false);
			}

			if (panelArtikel.getWnfZusatzrabattsatz() != null) {
				panelArtikel.getWnfZusatzrabattsatz().setMandatoryField(false);
			}

			if (panelArtikel.wnfNettopreis != null) {
				panelArtikel.wnfNettopreis.setMandatoryField(false);
			}

			if (panelArtikel instanceof PanelPositionenArtikelVerkaufSNR) {
				((PanelPositionenArtikelVerkaufSNR) panelArtikel).wtfSerienchargennummer
						.setMandatoryField(false);
			} else if (panelArtikel instanceof PanelPositionenArtikelGutschrift) {
				((PanelPositionenArtikelGutschrift) panelArtikel).wtfSerienchargennummer
						.setMandatoryField(false);
			}

			panelTextbaustein.setVisible(false);
			panelTextbaustein.wtfTextmodul.setMandatoryField(false);

			panelLeerzeile.setVisible(false);
			panelEndsumme.setVisible(false);

			panelSeitenumbruch.setVisible(false);

			panelUrsprung.setVisible(false);

			panelZwischensumme.setVisible(false);
			panelIntZwischensumme.setVisible(false);

			panelLieferschein.setVisible(true);

			panelAGStueckliste.setVisible(false);
			panelAGStueckliste.wnfMenge.setMandatoryField(false);
			panelAGStueckliste.wnfEinzelpreis.setMandatoryField(false);
			panelAGStueckliste.getWnfRabattsatz().setMandatoryField(false);
			panelAGStueckliste.getWnfZusatzrabattsatz()
					.setMandatoryField(false);
			panelAGStueckliste.wnfNettopreis.setMandatoryField(false);
			panelAGStueckliste.wtfAgstkl.setMandatoryField(false);

			wlaPositiontyp.setVisible(false);
			wcoPositiontyp.setVisible(false);
			wlaPositionBez.setVisible(false);
			wtfPositionBez.setVisible(false);
			wcoPositionZBez.setVisible(false);
			wlaPauschalposition.setVisible(false);
			wnfPauschalpositionpreis.setVisible(false);
			item.getButton().setVisible(false);
		} else if (currentArt.equals(LocaleFac.POSITIONSART_AGSTUECKLISTE)) {
			panelTexteingabe.setVisible(false);

			panelBetreff.setVisible(false);

			panelHandeingabe.setVisible(false);
			panelHandeingabe.wnfMenge.setMandatoryField(false);
			panelHandeingabe.wnfEinzelpreis.setMandatoryField(false);
			panelHandeingabe.getWnfRabattsatz().setMandatoryField(false);
			panelHandeingabe.getWnfZusatzrabattsatz().setMandatoryField(false);
			panelHandeingabe.wnfNettopreis.setMandatoryField(false);
			panelHandeingabe.wtfBezeichnung.setMandatoryField(false);

			panelArtikel.setVisible(false);
			panelArtikel.wtfArtikel.setMandatoryField(false);
			panelArtikel.wnfMenge.setMandatoryField(false);
			panelArtikel.wnfEinzelpreis.setMandatoryField(false);

			if (panelArtikel.getWnfRabattsatz() != null) {
				panelArtikel.getWnfRabattsatz().setMandatoryField(false);
			}

			if (panelArtikel.getWnfZusatzrabattsatz() != null) {
				panelArtikel.getWnfZusatzrabattsatz().setMandatoryField(false);
			}

			if (panelArtikel.wnfNettopreis != null) {
				panelArtikel.wnfNettopreis.setMandatoryField(false);
			}

			if (panelArtikel instanceof PanelPositionenArtikelVerkaufSNR) {
				((PanelPositionenArtikelVerkaufSNR) panelArtikel).wtfSerienchargennummer
						.setMandatoryField(false);
			} else if (panelArtikel instanceof PanelPositionenArtikelGutschrift) {
				((PanelPositionenArtikelGutschrift) panelArtikel).wtfSerienchargennummer
						.setMandatoryField(false);
			}

			panelTextbaustein.setVisible(false);
			panelTextbaustein.wtfTextmodul.setMandatoryField(false);

			panelLeerzeile.setVisible(false);
			panelEndsumme.setVisible(false);

			panelSeitenumbruch.setVisible(false);

			panelUrsprung.setVisible(false);

			panelZwischensumme.setVisible(false);
			panelIntZwischensumme.setVisible(false);

			panelLieferschein.setVisible(false);

			panelAGStueckliste.setVisible(true);
			panelAGStueckliste.wnfMenge.setMandatoryField(true);
			panelAGStueckliste.wnfEinzelpreis.setMandatoryField(true);
			panelAGStueckliste.getWnfRabattsatz().setMandatoryField(true);
			panelAGStueckliste.getWnfZusatzrabattsatz().setMandatoryField(true);
			panelAGStueckliste.wnfNettopreis.setMandatoryField(true);
			panelAGStueckliste.wtfAgstkl.setMandatoryField(true);

			wlaPositiontyp.setVisible(false);
			wcoPositiontyp.setVisible(false);
			wlaPositionBez.setVisible(false);
			wcoPositionZBez.setVisible(false);
			wlaPauschalposition.setVisible(false);
			wnfPauschalpositionpreis.setVisible(false);
			wlaPauschalposition.setVisible(false);
			wnfPauschalpositionpreis.setVisible(false);
			item.getButton().setVisible(false);
		} else if (currentArt.equals(LocaleFac.POSITIONSART_POSITION)) {
			panelTexteingabe.setVisible(false);

			panelBetreff.setVisible(false);

			panelHandeingabe.setVisible(false);
			panelHandeingabe.wnfMenge.setMandatoryField(false);
			panelHandeingabe.wnfEinzelpreis.setMandatoryField(false);
			panelHandeingabe.getWnfRabattsatz().setMandatoryField(false);
			panelHandeingabe.getWnfZusatzrabattsatz().setMandatoryField(false);
			panelHandeingabe.wnfNettopreis.setMandatoryField(false);
			panelHandeingabe.wtfBezeichnung.setMandatoryField(false);

			panelArtikel.setVisible(false);
			panelArtikel.wtfArtikel.setMandatoryField(false);
			panelArtikel.wnfMenge.setMandatoryField(false);
			panelArtikel.wnfEinzelpreis.setMandatoryField(false);

			wlaPositiontyp.setVisible(true);
			wcoPositiontyp.setVisible(true);
			wlaPositionBez.setVisible(true);
			wtfPositionBez.setVisible(true);
			wcoPositionZBez.setVisible(true);
			wlaPauschalposition.setVisible(true);
			wnfPauschalpositionpreis.setVisible(true);

			String currentZBeZ = (String) wcoPositionZBez
					.getKeyOfSelectedItem();
			if (currentZBeZ.equals(LocaleFac.POSITIONBEZ_ENDE)) {
				wcoPositiontyp.setVisible(false);
				wlaPositiontyp.setVisible(false);
				wtfPositionBez.setVisible(false);
				wlaPositionBez.setVisible(false);
				wlaPauschalposition.setVisible(false);
				wnfPauschalpositionpreis.setVisible(false);
			}

			if (panelArtikel.getWnfRabattsatz() != null) {
				panelArtikel.getWnfRabattsatz().setMandatoryField(false);
			}

			if (panelArtikel.getWnfZusatzrabattsatz() != null) {
				panelArtikel.getWnfZusatzrabattsatz().setMandatoryField(false);
			}

			if (panelArtikel.wnfNettopreis != null) {
				panelArtikel.wnfNettopreis.setMandatoryField(false);
			}

			if (panelArtikel instanceof PanelPositionenArtikelVerkaufSNR) {
				((PanelPositionenArtikelVerkaufSNR) panelArtikel).wtfSerienchargennummer
						.setMandatoryField(false);
			} else if (panelArtikel instanceof PanelPositionenArtikelGutschrift) {
				((PanelPositionenArtikelGutschrift) panelArtikel).wtfSerienchargennummer
						.setMandatoryField(false);
			}

			panelTextbaustein.setVisible(false);
			panelTextbaustein.wtfTextmodul.setMandatoryField(false);

			panelLeerzeile.setVisible(true);

			panelSeitenumbruch.setVisible(false);

			panelUrsprung.setVisible(false);

			panelZwischensumme.setVisible(false);
			panelIntZwischensumme.setVisible(false);

			panelLieferschein.setVisible(false);

			panelAGStueckliste.setVisible(false);
			panelAGStueckliste.wnfMenge.setMandatoryField(false);
			panelAGStueckliste.wnfEinzelpreis.setMandatoryField(false);
			panelAGStueckliste.getWnfRabattsatz().setMandatoryField(false);
			panelAGStueckliste.getWnfZusatzrabattsatz()
					.setMandatoryField(false);
			panelAGStueckliste.wnfNettopreis.setMandatoryField(false);
			panelAGStueckliste.wtfAgstkl.setMandatoryField(false);

			panelEndsumme.setVisible(false);
			item.getButton().setVisible(false);

		} else if (currentArt
				.equals(LocaleFac.POSITIONSART_INTELLIGENTE_ZWISCHENSUMME)) {
			panelTexteingabe.setVisible(false);
			panelBetreff.setVisible(false);
			panelHandeingabe.setVisible(false);
			panelArtikel.setVisible(false);
			panelTextbaustein.setVisible(false);
			panelLeerzeile.setVisible(false);
			panelEndsumme.setVisible(false);
			panelZwischensumme.setVisible(false);
			panelSeitenumbruch.setVisible(false);
			panelUrsprung.setVisible(false);
			panelLieferschein.setVisible(false);
			panelAGStueckliste.setVisible(false);

			wlaPositiontyp.setVisible(false);
			wcoPositiontyp.setVisible(false);
			wlaPositionBez.setVisible(false);
			wtfPositionBez.setVisible(false);
			wcoPositionZBez.setVisible(false);
			wlaPauschalposition.setVisible(false);
			wnfPauschalpositionpreis.setVisible(false);

			panelIntZwischensumme.setVisible(true);
			item.getButton().setVisible(false);
		}
	}

	protected final void jComboBoxPositionZBez_actionPerformed() {
		String currentZBeZ = (String) wcoPositionZBez.getKeyOfSelectedItem();
		if (currentZBeZ.equals(LocaleFac.POSITIONBEZ_ENDE)) {
			wcoPositiontyp.setVisible(false);
			wlaPositiontyp.setVisible(false);
			wtfPositionBez.setVisible(false);
			wlaPositionBez.setVisible(false);
			wlaPauschalposition.setVisible(false);
			wnfPauschalpositionpreis.setVisible(false);
		} else {
			wcoPositiontyp.setVisible(true);
			wlaPositiontyp.setVisible(true);
			wtfPositionBez.setVisible(true);
			wlaPositionBez.setVisible(true);
			wlaPauschalposition.setVisible(true);
			wnfPauschalpositionpreis.setVisible(true);
		}
	}

	protected void wnfPauschalposition_focusLost(FocusEvent e) {
		// im Moment hier nix. ACHTUNG spezialbehandlung in den subklassen.
	}

	/**
	 * Die Zeile mit den Positionsarten.
	 * 
	 * @throws ExceptionLP
	 */
	public void createPanelPositionsarten() throws ExceptionLP {
		if (jPanelPositionsarten == null) {
			jPanelPositionsarten = new JPanel();
			GridBagLayout gridBagLayout = new GridBagLayout();
			jPanelPositionsarten.setLayout(gridBagLayout);
			jPanelPositionsarten.setBorder(BorderFactory.createEmptyBorder(10,
					0, 10, 0));
		}

		wlaPositionsart = new WrapperLabel(
				LPMain.getTextRespectUISPr("label.art"));
		wlaPositionsart.setMaximumSize(new Dimension(
				iSpaltenbreiteArtikelMitGoto, Defaults.getInstance()
						.getControlHeight()));
		wlaPositionsart.setMinimumSize(new Dimension(
				iSpaltenbreiteArtikelMitGoto, Defaults.getInstance()
						.getControlHeight()));
		wlaPositionsart.setPreferredSize(new Dimension(
				iSpaltenbreiteArtikelMitGoto, Defaults.getInstance()
						.getControlHeight()));

		wcoPositionsart = new WrapperComboBox();
		wcoPositionsart.setMandatoryFieldDB(true);
		wcoPositionsart
				.addActionListener(new PanelPositionen2_jComboBoxPositionsart_actionAdapter(
						this));

		jPanelPositionsarten.add(wlaPositionsart, new GridBagConstraints(0, 0,
				1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jPanelPositionsarten.add(wcoPositionsart, new GridBagConstraints(1, 0,
				3, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		wlaPositiontyp = new WrapperLabel(
				LPMain.getTextRespectUISPr("lp.position"));
		wcoPositiontyp = new WrapperComboBox();
		wcoPositiontyp.setMandatoryFieldDB(true);
		// wcoPositiontyp.addItemListener(new
		// PanelPositionen2_jComboBoxPositionsart_actionAdapter(this));
		LinkedHashMap<String, String> hm = new LinkedHashMap<String, String>();
		hm.put(LocaleFac.POSITIONTYP_ALLES,
				LPMain.getTextRespectUISPr("lp.position.preiszusammenfassen"));
		hm.put(LocaleFac.POSITIONTYP_VERDICHTET,
				LPMain.getTextRespectUISPr("lp.position.verdichten"));
		hm.put(LocaleFac.POSITIONTYP_OHNEPREISE,
				LPMain.getTextRespectUISPr("lp.position.preisunterdrueken"));
		hm.put(LocaleFac.POSITIONTYP_MITPREISE,
				LPMain.getTextRespectUISPr("lp.position.mitpreis"));
		wcoPositiontyp.setMap(hm);

		wlaPauschalposition = new WrapperLabel(
				LPMain.getTextRespectUISPr("lp.pauschalpreis"));
		wnfPauschalpositionpreis = new WrapperNumberField();
		wnfPauschalpositionpreis
				.addFocusListener(new PanelPositionen2_wnfPauschalposition_focusAdapter(
						this));

		wlaPositionBez = new WrapperLabel(
				LPMain.getTextRespectUISPr("lp.bezeichnung"));
		wtfPositionBez = new WrapperTextField();

		wcoPositionZBez = new WrapperComboBox();
		wcoPositionZBez.setMandatoryFieldDB(true);
		wcoPositionZBez
				.addActionListener(new PanelPositionen2_jComboBoxPositionZBez_actionAdapter(
						this));
		LinkedHashMap<String, String> hmBez = new LinkedHashMap<String, String>();
		hmBez.put(LocaleFac.POSITIONBEZ_BEGINN,
				LPMain.getTextRespectUISPr("lp.beginn"));
		hmBez.put(LocaleFac.POSITIONBEZ_ENDE,
				LPMain.getTextRespectUISPr("lp.ende"));
		wcoPositionZBez.setMap(hmBez);

		jPanelPositionsarten.add(wcoPositionZBez, new GridBagConstraints(1, 1,
				1, 1, 0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 20, 0));
		jPanelPositionsarten.add(wlaPositionBez, new GridBagConstraints(2, 1,
				1, 1, 0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 40, 0));

		jPanelPositionsarten.add(wtfPositionBez, new GridBagConstraints(3, 1,
				1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		jPanelPositionsarten.add(wlaPositiontyp, new GridBagConstraints(2, 2,
				1, 1, 0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jPanelPositionsarten.add(wcoPositiontyp, new GridBagConstraints(3, 2,
				1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		jPanelPositionsarten.add(wlaPauschalposition, new GridBagConstraints(2,
				3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jPanelPositionsarten.add(wnfPauschalpositionpreis,
				new GridBagConstraints(3, 3, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(2, 2, 2, 2), 0, 0));

	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return NO_VALUE_THATS_OK_JCOMPONENT;
	}

	/**
	 * Verwerfen der aktuelle Usereingabe und zurueckgehen auf den bestehenden
	 * Datensatz, wenn einer existiert.
	 * 
	 * @param e
	 *            Ereignis
	 * @throws Throwable
	 *             Ausnahme
	 */
	protected void eventActionDiscard(ActionEvent e) throws Throwable {
		super.eventActionDiscard(e);

		// poseinfuegen: 1 per Default wird eine Position hinten angefuegt
		bFuegeNeuePositionVorDerSelektiertenEin = false;
		resetArtikelsetIIdForNewPosition();
	}

	private String getLocaleDesPartners() throws Throwable {

		String localeCNrKommunikation = null;
		if (getInternalFrame() instanceof InternalFrameAngebot) {
			localeCNrKommunikation = ((InternalFrameAngebot) getInternalFrame())
					.getTabbedPaneAngebot().getKundeDto().getPartnerDto()
					.getLocaleCNrKommunikation();
		} else if (getInternalFrame() instanceof InternalFrameAuftrag) {
			localeCNrKommunikation = ((InternalFrameAuftrag) getInternalFrame())
					.getTabbedPaneAuftrag().getKundeAuftragDto()
					.getPartnerDto().getLocaleCNrKommunikation();
		} else if (getInternalFrame() instanceof InternalFrameLieferschein) {
			localeCNrKommunikation = ((InternalFrameLieferschein) getInternalFrame())
					.getTabbedPaneLieferschein().getKundeLieferadresseDto()
					.getPartnerDto().getLocaleCNrKommunikation();
		} else if (getInternalFrame() instanceof InternalFrameRechnung) {

			InternalFrameRechnung ifRechnung = ((InternalFrameRechnung) getInternalFrame());
			if (ifRechnung.getTabbedPaneRoot().getSelectedComponent() instanceof TabbedPaneRechnung) {
				localeCNrKommunikation = ((TabbedPaneRechnung) ifRechnung
						.getTabbedPaneRoot().getSelectedComponent())
						.getKundeDto().getPartnerDto()
						.getLocaleCNrKommunikation();
			} else if (ifRechnung.getTabbedPaneRoot().getSelectedComponent() instanceof TabbedPaneGutschrift) {
				localeCNrKommunikation = ((TabbedPaneGutschrift) ifRechnung
						.getTabbedPaneRoot().getSelectedComponent())
						.getKundeDto().getPartnerDto()
						.getLocaleCNrKommunikation();

			} else if (ifRechnung.getTabbedPaneRoot().getSelectedComponent() instanceof TabbedPaneProformarechnung) {
				localeCNrKommunikation = ((TabbedPaneProformarechnung) ifRechnung
						.getTabbedPaneRoot().getSelectedComponent())
						.getKundeDto().getPartnerDto()
						.getLocaleCNrKommunikation();

			}

		}
		return localeCNrKommunikation;
	}

	public void eventActionText(ActionEvent e) throws Throwable {
		super.eventActionText(e);
		if (getLockedstateDetailMainKey().getIState() == PanelBasis.LOCK_IS_LOCKED_BY_ME) {
			// Editor auf Read Only schalten

		}

		// PJ 17267
		if (wefText.getLpEditor().getText() == null
				|| wefText.getLpEditor().getText().length() == 0) {
			ParametermandantDto parameter = (ParametermandantDto) DelegateFactory
					.getInstance()
					.getParameterDelegate()
					.getParametermandant(
							ParameterFac.PARAMETER_ARTIKELLANGTEXTE_UEBERSTEUERBAR,
							ParameterFac.KATEGORIE_ALLGEMEIN,
							LPMain.getTheClient().getMandant());

			if (parameter.getCWert() != null
					&& ((Boolean) parameter.getCWertAsObject()) == true) {
				if (panelArtikel.getArtikelDto() != null
						&& panelArtikel.getArtikelDto().getIId() != null) {

					String locale = getLocaleDesPartners();
					if (locale != null) {
						ArtikelkommentarDto[] kommDtos = DelegateFactory
								.getInstance()
								.getArtikelkommentarDelegate()
								.artikelkommentardruckFindByArtikelIIdBelegartCNr(
										panelArtikel.getArtikelDto().getIId(),
										locale,
										getInternalFrame().getBelegartCNr());
						if (kommDtos.length > 0) {
							if (kommDtos[0].getDatenformatCNr().equals(
									MediaFac.DATENFORMAT_MIMETYPE_TEXT_HTML)
									&& kommDtos[0].getArtikelkommentarsprDto() != null) {

								if (DialogFactory
										.showModalJaNeinDialog(
												getInternalFrame(),
												LPMain.getTextRespectUISPr("artikel.artikeltextuebersteuern"))) {

									wefText.getLpEditor()
											.setText(
													kommDtos[0]
															.getArtikelkommentarsprDto()
															.getXKommentar());
								}
							}
						}
					}
				}

			}
		}

		getInternalFrame().showPanelEditor(wefText, this.getAdd2Title(),
				wefText.getLpEditor().getText(),
				getLockedstateDetailMainKey().getIState());
	}

	protected boolean wirdPreisvorschlagsdialogGeradeAngezeit() {

		if (getPositionsartCNr().equals(LocaleFac.POSITIONSART_IDENT)) {
			if (panelArtikel instanceof PanelPositionenArtikelVerkauf) {
				PanelPositionenArtikelVerkauf pv = (PanelPositionenArtikelVerkauf) panelArtikel;
				if (pv.pdPreisvorschlag != null) {
					if (pv.pdPreisvorschlag.isbDialogWirdGeradeAngezeigt() == true) {
						pv.pdPreisvorschlag
								.setbDialogWirdGeradeAngezeigt(false);
						return true;
					}
				}

			}
		}

		return false;
	}

	protected void eventActionEscape(ActionEvent e) throws Throwable {
		super.eventActionEscape(e);
	}

	protected void wirdLagermindeststandUnterschritten(
			java.sql.Timestamp dLiefertermin, java.math.BigDecimal nMenge,
			Integer artikelIId) throws Throwable {

		if (dLiefertermin != null && nMenge != null && artikelIId != null) {

			if (getPositionsartCNr().equals(LocaleFac.POSITIONSART_IDENT)) {
				String s = DelegateFactory
						.getInstance()
						.getLagerDelegate()
						.wirdLagermindeststandUnterschritten(
								new java.sql.Date(dLiefertermin.getTime()),
								nMenge, artikelIId);

				if (s != null) {
					DialogFactory.showModalDialog(
							LPMain.getTextRespectUISPr("lp.info"), s);
				}
			}
		}

	}

	public boolean handleOwnException(ExceptionLP exfc) throws Throwable {
		/** @todo JO->UW PJ 5029 */
		boolean bErrorErkannt = true;
		int code = exfc.getICode();

		switch (code) {
		case EJBExceptionLP.FEHLER_FORMAT_NUMBER:
			DialogFactory.showModalDialog(
					LPMain.getTextRespectUISPr("lp.error"),
					LPMain.getTextRespectUISPr("lp.error.belegwerte"));
			break;

		case EJBExceptionLP.FEHLER_DLG_DONE_DO_NOTHING:

			// die laufende Aktion wurde abgebrochen
			break;

		default:
			bErrorErkannt = false;
		}
		return bErrorErkannt;
	}

	/**
	 * Nochmaliges Durchrechnen der Preisfelder.
	 * 
	 * @throws Throwable
	 */
	protected final void calculateFields() throws Throwable {

		String sPosart = (String) wcoPositionsart.getKeyOfSelectedItem();
		if (sPosart != null) {
			if (sPosart.equals(LocaleFac.POSITIONSART_AGSTUECKLISTE)) {
				panelAGStueckliste.calculateFields();
			} else if (sPosart.equals(LocaleFac.POSITIONSART_ANZAHLUNG)) {
				// clientseitig noch nicht implementiert
			} else if (sPosart.equals(LocaleFac.POSITIONSART_BETRIFFT)) {
				// nothing here
			} else if (sPosart.equals(LocaleFac.POSITIONSART_BILD)) {
				// clientseitig noch nicht implementiert
			} else if (sPosart.equals(LocaleFac.POSITIONSART_ENDSUMME)) {
				// clientseitig noch nicht implementiert
			} else if (sPosart.equals(LocaleFac.POSITIONSART_FREMDARTIKEL)) {
				// clientseitig noch nicht implementiert
			} else if (sPosart.equals(LocaleFac.POSITIONSART_GUTSCHRIFT)) {
				// clientseitig noch nicht implementiert
			} else if (sPosart.equals(LocaleFac.POSITIONSART_HANDEINGABE)) {
				panelHandeingabe.calculateFields();
			} else if (sPosart.equals(LocaleFac.POSITIONSART_IDENT)) {
				panelArtikel.calculateFields();
			} else if (sPosart.equals(LocaleFac.POSITIONSART_LEERZEILE)) {
				// nothing here
			} else if (sPosart.equals(LocaleFac.POSITIONSART_ENDSUMME)) {
				// nothing here
			} else if (sPosart.equals(LocaleFac.POSITIONSART_LIEFERSCHEIN)) {
				// nothing here
			} else if (sPosart.equals(LocaleFac.POSITIONSART_PAUSCHALPOSITION)) {
				// clientseitig noch nicht implementiert
			} else if (sPosart.equals(LocaleFac.POSITIONSART_POSITION)) {
				// clientseitig noch nicht implementiert
			} else if (sPosart.equals(LocaleFac.POSITIONSART_RECHNUNG)) {
				// clientseitig noch nicht implementiert
			} else if (sPosart.equals(LocaleFac.POSITIONSART_REPARATUR)) {
				// clientseitig noch nicht implementiert
			} else if (sPosart.equals(LocaleFac.POSITIONSART_RUECKSCHEIN)) {
				// clientseitig noch nicht implementiert
			} else if (sPosart.equals(LocaleFac.POSITIONSART_SEITENUMBRUCH)) {
				// nothing here
			} else if (sPosart
					.equals(LocaleFac.POSITIONSART_STUECKLISTENPOSITION)) {
				// nothing here
			} else if (sPosart.equals(LocaleFac.POSITIONSART_TEXTBAUSTEIN)) {
				// nothing here
			} else if (sPosart.equals(LocaleFac.POSITIONSART_TEXTEINGABE)) {
				// nothing here
			} else if (sPosart.equals(LocaleFac.POSITIONSART_TRANSPORTSPESEN)) {
				// clientseitig noch nicht implementiert
			} else if (sPosart.equals(LocaleFac.POSITIONSART_URSPRUNGSLAND)) {
				// nothing here
			} else if (sPosart.equals(LocaleFac.POSITIONSART_VORAUSZAHLUNG)) {
				// clientseitig noch nicht implementiert
			} else if (sPosart.equals(LocaleFac.POSITIONSART_ZWISCHENSUMME)) {
				// nothing here
			} else if (sPosart
					.equals(LocaleFac.POSITIONSART_INTELLIGENTE_ZWISCHENSUMME)) {
				panelIntZwischensumme.calculateFields();
			}
		}
	}

	/**
	 * Die Positionsart ist aenderbar, wenn eine neue Position angelegt wird.
	 * 
	 * @throws Throwable
	 *             Ausnahme
	 * @param belegpositionDto
	 *            BelegpositionDto
	 */
	protected final void setzePositionsartAenderbar(
			BelegpositionDto belegpositionDto) throws Throwable {
		if (belegpositionDto != null) {
			if (belegpositionDto.getIId() == null) {
				// Neu anlegen
				wcoPositionsart.setEnabled(true);
			} else {
				// Seitenumbruch, Leerzeile, Zwischensumme darf geaendert werden
				if (belegpositionDto.getPositionsartCNr() != null
						&& (belegpositionDto.getPositionsartCNr().equals(
								LocaleFac.POSITIONSART_SEITENUMBRUCH)
								|| belegpositionDto
										.getPositionsartCNr()
										.equals(LocaleFac.POSITIONSART_LEERZEILE) || belegpositionDto
								.getPositionsartCNr().equals(
										LocaleFac.POSITIONSART_ZWISCHENSUMME))) {
					// wenn ich ihn gelockt hab
					if (super.getLockedstateDetailMainKey().getIState() == LOCK_IS_LOCKED_BY_ME) {
						wcoPositionsart.setEnabled(true);
					}
				} else {
					wcoPositionsart.setEnabled(false);
				}
			}
		}
	}

	public Timestamp getTBelegdatumMwstsatz() {
		return tBelegdatumMwstsatz;
	}

	protected final void setTBelegdatumMwstsatz(
			java.util.Date tBelegdatumMwstsatz) {
		Timestamp tDatum = new Timestamp(tBelegdatumMwstsatz.getTime());
		setTBelegdatumMwstsatz(tDatum);
	}

	protected final void setTBelegdatumMwstsatz(Timestamp tBelegdatumMwstsatz) {
		if (panelArtikel != null) {
			panelArtikel.setTBelegdatumMwstsatz(tBelegdatumMwstsatz);
		}
		if (panelHandeingabe != null) {
			panelHandeingabe.setTBelegdatumMwstsatz(tBelegdatumMwstsatz);
		}
		if (panelAGStueckliste != null) {
			panelAGStueckliste.setTBelegdatumMwstsatz(tBelegdatumMwstsatz);
		}
	}

	protected void components2positionDto(BelegpositionDto positionDto,
			BelegpositionDto aposDto) throws Throwable {

	}

	protected final void components2Dto(BelegpositionDto positionDto,
			String locPartner, Integer belegIId) throws Throwable {
		// Positionsart aus der Combobox
		String positionsart = (String) wcoPositionsart.getKeyOfSelectedItem();
		// Neue Position? nur dann wird die Positionsart zugewiesen, ausser ...
		// Sk positionsart muss in der auswahl immer stimmen. Fuer Projekt 12737
		// hierher gegeben
		positionDto.setPositionsartCNr(positionsart);
		if (positionDto.getIId() == null) {
			// positionDto.setPositionsartCNr(positionsart);
			// gilt auch fuer die I_ID des Belegs
			positionDto.setBelegIId(belegIId);
			// Boolean - Felder vorbelegen. Diese werden im Normalfall nachher
			// ueberschrieben.
			positionDto.setBArtikelbezeichnunguebersteuert(Helper
					.boolean2Short(false));
		}
		// ... Seitenumbruch, Leerzeile, Zwischensumme duerfen untereinander
		// geaendert werden
		else {
			if ((positionDto.getPositionsartCNr().equals(
					LocaleFac.POSITIONSART_SEITENUMBRUCH)
					|| positionDto.getPositionsartCNr().equals(
							LocaleFac.POSITIONSART_LEERZEILE) || positionDto
					.getPositionsartCNr().equals(
							LocaleFac.POSITIONSART_ZWISCHENSUMME))
					&& (positionsart
							.equals(LocaleFac.POSITIONSART_SEITENUMBRUCH)
							|| positionsart
									.equals(LocaleFac.POSITIONSART_LEERZEILE) || positionsart
								.equals(LocaleFac.POSITIONSART_ZWISCHENSUMME))) {
				positionDto.setPositionsartCNr(positionsart);
			}
		}
		// Positionsarten
		if (positionDto.getPositionsartCNr().equals(
				LocaleFac.POSITIONSART_BETRIFFT)) {
			positionDto.setCBez(panelBetreff.wtfBetreff.getText());
		} else if (positionDto.getPositionsartCNr().equals(
				LocaleFac.POSITIONSART_POSITION)) {
			positionDto.setCZusatzbez((String) wcoPositionZBez
					.getKeyOfSelectedItem());
			positionDto.setCBez(wtfPositionBez.getText());
			if (positionDto instanceof BelegpositionVerkaufDto) {
				BelegpositionVerkaufDto positionDtoVK = (BelegpositionVerkaufDto) positionDto;
				positionDtoVK.setTypCNr(getPositiontyp());
			}
		} else if (positionDto.getPositionsartCNr().equalsIgnoreCase(
				LocaleFac.POSITIONSART_TEXTBAUSTEIN)) {
			if (panelTextbaustein.oMediastandardDto.getDatenformatCNr().equals(
					MediaFac.DATENFORMAT_MIMETYPE_TEXT_HTML)) {
				String sMediaText = panelTextbaustein.oMediastandardDto
						.getOMediaText();
				String sAktEditiorText = panelTextbaustein.wefText.getText();
				if (!sMediaText.equals(sAktEditiorText)) {
					// Der Medientext wurde geaendert. Umbauen auf Texteingabe
					positionDto
							.setPositionsartCNr(LocaleFac.POSITIONSART_TEXTEINGABE);
					positionDto.setXTextinhalt(sAktEditiorText);
				}
			}
			positionDto.setMediastandardIId(panelTextbaustein.oMediastandardDto
					.getIId());
		} else if (positionDto.getPositionsartCNr().equalsIgnoreCase(
				LocaleFac.POSITIONSART_TEXTEINGABE)) {
			positionDto
					.setXTextinhalt(panelTexteingabe.getLpEditor().getText());
		} else if (positionDto.getPositionsartCNr().equals(
				LocaleFac.POSITIONSART_URSPRUNGSLAND)) {
			positionDto.setCBez(panelUrsprung.wtfUrsprung.getText());
		} else if (positionDto.getPositionsartCNr().equals(
				LocaleFac.POSITIONSART_IDENT)) {
			positionDto.setArtikelIId(panelArtikel.getArtikelDto().getIId());

			positionDto.setNMenge(panelArtikel.wnfMenge.getBigDecimal());
			positionDto.setEinheitCNr((String) panelArtikel.wcoEinheit
					.getKeyOfSelectedItem());

			if (wefText.getText() != null)
				positionDto.setXTextinhalt(wefText.getText());

			/**
			 * @todo locale-logik umbauen.
			 * @todo das Flag muss anders gesteuert werden.
			 */
			// wurde die bezeichnung aus dem artikel uebersteuert?
			if (panelArtikel.wtfBezeichnung.getText() != null
					&& ((panelArtikel.getArtikelDto().getArtikelsprDto() == null || panelArtikel
							.getArtikelDto().getArtikelsprDto().getCBez() == null) || (panelArtikel
							.getArtikelDto().getArtikelsprDto() != null
							&& panelArtikel.getArtikelDto().getArtikelsprDto()
									.getCBez() != null && !panelArtikel.wtfBezeichnung
							.getText().equals(
									panelArtikel.getArtikelDto()
											.getArtikelsprDto().getCBez())))) {
				positionDto.setCBez(panelArtikel.wtfBezeichnung.getText());
				positionDto.setBArtikelbezeichnunguebersteuert(Helper
						.boolean2Short(true));
			} else {
				positionDto.setCBez(null);
				positionDto.setBArtikelbezeichnunguebersteuert(Helper
						.boolean2Short(false));
			}

			// wurde die zusatzbezeichnung aus dem artikel uebersteuert?
			if (panelArtikel.wtfZusatzbezeichnung.getText() != null
					&& ((panelArtikel.getArtikelDto().getArtikelsprDto() == null || panelArtikel
							.getArtikelDto().getArtikelsprDto().getCZbez() == null) || (panelArtikel
							.getArtikelDto().getArtikelsprDto() != null
							&& panelArtikel.getArtikelDto().getArtikelsprDto()
									.getCZbez() != null && !panelArtikel.wtfZusatzbezeichnung
							.getText().equals(
									panelArtikel.getArtikelDto()
											.getArtikelsprDto().getCZbez())))) {
				positionDto.setCZusatzbez(panelArtikel.wtfZusatzbezeichnung
						.getText());
				positionDto.setBArtikelbezeichnunguebersteuert(Helper
						.boolean2Short(true));
			} else {
				// wurde die bezeichnung aus dem artikel uebersteuert?
				if (Helper.short2boolean(positionDto
						.getBArtikelbezeichnunguebersteuert())) {
					positionDto.setCZusatzbez(null);
				} else {
					positionDto.setCZusatzbez(null);
					positionDto.setBArtikelbezeichnunguebersteuert(Helper
							.boolean2Short(false));
				}
			}
			// Preise und Rabatte fuer Verkaufsbelege.
			if (positionDto instanceof BelegpositionVerkaufDto) {
				BelegpositionVerkaufDto positionDtoVK = (BelegpositionVerkaufDto) positionDto;

				positionDtoVK.setKostentraegerIId(panelArtikel.wsfKostentraeger
						.getIKey());

				positionDtoVK.setCLvposition(panelArtikel.wtfLVPosition
						.getText());

				if (positionDto instanceof LieferscheinpositionDto) {
					((LieferscheinpositionDto) positionDto)
							.setLagerIId(panelArtikel.selectedlagerIId);
				}

				positionDtoVK.setFRabattsatz(panelArtikel.getWnfRabattsatz()
						.getDouble());
				// wurde der Rabattsatz aus der Verkaufspreisfindung
				// ubersteuert?
				if (((PanelPositionenArtikelVerkauf) panelArtikel).bIstRabattsatzDefaultUebersteuert) {
					positionDtoVK.setBRabattsatzuebersteuert(Helper
							.boolean2Short(true));
				} else {
					positionDtoVK.setBRabattsatzuebersteuert(Helper
							.boolean2Short(false));
				}
				// Zusatzrabatt // poszusatzrabatt: 4 den Zusatzrabattsatz
				// setzen
				positionDtoVK.setFZusatzrabattsatz(panelArtikel
						.getWnfZusatzrabattsatz().getDouble());
				// MWST-Satz
				positionDtoVK.setMwstsatzIId((Integer) panelArtikel.wcoMwstsatz
						.getKeyOfSelectedItem());
				// wurde der Vorschlag fuer den Mwstsatz uebersteuert?
				if (((PanelPositionenArtikelVerkauf) panelArtikel).bIstMwstsatzDefaultUebersteuert) {
					positionDtoVK.setBMwstsatzuebersteuert(Helper
							.boolean2Short(true));
				} else {
					positionDtoVK.setBMwstsatzuebersteuert(Helper
							.boolean2Short(false));
				}

			}

			if (positionDto instanceof AgstklpositionDto) {
				AgstklpositionDto agstklpositionDto = (AgstklpositionDto) positionDto;
				if (panelArtikel.wnfAufschlagBetrag != null) {
					agstklpositionDto
							.setNAufschlag(panelArtikel.wnfAufschlagBetrag
									.getBigDecimal());
				}
				if (panelArtikel.wnfAufschlagProzent != null) {
					agstklpositionDto
							.setFAufschlag(panelArtikel.wnfAufschlagProzent
									.getDouble());
				}
				if (panelArtikel.wnfGesamtpreisMitAufschlag != null) {
					agstklpositionDto
							.setNNettogesamtmitaufschlag(panelArtikel.wnfGesamtpreisMitAufschlag
									.getBigDecimal());
					agstklpositionDto
							.setBAufschlaggesamtFixiert(panelArtikel.wnfGesamtpreisMitAufschlag
									.getWrbFixNumber().getShort());
				}

			}

			if (panelArtikel.wnfNettopreis.getWrbFixNumber().isSelected())
				positionDto.setBNettopreisuebersteuert(Helper
						.boolean2Short(true));
			else {
				int iPreiseUINachkommastellen = Defaults.getInstance()
						.getIUINachkommastellenPreiseAllgemein();

				if (positionDto instanceof BelegpositionVerkaufDto) {
					iPreiseUINachkommastellen = Defaults.getInstance()
							.getIUINachkommastellenPreiseVK();
				} else {
					iPreiseUINachkommastellen = Defaults.getInstance()
							.getIUINachkommastellenPreiseEK();
				}

				BigDecimal nEinzelpreisMinusRabattsumme = panelArtikel.wnfEinzelpreis
						.getBigDecimal();
				if (panelArtikel.wnfRabattsatz.getDouble().doubleValue() != 0) {
					BigDecimal nRabattsumme = panelArtikel.wnfEinzelpreis
							.getBigDecimal().multiply(
									new BigDecimal(panelArtikel.wnfRabattsatz
											.getDouble().doubleValue())
											.movePointLeft(2));
					nRabattsumme = Helper.rundeKaufmaennisch(nRabattsumme,
							iPreiseUINachkommastellen);
					nEinzelpreisMinusRabattsumme = panelArtikel.wnfEinzelpreis
							.getBigDecimal().subtract(nRabattsumme);
				}

				if (positionDto instanceof BelegpositionVerkaufDto) {
					if (panelArtikel.wnfZusatzrabattsatz.getDouble()
							.doubleValue() != 0) {
						BigDecimal nZusatzrabattsumme = nEinzelpreisMinusRabattsumme
								.multiply(
										new BigDecimal(
												panelArtikel.wnfZusatzrabattsatz
														.getDouble()
														.doubleValue()))
								.movePointLeft(2);
						nZusatzrabattsumme = Helper.rundeKaufmaennisch(
								nZusatzrabattsumme, iPreiseUINachkommastellen);
						nEinzelpreisMinusRabattsumme = nEinzelpreisMinusRabattsumme
								.subtract(nZusatzrabattsumme);
					}
				}

				BigDecimal nettoPreisOhneMaterialzuschlag = panelArtikel.wnfNettopreis
						.getBigDecimal();

				if (panelArtikel.wnfMaterialzuschlag != null
						&& panelArtikel.wnfMaterialzuschlag.getBigDecimal() != null) {
					nettoPreisOhneMaterialzuschlag = nettoPreisOhneMaterialzuschlag
							.subtract(panelArtikel.wnfMaterialzuschlag
									.getBigDecimal());
				}

				boolean bUebersteuert = (nEinzelpreisMinusRabattsumme
						.compareTo(nettoPreisOhneMaterialzuschlag) != 0);
				if (bUebersteuert) {
					positionDto.setBNettopreisuebersteuert(Helper
							.boolean2Short(true));
				} else {
					positionDto.setBNettopreisuebersteuert(Helper
							.boolean2Short(false));
				}
			}
			if (positionDto instanceof BelegpositionVerkaufDto) {
				BelegpositionVerkaufDto positionDtoVK = (BelegpositionVerkaufDto) positionDto;
				// Preise // posvkpf: 8
				positionDtoVK.setNEinzelpreis(panelArtikel.wnfEinzelpreis
						.getBigDecimal());
				positionDtoVK.setNNettoeinzelpreis(panelArtikel.wnfNettopreis
						.getBigDecimal());
				positionDtoVK.setNBruttoeinzelpreis(panelArtikel.wnfBruttopreis
						.getBigDecimal());
				positionDtoVK
						.setNMaterialzuschlag(panelArtikel.wnfMaterialzuschlag
								.getBigDecimal());

				if (panelArtikel.wcoVerleih != null) {
					positionDtoVK
							.setVerleihIId((Integer) panelArtikel.wcoVerleih
									.getKeyOfSelectedItem());
				}
			}
		} else if (positionDto.getPositionsartCNr().equals(
				LocaleFac.POSITIONSART_HANDEINGABE)) {
			// Handeingabe muss immer Uebersteuert sein.
			positionDto.setBArtikelbezeichnunguebersteuert(Helper
					.boolean2Short(true));
			positionDto.setCBez(panelHandeingabe.wtfBezeichnung.getText());
			positionDto.setCZusatzbez(panelHandeingabe.wtfZusatzbezeichnung
					.getText());
			positionDto.setNMenge(panelHandeingabe.wnfMenge.getBigDecimal());
			positionDto.setEinheitCNr((String) panelHandeingabe.wcoEinheit
					.getKeyOfSelectedItem());
			// Preise und Rabatte fuer Verkaufsbelege.
			if (positionDto instanceof BelegpositionVerkaufDto) {
				BelegpositionVerkaufDto positionDtoVK = (BelegpositionVerkaufDto) positionDto;
				positionDtoVK.setFRabattsatz(panelHandeingabe
						.getWnfRabattsatz().getDouble());
				positionDtoVK
						.setKostentraegerIId(panelHandeingabe.wsfKostentraeger
								.getIKey());
				positionDtoVK.setCLvposition(panelHandeingabe.wtfLVPosition
						.getText());
				positionDtoVK.setBRabattsatzuebersteuert(Helper
						.boolean2Short(false));
				positionDtoVK
						.setMwstsatzIId((Integer) panelHandeingabe.wcoMwstsatz
								.getKeyOfSelectedItem());
				positionDtoVK.setBMwstsatzuebersteuert(Helper
						.boolean2Short(false));
				positionDtoVK.setFZusatzrabattsatz(panelHandeingabe
						.getWnfZusatzrabattsatz().getDouble());
				positionDtoVK.setNEinzelpreis(panelHandeingabe.wnfEinzelpreis
						.getBigDecimal());
				positionDtoVK
						.setNNettoeinzelpreis(panelHandeingabe.wnfNettopreis
								.getBigDecimal());
				positionDtoVK
						.setNBruttoeinzelpreis(panelHandeingabe.wnfBruttopreis
								.getBigDecimal());
			}

			if (positionDto instanceof AgstklpositionDto) {
				AgstklpositionDto agstklpositionDto = (AgstklpositionDto) positionDto;
				if (panelHandeingabe.wnfAufschlagBetrag != null) {
					agstklpositionDto
							.setNAufschlag(panelHandeingabe.wnfAufschlagBetrag
									.getBigDecimal());
				}
				if (panelHandeingabe.wnfAufschlagProzent != null) {
					agstklpositionDto
							.setFAufschlag(panelHandeingabe.wnfAufschlagProzent
									.getDouble());
				}
				if (panelHandeingabe.wnfGesamtpreisMitAufschlag != null) {
					agstklpositionDto
							.setNNettogesamtmitaufschlag(panelHandeingabe.wnfGesamtpreisMitAufschlag
									.getBigDecimal());
					agstklpositionDto
							.setBAufschlaggesamtFixiert(panelHandeingabe.wnfGesamtpreisMitAufschlag
									.getWrbFixNumber().getShort());
				}

			}

		}
	}

	protected final void dto2Components(BelegpositionDto positionDto,
			String locPartner) throws Throwable {
		String positionsart = positionDto.getPositionsartCNr();
		// Positionsart in die Combobox setzen
		wcoPositionsart.setKeyOfSelectedItem(positionsart);
		if (positionsart.equals(LocaleFac.POSITIONSART_BETRIFFT)) {
			panelBetreff.wtfBetreff.setText(positionDto.getCBez());
		} else if (positionsart.equals(LocaleFac.POSITIONSART_URSPRUNGSLAND)) {
			panelUrsprung.wtfUrsprung.setText(positionDto.getCBez());
		} else if (positionsart
				.equalsIgnoreCase(LocaleFac.POSITIONSART_TEXTEINGABE)) {
			panelTexteingabe.getLpEditor()
					.setText(positionDto.getXTextinhalt());
		} else if (positionsart
				.equalsIgnoreCase(LocaleFac.POSITIONSART_TEXTBAUSTEIN)) {
			panelTextbaustein.oMediastandardDto = DelegateFactory
					.getInstance()
					.getMediaDelegate()
					.mediastandardFindByPrimaryKey(
							positionDto.getMediastandardIId());
			panelTextbaustein.dto2Components();
		} else if (positionsart
				.equalsIgnoreCase(LocaleFac.POSITIONSART_POSITION)) {
			if (LocaleFac.POSITIONBEZ_ENDE.equals(positionDto.getCZusatzbez())) { // SK
				// :
				// equals
				// umgedreht
				// gegen
				// NULL
				// -
				// Pointer
				// (if
				// (positionDto.getCZusatzbez().equals(LocaleFac.POSITIONBEZ_ENDE
				// )) {
				wcoPositiontyp.setVisible(false);
				wlaPositiontyp.setVisible(false);
				wtfPositionBez.setVisible(false);
				wlaPositionBez.setVisible(false);
				wlaPauschalposition.setVisible(false);
				wnfPauschalpositionpreis.setVisible(false);
			} else {
				wcoPositiontyp.setVisible(true);
				wlaPositiontyp.setVisible(true);
				wtfPositionBez.setVisible(true);
				wlaPositionBez.setVisible(true);
				wlaPauschalposition.setVisible(true);
				wnfPauschalpositionpreis.setVisible(true);

				if (positionDto instanceof BelegpositionVerkaufDto) {
					BelegpositionVerkaufDto positionDtoVK = (BelegpositionVerkaufDto) positionDto;
					wcoPositiontyp.setKeyOfSelectedItem(positionDtoVK
							.getTypCNr());
				}
			}
			if (positionDto.getCBez() != null) {
				wtfPositionBez.setText(positionDto.getCBez());
			}
			wcoPositionZBez.setKeyOfSelectedItem(positionDto.getCZusatzbez());
		} else if (positionsart.equalsIgnoreCase(LocaleFac.POSITIONSART_IDENT)) {
			// Artikel holen.
			ArtikelDto oArtikelDto = DelegateFactory.getInstance()
					.getArtikelDelegate()
					.artikelFindByPrimaryKey(positionDto.getArtikelIId());

			// PJ15459 Wenn Materialzuschlag vorhanden, dann Zeile anzeigen
			panelArtikel.setVisibleZeileMaterialzuschlag(false);
			if (oArtikelDto.getMaterialIId() != null) {
				if (DelegateFactory
						.getInstance()
						.getMaterialDelegate()
						.materialzuschlagFindAktuellenzuschlag(
								oArtikelDto.getMaterialIId()) != null) {
					panelArtikel.setVisibleZeileMaterialzuschlag(true);
				}
			} else if (positionDto.getNMaterialzuschlag() != null
					&& positionDto.getNMaterialzuschlag().doubleValue() != 0) {
				panelArtikel.setVisibleZeileMaterialzuschlag(true);
			}

			panelArtikel.setArtikelDto(oArtikelDto);
			// Artikel Goto Key setzen
			panelArtikel.wbuArtikelauswahl.setOKey(oArtikelDto.getIId());
			// die beste uebersetzung holen.
			if (locPartner == null
					|| locPartner.equals(LPMain.getTheClient()
							.getLocUiAsString())) {
				// passt genau
			} else {
				// 1. schaun, obs eine Uebersetzung im Partner-Locale gibt.
				ArtikelsprDto sprDtoLocPartner = DelegateFactory
						.getInstance()
						.getArtikelDelegate()
						.artikelsprFindByArtikelIIdLocaleCNrOhneExc(
								oArtikelDto.getIId(), locPartner);
				if (sprDtoLocPartner != null) {
					oArtikelDto.setArtikelsprDto(sprDtoLocPartner);
				}
				// 2. im Locale des Mandanten.
				else {
					ArtikelsprDto sprDtoLocMandant = DelegateFactory
							.getInstance()
							.getArtikelDelegate()
							.artikelsprFindByArtikelIIdLocaleCNrOhneExc(
									oArtikelDto.getIId(),
									LPMain.getTheClient()
											.getLocMandantAsString());
					if (sprDtoLocMandant != null) {
						oArtikelDto.setArtikelsprDto(sprDtoLocMandant);
					}
					// 2. im Locale des Konzerns.
					else {
						ArtikelsprDto sprDtoLocKonzern = DelegateFactory
								.getInstance()
								.getArtikelDelegate()
								.artikelsprFindByArtikelIIdLocaleCNrOhneExc(
										oArtikelDto.getIId(),
										LPMain.getTheClient()
												.getLocKonzernAsString());
						if (sprDtoLocKonzern != null) {
							oArtikelDto.setArtikelsprDto(sprDtoLocKonzern);
						}
					}
				}
			}
			/**
			 * @todo das Flag muss anders gesteuert werden.
			 */
			// die Artikelbezeichnung kann uebersteuert sein
			if (positionDto.getCBez() == null) {
				if (panelArtikel.getArtikelDto().getArtikelsprDto() != null) {
					panelArtikel.wtfBezeichnung.setText(panelArtikel
							.getArtikelDto().getArtikelsprDto().getCBez());
				}
			} else {
				panelArtikel.wtfBezeichnung.setText(positionDto.getCBez());
			}

			// die Artikelzusatzbezeichnung kann uebersteuert sein
			if (positionDto.getCZusatzbez() == null) {
				if (panelArtikel.getArtikelDto().getArtikelsprDto() != null) {
					panelArtikel.wtfZusatzbezeichnung.setText(panelArtikel
							.getArtikelDto().getArtikelsprDto().getCZbez());
				}
			} else {
				panelArtikel.wtfZusatzbezeichnung.setText(positionDto
						.getCZusatzbez());
			}
			if (positionDto.getXTextinhalt() != null)
				wefText.setText(positionDto.getXTextinhalt());

			// Menge / Einheit
			panelArtikel.wnfMenge.setBigDecimal(positionDto.getNMenge());
			panelArtikel.wcoEinheit.setKeyOfSelectedItem(positionDto
					.getEinheitCNr());
			// Preise und Rabatte fuer Verkaufsbelege.
			if (positionDto instanceof BelegpositionVerkaufDto) {
				BelegpositionVerkaufDto positionDtoVK = (BelegpositionVerkaufDto) positionDto;
				panelArtikel.wsfKostentraeger.setKey(positionDtoVK
						.getKostentraegerIId());
				panelArtikel.wtfLVPosition.setText(positionDtoVK
						.getCLvposition());

				if (positionDto instanceof LieferscheinpositionDto) {
					panelArtikel.setUebersteuertesLagerIId(((LieferscheinpositionDto) positionDto)
									.getLagerIId());
				}

				boolean bAktuell = DelegateFactory
						.getInstance()
						.getMandantDelegate()
						.pruefeObMwstsatzNochAktuell(positionDtoVK,
								panelArtikel.getTBelegdatumMwstsatz());
				if (bAktuell == false) {
					// Mwstbetrag neu berechnen
					MwstsatzDto mwstSatzDto = DelegateFactory
							.getInstance()
							.getMandantDelegate()
							.mwstsatzFindByPrimaryKey(
									positionDtoVK.getMwstsatzIId());
					BigDecimal mwstBetrag = Helper.getProzentWert(positionDtoVK
							.getNNettoeinzelpreis(),
							new BigDecimal(mwstSatzDto.getFMwstsatz()),
							Defaults.getInstance()
									.getIUINachkommastellenPreiseVK());

					positionDtoVK.setNBruttoeinzelpreis(positionDtoVK
							.getNNettoeinzelpreis().add(mwstBetrag));
				}
				panelArtikel.wcoMwstsatz.setKeyOfSelectedItem(positionDtoVK
						.getMwstsatzIId());

				// Rabattbetrag ermitteln.
				BigDecimal bdRabattbetrag = null;

				BigDecimal nZusatzRabattsumme = new BigDecimal(0);

				if (!Helper.short2boolean(positionDtoVK
						.getBNettopreisuebersteuert())) {
					bdRabattbetrag = positionDtoVK
							.getNEinzelpreis()
							.multiply(
									new BigDecimal(positionDtoVK
											.getFRabattsatz().doubleValue()))
							.movePointLeft(2);

					if (positionDtoVK.getFZusatzrabattsatz() != null) {
						nZusatzRabattsumme = positionDtoVK
								.getNEinzelpreis()
								.subtract(bdRabattbetrag)
								.multiply(
										new BigDecimal(positionDtoVK
												.getFZusatzrabattsatz()
												.doubleValue())
												.movePointLeft(2));
					}

				} else {
					BigDecimal materialzuschlag = new BigDecimal(0);
					if (positionDtoVK.getNMaterialzuschlag() != null) {
						materialzuschlag = positionDtoVK.getNMaterialzuschlag();
					}

					bdRabattbetrag = positionDtoVK.getNEinzelpreis().subtract(
							positionDtoVK.getNNettoeinzelpreis().subtract(
									materialzuschlag));
					if (positionDtoVK.getFZusatzrabattsatz() != null) {
						nZusatzRabattsumme = positionDtoVK
								.getNNettoeinzelpreis()
								.subtract(materialzuschlag)
								.divide(new BigDecimal(
										1 - ((positionDtoVK
												.getFZusatzrabattsatz()
												.doubleValue() / 100))), 4,
										BigDecimal.ROUND_HALF_EVEN)
								.multiply(
										new BigDecimal(positionDtoVK
												.getFZusatzrabattsatz()
												.doubleValue() / 100));
					}

				}

				// Mwstbetrag ermitteln.
				BigDecimal bdMwstbetrag = positionDtoVK.getNBruttoeinzelpreis()
						.subtract(positionDtoVK.getNNettoeinzelpreis());

				// posvkpf: 7 den Verkaufspreis hinterlegen und anzeigen
				VerkaufspreisDto vkDto = new VerkaufspreisDto();
				vkDto.einzelpreis = positionDtoVK.getNEinzelpreis();
				if (!Helper.short2boolean(positionDtoVK
						.getBNettopreisuebersteuert())) {
					vkDto.rabattsumme = bdRabattbetrag;
				} else {
					vkDto.rabattsumme = bdRabattbetrag
							.subtract(nZusatzRabattsumme);
				}

				vkDto.nettopreis = positionDtoVK.getNNettoeinzelpreis();
				vkDto.mwstsumme = bdMwstbetrag;
				vkDto.bdMaterialzuschlag = positionDtoVK.getNMaterialzuschlag();
				vkDto.bruttopreis = positionDtoVK.getNBruttoeinzelpreis();
				vkDto.rabattsatz = positionDtoVK.getFRabattsatz();
				vkDto.setDdZusatzrabattsatz(positionDtoVK
						.getFZusatzrabattsatz());
				// poszusatzrabatt: 3 den Zusatzrabattsatz setzen
				if (positionDtoVK.getFZusatzrabattsatz() != null) {
					vkDto.setNZusatzrabattsumme(nZusatzRabattsumme);
				}
				vkDto.mwstsatzIId = positionDtoVK.getMwstsatzIId();
				((PanelPositionenArtikelVerkauf) panelArtikel).verkaufspreisDtoInZielwaehrung = vkDto;
				((PanelPositionenArtikelVerkauf) panelArtikel)
						.verkaufspreisDto2components();
				if (panelArtikel.wcoVerleih != null) {
					panelArtikel.wcoVerleih.setKeyOfSelectedItem(positionDtoVK
							.getVerleihIId());
				}

			}
			if (positionDto instanceof AnfragepositionDto) {
			} else if (positionDto instanceof AgstklpositionDto) {

				AgstklpositionDto agstklpositionDto = (AgstklpositionDto) positionDto;
				if (panelArtikel.wnfAufschlagProzent != null) {
					panelArtikel.wnfAufschlagProzent
							.setDouble(agstklpositionDto.getFAufschlag());

				}
				if (panelArtikel.wnfAufschlagBetrag != null) {

					panelArtikel.wnfAufschlagBetrag
							.setBigDecimal(agstklpositionDto.getNAufschlag());

					if (Helper.short2boolean(agstklpositionDto
							.getBAufschlaggesamtFixiert()) == false) {
						panelArtikel.wnfAufschlagBetrag.getWrbFixNumber()
								.setSelected(true);
					}

				}
				if (panelArtikel.wnfGesamtpreisMitAufschlag != null) {

					panelArtikel.wnfGesamtpreisMitAufschlag
							.setBigDecimal(agstklpositionDto
									.getNNettogesamtmitaufschlag());
					if (Helper.short2boolean(agstklpositionDto
							.getBAufschlaggesamtFixiert()) == true) {
						panelArtikel.wnfGesamtpreisMitAufschlag
								.getWrbFixNumber().setSelected(true);
					}

				}
			} else {
				if (Helper.short2boolean(positionDto
						.getBNettopreisuebersteuert()))
					panelArtikel.wnfNettopreis.getWrbFixNumber().setSelected(
							true);
			}

		} else if (positionsart
				.equalsIgnoreCase(LocaleFac.POSITIONSART_HANDEINGABE)) {
			// Handartikel holen.
			ArtikelDto artikelDto = DelegateFactory.getInstance()
					.getArtikelDelegate()
					.artikelFindByPrimaryKey(positionDto.getArtikelIId());
			if (artikelDto.getArtikelsprDto() != null) {
				panelHandeingabe.wtfBezeichnung.setText(artikelDto
						.getArtikelsprDto().getCBez());
				panelHandeingabe.wtfZusatzbezeichnung.setText(artikelDto
						.getArtikelsprDto().getCZbez());
			} else {
				panelHandeingabe.wtfBezeichnung.setText(artikelDto.getCNr());
			}
			// Menge / Einheit
			panelHandeingabe.wnfMenge.setBigDecimal(positionDto.getNMenge());
			panelHandeingabe.wcoEinheit.setKeyOfSelectedItem(positionDto
					.getEinheitCNr());
			// Preise und Rabatte fuer Verkaufsbelege.
			if (positionDto instanceof BelegpositionVerkaufDto) {
				BelegpositionVerkaufDto positionDtoVK = (BelegpositionVerkaufDto) positionDto;
				panelHandeingabe.wsfKostentraeger.setKey(positionDtoVK
						.getKostentraegerIId());
				panelHandeingabe.wtfLVPosition.setText(positionDtoVK
						.getCLvposition());
				panelHandeingabe.getWnfRabattsatz().setDouble(
						positionDtoVK.getFRabattsatz());
				panelHandeingabe.wcoMwstsatz.setKeyOfSelectedItem(positionDtoVK
						.getMwstsatzIId());
				panelHandeingabe.wnfEinzelpreis.setBigDecimal(positionDtoVK
						.getNEinzelpreis());
				// Rabattbetrag ermitteln.
				BigDecimal bdRabattbetrag = positionDtoVK
						.getNEinzelpreis()
						.multiply(
								new BigDecimal(positionDtoVK.getFRabattsatz()
										.doubleValue())).movePointLeft(2);
				panelHandeingabe.wnfRabattsumme.setBigDecimal(bdRabattbetrag);

				// den Zusatzrabattsatz setzen
				if (positionDtoVK.getFZusatzrabattsatz() != null) {
					panelHandeingabe.wnfZusatzrabattsatz
							.setDouble(positionDtoVK.getFZusatzrabattsatz());
					BigDecimal nZusatzrabattsumme = positionDtoVK
							.getNEinzelpreis()
							.subtract(bdRabattbetrag)
							.multiply(
									new BigDecimal(positionDtoVK
											.getFZusatzrabattsatz()
											.doubleValue())).movePointLeft(2);
					panelHandeingabe.wnfZusatzrabattsumme
							.setBigDecimal(nZusatzrabattsumme);
				}
				panelHandeingabe.wnfNettopreis.setBigDecimal(positionDtoVK
						.getNNettoeinzelpreis());
				panelHandeingabe.wnfMwstsumme.setBigDecimal(positionDtoVK
						.getNBruttoeinzelpreis().subtract(
								positionDtoVK.getNNettoeinzelpreis()));
				panelHandeingabe.wnfBruttopreis.setBigDecimal(positionDtoVK
						.getNBruttoeinzelpreis());
			}

			if (positionDto instanceof AgstklpositionDto) {

				AgstklpositionDto agstklpositionDto = (AgstklpositionDto) positionDto;
				if (panelHandeingabe.wnfAufschlagProzent != null) {
					panelHandeingabe.wnfAufschlagProzent
							.setDouble(agstklpositionDto.getFAufschlag());

				}
				if (panelHandeingabe.wnfAufschlagBetrag != null) {

					panelHandeingabe.wnfAufschlagBetrag
							.setBigDecimal(agstklpositionDto.getNAufschlag());

					if (Helper.short2boolean(agstklpositionDto
							.getBAufschlaggesamtFixiert()) == false) {
						panelHandeingabe.wnfAufschlagBetrag.getWrbFixNumber()
								.setSelected(true);
					}

				}
				if (panelHandeingabe.wnfGesamtpreisMitAufschlag != null) {

					panelHandeingabe.wnfGesamtpreisMitAufschlag
							.setBigDecimal(agstklpositionDto
									.getNNettogesamtmitaufschlag());
					if (Helper.short2boolean(agstklpositionDto
							.getBAufschlaggesamtFixiert()) == true) {
						panelHandeingabe.wnfGesamtpreisMitAufschlag
								.getWrbFixNumber().setSelected(true);
					}

				}
			}
		} else if (positionsart
				.equals(LocaleFac.POSITIONSART_INTELLIGENTE_ZWISCHENSUMME)) {
			panelIntZwischensumme.dto2Components(positionDto);
		}
	}

	protected final String getPositionsartCNr() {
		return (String) wcoPositionsart.getKeyOfSelectedItem();
	}

	protected final void setPositionsarten(Map<String, String> mapPositionsarten) {
		wcoPositionsart.setMap(mapPositionsarten);
	}

	protected final String getPositiontyp() {
		return (String) wcoPositiontyp.getKeyOfSelectedItem();
	}

	protected final String getPositionBez() {
		return (String) wcoPositionZBez.getKeyOfSelectedItem();
	}

	public WrapperComboBox getWcoPositionTyp() {
		return wcoPositiontyp;
	}

	public WrapperComboBox getWcoPositionZBez() {
		return wcoPositionZBez;
	}

}

class PanelPositionen2_jComboBoxPositionsart_actionAdapter implements
		java.awt.event.ActionListener {
	private PanelPositionen2 adaptee;

	PanelPositionen2_jComboBoxPositionsart_actionAdapter(
			PanelPositionen2 adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		adaptee.jComboBoxPositionsart_actionPerformed();
	}
}

class PanelPositionen2_jComboBoxPositionZBez_actionAdapter implements
		java.awt.event.ActionListener {
	private PanelPositionen2 adaptee;

	PanelPositionen2_jComboBoxPositionZBez_actionAdapter(
			PanelPositionen2 adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		adaptee.jComboBoxPositionZBez_actionPerformed();
	}
}

class PanelPositionen2_wnfPauschalposition_focusAdapter extends
		java.awt.event.FocusAdapter {
	private PanelPositionen2 adaptee;

	PanelPositionen2_wnfPauschalposition_focusAdapter(PanelPositionen2 adaptee) {
		this.adaptee = adaptee;
	}

	public void focusLost(FocusEvent e) {
		adaptee.wnfPauschalposition_focusLost(e);
	}
}
