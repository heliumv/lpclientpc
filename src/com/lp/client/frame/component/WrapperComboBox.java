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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;

/**
 * <p>
 * Gewrappte Combobox<br/>
 * </p>
 * 
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2008
 * </p>
 * 
 * @author Martin Bluehweis
 * @version $Revision: 1.9 $
 */
public class WrapperComboBox extends JComboBox implements IControl,
		IDirektHilfe {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final int MAXIMUM_ROW_COUNT = 15;

	private boolean isMapSet = false;
	private boolean isMandatoryField = false;
	private boolean isMandatoryFieldDB = false;
	private boolean isActivatable = true;
	public Object emptyEntry = null;
	private boolean dependenceField = false;
	private CornerInfoButton cornerInfoButton = null;

	public WrapperComboBox() {
		this.setDefaults();
	}

	public void setEmptyEntry(Object emptyEntry) {
		this.emptyEntry = emptyEntry;
	}

	public WrapperComboBox(Object[] p0) {
		super(p0);
		this.setDefaults();
	}

	public WrapperComboBox(Vector<?> p0) {
		super(p0);
		this.setDefaults();
	}

	public WrapperComboBox(ComboBoxModel p0) {
		super(p0);
		this.setDefaults();
	}

	/**
	 * isActivateable.
	 * 
	 * @return boolean
	 */

	public boolean isActivatable() {
		return isActivatable;
	}

	@Override
	protected void installAncestorListener() {
		super.installAncestorListener();

	}

	/**
	 * isMandatoryField.
	 * 
	 * @return boolean
	 */
	public boolean isMandatoryFieldDB() {
		return isMandatoryFieldDB;
	}

	/**
	 * isMandatoryFieldDB.
	 * 
	 * @return boolean
	 */
	public boolean isMandatoryField() {
		return isMandatoryField || isMandatoryFieldDB;
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
				setBorder(new WrapperComboBox().getBorder());
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
	 * setActivatable.
	 * 
	 * @param isActivatable
	 *            boolean
	 */
	public void setActivatable(boolean isActivatable) {
		this.isActivatable = isActivatable;
		this.setFocusable(isActivatable);
	}

	/**
	 * Remove Content.
	 */
	public void removeContent() {
		// MB 10.12.04: Inhalt belassen
		// this.clearMe();
	}

	/**
	 * Setzt eine Map. Die Map-Values kommen in die Combobox; und es werden die
	 * Map-Keys im Array sortedMapKeys sortiert abgelegt
	 * 
	 * @param map
	 *            Map mit den Elementen
	 */
	public void setMap(Map<?, ?> map) {
		setMap(map, false);
	}

	public void setMap(Map<?, ?> map, boolean sort) {
		this.clearMe(); // sicherheitshalber
		if (!isMandatoryField()) {
			this.addEmptyEntry();
		}
		if (map != null) {
			List<WrapperComboBoxItem> items = new ArrayList<WrapperComboBoxItem>();

			Iterator<?> it = map.keySet().iterator();

			while (it.hasNext()) {
				Object key = it.next();
				items.add(new WrapperComboBoxItem(key, map.get(key)));
			}

			if (sort) {
				Collections
						.sort(items, new WrapperComboBoxItemComparator(true));
			}

			this.addAll(items.toArray());
			isMapSet = true;
		}
	}

	/**
	 * Gibt Schluessel-Objekt zur&uuml;ck
	 * 
	 * @return Object
	 */
	public Object getKeyOfSelectedItem() {
		int selectedIndex = getSelectedIndex();

		// @uw java.lang.ArrayIndexOutOfBoundsException: -1 wenn es keinen
		// selectedKey gibt
		if (selectedIndex == -1) {
			return null;
		}
		if (super.getSelectedItem().equals(emptyEntry)) {
			return null;
		}
		// wenn der <LEER> - Eintrag drin ist, stehen alle um 1 weiter unten
		if (isMandatoryField() == false) {
			selectedIndex--;
		}
		if (super.getSelectedItem() instanceof WrapperComboBoxItem) {
			return ((WrapperComboBoxItem) super.getSelectedItem()).getKey();
		} else {
			return null;
		}
	}

	@Override
	public void setSelectedItem(Object anObject) {

		if (anObject == null) {
			this.setSelectedItem(emptyEntry);
		}
		
		if(isMapSet()) {
			for(int i = 0; i < getItemCount(); i++) {
				Object item = getItemAt(i);
				if(item instanceof WrapperComboBoxItem) {
					WrapperComboBoxItem wcbi = (WrapperComboBoxItem)item;
					if(wcbi.getValue() != null && wcbi.getValue().equals(anObject)) {
						anObject = item;
						break;
					}
				}
			}
		}
		super.setSelectedItem(anObject);
	}
	/**
	 * setzt das Schluesselobjekt.
	 * 
	 * @param key
	 *            Object
	 * @throws Exception
	 */
	public void setKeyOfSelectedItem(Object key) {
		if (key == null) {
			this.setSelectedItem(emptyEntry);
		}
		int offset = 0;
		// wenn der <LEER> dabei ist, stehen alle um 1 weiter unten
		if (isMandatoryField() == false) {
			offset = 1;
		}

		for (int i = offset; i < getItemCount(); i++) {
			if (getItemAt(i) instanceof WrapperComboBoxItem) {
				if (((WrapperComboBoxItem) getItemAt(i)).getKey().equals(key)) {
					setSelectedIndex(i);
					break;
				}
			}
		}
	}

	@Override
	public Object getSelectedItem() {
		Object item = super.getSelectedItem();
		return item instanceof WrapperComboBoxItem ? ((WrapperComboBoxItem) item)
				.getValue() : item;
	}

	/**
	 * Ueberpruefen, ob bereits Werte eingetragen wurden
	 * 
	 * @return boolean
	 */
	public boolean isMapSet() {
		return isMapSet;
	}

	private void clearMe() {
		this.removeAllItems();
		// this.keyValueDto = null;
		isMapSet = false;
	}

	private void addEmptyEntry() {
		this.addItem(emptyEntry);
	}

	private void setDefaults() {
		cornerInfoButton = new CornerInfoButton(this);
		HelperClient.setDefaultsToComponent(this);
		this.emptyEntry = Defaults.getInstance().getSComboboxEmptyEntry();
		setMaximumRowCount(MAXIMUM_ROW_COUNT);

	}

	public boolean isDependenceField() {
		return dependenceField;
	}

	public void setDependenceField(boolean calculationField) throws ExceptionLP {
		this.dependenceField = calculationField;
		if (calculationField) {
			this.setBackground(HelperClient.getDependenceFieldBackgroundColor());
		} else {
			this.setBackground(new WrapperNumberField().getBackground());
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

	public void addAll(Object[] items) {
		for (Object item : items) {
			addItem(item);
		}
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
	public boolean hasContent() throws Throwable {
		return getKeyOfSelectedItem() != null;
	}
}
