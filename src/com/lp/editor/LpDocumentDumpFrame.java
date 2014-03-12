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
package com.lp.editor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.text.DefaultStyledDocument;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.view.JasperViewer;

import com.lp.editor.text.LpJasperReportEditorKit;
import com.lp.editor.util.LpEditorReportData;
import com.lp.editor.util.LpEditorReportDataSource;
import com.lp.editor.util.TextBlockOverflowException;

/**
 *
 * <p><I>Frame zum Testen und Debuggen des LpEditors</I>.</p>
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 * <p>Erstellungsdatum <I>September 2003</I></p>
 * <p> </p>
 * @author Kajetan Fuchsberger
 * @author Sascha Zelzer
 * @version $Revision: 1.3 $
 */

public class LpDocumentDumpFrame
    extends JFrame {
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

LpEditor lpEditor;

  String[] items = {
      "text/plain", "text/jasper"};

  LpEditorReportData data = null;

  BorderLayout borderLayout1 = new BorderLayout();
  JSplitPane jSplitPane = new JSplitPane();
  JScrollPane jScrollPane1 = new JScrollPane();
  JScrollPane jScrollPane2 = new JScrollPane();
  JTextArea jTextAreaDoc = new JTextArea();
  JTextArea jTextAreaRtf = new JTextArea();
  JPanel jPanelButtons = new JPanel();
  JToolBar jToolBar = new JToolBar();
  JButton jButtonJasper = new JButton();
  JButton jButtonUpdate = new JButton();
  JButton jButtonExportPDF = new JButton();
  JButton jButtonSave = new JButton();
  JButton jButtonRestore = new JButton();
  FlowLayout flowLayout1 = new FlowLayout();
  JCheckBox jCheckBoxMenu = new JCheckBox();
  JCheckBox jCheckBoxToolBar = new JCheckBox();
  JCheckBox jCheckBoxTabRuler = new JCheckBox();
  JCheckBox jCheckBoxStatusBar = new JCheckBox();
  JCheckBox jCheckBoxEnabled = new JCheckBox();
  JCheckBox jCheckBoxFileItems = new JCheckBox();
  JCheckBox jCheckBoxTableItems = new JCheckBox();
  JComboBox jComboBox1 = new JComboBox(items);

  public LpDocumentDumpFrame(LpEditor lpEditor) throws HeadlessException {

    this.lpEditor = lpEditor;

    try {
      jbInit();
      String strType = lpEditor.getTextPane().getContentType();
      jComboBox1.setSelectedItem(strType);
      pack();
    }
    catch (Throwable e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    this.getContentPane().setLayout(borderLayout1);
    this.setTitle("Document - Dump");
    jSplitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
    jSplitPane.setForeground(Color.black);
    jTextAreaDoc.setEditable(false);
    jTextAreaDoc.setText("press 'update ...'");
    jTextAreaRtf.setEditable(false);
    jButtonUpdate.addActionListener(new
                                    LpDocumentDumpFrame_jButtonUpdate_actionAdapter(this));
    jButtonUpdate.setText("Update ...");
    jButtonExportPDF.setText("Export to PDF");
    jButtonExportPDF.addActionListener(new
                                       LpDocumentDumpFrame_jButtonExportPDF_actionAdapter(this));
    jButtonSave.setText("Save");
    jButtonSave.addActionListener(new
                                  LpDocumentDumpFrame_jButtonSave_actionAdapter(this));
    jButtonRestore.setText("Restore");
    jButtonRestore.addActionListener(new
                                     LpDocumentDumpFrame_jButtonRestore_actionAdapter(this));
    jPanelButtons.setLayout(flowLayout1);
    jCheckBoxMenu.setText("Menue");
    jCheckBoxMenu.addActionListener(new
                                    LpDocumentDumpFrame_jCheckBoxMenu_actionAdapter(this));
    jCheckBoxToolBar.setText("ToolBar");
    jCheckBoxToolBar.addActionListener(new
                                       LpDocumentDumpFrame_jCheckBoxToolBar_actionAdapter(this));
    jCheckBoxTabRuler.setText("TabRuler");
    jCheckBoxTabRuler.addActionListener(new
                                        LpDocumentDumpFrame_jCheckBoxTabRuler_actionAdapter(this));
    jCheckBoxStatusBar.setText("StatusBar");
    jCheckBoxStatusBar.addActionListener(new
                                         LpDocumentDumpFrame_jCheckBoxStatusBar_actionAdapter(this));
    jCheckBoxEnabled.setText("enabled");
    jCheckBoxEnabled.addActionListener(new
                                       LpDocumentDumpFrame_jCheckBoxEnabled_actionAdapter(this));
    jCheckBoxFileItems.setSelected(true);
    jCheckBoxFileItems.setText("FileItems");
    jCheckBoxFileItems.addActionListener(new
                                         LpDocumentDumpFrame_jCheckBoxFileItems_actionAdapter(this));
    jComboBox1.addActionListener(new
                                 LpDocumentDumpFrame_jComboBox1_actionAdapter(this));
    jButtonJasper.addActionListener(new
                                    LpDocumentDumpFrame_jButtonJasper_actionAdapter(this));
    jCheckBoxTableItems.setText("Table");
    jCheckBoxTableItems.addActionListener(new
                                          LpDocumentDumpFrame_jCheckBoxTableItems_actionAdapter(this));
    jToolBar.add(jComboBox1);
    jPanelButtons.add(jCheckBoxFileItems, null);
    jToolBar.add(jButtonUpdate, null);
    this.getContentPane().add(jSplitPane, BorderLayout.CENTER);
    jSplitPane.add(jScrollPane1, JSplitPane.LEFT);
    jScrollPane1.getViewport().add(jTextAreaDoc, null);
    jSplitPane.add(jScrollPane2, JSplitPane.RIGHT);
    this.getContentPane().add(jPanelButtons, BorderLayout.SOUTH);
    jScrollPane2.getViewport().add(jTextAreaRtf, null);
    jButtonJasper.setText("Jasper Viewer");
    jToolBar.add(jButtonJasper);
    jToolBar.add(jButtonExportPDF);
    jToolBar.add(jButtonSave);
    jToolBar.add(jButtonRestore);
    this.getContentPane().add(jToolBar, BorderLayout.NORTH);
    jPanelButtons.add(jCheckBoxMenu, null);
    jPanelButtons.add(jCheckBoxToolBar, null);
    jPanelButtons.add(jCheckBoxTableItems, null);
    jPanelButtons.add(jCheckBoxTabRuler, null);
    jPanelButtons.add(jCheckBoxStatusBar, null);
    jPanelButtons.add(jCheckBoxEnabled, null);
    jSplitPane.setDividerLocation(100);

    jCheckBoxMenu.setSelected(lpEditor.isMenuVisible());
    jCheckBoxToolBar.setSelected(lpEditor.isToolBarVisible());
    jCheckBoxTabRuler.setSelected(lpEditor.isTabRulerVisible());
    jCheckBoxStatusBar.setSelected(lpEditor.isStatusBarVisible());
    jCheckBoxEnabled.setSelected(lpEditor.isEditable());
    jCheckBoxFileItems.setSelected(lpEditor.isFileItemVisible());
    jCheckBoxTableItems.setSelected(lpEditor.isTableItemVisible());
  }

  void jButtonUpdate_actionPerformed(ActionEvent e) {
    ByteArrayOutputStream stream = new ByteArrayOutputStream();

    ( (DefaultStyledDocument) lpEditor.getStyledDocument()).dump(new
        PrintStream(stream));
    jTextAreaDoc.setText(stream.toString());

   /* jTextAreaRtf.setText( ( (LpJasperReportEditorKit) lpEditor.getTextPane().
                           getEditorKit()).getJasperReportXML(lpEditor.
        getTextPane().getStyledDocument()));*/
    lpEditor.printMarkerInfo();
    //System.out.println(lpEditor.getTextPane().getText());
  }

  void jCheckBoxMenu_actionPerformed(ActionEvent e) {
    lpEditor.showMenu(jCheckBoxMenu.isSelected());
  }

  void jCheckBoxToolBar_actionPerformed(ActionEvent e) {
    lpEditor.showToolBar(jCheckBoxToolBar.isSelected());
  }

  void jCheckBoxTabRuler_actionPerformed(ActionEvent e) {
    lpEditor.showTabRuler(jCheckBoxTabRuler.isSelected());
  }

  void jCheckBoxStatusBar_actionPerformed(ActionEvent e) {
    lpEditor.showStatusBar(jCheckBoxStatusBar.isSelected());
  }

  void jCheckBoxEnabled_actionPerformed(ActionEvent e) {
    lpEditor.setEditable(jCheckBoxEnabled.isSelected());
  }

  void jCheckBoxFileItems_actionPerformed(ActionEvent e) {
    lpEditor.showFileItems(jCheckBoxFileItems.isSelected());
  }

  void jComboBox1_actionPerformed(ActionEvent e) {
    lpEditor.getTextPane().setContentType( (String) jComboBox1.getSelectedItem());
  }
/*
  void jButtonJasper_actionPerformed(ActionEvent e) {
    //System.out.println("viewing report...");
    try {
      LpEditorReportData data = lpEditor.getReportData();
      LpEditorReportDataSource ds = new LpEditorReportDataSource(data);
      //OverflowDataSource ds = new OverflowDataSource();
      JasperReport report = lpEditor.getJasperReport();

      JasperPrint print = JasperFillManager.fillReport(report, null, ds);
      JasperViewer.viewReport(print);
    }
    catch (JRException exc) {
      exc.printStackTrace();
    }
    catch (TextBlockOverflowException exc) {
      //System.out.println(exc.getMessage());
    }
  }*/

/*  void jButtonExportPDF_actionPerformed(ActionEvent e) {
    //System.out.print("exporting report...");
    try {
      LpEditorReportData data = lpEditor.getReportData();
      LpEditorReportDataSource ds = new LpEditorReportDataSource(data);
      JasperReport report = lpEditor.getJasperReport();

      JasperPrint print = JasperFillManager.fillReport(report, null, ds);
      File file = File.createTempFile("jasper", ".pdf");
      JasperExportManager.exportReportToPdfFile(print, file.getAbsolutePath());
      //System.out.println(file.getAbsolutePath());
    }
    catch (JRException exc) {
      exc.printStackTrace();
    }
    catch (IOException exc) {
      exc.printStackTrace();
    }
    catch (TextBlockOverflowException exc) {
      //System.out.println(exc.getMessage());
    }

  }*/

  void jCheckBoxTableItems_actionPerformed(ActionEvent e) {
    lpEditor.showTableItems(jCheckBoxTableItems.isSelected());
  }

  void jButtonSave_actionPerformed(ActionEvent e) {
    try {
      data = lpEditor.getReportData();
    }
    catch (TextBlockOverflowException exc) {
      //System.out.println(exc.getMessage());
    }
  }

  void jButtonRestore_actionPerformed(ActionEvent e) {
    lpEditor.setReportData(data);
  }

}

class LpDocumentDumpFrame_jButtonUpdate_actionAdapter
    implements java.awt.event.ActionListener {
  LpDocumentDumpFrame adaptee;

  LpDocumentDumpFrame_jButtonUpdate_actionAdapter(LpDocumentDumpFrame adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.jButtonUpdate_actionPerformed(e);
  }
}

class LpDocumentDumpFrame_jCheckBoxMenu_actionAdapter
    implements java.awt.event.ActionListener {
  LpDocumentDumpFrame adaptee;

  LpDocumentDumpFrame_jCheckBoxMenu_actionAdapter(LpDocumentDumpFrame adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.jCheckBoxMenu_actionPerformed(e);
  }
}

class LpDocumentDumpFrame_jCheckBoxToolBar_actionAdapter
    implements java.awt.event.ActionListener {
  LpDocumentDumpFrame adaptee;

  LpDocumentDumpFrame_jCheckBoxToolBar_actionAdapter(LpDocumentDumpFrame
      adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.jCheckBoxToolBar_actionPerformed(e);
  }
}

class LpDocumentDumpFrame_jCheckBoxTabRuler_actionAdapter
    implements java.awt.event.ActionListener {
  LpDocumentDumpFrame adaptee;

  LpDocumentDumpFrame_jCheckBoxTabRuler_actionAdapter(LpDocumentDumpFrame
      adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.jCheckBoxTabRuler_actionPerformed(e);
  }
}

class LpDocumentDumpFrame_jCheckBoxStatusBar_actionAdapter
    implements java.awt.event.ActionListener {
  LpDocumentDumpFrame adaptee;

  LpDocumentDumpFrame_jCheckBoxStatusBar_actionAdapter(LpDocumentDumpFrame
      adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.jCheckBoxStatusBar_actionPerformed(e);
  }
}

class LpDocumentDumpFrame_jCheckBoxEnabled_actionAdapter
    implements java.awt.event.ActionListener {
  LpDocumentDumpFrame adaptee;

  LpDocumentDumpFrame_jCheckBoxEnabled_actionAdapter(LpDocumentDumpFrame
      adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.jCheckBoxEnabled_actionPerformed(e);
  }
}

class LpDocumentDumpFrame_jCheckBoxFileItems_actionAdapter
    implements java.awt.event.ActionListener {
  LpDocumentDumpFrame adaptee;

  LpDocumentDumpFrame_jCheckBoxFileItems_actionAdapter(LpDocumentDumpFrame
      adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.jCheckBoxFileItems_actionPerformed(e);
  }
}

class LpDocumentDumpFrame_jComboBox1_actionAdapter
    implements java.awt.event.ActionListener {
  LpDocumentDumpFrame adaptee;

  LpDocumentDumpFrame_jComboBox1_actionAdapter(LpDocumentDumpFrame adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.jComboBox1_actionPerformed(e);
  }
}

class LpDocumentDumpFrame_jButtonJasper_actionAdapter
    implements java.awt.event.ActionListener {
  LpDocumentDumpFrame adaptee;

  LpDocumentDumpFrame_jButtonJasper_actionAdapter(LpDocumentDumpFrame adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
  //  adaptee.jButtonJasper_actionPerformed(e);
  }
}

class LpDocumentDumpFrame_jButtonExportPDF_actionAdapter
    implements java.awt.event.ActionListener {
  LpDocumentDumpFrame adaptee;

  LpDocumentDumpFrame_jButtonExportPDF_actionAdapter(LpDocumentDumpFrame
      adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
   // adaptee.jButtonExportPDF_actionPerformed(e);
  }
}

class LpDocumentDumpFrame_jCheckBoxTableItems_actionAdapter
    implements java.awt.event.ActionListener {
  LpDocumentDumpFrame adaptee;

  LpDocumentDumpFrame_jCheckBoxTableItems_actionAdapter(LpDocumentDumpFrame
      adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.jCheckBoxTableItems_actionPerformed(e);
  }
}

class LpDocumentDumpFrame_jButtonSave_actionAdapter
    implements java.awt.event.ActionListener {
  LpDocumentDumpFrame adaptee;

  LpDocumentDumpFrame_jButtonSave_actionAdapter(LpDocumentDumpFrame adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.jButtonSave_actionPerformed(e);
  }
}

class LpDocumentDumpFrame_jButtonRestore_actionAdapter
    implements java.awt.event.ActionListener {
  LpDocumentDumpFrame adaptee;

  LpDocumentDumpFrame_jButtonRestore_actionAdapter(LpDocumentDumpFrame adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.jButtonRestore_actionPerformed(e);
  }
}
