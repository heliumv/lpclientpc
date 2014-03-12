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
package com.lp.client.artikel;


import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.EventObject;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.border.Border;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.WrapperDateField;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.artikel.service.MaterialzuschlagDto;

@SuppressWarnings("static-access")
public class PanelMaterialzuschlag
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
  private InternalFrameArtikel internalFrameArtikel = null;
  private WrapperLabel wlaLager = new WrapperLabel();
  private MaterialzuschlagDto materialzuschlagDto = null;
  private WrapperLabel wlaLagerart = new WrapperLabel();
  private WrapperLabel wlaWaehrung = new WrapperLabel();
  private WrapperNumberField wnfZuschlag = new WrapperNumberField();
  private WrapperDateField wdfGueltigab = new WrapperDateField();

  public PanelMaterialzuschlag(InternalFrame internalFrame, String add2TitleI,
                               Object pk)
      throws Throwable {
    super(internalFrame, add2TitleI, pk);
    internalFrameArtikel = (InternalFrameArtikel) internalFrame;
    jbInit();
    setDefaults();
    initComponents();
    enableAllComponents(this, false);
  }


  protected JComponent getFirstFocusableComponent()
      throws Exception {
    return wnfZuschlag;
  }


  protected void setDefaults() {

  }


  public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
      throws Throwable {

    super.eventYouAreSelected(false);
    Object key = getKeyWhenDetailPanel();

    if (key == null
        || (key.equals(LPMain.getLockMeForNew()))) {

      leereAlleFelder(this);
      clearStatusbar();
    }
    else {
      materialzuschlagDto = DelegateFactory.getInstance().getMaterialDelegate().
          materialzuschlagFindByPrimaryKey( (Integer) key);

      dto2Components();

    }
  }


  public void eventActionNew(EventObject eventObject, boolean bLockMeI,
                             boolean bNeedNoNewI)
      throws Throwable {
    super.eventActionNew(eventObject, true, false);
    leereAlleFelder(this);
    materialzuschlagDto = new MaterialzuschlagDto();
    wdfGueltigab.setDate(new java.sql.Date(System.currentTimeMillis()));
  }


  protected void dto2Components()
      throws ExceptionLP {
    wnfZuschlag.setBigDecimal(materialzuschlagDto.getNZuschlag());
    wdfGueltigab.setTimestamp(materialzuschlagDto.getTGueltigab());
  }


  protected void eventItemchanged(EventObject eI)
      throws Throwable {
    ItemChangedEvent e = (ItemChangedEvent) eI;
  }


  public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
      throws Throwable {
    if (allMandatoryFieldsSetDlg()) {
      components2Dto();
      if (materialzuschlagDto.getIId() == null) {
        materialzuschlagDto.setMaterialIId(internalFrameArtikel.materialDto.
                                           getIId());
        materialzuschlagDto.setIId(DelegateFactory.getInstance().getMaterialDelegate().
                                   createMaterialzuschlag(
                                       materialzuschlagDto));
        setKeyWhenDetailPanel(materialzuschlagDto.getIId());
      }
      else {
        DelegateFactory.getInstance().getMaterialDelegate().updateMaterialzuschlag(
            materialzuschlagDto);
      }
      super.eventActionSave(e, true);

      if (getInternalFrame().getKeyWasForLockMe() == null) {
        getInternalFrame().setKeyWasForLockMe(internalFrameArtikel.
                                              getArtikelDto().getIId().
                                              toString());
      }
      eventYouAreSelected(false);
    }
  }


  protected void components2Dto()
      throws ExceptionLP {
    materialzuschlagDto.setNZuschlag(wnfZuschlag.getBigDecimal());
    materialzuschlagDto.setTGueltigab(wdfGueltigab.getTimestamp());
  }


  protected void eventActionDelete(ActionEvent e, boolean bAdministrateLockKeyI,
                                   boolean bNeedNoDeleteI)
      throws Throwable {
    MaterialzuschlagDto dto = new MaterialzuschlagDto();
    dto.setIId( (Integer) getKeyWhenDetailPanel());
    DelegateFactory.getInstance().getMaterialDelegate().removeMaterialzuschlag(
        dto);
    this.setKeyWhenDetailPanel(null);
    super.eventActionDelete(e, false, false);
    materialzuschlagDto = new MaterialzuschlagDto();
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

    wlaLager.setText(LPMain.getInstance().getTextRespectUISPr("lp.gueltigab"));
    getInternalFrame().addItemChangedListener(this);

    wlaLagerart.setToolTipText("");
    wlaLagerart.setText(LPMain.getInstance().getTextRespectUISPr(
        "artikel.technik.zuschlag"));

    wlaWaehrung.setText(DelegateFactory.getInstance().getMandantDelegate().
                        mandantFindByPrimaryKey(LPMain.getInstance().
                                                getTheClient().getMandant()).
                        getWaehrungCNr());
    ;
    wdfGueltigab.setMandatoryField(true);
    wnfZuschlag.setMandatoryField(true);
    wnfZuschlag.setFractionDigits(6);
    this.add(jpaButtonAction, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
        , GridBagConstraints.WEST, GridBagConstraints.NONE,
        new Insets(0, 0, 0, 0), 0, 0));

    //jetzt meine felder
    jpaWorkingOn = new JPanel();
    gridBagLayoutWorkingPanel = new GridBagLayout();
    jpaWorkingOn.setLayout(gridBagLayoutWorkingPanel);
    this.add(jpaWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0
        , GridBagConstraints.SOUTHEAST, GridBagConstraints.BOTH,
        new Insets(0, 0, 0, 0), 0, 0));
    this.add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.BOTH,
        new Insets(0, 0, 0, 0), 0, 0));
    jpaWorkingOn.add(wlaLager,
                        new GridBagConstraints(0, 0, 1, 1, 0.05, 0.0
                                               , GridBagConstraints.CENTER,
                                               GridBagConstraints.HORIZONTAL,
                                               new Insets(2, 2, 2, 2), 0, 0));
    jpaWorkingOn.add(wlaLagerart,
                        new GridBagConstraints(3, 0, 1, 1, 0.05, 0.0
                                               , GridBagConstraints.CENTER,
                                               GridBagConstraints.HORIZONTAL,
                                               new Insets(2, 2, 2, 2), 0, 0));
    jpaWorkingOn.add(wnfZuschlag,
                        new GridBagConstraints(4, 0, 1, 1, 0.0, 0.0
                                               , GridBagConstraints.CENTER,
                                               GridBagConstraints.NONE,
                                               new Insets(0, 0, 0, 0), 0, 0));
    jpaWorkingOn.add(wdfGueltigab,
                        new GridBagConstraints(1, 0, 1, 1, 0.1, 0.0
                                               , GridBagConstraints.CENTER,
                                               GridBagConstraints.HORIZONTAL,
                                               new Insets(0, 0, 0, 0), 0, 0));
    jpaWorkingOn.add(wlaWaehrung,
                        new GridBagConstraints(5, 0, 1, 1, 0, 0.0
                                               , GridBagConstraints.CENTER,
                                               GridBagConstraints.HORIZONTAL,
                                               new Insets(0, 0, 0, 0), 20, 0));
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
    return HelperClient.LOCKME_MATERIAL;
  }

}
