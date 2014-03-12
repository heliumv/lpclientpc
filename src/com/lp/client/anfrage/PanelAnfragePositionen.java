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
package com.lp.client.anfrage;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.math.BigDecimal;
import java.util.EventObject;

import javax.swing.JComponent;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.LockStateValue;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelPositionen2;
import com.lp.client.frame.component.PanelPositionenArtikelEinkauf;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.anfrage.service.AnfrageServiceFac;
import com.lp.server.anfrage.service.AnfragepositionDto;
import com.lp.server.artikel.service.ArtgruDto;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

@SuppressWarnings("static-access")
/**
 * <p>Panel fuer Anfragepositionen.</p>
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 * <p>Erstellungsdatum 14.06.05</p>
 * <p> </p>
 * @author Uli Walch
 * @version $Revision: 1.7 $
 */
public class PanelAnfragePositionen extends PanelPositionen2 {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** Cache for convenience. */
	private InternalFrameAnfrage intFrame = null;
	/** Cache for convenience. */
	private TabbedPaneAnfrage tpAnfrage = null;
	/** Cache for convenience. */
	private AnfragepositionDto anfragepositionDto = null;

	public PanelAnfragePositionen(InternalFrame internalFrame,
			String add2TitleI, Object key) throws Throwable {
		super(internalFrame, add2TitleI, key,
				PanelPositionen2.TYP_PANELPREISEINGABE_ARTIKELANFRAGE);
		intFrame = (InternalFrameAnfrage) internalFrame;
		tpAnfrage = intFrame.getTabbedPaneAnfrage();

		jbInit();
		initComponents();
		initPanel();
	}

	private void jbInit() throws Throwable {
		// braucht nur refresh, save und aendern
		resetToolsPanel();

		// zusaetzliche buttons
		String[] aWhichButtonIUse = { ACTION_UPDATE, ACTION_SAVE,
				ACTION_DELETE, ACTION_DISCARD, ACTION_PRINT, ACTION_TEXT };

		enableToolsPanelButtons(aWhichButtonIUse);

		panelHandeingabe.setVisibleZeileRabattsumme(false);
		panelHandeingabe.setVisibleZeileZusatzrabattsumme(false);
		panelHandeingabe.setVisibleZeileNettogesamtpreis(false);
		panelHandeingabe.setVisibleZeileMwstsumme(false);
		panelHandeingabe.setVisibleZeileBruttogesamtpreis(false);
		panelHandeingabe.setVisibleZeileLieferterminposition(false);
		panelHandeingabe.wlaEinzelpreis.setText(LPMain.getInstance()
				.getTextRespectUISPr("anf.richtpreis"));

		panelArtikel.setVisibleZeileRabattsumme(false);
		// panelArtikel.setVisibleZeileZusatzrabattsumme(false);
		panelArtikel.setVisibleZeileNettogesamtpreis(false);

		((PanelPositionenArtikelEinkauf) panelArtikel).wlaRabattsatz
				.setVisible(false);
		panelArtikel.wlaEinzelpreis.setText(LPMain.getInstance()
				.getTextRespectUISPr("anf.richtpreis"));

		panelHandeingabe.wnfEinzelpreis.setDependenceField(false);

		panelArtikel.wnfEinzelpreis.setDependenceField(false);

		// Statusbar an den unteren Rand des Panels haengen
		iZeile++;
		add(getPanelStatusbar(), new GridBagConstraints(0, iZeile, 1, 1, 1.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
	}

	private void initPanel() throws Throwable {
		// combobox Positionen in der UI Sprache des Benutzers fuellen
		setPositionsarten(DelegateFactory
				.getInstance()
				.getAnfrageServiceDelegate()
				.getAnfragepositionart(
						LPMain.getInstance().getTheClient().getLocUi()));
	}

	protected void setDefaults() throws Throwable {
		anfragepositionDto = new AnfragepositionDto();
		anfragepositionDto.setBNettopreisuebersteuert(new Short( (short) 0));
		leereAlleFelder(this);

		// default positionsart ist ident
		wcoPositionsart
				.setKeyOfSelectedItem(AnfrageServiceFac.ANFRAGEPOSITIONART_IDENT);

		super.setDefaults();

		if (tpAnfrage.getAnfrageDto() != null
				&& tpAnfrage.getAnfrageDto().getIId() != null) {
			// alle auftragabhaengigen Defaults in den verschiedenen
			// PanelPositionen setzen
			panelHandeingabe.setWaehrungCNr(tpAnfrage.getAnfrageDto()
					.getWaehrungCNr());
			panelArtikel.setWaehrungCNr(tpAnfrage.getAnfrageDto()
					.getWaehrungCNr());

			((PanelPositionenArtikelEinkauf) panelArtikel)
					.setLieferantDto(tpAnfrage.getLieferantDto());
			((PanelPositionenArtikelEinkauf) panelArtikel)
					.setBdWechselkurs(new BigDecimal(tpAnfrage.getAnfrageDto()
							.getFWechselkursmandantwaehrungzubelegwaehrung()
							.doubleValue()));
		}
		panelArtikel.wtfZusatzbezeichnung.setActivatable(true);
	}

	public void eventActionNew(EventObject eventObject, boolean bLockMeI,
			boolean bNeedNoNewI) throws Throwable {
		if (tpAnfrage.istAktualisierenAnfrageErlaubt()) {

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
			tpAnfrage.getAnfragePositionenTop().updateButtons(
					tpAnfrage.getAnfragePositionenBottom()
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
	}

	protected void eventActionUpdate(ActionEvent aE, boolean bNeedNoUpdateI)
			throws Throwable {
		if (tpAnfrage.istAktualisierenAnfrageErlaubt()) {
			super.eventActionUpdate(aE, false);

			panelArtikel.setArtikelEingabefelderEditable(true);

			setzePositionsartAenderbar(anfragepositionDto);
			panelArtikel.setzeEinheitAenderbar();
		}
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {
		super.eventYouAreSelected(false);
		setTBelegdatumMwstsatz(tpAnfrage.getAnfrageDto().getTBelegdatum());
		// den gesamten Inhalt zuruecksetzen, ausloeser war ev. ein discard
		setDefaults();
		// position neu einlesen, ausloeser war ev. ein refresh
		Object pkPosition = getKeyWhenDetailPanel();

		if (pkPosition != null && !pkPosition.equals(LPMain.getLockMeForNew())) {
			// Position neu einlesen.
			anfragepositionDto = DelegateFactory.getInstance()
					.getAnfragepositionDelegate()
					.anfragepositionFindByPrimaryKey((Integer) pkPosition);
			// die Anfrage fuer die Statusbar neu einlesen
			tpAnfrage.setAnfrageDto(DelegateFactory.getInstance()
					.getAnfrageDelegate()
					.anfrageFindByPrimaryKey(anfragepositionDto.getBelegIId()));
			dto2Components();
		} else {
			panelArtikel.setLetzteArtikelCNr();
		}

		/**
		 * @todo nicht hier, sondern in der TP
		 */
		tpAnfrage.setTitleAnfrage(LPMain
				.getTextRespectUISPr("anf.panel.positionen"));

		// wenn die Anfrage gerade von mir gelockt ist, die Artikeleingabefelder
		// freischalten
		if (getLockedstateDetailMainKey().getIState() == PanelBasis.LOCK_IS_LOCKED_BY_ME) {
			panelArtikel.setArtikelEingabefelderEditable(true);
		}

		setzePositionsartAenderbar(anfragepositionDto);
		panelArtikel.setzeEinheitAenderbar();

		aktualisiereStatusbar();
		tpAnfrage.enableLieferdaten(getLockedstateDetailMainKey());
	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {
		try {
			calculateFields();

			if (allMandatoryFieldsSetDlg()) {

				components2Dto();

				boolean bDiePositionSpeichern = true;

				bDiePositionSpeichern = HelperClient
						.checkNumberFormat(panelArtikel.wnfNettopreis
								.getBigDecimal());

				if (bDiePositionSpeichern) {

					// PJ 16966

					boolean bZertifiziert = pruefeObZertifiziert(
							anfragepositionDto.getArtikelIId(), tpAnfrage.getLieferantDto());

					if (bZertifiziert == false) {
						boolean b = DialogFactory
								.showModalJaNeinDialog(
										getInternalFrame(),
										LPMain.getTextRespectUISPr("part.lieferant.nichtzertifiziert.trotzdem"));

						if (b == false) {
							return;
						}
					}

					if (anfragepositionDto.getIId() == null) {

						// Soll die neue Position vor der aktuell selektierten
						// stehen?
						if (bFuegeNeuePositionVorDerSelektiertenEin) {
							Integer iIdAktuellePosition = (Integer) tpAnfrage
									.getAnfragePositionenTop().getSelectedId();

							// die erste Position steht an der Stelle 1
							Integer iSortAktuellePosition = new Integer(1);

							if (iIdAktuellePosition != null) {
								iSortAktuellePosition = DelegateFactory
										.getInstance()
										.getAnfragepositionDelegate()
										.anfragepositionFindByPrimaryKey(
												iIdAktuellePosition).getISort();

								// Die bestehenden Positionen muessen Platz fuer
								// die neue schaffen
								DelegateFactory
										.getInstance()
										.getAnfragepositionDelegate()
										.sortierungAnpassenBeiEinfuegenEinerPositionVorPosition(
												tpAnfrage.getAnfrageDto()
														.getIId(),
												iSortAktuellePosition
														.intValue());
							}

							// Die neue Position wird an frei gemachte Position
							// gesetzt
							anfragepositionDto.setISort(iSortAktuellePosition);
						}

						// wir legen eine neue Position an
						Integer pkPosition = DelegateFactory.getInstance()
								.getAnfragepositionDelegate()
								.createAnfrageposition(anfragepositionDto);

						anfragepositionDto = DelegateFactory.getInstance()
								.getAnfragepositionDelegate()
								.anfragepositionFindByPrimaryKey(pkPosition);

						setKeyWhenDetailPanel(pkPosition);
					} else {
						DelegateFactory.getInstance()
								.getAnfragepositionDelegate()
								.updateAnfrageposition(anfragepositionDto);
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
	}

	protected void eventActionDelete(ActionEvent e,
			boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {
		if (tpAnfrage.istAktualisierenAnfrageErlaubt()) {
			DelegateFactory.getInstance().getAnfragepositionDelegate()
					.removeAnfrageposition(anfragepositionDto);
			this.setKeyWhenDetailPanel(null);
			super.eventActionDelete(e, false, false); // keyWasForLockMe nicht
														// ueberschreiben
		}
	}

	private void dto2Components() throws Throwable {
		// 1. Behandlung der trivialen Positionsarten.
		super.dto2Components(anfragepositionDto, tpAnfrage.getLieferantDto()
				.getPartnerDto().getLocaleCNrKommunikation());
		// 2. Weiter mit den anderen.
		String positionsart = anfragepositionDto.getPositionsartCNr();

		if (positionsart
				.equalsIgnoreCase(AnfrageServiceFac.ANFRAGEPOSITIONART_IDENT)) {
			panelArtikel.wnfEinzelpreis.setBigDecimal(anfragepositionDto
					.getNRichtpreis());
		} else if (positionsart
				.equalsIgnoreCase(AnfrageServiceFac.ANFRAGEPOSITIONART_HANDEINGABE)) {
			panelHandeingabe.wnfEinzelpreis.setBigDecimal(anfragepositionDto
					.getNRichtpreis());
		}
	}

	/**
	 * Alle Positionsdaten aus dem Panel sammeln.
	 * 
	 * @throws Throwable
	 */
	private void components2Dto() throws Throwable {
		// 1. Behandlung der trivialen Positionsarten.
		super.components2Dto(anfragepositionDto, tpAnfrage.getLieferantDto()
				.getPartnerDto().getLocaleCNrKommunikation(), tpAnfrage
				.getAnfrageDto().getIId());
		// 2. Weiter mit den anderen.

		String positionsart = getPositionsartCNr();

		if (positionsart
				.equalsIgnoreCase(AnfrageServiceFac.ANFRAGEPOSITIONART_IDENT)) {
			anfragepositionDto.setNRichtpreis(panelArtikel.wnfEinzelpreis
					.getBigDecimal());
		} else if (positionsart
				.equalsIgnoreCase(AnfrageServiceFac.ANFRAGEPOSITIONART_HANDEINGABE)) {
			anfragepositionDto.setNRichtpreis(panelHandeingabe.wnfEinzelpreis
					.getBigDecimal());
		}
	}

	private void aktualisiereStatusbar() throws Throwable {
		setStatusbarPersonalIIdAnlegen(tpAnfrage.getAnfrageDto()
				.getPersonalIIdAnlegen());
		setStatusbarTAnlegen(tpAnfrage.getAnfrageDto().getTAnlegen());
		setStatusbarPersonalIIdAendern(tpAnfrage.getAnfrageDto()
				.getPersonalIIdAendern());
		setStatusbarTAendern(tpAnfrage.getAnfrageDto().getTAendern());
		setStatusbarStatusCNr(tpAnfrage.getAnfrageStatus());
	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_ANFRAGE;
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wcoPositionsart;
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

	/**
	 * Drucke Anfrage.
	 * 
	 * @param e
	 *            Ereignis
	 * @throws Throwable
	 */
	protected void eventActionPrint(ActionEvent e) throws Throwable {
		tpAnfrage.printAnfrage();
		eventYouAreSelected(false);
	}

	// statuspositionen: 0 getLockedstateDetailMainKey ueberschreiben
	public LockStateValue getLockedstateDetailMainKey() throws Throwable {

		LockStateValue lsv = super.getLockedstateDetailMainKey();

		if (tpAnfrage.getAnfrageDto().getIId() != null) {
			if (tpAnfrage.getAnfrageDto().getStatusCNr()
					.equals(AnfrageServiceFac.ANFRAGESTATUS_STORNIERT)
					|| tpAnfrage.getAnfrageDto().getStatusCNr()
							.equals(AnfrageServiceFac.ANFRAGESTATUS_ERFASST)
					|| tpAnfrage.getAnfrageDto().getStatusCNr()
							.equals(AnfrageServiceFac.ANFRAGESTATUS_ERLEDIGT)) {
				lsv = new LockStateValue(
						PanelBasis.LOCK_ENABLE_REFRESHANDPRINT_ONLY);
			}
		}

		return lsv;
	}

	// statuspositionen: X
	protected void eventActionRefresh(ActionEvent e, boolean bNeedNoRefreshI)
			throws Throwable {
		super.eventActionRefresh(e, bNeedNoRefreshI);

		tpAnfrage.enableLieferdaten();
	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;
	}

}
