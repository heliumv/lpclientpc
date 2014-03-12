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
package com.lp.editor.util;

/**
 * <p><I>Ein Datenelement des Editors - entspricht einem
 * durchgehenden Fliesztext-Block oder einer Tabellenzeile</I> </p>
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 * <p>Erstellungsdatum <I>Oktober 2004</I></p>
 * <p> </p>
 * @author Sascha Zelzer
 * @version $Revision: 1.1 $
 */
public class LpEditorRow {
  String strText = null;
  String[] tableRow = null;

  public LpEditorRow() {
  }

  /**
   * Konstruktor fuer ein Fliesztext - Element.
   * @param text String
   */
  public LpEditorRow(String text) {
    strText = text;
  }

  /**
   * Konstruktor fuer ein Tabellenzeilen - Element.
   * @param row String[]
   */
  public LpEditorRow(String[] row) {
    strText = null;
    tableRow = row;
  }

  public void setText(String text) {
    strText = text;
  }

  public void setTableRow(String[] row) {
    tableRow = row;
  }

  public String getText() {
    return strText;
  }

  public String getTableText(int index) {
    if (tableRow == null || index < 0 || index > tableRow.length - 1) {
      return "";
    }
    return tableRow[index];
  }

  public String getTableRowText() {
    String s = "";
    if (tableRow == null) {
      return "";
    }
    s += LpEditorReportData.HTML_TR_BEGIN;
    for (int i = 0; i < tableRow.length; i++) {
      s += LpEditorReportData.HTML_TD_BEGIN;
      s += tableRow[i];
      s += LpEditorReportData.HTML_TD_END;
    }
    s += LpEditorReportData.HTML_TR_END;
    return s;
  }
}
