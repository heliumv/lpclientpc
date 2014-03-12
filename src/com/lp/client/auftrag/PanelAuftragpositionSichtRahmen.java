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
package com.lp.client.auftrag;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.math.BigDecimal;

import com.lp.client.frame.HelperClient;
import com.lp.client.frame.LockStateValue;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.PanelPositionen2;
import com.lp.client.frame.component.PanelPositionenArtikelVerkauf;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.artikel.service.VerkaufspreisDto;
import com.lp.server.auftrag.service.AuftragServiceFac;
import com.lp.server.auftrag.service.AuftragpositionDto;
import com.lp.server.bestellung.service.BestellpositionFac;
import com.lp.server.system.service.LockMeDto;

@SuppressWarnings("static-access")
/**
 * <p>In diesem Detailfenster bekommt man einen Blick auf alle mengenbehafteten
 * Rahmenpositionen eines Rahmenauftrags.</p>
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 * <p>Erstellungsdatum 11.11.05</p>
 * <p> </p>
 * @author Uli Walch
 * @version $Revision: 1.6 $
 */
public class PanelAuftragpositionSichtRahmen extends PanelPositionen2 {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private InternalFrameAuftrag intFrame = null;
	private TabbedPaneAuftrag tpAuftrag = null;

	private WrapperLabel wlaGeliefertArtikel = null;
	private WrapperLabel wlaOffenimrahmenArtikel = null;

	private WrapperLabel wlaGeliefertDieserAbrufArtikel = null;
	protected WrapperNumberField wnfGeliefertDieserAbrufArtikel = null;

	private WrapperLabel wlaGeliefertDieserAbrufHand = null;
	protected WrapperNumberField wnfGeliefertDieserAbrufHand = null;

	protected WrapperNumberField wnfGeliefertArtikel = null;
	protected WrapperNumberField wnfOffenimrahmenArtikel = null;

	private WrapperLabel wlaGeliefertHand = null;
	private WrapperLabel wlaOffenimrahmenHand = null;

	protected WrapperNumberField wnfGeliefertHand = null;
	protected WrapperNumberField wnfOffenimrahmenHand = null;

	protected AuftragpositionDto auftragpositionDto = null;

	private LockMeDto lockMeAuftrag = null;

	public PanelAuftragpositionSichtRahmen(InternalFrame internalFrame,
			String add2TitleI, Object key) throws Throwable {
		super(internalFrame, add2TitleI, key,
				PanelPositionen2.TYP_PANELPREISEINGABE_ARTIKELVERKAUF);

		intFrame = (InternalFrameAuftrag) internalFrame;
		tpAuftrag = intFrame.getTabbedPaneAuftrag();

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

		// einfache Preisauswahl ueber Dialog deaktivieren
		((PanelPositionenArtikelVerkauf) panelArtikel)
				.remove(((PanelPositionenArtikelVerkauf) panelArtikel).wbuPreisauswahl);

		((PanelPositionenArtikelVerkauf) panelArtikel).add(
				((PanelPositionenArtikelVerkauf) panelArtikel).wlaEinzelpreis,
				new GridBagConstraints(5, 2, 1, 1, 0.0, 0.0,
						GridBagConstraints.WEST, GridBagConstraints.BOTH,
						new Insets(10, 2, 2, 2), 0, 0));

		// zusaetzliche Felder am PanelArtikel
		panelArtikel.wlaMenge.setText(LPMain.getInstance().getTextRespectUISPr(
				"auft.rahmenmenge"));
		HelperClient.setDefaultsToComponent(panelArtikel.wlaMenge, 85);
		panelArtikel.wnfMenge.setMinimumValue(1); // Eingabe muss > 0 sein

		wlaGeliefertArtikel = new WrapperLabel();
		wlaGeliefertArtikel.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.abgerufen.gesamt"));

		wlaGeliefertDieserAbrufArtikel = new WrapperLabel();
		wlaGeliefertDieserAbrufArtikel.setText(LPMain.getInstance()
				.getTextRespectUISPr("auftrag.rahmen.aktuellerabruf"));

		wlaGeliefertDieserAbrufHand = new WrapperLabel();
		wlaGeliefertDieserAbrufHand.setText(LPMain.getInstance()
				.getTextRespectUISPr("auftrag.rahmen.aktuellerabruf"));

		wlaOffenimrahmenArtikel = new WrapperLabel();
		wlaOffenimrahmenArtikel.setText(LPMain.getInstance()
				.getTextRespectUISPr("lp.offenimrahmen"));

		wnfGeliefertArtikel = new WrapperNumberField();
		wnfGeliefertDieserAbrufArtikel = new WrapperNumberField();
		wnfGeliefertDieserAbrufHand = new WrapperNumberField();

		wnfOffenimrahmenArtikel = new WrapperNumberField();

		panelArtikel.add(wlaGeliefertDieserAbrufArtikel,
				new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(2, 2, 2, 2), 0, 0));
		panelArtikel.add(wnfGeliefertDieserAbrufArtikel,
				new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(2, 2, 2, 2), 0, 0));
		panelArtikel.add(wlaOffenimrahmenArtikel, new GridBagConstraints(0, 4,
				1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		panelArtikel.add(wnfOffenimrahmenArtikel, new GridBagConstraints(1, 4,
				1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		panelArtikel.add(wlaGeliefertArtikel, new GridBagConstraints(0, 5, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 100, 0));

		panelArtikel.add(wnfGeliefertArtikel, new GridBagConstraints(1, 5, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		// zusaetzliche Felder am PanelHandeingabe
		panelHandeingabe.wlaMenge.setText(LPMain
				.getTextRespectUISPr("auft.rahmenmenge"));

		wlaGeliefertHand = new WrapperLabel();
		wlaGeliefertHand.setText(LPMain
				.getTextRespectUISPr("lp.abgerufen.gesamt"));

		wlaOffenimrahmenHand = new WrapperLabel();
		wlaOffenimrahmenHand.setText(LPMain
				.getTextRespectUISPr("lp.offenimrahmen"));

		wnfGeliefertHand = new WrapperNumberField();

		wnfOffenimrahmenHand = new WrapperNumberField();

		panelHandeingabe.add(wlaGeliefertDieserAbrufHand,
				new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(2, 2, 2, 2), 0, 0));
		panelHandeingabe.add(wnfGeliefertDieserAbrufHand,
				new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(2, 2, 2, 2), 0, 0));

		panelHandeingabe.add(wlaGeliefertHand, new GridBagConstraints(0, 5, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		panelHandeingabe.add(wnfGeliefertHand, new GridBagConstraints(1, 5, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		panelHandeingabe.add(wlaOffenimrahmenHand, new GridBagConstraints(0, 4,
				1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		panelHandeingabe.add(wnfOffenimrahmenHand, new GridBagConstraints(1, 4,
				1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		// UW 21.06.06 keine VKPF
		panelArtikel.wnfMenge
				.removeFocusListener(((PanelPositionenArtikelVerkauf) panelArtikel).wnfMengeFocusListener);
	}

	private void initPanel() throws Throwable {
		/**
		 * @todo MB->VF warum bestellpositionarten?
		 */
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
		panelArtikel.wnfRabattsumme.setActivatable(false);
		panelArtikel.getWnfZusatzrabattsumme().setActivatable(false);
		panelArtikel.wnfNettopreis.setActivatable(false);
		panelArtikel.wnfMwstsumme.setActivatable(false);
		panelArtikel.wnfBruttopreis.setActivatable(false);
		panelArtikel.wbuArtikelauswahl.setActivatable(false);
		panelArtikel.getWtfArtikel().setActivatable(false);
		// panelArtikel.wtfArtikel.setMandatoryField(false);
		panelArtikel.wtfBezeichnung.setActivatable(false);
		panelArtikel.wtfZusatzbezeichnung.setActivatable(false);
		panelArtikel.wcoEinheit.setActivatable(false);
		panelArtikel.getWnfRabattsatz().setActivatable(false);
		// in der Basisklasse wurde der Zusatzrabattsatz als Mandatory definiert
		// panelArtikel.getWnfZusatzrabattsatz().setMandatoryField(false);
		panelArtikel.getWnfZusatzrabattsatz().setActivatable(false);
		panelArtikel.wcoMwstsatz.setActivatable(false);

		wnfGeliefertArtikel.setActivatable(false);
		wnfOffenimrahmenHand.setActivatable(false);
		wnfOffenimrahmenArtikel.setActivatable(false);
		wnfGeliefertHand.setActivatable(false);
		wnfGeliefertDieserAbrufArtikel.setActivatable(false);
		wnfGeliefertDieserAbrufHand.setActivatable(false);

		panelHandeingabe.wnfEinzelpreis.setActivatable(false);
		panelHandeingabe.wnfRabattsumme.setActivatable(false);
		panelHandeingabe.getWnfZusatzrabattsumme().setActivatable(false);
		panelHandeingabe.wnfNettopreis.setActivatable(false);
		panelHandeingabe.wnfMwstsumme.setActivatable(false);
		panelHandeingabe.wnfBruttopreis.setActivatable(false);
		panelHandeingabe.wtfBezeichnung.setActivatable(false);
		panelHandeingabe.wtfZusatzbezeichnung.setActivatable(false);
		panelHandeingabe.wcoEinheit.setActivatable(false);
		panelHandeingabe.getWnfRabattsatz().setActivatable(false);
		panelHandeingabe.getWnfZusatzrabattsatz().setActivatable(false);
		panelHandeingabe.wcoMwstsatz.setActivatable(false);

		// es gilt die waehrung des lieferscheins
		panelArtikel.setWaehrungCNr(tpAuftrag.getAuftragDto()
				.getCAuftragswaehrung());
		panelHandeingabe.setWaehrungCNr(tpAuftrag.getAuftragDto()
				.getCAuftragswaehrung());
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {
		super.eventYouAreSelected(false);
		setTBelegdatumMwstsatz(tpAuftrag.getAuftragDto().getTBelegdatum());
		// den gesamten Inhalt zuruecksetzen, ausloeser war ev. ein discard
		setDefaults();
		// position neu einlesen, ausloeser war ev. ein refresh
		Object pkPosition = getKeyWhenDetailPanel();

		if (pkPosition != null && !pkPosition.equals(LPMain.getLockMeForNew())) {
			// Position neu einlesen.
			auftragpositionDto = DelegateFactory.getInstance()
					.getAuftragpositionDelegate()
					.auftragpositionFindByPrimaryKey((Integer) pkPosition);

			dto2Components();

			// auch der zugehoerige Rahmenauftrag muss gelockt werden
			lockMeAuftrag = new LockMeDto(HelperClient.LOCKME_AUFTRAG,
					auftragpositionDto.getBelegIId() + "", LPMain
							.getInstance().getCNrUser());

			if (tpAuftrag.getAuftragDto().getAuftragartCNr() != null
					&& tpAuftrag.getAuftragDto().getAuftragartCNr()
							.equals(AuftragServiceFac.AUFTRAGART_ABRUF)) {
				panelArtikel.wlaMenge.setText(LPMain
						.getTextRespectUISPr("lp.mengeabruf"));
				panelHandeingabe.wlaMenge.setText(LPMain
						.getTextRespectUISPr("lp.mengeabruf"));
			} else if (tpAuftrag.getAuftragDto().getAuftragartCNr() != null
					&& tpAuftrag.getAuftragDto().getAuftragartCNr()
							.equals(AuftragServiceFac.AUFTRAGART_RAHMEN)) {
				panelArtikel.wlaMenge.setText(LPMain
						.getTextRespectUISPr("auft.rahmenmenge"));
				panelHandeingabe.wlaMenge.setText(LPMain
						.getTextRespectUISPr("auft.rahmenmenge"));
			}
		}

		tpAuftrag.enablePanelsNachBitmuster();
	}

	protected void dto2Components() throws Throwable {
		// 1. Behandlung der trivialen Positionsarten.
		super.dto2Components(auftragpositionDto, tpAuftrag.getKundeAuftragDto()
				.getPartnerDto().getLocaleCNrKommunikation());
		// 2. Weiter mit den anderen.
		String positionsart = auftragpositionDto.getPositionsartCNr();

		if (positionsart
				.equalsIgnoreCase(AuftragServiceFac.AUFTRAGPOSITIONART_IDENT)) {
			if (tpAuftrag.getAuftragDto().getAuftragartCNr()
					.equals(AuftragServiceFac.AUFTRAGART_RAHMEN)) {
				panelArtikel.wnfMenge.setBigDecimal(auftragpositionDto
						.getNMenge());
			} else if (tpAuftrag.getAuftragDto().getAuftragartCNr()
					.equals(AuftragServiceFac.AUFTRAGART_ABRUF)) {
				panelArtikel.wnfMenge.setText("");
			}

			BigDecimal abrufmengeAktuellerAuftrag = new BigDecimal(0);
			AuftragpositionDto[] abrufPositionenDtos = DelegateFactory
					.getInstance()
					.getAuftragpositionDelegate()
					.auftragpositionFindByAuftragpositionIIdRahmenpositionOhneExc(
							auftragpositionDto.getIId());
			for (int i = 0; i < abrufPositionenDtos.length; i++) {
				if (abrufPositionenDtos[i].getNMenge() != null
						&& abrufPositionenDtos[i].getBelegIId().equals(
								tpAuftrag.getAuftragDto().getIId())) {
					abrufmengeAktuellerAuftrag = abrufmengeAktuellerAuftrag
							.add(abrufPositionenDtos[i].getNMenge());
				}
			}

			wnfGeliefertDieserAbrufArtikel
					.setBigDecimal(abrufmengeAktuellerAuftrag);

			wnfOffenimrahmenArtikel.setBigDecimal(auftragpositionDto
					.getNOffeneRahmenMenge());
			if (auftragpositionDto.getNOffeneRahmenMenge() != null) {
				wnfGeliefertArtikel.setBigDecimal(auftragpositionDto
						.getNMenge().subtract(
								auftragpositionDto.getNOffeneRahmenMenge()));
			}
			((PanelPositionenArtikelVerkauf) panelArtikel).verkaufspreisDtoInZielwaehrung = DelegateFactory
					.getInstance()
					.getVkPreisfindungDelegate()
					.berechneVerkaufspreis(
							auftragpositionDto.getNEinzelpreis(),
							auftragpositionDto.getFRabattsatz(),
							auftragpositionDto.getFZusatzrabattsatz(),
							auftragpositionDto.getMwstsatzIId(),
							auftragpositionDto.getNMaterialzuschlag());

			((PanelPositionenArtikelVerkauf) panelArtikel)
					.verkaufspreisDto2components();
		} else if (positionsart
				.equalsIgnoreCase(AuftragServiceFac.AUFTRAGPOSITIONART_HANDEINGABE)) {
			if (tpAuftrag.getAuftragDto().getAuftragartCNr()
					.equals(AuftragServiceFac.AUFTRAGART_RAHMEN)) {
				panelHandeingabe.wnfMenge.setBigDecimal(auftragpositionDto
						.getNMenge());
			} else if (tpAuftrag.getAuftragDto().getAuftragartCNr()
					.equals(AuftragServiceFac.AUFTRAGART_ABRUF)) {
				panelHandeingabe.wnfMenge.setText("");
			}

			BigDecimal abrufmengeAktuellerAuftrag = new BigDecimal(0);
			AuftragpositionDto[] abrufPositionenDtos = DelegateFactory
					.getInstance()
					.getAuftragpositionDelegate()
					.auftragpositionFindByAuftragpositionIIdRahmenpositionOhneExc(
							auftragpositionDto.getIId());
			for (int i = 0; i < abrufPositionenDtos.length; i++) {
				if (abrufPositionenDtos[i].getNMenge() != null
						&& abrufPositionenDtos[i].getBelegIId().equals(
								tpAuftrag.getAuftragDto().getIId())) {
					abrufmengeAktuellerAuftrag = abrufmengeAktuellerAuftrag
							.add(abrufPositionenDtos[i].getNMenge());
				}
			}

			wnfGeliefertDieserAbrufHand
					.setBigDecimal(abrufmengeAktuellerAuftrag);

			wnfOffenimrahmenHand.setBigDecimal(auftragpositionDto
					.getNOffeneRahmenMenge());
			wnfGeliefertHand.setBigDecimal(auftragpositionDto.getNMenge()
					.subtract(auftragpositionDto.getNOffeneRahmenMenge()));

			VerkaufspreisDto verkaufspreisDtoInZielwaehrung = DelegateFactory
					.getInstance()
					.getVkPreisfindungDelegate()
					.berechneVerkaufspreis(
							auftragpositionDto.getNEinzelpreis(),
							auftragpositionDto.getFRabattsatz(),
							auftragpositionDto.getFZusatzrabattsatz(),
							auftragpositionDto.getMwstsatzIId(),
							auftragpositionDto.getNMaterialzuschlag());

			panelHandeingabe.wnfEinzelpreis
					.setBigDecimal(verkaufspreisDtoInZielwaehrung.einzelpreis);
			panelHandeingabe.getWnfRabattsatz().setDouble(
					verkaufspreisDtoInZielwaehrung.rabattsatz);
			panelHandeingabe.wnfRabattsumme
					.setBigDecimal(verkaufspreisDtoInZielwaehrung.rabattsumme);
			panelHandeingabe.getWnfZusatzrabattsatz().setDouble(
					verkaufspreisDtoInZielwaehrung.getDdZusatzrabattsatz());
			panelHandeingabe.getWnfZusatzrabattsumme().setBigDecimal(
					verkaufspreisDtoInZielwaehrung.getNZusatzrabattsumme());
			panelHandeingabe.wnfNettopreis
					.setBigDecimal(verkaufspreisDtoInZielwaehrung.nettopreis);
			panelHandeingabe.wcoMwstsatz
					.setKeyOfSelectedItem(verkaufspreisDtoInZielwaehrung.mwstsatzIId);
			panelHandeingabe.wnfMwstsumme
					.setBigDecimal(verkaufspreisDtoInZielwaehrung.mwstsumme);
			panelHandeingabe.wnfBruttopreis
					.setBigDecimal(verkaufspreisDtoInZielwaehrung.bruttopreis);
		}
	}

	protected void eventActionUpdate(ActionEvent aE, boolean bNeedNoUpdateI)
			throws Throwable {
		if (tpAuftrag.getAuftragDto().getAuftragartCNr()
				.equals(AuftragServiceFac.AUFTRAGART_ABRUF)) {
			super.eventActionUpdate(aE, false);

			// Vorschlagswert fuer die abzurufende Menge setzen
			if (auftragpositionDto.getPositionsartCNr().equals(
					AuftragServiceFac.AUFTRAGPOSITIONART_IDENT)) {
				if (auftragpositionDto.getNOffeneRahmenMenge() != null)
					if (auftragpositionDto.getNOffeneRahmenMenge().intValue() == 0)
						DialogFactory.showModalDialog(
								LPMain.getInstance().getTextRespectUISPr(
										"lp.warning"),
								LPMain.getInstance().getTextRespectUISPr(
										"auft.offenrahmenmengenull"));
					else
						panelArtikel.wnfMenge.setBigDecimal(auftragpositionDto
								.getNOffeneRahmenMenge());
			} else if (auftragpositionDto.getPositionsartCNr().equals(
					AuftragServiceFac.AUFTRAGPOSITIONART_HANDEINGABE)) {
				panelHandeingabe.wnfMenge.setBigDecimal(auftragpositionDto
						.getNOffeneRahmenMenge());
			}
		} else {
			DialogFactory.showModalDialog(LPMain.getInstance()
					.getTextRespectUISPr("lp.warning"), LPMain.getInstance()
					.getTextRespectUISPr("auft.keinabrufauftrag"));
		}
	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {
		if (allMandatoryFieldsSetDlg()) {
			calculateFields();
			checkLockedDlg();
			if (lockMeAuftrag != null) {
				checkLockedDlg(lockMeAuftrag);
			}

			if (auftragpositionDto.getPositionsartCNr().equals(
					AuftragServiceFac.AUFTRAGPOSITIONART_IDENT)
					|| auftragpositionDto.getPositionsartCNr().equals(
							AuftragServiceFac.AUFTRAGPOSITIONART_HANDEINGABE)) {
				saveAbrufposition(e, false);
			}
		}
	}

	/**
	 * Bevor eine Position im Abrufauftrag gespeichert werden kann, muessen
	 * verschiedene Vorbedingungen geprueft werden.
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
				AuftragServiceFac.AUFTRAGPOSITIONART_IDENT)
				&& panelArtikel.wnfMenge.getDouble().doubleValue() > wnfOffenimrahmenArtikel
						.getDouble().doubleValue()) {
			bDiePositionSpeichern = false;
		} else if (getPositionsartCNr().equals(
				AuftragServiceFac.AUFTRAGPOSITIONART_HANDEINGABE)
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
			// wird die bestehende Position aktualisiert
			AuftragpositionDto abrufpositionDto = DelegateFactory
					.getInstance()
					.getAuftragpositionDelegate()
					.auftragpositionFindByAuftragIIdAuftragpositionIIdRahmenposition(
							tpAuftrag.getAuftragDto().getIId(),
							auftragpositionDto.getIId());

			if (abrufpositionDto == null) {
				// es gibt noch keine Position, eine neue erfassen
				abrufpositionDto = (AuftragpositionDto) auftragpositionDto
						.clone();
				abrufpositionDto.setBelegIId(tpAuftrag.getAuftragDto()
						.getIId());
				abrufpositionDto
						.setAuftragpositionIIdRahmenposition(auftragpositionDto
								.getIId());

				if (getPositionsartCNr().equals(
						AuftragServiceFac.AUFTRAGPOSITIONART_IDENT)) {
					abrufpositionDto.setNMenge(panelArtikel.wnfMenge
							.getBigDecimal());
					abrufpositionDto.setNOffeneMenge(panelArtikel.wnfMenge
							.getBigDecimal());
				} else if (getPositionsartCNr().equals(
						AuftragServiceFac.AUFTRAGPOSITIONART_HANDEINGABE)) {
					abrufpositionDto.setNMenge(panelHandeingabe.wnfMenge
							.getBigDecimal());
					abrufpositionDto.setNOffeneMenge(panelArtikel.wnfMenge
							.getBigDecimal());
				}

				abrufpositionDto
						.setAuftragpositionstatusCNr(AuftragServiceFac.AUFTRAGPOSITIONSTATUS_OFFEN);

				DelegateFactory.getInstance().getAuftragRahmenAbrufDelegate()
						.createAbrufpositionZuRahmenposition(abrufpositionDto);
			} else {
				// zusaetzlich wird die eingegebene Menge abgebucht -> das
				// Handling
				// in Bezug auf die bestehende Position geschieht am Server
				if (getPositionsartCNr().equals(
						AuftragServiceFac.AUFTRAGPOSITIONART_IDENT)) {
					abrufpositionDto.setNMenge(panelArtikel.wnfMenge
							.getBigDecimal());
				} else if (getPositionsartCNr().equals(
						AuftragServiceFac.AUFTRAGPOSITIONART_HANDEINGABE)) {
					abrufpositionDto.setNMenge(panelHandeingabe.wnfMenge
							.getBigDecimal());
				}

				DelegateFactory
						.getInstance()
						.getAuftragRahmenAbrufDelegate()
						.updateAbrufpositionZuRahmenpositionSichtRahmen(
								abrufpositionDto);
			}
			DelegateFactory
					.getInstance()
					.getAuftragRahmenAbrufDelegate()
					.pruefeUndSetzeRahmenstatus(
							auftragpositionDto.getBelegIId());

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

		if (lockMeAuftrag != null) {
			// Zugehoerige Rahmenbestellung locken.
			super.lock(lockMeAuftrag);
		}
	}

	protected void eventActionUnlock(ActionEvent e) throws Throwable {

		// Lieferschein unlocken.
		super.eventActionUnlock(e);

		if (lockMeAuftrag != null) {
			// Zugehoerige Rahmenbestellung unlocken.
			super.unlock(lockMeAuftrag);
		}
	}

	public LockStateValue getLockedstateDetailMainKey() throws Throwable {

		LockStateValue l = super.getLockedstateDetailMainKey();
		if (l.getIState() == LOCK_IS_NOT_LOCKED && lockMeAuftrag != null) {
			l.setIState(getLockedByWerWas(lockMeAuftrag));
		}

		return l;
	}

	protected void eventActionDiscard(ActionEvent e) throws Throwable {
		super.eventActionDiscard(e);

		// auch die zugehoerige Rahmenbestellung muss gelockt werden
		lockMeAuftrag = new LockMeDto(HelperClient.LOCKME_AUFTRAG,
				auftragpositionDto.getBelegIId() + "", LPMain.getInstance()
						.getCNrUser());

		tpAuftrag.enablePanelsNachBitmuster();
	}

	protected void eventActionRefresh(ActionEvent e, boolean bNeedNoRefreshI)
			throws Throwable {
		super.eventActionRefresh(e, bNeedNoRefreshI);

		tpAuftrag.enablePanelsNachBitmuster();
	}
}
