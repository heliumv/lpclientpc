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
package com.lp.client.frame.component;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.regex.Pattern;

import javax.swing.ImageIcon;
import javax.swing.JButton;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.component.jtapi.Outcall;
import com.lp.client.frame.component.phone.HttpPhoneDialer;
import com.lp.client.frame.component.phone.HttpPhoneDialerAuth;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.util.logger.LpLogger;
import com.lp.server.finanz.service.FinanzFac;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.partner.service.PartnerFac;
import com.lp.server.system.service.ArbeitsplatzparameterDto;
import com.lp.server.system.service.MandantFac;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.util.Helper;

/**
 * <p>
 * Diese Klasse beinhaltet ein TextField mit Button.
 * Eingegebene Telefonnummern werden automatisch formatiert.
 * Ein Buttonklick setzt einen TAPI-Anruf ab.
 * </p>
 * 
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004 - 2007
 * </p>
 * 
 * <p>
 * Erstellung: Martin Bluehweis; 28.07.07
 * </p>
 * 
 * <p>
 * 
 * @author $Author: robert $
 *         </p>
 * 
 * @version not attributable Date $Date: 2012/09/24 14:59:59 $
 */

public class WrapperTelefonField extends WrapperTextFieldWithIconButton {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4229623131816216716L;
	
	private final static int DEFAULT_MAXIMUMDIGITS = 25;
	private int maximumDigits = 0;
	private String cTelefon = null;
	private PartnerDto partnerDto = null;
	private boolean bIstAbsprechpartner = false;

	private final String hashs = "####################################################################################################";

	public WrapperTelefonField() throws Throwable {
		this(DEFAULT_MAXIMUMDIGITS);
	}

	public WrapperTelefonField(int maximumDigits) throws Throwable {
		super(new ImageIcon(WrapperTelefonField.class.getResource("/com/lp/client/res/mobilephone2.png")),
				"lp.nummerwaehlen");
		this.setMaximumDigits(maximumDigits);
	}
	
	@Override
	protected boolean installComponent(JButton jButton) {
		return LPMain.getInstance().getDesktop().darfAnwenderAufZusatzfunktionZugreifen(
				MandantFac.ZUSATZFUNKTION_TAPISERVICE);
	}

	public void setIstAnsprechpartner(boolean bIstAbsprechpartner) {
		this.bIstAbsprechpartner = bIstAbsprechpartner;
	}

	private String buildMask() {
		String result = hashs.substring(0, maximumDigits);
		return result;
	}

	protected void setMask(String mask) {
		wtfText.setColumns(mask.length() + 1);
		String regExp = null;
		if (Pattern.matches("#{1,}", mask)) { // |(\\d{0,0}) hinzugefuegt,
			// damits auch leer sein kann
			regExp = "\\d{0," + mask.length() + "}";
		} else if (Pattern.matches("#{1,}.#{1,}", mask)) { // |(\\d{0,0})
			// hinzugefuegt,
			// damits auch leer
			// sein kann
			regExp = "\\d{0," + mask.length() + "}";
		} else {
			LpLogger.getInstance(this.getClass()).warn(
					"Mask=\"" + mask + "\" is not correct");
			return;
		}
		Pattern.compile(regExp);
	}

	public int getMaximumDigits() {
		return maximumDigits;
	}

	public void setMaximumDigits(int maximumDigits) {
		this.maximumDigits = maximumDigits;
		wtfText.setDocument(new LimitedLengthNumberDocument(maximumDigits));
		this.setMask(buildMask());
	}

	public void setTextDurchwahl(String durchwahl) {
		wtfText.setText(durchwahl);
	}

	public void setPartnerKommunikationDto(PartnerDto partnerDto,String cTelefon) throws Throwable {
		wtfText.setForeground(new WrapperTextField().getForeground());
		this.cTelefon = cTelefon;
		this.partnerDto = partnerDto;
		if (cTelefon != null) {
			String text = cTelefon;

			if (text != null) {
				wtfText.setText(text);
			}

			// Wenn die Vorwahl nicht mit der Vorwahl des Landes uebereinstimmt,
			// dann rot anzeigen.
			// Dazu muss zuerst die Laenderart bestimmt werden.
			if (partnerDto.getLandplzortDto() != null) {
				String sLaenderart = DelegateFactory.getInstance()
						.getFinanzServiceDelegate()
						.getLaenderartZuPartner(partnerDto.getIId());
				// Wenns ein Auslaender ist.
				if (sLaenderart != null
						&& !sLaenderart.equals(FinanzFac.LAENDERART_INLAND)) {
					String sVorwahl = partnerDto.getLandplzortDto()
							.getLandDto().getCTelvorwahl();
					String sVorwahl00 = null; // Vorwahl mit der Default
					// Auslandsvorwahl
					if (sVorwahl != null) {
						ParametermandantDto parameter = (ParametermandantDto) DelegateFactory
								.getInstance()
								.getParameterDelegate()
								.getParametermandant(
										ParameterFac.PARAMETER_AUSLANDSVORWAHL,
										ParameterFac.KATEGORIE_ALLGEMEIN,
										LPMain.getTheClient()
												.getMandant());
						String sAuslandsvorwahl = parameter.getCWert();
						// "+" durch "00" ersetzen
						if (sVorwahl.startsWith("+")) {
							sVorwahl00 = sAuslandsvorwahl
									+ sVorwahl.substring(1);
						} else {
							sVorwahl00 = sVorwahl;
						}
						if (wtfText.getText() != null
								&& !wtfText.getText().startsWith(sVorwahl)
								&& !wtfText.getText().startsWith(sVorwahl00)) {
							wtfText.setForeground(Color.red);
						}
					}
				}
			}

		} else {
			wtfText.setText("");
		}
	}
	
	/* FocusListener noch nicht implementiert
	protected void focusGained() {
		if (getText() != null) {
			wtfText.setSelectionStart(0);
			wtfText.setSelectionEnd(getText().length());
		}
	}*/

	@Override
	protected void actionEventImpl(ActionEvent e) {
		try {
			if (partnerDto != null) {
				String nummerFertig = null;
				if (cTelefon == null && bIstAbsprechpartner == true) {
					if (partnerDto.getCTelefon() != null) {
						cTelefon = partnerDto.getCTelefon();
					} else {
						return;
					}
				}
				nummerFertig = DelegateFactory
						.getInstance()
						.getPartnerDelegate()
						.passeInlandsAuslandsVorwahlAn(partnerDto.getIId(),cTelefon);
				if (bIstAbsprechpartner) {
					nummerFertig = DelegateFactory
							.getInstance()
							.getPartnerDelegate()
							.enrichNumber(partnerDto.getIId(),
									PartnerFac.KOMMUNIKATIONSART_TELEFON,
									cTelefon, false);
					nummerFertig = DelegateFactory
							.getInstance()
							.getPartnerDelegate()
							.passeInlandsAuslandsVorwahlAn(
									partnerDto.getIId(), nummerFertig);
				}
	
				ArbeitsplatzparameterDto parameter = DelegateFactory
						.getInstance()
						.getParameterDelegate()
						.holeArbeitsplatzparameter(
								ParameterFac.ARBEITSPLATZPARAMETER_TELEFONWAHL_HTTP_REQUEST);
				
				if (parameter != null) {
					if (nummerFertig != null
							&& parameter.getCWert() != null) {
						
						String nummer = Helper
								.befreieNummerVonSonderzeichenInklisiveLeerzeichen(nummerFertig);
	
						String aufruf = parameter.getCWert();
	
						aufruf = aufruf.replaceAll("###NUMMER###", nummer);
	
						try {
							HttpPhoneDialer dialer = new HttpPhoneDialerAuth();
							dialer.dial(aufruf.trim());
	
							// java.awt.Desktop.getDesktop().browse(
							// new URI(aufruf.trim()));
							//
							// } catch (URISyntaxException ex1) {
							// DialogFactory
							// .showModalDialog(
							// LPMain.getTextRespectUISPr("lp.error"),
							// LPMain.getTextRespectUISPr("lp.fehlerhafteurl")
							// +": "+aufruf.trim());
							// }
						} catch (ExceptionLP ex) {
							DialogFactory
									.showModalDialog(
											LPMain.getTextRespectUISPr("lp.error"),
											LPMain.getMessageTextRespectUISPr(
													"lp.fehlerbeimwahlvorgang",
													new Object[] { ex
															.getMessage() }));
						}
					}
				} else {
					parameter = DelegateFactory
							.getInstance()
							.getParameterDelegate()
							.holeArbeitsplatzparameter(
									ParameterFac.ARBEITSPLATZPARAMETER_PFAD_MIT_PARAMETER_TAPITOOL);
	
					if (parameter != null) {
						if (nummerFertig != null) {
	
							String amtsholung = "";
	
							ParametermandantDto parameterAmt = (ParametermandantDto) DelegateFactory
									.getInstance()
									.getParameterDelegate()
									.getParametermandant(
											ParameterFac.PARAMETER_AMTSLEITUNGSVORWAHL_TELEFON,
											ParameterFac.KATEGORIE_ALLGEMEIN,
											LPMain.getTheClient().getMandant());
	
							if (parameter.getCWert() != null
									&& !parameter.getCWert().equals(" ")) {
								amtsholung = parameterAmt.getCWert();
							}
	
							String command = parameter.getCWert()
									+ amtsholung
									+ Helper.befreieNummerVonSonderzeichen(nummerFertig);
							Process p = Runtime.getRuntime().exec(command);
							BufferedReader input = new BufferedReader(
									new InputStreamReader(
											p.getInputStream()));
							String output = "";
							String line;
							while ((line = input.readLine()) != null) {
								output = line + "\n";
							}
							if (output.length() > 0) {
								DialogFactory.showModalDialog("Fehler",
										output);
							}
							input.close();
						}
	
					} else {
	
						Outcall outcall = Outcall.getInstance();
	
						if (outcall != null && nummerFertig != null
								&& nummerFertig.length() > 0) {
							outcall.call(Helper
									.befreieNummerVonSonderzeichen(nummerFertig));
						}
					}
				}
			}
		} catch (Throwable ex) {
			ex.printStackTrace();
		}
		
	}

}
