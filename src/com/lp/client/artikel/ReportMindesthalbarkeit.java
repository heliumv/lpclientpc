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
package com.lp.client.artikel;


import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;

import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperRadioButton;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.report.PanelReportIfJRDS;
import com.lp.client.frame.report.PanelReportKriterien;
import com.lp.client.pc.LPMain;
import com.lp.server.artikel.service.LagerReportFac;
import com.lp.server.system.service.MailtextDto;
import com.lp.server.util.report.JasperPrintLP;

@SuppressWarnings("static-access")
public class ReportMindesthalbarkeit
    extends PanelBasis implements PanelReportIfJRDS
{
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
protected JPanel jpaWorkingOn = new JPanel();
  private GridBagLayout gridBagLayout2 = new GridBagLayout();
  private GridBagLayout gridBagLayout1 = new GridBagLayout();

  private WrapperLabel wlaArtikelnrVon = new WrapperLabel();
  private WrapperLabel wlaArtikelnrBis = new WrapperLabel();
  private WrapperTextField wtfArtikelnrVon = new WrapperTextField();
  private WrapperTextField wtfArtikelnrBis = new WrapperTextField();

  private WrapperNumberField wnfMHAlter=new WrapperNumberField(0,9999);

  private WrapperLabel wlaSortierung = new WrapperLabel();
  private WrapperLabel wlaMHalter = new WrapperLabel();

  private ButtonGroup buttonGroupSortierung = new ButtonGroup();
  private WrapperRadioButton wrbSortierungIdent = new WrapperRadioButton();
  private WrapperRadioButton wrbChargennummer = new WrapperRadioButton();

  public ReportMindesthalbarkeit(InternalFrameArtikel internalFrame, String add2Title)
      throws Throwable {
    super(internalFrame, add2Title);
    LPMain.getInstance().getTextRespectUISPr("artikel.journal.mindesthaltbarkeit");
    jbInit();
    initComponents();

  }


  protected void eventActionSpecial(ActionEvent e)
      throws Throwable {
  }


  protected JComponent getFirstFocusableComponent()
      throws Exception {
    return wtfArtikelnrVon;
  }


  private void jbInit()
      throws Exception {
    this.setLayout(gridBagLayout1);
    jpaWorkingOn.setLayout(gridBagLayout2);
    wlaArtikelnrBis.setText(LPMain.getInstance().getTextRespectUISPr("lp.bis"));

    wlaSortierung.setText(LPMain.getInstance().getTextRespectUISPr("label.sortierung"));

    wrbSortierungIdent.setText(LPMain.getInstance().getTextRespectUISPr(
        "artikel.artikelnummer"));
    wrbSortierungIdent.setSelected(true);
    wrbChargennummer.setText(LPMain.getInstance().getTextRespectUISPr(
        "lp.chargennummer_lang"));
    wlaMHalter.setText(LPMain.getInstance().getTextRespectUISPr(
        "artikel.journal.mindesthaltbarkeit.mhalter"));

    wnfMHAlter.setFractionDigits(0);

    WrapperLabel  wlaInmonaten=new WrapperLabel(LPMain.getInstance().getTextRespectUISPr(
        "artikel.sonstiges.inmonaten"));
    wlaInmonaten.setHorizontalAlignment(SwingConstants.LEFT);

    wlaArtikelnrVon.setText(wrbSortierungIdent.getText() + " " +
                            LPMain.getInstance().getTextRespectUISPr("lp.von"));

    buttonGroupSortierung.add(wrbSortierungIdent);
    buttonGroupSortierung.add(wrbChargennummer);

    this.add(jpaWorkingOn,
             new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.WEST,
                                    GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));


    jpaWorkingOn.add(wlaArtikelnrVon, new GridBagConstraints(0, 0, 1, 1, 0.1, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
        0, 0));
    jpaWorkingOn.add(wtfArtikelnrVon, new GridBagConstraints(1, 0, 1, 1, 0.1, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
        0, 0));
    jpaWorkingOn.add(wlaArtikelnrBis, new GridBagConstraints(2, 0, 1, 1, 0.1, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
        0, 0));
    jpaWorkingOn.add(wtfArtikelnrBis, new GridBagConstraints(3, 0, 1, 1, 0.1, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
        0, 0));

    jpaWorkingOn.add(wlaMHalter, new GridBagConstraints(0, 1, 1, 1, 0, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
        0, 0));
    jpaWorkingOn.add(wnfMHAlter, new GridBagConstraints(1, 1, 1, 1, 0, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
        -50, 0));
    jpaWorkingOn.add(wlaInmonaten, new GridBagConstraints(2, 1, 1, 1, 0, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
        0, 0));




    jpaWorkingOn.add(wlaSortierung, new GridBagConstraints(0, 2, 1, 1, 0.1, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
        0, 0));
    jpaWorkingOn.add(wrbSortierungIdent, new GridBagConstraints(1, 2, 1, 1, 0.1, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
        0, 0));
    jpaWorkingOn.add(wrbChargennummer,
                     new GridBagConstraints(1, 3, 1, 1, 0.1, 0.0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.HORIZONTAL,
                                            new Insets(2, 2, 2, 2),
                                            0, 0));


  }


  public String getModul() {
    return LagerReportFac.REPORT_MODUL;
  }


  public String getReportname() {
    return LagerReportFac.REPORT_MINDESTHALTBARKEIT;
  }


  public JasperPrintLP getReport(String sDrucktype)
      throws Throwable {
    return DelegateFactory.getInstance().getLagerReportDelegate().
    printMindesthaltbarkeit(wtfArtikelnrVon.getText(),wtfArtikelnrBis.getText(),wrbChargennummer.isSelected(),wnfMHAlter.getInteger());
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
