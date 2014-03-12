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
package com.lp.client.frame.component.mengenstaffel;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.FocusEvent;
import java.beans.PropertyChangeEvent;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.EventObject;

import javax.swing.SwingConstants;

import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.WrapperDateField;
import com.lp.client.frame.component.WrapperIdentField;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.LagerDto;
import com.lp.server.artikel.service.VerkaufspreisDto;
import com.lp.server.artikel.service.VkPreisfindungEinzelverkaufspreisDto;
import com.lp.server.artikel.service.VkPreisfindungPreislisteDto;
import com.lp.server.partner.service.KundesokoDto;
import com.lp.server.partner.service.KundesokomengenstaffelDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.util.Helper;

@SuppressWarnings("static-access")
/*
 * <p>Erfassung von Mengenstaffeln fuer einen Artikel. <p>Copyright Logistik Pur
 * Software GmbH (c) 2004-2008</p> <p>Erstellungsdatum 07.07.2006</p> <p> </p>
 * 
 * @author Uli Walch
 * 
 * @version $Revision: 1.14 $
 */
public class PanelMengenstaffelArtikel extends PanelMengenstaffelBasis {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// Preisgueltigkeitsanzeige ab legt die VK-Basis fest
	protected WrapperLabel wlaPreisgueltigkeitsanzeigeab = null;
	protected WrapperDateField wdfPreisgueltigkeitsanzeigeab = null;
	protected Date datGueltigkeitsanzeigeab = null;
	int iPreisbasis = 0;
	/** Preisbasis als Bezugsgroesse fuer den Rabattsatz. */
	private BigDecimal nPreisbasis = null;

	private WrapperLabel wlaGestehungspreis = null;
	private WrapperNumberField wnfGestehungspreis = null;
	private WrapperLabel wlaWaehrungGestehungspreis = null;
	private WrapperLabel wlaMinverkaufspreis = null;
	private WrapperNumberField wnfMinverkaufspreis = null;
	private WrapperLabel wlaWaehrungMinverkaufspreis = null;

	private WrapperLabel wlaVkbasis = null;
	private WrapperNumberField wnfVkbasis = null;
	private WrapperLabel wlaVkbasiswaehrung = null;
	private WrapperLabel wlaVkbasisGueltigab = null;
	private WrapperDateField wdfVkbasisGueltigab = null;

	private WrapperLabel wlaKndArtBez = null;
	private WrapperTextField wtfKndArtBez = null;
	private WrapperTextField wtfKndArtZBez = null;

	private WrapperLabel wlaFixpreis = null;
	private WrapperLabel wlaBerechneterpreis = null;

	private WrapperNumberField wnfFixpreis = null;
	private WrapperLabel wlaFixpreiswaehrung = null;
	private WrapperNumberField wnfBerechneterpreis = null;
	private WrapperLabel wlaBerechneterpreiswaehrung = null;
	private boolean bMitUebersteuerbarenArtikelbezeichnungen = false;

	private WrapperIdentField wifArtikel = null;

	public PanelMengenstaffelArtikel(InternalFrame internalFrameI,
			String addTitle2I, boolean bMitUebersteuerbarenArtikelbezeichnungen)
			throws Throwable {
		super(internalFrameI, addTitle2I);
		this.bMitUebersteuerbarenArtikelbezeichnungen = bMitUebersteuerbarenArtikelbezeichnungen;
		jbInit();
	}

	private void jbInit() throws Throwable {
		getInternalFrame().addItemChangedListener(this);

		wlaGestehungspreis = new WrapperLabel(LPMain.getInstance()
				.getTextRespectUISPr("lp.gestehungspreis"));
		wnfGestehungspreis = new WrapperNumberField();
		wnfGestehungspreis.setActivatable(false);
		wnfGestehungspreis.setFractionDigits(iPreiseUINachkommastellen);
		wlaWaehrungGestehungspreis = new WrapperLabel(waehrungCNr);
		wlaWaehrungGestehungspreis
				.setHorizontalAlignment(SwingConstants.LEADING);

		wlaMinverkaufspreis = new WrapperLabel(LPMain.getInstance()
				.getTextRespectUISPr("lp.minverkaufspreisshort"));
		wnfMinverkaufspreis = new WrapperNumberField();
		wnfMinverkaufspreis.setActivatable(false);
		wnfMinverkaufspreis.setFractionDigits(iPreiseUINachkommastellen);
		wlaWaehrungMinverkaufspreis = new WrapperLabel(waehrungCNr);
		wlaWaehrungMinverkaufspreis
				.setHorizontalAlignment(SwingConstants.LEADING);

		// PJ 17390
		ParametermandantDto parameter = (ParametermandantDto) DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getParametermandant(ParameterFac.PARAMETER_PREISBASIS_VERKAUF,
						ParameterFac.KATEGORIE_KUNDEN,
						LPMain.getTheClient().getMandant());

		if (parameter.getCWert() != null) {

			iPreisbasis = (Integer) parameter.getCWertAsObject();
		}

		if (iPreisbasis == 0 || iPreisbasis == 2) {
			wlaVkbasis = new WrapperLabel(LPMain.getInstance()
					.getTextRespectUISPr("artikel.label.einzelverkaufspreis"));
		} else {
			wlaVkbasis = new WrapperLabel(LPMain.getInstance()
					.getTextRespectUISPr(
							"part.kundensoko.preisbasis.preisliste"));
		}

		wnfVkbasis = new WrapperNumberField();
		wnfVkbasis.setFractionDigits(iPreiseUINachkommastellen);
		wnfVkbasis.setActivatable(false);
		wlaVkbasiswaehrung = new WrapperLabel(waehrungCNr);
		wlaVkbasiswaehrung.setHorizontalAlignment(SwingConstants.LEADING);
		wlaVkbasisGueltigab = new WrapperLabel(LPMain.getInstance()
				.getTextRespectUISPr("vkpf.vkbasisgueltigab"));
		wdfVkbasisGueltigab = new WrapperDateField();
		wdfVkbasisGueltigab.setActivatable(false);

		wlaFixpreis = new WrapperLabel(LPMain.getInstance()
				.getTextRespectUISPr("lp.fixpreis"));
		wlaFixpreis.setHorizontalAlignment(SwingConstants.LEADING);
		wlaFixpreis.setVerticalAlignment(SwingConstants.BOTTOM);
		wlaBerechneterpreis = new WrapperLabel(LPMain.getInstance()
				.getTextRespectUISPr("lp.berechneterpreis"));
		wlaBerechneterpreis.setHorizontalAlignment(SwingConstants.LEADING);
		wlaBerechneterpreis.setVerticalAlignment(SwingConstants.BOTTOM);

		wnfFixpreis = new WrapperNumberField();
		wnfFixpreis.setFractionDigits(iPreiseUINachkommastellen);
		wnfFixpreis.setDependenceField(true);
		wnfFixpreis
				.addFocusListener(new PanelMengenstaffelArtikel_fixpreis_focusAdapter(
						this));

		wlaFixpreiswaehrung = new WrapperLabel(waehrungCNr);
		wlaFixpreiswaehrung.setHorizontalAlignment(SwingConstants.LEADING);

		wnfBerechneterpreis = new WrapperNumberField();
		wnfBerechneterpreis.setFractionDigits(iPreiseUINachkommastellen);
		wnfBerechneterpreis.setActivatable(false);
		wnfBerechneterpreis.setDependenceField(true);

		wlaBerechneterpreiswaehrung = new WrapperLabel(waehrungCNr);
		wlaBerechneterpreiswaehrung
				.setHorizontalAlignment(SwingConstants.LEADING);

		initMengeRabattsatz();

		wnfRabattsatz
				.addFocusListener(new PanelMengenstaffelArtikel_rabattsatz_focusAdapter(
						this));

		wifArtikel = new WrapperIdentField(getInternalFrame(), this);
		wifArtikel.addEinheitLabel(wlaMengeeinheit);

		addFormatierungszeile();

		iZeile++;
		add(wifArtikel.getWbuArtikel(), new GridBagConstraints(0, iZeile, 2, 1,
				0.0, 0.0, GridBagConstraints.NORTH,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		add(wifArtikel.getWtfIdent(), new GridBagConstraints(2, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.NORTH,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		add(wifArtikel.getWtfBezeichnung(), new GridBagConstraints(4, iZeile,
				4, 1, 1.0, 0.0, GridBagConstraints.NORTH,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		if (bMitUebersteuerbarenArtikelbezeichnungen) {
			iZeile++;
			wlaKndArtBez = new WrapperLabel(LPMain.getInstance()
					.getTextRespectUISPr("artikel.kundesoko.kndartbez"));

			add(wlaKndArtBez, new GridBagConstraints(2, iZeile, 2, 1, 0.0, 0.0,
					GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
					new Insets(2, 2, 2, 2), 0, 0));

			wtfKndArtBez = new WrapperTextField(40);
			wtfKndArtZBez = new WrapperTextField(25);
			add(wtfKndArtBez, new GridBagConstraints(4, iZeile, 4, 1, 1.0, 0.0,
					GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
					new Insets(2, 2, 2, 2), 0, 0));

		}

		iZeile++;

		add(wlaGestehungspreis, new GridBagConstraints(0, iZeile, 2, 1, 0.0,
				0.0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		add(wnfGestehungspreis, new GridBagConstraints(2, iZeile, 1, 1, 0.0,
				0.0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		add(wlaWaehrungGestehungspreis, new GridBagConstraints(3, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.NORTH,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		if (bMitUebersteuerbarenArtikelbezeichnungen) {
			add(wtfKndArtZBez,
					new GridBagConstraints(4, iZeile, 4, 1, 1.0, 0.0,
							GridBagConstraints.NORTH,
							GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2,
									2), 0, 0));
		}

		iZeile++;
		add(wlaMinverkaufspreis, new GridBagConstraints(0, iZeile, 2, 1, 0.0,
				0.0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		add(wnfMinverkaufspreis, new GridBagConstraints(2, iZeile, 1, 1, 0.0,
				0.0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		add(wlaWaehrungMinverkaufspreis, new GridBagConstraints(3, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.NORTH,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		wlaPreisgueltigkeitsanzeigeab = new WrapperLabel(LPMain.getInstance()
				.getTextRespectUISPr("vkpf.preisgueltigkeitsanzeigeab"));
		wdfPreisgueltigkeitsanzeigeab = new WrapperDateField();
		wdfPreisgueltigkeitsanzeigeab.getDisplay().addPropertyChangeListener(
				this);

		add(wlaPreisgueltigkeitsanzeigeab, new GridBagConstraints(0, iZeile, 6,
				1, 0.0, 0.0, GridBagConstraints.NORTH,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		add(wdfPreisgueltigkeitsanzeigeab, new GridBagConstraints(6, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.NORTH,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		add(wlaVkbasis, new GridBagConstraints(0, iZeile, 2, 1, 0.0, 0.0,
				GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		add(wnfVkbasis, new GridBagConstraints(2, iZeile, 1, 1, 0.0, 0.0,
				GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		add(wlaVkbasiswaehrung, new GridBagConstraints(3, iZeile, 1, 1, 0.0,
				0.0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		add(wlaVkbasisGueltigab, new GridBagConstraints(4, iZeile, 2, 1, 0.0,
				0.0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		add(wdfVkbasisGueltigab, new GridBagConstraints(6, iZeile, 1, 1, 0.0,
				0.0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		add(wlaMenge, new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0,
				GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		add(wlaFixpreis, new GridBagConstraints(2, iZeile, 1, 1, 0.0, 0.0,
				GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		add(wlaRabattsatz, new GridBagConstraints(4, iZeile, 1, 1, 0.0, 0.0,
				GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		add(wlaBerechneterpreis, new GridBagConstraints(6, iZeile, 1, 1, 0.0,
				0.0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		add(wnfMenge, new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0,
				GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		add(wlaMengeeinheit, new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0,
				GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		add(wnfFixpreis, new GridBagConstraints(2, iZeile, 1, 1, 0.0, 0.0,
				GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		add(wlaFixpreiswaehrung, new GridBagConstraints(3, iZeile, 1, 1, 0.0,
				0.0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		add(wnfRabattsatz, new GridBagConstraints(4, iZeile, 1, 1, 0.0, 0.0,
				GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		add(wlaRabattsatzprozent, new GridBagConstraints(5, iZeile, 1, 1, 0.0,
				0.0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		add(wnfBerechneterpreis, new GridBagConstraints(6, iZeile, 1, 1, 0.0,
				0.0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		add(wlaBerechneterpreiswaehrung, new GridBagConstraints(7, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.NORTH,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		addZeileGueltigVonBis(iZeile);
	}

	public void setMandatoryFields(boolean bMandatoryI) {
		super.setMandatoryFields(bMandatoryI);

		wdfPreisgueltigkeitsanzeigeab.setMandatoryField(bMandatoryI);
		wifArtikel.getWtfIdent().setMandatoryField(bMandatoryI);
		wnfMenge.setMandatoryField(bMandatoryI);
		wnfRabattsatz.setMandatoryField(bMandatoryI);

		// wnfBerechneterpreis.setMandatoryField(bMandatoryI);
	}

	protected void setDefaults() throws Throwable {
		super.setDefaults();

		datGueltigkeitsanzeigeab = Helper.cutDate(new Date(System
				.currentTimeMillis()));

		wdfPreisgueltigkeitsanzeigeab.setDate(datGueltigkeitsanzeigeab);

		wifArtikel.setArtikelDto(new ArtikelDto());

		wnfRabattsatz.setDouble(new Double(0)); // wird mit 0 initialisiert

	}

	public void setLabels(String waehrungCNr) {
		boolean bEnable = true;

		wnfRabattsatz.setEditable(bEnable);
		wnfRabattsatz.setMandatoryField(bEnable);

		wlaWaehrungGestehungspreis.setText(waehrungCNr);
		wlaWaehrungMinverkaufspreis.setText(waehrungCNr);
		wlaBerechneterpreiswaehrung.setText(waehrungCNr);
		wlaVkbasiswaehrung.setText(waehrungCNr);
		wlaFixpreiswaehrung.setText(waehrungCNr);
	}

	public void artikelDto2Components(ArtikelDto artikelDtoI) throws Throwable {
		if (artikelDtoI != null && artikelDtoI.getIId() != null) {
			wifArtikel.setArtikelDto(DelegateFactory.getInstance()
					.getArtikelDelegate()
					.artikelFindByPrimaryKey(artikelDtoI.getIId()));

			if (iPreisbasis == 0 || iPreisbasis == 2) {

				nPreisbasis = DelegateFactory
						.getInstance()
						.getVkPreisfindungDelegate()
						.ermittlePreisbasis(artikelDtoI.getIId(),
								datGueltigkeitsanzeigeab, null, waehrungCNr);
			} else {
				nPreisbasis = DelegateFactory
						.getInstance()
						.getVkPreisfindungDelegate()
						.ermittlePreisbasis(artikelDtoI.getIId(),
								datGueltigkeitsanzeigeab,
								getKundenpreislisteIId(), waehrungCNr);
			}

			// Gestehungspreis des Artikels am Hauptlager des Mandanten
			LagerDto hauptlagerDto = DelegateFactory.getInstance()
					.getLagerDelegate().getHauptlagerDesMandanten();

			BigDecimal nGestehungspreis = DelegateFactory
					.getInstance()
					.getLagerDelegate()
					.getGemittelterGestehungspreisEinesLagers(
							artikelDtoI.getIId(), hauptlagerDto.getIId());

			wnfGestehungspreis
					.setBigDecimal(berechnewertInKundenWaehrung(nGestehungspreis));

			// den minimalen Verkaufspreis (in Mandantenwaehrung) des Artikels
			// auf dem Hauptlager anzeigen
			BigDecimal bdMinverkaufspreis = new BigDecimal(0);

			try {
				bdMinverkaufspreis = DelegateFactory
						.getInstance()
						.getLagerDelegate()
						.getMindestverkaufspreis(
								artikelDtoI.getIId(),
								DelegateFactory.getInstance()
										.getLagerDelegate()
										.getHauptlagerDesMandanten().getIId(),
								new BigDecimal(1));
			} catch (Throwable t) {
				// wenn der minVerkaufspreis nicht gefunden wird, z.B. der
				// Artikel liegt nicht am Hauptlager -> 0 anzeigen
			}

			wnfMinverkaufspreis
					.setBigDecimal(berechnewertInKundenWaehrung(bdMinverkaufspreis));

			if (iPreisbasis == 0) {

				// die passende Verkaufspreisbasis anzeigen
				verkaufspreisbasisDto2comp(DelegateFactory
						.getInstance()
						.getVkPreisfindungDelegate()
						.getArtikeleinzelverkaufspreis(
								wifArtikel.getArtikelDto().getIId(),
								datGueltigkeitsanzeigeab, waehrungCNr));
			} else {

				vkPreisfindungPreislisteDto2com(DelegateFactory
						.getInstance()
						.getVkPreisfindungDelegate()
						.getAktuellePreislisteByArtikelIIdPreislisteIId(
								wifArtikel.getArtikelDto().getIId(),
								getKundenpreislisteIId(),
								datGueltigkeitsanzeigeab, waehrungCNr));
			}
		}
	}

	private void vkPreisfindungPreislisteDto2com(
			VkPreisfindungPreislisteDto preisDto) throws Throwable {
		// werte zuruecksetzen
		wnfVkbasis.setBigDecimal(null);
		wdfVkbasisGueltigab.setDate(null);
		if (preisDto != null && preisDto.getIId() != null) {
			// werte anzeigen

			if (preisDto.getNArtikelfixpreis() != null) {
				wnfVkbasis.setBigDecimal(preisDto.getNArtikelfixpreis());
			} else {
				if (preisDto.getNArtikelstandardrabattsatz() != null) {

					VkPreisfindungEinzelverkaufspreisDto preisBasisDto = DelegateFactory
							.getInstance()
							.getVkPreisfindungDelegate()
							.getArtikeleinzelverkaufspreis(
									wifArtikel.getArtikelDto().getIId(),
									datGueltigkeitsanzeigeab, waehrungCNr);

					BigDecimal bdRabattsumme = preisBasisDto
							.getNVerkaufspreisbasis().multiply(
									preisDto.getNArtikelstandardrabattsatz()
											.movePointLeft(2));
					wnfVkbasis.setBigDecimal(preisBasisDto
							.getNVerkaufspreisbasis().subtract(bdRabattsumme));
				}
			}

			wdfVkbasisGueltigab.setDate(preisDto.getTPreisgueltigab());
		}
	}

	private void verkaufspreisbasisDto2comp(
			VkPreisfindungEinzelverkaufspreisDto preisDto) throws Throwable {
		if (preisDto != null && preisDto.getIId() != null) {
			// werte anzeigen
			wnfVkbasis.setBigDecimal(preisDto.getNVerkaufspreisbasis());
			wdfVkbasisGueltigab.setDate(preisDto
					.getTVerkaufspreisbasisgueltigab());
		} else {
			// werte zuruecksetzen
			wnfVkbasis.setBigDecimal(null);
			wdfVkbasisGueltigab.setDate(null);
		}
	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		super.eventItemchanged(eI);
		ItemChangedEvent e = (ItemChangedEvent) eI;

		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {

			if (e.getSource().equals(wifArtikel)) {

				wifArtikel.setArtikelDto(DelegateFactory
						.getInstance()
						.getArtikelDelegate()
						.artikelFindByPrimaryKey(
								wifArtikel.getArtikelDto().getIId()));

				artikelDto2Components(wifArtikel.getArtikelDto());
			}
		}
	}

	public String baueTitel() {
		String sTitel = "";

		if (wifArtikel.getArtikelDto().getArtikelsprDto() != null
				&& wifArtikel.getArtikelDto().getArtikelsprDto().getCBez() != null) {
			sTitel += " | "
					+ wifArtikel.getArtikelDto().getArtikelsprDto().getCBez();
		} else if (wifArtikel.getArtikelDto().getCNr() != null) {
			sTitel += " | " + wifArtikel.getArtikelDto().getCNr();
		}

		return sTitel;
	}

	/**
	 * Fixpreis uebersteuert einen ev. vorhandenen Rabattsatz.
	 * 
	 * @param e
	 *            FocusEvent
	 * @throws Throwable
	 */
	public void fixpreis_focusLost(FocusEvent e) throws Throwable {
		if (e.getSource().equals(wnfFixpreis)) {
			// Hinweis, falls es einen Rabattsatz gab, Rabattsatz auf 0 setzen
			if (wnfRabattsatz.getDouble().doubleValue() != 0) {
				wnfRabattsatz.setDouble(new Double(0));
			}
		}

		berechneArtikelpreis(e);
	}

	/**
	 * Fixpreis uebersteuert einen ev. vorhandenen Rabattsatz.
	 * 
	 * @param e
	 *            FocusEvent
	 * @throws Throwable
	 */
	public void rabattsatz_focusLost(FocusEvent e) throws Throwable {
		if (e.getSource().equals(wnfRabattsatz)) {
			// Hinweis, falls es einen Fixpreis gibt, Rabattsatz auf 0 setzen
			if (wnfFixpreis.getDouble() != null
					&& wnfRabattsatz.getDouble().doubleValue() != 0) {
				DialogFactory.showModalDialog(
						LPMain.getInstance().getTextRespectUISPr("lp.hint"),
						LPMain.getInstance().getTextRespectUISPr(
								"vkpf.hint.fixpreisueberschreibtrabattsatz"));

				wnfRabattsatz.setDouble(new Double(0));
			}
		}

		berechneArtikelpreis(e);
	}

	private void berechneArtikelpreis(FocusEvent e) throws Throwable {
		boolean bEventBehandelt = false;

		// Source ist wnfFixpreis oder wnfRabattsatz
		if (e.getSource() == wnfFixpreis) {
			preisSetzen();
			bEventBehandelt = true;
		}

		if (!bEventBehandelt) {
			if (e.getSource() == wnfRabattsatz) {
				preisSetzen();
				bEventBehandelt = true;
			}
		}
	}

	/**
	 * Aufgrund einer Benutzereingabe den Staffelpreis neu berechnen.
	 * 
	 * @throws Throwable
	 */
	public void preisSetzen() throws Throwable {
		BigDecimal bdBerechneterpreis = null;

		// Artikelfixpreis zaehlt zuerst
		if (wnfFixpreis.getText() != null && wnfFixpreis.getText().length() > 0) {
			bdBerechneterpreis = wnfFixpreis.getBigDecimal();
		} else {
			// PJ17863
			if (nPreisbasis != null) {
				BigDecimal bdRabattsumme = nPreisbasis.multiply(wnfRabattsatz
						.getBigDecimal().movePointLeft(2));
				bdBerechneterpreis = nPreisbasis.subtract(bdRabattsumme);
			}
		}

		wnfBerechneterpreis.setBigDecimal(bdBerechneterpreis);

		// wenn der berechnete Preis unter dem minimalen Verkaufspreis liegt ->
		// rote Markierung
		setColorBerechneterPreis();
	}

	/**
	 * Wenn der berechnete Preis unter dem Min VK liegt, wird er rot
	 * gekennzeichnet.
	 * 
	 * @throws Throwable
	 */
	private void setColorBerechneterPreis() throws Throwable {
		if (wnfMinverkaufspreis.getBigDecimal() != null
				&& wnfBerechneterpreis != null && wnfBerechneterpreis.getDouble() !=null
				&& wnfBerechneterpreis.getDouble().doubleValue() > 0) {
			double dMinverkaufspreis = wnfMinverkaufspreis.getDouble()
					.doubleValue();
			double dBerechneterPreis = wnfBerechneterpreis.getDouble()
					.doubleValue();

			if (dBerechneterPreis < dMinverkaufspreis) {
				wnfBerechneterpreis.setForeground(Color.red);
			} else {
				wnfBerechneterpreis.setForeground(Color.black);
			}
		}
	}

	public void propertyChange(PropertyChangeEvent evt) {
		super.propertyChange(evt);

		try {
			if (wdfPreisgueltigkeitsanzeigeab != null
					&& wifArtikel.getArtikelDto() != null
					&& wifArtikel.getArtikelDto().getIId() != null) {
				if (evt.getSource() == wdfPreisgueltigkeitsanzeigeab
						.getDisplay()
						&& evt.getPropertyName().equals("date")
						&& wdfPreisgueltigkeitsanzeigeab.getDate() != null) {
					datGueltigkeitsanzeigeab = wdfPreisgueltigkeitsanzeigeab
							.getDate();

					// die passende Verkaufspreisbasis anzeigen
					if (iPreisbasis == 0 || iPreisbasis == 2) {
						verkaufspreisbasisDto2comp(DelegateFactory
								.getInstance()
								.getVkPreisfindungDelegate()
								.getArtikeleinzelverkaufspreis(
										wifArtikel.getArtikelDto().getIId(),
										datGueltigkeitsanzeigeab, waehrungCNr));
					} else {
						vkPreisfindungPreislisteDto2com(DelegateFactory
								.getInstance()
								.getVkPreisfindungDelegate()
								.getAktuellePreislisteByArtikelIIdPreislisteIId(
										wifArtikel.getArtikelDto().getIId(),
										getKundenpreislisteIId(),
										datGueltigkeitsanzeigeab, waehrungCNr));
					}

					// den berechneten Preis aktualisieren
					BigDecimal nBerechneterPreis = new BigDecimal(0);

					if (wnfFixpreis.getBigDecimal() == null
							&& wnfRabattsatz.getDouble() != null) {
						// WH 21.06.06 Es gilt die VK-Basis zur
						// Preisgueltigkeit. Damit kann der
						// berechnete Preis von dem dargstellten in der FLR
						// Liste abweichen, der wird
						// fuer das Beginndatum der Mengenstaffel angezeigt
						BigDecimal nPreisbasis = DelegateFactory
								.getInstance()
								.getVkPreisfindungDelegate()
								.ermittlePreisbasis(
										wifArtikel.getArtikelDto().getIId(),
										wdfPreisgueltigkeitsanzeigeab.getDate(),
										null, waehrungCNr);

						VerkaufspreisDto vkpfDto = DelegateFactory
								.getInstance()
								.getVkPreisfindungDelegate()
								.berechneVerkaufspreis(nPreisbasis,
										wnfRabattsatz.getDouble());

						nBerechneterPreis = vkpfDto.nettopreis;
					}

					wnfBerechneterpreis.setBigDecimal(nBerechneterPreis);
				}
			}
		} catch (Throwable t) {
			DialogFactory.showModalDialog(
					LPMain.getInstance().getTextRespectUISPr("lp.error"),
					LPMain.getInstance().getTextRespectUISPr(
							"vkpf.error.preisgueltigkeitsanzeigeab"));
		}
	}

	public KundesokomengenstaffelDto components2mengenstaffelDto(
			KundesokomengenstaffelDto mengenstaffelDtoI) throws Throwable {
		// if (mengenstaffelDtoI.getKundesokoIId() == null) {
		// throw new ExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL, new
		// Exception("kundesokoIId == null"));
		// }

		mengenstaffelDtoI.setNMenge(wnfMenge.getBigDecimal());
		mengenstaffelDtoI.setNArtikelfixpreis(wnfFixpreis.getBigDecimal());
		mengenstaffelDtoI.setFArtikelstandardrabattsatz(wnfRabattsatz
				.getDouble());

		return mengenstaffelDtoI;
	}

	public void mengenstaffelDto2components(
			KundesokomengenstaffelDto mengenstaffelDtoI) throws Throwable {
		boolean bEnable = LPMain.getTheClient().getSMandantenwaehrung()
				.equals(waehrungCNr);
		wnfRabattsatz.setEditable(bEnable);
		wnfRabattsatz.setMandatoryField(bEnable);

		wnfRabattsatz.setDouble(mengenstaffelDtoI
				.getFArtikelstandardrabattsatz());
		wnfMenge.setBigDecimal(mengenstaffelDtoI.getNMenge());
		wnfFixpreis.setBigDecimal(mengenstaffelDtoI.getNArtikelfixpreis());

		if (mengenstaffelDtoI.getNArtikelfixpreis() != null) {
			wnfBerechneterpreis.setBigDecimal(mengenstaffelDtoI
					.getNArtikelfixpreis());
		} else {
			// WH 21.06.06 Es gilt die VK-Basis zur Preisgueltigkeit. Damit kann
			// der
			// berechnete Preis von dem dargstellten in der FLR Liste abweichen,
			// der wird
			// fuer das Beginndatum der Mengenstaffel angezeigt
			BigDecimal nPreisbasis = null;
			if (iPreisbasis == 0 || iPreisbasis == 2) {

				nPreisbasis = DelegateFactory
						.getInstance()
						.getVkPreisfindungDelegate()
						.ermittlePreisbasis(
								wifArtikel.getArtikelDto().getIId(),
								wdfPreisgueltigkeitsanzeigeab.getDate(), null,
								waehrungCNr);
			} else {

				nPreisbasis = DelegateFactory
						.getInstance()
						.getVkPreisfindungDelegate()
						.ermittlePreisbasis(
								wifArtikel.getArtikelDto().getIId(),
								wdfPreisgueltigkeitsanzeigeab.getDate(),
								getKundenpreislisteIId(), waehrungCNr);
			}

			if (nPreisbasis != null) {
				VerkaufspreisDto vkpfDto = DelegateFactory
						.getInstance()
						.getVkPreisfindungDelegate()
						.berechneVerkaufspreis(
								nPreisbasis,
								mengenstaffelDtoI
										.getFArtikelstandardrabattsatz());

				wnfBerechneterpreis.setBigDecimal(vkpfDto.nettopreis);
			} else {
				wnfBerechneterpreis.setBigDecimal(null);
			}
		}

	}

	public KundesokoDto components2kundesokoDto(KundesokoDto kundesokoDtoI)
			throws Throwable {
		super.components2kundesokoDto(kundesokoDtoI);

		kundesokoDtoI.setArtgruIId(null);
		kundesokoDtoI.setArtikelIId(wifArtikel.getArtikelDto().getIId());

		if (wtfKndArtBez != null) {
			kundesokoDtoI.setCKundeartikelbez(wtfKndArtBez.getText());
			kundesokoDtoI.setCKundeartikelzbez(wtfKndArtZBez.getText());
		}

		return kundesokoDtoI;
	}

	public void kundesokoDto2components(KundesokoDto kundesokoDtoI)
			throws Throwable {
		boolean bEnable = LPMain.getTheClient().getSMandantenwaehrung()
				.equals(waehrungCNr);
		wnfRabattsatz.setEditable(bEnable);
		wnfRabattsatz.setMandatoryField(bEnable);
		wlaWaehrungGestehungspreis.setText(waehrungCNr);
		wlaWaehrungMinverkaufspreis.setText(waehrungCNr);
		wlaVkbasiswaehrung.setText(waehrungCNr);
		wlaFixpreiswaehrung.setText(waehrungCNr);

		wdfGueltigab.setDate(kundesokoDtoI.getTPreisgueltigab());
		wdfGueltigbis.setDate(kundesokoDtoI.getTPreisgueltigbis());
		wtfBemerkung.setText(kundesokoDtoI.getCBemerkung());

		if (wtfKndArtBez != null) {
			wtfKndArtBez.setText(kundesokoDtoI.getCKundeartikelbez());

		}
		if (wtfKndArtZBez != null) {
			wtfKndArtZBez.setText(kundesokoDtoI.getCKundeartikelzbez());
		}

		if (kundesokoDtoI.getBDrucken() == null) {
			wcbDrucken.setSelected(false);
		} else {
			wcbDrucken.setSelected(Helper.short2boolean(kundesokoDtoI
					.getBDrucken()));
		}

		if (kundesokoDtoI.getBBemerkungdrucken() == null) {
			wcbBemerkungDrucken.setSelected(false);
		} else {
			wcbBemerkungDrucken.setSelected(Helper.short2boolean(kundesokoDtoI
					.getBBemerkungdrucken()));
		}

		if (kundesokoDtoI.getBRabattsichtbar() == null) {
			wcbRabattsichtbar.setSelected(false);
		} else {
			wcbRabattsichtbar.setSelected(Helper.short2boolean(kundesokoDtoI
					.getBRabattsichtbar()));
		}
	}

	public WrapperIdentField getWifArtikel() {
		return wifArtikel;
	}
}

// Inner classes -------------------------------------------------------------
@SuppressWarnings("static-access")
class PanelMengenstaffelArtikel_fixpreis_focusAdapter extends
		java.awt.event.FocusAdapter {
	PanelMengenstaffelArtikel adaptee;

	PanelMengenstaffelArtikel_fixpreis_focusAdapter(
			PanelMengenstaffelArtikel adaptee) {
		this.adaptee = adaptee;
	}

	public void focusLost(FocusEvent e) {
		try {
			adaptee.fixpreis_focusLost(e);
		} catch (Throwable ex) {
			DialogFactory.showModalDialog(
					LPMain.getInstance().getTextRespectUISPr("lp.error"),
					LPMain.getInstance().getTextRespectUISPr(
							"lp.error.preisberechnungfehlgeschlagen"));
		}
	}
}

@SuppressWarnings("static-access")
class PanelMengenstaffelArtikel_rabattsatz_focusAdapter extends
		java.awt.event.FocusAdapter {
	PanelMengenstaffelArtikel adaptee;

	PanelMengenstaffelArtikel_rabattsatz_focusAdapter(
			PanelMengenstaffelArtikel adaptee) {
		this.adaptee = adaptee;
	}

	public void focusLost(FocusEvent e) {
		try {
			adaptee.rabattsatz_focusLost(e);
		} catch (Throwable ex) {
			DialogFactory.showModalDialog(
					LPMain.getInstance().getTextRespectUISPr("lp.error"),
					LPMain.getInstance().getTextRespectUISPr(
							"lp.error.preisberechnungfehlgeschlagen"));
		}
	}
}
