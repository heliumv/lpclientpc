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
package com.lp.editor.util;


import java.util.ArrayList;


/**
 * <p><I>Repraesentiert den vom Benutzer definierten Inhalt des
 * Editors</I> </p>
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 * <p>Erstellungsdatum <I>Oktober 2004</I></p>
 * <p> </p>
 * @author Sascha Zelzer
 * @see com.lp.editor.LpEditor#getReportData()
 * @see com.lp.editor.LpEditor#setReportData(LpEditorReportData)
 * @version $Revision: 1.3 $
 */
public class LpEditorReportData
{
  private int iColumnNum;
  private ArrayList<LpEditorRow> rows = new ArrayList<LpEditorRow>();

  public final static String HTML_TB_BEGIN = "<table>";
  public final static String HTML_TB_END = "</table>";
  public final static int HTML_TB_BEGIN_LEN = HTML_TB_BEGIN.length();
  public final static int HTML_TB_END_LEN = HTML_TB_END.length();

  public final static String HTML_TR_BEGIN = "<tr>";
  public final static String HTML_TR_END = "</tr>";
  public final static int HTML_TR_BEGIN_LEN = HTML_TR_BEGIN.length();
  public final static int HTML_TR_END_LEN = HTML_TR_END.length();

  public final static String HTML_TD_BEGIN = "<td>";
  public final static String HTML_TD_END = "</td>";
  public final static int HTML_TD_BEGIN_LEN = HTML_TD_BEGIN.length();
  public final static int HTML_TD_END_LEN = HTML_TD_END.length();

  /**
   * Alle Tabellen in einem Dokument muessen die selbe Spaltenanzahl
   * haben.
   *
   * @param iColumnNum Die Anzahl der Spalten der Tabellen.
   */
  public LpEditorReportData(int iColumnNum) {
    this.iColumnNum = iColumnNum;
  }


  public LpEditorReportData(String sTextAusDb) {
    int iStart = 0;
    if (sTextAusDb == null) {
      sTextAusDb = "";
    }
    int spalten = 0;
    String zaehlen = sTextAusDb.substring(sTextAusDb.indexOf(HTML_TR_BEGIN),
                                          sTextAusDb.indexOf(HTML_TR_END));
    for (int i = 0; i < zaehlen.length() - HTML_TD_BEGIN_LEN; i++) {
      if (zaehlen.substring(i, i + HTML_TD_BEGIN_LEN).equals(HTML_TD_BEGIN)) {
        spalten++;
      }
    }

    String text = null;
    while (iStart < sTextAusDb.length()) {
      if (sTextAusDb.indexOf(HTML_TB_BEGIN,iStart) == -1) {
        text = sTextAusDb.substring(iStart);
        if (text.length() > 0)
          addRow(new LpEditorRow(text));
        break;
      }
      else {
        text = sTextAusDb.substring(iStart, sTextAusDb.indexOf(HTML_TB_BEGIN,iStart));
        if (text.length() > 0)
          addRow(new LpEditorRow(text));
      }
      iStart = sTextAusDb.indexOf(HTML_TB_BEGIN, iStart);
      if (iStart > 0) {
        String tabelle = sTextAusDb.substring(iStart,
                                              sTextAusDb.indexOf(HTML_TB_END, iStart));
        getTabelle(tabelle, spalten);
        iStart = sTextAusDb.indexOf(HTML_TB_END, iStart)+HTML_TB_END_LEN;
      }
      else
        break;
    }
  }

  private void getTabelle(String tabelle, int spalten)
      throws ArrayIndexOutOfBoundsException {

    this.iColumnNum = spalten;
    int spalte = 0;
    int iBegin = 0;
    String[] elements = new String[iColumnNum];
    for (int i = 0; i < tabelle.length() - HTML_TD_END_LEN; i++) {
      if (tabelle.substring(i, i + HTML_TD_BEGIN_LEN).equals(HTML_TD_BEGIN)) {
        iBegin = i + HTML_TD_BEGIN_LEN;
      }
      if (tabelle.substring(i, i + HTML_TD_END_LEN).equals(HTML_TD_END)) {
    	  if(iBegin == i) {
    		  elements[spalte] = "" ;
    	  } else {
    		  elements[spalte] = tabelle.substring(iBegin, i);
    	  }
        spalte++;
      }

      if (tabelle.substring(i, i + HTML_TR_END_LEN).equals(HTML_TR_END)) {
        addRow(new LpEditorRow(elements));
        elements = new String[iColumnNum];
        spalte = 0;
      }
    }
  }


  /**
   * Fuegt ein Datenelement hinzu: Fliesztext oder eine Tabellenzeile.
   *
   * @param row LpEditorRow
   * @throws ArrayIndexOutOfBoundsException Die Anzahl der Spalten
   * in LpEditorRow musz mit der Anzahl von LpEditorReportData
   * uebereinstimmen.
   */
  public void addRow(LpEditorRow row)
      throws ArrayIndexOutOfBoundsException {
    if (row.tableRow != null && row.tableRow.length != iColumnNum) {
      throw new ArrayIndexOutOfBoundsException(
          "Number of columns does not match!");
    }

    rows.add(row);
  }


  public ArrayList<LpEditorRow> getRows() {
    return rows;
  }

  public int getColumnNum() {
    return iColumnNum;
  }
}
