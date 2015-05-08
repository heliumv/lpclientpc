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

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Locale;

import javax.swing.JFrame;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;

import com.lp.editor.LpEditor;

public class LpEditorApp
    extends JFrame {

  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private LpEditor editor;

  public LpEditorApp() {
    //setDefaultLookAndFeelDecorated(true);
    try{
    javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
    }
    catch (Exception exc) {
      exc.printStackTrace();
    }
    // Locale locale = new Locale("en");
    String strReport = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
        "<!DOCTYPE jasperReport PUBLIC \"-//JasperReports//DTD Report Design//EN\" \"http://jasperreports.sourceforge.net/dtds/jasperreport.dtd\">" +
        "<jasperReport name=\"Untitled\" pageWidth=\"711\" pageHeight=\"1005\" columnWidth=\"701\" leftMargin=\"5\" rightMargin=\"5\" topMargin=\"5\" bottomMargin=\"5\">" +
        "<reportFont name=\"default\" isDefault=\"true\" fontName=\"Arial\" size=\"12\"/>" +
        "<field name=\"text\" class=\"java.lang.String\"></field>" +
        "<field name=\"column1\" class=\"java.lang.String\"></field>" +
        "<field name=\"column2\" class=\"java.lang.String\"></field>" +
        "<field name=\"column3\" class=\"java.lang.String\"></field>" +
        "<field name=\"column4\" class=\"java.lang.String\"></field>" +
        "<detail><band height=\"13\">" +
        "<textField isStretchWithOverflow=\"true\" isBlankWhenNull=\"true\">" +
        "<reportElement key=\"textblock\" positionType=\"Float\" x=\"0\" y=\"0\" width=\"701\" height=\"5\" isRemoveLineWhenBlank=\"true\"/>" +
        "<textElement textAlignment=\"Left\" isStyledText=\"true\"/>" +
        "<textFieldExpression class=\"java.lang.String\"><![CDATA[$F{text}]]></textFieldExpression>" +
        "</textField>" +
        "<textField isStretchWithOverflow=\"true\" isBlankWhenNull=\"true\">" +
        "<reportElement key=\"column1\" positionType=\"Float\" x=\"0\" y=\"6\" width=\"233\" height=\"5\" isRemoveLineWhenBlank=\"true\"/>" +
        "<textElement isStyledText=\"true\"/>" +
        "<textFieldExpression class=\"java.lang.String\"><![CDATA[$F{column1}]]></textFieldExpression>" +
        "</textField>" +
        "<textField isStretchWithOverflow=\"true\" isBlankWhenNull=\"true\">" +
        "<reportElement key=\"column2\" positionType=\"Float\" x=\"233\" y=\"6\" width=\"233\" height=\"5\" isRemoveLineWhenBlank=\"true\"/>" +
        "<textElement isStyledText=\"true\"/>" +
        "<textFieldExpression class=\"java.lang.String\"><![CDATA[$F{column2}]]></textFieldExpression>" +
        "</textField>" +
        "<textField isStretchWithOverflow=\"true\" isBlankWhenNull=\"true\">" +
        "<reportElement key=\"column3\" positionType=\"Float\" x=\"466\" y=\"6\" width=\"133\" height=\"5\" isRemoveLineWhenBlank=\"true\"/>" +
        "<textElement isStyledText=\"true\"/>" +
        "<textFieldExpression class=\"java.lang.String\"><![CDATA[$F{column3}]]></textFieldExpression>" +
        "</textField>" +
        "<textField isStretchWithOverflow=\"true\" isBlankWhenNull=\"true\">" +
        "<reportElement key=\"column4\" positionType=\"Float\" x=\"600\" y=\"6\" width=\"100\" height=\"5\" isRemoveLineWhenBlank=\"true\"/>" +
        "<textElement isStyledText=\"false\" textAlignment=\"Right\">" +
        "<font size=\"12\" fontName=\"Courier\"/></textElement>" +
        "<textFieldExpression class=\"java.lang.String\"><![CDATA[$F{column4}]]></textFieldExpression>" +
        "</textField>" +

        "<line>" +
        "<reportElement positionType=\"Float\" x=\"0\" y=\"12\" width=\"701\" height=\"0\">" +
        "<printWhenExpression><![CDATA[new Boolean($F{text} == null || $F{text}.length() == 0)]]></printWhenExpression>" +
        "</reportElement>" +
        "<graphicElement pen=\"Thin\"/>" +
        "</line></band></detail></jasperReport>";

    editor = new LpEditor(this);
    try {
 //     String[] match = editor.setFontFilter(new String[] {"dialog", "courier"}
 //                                           , false);

      InputStream stream = new ByteArrayInputStream(strReport.getBytes("UTF-8"));
      JasperReport report = JasperCompileManager.compileReport(stream);
     // editor.setJasperReport(report);
      //editor.setColumnCount(3);
      //editor.showTableItems(false);
    }
    catch (Exception e) {
      e.printStackTrace();
    }
//    editor.getTextBlockAttributes(0).capacity = 40;
//    editor.getTextBlockAttributes(2).capacity = 100;
//    editor.getTextBlockAttributes(3).capacity = 12;
//    editor.showMenu(true);
    editor.showAlignmentItems(false);
    editor.getTextBlockAttributes( -1).capacity = 200;

    com.lp.client.pc.LPMain.getInstance().setUISprLocale(new Locale("de_DE"));
    //editor.setText("as<style isBold=\"true\">fett</style>lj.");
    getContentPane().add(editor, BorderLayout.CENTER);

    setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

    addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        closeFrame();
      }
    });

    editor.getExitMenuItem().addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent ae) {
        closeFrame();
      }
    });

    pack();
    this.setSize(800, 500);

    setVisible(true);

    editor.setDebug(true);

  }

  public void closeFrame() {
    boolean state;
    state = editor.prepareExit();

    if (state) {
      System.exit(0);
    }
  }

  static public void main(String[] args) {
    new LpEditorApp();
  }

}
