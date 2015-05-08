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
package com.lp.client.frame;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;

import com.lp.client.util.dtable.DistributedTableModel;
import com.lp.util.Helper;

/**
 * <p>
 * Diese Klasse kuemmert sich um das Rendern der FLR-Tabelle
 * </p>
 * 
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004 - 2007
 * </p>
 * 
 * <p>
 * Erstellung: Victor Finder; 12.07.07
 * </p>
 * 
 * <p>
 * 
 * @author $Author: adi $
 *         </p>
 * 
 * @version not attributable Date $Date: 2012/02/03 17:40:32 $
 */
public class LPDefaultTableCellRenderer extends DefaultTableCellRenderer {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Color defaultCellForegroundColor = UIManager.getColor("Table.foreground");
	
	protected Color getSpecialForecolor(DistributedTableModel model, int row) {
		Color c = null;
		try {
			Class<?> letzteKlasse = model.getDataSource()
				.getColumnClasses()[model.getDataSource().getColumnClasses().length - 1];
			if (letzteKlasse.equals(java.awt.Color.class)) {
				c = (Color) model.getValueAt(row, model.getDataSource()
						.getColumnClasses().length - 1);
			}
		} catch (ExceptionLP e) {
			e.printStackTrace();
		}
		return c;
	}
	
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		DistributedTableModel model = (DistributedTableModel) table.getModel();

		Color forecolor = getSpecialForecolor(model, row);
		if (forecolor != null) {
			if (!isSelected) { 
				setForeground(forecolor);
			}
		} else {
			setForeground(UIManager.getColor("Table.foreground"));
		}
				
		Component component = super.getTableCellRendererComponent(table, value,
				isSelected, hasFocus, row, column);
		try {
			if (model.getDataSource().getTableInfo()
					.isNegativeWerteRoteinfaerben()) {

				if (value != null) {

					if (value instanceof Number) {
						if (((Number) value).doubleValue() < 0) {
							component.setForeground(Color.RED);
						} else {
							component.setForeground(defaultCellForegroundColor);
						}
					}
				}
			}
		} catch (ExceptionLP ex) {
			ex.printStackTrace();
		}
		
		// selektierte Zelle angepasst einfaerben
		try {
			if (isSelected) {
				if (forecolor != null) {
					component.setForeground(HelperClient.getContrastYIQ(forecolor));
					component.setBackground(forecolor);
					
				} else
					component.setBackground(table.getSelectionBackground());
			} else {
				component.setBackground(UIManager.getColor("Table.background"));
			}
		} catch (Exception e) {
			//
		}

		// model
		String text = model.getToolTipAt(row);
		if (text != null) {
			// Laenge auf 1000 Zeichen beschraenken.
			if (text.length() > 1000) {
				text = Helper.cutString(text, 1000) + " ...";
			}
			setToolTipText(text);
		} else {
			setToolTipText(null);
		}

		return component;
	}
}
