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
import java.util.EventObject;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import com.lp.client.artikel.ArtikelFilterFactory;
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
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.personal.service.PersonalverfuegbarkeitDto;
import com.lp.server.util.Facade;
import com.lp.server.util.fastlanereader.service.query.FilterKriteriumDirekt;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;

@SuppressWarnings("static-access")
public class PanelPersonalverfuegbarkeit
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
  private WrapperLabel wlaAnteil = new WrapperLabel();
  private WrapperNumberField wnfAnteil = new WrapperNumberField();
  private PersonalverfuegbarkeitDto personalverfuegbarkeitDto = null;
  private WrapperButton wbuArtikel = new WrapperButton();
  private WrapperTextField wtfArtikel = new WrapperTextField();
  private WrapperLabel wlaEinheitProzent = new WrapperLabel();
  private WrapperLabel wlaGesamt = new WrapperLabel();

  private PanelQueryFLR panelQueryFLRArtikel = null;

  static final public String ACTION_SPECIAL_ARTIKEL_FROM_LISTE =
      "ACTION_SPECIAL_ARTIKEL_FROM_LISTE";

  public PanelPersonalverfuegbarkeit(InternalFrame internalFrame, String add2TitleI,
                                 Object pk)
      throws Throwable {
    super(internalFrame, add2TitleI, pk);
    internalFramePersonal = (InternalFramePersonal) internalFrame;
    jbInit();
    setDefaults();
    initComponents();
    enableAllComponents(this, false);
  }


  protected void setDefaults() {
  }

  protected JComponent getFirstFocusableComponent()
      throws Exception {
    return wbuArtikel;
  }


  void dialogQueryArtikelFromListe(ActionEvent e)
      throws Throwable {
    String[] aWhichButtonIUse = {
        PanelBasis.ACTION_REFRESH};

    panelQueryFLRArtikel = new PanelQueryFLR(
        null,
        ArtikelFilterFactory.getInstance().createFKArtikellisteNurArbeitszeit(),
        QueryParameters.UC_ID_ARTIKELLISTE,
        aWhichButtonIUse,
        internalFramePersonal,
        LPMain.getInstance().getTextRespectUISPr("title.artikelauswahlliste"),ArtikelFilterFactory.getInstance().createFKVArtikel(), null);

    FilterKriteriumDirekt fkDirekt1 = ArtikelFilterFactory.getInstance().
        createFKDArtikelnummer(getInternalFrame());
    FilterKriteriumDirekt fkDirekt2 = ArtikelFilterFactory.getInstance().
        createFKDVolltextsuche();
    panelQueryFLRArtikel.befuellePanelFilterkriterienDirekt(fkDirekt1, fkDirekt2);

    panelQueryFLRArtikel.setSelectedId(personalverfuegbarkeitDto.getArtikelIId());

    new DialogQuery(panelQueryFLRArtikel);
  }


  public void eventActionNew(EventObject eventObject, boolean bLockMeI,
                             boolean bNeedNoNewI)
      throws Throwable {
    super.eventActionNew(eventObject, true, false);
    personalverfuegbarkeitDto = new PersonalverfuegbarkeitDto();
    // getInternalFrame().setKeyWasForLockMe(LPMain.getLockMeForNew());

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
    if (e.getActionCommand().equals(ACTION_SPECIAL_ARTIKEL_FROM_LISTE)) {
      dialogQueryArtikelFromListe(e);
    }
  }


  protected void eventActionDelete(ActionEvent e, boolean bAdministrateLockKeyI,
                                   boolean bNeedNoDeleteI)
      throws Throwable {
    DelegateFactory.getInstance().getPersonalDelegate().removePersonalverfuegbarkeit(
        personalverfuegbarkeitDto);
    this.setKeyWhenDetailPanel(null);
    super.eventActionDelete(e, false, false);
  }


  protected void components2Dto() throws Throwable{
    personalverfuegbarkeitDto.setFAnteilprozent(wnfAnteil.getDouble());
     personalverfuegbarkeitDto.setPersonalIId(internalFramePersonal.getPersonalDto().getIId());
  }


  protected void dto2Components() throws Throwable {
    wnfAnteil.setDouble(personalverfuegbarkeitDto.getFAnteilprozent());
    ArtikelDto artikelDto = DelegateFactory.getInstance().getArtikelDelegate().
          artikelFindByPrimaryKey( personalverfuegbarkeitDto.getArtikelIId());
      wtfArtikel.setText(artikelDto.formatArtikelbezeichnung());

      Double d = DelegateFactory.getInstance().getPersonalDelegate().
            getAuslastungEinerPerson(internalFramePersonal.getPersonalDto().getIId());
    wlaGesamt.setText(d+" "+LPMain.getInstance().getTextRespectUISPr("pers.verfuegbarkeit.zugewiesen"));
  }


  public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
      throws Throwable {
    if (allMandatoryFieldsSetDlg()) {
      components2Dto();

      Double d = DelegateFactory.getInstance().getPersonalDelegate().
          getAuslastungEinerPerson(internalFramePersonal.getPersonalDto().getIId());
      if (d + wnfAnteil.getDouble() > 100) {
        String msg = LPMain.getInstance().getTextRespectUISPr(
              "pers.error.verfuegbarkeit");
        DialogFactory.showModalDialog(LPMain.getInstance().getTextRespectUISPr(
              "lp.error"), msg);
      }
      else {

        if (personalverfuegbarkeitDto.getIId() == null) {
          personalverfuegbarkeitDto.setIId(DelegateFactory.getInstance().
                                           getPersonalDelegate().
                                           createPersonalverfuegbarkeit(
              personalverfuegbarkeitDto));
          setKeyWhenDetailPanel(personalverfuegbarkeitDto.getIId());
        }
        else {
          DelegateFactory.getInstance().getPersonalDelegate().
              updatePersonalverfuegbarkeit(
                  personalverfuegbarkeitDto);
        }
        super.eventActionSave(e, true);

        if (getInternalFrame().getKeyWasForLockMe() == null) {
          getInternalFrame().setKeyWasForLockMe(internalFramePersonal.getPersonalDto().
                                                getIId() + "");
        }
        eventYouAreSelected(false);
      }
    }
  }


  protected void eventItemchanged(EventObject eI)
      throws Throwable {
    ItemChangedEvent e = (ItemChangedEvent) eI;
    if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
      if (e.getSource() == panelQueryFLRArtikel) {
        Object key = ( (ISourceEvent) e.getSource()).getIdSelected();
        ArtikelDto artikelDto = DelegateFactory.getInstance().getArtikelDelegate().
            artikelFindByPrimaryKey( (Integer) key);
        wtfArtikel.setText(artikelDto.formatArtikelbezeichnung());
        personalverfuegbarkeitDto.setArtikelIId(artikelDto.getIId());
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

    wlaAnteil.setText(LPMain.getInstance().getTextRespectUISPr("pers.personalgehalt.verfuegbarkeit"));
    wnfAnteil.setMandatoryField(true);
    wnfAnteil.setMinimumValue(0);
    wnfAnteil.setMaximumValue(100);

    getInternalFrame().addItemChangedListener(this);
    wbuArtikel.setText(LPMain.getInstance().getTextRespectUISPr("button.artikel"));

    wlaEinheitProzent.setText("%");
    wlaEinheitProzent.setHorizontalAlignment(SwingConstants.LEFT);
    wbuArtikel.setActionCommand(PanelPersonalverfuegbarkeit.ACTION_SPECIAL_ARTIKEL_FROM_LISTE);
    wbuArtikel.addActionListener(this);

    wtfArtikel.setActivatable(false);
    wtfArtikel.setMandatoryField(true);
    wtfArtikel.setText("");
    wtfArtikel.setColumnsMax(Facade.MAX_UNBESCHRAENKT);

    Double d = DelegateFactory.getInstance().getPersonalDelegate().
          getAuslastungEinerPerson(internalFramePersonal.getPersonalDto().getIId());
    wlaGesamt.setText(d+" "+LPMain.getInstance().getTextRespectUISPr("pers.verfuegbarkeit.zugewiesen"));
    wlaGesamt.setHorizontalAlignment(SwingConstants.LEFT);
    jpaWorkingOn = new JPanel();
    gridBagLayoutWorkingPanel = new GridBagLayout();
    jpaWorkingOn.setLayout(gridBagLayoutWorkingPanel);
    this.add(jpaWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0
        , GridBagConstraints.SOUTHEAST, GridBagConstraints.BOTH,
        new Insets(0, 0, 0, 0), 0, 0));
    jpaWorkingOn.add(wbuArtikel, new GridBagConstraints(0, 0, 1, 1, 0.2, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
        0, 0));
    jpaWorkingOn.add(wtfArtikel, new GridBagConstraints(1, 0, 2, 1, 0.1, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
        0, 0));

    jpaWorkingOn.add(wlaAnteil, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
        0, 0));
    jpaWorkingOn.add(wnfAnteil, new GridBagConstraints(1, 2, 1, 1, 0.1, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
        0, 0));
    jpaWorkingOn.add(wlaEinheitProzent, new GridBagConstraints(2, 2, 1, 1, 0.7, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
        0, 0));
    jpaWorkingOn.add(wlaGesamt, new GridBagConstraints(1, 3, 2, 1, 0.0, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
        0, 0));
    this.add(jpaButtonAction, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
        , GridBagConstraints.WEST, GridBagConstraints.NONE,
        new Insets(0, 0, 0, 0), 0, 0));

    //jetzt meine felder
    jpaWorkingOn = new JPanel();
    gridBagLayoutWorkingPanel = new GridBagLayout();
    jpaWorkingOn.setLayout(gridBagLayoutWorkingPanel);
    this.add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.BOTH,
        new Insets(0, 0, 0, 0), 0, 0));
    this.add(jpaWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0
        , GridBagConstraints.SOUTHEAST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0),
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
    super.eventYouAreSelected(false);
    Object key = getKeyWhenDetailPanel();

    if (key == null
        || (key.equals(LPMain.getLockMeForNew()))) {

      leereAlleFelder(this);

      clearStatusbar();

      Double d = DelegateFactory.getInstance().getPersonalDelegate().
          getAuslastungEinerPerson(internalFramePersonal.getPersonalDto().getIId());
      wlaGesamt.setText(d + " " +
                      LPMain.getInstance().getTextRespectUISPr("pers.verfuegbarkeit.zugewiesen"));
      
      //Lt. WH mit 1 Prozent vorbesetzen
      
      wnfAnteil.setDouble(1D);




    }
    else {
      personalverfuegbarkeitDto = DelegateFactory.getInstance().getPersonalDelegate().
          personalverfuegbarkeitFindByPrimaryKey( (Integer) key);
      Double d = DelegateFactory.getInstance().getPersonalDelegate().
          getAuslastungEinerPerson(internalFramePersonal.getPersonalDto().getIId());
      wlaGesamt.setText(d + " " +
                      LPMain.getInstance().getTextRespectUISPr("pers.verfuegbarkeit.zugewiesen"));

      dto2Components();

    }
  }
}
