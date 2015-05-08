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
package com.lp.client.partner;


import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Calendar;
import java.util.Date;

import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JPanel;

import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperDateField;
import com.lp.client.frame.component.WrapperDateRangeController;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperRadioButton;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.report.PanelReportIfJRDS;
import com.lp.client.frame.report.PanelReportKriterien;
import com.lp.client.pc.LPMain;
import com.lp.server.partner.service.LieferantReportFac;
import com.lp.server.partner.service.StatistikParamDto;
import com.lp.server.system.service.MailtextDto;
import com.lp.server.util.report.JasperPrintLP;
import com.lp.util.Helper;

@SuppressWarnings("static-access") 
/**
 * <p> Diese Klasse kuemmert sich ...</p>
 *
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 *
 * <p>Erstellung: Josef Ornetsmueller; 08.08.05</p>
 *
 * <p>@author $Author: christian $</p>
 *
 * @version not attributable Date $Date: 2009/12/01 12:23:16 $
 */
public class ReportLieferantenstatistik
    extends PanelBasis implements PanelReportIfJRDS
{
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private WrapperLabel wlaSortierung = new WrapperLabel();
  private WrapperLabel wlaVon = new WrapperLabel();
  private WrapperDateField wdfVon = new WrapperDateField();
  private WrapperLabel wlaBis = new WrapperLabel();
  private WrapperDateField wdfBis = new WrapperDateField();
  private GridBagLayout gridBagLayout = new GridBagLayout();
  private GridBagLayout gridBagLayoutWO = new GridBagLayout();
  protected JPanel jpaWorkingOn = new JPanel();
  private WrapperDateRangeController wdrBereich = null;
  private WrapperCheckBox wcbVerdichtet = new WrapperCheckBox();
  private WrapperRadioButton wrbDatum = null;
  private WrapperRadioButton wrbBelegnr = null;
  private WrapperRadioButton wrbArtikel = null;
  private ButtonGroup jbgSortierung = null;
  private StatistikParamDto statistikParamDto = new StatistikParamDto();

	private WrapperCheckBox wcbEingeschraenkt = new WrapperCheckBox();
  
  public ReportLieferantenstatistik(InternalFrameLieferant internalFrameLieferantI,
                                    String add2TitleI)
      throws Throwable {

    super(internalFrameLieferantI, add2TitleI);
    LPMain.getInstance().getTextRespectUISPr("part.lf.statistik");
    jbInit();
    initComponents();
  }


  /**
   * jbInit
   */
  private void jbInit() {

    this.setLayout(gridBagLayout);
    jpaWorkingOn.setLayout(gridBagLayoutWO);
    this.add(jpaWorkingOn,
             new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.WEST,
                                    GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

    wlaVon.setText(LPMain.getInstance().getTextRespectUISPr("lp.von"));
  //  wdfVon.setMandatoryField(true);
    wdfVon.setDate(new Date(System.currentTimeMillis()));
    wcbVerdichtet.setText(LPMain.getInstance().getTextRespectUISPr("lp.verdichtetartikelnummer"));
    wlaBis.setText(LPMain.getInstance().getTextRespectUISPr("lp.bis"));
 //   wdfBis.setMandatoryField(true);

    wdrBereich = new WrapperDateRangeController(wdfVon, wdfBis);
    Calendar c = Calendar.getInstance();
	c.set(Calendar.YEAR, c.get(Calendar.YEAR) - 1);
	c.set(Calendar.MONTH, Calendar.JANUARY);
	c.set(Calendar.DAY_OF_MONTH, 1);

	wdfVon.setTimestamp(Helper.cutTimestamp(new java.sql.Timestamp(c
			.getTimeInMillis())));
	wdfBis.setTimestamp(Helper.cutTimestamp(new java.sql.Timestamp(System
			.currentTimeMillis() + 24 * 3600000)));
    wlaSortierung.setText(LPMain.getInstance().getTextRespectUISPr("lp.sortierung"));
    jbgSortierung = new ButtonGroup();

    wrbDatum = new WrapperRadioButton();
    wrbDatum.setText(LPMain.getInstance().getTextRespectUISPr("label.belegdatum"));
    jbgSortierung.add(wrbDatum);

    wrbBelegnr = new WrapperRadioButton();
    wrbBelegnr.setText(LPMain.getInstance().getTextRespectUISPr("lp.belegnr"));

    wrbArtikel = new WrapperRadioButton();
    wrbArtikel.setText(LPMain.getInstance().getTextRespectUISPr("artikel.artikelnummer"));
    wcbEingeschraenkt.setText(LPMain.getInstance().getTextRespectUISPr(
	"lp.eingeschraenkt"));
	wcbEingeschraenkt.setSelected(true);



    jbgSortierung.add(wrbBelegnr);
    jbgSortierung.add(wrbArtikel);
    wrbDatum.setSelected(true);

    // einhaengen
    jpaWorkingOn.add(new WrapperLabel(),
                     new GridBagConstraints(0, iZeile, 1, 1, 0.1, 0.0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.HORIZONTAL,
                                            new Insets(2, 2, 2, 2),
                                            0, 0));
    iZeile++;
    jpaWorkingOn.add(wlaVon,
                     new GridBagConstraints(0, iZeile, 1, 1, 0.1, 0.0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.HORIZONTAL,
                                            new Insets(2, 2, 2, 2),
                                            0, 0));
    jpaWorkingOn.add(wdfVon,
                     new GridBagConstraints(1, iZeile, 1, 1, 0.2, 0.0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.HORIZONTAL,
                                            new Insets(2, 2, 2, 2),
                                            0, 0));
    jpaWorkingOn.add(wlaBis,
                     new GridBagConstraints(2, iZeile, 1, 1, 0.1, 0.0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.HORIZONTAL,
                                            new Insets(2, 2, 2, 2),
                                            0, 0));
    jpaWorkingOn.add(wdfBis,
                     new GridBagConstraints(3, iZeile, 1, 1, 0.2, 0.0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.HORIZONTAL,
                                            new Insets(2, 2, 2, 2),
                                            0, 0));
    jpaWorkingOn.add(wdrBereich,
                     new GridBagConstraints(4, iZeile, 1, 1, 0, 0.0,
                                            GridBagConstraints.WEST,
                                            GridBagConstraints.BOTH,
                                            new Insets(2, 2, 2, 2),
                                            0, 0));

    iZeile++;
    jpaWorkingOn.add(wlaSortierung,
                     new GridBagConstraints(0, iZeile, 1, 1, 0, 0.0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.HORIZONTAL,
                                            new Insets(2, 2, 2, 2),
                                            0, 0));
    jpaWorkingOn.add(wrbDatum,
                     new GridBagConstraints(1, iZeile, 2, 1, 0, 0.0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.BOTH,
                                            new Insets(2, 2, 2, 2), 0, 0));
    jpaWorkingOn.add(wcbEingeschraenkt,
            new GridBagConstraints(3, iZeile, 2, 1, 0, 0.0
                                   , GridBagConstraints.WEST,
                                   GridBagConstraints.BOTH,
                                   new Insets(2, 2, 2, 2), 0, 0));
    iZeile++;
    jpaWorkingOn.add(wrbBelegnr,
                     new GridBagConstraints(1, iZeile, 2, 1, 0, 0.0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.BOTH,
                                            new Insets(2, 2, 2, 2), 0, 0));
    iZeile++;
    jpaWorkingOn.add(wrbArtikel,
                     new GridBagConstraints(1, iZeile, 2, 1, 0, 0.0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.BOTH,
                                            new Insets(2, 2, 2, 2), 0, 0));
    jpaWorkingOn.add(wcbVerdichtet,
                     new GridBagConstraints(3, iZeile, 2, 1, 0, 0.0
                                            , GridBagConstraints.WEST,
                                            GridBagConstraints.BOTH,
                                            new Insets(2, 2, 2, 2), 0, 0));
  }


  public String getModul() {
    return LieferantReportFac.REPORT_MODUL;
  }


  public String getReportname() {
    return LieferantReportFac.REPORT_LIEFERANT_LIEFERSTATISTIK;
  }


  public boolean getBErstelleReportSofort() {
    return false;
  }


  public JasperPrintLP getReport(String sDrucktype)
      throws Throwable {
    statistikParamDto.setDDatumVon(wdfVon.getDate());
    statistikParamDto.setDDatumBis(wdfBis.getDate());
    statistikParamDto.setId(
        ( (InternalFrameLieferant) getInternalFrame()).getLieferantDto().getIId());

    statistikParamDto.setISortierung(new Integer(Helper.SORTIERUNG_ABSTEIGEND));

    if (wrbBelegnr.isSelected()) {
      statistikParamDto.setISortierungNachWas(new Integer(Helper.SORTIERUNG_NACH_BELEGNR));
    }
    else if (wrbArtikel.isSelected()) {
      statistikParamDto.setISortierungNachWas(new Integer(Helper.SORTIERUNG_NACH_IDENT));
    }
    else {
      statistikParamDto.setISortierungNachWas(new Integer(Helper.SORTIERUNG_NACH_DATUM));
    }

    return DelegateFactory.getInstance().getLieferantDelegate().printLieferantenstatistik(
        statistikParamDto,wcbVerdichtet.isSelected(),wcbEingeschraenkt.isSelected());
  }


  public MailtextDto getMailtextDto() throws Throwable  {
    MailtextDto mailtextDto=PanelReportKriterien.getDefaultMailtextDto(this);
    return mailtextDto;
  }


  protected JComponent getFirstFocusableComponent()
      throws Exception {
    return wdfVon;
  }

}
