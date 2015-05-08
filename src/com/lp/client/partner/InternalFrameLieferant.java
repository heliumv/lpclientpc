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


import java.util.EventObject;

import javax.swing.ImageIcon;
import javax.swing.JTabbedPane;

import com.lp.client.frame.Command;
import com.lp.client.frame.CommandGoto;
import com.lp.client.frame.ICommand;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQueryFLRGoto;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.partner.service.AnsprechpartnerDto;
import com.lp.server.partner.service.LflfliefergruppeDto;
import com.lp.server.partner.service.LfliefergruppeDto;
import com.lp.server.partner.service.LieferantDto;

@SuppressWarnings("static-access")
/**
 * <p>Diese Klasse kuemmert sich um die Lieferanten.</p>
 *
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 *
 * <p>Erstellungsdatum 10.01.05</p>
 *
 * @author $Author: christian $
 * @version $Revision: 1.4 $
 * Date $Date: 2012/08/13 13:17:29 $
 */
public class InternalFrameLieferant
    extends InternalFrame
{
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

public static String I_ID_LIEFERANT = "iIdLieferant";

  private LieferantDto lieferantDto = new LieferantDto();
  private Integer lieferantiId = null;
  private AnsprechpartnerDto ansprechpartnerDto = new AnsprechpartnerDto();
  private LfliefergruppeDto lfliefergruppeDto = new LfliefergruppeDto();
  private LflfliefergruppeDto lflfliefergruppeDto = null;

  private TabbedPaneLieferant tpLieferant = null;
  private TabbedPaneGrunddatenLieferant tpGrunddaten = null;

  public static int IDX_PANE_LIEFERANT = 0;
  public static int IDX_PANE_GRUNDDATEN = 1;
  
  private String rechtModulweit = null;
  

  public InternalFrameLieferant(String title,
                                String belegartCNr, String sRechtModulweitI)
      throws Throwable {

    super(title, belegartCNr, sRechtModulweitI);
    rechtModulweit = sRechtModulweitI;
    jbInit();
    initComponents();
  }


  private void jbInit()
      throws Throwable {
    //1 unteres tab: Partner; lazy loading; ist auch default.
    tabbedPaneRoot.insertTab(
        LPMain.getInstance().getTextRespectUISPr("lp.lieferant.modulname"),
        null,
        tpLieferant,
        LPMain.getInstance().getTextRespectUISPr("lp.lieferant.modulname"),
        IDX_PANE_LIEFERANT);

    // 2 tab unten: Grunddaten
    // nur anzeigen wenn Benutzer Recht dazu hat
    if(DelegateFactory.getInstance().getTheJudgeDelegate().hatRecht(RechteFac.RECHT_LP_DARF_GRUNDDATEN_SEHEN)){
      tabbedPaneRoot.insertTab(
          LPMain.getInstance().getTextRespectUISPr("pers.title.tab.grunddaten"),
          null,
          null,
          LPMain.getInstance().getTextRespectUISPr("pers.title.tab.grunddaten"),
          IDX_PANE_GRUNDDATEN);
    }

    // Defaulttabbedpane setzen.
    refreshLieferantTP();

    tpLieferant.lPEventObjectChanged(null);

    tabbedPaneRoot.setSelectedComponent(tpLieferant);

    //ich selbst moechte informiert werden.
    addItemChangedListener(this);
    //awt: listener bin auch ich.
    registerChangeListeners();

    // dem frame das icon setzen
    ImageIcon iicon = new javax.swing.ImageIcon(
        getClass().getResource("/com/lp/client/res/address_book216x16.png"));
    setFrameIcon(iicon);
  }


  public void lPStateChanged(EventObject e)
      throws Throwable {
	  setRechtModulweit(rechtModulweit);
    int selectedCur = ( (JTabbedPane) e.getSource()).getSelectedIndex();

    if (selectedCur == IDX_PANE_LIEFERANT) {
      refreshLieferantTP();
      tpLieferant.lPEventObjectChanged(null);
      setLpTitle(InternalFrame.TITLE_IDX_OHRWASCHLUNTEN,
                 LPMain.getInstance().getTextRespectUISPr("label.lieferant"));
    }

    else if (selectedCur == IDX_PANE_GRUNDDATEN) {
      refreshGrunddatenTP();
      tabbedPaneRoot.setSelectedComponent(tpGrunddaten);
      //Info an Tabbedpane, bist selektiert worden.
      tpGrunddaten.lPEventObjectChanged(null);
      setLpTitle(InternalFrame.TITLE_IDX_OHRWASCHLUNTEN,
                 LPMain.getInstance().getTextRespectUISPr(
                     "bes.title.panel.bestellunggrunddaten"));
    }

  }


  public void lPEventItemChanged(ItemChangedEvent eI)
      throws Throwable {
    if (eI.getID() == ItemChangedEvent.ACTION_UPDATE) {
      if (eI.getSource() instanceof PanelQueryFLRGoto) {
        Integer iIdLieferant = (Integer) ( (ISourceEvent) eI.getSource()).
            getIdSelected();
        LieferantDto lieferantDto = DelegateFactory.getInstance().getLieferantDelegate().
            lieferantFindByPrimaryKey(iIdLieferant);
        setLieferantDto(lieferantDto);
        tpLieferant.lPEventItemChanged(eI);
      }
    }
  }


  public LieferantDto getLieferantDto() {
    return lieferantDto;
  }


  public AnsprechpartnerDto getAnsprechpartnerDto() {
    return ansprechpartnerDto;
  }


  public TabbedPaneGrunddatenLieferant getTpGrunddaten() {
    return tpGrunddaten;
  }


  public TabbedPaneLieferant getTpLieferant() {
    return tpLieferant;
  }


  public LfliefergruppeDto getLfliefergruppeDto() {
    return lfliefergruppeDto;
  }


  public LflfliefergruppeDto getLflfliefergruppeDto() {
    return lflfliefergruppeDto;
  }


  public Integer getLieferantiId() {
    return lieferantiId;
  }


  public void setLieferantDto(LieferantDto lieferantDtoI) {
    lieferantDto = lieferantDtoI;
  }


  public void setAnsprechpartnerDto(AnsprechpartnerDto ansprechpartnerDtoI) {
    ansprechpartnerDto = ansprechpartnerDtoI;
  }


  public void setLfliefergruppeDto(LfliefergruppeDto lfliefergruppeDto) {
    this.lfliefergruppeDto = lfliefergruppeDto;
  }


  public void setLflfliefergruppeDto(LflfliefergruppeDto lflfliefergruppeDto) {
    this.lflfliefergruppeDto = lflfliefergruppeDto;
  }


  public void setLieferantiId(Integer lieferantiId) {
    this.lieferantiId = lieferantiId;
  }


  private void refreshLieferantTP()
      throws Throwable {
    if (tpLieferant == null) {
      tpLieferant = new TabbedPaneLieferant(this);
      tabbedPaneRoot.setComponentAt(IDX_PANE_LIEFERANT, tpLieferant);
      initComponents();
    }
  }


  private void refreshGrunddatenTP()
      throws Throwable {
    if (tpGrunddaten == null) {
      //lazy loading
      tpGrunddaten = new TabbedPaneGrunddatenLieferant(this);
      tabbedPaneRoot.setComponentAt(IDX_PANE_GRUNDDATEN, tpGrunddaten);
      initComponents();
    }
  }

  public int execute(Command commandI)
     throws Throwable {

   int iRetCmd = ICommand.COMMAND_DONE;

   if (commandI instanceof CommandGoto) {
     CommandGoto gotoCommand = (CommandGoto) commandI;

     if (gotoCommand.getsInternalFrame() == Command.S_INTERNALFRAME_LIEFERANT) {
       if (gotoCommand.getTabbedPaneAsClass() == Command.CLASS_TABBED_PANE_LIEFERANT) {
         if (gotoCommand.getsPanel().equals(Command.PANEL_LIEFERANT_KOPFDATEN)) {
           Integer iId =
               (Integer) gotoCommand.getHmOfExtraData().get(I_ID_LIEFERANT);
           tpLieferant.getPanelLieferantenQP1().setSelectedId(iId);
           tpLieferant.setSelectedIndex(1);
           LieferantDto l = DelegateFactory.getInstance().getLieferantDelegate().
               lieferantFindByPrimaryKey(iId);
           setLieferantiId(iId);
           setLieferantDto(l);
           if (gotoCommand.getSAction().equals(PanelBasis.ACTION_UPDATE)) {
             this.fireItemChanged(this, ItemChangedEvent.ACTION_UPDATE);
             tpLieferant.getPanelLieferantenKopfdatenD2().setKeyWhenDetailPanel(iId);
             tpLieferant.getPanelLieferantenKopfdatenD2().eventYouAreSelected(false);
            }
         }
       }
     }
   }
   else {
     iRetCmd = ICommand.COMMAND_NOT_DONE;
   }

   return iRetCmd;

 }


}
