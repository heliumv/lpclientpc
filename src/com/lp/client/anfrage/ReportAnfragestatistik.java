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
package com.lp.client.anfrage;


import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JComponent;
import javax.swing.JPanel;

import com.lp.client.artikel.InternalFrameArtikel;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.WrapperDateField;
import com.lp.client.frame.component.WrapperDateRangeController;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.report.PanelReportIfJRDS;
import com.lp.client.frame.report.PanelReportKriterien;
import com.lp.client.pc.LPMain;
import com.lp.server.anfrage.service.AnfrageReportFac;
import com.lp.server.anfrage.service.ReportAnfragestatistikKriterienDto;
import com.lp.server.system.service.MailtextDto;
import com.lp.server.util.report.JasperPrintLP;

@SuppressWarnings("static-access")
/**
 * <p>Report Anfragestatistik.
 * <br>Wird aus dem Artikel heraus aufgerufen.</p>
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 * <p>Erstellungsdatum 27.06.05</p>
 * <p> </p>
 * @author Uli Walch
 * @version $Revision: 1.5 $
 */
public class ReportAnfragestatistik
    extends PanelBasis implements PanelReportIfJRDS
{
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

protected JPanel jpaWorkingOn = new JPanel();

  private WrapperLabel wlaArtikel = null;
  private WrapperTextField wtfArtikel = null;

  private WrapperLabel wlaZeitraum = null;
  private WrapperLabel wlaVon = null;
  private WrapperDateField wdfVon = null;
  private WrapperLabel wlaBis = null;
  private WrapperDateField wdfBis = null;
  private WrapperDateRangeController wdrBereich = null;

  private Integer artikelIId = null;

  public ReportAnfragestatistik(InternalFrameArtikel internalFrame,
                                String add2Title)
      throws Throwable {
    super(internalFrame, add2Title);
    LPMain.getInstance().getTextRespectUISPr("anf.anfragestatistik");
    jbInit();
    initComponents();
    if (internalFrame.getArtikelDto() != null) {
      wtfArtikel.setText(internalFrame.getArtikelDto().getCNr());
      artikelIId = internalFrame.getArtikelDto().getIId();
    }
  }


  private void jbInit()
      throws Exception {
    setLayout(new GridBagLayout());

    jpaWorkingOn.setLayout(new GridBagLayout());
    add(jpaWorkingOn,
        new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.WEST,
                               GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

    wlaArtikel =
        new WrapperLabel(
          LPMain.getInstance().getTextRespectUISPr("artikel.report.artikelbestellt.selektierterartikel")
          + (String)": ");

    wtfArtikel = new WrapperTextField();
    wtfArtikel.setEditable(false);
    wtfArtikel.setMandatoryField(true);
    wlaZeitraum = new WrapperLabel(LPMain.getInstance().getTextRespectUISPr(
        "anf.zeitraum"));
    wlaVon = new WrapperLabel(LPMain.getInstance().getTextRespectUISPr("lp.von"));
    wdfVon = new WrapperDateField();
    wlaBis = new WrapperLabel(LPMain.getInstance().getTextRespectUISPr("lp.bis"));
    wdfBis = new WrapperDateField();

    wdrBereich = new WrapperDateRangeController(wdfVon, wdfBis);

    jpaWorkingOn.add(wlaZeitraum, new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
        0, 0));

    iZeile++;
    jpaWorkingOn.add(wlaVon,
                     new GridBagConstraints(0, iZeile, 1, 1, 0.1, 0.0,
                                            GridBagConstraints.CENTER,
                                            GridBagConstraints.HORIZONTAL,
                                            new Insets(2, 2, 2, 2), 0, 0));
    jpaWorkingOn.add(wdfVon, new GridBagConstraints(1, iZeile, 1, 1, 0.1, 0.0,
        GridBagConstraints.CENTER,
        GridBagConstraints.HORIZONTAL,
        new Insets(2, 2, 2, 2), 0, 0));
    jpaWorkingOn.add(wlaBis, new GridBagConstraints(2, iZeile, 1, 1, 0.1, 0.0,
        GridBagConstraints.CENTER,
        GridBagConstraints.HORIZONTAL,
        new Insets(2, 2, 2, 2), 0, 0));
    jpaWorkingOn.add(wdfBis, new GridBagConstraints(3, iZeile, 1, 1, 0.1, 0.0,
        GridBagConstraints.CENTER,
        GridBagConstraints.HORIZONTAL,
        new Insets(2, 2, 2, 2), 0, 0));
    jpaWorkingOn.add(wdrBereich, new GridBagConstraints(4, iZeile, 1, 1, 0.0, 0.0,
        GridBagConstraints.WEST,
        GridBagConstraints.BOTH,
        new Insets(2, 2, 2, 2),
        0, 0));

    iZeile++;
    jpaWorkingOn.add(wlaArtikel, new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
        0, 0));
    jpaWorkingOn.add(wtfArtikel, new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
        0, 0));
  }


  public String getModul() {
    return AnfrageReportFac.REPORT_MODUL;
  }


  public String getReportname() {
    return AnfrageReportFac.REPORT_ANFRAGESTATISTIK;
  }


  public JasperPrintLP getReport(String sDrucktype)
      throws Throwable {
    ReportAnfragestatistikKriterienDto kritDto =
        new ReportAnfragestatistikKriterienDto();

    kritDto.setArtikelIId(artikelIId);
    kritDto.setDVon(wdfVon.getDate());
    kritDto.setDBis(wdfBis.getDate());

    return DelegateFactory.getInstance().getAnfrageReportDelegate().
        printAnfragestatistik(kritDto);
  }


  protected JComponent getFirstFocusableComponent()
      throws Exception {
    return wdfVon;
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
