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


import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JPanel;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.system.service.MediaFac;
import com.lp.util.Helper;

@SuppressWarnings("static-access") 
/**
 * <p> Diese Klasse kuemmert sich ...</p>
 *
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 *
 * <p>Erstellung: Vorname Nachname; dd.mm.06</p>
 *
 * <p>@author $Author: christoph $</p>
 *
 * @version not attributable Date $Date: 2009/08/19 08:11:21 $
 *
 * @todo scrollpane auf bild  PJ 3416
 * @todo texte uebersetzen  PJ 3416
 */
public class WrapperSonstige
    extends PanelBasis
{
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private final static String ACTION_SPECIAL_DATEI_OFFEN = "action_special_datei_offen";
  private final static String ACTION_SPECIAL_DATEI_SPEICHERN =
      "action_special_datei_speichern";

  protected WrapperNumberField wnfGroesse = new WrapperNumberField();
  private WrapperTextField wtfDatei = new WrapperTextField();
  private JPanel jpaWorkingOn = new JPanel();
  private GridBagLayout gridBagLayout2 = new GridBagLayout();
  private WrapperButton wbuDatei = new WrapperButton();
  private JButton wbuSpeichern = new JButton();
  private WrapperLabel wlaKb = new WrapperLabel();
  private WrapperTextField fieldToDisplayFileName = null;
  private byte[] datei = null;

  public WrapperButton getButtonDatei() {
    return wbuDatei;
  }


  public WrapperSonstige(InternalFrame internalFrame, String addTitel)
      throws Throwable {
    super(internalFrame, addTitel);
    jbInit();
    initComponents();
  }


  public WrapperButton getDateiButton() {
    return wbuDatei;
  }


  public WrapperSonstige(InternalFrame internalFrame, String addTitel,
                         WrapperTextField fieldToDisplayFileName)
      throws Throwable {
    super(internalFrame, addTitel);
    this.fieldToDisplayFileName = fieldToDisplayFileName;
    jbInit();
    initComponents();
  }


  private void jbInit()
      throws Throwable {
    this.setLayout(new GridBagLayout());
    wbuDatei.setText(LPMain.getInstance().getTextRespectUISPr("lp.datei"));

    wbuSpeichern.setText(LPMain.getInstance().getTextRespectUISPr("lp.speichernunter"));

    wlaKb.setText("kB");
    jpaWorkingOn.setLayout(gridBagLayout2);
    wbuDatei.setMinimumSize(new Dimension(80, Defaults.getInstance().getControlHeight()));
    wbuDatei.setPreferredSize(new Dimension(80, Defaults.getInstance().getControlHeight()));

    wbuSpeichern.setMinimumSize(new Dimension(80, Defaults.getInstance().getControlHeight()));
    wbuSpeichern.setPreferredSize(new Dimension(80,
                                                Defaults.getInstance().getControlHeight()));

    wnfGroesse.setMinimumSize(new Dimension(80, Defaults.getInstance().getControlHeight()));
    wnfGroesse.setPreferredSize(new Dimension(80, Defaults.getInstance().getControlHeight()));
    wnfGroesse.setActivatable(false);

    wbuDatei.setActionCommand(ACTION_SPECIAL_DATEI_OFFEN);
    wbuDatei.addActionListener(this);

    wbuSpeichern.setActionCommand(ACTION_SPECIAL_DATEI_SPEICHERN);
    wbuSpeichern.addActionListener(this);

    wtfDatei.setColumnsMax(MediaFac.MAX_MEDIASTANDARD_DATEINAME);
    wtfDatei.setActivatable(false);

    this.add(jpaWorkingOn, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0
                                                 , GridBagConstraints.CENTER,
                                                 GridBagConstraints.BOTH,
                                                 new Insets(0, 0, 0, 0), 0, 0));

    jpaWorkingOn.add(wbuDatei,
                    new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                                           GridBagConstraints.CENTER,
                                           GridBagConstraints.HORIZONTAL,
                                           new Insets(2, 2, 2, 2), 0, 0));

    jpaWorkingOn.add(wbuSpeichern,
                    new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                                           GridBagConstraints.CENTER,
                                           GridBagConstraints.HORIZONTAL,
                                           new Insets(2, 2, 2, 2), 0, 0));

    jpaWorkingOn.add(wtfDatei,
                    new GridBagConstraints(0, 1, 2, 1, 0.0, 0.0,
                                           GridBagConstraints.CENTER,
                                           GridBagConstraints.HORIZONTAL,
                                           new Insets(2, 2, 2, 2), 0, 0));

    jpaWorkingOn.add(wnfGroesse,
                    new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0,
                                           GridBagConstraints.CENTER,
                                           GridBagConstraints.BOTH,
                                           new Insets(2, 2, 2, 2), 0, 0));
    jpaWorkingOn.add(wlaKb,
                    new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0,
                                           GridBagConstraints.CENTER,
                                           GridBagConstraints.BOTH,
                                           new Insets(2, 2, 2, 2), 0, 0));

  }


  protected void eventActionSpecial(ActionEvent e)
      throws Throwable {
    if (e.getActionCommand().equalsIgnoreCase(ACTION_SPECIAL_DATEI_OFFEN)) {
      JFileChooser open = new JFileChooser();

      int returnVal = open.showOpenDialog(null);
      if (returnVal == JFileChooser.APPROVE_OPTION) {
        File file = open.getSelectedFile();

        double groesseInKB = ( (double) file.length()) /
            ( (double) 1024);

        if (groesseInKB > 1024) {
          DialogFactory.showModalDialog(LPMain.getInstance().getTextRespectUISPr(
              "lp.error"),
                                               LPMain.getInstance().getTextRespectUISPr(
              "lp.error.dateizugross"));
        }
        else {
          // darstellen
          if (fieldToDisplayFileName != null) {
            fieldToDisplayFileName.setText(file.getName());

          }
          wtfDatei.setText(file.getName());
          wbuDatei.setToolTipText(file.getAbsolutePath());

          wnfGroesse.setDouble(new Double(groesseInKB));
          datei = Helper.getBytesFromFile(file);
        }
      }
      else {
        // keine auswahl
      }
    }
    else if (e.getActionCommand().equalsIgnoreCase(ACTION_SPECIAL_DATEI_SPEICHERN)) {
      JFileChooser save = new JFileChooser();
      save.setFileSelectionMode(JFileChooser.FILES_ONLY);
      save.setApproveButtonText(LPMain.getInstance().getTextRespectUISPr("lp.report.save"));
      save.setDialogTitle(LPMain.getInstance().getTextRespectUISPr("lp.report.save"));

      save.showOpenDialog(this);
      int returnVal = save.showOpenDialog(null);
      if (returnVal == JFileChooser.APPROVE_OPTION) {
        File file = save.getSelectedFile();
        // Create file if it does not exist
        java.io.FileOutputStream out = new java.io.FileOutputStream(file);
        out.write(datei);
        out.close();
      }
    }
  }


  public String getDateiname() {
    return wtfDatei.getText();
  }


  public byte[] getDatei() {
    return datei;
  }


  public void setDatei(byte[] datei)
      throws Throwable {
    this.datei = datei;
    if (datei != null) {
      double groesseInKB = ( (double) datei.length) /
          ( (double) 1024);
      wnfGroesse.setDouble(new Double(groesseInKB));
    }
  }


  public void setDateiname(String dateiname) {
    wtfDatei.setText(dateiname);
  }


  protected JComponent getFirstFocusableComponent()
      throws Exception {
    return wbuDatei;
  }

}
