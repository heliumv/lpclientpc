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
package com.lp.client.zutritt;


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
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.component.WrapperTimestampField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.personal.service.ZutrittonlinecheckDto;
import com.lp.server.personal.service.ZutrittscontrollerFac;
import com.lp.server.personal.service.ZutrittsklasseDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;

@SuppressWarnings("static-access")
public class PanelOnlinecheck
    extends PanelBasis
{

  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private GridBagLayout gridBagLayoutAll = null;
  private JPanel jpaWorkingOn = new JPanel();
  private JPanel jpaButtonAction = null;
  private Border border = null;
  private GridBagLayout gridBagLayoutWorkingPanel = null;
  private WrapperLabel wlaVon = new WrapperLabel();
  private WrapperTimestampField wdfVon = new WrapperTimestampField();
  private WrapperLabel wlaBis = new WrapperLabel();
  private WrapperTimestampField wdfBis = new WrapperTimestampField();

  private WrapperLabel wlaAusweis = new WrapperLabel();
  private WrapperTextField wtfAusweis = new WrapperTextField();
  private WrapperLabel wlaPincode = new WrapperLabel();
  private WrapperTextField wtfPincode = new WrapperTextField();

  private ZutrittonlinecheckDto zutrittonlinecheckDto = null;
  private WrapperButton wbuZutrittsklasse = new WrapperButton();
  private WrapperTextField wtfZutrittsklasse = new WrapperTextField();

  static final public String ACTION_SPECIAL_ZUTRITTSKLASSE_FROM_LISTE =
      "action_zutrittsklasse_from_liste";

  private PanelQueryFLR panelQueryFLRZutrittsklasse = null;

  public PanelOnlinecheck(InternalFrame internalFrame, String add2TitleI,
                          Object pk)
      throws Throwable {
    super(internalFrame, add2TitleI, pk);
    jbInit();
    setDefaults();
    initComponents();
    enableAllComponents(this, false);
  }


  protected JComponent getFirstFocusableComponent()
      throws Exception {
    return wdfVon;
  }


  protected void setDefaults()
      throws Throwable {
  }


  public void eventActionNew(EventObject eventObject, boolean bLockMeI,
                             boolean bNeedNoNewI)
      throws Throwable {
    super.eventActionNew(eventObject, true, false);
    zutrittonlinecheckDto = new ZutrittonlinecheckDto();

    leereAlleFelder(this);
  }


  protected void eventActionSpecial(ActionEvent e)
      throws Throwable {
    if (e.getActionCommand().equals(ACTION_SPECIAL_ZUTRITTSKLASSE_FROM_LISTE)) {
      dialogQueryZutrittsklasseFromListe(e);
    }
  }


  protected void eventActionDelete(ActionEvent e, boolean bAdministrateLockKeyI,
                                   boolean bNeedNoDeleteI)
      throws Throwable {
    DelegateFactory.getInstance().getZutrittDelegate().removeZutrittonlinecheck(
        zutrittonlinecheckDto);
    this.setKeyWhenDetailPanel(null);
    super.eventActionDelete(e, false, false);
  }


  void dialogQueryZutrittsklasseFromListe(ActionEvent e)
      throws Throwable {
    String[] aWhichButtonIUse = {
        PanelBasis.ACTION_REFRESH};
    FilterKriterium[] fk = null;

    InternalFrameZutritt ifz = (InternalFrameZutritt) getInternalFrame();
    fk = ZutrittFilterFactory.getInstance().createFKZutrittsklasse(ifz.isBIstHauptmandant());

    panelQueryFLRZutrittsklasse = new PanelQueryFLR(
        null,
        fk,
        QueryParameters.UC_ID_ZUTRITTSKLASSE,
        aWhichButtonIUse,
        getInternalFrame(),
        LPMain.getInstance().getTextRespectUISPr(
            "pers.zutritt.zutrittsklasse"));
    panelQueryFLRZutrittsklasse.setSelectedId(zutrittonlinecheckDto.getZutrittsklasseIId());

    new DialogQuery(panelQueryFLRZutrittsklasse);
  }


  protected void components2Dto()
      throws Throwable {
    zutrittonlinecheckDto.setCAusweis(wtfAusweis.getText());
    zutrittonlinecheckDto.setCPincode(wtfPincode.getText());
    zutrittonlinecheckDto.setTGueltigab(wdfVon.getTimestamp());
    zutrittonlinecheckDto.setTGueltigbis(wdfBis.getTimestamp());
  }


  protected void dto2Components()
      throws Throwable {
    wdfVon.setTimestamp(zutrittonlinecheckDto.getTGueltigab());
    wdfBis.setTimestamp(zutrittonlinecheckDto.getTGueltigbis());
    wtfAusweis.setText(zutrittonlinecheckDto.getCAusweis());
    wtfPincode.setText(zutrittonlinecheckDto.getCPincode());

    ZutrittsklasseDto dto = DelegateFactory.getInstance().getZutrittDelegate().
        zutrittsklasseFindByPrimaryKey(zutrittonlinecheckDto.getZutrittsklasseIId());

    wtfZutrittsklasse.setText(dto.getBezeichnung());
  }


  public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
      throws Throwable {
    if (allMandatoryFieldsSetDlg()) {
      if (wtfAusweis.getText() != null && wtfPincode.getText() != null) {
        DialogFactory.showModalDialog(LPMain.getInstance().getTextRespectUISPr(
            "lp.error"),
                                             "Es darf entweder nur ein PIN-Code oder nur ein Ausweis angegeben werden.");
      }
      else {
        if (wtfAusweis.getText() == null && wtfPincode.getText() == null) {

          DialogFactory.showModalDialog(LPMain.getInstance().getTextRespectUISPr(
              "lp.error"),
                                               "Es muss mindestens ein PIN-Code oder ein Ausweis angegeben werden.");
        }
        else {

          if (wtfPincode.getText() != null && wtfPincode.getText().length()<4) {

            DialogFactory.showModalDialog(LPMain.getInstance().getTextRespectUISPr(
                "lp.error"),
                                                 "PIN-Code muss 4-stellig sein.");
          }
          else {

            components2Dto();
            if (zutrittonlinecheckDto.getIId() == null) {
              zutrittonlinecheckDto.setIId(DelegateFactory.getInstance().
                                           getZutrittDelegate().
                                           createZutrittonlinecheck(
                                               zutrittonlinecheckDto));
              setKeyWhenDetailPanel(zutrittonlinecheckDto.getIId());

            }
            else {
              DelegateFactory.getInstance().getZutrittDelegate().updateZutrittonlinecheck(
                  zutrittonlinecheckDto);
            }
            super.eventActionSave(e, true);

            if (getInternalFrame().getKeyWasForLockMe() == null) {
              getInternalFrame().setKeyWasForLockMe(zutrittonlinecheckDto.getIId() + "");
            }
            eventYouAreSelected(false);
          }

        }
      }
    }
  }


  protected void eventItemchanged(EventObject eI)
      throws Throwable {
    ItemChangedEvent e = (ItemChangedEvent) eI;
    if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
      if (e.getSource() == panelQueryFLRZutrittsklasse) {
        Object key = ( (ISourceEvent) e.getSource()).getIdSelected();
        ZutrittsklasseDto zutrittsklasseDto = DelegateFactory.getInstance().
            getZutrittDelegate().
            zutrittsklasseFindByPrimaryKey( (Integer)
                                           key);
        wtfZutrittsklasse.setText(zutrittsklasseDto.getCNr());
        zutrittonlinecheckDto.setZutrittsklasseIId(zutrittsklasseDto.getIId());
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

    wlaVon.setText(LPMain.getInstance().getTextRespectUISPr("lp.gueltigab"));
    wlaBis.setText(LPMain.getInstance().getTextRespectUISPr("lp.gueltigbis"));
    wdfVon.setMandatoryField(true);
    wdfBis.setMandatoryField(true);

    getInternalFrame().addItemChangedListener(this);
    wbuZutrittsklasse.setText(LPMain.getInstance().getTextRespectUISPr(
        "pers.zutritt.zutrittsklasse") + "...");
    wbuZutrittsklasse.setActionCommand(this.
                                       ACTION_SPECIAL_ZUTRITTSKLASSE_FROM_LISTE);
    wbuZutrittsklasse.addActionListener(this);

    wtfZutrittsklasse.setActivatable(false);
    wtfZutrittsklasse.setMandatoryField(true);
    wlaAusweis.setText(LPMain.getInstance().getTextRespectUISPr("pers.personal.ausweis"));
    wlaPincode.setText(LPMain.getInstance().getTextRespectUISPr("pers.zutritt.pincode"));
    wtfPincode.setColumnsMax(4);

    wtfAusweis.setColumnsMax(ZutrittscontrollerFac.MAX_ZUTRITTONLINECHECK_C_AUSWEIS);
    wtfAusweis.setUppercaseField(true);
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
    jpaWorkingOn.add(wlaVon,
                     new GridBagConstraints(0, 0, 1, 1, 0.1, 0.0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.HORIZONTAL,
                                            new Insets(2, 2, 2, 2), 0, 0));
    jpaWorkingOn.add(wdfVon,
                     new GridBagConstraints(1, 0, 1, 1, 0.1, 0.0
                                            , GridBagConstraints.WEST,
                                            GridBagConstraints.NONE,
                                            new Insets(2, 2, 2, 2), 180, 0));
    jpaWorkingOn.add(wlaBis,
                     new GridBagConstraints(2, 0, 1, 1, 0.1, 0.0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.HORIZONTAL,
                                            new Insets(2, 2, 2, 2), 0, 0));
    jpaWorkingOn.add(wdfBis,
                     new GridBagConstraints(3, 0, 1, 1, 0.1, 0.0
                                            , GridBagConstraints.WEST,
                                            GridBagConstraints.NONE,
                                            new Insets(2, 2, 2, 2), 180, 0));
    jpaWorkingOn.add(wbuZutrittsklasse, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
        0, 0));
    jpaWorkingOn.add(wtfZutrittsklasse, new GridBagConstraints(1, 3, 4, 1, 0.0, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
        0, 0));
    jpaWorkingOn.add(wlaAusweis, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
        0, 0));
    jpaWorkingOn.add(wtfAusweis, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
        0, 0));
    jpaWorkingOn.add(wlaPincode, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
        0, 0));
    jpaWorkingOn.add(wtfPincode, new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0
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
    return HelperClient.LOCKME_BETRIEBSKALENDER;
  }


  public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
      throws Throwable {

    super.eventYouAreSelected(false);

    Object key = getKeyWhenDetailPanel();

    if (key == null
        || (key.equals(LPMain.getLockMeForNew()))) {

      leereAlleFelder(this);
      if (key != null && key.equals(LPMain.getLockMeForNew())) {
        wdfVon.setTimestamp(new java.sql.Timestamp(System.currentTimeMillis()));
        wdfBis.setTimestamp(new java.sql.Timestamp(System.currentTimeMillis() + 600000));
        double zZahl = Math.random() * 10000;
        int iZahl = (int) zZahl;
        String sZahl = "" + iZahl;
        if (iZahl < 10) {
          sZahl = "000" + iZahl;
        }
        else if (iZahl < 100) {
          sZahl = "00" + iZahl;

        }
        else if (iZahl < 1000) {
          sZahl = "0" + iZahl;

        }
        wtfPincode.setText(sZahl);

        //DEFAULT-Zutrittsklasse
        ParametermandantDto parameter = (ParametermandantDto)
            DelegateFactory.getInstance().getParameterDelegate().
            getParametermandant(ParameterFac.PARAMETER_ZUTRITT_DEFAULT_ZUTRITTSKLASSE,
                                ParameterFac.KATEGORIE_PERSONAL,
                                LPMain.getInstance().getTheClient().getMandant());

        if (parameter.getCWert() != null && !parameter.getCWert().trim().equals("")) {
          try {
            ZutrittsklasseDto zutrittsaklasseDto = DelegateFactory.getInstance().
                getZutrittDelegate().
                zutrittsklasseFindByCNr(parameter.getCWert());
            if (zutrittsaklasseDto.getMandantCNr().equals(LPMain.getInstance().
                getTheClient().getMandant())) {
              wtfZutrittsklasse.setText(zutrittsaklasseDto.getBezeichnung());
              zutrittonlinecheckDto.setZutrittsklasseIId(zutrittsaklasseDto.getIId());
            }
            else {
              DialogFactory.showModalDialog("Fehler",
                  "Zutrittsklasse bei diesem Mandanten nicht vorhanden.");
            }

          }
          catch (Throwable ex) {
            DialogFactory.showModalDialog("Fehler",
                                                 "Zutrittsklasse '" + parameter.getCWert() +
                                                 "' nicht angelegt.");
          }

        }

      }

      clearStatusbar();
    }
    else {
      zutrittonlinecheckDto = DelegateFactory.getInstance().getZutrittDelegate().
          zutrittonlinecheckFindByPrimaryKey( (Integer) key);

      dto2Components();

    }
  }

}
