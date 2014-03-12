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
package com.lp.client.system;


import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.EventObject;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import com.lp.client.finanz.FinanzFilterFactory;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperEditorField;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberLetterField;
import com.lp.client.frame.component.WrapperSelectField;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPButtonAction;
import com.lp.client.pc.LPMain;
import com.lp.client.util.fastlanereader.gui.QueryType;
import com.lp.server.finanz.service.KontoDto;
import com.lp.server.system.service.KostenstelleDto;
import com.lp.server.system.service.SystemFac;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.FilterKriteriumDirekt;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.util.Helper;

@SuppressWarnings("static-access")
/**
 * <p> Diese Klasse kuemmert sich um das KostenstellenCRUD.</p>
 *
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 *
 * <p>Erstellung: Josef Ornetsmueller; 13.05.05</p>
 *
 * <p>@author $Author: christian $</p>
 *
 * @version $Revision: 1.4 $ Date $Date: 2009/11/13 10:12:50 $
 */
public class PanelKostenstelle
    extends PanelBasis
{
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private GridBagLayout gridBagLayoutAll = null;
  private JPanel jpaWorkingOn = null;
  private JPanel jpaButtonAction = null;
  private Border border = null;
  private GridBagLayout gridBagLayoutWorkingPanel = null;

  static final private String ACTION_SPECIAL_FLR_SACHKONTEN =
      "action_special_flr_sachkonten";

  private WrapperTextField wtfCNr = null;
  private WrapperLabel wlaCNr = null;
  private WrapperLabel wlaBezeichnung = null;
  private WrapperTextField wtfBezeichnung = null;
  private WrapperCheckBox wcbIstProfitcenter = null;
  private WrapperTextField wtfSachkonto = null;
  private WrapperButton wbuSachkonto = null;
  private WrapperLabel wlaBemerkung = null;
  private WrapperEditorField wefBemerkung = null;
  private WrapperLabel wlaSubdirectory = null;
  private WrapperNumberLetterField wnlfSubdirectory =null;
  private WrapperCheckBox wcbVersteckt = null;
  private WrapperSelectField wsfLagerOhneAbbuchung = null;


  private PanelQueryFLR panelQueryFLRSachkonto = null;

  public PanelKostenstelle(InternalFrame internalFrame,
                           String add2TitleI,
                           Object keyI)
      throws Throwable {

    super(internalFrame, add2TitleI, keyI);

    jbInit();
    initComponents();
    initPanel();
  }


  protected void eventItemchanged(EventObject eI)
      throws Throwable {

    ItemChangedEvent e = (ItemChangedEvent) eI;

    if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
      if (e.getSource() == panelQueryFLRSachkonto) {
        Integer iId = (Integer) ( (ISourceEvent) e.getSource()).
            getIdSelected();
        KontoDto k =
            DelegateFactory.getInstance().getFinanzDelegate().kontoFindByPrimaryKey(iId);

        wtfSachkonto.setText(k.getCNr());

        getKostenstelleDto().setKontoIId(iId);
      }
    }
    if (e.getID() == ItemChangedEvent.ACTION_LEEREN) {
      if (e.getSource() == panelQueryFLRSachkonto) {
        wtfSachkonto.setText(null);
        getKostenstelleDto().setKontoIId(null);
      }
    }
  }


  private void jbInit()
      throws Exception, Throwable {
    //von hier ...
    String[] aButton = {
        PanelBasis.ACTION_UPDATE,
        PanelBasis.ACTION_SAVE,
        PanelBasis.ACTION_DELETE,
        PanelBasis.ACTION_DISCARD
    };
    enableToolsPanelButtons(aButton);

    border = BorderFactory.createEmptyBorder(10, 10, 10, 10);
    setBorder(border);

    //das Aussenpanel hat immer das Gridbaglayout.
    gridBagLayoutAll = new GridBagLayout();
    setLayout(gridBagLayoutAll);

    //Actionpanel von Oberklasse holen und anhaengen.
    jpaButtonAction = getToolsPanel();

    add(jpaButtonAction,
        new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
                               , GridBagConstraints.NORTHWEST,
                               GridBagConstraints.HORIZONTAL,
                               new Insets(0, 0, 0, 0), 0, 0));

    // Statusbar an den unteren Rand des Panels haengen.
    add(getPanelStatusbar(),
        new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0
                               , GridBagConstraints.CENTER,
                               GridBagConstraints.BOTH,
                               new Insets(0, 0, 0, 0), 0, 0));
    //jetzt meine Felder
    jpaWorkingOn = new JPanel();
    gridBagLayoutWorkingPanel = new GridBagLayout();
    jpaWorkingOn.setLayout(gridBagLayoutWorkingPanel);
    add(jpaWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0
                                             , GridBagConstraints.SOUTHEAST,
                                             GridBagConstraints.BOTH,
                                             new Insets(0, 0, 0, 0), 0, 0));

    getInternalFrame().addItemChangedListener(this);

    wtfBezeichnung = new WrapperTextField();
    wtfBezeichnung = new WrapperTextField(SystemFac.MAX_KOSTENSTELLE_BEZEICHNUNG);

    wlaBezeichnung = new WrapperLabel(
        LPMain.getInstance().getTextRespectUISPr("lp.bezeichnung"));

    wlaCNr = new WrapperLabel(
        LPMain.getInstance().getTextRespectUISPr("label.kostenstelle"));
    wtfCNr = new WrapperTextField(SystemFac.MAX_KOSTENSTELLE_CNR);
    wtfCNr.setMandatoryFieldDB(true);

    wcbIstProfitcenter = new WrapperCheckBox();
    wcbIstProfitcenter.setText(LPMain.getInstance().getTextRespectUISPr("part.profitcenter"));


    wtfSachkonto = new WrapperTextField();
    wtfSachkonto.setActivatable(false);

    wbuSachkonto = new WrapperButton();
    wbuSachkonto.setText(LPMain.getInstance().getTextRespectUISPr("button.sachkonto"));
    wbuSachkonto.setToolTipText(LPMain.getInstance().getTextRespectUISPr("button.sachkonto.tooltip"));
    wbuSachkonto.setActionCommand(ACTION_SPECIAL_FLR_SACHKONTEN);
    wbuSachkonto.addActionListener(this);

    wlaBemerkung = new WrapperLabel(LPMain.getInstance().
                                    getTextRespectUISPr("lp.bemerkung"));
    wlaBemerkung.setVerticalAlignment(SwingConstants.NORTH);

    wefBemerkung = new WrapperEditorField(
        getInternalFrame(),
        LPMain.getInstance().getTextRespectUISPr("lp.bemerkung"));
    wlaSubdirectory = new WrapperLabel(LPMain.getInstance().getTextRespectUISPr("lp.subdirectory"));
    wnlfSubdirectory = new WrapperNumberLetterField();
    wnlfSubdirectory.setMaximumDigits(40);

    wcbVersteckt = new WrapperCheckBox();
    wcbVersteckt.setText(LPMain.getInstance().getTextRespectUISPr("lp.versteckt"));

    
    
    
    
    wsfLagerOhneAbbuchung=new WrapperSelectField(WrapperSelectField.LAGER,
			getInternalFrame(), true);
    
    wsfLagerOhneAbbuchung.setText(LPMain.getInstance().getTextRespectUISPr("kostenstelle.lagerohneabbuchung")+"...");
    
    // Zeile.
    iZeile++;
    jpaWorkingOn.add(wlaCNr,
                     new GridBagConstraints(0, iZeile, 1, 1, 0.2, 0.0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.BOTH,
                                            new Insets(2, 2, 2, 2), 0, 0));
    jpaWorkingOn.add(wtfCNr,
                     new GridBagConstraints(1, iZeile, 1, 1, 0.2, 0.0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.BOTH,
                                            new Insets(2, 2, 2, 2), 0, 0));
    jpaWorkingOn.add(new WrapperLabel(),
                     new GridBagConstraints(2, iZeile, 1, 1, 0.0, 0.0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.BOTH,
                                            new Insets(2, 2, 2, 2), 0, 0));
    jpaWorkingOn.add(wcbIstProfitcenter,
                     new GridBagConstraints(3, iZeile, 1, 1, 0.2, 0.0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.BOTH,
                                            new Insets(2, 2, 2, 2), 0, 0));
    if (DelegateFactory.getInstance().getTheJudgeDelegate().hatRecht(com.lp.server.
          benutzer.service.
          RechteFac.
        RECHT_LP_DARF_VERSTECKTE_SEHEN)) {
      jpaWorkingOn.add(wcbVersteckt,
                       new GridBagConstraints(4, iZeile, 1, 1, 0.2, 0.0
                                              , GridBagConstraints.CENTER,
                                              GridBagConstraints.BOTH,
                                              new Insets(2, 2, 2, 2), 0, 0));
    }

    // Zeile.
    iZeile++;
    jpaWorkingOn.add(wbuSachkonto,
                     new GridBagConstraints(0, iZeile, 1, 1, 0, 0.0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.BOTH,
                                            new Insets(2, 2, 2, 2), 0, 0));
    jpaWorkingOn.add(wtfSachkonto,
                     new GridBagConstraints(1, iZeile, 1, 1, 0, 0.0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.BOTH,
                                            new Insets(2, 2, 2, 2), 0, 0));
    jpaWorkingOn.add(wlaSubdirectory,
                     new GridBagConstraints(3, iZeile, 1, 1, 0, 0.0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.BOTH,
                                            new Insets(2, 2, 2, 2), 0, 0));
    jpaWorkingOn.add(wnlfSubdirectory,
                     new GridBagConstraints(4, iZeile, 1, 1, 0, 0.0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.BOTH,
                                            new Insets(2, 2, 2, 2), 0, 0));

    // Zeile.
    iZeile++;
    jpaWorkingOn.add(wlaBezeichnung,
                     new GridBagConstraints(0, iZeile, 1, 1, 0, 0.0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.BOTH,
                                            new Insets(2, 2, 2, 2), 0, 0));
    jpaWorkingOn.add(wtfBezeichnung,
                     new GridBagConstraints(1, iZeile, 4, 1, 0, 0.0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.BOTH,
                                            new Insets(2, 2, 2, 2), 0, 0));
    // Zeile
    iZeile++;
    jpaWorkingOn.add(wlaBemerkung,
                     new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.BOTH,
                                            new Insets(2, 2, 2, 2), 0, 0));

    jpaWorkingOn.add(wefBemerkung,
                     new GridBagConstraints(1, iZeile, 5, 1, 0.0, 1.0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.BOTH,
                                            new Insets(2, 2, 2, 2), 0, 0));
    // Zeile
    iZeile++;
    jpaWorkingOn.add(wsfLagerOhneAbbuchung,
                     new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.BOTH,
                                            new Insets(2, 2, 2, 2), 0, 0));

    jpaWorkingOn.add(wsfLagerOhneAbbuchung.getWrapperTextField(),
                     new GridBagConstraints(1, iZeile, 5, 1, 0.0, 0.0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.HORIZONTAL,
                                            new Insets(2, 2, 2, 2), 0, 0));
    

  }


  protected void dto2Components()
      throws Throwable {

    wtfCNr.setText(getKostenstelleDto().getCNr());
    wtfBezeichnung.setText(getKostenstelleDto().getCBez());
    wcbIstProfitcenter.setShort(getKostenstelleDto().getBProfitcenter());
    wcbVersteckt.setShort(getKostenstelleDto().getBVersteckt());
    wefBemerkung.setText(getKostenstelleDto().getxBemerkung());
    wnlfSubdirectory.setText(getKostenstelleDto().getCSubdirectory());
    if (getKostenstelleDto().getKontoIId() != null) {
      KontoDto k = DelegateFactory.getInstance().getFinanzDelegate().kontoFindByPrimaryKey(
          getKostenstelleDto().getKontoIId());
      wtfSachkonto.setText(k.getCNr());
    }
    else {
      wtfSachkonto.setText(null);
    }
    
    wsfLagerOhneAbbuchung.setKey(getKostenstelleDto().getLagerIIdOhneabbuchung());
    
  }


  protected void components2Dto()
      throws Throwable {

    getKostenstelleDto().setCNr(wtfCNr.getText());
    getKostenstelleDto().setCBez(wtfBezeichnung.getText());
    getKostenstelleDto().setBProfitcenter(wcbIstProfitcenter.getShort());
    getKostenstelleDto().setxBemerkung(wefBemerkung.getText());
    getKostenstelleDto().setCSubdirectory(wnlfSubdirectory.getText());
    getKostenstelleDto().setBVersteckt(wcbVersteckt.getShort());
    getKostenstelleDto().setLagerIIdOhneabbuchung(wsfLagerOhneAbbuchung.getIKey());

    // Wegen zirkulaere Abh. beim Testaufbau-DB.
    getKostenstelleDto().setPersonalIIdAendern(
        LPMain.getInstance().getTheClient().getIDPersonal());
    getKostenstelleDto().setPersonalIIdAnlegen(
        LPMain.getInstance().getTheClient().getIDPersonal());
  }


  private KostenstelleDto getKostenstelleDto() {
    return ( (InternalFrameSystem) getInternalFrame()).getKostenstelleDto();
  }


  private void setKostenstelleDto(KostenstelleDto kostenstelleDtoI) {
    ( (InternalFrameSystem) getInternalFrame()).setKostenstelleDto(
        kostenstelleDtoI);
  }


  protected void eventActionDelete(ActionEvent e,
                                   boolean bAdministrateLockKeyI,
                                   boolean bNeedNoDeleteI)
      throws Throwable {

    DelegateFactory.getInstance().getSystemDelegate().
        removeKostenstelle(getKostenstelleDto().getIId());

    super.eventActionDelete(e, false, false);
  }


  public void eventActionNew(EventObject eventObject,
                             boolean bLockMeI,
                             boolean bNeedNoNewI)
      throws Throwable {

    super.eventActionNew(eventObject, true, false);

    if (!bNeedNoNewI) {
      setKostenstelleDto(new KostenstelleDto());
      setDefaults();
    }
  }


  public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
      throws Throwable {

    super.eventYouAreSelected(false);

    this.checkMandantLoggedInEqualsMandantSelected(LPMain.getInstance().getTheClient().
        getMandant(), getInternalFrameSystem().getMandantDto().getCNr());

    if (!bNeedNoYouAreSelectedI) {
      Object key = getKeyWhenDetailPanel();
      if (key == null
          || (key != null && key.equals(LPMain.getLockMeForNew()))) {
        leereAlleFelder(this);
        clearStatusbar();
        setDefaults();
      }
      else {
        setKostenstelleDto(DelegateFactory.getInstance().getSystemDelegate().
                           kostenstelleFindByPrimaryKey( (Integer) key));
        getInternalFrame().setLpTitle(
            InternalFrame.TITLE_IDX_AS_I_LIKE,
            getInternalFrameSystem().getMandantDto().getCNr());

        setStatusbar();
      }
      dto2Components();
      initPanel();
    }
  }


  public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
      throws Throwable {

    if (!bNeedNoSaveI) {
      if (allMandatoryFieldsSetDlg()) {
        components2Dto();
        if (getKostenstelleDto().getIId() == null) {
          //create
          Integer key = DelegateFactory.getInstance().getSystemDelegate().
              createKostenstelle(getKostenstelleDto());
          setKeyWhenDetailPanel(key);
          getKostenstelleDto().setIId(key);
        }
        else {
          //update
          DelegateFactory.getInstance().getSystemDelegate().updateKostenstelle(
              getKostenstelleDto());
        }
        super.eventActionSave(e, true);
        eventYouAreSelected(false);
      }
    }
    else {
      super.eventActionSave(e, true);
    }

  }


  protected void eventActionSpecial(ActionEvent e)
      throws Throwable {

    if (e.getActionCommand().equals(ACTION_SPECIAL_FLR_SACHKONTEN)) {
      String[] aWhichButtonIUse = {
          PanelBasis.ACTION_REFRESH,
          PanelBasis.ACTION_LEEREN,
      };
      QueryType[] querytypes = null;
      FilterKriterium[] filters = FinanzFilterFactory.getInstance().createFKSachkonten();
      panelQueryFLRSachkonto = new PanelQueryFLR(
          querytypes,
          filters,
          QueryParameters.UC_ID_FINANZKONTEN_SACHKONTEN,
          aWhichButtonIUse,
          getInternalFrame(),
          LPMain.getInstance().getTextRespectUISPr(
              "finanz.tab.unten.sachkonten.title"));
      FilterKriteriumDirekt fkDirekt1 = FinanzFilterFactory.getInstance().
          createFKDKontonummer();
      FilterKriteriumDirekt fkDirekt2 = FinanzFilterFactory.getInstance().
          createFKDKontobezeichnung();
      panelQueryFLRSachkonto.befuellePanelFilterkriterienDirekt(fkDirekt1, fkDirekt2);
      if(getKostenstelleDto().getKontoIId()!=null) {
        panelQueryFLRSachkonto.setSelectedId(getKostenstelleDto().getKontoIId());
      }
      new DialogQuery(panelQueryFLRSachkonto);
    }
  }


  protected String getLockMeWer()
      throws Exception {
    return HelperClient.LOCKME_KOSTENSTELLE;
  }


  protected void setStatusbar()
      throws Throwable {
    setStatusbarPersonalIIdAnlegen(getKostenstelleDto().getPersonalIIdAnlegen());
    setStatusbarPersonalIIdAendern(getKostenstelleDto().getPersonalIIdAendern());
    setStatusbarTAendern(getKostenstelleDto().getTAendern());
    setStatusbarTAnlegen(getKostenstelleDto().getTAnlegen());
  }


  protected void setDefaults()
      throws Throwable {
    wefBemerkung.setText(null);
    getKostenstelleDto().setBProfitcenter(Helper.boolean2Short(false));
    getKostenstelleDto().setBVersteckt(Helper.boolean2Short(false));
    getKostenstelleDto().setMandantCNr(getInternalFrameSystem().getMandantDto().getCNr());
  }


  private void initPanel() {

  }


  private InternalFrameSystem getInternalFrameSystem() {
    return (InternalFrameSystem) getInternalFrame();
  }


  protected void eventActionRefresh(ActionEvent aE, boolean bNeedNoRefreshI)
      throws Throwable {

    super.eventActionRefresh(aE, bNeedNoRefreshI);
    this.checkMandantLoggedInEqualsMandantSelected(LPMain.getInstance().getTheClient().
        getMandant(), getInternalFrameSystem().getMandantDto().getCNr());
  }


  /**
   *
   * @param loggedinMandant String
   * @param selectedMandant String
   * @throws Throwable
   */
  private void checkMandantLoggedInEqualsMandantSelected(String loggedinMandant,
      String selectedMandant)
      throws Throwable {

    if (!loggedinMandant.equals(selectedMandant)) {

      LPButtonAction item = (LPButtonAction) getHmOfButtons().get(PanelBasis.
          ACTION_UPDATE);
      LPButtonAction item1 = (LPButtonAction) getHmOfButtons().get(PanelBasis.
          ACTION_DELETE);

      item.getButton().setEnabled(false);
      item1.getButton().setEnabled(false);

      getPanelStatusbar().setLockField(LPMain.getInstance().getTextRespectUISPr(
          "system.nurleserecht"));
    }
  }

}
