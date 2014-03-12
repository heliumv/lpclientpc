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

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.border.Border;

import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.system.service.OrtDto;
import com.lp.server.system.service.SystemFac;

@SuppressWarnings("static-access")
/**
 * <p><I>Diese Klasse kuemmert sich ...</I> </p>
 *
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 *
 * <p>Erstellungsdatum <I>18.02.05</I></p>
 *
 * <p> </p>
 *
 * @author josef erlinger
 * @version $Revision: 1.3 $
 */
public class PanelOrt
    extends PanelBasis
{
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

private OrtDto ortDto = new OrtDto();

  //von hier ...
  private GridBagLayout gridBagLayoutAll = null;
  private JPanel jPanelWorkingOn = new JPanel();
  private JPanel panelButtonAction = null;
  private Border border = null;
  private GridBagLayout gridBagLayoutWorkingPanel = null;
  private WrapperLabel wlaOrt = new WrapperLabel();
  private WrapperTextField wtfOrt = new WrapperTextField();

  public PanelOrt(InternalFrame internalFrame, String add2TitleI, Object pk)
      throws Throwable {
    super(internalFrame, add2TitleI, pk);
    initPanel();
    jbInit();
    initComponents();
  }


  //Intitialwerte von panel setzen (beim ersten laden)
  private void initPanel() {

  }


  protected void eventActionSpecial(ActionEvent e)
      throws Throwable {
  }


  public void eventActionNew(EventObject eventObject, boolean bLockMeI,
                             boolean bNeedNoNewI)
      throws Throwable {
    super.eventActionNew(eventObject, true, false);

    // getInternalFrame().setKeyWasForLockMe(LPMain.getLockMeForNew());

    //hier wird Id null gesetzt fuer update
    getOrtDto().setIId(null);
    leereAlleFelder(this);
  }


  protected void eventActionDelete(ActionEvent e, boolean bAdministrateLockKeyI,
                                   boolean bNeedNoDeleteI)
      throws Throwable {
    DelegateFactory.getInstance().getSystemDelegate().removeOrt(getOrtDto());
    super.eventActionDelete(e, false, false);
  }


  protected void components2Dto() {
    getOrtDto().setCName(wtfOrt.getText());
  }


  protected void dto2Components() {
    wtfOrt.setText(getOrtDto().getCName());
  }


  public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
      throws Throwable {

    if (allMandatoryFieldsSetDlg()) {
      Integer iIdOrt = null;

      // Dto befuellen.
      components2Dto();
      if (getOrtDto().getIId() == null) {
        // create.
        iIdOrt = DelegateFactory.getInstance().getSystemDelegate().createOrt(getOrtDto());

        // dem dto den key setzen.
        DelegateFactory.getInstance().getSystemDelegate().ortFindByPrimaryKey(iIdOrt);

        // diesem panel den key setzen.
        setKeyWhenDetailPanel(iIdOrt);
      }
      else {
        // update
        DelegateFactory.getInstance().getSystemDelegate().updateOrt(getOrtDto());
      }

      super.eventActionSave(e, true);

      eventYouAreSelected(false);
    }

  }


  protected void eventItemchanged(EventObject eI)
      throws Throwable {
    ItemChangedEvent e = (ItemChangedEvent) eI;
  }


  private void jbInit()
      throws Throwable {
    getInternalFrame().addItemChangedListener(this);

    String[] aWhichButtonIUse = {
        ACTION_UPDATE,
        ACTION_SAVE,
        ACTION_DELETE,
        ACTION_DISCARD,
    };

    enableToolsPanelButtons(aWhichButtonIUse);

    border = BorderFactory.createEmptyBorder(10, 10, 10, 10);
    setBorder(border);
    //das Aussenpanel hat immer das Gridbaglayout.
    gridBagLayoutAll = new GridBagLayout();
    setLayout(gridBagLayoutAll);

    //Actionpanel von Oberklasse holen und anhaengen.
    panelButtonAction = getToolsPanel();
    this.add(panelButtonAction, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
        , GridBagConstraints.WEST, GridBagConstraints.NONE,
        new Insets(0, 0, 0, 0), 0, 0));

    wlaOrt.setText(LPMain.getInstance().getTextRespectUISPr("lp.label.ort"));

    wtfOrt.setColumnsMax(SystemFac.MAX_ORT);
    wtfOrt.setMandatoryField(true);

    //jetzt meine felder
    jPanelWorkingOn = new JPanel();
    gridBagLayoutWorkingPanel = new GridBagLayout();
    jPanelWorkingOn.setLayout(gridBagLayoutWorkingPanel);
    add(jPanelWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0
                                                , GridBagConstraints.SOUTHEAST,
                                                GridBagConstraints.BOTH,
                                                new Insets(0, 0, 0, 0), 0, 0));

    add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.BOTH,
        new Insets(0, 0, 0, 0), 0, 0));

    jPanelWorkingOn.add(wlaOrt,
                        new GridBagConstraints(0, 1, 1, 1, 0.1, 0.0
                                               , GridBagConstraints.CENTER,
                                               GridBagConstraints.HORIZONTAL,
                                               new Insets(2, 2, 2, 2), 0, 0));
    jPanelWorkingOn.add(wtfOrt,
                        new GridBagConstraints(1, 1, 1, 1, 0.1, 0.0
                                               , GridBagConstraints.CENTER,
                                               GridBagConstraints.HORIZONTAL,
                                               new Insets(2, 2, 2, 2), 0, 0));
  }


  protected String getLockMeWer()
      throws Exception {
    return HelperClient.LOCKME_ORT;
  }


  public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
      throws Throwable {

    super.eventYouAreSelected(false);

    Object key = getKeyWhenDetailPanel();

    if (key == null || (key != null && key.equals(LPMain.getLockMeForNew()))) {

      leereAlleFelder(this);
      clearStatusbar();
    }
    else {
      setOrtDto(DelegateFactory.getInstance().getSystemDelegate().ortFindByPrimaryKey( (
          Integer) key));
      setStatusbar();
      dto2Components();
    }
  }


//hier erfolgt setting der statusbar (lt. felder in der db)
  protected void setStatusbar()
      throws Throwable {
    setStatusbarTAendern(getOrtDto().getTAendern());
  }

  public OrtDto getOrtDto() {
    return ortDto;
  }


  public void setOrtDto(OrtDto ortDto) {
    this.ortDto = ortDto;
  }

  protected JComponent getFirstFocusableComponent()
     throws Exception {
   return wtfOrt;
 }

}
