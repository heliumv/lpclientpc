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
package com.lp.client.bestellung;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.math.BigDecimal;
import java.sql.Date;
import java.text.MessageFormat;
import java.util.EventObject;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.lp.client.artikel.ArtikelFilterFactory;
import com.lp.client.eingangsrechnung.EingangsrechnungFilterFactory;
import com.lp.client.frame.Defaults;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperDateField;
import com.lp.client.frame.component.WrapperGotoButton;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPButtonAction;
import com.lp.client.pc.LPMain;
import com.lp.server.artikel.service.LagerDto;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.bestellung.service.BestellungDto;
import com.lp.server.bestellung.service.BestellungFac;
import com.lp.server.bestellung.service.WareneingangDto;
import com.lp.server.bestellung.service.WareneingangFac;
import com.lp.server.eingangsrechnung.service.EingangsrechnungDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.system.service.WechselkursDto;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;

/**
 * <p>
 * <I>Diese Klasse kuemmert sich die WEs.</I>
 * </p>
 * 
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2008
 * </p>
 * 
 * <p>
 * Erstellungsdatum <I>18.02.05</I>
 * </p>
 * 
 * <p>
 * </p>
 * 
 * @author josef erlinger
 * @version $Revision: 1.21 $
 */
public class PanelBestellungWareneingang extends PanelBasis {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// Soll eine neue Position vor der aktuell selektierten eingefuegt werden.
	protected boolean bFuegeNeuePositionVorDerSelektiertenEin = false;
	private WrapperGotoButton wbuEingangsrechnung = null;
	private WrapperTextField wtfEingangsrechnungNummer = null;
	private WrapperTextField wtfEingangsrechnungLFRENr = null;

	// wird benoetigt in der eventActionSave initialisiert wird es im
	// Konstruktor
	private final TabbedPaneBestellung tpBestellung;

	private JPanel jpaWorkingOn = null;

	private WrapperLabel wlaLieferscheinbeschreibung = new WrapperLabel();
	private WrapperTextField wtfLieferscheinbeschreibung = new WrapperTextField();

	private WrapperLabel wlaLieferscheinDatum = new WrapperLabel();
	private WrapperDateField wdfLieferscheinDatum = new WrapperDateField();

	private WrapperLabel wlaWareneingangsDatum = new WrapperLabel();
	public WrapperDateField wdfWareneingangsDatum = new WrapperDateField();

	private WrapperLabel wlaTransportkosten = new WrapperLabel();
	private WrapperButton wbuTransportkostenwaehrung = new WrapperButton();
	public WrapperNumberField wnfTransportkosten = new WrapperNumberField();
	private WrapperLabel wlaZollkosten = new WrapperLabel();
	private WrapperLabel wlaZollkostenwaehrung = new WrapperLabel();
	public WrapperNumberField wnfZollkosten = new WrapperNumberField();
	private WrapperLabel wlaBankspesen = new WrapperLabel();
	private WrapperLabel wlaBankspesenwaehrung = new WrapperLabel();
	public WrapperNumberField wnfBankspesen = new WrapperNumberField();
	private WrapperLabel wlaSonstigespesen = new WrapperLabel();
	private WrapperLabel wlaSonstigespesenwaehrung = new WrapperLabel();
	public WrapperNumberField wnfSonstigespesen = new WrapperNumberField();

	private WrapperLabel wlaGemeinKostenFaktor = new WrapperLabel();
	private WrapperNumberField wnfGemeinKostenFaktor = new WrapperNumberField();
	private WrapperLabel wlaProzent = new WrapperLabel();
	private WrapperLabel wlaProzent1 = new WrapperLabel();

	private WrapperButton wbuLager = new WrapperButton();
	private WrapperTextField wtfLager = new WrapperTextField();

	private WrapperLabel wlaRabattsatz = new WrapperLabel();
	private WrapperNumberField wnfRabattsatz = new WrapperNumberField();

	private WrapperLabel wlaWechselkurs = null;

	static final private String ACTION_SPECIAL_FLR_LAGER = "action_special_flr_lager";
	static final private String ACTION_SPECIAL_FLR_EINGANGSRECHNUNG = "action_special_flr_eingangsrechnung";

	static final private String ACTION_SPECIAL_TP_FREMDWAEHRUNG = "ACTION_SPECIAL_TP_FREMDWAEHRUNG";

	private PanelQueryFLR panelQueryFLRLager = null;
	private PanelQueryFLR panelQueryFLREingangsrechnung = null;

	public PanelBestellungWareneingang(InternalFrame internalFrame,
			String add2TitleI, Object pk) throws Throwable {
		super(internalFrame, add2TitleI, pk);
		// initialisieren von TabbedPaneBestellung
		tpBestellung = getInternalFrameBestellung().getTabbedPaneBestellung();
		jbInit();
		initPanel();
		initComponents();
	}

	private void initPanel() {
		// Wenn der Benutzer keine Preise sehen darf, sind einige Felder nicht
		// sichtbar.
		if (!getInternalFrame().bRechtDarfPreiseSehenEinkauf) {
			wlaTransportkosten.setVisible(false);
			wnfTransportkosten.setVisible(false);
			wbuTransportkostenwaehrung.setVisible(false);

			wlaZollkosten.setVisible(false);
			wnfZollkosten.setVisible(false);
			wlaZollkostenwaehrung.setVisible(false);
			wlaSonstigespesen.setVisible(false);
			wnfSonstigespesen.setVisible(false);
			wlaSonstigespesenwaehrung.setVisible(false);
			wlaBankspesen.setVisible(false);
			wnfBankspesen.setVisible(false);
			wlaBankspesenwaehrung.setVisible(false);

			wlaGemeinKostenFaktor.setVisible(false);
			wnfGemeinKostenFaktor.setVisible(false);
			wlaRabattsatz.setVisible(false);
			wnfRabattsatz.setVisible(false);
			wlaProzent.setVisible(false);
			wlaProzent1.setVisible(false);
			// ERs darf man auch nicht zuordnen
			wbuEingangsrechnung.setActivatable(false);
		}
	}

	protected void eventActionUpdate(ActionEvent aE, boolean bNeedNoUpdateI)
			throws Throwable {
		String cNrStatus = tpBestellung.getBesDto().getStatusCNr();
		if (cNrStatus.equals(BestellungFac.BESTELLSTATUS_ERLEDIGT)) {
			boolean bStatus2Erledigt = bStatus2Erledigt = DialogFactory
					.showModalJaNeinDialog(
							getInternalFrame(),
							LPMain.getTextRespectUISPr("bs.erledigt2geliefert"),
							LPMain.getTextRespectUISPr("lp.hinweis"));

			if (bStatus2Erledigt) {

				// auch im Zwischenpuffer setzen
				getBestellungDto().setStatusCNr(
						BestellungFac.BESTELLSTATUS_GELIEFERT);
				DelegateFactory.getInstance().getBestellungDelegate()
						.updateBestellung(getBestellungDto());
				super.eventActionUpdate(aE, bNeedNoUpdateI);
			}
		} else {
			super.eventActionUpdate(aE, bNeedNoUpdateI);
		}
		refreshMyComponents();
	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		if (e.getActionCommand().equals(ACTION_SPECIAL_FLR_LAGER)) {
			Integer lagerIId = null;
			if (tpBestellung.getWareneingangDto() != null) {
				lagerIId = tpBestellung.getWareneingangDto().getLagerIId();
			}
			panelQueryFLRLager = ArtikelFilterFactory.getInstance()
					.createPanelFLRLager(getInternalFrame(), lagerIId);
			new DialogQuery(panelQueryFLRLager);
		} else if (e.getActionCommand().equals(
				ACTION_SPECIAL_FLR_EINGANGSRECHNUNG)) {
			erstelleEingangsrechnung();
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_TP_FREMDWAEHRUNG)) {
			DialogTransportkostenWaehrung dialog = new DialogTransportkostenWaehrung(
					this);
			LPMain.getInstance().getDesktop()
					.platziereDialogInDerMitteDesFensters(dialog);
			dialog.setVisible(true);
		}
	}

	private void setDefaults() throws Throwable {
		tpBestellung.setWareneingangDto(new WareneingangDto());
		leereAlleFelder(this);

		// datum vom heutigen Tag setzen
		wdfLieferscheinDatum.setDate(new java.sql.Date(System
				.currentTimeMillis()));
		wdfWareneingangsDatum.setDate(new java.sql.Date(System
				.currentTimeMillis()));
		tpBestellung.getWareneingangDto().setBestellungIId(
				getInternalFrameBestellung().getTabbedPaneBestellung()
						.getBesDto().getIId());

		WareneingangDto[] aWareneingangDto = DelegateFactory
				.getInstance()
				.getWareneingangDelegate()
				.wareneingangFindByBestellungIId(
						getInternalFrameBestellung().getTabbedPaneBestellung()
								.getBesDto().getIId());
		if (aWareneingangDto.length > 0) {
			LagerDto lagerDto = DelegateFactory.getInstance()
					.getLagerDelegate()
					.lagerFindByPrimaryKey(aWareneingangDto[0].getLagerIId());
			wtfLager.setText(lagerDto.getCNr());
			tpBestellung.getWareneingangDto().setLagerIId(lagerDto.getIId());
		} else {
			LagerDto lagerDto = DelegateFactory
					.getInstance()
					.getLagerDelegate()
					.lagerFindByPrimaryKey(
							tpBestellung.getLieferantDto()
									.getLagerIIdZubuchungslager());
			wtfLager.setText(lagerDto.getCNr());
			tpBestellung.getWareneingangDto().setLagerIId(lagerDto.getIId());
		}
		wnfRabattsatz.setBigDecimal(new BigDecimal(DelegateFactory
				.getInstance().getBestellungDelegate()
				.bestellungFindByPrimaryKey(getBestellungDto().getIId())
				.getFAllgemeinerRabattsatz()));
		// holen des Gemeinkostenfaktors
		ParametermandantDto pm = DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getMandantparameter(LPMain.getTheClient().getMandant(),
						ParameterFac.KATEGORIE_ALLGEMEIN,
						ParameterFac.PARAMETER_GEMEINKOSTENFAKTOR,wdfWareneingangsDatum.getTimestamp());
		Double gkdouble = new Double(pm.getCWert());
		wnfGemeinKostenFaktor.setDouble(gkdouble);

		wbuTransportkostenwaehrung.setText(getBestellungDto()
				.getWaehrungCNr());
		wlaBankspesenwaehrung.setText(getBestellungDto()
				.getWaehrungCNr());
		wlaZollkostenwaehrung.setText(getBestellungDto()
				.getWaehrungCNr());
		wlaSonstigespesenwaehrung.setText(getBestellungDto()
				.getWaehrungCNr());
		wbuEingangsrechnung.setEnabled(false);
	}

	private InternalFrameBestellung getInternalFrameBestellung() {
		return (InternalFrameBestellung) getInternalFrame();
	}

	protected void eventActionRefresh(ActionEvent e, boolean bNeedNoRefreshI)
			throws Throwable {

		super.eventActionRefresh(e, bNeedNoRefreshI);

		refreshMyComponents();

		tpBestellung.enablePanels(tpBestellung.getBesDto(), true);
		if (tpBestellung.getWareneingangDto() != null) {
			if (DelegateFactory
					.getInstance()
					.getWareneingangDelegate()
					.allePreiseFuerWareneingangErfasst(
							tpBestellung.getWareneingangDto().getIId())) {
				wbuEingangsrechnung.setEnabled(true);
			} else {
				wbuEingangsrechnung.setEnabled(false);
			}
		}
	}

	public void eventActionNew(EventObject eventObject, boolean bLockMeI,
			boolean bNeedNoNewI) throws Throwable {
		if (tpBestellung.getBesDto().getStatusCNr()
				.equals(BestellungFac.BESTELLSTATUS_GELIEFERT)) {
			DialogFactory.showModalDialog(
					LPMain.getTextRespectUISPr("lp.hinweis"),
					LPMain.getTextRespectUISPr("bes.noupdate"));
			return;
		}
		super.eventActionNew(eventObject, true, false);
		// hier wird aktuelles datum beim neuanlegen gesetzt
		setDefaults();

		// 2 die neue Position soll vor der aktuell selektierten eingefuegt
		// werden
		if (((ItemChangedEvent) eventObject).getID() == ItemChangedEvent.ACTION_POSITION_VORPOSITIONEINFUEGEN) {
			// Dieses Flag gibt an, ob die neue Position vor der aktuellen
			// eingefuegt werden soll
			bFuegeNeuePositionVorDerSelektiertenEin = true;
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

		tpBestellung.enablePanels(tpBestellung.getBesDto(), true);

		// if
		// (tpBestellung.getBestellungDto().getBestellungsstatusCNr().equals(BestellungFac.
		// BESTELLSTATUS_OFFEN) ||
		// tpBestellung.getBestellungDto().getBestellungsstatusCNr().
		// equals(BestellungFac.BESTELLSTATUS_BESTAETIGT)) {
		// tpBestellung.enableWareneingangPanels();
		// tpBestellung.enablePanels(null);
		// }
		// else {
		// tpBestellung.enableWE(true);
		// tpBestellung.enableWEP(true);
		// tpBestellung.enablePanels(null);
		// }
		// tpBestellung.enableSichtLieferantTermine();

		// per Default wird eine Position hinten angefuegt
		bFuegeNeuePositionVorDerSelektiertenEin = false;
	}

	protected void eventActionDelete(ActionEvent e,
			boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {
		DelegateFactory.getInstance().getWareneingangDelegate()
				.removeWareneingang(tpBestellung.getWareneingangDto());
		super.eventActionDelete(e, false, false);
	}

	private void setWechselkursVonBestellwaehrungNachMandantwaehrung()
			throws Throwable {
		String sMandantWaehrung = LPMain.getTheClient().getSMandantenwaehrung();
		if (tpBestellung.getBesDto().getWaehrungCNr()
				.equals(sMandantWaehrung)) {
			// gleich wie Mandantenwaehrung -> Kurs = 1.
			tpBestellung.getWareneingangDto()
					.setNWechselkurs(new BigDecimal(1));
		} else {
			// aktuellen Kurs zur Mandantenwaehrung holen.
			WechselkursDto wechselkursDto = DelegateFactory
					.getInstance()
					.getLocaleDelegate()
					.getKursZuDatum(
							sMandantWaehrung,
							tpBestellung.getBesDto()
									.getWaehrungCNr(),
							new Date(tpBestellung.getWareneingangDto()
									.getTWareneingangsdatum().getTime()));
			tpBestellung.getWareneingangDto().setNWechselkurs(
					wechselkursDto.getNKurs());
		}
	}

	protected void components2Dto() throws Throwable {
		ParametermandantDto pm = DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getMandantparameter(LPMain.getTheClient().getMandant(),
						ParameterFac.KATEGORIE_ALLGEMEIN,
						ParameterFac.PARAMETER_GEMEINKOSTENFAKTOR,wdfWareneingangsDatum.getTimestamp());

		Double gkdouble = new Double(pm.getCWert());
		/**
		 * @todo MB wirklich? das koennte den Kurs ueberschreiben
		 */

		// eine neue Position bekommt ein eindeutiges iSort
		// WareneingangDelegate verwenden und die BestellungIID

		tpBestellung.getWareneingangDto().setCLieferscheinnr(
				wtfLieferscheinbeschreibung.getText());
		tpBestellung.getWareneingangDto().setTLieferscheindatum(
				wdfLieferscheinDatum.getTimestamp());
		tpBestellung.getWareneingangDto().setTWareneingangsdatum(
				wdfWareneingangsDatum.getTimestamp());
		setWechselkursVonBestellwaehrungNachMandantwaehrung();
		if (wnfTransportkosten.getBigDecimal() == null) {
			tpBestellung.getWareneingangDto().setNTransportkosten(
					new BigDecimal(0));
		} else {
			tpBestellung.getWareneingangDto().setNTransportkosten(
					wnfTransportkosten.getBigDecimal());
		}
		if (wnfBankspesen.getBigDecimal() == null) {
			tpBestellung.getWareneingangDto().setNBankspesen(new BigDecimal(0));
		} else {
			tpBestellung.getWareneingangDto().setNBankspesen(
					wnfBankspesen.getBigDecimal());
		}
		if (wnfZollkosten.getBigDecimal() == null) {
			tpBestellung.getWareneingangDto().setNZollkosten(new BigDecimal(0));
		} else {
			tpBestellung.getWareneingangDto().setNZollkosten(
					wnfZollkosten.getBigDecimal());
		}
		if (wnfSonstigespesen.getBigDecimal() == null) {
			tpBestellung.getWareneingangDto().setNSonstigespesen(
					new BigDecimal(0));
		} else {
			tpBestellung.getWareneingangDto().setNSonstigespesen(
					wnfSonstigespesen.getBigDecimal());
		}

		tpBestellung.getWareneingangDto().setDGemeinkostenfaktor(gkdouble);
		tpBestellung.getWareneingangDto().setFRabattsatz(
				wnfRabattsatz.getDouble());

	}

	protected void dto2Components() throws Throwable {
		wnfGemeinKostenFaktor.setDouble(tpBestellung.getWareneingangDto()
				.getDGemeinkostenfaktor());
		wtfLieferscheinbeschreibung.setText(tpBestellung.getWareneingangDto()
				.getCLieferscheinnr());
		wdfLieferscheinDatum.setTimestamp(tpBestellung.getWareneingangDto()
				.getTLieferscheindatum());
		wdfWareneingangsDatum.setTimestamp(tpBestellung.getWareneingangDto()
				.getTWareneingangsdatum());
		wnfTransportkosten.setBigDecimal(tpBestellung.getWareneingangDto()
				.getNTransportkosten());

		wnfZollkosten.setBigDecimal(tpBestellung.getWareneingangDto()
				.getNZollkosten());
		wnfBankspesen.setBigDecimal(tpBestellung.getWareneingangDto()
				.getNBankspesen());
		wnfSonstigespesen.setBigDecimal(tpBestellung.getWareneingangDto()
				.getNSonstigespesen());

		wnfRabattsatz.setDouble(tpBestellung.getWareneingangDto()
				.getFRabattsatz());
		wtfLager.setText(DelegateFactory
				.getInstance()
				.getLagerDelegate()
				.lagerFindByPrimaryKey(
						tpBestellung.getWareneingangDto().getLagerIId())
				.getCNr());
		if (tpBestellung.getWareneingangDto().getEingangsrechnungIId() != null) {
			wbuEingangsrechnung.setOKey(tpBestellung.getWareneingangDto()
					.getEingangsrechnungIId());
			EingangsrechnungDto erDto = DelegateFactory
					.getInstance()
					.getEingangsrechnungDelegate()
					.eingangsrechnungFindByPrimaryKey(
							tpBestellung.getWareneingangDto()
									.getEingangsrechnungIId());
			wtfEingangsrechnungNummer.setText(erDto.getCNr());
			wtfEingangsrechnungLFRENr.setText(erDto
					.getCLieferantenrechnungsnummer());
		} else {
			wbuEingangsrechnung.setOKey(null);
			wtfEingangsrechnungNummer.setText(null);
			wtfEingangsrechnungLFRENr.setText(null);
		}
		setStatusbarSpalte5(LPMain
				.getTextRespectUISPr("bes.wareneingang.wertsumme")
				+ ": "
				+ tpBestellung.getWareneingangWertsumme(tpBestellung
						.getWareneingangDto()));
	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {
		if (allMandatoryFieldsSetDlg()) {
			try {
				components2Dto();
				if (tpBestellung.getWareneingangDto().getTWareneingangsdatum()
						.before(tpBestellung.getBesDto().getDBelegdatum())) {
					DialogFactory
							.showModalDialog(
									LPMain.getTextRespectUISPr("lp.hinweis"),
									LPMain.getTextRespectUISPr("bes.wareneingang.datumvorbelegdatum"));
				}

				BigDecimal spesen = tpBestellung
						.getWareneingangDto()
						.getNTransportkosten()
						.add(tpBestellung
								.getWareneingangDto()
								.getNBankspesen()
								.add(tpBestellung
										.getWareneingangDto()
										.getNZollkosten()
										.add(tpBestellung.getWareneingangDto()
												.getNSonstigespesen())));

				BigDecimal wertsumme = tpBestellung
						.getWareneingangWertsumme(tpBestellung
								.getWareneingangDto());

				if (wertsumme == null) {
					wertsumme = new BigDecimal(0);
				}
				if (wertsumme.add(spesen).doubleValue() < 0) {

					DialogFactory
							.showModalDialog(
									LPMain.getTextRespectUISPr("lp.hinweis"),
									LPMain.getTextRespectUISPr("bes.wareneingang.error.wertsumme"));
					return;
				}

				if (tpBestellung.getWareneingangDto().getIId() == null) {
					// Soll die neue Position vor der aktuell selektierten
					// stehen?
					if (bFuegeNeuePositionVorDerSelektiertenEin) {
						Integer iIdSelektierterWE = (Integer) tpBestellung
								.getWareneingangTop().getSelectedId();
						if (iIdSelektierterWE != null) {
							Integer iSort = DelegateFactory
									.getInstance()
									.getWareneingangDelegate()
									.wareneingangFindByPrimaryKey(
											iIdSelektierterWE).getISort();
							tpBestellung.getWareneingangDto().setISort(iSort);
						}
					}
					// wir legen einen neuen WE an
					Integer pkPosition = DelegateFactory
							.getInstance()
							.getWareneingangDelegate()
							.createWareneingang(
									tpBestellung.getWareneingangDto());
					tpBestellung.setWareneingangDto(DelegateFactory
							.getInstance().getWareneingangDelegate()
							.wareneingangFindByPrimaryKey(pkPosition));
					setKeyWhenDetailPanel(pkPosition);
					// Alle Positionen die nicht Ident oder Handeingabe sind auf
					// erledigt setzen
					DelegateFactory
							.getInstance()
							.getBestellungDelegate()
							.erledigeAlleNichtMengenpositionen(
									tpBestellung.getBesDto());
				} else {
					DelegateFactory
							.getInstance()
							.getWareneingangDelegate()
							.updateWareneingang(
									tpBestellung.getWareneingangDto());
				}
				super.eventActionSave(e, true);
				eventYouAreSelected(false);
			} finally {
				// per Default wird eine neue Position ans Ende der Liste
				// gesetzt
				bFuegeNeuePositionVorDerSelektiertenEin = false;
			}
		}
	}

	protected void eventItemchanged(EventObject eI) throws Throwable {

		ItemChangedEvent e = (ItemChangedEvent) eI;
		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			if (e.getSource() == panelQueryFLRLager) {
				Integer iId = (Integer) ((ISourceEvent) e.getSource())
						.getIdSelected();
				// hier wird das lager vom server ueber id geholt und im
				// lagerDto gespeichert
				LagerDto lagerDto = DelegateFactory.getInstance()
						.getLagerDelegate().lagerFindByPrimaryKey(iId);
				// hier wird ID gesetzt
				tpBestellung.getWareneingangDto().setLagerIId(iId);
				wtfLager.setText(lagerDto.getCNr());
			} else if (e.getSource() == panelQueryFLREingangsrechnung) {
				Integer iId = (Integer) ((ISourceEvent) e.getSource())
						.getIdSelected();
				EingangsrechnungDto erDto = DelegateFactory.getInstance()
						.getEingangsrechnungDelegate()
						.eingangsrechnungFindByPrimaryKey(iId);
				// hier wird ID gesetzt
				tpBestellung.getWareneingangDto().setEingangsrechnungIId(iId);
				wtfEingangsrechnungNummer.setText(erDto.getCNr());
				wtfEingangsrechnungLFRENr.setText(erDto
						.getCLieferantenrechnungsnummer());
				wbuEingangsrechnung.setOKey(erDto.getIId());
				tpBestellung.getWareneingangDto().setEingangsrechnungIId(
						erDto.getIId());
				DelegateFactory.getInstance().getWareneingangDelegate()
						.updateWareneingang(tpBestellung.getWareneingangDto());
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_LEEREN) {
			if (e.getSource() == panelQueryFLREingangsrechnung) {
				tpBestellung.getWareneingangDto().setEingangsrechnungIId(null);
				wtfEingangsrechnungNummer.setText(null);
				wtfEingangsrechnungLFRENr.setText(null);
				wbuEingangsrechnung.setOKey(null);
				tpBestellung.getWareneingangDto().setEingangsrechnungIId(null);
				DelegateFactory.getInstance().getWareneingangDelegate()
						.updateWareneingang(tpBestellung.getWareneingangDto());
			}
		}
	}

	private void jbInit() throws Throwable {
		getInternalFrame().addItemChangedListener(this);

		String[] aWhichButtonIUse = { ACTION_UPDATE, ACTION_SAVE,
				ACTION_DELETE, ACTION_DISCARD, };

		enableToolsPanelButtons(aWhichButtonIUse);

		setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		// das Aussenpanel hat immer das Gridbaglayout.
		setLayout(new GridBagLayout());

		// Actionpanel von Oberklasse holen und anhaengen.
		add(getToolsPanel(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0,
						0, 0, 0), 0, 0));

		wlaLieferscheinbeschreibung.setText(LPMain
				.getTextRespectUISPr("bes.lieferscheinnummer"));
		wtfLieferscheinbeschreibung
				.setColumnsMax(WareneingangFac.MAX_LIEFERSCHEINNUMMER);
		wtfLieferscheinbeschreibung.setMandatoryField(true);

		wlaLieferscheinDatum.setText(LPMain
				.getTextRespectUISPr("bes.lieferscheindatum"));
		wdfLieferscheinDatum.setMandatoryField(true);

		wlaWareneingangsDatum.setText(LPMain
				.getTextRespectUISPr("bes.wareneingangsdatum"));
		wdfWareneingangsDatum.setMandatoryField(true);

		wlaTransportkosten.setText(LPMain
				.getTextRespectUISPr("bes.transportkosten"));
		wnfTransportkosten.setMinimumValue(0);
		wnfTransportkosten.setFractionDigits(Defaults.getInstance()
				.getIUINachkommastellenPreiseWE());
		wbuTransportkostenwaehrung
				.setHorizontalAlignment(SwingConstants.LEADING);
		wbuTransportkostenwaehrung.addActionListener(this);
		wbuTransportkostenwaehrung
				.setActionCommand(ACTION_SPECIAL_TP_FREMDWAEHRUNG);

		wlaZollkosten.setText(LPMain.getTextRespectUISPr("bes.zollkosten"));
		wnfZollkosten.setMinimumValue(0);
		wnfZollkosten.setFractionDigits(Defaults.getInstance()
				.getIUINachkommastellenPreiseWE());
		wlaZollkostenwaehrung.setHorizontalAlignment(SwingConstants.LEADING);

		wlaSonstigespesen.setText(LPMain
				.getTextRespectUISPr("bes.sonstigespesen"));
		wnfSonstigespesen.setFractionDigits(Defaults.getInstance()
				.getIUINachkommastellenPreiseWE());
		wlaSonstigespesenwaehrung
				.setHorizontalAlignment(SwingConstants.LEADING);

		wlaBankspesen.setText(LPMain.getTextRespectUISPr("bes.bankspesen"));
		wnfBankspesen.setMinimumValue(0);
		wnfBankspesen.setFractionDigits(Defaults.getInstance()
				.getIUINachkommastellenPreiseWE());
		wlaBankspesenwaehrung.setHorizontalAlignment(SwingConstants.LEADING);

		wlaGemeinKostenFaktor.setText(LPMain
				.getTextRespectUISPr("bes.gemeinkostenfaktor"));
		wnfGemeinKostenFaktor.setMinimumValue(0);
		wnfGemeinKostenFaktor.setMaximumValue(100);
		wnfGemeinKostenFaktor.setActivatable(false);

		wlaProzent1.setText(LPMain.getTextRespectUISPr("label.prozent"));
		wlaProzent1.setHorizontalAlignment(SwingConstants.LEADING);

		wlaProzent.setText(LPMain.getTextRespectUISPr("label.prozent"));
		wlaProzent.setHorizontalAlignment(SwingConstants.LEADING);

		wbuLager.setText(LPMain.getTextRespectUISPr("button.lager"));
		wbuLager.setToolTipText(LPMain
				.getTextRespectUISPr("button.lager.tooltip"));

		wbuLager.setActionCommand(ACTION_SPECIAL_FLR_LAGER);
		wbuLager.addActionListener(this);
		wtfLager.setColumnsMax(QueryParameters.FLR_BREITE_XL);
		wtfLager.setMandatoryFieldDB(true);
		wtfLager.setActivatable(false);

		wlaRabattsatz.setText(LPMain.getTextRespectUISPr("label.rabattsatz"));
		wnfRabattsatz.setColumns(QueryParameters.FLR_BREITE_XS);
		wnfRabattsatz.setMandatoryFieldDB(true);

		wlaWechselkurs = new WrapperLabel();
		wlaWechselkurs.setHorizontalAlignment(SwingConstants.LEADING);

		wbuEingangsrechnung = new WrapperGotoButton(
				LPMain.getTextRespectUISPr("button.eingangsrechnung"),
				WrapperGotoButton.GOTO_EINGANGSRECHNUNG_AUSWAHL);
		wbuEingangsrechnung.setToolTipText(LPMain
				.getTextRespectUISPr("button.eingangsrechnung.tooltip"));
		wbuEingangsrechnung
				.setActionCommand(ACTION_SPECIAL_FLR_EINGANGSRECHNUNG);
		wbuEingangsrechnung.addActionListener(this);
		wbuEingangsrechnung
				.setRechtCNr(RechteFac.RECHT_LP_DARF_PREISE_SEHEN_EINKAUF);
		wtfEingangsrechnungNummer = new WrapperTextField();
		wtfEingangsrechnungNummer.setActivatable(false);
		wtfEingangsrechnungLFRENr = new WrapperTextField();
		wtfEingangsrechnungLFRENr.setActivatable(false);

		if (!getInternalFrame().bRechtDarfPreiseAendernEinkauf) {
			wnfTransportkosten.setActivatable(false);
			wnfZollkosten.setActivatable(false);
			wnfSonstigespesen.setActivatable(false);
			wnfBankspesen.setActivatable(false);
			wnfGemeinKostenFaktor.setActivatable(false);
			wnfRabattsatz.setActivatable(false);
		}
		
		
		
		jpaWorkingOn = new JPanel(new GridBagLayout());
		add(jpaWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
				GridBagConstraints.SOUTHEAST, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));

		add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						0, 0, 0, 0), 0, 0));

		// ab hier einhaengen.

		// Zeile.
		iZeile++;
		jpaWorkingOn.add(wlaWechselkurs, new GridBagConstraints(0, iZeile, 3,
				1, 0.2, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(new WrapperLabel(), new GridBagConstraints(1, iZeile,
				1, 1, 0.2, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wlaTransportkosten, new GridBagConstraints(2, iZeile,
				1, 1, 0.2, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wnfTransportkosten, new GridBagConstraints(3, iZeile,
				1, 1, 0.05, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wbuTransportkostenwaehrung, new GridBagConstraints(4,
				iZeile, 1, 1, 0.02, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 35, 0));

		iZeile++;
		jpaWorkingOn.add(wlaLieferscheinbeschreibung, new GridBagConstraints(0,
				iZeile, 1, 1, 0.2, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfLieferscheinbeschreibung, new GridBagConstraints(1,
				iZeile, 1, 1, 0.2, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaZollkosten, new GridBagConstraints(2, iZeile, 1, 1,
				0.2, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wnfZollkosten, new GridBagConstraints(3, iZeile, 1, 1,
				0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaZollkostenwaehrung, new GridBagConstraints(4,
				iZeile, 1, 1, 0.02, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		// Zeile.
		iZeile++;
		jpaWorkingOn.add(wlaLieferscheinDatum, new GridBagConstraints(0,
				iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wdfLieferscheinDatum, new GridBagConstraints(1,
				iZeile, 1, 1, 0.2, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaBankspesen, new GridBagConstraints(2, iZeile, 1, 1,
				0.2, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wnfBankspesen, new GridBagConstraints(3, iZeile, 1, 1,
				0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaBankspesenwaehrung, new GridBagConstraints(4,
				iZeile, 1, 1, 0.02, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		// Zeile.
		iZeile++;
		jpaWorkingOn.add(wlaWareneingangsDatum, new GridBagConstraints(0,
				iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wdfWareneingangsDatum, new GridBagConstraints(1,
				iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaSonstigespesen, new GridBagConstraints(2, iZeile,
				1, 1, 0.2, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wnfSonstigespesen, new GridBagConstraints(3, iZeile,
				1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaSonstigespesenwaehrung, new GridBagConstraints(4,
				iZeile, 1, 1, 0.02, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		// Zeile.
		iZeile++;
		jpaWorkingOn.add(wlaGemeinKostenFaktor, new GridBagConstraints(2,
				iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wnfGemeinKostenFaktor, new GridBagConstraints(3,
				iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wlaProzent, new GridBagConstraints(4, iZeile, 1, 1, 0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));

		// Zeile.
		iZeile++;
		jpaWorkingOn.add(wbuLager, new GridBagConstraints(0, iZeile, 1, 1, 0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wtfLager, new GridBagConstraints(1, iZeile, 1, 1, 0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wlaRabattsatz, new GridBagConstraints(2, iZeile, 1, 1,
				0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wnfRabattsatz, new GridBagConstraints(3, iZeile, 1, 1,
				0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaProzent1, new GridBagConstraints(4, iZeile, 1, 1,
				0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wbuEingangsrechnung, new GridBagConstraints(0, iZeile,
				1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfEingangsrechnungNummer, new GridBagConstraints(1,
				iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn
				.add(new WrapperLabel(
						LPMain.getTextRespectUISPr("label.lieferantenrechnungsnummer")),
						new GridBagConstraints(2, iZeile, 1, 1, 0, 0.0,
								GridBagConstraints.CENTER,
								GridBagConstraints.BOTH,
								new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfEingangsrechnungLFRENr, new GridBagConstraints(3,
				iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_BESTELLUNG;
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {

		super.eventYouAreSelected(false);

		Object key = getKeyWhenDetailPanel();

		if (key == null || key.equals(LPMain.getLockMeForNew())) {
			// Create.
			clearStatusbar();
			leereAlleFelder(this);
			setDefaults();
		} else {
			// Update
			// holen der Waehrung von der zugehoerigen Bestellung
			wbuTransportkostenwaehrung.setText(getBestellungDto()
					.getWaehrungCNr());
			wlaBankspesenwaehrung.setText(getBestellungDto()
					.getWaehrungCNr());
			wlaZollkostenwaehrung.setText(getBestellungDto()
					.getWaehrungCNr());
			wlaSonstigespesenwaehrung.setText(getBestellungDto()
					.getWaehrungCNr());
			tpBestellung.setWareneingangDto(DelegateFactory.getInstance()
					.getWareneingangDelegate()
					.wareneingangFindByPrimaryKey((Integer) key));
			dto2Components();

			tpBestellung.enablePanels(tpBestellung.getBesDto(), true);
		}
		// Wechselkurs anzeigen
		dto2ComponentsWechselkurs();
		// Status setzen
		getPanelStatusbar().setStatusCNr(
				tpBestellung.getBesDto().getStatusCNr());

		refreshMyComponents();
		if (key instanceof Integer) {
			if (DelegateFactory.getInstance().getWareneingangDelegate()
					.allePreiseFuerWareneingangErfasst((Integer) key)) {
				wbuEingangsrechnung.setEnabled(true);
			} else {
				wbuEingangsrechnung.setEnabled(false);
			}
			if (wbuEingangsrechnung.getOKey() != null) {
				wbuEingangsrechnung.getWrapperButtonGoTo().setEnabled(true);
			}
		}
	}

	private void dto2ComponentsWechselkurs() throws Throwable {
		BigDecimal bdAngezeigterKurs;
		if (tpBestellung.getWareneingangDto() != null
				&& tpBestellung.getWareneingangDto().getNWechselkurs() != null) {
			bdAngezeigterKurs = tpBestellung.getWareneingangDto()
					.getNWechselkurs();
		} else {
			// noch kein WE eingetragen -> den Kurs des BS anzeigen
			bdAngezeigterKurs = new BigDecimal(tpBestellung.getBesDto()
					.getFWechselkursmandantwaehrungzubelegwaehrung());
			String sMandantWaehrung = LPMain.getTheClient()
					.getSMandantenwaehrung();
			WechselkursDto wechselkursDto = DelegateFactory
					.getInstance()
					.getLocaleDelegate()
					.getKursZuDatum(
							sMandantWaehrung,
							tpBestellung.getBesDto()
									.getWaehrungCNr(),
							new Date(System.currentTimeMillis()));
			if (wechselkursDto != null) {
				bdAngezeigterKurs = wechselkursDto.getNKurs();
			}
		}
		Object pattern[] = { LPMain.getTheClient().getSMandantenwaehrung(),
				LPMain.getTextRespectUISPr("lp.mandantwaehrung"),
				bdAngezeigterKurs,
				tpBestellung.getBesDto().getWaehrungCNr(),
				LPMain.getTextRespectUISPr("bes.waehrung") };
		String sText = LPMain.getTextRespectUISPr("bes.wechselkurs");
		sText = MessageFormat.format(sText, pattern);
		wlaWechselkurs.setText(sText);
	}

	private BestellungDto getBestellungDto() {
		return tpBestellung.getBesDto();
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wtfLieferscheinbeschreibung;
	}

	private void refreshMyComponents() throws Throwable {

		LPButtonAction item = null;

		int lockstate = getLockedstateDetailMainKey().getIState();
		boolean bEnable = !tpBestellung.getBesDto().getStatusCNr()
				.equals(BestellungFac.BESTELLSTATUS_ERLEDIGT)
				&& !tpBestellung.getBesDto().getStatusCNr()
						.equals(BestellungFac.BESTELLSTATUS_GELIEFERT)
				&& lockstate != LOCK_FOR_EMPTY
				&& lockstate != LOCK_FOR_NEW
				&& lockstate != LOCK_IS_LOCKED_BY_ME;

		item = (LPButtonAction) getHmOfButtons().get(PanelBasis.ACTION_DELETE);
		item.getButton().setEnabled(bEnable);
		bEnable = (tpBestellung.getWareneingangDto().getIId() == null || // neu
				(DelegateFactory
						.getInstance()
						.getWareneingangDelegate()
						.getAnzahlWEP(
								tpBestellung.getWareneingangDto().getIId())
						.intValue() == 0))
				&& (lockstate == LOCK_FOR_EMPTY || lockstate == LOCK_FOR_NEW || lockstate == LOCK_IS_LOCKED_BY_ME);
		// Das Lager darf solange veraendert werden, solange es noch keine WEP
		// gibt.
		int iAnzahlWEP = 0;
		if (tpBestellung.getWareneingangDto() != null
				&& tpBestellung.getWareneingangDto().getIId() != null) {
			iAnzahlWEP = DelegateFactory.getInstance()
					.getWareneingangDelegate()
					.getAnzahlWEP(tpBestellung.getWareneingangDto().getIId());
		}
		boolean bEdit = lockstate == LOCK_FOR_NEW
				|| lockstate == LOCK_IS_LOCKED_BY_ME;
		wbuLager.setEnabled(bEdit && iAnzahlWEP == 0);
		boolean bEditEingangsrechnung = (lockstate != LOCK_FOR_NEW && lockstate == LOCK_IS_LOCKED_BY_ME);
		// wbuEingangsrechnung.setEnabled(bEditEingangsrechnung);
		if (bEditEingangsrechnung) {
			if (tpBestellung.getWareneingangDto().getIId() != null) {
				if (DelegateFactory
						.getInstance()
						.getWareneingangDelegate()
						.allePreiseFuerWareneingangErfasst(
								tpBestellung.getWareneingangDto().getIId())) {
					wbuEingangsrechnung.setEnabled(true);
				} else {
					wbuEingangsrechnung.setEnabled(false);
				}
			} else {
				wbuEingangsrechnung.setEnabled(false);
			}
		}
		// Status setzen
		getPanelStatusbar().setStatusCNr(
				tpBestellung.getBesDto().getStatusCNr());

		/**
		 * @todo nicht hier - alle referenzen anschaun.
		 */
		// titel
		getTabbedPaneBestellung().setTitle();
	}

	public TabbedPaneBestellung getTabbedPaneBestellung() {
		return getInternalFrameBestellung().getTabbedPaneBestellung();
	}

	private void erstelleEingangsrechnung() throws Throwable {
		// zur Zeit nur Auswahl einer bestehenden ER.
		// Auswahl bestehnder ER mit derselben LieferantenIId wie Bestellung
		wtfEingangsrechnungNummer.setText(null);
		wtfEingangsrechnungLFRENr.setText(null);
		panelQueryFLREingangsrechnung = EingangsrechnungFilterFactory
				.getInstance()
				.createPanelFLREingangsrechnungWareneingangGoto(
						getInternalFrame(),
						tpBestellung.getWareneingangDto()
								.getEingangsrechnungIId(),
						tpBestellung.getBesDto()
								.getLieferantIIdRechnungsadresse(), false, true);
		new DialogQuery(panelQueryFLREingangsrechnung);
	}
}