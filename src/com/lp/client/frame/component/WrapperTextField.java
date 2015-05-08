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
import java.awt.LayoutManager;
import java.awt.Point;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JTextField;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;

/**
 * <p>
 * Textfeld mit Laengenbeschraenkung <br/>
 * </p>
 *
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2008
 * </p>
 *
 * @author Martin Bluehweis
 * @version $Revision: 1.9 $
 */
public class WrapperTextField extends JTextField implements IControl, IDirektHilfe, IHvValueHolder {
	private static final long serialVersionUID = 1L;
	protected boolean isMandatoryField = false;
	protected boolean isMandatoryFieldDB = false;
	protected boolean isActivatable = true;
	protected int columnsMax = 0;
	protected final static int I_COLUMNSMAX_DEFAULT = 40;
	private boolean dependenceField = false;
	private boolean uppercaseField = false;
	private boolean saveReportInformation = true;
	private CornerInfoButton cib = null;
	boolean infoButton = false;

	private WrapperSelectField zugehoerigesSelectField = null;
	private HvValueHolder<String> valueHolder ;

	public WrapperTextField() {
		this(I_COLUMNSMAX_DEFAULT);
	}

	public WrapperTextField(int columnsMax) {
		valueHolder = new HvValueHolder<String>(this, null) ;
		HelperClient.setDefaultsToComponent(this);
		cib = new CornerInfoButton(this);
		new FocusHighlighter(this);
		setColumnsMax(columnsMax);
	}

	public WrapperTextField(boolean infoButton) {
		valueHolder = new HvValueHolder<String>(this, null) ;
		HelperClient.setDefaultsToComponent(this);

		if (!infoButton) {
			cib = null;
		}
		else {
			cib = new CornerInfoButton(this);
		}

		new FocusHighlighter(this);
		setColumnsMax(columnsMax);
	}

	public WrapperTextField(String toolTipToken) {
		this(I_COLUMNSMAX_DEFAULT);
		setToken(toolTipToken);
	}

	public WrapperTextField(int columnsMax, String toolTipToken) {
		this(columnsMax);
		setToken(toolTipToken);
	}

	public WrapperTextField(String text, int columnsMax, String toolTipToken) {
		this(columnsMax, toolTipToken);
		setText(text);
	}

	public WrapperTextField(String text, int columnsMax) {
		this(columnsMax);
		setText(text);
	}


	public WrapperSelectField getZugehoerigesSelectField() {
		return zugehoerigesSelectField;
	}

	public void setZugehoerigesSelectField(
			WrapperSelectField zugehoerigesSelectField) {
		this.zugehoerigesSelectField = zugehoerigesSelectField;
	}

	@Override
	public boolean hasContent() throws Throwable {
		return getText() != null && !getText().trim().isEmpty();
	}

	/**
	 * isActivateable
	 *
	 * @return boolean
	 */

	public boolean isSaveReportInformation() {
		return saveReportInformation;
	}

	public void setSaveReportInformation(boolean saveReportInformation) {
		this.saveReportInformation = saveReportInformation;
	}

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
		this.isActivatable = isActivatable;
		if(!isActivatable) {
			setEditable(false) ;
		}
	}

	/**
	 * setText ueberschreiben
	 *
	 * @param text
	 *            String
	 */
	public void setText(String text) {
		if (text != null) {
			if (isUppercaseField()) {
				text = text.toUpperCase();
			}
			if (this.getDocument() != null
					&& this.getDocument() instanceof LimitedLengthDocument) {
				LimitedLengthDocument document = (LimitedLengthDocument) getDocument();
				int limit = document.getLimit();
				if (text.length() > limit) {
					super.setText(text.substring(0, limit - 3) + "...");
					// MB: dd.mm.05 dieses printStackTrace ist absichtlich!
					new IllegalArgumentException("text.length() > limit")
							.printStackTrace();
				} else {
					super.setText(text);
				}
			} else {
				super.setText(text);
			}

			valueHolder.setValue(text.length() > 0 ? text : null) ;
		} else {
			super.setText("");
			valueHolder.setValue(null) ;
		}
	}

	/**
	 * Das Textfeld leeren
	 */
	public void removeContent() {
		this.setText("");
	}

	public String getText() {
		/*
		 * Achtung: Wenn an dieser Logik, dass null zur&uuml;ckgegeben werden soll,
		 * wenn es ein Leerstring ist etwas ge&auml;ndert wird, dann muss auch im
		 * setText am valueHolder angepasst werden! (ghp)
		 */
		String s = super.getText();
		if (s == null) return null ;

		return s.length() > 0 ? s : null ;
	}

	public Boolean isTextForFileNameAllowed() {

		String s = super.getText();

		Pattern p = Pattern.compile("^(?!.*\\.{2})[0-9a-zA-Z_]*(\\.[a-zA-z]*$)", Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(s);
		boolean b = m.find();

		return (b || s.equals("")) ? true : false;
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
			// setFocusable(isActivatable());
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

	@Override
	public void setLayout(LayoutManager arg0) {
		super.setLayout(arg0);
	}

	@Override
	public void setToken(String token) {
		cib.setToolTipToken(token);
	}

	@Override
	public void removeCib() {
		cib = null;
	}

	@Override
	public Point getLocationOffset() {
		return InfoButtonRelocator.getInstance().getRelocation(this);
	}
	@Override
	public String getToken() {
		return cib.getToolTipToken();
	}


	@Override
	public Object getValueHolderValue() {
		return getText() ;
	}

	public void addValueChangedListener(IHvValueHolderListener l) {
		valueHolder.addListener(l);
	}

	public void removeValueChangedListener(IHvValueHolderListener l) {
		valueHolder.removeListener(l);
	}
}
