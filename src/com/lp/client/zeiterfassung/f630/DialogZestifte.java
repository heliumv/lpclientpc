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
package com.lp.client.zeiterfassung.f630;

import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;

import java.awt.Button;
import java.awt.Color;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.StringTokenizer;
import java.util.TooManyListenersException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileFilter;

import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.util.logger.LpLogger;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.fertigung.service.LosDto;
import com.lp.server.fertigung.service.LossollarbeitsplanDto;
import com.lp.server.personal.service.MaschineDto;
import com.lp.server.personal.service.PersonalDto;
import com.lp.server.personal.service.TaetigkeitDto;
import com.lp.server.personal.service.ZeitdatenDto;
import com.lp.server.personal.service.ZeiterfassungFac;
import com.lp.server.personal.service.ZeitstiftDto;
import com.lp.server.personal.service.ZeitverteilungDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.util.Helper;
import com.lp.util.HelperReport;

@SuppressWarnings("static-access")
public class DialogZestifte extends Frame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JPanel panel1 = new JPanel();
	GridBagLayout gridBagLayout1 = new GridBagLayout();
	JLabel labelDelay = new JLabel();
	JTextField textfieldLocale = new JTextField();
	JLabel labelPassword = new JLabel();
	JScrollPane jScrollPane = new JScrollPane();
	JEditorPane textPanelLog = new JEditorPane();
	JLabel labelUsername = new JLabel();
	JLabel labelLocale = new JLabel();
	JTextField textfieldUsername = new JTextField();
	JPasswordField passwordfieldPassword = new JPasswordField();
	JLabel labelPort = new JLabel();
	JTextField textfieldPort = new JTextField();
	JTextField textfieldDelay = new JTextField();
	JLabel labelMs = new JLabel();
	public JButton jButtonStart = new JButton();
	JButton jButtonStop = new JButton();
	JButton jButtonClear = new JButton();
	static CommPortIdentifier portID;
	InputStream ins;
	OutputStream out;
	static SerialPort serss;
	TextField ausgabe;
	Button bt;
	Locale loc = null;
	boolean bGutstueckerhoehen = false;

	JButton jButtonDateiEinlesen = new JButton();

	boolean bInDateiEinlesen = false;

	int blockCount = 0;
	final static char ASCII_ACK = '\u0006';
	final static char ASCII_NACK = '\u0015';
	final static int SYNC_CHAR = '-';
	final static char CHAR_CR = 13;
	final static String SITFT_TYP_F630 = "F630";
	final static String VERSION = "Helium V Zeit V1";

	protected final LpLogger myLogger = (LpLogger) LpLogger.getInstance(this
			.getClass());

	int pauseNachSenden = 500;

	final static String STRING_ENDE = "0000FF";
	JScrollPane jScrollPane1 = new JScrollPane();

	private ZETimer timer = null;

	public DialogZestifte(ResourceBundle ressourceBundle, ZETimer timer) {
		myLogger.info("ZE-Stift Importprogramm gestartet");

		this.timer = timer;
		try {
			jbInit();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		ImageIcon ii = new ImageIcon(getClass().getResource(
				"/com/lp/client/res/scanner16x16.png"));
		setIconImage(ii.getImage());

		setTitle(ressourceBundle.getString("zestift.titel"));

		// PORT aus properties-File setzten
		String port = ressourceBundle.getString("zestift.port");
		String locale = ressourceBundle.getString("zestift.locale");

		String delay = ressourceBundle.getString("zestift.delay");

		Integer iInDateiEinlesen;
		try {
			iInDateiEinlesen = new Integer(ressourceBundle.getString(
					"zestift.indateieinlesen").trim());
			if (iInDateiEinlesen == 1) {
				bInDateiEinlesen = true;
			}

		} catch (MissingResourceException e1) {
			logToTextPane("Property zestift.indateieinlesen fehlt");
		}

		
		textfieldDelay.setText(delay);
		loc = new Locale(locale);

		textfieldPort.setText(port);
		textfieldLocale.setText(locale);
		textfieldUsername
				.setText(ressourceBundle.getString("zestift.username"));

		try {
			pauseNachSenden = new Integer(delay).intValue();
		} catch (NumberFormatException ex1) {
			// default
			pauseNachSenden = 500;
		}

		try {
			ParametermandantDto parameter = (ParametermandantDto) DelegateFactory
					.getInstance().getParameterDelegate().getParametermandant(
							ParameterFac.PARAMETER_ZESTIFT_STUECKRUECKMELDUNG,
							ParameterFac.KATEGORIE_PERSONAL,
							LPMain.getInstance().getTheClient().getMandant());

			if (parameter.getCWertAsObject() != null) {
				bGutstueckerhoehen = ((Boolean) parameter.getCWertAsObject())
						.booleanValue();

			}
		} catch (Throwable ex2) {
			logToTextPane(ex2.toString());
		}

		initPort(port);
		timer.start();
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				myLogger.info("ZE-Stift Importprogramm gestoppt");
				dispose();
				serss.close();
				System.exit(0);
			}
		});

	}

	private void warten(int ms) {
		try {
			Thread.currentThread().sleep(ms);
		} catch (InterruptedException ex) {
			logToTextPane(htmlRot(ex.toString()));
		}

	}

	private boolean istErstesZeichenGueltig(String string) {
		if (string != null && string.length() > 0
				&& string.charAt(0) == SYNC_CHAR) {
			return true;
		} else {
			sendeNAK();
			return false;
		}
	}

	private int berechneChecksumme(String string) {
		int chksum = -1;
		if (string != null) {
			chksum = 0;
			for (int i = 0; i < string.length(); i++) {
				char c = string.charAt(i);
				chksum += c;
				chksum = chksum & 255;
			}
		}

		return chksum;
	}

	private static String rechneNachHex(int dezimal) {
		String ergebnis = "";
		String hexarray[] = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
				"A", "B", "C", "D", "E", "F" };
		int rest = Math.abs(dezimal);
		int aktbasiszahl = 0;
		if (rest > 0) {
			while (rest > 0) {
				aktbasiszahl = rest % 16;
				rest = (rest / 16);
				ergebnis = hexarray[aktbasiszahl] + ergebnis;
			}
		} else {
			ergebnis = "00";
		}
		if (ergebnis.length() == 1) {
			ergebnis = "0" + ergebnis;
		}

		return ergebnis;
	}

	private void stiftEinlesenUndSetzen(ZeitstiftDto zeitstiftDto)
			throws Throwable {

		boolean bZeitverteilungBeginnGebucht = false;
		blockCount = 0;
		String stiftId = zeitstiftDto.getCNr().trim();
		if (istStiftVorhanden(stiftId)) {
			String kopfdaten = fordereStiftKopfdatenAn(stiftId);

			if (kopfdaten != null && istChecksummeRichtig(kopfdaten)) {
				StringTokenizer tokKopfdaten = new StringTokenizer(kopfdaten,
						";");
				if (tokKopfdaten.countTokens() == 5) {
					String chkDatum = tokKopfdaten.nextToken();
					String datum = chkDatum.substring(3);
					// datum muss mit pc-zeit zusamenstimmen

					String stiftID = tokKopfdaten.nextToken();

					if (!stiftId.equals(stiftID)) {
						sendeNAK();
						return;
					}

					String anzDatensaetze = tokKopfdaten.nextToken();
					String stiftTyp = tokKopfdaten.nextToken();

					if (stiftTyp == null || !stiftTyp.equals(SITFT_TYP_F630)) {
						sendeNAK();
						return;
					}
					String version = tokKopfdaten.nextToken();
					version = version.substring(0, version.length() - 3);

					if (!version.startsWith(VERSION)) {

						logToTextPane(htmlRot("Die Version des ZE-Stiftes entspricht nicht "
								+ VERSION));

						sendeNAK();
						return;
					}

					int iAnzDatensaetze = 0;
					try {
						iAnzDatensaetze = new Integer(anzDatensaetze)
								.intValue();
					} catch (NumberFormatException ex) {
						sendeNAK();
						return;
					}

					sendeACK();

					ArrayList<String> al = new ArrayList<String>();

					for (int i = 0; i < iAnzDatensaetze; i++) {
						String zeile = getDatenzeile();
						int versuche = 0;
						while (!istErstesZeichenGueltig(zeile)
								|| !istChecksummeRichtig(zeile)) {
							versuche++;
							if (versuche > 5) {
								return;
							}

							zeile = getDatenzeile();
						}
						al.add(zeile.substring(4));

						sendeACK();

					}

					// Wenn alle uebertragen und der letzte -0000FF, dann OK
					String zeile = getDatenzeile();
					if (istErstesZeichenGueltig(zeile)
							&& zeile.equals("-" + STRING_ENDE)) {

						sendeACK();

						// Daten aufbereiten
						ZeitdatenDto[] createDtos = new ZeitdatenDto[al.size()];

						for (int i = 0; i < al.size(); i++) {
							String datenzeile = (String) al.get(i);

							String[] tokens = zerlegeString(datenzeile);

							String ckkDatum = tokens[0];
							String ausweis = tokens[1];
							String losTaetigkeit = tokens[2];
							String maschine = tokens[3];

							String artikel = tokens[4];

							if (losTaetigkeit != null
									&& losTaetigkeit.equals("$Q")) {
								// Zeile auslassen (Stueckrueckmeldung)
							} else {

								ZeitdatenDto dto = new ZeitdatenDto();

								// Personal holen
								PersonalDto personalDto = DelegateFactory
										.getInstance().getPersonalDelegate()
										.personalFindByCAusweis(ausweis);
								if (personalDto == null) {
									logToTextPane(htmlRot("Ausweis "
											+ ausweis
											+ " nicht gefunden. Es wird der angemeldete Benutzer verwendet"));
									dto.setPersonalIId(LPMain.getTheClient()
											.getIDPersonal());
									dto
											.setCBemerkungZuBelegart("Ausweisnummer nicht gefunden:"
													+ ausweis);
								} else {
									dto.setPersonalIId(personalDto.getIId());
								}
								// Zeit auselesen
								dto.setTZeit(stringToTimestamp(ckkDatum));

								dto.setCWowurdegebucht("F630 ID:" + stiftId);

								// AUFTRAG/LOS bzw. TAETIGKEIT
								if (artikel != null && !artikel.equals("")) {
									ArtikelDto artikelDto = null;
									try {

										if (artikel.equals("$PLUS")) {
											// Wenn schon Zeitverteilung fuer
											// heute vorhanden, dann diese zuvor
											// beenden
											if (bZeitverteilungBeginnGebucht == false) {
												DelegateFactory
														.getInstance()
														.getZeiterfassungDelegate()
														.zeitAufLoseVerteilen(
																personalDto
																		.getIId(),
																dto.getTZeit());
											}

											// Zeitverteilung beginn
											ZeitverteilungDto zvDto = new ZeitverteilungDto();

											zvDto.setPersonalIId(personalDto
													.getIId());
											zvDto.setTZeit(dto.getTZeit());

											LosDto losDto = DelegateFactory
													.getInstance()
													.getFertigungDelegate()
													.losFindByCNrMandantCNr(
															losTaetigkeit,
															LPMain
																	.getInstance()
																	.getTheClient()
																	.getMandant());

											zvDto.setLosIId(losDto.getIId());
											DelegateFactory
													.getInstance()
													.getZeiterfassungDelegate()
													.createZeitverteilung(zvDto);
											bZeitverteilungBeginnGebucht = true;
											continue;
										} else {
											artikelDto = DelegateFactory
													.getInstance()
													.getArtikelDelegate()
													.artikelFindByCNr(artikel);
											dto.setArtikelIId(artikelDto
													.getIId());
											// DERZEIT NUR AUF LOS:
											LosDto losDto = DelegateFactory
													.getInstance()
													.getFertigungDelegate()
													.losFindByCNrMandantCNr(
															losTaetigkeit,
															LPMain
																	.getInstance()
																	.getTheClient()
																	.getMandant());
											dto.setIBelegartid(losDto.getIId());
											dto
													.setCBelegartnr(LocaleFac.BELEGART_LOS);

											//

											if (bZeitverteilungBeginnGebucht == true) {
												// Zeitverteilung beenden
												ZeitverteilungDto zvDto = new ZeitverteilungDto();

												zvDto
														.setPersonalIId(personalDto
																.getIId());
												zvDto.setTZeit(dto.getTZeit());
												zvDto.setArtikelIId(artikelDto
														.getIId());
												zvDto
														.setLosIId(losDto
																.getIId());
												DelegateFactory
														.getInstance()
														.getZeiterfassungDelegate()
														.createZeitverteilung(
																zvDto);
												bZeitverteilungBeginnGebucht = false;
												continue;
											}

											LossollarbeitsplanDto[] dtos = DelegateFactory
													.getInstance()
													.getFertigungDelegate()
													.lossollarbeitsplanFindByLosIIdArtikelIIdTaetigkeit(
															losDto.getIId(),
															artikelDto.getIId());

											if (bGutstueckerhoehen == true) {
												/*
												 * dto .setNGut(new
												 * java.math.BigDecimal( 1));
												 */
											}

											if (al.size() > i + 1) {
												String datenzeile_next = (String) al
														.get(i + 1);

												String[] tokens_next = zerlegeString(datenzeile_next);

												String ckkDatum_next = tokens_next[0];
												String ausweis_next = tokens_next[1];
												String losTaetigkeit_next = tokens_next[2];
												String maschine_next = tokens_next[3];
												String menge = tokens_next[4];
												if (losTaetigkeit_next != null
														&& losTaetigkeit_next
																.startsWith("$Q")) {
													Integer iGutstueck = new Integer(
															menge);
													/*
													 * dto .setNGut(new
													 * java.math.BigDecimal(
													 * iGutstueck));
													 */
												}

											}
											if (dtos != null && dtos.length > 0) {
												dto
														.setIBelegartpositionid(dtos[0]
																.getIId());
											} else {
												LossollarbeitsplanDto[] dtosErstePosition = DelegateFactory
														.getInstance()
														.getFertigungDelegate()
														.lossollarbeitsplanFindByLosIId(
																losDto.getIId());
												if (dtosErstePosition != null
														&& dtosErstePosition.length > 0) {
													dto
															.setIBelegartpositionid(dtosErstePosition[0]
																	.getIId());
												} else {
													// Ende + Bemerkung
													TaetigkeitDto taetigkeitDto = DelegateFactory
															.getInstance()
															.getZeiterfassungDelegate()
															.taetigkeitFindByCNr(
																	ZeiterfassungFac.TAETIGKEIT_ENDE);
													dto
															.setTaetigkeitIId(taetigkeitDto
																	.getIId());
													dto.setArtikelIId(null);
													dto.setCBelegartnr(null);
													dto.setIBelegartid(null);
													dto
															.setIBelegartpositionid(null);
													/*
													 * dto.setMaschineIId(null);
													 * dto.setNGut(null);
													 */
													dto
															.setCBemerkungZuBelegart("Fehler aus ZE-Stift: Los "
																	+ losDto
																			.getCNr()
																	+ " hat keine Positionen");

												}
											}

										}
									} catch (Throwable ex1) {
										// Ende + Bemerkung
										TaetigkeitDto taetigkeitDto = DelegateFactory
												.getInstance()
												.getZeiterfassungDelegate()
												.taetigkeitFindByCNr(
														ZeiterfassungFac.TAETIGKEIT_ENDE);
										dto.setTaetigkeitIId(taetigkeitDto
												.getIId());
										dto.setArtikelIId(null);
										dto.setCBelegartnr(null);
										dto.setIBelegartid(null);
										dto.setIBelegartpositionid(null);
										// dto.setMaschineIId(null);
										String bemerkung = "Fehler aus ZE-Stift: "
												+ losTaetigkeit
												+ ", "
												+ artikel + ", " + maschine;

										dto.setCBemerkungZuBelegart(bemerkung);
									}
									// MASCHINE
									if (maschine != null
											&& !maschine.trim().equals("")) {

										if (maschine.equals("--")) {
											dto.bFertigFuerLossollarbeitsplan = true;
										} else {

											try {
												MaschineDto maschineDto = DelegateFactory
														.getInstance()
														.getZeiterfassungDelegate()
														.maschineFindByCIdentifikationsnr(
																maschine);
												//dto.setMaschineIId(maschineDto
												// .getIId());
											} catch (Throwable ex1) {
												dto
														.setCBemerkungZuBelegart(dto
																.getCBemerkungZuBelegart()
																+ ", Maschine "
																+ maschine
																+ " nicht gefunden.");
											}
										}
									}
								} else {
									TaetigkeitDto taetigkeitDto = DelegateFactory
											.getInstance()
											.getZeiterfassungDelegate()
											.taetigkeitFindByCNr(
													losTaetigkeit.substring(1));
									dto
											.setTaetigkeitIId(taetigkeitDto
													.getIId());

								}

								/*
								 * Integer taetigkeitIId_STOP = DelegateFactory
								 * .getInstance() .getZeiterfassungDelegate()
								 * .taetigkeitFindByCNr(
								 * ZeiterfassungFac.TAETIGKEIT_STOP) .getIId();
								 * if (dto.getTaetigkeitIId() != null &&
								 * dto.getTaetigkeitIId().equals(
								 * taetigkeitIId_STOP) && dto.getMaschineIId()
								 * == null) { // Ende + Bemerkung TaetigkeitDto
								 * taetigkeitDto = DelegateFactory
								 * .getInstance() .getZeiterfassungDelegate()
								 * .taetigkeitFindByCNr(
								 * ZeiterfassungFac.TAETIGKEIT_ENDE); dto
								 * .setTaetigkeitIId(taetigkeitDto .getIId());
								 * dto.setArtikelIId(null);
								 * dto.setCBelegartnr(null);
								 * dto.setIBelegartid(null);
								 * dto.setIBelegartpositionid(null);
								 * dto.setMaschineIId(null); String bemerkung =
								 * "Fehler aus ZE-Stift: STOP ohne Maschine.";
								 * dto.setCBemerkungZuBelegart(bemerkung); }
								 */

								createDtos[i] = dto;
							}
						}
						// Leere Zeilen herausfiltern (wg. Stueckrueckmeldung)
						int anzahl = 0;
						for (int i = 0; i < createDtos.length; i++) {
							if (createDtos[i] != null) {
								anzahl++;
							}
						}
						ZeitdatenDto[] dtosTemp = new ZeitdatenDto[anzahl];
						int akt = 0;
						for (int i = 0; i < createDtos.length; i++) {
							if (createDtos[i] != null) {
								dtosTemp[akt] = createDtos[i];
								akt++;
							}
						}

						if (bInDateiEinlesen) {
							FileOutputStream fs = new FileOutputStream(File
									.createTempFile(zeitstiftDto.getCNr()
											.trim()
											+ "_", ".F630"));
							ObjectOutputStream os = new ObjectOutputStream(fs);
							os.writeObject(dtosTemp);
							os.flush();
							os.close();
						} else {

							DelegateFactory.getInstance()
									.getZeiterfassungDelegate()
									.speichereZeidatenVonZEStift(dtosTemp);

							if (istStiftVorhanden(zeitstiftDto.getCNr().trim())) {
								// Dem Stift sagen, dass jetzt was kommt
								String stirng = zeitstiftDto.getCNr().trim()
										+ "D" + CHAR_CR;
								sendeZeichenkette(stirng.toCharArray());
								if (returnedACK()) {
									// Header schicken
									sendBlock("1", "LOGISTIK PUR Daten");
									if (returnedACK()) {
										// DATUM + Zeit setzten
										setzeDatumUhrzeit();
										if (returnedACK()) {
											if (Helper
													.short2Boolean(zeitstiftDto
															.getBMehrfachstift())) {
												sendBlock("U", "P0***MEHRFACH ");
											} else if (!Helper
													.short2Boolean(zeitstiftDto
															.getBPersonenzuordnung())) {
												PersonalDto personalDto = DelegateFactory
														.getInstance()
														.getPersonalDelegate()
														.personalFindByPrimaryKey(
																zeitstiftDto
																		.getPersonalIId());
												sendBlock(
														"U",
														"P1"
																+ personalDto
																		.getCAusweis()
																+ HelperReport
																		.ersetzeUmlauteUndSchneideAb(
																				personalDto
																						.getPartnerDto()
																						.formatFixName2Name1(),
																				16));

											} else {
												sendBlock(
														"U",
														"P2"
																+ "HV1"
																+ HelperReport
																		.ersetzeUmlauteUndSchneideAb(
																				"HELIUMV11111111111",
																				16));

											}
											if (returnedACK()) {
												// STIFTDATEN LOESCHEN
												sendBlock("U", "D");
												if (returnedACK()) {

													sendBlock(STRING_ENDE, "");
													if (returnedACK()) {
														stiftAusschalten(stiftId);
													}
												}
											}
										}
									}
								}
							}
						}
					}
				} else {
					sendeNAK();
				}
			}
		}

	}

	public class sendeListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			timer.start();

		}
	}

	public void starteDurchgang() {
		timer.bProzessInArbeit = true;
		try {
			ZeitstiftDto[] dtos = null;
			dtos = DelegateFactory.getInstance().getZeiterfassungDelegate()
					.zeitstiftFindByMandantCNr(
							LPMain.getInstance().getTheClient());

			for (int anzStifte = 0; anzStifte < dtos.length; anzStifte++) {
				stiftEinlesenUndSetzen(dtos[anzStifte]);
				warten(1000);
			}
		} catch (Throwable ex3) {
			logToTextPane(htmlRot(ex3.toString()));
		}
	}

	private Timestamp stringToTimestamp(String timestamp) {
		Calendar c = Calendar.getInstance(loc);
		int iJahr = new Integer(timestamp.substring(0, 2)).intValue();
		if (iJahr < 50) {
			c.set(Calendar.YEAR, 2000 + iJahr);
		} else {
			c.set(Calendar.YEAR, 1900 + iJahr);
		}
		c.set(Calendar.MONTH,
				new Integer(timestamp.substring(2, 4)).intValue() - 1);
		c.set(Calendar.DATE, new Integer(timestamp.substring(4, 6)).intValue());
		c.set(Calendar.HOUR_OF_DAY, new Integer(timestamp.substring(6, 8))
				.intValue());
		c.set(Calendar.MINUTE, new Integer(timestamp.substring(8, 10))
				.intValue());
		c.set(Calendar.SECOND, new Integer(timestamp.substring(10, 12))
				.intValue());
		c.set(Calendar.MILLISECOND, 0);

		return new Timestamp(c.getTimeInMillis());
	}

	private void stiftAusschalten(String stiftId) {
		String aussch = stiftId + "0" + CHAR_CR;
		sendeZeichenkette(aussch.toCharArray());
	}

	private void initPort(String port) {
		try {
			portID = CommPortIdentifier.getPortIdentifier(port);

			serss = (SerialPort) portID.open("mein_programm", 2000);

			ins = serss.getInputStream();
			out = serss.getOutputStream();
			serss.addEventListener(new commListener());
			serss.notifyOnDataAvailable(true);

			serss.setSerialPortParams(9600, SerialPort.DATABITS_8,
					SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
			logToTextPane(htmlBlau("Warten auf Antwort der \u00DCbertragungsstation auf "
					+ port));
		} catch (UnsupportedCommOperationException ex) {
			logToTextPane(htmlRot(ex + ""));
		} catch (TooManyListenersException ex) {
			logToTextPane(htmlRot(ex + ""));
		} catch (IOException ex) {
			logToTextPane(htmlRot(ex + ""));
		} catch (PortInUseException ex) {
			logToTextPane(htmlRot("Port '" + port + "' derzeit in Verwendung"));
		} catch (NoSuchPortException ex) {
			logToTextPane(htmlRot("Port '" + port + "' nicht vorhanden."));
		}

	}

	private void setzeDatumUhrzeit() {
		String datumUhrzeit = "U";

		Calendar c = Calendar.getInstance(loc);
		c.setTimeInMillis(System.currentTimeMillis());

		int wert = c.get(Calendar.YEAR);
		wert = wert % 100;

		if (wert < 10) {
			datumUhrzeit += "0" + wert;
		} else {
			datumUhrzeit += wert;

		}

		wert = c.get(Calendar.MONTH) + 1;
		if (wert < 10) {
			datumUhrzeit += "0" + wert;
		} else {
			datumUhrzeit += wert;

		}

		wert = c.get(Calendar.DAY_OF_MONTH);
		if (wert < 10) {
			datumUhrzeit += "0" + wert;
		} else {
			datumUhrzeit += wert;

		}

		wert = c.get(Calendar.HOUR_OF_DAY);
		if (wert < 10) {
			datumUhrzeit += "0" + wert;
		} else {
			datumUhrzeit += wert;

		}
		wert = c.get(Calendar.MINUTE);
		if (wert < 10) {
			datumUhrzeit += "0" + wert;
		} else {
			datumUhrzeit += wert;

		}
		wert = c.get(Calendar.SECOND);
		if (wert < 10) {
			datumUhrzeit += "0" + wert;
		} else {
			datumUhrzeit += wert;

		}

		sendBlock("U", datumUhrzeit);
	}

	private void sendBlock(String type, String string) {

		String outString = "";
		if (type != null && string != null) {

			if (type.equals(STRING_ENDE)) {
				outString = STRING_ENDE;
			} else {
				outString = rechneNachHex(string.length());
				outString += type;
				outString += string;
				outString += blockCount;
				blockCount++;
				if (blockCount > 9) {
					blockCount = 0;
				}
				outString += rechneNachHex(berechneChecksumme(outString));

			}
			outString = "-" + outString + CHAR_CR;

			sendeZeichenkette(outString.toCharArray());
		}

	}

	private void sendeACK() {
		char[] chars = new char[1];
		chars[0] = ASCII_ACK;
		sendeZeichenkette(chars);
	}

	private void sendeNAK() {
		char[] chars = new char[1];
		chars[0] = ASCII_NACK;
		sendeZeichenkette(chars);
	}

	private void sendeZeichenkette(char[] chars) {

		try {
			// AUF EMPFANGEN UMSCHALTEN
			serss.setFlowControlMode(SerialPort.FLOWCONTROL_RTSCTS_IN);
		} catch (UnsupportedCommOperationException ex1) {
			ex1.printStackTrace();
		}
		PrintWriter aus = new PrintWriter(out);
		ausgabe.setText("");
		aus.print(chars);
		aus.flush();
		warten(50);

		if (chars != null & chars[0] == ASCII_ACK) {
			logToTextPane(htmlBlau("OUT: ACK"));
		} else if (chars != null & chars[0] == ASCII_NACK) {
			logToTextPane(htmlRot("OUT: NAK"));
		} else {
			logToTextPane(htmlBlau("OUT:" + new String(chars)));
		}
		aus.close();
		warten(pauseNachSenden);
		try {
			// AUF SENDEN UMSCHALTEN
			serss.setFlowControlMode(SerialPort.FLOWCONTROL_RTSCTS_OUT);
			serss.setRTS(true);
		} catch (UnsupportedCommOperationException ex) {
			logToTextPane(htmlRot("" + ex));
		}
	}

	private boolean returnedACK() {
		if (ausgabe.getText() != null && ausgabe.getText().length() != 0
				&& ausgabe.getText().charAt(0) == ASCII_ACK) {
			ausgabe.setText("");
			logToTextPane(htmlBlau("IN: ACK"));
			return true;
		} else if (ausgabe.getText() != null && ausgabe.getText().length() != 0
				&& ausgabe.getText().charAt(0) == ASCII_NACK) {
			ausgabe.setText("");
			logToTextPane(htmlRot("IN: NAK"));
			sendeNAK();
			return false;
		} else {
			logToTextPane(htmlRot("IN: " + ausgabe.getText()));
			sendeNAK();
			return false;
		}

	}

	private boolean istStiftVorhanden(String sStiftId) {

		sStiftId = sStiftId + "?" + CHAR_CR;
		sendeZeichenkette(sStiftId.toCharArray());
		if (ausgabe.getText() != null && ausgabe.getText().length() != 0
				&& ausgabe.getText().charAt(0) == ASCII_ACK) {
			ausgabe.setText("");
			logToTextPane(htmlBlau("IN: ACK"));
			return true;
		} else {
			return false;
		}

	}

	private String getDatenzeile() {
		String zeile = ausgabe.getText();
		ausgabe.setText("");
		logToTextPane(htmlBlau("IN: " + zeile));
		return zeile;
	}

	private String htmlBlau(String s) {
		if (s != null) {
			s = "<font color=\"#0000FF\" face=\"Verdana\" size=\"2\">" + s
					+ "</font>";
		}
		return s;
	}

	private String htmlRot(String s) {
		if (s != null) {
			s = "<font color=\"#FF0000\" face=\"Verdana\" size=\"2\">" + s
					+ "</font>";
		}
		return s;
	}

	private String[] zerlegeString(String datenzeile) {
		ArrayList<String> al = new ArrayList<String>();
		int letzterStrichpunkt = 0;
		for (int i = 0; i < datenzeile.length(); i++) {
			if (datenzeile.charAt(i) == ';') {
				al.add(datenzeile.substring(letzterStrichpunkt, i));
				letzterStrichpunkt = i + 1;
			}
			if (i == datenzeile.length() - 1) {
				al.add(datenzeile.substring(letzterStrichpunkt));
			}
		}

		String[] s = new String[al.size()];
		return (String[]) al.toArray(s);
	}

	private void logToTextPane(String s) {
		textPanelLog.setText(s + "\n" + textPanelLog.getText());
		if (textPanelLog.getText() != null
				&& textPanelLog.getText().length() > 30000) {
			textPanelLog.removeAll();
			textPanelLog.setText(s + "\n");
		}
	}

	private String fordereStiftKopfdatenAn(String sStiftId) {

		sStiftId = sStiftId + "!" + CHAR_CR;
		sendeZeichenkette(sStiftId.toCharArray());
		String zeile = getDatenzeile();
		int versuche = 0;
		while (!istErstesZeichenGueltig(zeile) || !istChecksummeRichtig(zeile)) {
			versuche++;
			if (versuche > 10) {
				return null;
			}
			zeile = getDatenzeile();

		}
		return zeile;

	}

	private boolean istChecksummeRichtig(String zeile) {
		if (zeile != null && zeile.length() > 3) {
			String sStringOhneChkSum = zeile.substring(1, zeile.length() - 2);
			String sChkSum = zeile
					.substring(zeile.length() - 2, zeile.length());

			if (sChkSum
					.equals(rechneNachHex(berechneChecksumme(sStringOhneChkSum)))) {
				return true;
			} else {
				sendeNAK();
				return false;
			}
		} else {
			sendeNAK();
			return false;
		}
	}

	private void jbInit() throws Exception {
		panel1.setLayout(gridBagLayout1);
		labelDelay.setHorizontalAlignment(SwingConstants.RIGHT);
		labelDelay.setText("Pause nach senden:");
		labelPassword.setHorizontalAlignment(SwingConstants.RIGHT);
		labelPassword.setText("Password");
		labelUsername.setForeground(Color.black);
		labelUsername.setHorizontalAlignment(SwingConstants.RIGHT);
		labelUsername.setHorizontalTextPosition(SwingConstants.RIGHT);
		labelUsername.setText("Benutzername");
		labelLocale.setHorizontalAlignment(SwingConstants.RIGHT);
		labelLocale.setText("Locale");
		passwordfieldPassword.setEnabled(false);
		passwordfieldPassword.setText("jPasswordField1");
		labelPort.setHorizontalAlignment(SwingConstants.RIGHT);
		labelPort.setText("Port:");
		labelMs.setText("ms");
		textPanelLog.setEditable(false);
		textfieldUsername.setEditable(false);
		textfieldDelay.setEditable(false);
		textfieldLocale.setEditable(false);
		textfieldPort.setEditable(false);
		jButtonStart.setText("START");
		jButtonStop.setText("STOP");
		jButtonStop
				.addActionListener(new DialogZestifte_jButtonStop_actionAdapter(
						this));
		jButtonClear.setText("CLEAR");
		jButtonClear
				.addActionListener(new DialogZestifte_jButtonClear_actionAdapter(
						this));

		jButtonDateiEinlesen.setText("DATEI EINLESEN");
		jButtonDateiEinlesen
				.addActionListener(new DialogZestifte_jButtonDateieinlesen_actionAdapter(
						this));

		add(panel1);
		jScrollPane.getViewport().add(textPanelLog);
		textPanelLog.setContentType("text/html");

		jButtonStart.addActionListener(new sendeListener());
		// add(ausgabe = new TextField("", 100));
		ausgabe = new TextField("", 100);

		int iZeile = 0;
		panel1.add(jButtonStop, new GridBagConstraints(2, iZeile, 3, 1, 0.0,
				0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(15, 15, 15, 0), 50, 0));
		panel1.add(jButtonStart, new GridBagConstraints(0, iZeile, 2, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(15, 15, 15, 0), 50, 0));
		panel1.add(jButtonClear, new GridBagConstraints(3, iZeile, 2, 1, 0.0,
				0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(15, 0, 15, 15), 50, 0));
		iZeile++;
		panel1.add(jButtonDateiEinlesen, new GridBagConstraints(0, iZeile, 3,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 15, 0, 0), 50, 0));
		iZeile++;
		panel1.add(labelUsername, new GridBagConstraints(0, iZeile, 2, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		panel1.add(textfieldUsername, new GridBagConstraints(2, iZeile, 3, 1,
				0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		panel1.add(labelPassword, new GridBagConstraints(0, iZeile, 2, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		panel1.add(passwordfieldPassword, new GridBagConstraints(2, iZeile, 3,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		panel1.add(labelMs, new GridBagConstraints(4, iZeile, 1, 3, 0.0, 0.0,
				GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 20, 0));

		panel1.add(labelDelay, new GridBagConstraints(0, iZeile, 2, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		panel1.add(textfieldDelay, new GridBagConstraints(2, iZeile, 2, 1, 1.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;

		panel1.add(labelLocale, new GridBagConstraints(0, iZeile, 2, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		panel1.add(textfieldLocale, new GridBagConstraints(2, iZeile, 3, 1,
				0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		panel1.add(labelPort, new GridBagConstraints(0, iZeile, 2, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		panel1.add(textfieldPort, new GridBagConstraints(2, iZeile, 3, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		panel1.add(ausgabe, new GridBagConstraints(0, iZeile, 5, 1, 1.0, 0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						0, 0, 0, 0), 0, 0));

		iZeile++;
		panel1.add(jScrollPane, new GridBagConstraints(0, iZeile, 5, 1, 1.0,
				1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));

	}

	public void jButtonClear_actionPerformed(ActionEvent e) {
		textPanelLog.removeAll();
		ausgabe.setText("");
	}

	public void jButtonDateieinlesen_actionPerformed(ActionEvent e) {
		JFileChooser fc = new JFileChooser();
		fc.setFileFilter(new FileFilter() {
			public boolean accept(File f) {
				return f.getName().toLowerCase().endsWith("F630".toLowerCase())
						|| f.isDirectory();
			}

			public String getDescription() {
				return "F630-Dateien";
			}
		});
		int returnVal = fc.showOpenDialog(LPMain.getInstance().getDesktop());
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = fc.getSelectedFile();

			try {
				FileInputStream fs = new FileInputStream(file);
				ObjectInputStream is = new ObjectInputStream(fs);
				ZeitdatenDto[] dtos = (ZeitdatenDto[]) is.readObject();
				is.close();
				fs.close();
				DelegateFactory.getInstance()
				.getZeiterfassungDelegate()
				.speichereZeidatenVonZEStift(dtos);

			} catch (FileNotFoundException e1) {
				DialogFactory.showModalDialog(LPMain
						.getTextRespectUISPr("lp.error"), e1.getMessage());
			} catch (ClassNotFoundException e2) {
				System.err.println(e.toString());
			} catch (IOException e3) {
				System.err.println(e.toString());
			} catch (Throwable e3) {
				logToTextPane(e3.toString());
				e3.printStackTrace();
			}

		}

	}

	public void jButtonStop_actionPerformed(ActionEvent e) {
		jButtonStop.setText("warten...");
		timer.stop();
	}

	public class commListener implements SerialPortEventListener {
		public void serialEvent(SerialPortEvent event) {
			if (event.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
				byte[] readBuffer = new byte[30];
				try {
					while (ins.available() > 0) {
						int numBytes = ins.read(readBuffer);
					}
					String nachricht = new String(readBuffer);
					ausgabe.setText(ausgabe.getText() + nachricht);
				} catch (IOException e) {
					System.out.println("Fehler: " + e);
				}
			}
		}
	}

}

class DialogZestifte_jButtonStop_actionAdapter implements ActionListener {
	private DialogZestifte adaptee;

	DialogZestifte_jButtonStop_actionAdapter(DialogZestifte adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		adaptee.jButtonStop_actionPerformed(e);
	}
}

class DialogZestifte_jButtonClear_actionAdapter implements ActionListener {
	private DialogZestifte adaptee;

	DialogZestifte_jButtonClear_actionAdapter(DialogZestifte adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		adaptee.jButtonClear_actionPerformed(e);
	}
}

class DialogZestifte_jButtonDateieinlesen_actionAdapter implements
		ActionListener {
	private DialogZestifte adaptee;

	DialogZestifte_jButtonDateieinlesen_actionAdapter(DialogZestifte adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		adaptee.jButtonDateieinlesen_actionPerformed(e);
	}
}
