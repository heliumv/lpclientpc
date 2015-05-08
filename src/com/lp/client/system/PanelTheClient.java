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
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.EventObject;
import java.util.GregorianCalendar;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.Border;

import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.WrapperDateField;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.personal.service.PersonalDto;
import com.lp.server.system.jms.service.LPMessage;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.system.service.TheClientFac;
import com.lp.util.Helper;


/**
 * <p> Diese Klasse kuemmert sich um die TheClient-Eintraege.</p>
 *
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 *
 * <p>Erstellung: Victor Finder; 28.03.05</p>
 *
 * <p>@author $Author: adi $</p>
 *
 * @version not attributable Date $Date: 2010/06/21 13:32:22 $
 */
public class PanelTheClient
    extends PanelBasis
{
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private TheClientDto theClientDto = null;
  private PersonalDto personalDto = null;

  private WrapperTextField wtfBenutzername = null;
  private WrapperTextField wtfLoggedin = null;
  private WrapperDateField wdfDatefieldVon = null;
  private WrapperDateField wdfDatefieldBis = null;
  private WrapperLabel wlaVon = null;
  private WrapperLabel wlaBis = null;

  private GridBagLayout gridBagLayoutAll = null;
  private JPanel jPanelWorkingOn = null;
  private GridBagLayout gridBagLayoutWorkingOn = null;
  private Border innerBorder = null;

  public PanelTheClient(InternalFrame internalFrame, String add2TitleI,
                        Object key)
      throws Throwable {
    super(internalFrame, add2TitleI, key);
    jbInit();
    initComponents();
  }


  private void jbInit()
      throws Throwable {

    // das Aussenpanel hat immer das Gridbaglayout.
    gridBagLayoutAll = new GridBagLayout();
    this.setLayout(gridBagLayoutAll);

    innerBorder = BorderFactory.createEmptyBorder(10, 10, 10, 10);
    this.setBorder(innerBorder);

    // Actionpanel setzen und anhaengen
    JPanel panelButtonAction = getToolsPanel();
    this.add(panelButtonAction, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
        , GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
        new Insets(0, 0, 0, 0), 0, 0));

    // zusaetzliche buttons
    String[] aWhichButtonIUse = {
        PanelBasis.ACTION_DELETE};
    enableToolsPanelButtons(aWhichButtonIUse);

    // Workingpanel
    jPanelWorkingOn = new JPanel();
    gridBagLayoutWorkingOn = new GridBagLayout();
    jPanelWorkingOn.setLayout(gridBagLayoutWorkingOn);
    this.add(jPanelWorkingOn,
             new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0
                                    , GridBagConstraints.SOUTH, GridBagConstraints.BOTH,
                                    new Insets(0, 0, 0, 0), 0, 0));

    // Statusbar an den unteren Rand des Panels haengen
    add(getPanelStatusbar(),
        new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0
                               , GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                               new Insets(0, 0, 0, 0), 0, 0));

    wtfBenutzername = new WrapperTextField();
    wtfBenutzername.setActivatable(false);
    wtfLoggedin = new WrapperTextField();
    wdfDatefieldVon = new WrapperDateField();
    wdfDatefieldVon.setActivatable(true);
    wdfDatefieldVon.setMandatoryField(true);
    wdfDatefieldBis = new WrapperDateField();
    wdfDatefieldBis.setActivatable(true);
    wdfDatefieldBis.setMandatoryField(true);

    GregorianCalendar gcBis = new GregorianCalendar();
    Date heute = new Date();
    gcBis.setTime(heute);
    GregorianCalendar monat = new GregorianCalendar();
    monat = new GregorianCalendar(gcBis.get(Calendar.YEAR), gcBis.get(Calendar.MONTH),
                                  gcBis.get(Calendar.DATE) - 2);
    Date gestern = monat.getTime();
    wdfDatefieldVon.setDate(gestern);
    wdfDatefieldBis.setDate(gestern);
    wdfDatefieldVon.setMaximumValue(gestern);
    wdfDatefieldBis.setMaximumValue(gestern);

    wlaVon = new WrapperLabel(LPMain.getTextRespectUISPr("lp.von"));
    wlaBis = new WrapperLabel(LPMain.getTextRespectUISPr("lp.bis"));

    jPanelWorkingOn.add(wlaVon,
                        new GridBagConstraints(0, iZeile, 1, 1, 0.1, 0.0
                                               , GridBagConstraints.CENTER,
                                               GridBagConstraints.BOTH,
                                               new Insets(2, 2, 2, 2), 0, 0));
    jPanelWorkingOn.add(wdfDatefieldVon,
                        new GridBagConstraints(1, iZeile, 1, 1, 0.1, 0.0
                                               , GridBagConstraints.CENTER,
                                               GridBagConstraints.BOTH,
                                               new Insets(2, 2, 2, 2), 0, 0));
    iZeile++;
    jPanelWorkingOn.add(wlaBis,
                        new GridBagConstraints(0, iZeile, 1, 1, 0.1, 0.0
                                               , GridBagConstraints.CENTER,
                                               GridBagConstraints.BOTH,
                                               new Insets(2, 2, 2, 2), 0, 0));
    jPanelWorkingOn.add(wdfDatefieldBis,
                        new GridBagConstraints(1, iZeile, 1, 1, 0.1, 0.0
                                               , GridBagConstraints.CENTER,
                                               GridBagConstraints.BOTH,
                                               new Insets(2, 2, 2, 2), 0, 0));

    iZeile++;
    // Zeile

  }


  protected void eventActionDelete(ActionEvent e, boolean bAdministrateLockKeyI,
                                   boolean bNeedNoDeleteI)
      throws Throwable {

    if (allMandatoryFieldsSetDlg()) {
      boolean bLoeschen = true;
      boolean bClientBeenden = false;
      // MB 23.08.07 Wenn inkl. heute, dann Warnung ausgeben, weil sonst mein client mit stirbt!
      if (!wdfDatefieldBis.getDate().before(Helper.cutDate(new java.sql.Date(System.
          currentTimeMillis())))) {
        bLoeschen = DialogFactory.showModalJaNeinDialog(
            getInternalFrame(),
            LPMain.getTextRespectUISPr("lp.warning.heutigeloggedinloeschen"),
            LPMain.getTextRespectUISPr("lp.frage"));
        bClientBeenden = bLoeschen;
      }
      //jmsqueue
      if (bLoeschen) {
    	  Timestamp tBis = wdfDatefieldBis.getTimestamp();
          tBis = new Timestamp(tBis.getTime() + (24 * 60 * 60 * 1000) - 1);
    	  DelegateFactory.getInstance().getTheClientDelegate().removeTheClientTVonTBis(wdfDatefieldVon.getTimestamp(), tBis);
    	  
        LPMessage lpm = new LPMessage("SY",
                                      LPMain.getInstance().getCNrUser(),
                                      LPMessage.IIDELETE_THE_CLIENT_LOGGED_IN);

        lpm.setValue(TheClientFac.FLRSPALTE_T_LOGGEDIN, wdfDatefieldVon.getTimestamp());
        tBis = new Timestamp(tBis.getTime() + (24 * 60 * 60 * 1000) - 1);
        lpm.setValue(TheClientFac.FLRSPALTE_T_LOGGEDOUT, tBis);

        DelegateFactory.getInstance().getLPAsynchSubscriber().send(lpm);
      }
      if (bClientBeenden) {
        LPMain.getInstance().exitClientNowErrorDlg(null);
      }
    }
    super.eventActionDelete(e, false, false);
  }


  protected void eventItemchanged(EventObject eI)
      throws Throwable {
//    ItemChangedEvent e = (ItemChangedEvent) eI;
  }


  private void dto2Components()
      throws Throwable {

    wtfBenutzername.setText(personalDto.getPartnerDto().getCName1nachnamefirmazeile1());
    wtfLoggedin.setText(Helper.formatTimestamp(theClientDto.getDLoggedin(),
                                               LPMain.getTheClient().getLocUi()));
  }


  public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
      throws Throwable {

    super.eventYouAreSelected(false);

    Object key = getKeyWhenDetailPanel();

    if (key == null || key.equals(LPMain.getLockMeForNew())) {
      theClientDto = new TheClientDto();
    }
    else {
      theClientDto = DelegateFactory.getInstance().getTheClientDelegate().
          theClientFindByPrimaryKey( (String) key);
      personalDto = DelegateFactory.getInstance().getPersonalDelegate().
          personalFindByPrimaryKey(theClientDto.getIDPersonal());
      aktualisiereStatusbar();
    }

    dto2Components();

    wdfDatefieldVon.setEnabled(true);
    wdfDatefieldBis.setEnabled(true);
  }


  private void aktualisiereStatusbar()
      throws Throwable {
    if (theClientDto != null && theClientDto.getIDPersonal() != null) {
      setStatusbarPersonalIIdAendern(theClientDto.getIDPersonal());
      setStatusbarTAendern(theClientDto.getDLoggedin());
    }
  }


  protected String getLockMeWer()
      throws Exception {
    return HelperClient.LOCKME_THECLIENT;
  }
}
