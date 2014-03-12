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

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperDateField;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.personal.service.DiaetentagessatzDto;


public class PanelDiaetentagessatz
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
  private InternalFrameZeiterfassung internalFrameZeiterfassung = null;
  private WrapperLabel wlaGueltigab = new WrapperLabel();
  private DiaetentagessatzDto diaetentagessatzDto = null;

  private WrapperCheckBox wcbStundenweise = new WrapperCheckBox();

  private WrapperLabel wlaWaehrung = new WrapperLabel();

  private WrapperLabel wlaMindestsatz = new WrapperLabel();
  private WrapperNumberField wnfMindestsatz = new WrapperNumberField();
  private WrapperLabel wlaTagessatz = new WrapperLabel();
  private WrapperNumberField wnfTagessatz = new WrapperNumberField();
  private WrapperLabel wlaStundensatz = new WrapperLabel();
  private WrapperNumberField wnfStundensatz = new WrapperNumberField();
  private WrapperLabel wlaAbstunden = new WrapperLabel();
  private WrapperNumberField wnfAbstunden = new WrapperNumberField();


  private WrapperDateField wdfGueltigab = new WrapperDateField();


  public PanelDiaetentagessatz(InternalFrame internalFrame, String add2TitleI,
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
    return wnfStundensatz;
  }


  protected void setDefaults() {

  }
  protected void eventActionSpecial(ActionEvent e)
      throws Throwable {
    if(e.getSource().equals(wcbStundenweise)){
      if(wcbStundenweise.isSelected()){
        wnfAbstunden.setEditable(true);
      } else {
        wnfAbstunden.setEditable(false);
        wnfAbstunden.setInteger(0);
      }

    }
  }

  public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
      throws Throwable {

    super.eventYouAreSelected(false);
    Object key = getKeyWhenDetailPanel();

    if (key == null
        || (key.equals(LPMain.getLockMeForNew()))) {

      leereAlleFelder(this);
      clearStatusbar();


        wdfGueltigab.setDate(new java.sql.Date(System.currentTimeMillis()));
        if(wcbStundenweise.isSelected()){
   wnfAbstunden.setEditable(true);
 } else {
   wnfAbstunden.setEditable(false);
   wnfAbstunden.setInteger(0);
 }

    }
    else {
      diaetentagessatzDto = DelegateFactory.getInstance().getZeiterfassungDelegate().
          diaetentagessatzFindByPrimaryKey( (Integer) key);

      dto2Components();

    }


  }


  public void eventActionNew(EventObject eventObject, boolean bLockMeI,
                             boolean bNeedNoNewI)
      throws Throwable {
    super.eventActionNew(eventObject, true, false);
    leereAlleFelder(this);
    diaetentagessatzDto = new DiaetentagessatzDto();

  }

  protected void eventActionUpdate(ActionEvent aE, boolean bNeedNoUpdateI)
      throws Throwable {
    super.eventActionUpdate(aE, bNeedNoUpdateI);
    if(wcbStundenweise.isSelected()){
       wnfAbstunden.setEditable(true);
     } else {
       wnfAbstunden.setEditable(false);
       wnfAbstunden.setInteger(0);
     }


  }
  protected void dto2Components()
      throws Throwable {
    wnfStundensatz.setBigDecimal(diaetentagessatzDto.getNStundensatz());
    wnfTagessatz.setBigDecimal(diaetentagessatzDto.getNTagessatz());
    wnfMindestsatz.setBigDecimal(diaetentagessatzDto.getNMindestsatz());
    wnfAbstunden.setInteger(diaetentagessatzDto.getIAbstunden());
    wcbStundenweise.setShort(diaetentagessatzDto.getBStundenweise());
    wdfGueltigab.setTimestamp(diaetentagessatzDto.getTGueltigab());

  }


  protected void eventItemchanged(EventObject eI)
      throws Throwable {
    ItemChangedEvent e = (ItemChangedEvent) eI;
  }


  public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
      throws Throwable {
    if (allMandatoryFieldsSetDlg()) {
      components2Dto();
      if (diaetentagessatzDto.getIId() == null) {
        diaetentagessatzDto.setDiaetenIId(internalFrameZeiterfassung.getDiaetenDto().
                                          getIId());
        diaetentagessatzDto.setIId(DelegateFactory.getInstance().getZeiterfassungDelegate().
                                  createDiaetentagessatz(
                                      diaetentagessatzDto));
        setKeyWhenDetailPanel(diaetentagessatzDto.getIId());
      }
      else {
        DelegateFactory.getInstance().getZeiterfassungDelegate().updateDiaetentagessatz(
            diaetentagessatzDto);
      }
      super.eventActionSave(e, true);

      if (getInternalFrame().getKeyWasForLockMe() == null) {
        getInternalFrame().setKeyWasForLockMe(internalFrameZeiterfassung.
                                              getDiaetenDto().getIId().
                                              toString());
      }
      eventYouAreSelected(false);
    }
  }


  protected void components2Dto()
      throws ExceptionLP {
    diaetentagessatzDto.setNStundensatz(wnfStundensatz.getBigDecimal());
    diaetentagessatzDto.setNTagessatz(wnfTagessatz.getBigDecimal());
    diaetentagessatzDto.setNMindestsatz(wnfMindestsatz.getBigDecimal());
    diaetentagessatzDto.setBStundenweise(wcbStundenweise.getShort());
    diaetentagessatzDto.setIAbstunden(wnfAbstunden.getInteger());
    diaetentagessatzDto.setTGueltigab(wdfGueltigab.getTimestamp());
  }


  protected void eventActionDelete(ActionEvent e, boolean bAdministrateLockKeyI,
                                   boolean bNeedNoDeleteI)
      throws Throwable {
    DiaetentagessatzDto dto = new DiaetentagessatzDto();
    dto.setIId( (Integer) getKeyWhenDetailPanel());
    DelegateFactory.getInstance().getZeiterfassungDelegate().removeDiaetentagessatz(dto);
    this.setKeyWhenDetailPanel(null);
    super.eventActionDelete(e, false, false);
    diaetentagessatzDto = new DiaetentagessatzDto();
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

    wlaGueltigab.setText(LPMain.getInstance().getTextRespectUISPr("lp.gueltigab"));
    getInternalFrame().addItemChangedListener(this);

    wlaStundensatz.setText(LPMain.getInstance().getTextRespectUISPr(
        "pers.personalgehalt.stundensatz"));
    wlaMindestsatz.setText(LPMain.getInstance().getTextRespectUISPr(
        "pers.zeiterfassung.diaeten.mindestsatz"));
    wlaTagessatz.setText(LPMain.getInstance().getTextRespectUISPr(
        "pers.zeiterfassung.diaeten.tagessatz"));

    wlaAbstunden.setText(LPMain.getInstance().getTextRespectUISPr(
        "pers.zeiterfassung.diaeten.abstunden"));


    wcbStundenweise.setText(LPMain.getInstance().getTextRespectUISPr("pers.zeiterfassung.diaeten.stundeweise"));
    wcbStundenweise.addActionListener(this);

    wlaWaehrung.setText(DelegateFactory.getInstance().getMandantDelegate().
                        mandantFindByPrimaryKey(LPMain.getInstance().
                                                getTheClient().getMandant()).
                        getWaehrungCNr());
    ;
    wdfGueltigab.setMandatoryField(true);
    wnfStundensatz.setMandatoryField(true);
    wnfStundensatz.setFractionDigits(6);
    wnfMindestsatz.setMandatoryField(true);
    wnfMindestsatz.setFractionDigits(6);
    wnfTagessatz.setMandatoryField(true);
    wnfTagessatz.setFractionDigits(6);
    wnfAbstunden.setMandatoryField(true);
    wnfAbstunden.setFractionDigits(0);
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


    jpaWorkingOn.add(wlaGueltigab,
                     new GridBagConstraints(0, iZeile, 1, 1, 0.05, 0.0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.HORIZONTAL,
                                            new Insets(2, 2, 2, 2), 0, 0));

    jpaWorkingOn.add(wdfGueltigab,
                     new GridBagConstraints(1, iZeile, 1, 1, 0.1, 0.0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.HORIZONTAL,
                                            new Insets(0, 0, 0, 0), 0, 0));


    jpaWorkingOn.add(wlaTagessatz,
                new GridBagConstraints(3, iZeile, 1, 1, 0.05, 0.0
                                       , GridBagConstraints.CENTER,
                                       GridBagConstraints.HORIZONTAL,
                                       new Insets(2, 2, 2, 2), 0, 0));

jpaWorkingOn.add(wnfTagessatz,
                new GridBagConstraints(4, iZeile, 1, 1, 0, 0.0
                                       , GridBagConstraints.WEST,
                                       GridBagConstraints.NONE,
                                       new Insets(0, 0, 0, 0), 0, 0));





    jpaWorkingOn.add(wlaWaehrung,
                     new GridBagConstraints(5, iZeile, 1, 1, 0, 0.0
                                            , GridBagConstraints.WEST,
                                            GridBagConstraints.HORIZONTAL,
                                            new Insets(0, 0, 0, 0), 20, 0));
    iZeile++;


    jpaWorkingOn.add(wlaStundensatz,
                      new GridBagConstraints(3, iZeile, 1, 1, 0.05, 0.0
                                             , GridBagConstraints.CENTER,
                                             GridBagConstraints.HORIZONTAL,
                                             new Insets(2, 2, 2, 2), 0, 0));
     jpaWorkingOn.add(wnfStundensatz,
                      new GridBagConstraints(4, iZeile, 1, 1, 0.0, 0.0
                                             , GridBagConstraints.WEST,
                                             GridBagConstraints.NONE,
                                           new Insets(0, 0, 0, 0), 0, 0));
     iZeile++;
   jpaWorkingOn.add(wlaMindestsatz,
                   new GridBagConstraints(3, iZeile, 1, 1, 0.05, 0.0
                                          , GridBagConstraints.CENTER,
                                          GridBagConstraints.HORIZONTAL,
                                          new Insets(2, 2, 2, 2), 0, 0));
  jpaWorkingOn.add(wnfMindestsatz,
                   new GridBagConstraints(4, iZeile, 1, 1, 0.0, 0.0
                                          , GridBagConstraints.WEST,
                                          GridBagConstraints.NONE,
                                          new Insets(0, 0, 0, 0), 0, 0));

  iZeile++;
 jpaWorkingOn.add(wcbStundenweise,
                 new GridBagConstraints(1, iZeile, 2, 1, 0.05, 0.0
                                        , GridBagConstraints.CENTER,
                                        GridBagConstraints.HORIZONTAL,
                                        new Insets(2, 2, 2, 2), 0, 0));


jpaWorkingOn.add(wlaAbstunden,
                new GridBagConstraints(3, iZeile, 1, 1, 0.05, 0.0
                                       , GridBagConstraints.EAST,
                                       GridBagConstraints.HORIZONTAL,
                                       new Insets(2, 2, 2, 2), 0, 0));
jpaWorkingOn.add(wnfAbstunden,
                new GridBagConstraints(4, iZeile, 1, 1, 0.0, 0.0
                                       , GridBagConstraints.WEST,
                                       GridBagConstraints.NONE,
                                       new Insets(0, 0, 0, 0), 0, 0));




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

}
