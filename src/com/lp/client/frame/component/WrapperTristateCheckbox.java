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

import javax.swing.Icon;
import javax.swing.ImageIcon;

import com.lp.client.frame.component.cib.CibIconWrapper;

/**
 * Diese Klasse erweitert eine WrapperCheckbox um einen dritten Zustand
 * Es sollten die neuen Set- und Get-Methoden fuer die Selektierung der
 * Checkbox verwendet werden. Vor allem zum Setzen des dritten Zustands.
 * 
 * @author andi
 *
 */
public class WrapperTristateCheckbox extends WrapperCheckBox {

	private static final long serialVersionUID = -9179487629925294910L;
	
	public static final int SELECTED = 1;
	public static final int DESELECTED = -1;
	public static final int HALFSELECTED = 0;
	
	public static final int DISABLE = -2;
	
	private int state = DESELECTED;
	
	public WrapperTristateCheckbox(String text) {
		super(text);
	}

	public Icon getHalfselectedIcon() {
		return new CibIconWrapper(new ImageIcon(
		        getClass().getResource("/com/lp/client/res/toogleButtonIcons/windowsCheckBox_halfselectedIcon.png")));
	}

	@Override
	public Icon getIcon() {
		if(state == HALFSELECTED)
			return getHalfselectedIcon();
		return super.getIcon();
	}

	public void setSelection(int value) {
		state = value == SELECTED || value == HALFSELECTED ? value : DESELECTED;
		super.setSelected(state == SELECTED ? true : false);
		repaint();
	}
	
	public int getSelection () {
		return state;
	}

	@Override
	public boolean isSelected() {
		return super.isSelected();
	}

	@Override
	public void setSelected(boolean b) {
		super.setSelected(b);
		state = b ? SELECTED : DESELECTED;
	}

}
