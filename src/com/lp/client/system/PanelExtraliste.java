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


import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.EventObject;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.WrapperComboBox;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperTextArea;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.system.service.ExtralisteDto;
import com.lp.server.system.service.SystemFac;

@SuppressWarnings("static-access")
public class PanelExtraliste
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


  private WrapperLabel wlaBelegart=new WrapperLabel();
  private WrapperComboBox wcbBelegart = new WrapperComboBox();

   private WrapperLabel wlaQuery = new WrapperLabel();
  private WrapperTextArea wtaQuery = new WrapperTextArea();

  private WrapperLabel wlaDialogbreite = new WrapperLabel();
   private WrapperNumberField wnfDialogbreite = new WrapperNumberField();

  static final public String ACTION_SPECIAL_BELEGART_FROM_LISTE =
      "action_belegart_from_liste";

  private ExtralisteDto extralisteDto = null;
  public PanelExtraliste(InternalFrame internalFrame, String add2TitleI,
                         Object pk)
      throws Throwable {
    super(internalFrame, add2TitleI, pk);
    jbInit();
    setDefaults();
    initComponents();
    enableAllComponents(this, false);
  }


  private void setDefaults() throws Throwable{
    Map<?, ?> m = DelegateFactory.getInstance().getLocaleDelegate().
        getAllBelegartenUebersetzt(LPMain.getInstance().getTheClient().getLocUi(),LPMain.getInstance().getTheClient().getLocUi());
    wcbBelegart.setMap(m);
  }


  protected JComponent getFirstFocusableComponent()
      throws Exception {
    return wtfBezeichnung;
  }


  public void eventActionNew(EventObject eventObject, boolean bLockMeI,
                             boolean bNeedNoNewI)
      throws Throwable {
    super.eventActionNew(eventObject, true, false);
    extralisteDto = new ExtralisteDto();
    leereAlleFelder(this);
  }


  /**
   * Hier kommen die events meiner speziellen Buttons an.
   * @param e ActionEvent
   * @throws Throwable
   */
  protected void eventActionSpecial(ActionEvent e)
      throws Throwable {
  }


  protected void eventActionDelete(ActionEvent e, boolean bAdministrateLockKeyI,
                                   boolean bNeedNoDeleteI)
      throws
      Throwable {
    DelegateFactory.getInstance().getSystemDelegate().removeExtraliste(extralisteDto);
    this.setKeyWhenDetailPanel(null);
    super.eventActionDelete(e, false, false);
  }


  protected void components2Dto() throws Throwable {
    extralisteDto.setCBez(wtfBezeichnung.getText());
    extralisteDto.setBelegartCNr((String)wcbBelegart.getKeyOfSelectedItem());
    extralisteDto.setXQuery(wtaQuery.getText());
    extralisteDto.setIDialogbreite(wnfDialogbreite.getInteger());

  }


  protected void dto2Components() {
    wtfBezeichnung.setText(extralisteDto.getCBez());
    wcbBelegart.setKeyOfSelectedItem(extralisteDto.getBelegartCNr());
    wtaQuery.setText(extralisteDto.getXQuery());
    wnfDialogbreite.setInteger(extralisteDto.getIDialogbreite());

  }


  public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
      throws
      Throwable {
    if (allMandatoryFieldsSetDlg()) {
      components2Dto();
      if (extralisteDto.getIId() == null) {
        extralisteDto.setCBez(wtfBezeichnung.getText());
        extralisteDto.setIId(DelegateFactory.getInstance().getSystemDelegate().
                             createExtraliste(extralisteDto));
        setKeyWhenDetailPanel(extralisteDto.getIId());
      }
      else {
        DelegateFactory.getInstance().getSystemDelegate().updateExtraliste(extralisteDto);
      }
      super.eventActionSave(e, true);

      if (getInternalFrame().getKeyWasForLockMe() == null) {
        getInternalFrame().setKeyWasForLockMe(extralisteDto.getIId() + "");
      }
      eventYouAreSelected(false);
    }
  }


  protected void eventItemchanged(EventObject eI)
      throws Throwable {
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

    wlaBezeichnung.setText(LPMain.getInstance().getTextRespectUISPr("lp.bezeichnung"));

    wlaBelegart.setText(LPMain.getInstance().getTextRespectUISPr("lp.belegart"));
    wlaQuery.setText("Query");

    wlaQuery.setHorizontalAlignment(SwingConstants.LEFT);

    wlaDialogbreite.setText(LPMain.getInstance().getTextRespectUISPr("system.extraliste.dialogbreite"));

    wnfDialogbreite.setMinimumValue(0);
    wnfDialogbreite.setFractionDigits(0);


    wtfBezeichnung.setColumnsMax(SystemFac.MAX_LAENGE_EXTRALISTE_BEZEICHNUNG);
    wtfBezeichnung.setText("");
    wtfBezeichnung.setMandatoryField(true);
    wcbBelegart.setMandatoryField(true);



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
    jpaWorkingOn.add(wlaBezeichnung,
                        new GridBagConstraints(0, 0, 1, 1, 0.3, 0.0
                                               , GridBagConstraints.CENTER,
                                               GridBagConstraints.HORIZONTAL,
                                               new Insets(2, 2, 2, 2), 0, 0));
    jpaWorkingOn.add(wtfBezeichnung,
                        new GridBagConstraints(1, 0, 1, 1, 0.7, 0.0
                                               , GridBagConstraints.CENTER,
                                               GridBagConstraints.HORIZONTAL,
                                               new Insets(2, 2, 2, 2), 0, 0));
    jpaWorkingOn.add(wlaBelegart,
                       new GridBagConstraints(0, 1, 1, 1, 0, 0.0
                                              , GridBagConstraints.CENTER,
                                              GridBagConstraints.HORIZONTAL,
                                              new Insets(2, 2, 2, 2), 0, 0));
   jpaWorkingOn.add(wcbBelegart,
                       new GridBagConstraints(1,1, 1, 1, 0, 0.0
                                              , GridBagConstraints.WEST,
                                              GridBagConstraints.HORIZONTAL,
                                              new Insets(2, 2, 2, 2), 0, 0));
   jpaWorkingOn.add(wlaDialogbreite,
                   new GridBagConstraints(0, 2, 1, 1, 0, 0.0
                                          , GridBagConstraints.CENTER,
                                          GridBagConstraints.HORIZONTAL,
                                          new Insets(2, 2, 2, 2), 0, 0));
jpaWorkingOn.add(wnfDialogbreite,
                   new GridBagConstraints(1,2, 1, 1, 0, 0
                                          , GridBagConstraints.WEST,
                                          GridBagConstraints.NONE,
                                          new Insets(2, 2, 2, 2), 30, 0));

   jpaWorkingOn.add(wlaQuery,
                      new GridBagConstraints(0, 3, 1, 1, 0, 0.0
                                             , GridBagConstraints.CENTER,
                                             GridBagConstraints.HORIZONTAL,
                                             new Insets(2, 2, 2, 2), 0, 0));

   JScrollPane jScrollPane1 = new JScrollPane();
   jScrollPane1.getViewport().add(wtaQuery);


  jpaWorkingOn.add(jScrollPane1,
                      new GridBagConstraints(0,4, 2, 1, 0, 1.0
                                             , GridBagConstraints.CENTER,
                                             GridBagConstraints.BOTH,
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
    return HelperClient.LOCKME_EXTRALISTE;
  }


  public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
      throws Throwable {

    super.eventYouAreSelected(false);
    Object key = getKeyWhenDetailPanel();

    if (key == null
        || (key != null && key.equals(LPMain.getLockMeForNew()))) {

      leereAlleFelder(this);

      clearStatusbar();
    }
    else {
      extralisteDto = DelegateFactory.getInstance().getSystemDelegate().
          extralisteFindByPrimaryKey( (Integer) key);

      dto2Components();

    }
  }

}
