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

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

/**
 * <p>Diese Klasse ist ein Dokument fuer Textfelder, welches die Eingabe auf
 * n Zeichen beschraenkt.</p>
 *
 * <p>Bechreibung</p>
 * Die Zuweisung zum JTextField geschieht beispielsweise &uuml;ber
 * <yourTextField>.setDocument(new LimitedDocument(5));
 * Danach darf das TextField hoechstens 5 Zeichen enthalten.

 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 * @author  Martin Bluehweis
 * @version $Revision: 1.4 $
 */
class LimitedLengthDocument
    extends PlainDocument {
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected int limit = 0;
  
	protected boolean uppercase=false;
  /**
   * Konstruktor f&uuml;r das limitierte Dokument
   *
   * @param newLimit limit: maximale Anzahl der einzugebenen Zeichen
   */
  public LimitedLengthDocument(int newLimit) {
    super();
    if (limit < 0) {
      limit = 0;
    }
    else {
      limit = newLimit;
    }
  }

  /**
   * ueberschreibt die Methode insertString von PlainDocument
   *
   * @param offset offset: Position
   * @param str str: der String
   * @param attr attr: Attributset
   * @throws BadLocationException
   */
  public void insertString(int offset, String str, AttributeSet attr) throws
      BadLocationException {
    if (str == null) {
      return;
    }
    if(uppercase) {
      str=str.toUpperCase();
    }
    int iLength=getLength() + str.length();
    if (iLength <= limit) {
      super.insertString(offset, str, attr);
    }
    else {
      int iAvailableLength=getLimit()-getLength();
      String s=str.substring(0, iAvailableLength);
      super.insertString(offset, s, attr);
    }
  }

  public int getLimit() {
    return limit;
  }

  public void setUppercase(boolean uppercase) {
    this.uppercase=uppercase;
  }
}
