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


/**
 * <p>Diese Klasse ist ein Dokument fuer Textfelder, welches die Eingabe auf
 * n Ziffern beschraenkt.</p>
 *
 * <p>Bechreibung</p>
 * Die Zuweisung zum JTextField geschieht beispielsweise &uuml;ber
 * <yourTextField>.setDocument(new LimitedDocument(5));
 * Danach darf das TextField hoechstens 5 Zeichen enthalten.

 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 * @author  Martin Bluehweis
 * @version $Revision: 1.2 $
 */
public class LimitedLengthNumberDocument extends LimitedLengthDocument
{
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

public LimitedLengthNumberDocument(int newLimit) {
    super(newLimit);
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
    if ( (getLength() + str.length()) <= limit) {
      super.insertString(offset, str, attr);
    }
  }

}
