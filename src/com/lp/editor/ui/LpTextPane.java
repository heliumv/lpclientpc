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
package com.lp.editor.ui;

import java.io.IOException;
import java.io.StringWriter;

import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;

/**
 * <p><I>Erweitert ein JTextPane um die Methode
 * getText(int pos, int length)</I> </p>
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 * <p>Erstellungsdatum <I>November 2004</I></p>
 * <p> </p>
 * @author Sascha Zelzer
 * @version $Revision: 1.3 $
 */
public class LpTextPane
    extends JTextPane {

  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

public String getText(int pos, int length) throws BadLocationException {
    String txt;
    try {
      StringWriter buf = new StringWriter();

      StyledDocument doc = getStyledDocument();

      getUI().getEditorKit(this).write(buf, doc, pos, length);

      txt = buf.toString();
    }
    catch (IOException ioe) {
      txt = null;
    }

    return txt;
  }

  public void repaint(int x, int y, int width, int height) {
    super.repaint(0,0,getWidth(),getHeight());
  }

}
