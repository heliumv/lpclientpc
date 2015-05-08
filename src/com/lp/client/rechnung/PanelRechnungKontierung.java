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
package com.lp.client.rechnung;


import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.math.BigDecimal;
import java.util.EventObject;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import net.miginfocom.swing.MigLayout;

import com.lp.client.frame.Defaults;
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
import com.lp.client.system.SystemFilterFactory;
import com.lp.server.rechnung.service.RechnungDto;
import com.lp.server.rechnung.service.RechnungFac;
import com.lp.server.rechnung.service.RechnungkontierungDto;
import com.lp.server.system.service.KostenstelleDto;

@SuppressWarnings("static-access")
/**
 * <p>Panel zum Bearbeiten der Kontierungen einer ER</p>
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 * <p>Erstellungsdatum <I>15. 03. 2005</I></p>
 * <p> </p>
 * @author  Martin Bluehweis
 * @version $Revision: 1.3 $
 */
public class PanelRechnungKontierung
    extends PanelBasis
{
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private TabbedPaneRechnungAll tabbedPaneRechnung = null;
  private RechnungkontierungDto kontierungDto = null;
  private KostenstelleDto kostenstelleDto = null;
  private final static String ACTION_SPECIAL_KOSTENSTELLE =
      "action_special_erkontierung_kostenstelle";
  private final static String ACTION_SPECIAL_REST = "action_special_erkontierung_rest";
  private PanelQueryFLR panelQueryFLRKostenstelle = null;

  private boolean bIstModulKostenstelleInstalliert = false;

  private WrapperTextField wtfKostenstelleNummer = null;
  private WrapperTextField wtfKostenstelleBezeichnung = null;
  private WrapperButton wbuKostenstelle = null;

  private WrapperNumberField wnfProzentsatz = null;
  private WrapperNumberField wnfProzentsatzOffen = null;
  private JPanel jpaWorkingOn = null;
  private WrapperLabel wlaProzentsatz = null;
  private WrapperLabel wlaProzentsatzOffen = null;
  private WrapperLabel wlaWaehrung1 = null;
  private WrapperButton wbuRest = null;
  private WrapperLabel wlaRechnungsbetrag = null;
  private WrapperNumberField wnfRechnungsbetrag = null;
  private WrapperLabel wlaBisherKontiert = null;
  private WrapperNumberField wnfBisherKontiert = null;
  private WrapperLabel wlaProzent1 = null;
  private WrapperLabel wlaProzent2 = null;
  private WrapperLabel wlaProzent3 = null;
  private WrapperLabel wlaKontierterBetrag = null;
  private WrapperNumberField wnfKontierterBetrag = null;
  private WrapperLabel wlaWaehrung2 = null;

  public PanelRechnungKontierung(InternalFrame internalFrame, String add2TitleI,
                                 Object key, TabbedPaneRechnungAll tabbedPaneRechnung)
      throws Throwable {
    super(internalFrame, add2TitleI);
    this.tabbedPaneRechnung = tabbedPaneRechnung;
    jbInit();
    initComponents();
    initPanel();
  }


  /**
   * initPanel
   *
   * @throws ExceptionForLPClients
   * @throws Throwable
   */
  private void initPanel()
      throws Throwable {
    // rechte
    // kst: ausblenden in initPanel()
    bIstModulKostenstelleInstalliert = true;
    wbuKostenstelle.setVisible(bIstModulKostenstelleInstalliert);
    wtfKostenstelleBezeichnung.setVisible(bIstModulKostenstelleInstalliert);
    wtfKostenstelleNummer.setVisible(bIstModulKostenstelleInstalliert);
    wtfKostenstelleNummer.setMandatoryField(bIstModulKostenstelleInstalliert);
  }


  private TabbedPaneRechnungAll getTabbedPaneRechnung() {
    return tabbedPaneRechnung;
  }


  /**
   * Die Klasse initialisieren.
   *
   * @throws Throwable
   */
  private void jbInit()
      throws Throwable {
    String[] aWhichButtonIUse = {
        ACTION_UPDATE,
        ACTION_SAVE,
        ACTION_DELETE,
        ACTION_DISCARD};
    this.enableToolsPanelButtons(aWhichButtonIUse);
    JPanel panelButtonAction = getToolsPanel();
    // wegen Dialogauswahl auf FLR events hoeren
    getInternalFrame().addItemChangedListener(this);
    this.setLayout(new GridBagLayout());
    wtfKostenstelleNummer = new WrapperTextField();
    wtfKostenstelleBezeichnung = new WrapperTextField();
    wbuKostenstelle = new WrapperButton();
    wnfProzentsatz = new WrapperNumberField();
    wnfProzentsatzOffen = new WrapperNumberField();
    wlaProzentsatz = new WrapperLabel();
    wlaProzentsatzOffen = new WrapperLabel();
    wlaWaehrung1 = new WrapperLabel();
    wbuRest = new WrapperButton();
    wlaRechnungsbetrag = new WrapperLabel();
    wnfRechnungsbetrag = new WrapperNumberField();
    wlaBisherKontiert = new WrapperLabel();
    wnfBisherKontiert = new WrapperNumberField();
    wlaProzent1 = new WrapperLabel("%");
    wlaProzent2 = new WrapperLabel("%");
    wlaProzent3 = new WrapperLabel("%");
    wlaKontierterBetrag = new WrapperLabel();
    wnfKontierterBetrag = new WrapperNumberField();
    wlaWaehrung2 = new WrapperLabel();

    wlaKontierterBetrag.setText(LPMain.getInstance().getTextRespectUISPr("label.betrag"));
    wnfKontierterBetrag.setActivatable(false);
    wtfKostenstelleNummer.setMandatoryField(true);
    wbuKostenstelle.setText(LPMain.getInstance().getTextRespectUISPr(
        "button.kostenstelle"));
    wbuKostenstelle.setToolTipText(LPMain.getInstance().getTextRespectUISPr(
        "button.kostenstelle.tooltip"));
    wbuRest.setText(LPMain.getInstance().getTextRespectUISPr("er.button.rest"));
    wbuRest.setToolTipText(LPMain.getInstance().getTextRespectUISPr(
        "er.button.rest.tooltip"));
    wlaRechnungsbetrag.setText(LPMain.getInstance().getTextRespectUISPr(
        "er.label.rechnungsbetrag"));
    wlaBisherKontiert.setText(LPMain.getInstance().getTextRespectUISPr(
        "er.label.bisherkontiert"));
    wnfProzentsatz.setMandatoryField(true);
    wnfProzentsatz.addFocusListener(new PanelRechnungKontierung_focusAdapter(this));

    wnfProzentsatzOffen.setActivatable(false);

    wlaProzentsatz.setText(LPMain.getInstance().getTextRespectUISPr("label.betrag"));
    wlaProzentsatzOffen.setText(LPMain.getInstance().getTextRespectUISPr("label.offen"));

    wbuKostenstelle.setMinimumSize(new Dimension(120,
                                                 Defaults.getInstance().getControlHeight()));
    wbuKostenstelle.setPreferredSize(new Dimension(120,
        Defaults.getInstance().getControlHeight()));
    wtfKostenstelleNummer.setMinimumSize(new Dimension(100,
        Defaults.getInstance().getControlHeight()));
    wtfKostenstelleNummer.setPreferredSize(new Dimension(100,
        Defaults.getInstance().getControlHeight()));
    wlaWaehrung1.setMinimumSize(new Dimension(40, Defaults.getInstance().getControlHeight()));
    wlaWaehrung1.setPreferredSize(new Dimension(40, Defaults.getInstance().getControlHeight()));
    wlaWaehrung2.setMinimumSize(new Dimension(40, Defaults.getInstance().getControlHeight()));
    wlaWaehrung2.setPreferredSize(new Dimension(40, Defaults.getInstance().getControlHeight()));
    wlaKontierterBetrag.setMinimumSize(new Dimension(60, Defaults.getInstance().getControlHeight()));
    wlaKontierterBetrag.setPreferredSize(new Dimension(60, Defaults.getInstance().getControlHeight()));
    wtfKostenstelleBezeichnung.setActivatable(false);
    wtfKostenstelleNummer.setActivatable(false);
    wnfProzentsatzOffen.setActivatable(false);
    wnfRechnungsbetrag.setActivatable(false);
    wnfBisherKontiert.setActivatable(false);

    wlaWaehrung1.setHorizontalAlignment(SwingConstants.LEFT);
    wlaWaehrung2.setHorizontalAlignment(SwingConstants.LEFT);
    wlaProzent1.setHorizontalAlignment(SwingConstants.LEFT);
    wlaProzent2.setHorizontalAlignment(SwingConstants.LEFT);
    wlaProzent3.setHorizontalAlignment(SwingConstants.LEFT);
    wnfProzentsatz.setMinimumValue(new BigDecimal(0));
    wnfProzentsatz.setMaximumValue(new BigDecimal(100.00));

    wbuKostenstelle.setActionCommand(ACTION_SPECIAL_KOSTENSTELLE);
    wbuRest.setActionCommand(ACTION_SPECIAL_REST);
    wbuKostenstelle.addActionListener(this);
    wbuRest.addActionListener(this);

    jpaWorkingOn = new JPanel(new MigLayout("wrap 6", "[fill,20%][fill,20%][fill,10%][fill,20%][fill,20%][fill,10%]"));
    this.add(panelButtonAction, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    this.add(jpaWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    this.add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    jpaWorkingOn.add(wbuKostenstelle);
    jpaWorkingOn.add(wtfKostenstelleNummer);
    jpaWorkingOn.add(wtfKostenstelleBezeichnung, "span");

    jpaWorkingOn.add(wlaRechnungsbetrag);
    jpaWorkingOn.add(wnfRechnungsbetrag);
    jpaWorkingOn.add(wlaWaehrung1);
    jpaWorkingOn.add(wlaKontierterBetrag);
    jpaWorkingOn.add(wnfKontierterBetrag);
    jpaWorkingOn.add(wlaWaehrung2, "wrap");
    
    jpaWorkingOn.add(wlaBisherKontiert);
    jpaWorkingOn.add(wnfBisherKontiert);
    jpaWorkingOn.add(wlaProzent1, "wrap");
    
    jpaWorkingOn.add(wlaProzentsatz);
    jpaWorkingOn.add(wnfProzentsatz);
    jpaWorkingOn.add(wlaProzent2);
    jpaWorkingOn.add(wbuRest, "span 2, wrap");
    
    jpaWorkingOn.add(wlaProzentsatzOffen);
    jpaWorkingOn.add(wnfProzentsatzOffen);
    jpaWorkingOn.add(wlaProzent3);
  }


  public String getLockMeWer() {
    return HelperClient.LOCKME_RECHNUNG;
  }


  public void eventActionNew(EventObject e, boolean bChangeKeyLockMeI,
                             boolean bNeedNoNewI)
      throws Throwable {
    RechnungDto erDto = getTabbedPaneRechnung().getRechnungDto();
    if (erDto.getStatusCNr().equals(RechnungFac.STATUS_STORNIERT)) {
      boolean answer = (DialogFactory.showMeldung(
          "Die Rechnung ist storniert\nSoll sie wieder verwendet werden?",
          LPMain.getTextRespectUISPr("lp.frage"),
          javax.swing.JOptionPane.YES_NO_OPTION) == javax.swing.JOptionPane.YES_OPTION);
      if (!answer) {
        return;
      }
      DelegateFactory.getInstance().getRechnungDelegate().storniereRechnungRueckgaengig(
          erDto.getIId());
      this.eventYouAreSelected(false);
    }
    super.eventActionNew(e, true, false);
    clearStatusbar();
    kontierungDto = null;
    kostenstelleDto = null;
    leereAlleFelder(this);
  }


  protected void eventActionDelete(ActionEvent e, boolean bAdministrateLockKeyI,
                                   boolean bNeedNoDeleteI)
      throws Throwable {
    if (this.kontierungDto != null) {
      if (kontierungDto.getIId() != null) {
        if (!isLockedDlg()) {
          DelegateFactory.getInstance().getRechnungDelegate().removeRechnungkontierung(kontierungDto);
          this.kontierungDto = null;
          this.leereAlleFelder(this);
          super.eventActionDelete(e, false, false);
        }
      }
    }
  }


  protected void eventActionUpdate(ActionEvent aE, boolean bNeedNoUpdateI)
      throws Throwable {
    RechnungDto erDto = getTabbedPaneRechnung().getRechnungDto();
    if (erDto.getStatusCNr().equals(RechnungFac.STATUS_STORNIERT)) {
      boolean answer = (DialogFactory.showMeldung(
          "Die Eingangsrechnung ist storniert\nSoll sie wieder verwendet werden?",
          "Frage", javax.swing.JOptionPane.YES_NO_OPTION) ==
                        javax.swing.JOptionPane.YES_OPTION);
      if (!answer) {
        return;
      }
      DelegateFactory.getInstance().getRechnungDelegate().
          storniereRechnungRueckgaengig(erDto.getIId());
      this.eventYouAreSelected(false);
    }
    super.eventActionUpdate(aE, bNeedNoUpdateI);
  }


  public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
      throws
      Throwable {
    if (allMandatoryFieldsSetDlg()) {
      components2Dto();
      if (kontierungDto != null) {
        RechnungkontierungDto savedDto = DelegateFactory.getInstance().
            getRechnungDelegate().updateRechnungkontierung(kontierungDto);
        this.kontierungDto = savedDto;
        setKeyWhenDetailPanel(kontierungDto.getIId());
        super.eventActionSave(e, true);
        if (getInternalFrame().getKeyWasForLockMe() == null) {
          getInternalFrame().setKeyWasForLockMe(getTabbedPaneRechnung().getRechnungDto().
                                                getIId().toString());
        }
        // jetz den anzeigen
        eventYouAreSelected(false);
      }
    }
  }


  private void components2Dto()
      throws Throwable {
    if (kontierungDto == null) {
      kontierungDto = new RechnungkontierungDto();
      kontierungDto.setRechnungIId(getTabbedPaneRechnung().getRechnungDto().getIId());
    }
    if (kostenstelleDto != null) {
      kontierungDto.setKostenstelleIId(kostenstelleDto.getIId());
    }
    else {
      kontierungDto.setKostenstelleIId(null);
    }
    kontierungDto.setNProzentsatz(wnfProzentsatz.getBigDecimal());
  }


  private void dto2Components()
      throws Throwable {
    if (getKeyWhenDetailPanel() != null) {
      kontierungDto = DelegateFactory.getInstance().getRechnungDelegate().
          rechnungkontierungFindByPrimaryKey( (Integer) getKeyWhenDetailPanel());
    }
    if (kontierungDto != null) {
      holeKostenstelle(kontierungDto.getKostenstelleIId());
      dto2ComponentsKostenstelle();
      // den maximalwert setzen, denn der ist ja 0, wenn alles zugeordnet ist
      wnfProzentsatz.setBigDecimal(kontierungDto.getNProzentsatz());
      this.setStatusbarPersonalIIdAendern(kontierungDto.getPersonalIIdAendern());
      this.setStatusbarTAendern(kontierungDto.getTAendern());
    }
  }


  /**
   * dto2Components
   */
  private void dto2ComponentsKostenstelle() {
    if (kostenstelleDto != null) {
      wtfKostenstelleNummer.setText(kostenstelleDto.getCNr());
      wtfKostenstelleBezeichnung.setText(kostenstelleDto.getCBez());
    }
    else {
      wtfKostenstelleNummer.setText(null);
      wtfKostenstelleBezeichnung.setText(null);
    }
  }


  protected void eventActionSpecial(ActionEvent e)
      throws Throwable {
    if (e.getActionCommand().equals(ACTION_SPECIAL_KOSTENSTELLE)) {
      dialogQueryKostenstelle(e);
    }
    else if (e.getActionCommand().equals(ACTION_SPECIAL_REST)) {
      BigDecimal bdKontiert = DelegateFactory.getInstance().getRechnungDelegate().getProzentsatzKontiert(
          getTabbedPaneRechnung().getRechnungDto().getIId());
      wnfProzentsatz.setBigDecimal(new BigDecimal(100).subtract(bdKontiert));
      focusLostKontierung(null);
    }
  }


  void dialogQueryKostenstelle(ActionEvent e)
      throws Throwable {
    panelQueryFLRKostenstelle = SystemFilterFactory.getInstance().
        createPanelFLRKostenstelle(getInternalFrame(), false, false);
    if (kostenstelleDto != null) {
      panelQueryFLRKostenstelle.setSelectedId(kostenstelleDto.getIId());
    }
    new DialogQuery(panelQueryFLRKostenstelle);
  }


  protected void eventItemchanged(EventObject eI)
      throws Throwable {
    ItemChangedEvent e = (ItemChangedEvent) eI;
    if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
      if (e.getSource() == panelQueryFLRKostenstelle) {
        Object key = ( (ISourceEvent) e.getSource()).getIdSelected();
        holeKostenstelle( (Integer) key);
      }
    }
  }


  /**
   * holeKostenstelle.
   *
   * @param key Object
   * @throws Throwable
   */
  private void holeKostenstelle(Integer key)
      throws Throwable {
    if (key != null) {
      this.kostenstelleDto = DelegateFactory.getInstance().getSystemDelegate().
          kostenstelleFindByPrimaryKey(key);
      dto2ComponentsKostenstelle();
    }
  }


  public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
      throws Throwable {
    super.eventYouAreSelected(false);
    Object key = getKeyWhenDetailPanel();
    if (key == null || (key != null && key.equals(LPMain.getLockMeForNew()))) {
      // einen neuen Eintrag anlegen oder die letzte Position wurde geloescht.
      this.leereAlleFelder(this);
      wbuRest.doClick();
    }
    else {
      kontierungDto = DelegateFactory.getInstance().getRechnungDelegate().
          rechnungkontierungFindByPrimaryKey( (Integer) key);
      dto2Components();
    }
    if (getTabbedPaneRechnung().getRechnungDto() != null) {
      wlaWaehrung1.setText(getTabbedPaneRechnung().getRechnungDto().getWaehrungCNr());
      wlaWaehrung2.setText(getTabbedPaneRechnung().getRechnungDto().getWaehrungCNr());
      wnfRechnungsbetrag.setBigDecimal(getTabbedPaneRechnung().getRechnungDto().
                                       getNWertfw());
      focusLostKontierung(null);
    }
    BigDecimal bdKontiert = DelegateFactory.getInstance().getRechnungDelegate().getProzentsatzKontiert(
        getTabbedPaneRechnung().getRechnungDto().getIId());
    wnfBisherKontiert.setBigDecimal(bdKontiert);
    wnfProzentsatzOffen.setBigDecimal(new BigDecimal(100).subtract(bdKontiert));
  }


  protected JComponent getFirstFocusableComponent()
      throws Exception {
    return wbuKostenstelle;
  }


  protected void focusLostKontierung(FocusEvent e)
      throws Throwable {
    if (wnfRechnungsbetrag.getBigDecimal()!=null &&
        wnfProzentsatz.getBigDecimal()!=null) {
      BigDecimal bdKontiert = wnfRechnungsbetrag.getBigDecimal().multiply(wnfProzentsatz.
          getBigDecimal()).movePointLeft(2);
      wnfKontierterBetrag.setBigDecimal(bdKontiert);
    }
    else {
      wnfKontierterBetrag.setBigDecimal(null);
    }
  }
}


class PanelRechnungKontierung_focusAdapter
    implements java.awt.event.FocusListener
{
  PanelRechnungKontierung adaptee;

  PanelRechnungKontierung_focusAdapter(PanelRechnungKontierung adaptee) {
    this.adaptee = adaptee;
  }


  public void focusLost(FocusEvent e) {
    try {
      adaptee.focusLostKontierung(e);
    }
    catch (Throwable ex) {
      LPMain.getInstance().exitFrame(adaptee.getInternalFrame());
    }
  }


  public void focusGained(FocusEvent e) {
  }
}

