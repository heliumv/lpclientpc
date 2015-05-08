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
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.personal.service.ZutrittsobjektDto;
import com.lp.server.personal.service.ZutrittsobjektverwendungDto;
import com.lp.server.system.service.MandantDto;
import com.lp.server.util.Facade;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;

@SuppressWarnings("static-access")
public class PanelZutrittsobjektverwendung
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
  public ZutrittsobjektDto zutrittsobjektDto = null;
  private GridBagLayout gridBagLayoutWorkingPanel = null;
  private WrapperLabel wlaZutrittsobjekt = new WrapperLabel();
  private WrapperTextField wtfZutrittsobjekt = new WrapperTextField();
  private WrapperLabel wlaAnzahl = new WrapperLabel();
  private WrapperNumberField wnfAnzahl = new WrapperNumberField();

  private WrapperButton wbuMandant = new WrapperButton();
  private WrapperTextField wtfMandant = new WrapperTextField();

  private PanelQueryFLR panelQueryFLRMandant = null;

  static final public String ACTION_SPECIAL_ZUTRITTSOBJEKT_FROM_LISTE =
      "ACTION_SPECIAL_ZUTRITTSOBJEKT_FROM_LISTE";
  static final public String ACTION_SPECIAL_MANDANT_FROM_LISTE =
      "ACTION_SPECIAL_MANDANT_FROM_LISTE";
  private ZutrittsobjektverwendungDto zutrittsobjektverwendungDto = null;

  public PanelZutrittsobjektverwendung(InternalFrame internalFrame, String add2TitleI,
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
  }


  protected JComponent getFirstFocusableComponent()
      throws Exception {
    return wnfAnzahl;
  }


  protected void components2Dto()
      throws Throwable {
    zutrittsobjektverwendungDto.setIAnzahlverwendung(wnfAnzahl.getInteger());
    zutrittsobjektverwendungDto.setZutrittsobjektIId(zutrittsobjektDto.getIId());
  }


  protected void dto2Components()
      throws Throwable {
    wnfAnzahl.setInteger(zutrittsobjektverwendungDto.getIAnzahlverwendung());

    ZutrittsobjektDto dto = DelegateFactory.getInstance().getZutrittDelegate().
        zutrittsobjektFindByPrimaryKey(zutrittsobjektverwendungDto.getZutrittsobjektIId());
    wtfZutrittsobjekt.setText(dto.getBezeichnung());

    MandantDto mandantDto = DelegateFactory.getInstance().
             getMandantDelegate().
             mandantFindByPrimaryKey(zutrittsobjektverwendungDto.getMandantCNr());
        wtfMandant.setText(mandantDto.getCNr());

  }


  public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
      throws
      Throwable {
    if (allMandatoryFieldsSetDlg()) {
      components2Dto();
      if (zutrittsobjektverwendungDto.getIId() == null) {
        zutrittsobjektverwendungDto.setIId(DelegateFactory.getInstance().
                                           getZutrittDelegate().
                                           createZutrittsobjektverwendung(
                                               zutrittsobjektverwendungDto));
        setKeyWhenDetailPanel(zutrittsobjektverwendungDto.getIId());
      }
      else {
        DelegateFactory.getInstance().getZutrittDelegate().updateZutrittsobjektverwendung(
            zutrittsobjektverwendungDto);
      }

      super.eventActionSave(e, true);

      if (getInternalFrame().getKeyWasForLockMe() == null) {
        getInternalFrame().setKeyWasForLockMe(zutrittsobjektverwendungDto.getIId() + "");
      }
      eventYouAreSelected(false);
    }
  }


  public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
      throws Throwable {

    super.eventYouAreSelected(false);
    if (zutrittsobjektDto != null) {
      leereAlleFelder(this);
      wtfZutrittsobjekt.setText(zutrittsobjektDto.getBezeichnung());

      Object key = getKeyWhenDetailPanel();
      if (key == null
          || (key.equals(LPMain.getLockMeForNew()))) {
      }
      else {
        zutrittsobjektverwendungDto = DelegateFactory.getInstance().getZutrittDelegate().
            zutrittsobjektverwendungFindByPrimaryKey( (Integer) key);
        dto2Components();
      }
    }
    else {
      leereAlleFelder(this);
    }

  }


  protected void eventActionDelete(ActionEvent e, boolean bAdministrateLockKeyI,
                                   boolean bNeedNoDeleteI)
      throws
      Throwable {
    DelegateFactory.getInstance().getZutrittDelegate().removeZutrittsobjektverwendung(
        zutrittsobjektverwendungDto);
    this.setKeyWhenDetailPanel(null);
    super.eventActionDelete(e, false, false);
  }


  public void eventActionNew(EventObject eventObject, boolean bLockMeI,
                             boolean bNeedNoNewI)
      throws Throwable {
    if (zutrittsobjektDto != null) {
      super.eventActionNew(eventObject, true, false);
      zutrittsobjektverwendungDto = new ZutrittsobjektverwendungDto();
      leereAlleFelder(this);
    }
    else {
      DialogFactory.showModalDialog(LPMain.getInstance().getTextRespectUISPr(
          "lp.error"),
                                           LPMain.getInstance().getTextRespectUISPr(
                                               "lp.error.keinartikellieferantdefiniert"));
    }

  }


  protected void eventActionSpecial(ActionEvent e)
      throws Throwable {
    if (e.getActionCommand().equals(ACTION_SPECIAL_MANDANT_FROM_LISTE)) {
      dialogQueryMandantFromListe(e);
    }
  }


  protected void eventItemchanged(EventObject eI)
      throws Throwable {
    ItemChangedEvent e = (ItemChangedEvent) eI;
    if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
      if (e.getSource() == panelQueryFLRMandant) {
        Object key = ( (ISourceEvent) e.getSource()).getIdSelected();
        MandantDto mandantDto = DelegateFactory.getInstance().
            getMandantDelegate().
            mandantFindByPrimaryKey( (String)
                                    key);
        wtfMandant.setText(mandantDto.getCNr());
        zutrittsobjektverwendungDto.setMandantCNr(mandantDto.getCNr());
      }
    }
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

    panelQueryFLRMandant.setSelectedId(zutrittsobjektverwendungDto.getMandantCNr());

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

    wlaAnzahl.setText(LPMain.getInstance().getTextRespectUISPr(
        "pers.zutritt.zutrittsobjektverwendung.anzahl"));
    wnfAnzahl.setMandatoryField(true);
    wtfMandant.setMandatoryField(true);
    wtfZutrittsobjekt.setMandatoryField(true);
    wtfZutrittsobjekt.setColumnsMax(Facade.MAX_UNBESCHRAENKT);
    wnfAnzahl.setFractionDigits(0);
    wnfAnzahl.setMinimumValue(0);
    wlaZutrittsobjekt.setText(LPMain.getInstance().getTextRespectUISPr(
        "pers.zutritt.zutrittsobjekt"));
    wbuMandant.setText(LPMain.getInstance().getTextRespectUISPr("lp.mandant") + "...");
    wbuMandant.setActionCommand(PanelZutrittsobjektverwendung.
                                ACTION_SPECIAL_MANDANT_FROM_LISTE);
    wbuMandant.addActionListener(this);

    getInternalFrame().addItemChangedListener(this);
    wtfMandant.setActivatable(false);
    wtfZutrittsobjekt.setActivatable(false);
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

    jpaWorkingOn.add(wlaZutrittsobjekt,
                     new GridBagConstraints(0, 0, 1, 1, 0.1, 0.0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.HORIZONTAL,
                                            new Insets(2, 2, 2, 2), 0, 0));
    jpaWorkingOn.add(wtfZutrittsobjekt,
                     new GridBagConstraints(1, 0, 4, 1, 0.3, 0.0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.HORIZONTAL,
                                            new Insets(2, 2, 2, 2), 0, 0));

    jpaWorkingOn.add(wbuMandant,
                     new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.HORIZONTAL,
                                            new Insets(2, 2, 2, 2), 0, 0));
    jpaWorkingOn.add(wtfMandant,
                     new GridBagConstraints(1, 1, 4, 1, 0.0, 0.0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.HORIZONTAL,
                                            new Insets(2, 2, 2, 2), 0, 0));

    jpaWorkingOn.add(wlaAnzahl,
                     new GridBagConstraints(0, 2, 1, 1, 0, 0.0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.HORIZONTAL,
                                            new Insets(2, 2, 2, 2), 0, 0));
    jpaWorkingOn.add(wnfAnzahl,
                     new GridBagConstraints(1, 2, 1, 1, 0, 0
                                            , GridBagConstraints.WEST,
                                            GridBagConstraints.HORIZONTAL,
                                            new Insets(2, 2, 2, 2), 50, 0));
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
    return HelperClient.LOCKME_ZUTRITTSOBJEKTVERWENDUNG;
  }

}
