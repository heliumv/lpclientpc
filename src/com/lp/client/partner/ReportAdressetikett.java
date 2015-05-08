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
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.EventObject;

import javax.swing.JComponent;

import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.report.PanelReportIfJRDS;
import com.lp.client.frame.report.PanelReportKriterien;
import com.lp.client.frame.report.ReportEtikett;
import com.lp.client.pc.LPMain;
import com.lp.server.partner.service.AnsprechpartnerDto;
import com.lp.server.partner.service.PartnerReportFac;
import com.lp.server.system.service.MailtextDto;
import com.lp.server.util.Facade;
import com.lp.server.util.report.JasperPrintLP;

@SuppressWarnings("static-access") 
public class ReportAdressetikett
    extends ReportEtikett implements PanelReportIfJRDS
{
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private WrapperButton wbuAnsprechpartner = new WrapperButton();
  private WrapperTextField wftAnsprechpartner = new WrapperTextField();
  private WrapperLabel wlaPartner = new WrapperLabel();
  private WrapperTextField wtfPartner = new WrapperTextField();
  private Integer partnerIId = null;
  private Integer ansprechpartnerIId = null;

  private PanelQueryFLR panelQueryFLRAnsprechpartner = null;
  static final public String ACTION_SPECIAL_ANSPRECHPARTNER_FROM_LISTE =
      "ACTION_SPECIAL_ANSPRECHPARTNER_FROM_LISTE";

  public ReportAdressetikett(InternalFrame frame,
                             com.lp.server.partner.service.PartnerDto partnerDto,
                             String add2Title)
      throws Throwable {
    super(frame, add2Title);
    jbInit();
    initComponents();
    if (partnerDto != null) {
      wtfPartner.setColumnsMax(Facade.MAX_UNBESCHRAENKT);
      wtfPartner.setText(partnerDto.formatFixTitelName1Name2());
      partnerIId = partnerDto.getIId();
    }
  }


  protected void eventActionSpecial(ActionEvent e)
      throws Throwable {
    if (e.getActionCommand().equals(ACTION_SPECIAL_ANSPRECHPARTNER_FROM_LISTE)) {
      dialogQueryAnsprechpartner(e);
    }
  }


  protected JComponent getFirstFocusableComponent()
      throws Exception {
    return wftAnsprechpartner;
  }


  protected void eventItemchanged(EventObject eI)
      throws Throwable {
    ItemChangedEvent e = (ItemChangedEvent) eI;
    if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {

      if (e.getSource() == panelQueryFLRAnsprechpartner) {
        Object key = ( (ISourceEvent) e.getSource()).getIdSelected();
        AnsprechpartnerDto ansprechpartnerDto = DelegateFactory.getInstance().
            getAnsprechpartnerDelegate().
            ansprechpartnerFindByPrimaryKey( (Integer)
                                            key);
        wftAnsprechpartner.setText(ansprechpartnerDto.getPartnerDto().
                                   formatFixTitelName1Name2());
        ansprechpartnerIId = ansprechpartnerDto.getIId();
      }
    }
    else if (e.getID() == ItemChangedEvent.ACTION_LEEREN) {
  if (e.getSource() == panelQueryFLRAnsprechpartner) {
    wftAnsprechpartner.setText(null);
    ansprechpartnerIId = null;
  }
}

  }


  private void jbInit()
      throws Exception {
    wbuAnsprechpartner.setText(LPMain.getInstance().getTextRespectUISPr(
        "button.ansprechpartner.long"));
    wlaPartner.setText(LPMain.getInstance().getTextRespectUISPr(
        "part.report.adressetikett.selektierterpartner") + ": ");
    wbuAnsprechpartner.setActionCommand(ACTION_SPECIAL_ANSPRECHPARTNER_FROM_LISTE);
    wbuAnsprechpartner.addActionListener(this);

    wtfPartner.setActivatable(false);
    wtfPartner.setMandatoryField(true);
    wtfPartner.setEditable(false);
    wtfPartner.setSaveReportInformation(false);

    wftAnsprechpartner.setEditable(false);
    wftAnsprechpartner.setActivatable(false);
    wftAnsprechpartner.setSaveReportInformation(false);
    
    getInternalFrame().addItemChangedListener(this);
    jpaWorkingOn.add(wlaPartner,
                     new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0,
                                            GridBagConstraints.CENTER,
                                            GridBagConstraints.HORIZONTAL,
                                            new Insets(2, 2, 2, 2), 0, 0));
    jpaWorkingOn.add(wtfPartner,
                     new GridBagConstraints(1, iZeile, 3, 1, 0.0, 0.0,
                                            GridBagConstraints.CENTER,
                                            GridBagConstraints.HORIZONTAL,
                                            new Insets(2, 2, 2, 2), 0, 0));
    iZeile++;
    jpaWorkingOn.add(wbuAnsprechpartner,
                     new GridBagConstraints(0, iZeile, 1, 1, 0.05, 0.0,
                                            GridBagConstraints.CENTER,
                                            GridBagConstraints.HORIZONTAL,
                                            new Insets(2, 2, 2, 2), 0, 0));
    jpaWorkingOn.add(wftAnsprechpartner,
                     new GridBagConstraints(1, iZeile, 3, 1, 0.1, 0.0,
                                            GridBagConstraints.CENTER,
                                            GridBagConstraints.HORIZONTAL,
                                            new Insets(2, 2, 2, 2), 0, 0));
  }


  /**
   * Dialogfenster zur Ansprechpartnerauswahl.
   *
   * @param e ActionEvent
   * @throws Throwable
   */
  private void dialogQueryAnsprechpartner(ActionEvent e)
      throws Throwable {
    panelQueryFLRAnsprechpartner = PartnerFilterFactory.getInstance().createPanelFLRAnsprechpartner(
      getInternalFrame(),
      partnerIId,
      ansprechpartnerIId,
      true,
      true);
    new DialogQuery(panelQueryFLRAnsprechpartner);
  }


  public String getModul() {
    return PartnerReportFac.REPORT_MODUL;
  }


  public String getReportname() {
    return PartnerReportFac.REPORT_PART_ADRESSETIKETT;
  }


  public JasperPrintLP getReport(String sDrucktype)
      throws Throwable {
    return DelegateFactory.getInstance().getKundeDelegate().printAdressetikett(partnerIId,
        ansprechpartnerIId);
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
