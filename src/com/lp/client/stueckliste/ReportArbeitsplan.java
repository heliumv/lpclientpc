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
package com.lp.client.stueckliste;


import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.EventObject;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.report.PanelReportIfJRDS;
import com.lp.client.frame.report.PanelReportKriterien;
import com.lp.client.pc.LPMain;
import com.lp.server.stueckliste.service.StuecklisteDto;
import com.lp.server.stueckliste.service.StuecklisteReportFac;
import com.lp.server.system.service.MailtextDto;
import com.lp.server.util.Facade;
import com.lp.server.util.report.JasperPrintLP;

@SuppressWarnings("static-access")
public class ReportArbeitsplan
    extends PanelBasis implements PanelReportIfJRDS
{
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
protected JPanel jpaWorkingOn = new JPanel();
  private GridBagLayout gridBagLayout2 = new GridBagLayout();
  private GridBagLayout gridBagLayout1 = new GridBagLayout();
  private InternalFrameStueckliste internalFrameStueckliste = null;
  private Integer stuecklisteIId = null;
  private JLabel wlaStueckliste = new JLabel();
  private WrapperTextField wtStueckliste = new WrapperTextField();
  private WrapperLabel wlaLosgroesse = new WrapperLabel();
  private WrapperNumberField wnfLosgroesse = new WrapperNumberField();
  private WrapperLabel wlaEinheit = new WrapperLabel();

  public ReportArbeitsplan(InternalFrameStueckliste internalFrame,
                           String add2Title, Integer stuecklisteIId)
      throws Throwable {
    super(internalFrame, add2Title);
    LPMain.getInstance().getTextRespectUISPr("stkl.stueckliste");
    internalFrameStueckliste = internalFrame;
    this.stuecklisteIId = stuecklisteIId;
    jbInit();
    initComponents();
    if (stuecklisteIId != null) {
      wtStueckliste.setColumnsMax(Facade.MAX_UNBESCHRAENKT);
      StuecklisteDto dto = DelegateFactory.getInstance().getStuecklisteDelegate().
          stuecklisteFindByPrimaryKey(
              stuecklisteIId);
      wtStueckliste.setText(dto.getArtikelDto().formatArtikelbezeichnung());
      wnfLosgroesse.setBigDecimal(dto.getNLosgroesse());
      wlaEinheit.setText(dto.getArtikelDto().getEinheitCNr().trim());
    }
  }

  protected JComponent getFirstFocusableComponent()
      throws Exception {
    return wnfLosgroesse;
  }

  private void jbInit()
      throws Exception {
    this.setLayout(gridBagLayout1);
    jpaWorkingOn.setLayout(gridBagLayout2);
    internalFrameStueckliste.addItemChangedListener(this);
    wlaStueckliste.setText("Selektierte St\u00FCckliste");
    wlaStueckliste.setHorizontalAlignment(SwingConstants.RIGHT);
    wlaStueckliste.setHorizontalTextPosition(SwingConstants.RIGHT);
    wtStueckliste.setEditable(false);
    wtStueckliste.setMandatoryField(true);
    wlaLosgroesse.setText(LPMain.getInstance().getTextRespectUISPr(
        "label.losgroesse"));

    wlaEinheit.setHorizontalAlignment(wlaEinheit.LEFT);
    wnfLosgroesse.setMandatoryField(true);
    this.add(jpaWorkingOn,
             new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.WEST,
                                    GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    jpaWorkingOn.add(wlaStueckliste, new GridBagConstraints(0, 0, 1, 1, 0.5, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
        0, 0));
    jpaWorkingOn.add(wtStueckliste, new GridBagConstraints(1, 0, 2, 1, 1, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
        0, 0));
    jpaWorkingOn.add(wlaLosgroesse, new GridBagConstraints(0, 1, 1, 1, 0, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
        0, 0));
    jpaWorkingOn.add(wnfLosgroesse, new GridBagConstraints(1, 1, 1, 1, 1, 0.0
        , GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
        -100, 0));
    jpaWorkingOn.add(wlaEinheit, new GridBagConstraints(2, 1, 1, 1, 1, 0.0
        , GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
        0, 0));
  }


  public String getModul() {
    return StuecklisteReportFac.REPORT_MODUL;
  }


  public String getReportname() {
    return StuecklisteReportFac.REPORT_STUECKLISTE_ARBEITSPLAN;
  }


  protected void eventItemchanged(EventObject eI)
      throws Throwable {
    ItemChangedEvent e = (ItemChangedEvent) eI;

  }


  protected void eventActionSpecial(ActionEvent e)
      throws Throwable {

  }


  public JasperPrintLP getReport(String sDrucktype)
      throws Throwable {
    return DelegateFactory.getInstance().getStuecklisteReportDelegate().printArbeitsplan(
        stuecklisteIId, wnfLosgroesse.getBigDecimal());
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
