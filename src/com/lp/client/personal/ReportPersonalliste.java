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
package com.lp.client.personal;


import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.Date;

import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JPanel;

import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperDateField;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperRadioButton;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.report.PanelReportIfJRDS;
import com.lp.client.frame.report.PanelReportKriterien;
import com.lp.client.pc.LPMain;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.personal.service.PersonalFac;
import com.lp.server.system.service.MailtextDto;
import com.lp.server.util.report.JasperPrintLP;

@SuppressWarnings("static-access")
public class ReportPersonalliste
    extends PanelBasis implements PanelReportIfJRDS
{
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
protected JPanel jpaWorkingOn = new JPanel();
  private GridBagLayout gridBagLayout2 = new GridBagLayout();
  private GridBagLayout gridBagLayout1 = new GridBagLayout();
  private WrapperDateField wdfStichtag = new WrapperDateField();
  private WrapperCheckBox wcbBarcodeliste = new WrapperCheckBox();
  private WrapperCheckBox wcbMitVersteckten = new WrapperCheckBox();

  private WrapperCheckBox wcbStichtag = new WrapperCheckBox();

  private ButtonGroup buttonGroupPersonalSortierung = new ButtonGroup();
  private WrapperRadioButton wrbName = new WrapperRadioButton();
  private WrapperRadioButton wrbAusweis = new WrapperRadioButton();
  private WrapperRadioButton wrbPersonalnummer = new WrapperRadioButton();
  private WrapperRadioButton wrbZutrittsklasse = new WrapperRadioButton();
  private WrapperRadioButton wrbGeburtstag = new WrapperRadioButton();

  public ReportPersonalliste(InternalFramePersonal internalFrame,
                             String add2Title)
      throws Throwable {
    super(internalFrame, add2Title);
    LPMain.getInstance().getTextRespectUISPr("pers.report.personalliste");
    jbInit();
    initComponents();
    wdfStichtag.setDate(new Date(System.currentTimeMillis()));
  }


  protected JComponent getFirstFocusableComponent()
      throws Exception {
    return wdfStichtag;
  }


  protected void eventActionSpecial(ActionEvent e)
      throws Throwable {
    if (e.getSource().equals(wcbStichtag)) {
      if (wcbStichtag.isSelected()) {
        wdfStichtag.setEnabled(true);
      }
      else {
        wdfStichtag.setEnabled(false);
      }
    }
  }


  private void jbInit()
      throws Throwable {
    this.setLayout(gridBagLayout1);
    jpaWorkingOn.setLayout(gridBagLayout2);

    wdfStichtag.setMandatoryField(true);
    wcbBarcodeliste.setText(LPMain.getInstance().getTextRespectUISPr(
        "pers.report.barcodeliste"));

    wrbAusweis.setText(LPMain.getInstance().getTextRespectUISPr("pers.personal.ausweis"));
    wrbName.setText(LPMain.getInstance().getTextRespectUISPr("lp.name"));
    wrbPersonalnummer.setText(LPMain.getInstance().getTextRespectUISPr(
        "pers.personal.personalnummer"));
    wrbZutrittsklasse.setText(LPMain.getInstance().getTextRespectUISPr(
        "pers.zutritt.zutrittsklasse"));
    wcbStichtag.setText(LPMain.getInstance().getTextRespectUISPr("lp.stichtag"));
    
    wrbGeburtstag.setText(LPMain.getInstance().getTextRespectUISPr("lp.geburtstag"));
    
    wcbStichtag.addActionListener(this);
    wcbStichtag.setSelected(false);
    wdfStichtag.setEditable(false);

    wcbMitVersteckten.setText(LPMain.getInstance().getTextRespectUISPr("lp.versteckte"));


    buttonGroupPersonalSortierung.add(wrbAusweis);
    buttonGroupPersonalSortierung.add(wrbName);
    buttonGroupPersonalSortierung.add(wrbPersonalnummer);
    buttonGroupPersonalSortierung.add(wrbZutrittsklasse);
    buttonGroupPersonalSortierung.add(wrbGeburtstag);

    wrbName.setSelected(true);

    this.add(jpaWorkingOn,
             new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.WEST,
                                    GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

    WrapperLabel wlaSortierung = new WrapperLabel(LPMain.getInstance().
                                                  getTextRespectUISPr("label.sortierung"));
    jpaWorkingOn.add(wlaSortierung, new GridBagConstraints(0, 0, 1, 1, 0.05, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
        0, 0));

    jpaWorkingOn.add(wrbName, new GridBagConstraints(1, 0, 1, 1, 0.1, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
        0, 0));
    jpaWorkingOn.add(wrbAusweis, new GridBagConstraints(1, 1, 1, 1, 0, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
        0, 0));
    jpaWorkingOn.add(wrbPersonalnummer, new GridBagConstraints(1, 2, 1, 1, 0, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
        0, 0));
    jpaWorkingOn.add(wrbGeburtstag, new GridBagConstraints(1, 3, 1, 1, 0, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
        0, 0));



    jpaWorkingOn.add(wcbStichtag, new GridBagConstraints(2, 0, 1, 1, 0.05, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
        0, 0));
    if (DelegateFactory.getInstance().getTheJudgeDelegate().hatRecht(RechteFac.
          RECHT_LP_DARF_VERSTECKTE_SEHEN)) {
    jpaWorkingOn.add(wcbMitVersteckten, new GridBagConstraints(2, 1, 2, 1, 0, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
        0, 0));
    }
    jpaWorkingOn.add(wdfStichtag, new GridBagConstraints(3, 0, 1, 1, 0, 0.1
      , GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
      0, 0));
  jpaWorkingOn.add(wcbBarcodeliste, new GridBagConstraints(4, 0, 1, 1, 0, 0.2
      , GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
      200, 0));








  }


  public String getModul() {
    return PersonalFac.REPORT_MODUL;
  }


  public String getReportname() {
    return PersonalFac.REPORT_PERSONALLISTE;
  }


  public JasperPrintLP getReport(String sDrucktype)
      throws Throwable {

    int iOptionSortierung = -1;

    if (wrbName.isSelected()) {
      iOptionSortierung = PersonalFac.REPORT_PERSONALLISTE_OPTION_SORTIERUNG_NAME;
    }
    else if (wrbAusweis.isSelected()) {
      iOptionSortierung = PersonalFac.REPORT_PERSONALLISTE_OPTION_SORTIERUNG_AUSWEIS;
    }
    else if (wrbPersonalnummer.isSelected()) {
      iOptionSortierung = PersonalFac.
          REPORT_PERSONALLISTE_OPTION_SORTIERUNG_PERSONALNUMMER;
    }
    else if (wrbGeburtstag.isSelected()) {
      iOptionSortierung = PersonalFac.
          REPORT_PERSONALLISTE_OPTION_SORTIERUNG_GEBURTSTAG;
    }

    java.sql.Timestamp datum=wdfStichtag.getTimestamp();
    if(!wcbStichtag.isSelected()){
      datum=null;
    }


    if (wcbBarcodeliste.isSelected()) {
      return DelegateFactory.getInstance().getPersonalDelegate().printPersonalliste(
          datum, true,wcbMitVersteckten.isSelected(), iOptionSortierung);
    }
    else {
      return DelegateFactory.getInstance().getPersonalDelegate().printPersonalliste(
          datum, false,wcbMitVersteckten.isSelected(), iOptionSortierung);
    }
  }


  public boolean getBErstelleReportSofort() {
    return false;
  }


  public MailtextDto getMailtextDto()
      throws Throwable {
    MailtextDto mailtextDto = PanelReportKriterien.getDefaultMailtextDto(this);
    return mailtextDto;
  }
}
