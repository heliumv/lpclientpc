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
package com.lp.client.frame;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.math.BigDecimal;

import javax.swing.SwingConstants;

import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.PanelDialogPreisvorschlagDto;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperRadioButton;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.artikel.service.ArtgruDto;
import com.lp.server.artikel.service.VerkaufspreisDto;
import com.lp.server.partner.service.KundesokoDto;
import com.lp.server.partner.service.KundesokomengenstaffelDto;
import com.lp.server.system.service.MwstsatzDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.util.Facade;
import com.lp.util.Helper;

/**
 * <p>
 * <I>Dialog zur Auswahl von Verkaufspreisen aufgrund von
 * Kundesoko-Mengenstaffeln</I>
 * </p>
 * <br>
 * 
 * @todo Anzeige von Artikelbez und Artikelzusatzbez in der Kundensprache. PJ
 *       4948 <br>
 * @todo Preisrecht des Benutzers beruecksichtigen PJ 4948
 *       <p>
 *       Copyright Logistik Pur Software GmbH (c) 2004-2008
 *       </p>
 *       <p>
 *       Erstellungsdatum <I>20.06.2006</I>
 *       </p>
 *       <p>
 *       </p>
 * 
 * @author Uli Walch
 * @version $Revision: 1.17 $
 */
@SuppressWarnings("static-access")
public class PanelDialogPreisvorschlagKundesokomengenstaffel extends
		PanelDialogPreisvorschlag {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private WrapperLabel wlaMenge = null;
	private WrapperNumberField wnfMenge = null;
	private WrapperLabel wlaMengeeinheit = null;

	private WrapperLabel wlaKunde = null;
	private WrapperTextField wtfKunde = null;
	private WrapperLabel wlaArtgru = null;
	private WrapperTextField wtfArtgru = null;

	int iPreisbasis = 0;

	private WrapperRadioButton[] awrbKundesokomengenstaffel = null;
	private WrapperNumberField[] awnfRabattsatz = null;
	private WrapperLabel[] awlaProzent = null;
	private WrapperNumberField[] awnfBerechneterPreis = null;
	private WrapperLabel[] awlaWaehrung = null;

	/** Alle vorhandenen Mengenstaffeln fuer diesen Mandanten. */
	private KundesokomengenstaffelDto[] aKundesokomengenstaffelDtos = null;

	/** VerkaufspreisDtos werden hinterlegt. */
	private VerkaufspreisDto[] aVerkaufspreisDtos = null;

	/**
	 * Konstruktor.
	 * 
	 * @param oInternalFrameI
	 *            wir sitzen in diesem InternalFrame
	 * @param title
	 *            der Titel des Fensters
	 * @param panelDialogPreisvorschlagDtoI
	 *            PanelDialogPreisvorschlagDto
	 * @throws Throwable
	 *             Ausnahme
	 */
	public PanelDialogPreisvorschlagKundesokomengenstaffel(
			InternalFrame oInternalFrameI, String title,
			PanelDialogPreisvorschlagDto panelDialogPreisvorschlagDtoI)
			throws Throwable {
		super(oInternalFrameI, title, panelDialogPreisvorschlagDtoI);

		jbInit();
		setDefaults();
		initComponents();
	}

	private void jbInit() throws Throwable {

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

		if (iPreisbasis == 1) {
			wrbVerkaufspreisbasis.setText(LPMain.getInstance()
					.getTextRespectUISPr(
							"part.kundensoko.preisbasis.preisliste"));
		}

		wlaMenge = new WrapperLabel(LPMain.getInstance().getTextRespectUISPr(
				"lp.menge"));
		wnfMenge = new WrapperNumberField();
		wnfMenge.setBigDecimal(panelDialogPreisvorschlagDto.getNMenge());
		wnfMenge.setEditable(false);
		wlaMengeeinheit = new WrapperLabel(panelDialogPreisvorschlagDto
				.getArtikelDto().getEinheitCNr().trim());

		jpaWorkingOn.add(wlaMenge, new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfMenge, new GridBagConstraints(4, 1, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaMengeeinheit, new GridBagConstraints(5, 1, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
	}

	private void setDefaults() throws Throwable {
		wlaKunde = new WrapperLabel(LPMain.getInstance().getTextRespectUISPr(
				"label.kunde"));

		wtfKunde = new WrapperTextField();
		wtfKunde.setColumnsMax(Facade.MAX_UNBESCHRAENKT);
		wtfKunde.setEditable(false);
		wtfKunde.setText(panelDialogPreisvorschlagDto.getKundeDto()
				.getPartnerDto().formatFixTitelName1Name2());

		wlaArtgru = new WrapperLabel(LPMain.getInstance().getTextRespectUISPr(
				"lp.artikelgruppe"));
		wtfArtgru = new WrapperTextField();
		wtfArtgru.setColumnsMax(Facade.MAX_UNBESCHRAENKT);
		wtfArtgru.setEditable(false);

		ArtgruDto artgruDto = null;

		if (panelDialogPreisvorschlagDto.getArtikelDto().getArtgruIId() != null) {
			artgruDto = DelegateFactory
					.getInstance()
					.getArtikelDelegate()
					.artgruFindByPrimaryKey(
							panelDialogPreisvorschlagDto.getArtikelDto()
									.getArtgruIId());
		}

		String artgruBez = null;

		if (artgruDto != null) {
			if (artgruDto.getArtgrusprDto() != null
					&& artgruDto.getArtgrusprDto().getCBez() != null) {
				artgruBez = artgruDto.getArtgrusprDto().getCBez();
			} else {
				artgruBez = artgruDto.getCNr();
			}
		}

		wtfArtgru.setText(artgruBez);

		iZeile++;
		jpaWorkingOn.add(wlaKunde, new GridBagConstraints(0, iZeile, 1, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(10, 2, 10, 2), 0, 0));
		jpaWorkingOn.add(wtfKunde, new GridBagConstraints(1, iZeile, 1, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(10, 2, 10, 2), 0, 0));
		jpaWorkingOn.add(wlaArtgru, new GridBagConstraints(3, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(10, 2, 10, 2), 0, 0));
		jpaWorkingOn.add(wtfArtgru, new GridBagConstraints(4, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(10, 2, 10, 2), 0, 0));

		// die Liste enthaelt die berechneten Preis fuer Artikel und
		// Artikelgruppen
		int iLaengeListe = 0;

		KundesokoDto kundesokoArtikelDto = DelegateFactory
				.getInstance()
				.getKundesokoDelegate()
				.kundesokoFindByKundeIIdArtikelIIdGueltigkeitsdatumOhneExc(
						panelDialogPreisvorschlagDto.getKundeDto().getIId(),
						panelDialogPreisvorschlagDto.getArtikelDto().getIId(),
						panelDialogPreisvorschlagDto
								.getDatGueltigkeitsdatumFuerPreise());

		KundesokomengenstaffelDto[] aKundesokomengenstaffelArtikelDtos = null;

		if (kundesokoArtikelDto != null) {
			aKundesokomengenstaffelArtikelDtos = DelegateFactory
					.getInstance()
					.getKundesokoDelegate()
					.kundesokomengenstaffelFindByKundesokoIIdInZielWaehrung(
							kundesokoArtikelDto.getIId(),
							panelDialogPreisvorschlagDto
									.getDatGueltigkeitsdatumFuerPreise(),
							panelDialogPreisvorschlagDto.getCNrWaehrung());
			iLaengeListe += aKundesokomengenstaffelArtikelDtos.length;
		}

		KundesokoDto kundesokoArtgruDto = DelegateFactory
				.getInstance()
				.getKundesokoDelegate()
				.kundesokoFindByKundeIIdArtgruIIdGueltigkeitsdatumOhneExc(
						panelDialogPreisvorschlagDto.getKundeDto().getIId(),
						panelDialogPreisvorschlagDto.getArtikelDto()
								.getArtgruIId(),
						panelDialogPreisvorschlagDto
								.getDatGueltigkeitsdatumFuerPreise());

		KundesokomengenstaffelDto[] aKundesokomengenstaffelArtgruDtos = null;

		if (kundesokoArtgruDto != null) {
			aKundesokomengenstaffelArtgruDtos = DelegateFactory
					.getInstance()
					.getKundesokoDelegate()
					.kundesokomengenstaffelFindByKundesokoIIdInZielWaehrung(
							kundesokoArtgruDto.getIId(),
							panelDialogPreisvorschlagDto
									.getDatGueltigkeitsdatumFuerPreise(),
							panelDialogPreisvorschlagDto.getCNrWaehrung());

			iLaengeListe += aKundesokomengenstaffelArtgruDtos.length;
		}

		aKundesokomengenstaffelDtos = new KundesokomengenstaffelDto[iLaengeListe];

		int iIndex = 0;

		if (aKundesokomengenstaffelArtikelDtos != null
				&& aKundesokomengenstaffelArtikelDtos.length > 0) {
			for (int i = 0; i < aKundesokomengenstaffelArtikelDtos.length; i++) {
				aKundesokomengenstaffelDtos[iIndex] = aKundesokomengenstaffelArtikelDtos[i];
				iIndex++;
			}
		}

		if (aKundesokomengenstaffelArtgruDtos != null
				&& aKundesokomengenstaffelArtgruDtos.length > 0) {
			for (int i = 0; i < aKundesokomengenstaffelArtgruDtos.length; i++) {
				aKundesokomengenstaffelDtos[iIndex] = aKundesokomengenstaffelArtgruDtos[i];
				iIndex++;
			}
		}

		awrbKundesokomengenstaffel = new WrapperRadioButton[iLaengeListe + 2]; // +
		// Handeingabe
		// Rabattsatz
		// und
		// Fixpreis
		awnfRabattsatz = new WrapperNumberField[iLaengeListe];
		awlaProzent = new WrapperLabel[iLaengeListe];
		awnfBerechneterPreis = new WrapperNumberField[iLaengeListe];
		awlaWaehrung = new WrapperLabel[iLaengeListe];

		aVerkaufspreisDtos = new VerkaufspreisDto[iLaengeListe];

		String sAnzeige = null;

		// die Liste der SOKOs fuer den Artikel
		for (int i = 0; i < aKundesokomengenstaffelDtos.length; i++) {
			int iNummer = i + 1;
			sAnzeige = iNummer
					+ " "
					+ Helper.formatZahl(
							aKundesokomengenstaffelDtos[i].getNMenge(), 2,
							LPMain.getInstance().getTheClient().getLocUi())
					+ " "
					+ panelDialogPreisvorschlagDto.getArtikelDto()
							.getEinheitCNr().trim();

			awrbKundesokomengenstaffel[i] = new WrapperRadioButton(sAnzeige);
			awrbKundesokomengenstaffel[i]
					.setMnemonic(getMnemonicRBArry(iNummer));
			awrbKundesokomengenstaffel[i]
					.setHorizontalAlignment(SwingConstants.LEFT);

			awrbKundesokomengenstaffel[i]
					.setActionCommand(ACTION_SPECIAL_PREISLISTE_GEWAEHLT);
			awrbKundesokomengenstaffel[i].addActionListener(this);
			jbgAuswahlliste.add(awrbKundesokomengenstaffel[i]);

			awnfRabattsatz[i] = new WrapperNumberField();
			awnfRabattsatz[i].setEditable(false);

			if (aKundesokomengenstaffelDtos[i].getNArtikelfixpreis() != null) {

				if (panelDialogPreisvorschlagDto.getAktuellerVerkaufspreisDto().einzelpreis
						.doubleValue() != 0) {

					BigDecimal rabattsatz = new BigDecimal(1)
							.subtract(aKundesokomengenstaffelDtos[i]
									.getNArtikelfixpreis()
									.divide(panelDialogPreisvorschlagDto
											.getAktuellerVerkaufspreisDto().einzelpreis,
											4, BigDecimal.ROUND_HALF_EVEN));
					awnfRabattsatz[i].setBigDecimal(rabattsatz
							.multiply(new BigDecimal(100)));

				} else {
					awnfRabattsatz[i].setBigDecimal(BigDecimal.ZERO);
				}

			} else {
				awnfRabattsatz[i].setDouble(aKundesokomengenstaffelDtos[i]
						.getFArtikelstandardrabattsatz());
			}

			awlaProzent[i] = new WrapperLabel(LPMain.getInstance()
					.getTextRespectUISPr("label.prozent"));
			awlaProzent[i].setHorizontalAlignment(SwingConstants.LEADING);

			// den aktuellen Verkaufpsreis nach der Preisliste berechnen
			BigDecimal nPreisbasis = null;
			if (iPreisbasis == 0 || iPreisbasis == 2) {
				nPreisbasis = DelegateFactory
						.getInstance()
						.getVkPreisfindungDelegate()
						.ermittlePreisbasis(
								panelDialogPreisvorschlagDto.getArtikelDto()
										.getIId(),
								panelDialogPreisvorschlagDto
										.getDatGueltigkeitsdatumFuerPreise(),
								null,
								panelDialogPreisvorschlagDto.getCNrWaehrung());
			} else {
				nPreisbasis = DelegateFactory
						.getInstance()
						.getVkPreisfindungDelegate()
						.ermittlePreisbasis(
								panelDialogPreisvorschlagDto.getArtikelDto()
										.getIId(),
								panelDialogPreisvorschlagDto
										.getDatGueltigkeitsdatumFuerPreise(),
								panelDialogPreisvorschlagDto
										.getKundeDto()
										.getVkpfArtikelpreislisteIIdStdpreisliste(),
								panelDialogPreisvorschlagDto.getCNrWaehrung());
				if (nPreisbasis != null) {
					wnfVerkaufspreisbasis.setBigDecimal(nPreisbasis);
				}
			}

			VerkaufspreisDto vkpInMandantenwaehrungDto = null;

			if (aKundesokomengenstaffelDtos[i].getNArtikelfixpreis() != null) {
				vkpInMandantenwaehrungDto = DelegateFactory
						.getInstance()
						.getVkPreisfindungDelegate()
						.berechneVerkaufspreis(
								nPreisbasis,
								aKundesokomengenstaffelDtos[i]
										.getNArtikelfixpreis(),
								panelDialogPreisvorschlagDto
										.getNMaterialzuschlag());
			} else {
				vkpInMandantenwaehrungDto = DelegateFactory
						.getInstance()
						.getVkPreisfindungDelegate()
						.berechneVerkaufspreis(
								nPreisbasis,
								aKundesokomengenstaffelDtos[i]
										.getFArtikelstandardrabattsatz(),
								panelDialogPreisvorschlagDto
										.getNMaterialzuschlag());
				if (aKundesokomengenstaffelDtos[i]
						.getFArtikelstandardrabattsatz().intValue() == 0)
					vkpInMandantenwaehrungDto.nettopreis = nPreisbasis;

			}

			// die Anzeige der Preise in diesem Panel erfolgt in der aktuellen
			// Zielwaehrung
			aVerkaufspreisDtos[i] = vkpInMandantenwaehrungDto;
			awnfBerechneterPreis[i] = new WrapperNumberField();
			awnfBerechneterPreis[i]
					.setFractionDigits(iPreiseUINachkommastellen);
			awnfBerechneterPreis[i].setBigDecimal(aVerkaufspreisDtos[i]
					.getNettpreisOhneMaterialzuschlag());
			awnfBerechneterPreis[i].setEditable(false);

			// wenn der berechnete Preis unter dem minimalen Verkaufspreis liegt
			// -> rote Markierung
			setColorBerechneterPreis(i);

			// Anzeige der Preise in der Zielwaehrung des Auftrags
			awlaWaehrung[i] = new WrapperLabel(
					panelDialogPreisvorschlagDto.getCNrWaehrung());
			awlaWaehrung[i].setHorizontalAlignment(SwingConstants.LEADING);

			iZeile++;
			jpaWorkingOn.add(awrbKundesokomengenstaffel[i],
					new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0,
							GridBagConstraints.CENTER, GridBagConstraints.BOTH,
							new Insets(2, 2, 2, 2), 0, 0));
			jpaWorkingOn.add(awnfRabattsatz[i], new GridBagConstraints(1,
					iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
			jpaWorkingOn.add(awlaProzent[i], new GridBagConstraints(2, iZeile,
					1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
			jpaWorkingOn.add(awnfBerechneterPreis[i], new GridBagConstraints(4,
					iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
			jpaWorkingOn.add(awlaWaehrung[i], new GridBagConstraints(5, iZeile,
					1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		}

		iIndex = aKundesokomengenstaffelDtos.length;

		// RadioButton fuer Handeingabe Fixpreis
		iZeile++;

		jpaWorkingOn.add(wrbHandeingabeFixpreis, new GridBagConstraints(0,
				iZeile, 4, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(10, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfHandeingabeFixpreis, new GridBagConstraints(4,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(10, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaHandeingabeFixpreisWaehrung,
				new GridBagConstraints(5, iZeile, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(10, 2, 2, 2), 0, 0));

		// RadioButton fuer Handeingabe Rabattsatz
		iZeile++;

		jpaWorkingOn.add(wrbHandeingabeRabattsatz, new GridBagConstraints(0,
				iZeile, 4, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfHandeingabeRabattsatz, new GridBagConstraints(4,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaHandeingabeRabattsatzProzent,
				new GridBagConstraints(5, iZeile, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(2, 2, 2, 2), 0, 0));

		if (panelDialogPreisvorschlagDto.getArtikelDto().getMaterialIId() != null) {
			iZeile++;

			jpaWorkingOn.add(wlaMaterialzuschlag, new GridBagConstraints(0,
					iZeile, 4, 1, 0.0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
			jpaWorkingOn.add(wnfMaterialzuschlag, new GridBagConstraints(4,
					iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
			jpaWorkingOn.add(wlaMaterialzuschlagWaehrung,
					new GridBagConstraints(5, iZeile, 1, 1, 0.0, 0.0,
							GridBagConstraints.CENTER, GridBagConstraints.BOTH,
							new Insets(2, 2, 2, 2), 0, 0));
		}

		// die beiden Buttons fuer die anderen VK-Stufen
		if (panelDialogPreisvorschlagDto.getBEditable()) {
			iZeile++;
			int iPosButton = 1;

			if (panelDialogPreisvorschlagDto.getVkpreisfindungDto()
					.getVkpStufe1() != null) {
				jpaWorkingOn
						.add(getWbuZweiteStufe(), new GridBagConstraints(1,
								iZeile, 1, 1, 0.0, 0.0,
								GridBagConstraints.CENTER,
								GridBagConstraints.BOTH,
								new Insets(10, 2, 2, 2), 0, 0));
				iPosButton = 4;
			}

			if (panelDialogPreisvorschlagDto.getVkpreisfindungDto()
					.getVkpStufe2() != null) {
				jpaWorkingOn
						.add(getWbuDritteStufe(), new GridBagConstraints(4,
								iZeile, 1, 1, 0.0, 0.0,
								GridBagConstraints.CENTER,
								GridBagConstraints.BOTH,
								new Insets(10, 2, 2, 2), 0, 0));
			}
		} else {
			removeButton(ACTION_SPECIAL_OK); // das Panel ist reine Info
		}

		// Dieses Panel kann erst aufgehen, wenn im PanelAuftragPositionen ein
		// Artikel gewaehlt wurde. Zu diesem Zeitpunkt ist sowohl eine Menge
		// als auch ein Verkaufspreis hinterlegt, wenn die Preisinformation
		// nicht
		// uebersteuert wurde
		if (panelDialogPreisvorschlagDto
				.getIIdKundesokomengenstaffelZuletztGewaehlt() != null
				&& aKundesokomengenstaffelDtos != null
				&& aKundesokomengenstaffelDtos.length != 0) {
			for (int i = 0; i < aKundesokomengenstaffelDtos.length; i++) {
				if (panelDialogPreisvorschlagDto
						.getIIdKundesokomengenstaffelZuletztGewaehlt()
						.intValue() == aKundesokomengenstaffelDtos[i].getIId()
						.intValue()) {
					awrbKundesokomengenstaffel[i].setSelected(true);
				}
			}
		} else {
			// Wenn im Konstruktor keine gewaehlte Vkpfmengenstaffel bestimmt
			// wurde oder der Artikel
			// in keiner der Mengenstaffeln enthalten ist dann
			// werden Fixpreis und Rabattsatz angegeben, moeglicherweise wurden
			// beide
			// Werte manuell uebersteuert

			// wenn der Rabattsatz geradzahlig ist, den Rabattsatz markieren,
			// sonst der Fixpreis
			if ((panelDialogPreisvorschlagDto.getAktuellerVerkaufspreisDto().rabattsatz
					.doubleValue()
					- panelDialogPreisvorschlagDto
							.getAktuellerVerkaufspreisDto().rabattsatz
							.intValue() > 0)
					|| panelDialogPreisvorschlagDto
							.getAktuellerVerkaufspreisDto().rabattsatz
							.doubleValue() == 0) {
				wrbHandeingabeFixpreis.setSelected(true);
				wnfHandeingabeFixpreis.setEditable(true);
			} else {
				wrbHandeingabeRabattsatz.setSelected(true);
				wnfHandeingabeRabattsatz.setEditable(true);
			}

			wnfHandeingabeFixpreis.setBigDecimal(panelDialogPreisvorschlagDto
					.getAktuellerVerkaufspreisDto().einzelpreis);
			wnfHandeingabeRabattsatz.setDouble(panelDialogPreisvorschlagDto
					.getAktuellerVerkaufspreisDto().rabattsatz);
		}

		wnfHandeingabeFixpreis.setBigDecimal(panelDialogPreisvorschlagDto
				.getAktuellerVerkaufspreisDto().einzelpreis);
		wnfHandeingabeRabattsatz.setDouble(panelDialogPreisvorschlagDto
				.getAktuellerVerkaufspreisDto().rabattsatz);

		if (panelDialogPreisvorschlagDto.getNFixPreis() != null) {

			if (wnfVerkaufspreisbasis.getBigDecimal() != null
					&& wnfVerkaufspreisbasis.getBigDecimal().equals(
							panelDialogPreisvorschlagDto.getNFixPreis())) {
				wrbVerkaufspreisbasis.setSelected(true);

			} else {

				boolean bPreisGefunden = false;
				for (int i = 0; i < aKundesokomengenstaffelDtos.length; i++) {
					if (awnfBerechneterPreis[i].getBigDecimal() != null
							&& awnfBerechneterPreis[i].getBigDecimal()
									.equals(panelDialogPreisvorschlagDto
											.getNFixPreis())) {
						awrbKundesokomengenstaffel[i].setSelected(true);
						bPreisGefunden = true;
					}
				}
				if (bPreisGefunden == false) {
					BigDecimal fixPreis = panelDialogPreisvorschlagDto
							.getNFixPreis();
					if (panelDialogPreisvorschlagDto.getNMaterialzuschlag() != null) {
						fixPreis = fixPreis
								.subtract(panelDialogPreisvorschlagDto
										.getNMaterialzuschlag());
					}
					wrbHandeingabeFixpreis.setSelected(true);
					wnfHandeingabeFixpreis.setBigDecimal(fixPreis);
					wnfHandeingabeFixpreis.setMandatoryField(true);
					wnfHandeingabeFixpreis.setEditable(true);

					// Hier nun noch den Rabattsatz berechnen

					if (panelDialogPreisvorschlagDto
							.getAktuellerVerkaufspreisDto().einzelpreis != null) {

						if (panelDialogPreisvorschlagDto
								.getAktuellerVerkaufspreisDto().einzelpreis
								.doubleValue() != 0) {

							BigDecimal rabattsatz = new BigDecimal(1)
									.subtract(panelDialogPreisvorschlagDto
											.getNFixPreis()
											.divide(panelDialogPreisvorschlagDto
													.getAktuellerVerkaufspreisDto().einzelpreis,
													4,
													BigDecimal.ROUND_HALF_EVEN));
							wnfHandeingabeRabattsatz.setBigDecimal(rabattsatz
									.multiply(new BigDecimal(100)));

						}

					}

				}
			}
		}

	}

	protected void eventActionAlt(ActionEvent e) throws Throwable {
		super.eventActionAlt(e);
		if (e.getActionCommand().equals(ALT1)) {
			if (awrbKundesokomengenstaffel.length > 0
					&& awrbKundesokomengenstaffel[0] != null)
				awrbKundesokomengenstaffel[0].setSelected(true);
		} else if (e.getActionCommand().equals(ALT2)) {
			if (awrbKundesokomengenstaffel.length > 1
					&& awrbKundesokomengenstaffel[1] != null)
				awrbKundesokomengenstaffel[1].setSelected(true);
		} else if (e.getActionCommand().equals(ALT3)) {
			if (awrbKundesokomengenstaffel.length > 2
					&& awrbKundesokomengenstaffel[2] != null)
				awrbKundesokomengenstaffel[2].setSelected(true);
		} else if (e.getActionCommand().equals(ALT4)) {
			if (awrbKundesokomengenstaffel.length > 3
					&& awrbKundesokomengenstaffel[3] != null)
				awrbKundesokomengenstaffel[3].setSelected(true);
		} else if (e.getActionCommand().equals(ALT5)) {
			if (awrbKundesokomengenstaffel.length > 4
					&& awrbKundesokomengenstaffel[4] != null)
				awrbKundesokomengenstaffel[4].setSelected(true);
		} else if (e.getActionCommand().equals(ALT6)) {
			if (awrbKundesokomengenstaffel.length > 5
					&& awrbKundesokomengenstaffel[5] != null)
				awrbKundesokomengenstaffel[5].setSelected(true);
		} else if (e.getActionCommand().equals(ALT7)) {
			if (awrbKundesokomengenstaffel.length > 6
					&& awrbKundesokomengenstaffel[6] != null)
				awrbKundesokomengenstaffel[6].setSelected(true);
		} else if (e.getActionCommand().equals(ALT8)) {
			if (awrbKundesokomengenstaffel.length > 7
					&& awrbKundesokomengenstaffel[7] != null)
				awrbKundesokomengenstaffel[7].setSelected(true);
		}
		if (e.getActionCommand().equals(ALT9)) {
			if (awrbKundesokomengenstaffel.length > 8
					&& awrbKundesokomengenstaffel[8] != null)
				awrbKundesokomengenstaffel[8].setSelected(true);
		}
	}

	/**
	 * Hier wird das Ergebnis zusammengebaut, das von aussen abgeholt werden
	 * kann.
	 * 
	 * @throws Throwable
	 */
	protected void buildMeinErgebnis() throws Throwable {
		for (int i = 0; i < aKundesokomengenstaffelDtos.length; i++) {
			if (awrbKundesokomengenstaffel[i].isSelected()) {

				// die Mengenstaffel hinterlegen
				panelDialogPreisvorschlagDto
						.setIIdKundesokomengenstaffelZuletztGewaehlt(aKundesokomengenstaffelDtos[i]
								.getIId());

				// den berechneten Verkaufspreis hinterlegen
				panelDialogPreisvorschlagDto
						.setAktuellerVerkaufspreisDto(aVerkaufspreisDtos[i]);
			}
		}

		if (wrbHandeingabeFixpreis.isSelected()) {
			panelDialogPreisvorschlagDto
					.setIIdKundesokomengenstaffelZuletztGewaehlt(null);
			panelDialogPreisvorschlagDto
					.setAktuellerVerkaufspreisDto(new VerkaufspreisDto());

			// Aktuellen MWST-Satz uebersetzen.
			MwstsatzDto mwstsatzDtoAktuell = DelegateFactory
					.getInstance()
					.getMandantDelegate()
					.mwstsatzFindByMwstsatzbezIIdAktuellster(
							panelDialogPreisvorschlagDto.getKundeDto()
									.getMwstsatzbezIId());
			if (nVkbasisInBelegwaehrung.intValue() == 0)
				nVkbasisInBelegwaehrung = wnfHandeingabeFixpreis
						.getBigDecimal();
			if (wnfHandeingabeFixpreis.getText() != null
					&& !wnfHandeingabeFixpreis.getText().equals("")) {
				panelDialogPreisvorschlagDto
						.setAktuellerVerkaufspreisDto(DelegateFactory
								.getInstance()
								.getVkPreisfindungDelegate()
								.berechneVerkaufspreis(
										nVkbasisInBelegwaehrung,
										wnfHandeingabeFixpreis.getBigDecimal(),
										panelDialogPreisvorschlagDto
												.getNMaterialzuschlag(),
										mwstsatzDtoAktuell.getIId()));
			}
		} else if (wrbVerkaufspreisbasis.isSelected()) {
			panelDialogPreisvorschlagDto
					.setAktuellerVerkaufspreisDto(DelegateFactory
							.getInstance()
							.getVkPreisfindungDelegate()
							.berechneVerkaufspreis(nVkbasisInBelegwaehrung,
									nVkbasisInBelegwaehrung));

		} else if (wrbHandeingabeRabattsatz.isSelected()) {
			panelDialogPreisvorschlagDto
					.setIIdKundesokomengenstaffelZuletztGewaehlt(null);
			VerkaufspreisDto verkaufspreisDtoInBelegwaehrung = null;

			if (wnfHandeingabeRabattsatz.getText() != null
					&& !wnfHandeingabeRabattsatz.getText().equals("")) {
				// Aktuellen MWST-Satz uebersetzen.
				MwstsatzDto mwstsatzDtoAktuell = DelegateFactory
						.getInstance()
						.getMandantDelegate()
						.mwstsatzFindByMwstsatzbezIIdAktuellster(
								panelDialogPreisvorschlagDto.getKundeDto()
										.getMwstsatzbezIId());
				/*
				 * // wenn es in der Anzeige einen Fixpreis gibt, dann gilt
				 * dieser als Basis // und wird nicht ueberschrieben if
				 * (wnfHandeingabeFixpreis.getBigDecimal() != null) {
				 * verkaufspreisDtoInBelegwaehrung =
				 * DelegateFactory.getInstance().getVkPreisfindungDelegate().
				 * berechneVerkaufspreis(wnfHandeingabeFixpreis.getBigDecimal(),
				 * wnfHandeingabeRabattsatz.getDouble(),
				 * panelDialogPreisvorschlagDto. getAktuellerVerkaufspreisDto().
				 * getDdZusatzrabattsatz(), mwstsatzDtoAktuell.getIId()); } else
				 * {
				 */

				// es muss in die Waehrung des Belegs umgerechnet werden
				VerkaufspreisDto verkaufspreisDtoInMandantenwaehrung = DelegateFactory
						.getInstance()
						.getVkPreisfindungDelegate()
						.berechneVerkaufspreis(
								getNVkbasisInBelegwaehrung(),
								wnfHandeingabeRabattsatz.getDouble(),
								panelDialogPreisvorschlagDto
										.getAktuellerVerkaufspreisDto()
										.getDdZusatzrabattsatz(),
								mwstsatzDtoAktuell.getIId(),
								panelDialogPreisvorschlagDto
										.getNMaterialzuschlag());

				verkaufspreisDtoInBelegwaehrung = DelegateFactory
						.getInstance()
						.getVkPreisfindungDelegate()
						.getPreisdetailsInFremdwaehrung(
								verkaufspreisDtoInMandantenwaehrung,
								new BigDecimal(panelDialogPreisvorschlagDto
										.getDdWechselkurs().doubleValue()));

				// }
			}

			panelDialogPreisvorschlagDto
					.setAktuellerVerkaufspreisDto(verkaufspreisDtoInBelegwaehrung);
		}
	}

	protected void setColorBerechneterPreis(int index) throws Throwable {
		if (wnfMinverkaufspreis.getBigDecimal() != null
				&& awnfBerechneterPreis != null
				&& awnfBerechneterPreis.length > 0) {
			double dMinverkaufspreis = wnfMinverkaufspreis.getDouble()
					.doubleValue();
			double dBerechneterPreis = awnfBerechneterPreis[index].getDouble()
					.doubleValue();

			if (dBerechneterPreis < dMinverkaufspreis) {
				awnfBerechneterPreis[index].setForeground(Color.red);
			} else {
				awnfBerechneterPreis[index].setForeground(Color.black);
			}
		}
	}

	/**
	 * Der Button fuer die Anzeige einer zweiten VK-Stufe muss initialisert
	 * werden. <br>
	 * Die zweite VK-Stufe sind die Kundenpreislisten.
	 * 
	 * @throws Throwable
	 */
	protected void initZweiteVkstufe() throws Throwable {
		getWbuZweiteStufe().setText(
				LPMain.getInstance().getTextRespectUISPr("vkpf.preisliste"));
		getWbuZweiteStufe().setActionCommand(ACTION_SPECIAL_VKPFSTUFE1);
	}

	/**
	 * Der Button fuer die Anzeige einer dritten VK-Stufe muss initialisert
	 * werden. <br>
	 * Die dritte VK-Stufe sind die VK-Mengenstaffeln.
	 * 
	 * @throws Throwable
	 */
	protected void initDritteVkstufe() throws Throwable {
		getWbuDritteStufe().setText(
				LPMain.getInstance()
						.getTextRespectUISPr("vkpf.vkmengenstaffel"));
		getWbuDritteStufe().setActionCommand(ACTION_SPECIAL_VKPFSTUFE2);
	}
}
