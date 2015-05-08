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
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperSpinner;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.personal.service.PersonalFac;
import com.lp.server.personal.service.TagesartDto;
import com.lp.server.personal.service.TagesartsprDto;

@SuppressWarnings("static-access")
public class PanelTagesart
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
  private WrapperLabel wlaKennung = new WrapperLabel();
  private WrapperTextField wtfKennung = new WrapperTextField();
  private TagesartDto tagesartDto = null;
  private WrapperLabel wlaBezeichnung = new WrapperLabel();
  private WrapperTextField wtfBezeichnung = new WrapperTextField();
  private WrapperLabel wlaReihung = new WrapperLabel();
  private WrapperSpinner wspReihung = new WrapperSpinner(new Integer(0), new Integer(0),
                                                 new Integer(9999), new Integer(1));
  public PanelTagesart(InternalFrame internalFrame, String add2TitleI,
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
    return wtfKennung;
  }


  public void eventActionNew(EventObject eventObject, boolean bLockMeI,
                             boolean bNeedNoNewI)
      throws Throwable {
    super.eventActionNew(eventObject, true, false);
    tagesartDto = new TagesartDto();
    // getInternalFrame().setKeyWasForLockMe(LPMain.getLockMeForNew());

    leereAlleFelder(this);
  }


  protected void eventActionSpecial(ActionEvent e)
      throws Throwable {
  }


  protected void eventActionDelete(ActionEvent e, boolean bAdministrateLockKeyI,
                                   boolean bNeedNoDeleteI)
      throws Throwable {
    DelegateFactory.getInstance().getZeiterfassungDelegate().removeTagesart(tagesartDto);
    this.setKeyWhenDetailPanel(null);
    super.eventActionDelete(e, false, false);
  }


  protected void components2Dto() {
    tagesartDto.setCNr(wtfKennung.getText());
    if (tagesartDto.getTagesartsprDto() == null) {
      tagesartDto.setTagesartsprDto(new TagesartsprDto());
    }
    tagesartDto.getTagesartsprDto().setCBez(wtfBezeichnung.
                                            getText());
    tagesartDto.setISort( (Integer) wspReihung.getValue());

  }


  protected void dto2Components() {
    wtfKennung.setText(tagesartDto.getCNr());
    wspReihung.setValue(tagesartDto.getISort());
    if (tagesartDto.getTagesartsprDto() != null) {
      wtfBezeichnung.setText(tagesartDto.getTagesartsprDto().getCBez());
    }
    else {
      wtfBezeichnung.setText("");
    }
  }


  public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
      throws Throwable {
    if (allMandatoryFieldsSetDlg()) {
      components2Dto();
      if (tagesartDto.getIId() == null) {
        tagesartDto.setIId(DelegateFactory.getInstance().getZeiterfassungDelegate().
                           createTagesart(tagesartDto));
        setKeyWhenDetailPanel(tagesartDto.getIId());
      }
      else {
        DelegateFactory.getInstance().getZeiterfassungDelegate().updateTagesart(tagesartDto);
      }
      super.eventActionSave(e, true);

      if (getInternalFrame().getKeyWasForLockMe() == null) {
        getInternalFrame().setKeyWasForLockMe(tagesartDto.getCNr());
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

    wlaKennung.setText(LPMain.getInstance().getTextRespectUISPr("label.kennung"));
    wtfKennung.setColumnsMax(PersonalFac.MAX_TAGESART_KENNUNG);
    wtfKennung.setText("");
    wtfKennung.setMandatoryField(true);

    wlaBezeichnung.setText(LPMain.getInstance().getTextRespectUISPr("lp.bezeichnung"));
    wtfBezeichnung.setToolTipText("");
    wtfBezeichnung.setColumnsMax(PersonalFac.MAX_TAGESART_BEZEICHNUNG);
    wtfBezeichnung.setText("");
    getInternalFrame().addItemChangedListener(this);
    wlaReihung.setText(LPMain.getInstance().getTextRespectUISPr("lp.reihung"));
    wspReihung.setMandatoryField(true);
    this.add(jpaButtonAction, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
        , GridBagConstraints.WEST, GridBagConstraints.NONE,
        new Insets(0, 0, 0, 0), 0, 0));
    this.add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.BOTH,
        new Insets(0, 0, 0, 0), 0, 0));

    //jetzt meine felder
    jpaWorkingOn = new JPanel();
    gridBagLayoutWorkingPanel = new GridBagLayout();
    jpaWorkingOn.setLayout(gridBagLayoutWorkingPanel);
    this.add(jpaWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0
        , GridBagConstraints.SOUTHEAST, GridBagConstraints.BOTH,
        new Insets(0, 0, 0, 0), 0, 0));
    jpaWorkingOn.add(wlaKennung,
                        new GridBagConstraints(0, 0, 1, 1, 0.1, 0.0
                                               , GridBagConstraints.CENTER,
                                               GridBagConstraints.HORIZONTAL,
                                               new Insets(2, 2, 2, 2), 0, 0));
    jpaWorkingOn.add(wtfKennung,
                        new GridBagConstraints(1, 0, 1, 1, 0.15, 0.0
                                               , GridBagConstraints.CENTER,
                                               GridBagConstraints.HORIZONTAL,
                                               new Insets(2, 2, 2, 2), 0, 0));
    jpaWorkingOn.add(wlaBezeichnung,
                        new GridBagConstraints(2, 0, 1, 1, 0.1, 0.0
                                               , GridBagConstraints.CENTER,
                                               GridBagConstraints.HORIZONTAL,
                                               new Insets(2, 2, 2, 2), 0, 0));
    jpaWorkingOn.add(wtfBezeichnung,
                        new GridBagConstraints(3, 0, 1, 1, 0.3, 0.0
                                               , GridBagConstraints.CENTER,
                                               GridBagConstraints.HORIZONTAL,
                                               new Insets(2, 2, 2, 2), 0, 0));
    jpaWorkingOn.add(wlaReihung, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
        0, 0));
    jpaWorkingOn.add(wspReihung, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
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
    return HelperClient.LOCKME_TAGESART;
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
      tagesartDto = DelegateFactory.getInstance().getZeiterfassungDelegate().
          tagesartFindByPrimaryKey( (Integer) key);

      dto2Components();
    }
  }
}
