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
package com.lp.client.artikel;


import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.Calendar;

import javax.swing.JComponent;
import javax.swing.JPanel;

import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperDateField;
import com.lp.client.frame.component.WrapperDateRangeController;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.report.PanelReportIfJRDS;
import com.lp.client.frame.report.PanelReportKriterien;
import com.lp.client.pc.LPMain;
import com.lp.server.artikel.service.ArtikelReportFac;
import com.lp.server.artikel.service.ArtikelbestelltFac;
import com.lp.server.system.service.MailtextDto;
import com.lp.server.util.Facade;
import com.lp.server.util.report.JasperPrintLP;
import com.lp.util.Helper;

@SuppressWarnings("static-access")
public class ReportVerwendungsnachweis
    extends PanelBasis implements PanelReportIfJRDS
{
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
protected JPanel jpaWorkingOn = new JPanel();
  private GridBagLayout gridBagLayout2 = new GridBagLayout();
  private GridBagLayout gridBagLayout1 = new GridBagLayout();
  WrapperLabel wlaArtikel = new WrapperLabel();
  WrapperTextField wtfArtikel = new WrapperTextField();
  private WrapperDateRangeController wdrBereich = null;
  
  private WrapperLabel wlaZeitraum = new WrapperLabel();
  private WrapperLabel wlaVon = new WrapperLabel();
  private WrapperDateField wdfVon = new WrapperDateField();
  private WrapperLabel wlaBis = new WrapperLabel();
  private WrapperDateField wdfBis = new WrapperDateField();
  private WrapperCheckBox wcbMitVerbrauchterMenge = new WrapperCheckBox();
  private WrapperCheckBox wcbVersteckte = null;
  
  private Integer artikelIId = null;
  public ReportVerwendungsnachweis(InternalFrameArtikel internalFrame, String add2Title)
      throws Throwable {
    super(internalFrame, add2Title);
    LPMain.getInstance().getTextRespectUISPr("artikel.verwendungsnachweis");
    jbInit();
    initComponents();

    if (internalFrame.getArtikelDto() != null) {
      wtfArtikel.setText(internalFrame.getArtikelDto().formatArtikelbezeichnung());
      artikelIId = internalFrame.getArtikelDto().getIId();
    }
  }


  protected JComponent getFirstFocusableComponent()
      throws Exception {
    return NO_VALUE_THATS_OK_JCOMPONENT;
  }


  private void jbInit()
      throws Exception {
    this.setLayout(gridBagLayout1);
    jpaWorkingOn.setLayout(gridBagLayout2);
    wlaArtikel.setText(LPMain.getInstance().getTextRespectUISPr(
        "artikel.report.artikelbestellt.selektierterartikel") + ": ");
    wtfArtikel.setActivatable(false);
    wtfArtikel.setEditable(false);
    wtfArtikel.setMandatoryField(true);
    wtfArtikel.setColumnsMax(Facade.MAX_UNBESCHRAENKT);
    
    wlaZeitraum.setText(LPMain.getInstance().getTextRespectUISPr("lp.zeitraum") + ":");
    wlaVon.setText(LPMain.getInstance().getTextRespectUISPr("lp.von"));
    wlaBis.setText(LPMain.getInstance().getTextRespectUISPr("lp.bis"));
    
    wcbVersteckte = new WrapperCheckBox(LPMain
			.getTextRespectUISPr("lp.versteckte"));
    
    Calendar c = Calendar.getInstance();
	c.set(Calendar.YEAR, c.get(Calendar.YEAR) - 1);
	c.set(Calendar.MONTH, Calendar.JANUARY);
	c.set(Calendar.DAY_OF_MONTH, 1);

	wdfVon.setTimestamp(Helper.cutTimestamp(new java.sql.Timestamp(c
			.getTimeInMillis())));
	wdfBis.setTimestamp(Helper.cutTimestamp(new java.sql.Timestamp(System
			.currentTimeMillis() + 24 * 3600000)));
    
    wcbMitVerbrauchterMenge.setText(LPMain.getInstance().getTextRespectUISPr(
    "artikel.verwendungsnachweis.mitverbrauchtermenge"));
    wcbMitVerbrauchterMenge.addActionListener(this);
    wdrBereich = new WrapperDateRangeController(wdfVon, wdfBis);
    wdfVon.setEnabled(false);
	  wdfVon.setMandatoryField(true);
	  wdfBis.setEnabled(false);
	  wdfBis.setMandatoryField(true);
    this.add(jpaWorkingOn,
             new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.WEST,
                                    GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    jpaWorkingOn.add(wtfArtikel, new GridBagConstraints(2, 2, 4, 1, 1.0, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
        0, 0));
    jpaWorkingOn.add(wlaArtikel, new GridBagConstraints(0, 2, 2, 1, 1.0, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
        0, 0));
    
    jpaWorkingOn.add(wcbMitVerbrauchterMenge, new GridBagConstraints(0, 3, 2, 1, 1.0, 0.0
            , GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
            0, 0));
    jpaWorkingOn.add(wcbVersteckte, new GridBagConstraints(4, 3, 2, 1, 1.0, 0.0
            , GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
            0, 0));
    
  
        jpaWorkingOn.add(wlaZeitraum, new GridBagConstraints(0, 4, 1, 1, 1.0, 0.0
            , GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
            0, 0));
        jpaWorkingOn.add(wlaVon, new GridBagConstraints(1, 4, 1, 1, 1.0, 0.0
                , GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
                0, 0));
        jpaWorkingOn.add(wdfVon, new GridBagConstraints(2, 4, 1, 1, 1.0, 0.0
                , GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
                0, 0));
        jpaWorkingOn.add(wlaBis, new GridBagConstraints(3, 4, 1, 1, 1.0, 0.0
                , GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
                0, 0));
            jpaWorkingOn.add(wdfBis, new GridBagConstraints(4, 4, 1, 1, 1.0, 0.0
                    , GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
                    0, 0));
            jpaWorkingOn.add(wdrBereich, new GridBagConstraints(5, 4, 1, 1, 1.0, 0.0
                    , GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2),
                    0, 0));
   
  }


  public String getModul() {
    return ArtikelbestelltFac.REPORT_MODUL;
  }


  public String getReportname() {
    return ArtikelReportFac.REPORT_VERWENDUNGSNACHWEIS;
  }


  protected void eventActionSpecial(ActionEvent e) throws Throwable {
	  
	  if(e.getSource().equals(wcbMitVerbrauchterMenge)){
		  
		  if(wcbMitVerbrauchterMenge.isSelected()){
			  wdfVon.setEnabled(true);
			  wdfBis.setEnabled(true);
		  } else {
			  wdfVon.setEnabled(false);
			  wdfBis.setEnabled(false);
		  }
	  }
	  
	  
  }
  
  public JasperPrintLP getReport(String sDrucktype)
      throws Throwable {
	  
	  if(wcbMitVerbrauchterMenge.isSelected()){
		  return DelegateFactory.getInstance().getArtikelReportDelegate().
	        printVerwendungsnachweis(
	            artikelIId,true,Helper.cutTimestamp(wdfVon.getTimestamp()),wdfBis.getTimestamp(), wcbVersteckte.isSelected());
	  } else {
		  return DelegateFactory.getInstance().getArtikelReportDelegate().
	        printVerwendungsnachweis(
	            artikelIId,false,null,null,wcbVersteckte.isSelected());
	  }
	  
   

  }


  public boolean getBErstelleReportSofort() {
    return true;
  }


  public MailtextDto getMailtextDto()
      throws Throwable {
    MailtextDto mailtextDto = PanelReportKriterien.getDefaultMailtextDto(this);
    return mailtextDto;
  }
}
