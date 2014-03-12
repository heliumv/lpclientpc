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
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperDateField;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.system.SystemFilterFactory;
import com.lp.server.personal.service.PersonalzutrittsklasseDto;
import com.lp.server.personal.service.ZutrittsklasseDto;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;

@SuppressWarnings("static-access")
public class PanelPersonalzutrittsklasse
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
  private WrapperLabel wlaGueltigAb = new WrapperLabel();
  private WrapperDateField wdfGueltigab = new WrapperDateField();
  private PersonalzutrittsklasseDto personalzutrittsklasseDto = null;
  private WrapperButton wbuZutrittsklasse = new WrapperButton();
  private WrapperTextField wtfZutrittsklasse = new WrapperTextField();
  private WrapperTextField wtfZutrittsklasseBezeichnung = new WrapperTextField();

  private PanelQueryFLR panelQueryFLRZutrittsklasse = null;

  static final public String ACTION_SPECIAL_ZUTRITTSKLASSE_FROM_LISTE =
      "ACTION_SPECIAL_ZUTRITTSKLASSE_FROM_LISTE";

  public PanelPersonalzutrittsklasse(InternalFrame internalFrame, String add2TitleI,
                                 Object pk)
      throws Throwable {
    super(internalFrame, add2TitleI, pk);
    internalFramePersonal = (InternalFramePersonal) internalFrame;
    jbInit();
    setDefaults();
    initComponents();
    enableAllComponents(this, false);
  }


  protected void setDefaults() {
  }

  protected JComponent getFirstFocusableComponent()
      throws Exception {
    return wbuZutrittsklasse;
  }

  void dialogQueryZutrittsklasseFromListe(ActionEvent e)
      throws Throwable {
    FilterKriterium[] filtersMandant = new FilterKriterium[1];
    String mandant = "'" + LPMain.getInstance().getTheClient().getMandant() +
        "'";
    FilterKriterium krit1 = new FilterKriterium("mandant_c_nr",
                                                true,
                                                mandant,
                                                FilterKriterium.OPERATOR_EQUAL, false);
    filtersMandant[0] = krit1;

    String[] aWhichButtonIUse = {
        PanelBasis.ACTION_REFRESH};
    panelQueryFLRZutrittsklasse = new PanelQueryFLR(
        null,
        SystemFilterFactory.getInstance().createFKMandantCNr(),
        QueryParameters.UC_ID_ZUTRITTSKLASSE,
        aWhichButtonIUse,
        internalFramePersonal,
        LPMain.getInstance().getTextRespectUISPr("pers.zutritt.zutrittsklasse"));


    panelQueryFLRZutrittsklasse.setSelectedId(personalzutrittsklasseDto.getZutrittsklasseIId());

    new DialogQuery(panelQueryFLRZutrittsklasse);
  }


  public void eventActionNew(EventObject eventObject, boolean bLockMeI,
                             boolean bNeedNoNewI)
      throws Throwable {
    super.eventActionNew(eventObject, true, false);
    personalzutrittsklasseDto = new PersonalzutrittsklasseDto();
    // getInternalFrame().setKeyWasForLockMe(LPMain.getLockMeForNew());

    leereAlleFelder(this);
    wdfGueltigab.setTimestamp(new java.sql.Timestamp(System.currentTimeMillis()));
  }


  /**
   * Hier kommen die events meiner speziellen Buttons an.
   *
   * @param e ActionEvent
   * @throws Throwable
   */
  protected void eventActionSpecial(ActionEvent e)
      throws Throwable {
    if (e.getActionCommand().equals(ACTION_SPECIAL_ZUTRITTSKLASSE_FROM_LISTE)) {
      dialogQueryZutrittsklasseFromListe(e);
    }
  }


  protected void eventActionDelete(ActionEvent e, boolean bAdministrateLockKeyI,
                                   boolean bNeedNoDeleteI)
      throws Throwable {
    DelegateFactory.getInstance().getZutrittDelegate().removePersonalzutrittsklasse(
        personalzutrittsklasseDto);
    this.setKeyWhenDetailPanel(null);
    super.eventActionDelete(e, false, false);
  }


  protected void components2Dto() {
    personalzutrittsklasseDto.setTGueltigab(wdfGueltigab.getTimestamp());
    personalzutrittsklasseDto.setPersonalIId(internalFramePersonal.getPersonalDto().getIId());
  }


  protected void dto2Components() throws Throwable {
    wdfGueltigab.setTimestamp(personalzutrittsklasseDto.getTGueltigab());

    ZutrittsklasseDto zutrittsklasseDto = DelegateFactory.getInstance().getZutrittDelegate().
            zutrittsklasseFindByPrimaryKey( personalzutrittsklasseDto.getZutrittsklasseIId());

    wtfZutrittsklasse.setText(zutrittsklasseDto.getCNr());
      wtfZutrittsklasseBezeichnung.setText(zutrittsklasseDto.getCBez());
  }


  public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
      throws Throwable {
    if (allMandatoryFieldsSetDlg()) {
      components2Dto();
      if (personalzutrittsklasseDto.getIId() == null) {
        personalzutrittsklasseDto.setIId(DelegateFactory.getInstance().getZutrittDelegate().
                                     createPersonalzutrittsklasse(personalzutrittsklasseDto));
        setKeyWhenDetailPanel(personalzutrittsklasseDto.getIId());
      }
      else {
        DelegateFactory.getInstance().getZutrittDelegate().updatePersonalzutrittsklasse(
            personalzutrittsklasseDto);
      }
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
    if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
      if (e.getSource() == panelQueryFLRZutrittsklasse) {
        Object key = ( (ISourceEvent) e.getSource()).getIdSelected();
        ZutrittsklasseDto zutrittsklasseDto = DelegateFactory.getInstance().getZutrittDelegate().
            zutrittsklasseFindByPrimaryKey( (Integer) key);
        wtfZutrittsklasse.setText(zutrittsklasseDto.getCNr());
          wtfZutrittsklasseBezeichnung.setText(zutrittsklasseDto.getCBez());
        personalzutrittsklasseDto.setZutrittsklasseIId(zutrittsklasseDto.getIId());
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

    wlaGueltigAb.setText(LPMain.getInstance().getTextRespectUISPr("lp.gueltigab"));
    wdfGueltigab.setMandatoryField(true);

    getInternalFrame().addItemChangedListener(this);
    wbuZutrittsklasse.setText(LPMain.getInstance().getTextRespectUISPr("pers.zutritt.zutrittsklasse") +
                          "...");

    wbuZutrittsklasse.setActionCommand(PanelPersonalzutrittsklasse.ACTION_SPECIAL_ZUTRITTSKLASSE_FROM_LISTE);
    wbuZutrittsklasse.addActionListener(this);

    wtfZutrittsklasse.setActivatable(false);
    wtfZutrittsklasse.setMandatoryField(true);
    wtfZutrittsklasse.setText("");
    wtfZutrittsklasseBezeichnung.setActivatable(false);
    wtfZutrittsklasseBezeichnung.setText("");

    jpaWorkingOn = new JPanel();
    gridBagLayoutWorkingPanel = new GridBagLayout();
    jpaWorkingOn.setLayout(gridBagLayoutWorkingPanel);
    this.add(jpaWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0
        , GridBagConstraints.SOUTHEAST, GridBagConstraints.BOTH,
        new Insets(0, 0, 0, 0), 0, 0));

    jpaWorkingOn.add(wlaGueltigAb, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
        0, 0));
    jpaWorkingOn.add(wdfGueltigab, new GridBagConstraints(1, 2, 1, 1, 0.1, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
        0, 0));
    jpaWorkingOn.add(wbuZutrittsklasse, new GridBagConstraints(0, 0, 1, 1, 0.1, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
        0, 0));
    jpaWorkingOn.add(wtfZutrittsklasse, new GridBagConstraints(1, 0, 1, 1, 0.1, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
        0, 0));
    jpaWorkingOn.add(wtfZutrittsklasseBezeichnung,
                        new GridBagConstraints(2, 0, 1, 1, 0.2, 0.0
                                               , GridBagConstraints.CENTER,
                                               GridBagConstraints.HORIZONTAL,
                                               new Insets(2, 2, 2, 2), 0, 0));
    this.add(jpaButtonAction, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
        , GridBagConstraints.WEST, GridBagConstraints.NONE,
        new Insets(0, 0, 0, 0), 0, 0));

    //jetzt meine felder
    jpaWorkingOn = new JPanel();
    gridBagLayoutWorkingPanel = new GridBagLayout();
    jpaWorkingOn.setLayout(gridBagLayoutWorkingPanel);
    this.add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.BOTH,
        new Insets(0, 0, 0, 0), 0, 0));
    this.add(jpaWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0
        , GridBagConstraints.SOUTHEAST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0),
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
    return HelperClient.LOCKME_PERSONAL;
  }


  public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
      throws Throwable {
    super.eventYouAreSelected(false);
    Object key = getKeyWhenDetailPanel();

    if (key == null
        || (key.equals(LPMain.getLockMeForNew()))) {

      leereAlleFelder(this);

      clearStatusbar();
       wdfGueltigab.setTimestamp(new java.sql.Timestamp(System.currentTimeMillis()));
    }
    else {
      personalzutrittsklasseDto = DelegateFactory.getInstance().getZutrittDelegate().
          personalzutrittsklasseFindByPrimaryKey( (Integer) key);

      dto2Components();

    }
  }
}
