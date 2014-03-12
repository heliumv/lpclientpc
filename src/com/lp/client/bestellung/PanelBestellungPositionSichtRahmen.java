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
package com.lp.client.bestellung;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.math.BigDecimal;
import java.sql.Timestamp;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.LockStateValue;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelPositionen2;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.bestellung.service.BestellpositionDto;
import com.lp.server.bestellung.service.BestellpositionFac;
import com.lp.server.bestellung.service.BestellungFac;
import com.lp.server.system.service.LockMeDto;

@SuppressWarnings("static-access")
/**
 * <p>In diesem Detailfenster bekommt man einen Blick auf alle mengenbehafteten
 * Rahmenpositionen einer Rahmenbestellung.</p>
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 * <p>Erstellungsdatum 02.08.05</p>
 * <p> </p>
 * @author Josef Erlinger
 * @version $Revision: 1.7 $
 */
public class PanelBestellungPositionSichtRahmen extends PanelPositionen2 {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private InternalFrameBestellung intFrame = null;
	private TabbedPaneBestellung tPBes = null;

	private WrapperLabel wlaGeliefertArtikel = null;
	private WrapperLabel wlaOffenimrahmenArtikel = null;

	protected WrapperNumberField wnfGeliefertArtikel = null;
	protected WrapperNumberField wnfOffenimrahmenArtikel = null;

	private WrapperLabel wlaGeliefertHand = null;
	private WrapperLabel wlaOffenimrahmenHand = null;

	private WrapperLabel wlaWEPnochNichtVerbuchteMengenArtikel = null;
	private WrapperNumberField wnfWEPnochNichtVerbuchteMengenArtikel = null;

	private WrapperLabel wlaWEPnochNichtVerbuchteMengenHand = null;
	private WrapperNumberField wnfWEPnochNichtVerbuchteMengenHand = null;

	protected WrapperNumberField wnfGeliefertHand = null;
	protected WrapperNumberField wnfOffenimrahmenHand = null;

	protected BestellpositionDto bestellpositionDto = null;

	private LockMeDto lockMeRahmenbestellung = null;

	public PanelBestellungPositionSichtRahmen(InternalFrame internalFrame,
			String add2TitleI, Object key) throws Throwable {

		super(internalFrame, add2TitleI, key,
				PanelPositionen2.TYP_PANELPREISEINGABE_ARTIKELEINKAUF);

		intFrame = (InternalFrameBestellung) internalFrame;
		tPBes = intFrame.getTabbedPaneBestellung();

		jbInit();
		initComponents();
		initPanel();
	}

	private void jbInit() throws Throwable {
		// braucht nur refresh, save und aendern
		resetToolsPanel();

		// zusaetzliche buttons
		String[] aWhichButtonIUse = { ACTION_UPDATE, ACTION_SAVE,
				ACTION_DISCARD };
		enableToolsPanelButtons(aWhichButtonIUse);

		// zusaetzliche Felder am PanelArtikel
		wlaWEPnochNichtVerbuchteMengenArtikel = new WrapperLabel();
		wlaWEPnochNichtVerbuchteMengenArtikel.setText(LPMain.getInstance()
				.getTextRespectUISPr("bes.offenelieferungen"));

		wnfWEPnochNichtVerbuchteMengenArtikel = new WrapperNumberField();
		wnfWEPnochNichtVerbuchteMengenArtikel.setActivatable(false);

		panelArtikel.wlaMenge.setText(LPMain.getTextRespectUISPr("bes.menge"));
		panelArtikel.wlaMenge.setMaximumSize(new Dimension(85, Defaults
				.getInstance().getControlHeight()));
		panelArtikel.wlaMenge.setMinimumSize(new Dimension(85, Defaults
				.getInstance().getControlHeight()));
		panelArtikel.wlaMenge.setPreferredSize(new Dimension(85, Defaults
				.getInstance().getControlHeight()));

		panelArtikel.wnfMenge.setMinimumValue(1); // Eingabe muss > 0 sein
		panelArtikel.wnfMenge.setMaximumValue(new BigDecimal(
				BestellpositionFac.MAX_D_MENGE));

		wlaGeliefertArtikel = new WrapperLabel();
		wlaGeliefertArtikel.setText(LPMain.getTextRespectUISPr("lp.abgerufen"));

		wlaOffenimrahmenArtikel = new WrapperLabel();
		wlaOffenimrahmenArtikel.setText(LPMain
				.getTextRespectUISPr("lp.offenimrahmen"));

		wnfGeliefertArtikel = new WrapperNumberField();
		wnfGeliefertArtikel.setActivatable(false);
		wnfGeliefertArtikel.setMinimumValue(0);
		wnfGeliefertArtikel.setMaximumValue(new BigDecimal(
				BestellpositionFac.MAX_D_MENGE));

		wnfOffenimrahmenArtikel = new WrapperNumberField();
		wnfOffenimrahmenArtikel.setActivatable(false);
		// wnfOffenimrahmenArtikel.setMinimumValue(0);
		wnfOffenimrahmenArtikel.setMaximumValue(new BigDecimal(
				BestellpositionFac.MAX_D_MENGE));

		panelArtikel.add(wlaGeliefertArtikel, new GridBagConstraints(0, 4, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		panelArtikel.add(wlaOffenimrahmenArtikel, new GridBagConstraints(0, 3,
				1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		panelArtikel.add(wnfGeliefertArtikel, new GridBagConstraints(1, 4, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		panelArtikel.add(wnfOffenimrahmenArtikel, new GridBagConstraints(1, 3,
				1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		panelArtikel.add(wlaWEPnochNichtVerbuchteMengenArtikel,
				new GridBagConstraints(0, 5, 1, 1, 0.1, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(2, 2, 2, 2), 0, 0));

		panelArtikel.add(wnfWEPnochNichtVerbuchteMengenArtikel,
				new GridBagConstraints(1, 5, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(2, 2, 2, 2), 0, 0));

		// zusaetzliche Felder am PanelHandeingabe

		wlaWEPnochNichtVerbuchteMengenHand = new WrapperLabel();
		wlaWEPnochNichtVerbuchteMengenHand.setText(LPMain.getInstance()
				.getTextRespectUISPr("bes.offenelieferungen"));

		wnfWEPnochNichtVerbuchteMengenHand = new WrapperNumberField();
		wnfWEPnochNichtVerbuchteMengenHand.setActivatable(false);

		panelHandeingabe.wlaMenge.setText(LPMain.getInstance()
				.getTextRespectUISPr("bes.menge"));

		wlaGeliefertHand = new WrapperLabel();
		wlaGeliefertHand.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.abgerufen"));

		wlaOffenimrahmenHand = new WrapperLabel();
		wlaOffenimrahmenHand.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.offenimrahmen"));

		wnfGeliefertHand = new WrapperNumberField();

		wnfOffenimrahmenHand = new WrapperNumberField();

		panelHandeingabe.add(wlaOffenimrahmenHand, new GridBagConstraints(0, 3,
				1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		panelHandeingabe.add(wlaGeliefertHand, new GridBagConstraints(0, 4, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		panelHandeingabe.add(wnfGeliefertHand, new GridBagConstraints(1, 4, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		panelHandeingabe.add(wnfOffenimrahmenHand, new GridBagConstraints(1, 3,
				1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		panelHandeingabe.add(wlaWEPnochNichtVerbuchteMengenHand,
				new GridBagConstraints(0, 6, 1, 1, 0.1, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(2, 2, 2, 2), 0, 0));

		panelHandeingabe.add(wnfWEPnochNichtVerbuchteMengenHand,
				new GridBagConstraints(1, 6, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(2, 2, 2, 2), 0, 0));

		add(getPanelStatusbar(), new GridBagConstraints(0, iZeile, 1, 1, 1.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));

		panelHandeingabe.setVisibleZeileZusatzrabattsumme(false);
		panelHandeingabe.setVisibleZeileMwstsumme(false);
		panelHandeingabe.setVisibleZeileBruttogesamtpreis(false);
	}

	private void initPanel() throws Throwable {
		// combobox Positionen in der UI Sprache des Benutzers fuellen
		setPositionsarten(DelegateFactory
				.getInstance()
				.getBestellungServiceDelegate()
				.getBestellpositionart(
						LPMain.getInstance().getTheClient().getLocUi()));
	}

	protected void setDefaults() throws Throwable {
		super.setDefaults();

		leereAlleFelder(this);

		wcoPositionsart.setActivatable(false);

		// default Positionsart ist Ident in der UI Sprache des Benutzers
		wcoPositionsart
				.setKeyOfSelectedItem(BestellpositionFac.BESTELLPOSITIONART_IDENT);

		// in dieser Ansicht kann nur die Menge geaendert werden
		panelArtikel.wnfEinzelpreis.setActivatable(false);
		panelArtikel.wnfEinzelpreis.setDependenceField(false);

		panelArtikel.wnfMaterialzuschlag.setActivatable(false);
		panelArtikel.wnfMaterialzuschlag.setDependenceField(false);

		panelArtikel.wnfRabattsumme.setActivatable(false);
		panelArtikel.wnfRabattsumme.setDependenceField(false);

		panelArtikel.wnfNettopreis.setActivatable(false);
		panelArtikel.wnfNettopreis.setDependenceField(false);

		panelArtikel.wbuArtikelauswahl.setActivatable(false);
		panelArtikel.getWtfArtikel().setActivatable(false);
		panelArtikel.wtfBezeichnung.setActivatable(false);
		panelArtikel.wtfZusatzbezeichnung.setActivatable(false);
		panelArtikel.wcoEinheit.setActivatable(false);

		panelArtikel.getWnfRabattsatz().setActivatable(false);
		panelArtikel.getWnfRabattsatz().setDependenceField(false);

		panelHandeingabe.wnfEinzelpreis.setActivatable(false);
		panelHandeingabe.wnfEinzelpreis.setDependenceField(false);

		panelHandeingabe.wnfRabattsumme.setActivatable(false);
		panelHandeingabe.wnfRabattsumme.setDependenceField(false);

		panelHandeingabe.wnfNettopreis.setActivatable(false);
		panelHandeingabe.wnfNettopreis.setDependenceField(false);

		panelHandeingabe.wtfBezeichnung.setActivatable(false);
		panelHandeingabe.wtfZusatzbezeichnung.setActivatable(false);
		panelHandeingabe.wcoEinheit.setActivatable(false);

		panelHandeingabe.getWnfRabattsatz().setActivatable(false);
		panelHandeingabe.getWnfRabattsatz().setDependenceField(false);

		wnfGeliefertHand.setActivatable(false);
		wnfOffenimrahmenHand.setActivatable(false);

		panelHandeingabe.setWaehrungCNr(tPBes.getBesDto().getWaehrungCNr());
		panelArtikel.setWaehrungCNr(tPBes.getBesDto().getWaehrungCNr());
	}

	private InternalFrameBestellung getInternalFrameBestellung() {
		return (InternalFrameBestellung) getInternalFrame();
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {
		super.eventYouAreSelected(false);
		setTBelegdatumMwstsatz(tPBes.getBesDto().getDBelegdatum());
		// den gesamten Inhalt zuruecksetzen, ausloeser war ev. ein discard
		setDefaults();
		// position neu einlesen, ausloeser war ev. ein refresh
		Object pkPosition = getKeyWhenDetailPanel();

		if (pkPosition != null && !pkPosition.equals(LPMain.getLockMeForNew())) {
			// Position neu einlesen.
			bestellpositionDto = DelegateFactory.getInstance()
					.getBestellungDelegate()
					.bestellpositionFindByPrimaryKey((Integer) pkPosition);

			dto2Components();

			// auch die zugehoerige Rahmenbestellung muss gelockt werden
			lockMeRahmenbestellung = new LockMeDto(
					HelperClient.LOCKME_BESTELLUNG,
					bestellpositionDto.getBestellungIId() + "", LPMain
							.getInstance().getCNrUser());
		}

		refreshMyComponents();

		tPBes.enablePanels(tPBes.getBesDto(), true);
	}

	private void dto2Components() throws Throwable {
		// 0. die Limits fuer das Mengenfeld muessen am anfang gesetzt werden.
		String positionsart = bestellpositionDto.getPositionsartCNr();
		if (positionsart
				.equalsIgnoreCase(BestellpositionFac.BESTELLPOSITIONART_IDENT)) {
			panelArtikel.wnfMenge.setMinimumValue(0);
			panelArtikel.wnfMenge.setMaximumValue(null);
		}

		// 1. Behandlung der trivialen Positionsarten.
		super.dto2Components(bestellpositionDto, tPBes.getLieferantDto()
				.getPartnerDto().getLocaleCNrKommunikation());

		// 2. Weiter mit den anderen.
		if (positionsart
				.equalsIgnoreCase(BestellpositionFac.BESTELLPOSITIONART_IDENT)) {
			if (tPBes.getBesDto().getBestellungartCNr()
					.equals(BestellungFac.BESTELLUNGART_RAHMENBESTELLUNG_C_NR)) {
				// Fuer die Rahmenbestellung handelt es sich um eine lesende
				// Sicht.
				panelArtikel.wnfMenge.setBigDecimal(bestellpositionDto
						.getNMenge());
				panelArtikel.wnfMenge.setMaximumValue(bestellpositionDto
						.getNMenge());
			} else if (tPBes.getBesDto().getBestellungartCNr()
					.equals(BestellungFac.BESTELLUNGART_ABRUFBESTELLUNG_C_NR)) {
				// Vorschlagswert fuer die neue Menge ist immer 1
				panelArtikel.wnfMenge.setBigDecimal(new BigDecimal(1));
				panelArtikel.wnfMenge.setMaximumValue(bestellpositionDto
						.getNOffeneMenge());
			}

			wnfOffenimrahmenArtikel.setBigDecimal(bestellpositionDto
					.getNOffeneMenge());

			wnfGeliefertArtikel.setBigDecimal(bestellpositionDto.getNMenge()
					.subtract(bestellpositionDto.getNOffeneMenge()));

			panelArtikel.getWnfRabattsatz().setDouble(
					bestellpositionDto.getDRabattsatz());
			panelArtikel.wnfEinzelpreis.setBigDecimal(bestellpositionDto
					.getNNettoeinzelpreis());
			panelArtikel.wnfMaterialzuschlag.setBigDecimal(bestellpositionDto
					.getNMaterialzuschlag());
			panelArtikel.wnfRabattsumme.setBigDecimal(bestellpositionDto
					.getNRabattbetrag());
			panelArtikel.wnfNettopreis.setBigDecimal(bestellpositionDto
					.getNNettogesamtpreis());

			this.wnfWEPnochNichtVerbuchteMengenArtikel.setBigDecimal(this
					.offeneLieferung(bestellpositionDto));

		} else if (bestellpositionDto.getBestellpositionartCNr()
				.equalsIgnoreCase(
						BestellpositionFac.BESTELLPOSITIONART_HANDEINGABE)) {
			if (tPBes.getBesDto().getBestellungartCNr()
					.equals(BestellungFac.BESTELLUNGART_RAHMENBESTELLUNG_C_NR)) {
				panelHandeingabe.wnfMenge.setBigDecimal(bestellpositionDto
						.getNMenge());
				panelHandeingabe.wnfMenge.setMaximumValue(bestellpositionDto
						.getNMenge());
			} else if (tPBes.getBesDto().getBestellungartCNr()
					.equals(BestellungFac.BESTELLUNGART_ABRUFBESTELLUNG_C_NR)) {
				// Vorschlagswert fuer die neue Menge ist immer 1
				panelHandeingabe.wnfMenge.setBigDecimal(new BigDecimal(1));
				panelHandeingabe.wnfMenge.setMaximumValue(bestellpositionDto
						.getNOffeneMenge());
			}

			wnfOffenimrahmenHand.setBigDecimal(bestellpositionDto
					.getNOffeneMenge());
			wnfGeliefertHand.setBigDecimal(bestellpositionDto.getNMenge()
					.subtract(bestellpositionDto.getNOffeneMenge()));

			panelHandeingabe.getWnfRabattsatz().setDouble(
					bestellpositionDto.getDRabattsatz());
			panelHandeingabe.wnfEinzelpreis.setBigDecimal(bestellpositionDto
					.getNNettoeinzelpreis());
			panelHandeingabe.wnfRabattsumme.setBigDecimal(bestellpositionDto
					.getNRabattbetrag());
			panelHandeingabe.wnfNettopreis.setBigDecimal(bestellpositionDto
					.getNNettogesamtpreis());

			wnfWEPnochNichtVerbuchteMengenHand.setBigDecimal(this
					.offeneLieferung(bestellpositionDto));
		}
	}

	private BigDecimal offeneLieferung(BestellpositionDto bestellpositionDto)
			throws ExceptionLP, Throwable {
		// offene Lieferung Wert setzen
		BestellpositionDto[] bestellposDto = DelegateFactory
				.getInstance()
				.getBestellungDelegate()
				.bestellpositionFindByBestellpositionIIdRahmenposition(
						bestellpositionDto.getIId());
		BigDecimal bdoffeneLieferMenge = new BigDecimal(0);
		for (int i = 0; i < bestellposDto.length; i++) {
			bdoffeneLieferMenge = bdoffeneLieferMenge.add(DelegateFactory
					.getInstance().getBestellungDelegate()
					.berechneOffeneMengePosition(bestellposDto[i].getIId()));
		}

		return bdoffeneLieferMenge;
	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {

		if (allMandatoryFieldsSetDlg()) {

			calculateFields();

			checkLockedDlg();

			if (lockMeRahmenbestellung != null) {
				checkLockedDlg(lockMeRahmenbestellung);
			}

			String besPosArt = bestellpositionDto.getBestellpositionartCNr();
			if (besPosArt.equals(BestellpositionFac.BESTELLPOSITIONART_IDENT)
					|| besPosArt
							.equals(BestellpositionFac.BESTELLPOSITIONART_HANDEINGABE)) {
				saveAbrufposition(e, false);
			} else {
				// Position neu anlegen / Status bleibt unberuehrt

				BestellpositionDto abrufbestellpositionDto = (BestellpositionDto) bestellpositionDto
						.clone();

				abrufbestellpositionDto.setCABKommentar(null);
				abrufbestellpositionDto.setCABNummer(null);
				abrufbestellpositionDto.setTAuftragsbestaetigungstermin(null);
				abrufbestellpositionDto.setBestellungIId(tPBes.getBesDto()
						.getIId());

				abrufbestellpositionDto
						.setIBestellpositionIIdRahmenposition(bestellpositionDto
								.getIId());
				abrufbestellpositionDto
						.setBestellpositionstatusCNr(BestellpositionFac.BESTELLPOSITIONSTATUS_OFFEN);

				DelegateFactory
						.getInstance()
						.getBestellungDelegate()
						.createAbrufbestellposition(
								abrufbestellpositionDto,
								BestellpositionFac.PREISPFLEGEARTIKELLIEFERANT_PREIS_UNVERAENDERT,
								null);
				super.eventActionSave(e, bNeedNoSaveI);
			}
		}
	}

	/**
	 * Bevor eine Position in der Abrufbestellung gespeichert werden kann,
	 * muessen verschiedene Vorbedingungen geprueft werden. Chargen- und SNR
	 * werden erst in den Wareneingaengen gehandelt.
	 * 
	 * @param e
	 *            Ereignis
	 * @param bNeedNoSaveI
	 *            flag
	 * @throws Throwable
	 *             Ausnahme
	 */
	protected void saveAbrufposition(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {
		boolean bDiePositionSpeichern = true;

		// die gewaehlte Menge kann nicht groesser als die offene Menge sein
		if (getPositionsartCNr().equals(
				BestellpositionFac.BESTELLPOSITIONART_IDENT)
				&& panelArtikel.wnfMenge.getDouble().doubleValue() > wnfOffenimrahmenArtikel
						.getDouble().doubleValue()) {
			bDiePositionSpeichern = false;
		} else if (getPositionsartCNr().equals(
				BestellpositionFac.BESTELLPOSITIONART_HANDEINGABE)
				&& panelHandeingabe.wnfMenge.getDouble().doubleValue() > wnfOffenimrahmenHand
						.getDouble().doubleValue()) {
			bDiePositionSpeichern = false;
		}

		if (!bDiePositionSpeichern) {
			DialogFactory.showModalDialog(
					LPMain.getInstance().getTextRespectUISPr("lp.warning"),
					LPMain.getInstance().getTextRespectUISPr(
							"ls.warning.geawehltemengegroesseroffenemenge"));
		}

		if (bDiePositionSpeichern) {
			// wenn es zu dieser Rahmenposition bereits eine Abrufposition gibt,
			// wird die bestehenden Position aktualisiert
			BestellpositionDto abrufbestellpositionDto = DelegateFactory
					.getInstance()
					.getBestellungDelegate()
					.bestellpositionFindByBestellungIIdBestellpositionIIdRahmenposition(
							tPBes.getBesDto().getIId(),
							bestellpositionDto.getIId());

			if (abrufbestellpositionDto == null) {
				// es gibt noch keine Position, eine neue erfassen
				abrufbestellpositionDto = (BestellpositionDto) bestellpositionDto
						.clone();
				// SK 14242
				abrufbestellpositionDto.setCABKommentar(null);
				abrufbestellpositionDto.setCABNummer(null);
				abrufbestellpositionDto.setTAuftragsbestaetigungstermin(null);
				abrufbestellpositionDto.setBestellungIId(tPBes.getBesDto()
						.getIId());
				abrufbestellpositionDto
						.setTUebersteuerterLiefertermin(new Timestamp(tPBes
								.getBesDto().getDLiefertermin().getTime()));
				abrufbestellpositionDto
						.setIBestellpositionIIdRahmenposition(bestellpositionDto
								.getIId());

				if (getPositionsartCNr().equals(
						BestellpositionFac.BESTELLPOSITIONART_IDENT)) {
					abrufbestellpositionDto.setNMenge(panelArtikel.wnfMenge
							.getBigDecimal());
				} else if (getPositionsartCNr().equals(
						BestellpositionFac.BESTELLPOSITIONART_HANDEINGABE)) {
					abrufbestellpositionDto.setNMenge(panelHandeingabe.wnfMenge
							.getBigDecimal());
				}

				abrufbestellpositionDto
						.setBestellpositionstatusCNr(BestellpositionFac.BESTELLPOSITIONSTATUS_OFFEN);

				DelegateFactory
						.getInstance()
						.getBestellungDelegate()
						.createAbrufbestellposition(
								abrufbestellpositionDto,
								BestellpositionFac.PREISPFLEGEARTIKELLIEFERANT_PREIS_UNVERAENDERT,
								null);
			}

			else {
				// es gibt in dieser Bestellung bereits eine Abrufposition zur
				// Rahmenbestellposition
				// zusaetzlich wird die eingegebene Menge abgebucht -> das
				// Handling
				// in Bezug auf die bestehende Position geschieht am Server
				if (getPositionsartCNr().equals(
						BestellpositionFac.BESTELLPOSITIONART_IDENT)) {
					abrufbestellpositionDto.setNMenge(panelArtikel.wnfMenge
							.getBigDecimal());
				} else if (getPositionsartCNr().equals(
						BestellpositionFac.BESTELLPOSITIONART_HANDEINGABE)) {
					abrufbestellpositionDto.setNMenge(panelHandeingabe.wnfMenge
							.getBigDecimal());
				}

				DelegateFactory
						.getInstance()
						.getBestellungDelegate()
						.updateAbrufbestellpositionSichtRahmen(
								abrufbestellpositionDto);
			}

			DelegateFactory
					.getInstance()
					.getBestellungDelegate()
					.refreshStatusWennAbgerufen(
							abrufbestellpositionDto.getBestellungIId());

			// buttons schalten
			super.eventActionSave(e, bNeedNoSaveI);
		}
	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_BESTELLUNG;
	}

	protected void eventActionLock(ActionEvent e) throws Throwable {

		// Lieferschein locken.
		super.eventActionLock(e);

		if (lockMeRahmenbestellung != null) {
			// Zugehoerige Rahmenbestellung locken.
			super.lock(lockMeRahmenbestellung);
		}
	}

	protected void eventActionUnlock(ActionEvent e) throws Throwable {

		// Lieferschein unlocken.
		super.eventActionUnlock(e);

		if (lockMeRahmenbestellung != null) {
			// Zugehoerige Rahmenbestellung unlocken.
			super.unlock(lockMeRahmenbestellung);
		}
	}

	public LockStateValue getLockedstateDetailMainKey() throws Throwable {

		LockStateValue l = super.getLockedstateDetailMainKey();
		if (l.getIState() == LOCK_IS_NOT_LOCKED
				&& lockMeRahmenbestellung != null) {
			l.setIState(getLockedByWerWas(lockMeRahmenbestellung));
		}

		return l;
	}

	protected void eventActionRefresh(ActionEvent e, boolean bNeedNoRefreshI)
			throws Throwable {

		super.eventActionRefresh(e, bNeedNoRefreshI);

		refreshMyComponents();
		tPBes.enablePanels(tPBes.getBesDto(), true);
	}

	private void refreshMyComponents() throws Throwable {

		// wenn die Bestellung gerade gelockt ist, die Artikeleingabefelder
		// freischalten
		if (getLockedstateDetailMainKey().getIState() == PanelBasis.LOCK_IS_LOCKED_BY_ME) {
			panelArtikel.setArtikelEingabefelderEditable(true);
		}

		setStatusbarPersonalIIdAnlegen(tPBes.getBesDto()
				.getPersonalIIdAnlegen());
		setStatusbarTAnlegen(tPBes.getBesDto().getTAnlegen());
		setStatusbarPersonalIIdAendern(tPBes.getBesDto()
				.getPersonalIIdAendern());
		setStatusbarTAendern(tPBes.getBesDto().getTAendern());
		setStatusbarStatusCNr(tPBes.getBesDto().getStatusCNr());

		String sBSArt = tPBes.getBesDto().getBestellungartCNr();
		boolean bDisableButtons = !sBSArt
				.equals(BestellungFac.BESTELLUNGART_ABRUFBESTELLUNG_C_NR)
				|| (tPBes.getBesDto().getStatusCNr()
						.equals(BestellungFac.BESTELLSTATUS_ERLEDIGT) && sBSArt
						.equals(BestellungFac.BESTELLUNGART_ABRUFBESTELLUNG_C_NR));

		if (bDisableButtons) {
			updateButtons(new LockStateValue(LOCK_ENABLE_REFRESHANDPRINT_ONLY));
		}
	}

	protected void eventActionDiscard(ActionEvent e) throws Throwable {
		super.eventActionDiscard(e);

		// auch die zugehoerige Rahmenbestellung muss gelockt werden
		/** @todo JO ??? PJ 4870 */
		lockMeRahmenbestellung = new LockMeDto(HelperClient.LOCKME_BESTELLUNG,
				bestellpositionDto.getBestellungIId() + "", LPMain
						.getInstance().getCNrUser());

		// if
		// (tpBestellung.getBestellungDto().getBestellungsstatusCNr().equals(BestellungFac.
		// BESTELLSTATUS_OFFEN) ||
		// tpBestellung.getBestellungDto().getBestellungsstatusCNr().
		// equals(BestellungFac.BESTELLSTATUS_BESTAETIGT)) {
		// tpBestellung.enableWareneingangPanels();
		// tpBestellung.enablePanels(null);
		// }
		// else {
		// tpBestellung.enableWE(false);
		// tpBestellung.enableWEP(false);
		// }
		// tpBestellung.enableSichtLieferantTermine();
	}

}
