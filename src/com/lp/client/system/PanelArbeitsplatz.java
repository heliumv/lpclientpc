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
import com.lp.server.system.service.ArbeitsplatzDto;

@SuppressWarnings("static-access")
public class PanelArbeitsplatz
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
  private InternalFrameSystem internalFrameArtikel = null;
  private WrapperLabel wlaPcname = new WrapperLabel();
  private WrapperTextField wtfPcname = new WrapperTextField();
  private WrapperLabel wlaStandort = new WrapperLabel();
  private WrapperTextField wtfStandort = new WrapperTextField();

  private WrapperLabel wlaBemerkung = new WrapperLabel();
  private WrapperTextField wtfBemerkung = new WrapperTextField();


  public PanelArbeitsplatz(InternalFrame internalFrame, String add2TitleI,
                       Object pk)
      throws Throwable {
    super(internalFrame, add2TitleI, pk);
    internalFrameArtikel = (InternalFrameSystem) internalFrame;
    jbInit();
    setDefaults();
    initComponents();
    enableAllComponents(this, false);
  }


  protected void setDefaults() {
  }


  public void eventActionNew(EventObject eventObject, boolean bLockMeI,
                             boolean bNeedNoNewI)
      throws Throwable {
    super.eventActionNew(eventObject, true, false);
    internalFrameArtikel.setArbeitsplatzDto(new ArbeitsplatzDto());
    leereAlleFelder(this);
  }


  protected void eventActionDelete(ActionEvent e, boolean bAdministrateLockKeyI,
                                   boolean bNeedNoDeleteI)
      throws Throwable {
    DelegateFactory.getInstance().getParameterDelegate().removeArbeitsplatz(internalFrameArtikel.getArbeitsplatzDto());
    super.eventActionDelete(e, false, false);
  }


  protected JComponent getFirstFocusableComponent()
      throws Exception {
    return wtfPcname;
  }


  public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
      throws Throwable {

    super.eventYouAreSelected(false);
    Object key = null;

    if (internalFrameArtikel.getArbeitsplatzDto() != null) {
      key = internalFrameArtikel.getArbeitsplatzDto().getIId();
    }

    if (key == null
        || (key.equals(LPMain.getLockMeForNew()))) {
      leereAlleFelder(this);
      clearStatusbar();
      wtfPcname.setText(java.net.InetAddress.getLocalHost().getHostName());
    }
    else {
      internalFrameArtikel.setArbeitsplatzDto(DelegateFactory.getInstance().getParameterDelegate().
          arbeitsplatzFindByPrimaryKey(internalFrameArtikel.getArbeitsplatzDto().getIId()));
      wtfPcname.setText(internalFrameArtikel.getArbeitsplatzDto().getCPcname());
      wtfStandort.setText(internalFrameArtikel.getArbeitsplatzDto().getCStandort());
      wtfBemerkung.setText(internalFrameArtikel.getArbeitsplatzDto().getCBemerkung());
      getInternalFrame().setLpTitle(
          InternalFrame.TITLE_IDX_AS_I_LIKE,
          LPMain.getInstance().getTextRespectUISPr("system.pcname") + ": " +
          internalFrameArtikel.getArbeitsplatzDto().getCPcname());
    }
  }


  protected void eventItemchanged(EventObject eI) {
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

    wlaPcname.setText(LPMain.getInstance().getTextRespectUISPr("system.pcname"));
    wtfPcname.setColumnsMax(20);
    wtfPcname.setText("");
    wtfPcname.setMandatoryField(true);
    getInternalFrame().addItemChangedListener(this);

    wlaStandort.setText(LPMain.getInstance().getTextRespectUISPr("system.standort"));
    wtfStandort.setColumnsMax(80);
    wtfStandort.setText("");

    wlaBemerkung.setText(LPMain.getInstance().getTextRespectUISPr("lp.bemerkung"));
    wtfBemerkung.setColumnsMax(80);


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
    jpaWorkingOn.add(wlaPcname,
                        new GridBagConstraints(0, 0, 1, 1, 0.05, 0.0
                                               , GridBagConstraints.CENTER,
                                               GridBagConstraints.HORIZONTAL,
                                               new Insets(2, 2, 2, 2), 0, 0));
    jpaWorkingOn.add(wtfPcname,
                        new GridBagConstraints(1, 0, 1, 1, 0.1, 0.0
                                               , GridBagConstraints.CENTER,
                                               GridBagConstraints.HORIZONTAL,
                                               new Insets(2, 2, 2, 2), 0, 0));
    jpaWorkingOn.add(wlaStandort,
                        new GridBagConstraints(2, 0, 1, 1, 0.05, 0.0
                                               , GridBagConstraints.CENTER,
                                               GridBagConstraints.HORIZONTAL,
                                               new Insets(2, 2, 2, 2), 0, 0));
    jpaWorkingOn.add(wtfStandort,
                        new GridBagConstraints(3, 0, 1, 1, 0.2, 0.0
                                               , GridBagConstraints.CENTER,
                                               GridBagConstraints.HORIZONTAL,
                                               new Insets(0, 0, 0, 0), 0, 0));

    jpaWorkingOn.add(wlaBemerkung,
                        new GridBagConstraints(0, 1, 1, 1, 0, 0.0
                                               , GridBagConstraints.CENTER,
                                               GridBagConstraints.HORIZONTAL,
                                               new Insets(2, 2, 2, 2), 0, 0));
    jpaWorkingOn.add(wtfBemerkung,
                        new GridBagConstraints(1, 1, 3, 1, 0, 0.0
                                               , GridBagConstraints.CENTER,
                                               GridBagConstraints.HORIZONTAL,
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
    return HelperClient.LOCKME_MATERIAL;
  }


  protected void components2Dto() {
    internalFrameArtikel.getArbeitsplatzDto().setCPcname(wtfPcname.getText());
    internalFrameArtikel.getArbeitsplatzDto().setCStandort(wtfStandort.getText());
    internalFrameArtikel.getArbeitsplatzDto().setCBemerkung(wtfBemerkung.getText());
  }


  public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
      throws
      Throwable {
    if (allMandatoryFieldsSetDlg()) {
      components2Dto();
      if (internalFrameArtikel.getArbeitsplatzDto().getIId() == null) {
        // Create
        internalFrameArtikel.getArbeitsplatzDto().setIId(DelegateFactory.getInstance().getParameterDelegate().
                                                createArbeitsplatz(
            internalFrameArtikel.getArbeitsplatzDto()));
        // diesem panel den key setzen.
        setKeyWhenDetailPanel(internalFrameArtikel.getArbeitsplatzDto().getIId());
      }
      else {
        DelegateFactory.getInstance().getParameterDelegate().updateArbeitsplatz(internalFrameArtikel.
            getArbeitsplatzDto());
      }
      super.eventActionSave(e, true);
      if (getInternalFrame().getKeyWasForLockMe() == null) {
        getInternalFrame().setKeyWasForLockMe(internalFrameArtikel.getArbeitsplatzDto().getIId().
                                              toString());
      }
      eventYouAreSelected(false);
    }
  }

}
