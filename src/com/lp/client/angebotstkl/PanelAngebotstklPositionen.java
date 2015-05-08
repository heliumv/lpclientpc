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
package com.lp.client.angebotstkl;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.math.BigDecimal;
import java.util.EventObject;

import javax.swing.JComponent;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelPositionen2;
import com.lp.client.frame.component.PanelPositionenArtikelVerkauf;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.angebotstkl.service.AgstklarbeitsplanDto;
import com.lp.server.angebotstkl.service.AgstklpositionDto;
import com.lp.server.angebotstkl.service.AngebotstklServiceFac;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.VerkaufspreisDto;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

@SuppressWarnings("static-access")
/**
 * <p>Panel fuer Angebotsstuecklistenpositionen.</p>
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 * <p>Erstellungsdatum 14.12.05</p>
 * <p> </p>
 * @author Uli Walch
 * @version $Revision: 1.6 $
 */
public class PanelAngebotstklPositionen extends PanelPositionen2 {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** Cache for convenience. */
	private InternalFrameAngebotstkl intFrame = null;
	/** Cache for convenience. */
	private TabbedPaneAngebotstkl tpAngebotstkl = null;
	/** Cache for convenience. */
	private AgstklpositionDto agstklpositionDto = null;

	private WrapperCheckBox wcbMitdrucken = new WrapperCheckBox();

	public PanelAngebotstklPositionen(InternalFrame internalFrame,
			String add2TitleI, Object key) throws Throwable {
		super(internalFrame, add2TitleI, key,
				PanelPositionen2.TYP_PANELPREISEINGABE_ARTIKELVERKAUF);

		intFrame = (InternalFrameAngebotstkl) internalFrame;
		tpAngebotstkl = intFrame.getTabbedPaneAngebotstkl();

		jbInit();
		initComponents();
		initPanel();
	}

	private void jbInit() throws Throwable {
		// braucht nur refresh, save und aendern
		resetToolsPanel();

		// zusaetzliche buttons
		String[] aWhichButtonIUse = { ACTION_UPDATE, ACTION_SAVE,
				ACTION_DELETE, ACTION_DISCARD, ACTION_PRINT };

		wcbMitdrucken.setText(LPMain.getInstance().getTextRespectUISPr(
				"stkl.positionen.mitdrucken"));
		wcbMitdrucken.setMnemonic('A');

		enableToolsPanelButtons(aWhichButtonIUse);
		iZeile++;

		add(wcbMitdrucken, new GridBagConstraints(0, 6, 1, 1, 1.0, 0.0,
				GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
				new Insets(0, 220, 10, 0), 0, 0));
		// Statusbar an den unteren Rand des Panels haengen
		iZeile++;
		add(getPanelStatusbar(), new GridBagConstraints(0, iZeile, 1, 1, 1.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));

		panelArtikel.setVisibleZeileMwstsumme(false);
		panelArtikel.setVisibleZeileBruttogesamtpreis(false);
		panelArtikel.setVisibleGestehungspreis(true);
		panelArtikel.wnfGestpreis.setMandatoryField(true);

		panelHandeingabe.setVisibleZeileMwstsumme(false);
		panelHandeingabe.setVisibleGestehungspreis(true);
		panelHandeingabe.setVisibleZeileMwstsumme(false);
		panelHandeingabe.wnfGestpreis.setMandatoryField(true);

		panelHandeingabe.setVisibleZeileBruttogesamtpreis(false);
		panelHandeingabe.setVisibleZeileLieferterminposition(false);
	}

	private void initPanel() throws Throwable {
		// combobox Positionen in der UI Sprache des Benutzers fuellen
		setPositionsarten(DelegateFactory.getInstance()
				.getAngebotstklDelegate().getAllAgstklpositionsart());
	}

	protected void setDefaults() throws Throwable {
		agstklpositionDto = new AgstklpositionDto();
		agstklpositionDto.setBRabattsatzuebersteuert(new Short((short) 0));

		leereAlleFelder(this);

		// default positionsart ist ident
		wcoPositionsart
				.setKeyOfSelectedItem(AngebotstklServiceFac.AGSTKLPOSITIONART_IDENT);

		super.setDefaults();

		if (intFrame.getAgstklDto() != null
				&& intFrame.getAgstklDto().getIId() != null) {
			// alle auftragabhaengigen Defaults in den verschiedenen
			// PanelPositionen setzen
			panelHandeingabe.setWaehrungCNr(intFrame.getAgstklDto()
					.getWaehrungCNr());
			panelArtikel.setWaehrungCNr(intFrame.getAgstklDto()
					.getWaehrungCNr());

			// im PanelArtikel alles fuer die VKPF vorbereiten
			((PanelPositionenArtikelVerkauf) panelArtikel)
					.setKundeDto(DelegateFactory
							.getInstance()
							.getKundeDelegate()
							.kundeFindByPrimaryKey(
									intFrame.getAgstklDto().getKundeIId()));
			((PanelPositionenArtikelVerkauf) panelArtikel)
					.setWechselkurs(new Double(intFrame.getAgstklDto()
							.getFWechselkursmandantwaehrungzuagstklwaehrung()
							.doubleValue()));
			((PanelPositionenArtikelVerkauf) panelArtikel)
					.setCNrWaehrung(intFrame.getAgstklDto().getWaehrungCNr());
			((PanelPositionenArtikelVerkauf) panelArtikel)
					.setGueltigkeitsdatumArtikeleinzelverkaufspreis(Helper
							.extractDate(intFrame.getAgstklDto()
									.getTBelegdatum()));
			((PanelPositionenArtikelVerkauf) panelArtikel)
					.setGueltigkeitsdatumArtikeleinzelverkaufspreis(new java.sql.Date(
							System.currentTimeMillis()));
			((PanelPositionenArtikelVerkauf) panelArtikel)
					.setIIdLager(DelegateFactory.getInstance()
							.getLagerDelegate().getHauptlagerDesMandanten()
							.getIId());

		}
	}

	public void eventActionNew(EventObject eventObject, boolean bLockMeI,
			boolean bNeedNoNewI) throws Throwable {
		super.eventActionNew(eventObject, true, false); // LockMeForNew setzen

		setDefaults();

		// die neue Position soll vor der aktuell selektierten eingefuegt werden
		if (((ItemChangedEvent) eventObject).getID() == ItemChangedEvent.ACTION_POSITION_VORPOSITIONEINFUEGEN) {
			// Dieses Flag gibt an, ob die neue Position vor der aktuellen
			// eingefuegt werden soll
			bFuegeNeuePositionVorDerSelektiertenEin = true;
		} else {
			bFuegeNeuePositionVorDerSelektiertenEin = false;
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
		// bei Discard zuruecksetzen
		bFuegeNeuePositionVorDerSelektiertenEin = false;
	}

	protected void eventActionUpdate(ActionEvent aE, boolean bNeedNoUpdateI)
			throws Throwable {
		super.eventActionUpdate(aE, false);

		panelArtikel.setArtikelEingabefelderEditable(true);
		((PanelPositionenArtikelVerkauf) panelArtikel).wbuPreisauswahl
				.setEnabled(true);

		setzePositionsartAenderbar(agstklpositionDto);
		panelArtikel.setzeEinheitAenderbar();
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {
		super.eventYouAreSelected(false);
		setTBelegdatumMwstsatz(intFrame.getAgstklDto().getTBelegdatum());
		// den gesamten Inhalt zuruecksetzen, ausloeser war ev. ein discard
		setDefaults();
		// position neu einlesen, ausloeser war ev. ein refresh
		Object pkPosition = getKeyWhenDetailPanel();

		if (pkPosition != null && !pkPosition.equals(LPMain.getLockMeForNew())) {
			// Position neu einlesen.
			agstklpositionDto = DelegateFactory.getInstance()
					.getAngebotstklpositionDelegate()
					.agstklpositionFindByPrimaryKey((Integer) pkPosition);
			// das Angebot fuer die Statusbar neu einlesen
			intFrame.setAgstklDto(DelegateFactory.getInstance()
					.getAngebotstklDelegate()
					.agstklFindByPrimaryKey(agstklpositionDto.getAgstklIId()));
			dto2Components();
		} else {
			panelArtikel.setLetzteArtikelCNr();
		}

		// wenn das Angebot gerade von mir gelockt ist, die Artikeleingabefelder
		// freischalten
		if (getLockedstateDetailMainKey().getIState() == PanelBasis.LOCK_IS_LOCKED_BY_ME) {
			panelArtikel.setArtikelEingabefelderEditable(true);
			((PanelPositionenArtikelVerkauf) panelArtikel).wbuPreisauswahl
					.setEnabled(true);
		}

		setzePositionsartAenderbar(agstklpositionDto);
		panelArtikel.setzeEinheitAenderbar();
		tpAngebotstkl.refreshTitle();
		aktualisiereStatusbar();
	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {
		boolean bPositionFuerZugehoerigenArtikelAnlegen = false;

		try {
			calculateFields();

			if (allMandatoryFieldsSetDlg()) {
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
					if (agstklpositionDto
							.getAgstklpositionsartCNr()
							.equalsIgnoreCase(
									AngebotstklServiceFac.AGSTKLPOSITIONART_IDENT)) {
						// auf Unterpreisigkeit pruefen
						bDiePositionSpeichern = DialogFactory
								.pruefeUnterpreisigkeitDlg(
										getInternalFrame(),
										panelArtikel.getArtikelDto().getIId(),
										null,
										agstklpositionDto
												.getNNettogesamtpreis(),
										intFrame.getAgstklDto()
												.getFWechselkursmandantwaehrungzuagstklwaehrung()
												.doubleValue(),
										agstklpositionDto.getNMenge());
						if (bDiePositionSpeichern) {
							// Soll eine Position mit einem eventuellen
							// zugehoerigen Artikel angelegt werden?
							if (agstklpositionDto.getIId() == null) {
								bPositionFuerZugehoerigenArtikelAnlegen = DialogFactory
										.pruefeZugehoerigenArtikelAnlegenDlg(panelArtikel
												.getArtikelDto());
							}
						}
					}
				}

				if (bDiePositionSpeichern) {
					if (agstklpositionDto.getIId() == null) {
						// Soll die neue Position vor der aktuell selektierten
						// stehen?
						if (bFuegeNeuePositionVorDerSelektiertenEin) {
							Integer iIdAktuellePosition = (Integer) intFrame
									.getTabbedPaneAngebotstkl()
									.getAngebotstklPositionenTop()
									.getSelectedId();

							// erstepos: 0 die erste Position steht an der
							// Stelle 1
							Integer iSortAktuellePosition = new Integer(1);

							// erstepos: 1 die erste Position steht an der
							// Stelle 1
							if (iIdAktuellePosition != null) {
								iSortAktuellePosition = DelegateFactory
										.getInstance()
										.getAngebotstklpositionDelegate()
										.agstklpositionFindByPrimaryKey(
												iIdAktuellePosition).getISort();

								// Die bestehenden Positionen muessen Platz fuer
								// die neue schaffen
								DelegateFactory
										.getInstance()
										.getAngebotstklpositionDelegate()
										.sortierungAnpassenBeiEinfuegenEinerPositionVorPosition(
												intFrame.getAgstklDto()
														.getIId(),
												iSortAktuellePosition
														.intValue());
							}

							// Die neue Position wird an frei gemachte Position
							// gesetzt
							agstklpositionDto.setISort(iSortAktuellePosition);
						}

						// wir legen eine neue Position an
						Integer pkPosition = DelegateFactory.getInstance()
								.getAngebotstklpositionDelegate()
								.createAgstklposition(agstklpositionDto);

						agstklpositionDto = DelegateFactory.getInstance()
								.getAngebotstklpositionDelegate()
								.agstklpositionFindByPrimaryKey(pkPosition);

						setKeyWhenDetailPanel(pkPosition);
					} else {
						DelegateFactory.getInstance()
								.getAngebotstklpositionDelegate()
								.updateAgstklposition(agstklpositionDto);
					}
				}

				// buttons schalten
				super.eventActionSave(e, false);

				eventYouAreSelected(false);
			}
		} finally {
			// per Default wird eine neue Position ans Ende der Liste gesetzt
			bFuegeNeuePositionVorDerSelektiertenEin = false;
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
			BigDecimal nMengeZugehoerig = agstklpositionDto.getNMenge();

			ItemChangedEvent ice = new ItemChangedEvent(this,
					ItemChangedEvent.ACTION_NEW);
			tpAngebotstkl.getAngebotstklPositionenBottom().eventActionNew(ice,
					true, false);
			tpAngebotstkl.getAngebotstklPositionenBottom().eventYouAreSelected(
					false);

			tpAngebotstkl.getAngebotstklPositionenTop().updateButtons(
					tpAngebotstkl.getAngebotstklPositionenBottom()
							.getLockedstateDetailMainKey());

			panelArtikel.setArtikelDto(artikelDtoZugehoerig);
			panelArtikel.artikelDto2components();
			panelArtikel.wnfMenge.setBigDecimal(nMengeZugehoerig);

			panelArtikel.setArtikelEingabefelderEditable(true);
			((PanelPositionenArtikelVerkauf) panelArtikel).wbuPreisauswahl
					.setEnabled(true);

			setzePositionsartAenderbar(agstklpositionDto);
			panelArtikel.setzeEinheitAenderbar();
		}
	}

	protected void eventActionDelete(ActionEvent e,
			boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {

		Object[] o = tpAngebotstkl.getAngebotstklPositionenTop()
				.getSelectedIds();
		if (o != null) {
			for (int i = 0; i < o.length; i++) {
				AgstklpositionDto toRemove = DelegateFactory.getInstance()
						.getAngebotstklpositionDelegate()
						.agstklpositionFindByPrimaryKey((Integer) o[i]);
				DelegateFactory.getInstance().getAngebotstklpositionDelegate()
						.removeAgstklposition(toRemove);

			}
		}

		this.setKeyWhenDetailPanel(null);
		super.eventActionDelete(e, false, false); // keyWasForLockMe nicht
													// ueberschreiben
	}

	private void dto2Components() throws Throwable {
		// 1. Behandlung der trivialen Positionsarten.
		super.dto2Components(agstklpositionDto,
				((PanelPositionenArtikelVerkauf) panelArtikel).getKundeDto()
						.getPartnerDto().getLocaleCNrKommunikation());
		// 2. Weiter mit den anderen.
		String positionsart = agstklpositionDto.getPositionsartCNr();

		wcbMitdrucken.setShort(agstklpositionDto.getBDrucken());

		if (positionsart
				.equalsIgnoreCase(AngebotstklServiceFac.AGSTKLPOSITIONART_IDENT)) {
			panelArtikel.wnfGestpreis.setBigDecimal(agstklpositionDto
					.getNGestehungspreis());

			// die Artikelbezeichnung kann in der Angebotposition uebersteuert
			// sein
			if (Helper.short2boolean(agstklpositionDto
					.getBArtikelbezeichnunguebersteuert())) {
				panelArtikel.wtfBezeichnung
						.setText(agstklpositionDto.getCBez());
			}

			// den Verkaufspreis hinterlegen und anzeigen
			((PanelPositionenArtikelVerkauf) panelArtikel).verkaufspreisDtoInZielwaehrung = new VerkaufspreisDto();
			((PanelPositionenArtikelVerkauf) panelArtikel).verkaufspreisDtoInZielwaehrung.einzelpreis = agstklpositionDto
					.getNNettoeinzelpreis();

			BigDecimal nRabattsumme = agstklpositionDto.getNNettoeinzelpreis()
					.multiply(
							new BigDecimal(agstklpositionDto.getFRabattsatz()
									.doubleValue()).movePointLeft(2));

			((PanelPositionenArtikelVerkauf) panelArtikel).verkaufspreisDtoInZielwaehrung.rabattsumme = nRabattsumme;
			((PanelPositionenArtikelVerkauf) panelArtikel).verkaufspreisDtoInZielwaehrung.nettopreis = agstklpositionDto
					.getNNettogesamtpreis();
			((PanelPositionenArtikelVerkauf) panelArtikel).verkaufspreisDtoInZielwaehrung.mwstsumme = new BigDecimal(
					0);
			((PanelPositionenArtikelVerkauf) panelArtikel).verkaufspreisDtoInZielwaehrung.bruttopreis = new BigDecimal(
					0);
			((PanelPositionenArtikelVerkauf) panelArtikel).verkaufspreisDtoInZielwaehrung.rabattsatz = new Double(
					agstklpositionDto.getFRabattsatz().doubleValue());

			if (agstklpositionDto.getFZusatzrabattsatz() != null) {
				((PanelPositionenArtikelVerkauf) panelArtikel).verkaufspreisDtoInZielwaehrung
						.setDdZusatzrabattsatz(new Double(agstklpositionDto
								.getFZusatzrabattsatz().doubleValue()));

				BigDecimal nZusatzrabattsumme = agstklpositionDto
						.getNNettoeinzelpreis()
						.subtract(nRabattsumme)
						.multiply(
								new BigDecimal(agstklpositionDto
										.getFZusatzrabattsatz().doubleValue()))
						.movePointLeft(2);

				((PanelPositionenArtikelVerkauf) panelArtikel).verkaufspreisDtoInZielwaehrung
						.setNZusatzrabattsumme(nZusatzrabattsumme);
			}

			((PanelPositionenArtikelVerkauf) panelArtikel)
					.verkaufspreisDto2components();
		} else if (positionsart
				.equalsIgnoreCase(AngebotstklServiceFac.AGSTKLPOSITIONART_HANDEINGABE)) {

			panelHandeingabe.wnfGestpreis.setBigDecimal(agstklpositionDto
					.getNGestehungspreis());

			panelHandeingabe.getWnfRabattsatz()
					.setDouble(
							new Double(agstklpositionDto.getFRabattsatz()
									.doubleValue()));
			panelHandeingabe.wnfEinzelpreis.setBigDecimal(agstklpositionDto
					.getNNettoeinzelpreis());

			BigDecimal nRabattsumme = agstklpositionDto.getNNettoeinzelpreis()
					.multiply(
							new BigDecimal(agstklpositionDto.getFRabattsatz()
									.doubleValue()).movePointLeft(2));

			panelHandeingabe.wnfRabattsumme.setBigDecimal(nRabattsumme);

			// den Zusatzrabattsatz setzen
			if (agstklpositionDto.getFZusatzrabattsatz() != null) {
				panelHandeingabe.wnfZusatzrabattsatz
						.setDouble(new Double(agstklpositionDto
								.getFZusatzrabattsatz().doubleValue()));

				BigDecimal nZusatzrabattsumme = agstklpositionDto
						.getNNettoeinzelpreis()
						.subtract(nRabattsumme)
						.multiply(
								new BigDecimal(agstklpositionDto
										.getFZusatzrabattsatz().doubleValue()))
						.movePointLeft(2);

				panelHandeingabe.wnfZusatzrabattsumme
						.setBigDecimal(nZusatzrabattsumme);
			}

			panelHandeingabe.wnfNettopreis.setBigDecimal(agstklpositionDto
					.getNNettogesamtpreis());
		}
	}

	/**
	 * Alle Positionsdaten aus dem Panel sammeln.
	 * 
	 * @throws Throwable
	 */
	private void components2Dto() throws Throwable {
		// 1. Behandlung der trivialen Positionsarten.
		super.components2Dto(agstklpositionDto,
				((PanelPositionenArtikelVerkauf) panelArtikel).getKundeDto()
						.getPartnerDto().getLocaleCNrKommunikation(), intFrame
						.getAgstklDto().getIId());
		// 2. Weiter mit den anderen.

		String positionsart = getPositionsartCNr();

		agstklpositionDto.setBDrucken(wcbMitdrucken.getShort());

		if (positionsart
				.equalsIgnoreCase(AngebotstklServiceFac.AGSTKLPOSITIONART_IDENT)) {
			agstklpositionDto.setNGestehungspreis(panelArtikel.wnfGestpreis
					.getBigDecimal());

			// muss fuer die AG Stkl. extra gesetzt werden.
			agstklpositionDto.setFRabattsatz(panelArtikel.getWnfRabattsatz()
					.getDouble());

			// wurde der Rabattsatz aus der Verkaufspreisfindung ubersteuert?
			if (((PanelPositionenArtikelVerkauf) panelArtikel).bIstRabattsatzDefaultUebersteuert) {
				agstklpositionDto.setBRabattsatzuebersteuert(new Short(
						(short) 1));
			} else {
				agstklpositionDto.setBRabattsatzuebersteuert(new Short(
						(short) 0));
			}

			agstklpositionDto.setFZusatzrabattsatz(panelArtikel
					.getWnfZusatzrabattsatz().getDouble());
			agstklpositionDto.setNNettoeinzelpreis(panelArtikel.wnfEinzelpreis
					.getBigDecimal());
			agstklpositionDto.setNNettogesamtpreis(panelArtikel.wnfNettopreis
					.getBigDecimal());
		} else if (positionsart
				.equalsIgnoreCase(AngebotstklServiceFac.AGSTKLPOSITIONART_HANDEINGABE)) {
			agstklpositionDto.setNGestehungspreis(panelHandeingabe.wnfGestpreis
					.getBigDecimal());

			agstklpositionDto.setFRabattsatz(panelHandeingabe
					.getWnfRabattsatz().getDouble());
			agstklpositionDto.setBRabattsatzuebersteuert(new Short((short) 0));
			agstklpositionDto
					.setNNettoeinzelpreis(panelHandeingabe.wnfEinzelpreis
							.getBigDecimal());
			agstklpositionDto.setFZusatzrabattsatz(panelHandeingabe
					.getWnfZusatzrabattsatz().getDouble());
			agstklpositionDto
					.setNNettogesamtpreis(panelHandeingabe.wnfNettopreis
							.getBigDecimal());
		}
	}

	private void aktualisiereStatusbar() throws Throwable {
		setStatusbarPersonalIIdAnlegen(intFrame.getAgstklDto()
				.getPersonalIIdAnlegen());
		setStatusbarTAnlegen(intFrame.getAgstklDto().getTAnlegen());
		setStatusbarPersonalIIdAendern(intFrame.getAgstklDto()
				.getPersonalIIdAendern());
		setStatusbarTAendern(intFrame.getAgstklDto().getTAendern());
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wcoPositionsart;
	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_AGSTKL;
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

	protected void eventActionPrint(ActionEvent e) throws Throwable {
		intFrame.getTabbedPaneAngebotstkl().printAngebotstkl();
		eventYouAreSelected(false);
	}
}
