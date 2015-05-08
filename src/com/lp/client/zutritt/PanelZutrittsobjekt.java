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
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperComboBox;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.system.SystemFilterFactory;
import com.lp.server.artikel.service.ArtikelFac;
import com.lp.server.personal.service.ZutrittscontrollerDto;
import com.lp.server.personal.service.ZutrittscontrollerFac;
import com.lp.server.personal.service.ZutrittsobjektDto;
import com.lp.server.system.service.MandantDto;
import com.lp.server.system.service.SystemFac;
import com.lp.server.util.Facade;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;

@SuppressWarnings("static-access")
public class PanelZutrittsobjekt
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
  private WrapperButton wbuZutrittscontroller = new WrapperButton();
  private WrapperTextField wtfZutrittscontroller = new WrapperTextField();
  private WrapperLabel wlaKennung = new WrapperLabel();
  private WrapperTextField wtfKennung = new WrapperTextField();
  private WrapperLabel wlaBezeichnung = new WrapperLabel();
  private WrapperTextField wtfBezeichnung = new WrapperTextField();
  private WrapperLabel wlaRelais = new WrapperLabel();
  private WrapperTextField wtfRelais = new WrapperTextField();
  private WrapperLabel wlaLeser = new WrapperLabel();
  private WrapperComboBox wcoLeser = new WrapperComboBox();
  private WrapperLabel wlaAdresse = new WrapperLabel();
  private WrapperTextField wtfAdresse = new WrapperTextField();

  private WrapperLabel wlaOeffnung = new WrapperLabel();
  private WrapperNumberField wnfOeffnung = new WrapperNumberField();

  private WrapperButton wbuMandant = new WrapperButton();
  private WrapperTextField wtfMandant = new WrapperTextField();

  private PanelQueryFLR panelQueryFLRZutrittscontroller = null;
  private PanelQueryFLR panelQueryFLRMandant = null;

  static final public String ACTION_SPECIAL_ZUTRITTSCONTROLLER_FROM_LISTE =
      "ACTION_SPECIAL_ZUTRITTSCONTROLLER_FROM_LISTE";
  static final public String ACTION_SPECIAL_MANDANT_FROM_LISTE =
      "ACTION_SPECIAL_MANDANT_FROM_LISTE";
  private ZutrittsobjektDto zutrittsobjektDto = null;

  public PanelZutrittsobjekt(InternalFrame internalFrame, String add2TitleI,
                             Object pk)
      throws Throwable {
    super(internalFrame, add2TitleI, pk);
    jbInit();
    setDefaults();
    initComponents();
    enableAllComponents(this, false);
  }


  protected void setDefaults()
      throws Throwable {
    Map<?, ?> m = DelegateFactory.getInstance().getZutrittDelegate().getAllZutrittsleser();
    wcoLeser.setMap(m);
  }


  protected JComponent getFirstFocusableComponent()
      throws Exception {
    return wtfKennung;
  }


  protected void components2Dto()
      throws Throwable {
    zutrittsobjektDto.setCNr(wtfKennung.getText());
    zutrittsobjektDto.setCBez(wtfBezeichnung.getText());
    zutrittsobjektDto.setCRelais(wtfRelais.getText());
    zutrittsobjektDto.setZutrittsleserCNr( (String) wcoLeser.getKeyOfSelectedItem());
    zutrittsobjektDto.setCAdresse(wtfAdresse.getText());
    zutrittsobjektDto.setFOeffnungszeit(wnfOeffnung.getDouble());
  }


  protected void dto2Components()
      throws Throwable {
    wtfKennung.setText(zutrittsobjektDto.getCNr());
    wtfBezeichnung.setText(zutrittsobjektDto.getCBez());
    wtfRelais.setText(zutrittsobjektDto.getCRelais());
    wcoLeser.setKeyOfSelectedItem(zutrittsobjektDto.getZutrittsleserCNr());
    wtfAdresse.setText(zutrittsobjektDto.getCAdresse());
    wtfMandant.setText(zutrittsobjektDto.getMandantCNr());
    wnfOeffnung.setDouble(zutrittsobjektDto.getFOeffnungszeit());

    ZutrittscontrollerDto dto = DelegateFactory.getInstance().getZutrittDelegate().
        zutrittscontrollerFindByPrimaryKey(zutrittsobjektDto.getZutrittscontrollerIId());
    wtfZutrittscontroller.setText(dto.getCNr());

  }


  public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
      throws
      Throwable {
    if (allMandatoryFieldsSetDlg()) {
      components2Dto();
      if (zutrittsobjektDto.getIId() == null) {
        zutrittsobjektDto.setIId(DelegateFactory.getInstance().getZutrittDelegate().
                                 createZutrittsobjekt(
                                     zutrittsobjektDto));
        setKeyWhenDetailPanel(zutrittsobjektDto.getIId());
      }
      else {
        DelegateFactory.getInstance().getZutrittDelegate().updateZutrittsobjekt(
            zutrittsobjektDto);
      }

      super.eventActionSave(e, true);

      if (getInternalFrame().getKeyWasForLockMe() == null) {
        getInternalFrame().setKeyWasForLockMe(zutrittsobjektDto.getIId() + "");
      }
      eventYouAreSelected(false);
    }
  }


  public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
      throws Throwable {

    super.eventYouAreSelected(false);
    Object key = getKeyWhenDetailPanel();
    if (key == null
        || (key.equals(LPMain.getLockMeForNew()))) {
      leereAlleFelder(this);
      clearStatusbar();
      wcoLeser.setKeyOfSelectedItem(ZutrittscontrollerFac.ZUTRITTSLESER_PROXLINEMIFARE);
      wnfOeffnung.setInteger(2);
      wtfRelais.setText("4");

    }
    else {
      zutrittsobjektDto = DelegateFactory.getInstance().getZutrittDelegate().
          zutrittsobjektFindByPrimaryKey( (Integer) key);
      dto2Components();
    }
  }


  protected void eventActionDelete(ActionEvent e, boolean bAdministrateLockKeyI,
                                   boolean bNeedNoDeleteI)
      throws
      Throwable {
    DelegateFactory.getInstance().getZutrittDelegate().removeZutrittsobjekt(
        zutrittsobjektDto);
    this.setKeyWhenDetailPanel(null);
    super.eventActionDelete(e, false, false);
  }


  public void eventActionNew(EventObject eventObject, boolean bLockMeI,
                             boolean bNeedNoNewI)
      throws Throwable {
    super.eventActionNew(eventObject, true, false);
    zutrittsobjektDto = new ZutrittsobjektDto();
    leereAlleFelder(this);
  }


  protected void eventActionSpecial(ActionEvent e)
      throws Throwable {
    if (e.getActionCommand().equals(ACTION_SPECIAL_ZUTRITTSCONTROLLER_FROM_LISTE)) {
      dialogQueryZutrittscontrollerFromListe(e);
    }
    else if (e.getActionCommand().equals(ACTION_SPECIAL_MANDANT_FROM_LISTE)) {
      dialogQueryMandantFromListe(e);
    }
  }


  protected void eventItemchanged(EventObject eI)
      throws Throwable {
    ItemChangedEvent e = (ItemChangedEvent) eI;
    if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
      if (e.getSource() == panelQueryFLRZutrittscontroller) {
        Object key = ( (ISourceEvent) e.getSource()).getIdSelected();
        ZutrittscontrollerDto zutrittscontrollerDto = DelegateFactory.getInstance().
            getZutrittDelegate().
            zutrittscontrollerFindByPrimaryKey( (Integer)
                                               key);
        wtfZutrittscontroller.setText(zutrittscontrollerDto.getCNr());
        zutrittsobjektDto.setZutrittscontrollerIId(zutrittscontrollerDto.getIId());
      }
      else if (e.getSource() == panelQueryFLRMandant) {
        Object key = ( (ISourceEvent) e.getSource()).getIdSelected();
        MandantDto mandantDto = DelegateFactory.getInstance().
            getMandantDelegate().
            mandantFindByPrimaryKey( (String)
                                    key);
        wtfMandant.setText(mandantDto.getCNr());
        zutrittsobjektDto.setMandantCNr(mandantDto.getCNr());
      }
    }
    else if (e.getID() == ItemChangedEvent.ACTION_LEEREN) {
      if (e.getSource() == panelQueryFLRMandant) {

        wtfMandant.setText(null);
        zutrittsobjektDto.setMandantCNr(null);
      }
    }
  }


  void dialogQueryZutrittscontrollerFromListe(ActionEvent e)
      throws Throwable {
    String[] aWhichButtonIUse = {
        PanelBasis.ACTION_REFRESH};
    panelQueryFLRZutrittscontroller = new PanelQueryFLR(
        null,
        null,
        QueryParameters.UC_ID_ZUTRITTSCONTROLLER,
        aWhichButtonIUse,
        getInternalFrame(),
        LPMain.getInstance().getTextRespectUISPr(
            "pers.zutritt.zutrittscontroller"));
    panelQueryFLRZutrittscontroller.befuellePanelFilterkriterienDirekt(
        SystemFilterFactory.
        getInstance().createFKDKennung(), null);
    panelQueryFLRZutrittscontroller.setSelectedId(zutrittsobjektDto.
                                                  getZutrittscontrollerIId());
    new DialogQuery(panelQueryFLRZutrittscontroller);

  }


  void dialogQueryMandantFromListe(ActionEvent e)
      throws Throwable {
    String[] aWhichButtonIUse = {
        PanelBasis.ACTION_REFRESH, PanelBasis.ACTION_LEEREN};

    panelQueryFLRMandant = new PanelQueryFLR(
        null,
        null,
        QueryParameters.UC_ID_MANDANT,
        aWhichButtonIUse,
        getInternalFrame(),
        LPMain.getInstance().getTextRespectUISPr("title.mandantauswahlliste"));

    panelQueryFLRMandant.setSelectedId(zutrittsobjektDto.getMandantCNr());

    new DialogQuery(panelQueryFLRMandant);

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

    wlaKennung.setText(LPMain.getInstance().getTextRespectUISPr("label.kennung"));
    wlaBezeichnung.setText(LPMain.getInstance().getTextRespectUISPr("lp.bezeichnung"));
    wlaOeffnung.setText(LPMain.getInstance().getTextRespectUISPr(
        "pers.zutritt.oeffnungszeit"));
    wlaRelais.setText(LPMain.getInstance().getTextRespectUISPr("pers.zutritt.relais"));
    wlaLeser.setText(LPMain.getInstance().getTextRespectUISPr("pers.zutritt.lesertyp"));
    wlaAdresse.setText(LPMain.getInstance().getTextRespectUISPr(
        "pers.zutritt.zutrittscontroller.adresse"));

    wtfKennung.setColumnsMax(ArtikelFac.MAX_HERSTELLER_NAME);
    wtfKennung.setText("");
    wtfKennung.setMandatoryField(true);
    wtfRelais.setMandatoryField(true);
    wnfOeffnung.setMandatoryField(true);
    wnfOeffnung.setFractionDigits(1);
    wbuZutrittscontroller.setText(LPMain.getInstance().getTextRespectUISPr(
        "pers.zutritt.zutrittscontroller") + "...");
    wbuZutrittscontroller.setActionCommand(PanelZutrittsobjekt.
                                           ACTION_SPECIAL_ZUTRITTSCONTROLLER_FROM_LISTE);
    wbuZutrittscontroller.addActionListener(this);

    wbuMandant.setText(LPMain.getInstance().getTextRespectUISPr("lp.mandant") + "...");
    wbuMandant.setActionCommand(PanelZutrittsobjekt.ACTION_SPECIAL_MANDANT_FROM_LISTE);
    wbuMandant.addActionListener(this);

    getInternalFrame().addItemChangedListener(this);
    wtfKennung.setColumnsMax(6);
    wnfOeffnung.setMaximumValue(99);

    wtfZutrittscontroller.setText("");
    wtfAdresse.setColumnsMax(ZutrittscontrollerFac.MAX_ZUTRITTSOBJEKT_C_ADRESSE);
    wtfRelais.setColumnsMax(1);

    wtfZutrittscontroller.setColumnsMax(Facade.MAX_UNBESCHRAENKT);
    wtfZutrittscontroller.setMandatoryField(true);
    wtfAdresse.setMandatoryField(true);
    wcoLeser.setMandatoryField(true);

    wtfZutrittscontroller.setActivatable(false);
    wtfMandant.setActivatable(false);
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
    jpaWorkingOn.add(wlaKennung,
                     new GridBagConstraints(0, 0, 1, 1, 0.2, 0.0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.HORIZONTAL,
                                            new Insets(2, 2, 2, 2), 0, 0));
    jpaWorkingOn.add(wtfKennung,
                     new GridBagConstraints(1, 0, 4, 1, 0.5, 0.0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.HORIZONTAL,
                                            new Insets(2, 2, 2, 2), 0, 0));
    jpaWorkingOn.add(wlaBezeichnung,
                     new GridBagConstraints(0, 1, 1, 1, 0, 0.0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.HORIZONTAL,
                                            new Insets(2, 2, 2, 2), 0, 0));
    jpaWorkingOn.add(wtfBezeichnung,
                     new GridBagConstraints(1, 1, 4, 1, 0, 0.0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.HORIZONTAL,
                                            new Insets(2, 2, 2, 2), 0, 0));
    jpaWorkingOn.add(wlaAdresse,
                     new GridBagConstraints(0, 2, 1, 1, 0, 0.0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.HORIZONTAL,
                                            new Insets(2, 2, 2, 2), 0, 0));
    jpaWorkingOn.add(wtfAdresse,
                     new GridBagConstraints(1, 2, 4, 1, 0, 0.0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.HORIZONTAL,
                                            new Insets(2, 2, 2, 2), 0, 0));
    jpaWorkingOn.add(wbuZutrittscontroller,
                     new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.HORIZONTAL,
                                            new Insets(2, 2, 2, 2), 0, 0));
    jpaWorkingOn.add(wtfZutrittscontroller,
                     new GridBagConstraints(1, 4, 4, 1, 0.0, 0.0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.HORIZONTAL,
                                            new Insets(2, 2, 2, 2), 0, 0));
    jpaWorkingOn.add(wlaRelais,
                     new GridBagConstraints(0, 5, 1, 1, 0, 0.0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.HORIZONTAL,
                                            new Insets(2, 2, 2, 2), 0, 0));
    jpaWorkingOn.add(wtfRelais,
                     new GridBagConstraints(1, 5, 1, 1, 0, 0.1
                                            , GridBagConstraints.WEST,
                                            GridBagConstraints.HORIZONTAL,
                                            new Insets(2, 2, 2, 2), 50, 0));

    jpaWorkingOn.add(wlaOeffnung,
                     new GridBagConstraints(2, 5, 1, 1, 0, 0.0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.HORIZONTAL,
                                            new Insets(2, 2, 2, 2), 0, 0));
    jpaWorkingOn.add(wnfOeffnung,
                     new GridBagConstraints(3, 5, 1, 1, 0, 0.0
                                            , GridBagConstraints.WEST,
                                            GridBagConstraints.NONE,
                                            new Insets(2, 2, 2, 2), -50, 0));
    WrapperLabel wlaSekunden = new WrapperLabel(SystemFac.EINHEIT_SEKUNDE.trim());
    wlaSekunden.setHorizontalAlignment(SwingConstants.LEFT);

    jpaWorkingOn.add(wlaSekunden,
                     new GridBagConstraints(4, 5, 1, 1, 0, 0.0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.HORIZONTAL,
                                            new Insets(2, 2, 2, 2), 15, 0));

    jpaWorkingOn.add(wlaLeser,
                     new GridBagConstraints(0, 6, 1, 1, 0, 0.0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.HORIZONTAL,
                                            new Insets(2, 2, 2, 2), 0, 0));
    jpaWorkingOn.add(wcoLeser,
                     new GridBagConstraints(1, 6, 2, 1, 0, 0.0
                                            , GridBagConstraints.WEST,
                                            GridBagConstraints.NONE,
                                            new Insets(2, 2, 2, 2), 150, 0));

    jpaWorkingOn.add(wbuMandant,
                     new GridBagConstraints(0, 8, 1, 1, 0.0, 0.0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.HORIZONTAL,
                                            new Insets(2, 2, 2, 2), 0, 0));
    jpaWorkingOn.add(wtfMandant,
                     new GridBagConstraints(1, 8, 4, 1, 0.0, 0.0
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


  protected String getLockMeWer()
      throws Exception {
    return HelperClient.LOCKME_ZUTRITTSOBJEKT;
  }

}
