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
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.FocusListener;
import java.util.GregorianCalendar;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.util.EJBExceptionLP;

/**
 * <p>
 * <I>Diese Klasse ist ein Feld, das Zeiteingaben ermoeglicht</I>
 * </p>
 * 
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2008
 * </p>
 * 
 * <p>
 * Erstellungsdatum <I>08.03.05</I>
 * </p>
 * 
 * <p>
 * </p>
 * 
 * @author Martin Bluehweis
 * @version $Revision: 1.4 $
 */
public class WrapperTimeField extends javax.swing.JPanel implements IControl, IDirektHilfe {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private WrapperSpinner wspMinuten = null;
	private WrapperSpinner wspSekunden = null;
	private WrapperSpinner wspStunden = null;

	private boolean isActivatable = true;
	private boolean isMandatoryField = false;
	private boolean showSeconds = false;

	private int millisekunden = 0;
	
	private CornerInfoButton cib;

	public WrapperTimeField() {
		super();
		jbInit();
	}

	/**
	 * Ruft den Eigenschaftswert showSeconds (boolean) ab.
	 * 
	 * @return Der Eigenschaftswert showSeconds.
	 */
	public boolean getShowSeconds() {
		return showSeconds;
	}

	/**
	 * Minuten auslesen
	 * 
	 * @return int
	 */
	public Integer getMinuten() {
		return wspMinuten.getInteger();
	}

	/**
	 * Sekunden auslesen
	 * 
	 * @return int
	 */
	public Integer getSekunden() {
		if (getShowSeconds() == true) {
			return wspSekunden.getInteger();
		} else {
			return new Integer(0);
		}
	}

	/**
	 * Stunden auslesen
	 * 
	 * @return int
	 */
	public Integer getStunden() {
		return wspStunden.getInteger();
	}

	/**
	 * Die Klasse initialisieren.
	 * 
	 * @throws Throwable
	 */
	private void jbInit() {
		setLayout(new java.awt.GridBagLayout());
		setSize(110, Defaults.getInstance().getControlHeight());

		sekundenModel = new RolloverSpinnerNumberModel(0, 0, 59, 1);
		minutenModel = new RolloverSpinnerNumberModel(0, 0, 59, 1);
		stundenModel = new RolloverSpinnerNumberModel(0, 0, 23, 1);

		sekundenModel.setLinkedSpinnerModel(minutenModel);
		minutenModel.setLinkedSpinnerModel(stundenModel);

		wspMinuten = new WrapperSpinner(minutenModel);
		wspSekunden = new WrapperSpinner(sekundenModel);
		wspStunden = new WrapperSpinner(stundenModel);
		wspStunden.setName("asdf");

		wspSekunden.setVisible(false);

		this.add(wspStunden, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						0, 0, 0, 0), 0, 0));
		this.add(wspMinuten, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						0, 0, 0, 0), 0, 0));
		this.add(wspSekunden, new GridBagConstraints(2, 0, 1, 1, 1.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						0, 0, 0, 0), 0, 0));
		cib = new CornerInfoButton(this);
	}

	/**
	 * Definiert den Eigenschaftswert showSeconds (boolean).
	 * 
	 * @param showSeconds
	 *            Der neue Wert f&uuml;r die Eigenschaft.
	 */
	public void setShowSeconds(boolean showSeconds) {
		this.showSeconds = showSeconds;
		wspSekunden.setVisible(showSeconds);
	}

	private java.sql.Date fieldLpDateZuTimeField = new java.sql.Date(0);
	private RolloverSpinnerNumberModel sekundenModel;
	
	private RolloverSpinnerNumberModel minutenModel;
	private RolloverSpinnerNumberModel stundenModel;

	/**
	 * Ruft den Eigenschaftswert lpDateZuTimeField (java.sql.Date) ab.
	 * 
	 * @return Der Eigenschaftswert lpDateZuTimeField.
	 */
	public java.sql.Date getLpDateZuTimeField() {
		return fieldLpDateZuTimeField;
	}

	/**
	 * Lese Zeitfeld mit Uhrzeit aus
	 * 
	 * @return time
	 * @throws ExceptionLP
	 */
	public java.sql.Time getTime() throws ExceptionLP {
		// Flag fuer geaenderte Daten zuruecksetzen, ohne Ereignis auszuloesen
		// Zeit ermitteln
		GregorianCalendar hgc = new GregorianCalendar();
		if (getLpDateZuTimeField() != null) {
			// Timestamp aus eingestellter Zeit bilden
			hgc.setTime(getLpDateZuTimeField());
		}
		// Eingabe pruefen
		validateTime();
		// eingestellte Zeit einarbeiten
		hgc.set(java.util.Calendar.HOUR, getStunden().intValue());
		hgc.set(java.util.Calendar.MINUTE, getMinuten().intValue());
		if (getShowSeconds()) {
			hgc.set(java.util.Calendar.SECOND, getSekunden().intValue());
		} else {
			// hgc.set(java.util.Calendar.SECOND, 0);
		}
		hgc.set(java.util.Calendar.MILLISECOND, millisekunden);
		// erstellten Timestamp zurueckgeben
		java.sql.Time time = new java.sql.Time(hgc.getTimeInMillis());
		return time;
	}

	/**
	 * Setze Zeitfeld mit Uhrzeit aus Time.
	 * 
	 * @param time
	 *            Time
	 */
	public void setTime(java.sql.Time time) {
		if (time != null) {
			java.util.Date zeit = (java.util.Date) time;
			java.text.SimpleDateFormat formatierer = new java.text.SimpleDateFormat(
					"s", Defaults.getInstance().getLocUI());
			int s, m, h, ms;
			try {
				s = Integer.parseInt(formatierer.format(zeit));
			} catch (NumberFormatException n) {
				s = 0;
			}
			formatierer.applyPattern("m");
			try {
				m = Integer.parseInt(formatierer.format(zeit));
			} catch (NumberFormatException n) {
				m = 0;
			}
			formatierer.applyPattern("H");
			try {
				h = Integer.parseInt(formatierer.format(zeit));
			} catch (NumberFormatException n) {
				h = 0;
			}
			formatierer.applyPattern("S");
			try {
				ms = Integer.parseInt(formatierer.format(zeit));
			} catch (NumberFormatException n) {
				ms = 0;
			}
			wspMinuten.setInteger(new Integer(m));
			wspSekunden.setInteger(new Integer(s));
			wspStunden.setInteger(new Integer(h));
			millisekunden = ms;
		} else {
			wspMinuten.setInteger(null);
			wspSekunden.setInteger(null);
			wspStunden.setInteger(null);
		}
	}

	public boolean isMandatoryField() {
		return isMandatoryField;
	}

	public void setMandatoryField(boolean isMandatoryField) {
		this.isMandatoryField = isMandatoryField;
	}

	public boolean isActivatable() {
		return isActivatable;
	}

	public void setActivatable(boolean isActivatable) {
		this.isActivatable = isActivatable;
	}

	public void removeContent() {
		wspSekunden.setInteger(new Integer(0));
		wspMinuten.setInteger(new Integer(0));
		wspStunden.setInteger(new Integer(0));
		millisekunden = 0;
	}
	
	@Override
	public boolean hasContent() throws Throwable {
		// TODO Auto-generated method stub
		return true;
	}

	public void setEnabled(boolean enabled) {
		wspMinuten.setEnabled(enabled);
		wspSekunden.setEnabled(enabled);
		wspStunden.setEnabled(enabled);
		Color c = null;
		if (enabled) {
			c = HelperClient.getEditableColor();
		} else {
			c = HelperClient.getNotEditableColor();
		}
		wspMinuten.setBackground(c);
		wspSekunden.setBackground(c);
		wspStunden.setBackground(c);
	}

	public void requestFocus() {
		wspStunden.getEditor().requestFocusInWindow();
		wspStunden.getEditor().transferFocus();
	}

	private void validateTime() throws ExceptionLP {
		boolean bValid = true;
		// Stunde zw. 0 und 23
		int iStunden = getStunden().intValue();
		if (iStunden > 23 || iStunden < 0) {
			bValid = false;
		}
		// Minute zw. 0 und 59
		int iMinuten = getMinuten().intValue();
		if (iMinuten > 59 || iMinuten < 0) {
			bValid = false;
		}
		// Sekunden (nur wenn angezeigt) zw. 0 und 59
		if (getShowSeconds()) {
			int iSekunden = getSekunden().intValue();
			if (iSekunden > 59 || iSekunden < 0) {
				bValid = false;
			}
		}
		// wenn was nicht stimmt -> Fehler
		if (!bValid) {
			throw new ExceptionLP(EJBExceptionLP.FEHLER_UNGUELTIGE_ZEITEINGABE,
					new Exception("FEHLER_UNGUELTIGE_ZEITEINGABE"));
		}
	}
	
	public void installFocusListener(FocusListener focusListener) {
		getWspStunden().installFocusListener(focusListener);
		getWspMinuten().installFocusListener(focusListener);
		getWspSekunden().installFocusListener(focusListener);

	}
	
	public WrapperSpinner getWspStunden() {
		return wspStunden;
	}
	
	public WrapperSpinner getWspMinuten() {
		return wspMinuten;
	}
	public WrapperSpinner getWspSekunden() {
		return wspSekunden;
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
