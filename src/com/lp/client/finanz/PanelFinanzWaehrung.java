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
package com.lp.client.finanz;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.EventObject;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.border.Border;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.finanz.service.FinanzFac;
import com.lp.server.system.service.WaehrungDto;


/**
 * <p> Diese Klasse kuemmert sich um die Waehrungen</p>
 *
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 *
 * <p>Erstellung: Martin Bluehweis; 22.06.05</p>
 *
 * <p>@author $Author: sebastian $</p>
 *
 * @version not attributable Date $Date: 2008/12/19 12:21:30 $
 */
public class PanelFinanzWaehrung
    extends PanelBasis
{

  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
//von hier ...
  private JPanel jPanelWorkingOn = null;
  private JPanel panelButtonAction = null;
  private Border border = null;
  private WrapperLabel wlaWaehrung = new WrapperLabel();
  private WrapperTextField wtfWaehrung = new WrapperTextField();
  private WrapperLabel wlaKommentar = new WrapperLabel();
  private WrapperTextField wtfKommentar = new WrapperTextField();
  private TabbedPaneWaehrung tpWaehrung=null;

  public PanelFinanzWaehrung(InternalFrame internalFrame, String add2TitleI,
                    Object pk, TabbedPaneWaehrung tpWaehrung)
      throws Throwable {
    super(internalFrame, add2TitleI, pk);
    this.tpWaehrung=tpWaehrung;
    jbInit();
    initComponents();
  }

  public void eventActionNew(EventObject eventObject, boolean bLockMeI,
                             boolean bNeedNoNewI)
      throws Throwable {
    super.eventActionNew(eventObject, true, false);
    tpWaehrung.setWaehrungDto(null);
    leereAlleFelder(this);
    clearStatusbar();
  }

  protected void dto2Components() throws ExceptionLP, Throwable {
    wtfWaehrung.setText(tpWaehrung.getWaehrungDto().getCNr());
    wtfKommentar.setText(tpWaehrung.getWaehrungDto().getCKommentar());

    // StatusBar
    this.setStatusbarPersonalIIdAnlegen(tpWaehrung.getWaehrungDto().getPersonalIIdAnlegen());
    this.setStatusbarTAnlegen(tpWaehrung.getWaehrungDto().getTAnlegen());
    this.setStatusbarPersonalIIdAendern(tpWaehrung.getWaehrungDto().getPersonalIIdAendern());
    this.setStatusbarTAendern(tpWaehrung.getWaehrungDto().getTAendern());
  }

  public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
      throws Throwable {
    super.eventYouAreSelected(false);
    Object key = getKeyWhenDetailPanel();
    if (key == null || key.equals(LPMain.getLockMeForNew())) {
      leereAlleFelder(this);
      clearStatusbar();
      if(key != null && key.equals(LPMain.getLockMeForNew())) {
        // beim Neuanlegen ist das Waehrungsfeld schon editierbar
        wtfWaehrung.setEditable(true);
      }
    }
    else {
      tpWaehrung.setWaehrungDto(DelegateFactory.getInstance().getLocaleDelegate().waehrungFindByPrimaryKey(key.toString()));
      dto2Components();
    }
   
  }

  protected void components2Dto() throws Throwable {
    Integer personalIId=LPMain.getTheClient().getIDPersonal();
    if(tpWaehrung.getWaehrungDto()==null) {
      tpWaehrung.setWaehrungDto(new WaehrungDto());
      /**
       * @todo IMS#771  PJ 4988
       * das gehoert eigentlich auf den server
       */
      tpWaehrung.getWaehrungDto().setPersonalIIdAnlegen(personalIId);
      tpWaehrung.getWaehrungDto().setTAnlegen(new java.sql.Timestamp(System.currentTimeMillis()));
    }
    tpWaehrung.getWaehrungDto().setCNr(wtfWaehrung.getText());
    tpWaehrung.getWaehrungDto().setCKommentar(wtfKommentar.getText());
    /**
     * @todo IMS#771  PJ 4988
     * das gehoert eigentlich auf den server
     */
    tpWaehrung.getWaehrungDto().setPersonalIIdAendern(personalIId);
  }

  public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
      throws
      Throwable {
    if (allMandatoryFieldsSetDlg()) {
      components2Dto();
      DelegateFactory.getInstance().getLocaleDelegate().updateWaehrung(tpWaehrung.getWaehrungDto());
      setKeyWhenDetailPanel(tpWaehrung.getWaehrungDto().getCNr());
      if (getInternalFrame().getKeyWasForLockMe() == null) {
        getInternalFrame().setKeyWasForLockMe(tpWaehrung.getWaehrungDto().getCNr());
      }
      super.eventActionSave(e, true);
      eventYouAreSelected(false);
    }
  }

  protected void eventActionDelete(ActionEvent e, boolean bAdministrateLockKeyI,
                                   boolean bNeedNoDeleteI)
      throws
      Throwable {
    DelegateFactory.getInstance().getLocaleDelegate().removeWaehrung(tpWaehrung.getWaehrungDto());
    super.eventActionDelete(e, false, false);
  }

  protected void eventItemchanged(EventObject eI)
      throws Throwable {
//    ItemChangedEvent e = (ItemChangedEvent) eI;
//    if (e.getID() == ItemChangedEvent.ITEM_CHANGED) {
//      if (e.getAcceptFromThisSrc() == e.getSource()) {
//
//        setKeyWhenDetailPanel( ( (ISourceEvent) e.getSource()).getIdSelected());
//        if (getKeyWhenDetailPanel() != null) {
//          tpWaehrung.setWaehrungDto(getInternalFrame().getLocaleDelegate().
//                                    waehrungFindByPrimaryKey(getKeyWhenDetailPanel().
//              toString()));
//          dto2Components();
//        }
//      }
//    }
  }

  private void jbInit()
      throws Throwable {
    //von hier ...
    border = BorderFactory.createEmptyBorder(10, 10, 10, 10);
    setBorder(border);
    //das Aussenpanel hat immer das Gridbaglayout.
    this.setLayout( new GridBagLayout());

    //Actionpanel von Oberklasse holen und anhaengen.
    panelButtonAction = getToolsPanel();
    this.setActionMap(null);

    wlaWaehrung.setText(LPMain.getTextRespectUISPr("lp.waehrung"));
    wtfWaehrung.setColumnsMax(FinanzFac.MAX_WAEHRUNG_C_NR);
    wtfWaehrung.setMandatoryField(true);
    wtfWaehrung.setActivatable(false);
    getInternalFrame().addItemChangedListener(this);

    wlaKommentar.setText(LPMain.getTextRespectUISPr("lp.kommentar"));
    wtfKommentar.setColumnsMax(FinanzFac.MAX_WAEHRUNG_C_KOMMENTAR);

    wlaWaehrung.setMinimumSize(new Dimension(80, Defaults.getInstance().getControlHeight()));
    wlaWaehrung.setPreferredSize(new Dimension(80, Defaults.getInstance().getControlHeight()));
    wtfWaehrung.setMinimumSize(new Dimension(30, Defaults.getInstance().getControlHeight()));
    wtfWaehrung.setPreferredSize(new Dimension(30, Defaults.getInstance().getControlHeight()));

    this.add(panelButtonAction, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
        , GridBagConstraints.WEST, GridBagConstraints.NONE,
        new Insets(0, 0, 0, 0), 0, 0));
    jPanelWorkingOn = new JPanel();
    jPanelWorkingOn.setLayout( new GridBagLayout());
    this.add(jPanelWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0
        , GridBagConstraints.SOUTHEAST, GridBagConstraints.BOTH,
        new Insets(0, 0, 0, 0), 0, 0));
    this.add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.BOTH,
        new Insets(0, 0, 0, 0), 0, 0));
    jPanelWorkingOn.add(wlaWaehrung,
                        new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0
                                               , GridBagConstraints.CENTER,
                                               GridBagConstraints.HORIZONTAL,
                                               new Insets(2, 2, 2, 2), 0, 0));
    jPanelWorkingOn.add(wtfWaehrung,
                        new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0
                                               , GridBagConstraints.CENTER,
                                               GridBagConstraints.HORIZONTAL,
                                               new Insets(2, 2, 2, 2), 0, 0));
    iZeile++;
    jPanelWorkingOn.add(wlaKommentar,
                        new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0
                                               , GridBagConstraints.CENTER,
                                               GridBagConstraints.HORIZONTAL,
                                               new Insets(2, 2, 2, 2), 0, 0));
    jPanelWorkingOn.add(wtfKommentar,
                        new GridBagConstraints(1, iZeile, 2, 1, 1.0, 0.0
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

  protected String getLockMeWer() {
    return HelperClient.LOCKME_WAEHRUNG;
  }


  protected JComponent getFirstFocusableComponent()
      throws Exception {
    return wtfWaehrung;
  }

}
