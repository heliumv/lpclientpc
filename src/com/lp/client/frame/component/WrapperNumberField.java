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
package com.lp.client.frame.component;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.util.logger.LpLogger;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

/**
 * 
 * <p>
 * <I>Textfeld zur Eingabe von Decimal - Zahlen</I>
 * </p>
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2008
 * </p>
 * <p>
 * Erstellungsdatum <I>24.11.2004</I>
 * </p>
 * <p>
 * </p>
 * 
 * @author Martin Bluehweis
 * @version $Revision: 1.15 $
 */
public class WrapperNumberField extends JTextField implements IControl,
		INumberRenderer, IDirektHilfe, IHvValueHolder {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	DecimalFormatSymbols decimalFormatSymbols = null;
	protected boolean isMandatoryField = false;
	protected boolean isMandatoryFieldDB = false;
	protected boolean isActivatable = true;
	private boolean dependenceField = false;
	private final static int DEFAULT_MAXIMUMINTEGERDIGITS = 10;
	private final static int DEFAULT_FRACTIONDIGITS = 2;
	// private final static Locale DEFAULT_LOCALE = Locale.GERMANY;
	private int maximumIntegerDigits = DEFAULT_MAXIMUMINTEGERDIGITS;
	private int fractionDigits = DEFAULT_FRACTIONDIGITS;
	// private Locale locale = DEFAULT_LOCALE;

	private Pattern regPattern;
	// Default 15,4 wie in der datenbank
	private BigDecimal maximumValue = new BigDecimal(9999999999.9999);
	private BigDecimal minimumValue = new BigDecimal(-9999999999.9999);

	private CornerInfoButton cornerInfoButton = null;

	private final String hashs = "####################";

	private BigDecimal compareValue = null;

	private boolean isNegativeWerteRoteinfaerben = false;

	public WrapperNumberField() throws ExceptionLP {
		HelperClient.setDefaultsToComponent(this);
		try {
			decimalFormatSymbols = new DecimalFormatSymbols(Defaults
					.getInstance().getLocUI());
		} catch (Throwable ex) {
			throw new ExceptionLP(EJBExceptionLP.FEHLER, ex);
		}
		this.setMask(buildMask());
		this.setHorizontalAlignment(SwingConstants.RIGHT);
		this.addFocusListener(new WrapperNumberField_focusAdapter(this));
		this.addKeyListener(new WrapperNumberField_keyAdapter(this));

		cornerInfoButton = new CornerInfoButton(this);
		new FocusHighlighter(this);
	}

	public WrapperNumberField(BigDecimal minimumValue, BigDecimal maximumValue)
			throws ExceptionLP {
		this();
		this.maximumValue = maximumValue;
		this.minimumValue = minimumValue;
	}

	public WrapperNumberField(int iMinimumValue, int iMaximumValue)
			throws ExceptionLP {
		this(new BigDecimal(iMinimumValue), new BigDecimal(iMaximumValue));
	}
	@Override
	public boolean hasContent() throws Throwable {
		return getBigDecimal()!=null;
	}

	@Override
	public boolean isNegativeWerteRoteinfaerben() {
		return isNegativeWerteRoteinfaerben;
	}

	@Override
	public void setNegativeWerteRoteinfaerben(
			boolean isNegativeWerteRoteinfaerben) {
		this.isNegativeWerteRoteinfaerben = isNegativeWerteRoteinfaerben;
	}

	private String buildMask() {
		StringBuffer result = new StringBuffer();
		// Die Stellen vor dem ersten Tausenderpunkt
		result.append("-");
		result.append(hashs.substring(0, maximumIntegerDigits % 3));
		// dann immer .### so oft wie benoetigt
		for (int i = 0; i < maximumIntegerDigits / 3; i++) {
			// wenn stellenanzahl durch 3 teilbar, dann kein fuehrender punkt
			if (!(maximumIntegerDigits % 3 == 0 && i == 0)) {
				result.append(decimalFormatSymbols.getGroupingSeparator());
			}
			result.append(hashs.substring(0, 3));
		}
		// nachkommastellen
		if (fractionDigits > 0) {
			result.append(decimalFormatSymbols.getDecimalSeparator());
			result.append(hashs.substring(0, fractionDigits));
		}
		return result.toString();
	}

	protected void setMask(String mask) {
		// Tausenderpunkte zaehlen
		StringTokenizer st = new StringTokenizer(mask, ""
				+ decimalFormatSymbols.getGroupingSeparator());
		int iTausenderpunkte = st.countTokens() - 1;

		this.setColumns(mask.length());
		String regExp = null;
		final String basePatternInteger = "[-]" + // optional negatives
													// Vorzeichen
				"#{1,3}" + // 1 bis 3 Stellen vor dem ersten tausenderpunkt
				"(" + decimalFormatSymbols.getGroupingSeparator() + "###){0,}"; // beliebig
																				// viele
																				// tausenderbloecke
		final String basePatternDecimal = basePatternInteger + // wie Integer
				decimalFormatSymbols.getDecimalSeparator() + // + Dezimaltrenner
				"#{1,}"; // + beliebig viele nachkommastellen

		final String regExVorzeichen = "((["
				+ decimalFormatSymbols.getMinusSign() + "]){0,1})"; // Ein oder
																	// kein
																	// Minus
		final String regExSonderfall = regExVorzeichen + "|" + "(\\d{0})"; // leer
		final String regExInteger = regExVorzeichen + "([\\d"
				+ decimalFormatSymbols.getGroupingSeparator() + "]{0,"
				+ (maximumIntegerDigits + iTausenderpunkte) + "})";
		if (Pattern.matches(basePatternInteger, mask)
				&& mask.indexOf(decimalFormatSymbols.getDecimalSeparator()) == -1) {
			regExp = regExSonderfall + "|" + regExInteger;
		} else if (Pattern.matches(basePatternDecimal, mask)) {
			regExp = regExSonderfall + "|" + regExInteger + "|"
					+ // kann zum zeitpunkt der eingabe auch ein int sein
					regExInteger + decimalFormatSymbols.getDecimalSeparator()
					+ "(\\d{0," + fractionDigits + "})";
		}

		// if (Pattern.matches("#{1,}", mask)) { // |(\\d{0,0}) hinzugefuegt,
		// damits auch leer sein kann
		// regExp = "([-+]){0,1}\\d{1," + mask.length() + "}"+
		// "|(\\d{0,0})"+
		// // Zahlen in loacaleabhaengigem format
		// "|([-+]){0,1}(\\d{1,3}[.])*?\\d{1,3}(([.,]\\d{0," + (mask.length() -
		// mask.indexOf('.') - 1) +"}){0,1})";
		// }
		// else if (Pattern.matches("#{1,}.#{1,}", mask)) { // |(\\d{0,0})
		// hinzugefuegt, damits auch leer sein kann
		// regExp = "([-+]){0,1}(\\d{1," + mask.indexOf('.') + "}" +
		// "([.,]\\d{0," + (mask.length() - mask.indexOf('.') - 1) +"}){0,1})"+
		// "|(\\d{0,0})"+
		// // Zahlen in loacaleabhaengigem format
		// "|([-+]){0,1}(\\d{1,3}[.])*?\\d{1,3}(([.,]\\d{0," + (mask.length() -
		// mask.indexOf('.') - 1) +"}){0,1})";
		// }
		else {
			LpLogger.getInstance(this.getClass()).warn(
					"Mask=\"" + mask + "\" is not correct");
			this.regPattern = null;
			return;
		}
		this.regPattern = Pattern.compile(regExp);
	}

	protected Document createDefaultModel() {
		NumberDocument numberDocument = new NumberDocument();
		return numberDocument;
	}

	public int getFractionDigits() {
		return fractionDigits;
	}

	public void setFractionDigits(int fractionDigits) {
		this.fractionDigits = fractionDigits;
		this.setMask(buildMask());
	}

	public int getMaximumIntegerDigits() {
		return maximumIntegerDigits;
	}

	public void setMaximumIntegerDigits(int maximumIntegerDigits) {
		this.maximumIntegerDigits = maximumIntegerDigits;
		this.setMaximumValue(new BigDecimal("99999999999999999999".substring(0,
				maximumIntegerDigits)));
		this.setMask(buildMask());
	}

	/**
	 * @param text
	 *            String
	 */
	public void setText(String text) {
		// Ignorieren des vom Designer generierten Codes
		if (!text.startsWith("wrapperNumberField")) {
			setSuperText(text);
		}
	}

	private void setForegroundColor(BigDecimal number) {
		if (isNegativeWerteRoteinfaerben() && (-1 == number.signum())) {
			setForeground(Color.red);
		} else {
			setForeground(Color.black);
		}
	}

	private void setSuperText(String value) {
		super.setText(value);
		setForeground(Color.black);
	}

	public void setBigDecimal(BigDecimal bdI) throws ExceptionLP {
		if (bdI != null) {
			BigDecimal bd = bdI.setScale(fractionDigits,
					BigDecimal.ROUND_HALF_EVEN);
			if (bdI.compareTo(bd) != 0) {
				LpLogger.getInstance(this.getClass()).info(
						"Wert " + bdI + " wird auf " + fractionDigits
								+ " Nachkommastellen gerundet");
			}
			try {
				Locale loc = Defaults.getInstance().getLocUI();
				String s = Helper.formatZahl(bd, fractionDigits, loc);
				super.setText(s);
			} catch (Throwable ex) {
			}

			setForegroundColor(bd);
		} else {
			setSuperText("");
		}
		checkMinimumMaximum();
		checkCompareValue();
	}

	public BigDecimal getBigDecimal() throws ExceptionLP {
		BigDecimal bdValue;
		if (this.getText() != null && !this.getText().equals("")
				&& !this.getText().equals("-")) {
			String number = null;
			try {
				number = NumberFormat
						.getNumberInstance(Defaults.getInstance().getLocUI())
						.parse(getText()).toString();
			} catch (Throwable ex) {
				throw new ExceptionLP(EJBExceptionLP.FEHLER, ex);
			}
			bdValue = new BigDecimal(number).setScale(fractionDigits,
					BigDecimal.ROUND_HALF_EVEN);
			// vorher den wert pruefen
			// checkMinimumMaximum();
		} else {
			bdValue = null;
		}
		return bdValue;
	}

	public void setDouble(Double d) throws ExceptionLP {
		if (d != null) {
			this.setBigDecimal(new BigDecimal(d.doubleValue()));
		} else {
			setSuperText("");
		}
	}

	public void setInteger(Integer i) {
		setSuperText(i == null ? "" : i.toString());
	}

	public void setInteger(int i) {
		setInteger(new Integer(i));
	}

	public Double getDouble() throws ExceptionLP {
		if (this.getText() != null && !this.getText().equals("")) {
			BigDecimal number = this.getBigDecimal();
			if (number != null) {
				return new Double(number.doubleValue());
			}
		}
		// sonst
		return null;
	}

	public Integer getInteger() throws ExceptionLP {
		if (this.getText() != null && !this.getText().equals("")) {
			BigDecimal number = this.getBigDecimal();
			if (number != null) {
				return new Integer(number.intValue());
			}
		}
		// sonst
		return null;
	}

	public void setFloat(Float f) throws ExceptionLP {
		if (f != null) {
			this.setBigDecimal(new BigDecimal(f));
		} else {
			setSuperText("");
		}
	}

	public Float getFloat() throws ExceptionLP {
		if (this.getText() != null && !this.getText().equals("")) {
			BigDecimal number = this.getBigDecimal();
			if (number != null) {
				return new Float(number.doubleValue());
			}
		}
		// sonst
		return null;
	}

	/**
	 * Leeren des Feldes.
	 */
	public void removeContent() {
		setSuperText("");
	}

	public BigDecimal getMaximumValue() {
		return maximumValue;
	}

	public BigDecimal getMinimumValue() {
		return minimumValue;
	}

	public void setMaximumValue(BigDecimal maximumValue) {
		if (maximumValue != null) {
			if (maximumValue.compareTo(this.getMinimumValue()) >= 0) {
				this.maximumValue = Helper.rundeKaufmaennisch(maximumValue,
						this.fractionDigits);
			}
		} else {
			setMaximumValue(new BigDecimal(9999999999.9999));
		}
	}

	public void setMaximumValue(int iMaximumValue) {
		this.setMaximumValue(new BigDecimal(iMaximumValue));
	}

	public void setMinimumValue(BigDecimal minimumValue) {
		if (minimumValue != null) {
			if (minimumValue.compareTo(this.getMaximumValue()) <= 0) {
				this.minimumValue = Helper.rundeKaufmaennisch(minimumValue,
						this.fractionDigits);
			}
		} else {
			setMinimumValue(new BigDecimal(-9999999999.9999));
		}
	}

	public void setMinimumValue(int iMinimumValue) {
		this.setMinimumValue(new BigDecimal(iMinimumValue));
	}

	public BigDecimal getCompareValue() {
		return compareValue;
	}

	public void setCompareValue(BigDecimal compareValueI) throws ExceptionLP {
		compareValue = compareValueI;
		checkCompareValue();
	}

	protected final void focusLost() throws ExceptionLP {
		checkMinimumMaximum();
		String sText = getText();
		// wenn ein Minus allein drinsteht, lass ich das
		if (sText == null || !sText.equals("-")) {
			this.setBigDecimal(this.getBigDecimal());
		}
	}

	protected final void focusGained() throws ExceptionLP {
		if (this.getBigDecimal() != null) {
			// Formatierung waehrend der Eingabe ohne Tausenderpunkte.
			StringBuffer sPattern = new StringBuffer();
			sPattern.append(hashs.substring(0, maximumIntegerDigits - 1))
					.append("0");
			// nachkommastellen
			if (fractionDigits > 0) {
				sPattern.append(".");
				sPattern.append("0000000000".substring(0, fractionDigits));
			}
			DecimalFormat f = new DecimalFormat(sPattern.toString(),
					decimalFormatSymbols);
			this.setText(f.format(this.getBigDecimal().doubleValue()));
		}

		if (getText() != null) {
			this.setSelectionStart(0);
			this.setSelectionEnd(getText().length());
		}
	}

	/**
	 * checkMinimumMaximum
	 * 
	 * @throws ExceptionLP
	 */
	public void checkMinimumMaximum() throws ExceptionLP {
		if (this.getBigDecimal() != null) {

			int iBigger = this.getBigDecimal()
					.compareTo(this.getMaximumValue());
			if (iBigger > 0) {
				this.setBigDecimal(this.getMaximumValue());
//				throw new ExceptionLP(
//						EJBExceptionLP.FEHLER_UNGUELTIGE_ZAHLENEINGABE,
//						new Exception());
				DialogFactory
				.showModalDialog(
						LPMain.getTextRespectUISPr("lp.error"),
						LPMain.getMessageTextRespectUISPr("lp.error.ungueltigezahleneingabe.max", this.getMaximumValue()));
			}

			int iSmaller = this.getBigDecimal().compareTo(
					this.getMinimumValue());
			if (iSmaller < 0) {
				this.setBigDecimal(this.getMinimumValue());
				DialogFactory
				.showModalDialog(
						LPMain.getTextRespectUISPr("lp.error"),
						LPMain.getMessageTextRespectUISPr("lp.error.ungueltigezahleneingabe.min", this.getMinimumValue()));
			}
		}
	}

	/**
	 * checkCompare Vordergrundfarbe abh&auml;ngig von Compare-Wert zu Wert setzen
	 * wenn diese definiert ist
	 * 
	 * @throws ExceptionLP
	 */
	public void checkCompareValue() throws ExceptionLP {
		if (compareValue != null && this.getBigDecimal() != null) {
			if (this.getBigDecimal().compareTo(compareValue) == -1) {
				setForeground(Color.red);
			} else {
				setForeground(Color.black);
			}
		}
	}

	/**
	 * Umkehrung des Vorzeichens.
	 * 
	 * @throws ExceptionLP
	 */
	public void negate() throws ExceptionLP {
		if (this.getBigDecimal() != null) {
			this.setBigDecimal(this.getBigDecimal().negate());
		}
	}

	protected void makeNegative() throws ExceptionLP {
		if (this.getBigDecimal() != null) {
			if (this.getBigDecimal().compareTo(new BigDecimal(0)) > 0) {
				// curserposition merken
				int b = getSelectionStart();
				int e = getSelectionEnd();
				this.negate();
				this.setSelectionStart(b + 1);
				this.setSelectionEnd(e + 1);
			}
		}
	}

	protected void makePositive() throws ExceptionLP {
		if (this.getBigDecimal() != null) {
			if (this.getBigDecimal().compareTo(new BigDecimal(0)) < 0) {
				// curserposition merken
				int b = getSelectionStart();
				int e = getSelectionEnd();
				this.negate();
				this.setSelectionStart(b - 1);
				this.setSelectionEnd(e - 1);
			}
		}
	}

	boolean wholeTextIsSelected() {
		if (getSelectionStart() == 0 && getSelectionEnd() == getText().length()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * isActivateable
	 * 
	 * @return boolean
	 */
	public boolean isActivatable() {
		return isActivatable;
	}

	/**
	 * isMandatoryField
	 * 
	 * @return boolean
	 */
	public boolean isMandatoryField() {
		return isMandatoryField || isMandatoryFieldDB;
	}

	/**
	 * isMandatoryField
	 * 
	 * @return boolean
	 */
	public boolean isMandatoryFieldDB() {
		return isMandatoryFieldDB;
	}

	/**
	 * setMandatoryField
	 * 
	 * @param isMandatoryField
	 *            boolean
	 */
	public void setMandatoryField(boolean isMandatoryField) {
		if (!isMandatoryFieldDB || isMandatoryField) {
			this.isMandatoryField = isMandatoryField;
			if (this.isMandatoryField) {
				setBorder(Defaults.getInstance().getMandatoryFieldBorder());
			} else {
				setBorder(new WrapperTextField().getBorder());
			}
		}
	}

	public void setMandatoryFieldDB(boolean isMandatoryFieldDB) {
		this.isMandatoryFieldDB = isMandatoryFieldDB;
		if (isMandatoryFieldDB) {
			setMandatoryField(true);
		}
	}

	/**
	 * setActivatable
	 * 
	 * @param isActivatable
	 *            boolean
	 */
	public void setActivatable(boolean isActivatable) {
		this.isActivatable = isActivatable;
		if(!isActivatable) {
			setEditable(false);
		}
	}

	public boolean isDependenceField() {
		return dependenceField;
	}

	public void setDependenceField(boolean dependenceField) throws ExceptionLP {
		this.dependenceField = dependenceField;
		if (dependenceField) {
			this.setBackground(HelperClient.getDependenceFieldBackgroundColor());
		} else {
			this.setBackground(new WrapperNumberField().getBackground());
		}
	}

	public void setEditable(boolean bEditable) {
		super.setEditable(bEditable);
		// UW->MB Die Farbe des DependenceField nicht ueberschreiben
		if (!isDependenceField()) {
			if (!bEditable) {
				this.setBackground(HelperClient.getNotEditableColor());
			} else {
				this.setBackground(HelperClient.getEditableColor());
			}
		}
	}

	/**
	 * @deprecated use setActivatable
	 * @param bEnabled
	 *            boolean
	 */
	public void setEnabled(boolean bEnabled) {
		super.setEnabled(bEnabled);
	}

	protected class NumberDocument extends PlainDocument {

		/**
	 * 
	 */
		private static final long serialVersionUID = 1L;

		public void remove(int offs, int len) throws BadLocationException {
			if (regPattern != null) {
				String text = WrapperNumberField.this.getText();
				StringBuffer stringBuffer = new StringBuffer(
						(text == null) ? "" : text);

				stringBuffer.delete(offs, offs + len);
				if (!regPattern.matcher(stringBuffer).matches()) {
					Toolkit.getDefaultToolkit().beep();
					return;
				}
			}
			super.remove(offs, len);
		}

		public void insertString(int offs, String str, AttributeSet a)
				throws BadLocationException {
			// plus ignorieren
			// siehe keyAdapter
			if (str == null || str.equals("+")) {
				return;
			}
			// Ein Minus allein darf sein
			if (str.equals("-")) {
				if (WrapperNumberField.this.getText() != null
						&& !WrapperNumberField.this.getText().equals("")) {
					return;
				}
			}
			if (str.equals(".") || str.equals(",")) {
				if (WrapperNumberField.this.getText() != null) {
					String s = WrapperNumberField.this.getText();
					if (s.indexOf(",") >= 0 || s.indexOf(".") >= 0) {
						return;
					}
				} else {
					return;
				}
			}
			StringBuffer strInsert = new StringBuffer(str);
			if (strInsert.length() > 0) {
				if (regPattern != null) {
					StringBuffer stringBuffer = new StringBuffer("");
					if (WrapperNumberField.this.getText() != null) {
						stringBuffer = new StringBuffer(
								WrapperNumberField.this.getText());
					}
					stringBuffer.insert(offs, strInsert.toString());
					if (!regPattern.matcher(stringBuffer).matches()) {
						Toolkit.getDefaultToolkit().beep();
						return;
					}
				}
				super.insertString(offs, strInsert.toString(), a);
			}
		}
	}

	public void setMinimumSize(Dimension d) {
		super.setMinimumSize(new Dimension(d.width, Defaults.getInstance()
				.getControlHeight()));
	}

	public void setMaximumSize(Dimension d) {
		super.setMaximumSize(new Dimension(d.width, Defaults.getInstance()
				.getControlHeight()));
	}

	public void setPreferredSize(Dimension d) {
		super.setPreferredSize(new Dimension(d.width, Defaults.getInstance()
				.getControlHeight()));
	}

	@Override
	public void setToken(String token) {
		cornerInfoButton.setToolTipToken(token);
	}

	@Override
	public Point getLocationOffset() {
		return InfoButtonRelocator.getInstance().getRelocation(this);
	}

	@Override
	public void removeCib() {
		cornerInfoButton = null;
	}
	@Override
	public String getToken() {
		return cornerInfoButton.getToolTipToken();
	}

	@Override
	public Object getValueHolderValue() {
		try {
			return getBigDecimal();
		} catch (ExceptionLP e) {
			e.printStackTrace();
			return null;
		}
	}
}

class WrapperNumberField_focusAdapter implements java.awt.event.FocusListener {
	private WrapperNumberField adaptee;

	WrapperNumberField_focusAdapter(WrapperNumberField adaptee) {
		this.adaptee = adaptee;
	}

	public void focusLost(FocusEvent e) {
		try {
			adaptee.focusLost();
		} catch (ExceptionLP ex) {
			if (ex.getICode() == EJBExceptionLP.FEHLER_UNGUELTIGE_ZAHLENEINGABE) {
				DialogFactory
						.showModalDialog(
								LPMain.getTextRespectUISPr("lp.error"),
								LPMain.getTextRespectUISPr("lp.error.ungueltigezahleneingabe"));
			} else {
				LPMain.getInstance().exitFrame(null);
			}
		} catch (Throwable t) {
			LPMain.getInstance().exitFrame(null);
		}
	}

	public void focusGained(FocusEvent e) {
		try {
			adaptee.focusGained();
		} catch (Throwable t) {
			LPMain.getInstance().exitFrame(null);
		}
	}
}

class WrapperNumberField_keyAdapter implements java.awt.event.KeyListener {
	private WrapperNumberField adaptee;

	WrapperNumberField_keyAdapter(WrapperNumberField adaptee) {
		this.adaptee = adaptee;
	}

	public void keyTyped(KeyEvent e) {
		try {
			if (adaptee.isActivatable) {
				if (e.getKeyChar() == '+') {
					adaptee.makePositive();
				} else if (e.getKeyChar() == KeyEvent.VK_MINUS) {
					adaptee.makeNegative();
				} else if (e.getKeyChar() == ',' || e.getKeyChar() == '.') {
					if (adaptee.wholeTextIsSelected()) {
						adaptee.setText("0"
								+ adaptee.decimalFormatSymbols
										.getDecimalSeparator());
					}
				}
			}
		} catch (ExceptionLP ex) {
			/**
			 * @todo PJ 5340
			 */
		}
	}

	public void keyPressed(KeyEvent e) {
	}

	public void keyReleased(KeyEvent e) {
	}
}
