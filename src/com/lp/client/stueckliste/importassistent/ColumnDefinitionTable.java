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
import java.awt.Font;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

import com.lp.server.stueckliste.service.FertigungsStklImportSpezifikation;
import com.lp.service.StklImportSpezifikation;

public class ColumnDefinitionTable extends JTable implements IColumnTypeDefiner {
	
	private static final long serialVersionUID = -4735167090574404362L;
	
	private Integer hightlightColumn = null;
	private int fromRow = 0;
	
	private List<IDefinitionListener> listeners = new ArrayList<IDefinitionListener>();
	
	public ColumnDefinitionTable(TableModel tm) {
		super(tm);
		getTableHeader().setReorderingAllowed(false);
		setAutoResizeMode(AUTO_RESIZE_OFF);
		setFont(Font.decode(Font.MONOSPACED));
		setDefaultRenderer(Object.class, new TableCellRenderer() {

			@Override
			public Component getTableCellRendererComponent(JTable table,
					Object value, boolean isSelected, boolean hasFocus,
					int row, int column) {
				JLabel l = new JLabel(value == null ? "" : value.toString());
				l.setFont(table.getFont());
				l.setOpaque(true);
				if(hightlightColumn != null && hightlightColumn.intValue() == column) {
					l.setBackground(Color.black);
					l.setForeground(Color.white);
				} else if(fromRow-1>row)
					l.setBackground(new Color(1f, 0.5f, 0.5f, 0.5f));
				else {
					l.setBackground(Color.white);
				}
				return l;
			}
			
		});
		ColumnsAutoSizer.sizeColumnsToFit(this);
	}
	
	@Override
	public Component prepareRenderer(TableCellRenderer renderer, int row,
			int column) {
		Component c = super.prepareRenderer(renderer, row, column);
		return c;
	}
	
	@Override
	public boolean isCellEditable(int row, int column) {
		return false;
	}

	@Override
	public void highlightColumnAt(Point p) {
		Rectangle bounds = getBounds();
		bounds.setLocation(0, 0);
		if(p == null || !bounds.contains(p)) hightlightColumn = null;
		else {
			int col = columnAtPoint(p);
			hightlightColumn = col > -1 ? col : null;
		}
		repaint();
	}

	@Override
	public void setColumnTypeAt(Point p, String type) {
		Rectangle bounds = getBounds();
		bounds.setLocation(0, 0);
		if(!bounds.contains(p)) return;
		int col = columnAtPoint(p);
		if(col >= 0)
			fireColumnTypeSetEvent(col, type);
	}

	@Override
	public void addDefinitionListener(IDefinitionListener listener) {
		if(!listeners.contains(listener))
			listeners.add(listener);
	}

	@Override
	public void removeDefinitionListener(IDefinitionListener listener) {
		listeners.remove(listener);
	}
	
	protected void fireColumnTypeSetEvent(Integer index, String type) {
		for(IDefinitionListener listener : listeners) {
			listener.updateColumnType(index, type);
		}
	}
	
	public void setSpezifikation(StklImportSpezifikation spezifikation) {
		int i = 0;
		fromRow = spezifikation.getFromRow();
		for(String type : spezifikation.getColumnTypes()) {
			if(i >= getColumnCount())return;
			getTableHeader().getColumnModel().getColumn(i++).setHeaderValue(type);
		}
		repaint();
	}
}
