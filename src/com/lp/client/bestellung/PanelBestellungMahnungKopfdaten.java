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
package com.lp.client.bestellung;


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
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.WrapperComboBox;
import com.lp.client.frame.component.WrapperDateField;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.bestellung.service.BSMahnungDto;
import com.lp.server.bestellung.service.BestellungDto;
import com.lp.server.partner.service.LieferantDto;

@SuppressWarnings("static-access") 
public class PanelBestellungMahnungKopfdaten
    extends PanelBasis
{
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private WrapperLabel wlaDatum = new WrapperLabel();
  private WrapperLabel wlaMahnstufe = new WrapperLabel();
  private WrapperLabel wlaLetzteMahnstufe = new WrapperLabel();
  private WrapperDateField wdfDatum = new WrapperDateField();
  private WrapperNumberField wnfLetzteMahnstufe = new WrapperNumberField();
  private Border border1;
  private GridBagLayout gridBagLayout1 = new GridBagLayout();
  private JPanel jPanelWorkingOn = new JPanel();
  private GridBagLayout gridBagLayout3 = new GridBagLayout();
  private TabbedPaneBESMahnwesen tpBSMahnwesen = null;
  private BSMahnungDto bsmahnungDto = null;
  private WrapperComboBox wcoMahnstufe = new WrapperComboBox();

  private final static String ACTION_SPECIAL_MAHNUNG_ZURUECKNEHMEN =
      "action_special_mahnung_zuruecknehmen";

  public PanelBestellungMahnungKopfdaten(InternalFrame internalFrame, String add2TitleI,
                                         Object key, TabbedPaneBESMahnwesen tpBSMahnwesen)
      throws Throwable {
    super(internalFrame, add2TitleI, key);
    this.tpBSMahnwesen = tpBSMahnwesen;
    jbInit();
    initPanel();
    initComponents();
  }


  /**
   * initPanel
   *
   * @throws Throwable
   */
  private void initPanel()
      throws Throwable {
    wcoMahnstufe.setMap(DelegateFactory.getInstance().getBSMahnwesenDelegate().getAllBSMahnstufen());
  }


  /**
   * jbInit
   *
   * @throws Throwable
   */
  private void jbInit()
      throws Throwable {
    border1 = BorderFactory.createEmptyBorder(10, 10, 10, 10);
    this.setLayout(gridBagLayout1);
    this.setBorder(BorderFactory.createEtchedBorder());
    JPanel panelButtonAction = getToolsPanel();
    createAndSaveAndShowButton(
        "/com/lp/client/res/leeren.png",
        LPMain.getTextRespectUISPr("finanz.tooltip.mahnungzuruecknehmen"),
        ACTION_SPECIAL_MAHNUNG_ZURUECKNEHMEN,
        RechteFac.RECHT_BES_BESTELLUNG_CUD);
    String[] aWhichButtonIUse = {
        ACTION_UPDATE,
        ACTION_SAVE,
        ACTION_DELETE,
        ACTION_DISCARD,
        ACTION_SPECIAL_MAHNUNG_ZURUECKNEHMEN};
    enableToolsPanelButtons(aWhichButtonIUse);
    //... bis hier ist's immer gleich
    //wegen dialogFLR
    getInternalFrame().addItemChangedListener(this);

    jPanelWorkingOn.setBorder(border1);
    jPanelWorkingOn.setOpaque(true);
    jPanelWorkingOn.setLayout(gridBagLayout3);
    wlaDatum.setMinimumSize(new Dimension(100, Defaults.getInstance().getControlHeight()));
    wlaDatum.setPreferredSize(new Dimension(100, Defaults.getInstance().getControlHeight()));
    wlaDatum.setText(LPMain.getInstance().getTextRespectUISPr("lp.datum"));
    wlaMahnstufe.setText("Mahnstufe");
    wlaLetzteMahnstufe.setText("Letzte Mahnstufe");
    wcoMahnstufe.setMandatoryFieldDB(true);
    wdfDatum.setMandatoryFieldDB(true);
    wnfLetzteMahnstufe.setActivatable(false);
    wnfLetzteMahnstufe.setFractionDigits(0);

    this.add(panelButtonAction,
             new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST,
                                    GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    this.add(jPanelWorkingOn,
             new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
                                    GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    this.add(getPanelStatusbar(),
             new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
                                    GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    jPanelWorkingOn.add(wlaDatum,
                        new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0,
                                               GridBagConstraints.CENTER,
                                               GridBagConstraints.NONE,
                                               new Insets(0, 0, 0, 0), 0, 0));
    jPanelWorkingOn.add(wdfDatum,
                        new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0,
                                               GridBagConstraints.CENTER,
                                               GridBagConstraints.BOTH,
                                               new Insets(2, 2, 2, 2), 0, 0));
    iZeile++;
    jPanelWorkingOn.add(wlaMahnstufe,
                        new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0,
                                               GridBagConstraints.CENTER,
                                               GridBagConstraints.BOTH,
                                               new Insets(2, 2, 2, 2), 0, 0));
    jPanelWorkingOn.add(wcoMahnstufe,
                        new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0,
                                               GridBagConstraints.CENTER,
                                               GridBagConstraints.BOTH,
                                               new Insets(2, 2, 2, 2), 0, 0));
    jPanelWorkingOn.add(wlaLetzteMahnstufe,
                        new GridBagConstraints(2, iZeile, 1, 1, 0.0, 0.0,
                                               GridBagConstraints.CENTER,
                                               GridBagConstraints.BOTH,
                                               new Insets(2, 2, 2, 2), 0, 0));
    jPanelWorkingOn.add(wnfLetzteMahnstufe,
                        new GridBagConstraints(3, iZeile, 1, 1, 0.0, 0.0,
                                               GridBagConstraints.CENTER,
                                               GridBagConstraints.BOTH,
                                               new Insets(2, 2, 2, 2), 0, 0));
  }


  protected String getLockMeWer() {
    return HelperClient.LOCKME_FINANZ_MAHNLAUF;
  }


  private void dto2Components()
      throws Throwable {
    if (bsmahnungDto != null) {
      wdfDatum.setDate(bsmahnungDto.getTMahndatum());
      wcoMahnstufe.setKeyOfSelectedItem(bsmahnungDto.getMahnstufeIId());
      wnfLetzteMahnstufe.setInteger(bsmahnungDto.getMahnstufeIId());
    }
    else {
      wdfDatum.setDate(null);
    }
  }


  private void components2Dto() {
    bsmahnungDto.setTMahndatum(wdfDatum.getDate());
    bsmahnungDto.setMahnstufeIId( (Integer) wcoMahnstufe.getKeyOfSelectedItem());
  }


  /**
   * Loeschen einer Mahnung
   *
   * @param e ActionEvent
   * @param bAdministrateLockKeyI boolean
   * @param bNeedNoDeleteI boolean
   * @throws Throwable
   */
  protected void eventActionDelete(ActionEvent e, boolean bAdministrateLockKeyI,
                                   boolean bNeedNoDeleteI)
      throws Throwable {
    if (bsmahnungDto != null) {
      if (bsmahnungDto.getIId() != null) {
        if (!isLockedDlg()) {
          DelegateFactory.getInstance().getBSMahnwesenDelegate().removeBSMahnung(bsmahnungDto);
          this.setBSMahnungDto(null);
          this.leereAlleFelder(this);
          super.eventActionDelete(e, false, false);
        }
      }
    }
  }


  public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
      throws Throwable {
    super.eventYouAreSelected(false);
    Object key = getKeyWhenDetailPanel();
    if (key == null
        || (key != null && key.equals(LPMain.getLockMeForNew()))) {
      // einen neuen Eintrag anlegen oder die letzte Position wurde geloescht.
      leereAlleFelder(this);
      dto2Components();
      clearStatusbar();
    }
    else {
      this.setBSMahnungDto( (DelegateFactory.getInstance().getBSMahnwesenDelegate().
                           bsmahnungFindByPrimaryKey( (
                               Integer) key)));
      dto2Components();
    }
  }


  public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
      throws
      Throwable {
    if (allMandatoryFieldsSetDlg()) {
      components2Dto();
      if (bsmahnungDto != null) {
        BSMahnungDto savedDto = DelegateFactory.getInstance().getBSMahnwesenDelegate().
            updateBSMahnung(bsmahnungDto);
        setBSMahnungDto(savedDto);
        super.eventActionSave(e, true);
        if (getInternalFrame().getKeyWasForLockMe() == null) {
          getInternalFrame().setKeyWasForLockMe(tpBSMahnwesen.getBSMahnlaufDto().getIId().
                                                toString());
        }
        eventYouAreSelected(false);
      }
    }
  }


  protected void eventItemchanged(EventObject eI)
      throws Throwable {
  }


  private void setBSMahnungDto(BSMahnungDto bsmahnungDto)
      throws Throwable {
    this.bsmahnungDto = bsmahnungDto;
    String sTitle = null;
    Integer key = null;

    if (bsmahnungDto != null) {
      key = bsmahnungDto.getIId();
      BestellungDto bestellungDto = DelegateFactory.getInstance().getBestellungDelegate().
          bestellungFindByPrimaryKey(bsmahnungDto.getBestellungIId());
      LieferantDto lieferantDto = DelegateFactory.getInstance().getLieferantDelegate().lieferantFindByPrimaryKey(
          bestellungDto.getLieferantIIdBestelladresse());
      sTitle = bestellungDto.getCNr() + " " +
          lieferantDto.getPartnerDto().formatFixTitelName1Name2();
    }
    else {
      sTitle = "";
    }
    getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_AS_I_LIKE, sTitle);
    setKeyWhenDetailPanel(key);
  }


  protected void eventActionSpecial(ActionEvent e)
      throws Throwable {
    if (e.getActionCommand().equals(ACTION_SPECIAL_MAHNUNG_ZURUECKNEHMEN)) {
      DelegateFactory.getInstance().getBSMahnwesenDelegate().mahneBSMahnungRueckgaengig(bsmahnungDto.
          getIId());
      // wirkt fuer diesen Filter wie Loeschen -> QP informieren
      ItemChangedEvent it = new ItemChangedEvent(this,
                                                 ItemChangedEvent.
                                                 ACTION_GOTO_MY_DEFAULT_QP);
      this.tpBSMahnwesen.lPEventItemChanged(it);
    }
  }


  protected JComponent getFirstFocusableComponent()
      throws Exception {
    return wdfDatum;
  }
}
