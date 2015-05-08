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
package com.lp.client.fertigung;


import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.EventObject;

import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.report.PanelReportIfJRDS;
import com.lp.client.frame.report.PanelReportJournalVerkauf;
import com.lp.client.frame.report.PanelReportKriterien;
import com.lp.client.pc.LPMain;
import com.lp.client.stueckliste.StuecklisteFilterFactory;
import com.lp.server.fertigung.service.FertigungReportFac;
import com.lp.server.stueckliste.service.FertigungsgruppeDto;
import com.lp.server.system.service.MailtextDto;
import com.lp.server.system.service.ReportJournalKriterienDto;
import com.lp.server.util.report.JasperPrintLP;

@SuppressWarnings("static-access") 
public class ReportAlleLose
    extends PanelReportJournalVerkauf implements PanelReportIfJRDS
{

  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
protected WrapperCheckBox wcbNichtAusgegebene = new WrapperCheckBox();
  private PanelQueryFLR panelQueryFLRFertigungsgruppe = null;
  static final public String ACTION_SPECIAL_FERTIGUNGSGRUPPE_FROM_LISTE =
      "ACTION_SPECIAL_FERTIGUNGSGRUPPE_FROM_LISTE";
  private WrapperTextField wtfFertigungsgruppe = new WrapperTextField();
  private WrapperButton wbuFertigungsgruppe = new WrapperButton();

  private Integer fertigungsgruppeIId = null;
  public ReportAlleLose(InternalFrame internalFrame,
                        String add2Title)
      throws Throwable {
    super(internalFrame, add2Title);

    wcbNichtAusgegebene.setText(LPMain.getTextRespectUISPr(
        "fert.los.allelose.nichtausgegebene"));

    wbuFertigungsgruppe.setText(LPMain.getInstance().getTextRespectUISPr(
        "stkl.fertigungsgruppe") + "...");
    wbuFertigungsgruppe.setActionCommand(this.
                                         ACTION_SPECIAL_FERTIGUNGSGRUPPE_FROM_LISTE);
    wbuFertigungsgruppe.addActionListener(this);
    wtfFertigungsgruppe.setActivatable(false);

    jpaWorkingOn.add(wcbNichtAusgegebene,
                     new GridBagConstraints(0, iZeile, 2, 1, 0.0, 0.0,
                                            GridBagConstraints.WEST,
                                            GridBagConstraints.BOTH,
                                            new Insets(2, 2, 2, 2),
                                            0, 0));

    jpaWorkingOn.add(wbuFertigungsgruppe,
                     new GridBagConstraints(2, iZeile, 1, 1, 0.0, 0.0,
                                            GridBagConstraints.WEST,
                                            GridBagConstraints.BOTH,
                                            new Insets(2, 2, 2, 2),
                                            150, 0));
    jpaWorkingOn.add(wtfFertigungsgruppe,
                      new GridBagConstraints(3, iZeile, 1, 1, 0.0, 0.0,
                                             GridBagConstraints.WEST,
                                             GridBagConstraints.BOTH,
                                             new Insets(2, 2, 2, 2),
                                             0, 0));

    //iZeile++;


  }


  public String getModul() {
    return FertigungReportFac.REPORT_MODUL;
  }


  public String getReportname() {
    return FertigungReportFac.REPORT_ALLE;
  }


  protected void eventActionSpecial(ActionEvent e)
      throws Throwable {
    super.eventActionSpecial(e);

    if (e.getActionCommand().equals(
        ACTION_SPECIAL_FERTIGUNGSGRUPPE_FROM_LISTE)) {
      dialogQueryFertigungsgruppeFromListe(e);
    }

  }


  protected void eventItemchanged(EventObject eI)
      throws Throwable {
    super.eventItemchanged(eI);
    ItemChangedEvent e = (ItemChangedEvent) eI;
    if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
      if (eI.getSource() == panelQueryFLRFertigungsgruppe) {
        Integer key = (Integer) ( (ISourceEvent) eI.getSource()).
            getIdSelected();
        FertigungsgruppeDto fertigungsgruppeDto = DelegateFactory.getInstance().
            getStuecklisteDelegate().
            fertigungsgruppeFindByPrimaryKey(key);
        fertigungsgruppeIId = fertigungsgruppeDto.getIId();
        wtfFertigungsgruppe.setText(fertigungsgruppeDto.getCBez());

      }

    }
     else if (e.getID() == ItemChangedEvent.ACTION_LEEREN) {
        if (eI.getSource() == panelQueryFLRFertigungsgruppe) {
          wtfFertigungsgruppe.setText(null);
          fertigungsgruppeIId=null;
        }
     }
  }


  public boolean getBErstelleReportSofort() {
    return false;
  }


  void dialogQueryFertigungsgruppeFromListe(ActionEvent e)
      throws Throwable {
    panelQueryFLRFertigungsgruppe = StuecklisteFilterFactory.getInstance().
        createPanelFLRFertigungsgruppe(
            getInternalFrame(), fertigungsgruppeIId, true);
    new DialogQuery(panelQueryFLRFertigungsgruppe);
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
    return DelegateFactory.getInstance().getFertigungDelegate().printAlle(krit,
        wcbNichtAusgegebene.isSelected(), fertigungsgruppeIId);
  }
}
