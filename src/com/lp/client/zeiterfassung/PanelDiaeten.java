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
package com.lp.client.zeiterfassung;


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
import com.lp.server.personal.service.DiaetenDto;
import com.lp.server.personal.service.ZeiterfassungFac;
import com.lp.server.system.service.LandDto;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;


public class PanelDiaeten
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

  private WrapperLabel wlaBezeichnung = new WrapperLabel();
  private WrapperTextField wtfBezeichnung = new WrapperTextField();

  private InternalFrameZeiterfassung internalFrameZeiterfassung = null;

  private WrapperButton wbuLand = new WrapperButton();
  private WrapperTextField wtfLand = new WrapperTextField();
  private PanelQueryFLR panelQueryFLRLand = null;

  static final public String ACTION_SPECIAL_LAND_FROM_LISTE =
      "ACTION_SPECIAL_LAND_FROM_LISTE";
  public PanelDiaeten(InternalFrame internalFrame, String add2TitleI,
                        Object pk)
      throws Throwable {
    super(internalFrame, add2TitleI, pk);
    internalFrameZeiterfassung = (InternalFrameZeiterfassung) internalFrame;
    jbInit();
    setDefaults();
    initComponents();
    enableAllComponents(this, false);
  }


  protected JComponent getFirstFocusableComponent()
      throws Exception {
    return wtfBezeichnung;
  }


  protected void setDefaults()
      throws Throwable {

  }


  public void eventActionNew(EventObject eventObject, boolean bLockMeI,
                             boolean bNeedNoNewI)
      throws Throwable {
    super.eventActionNew(eventObject, true, false);
    internalFrameZeiterfassung.setDiaetenDto(new DiaetenDto());
    leereAlleFelder(this);

  }


  protected void eventActionSpecial(ActionEvent e)
      throws Throwable {
    if (e.getActionCommand().equals(ACTION_SPECIAL_LAND_FROM_LISTE)) {
      dialogQueryLandFromListe(e);
    }

  }


  void dialogQueryLandFromListe(ActionEvent e)
      throws Throwable {

    String[] aWhichButtonIUse = {
         PanelBasis.ACTION_REFRESH, PanelBasis.ACTION_LEEREN};

     panelQueryFLRLand = new PanelQueryFLR(
         null,
         null,
         QueryParameters.UC_ID_LAND,
         aWhichButtonIUse,
         getInternalFrame(),
         LPMain.getInstance().getTextRespectUISPr(
             "title.landauswahlliste"));

     panelQueryFLRLand.setSelectedId(internalFrameZeiterfassung.
         getDiaetenDto().getLandIId());

     new DialogQuery(panelQueryFLRLand);

  }


  protected void eventActionDelete(ActionEvent e, boolean bAdministrateLockKeyI,
                                   boolean bNeedNoDeleteI)
      throws Throwable {
    DelegateFactory.getInstance().getZeiterfassungDelegate().removeDiaeten(
        internalFrameZeiterfassung.getDiaetenDto());
    this.setKeyWhenDetailPanel(null);
    super.eventActionDelete(e, false, false);
  }


  protected void components2Dto()
      throws Throwable {
    internalFrameZeiterfassung.getDiaetenDto().setCBez(wtfBezeichnung.
        getText());
  }


  protected void dto2Components()
      throws Throwable {
    wtfBezeichnung.setText(internalFrameZeiterfassung.getDiaetenDto().getCBez());

    LandDto landDto = DelegateFactory.getInstance().getSystemDelegate().
            landFindByPrimaryKey( internalFrameZeiterfassung.getDiaetenDto().getLandIId());
        wtfLand.setText(landDto.getCLkz());
  }


  public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
      throws Throwable {
    if (allMandatoryFieldsSetDlg()) {
      components2Dto();
      if (internalFrameZeiterfassung.getDiaetenDto().getIId() == null) {
        internalFrameZeiterfassung.getDiaetenDto().setIId(DelegateFactory.getInstance().
            getZeiterfassungDelegate().
            createDiaeten(internalFrameZeiterfassung.getDiaetenDto()));
        setKeyWhenDetailPanel(internalFrameZeiterfassung.getDiaetenDto().getIId());
      }
      else {
        DelegateFactory.getInstance().getZeiterfassungDelegate().updateDiaetenz(
            internalFrameZeiterfassung.getDiaetenDto());
      }
      super.eventActionSave(e, true);
      if (getInternalFrame().getKeyWasForLockMe() == null) {
        getInternalFrame().setKeyWasForLockMe(internalFrameZeiterfassung.getDiaetenDto().
                                              getIId() + "");
      }
      eventYouAreSelected(false);
    }
  }


  protected void eventItemchanged(EventObject eI)
      throws Throwable {
    ItemChangedEvent e = (ItemChangedEvent) eI;
    if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
      if (e.getSource() == panelQueryFLRLand) {
        Object key = ( (ISourceEvent) e.getSource()).getIdSelected();
        LandDto landDto = DelegateFactory.getInstance().getSystemDelegate().
            landFindByPrimaryKey( (Integer)
                                 key);
        wtfLand.setText(landDto.getCLkz());
        internalFrameZeiterfassung.getDiaetenDto().setLandIId(landDto.getIID());
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

    wtfLand.setActivatable(false);
     wtfLand.setMandatoryField(true);
     wtfBezeichnung.setMandatoryField(true);

    wbuLand.setText(LPMain.getInstance().getTextRespectUISPr(
        "lp.land.flr"));
    wbuLand.setActionCommand(ACTION_SPECIAL_LAND_FROM_LISTE);
    wbuLand.addActionListener(this);
    wlaBezeichnung.setText(LPMain.getInstance().getTextRespectUISPr("lp.bezeichnung"));


    wtfBezeichnung.setColumns(ZeiterfassungFac.MAX_TAETIGKEIT_BEZEICHNUNG);
    getInternalFrame().addItemChangedListener(this);




    this.add(jpaButtonAction, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
        , GridBagConstraints.WEST, GridBagConstraints.NONE,
        new Insets(0, 0, 0, 0), 0, 0));

    //jetzt meine felder
    jpaWorkingOn = new JPanel();
    gridBagLayoutWorkingPanel = new GridBagLayout();
    jpaWorkingOn.setLayout(gridBagLayoutWorkingPanel);
    this.add(jpaWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0
                                                  , GridBagConstraints.SOUTHEAST,
                                                  GridBagConstraints.BOTH,
                                                  new Insets(0, 0, 0, 0), 0, 0));
    this.add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.BOTH,
        new Insets(0, 0, 0, 0), 0, 0));


    jpaWorkingOn.add(wlaBezeichnung, new GridBagConstraints(0, 1, 1, 1, 0.1, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
        0, 0));
    jpaWorkingOn.add(wtfBezeichnung, new GridBagConstraints(1, 1, 1, 1, 0.3, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
        0, 0));
    jpaWorkingOn.add(wbuLand, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
        0, 0));
    jpaWorkingOn.add(wtfLand, new GridBagConstraints(1, 2, 1, 1, 0, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
        0, 0));




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
    return HelperClient.LOCKME_DIAETEN;
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
      internalFrameZeiterfassung.setDiaetenDto(DelegateFactory.getInstance().
                                                getZeiterfassungDelegate().
                                                diaetenFindByPrimaryKey( (Integer) key));
      dto2Components();
    }
  }
}
