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
 *******************************************************************************/
package com.lp.client.stueckliste.importassistent;

import java.awt.Color;
import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import com.lp.server.stueckliste.service.IStklImportResult;

public class ResultTableCellRendererCheckbox extends JCheckBox implements
		TableCellRenderer {

	private static final long serialVersionUID = 1872004671007175678L;
	
	public ResultTableCellRendererCheckbox() {
		setHorizontalAlignment(CENTER);
	}

	protected Color getBackgroundColor(JTable table, int row) {
		if(table.getModel() instanceof ResultTableModel) {
			IStklImportResult result = ((ResultTableModel)table.getModel()).getResultAtRow(row);
			if(result.isTotalMatch())
				return new Color(150, 255, 150);
			if(result.getSelectedIndex() != null && result.getSelectedIndex() != -1)
				return Color.white;
			if(result.getFoundItems().size() > 2)
				return new Color(255, 255, 125);
			return new Color(255, 150, 150);
		}
		return Color.white;
	}

	@Override
	/**
	 * Rendert eine Zelle der Spalte Soko Update.
	 * Ist die Zelle nicht editierbar, also wenn die Zeile ein TotalMatch ist, 
	 * kein Handartikel ausgew&auml;hlt wurde oder die Kundeartikelnummer
	 * nicht vorhanden ist, dann wird f&uuml;r diese Zelle ein Panel retourniert, 
	 * 
	 * Ansonsten soll dieser Artikel f&uuml;r ein Soko Update ausw&auml;hlbar
	 * sein -> Checkbox erscheint und wird mit value gesetzt
	 */
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {

		JComponent retComponent;
		Color bg = getBackgroundColor(table, row);
		
		if(table.isCellEditable(row, column) ) {
			setSelected((Boolean) value);
			retComponent = this;
		} else {
			JPanel panel = new JPanel();
			retComponent = panel; 
		}
		
		retComponent.setOpaque(true);
		retComponent.setBackground(bg);
		
		if(table.getEditingRow() == row)
			retComponent.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, Color.black));
		else
			retComponent.setBorder(BorderFactory.createEmptyBorder());

		return retComponent;
	}

}
