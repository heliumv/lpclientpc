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
package com.lp.client.personal;


import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.EventObject;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperComboBox;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperSpinner;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.personal.service.GleitzeitsaldoDto;

@SuppressWarnings("static-access")
public class PanelGleitzeitsaldo
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
  private GleitzeitsaldoDto gleitzeitsaldoDto = null;
  private WrapperSpinner wspJahr = new WrapperSpinner(new Integer(0), new Integer(0),
                                              new Integer(9999), new Integer(1));

  private WrapperLabel wlaJahr = new WrapperLabel();
  private WrapperLabel wlaMonat = new WrapperLabel();
  private WrapperLabel wlaMehrstunden = new WrapperLabel();
  private WrapperLabel wlaUestfrei50 = new WrapperLabel();
  private WrapperLabel wlaUestpflichtig50 = new WrapperLabel();
  private WrapperLabel wlaUestfrei100 = new WrapperLabel();
  private WrapperLabel wlaUestpflichtig100 = new WrapperLabel();
  private WrapperLabel wlaUest200 = new WrapperLabel();
  private WrapperComboBox wcoMonat = new WrapperComboBox();
  private WrapperNumberField wnfMehrstunden = new WrapperNumberField();
  private WrapperNumberField wnfUestfrei50 = new WrapperNumberField();
  private WrapperNumberField wnfUestpflichtig50 = new WrapperNumberField();
  private WrapperNumberField wnfUestfrei100 = new WrapperNumberField();
  private WrapperNumberField wnfUestpflichtig100 = new WrapperNumberField();
  private WrapperNumberField wnfUest200 = new WrapperNumberField();
  private WrapperCheckBox wcbGesperrt = new WrapperCheckBox();
  private WrapperLabel wrapperLabel1 = new WrapperLabel();
  private WrapperLabel wrapperLabel2 = new WrapperLabel();
  private WrapperLabel wrapperLabel3 = new WrapperLabel();
  private WrapperLabel wrapperLabel4 = new WrapperLabel();
  private WrapperLabel wrapperLabel5 = new WrapperLabel();
  private WrapperLabel wrapperLabel6 = new WrapperLabel();

  private WrapperLabel wlaSaldo = new WrapperLabel();
  private WrapperNumberField wnfSaldo = new WrapperNumberField();

  public PanelGleitzeitsaldo(InternalFrame internalFrame, String add2TitleI,
                             Object pk)

      throws Throwable {
    super(internalFrame, add2TitleI, pk);
    internalFramePersonal = (InternalFramePersonal) internalFrame;
    jbInit();
    setDefaults();
    initComponents();
    enableAllComponents(this, false);
  }
  protected JComponent getFirstFocusableComponent()
      throws Exception {
    return wnfMehrstunden;
  }


  protected void setDefaults() {

    DateFormatSymbols symbols = new DateFormatSymbols(LPMain.getInstance().
        getUISprLocale());
    String[] defaultMonths = symbols.getMonths();
    Map<Integer, String> m = new TreeMap<Integer, String>();
    for (int i = 0; i < defaultMonths.length - 1; i++) {
      m.put(new Integer(i), defaultMonths[i]);

    }
    wcoMonat.setMap(m);

  }


  public void eventActionNew(EventObject eventObject, boolean bLockMeI,
                             boolean bNeedNoNewI)
      throws Throwable {
    super.eventActionNew(eventObject, true, false);
    gleitzeitsaldoDto = new GleitzeitsaldoDto();

    leereAlleFelder(this);

    Calendar cal = Calendar.getInstance();
    wcoMonat.setSelectedIndex(cal.get(Calendar.MONTH));
    wspJahr.setValue(new Integer(cal.get(Calendar.YEAR)));

  }


  protected void eventActionSpecial(ActionEvent e)
      throws Throwable {
  }


  protected void eventActionDelete(ActionEvent e, boolean bAdministrateLockKeyI,
                                   boolean bNeedNoDeleteI)
      throws Throwable {
    DelegateFactory.getInstance().getPersonalDelegate().removeGleitzeitsaldo(
        gleitzeitsaldoDto);
    this.setKeyWhenDetailPanel(null);
    super.eventActionDelete(e, false, false);
  }


  protected void components2Dto()
      throws ExceptionLP {
    gleitzeitsaldoDto.setPersonalIId(internalFramePersonal.getPersonalDto().getIId());
    gleitzeitsaldoDto.setIJahr( (Integer) wspJahr.getValue());
    gleitzeitsaldoDto.setIMonat( (Integer) wcoMonat.getKeyOfSelectedItem());

    gleitzeitsaldoDto.setNSaldomehrstunden(wnfMehrstunden.getBigDecimal());
    gleitzeitsaldoDto.setNSaldo(wnfSaldo.getBigDecimal());
    gleitzeitsaldoDto.setNSaldouestfrei100(wnfUestfrei100.getBigDecimal());
    gleitzeitsaldoDto.setNSaldouestfrei50(wnfUestfrei50.getBigDecimal());
    gleitzeitsaldoDto.setNSaldouestpflichtig100(wnfUestpflichtig100.getBigDecimal());
    gleitzeitsaldoDto.setNSaldouestpflichtig50(wnfUestpflichtig50.getBigDecimal());
    gleitzeitsaldoDto.setNSaldouest200(wnfUest200.getBigDecimal());

    gleitzeitsaldoDto.setBGesperrt(wcbGesperrt.getShort());

  }


  protected void dto2Components()
      throws ExceptionLP, Throwable {
    wspJahr.setValue(gleitzeitsaldoDto.getIJahr());
    wcoMonat.setKeyOfSelectedItem(gleitzeitsaldoDto.getIMonat());

    wnfMehrstunden.setBigDecimal(gleitzeitsaldoDto.getNSaldomehrstunden());
    wnfUestfrei100.setBigDecimal(gleitzeitsaldoDto.getNSaldouestfrei100());
    wnfUestfrei50.setBigDecimal(gleitzeitsaldoDto.getNSaldouestfrei50());
    wnfUestpflichtig100.setBigDecimal(gleitzeitsaldoDto.getNSaldouestpflichtig100());
    wnfUestpflichtig50.setBigDecimal(gleitzeitsaldoDto.getNSaldouestpflichtig50());
    wnfSaldo.setBigDecimal(gleitzeitsaldoDto.getNSaldo());
    wnfUest200.setBigDecimal(gleitzeitsaldoDto.getNSaldouest200());
    
    wcbGesperrt.setShort(gleitzeitsaldoDto.getBGesperrt());

    this.setStatusbarPersonalIIdAendern(gleitzeitsaldoDto.getPersonalIIdAendern());
    this.setStatusbarTAendern(gleitzeitsaldoDto.getTAendern());

  }


  public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
      throws Throwable {
    if (allMandatoryFieldsSetDlg()) {
      components2Dto();
      if (gleitzeitsaldoDto.getIId() == null) {
        gleitzeitsaldoDto.setIId(DelegateFactory.getInstance().getPersonalDelegate().
                                 createGleitzeitsaldo(gleitzeitsaldoDto));
        setKeyWhenDetailPanel(gleitzeitsaldoDto.getIId());
      }
      else {
        DelegateFactory.getInstance().getPersonalDelegate().updateGleitzeitsaldo(
            gleitzeitsaldoDto);
      }
      super.eventActionSave(e, true);

      if (getInternalFrame().getKeyWasForLockMe() == null) {
        getInternalFrame().setKeyWasForLockMe(internalFramePersonal.getPersonalDto().
                                              getIId() + "");
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
    border = BorderFactory.createEmptyBorder(10, 10, 10, 10);
    setBorder(border);
    //das Aussenpanel hat immer das Gridbaglayout.
    gridBagLayoutAll = new GridBagLayout();
    this.setLayout(gridBagLayoutAll);
    getInternalFrame().addItemChangedListener(this);

    //Actionpanel von Oberklasse holen und anhaengen.
    jpaButtonAction = getToolsPanel();
    this.setActionMap(null);
    jpaWorkingOn = new JPanel();
    gridBagLayoutWorkingPanel = new GridBagLayout();
    jpaWorkingOn.setLayout(gridBagLayoutWorkingPanel);

    wlaJahr.setText(LPMain.getInstance().getTextRespectUISPr("lp.jahr"));
    //jetzt meine felder
    jpaWorkingOn = new JPanel();
    gridBagLayoutWorkingPanel = new GridBagLayout();
    jpaWorkingOn.setLayout(gridBagLayoutWorkingPanel);
    wspJahr.setMandatoryField(true);
    wlaMonat.setText(LPMain.getInstance().getTextRespectUISPr("lp.monat1"));
    wlaSaldo.setText(LPMain.getInstance().getTextRespectUISPr(
        "pers.title.tab.gleitzeitsaldo"));
    wlaMehrstunden.setText(LPMain.getInstance().getTextRespectUISPr(
        "pers.gleitzeitsaldo.mehrstunden"));
    wlaUestfrei50.setText(LPMain.getInstance().getTextRespectUISPr(
        "pers.gleitzeitsaldo.steuerfrei50"));
    wlaUestpflichtig50.setText(LPMain.getInstance().getTextRespectUISPr(
        "pers.gleitzeitsaldo.steuerpflichtig50"));
    wlaUestfrei100.setText(LPMain.getInstance().getTextRespectUISPr(
        "pers.gleitzeitsaldo.steuerfrei100"));
    wlaUestpflichtig100.setText(LPMain.getInstance().getTextRespectUISPr(
        "pers.gleitzeitsaldo.steuerpflichtig100"));
    wlaUest200.setText(LPMain.getInstance().getTextRespectUISPr(
    "pers.gleitzeitsaldo.200"));
    
    wcoMonat.setMandatoryField(true);

    wnfMehrstunden.setMandatoryField(true);
    wnfSaldo.setMandatoryField(true);
    wnfUestfrei100.setMandatoryField(true);
    wnfUestfrei50.setMandatoryField(true);
    wnfUestpflichtig100.setMandatoryField(true);
    wnfUestpflichtig50.setMandatoryField(true);
    wnfUest200.setMandatoryField(true);

    wcbGesperrt.setText(LPMain.getInstance().getTextRespectUISPr(
        "pers.gleitzeitsaldo.gesperrt"));
    wrapperLabel1.setHorizontalAlignment(SwingConstants.LEFT);
    wrapperLabel1.setText("h");
    wrapperLabel2.setHorizontalAlignment(SwingConstants.LEFT);
    wrapperLabel2.setText("h");
    wrapperLabel3.setHorizontalAlignment(SwingConstants.LEFT);
    wrapperLabel3.setText("h");
    wrapperLabel4.setHorizontalAlignment(SwingConstants.LEFT);
    wrapperLabel4.setText("h");
    wrapperLabel5.setHorizontalAlignment(SwingConstants.LEFT);
    wrapperLabel5.setText("h");
    wrapperLabel6.setHorizontalAlignment(SwingConstants.LEFT);
    wrapperLabel6.setText("h");
    this.add(jpaButtonAction, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
        , GridBagConstraints.WEST, GridBagConstraints.NONE,
        new Insets(0, 0, 0, 0), 0, 0));
    this.add(jpaWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0
        , GridBagConstraints.NORTHEAST, GridBagConstraints.BOTH,
        new Insets( -9, 0, 9, 0), 0, 0));
    this.add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.BOTH,
        new Insets(0, 0, 0, 0), 0, 0));

    iZeile++;
    jpaWorkingOn.add(wlaJahr, new GridBagConstraints(0, iZeile, 1, 1, 0.1, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
        0, 0));
    jpaWorkingOn.add(wspJahr, new GridBagConstraints(1, iZeile, 1, 1, 0.01, 0.0
        , GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 70, 0));
     iZeile++;
    jpaWorkingOn.add(wlaMonat, new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
        0, 0));
    jpaWorkingOn.add(wcoMonat, new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0
        , GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 70, 0));
    iZeile++;
   jpaWorkingOn.add(wlaSaldo, new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0
       , GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
       0, 0));
   jpaWorkingOn.add(wnfSaldo, new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0
       , GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
       0, 0));  iZeile++;
    jpaWorkingOn.add(wlaMehrstunden, new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
        0, 0));
    jpaWorkingOn.add(wnfMehrstunden, new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0
        , GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
        0, 0));


    jpaWorkingOn.add(wrapperLabel1, new GridBagConstraints(2, iZeile, 1, 1, 0.03, 0.0
    , GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
    0, 0));
    iZeile++;

    jpaWorkingOn.add(wlaUestfrei50, new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
        0, 0));

    jpaWorkingOn.add(wnfUestfrei50, new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0
        , GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
        0, 0));
    jpaWorkingOn.add(wrapperLabel2, new GridBagConstraints(2, iZeile, 1, 1, 0.0, 0.0
    , GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
    0, 0));
    iZeile++;

    jpaWorkingOn.add(wlaUestpflichtig50, new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
        0, 0));
    jpaWorkingOn.add(wnfUestpflichtig50, new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0
       , GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
       0, 0));
    jpaWorkingOn.add(wrapperLabel3, new GridBagConstraints(2, iZeile, 1, 1, 0.0, 0.0
    , GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
    0, 0));

    iZeile++;
    jpaWorkingOn.add(wlaUestfrei100, new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
        0, 0));
    jpaWorkingOn.add(wnfUestfrei100, new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0
      , GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
      0, 0));
    jpaWorkingOn.add(wrapperLabel4, new GridBagConstraints(2, iZeile, 1, 1, 0.0, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
        0, 0));
  iZeile++;
    jpaWorkingOn.add(wlaUestpflichtig100, new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
        0, 0));
    jpaWorkingOn.add(wnfUestpflichtig100, new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0
        , GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
        0, 0));
    jpaWorkingOn.add(wrapperLabel5, new GridBagConstraints(2, iZeile, 1, 1, 0.0, 0.0
     , GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
     0, 0));
    iZeile++;
    jpaWorkingOn.add(wlaUest200, new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0
            , GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
            0, 0));
        jpaWorkingOn.add(wnfUest200, new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0
            , GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
            0, 0));
        
        WrapperLabel w200=new WrapperLabel("h");
		w200.setHorizontalAlignment(SwingConstants.LEFT);
        
        jpaWorkingOn.add(w200, new GridBagConstraints(2, iZeile, 1, 1, 0.0, 0.0
         , GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
         0, 0));
        iZeile++;
    jpaWorkingOn.add(wcbGesperrt, new GridBagConstraints(1, iZeile, 2, 1, 0.0, 0.0
        , GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
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
    return HelperClient.LOCKME_PERSONAL;
  }


  public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
      throws Throwable {
    leereAlleFelder(this);
    super.eventYouAreSelected(false);
    Object key = getKeyWhenDetailPanel();

    if (key == null
        || (key.equals(LPMain.getLockMeForNew()))) {
      Calendar cal = Calendar.getInstance();
      wcoMonat.setSelectedIndex(cal.get(Calendar.MONTH));
      wspJahr.setValue(new Integer(cal.get(Calendar.YEAR)));
      wnfSaldo.setInteger(0);
      wnfUestfrei100.setInteger(0);
      wnfUestfrei50.setInteger(0);
      wnfUestpflichtig100.setInteger(0);
      wnfUest200.setInteger(0);
      wnfUestpflichtig50.setInteger(0);
      wnfMehrstunden.setInteger(0);
    }
    else {
      gleitzeitsaldoDto = DelegateFactory.getInstance().getPersonalDelegate().
          gleitzeitsaldoFindByPrimaryKey( (Integer) key);
      dto2Components();
    }
  }
}
