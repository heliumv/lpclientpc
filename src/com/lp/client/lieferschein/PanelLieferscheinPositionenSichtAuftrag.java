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
package com.lp.client.lieferschein;

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
import com.lp.client.pc.LPButtonAction;
import com.lp.client.pc.LPMain;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikelsprDto;
import com.lp.server.artikel.service.SeriennrChargennrMitMengeDto;
import com.lp.server.auftrag.service.AuftragDto;
import com.lp.server.auftrag.service.AuftragServiceFac;
import com.lp.server.auftrag.service.AuftragpositionDto;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.lieferschein.service.LieferscheinFac;
import com.lp.server.lieferschein.service.LieferscheinpositionDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.LockMeDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.service.Artikelset;
import com.lp.service.BelegpositionVerkaufDto;
import com.lp.util.Helper;

@SuppressWarnings("static-access")
/*
 * <p>In diesem Detailfenster des Lieferscheins werden Positionen erfasst bzw.
 * geaendert.</p>
 * 
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p> <p>Erstellungsdatum
 * 2004-10-13</p>
 * 
 * @author Uli Walch
 * 
 * @version $Revision: 1.36 $
 */
public class PanelLieferscheinPositionenSichtAuftrag extends
		PanelPositionenSichtAuftragSNR {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** Cache for convenience. */
	private InternalFrameLieferschein intFrame = null;
	/** Cache for convenience. */
	protected TabbedPaneLieferschein tpLieferschein = null;

	private final static String ACTION_SPECIAL_ZUSAETZLICHE_CHARGEN = PanelBasis.ACTION_MY_OWN_NEW
			+ "action_special_mehrere_zubuchen";

	private final static String ACTION_SPECIAL_LOSABLIEFERMENGE_UEBERNEHMEN = PanelBasis.ACTION_MY_OWN_BUTTON_SIMILAR_TO_SAVE
			+ "action_losabliefermenge_uebernehmen";
	private final static String ACTION_SPECIAL_KEIN_LIEFERREST = PanelBasis.ACTION_MY_OWN_NEW
			+ "action_special_keinlieferrest";

	private LockMeDto lockMeAuftrag = null;

	private LieferscheinpositionDto lieferscheinpositionAusAuftragpositionDto = null;

	private boolean bZusaetzlicheChargenposition = false;

	private boolean bBezeichnungEinfrieren = false;

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
	 * @throws java.lang.Throwable
	 *             Ausnahme
	 */
	public PanelLieferscheinPositionenSichtAuftrag(InternalFrame internalFrame,
			String add2TitleI, Object key, Integer iIdLagerI) throws Throwable {
		super(internalFrame, add2TitleI, key, iIdLagerI);

		intFrame = (InternalFrameLieferschein) internalFrame;
		tpLieferschein = intFrame.getTabbedPaneLieferschein();

		ParametermandantDto parameter = (ParametermandantDto) DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getParametermandant(
						ParameterFac.PARAMETER_ARTIKELBEZEICHNUNG_IN_LS_EINFRIEREN,
						ParameterFac.KATEGORIE_LIEFERSCHEIN,
						LPMain.getTheClient().getMandant());

		if (parameter.getCWert() != null && !parameter.getCWert().equals("0")) {
			bBezeichnungEinfrieren = true;
		}

		jbInit();
		initComponents();
	}

	private void jbInit() throws Throwable {
		// Statusbar an den unteren Rand des Panels haengen
		iZeile++;
		add(getPanelStatusbar(), new GridBagConstraints(0, iZeile, 1, 1, 1.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));

		// einfache Preisauswahl ueber Dialog deaktivieren
		((PanelPositionenArtikelVerkauf) panelArtikel)
				.remove(((PanelPositionenArtikelVerkauf) panelArtikel).wbuPreisauswahl);

		((PanelPositionenArtikelVerkauf) panelArtikel).add(
				((PanelPositionenArtikelVerkauf) panelArtikel).wlaEinzelpreis,
				new GridBagConstraints(5, 2, 1, 1, 0.0, 0.0,
						GridBagConstraints.WEST, GridBagConstraints.BOTH,
						new Insets(10, 2, 2, 2), 0, 0));

		this.createAndSaveAndShowButton("/com/lp/client/res/data_next.png",
				"Zus\u00E4tzliche Chargennummer angeben",
				ACTION_SPECIAL_ZUSAETZLICHE_CHARGEN,
				RechteFac.RECHT_LS_LIEFERSCHEIN_CUD);

		this.createAndSaveAndShowButton("/com/lp/client/res/check2.png",
				LPMain.getTextRespectUISPr("ls.sichtauftrag.keinlieferrrest"),
				ACTION_SPECIAL_KEIN_LIEFERREST,
				RechteFac.RECHT_LS_LIEFERSCHEIN_CUD);

		if (LPMain.getInstance().getDesktop()
				.darfAnwenderAufModulZugreifen(LocaleFac.BELEGART_LOS)) {

			this.createAndSaveAndShowButton(
					"/com/lp/client/res/factory16x16.png",
					LPMain.getTextRespectUISPr("ls.sichtauftrag.letzteabliefermengeuebernehmen"),
					ACTION_SPECIAL_LOSABLIEFERMENGE_UEBERNEHMEN,
					RechteFac.RECHT_LS_LIEFERSCHEIN_CUD);
		}

		/*
		 * LPButtonAction but=
		 * getHmOfButtons().get(ACTION_SPECIAL_LOSABLIEFERMENGE_UEBERNEHMEN);
		 * but.getButton().setEnabled(false);
		 */
	}

	protected void setDefaults() throws Throwable {
		super.setDefaults();
		if (tpLieferschein.pruefeAktuellenLieferschein()) {
			// es gilt die waehrung des lieferscheins
			panelHandeingabe.setWaehrungCNr(tpLieferschein.getLieferscheinDto()
					.getWaehrungCNr());
			panelArtikel.setWaehrungCNr(tpLieferschein.getLieferscheinDto()
					.getWaehrungCNr());
		}
		// dem panelArtikel muss die momentan eingetragene Menge gesetzt werden
		// -> fuer Chargennummern
		((PanelPositionenArtikelVerkaufSNR) panelArtikel)
				.setDDBisherigeMenge(new Double(0));

		Integer lagerIId = tpLieferschein.getLieferscheinDto().getLagerIId();

		if (oAuftragpositionDto != null) {
			lieferscheinpositionAusAuftragpositionDto = DelegateFactory
					.getInstance()
					.getLsDelegate()
					.getLieferscheinpositionByLieferscheinAuftragposition(
							tpLieferschein.getLieferscheinDto().getIId(),
							oAuftragpositionDto.getIId());

			if (lieferscheinpositionAusAuftragpositionDto != null
					&& lieferscheinpositionAusAuftragpositionDto.getLagerIId() != null) {
				lagerIId = lieferscheinpositionAusAuftragpositionDto
						.getLagerIId();
			}
		}
		((PanelPositionenArtikelVerkaufSNR) panelArtikel).setIIdLager(lagerIId);
	}

	protected void eventActionUpdate(ActionEvent aE, boolean bNeedNoUpdateI)
			throws Throwable {
		if (tpLieferschein.istAktualisierenLieferscheinErlaubt()) {

			boolean bAendern = true;

			AuftragDto auftragDto = DelegateFactory.getInstance()
					.getAuftragDelegate()
					.auftragFindByPrimaryKey(oAuftragpositionDto.getBelegIId());

			if (auftragDto.getAuftragstatusCNr().equals(
					AuftragServiceFac.AUFTRAGSTATUS_ERLEDIGT)) {
				DialogFactory
						.showModalDialog(
								LPMain.getTextRespectUISPr("lp.hint"),
								LPMain.getTextRespectUISPr("ls.error.auftragbereitserledigt"));
				bAendern = false;
				return;
			}

			if (oAuftragpositionDto.getAuftragpositionstatusCNr().equals(
					AuftragServiceFac.AUFTRAGPOSITIONSTATUS_ERLEDIGT)) {
				bAendern = DialogFactory
						.showModalJaNeinDialog(
								getInternalFrame(),
								LPMain.getTextRespectUISPr("ls.warning.auftragspositionerledigt.trotzdemaendern"));
			}
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
					lieferscheinpositionAusAuftragpositionDto = DelegateFactory
							.getInstance()
							.getLsDelegate()
							.getLieferscheinpositionByLieferscheinAuftragposition(
									tpLieferschein.getLieferscheinDto()
											.getIId(),
									oAuftragpositionDto.getIId());

				}

				super.eventActionUpdate(aE, false); // die Buttons muessen auf
				// alle Faelle geschaltet
				// werden

				if (oAuftragpositionDto.getPositionsartCNr().equals(
						LocaleFac.POSITIONSART_IDENT)) {
					// IMS 1553 Wenn die Lieferscheinposition neu angelegt wird,
					// ist der
					// Vorschlagswert die offene Menge im Auftrag, sonst die
					// bereits erfasste
					// Menge im Lieferschein
					if (panelArtikel.getArtikelDto().getIId() != null
							&& (Helper.short2boolean(panelArtikel
									.getArtikelDto().getBChargennrtragend()) || Helper
									.short2boolean(panelArtikel.getArtikelDto()
											.getBSeriennrtragend()))) {
						panelArtikel.wnfMenge.setEditable(false);

					} else {
						panelArtikel.wnfMenge.setEditable(true);
					}

					if (lieferscheinpositionAusAuftragpositionDto == null) {

						if (bOffeneMengeVorschlagen) {
							befuelleMengeMitVorschlagswert();
						} else {
							panelArtikel.wnfMenge.setBigDecimal(null);
						}

					} else {
						panelArtikel.wnfMenge
								.setBigDecimal(lieferscheinpositionAusAuftragpositionDto
										.getNMenge());
						panelArtikel.wbuLager
								.setEnabled(false);
					}
				} else if (oAuftragpositionDto.getPositionsartCNr().equals(
						LocaleFac.POSITIONSART_HANDEINGABE)) {
					// IMS 1553 Wenn die Lieferscheinposition neu angelegt wird,
					// ist der
					// Vorschlagswert die offene Menge im Auftrag, sonst die
					// bereits erfasste
					// Menge im Lieferschein
					if (lieferscheinpositionAusAuftragpositionDto == null) {
						if (bOffeneMengeVorschlagen) {
							panelHandeingabe.wnfMenge
									.setBigDecimal(oAuftragpositionDto
											.getNOffeneMenge());
						} else {
							panelHandeingabe.wnfMenge.setBigDecimal(null);
						}
					} else {
						panelHandeingabe.wnfMenge
								.setBigDecimal(lieferscheinpositionAusAuftragpositionDto
										.getNMenge());
					}
				}

			}
		}
	}

	/**
	 * Anzeige saemtlicher Felder der Auftragposition. <br>
	 * Es kann nichts veraendert werden, ausser die Menge im Lieferschein. <br>
	 * Hier muessen die Anzeige der offenen Menge und der gelieferten Menge
	 * konistent gehalten werden.
	 * 
	 * @throws Throwable
	 *             Ausnahme
	 */
	protected void auftragpositionDto2Components() throws Throwable {
		if (oAuftragpositionDto.getNMenge() != null) {
			// fuer positive auftragsmengen nur positive mengen liefern

			// PJ SP479
			/*
			 * if (oAuftragpositionDto.getNMenge().doubleValue() > 0) {
			 * panelArtikel.wnfMenge.setMinimumValue(new BigDecimal(0));
			 * panelHandeingabe.wnfMenge.setMinimumValue(new BigDecimal(0));
			 * panelArtikel.wnfMenge.setMaximumValue(null);
			 * panelHandeingabe.wnfMenge.setMaximumValue(null); } // fuer
			 * negative auftragsmengen nur negative mengen liefern else if
			 * (oAuftragpositionDto.getNMenge().doubleValue() < 0) {
			 * panelArtikel.wnfMenge.setMinimumValue(null);
			 * panelHandeingabe.wnfMenge.setMinimumValue(null);
			 * panelArtikel.wnfMenge.setMaximumValue(0);
			 * panelHandeingabe.wnfMenge.setMaximumValue(0); }
			 */
		}

		super.auftragpositionDto2Components();

		if (oAuftragpositionDto.getNMenge() != null) {

			// der Wert im Feld "Menge LS" ist zunaechst jene Menge, die zu
			// dieser
			// Auftragposition tatsaechlich bereits im Lieferschein erfasst ist;
			// wenn es keine
			// Position gibt, dann ist die Menge 0; SNR/Chargennummer
			// beruecksichtigen
			LieferscheinpositionDto oLieferscheinpositionDto = DelegateFactory
					.getInstance()
					.getLsDelegate()
					.getLieferscheinpositionByLieferscheinAuftragposition(
							tpLieferschein.getLieferscheinDto().getIId(),
							oAuftragpositionDto.getIId());

			if (oLieferscheinpositionDto != null
					&& oLieferscheinpositionDto.getLieferscheinpositionartCNr()
							.equals(LocaleFac.POSITIONSART_IDENT)) {
				panelArtikel.wnfMenge.setBigDecimal(oLieferscheinpositionDto
						.getNMenge());

				if (oLieferscheinpositionDto.getSeriennrChargennrMitMenge() != null) {
					((PanelPositionenArtikelVerkaufSNR) panelArtikel).wtfSerienchargennummer
							.setSeriennummern(oLieferscheinpositionDto
									.getSeriennrChargennrMitMenge(),
									oLieferscheinpositionDto.getArtikelIId(),
									tpLieferschein.getLieferscheinDto()
											.getLagerIId());
				}
				panelArtikel.setUebersteuertesLagerIId(oLieferscheinpositionDto
						.getLagerIId());
			} else if (oLieferscheinpositionDto != null
					&& oLieferscheinpositionDto.getLieferscheinpositionartCNr()
							.equals(LocaleFac.POSITIONSART_HANDEINGABE)) {
				panelHandeingabe.wnfMenge
						.setBigDecimal(oLieferscheinpositionDto.getNMenge());
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
			lieferscheinpositionAusAuftragpositionDto = DelegateFactory
					.getInstance()
					.getLsDelegate()
					.getLieferscheinpositionByLieferscheinAuftragposition(
							tpLieferschein.getLieferscheinDto().getIId(),
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

				LieferscheinpositionDto oLieferscheinpositionDto = new LieferscheinpositionDto();
				oLieferscheinpositionDto
						.setLieferscheinpositionartCNr(oAuftragpositionDto
								.getPositionsartCNr());
				oLieferscheinpositionDto.setCBez(oAuftragpositionDto.getCBez());
				oLieferscheinpositionDto.setXTextinhalt(oAuftragpositionDto
						.getXTextinhalt());
				oLieferscheinpositionDto.setCZusatzbez(oAuftragpositionDto
						.getCZusatzbez());
				oLieferscheinpositionDto.setEinheitCNr(oAuftragpositionDto
						.getEinheitCNr());
				oLieferscheinpositionDto.setLieferscheinIId(tpLieferschein
						.getLieferscheinDto().getIId());
				oLieferscheinpositionDto
						.setAuftragpositionIId(oAuftragpositionDto.getIId()); // zu
				// beginn
				// ist
				// die
				// gesamte
				// menge
				// offen
				oLieferscheinpositionDto.setTypCNr(oAuftragpositionDto
						.getTypCNr());
				oLieferscheinpositionDto
						.setBNettopreisuebersteuert(oAuftragpositionDto
								.getBNettopreisuebersteuert());
				if (LocaleFac.POSITIONSART_INTELLIGENTE_ZWISCHENSUMME
						.equals(oAuftragpositionDto.getPositionsartCNr())) {
					oLieferscheinpositionDto.setFRabattsatz(oAuftragpositionDto
							.getFRabattsatz());

					// SPJ 1779 - Mwstsatzfelder belegen
					oLieferscheinpositionDto
							.setBMwstsatzuebersteuert(oAuftragpositionDto
									.getBMwstsatzuebersteuert());
					oLieferscheinpositionDto.setMwstsatzIId(oAuftragpositionDto
							.getMwstsatzIId());

					// SPJ 1779 - Mwstsatzfelder belegen
					oLieferscheinpositionDto
							.setBMwstsatzuebersteuert(oAuftragpositionDto
									.getBMwstsatzuebersteuert());
					oLieferscheinpositionDto.setMwstsatzIId(oAuftragpositionDto
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
						Integer lsId = tpLieferschein.getLieferscheinDto()
								.getIId();
						vonPosIId = DelegateFactory
								.getInstance()
								.getLieferscheinpositionDelegate()
								.getLSPositionIIdFromPositionNummer(lsId,
										vonPosNr);
						bisPosIId = DelegateFactory
								.getInstance()
								.getLieferscheinpositionDelegate()
								.getLSPositionIIdFromPositionNummer(lsId,
										bisPosNr);

						oLieferscheinpositionDto.setZwsVonPosition(vonPosIId);
						oLieferscheinpositionDto.setZwsBisPosition(bisPosIId);
					}

					String msg = LPMain.getMessageTextRespectUISPr(
							"lp.hint.zwsauftraglieferschein",
							oAuftragpositionDto.getCBez(), vonPosNr, bisPosNr);
					DialogFactory.showModalDialog(
							LPMain.getTextRespectUISPr("lp.warning"), msg);
				}

				DelegateFactory
						.getInstance()
						.getLieferscheinpositionDelegate()
						.createLieferscheinposition(oLieferscheinpositionDto,
								false);

				super.eventActionSave(e, bNeedNoSaveI);
			}
		}
	}

	private void saveHandeingabePosition(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {
		// wenn es zu dieser Auftragposition bereits eine Lieferscheinposition
		// gibt,
		// wird die bestehenden Position aktualisiert
		LieferscheinpositionDto oLieferscheinpositionDto = null;

		oLieferscheinpositionDto = DelegateFactory
				.getInstance()
				.getLsDelegate()
				.getLieferscheinpositionByLieferscheinAuftragposition(
						tpLieferschein.getLieferscheinDto().getIId(),
						oAuftragpositionDto.getIId());

		if (oLieferscheinpositionDto == null) {
			// es gibt noch keine Position, eine neue erfassen
			oLieferscheinpositionDto = new LieferscheinpositionDto();
			oLieferscheinpositionDto
					.setLieferscheinpositionartCNr(oAuftragpositionDto
							.getPositionsartCNr());
			oLieferscheinpositionDto.setArtikelIId(oAuftragpositionDto
					.getArtikelIId());
			oLieferscheinpositionDto
					.setSeriennrChargennrMitMenge(((PanelPositionenArtikelVerkaufSNR) panelArtikel).wtfSerienchargennummer
							.getSeriennummern());
			oLieferscheinpositionDto
					.setBArtikelbezeichnunguebersteuert(oAuftragpositionDto
							.getBArtikelbezeichnunguebersteuert());
			oLieferscheinpositionDto
					.setBMwstsatzuebersteuert(oAuftragpositionDto
							.getBMwstsatzuebersteuert());
			oLieferscheinpositionDto
					.setBRabattsatzuebersteuert(oAuftragpositionDto
							.getBRabattsatzuebersteuert());
			oLieferscheinpositionDto
					.setBNettopreisuebersteuert(oAuftragpositionDto
							.getBNettopreisuebersteuert());
			oLieferscheinpositionDto.setCBez(oAuftragpositionDto.getCBez());
			oLieferscheinpositionDto.setEinheitCNr(oAuftragpositionDto
					.getEinheitCNr());
			oLieferscheinpositionDto.setFRabattsatz(oAuftragpositionDto
					.getFRabattsatz());
			oLieferscheinpositionDto.setFZusatzrabattsatz(oAuftragpositionDto
					.getFZusatzrabattsatz());
			oLieferscheinpositionDto.setLieferscheinIId(tpLieferschein
					.getLieferscheinDto().getIId());
			oLieferscheinpositionDto.setMwstsatzIId(oAuftragpositionDto
					.getMwstsatzIId());
			oLieferscheinpositionDto
					.setNBruttoeinzelpreis(panelHandeingabe.wnfBruttopreis
							.getBigDecimal());
			oLieferscheinpositionDto.setNMenge(panelHandeingabe.wnfMenge
					.getBigDecimal());
			oLieferscheinpositionDto
					.setNMwstbetrag(panelHandeingabe.wnfMwstsumme
							.getBigDecimal());
			oLieferscheinpositionDto
					.setNEinzelpreis(panelHandeingabe.wnfEinzelpreis
							.getBigDecimal());
			oLieferscheinpositionDto
					.setNNettoeinzelpreis(panelHandeingabe.wnfNettopreis
							.getBigDecimal());
			oLieferscheinpositionDto.setAuftragpositionIId(oAuftragpositionDto
					.getIId()); // zu beginn ist die gesamte menge offen
			oLieferscheinpositionDto
					.setNRabattbetrag(panelHandeingabe.wnfRabattsumme
							.getBigDecimal());

			DelegateFactory
					.getInstance()
					.getLieferscheinpositionDelegate()
					.createLieferscheinposition(oLieferscheinpositionDto, false);
		} else {
			// die bestehende Position mit der neuen Menge aktualisieren
			oLieferscheinpositionDto.setNMenge(panelHandeingabe.wnfMenge
					.getBigDecimal());

			DelegateFactory.getInstance().getLieferscheinpositionDelegate()
					.updateLieferscheinposition(oLieferscheinpositionDto);
		}

		// die werte der auftragposition aktualisieren
		oAuftragpositionDto = DelegateFactory.getInstance()
				.getAuftragpositionDelegate()
				.auftragpositionFindByPrimaryKey(oAuftragpositionDto.getIId());

		// buttons schalten
		super.eventActionSave(e, bNeedNoSaveI);
	}

	/**
	 * Bevor eine Identposition im Lieferschein gespeichert werden kann, muessen
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
		BigDecimal nMengeInLieferscheinposition = new BigDecimal(0);

		if (lieferscheinpositionAusAuftragpositionDto != null) {
			nMengeInLieferscheinposition = lieferscheinpositionAusAuftragpositionDto
					.getNMenge();
			nBenoetigteMengeVomLager = panelArtikel.wnfMenge.getBigDecimal()
					.subtract(nMengeInLieferscheinposition);
		} else {
			nBenoetigteMengeVomLager = panelArtikel.wnfMenge.getBigDecimal();
		}

		boolean bChargennrtragend = false;
		if (panelArtikel.getArtikelDto() != null) {
			bChargennrtragend = Helper.short2boolean(panelArtikel
					.getArtikelDto().getBChargennrtragend());
		}

		if (!bChargennrtragend
				&& nBenoetigteMengeVomLager.compareTo(oAuftragpositionDto
						.getNOffeneMenge()) == 1) {

			boolean b = DialogFactory
					.showModalJaNeinDialog(
							getInternalFrame(),
							LPMain.getInstance()
									.getTextRespectUISPr(
											"ls.warning.geawehltemengegroesseroffenemenge.ueberliefern"));

			if (b == true) {
				bDiePositionSpeichern = true;
			} else {
				panelArtikel.wnfMenge
						.setBigDecimal(nMengeInLieferscheinposition
								.add(oAuftragpositionDto.getNOffeneMenge()));

				bDiePositionSpeichern = false;
			}

		} else if (Helper.short2boolean(panelArtikel.getArtikelDto()
				.getBChargennrtragend())
				&& (((PanelPositionenArtikelVerkaufSNR) panelArtikel).wtfSerienchargennummer
						.getText() == null || ((PanelPositionenArtikelVerkaufSNR) panelArtikel).wtfSerienchargennummer
						.getText().length() == 0)) {

			MessageFormat mf = new MessageFormat(LPMain.getInstance()
					.getTextRespectUISPr("lp.hint.chargennummer"));

			mf.setLocale(LPMain.getInstance().getTheClient().getLocUi());

			Object pattern[] = { panelArtikel.wnfMenge.getBigDecimal() };

			DialogFactory.showModalDialog(LPMain.getInstance()
					.getTextRespectUISPr("lp.warning"), mf.format(pattern));

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
				MessageFormat mf = new MessageFormat(LPMain.getInstance()
						.getTextRespectUISPr("lp.hint.seriennummer"));

				mf.setLocale(LPMain.getInstance().getTheClient().getLocUi());

				Object pattern[] = { panelArtikel.wnfMenge.getBigDecimal(),
						new Integer(iAnzahl) };

				DialogFactory.showModalDialog(LPMain.getInstance()
						.getTextRespectUISPr("lp.warning"), mf.format(pattern));

				bDiePositionSpeichern = false;
			}
		}

		if (bDiePositionSpeichern) {

			Integer lagerIId = tpLieferschein.getLieferscheinDto()
					.getLagerIId();
			if (panelArtikel.selectedlagerIId != null) {
				lagerIId = panelArtikel.selectedlagerIId;
			}

			if (!tpLieferschein
					.istMengeAufLager(
							oAuftragpositionDto.getArtikelIId(),
							lagerIId,
							((PanelPositionenArtikelVerkaufSNR) panelArtikel).wtfSerienchargennummer
									.getSeriennummern(),
							nBenoetigteMengeVomLager)) {
				bDiePositionSpeichern = false;
			}
		}

		if (bDiePositionSpeichern) {
			// wenn es zu dieser Auftragposition bereits eine
			// Lieferscheinposition gibt,
			// wird die bestehenden Position aktualisiert
			LieferscheinpositionDto oLieferscheinpositionDto = DelegateFactory
					.getInstance()
					.getLsDelegate()
					.getLieferscheinpositionByLieferscheinAuftragposition(
							tpLieferschein.getLieferscheinDto().getIId(),
							oAuftragpositionDto.getIId());

			if (oLieferscheinpositionDto == null
					|| bZusaetzlicheChargenposition == true) {
				bZusaetzlicheChargenposition = false;
				// es gibt noch keine Position, eine neue erfassen
				oLieferscheinpositionDto = new LieferscheinpositionDto();
				oLieferscheinpositionDto
						.setLieferscheinpositionartCNr(oAuftragpositionDto
								.getPositionsartCNr());
				oLieferscheinpositionDto.setArtikelIId(oAuftragpositionDto
						.getArtikelIId());
				oLieferscheinpositionDto
						.setKostentraegerIId(oAuftragpositionDto
								.getKostentraegerIId());
				oLieferscheinpositionDto.setCLvposition(oAuftragpositionDto
						.getCLvposition());
				// PJ 17254
				oLieferscheinpositionDto
						.setBArtikelbezeichnunguebersteuert(oAuftragpositionDto
								.getBArtikelbezeichnunguebersteuert());
				oLieferscheinpositionDto.setCBez(oAuftragpositionDto.getCBez());
				oLieferscheinpositionDto.setCZusatzbez(oAuftragpositionDto
						.getCZusatzbez());
				if (panelArtikel.getArtikelDto() != null
						&& bBezeichnungEinfrieren == true) {
					oLieferscheinpositionDto
							.setBArtikelbezeichnunguebersteuert(Helper
									.boolean2Short(true));
					ArtikelsprDto asprDto = panelArtikel.getArtikelDto()
							.getArtikelsprDto();
					if (asprDto != null) {
						oLieferscheinpositionDto.setCBez(asprDto.getCBez());
						oLieferscheinpositionDto.setCZusatzbez(asprDto
								.getCZbez());
					}
				}

				oLieferscheinpositionDto
						.setSeriennrChargennrMitMenge(((PanelPositionenArtikelVerkaufSNR) panelArtikel).wtfSerienchargennummer
								.getSeriennummern());

				oLieferscheinpositionDto
						.setBMwstsatzuebersteuert(oAuftragpositionDto
								.getBMwstsatzuebersteuert());
				oLieferscheinpositionDto
						.setBNettopreisuebersteuert(oAuftragpositionDto
								.getBNettopreisuebersteuert());
				oLieferscheinpositionDto
						.setBRabattsatzuebersteuert(oAuftragpositionDto
								.getBRabattsatzuebersteuert());

				oLieferscheinpositionDto.setEinheitCNr(oAuftragpositionDto
						.getEinheitCNr());
				oLieferscheinpositionDto.setFRabattsatz(oAuftragpositionDto
						.getFRabattsatz());
				oLieferscheinpositionDto
						.setFZusatzrabattsatz(oAuftragpositionDto
								.getFZusatzrabattsatz());

				oLieferscheinpositionDto.setLieferscheinIId(tpLieferschein
						.getLieferscheinDto().getIId());
				oLieferscheinpositionDto.setMwstsatzIId(oAuftragpositionDto
						.getMwstsatzIId());
				oLieferscheinpositionDto
						.setNBruttoeinzelpreis(panelArtikel.wnfBruttopreis
								.getBigDecimal());
				oLieferscheinpositionDto.setNMenge(panelArtikel.wnfMenge
						.getBigDecimal());
				oLieferscheinpositionDto
						.setNMwstbetrag(panelArtikel.wnfMwstsumme
								.getBigDecimal());
				oLieferscheinpositionDto
						.setNEinzelpreis(panelArtikel.wnfEinzelpreis
								.getBigDecimal());

				oLieferscheinpositionDto.setLagerIId(panelArtikel.selectedlagerIId);

				if (panelArtikel.wnfMaterialzuschlag != null) {
					oLieferscheinpositionDto
							.setNMaterialzuschlag(panelArtikel.wnfMaterialzuschlag
									.getBigDecimal());
				}

				oLieferscheinpositionDto
						.setNNettoeinzelpreis(panelArtikel.wnfNettopreis
								.getBigDecimal());
				oLieferscheinpositionDto
						.setAuftragpositionIId(oAuftragpositionDto.getIId()); // zu
				// beginn
				// ist
				// die
				// gesamte
				// menge
				// offen
				oLieferscheinpositionDto
						.setNRabattbetrag(panelArtikel.wnfRabattsumme
								.getBigDecimal());
				oLieferscheinpositionDto.setTypCNr(oAuftragpositionDto
						.getTypCNr());
				oLieferscheinpositionDto.setVerleihIId(oAuftragpositionDto
						.getVerleihIId());

				bDiePositionSpeichern = getArtikelsetViewController()
						.validateArtikelsetConstraints(oLieferscheinpositionDto);

				List<SeriennrChargennrMitMengeDto> snrs = getArtikelsetViewController()
						.handleArtikelsetSeriennummern(
								tpLieferschein.getLieferscheinDto()
										.getLagerIId(),
								oLieferscheinpositionDto);

				if (!getArtikelsetViewController()
						.isArtikelsetWithSnrsStoreable(
								oLieferscheinpositionDto, snrs)) {
					bDiePositionSpeichern = false;
				}

				if (bDiePositionSpeichern) {
					Integer pkPosition = DelegateFactory
							.getInstance()
							.getLieferscheinpositionDelegate()
							.createLieferscheinposition(
									oLieferscheinpositionDto, snrs, true);
				}

				if (bDiePositionSpeichern) {
					bFuegeNeuePositionVorDerSelektiertenEin = false;
					getArtikelsetViewController()
							.resetArtikelSetIIdFuerNeuePosition();
				}
			}

			// es gibt in diesem Lieferschein bereits eine
			// Lieferscheinposition zu dieser Auftragposition
			else {
				// zusaetzlich wird die eingegebene Menge abgebucht -> das
				// Handling
				// in Bezug auf die bestehende Position geschieht am Server
				oLieferscheinpositionDto.setNMenge(panelArtikel.wnfMenge
						.getBigDecimal());
				oLieferscheinpositionDto
						.setSeriennrChargennrMitMenge(((PanelPositionenArtikelVerkaufSNR) panelArtikel).wtfSerienchargennummer
								.getSeriennummern());

				List<SeriennrChargennrMitMengeDto> bekannteSnrs = DelegateFactory
						.getInstance()
						.getLieferscheinpositionDelegate()
						.getSeriennrchargennrForArtikelsetPosition(
								oLieferscheinpositionDto.getIId());

				List<SeriennrChargennrMitMengeDto> snrs = getArtikelsetViewController()
						.handleArtikelsetSeriennummern(
								tpLieferschein.getLieferscheinDto()
										.getLagerIId(),
								oLieferscheinpositionDto, bekannteSnrs);

				if (!getArtikelsetViewController()
						.isArtikelsetWithSnrsStoreable(
								oLieferscheinpositionDto, snrs)) {
					bDiePositionSpeichern = false;
				}

				if (bDiePositionSpeichern) {
					DelegateFactory
							.getInstance()
							.getLieferscheinpositionDelegate()
							.updateLieferscheinposition(
									oLieferscheinpositionDto, snrs);
				}
			}

			// buttons schalten
			super.eventActionSave(e, bNeedNoSaveI);
		}/*
		 * else { panelArtikel.wnfMenge.setBigDecimal(DelegateFactory
		 * .getInstance() .getLagerDelegate() .getMengeAufLager(
		 * oAuftragpositionDto.getArtikelIId(),
		 * tpLieferschein.getLieferscheinDto() .getLagerIId(), null)); }
		 */
	}

	/**
	 * Bevor eine Identposition im Lieferschein gespeichert werden kann, muessen
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
		BigDecimal nMengeInLieferscheinposition = new BigDecimal(0);

		if (lieferscheinpositionAusAuftragpositionDto != null) {
			nMengeInLieferscheinposition = lieferscheinpositionAusAuftragpositionDto
					.getNMenge();
			nBenoetigteMengeVomLager = panelArtikel.wnfMenge.getBigDecimal()
					.subtract(nMengeInLieferscheinposition);
		} else {
			nBenoetigteMengeVomLager = panelArtikel.wnfMenge.getBigDecimal();
		}

		if (nBenoetigteMengeVomLager.compareTo(oAuftragpositionDto
				.getNOffeneMenge()) == -1) {
			DialogFactory.showModalDialog(
					LPMain.getInstance().getTextRespectUISPr("lp.warning"),
					LPMain.getInstance().getTextRespectUISPr(
							"ls.warning.geawehltemengegroesseroffenemenge"));

			panelArtikel.wnfMenge.setBigDecimal(nMengeInLieferscheinposition
					.add(oAuftragpositionDto.getNOffeneMenge()));

			bDiePositionSpeichern = false;
		} else if (Helper.short2boolean(panelArtikel.getArtikelDto()
				.getBChargennrtragend())
				&& (((PanelPositionenArtikelVerkaufSNR) panelArtikel).wtfSerienchargennummer
						.getText() == null || ((PanelPositionenArtikelVerkaufSNR) panelArtikel).wtfSerienchargennummer
						.getText().length() == 0)) {

			MessageFormat mf = new MessageFormat(LPMain.getInstance()
					.getTextRespectUISPr("lp.hint.chargennummer"));

			mf.setLocale(LPMain.getInstance().getTheClient().getLocUi());

			Object pattern[] = { panelArtikel.wnfMenge.getBigDecimal() };

			DialogFactory.showModalDialog(LPMain.getInstance()
					.getTextRespectUISPr("lp.warning"), mf.format(pattern));

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
				MessageFormat mf = new MessageFormat(LPMain.getInstance()
						.getTextRespectUISPr("lp.hint.seriennummer"));

				mf.setLocale(LPMain.getInstance().getTheClient().getLocUi());

				Object pattern[] = { panelArtikel.wnfMenge.getBigDecimal(),
						new Integer(iAnzahl) };

				DialogFactory.showModalDialog(LPMain.getInstance()
						.getTextRespectUISPr("lp.warning"), mf.format(pattern));

				bDiePositionSpeichern = false;
			}
		}

		if (bDiePositionSpeichern) {
			// IMS 2129
			// wenn es zu dieser Auftragposition bereits eine
			// Lieferscheinposition gibt,
			// wird die bestehenden Position aktualisiert
			LieferscheinpositionDto oLieferscheinpositionDto = null;

			oLieferscheinpositionDto = DelegateFactory
					.getInstance()
					.getLsDelegate()
					.getLieferscheinpositionByLieferscheinAuftragposition(
							tpLieferschein.getLieferscheinDto().getIId(),
							oAuftragpositionDto.getIId());

			if (oLieferscheinpositionDto == null) {
				// es gibt noch keine Position, eine neue erfassen
				oLieferscheinpositionDto = new LieferscheinpositionDto();
				oLieferscheinpositionDto
						.setLieferscheinpositionartCNr(oAuftragpositionDto
								.getPositionsartCNr());
				oLieferscheinpositionDto
						.setKostentraegerIId(oAuftragpositionDto
								.getKostentraegerIId());
				oLieferscheinpositionDto.setCLvposition(oAuftragpositionDto
						.getCLvposition());
				oLieferscheinpositionDto.setArtikelIId(oAuftragpositionDto
						.getArtikelIId());
				oLieferscheinpositionDto
						.setSeriennrChargennrMitMenge(((PanelPositionenArtikelVerkaufSNR) panelArtikel).wtfSerienchargennummer
								.getSeriennummern());
				oLieferscheinpositionDto
						.setBArtikelbezeichnunguebersteuert(oAuftragpositionDto
								.getBArtikelbezeichnunguebersteuert());
				oLieferscheinpositionDto
						.setBMwstsatzuebersteuert(oAuftragpositionDto
								.getBMwstsatzuebersteuert());
				oLieferscheinpositionDto
						.setBNettopreisuebersteuert(oAuftragpositionDto
								.getBNettopreisuebersteuert());
				oLieferscheinpositionDto
						.setBRabattsatzuebersteuert(oAuftragpositionDto
								.getBRabattsatzuebersteuert());
				oLieferscheinpositionDto.setCBez(oAuftragpositionDto.getCBez());
				oLieferscheinpositionDto.setCZusatzbez(oAuftragpositionDto
						.getCZusatzbez());
				oLieferscheinpositionDto.setEinheitCNr(oAuftragpositionDto
						.getEinheitCNr());
				oLieferscheinpositionDto.setFRabattsatz(oAuftragpositionDto
						.getFRabattsatz());
				oLieferscheinpositionDto
						.setFZusatzrabattsatz(oAuftragpositionDto
								.getFZusatzrabattsatz());

				oLieferscheinpositionDto.setLieferscheinIId(tpLieferschein
						.getLieferscheinDto().getIId());
				oLieferscheinpositionDto.setMwstsatzIId(oAuftragpositionDto
						.getMwstsatzIId());
				oLieferscheinpositionDto
						.setNBruttoeinzelpreis(panelArtikel.wnfBruttopreis
								.getBigDecimal());
				oLieferscheinpositionDto.setNMenge(panelArtikel.wnfMenge
						.getBigDecimal());
				oLieferscheinpositionDto
						.setNMwstbetrag(panelArtikel.wnfMwstsumme
								.getBigDecimal());
				oLieferscheinpositionDto
						.setNEinzelpreis(panelArtikel.wnfEinzelpreis
								.getBigDecimal());
				oLieferscheinpositionDto.setLagerIId(panelArtikel.selectedlagerIId);

				if (panelArtikel.wnfMaterialzuschlag != null) {
					oLieferscheinpositionDto
							.setNMaterialzuschlag(panelArtikel.wnfMaterialzuschlag
									.getBigDecimal());
				}

				oLieferscheinpositionDto
						.setNNettoeinzelpreis(panelArtikel.wnfNettopreis
								.getBigDecimal());
				oLieferscheinpositionDto
						.setAuftragpositionIId(oAuftragpositionDto.getIId()); // zu
				// beginn
				// ist
				// die
				// gesamte
				// menge
				// offen
				oLieferscheinpositionDto
						.setNRabattbetrag(panelArtikel.wnfRabattsumme
								.getBigDecimal());

				DelegateFactory
						.getInstance()
						.getLieferscheinpositionDelegate()
						.createLieferscheinposition(oLieferscheinpositionDto,
								false);
			}

			// es gibt in diesem Lieferschein bereits eine Lieferscheinposition
			// zu dieser Auftragposition
			else {
				// zusaetzlich wird die eingegebene Menge abgebucht -> das
				// Handling
				// in Bezug auf die bestehende Position geschieht am Server
				oLieferscheinpositionDto.setNMenge(panelArtikel.wnfMenge
						.getBigDecimal());
				oLieferscheinpositionDto
						.setSeriennrChargennrMitMenge(((PanelPositionenArtikelVerkaufSNR) panelArtikel).wtfSerienchargennummer
								.getSeriennummern());

				DelegateFactory.getInstance().getLieferscheinpositionDelegate()
						.updateLieferscheinposition(oLieferscheinpositionDto);
			}

			// buttons schalten
			super.eventActionSave(e, bNeedNoSaveI);
			// IMS 2129
			/*
			 * } else {
			 * panelArtikel.wnfMenge.setDouble(DelegateFactory.getInstance
			 * ().getLagerDelegate().getMengeAufLager(
			 * oAuftragpositionDto.getArtikelIId(),
			 * tpLieferschein.getLieferscheinDto().getLagerIId(), null)); }
			 */
		}
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {
		setTBelegdatumMwstsatz(tpLieferschein.getLieferscheinDto()
				.getTBelegdatum());
		super.eventYouAreSelected(false);

		setLagerIId(tpLieferschein.getLieferscheinDto().getLagerIId());

		// auch der zugehoerige Auftrag muss gelockt werden
		if (oAuftragpositionDto != null) {
			lockMeAuftrag = new LockMeDto(HelperClient.LOCKME_AUFTRAG,
					oAuftragpositionDto.getBelegIId() + "", LPMain
							.getInstance().getCNrUser());
		}

		// wenn der Lieferschein gerade gelockt ist, die Eingabefelder
		// freischalten
		if (getLockedstateDetailMainKey().getIState() == PanelBasis.LOCK_IS_LOCKED_BY_ME) {
			panelArtikel.wnfMenge.setBigDecimal(null);
			panelArtikel.wnfMenge.setEditable(true);

			zeigeSerienchargennummer(true, true);
		} else {
			zeigeSerienchargennummer(false, false);
		}
		refreshMyComponents();
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

		tpLieferschein.setTitleLieferschein(LPMain.getInstance()
				.getTextRespectUISPr("ls.title.panel.sichtauftrag"));

		aktualisiereStatusbar();
	}

	private void aktualisiereStatusbar() throws Throwable {
		setStatusbarPersonalIIdAnlegen(tpLieferschein.getLieferscheinDto()
				.getPersonalIIdAnlegen());
		setStatusbarTAnlegen(tpLieferschein.getLieferscheinDto().getTAnlegen());
		setStatusbarPersonalIIdAendern(tpLieferschein.getLieferscheinDto()
				.getPersonalIIdAendern());
		setStatusbarTAendern(tpLieferschein.getLieferscheinDto().getTAendern());
		setStatusbarStatusCNr(tpLieferschein.getLieferscheinStatus());

		setStatusbarSpalte5(this
				.getLagerstandFuerStatusbarSpalte5(tpLieferschein
						.getLieferscheinDto().getLagerIId()));
	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_LIEFERSCHEIN;
	}

	protected void eventActionLock(ActionEvent e) throws Throwable {

		// Lieferschein locken.
		super.eventActionLock(e);

		if (lockMeAuftrag != null) {
			// Zugehoerigen Auftrag locken.
			super.lock(lockMeAuftrag);
		}
	}

	protected void eventActionUnlock(ActionEvent e) throws Throwable {

		// Lieferschein unlocken.
		super.eventActionUnlock(e);
		bZusaetzlicheChargenposition = false;
		if (lockMeAuftrag != null) {
			// Zugehoerigen Auftrag unlocken.
			super.unlock(lockMeAuftrag);
		}
	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {

		if (e.getActionCommand().equals(ACTION_SPECIAL_ZUSAETZLICHE_CHARGEN)) {

			bZusaetzlicheChargenposition = true;
			super.eventActionUpdate(e, false);
			((PanelPositionenArtikelVerkaufSNR) panelArtikel).wtfSerienchargennummer
					.setText(null);
			befuelleMengeMitVorschlagswert();
		} else if (e.getActionCommand().equals(
				ACTION_SPECIAL_LOSABLIEFERMENGE_UEBERNEHMEN)) {
			if (oAuftragpositionDto != null
					&& oAuftragpositionDto.getBelegIId() != null
					&& oAuftragpositionDto.getArtikelIId() != null) {
				BigDecimal bd = DelegateFactory
						.getInstance()
						.getFertigungDelegate()
						.getMengeDerLetztenLosablieferungEinerAuftragsposition(
								oAuftragpositionDto.getBelegIId(),
								oAuftragpositionDto.getArtikelIId());
				if (bd != null) {
					panelArtikel.wnfMenge.setBigDecimal(bd);
				}
			}
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_KEIN_LIEFERREST)) {
			if (!oAuftragpositionDto.getAuftragpositionstatusCNr().equals(
					AuftragServiceFac.AUFTRAGPOSITIONSTATUS_ERLEDIGT)) {

				lieferscheinpositionAusAuftragpositionDto = DelegateFactory
						.getInstance()
						.getLsDelegate()
						.getLieferscheinpositionByLieferscheinAuftragposition(
								tpLieferschein.getLieferscheinDto().getIId(),
								oAuftragpositionDto.getIId());

				if (!oAuftragpositionDto.getAuftragpositionstatusCNr().equals(
						AuftragServiceFac.AUFTRAGPOSITIONSTATUS_ERLEDIGT)) {
					// tpAuftrag
					boolean answer = (DialogFactory
							.showMeldung(
									LPMain.getInstance()
											.getTextRespectUISPr(
													"bes.menu.bearbeiten.manuellerledigen.mitkeinlieferrest")
											+ " ?", LPMain.getInstance()
											.getTextRespectUISPr("lp.warning"),
									javax.swing.JOptionPane.YES_NO_OPTION) == javax.swing.JOptionPane.YES_OPTION);
					if (answer) {
						DelegateFactory.getInstance()
								.getAuftragpositionDelegate()
								.manuellErledigen(oAuftragpositionDto.getIId());
						tpLieferschein
								.getLieferscheinPositionenSichtAuftragTop()
								.eventYouAreSelected(true);

						if (lieferscheinpositionAusAuftragpositionDto != null) {
							DelegateFactory
									.getInstance()
									.getLieferscheinpositionDelegate()
									.lieferscheinpositionKeinLieferrestEintragen(
											lieferscheinpositionAusAuftragpositionDto
													.getIId(),
											true);
						}

					}
				} else {
					boolean answer = (DialogFactory
							.showMeldung(
									LPMain.getInstance()
											.getTextRespectUISPr(
													"bes.menu.bearbeiten.erledigungaufheben.mitkeinlieferrest")
											+ " ?", LPMain.getInstance()
											.getTextRespectUISPr("lp.warning"),
									javax.swing.JOptionPane.YES_NO_OPTION) == javax.swing.JOptionPane.YES_OPTION);
					if (answer) {
						DelegateFactory
								.getInstance()
								.getAuftragpositionDelegate()
								.manuellErledigungAufgeben(
										oAuftragpositionDto.getIId());
						tpLieferschein
								.getLieferscheinPositionenSichtAuftragTop()
								.eventYouAreSelected(true);

						if (lieferscheinpositionAusAuftragpositionDto != null) {
							DelegateFactory
									.getInstance()
									.getLieferscheinpositionDelegate()
									.lieferscheinpositionKeinLieferrestEintragen(
											lieferscheinpositionAusAuftragpositionDto
													.getIId(),
											false);
						}

					}
				}

			}

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
		bZusaetzlicheChargenposition = false;
		// auch der zugehoerige Auftrag muss gelockt werden
		if (oAuftragpositionDto != null) {
			lockMeAuftrag = new LockMeDto(HelperClient.LOCKME_AUFTRAG,
					oAuftragpositionDto.getBelegIId() + "", LPMain
							.getInstance().getCNrUser());
		}
	}

	public void eventActionNew(EventObject eventObject, boolean bLockMeI,
			boolean bNeedNoNewI) throws Throwable {
		if (tpLieferschein.istAktualisierenLieferscheinErlaubt()) {

			super.eventActionNew(eventObject, true, false);

			setDefaults();

			// es kann ein normales oder das spezielle neu sein
			if (((ItemChangedEvent) eventObject).getID() == ItemChangedEvent.ACTION_MY_OWN_NEW) {

				// alle Positionen des Auftrags in den Lieferschein uebernehmen
				if (tpLieferschein.getLieferscheinDto().getLieferscheinartCNr()
						.equals(LieferscheinFac.LSART_AUFTRAG)) {
					uebernimmAlleOffenenAuftragpositionenOhneBenutzerinteraktion();
				}
			}
		} else {
			tpLieferschein.getLieferscheinPositionenTop().updateButtons(
					tpLieferschein.getLieferscheinPositionenBottom()
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
	 * Alle offenen Auftragpositionen in den Lieferschein kopieren, die ohne
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
			if (tpLieferschein.getLieferscheinPositionenSichtAuftragTop()
					.getSelectedId() != null) {
				AuftragDto auftragDto = tpLieferschein
						.getAuftragDtoSichtAuftrag();
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
										tpLieferschein.getLieferscheinDto()
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
										tpLieferschein.getLieferscheinDto()
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
												tpLieferschein
														.getLieferscheinDto()
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
						.getLsDelegate()
						.uebernimmAlleOffenenAuftragpositionenOhneBenutzerinteraktionNew(
								tpLieferschein.getLieferscheinDto().getIId(),
								tpLieferschein.getAuftragDtoSichtAuftrag()
										.getIId(), artikelSets);

				// DelegateFactory
				// .getInstance()
				// .getLsDelegate()
				// .uebernimmAlleOffenenAuftragpositionenOhneBenutzerinteraktion(
				// tpLieferschein.getLieferscheinDto().getIId(),
				// tpLieferschein.getAuftragDtoSichtAuftrag()
				// .getIId());

				// das Panel neu laden, damit die uebernommenen Positionen
				// verschwinden
				tpLieferschein.getLsPositionenSichtAuftrag()
						.eventYouAreSelected(false);
			}
		} finally {
			// den gelockten Auftrag explizit freigeben
			if (lockMeAuftrag != null) {
				super.unlock(lockMeAuftrag);
			}

			intFrame.setKeyWasForLockMe(tpLieferschein.getLieferscheinDto()
					.getIId() + "");
		}
	}

	private void refreshMyComponents() throws Throwable {
		boolean bEnable = true;
		LPButtonAction item = null;
		if (oAuftragpositionDto != null) {
			String cArt = oAuftragpositionDto.getPositionsartCNr();
			if (cArt != null) {
				bEnable = !cArt.equals(LocaleFac.POSITIONSART_POSITION);
				item = (LPButtonAction) getHmOfButtons().get(ACTION_UPDATE);
				item.getButton().setEnabled(bEnable);
			}
			/*
			 * if (panelArtikel.getArtikelDto() != null &&
			 * panelArtikel.getArtikelDto().getBChargennrtragend() != null) if
			 * (Helper.short2boolean(panelArtikel.getArtikelDto()
			 * .getBChargennrtragend())) { if (oAuftragpositionDto.getNMenge()
			 * != null && oAuftragpositionDto.getNOffeneMenge() != null) {
			 * bEnable = oAuftragpositionDto.getNMenge().intValue() ==
			 * oAuftragpositionDto .getNOffeneMenge().intValue(); item =
			 * (LPButtonAction) getHmOfButtons().get( ACTION_UPDATE);
			 * item.getButton().setEnabled(bEnable); } }
			 */
		}
	}

	protected String getWaehrungCNrBeleg() {
		return tpLieferschein.getLieferscheinDto().getWaehrungCNr();
	}
}
