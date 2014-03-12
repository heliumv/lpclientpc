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
package com.lp.client.angebotstkl;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.math.BigDecimal;
import java.util.EventObject;
import java.util.Map;

import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.lp.client.bestellung.PanelPositionenArtikelBestellung;
import com.lp.client.frame.Defaults;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelPositionen2;
import com.lp.client.frame.component.PanelPositionenArtikelEinkauf;
import com.lp.client.frame.component.PanelPositionenArtikelVerkauf;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperFixableNumberField;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.angebotstkl.service.AgstklpositionDto;
import com.lp.server.angebotstkl.service.AngebotstklServiceFac;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikellieferantDto;
import com.lp.server.artikel.service.ArtikellieferantstaffelDto;
import com.lp.server.artikel.service.VerkaufspreisDto;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
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
public class PanelAgstklPositionenEinkauf extends PanelPositionen2 {
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

	JPanel panelAufschlag = new JPanel(new GridBagLayout());

	public ButtonGroup buttonGroupFixable = new ButtonGroup();

	private WrapperCheckBox wcbMitdrucken = new WrapperCheckBox();

	public PanelAgstklPositionenEinkauf(InternalFrame internalFrame,
			String add2TitleI, Object key) throws Throwable {
		super(internalFrame, add2TitleI, key,
				PanelPositionen2.TYP_PANELPREISEINGABE_ARTIKELEINKAUF);

		intFrame = (InternalFrameAngebotstkl) internalFrame;
		tpAngebotstkl = intFrame.getTabbedPaneAngebotstkl();

		jbInit();
		initComponents();
		initPanel();
	}

	private void jbInit() throws Throwable {
		// braucht nur refresh, save und aendern
		resetToolsPanel();

		wcbMitdrucken.setText(LPMain.getInstance().getTextRespectUISPr(
				"stkl.positionen.mitdrucken"));
		wcbMitdrucken.setMnemonic('A');

		getInternalFrame().addItemChangedListener(this);
		((PanelPositionenArtikelEinkauf) panelArtikel).wbuPreisauswahl
				.addActionListener(this);

		// zusaetzliche buttons
		String[] aWhichButtonIUse = { ACTION_UPDATE, ACTION_SAVE,
				ACTION_DELETE, ACTION_DISCARD, ACTION_PRINT };

		enableToolsPanelButtons(aWhichButtonIUse);

		iZeile++;

		add(wcbMitdrucken, new GridBagConstraints(0, 7, 1, 1, 1.0, 0.0,
				GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
				new Insets(0, 220, 10, 0), 0, 0));
		// Statusbar an den unteren Rand des Panels haengen
		iZeile++;
		add(getPanelStatusbar(), new GridBagConstraints(0, iZeile, 1, 1, 1.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));

		// Zeile
		if (intFrame.iKalkulationsart == 3) {
			panelArtikel.addZeileAufschlag(panelArtikel, iZeile);
			panelHandeingabe.addZeileAufschlag(panelHandeingabe, iZeile);
		}

		panelArtikel.setVisibleGestehungspreis(false);

		if (intFrame.iKalkulationsart == 3) {
			// panelArtikel.setVisibleGestehungspreis(false);

		}

		panelHandeingabe.setVisibleZeileMwstsumme(false);
		panelHandeingabe.setVisibleGestehungspreis(false);

		panelHandeingabe.setVisibleZeileBruttogesamtpreis(false);
		panelHandeingabe.setVisibleZeileLieferterminposition(false);
		panelHandeingabe.setVisibleZeileZusatzrabattsumme(false);

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

			ParametermandantDto parameter = (ParametermandantDto) DelegateFactory
					.getInstance()
					.getParameterDelegate()
					.getParametermandant(
							ParameterFac.PARAMETER_DEFAULT_AUFSCHLAG,
							ParameterFac.KATEGORIE_ANGEBOTSSTUECKLISTE,
							LPMain.getTheClient().getMandant());

			if (parameter.getCWertAsObject() != null) {
				Double dAufschlag = (Double) parameter.getCWertAsObject();

				if (panelArtikel.wnfAufschlagProzent != null) {
					panelArtikel.wnfAufschlagProzent.setDouble(dAufschlag);
				}
				if (panelHandeingabe.wnfAufschlagProzent != null) {
					panelHandeingabe.wnfAufschlagProzent.setDouble(dAufschlag);
				}
			}

			// im PanelArtikel alles fuer die VKPF vorbereiten

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
		((PanelPositionenArtikelEinkauf) panelArtikel).wbuPreisauswahl
				.setEnabled(false);
		// bei Discard zuruecksetzen
		bFuegeNeuePositionVorDerSelektiertenEin = false;
	}

	protected void eventActionUpdate(ActionEvent aE, boolean bNeedNoUpdateI)
			throws Throwable {
		super.eventActionUpdate(aE, false);

		panelArtikel.setArtikelEingabefelderEditable(true);
		((PanelPositionenArtikelEinkauf) panelArtikel).wbuPreisauswahl
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
			((PanelPositionenArtikelEinkauf) panelArtikel).wbuPreisauswahl
					.setEnabled(true);
		}

		setzePositionsartAenderbar(agstklpositionDto);
		panelArtikel.setzeEinheitAenderbar();

		aktualisiereStatusbar();
	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		if (e.getActionCommand().equals(
				PanelPositionenArtikelEinkauf.ACTION_SPECIAL_EK_PREIS_HOLEN)) {
			try {
				setPreis();
			} catch (Throwable exDummy) {
				// nothing here
			}

		}
	}

	protected void eventItemchanged(EventObject eI) throws Throwable {

		ItemChangedEvent e = (ItemChangedEvent) eI;

		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			if (e.getSource() == ((PanelPositionenArtikelEinkauf) panelArtikel).wifArtikelauswahl) {
				if (((PanelPositionenArtikelEinkauf) panelArtikel).wifArtikelauswahl
						.getArtikelDto() != null) {

					setPreis();
				}
			}
		}
	}

	protected void setPreis() throws Throwable {

		ArtikellieferantDto artLiefDto = DelegateFactory
				.getInstance()
				.getArtikelDelegate()
				.getArtikelEinkaufspreis(
						panelArtikel.getArtikelDto().getIId(),
						null,
						panelArtikel.wnfMenge.getBigDecimal(),
						tpAngebotstkl.getInternalFrameAngebotstkl()
								.getAgstklDto().getWaehrungCNr(),
						new java.sql.Date(tpAngebotstkl
								.getInternalFrameAngebotstkl().getAgstklDto()
								.getTBelegdatum().getTime()));

		if (artLiefDto != null) {
			if (artLiefDto.getNNettopreis() != null) {
				BigDecimal nettopreis = artLiefDto.getNNettopreis();
				if (panelArtikel.wnfMaterialzuschlag != null
						&& panelArtikel.wnfMaterialzuschlag.getBigDecimal() != null) {
					nettopreis = nettopreis
							.add(panelArtikel.wnfMaterialzuschlag
									.getBigDecimal());
				}
				panelArtikel.wnfNettopreis.setBigDecimal(nettopreis);

			}
			if (artLiefDto.getFRabatt() != null) {
				panelArtikel.wnfRabattsatz.setDouble(artLiefDto.getFRabatt());
				if (artLiefDto.getNNettopreis() != null) {
					panelArtikel.wnfRabattsumme.setBigDecimal(Helper
							.getProzentWert(artLiefDto.getNNettopreis(),
									new BigDecimal(artLiefDto.getFRabatt()),
									Defaults.getInstance()
											.getIUINachkommastellenPreiseEK()));
				}
			}

			if (artLiefDto.getNEinzelpreis() != null) {
				panelArtikel.wnfEinzelpreis.setBigDecimal(artLiefDto
						.getNEinzelpreis());
			}

			if (Helper.short2boolean(artLiefDto.getBRabattbehalten()) == true) {
				panelArtikel.wnfRabattsumme.getWrbFixNumber().setSelected(true);
			} else {
				panelArtikel.wnfNettopreis.getWrbFixNumber().setSelected(true);
			}

			// Bei Neu Aufschlag auf Parameter holen

		}
		panelArtikel.berechneAufschlag();
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
					if (agstklpositionDto
							.getAgstklpositionsartCNr()
							.equalsIgnoreCase(
									AngebotstklServiceFac.AGSTKLPOSITIONART_IDENT)) {

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
			((PanelPositionenArtikelEinkauf) panelArtikel).wbuPreisauswahl
					.setEnabled(true);

			setzePositionsartAenderbar(agstklpositionDto);
			panelArtikel.setzeEinheitAenderbar();
		}
	}

	protected void eventActionDelete(ActionEvent e,
			boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {
		DelegateFactory.getInstance().getAngebotstklpositionDelegate()
				.removeAgstklposition(agstklpositionDto);
		this.setKeyWhenDetailPanel(null);
		super.eventActionDelete(e, false, false); // keyWasForLockMe nicht
													// ueberschreiben
	}

	private void dto2Components() throws Throwable {
		// 1. Behandlung der trivialen Positionsarten.

		KundeDto kdDto = DelegateFactory.getInstance().getKundeDelegate()
				.kundeFindByPrimaryKey(intFrame.getAgstklDto().getKundeIId());

		super.dto2Components(agstklpositionDto, kdDto.getPartnerDto()
				.getLocaleCNrKommunikation());
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

			panelArtikel.getWnfRabattsatz().setDouble(
					agstklpositionDto.getFRabattsatz());
			panelArtikel.wnfEinzelpreis.setBigDecimal(agstklpositionDto
					.getNNettoeinzelpreis());
			panelArtikel.wnfRabattsumme.setBigDecimal(Helper.getProzentWert(
					agstklpositionDto.getNNettoeinzelpreis(), new BigDecimal(
							agstklpositionDto.getFRabattsatz()), 4));
			panelArtikel.wnfMaterialzuschlag.setBigDecimal(agstklpositionDto
					.getNMaterialzuschlag());
			panelArtikel.wnfNettopreis.setBigDecimal(agstklpositionDto
					.getNNettogesamtpreis());

			/*
			 * panelArtikel.setArtikelDto(agstklpositionDto); if
			 * (oArtikelDto.getIId() != null) { artLiefDto = DelegateFactory
			 * .getInstance() .getArtikelDelegate()
			 * .artikellieferantFindByArtikellIIdLieferantIIdOhneExc(
			 * oArtikelDto.getIId(), tPBes.getLieferantDto().getIId()); }
			 */

			BigDecimal nNettoBetrag = panelArtikel.wnfEinzelpreis
					.getBigDecimal().subtract(
							panelArtikel.wnfRabattsumme.getBigDecimal());

			if (Helper.short2Boolean((agstklpositionDto
					.getBRabattsatzuebersteuert())) == true) {
				panelArtikel.wnfRabattsumme.getWrbFixNumber().setSelected(true);
			} else {
				panelArtikel.wnfNettopreis.getWrbFixNumber().setSelected(true);
			}

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
		KundeDto kdDto = DelegateFactory.getInstance().getKundeDelegate()
				.kundeFindByPrimaryKey(intFrame.getAgstklDto().getKundeIId());

		super.components2Dto(agstklpositionDto, kdDto.getPartnerDto()
				.getLocaleCNrKommunikation(), intFrame.getAgstklDto().getIId());
		// 2. Weiter mit den anderen.

		String positionsart = getPositionsartCNr();
		agstklpositionDto.setNGestehungspreis(new BigDecimal(0));
		agstklpositionDto.setBDrucken(wcbMitdrucken.getShort());
		if (positionsart
				.equalsIgnoreCase(AngebotstklServiceFac.AGSTKLPOSITIONART_IDENT)) {
			agstklpositionDto.setNGestehungspreis(panelArtikel.wnfGestpreis
					.getBigDecimal());

			// muss fuer die AG Stkl. extra gesetzt werden.
			agstklpositionDto.setFRabattsatz(panelArtikel.getWnfRabattsatz()
					.getDouble());

			// wurde der Rabattsatz aus der Verkaufspreisfindung ubersteuert?

			agstklpositionDto
					.setBRabattsatzuebersteuert(panelArtikel.wnfRabattsumme
							.getWrbFixNumber().getShort());
			agstklpositionDto
					.setBNettopreisuebersteuert(panelArtikel.wnfNettopreis
							.getWrbFixNumber().getShort());

			agstklpositionDto.setFZusatzrabattsatz(0D);
			agstklpositionDto.setNNettoeinzelpreis(panelArtikel.wnfEinzelpreis
					.getBigDecimal());
			agstklpositionDto.setNNettogesamtpreis(panelArtikel.wnfNettopreis
					.getBigDecimal());

		} else if (positionsart
				.equalsIgnoreCase(AngebotstklServiceFac.AGSTKLPOSITIONART_HANDEINGABE)) {

			agstklpositionDto.setFRabattsatz(panelHandeingabe
					.getWnfRabattsatz().getDouble());
			agstklpositionDto.setBRabattsatzuebersteuert(new Short((short) 0));
			agstklpositionDto
					.setNNettoeinzelpreis(panelHandeingabe.wnfEinzelpreis
							.getBigDecimal());
			agstklpositionDto.setFZusatzrabattsatz(0D);
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
