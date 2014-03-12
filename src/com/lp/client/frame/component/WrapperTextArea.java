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

import java.awt.Point;

import javax.swing.JTextArea;
import javax.swing.text.Document;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.HelperClient;

/**
 * <p>
 * TextArea <br/>
 * </p>
 * 
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2008
 * </p>
 * 
 * @author Martin Bluehweis
 * @version $Revision: 1.6 $
 */
public class WrapperTextArea extends JTextArea implements IControl,
		IDirektHilfe {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private boolean isMandatoryField = false;
	private boolean isMandatoryFieldDB = false;
	private boolean isActivatable = true;
	private CornerInfoButton cib = null;

	public WrapperTextArea() {
		cib = new CornerInfoButton(this);
	}

	public WrapperTextArea(int p0, int p1) {
		super(p0, p1);
		cib = new CornerInfoButton(this);
	}

	public WrapperTextArea(String p0) {
		super(p0);
		cib = new CornerInfoButton(this);
	}

	public WrapperTextArea(String p0, int p1, int p2) {
		super(p0, p1, p2);
		this.setName(null);
		cib = new CornerInfoButton(this);
	}

	public WrapperTextArea(Document p0) {
		super(p0);
		cib = new CornerInfoButton(this);
	}

	public WrapperTextArea(Document p0, String p1, int p2, int p3) {
		super(p0, p1, p2, p3);
		cib = new CornerInfoButton(this);
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
	 * isMandatoryFieldDB
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
				setBorder(new WrapperTextArea().getBorder());
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
		this.isActivatable = isActivatable;
		this.setFocusable(isActivatable);
	}

	/**
	 * Das Textfeld leeren
	 */
	public void removeContent() {
		this.setText("");
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
	 * setText ueberschreiben
	 * 
	 * @param text
	 *            String
	 */
	public void setText(String text) {
		if (text != null) {
			super.setText(text);
		} else {
			super.setText("");
		}
	}

	public void setEditable(boolean bEditable) {
		super.setEditable(bEditable);
		if (bEditable == false) {
			this.setBackground(HelperClient.getNotEditableColor());
			setFocusable(isActivatable());
		} else {
			this.setBackground(HelperClient.getEditableColor());
			setFocusable(true);
		}
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

	@Override
	public boolean hasContent() throws Throwable {
		return getText() != null && !getText().trim().isEmpty();
	}
}
