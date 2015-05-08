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
package com.lp.client.angebot;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.EventObject;

import javax.swing.JComponent;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.HvActionEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelPositionen2;
import com.lp.client.frame.component.PanelPositionenArtikelVerkauf;
import com.lp.client.frame.component.PositionNumberHelperAngebot;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPButtonAction;
import com.lp.client.pc.LPMain;
import com.lp.client.pc.PasteBuffer;
import com.lp.server.angebot.service.AngebotServiceFac;
import com.lp.server.angebot.service.AngebotpositionDto;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MwstsatzDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

@SuppressWarnings("static-access")
/**
 * <p>Panel fuer Angebotpositionen.</p>
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 * <p>Erstellungsdatum 12.07.05</p>
 * <p> </p>
 * @author Uli Walch
 * @version $Revision: 1.15 $
 */
public class PanelAngebotPositionen extends PanelPositionen2 {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	/** Cache for convenience. */
	private InternalFrameAngebot intFrame = null;
	/** Cache for convenience. */
	private TabbedPaneAngebot tpAngebot = null;
	/** Cache for convenience. */
	private AngebotpositionDto angebotpositionDto = null;

	private WrapperCheckBox wcbAlternativeArtikel = null;
	private WrapperLabel wlaGestehungspreisArtikel = null;
	private WrapperNumberField wnfGestehungspreisArtikel = null;
	private WrapperLabel wlaWaehrungGestehungspreisArtikel = null;

	private WrapperLabel wlaEinkaufspreisArtikel = null;
	private WrapperNumberField wnfEinkaufspreisArtikel = null;

	private WrapperCheckBox wcbAlternativeHandeingabe = null;
	private WrapperLabel wlaGestehungspreisHandeingabe = null;
	private WrapperNumberField wnfGestehungspreisHandeingabe = null;
	private WrapperLabel wlaWaehrungGestehungspreisHandeingabe = null;

	public PanelAngebotPositionen(InternalFrame internalFrame,
			String add2TitleI, Object key) throws Throwable {
		super(internalFrame, add2TitleI, key,
				PanelPositionen2.TYP_PANELPREISEINGABE_ARTIKELVERKAUF);

		intFrame = (InternalFrameAngebot) internalFrame;
		tpAngebot = intFrame.getTabbedPaneAngebot();

		zwController.setPositionNumberHelper(new PositionNumberHelperAngebot());
		zwController.setBelegDto(tpAngebot.getAngebotDto());

		jbInit();
		initComponents();
		initPanel();
	}

	private void jbInit() throws Throwable {
		// wegen Dialogauswahl auf FLR events hoeren
		getInternalFrame().addItemChangedListener(this);

		// braucht nur refresh, save und aendern
		resetToolsPanel();

		// zusaetzliche buttons
		String[] aWhichButtonIUse = { ACTION_UPDATE, ACTION_SAVE,
				ACTION_DELETE, ACTION_DISCARD, ACTION_PRINT, ACTION_TEXT };

		enableToolsPanelButtons(aWhichButtonIUse);
		// Statusbar an den unteren Rand des Panels haengen
		iZeile++;
		add(getPanelStatusbar(), new GridBagConstraints(0, iZeile, 1, 1, 1.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));

		// die Positionsarten Handeingabe und Ident, Agstkl bekommen ein
		// zusaetzliches Feld
		wcbAlternativeArtikel = new WrapperCheckBox(LPMain.getInstance()
				.getTextRespectUISPr("angb.alternative"));
		wcbAlternativeArtikel.setMnemonic('A');

		wlaGestehungspreisArtikel = new WrapperLabel(
				LPMain.getTextRespectUISPr("lp.gestehungspreis"));
		wnfGestehungspreisArtikel = new WrapperNumberField();
		wnfGestehungspreisArtikel.setActivatable(false);
		wnfGestehungspreisArtikel.setFractionDigits(Defaults.getInstance()
				.getIUINachkommastellenPreiseVK());
		wlaWaehrungGestehungspreisArtikel = new WrapperLabel();

		wlaEinkaufspreisArtikel = new WrapperLabel(
				LPMain.getTextRespectUISPr("lp.einkaufspreis"));
		wnfEinkaufspreisArtikel = new WrapperNumberField();
		wnfEinkaufspreisArtikel.setFractionDigits(Defaults.getInstance()
				.getIUINachkommastellenPreiseVK());

		panelArtikel.add(wcbAlternativeArtikel, new GridBagConstraints(1, 9, 1,
				1, 0.0, 0.0, GridBagConstraints.NORTH,
				GridBagConstraints.HORIZONTAL, new Insets(10, 0, 2, 2), 0, 0));

		ParametermandantDto parameterDto = DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getMandantparameter(LPMain.getTheClient().getMandant(),
						ParameterFac.KATEGORIE_ANGEBOT,
						ParameterFac.PARAMETER_LIEFERANT_ANGEBEN);
		boolean bLieferantAngeben = (Boolean) parameterDto.getCWertAsObject();
		if (bLieferantAngeben == true) {
			panelArtikel.add(wlaEinkaufspreisArtikel, new GridBagConstraints(3,
					9, 3, 1, 0.0, 0.0, GridBagConstraints.NORTH,
					GridBagConstraints.HORIZONTAL, new Insets(10, 2, 2, 2), 0,
					0));
			panelArtikel.add(wnfEinkaufspreisArtikel, new GridBagConstraints(6,
					9, 1, 1, 0.0, 0.0, GridBagConstraints.NORTH,
					GridBagConstraints.HORIZONTAL, new Insets(10, 2, 2, 2), 0,
					0));
		} else {
			panelArtikel.add(wlaGestehungspreisArtikel, new GridBagConstraints(
					3, 9, 3, 1, 0.0, 0.0, GridBagConstraints.NORTH,
					GridBagConstraints.HORIZONTAL, new Insets(10, 2, 2, 2), 0,
					0));
			panelArtikel.add(wnfGestehungspreisArtikel, new GridBagConstraints(
					6, 9, 1, 1, 0.0, 0.0, GridBagConstraints.NORTH,
					GridBagConstraints.HORIZONTAL, new Insets(10, 2, 2, 2), 0,
					0));
		}

		panelArtikel.add(wlaWaehrungGestehungspreisArtikel,
				new GridBagConstraints(7, 9, 1, 1, 0.0, 0.0,
						GridBagConstraints.NORTH,
						GridBagConstraints.HORIZONTAL, new Insets(10, 2, 2, 2),
						0, 0));

		wcbAlternativeHandeingabe = new WrapperCheckBox(
				LPMain.getTextRespectUISPr("angb.alternative"));
		wcbAlternativeHandeingabe.setMnemonic('A');

		wlaGestehungspreisHandeingabe = new WrapperLabel(
				LPMain.getTextRespectUISPr("lp.gestehungspreis"));
		wnfGestehungspreisHandeingabe = new WrapperNumberField();
		wnfGestehungspreisHandeingabe.setActivatable(true);
		wnfGestehungspreisHandeingabe.setFractionDigits(Defaults.getInstance()
				.getIUINachkommastellenPreiseVK());
		wlaWaehrungGestehungspreisHandeingabe = new WrapperLabel();

		panelHandeingabe.add(wcbAlternativeHandeingabe, new GridBagConstraints(
				1, 8, 1, 1, 0.0, 0.0, GridBagConstraints.NORTH,
				GridBagConstraints.HORIZONTAL, new Insets(10, 0, 2, 2), 0, 0));
		panelHandeingabe.add(wlaGestehungspreisHandeingabe,
				new GridBagConstraints(3, 8, 3, 1, 0.0, 0.0,
						GridBagConstraints.NORTH,
						GridBagConstraints.HORIZONTAL, new Insets(10, 2, 2, 2),
						0, 0));
		panelHandeingabe.add(wnfGestehungspreisHandeingabe,
				new GridBagConstraints(6, 8, 1, 1, 0.0, 0.0,
						GridBagConstraints.NORTH,
						GridBagConstraints.HORIZONTAL, new Insets(10, 2, 2, 2),
						0, 0));
		panelHandeingabe.add(wlaWaehrungGestehungspreisHandeingabe,
				new GridBagConstraints(7, 8, 1, 1, 0.0, 0.0,
						GridBagConstraints.NORTH,
						GridBagConstraints.HORIZONTAL, new Insets(10, 2, 2, 2),
						0, 0));

		panelHandeingabe.setVisibleZeileLieferterminposition(false);
	}

	private void initPanel() throws Throwable {
		// combobox Positionen in der UI Sprache des Benutzers fuellen
		setPositionsarten(DelegateFactory.getInstance()
				.getAngebotServiceDelegate()
				.getAngebotpositionart(LPMain.getTheClient().getLocUi()));
		// Die Gestehungspreise sind nur mit Recht "dar EK preise sehen"
		// sichtbar
		wlaGestehungspreisArtikel
				.setVisible(getInternalFrame().bRechtDarfPreiseSehenEinkauf);
		wnfGestehungspreisArtikel
				.setVisible(getInternalFrame().bRechtDarfPreiseSehenEinkauf);
		wlaEinkaufspreisArtikel
				.setVisible(getInternalFrame().bRechtDarfPreiseSehenEinkauf);
		wnfEinkaufspreisArtikel
				.setVisible(getInternalFrame().bRechtDarfPreiseSehenEinkauf);
		wlaWaehrungGestehungspreisArtikel
				.setVisible(getInternalFrame().bRechtDarfPreiseSehenEinkauf);
		wlaGestehungspreisHandeingabe
				.setVisible(getInternalFrame().bRechtDarfPreiseSehenEinkauf);
		wnfGestehungspreisHandeingabe
				.setVisible(getInternalFrame().bRechtDarfPreiseSehenEinkauf);
		wlaWaehrungGestehungspreisHandeingabe
				.setVisible(getInternalFrame().bRechtDarfPreiseSehenEinkauf);
	}

	protected void setDefaults() throws Throwable {
		angebotpositionDto = new AngebotpositionDto();

		angebotpositionDto.setBMwstsatzuebersteuert(new Short((short) 0));
		angebotpositionDto.setBRabattsatzuebersteuert(new Short((short) 0));
		angebotpositionDto.setBNettopreisuebersteuert(new Short((short) 0));

		leereAlleFelder(this);

		// default positionsart ist ident
		wcoPositionsart
				.setKeyOfSelectedItem(AngebotServiceFac.ANGEBOTPOSITIONART_IDENT);

		super.setDefaults();

		if (tpAngebot.getAngebotDto() != null
				&& tpAngebot.getAngebotDto().getIId() != null) {
			// alle auftragabhaengigen Defaults in den verschiedenen
			// PanelPositionen setzen
			panelHandeingabe.setWaehrungCNr(tpAngebot.getAngebotDto()
					.getWaehrungCNr());
			panelArtikel.setWaehrungCNr(tpAngebot.getAngebotDto()
					.getWaehrungCNr());
			panelAGStueckliste.setWaehrungCNr(tpAngebot.getAngebotDto()
					.getWaehrungCNr());

			panelAGStueckliste.setBelegwaehrungCNr(tpAngebot.getAngebotDto()
					.getWaehrungCNr());

			// im PanelArtikel alles fuer die VKPF vorbereiten
			((PanelPositionenArtikelVerkauf) panelArtikel)
					.setKundeDto(tpAngebot.getKundeDto());
			((PanelPositionenArtikelVerkauf) panelArtikel)
					.setWechselkurs(tpAngebot.getAngebotDto()
							.getFWechselkursmandantwaehrungzubelegwaehrung());
			((PanelPositionenArtikelVerkauf) panelArtikel)
					.setCNrWaehrung(tpAngebot.getAngebotDto().getWaehrungCNr());
			((PanelPositionenArtikelVerkauf) panelArtikel)
					.setGueltigkeitsdatumArtikeleinzelverkaufspreis(Helper
							.extractDate(tpAngebot.getAngebotDto()
									.getTBelegdatum()));
			((PanelPositionenArtikelVerkauf) panelArtikel)
					.setIIdLager(DelegateFactory.getInstance()
							.getLagerDelegate().getHauptlagerDesMandanten()
							.getIId());

		}

		wlaWaehrungGestehungspreisArtikel.setText(tpAngebot.getAngebotDto()
				.getWaehrungCNr());
		wnfGestehungspreisArtikel.setBigDecimal(new BigDecimal(0));

		wnfGestehungspreisArtikel.setBigDecimal(null);

		wlaWaehrungGestehungspreisHandeingabe.setText(tpAngebot.getAngebotDto()
				.getWaehrungCNr());
		wnfGestehungspreisHandeingabe.setBigDecimal(new BigDecimal(0));

		panelAGStueckliste.getWlaWaehrungKalkulatorischerWertAgstkl().setText(
				tpAngebot.getAngebotDto().getWaehrungCNr());
		panelAGStueckliste.getWnfKalkulatorischerWertAgstkl().setBigDecimal(
				new BigDecimal(0));

		// Wenn der Vorschlagswert fuer den Mwstsatz aus dem Belegkunden kommt
		if (!panelHandeingabe.getBDefaultMwstsatzAusArtikel()) {
			if (tpAngebot.getKundeDto() != null
					&& tpAngebot.getKundeDto().getIId() != null) {
				// Aktuellen MWST-Satz uebersetzen.
				MwstsatzDto mwstsatzDtoAktuell = DelegateFactory
						.getInstance()
						.getMandantDelegate()
						.mwstsatzFindByMwstsatzbezIIdAktuellster(
								tpAngebot.getKundeDto().getMwstsatzbezIId());
				panelHandeingabe.wcoMwstsatz
						.setKeyOfSelectedItem(mwstsatzDtoAktuell.getIId());
			}
		}

		if (!panelAGStueckliste.getBDefaultMwstsatzAusArtikel()) {
			if (tpAngebot.getKundeDto() != null
					&& tpAngebot.getKundeDto().getIId() != null) {
				// Aktuellen MWST-Satz uebersetzen.
				MwstsatzDto mwstsatzDtoAktuell = DelegateFactory
						.getInstance()
						.getMandantDelegate()
						.mwstsatzFindByMwstsatzbezIIdAktuellster(
								tpAngebot.getKundeDto().getMwstsatzbezIId());
				panelAGStueckliste.wcoMwstsatz
						.setKeyOfSelectedItem(mwstsatzDtoAktuell.getIId());
			}
		}

		wcbAlternativeArtikel.setSelected(false);
		wcbAlternativeHandeingabe.setSelected(false);
		panelArtikel.wtfZusatzbezeichnung.setActivatable(true);
	}

	public AngebotpositionDto getAngebotpositionDto() {
		return angebotpositionDto;
	}

	public void eventActionNew(EventObject eventObject, boolean bLockMeI,
			boolean bNeedNoNewI) throws Throwable {
		if (tpAngebot.istAktualisierenAngebotErlaubt()) {
			super.eventActionNew(eventObject, true, false); // LockMeForNew
															// setzen

			setDefaults();

			// die neue Position soll vor der aktuell selektierten eingefuegt
			// werden
			if (((ItemChangedEvent) eventObject).getID() == ItemChangedEvent.ACTION_POSITION_VORPOSITIONEINFUEGEN) {
				// Dieses Flag gibt an, ob die neue Position vor der aktuellen
				// eingefuegt werden soll
				bFuegeNeuePositionVorDerSelektiertenEin = true;
			}
		} else {
			tpAngebot.getAngebotPositionenTop().updateButtons(
					tpAngebot.getAngebotPositionenBottom()
							.getLockedstateDetailMainKey());
		}
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
		((PanelPositionenArtikelVerkauf) panelArtikel).wbuPreisauswahl
				.setEnabled(false);
	}

	protected void eventActionUpdate(ActionEvent aE, boolean bNeedNoUpdateI)
			throws Throwable {

		refreshMyComponents();

		if (tpAngebot.istAktualisierenAngebotErlaubt()) {
			super.eventActionUpdate(aE, false);

			panelArtikel.setArtikelEingabefelderEditable(true);
			if (getInternalFrame().bRechtDarfPreiseAendernVerkauf == true) {
				((PanelPositionenArtikelVerkauf) panelArtikel).wbuPreisauswahl
						.setEnabled(true);
			} else {
				((PanelPositionenArtikelVerkauf) panelArtikel).wbuPreisauswahl
						.setEnabled(false);
			}

			setzePositionsartAenderbar(angebotpositionDto);
			panelArtikel.setzeEinheitAenderbar();
		}
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {
		super.eventYouAreSelected(false);
		setTBelegdatumMwstsatz(tpAngebot.getAngebotDto().getTBelegdatum());
		// zuerst alles zuruecksetzen, ausloeser war ev. ein discard
		setDefaults();
		// position neu einlesen, ausloeser war ev. ein refresh oder discard
		Object oKey = getKeyWhenDetailPanel();

		if (oKey != null && !oKey.equals(LPMain.getLockMeForNew())) {
			// Position neu einlesen.
			angebotpositionDto = DelegateFactory.getInstance()
					.getAngebotpositionDelegate()
					.angebotpositionFindByPrimaryKey((Integer) oKey);
			// das Angebot fuer die Statusbar neu einlesen
			tpAngebot.setAngebotDto(DelegateFactory.getInstance()
					.getAngebotDelegate()
					.angebotFindByPrimaryKey(angebotpositionDto.getBelegIId()));
			dto2Components();
		} else {
			panelArtikel.setLetzteArtikelCNr();
		}

		/**
		 * @todo titel nicht hier setzen
		 */
		tpAngebot.setTitleAngebot(LPMain
				.getTextRespectUISPr("angb.panel.positionen"));

		// wenn das Angebot gerade von mir gelockt ist, die Artikeleingabefelder
		// freischalten
		if (getLockedstateDetailMainKey().getIState() == PanelBasis.LOCK_IS_LOCKED_BY_ME) {
			panelArtikel.setArtikelEingabefelderEditable(true);
			((PanelPositionenArtikelVerkauf) panelArtikel).wbuPreisauswahl
					.setEnabled(true);
		}

		setzePositionsartAenderbar(angebotpositionDto);
		panelArtikel.setzeEinheitAenderbar();

		setModified(false);

		aktualisiereStatusbar();
		refreshMyComponents();
	}

	public AngebotpositionDto getPositionDto() {
		return angebotpositionDto;
	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {
		boolean bPositionFuerZugehoerigenArtikelAnlegen = false;

		if (wirdPreisvorschlagsdialogGeradeAngezeit()) {
			return;
		}

		try {

			zwController.setBelegDto(tpAngebot.getAngebotDto());
			zwController.setBelegPositionDto(getPositionDto());
			zwController
					.setSelectedPositionIId(bFuegeNeuePositionVorDerSelektiertenEin ? tpAngebot
							.getSelectedIdPositionen() : null);

			calculateFields();

			if (allMandatoryFieldsSetDlg()) {
				// vkhint: Wenn ein guenstigerer VK-Preis moeglich waere und der
				// Mandantparameter == true,
				// dann den Benutzer darauf hinweisen
				((PanelPositionenArtikelVerkauf) panelArtikel).checkVkp();

				components2Dto();

				boolean bDiePositionSpeichern = true;

				bDiePositionSpeichern = HelperClient
						.checkNumberFormat(panelArtikel.wnfNettopreis
								.getBigDecimal());

				if (bDiePositionSpeichern) {
					bDiePositionSpeichern = HelperClient
							.checkNumberFormat(panelArtikel.wnfBruttopreis
									.getBigDecimal());
				}

				if (bDiePositionSpeichern) {
					if (angebotpositionDto.getPositionsartCNr()
							.equalsIgnoreCase(
									AngebotServiceFac.ANGEBOTPOSITIONART_IDENT)) {
						// auf Unterpreisigkeit pruefen
						bDiePositionSpeichern = DialogFactory
								.pruefeUnterpreisigkeitDlg(
										getInternalFrame(),
										panelArtikel.getArtikelDto().getIId(),
										null,
										angebotpositionDto
												.getNNettoeinzelpreis(),
										tpAngebot
												.getAngebotDto()
												.getFWechselkursmandantwaehrungzubelegwaehrung(),
										angebotpositionDto.getNMenge());

						/*
						 * entfernt da doppelt gepr&uuml;ft und nochdazu am
						 * Client! AD if (bDiePositionSpeichern) {
						 * bDiePositionSpeichern =
						 * speichernNachPruefungAufMindestDB(); }
						 */
						if (bDiePositionSpeichern) {
							// Soll eine Position mit einem eventuellen
							// zugehoerigen Artikel angelegt werden?
							if (angebotpositionDto.getIId() == null) {
								bPositionFuerZugehoerigenArtikelAnlegen = DialogFactory
										.pruefeZugehoerigenArtikelAnlegenDlg(panelArtikel
												.getArtikelDto());
							}
						}
					}
				}

				if (bDiePositionSpeichern) {
					bDiePositionSpeichern = getArtikelsetViewController()
							.validateArtikelsetConstraints(angebotpositionDto);
				}

				if (bDiePositionSpeichern) {
					if (angebotpositionDto.getIId() == null) {
						// Soll die neue Position vor der aktuell selektierten
						// stehen?
						if (bFuegeNeuePositionVorDerSelektiertenEin) {
							Integer iIdAktuellePosition = (Integer) tpAngebot
									.getAngebotPositionenTop().getSelectedId();

							// erstepos: 0 die erste Position steht an der
							// Stelle 1
							Integer iSortAktuellePosition = new Integer(1);

							// erstepos: 1 die erste Position steht an der
							// Stelle 1
							if (iIdAktuellePosition != null) {
								iSortAktuellePosition = DelegateFactory
										.getInstance()
										.getAngebotpositionDelegate()
										.angebotpositionFindByPrimaryKey(
												iIdAktuellePosition).getISort();

								// Die bestehenden Positionen muessen Platz fuer
								// die neue schaffen
								DelegateFactory
										.getInstance()
										.getAngebotpositionDelegate()
										.sortierungAnpassenBeiEinfuegenEinerPositionVorPosition(
												tpAngebot.getAngebotDto()
														.getIId(),
												iSortAktuellePosition
														.intValue());
							}

							// Die neue Position wird an frei gemachte Position
							// gesetzt
							angebotpositionDto.setISort(iSortAktuellePosition);
						}

						// wir legen eine neue Position an
						angebotpositionDto
								.setPositioniIdArtikelset(getArtikelsetViewController()
										.getArtikelSetIIdFuerNeuePosition());
						Integer pkPosition = DelegateFactory.getInstance()
								.getAngebotpositionDelegate()
								.createAngebotposition(angebotpositionDto);

						angebotpositionDto = DelegateFactory.getInstance()
								.getAngebotpositionDelegate()
								.angebotpositionFindByPrimaryKey(pkPosition);

						setKeyWhenDetailPanel(pkPosition);
					} else {
						DelegateFactory.getInstance()
								.getAngebotpositionDelegate()
								.updateAngebotposition(angebotpositionDto);
					}

					// SP2140 Korrekturbetraag zuruecksetzen
					DelegateFactory
							.getInstance()
							.getAngebotDelegate()
							.korrekturbetragZuruecknehmen(
									angebotpositionDto.getBelegIId());

					// buttons schalten
					super.eventActionSave(e, false);

					eventYouAreSelected(false);

					if (bDiePositionSpeichern) {
						bFuegeNeuePositionVorDerSelektiertenEin = false;
						getArtikelsetViewController()
								.resetArtikelSetIIdFuerNeuePosition();
						setModified(true);
					}
				}
			}
		} finally {
			// per Default wird eine neue Position ans Ende der Liste gesetzt
			// bFuegeNeuePositionVorDerSelektiertenEin = false;
		}

		// wenn eine Position fuer einen zugehoerigen Artikel angelegt werden
		// soll,
		// dann muss die Eingabe fuer den zugehoerigen Artikel geoeffnet werden
		if (bPositionFuerZugehoerigenArtikelAnlegen) {
			ArtikelDto artikelDtoZugehoerig = DelegateFactory
					.getInstance()
					.getArtikelDelegate()
					.artikelFindByPrimaryKey(
							panelArtikel.getArtikelDto()
									.getArtikelIIdZugehoerig());
			BigDecimal nMengeZugehoerig = angebotpositionDto.getNMenge();

			ItemChangedEvent ice = new ItemChangedEvent(this,
					ItemChangedEvent.ACTION_NEW);
			tpAngebot.getAngebotPositionenBottom().eventActionNew(ice, true,
					false);
			tpAngebot.getAngebotPositionenBottom().eventYouAreSelected(false);

			tpAngebot.getAngebotPositionenTop().updateButtons(
					tpAngebot.getAngebotPositionenBottom()
							.getLockedstateDetailMainKey());

			panelArtikel.setArtikelDto(artikelDtoZugehoerig);
			panelArtikel.artikelDto2components();

			panelArtikel.wnfMenge.setBigDecimal(nMengeZugehoerig);

			panelArtikel.setArtikelEingabefelderEditable(true);
			((PanelPositionenArtikelVerkauf) panelArtikel).wbuPreisauswahl
					.setEnabled(true);

			setzePositionsartAenderbar(angebotpositionDto);
			panelArtikel.setzeEinheitAenderbar();
		}
	}

	protected void eventActionDelete(ActionEvent e,
			boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {
		if (tpAngebot.istAktualisierenAngebotErlaubt()) {
			DelegateFactory.getInstance().getAngebotpositionDelegate()
					.removeAngebotposition(angebotpositionDto);
			DelegateFactory
					.getInstance()
					.getAngebotDelegate()
					.korrekturbetragZuruecknehmen(
							angebotpositionDto.getBelegIId());

			this.setKeyWhenDetailPanel(null);
			super.eventActionDelete(e, false, false); // keyWasForLockMe nicht
														// ueberschreiben
		}
	}

	private void dto2Components() throws Throwable {
		// 1. Behandlung der trivialen Positionsarten.
		super.dto2Components(angebotpositionDto, tpAngebot.getKundeDto()
				.getPartnerDto().getLocaleCNrKommunikation());
		// 2. Weiter mit den anderen.
		String positionsart = angebotpositionDto.getPositionsartCNr();

		if (positionsart
				.equalsIgnoreCase(AngebotServiceFac.ANGEBOTPOSITIONART_IDENT)) {
			// Angebotsspezifisches.
			wcbAlternativeArtikel.setSelected(Helper
					.short2boolean(angebotpositionDto.getBAlternative()));
			wnfGestehungspreisArtikel.setBigDecimal(angebotpositionDto
					.getNGestehungspreis());
			wnfEinkaufspreisArtikel.setBigDecimal(angebotpositionDto
					.getNEinkaufpreis());
		} else if (positionsart
				.equalsIgnoreCase(AngebotServiceFac.ANGEBOTPOSITIONART_HANDEINGABE)) {
			// Angebotsspezifisches.
			wcbAlternativeHandeingabe.setSelected(Helper
					.short2boolean(angebotpositionDto.getBAlternative()));
			wnfGestehungspreisHandeingabe.setBigDecimal(angebotpositionDto
					.getNGestehungspreis());

		} else if (positionsart
				.equalsIgnoreCase(AngebotServiceFac.ANGEBOTPOSITIONART_AGSTUECKLISTE)) {
			panelAGStueckliste.agstklDto = DelegateFactory.getInstance()
					.getAngebotstklDelegate()
					.agstklFindByPrimaryKey(angebotpositionDto.getAgstklIId());

			panelAGStueckliste.wtfAgstkl.setText(panelAGStueckliste.agstklDto
					.getCNr());
			panelAGStueckliste.wtfBezeichnungAgstkl
					.setText(panelAGStueckliste.agstklDto.getCBez());
			panelAGStueckliste.wbuAgstklGoto.setOKey(panelAGStueckliste.agstklDto.getIId());

			panelAGStueckliste.wnfMenge.setBigDecimal(angebotpositionDto
					.getNMenge());
			panelAGStueckliste.wcoEinheit
					.setKeyOfSelectedItem(angebotpositionDto.getEinheitCNr());
			panelAGStueckliste.getWnfRabattsatz().setDouble(
					angebotpositionDto.getFRabattsatz());
			panelAGStueckliste.wcoMwstsatz
					.setKeyOfSelectedItem(angebotpositionDto.getMwstsatzIId());
			panelAGStueckliste.wnfEinzelpreis.setBigDecimal(angebotpositionDto
					.getNEinzelpreis());
			panelAGStueckliste.wnfRabattsumme.setBigDecimal(angebotpositionDto
					.getNRabattbetrag());

			// den Zusatzrabattsatz setzen
			if (angebotpositionDto.getFZusatzrabattsatz() != null) {
				panelAGStueckliste.wnfZusatzrabattsatz
						.setDouble(angebotpositionDto.getFZusatzrabattsatz());

				BigDecimal nZusatzrabattsumme = angebotpositionDto
						.getNEinzelpreis()
						.subtract(angebotpositionDto.getNRabattbetrag())
						.multiply(
								new BigDecimal(angebotpositionDto
										.getFZusatzrabattsatz().doubleValue()))
						.movePointLeft(2);

				panelAGStueckliste.wnfZusatzrabattsumme
						.setBigDecimal(nZusatzrabattsumme);
			}

			panelAGStueckliste.wnfNettopreis.setBigDecimal(angebotpositionDto
					.getNNettoeinzelpreis());
			panelAGStueckliste.wnfNettopreis.getWrbFixNumber().setSelected(
					Helper.short2boolean(angebotpositionDto
							.getBNettopreisuebersteuert()));
			panelAGStueckliste.wnfRabattsumme.getWrbFixNumber().setSelected(
					Helper.short2boolean(angebotpositionDto
							.getBRabattsatzuebersteuert()));

			BigDecimal nMwstsumme = angebotpositionDto.getNNettoeinzelpreis()
					.multiply(
							new BigDecimal(
									DelegateFactory
											.getInstance()
											.getMandantDelegate()
											.mwstsatzFindByPrimaryKey(
													angebotpositionDto
															.getMwstsatzIId())
											.getFMwstsatz().doubleValue())
									.movePointLeft(2));

			panelAGStueckliste.wnfMwstsumme.setBigDecimal(nMwstsumme);
			panelAGStueckliste.wnfBruttopreis.setBigDecimal(angebotpositionDto
					.getNBruttoeinzelpreis());

			panelAGStueckliste.getWcbAlternativeAgstkl().setSelected(
					Helper.short2boolean(angebotpositionDto.getBAlternative()));
			panelAGStueckliste.getWnfKalkulatorischerWertAgstkl()
					.setBigDecimal(
							angebotpositionDto
									.getNGesamtwertagstklinangebotswaehrung());
		}

		// die Werte des Angebots anzeigen
		BigDecimal bdAngebotswertinanfragewaehrung = tpAngebot.getAngebotDto()
				.getNGesamtwertinbelegwaehrung();

		if (bdAngebotswertinanfragewaehrung == null
				|| tpAngebot.getAngebotDto().getStatusCNr()
						.equals(AngebotServiceFac.ANGEBOTSTATUS_STORNIERT)) {
			bdAngebotswertinanfragewaehrung = DelegateFactory
					.getInstance()
					.getAngebotDelegate()
					.berechneNettowertGesamt(tpAngebot.getAngebotDto().getIId());

			if (tpAngebot.getAngebotDto().getNKorrekturbetrag() != null) {
				bdAngebotswertinanfragewaehrung = bdAngebotswertinanfragewaehrung
						.add(tpAngebot.getAngebotDto().getNKorrekturbetrag());
			}

		}
		String sGesamtbetrag = Helper.formatZahl(
				bdAngebotswertinanfragewaehrung, 2, LPMain.getInstance()
						.getUISprLocale());
		sGesamtbetrag = sGesamtbetrag + " "
				+ tpAngebot.getAngebotDto().getWaehrungCNr();
		setStatusbarSpalte5(sGesamtbetrag);
	}

	private void components2Dto() throws Throwable {
		// 1. Behandlung der trivialen Positionsarten.
		super.components2Dto(angebotpositionDto, tpAngebot.getKundeDto()
				.getPartnerDto().getLocaleCNrKommunikation(), tpAngebot
				.getAngebotDto().getIId());
		// 2. Weiter mit den anderen.

		String positionsart = getPositionsartCNr();

		if (angebotpositionDto.getIId() == null) {
			angebotpositionDto.setBAlternative(new Short((short) 0));
		}

		if (positionsart
				.equalsIgnoreCase(AngebotServiceFac.ANGEBOTPOSITIONART_IDENT)) {
			// hier zusaetzlich gespeicherte Betraege.
			angebotpositionDto.setNRabattbetrag(panelArtikel.wnfRabattsumme
					.getBigDecimal());
			angebotpositionDto.setNMwstbetrag(panelArtikel.wnfMwstsumme
					.getBigDecimal());
			// Angebotsspezifisch.
			angebotpositionDto.setBAlternative(Helper
					.boolean2Short(wcbAlternativeArtikel.isSelected()));
			angebotpositionDto.setNGestehungspreis(wnfGestehungspreisArtikel
					.getBigDecimal());
			angebotpositionDto.setNEinkaufpreis(wnfEinkaufspreisArtikel
					.getBigDecimal());
		} else if (positionsart
				.equalsIgnoreCase(AngebotServiceFac.ANGEBOTPOSITIONART_HANDEINGABE)) {
			// hier zusaetzlich gespeicherte Betraege.
			angebotpositionDto.setNRabattbetrag(panelHandeingabe.wnfRabattsumme
					.getBigDecimal());
			angebotpositionDto.setNMwstbetrag(panelHandeingabe.wnfMwstsumme
					.getBigDecimal());
			// Angebotsspezifisch.
			angebotpositionDto.setBAlternative(Helper
					.boolean2Short(wcbAlternativeHandeingabe.isSelected()));
			angebotpositionDto
					.setNGestehungspreis(wnfGestehungspreisHandeingabe
							.getBigDecimal());
		} else if (positionsart
				.equalsIgnoreCase(AngebotServiceFac.ANGEBOTPOSITIONART_AGSTUECKLISTE)) {
			angebotpositionDto.setAgstklIId(panelAGStueckliste.agstklDto
					.getIId());
			angebotpositionDto.setCBez(panelAGStueckliste.agstklDto.getCBez());

			angebotpositionDto.setNMenge(panelAGStueckliste.wnfMenge
					.getBigDecimal());
			angebotpositionDto
					.setEinheitCNr((String) panelAGStueckliste.wcoEinheit
							.getKeyOfSelectedItem());

			angebotpositionDto.setFRabattsatz(panelAGStueckliste
					.getWnfRabattsatz().getDouble());

			angebotpositionDto.setBNettopreisuebersteuert(Helper
					.boolean2Short(panelAGStueckliste.wnfNettopreis
							.getWrbFixNumber().isSelected()));

			angebotpositionDto.setBRabattsatzuebersteuert(Helper
					.boolean2Short(panelAGStueckliste.wnfRabattsumme
							.getWrbFixNumber().isSelected()));
			angebotpositionDto
					.setMwstsatzIId((Integer) panelAGStueckliste.wcoMwstsatz
							.getKeyOfSelectedItem());
			angebotpositionDto.setBMwstsatzuebersteuert(new Short((short) 0));
			angebotpositionDto
					.setNEinzelpreis(panelAGStueckliste.wnfEinzelpreis
							.getBigDecimal());
			angebotpositionDto
					.setNRabattbetrag(panelAGStueckliste.wnfRabattsumme
							.getBigDecimal());
			angebotpositionDto.setFZusatzrabattsatz(panelAGStueckliste
					.getWnfZusatzrabattsatz().getDouble());
			angebotpositionDto
					.setNNettoeinzelpreis(panelAGStueckliste.wnfNettopreis
							.getBigDecimal());
			angebotpositionDto.setNMwstbetrag(panelAGStueckliste.wnfMwstsumme
					.getBigDecimal());
			angebotpositionDto
					.setNBruttoeinzelpreis(panelAGStueckliste.wnfBruttopreis
							.getBigDecimal());

			angebotpositionDto.setBAlternative(Helper
					.boolean2Short(panelAGStueckliste.getWcbAlternativeAgstkl()
							.isSelected()));
			angebotpositionDto
					.setNGesamtwertagstklinangebotswaehrung(panelAGStueckliste
							.getWnfKalkulatorischerWertAgstkl().getBigDecimal());
		} else if (positionsart
				.equalsIgnoreCase(LocaleFac.POSITIONSART_INTELLIGENTE_ZWISCHENSUMME)) {

			Integer lastZwsPositionIId = panelIntZwischensumme.components2Dto(
					tpAngebot.getAngebotDto(), getPositionDto());
			AngebotpositionDto lastPosition = DelegateFactory.getInstance()
					.getAngebotpositionDelegate()
					.angebotpositionFindByPrimaryKey(lastZwsPositionIId);
			getPositionDto().setMwstsatzIId(lastPosition.getMwstsatzIId());
		}
	}

	private void aktualisiereStatusbar() throws Throwable {
		setStatusbarPersonalIIdAnlegen(tpAngebot.getAngebotDto()
				.getPersonalIIdAnlegen());
		setStatusbarTAnlegen(tpAngebot.getAngebotDto().getTAnlegen());
		setStatusbarPersonalIIdAendern(tpAngebot.getAngebotDto()
				.getPersonalIIdAendern());
		setStatusbarTAendern(tpAngebot.getAngebotDto().getTAendern());
		setStatusbarStatusCNr(tpAngebot.getAngebotStatus());
	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_ANGEBOT;
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wcoPositionsart;
	}

	public boolean handleOwnException(ExceptionLP exfc) {
		boolean bErrorErkannt = true;

		if (exfc.getICode() == EJBExceptionLP.FEHLER_LAGER_HAUPTLAGERDESMANDANTEN_NICHT_ANGELEGT) {
			DialogFactory
					.showModalDialog(
							LPMain.getTextRespectUISPr("lp.error"),
							LPMain.getTextRespectUISPr("auft.mandant.hauptlager_fehlt"));
		} else {
			bErrorErkannt = false;
		}

		return bErrorErkannt;
	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;

		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			if ((panelArtikel.getArtikelDto() != null && panelArtikel
					.getArtikelDto().getIId() != null)
					|| (e.getSource() == panelArtikel.wifArtikelauswahl && panelArtikel.wifArtikelauswahl
							.getArtikelDto() != null)) {
				// den Gestehungspreis des Artikels anzeigen
				BigDecimal nGestehungspreis = DelegateFactory
						.getInstance()
						.getLagerDelegate()
						.getGemittelterGestehungspreisEinesLagers(
								panelArtikel.getArtikelDto().getIId(),
								DelegateFactory.getInstance()
										.getLagerDelegate()
										.getHauptlagerDesMandanten().getIId());
				if (tpAngebot.getAngebotDto() != null)
					// der Gestehungspreis muss in Angebotswaehrung angezeigt
					// werden
					if (tpAngebot.getAngebotDto()
							.getFWechselkursmandantwaehrungzubelegwaehrung() != null) {
						nGestehungspreis = nGestehungspreis
								.multiply(new BigDecimal(
										tpAngebot
												.getAngebotDto()
												.getFWechselkursmandantwaehrungzubelegwaehrung()
												.doubleValue()));
					}
				wnfGestehungspreisArtikel.setBigDecimal(nGestehungspreis);
			}
		}
	}

	protected void eventActionPrint(HvActionEvent e) throws Throwable {

		if (e.isMouseEvent() && e.isRightButtonPressed()) {

			boolean bStatusAngelegt = tpAngebot.getAngebotDto().getStatusCNr().equals(AngebotServiceFac.ANGEBOTSTATUS_ANGELEGT);
			boolean bKonditionen = tpAngebot.pruefeKonditionen();

			if (bStatusAngelegt && bKonditionen) {
				DelegateFactory.getInstance().getAngebotDelegate().berechneAktiviereBelegControlled(
						tpAngebot.getAngebotDto().getIId());
				eventActionRefresh(e, false);
			}
			else if (!bStatusAngelegt) {
				DialogFactory.showModalDialog("Status",
						LPMain.getMessageTextRespectUISPr("status.zustand",
								LPMain.getTextRespectUISPr("angb.angebot"),
								tpAngebot.getAngebotStatus().trim()));
			}


		} else {
			tpAngebot.printAngebot();
		}

		eventYouAreSelected(false);
	}

	/**
	 * Wenn eine Verkaufsposition abgespeichert werden soll, muss
	 * moeglicherweise der Nettogesamtpreis darauf ueberpruft werden, ob er den
	 * MindestDB des Artikels erreicht (Gestehungspreis + Aufschlag MindestDB).
	 * Ob diese Pruefung notwendig ist, bestimmt der Mandantenparameter
	 * DEFAULT_ANGEBOT_PRUEFESTANDARDDBARTIKEL.
	 *
	 * @return boolean true, wenn der Parameter auf 1 steht
	 * @throws java.lang.Throwable
	 *             Ausnahme
	 */
	protected boolean speichernNachPruefungAufMindestDB() throws Throwable {
		boolean bSpeichernNachPruefungAufMindestDB = ((PanelPositionenArtikelVerkauf) panelArtikel)
				.pruefeStandardDBArtikel();

		if (bSpeichernNachPruefungAufMindestDB) {
			BigDecimal nStandardDB = wnfGestehungspreisArtikel
					.getBigDecimal()
					.multiply(
							new BigDecimal(panelArtikel.getArtikelDto()
									.getFMindestdeckungsbeitrag().doubleValue())
									.movePointLeft(2));

			if (wnfGestehungspreisArtikel.getBigDecimal().add(nStandardDB)
					.compareTo(panelArtikel.wnfNettopreis.getBigDecimal()) == 1) {
				if (DialogFactory.showMeldung(LPMain.getInstance()
						.getTextRespectUISPr("lp.warning.mindestdb"), LPMain
						.getInstance().getTextRespectUISPr("lp.warning"),
						javax.swing.JOptionPane.YES_NO_OPTION) == javax.swing.JOptionPane.NO_OPTION) {
					bSpeichernNachPruefungAufMindestDB = false;
				}
			}
		}

		return bSpeichernNachPruefungAufMindestDB;
	}

	private void refreshMyComponents() throws Throwable {

		boolean bEnableEinfuegen = ((LPMain.getInstance().getPasteBuffer()
				.existsPasteBuffer(PasteBuffer.FILE_PASTEBUFFER_POSITIONS)) != null);
		((LPButtonAction) (getHmOfButtons().get(ACTION_EINFUEGEN_LIKE_NEW)))
				.getButton().setEnabled(bEnableEinfuegen);
	}

	public void wnfPauschalposition_focusLost(FocusEvent e) {
		super.wnfPauschalposition_focusLost(e);
		try {
			DelegateFactory
					.getInstance()
					.getAngebotpositionDelegate()
					.berechnePauschalposition(
							wnfPauschalpositionpreis.getBigDecimal(),
							angebotpositionDto.getIId(),
							angebotpositionDto.getBelegIId());
		} catch (ExceptionLP e1) {
		} catch (Throwable e1) {
		}
	}

	public void setModified(boolean changed) {
		zwController.setChanged(changed);
	}
}
