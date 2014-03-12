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
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.Calendar;
import java.util.EventObject;

import javax.swing.JPanel;

import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperDateField;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.report.PanelReportIfJRDS;
import com.lp.client.frame.report.PanelReportKriterien;
import com.lp.client.pc.LPMain;
import com.lp.client.personal.PersonalFilterFactory;
import com.lp.client.system.SystemFilterFactory;
import com.lp.server.auftrag.service.AuftragReportFac;
import com.lp.server.personal.service.PersonalDto;
import com.lp.server.system.service.KostenstelleDto;
import com.lp.server.system.service.MailtextDto;
import com.lp.server.util.Facade;
import com.lp.server.util.report.JasperPrintLP;

@SuppressWarnings("static-access") 
/**
 * <p>Report Auftragnachkalkulation.
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 * <p>Erstellungsdatum 12.07.05</p>
 * <p> </p>
 * @author Uli Walch
 * @version $Revision: 1.5 $
 */
public class ReportErfuellungsgrad
    extends PanelBasis implements PanelReportIfJRDS
{
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private WrapperLabel wlaStichtag = new WrapperLabel();
  private WrapperDateField wtdArtikel = new WrapperDateField();
  private final static String ACTION_SPECIAL_VERTRETER_AUSWAHL =
      "action_special_vertreter_auswahl";
  static final public String ACTION_SPECIAL_KOSTENSTELLE_FROM_LISTE =
      "ACTION_SPECIAL_KOSTENSTELLE_FROM_LISTE";

  protected JPanel jpaWorkingOn = new JPanel();
  private GridBagLayout gridBagLayout2 = new GridBagLayout();
  private GridBagLayout gridBagLayout1 = new GridBagLayout();

  Integer vertreterIId = null;
  Integer kostenstelleIId = null;

  private WrapperButton wbuKostenstelle = new WrapperButton();
  private WrapperTextField wtfKostenstelle = new WrapperTextField();
  private WrapperButton wbuVertreter = null;
  private WrapperTextField wtfVertreter = null;
  private PanelQueryFLR panelQueryFLRVertreter = null;
  private PanelQueryFLR panelQueryFLRKostenstelle = null;
  private WrapperCheckBox wcoMitWiederholenden = null;

  public ReportErfuellungsgrad(InternalFrame internalFrame, String sAdd2Title)
      throws Throwable {
    super(internalFrame, sAdd2Title);
    jbInit();

    Calendar c = Calendar.getInstance();
    c.setTimeInMillis(System.currentTimeMillis());
    c.set(Calendar.DAY_OF_MONTH, 1);
    c.set(Calendar.DAY_OF_MONTH, c.get(Calendar.DAY_OF_MONTH) - 1);
    wtdArtikel.setDate(c.getTime());

  }


  public String getModul() {
    return AuftragReportFac.REPORT_MODUL;
  }


  protected void eventItemchanged(EventObject eI)
      throws Throwable {
    ItemChangedEvent e = (ItemChangedEvent) eI;

    if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
      if (e.getSource() == panelQueryFLRVertreter) {

        Object key = ( (ISourceEvent) e.getSource()).getIdSelected();

        if (key != null) {
          PersonalDto vertreterDto = DelegateFactory.getInstance().getPersonalDelegate().
              personalFindByPrimaryKey( (Integer) key);

          wtfVertreter.setText(vertreterDto.getPartnerDto().formatFixName2Name1());
          vertreterIId = vertreterDto.getIId();
        }
      }
      else if (e.getSource() == panelQueryFLRKostenstelle) {
        Object key = ( (ISourceEvent) e.getSource()).getIdSelected();
        KostenstelleDto kostenstelleDto = DelegateFactory.getInstance().getSystemDelegate().
            kostenstelleFindByPrimaryKey( (Integer) key);
        wtfKostenstelle.setText(kostenstelleDto.formatKostenstellenbezeichnung());
        kostenstelleIId = kostenstelleDto.getIId();
      }

    }
    else if (e.getID() == ItemChangedEvent.ACTION_LEEREN) {
      if (e.getSource() == panelQueryFLRVertreter) {
        vertreterIId = null;
        wtfVertreter.setText(null);
      }
      else if (e.getSource() == panelQueryFLRKostenstelle) {
        wtfKostenstelle.setText(null);
        kostenstelleIId = null;
      }

    }
  }


  private void jbInit()
      throws Throwable {
    this.setLayout(gridBagLayout1);
    jpaWorkingOn.setLayout(gridBagLayout2);
    getInternalFrame().addItemChangedListener(this);
    wbuVertreter = new WrapperButton(LPMain.getInstance().getTextRespectUISPr(
        "button.vertreter"));
    wbuVertreter.setActionCommand(ACTION_SPECIAL_VERTRETER_AUSWAHL);
    wbuVertreter.addActionListener(this);

    wtfVertreter = new WrapperTextField();
    wtfVertreter.setEditable(false);

    wbuKostenstelle.setText(LPMain.getInstance().getTextRespectUISPr(
        "button.kostenstelle"));

    wtfKostenstelle.setColumnsMax(Facade.MAX_UNBESCHRAENKT);
    wtfKostenstelle.setEditable(false);

    wbuKostenstelle.setActionCommand(this.
                                     ACTION_SPECIAL_KOSTENSTELLE_FROM_LISTE);
    wbuKostenstelle.addActionListener(this);

    wcoMitWiederholenden=new WrapperCheckBox(LPMain.getInstance().getTextRespectUISPr(
    "auft.report.erfuellungsgrad.mitwiederholenden"));
    
    wlaStichtag.setText(LPMain.getInstance().getTextRespectUISPr("lp.stichtag"));
    wtdArtikel.setMandatoryField(true);
    this.add(jpaWorkingOn,
             new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.WEST,
                                    GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    jpaWorkingOn.add(wlaStichtag,
                     new GridBagConstraints(0, 0, 1, 1, 0.2, 0.0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.HORIZONTAL,
                                            new Insets(2, 2, 2, 2),
                                            0, 0));
    jpaWorkingOn.add(wtdArtikel,
                     new GridBagConstraints(1, 0, 1, 1, 0.8, 0.0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.HORIZONTAL,
                                            new Insets(2, 2, 2, 2),
                                            0, 0));
    jpaWorkingOn.add(wbuVertreter,
                     new GridBagConstraints(0, 1, 1, 1, 0, 0.0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.HORIZONTAL,
                                            new Insets(2, 2, 2, 2),
                                            0, 0));
    jpaWorkingOn.add(wtfVertreter,
                     new GridBagConstraints(1, 1, 1, 1, 0, 0.0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.HORIZONTAL,
                                            new Insets(2, 2, 2, 2),
                                            0, 0));
    jpaWorkingOn.add(wbuKostenstelle,
                     new GridBagConstraints(0, 2, 1, 1, 0, 0.0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.HORIZONTAL,
                                            new Insets(2, 2, 2, 2),
                                            0, 0));
    jpaWorkingOn.add(wtfKostenstelle,
                     new GridBagConstraints(1, 2, 1, 1, 0, 0.0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.HORIZONTAL,
                                            new Insets(2, 2, 2, 2),
                                            0, 0));
    jpaWorkingOn.add(wcoMitWiederholenden,
            new GridBagConstraints(0, 3, 1, 1, 0, 0.0
                                   , GridBagConstraints.CENTER,
                                   GridBagConstraints.HORIZONTAL,
                                   new Insets(2, 2, 2, 2),
                                   0, 0));
    
    
    
    

  }


  protected void eventActionSpecial(ActionEvent e)
      throws Throwable {
    if (e.getActionCommand().equals(ACTION_SPECIAL_VERTRETER_AUSWAHL)) {
      dialogQueryVertreter(e);
    }
    else if (e.getActionCommand().equals(
        ACTION_SPECIAL_KOSTENSTELLE_FROM_LISTE)) {
      dialogQueryKostenstelleFromListe(e);
    }

  }


  public String getReportname() {
    return AuftragReportFac.REPORT_AUFTRAG_ERFUELLUNGSGRAD;
  }


  public JasperPrintLP getReport(String sDrucktype)
      throws Throwable {
    return DelegateFactory.getInstance().getAuftragReportDelegate().printErfuellungsgrad(
        wtdArtikel.getTimestamp(), vertreterIId, kostenstelleIId, wcoMitWiederholenden.isSelected());
  }


  private void dialogQueryVertreter(ActionEvent e)
      throws Throwable {
    panelQueryFLRVertreter = PersonalFilterFactory.getInstance().createPanelFLRPersonal(
        getInternalFrame(),
        true,
        true,
        vertreterIId);

    new DialogQuery(panelQueryFLRVertreter);
  }


  void dialogQueryKostenstelleFromListe(ActionEvent e)
      throws Throwable {
    panelQueryFLRKostenstelle = SystemFilterFactory.getInstance().
        createPanelFLRKostenstelle(getInternalFrame(), false, true,
                                   kostenstelleIId);
    new DialogQuery(panelQueryFLRKostenstelle);
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
