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

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JRadioButton;

import com.lp.client.frame.Defaults;
import com.lp.util.Helper;

/**
 * <p>
 * Gewrappter RadioButton<br/>
 * </p>
 * 
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2008
 * </p>
 * 
 * @author Martin Bluehweis
 * @version $Revision: 1.8 $
 */
public class WrapperRadioButton extends JRadioButton implements IControl,
		IDirektHilfe {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private boolean isActivatable = true;
	private CornerInfoButton cib = new PaintingCornerInfoButton(this) ;

	public WrapperRadioButton() {
		setDefaults();
	}

	public WrapperRadioButton(String p0) {
		super(p0);
		setDefaults();
	}

	public WrapperRadioButton(String p0, boolean p1) {
		super(p0, p1);
		setDefaults();
	}

	public WrapperRadioButton(Action p0) {
		super(p0);
		setDefaults();
	}

	public WrapperRadioButton(Icon p0) {
		super(p0);
		setDefaults();
	}

	public WrapperRadioButton(Icon p0, boolean p1) {
		super(p0, p1);
		setDefaults();
	}

	public WrapperRadioButton(String p0, Icon p1) {
		super(p0, p1);
		setDefaults();
	}

	public WrapperRadioButton(String p0, Icon p1, boolean p2) {
		super(p0, p1, p2);
		setDefaults();
	}

	public WrapperRadioButton(boolean bMnemonic, String p0) {
		super(p0);
		setDefaults();
		if (bMnemonic)
			setText(getMnemonicFromString(p0));
	}

	public void setShort(Short s) {
		this.setSelected(Helper.short2boolean(s));
	}

	public Short getShort() {
		return Helper.boolean2Short(isSelected());
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
		return false;
	}

	/**
	 * setMandatoryField
	 * 
	 * @param isMandatoryField
	 *            boolean
	 */
	public void setMandatoryField(boolean isMandatoryField) {
		// nix tun
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

	public void removeContent() {
		this.setSelected(false);
	}

	private void setDefaults() {
		//HelperClient.setDefaultsToComponent(this);
		//setMargin(new Insets(2, 0, 2, 2));
		cib = new PaintingCornerInfoButton(this);
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

	public void setMnemonic(char mnemonic) {
		super.setMnemonic(mnemonic);
	}

	public void setMnemonic(int mnemonic) {
		super.setMnemonic(mnemonic);
	}

	public String getMnemonicFromString(String text) {
		String p0 = text;
		Pattern pattern = Pattern.compile("&\\w");
		Matcher matcher = pattern.matcher(text);
		String match = null;
		while (matcher.find()) {
			// System.out.println("Found a match: " + matcher.group());
			match = new String(text.substring(matcher.start() + 1,
					matcher.end()));
			// System.out.println("Start position: " + matcher.start());
			// System.out.println("End position: " + matcher.end());
		}
		if (match != null) {
			if (match.equalsIgnoreCase("b"))
				setMnemonic(KeyEvent.VK_B);
			if (match.equalsIgnoreCase("r"))
				setMnemonic(KeyEvent.VK_R);
			if (match.equalsIgnoreCase("f"))
				setMnemonic(KeyEvent.VK_F);
			String[] t = text.split("&");
			p0 = t[0] + t[1];
		}
		return p0;
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
		return true;
	}
}
