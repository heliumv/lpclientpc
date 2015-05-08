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
package com.lp.client.zeiterfassung;


import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.WrapperDateField;
import com.lp.client.frame.component.WrapperDateRangeController;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.report.PanelReportIfJRDS;
import com.lp.client.frame.report.PanelReportKriterien;
import com.lp.client.pc.LPMain;
import com.lp.server.personal.service.ZeiterfassungReportFac;
import com.lp.server.system.service.MailtextDto;
import com.lp.server.util.report.JasperPrintLP;


public class ReportMaschinenproduktivitaet
    extends PanelBasis implements PanelReportIfJRDS
{
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
protected JPanel jpaWorkingOn = new JPanel();
  private GridBagLayout gridBagLayout2 = new GridBagLayout();
  private GridBagLayout gridBagLayout1 = new GridBagLayout();
  private WrapperLabel wlaZeitraum = new WrapperLabel();
  private WrapperLabel wlaVon = new WrapperLabel();
  private WrapperDateField wdfVon = new WrapperDateField();
  private WrapperLabel wlaBis = new WrapperLabel();
  private WrapperDateField wdfBis = new WrapperDateField();
  private WrapperLabel wlaMaschine = new WrapperLabel();
  private WrapperTextField wtfMaschine = new WrapperTextField();
  private Integer maschineIId = null;
  private JRadioButton wrbSekektiertemaschine = new JRadioButton();
  private ButtonGroup buttonGroupAuswertung = new ButtonGroup();
  private WrapperLabel wlaAuswertung = new WrapperLabel();
  private JRadioButton wrbAllemaschinen = new JRadioButton();


  private WrapperDateRangeController wdrBereich = null;
  public ReportMaschinenproduktivitaet(InternalFrameZeiterfassung internalFrame,
                                        String add2Title)
      throws Throwable {
    super(internalFrame, add2Title);
    LPMain.getInstance().getTextRespectUISPr(
        "zeiterfassung.report.produktivitaetsstatistik");

    jbInit();
    initComponents();
    if (internalFrame.getPersonalDto() != null) {
      wtfMaschine.setText(internalFrame.getMaschineDto().getBezeichnung());
      maschineIId = internalFrame.getMaschineDto().getIId();
    }
    wdrBereich.doClickDown();
    wdrBereich.doClickUp();

  }
  protected JComponent getFirstFocusableComponent()
      throws Exception {
    return wdfVon;
  }


  private void jbInit()
      throws Throwable {
    this.setLayout(gridBagLayout1);
    jpaWorkingOn.setLayout(gridBagLayout2);
    wlaZeitraum.setText(LPMain.getInstance().getTextRespectUISPr("lp.zeitraum") + ":");
    wlaVon.setText(LPMain.getInstance().getTextRespectUISPr("lp.von"));
    wlaBis.setText(LPMain.getInstance().getTextRespectUISPr("lp.bis"));
    wlaMaschine.setText("Selektierte Maschine: ");
    wdfVon.setMandatoryField(true);
    wdfBis.setMandatoryField(true);
    wtfMaschine.setEditable(false);
    wtfMaschine.setActivatable(false);
    wrbSekektiertemaschine.setSelected(true);
    wrbSekektiertemaschine.setText("Selektierte Maschine");
    wrbAllemaschinen.setText("Alle Maschinen");
    wdrBereich = new WrapperDateRangeController(wdfVon, wdfBis);

    this.add(jpaWorkingOn,
             new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.WEST,
                                    GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    buttonGroupAuswertung.add(wrbSekektiertemaschine);
    buttonGroupAuswertung.add(wrbAllemaschinen);
    jpaWorkingOn.add(wlaVon, new GridBagConstraints(0, 9, 1, 1, 0.1, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
        0, 0));
    jpaWorkingOn.add(wdfVon, new GridBagConstraints(1, 9, 1, 1, 0.1, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
        0, 0));
    jpaWorkingOn.add(wlaBis, new GridBagConstraints(2, 9, 1, 1, 0.1, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
        0, 0));
    jpaWorkingOn.add(wdfBis, new GridBagConstraints(3, 9, 1, 1, 0.1, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
        0, 0));
    jpaWorkingOn.add(wdrBereich,
                     new GridBagConstraints(4, 9, 1, 1, 0.0, 0.0,
                                            GridBagConstraints.WEST,
                                            GridBagConstraints.BOTH,
                                            new Insets(2, 2, 2, 2),
                                            0, 0));


    jpaWorkingOn.add(wlaZeitraum, new GridBagConstraints(0, 8, 1, 1, 0.0, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
        0, 0));
    jpaWorkingOn.add(wtfMaschine, new GridBagConstraints(1, 1, 3, 1, 0.0, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
        0, 0));
    jpaWorkingOn.add(wlaMaschine, new GridBagConstraints(0, 1, 1, 1, 0.1, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
        50, 0));
    jpaWorkingOn.add(wlaAuswertung, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
        0, 0));
    jpaWorkingOn.add(wrbSekektiertemaschine, new GridBagConstraints(1, 2, 3, 1, 0.0, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
        0, 0));
    jpaWorkingOn.add(wrbAllemaschinen, new GridBagConstraints(1, 4, 3, 1, 0.0, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
        0, 0));
  }


  public String getModul() {
    return ZeiterfassungReportFac.REPORT_MODUL;
  }


  public String getReportname() {
    return ZeiterfassungReportFac.REPORT_MASCHINENPRODUKTIVITAET;
  }


  public JasperPrintLP getReport(String sDrucktype)
      throws Throwable {
    JasperPrintLP jasperPrint = null;

    java.sql.Timestamp wdfBisTemp = new java.sql.Timestamp(wdfBis.getTimestamp().getTime() +
        24 * 3600000);

    if (wrbSekektiertemaschine.isSelected()) {
      jasperPrint = DelegateFactory.getInstance().getZeiterfassungReportDelegate().
          printMaschinenproduktivitaet(maschineIId, wdfVon.getTimestamp(),
                                        wdfBisTemp);
    }
    else if (wrbAllemaschinen.isSelected()) {
      jasperPrint = DelegateFactory.getInstance().getZeiterfassungReportDelegate().
          printMaschinenproduktivitaet(null, wdfVon.getTimestamp(),
                                        wdfBisTemp);    }
    return jasperPrint;

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
