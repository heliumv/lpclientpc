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
package com.lp.client.util.dtable;

import java.util.List;
import java.util.UUID;

import javax.naming.InitialContext;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.delegate.Delegate;
import com.lp.client.pc.LPMain;
import com.lp.client.util.logger.LpLogger;
import com.lp.server.system.fastlanereader.service.FastLaneReader;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.server.util.fastlanereader.service.query.QueryResult;
import com.lp.server.util.fastlanereader.service.query.SortierKriterium;
import com.lp.server.util.fastlanereader.service.query.TableInfo;
import com.lp.util.Pair;

/**
 * Default implementation of DistributedTableDataSource. This implementation
 * connects to the FastLaneReaderBean in order to retrieve data.
 * 
 * @author werner
 */
public class DistributedTableDataSourceImpl extends Delegate implements
		DistributedTableDataSource {

	private Integer useCaseId;
	private String uuid = null;

	public String getUuid() {
		return uuid;
	}

	public Integer getUseCaseId() {
		return useCaseId;
	}

	private boolean returnNullOnGetValueAt = false;

	static protected LpLogger myLogger = null;

	/**
	 * creates a new instance of type DistributedTableDataSourceImpl.
	 * 
	 * @param useCaseId
	 *            the target usecase.
	 */
	public DistributedTableDataSourceImpl(Integer useCaseId) {
		this.useCaseId = useCaseId;
		this.uuid = UUID.randomUUID() + "";
		myLogger = (LpLogger) com.lp.client.util.logger.LpLogger
				.getInstance(this.getClass());
	}

	/**
	 * the result of a query.
	 */
	private QueryResult data;

	/**
	 * yields information about the table (e.g. number of columns and column
	 * types).
	 */
	private TableInfo tableInfo = null;

	/**
	 * the FastLaneReader remote interface.
	 */
	private static FastLaneReader fastLaneReader = null;

	/**
	 * the FastLaneReader home interface.
	 */
	private Object query = null;

	/**
	 * gets the number of rows in the table.
	 * 
	 * @see DistributedTableDataSource#getRowCount()
	 * @return int
	 */
	public long getRowCount() {
		long rowCount = 0;
		if (this.data != null) {
			rowCount = this.data.getRowCount();
		}
		return rowCount;
	}

	/**
	 * gets the number of columns in the table.
	 * 
	 * @see DistributedTableDataSource#getColumnCount()
	 * @return int
	 * @throws ExceptionLP
	 */
	public int getColumnCount() throws ExceptionLP {
		int columnCount = 0;

		columnCount = this.getTableInfo().getColumnClasses().length;
		return columnCount;
	}

	/**
	 * 
	 * @see DistributedTableDataSource#getColumnClasses()
	 * @return Class[]
	 * @throws ExceptionLP
	 */
	public Class<?>[] getColumnClasses() throws ExceptionLP {
		return this.getTableInfo().getColumnClasses();
	}

	/**
	 * 
	 * @see DistributedTableDataSource#getColumnHeaderValues()
	 * @return Object[]
	 * @throws ExceptionLP
	 */
	public Object[] getColumnHeaderValues() {
		Object[] values = null;
		try {
			values = this.getTableInfo().getColumnHeaderValues();
		} catch (ExceptionLP ex) {
			/** @todo JO->JO PJ 5368 */
		}
		return values;
	}

	/**
	 * gets the data for the specified row. If the data is not locally
	 * available, a call to the server replaces the local data.
	 * 
	 * @param rowIndex
	 *            the index of the row for wich data should be returned.
	 * @return the data of the row, with each Object in the array representing
	 *         the data for one column.
	 * @throws ExceptionLP
	 */
	public Object[] getRow(int rowIndex) throws ExceptionLP {
		Object[] row = null;
		if (rowIndex < 0) {
			myLogger.error("invalid rowIndex: rowIndex < 0");
		} else {
			if (this.isRowLocallyAvailable(rowIndex)) {
				try {
					int nRow = this.data.getRowData().length;
					if ((rowIndex - this.data.getIndexOfFirstRow()) < nRow) {
						row = this.data.getRowData()[(int) (rowIndex - this.data
								.getIndexOfFirstRow())];
					} else {
						myLogger.error("rowIndex - this.data.getIndexOfFirstRow()) >= nRow (rowindex='"
								+ rowIndex + "', nRow='" + nRow + "').");
					}
				} catch (Exception ex) {
					myLogger.error(ex);
				}
			} else {
				try {
					this.data = this.getFastLaneReader(this.useCaseId,
							(QueryParameters) query).getPageAt(uuid,
							this.useCaseId, new Integer(rowIndex),
							LPMain.getTheClient());
					if (this.data.isEmpty()) {
						myLogger.error("no data recieved.");
					} else {
						row = this.getRow(rowIndex);
					}
				} catch (Throwable t) {
					handleThrowable(t);
				}
			}
		}
		return row;
	}

	/**
	 * gets the tooltip text for the specified row. If the data is not locally
	 * available, a call to the server replaces the local data.
	 * 
	 * @param rowIndex
	 *            the index of the row for wich data should be returned.
	 * @return the data of the row, with each Object in the array representing
	 *         the data for one column.
	 * @throws ExceptionLP
	 */
	public String getToolTipAt(int rowIndex) throws ExceptionLP {
		String tooltip = null;
		if (rowIndex < 0) {
			myLogger.error("invalid rowIndex");
		} else {
			if (this.isRowLocallyAvailable(rowIndex)) {
				try {
					int nRow = this.data.getRowData().length;
					if (this.data.getTooltipData() != null
							&& (rowIndex - this.data.getIndexOfFirstRow()) < nRow) {
						tooltip = this.data.getTooltipData()[(int) (rowIndex - this.data
								.getIndexOfFirstRow())];
					}
				} catch (Exception ex) {
					myLogger.error(ex);
				}
			} else {
				try {
					this.data = this.getFastLaneReader(this.useCaseId,
							(QueryParameters) query).getPageAt(uuid,
							this.useCaseId, new Integer(rowIndex),
							LPMain.getTheClient());
					if (this.data.isEmpty()) {
						myLogger.error("no data recieved.");
					} else {
						tooltip = this.getToolTipAt(rowIndex);
					}
				} catch (Throwable t) {
					handleThrowable(t);
				}
			}
		}
		return tooltip;
	}

	/**
	 * gets the FastLaneReader's remote interface. All server access is done
	 * through this interface.
	 * 
	 * @return the FastLaneReader's remote interface.
	 * @throws Throwable
	 * @param useCaseIdI
	 *            Integer
	 * @param query
	 *            QueryParameters
	 */
	public FastLaneReader getFastLaneReader(Integer useCaseIdI,
			QueryParameters query) throws Throwable {
		if (fastLaneReader == null) {
			fastLaneReader = (FastLaneReader) new InitialContext()
					.lookup("lpserver/FastLaneReaderBean/remote");
		}
		return fastLaneReader;
	}

	/**
	 * checks if the data for the specified row is locally available. Used to
	 * determine if a server call is necessary in order to retrieve data or not.
	 * 
	 * @param rowIndex
	 *            the row to check for.
	 * @return true, if the data for the row is present, false if a server call
	 *         has to be executed.
	 */
	private boolean isRowLocallyAvailable(int rowIndex) {
		boolean available = false;
		if (this.data != null && this.data.getIndexOfFirstRow() <= rowIndex
				&& this.data.getIndexOfLastRow() >= rowIndex) {
			available = true;
		}
		return available;
	}

	/**
	 * gets the value of the specified cell. If returnNullOnGetValueAt is true,
	 * null is returned for all cells in order to avoid server calls.
	 * 
	 * @see DistributedTableDataSource#getValueAt(int, int)
	 * @param row
	 *            int
	 * @param col
	 *            int
	 * @return Object
	 * @throws ExceptionLP
	 */
	public Object getValueAt(int row, int col) throws ExceptionLP {
		Object value = null;

		if (!this.returnNullOnGetValueAt) {
			Object[] rowValues = this.getRow(row);
			if (rowValues != null && rowValues.length > col && col >= 0) {
				value = rowValues[col];
			}
		}
		return value;
	}

	/**
	 * gets the index of the (previosly) selected row. This is needed in order
	 * to be able to select the row again after a sort.
	 * 
	 * @see DistributedTableDataSource#getIndexOfSelectedRow()
	 * @return int
	 */
	public long getIndexOfSelectedRow() {
		long rowIndex = this.getRowCount() - 1;

		if (this.data != null) {
			rowIndex = this.data.getIndexOfSelectedRow();
		}

		return rowIndex;
	}

	/**
	 * sets the query that is used to determine the table's underlying data.
	 * 
	 * @see DistributedTableDataSource#setQuery(java.lang.Object)
	 * @param queryI
	 *            Object
	 * @throws ExceptionLP
	 */
	public void setQuery(Object queryI) throws ExceptionLP {

		try {
			query = queryI;

			/*
			 * if (bAktualisiereNurNachBedarf == true) { if (data != null &&
			 * bForceRefresh == false) { QueryParameters queryParams =
			 * (QueryParameters) query; if (queryParams.getKeyOfSelectedRow() !=
			 * null) { for (int i = 0; i < data.getRowData().length; i++) { if
			 * (queryParams.getKeyOfSelectedRow().equals(
			 * data.getRowData()[i][0])) { data.setIndexOfSelectedRow(data
			 * .getIndexOfFirstRow() + i); return; } } } } }
			 */

			data = this.getFastLaneReader(useCaseId, (QueryParameters) query)
					.setQuery(uuid, useCaseId, (QueryParameters) query,
							LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	/**
	 * gets (column) information of the table.
	 * 
	 * @return information about the table.
	 * @throws ExceptionLP
	 */
	public TableInfo getTableInfo() throws ExceptionLP {
		if (tableInfo == null) {
			try {
				tableInfo = this.getFastLaneReader(useCaseId,
						(QueryParameters) query).getTableInfo(uuid, useCaseId,
						LPMain.getTheClient());
			} catch (Throwable t) {
				handleThrowable(t);
			}
		}

		return tableInfo;
	}

	/**
	 * sorts the table.
	 * 
	 * @param sortierKriterien
	 *            SortierKriterium[]
	 * @param selectedId
	 *            Object
	 * @throws ExceptionLP
	 * @return QueryResult
	 */
	public QueryResult sort(SortierKriterium[] sortierKriterien,
			Object selectedId) throws ExceptionLP {
		try {
			data = this.getFastLaneReader(useCaseId, (QueryParameters) query)
					.sort(uuid, useCaseId, sortierKriterien, selectedId,
							LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return data;
	}

	/**
	 * @return Returns the returnNullOnGetValueAt.
	 */
	public boolean isReturnNullOnGetValueAt() {
		return returnNullOnGetValueAt;
	}

	/**
	 * @param returnNullOnGetValueAt
	 *            The returnNullOnGetValueAt to set.
	 */
	public void setReturnNullOnGetValueAt(boolean returnNullOnGetValueAt) {
		this.returnNullOnGetValueAt = returnNullOnGetValueAt;
	}

	public void cleanup() {
		if (fastLaneReader != null) {
			try {
				fastLaneReader.cleanup(uuid, useCaseId, LPMain.getTheClient());

				// Warum sollen bei einem Cleanup *dieses* TableDataSource
				// *alle anderen* TableDatasource (Usecasehandler)
				// rausgeschmissen werden?
				// fastLaneReader.cleanup(useCaseId) ;
			} catch (Throwable e) {
				// bereits von jboss removed
			}
			fastLaneReader = null;
		}
	}

	public List<Pair<?, ?>> getInfoForSelectedIIds(List<Object> iids)
			throws ExceptionLP {
		try {
			return this.getFastLaneReader(useCaseId, (QueryParameters) query)
					.getInfoForSelectedIIds(uuid, useCaseId,
							LPMain.getTheClient(), iids);
		} catch (Throwable t) {
			handleThrowable(t);
		}
		return null;
	}
}
