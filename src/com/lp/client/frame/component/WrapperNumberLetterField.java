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

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.FocusEvent;
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
import com.lp.client.pc.LPMain;
import com.lp.client.util.logger.LpLogger;

/**
 * 
 * <p>
 * <I>Textfeld zur Eingabe von ganzen Zahlen als Text</I>
 * </p>
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2008
 * </p>
 * <p>
 * Erstellungsdatum <I>02.06.2005</I>
 * </p>
 * <p>
 * </p>
 * 
 * @author Martin Bluehweis
 * @version $Revision: 1.8 $
 */
public class WrapperNumberLetterField extends JTextField implements IControl,
		IDirektHilfe {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected boolean isMandatoryField = false;
	protected boolean isMandatoryFieldDB = false;
	protected boolean isActivatable = true;
	private boolean dependenceField = false;
	private final static int DEFAULT_MAXIMUMDIGITS = 10;
	private int maximumDigits = DEFAULT_MAXIMUMDIGITS;

	private Pattern regPattern;

	private CornerInfoButton cib = null;

	private final String hashs = "############################################################";

	public WrapperNumberLetterField() {
		HelperClient.setDefaultsToComponent(this);
		this.setMask(buildMask());
		this.setHorizontalAlignment(SwingConstants.RIGHT);
		this.addFocusListener(new WrapperNumberLetterField_focusAdapter(this));
		cib = new CornerInfoButton(this);
	}

	private String buildMask() {
		String result = hashs.substring(0, maximumDigits);
		return result;
	}

	protected void setMask(String mask) {
		this.setColumns(mask.length() + 1);
		String regExp = null;
		if (Pattern.matches("#{1,}", mask)) { // |(\\d{0,0}) hinzugefuegt,
												// damits auch leer sein kann
			regExp = "\\w{0," + mask.length() + "}";
		} else if (Pattern.matches("#{1,}.#{1,}", mask)) { // |(\\d{0,0})
															// hinzugefuegt,
															// damits auch leer
															// sein kann
			regExp = "\\w{0," + mask.length() + "}";
		} else {
			LpLogger.getInstance(this.getClass()).warn(
					"Mask=\"" + mask + "\" is not correct");
			this.regPattern = null;
			return;
		}
		this.regPattern = Pattern.compile(regExp);
		// HelperClient.setDefaultsToComponent(this);
	}

	protected Document createDefaultModel() {
		NumberDocument numberDocument = new NumberDocument();
		return numberDocument;
	}

	public int getMaximumDigits() {
		return maximumDigits;
	}

	public void setMaximumDigits(int maximumDigits) {
		this.maximumDigits = maximumDigits;
		this.setMask(buildMask());
	}

	/**
	 * @param text
	 *            String
	 */
	public void setText(String text) {
		// Ignorieren des vom Designer generierten Codes
		if (text != null) {
			if (!text.startsWith("wrapperNumberLetterField")) {
				super.setText(text);
			}
		} else {
			super.setText("");
		}
	}

	public String getText() {
		// Originalmethode aufrufen und String gleich trimmen
		String s = super.getText();
		if (s != null) {
			// s = s.trim();
			if (s.length() > 0) {
				// gib String zurueck und verlasse Methode
				return s;
			}
			if (s.equals("")) {
				s = null;
			}
		}
		return s;
	}

	/**
	 * Leeren des Feldes.
	 */
	public void removeContent() {
		super.setText("");
	}

	protected void focusGained() {
		if (getText() != null) {
			this.setSelectionStart(0);
			this.setSelectionEnd(getText().length());
		}
	}

	protected void focusLost() throws ExceptionLP {
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
		if (isMandatoryFieldDB == false || isMandatoryField == true) {
			this.isMandatoryField = isMandatoryField;
			if (this.isMandatoryField == true) {
				setBorder(Defaults.getInstance().getMandatoryFieldBorder());
			} else {
				setBorder(new WrapperTextField().getBorder());
			}
		}
	}

	public void setMandatoryFieldDB(boolean isMandatoryFieldDB) {
		this.isMandatoryFieldDB = isMandatoryFieldDB;
		if (isMandatoryFieldDB == true) {
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
//		this.isActivatable = isActivatable;
//		this.setFocusable(isActivatable);
		this.isActivatable = isActivatable;		
		if(!isActivatable) {
			setEditable(false) ;
		}
	}

	public boolean isDependenceField() {
		return dependenceField;
	}

	public void setDependenceField(boolean dependenceField) {
		this.dependenceField = dependenceField;
		if (dependenceField) {
			this.setBackground(HelperClient.getDependenceFieldBackgroundColor());
		} else {
			this.setBackground(new WrapperNumberLetterField().getBackground());
		}
	}

	public void setEditable(boolean bEditable) {
		super.setEditable(bEditable);
		if (bEditable == false) {
			this.setBackground(HelperClient.getNotEditableColor());
		} else {
			this.setBackground(HelperClient.getEditableColor());
		}
	}
	
	@Override
	public boolean hasContent() throws Throwable {
		return getText() != null && !getText().trim().isEmpty();
	}

	protected class NumberDocument extends PlainDocument {

		/**
	 * 
	 */
		private static final long serialVersionUID = 1L;

		public void remove(int offs, int len) throws BadLocationException {
			if (regPattern != null) {
				String text = WrapperNumberLetterField.this.getText();
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
			if (str != null && str.length() > maximumDigits) {
				str = str.substring(0, maximumDigits);
				setBackground(HelperClient.getInvalidDataColor());
				Toolkit.getDefaultToolkit().beep();
			}
			StringBuffer strInsert = new StringBuffer(str != null ? str : "");
			if (strInsert.length() > 0) {
				if (regPattern != null) {
					StringBuffer stringBuffer = new StringBuffer("");
					if (WrapperNumberLetterField.this.getText() != null) {
						stringBuffer = new StringBuffer(
								WrapperNumberLetterField.this.getText());
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

	/**
	 * @deprecated don't use this method
	 * @param bEnabled
	 *            boolean
	 */
	public void setEnabled(boolean bEnabled) {
		super.setEnabled(bEnabled);
	}

	@Override
	public void setToken(String token) {
		cib.setToolTipToken(token);
	}

	@Override
	public Point getLocationOffset() {
		return InfoButtonRelocator.getInstance().getRelocation(this);
	}

	@Override
	public void removeCib() {
		cib = null;
	}

	@Override
	public String getToken() {
		return cib.getToolTipToken();
	}
}

class WrapperNumberLetterField_focusAdapter implements
		java.awt.event.FocusListener {
	private WrapperNumberLetterField adaptee;

	WrapperNumberLetterField_focusAdapter(WrapperNumberLetterField adaptee) {
		this.adaptee = adaptee;
	}

	public void focusLost(FocusEvent e) {
		try {
			adaptee.focusLost();
		} catch (Throwable ex) {
			LPMain.getInstance().exitFrame(null);
		}
	}

	public void focusGained(FocusEvent e) {
		adaptee.focusGained();
	}
}
