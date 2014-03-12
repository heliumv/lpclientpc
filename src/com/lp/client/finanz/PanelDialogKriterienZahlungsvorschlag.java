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


import java.awt.GridBagConstraints;
import java.awt.HeadlessException;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.EventObject;

import javax.swing.JComponent;

import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelDialogKriterien;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperDateField;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.eingangsrechnung.service.ZahlungsvorschlagkriterienDto;
import com.lp.server.finanz.service.BankverbindungDto;
import com.lp.server.partner.service.BankDto;
import com.lp.server.system.service.MandantDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;

@SuppressWarnings("static-access") 
/**
 * <p> Diese Klasse kuemmert sich um die Eingabe der kriterien zum ZV</p>
 *
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008, 2007</p>
 *
 * <p>Erstellung: Martin Bluehweis; 10.02.07</p>
 *
 * <p>@author $Author: sebastian $</p>
 *
 * @version not attributable Date $Date: 2008/09/22 15:36:44 $
 */
public class PanelDialogKriterienZahlungsvorschlag
    extends PanelDialogKriterien
{
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private WrapperLabel wlaStichtag = null;
  private WrapperDateField wdfStichtag = null;

  private WrapperLabel wlaNaechster = null;
  private WrapperDateField wdfNaechster = null;

  private WrapperCheckBox wcbMitSkonto = null;

  private WrapperLabel wlaUEZFrist = null;
  private WrapperNumberField wnfUEZFrist = null;

  private WrapperButton wbuBankverbindung = null;
  private WrapperTextField wtfBankverbindung = null;

  private BankverbindungDto bankverbindungDto = null;
  private PanelQueryFLR panelQueryFLRBank = null;

  private final static String ACTION_SPECIAL_BANKVERBINDUNG = "action_special_bankverbindung";

  public PanelDialogKriterienZahlungsvorschlag(
      InternalFrame oInternalFrameI,
      String title)
      throws HeadlessException, Throwable {
    super(oInternalFrameI, title);
    jbInit();
    setDefaults();
    initComponents();
  }


  private void jbInit()
      throws Throwable {
    getInternalFrame().addItemChangedListener(this);


    wlaStichtag = new WrapperLabel(LPMain.getInstance().getTextRespectUISPr(
        "finanz.zahlungsstichtag"));
    wdfStichtag = new WrapperDateField();
    wdfStichtag.setMandatoryField(true);

    wlaNaechster = new WrapperLabel(LPMain.getInstance().getTextRespectUISPr(
        "finanz.naechsterzahlungslauf"));
    wdfNaechster = new WrapperDateField();
    wdfNaechster.setMandatoryField(true);

    wcbMitSkonto = new WrapperCheckBox();
    wcbMitSkonto.setText(LPMain.getInstance().getTextRespectUISPr("finanz.mitskonto"));

    wlaUEZFrist =new WrapperLabel(LPMain.getInstance().getTextRespectUISPr(
        "finanz.skontoueberziehungsfrist"));
    wnfUEZFrist = new WrapperNumberField();
    wnfUEZFrist.setMaximumIntegerDigits(2);
    wnfUEZFrist.setFractionDigits(0);
    wnfUEZFrist.setMinimumValue(0);

    wbuBankverbindung = new WrapperButton();
    wbuBankverbindung.setText(LPMain.getInstance().getTextRespectUISPr("button.bankverbindung"));
    wbuBankverbindung.setToolTipText(LPMain.getInstance().getTextRespectUISPr("button.bankverbindung.tooltip"));
    wbuBankverbindung.setActionCommand(ACTION_SPECIAL_BANKVERBINDUNG);
    wbuBankverbindung.addActionListener(this);

    wtfBankverbindung = new WrapperTextField();
    wtfBankverbindung.setMandatoryField(true);
    wtfBankverbindung.setColumnsMax(100);

    iZeile++;
    jpaWorkingOn.add(wlaStichtag,
                     new GridBagConstraints(0, iZeile, 1, 1, 1.0, 0.0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.BOTH,
                                            new Insets(2, 2, 2, 2), 0, 0));

    jpaWorkingOn.add(wdfStichtag,
                     new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.BOTH,
                                            new Insets(2, 2, 2, 2), 0, 0));

    iZeile++;
    jpaWorkingOn.add(wlaNaechster,
                     new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.BOTH,
                                            new Insets(2, 2, 2, 2), 0, 0));

    jpaWorkingOn.add(wdfNaechster,
                     new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.BOTH,
                                            new Insets(2, 2, 2, 2), 0, 0));

    iZeile++;
    jpaWorkingOn.add(wcbMitSkonto,
                     new GridBagConstraints(1, iZeile, 1, 1, 1.0, 0.0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.BOTH,
                                            new Insets(2, 2, 2, 2), 0, 0));

    iZeile++;
    jpaWorkingOn.add(wlaUEZFrist,
                     new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.BOTH,
                                            new Insets(2, 2, 2, 2), 0, 0));

    jpaWorkingOn.add(wnfUEZFrist,
                     new GridBagConstraints(1, iZeile, 1, 1, 1.0, 0.0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.BOTH,
                                            new Insets(2, 2, 2, 2), 0, 0));

    iZeile++;
    jpaWorkingOn.add(wbuBankverbindung,
                     new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.BOTH,
                                            new Insets(2, 2, 2, 2), 0, 0));

    jpaWorkingOn.add(wtfBankverbindung,
                     new GridBagConstraints(1, iZeile, 1, 1, 1.0, 0.0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.BOTH,
                                            new Insets(2, 2, 2, 2), 0, 0));

  }


  private void setDefaults()
      throws Throwable {
    java.sql.Date dHeute = new java.sql.Date(System.currentTimeMillis());
    wdfStichtag.setDate(dHeute);
    // Periode
    ParametermandantDto parametermandantDtoPeriode =
        DelegateFactory.getInstance().getParameterDelegate().getMandantparameter(
            LPMain.getInstance().getTheClient().getMandant(),
            ParameterFac.KATEGORIE_EINGANGSRECHNUNG,
            ParameterFac.PARAMETER_ZAHLUNGSVORSCHLAG_PERIODE);
    if (parametermandantDtoPeriode != null) {
      int iTage = (Integer)parametermandantDtoPeriode.getCWertAsObject();
      dHeute = new java.sql.Date(dHeute.getTime()+iTage*24*60*60*1000);
    }
    wdfNaechster.setDate(dHeute);
    // Mit Skonto
    ParametermandantDto parametermandantDtoMitSkonto =
        DelegateFactory.getInstance().getParameterDelegate().getMandantparameter(
            LPMain.getInstance().getTheClient().getMandant(),
            ParameterFac.KATEGORIE_EINGANGSRECHNUNG,
            ParameterFac.PARAMETER_ZAHLUNGSVORSCHLAG_MIT_SKONTO);
    boolean bMitSkonto = true;
    if (parametermandantDtoMitSkonto!=null) {
      bMitSkonto = (Boolean)parametermandantDtoMitSkonto.getCWertAsObject();
    }
    wcbMitSkonto.setSelected(bMitSkonto);
    // Ueberziehungsfrist
    ParametermandantDto parametermandantDtoFrist =
        DelegateFactory.getInstance().getParameterDelegate().getMandantparameter(
            LPMain.getInstance().getTheClient().getMandant(),
            ParameterFac.KATEGORIE_EINGANGSRECHNUNG,
            ParameterFac.PARAMETER_ZAHLUNGSVORSCHLAG_SKONTOUEBERZIEHUNGSFRIST);
    int iUEZFrist = 0;
    if (parametermandantDtoFrist!=null) {
      iUEZFrist = (Integer) parametermandantDtoFrist.getCWertAsObject();
    }
    wnfUEZFrist.setInteger(iUEZFrist);

    MandantDto mandantDto = DelegateFactory.getInstance().getMandantDelegate().
        mandantFindByPrimaryKey(LPMain.getInstance().getTheClient().getMandant());
    if (mandantDto.getIBankverbindung() != null) {
      bankverbindungDto = DelegateFactory.getInstance().getFinanzDelegate().
          bankverbindungFindByPrimaryKey(mandantDto.getIBankverbindung());
      dto2ComponentsBankverbindung();
    }

  }


  protected void dto2ComponentsBankverbindung() throws Throwable {
    if (bankverbindungDto != null) {
      if (bankverbindungDto.getCBez()!=null) {
        wtfBankverbindung.setText(bankverbindungDto.getCBez());
      }
      else {
        BankDto bankDto = DelegateFactory.getInstance().getPartnerbankDelegate().bankFindByPrimaryKey(bankverbindungDto.getBankIId());
        wtfBankverbindung.setText(bankverbindungDto.getCKontonummer()+" | "+bankDto.getPartnerDto().formatFixTitelName1Name2());
      }
    }
    else {
      wtfBankverbindung.setText(null);
    }
  }


  void dialogQueryBank(ActionEvent e)
      throws Throwable {
    // ffcreatespanel: fuer eine dialogquery
    panelQueryFLRBank = FinanzFilterFactory.getInstance().createPanelFLRBankverbindung(
        getInternalFrame(), false, false);
    new DialogQuery(panelQueryFLRBank);
  }


  protected void eventActionSpecial(ActionEvent e)
      throws Throwable {
    super.eventActionSpecial(e);
    if (e.getActionCommand().equals(ACTION_SPECIAL_BANKVERBINDUNG)) {
      dialogQueryBank(e);
    }
  }

  protected void eventItemchanged(EventObject eI) throws Throwable {
    ItemChangedEvent e = (ItemChangedEvent) eI;
    if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
      if (e.getSource() == panelQueryFLRBank) {
        Object key = ( (ISourceEvent) e.getSource()).getIdSelected();
        if (key != null) {
          bankverbindungDto = DelegateFactory.getInstance().getFinanzDelegate().
              bankverbindungFindByPrimaryKey( (Integer) key);
          dto2ComponentsBankverbindung();
        }
      }
    }
  }


  public ZahlungsvorschlagkriterienDto getKriterienDto()
      throws Throwable {
    ZahlungsvorschlagkriterienDto krit = null;
    if (allMandatoryFieldsSetDlg()) {
      krit = new ZahlungsvorschlagkriterienDto();
      krit.setBankvertbindungIId(bankverbindungDto.getIId());
      krit.setBMitSkonto(wcbMitSkonto.isSelected());
      krit.setDNaechsterZahlungslauf(wdfNaechster.getDate());
      krit.setDZahlungsstichtag(wdfStichtag.getDate());
      krit.setISkontoUeberziehungsfristInTage(wnfUEZFrist.getInteger());
    }
    return krit;
  }

  protected JComponent getFirstFocusableComponent()
      throws Exception {
    return wdfStichtag;
  }
}
