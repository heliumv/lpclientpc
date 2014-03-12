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
package com.lp.client.system;


import java.awt.event.ActionEvent;

import javax.swing.JMenuBar;
import javax.swing.event.ChangeEvent;

import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQuery;
import com.lp.client.frame.component.PanelSplit;
import com.lp.client.frame.component.TabbedPane;
import com.lp.client.frame.component.WrapperMenuBar;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.util.fastlanereader.gui.QueryType;
import com.lp.server.system.service.AutomatikjobDto;
import com.lp.server.system.service.AutomatikjobFac;
import com.lp.server.system.service.AutomatikjobtypeDto;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;


@SuppressWarnings("static-access")
public class TabbedPaneAutomatik
    extends TabbedPane
{

  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private PanelQuery panelQueryUebersicht = null;
  private PanelSplit panelSplitUebersicht = null;
  private PanelAutomatik panelAutomatikUebersicht = null;

  private PanelAutomatikDetails panelAutomatikDetails = null;

  private final static int IDX_UEBERSICHT = 0;
  private final static int IDX_DETAIL = 1;


  public TabbedPaneAutomatik(InternalFrame internalFrameI, String addTitleI)
      throws Throwable {
    super(internalFrameI, addTitleI);
    jbInit();
    initComponents();
  }

  public TabbedPaneAutomatik(InternalFrame internalFrameI)
      throws Throwable {
    super(internalFrameI,
          LPMain.getInstance().getTextRespectUISPr("system.automatik"));
    jbInit();
    initComponents();
  }

  private void jbInit()
      throws Throwable {
    // Tab 1: Uebersicht
    insertTab(LPMain.getTextRespectUISPr("lp.system.automatik.tab.oben.uebersicht.title"),
              null,
              null,
              LPMain.getTextRespectUISPr("lp.system.automatik.tab.oben.uebersicht.tooltip"),
              IDX_UEBERSICHT);
  // Tab 2: Detail
    insertTab(LPMain.getTextRespectUISPr("lp.system.automatik.tab.oben.detail.title"),
              null,
              null,
              LPMain.getTextRespectUISPr("lp.system.automatik.tab.oben.detail.tooltip"),
              IDX_DETAIL);

    setSelectedComponent(getPanelSplitUebersicht(true));
   

    ItemChangedEvent it = new ItemChangedEvent(getPanelSplitUebersicht(true),
                                               ItemChangedEvent.ITEM_CHANGED);
    lPEventItemChanged(it);

    // Listener
    addChangeListener(this);
    getInternalFrame().addItemChangedListener(this);
  }


  private PanelSplit getPanelSplitUebersicht(boolean bNeedInstantiationIfNull)
      throws Throwable {
    if (panelSplitUebersicht == null && bNeedInstantiationIfNull) {
      getPanelQueryUebersicht(true);
      getPanelAutomatikUebersicht(true);
      panelSplitUebersicht = new PanelSplit(getInternalFrame(),
                                             panelAutomatikUebersicht,
                                            panelQueryUebersicht, 400);
      this.setComponentAt(IDX_UEBERSICHT, panelSplitUebersicht);
    }
    return panelSplitUebersicht;
  }



  private PanelAutomatikDetails getPanelAutomatikDetails(boolean bNeedInstatiationIfNull,String sPropertyType)
    throws Throwable {
  //if (panelAutomatikDetails == null && bNeedInstatiationIfNull) {
    panelAutomatikDetails = new PanelAutomatikDetails(getInternalFrame(),
        LPMain.getTextRespectUISPr("lp.system.automatik.tab.oben.detail.title"),panelQueryUebersicht.getSelectedId(),this,sPropertyType);
    this.setComponentAt(IDX_DETAIL, panelAutomatikDetails);
  //}
  return panelAutomatikDetails;
}

private FilterKriterium[] buildDefaultFilterAutomatikjobs()
      throws Throwable {
  FilterKriterium[] kriterien = new FilterKriterium[1];
  FilterKriterium krit1 = new FilterKriterium(AutomatikjobFac.FLR_AUTOMATIKJOB_MANDANT_C_NR,
                                              true,
                                              "'" +
                                              LPMain.getInstance().getTheClient().getMandant() +
                                              "'",
                                              FilterKriterium.OPERATOR_EQUAL,true);
  kriterien[0] = krit1;
  return kriterien;
}


  private PanelQuery getPanelQueryUebersicht(boolean bNeedInstantiationIfNull)
      throws Throwable {
    if (panelQueryUebersicht == null && bNeedInstantiationIfNull) {
      String[] aWhichStandardButtonIUse = new String[] {
          PanelBasis.ACTION_POSITION_VONNNACHNMINUS1,
          PanelBasis.ACTION_POSITION_VONNNACHNPLUS1
      };

      QueryType[] querytypes = null;

      FilterKriterium[] filters = buildDefaultFilterAutomatikjobs();

      panelQueryUebersicht = new PanelQuery(querytypes,
                                            filters,
                                            QueryParameters.UC_ID_AUTOMATIK,
                                            aWhichStandardButtonIUse,
                                            getInternalFrame(),
                                            LPMain.getTextRespectUISPr(
                                                "lp.system.automatik.tab.oben.uebersicht.title"),
                                            true);
    }
    return panelQueryUebersicht;
  }


  private PanelAutomatik getPanelAutomatikUebersicht(boolean bNeedInstantiationIfNull)
      throws Throwable {
    if (panelAutomatikUebersicht == null && bNeedInstantiationIfNull) {
      panelAutomatikUebersicht = new PanelAutomatik(getInternalFrame(),
          LPMain.getTextRespectUISPr("lp.system.automatik.tab.oben.uebersicht.title"),
          null,
          this);
    }
    return panelAutomatikUebersicht;
  }

  public void lPEventObjectChanged(ChangeEvent e)
      throws Throwable {
    super.lPEventObjectChanged(e);
    int selectedIndex = this.getSelectedIndex();

    switch (selectedIndex) {
      case IDX_UEBERSICHT: {
        getPanelSplitUebersicht(true).eventYouAreSelected(false);
        getPanelAutomatikUebersicht(true).eventYouAreSelected(false);
      }
      break;
      case IDX_DETAIL: {
        AutomatikjobDto selectedautomatikjobDto = DelegateFactory.getInstance().getAutomatikDelegate().
        automatikjobFindByPrimaryKey(Integer.parseInt(panelQueryUebersicht.getSelectedId().toString()));
        AutomatikjobtypeDto automatikjobtypeDto = DelegateFactory.getInstance().getAutomatikDelegate().
            automatikjobtypeFindByPrimaryKey(selectedautomatikjobDto.getIAutomatikjobtypeIid());
        getPanelAutomatikDetails(true,automatikjobtypeDto.getCJobtype()).eventYouAreSelected(false);
      }
      break;
    }

  }


  public void lPEventItemChanged(ItemChangedEvent eI)
    throws Throwable {
  String sMandantCNr =LPMain.getInstance().getTheClient().getMandant();
  if (eI.getID() == ItemChangedEvent.ACTION_POSITION_VONNNACHNMINUS1) {
    //UP Pressed
    int selectedJob = Integer.parseInt(panelQueryUebersicht.getSelectedId().toString());
    AutomatikjobDto selectedautomatikjobDto = DelegateFactory.getInstance().
        getAutomatikDelegate().
        automatikjobFindByPrimaryKey(selectedJob);
    int jobBefore = selectedautomatikjobDto.getISort() - 1;
    if (! (jobBefore < 0)) {

      AutomatikjobDto beforeautomatikjobDto = DelegateFactory.getInstance().
          getAutomatikDelegate().
          automatikjobFindByISort(jobBefore);
      if(beforeautomatikjobDto.getCMandantCNr().equals(sMandantCNr)){
        beforeautomatikjobDto.setISort(selectedautomatikjobDto.getISort());
        selectedautomatikjobDto.setISort(jobBefore);
        DelegateFactory.getInstance().getAutomatikDelegate().updateAutomatikjob(
            selectedautomatikjobDto);
        DelegateFactory.getInstance().getAutomatikDelegate().updateAutomatikjob(
            beforeautomatikjobDto);
        getPanelSplitUebersicht(true).eventYouAreSelected(false);
      }
    }
  }
  else if (eI.getID() == ItemChangedEvent.ACTION_POSITION_VONNNACHNPLUS1) {
    //DOWN Pressed
    int selectedJob = Integer.parseInt(panelQueryUebersicht.getSelectedId().toString());
    AutomatikjobDto selectedautomatikjobDto = DelegateFactory.getInstance().
        getAutomatikDelegate().
        automatikjobFindByPrimaryKey(selectedJob);
    int jobAfter = selectedautomatikjobDto.getISort() + 1;
    AutomatikjobDto afterautomatikjobDto = null;
    try {
      afterautomatikjobDto = DelegateFactory.getInstance().
          getAutomatikDelegate().
          automatikjobFindByISort(jobAfter);
    }
    catch (Exception e) {
      //User is trying to move last job down
      //nothing here
    }
    if (afterautomatikjobDto != null) {
      if(afterautomatikjobDto.getCMandantCNr().equals(sMandantCNr)){
        afterautomatikjobDto.setISort(selectedautomatikjobDto.getISort());
        selectedautomatikjobDto.setISort(jobAfter);
        DelegateFactory.getInstance().getAutomatikDelegate().updateAutomatikjob(
            selectedautomatikjobDto);
        DelegateFactory.getInstance().getAutomatikDelegate().updateAutomatikjob(
            afterautomatikjobDto);
        getPanelSplitUebersicht(true).eventYouAreSelected(false);
      }
    }
  }
}



  /**
   * getJMenuBar
   *
   * @return JMenuBar
   * @throws Throwable
   * @todo Diese com.lp.client.frame.component.TabbedPane-Methode
   *   implementieren
   */
  protected JMenuBar getJMenuBar()
      throws Throwable {
    return new WrapperMenuBar(this);
  }


  /**
   * Behandle ActionEvent; zB Menue-Klick oben.
   *
   * @param e ActionEvent
   * @throws Throwable
   * @todo Diese com.lp.client.frame.component.TabbedPane-Methode
   *   implementieren
   */
  protected void lPActionEvent(ActionEvent e)
      throws Throwable {
  }
}
