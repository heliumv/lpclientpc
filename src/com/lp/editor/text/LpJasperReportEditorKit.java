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

import java.awt.Dimension;
import java.awt.Insets;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.StyledDocument;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JRDesignBand;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.engine.design.JRDesignField;
import net.sf.jasperreports.engine.design.JRDesignGraphicElement;
import net.sf.jasperreports.engine.design.JRDesignLine;
import net.sf.jasperreports.engine.design.JRDesignTextField;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.type.PenEnum;

import com.lp.client.util.logger.LpLogger;
import com.lp.editor.LpEditor;
import com.lp.editor.ui.LpDecoratedTextPane;
import com.lp.editor.util.TextBlockAttributes;

/**
 * <p><I>Ein EditorKit fuer JasperReports</I> </p>
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 * <p>Erstellungsdatum <I>Oktober 2004</I></p>
 * <p> </p>
 * @author Sascha Zelzer
 * @version $Revision: 1.3 $
 */
public class LpJasperReportEditorKit
    extends LpStyledEditorKit {

  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private static Hashtable<?, ?> actions;
  private LpLogger myLogger = (LpLogger) LpLogger.getInstance(LpJasperReportEditorKit.class);

  public LpJasperReportEditorKit() {
    super();
  }

  public String getContentType() {
    return "text/jasper";
  }

  public void read(InputStream in, Document doc, int pos) throws
      IOException, BadLocationException {
    if (doc instanceof StyledDocument) {
      LpJasperParser parser = new LpJasperParser();
      parser.parseFromStream(in, (StyledDocument) doc, pos);
    }
    else {
      // treat as text/plain
      super.read(in, doc, pos);
    }
  }

  public void read(Reader reader, Document doc, int pos) throws
      IOException, BadLocationException {
    if (doc instanceof StyledDocument) {
      LpJasperParser parser = new LpJasperParser();
      parser.parseFromReader(reader, (StyledDocument) doc, pos);
    }
    else {
      // treat as text/plain
      super.read(reader, doc, pos);
    }

  }

  public void write(OutputStream out, Document doc, int pos, int len) throws
      IOException, BadLocationException {
    if (doc instanceof StyledDocument) {
      LpJasperGenerator generator = new LpJasperGenerator();

      if ( (pos < 0) || ( (pos + len) > doc.getLength())) {
        throw new BadLocationException("LPJasperReportEditorKit.write", pos);
      }

      generator.writeFormat( (StyledDocument) doc, out, pos, len);
    }
    else {
      super.write(out, doc, pos, len);
    }
  }

  public void write(Writer writer, Document doc, int pos, int len) throws
      IOException, BadLocationException {
    if (doc instanceof StyledDocument) {
      LpJasperGenerator generator = new LpJasperGenerator();

      if ( (pos < 0) || ( (pos + len) > doc.getLength())) {
        throw new BadLocationException("LPJasperReportEditorKit.write", pos);
      }

      generator.writeFormat( (StyledDocument) doc, writer, pos, len);
    }
    else {
      super.write(writer, doc, pos, len);
    }

  }

  /**
   * Liefert einen JasperReport entsprechend dem vom Benutzer
   * erstellten Layout.
   *
   * @param document StyledDocument
   * @return JasperReport
   */
/*  public JasperReport getJasperReport(StyledDocument document) {
    if (document != null) {
      JasperDesign design = createJasperDesign(document);

      try {
        return JasperCompileManager.compileReport(design);
      }
      catch (JRException e) {
        myLogger.error(e.getLocalizedMessage(),e);
      }
    }
    return null;
  }

  public String getJasperReportXML(StyledDocument document) {
   
    return JasperCompileManager.writeReportToXml(getJasperReport(document));
  }*/

  /**
   * Erzeugt das JasperDesign abhaengig vom aktuellen Layout. Beruecksichtigt
   * noch nicht alle moeglichen Einstellungen
   *
   * @param document StyledDocument
   * @return JasperDesign
   *
   * @todo Anpassen an TextBlockAttributes  PJ 4780
   */
 /* protected JasperDesign createJasperDesign(StyledDocument document) {
    Dimension pageSize = (Dimension) document.getProperty(LpDecoratedTextPane.
        DimensionProperty);
    Insets pageMargin = (Insets) document.getProperty(LpDecoratedTextPane.
        MarginProperty);
    String strTitle = (String) document.getProperty(Document.TitleProperty);
    Vector<?> vecColAttr = (Vector<?>) document.getProperty(LpEditor.
        COLUMN_TEXT_ATTRIBUTES);

    JasperDesign design = new JasperDesign();
    design.setName(strTitle);
    design.setPageWidth(pageSize.width);
    design.setPageHeight(pageSize.height);
    design.setBottomMargin(pageMargin.bottom);
    design.setTopMargin(pageMargin.top);
    design.setLeftMargin(pageMargin.left);
    design.setRightMargin(pageMargin.right);
    design.setColumnWidth(pageSize.width - pageMargin.left - pageMargin.right);

    // Fields
    try {
      JRDesignField field = new JRDesignField();
      field.setName("text");
      field.setValueClass(java.lang.String.class);
      design.addField(field);
      for (int i = 0; i < vecColAttr.size(); i++) {
        field = new JRDesignField();
        field.setName("column" + (i + 1));
        field.setValueClass(java.lang.String.class);
        design.addField(field);
      }
    }
    catch (JRException e) {
      myLogger.error(e.getLocalizedMessage(),e);
    }

    // Detail
    JRDesignBand band = new JRDesignBand();
    band.setHeight(13);
    JRDesignTextField textField = new JRDesignTextField();
    textField.setX(0);
    textField.setY(0);
    textField.setWidth(design.getColumnWidth());
    textField.setHeight(5);
    textField.setStretchWithOverflow(true);
    //textField.setPositionType(textField.POSITION_TYPE_FLOAT);
    textField.setStyledText(true);
    textField.setBlankWhenNull(true);
    textField.setRemoveLineWhenBlank(true);
    JRDesignExpression expression = new JRDesignExpression();
    expression.setValueClass(java.lang.String.class);
    expression.setText("$F{text}");
    textField.setExpression(expression);
    band.addElement(textField);
    // Tabellen Struktur
    // Spaltenbreite auswerten
    int[] aColumnWidth = new int[vecColAttr.size()];
    int iFixedWidth = 0;
    int iFixedCount = 0;
    for (int i = 0; i < aColumnWidth.length; i++) {
      int iWidth = ( (TextBlockAttributes) vecColAttr.get(i)).width;
      if (iWidth != 0) {
        iFixedWidth += iWidth;
        iFixedCount++;
      }
      aColumnWidth[i] = iWidth;
    }
    if (iFixedCount < aColumnWidth.length) {
      int iWidth = (design.getColumnWidth() - iFixedWidth)
          / (aColumnWidth.length - iFixedCount);
      for (int i = 0; i < aColumnWidth.length; i++) {
        if (aColumnWidth[i] == 0) {
          aColumnWidth[i] = iWidth;
        }
      }
    }

    int iTextPosX = 0;
    for (int i = 0; i < aColumnWidth.length; i++) {
      textField = new JRDesignTextField();
      textField.setX(iTextPosX);
      textField.setY(6);
      textField.setWidth(aColumnWidth[i]);
      textField.setHeight(5);
      textField.setStretchWithOverflow(true);
      //textField.setPositionType(textField.POSITION_TYPE_FLOAT);
      //textField.setVerticalAlignment(textField.VERTICAL_ALIGN_BOTTOM);
      textField.setStyledText(true);
      textField.setBlankWhenNull(true);
      textField.setRemoveLineWhenBlank(true);
      expression = new JRDesignExpression();
      expression.setValueClass(java.lang.String.class);
      expression.setText("$F{column" + (i + 1) + "}");
      textField.setExpression(expression);
      textField.setKey("col" + (i + 1));
      band.addElement(textField);
      iTextPosX += aColumnWidth[i];
    }

    JRDesignLine line = new JRDesignLine();
    line.setX(0);
    line.setY(12);
    line.setPositionType(JRDesignLine.POSITION_TYPE_FLOAT);
    line.setWidth(design.getColumnWidth());
    line.setHeight(0);
    line.setPen(PenEnum.PEN_THIN);
    expression = new JRDesignExpression();
    expression.setValueClass(java.lang.Boolean.class);
    expression.setText("new Boolean($F{text}.length() == 0)");
    line.setPrintWhenExpression(expression);
    band.addElement(line);

    design.setDetail(band);

    return design;
  }*/

}
