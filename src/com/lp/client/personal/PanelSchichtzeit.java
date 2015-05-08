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
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.EventObject;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.border.Border;

import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperComboBox;
import com.lp.client.frame.component.WrapperDateField;
import com.lp.client.frame.component.WrapperDateRangeController;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperSpinner;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.personal.service.ZeiterfassungFac;
import com.lp.server.personal.service.ZeitmodellDto;

@SuppressWarnings("static-access")
public class PanelSchichtzeit
    extends PanelBasis implements PropertyChangeListener
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

  WrapperLabel wlaSchichtwechsel = new WrapperLabel();
  WrapperLabel wlaGueltigab = new WrapperLabel();
  WrapperLabel wlaBerechnebis = new WrapperLabel();

  WrapperTextField wtfSchicht1 = new WrapperTextField();
  WrapperTextField wtfSchicht2 = new WrapperTextField();
  WrapperTextField wtfSchicht3 = new WrapperTextField();
  WrapperTextField wtfSchicht4 = new WrapperTextField();

  WrapperButton wbuSchicht1 = new WrapperButton();
  WrapperButton wbuSchicht2 = new WrapperButton();
  WrapperButton wbuSchicht3 = new WrapperButton();
  WrapperButton wbuSchicht4 = new WrapperButton();

  Integer iSchicht1 = null;
  Integer iSchicht2 = null;
  Integer iSchicht3 = null;
  Integer iSchicht4 = null;

  WrapperComboBox wcoSchichtwechsel = new WrapperComboBox();

  WrapperSpinner wspKwab = null;
  WrapperSpinner wspKwbis = null;
  WrapperSpinner wspJahrab = null;
  WrapperSpinner wspJahrbis = null;

  WrapperDateField wdfAb = new WrapperDateField();
  WrapperDateField wdfBis = new WrapperDateField();

  private InternalFramePersonal internalFrame = null;
  private Integer personalIId = null;

  private final static String MENUE_ACTION_SPEICHERN =
      "MENUE_ACTION_SPEICHERN";
  private final static String MENUE_ACTION_SCHICHT1 =
      "MENUE_ACTION_SCHICHT1";
  private final static String MENUE_ACTION_SCHICHT2 =
      "MENUE_ACTION_SCHICHT2";
  private final static String MENUE_ACTION_SCHICHT3 =
      "MENUE_ACTION_SCHICHT3";
  private final static String MENUE_ACTION_SCHICHT4 =
      "MENUE_ACTION_SCHICHT4";

  private PanelQueryFLR panelQueryFLRZeitmodell1 = null;
  private PanelQueryFLR panelQueryFLRZeitmodell2 = null;
  private PanelQueryFLR panelQueryFLRZeitmodell3 = null;
  private PanelQueryFLR panelQueryFLRZeitmodell4 = null;

  private WrapperDateRangeController wdrBereich = null;

  DialogSchichtzeit dialog = null;
  public PanelSchichtzeit(InternalFramePersonal internalFrame, String add2TitleI,
                          Integer personalIId, DialogSchichtzeit dialog)
      throws Throwable {
    super(internalFrame, add2TitleI, null);
    this.internalFrame = internalFrame;
    this.dialog = dialog;
    this.personalIId = personalIId;
    jbInit();
    initComponents();
    wdrBereich.doClickDown();
    setDefaults();
    enableAllComponents(this, false);
  }


  private void setDateFieldsFromDate(Timestamp timestamp, WrapperSpinner uiKw, WrapperSpinner uiYear) {
      Calendar c = Calendar.getInstance();
      c.setTime(timestamp) ;
      uiKw.setInteger(c.get(Calendar.WEEK_OF_YEAR));

      int year = c.get(Calendar.YEAR) ;
      // Die Kalenderwoche 1 kann es zwei mal geben
      if(c.get(Calendar.WEEK_OF_YEAR) == 1 && c.get(Calendar.MONTH) > 1) {
      	++year ;
      }
      // Kalenderwoche 53 sollte noch das alte Jahr f?r den Server bekommen
      if(c.get(Calendar.WEEK_OF_YEAR) == 53 && c.get(Calendar.MONTH) == 12) {
      	--year ;
      }
      uiYear.setInteger(year) ;
  }
  
  public void propertyChange(PropertyChangeEvent e) {
    if (e.getSource() == wdfAb.getDisplay() && e.getNewValue() instanceof Date &&
        e.getPropertyName().equals("date")) {
      wdfAb.setDate( (Date) e.getNewValue());
      if (wdfAb.getTimestamp() != null) {
//        Calendar c = Calendar.getInstance();
//        c.setTime(wdfAb.getTimestamp());
//        wspKwab.setInteger(c.get(Calendar.WEEK_OF_YEAR));
//        wspJahrab.setInteger(c.get(Calendar.YEAR));
        setDateFieldsFromDate(wdfAb.getTimestamp(), wspKwab, wspJahrab);
      }
    }
    else if (e.getSource() == wdfBis.getDisplay() && e.getNewValue() instanceof Date &&
        e.getPropertyName().equals("date")) {
      wdfBis.setDate( (Date) e.getNewValue());
      if (wdfBis.getTimestamp() != null) {
//        Calendar c = Calendar.getInstance();
//        c.setTime(wdfBis.getTimestamp());
//        wspKwbis.setInteger(c.get(Calendar.WEEK_OF_YEAR));
//        wspJahrbis.setInteger(c.get(Calendar.YEAR));
          setDateFieldsFromDate(wdfBis.getTimestamp(), wspKwbis, wspJahrbis);
      }
    }
  }


  protected void setDefaults() {
    if(internalFrame.schichtzeitEinstellungen!=null){
      SchichtzeitEinstellungen s=internalFrame.schichtzeitEinstellungen;
      if(s.getSchicht1ZeilmodellDto()!=null){
        wtfSchicht1.setText(s.getSchicht1ZeilmodellDto().getBezeichnung());
        iSchicht1 = s.getSchicht1ZeilmodellDto().getIId();
      }
      if(s.getSchicht2ZeilmodellDto()!=null){
        wtfSchicht2.setText(s.getSchicht2ZeilmodellDto().getBezeichnung());
        iSchicht2 = s.getSchicht2ZeilmodellDto().getIId();
      }
      if(s.getSchicht3ZeilmodellDto()!=null){
        wtfSchicht3.setText(s.getSchicht3ZeilmodellDto().getBezeichnung());
        iSchicht3 = s.getSchicht3ZeilmodellDto().getIId();
      }
      if(s.getSchicht4ZeilmodellDto()!=null){
        wtfSchicht4.setText(s.getSchicht4ZeilmodellDto().getBezeichnung());
        iSchicht4 = s.getSchicht4ZeilmodellDto().getIId();
      }
      wdfAb.setTimestamp(s.getGueltigAbDatum());
      wdfBis.setTimestamp(s.getGueltigBisDatum());
      wspKwab.setInteger(s.getKwGueltigAb());
      wspKwbis.setInteger(s.getKwGueltigBis());
      wspJahrab.setInteger(s.getJahrGueltigAb());
      wspJahrbis.setInteger(s.getJahrGueltigBis());

      wcoSchichtwechsel.setKeyOfSelectedItem(s.getTagesartSchichtwechsel());

    }
  }


  protected JComponent getFirstFocusableComponent()
      throws Exception {
    return wbuSchicht1;
  }


  public void eventActionNew(EventObject eventObject, boolean bLockMeI,
                             boolean bNeedNoNewI)
      throws Throwable {
    super.eventActionNew(eventObject, true, false);
    leereAlleFelder(this);
  }


  /**
   * Hier kommen die events meiner speziellen Buttons an.
   *
   * @param e ActionEvent
   * @throws Throwable
   */
  protected void eventActionSpecial(ActionEvent e)
      throws Throwable {
    if (e.getActionCommand().equals(MENUE_ACTION_SCHICHT1)) {
      dialogQueryZeitmodell1FromListe(e);
    }
    else if (e.getActionCommand().equals(MENUE_ACTION_SCHICHT2)) {
      dialogQueryZeitmodell2FromListe(e);
    }
    else if (e.getActionCommand().equals(MENUE_ACTION_SCHICHT3)) {
      dialogQueryZeitmodell3FromListe(e);
    }
    else if (e.getActionCommand().equals(MENUE_ACTION_SCHICHT4)) {
      dialogQueryZeitmodell4FromListe(e);
    }
  }


  void dialogQueryZeitmodell1FromListe(ActionEvent e)
      throws Throwable {
    panelQueryFLRZeitmodell1 = PersonalFilterFactory.getInstance().
        createPanelFLRZeitmodell(
            getInternalFrame(), iSchicht1, false);
    new DialogQuery(panelQueryFLRZeitmodell1);
  }


  void dialogQueryZeitmodell2FromListe(ActionEvent e)
      throws Throwable {
    panelQueryFLRZeitmodell2 = PersonalFilterFactory.getInstance().
        createPanelFLRZeitmodell(
            getInternalFrame(), iSchicht2, false);
    new DialogQuery(panelQueryFLRZeitmodell2);
  }


  void dialogQueryZeitmodell3FromListe(ActionEvent e)
      throws Throwable {
    panelQueryFLRZeitmodell3 = PersonalFilterFactory.getInstance().
        createPanelFLRZeitmodell(
            getInternalFrame(), iSchicht3, true);
    new DialogQuery(panelQueryFLRZeitmodell3);
  }


  void dialogQueryZeitmodell4FromListe(ActionEvent e)
      throws Throwable {
    panelQueryFLRZeitmodell4 = PersonalFilterFactory.getInstance().
        createPanelFLRZeitmodell(
            getInternalFrame(), iSchicht4, true);
    new DialogQuery(panelQueryFLRZeitmodell4);
  }


  public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
      throws Throwable {
    if (allMandatoryFieldsSetDlg()) {
      DelegateFactory.getInstance().getPersonalDelegate().
          schichtzeitenVorausplanen(personalIId, iSchicht1, iSchicht2, iSchicht3,
                                    iSchicht4, wspKwab.getInteger(),wspKwbis.getInteger(),wspJahrab.getInteger(),wspJahrbis.getInteger(),
                                    (String) wcoSchichtwechsel.getKeyOfSelectedItem());
      internalFrame.schichtzeitEinstellungen.setGueltigAbDatum(wdfAb.getTimestamp());
      internalFrame.schichtzeitEinstellungen.setGueltigBisDatum(wdfBis.getTimestamp());
      internalFrame.schichtzeitEinstellungen.setJahrGueltigAb(wspJahrab.getInteger());
      internalFrame.schichtzeitEinstellungen.setJahrGueltigBis(wspJahrbis.getInteger());
      internalFrame.schichtzeitEinstellungen.setKwGueltigAb(wspKwab.getInteger());
      internalFrame.schichtzeitEinstellungen.setKwGueltigBis(wspKwbis.getInteger());
      internalFrame.schichtzeitEinstellungen.setTagesartSchichtwechsel((String)wcoSchichtwechsel.getKeyOfSelectedItem());;
      dialog.closeDialog();
    }
  }


  protected void eventItemchanged(EventObject eI)
      throws Throwable {
    ItemChangedEvent e = (ItemChangedEvent) eI;
    if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
      if( internalFrame.schichtzeitEinstellungen==null){
        internalFrame.schichtzeitEinstellungen=new SchichtzeitEinstellungen();
      }
      if (e.getSource() == panelQueryFLRZeitmodell1) {
        Object key = ( (ISourceEvent) e.getSource()).getIdSelected();
        ZeitmodellDto zeitmodellDto = DelegateFactory.getInstance().
            getZeiterfassungDelegate().
            zeitmodellFindByPrimaryKey( (Integer) key);
        wtfSchicht1.setText(zeitmodellDto.getBezeichnung());
        iSchicht1 = zeitmodellDto.getIId();
        internalFrame.schichtzeitEinstellungen.setSchicht1ZeilmodellDto(zeitmodellDto);
      }
      else if (e.getSource() == panelQueryFLRZeitmodell2) {
        Object key = ( (ISourceEvent) e.getSource()).getIdSelected();
        ZeitmodellDto zeitmodellDto = DelegateFactory.getInstance().
            getZeiterfassungDelegate().
            zeitmodellFindByPrimaryKey( (Integer) key);
        wtfSchicht2.setText(zeitmodellDto.getBezeichnung());
        iSchicht2 = zeitmodellDto.getIId();
        internalFrame.schichtzeitEinstellungen.setSchicht2ZeilmodellDto(zeitmodellDto);

      }
      else if (e.getSource() == panelQueryFLRZeitmodell3) {
        Object key = ( (ISourceEvent) e.getSource()).getIdSelected();
        ZeitmodellDto zeitmodellDto = DelegateFactory.getInstance().
            getZeiterfassungDelegate().
            zeitmodellFindByPrimaryKey( (Integer) key);
        wtfSchicht3.setText(zeitmodellDto.getBezeichnung());
        iSchicht3 = zeitmodellDto.getIId();
        internalFrame.schichtzeitEinstellungen.setSchicht3ZeilmodellDto(zeitmodellDto);

      }
      else if (e.getSource() == panelQueryFLRZeitmodell4) {
        Object key = ( (ISourceEvent) e.getSource()).getIdSelected();
        ZeitmodellDto zeitmodellDto = DelegateFactory.getInstance().
            getZeiterfassungDelegate().
            zeitmodellFindByPrimaryKey( (Integer) key);
        wtfSchicht4.setText(zeitmodellDto.getBezeichnung());
        iSchicht4 = zeitmodellDto.getIId();
        internalFrame.schichtzeitEinstellungen.setSchicht4ZeilmodellDto(zeitmodellDto);

      }
    }
    else if (e.getID() == ItemChangedEvent.ACTION_LEEREN) {
      if (e.getSource() == panelQueryFLRZeitmodell3) {
        wtfSchicht3.setText(null);
        iSchicht3 = null;
        wtfSchicht4.setText(null);
        iSchicht4 = null;
      }
      if (e.getSource() == panelQueryFLRZeitmodell4) {
        wtfSchicht4.setText(null);
        iSchicht4 = null;
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

    wbuSchicht1.setText(LPMain.getInstance().getTextRespectUISPr("pers.schicht") +
                        " 1...");
    wbuSchicht1.setActionCommand(MENUE_ACTION_SCHICHT1);
    wbuSchicht1.addActionListener(this);

    wbuSchicht2.setText(LPMain.getInstance().getTextRespectUISPr("pers.schicht") +
                        " 2...");
    wbuSchicht2.setActionCommand(MENUE_ACTION_SCHICHT2);
    wbuSchicht2.addActionListener(this);

    wbuSchicht3.setText(LPMain.getInstance().getTextRespectUISPr("pers.schicht") +
                        " 3...");
    wbuSchicht3.setActionCommand(MENUE_ACTION_SCHICHT3);
    wbuSchicht3.addActionListener(this);

    wbuSchicht4.setText(LPMain.getInstance().getTextRespectUISPr("pers.schicht") +
                        " 4...");
    wbuSchicht4.setActionCommand(MENUE_ACTION_SCHICHT4);
    wbuSchicht4.addActionListener(this);

    wtfSchicht1.setActivatable(false);
    wtfSchicht2.setActivatable(false);
    wtfSchicht3.setActivatable(false);
    wtfSchicht4.setActivatable(false);

    wtfSchicht1.setMandatoryField(true);
    wtfSchicht2.setMandatoryField(true);
    wcoSchichtwechsel.setMandatoryField(true);

    Map<String, String> m = new LinkedHashMap<String, String> ();
    m.put(ZeiterfassungFac.TAGESART_MONTAG, ZeiterfassungFac.TAGESART_MONTAG);
    m.put(ZeiterfassungFac.TAGESART_DIENSTAG, ZeiterfassungFac.TAGESART_DIENSTAG);
    m.put(ZeiterfassungFac.TAGESART_MITTWOCH, ZeiterfassungFac.TAGESART_MITTWOCH);
    m.put(ZeiterfassungFac.TAGESART_DONNERSTAG, ZeiterfassungFac.TAGESART_DONNERSTAG);
    m.put(ZeiterfassungFac.TAGESART_FREITAG, ZeiterfassungFac.TAGESART_FREITAG);
    m.put(ZeiterfassungFac.TAGESART_SAMSTAG, ZeiterfassungFac.TAGESART_SAMSTAG);
    m.put(ZeiterfassungFac.TAGESART_SONNTAG, ZeiterfassungFac.TAGESART_SONNTAG);
    wcoSchichtwechsel.setMap(m);

    wlaSchichtwechsel.setText(LPMain.getInstance().getTextRespectUISPr(
        "pers.schichtwechsel"));
    wlaGueltigab.setText(LPMain.getInstance().getTextRespectUISPr("lp.gueltigab"));
    wlaBerechnebis.setText(LPMain.getInstance().getTextRespectUISPr(
        "pers.schichzeit.berechnebis"));

    wspJahrab = new WrapperSpinner(1970, 1970, 2100);
    wspJahrbis = new WrapperSpinner(1970, 1970, 2100);
    wspKwab = new WrapperSpinner(1, 1, 53);
    wspKwbis = new WrapperSpinner(1, 1, 53);

    Calendar cal = Calendar.getInstance();
    wspJahrab.setValue(new Integer(cal.get(cal.YEAR)));
    wspJahrbis.setValue(new Integer(cal.get(cal.YEAR)));
    wspKwab.setValue(new Integer(cal.get(cal.WEEK_OF_YEAR)));
    wspKwbis.setValue(new Integer(cal.get(cal.WEEK_OF_YEAR)));
    wdfAb.setDate(cal.getTime());
    wdfAb.getDisplay().addPropertyChangeListener(this);
    wdfBis.setDate(cal.getTime());
    wdfBis.getDisplay().addPropertyChangeListener(this);
    wdfAb.setMandatoryField(true);
    wdfBis.setMandatoryField(true);
    wdrBereich = new WrapperDateRangeController(wdfAb, wdfBis);
    //Actionpanel von Oberklasse holen und anhaengen.
    jpaButtonAction = getToolsPanel();
    this.setActionMap(null);

    getInternalFrame().addItemChangedListener(this);

    jpaWorkingOn = new JPanel();
    gridBagLayoutWorkingPanel = new GridBagLayout();
    jpaWorkingOn.setLayout(gridBagLayoutWorkingPanel);
    this.add(jpaButtonAction, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
        , GridBagConstraints.WEST, GridBagConstraints.NONE,
        new Insets(0, 0, 0, 0), 0, 0));

    this.add(jpaWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0
                                                  , GridBagConstraints.SOUTHEAST,
                                                  GridBagConstraints.BOTH,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    int iZeile = 0;

    jpaWorkingOn.add(wbuSchicht1,
                     new GridBagConstraints(0, iZeile, 1, 1, 0.5, 0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.BOTH,
                                            new Insets(2, 2, 2, 2), 0,
                                            0));
    jpaWorkingOn.add(wtfSchicht1,
                     new GridBagConstraints(1, iZeile, 3, 1, 1.0, 0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.BOTH,
                                            new Insets(2, 2, 2, 2), 0,
                                            0));
    iZeile++;
    jpaWorkingOn.add(wbuSchicht2,
                     new GridBagConstraints(0, iZeile, 1, 1, 0, 0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.BOTH,
                                            new Insets(2, 2, 2, 2), 0,
                                            0));
    jpaWorkingOn.add(wtfSchicht2,
                     new GridBagConstraints(1, iZeile, 3, 1, 0, 0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.BOTH,
                                            new Insets(2, 2, 2, 2), 0,
                                            0));
    iZeile++;
    jpaWorkingOn.add(wbuSchicht3,
                     new GridBagConstraints(0, iZeile, 1, 1, 0, 0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.BOTH,
                                            new Insets(2, 2, 2, 2), 0,
                                            0));
    jpaWorkingOn.add(wtfSchicht3,
                     new GridBagConstraints(1, iZeile, 3, 1, 0, 0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.BOTH,
                                            new Insets(2, 2, 2, 2), 0,
                                            0));
    iZeile++;
    jpaWorkingOn.add(wbuSchicht4,
                     new GridBagConstraints(0, iZeile, 1, 1, 0, 0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.BOTH,
                                            new Insets(2, 2, 2, 2), 0,
                                            0));
    jpaWorkingOn.add(wtfSchicht4,
                     new GridBagConstraints(1, iZeile, 3, 1, 0, 0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.BOTH,
                                            new Insets(2, 2, 2, 2), 0,
                                            0));
    iZeile++;
    jpaWorkingOn.add(wlaSchichtwechsel,
                     new GridBagConstraints(0, iZeile, 1, 1, 0, 0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.BOTH,
                                            new Insets(2, 2, 2, 2), 0,
                                            0));
    jpaWorkingOn.add(wcoSchichtwechsel,
                     new GridBagConstraints(1, iZeile, 1, 1, 0, 0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.BOTH,
                                            new Insets(2, 2, 2, 2), 0,
                                            0));
    iZeile++;
    jpaWorkingOn.add(wlaGueltigab,
                     new GridBagConstraints(0, iZeile, 1, 1, 0, 0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.BOTH,
                                            new Insets(2, 2, 2, 2), 0,
                                            0));
    jpaWorkingOn.add(wspKwab,
                     new GridBagConstraints(1, iZeile, 1, 1, 0.3, 0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.BOTH,
                                            new Insets(2, 2, 2, 2), 0,
                                            0));
    jpaWorkingOn.add(wspJahrab,
                     new GridBagConstraints(2, iZeile, 1, 1, 0.3, 0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.BOTH,
                                            new Insets(2, 2, 2, 2), 0,
                                            0));
    jpaWorkingOn.add(wdfAb,
                     new GridBagConstraints(3, iZeile, 1, 1, 0.3, 0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.BOTH,
                                            new Insets(2, 2, 2, 2), 0,
                                            0));
    iZeile++;
    jpaWorkingOn.add(wlaBerechnebis,
                     new GridBagConstraints(0, iZeile, 1, 1, 0, 0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.BOTH,
                                            new Insets(2, 2, 2, 2), 0,
                                            0));
    jpaWorkingOn.add(wspKwbis,
                     new GridBagConstraints(1, iZeile, 1, 1, 0, 0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.BOTH,
                                            new Insets(2, 2, 2, 2), 0,
                                            0));
    jpaWorkingOn.add(wspJahrbis,
                     new GridBagConstraints(2, iZeile, 1, 1, 0, 0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.BOTH,
                                            new Insets(2, 2, 2, 2), 0,
                                            0));
    jpaWorkingOn.add(wdfBis,
                     new GridBagConstraints(3, iZeile, 1, 1, 0, 0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.BOTH,
                                            new Insets(2, 2, 2, 2), 0,
                                            0));

    jpaWorkingOn.add(wdrBereich,
                     new GridBagConstraints(4, iZeile, 1, 1, 0.0, 0.0,
                                            GridBagConstraints.WEST,
                                            GridBagConstraints.BOTH,
                                            new Insets(2, 2, 2, 2),
                                            0, 0));

    String[] aWhichButtonIUse = {
        ACTION_SAVE,
        ACTION_DISCARD,
    };

    enableToolsPanelButtons(aWhichButtonIUse);

  }


  protected String getLockMeWer()
      throws Exception {
    return HelperClient.LOCKME_ZEITMODELL;
  }


  protected void eventActionDiscard(ActionEvent e)
      throws Throwable {
    dialog.closeDialog();
  }


  public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
      throws Throwable {
    super.eventYouAreSelected(false);
    Object key = getKeyWhenDetailPanel();

    if (key == null
        || (key.equals(LPMain.getLockMeForNew()))) {

      leereAlleFelder(this);

      clearStatusbar();
      // wdfGueltigab.setTimestamp(new java.sql.Timestamp(System.currentTimeMillis()));
    }
  }
}
