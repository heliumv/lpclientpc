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
package com.lp.client.eingangsrechnung;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.EventObject;
import java.util.HashMap;

import com.lp.client.finanz.FinanzFilterFactory;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.PanelZahlung;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.delegate.EingangsrechnungDelegate;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPButtonAction;
import com.lp.client.pc.LPMain;
import com.lp.server.eingangsrechnung.service.EingangsrechnungDto;
import com.lp.server.eingangsrechnung.service.EingangsrechnungFac;
import com.lp.server.eingangsrechnung.service.EingangsrechnungzahlungDto;
import com.lp.server.finanz.service.BuchenFac;
import com.lp.server.finanz.service.FinanzFac;
import com.lp.server.finanz.service.KontoDto;
import com.lp.server.partner.service.LieferantDto;
import com.lp.server.rechnung.service.RechnungDto;
import com.lp.server.rechnung.service.RechnungFac;
import com.lp.server.rechnung.service.RechnungzahlungDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.system.service.WechselkursDto;
import com.lp.util.Helper;

//@SuppressWarnings("static-access")
/*
 * <p>Panel zum Bearbeiten der Kopfdaten einer Zahlung</p> <p>Copyright Logistik
 * Pur Software GmbH (c) 2004-2008</p> <p>Erstellungsdatum <I>06. 04.
 * 2005</I></p> <p> </p>
 * 
 * @author Martin Bluehweis
 * 
 * @version $Revision: 1.29 $
 */
public class PanelEingangsrechnungZahlung extends PanelZahlung {
	private static final long serialVersionUID = 1L;

//	private TabbedPaneEingangsrechnung tabbedPaneRechnungAll = null;

	private EingangsrechnungzahlungDto zahlungDto = null;

	private EingangsrechnungDto erGutschriftDto = null;
	private EingangsrechnungzahlungDto erGutschriftZahlungDto = null;
	private WrapperCheckBox wcbKursUebersteuert = null;
	private final static String ACTION_SPECIAL_KURSUEBERSTEUERT = "ACTION_SPECIAL_KURSUEBERSTEUERT";
	private boolean bMitKurseingabe = false;
	private HashMap<String, BigDecimal> hmKurse = new HashMap<String, BigDecimal>();

	public PanelEingangsrechnungZahlung(InternalFrame internalFrame,
			String add2TitleI, TabbedPaneEingangsrechnung tabbedPaneRechnungAll)
			throws Throwable {
		super(internalFrame, tabbedPaneRechnungAll, add2TitleI);
//		this.tabbedPaneRechnungAll = tabbedPaneRechnungAll;
		jbInit();
		initComponents();
	}

	private TabbedPaneEingangsrechnung getTabbedPane() {
		return (TabbedPaneEingangsrechnung) tabbedPane ;
//		return tabbedPaneRechnungAll;
	}

	/**
	 * jbInit.
	 * 
	 * @throws Throwable
	 */
	private void jbInit() throws Throwable {
		ParametermandantDto para = DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getMandantparameter(
						LPMain.getTheClient().getMandant(),
						ParameterFac.KATEGORIE_EINGANGSRECHNUNG,
						ParameterFac.PARAMETER_EINGANGSRECHNUNG_ZAHLUNG_MIT_KURSEINGABE);
		bMitKurseingabe = para.getCWert().equals("1");
		if (bMitKurseingabe) {
			wcbKursUebersteuert = new WrapperCheckBox();
			wcbKursUebersteuert.setText(LPMain
					.getTextRespectUISPr("label.zahlung.kursuebersteuert"));
			wcbKursUebersteuert
					.setActionCommand(ACTION_SPECIAL_KURSUEBERSTEUERT);
			wcbKursUebersteuert.addActionListener(this);
			jPanelWorkingOn.add(wcbKursUebersteuert, "cell 6 8, growx");
		}
	}
	
	@Override
	protected void setVisibilityOfComponents() {
		super.setVisibilityOfComponents();
		//TODO: skonto auf Gutschrift in ER noch nicht unterstuetzt
		wcbErledigtGutschrift.setVisible(false);
	}

	/**
	 * Ein Dto-Objekt ins Panel uebertragen.
	 * 
	 * @throws Throwable
	 */
	private void dto2Components() throws Throwable {
		if (getKeyWhenDetailPanel() != null) {
			zahlungDto = DelegateFactory
					.getInstance()
					.getEingangsrechnungDelegate()
					.eingangsrechnungzahlungFindByPrimaryKey(
							(Integer) getKeyWhenDetailPanel());
		}
		if (zahlungDto != null) {
			// sub-dtos
			dto2ComponentsRechnung();
			String zahlungsartCNr = zahlungDto.getZahlungsartCNr();
			if (zahlungsartCNr.equalsIgnoreCase(RechnungFac.ZAHLUNGSART_BANK)) {
				super.bankverbindungDto = DelegateFactory
						.getInstance()
						.getFinanzDelegate()
						.bankverbindungFindByPrimaryKey(
								zahlungDto.getBankverbindungIId());
				super.dto2ComponentsBankverbindung();
				super.wtnfAuszug.setInteger(zahlungDto.getIAuszug());
			} else if (zahlungsartCNr
					.equalsIgnoreCase(RechnungFac.ZAHLUNGSART_BAR)) {
				kassenbuchDto = DelegateFactory
						.getInstance()
						.getFinanzDelegate()
						.kassenbuchFindByPrimaryKey(
								zahlungDto.getKassenbuchIId());
				dto2ComponentsKassenbuch();
			} else if (zahlungsartCNr
					.equalsIgnoreCase(RechnungFac.ZAHLUNGSART_GEGENVERRECHNUNG)) {
				if (zahlungDto.getRechnungzahlungIId() != null) {
					RechnungzahlungDto dto = DelegateFactory
							.getInstance()
							.getRechnungDelegate()
							.zahlungFindByPrimaryKey(
									zahlungDto.getRechnungzahlungIId());
					RechnungDto rechnungDto = DelegateFactory.getInstance()
							.getRechnungDelegate()
							.rechnungFindByPrimaryKey(dto.getRechnungIId());
					wtfRechnung.setText(rechnungDto.getCNr());
				}
			} else if (zahlungsartCNr
					.equalsIgnoreCase(RechnungFac.ZAHLUNGSART_GUTSCHRIFT)) {
				if (zahlungDto.getEingangsrechnungIIdGutschriftZahlung() != null) {
					erGutschriftZahlungDto = DelegateFactory
							.getInstance()
							.getEingangsrechnungDelegate()
							.eingangsrechnungzahlungFindByPrimaryKey(
									zahlungDto
											.getEingangsrechnungIIdGutschriftZahlung());
				}
				if (zahlungDto.getEingangsrechnungIIdGutschrift() != null) {
					erGutschriftDto = DelegateFactory
							.getInstance()
							.getEingangsrechnungDelegate()
							.eingangsrechnungFindByPrimaryKey(
									zahlungDto
											.getEingangsrechnungIIdGutschrift());
					wtfGutschrift.setText(erGutschriftDto.getCNr());
				}

			} else if (zahlungsartCNr
					.equalsIgnoreCase(RechnungFac.ZAHLUNGSART_VORAUSZAHLUNG)
					&& zahlungDto.getBuchungdetailIId() != null) {
				buchungdetailDto = DelegateFactory
						.getInstance()
						.getBuchenDelegate()
						.buchungdetailFindByPrimaryKey(
								zahlungDto.getBuchungdetailIId());
				dto2ComponentsVorauszahlung();
			}
			// restliche felder
			initializeGutschriftButton(zahlungsartCNr);
			wcoZahlungsart.setKeyOfSelectedItem(zahlungDto.getZahlungsartCNr());
			wdfDatum.setDate(zahlungDto.getTZahldatum());

			wnfKurs.setBigDecimal(zahlungDto.getNKurs());
			wnfKurs.setForeground(Color.BLACK);
			if (bMitKurseingabe) {
				if (Helper.short2boolean(zahlungDto.getBKursuebersteuert())) {
					wnfKurs.setActivatable(true);
					wcbKursUebersteuert.setSelected(true);
				} else {
					wnfKurs.setActivatable(false);
					wcbKursUebersteuert.setSelected(false);
				}
			} else {
				String waehrung = getEingangsrechnungDto().getWaehrungCNr();
				WechselkursDto wDto = DelegateFactory
						.getInstance()
						.getLocaleDelegate()
						.getKursZuDatum(
								LPMain.getTheClient().getSMandantenwaehrung(),
								waehrung, wdfDatum.getDate());

				BigDecimal aktuelleKurs = new BigDecimal(1);
				if (wDto != null) {
					aktuelleKurs = wDto.getNKurs().setScale(6,
							BigDecimal.ROUND_HALF_EVEN);
				}

				if (aktuelleKurs != null) {
					if (wnfKurs.getBigDecimal().compareTo(aktuelleKurs) != 0) {
						wnfKurs.setForeground(Color.RED);
					}
				}
			}
			wnfBetrag.setBigDecimal(zahlungDto.getNBetragfw());
			wnfBetragUST.setBigDecimal(zahlungDto.getNBetragustfw());

			// TODO: Neu, durch ghp
			focusLostWnfBetrag();

			this.setStatusbarPersonalIIdAnlegen(zahlungDto
					.getPersonalIIdAnlegen());
			this.setStatusbarTAnlegen(zahlungDto.getTAnlegen());
			this.setStatusbarPersonalIIdAendern(zahlungDto
					.getPersonalIIdAendern());
			this.setStatusbarTAendern(zahlungDto.getTAendern());
		} else {
			leereAlleFelder(this);
		}
	}

	/**
	 * Eine neue Zahlung erstellen.
	 * 
	 * @param e
	 *            ActionEvent
	 * @param bChangeKeyLockMeI
	 *            boolean
	 * @param bNeedNoNewI
	 *            boolean
	 * @throws Throwable
	 */
	public void eventActionNew(EventObject e, boolean bChangeKeyLockMeI,
			boolean bNeedNoNewI) throws Throwable {
		this.zahlungDto = null;
		this.erGutschriftDto = null;
		this.erGutschriftZahlungDto = null;

		super.eventActionNew(e, false, false);

		// PJ16076
		ArrayList<EingangsrechnungDto> al = DelegateFactory
				.getInstance()
				.getEingangsrechnungDelegate()
				.eingangsrechnungFindOffeneByLieferantIId(
						getEingangsrechnungDto().getLieferantIId());

		if (al.size() > 0) {
			String s = "\n";
			for (int i = 0; i < al.size(); i++) {
				EingangsrechnungDto erDto = al.get(i);
				s += erDto.getCNr()
						+ " "
						+ LPMain.getTextRespectUISPr("lp.bruttowert")
						+ " "
						+ Helper.formatZahl(erDto.getNBetragfw(), 2, 
								LPMain.getTheClient().getLocUi()) + " "
						+ erDto.getWaehrungCNr() + "\n";
			}

			DialogFactory
					.showModalDialog(
							LPMain.getTextRespectUISPr("lp.info"),
							LPMain.getTextRespectUISPr(
									"er.offenegutschriften.vorhanden")
									+ s);

		}

	}

	/**
	 * Loeschen einer Zahlung
	 * 
	 * @param e
	 *            ActionEvent
	 * @param bAdministrateLockKeyI
	 *            boolean
	 * @param bNeedNoDeleteI
	 *            boolean
	 * @throws Throwable
	 */
	protected void eventActionDelete(ActionEvent e,
			boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {
		if (this.zahlungDto != null) {
			if (zahlungDto.getIId() != null) {
				if (!isLockedDlg()) {

					if (wcoZahlungsart.getSelectedItem().equals(
							RechnungFac.ZAHLUNGSART_GEGENVERRECHNUNG)) {
						DialogFactory.showModalDialog(
								LPMain.getTextRespectUISPr("lp.error"),
								LPMain.getTextRespectUISPr(
										"er.zahlung.error.gegenverrechnung"));
						return;
					}

					if (getTabbedPane().getEingangsrechnungDto().getStatusCNr()
							.equals(EingangsrechnungFac.STATUS_ERLEDIGT)) {
						boolean answer = (DialogFactory.showMeldung(
								LPMain.getTextRespectUISPr(
										"er.erledigtzahlungloeschen"),
								LPMain.getTextRespectUISPr("lp.frage"),
								javax.swing.JOptionPane.YES_NO_OPTION) == javax.swing.JOptionPane.YES_OPTION);
						if (!answer) {
							return;
						}
					}

					DelegateFactory.getInstance().getEingangsrechnungDelegate()
							.removeEingangsrechnungzahlung(zahlungDto);
					if (zahlungDto.getZahlungsartCNr().equals(
							RechnungFac.RECHNUNGART_GUTSCHRIFT)) {
						DelegateFactory
								.getInstance()
								.getEingangsrechnungDelegate()
								.removeEingangsrechnungzahlung(
										erGutschriftZahlungDto);
					}
					this.zahlungDto = null;
					this.setKeyWhenDetailPanel(null);
					this.leereAlleFelder(this);
					super.eventActionDelete(e, false, false);
				}
			}
		}
	}

	/**
	 * Speichern einer Zahlung.
	 * 
	 * @param e
	 *            ActionEvent
	 * @param bNeedNoSaveI
	 *            boolean
	 * @throws Throwable
	 */
	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {
		if (allMandatoryFieldsSetDlg()) {
			boolean bErledigt = wcbErledigt.isSelected();

			components2Dto();

			if (wcoZahlungsart.getSelectedItem().equals(
					RechnungFac.ZAHLUNGSART_GEGENVERRECHNUNG)) {
				DialogFactory.showModalDialog(
						LPMain.getTextRespectUISPr("lp.error"),
						LPMain.getTextRespectUISPr(
								"er.zahlung.error.gegenverrechnung"));
				return;
			}

			boolean bSpeichern = true;
			if (wnfOffen.getBigDecimal() != null
					&& wnfOffen.getBigDecimal().compareTo(new BigDecimal(0)) < 0) {
				bSpeichern = DialogFactory
						.showModalJaNeinDialog(
								getInternalFrame(),
								LPMain.getTextRespectUISPr(
												"rech.warnung.zahlbetraguebersteigtoffenenbetrag"),
								LPMain.getTextRespectUISPr(
										"lp.frage"));
			}
			if (bErledigt
					&& wnfOffen.getBigDecimal() != null
					&& wnfOffen.getBigDecimal().compareTo(new BigDecimal(0)) > 0) {
				bSpeichern = DialogFactory
						.showModalJaNeinDialog(
								getInternalFrame(),
								LPMain.getTextRespectUISPr(
												"rech.warnung.zahlbetragwenigeralsoffenerbetrag"),
								LPMain.getTextRespectUISPr(
										"lp.frage"));
			}
			if (bSpeichern) {
				// speichern
				if (zahlungDto.getZahlungsartCNr().equals(
						RechnungFac.RECHNUNGART_GUTSCHRIFT)) {
					BigDecimal bdMinus = new BigDecimal(-1);
					if (erGutschriftZahlungDto == null) {
						// neue Zahlung auf gutschrift
						erGutschriftZahlungDto = new EingangsrechnungzahlungDto();
					}
					erGutschriftZahlungDto
							.setEingangsrechnungIId(erGutschriftDto.getIId());
					// Bei einer Gutschrift wird der FK auf die Gutschrift fuer
					// Die zugehoerige Rechnung verwendet
					erGutschriftZahlungDto
							.setEingangsrechnungIIdGutschrift(zahlungDto
									.getEingangsrechnungIId());
					erGutschriftZahlungDto.setTZahldatum(zahlungDto
							.getTZahldatum());
					erGutschriftZahlungDto
							.setZahlungsartCNr(RechnungFac.ZAHLUNGSART_GUTSCHRIFT);
					erGutschriftZahlungDto.setNBetrag(zahlungDto.getNBetrag()
							.multiply(bdMinus));
					erGutschriftZahlungDto.setNBetragfw(zahlungDto
							.getNBetragfw().multiply(bdMinus));
					erGutschriftZahlungDto.setNBetragust(zahlungDto
							.getNBetragust().multiply(bdMinus));
					erGutschriftZahlungDto.setNBetragustfw(zahlungDto
							.getNBetragustfw().multiply(bdMinus));
					erGutschriftZahlungDto.setNKurs(zahlungDto.getNKurs());
					erGutschriftZahlungDto.setBKursuebersteuert(zahlungDto
							.getBKursuebersteuert());
					erGutschriftZahlungDto.setPersonalIIdAendern(zahlungDto
							.getPersonalIIdAendern());
					erGutschriftZahlungDto.setPersonalIIdAnlegen(zahlungDto
							.getPersonalIIdAnlegen());
					erGutschriftZahlungDto.setTAnlegen(new Timestamp(Calendar
							.getInstance().getTime().getTime()));
					erGutschriftZahlungDto.setTAendern(new Timestamp(Calendar
							.getInstance().getTime().getTime()));
					erGutschriftZahlungDto.setPersonalIIdAendern(zahlungDto
							.getPersonalIIdAendern());
					erGutschriftZahlungDto.setPersonalIIdAnlegen(zahlungDto
							.getPersonalIIdAnlegen());
					BigDecimal bdBereitsBezahlt = DelegateFactory.getInstance()
							.getEingangsrechnungDelegate()
							.getBezahltBetragFw(erGutschriftDto.getIId(), null);

					bdBereitsBezahlt = bdBereitsBezahlt.negate().add(
							erGutschriftZahlungDto.getNBetragfw().negate());

					if (bdBereitsBezahlt.add(erGutschriftDto.getNBetragfw())
							.signum() > 0) {
						DialogFactory.showModalDialog(
								LPMain.getTextRespectUISPr("lp.error"),
								LPMain.getTextRespectUISPr(
										"rech.error.geutschriftzuniedrig"));
						return;
					}

					if (bdBereitsBezahlt.doubleValue() >= erGutschriftDto
							.getNBetragfw().negate().doubleValue()) {
						erGutschriftDto
								.setStatusCNr(EingangsrechnungFac.STATUS_ERLEDIGT);
						erGutschriftZahlungDto = DelegateFactory
								.getInstance()
								.getEingangsrechnungDelegate()
								.updateEingangsrechnungzahlung(
										erGutschriftZahlungDto, true);

					} else {
						erGutschriftDto
								.setStatusCNr(EingangsrechnungFac.STATUS_TEILBEZAHLT);
						erGutschriftZahlungDto = DelegateFactory
								.getInstance()
								.getEingangsrechnungDelegate()
								.updateEingangsrechnungzahlung(
										erGutschriftZahlungDto, false);

					}

					zahlungDto
							.setEingangsrechnungIIdGutschriftZahlung(erGutschriftZahlungDto
									.getIId());
					zahlungDto.setEingangsrechnungIIdGutschrift(erGutschriftDto
							.getIId());
				}
				zahlungDto = DelegateFactory.getInstance()
						.getEingangsrechnungDelegate()
						.updateEingangsrechnungzahlung(zahlungDto, bErledigt);
				setKeyWhenDetailPanel(getEingangsrechnungDto().getIId());
				super.eventActionSave(e, true);
				/** @todo uuu UW: brauchen wir das auch bei 1:n? PJ 4969 */
				if (getInternalFrame().getKeyWasForLockMe() == null) {
					// der erste 1:n eintrag wurde angelegt den hauptkey locken.
					getInternalFrame().setKeyWasForLockMe(
							zahlungDto.getEingangsrechnungIId().toString());
				}
				// Eine eventuelle statusaenderung auch anzeigen
				getTabbedPane().reloadEingangsrechnungDto();

				eventYouAreSelected(false);
			}
		}
		return;
	}

	protected String getLockMeWer() {
		return HelperClient.LOCKME_EINGANGSRECHNUNG;
	}

	private void components2Dto() throws Throwable {
		EingangsrechnungDto rechnungDto = getEingangsrechnungDto();
		if (zahlungDto == null) {
			zahlungDto = new EingangsrechnungzahlungDto();
			zahlungDto.setEingangsrechnungIId(rechnungDto.getIId());
		}
		zahlungDto.setTZahldatum(wdfDatum.getDate());
		// Wert pruefen
		wnfBetrag.checkMinimumMaximum();
		zahlungDto.setNBetragfw(wnfBetrag.getBigDecimal());
		zahlungDto.setNBetragustfw(wnfBetragUST.getBigDecimal());

		String waehrung = rechnungDto.getWaehrungCNr();
		if (bMitKurseingabe && wcbKursUebersteuert.isSelected()) {
			// Kurseingabe ist erlaubt und Kurs ist uebersteuert
			zahlungDto.setBKursuebersteuert(Helper.boolean2Short(true));
			zahlungDto.setNKurs(wnfKurs.getBigDecimal());
			zahlungDto.setNBetrag(wnfBetrag.getBigDecimal().divide(
					zahlungDto.getNKurs(), FinanzFac.NACHKOMMASTELLEN,
					BigDecimal.ROUND_HALF_EVEN));
			zahlungDto.setNBetragust(wnfBetragUST.getBigDecimal().divide(
					zahlungDto.getNKurs(), FinanzFac.NACHKOMMASTELLEN,
					BigDecimal.ROUND_HALF_EVEN));
			hmKurse.remove(waehrung);
			hmKurse.put(waehrung, wnfKurs.getBigDecimal());
		} else {
			zahlungDto.setBKursuebersteuert(Helper.boolean2Short(false));
			String mandantWaehrung = DelegateFactory
					.getInstance()
					.getMandantDelegate()
					.mandantFindByPrimaryKey(
							LPMain.getTheClient().getMandant())
					.getWaehrungCNr();

			zahlungDto.setNBetrag(DelegateFactory
					.getInstance()
					.getLocaleDelegate()
					.rechneUmInAndereWaehrungGerundetZuDatum(
							wnfBetrag.getBigDecimal(), waehrung,
							mandantWaehrung, wdfDatum.getDate()));
			zahlungDto.setNBetragust(DelegateFactory
					.getInstance()
					.getLocaleDelegate()
					.rechneUmInAndereWaehrungGerundetZuDatum(
							wnfBetragUST.getBigDecimal(), waehrung,
							mandantWaehrung, wdfDatum.getDate()));

			if (mandantWaehrung.equals(waehrung)) {
				zahlungDto.setNKurs(new BigDecimal(1));
			} else {
				WechselkursDto kursDto = DelegateFactory
						.getInstance()
						.getLocaleDelegate()
						.getKursZuDatum(mandantWaehrung, waehrung,
								wdfDatum.getDate());
				zahlungDto.setNKurs(kursDto.getNKurs());
			}
		}
		zahlungDto.setZahlungsartCNr((String) wcoZahlungsart
				.getKeyOfSelectedItem());
		String zahlungsartCNr = (String) wcoZahlungsart.getKeyOfSelectedItem();
		if (zahlungsartCNr.equalsIgnoreCase(RechnungFac.ZAHLUNGSART_BANK)) {
			zahlungDto.setBankverbindungIId(bankverbindungDto.getIId());
			zahlungDto.setIAuszug(wtnfAuszug.getInteger());
		} else {
			zahlungDto.setBankverbindungIId(null);
			zahlungDto.setIAuszug(null);
		}
		if (zahlungsartCNr.equalsIgnoreCase(RechnungFac.ZAHLUNGSART_BAR)) {
			zahlungDto.setKassenbuchIId(kassenbuchDto.getIId());
		} else {
			zahlungDto.setKassenbuchIId(null);
		}
		if (zahlungsartCNr.equalsIgnoreCase(RechnungFac.ZAHLUNGSART_GUTSCHRIFT)) {
			if (erGutschriftZahlungDto != null) {
				zahlungDto
						.setEingangsrechnungIIdGutschriftZahlung(erGutschriftZahlungDto
								.getIId());
			}
			if (erGutschriftDto != null) {
				zahlungDto.setEingangsrechnungIIdGutschrift(erGutschriftDto
						.getIId());
			}
		} else {
			zahlungDto.setEingangsrechnungIIdGutschriftZahlung(null);
			zahlungDto.setEingangsrechnungIIdGutschrift(null);
		}
		if (zahlungsartCNr.equalsIgnoreCase(RechnungFac.ZAHLUNGSART_VORAUSZAHLUNG)) {
			zahlungDto.setBuchungdetailIId(buchungdetailDto.getIId());
		} else {
			zahlungDto.setBuchungdetailIId(null);
		}

		// }
	}

	private void dto2ComponentsRechnung() throws Throwable {
		EingangsrechnungDto rechnungDto = getEingangsrechnungDto();
		// das erledigt-hakerl
		if (this.getLockedstateDetailMainKey().getIState() != PanelBasis.LOCK_FOR_NEW
				&& this.getLockedstateDetailMainKey().getIState() != PanelBasis.LOCK_IS_LOCKED_BY_ME) {
			wcbErledigt.setSelected(rechnungDto.getStatusCNr().equals(
					EingangsrechnungFac.STATUS_ERLEDIGT));
		}
	}

	private void setDefaults() throws Throwable {
		boolean bSchlussrechnung = false;
		if (getEingangsrechnungDto().getEingangsrechnungartCNr().equals(
				EingangsrechnungFac.EINGANGSRECHNUNGART_SCHLUSSZAHLUNG)) {
			bSchlussrechnung = true;
		}

		wlaAnzahlungen.setVisible(bSchlussrechnung);
		wnfAnzahlungen.setVisible(bSchlussrechnung);
		wlaAnzahlungenUST.setVisible(bSchlussrechnung);
		wnfAnzahlungenUST.setVisible(bSchlussrechnung);
		wlaWaehrungAnz.setVisible(bSchlussrechnung);
		wlaWaehrungAnzUst.setVisible(bSchlussrechnung);
	}

	/**
	 * getRechnungDto
	 * 
	 * @return RechnungDto
	 */
	private EingangsrechnungDto getEingangsrechnungDto() {
		return getTabbedPane().getEingangsrechnungDto();
	}

	protected void dialogQueryGutschriftER(ActionEvent e, Integer LieferantIId)
			throws Throwable {
		// ffcreatespanel: fuer eine dialogquery
		panelQueryFLRGutschrift = FinanzFilterFactory.getInstance()
				.createPanelFLRGutschriftER(getInternalFrame(), LieferantIId);
		new DialogQuery(panelQueryFLRGutschrift);
	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		super.eventActionSpecial(e);
		if (e.getActionCommand().equals(ACTION_SPECIAL_GUTSCHRIFT)) {
			dialogQueryGutschriftER(e, getEingangsrechnungDto()
					.getLieferantIId());
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_KURSUEBERSTEUERT)) {
			if (wcbKursUebersteuert.isSelected()) {
				wnfKurs.setEditable(true);
				wnfKurs.setActivatable(true);
				if (wnfKurs.getBigDecimal() == null) {
					EingangsrechnungDto rechnungDto = getEingangsrechnungDto();
					BigDecimal k = hmKurse.get(rechnungDto.getWaehrungCNr());
					if (k == null)
						wnfKurs.setBigDecimal(getKursAktuell());
					else
						wnfKurs.setBigDecimal(k);
				}
				wnfKurs.requestFocusInWindow();
			} else {
				wnfKurs.setEditable(false);
				wnfKurs.setBigDecimal(getKursAktuell());
			}
		}
	}

	private BigDecimal getKursAktuell() throws ExceptionLP, Throwable {
		EingangsrechnungDto rechnungDto = getEingangsrechnungDto();
		String mandantWaehrung = DelegateFactory
				.getInstance()
				.getMandantDelegate()
				.mandantFindByPrimaryKey(LPMain.getTheClient().getMandant())
				.getWaehrungCNr();
		if (rechnungDto.getWaehrungCNr().equals(mandantWaehrung)) {
			return new BigDecimal(1);
		} else {
			WechselkursDto kursDto = DelegateFactory
					.getInstance()
					.getLocaleDelegate()
					.getKursZuDatum(mandantWaehrung,
							rechnungDto.getWaehrungCNr(), wdfDatum.getDate());
			return kursDto.getNKurs();
		}
	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		super.eventItemchanged(eI);
		ItemChangedEvent e = (ItemChangedEvent) eI;
		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			if (e.getSource() == panelQueryFLRGutschrift) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				erGutschriftDto = DelegateFactory.getInstance()
						.getEingangsrechnungDelegate()
						.eingangsrechnungFindByPrimaryKey((Integer) key);
				wtfGutschrift.setText(erGutschriftDto.getCNr());
				BigDecimal bdBezahlt;
				if (erGutschriftZahlungDto != null) {
					bdBezahlt = DelegateFactory
							.getInstance()
							.getEingangsrechnungDelegate()
							.getBezahltBetragFw(erGutschriftDto.getIId(),
									erGutschriftZahlungDto.getIId());
				} else {
					bdBezahlt = DelegateFactory.getInstance()
							.getEingangsrechnungDelegate()
							.getBezahltBetragFw(erGutschriftDto.getIId(), null);

				}
				BigDecimal diff = erGutschriftDto.getNBetragfw().subtract(
						bdBezahlt);
				BigDecimal gesOffen = getWertGesamtOffenExklusiveZahlung();
				wnfBetrag.setBigDecimal(diff.negate().doubleValue() > gesOffen.doubleValue() ? gesOffen : diff.negate());
				focusLostWnfBetrag();
			}
		}

	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {
		super.eventYouAreSelected(false);
		Object key = getKeyWhenDetailPanel();
		setDefaults();
		// status aktualisieren
		getTabbedPane().refreshEingangsrechnungDto();
		if (key == null
				|| (key != null && key.equals(LPMain.getLockMeForNew()))) {
			// einen neuen Eintrag anlegen oder die letzte Position wurde
			// geloescht.
			dto2ComponentsRechnung();
			zahlungDto = null;
		} else {
			// einen alten Eintrag laden.
			zahlungDto = DelegateFactory.getInstance()
					.getEingangsrechnungDelegate()
					.eingangsrechnungzahlungFindByPrimaryKey((Integer) key);

			dto2Components();
			getTabbedPane().enablePanels();
			if (EingangsrechnungFac.EINGANGSRECHNUNGART_GUTSCHRIFT
					.equals(getTabbedPane().getEingangsrechnungDto()
							.getEingangsrechnungartCNr())) {
				LPButtonAction item = (LPButtonAction) getHmOfButtons().get(
						PanelBasis.ACTION_UPDATE);
				item.getButton().setEnabled(false);
				item = (LPButtonAction) getHmOfButtons().get(
						PanelBasis.ACTION_DELETE);
				item.getButton().setEnabled(false);
			}

		}

	}

	protected void eventActionUpdate(ActionEvent aE, boolean bNeedNoUpdateI)
			throws Throwable {
		EingangsrechnungDto erDto = getTabbedPane().getEingangsrechnungDto();
		if (erDto.getStatusCNr().equals(EingangsrechnungFac.STATUS_ERLEDIGT)) {
			boolean answer = (DialogFactory
					.showMeldung(
							"Die Eingangsrechnung ist bereits vollst\u00E4ndig bezahlt\nTrotzdem \u00E4ndern?",
							"Frage", javax.swing.JOptionPane.YES_NO_OPTION) == javax.swing.JOptionPane.YES_OPTION);
			if (!answer) {
				return;
			}
		}

		super.eventActionUpdate(aE, bNeedNoUpdateI);
		wcoZahlungsart.setEnabled(false);
	}

	@Override
	protected BigDecimal getWertGesamtOffenExklusiveZahlung() throws Throwable {
		EingangsrechnungDto erDto = getTabbedPane().getEingangsrechnungDto();
		return erDto
				.getNBetragfw()
				.subtract(
						getEingangsrechnungDelegate().getBezahltBetragFw(
								erDto.getIId(), null))
				.add(zahlungDto == null ? new BigDecimal(0) : zahlungDto
						.getNBetragfw());
	}

	@Override
	protected BigDecimal getWertGesamtOffenUstExklusiveZahlung()
			throws Throwable {
		EingangsrechnungDto erDto = getTabbedPane().getEingangsrechnungDto();
		return erDto
				.getNUstBetragfw()
				.subtract(
						getEingangsrechnungDelegate().getBezahltBetragUstFw(
								erDto.getIId(), null))
				.add(zahlungDto == null ? new BigDecimal(0) : zahlungDto
						.getNBetragustfw());
	}

	protected BigDecimal getWertBereitsBezahlt() throws Throwable {
		EingangsrechnungDto erDto = getTabbedPane().getEingangsrechnungDto();
		BigDecimal betragAbZahlung = zahlungDto != null ? getEingangsrechnungDelegate()
				.getBezahltBetragFw(erDto.getIId(), zahlungDto.getIId()).add(
						zahlungDto.getNBetragfw()) : new BigDecimal(0);
		BigDecimal gesamtBezahlt = getEingangsrechnungDelegate()
				.getBezahltBetragFw(erDto.getIId(), null);
		return gesamtBezahlt.subtract(betragAbZahlung);

	}

	protected BigDecimal getWertBereitsBezahltUst() throws Throwable {
		EingangsrechnungDto erDto = getTabbedPane().getEingangsrechnungDto();
		BigDecimal betragAbZahlung = zahlungDto != null ? getEingangsrechnungDelegate()
				.getBezahltBetragUstFw(erDto.getIId(), zahlungDto.getIId())
				.add(zahlungDto.getNBetragustfw()) : new BigDecimal(0);
		BigDecimal gesamtBezahlt = getEingangsrechnungDelegate()
				.getBezahltBetragUstFw(erDto.getIId(), null);
		return gesamtBezahlt.subtract(betragAbZahlung);
	}

	protected BigDecimal getWertAnzahlungen() throws Throwable {
		EingangsrechnungDto erDto = getTabbedPane().getEingangsrechnungDto();
		if (erDto.getEingangsrechnungartCNr().equals(
				EingangsrechnungFac.EINGANGSRECHNUNGART_SCHLUSSZAHLUNG))
			return getEingangsrechnungDelegate()
					.getAnzahlungenZuSchlussrechnungFw(erDto.getIId());
		else
			return new BigDecimal(0);
	}

	protected BigDecimal getWertAnzahlungenUst() throws Throwable {
		EingangsrechnungDto erDto = getTabbedPane().getEingangsrechnungDto();
		if (erDto.getEingangsrechnungartCNr().equals(
				EingangsrechnungFac.EINGANGSRECHNUNGART_SCHLUSSZAHLUNG))
			return getEingangsrechnungDelegate()
					.getAnzahlungenZuSchlussrechnungUstFw(erDto.getIId());
		else
			return new BigDecimal(0);
	}

	protected BigDecimal getWert() throws Throwable {
		return getTabbedPane().getEingangsrechnungDto().getNBetragfw();
	}

	protected BigDecimal getWertUst() throws Throwable {
		return getTabbedPane().getEingangsrechnungDto().getNUstBetragfw();
	}

	protected String getWaehrung() throws Throwable {
		return getTabbedPane().getEingangsrechnungDto().getWaehrungCNr();
	}

	protected Integer getZahlungszielIId() throws Throwable {
		return getTabbedPane().getEingangsrechnungDto().getZahlungszielIId();
	}

	protected java.sql.Date getDBelegdatum() {
		return new java.sql.Date(getTabbedPane().getEingangsrechnungDto()
				.getDBelegdatum().getTime());
	}

	protected void eventActionRefresh(ActionEvent e, boolean bNeedNoRefreshI)
			throws Throwable {
		super.eventActionRefresh(e, bNeedNoRefreshI);
		getTabbedPane().enablePanels();
	}

	protected void eventActionDiscard(ActionEvent e) throws Throwable {
		super.eventActionDiscard(e);
		// getTabbedPane().enablePanels();
	}

	private EingangsrechnungDelegate getEingangsrechnungDelegate()
			throws Throwable {
		return DelegateFactory.getInstance().getEingangsrechnungDelegate();
	}

	@Override
	protected Integer getKontoIIdAnzahlung() throws Throwable {
		LieferantDto lieferant = DelegateFactory
				.getInstance()
				.getLieferantDelegate()
				.lieferantFindByPrimaryKey(
						getEingangsrechnungDto().getLieferantIId());
		KontoDto konto = DelegateFactory.getInstance().getFinanzDelegate()
				.kontoFindByPrimaryKey(lieferant.getKontoIIdKreditorenkonto());
		return konto.getIId();
	}

	@Override
	protected String getBuchungDetailAtCNr() { 
		return BuchenFac.SollBuchung;
	}

	@Override
	protected boolean isGutschriftErlaubt() {
		return !getEingangsrechnungDto().getEingangsrechnungartCNr().equals(EingangsrechnungFac.EINGANGSRECHNUNGART_ANZAHLUNG);
	}

	@Override
	protected boolean darfManuellErledigen() throws Throwable {
		if(!EingangsrechnungFac.EINGANGSRECHNUNGART_ANZAHLUNG.equals(getEingangsrechnungDto().getEingangsrechnungartCNr()))
			return true;
		EingangsrechnungDto[] rechArray = DelegateFactory.getInstance().getEingangsrechnungDelegate()
			.findByBestellungIId(getEingangsrechnungDto().getBestellungIId());
		for (EingangsrechnungDto rech : rechArray) {
			if (EingangsrechnungFac.EINGANGSRECHNUNGART_SCHLUSSZAHLUNG
					.equals(rech.getEingangsrechnungartCNr())
					&& !EingangsrechnungFac.STATUS_STORNIERT.equals(rech
							.getStatusCNr())) {
				return false;
			}
		}
		return true;
	}
	
	protected void initializeGutschriftButton(String zahlungartCnr) {
		if(RechnungFac.ZAHLUNGSART_GUTSCHRIFT.equalsIgnoreCase(zahlungartCnr) && 
				erGutschriftDto.getEingangsrechnungartCNr().equalsIgnoreCase(EingangsrechnungFac.EINGANGSRECHNUNGART_EINGANGSRECHNUNG)) {
			wbuGutschrift.setText(LPMain.getTextRespectUISPr("rech.rechnung"));	
		} else {
			super.initializeGutschriftButton();
		}
	}
}
