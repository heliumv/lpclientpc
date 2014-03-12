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

import com.lp.client.frame.ExceptionLP;
import com.lp.server.util.fastlanereader.service.query.QueryResult;
import com.lp.server.util.fastlanereader.service.query.SortierKriterium;
import com.lp.server.util.fastlanereader.service.query.TableInfo;

/**
 * This interface must be implemented by all classes that want to serve as data
 * source for a DistributedTableModel.
 *
 * @author werner
 */
public interface DistributedTableDataSource {

    /**
     * get the total number of rows in the table.
     *
     * @return the number of rows.
     */
    public long getRowCount();


  /**
   * get the number of columns in the table.
   *
   * @return the number of columns.
   * @throws ExceptionLP
   */
  public int getColumnCount() throws ExceptionLP;
  


  /**
   * gets the types for the columns. The column types are needed to install
   * the correct renderers and editors in the table.
   *
   * @return the column types.
   * @throws ExceptionLP
   */
  public Class[] getColumnClasses() throws ExceptionLP;

    /**
     * gets the values to be displayed in the column headers (column names).
     *
     * @return the column names for the table.
     */
    public Object[] getColumnHeaderValues();


  /**
   * gets the data for the specified cell. If necessary data must be fetched
   * from the server.
   *
   * @param row the row coordinate of the cell.
   * @param col the column coordinate of the cell.
   * @return the cell's data
   * @throws ExceptionLP
   */
  public Object getValueAt(int row, int col)  throws ExceptionLP;

  /**
   * gets the data for the specified cell. If necessary data must be fetched
   * from the server.
   *
   * @param row the row coordinate of the cell.
   * @return the cell's data
   * @throws ExceptionLP
   */
  public String getToolTipAt(int row)  throws ExceptionLP;


    /**
     * gets the row number of the row the table should display after a new query
     * has been executed (e.g. sort, new filter criteria,...). Used to keep a
     * selected row visible after a sort or filter operation.
     *
     * @return the row index of the row to display in the table.
     */
    public long getIndexOfSelectedRow();

    public String getUuid();
  /**
   * used to change the query to retrieve data. The query should contain
   * filter criteria, sort orders and the id of the currently selected row (in
   * order to display it after a sort, etc).
   *
   * @param query the query object.
   * @throws ExceptionLP
   */
  public void setQuery(Object query) throws ExceptionLP;


  /**
   * gets information such as column names, column types and db column names.
   *
   * @return information about the table.
   * @throws ExceptionLP
   */
  public TableInfo getTableInfo() throws ExceptionLP;


  /**
   * sorts the table according to the specified sort criterias and returns the
   * page of sorted data that contains the row with the selected id.
   *
   * @param sortierKriterien the sort criterias to use for sorting.
   * @param selectedId the id of the currently selected row in the gui's
   *   table.
   * @return the sorted data containing the page where the row with the
   *   selectedId is located.
   * @throws ExceptionLP
   */
  public QueryResult sort(SortierKriterium[] sortierKriterien,
            Object selectedId) throws ExceptionLP;

    /**
     * gets the null return property for the
     * {@link #getValueAt(int, int) getValueAt()}method.
     *
     * @return true if {@link #getValueAt(int, int) getValueAt()}returns null
     *         for all cells, false otherwise.
     */
    public boolean isReturnNullOnGetValueAt();

    /**
     * sets the return null property for the
     * {@link #getValueAt(int, int) getValueAt()}method. If set to true,
     * {@link #getValueAt(int, int) getValueAt()}should return null for all
     * cells and no server calls should be executed. If set to false, values
     * should be returned normally.
     *
     * @param returnNullOnGetValueAt
     *            the value of the property.
     */
    public void setReturnNullOnGetValueAt(boolean returnNullOnGetValueAt);
    
    public Integer getUseCaseId();
    
    
}
