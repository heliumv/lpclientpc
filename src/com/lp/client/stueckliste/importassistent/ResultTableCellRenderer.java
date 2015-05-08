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
 package com.lp.client.stueckliste.importassistent;

import java.awt.Color;
import java.awt.Component;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import com.lp.client.frame.HelperClient;
import com.lp.client.pc.LPMain;
import com.lp.server.stueckliste.service.IStklImportResult;

public class ResultTableCellRenderer extends JLabel implements TableCellRenderer{
	
	private static final long serialVersionUID = 2439960570760252531L;
	
	private NumberFormat format;

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
	
	protected String getValue(Object value) {
		if(value == null) return null;
		if(value instanceof BigDecimal) {
			BigDecimal bd = (BigDecimal) value;
			return getFormat().format(bd.doubleValue());
		}
		return value.toString();
	}
	
	private NumberFormat getFormat() {
		if(format == null) {
			try {
				format = DecimalFormat.getInstance(LPMain.getTheClient().getLocUi());
			} catch (Throwable e) {
				format = DecimalFormat.getInstance();
			}
		}
		return format;
	}
	
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		Color bg = getBackgroundColor(table, row);
		setOpaque(true);
		setBackground(bg);
		setForeground(HelperClient.getContrastYIQ(bg));
		setText(getValue(value));
		if(table.getEditingRow() == row)
			setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, Color.black));
		else
			setBorder(BorderFactory.createEmptyBorder());
		return this;
	}

}
