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
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.system.SystemFilterFactory;
import com.lp.client.zutritt.DialogReadFingerprint;
import com.lp.server.personal.service.FingerartDto;
import com.lp.server.personal.service.PersonalfingerDto;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;

@SuppressWarnings("static-access")
public class PanelFinger
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
  private WrapperButton wbuTemplate = new WrapperButton();
  private WrapperTextField wtfTemplate = new WrapperTextField();
  private WrapperButton wbuFingerart = new WrapperButton();
  private WrapperTextField wtfFingerart = new WrapperTextField();
  private PanelQueryFLR panelQueryFLRFingerart = null;
  static final public String ACTION_SPECIAL_READ_FINGERPRINT =
      "ACTION_SPECIAL_READ_FINGERPRINT";

  static final public String ACTION_SPECIAL_FINGERART_FROM_LISTE =
      "ACTION_SPECIAL_FINGERART_FROM_LISTE";

  private PersonalfingerDto personalfingerDto = null;
  public PanelFinger(InternalFrame internalFrame, String add2TitleI,
                     Object pk)
      throws Throwable {
    super(internalFrame, add2TitleI, pk);
    jbInit();
    setDefaults();
    initComponents();
    enableAllComponents(this, false);
  }


  protected void setDefaults() {

  }


  protected JComponent getFirstFocusableComponent()
      throws Exception {
    return wtfTemplate;
  }


  public void eventActionNew(EventObject eventObject, boolean bLockMeI,
                             boolean bNeedNoNewI)
      throws Throwable {
    super.eventActionNew(eventObject, true, false);
    personalfingerDto = new PersonalfingerDto();
    leereAlleFelder(this);
  }


  /**
   * Hier kommen die events meiner speziellen Buttons an.
   * @param e ActionEvent
   * @throws Throwable
   */
  protected void eventActionSpecial(ActionEvent e)
      throws Throwable {
    if (e.getActionCommand().equals(
        ACTION_SPECIAL_READ_FINGERPRINT)) {
      DialogReadFingerprint dialog = new DialogReadFingerprint(getInternalFrame());
      dialog.setSize(500, 500);
      dialog.setVisible(true);

      personalfingerDto.setOTemplate1(dialog.getTemplate1());

      if (dialog.getTemplate1() != null) {
        wtfTemplate.setText("Bin\u00E4r");
      } else {
        wtfTemplate.setText(null);
      }

      personalfingerDto.setOTemplate2(dialog.getTemplate2());
    }
    if (e.getActionCommand().equals(ACTION_SPECIAL_FINGERART_FROM_LISTE)) {
      dialogQueryMaschinengruppeFromListe(e);
    }
  }


  void dialogQueryMaschinengruppeFromListe(ActionEvent e)
      throws Throwable {
    String[] aWhichButtonIUse = {
        PanelBasis.ACTION_REFRESH, PanelBasis.ACTION_LEEREN};

    panelQueryFLRFingerart = new PanelQueryFLR(
        null,
        null,
        QueryParameters.UC_ID_FINGERART,
        aWhichButtonIUse,
        getInternalFrame(),
        LPMain.getInstance().getTextRespectUISPr(
            "pers.zutritt.fingerart"));
    panelQueryFLRFingerart.befuellePanelFilterkriterienDirekt(SystemFilterFactory.
        getInstance().createFKDBezeichnung(), null);
    panelQueryFLRFingerart.setSelectedId(personalfingerDto.
                                         getFingerartIId());

    new DialogQuery(panelQueryFLRFingerart);
  }


  protected void eventActionDelete(ActionEvent e, boolean bAdministrateLockKeyI,
                                   boolean bNeedNoDeleteI)
      throws
      Throwable {
    DelegateFactory.getInstance().getZutrittDelegate().removePersonalfinger(personalfingerDto);
    this.setKeyWhenDetailPanel(null);
    super.eventActionDelete(e, false, false);
  }


  protected void components2Dto() {
    personalfingerDto.setPersonalIId( ( (InternalFramePersonal) getInternalFrame()).
                                     getPersonalDto().getIId());

  }


  protected void dto2Components()
      throws Throwable {
    wtfTemplate.setText("Bin\u00E4r");
    FingerartDto fingerartDto = DelegateFactory.getInstance().
        getZutrittDelegate().
        fingerartFindByPrimaryKey(personalfingerDto.getFingerartIId());
    wtfFingerart.setText(fingerartDto.getCBez());
  }


  public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
      throws
      Throwable {
    if (allMandatoryFieldsSetDlg()) {
      components2Dto();
      if (personalfingerDto.getIId() == null) {
        personalfingerDto.setIId(DelegateFactory.getInstance().getZutrittDelegate().
                                 createPersonalfinger(personalfingerDto));
        setKeyWhenDetailPanel(personalfingerDto.getIId());
      }
      else {
        DelegateFactory.getInstance().getZutrittDelegate().updatePersonalfinger(
            personalfingerDto);
      }
      super.eventActionSave(e, true);

      if (getInternalFrame().getKeyWasForLockMe() == null) {
        getInternalFrame().setKeyWasForLockMe(personalfingerDto.getIId() + "");
      }
      eventYouAreSelected(false);
    }
  }


  protected void eventItemchanged(EventObject eI)
      throws Throwable {
    ItemChangedEvent e = (ItemChangedEvent) eI;
    if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
      if (e.getSource() == panelQueryFLRFingerart) {
        Object key = ( (ISourceEvent) e.getSource()).getIdSelected();
        FingerartDto fingerartDto = DelegateFactory.getInstance().
            getZutrittDelegate().
            fingerartFindByPrimaryKey( (Integer)
                                      key);
        wtfFingerart.setText(fingerartDto.getCBez());
        personalfingerDto.setFingerartIId(
            fingerartDto.getIId());
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

    wbuTemplate.setText("Template");
    wbuTemplate.setActionCommand(ACTION_SPECIAL_READ_FINGERPRINT);
    wbuTemplate.addActionListener(this);

    wbuFingerart.setText(LPMain.getInstance().getTextRespectUISPr(
        "pers.zutritt.fingerart") + "...");
    wbuFingerart.setActionCommand(ACTION_SPECIAL_FINGERART_FROM_LISTE);
    wbuFingerart.addActionListener(this);

    wtfFingerart.setMandatoryField(true);
    wtfFingerart.setActivatable(false);

    wtfTemplate.setText("");
    wtfTemplate.setMandatoryField(true);
    wtfTemplate.setActivatable(false);
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
    jpaWorkingOn.add(wbuTemplate,
                     new GridBagConstraints(0, 0, 1, 1, 0.1, 0.0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.HORIZONTAL,
                                            new Insets(2, 2, 2, 2), 0, 0));
    jpaWorkingOn.add(wtfTemplate,
                     new GridBagConstraints(1, 0, 1, 1, 0.3, 0.0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.HORIZONTAL,
                                            new Insets(2, 2, 2, 2), 0, 0));
    jpaWorkingOn.add(wbuFingerart,
                     new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.HORIZONTAL,
                                            new Insets(2, 2, 2, 2), 0, 0));
    jpaWorkingOn.add(wtfFingerart,
                     new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
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
    return HelperClient.LOCKME_BERUF;
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
      personalfingerDto = DelegateFactory.getInstance().getZutrittDelegate().
          personalfingerFindByPrimaryKey( (Integer) key);

      dto2Components();

    }
  }

}
