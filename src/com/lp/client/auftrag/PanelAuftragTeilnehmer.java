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
package com.lp.client.auftrag;


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
import com.lp.client.frame.LockStateValue;
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
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.partner.PartnerFilterFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.personal.PersonalFilterFactory;
import com.lp.client.system.SystemFilterFactory;
import com.lp.client.util.fastlanereader.gui.QueryType;
import com.lp.server.auftrag.service.AuftragServiceFac;
import com.lp.server.auftrag.service.AuftragteilnehmerDto;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.personal.service.PersonalDto;
import com.lp.server.system.service.FunktionDto;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.util.Helper;

@SuppressWarnings("static-access") 
/**
 * <p>In diesem Fenster werden Auftragteilnehmer erfasst.
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 * <p>Erstellungsdatum 2004-09-29</p>
 * <p> </p>
 * @author uli walch
 * @version $Revision: 1.4 $
 */
public class PanelAuftragTeilnehmer
    extends PanelBasis
{
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
/** Cache for convenience. */
  private InternalFrameAuftrag intFrame = null;
  /** Cache for convenience. */
  private TabbedPaneAuftrag tpAuftrag = null;

  /** Aktueller Teilnehmer. */
  private AuftragteilnehmerDto auftragteilnehmerDto = null;
  /** Der aktuell gewaehlte Teilnehmer ist ein Partner. */
  //private PartnerDto partnerteilnehmerDto = null;
  /** Der aktuelle Benutzer. */
  private PersonalDto personalbenutzerDto = null;
  /** Die aktuelle Funktion. */
  private FunktionDto funktionDto = null;

  private GridBagLayout gridBagLayoutAll = null;
  private JPanel jPanelWorkingOn = null;
  private GridBagLayout gridBagLayoutWorkingOn = null;
  private Border innerBorder = null;

  static final public String ACTION_SPECIAL_TEILNEHMER =
      "action_special_personal_teilnehmer";
  static final public String ACTION_SPECIAL_TEILNEHMERFUNKTION =
      "action_special_teilnehmerfunktion";

  private WrapperButton wbuTeilnehmer = null;

  private PanelQueryFLR panelQueryFLRPersonal = null;

  private PanelQueryFLR panelQueryFLRPartner = null;

  private WrapperTextField wtfTeilnehmer = null;

  private WrapperButton wbuTeilnehmerfunktion = null;
  private PanelQueryFLR panelQueryFLRTeilnehmerfunktion = null;
  private WrapperTextField wtfTeilnehmerfunktion = null;

  private WrapperLabel wlaAngelegt = null;
  private WrapperTextField wtfAngelegt = null;

  private WrapperCheckBox wcbExternerTeilnehmer = null;

  protected boolean bFuegeNeuePositionVorDerSelektiertenEin = false;

  public PanelAuftragTeilnehmer(InternalFrame internalFrame, String add2TitleI,
                                Object key)
      throws Throwable {
    super(internalFrame, add2TitleI, key);

    intFrame = (InternalFrameAuftrag) internalFrame;
    tpAuftrag = intFrame.getTabbedPaneAuftrag();

    jbInit();
    setDefaults();
    initComponents();
  }


  void jbInit()
      throws Throwable {
    // das Aussenpanel hat immer das Gridbaglayout.
    gridBagLayoutAll = new GridBagLayout();
    this.setLayout(gridBagLayoutAll);

    innerBorder = BorderFactory.createEmptyBorder(10, 10, 10, 10);
    this.setBorder(innerBorder);

    // Actionpanel setzen und anhaengen
    JPanel panelButtonAction = getToolsPanel();
    this.add(panelButtonAction, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
        , GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
        new Insets(0, 0, 0, 0), 0, 0));

    // zusaetzliche buttons
    String[] aWhichButtonIUse = {
        PanelBasis.ACTION_UPDATE,
        PanelBasis.ACTION_SAVE,
        PanelBasis.ACTION_DISCARD,
        PanelBasis.ACTION_DELETE};
    enableToolsPanelButtons(aWhichButtonIUse);

    // Workingpanel
    jPanelWorkingOn = new JPanel();
    gridBagLayoutWorkingOn = new GridBagLayout();
    jPanelWorkingOn.setLayout(gridBagLayoutWorkingOn);
    this.add(jPanelWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0
        , GridBagConstraints.SOUTH, GridBagConstraints.BOTH,
        new Insets(0, 0, 0, 0), 0, 0));

    // Statusbar an den unteren Rand des Panels haengen
    add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.BOTH,
        new Insets(0, 0, 0, 0), 0, 0));

    // wegen Dialogauswahl auf FLR events hoeren
    getInternalFrame().addItemChangedListener(this);

    wbuTeilnehmer
        = new WrapperButton();
    wbuTeilnehmer.setText(LPMain.getInstance().getTextRespectUISPr(
        "button.teilnehmer"));
    wbuTeilnehmer.addActionListener(this);
    wbuTeilnehmer.setActionCommand(
        ACTION_SPECIAL_TEILNEHMER);

    wtfTeilnehmer = new WrapperTextField();
    wtfTeilnehmer.setMandatoryFieldDB(true);
    wtfTeilnehmer.setActivatable(false);

    wbuTeilnehmerfunktion = new WrapperButton(
        LPMain.getInstance().getTextRespectUISPr("teiln.label.funktion"));
    wbuTeilnehmerfunktion.addActionListener(this);
    wbuTeilnehmerfunktion.setActionCommand(
        ACTION_SPECIAL_TEILNEHMERFUNKTION);

    wtfTeilnehmerfunktion = new WrapperTextField();
    wtfTeilnehmerfunktion.setActivatable(false);
    wtfTeilnehmerfunktion.setMandatoryFieldDB(true);

    wlaAngelegt = new WrapperLabel(
        LPMain.getInstance().getTextRespectUISPr("teiln.label.erfasst"));
    wlaAngelegt.setMaximumSize(new Dimension(120, 23));
    wlaAngelegt.setMinimumSize(new Dimension(120, 23));
    wlaAngelegt.setPreferredSize(new Dimension(120, 23));

    wtfAngelegt = new WrapperTextField();
    wtfAngelegt.setActivatable(false);

    wcbExternerTeilnehmer = new WrapperCheckBox();
    wcbExternerTeilnehmer.setText(LPMain.getInstance().getTextRespectUISPr(
        "auft.teiln.externerteilnehmer"));
    wcbExternerTeilnehmer.setActivatable(false);

    jPanelWorkingOn.add(wbuTeilnehmer,
                        new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
                                               , GridBagConstraints.CENTER,
                                               GridBagConstraints.BOTH,
                                               new Insets(2, 2, 2, 2), 0, 0));
    jPanelWorkingOn.add(wtfTeilnehmer,
                        new GridBagConstraints(1, 0, 4, 1, 0.0, 0.0
                                               , GridBagConstraints.CENTER,
                                               GridBagConstraints.BOTH,
                                               new Insets(2, 2, 2, 2), 0, 0));
    jPanelWorkingOn.add(wbuTeilnehmerfunktion,
                        new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
                                               , GridBagConstraints.CENTER,
                                               GridBagConstraints.BOTH,
                                               new Insets(2, 2, 2, 2), 0, 0));
    jPanelWorkingOn.add(wtfTeilnehmerfunktion,
                        new GridBagConstraints(1, 1, 1, 1, 0.1, 0.0
                                               , GridBagConstraints.CENTER,
                                               GridBagConstraints.BOTH,
                                               new Insets(2, 2, 2, 2), 0, 0));
    jPanelWorkingOn.add(wlaAngelegt,
                        new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
                                               , GridBagConstraints.CENTER,
                                               GridBagConstraints.BOTH,
                                               new Insets(2, 2, 2, 2), 0, 0));
    jPanelWorkingOn.add(wtfAngelegt,
                        new GridBagConstraints(1, 2, 1, 1, 0.1, 0.0
                                               , GridBagConstraints.CENTER,
                                               GridBagConstraints.BOTH,
                                               new Insets(2, 2, 2, 2), 0, 0));
    jPanelWorkingOn.add(wcbExternerTeilnehmer,
                        new GridBagConstraints(1, 3, 3, 1, 0.0, 0.0
                                               , GridBagConstraints.CENTER,
                                               GridBagConstraints.BOTH,
                                               new Insets(2, 0, 2, 2), 0, 0));
  }


  private void setDefaults()
      throws Throwable {
    leereAlleFelder(this);

    wbuTeilnehmer.setActivatable(true);
    //wcbExternerTeilnehmer.setSelected(false); //das Flag hier nicht veraendern

    // den Benutzer setzen
    Integer pkPersonal = LPMain.getInstance().getTheClient().getIDPersonal();
    personalbenutzerDto = DelegateFactory.getInstance().getPersonalDelegate().
        personalFindByPrimaryKey(
            pkPersonal);
    wtfAngelegt.setText(personalbenutzerDto.
                        getPartnerDto().formatAnrede());
  }


  protected void eventItemchanged(EventObject eI)
      throws Throwable {
    ItemChangedEvent e = (ItemChangedEvent) eI;

    if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
      if (e.getSource() == panelQueryFLRPersonal) {
        // es wurde ein Teilnehmer aus der Personalliste gewaehlt
        Integer iIdPersonal = (Integer) ( (ISourceEvent) e.getSource()).
            getIdSelected();

        // kann nicht uebernommen werden, wenn der Teilnehmer bereits als Vertreter erfasst wurde
        if (tpAuftrag.getAuftragDto().getPersonalIIdVertreter().intValue() ==
            iIdPersonal.intValue()) {
          DialogFactory.showModalDialog(
              LPMain.getInstance().getTextRespectUISPr("lp.warning"),
              LPMain.getInstance().getTextRespectUISPr(
              "auft.teilnehmer.warning.personalistvertreter"));
        }
        else {

          // kann nicht uebernommen werden, wenn der Teilnehmer bereits erfasst wurde
          PersonalDto personalDto = DelegateFactory.getInstance().getPersonalDelegate().
              personalFindByPrimaryKey(iIdPersonal);

          if (DelegateFactory.getInstance().getAuftragteilnehmerDelegate().
              istPartnerEinAuftragteilnehmer(
                  personalDto.getPartnerIId(), tpAuftrag.getAuftragDto().getIId())) {
            DialogFactory.showModalDialog(
                LPMain.getInstance().getTextRespectUISPr("lp.warning"),
                LPMain.getInstance().getTextRespectUISPr(
                    "auft.teilnehmer.warning.personalbereitszugeordnet"));
          }
          else {
            //partnerteilnehmerDto = personalDto.getPartnerDto();
            auftragteilnehmerDto.setPartnerIIdAuftragteilnehmer(personalDto.getPartnerDto().
                getIId());

            wtfTeilnehmer.setText(personalDto.getPartnerDto().
                                  formatAnrede());
          }
        }
      }
      else if (e.getSource() == panelQueryFLRPartner) {
        // es wurde ein externer Partner aus der Liste der Partner gewaehlt
        Integer iIdPartner = (Integer) ( (ISourceEvent) e.getSource()).
            getIdSelected();

        // kann nicht uebernommen werden, wenn der Teilnehmer bereits als Vertreter erfasst wurde
        if (tpAuftrag.getAuftragDto().getPersonalIIdVertreter().intValue() ==
            iIdPartner.intValue()) {
          DialogFactory.showModalDialog(
              LPMain.getInstance().getTextRespectUISPr("lp.warning"),
              LPMain.getInstance().getTextRespectUISPr(
              "auft.teilnehmer.warning.partneristvertreter"));
        }
        else {

          // kann nicht uebernommen werden, wenn der Teilnehmer bereits erfasst wurde
          PartnerDto partnerDto = DelegateFactory.getInstance().getPartnerDelegate().
              partnerFindByPrimaryKey(iIdPartner);

          if (DelegateFactory.getInstance().getAuftragteilnehmerDelegate().
              istPartnerEinAuftragteilnehmer(
                  iIdPartner, tpAuftrag.getAuftragDto().getIId())) {
            DialogFactory.showModalDialog(
                LPMain.getInstance().getTextRespectUISPr("lp.warning"),
                LPMain.getInstance().getTextRespectUISPr(
                    "auft.teilnehmer.warning.partnerbereitszugeordnet"));
          }
          else {
            auftragteilnehmerDto.setPartnerIIdAuftragteilnehmer(iIdPartner);

            wtfTeilnehmer.setText(partnerDto.formatAnrede());
          }
        }
      }
      else if (e.getSource() == panelQueryFLRTeilnehmerfunktion) {
        Integer iIdFunktion = (Integer) ( (ISourceEvent) e.getSource()).
            getIdSelected();

        funktionDto = DelegateFactory.getInstance().getLocaleDelegate().
            funktionFindByPrimaryKey(iIdFunktion);

        wtfTeilnehmerfunktion.setText(funktionDto.getCNr());
      }
      /*else {
        // source ist die flr liste auf meinem panel
        Integer pkTeilnehmer = (Integer) ( (ISourceEvent) e.getSource()).
            getIdSelected();
        if (pkTeilnehmer != null) {
       auftragteilnehmerDto = DelegateFactory.getInstance().getAuftragteilnehmerDelegate().
              auftragteilnehmerFindByPrimaryKey(pkTeilnehmer);
          dto2Components();
        }
             }*/
    }
  }


  public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
      throws
      Throwable {
    if (allMandatoryFieldsSetDlg()) {

      try {
        components2Dto();

        if (auftragteilnehmerDto.getIId() == null) {
          if (bFuegeNeuePositionVorDerSelektiertenEin) {
            Integer iIdAktuellePosition = (Integer) tpAuftrag.getAuftragTeilnehmerTop().
                getSelectedId();

            // die erste Position steht an der Stelle 1
            Integer iSortAktuellePosition = new Integer(1);

            if (iIdAktuellePosition != null) {
              iSortAktuellePosition = DelegateFactory.getInstance().
                  getAuftragteilnehmerDelegate().
                  auftragteilnehmerFindByPrimaryKey(iIdAktuellePosition).getISort();

              DelegateFactory.getInstance().getAuftragteilnehmerDelegate().
                  sortierungAnpassenBeiEinfuegenEinerPositionVorPosition(
                      tpAuftrag.getAuftragDto().getIId(), iSortAktuellePosition.intValue());
            }

            auftragteilnehmerDto.setISort(iSortAktuellePosition);
          }

          Integer pkTeilnehmer = DelegateFactory.getInstance().
              getAuftragteilnehmerDelegate().
              createAuftragteilnehmer(auftragteilnehmerDto);
          auftragteilnehmerDto = DelegateFactory.getInstance().
              getAuftragteilnehmerDelegate().
              auftragteilnehmerFindByPrimaryKey(pkTeilnehmer);
          this.setKeyWhenDetailPanel(pkTeilnehmer);

          // @todo in der Folge wird am Server fuer den entsprechenden Teilnehmer eine Aufgabe erzeugt  PJ 4818
        }
        else {
          DelegateFactory.getInstance().getAuftragteilnehmerDelegate().
              updateAuftragteilnehmer(this.
                                      auftragteilnehmerDto);
        }

        // buttons schalten
        super.eventActionSave(e, true);

        eventYouAreSelected(false);
      }
      finally {
        bFuegeNeuePositionVorDerSelektiertenEin = false;
      }
    }
  }


  public void eventActionNew(EventObject eventObject, boolean bLockMeI,
                             boolean bNeedNoNewI)
      throws
      Throwable {
    if (tpAuftrag.istAktualisierenAuftragErlaubt()) {

      super.eventActionNew(eventObject, true, false);

      this.resetPanel();

      // @todo darf ich hier casten?  PJ 4819

      // pqaddbutton: 4 es kann ein normales oder das spezielle neu sein
      if ( ( (ItemChangedEvent) eventObject).getID() ==
          ItemChangedEvent.ACTION_MY_OWN_NEW) {
        // pqaddbutton: 5 Dieses Flag gibt an, ob aus Personal oder Partner gewaehlt wird
        auftragteilnehmerDto.setBIstExternerTeilnehmer(new Short( (short) 1)); // vorbelegen
        wcbExternerTeilnehmer.setSelected(true);
      }
      else {
        auftragteilnehmerDto.setBIstExternerTeilnehmer(new Short( (short) 0)); // vorbelegen
        wcbExternerTeilnehmer.setSelected(false);
      }

      if ( ( (ItemChangedEvent) eventObject).getID() ==
          ItemChangedEvent.ACTION_POSITION_VORPOSITIONEINFUEGEN) {
        bFuegeNeuePositionVorDerSelektiertenEin = true;
      }
    }
    else {
      tpAuftrag.getAuftragTeilnehmerTop().updateButtons(
          tpAuftrag.getAuftragTeilnehmerBottom().getLockedstateDetailMainKey());
    }
  }


  protected void eventActionUpdate(ActionEvent aE, boolean bNeedNoUpdateI)
      throws
      Throwable {
    if (tpAuftrag.istAktualisierenAuftragErlaubt()) {
      super.eventActionUpdate(aE, false);
    }
  }


  protected void eventActionDelete(ActionEvent e, boolean bAdministrateLockKeyI,
                                   boolean bNeedNoDeleteI)
      throws
      Throwable {
    if (tpAuftrag.istAktualisierenAuftragErlaubt()) {
      DelegateFactory.getInstance().getAuftragteilnehmerDelegate().
          removeAuftragteilnehmer(auftragteilnehmerDto);
      this.setKeyWhenDetailPanel(null);
      super.eventActionDelete(e, false, false); // keyWasForLockMe nicht ueberschreiben
    }
  }


  protected void eventActionSpecial(ActionEvent e)
      throws Throwable {
    if (e.getActionCommand().equals(ACTION_SPECIAL_TEILNEHMER)) {
      if (Helper.short2boolean(auftragteilnehmerDto.getBIstExternerTeilnehmer())) {
        dialogQueryPartner(e);
      }
      else {
        dialogQueryPersonal(e);
      }
    }
    else
    if (e.getActionCommand().equals(ACTION_SPECIAL_TEILNEHMERFUNKTION)) {
      dialogQueryTeilnehmerfunktion(e);
    }
  }


  /**
   * Dialogfenster zur Auswahl eines Partners.
   * <br>Erfasst werden externe Teilnehmer zu einem Auftrag
   *
   * @param e ActionEvent
   * @throws java.lang.Throwable Ausnahme
   */
  void dialogQueryPartner(ActionEvent e)
      throws Throwable {
    panelQueryFLRPartner = PartnerFilterFactory.getInstance()
	.createPanelFLRPartner(getInternalFrame());
    new DialogQuery(panelQueryFLRPartner);
  }


  void dialogQueryPersonal(ActionEvent e)
      throws Throwable {
    QueryType[] qt = null;
    FilterKriterium[] fk = SystemFilterFactory.getInstance().createFKMandantCNr();

    Integer iIdPartner = auftragteilnehmerDto.getPartnerIIdAuftragteilnehmer();

    if (iIdPartner != null) {
      iIdPartner = DelegateFactory.getInstance().getPersonalDelegate().
          personalFindByPartnerIIdMandantCNr(
              auftragteilnehmerDto.getPartnerIIdAuftragteilnehmer(),
              LPMain.getInstance().getTheClient().getMandant()).getIId();
    }

    panelQueryFLRPersonal = PersonalFilterFactory.getInstance().createPanelFLRPersonal(
        getInternalFrame(), true, false, iIdPartner);

    new DialogQuery(panelQueryFLRPersonal);
  }


  /**
   * Dialogfenster zur Auswahl einer Funktion aus den Auftragteilnehmerfunktionen.
   * @param e ActionEvent
   * @throws java.lang.Throwable Ausnahme
   */
  void dialogQueryTeilnehmerfunktion(ActionEvent e)
      throws Throwable {
    QueryType[] qt = null;
    FilterKriterium[] fk = null;

    panelQueryFLRTeilnehmerfunktion = new PanelQueryFLR(
        qt,
        fk,
        QueryParameters.UC_ID_FUNKTION,
        null,
        intFrame,
        LPMain.getInstance().getTextRespectUISPr("lp.funktionauswahlliste"),
        funktionDto.getIId());

    new DialogQuery(panelQueryFLRTeilnehmerfunktion);
  }


  private void resetPanel()
      throws Throwable {
    auftragteilnehmerDto = new AuftragteilnehmerDto();
    //partnerteilnehmerDto = new PartnerDto();
    personalbenutzerDto = new PersonalDto();
    funktionDto = new FunktionDto();

    leereAlleFelder(this);
    setDefaults();
  }


  private void dto2Components()
      throws Throwable {
    // den zugehoerigen Partner zum Auftragteilnehmer bestimmen und anzeigen
    //partnerteilnehmerDto = DelegateFactory.getInstance().getPartnerDelegate().
    //    partnerFindByPrimaryKey(auftragteilnehmerDto.getPartnerIIdAuftragteilnehmer());
    wtfTeilnehmer.setText(DelegateFactory.getInstance().getPartnerDelegate().
                          partnerFindByPrimaryKey(auftragteilnehmerDto.
                                                  getPartnerIIdAuftragteilnehmer()).
                          formatAnrede());

    // die zugehoerige Funktion zum Auftragteilnehmer bestimmen und anzeigen
    funktionDto = DelegateFactory.getInstance().getLocaleDelegate().
        funktionFindByPrimaryKey(auftragteilnehmerDto.getAuftragteilnehmerfunktionIId());
    wtfTeilnehmerfunktion.setText(funktionDto.getCNr());

    // bestimmen, wer den Teilnehmer angelegt hat und anzeigen
    Integer pkPartnerAnlegen = auftragteilnehmerDto.getPersonalIIDAnlegen();
    personalbenutzerDto = DelegateFactory.getInstance().getPersonalDelegate().
        personalFindByPrimaryKey(
            pkPartnerAnlegen);
    wtfAngelegt.setText(personalbenutzerDto.getPartnerDto().formatAnrede());
    wcbExternerTeilnehmer.setSelected(Helper.short2boolean(auftragteilnehmerDto.
        getBIstExternerTeilnehmer()));
  }


  private void components2Dto()
      throws ExceptionLP {
    if (auftragteilnehmerDto.getIId() == null) {
      auftragteilnehmerDto.setAuftragIId(tpAuftrag.getAuftragDto().getIId());

      // eine neue Positione bekommt ein eindeutiges iSort
      int iSortNeu = DelegateFactory.getInstance().getAuftragteilnehmerDelegate().
          getMaxISort(tpAuftrag.getAuftragDto().getIId()).intValue() + 1;
      auftragteilnehmerDto.setISort(new Integer(iSortNeu));
    }

    //this.auftragteilnehmerDto.setPartnerIIdAuftragteilnehmer(
    //    partnerteilnehmerDto.getIId());
    this.auftragteilnehmerDto.setAuftragteilnehmerfunktionIId(
        funktionDto.getIId());
    this.auftragteilnehmerDto.setPersonalIIdAnlegen(personalbenutzerDto.getIId());
  }


  public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
      throws Throwable {
    super.eventYouAreSelected(false);

    // teilnehmer neu einlesen, ausloeser war ev. ein refresh
    Object oKey = getKeyWhenDetailPanel();

    // zuerst alles zuruecksetzen, ausloeser war ev. ein discard
    setDefaults();

    if (oKey != null && !oKey.equals(LPMain.getLockMeForNew())) {
      auftragteilnehmerDto = DelegateFactory.getInstance().getAuftragteilnehmerDelegate().
          auftragteilnehmerFindByPrimaryKey( (Integer) oKey);
      dto2Components();

      // der Teilnehmer selbst kann nicht geaendert werden
      wbuTeilnehmer.setActivatable(false);
    }

    tpAuftrag.setTitleAuftrag(LPMain.getInstance().
                              getTextRespectUISPr(
                                  "auft.title.panel.teilnehmer"));

    tpAuftrag.enablePanelsNachBitmuster();

    aktualisiereStatusbar();
  }


  private void aktualisiereStatusbar()
      throws Throwable {
    setStatusbarPersonalIIdAnlegen(tpAuftrag.getAuftragDto().getPersonalIIdAnlegen());
    setStatusbarTAnlegen(tpAuftrag.getAuftragDto().getTAnlegen());
    setStatusbarPersonalIIdAendern(tpAuftrag.getAuftragDto().getPersonalIIdAendern());
    setStatusbarTAendern(tpAuftrag.getAuftragDto().getTAendern());
    setStatusbarStatusCNr(tpAuftrag.getAuftragStatus());
  }


  /**
   * Verwerfen der aktuelle Usereingabe und zurueckgehen auf den bestehenden
   * Datensatz, wenn einer existiert.
   * @param e Ereignis
   * @throws Throwable Ausnahme
   */
  protected void eventActionDiscard(ActionEvent e)
      throws Throwable {
    super.eventActionDiscard(e);

    // poseinfuegen: 1 per Default wird eine Position hinten angefuegt
    bFuegeNeuePositionVorDerSelektiertenEin = false;

    tpAuftrag.enablePanelsNachBitmuster();
  }


  protected String getLockMeWer()
      throws Exception {
    return HelperClient.LOCKME_AUFTRAG;
  }


  protected void eventActionRefresh(ActionEvent e, boolean bNeedNoRefreshI)
      throws Throwable {
    super.eventActionRefresh(e, bNeedNoRefreshI);

    tpAuftrag.enablePanelsNachBitmuster();
  }


  public LockStateValue getLockedstateDetailMainKey()
      throws Throwable {

    LockStateValue lsv = super.getLockedstateDetailMainKey();

    if (tpAuftrag.getAuftragDto().getIId() != null) {
      if (tpAuftrag.getAuftragDto().getStatusCNr().equals(AuftragServiceFac.
          AUFTRAGSTATUS_STORNIERT) ||
          tpAuftrag.getAuftragDto().getStatusCNr().equals(AuftragServiceFac.
          AUFTRAGSTATUS_TEILERLEDIGT) ||
          tpAuftrag.getAuftragDto().getStatusCNr().equals(AuftragServiceFac.
          AUFTRAGSTATUS_ERLEDIGT)) {
        lsv = new LockStateValue(PanelBasis.LOCK_ENABLE_REFRESHANDPRINT_ONLY);
      }
    }

    return lsv;
  }
}
