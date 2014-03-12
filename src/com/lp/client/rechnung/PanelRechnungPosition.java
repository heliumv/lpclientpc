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
package com.lp.client.rechnung;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.math.BigDecimal;
import java.sql.Date;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JPanel;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.ArtikelsetViewController;
import com.lp.client.frame.component.FehlmengenAufloesen;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelPositionen2;
import com.lp.client.frame.component.PanelPositionenArtikelVerkauf;
import com.lp.client.frame.component.PanelPositionenArtikelVerkaufSNR;
import com.lp.client.frame.component.PositionNumberHelperRechnung;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPButtonAction;
import com.lp.client.pc.LPMain;
import com.lp.server.artikel.service.SeriennrChargennrMitMengeDto;
import com.lp.server.auftrag.service.AuftragDto;
import com.lp.server.auftrag.service.AuftragpositionDto;
import com.lp.server.inserat.service.InseratDto;
import com.lp.server.lieferschein.service.LieferscheinDto;
import com.lp.server.rechnung.service.RechnungDto;
import com.lp.server.rechnung.service.RechnungFac;
import com.lp.server.rechnung.service.RechnungPositionDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MwstsatzDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.service.Artikelset;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

/**
 * <p>
 * <I>Panel zur Darstellung einer Rechnungs- / Gutschriftenposition</I>
 * </p>
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2008
 * </p>
 * <p>
 * Erstellungsdatum <I>02. 03. 2005</I>
 * </p>
 * <p>
 * </p>
 * 
 * @author Martin Bluehweis
 * @version $Revision: 1.38 $
 */

public class PanelRechnungPosition extends PanelPositionen2 {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private TabbedPaneRechnungAll tpRechnung = null;

	private WrapperTextField wtfAuftragNummer = null;
	private WrapperTextField wtfAuftragProjekt = null;
	private WrapperTextField wtfAuftragBestellnummer = null;

	/**
	 * Konstruktor.
	 * 
	 * @param internalFrame
	 *            der InternalFrame auf dem das Panel sitzt
	 * @param add2TitleI
	 *            der default Titel des Panels
	 * @param key
	 *            PK der Position
	 * @param tpRechnung
	 *            TabbedPaneRechnungAll
	 * @param typ
	 *            int
	 * @throws Throwable
	 */
	public PanelRechnungPosition(InternalFrame internalFrame,
			String add2TitleI, Object key, TabbedPaneRechnungAll tpRechnung,
			int typ) throws Throwable {
		super(internalFrame, add2TitleI, key, typ);
		this.tpRechnung = tpRechnung;
		zwController
				.setPositionNumberHelper(new PositionNumberHelperRechnung());
		zwController.setBelegDto(tpRechnung.getRechnungDto());
		jbInit();
		initComponents();
		initPanel();
	}

	protected RechnungPositionDto getPositionDto() {
		return tpRechnung.getRechnungPositionDto();
	}

	private void jbInit() throws Throwable {
		// zusaetzliche buttons
		String[] aWhichButtonIUse = { ACTION_UPDATE, ACTION_SAVE,
				ACTION_DELETE, ACTION_DISCARD, ACTION_PRINT, ACTION_TEXT };

		enableToolsPanelButtons(aWhichButtonIUse);

		// zusaetzliche Felder fuer den Auftrag beim Artikel
		iZeile++;
		WrapperLabel wlaAuftrag = new WrapperLabel(
				LPMain.getTextRespectUISPr("auft.auftrag"));
		wlaAuftrag.setMinimumSize(new Dimension(iSpaltenbreiteArtikelMitGoto,
				Defaults.getInstance().getControlHeight()));
		wlaAuftrag.setPreferredSize(new Dimension(iSpaltenbreiteArtikelMitGoto,
				Defaults.getInstance().getControlHeight()));
		wtfAuftragNummer = new WrapperTextField();
		wtfAuftragNummer.setActivatable(false);
		wtfAuftragNummer.setMinimumSize(new Dimension(80, Defaults
				.getInstance().getControlHeight()));
		wtfAuftragNummer.setPreferredSize(new Dimension(80, Defaults
				.getInstance().getControlHeight()));
		wtfAuftragProjekt = new WrapperTextField();
		wtfAuftragProjekt.setActivatable(false);
		wtfAuftragBestellnummer = new WrapperTextField();
		wtfAuftragBestellnummer.setActivatable(false);
		JPanel jpaAuftrag = new JPanel(new GridBagLayout());
		jpaAuftrag.add(wlaAuftrag, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						2, 2, 2, 2), 0, 0));
		jpaAuftrag.add(wtfAuftragNummer, new GridBagConstraints(1, 0, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaAuftrag.add(wtfAuftragProjekt, new GridBagConstraints(2, 0, 1, 1,
				1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaAuftrag.add(wtfAuftragBestellnummer, new GridBagConstraints(3, 0, 1,
				1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		add(jpaAuftrag, new GridBagConstraints(0, iZeile, 1, 1, 1.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						0, 0, 0, 0), 0, 0));

		this.add(getPanelStatusbar(), new GridBagConstraints(0, 100, 1, 1, 1.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
		getInternalFrame().addItemChangedListener(this);
		panelHandeingabe.setVisibleZeileLieferterminposition(false);
	}

	private void initPanel() throws Throwable {
		// combobox Positionen in der UI Sprache des Benutzers fuellen
		if (tpRechnung.getRechnungstyp().equals(
				RechnungFac.RECHNUNGTYP_RECHNUNG)) {
			setPositionsarten(DelegateFactory.getInstance()
					.getRechnungServiceDelegate().getAllRechnungpositionsart());
		} else if (tpRechnung.getRechnungstyp().equals(
				RechnungFac.RECHNUNGTYP_GUTSCHRIFT)) {
			setPositionsarten(DelegateFactory.getInstance()
					.getRechnungServiceDelegate()
					.getAllGutschriftpositionsart());
		} else if (tpRechnung.getRechnungstyp().equals(
				RechnungFac.RECHNUNGTYP_PROFORMARECHNUNG)) {
			setPositionsarten(DelegateFactory.getInstance()
					.getRechnungServiceDelegate()
					.getAllProformarechnungpositionsart());
		}
	}

	protected void setDefaults() throws Throwable {
		tpRechnung.setRechnungPositionDto(new RechnungPositionDto());
		tpRechnung.getRechnungPositionDto().setBNettopreisuebersteuert(
				new Short((short) 0));
		leereAlleFelder(this);

		// default positionsart ist ident
		wcoPositionsart
				.setKeyOfSelectedItem(RechnungFac.POSITIONSART_RECHNUNG_IDENT);

		super.setDefaults();

		// der Vorschlagswert fuer eine frei erfasste Position ist 1
		panelArtikel.wnfMenge.setDouble(new Double(1));

		// dem panelArtikel muss das Lager und der Kunde gesetzt werden
		if (tpRechnung.getRechnungstyp().equals(
				RechnungFac.RECHNUNGTYP_GUTSCHRIFT)) {
			((PanelPositionenArtikelGutschrift) panelArtikel)
					.setKundeDto(tpRechnung.getKundeDto());
			((PanelPositionenArtikelGutschrift) panelArtikel)
					.setIIdLager(tpRechnung.getRechnungDto().getLagerIId());
		} else {
			// AR/PR
			((PanelPositionenArtikelVerkaufSNR) panelArtikel)
					.setKundeDto(tpRechnung.getKundeDto());
			((PanelPositionenArtikelVerkaufSNR) panelArtikel)
					.setIIdLager(tpRechnung.getRechnungDto().getLagerIId());
		}

		if (tpRechnung.getRechnungDto() != null
				&& tpRechnung.getRechnungDto().getIId() != null) {
			panelHandeingabe.setWaehrungCNr(tpRechnung.getRechnungDto()
					.getWaehrungCNr());
			panelArtikel.setWaehrungCNr(tpRechnung.getRechnungDto()
					.getWaehrungCNr());

			// im PanelArtikel alles fuer die VKPF vorbereiten
			((PanelPositionenArtikelVerkauf) panelArtikel)
					.setCNrWaehrung(tpRechnung.getRechnungDto()
							.getWaehrungCNr());
			((PanelPositionenArtikelVerkauf) panelArtikel)
					.setWechselkurs(new Double(tpRechnung.getRechnungDto()
							.getNKurs().doubleValue()));
			((PanelPositionenArtikelVerkauf) panelArtikel)
					.setKundeDto(tpRechnung.getKundeDto());
			((PanelPositionenArtikelVerkauf) panelArtikel)
					.setGueltigkeitsdatumArtikeleinzelverkaufspreis(new Date(
							tpRechnung.getRechnungDto().getTBelegdatum()
									.getTime()));

			panelLieferschein.setKundeDto(tpRechnung.getKundeDto());

			// dem panelArtikel muss die momentan eingetragene Menge gesetzt
			// werden -> fuer Chargennummern
			if (tpRechnung.getRechnungstyp().equals(
					RechnungFac.RECHNUNGTYP_GUTSCHRIFT)) {
			} else {
				((PanelPositionenArtikelVerkaufSNR) panelArtikel)
						.setDDBisherigeMenge(new Double(0));
			}
			if (!panelHandeingabe.getBDefaultMwstsatzAusArtikel()) {
				if (tpRechnung.getKundeDto() != null
						&& tpRechnung.getKundeDto().getIId() != null) {
					// Aktuellen MWST-Satz uebersetzen.
					MwstsatzDto mwstsatzDtoAktuell = DelegateFactory
							.getInstance()
							.getMandantDelegate()
							.mwstsatzFindByMwstsatzbezIIdAktuellster(
									tpRechnung.getKundeDto()
											.getMwstsatzbezIId());
					panelHandeingabe.wcoMwstsatz
							.setKeyOfSelectedItem(mwstsatzDtoAktuell.getIId());
				}
			}
		}
		panelArtikel.wtfZusatzbezeichnung.setActivatable(true);
	}

	protected void eventActionPrint(ActionEvent e) throws Throwable {
		tpRechnung.print();

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
	 */
	public void eventActionNew(EventObject eventObject, boolean bLockMeI,
			boolean bNeedNoNewI) throws Throwable {
		super.eventActionNew(eventObject, true, false); // LockMeForNew setzen
		setDefaults();
		if (((ItemChangedEvent) eventObject).getID() == ItemChangedEvent.ACTION_POSITION_VORPOSITIONEINFUEGEN) {
			bFuegeNeuePositionVorDerSelektiertenEin = true;
		}
		super.clearStatusbar();
	}

	/**
	 * Verwerfen der aktuelle Usereingabe und zurueckgehen auf den bestehenden
	 * Datensatz, wenn einer existiert.
	 * 
	 * @param e
	 *            Ereignis
	 * @throws Throwable
	 */
	protected void eventActionDiscard(ActionEvent e) throws Throwable {
		super.eventActionDiscard(e);

		panelArtikel.setArtikelEingabefelderEditable(false);

		wcoPositionsart.setEnabled(false);
	}

	protected void eventActionUpdate(ActionEvent aE, boolean bNeedNoUpdateI)
			throws Throwable {
		if (((InternalFrameRechnung) getInternalFrame())
				.isUpdateAllowedForRechnungDto(tpRechnung.getRechnungDto())) {
			// PJ 14648 Setartikel
			if (panelArtikel.getArtikelDto().getIId() != null
					&& getPositionDto().getPositioniIdArtikelset() != null) {
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

			super.eventActionUpdate(aE, false);
			panelArtikel.setArtikelEingabefelderEditable(true);
			if (panelArtikel instanceof PanelPositionenArtikelVerkauf) {
				if (getInternalFrame().bRechtDarfPreiseAendernVerkauf == true) {
					((PanelPositionenArtikelVerkauf) panelArtikel).wbuPreisauswahl
							.setEnabled(true);
				} else {
					((PanelPositionenArtikelVerkauf) panelArtikel).wbuPreisauswahl
							.setEnabled(false);
				}
			}
			// tpRechnung.setRechnungDto(getInternalFrame().getRechnungDelegate()
			// .rechnungFindByPrimaryKey(tpRechnung.getRechnungDto().getIId()));
		}
		setzePositionsartAenderbar(getPositionDto());
		panelArtikel.setzeEinheitAenderbar();
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {
		super.eventYouAreSelected(false);
		setTBelegdatumMwstsatz(tpRechnung.getRechnungDto().getTBelegdatum());
		// den gesamten Inhalt zuruecksetzen, ausloeser war ev. ein discard
		setDefaults();
		// position neu einlesen, ausloeser war ev. ein refresh
		Object pkPosition = getKeyWhenDetailPanel();
		if (pkPosition != null && !pkPosition.equals(LPMain.getLockMeForNew())) {
			tpRechnung.setRechnungPositionDto(DelegateFactory.getInstance()
					.getRechnungDelegate()
					.rechnungPositionFindByPrimaryKey((Integer) pkPosition));
			dto2Components();
		} else {
			// MB letzteArtikelNr:
			panelArtikel.setLetzteArtikelCNr();
		}

		// wenn die Rechnung gerade gelockt ist, die Artikeleingabefelder
		// freischalten
		if (getLockedstateDetailMainKey().getIState() == LOCK_IS_LOCKED_BY_ME) {
			panelArtikel.setArtikelEingabefelderEditable(true);
			if (tpRechnung.getRechnungstyp().equals(
					RechnungFac.RECHNUNGTYP_GUTSCHRIFT)) {
			} else {
				((PanelPositionenArtikelVerkaufSNR) panelArtikel)
						.zeigeSerienchargennummer(true, false);
			}
		} else {
			panelArtikel.setArtikelEingabefelderEditable(false);
			if (tpRechnung.getRechnungstyp().equals(
					RechnungFac.RECHNUNGTYP_GUTSCHRIFT)) {
				((PanelPositionenArtikelGutschrift) panelArtikel).wtfSerienchargennummer
						.setSeriennummern(getPositionDto()
								.getSeriennrChargennrMitMenge(),
								getPositionDto().getArtikelIId(), tpRechnung
										.getRechnungDto().getLagerIId());
			} else {
				((PanelPositionenArtikelVerkaufSNR) panelArtikel)
						.zeigeSerienchargennummer(false, false);
			}
		}
		setzePositionsartAenderbar(getPositionDto());
		panelArtikel.setzeEinheitAenderbar();
		RechnungPositionDto positionDto = tpRechnung.getRechnungPositionDto();
		LPButtonAction item = null;
		if (positionDto.getRechnungpositionartCNr() != null) {
			boolean bEnableVerschieben = !positionDto
					.getRechnungpositionartCNr().equals(
							LocaleFac.POSITIONSART_POSITION);
			item = (LPButtonAction) tpRechnung.getRechnungPositionTop()
					.getHmOfButtons()
					.get(PanelBasis.ACTION_POSITION_VONNNACHNMINUS1);
			item.getButton().setVisible(bEnableVerschieben);
			item = (LPButtonAction) tpRechnung.getRechnungPositionTop()
					.getHmOfButtons()
					.get(PanelBasis.ACTION_POSITION_VONNNACHNPLUS1);
			item.getButton().setVisible(bEnableVerschieben);
		}
		setStatusbarSpalte5(this.getLagerstandFuerStatusbarSpalte5(tpRechnung
				.getRechnungDto().getLagerIId()));

	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {
		try {
			if (wirdPreisvorschlagsdialogGeradeAngezeit()) {
				return;
			}
			// Die menge die ich vom lager brauche ist die differenz zwischen
			// vorher und jetzt
			BigDecimal bdBenoetigtVomLager = new BigDecimal(0);
			if (getPositionDto() != null
					&& getPositionDto().getNMenge() != null) {
				bdBenoetigtVomLager = bdBenoetigtVomLager
						.subtract(getPositionDto().getNMenge());
			}

			zwController.setBelegDto(tpRechnung.getRechnungDto());
			zwController.setBelegPositionDto(getPositionDto());
			zwController
					.setSelectedPositionIId(bFuegeNeuePositionVorDerSelektiertenEin ? tpRechnung
							.getSelectedIdPositionen() : null);

			calculateFields();
			if (allMandatoryFieldsSetDlg()) {
				components2Dto();
				boolean bDiePositionSpeichern = true;
				if (getPositionsartCNr().equals(LocaleFac.POSITIONSART_IDENT)) {
					if (getPositionDto().getNMenge().compareTo(
							new BigDecimal(0)) < 0) {
						// Menge 0
						DialogFactory
								.showModalDialog(
										LPMain.getTextRespectUISPr("lp.hint"),
										LPMain.getTextRespectUISPr("rechnung.hint.mengegroessernull"));
						return;
					}
					bdBenoetigtVomLager = bdBenoetigtVomLager
							.add(getPositionDto().getNMenge());
					// brauch ich mehr als vorher?
					if (tpRechnung.getRechnungstyp().equals(
							RechnungFac.RECHNUNGTYP_GUTSCHRIFT)) {
						/**
						 * @todo GS lager pruefen (bei mengenminderung) PJ 5080
						 */
					} else {
						if (bdBenoetigtVomLager.doubleValue() > 0) {
							bDiePositionSpeichern = tpRechnung
									.istMengeAufLager(
											panelArtikel.getArtikelDto()
													.getIId(),
											tpRechnung.getRechnungDto()
													.getLagerIId(),
											((PanelPositionenArtikelVerkaufSNR) panelArtikel).wtfSerienchargennummer
													.getSeriennummern(),
											bdBenoetigtVomLager);
							if (!bDiePositionSpeichern) {
								return;
							}
						}
					}
					if (getPositionDto().getNMenge().equals(new BigDecimal(0))) {
						boolean bAnswer = (DialogFactory.showMeldung(
								"Wirklich Menge 0?",
								LPMain.getTextRespectUISPr("lp.frage"),
								javax.swing.JOptionPane.YES_NO_OPTION) == javax.swing.JOptionPane.YES_OPTION);
						if (!bAnswer) {
							return;
						}
					}
					if (tpRechnung.getRechnungstyp().equals(
							RechnungFac.RECHNUNGTYP_GUTSCHRIFT)) {
						/**
						 * @todo GS SNR Behandlung PJ 5080
						 */
					} else {
						// Behandlung chargennummerntragender Artikel
						if (Helper.short2boolean(panelArtikel.getArtikelDto()
								.getBChargennrtragend())
								&& panelArtikel.wnfMenge.getBigDecimal()
										.compareTo(new BigDecimal(0)) > 0) {
							// Wenn Menge > 0 ist muss mindestens 1
							// Chargennummer eingetragen sein
							if (((PanelPositionenArtikelVerkaufSNR) panelArtikel).wtfSerienchargennummer
									.getText() == null
									|| ((PanelPositionenArtikelVerkaufSNR) panelArtikel).wtfSerienchargennummer
											.getText().length() == 0) {
								DialogFactory
										.showModalDialog(
												LPMain.getTextRespectUISPr("lp.warning"),
												LPMain.getTextRespectUISPr("lp.hint.chargennummer"));
								bDiePositionSpeichern = false;
							}
						} else
						// Behandlung seriennummerntragender Artikel
						if (Helper.short2boolean(panelArtikel.getArtikelDto()
								.getBSeriennrtragend())
								&& panelArtikel.wnfMenge.getBigDecimal()
										.compareTo(new BigDecimal(0)) > 0) {
							// es muss mindestens 1 SNR eingetragen sein
							if (((PanelPositionenArtikelVerkaufSNR) panelArtikel).wtfSerienchargennummer
									.getText() == null
									|| ((PanelPositionenArtikelVerkaufSNR) panelArtikel).wtfSerienchargennummer
											.getText().length() == 0) {
								DialogFactory
										.showModalDialog(
												LPMain.getTextRespectUISPr("lp.warning"),
												LPMain.getTextRespectUISPr("lp.hint.seriennummer"));
								bDiePositionSpeichern = false;
							} else {
								if (panelArtikel.wnfMenge.getBigDecimal()
										.compareTo(new BigDecimal(0)) > 0) {
									// die Anzahl der Seriennummern bestimmen
									String[] aNummer = Helper
											.erzeugeStringArrayAusString(((PanelPositionenArtikelVerkaufSNR) panelArtikel).wtfSerienchargennummer
													.getText());
									int iAnzahlSnr = aNummer.length;
									if (iAnzahlSnr != panelArtikel.wnfMenge
											.getInteger()) {
										MessageFormat mf = new MessageFormat(
												LPMain.getTextRespectUISPr("lp.hint.seriennummer"));
										mf.setLocale(LPMain.getTheClient()
												.getLocUi());
										Object pattern[] = {
												panelArtikel.wnfMenge
														.getInteger(),
												new Integer(aNummer.length) };
										DialogFactory
												.showModalDialog(
														LPMain.getTextRespectUISPr("lp.warning"),
														mf.format(pattern));
										bDiePositionSpeichern = false;
									}
								}
							}
						}
					}

					// PJ 16475 Bei Verleihartikeln nicht pruefen
					if (Helper.short2boolean(panelArtikel.getArtikelDto()
							.getBVerleih()) == false) {
						// auf Unterpreisigkeit pruefen
						bDiePositionSpeichern = DialogFactory
								.pruefeUnterpreisigkeitDlg(
										getInternalFrame(),
										panelArtikel.getArtikelDto().getIId(),
										tpRechnung.getRechnungDto()
												.getLagerIId(),
										getPositionDto().getNNettoeinzelpreis(),
										new Double(tpRechnung.getRechnungDto()
												.getNKurs().doubleValue()),
										getPositionDto().getNMenge());
					}
				}

				if (bDiePositionSpeichern) {
					bDiePositionSpeichern = getArtikelsetViewController()
							.validateArtikelsetConstraints(getPositionDto());
				}

				if (bDiePositionSpeichern) {
					if (getPositionDto().getPositionsartCNr().equals(
							LocaleFac.POSITIONSART_LIEFERSCHEIN)) {
						// Wenn position ein Lieferschein ist ueberpruefen ob
						// Die Konditionen uebereinstimmen
						LieferscheinDto lieferscheinDto = DelegateFactory
								.getInstance()
								.getLsDelegate()
								.lieferscheinFindByPrimaryKey(
										getPositionDto().getLieferscheinIId());
						RechnungDto rechDto = tpRechnung.getRechnungDto();
						if (!(rechDto.getLieferartIId().equals(lieferscheinDto
								.getLieferartIId()))
								|| !(rechDto.getZahlungszielIId()
										.equals(lieferscheinDto
												.getZahlungszielIId()))
								|| !(rechDto.getSpediteurIId()
										.equals(lieferscheinDto
												.getSpediteurIId()))) {
							DialogFactory
									.showModalDialog(
											LPMain.getTextRespectUISPr("lp.hint"),
											LPMain.getTextRespectUISPr("lp.konditionenenichtgleich"));
						}
						if (!rechDto.getFAllgemeinerRabattsatz().equals(
								lieferscheinDto.getFAllgemeinerRabattsatz())) {
							DialogFactory
									.showModalDialog(
											LPMain.getTextRespectUISPr("lp.hint"),
											LPMain.getTextRespectUISPr("rech.label.allgrabattueberschreiben"));
						}

						ParametermandantDto parameter = (ParametermandantDto) DelegateFactory
								.getInstance()
								.getParameterDelegate()
								.getParametermandant(
										ParameterFac.PARAMETER_LIEFERSCHEIN_UEBERNAHME_NACH_ANSPRECHPARTNER,
										ParameterFac.KATEGORIE_RECHNUNG,
										LPMain.getTheClient().getMandant());

						if ((Boolean) parameter.getCWertAsObject()) {
							Integer iSort = DelegateFactory
									.getInstance()
									.getRechnungDelegate()
									.getISortUmLieferscheinNachAnsprechpartnerEinzusortieren(
											getPositionDto().getRechnungIId(),
											getPositionDto()
													.getLieferscheinIId());
							getPositionDto().setISort(iSort);
						}

					}
					if (getPositionDto().getIId() == null) {
						// Soll die neue Position vor der aktuell selektierten
						// stehen?
						if (bFuegeNeuePositionVorDerSelektiertenEin) {
							Integer iIdAktuellePosition = tpRechnung
									.getSelectedIdPositionen();
							if (iIdAktuellePosition != null) {
								Integer iSortAktuellePosition = DelegateFactory
										.getInstance()
										.getRechnungDelegate()
										.rechnungPositionFindByPrimaryKey(
												iIdAktuellePosition).getISort();
								getPositionDto()
										.setISort(iSortAktuellePosition);
							}
						}

						// wir legen eine neue Position an
						getPositionDto().setPositioniIdArtikelset(
								getArtikelsetViewController()
										.getArtikelSetIIdFuerNeuePosition());

						List<SeriennrChargennrMitMengeDto> snrs = getArtikelsetViewController()
								.handleArtikelsetSeriennummern(
										RechnungFac.RECHNUNGTYP_GUTSCHRIFT.equals(tpRechnung
												.getRechnungstyp()),
										tpRechnung.getRechnungDto()
												.getLagerIId(),
										getPositionDto());

						if (!getArtikelsetViewController()
								.isArtikelsetWithSnrsStoreable(
										getPositionDto(), snrs)) {
							bDiePositionSpeichern = false;
						}

						if (bDiePositionSpeichern) {
							tpRechnung.setRechnungPositionDto(DelegateFactory
									.getInstance()
									.getRechnungDelegate()
									.updateRechnungPosition(
											getPositionDto(),
											tpRechnung.getRechnungDto()
													.getLagerIId(), snrs));
							setKeyWhenDetailPanel(getPositionDto().getIId());
						}
					} else {
						Artikelset artikelset = null;
						if (getPositionDto().getIId() != null
								&& getArtikelsetViewController()
										.isArtikelsetKopfartikel(
												getPositionDto()
														.getArtikelIId())) {
							RechnungPositionDto actualDtos[] = DelegateFactory
									.getInstance()
									.getRechnungDelegate()
									.getArtikelsetForIId(
											getPositionDto().getIId());
							RechnungPositionDto previousKopfRePosDto = DelegateFactory
									.getInstance()
									.getRechnungDelegate()
									.rechnungPositionFindByPrimaryKey(
											getPositionDto().getIId());
							artikelset = new Artikelset(getPositionDto(),
									actualDtos);
							artikelset
									.setPreviousSlipAmount(previousKopfRePosDto
											.getNMenge());
						}

						List<SeriennrChargennrMitMengeDto> snrs = getArtikelsetViewController()
								.handleArtikelsetSeriennummern(
										RechnungFac.RECHNUNGTYP_GUTSCHRIFT.equals(tpRechnung
												.getRechnungstyp()),
										tpRechnung.getRechnungDto()
												.getLagerIId(), artikelset,
										getPositionDto());

						if (!getArtikelsetViewController()
								.isArtikelsetWithSnrsStoreable(
										getPositionDto(), snrs)) {
							bDiePositionSpeichern = false;
						}

						if (bDiePositionSpeichern) {
							DelegateFactory
									.getInstance()
									.getRechnungDelegate()
									.updateRechnungPosition(
											getPositionDto(),
											tpRechnung.getRechnungDto()
													.getLagerIId(), snrs,
											artikelset);
						}
					}
					if (bDiePositionSpeichern) {
						// buttons schalten
						super.eventActionSave(e, false);
						eventYouAreSelected(false);
						// Fehlmengen aufloesen (fuer Gutschriftenpositionen).
						if (getPositionsartCNr().equals(
								LocaleFac.POSITIONSART_IDENT)
								&& tpRechnung.getRechnungstyp().equals(
										RechnungFac.RECHNUNGTYP_GUTSCHRIFT)) {
							String[] sSeriennummernArray = null;
							if (Helper.short2boolean(panelArtikel
									.getArtikelDto().getBChargennrtragend())) {
								sSeriennummernArray = new String[1];
								sSeriennummernArray[0] = ((PanelPositionenArtikelGutschrift) panelArtikel).wtfSerienchargennummer
										.getText();
							} else if (Helper.short2boolean(panelArtikel
									.getArtikelDto().getBSeriennrtragend())) {
								sSeriennummernArray = Helper
										.erzeugeSeriennummernArray(
												((PanelPositionenArtikelGutschrift) panelArtikel).wtfSerienchargennummer
														.getText(),
												panelArtikel.wnfMenge
														.getBigDecimal(), true);
							}
							FehlmengenAufloesen.fehlmengenAufloesen(
									getInternalFrame(), panelArtikel
											.getArtikelDto().getIId(),
									tpRechnung.getRechnungDto().getLagerIId(),
									sSeriennummernArray, panelArtikel.wnfMenge
											.getBigDecimal());
						}
					}
				}

				if (bDiePositionSpeichern) {
					bFuegeNeuePositionVorDerSelektiertenEin = false;
					getArtikelsetViewController()
							.resetArtikelSetIIdFuerNeuePosition();
					setModified(true);
				}
			}
		} finally {
			// bFuegeNeuePositionVorDerSelektiertenEin = false;
			// resetArtikelsetIIdForNewPosition();
		}

		ParametermandantDto parameter = (ParametermandantDto) DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getParametermandant(
						ParameterFac.PARAMETER_MINDESTSTANDSWARNUNG,
						ParameterFac.KATEGORIE_RECHNUNG,
						LPMain.getTheClient().getMandant());

		if ((Boolean) parameter.getCWertAsObject()) {

			wirdLagermindeststandUnterschritten(tpRechnung.getRechnungDto()
					.getTBelegdatum(), getPositionDto().getNMenge(),
					getPositionDto().getArtikelIId());
		}

		setzePositionsartAenderbar(getPositionDto());
		panelArtikel.setzeEinheitAenderbar();
	}

	/**
	 * Eine Position loeschen.
	 * 
	 * @param e
	 *            Ereignis
	 * @param bAdministrateLockKeyI
	 *            flag
	 * @param bNeedNoDeleteI
	 *            boolean
	 * @throws Throwable
	 */
	protected void eventActionDelete(ActionEvent e,
			boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {
		if (((InternalFrameRechnung) getInternalFrame())
				.isUpdateAllowedForRechnungDto(tpRechnung.getRechnungDto())) {

			// PJ17873 Verrechnung des Inserates zuruecknehmen, wenn vorhanden
			InseratDto inseratDto = DelegateFactory
					.getInstance()
					.getInseratDelegate()
					.istIseratAufRechnungspositionVorhanden(
							getPositionDto().getIId());
			if (inseratDto != null) {
				boolean b = DialogFactory
						.showModalJaNeinDialog(
								getInternalFrame(),
								LPMain.getTextRespectUISPr("iv.beziehung.zu.rechnung.vorhanden")
										+ " "
										+ inseratDto.getCNr()
										+ ". "
										+ LPMain.getTextRespectUISPr("iv.beziehung.zu.rechnung.vorhanden2"));
				if (b == true) {
					DelegateFactory
							.getInstance()
							.getInseratDelegate()
							.beziehungZuRechnungspositionAufloesenUndRechnungspositionenLoeschen(
									getPositionDto().getIId());
					super.eventActionDelete(e, false, false);

					ArrayList<RechnungDto> alRe = DelegateFactory
							.getInstance()
							.getInseratDelegate()
							.gibtEsNochWeitereRechnungenFuerdiesesInserat(
									inseratDto.getIId());
					if (alRe != null && alRe.size() > 0) {

						String s="";
						
						for(int i=0;i<alRe.size();i++){
							s+=alRe.get(i).getCNr();
							if(i<alRe.size()-1){
								s+=", ";							}
						}
						
						DialogFactory
								.showModalDialog(
										LPMain.getTextRespectUISPr("lp.info"),
										LPMain.getTextRespectUISPr("iv.info.weitere.rechnungen.vorhanden")+ " "+s);

					}

				} else {
					return;
				}
			} else {

				DelegateFactory.getInstance().getRechnungDelegate()
						.removeRechnungPosition(getPositionDto());
				super.eventActionDelete(e, false, false); // keyWasForLockMe
															// nicht
				// ueberschreiben
			}
		}
	}

	private void dto2Components() throws Throwable {
		// 1. Behandlung der trivialen Positionsarten.
		super.dto2Components(getPositionDto(), tpRechnung.getKundeDto()
				.getPartnerDto().getLocaleCNrKommunikation());
		// 2. Weiter mit den anderen.
		String positionsart = getPositionDto().getPositionsartCNr();

		if (positionsart
				.equalsIgnoreCase(RechnungFac.POSITIONSART_RECHNUNG_IDENT)) {
			// Serien/Chargennummern.
			if (tpRechnung.getRechnungstyp().equals(
					RechnungFac.RECHNUNGTYP_GUTSCHRIFT)) {
				((PanelPositionenArtikelGutschrift) panelArtikel)
						.zeigeSerienchargennummer(false, false);
				((PanelPositionenArtikelGutschrift) panelArtikel).wtfSerienchargennummer
						.setSeriennummern(getPositionDto()
								.getSeriennrChargennrMitMenge(),
								getPositionDto().getArtikelIId(), tpRechnung
										.getRechnungDto().getLagerIId());
			} else {
				((PanelPositionenArtikelVerkaufSNR) panelArtikel)
						.zeigeSerienchargennummer(false, false);
				((PanelPositionenArtikelVerkaufSNR) panelArtikel).wtfSerienchargennummer
						.setSeriennummern(getPositionDto()
								.getSeriennrChargennrMitMenge(),
								getPositionDto().getArtikelIId(), tpRechnung
										.getRechnungDto().getLagerIId());
			}
		} else if (positionsart
				.equalsIgnoreCase(RechnungFac.POSITIONSART_RECHNUNG_HANDEINGABE)) {
			// zzt. nicht rechnungsspezifisches.
		} else if (positionsart
				.equalsIgnoreCase(RechnungFac.POSITIONSART_RECHNUNG_LIEFERSCHEIN)) {
			LieferscheinDto ls = DelegateFactory
					.getInstance()
					.getLsDelegate()
					.lieferscheinFindByPrimaryKey(
							getPositionDto().getLieferscheinIId());
			panelLieferschein.setLieferscheinDto(ls);
		}
		// das Lager des Lieferscheins und Lagerstand des aktuellen Artikel
		String lagerinfo = DelegateFactory
				.getInstance()
				.getLagerDelegate()
				.lagerFindByPrimaryKey(
						tpRechnung.getRechnungDto().getLagerIId()).getCNr();
		setStatusbarPersonalIIdAendern(tpRechnung.getRechnungDto()
				.getPersonalIIdAendern());
		setStatusbarPersonalIIdAnlegen(tpRechnung.getRechnungDto()
				.getPersonalIIdAnlegen());
		setStatusbarTAendern(tpRechnung.getRechnungDto().getTAendern());
		setStatusbarTAnlegen(tpRechnung.getRechnungDto().getTAnlegen());
		setStatusbarStatusCNr(tpRechnung.getRechnungDto().getStatusCNr());
		setStatusbarSpalte5(lagerinfo);

		// Auftrag anzeigen
		if (getPositionDto().getAuftragpositionIId() != null) {
			AuftragpositionDto abPos = DelegateFactory
					.getInstance()
					.getAuftragpositionDelegate()
					.auftragpositionFindByPrimaryKey(
							getPositionDto().getAuftragpositionIId());
			AuftragDto auftragDto = DelegateFactory.getInstance()
					.getAuftragDelegate()
					.auftragFindByPrimaryKey(abPos.getBelegIId());
			wtfAuftragNummer.setText(auftragDto.getCNr());
			wtfAuftragProjekt.setText(auftragDto.getCBezProjektbezeichnung());
			wtfAuftragBestellnummer.setText(auftragDto.getCBestellnummer());
		} else {
			wtfAuftragNummer.setText(null);
			wtfAuftragProjekt.setText(null);
			wtfAuftragBestellnummer.setText(null);
		}
	}

	/**
	 * Alle Positionsdaten aus dem Panel sammeln.
	 * 
	 * @throws Throwable
	 */
	private void components2Dto() throws Throwable {
		// 1. Behandlung der trivialen Positionsarten.
		super.components2Dto(getPositionDto(), tpRechnung.getKundeDto()
				.getPartnerDto().getLocaleCNrKommunikation(), tpRechnung
				.getRechnungDto().getIId());
		// 2. Weiter mit den anderen.

		/**
		 * @todo eine checkbox fuer bdrucken PJ 5088
		 */
		getPositionDto().setBDrucken(Helper.boolean2Short(true));
		getPositionDto()
				.setBRabattsatzuebersteuert(Helper.boolean2Short(false));
		getPositionDto().setBMwstsatzuebersteuert(Helper.boolean2Short(false));

		if (getPositionDto().getPositionsartCNr().equalsIgnoreCase(
				RechnungFac.POSITIONSART_RECHNUNG_IDENT)) {

			if (Helper.short2boolean(panelArtikel.getArtikelDto()
					.getBSeriennrtragend())
					|| Helper.short2boolean(panelArtikel.getArtikelDto()
							.getBChargennrtragend())) {

				// Serien/Chargennummern.
				if (tpRechnung.getRechnungstyp().equals(
						RechnungFac.RECHNUNGTYP_GUTSCHRIFT)) {
					getPositionDto()
							.setSeriennrChargennrMitMenge(
									((PanelPositionenArtikelGutschrift) panelArtikel).wtfSerienchargennummer
											.getSeriennummern());
				} else {
					getPositionDto()
							.setSeriennrChargennrMitMenge(
									((PanelPositionenArtikelVerkaufSNR) panelArtikel).wtfSerienchargennummer
											.getSeriennummern());
				}
			} else {
				getPositionDto().setSeriennrChargennrMitMenge(null);
			}
		} else if (getPositionDto().getPositionsartCNr().equalsIgnoreCase(
				RechnungFac.POSITIONSART_RECHNUNG_HANDEINGABE)) {
			// zzt. nichts rechnungsspezifisches.
		} else if (getPositionDto().getPositionsartCNr().equalsIgnoreCase(
				RechnungFac.POSITIONSART_RECHNUNG_LIEFERSCHEIN)) {
			LieferscheinDto ls = panelLieferschein.getLieferscheinDto();
			getPositionDto().setLieferscheinIId(ls.getIId());

			if (ls.getCBezProjektbezeichnung() != null
					&& ls.getCBezProjektbezeichnung().length() > 40) {
				getPositionDto().setCBez(
						ls.getCBezProjektbezeichnung().substring(0, 40));
			} else {
				getPositionDto().setCBez(ls.getCBezProjektbezeichnung());
			}

			getPositionDto().setNEinzelpreis(
					ls.getNGesamtwertInLieferscheinwaehrung());
			getPositionDto().setNNettoeinzelpreis(
					ls.getNGesamtwertInLieferscheinwaehrung());
			getPositionDto().setNBruttoeinzelpreis(
					ls.getNGesamtwertInLieferscheinwaehrung());
		} else if (getPositionDto().getPositionsartCNr().equalsIgnoreCase(
				RechnungFac.POSITIONSART_RECHNUNG_INTELLIGENTE_ZWISCHENSUMME)) {

			Integer lastZwsPositionIId = panelIntZwischensumme.components2Dto(
					tpRechnung.getRechnungDto(), getPositionDto());
			RechnungPositionDto lastPosition = DelegateFactory.getInstance()
					.getRechnungDelegate()
					.rechnungPositionFindByPrimaryKey(lastZwsPositionIId);
			getPositionDto().setMwstsatzIId(lastPosition.getMwstsatzIId());
		}
	}

	public void setModified(boolean changed) {
		zwController.setChanged(changed);
	}

	protected String getLockMeWer() {
		return HelperClient.LOCKME_RECHNUNG;
	}

	protected void eventItemchanged(EventObject eI) {
		ItemChangedEvent e = (ItemChangedEvent) eI;
		// mehrfachselekt: hier kriegst dus mit
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wcoPositionsart;
	}

	public boolean handleOwnException(ExceptionLP exfc) {
		if (exfc.getICode() == EJBExceptionLP.FEHLER_ZUGEBUCHTES_MATERIAL_BEREITS_VOM_LAGER_ENTNOMMEN) {
			DialogFactory.showModalDialog(LPMain
					.getTextRespectUISPr("lp.error"), LPMain.getInstance()
					.getMsg(exfc));
			return true;
		} else {
			return false;
		}
	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
	}

	public void wnfPauschalposition_focusLost(FocusEvent e) {
		super.wnfPauschalposition_focusLost(e);
		try {
			DelegateFactory
					.getInstance()
					.getRechnungDelegate()
					.berechnePauschalposition(
							wnfPauschalpositionpreis.getBigDecimal(),
							getPositionDto().getIId(),
							getPositionDto().getRechnungIId());
		} catch (ExceptionLP e1) {
		} catch (Throwable e1) {
		}
	}

}
