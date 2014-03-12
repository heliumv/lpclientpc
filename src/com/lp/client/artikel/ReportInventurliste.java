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
import java.util.EventObject;

import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperRadioButton;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.report.PanelReportIfJRDS;
import com.lp.client.frame.report.PanelReportKriterien;
import com.lp.client.pc.LPMain;
import com.lp.server.artikel.service.InventurFac;
import com.lp.server.artikel.service.LagerDto;
import com.lp.server.system.service.MailtextDto;
import com.lp.server.util.report.JasperPrintLP;

@SuppressWarnings("static-access")
public class ReportInventurliste
    extends PanelBasis implements PanelReportIfJRDS
{
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
protected JPanel jpaWorkingOn = new JPanel();
  private GridBagLayout gridBagLayout2 = new GridBagLayout();
  private GridBagLayout gridBagLayout1 = new GridBagLayout();
  private WrapperButton wbuLager = new WrapperButton();
  private WrapperTextField wtfLager = new WrapperTextField();
  private Integer lagerIId = null;
  private Integer inventurIId = null;
  private PanelQueryFLR panelQueryFLRLager = null;
  static final public String ACTION_SPECIAL_LAGER_FROM_LISTE =
      "action_lager_from_liste";
  private JLabel wlaInventur = new JLabel();
  private WrapperTextField wtfInventur = new WrapperTextField();

  private ButtonGroup buttonGroupOption = new ButtonGroup();
   private WrapperRadioButton wrbOptionInventurpreis = new WrapperRadioButton();
  private WrapperRadioButton wrbOptionGestehungspreis = new WrapperRadioButton();

  public ReportInventurliste(InternalFrameArtikel internalFrame,
                             String add2Title, Integer inventurIId)
      throws Throwable {
    super(internalFrame, add2Title);
    LPMain.getInstance().getTextRespectUISPr("artikel.report.inventurliste");
    this.inventurIId = inventurIId;
    jbInit();
    initComponents();
    if (inventurIId != null) {
      wtfInventur.setText(DelegateFactory.getInstance().getInventurDelegate().inventurFindByPrimaryKey(
          inventurIId).getCBez());
    }
  }

  protected JComponent getFirstFocusableComponent()
      throws Exception {
    return wbuLager;
  }

  private void jbInit()
      throws Exception {
    this.setLayout(gridBagLayout1);
    jpaWorkingOn.setLayout(gridBagLayout2);
    wbuLager.setText(LPMain.getInstance().getTextRespectUISPr("button.lager"));
    wtfLager.setEditable(false);
    // wbuLager.setEnabled(false);
    wbuLager.setActionCommand(this.
                              ACTION_SPECIAL_LAGER_FROM_LISTE);
    wbuLager.addActionListener(this);
    getInternalFrame().addItemChangedListener(this);
    wlaInventur.setText(LPMain.getInstance().getTextRespectUISPr(
        "artikel.inventur.selektierteinventur"));
    wtfInventur.setEditable(false);
    wtfInventur.setMandatoryField(true);


    wrbOptionInventurpreis.setText(LPMain.getInstance().getTextRespectUISPr(
        "artikel.inventurpreis"));
    wrbOptionGestehungspreis.setText(LPMain.getInstance().getTextRespectUISPr(
        "lp.gestehungspreis"));

    buttonGroupOption.add(wrbOptionInventurpreis);
    buttonGroupOption.add(wrbOptionGestehungspreis);
    wrbOptionInventurpreis.setSelected(true);

    this.add(jpaWorkingOn,
             new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.WEST,
                                    GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    jpaWorkingOn.add(wlaInventur, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
        0, 0));
    jpaWorkingOn.add(wtfInventur, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
        0, 0));

    jpaWorkingOn.add(wbuLager, new GridBagConstraints(0, 1, 1, 1, 0.1, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
        0, 0));
    jpaWorkingOn.add(wtfLager, new GridBagConstraints(1, 1, 1, 1, 0.2, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
        0, 0));

    jpaWorkingOn.add(wrbOptionInventurpreis, new GridBagConstraints(1, 2, 1, 1, 0, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
        0, 0));
    jpaWorkingOn.add(wrbOptionGestehungspreis, new GridBagConstraints(1, 3, 1, 1, 0, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
        0, 0));


  }


  public String getModul() {
    return InventurFac.REPORT_MODUL;
  }


  public String getReportname() {
    return InventurFac.REPORT_INVENTURLISTE;
  }


  void dialogQueryLagerFromListe(ActionEvent e)
      throws Throwable {
    panelQueryFLRLager = ArtikelFilterFactory.getInstance().
        createPanelFLRLager_MitLeerenButton(
            getInternalFrame());

    new DialogQuery(panelQueryFLRLager);
  }


  protected void eventItemchanged(EventObject eI)
      throws Throwable {
    ItemChangedEvent e = (ItemChangedEvent) eI;
    if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {

      if (e.getSource() == panelQueryFLRLager) {
        Object key = ( (ISourceEvent) e.getSource()).getIdSelected();
        LagerDto lagerDto = DelegateFactory.getInstance().getLagerDelegate().
            lagerFindByPrimaryKey( (Integer)
                                  key);
        wtfLager.setText(lagerDto.getCNr());
        lagerIId = lagerDto.getIId();
        wtfLager.setText(lagerDto.getCNr());
      }
    }
    else if (e.getID() == ItemChangedEvent.ACTION_LEEREN) {
      if (e.getSource() == panelQueryFLRLager) {
        wtfLager.setText(null);
        lagerIId = null;
      }
    }

  }


  protected void eventActionSpecial(ActionEvent e)
      throws Throwable {
    if (e.getActionCommand().equals(ACTION_SPECIAL_LAGER_FROM_LISTE)) {
      dialogQueryLagerFromListe(e);
    }
  }


  public JasperPrintLP getReport(String sDrucktype)
      throws Throwable {
    return DelegateFactory.getInstance().getInventurDelegate().printInventurliste(inventurIId,
        lagerIId,wrbOptionInventurpreis.isSelected());
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
