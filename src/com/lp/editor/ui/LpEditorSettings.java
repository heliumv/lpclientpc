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
package com.lp.editor.ui;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.SpinnerNumberModel;

import com.lp.editor.util.LpEditorMessages;

/**
 * <p>Title: LogP Editor</p>
 * <p>Description: Dialog fuer Seitengroesse und Raender</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Logistik Pur Software GmbH</p>
 * @author Sascha Zelzer
 * @author Kajetan Fuchsberger
 * @version 0.0
 * @since 0.0
 *
 */

public class LpEditorSettings
    extends JDialog {

  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
JRadioButton btA4;
  JRadioButton btA5;
  JCheckBox btCustom;
  JLabel labelWidth, labelHeight;
  JSpinner spWidth, spHeight;
  JSpinner spMarginLeft, spMarginRight, spMarginTop, spMarginBottom;

  int option = JOptionPane.CLOSED_OPTION;

  public LpEditorSettings(JFrame parent, String title) {
    super(parent, title, true);

    getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
    JTabbedPane tabs = new JTabbedPane();
    getContentPane().add(tabs);

    btA4 = new JRadioButton("A4");
    btA5 = new JRadioButton("A5");
    JPanel panelRadio = new JPanel(new GridLayout(1, 2));
    panelRadio.add(btA4);
    panelRadio.add(btA5);
    ButtonGroup groupFormat = new ButtonGroup();
    groupFormat.add(btA4);
    groupFormat.add(btA5);

    btCustom = new JCheckBox(LpEditorMessages.getInstance().getString(
        "Settings.Custom"));
    btCustom.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent ae) {
        custom(btCustom.isSelected());
      }
    });
    Box box = Box.createHorizontalBox();
    box.add(btCustom);
    box.add(Box.createHorizontalGlue());

    Box boxCustom = Box.createHorizontalBox();
    boxCustom.add(Box.createHorizontalStrut(15));
    SpinnerNumberModel spModel = new SpinnerNumberModel(0, 0, 10, 1);
    spModel.setMaximum(null);
    spWidth = new JSpinner(spModel);
    spWidth.setPreferredSize(new Dimension(60,
                                           spWidth.getPreferredSize().height));
    spModel = new SpinnerNumberModel(0, 0, 10, 1);
    spModel.setMaximum(null);
    spHeight = new JSpinner(spModel);
    spHeight.setPreferredSize(new Dimension(60,
                                            spHeight.getPreferredSize().height));
    labelWidth = new JLabel(LpEditorMessages.getInstance().getString(
        "Settings.Width") + " ");
    boxCustom.add(labelWidth);
    boxCustom.add(spWidth);
    boxCustom.add(Box.createHorizontalStrut(15));
    labelHeight = new JLabel(LpEditorMessages.getInstance().getString(
        "Settings.Height") + " ");
    boxCustom.add(labelHeight);
    boxCustom.add(spHeight);

    Box boxFormat = Box.createVerticalBox();
    boxFormat.add(panelRadio);
    boxFormat.add(box);
    boxFormat.add(boxCustom);
    boxFormat.setBorder(BorderFactory.createTitledBorder(LpEditorMessages.
        getInstance().getString("Settings.Format")));

    box = Box.createHorizontalBox();
    box.add(new JLabel(LpEditorMessages.getInstance().getString("Settings.Left") +
                       " "));
    spModel = new SpinnerNumberModel(0, 0, 10, 1);
    spModel.setMaximum(null);
    spMarginLeft = new JSpinner(spModel);
    spMarginLeft.setPreferredSize(new Dimension(60,
                                                spMarginLeft.getPreferredSize().
                                                height));
    box.add(spMarginLeft);
    box.add(Box.createHorizontalStrut(15));
    box.add(new JLabel(LpEditorMessages.getInstance().getString(
        "Settings.Right") + " "));
    spModel = new SpinnerNumberModel(0, 0, 10, 1);
    spModel.setMaximum(null);
    spMarginRight = new JSpinner(spModel);
    spMarginRight.setPreferredSize(new Dimension(60,
                                                 spMarginRight.getPreferredSize().
                                                 height));
    box.add(spMarginRight);

    Box boxMargin = Box.createVerticalBox();
    boxMargin.add(box);
    boxMargin.setBorder(BorderFactory.createTitledBorder(LpEditorMessages.
        getInstance().getString("Settings.Margin")));

    box = Box.createHorizontalBox();
    box.add(new JLabel(LpEditorMessages.getInstance().getString("Settings.Top") +
                       " "));
    spModel = new SpinnerNumberModel(0, 0, 10, 1);
    spModel.setMaximum(null);
    spMarginTop = new JSpinner(spModel);
    spMarginTop.setPreferredSize(new Dimension(60,
                                               spMarginTop.getPreferredSize().
                                               height));
    box.add(spMarginTop);
    box.add(Box.createHorizontalStrut(15));
    box.add(new JLabel(LpEditorMessages.getInstance().getString(
        "Settings.Bottom") + " "));
    spModel = new SpinnerNumberModel(0, 0, 10, 1);
    spModel.setMaximum(null);
    spMarginBottom = new JSpinner(spModel);
    spMarginBottom.setPreferredSize(new Dimension(60,
                                                  spMarginBottom.
                                                  getPreferredSize().
                                                  height));
    box.add(spMarginBottom);
    boxMargin.add(Box.createVerticalStrut(3));
    boxMargin.add(box);
    boxMargin.add(Box.createVerticalStrut(2));

    Box boxPaper = Box.createVerticalBox();
    boxPaper.add(boxFormat);
    boxPaper.add(boxMargin);

    tabs.add(LpEditorMessages.getInstance().getString("Settings.Paper"),
             boxPaper);

    Box panelButtons = Box.createHorizontalBox();
    JButton btOK = new JButton(LpEditorMessages.getInstance().getString("Ok"));
    btOK.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent ev) {
        option = JOptionPane.OK_OPTION;
        setVisible(false);
      }
    });
    JButton btCancel = new JButton(LpEditorMessages.getInstance().getString(
        "Cancel"));
    btCancel.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent ev) {
        option = JOptionPane.CANCEL_OPTION;
        setVisible(false);
      }
    });
    panelButtons.add(Box.createHorizontalGlue());
    panelButtons.add(btOK);
    panelButtons.add(Box.createHorizontalGlue());
    panelButtons.add(btCancel);
    panelButtons.add(Box.createHorizontalGlue());

    getContentPane().add(Box.createVerticalStrut(3));
    getContentPane().add(panelButtons);
    getContentPane().add(Box.createVerticalStrut(3));

    pack();
    setResizable(false);
  }

  /**
   * Setzt die Dialog-Elemente dem Format entsprechend.
   *
   * @param dim Die Breite und Hoehe in Millimeter.
   */
  public void setFormat(Dimension dim) {
    custom(false);
    if (dim == LpDecoratedTextPane.FORMAT_A4) {
      btA4.setSelected(true);
    }
    else if (dim == LpDecoratedTextPane.FORMAT_A5) {
      btA5.setSelected(true);
    }
    else {
      custom(true);

    }
    spWidth.setValue(new Integer(dim.width));
    spHeight.setValue(new Integer(dim.height));
  }

  /**
   * Liefert das vom Benutzer eingestellte Format.
   *
   * @return Das Format in Millimeter.
   */
  public Dimension getFormat() {
    if (btCustom.isSelected()) {
      return new Dimension( ( (Integer) spWidth.getValue()).intValue(),
                           ( (Integer) spHeight.getValue()).intValue());
    }
    else {
      if (btA4.isSelected()) {
        return LpDecoratedTextPane.FORMAT_A4;
      }
      else if (btA5.isSelected()) {
        return LpDecoratedTextPane.FORMAT_A5;
      }
      else {
        return null;
      }
    }
  }

  /**
   * Setzt die Dialog-Elemente auf die uebergebenen Werte.
   *
   * @param m Die Raender in Millimeter.
   */
  public void setPageMargin(Insets m) {
    spMarginLeft.setValue(new Integer(m.left));
    spMarginRight.setValue(new Integer(m.right));
    spMarginTop.setValue(new Integer(m.top));
    spMarginBottom.setValue(new Integer(m.bottom));
  }

  public Insets getPageMargin() {
    int left = ( (Integer) spMarginLeft.getValue()).intValue();
    int right = ( (Integer) spMarginRight.getValue()).intValue();
    int top = ( (Integer) spMarginTop.getValue()).intValue();
    int bottom = ( (Integer) spMarginBottom.getValue()).intValue();

    return new Insets(top, left, bottom, right);
  }

  public int getOption() {
    return option;
  }

  protected void custom(boolean state) {
    if (state) {
      btA4.setEnabled(false);
      btA5.setEnabled(false);
      labelWidth.setEnabled(true);
      labelHeight.setEnabled(true);
      spWidth.setEnabled(true);
      spHeight.setEnabled(true);
    }
    else {
      btA4.setEnabled(true);
      btA5.setEnabled(true);
      labelWidth.setEnabled(false);
      labelHeight.setEnabled(false);
      spWidth.setEnabled(false);
      spHeight.setEnabled(false);
    }
  }
}
