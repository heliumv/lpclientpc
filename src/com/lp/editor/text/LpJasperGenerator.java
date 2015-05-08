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
package com.lp.editor.text;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Vector;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

import com.lp.client.util.logger.LpLogger;
import com.lp.editor.LpEditor;
import com.lp.editor.LpEditorTableModel;
import com.lp.editor.util.LpEditorReportData;
import com.lp.editor.util.LpEditorRow;

/**
 *
 * <p><I>Analysiert einen formatierten Text und Tabellen in
 * einem StyledDocument und uebersetzt die Formatierung nach
 * Jasper Style-Tags</I> </p>
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 * <p>Erstellungsdatum <I>Oktober 2004</I></p>
 * <p> </p>
 * @author Sascha Zelzer
 * @version $Revision: 1.5 $
 */

public class LpJasperGenerator
    implements LpFormatGenerator {

  private static LpLogger myLogger = (LpLogger) LpLogger.getInstance(
      LpJasperGenerator.class);

  static LpEditorReportData data = null;

  static private AttributeSet defaultAttributes = null;
  static private AttributeSet attributes = null;
  static private String strStyledText = "";
  static private String strContent = "";

  public LpJasperGenerator() {
  }

  /**
   * Schreibt den Text des StyledDocument mit Jasper Style-Tags
   * in den OutputStream. Tabellen-Daten werden ignoriert.
   *
   * @param document StyledDocument
   * @param out OutputStream
   * @throws IOException
   */
  public void writeFormat(StyledDocument document, OutputStream out) throws
      IOException {

    String text = getTextFromReportData(getReportData(document));
    out.write(text.getBytes());
  }

  public void writeFormat(StyledDocument document, OutputStream out, int pos,
                          int length) throws
      IOException {

    String text = getTextBlockContent(document, pos, length);
    out.write(text.getBytes());
  }

  /**
   * Schreibt den Text des StyledDocument mit Jasper Style-Tags
   * in den Writer. Tabellen-Daten werden ignoriert.
   *
   * @param document StyledDocument
   * @param writer Writer
   * @param pos Position offset
   * @param length int
   * @throws IOException
   */
  public void writeFormat(StyledDocument document, Writer writer, int pos,
                          int length) throws IOException {
    writer.write(getTextBlockContent(document, pos, length));
  }

  /**
   * Setzt die Fliesztext - Elemente aus dem data Object zu einem
   * String zusammen.
   *
   * @param data LpEditorReportData
   * @return String
   */
  protected String getTextFromReportData(LpEditorReportData data) {
    ArrayList<?> list = data.getRows();
    LpEditorRow row = null;
    String text = "";
    for (int i = 0; i < list.size(); i++) {
      row = (LpEditorRow) list.get(i);
      if (row.getText() != null) {
        text += row.getText() + "\n";
      }
    }
    if (text.length() > 0) {
      text = text.substring(0, text.length() - 1);
    }
    return text;
  }

  /**
   * Liefert den Text von start bis start+length inklusive
   * Jasper Format - Tags
   * @param document StyledDocument
   * @param start Start - Offset
   * @param length Laenge des Textes
   * @return Text mit Format - Tags
   */
  static public String getTextBlockContent(StyledDocument document,
                                           int start, int length) {
    defaultAttributes = document.getStyle(StyleContext.DEFAULT_STYLE).
        copyAttributes();

    Element rootElement = document.getDefaultRootElement();
    int iElementCount = rootElement.getElementCount();
    strStyledText = "";
    strContent = "";
    for (int i = 0; i < iElementCount; i++) {
      Element paragraphElement = rootElement.getElement(i);
      for (int j = 0; j < paragraphElement.getElementCount(); j++) {
        Element contentElement = paragraphElement.getElement(j);
        if (contentElement.getName().equals("content")) {
          if (contentElement.getStartOffset() < start + length &&
              contentElement.getEndOffset() > start) {
            processContentElement(contentElement, start, start + length);
          }
        }
      }
    }

    if (strContent.length() > 0) {
      if ( (start + length) == document.getLength()) {
        // AD letztes \n nicht filtern
       // strContent = strContent.replaceFirst("\\s*$", "");
      }
      strStyledText += createStyledText(attributes, strContent);
    }

    return strStyledText;
  }


  /**
   * Liefert den Inhalt des aktuellen Dokuments gekapselt in LpEditorReportData.
   * Sehr gut geeignet um die Daten weiters in z.B. einer Datebank abzulegen.
   *
   * @param document StyledDocument
   * @return LpEditorReportData
   * @see com.lp.editor.util.LpEditorReportData
   */
  static public LpEditorReportData getReportData(StyledDocument document) {
    int iColumnNum = 0;
    if (document.getProperty(LpEditor.COLUMN_TEXT_ATTRIBUTES) != null) {
      iColumnNum = ( (Vector<?>) document.getProperty(LpEditor.
          COLUMN_TEXT_ATTRIBUTES)).size();
    }
    data = new LpEditorReportData(iColumnNum);

    Vector<?> vecTableModels = (Vector<?>) document.getProperty(LpEditor.
        TABLE_MODELS_PROP);

    String strTextBlock = "";

    if (vecTableModels.size() == 0) {
      strTextBlock = getTextBlockContent(document, 0, document.getLength());
      if (strTextBlock.length() > 0) {
        LpEditorRow row = new LpEditorRow(strTextBlock);
        data.addRow(row);
      }
    }
    else {
      int startText = 0;
      int endText = 0;
      for (int k = 0; k < vecTableModels.size(); k++) {
        LpEditorTableModel model = (LpEditorTableModel) vecTableModels.get(k);
        endText = model.getPosBefore().getOffset() - 1;

        // Text vor der Tabelle
        if (endText > 0) {
          strTextBlock = getTextBlockContent(document, startText,
                                             endText - startText);
          if (strTextBlock.length() > 0) {
            LpEditorRow row = new LpEditorRow(strTextBlock);
            data.addRow(row);
          }
        }

        int col = model.getColumnCount();
        for (int i = 0; i < model.getRowCount(); i++) {
          String[] rowData = new String[col];
          boolean bEmpty = true;
          for (int j = 0; j < col; j++) {
            String strCell = (String) model.getValueAt(i, j);
            if (strCell == null) {
              rowData[j] = "";
            }
            else {
              rowData[j] = strCell;
              if (rowData[j].trim().length() > 0) {
                bEmpty = false;
              }
            }
          }

          if (!bEmpty) {
            LpEditorRow row = new LpEditorRow(rowData);
            data.addRow(row);
          }
        }

        startText = model.getPosAfter().getOffset() + 1;

        // Text nach der letzten Tabelle
        if (k == vecTableModels.size() - 1) {
          endText = document.getLength();
          if (endText > startText) {
            strTextBlock = getTextBlockContent(document, startText,
                                               endText - startText);
            if (strTextBlock.length() > 0) {
              LpEditorRow row = new LpEditorRow(strTextBlock);
              data.addRow(row);
            }

          }
        }
      }
    }

    return data;
  }

  static protected void processContentElement(Element element, int startpos,
                                              int endpos) {
    int begin = element.getStartOffset();
    if (begin < startpos) {
      begin = startpos;
    }
    int end = element.getEndOffset();
    if (end > endpos) {
      end = endpos;

    }
    String strTmpContent = "";
    try {
      strTmpContent = element.getDocument().getText(begin, end - begin);
    }
    catch (BadLocationException e) {
      myLogger.error(e.getLocalizedMessage(), e);
      //System.out.println("error " + begin + " - " + end);
    }

    if (element.getAttributes().getAttributeCount() > 0
        & strTmpContent.length() > 0) {
      if (attributes != null &&
          compareAttributes(attributes, element.getAttributes())) {
        //attributes.isEqual(element.getAttributes())) {
        strContent += strTmpContent;

        //System.out.println("attribs match, content = " + strContent);
      }
      else {
        if (strContent.length() > 0) {
          strStyledText += createStyledText(attributes, strContent);
        }
        //System.out.println("attribs dont match, styledtext = " + strStyledText);
        strContent = strTmpContent;
        attributes = element.getAttributes();
      }
    }
    else {
      if (strContent.length() > 0) {
        strStyledText += createStyledText(attributes, strContent);
      }
      strStyledText += replaceTextToHTML(strTmpContent);
      //System.out.println("no attribs, styledtext = " + strStyledText);
      strContent = "";
      attributes = null;
    }
  }

  static private boolean compareAttributes(AttributeSet attr1,
                                           AttributeSet attr2) {
    SimpleAttributeSet tmpAttrib1 = new SimpleAttributeSet(defaultAttributes.
        copyAttributes());
    SimpleAttributeSet tmpAttrib2 = new SimpleAttributeSet(defaultAttributes.
        copyAttributes());

    tmpAttrib1.addAttributes(attr1);
    tmpAttrib2.addAttributes(attr2);

    return tmpAttrib1.isEqual(tmpAttrib2) ;
  }

  static private String createStyledText(AttributeSet attributes, String text) {

    if (text == null || text.length() == 0) {
      return "";
    }

    text = replaceTextToHTML(text);

    if (attributes == null || attributes.getAttributeCount() == 0) {
      return text;
    }
    if (defaultAttributes.containsAttributes(attributes)) {
      return text;
    }

    String strStyled = "<style ";

    Enumeration<?> keys = attributes.getAttributeNames();
    while (keys.hasMoreElements()) {
      Object attributeName = keys.nextElement();
      if (!defaultAttributes.containsAttribute(attributeName,
                                               attributes.
                                               getAttribute(attributeName))) {
        // Bold
        if (attributeName == StyleConstants.Bold) {
          strStyled += "isBold=\"" + attributes.getAttribute(attributeName) +
              "\" ";
          // Italic ...
        }
        else if (attributeName == StyleConstants.Italic) {
          strStyled += "isItalic=\"" + attributes.getAttribute(attributeName) +
              "\" ";
          // Underline ...
        }
        else if (attributeName == StyleConstants.Underline) {
          strStyled += "isUnderline=\"" + attributes.getAttribute(attributeName) +
              "\" ";
          //Strikethrough
        }
        else if (attributeName == StyleConstants.StrikeThrough) {
          strStyled += "isStrikeThrough=\"" +
              attributes.getAttribute(attributeName) + "\" ";
          // Font Family & Size
        }
        else if (attributeName == StyleConstants.FontFamily) {
          strStyled += "fontName=\"" + attributes.getAttribute(attributeName) +
              "\" ";
        }
        else if (attributeName == StyleConstants.FontSize) {
          strStyled += "size=\"" + attributes.getAttribute(attributeName) +
              "\" ";
          // Vorder- und Hintergrundfarbe
        }
        else if (attributeName == StyleConstants.Foreground) {
          strStyled += "forecolor=\"" +
              composeColorString(StyleConstants.getForeground(attributes).
                                 getRGB()) +
              "\" ";
        }
        else if (attributeName == StyleConstants.Background) {
          strStyled += "backcolor=\"" +
              composeColorString(StyleConstants.getBackground(attributes).
                                 getRGB()) +
              "\" ";
        }
        else {
          myLogger.debug("unknown attribute " + attributeName);
          //System.out.println("unknown attribute " + attributeName);
        }
      }
    }

    strStyled += ">" + text + "</style>";
    return strStyled;
  }

  private static String replaceTextToHTML(String text) {
    text = text.replaceAll("&", "&amp;");
    text = text.replaceAll("\"", "&quot;");
    text = text.replaceAll("<", "&lt;");
    text = text.replaceAll(">", "&gt;");
    return text;
  }

  
  public static String replaceHTMLtoText(String htmlText) {
	  if(null == htmlText) throw new IllegalArgumentException("htmlText") ;
	  
	  String text = htmlText.replaceAll("&amp;", "&") ;
	  text = text.replaceAll("&quot;", "\"");
	  text = text.replaceAll("&lt;", "<");
	  text = text.replaceAll("&gt;", ">");
	  return text ;
  }
  
  
  static private String composeColorString(int iColorRGB) {
    String sColor = Integer.toHexString(iColorRGB);
    if (sColor.length() > 6) {
      sColor = sColor.substring(sColor.length() - 6);
    }
    sColor = "#" + sColor;
    return sColor;
  }

}
