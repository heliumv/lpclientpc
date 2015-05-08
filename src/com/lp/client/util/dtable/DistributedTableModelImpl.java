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
package com.lp.client.util.dtable;

import javax.swing.table.DefaultTableModel;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.util.logger.LpLogger;
import com.lp.server.util.fastlanereader.service.query.QueryResult;
import com.lp.server.util.fastlanereader.service.query.SortierKriterium;

/**
 * Default implementation of DistributedTableModel.
 * 
 * @author werner
 */
public class DistributedTableModelImpl extends DefaultTableModel implements
		DistributedTableModel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected final LpLogger myLogger = (LpLogger) LpLogger.getInstance(this
			.getClass());

	/**
	 * the underlying data source.
	 */
	private DistributedTableDataSource dataSource;

	/**
	 * Creates a new instance. This instance will fetch data from dataSource.
	 * 
	 * @param dataSource
	 *            the data source to get data from.
	 */
	public DistributedTableModelImpl(DistributedTableDataSource dataSource) {
		this.dataSource = dataSource;
	}

	public DistributedTableDataSource getDataSource() throws ExceptionLP {
		return dataSource;
	}

	/**
	 * sets a new query that will change the underlying data. This method
	 * delegates to dataSource.setQuery(query).
	 * 
	 * @see DistributedTableModel#setQuery(java.lang.Object)
	 * @param query
	 *            Object
	 * @throws ExceptionLP
	 */
	public void setQuery(Object query)
      throws ExceptionLP { try {
    	  this.dataSource.setQuery(query);
    	  
    	  dataSource.setReturnNullOnGetValueAt(false);
      } catch (ExceptionLP e) {
    	  // Damit im Falle eines Fehler keine alten Daten in der Tabelle angezeigt werden
    	  dataSource.setReturnNullOnGetValueAt(true);
    	  throw e;
      }
  }

	/**
	 * gets the number of columns of the table. This method delegates to the
	 * dataSource.
	 * 
	 * @see javax.swing.table.TableModel#getColumnCount()
	 * @return int
	 */
	public int getColumnCount() {
		int columnCount = 0;
		if (this.dataSource != null) {
			try {
				columnCount = this.dataSource.getColumnCount();
			} catch (ExceptionLP ex) {
				// /** @todo JO->JO PJ 5369 */
			}
		}
		return columnCount;
	}

	/**
	 * gets the number of rows in the table. This method delegates to the
	 * dataSource.
	 * 
	 * @see javax.swing.table.TableModel#getRowCount()
	 * @return int
	 */
	public int getRowCount() {
		long rowCount = 0;
		if (this.dataSource != null) {

			rowCount = this.dataSource.getRowCount();
		}
		return (int) rowCount;
	}

	/**
	 * returns always false since FastLaneReader tables cannot be editable.
	 * 
	 * @see javax.swing.table.TableModel#isCellEditable(int, int)
	 * @param arg0
	 *            int
	 * @param arg1
	 *            int
	 * @return boolean
	 */
	public boolean isCellEditable(int arg0, int arg1) {
		return false;
	}

	/**
	 * gets the column's type. This is needed in order to install the correct
	 * Renderers in the table.
	 * 
	 * @see javax.swing.table.TableModel#getColumnClass(int)
	 * @param col
	 *            int
	 * @return Class
	 */
	public Class getColumnClass(int col) {

		Class<Object> columnClass = null;
		try {
			if (this.dataSource != null
					&& this.dataSource.getColumnCount() > col && col >= 0) {
				columnClass = this.dataSource.getColumnClasses()[col];
			}
		} catch (ExceptionLP ex) {
			/** @todo JO->JO PJ 5369 */
		}
		if (columnClass == null) {
			columnClass = Object.class;
		}

		return columnClass;
	}

	/**
	 * gets the data of the specified cell. This method delegates to dataSource.
	 * 
	 * @see javax.swing.table.TableModel#getValueAt(int, int)
	 * @param row
	 *            int
	 * @param col
	 *            int
	 * @return Object
	 */
	public Object getValueAt(int row, int col) {
		Object value = null;
		try {
			if (this.dataSource != null) {
				value = this.dataSource.getValueAt(row, col);
			}
		} catch (Throwable t) {
			myLogger.error("Zelle kann nicht befuellt werden.", t);

			value = "x"; // @todo rot markieren PJ 5370
		}

		return value;
	}

	/**
	 * gets the tooltip text of the specified row. This method delegates to
	 * dataSource.
	 * 
	 * @see javax.swing.table.TableModel#getToolTipAt(int)
	 * @param row
	 *            int
	 * @return Object
	 */
	public String getToolTipAt(int row) {
		String value = null;
		try {
			if (this.dataSource != null) {
				value = this.dataSource.getToolTipAt(row);
			}
		} catch (Throwable t) {
			myLogger.error("Zelle kann nicht befuellt werden.", t);

			value = "x"; // @todo rot markieren PJ 5370
		}

		return value;
	}

	/**
	 * this method does nothing since editing is not allowed here.
	 * 
	 * @see javax.swing.table.TableModel#setValueAt(java.lang.Object, int, int)
	 * @param arg0
	 *            Object
	 * @param arg1
	 *            int
	 * @param arg2
	 *            int
	 */
	public void setValueAt(Object arg0, int arg1, int arg2) {
		// read only, so no set available!
	}

	/**
	 * gets the column's name. Delegates to dataSource.getColumnHeaderValue().
	 * 
	 * @see javax.swing.table.TableModel#getColumnName(int)
	 * @param col
	 *            int
	 * @return String
	 */
	public String getColumnName(int col) {
		String columnName = "Column " + col;
		if (this.dataSource != null
				&& this.dataSource.getColumnHeaderValues() != null
				&& this.dataSource.getColumnHeaderValues().length > col
				&& col >= 0) {
			columnName = this.dataSource.getColumnHeaderValues()[col]
					.toString();
		}

		return columnName;
	}

	/**
	 * gets the index of the previously selected row after a sort or filter
	 * operation. Used to scroll to the new position of the row in order to keep
	 * it visible.
	 * 
	 * @return int
	 */
	public int getIndexOfSelectedRow() {
		long index = 0;
		if (this.dataSource != null) {
			index = this.dataSource.getIndexOfSelectedRow();
		}
		return (int) index;
	}

	/**
	 * sorts the table according to the specified sort criterias and returns the
	 * page of sorted data that contains the row with the selected id.
	 * 
	 * @param sortierKriterien
	 *            the sort criterias to use for sorting.
	 * @param selectedId
	 *            the id of the currently selected row in the gui's table.
	 * @return the sorted data containing the page where the row with the
	 *         selectedId is located.
	 * @throws ExceptionLP
	 */
	public QueryResult sort(SortierKriterium[] sortierKriterien,
			Object selectedId) throws ExceptionLP {

		QueryResult result = null;

		if (this.dataSource != null) {
			result = this.dataSource.sort(sortierKriterien, selectedId);
		}

		return result;
	}
}
