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

import java.math.BigInteger;
import java.text.ParseException;

import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;

/**
 * Utility class to validate IBAN codes.
 * 
 * The IBAN consists of a ISO 3166-1 alpha-2 country code, followed by two check
 * digits (represented by kk in the examples below), and up to thirty
 * alphanumeric characters for the domestic bank account number, called the BBAN
 * (Basic Bank Account Number).
 * 
 * <h1>Exampe usage scenario</h1>
 * <code><pre>IBAN iban = new IBAN("ES2153893489");
 * if (iban.isValid())
 * 		System.out.println("ok");
 * else
 * 		System.out.println("problem with iban: "+iban.getInvalidCause());
 * </pre></code>
 * 
 * @author mgriffa
 * @since 3.3
 * @version $Id: WrapperIBANField.java,v 1.1 2012/08/09 08:02:26 christian Exp $
 */
public class WrapperIBANField extends WrapperFormattedTextField {
	private static final long serialVersionUID = 6707105829333880998L;
	private static final BigInteger BD_97 = new BigInteger("97");
	private static final BigInteger BD_98 = new BigInteger("98");
	private int invalidCause = -1;

	public static int FEHLER_PRUEFSUMME_FALSCH = 0;
	public static int FEHLER_IBAN_ZU_KURZ = 1;
	public static int FEHLER_KONTO_LEER = 2;

	private String iban;

	/**
	 * Get the IBAN
	 * 
	 * @return a string with the IBAN
	 */
	public String getIban() {
		return getFormattedText();
	}

	/**
	 * Set the IBAN
	 * 
	 * @param iban
	 *            the IBAN to set
	 */
	public void setIban(String iban) {
		this.iban = iban;
		setText(iban);
	}

	/**
	 * Create an IBAN object with the given iban code. This constructor does not
	 * perform any validation on the iban, only
	 * 
	 * @param iban
	 */
	public WrapperIBANField(String iban) throws ParseException {
		this.iban = iban;
		setFormat("UU** **** **** **** **** **** **** ****");
		setText(iban);

	}

	public WrapperIBANField() throws ParseException {
		setFormat("UU** **** **** **** **** **** **** ****");
	}

	public String getFormattedText() {
		// Originalmethode aufrufen und String gleich trimmen
		String s = super.getText();
		if (s != null) {
			s = s.replace("_", "");
			s = s.replace(" ", "");
			s = s.trim();

			if (s.length() == 0) {
				return null;
			} else {
				return s;
			}

		}
		return null;
	}

	public boolean isValid() {
		return true;
	}

	public boolean pruefeIBAN() {
		iban = getFormattedText();

		if (iban == null || iban.length() == 0) {
			return true;
		}

		
		if(isEditValid()==false){
			DialogFactory.showModalDialog(
					LPMain.getTextRespectUISPr("lp.error"),
					LPMain.getTextRespectUISPr("part.pruefeiban.ungueltigezeichen"));
			return false;
		}
		
		invalidCause = -1;
		final String code = removeNonAlpha(this.iban);
		final int len = code.length();
		if (len < 4) {
			DialogFactory.showModalDialog(
					LPMain.getTextRespectUISPr("lp.error"),
					LPMain.getTextRespectUISPr("part.pruefeiban.zukurz"));

			this.invalidCause = FEHLER_IBAN_ZU_KURZ;
			return false;
		}

		final StringBuffer bban = new StringBuffer(code.substring(4));
		if (bban.length() == 0) {
			DialogFactory.showModalDialog(
					LPMain.getTextRespectUISPr("lp.error"),
					LPMain.getTextRespectUISPr("part.pruefeiban.kontoleer"));
			this.invalidCause = FEHLER_KONTO_LEER;
			return false;
		}
		bban.append(code.substring(0, 4));

		String workString = translateChars(bban);
		int mod = modulo97(workString);
		if (mod != 1) {
			DialogFactory.showModalDialog(LPMain
					.getTextRespectUISPr("lp.error"), LPMain
					.getTextRespectUISPr("part.pruefeiban.pruefsummefalsch"));
			this.invalidCause = FEHLER_PRUEFSUMME_FALSCH;
			return false;
		}

		return true;
	}

	/**
	 * Translate letters to numbers, also ignoring non alphanumeric characters
	 * 
	 * @param bban
	 * @return the translated value
	 */
	public String translateChars(final StringBuffer bban) {
		final StringBuffer result = new StringBuffer();
		for (int i = 0; i < bban.length(); i++) {
			char c = bban.charAt(i);
			if (Character.isLetter(c)) {
				result.append(Character.getNumericValue(c));
			} else {
				result.append((char) c);
			}
		}
		return result.toString();
	}

	/**
	 * 
	 * @param iban
	 * @return the resulting IBAN
	 */
	public String removeNonAlpha(final String iban) {
		final StringBuffer result = new StringBuffer();
		for (int i = 0; i < iban.length(); i++) {
			char c = iban.charAt(i);
			if (Character.isLetter(c) || Character.isDigit(c)) {
				result.append((char) c);
			}
		}
		return result.toString();
	}

	private int modulo97(String bban) {
		BigInteger b = new BigInteger(bban);
		b = b.divideAndRemainder(BD_97)[1];
		b = BD_98.min(b);
		b = b.divideAndRemainder(BD_97)[1];
		return b.intValue();
		// return ((int)(98 - (Long.parseLong(bban) * 100) % 97L)) % 97;
	}

	/**
	 * Get a string with information about why the IBAN was found invalid
	 * 
	 * @return a human readable (english) string
	 */
	public int getInvalidCause() {
		return invalidCause;
	}

}
