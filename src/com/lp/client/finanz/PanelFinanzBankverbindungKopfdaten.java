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
import javax.swing.JPanel;
import javax.swing.border.Border;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperIBANField;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.partner.PartnerFilterFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.util.fastlanereader.gui.QueryType;
import com.lp.server.finanz.service.BankverbindungDto;
import com.lp.server.finanz.service.FinanzFac;
import com.lp.server.finanz.service.KontoDtoSmall;
import com.lp.server.partner.service.BankDto;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.util.EJBExceptionLP;

@SuppressWarnings("static-access") 
/**
 * <p><I>Panel zur Anzeige der Bankverbindungskopfdaten</I> </p>
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 * <p>Erstellungsdatum <I>18. 11. 2004</I></p>
 * <p> </p>
 * @author  Martin Bluehweis
 * @version $Revision: 1.7 $
 */
public class PanelFinanzBankverbindungKopfdaten
    extends PanelBasis {
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

private TabbedPaneBankverbindung tabbedPaneBankverbindung = null;

  private KontoDtoSmall kontoDto = null;
  private BankDto bankDto = null;
  private PanelQueryFLR panelQueryFLRBank = null;
  private PanelQueryFLR panelQueryFLRKonto = null;
  private static final String ACTION_SPECIAL_BANK =
      "action_special_bank";
  private static final String ACTION_SPECIAL_KONTO =
      "action_special_konto";

  private Border border1;
  private JPanel jPanelWorkingOn = new JPanel();
  private GridBagLayout gridBagLayout1 = new GridBagLayout();
  private Border border2;
  private WrapperTextField wtfKontoNummer = new WrapperTextField();
  private WrapperLabel wlaBankkontoNummer = new WrapperLabel();
  private WrapperTextField wtfBankkontoNummer = new WrapperTextField();
  private WrapperTextField wtfBankBezeichnung1 = new WrapperTextField();
  private WrapperLabel wlaBlz = new WrapperLabel();
  private WrapperTextField wtfBlz = new WrapperTextField();
  private WrapperTextField wtfBankBezeichnung2 = new WrapperTextField();
  private WrapperLabel wlaIban = new WrapperLabel();
  private WrapperIBANField wtfIban = null;
  private WrapperButton wbuBank = new WrapperButton();
  private WrapperButton wbuKonto = new WrapperButton();
  private WrapperTextField wtfKontoBezeichnung = new WrapperTextField();
  private WrapperLabel wlaAbstand = new WrapperLabel();
  private WrapperLabel wlaBezeichnung = new WrapperLabel();
  private WrapperTextField wtfBezeichnung = new WrapperTextField();
  private GridBagLayout gridBagLayout2 = new GridBagLayout();
  private WrapperLabel wlaButtonBreiteLinks = new WrapperLabel();
  private WrapperLabel wlaButtonBreiteRechts = new WrapperLabel();
  private WrapperLabel wlaBic=new WrapperLabel();
  private WrapperTextField wtfBic=new WrapperTextField();

  public PanelFinanzBankverbindungKopfdaten(InternalFrame internalFrame,
                                            String add2TitleI, Object key,
                                            TabbedPaneBankverbindung
                                            tabbedPaneBankverbindung) throws Throwable {
    super(internalFrame, add2TitleI, key);
    this.tabbedPaneBankverbindung = tabbedPaneBankverbindung;
    jbInit();
    setDefaults();
    initComponents();
  }


  /**
   * jbInit
   *
   * @throws Throwable
   */
  private void jbInit() throws Throwable {
    border2 = BorderFactory.createEmptyBorder(10, 10, 10, 10);
    border1 = BorderFactory.createEmptyBorder(10, 10, 10, 10);

    JPanel panelButtonAction = getToolsPanel();
    getInternalFrame().addItemChangedListener(this);
    jPanelWorkingOn.setLayout(gridBagLayout1);
    jPanelWorkingOn.setBorder(border2);
    wtfKontoNummer.setMandatoryField(true);
    wtfKontoNummer.setActivatable(false);
    wlaBankkontoNummer.setText(LPMain.getInstance().getTextRespectUISPr("lp.kontonr"));
    wtfBankkontoNummer.setMandatoryField(true);
    wtfBankkontoNummer.setColumnsMax(FinanzFac.MAX_BANKVERBINDUNG_BANKKONTONUMMER);
    wtfBankkontoNummer.setActivatable(true);
    wtfBankBezeichnung1.setMandatoryField(true);
    wtfBankBezeichnung1.setActivatable(false);
    wlaBic.setText(LPMain.getInstance().getTextRespectUISPr("lp.bic"));
    wtfBic.setActivatable(false);
    wlaBlz.setText(LPMain.getInstance().getTextRespectUISPr("lp.blz"));
    wtfBlz.setActivatable(false);
    wtfBankBezeichnung2.setActivatable(false);
    wlaIban.setText(LPMain.getInstance().getTextRespectUISPr("lp.iban"));
    
    wtfIban = new WrapperIBANField();
    
    wtfIban.setActivatable(true);
    wbuBank.setText(LPMain.getInstance().getTextRespectUISPr("button.bank"));
    wbuBank.setToolTipText(LPMain.getInstance().getTextRespectUISPr("button.bank.tooltip"));
    wbuBank.addActionListener(this);
    wbuBank.setActionCommand(ACTION_SPECIAL_BANK);
    wbuKonto.setText(LPMain.getInstance().getTextRespectUISPr("button.sachkonto"));
    wbuKonto.setToolTipText(LPMain.getInstance().getTextRespectUISPr("button.sachkonto.tooltip"));
    wbuKonto.addActionListener(this);
    wbuKonto.setActionCommand(ACTION_SPECIAL_KONTO);
    wtfKontoBezeichnung.setActivatable(false);
    wlaAbstand.setText("  ");
    wlaBezeichnung.setRequestFocusEnabled(true);
    wlaBezeichnung.setText(LPMain.getInstance().getTextRespectUISPr("lp.bezeichnung"));
    wtfBezeichnung.setColumnsMax(FinanzFac.MAX_BANKVERBINDUNG_BEZEICHNUNG);
    wtfBezeichnung.setActivatable(true);
    this.setLayout(gridBagLayout2);
    wlaButtonBreiteLinks.setMinimumSize(new Dimension(100, 23));
    wlaButtonBreiteLinks.setPreferredSize(new Dimension(100, 23));
    wlaButtonBreiteRechts.setMinimumSize(new Dimension(70, 23));
    wlaButtonBreiteRechts.setPreferredSize(new Dimension(70, 23));
    this.add(panelButtonAction, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
        , GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
        new Insets(0, 0, 0, 0), 0, 0));
    this.add(jPanelWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0
        , GridBagConstraints.CENTER, GridBagConstraints.BOTH,
        new Insets(0, 0, 0, 0), 0, 0));
    this.add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.BOTH,
        new Insets(0, 0, 0, 0), 0, 0));
    jPanelWorkingOn.add(wbuBank,
                        new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0,
                                               GridBagConstraints.CENTER,
                                               GridBagConstraints.BOTH,
                                               new Insets(2, 2, 2, 2), 0, 0));
    jPanelWorkingOn.add(wtfBankBezeichnung1,
                        new GridBagConstraints(1, iZeile, 4, 1, 0.0, 0.0,
                                               GridBagConstraints.CENTER,
                                               GridBagConstraints.HORIZONTAL,
                                               new Insets(2, 2, 2, 2), 0, 0));
    iZeile++;
    jPanelWorkingOn.add(wtfBankBezeichnung2,
                        new GridBagConstraints(1, iZeile, 4, 1, 0.0, 0.0,
                                               GridBagConstraints.CENTER,
                                               GridBagConstraints.HORIZONTAL,
                                               new Insets(2, 2, 2, 2), 0, 0));
    iZeile++;
    jPanelWorkingOn.add(wlaBlz,
                        new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0,
                                               GridBagConstraints.CENTER,
                                               GridBagConstraints.HORIZONTAL,
                                               new Insets(2, 2, 2, 2), 0, 0));
    jPanelWorkingOn.add(wtfBlz,
                        new GridBagConstraints(1, iZeile, 1, 1, 1.0, 0.0,
                                               GridBagConstraints.CENTER,
                                               GridBagConstraints.HORIZONTAL,
                                               new Insets(2, 2, 2, 2), 0, 0));
      iZeile++;
    jPanelWorkingOn.add(wlaBic,
                        new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0,
                                               GridBagConstraints.CENTER,
                                               GridBagConstraints.HORIZONTAL,
                                               new Insets(2, 2, 2, 2), 0, 0));
    jPanelWorkingOn.add(wtfBic,
                        new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0,
                                               GridBagConstraints.CENTER,
                                               GridBagConstraints.HORIZONTAL,
                                               new Insets(2, 2, 2, 2), 0, 0));
    iZeile++;
    jPanelWorkingOn.add(wlaBankkontoNummer,
                        new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0,
                                               GridBagConstraints.CENTER,
                                               GridBagConstraints.HORIZONTAL,
                                               new Insets(2, 2, 2, 2), 0, 0));
    jPanelWorkingOn.add(wtfBankkontoNummer,
                        new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0,
                                               GridBagConstraints.CENTER,
                                               GridBagConstraints.HORIZONTAL,
                                               new Insets(2, 2, 2, 2), 0, 0));
    jPanelWorkingOn.add(wlaAbstand,
                        new GridBagConstraints(2, iZeile, 1, 1, 0.0, 0.0,
                                               GridBagConstraints.CENTER,
                                               GridBagConstraints.NONE,
                                               new Insets(0, 0, 0, 0), 0, 0));
    iZeile++;
    jPanelWorkingOn.add(wlaIban,
                        new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0,
                                               GridBagConstraints.CENTER,
                                               GridBagConstraints.HORIZONTAL,
                                               new Insets(2, 2, 2, 2), 0, 0));
    jPanelWorkingOn.add(wtfIban,
                        new GridBagConstraints(1, iZeile, 4, 1, 0.0, 0.0,
                                               GridBagConstraints.CENTER,
                                               GridBagConstraints.HORIZONTAL,
                                               new Insets(2, 2, 2, 2), 0, 0));
    iZeile++;
    jPanelWorkingOn.add(wlaBezeichnung,
                        new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0,
                                               GridBagConstraints.CENTER,
                                               GridBagConstraints.BOTH,
                                               new Insets(2, 2, 2, 2), 0, 0));
    jPanelWorkingOn.add(wtfBezeichnung,
                        new GridBagConstraints(1, iZeile, 4, 1, 0.0, 0.0,
                                               GridBagConstraints.CENTER,
                                               GridBagConstraints.HORIZONTAL,
                                               new Insets(2, 2, 2, 2), 0, 0));
    iZeile++;
    jPanelWorkingOn.add(wbuKonto,
                        new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0,
                                               GridBagConstraints.CENTER,
                                               GridBagConstraints.BOTH,
                                               new Insets(2, 2, 2, 2), 0, 0));
    jPanelWorkingOn.add(wtfKontoNummer,
                        new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0,
                                               GridBagConstraints.CENTER,
                                               GridBagConstraints.HORIZONTAL,
                                               new Insets(2, 2, 2, 2), 0, 0));
    jPanelWorkingOn.add(wtfKontoBezeichnung,
                        new GridBagConstraints(3, iZeile, 2, 1, 0.0, 0.0,
                                               GridBagConstraints.CENTER,
                                               GridBagConstraints.HORIZONTAL,
                                               new Insets(2, 2, 2, 2), 0, 0));
    iZeile++;
    jPanelWorkingOn.add(wlaButtonBreiteLinks,
                        new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0,
                                               GridBagConstraints.CENTER,
                                               GridBagConstraints.NONE,
                                               new Insets(0, 0, 0, 0), 0, 0));
    jPanelWorkingOn.add(wlaButtonBreiteRechts,
                        new GridBagConstraints(3, iZeile, 1, 1, 0.0, 0.0,
                                               GridBagConstraints.CENTER,
                                               GridBagConstraints.NONE,
                                               new Insets(0, 0, 0, 0), 0, 0));
    iZeile++;
  }

  /**
   * Die Daten der Bankverbindung ins Panel schreiben
   *
   * @throws Throwable
   */
  private void dto2Components() throws Throwable {
    BankverbindungDto bankverbindungDto = getTabbedPaneBankverbindung().
        getBankverbindungDto();
    if (bankverbindungDto != null) {
      wtfBankkontoNummer.setText(bankverbindungDto.getCKontonummer());
      wtfIban.setIban(bankverbindungDto.getCIban());
      wtfBezeichnung.setText(bankverbindungDto.getCBez());

      setStatusbarModification(bankverbindungDto) ;

      // Konto und Bank holen
      kontoDto = DelegateFactory.getInstance().getFinanzDelegate().kontoFindByPrimaryKeySmall(
          bankverbindungDto.getKontoIId());
      bankDto = DelegateFactory.getInstance().getPartnerbankDelegate().bankFindByPrimaryKey(
          bankverbindungDto.getBankIId());
    }
    dto2ComponentsKonto();
    dto2ComponentsBank();
  }

  private void components2Dto()
      throws Throwable {
    BankverbindungDto bankverbindungDto = getTabbedPaneBankverbindung().
        getBankverbindungDto();
    // neue Bankverbindung: neues Dto  ;-)
    if (bankverbindungDto == null) {
      bankverbindungDto = new BankverbindungDto();
      bankverbindungDto.setMandantCNr(LPMain.getInstance().getTheClient().
                                      getMandant());
    }
    if (kontoDto != null) {
      bankverbindungDto.setKontoIId(kontoDto.getIId());
    }
    if (bankDto != null) {
      bankverbindungDto.setBankIId(bankDto.getPartnerIId());
    }
    bankverbindungDto.setCBez(wtfBezeichnung.getText());
    bankverbindungDto.setCIban(wtfIban.getIban());
    bankverbindungDto.setCKontonummer(wtfBankkontoNummer.getText());
    getTabbedPaneBankverbindung().setBankverbindungDto(bankverbindungDto);
  }


  /**
   * Loesche das Konto.
   *
   * @param e ActionEvent
   * @param bAdministrateLockKeyI boolean
   * @param bNeedNoDeleteI boolean
   * @throws Throwable
   */
  protected void eventActionDelete(ActionEvent e, boolean bAdministrateLockKeyI,
                                   boolean bNeedNoDeleteI)
      throws Throwable {
    BankverbindungDto bankverbindungDto = getTabbedPaneBankverbindung().
        getBankverbindungDto();
    if (bankverbindungDto != null) {
      DelegateFactory.getInstance().getFinanzDelegate().removeBankverbindung(bankverbindungDto);
      super.eventActionDelete(e, true, true);
    }
  }

  /**
   * Speichere Daten des Panels
   *
   * @param e ActionEvent
   * @param bNeedNoSaveI boolean
   * @throws Throwable
   */
  public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
      throws Throwable {
    if (allMandatoryFieldsSetDlg()) {
      components2Dto();
      BankverbindungDto bankverbindungDto = DelegateFactory.getInstance().
          getFinanzDelegate().updateBankverbindung(getTabbedPaneBankverbindung().
          getBankverbindungDto());
      this.setKeyWhenDetailPanel(bankverbindungDto.getIId());
      getTabbedPaneBankverbindung().setBankverbindungDto(bankverbindungDto);
      super.eventActionSave(e, true);
      eventYouAreSelected(false);
    }
  }

  protected void eventActionSpecial(ActionEvent e) throws Throwable {
    if (e.getActionCommand().equals(ACTION_SPECIAL_KONTO)) {
      dialogQueryKonto(e);
    }
    if (e.getActionCommand().equals(ACTION_SPECIAL_BANK)) {
      dialogQueryBank(e);
    }
  }

  protected void eventItemchanged(EventObject eI) throws Throwable {
    ItemChangedEvent e = (ItemChangedEvent) eI;
    if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
      if (e.getSource() == panelQueryFLRKonto) {
        Object key = ( (ISourceEvent) e.getSource()).getIdSelected();
        kontoDto = DelegateFactory.getInstance().getFinanzDelegate().
            kontoFindByPrimaryKeySmall( (Integer) key);
        dto2ComponentsKonto();
      }
      else if (e.getSource() == panelQueryFLRBank) {
        Object key = ( (ISourceEvent) e.getSource()).getIdSelected();
        bankDto = DelegateFactory.getInstance().getPartnerbankDelegate().bankFindByPrimaryKey( (
            Integer) key);
        dto2ComponentsBank();
      }
    }
  }

  void dialogQueryKonto(ActionEvent e)
      throws Throwable {
	  
	  Integer kontoIId=null;
	  if (kontoDto != null) {
		  kontoIId=kontoDto.getIId();
	    }
	  
	  panelQueryFLRKonto= FinanzFilterFactory.getInstance().createPanelFLRSachKonto(getInternalFrame(),
			  kontoIId, false);
	      new DialogQuery(panelQueryFLRKonto);
  }

  void dialogQueryBank(ActionEvent e)
      throws Throwable {
    String[] aWhichButtonIUse = {
        PanelBasis.ACTION_REFRESH,
        };
    QueryType[] qt = null;
    FilterKriterium[] filters = null;
    panelQueryFLRBank = new PanelQueryFLR(
        qt,
        filters,
        QueryParameters.UC_ID_BANK,
        aWhichButtonIUse,
        getInternalFrame(),
        LPMain.getInstance().getTextRespectUISPr("part.kund.banken"));
    panelQueryFLRBank.befuellePanelFilterkriterienDirekt(
        PartnerFilterFactory.getInstance().createFKDBankName(),
        PartnerFilterFactory.getInstance().createFKDBankBLZ());
    if(bankDto !=null) {
      panelQueryFLRBank.setSelectedId(bankDto.getPartnerIId());
    }
    new DialogQuery(panelQueryFLRBank);
  }


  /**
   * Neu
   *
   * @param eventObject ActionEvent
   * @param bLockMeI boolean
   * @param bNeedNoNewI boolean
   * @throws Throwable
   */
  public void eventActionNew(EventObject eventObject, boolean bLockMeI,
                             boolean bNeedNoNewI) throws Throwable {
    super.eventActionNew(eventObject, true, false);
    leereAlleFelder(this);

    getTabbedPaneBankverbindung().setBankverbindungDto(null);
    kontoDto = null;
    bankDto = null;
  }

  public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI) throws Throwable {
    super.eventYouAreSelected(false);
    if (!bNeedNoYouAreSelectedI) {
      BankverbindungDto bankverbindungDto = getTabbedPaneBankverbindung().
          getBankverbindungDto();
      if (bankverbindungDto != null) {
        Object key = bankverbindungDto.getIId();
        //muss: alter kunde; einlesen; wenn bereits geloescht: exception nach oben.
        getTabbedPaneBankverbindung().setBankverbindungDto(DelegateFactory.getInstance().
            getFinanzDelegate().bankverbindungFindByPrimaryKey( (Integer) key));
        dto2Components();
      }
    }
  }
  /**
   * Daten des ausgewaehlten Kontos ins Panel schreiben
   */
  private void dto2ComponentsKonto() {
    if (kontoDto != null) {
      this.wtfKontoNummer.setText(kontoDto.getCNr());
      this.wtfKontoBezeichnung.setText(kontoDto.getCBez());
    }
  }

  /**
   * Daten der ausgewaehlten Bank ins Panel schreiben
   */
  private void dto2ComponentsBank() {
    if (bankDto != null) {
      this.wtfBankBezeichnung1.setText(bankDto.getPartnerDto().getCName1nachnamefirmazeile1());
      this.wtfBankBezeichnung2.setText(bankDto.getPartnerDto().getCName2vornamefirmazeile2());
      this.wtfBlz.setText(bankDto.getCBlz());
      this.wtfBic.setText(bankDto.getCBic());
    }
  }

  private void setDefaults()
      throws Throwable {
    String[] aWhichButtonIUse = {
        ACTION_UPDATE,
        ACTION_SAVE,
        ACTION_DELETE,
        ACTION_DISCARD};
    enableToolsPanelButtons(aWhichButtonIUse);
  }

  /**
   * Drucken des Kontoblattes
   *
   * @param e ActionEvent
   * @throws Throwable
   */
  protected void eventActionPrint(ActionEvent e) throws Throwable {
    this.tabbedPaneBankverbindung.printKontoblatt();
  }

  public String getLockMeWer() {
    return HelperClient.LOCKME_FINANZ_BANKVERBINDUNG;
  }

  private TabbedPaneBankverbindung getTabbedPaneBankverbindung() {
    return tabbedPaneBankverbindung;
  }

  public boolean handleOwnException(ExceptionLP exfc) throws Throwable {
    if (exfc.getICode() == EJBExceptionLP.FEHLER_FINANZ_BANKVERBINDUNG_KONTO_SCHON_ZUGEWIESEN) {
      DialogFactory.showModalDialog(LPMain.getInstance().getTextRespectUISPr(
          "lp.error"),
          LPMain.getInstance().getTextRespectUISPr("finanz.error.bankverbindungkonto"));
      return true;
    }
    else {
      return false;
    }
  }


  protected javax.swing.JComponent getFirstFocusableComponent()
      throws Exception {
    return wbuBank;
  }
}
