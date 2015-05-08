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

import java.awt.AWTException;
import java.awt.Component;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.table.TableCellEditor;

import com.lp.server.stueckliste.service.IStklImportResult;

public class ResultTableCellEditorChooser extends JComboBox implements TableCellEditor {
	
	private static final long serialVersionUID = -7357331937277658695L;
	
	private IStklImportResult result;
	private List<CellEditorListener> listeners = new ArrayList<CellEditorListener>();
	private ComboBoxListener cbListener = new ComboBoxListener();
	
	public ResultTableCellEditorChooser() {
		setRenderer(new ResultListCellRenderer());
	}

	@Override
	public Object getCellEditorValue() {
		return result;
	}

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value,
			boolean isSelected, int row, int column) {
		if(value instanceof IStklImportResult) {
			result = (IStklImportResult) value;
			removeItemListener(cbListener);
			removePopupMenuListener(cbListener);
			setModel(new DefaultComboBoxModel(result.getFoundItems().toArray()));
			setSelectedIndex(result.getSelectedIndex() == null ? -1 : result.getSelectedIndex());
			addItemListener(cbListener);
			addPopupMenuListener(cbListener);
			setMinimumSize(getPreferredSize());
			return this;
		} else {
			return new JPanel();
		}
	}

	@Override
	public void addCellEditorListener(CellEditorListener l) {
		if(!listeners.contains(l))
			listeners.add(l);
	}

	@Override
	public void cancelCellEditing() {
		hidePopup();
	}
	
	@Override
	public boolean isCellEditable(EventObject anEvent) {
		return true;
	}

	@Override
	public void removeCellEditorListener(CellEditorListener l) {
		listeners.remove(l);
	}

	@Override
	public boolean shouldSelectCell(EventObject anEvent) {
		if(getParent() != null) getParent().repaint();
		return false;
	}

	@Override
	public boolean stopCellEditing() {
		return true;
	}

	
	private void fireEditingStopped() {
		if(listeners.size() > 0) {
			listeners.get(0).editingStopped(new ChangeEvent(this));
		}
	}
	
	private class ComboBoxListener implements ItemListener, PopupMenuListener {

		@Override
		public void itemStateChanged(ItemEvent e) {
			result.setSelectedIndex(getSelectedIndex());
			if(result.getSelectedArtikelDto() == StklImportPage3Ctrl.FLR_LISTE) {
				removeItemListener(this);
				setSelectedIndex(-1);
				addItemListener(this);
			}
			if(getParent() != null) getParent().repaint();
//			Robot r;
//			try {
//				r = new Robot();
//				ResultTableCellEditorChooser that = ResultTableCellEditorChooser.this;
//				Rectangle rect = new Rectangle(that.getSize());
//				rect.setLocation(that.getLocationOnScreen());
//				Point p = new Point((int)rect.getCenterX(), (int)rect.getCenterY());
////				SwingUtilities.convertPointToScreen(p, that);
//				
//				r.mouseMove(MouseInfo.getPointerInfo().getLocation().x, p.y);
//			} catch (Exception e1) {
//				e1.printStackTrace();
//			}
			fireEditingStopped();
			removeItemListener(this);
			setSelectedIndex(result.getSelectedIndex());
			addItemListener(this);
		}

		@Override
		public void popupMenuCanceled(PopupMenuEvent e) {
			fireEditingStopped();
			removeItemListener(this);
			result.setSelectedIndex(getSelectedIndex());
			addItemListener(this);
		}

		@Override
		public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
			fireEditingStopped();
			setSelectedIndex(result.getSelectedIndex());
		}

		@Override
		public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
		}
	}

}
