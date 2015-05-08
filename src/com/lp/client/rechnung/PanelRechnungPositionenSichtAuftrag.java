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
package com.lp.client.rechnung;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;

import com.lp.client.auftrag.PanelPositionenSichtAuftragSNR;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.LockStateValue;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelPositionenArtikelVerkauf;
import com.lp.client.frame.component.PanelPositionenArtikelVerkaufSNR;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.SeriennrChargennrMitMengeDto;
import com.lp.server.auftrag.service.AuftragDto;
import com.lp.server.auftrag.service.AuftragServiceFac;
import com.lp.server.auftrag.service.AuftragpositionDto;
import com.lp.server.rechnung.service.RechnungPositionDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.LockMeDto;
import com.lp.service.Artikelset;
import com.lp.service.BelegpositionVerkaufDto;
import com.lp.util.Helper;

/**
 * <p>
 * In diesem Detailfenster der Rechnung werden Positionen erfasst bzw.
 * geaendert.
 * </p>
 * 
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2008
 * </p>
 * <p>
 * Erstellungsdatum 2004-10-13
 * </p>
 * 
 * @author Uli Walch
 * 
 * @version $Revision: 1.20 $
 */
public class PanelRechnungPositionenSichtAuftrag extends
		PanelPositionenSichtAuftragSNR {

	private static final long serialVersionUID = 1L;
	private final TabbedPaneRechnung tpRechnung;
	private LockMeDto lockMeAuftrag = null;
	private RechnungPositionDto rechnungpositionAusAuftragpositionDto = null;
	private InternalFrameRechnung intFrame = null;

	/**
	 * Konstruktor.
	 * 
	 * @param internalFrame
	 *            der InternalFrame auf dem das Panel sitzt
	 * @param add2TitleI
	 *            der default Titel des Panels
	 * @param key
	 *            PK des Auftrags
	 * @param iIdLagerI
	 *            von diesem Lager soll abgebucht werden
	 * @param tpRechnung
	 *            TabbedPaneRechnung
	 * @throws Throwable
	 *             Ausnahme
	 */
	public PanelRechnungPositionenSichtAuftrag(InternalFrame internalFrame,
			String add2TitleI, Object key, Integer iIdLagerI,
			TabbedPaneRechnung tpRechnung) throws Throwable {
		super(internalFrame, add2TitleI, key, iIdLagerI);
		this.intFrame = (InternalFrameRechnung) internalFrame;
		this.tpRechnung = tpRechnung;
		jbInitPanel();
		initComponents();
	}

	private void jbInitPanel() throws Throwable {
		// Statusbar an den unteren Rand des Panels haengen
		iZeile++;
		add(getPanelStatusbar(), new GridBagConstraints(0, iZeile, 1, 1, 1.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));

		// einfache Preisauswahl ueber Dialog deaktivieren
		((PanelPositionenArtikelVerkauf) panelArtikel)
				.remove(((PanelPositionenArtikelVerkauf) panelArtikel).wbuPreisauswahl);

		((PanelPositionenArtikelVerkauf) panelArtikel)
				.add((panelArtikel).wlaEinzelpreis, new GridBagConstraints(5,
						2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
						GridBagConstraints.BOTH, new Insets(10, 2, 2, 2), 0, 0));
		// Labels uebersteuern
		panelArtikel.wlaMenge.setText(LPMain
				.getTextRespectUISPr("re.label.mengere"));
		panelHandeingabe.wlaMenge.setText(LPMain
				.getTextRespectUISPr("re.label.mengere"));
	}

	protected void setDefaults() throws Throwable {
		super.setDefaults();

		if (tpRechnung.pruefeAktuelleRechnung()) {
			// es gilt die waehrung der Rechnung
			panelArtikel.setWaehrungCNr(tpRechnung.getRechnungDto()
					.getWaehrungCNr());
			panelHandeingabe.setWaehrungCNr(tpRechnung.getRechnungDto()
					.getWaehrungCNr());
		}
	}

	protected void eventActionUpdate(ActionEvent aE, boolean bNeedNoUpdateI)
			throws Throwable {
		if (intFrame.isUpdateAllowedForRechnungDto(tpRechnung.getRechnungDto())) {
			boolean bAendern = true;

			AuftragDto auftragDto = DelegateFactory.getInstance()
					.getAuftragDelegate()
					.auftragFindByPrimaryKey(oAuftragpositionDto.getBelegIId());

			if (auftragDto.getStatusCNr().equals(
					AuftragServiceFac.AUFTRAGSTATUS_ERLEDIGT)) {
				DialogFactory
						.showModalDialog(
								LPMain.getTextRespectUISPr("lp.hint"),
								LPMain.getTextRespectUISPr("ls.error.auftragbereitserledigt"));
				bAendern = false;
				return;
			}

			if (oAuftragpositionDto.getNOffeneMenge() != null
					&& oAuftragpositionDto.getNOffeneMenge().doubleValue() < 0) {
				DialogFactory
						.showModalDialog(
								LPMain.getTextRespectUISPr("lp.hint"),
								LPMain.getTextRespectUISPr("rech.sichtauftrag.negativemengen.error"));
				return;
			}

			if (oAuftragpositionDto.getAuftragpositionstatusCNr().equals(
					AuftragServiceFac.AUFTRAGPOSITIONSTATUS_ERLEDIGT)) {
				bAendern = DialogFactory
						.showModalJaNeinDialog(
								getInternalFrame(),
								LPMain.getTextRespectUISPr("ls.warning.auftragspositionerledigt.trotzdemaendern"));
			}
			// PJ 14648 Setartikel
			if (panelArtikel.getArtikelDto() != null
					&& panelArtikel.getArtikelDto().getIId() != null
					&& oAuftragpositionDto.getPositioniIdArtikelset() != null) {
				if (Helper.short2boolean(panelArtikel.getArtikelDto()
						.getBSeriennrtragend())
						|| Helper.short2boolean(panelArtikel.getArtikelDto()
								.getBChargennrtragend())) {

				} else {
					// Es kann nur das Artikel-Set geaendert werden
					DialogFactory
							.showModalDialog(
									LPMain.getTextRespectUISPr("lp.hint"),
									LPMain.getTextRespectUISPr("ls.warning.setartikelnichtaenderbar"));
					return;
				}
			}
			if (bAendern) {
				// IMS 1646 pruefen, ob die zugehoerige Auftragposition noch
				// existiert
				boolean bAuftragpositionExistiert = true;

				try {
					oAuftragpositionDto = DelegateFactory
							.getInstance()
							.getAuftragpositionDelegate()
							.auftragpositionFindByPrimaryKey(
									oAuftragpositionDto.getIId());
				} catch (ExceptionLP ex) {
					bAuftragpositionExistiert = false;
					oAuftragpositionDto = null;

					DialogFactory.showModalDialog(
							LPMain.getTextRespectUISPr("lp.hint"),
							LPMain.getTextRespectUISPr("lp.hint.geloescht"));
				}

				if (bAuftragpositionExistiert) {
					// pruefen, ob es zu dieser Auftragposition bereits eines LS
					// Position gibt
					rechnungpositionAusAuftragpositionDto = DelegateFactory
							.getInstance()
							.getRechnungDelegate()
							.getRechnungPositionByRechnungAuftragposition(
									tpRechnung.getRechnungDto().getIId(),
									oAuftragpositionDto.getIId());

				}
				// super.eventActionUpdate(aE, false);

				zeigeSerienchargennummer(true, false);

				super.eventActionUpdate(aE, false); // die Buttons muessen auf

				if (LocaleFac.POSITIONSART_IDENT.equals(oAuftragpositionDto
						.getPositionsartCNr())) {
					// IMS 1553 Wenn die Rechnungposition neu angelegt
					// wird, ist der
					// Vorschlagswert die offene Menge im Auftrag, sonst
					// die bereits erfasste
					// Menge in der Rechnung
					if (rechnungpositionAusAuftragpositionDto == null) {
						if (bOffeneMengeVorschlagen) {
							befuelleMengeMitVorschlagswert();
						} else {
							panelArtikel.wnfMenge.setBigDecimal(null);
						}
					} else {
						panelArtikel.wnfMenge
								.setBigDecimal(rechnungpositionAusAuftragpositionDto
										.getNMenge());
					}
					// if
					// (!Helper.short2boolean(panelArtikel.artikelDto.
					// getBChargennrtragend()) &&
					// !Helper.short2boolean(panelArtikel.artikelDto.
					// getBSeriennrtragend())) {
				} else if (oAuftragpositionDto.getPositionsartCNr().equals(
						LocaleFac.POSITIONSART_HANDEINGABE)) {
					// IMS 1553 Wenn die Rechnungsposition neu angelegt
					// wird, ist der
					// Vorschlagswert die offene Menge im Auftrag, sonst
					// die bereits erfasste
					// Menge in der Rechnung
					if (rechnungpositionAusAuftragpositionDto == null) {
						if (bOffeneMengeVorschlagen) {
							panelHandeingabe.wnfMenge
									.setBigDecimal(oAuftragpositionDto
											.getNOffeneMenge());
						} else {
							panelHandeingabe.wnfMenge.setBigDecimal(null);
						}
					} else {
						panelHandeingabe.wnfMenge
								.setBigDecimal(rechnungpositionAusAuftragpositionDto
										.getNMenge());
					}
				}

				if (oAuftragpositionDto.getPositionsartCNr().equals(
						LocaleFac.POSITIONSART_IDENT)) {
					if (panelArtikel.getArtikelDto().getIId() != null
							&& (Helper.short2boolean(panelArtikel
									.getArtikelDto().getBChargennrtragend()) || Helper
									.short2boolean(panelArtikel.getArtikelDto()
											.getBSeriennrtragend()))) {
						panelArtikel.wnfMenge.setEditable(false);

					} else {
						panelArtikel.wnfMenge.setEditable(true);
					}
				}

			}
		}
	}

	/**
	 * Anzeige saemtlicher Felder der Auftragposition. <br>
	 * Es kann nichts veraendert werden, ausser die Menge in der Rechnung. <br>
	 * Hier muessen die Anzeige der offenen Menge und der gelieferten Menge
	 * konistent gehalten werden.
	 * 
	 * @throws Throwable
	 *             Ausnahme
	 */
	protected void auftragpositionDto2Components() throws Throwable {
		// fuer positive auftragsmengen nur positive mengen liefern
		if (oAuftragpositionDto.getNMenge() != null) {

			if (oAuftragpositionDto.getNMenge().doubleValue() > 0) {
				panelArtikel.wnfMenge.setMinimumValue(new BigDecimal(0));
				panelHandeingabe.wnfMenge.setMinimumValue(new BigDecimal(0));
				panelArtikel.wnfMenge.setMaximumValue(null);
				panelHandeingabe.wnfMenge.setMaximumValue(null);
			}
			// fuer negative auftragsmengen nur negative mengen liefern
			else if (oAuftragpositionDto.getNMenge().doubleValue() < 0) {
				panelArtikel.wnfMenge.setMinimumValue(null);
				panelHandeingabe.wnfMenge.setMinimumValue(null);
				panelArtikel.wnfMenge.setMaximumValue(0);
				panelHandeingabe.wnfMenge.setMaximumValue(0);
			}
		}

		super.auftragpositionDto2Components();

		if (oAuftragpositionDto.getNMenge() != null) {
			// der Wert im Feld "Menge LS" ist zunaechst jene Menge, die zu
			// dieser
			// Auftragposition tatsaechlich bereits in der Rechnung erfasst ist;
			// wenn es keine
			// Position gibt, dann ist die Menge 0; SNR/Chargennummer
			// beruecksichtigen
			RechnungPositionDto oRechnungpositionDto = DelegateFactory
					.getInstance()
					.getRechnungDelegate()
					.getRechnungPositionByRechnungAuftragposition(
							tpRechnung.getRechnungDto().getIId(),
							oAuftragpositionDto.getIId());

			if (oRechnungpositionDto != null
					&& oRechnungpositionDto.getRechnungpositionartCNr().equals(
							LocaleFac.POSITIONSART_IDENT)) {
				panelArtikel.wnfMenge.setBigDecimal(oRechnungpositionDto
						.getNMenge());

				if (oRechnungpositionDto.getSeriennrChargennrMitMenge() != null) {
					((PanelPositionenArtikelVerkaufSNR) panelArtikel).wtfSerienchargennummer
							.setSeriennummern(oRechnungpositionDto
									.getSeriennrChargennrMitMenge(),
									oRechnungpositionDto.getArtikelIId(),
									tpRechnung.getRechnungDto().getLagerIId());
				}
			} else if (oRechnungpositionDto != null
					&& oRechnungpositionDto.getRechnungpositionartCNr().equals(
							LocaleFac.POSITIONSART_HANDEINGABE)) {
				panelHandeingabe.wnfMenge.setBigDecimal(oRechnungpositionDto
						.getNMenge());
			}

			wnfOffenimauftragArtikel.setBigDecimal(oAuftragpositionDto
					.getNOffeneMenge());
			wnfGeliefertArtikel.setBigDecimal(oAuftragpositionDto.getNMenge()
					.subtract(oAuftragpositionDto.getNOffeneMenge()));

			wnfOffenimauftragHand.setBigDecimal(oAuftragpositionDto
					.getNOffeneMenge());
			wnfGeliefertHand.setBigDecimal(oAuftragpositionDto.getNMenge()
					.subtract(oAuftragpositionDto.getNOffeneMenge()));
		}
	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {
		if (allMandatoryFieldsSetDlg()) {
			checkLockedDlg();
			if (lockMeAuftrag != null) {
				checkLockedDlg(lockMeAuftrag);
			}

			// @todo redundant? vorerst zur Sicherheit PJ 5000
			rechnungpositionAusAuftragpositionDto = DelegateFactory
					.getInstance()
					.getRechnungDelegate()
					.getRechnungPositionByRechnungAuftragposition(
							tpRechnung.getRechnungDto().getIId(),
							oAuftragpositionDto.getIId());

			if (oAuftragpositionDto.getPositionsartCNr().equals(
					LocaleFac.POSITIONSART_IDENT)) {

				// artikelkommentar zeigen
				DelegateFactory
						.getInstance()
						.getArtikelkommentarDelegate()
						.pruefeArtikel(panelArtikel.getArtikelDto(),
								intFrame.getBelegartCNr(), intFrame);

				if (oAuftragpositionDto.getNMenge().doubleValue() > 0) {
					saveIdentPositionPositiveMenge(e, false);
				} else if (oAuftragpositionDto.getNMenge().doubleValue() < 0) {
					saveIdentPositionNegativeMenge(e, false);
				}
				if (oAuftragpositionDto.getNOffeneMenge() != null)
					// die Anzeige fuer die naechste Eingabe aktualisieren
					wnfOffenimauftragArtikel.setBigDecimal(oAuftragpositionDto
							.getNOffeneMenge());
				if (oAuftragpositionDto.getNMenge() != null
						&& oAuftragpositionDto.getNOffeneMenge() != null)
					wnfGeliefertArtikel.setBigDecimal(oAuftragpositionDto
							.getNMenge().subtract(
									oAuftragpositionDto.getNOffeneMenge()));
			} else if (oAuftragpositionDto.getPositionsartCNr().equals(
					LocaleFac.POSITIONSART_HANDEINGABE)) {
				saveHandeingabePosition(e, false);
				if (oAuftragpositionDto.getNOffeneMenge() != null)
					// die Anzeige fuer die naechste Eingabe aktualisieren
					wnfOffenimauftragArtikel.setBigDecimal(oAuftragpositionDto
							.getNOffeneMenge());
				if (oAuftragpositionDto.getNMenge() != null
						&& oAuftragpositionDto.getNOffeneMenge() != null)
					wnfGeliefertArtikel.setBigDecimal(oAuftragpositionDto
							.getNMenge().subtract(
									oAuftragpositionDto.getNOffeneMenge()));

			} else {
				// es gibt noch keine Position, eine neue erfassen
				RechnungPositionDto oRechnungpositionDto = new RechnungPositionDto();
				oRechnungpositionDto
						.setRechnungpositionartCNr(oAuftragpositionDto
								.getPositionsartCNr());
				oRechnungpositionDto.setCBez(oAuftragpositionDto.getCBez());
				oRechnungpositionDto.setXTextinhalt(oAuftragpositionDto
						.getXTextinhalt());
				oRechnungpositionDto.setCZusatzbez(oAuftragpositionDto
						.getCZusatzbez());
				oRechnungpositionDto.setEinheitCNr(oAuftragpositionDto
						.getEinheitCNr());
				oRechnungpositionDto.setRechnungIId(tpRechnung.getRechnungDto()
						.getIId());
				oRechnungpositionDto.setAuftragpositionIId(oAuftragpositionDto
						.getIId()); // zu
				// beginn
				// ist
				// die
				// gesamte
				// menge
				// offen
				oRechnungpositionDto.setTypCNr(oAuftragpositionDto.getTypCNr());
				oRechnungpositionDto
						.setBNettopreisuebersteuert(oAuftragpositionDto
								.getBNettopreisuebersteuert());

				if (oAuftragpositionDto.isIntelligenteZwischensumme()) {
					// SPJ 1779 - Mwstsatzfelder belegen
					oRechnungpositionDto
							.setBMwstsatzuebersteuert(oAuftragpositionDto
									.getBMwstsatzuebersteuert());
					oRechnungpositionDto.setMwstsatzIId(oAuftragpositionDto
							.getMwstsatzIId());
					oRechnungpositionDto.setFRabattsatz(oAuftragpositionDto
							.getFRabattsatz());

					// SPJ 1779 - Mwstsatzfelder belegen
					oRechnungpositionDto
							.setBMwstsatzuebersteuert(oAuftragpositionDto
									.getBMwstsatzuebersteuert());
					oRechnungpositionDto.setMwstsatzIId(oAuftragpositionDto
							.getMwstsatzIId());

					Integer vonPosNr = DelegateFactory
							.getInstance()
							.getAuftragpositionDelegate()
							.getPositionNummer(
									oAuftragpositionDto.getZwsVonPosition());
					Integer bisPosNr = DelegateFactory
							.getInstance()
							.getAuftragpositionDelegate()
							.getPositionNummer(
									oAuftragpositionDto.getZwsBisPosition());
					Integer vonPosIId = null;
					Integer bisPosIId = null;
					if (vonPosNr != null && bisPosNr != null) {
						Integer lsId = tpRechnung.getRechnungDto().getIId();
						vonPosIId = DelegateFactory
								.getInstance()
								.getRechnungDelegate()
								.getPositionIIdFromPositionNummer(lsId,
										vonPosNr);
						bisPosIId = DelegateFactory
								.getInstance()
								.getRechnungDelegate()
								.getPositionIIdFromPositionNummer(lsId,
										bisPosNr);

						oRechnungpositionDto.setZwsVonPosition(vonPosIId);
						oRechnungpositionDto.setZwsBisPosition(bisPosIId);
					}

					String msg = LPMain.getMessageTextRespectUISPr(
							"lp.hint.zwsauftraglieferschein",
							oAuftragpositionDto.getCBez(), vonPosNr, bisPosNr);
					DialogFactory.showModalDialog(
							LPMain.getTextRespectUISPr("lp.warning"), msg);
				}

				DelegateFactory
						.getInstance()
						.getRechnungDelegate()
						.createRechnungposition(oRechnungpositionDto,
								tpRechnung.getRechnungDto().getLagerIId());

				super.eventActionSave(e, bNeedNoSaveI);
			}

		}
	}

	private void saveHandeingabePosition(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {
		// wenn es zu dieser Auftragposition bereits eine Rechnungsposition
		// gibt,
		// wird die bestehenden Position aktualisiert
		RechnungPositionDto oRechnungspositionDto = null;

		oRechnungspositionDto = DelegateFactory
				.getInstance()
				.getRechnungDelegate()
				.getRechnungPositionByRechnungAuftragposition(
						tpRechnung.getRechnungDto().getIId(),
						oAuftragpositionDto.getIId());

		if (oRechnungspositionDto == null) {
			// es gibt noch keine Position, eine neue erfassen
			oRechnungspositionDto = new RechnungPositionDto();
			oRechnungspositionDto.setRechnungpositionartCNr(oAuftragpositionDto
					.getPositionsartCNr());
			oRechnungspositionDto.setArtikelIId(oAuftragpositionDto
					.getArtikelIId());
			oRechnungspositionDto
					.setSeriennrChargennrMitMenge(((PanelPositionenArtikelVerkaufSNR) panelArtikel).wtfSerienchargennummer
							.getSeriennummern());
			oRechnungspositionDto
					.setBArtikelbezeichnunguebersteuert(oAuftragpositionDto
							.getBArtikelbezeichnunguebersteuert());
			oRechnungspositionDto.setBMwstsatzuebersteuert(oAuftragpositionDto
					.getBMwstsatzuebersteuert());
			oRechnungspositionDto
					.setBRabattsatzuebersteuert(oAuftragpositionDto
							.getBRabattsatzuebersteuert());
			oRechnungspositionDto.setCLvposition(oAuftragpositionDto
					.getCLvposition());
			oRechnungspositionDto.setCBez(oAuftragpositionDto.getCBez());
			oRechnungspositionDto.setEinheitCNr(oAuftragpositionDto
					.getEinheitCNr());
			oRechnungspositionDto.setFRabattsatz(oAuftragpositionDto
					.getFRabattsatz());
			oRechnungspositionDto.setFZusatzrabattsatz(oAuftragpositionDto
					.getFZusatzrabattsatz());
			oRechnungspositionDto.setISort(oAuftragpositionDto.getISort());
			oRechnungspositionDto.setRechnungIId(tpRechnung.getRechnungDto()
					.getIId());
			oRechnungspositionDto.setMwstsatzIId(oAuftragpositionDto
					.getMwstsatzIId());
			oRechnungspositionDto
					.setNBruttoeinzelpreis(panelHandeingabe.wnfBruttopreis
							.getBigDecimal());
			oRechnungspositionDto.setNMenge(panelHandeingabe.wnfMenge
					.getBigDecimal());
			// oRechnungspositionDto.setNMwstbetrag(panelHandeingabe.wnfMwstsumme
			// .getBigDecimal());
			oRechnungspositionDto
					.setNEinzelpreis(panelHandeingabe.wnfEinzelpreis
							.getBigDecimal());
			oRechnungspositionDto
					.setNNettoeinzelpreis(panelHandeingabe.wnfNettopreis
							.getBigDecimal());
			oRechnungspositionDto.setAuftragpositionIId(oAuftragpositionDto
					.getIId()); // zu beginn ist die gesamte menge offen
			// oRechnungspositionDto.setNRabattbetrag(panelHandeingabe.
			// wnfRabattsumme.getBigDecimal());
			oRechnungspositionDto
					.setBNettopreisuebersteuert(oAuftragpositionDto
							.getBNettopreisuebersteuert());
			DelegateFactory
					.getInstance()
					.getRechnungDelegate()
					.updateRechnungPosition(oRechnungspositionDto,
							tpRechnung.getRechnungDto().getLagerIId());
		} else {
			// die bestehende Position mit der neuen Menge aktualisieren
			oRechnungspositionDto.setNMenge(panelHandeingabe.wnfMenge
					.getBigDecimal());

			DelegateFactory
					.getInstance()
					.getRechnungDelegate()
					.updateRechnungPosition(oRechnungspositionDto,
							tpRechnung.getRechnungDto().getLagerIId());
		}

		// die werte der auftragposition aktualisieren
		oAuftragpositionDto = DelegateFactory.getInstance()
				.getAuftragpositionDelegate()
				.auftragpositionFindByPrimaryKey(oAuftragpositionDto.getIId());

		// buttons schalten
		super.eventActionSave(e, bNeedNoSaveI);
	}

	/**
	 * Bevor eine Identposition in der Rechnung gespeichert werden kann, muessen
	 * verschiedene Vorbedingungen geprueft werden. <br>
	 * Der Komplexe Fall ist das Speichern einer Identposition mit einerm
	 * serien- oder chargennummerbehafteten Artikel.
	 * 
	 * @param e
	 *            Ereignis
	 * @param bNeedNoSaveI
	 *            flag
	 * @throws Throwable
	 *             Ausnahme
	 */
	protected void saveIdentPositionPositiveMenge(ActionEvent e,
			boolean bNeedNoSaveI) throws Throwable {
		boolean bDiePositionSpeichern = true;

		// eine ev. zusaetzlich gewaehlte Menge darf nicht groesser als die
		// offene Menge
		// der Auftragposition sein
		BigDecimal nBenoetigteMengeVomLager = new BigDecimal(0);
		BigDecimal nMengeInRechnungsposition = new BigDecimal(0);

		if (rechnungpositionAusAuftragpositionDto != null) {
			nMengeInRechnungsposition = rechnungpositionAusAuftragpositionDto
					.getNMenge();
			nBenoetigteMengeVomLager = panelArtikel.wnfMenge.getBigDecimal()
					.subtract(nMengeInRechnungsposition);
		} else {
			nBenoetigteMengeVomLager = panelArtikel.wnfMenge.getBigDecimal();
		}

		if (nBenoetigteMengeVomLager.compareTo(oAuftragpositionDto
				.getNOffeneMenge()) == 1) {

			boolean b = DialogFactory
					.showModalJaNeinDialog(
							getInternalFrame(),
							LPMain.getTextRespectUISPr("ls.warning.geawehltemengegroesseroffenemenge.ueberliefern"));

			if (b == true) {
				bDiePositionSpeichern = true;
			} else {
				panelArtikel.wnfMenge.setBigDecimal(nMengeInRechnungsposition
						.add(oAuftragpositionDto.getNOffeneMenge()));

				bDiePositionSpeichern = false;
			}
		} else if (Helper.short2boolean(panelArtikel.getArtikelDto()
				.getBChargennrtragend())
				&& (((PanelPositionenArtikelVerkaufSNR) panelArtikel).wtfSerienchargennummer
						.getText() == null || ((PanelPositionenArtikelVerkaufSNR) panelArtikel).wtfSerienchargennummer
						.getText().length() == 0)) {

			MessageFormat mf = new MessageFormat(
					LPMain.getTextRespectUISPr("lp.hint.chargennummer"));

			mf.setLocale(LPMain.getTheClient().getLocUi());

			Object pattern[] = { panelArtikel.wnfMenge.getBigDecimal() };

			DialogFactory.showModalDialog(
					LPMain.getTextRespectUISPr("lp.warning"),
					mf.format(pattern));

			bDiePositionSpeichern = false;
		} else

		// die Anzahl der eingegebenen Seriennummern pruefen
		if (Helper.short2boolean(panelArtikel.getArtikelDto()
				.getBSeriennrtragend())) {

			int iAnzahl = 0;

			if (((PanelPositionenArtikelVerkaufSNR) panelArtikel).wtfSerienchargennummer
					.getText() != null
					&& ((PanelPositionenArtikelVerkaufSNR) panelArtikel).wtfSerienchargennummer
							.getText().length() != 0) {
				iAnzahl = Helper
						.erzeugeStringArrayAusString(((PanelPositionenArtikelVerkaufSNR) panelArtikel).wtfSerienchargennummer
								.getText()).length;
			}

			if (iAnzahl != panelArtikel.wnfMenge.getDouble().intValue()) {
				MessageFormat mf = new MessageFormat(
						LPMain.getTextRespectUISPr("lp.hint.seriennummer"));

				mf.setLocale(LPMain.getTheClient().getLocUi());

				Object pattern[] = { panelArtikel.wnfMenge.getBigDecimal(),
						new Integer(iAnzahl) };

				DialogFactory.showModalDialog(
						LPMain.getTextRespectUISPr("lp.warning"),
						mf.format(pattern));

				bDiePositionSpeichern = false;
			}
		}

		if (bDiePositionSpeichern) {
			if (tpRechnung
					.istMengeAufLager(
							oAuftragpositionDto.getArtikelIId(),
							tpRechnung.getRechnungDto().getLagerIId(),
							((PanelPositionenArtikelVerkaufSNR) panelArtikel).wtfSerienchargennummer
									.getSeriennummern(),
							nBenoetigteMengeVomLager)) {

				// wenn es zu dieser Auftragposition bereits eine
				// Rechnungsposition gibt,
				// wird die bestehenden Position aktualisiert
				RechnungPositionDto oRechnungspositionDto = null;

				oRechnungspositionDto = DelegateFactory
						.getInstance()
						.getRechnungDelegate()
						.getRechnungPositionByRechnungAuftragposition(
								tpRechnung.getRechnungDto().getIId(),
								oAuftragpositionDto.getIId());

				if (oRechnungspositionDto == null) {
					// es gibt noch keine Position, eine neue erfassen
					oRechnungspositionDto = new RechnungPositionDto();
					oRechnungspositionDto
							.setRechnungpositionartCNr(oAuftragpositionDto
									.getPositionsartCNr());
					oRechnungspositionDto.setArtikelIId(oAuftragpositionDto
							.getArtikelIId());
					oRechnungspositionDto
							.setSeriennrChargennrMitMenge(((PanelPositionenArtikelVerkaufSNR) panelArtikel).wtfSerienchargennummer
									.getSeriennummern());
					oRechnungspositionDto
							.setBArtikelbezeichnunguebersteuert(oAuftragpositionDto
									.getBArtikelbezeichnunguebersteuert());
					oRechnungspositionDto
							.setBMwstsatzuebersteuert(oAuftragpositionDto
									.getBMwstsatzuebersteuert());
					oRechnungspositionDto
							.setBRabattsatzuebersteuert(oAuftragpositionDto
									.getBRabattsatzuebersteuert());
					oRechnungspositionDto
							.setCBez(oAuftragpositionDto.getCBez());
					oRechnungspositionDto.setCZusatzbez(oAuftragpositionDto
							.getCZusatzbez());
					oRechnungspositionDto.setEinheitCNr(oAuftragpositionDto
							.getEinheitCNr());
					oRechnungspositionDto.setFRabattsatz(oAuftragpositionDto
							.getFRabattsatz());
					oRechnungspositionDto
							.setFZusatzrabattsatz(oAuftragpositionDto
									.getFZusatzrabattsatz());
					oRechnungspositionDto
							.setKostentraegerIId(oAuftragpositionDto
									.getKostentraegerIId());
					oRechnungspositionDto
							.setNMaterialzuschlagKurs(oAuftragpositionDto
									.getNMaterialzuschlagKurs());
					oRechnungspositionDto
							.setTMaterialzuschlagDatum(oAuftragpositionDto
									.getTMaterialzuschlagDatum());
					oRechnungspositionDto.setRechnungIId(tpRechnung
							.getRechnungDto().getIId());
					oRechnungspositionDto.setMwstsatzIId(oAuftragpositionDto
							.getMwstsatzIId());
					oRechnungspositionDto
							.setNBruttoeinzelpreis(panelArtikel.wnfBruttopreis
									.getBigDecimal());
					oRechnungspositionDto.setNMenge(panelArtikel.wnfMenge
							.getBigDecimal());
					// oRechnungspositionDto.setNMwstbetrag(panelArtikel.
					// wnfMwstsumme.getBigDecimal());
					oRechnungspositionDto
							.setNEinzelpreis(panelArtikel.wnfEinzelpreis
									.getBigDecimal());
					oRechnungspositionDto
							.setNNettoeinzelpreis(panelArtikel.wnfNettopreis
									.getBigDecimal());
					if (panelArtikel.wnfMaterialzuschlag != null) {
						oRechnungspositionDto
								.setNMaterialzuschlag(panelArtikel.wnfMaterialzuschlag
										.getBigDecimal());
					}
					oRechnungspositionDto
							.setAuftragpositionIId(oAuftragpositionDto.getIId()); // zu
					// beginn
					// ist
					// die
					// gesamte
					// menge
					// offen
					// oRechnungspositionDto.setNRabattbetrag(panelArtikel.
					// wnfRabattsumme.getBigDecimal());
					oRechnungspositionDto
							.setBNettopreisuebersteuert(oAuftragpositionDto
									.getBNettopreisuebersteuert());

					List<SeriennrChargennrMitMengeDto> snrs = getArtikelsetViewController()
							.handleArtikelsetSeriennummern(
									tpRechnung.getRechnungDto().getLagerIId(),
									oRechnungspositionDto);

					if (!getArtikelsetViewController()
							.isArtikelsetWithSnrsStoreable(
									oRechnungspositionDto, snrs)) {
						bDiePositionSpeichern = false;
					}

					if (bDiePositionSpeichern) {
						DelegateFactory
								.getInstance()
								.getRechnungDelegate()
								.updateRechnungPosition(
										oRechnungspositionDto,
										tpRechnung.getRechnungDto()
												.getLagerIId(), snrs);
					}
				}

				// es gibt in dieser Rechnung bereits eine Rechnungposition zu
				// dieser Auftragposition
				else {
					// zusaetzlich wird die eingegebene Menge abgebucht -> das
					// Handling
					// in Bezug auf die bestehende Position geschieht am Server
					oRechnungspositionDto.setNMenge(panelArtikel.wnfMenge
							.getBigDecimal());
					oRechnungspositionDto
							.setSeriennrChargennrMitMenge(((PanelPositionenArtikelVerkaufSNR) panelArtikel).wtfSerienchargennummer
									.getSeriennummern());

					List<SeriennrChargennrMitMengeDto> bekannteSnrs = DelegateFactory
							.getInstance()
							.getRechnungDelegate()
							.getSeriennrchargennrForArtikelsetPosition(
									oRechnungspositionDto.getIId());

					List<SeriennrChargennrMitMengeDto> snrs = getArtikelsetViewController()
							.handleArtikelsetSeriennummern(
									tpRechnung.getRechnungDto().getLagerIId(),
									oRechnungspositionDto, bekannteSnrs);

					if (!getArtikelsetViewController()
							.isArtikelsetWithSnrsStoreable(
									oRechnungspositionDto, snrs)) {
						bDiePositionSpeichern = false;
					}

					if (bDiePositionSpeichern) {
						DelegateFactory
								.getInstance()
								.getRechnungDelegate()
								.updateRechnungPosition(
										oRechnungspositionDto,
										tpRechnung.getRechnungDto()
												.getLagerIId(), snrs);
					}
				}

				// buttons schalten
				super.eventActionSave(e, bNeedNoSaveI);
			} else {
				panelArtikel.wnfMenge
						.setBigDecimal(DelegateFactory
								.getInstance()
								.getLagerDelegate()
								.getMengeAufLager(
										oAuftragpositionDto.getArtikelIId(),
										tpRechnung.getRechnungDto()
												.getLagerIId(), null));
			}
		}
	}

	/**
	 * Bevor eine Identposition in der Rechnung gespeichert werden kann, muessen
	 * verschiedene Vorbedingungen geprueft werden. <br>
	 * Der Komplexe Fall ist das Speichern einer Identposition mit einerm
	 * serien- oder chargennummerbehafteten Artikel.
	 * 
	 * @param e
	 *            Ereignis
	 * @param bNeedNoSaveI
	 *            flag
	 * @throws Throwable
	 *             Ausnahme
	 */
	protected void saveIdentPositionNegativeMenge(ActionEvent e,
			boolean bNeedNoSaveI) throws Throwable {
		boolean bDiePositionSpeichern = true;

		// IMS 2129
		// eine ev. zusaetzlich gewaehlte Menge darf nicht groesser als die
		// offene Menge
		// der Auftragposition sein
		BigDecimal nBenoetigteMengeVomLager = new BigDecimal(0);
		BigDecimal nMengeInRechnungsposition = new BigDecimal(0);

		if (rechnungpositionAusAuftragpositionDto != null) {
			nMengeInRechnungsposition = rechnungpositionAusAuftragpositionDto
					.getNMenge();
			nBenoetigteMengeVomLager = panelArtikel.wnfMenge.getBigDecimal()
					.subtract(nMengeInRechnungsposition);
		} else {
			nBenoetigteMengeVomLager = panelArtikel.wnfMenge.getBigDecimal();
		}

		if (nBenoetigteMengeVomLager.compareTo(oAuftragpositionDto
				.getNOffeneMenge()) == -1) {
			DialogFactory
					.showModalDialog(
							LPMain.getTextRespectUISPr("lp.warning"),
							LPMain.getTextRespectUISPr("ls.warning.geawehltemengegroesseroffenemenge"));

			panelArtikel.wnfMenge.setBigDecimal(nMengeInRechnungsposition
					.add(oAuftragpositionDto.getNOffeneMenge()));

			bDiePositionSpeichern = false;
		} else if (Helper.short2boolean(panelArtikel.getArtikelDto()
				.getBChargennrtragend())
				&& (((PanelPositionenArtikelVerkaufSNR) panelArtikel).wtfSerienchargennummer
						.getText() == null || ((PanelPositionenArtikelVerkaufSNR) panelArtikel).wtfSerienchargennummer
						.getText().length() == 0)) {

			MessageFormat mf = new MessageFormat(
					LPMain.getTextRespectUISPr("lp.hint.chargennummer"));

			mf.setLocale(LPMain.getTheClient().getLocUi());

			Object pattern[] = { panelArtikel.wnfMenge.getBigDecimal() };

			DialogFactory.showModalDialog(
					LPMain.getTextRespectUISPr("lp.warning"),
					mf.format(pattern));

			bDiePositionSpeichern = false;
		} else

		// die Anzahl der eingegebenen Seriennummern pruefen
		if (Helper.short2boolean(panelArtikel.getArtikelDto()
				.getBSeriennrtragend())) {

			int iAnzahl = 0;

			if (((PanelPositionenArtikelVerkaufSNR) panelArtikel).wtfSerienchargennummer
					.getText() != null
					&& ((PanelPositionenArtikelVerkaufSNR) panelArtikel).wtfSerienchargennummer
							.getText().length() != 0) {
				iAnzahl = Helper
						.erzeugeStringArrayAusString(((PanelPositionenArtikelVerkaufSNR) panelArtikel).wtfSerienchargennummer
								.getText()).length;
			}

			if (iAnzahl != panelArtikel.wnfMenge.getDouble().intValue()) {
				MessageFormat mf = new MessageFormat(
						LPMain.getTextRespectUISPr("lp.hint.seriennummer"));
				mf.setLocale(LPMain.getTheClient().getLocUi());

				Object pattern[] = { panelArtikel.wnfMenge.getBigDecimal(),
						new Integer(iAnzahl) };

				DialogFactory.showModalDialog(
						LPMain.getTextRespectUISPr("lp.warning"),
						mf.format(pattern));

				bDiePositionSpeichern = false;
			}
		}

		if (bDiePositionSpeichern) {
			// IMS 2129
			// wenn es zu dieser Auftragposition bereits eine Rechnungsposition
			// gibt,
			// wird die bestehenden Position aktualisiert
			RechnungPositionDto oRechnungspositionDto = null;

			oRechnungspositionDto = DelegateFactory
					.getInstance()
					.getRechnungDelegate()
					.getRechnungPositionByRechnungAuftragposition(
							tpRechnung.getRechnungDto().getIId(),
							oAuftragpositionDto.getIId());

			if (oRechnungspositionDto == null) {
				// es gibt noch keine Position, eine neue erfassen
				oRechnungspositionDto = new RechnungPositionDto();
				oRechnungspositionDto
						.setRechnungpositionartCNr(oAuftragpositionDto
								.getPositionsartCNr());
				oRechnungspositionDto.setArtikelIId(oAuftragpositionDto
						.getArtikelIId());
				oRechnungspositionDto
						.setSeriennrChargennrMitMenge(((PanelPositionenArtikelVerkaufSNR) panelArtikel).wtfSerienchargennummer
								.getSeriennummern());
				oRechnungspositionDto.setCLvposition(oAuftragpositionDto
						.getCLvposition());
				oRechnungspositionDto
						.setBArtikelbezeichnunguebersteuert(oAuftragpositionDto
								.getBArtikelbezeichnunguebersteuert());
				oRechnungspositionDto
						.setBMwstsatzuebersteuert(oAuftragpositionDto
								.getBMwstsatzuebersteuert());
				oRechnungspositionDto
						.setBRabattsatzuebersteuert(oAuftragpositionDto
								.getBRabattsatzuebersteuert());
				oRechnungspositionDto.setCBez(oAuftragpositionDto.getCBez());
				oRechnungspositionDto.setCZusatzbez(oAuftragpositionDto
						.getCZusatzbez());
				oRechnungspositionDto.setEinheitCNr(oAuftragpositionDto
						.getEinheitCNr());
				oRechnungspositionDto.setFRabattsatz(oAuftragpositionDto
						.getFRabattsatz());
				oRechnungspositionDto.setFZusatzrabattsatz(oAuftragpositionDto
						.getFZusatzrabattsatz());
				oRechnungspositionDto.setKostentraegerIId(oAuftragpositionDto
						.getKostentraegerIId());
				oRechnungspositionDto
						.setNMaterialzuschlagKurs(oAuftragpositionDto
								.getNMaterialzuschlagKurs());
				oRechnungspositionDto
						.setTMaterialzuschlagDatum(oAuftragpositionDto
								.getTMaterialzuschlagDatum());
				oRechnungspositionDto.setRechnungIId(tpRechnung
						.getRechnungDto().getIId());
				oRechnungspositionDto.setMwstsatzIId(oAuftragpositionDto
						.getMwstsatzIId());
				oRechnungspositionDto
						.setNBruttoeinzelpreis(panelArtikel.wnfBruttopreis
								.getBigDecimal());
				if (panelArtikel.wnfMaterialzuschlag != null) {
					oRechnungspositionDto
							.setNMaterialzuschlag(panelArtikel.wnfMaterialzuschlag
									.getBigDecimal());
				}
				oRechnungspositionDto.setNMenge(panelArtikel.wnfMenge
						.getBigDecimal());
				// oRechnungspositionDto.setNMwstbetrag(panelArtikel.wnfMwstsumme
				// .getBigDecimal());
				oRechnungspositionDto
						.setNEinzelpreis(panelArtikel.wnfEinzelpreis
								.getBigDecimal());
				oRechnungspositionDto
						.setNNettoeinzelpreis(panelArtikel.wnfNettopreis
								.getBigDecimal());
				oRechnungspositionDto.setAuftragpositionIId(oAuftragpositionDto
						.getIId()); // zu beginn ist die gesamte menge offen
				// oRechnungspositionDto.setNRabattbetrag(panelArtikel.
				// wnfRabattsumme.getBigDecimal());
				oRechnungspositionDto
						.setBNettopreisuebersteuert(oAuftragpositionDto
								.getBNettopreisuebersteuert());
				oRechnungspositionDto.setVerleihIId(oAuftragpositionDto
						.getVerleihIId());
				DelegateFactory
						.getInstance()
						.getRechnungDelegate()
						.updateRechnungPosition(oRechnungspositionDto,
								tpRechnung.getRechnungDto().getLagerIId());
			}

			// es gibt in dieser Rechnung bereits eine Rechnungsposition zu
			// dieser Auftragposition
			else {
				// zusaetzlich wird die eingegebene Menge abgebucht -> das
				// Handling
				// in Bezug auf die bestehende Position geschieht am Server
				oRechnungspositionDto.setNMenge(panelArtikel.wnfMenge
						.getBigDecimal());
				oRechnungspositionDto
						.setSeriennrChargennrMitMenge(((PanelPositionenArtikelVerkaufSNR) panelArtikel).wtfSerienchargennummer
								.getSeriennummern());

				DelegateFactory
						.getInstance()
						.getRechnungDelegate()
						.updateRechnungPosition(oRechnungspositionDto,
								tpRechnung.getRechnungDto().getLagerIId());
			}

			// buttons schalten
			super.eventActionSave(e, bNeedNoSaveI);
			// IMS 2129
			/*
			 * } else {
			 * panelArtikel.wnfMenge.setDouble(DelegateFactory.getInstance
			 * ().getLagerDelegate().getMengeAufLager(
			 * oAuftragpositionDto.getArtikelIId(),
			 * tpRechnung.getRechnungDto().getLagerIId(), null)); }
			 */
		}
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {
		super.eventYouAreSelected(false); // buttons schalten

		setLagerIId(tpRechnung.getRechnungDto().getLagerIId());

		setTBelegdatumMwstsatz(tpRechnung.getRechnungDto().getTBelegdatum());
		// auch der zugehoerige Auftrag muss gelockt werden
		if (oAuftragpositionDto != null) {
			lockMeAuftrag = new LockMeDto(HelperClient.LOCKME_AUFTRAG,
					oAuftragpositionDto.getBelegIId() + "", LPMain
							.getInstance().getCNrUser());
		}

		// wenn die Rechnung gerade gelockt ist, die Eingabefelder freischalten
		if (getLockedstateDetailMainKey().getIState() == PanelBasis.LOCK_IS_LOCKED_BY_ME) {
			panelArtikel.wnfMenge.setBigDecimal(null);
			panelArtikel.wnfMenge.setEditable(true);

			zeigeSerienchargennummer(true, true);
		} else {
			zeigeSerienchargennummer(false, false);
		}

	}

	/**
	 * Bei jeder Anzeige eines Artikels muss darueber entschieden werden, ob die
	 * Controls fuer die Serien- bzw. Chargennummereingabe angezeigt werden.
	 * 
	 * @param bEditableI
	 *            true, wenn eine Eingabe moeglich sein soll
	 * @param bSerienchargennrLeer
	 *            true, wenn die Eingabe zurueckgesetzt werden soll
	 * @throws java.lang.Throwable
	 *             Ausnahme
	 */
	protected void zeigeSerienchargennummer(boolean bEditableI,
			boolean bSerienchargennrLeer) throws Throwable {
		((PanelPositionenArtikelVerkaufSNR) panelArtikel)
				.zeigeSerienchargennummer(bEditableI, bSerienchargennrLeer);
		aktualisiereStatusbar();
	}

	private void aktualisiereStatusbar() throws Throwable {
		setStatusbarPersonalIIdAnlegen(tpRechnung.getRechnungDto()
				.getPersonalIIdAnlegen());
		setStatusbarTAnlegen(tpRechnung.getRechnungDto().getTAnlegen());
		setStatusbarPersonalIIdAendern(tpRechnung.getRechnungDto()
				.getPersonalIIdAendern());
		setStatusbarTAendern(tpRechnung.getRechnungDto().getTAendern());
		setStatusbarStatusCNr(tpRechnung.getRechnungDto().getStatusCNr());

		setStatusbarSpalte5(this.getLagerstandFuerStatusbarSpalte5(tpRechnung
				.getRechnungDto().getLagerIId()));
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

		if (panelArtikel.getArtikelDto() != null
				&& panelArtikel.getArtikelDto().getIId() != null) {
			String serienchargennummer = null;

			if (Helper.short2boolean(panelArtikel.getArtikelDto()
					.getBSeriennrtragend())
					&& ((PanelPositionenArtikelVerkaufSNR) panelArtikel).wtfSerienchargennummer
							.getText() != null
					&& ((PanelPositionenArtikelVerkaufSNR) panelArtikel).wtfSerienchargennummer
							.getText().length() > 0) {
				serienchargennummer = ((PanelPositionenArtikelVerkaufSNR) panelArtikel).wtfSerienchargennummer
						.getText();
			} else if (Helper.short2boolean(panelArtikel.getArtikelDto()
					.getBChargennrtragend())
					&& ((PanelPositionenArtikelVerkaufSNR) panelArtikel).wtfSerienchargennummer
							.getText() != null
					&& ((PanelPositionenArtikelVerkaufSNR) panelArtikel).wtfSerienchargennummer
							.getText().length() > 0) {
				serienchargennummer = ((PanelPositionenArtikelVerkaufSNR) panelArtikel).wtfSerienchargennummer
						.getText();
			}

			if ((!Helper.short2boolean(panelArtikel.getArtikelDto()
					.getBSeriennrtragend()) && !Helper
					.short2boolean(panelArtikel.getArtikelDto()
							.getBChargennrtragend()))
					|| serienchargennummer != null) {
				if (panelArtikel.getArtikelDto() != null
						&& panelArtikel.getArtikelDto().getIId() != null) {
					BigDecimal ddMenge = DelegateFactory
							.getInstance()
							.getLagerDelegate()
							.getMengeAufLager(
									panelArtikel.getArtikelDto().getIId(),
									iIdLager, serienchargennummer);

					sLagerinfoO += ": ";
					sLagerinfoO += ddMenge;
				}
			}
		}
		return sLagerinfoO;
	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_RECHNUNG;
	}

	protected void eventActionLock(ActionEvent e) throws Throwable {

		// Rechnung locken.
		super.eventActionLock(e);

		if (lockMeAuftrag != null) {
			// Zugehoerigen Auftrag locken.
			super.lock(lockMeAuftrag);
		}
	}

	protected void eventActionUnlock(ActionEvent e) throws Throwable {

		// Rechnung unlocken.
		super.eventActionUnlock(e);

		if (lockMeAuftrag != null) {
			// Zugehoerigen Auftrag unlocken.
			super.unlock(lockMeAuftrag);
		}
	}

	public LockStateValue getLockedstateDetailMainKey() throws Throwable {

		LockStateValue lockstateValue = super.getLockedstateDetailMainKey();
		if (lockstateValue.getIState() == LOCK_IS_NOT_LOCKED
				&& lockMeAuftrag != null) {
			int iLockstate = getLockedByWerWas(lockMeAuftrag);

			if (iLockstate == LOCK_IS_LOCKED_BY_ME) {
				iLockstate = LOCK_IS_LOCKED_BY_OTHER_USER; // anderes Modul hat
				// gelock zB Partner
			}

			lockstateValue.setIState(iLockstate);
		}

		return lockstateValue;
	}

	protected void eventActionDiscard(ActionEvent e) throws Throwable {
		super.eventActionDiscard(e);

		// auch der zugehoerige Auftrag muss gelockt werden
		if (oAuftragpositionDto != null) {
			lockMeAuftrag = new LockMeDto(HelperClient.LOCKME_AUFTRAG,
					oAuftragpositionDto.getBelegIId() + "", LPMain
							.getInstance().getCNrUser());
		}
	}

	public void eventActionNew(EventObject eventObject, boolean bLockMeI,
			boolean bNeedNoNewI) throws Throwable {
		if (tpRechnung.istAktualisierenRechnungErlaubt()) {

			super.eventActionNew(eventObject, true, false);

			setDefaults();

			// es kann ein normales oder das spezielle neu sein
			if (((ItemChangedEvent) eventObject).getID() == ItemChangedEvent.ACTION_MY_OWN_NEW) {

				// alle Positionen des Auftrags in die Rechnung uebernehmen
				if (tpRechnung.getRechnungDto().getAuftragIId() != null) {
					uebernimmAlleOffenenAuftragpositionenOhneBenutzerinteraktion();
				}
			}
		} else {
			tpRechnung.getPanelQueryPositionen(true).updateButtons(
					tpRechnung.getPanelDetailPositionen(true)
							.getLockedstateDetailMainKey());
		}
	}

	/**
	 * ACHTUNG: Das ist eine ganz b&ouml;se Routine. Sie castet - mit dem Wissen
	 * dass es sich nur um Auftragpositionen handeln kann(!) - von
	 * Belegpositionen auf Auftragpositionen.
	 * 
	 * @param artikelset
	 * @return AuftragpositionDto[]
	 */
	private AuftragpositionDto[] getAuftragpositionDtos(Artikelset artikelset) {
		BelegpositionVerkaufDto[] allpositions = artikelset.getAllPositions();
		AuftragpositionDto[] dtos = new AuftragpositionDto[allpositions.length];
		for (int i = 0; i < allpositions.length; i++) {
			dtos[i] = (AuftragpositionDto) allpositions[i];
		}
		return dtos;
	}

	/**
	 * Alle offenen Auftragpositionen in die Rechnung kopieren, die ohne
	 * Benutzeraktion uebernommen werden koennen.
	 * <p>
	 * Es gilt:
	 * <ul>
	 * <li>Lagerbewirtschaftete Artikel werden nur mit der Menge uebernommen,
	 * die auf Lager liegt.
	 * <li>Artikel mit Chargennummer werden nur uebernommen, wenn es eine
	 * eindeutige Charge auf Lager gibt.
	 * <li>Artikel mit Seriennummer werden nicht uebernommen.
	 * </ul>
	 * 
	 * @throws Throwable
	 *             Ausnahme
	 */
	private void uebernimmAlleOffenenAuftragpositionenOhneBenutzerinteraktion()
			throws Throwable {
		try {
			if (tpRechnung.getPanelQueryPositionenSichtAuftrag(true)
					.getSelectedId() != null) {

				AuftragDto auftragDto = tpRechnung.getAuftragDtoSichtAuftrag();
				AuftragpositionDto[] positions = DelegateFactory.getInstance()
						.getAuftragpositionDelegate()
						.auftragpositionFindByAuftrag(auftragDto.getIId());

				List<Artikelset> artikelSets = DelegateFactory.getInstance()
						.getAuftragpositionDelegate()
						.getOffeneAuftragpositionDtoMitArtikelset(positions);

				if (artikelSets.size() > 0) {
					List<BelegpositionVerkaufDto> nichtErfuellbarePositionen = new ArrayList<BelegpositionVerkaufDto>();
					for (Artikelset artikelset : artikelSets) {
						BigDecimal erfuellbareMenge = DelegateFactory
								.getInstance()
								.getAuftragpositionDelegate()
								.getErfuellbareMengeArtikelset(
										getAuftragpositionDtos(artikelset),
										tpRechnung.getRechnungDto()
												.getLagerIId());
						artikelset.setAvailableAmount(erfuellbareMenge);

						if (erfuellbareMenge.compareTo(artikelset
								.getSlipAmount()) < 0) {
							nichtErfuellbarePositionen
									.add(artikelset.getHead());
						}
					}

					if (nichtErfuellbarePositionen.size() > 0) {
						boolean yes = DialogFactory
								.showModalJaNeinDialog(
										getInternalFrame(),
										"Es sind nicht alle Auftragpositionen mit Artikelsets \n"
												+ "komplett erf\u00FCllbar.\n"
												+ "Trotzdem mit der \u00DCbernahme der Positionen fortfahren?");
						if (!yes)
							return;
					}

					for (Artikelset artikelset : artikelSets) {
						if (getArtikelsetViewController()
								.needArtikelsetSeriennummern(
										tpRechnung.getRechnungDto()
												.getLagerIId(),
										artikelset.getAllPositions())) {
							ArtikelDto headDto = DelegateFactory
									.getInstance()
									.getArtikelDelegate()
									.artikelFindByPrimaryKey(
											artikelset.getHead()
													.getArtikelIId());

							boolean needsInput = true;
							do {
								List<SeriennrChargennrMitMengeDto> snrs = getArtikelsetViewController()
										.inputArtikelsetSeriennummern(
												"Seriennummernerfassung f\u00FCr ["
														+ headDto.getCNr()
														+ "]",
												tpRechnung.getRechnungDto()
														.getLagerIId(),
												artikelset);
								artikelset.setIdentities(snrs);

								if (snrs.size() > 0) {
									needsInput = false;
								} else {
									boolean yes = DialogFactory
											.showModalJaNeinDialog(
													getInternalFrame(),
													"Es wurden keine Seriennummern erfasst!\n"
															+ "Soll die \u00DCbernahme der Positionen abgebrochen werden?");
									if (yes)
										return;
								}
							} while (needsInput);
						}
					}
				}

				DelegateFactory
						.getInstance()
						.getRechnungDelegate()
						.uebernimmAlleOffenenAuftragpositionenOhneBenutzerinteraktionNew(
								tpRechnung.getRechnungDto().getIId(),
								tpRechnung.getAuftragDtoSichtAuftrag().getIId(),
								artikelSets);

				// das Panel neu laden, damit die uebernommenen Positionen
				// verschwinden
				tpRechnung.getPanelDetailPositionenSichtAuftrag(true)
						.eventYouAreSelected(false);
			}
		} finally {
			// den gelockten Auftrag explizit freigeben
			if (lockMeAuftrag != null) {
				super.unlock(lockMeAuftrag);
			}
			getInternalFrame().setKeyWasForLockMe(
					tpRechnung.getRechnungDto().getIId() + "");
		}
	}

	protected String getWaehrungCNrBeleg() {
		return tpRechnung.getRechnungDto().getWaehrungCNr();
	}
}
