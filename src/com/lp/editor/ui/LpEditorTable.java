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
package com.lp.editor.ui;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.TableModel;
import javax.swing.text.JTextComponent;

import com.lp.editor.LpEditorTableModel;

/**
 * <p><I>Erweitert JTable um Methoden zur automatischen Anpassung
 * der Zeilenhoehe</I> </p>
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 * <p>Erstellungsdatum <I>November 2004</I></p>
 * <p> </p>
 * @author Sascha Zelzer
 * @version $Revision: 1.3 $
 */
public class LpEditorTable
    extends JTable {
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private boolean bDoRowHeightUpdate = true;
  private boolean bPreparingEditor = false;

  public LpEditorTable(LpEditorTableModel model) {
    super(model);
  }

  public void updateRowHeight(int row) {
    if (!bDoRowHeightUpdate) {
      return;
    }

    int iRowHeight = getRowHeight(row);

    TableModel model = getModel();
    if (model instanceof LpEditorTableModel) {
      if (isEditing() && row == getEditingRow()) {
        int newCellHeight = getEditorComponent().getPreferredSize().height;
        ( (LpEditorTableModel) model).setPreferredCellHeight(row,
            getEditingColumn(), newCellHeight);
      }
      int height = ( (LpEditorTableModel) model).getPreferredRowHeight(row);

      if (height != iRowHeight) {
        setRowHeight(row, height);
      }
    }
  }

  public void updateRowHeight(boolean recalculate) {
    for (int i = 0; i < getRowCount(); i++) {
      if (recalculate) {
        for (int j = 0; j < getColumnCount(); j++) {
          Component comp = getCellRenderer(i, j).
              getTableCellRendererComponent(this,
                                            getModel().getValueAt(i, j),
                                            false, false, i, j);

          if (comp instanceof JTextComponent) {
            ( (JTextComponent) comp).updateUI();
          }
          else {
            comp.validate();

          }
          int cellHeight = comp.getPreferredSize().height;
          ( (LpEditorTableModel) getModel()).setPreferredCellHeight(i, j,
              cellHeight+4);
        }
      }
      updateRowHeight(i);
    }
  }

  public void doRowHeightUpdate(boolean flag) {
    bDoRowHeightUpdate = flag;
  }

  public void setPreparingEditor(boolean flag) {
    bPreparingEditor = flag;
  }

  public boolean isPreparingEditor() {
    return bPreparingEditor;
  }
}
