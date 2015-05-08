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
package com.lp.client.artikel;


import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.EventObject;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.border.Border;

import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.system.SystemFilterFactory;
import com.lp.server.artikel.service.ArtikelsperrenDto;
import com.lp.server.artikel.service.SperrenDto;
import com.lp.server.system.jcr.service.JCRDocFac;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;

@SuppressWarnings("static-access")
public class PanelArtikelsperren
    extends PanelBasis
{

  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
//von hier ...
  private GridBagLayout gridBagLayoutAll = null;
  private JPanel jpaWorkingOn = new JPanel();
  private JPanel jpaButtonAction = null;
  private Border border = null;
  private GridBagLayout gridBagLayoutWorkingPanel = null;
  private InternalFrameArtikel internalFrameArtikel = null;
  private ArtikelsperrenDto artikelsperrenDto = null;
  private PanelQueryFLR panelQueryFLRSperren = null;
  private WrapperButton wbuSperren = new WrapperButton();
  private WrapperTextField wtfSperren = new WrapperTextField();
  
  private WrapperLabel wlaGrund = new WrapperLabel();
  private WrapperTextField wtfGrund = new WrapperTextField();

  static final public String ACTION_SPECIAL_SPERREN_FROM_LISTE =
      "ACTION_SPECIAL_SPERREN_FROM_LISTE";
  public PanelArtikelsperren(InternalFrame internalFrame, String add2TitleI,
                           Object pk)
      throws Throwable {
    super(internalFrame, add2TitleI, pk);
    internalFrameArtikel = (InternalFrameArtikel) internalFrame;
    jbInit();
    setDefaults();
    initComponents();
    enableAllComponents(this, false);
  }


  protected void setDefaults() {

  }


  protected JComponent getFirstFocusableComponent()
      throws Exception {
    return wtfSperren;
  }


  public void eventActionNew(EventObject eventObject, boolean bLockMeI,
                             boolean bNeedNoNewI)
      throws Throwable {
    super.eventActionNew(eventObject, true, false);
    leereAlleFelder(this);
    artikelsperrenDto = new ArtikelsperrenDto();
  }


  protected void dto2Components() throws Throwable {
    if(artikelsperrenDto.getSperrenIId()!=null){
      wtfSperren.setText(DelegateFactory.getInstance().getArtikelDelegate().sperrenFindByPrimaryKey(artikelsperrenDto.getSperrenIId()).getCBez());
    }
    wtfGrund.setText(artikelsperrenDto.getCGrund());
    
    this.setStatusbarPersonalIIdAendern(artikelsperrenDto
			.getPersonalIIdAendern());
	this.setStatusbarTAendern(artikelsperrenDto.getTAendern());
    
  }


  void dialogQuerySperrenFromListe(ActionEvent e)
        throws Throwable {

      panelQueryFLRSperren = new PanelQueryFLR(
          null,
          null,
          QueryParameters.UC_ID_SPERREN,
          null,
          getInternalFrame(),
          LPMain.getInstance().getTextRespectUISPr(
              "lp.sperren"));

      panelQueryFLRSperren.befuellePanelFilterkriterienDirekt(SystemFilterFactory.
          getInstance().createFKDBezeichnung(),null);
      panelQueryFLRSperren.setSelectedId(artikelsperrenDto.getSperrenIId());
      new DialogQuery(panelQueryFLRSperren);
  }

  protected void components2Dto() {
	  artikelsperrenDto.setCGrund(wtfGrund.getText());
  }


  protected void eventItemchanged(EventObject eI)
      throws Throwable {
    ItemChangedEvent e = (ItemChangedEvent) eI;
    if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
      if (e.getSource() == panelQueryFLRSperren) {
        Object key = ( (ISourceEvent) e.getSource()).getIdSelected();
        SperrenDto sperrenDto = DelegateFactory.getInstance().getArtikelDelegate().sperrenFindByPrimaryKey((Integer)key);

        wtfSperren.setText(sperrenDto.getCBez());
        artikelsperrenDto.setSperrenIId(sperrenDto.getIId());
      }

    }
  }
  private void jbInit()
      throws Throwable {
    //von hier ...
    border = BorderFactory.createEmptyBorder(10, 10, 10, 10);
    setBorder(border);
    //das Aussenpanel hat immer das Gridbaglayout.
    gridBagLayoutAll = new GridBagLayout();
    this.setLayout(gridBagLayoutAll);

    //Actionpanel von Oberklasse holen und anhaengen.
    jpaButtonAction = getToolsPanel();
    this.setActionMap(null);

    wbuSperren.setText(LPMain.getInstance().getTextRespectUISPr("lp.sperren"));

    wlaGrund.setText(LPMain.getInstance().getTextRespectUISPr("lp.grund"));
    
    wbuSperren.setActionCommand(ACTION_SPECIAL_SPERREN_FROM_LISTE);
    wbuSperren.addActionListener(this);


    wtfSperren.setMandatoryField(true);
    wtfSperren.setActivatable(false);
    wtfGrund.setColumnsMax(80);
    wtfGrund.setMandatoryField(true);


    getInternalFrame().addItemChangedListener(this);

    this.add(jpaButtonAction, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
        , GridBagConstraints.WEST, GridBagConstraints.NONE,
        new Insets(0, 0, 0, 0), 0, 0));

    //jetzt meine felder
    jpaWorkingOn = new JPanel();
    gridBagLayoutWorkingPanel = new GridBagLayout();
    jpaWorkingOn.setLayout(gridBagLayoutWorkingPanel);
    this.add(jpaWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0
        , GridBagConstraints.SOUTHEAST, GridBagConstraints.BOTH,
        new Insets(0, 0, 0, 0), 0, 0));
    this.add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.BOTH,
        new Insets(0, 0, 0, 0), 0, 0));
    jpaWorkingOn.add(wbuSperren,
                        new GridBagConstraints(0, 0, 1, 1, 0.05, 0.0
                                               , GridBagConstraints.CENTER,
                                               GridBagConstraints.HORIZONTAL,
                                               new Insets(2, 2, 2, 2), 0, 0));
    jpaWorkingOn.add(wtfSperren,
                        new GridBagConstraints(1, 0, 1, 1, 0.1, 0.0
                                               , GridBagConstraints.CENTER,
                                               GridBagConstraints.HORIZONTAL,
                                               new Insets(2, 2, 2, 2), 0, 0));
    jpaWorkingOn.add(wlaGrund,
            new GridBagConstraints(0, 1, 1, 1, 0, 0.0
                                   , GridBagConstraints.CENTER,
                                   GridBagConstraints.HORIZONTAL,
                                   new Insets(2, 2, 2, 2), 0, 0));
jpaWorkingOn.add(wtfGrund,
            new GridBagConstraints(1, 1, 1, 1, 0, 0.0
                                   , GridBagConstraints.CENTER,
                                   GridBagConstraints.HORIZONTAL,
                                   new Insets(2, 2, 2, 2), 0, 0));
    String[] aWhichButtonIUse = {
        ACTION_UPDATE,
        ACTION_SAVE,
        ACTION_DELETE,
        ACTION_DISCARD,
    };

    enableToolsPanelButtons(aWhichButtonIUse);

  }


  protected String getLockMeWer()
      throws Exception {
    return HelperClient.LOCKME_ARTIKEL;
  }


  protected void eventActionSpecial(ActionEvent e)
      throws Throwable {
    if (e.getActionCommand().equals(ACTION_SPECIAL_SPERREN_FROM_LISTE)) {
      dialogQuerySperrenFromListe(e);
    }
  }


  protected void eventActionDelete(ActionEvent e, boolean bAdministrateLockKeyI,
                                   boolean bNeedNoDeleteI)
      throws Throwable, Throwable {
    DelegateFactory.getInstance().getArtikelDelegate().removeArtikelsperren(artikelsperrenDto);
    this.setKeyWhenDetailPanel(null);
    super.eventActionDelete(e, false, false);
  }


  public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
      throws Throwable {

    super.eventYouAreSelected(false);
    Object key = getKeyWhenDetailPanel();

    if (key == null
        || (key.equals(LPMain.getLockMeForNew()))) {
      leereAlleFelder(this);
      clearStatusbar();
    }
    else {
      artikelsperrenDto = DelegateFactory.getInstance().getArtikelDelegate().
          artikelsperrenFindByPrimaryKey( (Integer) key);
      dto2Components();
    }

  }


  public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
      throws Throwable {
    if (allMandatoryFieldsSetDlg()) {
      components2Dto();
      if (artikelsperrenDto.getIId() == null) {
        artikelsperrenDto.setArtikelIId(internalFrameArtikel.getArtikelDto().
                                 getIId());
        artikelsperrenDto.setIId(DelegateFactory.getInstance().getArtikelDelegate().createArtikelsperren(
            artikelsperrenDto));
        setKeyWhenDetailPanel(artikelsperrenDto.getIId());
      }
      else {
        DelegateFactory.getInstance().getArtikelDelegate().updateArtikelsperren(
            artikelsperrenDto);
      }
      super.eventActionSave(e, true);
      if (getInternalFrame().getKeyWasForLockMe() == null) {
        getInternalFrame().setKeyWasForLockMe(internalFrameArtikel.
                                              getArtikelDto().getIId().
                                              toString());
      }
      eventYouAreSelected(false);
    }
  }
}
