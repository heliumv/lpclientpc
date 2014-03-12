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
package com.lp.client.fertigung;


import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.EventObject;
import java.util.LinkedHashMap;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import com.lp.client.artikel.ArtikelFilterFactory;
import com.lp.client.artikel.PanelHandlagerbewegung;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperComboBox;
import com.lp.client.frame.component.WrapperDateField;
import com.lp.client.frame.component.WrapperGotoButton;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.stueckliste.StuecklisteFilterFactory;
import com.lp.client.system.SystemFilterFactory;
import com.lp.server.artikel.service.InventurDto;
import com.lp.server.artikel.service.LagerDto;
import com.lp.server.fertigung.service.FertigungFac;
import com.lp.server.fertigung.service.WiederholendeloseDto;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.stueckliste.service.FertigungsgruppeDto;
import com.lp.server.stueckliste.service.StuecklisteDto;
import com.lp.server.system.service.KostenstelleDto;
import com.lp.server.system.service.MandantDto;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.util.Helper;

@SuppressWarnings("static-access") 
public class PanelWiederholendelose
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
  private InternalFrameFertigung internalFrameFertigung = null;
  private WrapperLabel wlaProjekt = new WrapperLabel();
  private WrapperLabel wlaAb = new WrapperLabel();
  private WrapperTextField wtfProjekt = new WrapperTextField();
  private WrapperDateField wdfAb = new WrapperDateField();
  private WrapperButton wbuZielager = new WrapperButton();
  private WrapperTextField wtfZiellager = new WrapperTextField();
  private PanelQueryFLR panelQueryFLRLager = null;

  private WrapperLabel wlaWiederholungsintervall = null;
  private WrapperComboBox wcoWiederholungsintervall = null;

  private WrapperCheckBox wcbVersteckt = new WrapperCheckBox();

  static final public String ACTION_SPECIAL_LAGER_FROM_LISTE =
      "action_lager_from_liste";

  private static final String ACTION_SPECIAL_STUECKLISTE =
      "action_special_los_stueckliste";
  private static final String ACTION_SPECIAL_FERTIGUNGSORT =
      "action_special_los_fertigungsort";
  private static final String ACTION_SPECIAL_LOSART =
      "action_special_los_losart";
  static final public String ACTION_SPECIAL_FERTIGUNGSGRUPPE_FROM_LISTE =
      "ACTION_SPECIAL_FERTIGUNGSGRUPPE_FROM_LISTE";
  private static final String ACTION_SPECIAL_KOSTENSTELLE =
      "action_special_los_kostenstelle";

  private PanelQueryFLR panelQueryFLRStueckliste = null;
  private PanelQueryFLR panelQueryFLRKostenstelle = null;
  private PanelQueryFLR panelQueryFLRFertigungsort = null;
  private PanelQueryFLR panelQueryFLRFertigungsgruppe = null;
  private WrapperButton wbuKostenstelle = new WrapperButton();
  private WrapperGotoButton wbuStueckliste = new WrapperGotoButton(WrapperGotoButton.
      GOTO_STUECKLISTE_AUSWAHL);
  private WrapperLabel wlaTage = new WrapperLabel();
  private WrapperTextField wtfStuecklisteNummer = new WrapperTextField();
  private WrapperButton wbuFertigungsort = new WrapperButton();
  private WrapperTextField wtfFertigungsort = new WrapperTextField();

  private WrapperLabel wlaLosgroesse = new WrapperLabel();
  private WrapperNumberField wnfLosgroesse = null;
  private WrapperTextField wtfKostenstelleNummer = new WrapperTextField();

  private WrapperLabel wlaVorauseilend = new WrapperLabel();
  private WrapperNumberField wnfVorauseilend = new WrapperNumberField();

  private WrapperLabel wlaSort = new WrapperLabel();
  private WrapperNumberField wnfSort = new WrapperNumberField();


  private WrapperLabel wlaLosart = new WrapperLabel();
  private WrapperComboBox wcoLosart = new WrapperComboBox();

  private WrapperButton wbuFertigungsgruppe = new WrapperButton();
  private WrapperTextField wtfFertigungsgruppe = new WrapperTextField();

  public PanelWiederholendelose(InternalFrame internalFrame, String add2TitleI,
                                Object pk)
      throws Throwable {
    super(internalFrame, add2TitleI, pk);
    internalFrameFertigung = (InternalFrameFertigung) internalFrame;
    jbInit();
    setDefaults();
    initComponents();
    enableAllComponents(this, false);
  }


  protected JComponent getFirstFocusableComponent()
      throws Exception {
    return wtfProjekt;
  }


  protected void setDefaults()
      throws Throwable {
    wcoWiederholungsintervall.setMap(DelegateFactory.getInstance().
                                     getAuftragServiceDelegate().
                                     getAuftragwiederholungsintervall(LPMain.getInstance().
        getUISprLocale()));

    LinkedHashMap<String, String> m = new LinkedHashMap<String, String> ();
    m.put(FertigungFac.LOSART_IDENT,
          LPMain.getInstance().getTextRespectUISPr("label.ident"));
    m.put(FertigungFac.LOSART_MATERIALLISTE,
          LPMain.getInstance().getTextRespectUISPr("label.materialliste"));
    wcoLosart.setMap(m);

  }


  private void dialogQueryFertigungsgruppeFromListe(ActionEvent e)
      throws Throwable {
    panelQueryFLRFertigungsgruppe = StuecklisteFilterFactory.getInstance().
        createPanelFLRFertigungsgruppe(
            getInternalFrame(), null, false);
    new DialogQuery(panelQueryFLRFertigungsgruppe);
  }


  private void dialogQueryKostenstelle(ActionEvent e)
      throws Throwable {
    panelQueryFLRKostenstelle = SystemFilterFactory.getInstance().
        createPanelFLRKostenstelle(getInternalFrame(), false, false);
    panelQueryFLRKostenstelle.setSelectedId(internalFrameFertigung.
                                            getWiederholendeloseDto().getKostenstelleIId());
    new DialogQuery(panelQueryFLRKostenstelle);
  }


  public void eventActionNew(EventObject eventObject, boolean bLockMeI,
                             boolean bNeedNoNewI)
      throws Throwable {
    super.eventActionNew(eventObject, true, false);
    internalFrameFertigung.setWiederholendeloseDto(new WiederholendeloseDto());
    leereAlleFelder(this);
  }


  private void dialogQueryFertigungsort(ActionEvent e)
      throws Throwable {
    String[] aWhichButtonIUse = {
        PanelBasis.ACTION_REFRESH};

    panelQueryFLRFertigungsort = new PanelQueryFLR(
        null,
        null,
        QueryParameters.UC_ID_MANDANT,
        aWhichButtonIUse,
        getInternalFrame(),
        LPMain.getInstance().getTextRespectUISPr("title.mandantauswahlliste"));
    if (internalFrameFertigung.getWiederholendeloseDto().getPartnerIIdFertigungsort() != null) {
      MandantDto[] mandantDto = DelegateFactory.getInstance().getMandantDelegate().
          mandantFindByPartnerIId(internalFrameFertigung.getWiederholendeloseDto().
                                  getPartnerIIdFertigungsort());
      panelQueryFLRFertigungsort.setSelectedId(mandantDto[0].getCNr());
    }
    new DialogQuery(panelQueryFLRFertigungsort);
  }


  private void dialogQueryStueckliste(ActionEvent e)
      throws Throwable {
    panelQueryFLRStueckliste = StuecklisteFilterFactory.getInstance().
        createPanelFLRStueckliste(getInternalFrame(),
                                  internalFrameFertigung.getWiederholendeloseDto().
                                  getStuecklisteIId(), false);
    new DialogQuery(panelQueryFLRStueckliste);
  }


  protected void eventActionDelete(ActionEvent e, boolean bAdministrateLockKeyI,
                                   boolean bNeedNoDeleteI)
      throws Throwable {
    DelegateFactory.getInstance().getFertigungDelegate().removeWiederholendelose(
        internalFrameFertigung.
        getWiederholendeloseDto());
    super.eventActionDelete(e, false, false);
  }


  public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
      throws Throwable {

    super.eventYouAreSelected(false);
    Object key = null;

    if (internalFrameFertigung.getWiederholendeloseDto() != null) {
      key = internalFrameFertigung.getWiederholendeloseDto().getIId();
    }

    if (key == null
        || (key.equals(LPMain.getLockMeForNew()))) {
      leereAlleFelder(this);
      clearStatusbar();
      wnfVorauseilend.setInteger(5);
      wcbVersteckt.setSelected(true);
    }
    else {
      internalFrameFertigung.setWiederholendeloseDto(DelegateFactory.getInstance().
          getFertigungDelegate().
          wiederholendeloseFindByPrimaryKey(internalFrameFertigung.
                                            getWiederholendeloseDto().getIId()));
      dto2Components();
      getInternalFrame().setLpTitle(
          InternalFrame.TITLE_IDX_AS_I_LIKE,
          LPMain.getInstance().getTextRespectUISPr("fert.wiederholendelose") + ": " +
          internalFrameFertigung.getWiederholendeloseDto().getCProjekt());
    }
  }


  protected void eventItemchanged(EventObject eI)
      throws Throwable {

    ItemChangedEvent e = (ItemChangedEvent) eI;
    if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
      if (e.getSource() == panelQueryFLRLager) {
        Object key = ( (ISourceEvent) e.getSource()).getIdSelected();
        LagerDto lagerDto = DelegateFactory.getInstance().getLagerDelegate().
            lagerFindByPrimaryKey( (Integer)
                                  key);
        wtfZiellager.setText(lagerDto.getCNr());
        internalFrameFertigung.getWiederholendeloseDto().setLagerIIdZiel(lagerDto.getIId());
      }
      else if (e.getSource() == panelQueryFLRKostenstelle) {
        Object key = ( (ISourceEvent) e.getSource()).getIdSelected();
        KostenstelleDto kostenstelleDto = DelegateFactory.getInstance().getSystemDelegate().
            kostenstelleFindByPrimaryKey( (Integer) key);
        wtfKostenstelleNummer.setText(kostenstelleDto.formatKostenstellenbezeichnung());
        internalFrameFertigung.getWiederholendeloseDto().setKostenstelleIId(
            kostenstelleDto.getIId());
      }
      else if (e.getSource() == panelQueryFLRStueckliste) {
        Integer key = (Integer) ( (ISourceEvent) e.getSource()).
            getIdSelected();

        StuecklisteDto stuecklisteDto = DelegateFactory.getInstance().
            getStuecklisteDelegate().
            stuecklisteFindByPrimaryKey( (Integer) key);

        wtfStuecklisteNummer.setText(stuecklisteDto.getArtikelDto().
                                     formatArtikelbezeichnung());
        internalFrameFertigung.getWiederholendeloseDto().setStuecklisteIId(stuecklisteDto.
            getIId());

      }
      else if (e.getSource() == panelQueryFLRFertigungsort) {
        String key = (String) ( (ISourceEvent) e.getSource()).
            getIdSelected();

        MandantDto mandantDto = DelegateFactory.getInstance().getMandantDelegate().
            mandantFindByPrimaryKey( (String) key);

        PartnerDto partnerDtoFertigungsort = DelegateFactory.getInstance().
            getPartnerDelegate().
            partnerFindByPrimaryKey(mandantDto.getPartnerIId());
        wtfFertigungsort.setText(partnerDtoFertigungsort.formatTitelAnrede());
        internalFrameFertigung.getWiederholendeloseDto().setPartnerIIdFertigungsort(
            partnerDtoFertigungsort.getIId());

      }
      else if (e.getSource() == panelQueryFLRFertigungsgruppe) {
        Integer key = (Integer) ( (ISourceEvent) e.getSource()).
            getIdSelected();
        FertigungsgruppeDto fertigungsgruppeDto = DelegateFactory.getInstance().
            getStuecklisteDelegate().
            fertigungsgruppeFindByPrimaryKey(key);
        wtfFertigungsgruppe.setText(fertigungsgruppeDto.getCBez());
        internalFrameFertigung.getWiederholendeloseDto().setFertigungsgruppeIId(
            fertigungsgruppeDto.getIId());

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
    getInternalFrame().addItemChangedListener(this);
    //Actionpanel von Oberklasse holen und anhaengen.
    jpaButtonAction = getToolsPanel();
    this.setActionMap(null);
    wlaTage.setText(LPMain.getInstance().getTextRespectUISPr("lp.tage"));
    wlaTage.setHorizontalAlignment(SwingConstants.LEFT);
    wlaProjekt.setText(LPMain.getInstance().getTextRespectUISPr("label.projekt"));
    wlaAb.setText(LPMain.getInstance().getTextRespectUISPr(
        "lp.ab"));
    wdfAb.setMandatoryField(true);

    wlaLosgroesse.setText(LPMain.getInstance().getTextRespectUISPr("label.losgroesse"));
    wlaLosart.setText(LPMain.getInstance().getTextRespectUISPr("lp.art"));
    wbuKostenstelle.setText(LPMain.getInstance().getTextRespectUISPr(
        "button.kostenstelle"));
    wbuFertigungsort.setText(LPMain.getInstance().getTextRespectUISPr(
        "button.fertigungsort"));
    wbuStueckliste.setText(LPMain.getInstance().getTextRespectUISPr("button.stueckliste"));
    wtfFertigungsgruppe.setMandatoryField(true);
    wtfKostenstelleNummer.setActivatable(false);
    wtfKostenstelleNummer.setMandatoryField(true);

    wlaSort.setText(LPMain.getInstance().getTextRespectUISPr("label.sortierung"));

    wlaVorauseilend.setText(LPMain.getInstance().getTextRespectUISPr("fert.wiederholendelose.vorauseilend"));


    wcoLosart.addActionListener(this);
    wcoLosart.setActionCommand(ACTION_SPECIAL_LOSART);
    wbuFertigungsort.addActionListener(this);
    wbuFertigungsort.setActionCommand(ACTION_SPECIAL_FERTIGUNGSORT);
    wbuStueckliste.addActionListener(this);
    wbuStueckliste.setActionCommand(ACTION_SPECIAL_STUECKLISTE);
    wbuKostenstelle.addActionListener(this);
    wnfLosgroesse = new WrapperNumberField();
    wnfLosgroesse.setMinimumValue(1);
    wnfLosgroesse.setFractionDigits(0);

    wnfSort.setFractionDigits(0);

    wnfVorauseilend.setMinimumValue(0);
    wnfVorauseilend.setFractionDigits(0);
    wnfVorauseilend.setMandatoryField(true);
    wtfZiellager.setMandatoryField(true);

    wnfLosgroesse.setMandatoryFieldDB(true);
    wcoLosart.setMandatoryFieldDB(true);
    wtfFertigungsort.setMandatoryFieldDB(true);

    wbuKostenstelle.setActionCommand(ACTION_SPECIAL_KOSTENSTELLE);
    wbuFertigungsgruppe.setText(LPMain.getInstance().getTextRespectUISPr(
        "stkl.fertigungsgruppe") + "...");
    wbuFertigungsgruppe.setActionCommand(this.
                                         ACTION_SPECIAL_FERTIGUNGSGRUPPE_FROM_LISTE);
    wbuFertigungsgruppe.addActionListener(this);
    wtfFertigungsgruppe.setActivatable(false);
    wlaWiederholungsintervall = new WrapperLabel(LPMain.getInstance().getTextRespectUISPr(
        "lp.wiederholungsintervall"));
    wcoWiederholungsintervall = new WrapperComboBox();
    wcoWiederholungsintervall.setMandatoryField(true);
    wbuZielager.setText(LPMain.getInstance().getTextRespectUISPr("button.ziellager"));
    wbuZielager.setActionCommand(PanelHandlagerbewegung.
                                 ACTION_SPECIAL_LAGER_FROM_LISTE);
    wbuZielager.addActionListener(this);

    wtfZiellager.setActivatable(false);

    getInternalFrame().addItemChangedListener(this);

    wcbVersteckt.setText(LPMain.getInstance().getTextRespectUISPr(
        "lp.aktiv"));
    wtfFertigungsort.setActivatable(false);
    wtfStuecklisteNummer.setActivatable(false);

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

    iZeile = 0;
    jpaWorkingOn.add(wlaLosart,
                     new GridBagConstraints(0, iZeile, 1, 1, 0, 0.0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.HORIZONTAL,
                                            new Insets(2, 2, 2, 2), 0, 0));
    jpaWorkingOn.add(wcoLosart,
                     new GridBagConstraints(1, iZeile, 3, 1, 0, 0.0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.HORIZONTAL,
                                            new Insets(2, 2, 2, 2), 0, 0));

    iZeile++;

    jpaWorkingOn.add(wbuKostenstelle,
                     new GridBagConstraints(0, iZeile, 1, 1, 0, 0.0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.HORIZONTAL,
                                            new Insets(2, 2, 2, 2), 0, 0));
    jpaWorkingOn.add(wtfKostenstelleNummer,
                     new GridBagConstraints(1, iZeile, 1, 1, 0, 0.0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.HORIZONTAL,
                                            new Insets(2, 2, 2, 2), 0, 0));
    iZeile++;

    jpaWorkingOn.add(wbuStueckliste,
                     new GridBagConstraints(0, iZeile, 1, 1, 0.05, 0.0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.HORIZONTAL,
                                            new Insets(2, 2, 2, 2), 0, 0));
    jpaWorkingOn.add(wtfStuecklisteNummer,
                     new GridBagConstraints(1, iZeile, 1, 1, 0.1, 0.0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.HORIZONTAL,
                                            new Insets(2, 2, 2, 2), 0, 0));

    iZeile++;

    jpaWorkingOn.add(wlaProjekt,
                     new GridBagConstraints(0, iZeile, 1, 1, 0, 0.0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.HORIZONTAL,
                                            new Insets(2, 2, 2, 2), 0, 0));
    jpaWorkingOn.add(wtfProjekt,
                     new GridBagConstraints(1, iZeile, 1, 1, 0, 0.0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.HORIZONTAL,
                                            new Insets(2, 2, 2, 2), 0, 0));
    iZeile++;
    jpaWorkingOn.add(wbuZielager,
                     new GridBagConstraints(0, iZeile, 1, 1, 0, 0.0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.HORIZONTAL,
                                            new Insets(2, 2, 2, 2), 0, 0));
    jpaWorkingOn.add(wtfZiellager,
                     new GridBagConstraints(1, iZeile, 1, 1, 0, 0.0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.HORIZONTAL,
                                            new Insets(2, 2, 2, 2), 0, 0));
    iZeile++;
    jpaWorkingOn.add(wbuFertigungsgruppe,
                     new GridBagConstraints(0, iZeile, 1, 1, 0, 0.0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.HORIZONTAL,
                                            new Insets(2, 2, 2, 2), 0, 0));
    jpaWorkingOn.add(wtfFertigungsgruppe,
                     new GridBagConstraints(1, iZeile, 1, 1, 0, 0.0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.HORIZONTAL,
                                            new Insets(2, 2, 2, 2), 0, 0));
    iZeile++;
    jpaWorkingOn.add(wbuFertigungsort,
                     new GridBagConstraints(0, iZeile, 1, 1, 0, 0.0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.HORIZONTAL,
                                            new Insets(2, 2, 2, 2), 0, 0));
    jpaWorkingOn.add(wtfFertigungsort,
                     new GridBagConstraints(1, iZeile, 1, 1, 0, 0.0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.HORIZONTAL,
                                            new Insets(2, 2, 2, 2), 0, 0));
    iZeile++;
    jpaWorkingOn.add(wlaWiederholungsintervall,
                     new GridBagConstraints(0, iZeile, 1, 1, 0.14, 0.0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.HORIZONTAL,
                                            new Insets(2, 2, 2, 2), 0, 0));
    jpaWorkingOn.add(wcoWiederholungsintervall,
                     new GridBagConstraints(1, iZeile, 1, 1, 0.2, 0.0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.HORIZONTAL,
                                            new Insets(2, 2, 2, 2), 0, 0));
    jpaWorkingOn.add(wlaAb,
                     new GridBagConstraints(2, iZeile, 1, 1, 0.1, 0.0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.HORIZONTAL,
                                            new Insets(2, 2, 2, 2), 0, 0));
    jpaWorkingOn.add(wdfAb,
                     new GridBagConstraints(3, iZeile, 1, 1, 0.2, 0.0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.HORIZONTAL,
                                            new Insets(2, 2, 2, 2), 0, 0));
    iZeile++;
    jpaWorkingOn.add(wlaLosgroesse,
                     new GridBagConstraints(0, iZeile, 1, 1, 0, 0.0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.HORIZONTAL,
                                            new Insets(2, 2, 2, 2), 0, 0));
    jpaWorkingOn.add(wnfLosgroesse,
                     new GridBagConstraints(1, iZeile, 1, 1, 0, 0.0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.HORIZONTAL,
                                            new Insets(2, 2, 2, 2), 0, 0));
    iZeile++;
    jpaWorkingOn.add(wlaVorauseilend,
                     new GridBagConstraints(0, iZeile, 1, 1, 0, 0.0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.HORIZONTAL,
                                            new Insets(2, 2, 2, 2), 0, 0));
    jpaWorkingOn.add(wnfVorauseilend,
                     new GridBagConstraints(1, iZeile, 1, 1, 0, 0.0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.HORIZONTAL,
                                            new Insets(2, 2, 2, 2), 0, 0));
    jpaWorkingOn.add(wlaTage,
                     new GridBagConstraints(2, iZeile, 1, 1, 0, 0.0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.HORIZONTAL,
                                            new Insets(2, 2, 2, 2), 0, 0));

    iZeile++;
    jpaWorkingOn.add(wcbVersteckt,
                     new GridBagConstraints(1, iZeile, 1, 1, 0, 0.0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.HORIZONTAL,
                                            new Insets(2, 2, 2, 2), 0, 0));
    iZeile++;
 jpaWorkingOn.add(wlaSort,
                  new GridBagConstraints(0, iZeile, 1, 1, 0, 0.0
                                         , GridBagConstraints.CENTER,
                                         GridBagConstraints.HORIZONTAL,
                                         new Insets(2, 2, 2, 2), 0, 0));
 jpaWorkingOn.add(wnfSort,
                  new GridBagConstraints(1, iZeile, 1, 1, 0, 0.0
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


  protected String getLockMeWer()
      throws Exception {
    return HelperClient.LOCKME_WIEDERHOLENDELOSE;
  }


  protected void components2Dto()
      throws Throwable {
    internalFrameFertigung.getWiederholendeloseDto().setCProjekt(wtfProjekt.getText());
    internalFrameFertigung.getWiederholendeloseDto().setTTermin(wdfAb.getTimestamp());
    internalFrameFertigung.getWiederholendeloseDto().setAuftragwiederholungsintervallCNr( (
        String) wcoWiederholungsintervall.getKeyOfSelectedItem());
    internalFrameFertigung.getWiederholendeloseDto().setBVersteckt(Helper.boolean2Short(!wcbVersteckt.isSelected()));
    internalFrameFertigung.getWiederholendeloseDto().setCProjekt(wtfProjekt.getText());
    internalFrameFertigung.getWiederholendeloseDto().setITagevoreilend(wnfVorauseilend.
        getInteger());
    internalFrameFertigung.getWiederholendeloseDto().setNLosgroesse(wnfLosgroesse.
        getBigDecimal());
    internalFrameFertigung.getWiederholendeloseDto().setISort(wnfSort.
        getInteger());
    internalFrameFertigung.getWiederholendeloseDto().setMandantCNr(LPMain.getTheClient().
        getMandant());
  }


  protected void dto2Components()
      throws Throwable {
    wtfProjekt.setText(internalFrameFertigung.getWiederholendeloseDto().getCProjekt());
    wdfAb.setTimestamp(internalFrameFertigung.getWiederholendeloseDto().getTTermin());
    wnfLosgroesse.setBigDecimal(internalFrameFertigung.getWiederholendeloseDto().getNLosgroesse());
    wnfSort.setInteger(internalFrameFertigung.getWiederholendeloseDto().getISort());
    wnfVorauseilend.setInteger(internalFrameFertigung.getWiederholendeloseDto().getITagevoreilend());
    wcbVersteckt.setSelected(!Helper.short2Boolean(internalFrameFertigung.getWiederholendeloseDto().getBVersteckt()));

    if (internalFrameFertigung.getWiederholendeloseDto().getLagerIIdZiel() != null) {
      LagerDto lagerDto = DelegateFactory.getInstance().getLagerDelegate().
          lagerFindByPrimaryKey(internalFrameFertigung.getWiederholendeloseDto().
                                getLagerIIdZiel());
      wtfZiellager.setText(lagerDto.getCNr());
    }
    else {
      wtfZiellager.setText(null);
    }

    if (internalFrameFertigung.getWiederholendeloseDto().getFertigungsgruppeIId() != null) {
      FertigungsgruppeDto fertigungsgruppeDto = DelegateFactory.getInstance().
          getStuecklisteDelegate().
          fertigungsgruppeFindByPrimaryKey(internalFrameFertigung.getWiederholendeloseDto().
                                           getFertigungsgruppeIId());
      wtfFertigungsgruppe.setText(fertigungsgruppeDto.getCBez());

    }
    else {
      wtfFertigungsgruppe.setText(null);
    }

    if (internalFrameFertigung.getWiederholendeloseDto().getPartnerIIdFertigungsort() != null) {
      PartnerDto partnerDtoFertigungsort = DelegateFactory.getInstance().
          getPartnerDelegate().
          partnerFindByPrimaryKey(internalFrameFertigung.getWiederholendeloseDto().
                                  getPartnerIIdFertigungsort());
      wtfFertigungsort.setText(partnerDtoFertigungsort.formatTitelAnrede());

    }
    else {
      wtfFertigungsort.setText(null);
    }
    if (internalFrameFertigung.getWiederholendeloseDto().getKostenstelleIId() != null) {
      KostenstelleDto kostenstelleDto = DelegateFactory.getInstance().getSystemDelegate().
          kostenstelleFindByPrimaryKey(internalFrameFertigung.getWiederholendeloseDto().
                                       getKostenstelleIId());
      wtfKostenstelleNummer.setText(kostenstelleDto.formatKostenstellenbezeichnung());

    }
    else {
      wtfKostenstelleNummer.setText(null);
    }
    if (internalFrameFertigung.getWiederholendeloseDto().getStuecklisteIId() != null) {
      StuecklisteDto stuecklisteDto = DelegateFactory.getInstance().
          getStuecklisteDelegate().
          stuecklisteFindByPrimaryKey(internalFrameFertigung.getWiederholendeloseDto().
                                      getStuecklisteIId());

      wtfStuecklisteNummer.setText(stuecklisteDto.getArtikelDto().
                                   formatArtikelbezeichnung());

    }
    else {
      wtfStuecklisteNummer.setText(null);
    }
    wcoWiederholungsintervall.setKeyOfSelectedItem(internalFrameFertigung.getWiederholendeloseDto().getAuftragwiederholungsintervallCNr());
    

  }


  private void updateLosart()
      throws Throwable {
    Object key = wcoLosart.getKeyOfSelectedItem();
    if (key.equals(FertigungFac.LOSART_IDENT)) {
      wbuStueckliste.setActionCommand(ACTION_SPECIAL_STUECKLISTE);
      wtfStuecklisteNummer.setMandatoryField(true);
    }
    else if (key.equals(FertigungFac.LOSART_MATERIALLISTE)) {
      wbuStueckliste.setActionCommand("");
      wtfStuecklisteNummer.setMandatoryField(false);
      wtfStuecklisteNummer.setText(null);
       internalFrameFertigung.getWiederholendeloseDto().setStuecklisteIId(null);
      internalFrameFertigung.getWiederholendeloseDto().setStuecklisteIId(null);

      FertigungsgruppeDto[] dtos = DelegateFactory.getInstance().getStuecklisteDelegate().
          fertigungsgruppeFindByMandantCNr();
      if (dtos != null && dtos.length > 0) {

        FertigungsgruppeDto fertigungsgruppeDto = DelegateFactory.getInstance().
            getStuecklisteDelegate().
            fertigungsgruppeFindByPrimaryKey(dtos[0].getIId());
        wtfFertigungsgruppe.setText(fertigungsgruppeDto.getCBez());
        internalFrameFertigung.getWiederholendeloseDto().setFertigungsgruppeIId(dtos[0].
            getIId());
      }

    }
  }


  protected void eventActionSpecial(ActionEvent e)
      throws Throwable {
    if (e.getActionCommand().equals(ACTION_SPECIAL_LAGER_FROM_LISTE)) {
      panelQueryFLRLager = ArtikelFilterFactory.getInstance().createPanelFLRLager(
          getInternalFrame(),
          internalFrameFertigung.getWiederholendeloseDto().getLagerIIdZiel(), true, false);

      new DialogQuery(panelQueryFLRLager);

    }
    else if (e.getActionCommand().equals(ACTION_SPECIAL_KOSTENSTELLE)) {
      dialogQueryKostenstelle(e);
    }
    else if (e.getActionCommand().equals(ACTION_SPECIAL_FERTIGUNGSORT)) {
      dialogQueryFertigungsort(e);
    }
    else if (e.getActionCommand().equals(ACTION_SPECIAL_STUECKLISTE)) {
      dialogQueryStueckliste(e);
    }
    else if (e.getActionCommand().equals(ACTION_SPECIAL_LOSART)) {
      updateLosart();
    }
    else if (e.getActionCommand().equals(
        ACTION_SPECIAL_FERTIGUNGSGRUPPE_FROM_LISTE)) {
      dialogQueryFertigungsgruppeFromListe(e);
    }
  }


  public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
      throws
      Throwable {
    if (allMandatoryFieldsSetDlg()) {
      components2Dto();

      if (internalFrameFertigung.getWiederholendeloseDto().getIId() == null) {

        internalFrameFertigung.getWiederholendeloseDto().setIId(DelegateFactory.
            getInstance().
            getFertigungDelegate().createWiederholendelose(
                internalFrameFertigung.getWiederholendeloseDto()));
        internalFrameFertigung.setWiederholendeloseDto(internalFrameFertigung.
            getWiederholendeloseDto());

        InventurDto[] dtos = DelegateFactory.getInstance().getInventurDelegate().
            inventurFindInventurenNachDatum(wdfAb.getTimestamp());

        if (dtos != null && dtos.length > 0) {

          DialogFactory.showModalDialog(LPMain.getInstance().getTextRespectUISPr(
              "lp.error"),
                                        "Es gibt bereits Inventuren nach dem angegebenen Inventurdatum");
        }
        // diesem panel den key setzen.
        setKeyWhenDetailPanel(internalFrameFertigung.getWiederholendeloseDto().getIId());
      }
      else {
        DelegateFactory.getInstance().getFertigungDelegate().updateWiederholendelose(
            internalFrameFertigung.
            getWiederholendeloseDto());
      }

      super.eventActionSave(e, true);
      if (getInternalFrame().getKeyWasForLockMe() == null) {
        getInternalFrame().setKeyWasForLockMe(internalFrameFertigung.
                                              getWiederholendeloseDto().
                                              getIId().toString());
      }
      eventYouAreSelected(false);

    }
  }

}
