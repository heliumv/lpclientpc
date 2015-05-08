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
package com.lp.editor;

import java.util.Vector;

import javax.swing.table.DefaultTableModel;
import javax.swing.text.Position;

/**
 * <p><I>Erweitert DefaultTableModel</I> </p>
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 * <p>Erstellungsdatum <I>November 2004</I></p>
 * <p> </p>
 * @author Sascha Zelzer
 * @version $Revision: 1.3 $
 */
public class LpEditorTableModel
    extends DefaultTableModel {

  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private Position posBefore = null;
  private Position posAfter = null;

  private Vector<int[]> vecRowHeight = null;
  private static int DEFAULT_ROW_HEIGHT = 17;

  public LpEditorTableModel(int rows, int cols) {
    super(rows, cols);

    vecRowHeight = new Vector<int[]>(rows);
    for (int i = 0; i < rows; i++) {
      int[] rowHeight = new int[cols];
      for (int j = 0; j < cols; j++) {
        rowHeight[j] = DEFAULT_ROW_HEIGHT;
      }
      vecRowHeight.add(rowHeight);
    }
  }

  public void setPosBefore(Position pos) {
    posBefore = pos;
  }

  public void setPosAfter(Position pos) {
    posAfter = pos;
  }

  public Position getPosBefore() {
    return posBefore;
  }

  public Position getPosAfter() {
    return posAfter;
  }

  public boolean isCellEditable(int row, int col) {
    return true;
  }

  public Class getColumnClass(int column) {
    return String.class;
  }

  public void insertRow(int row, Object[] rowData) {
    super.insertRow(row, rowData);

    int[] rowHeight = new int[getColumnCount()];
    for (int i = 0; i < rowHeight.length; i++) {
      rowHeight[i] = DEFAULT_ROW_HEIGHT;
    }
    vecRowHeight.insertElementAt(rowHeight, row);
  }

  public void insertRow(int row, Vector rowData) {
    super.insertRow(row, rowData);

    int[] rowHeight = new int[getColumnCount()];
    for (int i = 0; i < rowHeight.length; i++) {
      rowHeight[i] = DEFAULT_ROW_HEIGHT;
    }
    vecRowHeight.insertElementAt(rowHeight, row);
  }

  public void removeRow(int row) {
    super.removeRow(row);

    vecRowHeight.remove(row);
  }

  public void setPreferredCellHeight(int row, int column, int height) {
    if (row >= vecRowHeight.size() || column > getColumnCount()) {
      return;
    }

    int[] rowHeight = (int[]) vecRowHeight.get(row);

    rowHeight[column] = height;
  }

  public int getPreferredRowHeight(int row) {
    if (row >= vecRowHeight.size()) {
      return DEFAULT_ROW_HEIGHT;
    }

    int max = 0;
    int[] rowHeight = (int[]) vecRowHeight.get(row);
    for (int i = 0; i < rowHeight.length; i++) {
      max = Math.max(max, rowHeight[i]);

    }
    return max;
  }

}
