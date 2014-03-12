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
import java.util.Date;
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
import com.lp.client.frame.component.WrapperDateField;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.personal.service.MaschinenkostenDto;


public class PanelMaschinenkosten
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
  private WrapperLabel wlaLager = new WrapperLabel();
  private MaschinenkostenDto maschinenkostenDto = null;
  private WrapperLabel wlaLagerart = new WrapperLabel();
  private WrapperLabel wlaWaehrung = new WrapperLabel();
  private WrapperNumberField wnfStundensatz = new WrapperNumberField();
  private WrapperDateField wdfGueltigab = new WrapperDateField();

  private WrapperLabel wlaKaufdatum = new WrapperLabel();

  public PanelMaschinenkosten(InternalFrame internalFrame, String add2TitleI,
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


  public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
      throws Throwable {

    super.eventYouAreSelected(false);
    Object key = getKeyWhenDetailPanel();

    if (key == null
        || (key.equals(LPMain.getLockMeForNew()))) {

      leereAlleFelder(this);
      clearStatusbar();

      //Das default-Kostendatum, gueltig ab, ist beim ersten Eintrag das Kaufdatum, sonst heute.

      if (internalFrameZeiterfassung.getTabbedPaneMaschinen().
          getPanelQueryMaschinenkosten().getSelectedId() == null &&
          internalFrameZeiterfassung.getMaschineDto().getTKaufdatum() != null) {
        wdfGueltigab.setTimestamp(internalFrameZeiterfassung.getMaschineDto().
                                  getTKaufdatum());
      }
      else {
        wdfGueltigab.setDate(new java.sql.Date(System.currentTimeMillis()));
      }
    }
    else {
      maschinenkostenDto = DelegateFactory.getInstance().getZeiterfassungDelegate().
          maschinenkostenFindByPrimaryKey( (Integer) key);

      dto2Components();

    }
    wlaKaufdatum.setText(LPMain.getTextRespectUISPr(
        "zeiterfassung.kaufdatum") + ": ");
    if (internalFrameZeiterfassung.getMaschineDto().getTKaufdatum() != null) {
      Date date = new Date(internalFrameZeiterfassung.getMaschineDto().getTKaufdatum().
                           getTime());
      wlaKaufdatum.setText(wlaKaufdatum.getText() +
                           com.lp.util.Helper.formatDatum(date,
          LPMain.getTheClient().getLocUi()));
    }

  }


  public void eventActionNew(EventObject eventObject, boolean bLockMeI,
                             boolean bNeedNoNewI)
      throws Throwable {
    super.eventActionNew(eventObject, true, false);
    leereAlleFelder(this);
    maschinenkostenDto = new MaschinenkostenDto();

  }


  protected void dto2Components()
      throws Throwable {
    wnfStundensatz.setBigDecimal(maschinenkostenDto.getNStundensatz());
    wdfGueltigab.setTimestamp(maschinenkostenDto.getTGueltigab());

  }


  protected void eventItemchanged(EventObject eI)
      throws Throwable {
    ItemChangedEvent e = (ItemChangedEvent) eI;
  }


  public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
      throws Throwable {
    if (allMandatoryFieldsSetDlg()) {
      components2Dto();
      if (maschinenkostenDto.getIId() == null) {
        maschinenkostenDto.setMaschineIId(internalFrameZeiterfassung.getMaschineDto().
                                          getIId());
        maschinenkostenDto.setIId(DelegateFactory.getInstance().getZeiterfassungDelegate().
                                  createMaschinenkosten(
                                      maschinenkostenDto));
        setKeyWhenDetailPanel(maschinenkostenDto.getIId());
      }
      else {
        DelegateFactory.getInstance().getZeiterfassungDelegate().updateMaschinenkosten(
            maschinenkostenDto);
      }
      super.eventActionSave(e, true);

      if (getInternalFrame().getKeyWasForLockMe() == null) {
        getInternalFrame().setKeyWasForLockMe(internalFrameZeiterfassung.
                                              getMaschineDto().getIId().
                                              toString());
      }
      eventYouAreSelected(false);
    }
  }


  protected void components2Dto()
      throws ExceptionLP {
    maschinenkostenDto.setNStundensatz(wnfStundensatz.getBigDecimal());
    maschinenkostenDto.setTGueltigab(wdfGueltigab.getTimestamp());
  }


  protected void eventActionDelete(ActionEvent e, boolean bAdministrateLockKeyI,
                                   boolean bNeedNoDeleteI)
      throws Throwable {
    MaschinenkostenDto dto = new MaschinenkostenDto();
    dto.setIId( (Integer) getKeyWhenDetailPanel());
    DelegateFactory.getInstance().getZeiterfassungDelegate().removeMaschinenkosten(dto);
    this.setKeyWhenDetailPanel(null);
    super.eventActionDelete(e, false, false);
    maschinenkostenDto = new MaschinenkostenDto();
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

    wlaLager.setText(LPMain.getTextRespectUISPr("lp.gueltigab"));
    getInternalFrame().addItemChangedListener(this);

    wlaLagerart.setToolTipText("");
    wlaLagerart.setText(LPMain.getTextRespectUISPr(
        "pers.personalgehalt.stundensatz"));

    wlaWaehrung.setText(DelegateFactory.getInstance().getMandantDelegate().
                        mandantFindByPrimaryKey(LPMain.
                                                getTheClient().getMandant()).
                        getWaehrungCNr());
    ;
    wdfGueltigab.setMandatoryField(true);
    wnfStundensatz.setMandatoryField(true);
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
    jpaWorkingOn.add(wlaKaufdatum,
                     new GridBagConstraints(0, 0,1, 1, 0, 0.0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.HORIZONTAL,
                                            new Insets(2, 2, 2, 2), 0, 0));

    jpaWorkingOn.add(wlaLager,
                     new GridBagConstraints(0, 1, 1, 1, 0.05, 0.0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.HORIZONTAL,
                                            new Insets(2, 2, 2, 2), 0, 0));
    jpaWorkingOn.add(wlaLagerart,
                     new GridBagConstraints(3, 1, 1, 1, 0.05, 0.0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.HORIZONTAL,
                                            new Insets(2, 2, 2, 2), 0, 0));
    jpaWorkingOn.add(wnfStundensatz,
                     new GridBagConstraints(4, 1, 1, 1, 0.0, 0.0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.NONE,
                                            new Insets(0, 0, 0, 0), 0, 0));
    jpaWorkingOn.add(wdfGueltigab,
                     new GridBagConstraints(1, 1, 1, 1, 0.1, 0.0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.HORIZONTAL,
                                            new Insets(0, 0, 0, 0), 0, 0));
    jpaWorkingOn.add(wlaWaehrung,
                     new GridBagConstraints(5, 1, 1, 1, 0, 0.0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.HORIZONTAL,
                                            new Insets(0, 0, 0, 0), 20, 0));
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
    return HelperClient.LOCKME_MASCHINE;
  }

}
