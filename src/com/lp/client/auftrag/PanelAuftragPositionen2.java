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
package com.lp.client.auftrag;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.Calendar;
import java.util.EventObject;

import javax.swing.JComponent;
import javax.swing.JOptionPane;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.LockStateValue;
import com.lp.client.frame.component.HvActionEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelPositionen2;
import com.lp.client.frame.component.PanelPositionenArtikelEingabeSNR;
import com.lp.client.frame.component.PanelPositionenArtikelVerkauf;
import com.lp.client.frame.component.PositionNumberHelperAuftrag;
import com.lp.client.frame.component.WrapperDateField;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperTimeField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPButtonAction;
import com.lp.client.pc.LPMain;
import com.lp.server.angebot.service.AngebotServiceFac;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.auftrag.service.AuftragServiceFac;
import com.lp.server.auftrag.service.AuftragpositionDto;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MandantFac;
import com.lp.server.system.service.MwstsatzDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

@SuppressWarnings("static-access")
/*
 * <p>Basisfenster fuer LP5 Positionen.</p> <p>Copyright Logistik Pur Software
 * GmbH (c) 2004-2008</p> <p>Erstellungsdatum 2005-02-11</p> <p> </p>
 *
 * @author Uli Walch
 *
 * @version $Revision: 1.32 $
 */
public class PanelAuftragPositionen2 extends PanelPositionen2 {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	/** Cache for convenience. */
	private InternalFrameAuftrag intFrame = null;
	/** Cache for convenience. */
	private TabbedPaneAuftrag tpAuftrag = null;
	/** Cache for convenience. */
	private AuftragpositionDto auftragpositionDto = null;

	private String[] cSeriennrn = null;

	private WrapperLabel jLabelLieferterminPosition = null;
	private WrapperDateField wdfLieferterminPosition = null;
	private WrapperTimeField wtfZeit = null;
	private boolean bPositionmiteinkaufpreis = false;

	private WrapperLabel wlaEinkaufpreis = null;
	private WrapperNumberField wnfEinkaufpreis = null;
	private WrapperLabel wlaWaehrungEinkaufpreis = null;

	static final private String ACTION_SPECIAL_POSITIONMANUELLERLEDIGEN = PanelBasis.ALWAYSENABLED
			+ "action_special_positionmanuellerledigen";
	static final private String ACTION_SPECIAL_SRN_ETIKETTDRUCKEN = "action_special_srn_etikettdrucken";
	public static final String ACTION_SPECIAL_AUFTRAGTERMIN_STUNDEN_MINUTEN = "action_special_auftragtermin_stunden_minuten";
	static final private String ACTION_SPECIAL_ETIKETTDRUCKEN = "action_special_etikettdrucken";

	/**
	 * Konstruktor.
	 *
	 * @param internalFrame
	 *            der InternalFrame auf dem das Panel sitzt
	 * @param add2TitleI
	 *            der default Titel des Panels
	 * @param key
	 *            PK der Position
	 * @throws java.lang.Throwable
	 *             Ausnahme
	 */
	public PanelAuftragPositionen2(InternalFrame internalFrame,
			String add2TitleI, Object key) throws Throwable {
		super(internalFrame, add2TitleI, key,
				PanelPositionen2.TYP_PANELPREISEINGABE_ARTIKELEINGABESNR); // posvkpf
		// :
		// 1
		// VKPF
		// ohne
		// SNR
		intFrame = (InternalFrameAuftrag) internalFrame;
		tpAuftrag = intFrame.getTabbedPaneAuftrag();
		zwController.setPositionNumberHelper(new PositionNumberHelperAuftrag());
		zwController.setBelegDto(tpAuftrag.getAuftragDto());

		jbInit();
		initComponents();
		initPanel();
	}

	private void jbInit() throws Throwable {

		if (LPMain
				.getInstance()
				.getDesktop()
				.darfAnwenderAufZusatzfunktionZugreifen(
						MandantFac.ZUSATZFUNKTION_AUFTRAG_MIT_EINKAUFPREIS)) {
			bPositionmiteinkaufpreis = true;
		} else {
			ParametermandantDto parameterDto = DelegateFactory
					.getInstance()
					.getParameterDelegate()
					.getMandantparameter(LPMain.getTheClient().getMandant(),
							ParameterFac.KATEGORIE_ANGEBOT,
							ParameterFac.PARAMETER_LIEFERANT_ANGEBEN);
			boolean bLieferantAngeben = (Boolean) parameterDto
					.getCWertAsObject();
			if (bLieferantAngeben) {
				bPositionmiteinkaufpreis = true;
			}
		}

		resetToolsPanel();

		// zusaetzliche buttons
		String[] aWhichButtonIUse = { ACTION_UPDATE, ACTION_SAVE,
				ACTION_DELETE, ACTION_DISCARD, ACTION_PRINT, ACTION_TEXT };

		enableToolsPanelButtons(aWhichButtonIUse);

		this.createAndSaveAndShowButton("/com/lp/client/res/box_preferences.png",
				LPMain.getTextRespectUISPr("bes.tooltip.manuellerledigen"),
				ACTION_SPECIAL_POSITIONMANUELLERLEDIGEN,
				RechteFac.RECHT_AUFT_AUFTRAG_CUD);

		this.createAndSaveAndShowButton(
				"/com/lp/client/res/data_replace16x16.png",
				LPMain.getTextRespectUISPr("auft.erzeugenegativemengenausab"),
				ACTION_SPECIAL_AUFTRAGTERMIN_STUNDEN_MINUTEN,
				RechteFac.RECHT_AUFT_AUFTRAG_CUD);

		ParametermandantDto parameter = DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getMandantparameter(
						LPMain.getInstance().getTheClient().getMandant(),
						ParameterFac.KATEGORIE_AUFTRAG,
						ParameterFac.PARAMETER_AUFTRAGSERIENNUMMERN_MIT_ETIKETTEN);

		Short sValue = new Short(parameter.getCWert());
		if (Helper.short2boolean(sValue)) {
			this.createAndSaveAndShowButton(
					"/com/lp/client/res/printer216x16.png",
					LPMain.getTextRespectUISPr("artikel.report.etikett"),
					ACTION_SPECIAL_SRN_ETIKETTDRUCKEN,
					RechteFac.RECHT_AUFT_AUFTRAG_CUD);

		}

		// Statusbar an den unteren Rand des Panels haengen
		iZeile++;
		add(getPanelStatusbar(), new GridBagConstraints(0, iZeile, 1, 1, 1.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));

		// der Liefertermin aus den Kopfdaten kann uebersteuert werden
		jLabelLieferterminPosition = new WrapperLabel();
		jLabelLieferterminPosition.setText(LPMain
				.getTextRespectUISPr("auft.label.postitionstermin"));
		wdfLieferterminPosition = new WrapperDateField();
		// wdfLieferterminPosition.setMandatoryField(true);
		wtfZeit = new WrapperTimeField();
		// posvkpf: 2
		panelArtikel.add(jLabelLieferterminPosition, new GridBagConstraints(0,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		panelArtikel.add(wdfLieferterminPosition, new GridBagConstraints(1,
				iZeile, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		if (tpAuftrag.getBAuftragterminstudenminuten())
			panelArtikel.add(wtfZeit, new GridBagConstraints(3, iZeile, 1, 1,
					0.0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		if (bPositionmiteinkaufpreis) {
			wlaEinkaufpreis = new WrapperLabel(
					LPMain.getTextRespectUISPr("lp.einkaufspreis"));
			wnfEinkaufpreis = new WrapperNumberField();
			wnfEinkaufpreis.setActivatable(true);
			wnfEinkaufpreis.setFractionDigits(Defaults.getInstance()
					.getIUINachkommastellenPreiseEK());
			wlaWaehrungEinkaufpreis = new WrapperLabel();

			panelArtikel.add(wlaEinkaufpreis, new GridBagConstraints(3, iZeile,
					3, 1, 0.0, 0.0, GridBagConstraints.NORTH,
					GridBagConstraints.HORIZONTAL, new Insets(10, 2, 2, 2), 0,
					0));
			panelArtikel.add(wnfEinkaufpreis, new GridBagConstraints(6, iZeile,
					1, 1, 0.0, 0.0, GridBagConstraints.NORTH,
					GridBagConstraints.HORIZONTAL, new Insets(10, 2, 2, 2), 0,
					0));
			panelArtikel.add(wlaWaehrungEinkaufpreis, new GridBagConstraints(7,
					iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.NORTH,
					GridBagConstraints.HORIZONTAL, new Insets(10, 2, 2, 2), 0,
					0));

		}

		this.createAndSaveAndShowButton("/com/lp/client/res/printer216x16.png",
				LPMain.getTextRespectUISPr("artikel.report.etikett"),
				ACTION_SPECIAL_ETIKETTDRUCKEN,
				RechteFac.RECHT_LS_LIEFERSCHEIN_CUD);

	}

	private void initPanel() throws Throwable {
		// combobox Positionen in der UI Sprache des Benutzers fuellen
		setPositionsarten(DelegateFactory
				.getInstance()
				.getAuftragServiceDelegate()
				.auftragpositionartFindAll(
						LPMain.getInstance().getTheClient().getLocUi()));
		if (bPositionmiteinkaufpreis)
			wnfEinkaufpreis.setVisible(true);
	}

	protected void setDefaults() throws Throwable {

		auftragpositionDto = new AuftragpositionDto();
		auftragpositionDto.setBNettopreisuebersteuert(new Short((short) 0));

		leereAlleFelder(this);

		// default positionsart ist ident
		wcoPositionsart
				.setKeyOfSelectedItem(AuftragServiceFac.AUFTRAGPOSITIONART_IDENT);

		super.setDefaults();

		if (tpAuftrag.getAuftragDto() != null
				&& tpAuftrag.getAuftragDto().getIId() != null) {
			// alle auftragabhaengigen Defaults in den verschiedenen
			// PanelPositionen setzen
			panelHandeingabe.setWaehrungCNr(tpAuftrag.getAuftragDto()
					.getCAuftragswaehrung());
			panelArtikel.setWaehrungCNr(tpAuftrag.getAuftragDto()
					.getCAuftragswaehrung());

			// posvkpf: 3 im PanelArtikel alles fuer die VKPF vorbereiten @todo
			// sprechende Exception, wenn nicht alles gesetzt ist... PJ 4810

			// SP1196 -> Preis/SOKOS muss von Rechnungsadresse kommen
			KundeDto kdDto_Rechnungsadresse = DelegateFactory
					.getInstance()
					.getKundeDelegate()
					.kundeFindByPrimaryKey(
							tpAuftrag.getAuftragDto()
									.getKundeIIdRechnungsadresse());

			((PanelPositionenArtikelVerkauf) panelArtikel)
					.setKundeDto(kdDto_Rechnungsadresse);
			((PanelPositionenArtikelVerkauf) panelArtikel)
					.setWechselkurs(tpAuftrag.getAuftragDto()
							.getFWechselkursmandantwaehrungzubelegwaehrung());
			((PanelPositionenArtikelVerkauf) panelArtikel)
					.setCNrWaehrung(tpAuftrag.getAuftragDto()
							.getCAuftragswaehrung());
			((PanelPositionenArtikelVerkauf) panelArtikel)
					.setGueltigkeitsdatumArtikeleinzelverkaufspreis(new java.sql.Date(
							tpAuftrag.getAuftragDto().getTBelegdatum()
									.getTime()));
			((PanelPositionenArtikelVerkauf) panelArtikel)
					.setIIdLager(DelegateFactory.getInstance()
							.getLagerDelegate().getHauptlagerDesMandanten()
							.getIId());

			// default liefertermin ist der termin aus den kopfdaten
			Timestamp datLieferterminKopfdaten = tpAuftrag.getAuftragDto()
					.getDLiefertermin();
			// Date datFinalterminKopfdaten = new Date(tpAuftrag.getAuftragDto()
			// .getDFinaltermin().getTime());
			if (bPositionmiteinkaufpreis) {
				wlaWaehrungEinkaufpreis.setText(tpAuftrag.getAuftragDto()
						.getCAuftragswaehrung());
			}
			wdfLieferterminPosition.setTimestamp(Helper
					.cutTimestamp(datLieferterminKopfdaten));
			if (tpAuftrag.getBAuftragterminstudenminuten())
				wtfZeit.setTime(new java.sql.Time(datLieferterminKopfdaten
						.getTime()));
			// SK Comment out. Finaltermin darf Posittionstermin nicht
			// einschraenken
			// wdfLieferterminPosition.setMinimumValue(new
			// Date(System.currentTimeMillis()));
			// wdfLieferterminPosition.setMaximumValue(datFinalterminKopfdaten);
			// wdfLieferterminPosition.setShowRubber(true);

			// IMS 1292 Wenn der Vorschlagswert fuer den Mwstsatz aus dem
			// Belegkunden kommt
			if (!panelHandeingabe.getBDefaultMwstsatzAusArtikel()) {
				if (tpAuftrag.getKundeAuftragDto() != null
						&& tpAuftrag.getKundeAuftragDto().getIId() != null) {
					// Aktuellen MWST-Satz uebersetzen.
					MwstsatzDto mwstsatzDtoAktuell = DelegateFactory
							.getInstance()
							.getMandantDelegate()
							.mwstsatzFindByMwstsatzbezIIdAktuellster(
									tpAuftrag.getKundeAuftragDto()
											.getMwstsatzbezIId());
					panelHandeingabe.wcoMwstsatz
							.setKeyOfSelectedItem(mwstsatzDtoAktuell.getIId());
				}
			}
			panelArtikel.wtfZusatzbezeichnung.setActivatable(true);
		}
		((PanelPositionenArtikelEingabeSNR) panelArtikel)
				.remove(((PanelPositionenArtikelEingabeSNR) panelArtikel).wbuSeriennr);
		((PanelPositionenArtikelEingabeSNR) panelArtikel)
				.remove(((PanelPositionenArtikelEingabeSNR) panelArtikel).wtfSeriennr);
	}

	public AuftragpositionDto getAuftragpositionDto() {
		return auftragpositionDto;
	}

	public void setModified(boolean changed) {
		zwController.setChanged(changed);
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
	 *             Ausnahme
	 */
	public void eventActionNew(EventObject eventObject, boolean bLockMeI,
			boolean bNeedNoNewI) throws Throwable {
		if (tpAuftrag.istAktualisierenAuftragErlaubt()) {

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
			tpAuftrag.getAuftragPositionenTop().updateButtons(
					tpAuftrag.getAuftragPositionenBottom()
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

		// posvkpf: 4
		panelArtikel.setArtikelEingabefelderEditable(false);
		((PanelPositionenArtikelVerkauf) panelArtikel).wbuPreisauswahl
				.setEnabled(false);

		tpAuftrag.enablePanelsNachBitmuster();
	}

	protected void eventActionUpdate(ActionEvent aE, boolean bNeedNoUpdateI)
			throws Throwable {
		if (tpAuftrag.istAktualisierenAuftragErlaubt()) {
			// Rahmenpositionen duerfen nur veraendert oder geloescht werden,
			// wenn es
			// noch keine Abrufe dazu gibt
			// SK 08/13169 auch teilerledigte abrufe sind aenderbar
			// if (DelegateFactory.getInstance().getAuftragpositionDelegate().
			// auftragpositionFindByAuftragpositionIIdRahmenpositionOhneExc(
			// auftragpositionDto.getIId()).length > 0) {
			// DialogFactory.showModalDialog(
			// LPMain.getInstance().getTextRespectUISPr("lp.hint"),
			// LPMain.getInstance().getTextRespectUISPr(
			// "bes.rahmenpositionnichtloeschen"));
			// }
			// else {
			super.eventActionUpdate(aE, false);
			// posvkpf: 5
			panelArtikel.setArtikelEingabefelderEditable(true);

			((PanelPositionenArtikelVerkauf) panelArtikel).wbuPreisauswahl
					.setEnabled(getInternalFrame().bRechtDarfPreiseAendernVerkauf);

			// if (getInternalFrame().bRechtDarfPreiseAendernVerkauf == true) {
			// ((PanelPositionenArtikelVerkauf) panelArtikel).wbuPreisauswahl
			// .setEnabled(true);
			// } else {
			// ((PanelPositionenArtikelVerkauf) panelArtikel).wbuPreisauswahl
			// .setEnabled(false);
			// }

			setzePositionsartAenderbar(auftragpositionDto);
			panelArtikel.setzeEinheitAenderbar();

			// sobald ich es erlaube, den Artikel der Ident Position umzudrehen,
			// muss ich
			// Logik ueberdenken -> Auftragreservierungen!
			panelArtikel.getWtfArtikel().setEditable(false); // UW 27.04.06
			wdfLieferterminPosition.setShowRubber(true);
			// }
		}
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
			// den Auftrag fuer die Statusbar neu einlesen
			tpAuftrag.setAuftragDto(DelegateFactory.getInstance()
					.getAuftragDelegate()
					.auftragFindByPrimaryKey(auftragpositionDto.getBelegIId()));
			dto2Components();
			LPButtonAction itemEtikket = (LPButtonAction) getHmOfButtons().get(
					ACTION_SPECIAL_ETIKETTDRUCKEN);
			itemEtikket.getButton().setEnabled(true);
		} else {
			panelArtikel.setLetzteArtikelCNr();
		}

		// posvkpf: 6 wenn der Auftrag gerade gelockt ist, die
		// Artikeleingabefelder freischalten
		if (getLockedstateDetailMainKey().getIState() == PanelBasis.LOCK_IS_LOCKED_BY_ME) {
			panelArtikel.setArtikelEingabefelderEditable(true);
			((PanelPositionenArtikelVerkauf) panelArtikel).wbuPreisauswahl
					.setEnabled(true);
		}

		setzePositionsartAenderbar(auftragpositionDto);
		panelArtikel.setzeEinheitAenderbar();
		tpAuftrag.enablePanelsNachBitmuster();

		aktualisiereStatusbar();
		refreshMyComponents();
		tpAuftrag.setTitleAuftrag(LPMain.getInstance().getTextRespectUISPr(
				"auft.title.panel.positionen"));

	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {
		// zugehoerig: 1
		boolean bPositionFuerZugehoerigenArtikelAnlegen = false;
		if (wirdPreisvorschlagsdialogGeradeAngezeit()) {
			return;
		}
		try {
			zwController.setBelegDto(tpAuftrag.getAuftragDto());
			zwController.setBelegPositionDto(getAuftragpositionDto());
			zwController
					.setSelectedPositionIId(bFuegeNeuePositionVorDerSelektiertenEin ? tpAuftrag
							.getSelectedIdPositionen() : null);

			calculateFields();

			if (allMandatoryFieldsSetDlg()) {

				if (auftragpositionDto.getTUebersteuerbarerLiefertermin() != null) {
					if (Helper.cutTimestamp(
							auftragpositionDto
									.getTUebersteuerbarerLiefertermin())
							.before(Helper.cutTimestamp(tpAuftrag
									.getAuftragDto().getTBelegdatum()))) {
						DialogFactory
								.showModalDialog(
										LPMain.getTextRespectUISPr("lp.hinweis"),
										LPMain.getTextRespectUISPr("auft.hint.positionsterminvorbelegtermin"));
					}
				}
				// Wenn ein guenstigerer VK-Preis moeglich waere und der
				// Mandantparameter == true,
				// dann den Benutzer darauf hinweisen
				((PanelPositionenArtikelVerkauf) panelArtikel).checkVkp();

				components2Dto();

				// PJ 15052
				if (auftragpositionDto.getTUebersteuerbarerLiefertermin() != null
						&& Helper.cutTimestamp(
								auftragpositionDto
										.getTUebersteuerbarerLiefertermin())
								.before(Helper.cutTimestamp(tpAuftrag
										.getAuftragDto().getTBelegdatum()))) {
					DialogFactory
							.showModalDialog(
									LPMain.getTextRespectUISPr("lp.warning"),
									LPMain.getTextRespectUISPr("bes.warning.lieferterminvorbelegdatum"));
				}

				boolean bDiePositionSpeichern = true;
				boolean bSeriennrVersionHeben = true;
				if (auftragpositionDto.getCSeriennrn() != null) {
					Object[][] data = DelegateFactory
							.getInstance()
							.getAuftragpositionDelegate()
							.isAuftragseriennrnVorhanden(
									auftragpositionDto.getCSeriennrn(),
									auftragpositionDto.getArtikelIId());
					if (data != null) {
						StringBuffer sb = new StringBuffer();
						for (int i = 0; i < data.length; i++) {
							if (data[i][1] != null) {
								MessageFormat mf = new MessageFormat(LPMain
										.getInstance().getTextRespectUISPr(
												"auft.hint.seriennrdefiniert"));
								mf.setLocale(LPMain.getInstance()
										.getTheClient().getLocUi());
								Object pattern[] = { data[i][0], data[i][1],
										data[i][2] };
								sb.append(mf.format(pattern) + "\n");
							}
						}
						bSeriennrVersionHeben = DialogFactory
								.showModalJaNeinDialog(getInternalFrame(), sb
										.toString(), LPMain
										.getTextRespectUISPr("lp.warning"),
										JOptionPane.WARNING_MESSAGE,
										JOptionPane.NO_OPTION);
					}
				}

				// checknumberformat: 4 die folgenden beiden Felder koennten
				// durch eine Berechnung zu gross fuer die DB sein
				bDiePositionSpeichern = HelperClient
						.checkNumberFormat(panelArtikel.wnfNettopreis
								.getBigDecimal());

				if (bDiePositionSpeichern && bSeriennrVersionHeben) {
					bDiePositionSpeichern = HelperClient
							.checkNumberFormat(panelArtikel.wnfBruttopreis
									.getBigDecimal());
				}

				bDiePositionSpeichern = tpAuftrag
						.warnungAuftragspositionOhneRahmenbezug(auftragpositionDto);

				if (bDiePositionSpeichern && bSeriennrVersionHeben) {
					if (auftragpositionDto.getPositionsartCNr()
							.equalsIgnoreCase(
									AuftragServiceFac.AUFTRAGPOSITIONART_IDENT)) {
						// auf Unterpreisigkeit pruefen
						bDiePositionSpeichern = DialogFactory
								.pruefeUnterpreisigkeitDlg(
										getInternalFrame(),
										panelArtikel.getArtikelDto().getIId(),
										null,
										auftragpositionDto
												.getNNettoeinzelpreis(),
										tpAuftrag
												.getAuftragDto()
												.getFWechselkursmandantwaehrungzubelegwaehrung(),
										auftragpositionDto.getNMenge());
						// zugehoerig: 2 Soll eine Position mit einem
						// eventuellen zugehoerigen Artikel angelegt werden?
						if (auftragpositionDto.getIId() == null) {
							bPositionFuerZugehoerigenArtikelAnlegen = DialogFactory
									.pruefeZugehoerigenArtikelAnlegenDlg(panelArtikel
											.getArtikelDto());
						}
					}
				}

				if (bDiePositionSpeichern) {
					bDiePositionSpeichern = getArtikelsetViewController()
							.validateArtikelsetConstraints(auftragpositionDto);
				}

				if (bDiePositionSpeichern && bSeriennrVersionHeben) {
					if (auftragpositionDto.getIId() == null) {
						// poseinfuegen: 3 Soll die neue Position vor der
						// aktuell selektierten stehen?
						if (bFuegeNeuePositionVorDerSelektiertenEin) {
							Integer iIdAktuellePosition = (Integer) tpAuftrag
									.getAuftragPositionenTop().getSelectedId();

							// die erste Position steht an der Stelle 1
							Integer iSortAktuellePosition = new Integer(1);

							if (iIdAktuellePosition != null) {
								iSortAktuellePosition = DelegateFactory
										.getInstance()
										.getAuftragpositionDelegate()
										.auftragpositionFindByPrimaryKey(
												iIdAktuellePosition).getISort();

								// poseinfuegen: 4 Die bestehenden Positionen
								// muessen Platz fuer die neue schaffen
								DelegateFactory
										.getInstance()
										.getAuftragpositionDelegate()
										.sortierungAnpassenBeiEinfuegenEinerPositionVorPosition(
												tpAuftrag.getAuftragDto()
														.getIId(),
												iSortAktuellePosition
														.intValue());
							}

							// poseinfuegen: 5 Die neue Position wird an frei
							// gemachte Position gesetzt
							auftragpositionDto.setISort(iSortAktuellePosition);
						}
						// wir legen eine neue Position an

						auftragpositionDto
								.setPositioniIdArtikelset(getArtikelsetViewController()
										.getArtikelSetIIdFuerNeuePosition());

						Integer pkPosition = DelegateFactory.getInstance()
								.getAuftragpositionDelegate()
								.createAuftragposition(auftragpositionDto);

						auftragpositionDto = DelegateFactory.getInstance()
								.getAuftragpositionDelegate()
								.auftragpositionFindByPrimaryKey(pkPosition);

						setKeyWhenDetailPanel(pkPosition);
					} else {

						// wir moechten eine bestehende Position mit neuen
						// Werten aktualisieren
						if (auftragpositionDto
								.getPositionsartCNr()
								.equalsIgnoreCase(
										AuftragServiceFac.AUFTRAGPOSITIONART_IDENT)
								|| auftragpositionDto
										.getPositionsartCNr()
										.equalsIgnoreCase(
												AuftragServiceFac.AUFTRAGPOSITIONART_HANDEINGABE)) {
							bDiePositionSpeichern = istMengenaenderungErlaubt(auftragpositionDto
									.getNMenge());
						}

						if (bDiePositionSpeichern) {
							if (tpAuftrag.getAuftragDto().getAuftragartCNr()
									.equals(AuftragServiceFac.AUFTRAGART_ABRUF)) {
								DelegateFactory
										.getInstance()
										.getAuftragRahmenAbrufDelegate()
										.updateAbrufpositionZuRahmenposition(
												auftragpositionDto);
							} else {
								DelegateFactory
										.getInstance()
										.getAuftragpositionDelegate()
										.updateAuftragposition(
												auftragpositionDto);
							}
						} else {
							DialogFactory
									.showModalDialog(
											LPMain.getInstance()
													.getTextRespectUISPr(
															"lp.warning"),
											LPMain.getInstance()
													.getTextRespectUISPr(
															"lp.hint.mengeliegtuntergeliefertermenge"));
						}
					}

					ParametermandantDto parameter = (ParametermandantDto) DelegateFactory
							.getInstance()
							.getParameterDelegate()
							.getParametermandant(
									ParameterFac.PARAMETER_MINDESTSTANDSWARNUNG,
									ParameterFac.KATEGORIE_AUFTRAG,
									LPMain.getTheClient().getMandant());

					if ((Boolean) parameter.getCWertAsObject()) {

						wirdLagermindeststandUnterschritten(
								auftragpositionDto
										.getTUebersteuerbarerLiefertermin(),
								auftragpositionDto.getNMenge(),
								auftragpositionDto.getArtikelIId());
					}


					//SP2140
					DelegateFactory
							.getInstance()
							.getAuftragDelegate()
							.korrekturbetragZuruecknehmen(
									auftragpositionDto.getBelegIId());

					// buttons schalten
					super.eventActionSave(e, false);

					eventYouAreSelected(false);

					bFuegeNeuePositionVorDerSelektiertenEin = false;
					getArtikelsetViewController()
							.resetArtikelSetIIdFuerNeuePosition();
				}
			}
		} finally {
			// poseinfuegen: 6 per Default wird eine neue Position ans Ende der
			// Liste gesetzt
			// bFuegeNeuePositionVorDerSelektiertenEin = false;
		}

		// zugehoerig: 3 wenn eine Position fuer einen zugehoerigen Artikel
		// angelegt werden soll,
		// dann muss die Eingabe fuer den zugehoerigen Artikel geoeffnet werden
		if (bPositionFuerZugehoerigenArtikelAnlegen) {
			ArtikelDto artikelDtoZugehoerig = DelegateFactory
					.getInstance()
					.getArtikelDelegate()
					.artikelFindByPrimaryKey(
							panelArtikel.getArtikelDto()
									.getArtikelIIdZugehoerig());
			BigDecimal nMengeZugehoerig = auftragpositionDto.getNMenge();

			ItemChangedEvent ice = new ItemChangedEvent(this,
					ItemChangedEvent.ACTION_NEW);
			tpAuftrag.getAuftragPositionenBottom().eventActionNew(ice, true,
					false);
			tpAuftrag.getAuftragPositionenBottom().eventYouAreSelected(false);

			tpAuftrag.getAuftragPositionenTop().updateButtons(
					tpAuftrag.getAuftragPositionenBottom()
							.getLockedstateDetailMainKey());

			panelArtikel.setArtikelDto(artikelDtoZugehoerig);
			panelArtikel.artikelDto2components();
			panelArtikel.wnfMenge.setBigDecimal(nMengeZugehoerig);

			panelArtikel.setArtikelEingabefelderEditable(true);
			((PanelPositionenArtikelVerkauf) panelArtikel).wbuPreisauswahl
					.setEnabled(true);

			setzePositionsartAenderbar(auftragpositionDto);
			panelArtikel.setzeEinheitAenderbar();
		}
		wdfLieferterminPosition.setShowRubber(false);
	}

	/**
	 * Wenn in einer bestehenden Auftragsposition die Menge geaendert wird,
	 * gilt: 1. Status der Position "OFFEN": Es kann beliebig geaendert werden.
	 * 2. Status der Position "TEILGELIEFERT": Es kann erhoeht oder auf die
	 * bereits gelieferte Menge reduziert werden. 3. Status der Position
	 * "GELIEFERT": Die Position kann nicht mehr geaendert werden.
	 *
	 * @param bdNeueMengeI
	 *            die eingegebene Menge
	 * @return boolean true, wenn die Mengenaenderung erlaubt ist
	 */
	private boolean istMengenaenderungErlaubt(BigDecimal bdNeueMengeI) {
		boolean aenderungErlaubt = true;

		if (auftragpositionDto.getAuftragpositionstatusCNr().equals(
				AuftragServiceFac.AUFTRAGPOSITIONSTATUS_TEILERLEDIGT)) {
			BigDecimal bereitsGelieferteMenge = auftragpositionDto.getNMenge()
					.subtract(auftragpositionDto.getNOffeneMenge());
			if (bdNeueMengeI.compareTo(bereitsGelieferteMenge) == -1) {
				aenderungErlaubt = false;
			} else if (bdNeueMengeI.compareTo(bereitsGelieferteMenge) == 0) {
				// erlaubt
				auftragpositionDto
						.setAuftragpositionstatusCNr(AuftragServiceFac.AUFTRAGPOSITIONSTATUS_ERLEDIGT);
			}
		} else if (auftragpositionDto.getAuftragpositionstatusCNr().equals(
				AuftragServiceFac.AUFTRAGPOSITIONSTATUS_ERLEDIGT)) {
			aenderungErlaubt = false;
		}

		return aenderungErlaubt;
	}

	private void refreshMyComponents() throws Throwable {
		boolean bEnable = true;
		LPButtonAction item = null;
		String cArt = tpAuftrag.getAuftragDto().getAuftragartCNr();
		BigDecimal bdNull = new BigDecimal(0);
		if (auftragpositionDto.getPositionsartCNr() != null) {
			bEnable = cArt.equals(AuftragServiceFac.AUFTRAGART_FREI)
					&& (auftragpositionDto.getPositionsartCNr().equals(
							AuftragServiceFac.AUFTRAGPOSITIONART_IDENT) || auftragpositionDto
							.getPositionsartCNr()
							.equals(AuftragServiceFac.AUFTRAGPOSITIONART_HANDEINGABE))
					&& (auftragpositionDto.getNOffeneMenge().compareTo(bdNull) != 0);
		} else {
			bEnable = cArt.equals(AuftragServiceFac.AUFTRAGART_FREI);
		}

		// boolean bEnableEinfuegen = ((LPMain.getInstance().getPasteBuffer()
		// .existsPasteBuffer(PasteBuffer.FILE_PASTEBUFFER_POSITIONS)) != null);

		/*
		 * item = (LPButtonAction) getHmOfButtons().get(
		 * ACTION_SPECIAL_POSITIONMANUELLERLEDIGEN);
		 * item.getButton().setEnabled(bEnable);
		 */
		if (auftragpositionDto.getPositionsartCNr() != null) {
			boolean bEnableEtikket = false;
			if (auftragpositionDto.getCZusatzbez() != null) {
				bEnableEtikket = (auftragpositionDto.getPositionsartCNr()
						.equals(LocaleFac.POSITIONSART_POSITION) && auftragpositionDto
						.getCZusatzbez().equals(LocaleFac.POSITIONBEZ_BEGINN));
			}
			if (getHmOfButtons().get(ACTION_SPECIAL_SRN_ETIKETTDRUCKEN) != null) {
				item = (LPButtonAction) getHmOfButtons().get(
						ACTION_SPECIAL_SRN_ETIKETTDRUCKEN);
				item.getButton().setEnabled(bEnableEtikket);
			}
			boolean bEnableVerschieben = !auftragpositionDto
					.getPositionsartCNr().equals(
							LocaleFac.POSITIONSART_POSITION);
			item = (LPButtonAction) tpAuftrag.getAuftragPositionenTop()
					.getHmOfButtons()
					.get(PanelBasis.ACTION_POSITION_VONNNACHNMINUS1);
			item.getButton().setVisible(bEnableVerschieben);
			item = (LPButtonAction) tpAuftrag.getAuftragPositionenTop()
					.getHmOfButtons()
					.get(PanelBasis.ACTION_POSITION_VONNNACHNPLUS1);
			item.getButton().setVisible(bEnableVerschieben);
		}
		// SK: Wenn es ein Rahmenauftrag ist wird das Feld Positionstermin nicht
		// angezeigt
		if (AuftragServiceFac.AUFTRAGART_RAHMEN.equals(tpAuftrag
				.getAuftragDto().getAuftragartCNr())) {
			jLabelLieferterminPosition.setVisible(false);
			wdfLieferterminPosition.setVisible(false);
		} else {
			jLabelLieferterminPosition.setVisible(true);
			wdfLieferterminPosition.setVisible(true);
		}

		boolean bEnableAuftragStundenMinuten = tpAuftrag
				.getBAuftragterminstudenminuten();
		LPButtonAction itemAuftragStundenMinuten = null;
		itemAuftragStundenMinuten = (LPButtonAction) getHmOfButtons().get(
				ACTION_SPECIAL_AUFTRAGTERMIN_STUNDEN_MINUTEN);
		itemAuftragStundenMinuten.getButton().setVisible(
				bEnableAuftragStundenMinuten);
		itemAuftragStundenMinuten.getButton().setEnabled(
				bEnableAuftragStundenMinuten);

	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		Integer posIId = (Integer) tpAuftrag.getAuftragPositionenTop()
				.getSelectedId();
		if (posIId != null) {
			if (e.getActionCommand().equals(
					ACTION_SPECIAL_POSITIONMANUELLERLEDIGEN)) {
				if (auftragpositionDto.getAuftragpositionstatusCNr() != null) {
					if (!auftragpositionDto
							.getAuftragpositionstatusCNr()
							.equals(AuftragServiceFac.AUFTRAGPOSITIONSTATUS_ERLEDIGT)) {
						// tpAuftrag
						boolean answer = (DialogFactory.showMeldung(
								LPMain.getInstance().getTextRespectUISPr(
										"bes.menu.bearbeiten.manuellerledigen")
										+ " ?", LPMain.getInstance()
										.getTextRespectUISPr("lp.warning"),
								javax.swing.JOptionPane.YES_NO_OPTION) == javax.swing.JOptionPane.YES_OPTION);
						if (answer) {
							DelegateFactory.getInstance()
									.getAuftragpositionDelegate()
									.manuellErledigen(posIId);
							tpAuftrag.getAuftragPositionenTop()
									.eventYouAreSelected(true);
						}
					} else {
						boolean answer = (DialogFactory
								.showMeldung(
										LPMain.getInstance()
												.getTextRespectUISPr(
														"bes.menu.bearbeiten.erledigungaufheben")
												+ " ?",
										LPMain.getInstance()
												.getTextRespectUISPr(
														"lp.warning"),
										javax.swing.JOptionPane.YES_NO_OPTION) == javax.swing.JOptionPane.YES_OPTION);
						if (answer) {
							DelegateFactory.getInstance()
									.getAuftragpositionDelegate()
									.manuellErledigungAufgeben(posIId);
							tpAuftrag.getAuftragPositionenTop()
									.eventYouAreSelected(true);
						}
					}
				}
			} else if (e.getActionCommand().equals(
					ACTION_SPECIAL_SRN_ETIKETTDRUCKEN)) {
				AuftragpositionDto pos = null;
				try {
					pos = DelegateFactory
							.getInstance()
							.getAuftragpositionDelegate()
							.auftragpositionFindByAuftragISort(
									auftragpositionDto.getBelegIId(),
									auftragpositionDto.getISort() + 1);
				} catch (Throwable ex) {
					/*
					 * if (ex instanceof EJBExceptionLP) { throw new
					 * ExceptionLP( ( (EJBExceptionLP) ex).getCode(),
					 * ex.getCause()); } else { handleException(ex, true); }
					 */
				}

				if (pos != null) {
					ReportAuftragSrnEtikett reportEtikett = new ReportAuftragSrnEtikett(
							getInternalFrame(), pos.getBelegIId(),
							pos.getIId(), pos.getArtikelIId(), "");
					getInternalFrame().showReportKriterienZweiDrucker(
							reportEtikett, null, null, false, false, false,
							false);
					reportEtikett.eventYouAreSelected(false);
				}
			} else if (e.getActionCommand().equals(
					ACTION_SPECIAL_AUFTRAGTERMIN_STUNDEN_MINUTEN)) {
				DelegateFactory
						.getInstance()
						.getAuftragDelegate()
						.erzeugeNegativeMengeAusAuftrag(
								tpAuftrag.getAuftragDto().getIId());
				tpAuftrag.getAuftragPositionenTop().eventYouAreSelected(false);
			} else if (e.getActionCommand().equals(
					ACTION_SPECIAL_ETIKETTDRUCKEN)) {
				ReportAuftragpositionsetikett pos = new ReportAuftragpositionsetikett(
						getInternalFrame(), auftragpositionDto.getIId(), "");
				getInternalFrame().showReportKriterien(pos);

			}
		}
	}

	protected void eventActionDelete(ActionEvent e,
			boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {
		if (tpAuftrag.istAktualisierenAuftragErlaubt()) {
			// Rahmenpositionen duerfen nur veraendert oder geloescht werden,
			// wenn es
			// noch keine Abrufe dazu gibt
			if (DelegateFactory
					.getInstance()
					.getAuftragpositionDelegate()
					.auftragpositionFindByAuftragpositionIIdRahmenpositionOhneExc(
							auftragpositionDto.getIId()).length > 0) {
				DialogFactory
						.showModalDialog(
								LPMain.getInstance().getTextRespectUISPr(
										"lp.hint"),
								LPMain.getInstance().getTextRespectUISPr(
										"bes.rahmenpositionnichtloeschen"));
			} else {
				if (tpAuftrag.getAuftragDto().getAuftragartCNr()
						.equals(AuftragServiceFac.AUFTRAGART_ABRUF)) {
					if (auftragpositionDto
							.getAuftragpositionIIdRahmenposition() != null) {
						DelegateFactory
								.getInstance()
								.getAuftragRahmenAbrufDelegate()
								.removeAbrufpositionZuRahmenposition(
										auftragpositionDto);
						this.setKeyWhenDetailPanel(null);
					} else {
						DelegateFactory.getInstance()
								.getAuftragpositionDelegate()
								.removeAuftragposition(auftragpositionDto);
						this.setKeyWhenDetailPanel(null);
					}
				} else {
					DelegateFactory.getInstance().getAuftragpositionDelegate()
							.removeAuftragposition(auftragpositionDto);
					this.setKeyWhenDetailPanel(null);
				}
				//SP2140
				DelegateFactory
						.getInstance()
						.getAuftragDelegate()
						.korrekturbetragZuruecknehmen(
								auftragpositionDto.getBelegIId());
				super.eventActionDelete(e, false, false); // keyWasForLockMe
				// nicht
				// ueberschreiben
			}
		}
	}

	private void dto2Components() throws Throwable {
		// 1. Behandlung der trivialen Positionsarten.
		super.dto2Components(auftragpositionDto, tpAuftrag.getKundeAuftragDto()
				.getPartnerDto().getLocaleCNrKommunikation());
		// 2. Weiter mit den anderen.
		String positionsart = auftragpositionDto.getPositionsartCNr();

		if (positionsart
				.equalsIgnoreCase(AuftragServiceFac.AUFTRAGPOSITIONART_IDENT)) {
			// Auftragsspezifisches.
			wdfLieferterminPosition.setDate(auftragpositionDto
					.getTUebersteuerbarerLiefertermin());
			if (tpAuftrag.getBAuftragterminstudenminuten())
				wtfZeit.setTime(new java.sql.Time(auftragpositionDto
						.getTUebersteuerbarerLiefertermin().getTime()));

			String srnnrn = DelegateFactory.getInstance()
					.getAuftragpositionDelegate()
					.getSeriennummmern(auftragpositionDto.getIId());
			((PanelPositionenArtikelEingabeSNR) panelArtikel).wtfSeriennr
					.setText(srnnrn);
			if (LPMain
					.getInstance()
					.getDesktop()
					.darfAnwenderAufZusatzfunktionZugreifen(
							MandantFac.ZUSATZFUNKTION_AUFTRAG_SERIENNUMMERN)) {
				((PanelPositionenArtikelEingabeSNR) panelArtikel)
						.aktualisiereFelderSnrChnr(false);
			}
			if (bPositionmiteinkaufpreis)
				wnfEinkaufpreis.setBigDecimal(auftragpositionDto
						.getBdEinkaufpreis());
		} else if (positionsart
				.equalsIgnoreCase(AuftragServiceFac.AUFTRAGPOSITIONART_HANDEINGABE)) {
			// Auftragsspezifisches.
			panelHandeingabe.wdfLieferterminPosition.setDate(auftragpositionDto
					.getTUebersteuerbarerLiefertermin());
		}
	}

	/**
	 * Alle Positionsdaten aus dem Panel sammeln.
	 *
	 * @throws Throwable
	 */
	private void components2Dto() throws Throwable {
		// 1. Behandlung der trivialen Positionsarten.
		super.components2Dto(auftragpositionDto, tpAuftrag.getKundeAuftragDto()
				.getPartnerDto().getLocaleCNrKommunikation(), tpAuftrag
				.getAuftragDto().getIId());
		// 2. Weiter mit den anderen.

		String positionsart = getPositionsartCNr();

		if (auftragpositionDto.getIId() == null) {
			// wenn es sich um eine mengenbehaftete Position handelt ...
			if (positionsart.equals(LocaleFac.POSITIONSART_HANDEINGABE)) {
				auftragpositionDto
						.setAuftragpositionstatusCNr(AuftragServiceFac.AUFTRAGPOSITIONSTATUS_OFFEN);
				auftragpositionDto.setNOffeneMenge(panelHandeingabe.wnfMenge
						.getBigDecimal());
			} else if (positionsart.equals(LocaleFac.POSITIONSART_IDENT)) {
				auftragpositionDto
						.setAuftragpositionstatusCNr(AuftragServiceFac.AUFTRAGPOSITIONSTATUS_OFFEN);
				auftragpositionDto.setNOffeneMenge(panelArtikel.wnfMenge
						.getBigDecimal());
			}
		}

		if (positionsart
				.equalsIgnoreCase(AuftragServiceFac.AUFTRAGPOSITIONART_IDENT)) {
			// hier zusaetzlich gespeicherte Betraege.
			auftragpositionDto.setNRabattbetrag(panelArtikel.wnfRabattsumme
					.getBigDecimal());
			auftragpositionDto.setNMwstbetrag(panelArtikel.wnfMwstsumme
					.getBigDecimal());
			if (bPositionmiteinkaufpreis)
				auftragpositionDto.setBdEinkaufpreis(wnfEinkaufpreis
						.getBigDecimal());

			// Auftragsspezifisch.
			if (wdfLieferterminPosition.getTimestamp() != null) {
				if (tpAuftrag.getBAuftragterminstudenminuten()) {
					java.sql.Timestamp ts = wdfLieferterminPosition
							.getTimestamp();
					ts = Helper.cutTimestamp(ts);

					Calendar cDatum = Calendar.getInstance();
					cDatum.setTimeInMillis(ts.getTime());

					Calendar cZeit = Calendar.getInstance();
					cZeit.setTimeInMillis(wtfZeit.getTime().getTime());

					cDatum.set(Calendar.HOUR_OF_DAY,
							cZeit.get(Calendar.HOUR_OF_DAY));
					cDatum.set(Calendar.MINUTE, cZeit.get(Calendar.MINUTE));
					cDatum.set(Calendar.SECOND, cZeit.get(Calendar.SECOND));
					cDatum.set(Calendar.MILLISECOND,
							cZeit.get(Calendar.MILLISECOND));
					ts.setTime(cDatum.getTimeInMillis());
					auftragpositionDto.setTUebersteuerbarerLiefertermin(ts);
				} else {
					auftragpositionDto.setTUebersteuerbarerLiefertermin(Helper
							.cutTimestamp(wdfLieferterminPosition
									.getTimestamp()));

				}
			} else {
				auftragpositionDto.setTUebersteuerbarerLiefertermin(Helper
						.cutTimestamp(tpAuftrag.getAuftragDto()
								.getDLiefertermin()));
			}
			// ueberpruefen ob artikel serien oder chargenbehaftet
			if (auftragpositionDto.getArtikelIId() != null) {
				ArtikelDto artikelDto = DelegateFactory
						.getInstance()
						.getArtikelDelegate()
						.artikelFindByPrimaryKey(
								auftragpositionDto.getArtikelIId());
				if (Helper.short2boolean(artikelDto.getBSeriennrtragend())) {
					try {
						cSeriennrn = Helper
								.erzeugeSeriennummernArray(
										((PanelPositionenArtikelEingabeSNR) panelArtikel).wtfSeriennr
												.getText(),
										panelArtikel.wnfMenge.getBigDecimal(),
										true);

					} catch (Throwable ex) {
						if (ex instanceof EJBExceptionLP) {
							throw new ExceptionLP(
									((EJBExceptionLP) ex).getCode(),
									ex.getCause());
						} else {
							handleException(ex, true);
						}
					}
					auftragpositionDto.setCSeriennrn(cSeriennrn);
				} else {
					auftragpositionDto.setCSeriennrchargennr(null);
				}
			}
		} else if (positionsart
				.equalsIgnoreCase(AuftragServiceFac.AUFTRAGPOSITIONART_HANDEINGABE)) {
			// hier zusaetzlich gespeicherte Betraege.
			auftragpositionDto.setNRabattbetrag(panelHandeingabe.wnfRabattsumme
					.getBigDecimal());
			auftragpositionDto.setNMwstbetrag(panelHandeingabe.wnfMwstsumme
					.getBigDecimal());
			// Auftragsspezifisch.
			if (panelHandeingabe.wdfLieferterminPosition.getTimestamp() != null) {
				auftragpositionDto
						.setTUebersteuerbarerLiefertermin(panelHandeingabe.wdfLieferterminPosition
								.getTimestamp());
			} else {
				auftragpositionDto.setTUebersteuerbarerLiefertermin(tpAuftrag
						.getAuftragDto().getDLiefertermin());
			}
		} else if (positionsart
				.equalsIgnoreCase(AuftragServiceFac.AUFTRAGPOSITIONART_INTELLIGENTE_ZWISCHENSUMME)) {
			Integer lastZwsPositionIId = panelIntZwischensumme.components2Dto(
					tpAuftrag.getAuftragDto(), auftragpositionDto);
			AuftragpositionDto lastPosition = DelegateFactory.getInstance()
					.getAuftragpositionDelegate()
					.auftragpositionFindByPrimaryKey(lastZwsPositionIId);
			auftragpositionDto.setMwstsatzIId(lastPosition.getMwstsatzIId());
		}

		auftragpositionDto
				.setAuftragpositionstatusCNr(AuftragServiceFac.AUFTRAGPOSITIONSTATUS_OFFEN);
	}

	/**
	 * Drucke Auftragbestaetigung.
	 *
	 * @param e
	 *            Ereignis
	 * @throws Throwable
	 */
	protected void eventActionPrint(HvActionEvent e) throws Throwable {

		if (e.isMouseEvent() && e.isRightButtonPressed()) {

			boolean bStatusAngelegt = tpAuftrag.getAuftragDto().getStatusCNr().equals(AngebotServiceFac.ANGEBOTSTATUS_ANGELEGT);
			boolean bKonditionen = tpAuftrag.pruefeKonditionen();

			if (bStatusAngelegt && bKonditionen) {
				DelegateFactory.getInstance().getAuftragDelegate().berechneAktiviereBelegControlled(
						tpAuftrag.getAuftragDto().getIId());
				eventActionRefresh(e, false);
			}
			else if (!bStatusAngelegt) {
				DialogFactory.showModalDialog("Status",
						LPMain.getMessageTextRespectUISPr("status.zustand",
								LPMain.getTextRespectUISPr("auft.auftrag"),
								tpAuftrag.getAuftragStatus().trim()));
			}

		} else {
			tpAuftrag.printAuftragbestaetigung();
		}
		// eventYouAreSelected(false);
	}

	protected void aktiviereOhnePrint(ActionEvent e) throws Throwable {
		tpAuftrag.getAuftragStatus();
	}

	public void aktualisiereStatusbar() throws Throwable {
		setStatusbarPersonalIIdAnlegen(tpAuftrag.getAuftragDto()
				.getPersonalIIdAnlegen());
		setStatusbarTAnlegen(tpAuftrag.getAuftragDto().getTAnlegen());
		setStatusbarPersonalIIdAendern(tpAuftrag.getAuftragDto()
				.getPersonalIIdAendern());
		setStatusbarTAendern(tpAuftrag.getAuftragDto().getTAendern());
		setStatusbarStatusCNr(tpAuftrag.getAuftragStatus());

		String sLagerinfoO = "";
		if (panelArtikel.getArtikelDto() != null
				&& panelArtikel.getArtikelDto().getIId() != null) {

			sLagerinfoO = DelegateFactory
					.getInstance()
					.getLagerDelegate()
					.lagerFindByPrimaryKey(
							tpAuftrag.getAuftragDto()
									.getLagerIIdAbbuchungslager()).getCNr();

			if (Helper.short2boolean(panelArtikel.getArtikelDto()
					.getBLagerbewirtschaftet())) {
				BigDecimal ddMenge = DelegateFactory
						.getInstance()
						.getLagerDelegate()
						.getLagerstand(
								panelArtikel.getArtikelDto().getIId(),
								tpAuftrag.getAuftragDto()
										.getLagerIIdAbbuchungslager());

				sLagerinfoO += ": ";
				sLagerinfoO += ddMenge;
			}

		}

		String lieferterminInfo = LPMain.getInstance().getTextRespectUISPr(
				"label.liefertermin.kurz")

				+ com.lp.util.Helper.formatDatum(tpAuftrag.getAuftragDto()
						.getDLiefertermin(), LPMain.getInstance()
						.getUISprLocale()) + ", " + sLagerinfoO;
		setStatusbarSpalte5(lieferterminInfo);
	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_AUFTRAG;
	}

	public boolean handleOwnException(ExceptionLP exfc) {
		boolean bErrorErkannt = true;

		if (exfc.getICode() == EJBExceptionLP.FEHLER_LAGER_HAUPTLAGERDESMANDANTEN_NICHT_ANGELEGT) {
			DialogFactory.showModalDialog(LPMain.getInstance()
					.getTextRespectUISPr("lp.error"), LPMain.getInstance()
					.getTextRespectUISPr("auft.mandant.hauptlager_fehlt"));
		} else {
			bErrorErkannt = false;
		}

		return bErrorErkannt;
	}

	protected void eventActionRefresh(ActionEvent e, boolean bNeedNoRefreshI)
			throws Throwable {
		super.eventActionRefresh(e, bNeedNoRefreshI);

		tpAuftrag.enablePanelsNachBitmuster();
	}

	public LockStateValue getLockedstateDetailMainKey() throws Throwable {

		LockStateValue lockStateValue = super.getLockedstateDetailMainKey();
		/*
		 * if (tpAuftrag.getAuftragDto().getIId() != null &&
		 * lockStateValue.getIState() != PanelBasis.LOCK_FOR_NEW &&
		 * lockStateValue.getIState() != PanelBasis.LOCK_IS_LOCKED_BY_ME &&
		 * lockStateValue.getIState() !=
		 * PanelBasis.LOCK_IS_LOCKED_BY_OTHER_USER) { if
		 * (tpAuftrag.getAuftragDto
		 * ().getAuftragstatusCNr().equals(AuftragServiceFac.
		 * AUFTRAGSTATUS_ANGELEGT)) { // Drucken kann man nur, wenn die
		 * Konditionen und mengenbehaftete Positionen erfasst wurden if
		 * (tpAuftrag.getAuftragDto().getAuftragIIdKopftext() != null &&
		 * tpAuftrag.getAuftragDto().getAuftragIIdFusstext() != null &&
		 * DelegateFactory.getInstance().getAuftragpositionDelegate().
		 * getAnzahlMengenbehafteteAuftragpositionen
		 * (tpAuftrag.getAuftragDto().getIId()) > 0) { lockStateValue = new
		 * LockStateValue(PanelBasis.
		 * LOCK_ENABLE_REFRESHANDUPDATEANDPRINT_ONLY); } else { lockStateValue =
		 * new LockStateValue(PanelBasis. LOCK_ENABLE_REFRESHANDUPDATE_ONLY); }
		 * } else if
		 * (tpAuftrag.getAuftragDto().getAuftragstatusCNr().equals(AuftragServiceFac
		 * . AUFTRAGSTATUS_TEILERLEDIGT) ||
		 * tpAuftrag.getAuftragDto().getAuftragstatusCNr
		 * ().equals(AuftragServiceFac. AUFTRAGSTATUS_ERLEDIGT) ||
		 * tpAuftrag.getAuftragDto
		 * ().getAuftragstatusCNr().equals(AuftragServiceFac.
		 * AUFTRAGSTATUS_STORNIERT)) { // Button Stornieren ist nicht
		 * verfuegbar, in diesen Faellen wird ein echtes Update ausgeloest
		 * lockStateValue = new LockStateValue(PanelBasis.
		 * LOCK_ENABLE_REFRESHANDUPDATEANDPRINT_ONLY); } }
		 */

		if (tpAuftrag.getAuftragDto().getIId() != null) {
			if (AuftragServiceFac.AUFTRAGART_RAHMEN.equals(tpAuftrag
					.getAuftragDto().getAuftragartCNr())) {
				if (tpAuftrag.getAuftragDto().getStatusCNr()
						.equals(AuftragServiceFac.AUFTRAGSTATUS_STORNIERT)
						|| tpAuftrag
								.getAuftragDto()
								.getStatusCNr()
								.equals(AuftragServiceFac.AUFTRAGSTATUS_ERLEDIGT)) {
					lockStateValue = new LockStateValue(
							PanelBasis.LOCK_ENABLE_REFRESHANDPRINT_ONLY);
				}
			} else {
				if (tpAuftrag.getAuftragDto().getStatusCNr()
						.equals(AuftragServiceFac.AUFTRAGSTATUS_STORNIERT)
						|| tpAuftrag
								.getAuftragDto()
								.getStatusCNr()
								.equals(AuftragServiceFac.AUFTRAGSTATUS_TEILERLEDIGT)
						|| tpAuftrag
								.getAuftragDto()
								.getStatusCNr()
								.equals(AuftragServiceFac.AUFTRAGSTATUS_ERLEDIGT)) {
					lockStateValue = new LockStateValue(
							PanelBasis.LOCK_ENABLE_REFRESHANDPRINT_ONLY);
				}
			}
		}
		return lockStateValue;
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wcoPositionsart;
	}

	public void wnfPauschalposition_focusLost(FocusEvent e) {
		super.wnfPauschalposition_focusLost(e);
		try {
			DelegateFactory
					.getInstance()
					.getAuftragpositionDelegate()
					.berechnePauschalposition(
							wnfPauschalpositionpreis.getBigDecimal(),
							auftragpositionDto.getIId(),
							auftragpositionDto.getBelegIId());
		} catch (ExceptionLP e1) {
		}
	}
}
