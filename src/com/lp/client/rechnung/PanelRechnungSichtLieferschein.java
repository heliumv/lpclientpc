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
package com.lp.client.rechnung;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.math.BigDecimal;
import java.sql.Date;
import java.text.MessageFormat;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JPanel;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.LockStateValue;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.PanelPositionen2;
import com.lp.client.frame.component.PanelPositionenArtikelVerkauf;
import com.lp.client.frame.component.PanelPositionenArtikelVerkaufSNR;
import com.lp.client.frame.component.PositionNumberHelperLieferschein;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.artikel.service.SeriennrChargennrMitMengeDto;
import com.lp.server.auftrag.service.AuftragDto;
import com.lp.server.auftrag.service.AuftragpositionDto;
import com.lp.server.lieferschein.service.LieferscheinpositionDto;
import com.lp.server.lieferschein.service.LieferscheinpositionFac;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MwstsatzDto;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

/**
 * <p>In diesem Detailfenster der Lieferscheinposition werden Daten erfasst bzw.
 * geaendert.</p>
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 * <p>Erstellungsdatum 2004-10-22</p>
 * <p> </p>
 * @author Uli Walch
 * @version $Revision: 1.15 $
 */
public class PanelRechnungSichtLieferschein
    extends PanelPositionen2 {
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

/** Cache for convenience. */
  private TabbedPaneRechnung tpRechnung = null;

  /** Die aktuelle Position. */
  	private LieferscheinpositionDto positionDto = null;
  	private WrapperTextField wtfAuftragNummer = null;
  	private WrapperTextField wtfAuftragProjekt = null;
	private WrapperTextField wtfAuftragBestellnummer = null;


   public PanelRechnungSichtLieferschein(InternalFrame internalFrame,
                                     String add2TitleI, Object key, TabbedPaneRechnung tpRechnung) throws Throwable {
    super(internalFrame, add2TitleI, key, PanelPositionen2.TYP_PANELPREISEINGABE_ARTIKELVERKAUFSNR); // VKPF
    this.tpRechnung = tpRechnung;

	zwController.setPositionNumberHelper(new PositionNumberHelperLieferschein()) ;
	// zwController.setBelegDto(tpRechnung.getRechnungDto()) ;
    
    jbInit();
    initComponents();
    initPanel();
  }

  private void jbInit() throws Throwable {
    // braucht nur refresh, save und aendern
    resetToolsPanel();

    // zusaetzliche buttons
    String[] aWhichButtonIUse = {
        ACTION_UPDATE,
        ACTION_SAVE,
        ACTION_DISCARD};

    enableToolsPanelButtons(aWhichButtonIUse);

    // Statusbar an den unteren Rand des Panels haengen
    iZeile++;
	WrapperLabel wlaAuftrag = new WrapperLabel(LPMain.getTextRespectUISPr("auft.auftrag"));
	wlaAuftrag.setMinimumSize(new Dimension(iSpaltenbreiteArtikelMitGoto,
			Defaults.getInstance().getControlHeight()));
	wlaAuftrag.setPreferredSize(new Dimension(iSpaltenbreiteArtikelMitGoto,
			Defaults.getInstance().getControlHeight()));
	wtfAuftragNummer = new WrapperTextField();
	wtfAuftragNummer.setActivatable(false);
	wtfAuftragNummer.setMinimumSize(new Dimension(80,
			Defaults.getInstance().getControlHeight()));
	wtfAuftragNummer.setPreferredSize(new Dimension(80,
			Defaults.getInstance().getControlHeight()));
	wtfAuftragProjekt = new WrapperTextField();
	wtfAuftragProjekt.setActivatable(false);
	wtfAuftragBestellnummer = new WrapperTextField();
	wtfAuftragBestellnummer.setActivatable(false);
	JPanel jpaAuftrag = new JPanel(new GridBagLayout());
	jpaAuftrag.add(wlaAuftrag,
			new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.BOTH, new Insets(2, 2, 2, 2),
					0, 0));
	jpaAuftrag.add(wtfAuftragNummer,
			new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.BOTH, new Insets(2, 2, 2, 2),
					0, 0));
	jpaAuftrag.add(wtfAuftragProjekt,
			new GridBagConstraints(2, 0, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.BOTH, new Insets(2, 2, 2, 2),
					0, 0));
	jpaAuftrag.add(wtfAuftragBestellnummer,
			new GridBagConstraints(3, 0, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.BOTH, new Insets(2, 2, 2, 2),
					0, 0));
	add(jpaAuftrag, new GridBagConstraints(0, iZeile, 1, 1, 1.0, 0.0
			, GridBagConstraints.CENTER,
			GridBagConstraints.BOTH,
			new Insets(0, 0, 0, 0), 0, 0));

    // Statusbar an den unteren Rand des Panels haengen
    iZeile++;
    add(getPanelStatusbar(), new GridBagConstraints(0, iZeile, 1, 1, 1.0, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.BOTH,
        new Insets(0, 0, 0, 0), 0, 0));

    panelArtikel.wlaMenge.setText(LPMain.getTextRespectUISPr("ls.label.mengels"));
    panelArtikel.wlaMenge.setMaximumSize(new Dimension(iSpaltenbreiteArtikelMitGoto,
        Defaults.getInstance().getControlHeight()));
    panelArtikel.wlaMenge.setMinimumSize(new Dimension(iSpaltenbreiteArtikelMitGoto,
        Defaults.getInstance().getControlHeight()));
    panelArtikel.wlaMenge.setPreferredSize(new Dimension(iSpaltenbreiteArtikelMitGoto,
        Defaults.getInstance().getControlHeight()));

    // UW 21.06.06 keine VKPF
    panelArtikel.wnfMenge.removeFocusListener(( (PanelPositionenArtikelVerkauf) panelArtikel).wnfMengeFocusListener);
    
	

  }


  private void initPanel()
      throws Throwable {
    // combobox Positionen in der UI Sprache des Benutzers fuellen
    setPositionsarten(DelegateFactory.getInstance().getLieferscheinServiceDelegate().
        getLieferscheinpositionart(LPMain.getTheClient().getLocUi()));
  }


  protected void setDefaults()
      throws Throwable {
    positionDto = new LieferscheinpositionDto();

    leereAlleFelder(this);

    // default positionsart ist ident
    wcoPositionsart.setKeyOfSelectedItem(LieferscheinpositionFac.LIEFERSCHEINPOSITIONSART_IDENT);

    super.setDefaults();

    // der Vorschlagswert fuer eine frei erfasste Position ist 1
    panelArtikel.wnfMenge.setDouble(new Double(1));

    // dem panelArtikel muss das Lager und der Kunde gesetzt werden
    ((PanelPositionenArtikelVerkaufSNR) panelArtikel).setKundeDto(tpRechnung.getKundeLieferadresseDto());
    ((PanelPositionenArtikelVerkaufSNR) panelArtikel).setIIdLager(tpRechnung.getLieferscheinDto().getLagerIId());

    if (tpRechnung.getLieferscheinDto() != null && tpRechnung.getLieferscheinDto().getIId() != null) {
      // alle lieferscheinabhaengigen Defaults in den verschiedenen PanelPositionen setzen
      panelHandeingabe.setWaehrungCNr(tpRechnung.getLieferscheinDto().
    		  getWaehrungCNr());
      panelArtikel.setWaehrungCNr(tpRechnung.getLieferscheinDto().getWaehrungCNr());

      // im PanelArtikel alles fuer die VKPF vorbereiten
      ((PanelPositionenArtikelVerkauf) panelArtikel).setKundeDto(tpRechnung.getKundeLieferadresseDto());
      ((PanelPositionenArtikelVerkauf) panelArtikel).setCNrWaehrung(tpRechnung.getLieferscheinDto().getWaehrungCNr());
      ((PanelPositionenArtikelVerkauf) panelArtikel).setWechselkurs(tpRechnung.getLieferscheinDto().getFWechselkursmandantwaehrungzubelegwaehrung());
      ((PanelPositionenArtikelVerkauf) panelArtikel).setGueltigkeitsdatumArtikeleinzelverkaufspreis(new Date(tpRechnung.getLieferscheinDto().
        getTBelegdatum().getTime()));
      ((PanelPositionenArtikelVerkauf) panelArtikel).setIIdLager(
          tpRechnung.getLieferscheinDto().getLagerIId());

      // dem panelArtikel muss die momentan eingetragene Menge gesetzt werden -> fuer Chargennummern
      ((PanelPositionenArtikelVerkaufSNR) panelArtikel).setDDBisherigeMenge(new Double(0));
    }

    // Wenn der Vorschlagswert fuer den Mwstsatz aus dem Belegkunden kommt
    if (!panelHandeingabe.getBDefaultMwstsatzAusArtikel()) {
      if (tpRechnung.getKundeLieferadresseDto() != null &&
          tpRechnung.getKundeLieferadresseDto().getIId() != null) {
        // Aktuellen MWST-Satz uebersetzen.
        MwstsatzDto mwstsatzDtoAktuell = DelegateFactory.getInstance().getMandantDelegate().
            mwstsatzFindByMwstsatzbezIIdAktuellster(tpRechnung.getKundeLieferadresseDto().
            getMwstsatzbezIId());
        panelHandeingabe.wcoMwstsatz.setKeyOfSelectedItem(mwstsatzDtoAktuell.getIId());
      }
    }
  }

   
  public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
      throws Throwable {
    super.eventYouAreSelected(false);
    setTBelegdatumMwstsatz(tpRechnung.getRechnungDto().getTBelegdatum());
    // den gesamten Inhalt zuruecksetzen, ausloeser war ev. ein discard
    setDefaults();
    // position neu einlesen, ausloeser war ev. ein refresh
    Object pkPosition = getKeyWhenDetailPanel();

    // wenn es eine Position anzuzeigen gibt, dann anzeigen
    if (pkPosition != null && !pkPosition.equals(LPMain.getLockMeForNew())) {
      positionDto = DelegateFactory.getInstance().getLieferscheinpositionDelegate().
          lieferscheinpositionFindByPrimaryKey( (Integer) pkPosition);
      dto2Components();

      // dem panelArtikel muss die momentan eingetragene Menge gesetzt werden -> fuer Chargennummern
      if (positionDto.getNMenge() != null) {
        ( (PanelPositionenArtikelVerkaufSNR) panelArtikel).setDDBisherigeMenge(new Double(
            positionDto.getNMenge().doubleValue()));
      }
    }
    aktualisiereStatusbar();
  }

  protected void eventActionDiscard(ActionEvent e) throws Throwable {
    super.eventActionDiscard(e);

    panelArtikel.setArtikelEingabefelderEditable(false);
    ( (PanelPositionenArtikelVerkauf) panelArtikel).wbuPreisauswahl.setEnabled(false);
  }

  /**
   * Alle Positionsdaten aus einem dto ins Panel setzen.
   * @throws Throwable
   */
  protected void dto2Components()
      throws Throwable {
	  
	  zwController.setBelegDto(tpRechnung.getLieferscheinDto()) ;
	  zwController.setBelegPositionDto(positionDto) ;
	  
    // 1. Behandlung der trivialen Positionsarten.
    super.dto2Components(positionDto,
                         tpRechnung.getKundeDto().getPartnerDto().getLocaleCNrKommunikation());
    // 2. Weiter mit den anderen.
    String positionsart = positionDto.getPositionsartCNr();

    if (positionsart.equalsIgnoreCase(LocaleFac.POSITIONSART_IDENT)) {
      // Serien/Chargennummern.
      ( (PanelPositionenArtikelVerkaufSNR) panelArtikel).zeigeSerienchargennummer(false, false);
      ( (PanelPositionenArtikelVerkaufSNR) panelArtikel).wtfSerienchargennummer.setSeriennummern(
          positionDto.getSeriennrChargennrMitMenge(),positionDto.getArtikelIId(),tpRechnung.getRechnungDto().getLagerIId());
    }
//    else if (positionsart.equalsIgnoreCase(LocaleFac.POSITIONSART_HANDEINGABE)) {
//      // zzt. nicht spezifisches.
//    }
    
	// Auftrag anzeigen
	if (positionDto.getAuftragpositionIId() != null) {
		AuftragpositionDto abPos = DelegateFactory.getInstance().getAuftragpositionDelegate().
		auftragpositionFindByPrimaryKey(positionDto.getAuftragpositionIId());
		AuftragDto auftragDto = DelegateFactory.getInstance().getAuftragDelegate().
		auftragFindByPrimaryKey(abPos.getBelegIId());
		wtfAuftragNummer.setText(auftragDto.getCNr());
		wtfAuftragProjekt.setText(auftragDto.getCBezProjektbezeichnung());
		wtfAuftragBestellnummer.setText(auftragDto.getCBestellnummer());
	}
	else {
		wtfAuftragNummer.setText(null);
		wtfAuftragProjekt.setText(null);
		wtfAuftragBestellnummer.setText(null);
	}

  }


  protected void eventActionUpdate(ActionEvent aE, boolean bNeedNoUpdateI) throws
      Throwable {
    if ( ( (InternalFrameRechnung) getInternalFrame()).isUpdateAllowedForRechnungDto(
        tpRechnung.getRechnungDto())) {
      super.eventActionUpdate(aE, false);
      // ausser den Preisfeldern darf nichts editierbar sein
      wcoPositionsart.setEnabled(false);

      panelArtikel.wbuArtikelauswahl.setEnabled(false);
      panelArtikel.getWtfArtikel().setEditable(false);
      panelArtikel.wtfBezeichnung.setEditable(false);
      panelArtikel.wnfMenge.setEditable(false);
      panelArtikel.wcoEinheit.setEnabled(false);

      ( (PanelPositionenArtikelVerkauf) panelArtikel).wbuPreisauswahl.setEnabled(true);
      ( (PanelPositionenArtikelVerkauf) panelArtikel).wnfNettopreis.setEditable(true);
      ( (PanelPositionenArtikelVerkaufSNR) panelArtikel).zeigeSerienchargennummer(false, false);

      panelHandeingabe.wtfBezeichnung.setEditable(false);
      panelHandeingabe.wtfZusatzbezeichnung.setEditable(false);
      panelHandeingabe.wnfMenge.setEditable(false);
      panelHandeingabe.wcoEinheit.setEnabled(false);

      panelTexteingabe.getLpEditor().setEditable(false);
      panelTexteingabe.getLpEditor().showAlignmentItems(false);
      panelTexteingabe.getLpEditor().showFileItems(false);
      panelTexteingabe.getLpEditor().showFontStyleItems(false);
      panelTexteingabe.getLpEditor().showMenu(false);
      panelTexteingabe.getLpEditor().showStatusBar(false);
      panelTexteingabe.getLpEditor().showTableItems(false);
      panelTexteingabe.getLpEditor().showTabRuler(false);
      panelTexteingabe.getLpEditor().showToolBar(false);

      panelBetreff.wtfBetreff.setEditable(false);

      panelUrsprung.wtfUrsprung.setEditable(false);
    }
  }


  public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI) throws
      Throwable {
    try {
      calculateFields();

      if (allMandatoryFieldsSetDlg()) {
        boolean bDiePositionSpeichern = true;
        // die folgenden beiden Felder koennten durch eine Berechnung zu gross fuer die DB sein
        bDiePositionSpeichern = HelperClient.checkNumberFormat(panelArtikel.wnfNettopreis.
            getBigDecimal());
        if (bDiePositionSpeichern) {
          bDiePositionSpeichern = HelperClient.checkNumberFormat(panelArtikel.wnfBruttopreis.
              getBigDecimal());
        }


        if (bDiePositionSpeichern) {
          if (getPositionsartCNr().equals(LocaleFac.POSITIONSART_IDENT) &&
              Helper.short2boolean(panelArtikel.getArtikelDto().getBLagerbewirtschaftet())) {

            if (panelArtikel.wnfMenge.getBigDecimal().doubleValue() == 0) {
              DialogFactory.showModalDialog(
                  LPMain.getTextRespectUISPr("lp.warning"),
                  LPMain.getTextRespectUISPr("lp.warning.nullmengenichterlaubt"));

              bDiePositionSpeichern = false;
            }
            else

            if (panelArtikel.wnfMenge.getBigDecimal().doubleValue() < 0) {
              DialogFactory.showModalDialog(
                  LPMain.getTextRespectUISPr("lp.warning"),
                  LPMain.getTextRespectUISPr("lp.warning.negativemengenichterlaubt"));

              bDiePositionSpeichern = false;
            }
            else

            if (panelArtikel.getArtikelDto().getIId() != null && Helper.short2boolean(panelArtikel.getArtikelDto().getBChargennrtragend())) {
              String[] aChargennummern = new String[0];

              if ( ( ( (PanelPositionenArtikelVerkaufSNR) panelArtikel).wtfSerienchargennummer.
                    getText() != null &&
                    ( (PanelPositionenArtikelVerkaufSNR) panelArtikel).wtfSerienchargennummer.
                    getText().
                    length() > 0)) {
                aChargennummern = Helper.erzeugeStringArrayAusString( ( (
                    PanelPositionenArtikelVerkaufSNR) panelArtikel).wtfSerienchargennummer.
                    getText());
              }

              BigDecimal ddMengeAufLager = DelegateFactory.getInstance().getLagerDelegate().
                  getMengeMehrererSeriennummernChargennummernAufLager(
                      panelArtikel.getArtikelDto().getIId(),
                      tpRechnung.getLieferscheinDto().getLagerIId(),
                      ( (PanelPositionenArtikelVerkaufSNR) panelArtikel).wtfSerienchargennummer.
                      getSeriennummern());
            }
            else

            // ueberpruefen von eingegebener Menge und Anzahl Seriennummern
            if (panelArtikel.getArtikelDto().getIId() != null && Helper.short2boolean(panelArtikel.getArtikelDto().getBSeriennrtragend())) {
              String[] aSeriennummern = new String[0];

              if ( ( ( (PanelPositionenArtikelVerkaufSNR) panelArtikel).wtfSerienchargennummer.
                    getText() != null &&
                    ( (PanelPositionenArtikelVerkaufSNR) panelArtikel).wtfSerienchargennummer.
                    getText().
                    length() > 0)) {
                aSeriennummern = Helper.erzeugeStringArrayAusString( ( (
                    PanelPositionenArtikelVerkaufSNR) panelArtikel).wtfSerienchargennummer.
                    getText());
              }

              if (aSeriennummern.length != panelArtikel.wnfMenge.getInteger()) {
                MessageFormat mf = new MessageFormat(
                    LPMain.getTextRespectUISPr("lp.hint.seriennummer"));

                mf.setLocale(LPMain.getTheClient().getLocUi());

                Object pattern[] = {
                    panelArtikel.wnfMenge.getInteger(),
                    new Integer(aSeriennummern.length)
                };

                DialogFactory.showModalDialog(
                    LPMain.getTextRespectUISPr("lp.warning"), mf.format(pattern));

                bDiePositionSpeichern = false;
              }
            }
          }
        }

        if (bDiePositionSpeichern) {
          if (getPositionsartCNr().equalsIgnoreCase(LieferscheinpositionFac.
                                                    LIEFERSCHEINPOSITIONSART_IDENT)) {
            // auf Unterpreisigkeit pruefen
            bDiePositionSpeichern = DialogFactory.pruefeUnterpreisigkeitDlg(
                getInternalFrame(),
                panelArtikel.getArtikelDto().getIId(),
                tpRechnung.getLieferscheinDto().getLagerIId(),
                panelArtikel.wnfNettopreis.getBigDecimal(),
                tpRechnung.getLieferscheinDto().
                getFWechselkursmandantwaehrungzubelegwaehrung(),
                panelArtikel.wnfMenge.getBigDecimal());
          }
        }

        if (bDiePositionSpeichern) {
          BigDecimal bdAktuelleMengeImLieferschein = panelArtikel.wnfMenge.getBigDecimal();
          BigDecimal bdBisherigeMengeImLieferschein = positionDto.getNMenge();
          BigDecimal bdWievielBraucheIchVomLager = new BigDecimal(0); // wenn die beiden Mengen gleich sind oder die neue Menge unter der alten Menge liegt

          if (bdBisherigeMengeImLieferschein != null &&
              bdBisherigeMengeImLieferschein.doubleValue() > 0) {
            bdWievielBraucheIchVomLager = bdAktuelleMengeImLieferschein.subtract(
                bdBisherigeMengeImLieferschein);
          }
          else {
            bdWievielBraucheIchVomLager = bdAktuelleMengeImLieferschein;
          }

          // Menge auf Lager pruefen
          if (getPositionsartCNr().equals(LocaleFac.POSITIONSART_IDENT) &&
              Helper.short2boolean(panelArtikel.getArtikelDto().getBLagerbewirtschaftet())) {

            if (!Helper.short2boolean(panelArtikel.getArtikelDto().getBSeriennrtragend()) &&
                !Helper.short2boolean(panelArtikel.getArtikelDto().getBChargennrtragend())) {
              if (bdWievielBraucheIchVomLager.doubleValue() > 0) {

                bDiePositionSpeichern = tpRechnung.istMengeAufLager(
                    panelArtikel.getArtikelDto().getIId(),
                    tpRechnung.getLieferscheinDto().getLagerIId(),
                    ( (PanelPositionenArtikelVerkaufSNR) panelArtikel).wtfSerienchargennummer.
                    getSeriennummern(),
                    bdWievielBraucheIchVomLager);

                if (!bDiePositionSpeichern) {

                  // bei einer neuen Position als Vorschlagswert die moegliche Menge setzen
                  if (positionDto.getIId() == null) {
                    panelArtikel.wnfMenge.setBigDecimal(DelegateFactory.getInstance().getLagerDelegate().
                                                    getMengeAufLager(
                                                        panelArtikel.getArtikelDto().getIId(),
                                                        tpRechnung.getLieferscheinDto().
                                                        getLagerIId(),
                                                        null));
                  }

                  // bei einer bestehenden Position auf den urspruenglichen Wert zuruecksetzen
                  else {
                    panelArtikel.wnfMenge.setBigDecimal(positionDto.getNMenge());
                  }
                }
              }
            }
          }

          if (bDiePositionSpeichern) {
            components2Dto();
           
			List<SeriennrChargennrMitMengeDto> bekannteSnrs = 
					DelegateFactory.getInstance()
						.getLieferscheinpositionDelegate()
						.getSeriennrchargennrForArtikelsetPosition(positionDto.getIId()) ;
			
			List<SeriennrChargennrMitMengeDto> snrs = getArtikelsetViewController()
					.handleArtikelsetSeriennummern(
							tpRechnung.getLieferscheinDto().getLagerIId(),
									positionDto, bekannteSnrs);

			if (!getArtikelsetViewController().isArtikelsetWithSnrsStoreable(
					positionDto, snrs)) {
				bDiePositionSpeichern = false;
			}
			
            if(bDiePositionSpeichern) {
            	DelegateFactory.getInstance().getLieferscheinpositionDelegate().
                	updateLieferscheinpositionAusRechnung(positionDto, 
                                tpRechnung.getRechnungPositionDto().getIId(), snrs);

            	// buttons schalten
            	super.eventActionSave(e, false);
            	eventYouAreSelected(false);
            }
          }
        }
      }
    }
    finally {
      // per Default wird eine neue Position ans Ende der Liste gesetzt
      bFuegeNeuePositionVorDerSelektiertenEin = false;
    }
  }


  protected void eventActionDelete(ActionEvent e, boolean bAdministrateLockKeyI,
                                   boolean bNeedNoDeleteI)
      throws Throwable {
    // Loeschen ist hier nicht vorgesehen
    super.eventActionDelete(e, false, false);
  }


  protected void components2Dto()
      throws Throwable {
    // 1. Behandlung der trivialen Positionsarten.
    super.components2Dto(positionDto,
                         tpRechnung.getKundeDto().getPartnerDto().getLocaleCNrKommunikation(),
                         null); // keine ID, da keine neuen Positionen.
    // 2. Weiter mit den anderen.

    String positionsart = positionDto.getLieferscheinpositionartCNr();

    // hier werden nur preisrelevante felder ueberschrieben, damit nix schiefgeht
    if (positionsart.equalsIgnoreCase(LieferscheinpositionFac.
                                      LIEFERSCHEINPOSITIONSART_IDENT)) {
      // hier zusaetzlich gespeicherte Betraege.
      positionDto.setNRabattbetrag(panelArtikel.wnfRabattsumme.getBigDecimal());
      positionDto.setNMwstbetrag(panelArtikel.wnfMwstsumme.getBigDecimal());
    }
    else if (positionsart.equalsIgnoreCase(LieferscheinpositionFac.
                                           LIEFERSCHEINPOSITIONSART_HANDEINGABE)) {
      // hier zusaetzlich gespeicherte Betraege.
      positionDto.setNRabattbetrag(panelHandeingabe.wnfRabattsumme.getBigDecimal());
      positionDto.setNMwstbetrag(panelHandeingabe.wnfMwstsumme.getBigDecimal());
    }
  }

  private void aktualisiereStatusbar() throws Throwable {
    setStatusbarPersonalIIdAnlegen(tpRechnung.getLieferscheinDto().
                                   getPersonalIIdAnlegen());
    setStatusbarTAnlegen(tpRechnung.getLieferscheinDto().getTAnlegen());
    setStatusbarPersonalIIdAendern(tpRechnung.getLieferscheinDto().
                                   getPersonalIIdAendern());
    setStatusbarTAendern(tpRechnung.getLieferscheinDto().getTAendern());
    setStatusbarStatusCNr(tpRechnung.getLieferscheinDto().getStatusCNr());

    // das Lager des Lieferscheins und Lagerstand des aktuellen Artikel
    String lagerinfo = DelegateFactory.getInstance().getLagerDelegate().lagerFindByPrimaryKey(
        tpRechnung.getLieferscheinDto().getLagerIId()).getCNr();

    String serienchargennummer = null;
 
    if ( ( (PanelPositionenArtikelVerkaufSNR) panelArtikel).wtfSerienchargennummer.getSeriennummern() != null &&
        ( (PanelPositionenArtikelVerkaufSNR) panelArtikel).wtfSerienchargennummer.getSeriennummern().
        size() > 0) {
      serienchargennummer = ( (PanelPositionenArtikelVerkaufSNR) panelArtikel).
          wtfSerienchargennummer.getSeriennummern().get(0).getCSeriennrChargennr();
    }

    if (panelArtikel.getArtikelDto().getIId() != null
        && panelArtikel.getArtikelDto().getIId() != null
        && panelArtikel.wnfMenge.getDouble().doubleValue() > 0) {
      BigDecimal ddMenge = DelegateFactory.getInstance().getLagerDelegate().getMengeAufLager(
          panelArtikel.getArtikelDto().getIId(),
          tpRechnung.getLieferscheinDto().getLagerIId(),
          serienchargennummer);

      lagerinfo += ": ";
      lagerinfo += ddMenge;
    }

    setStatusbarSpalte5(lagerinfo);
  }


  protected String getLockMeWer() throws Exception {
    return HelperClient.LOCKME_RECHNUNG;
  }


  public LockStateValue getLockedstateDetailMainKey()
      throws Throwable {
    return super.getLockedstateDetailMainKey();
  }

  public boolean handleOwnException(ExceptionLP exfc) {
    boolean bErrorErkannt = true;

    switch (exfc.getICode()) {
      case EJBExceptionLP.FEHLER_STATUS:
        DialogFactory.showModalDialog(
            LPMain.getTextRespectUISPr("lp.error"),
            LPMain.getTextRespectUISPr("lp.error.status"));
        break;

      default:
        bErrorErkannt = false;
        break;
    }

    return bErrorErkannt;
  }


  protected JComponent getFirstFocusableComponent()
      throws Exception {
    return wcoPositionsart;
  }
}
