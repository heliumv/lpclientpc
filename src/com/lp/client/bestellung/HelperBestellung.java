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

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.swing.JOptionPane;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.WrapperFixableNumberField;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikellieferantDto;
import com.lp.server.artikel.service.ArtikellieferantstaffelDto;
import com.lp.server.bestellung.service.BestellpositionDto;
import com.lp.server.bestellung.service.BestellungDto;
import com.lp.server.bestellung.service.BestellungFac;
import com.lp.server.bestellung.service.WareneingangspositionDto;
import com.lp.server.partner.service.LieferantDto;

@SuppressWarnings("static-access")
/**
 // * <p> Diese Klasse kuemmert sich ...</p>
 // *
 // * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 // *
 // * <p>Erstellung: Vorname Nachname; dd.mm.05</p>
 // *
 // * <p>@author $Author: christian $</p>
 // *
 // * @version not attributable Date $Date: 2012/08/17 06:51:43 $
 // */
public class HelperBestellung implements Comparator<Object> {
	private static HelperBestellung helperBestellung = null;

	protected HelperBestellung() {

	}

	public synchronized static HelperBestellung getInstance() {
		if (helperBestellung == null) {
			helperBestellung = new HelperBestellung();
		}
		return helperBestellung;
	}

	/**
	 * schaut bei eventActionSave nach ob mindestbestellmenge unterschritten
	 * 
	 * @return boolean
	 * @throws Throwable
	 * @param internalFrame
	 *            InternalFrame
	 * @param artikelDto
	 *            ArtikelDto
	 * @param wnfMenge
	 *            PanelPositionenPreiseingabe
	 * @param wnfNettopreis
	 *            WrapperNumberField
	 * @param wnfRabattsatz
	 *            WrapperNumberField
	 * @param wnfRabattsumme
	 *            WrapperNumberField
	 * @param wnfEinzelpreis
	 *            WrapperNumberField
	 * @param lieferantDto
	 *            LieferantDto
	 */
	public static synchronized boolean checkMindestbestellmenge(
			InternalFrame internalFrame, ArtikelDto artikelDto,
			WrapperNumberField wnfMenge,
			WrapperFixableNumberField wnfNettopreis,
			WrapperNumberField wnfRabattsatz,
			WrapperFixableNumberField wnfRabattsumme,
			WrapperNumberField wnfEinzelpreis, LieferantDto lieferantDto)

	throws Throwable {

		boolean answer = true;
		// nachschauen ob es artikelieferant gibt fuer diesen artikel
		ArtikellieferantDto artlieferantDto = null;
		if (lieferantDto != null) {
			artlieferantDto = DelegateFactory
					.getInstance()
					.getArtikelDelegate()
					.getArtikelEinkaufspreis(artikelDto.getIId(),
							lieferantDto.getIId(), BigDecimal.ONE,
							LPMain.getTheClient().getSMandantenwaehrung(),
							new java.sql.Date(System.currentTimeMillis()));
		}

		// checked Verpackungseinheit
		if (artlieferantDto != null) {
			if (artlieferantDto.getNVerpackungseinheit() != null) {
				if (new Integer(wnfMenge.getBigDecimal().intValue())
						.compareTo(artlieferantDto.getNVerpackungseinheit()
								.intValue()) < 0) {
					answer = HelperBestellung.checkVerpackungseinheit(
							artlieferantDto, artikelDto);
					if (answer == false) {
						DialogFactory
								.showModalDialog(
										LPMain.getInstance()
												.getTextRespectUISPr(
														"lp.hinweis"),
										LPMain.getInstance()
												.getTextRespectUISPr(
														"bes.mengeaufverpackungseinheitkorrigieren"));
						wnfMenge.setBigDecimal(artlieferantDto
								.getNVerpackungseinheit());
					}
				}
			}
		}

		if (artlieferantDto != null) {
			if (artlieferantDto.getFMindestbestelmenge() != null) {
				if (wnfMenge.getBigDecimal().doubleValue() < artlieferantDto
						.getFMindestbestelmenge().doubleValue()) {

					MessageFormat mf = new MessageFormat(LPMain.getInstance()
							.getTextRespectUISPr(
									"bes.mindesbestellmengeunterschritten"));

					mf.setLocale(LPMain.getInstance().getTheClient().getLocUi());

					Object pattern[] = {
							artlieferantDto.getFMindestbestelmenge(),
							artikelDto.getEinheitCNr().trim() }; // UW->JE
					String sMsg = mf.format(pattern);

					answer = (DialogFactory.showMeldung(sMsg, LPMain
							.getInstance().getTextRespectUISPr("lp.warning"),
							javax.swing.JOptionPane.YES_NO_OPTION) == javax.swing.JOptionPane.YES_OPTION);
					// nein die menge wird nicht erfasst nix passiert
				}
			}
		}
		return answer;
	}

	private static synchronized boolean checkVerpackungseinheit(
			ArtikellieferantDto artlieferantDto, ArtikelDto artikelDto)
			throws Throwable {
		MessageFormat mf = new MessageFormat(LPMain.getInstance()
				.getTextRespectUISPr("bes.verpackungseinheit"));

		mf.setLocale(LPMain.getInstance().getTheClient().getLocUi());

		Object pattern[] = { artlieferantDto.getNVerpackungseinheit(),
				artikelDto.getEinheitCNr().trim() }; // UW->JE
		String sMsg = mf.format(pattern);

		boolean answer = (DialogFactory.showMeldung(sMsg, LPMain.getInstance()
				.getTextRespectUISPr("lp.warning"),
				javax.swing.JOptionPane.YES_NO_OPTION) == javax.swing.JOptionPane.YES_OPTION);

		return answer;
	}

	public static synchronized boolean checkStaffelpreiseUndMengen(
			ArtikelDto artikelDto, InternalFrame internalFrame,
			boolean checkMenge, WrapperNumberField wnfMenge,
			WrapperFixableNumberField wnfNettopreis,
			WrapperNumberField wnfRabattsatz,
			WrapperFixableNumberField wnfRabattsumme,
			WrapperNumberField wnfEinzelpreis, LieferantDto lieferantDto,
			String mandantWaehrung, boolean umrechnenInMandantwaehrung)
			throws ExceptionLP, Throwable {

		// nachschauen ob es artikelieferant gibt fuer diesen artikel
		ArtikellieferantDto artlieferantDto = null;
		if (lieferantDto != null) {
			
			artlieferantDto = DelegateFactory
					.getInstance()
					.getArtikelDelegate().getArtikelEinkaufspreis(artikelDto.getIId(),lieferantDto.getIId(), BigDecimal.ONE,LPMain.getTheClient().getSMandantenwaehrung(),new java.sql.Date(System.currentTimeMillis()));

		}

		// nachschauen ob es Staffeln dazu gibt
		if (artlieferantDto != null) {

			// mindestbestellmenge ist null
			if (artlieferantDto.getFMindestbestelmenge() == null
					&& artlieferantDto.getNVerpackungseinheit() == null) {
				HelperBestellung.zeigeStaffelpreiseDlg(artlieferantDto,
						internalFrame, wnfMenge, wnfNettopreis, wnfRabattsatz,
						wnfRabattsumme, wnfEinzelpreis, mandantWaehrung,
						umrechnenInMandantwaehrung);

			}

			ArtikelDto artikelDtofresh = DelegateFactory.getInstance()
					.getArtikelDelegate()
					.artikelFindByPrimaryKey(artikelDto.getIId());

			if (artlieferantDto.getNVerpackungseinheit() != null) {
				if (wnfMenge.getBigDecimal().intValue() < artlieferantDto
						.getNVerpackungseinheit().intValue()) {

					boolean answer = false;
					try {
						answer = HelperBestellung.checkVerpackungseinheit(
								artlieferantDto, artikelDtofresh);
					} catch (Throwable ex1) {
						// @todo PJ 4688
					}

					if (answer == false) {
						DialogFactory
								.showModalDialog(
										LPMain.getInstance()
												.getTextRespectUISPr(
														"lp.hinweis"),
										LPMain.getInstance()
												.getTextRespectUISPr(
														"bes.mengeaufverpackungseinheitkorrigieren"));
						wnfMenge.setBigDecimal(new BigDecimal(artlieferantDto
								.getNVerpackungseinheit().intValue()));
					}
				}
			}
			if (artlieferantDto.getFMindestbestelmenge() != null) {
				// mindestbestellmenge unterschritten
				if (wnfMenge.getBigDecimal().doubleValue() < artlieferantDto
						.getFMindestbestelmenge().doubleValue()) {

					MessageFormat mf = new MessageFormat(LPMain.getInstance()
							.getTextRespectUISPr(
									"bes.mindesbestellmengeunterschritten"));

					try {
						mf.setLocale(LPMain.getInstance().getTheClient()
								.getLocUi());
					} catch (Throwable ex) {
						// @todo PJ 4688
					}

					Object pattern[] = {
							artlieferantDto.getFMindestbestelmenge(),
							artikelDto.getEinheitCNr().trim() }; // UW->JE
					String sMsg = mf.format(pattern);

					boolean answer = (DialogFactory.showMeldung(sMsg, LPMain
							.getInstance().getTextRespectUISPr("lp.warning"),
							javax.swing.JOptionPane.YES_NO_OPTION) == javax.swing.JOptionPane.YES_OPTION);

					// setzen von checkmenge auf false damit nicht nochmal
					// aufgerufen beim save
					checkMenge = false;

					// ja die menge wird erfasst
					if (answer == true) {
						HelperBestellung.zeigeStaffelpreiseDlg(artlieferantDto,
								internalFrame, wnfMenge, wnfNettopreis,
								wnfRabattsatz, wnfRabattsumme, wnfEinzelpreis,
								mandantWaehrung, umrechnenInMandantwaehrung);

					}
				}
				// mindestbestellmenge nicht unterschritten
				else {
					HelperBestellung.zeigeStaffelpreiseDlg(artlieferantDto,
							internalFrame, wnfMenge, wnfNettopreis,
							wnfRabattsatz, wnfRabattsumme, wnfEinzelpreis,
							mandantWaehrung, umrechnenInMandantwaehrung);

				}

			}

		}
		return checkMenge;
	}

	/**
	 * // * zeigt staffelpreise wenn vorhanden richtig an // * @param
	 * artlieferantDto ArtikellieferantDto //
	 * 
	 * @param artlieferantDto
	 *            ArtikellieferantDto
	 * @param internalFrame
	 *            InternalFrame
	 * @param wnfMenge
	 *            PanelPositionenPreiseingabe
	 * @param wnfNettopreis
	 *            WrapperNumberField
	 * @param wnfRabattsatz
	 *            WrapperNumberField
	 * @param wnfRabattsumme
	 *            WrapperNumberField
	 * @param wnfEinzelpreis
	 *            WrapperNumberField
	 * @param mandantWaehrung
	 *            String
	 * @param umrechnenInMandantwaehrung
	 *            boolean
	 * @throws ExceptionLP
	 * @throws Throwable
	 */
	private static synchronized void zeigeStaffelpreiseDlg(
			ArtikellieferantDto artlieferantDto, InternalFrame internalFrame,
			WrapperNumberField wnfMenge,
			WrapperFixableNumberField wnfNettopreis,
			WrapperNumberField wnfRabattsatz,
			WrapperFixableNumberField wnfRabattsumme,
			WrapperNumberField wnfEinzelpreis, String mandantWaehrung,
			boolean umrechnenInMandantwaehrung) throws ExceptionLP, Throwable {

		ArtikellieferantstaffelDto artikellieferantStaffelDto[] = null;
		ArrayList<ArtikellieferantstaffelDto> arlist = new ArrayList<ArtikellieferantstaffelDto>();

		artikellieferantStaffelDto = DelegateFactory
				.getInstance()
				.getArtikelDelegate()
				.artikellieferantstaffelFindByArtikellieferantIId(
						artlieferantDto.getIId());

		for (int j = 0; j < artikellieferantStaffelDto.length; j++) {
			arlist.add(artikellieferantStaffelDto[j]);
		}

		// sortieren
		Collections.sort(arlist, HelperBestellung.getInstance());
		for (int m = 0; m < artikellieferantStaffelDto.length; m++) {
			artikellieferantStaffelDto[m] = (ArtikellieferantstaffelDto) arlist
					.get(m);
		}

		// sortiert 1 kleinste Menge i+1 groesste Menge
		// wenn groesser als groesster Staffelpreis
		// vorher pruefen ob es Staffelpreise uberhaupt gibt deswegen auf laenge
		// abfragen
		if (artikellieferantStaffelDto.length > 0) {
			if (wnfMenge
					.getBigDecimal()
					.compareTo(
							artikellieferantStaffelDto[artikellieferantStaffelDto.length - 1]
									.getNMenge()) >= 0) {

				setzePreisfelder(
						wnfEinzelpreis,
						wnfNettopreis,
						wnfRabattsatz,
						wnfRabattsumme,
						mandantWaehrung,
						internalFrame,
						artlieferantDto,
						umrechnenInMandantwaehrung,
						artikellieferantStaffelDto[artikellieferantStaffelDto.length - 1]);

			}
			// oder wenn kleiner als kleinster Staffelpreis
			else if (wnfMenge.getBigDecimal().compareTo(
					artikellieferantStaffelDto[0].getNMenge()) < 0) {

				// wenn true dann wird in mandantwaehrung umgerechnet ansonsten
				// wird wechselkurs
				// einfach mit 1 initalisiert und es passiert nix
				BigDecimal wechselkursLieferantnachMandantWaehrung = new BigDecimal(
						1);
				if (umrechnenInMandantwaehrung == true) {
					// holen des wechselkurses von lieferanten in
					// mandantenwaehrung
					wechselkursLieferantnachMandantWaehrung = DelegateFactory
							.getInstance()
							.getLocaleDelegate()
							.getWechselkurs2(
									artlieferantDto.getLieferantDto()
											.getWaehrungCNr(), mandantWaehrung);
				}

				// setzen der werte
				wnfNettopreis.setBigDecimal(artlieferantDto.getNNettopreis()
						.multiply(wechselkursLieferantnachMandantWaehrung));

				wnfRabattsatz.setBigDecimal(new BigDecimal(artlieferantDto
						.getFRabatt().doubleValue()));

				// ausrechnen der Rabattsumme
				BigDecimal bdRabattsumme = wnfEinzelpreis
						.getBigDecimal()
						.multiply(wechselkursLieferantnachMandantWaehrung)
						.multiply(
								wnfRabattsatz.getBigDecimal().movePointLeft(2));
				wnfRabattsumme.setBigDecimal(bdRabattsumme);

			}

			else {

				for (int i = 0; i < artikellieferantStaffelDto.length; i++) {

					// sonst wenn genau gleich
					if (wnfMenge.getBigDecimal().compareTo(
							artikellieferantStaffelDto[i].getNMenge()) == 0) {

						setzePreisfelder(wnfEinzelpreis, wnfNettopreis,
								wnfRabattsatz, wnfRabattsumme, mandantWaehrung,
								internalFrame, artlieferantDto,
								umrechnenInMandantwaehrung,
								artikellieferantStaffelDto[i]);

					}
					// oder wenn groesser als jeztiges aber kleiner als
					// naechstes
					else if (wnfMenge.getBigDecimal().compareTo(
							artikellieferantStaffelDto[i].getNMenge()) >= 0
							&& wnfMenge.getBigDecimal().compareTo(
									artikellieferantStaffelDto[i + 1]
											.getNMenge()) <= 0) {

						setzePreisfelder(wnfEinzelpreis, wnfNettopreis,
								wnfRabattsatz, wnfRabattsumme, mandantWaehrung,
								internalFrame, artlieferantDto,
								umrechnenInMandantwaehrung,
								artikellieferantStaffelDto[i]);

					}

				}
			}

			// Meldung ob man bei naechst hoeherem Staffelpreis steht

			if (wnfMenge.getBigDecimal().compareTo(
					artikellieferantStaffelDto[0].getNMenge()) < 0) {
				boolean answer = getMeldungMengeAnStaffelpreisAnpassen(artikellieferantStaffelDto[0]);

				if (answer == true) {

					setzePreisfelder(wnfEinzelpreis, wnfNettopreis,
							wnfRabattsatz, wnfRabattsumme, mandantWaehrung,
							internalFrame, artlieferantDto,
							umrechnenInMandantwaehrung,
							artikellieferantStaffelDto[0]);

					wnfMenge.setBigDecimal(artikellieferantStaffelDto[0]
							.getNMenge());

				}

			}
			for (int i = 0; i < artikellieferantStaffelDto.length; i++) {

				if (wnfMenge.getBigDecimal().compareTo(
						artikellieferantStaffelDto[i].getNMenge()) > 0) {

					for (int j = 1; j < artikellieferantStaffelDto.length; j++) {

						if (wnfMenge.getBigDecimal().compareTo(
								artikellieferantStaffelDto[j].getNMenge()) < 0
								&& wnfMenge.getBigDecimal().compareTo(
										artikellieferantStaffelDto[j - 1]
												.getNMenge()) > 0) {

							// berechnen der Werte Staffelmengenpreis und
							// UsereingabeMengenpreis
							BigDecimal priceUserMenge = wnfMenge
									.getBigDecimal().multiply(
											wnfNettopreis.getBigDecimal());

							BigDecimal priceStaffelMenge = artikellieferantStaffelDto[j]
									.getNMenge().multiply(
											artikellieferantStaffelDto[j]
													.getNNettopreis());
							boolean answer;

							if (priceUserMenge.compareTo(priceStaffelMenge) > 0) {
								answer = getMeldungStaffelmengenpreisBilligerAlsUserMengenPreis(
										priceStaffelMenge, priceUserMenge);
							} else {

								answer = getMeldungMengeAnStaffelpreisAnpassen(artikellieferantStaffelDto[j]);
							}
							if (answer == true) {

								setzePreisfelder(wnfEinzelpreis, wnfNettopreis,
										wnfRabattsatz, wnfRabattsumme,
										mandantWaehrung, internalFrame,
										artlieferantDto,
										umrechnenInMandantwaehrung,
										artikellieferantStaffelDto[j]);

								wnfMenge.setBigDecimal(artikellieferantStaffelDto[j]
										.getNMenge());

							} else if (answer == false) {
								// fuer Abbruch der 1sten for() sonst wird 2 mal
								// abgefragt
								i = i + j;
							}

							// wenn einmal abgefragt wurde
							break;
						}
					}

				}
			}
		}
	}

	/**
	 * 
	 * @param priceStaffelMenge
	 *            BigDecimal
	 * @param priceUserMenge
	 *            BigDecimal
	 * @return boolean
	 */
	private static boolean getMeldungStaffelmengenpreisBilligerAlsUserMengenPreis(
			BigDecimal priceStaffelMenge, BigDecimal priceUserMenge) {
		MessageFormat mf = new MessageFormat(LPMain.getInstance()
				.getTextRespectUISPr(
						"bes.staffelpreiskleineralsangebebenemenge"));

		try {
			mf.setLocale(LPMain.getInstance().getTheClient().getLocUi());
		} catch (Throwable ex) {
		}

		Object pattern[] = { priceStaffelMenge, priceUserMenge };
		String sMsg = mf.format(pattern);

		boolean answer = (DialogFactory.showMeldung(sMsg, LPMain.getInstance()
				.getTextRespectUISPr("lp.hinweis"),
				javax.swing.JOptionPane.YES_NO_OPTION) == javax.swing.JOptionPane.YES_OPTION);

		return answer;

	}

	/**
	 * 
	 * @param artikellieferantStaffelDto
	 *            ArtikellieferantStaffelDto
	 * @return boolean
	 */
	private static boolean getMeldungMengeAnStaffelpreisAnpassen(
			ArtikellieferantstaffelDto artikellieferantStaffelDto) {
		MessageFormat mf = new MessageFormat(
				LPMain.getInstance().getTextRespectUISPr(
						"bes.staffelpreisindernahevonbestellmenge"));

		try {
			mf.setLocale(LPMain.getInstance().getTheClient().getLocUi());
		} catch (Throwable ex) {
		}

		Object pattern[] = { artikellieferantStaffelDto.getNMenge() };
		String sMsg = mf.format(pattern);

		boolean answer = (DialogFactory.showMeldung(sMsg, LPMain.getInstance()
				.getTextRespectUISPr("lp.hinweis"),
				javax.swing.JOptionPane.YES_NO_OPTION) == javax.swing.JOptionPane.YES_OPTION);

		return answer;

	}

	/**
	 * 
	 * @param wnfEinzelpreis
	 *            WrapperNumberField
	 * @param wnfNettopreis
	 *            WrapperNumberField
	 * @param wnfRabattsatz
	 *            WrapperNumberField
	 * @param wnfRabattsumme
	 *            WrapperNumberField
	 * @param mandantWaehrung
	 *            String
	 * @param internalFrame
	 *            InternalFrame
	 * @param artlieferantDto
	 *            ArtikellieferantDto
	 * @param umrechnenInMandantwaehrung
	 *            boolean
	 * @param artikellieferanStaffelDto
	 *            ArtikellieferantstaffelDto
	 * @throws Throwable
	 */
	private static void setzePreisfelder(WrapperNumberField wnfEinzelpreis,
			WrapperFixableNumberField wnfNettopreis,
			WrapperNumberField wnfRabattsatz,
			WrapperFixableNumberField wnfRabattsumme, String mandantWaehrung,
			InternalFrame internalFrame, ArtikellieferantDto artlieferantDto,
			boolean umrechnenInMandantwaehrung,
			ArtikellieferantstaffelDto artikellieferanStaffelDto)
			throws Throwable {

		// wenn true dann wird in mandantwaehrung umgerechnet ansonsten wird
		// wechselkurs
		// einfach mit 1 initalisiert und es passiert nix
		BigDecimal wechselkursLieferantnachMandantWaehrung = new BigDecimal(1);
		if (umrechnenInMandantwaehrung == true) {
			// holen des wechselkurses von lieferanten in mandantenwaehrung
			wechselkursLieferantnachMandantWaehrung = DelegateFactory
					.getInstance()
					.getLocaleDelegate()
					.getWechselkurs2(
							artlieferantDto.getLieferantDto().getWaehrungCNr(),
							mandantWaehrung);
		}

		// setzen der werte
		BigDecimal bdEinzelpreis = new BigDecimal(0);
		if (artlieferantDto.getNEinzelpreis() != null) {
			bdEinzelpreis = artlieferantDto.getNEinzelpreis().multiply(
					wechselkursLieferantnachMandantWaehrung);
		}

		wnfNettopreis.setBigDecimal(artikellieferanStaffelDto.getNNettopreis()
				.multiply(wechselkursLieferantnachMandantWaehrung));

		wnfRabattsatz.setBigDecimal(new BigDecimal(artikellieferanStaffelDto
				.getFRabatt().doubleValue()));

		wnfEinzelpreis.setBigDecimal(bdEinzelpreis);
		// ausrechnen der Rabattsumme
		BigDecimal bdRabattsumme = bdEinzelpreis.multiply(wnfRabattsatz
				.getBigDecimal().movePointLeft(2));
		wnfRabattsumme.setBigDecimal(bdRabattsumme);
	}

	public int compare(Object a, Object b) {
		ArtikellieferantstaffelDto artDto1;
		ArtikellieferantstaffelDto artDto2;

		artDto1 = (ArtikellieferantstaffelDto) a;
		artDto2 = (ArtikellieferantstaffelDto) b;

		if (artDto1.getNMenge().doubleValue() > artDto2.getNMenge()
				.doubleValue()) {
			return 1;
		} else if (artDto1.getNMenge().doubleValue() == artDto2.getNMenge()
				.doubleValue()) {
			return 0;
		} else {
			return -1;
		}
	}

	/**
	 * setzt den Richtigen status: wenn alle Positionen bestaetigt dann Status:
	 * bestaetigt sonst Status: offen in Abfrage
	 * 
	 * @param bestellungDto
	 *            BestellungDto
	 * @param internalFrameBestellung
	 *            InternalFrameBestellung
	 * @return String
	 * @throws ExceptionLP
	 * @throws Throwable
	 */
	public synchronized static String showBestellstatusWennVonGeliefertAufOffenOderBestaetigt(
			BestellungDto bestellungDto,
			InternalFrameBestellung internalFrameBestellung)
			throws ExceptionLP, Throwable {

		BestellpositionDto besposDto[] = null;

		besposDto = DelegateFactory.getInstance().getBestellungDelegate()
				.bestellpositionFindByBestellung(bestellungDto.getIId());
		int countBestaetigt = 0;
		String sStatus = null;
		for (int i = 0; i < besposDto.length; i++) {
			if (besposDto[i].getBestellpositionstatusCNr().equals(
					BestellungFac.BESTELLSTATUS_BESTAETIGT)) {
				countBestaetigt++;
			}
		}

		if (countBestaetigt == besposDto.length) {
			sStatus = BestellungFac.BESTELLSTATUS_BESTAETIGT;
		} else {
			sStatus = BestellungFac.BESTELLSTATUS_OFFEN;
		}
		return sStatus.trim();
	}

	/**
	 * checkt ob schon WEP auf eine Bestellposition sind aufruf in
	 * eventActionUpdate eventActionNew
	 * 
	 * @return boolean
	 * @param internalFrameBestellung
	 *            InternalFrameBestellung
	 * @param bestellpositionDto
	 *            BestellpositionDto
	 * @param bestellungDto
	 *            BestellungDto
	 */
	public static boolean canChangeBestellpositionbeiUpdate(
			InternalFrameBestellung internalFrameBestellung,
			BestellpositionDto bestellpositionDto, BestellungDto bestellungDto) {
		boolean goAheadbeiUpdate = false;
		try {
			WareneingangspositionDto[] wepDto = DelegateFactory
					.getInstance()
					.getWareneingangDelegate()
					.wareneingangspositionFindByBestellpositionIId(
							bestellpositionDto.getIId());

			if (bestellungDto.getStatusCNr().equals(
					BestellungFac.BESTELLSTATUS_OFFEN)
					&& wepDto.length != 0) {

				int answeroption = DialogFactory
						.showModalJaNeinAbbrechenDialog(
								internalFrameBestellung,
								"Diese Bestellposition hat bereits WEP wollen Sie die Position wirklich ver\u00E4ndern",
								"lp.warning");
				/**
				 * @todo
				 */
				if (answeroption == JOptionPane.OK_OPTION) {
					goAheadbeiUpdate = true;
				}
			} else {
				goAheadbeiUpdate = true;
			}
			return goAheadbeiUpdate;
		} catch (Throwable ex) {
			// nothing in here
		}
		return goAheadbeiUpdate;
	}

}
