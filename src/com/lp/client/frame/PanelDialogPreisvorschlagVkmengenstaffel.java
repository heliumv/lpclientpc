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
import com.lp.client.pc.LPMain;
import com.lp.server.artikel.service.VerkaufspreisDto;
import com.lp.server.artikel.service.VkpfMengenstaffelDto;
import com.lp.server.artikel.service.VkpfartikelpreislisteDto;
import com.lp.server.artikel.service.VkpreisfindungDto;
import com.lp.server.system.service.MwstsatzDto;
import com.lp.util.Helper;

/**
 * <p>
 * <I>Dialog zur Auswahl von Verkaufspreisen aufgrund von VK-Mengenstaffeln</I>
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
 * @version $Revision: 1.13 $
 */
@SuppressWarnings("static-access")
public class PanelDialogPreisvorschlagVkmengenstaffel extends
		PanelDialogPreisvorschlag {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private WrapperLabel wlaMenge = null;
	private WrapperNumberField wnfMenge = null;
	private WrapperLabel wlaMengeeinheit = null;

	private WrapperRadioButton[] awrbVkmengenstaffel = null;
	private WrapperNumberField[] awnfRabattsatz = null;
	private WrapperLabel[] awlaProzent = null;
	private WrapperNumberField[] awnfBerechneterPreis = null;
	private WrapperLabel[] awlaWaehrung = null;

	private WrapperNumberField wnfPreisBasisAusPreisliste = new WrapperNumberField();

	/** Alle vorhandenen Mengenstaffeln fuer diesen Mandanten. */
	private VkpfMengenstaffelDto[] aVkpfMengenstaffelDtos = null;

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
	public PanelDialogPreisvorschlagVkmengenstaffel(
			InternalFrame oInternalFrameI, String title,
			PanelDialogPreisvorschlagDto panelDialogPreisvorschlagDtoI)
			throws Throwable {
		super(oInternalFrameI, title, panelDialogPreisvorschlagDtoI);

		jbInit();
		setDefaults();
		initComponents();
	}

	private void jbInit() throws Throwable {
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

		iZeile++;
		// PJ 07/8670

		VkpfartikelpreislisteDto plDto = DelegateFactory
				.getInstance()
				.getVkPreisfindungDelegate()
				.vkpfartikelpreislisteFindByPrimaryKey(
						panelDialogPreisvorschlagDto.getKundeDto()
								.getVkpfArtikelpreislisteIIdStdpreisliste());

		WrapperLabel wlaPreisliste = new WrapperLabel(plDto.getCNr());
		wlaPreisliste.setHorizontalAlignment(SwingConstants.LEADING);

		wlaPreisliste.setForeground(Color.blue);

		jpaWorkingOn.add(wlaPreisliste, new GridBagConstraints(0, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		wnfPreisBasisAusPreisliste.setEditable(false);
		jpaWorkingOn.add(wnfPreisBasisAusPreisliste, new GridBagConstraints(1,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		WrapperLabel wlaWaehrung = new WrapperLabel(
				panelDialogPreisvorschlagDto.getCNrWaehrung());
		wlaWaehrung.setHorizontalAlignment(SwingConstants.LEADING);
		jpaWorkingOn.add(wlaWaehrung, new GridBagConstraints(2, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));

		// die Liste der berechneten Preise pro VK-Mengenstaffel zusammenstellen
		aVkpfMengenstaffelDtos = DelegateFactory
				.getInstance()
				.getVkPreisfindungDelegate()
				.vkpfMengenstaffelFindByArtikelIIdGueltigkeitsdatum(
						panelDialogPreisvorschlagDto.getArtikelDto().getIId(),
						panelDialogPreisvorschlagDto
								.getDatGueltigkeitsdatumFuerPreise(),
						panelDialogPreisvorschlagDto.getKundeDto()
								.getVkpfArtikelpreislisteIIdStdpreisliste());

		wnfPreisBasisAusPreisliste.setBigDecimal(DelegateFactory
				.getInstance()
				.getVkPreisfindungDelegate()
				.ermittlePreisbasis(
						panelDialogPreisvorschlagDto.getArtikelDto().getIId(),
						panelDialogPreisvorschlagDto
								.getDatGueltigkeitsdatumFuerPreise(),
						panelDialogPreisvorschlagDto.getKundeDto()
								.getVkpfArtikelpreislisteIIdStdpreisliste(),
						panelDialogPreisvorschlagDto.getNMenge(),
						panelDialogPreisvorschlagDto.getCNrWaehrung()));

		awrbVkmengenstaffel = new WrapperRadioButton[aVkpfMengenstaffelDtos.length + 2]; // +
																							// Handeingabe
																							// Rabattsatz
																							// und
																							// Fixpreis
		awnfRabattsatz = new WrapperNumberField[aVkpfMengenstaffelDtos.length];
		awlaProzent = new WrapperLabel[aVkpfMengenstaffelDtos.length];
		awnfBerechneterPreis = new WrapperNumberField[aVkpfMengenstaffelDtos.length];
		awlaWaehrung = new WrapperLabel[aVkpfMengenstaffelDtos.length];

		aVerkaufspreisDtos = new VerkaufspreisDto[aVkpfMengenstaffelDtos.length];

		String sAnzeige = null;

		for (int i = 0; i < aVkpfMengenstaffelDtos.length; i++) {
			int iNummer = i + 1;
			sAnzeige = iNummer
					+ " "
					+ Helper.formatZahl(aVkpfMengenstaffelDtos[i].getNMenge(),
							2, LPMain.getInstance().getTheClient().getLocUi())
					+ " "
					+ panelDialogPreisvorschlagDto.getArtikelDto()
							.getEinheitCNr().trim();

			awrbVkmengenstaffel[i] = new WrapperRadioButton(sAnzeige);
			awrbVkmengenstaffel[i].setMnemonic(getMnemonicRBArry(iNummer));
			awrbVkmengenstaffel[i].setHorizontalAlignment(SwingConstants.LEFT);

			awrbVkmengenstaffel[i]
					.setActionCommand(ACTION_SPECIAL_PREISLISTE_GEWAEHLT);

			awrbVkmengenstaffel[i].addActionListener(this);
			jbgAuswahlliste.add(awrbVkmengenstaffel[i]);

			awnfRabattsatz[i] = new WrapperNumberField();
			awnfRabattsatz[i].setEditable(false);
			awnfRabattsatz[i].setDouble(aVkpfMengenstaffelDtos[i]
					.getFArtikelstandardrabattsatz());

			awlaProzent[i] = new WrapperLabel(LPMain.getInstance()
					.getTextRespectUISPr("label.prozent"));
			awlaProzent[i].setHorizontalAlignment(SwingConstants.LEADING);

			// Aktuellen MWST-Satz uebersetzen.
			MwstsatzDto mwstsatzDtoAktuell = DelegateFactory
					.getInstance()
					.getMandantDelegate()
					.mwstsatzFindByMwstsatzbezIIdAktuellster(
							panelDialogPreisvorschlagDto.getKundeDto()
									.getMwstsatzbezIId());
			// den aktuellen Verkaufpsreis berechnen
			VkpreisfindungDto vkpreisfindungDto = DelegateFactory
					.getInstance()
					.getVkPreisfindungDelegate()
					.verkaufspreisfindung(
							panelDialogPreisvorschlagDto.getArtikelDto()
									.getIId(),
							panelDialogPreisvorschlagDto.getKundeDto().getIId(),
							aVkpfMengenstaffelDtos[i].getNMenge(),
							panelDialogPreisvorschlagDto
									.getDatGueltigkeitsdatumFuerPreise(),
							aVkpfMengenstaffelDtos[i]
									.getVkpfartikelpreislisteIId(),
							mwstsatzDtoAktuell.getIId(),
							panelDialogPreisvorschlagDto.getCNrWaehrung());

			aVerkaufspreisDtos[i] = Helper
					.getVkpreisBerechnet(vkpreisfindungDto);

			// Wenn von der selben Preisliste wie Kunde, dann blau:
			if (panelDialogPreisvorschlagDto
					.getKundeDto()
					.getVkpfArtikelpreislisteIIdStdpreisliste()
					.equals(aVkpfMengenstaffelDtos[i]
							.getVkpfartikelpreislisteIId())) {
				awrbVkmengenstaffel[i].setForeground(Color.blue);
			}

			awnfBerechneterPreis[i] = new WrapperNumberField();
			awnfBerechneterPreis[i]
					.setFractionDigits(iPreiseUINachkommastellen);
			awnfBerechneterPreis[i]
					.setBigDecimal(aVerkaufspreisDtos[i].getNettpreisOhneMaterialzuschlag());
			awnfBerechneterPreis[i].setEditable(false);

			// wenn der berechnete Preis unter dem minimalen Verkaufspreis liegt
			// -> rote Markierung
			setColorBerechneterPreis(i);

			// Anzeige der Preise in der Zielwaehrung des Auftrags
			awlaWaehrung[i] = new WrapperLabel(
					panelDialogPreisvorschlagDto.getCNrWaehrung());
			awlaWaehrung[i].setHorizontalAlignment(SwingConstants.LEADING);

			iZeile++;
			jpaWorkingOn.add(awrbVkmengenstaffel[i], new GridBagConstraints(0,
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
				jpaWorkingOn.add(getWbuZweiteStufe(), new GridBagConstraints(
						iPosButton, iZeile, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(10, 2, 2, 2), 0, 0));
				iPosButton = 4;
			}

			if (panelDialogPreisvorschlagDto.getVkpreisfindungDto()
					.getVkpStufe3() != null) {
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

		// lt. WH: 01/03/2011

		wnfHandeingabeFixpreis.setBigDecimal(panelDialogPreisvorschlagDto
				.getAktuellerVerkaufspreisDto().einzelpreis);
		wnfHandeingabeRabattsatz.setDouble(panelDialogPreisvorschlagDto
				.getAktuellerVerkaufspreisDto().rabattsatz);

		wnfMaterialzuschlag.setBigDecimal(panelDialogPreisvorschlagDto
				.getNMaterialzuschlag());
		
		if (panelDialogPreisvorschlagDto.getNFixPreis() != null) {

			if (wnfVerkaufspreisbasis.getBigDecimal() != null
					&& wnfVerkaufspreisbasis.getBigDecimal().equals(
							panelDialogPreisvorschlagDto.getNFixPreis())) {
				wrbVerkaufspreisbasis.setSelected(true);

			} else {

				boolean bPreisGefunden = false;
				for (int i = 0; i < aVkpfMengenstaffelDtos.length; i++) {
					if (awnfBerechneterPreis[i].getBigDecimal() != null
							&& awnfBerechneterPreis[i].getBigDecimal()
									.equals(panelDialogPreisvorschlagDto
											.getNFixPreis())) {
						awrbVkmengenstaffel[i].setSelected(true);
						bPreisGefunden = true;
					}
				}
				if (bPreisGefunden == false) {
					
					BigDecimal fixPreis = panelDialogPreisvorschlagDto
							.getNFixPreis();
					if (panelDialogPreisvorschlagDto.getNMaterialzuschlag() != null) {
						fixPreis=fixPreis.subtract(panelDialogPreisvorschlagDto.getNMaterialzuschlag());
					}
					
					wrbHandeingabeFixpreis.setSelected(true);
					wnfHandeingabeFixpreis
							.setBigDecimal(fixPreis);
					wnfHandeingabeFixpreis.setMandatoryField(true);
					wnfHandeingabeFixpreis.setEditable(true);
				}
			}

		}

		/*
		 * // Dieses Panel kann erst aufgehen, wenn im PanelAuftragPositionen
		 * ein // Artikel gewaehlt wurde. Zu diesem Zeitpunkt ist sowohl eine
		 * Menge // als auch ein Verkaufspreis hinterlegt, wenn die
		 * Preisinformation // nicht // uebersteuert wurde if
		 * (panelDialogPreisvorschlagDto.getIIdVkmengenstaffelZuletztGewaehlt()
		 * != null && aVkpfMengenstaffelDtos != null &&
		 * aVkpfMengenstaffelDtos.length != 0) {
		 * 
		 * // wnfPreisBasisAusPreisliste
		 * 
		 * for (int i = 0; i < aVkpfMengenstaffelDtos.length; i++) { if
		 * (panelDialogPreisvorschlagDto
		 * .getIIdVkmengenstaffelZuletztGewaehlt().intValue() ==
		 * aVkpfMengenstaffelDtos[i] .getIId().intValue()) {
		 * awrbVkmengenstaffel[i].setSelected(true); } } } else { // Wenn im
		 * Konstruktor keine gewaehlte Vkpfmengenstaffel bestimmt // wurde oder
		 * der Artikel // in keiner der Mengenstaffeln enthalten ist dann //
		 * werden Fixpreis und Rabattsatz angegeben, moeglicherweise wurden //
		 * beide // Werte manuell uebersteuert
		 * 
		 * // wenn der Rabattsatz geradzahlig ist, den Rabattsatz markieren, //
		 * sonst der Fixpreis if
		 * ((panelDialogPreisvorschlagDto.getAktuellerVerkaufspreisDto
		 * ().rabattsatz .doubleValue() - panelDialogPreisvorschlagDto
		 * .getAktuellerVerkaufspreisDto().rabattsatz .intValue() > 0) ||
		 * panelDialogPreisvorschlagDto
		 * .getAktuellerVerkaufspreisDto().rabattsatz .doubleValue() == 0) {
		 * wrbHandeingabeFixpreis.setSelected(true);
		 * wnfHandeingabeFixpreis.setEditable(true); } else {
		 * wrbHandeingabeRabattsatz.setSelected(true);
		 * wnfHandeingabeRabattsatz.setEditable(true); }
		 * 
		 * wnfHandeingabeFixpreis.setBigDecimal(panelDialogPreisvorschlagDto
		 * .getAktuellerVerkaufspreisDto().einzelpreis);
		 * wnfHandeingabeRabattsatz.setDouble(panelDialogPreisvorschlagDto
		 * .getAktuellerVerkaufspreisDto().rabattsatz); }
		 * wnfHandeingabeFixpreis.setBigDecimal(panelDialogPreisvorschlagDto
		 * .getNFixPreis()); // PJ 09/0013851 /*
		 * wrbHandeingabeFixpreis.setSelected(true);
		 * wnfHandeingabeFixpreis.setMandatoryField(true);
		 * wnfHandeingabeFixpreis.setEditable(true);
		 */
		// wrbVerkaufspreisbasis.setSelected(true);

	}

	protected void eventActionAlt(ActionEvent e) throws Throwable {
		super.eventActionAlt(e);
		if (e.getActionCommand().equals(ALT1)) {
			if (awrbVkmengenstaffel.length > 0
					&& awrbVkmengenstaffel[0] != null)
				awrbVkmengenstaffel[0].setSelected(true);
		} else if (e.getActionCommand().equals(ALT2)) {
			if (awrbVkmengenstaffel.length > 1
					&& awrbVkmengenstaffel[1] != null)
				awrbVkmengenstaffel[1].setSelected(true);
		} else if (e.getActionCommand().equals(ALT3)) {
			if (awrbVkmengenstaffel.length > 2
					&& awrbVkmengenstaffel[2] != null)
				awrbVkmengenstaffel[2].setSelected(true);
		} else if (e.getActionCommand().equals(ALT4)) {
			if (awrbVkmengenstaffel.length > 3
					&& awrbVkmengenstaffel[3] != null)
				awrbVkmengenstaffel[3].setSelected(true);
		} else if (e.getActionCommand().equals(ALT5)) {
			if (awrbVkmengenstaffel.length > 4
					&& awrbVkmengenstaffel[4] != null)
				awrbVkmengenstaffel[4].setSelected(true);
		} else if (e.getActionCommand().equals(ALT6)) {
			if (awrbVkmengenstaffel.length > 5
					&& awrbVkmengenstaffel[5] != null)
				awrbVkmengenstaffel[5].setSelected(true);
		} else if (e.getActionCommand().equals(ALT7)) {
			if (awrbVkmengenstaffel.length > 6
					&& awrbVkmengenstaffel[6] != null)
				awrbVkmengenstaffel[6].setSelected(true);
		} else if (e.getActionCommand().equals(ALT8)) {
			if (awrbVkmengenstaffel.length > 7
					&& awrbVkmengenstaffel[7] != null)
				awrbVkmengenstaffel[7].setSelected(true);
		}
		if (e.getActionCommand().equals(ALT9)) {
			if (awrbVkmengenstaffel.length > 8
					&& awrbVkmengenstaffel[8] != null)
				awrbVkmengenstaffel[8].setSelected(true);
		}
	}

	/**
	 * Hier wird das Ergebnis zusammengebaut, das von aussen abgeholt werden
	 * kann.
	 * 
	 * @throws Throwable
	 */
	protected void buildMeinErgebnis() throws Throwable {
		for (int i = 0; i < aVkpfMengenstaffelDtos.length; i++) {
			if (awrbVkmengenstaffel[i].isSelected()) {

				// die Mengenstaffel hinterlegen
				panelDialogPreisvorschlagDto
						.setIIdVkmengenstaffelZuletztGewaehlt(aVkpfMengenstaffelDtos[i]
								.getIId());

				// den berechneten Verkaufspreis hinterlegen
				panelDialogPreisvorschlagDto
						.setAktuellerVerkaufspreisDto(aVerkaufspreisDtos[i]);
			}
		}

		if (wrbHandeingabeFixpreis.isSelected()) {
			panelDialogPreisvorschlagDto
					.setIIdVkmengenstaffelZuletztGewaehlt(null);
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
				if (nVkbasisInBelegwaehrung.intValue() == 0)
					nVkbasisInBelegwaehrung = wnfHandeingabeFixpreis
							.getBigDecimal();
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
					.setIIdVkmengenstaffelZuletztGewaehlt(null);
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
