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
package com.lp.client.partner;


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
import com.lp.client.personal.PersonalFilterFactory;
import com.lp.client.util.fastlanereader.gui.QueryType;
import com.lp.server.partner.service.KundesachbearbeiterDto;
import com.lp.server.personal.service.PersonalDto;
import com.lp.server.system.jcr.service.JCRDocFac;
import com.lp.server.system.service.FunktionDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;

@SuppressWarnings("static-access")
/**
 * <p><I>Diese Klasse kuemmert sich um den Sachbearbeiter.</I> </p>
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 * <p>xx.11.04</p>
 * @author $Author: christoph $
 * @version $Revision: 1.9 $
 */

public class PanelSachbearbeiter
    extends PanelBasis
{
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private Border border = null;
  private GridBagLayout gridBagLayoutAll = null;
  private JPanel jpaButtonAction = null;
  private JPanel jpaWorkingOn = null;
  private GridBagLayout gridBagLayout = null;
  private WrapperTextField wtfPersonal = null;
  private WrapperDateField wdfGueltigAb = null;
  private WrapperLabel wlaGueltigAb = null;
  private WrapperButton wbuSachbearbeiterFunktion = null;
  private WrapperTextField wtfSachbearbeiterFunktion = null;
  private PanelQueryFLR panelQueryFLRFunktion = null;
  private WrapperButton wbuPersonal = null;
  private PanelQueryFLR panelQueryFLRPersonal = null;

  static final private String ACTION_SPECIAL_FLR_PERSONAL =
      "action_special_flr_personal";
  static final private String ACTION_SPECIAL_FLR_SACHBEARBEITERFUNKTION =
      "action_special_flr_funktion";

  public PanelSachbearbeiter(InternalFrame internalFrame,
                             String add2TitleI,
                             Object keyI)
      throws Throwable {
    super(internalFrame, add2TitleI, keyI);

    jbInit();
    initComponents();
  }


  protected void eventActionSpecial(ActionEvent e)
      throws Throwable {

    if (e.getActionCommand().equals(ACTION_SPECIAL_FLR_PERSONAL)) {
      panelQueryFLRPersonal = PersonalFilterFactory.getInstance().createPanelFLRPersonal(
          getInternalFrame(), true, false, getKundesachbearbeiterDto().getPersonalIId());
      new DialogQuery(panelQueryFLRPersonal);
    }

    else if (e.getActionCommand().equals(ACTION_SPECIAL_FLR_SACHBEARBEITERFUNKTION)) {

      QueryType[] querytypes = null;
      FilterKriterium[] filters = null;

      panelQueryFLRFunktion = new PanelQueryFLR(
          querytypes,
          filters,
          QueryParameters.UC_ID_FUNKTION,
          null,
          getInternalFrame(),
          LPMain.getInstance().getTextRespectUISPr("part.sachbearbeiter_funktion"));

      new DialogQuery(panelQueryFLRFunktion);
    }
  }


  protected void eventItemchanged(EventObject eI) {

    ItemChangedEvent e = (ItemChangedEvent) eI;
    if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {

      if (e.getSource() == panelQueryFLRPersonal) {
        Integer keyPersonal = (Integer) ( (ISourceEvent) e.getSource()).getIdSelected();
        if (keyPersonal != null) {
          getKundesachbearbeiterDto().setPersonalIId(keyPersonal);
          try {
            PersonalDto personalDto =  DelegateFactory.getInstance().
                getPersonalDelegate().personalFindByPrimaryKey(keyPersonal);
            wtfPersonal.setText(personalDto.getPartnerDto().formatAnrede());
          }
          catch (Throwable t) {
            // lassen wegen event.
            handleException(t, true);
          }
        }
      }
      else if (e.getSource() == panelQueryFLRFunktion) {
        Integer iId = (Integer) ( (ISourceEvent) e.getSource()).
            getIdSelected();
        if (iId != null) {
          getKundesachbearbeiterDto().setFunktionIId(iId);

          try {
            FunktionDto funktionDto = DelegateFactory.getInstance().getLocaleDelegate().
                funktionFindByPrimaryKey(iId);
            wtfSachbearbeiterFunktion.setText(funktionDto.getCNr());
          }
          catch (Throwable t) {
            // lassen wegen event.
            handleException(t, true);
          }
        }
      }
    }
  }


  private void jbInit()
      throws Throwable {
    getInternalFrame().addItemChangedListener(this);

    String[] aButton = {
        PanelBasis.ACTION_UPDATE,
        PanelBasis.ACTION_SAVE,
        PanelBasis.ACTION_DELETE,
        PanelBasis.ACTION_DISCARD
    };
    enableToolsPanelButtons(aButton);

    //von hier ...
    border = BorderFactory.createEmptyBorder(10, 10, 10, 10);
    setBorder(border);

    //das aussenpanel hat immer das gridbaglayout.
    gridBagLayoutAll = new GridBagLayout();
    setLayout(gridBagLayoutAll);

    //actionpanel von oberklasse holen und anhaengen.
    jpaButtonAction = getToolsPanel();

    add(jpaButtonAction,
        new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
                               , GridBagConstraints.NORTHWEST,
                               GridBagConstraints.HORIZONTAL,
                               new Insets(0, 0, 0, 0), 0, 0));

    jpaWorkingOn = new JPanel();
    gridBagLayout = new GridBagLayout();
    jpaWorkingOn.setLayout(gridBagLayout);
    add(jpaWorkingOn,
        new GridBagConstraints(0, 1, 1, 1, 1, 1
                               , GridBagConstraints.SOUTH,
                               GridBagConstraints.BOTH,
                               new Insets(0, 0, 0, 0), 0, 0));

    // Statusbar an den unteren Rand des Panels haengen
    add(getPanelStatusbar(),
        new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0
                               , GridBagConstraints.CENTER,
                               GridBagConstraints.BOTH,
                               new Insets(0, 0, 0, 0), 0, 0));

    //... bis hier ist's immer gleich

    //jetzt meine felder
    wbuPersonal = new WrapperButton();
    wbuPersonal.setMandatoryField(true);
    wbuPersonal.setActivatable(true);
    wbuPersonal.setText(LPMain.getInstance().
                        getTextRespectUISPr("lp.personal"));
    //FLR personal als sachbearbeiter suchen
    wbuPersonal.setActionCommand(ACTION_SPECIAL_FLR_PERSONAL);
    wbuPersonal.addActionListener(this);

    wtfPersonal = new WrapperTextField();
    wtfPersonal.setMandatoryFieldDB(true);
    wtfPersonal.setActivatable(false);
    wtfPersonal.setText("");

    wlaGueltigAb = new WrapperLabel();
    wlaGueltigAb.setText(LPMain.getInstance().getTextRespectUISPr(
        "lp.gueltigab"));
    wdfGueltigAb = new WrapperDateField();
    wdfGueltigAb.setMandatoryFieldDB(true);

    wbuSachbearbeiterFunktion = new WrapperButton();
    wbuSachbearbeiterFunktion.setText(
        LPMain.getInstance().getTextRespectUISPr("part.sachbearbeiter_funktion"));
    wbuSachbearbeiterFunktion.setActionCommand(
        ACTION_SPECIAL_FLR_SACHBEARBEITERFUNKTION);
    wbuSachbearbeiterFunktion.addActionListener(this);
    wtfSachbearbeiterFunktion = new WrapperTextField();
    wtfSachbearbeiterFunktion.setActivatable(false);

    //ab hier einhaengen.

    //Zeile
    jpaWorkingOn.add(wbuPersonal,
                     new GridBagConstraints(0, iZeile, 1, 1, 0.5, 0.0
                                            , GridBagConstraints.NORTH,
                                            GridBagConstraints.BOTH,
                                            new Insets(2, 2, 2, 2), 0, 0));
    jpaWorkingOn.add(wtfPersonal,
                     new GridBagConstraints(1, iZeile, 1, 1, 0.5, 0.0
                                            , GridBagConstraints.NORTH,
                                            GridBagConstraints.BOTH,
                                            new Insets(2, 2, 2, 0), 0, 0));
    //Zeile - bestimmt Abstaende
    iZeile++;
    jpaWorkingOn.add(wlaGueltigAb,
                     new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.BOTH,
                                            new Insets(2, 2, 2, 2), 0, 0));
    jpaWorkingOn.add(wdfGueltigAb,
                     new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.BOTH,
                                            new Insets(2, 2, 2, 2), 0, 0));
    iZeile++;
    jpaWorkingOn.add(wbuSachbearbeiterFunktion,
                     new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.BOTH,
                                            new Insets(2, 2, 2, 2), 0, 0));
    jpaWorkingOn.add(wtfSachbearbeiterFunktion,
                     new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.BOTH,
                                            new Insets(2, 2, 2, 2), 0, 0));
  }


  protected void setDefaults()
      throws Throwable {
    //Vorbelegungen.
    getInternalFrameKunde().getKundesachbearbeiterDto().setTGueltigab(
        new java.sql.Date(System.currentTimeMillis()));

    wdfGueltigAb.setDate(getInternalFrameKunde().getKundesachbearbeiterDto().
                         getTGueltigab());
  }


  public void eventActionNew(EventObject eventObject,
                             boolean bLockMeI,
                             boolean bNeedNoNewI)
      throws Throwable {

    //btnnew: 1 frame informieren, buttons ...
    super.eventActionNew(eventObject, true, false);

    //btnnew: 2 dto leeren.
    getInternalFrameKunde().setKundesachbearbeiterDto(
        new KundesachbearbeiterDto());

    //btnnew: 3 defaults setzen.
    setDefaults();
  }


  /**
   * btnsave: 0 behandle das ereignis save.
   *
   * @param e ereignis.
   * @param bNeedNoSaveI brauche kein save.
   * @throws Throwable ausnahme.
   */
  public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
      throws Throwable {

    // btnsave: 1 alles gesetzt.
    if (allMandatoryFieldsSetDlg()) {

      // btnsave: 2 dtos fuellen.
      components2Dto();

      Integer key = null;
      if (getKundesachbearbeiterDto().getIId() == null) {
        // btnsave: 3 ist create.
        key = DelegateFactory.getInstance().
            getKundesachbearbeiterDelegate().
            createKundesachbearbeiter(getKundesachbearbeiterDto(),
                                      LPMain.getInstance().getTheClient()); ;
        // btnsave: 4 dem dto neuen key setzen.
        getKundesachbearbeiterDto().setIId(key);

        // btnsave: 5 diesem panel neuen key setzen.
        setKeyWhenDetailPanel(key);
      }
      else {
        // btnsave: 6 update.
        DelegateFactory.getInstance().getKundesachbearbeiterDelegate().
            updateKundesachbearbeiter(getKundesachbearbeiterDto());
      }

      // btnsave: 7 frame informieren, buttons ...
      super.eventActionSave(e, true);

      // btnsave: 9: refresen.
      eventYouAreSelected(false);
    }
  }


  protected void setStatusbar()
      throws Throwable {
    setStatusbarPersonalIIdAnlegen(
        getInternalFrameKunde().getKundesachbearbeiterDto().getPersonalIIdAnlegen());
    setStatusbarPersonalIIdAendern(
        getInternalFrameKunde().getKundesachbearbeiterDto().getPersonalIIdAendern());
    setStatusbarTAendern(
        getInternalFrameKunde().getKundesachbearbeiterDto().getTAendern());
    setStatusbarTAnlegen(
        getInternalFrameKunde().getKundesachbearbeiterDto().getTAnlegen());
  }


  protected void dto2Components()
      throws Throwable {
    wdfGueltigAb.setDate(getKundesachbearbeiterDto().getTGueltigab());
    PersonalDto personalDto =  DelegateFactory.getInstance().
        getPersonalDelegate().personalFindByPrimaryKey(
            getKundesachbearbeiterDto().getPersonalIId());
    wtfPersonal.setText(personalDto.getPartnerDto().formatAnrede());

    FunktionDto funktionDto = null;
    if (getKundesachbearbeiterDto().getFunktionIId() != null) {
      funktionDto = DelegateFactory.getInstance().getLocaleDelegate().
          funktionFindByPrimaryKey(getKundesachbearbeiterDto().getFunktionIId());
      wtfSachbearbeiterFunktion.setText(funktionDto.getCNr());
    }
  }


  /**
   * dto2Components
   */
  protected void components2Dto() {

    getKundesachbearbeiterDto().setTGueltigab(wdfGueltigAb.getDate());

    getKundesachbearbeiterDto().setKundeIId(
        getInternalFrameKunde().getKundeDto().getIId());
  }


  public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
      throws Throwable {

    super.eventYouAreSelected(false);

    Object key = getKeyWhenDetailPanel();

    //btnnew: 5
    leereAlleFelder(this);
    getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_AS_I_LIKE, "");
    if (key == null
        || (key != null && key.equals(LPMain.getLockMeForNew()))) {
      // einen neuen Eintrag anlegen oder die letzte Position wurde geloescht.
      clearStatusbar();

    }
    else {
      // Bestehenden Ansprechpartner einlesen.
      getInternalFrameKunde().setKundesachbearbeiterDto(DelegateFactory.getInstance().
          getKundesachbearbeiterDelegate().
          kundesachbearbeiterFindByPrimaryKey( (Integer) key));

      dto2Components();

      setStatusbar();

      PersonalDto personalDto = ( DelegateFactory.getInstance()).
          getPersonalDelegate().personalFindByPrimaryKey(
              getKundesachbearbeiterDto().getPersonalIId());

      String t1 = getInternalFrameKunde().getKundeDto().getPartnerDto().
          formatFixTitelName1Name2();
      getInternalFrame().setLpTitle(
          InternalFrame.TITLE_IDX_AS_I_LIKE,
          t1 + " | " + personalDto.getPartnerDto().formatAnrede());
    }
  }


  protected void eventActionDelete(ActionEvent e, boolean bAdministrateLockKeyI,
                                   boolean bNeedNoDeleteI)
      throws Throwable {

    if (!isLockedDlg()) {
      DelegateFactory.getInstance().getKundesachbearbeiterDelegate().
          removeKundesachbearbeiter(getKundesachbearbeiterDto());

      super.eventActionDelete(e, false, false);
    }
  }


  protected KundesachbearbeiterDto getKundesachbearbeiterDto() {
    return getInternalFrameKunde().getKundesachbearbeiterDto();
  }


  private InternalFrameKunde getInternalFrameKunde() {
    return (InternalFrameKunde) getInternalFrame();
  }


  protected String getLockMeWer()
      throws Exception {
    return HelperClient.LOCKME_KUNDE;
  }


  protected JComponent getFirstFocusableComponent()
      throws Exception {
    return wbuPersonal;
  }
}
