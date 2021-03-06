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

import java.awt.Point;
import java.awt.Rectangle;

import javax.swing.AbstractButton;
import javax.swing.JComponent;

import com.lp.client.frame.component.calendar.JTextFieldDateEditor;
import com.lp.client.pc.SystemProperties;

public class InfoButtonRelocator {
	private final Point defaultOffset = new Point(1, 1);

	protected InfoButtonRelocator() {
	}

	private static InfoButtonRelocator instance;

	public static InfoButtonRelocator getInstance() {
		if (null == instance) {
			instance = SystemProperties.isMacOs() ? new MacInfoButtonRelocator()
					: new WindowsInfoButtonRelocator();
		}
		return instance;
	}

	public Point getRelocation(JComponent parent) {
		return defaultOffset;
	}

	public Point getRelocation(WrapperKeyValueField parent) {
		return defaultOffset;
	}

	public Point getRelocation(WrapperButton parent) {
		return defaultOffset;
	}

	public Point getRelocation(WrapperComboBox parent) {
		return defaultOffset;
	}

	public Point getRelocation(WrapperNumberField parent) {
		return defaultOffset;
	}

	public Point getRelocation(WrapperTextNumberField parent) {
		return defaultOffset;
	}

	public Point getRelocation(JTextFieldDateEditor parent) {
		return defaultOffset;
	}

	public Point getRelocation(WrapperNumberLetterField parent) {
		return defaultOffset;
	}
	public Point getRelocation(WrapperDateField parent) {
		return defaultOffset;
	}
	public Point getRelocation(WrapperRadioButton parent) {
		return getRelocationIconButton(parent, parent.getIconRect());
	}
	public Point getRelocation(WrapperCheckBox parent) {
		return getRelocationIconButton(parent, parent.getIconRect());
	}
	
	protected Point getRelocationIconButton(AbstractButton parent, Rectangle iconRect) {
		
		if(iconRect == null) return defaultOffset;
		Point p = new Point(-iconRect.x, iconRect.y);
		p.translate(-iconRect.width, 0);
		p.translate(parent.getWidth(), 0);
		return p;
	}
}
