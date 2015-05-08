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
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.artikel.service.VerkaufspreisDto;
import com.lp.server.artikel.service.VkPreisfindungPreislisteDto;
import com.lp.server.artikel.service.VkpfartikelpreislisteDto;
import com.lp.server.system.service.MwstsatzDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;

/**
 * <p>
 * <I>Dialog zur Auswahl von Verkaufspreisen aufgrund von Preislisten</I>
 * </p>
 * <br>
 * 
 * @todo Anzeige von Artikelbez und Artikelzusatzbez in der Kundensprache.PJ
 *       4948 <br>
 * @todo Preisrecht des Benutzers beruecksichtigenPJ 4948
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
 * @version $Revision: 1.27 $
 */
@SuppressWarnings("static-access")
public class PanelDialogPreisvorschlagPreisliste extends
		PanelDialogPreisvorschlag {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private WrapperRadioButton[] awrbPreislistenname = null;
	private WrapperNumberField[] awnfRabattsatz = null;
	private WrapperLabel[] awlaProzent = null;
	private WrapperNumberField[] awnfBerechneterPreis = null;
	private WrapperLabel[] awlaWaehrung = null;
	private WrapperNumberField[] awnfPreisFuerMenge = null;

	/** Alle aktiven Preislisten fuer diesen Mandanten. */
	private VkpfartikelpreislisteDto[] aVkpfartikelpreislisteDtos = null;

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
	public PanelDialogPreisvorschlagPreisliste(InternalFrame oInternalFrameI,
			String title,
			PanelDialogPreisvorschlagDto panelDialogPreisvorschlagDtoI)
			throws Throwable {
		super(oInternalFrameI, title, panelDialogPreisvorschlagDtoI);

		setDefaults();
		initComponents();
	}

	private void setDefaults() throws Throwable {
		// die Liste der berechneten Preise pro Preisliste zusammenstellen
		aVkpfartikelpreislisteDtos = DelegateFactory
				.getInstance()
				.getVkPreisfindungDelegate()
				.getAlleAktivenPreislistenMitHinterlegtemArtikelpreis(
						panelDialogPreisvorschlagDto.getArtikelDto().getIId(),
						panelDialogPreisvorschlagDto.getCNrWaehrung());

		awrbPreislistenname = new WrapperRadioButton[aVkpfartikelpreislisteDtos.length + 2]; // +
		// Handeingabe
		// Rabattsatz
		// und
		// Fixpreis
		// awlaPreislistenname = new WrapperLabel[preislistennameDtos.length];
		awnfRabattsatz = new WrapperNumberField[aVkpfartikelpreislisteDtos.length];
		awlaProzent = new WrapperLabel[aVkpfartikelpreislisteDtos.length];
		awnfBerechneterPreis = new WrapperNumberField[aVkpfartikelpreislisteDtos.length];
		awlaWaehrung = new WrapperLabel[aVkpfartikelpreislisteDtos.length];
		awnfPreisFuerMenge = new WrapperNumberField[aVkpfartikelpreislisteDtos.length];
		aVerkaufspreisDtos = new VerkaufspreisDto[aVkpfartikelpreislisteDtos.length];

		String sAnzeige = null;

		for (int i = 0; i < aVkpfartikelpreislisteDtos.length; i++) {
			int iNummer = i + 1;

			sAnzeige = iNummer + " " + aVkpfartikelpreislisteDtos[i].getCNr();

			awrbPreislistenname[i] = new WrapperRadioButton(sAnzeige);
			awrbPreislistenname[i].setMnemonic(getMnemonicRBArry(iNummer));
			awrbPreislistenname[i].setHorizontalAlignment(SwingConstants.LEFT);
			if (aVkpfartikelpreislisteDtos[i].getIId().equals(
					panelDialogPreisvorschlagDto.getKundeDto()
							.getVkpfArtikelpreislisteIIdStdpreisliste())) {
				awrbPreislistenname[i].setForeground(Color.blue);
			}

			awrbPreislistenname[i]
					.setActionCommand(ACTION_SPECIAL_PREISLISTE_GEWAEHLT);
			awrbPreislistenname[i].addActionListener(this);
			jbgAuswahlliste.add(awrbPreislistenname[i]);

			// Preise fuer den Artikel in dieser Preisliste holen
			VkPreisfindungPreislisteDto preislisteDto = DelegateFactory
					.getInstance()
					.getVkPreisfindungDelegate()
					.getAktuellePreislisteByArtikelIIdPreislisteIId(
							// getNeuestePreislisteByArtikelPreislistenname(
							panelDialogPreisvorschlagDto.getArtikelDto()
									.getIId(),
							aVkpfartikelpreislisteDtos[i].getIId(),
							panelDialogPreisvorschlagDto
									.getDatGueltigkeitsdatumFuerPreise(),
							panelDialogPreisvorschlagDto.getCNrWaehrung());

			awnfRabattsatz[i] = new WrapperNumberField();
			awnfRabattsatz[i].setEditable(false);

			Integer preislisteIId = null;
			if (preislisteDto == null) {
				//wg. SP3032 auskommentiert
				//DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"), LPMain.getTextRespectUISPr("artikel.preisliste.keinepreisliste"));
				//return;
			} else {
				awnfRabattsatz[i].setBigDecimal(preislisteDto
						.getNArtikelstandardrabattsatz());
				preislisteIId = preislisteDto.getVkpfartikelpreislisteIId();

			}
			
			awlaProzent[i] = new WrapperLabel(LPMain.getInstance()
					.getTextRespectUISPr("label.prozent"));
			awlaProzent[i].setHorizontalAlignment(SwingConstants.LEADING);

			// den aktuellen Verkaufpsreis nach der Preisliste berechnen
			BigDecimal nPreisbasis = DelegateFactory
					.getInstance()
					.getVkPreisfindungDelegate()
					.ermittlePreisbasis(
							panelDialogPreisvorschlagDto.getArtikelDto()
									.getIId(),
							panelDialogPreisvorschlagDto
									.getDatGueltigkeitsdatumFuerPreise(),
							preislisteIId,
							panelDialogPreisvorschlagDto.getNMenge(),
							panelDialogPreisvorschlagDto.getCNrWaehrung());

			VerkaufspreisDto vkpInZielwaehrungDto = null;

			if (preislisteDto == null) {
				awrbPreislistenname[i].setEnabled(false);
			} else if (preislisteDto.getNArtikelfixpreis() != null) {

				vkpInZielwaehrungDto = DelegateFactory
						.getInstance()
						.getVkPreisfindungDelegate()
						.berechneVerkaufspreis(
								nPreisbasis,
								preislisteDto.getNArtikelfixpreis(),
								panelDialogPreisvorschlagDto
										.getNMaterialzuschlag());

			} else {
				if (nPreisbasis == null) {
					nPreisbasis = new BigDecimal(0);
				}
				vkpInZielwaehrungDto = DelegateFactory
						.getInstance()
						.getVkPreisfindungDelegate()
						.berechneVerkaufspreis(
								nPreisbasis,
								preislisteDto.getNArtikelstandardrabattsatz()
										.doubleValue(),
								panelDialogPreisvorschlagDto
										.getNMaterialzuschlag());
				if (preislisteDto.getNArtikelstandardrabattsatz().intValue() == 0)
					awrbPreislistenname[i].setEnabled(false);
			}
			// die Anzeige der Preise in diesem Panel erfolgt in der aktuellen
			// Zielwaehrung
			if(vkpInZielwaehrungDto == null) {
				vkpInZielwaehrungDto = new VerkaufspreisDto();
			}
			aVerkaufspreisDtos[i] = vkpInZielwaehrungDto;

			awnfBerechneterPreis[i] = new WrapperNumberField();
			awnfBerechneterPreis[i]
					.setFractionDigits(iPreiseUINachkommastellen);
			awnfBerechneterPreis[i].setBigDecimal(aVerkaufspreisDtos[i]
					.getNettpreisOhneMaterialzuschlag());
			awnfBerechneterPreis[i].setEditable(false);

			// Anzeige der Preise in der Zielwaehrung des Auftrags
			awlaWaehrung[i] = new WrapperLabel(
					panelDialogPreisvorschlagDto.getCNrWaehrung());
			awlaWaehrung[i].setHorizontalAlignment(SwingConstants.LEADING);

			// Preis fuer Menge
			awnfPreisFuerMenge[i] = new WrapperNumberField();
			awnfPreisFuerMenge[i].setFractionDigits(iPreiseUINachkommastellen);
			if (aVerkaufspreisDtos[i].nettopreis != null
					&& panelDialogPreisvorschlagDto.getNMenge() != null)
				awnfPreisFuerMenge[i].setBigDecimal((aVerkaufspreisDtos[i]
						.getNettpreisOhneMaterialzuschlag()
						.multiply(panelDialogPreisvorschlagDto.getNMenge())));
			awnfPreisFuerMenge[i].setEditable(false);

			// wenn der berechnete Preis unter dem minimalen Verkaufspreis liegt
			// -> rote Markierung
			setColorBerechneterPreis(i);

			iZeile++;
			jpaWorkingOn.add(awrbPreislistenname[i], new GridBagConstraints(0,
					iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
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
			jpaWorkingOn.add(awnfPreisFuerMenge[i], new GridBagConstraints(6,
					iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		}

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
		jpaWorkingOn.add(wnfHandeingabeFuerMenge, new GridBagConstraints(6,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(10, 2, 2, 2), 0, 0));
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
					.getVkpStufe2() != null) {
				jpaWorkingOn.add(getWbuZweiteStufe(), new GridBagConstraints(
						iPosButton, iZeile, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(10, 2, 2, 2), 0, 0));
				iPosButton = 4;
			}

			if (panelDialogPreisvorschlagDto.getVkpreisfindungDto()
					.getVkpStufe3() != null) {
				jpaWorkingOn.add(getWbuDritteStufe(), new GridBagConstraints(
						iPosButton, iZeile, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(10, 2, 2, 2), 0, 0));
			}
		} else {
			removeButton(ACTION_SPECIAL_OK); // das Panel ist reine Info
		}

		// die aktuelle Auswahl treffen

		// Dieses Panel kann erst aufgehen, wenn im PanelAuftragPositionen ein
		// Artikel gewaehlt wurde. Zu diesem Zeitpunkt ist sowohl eine
		// Preisliste
		// als auch ein Verkaufspreis hinterlegt, wenn die Preisinformation
		// nicht
		// uebersteuert wurde => iIdPreislisteGewaehlt == Preisliste des Kunden
		// oder
		// die zuletzt manuell gewaehlte Preisliste.

		wnfMaterialzuschlag.setBigDecimal(panelDialogPreisvorschlagDto
				.getNMaterialzuschlag());

		// PJ 15270
		boolean selected = false;
		if (panelDialogPreisvorschlagDto.getIIdPreislisteZuletztGewaehlt() != null
				&& aVkpfartikelpreislisteDtos != null
				&& aVkpfartikelpreislisteDtos.length != 0) {
			for (int i = 0; i < aVkpfartikelpreislisteDtos.length; i++) {
				if (panelDialogPreisvorschlagDto
						.getIIdPreislisteZuletztGewaehlt().intValue() == aVkpfartikelpreislisteDtos[i]
						.getIId().intValue()) {
					if (panelDialogPreisvorschlagDto
							.getAktuellerVerkaufspreisDto().rabattsatz
							.doubleValue() == aVerkaufspreisDtos[i].rabattsatz
							.doubleValue()) {
						if (awrbPreislistenname[i].isEnabled()) {
							awrbPreislistenname[i].setSelected(true);
							selected = true;
						}
						break;
					}
				}
			}
		}
		if (!selected) {

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
					for (int i = 0; i < aVkpfartikelpreislisteDtos.length; i++) {
						if (awnfBerechneterPreis[i].getBigDecimal() != null
								&& awnfBerechneterPreis[i].getBigDecimal()
										.equals(panelDialogPreisvorschlagDto
												.getNFixPreis())) {
							if(awrbPreislistenname[i].isEnabled()) {
								awrbPreislistenname[i].setSelected(true);
								bPreisGefunden = true;
							}
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
						wnfHandeingabeFixpreis
								.addFocusListener(wnfFocusListener);
						wnfHandeingabeFixpreis.setMandatoryField(true);
						wnfHandeingabeFixpreis.setEditable(true);
					}
				}

			}

			// Wenn im Konstruktor keine gewaehlte Preisliste bestimmt wurde
			// oder der Artikel
			// in keiner der Preislisten des Mandanten enthalten ist dann
			// werden Fixpreis und Rabattsatz angegeben, moeglicherweise wurden
			// beide
			// Werte manuell uebersteuert
			// if
			// (panelDialogPreisvorschlagDto.getIIdPreislisteZuletztGewaehlt()
			// == null ||
			// awrbPreislistenname == null || awrbPreislistenname.length == 0) {
			// wenn der Rabattsatz geradzahlig ist, den Rabattsatz markieren,
			// sonst der Fixpreis
			/*
			 * if (panelDialogPreisvorschlagDto.getNFixPreis().compareTo(
			 * Helper.getWertPlusProzent(
			 * panelDialogPreisvorschlagDto.getAktuellerVerkaufspreisDto
			 * ().einzelpreis,
			 * panelDialogPreisvorschlagDto.getAktuellerVerkaufspreisDto
			 * ().rabattsatz.floatValue(), 4)) != 0) {
			 */
			/*
			 * if (panelDialogPreisvorschlagDto.isBNettopreisUebersteuert()) {
			 * wrbHandeingabeFixpreis.setSelected(true);
			 * wnfHandeingabeFixpreis.setEditable(true); selected = true; } else
			 * { wrbHandeingabeRabattsatz.setSelected(true);
			 * wnfHandeingabeRabattsatz.setEditable(true); selected = true; }
			 * 
			 * 
			 * 
			 * wnfHandeingabeFixpreis.setBigDecimal(panelDialogPreisvorschlagDto.
			 * getAktuellerVerkaufspreisDto().einzelpreis);
			 * wnfHandeingabeRabattsatz.setDouble(panelDialogPreisvorschlagDto.
			 * getAktuellerVerkaufspreisDto().rabattsatz);
			 */
		}

		/*
		 * wnfHandeingabeFixpreis.setBigDecimal(panelDialogPreisvorschlagDto.
		 * getNFixPreis ()); if (wnfHandeingabeFixpreis.getBigDecimal() != null)
		 * wnfHandeingabeFuerMenge
		 * .setBigDecimal(wnfHandeingabeFixpreis.getBigDecimal()
		 * .multiply(panelDialogPreisvorschlagDto.getNMenge()));
		 */

		// PJ 09/0013851
		/*
		 * wrbHandeingabeFixpreis.setSelected(true);
		 * wnfHandeingabeFixpreis.setMandatoryField(true);
		 * wnfHandeingabeFixpreis.setEditable(true);
		 */
		// PJ 15270
		// if (!selected) wrbVerkaufspreisbasis.setSelected(true);
		// PJ 15269
		// wnfHandeingabeFixpreis.addFocusListener(wnfFocusListener);
	}

	protected void eventActionAlt(ActionEvent e) throws Throwable {

		if (e.getActionCommand().equals(ALT1)) {
			if (awrbPreislistenname.length > 0
					&& awrbPreislistenname[0] != null)
				awrbPreislistenname[0].setSelected(true);
		} else if (e.getActionCommand().equals(ALT2)) {
			if (awrbPreislistenname.length > 1
					&& awrbPreislistenname[1] != null)
				awrbPreislistenname[1].setSelected(true);
		} else if (e.getActionCommand().equals(ALT3)) {
			if (awrbPreislistenname.length > 2
					&& awrbPreislistenname[2] != null)
				awrbPreislistenname[2].setSelected(true);
		} else if (e.getActionCommand().equals(ALT4)) {
			if (awrbPreislistenname.length > 3
					&& awrbPreislistenname[3] != null)
				awrbPreislistenname[3].setSelected(true);
		} else if (e.getActionCommand().equals(ALT5)) {
			if (awrbPreislistenname.length > 4
					&& awrbPreislistenname[4] != null)
				awrbPreislistenname[4].setSelected(true);
		} else if (e.getActionCommand().equals(ALT6)) {
			if (awrbPreislistenname.length > 5
					&& awrbPreislistenname[5] != null)
				awrbPreislistenname[5].setSelected(true);
		} else if (e.getActionCommand().equals(ALT7)) {
			if (awrbPreislistenname.length > 6
					&& awrbPreislistenname[6] != null)
				awrbPreislistenname[6].setSelected(true);
		} else if (e.getActionCommand().equals(ALT8)) {
			if (awrbPreislistenname.length > 7
					&& awrbPreislistenname[7] != null)
				awrbPreislistenname[7].setSelected(true);
		}
		if (e.getActionCommand().equals(ALT9)) {
			if (awrbPreislistenname.length > 8
					&& awrbPreislistenname[8] != null)
				awrbPreislistenname[8].setSelected(true);
		}
		super.eventActionAlt(e);
	}

	/**
	 * Hier wird das Ergebnis zusammengebaut, das von aussen abgeholt werden
	 * kann.
	 * 
	 * @throws Throwable
	 */
	protected void buildMeinErgebnis() throws Throwable {
		for (int i = 0; i < aVkpfartikelpreislisteDtos.length; i++) {
			if(awrbPreislistenname[i] == null) continue;
			if (awrbPreislistenname[i].isSelected()) {
				// die Preisliste hinterlegen
				panelDialogPreisvorschlagDto
						.setIIdPreislisteZuletztGewaehlt(aVkpfartikelpreislisteDtos[i]
								.getIId());
				// den berechneten Verkaufspreis hinterlegen
				panelDialogPreisvorschlagDto
						.setAktuellerVerkaufspreisDto(aVerkaufspreisDtos[i]);
			}
		}

		ParametermandantDto parameter = (ParametermandantDto) DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getParametermandant(
						ParameterFac.PARAMETER_KUNDEN_POSITIONSKONTIERUNG,
						ParameterFac.KATEGORIE_KUNDEN,
						LPMain.getTheClient().getMandant());
		boolean bPositionskontierung = ((Boolean) parameter.getCWertAsObject());

		if (wrbHandeingabeFixpreis.isSelected()) {
			panelDialogPreisvorschlagDto.setIIdPreislisteZuletztGewaehlt(null);
			panelDialogPreisvorschlagDto
					.setAktuellerVerkaufspreisDto(new VerkaufspreisDto());

			if (wnfHandeingabeFixpreis.getText() != null
					&& !wnfHandeingabeFixpreis.getText().equals("")) {
				// Aktuellen MWST-Satz uebersetzen.
				MwstsatzDto mwstsatzDtoAktuell = DelegateFactory
						.getInstance()
						.getMandantDelegate()
						.mwstsatzFindByMwstsatzbezIIdAktuellster(
								panelDialogPreisvorschlagDto.getKundeDto()
										.getMwstsatzbezIId());

				if (bPositionskontierung
						&& panelDialogPreisvorschlagDto.getArtikelDto()
								.getMwstsatzbezIId() != null) {

					mwstsatzDtoAktuell = DelegateFactory
							.getInstance()
							.getMandantDelegate()
							.mwstsatzFindByMwstsatzbezIIdAktuellster(
									panelDialogPreisvorschlagDto
											.getArtikelDto()
											.getMwstsatzbezIId());
				}

				if (nVkbasisInBelegwaehrung.intValue() == 0)
					nVkbasisInBelegwaehrung = wnfHandeingabeFixpreis
							.getBigDecimal();
				//auskommentiert wg. SP3124
				//if (wnfHandeingabeFixpreis.getBigDecimal().intValue() == 0)
				//	wnfHandeingabeFixpreis
				//			.setBigDecimal(nVkbasisInBelegwaehrung);
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
							.berechneVerkaufspreis(
									nVkbasisInBelegwaehrung,
									nVkbasisInBelegwaehrung,
									panelDialogPreisvorschlagDto
											.getNMaterialzuschlag()));

		} else if (wrbHandeingabeRabattsatz.isSelected()) {
			panelDialogPreisvorschlagDto.setIIdPreislisteZuletztGewaehlt(null);
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

				if (bPositionskontierung
						&& panelDialogPreisvorschlagDto.getArtikelDto()
								.getMwstsatzbezIId() != null) {

					mwstsatzDtoAktuell = DelegateFactory
							.getInstance()
							.getMandantDelegate()
							.mwstsatzFindByMwstsatzbezIIdAktuellster(
									panelDialogPreisvorschlagDto
											.getArtikelDto()
											.getMwstsatzbezIId());
				}

				// wenn es in der Anzeige einen Fixpreis gibt, dann gilt dieser
				// als Basis
				// und wird nicht ueberschrieben
				/*
				 * if (wnfHandeingabeFixpreis.getBigDecimal() != null) {
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
				awnfPreisFuerMenge[index].setForeground(Color.red);
			} else {
				awnfBerechneterPreis[index].setForeground(Color.black);
				awnfPreisFuerMenge[index].setForeground(Color.black);
			}
		}
	}

	/**
	 * Der Button fuer die Anzeige einer zweiten VK-Stufe muss initialisert
	 * werden. <br>
	 * Die zweite VK-Stufe sind die VK-Mengenstaffeln.
	 * 
	 * @throws Throwable
	 */
	protected void initZweiteVkstufe() throws Throwable {
		getWbuZweiteStufe().setText(
				LPMain.getInstance()
						.getTextRespectUISPr("vkpf.vkmengenstaffel"));
		getWbuZweiteStufe().setActionCommand(ACTION_SPECIAL_VKPFSTUFE2);
	}

	/**
	 * Der Button fuer die Anzeige einer dritten VK-Stufe muss initialisert
	 * werden. <br>
	 * Die dritte VK-Stufe sind die Kundensokos.
	 * 
	 * @throws Throwable
	 */
	protected void initDritteVkstufe() throws Throwable {
		getWbuDritteStufe().setText(
				LPMain.getInstance().getTextRespectUISPr("vkpf.kundesoko"));
		getWbuDritteStufe().setActionCommand(ACTION_SPECIAL_VKPFSTUFE3);
	}
}
