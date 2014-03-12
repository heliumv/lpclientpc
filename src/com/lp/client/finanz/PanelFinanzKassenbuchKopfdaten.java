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
import java.math.BigDecimal;
import java.util.EventObject;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.Border;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperDateField;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.BuchenDelegate;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.util.fastlanereader.gui.QueryType;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.finanz.service.BuchungdetailDto;
import com.lp.server.finanz.service.KassenbuchDto;
import com.lp.server.finanz.service.KontoDtoSmall;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.FilterKriteriumDirekt;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.util.Helper;

@SuppressWarnings("static-access") 
/**
 * <p><I>Panel zur Anzeige der Kassenbuchkopfdaten</I> </p>
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 * <p>Erstellungsdatum <I>18. 11. 2004</I></p>
 * <p> </p>
 * @author  Martin Bluehweis
 * @version $Revision: 1.8 $
 */
public class PanelFinanzKassenbuchKopfdaten
    extends PanelBasis
{
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

private TabbedPaneKassenbuch tabbedPaneKassenbuch = null;

  private KontoDtoSmall kontoDto = null;

  private static final String ACTION_SPECIAL_KASSENBUCH_KONTO =
      "action_special_kassenbuch_konto";
  private PanelQueryFLR panelQueryFLRKonto = null;

  JPanel jPanelWorkingOn = new JPanel();
  Border border2;
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  WrapperTextField wtfBezeichnung = new WrapperTextField();
  WrapperLabel wlaBezeichnung = new WrapperLabel();
  WrapperTextField wtfKontoNummer = new WrapperTextField();
  WrapperDateField wdfGueltigVon = new WrapperDateField();
  WrapperLabel wlaGueltigVon = new WrapperLabel();
  WrapperDateField wdfGueltigBis = new WrapperDateField();
  WrapperCheckBox wcbHauptkassenbuch = new WrapperCheckBox();
  WrapperLabel wlaSaldo = new WrapperLabel();
  WrapperButton wbuKonto = new WrapperButton();
  WrapperNumberField wnfSaldo = null;
  GridBagLayout gridBagLayout2 = new GridBagLayout();
  WrapperLabel wlaBreite1 = new WrapperLabel();
  WrapperLabel wlaGueltigBis = new WrapperLabel();
  WrapperTextField wtfKontoBezeichnung = new WrapperTextField();
  WrapperLabel wrapperLabel2 = new WrapperLabel();
  WrapperCheckBox wcbNegativErlaubt = new WrapperCheckBox();
  
  public PanelFinanzKassenbuchKopfdaten(InternalFrame internalFrame,
                                        String add2TitleI, Object key,
                                        TabbedPaneKassenbuch
                                        tabbedPaneKassenbuch) throws Throwable {
    super(internalFrame, add2TitleI, key);
    this.tabbedPaneKassenbuch = tabbedPaneKassenbuch;
    
    jbInit();
    setDefaults();
    initComponents();
  }


  protected InternalFrameFinanz getInternalFrameFinanz() {
	  return (InternalFrameFinanz) getInternalFrame() ;
  }
  
  
  private String[] getActionButtons() {	
	  boolean isChefBuchhalter = false ;

	  try {
		  isChefBuchhalter = DelegateFactory.getInstance().getTheJudgeDelegate()
				  .hatRecht(RechteFac.RECHT_FB_CHEFBUCHHALTER);
	  } catch (Throwable ex) {
	  }

	  return isChefBuchhalter 
			  ? new String[] {ACTION_UPDATE, ACTION_SAVE, ACTION_DELETE, ACTION_DISCARD} 
	  		  : new String[] {} ;
  }
  
  
  /**
   * jbInit
   *
   * @throws Throwable
   */
  private void jbInit()
      throws Throwable {
    wnfSaldo = new WrapperNumberField();
    wnfSaldo.setNegativeWerteRoteinfaerben(true) ;
    
    border2 = BorderFactory.createEmptyBorder(10, 10, 10, 10);
    JPanel panelButtonAction = getToolsPanel();
//    String[] aWhichButtonIUse = {
//        ACTION_UPDATE,
//        ACTION_SAVE,
//        ACTION_DELETE,
//        ACTION_DISCARD};
    enableToolsPanelButtons(getActionButtons());
    //wegen dialogFLR
    getInternalFrame().addItemChangedListener(this);

    jPanelWorkingOn.setBorder(border2);
    jPanelWorkingOn.setLayout(gridBagLayout1);
    wtfBezeichnung.setColumns(0);
    wtfBezeichnung.setColumnsMax(40);
    wtfBezeichnung.setActivatable(true);
    wtfBezeichnung.setMandatoryField(true);

    wlaBezeichnung.setText(LPMain.getInstance().getTextRespectUISPr("lp.bezeichnung"));
    wlaGueltigVon.setText(LPMain.getInstance().getTextRespectUISPr("lp.gueltigab"));
    wcbHauptkassenbuch.setText(LPMain.getInstance().getTextRespectUISPr("finanz.hauptkassenbuch"));
    wcbHauptkassenbuch.setActivatable(true);
    wlaSaldo.setText(LPMain.getInstance().getTextRespectUISPr("finanz.saldo"));
    wbuKonto.setText(LPMain.getInstance().getTextRespectUISPr("button.konto"));
    wbuKonto.setToolTipText(LPMain.getInstance().getTextRespectUISPr("button.konto.tooltip"));
    wbuKonto.addActionListener(this);
    wbuKonto.setActionCommand(ACTION_SPECIAL_KASSENBUCH_KONTO);

    wnfSaldo.setActivatable(false);
    this.setLayout(gridBagLayout2);
    wlaBreite1.setMinimumSize(new Dimension(100, Defaults.getInstance().getControlHeight()));
    wlaBreite1.setPreferredSize(new Dimension(100, Defaults.getInstance().getControlHeight()));
    wlaGueltigBis.setText(LPMain.getInstance().getTextRespectUISPr("lp.bis"));
    wtfKontoBezeichnung.setActivatable(false);
    wrapperLabel2.setMinimumSize(new Dimension(30, Defaults.getInstance().getControlHeight()));
    wrapperLabel2.setPreferredSize(new Dimension(30, Defaults.getInstance().getControlHeight()));
    wtfKontoNummer.setActivatable(false);
    wtfKontoNummer.setMandatoryField(true);
    wdfGueltigVon.setActivatable(true);
    wdfGueltigVon.setMandatoryField(true);
    wdfGueltigBis.setActivatable(true);
    wcbNegativErlaubt.setText(LPMain.getInstance().getTextRespectUISPr("finanz.negativerlaubt"));
    this.add(panelButtonAction, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0
        , GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
        new Insets(0, 0, 0, 0), 0, 0));
    this.add(jPanelWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0
        , GridBagConstraints.CENTER, GridBagConstraints.BOTH,
        new Insets(0, 0, 0, 0), 0, 0));
    this.add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.BOTH,
        new Insets(0, 0, 0, 0), 0, 0));
    jPanelWorkingOn.add(wlaBezeichnung,
                        new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
                                               , GridBagConstraints.CENTER,
                                               GridBagConstraints.HORIZONTAL,
                                               new Insets(2, 2, 2, 2), 0, 0));
    jPanelWorkingOn.add(wtfBezeichnung,
                        new GridBagConstraints(1, 0, 3, 1, 0.0, 0.0
                                               , GridBagConstraints.CENTER,
                                               GridBagConstraints.HORIZONTAL,
                                               new Insets(2, 2, 2, 2), 0, 0));
    jPanelWorkingOn.add(wtfKontoNummer,
                        new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
                                               , GridBagConstraints.CENTER,
                                               GridBagConstraints.HORIZONTAL,
                                               new Insets(2, 2, 2, 2), 0, 0));
    jPanelWorkingOn.add(wdfGueltigVon,
                        new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0
                                               , GridBagConstraints.CENTER,
                                               GridBagConstraints.HORIZONTAL,
                                               new Insets(2, 2, 2, 2), 0, 0));
    jPanelWorkingOn.add(wlaGueltigVon,
                        new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
                                               , GridBagConstraints.CENTER,
                                               GridBagConstraints.HORIZONTAL,
                                               new Insets(2, 2, 2, 2), 0, 0));
    jPanelWorkingOn.add(wbuKonto, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
        new Insets(2, 2, 2, 2), 0, 0));
    jPanelWorkingOn.add(wrapperLabel2,
                        new GridBagConstraints(2, 4, 1, 1, 0.0, 0.0
                                               , GridBagConstraints.CENTER,
                                               GridBagConstraints.NONE,
                                               new Insets(0, 0, 0, 0), 0, 0));
    jPanelWorkingOn.add(wtfKontoBezeichnung,
                        new GridBagConstraints(2, 1, 2, 1, 2.0, 0.0
                                               , GridBagConstraints.CENTER,
                                               GridBagConstraints.BOTH,
                                               new Insets(2, 2, 2, 2), 0, 0));
    jPanelWorkingOn.add(wnfSaldo, new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
        new Insets(2, 2, 2, 2), 0, 0));
    jPanelWorkingOn.add(wlaBreite1, new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
        new Insets(2, 2, 2, 2), 0, 0));
    jPanelWorkingOn.add(wlaSaldo, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0
        , GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
        new Insets(3, 4, 1, 0), 0, 0));
    jPanelWorkingOn.add(wdfGueltigBis,
                        new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0
                                               , GridBagConstraints.CENTER,
                                               GridBagConstraints.HORIZONTAL,
                                               new Insets(2, 2, 2, 2), 0, 0));
    jPanelWorkingOn.add(wlaGueltigBis,
                        new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0
                                               , GridBagConstraints.CENTER,
                                               GridBagConstraints.HORIZONTAL,
                                               new Insets(2, 2, 2, 2), 0, 0));
    jPanelWorkingOn.add(wcbHauptkassenbuch,
                        new GridBagConstraints(2, 3, 2, 1, 0.0, 0.0
                                               , GridBagConstraints.WEST,
                                               GridBagConstraints.HORIZONTAL,
                                               new Insets(2, 2, 2, 2), 0, 0));
    jPanelWorkingOn.add(wcbNegativErlaubt,
                        new GridBagConstraints(2, 4, 2, 1, 0.0, 0.0
                                               , GridBagConstraints.WEST,
                                               GridBagConstraints.BOTH,
                                               new Insets(2, 2, 2, 2), 0, 0));
  }


  /**
   * Daten des Kassenbuchs ins Panel schreiben
   *
   * @throws Throwable
   */
  private void dto2Components() throws Throwable {
	  KassenbuchDto kassenbuchDto = getTabbedPaneKassenbuch().getKassenbuchDto();
	  kontoDto = DelegateFactory.getInstance().getFinanzDelegate().kontoFindByPrimaryKeySmall(
			  kassenbuchDto.getKontoIId());
	  dto2ComponentsKonto();

	  wtfBezeichnung.setText(kassenbuchDto.getCBez());

	  wnfSaldo.setBigDecimal(calculateSaldo(kassenbuchDto.getKontoIId())) ;
	  if (kassenbuchDto.getDGueltigBis() != null) {
		  wdfGueltigBis.setDate(kassenbuchDto.getDGueltigBis());
	  }
	  if (kassenbuchDto.getDGueltigVon() != null) {
		  wdfGueltigVon.setDate(kassenbuchDto.getDGueltigVon());
	  }
	  wcbHauptkassenbuch.setSelected(Helper.short2boolean(kassenbuchDto.
			  getBHauptkassenbuch()));
	  wcbNegativErlaubt.setSelected(Helper.short2boolean(kassenbuchDto.
			  getBNegativErlaubt()));

	  this.setStatusbarPersonalIIdAnlegen(kassenbuchDto.getPersonalIIdAnlegen());
	  this.setStatusbarTAnlegen(kassenbuchDto.getTAnlegen());
	  this.setStatusbarPersonalIIdAendern(kassenbuchDto.getPersonalIIdaendern());
	  this.setStatusbarTAendern(kassenbuchDto.getTAendern());
  }


  private BigDecimal calculateSaldo(Integer kontoIId) throws ExceptionLP, Throwable {
	  BuchenDelegate delegate = DelegateFactory.getInstance().getBuchenDelegate() ;
	  int geschaeftsjahr = getInternalFrameFinanz().getIAktuellesGeschaeftsjahr() ;

	  BigDecimal saldo = delegate.getSaldoVonKonto(kontoIId, 
			  getInternalFrameFinanz().getIAktuellesGeschaeftsjahr(), 12, true);
	  BigDecimal ebKonto = delegate.getSummeEroeffnungKontoIId(kontoIId,
			  geschaeftsjahr, 12, true) ;
	  if(null != saldo && null != ebKonto) {
		  saldo = saldo.add(ebKonto) ;
	  }

	  return saldo ;
  }
  
  private void components2Dto()
      throws Throwable {
    KassenbuchDto kassenbuchDto = getTabbedPaneKassenbuch().getKassenbuchDto();
    if (kassenbuchDto == null) {
      kassenbuchDto = new KassenbuchDto();
      kassenbuchDto.setMandantCNr(LPMain.getInstance().getTheClient().
                                  getMandant());
    }
    kassenbuchDto.setCBez(wtfBezeichnung.getText());
    kassenbuchDto.setDGueltigBis(wdfGueltigBis.getDate());
    kassenbuchDto.setDGueltigVon(wdfGueltigVon.getDate());
    kassenbuchDto.setKontoIId(kontoDto.getIId());
    kassenbuchDto.setBHauptkassenbuch(Helper.boolean2Short(wcbHauptkassenbuch.
        isSelected()));
    kassenbuchDto.setBNegativErlaubt(Helper.boolean2Short(wcbNegativErlaubt.
        isSelected()));
    getTabbedPaneKassenbuch().setKassenbuchDto(kassenbuchDto);
  }


  /**
   * Loesche das Kassenbuch.
   *
   * @param e ActionEvent
   * @param bAdministrateLockKeyI boolean
   * @param bNeedNoDeleteI boolean
   * @throws Throwable
   */
  protected void eventActionDelete(ActionEvent e,
                                   boolean bAdministrateLockKeyI,
                                   boolean bNeedNoDeleteI)
      throws Throwable {
    KassenbuchDto kassenbuchDto = getTabbedPaneKassenbuch().getKassenbuchDto();
    if (kassenbuchDto != null) {
      DelegateFactory.getInstance().getFinanzDelegate().removeKassenbuch(kassenbuchDto);
      super.eventActionDelete(e, true, true);
    }
  }


  /**
   * Speichere Kassenbuch.
   *
   * @param e ActionEvent
   * @param bNeedNoSaveI boolean
   * @throws Throwable
   */
  public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
      throws Throwable {
    if (allMandatoryFieldsSetDlg()) {
      components2Dto();
      KassenbuchDto kassenbuchDto = DelegateFactory.getInstance().getFinanzDelegate().
          updateKassenbuch(getTabbedPaneKassenbuch().getKassenbuchDto());
      this.setKeyWhenDetailPanel(kassenbuchDto.getIId());
      getTabbedPaneKassenbuch().setKassenbuchDto(kassenbuchDto);
      super.eventActionSave(e, true);
      eventYouAreSelected(false);
    }
  }


  public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
      throws Throwable {
    super.eventYouAreSelected(false);
    if (!bNeedNoYouAreSelectedI) {
      KassenbuchDto kassenbuchDto = getTabbedPaneKassenbuch().getKassenbuchDto();
      if (kassenbuchDto != null) {
        Object key = kassenbuchDto.getIId();
        getTabbedPaneKassenbuch().setKassenbuchDto(DelegateFactory.getInstance().
            getFinanzDelegate().kassenbuchFindByPrimaryKey( (Integer) key));
        dto2Components();
      }
    }
  }

  /**
   * eventActionSpecial.
   *
   * @param e ActionEvent
   * @throws Throwable
   */
  protected void eventActionSpecial(ActionEvent e)
      throws Throwable {
    if (e.getActionCommand().equals(ACTION_SPECIAL_KASSENBUCH_KONTO)) {
      dialogQueryKonto(e);
    }
  }


  /**
   * eventItemchanged.
   *
   * @param eI EventObject
   * @throws ExceptionForLPClients
   * @throws Throwable
   */
  protected void eventItemchanged(EventObject eI)
      throws Throwable {
    ItemChangedEvent e = (ItemChangedEvent) eI;
    if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
      if (e.getSource() == panelQueryFLRKonto) {
        Object key = ( (ISourceEvent) e.getSource()).getIdSelected();
        kontoDto = DelegateFactory.getInstance().getFinanzDelegate().
            kontoFindByPrimaryKeySmall( (Integer) key);
        this.dto2ComponentsKonto();
      }
    }
  }


  private void dialogQueryKonto(ActionEvent e)
      throws Throwable {
    String[] aWhichButtonIUse = {
        PanelBasis.ACTION_REFRESH,
        };
    QueryType[] qt = null;
    // nur Sachkonten dieses Mandanten
    FilterKriterium[] filters = FinanzFilterFactory.getInstance().createFKSachkonten();
    panelQueryFLRKonto = new PanelQueryFLR(
        qt,
        filters,
        QueryParameters.UC_ID_FINANZKONTEN,
        aWhichButtonIUse,
        getInternalFrame(),
        LPMain.getInstance().getTextRespectUISPr("finanz.liste.sachkonten"));
    FilterKriteriumDirekt fkDirekt1 = FinanzFilterFactory.getInstance().
        createFKDKontonummer();
    FilterKriteriumDirekt fkDirekt2 = FinanzFilterFactory.getInstance().
        createFKDKontobezeichnung();
    FilterKriterium fkVersteckt = FinanzFilterFactory.getInstance()
	.createFKVKonto();
    panelQueryFLRKonto.befuellePanelFilterkriterienDirektUndVersteckte(fkDirekt1, fkDirekt2, fkVersteckt);
    new DialogQuery(panelQueryFLRKonto);
  }


  /**
   * Neu.
   *
   * @param eventObject ActionEvent
   * @param bLockMeI boolean
   * @param bNeedNoNewI boolean
   * @throws Throwable
   */
  public void eventActionNew(EventObject eventObject, boolean bLockMeI,
                             boolean bNeedNoNewI)
      throws Throwable {
    super.eventActionNew(eventObject, true, false);
    getTabbedPaneKassenbuch().setKassenbuchDto(null);
    this.kontoDto = null;
    this.leereAlleFelder(this);
    setDefaults();
  }


  /**
   * Kontodaten anzeigen.
   */
  private void dto2ComponentsKonto() {
    if (kontoDto != null) {
      wtfKontoNummer.setText(kontoDto.getCNr());
      wtfKontoBezeichnung.setText(kontoDto.getCBez());
    }
    else {
      wtfKontoNummer.setText(null);
      wtfKontoBezeichnung.setText(null);
    }
  }


  /**
   * Defaults setzen.
   *
   * @throws Throwable
   */
  private void setDefaults() throws Throwable {
    this.wdfGueltigVon.setDate(new java.sql.Date(System.currentTimeMillis()));
    this.wnfSaldo.setDouble(null);
    wcbHauptkassenbuch.setSelected(false);
    wcbNegativErlaubt.setSelected(false);
  }


  public String getLockMeWer() {
    return HelperClient.LOCKME_FINANZ_KASSENBUCH;
  }


  private TabbedPaneKassenbuch getTabbedPaneKassenbuch() {
    return tabbedPaneKassenbuch;
  }


  /**
   * Drucken des Kontoblattes.
   *
   * @param e ActionEvent
   * @throws Throwable
   * @throws Throwable
   */
  protected void eventActionPrint(ActionEvent e)
      throws Throwable {
    if (getTabbedPaneKassenbuch().getKassenbuchDto() != null) {
      Integer kontoIId = getTabbedPaneKassenbuch().getKassenbuchDto().
          getKontoIId();
      BuchungdetailDto[] buchungen = DelegateFactory.getInstance().getBuchenDelegate().
          buchungdetailFindByKontoIId(kontoIId);
      if (buchungen.length == 0) {
        DialogFactory.showModalDialog(LPMain.getInstance().getTextRespectUISPr(
            "lp.error"),
            LPMain.getInstance().getTextRespectUISPr("finanz.error.keinebuchungenaufdiesemkonto"));
      }
      else {
        String sTitle = kontoDto.getCNr() + " " +
            LPMain.getInstance().getTextRespectUISPr("finanz.buchungen");
        getInternalFrame().showReportKriterien(new ReportBuchungenAufKonto(
            getInternalFrame(), kontoIId, sTitle));
      }
    }
  }

	protected void lPActionEvent(java.awt.event.ActionEvent e) throws Throwable {
		/*
		 * Es hat sich das Geschaeftsjahr ge&auml;ndert. Daten erneuern
		 */
		eventYouAreSelected(false) ;
	}

  protected javax.swing.JComponent getFirstFocusableComponent()
      throws Exception {
    return wtfBezeichnung;
  }

}
