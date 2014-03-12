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

import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.artikel.service.ArtikelFac;
import com.lp.server.artikel.service.ArtklaDto;
import com.lp.server.artikel.service.ArtklasprDto;

@SuppressWarnings("static-access") 
public class PanelArtikelklassen
    extends PanelBasis
{

  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
//von hier ...
  private GridBagLayout gridBagLayoutAll = null;
  private JPanel jpaPanelWorkingOn = new JPanel();
  private JPanel jpaButtonAction = null;
  private Border border = null;
  private GridBagLayout gridBagLayoutWorkingPanel = null;
  private WrapperLabel wlaLager = new WrapperLabel();
  private WrapperTextField wtfKennung = new WrapperTextField();
  private ArtklaDto artklaDto = null;
  private WrapperLabel wlaBezeichnung = new WrapperLabel();
  private WrapperTextField wtfBezeichnung = new WrapperTextField();
  private WrapperButton wbuVatergruppe = new WrapperButton();
  private WrapperTextField wtfVaterklasse = new WrapperTextField();
  private PanelQueryFLR panelQueryFLRArtikelklasse = null;

  private WrapperCheckBox wcbTops = new WrapperCheckBox();


  static final public String ACTION_SPECIAL_ARTIKELKLASSE_FROM_LISTE =
      "action_artikelklasse_from_liste";

  public PanelArtikelklassen(InternalFrame internalFrame, String add2TitleI,
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
    return wtfKennung;
  }


  protected void setDefaults() {
  }


  public void eventActionNew(EventObject eventObject, boolean bLockMeI,
                             boolean bNeedNoNewI)
      throws Throwable {
    super.eventActionNew(eventObject, true, false);
    artklaDto = new ArtklaDto();

    leereAlleFelder(this);
  }


  protected void eventActionSpecial(ActionEvent e)
      throws Throwable {
    if (e.getActionCommand().equals(ACTION_SPECIAL_ARTIKELKLASSE_FROM_LISTE)) {
      dialogQueryArtikelklasseFromListe(e);
    }
  }


  protected void eventActionDelete(ActionEvent e, boolean bAdministrateLockKeyI,
                                   boolean bNeedNoDeleteI)
      throws
      Throwable {
    DelegateFactory.getInstance().getArtikelDelegate().removeArtkla(artklaDto.getIId());
    this.setKeyWhenDetailPanel(null);
    super.eventActionDelete(e, false, false);
  }


  protected void components2Dto() {
    artklaDto.setCNr(wtfKennung.getText());
    artklaDto.setBTops(wcbTops.getShort());
    if (artklaDto.getArtklasprDto() == null) {
      artklaDto.setArtklasprDto(new ArtklasprDto());
    }
    artklaDto.getArtklasprDto().setCBez(wtfBezeichnung.
                                        getText());
  }


  protected void dto2Components()
      throws Throwable {
    wtfKennung.setText(artklaDto.getCNr());
     wcbTops.setShort(artklaDto.getBTops());
    if (artklaDto.getArtklasprDto() != null) {
      wtfBezeichnung.setText(artklaDto.getArtklasprDto().getCBez());
    }
    else {
      wtfBezeichnung.setText("");
    }
    if (artklaDto.getArtklaIId() != null) {
      ArtklaDto temp = DelegateFactory.getInstance().getArtikelDelegate().
          artklaFindByPrimaryKey(artklaDto.getArtklaIId());
      wtfVaterklasse.setText(temp.getCNr());
    }
    else {
      wtfVaterklasse.setText(null);
    }
  }


  public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
      throws
      Throwable {
    if (allMandatoryFieldsSetDlg()) {
      components2Dto();
      if (artklaDto.getIId() == null) {
        artklaDto.setIId(DelegateFactory.getInstance().getArtikelDelegate().createArtkla(artklaDto));
        setKeyWhenDetailPanel(artklaDto.getIId());
      }
      else {
        DelegateFactory.getInstance().getArtikelDelegate().updateArtkla(artklaDto);
      }
      super.eventActionSave(e, true);

      if (getInternalFrame().getKeyWasForLockMe() == null) {
        getInternalFrame().setKeyWasForLockMe(artklaDto.getIId() + "");
      }
      eventYouAreSelected(false);
    }
  }


  protected void eventItemchanged(EventObject eI)
      throws Throwable {
    ItemChangedEvent e = (ItemChangedEvent) eI;
    if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
      if (e.getSource() == panelQueryFLRArtikelklasse) {
        Object key = ( (ISourceEvent) e.getSource()).getIdSelected();
        ArtklaDto temp = DelegateFactory.getInstance().getArtikelDelegate().
            artklaFindByPrimaryKey( (Integer) key);
        wtfVaterklasse.setText(temp.getCNr());
        artklaDto.setArtklaIId(temp.getIId());
      }
    }
    else if (e.getID() == ItemChangedEvent.ACTION_LEEREN) {
      if (e.getSource() == panelQueryFLRArtikelklasse) {

        wtfVaterklasse.setText(null);
        artklaDto.setArtklaIId(null);
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

    wlaLager.setText(LPMain.getInstance().getTextRespectUISPr("label.kennung"));
    wtfKennung.setColumnsMax(ArtikelFac.MAX_ARTIKELKLASSE_NAME);
    wtfKennung.setText("");
    wtfKennung.setMandatoryField(true);

    wlaBezeichnung.setText(LPMain.getInstance().getTextRespectUISPr(
        "lp.bezeichnung"));

    wcbTops.setText(LPMain.getInstance().getTextRespectUISPr(
        "fert.tops"));
    wtfBezeichnung.setToolTipText("");
    wtfBezeichnung.setColumnsMax(ArtikelFac.MAX_ARTIKELKLASSE_BEZEICHNUNG);
    wtfBezeichnung.setText("");
    wbuVatergruppe.setText(LPMain.getInstance().getTextRespectUISPr(
        "artikel.vaterklasse") + "...");
    wbuVatergruppe.setActionCommand(PanelArtikelklassen.
                                    ACTION_SPECIAL_ARTIKELKLASSE_FROM_LISTE);
    wbuVatergruppe.addActionListener(this);
    getInternalFrame().addItemChangedListener(this);
    wtfVaterklasse.setText("");
    wtfVaterklasse.setActivatable(false);
    this.add(jpaButtonAction, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
        , GridBagConstraints.WEST, GridBagConstraints.NONE,
        new Insets(0, 0, 0, 0), 0, 0));

    //jetzt meine felder
    jpaPanelWorkingOn = new JPanel();
    gridBagLayoutWorkingPanel = new GridBagLayout();
    jpaPanelWorkingOn.setLayout(gridBagLayoutWorkingPanel);
    this.add(jpaPanelWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0
        , GridBagConstraints.SOUTHEAST, GridBagConstraints.BOTH,
        new Insets(0, 0, 0, 0), 0, 0));
    this.add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.BOTH,
        new Insets(0, 0, 0, 0), 0, 0));
    jpaPanelWorkingOn.add(wlaLager,
                        new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
                                               , GridBagConstraints.CENTER,
                                               GridBagConstraints.HORIZONTAL,
                                               new Insets(2, 2, 2, 2), 0, 0));
    jpaPanelWorkingOn.add(wtfKennung,
                        new GridBagConstraints(1, 0, 1, 1, 0.1, 0.0
                                               , GridBagConstraints.CENTER,
                                               GridBagConstraints.HORIZONTAL,
                                               new Insets(2, 2, 2, 2), 0, 0));
    jpaPanelWorkingOn.add(wlaBezeichnung,
                        new GridBagConstraints(2, 0, 1, 1, 0.1, 0.0
                                               , GridBagConstraints.CENTER,
                                               GridBagConstraints.HORIZONTAL,
                                               new Insets(2, 2, 2, 2), 0, 0));
    jpaPanelWorkingOn.add(wtfBezeichnung,
                        new GridBagConstraints(3, 0, 1, 1, 0.2, 0.0
                                               , GridBagConstraints.CENTER,
                                               GridBagConstraints.HORIZONTAL,
                                               new Insets(2, 2, 2, 2), 0, 0));
    jpaPanelWorkingOn.add(wbuVatergruppe,
                        new GridBagConstraints(0, 1, 1, 1, 0.15, 0.0
                                               , GridBagConstraints.CENTER,
                                               GridBagConstraints.HORIZONTAL,
                                               new Insets(0, 0, 0, 0), 0, 0));
    jpaPanelWorkingOn.add(wtfVaterklasse,
                        new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
                                               , GridBagConstraints.CENTER,
                                               GridBagConstraints.HORIZONTAL,
                                               new Insets(2, 2, 2, 2), 0, 0));
    jpaPanelWorkingOn.add(wcbTops,
                       new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0
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


  void dialogQueryArtikelklasseFromListe(ActionEvent e)
      throws Throwable {

    panelQueryFLRArtikelklasse = ArtikelFilterFactory.getInstance().
        createPanelFLRArtikelklasse(getInternalFrame(), artklaDto.getArtklaIId());

    new DialogQuery(panelQueryFLRArtikelklasse);
  }


  protected String getLockMeWer()
      throws Exception {
    return HelperClient.LOCKME_ARTIKELKLASSE;
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
      artklaDto = DelegateFactory.getInstance().getArtikelDelegate().
          artklaFindByPrimaryKey( (Integer) key);

      dto2Components();

    }
  }
}
