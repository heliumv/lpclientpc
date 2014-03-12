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
package com.lp.client.auftrag;


import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.util.Date;

import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperDateField;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.report.PanelReportIfJRDS;
import com.lp.client.frame.report.PanelReportJournalVerkauf;
import com.lp.client.frame.report.PanelReportKriterien;
import com.lp.client.pc.LPMain;
import com.lp.server.auftrag.service.AuftragReportFac;
import com.lp.server.system.service.MailtextDto;
import com.lp.server.system.service.ReportJournalKriterienDto;
import com.lp.server.util.report.JasperPrintLP;

@SuppressWarnings("static-access") 
public class ReportAuftragTeillieferbar
    extends PanelReportJournalVerkauf implements PanelReportIfJRDS
{

  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private WrapperLabel wlaStichtag = new WrapperLabel();
  private WrapperDateField wdfStichtag = new WrapperDateField();

  private WrapperCheckBox wcbInternenKommentarDrucken = new WrapperCheckBox();
  private WrapperCheckBox wcbMitDetails = new WrapperCheckBox();
  private WrapperCheckBox wcbSortiereNachLiefertermin = new WrapperCheckBox();

  public ReportAuftragTeillieferbar(InternalFrame internalFrame, String add2Title)
      throws Throwable {
    super(internalFrame, add2Title);
    jbInit();
    initComponents();
  }


  protected void jbInit()
      throws Exception {
    wlaStichtag.setText(LPMain.getInstance().getTextRespectUISPr("lp.stichtag"));
    wdfStichtag.setMandatoryField(true);
    wdfStichtag.setDate(new Date(System.currentTimeMillis()));
    wcbInternenKommentarDrucken.setText(LPMain.getInstance().getTextRespectUISPr(
        "auft.internenkommentarmitdrucken"));
    wcbMitDetails.setText(LPMain.getInstance().getTextRespectUISPr(
        "auft.offenemitdetaildrucken"));
    wcbSortiereNachLiefertermin.setText(LPMain.getInstance().getTextRespectUISPr(
            "auft.sortierungnachliefertermin"));
    jpaWorkingOn.add(wlaStichtag,
                     new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0,
                                            GridBagConstraints.WEST,
                                            GridBagConstraints.BOTH,
                                            new Insets(2, 2, 2, 2),
                                            0, 0));
    jpaWorkingOn.add(wdfStichtag,
                     new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0,
                                            GridBagConstraints.WEST,
                                            GridBagConstraints.BOTH,
                                            new Insets(2, 2, 2, 2),
                                            0, 0));
    iZeile++;
    jpaWorkingOn.add(wcbInternenKommentarDrucken,
                     new GridBagConstraints(0, iZeile, 6, 1, 0.0, 0.0,
                                            GridBagConstraints.WEST,
                                            GridBagConstraints.BOTH,
                                            new Insets(2, 2, 2, 2),
                                            0, 0));
    iZeile++;
    jpaWorkingOn.add(wcbMitDetails,
                     new GridBagConstraints(0, iZeile, 6, 1, 0.0, 0.0,
                                            GridBagConstraints.WEST,
                                            GridBagConstraints.BOTH,
                                            new Insets(2, 2, 2, 2),
                                            0, 0));
    iZeile++;
    jpaWorkingOn.add(wcbSortiereNachLiefertermin,
                     new GridBagConstraints(0, iZeile, 6, 1, 0.0, 0.0,
                                            GridBagConstraints.WEST,
                                            GridBagConstraints.BOTH,
                                            new Insets(2, 2, 2, 2),
                                            0, 0));


    this.setEinschraenkungDatumBelegnummerSichtbar(false);

  }


  public String getModul() {
    return AuftragReportFac.REPORT_MODUL;
  }


  public String getReportname() {
    return AuftragReportFac.REPORT_AUFTRAG_OFFENE;
  }


  public boolean getBErstelleReportSofort() {
    return false;
  }


  public MailtextDto getMailtextDto()
      throws Throwable {
    MailtextDto mailtextDto = PanelReportKriterien.getDefaultMailtextDto(this);
    return mailtextDto;
  }


  public JasperPrintLP getReport(String sDrucktype)
      throws Throwable {
    ReportJournalKriterienDto krit = new ReportJournalKriterienDto();
    befuelleKriterien(krit);
    if (wcbMitDetails.isSelected()) {
      return DelegateFactory.getInstance().getAuftragReportDelegate().printAuftragOffene(
          krit, wdfStichtag.getDate(),
          new Boolean(wcbInternenKommentarDrucken.isSelected()),1);
    }
    else {
      /*
             return DelegateFactory.getInstance().getAuftragReportDelegate().
          printAuftragOffeneOhneDetail(
              getKriterien(), wdfStichtag.getDate(), new Boolean(wcbSortiereNachLiefertermin.isSelected()), new Boolean(false));
       */
      return null;
    }

  }
}
