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
package com.lp.client.fertigung;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;

import javax.swing.JComponent;

import com.lp.client.auftrag.AuftragFilterFactory;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelDialogKriterien;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.system.SystemFilterFactory;
import com.lp.client.util.fastlanereader.gui.QueryType;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.auftrag.service.AuftragDto;
import com.lp.server.auftrag.service.AuftragServiceFac;
import com.lp.server.auftrag.service.AuftragpositionDto;
import com.lp.server.fertigung.service.LosDto;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.stueckliste.service.StuecklisteDto;
import com.lp.server.system.service.KostenstelleDto;
import com.lp.server.util.Facade;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;


@SuppressWarnings("static-access") 
/**
 * <p> Diese Klasse kuemmert sich um die nachtraegliche Aenderung der Loskopfdaten</p>
 *
 * <p>Copyright Logistik Pur Software GmbH (c) 2004, 2005, 2006, 2007</p>
 *
 * <p>Erstellung: Martin Bluehweis; 13.04.07</p>
 *
 * <p>@author $Author: christian $</p>
 *
 * @version not attributable Date $Date: 2011/03/08 13:21:01 $
 */
public class PanelDialogZuordnung
    extends PanelDialogKriterien {
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private LosDto losDto = null;
  private LosDto losDtoElternlos = null;
  private AuftragDto auftragDto = null;
  private AuftragpositionDto auftragpositionDto = null;
  private KostenstelleDto kostenstelleDto = null;

  private WrapperTextField wtfKostenstelleNummer = null;
  private WrapperButton wbuKostenstelle = null;
  private WrapperTextField wtfKostenstelleBezeichnung = null;

  private WrapperLabel wlaAbteilung = null;
  private WrapperTextField wtfAbteilung = null;
  private WrapperLabel wlaKunde = null;
  private WrapperTextField wtfAuftragNummer = null;
  private WrapperButton wbuAuftrag = null;
  private WrapperTextField wtfAuftragBezeichnung = null;
  private WrapperTextField wtfKunde = null;
  private WrapperTextField wtfAdresse = null;
  private WrapperLabel wlaProjekt = null;
  private WrapperTextField wtfProjekt = null;

  private WrapperButton wbuAuftragposition = null;
  private WrapperNumberField wtfAuftragpositionNummer = null;
  private WrapperTextField wtfAuftragpositionBezeichnung = null;

  private WrapperButton wbuElternlos = null;
  private WrapperTextField wtfElternlosNummer = null;

  private static final String ACTION_SPECIAL_AUFTRAG =
      "action_special_loszuordnung_auftrag";
  private static final String ACTION_SPECIAL_AUFTRAGPOSITION =
      "action_special_loszuordnung_auftragposition";
  private static final String ACTION_SPECIAL_KOSTENSTELLE =
      "action_special_loszuordnung_kostenstelle";
  private static final String ACTION_SPECIAL_ELTERNLOS =
      "action_special_loszuordnung_elternlos";

  private PanelQueryFLR panelQueryFLRAuftrag = null;
  private PanelQueryFLR panelQueryFLRAuftragposition = null;
  private PanelQueryFLR panelQueryFLRKostenstelle = null;
  private PanelQueryFLR panelQueryFLRElternlos = null;


  public PanelDialogZuordnung(InternalFrame internalFrame, String title,  LosDto losDto) throws Throwable {
    super(internalFrame, title);
    this.losDto = losDto;
    jbInit();
    setDefaults();
    initComponents();
  }


  private void setDefaults()
      throws Throwable {
    holeAuftrag(losDto.getAuftragIId());
    holeAuftragposition(losDto.getAuftragpositionIId());
    holeElternlos(losDto.getLosIIdElternlos());
    holeKostenstelle(losDto.getKostenstelleIId());
  }


  private void jbInit()
      throws Throwable {
    wtfKostenstelleNummer = new WrapperTextField();
    wbuKostenstelle = new WrapperButton();
    wtfKostenstelleBezeichnung = new WrapperTextField();

    wlaAbteilung = new WrapperLabel();
    wtfAbteilung = new WrapperTextField();
    wlaKunde = new WrapperLabel();
    wtfAuftragNummer = new WrapperTextField();
    wbuAuftrag = new WrapperButton();
    wtfAuftragBezeichnung = new WrapperTextField();
    wtfKunde = new WrapperTextField();
    wtfAdresse = new WrapperTextField();
    wlaProjekt = new WrapperLabel();
    wtfProjekt = new WrapperTextField();

    wbuAuftragposition = new WrapperButton();
    wtfAuftragpositionNummer = new WrapperNumberField();
    wtfAuftragpositionBezeichnung = new WrapperTextField();

    wbuElternlos = new WrapperButton();
    wtfElternlosNummer = new WrapperTextField();





    jpaWorkingOn.setLayout(new GridBagLayout());
    this.add(jpaWorkingOn,  new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0,0,0,0), 0, 0));




    wtfKunde.setColumnsMax(Facade.MAX_UNBESCHRAENKT);
    wlaAbteilung.setText(LPMain.getInstance().getTextRespectUISPr("lp.abteilung"));
    wtfAdresse.setActivatable(false);
    wtfAdresse.setColumnsMax(Facade.MAX_UNBESCHRAENKT);
    wtfAuftragpositionNummer.setFractionDigits(0);
    wtfAuftragpositionNummer.setMaximumIntegerDigits(4);
    wtfAuftragpositionNummer.setActivatable(false);
    wbuAuftrag.setText(LPMain.getInstance().getTextRespectUISPr("button.auftrag"));
    wbuAuftrag.setToolTipText(LPMain.getInstance().getTextRespectUISPr(
        "button.auftrag.tooltip"));
    wbuAuftragposition.setText(LPMain.getInstance().getTextRespectUISPr("lp.position"));
    wbuAuftragposition.setToolTipText(LPMain.getInstance().getTextRespectUISPr(
        "lp.position"));
    wlaKunde.setText(LPMain.getInstance().getTextRespectUISPr("label.kunde"));
    wbuKostenstelle.setText(LPMain.getInstance().getTextRespectUISPr(
        "button.kostenstelle"));
    wbuKostenstelle.setToolTipText(LPMain.getInstance().getTextRespectUISPr(
        "button.kostenstelle.tooltip"));

    wtfKunde.setActivatable(false);
    getInternalFrame().addItemChangedListener(this);

    wtfAuftragNummer.setActivatable(false);
    wtfAuftragBezeichnung.setActivatable(false);
    wtfKostenstelleNummer.setActivatable(false);
    wtfKostenstelleNummer.setMandatoryField(true);
    wtfKostenstelleBezeichnung.setActivatable(false);
    wtfAuftragpositionBezeichnung.setActivatable(false);

    wbuAuftrag.addActionListener(this);
    wbuAuftrag.setActionCommand(ACTION_SPECIAL_AUFTRAG);
    wbuAuftragposition.addActionListener(this);
    wbuAuftragposition.setActionCommand(ACTION_SPECIAL_AUFTRAGPOSITION);
    wbuKostenstelle.addActionListener(this);
    wbuKostenstelle.setActionCommand(ACTION_SPECIAL_KOSTENSTELLE);

    wtfAbteilung.setActivatable(false);

    iZeile++;
    jpaWorkingOn.add(wbuAuftrag,
                        new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0,
                                               GridBagConstraints.CENTER,
                                               GridBagConstraints.BOTH,
                                               new Insets(2, 2, 2, 2), 0, 0));
    jpaWorkingOn.add(wtfAuftragNummer,
                        new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0,
                                               GridBagConstraints.CENTER,
                                               GridBagConstraints.BOTH,
                                               new Insets(2, 2, 2, 2), 0, 0));
    jpaWorkingOn.add(wtfAuftragBezeichnung,
                        new GridBagConstraints(2, iZeile, 3, 1, 0.0, 0.0,
                                               GridBagConstraints.CENTER,
                                               GridBagConstraints.BOTH,
                                               new Insets(2, 2, 2, 2), 0, 0));
    iZeile++;
    jpaWorkingOn.add(wbuAuftragposition,
                        new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0,
                                               GridBagConstraints.CENTER,
                                               GridBagConstraints.BOTH,
                                               new Insets(2, 2, 2, 2), 0, 0));
    jpaWorkingOn.add(wtfAuftragpositionNummer,
                        new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0,
                                               GridBagConstraints.CENTER,
                                               GridBagConstraints.BOTH,
                                               new Insets(2, 2, 2, 2), 0, 0));
    jpaWorkingOn.add(wtfAuftragpositionBezeichnung,
                        new GridBagConstraints(2, iZeile, 3, 1, 0.0, 0.0,
                                               GridBagConstraints.CENTER,
                                               GridBagConstraints.BOTH,
                                               new Insets(2, 2, 2, 2), 0, 0));
    iZeile++;
    jpaWorkingOn.add(wlaKunde,
                        new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0,
                                               GridBagConstraints.CENTER,
                                               GridBagConstraints.BOTH,
                                               new Insets(2, 2, 2, 2), 0, 0));
    jpaWorkingOn.add(wtfKunde,
                        new GridBagConstraints(1, iZeile, 4, 1, 0.0, 0.0,
                                               GridBagConstraints.CENTER,
                                               GridBagConstraints.BOTH,
                                               new Insets(2, 2, 2, 2), 0, 0));
    iZeile++;
    jpaWorkingOn.add(wtfAdresse,
                        new GridBagConstraints(1, iZeile, 4, 1, 0.0, 0.0,
                                               GridBagConstraints.CENTER,
                                               GridBagConstraints.BOTH,
                                               new Insets(2, 2, 2, 2), 0, 0));
    iZeile++;
    jpaWorkingOn.add(wlaAbteilung,
                        new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0,
                                               GridBagConstraints.WEST,
                                               GridBagConstraints.BOTH,
                                               new Insets(2, 2, 2, 2), 0, 0));
    jpaWorkingOn.add(wtfAbteilung,
                        new GridBagConstraints(1, iZeile, 4, 1, 0.0, 0.0,
                                               GridBagConstraints.CENTER,
                                               GridBagConstraints.BOTH,
                                               new Insets(2, 2, 2, 2), 0, 0));
    iZeile++;
    jpaWorkingOn.add(wbuKostenstelle,
                        new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0,
                                               GridBagConstraints.CENTER,
                                               GridBagConstraints.BOTH,
                                               new Insets(2, 2, 2, 2), 0, 0));
    jpaWorkingOn.add(wtfKostenstelleNummer,
                        new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0,
                                               GridBagConstraints.CENTER,
                                               GridBagConstraints.BOTH,
                                               new Insets(2, 2, 2, 2), 0, 0));
    jpaWorkingOn.add(wtfKostenstelleBezeichnung,
                        new GridBagConstraints(2, iZeile, 3, 1, 0.0, 0.0,
                                               GridBagConstraints.CENTER,
                                               GridBagConstraints.BOTH,
                                               new Insets(2, 2, 2, 2), 0, 0));
  }


  /**
   *
   * @param e ActionEvent
   * @throws Throwable
   * @todo vom internalframe das dautm abholen  PJ 4940
   */
  protected void eventActionSpecial(ActionEvent e) throws Throwable {
    if (e.getActionCommand().equals(ACTION_SPECIAL_AUFTRAG)) {
      dialogQueryAuftrag(e);
    }
    else if (e.getActionCommand().equals(ACTION_SPECIAL_AUFTRAGPOSITION)) {
      if (auftragDto == null) {
        // zuerst den auftrag
        dialogQueryAuftrag(e);
      }
      else {
        dialogQueryAuftragposition(e);
      }
    }
    else if (e.getActionCommand().equals(ACTION_SPECIAL_KOSTENSTELLE)) {
      dialogQueryKostenstelle(e);
    }
    else if (e.getActionCommand().equals(ACTION_SPECIAL_ELTERNLOS)) {
      dialogQueryElternlos(e);
    }
    else if (e.getActionCommand().equals(ACTION_SPECIAL_OK)) {
      DelegateFactory.getInstance().getFertigungDelegate().updateLosZuordnung(losDto);
    }
    super.eventActionSpecial(e);
  }


  protected JComponent getFirstFocusableComponent()
      throws Exception {
    return wbuAuftrag;
  }

  private void holeAuftrag(Object key)
      throws Throwable {
    if (key != null) {
      auftragDto = DelegateFactory.getInstance().getAuftragDelegate().
          auftragFindByPrimaryKey( (Integer) key);
    }
    else {
      auftragDto = null;
    }
    dto2ComponentsAuftrag();
  }


  private void holeKostenstelle(Object key)
      throws Throwable {
    if (key != null) {
      kostenstelleDto = DelegateFactory.getInstance().getSystemDelegate().
          kostenstelleFindByPrimaryKey( (Integer) key);
    }
    else {
      kostenstelleDto = null;
    }
    dto2ComponentsKostenstelle();
  }


  private void holeElternlos(Object key)
      throws Throwable {
    if (key != null) {
      losDtoElternlos = DelegateFactory.getInstance().getFertigungDelegate().
          losFindByPrimaryKey( (Integer) key);
    }
    else {
      losDtoElternlos = null;
    }
    dto2ComponentsElternlos();
  }


  private void dto2ComponentsAuftrag()
      throws Throwable {
    if (auftragDto != null) {
      wtfAuftragNummer.setText(auftragDto.getCNr());
      wtfAuftragBezeichnung.setText(auftragDto.getCBezProjektbezeichnung());
      // wenn noch kein Projekt eingegeben ist, wird das vom Auftrag uebernommen
      if (wtfProjekt.getText() == null || wtfProjekt.getText().trim().equals("")) {
        wtfProjekt.setText(auftragDto.getCBezProjektbezeichnung());
      }
      // die kostenstelle wird aus dem auftrag uebernommen, wenn nicht schon anders definiert
      if (kostenstelleDto == null && auftragDto.getKostIId() != null) {
        holeKostenstelle(auftragDto.getKostIId());
      }
      // wenn schon eine position definiert ist und die zu einem anderen auftrag gehoert, dann loeschen
      if (auftragpositionDto != null &&
          !auftragpositionDto.getBelegIId().equals(auftragDto.getIId())) {
        holeAuftragposition(null);
      }
    }
    else {
      wtfAuftragNummer.setText(null);
      wtfAuftragBezeichnung.setText(null);
      // auch die auftragsposition loeschen
      holeAuftragposition(null);
    }
    dto2ComponentsKunde();
  }


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


  private void dto2ComponentsKunde()
      throws Throwable {
    if (auftragDto != null) {
      KundeDto kundeDto = DelegateFactory.getInstance().getKundeDelegate().
          kundeFindByPrimaryKey(auftragDto.getKundeIIdAuftragsadresse());
      this.wtfKunde.setText(kundeDto.getPartnerDto().formatFixTitelName1Name2());
      if (kundeDto.getPartnerDto().getLandplzortDto() != null) {
        this.wtfAdresse.setText(kundeDto.getPartnerDto().getLandplzortDto().
                                formatLandPlzOrt());
      }
      else {
        this.wtfAdresse.setText(null);
      }
      this.wtfAbteilung.setText(kundeDto.getPartnerDto().getCName3vorname2abteilung());
    }
    else {
      wtfKunde.setText(null);
      wtfAdresse.setText(null);
      wtfAbteilung.setText(null);
    }
  }


  private void holeAuftragposition(Object key)
      throws Throwable {
    if (key != null) {
      auftragpositionDto = DelegateFactory.getInstance().getAuftragpositionDelegate().
          auftragpositionFindByPrimaryKey( (Integer) key);
    }
    else {
      auftragpositionDto = null;
    }
    dto2ComponentsAuftragposition();
  }


  private void dto2ComponentsAuftragposition()
      throws Throwable {
    if (auftragpositionDto != null) {
      wtfAuftragpositionNummer.setInteger(auftragpositionDto.getISort());
      wtfAuftragpositionBezeichnung.setText(auftragpositionDto.getCBez());
      // Stueckliste vorbesetzen, wenns eine ist und wenn vorher noch keine definiert wurde
      if (auftragpositionDto.getPositionsartCNr().equals(AuftragServiceFac.
          AUFTRAGPOSITIONART_IDENT)) {
        StuecklisteDto stuecklisteDto = DelegateFactory.getInstance().
            getStuecklisteDelegate().
            stuecklisteFindByMandantCNrArtikelIIdOhneExc(auftragpositionDto.
            getArtikelIId());
        if (stuecklisteDto == null) {
          auftragpositionDto = null;
          dto2ComponentsAuftragposition();
          DialogFactory.showModalDialog(
              LPMain.getInstance().getTextRespectUISPr("lp.hint"),
              LPMain.getInstance().getTextRespectUISPr("fert.warning.keinestueckliste"));
          return;
        }
      }
      // kein Stuecklistenartikel
      else {
        auftragpositionDto = null;
        dto2ComponentsAuftragposition();
        DialogFactory.showModalDialog(
            LPMain.getInstance().getTextRespectUISPr("lp.hint"),
            LPMain.getInstance().getTextRespectUISPr("fert.warning.keinestueckliste"));
        return;
      }
      //
      if (auftragpositionDto.getPositionsartCNr().equals(AuftragServiceFac.
          AUFTRAGPOSITIONART_IDENT)) {
        ArtikelDto artikelDto = DelegateFactory.getInstance().getArtikelDelegate().
            artikelFindByPrimaryKey(auftragpositionDto.getArtikelIId());
        if (artikelDto.getArtikelsprDto() != null) {
          wtfAuftragpositionBezeichnung.setText(artikelDto.getArtikelsprDto().getCBez());
        }
        else {
          wtfAuftragpositionBezeichnung.setText(auftragpositionDto.getCBez());
        }
      }
      else {
        wtfAuftragpositionBezeichnung.setText(auftragpositionDto.getCBez());
      }
    }
    else {
      wtfAuftragpositionNummer.setInteger(null);
      wtfAuftragpositionBezeichnung.setText(null);
    }
  }


  /**
   * Dialogfenster zur Auftragauswahl.
   *
   * @param e ActionEvent
   * @throws Throwable
   */
  private void dialogQueryAuftrag(ActionEvent e)
      throws Throwable {
    //FilterKriterium[] fk = SystemFilterFactory.getInstance().createFKMandantcnr();
    panelQueryFLRAuftrag = AuftragFilterFactory.getInstance().createPanelFLRAuftrag(
        getInternalFrame(), true, true, null);
    if (auftragDto != null) {
      panelQueryFLRAuftrag.setSelectedId(auftragDto.getIId());
    }
    new DialogQuery(panelQueryFLRAuftrag);
  }


  /**
   * Dialogfenster zur Auftragauswahl.
   *
   * @param e ActionEvent
   * @throws Throwable
   */
  private void dialogQueryAuftragposition(ActionEvent e)
      throws Throwable {
    QueryType[] qtPositionen = null;
    FilterKriterium[] filtersPositionen = AuftragFilterFactory.getInstance().
        createFKFlrauftragiid(auftragDto.getIId());

    String[] aWhichButtonIUse = {
        PanelBasis.ACTION_REFRESH,
    };

    panelQueryFLRAuftragposition = new PanelQueryFLR(
        qtPositionen,
        filtersPositionen,
        QueryParameters.UC_ID_AUFTRAGPOSITION,
        aWhichButtonIUse,
        getInternalFrame(),
        LPMain.getInstance().getTextRespectUISPr("title.auftragauswahlliste"));
    if (auftragpositionDto != null) {
      panelQueryFLRAuftragposition.setSelectedId(auftragpositionDto.getIId());
    }
    new DialogQuery(panelQueryFLRAuftragposition);
  }


  private void dialogQueryKostenstelle(ActionEvent e)
      throws Throwable {
    panelQueryFLRKostenstelle = SystemFilterFactory.getInstance().
        createPanelFLRKostenstelle(getInternalFrame(), false, false);
    if (kostenstelleDto != null) {
      panelQueryFLRKostenstelle.setSelectedId(kostenstelleDto.getIId());
    }
    new DialogQuery(panelQueryFLRKostenstelle);
  }


  private void dto2ComponentsElternlos() {
    if (losDtoElternlos != null) {
      wtfElternlosNummer.setText(losDtoElternlos.getCNr());
    }
    else {
      wtfElternlosNummer.setText(null);
    }
  }


  private void dialogQueryElternlos(ActionEvent e)
      throws Throwable {
    panelQueryFLRElternlos = FertigungFilterFactory.getInstance().
        createPanelFLRLose(getInternalFrame(),null, false);
    if (losDtoElternlos != null) {
      panelQueryFLRElternlos.setSelectedId(losDtoElternlos.getIId());
    }
    new DialogQuery(panelQueryFLRElternlos);
  }


}
