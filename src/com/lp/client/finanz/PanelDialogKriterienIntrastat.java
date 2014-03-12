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
package com.lp.client.finanz;


import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.math.BigDecimal;

import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.SwingConstants;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.PanelDialogKriterien;
import com.lp.client.frame.component.WrapperDateField;
import com.lp.client.frame.component.WrapperDateRangeController;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperRadioButton;
import com.lp.client.pc.LPMain;
import com.lp.server.finanz.service.FinanzReportFac;


/**
 * <p> Diese Klasse kuemmert sich um die Eingabe der Kriterien fuer die Intrastatmeldung</p>
 *
 * <p>Copyright Logistik Pur Software GmbH (c) 2004 - 2007</p>
 *
 * <p>Erstellung: Martin Bluehweis; 07.08.07</p>
 *
 * <p>@author $Author: valentin $</p>
 *
 * @version not attributable Date $Date: 2008/08/11 08:09:12 $
 */
public class PanelDialogKriterienIntrastat
    extends PanelDialogKriterien
{
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private WrapperRadioButton wrbEingang = null;
  private WrapperRadioButton wrbVersand = null;
  private WrapperLabel wlaTransportkosten = null;
  private WrapperNumberField wnfTransportkosten = null;
  private WrapperLabel wlaWaehrung = null;
  private ButtonGroup bgBelegart = null;
  private WrapperLabel wlaVon = null;
  private WrapperDateField wdfVon = null;
  private WrapperLabel wlaBis = null;
  private WrapperDateField wdfBis = null;
  private WrapperDateRangeController wdrcDate = null;

  public PanelDialogKriterienIntrastat(
      InternalFrame oInternalFrameI,
      String title)
      throws Throwable {
    super(oInternalFrameI, title);
    jbInitPanel();
    setDefaults();
    initComponents();
  }


  private void jbInitPanel()
      throws Throwable {
    wlaTransportkosten = new WrapperLabel(LPMain.getTextRespectUISPr(
        "bes.transportkosten"));
    wnfTransportkosten = new WrapperNumberField();
    wnfTransportkosten.setMandatoryField(true);
    wnfTransportkosten.setMinimumValue(0);
    wnfTransportkosten.setMinimumSize(new Dimension(100,
        Defaults.getInstance().getControlHeight()));
    wnfTransportkosten.setPreferredSize(new Dimension(100,
        Defaults.getInstance().getControlHeight()));
    wlaTransportkosten.setMinimumSize(new Dimension(100,
        Defaults.getInstance().getControlHeight()));
    wlaTransportkosten.setPreferredSize(new Dimension(100,
        Defaults.getInstance().getControlHeight()));

    wlaWaehrung = new WrapperLabel();
    wlaWaehrung.setHorizontalAlignment(SwingConstants.LEFT);

    wrbEingang = new WrapperRadioButton();
    wrbEingang.setText(LPMain.getTextRespectUISPr("finanz.intrastat.eingang"));
    wrbVersand = new WrapperRadioButton();
    wrbVersand.setText(LPMain.getTextRespectUISPr("finanz.intrastat.versand"));
    bgBelegart = new ButtonGroup();
    bgBelegart.add(wrbEingang);
    bgBelegart.add(wrbVersand);

    wlaVon = new WrapperLabel(LPMain.getTextRespectUISPr("lp.von"));
    wdfVon = new WrapperDateField();
    wdfVon.setMandatoryField(true);
    wlaBis = new WrapperLabel(LPMain.getTextRespectUISPr("lp.bis"));
    wdfBis = new WrapperDateField();
    wdfBis.setMandatoryField(true);
    wlaBis.setMinimumSize(new Dimension(30, Defaults.getInstance().getControlHeight()));
    wlaBis.setPreferredSize(new Dimension(30, Defaults.getInstance().getControlHeight()));

    wdrcDate = new WrapperDateRangeController(wdfVon, wdfBis);

    iZeile++;
    jpaWorkingOn.add(wrbEingang,
                     new GridBagConstraints(1, iZeile, 2, 1, 0.0, 0.0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.BOTH,
                                            new Insets(2, 2, 2, 2), 0, 0));
    iZeile++;
    jpaWorkingOn.add(wrbVersand,
                     new GridBagConstraints(1, iZeile, 2, 1, 0.0, 0.0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.BOTH,
                                            new Insets(2, 2, 2, 2), 0, 0));
    iZeile++;
    jpaWorkingOn.add(wlaTransportkosten,
                     new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.BOTH,
                                            new Insets(2, 2, 2, 2), 0, 0));

    jpaWorkingOn.add(wnfTransportkosten,
                     new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.BOTH,
                                            new Insets(2, 2, 2, 2), 0, 0));
    jpaWorkingOn.add(wlaWaehrung,
                     new GridBagConstraints(2, iZeile, 1, 1, 0.0, 0.0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.BOTH,
                                            new Insets(2, 2, 2, 2), 0, 0));
    iZeile++;
    jpaWorkingOn.add(wlaVon,
                     new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.BOTH,
                                            new Insets(2, 2, 2, 2), 0, 0));
    jpaWorkingOn.add(wdfVon,
                     new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.BOTH,
                                            new Insets(2, 2, 2, 2), 0, 0));
    jpaWorkingOn.add(wlaBis,
                     new GridBagConstraints(2, iZeile, 1, 1, 0.0, 0.0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.BOTH,
                                            new Insets(2, 2, 2, 2), 0, 0));
    jpaWorkingOn.add(wdfBis,
                     new GridBagConstraints(3, iZeile, 1, 1, 0.0, 0.0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.BOTH,
                                            new Insets(2, 2, 2, 2), 0, 0));
    jpaWorkingOn.add(wdrcDate,
                     new GridBagConstraints(4, iZeile, 1, 1, 0.0, 0.0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.BOTH,
                                            new Insets(2, 2, 2, 2), 0, 0));
  }


  private void setDefaults()
      throws Throwable {
    wnfTransportkosten.setBigDecimal(new BigDecimal(0));
    wlaWaehrung.setText(LPMain.getTheClient().getSMandantenwaehrung());
    // Default ist Eingang Selektiert
    wrbEingang.setSelected(true);
    wdrcDate.doClickDown();
    wdrcDate.doClickUp();
  }


  public BigDecimal getTTransportkosten()
      throws Throwable {
    return wnfTransportkosten.getBigDecimal();
  }


  public String getVerfahren() {
    if (wrbEingang.isSelected()) {
      return FinanzReportFac.INTRASTAT_VERFAHREN_WARENEINGANG;
    }
    else {
      return FinanzReportFac.INTRASTAT_VERFAHREN_VERSAND;
    }
  }


  public java.sql.Date getDVon() {
    return wdfVon.getDate();
  }


  public java.sql.Date getDBis() {
    return wdfBis.getDate();
  }


  protected JComponent getFirstFocusableComponent()
      throws Exception {
    return wnfTransportkosten;
  }


  protected void eventActionSpecial(ActionEvent e)
      throws Throwable {
    if (e.getActionCommand().equals(ACTION_SPECIAL_OK)) {
      if (!allMandatoryFieldsSetDlg()) {
        return;
      }
    }
    super.eventActionSpecial(e);
  }
}
