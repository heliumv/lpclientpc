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
package com.lp.client.finanz;


import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.HeadlessException;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.swing.ButtonGroup;
import javax.swing.JComponent;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.PanelDialogKriterien;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperDateField;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperRadioButton;
import com.lp.client.pc.LPMain;
import com.lp.server.system.service.LocaleFac;


/**
 * <p> Diese Klasse kuemmert sich um die Eingabe der Kriterien fuer den Exportlauf</p>
 *
 * <p>Copyright Logistik Pur Software GmbH (c) 2004 - 2007</p>
 *
 * <p>Erstellung: Martin Bluehweis; 15.03.06</p>
 *
 * <p>@author $Author: valentin $</p>
 *
 * @version not attributable Date $Date: 2008/08/11 08:09:12 $
 */
public class PanelDialogKriterienExportlauf
    extends PanelDialogKriterien
{
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private WrapperLabel wlaStichtag = null;
  private WrapperDateField wdfStichtag = null;
  private WrapperCheckBox wcbAuchAusserhalbGueltigkeitszeitraum = null;
  private WrapperCheckBox wcbBelegeAusserhalbGueltigkeitszeitraumNurMarkieren = null;
  private WrapperRadioButton wrbEingangsrechnung = null;
  private WrapperRadioButton wrbRechnung = null;
  private WrapperRadioButton wrbGutschrift = null;
  private ButtonGroup bgBelegart = null;

  private final static String ACTION_SPECIAL_AUSSERHALB =
      "action_special_ausserhalb_gueltigkeitszeitraum";

  public PanelDialogKriterienExportlauf(
      InternalFrame oInternalFrameI,
      String title)
      throws HeadlessException, Throwable {
    super(oInternalFrameI, title);
    jbInitPanel();
    setDefaults();
    initComponents();
  }


  private void jbInitPanel()
      throws Throwable {
    wlaStichtag = new WrapperLabel();
    wlaStichtag.setText(LPMain.getTextRespectUISPr("lp.stichtag"));
    wdfStichtag = new WrapperDateField();
    wlaStichtag.setMinimumSize(new Dimension(100, Defaults.getInstance().getControlHeight()));
    wlaStichtag.setPreferredSize(new Dimension(100,
                                               Defaults.getInstance().getControlHeight()));
    wdfStichtag.setMandatoryField(true);
    wcbAuchAusserhalbGueltigkeitszeitraum = new WrapperCheckBox();
    wcbAuchAusserhalbGueltigkeitszeitraum.setText(LPMain.getTextRespectUISPr(
        "finanz.belegeausserhalbgueltigkeitszeitraum"));
    wcbAuchAusserhalbGueltigkeitszeitraum.setPreferredSize(new Dimension(500,
        Defaults.getInstance().getControlHeight()));
    wcbAuchAusserhalbGueltigkeitszeitraum.setMinimumSize(new Dimension(500,
        Defaults.getInstance().getControlHeight()));
    wcbBelegeAusserhalbGueltigkeitszeitraumNurMarkieren = new WrapperCheckBox();
    wcbBelegeAusserhalbGueltigkeitszeitraumNurMarkieren.setText(LPMain.
        getTextRespectUISPr("finanz.belegeausserhalbgueltigkeitszeitraumnurmarkieren"));
    wcbAuchAusserhalbGueltigkeitszeitraum.addActionListener(this);
    wcbAuchAusserhalbGueltigkeitszeitraum.setActionCommand(ACTION_SPECIAL_AUSSERHALB);
    wrbEingangsrechnung = new WrapperRadioButton();
    wrbEingangsrechnung.setText(LPMain.getTextRespectUISPr("fb.export.eingangsrechnungen"));
    wrbRechnung = new WrapperRadioButton();
    wrbRechnung.setText(LPMain.getTextRespectUISPr("fb.export.rechnungen"));
    wrbGutschrift = new WrapperRadioButton();
    wrbGutschrift.setText(LPMain.getTextRespectUISPr("fb.export.gutschriften"));
    bgBelegart = new ButtonGroup();
    bgBelegart.add(wrbEingangsrechnung);
    bgBelegart.add(wrbRechnung);
    bgBelegart.add(wrbGutschrift);

    iZeile++;
    jpaWorkingOn.add(wrbEingangsrechnung,
                     new GridBagConstraints(1, iZeile, 2, 1, 0.0, 0.0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.BOTH,
                                            new Insets(2, 2, 2, 2), 0, 0));
    iZeile++;
    jpaWorkingOn.add(wrbRechnung,
                     new GridBagConstraints(1, iZeile, 2, 1, 0.0, 0.0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.BOTH,
                                            new Insets(2, 2, 2, 2), 0, 0));
    iZeile++;
    jpaWorkingOn.add(wrbGutschrift,
                     new GridBagConstraints(1, iZeile, 2, 1, 0.0, 0.0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.BOTH,
                                            new Insets(2, 2, 2, 2), 0, 0));
    iZeile++;
    jpaWorkingOn.add(wlaStichtag,
                     new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.BOTH,
                                            new Insets(2, 2, 2, 2), 0, 0));

    jpaWorkingOn.add(wdfStichtag,
                     new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.BOTH,
                                            new Insets(2, 2, 2, 2), 0, 0));
    iZeile++;
    jpaWorkingOn.add(wcbAuchAusserhalbGueltigkeitszeitraum,
                     new GridBagConstraints(0, iZeile, 3, 1, 0.0, 0.0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.BOTH,
                                            new Insets(2, 2, 2, 2), 0, 0));
    iZeile++;
    jpaWorkingOn.add(wcbBelegeAusserhalbGueltigkeitszeitraumNurMarkieren,
                     new GridBagConstraints(0, iZeile, 3, 1, 0.0, 0.0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.BOTH,
                                            new Insets(2, 2, 2, 2), 0, 0));
  }


  public void eventActionSpecial(ActionEvent e)
      throws Throwable {
    super.eventActionSpecial(e);
    if (e.getActionCommand().equals(ACTION_SPECIAL_AUSSERHALB)) {
      boolean bEnable = wcbAuchAusserhalbGueltigkeitszeitraum.isSelected();
      wcbBelegeAusserhalbGueltigkeitszeitraumNurMarkieren.setEnabled(bEnable);
      if (!bEnable) {
        wcbBelegeAusserhalbGueltigkeitszeitraumNurMarkieren.setSelected(false);
      }
    }
  }


  private void setDefaults()
      throws Throwable {
    // Default ist der letzte des Vormonats
    GregorianCalendar c = new GregorianCalendar();
    // das ist ein tag vor dem ersten dieses Monats
    c.set(Calendar.DATE, 1);
    c.set(Calendar.DATE, c.get(Calendar.DATE) - 1);
    wdfStichtag.setDate(c.getTime());
    // Default: auch belege ausserhalb exportieren angehakt, WH 31.03.06 IMS 1894
    wcbAuchAusserhalbGueltigkeitszeitraum.setSelected(true);
    // Default: selektierte Belegart ist die ER
    wrbEingangsrechnung.setSelected(true);
  }


  public java.sql.Date getTStichtag() {
    return wdfStichtag.getDate();
  }


  public boolean getBExportiereBelegeAusserhalbGueltigkeitszeitraum() {
    return wcbAuchAusserhalbGueltigkeitszeitraum.isSelected();
  }


  public boolean getBBelegeAusserhalbGueltigkeitszeitraumNurMarkieren() {
    return wcbBelegeAusserhalbGueltigkeitszeitraumNurMarkieren.isSelected();
  }


  protected JComponent getFirstFocusableComponent()
      throws Exception {
    return wdfStichtag;
  }


  protected String getSBelegartCNr() {
    String sBelegart = null;
    if (wrbEingangsrechnung.isSelected()) {
      sBelegart = LocaleFac.BELEGART_EINGANGSRECHNUNG;
    }
    else if (wrbRechnung.isSelected()) {
      sBelegart = LocaleFac.BELEGART_RECHNUNG;
    }
    else if (wrbGutschrift.isSelected()) {
      sBelegart = LocaleFac.BELEGART_GUTSCHRIFT;
    }
    return sBelegart;
  }
}
