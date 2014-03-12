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

import javax.swing.table.TableModel;

import com.lp.client.frame.ExceptionLP;
import com.lp.server.util.fastlanereader.service.query.QueryResult;
import com.lp.server.util.fastlanereader.service.query.SortierKriterium;

/**
 * This class is serves as Model for a JTable. The underlying data can be
 * filtered and sorted using the setQuery() method. The getIndexOfSelectedRow()
 * method returns the new location of a previously selected row after a sort or
 * filter operation.
 *
 * @author werner
 */
public interface DistributedTableModel extends TableModel {

  /**
   * set a new query. The implementation should pass this query to the data
   * source which then internally modifies the model's data.
   *
   * @param query the new query.
   * @throws ExceptionLP
   */
  public void setQuery(Object query)  throws ExceptionLP;

    /**
     * gets the new index of a previously selected row after a filter or sort
     * operation.
     *
     * @return the new index of a selected row after a sort.
     */
    public int getIndexOfSelectedRow();

    public DistributedTableDataSource getDataSource()throws ExceptionLP;

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
   * gets the tooltip text of the specified row. This method delegates to dataSource.
   *
   * @see javax.swing.table.TableModel#getToolTipAt(int)
   * @param row int
   * @return Object
   */
  public String getToolTipAt(int row);
}
