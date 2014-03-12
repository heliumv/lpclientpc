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

import java.math.BigDecimal;
import java.util.Vector;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

import com.lp.client.frame.HelperClient;
import com.lp.client.frame.LPDefaultTableCellRenderer;
import com.lp.server.artikel.service.SperrenIcon;
import com.lp.util.BigDecimal3;
import com.lp.util.BigDecimal4;
import com.lp.util.BigDecimal6;
import com.lp.util.BigDecimalFinanz;

/**
 * <p>
 * Diese Klasse wrappt eine JTable
 * </p>
 * 
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2008
 * </p>
 * 
 * <p>
 * Erstellung: Martin Bluehweis 24.10.05
 * </p>
 * 
 * <p>
 * @author $Author: adi $
 * </p>
 * 
 * @version not attributable Date $Date: 2012/02/06 16:11:02 $
 */
public class WrapperTable extends JTable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private InternalFrame internalFrame = null;

	public WrapperTable(InternalFrame internalFrame) {
		super();
		setDefaults(internalFrame);
	}

	public WrapperTable(InternalFrame internalFrame, int numRows, int numColumns) {
		super(numRows, numColumns);
		setDefaults(internalFrame);
	}

	public WrapperTable(InternalFrame internalFrame, TableModel dm) {
		super(dm);
		setDefaults(internalFrame);
	}

	public WrapperTable(InternalFrame internalFrame, Object[][] rowData,
			Object[] columnNames) {
		super(rowData, columnNames);
		setDefaults(internalFrame);
	}

	public WrapperTable(InternalFrame internalFrame, Vector<?> rowData,
			Vector<?> columnNames) {
		super(rowData, columnNames);
		setDefaults(internalFrame);
	}

	public WrapperTable(InternalFrame internalFrame, TableModel dm,
			TableColumnModel cm) {
		super(dm, cm);
		setDefaults(internalFrame);
	}

	public WrapperTable(InternalFrame internalFrame, TableModel dm,
			TableColumnModel cm, ListSelectionModel sm) {
		super(dm, cm, sm);
		setDefaults(internalFrame);
	}

	public void valueChanged(ListSelectionEvent e) {
		super.valueChanged(e);
		if (internalFrame != null) {
			getInternalFrame().fireItemChanged(this,
					ItemChangedEvent.ACTION_TABLE_SELECTION_CHANGED);
		}
	}

	private void setDefaults(InternalFrame internalFrame) {
		this.internalFrame = internalFrame;
		// Hier kann die Zeilenhoehe der Table gesteuert werden --> wieder zurueck
		// auf Standard
		// this.setRowHeight(20); // hoehere Zeilen damit Icon Platz hat
		setDefaultRenderer(String.class, new LPDefaultTableCellRenderer());
		setDefaultRenderer(BigDecimal3.class,
				HelperClient.getBigDecimal3Renderer());
		setDefaultRenderer(BigDecimal4.class,
				HelperClient.getBigDecimal4Renderer());
		setDefaultRenderer(BigDecimal6.class,
				HelperClient.getBigDecimal6Renderer());
		setDefaultRenderer(BigDecimal.class,
				HelperClient.getBigDecimalRenderer());
		setDefaultRenderer(Double.class, HelperClient.getDoubleFloatRenderer());
		setDefaultRenderer(Float.class, HelperClient.getDoubleFloatRenderer());
		setDefaultRenderer(Byte.class, HelperClient.getNumberRenderer());
		setDefaultRenderer(Integer.class, HelperClient.getNumberRenderer());
		setDefaultRenderer(Long.class, HelperClient.getNumberRenderer());
		setDefaultRenderer(Short.class, HelperClient.getShortRenderer());
		setDefaultRenderer(java.sql.Date.class, HelperClient.getDateRenderer());
		setDefaultRenderer(java.util.Date.class, HelperClient.getDateRenderer());
		setDefaultRenderer(java.sql.Timestamp.class,
				HelperClient.getTimestampRenderer());
		setDefaultRenderer(javax.swing.JLabel.class,
				HelperClient.getLabelRenderer());
		setDefaultRenderer(ImageIcon.class, HelperClient.getImageIconRenderer());
		setDefaultRenderer(Icon.class, HelperClient.getStatusIconRenderer());
		setDefaultRenderer(SperrenIcon.class,
				HelperClient.getSperrenIconRenderer());
		setDefaultRenderer(Boolean.class, HelperClient.getBooleanRenderer());
		setDefaultRenderer(BigDecimalFinanz.class,
				HelperClient.getBigDecimalFinanzRenderer());

		if (getFont() != null)
			setRowHeight((int) (16 / 11f * getFont().getSize()));
	}

	@Override
	public void updateUI() {
		if (getFont() != null)
			setRowHeight((int) (16 / 11f * getFont().getSize()));
		super.updateUI();
	}

	public InternalFrame getInternalFrame() {
		return internalFrame;
	}
}
