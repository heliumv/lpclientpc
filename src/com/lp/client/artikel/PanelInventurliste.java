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
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.math.BigDecimal;
import java.util.EventObject;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
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
import com.lp.client.frame.component.WrapperIdentField;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.InventurFac;
import com.lp.server.artikel.service.InventurlisteDto;
import com.lp.server.artikel.service.LagerDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

@SuppressWarnings("static-access")
public class PanelInventurliste
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
  private WrapperIdentField wifArtikel = null;
  private InventurlisteDto inventurlisteDto = null;
  private WrapperButton wbuLager = new WrapperButton();
  private WrapperTextField wtfLager = new WrapperTextField();
  private WrapperTextField wtfSeriennummer = new WrapperTextField();

  private WrapperLabel wlaInventurmenge = new WrapperLabel();
  private WrapperLabel wlaSeriennummer = new WrapperLabel();

  private WrapperLabel wlaVoraussichtlicherInventurstand = new WrapperLabel();

  private WrapperLabel wlaEinheitMenge = new WrapperLabel();

  private WrapperNumberField wnfInventurmenge = new WrapperNumberField();

  Integer lastLager = null;

  static final public String ACTION_SPECIAL_LAGER_FROM_LISTE =
      "action_lager_from_liste";

  private PanelQueryFLR panelQueryFLRLager = null;

  public PanelInventurliste(InternalFrame internalFrame, String add2TitleI,
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
    return wifArtikel.getWtfIdent();
  }


  protected void setDefaults() {

  }


  protected void eventActionUpdate(ActionEvent aE, boolean bNeedNoUpdateI)
	throws Throwable {
	  
	  if (!Helper.short2boolean(internalFrameArtikel.getInventurDto().
              getBInventurdurchgefuehrt())) {
		  super.eventActionUpdate(aE, bNeedNoUpdateI);
	  } else {
		  DialogFactory.showModalDialog(LPMain.getInstance().getTextRespectUISPr(
	        "lp.error"),LPMain.getInstance().getTextRespectUISPr(
	        "artikel.error.inventur.bereitsdurchgefuehrt"));
		  return;
	  }
	  
	 
  }
  
  public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
      throws Throwable {

    if (Helper.short2boolean(internalFrameArtikel.getInventurDto().
                             getBInventurdurchgefuehrt())) {
      wnfInventurmenge.setActivatable(true);
    }
    else {
      wnfInventurmenge.setActivatable(true);
    }

    super.eventYouAreSelected(false);
    Object key = getKeyWhenDetailPanel();

    if (key == null
        || (key.equals(LPMain.getLockMeForNew()))) {

      leereAlleFelder(this);
      clearStatusbar();
      wlaVoraussichtlicherInventurstand.setText(null);
      if (lastLager != null) {
        inventurlisteDto.setLagerIId(lastLager);
        wtfLager.setText(DelegateFactory.getInstance().getLagerDelegate().
                         lagerFindByPrimaryKey(
                             lastLager).getCNr());
      }
      else {
        ParametermandantDto parameter =
            DelegateFactory.getInstance().getParameterDelegate().
            getMandantparameter(LPMain.getInstance().getTheClient().getMandant(),
                                ParameterFac.KATEGORIE_ARTIKEL,
                                ParameterFac.PARAMETER_DEFAULT_LAGER);
        LagerDto lagerDto = DelegateFactory.getInstance().getLagerDelegate().
            lagerFindByPrimaryKeyOhneExc(new Integer(parameter.getCWert()));
        
        if(lagerDto==null){
        	//SP740
        	DialogFactory.showModalDialog(LPMain.getInstance().getTextRespectUISPr(
	        "lp.error"), LPMain.getInstance().getTextRespectUISPr(
	        "artikel.inventur.parameter.defaultlager.falsch"));
        }
        
        if (key != null && lagerDto != null &&
            lagerDto.getMandantCNr().equals(LPMain.getInstance().getTheClient().
                                            getMandant())) {
          wtfLager.setText(lagerDto.getCNr());
          inventurlisteDto.setLagerIId(lagerDto.getIId());
        }

      }
      wtfSeriennummer.setEditable(true);
      wbuLager.setEnabled(true);
      wifArtikel.getWbuArtikel().setEnabled(true);
      wifArtikel.getWtfIdent().setEditable(true);
      wifArtikel.getWtfIdent().requestFocus();
    }
    else {
      inventurlisteDto = DelegateFactory.getInstance().getInventurDelegate().
          inventurlisteFindByPrimaryKey( (Integer) key);

      if (Helper.short2boolean(inventurlisteDto.getArtikelDto().getBSeriennrtragend()) ||
          Helper.
          short2boolean(inventurlisteDto.getArtikelDto().
                        getBChargennrtragend()
          )) {
        wtfSeriennummer.setMandatoryField(true);
        wtfSeriennummer.setEditable(true);
      }

      else {
        wtfSeriennummer.setMandatoryField(false);
        wtfSeriennummer.setText(null);
      }
      dto2Components();

      BigDecimal inventurstand=DelegateFactory.getInstance().getInventurDelegate().getInventurstand(inventurlisteDto.getArtikelIId(),inventurlisteDto.getLagerIId(),internalFrameArtikel.getInventurDto().getIId(),internalFrameArtikel.getInventurDto().getTInventurdatum());

      wlaVoraussichtlicherInventurstand.setText(LPMain.getInstance().getTextRespectUISPr(
        "artikel.inventur.voraussichtlicherinventurstand")+" "+Helper.formatZahl(inventurstand,2,LPMain.getInstance().getTheClient().getLocUi()));


    }
  }


  public void eventActionNew(EventObject eventObject, boolean bLockMeI,
                             boolean bNeedNoNewI)
      throws Throwable {
    if (!Helper.short2boolean(internalFrameArtikel.getInventurDto().
                              getBInventurdurchgefuehrt())) {
      wnfInventurmenge.setActivatable(true);

      wifArtikel.getWbuArtikel().setActivatable(true);
      wbuLager.setActivatable(true);
      //wtfSeriennummer.setActivatable(true);

      super.eventActionNew(eventObject, true, false);
      leereAlleFelder(this);
      inventurlisteDto = new InventurlisteDto();

    }
    else {
      DialogFactory.showModalDialog(LPMain.getInstance().getTextRespectUISPr(
        "lp.error"),LPMain.getInstance().getTextRespectUISPr(
        "artikel.error.inventur.bereitsdurchgefuehrt"));
    }
  }


  protected void dto2Components()
      throws Throwable {

    wtfLager.setText(inventurlisteDto.getLagerDto().getCNr());
    wnfInventurmenge.setBigDecimal(inventurlisteDto.getNInventurmenge());
    wtfSeriennummer.setText(inventurlisteDto.getCSeriennrchargennr());

    ArtikelDto artikelTempDto = DelegateFactory.getInstance().getArtikelDelegate().
        artikelFindByPrimaryKey(inventurlisteDto.getArtikelIId());
    wifArtikel.setArtikelDto(artikelTempDto);
    wlaEinheitMenge.setText(artikelTempDto.getEinheitCNr().trim());
    inventurlisteDto.setArtikelIId(artikelTempDto.getIId());

	this.setStatusbarPersonalIIdAnlegen(inventurlisteDto.getPersonalIIdAendern());
	this.setStatusbarTAnlegen(inventurlisteDto.getTAendern());
    
  }


  void dialogQueryLagerFromListe(ActionEvent e)
      throws Throwable {
    panelQueryFLRLager = ArtikelFilterFactory.getInstance().createPanelFLRLager(
        getInternalFrame(), inventurlisteDto.getLagerIId());

    new DialogQuery(panelQueryFLRLager);
  }


  protected void components2Dto()
      throws ExceptionLP {
    inventurlisteDto.setNInventurmenge(wnfInventurmenge.getBigDecimal());
    inventurlisteDto.setCSeriennrchargennr(wtfSeriennummer.getText());
    inventurlisteDto.setArtikelIId(wifArtikel.getArtikelDto().getIId());
  }


  protected void eventItemchanged(EventObject eI)
      throws Throwable {
    ItemChangedEvent e = (ItemChangedEvent) eI;
    if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {

      if (e.getSource() == wifArtikel) {

        ArtikelDto artikelTempDto = wifArtikel.getArtikelDto();
        wifArtikel.setArtikelDto(artikelTempDto);
        wlaEinheitMenge.setText(artikelTempDto.getEinheitCNr().trim());
        inventurlisteDto.setArtikelIId(artikelTempDto.getIId());

        if (Helper.short2boolean(artikelTempDto.getBSeriennrtragend())) {
          wtfSeriennummer.setMandatoryField(true);
          wtfSeriennummer.setEditable(true);
        }
        else if (Helper.short2boolean(artikelTempDto.getBChargennrtragend())) {
          wtfSeriennummer.setMandatoryField(true);
          wtfSeriennummer.setEditable(true);
        }
        else {
          wtfSeriennummer.setMandatoryField(false);
          wtfSeriennummer.setEditable(false);
        }
      }
      else if (e.getSource() == panelQueryFLRLager) {
        Object key = ( (ISourceEvent) e.getSource()).getIdSelected();
        LagerDto lagerDto = DelegateFactory.getInstance().getLagerDelegate().
            lagerFindByPrimaryKey( (Integer)
                                  key);
        wtfLager.setText(lagerDto.getCNr());
        inventurlisteDto.setLagerIId(lagerDto.getIId());
      }
    }
    else if (e.getID() == ItemChangedEvent.ITEM_CHANGED) {
      if (inventurlisteDto != null && inventurlisteDto.getArtikelDto() != null) {

        if (Helper.short2boolean(inventurlisteDto.getArtikelDto().
                                 getBSeriennrtragend()) ||
            Helper.
            short2boolean(inventurlisteDto.getArtikelDto().
                          getBChargennrtragend())) {
          wtfSeriennummer.setMandatoryField(true);
          wtfSeriennummer.setEditable(false);
        }
        else {
          wtfSeriennummer.setMandatoryField(false);
          wtfSeriennummer.setEditable(false);
          wtfSeriennummer.setText(null);
        }
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

    wifArtikel = new WrapperIdentField(getInternalFrame(), this);

    wtfSeriennummer.setColumnsMax(InventurFac.MAX_INVENTURLISTE_SERIENNRCHARGENNR);
    wtfSeriennummer.setActivatable(false);

    getInternalFrame().addItemChangedListener(this);

    wnfInventurmenge.setMandatoryField(true);
    wnfInventurmenge.addFocusListener(new
                                      PanelInventurliste_wnfInventurmenge_focusAdapter(this));

    
	int iNachkommastellen = Defaults.getInstance()
	.getIUINachkommastellenMenge();
	wnfInventurmenge.setFractionDigits(iNachkommastellen);
    
    wlaInventurmenge.setText(LPMain.getInstance().getTextRespectUISPr(
        "artikel.inventurmenge"));
    wlaSeriennummer.setText(LPMain.getInstance().getTextRespectUISPr(
        "artikel.handlagerbewegung.seriennrchargennreinzeln"));

    wbuLager.setText(LPMain.getInstance().getTextRespectUISPr("button.lager"));
    wbuLager.setActionCommand(PanelInventurliste.
                              ACTION_SPECIAL_LAGER_FROM_LISTE);
    wbuLager.addActionListener(this);


    wbuLager.setActivatable(false);
    wifArtikel.getWbuArtikel().setActivatable(false);
    wifArtikel.getWtfIdent().setActivatable(false);

    wtfLager.setMandatoryField(true);
    wtfLager.setActivatable(false);
    wlaEinheitMenge.setHorizontalAlignment(SwingConstants.LEFT);

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

    jpaWorkingOn.add(wifArtikel.getWbuArtikel(),
                     new GridBagConstraints(0, 0, 1, 1, 1, 0.0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.HORIZONTAL,
                                            new Insets(2, 2, 2, 2), 0, 0));

    wifArtikel.getWtfIdent().setUppercaseField(true);
    jpaWorkingOn.add(wifArtikel.getWtfIdent(),
                     new GridBagConstraints(1, 0, 1, 1, 1, 0.0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.HORIZONTAL,
                                            new Insets(2, 2, 2, 2), 0, 0));

    jpaWorkingOn.add(wifArtikel.getWtfBezeichnung(),
                     new GridBagConstraints(2, 0, 1, 1, 2, 0.0,
                                            GridBagConstraints.CENTER,
                                            GridBagConstraints.BOTH,
                                            new Insets(2, 2, 2, 2), 0, 0));

    jpaWorkingOn.add(wlaSeriennummer,
                     new GridBagConstraints(0, 1, 1, 1, 0, 0.0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.HORIZONTAL,
                                            new Insets(2, 2, 2, 2), 0, 0));
    jpaWorkingOn.add(wtfSeriennummer,
                     new GridBagConstraints(1, 1, 2, 1, 0, 0.0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.HORIZONTAL,
                                            new Insets(2, 2, 2, 2), 0, 0));

    jpaWorkingOn.add(wbuLager,
                     new GridBagConstraints(0, 2, 1, 1, 0, 0.0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.HORIZONTAL,
                                            new Insets(2, 2, 2, 2), 0, 0));
    jpaWorkingOn.add(wtfLager,
                     new GridBagConstraints(1, 2, 2, 1, 0, 0.0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.HORIZONTAL,
                                            new Insets(2, 2, 2, 2), 0, 0));
    jpaWorkingOn.add(wlaInventurmenge,
                     new GridBagConstraints(0, 3, 1, 1, 0, 0.0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.HORIZONTAL,
                                            new Insets(2, 2, 2, 2), 0, 0));
    jpaWorkingOn.add(wnfInventurmenge,
                     new GridBagConstraints(1, 3, 1, 1, 0, 0.0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.HORIZONTAL,
                                            new Insets(2, 2, 2, 2), 0, 0));
    jpaWorkingOn.add(wlaEinheitMenge, new GridBagConstraints(2, 3, 1, 1, 0.0, 0.0
        , GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0),
        20, 0));
    jpaWorkingOn.add(wlaVoraussichtlicherInventurstand,
                       new GridBagConstraints(1, 4, 1, 1, 0, 0.0
                                              , GridBagConstraints.CENTER,
                                              GridBagConstraints.HORIZONTAL,
                                            new Insets(2, 2, 2, 2), 0, 0));
    String[] aWhichButtonIUse = {
        ACTION_UPDATE,
        ACTION_SAVE,
        ACTION_DISCARD,
    };

    enableToolsPanelButtons(aWhichButtonIUse);

  }


  protected String getLockMeWer()
      throws Exception {
    return HelperClient.LOCKME_INVENTUR;
  }


  protected void eventActionDelete(ActionEvent e, boolean bAdministrateLockKeyI,
                                   boolean bNeedNoDeleteI)
      throws Throwable, Throwable {
    if (!Helper.short2boolean(internalFrameArtikel.getInventurDto().
                              getBInventurdurchgefuehrt())) {
      DelegateFactory.getInstance().getInventurDelegate().removeInventurliste(
          inventurlisteDto);
      this.setKeyWhenDetailPanel(null);
      super.eventActionDelete(e, false, false);
    }
    else {
      DialogFactory.showModalDialog("Fehler",
                                    "Inventur wurde bereits durchgef\u00FChrt");
    }

  }


  protected void eventActionSpecial(ActionEvent e)
      throws Throwable {
    if (e.getActionCommand().equals(ACTION_SPECIAL_LAGER_FROM_LISTE)) {
      dialogQueryLagerFromListe(e);
    }
  }


  public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
      throws Throwable {
    if (allMandatoryFieldsSetDlg()) {
      components2Dto();
      java.math.BigDecimal mengeAusInventurliste = inventurlisteDto.getNInventurmenge();
      java.math.BigDecimal lagerstandVeraenderung = DelegateFactory.getInstance().
          getLagerDelegate().
          getLagerstandsVeraenderungOhneInventurbuchungen(
              inventurlisteDto.getArtikelIId(),
              inventurlisteDto.getLagerIId(),
              internalFrameArtikel.getInventurDto().getTInventurdatum(),
              new java.sql.Timestamp(System.currentTimeMillis()));

      if (mengeAusInventurliste.subtract(lagerstandVeraenderung).doubleValue() < 0) {
        DialogFactory.showModalDialog(LPMain.getInstance().getTextRespectUISPr(
            "lp.error"),
                                      "Aufgrund der Artikelbewegungen zwischen Inventurdatum (" +
                                      Helper.formatDatum(internalFrameArtikel.
            getInventurDto().getTInventurdatum(),
            LPMain.getInstance().getTheClient().getLocUi()) +
                                      ") und jetzt, w\u00FCrde sich ein Inventurstand von " +
                                      mengeAusInventurliste.
                                      subtract(lagerstandVeraenderung).doubleValue() +
                                      " ergeben. Bitte korrigieren! Buchung wurde nicht durchgef\u00FChrt.");
      }
      else {

        if (inventurlisteDto.getIId() == null) {
        	
          inventurlisteDto.setInventurIId(internalFrameArtikel.getInventurDto().getIId());
          inventurlisteDto.setIId(DelegateFactory.getInstance().getInventurDelegate().
                                  createInventurliste(
                                      inventurlisteDto));
          
    	
          
          setKeyWhenDetailPanel(inventurlisteDto.getIId());
        }
        else {
          Integer key_neu=DelegateFactory.getInstance().getInventurDelegate().updateInventurliste(
              inventurlisteDto);
          inventurlisteDto.setIId(key_neu);
          setKeyWhenDetailPanel(inventurlisteDto.getIId());
        }
        lastLager = inventurlisteDto.getLagerIId();
        super.eventActionSave(e, true);
        if (getInternalFrame().getKeyWasForLockMe() == null) {
          getInternalFrame().setKeyWasForLockMe(internalFrameArtikel.
                                                getArtikelDto().getIId().
                                                toString());
        }
        eventYouAreSelected(false);
        wtfSeriennummer.setEditable(false);
      }
    }
  }


  protected void eventActionDiscard(ActionEvent e)
      throws Throwable {
    super.eventActionDiscard(e);
    wtfSeriennummer.setEditable(false);

  }


  public void wnfInventurmenge_focusLost(FocusEvent e) {

  }

}


class PanelInventurliste_wnfInventurmenge_focusAdapter
    extends FocusAdapter
{
  private PanelInventurliste adaptee;
  PanelInventurliste_wnfInventurmenge_focusAdapter(PanelInventurliste adaptee) {
    this.adaptee = adaptee;
  }


  public void focusLost(FocusEvent e) {
    adaptee.wnfInventurmenge_focusLost(e);
  }
}
