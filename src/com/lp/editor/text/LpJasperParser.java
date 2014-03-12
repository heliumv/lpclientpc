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
package com.lp.editor.text;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import com.lp.client.util.logger.LpLogger;
import com.lp.editor.util.LpEditorReportData;

/**
 * <p><I>Uebersetzt Jasper - StyledText fuer ein StyledDocument</I> </p>
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 * <p>Erstellungsdatum <I>Oktober 2004</I></p>
 * <p> </p>
 * @author Sascha Zelzer
 * @version $Revision: 1.2 $
 */
public class LpJasperParser
    implements LpFormatParser {

  static private LpLogger myLogger = (LpLogger) LpLogger.getInstance(LpJasperParser.class);

  public LpJasperParser() {
  }

  public Dimension getPageSize() {
    return null;
  }

  public Insets getPageMargin() {
    return null;
  }

  public void parseFromStream(InputStream in, StyledDocument document, int pos) {
    try {
      int count = in.available();
      byte[] bytes = new byte[count];
      in.read(bytes);
      String text = new String(bytes);
      parseString(text, document, pos);
    }
    catch (IOException e) {
      myLogger.error(e.getLocalizedMessage(), e);
    }
  }

  public void parseFromReader(Reader reader, StyledDocument document, int pos) {
    BufferedReader bufferedReader = new BufferedReader(reader);
    String text = "";
    String line = null;
    int c = 0;
    try {
      while ( (c = bufferedReader.read()) != -1 ) {
        text += (char)c;
      }

//      while ( (line = bufferedReader.readLine()) != null) {
//        text += line + "\n";
//      }
    }
    catch (IOException e) {
      myLogger.error(e.getLocalizedMessage(),e);
    }

    if (text.length() > 0) {
      // AD letztes \n nicht entfernen
      //text = text.substring(0, text.length() - 1);

    }

    parseString(text, document, pos);
  }

  static public void setReportData(LpEditorReportData data,
                                   StyledDocument document) {
    try {
      document.remove(0, document.getLength());
    }
    catch (BadLocationException e) {
      myLogger.error(e.getLocalizedMessage(),e);
    }

  }

  static public void parseString(String text, StyledDocument document, int pos) {
    SimpleAttributeSet attributes = new SimpleAttributeSet();
    String parsedString = "";
    String strAttr = "";
    String strTmp = "";
    int index = text.indexOf("<style");
    int endStyle = 0;
    int endAttr = 0;
    try {
      if (index < 0) {
        document.insertString(pos,
                              parseEscapeCharacter(text), null);
        return;
      }

      if (index > 0) {
        parsedString = parseEscapeCharacter(text.substring(0, index));
        document.insertString(pos, parsedString, null);
        pos += parsedString.length();
      }
      text = text.substring(index + 6);
      endStyle = text.indexOf(">");

      // Bold
      index = text.indexOf("isBold=\"");
      if (index > -1 & index < endStyle) {
        strTmp = text.substring(index + 8);
        endAttr = strTmp.indexOf("\"");
        strAttr = strTmp.substring(0, endAttr);
        //System.out.println("Attribute isBold = " + strAttr);
        if (strAttr.equals("true")) {
          attributes.addAttribute(StyleConstants.Bold, new Boolean(true));
        }
        else {
          attributes.addAttribute(StyleConstants.Bold, new Boolean(false));
        }
      }
      // Italic
      index = text.indexOf("isItalic=\"");
      if (index > -1 & index < endStyle) {
        strTmp = text.substring(index + 10);
        endAttr = strTmp.indexOf("\"");
        strAttr = strTmp.substring(0, endAttr);
        //System.out.println("Attribute isItalic = " + strAttr);
        if (strAttr.equals("true")) {
          attributes.addAttribute(StyleConstants.Italic, new Boolean(true));
        }
        else {
          attributes.addAttribute(StyleConstants.Italic, new Boolean(false));
        }
      }
      // Underline
      index = text.indexOf("isUnderline=\"");
      if (index > -1 & index < endStyle) {
        strTmp = text.substring(index + 13);
        endAttr = strTmp.indexOf("\"");
        strAttr = strTmp.substring(0, endAttr);
        //System.out.println("Attribute isUnderline = " + strAttr);
        if (strAttr.equals("true")) {
          attributes.addAttribute(StyleConstants.Underline, new Boolean(true));
        }
        else {
          attributes.addAttribute(StyleConstants.Underline, new Boolean(false));
        }
      }
      // StrikeThrough
      index = text.indexOf("isStrikeThrough=\"");
      if (index > -1 & index < endStyle) {
        strTmp = text.substring(index + 17);
        endAttr = strTmp.indexOf("\"");
        strAttr = strTmp.substring(0, endAttr);
        //System.out.println("Attribute isStrikeThrough = " + strAttr);
        if (strAttr.equals("true")) {
          attributes.addAttribute(StyleConstants.StrikeThrough, new Boolean(true));
        }
        else {
          attributes.addAttribute(StyleConstants.StrikeThrough, new Boolean(false));
        }
      }
      // Forecolor
      index = text.indexOf("forecolor=\"");
      if (index > -1 & index < endStyle) {
        strTmp = text.substring(index + 11);
        endAttr = strTmp.indexOf("\"");
        strAttr = strTmp.substring(0, endAttr);
        //System.out.println("Attribute forecolor = " + strAttr);
        attributes.addAttribute(StyleConstants.Foreground, createColor(strAttr));
      }
      // Backcolor
      index = text.indexOf("backcolor=\"");
      if (index > -1 & index < endStyle) {
        strTmp = text.substring(index + 11);
        endAttr = strTmp.indexOf("\"");
        strAttr = strTmp.substring(0, endAttr);
        //System.out.println("Attribute backcolor = " + strAttr);
        attributes.addAttribute(StyleConstants.Background, createColor(strAttr));
      }
      // Font Size
      index = text.indexOf("size=\"");
      if (index > -1 & index < endStyle) {
        strTmp = text.substring(index + 6);
        endAttr = strTmp.indexOf("\"");
        strAttr = strTmp.substring(0, endAttr);
        //System.out.println("Attribute size = " + strAttr);
        attributes.addAttribute(StyleConstants.FontSize,
                                new Integer(Integer.parseInt(strAttr)));
      }
      // Font Family
      index = text.indexOf("fontName=\"");
      if (index > -1 & index < endStyle) {
        strTmp = text.substring(index + 10);
        endAttr = strTmp.indexOf("\"");
        strAttr = strTmp.substring(0, endAttr);
        //System.out.println("Attribute fontName = " + strAttr);
        attributes.addAttribute(StyleConstants.FontFamily, strAttr);
      }

      text = text.substring(endStyle + 1);
      index = text.indexOf("</style>");
      parsedString = text.substring(0, index);

      parsedString = parseEscapeCharacter(parsedString);

      document.insertString(pos, parsedString, attributes);
      pos += parsedString.length();

      text = text.substring(index + 8);
      if (text.length() > 0) {
        parseString(text, document, pos);
      }
    }
    catch (BadLocationException e) {
      myLogger.error(e.getLocalizedMessage(),e);
    }

  }

  static private String parseEscapeCharacter(String text) {
    String parsed = text.replaceAll("&amp;", "&");
    parsed = parsed.replaceAll("&quot;", "\"");
    parsed = parsed.replaceAll("&lt;", "<");
    parsed = parsed.replaceAll("&gt;", ">");

    return parsed;
  }

  static private Color createColor(String sColor) {
    int iColor;

    try {
      if (sColor.length() > 6) {
        iColor = Integer.parseInt(sColor.substring(sColor.length() - 6), 16);
      }
      else {
        iColor = Integer.parseInt(sColor, 16);
      }
    }
    catch (NumberFormatException e) {
      LpLogger.getInstance(LpJasperParser.class).warn("Invalid color specifier");
      return Color.BLACK;
    }
    return new Color(iColor);
  }

}
