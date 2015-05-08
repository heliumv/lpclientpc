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
package com.lp.client.auftrag;


import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.EventObject;

import javax.swing.JPanel;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperSNRField;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.report.PanelReportIfJRDSZweiDrucker;
import com.lp.client.pc.LPButtonAction;
import com.lp.client.pc.LPMain;
import com.lp.server.auftrag.service.AuftragReportFac;
import com.lp.server.auftrag.service.AuftragseriennrnDto;
import com.lp.server.system.service.MailtextDto;
import com.lp.server.util.report.JasperPrintLP;


@SuppressWarnings("static-access") 
/**
 * <p>Report ReportAuftragSrnEtikett
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 * <p>Erstellungsdatum 28.11.07</p>
 * <p> </p>
 * @author Victor Finder
 * @version $Revision: 1.3 $
 */
public class ReportAuftragSrnEtikett
    extends PanelBasis implements PanelReportIfJRDSZweiDrucker
{

  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
protected JPanel jpaWorkingOn = new JPanel();
  private WrapperLabel wlaExemplare = null;
  private WrapperLabel wlaSeriennr = null;
  private WrapperLabel wlaSeriennrWarning = null;
  protected WrapperNumberField wnfExemplare=null;
  protected WrapperSNRField wtfSeriennr = null;
  private WrapperLabel wlaKommentar = null;
  private WrapperTextField wtfKommentar = null;
  private Integer iIdAuftrag = null;
  private Integer iIdArtikel = null;
  private Integer iIdAuftragpositionI = null;
  private Integer auftragseriennrnIId = null;
  private AuftragseriennrnDto auftragseriennrnDto = null;

  static final public String ACTION_SPECIAL_UPDATE =
       "ACTION_SPECIAL_UPDATE";
   static final public String ACTION_SPECIAL_SAVE =
       "ACTION_SPECIAL_SAVE";
   static final public String ACTION_SPECIAL_DISCARD =
      "ACTION_SPECIAL_DISCARD";

  public ReportAuftragSrnEtikett(InternalFrame internalFrame,
                          Integer iIdAuftragI,
                          Integer iIdAuftragpositionI,
                          Integer iIdArtikelI,
                          String sAdd2Title)
      throws Throwable {
    super(internalFrame, sAdd2Title);
    this.iIdAuftrag = iIdAuftragI;
    this.iIdArtikel = iIdArtikelI;
    this.iIdAuftragpositionI = iIdAuftragpositionI;
    jbInit();
    setDefaults();

  }


  private void jbInit()
      throws Throwable {

    String[] aWhichButtonIUse = {
            ACTION_UPDATE,
            ACTION_SAVE,
            ACTION_DISCARD
        };
    enableToolsPanelButtons(aWhichButtonIUse);
    jpaWorkingOn.setLayout(new GridBagLayout());
    getInternalFrame().addItemChangedListener(this);
    wlaExemplare = new WrapperLabel();
    wlaExemplare.setText(LPMain.getInstance().getTextRespectUISPr("report.exemplare"));
    wlaExemplare.setMinimumSize(new Dimension(100, Defaults.getInstance().getControlHeight()));
    wlaExemplare.setPreferredSize(new Dimension(100,
                                                Defaults.getInstance().getControlHeight()));
    wnfExemplare = new WrapperNumberField();
    wnfExemplare.setMinimumSize(new Dimension(30, Defaults.getInstance().getControlHeight()));
    wnfExemplare.setPreferredSize(new Dimension(30, Defaults.getInstance().getControlHeight()));
    wnfExemplare.setFractionDigits(0);
    wnfExemplare.setMaximumIntegerDigits(2);

    wlaSeriennr = new WrapperLabel();
    wlaSeriennr.setText(LPMain.getInstance().getTextRespectUISPr("artikel.report.seriennummern"));
    wlaSeriennr.setMinimumSize(new Dimension(100, Defaults.getInstance().getControlHeight()));
    wlaSeriennr.setPreferredSize(new Dimension(100,
                                                Defaults.getInstance().getControlHeight()));

    wlaSeriennrWarning = new WrapperLabel();
    wtfSeriennr = new WrapperSNRField();
    wtfSeriennr.setMinimumSize(new Dimension(200, Defaults.getInstance().getControlHeight()));
    wtfSeriennr.setPreferredSize(new Dimension(200, Defaults.getInstance().getControlHeight()));



    wlaKommentar = new WrapperLabel();
    wlaKommentar.setText(LPMain.getInstance().getTextRespectUISPr("lp.kommentar"));
    wtfKommentar = new WrapperTextField();


    this.setLayout(new GridBagLayout());


    jpaWorkingOn.add(wlaSeriennr,
                     new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0,
                                            GridBagConstraints.CENTER,
                                            GridBagConstraints.BOTH, new Insets(2, 2, 2, 2),
                                            0, 0));
    jpaWorkingOn.add(wtfSeriennr,
                     new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0,
                                            GridBagConstraints.CENTER,
                                            GridBagConstraints.BOTH, new Insets(2, 2, 2, 2),
                                            0, 0));

    jpaWorkingOn.add(wlaSeriennrWarning,
                     new GridBagConstraints(2, iZeile, 1, 1, 1.0, 0.0,
                                            GridBagConstraints.CENTER,
                                            GridBagConstraints.BOTH, new Insets(2, 2, 2, 2),
                                            0, 0));

    iZeile++;
    jpaWorkingOn.add(wlaKommentar,
                  new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0,
                                         GridBagConstraints.CENTER,
                                         GridBagConstraints.BOTH, new Insets(2, 2, 2, 2),
                                         0, 0));
    jpaWorkingOn.add(wtfKommentar,
                  new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0,
                                         GridBagConstraints.CENTER,
                                         GridBagConstraints.BOTH, new Insets(2, 2, 2, 2),
                                         0, 0));
    this.add(jpaWorkingOn,
               new GridBagConstraints(0, iZeile, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
                                      GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

    iZeile++;
       this.add(getToolsPanel(),
                new GridBagConstraints(0, iZeile, 1, 1, 1.0, 1.0, GridBagConstraints.NORTHWEST,
                                    GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
  }

  protected void components2Dto()
      throws Throwable {
    auftragseriennrnDto.setCSeriennr(wtfSeriennr.getText());
    auftragseriennrnDto.setCKommentar(wtfKommentar.getText());
    auftragseriennrnDto.setArtikelIId(iIdArtikel);
    auftragseriennrnDto.setAuftragpositionIId(iIdAuftragpositionI);
  }


  protected void dto2Components()
      throws Throwable {
    auftragseriennrnDto = DelegateFactory.getInstance().getAuftragpositionDelegate().
        auftragseriennrnFindByAuftragpositionIId(iIdAuftragpositionI);
     if(auftragseriennrnDto == null){
      auftragseriennrnDto = new AuftragseriennrnDto();
      auftragseriennrnDto.setAuftragpositionIId(iIdAuftragpositionI);
      auftragseriennrnDto.setArtikelIId(iIdArtikel);
      String srn = DelegateFactory.getInstance().getAuftragpositionDelegate().
            getNextSeriennr(iIdArtikel);
        if (srn != null) {
          wtfSeriennr.setText(srn);
          auftragseriennrnDto.setCSeriennr(srn);
        }
     }else{
      wtfSeriennr.setText(auftragseriennrnDto.getCSeriennr());
      wtfKommentar.setText(auftragseriennrnDto.getCKommentar());
     }
  }

  private void setDefaults()
      throws Throwable {
    leereAlleFelder(this);
    enableAllComponents(this, false);
  }

  public String getModul() {
    return AuftragReportFac.REPORT_MODUL;
  }


  public String getReportnameTop() {
    return AuftragReportFac.REPORT_AUFTRAG_SRN_ETIKETT_K;
  }

  public String getReportnameBottom() {
   return AuftragReportFac.REPORT_AUFTRAG_SRN_ETIKETT_G;
 }

  public JasperPrintLP getReportTop()
      throws Throwable {
      return DelegateFactory.getInstance().getAuftragReportDelegate().
          printAuftragSrnnrnEtikett(iIdAuftrag,
                          iIdAuftragpositionI,
                          getReportnameTop()
          );
  }

  public JasperPrintLP getReportBottom()
      throws Throwable {
      return DelegateFactory.getInstance().getAuftragReportDelegate().
          printAuftragSrnnrnEtikett(iIdAuftrag,
                          iIdAuftragpositionI,
                          getReportnameBottom()
          );
  }


  public boolean getBErstelleReportSofort() {
    return true;
  }



  public MailtextDto getMailtextDto() throws Throwable  {
    return null;
  }


  private void setEditable(Boolean flag){
    wtfSeriennr.setEditable(flag);
    wtfKommentar.setEditable(flag);
  }


  public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
      throws Throwable {
    super.eventYouAreSelected(false);
    dto2Components();
    LPButtonAction item = (LPButtonAction) getHmOfButtons().get(PanelBasis.ACTION_UPDATE);
    item.getButton().setEnabled(true);
    item.getButton().setActionCommand(ACTION_SPECIAL_UPDATE);


    item = (LPButtonAction) getHmOfButtons().get(PanelBasis.ACTION_SAVE);
    item.getButton().setEnabled(true);
    item.getButton().setActionCommand(ACTION_SPECIAL_SAVE);


    item = (LPButtonAction) getHmOfButtons().get(PanelBasis.ACTION_DISCARD);
    item.getButton().setEnabled(true);
    item.getButton().setActionCommand(ACTION_SPECIAL_DISCARD);
    wtfSeriennr.setEditable(false);
    wtfKommentar.setEditable(false);
  }


  protected void eventActionSpecial(ActionEvent e)
      throws Throwable {
    if (e.getActionCommand().equals(ACTION_SPECIAL_UPDATE)) {
      setEditable(true);
      LPButtonAction item = (LPButtonAction) getHmOfButtons().get(PanelBasis.
          ACTION_UPDATE);
      item.getButton().setEnabled(false);
   }
   else if (e.getActionCommand().equals(ACTION_SPECIAL_SAVE)) {
     components2Dto();
     if (auftragseriennrnDto.getIId() != null) {
       DelegateFactory.getInstance().getAuftragpositionDelegate().updateAuftragseriennrn(
           auftragseriennrnDto);
     }
     else {
       if (wtfSeriennr.getText() != null) {
         auftragseriennrnIId = DelegateFactory.getInstance().getAuftragpositionDelegate().
             createAuftragseriennrn(
                 auftragseriennrnDto);
         auftragseriennrnDto.setIId(auftragseriennrnIId);
       }
     }
     setEditable(false);
     LPButtonAction item = (LPButtonAction) getHmOfButtons().get(PanelBasis.
         ACTION_UPDATE);
     item.getButton().setEnabled(true);
   }
   else if (e.getActionCommand().equals(ACTION_SPECIAL_DISCARD)) {
     setEditable(false);
     dto2Components();
     LPButtonAction item = (LPButtonAction) getHmOfButtons().get(PanelBasis.
         ACTION_UPDATE);
     item.getButton().setEnabled(true);
   }

    }


  protected void eventItemchanged(EventObject eI)
      throws Throwable {
    ItemChangedEvent e = (ItemChangedEvent) eI;
    if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {

    }
  }


  public Integer getAnzahlExemplare()
      throws Throwable {
    return wnfExemplare.getInteger();
  }


  protected String getLockMeWer()
      throws Exception {
    return HelperClient.LOCKME_AUFTRAG;
  }


}
