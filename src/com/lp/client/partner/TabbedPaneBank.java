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
package com.lp.client.partner;


import javax.swing.event.ChangeEvent;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQuery;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.TabbedPane;
import com.lp.client.frame.component.WrapperMenuBar;
import com.lp.client.pc.LPMain;
import com.lp.client.util.fastlanereader.gui.QueryType;
import com.lp.server.partner.service.BankDto;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.partner.service.PartnerbankDto;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.util.EJBExceptionLP;

@SuppressWarnings("static-access") 
/**
 * <p>Diese Klasse kuemmert sich um das BankCRUD.</p>
 *
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 *
 * <p>Erstellungsdatum <I>22.03.05</I></p>
 *
 * @author $Author: valentin $
 *
 * @version $Revision: 1.3 $
 */
public class TabbedPaneBank
    extends TabbedPane
{
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private static int IDX_PANE_BANK_QP1 = 0;
  private static int IDX_PANE_BANK_D2 = 1;

  private PartnerDto partnerDto = new PartnerDto();
  private BankDto bankDto = new BankDto();
  private PartnerbankDto partnerBankDto = new PartnerbankDto();


  private PanelQuery panelBankQP1 = null;
  private PanelBasis panelBankD2 = null;

  private final static String EXTRA_NEU_AUS_PARTNER = "neu_aus_partner";
  private PanelQueryFLR panelPartnerQPFLR = null;

  public TabbedPaneBank(InternalFrame internalFrameI)
      throws Throwable {
    super(internalFrameI, LPMain.getTextRespectUISPr("part.kund.banken"));
    jbInit();
    initComponents();
  }


  private void jbInit()
      throws Throwable {

    //1 tab oben; QP1; lazy loading
    insertTab(
        LPMain.getInstance().getTextRespectUISPr("anf.panel.auswahl"),
        null,
        null,
        LPMain.getInstance().getTextRespectUISPr("anf.panel.auswahl"),
        IDX_PANE_BANK_QP1);

    //2 tab oben; D2; lazy loading
    insertTab(
        LPMain.getInstance().getTextRespectUISPr("lp.detail"),
        null,
        null,
        LPMain.getInstance().getTextRespectUISPr("lp.detail"),
        IDX_PANE_BANK_D2);

    // Defaults.
    //QP1 ist default.
    refreshBankQP1();

    panelBankQP1.eventYouAreSelected(false);
    getBankDto().setPartnerIId(
        (Integer) panelBankQP1.getSelectedId());

    ItemChangedEvent it = new ItemChangedEvent(panelBankQP1,
                                               ItemChangedEvent.ITEM_CHANGED);
    lPEventItemChanged(it);

    addChangeListener(this);
    getInternalFrame().addItemChangedListener(this);

    getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_OHRWASCHLUNTEN,
                              LPMain.getInstance().getTextRespectUISPr("lp.bank"));
  }


  public void lPEventItemChanged(ItemChangedEvent eI)
      throws Throwable {

    if (eI.getID() == ItemChangedEvent.ITEM_CHANGED) {
      if (eI.getSource() == panelBankQP1) {
        Object iId = ( (ISourceEvent) eI.getSource()).getIdSelected();
        iId = panelBankQP1.getSelectedId();
        refreshBankD2( (Integer) panelBankQP1.
                       getSelectedId());
        panelBankD2.setKeyWhenDetailPanel(panelBankQP1.
              getSelectedId());
        getBankDto().setPartnerIId( (Integer) iId);
        getInternalFrame().setKeyWasForLockMe(iId + "");
        if (iId == null) {
          getInternalFrame().enableAllOberePanelsExceptMe(this, IDX_PANE_BANK_QP1, false);
        }
        else {
          getInternalFrame().enableAllOberePanelsExceptMe(this, IDX_PANE_BANK_QP1, true);
        }
      }
    }

    else if ( (eI.getID() == ItemChangedEvent.ACTION_NEW) ||
             (eI.getID() == ItemChangedEvent.ACTION_MY_OWN_NEW)) {
      getBankDto().setPartnerIIdNeuAus(null);
      if (eI.getSource() == panelBankQP1) {
        if (eI.getID() == ItemChangedEvent.ACTION_NEW) {
          refreshBankD2(getBankDto().getPartnerIId());
          setSelectedComponent(panelBankD2);
          panelBankD2.eventActionNew(eI, true, false);
          panelBankD2.eventYouAreSelected(false);
        }
        else {
          // Neu aus Partner.
          dialogPartner(eI);
        }
      }
    }

    else if (eI.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
      if (eI.getSource() == panelBankQP1) {
        setSelectedComponent(panelBankD2);
      }
      else if (eI.getSource() == panelPartnerQPFLR) {
        // Neu aus Partner.
        Integer iIdPartner = (Integer) panelPartnerQPFLR.getSelectedId();
        refreshBankD2(null);
        setBankDto(new BankDto());
        getBankDto().setPartnerIIdNeuAus(iIdPartner);
        panelBankD2.eventActionNew(eI, true, false);
        setSelectedComponent(panelBankD2);
      }
    }

    else if (eI.getID() == ItemChangedEvent.ACTION_DISCARD) {
      if (eI.getSource() == panelBankD2) {
        panelBankD2.eventYouAreSelected(false); // refresh auf das gesamte 1:n panel
      }
    }

    else if (eI.getID() == ItemChangedEvent.ACTION_SAVE) {
      if (eI.getSource() == panelBankD2) {
        Object oKey = panelBankD2.getKeyWhenDetailPanel();
        panelBankQP1.eventYouAreSelected(false);
        panelBankQP1.setSelectedId(oKey);
        panelBankQP1.eventYouAreSelected(false);
      }
    }

    else if (eI.getID() == ItemChangedEvent.ACTION_GOTO_MY_DEFAULT_QP) {
      if (eI.getSource() == panelBankD2) {
        getInternalFrame().setKeyWasForLockMe(panelBankQP1.getSelectedId() + "");
        getPartnerDto().setIId( (Integer) panelBankQP1.getSelectedId());
        setSelectedComponent(panelBankQP1);
        panelBankQP1.eventYouAreSelected(false);
      }
    }
  }


  public void lPEventObjectChanged(ChangeEvent e)
      throws Throwable {
    super.lPEventObjectChanged(e);

    int selectedIndex = this.getSelectedIndex();

    if (selectedIndex == IDX_PANE_BANK_QP1) {
      refreshBankQP1();
      panelBankQP1.eventYouAreSelected(false);

      // emptytable: TP: 1 wenn es fuer das tabbed pane noch keinen Eintrag gibt,
      //   die restlichen oberen Laschen deaktivieren, ausser ...
      if (panelBankQP1.getSelectedId() == null) {
        getInternalFrame().enableAllOberePanelsExceptMe(this, IDX_PANE_BANK_QP1, false);
      }

      //im QP die Buttons setzen.
      panelBankQP1.updateButtons();
      getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_AS_I_LIKE, "");
      getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_OHRWASCHLOBEN, "");
    }

    else if (selectedIndex == IDX_PANE_BANK_D2) {
      refreshBankD2(getBankDto().getPartnerIId());

      try {
        panelBankD2.eventYouAreSelected(false);
      }
      catch (ExceptionLP efc) {
        if (efc.getICode() == EJBExceptionLP.FEHLER_DLG_DONE_DO_NOTHING) {
          panelBankQP1.eventYouAreSelected(false);
          setEnabledAt(IDX_PANE_BANK_QP1, true);
          setSelectedComponent(panelBankQP1);
        }
      }
    }
  }


  protected void lPActionEvent(java.awt.event.ActionEvent e) {
    //nothing here.
  }


  private void refreshBankD2(Integer iIdI)
      throws Throwable {
    if (panelBankD2 == null) {
      panelBankD2 =
          new PanelBank(
              getInternalFrame(),
              LPMain.getInstance().getTextRespectUISPr("lp.detail"),
              iIdI,
              this);
      setComponentAt(IDX_PANE_BANK_D2, panelBankD2);
    }
  }


  private void refreshBankQP1()
      throws Throwable {
    if (panelBankQP1 == null) {
      String[] aWhichStandardButtonIUse = {
          PanelBasis.ACTION_NEW
      };

      QueryType[] querytypes = null;
      FilterKriterium[] filters = null;
      panelBankQP1 = new PanelQuery(
          querytypes,
          filters,
          QueryParameters.UC_ID_BANK,
          aWhichStandardButtonIUse,
          getInternalFrame(),
          LPMain.getInstance().getTextRespectUISPr("anf.panel.auswahl"),
          true);

      setComponentAt(IDX_PANE_BANK_QP1, panelBankQP1);

      // Hier den zusaetzlichen Button aufs Panel bringen
      panelBankQP1.createAndSaveAndShowButton(
          "/com/lp/client/res/businessmen16x16.png",
          LPMain.getInstance().getTextRespectUISPr(
              "part.tooltip.new_bank_from_partner"),
          PanelBasis.ACTION_MY_OWN_NEW + EXTRA_NEU_AUS_PARTNER,null);

      panelBankQP1.befuellePanelFilterkriterienDirekt(
          PartnerFilterFactory.getInstance().createFKDBankName(),
          PartnerFilterFactory.getInstance().createFKDBankBLZ());
    }
  }


  public void setKeyWasForLockMe() {
    Object oKey = panelBankQP1.getSelectedId();

    if (oKey != null) {
      getInternalFrame().setKeyWasForLockMe(oKey.toString());
    }
    else {
      getInternalFrame().setKeyWasForLockMe(null);
    }
  }


  protected javax.swing.JMenuBar getJMenuBar()
      throws Throwable {
    return new WrapperMenuBar(this);
  }


  private void dialogPartner(ItemChangedEvent e)
      throws Throwable {
    panelPartnerQPFLR = PartnerFilterFactory.getInstance().createPanelFLRPartner(
        getInternalFrame());
    new DialogQuery(panelPartnerQPFLR);
  }


  public BankDto getBankDto() {
    return bankDto;
  }


  public void setBankDto(BankDto bankDto) {
    this.bankDto = bankDto;
  }


  public PartnerDto getPartnerDto() {
    return partnerDto;
  }


  public void setPartnerDto(PartnerDto partnerDto) {
    this.partnerDto = partnerDto;
  }


  public PartnerbankDto getPartnerBankDto() {
    return partnerBankDto;
  }


  public void setPartnerBankDto(PartnerbankDto partnerBankDto) {
    this.partnerBankDto = partnerBankDto;
  }


  public Object getDto() {
    return partnerDto;
  }

}
