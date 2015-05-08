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
package com.lp.client.personal;


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
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.WrapperComboBox;
import com.lp.client.frame.component.WrapperDateField;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.personal.service.PersonalFac;
import com.lp.server.personal.service.PersonalangehoerigeDto;

@SuppressWarnings("static-access")
public class PanelPersonalangehoerige
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
  private InternalFramePersonal internalFramePersonal = null;
  private WrapperLabel wlaName = new WrapperLabel();
  private WrapperTextField wtfName = new WrapperTextField();
  private PersonalangehoerigeDto personalangehoerigeDto = null;
  private WrapperLabel wlaVorname = new WrapperLabel();
  private WrapperTextField wtfVorname = new WrapperTextField();
  private WrapperLabel wlaAngehoerigenart = new WrapperLabel();
  private WrapperComboBox wcoAngehoerigenart = new WrapperComboBox();
  private WrapperLabel wlaGeburtsdatum = new WrapperLabel();
  private WrapperLabel wlaSolialversicherungsnummer = new WrapperLabel();
  private WrapperDateField wdfGeburtsdatum = new WrapperDateField();
  private WrapperTextField wtfSozialversicherungsnummer = new WrapperTextField();
  private String lastAngehoerigenart = null;

  public PanelPersonalangehoerige(InternalFrame internalFrame, String add2TitleI,
                                  Object pk)
      throws Throwable {
    super(internalFrame, add2TitleI, pk);
    internalFramePersonal = (InternalFramePersonal) internalFrame;
    jbInit();
    setDefaults();
    initComponents();
    enableAllComponents(this, false);
  }


  protected JComponent getFirstFocusableComponent()
      throws Exception {
    return wtfVorname;
  }


  protected void setDefaults()
      throws Throwable {
    wcoAngehoerigenart.setMap(DelegateFactory.getInstance().getPersonalDelegate().
                              getAllSprAngehoerigenarten());
  }


  public void eventActionNew(EventObject eventObject, boolean bLockMeI,
                             boolean bNeedNoNewI)
      throws Throwable {
    super.eventActionNew(eventObject, true, false);
    personalangehoerigeDto = new PersonalangehoerigeDto();
    // getInternalFrame().setKeyWasForLockMe(LPMain.getLockMeForNew());

    leereAlleFelder(this);
    if (lastAngehoerigenart != null) {
      wcoAngehoerigenart.setKeyOfSelectedItem(lastAngehoerigenart);
    }
  }


  /**
   * Hier kommen die events meiner speziellen Buttons an.
   *
   * @param e ActionEvent
   * @throws Throwable
   */
  protected void eventActionSpecial(ActionEvent e)
      throws Throwable {
  }


  protected void eventActionDelete(ActionEvent e, boolean bAdministrateLockKeyI,
                                   boolean bNeedNoDeleteI)
      throws Throwable {
    DelegateFactory.getInstance().getPersonalDelegate().removePersonalangehoerige(
        personalangehoerigeDto.getIId());
    this.setKeyWhenDetailPanel(null);
    super.eventActionDelete(e, false, false);
  }


  protected void components2Dto() {
    personalangehoerigeDto.setPersonalIId(internalFramePersonal.getPersonalDto().
                                          getIId());
    personalangehoerigeDto.setAngehoerigenartCNr( (String) wcoAngehoerigenart.
                                                 getKeyOfSelectedItem());
    personalangehoerigeDto.setCName(wtfName.getText());
    personalangehoerigeDto.setCSozialversnr(wtfSozialversicherungsnummer.getText());
    personalangehoerigeDto.setCVorname(wtfVorname.getText());
    personalangehoerigeDto.setTGeburtsdatum(wdfGeburtsdatum.getTimestamp());

  }


  protected void dto2Components() {
    wtfName.setText(personalangehoerigeDto.getCName());
    wtfVorname.setText(personalangehoerigeDto.getCVorname());
    wtfSozialversicherungsnummer.setText(personalangehoerigeDto.getCSozialversnr());
    wcoAngehoerigenart.setKeyOfSelectedItem(personalangehoerigeDto.getAngehoerigenartCNr());
    wdfGeburtsdatum.setTimestamp(personalangehoerigeDto.getTGeburtsdatum());

  }


  public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
      throws Throwable {
    if (allMandatoryFieldsSetDlg()) {
      components2Dto();
      if (personalangehoerigeDto.getIId() == null) {
        personalangehoerigeDto.setIId(DelegateFactory.getInstance().getPersonalDelegate().
                                      createPersonalangehoerige(personalangehoerigeDto));
        setKeyWhenDetailPanel(personalangehoerigeDto.getIId());
      }
      else {
        DelegateFactory.getInstance().getPersonalDelegate().updatePersonalangehoerige(
            personalangehoerigeDto);
      }
      lastAngehoerigenart = personalangehoerigeDto.getAngehoerigenartCNr();
      super.eventActionSave(e, true);

      if (getInternalFrame().getKeyWasForLockMe() == null) {
        getInternalFrame().setKeyWasForLockMe(internalFramePersonal.getPersonalDto().
                                              getIId() + "");
      }
      eventYouAreSelected(false);
    }
  }


  protected void eventItemchanged(EventObject eI)
      throws Throwable {
    ItemChangedEvent e = (ItemChangedEvent) eI;
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

    wlaName.setText(LPMain.getInstance().getTextRespectUISPr("lp.name"));
    wtfName.setColumnsMax(PersonalFac.MAX_PERSONALANGEHOERIGE_NACHNAME);
    wtfName.setText("");
    wtfName.setMandatoryField(true);

    wlaVorname.setText(LPMain.getInstance().getTextRespectUISPr("lp.vorname"));
    wtfVorname.setToolTipText("");
    wtfVorname.setMandatoryField(true);
    wtfVorname.setColumnsMax(PersonalFac.MAX_PERSONALANGEHOERIGE_VORNAME);
    getInternalFrame().addItemChangedListener(this);
    wlaAngehoerigenart.setText(LPMain.getInstance().getTextRespectUISPr(
        "pers.personalangehoerige.angehoerigenart"));
    wlaGeburtsdatum.setText(LPMain.getInstance().getTextRespectUISPr(
        "pers.personalangehoerige.geburtsdatum"));
    wlaSolialversicherungsnummer.setText(LPMain.getInstance().getTextRespectUISPr(
        "pers.personalangehoerige.sozialversicherungsnummer"));
    wtfSozialversicherungsnummer.setText("");
    wtfSozialversicherungsnummer.setColumnsMax(PersonalFac.
                                               MAX_PERSONALANGEHOERIGE_SOZIALVERSNR);

    wcoAngehoerigenart.setMandatoryField(true);
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
    jpaWorkingOn.add(wlaName,
                        new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
                                               , GridBagConstraints.CENTER,
                                               GridBagConstraints.HORIZONTAL,
                                               new Insets(2, 2, 2, 2), 0, 0));
    jpaWorkingOn.add(wtfName,
                        new GridBagConstraints(1, 1, 1, 1, 0.3, 0.0
                                               , GridBagConstraints.CENTER,
                                               GridBagConstraints.HORIZONTAL,
                                               new Insets(2, 2, 2, 2), 0, 0));
    jpaWorkingOn.add(wlaAngehoerigenart, new GridBagConstraints(0, 0, 1, 1, 0.1, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0),
        0, 0));
    jpaWorkingOn.add(wcoAngehoerigenart, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
        , GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 150,
        0));
    jpaWorkingOn.add(wlaVorname, new GridBagConstraints(0, 2, 1, 1, 0.1, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
        0, 0));
    jpaWorkingOn.add(wtfVorname, new GridBagConstraints(1, 2, 3, 1, 0.1, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
        0, 0));
    jpaWorkingOn.add(wlaGeburtsdatum, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0),
        0, 0));
    jpaWorkingOn.add(wlaSolialversicherungsnummer,
                        new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0
                                               , GridBagConstraints.CENTER,
                                               GridBagConstraints.HORIZONTAL,
                                               new Insets(0, 0, 0, 0), 0, 0));
    jpaWorkingOn.add(wdfGeburtsdatum, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0
        , GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 100,
        0));
    jpaWorkingOn.add(wtfSozialversicherungsnummer,
                        new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0
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
    return HelperClient.LOCKME_PERSONAL;
  }


  public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
      throws Throwable {

    super.eventYouAreSelected(false);
    Object key = getKeyWhenDetailPanel();

    if (key == null
        || (key.equals(LPMain.getLockMeForNew()))) {

      leereAlleFelder(this);
      wtfName.setText(internalFramePersonal.getPersonalDto().getPartnerDto().
                      getCName1nachnamefirmazeile1());

      clearStatusbar();
    }
    else {
      personalangehoerigeDto = DelegateFactory.getInstance().getPersonalDelegate().
          personalangehoerigeFindByPrimaryKey( (Integer) key);

      dto2Components();

    }
  }

}
