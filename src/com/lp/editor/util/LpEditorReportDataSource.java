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

import java.util.ArrayList;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

/**
 * <p><I>Implementation von JRDatasource um einen Report mit einem
 * LpEditorReportData Objekt befuellen zu koennen, statt ueber ein
 * SQL Statement</I>. Hauptsaechlich fuer Testzwecke gedacht.</p>
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 * <p>Erstellungsdatum <I>Oktober 2004</I></p>
 * <p> </p>
 * @author Sascha Zelzer
 * @version $Revision: 1.3 $
 */
public class LpEditorReportDataSource
    implements JRDataSource {

  private ArrayList<?> rows = null;
  private LpEditorRow currentRow = null;

  private int iColumnNum = 0;
  private int iRowIndex = 0;

  public LpEditorReportDataSource(LpEditorReportData data) {
    //System.out.println("ds row count = " + data.getRows().size());
    this.rows = data.getRows();
    this.iColumnNum = data.getColumnNum();
  }

  public boolean next() throws JRException {
    if (rows == null || rows.size() == 0) {
      return false;
    }
    if (iRowIndex == rows.size()) {
      return false;
    }

    //System.out.println("next row");
    currentRow = (LpEditorRow) rows.get(iRowIndex);
    iRowIndex++;

    return true;
  }

  public Object getFieldValue(JRField field) throws JRException {
    String strFieldName = field.getName();
    if (strFieldName.equals("text")) {
      //System.out.println("geting text");
      return currentRow.getText();
    }
    else if (strFieldName.startsWith("column")) {
      int iColIndex = Integer.parseInt(
          strFieldName.substring(6, strFieldName.length()));
      if (iColIndex < 0 || iColIndex > iColumnNum) {
        return null;
      }
      //System.out.println("geting column" + iColIndex);
      return currentRow.getTableText(iColIndex - 1);
    }
    else {
      return null;
    }
  }
}
