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
import java.text.ParseException;

import javax.swing.JFormattedTextField;
import javax.swing.text.MaskFormatter;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;

public class WrapperFormattedTextField extends JFormattedTextField implements
		IControl, IDirektHilfe {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected boolean isMandatoryField = false;
	protected boolean isMandatoryFieldDB = false;
	protected boolean isActivatable = true;
	protected int columnsMax = 0;
	protected final static int I_COLUMNSMAX_DEFAULT = 40;
	private boolean dependenceField = false;
	private boolean uppercaseField = false;
	private char cPlaceHolder = '_';
//	private CornerInfoButton cib = null;

	public WrapperFormattedTextField() {
		this(I_COLUMNSMAX_DEFAULT);
	}

	/**
	 * WrapperFormattedTextField
	 * 
	 * @param columnsMax
	 *            int
	 */
	public WrapperFormattedTextField(int columnsMax) {
		HelperClient.setDefaultsToComponent(this);
		this.columnsMax = columnsMax;
		setDocument(new LimitedLengthDocument(columnsMax));
//		cib = new CornerInfoButton(this);
	}
	
	@Override
	public boolean hasContent() throws Throwable {
		return getText() != null && !getText().trim().isEmpty();
	}

	public WrapperFormattedTextField(String text, int columnsMax) {
		this(columnsMax);
		setText(text);
	}

	public boolean isActivatable() {
		return isActivatable;
	}

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

	public void setMandatoryFieldDB(boolean isMandatoryFieldDB) {
		this.isMandatoryFieldDB = isMandatoryFieldDB;
		if (isMandatoryFieldDB == true) {
			setMandatoryField(true);
		}
	}

	/**
	 * Das Textfeld leeren
	 */
	public void removeContent() {
		setValue(null);
		setText(null);
	}

	public String getText() {
		// Originalmethode aufrufen und String gleich trimmen
		// String s = super.getText();
		// if(s!=null){
		// return s.trim();
		// }
		return super.getText();
	}

	public String getFormattedText() {
		// Originalmethode aufrufen und String gleich trimmen
		String s = super.getText();
		if (s != null) {
			s = s.replace("_", "");
			return s.trim();
		}
		return null;
	}

	public void setActivatable(boolean isActivatable) {
//		this.isActivatable = isActivatable;
//		this.setFocusable(isActivatable);
		this.isActivatable = isActivatable;		
		if(!isActivatable) {
			setEditable(false) ;
		}
	}

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

	public int getColumnsMax() {
		return columnsMax;
	}

	public void setColumnsMax(int columnsMax) {
		this.columnsMax = columnsMax;
		setDocument(new LimitedLengthDocument(columnsMax));
	}

	public boolean isDependenceField() {
		return dependenceField;
	}

	public boolean isUppercaseField() {
		return uppercaseField;
	}

	public void setDependenceField(boolean dependenceField) throws ExceptionLP {
		this.dependenceField = dependenceField;
		if (dependenceField) {
			this.setBackground(HelperClient.getDependenceFieldBackgroundColor());
		} else {
			this.setBackground(new WrapperNumberField().getBackground());
		}
	}

	public void setUppercaseField(boolean uppercaseField) {
		this.uppercaseField = uppercaseField;
		if (this.getDocument() != null
				&& this.getDocument() instanceof LimitedLengthDocument) {
			((LimitedLengthDocument) this.getDocument()).setUppercase(true);
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
	 * @deprecated use setActivatable
	 * @param bEnabled
	 *            boolean
	 */
	public void setEnabled(boolean bEnabled) {
		/** @todo JO->MB PJ 5349 */
		super.setEnabled(bEnabled);
	}

	public void requestFocus() {
		super.requestFocusInWindow();
	}

	public void transferFocus() {
		super.transferFocus();
	}

	public void setCplaceHolder(char cPlaceHolder) {
		this.cPlaceHolder = cPlaceHolder;
	}

	public char getCplaceHolder() {
		return this.cPlaceHolder;
	}

	public void setFormat(String sPattern) throws ParseException {
		MaskFormatter mFormatter = new MaskFormatter(sPattern);
		mFormatter.setPlaceholderCharacter(cPlaceHolder);
		mFormatter.setOverwriteMode(true);
		mFormatter.install(this);
		/*
		 * MaskFormatter mFHelper = new MaskFormatter(sPattern);
		 * mFHelper.setPlaceholderCharacter(' '); DefaultFormatterFactory
		 * formatterfactory = new
		 * DefaultFormatterFactory(mFormatter,mFHelper,mFormatter);
		 * this.setFormatterFactory(formatterfactory);
		 */
	}

	@Override
	public void setToken(String token) {
//		cib.setToolTipToken(token);
	}

	@Override
	public Point getLocationOffset() {
		return InfoButtonRelocator.getInstance().getRelocation(this);
	}

	@Override
	public void removeCib() {
//		cib = null;
	}
	
	@Override
	public String getToken() {
		return null;
	}

}
