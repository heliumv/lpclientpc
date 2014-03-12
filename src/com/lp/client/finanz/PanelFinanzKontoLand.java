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
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.component.WrapperTextNumberField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.util.fastlanereader.gui.QueryType;
import com.lp.server.finanz.ejb.KontolandPK;
import com.lp.server.finanz.service.KontoDto;
import com.lp.server.finanz.service.KontolandDto;
import com.lp.server.system.service.LandDto;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.FilterKriteriumDirekt;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;

@SuppressWarnings("static-access") 
/**
 * <p> Diese Klasse kuemmert sich um die Kontolaenderart</p>
 *
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 *
 * <p>Erstellung: Martin Bluehweis; 30.09.05</p>
 *
 * <p>@author $Author: christoph $</p>
 *
 * @version not attributable Date $Date: 2009/08/19 08:08:37 $
 */
public class PanelFinanzKontoLand
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

  private WrapperButton wbuKonto = null;
  private WrapperTextNumberField wtnfKontonummer = null;
  private WrapperTextField wtfKontobezeichnung = null;

  private WrapperButton wbuLand = null;
  private WrapperTextField wtfLand = null;

  private final static String ACTION_SPECIAL_LAND = "action_special_laenderart";
  private final static String ACTION_SPECIAL_KONTO = "action_special_konto";
  private PanelQueryFLR panelQueryFLRLand = null;
  private PanelQueryFLR panelQueryFLRKonto = null;

  private KontolandDto klDto = null;
  private KontoDto kontoDto = null;
  private LandDto landDto = null;

  private TabbedPaneKonten tabbedPaneKonten = null;

  public PanelFinanzKontoLand(InternalFrame internalFrame, String add2TitleI,
                                    Object pk, TabbedPaneKonten tabbedPaneKonten)
      throws Throwable {
    super(internalFrame, add2TitleI, pk);
    this.tabbedPaneKonten = tabbedPaneKonten;
    jbInit();
    setDefaults();
    initComponents();
  }


  private void setDefaults()
      throws Throwable {
  }


  public void eventActionNew(EventObject eventObject, boolean bLockMeI,
                             boolean bNeedNoNewI)
      throws Throwable {
    super.eventActionNew(eventObject, true, false);
    klDto = null;
    leereAlleFelder(this);
    setDefaults();
    clearStatusbar();
  }


  private void dto2Components()
      throws Throwable {
    if (klDto != null) {
      holeLand(klDto.getLandIId());
      holeKonto(klDto.getKontoIIdUebersetzt());
      // StatusBar
      this.setStatusbarPersonalIIdAnlegen(klDto.getPersonalIIdAnlegen());
      this.setStatusbarTAnlegen(klDto.getTAnlegen());
      this.setStatusbarPersonalIIdAendern(klDto.getPersonalIIdAendern());
      this.setStatusbarTAendern(klDto.getTAendern());
    }
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
      KontolandPK kontolandPK = (KontolandPK) key;
      klDto = DelegateFactory.getInstance().getFinanzDelegate().kontolandFindByPrimaryKey(kontolandPK.getKonto_i_id(),kontolandPK.getLand_i_id());
      dto2Components();
    }
  }


  private void components2Dto()
      throws Throwable {
    if (klDto == null) {
      klDto = new KontolandDto();

    }
    klDto.setKontoIId(tabbedPaneKonten.getKontoDto().getIId());
    klDto.setLandIId(landDto.getIID());
    klDto.setKontoIIdUebersetzt(kontoDto.getIId());
  }


  public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
      throws Throwable {
    if (allMandatoryFieldsSetDlg()) {
      components2Dto();
      DelegateFactory.getInstance().getFinanzDelegate().updateKontoland(klDto);
      setKeyWhenDetailPanel(new KontolandPK(klDto.getKontoIId(),
          klDto.getKontoIId()));
      super.eventActionSave(e, true);
      eventYouAreSelected(false);
    }
  }


  protected void eventActionDelete(ActionEvent e, boolean bAdministrateLockKeyI,
                                   boolean bNeedNoDeleteI)
      throws
      Throwable {
    DelegateFactory.getInstance().getFinanzDelegate().removeKontoland(klDto);
    super.eventActionDelete(e, false, false);
  }


  protected void eventItemchanged(EventObject eI)
      throws Throwable {
    ItemChangedEvent e = (ItemChangedEvent) eI;
    if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
      if (e.getSource() == panelQueryFLRLand) {
        Object key = ( (ISourceEvent) e.getSource()).getIdSelected();
        holeLand( (Integer) key);
      }
      else if (e.getSource() == panelQueryFLRKonto) {
        Object key = ( (ISourceEvent) e.getSource()).getIdSelected();
        holeKonto( (Integer) key);
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

    getInternalFrame().addItemChangedListener(this);

    wbuKonto = new WrapperButton();
    wbuLand = new WrapperButton();
    wtfKontobezeichnung = new WrapperTextField();
    wtfLand = new WrapperTextField();
    wtnfKontonummer = new WrapperTextNumberField();

    wtfKontobezeichnung.setActivatable(false);
    wtfLand.setActivatable(false);
    wtnfKontonummer.setActivatable(false);

    wtnfKontonummer.setMandatoryFieldDB(true);
    wtfLand.setMandatoryFieldDB(true);

    wbuKonto.addActionListener(this);
    wbuLand.addActionListener(this);
    wbuKonto.setActionCommand(ACTION_SPECIAL_KONTO);
    wbuLand.setActionCommand(ACTION_SPECIAL_LAND);


    wbuLand.setText(LPMain.getInstance().getTextRespectUISPr("button.land"));
    wbuLand.setToolTipText(LPMain.getInstance().getTextRespectUISPr(
        "button.land.tooltip"));
    wbuKonto.setText(LPMain.getInstance().getTextRespectUISPr("button.konto"));
    wbuKonto.setToolTipText(LPMain.getInstance().getTextRespectUISPr(
        "button.konto.tooltip"));
    wbuKonto.setMinimumSize(new Dimension(120, Defaults.getInstance().getControlHeight()));
    wbuKonto.setPreferredSize(new Dimension(120, Defaults.getInstance().getControlHeight()));

    this.add(jpaButtonAction, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
        , GridBagConstraints.WEST, GridBagConstraints.NONE,
        new Insets(0, 0, 0, 0), 0, 0));
    jpaWorkingOn = new JPanel();
    gridBagLayoutWorkingPanel = new GridBagLayout();
    jpaWorkingOn.setLayout(gridBagLayoutWorkingPanel);
    this.add(jpaWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0
        , GridBagConstraints.SOUTHEAST, GridBagConstraints.BOTH,
        new Insets(0, 0, 0, 0), 0, 0));
    this.add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.BOTH,
        new Insets(0, 0, 0, 0), 0, 0));
    iZeile++;
    jpaWorkingOn.add(wbuLand,
                        new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0,
                                               GridBagConstraints.CENTER,
                                               GridBagConstraints.BOTH,
                                               new Insets(2, 2, 2, 2),
                                               0, 0));
    jpaWorkingOn.add(wtfLand,
                        new GridBagConstraints(1, iZeile, 2, 1, 0.0, 0.0,
                                               GridBagConstraints.CENTER,
                                               GridBagConstraints.BOTH,
                                               new Insets(2, 2, 2, 2),
                                               0, 0));
    iZeile++;
    jpaWorkingOn.add(wbuKonto,
                        new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0,
                                               GridBagConstraints.CENTER,
                                               GridBagConstraints.BOTH,
                                               new Insets(2, 2, 2, 2),
                                               0, 0));
    jpaWorkingOn.add(wtnfKontonummer,
                        new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0
                                               , GridBagConstraints.CENTER,
                                               GridBagConstraints.BOTH,
                                               new Insets(2, 2, 2, 2), 0, 0));
    jpaWorkingOn.add(wtfKontobezeichnung,
                        new GridBagConstraints(2, iZeile, 1, 1, 1.0, 0.0
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


  protected String getLockMeWer() {
    return HelperClient.LOCKME_FINANZ_KONTO;
  }


  /**
   * holeKonto.
   *
   * @param key Object
   * @throws Throwable
   */
  private void holeKonto(Integer key)
      throws Throwable {
    if (key != null) {
      this.kontoDto = DelegateFactory.getInstance().getFinanzDelegate().kontoFindByPrimaryKey(key);
      dto2ComponentsKonto();
    }
  }


  /**
   * holeKonto.
   *
   * @param key Object
   * @throws Throwable
   */
  private void holeLand(Integer key)
      throws Throwable {
    if (key != null) {
      this.landDto = DelegateFactory.getInstance().getSystemDelegate().landFindByPrimaryKey(key);
      dto2ComponentsLand();
    }
  }


  private void dto2ComponentsKonto() {
    if (kontoDto != null) {
      wtnfKontonummer.setText(kontoDto.getCNr());
      wtfKontobezeichnung.setText(kontoDto.getCBez());
    }
    else {
      wtnfKontonummer.setText(null);
      wtfKontobezeichnung.setText(null);
    }
  }


  private void dto2ComponentsLand() {
    if (landDto != null) {
      wtfLand.setText(landDto.getCName());
    }
    else {
      wtfLand.setText(null);
    }
  }


  private void dialogQueryKonto(ActionEvent e)
      throws Throwable {
    String[] aWhichButtonIUse = {
        PanelBasis.ACTION_REFRESH
    };
    QueryType[] qt = null;
    // nur Sachkonten dieses Mandanten
    FilterKriterium[] filters = FinanzFilterFactory.getInstance().createFKKonten(
        tabbedPaneKonten.getKontotyp());

    panelQueryFLRKonto = new PanelQueryFLR(
        qt,
        filters,
        QueryParameters.UC_ID_FINANZKONTEN,
        aWhichButtonIUse,
        getInternalFrame(),
        LPMain.getInstance().getTextRespectUISPr("finanz.liste.konten"));
    FilterKriteriumDirekt fkDirekt1 = FinanzFilterFactory.getInstance().
        createFKDKontonummer();
    FilterKriteriumDirekt fkDirekt2 = FinanzFilterFactory.getInstance().
        createFKDKontobezeichnung();
    panelQueryFLRKonto.befuellePanelFilterkriterienDirekt(fkDirekt1, fkDirekt2);
    if(kontoDto != null) {
      panelQueryFLRKonto.setSelectedId(kontoDto.getIId());
    }
    new DialogQuery(panelQueryFLRKonto);
  }


  private void dialogQueryLand(ActionEvent e)
      throws Throwable {
    String[] aWhichButtonIUse = {
        PanelBasis.ACTION_REFRESH
    };
    QueryType[] qt = null;
    // Inland nicht uebersetzen
   // SystemFilterFactory.getInstance().createFKDLandName();

    panelQueryFLRLand = new PanelQueryFLR(
        qt,
        null,
        QueryParameters.UC_ID_LAND,
        aWhichButtonIUse,
        getInternalFrame(),
        LPMain.getInstance().getTextRespectUISPr("finanz.tab.oben.land.title"));
    if (landDto != null) {
      panelQueryFLRLand.setSelectedId(landDto.getIID());
    }
    new DialogQuery(panelQueryFLRLand);
  }


  protected void eventActionSpecial(ActionEvent e)
      throws Throwable {
    if (e.getActionCommand().equals(ACTION_SPECIAL_KONTO)) {
      dialogQueryKonto(e);
    }
    else if (e.getActionCommand().equals(ACTION_SPECIAL_LAND)) {
      dialogQueryLand(e);
    }
  }


  protected JComponent getFirstFocusableComponent()
      throws Exception {
    return wbuLand;
  }
}
