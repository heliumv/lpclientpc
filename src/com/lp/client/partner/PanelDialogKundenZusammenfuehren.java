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
package com.lp.client.partner;


import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.DialogError;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.LockStateValue;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelDialog;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperButtonZusammenfuehren;
import com.lp.client.frame.component.WrapperCheckBoxZusammenfuehren;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperTextAreaZusammenfuehren;
import com.lp.client.frame.component.WrapperTextFieldZusammenfuehren;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.artikel.service.VkpfartikelpreislisteDto;
import com.lp.server.finanz.service.KontoDto;
import com.lp.server.finanz.service.KontoDtoSmall;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.partner.service.KundesokoDto;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.personal.service.PersonalDto;
import com.lp.server.system.service.KostenstelleDto;
import com.lp.server.system.service.SpediteurDto;
import com.lp.server.system.service.ZahlungszielDto;
import com.lp.util.Helper;

@SuppressWarnings("static-access")
/**
 * <p> Diese Klasse kuemmert sich um das Zusammenf&uuml;hren von Kunden</p>
 *
 * <p>Copyright Logistik Pur Software GmbH (c) 2004, 2005, 2006</p>
 *
 * <p>Erstellung: Vorname Nachname; 01.04.08</p>
 *
 * <p>@author $Author: christian $</p>
 *
 * @version not attributable Date $Date: 2012/09/14 08:09:47 $
 */
public class PanelDialogKundenZusammenfuehren
    extends PanelDialog
{
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

public static final String ACTION_SPECIAL_SAVE = "action_" + ALWAYSENABLED + "save";

  private Integer iKundensokoSelectedKundeIId = null;
  private static final String sKundesokoNullEintraege = "0 " + LPMain.getInstance().getTextRespectUISPr("part.kunde.zusammenfuehre.eintraege");

  private final String ACTION_QUELLKUNDE_AUSWAEHLEN = "ACTION_QUELLKUNDE_AUSWAEHLEN";
  private final String ACTION_ZIELKUNDE_AUSWAEHLEN = "ACTION_ZIELKUNDE_AUSWAEHLEN";
  private final String ACTION_ALLE_DATENRICHTUNGEN_WAEHLEN = "ACTION_ALLE_DATENRICHTUNGEN_WAEHLEN";
  private final String ACTION_DATENRICHTUNG_WAEHLEN = "ACTION_DATENRICHTUNG_WAEHLEN";
  private final int MAX_KUNDENKOMMENTAR = 3000; // ntext 16 - wie gross ist die max. anzahl an zeichen?

  // mind. alle Texte, die nvarchar > 40 haben, hier definieren bzw. Felder, deren Inhalt kombiniert werden kann
  private final int MAX_HINWEISEXTERN = 80;
  private final int MAX_HINWEISINTERN = 80;


  private JButton saveButton = null;

  // in diesen vector schreiben wir die wrapperbuttonzusammenfuehren, die kundendetails beinhalten
  private Vector<WrapperButtonZusammenfuehren> vWrapperButtonsZusammenfuehren = new Vector<WrapperButtonZusammenfuehren> ();
  private Vector<JComponent> vZielfelder = new Vector<JComponent>();
  private Vector<JComponent> vQuellfelder = new Vector<JComponent>();

  private KundeDto kundeZielDto = null;
  private KundeDto kundeQuellDto = null;
  // soll der Partner des rechten Kunden genommen werden, so steht diese PartnerIId in iKundePartnerIId
  private Integer iKundePartnerIId = null;

  private PanelQueryFLR panelQueryKundeFlr = null;
  protected boolean bHandleEventInSuperklasse = true;

  protected JPanel jpaWorkingOnScroll = null;
  private JScrollPane jScrollPaneKunde = null;

  private WrapperButton wbuZielKundeAuswaehlen = null;
  private WrapperButton wbuQuellKundeAuswaehlen = null;

  private WrapperButtonZusammenfuehren wbuAuswahlReset = null;

  // Partnername
  private WrapperLabel wlaPartnername = null;
  private WrapperButtonZusammenfuehren wbuPartnernameDatenrichtung = null;
  private WrapperTextFieldZusammenfuehren wtfZielKundePartnername = null;
  private WrapperTextFieldZusammenfuehren wtfQuellKundePartnername = null;

  /* MandantCNr
    - nicht noetig, da beim manuellen Zusammenfuehren nur Kunden auf dem selben
     Mandanten angezeigt werden
   */

  // Mehrwertsteuer
  private WrapperLabel wlaMwst = null;
  private WrapperButtonZusammenfuehren wbuMwstDatenrichtung = null;
  private WrapperTextFieldZusammenfuehren wtfZielKundeMwst = null;
  private WrapperTextFieldZusammenfuehren wtfQuellKundeMwst = null;

  // Waehrung
  private WrapperLabel wlaWaehrung = null;
  private WrapperButtonZusammenfuehren wbuWaehrungDatenrichtung = null;
  private WrapperTextFieldZusammenfuehren wtfZielKundeWaehrung = null;
  private WrapperTextFieldZusammenfuehren wtfQuellKundeWaehrung = null;

  // Lieferart
  private WrapperLabel wlaLieferart = null;
  private WrapperButtonZusammenfuehren wbuLieferartDatenrichtung = null;
  private WrapperTextFieldZusammenfuehren wtfZielKundeLieferart = null;
  private WrapperTextFieldZusammenfuehren wtfQuellKundeLieferart = null;

  // Spediteur
  private WrapperLabel wlaSpediteur = null;
  private WrapperButtonZusammenfuehren wbuSpediteurDatenrichtung = null;
  private WrapperTextFieldZusammenfuehren wtfZielKundeSpediteur = null;
  private WrapperTextFieldZusammenfuehren wtfQuellKundeSpediteur = null;

  // Zahlungsziel
  private WrapperLabel wlaZahlungsziel = null;
  private WrapperButtonZusammenfuehren wbuZahlungszielDatenrichtung = null;
  private WrapperTextFieldZusammenfuehren wtfZielKundeZahlungsziel = null;
  private WrapperTextFieldZusammenfuehren wtfQuellKundeZahlungsziel = null;

  // Kostenstelle
  private WrapperLabel wlaKostenstelle = null;
  private WrapperButtonZusammenfuehren wbuKostenstelleDatenrichtung = null;
  private WrapperTextFieldZusammenfuehren wtfZielKundeKostenstelle = null;
  private WrapperTextFieldZusammenfuehren wtfQuellKundeKostenstelle = null;

  // Provisionsempfaenger - kunde - kond. - personal_i_id_bekommeprovision
  private WrapperLabel wlaProvisionsempfaenger = null;
  private WrapperButtonZusammenfuehren wbuProvisionsempfaengerDatenrichtung = null;
  private WrapperTextFieldZusammenfuehren wtfZielKundeProvisionsempfaenger = null;
  private WrapperTextFieldZusammenfuehren wtfQuellKundeProvisionsempfaenger = null;

  // Rabattsatz
  private WrapperLabel wlaRabattsatz = null;
  private WrapperButtonZusammenfuehren wbuRabattsatzDatenrichtung = null;
  private WrapperTextFieldZusammenfuehren wtfZielKundeRabattsatz = null;
  private WrapperTextFieldZusammenfuehren wtfQuellKundeRabattsatz = null;

  // Garantie
  private WrapperLabel wlaGarantie = null;
  private WrapperButtonZusammenfuehren wbuGarantieDatenrichtung = null;
  private WrapperTextFieldZusammenfuehren wtfZielKundeGarantie = null;
  private WrapperTextFieldZusammenfuehren wtfQuellKundeGarantie = null;

  // XKommentar
  private WrapperLabel wlaKommentar = null;
  private WrapperButtonZusammenfuehren wbuKommentarDatenrichtung = null;
  private WrapperTextAreaZusammenfuehren wtaZielKundeKommentar = null;
  private WrapperTextAreaZusammenfuehren wtaQuellKundeKommentar = null;
  private JScrollPane jScrollPaneZielKundeKommentar = null;
  private JScrollPane jScrollPaneQuellKundeKommentar = null;

  // Kurznr
  private WrapperLabel wlaKurznr = null;
  private WrapperButtonZusammenfuehren wbuKurznrDatenrichtung = null;
  private WrapperTextFieldZusammenfuehren wtfZielKundeKurznr = null;
  private WrapperTextFieldZusammenfuehren wtfQuellKundeKurznr = null;

  // Kreditlimit
  private WrapperLabel wlaKreditlimit = null;
  private WrapperButtonZusammenfuehren wbuKreditlimitDatenrichtung = null;
  private WrapperTextFieldZusammenfuehren wtfZielKundeKreditlimit = null;
  private WrapperTextFieldZusammenfuehren wtfQuellKundeKreditlimit = null;

  // letzte Bonitaetspruefung
  private WrapperLabel wlaBonitaetspruefung = null;
  private WrapperButtonZusammenfuehren wbuBonitaetspruefungDatenrichtung = null;
  private WrapperTextFieldZusammenfuehren wtfZielKundeBonitaetspruefung = null;
  private WrapperTextFieldZusammenfuehren wtfQuellKundeBonitaetspruefung = null;

  // Liefersperre
  private WrapperLabel wlaLiefersperre = null;
  private WrapperButtonZusammenfuehren wbuLiefersperreDatenrichtung = null;
  private WrapperTextFieldZusammenfuehren wtfZielKundeLiefersperre = null;
  private WrapperTextFieldZusammenfuehren wtfQuellKundeLiefersperre = null;

  // Rechnungskopien
  private WrapperLabel wlaRechnungskopien = null;
  private WrapperButtonZusammenfuehren wbuRechnungskopienDatenrichtung = null;
  private WrapperTextFieldZusammenfuehren wtfZielKundeRechnungskopien = null;
  private WrapperTextFieldZusammenfuehren wtfQuellKundeRechnungskopien = null;

  // Lieferscheinkopien
  private WrapperLabel wlaLieferscheinkopien = null;
  private WrapperButtonZusammenfuehren wbuLieferscheinkopienDatenrichtung = null;
  private WrapperTextFieldZusammenfuehren wtfZielKundeLieferscheinkopien = null;
  private WrapperTextFieldZusammenfuehren wtfQuellKundeLieferscheinkopien = null;

  // Mitarbeiter
  private WrapperLabel wlaMitarbeiter = null;
  private WrapperButtonZusammenfuehren wbuMitarbeiterDatenrichtung = null;
  private WrapperTextFieldZusammenfuehren wtfZielKundeMitarbeiter = null;
  private WrapperTextFieldZusammenfuehren wtfQuellKundeMitarbeiter = null;

  // Tour
  private WrapperLabel wlaTour = null;
  private WrapperButtonZusammenfuehren wbuTourDatenrichtung = null;
  private WrapperTextFieldZusammenfuehren wtfZielKundeTour = null;
  private WrapperTextFieldZusammenfuehren wtfQuellKundeTour = null;

  // Lieferantennr
  private WrapperLabel wlaLieferantennr = null;
  private WrapperButtonZusammenfuehren wbuLieferantennrDatenrichtung = null;
  private WrapperTextFieldZusammenfuehren wtfZielKundeLieferantennr = null;
  private WrapperTextFieldZusammenfuehren wtfQuellKundeLieferantennr = null;

  // Abc
  private WrapperLabel wlaAbc = null;
  private WrapperButtonZusammenfuehren wbuAbcDatenrichtung = null;
  private WrapperTextFieldZusammenfuehren wtfZielKundeAbc = null;
  private WrapperTextFieldZusammenfuehren wtfQuellKundeAbc = null;

  // Agb
  private WrapperLabel wlaAgb = null;
  private WrapperButtonZusammenfuehren wbuAgbDatenrichtung = null;
  private WrapperTextFieldZusammenfuehren wtfZielKundeAgb = null;
  private WrapperTextFieldZusammenfuehren wtfQuellKundeAgb = null;

  // Fremdsystemnr
  private WrapperLabel wlaFremdsystemnr = null;
  private WrapperButtonZusammenfuehren wbuFremdsystemnrDatenrichtung = null;
  private WrapperTextFieldZusammenfuehren wtfZielKundeFremdsystemnr = null;
  private WrapperTextFieldZusammenfuehren wtfQuellKundeFremdsystemnr = null;

  // Erloeskonto
  private WrapperLabel wlaErloeskonto = null;
  private WrapperButtonZusammenfuehren wbuErloeskontoDatenrichtung = null;
  private WrapperTextFieldZusammenfuehren wtfZielKundeErloeskonto = null;
  private WrapperTextFieldZusammenfuehren wtfQuellKundeErloeskonto = null;

  // Debitorenkonto
  private WrapperLabel wlaDebitorenkonto = null;
  private WrapperButtonZusammenfuehren wbuDebitorenkontoDatenrichtung = null;
  private WrapperTextFieldZusammenfuehren wtfZielKundeDebitorenkonto = null;
  private WrapperTextFieldZusammenfuehren wtfQuellKundeDebitorenkonto = null;

  // Rechnungsadresspartner
  private WrapperLabel wlaRechnungsadresspartner = null;
  private WrapperButtonZusammenfuehren wbuRechnungsadresspartnerDatenrichtung = null;
  private WrapperTextFieldZusammenfuehren wtfZielKundeRechnungsadresspartner = null;
  private WrapperTextFieldZusammenfuehren wtfQuellKundeRechnungsadresspartner = null;

  // HinweisInt
  private WrapperLabel wlaHinweisInt = null;
  private WrapperButtonZusammenfuehren wbuHinweisIntDatenrichtung = null;
  private WrapperTextFieldZusammenfuehren wtfZielKundeHinweisInt = null;
  private WrapperTextFieldZusammenfuehren wtfQuellKundeHinweisInt = null;

  // HinweisExt
  private WrapperLabel wlaHinweisExt = null;
  private WrapperButtonZusammenfuehren wbuHinweisExtDatenrichtung = null;
  private WrapperTextFieldZusammenfuehren wtfZielKundeHinweisExt = null;
  private WrapperTextFieldZusammenfuehren wtfQuellKundeHinweisExt = null;

  // Zessionsfaktor
  private WrapperLabel wlaZessionsfaktor = null;
  private WrapperButtonZusammenfuehren wbuZessionsfaktorDatenrichtung = null;
  private WrapperTextFieldZusammenfuehren wtfZielKundeZessionsfaktor = null;
  private WrapperTextFieldZusammenfuehren wtfQuellKundeZessionsfaktor = null;

  // Preisliste
  private WrapperLabel wlaPreisliste = null;
  private WrapperButtonZusammenfuehren wbuPreislisteDatenrichtung = null;
  private WrapperTextFieldZusammenfuehren wtfZielKundePreisliste = null;
  private WrapperTextFieldZusammenfuehren wtfQuellKundePreisliste = null;

  // Kundennr
  private WrapperLabel wlaKundennr = null;
  private WrapperButtonZusammenfuehren wbuKundennrDatenrichtung = null;
  private WrapperTextFieldZusammenfuehren wtfZielKundeKundennr = null;
  private WrapperTextFieldZusammenfuehren wtfQuellKundeKundennr = null;

  // Kundensoko
  private WrapperLabel wlaKundensoko = null;
  private WrapperButtonZusammenfuehren wbuKundensokoDatenrichtung = null;
  private WrapperTextFieldZusammenfuehren wtfZielKundeKundensoko = null;
  private WrapperTextFieldZusammenfuehren wtfQuellKundeKundensoko = null;

  // Mindermengenzuschlag
  private WrapperLabel wlaMindermengenzuschlag = null;
  private WrapperButtonZusammenfuehren wbuMindermengenzuschlagDatenrichtung = null;
  private WrapperCheckBoxZusammenfuehren wcbZielKundeMindermengenzuschlag = null;
  private WrapperCheckBoxZusammenfuehren wcbQuellKundeMindermengenzuschlag = null;

  // Monatsrechnung
  private WrapperLabel wlaMonatsrechnung = null;
  private WrapperButtonZusammenfuehren wbuMonatsrechnungDatenrichtung = null;
  private WrapperCheckBoxZusammenfuehren wcbZielKundeMonatsrechnung = null;
  private WrapperCheckBoxZusammenfuehren wcbQuellKundeMonatsrechnung = null;

  // Sammelrechnung
  private WrapperLabel wlaSammelrechnung = null;
  private WrapperButtonZusammenfuehren wbuSammelrechnungDatenrichtung = null;
  private WrapperCheckBoxZusammenfuehren wcbZielKundeSammelrechnung = null;
  private WrapperCheckBoxZusammenfuehren wcbQuellKundeSammelrechnung = null;

  // Rechnungsempfaenger
  private WrapperLabel wlaRechnungsempfaenger = null;
  private WrapperButtonZusammenfuehren wbuRechnungsempfaengerDatenrichtung = null;
  private WrapperCheckBoxZusammenfuehren wcbZielKundeRechnungsempfaenger = null;
  private WrapperCheckBoxZusammenfuehren wcbQuellKundeRechnungsempfaenger = null;

  // PreiseAmLs
  private WrapperLabel wlaPreiseAmLs = null;
  private WrapperButtonZusammenfuehren wbuPreiseAmLsDatenrichtung = null;
  private WrapperCheckBoxZusammenfuehren wcbZielKundePreiseAmLs = null;
  private WrapperCheckBoxZusammenfuehren wcbQuellKundePreiseAmLs = null;

  // BelegdruckMitRabatt
  private WrapperLabel wlaBelegdruckMitRabatt = null;
  private WrapperButtonZusammenfuehren wbuBelegdruckMitRabattDatenrichtung = null;
  private WrapperCheckBoxZusammenfuehren wcbZielKundeBelegdruckMitRabatt = null;
  private WrapperCheckBoxZusammenfuehren wcbQuellKundeBelegdruckMitRabatt = null;

  // Distributor
  private WrapperLabel wlaDistributor = null;
  private WrapperButtonZusammenfuehren wbuDistributorDatenrichtung = null;
  private WrapperCheckBoxZusammenfuehren wcbZielKundeDistributor = null;
  private WrapperCheckBoxZusammenfuehren wcbQuellKundeDistributor = null;

  // Teillieferungen
  private WrapperLabel wlaTeillieferungen = null;
  private WrapperButtonZusammenfuehren wbuTeillieferungenDatenrichtung = null;
  private WrapperCheckBoxZusammenfuehren wcbZielKundeTeillieferungen = null;
  private WrapperCheckBoxZusammenfuehren wcbQuellKundeTeillieferungen = null;

  // Lsgewicht
  private WrapperLabel wlaLsgewicht = null;
  private WrapperButtonZusammenfuehren wbuLsgewichtDatenrichtung = null;
  private WrapperCheckBoxZusammenfuehren wcbZielKundeLsgewicht = null;
  private WrapperCheckBoxZusammenfuehren wcbQuellKundeLsgewicht = null;

  // Interessent
  private WrapperLabel wlaInteressent = null;
  private WrapperButtonZusammenfuehren wbuInteressentDatenrichtung = null;
  private WrapperCheckBoxZusammenfuehren wcbZielKundeInteressent = null;
  private WrapperCheckBoxZusammenfuehren wcbQuellKundeInteressent = null;

  // Versteckt
  private WrapperLabel wlaVersteckt = null;
  private WrapperButtonZusammenfuehren wbuVerstecktDatenrichtung = null;
  private WrapperCheckBoxZusammenfuehren wcbZielKundeVersteckt = null;
  private WrapperCheckBoxZusammenfuehren wcbQuellKundeVersteckt = null;

  // Reversecharge
  private WrapperLabel wlaReversecharge = null;
  private WrapperButtonZusammenfuehren wbuReversechargeDatenrichtung = null;
  private WrapperCheckBoxZusammenfuehren wcbZielKundeReversecharge = null;
  private WrapperCheckBoxZusammenfuehren wcbQuellKundeReversecharge = null;


  public PanelDialogKundenZusammenfuehren(KundeDto kundeZielDto,
                                          InternalFrame internalFrame,
                                          String add2TitleI)
      throws Throwable {

    super(internalFrame,
          add2TitleI + " | " + kundeZielDto.getPartnerDto().formatName());

    this.kundeZielDto = kundeZielDto;
    jbInit(); // zuerst QuellFelder leer, erst nach Quellauswahl Werte drin
    initComponents();
    setSaveButtonStatus();
  }

  /**
   * setzt den Button fuer das rndgueltige zusammenfuehren aktiv oder inaktiv, je nach Vorhandensein des
   * linken und rechten Kundens
   * @throws Throwable
   */
  private void setSaveButtonStatus()
      throws Throwable {
    if (this.kundeQuellDto != null && this.kundeZielDto != null) {
      this.saveButton.setEnabled(true);
    }
    else {
      this.saveButton.setEnabled(false);
    }
  }


  private void jbInit()
      throws Throwable {

    jScrollPaneKunde = new javax.swing.JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
        JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

    jpaWorkingOnScroll = new JPanel();
    jpaWorkingOnScroll.setLayout(new GridBagLayout());


    // Zielkunde-Auswahlbutton
    wbuZielKundeAuswaehlen = new WrapperButtonZusammenfuehren();
    wbuZielKundeAuswaehlen.setText(LPMain.getInstance().getTextRespectUISPr(
        "lp.kunde") + "...");
    wbuZielKundeAuswaehlen.setToolTipText(LPMain.getInstance().getTextRespectUISPr(
        "button.kunde.tooltip"));
    wbuZielKundeAuswaehlen.addActionListener(this);
    wbuZielKundeAuswaehlen.setActionCommand(ACTION_ZIELKUNDE_AUSWAEHLEN);
    wbuZielKundeAuswaehlen.setMaximumSize(new Dimension(125,
        Defaults.getInstance().getControlHeight()));
    wbuZielKundeAuswaehlen.setPreferredSize(new Dimension(115,
        Defaults.getInstance().getControlHeight()));
    wbuZielKundeAuswaehlen.setMinimumSize(new Dimension(110,
        Defaults.getInstance().getControlHeight()));
    wbuZielKundeAuswaehlen.setFocusable(true);


    // Quellkunde-Auswahlbutton
    wbuQuellKundeAuswaehlen = new WrapperButtonZusammenfuehren();
    wbuQuellKundeAuswaehlen.setText(LPMain.getInstance().getTextRespectUISPr(
        "lp.kunde") + "...");
    wbuQuellKundeAuswaehlen.setToolTipText(LPMain.getInstance().getTextRespectUISPr(
        "button.kunde.tooltip"));
    wbuQuellKundeAuswaehlen.addActionListener(this);
    wbuQuellKundeAuswaehlen.setActionCommand(ACTION_QUELLKUNDE_AUSWAEHLEN);
    wbuQuellKundeAuswaehlen.setMaximumSize(new Dimension(125,
        Defaults.getInstance().getControlHeight()));
    wbuQuellKundeAuswaehlen.setPreferredSize(new Dimension(115,
        Defaults.getInstance().getControlHeight()));
    wbuQuellKundeAuswaehlen.setMinimumSize(new Dimension(110,
        Defaults.getInstance().getControlHeight()));
    wbuQuellKundeAuswaehlen.setFocusable(true);

    // AuswahlReset der Datenrichtung
    wbuAuswahlReset = new WrapperButtonZusammenfuehren();
    wbuAuswahlReset.setButtonIsAlle(true);
    wbuAuswahlReset.addActionListener(this);
    wbuAuswahlReset.setActionCommand(ACTION_ALLE_DATENRICHTUNGEN_WAEHLEN);

    // Partnername - nicht editierbar, nur auswaehlbar
    wlaPartnername = new WrapperLabel();
    wlaPartnername.setText(LPMain.getInstance().getTextRespectUISPr("part.firma_nachname") +
                           ": ");
    wlaPartnername.setMaximumSize(new Dimension(115,
                                                Defaults.getInstance().getControlHeight()));
    wlaPartnername.setPreferredSize(new Dimension(100,
                                                  Defaults.getInstance().getControlHeight()));
    wlaPartnername.setMinimumSize(new Dimension(100,
                                                Defaults.getInstance().getControlHeight()));

    wbuPartnernameDatenrichtung = new WrapperButtonZusammenfuehren();
    wbuPartnernameDatenrichtung.addActionListener(this);
    wbuPartnernameDatenrichtung.setActionCommand(ACTION_DATENRICHTUNG_WAEHLEN);
    vWrapperButtonsZusammenfuehren.addElement(wbuPartnernameDatenrichtung);

    wtfZielKundePartnername = new WrapperTextFieldZusammenfuehren();
    wtfZielKundePartnername.setText("");
    wtfZielKundePartnername.setColumnsMax(80);
    wtfZielKundePartnername.setMaximumSize(new Dimension(180,
        Defaults.getInstance().getControlHeight()));
    wtfZielKundePartnername.setPreferredSize(new Dimension(150,
        Defaults.getInstance().getControlHeight()));
    wtfZielKundePartnername.setMinimumSize(new Dimension(150,
        Defaults.getInstance().getControlHeight()));
    wtfZielKundePartnername.setActivatable(false);
    vZielfelder.addElement(wtfZielKundePartnername);

    wtfQuellKundePartnername = new WrapperTextFieldZusammenfuehren();
    wtfQuellKundePartnername.setText("");
    wtfQuellKundePartnername.setColumnsMax(80);
    wtfQuellKundePartnername.setMaximumSize(new Dimension(180,
        Defaults.getInstance().getControlHeight()));
    wtfQuellKundePartnername.setPreferredSize(new Dimension(150,
        Defaults.getInstance().getControlHeight()));
    wtfQuellKundePartnername.setMinimumSize(new Dimension(150,
        Defaults.getInstance().getControlHeight()));
    wtfQuellKundePartnername.setActivatable(false);
    vQuellfelder.addElement(wtfQuellKundePartnername);

    // Mwst - nicht editierbar, nur auswaehlbar
    wlaMwst = new WrapperLabel();
    wlaMwst.setText(LPMain.getInstance().getTextRespectUISPr("lp.mwst") + ": ");
    wlaMwst.setMaximumSize(new Dimension(115, Defaults.getInstance().getControlHeight()));
    wlaMwst.setPreferredSize(new Dimension(100, Defaults.getInstance().getControlHeight()));
    wlaMwst.setMinimumSize(new Dimension(100, Defaults.getInstance().getControlHeight()));

    wbuMwstDatenrichtung = new WrapperButtonZusammenfuehren();
    wbuMwstDatenrichtung.addActionListener(this);
    wbuMwstDatenrichtung.setActionCommand(ACTION_DATENRICHTUNG_WAEHLEN);
    vWrapperButtonsZusammenfuehren.addElement(wbuMwstDatenrichtung);

    wtfZielKundeMwst = new WrapperTextFieldZusammenfuehren();
    wtfZielKundeMwst.setText("");
    wtfZielKundeMwst.setMaximumSize(new Dimension(180,
                                                  Defaults.getInstance().getControlHeight()));
    wtfZielKundeMwst.setPreferredSize(new Dimension(150,
        Defaults.getInstance().getControlHeight()));
    wtfZielKundeMwst.setMinimumSize(new Dimension(150,
                                                  Defaults.getInstance().getControlHeight()));
    wtfZielKundeMwst.setActivatable(false);
    vZielfelder.addElement(wtfZielKundeMwst);

    wtfQuellKundeMwst = new WrapperTextFieldZusammenfuehren();
    wtfQuellKundeMwst.setText("");
    wtfQuellKundeMwst.setMaximumSize(new Dimension(180,
        Defaults.getInstance().getControlHeight()));
    wtfQuellKundeMwst.setPreferredSize(new Dimension(150,
        Defaults.getInstance().getControlHeight()));
    wtfQuellKundeMwst.setMinimumSize(new Dimension(150,
        Defaults.getInstance().getControlHeight()));
    wtfQuellKundeMwst.setActivatable(false);
    vQuellfelder.addElement(wtfQuellKundeMwst);

    // Waehrung - nicht editierbar, nur auswaehlbar
    wlaWaehrung = new WrapperLabel();
    wlaWaehrung.setText(LPMain.getInstance().getTextRespectUISPr("lp.waehrung") + ": ");
    wlaWaehrung.setMaximumSize(new Dimension(115, Defaults.getInstance().getControlHeight()));
    wlaWaehrung.setPreferredSize(new Dimension(100,
                                               Defaults.getInstance().getControlHeight()));
    wlaWaehrung.setMinimumSize(new Dimension(100, Defaults.getInstance().getControlHeight()));

    wbuWaehrungDatenrichtung = new WrapperButtonZusammenfuehren();
    wbuWaehrungDatenrichtung.addActionListener(this);
    wbuWaehrungDatenrichtung.setActionCommand(ACTION_DATENRICHTUNG_WAEHLEN);
    vWrapperButtonsZusammenfuehren.addElement(wbuWaehrungDatenrichtung);

    wtfZielKundeWaehrung = new WrapperTextFieldZusammenfuehren();
    wtfZielKundeWaehrung.setText("");
    wtfZielKundeWaehrung.setMaximumSize(new Dimension(180,
        Defaults.getInstance().getControlHeight()));
    wtfZielKundeWaehrung.setPreferredSize(new Dimension(150,
        Defaults.getInstance().getControlHeight()));
    wtfZielKundeWaehrung.setMinimumSize(new Dimension(150,
        Defaults.getInstance().getControlHeight()));
    wtfZielKundeWaehrung.setActivatable(false);
    vZielfelder.addElement(wtfZielKundeWaehrung);

    wtfQuellKundeWaehrung = new WrapperTextFieldZusammenfuehren();
    wtfQuellKundeWaehrung.setText("");
    wtfQuellKundeWaehrung.setMaximumSize(new Dimension(180,
        Defaults.getInstance().getControlHeight()));
    wtfQuellKundeWaehrung.setPreferredSize(new Dimension(150,
        Defaults.getInstance().getControlHeight()));
    wtfQuellKundeWaehrung.setMinimumSize(new Dimension(150,
        Defaults.getInstance().getControlHeight()));
    wtfQuellKundeWaehrung.setActivatable(false);
    vQuellfelder.addElement(wtfQuellKundeWaehrung);

    // Lieferart - nicht editierbar, nur auswaehlbar
    wlaLieferart = new WrapperLabel();
    wlaLieferart.setText(LPMain.getInstance().getTextRespectUISPr("label.lieferart") +
                         ": ");
    wlaLieferart.setMaximumSize(new Dimension(115,
                                              Defaults.getInstance().getControlHeight()));
    wlaLieferart.setPreferredSize(new Dimension(100,
                                                Defaults.getInstance().getControlHeight()));
    wlaLieferart.setMinimumSize(new Dimension(100,
                                              Defaults.getInstance().getControlHeight()));

    wbuLieferartDatenrichtung = new WrapperButtonZusammenfuehren();
    wbuLieferartDatenrichtung.addActionListener(this);
    wbuLieferartDatenrichtung.setActionCommand(ACTION_DATENRICHTUNG_WAEHLEN);
    vWrapperButtonsZusammenfuehren.addElement(wbuLieferartDatenrichtung);

    wtfZielKundeLieferart = new WrapperTextFieldZusammenfuehren();
    wtfZielKundeLieferart.setText("");
    wtfZielKundeLieferart.setMaximumSize(new Dimension(180,
        Defaults.getInstance().getControlHeight()));
    wtfZielKundeLieferart.setPreferredSize(new Dimension(150,
        Defaults.getInstance().getControlHeight()));
    wtfZielKundeLieferart.setMinimumSize(new Dimension(150,
        Defaults.getInstance().getControlHeight()));
    wtfZielKundeLieferart.setActivatable(false);
    vZielfelder.addElement(wtfZielKundeLieferart);

    wtfQuellKundeLieferart = new WrapperTextFieldZusammenfuehren();
    wtfQuellKundeLieferart.setText("");
    wtfQuellKundeLieferart.setMaximumSize(new Dimension(180,
        Defaults.getInstance().getControlHeight()));
    wtfQuellKundeLieferart.setPreferredSize(new Dimension(150,
        Defaults.getInstance().getControlHeight()));
    wtfQuellKundeLieferart.setMinimumSize(new Dimension(150,
        Defaults.getInstance().getControlHeight()));
    wtfQuellKundeLieferart.setActivatable(false);
    vQuellfelder.addElement(wtfQuellKundeLieferart);

    // Spediteur - nicht editierbar, nur auswaehlbar
    wlaSpediteur = new WrapperLabel();
    wlaSpediteur.setText(LPMain.getInstance().getTextRespectUISPr("lp.spediteur") + ": ");
    wlaSpediteur.setMaximumSize(new Dimension(115,
                                              Defaults.getInstance().getControlHeight()));
    wlaSpediteur.setPreferredSize(new Dimension(100,
                                                Defaults.getInstance().getControlHeight()));
    wlaSpediteur.setMinimumSize(new Dimension(100,
                                              Defaults.getInstance().getControlHeight()));

    wbuSpediteurDatenrichtung = new WrapperButtonZusammenfuehren();
    wbuSpediteurDatenrichtung.addActionListener(this);
    wbuSpediteurDatenrichtung.setActionCommand(ACTION_DATENRICHTUNG_WAEHLEN);
    vWrapperButtonsZusammenfuehren.addElement(wbuSpediteurDatenrichtung);

    wtfZielKundeSpediteur = new WrapperTextFieldZusammenfuehren();
    wtfZielKundeSpediteur.setText("");
    wtfZielKundeSpediteur.setMaximumSize(new Dimension(180,
        Defaults.getInstance().getControlHeight()));
    wtfZielKundeSpediteur.setPreferredSize(new Dimension(150,
        Defaults.getInstance().getControlHeight()));
    wtfZielKundeSpediteur.setMinimumSize(new Dimension(150,
        Defaults.getInstance().getControlHeight()));
    wtfZielKundeSpediteur.setActivatable(false);
    vZielfelder.addElement(wtfZielKundeSpediteur);

    wtfQuellKundeSpediteur = new WrapperTextFieldZusammenfuehren();
    wtfQuellKundeSpediteur.setText("");
    wtfQuellKundeSpediteur.setMaximumSize(new Dimension(180,
        Defaults.getInstance().getControlHeight()));
    wtfQuellKundeSpediteur.setPreferredSize(new Dimension(150,
        Defaults.getInstance().getControlHeight()));
    wtfQuellKundeSpediteur.setMinimumSize(new Dimension(150,
        Defaults.getInstance().getControlHeight()));
    wtfQuellKundeSpediteur.setActivatable(false);
    vQuellfelder.addElement(wtfQuellKundeSpediteur);

    // Zahlungsziel - nicht editierbar, nur auswaehlbar
    wlaZahlungsziel = new WrapperLabel();
    wlaZahlungsziel.setText(LPMain.getInstance().getTextRespectUISPr("label.zahlungsziel") +
                            ": ");
    wlaZahlungsziel.setMaximumSize(new Dimension(115,
                                                 Defaults.getInstance().getControlHeight()));
    wlaZahlungsziel.setPreferredSize(new Dimension(100,
        Defaults.getInstance().getControlHeight()));
    wlaZahlungsziel.setMinimumSize(new Dimension(100,
                                                 Defaults.getInstance().getControlHeight()));

    wbuZahlungszielDatenrichtung = new WrapperButtonZusammenfuehren();
    wbuZahlungszielDatenrichtung.addActionListener(this);
    wbuZahlungszielDatenrichtung.setActionCommand(ACTION_DATENRICHTUNG_WAEHLEN);
    vWrapperButtonsZusammenfuehren.addElement(wbuZahlungszielDatenrichtung);

    wtfZielKundeZahlungsziel = new WrapperTextFieldZusammenfuehren();
    wtfZielKundeZahlungsziel.setText("");
    wtfZielKundeZahlungsziel.setMaximumSize(new Dimension(180,
        Defaults.getInstance().getControlHeight()));
    wtfZielKundeZahlungsziel.setPreferredSize(new Dimension(150,
        Defaults.getInstance().getControlHeight()));
    wtfZielKundeZahlungsziel.setMinimumSize(new Dimension(150,
        Defaults.getInstance().getControlHeight()));
    wtfZielKundeZahlungsziel.setActivatable(false);
    vZielfelder.addElement(wtfZielKundeZahlungsziel);

    wtfQuellKundeZahlungsziel = new WrapperTextFieldZusammenfuehren();
    wtfQuellKundeZahlungsziel.setText("");
    wtfQuellKundeZahlungsziel.setMaximumSize(new Dimension(180,
        Defaults.getInstance().getControlHeight()));
    wtfQuellKundeZahlungsziel.setPreferredSize(new Dimension(150,
        Defaults.getInstance().getControlHeight()));
    wtfQuellKundeZahlungsziel.setMinimumSize(new Dimension(150,
        Defaults.getInstance().getControlHeight()));
    wtfQuellKundeZahlungsziel.setActivatable(false);
    vQuellfelder.addElement(wtfQuellKundeZahlungsziel);

    // Kostenstelle - nicht editierbar, nur auswaehlbar
    wlaKostenstelle = new WrapperLabel();
    wlaKostenstelle.setText(LPMain.getInstance().getTextRespectUISPr("label.kostenstelle") +
                            ": ");
    wlaKostenstelle.setMaximumSize(new Dimension(115,
                                                 Defaults.getInstance().getControlHeight()));
    wlaKostenstelle.setPreferredSize(new Dimension(100,
        Defaults.getInstance().getControlHeight()));
    wlaKostenstelle.setMinimumSize(new Dimension(100,
                                                 Defaults.getInstance().getControlHeight()));

    wbuKostenstelleDatenrichtung = new WrapperButtonZusammenfuehren();
    wbuKostenstelleDatenrichtung.addActionListener(this);
    wbuKostenstelleDatenrichtung.setActionCommand(ACTION_DATENRICHTUNG_WAEHLEN);
    vWrapperButtonsZusammenfuehren.addElement(wbuKostenstelleDatenrichtung);

    wtfZielKundeKostenstelle = new WrapperTextFieldZusammenfuehren();
    wtfZielKundeKostenstelle.setText("");
    wtfZielKundeKostenstelle.setMaximumSize(new Dimension(180,
        Defaults.getInstance().getControlHeight()));
    wtfZielKundeKostenstelle.setPreferredSize(new Dimension(150,
        Defaults.getInstance().getControlHeight()));
    wtfZielKundeKostenstelle.setMinimumSize(new Dimension(150,
        Defaults.getInstance().getControlHeight()));
    wtfZielKundeKostenstelle.setActivatable(false);
    vZielfelder.addElement(wtfZielKundeKostenstelle);

    wtfQuellKundeKostenstelle = new WrapperTextFieldZusammenfuehren();
    wtfQuellKundeKostenstelle.setText("");
    wtfQuellKundeKostenstelle.setMaximumSize(new Dimension(180,
        Defaults.getInstance().getControlHeight()));
    wtfQuellKundeKostenstelle.setPreferredSize(new Dimension(150,
        Defaults.getInstance().getControlHeight()));
    wtfQuellKundeKostenstelle.setMinimumSize(new Dimension(150,
        Defaults.getInstance().getControlHeight()));
    wtfQuellKundeKostenstelle.setActivatable(false);
    vQuellfelder.addElement(wtfQuellKundeKostenstelle);

    // Provisionsempfaenger - nicht editierbar, nur auswaehlbar
    wlaProvisionsempfaenger = new WrapperLabel();
    wlaProvisionsempfaenger.setText(LPMain.getInstance().getTextRespectUISPr(
        "part.kunde.provisionsempfaenger") + ": ");
    wlaProvisionsempfaenger.setMaximumSize(new Dimension(130,
        Defaults.getInstance().getControlHeight()));
    wlaProvisionsempfaenger.setPreferredSize(new Dimension(110,
        Defaults.getInstance().getControlHeight()));
    wlaProvisionsempfaenger.setMinimumSize(new Dimension(110,
        Defaults.getInstance().getControlHeight()));

    wbuProvisionsempfaengerDatenrichtung = new WrapperButtonZusammenfuehren();
    wbuProvisionsempfaengerDatenrichtung.addActionListener(this);
    wbuProvisionsempfaengerDatenrichtung.setActionCommand(ACTION_DATENRICHTUNG_WAEHLEN);
    vWrapperButtonsZusammenfuehren.addElement(wbuProvisionsempfaengerDatenrichtung);

    wtfZielKundeProvisionsempfaenger = new WrapperTextFieldZusammenfuehren();
    wtfZielKundeProvisionsempfaenger.setText("");
    wtfZielKundeProvisionsempfaenger.setMaximumSize(new Dimension(180,
        Defaults.getInstance().getControlHeight()));
    wtfZielKundeProvisionsempfaenger.setPreferredSize(new Dimension(150,
        Defaults.getInstance().getControlHeight()));
    wtfZielKundeProvisionsempfaenger.setMinimumSize(new Dimension(150,
        Defaults.getInstance().getControlHeight()));
    wtfZielKundeProvisionsempfaenger.setActivatable(false);
    vZielfelder.addElement(wtfZielKundeProvisionsempfaenger);

    wtfQuellKundeProvisionsempfaenger = new WrapperTextFieldZusammenfuehren();
    wtfQuellKundeProvisionsempfaenger.setText("");
    wtfQuellKundeProvisionsempfaenger.setMaximumSize(new Dimension(180,
        Defaults.getInstance().getControlHeight()));
    wtfQuellKundeProvisionsempfaenger.setPreferredSize(new Dimension(150,
        Defaults.getInstance().getControlHeight()));
    wtfQuellKundeProvisionsempfaenger.setMinimumSize(new Dimension(150,
        Defaults.getInstance().getControlHeight()));
    wtfQuellKundeProvisionsempfaenger.setActivatable(false);
    vQuellfelder.addElement(wtfQuellKundeProvisionsempfaenger);

    // Rabattsatz - nicht editierbar, nur auswaehlbar
    wlaRabattsatz = new WrapperLabel();
    wlaRabattsatz.setText(LPMain.getInstance().getTextRespectUISPr("label.rabatt") +
                          ": ");
    wlaRabattsatz.setMaximumSize(new Dimension(115,
                                               Defaults.getInstance().getControlHeight()));
    wlaRabattsatz.setPreferredSize(new Dimension(100,
                                                 Defaults.getInstance().getControlHeight()));
    wlaRabattsatz.setMinimumSize(new Dimension(100,
                                               Defaults.getInstance().getControlHeight()));

    wbuRabattsatzDatenrichtung = new WrapperButtonZusammenfuehren();
    wbuRabattsatzDatenrichtung.addActionListener(this);
    wbuRabattsatzDatenrichtung.setActionCommand(ACTION_DATENRICHTUNG_WAEHLEN);
    vWrapperButtonsZusammenfuehren.addElement(wbuRabattsatzDatenrichtung);

    wtfZielKundeRabattsatz = new WrapperTextFieldZusammenfuehren();
    wtfZielKundeRabattsatz.setText("");
    wtfZielKundeRabattsatz.setMaximumSize(new Dimension(180,
        Defaults.getInstance().getControlHeight()));
    wtfZielKundeRabattsatz.setPreferredSize(new Dimension(150,
        Defaults.getInstance().getControlHeight()));
    wtfZielKundeRabattsatz.setMinimumSize(new Dimension(150,
        Defaults.getInstance().getControlHeight()));
    wtfZielKundeRabattsatz.setActivatable(false);
    vZielfelder.addElement(wtfZielKundeRabattsatz);

    wtfQuellKundeRabattsatz = new WrapperTextFieldZusammenfuehren();
    wtfQuellKundeRabattsatz.setText("");
    wtfQuellKundeRabattsatz.setMaximumSize(new Dimension(180,
        Defaults.getInstance().getControlHeight()));
    wtfQuellKundeRabattsatz.setPreferredSize(new Dimension(150,
        Defaults.getInstance().getControlHeight()));
    wtfQuellKundeRabattsatz.setMinimumSize(new Dimension(150,
        Defaults.getInstance().getControlHeight()));
    wtfQuellKundeRabattsatz.setActivatable(false);
    vQuellfelder.addElement(wtfQuellKundeRabattsatz);

    // Garantie - nicht editierbar, nur auswaehlbar
    wlaGarantie = new WrapperLabel();
    wlaGarantie.setText(LPMain.getInstance().getTextRespectUISPr("kond.label.garantie") +
                        ": ");
    wlaGarantie.setMaximumSize(new Dimension(115, Defaults.getInstance().getControlHeight()));
    wlaGarantie.setPreferredSize(new Dimension(100,
                                               Defaults.getInstance().getControlHeight()));
    wlaGarantie.setMinimumSize(new Dimension(100, Defaults.getInstance().getControlHeight()));

    wbuGarantieDatenrichtung = new WrapperButtonZusammenfuehren();
    wbuGarantieDatenrichtung.addActionListener(this);
    wbuGarantieDatenrichtung.setActionCommand(ACTION_DATENRICHTUNG_WAEHLEN);
    vWrapperButtonsZusammenfuehren.addElement(wbuGarantieDatenrichtung);

    wtfZielKundeGarantie = new WrapperTextFieldZusammenfuehren();
    wtfZielKundeGarantie.setText("");
    wtfZielKundeGarantie.setMaximumSize(new Dimension(180,
        Defaults.getInstance().getControlHeight()));
    wtfZielKundeGarantie.setPreferredSize(new Dimension(150,
        Defaults.getInstance().getControlHeight()));
    wtfZielKundeGarantie.setMinimumSize(new Dimension(150,
        Defaults.getInstance().getControlHeight()));
    wtfZielKundeGarantie.setActivatable(false);
    vZielfelder.addElement(wtfZielKundeGarantie);

    wtfQuellKundeGarantie = new WrapperTextFieldZusammenfuehren();
    wtfQuellKundeGarantie.setText("");
    wtfQuellKundeGarantie.setMaximumSize(new Dimension(180,
        Defaults.getInstance().getControlHeight()));
    wtfQuellKundeGarantie.setPreferredSize(new Dimension(150,
        Defaults.getInstance().getControlHeight()));
    wtfQuellKundeGarantie.setMinimumSize(new Dimension(150,
        Defaults.getInstance().getControlHeight()));
    wtfQuellKundeGarantie.setActivatable(false);
    vQuellfelder.addElement(wtfQuellKundeGarantie);

    // XKommentar - Konditionen-Kommentar
    wlaKommentar = new WrapperLabel();
    wlaKommentar.setText(LPMain.getInstance().getTextRespectUISPr("lp.kommentar") + ": ");
    wlaKommentar.setMaximumSize(new Dimension(115,
                                              Defaults.getInstance().getControlHeight()));
    wlaKommentar.setPreferredSize(new Dimension(100,
                                                Defaults.getInstance().getControlHeight()));
    wlaKommentar.setMinimumSize(new Dimension(100,
                                              Defaults.getInstance().getControlHeight()));

    wbuKommentarDatenrichtung = new WrapperButtonZusammenfuehren();
    wbuKommentarDatenrichtung.addActionListener(this);
    wbuKommentarDatenrichtung.setActionCommand(ACTION_DATENRICHTUNG_WAEHLEN);
    wbuKommentarDatenrichtung.setButtonKombinierbar(true);
    vWrapperButtonsZusammenfuehren.addElement(wbuKommentarDatenrichtung);
    wtaZielKundeKommentar = new WrapperTextAreaZusammenfuehren();
    wtaZielKundeKommentar.setAutoscrolls(true);
    wtaZielKundeKommentar.setRows(3);
    wtaZielKundeKommentar.setColumns(15);
    wtaZielKundeKommentar.setEditable(false);
    wtaZielKundeKommentar.setActivatable(false);
    wtaZielKundeKommentar.setCaretPosition(0);
    vZielfelder.addElement(wtaZielKundeKommentar);
    jScrollPaneZielKundeKommentar = new javax.swing.JScrollPane();
    jScrollPaneZielKundeKommentar.setViewportView(wtaZielKundeKommentar);

    wtaQuellKundeKommentar = new WrapperTextAreaZusammenfuehren();
    wtaQuellKundeKommentar.setAutoscrolls(true);
    wtaQuellKundeKommentar.setRows(3);
    wtaQuellKundeKommentar.setColumns(15);
    wtaQuellKundeKommentar.setEditable(false);
    wtaQuellKundeKommentar.setActivatable(false);
    wtaQuellKundeKommentar.setCaretPosition(0);
    vQuellfelder.addElement(wtaQuellKundeKommentar);
    jScrollPaneQuellKundeKommentar = new javax.swing.JScrollPane();
    jScrollPaneQuellKundeKommentar.setViewportView(wtaQuellKundeKommentar);

    // Kurznr - Konditionen links neben generieren Kurznr - nicht editierbar, nur auswaehlbar
    wlaKurznr = new WrapperLabel();
    wlaKurznr.setText(LPMain.getInstance().getTextRespectUISPr("lp.kurznr") + ": ");
    wlaKurznr.setMaximumSize(new Dimension(115, Defaults.getInstance().getControlHeight()));
    wlaKurznr.setPreferredSize(new Dimension(100, Defaults.getInstance().getControlHeight()));
    wlaKurznr.setMinimumSize(new Dimension(100, Defaults.getInstance().getControlHeight()));

    wbuKurznrDatenrichtung = new WrapperButtonZusammenfuehren();
    wbuKurznrDatenrichtung.addActionListener(this);
    wbuKurznrDatenrichtung.setActionCommand(ACTION_DATENRICHTUNG_WAEHLEN);
    vWrapperButtonsZusammenfuehren.addElement(wbuKurznrDatenrichtung);

    wtfZielKundeKurznr = new WrapperTextFieldZusammenfuehren();
    wtfZielKundeKurznr.setText("");
    wtfZielKundeKurznr.setMaximumSize(new Dimension(180,
        Defaults.getInstance().getControlHeight()));
    wtfZielKundeKurznr.setPreferredSize(new Dimension(150,
        Defaults.getInstance().getControlHeight()));
    wtfZielKundeKurznr.setMinimumSize(new Dimension(150,
        Defaults.getInstance().getControlHeight()));
    wtfZielKundeKurznr.setActivatable(false);
    vZielfelder.addElement(wtfZielKundeKurznr);

    wtfQuellKundeKurznr = new WrapperTextFieldZusammenfuehren();
    wtfQuellKundeKurznr.setText("");
    wtfQuellKundeKurznr.setMaximumSize(new Dimension(180,
        Defaults.getInstance().getControlHeight()));
    wtfQuellKundeKurznr.setPreferredSize(new Dimension(150,
        Defaults.getInstance().getControlHeight()));
    wtfQuellKundeKurznr.setMinimumSize(new Dimension(150,
        Defaults.getInstance().getControlHeight()));
    wtfQuellKundeKurznr.setActivatable(false);
    vQuellfelder.addElement(wtfQuellKundeKurznr);

    // Kreditlimit - nicht editierbar, nur auswaehlbar
    wlaKreditlimit = new WrapperLabel();
    wlaKreditlimit.setText(LPMain.getInstance().getTextRespectUISPr("part.kreditlimit") +
                           ": ");
    wlaKreditlimit.setMaximumSize(new Dimension(115,
                                                Defaults.getInstance().getControlHeight()));
    wlaKreditlimit.setPreferredSize(new Dimension(100,
                                                  Defaults.getInstance().getControlHeight()));
    wlaKreditlimit.setMinimumSize(new Dimension(100,
                                                Defaults.getInstance().getControlHeight()));

    wbuKreditlimitDatenrichtung = new WrapperButtonZusammenfuehren();
    wbuKreditlimitDatenrichtung.addActionListener(this);
    wbuKreditlimitDatenrichtung.setActionCommand(ACTION_DATENRICHTUNG_WAEHLEN);
    vWrapperButtonsZusammenfuehren.addElement(wbuKreditlimitDatenrichtung);

    wtfZielKundeKreditlimit = new WrapperTextFieldZusammenfuehren();
    wtfZielKundeKreditlimit.setText("");
    wtfZielKundeKreditlimit.setMaximumSize(new Dimension(180,
        Defaults.getInstance().getControlHeight()));
    wtfZielKundeKreditlimit.setPreferredSize(new Dimension(150,
        Defaults.getInstance().getControlHeight()));
    wtfZielKundeKreditlimit.setMinimumSize(new Dimension(150,
        Defaults.getInstance().getControlHeight()));
    wtfZielKundeKreditlimit.setActivatable(false);
    vZielfelder.addElement(wtfZielKundeKreditlimit);

    wtfQuellKundeKreditlimit = new WrapperTextFieldZusammenfuehren();
    wtfQuellKundeKreditlimit.setText("");
    wtfQuellKundeKreditlimit.setMaximumSize(new Dimension(180,
        Defaults.getInstance().getControlHeight()));
    wtfQuellKundeKreditlimit.setPreferredSize(new Dimension(150,
        Defaults.getInstance().getControlHeight()));
    wtfQuellKundeKreditlimit.setMinimumSize(new Dimension(150,
        Defaults.getInstance().getControlHeight()));
    wtfQuellKundeKreditlimit.setActivatable(false);
    vQuellfelder.addElement(wtfQuellKundeKreditlimit);

    // Bonitaetspruefung - nicht editierbar, nur auswaehlbar
    wlaBonitaetspruefung = new WrapperLabel();
    wlaBonitaetspruefung.setText(LPMain.getInstance().getTextRespectUISPr(
        "part.kund.bonitaet.check.last.kurz") + ": ");
    wlaBonitaetspruefung.setMaximumSize(new Dimension(130,
        Defaults.getInstance().getControlHeight()));
    wlaBonitaetspruefung.setPreferredSize(new Dimension(125,
        Defaults.getInstance().getControlHeight()));
    wlaBonitaetspruefung.setMinimumSize(new Dimension(125,
        Defaults.getInstance().getControlHeight()));

    wbuBonitaetspruefungDatenrichtung = new WrapperButtonZusammenfuehren();
    wbuBonitaetspruefungDatenrichtung.addActionListener(this);
    wbuBonitaetspruefungDatenrichtung.setActionCommand(ACTION_DATENRICHTUNG_WAEHLEN);
    vWrapperButtonsZusammenfuehren.addElement(wbuBonitaetspruefungDatenrichtung);

    wtfZielKundeBonitaetspruefung = new WrapperTextFieldZusammenfuehren();
    wtfZielKundeBonitaetspruefung.setText("");
    wtfZielKundeBonitaetspruefung.setMaximumSize(new Dimension(180,
        Defaults.getInstance().getControlHeight()));
    wtfZielKundeBonitaetspruefung.setPreferredSize(new Dimension(150,
        Defaults.getInstance().getControlHeight()));
    wtfZielKundeBonitaetspruefung.setMinimumSize(new Dimension(150,
        Defaults.getInstance().getControlHeight()));
    wtfZielKundeBonitaetspruefung.setActivatable(false);
    vZielfelder.addElement(wtfZielKundeBonitaetspruefung);

    wtfQuellKundeBonitaetspruefung = new WrapperTextFieldZusammenfuehren();
    wtfQuellKundeBonitaetspruefung.setText("");
    wtfQuellKundeBonitaetspruefung.setMaximumSize(new Dimension(180,
        Defaults.getInstance().getControlHeight()));
    wtfQuellKundeBonitaetspruefung.setPreferredSize(new Dimension(150,
        Defaults.getInstance().getControlHeight()));
    wtfQuellKundeBonitaetspruefung.setMinimumSize(new Dimension(150,
        Defaults.getInstance().getControlHeight()));
    wtfQuellKundeBonitaetspruefung.setActivatable(false);
    vQuellfelder.addElement(wtfQuellKundeBonitaetspruefung);

    // Liefersperre - nicht editierbar, nur auswaehlbar
    wlaLiefersperre = new WrapperLabel();
    wlaLiefersperre.setText(LPMain.getInstance().getTextRespectUISPr(
        "part.kund.liefersperre_am") + ": ");
    wlaLiefersperre.setMaximumSize(new Dimension(120,
                                                 Defaults.getInstance().getControlHeight()));
    wlaLiefersperre.setPreferredSize(new Dimension(115,
        Defaults.getInstance().getControlHeight()));
    wlaLiefersperre.setMinimumSize(new Dimension(115,
                                                 Defaults.getInstance().getControlHeight()));

    wbuLiefersperreDatenrichtung = new WrapperButtonZusammenfuehren();
    wbuLiefersperreDatenrichtung.addActionListener(this);
    wbuLiefersperreDatenrichtung.setActionCommand(ACTION_DATENRICHTUNG_WAEHLEN);
    vWrapperButtonsZusammenfuehren.addElement(wbuLiefersperreDatenrichtung);

    wtfZielKundeLiefersperre = new WrapperTextFieldZusammenfuehren();
    wtfZielKundeLiefersperre.setText("");
    wtfZielKundeLiefersperre.setMaximumSize(new Dimension(180,
        Defaults.getInstance().getControlHeight()));
    wtfZielKundeLiefersperre.setPreferredSize(new Dimension(150,
        Defaults.getInstance().getControlHeight()));
    wtfZielKundeLiefersperre.setMinimumSize(new Dimension(150,
        Defaults.getInstance().getControlHeight()));
    wtfZielKundeLiefersperre.setActivatable(false);
    vZielfelder.addElement(wtfZielKundeLiefersperre);

    wtfQuellKundeLiefersperre = new WrapperTextFieldZusammenfuehren();
    wtfQuellKundeLiefersperre.setText("");
    wtfQuellKundeLiefersperre.setMaximumSize(new Dimension(180,
        Defaults.getInstance().getControlHeight()));
    wtfQuellKundeLiefersperre.setPreferredSize(new Dimension(150,
        Defaults.getInstance().getControlHeight()));
    wtfQuellKundeLiefersperre.setMinimumSize(new Dimension(150,
        Defaults.getInstance().getControlHeight()));
    wtfQuellKundeLiefersperre.setActivatable(false);
    vQuellfelder.addElement(wtfQuellKundeLiefersperre);

    // Rechnungskopien - nicht editierbar, nur auswaehlbar
    wlaRechnungskopien = new WrapperLabel();
    wlaRechnungskopien.setText(LPMain.getInstance().getTextRespectUISPr(
        "part.kund.kopien_re") + ": ");
    wlaRechnungskopien.setMaximumSize(new Dimension(120,
        Defaults.getInstance().getControlHeight()));
    wlaRechnungskopien.setPreferredSize(new Dimension(115,
        Defaults.getInstance().getControlHeight()));
    wlaRechnungskopien.setMinimumSize(new Dimension(115,
        Defaults.getInstance().getControlHeight()));

    wbuRechnungskopienDatenrichtung = new WrapperButtonZusammenfuehren();
    wbuRechnungskopienDatenrichtung.addActionListener(this);
    wbuRechnungskopienDatenrichtung.setActionCommand(ACTION_DATENRICHTUNG_WAEHLEN);
    vWrapperButtonsZusammenfuehren.addElement(wbuRechnungskopienDatenrichtung);

    wtfZielKundeRechnungskopien = new WrapperTextFieldZusammenfuehren();
    wtfZielKundeRechnungskopien.setText("");
    wtfZielKundeRechnungskopien.setMaximumSize(new Dimension(180,
        Defaults.getInstance().getControlHeight()));
    wtfZielKundeRechnungskopien.setPreferredSize(new Dimension(150,
        Defaults.getInstance().getControlHeight()));
    wtfZielKundeRechnungskopien.setMinimumSize(new Dimension(150,
        Defaults.getInstance().getControlHeight()));
    wtfZielKundeRechnungskopien.setActivatable(false);
    vZielfelder.addElement(wtfZielKundeRechnungskopien);

    wtfQuellKundeRechnungskopien = new WrapperTextFieldZusammenfuehren();
    wtfQuellKundeRechnungskopien.setText("");
    wtfQuellKundeRechnungskopien.setMaximumSize(new Dimension(180,
        Defaults.getInstance().getControlHeight()));
    wtfQuellKundeRechnungskopien.setPreferredSize(new Dimension(150,
        Defaults.getInstance().getControlHeight()));
    wtfQuellKundeRechnungskopien.setMinimumSize(new Dimension(150,
        Defaults.getInstance().getControlHeight()));
    wtfQuellKundeRechnungskopien.setActivatable(false);
    vQuellfelder.addElement(wtfQuellKundeRechnungskopien);

    // Lieferscheinkopien - nicht editierbar, nur auswaehlbar
    wlaLieferscheinkopien = new WrapperLabel();
    wlaLieferscheinkopien.setText(LPMain.getInstance().getTextRespectUISPr(
        "part.kund.kopien_ls") + ": ");
    wlaLieferscheinkopien.setMaximumSize(new Dimension(120,
        Defaults.getInstance().getControlHeight()));
    wlaLieferscheinkopien.setPreferredSize(new Dimension(115,
        Defaults.getInstance().getControlHeight()));
    wlaLieferscheinkopien.setMinimumSize(new Dimension(115,
        Defaults.getInstance().getControlHeight()));

    wbuLieferscheinkopienDatenrichtung = new WrapperButtonZusammenfuehren();
    wbuLieferscheinkopienDatenrichtung.addActionListener(this);
    wbuLieferscheinkopienDatenrichtung.setActionCommand(ACTION_DATENRICHTUNG_WAEHLEN);
    vWrapperButtonsZusammenfuehren.addElement(wbuLieferscheinkopienDatenrichtung);

    wtfZielKundeLieferscheinkopien = new WrapperTextFieldZusammenfuehren();
    wtfZielKundeLieferscheinkopien.setText("");
    wtfZielKundeLieferscheinkopien.setMaximumSize(new Dimension(180,
        Defaults.getInstance().getControlHeight()));
    wtfZielKundeLieferscheinkopien.setPreferredSize(new Dimension(150,
        Defaults.getInstance().getControlHeight()));
    wtfZielKundeLieferscheinkopien.setMinimumSize(new Dimension(150,
        Defaults.getInstance().getControlHeight()));
    wtfZielKundeLieferscheinkopien.setActivatable(false);
    vZielfelder.addElement(wtfZielKundeLieferscheinkopien);

    wtfQuellKundeLieferscheinkopien = new WrapperTextFieldZusammenfuehren();
    wtfQuellKundeLieferscheinkopien.setText("");
    wtfQuellKundeLieferscheinkopien.setMaximumSize(new Dimension(180,
        Defaults.getInstance().getControlHeight()));
    wtfQuellKundeLieferscheinkopien.setPreferredSize(new Dimension(150,
        Defaults.getInstance().getControlHeight()));
    wtfQuellKundeLieferscheinkopien.setMinimumSize(new Dimension(150,
        Defaults.getInstance().getControlHeight()));
    wtfQuellKundeLieferscheinkopien.setActivatable(false);
    vQuellfelder.addElement(wtfQuellKundeLieferscheinkopien);

    // Mitarbeiter - nicht editierbar, nur auswaehlbar
    wlaMitarbeiter = new WrapperLabel();
    wlaMitarbeiter.setText(LPMain.getInstance().getTextRespectUISPr("lp.mitarbeiter") +
                           ": ");
    wlaMitarbeiter.setMaximumSize(new Dimension(120,
                                                Defaults.getInstance().getControlHeight()));
    wlaMitarbeiter.setPreferredSize(new Dimension(115,
                                                  Defaults.getInstance().getControlHeight()));
    wlaMitarbeiter.setMinimumSize(new Dimension(115,
                                                Defaults.getInstance().getControlHeight()));

    wbuMitarbeiterDatenrichtung = new WrapperButtonZusammenfuehren();
    wbuMitarbeiterDatenrichtung.addActionListener(this);
    wbuMitarbeiterDatenrichtung.setActionCommand(ACTION_DATENRICHTUNG_WAEHLEN);
    vWrapperButtonsZusammenfuehren.addElement(wbuMitarbeiterDatenrichtung);

    wtfZielKundeMitarbeiter = new WrapperTextFieldZusammenfuehren();
    wtfZielKundeMitarbeiter.setText("");
    wtfZielKundeMitarbeiter.setMaximumSize(new Dimension(180,
        Defaults.getInstance().getControlHeight()));
    wtfZielKundeMitarbeiter.setPreferredSize(new Dimension(150,
        Defaults.getInstance().getControlHeight()));
    wtfZielKundeMitarbeiter.setMinimumSize(new Dimension(150,
        Defaults.getInstance().getControlHeight()));
    wtfZielKundeMitarbeiter.setActivatable(false);
    vZielfelder.addElement(wtfZielKundeMitarbeiter);

    wtfQuellKundeMitarbeiter = new WrapperTextFieldZusammenfuehren();
    wtfQuellKundeMitarbeiter.setText("");
    wtfQuellKundeMitarbeiter.setMaximumSize(new Dimension(180,
        Defaults.getInstance().getControlHeight()));
    wtfQuellKundeMitarbeiter.setPreferredSize(new Dimension(150,
        Defaults.getInstance().getControlHeight()));
    wtfQuellKundeMitarbeiter.setMinimumSize(new Dimension(150,
        Defaults.getInstance().getControlHeight()));
    wtfQuellKundeMitarbeiter.setActivatable(false);
    vQuellfelder.addElement(wtfQuellKundeMitarbeiter);

    // Tour - nicht editierbar, nur auswaehlbar
    wlaTour = new WrapperLabel();
    wlaTour.setText(LPMain.getInstance().getTextRespectUISPr("part.kund.tour") + ": ");
    wlaTour.setMaximumSize(new Dimension(120, Defaults.getInstance().getControlHeight()));
    wlaTour.setPreferredSize(new Dimension(115, Defaults.getInstance().getControlHeight()));
    wlaTour.setMinimumSize(new Dimension(115, Defaults.getInstance().getControlHeight()));

    wbuTourDatenrichtung = new WrapperButtonZusammenfuehren();
    wbuTourDatenrichtung.addActionListener(this);
    wbuTourDatenrichtung.setActionCommand(ACTION_DATENRICHTUNG_WAEHLEN);
    vWrapperButtonsZusammenfuehren.addElement(wbuTourDatenrichtung);

    wtfZielKundeTour = new WrapperTextFieldZusammenfuehren();
    wtfZielKundeTour.setText("");
    wtfZielKundeTour.setColumnsMax(80);
    wtfZielKundeTour.setMaximumSize(new Dimension(180,
                                                  Defaults.getInstance().getControlHeight()));
    wtfZielKundeTour.setPreferredSize(new Dimension(150,
        Defaults.getInstance().getControlHeight()));
    wtfZielKundeTour.setMinimumSize(new Dimension(150,
                                                  Defaults.getInstance().getControlHeight()));
    wtfZielKundeTour.setActivatable(false);
    vZielfelder.addElement(wtfZielKundeTour);

    wtfQuellKundeTour = new WrapperTextFieldZusammenfuehren();
    wtfQuellKundeTour.setText("");
    wtfQuellKundeTour.setColumnsMax(80);
    wtfQuellKundeTour.setMaximumSize(new Dimension(180,
        Defaults.getInstance().getControlHeight()));
    wtfQuellKundeTour.setPreferredSize(new Dimension(150,
        Defaults.getInstance().getControlHeight()));
    wtfQuellKundeTour.setMinimumSize(new Dimension(150,
        Defaults.getInstance().getControlHeight()));
    wtfQuellKundeTour.setActivatable(false);
    vQuellfelder.addElement(wtfQuellKundeTour);

    // Lieferantennr - nicht editierbar, nur auswaehlbar
    wlaLieferantennr = new WrapperLabel();
    wlaLieferantennr.setText(LPMain.getInstance().getTextRespectUISPr("lp.lieferantennr") +
                             ": ");
    wlaLieferantennr.setMaximumSize(new Dimension(120,
                                                  Defaults.getInstance().getControlHeight()));
    wlaLieferantennr.setPreferredSize(new Dimension(115,
        Defaults.getInstance().getControlHeight()));
    wlaLieferantennr.setMinimumSize(new Dimension(115,
                                                  Defaults.getInstance().getControlHeight()));

    wbuLieferantennrDatenrichtung = new WrapperButtonZusammenfuehren();
    wbuLieferantennrDatenrichtung.addActionListener(this);
    wbuLieferantennrDatenrichtung.setActionCommand(ACTION_DATENRICHTUNG_WAEHLEN);
    vWrapperButtonsZusammenfuehren.addElement(wbuLieferantennrDatenrichtung);

    wtfZielKundeLieferantennr = new WrapperTextFieldZusammenfuehren();
    wtfZielKundeLieferantennr.setText("");
    wtfZielKundeLieferantennr.setMaximumSize(new Dimension(180,
        Defaults.getInstance().getControlHeight()));
    wtfZielKundeLieferantennr.setPreferredSize(new Dimension(150,
        Defaults.getInstance().getControlHeight()));
    wtfZielKundeLieferantennr.setMinimumSize(new Dimension(150,
        Defaults.getInstance().getControlHeight()));
    wtfZielKundeLieferantennr.setActivatable(false);
    vZielfelder.addElement(wtfZielKundeLieferantennr);

    wtfQuellKundeLieferantennr = new WrapperTextFieldZusammenfuehren();
    wtfQuellKundeLieferantennr.setText("");
    wtfQuellKundeLieferantennr.setMaximumSize(new Dimension(180,
        Defaults.getInstance().getControlHeight()));
    wtfQuellKundeLieferantennr.setPreferredSize(new Dimension(150,
        Defaults.getInstance().getControlHeight()));
    wtfQuellKundeLieferantennr.setMinimumSize(new Dimension(150,
        Defaults.getInstance().getControlHeight()));
    wtfQuellKundeLieferantennr.setActivatable(false);
    vQuellfelder.addElement(wtfQuellKundeLieferantennr);

    // Abc - nicht editierbar, nur auswaehlbar
    wlaAbc = new WrapperLabel();
    wlaAbc.setText(LPMain.getInstance().getTextRespectUISPr("lp.abc") + ": ");
    wlaAbc.setMaximumSize(new Dimension(120, Defaults.getInstance().getControlHeight()));
    wlaAbc.setPreferredSize(new Dimension(115, Defaults.getInstance().getControlHeight()));
    wlaAbc.setMinimumSize(new Dimension(115, Defaults.getInstance().getControlHeight()));

    wbuAbcDatenrichtung = new WrapperButtonZusammenfuehren();
    wbuAbcDatenrichtung.addActionListener(this);
    wbuAbcDatenrichtung.setActionCommand(ACTION_DATENRICHTUNG_WAEHLEN);
    vWrapperButtonsZusammenfuehren.addElement(wbuAbcDatenrichtung);

    wtfZielKundeAbc = new WrapperTextFieldZusammenfuehren();
    wtfZielKundeAbc.setText("");
    wtfZielKundeAbc.setMaximumSize(new Dimension(180,
                                                 Defaults.getInstance().getControlHeight()));
    wtfZielKundeAbc.setPreferredSize(new Dimension(150,
        Defaults.getInstance().getControlHeight()));
    wtfZielKundeAbc.setMinimumSize(new Dimension(150,
                                                 Defaults.getInstance().getControlHeight()));
    wtfZielKundeAbc.setActivatable(false);
    vZielfelder.addElement(wtfZielKundeAbc);

    wtfQuellKundeAbc = new WrapperTextFieldZusammenfuehren();
    wtfQuellKundeAbc.setText("");
    wtfQuellKundeAbc.setMaximumSize(new Dimension(180,
                                                  Defaults.getInstance().getControlHeight()));
    wtfQuellKundeAbc.setPreferredSize(new Dimension(150,
        Defaults.getInstance().getControlHeight()));
    wtfQuellKundeAbc.setMinimumSize(new Dimension(150,
                                                  Defaults.getInstance().getControlHeight()));
    wtfQuellKundeAbc.setActivatable(false);
    vQuellfelder.addElement(wtfQuellKundeAbc);

    // Agb - nicht editierbar, nur auswaehlbar
    wlaAgb = new WrapperLabel();
    wlaAgb.setText(LPMain.getInstance().getTextRespectUISPr(
        "part.kund.agb_uebermittelt_am") + ": ");
    wlaAgb.setMaximumSize(new Dimension(130, Defaults.getInstance().getControlHeight()));
    wlaAgb.setPreferredSize(new Dimension(125, Defaults.getInstance().getControlHeight()));
    wlaAgb.setMinimumSize(new Dimension(125, Defaults.getInstance().getControlHeight()));

    wbuAgbDatenrichtung = new WrapperButtonZusammenfuehren();
    wbuAgbDatenrichtung.addActionListener(this);
    wbuAgbDatenrichtung.setActionCommand(ACTION_DATENRICHTUNG_WAEHLEN);
    vWrapperButtonsZusammenfuehren.addElement(wbuAgbDatenrichtung);

    wtfZielKundeAgb = new WrapperTextFieldZusammenfuehren();
    wtfZielKundeAgb.setText("");
    wtfZielKundeAgb.setMaximumSize(new Dimension(180,
                                                 Defaults.getInstance().getControlHeight()));
    wtfZielKundeAgb.setPreferredSize(new Dimension(150,
        Defaults.getInstance().getControlHeight()));
    wtfZielKundeAgb.setMinimumSize(new Dimension(150,
                                                 Defaults.getInstance().getControlHeight()));
    wtfZielKundeAgb.setActivatable(false);
    vZielfelder.addElement(wtfZielKundeAgb);

    wtfQuellKundeAgb = new WrapperTextFieldZusammenfuehren();
    wtfQuellKundeAgb.setText("");
    wtfQuellKundeAgb.setMaximumSize(new Dimension(180,
                                                  Defaults.getInstance().getControlHeight()));
    wtfQuellKundeAgb.setPreferredSize(new Dimension(150,
        Defaults.getInstance().getControlHeight()));
    wtfQuellKundeAgb.setMinimumSize(new Dimension(150,
                                                  Defaults.getInstance().getControlHeight()));
    wtfQuellKundeAgb.setActivatable(false);
    vQuellfelder.addElement(wtfQuellKundeAgb);

    // Fremdsystemnr - nicht editierbar, nur auswaehlbar
    wlaFremdsystemnr = new WrapperLabel();
    wlaFremdsystemnr.setText(LPMain.getInstance().getTextRespectUISPr(
        "part.kund.fremdsystemnr") + ": ");
    wlaFremdsystemnr.setMaximumSize(new Dimension(130,
                                                  Defaults.getInstance().getControlHeight()));
    wlaFremdsystemnr.setPreferredSize(new Dimension(125,
        Defaults.getInstance().getControlHeight()));
    wlaFremdsystemnr.setMinimumSize(new Dimension(125,
                                                  Defaults.getInstance().getControlHeight()));

    wbuFremdsystemnrDatenrichtung = new WrapperButtonZusammenfuehren();
    wbuFremdsystemnrDatenrichtung.addActionListener(this);
    wbuFremdsystemnrDatenrichtung.setActionCommand(ACTION_DATENRICHTUNG_WAEHLEN);
    vWrapperButtonsZusammenfuehren.addElement(wbuFremdsystemnrDatenrichtung);

    wtfZielKundeFremdsystemnr = new WrapperTextFieldZusammenfuehren();
    wtfZielKundeFremdsystemnr.setText("");
    wtfZielKundeFremdsystemnr.setMaximumSize(new Dimension(180,
        Defaults.getInstance().getControlHeight()));
    wtfZielKundeFremdsystemnr.setPreferredSize(new Dimension(150,
        Defaults.getInstance().getControlHeight()));
    wtfZielKundeFremdsystemnr.setMinimumSize(new Dimension(150,
        Defaults.getInstance().getControlHeight()));
    wtfZielKundeFremdsystemnr.setActivatable(false);
    vZielfelder.addElement(wtfZielKundeFremdsystemnr);

    wtfQuellKundeFremdsystemnr = new WrapperTextFieldZusammenfuehren();
    wtfQuellKundeFremdsystemnr.setText("");
    wtfQuellKundeFremdsystemnr.setMaximumSize(new Dimension(180,
        Defaults.getInstance().getControlHeight()));
    wtfQuellKundeFremdsystemnr.setPreferredSize(new Dimension(150,
        Defaults.getInstance().getControlHeight()));
    wtfQuellKundeFremdsystemnr.setMinimumSize(new Dimension(150,
        Defaults.getInstance().getControlHeight()));
    wtfQuellKundeFremdsystemnr.setActivatable(false);
    vQuellfelder.addElement(wtfQuellKundeFremdsystemnr);

    // Erloeskonto - nicht editierbar, nur auswaehlbar
    wlaErloeskonto = new WrapperLabel();
    wlaErloeskonto.setText(LPMain.getInstance().getTextRespectUISPr("lp.erloesekonto") +
                           ": ");
    wlaErloeskonto.setMaximumSize(new Dimension(130,
                                                Defaults.getInstance().getControlHeight()));
    wlaErloeskonto.setPreferredSize(new Dimension(125,
                                                  Defaults.getInstance().getControlHeight()));
    wlaErloeskonto.setMinimumSize(new Dimension(125,
                                                Defaults.getInstance().getControlHeight()));

    wbuErloeskontoDatenrichtung = new WrapperButtonZusammenfuehren();
    wbuErloeskontoDatenrichtung.addActionListener(this);
    wbuErloeskontoDatenrichtung.setActionCommand(ACTION_DATENRICHTUNG_WAEHLEN);
    vWrapperButtonsZusammenfuehren.addElement(wbuErloeskontoDatenrichtung);

    wtfZielKundeErloeskonto = new WrapperTextFieldZusammenfuehren();
    wtfZielKundeErloeskonto.setText("");
    wtfZielKundeErloeskonto.setMaximumSize(new Dimension(180,
        Defaults.getInstance().getControlHeight()));
    wtfZielKundeErloeskonto.setPreferredSize(new Dimension(150,
        Defaults.getInstance().getControlHeight()));
    wtfZielKundeErloeskonto.setMinimumSize(new Dimension(150,
        Defaults.getInstance().getControlHeight()));
    wtfZielKundeErloeskonto.setActivatable(false);
    vZielfelder.addElement(wtfZielKundeErloeskonto);

    wtfQuellKundeErloeskonto = new WrapperTextFieldZusammenfuehren();
    wtfQuellKundeErloeskonto.setText("");
    wtfQuellKundeErloeskonto.setMaximumSize(new Dimension(180,
        Defaults.getInstance().getControlHeight()));
    wtfQuellKundeErloeskonto.setPreferredSize(new Dimension(150,
        Defaults.getInstance().getControlHeight()));
    wtfQuellKundeErloeskonto.setMinimumSize(new Dimension(150,
        Defaults.getInstance().getControlHeight()));
    wtfQuellKundeErloeskonto.setActivatable(false);
    vQuellfelder.addElement(wtfQuellKundeErloeskonto);

    // Debitorenkonto - nicht editierbar, nur auswaehlbar
    wlaDebitorenkonto = new WrapperLabel();
    wlaDebitorenkonto.setText(LPMain.getInstance().getTextRespectUISPr(
        "lp.debitorenkonto") + ": ");
    wlaDebitorenkonto.setMaximumSize(new Dimension(130,
        Defaults.getInstance().getControlHeight()));
    wlaDebitorenkonto.setPreferredSize(new Dimension(125,
        Defaults.getInstance().getControlHeight()));
    wlaDebitorenkonto.setMinimumSize(new Dimension(125,
        Defaults.getInstance().getControlHeight()));

    wbuDebitorenkontoDatenrichtung = new WrapperButtonZusammenfuehren();
    wbuDebitorenkontoDatenrichtung.addActionListener(this);
    wbuDebitorenkontoDatenrichtung.setActionCommand(ACTION_DATENRICHTUNG_WAEHLEN);
    vWrapperButtonsZusammenfuehren.addElement(wbuDebitorenkontoDatenrichtung);

    wtfZielKundeDebitorenkonto = new WrapperTextFieldZusammenfuehren();
    wtfZielKundeDebitorenkonto.setText("");
    wtfZielKundeDebitorenkonto.setMaximumSize(new Dimension(180,
        Defaults.getInstance().getControlHeight()));
    wtfZielKundeDebitorenkonto.setPreferredSize(new Dimension(150,
        Defaults.getInstance().getControlHeight()));
    wtfZielKundeDebitorenkonto.setMinimumSize(new Dimension(150,
        Defaults.getInstance().getControlHeight()));
    wtfZielKundeDebitorenkonto.setActivatable(false);
    vZielfelder.addElement(wtfZielKundeDebitorenkonto);

    wtfQuellKundeDebitorenkonto = new WrapperTextFieldZusammenfuehren();
    wtfQuellKundeDebitorenkonto.setText("");
    wtfQuellKundeDebitorenkonto.setMaximumSize(new Dimension(180,
        Defaults.getInstance().getControlHeight()));
    wtfQuellKundeDebitorenkonto.setPreferredSize(new Dimension(150,
        Defaults.getInstance().getControlHeight()));
    wtfQuellKundeDebitorenkonto.setMinimumSize(new Dimension(150,
        Defaults.getInstance().getControlHeight()));
    wtfQuellKundeDebitorenkonto.setActivatable(false);
    vQuellfelder.addElement(wtfQuellKundeDebitorenkonto);

    // Rechnungsadresspartner - nicht editierbar, nur auswaehlbar
    wlaRechnungsadresspartner = new WrapperLabel();
    wlaRechnungsadresspartner.setText(LPMain.getInstance().getTextRespectUISPr(
        "lp.rechnungsadresse") + ": ");
    wlaRechnungsadresspartner.setMaximumSize(new Dimension(130,
        Defaults.getInstance().getControlHeight()));
    wlaRechnungsadresspartner.setPreferredSize(new Dimension(125,
        Defaults.getInstance().getControlHeight()));
    wlaRechnungsadresspartner.setMinimumSize(new Dimension(125,
        Defaults.getInstance().getControlHeight()));

    wbuRechnungsadresspartnerDatenrichtung = new WrapperButtonZusammenfuehren();
    wbuRechnungsadresspartnerDatenrichtung.addActionListener(this);
    wbuRechnungsadresspartnerDatenrichtung.setActionCommand(ACTION_DATENRICHTUNG_WAEHLEN);
    vWrapperButtonsZusammenfuehren.addElement(wbuRechnungsadresspartnerDatenrichtung);

    wtfZielKundeRechnungsadresspartner = new WrapperTextFieldZusammenfuehren();
    wtfZielKundeRechnungsadresspartner.setText("");
    wtfZielKundeRechnungsadresspartner.setMaximumSize(new Dimension(180,
        Defaults.getInstance().getControlHeight()));
    wtfZielKundeRechnungsadresspartner.setPreferredSize(new Dimension(150,
        Defaults.getInstance().getControlHeight()));
    wtfZielKundeRechnungsadresspartner.setMinimumSize(new Dimension(150,
        Defaults.getInstance().getControlHeight()));
    wtfZielKundeRechnungsadresspartner.setActivatable(false);
    vZielfelder.addElement(wtfZielKundeRechnungsadresspartner);

    wtfQuellKundeRechnungsadresspartner = new WrapperTextFieldZusammenfuehren();
    wtfQuellKundeRechnungsadresspartner.setText("");
    wtfQuellKundeRechnungsadresspartner.setMaximumSize(new Dimension(180,
        Defaults.getInstance().getControlHeight()));
    wtfQuellKundeRechnungsadresspartner.setPreferredSize(new Dimension(150,
        Defaults.getInstance().getControlHeight()));
    wtfQuellKundeRechnungsadresspartner.setMinimumSize(new Dimension(150,
        Defaults.getInstance().getControlHeight()));
    wtfQuellKundeRechnungsadresspartner.setActivatable(false);
    vQuellfelder.addElement(wtfQuellKundeRechnungsadresspartner);

    // HinweisInt - nicht editierbar, nur auswaehlbar
    wlaHinweisInt = new WrapperLabel();
    wlaHinweisInt.setText(LPMain.getInstance().getTextRespectUISPr("kund.hinweisintern") +
                          " ");
    wlaHinweisInt.setMaximumSize(new Dimension(130,
                                               Defaults.getInstance().getControlHeight()));
    wlaHinweisInt.setPreferredSize(new Dimension(125,
                                                 Defaults.getInstance().getControlHeight()));
    wlaHinweisInt.setMinimumSize(new Dimension(125,
                                               Defaults.getInstance().getControlHeight()));

    wbuHinweisIntDatenrichtung = new WrapperButtonZusammenfuehren();
    wbuHinweisIntDatenrichtung.addActionListener(this);
    wbuHinweisIntDatenrichtung.setButtonKombinierbar(true);
    wbuHinweisIntDatenrichtung.setActionCommand(ACTION_DATENRICHTUNG_WAEHLEN);
    vWrapperButtonsZusammenfuehren.addElement(wbuHinweisIntDatenrichtung);

    wtfZielKundeHinweisInt = new WrapperTextFieldZusammenfuehren();
    wtfZielKundeHinweisInt.setText("");
    wtfZielKundeHinweisInt.setColumnsMax(MAX_HINWEISINTERN);
    wtfZielKundeHinweisInt.setMaximumSize(new Dimension(180,
        Defaults.getInstance().getControlHeight()));
    wtfZielKundeHinweisInt.setPreferredSize(new Dimension(150,
        Defaults.getInstance().getControlHeight()));
    wtfZielKundeHinweisInt.setMinimumSize(new Dimension(150,
        Defaults.getInstance().getControlHeight()));
    wtfZielKundeHinweisInt.setActivatable(false);
    vZielfelder.addElement(wtfZielKundeHinweisInt);

    wtfQuellKundeHinweisInt = new WrapperTextFieldZusammenfuehren();
    wtfQuellKundeHinweisInt.setText("");
    wtfQuellKundeHinweisInt.setColumnsMax(MAX_HINWEISINTERN);
    wtfQuellKundeHinweisInt.setMaximumSize(new Dimension(180,
        Defaults.getInstance().getControlHeight()));
    wtfQuellKundeHinweisInt.setPreferredSize(new Dimension(150,
        Defaults.getInstance().getControlHeight()));
    wtfQuellKundeHinweisInt.setMinimumSize(new Dimension(150,
        Defaults.getInstance().getControlHeight()));
    wtfQuellKundeHinweisInt.setActivatable(false);
    vQuellfelder.addElement(wtfQuellKundeHinweisInt);

    // HinweisExt - nicht editierbar, nur auswaehlbar
    wlaHinweisExt = new WrapperLabel();
    wlaHinweisExt.setText(LPMain.getInstance().getTextRespectUISPr("kund.hinweisextern") +
                          " ");
    wlaHinweisExt.setMaximumSize(new Dimension(130,
                                               Defaults.getInstance().getControlHeight()));
    wlaHinweisExt.setPreferredSize(new Dimension(125,
                                                 Defaults.getInstance().getControlHeight()));
    wlaHinweisExt.setMinimumSize(new Dimension(125,
                                               Defaults.getInstance().getControlHeight()));

    wbuHinweisExtDatenrichtung = new WrapperButtonZusammenfuehren();
    wbuHinweisExtDatenrichtung.addActionListener(this);
    wbuHinweisExtDatenrichtung.setButtonKombinierbar(true);
    wbuHinweisExtDatenrichtung.setActionCommand(ACTION_DATENRICHTUNG_WAEHLEN);
    vWrapperButtonsZusammenfuehren.addElement(wbuHinweisExtDatenrichtung);

    wtfZielKundeHinweisExt = new WrapperTextFieldZusammenfuehren();
    wtfZielKundeHinweisExt.setText("");
    wtfZielKundeHinweisExt.setCaretPosition(0);
    wtfZielKundeHinweisExt.setColumnsMax(MAX_HINWEISEXTERN);
    wtfZielKundeHinweisExt.setMaximumSize(new Dimension(180, Defaults.getInstance().getControlHeight()));
    wtfZielKundeHinweisExt.setPreferredSize(new Dimension(150, Defaults.getInstance().getControlHeight()));
    wtfZielKundeHinweisExt.setMinimumSize(new Dimension(150, Defaults.getInstance().getControlHeight()));
    wtfZielKundeHinweisExt.setActivatable(false);
    vZielfelder.addElement(wtfZielKundeHinweisExt);

    wtfQuellKundeHinweisExt = new WrapperTextFieldZusammenfuehren();
    wtfQuellKundeHinweisExt.setText("");
    wtfQuellKundeHinweisExt.setColumnsMax(MAX_HINWEISEXTERN);
    wtfQuellKundeHinweisExt.setMaximumSize(new Dimension(180, Defaults.getInstance().getControlHeight()));
    wtfQuellKundeHinweisExt.setPreferredSize(new Dimension(150, Defaults.getInstance().getControlHeight()));
    wtfQuellKundeHinweisExt.setMinimumSize(new Dimension(150, Defaults.getInstance().getControlHeight()));
    wtfQuellKundeHinweisExt.setActivatable(false);
    vQuellfelder.addElement(wtfQuellKundeHinweisExt);

    // Zessionsfaktor - nicht editierbar, nur auswaehlbar
    wlaZessionsfaktor = new WrapperLabel();
    wlaZessionsfaktor.setText(LPMain.getInstance().getTextRespectUISPr(
        "part.kund.zessionsfaktor") + ": ");
    wlaZessionsfaktor.setMaximumSize(new Dimension(130,
        Defaults.getInstance().getControlHeight()));
    wlaZessionsfaktor.setPreferredSize(new Dimension(125,
        Defaults.getInstance().getControlHeight()));
    wlaZessionsfaktor.setMinimumSize(new Dimension(125,
        Defaults.getInstance().getControlHeight()));

    wbuZessionsfaktorDatenrichtung = new WrapperButtonZusammenfuehren();
    wbuZessionsfaktorDatenrichtung.addActionListener(this);
    wbuZessionsfaktorDatenrichtung.setActionCommand(ACTION_DATENRICHTUNG_WAEHLEN);
    vWrapperButtonsZusammenfuehren.addElement(wbuZessionsfaktorDatenrichtung);

    wtfZielKundeZessionsfaktor = new WrapperTextFieldZusammenfuehren();
    wtfZielKundeZessionsfaktor.setText("");
    wtfZielKundeZessionsfaktor.setMaximumSize(new Dimension(180,
        Defaults.getInstance().getControlHeight()));
    wtfZielKundeZessionsfaktor.setPreferredSize(new Dimension(150,
        Defaults.getInstance().getControlHeight()));
    wtfZielKundeZessionsfaktor.setMinimumSize(new Dimension(150,
        Defaults.getInstance().getControlHeight()));
    wtfZielKundeZessionsfaktor.setActivatable(false);
    vZielfelder.addElement(wtfZielKundeZessionsfaktor);

    wtfQuellKundeZessionsfaktor = new WrapperTextFieldZusammenfuehren();
    wtfQuellKundeZessionsfaktor.setText("");
    wtfQuellKundeZessionsfaktor.setMaximumSize(new Dimension(180,
        Defaults.getInstance().getControlHeight()));
    wtfQuellKundeZessionsfaktor.setPreferredSize(new Dimension(150,
        Defaults.getInstance().getControlHeight()));
    wtfQuellKundeZessionsfaktor.setMinimumSize(new Dimension(150,
        Defaults.getInstance().getControlHeight()));
    wtfQuellKundeZessionsfaktor.setActivatable(false);
    vQuellfelder.addElement(wtfQuellKundeZessionsfaktor);

    // Preisliste - nicht editierbar, nur auswaehlbar
    wlaPreisliste = new WrapperLabel();
    wlaPreisliste.setText(LPMain.getInstance().getTextRespectUISPr("vkpf.preisliste") +
                          ": ");
    wlaPreisliste.setMaximumSize(new Dimension(130,
                                               Defaults.getInstance().getControlHeight()));
    wlaPreisliste.setPreferredSize(new Dimension(125,
                                                 Defaults.getInstance().getControlHeight()));
    wlaPreisliste.setMinimumSize(new Dimension(125,
                                               Defaults.getInstance().getControlHeight()));

    wbuPreislisteDatenrichtung = new WrapperButtonZusammenfuehren();
    wbuPreislisteDatenrichtung.addActionListener(this);
    wbuPreislisteDatenrichtung.setActionCommand(ACTION_DATENRICHTUNG_WAEHLEN);
    vWrapperButtonsZusammenfuehren.addElement(wbuPreislisteDatenrichtung);

    wtfZielKundePreisliste = new WrapperTextFieldZusammenfuehren();
    wtfZielKundePreisliste.setText("");
    wtfZielKundePreisliste.setMaximumSize(new Dimension(180,
        Defaults.getInstance().getControlHeight()));
    wtfZielKundePreisliste.setPreferredSize(new Dimension(150,
        Defaults.getInstance().getControlHeight()));
    wtfZielKundePreisliste.setMinimumSize(new Dimension(150,
        Defaults.getInstance().getControlHeight()));
    wtfZielKundePreisliste.setActivatable(false);
    vZielfelder.addElement(wtfZielKundePreisliste);

    wtfQuellKundePreisliste = new WrapperTextFieldZusammenfuehren();
    wtfQuellKundePreisliste.setText("");
    wtfQuellKundePreisliste.setMaximumSize(new Dimension(180,
        Defaults.getInstance().getControlHeight()));
    wtfQuellKundePreisliste.setPreferredSize(new Dimension(150,
        Defaults.getInstance().getControlHeight()));
    wtfQuellKundePreisliste.setMinimumSize(new Dimension(150,
        Defaults.getInstance().getControlHeight()));
    wtfQuellKundePreisliste.setActivatable(false);
    vQuellfelder.addElement(wtfQuellKundePreisliste);

    // Kundennr - nicht editierbar, nur auswaehlbar
    wlaKundennr = new WrapperLabel();
    wlaKundennr.setText(LPMain.getInstance().getTextRespectUISPr("lp.kundennummer") +
                        ": ");
    wlaKundennr.setMaximumSize(new Dimension(130, Defaults.getInstance().getControlHeight()));
    wlaKundennr.setPreferredSize(new Dimension(125,
                                               Defaults.getInstance().getControlHeight()));
    wlaKundennr.setMinimumSize(new Dimension(125, Defaults.getInstance().getControlHeight()));

    wbuKundennrDatenrichtung = new WrapperButtonZusammenfuehren();
    wbuKundennrDatenrichtung.addActionListener(this);
    wbuKundennrDatenrichtung.setActionCommand(ACTION_DATENRICHTUNG_WAEHLEN);
    vWrapperButtonsZusammenfuehren.addElement(wbuKundennrDatenrichtung);

    wtfZielKundeKundennr = new WrapperTextFieldZusammenfuehren();
    wtfZielKundeKundennr.setText("");
    wtfZielKundeKundennr.setMaximumSize(new Dimension(180,
        Defaults.getInstance().getControlHeight()));
    wtfZielKundeKundennr.setPreferredSize(new Dimension(150,
        Defaults.getInstance().getControlHeight()));
    wtfZielKundeKundennr.setMinimumSize(new Dimension(150,
        Defaults.getInstance().getControlHeight()));
    wtfZielKundeKundennr.setActivatable(false);
    vZielfelder.addElement(wtfZielKundeKundennr);

    wtfQuellKundeKundennr = new WrapperTextFieldZusammenfuehren();
    wtfQuellKundeKundennr.setText("");
    wtfQuellKundeKundennr.setMaximumSize(new Dimension(180,
        Defaults.getInstance().getControlHeight()));
    wtfQuellKundeKundennr.setPreferredSize(new Dimension(150,
        Defaults.getInstance().getControlHeight()));
    wtfQuellKundeKundennr.setMinimumSize(new Dimension(150,
        Defaults.getInstance().getControlHeight()));
    wtfQuellKundeKundennr.setActivatable(false);
    vQuellfelder.addElement(wtfQuellKundeKundennr);

    // Kundensoko - nicht editierbar, nur auswaehlbar
    wlaKundensoko = new WrapperLabel();
    wlaKundensoko.setText(LPMain.getInstance().getTextRespectUISPr("vkpf.kundesoko") +
                        ": ");
    wlaKundensoko.setMaximumSize(new Dimension(130, Defaults.getInstance().getControlHeight()));
    wlaKundensoko.setPreferredSize(new Dimension(125,
                                               Defaults.getInstance().getControlHeight()));
    wlaKundensoko.setMinimumSize(new Dimension(125, Defaults.getInstance().getControlHeight()));

    wbuKundensokoDatenrichtung = new WrapperButtonZusammenfuehren();
    wbuKundensokoDatenrichtung.addActionListener(this);
    wbuKundensokoDatenrichtung.setActionCommand(ACTION_DATENRICHTUNG_WAEHLEN);
    vWrapperButtonsZusammenfuehren.addElement(wbuKundensokoDatenrichtung);

    wtfZielKundeKundensoko = new WrapperTextFieldZusammenfuehren();
    wtfZielKundeKundensoko.setText("");
    wtfZielKundeKundensoko.setMaximumSize(new Dimension(180,
        Defaults.getInstance().getControlHeight()));
    wtfZielKundeKundensoko.setPreferredSize(new Dimension(150,
        Defaults.getInstance().getControlHeight()));
    wtfZielKundeKundensoko.setMinimumSize(new Dimension(150,
        Defaults.getInstance().getControlHeight()));
    wtfZielKundeKundensoko.setActivatable(false);
    vZielfelder.addElement(wtfZielKundeKundensoko);

    wtfQuellKundeKundensoko = new WrapperTextFieldZusammenfuehren();
    wtfQuellKundeKundensoko.setText("");
    wtfQuellKundeKundensoko.setMaximumSize(new Dimension(180,
        Defaults.getInstance().getControlHeight()));
    wtfQuellKundeKundensoko.setPreferredSize(new Dimension(150,
        Defaults.getInstance().getControlHeight()));
    wtfQuellKundeKundensoko.setMinimumSize(new Dimension(150,
        Defaults.getInstance().getControlHeight()));
    wtfQuellKundeKundensoko.setActivatable(false);
    vQuellfelder.addElement(wtfQuellKundeKundensoko);

    // Mindermengenzuschlag - nicht editierbar, nur auswaehlbar
    wlaMindermengenzuschlag = new WrapperLabel();
    wlaMindermengenzuschlag.setText(LPMain.getInstance().getTextRespectUISPr(
        "label.mindermengenzuschlag") + ": ");
    wlaMindermengenzuschlag.setMaximumSize(new Dimension(130,
        Defaults.getInstance().getControlHeight()));
    wlaMindermengenzuschlag.setPreferredSize(new Dimension(125,
        Defaults.getInstance().getControlHeight()));
    wlaMindermengenzuschlag.setMinimumSize(new Dimension(125,
        Defaults.getInstance().getControlHeight()));

    wbuMindermengenzuschlagDatenrichtung = new WrapperButtonZusammenfuehren();
    wbuMindermengenzuschlagDatenrichtung.addActionListener(this);
    wbuMindermengenzuschlagDatenrichtung.setActionCommand(ACTION_DATENRICHTUNG_WAEHLEN);
    vWrapperButtonsZusammenfuehren.addElement(wbuMindermengenzuschlagDatenrichtung);

    wcbZielKundeMindermengenzuschlag = new WrapperCheckBoxZusammenfuehren();
    wcbZielKundeMindermengenzuschlag.setPreferredSize(new Dimension(17, Defaults.getInstance().getControlHeight()));
    wcbZielKundeMindermengenzuschlag.setShort(Helper.boolean2Short(false));
    wcbZielKundeMindermengenzuschlag.setActivatable(false);
    vZielfelder.addElement(wcbZielKundeMindermengenzuschlag);

    wcbQuellKundeMindermengenzuschlag = new WrapperCheckBoxZusammenfuehren();
    wcbQuellKundeMindermengenzuschlag.setPreferredSize(new Dimension(17, Defaults.getInstance().getControlHeight()));
    wcbQuellKundeMindermengenzuschlag.setShort(Helper.boolean2Short(false));
    wcbQuellKundeMindermengenzuschlag.setActivatable(false);
    vQuellfelder.addElement(wcbQuellKundeMindermengenzuschlag);


    // Monatsrechnung - nicht editierbar, nur auswaehlbar
    wlaMonatsrechnung = new WrapperLabel();
    wlaMonatsrechnung.setText(LPMain.getInstance().getTextRespectUISPr(
        "part.monatsrechnung") + ": ");
    wlaMonatsrechnung.setMaximumSize(new Dimension(130,
        Defaults.getInstance().getControlHeight()));
    wlaMonatsrechnung.setPreferredSize(new Dimension(125,
        Defaults.getInstance().getControlHeight()));
    wlaMonatsrechnung.setMinimumSize(new Dimension(125,
        Defaults.getInstance().getControlHeight()));

    wbuMonatsrechnungDatenrichtung = new WrapperButtonZusammenfuehren();
    wbuMonatsrechnungDatenrichtung.addActionListener(this);
    wbuMonatsrechnungDatenrichtung.setActionCommand(ACTION_DATENRICHTUNG_WAEHLEN);
    vWrapperButtonsZusammenfuehren.addElement(wbuMonatsrechnungDatenrichtung);

    wcbZielKundeMonatsrechnung = new WrapperCheckBoxZusammenfuehren();
    wcbZielKundeMonatsrechnung.setPreferredSize(new Dimension(17, Defaults.getInstance().getControlHeight()));
    wcbZielKundeMonatsrechnung.setShort(Helper.boolean2Short(false));
    wcbZielKundeMonatsrechnung.setActivatable(false);
    vZielfelder.addElement(wcbZielKundeMonatsrechnung);

    wcbQuellKundeMonatsrechnung = new WrapperCheckBoxZusammenfuehren();
    wcbQuellKundeMonatsrechnung.setPreferredSize(new Dimension(17, Defaults.getInstance().getControlHeight()));
    wcbQuellKundeMonatsrechnung.setShort(Helper.boolean2Short(false));
    wcbQuellKundeMonatsrechnung.setActivatable(false);
    vQuellfelder.addElement(wcbQuellKundeMonatsrechnung);

    // Sammelrechnung - nicht editierbar, nur auswaehlbar
    wlaSammelrechnung = new WrapperLabel();
    wlaSammelrechnung.setText(LPMain.getInstance().getTextRespectUISPr(
        "part.bekommt_sammelrechnung") + ": ");
    wlaSammelrechnung.setMaximumSize(new Dimension(140,
        Defaults.getInstance().getControlHeight()));
    wlaSammelrechnung.setPreferredSize(new Dimension(135,
        Defaults.getInstance().getControlHeight()));
    wlaSammelrechnung.setMinimumSize(new Dimension(135,
        Defaults.getInstance().getControlHeight()));

    wbuSammelrechnungDatenrichtung = new WrapperButtonZusammenfuehren();
    wbuSammelrechnungDatenrichtung.addActionListener(this);
    wbuSammelrechnungDatenrichtung.setActionCommand(ACTION_DATENRICHTUNG_WAEHLEN);
    vWrapperButtonsZusammenfuehren.addElement(wbuSammelrechnungDatenrichtung);

    wcbZielKundeSammelrechnung = new WrapperCheckBoxZusammenfuehren();
    wcbZielKundeSammelrechnung.setPreferredSize(new Dimension(17, Defaults.getInstance().getControlHeight()));
    wcbZielKundeSammelrechnung.setShort(Helper.boolean2Short(false));
    wcbZielKundeSammelrechnung.setActivatable(false);
    vZielfelder.addElement(wcbZielKundeSammelrechnung);

    wcbQuellKundeSammelrechnung = new WrapperCheckBoxZusammenfuehren();
    wcbQuellKundeSammelrechnung.setPreferredSize(new Dimension(17, Defaults.getInstance().getControlHeight()));
    wcbQuellKundeSammelrechnung.setShort(Helper.boolean2Short(false));
    wcbQuellKundeSammelrechnung.setActivatable(false);
    vQuellfelder.addElement(wcbQuellKundeSammelrechnung);

    // Rechnungsempfaenger - nicht editierbar, nur auswaehlbar
    wlaRechnungsempfaenger = new WrapperLabel();
    wlaRechnungsempfaenger.setText(LPMain.getInstance().getTextRespectUISPr(
        "part.ist_rechnungempfaenger") + ": ");
    wlaRechnungsempfaenger.setMaximumSize(new Dimension(140,
        Defaults.getInstance().getControlHeight()));
    wlaRechnungsempfaenger.setPreferredSize(new Dimension(135,
        Defaults.getInstance().getControlHeight()));
    wlaRechnungsempfaenger.setMinimumSize(new Dimension(135,
        Defaults.getInstance().getControlHeight()));

    wbuRechnungsempfaengerDatenrichtung = new WrapperButtonZusammenfuehren();
    wbuRechnungsempfaengerDatenrichtung.addActionListener(this);
    wbuRechnungsempfaengerDatenrichtung.setActionCommand(ACTION_DATENRICHTUNG_WAEHLEN);
    vWrapperButtonsZusammenfuehren.addElement(wbuRechnungsempfaengerDatenrichtung);

    wcbZielKundeRechnungsempfaenger = new WrapperCheckBoxZusammenfuehren();
    wcbZielKundeRechnungsempfaenger.setPreferredSize(new Dimension(17, Defaults.getInstance().getControlHeight()));
    wcbZielKundeRechnungsempfaenger.setShort(Helper.boolean2Short(false));
    wcbZielKundeRechnungsempfaenger.setActivatable(false);
    vZielfelder.addElement(wcbZielKundeRechnungsempfaenger);

    wcbQuellKundeRechnungsempfaenger = new WrapperCheckBoxZusammenfuehren();
    wcbQuellKundeRechnungsempfaenger.setPreferredSize(new Dimension(17, Defaults.getInstance().getControlHeight()));
    wcbQuellKundeRechnungsempfaenger.setShort(Helper.boolean2Short(false));
    wcbQuellKundeRechnungsempfaenger.setActivatable(false);
    vQuellfelder.addElement(wcbQuellKundeRechnungsempfaenger);

    // PreiseAmLs - nicht editierbar, nur auswaehlbar
    wlaPreiseAmLs = new WrapperLabel();
    wlaPreiseAmLs.setText(LPMain.getInstance().getTextRespectUISPr(
        "part.preise_an_ls_andrucken") + ": ");
    wlaPreiseAmLs.setMaximumSize(new Dimension(140,
        Defaults.getInstance().getControlHeight()));
    wlaPreiseAmLs.setPreferredSize(new Dimension(135,
        Defaults.getInstance().getControlHeight()));
    wlaPreiseAmLs.setMinimumSize(new Dimension(135,
        Defaults.getInstance().getControlHeight()));

    wbuPreiseAmLsDatenrichtung = new WrapperButtonZusammenfuehren();
    wbuPreiseAmLsDatenrichtung.addActionListener(this);
    wbuPreiseAmLsDatenrichtung.setActionCommand(ACTION_DATENRICHTUNG_WAEHLEN);
    vWrapperButtonsZusammenfuehren.addElement(wbuPreiseAmLsDatenrichtung);

    wcbZielKundePreiseAmLs = new WrapperCheckBoxZusammenfuehren();
    wcbZielKundePreiseAmLs.setPreferredSize(new Dimension(17, Defaults.getInstance().getControlHeight()));
    wcbZielKundePreiseAmLs.setShort(Helper.boolean2Short(false));
    wcbZielKundePreiseAmLs.setActivatable(false);
    vZielfelder.addElement(wcbZielKundePreiseAmLs);

    wcbQuellKundePreiseAmLs = new WrapperCheckBoxZusammenfuehren();
    wcbQuellKundePreiseAmLs.setPreferredSize(new Dimension(17, Defaults.getInstance().getControlHeight()));
    wcbQuellKundePreiseAmLs.setShort(Helper.boolean2Short(false));
    wcbQuellKundePreiseAmLs.setActivatable(false);
    vQuellfelder.addElement(wcbQuellKundePreiseAmLs);

    // BelegdruckMitRabatt - nicht editierbar, nur auswaehlbar
    wlaBelegdruckMitRabatt = new WrapperLabel();
    wlaBelegdruckMitRabatt.setText(LPMain.getInstance().getTextRespectUISPr(
        "part.rechnungsdruck_mit_rabatt") + ": ");
    wlaBelegdruckMitRabatt.setMaximumSize(new Dimension(140,
        Defaults.getInstance().getControlHeight()));
    wlaBelegdruckMitRabatt.setPreferredSize(new Dimension(135,
        Defaults.getInstance().getControlHeight()));
    wlaBelegdruckMitRabatt.setMinimumSize(new Dimension(135,
        Defaults.getInstance().getControlHeight()));

    wbuBelegdruckMitRabattDatenrichtung = new WrapperButtonZusammenfuehren();
    wbuBelegdruckMitRabattDatenrichtung.addActionListener(this);
    wbuBelegdruckMitRabattDatenrichtung.setActionCommand(ACTION_DATENRICHTUNG_WAEHLEN);
    vWrapperButtonsZusammenfuehren.addElement(wbuBelegdruckMitRabattDatenrichtung);

    wcbZielKundeBelegdruckMitRabatt = new WrapperCheckBoxZusammenfuehren();
    wcbZielKundeBelegdruckMitRabatt.setPreferredSize(new Dimension(17, Defaults.getInstance().getControlHeight()));
    wcbZielKundeBelegdruckMitRabatt.setShort(Helper.boolean2Short(false));
    wcbZielKundeBelegdruckMitRabatt.setActivatable(false);
    vZielfelder.addElement(wcbZielKundeBelegdruckMitRabatt);

    wcbQuellKundeBelegdruckMitRabatt = new WrapperCheckBoxZusammenfuehren();
    wcbQuellKundeBelegdruckMitRabatt.setPreferredSize(new Dimension(17, Defaults.getInstance().getControlHeight()));
    wcbQuellKundeBelegdruckMitRabatt.setShort(Helper.boolean2Short(false));
    wcbQuellKundeBelegdruckMitRabatt.setActivatable(false);
    vQuellfelder.addElement(wcbQuellKundeBelegdruckMitRabatt);

    // Distributor - nicht editierbar, nur auswaehlbar
    wlaDistributor = new WrapperLabel();
    wlaDistributor.setText(LPMain.getInstance().getTextRespectUISPr(
        "part.ist_distributor") + ": ");
    wlaDistributor.setMaximumSize(new Dimension(140,
        Defaults.getInstance().getControlHeight()));
    wlaDistributor.setPreferredSize(new Dimension(135,
        Defaults.getInstance().getControlHeight()));
    wlaDistributor.setMinimumSize(new Dimension(135,
        Defaults.getInstance().getControlHeight()));

    wbuDistributorDatenrichtung = new WrapperButtonZusammenfuehren();
    wbuDistributorDatenrichtung.addActionListener(this);
    wbuDistributorDatenrichtung.setActionCommand(ACTION_DATENRICHTUNG_WAEHLEN);
    vWrapperButtonsZusammenfuehren.addElement(wbuDistributorDatenrichtung);

    wcbZielKundeDistributor = new WrapperCheckBoxZusammenfuehren();
    wcbZielKundeDistributor.setPreferredSize(new Dimension(17, Defaults.getInstance().getControlHeight()));
    wcbZielKundeDistributor.setShort(Helper.boolean2Short(false));
    wcbZielKundeDistributor.setActivatable(false);
    vZielfelder.addElement(wcbZielKundeDistributor);

    wcbQuellKundeDistributor = new WrapperCheckBoxZusammenfuehren();
    wcbQuellKundeDistributor.setPreferredSize(new Dimension(17, Defaults.getInstance().getControlHeight()));
    wcbQuellKundeDistributor.setShort(Helper.boolean2Short(false));
    wcbQuellKundeDistributor.setActivatable(false);
    vQuellfelder.addElement(wcbQuellKundeDistributor);

    // Teillieferungen - nicht editierbar, nur auswaehlbar
    wlaTeillieferungen = new WrapperLabel();
    wlaTeillieferungen.setText(LPMain.getInstance().getTextRespectUISPr(
        "part.akzeptiert_teillieferung") + ": ");
    wlaTeillieferungen.setMaximumSize(new Dimension(140,
        Defaults.getInstance().getControlHeight()));
    wlaTeillieferungen.setPreferredSize(new Dimension(135,
        Defaults.getInstance().getControlHeight()));
    wlaTeillieferungen.setMinimumSize(new Dimension(135,
        Defaults.getInstance().getControlHeight()));

    wbuTeillieferungenDatenrichtung = new WrapperButtonZusammenfuehren();
    wbuTeillieferungenDatenrichtung.addActionListener(this);
    wbuTeillieferungenDatenrichtung.setActionCommand(ACTION_DATENRICHTUNG_WAEHLEN);
    vWrapperButtonsZusammenfuehren.addElement(wbuTeillieferungenDatenrichtung);

    wcbZielKundeTeillieferungen = new WrapperCheckBoxZusammenfuehren();
    wcbZielKundeTeillieferungen.setPreferredSize(new Dimension(17, Defaults.getInstance().getControlHeight()));
    wcbZielKundeTeillieferungen.setShort(Helper.boolean2Short(false));
    wcbZielKundeTeillieferungen.setActivatable(false);
    vZielfelder.addElement(wcbZielKundeTeillieferungen);

    wcbQuellKundeTeillieferungen = new WrapperCheckBoxZusammenfuehren();
    wcbQuellKundeTeillieferungen.setPreferredSize(new Dimension(17, Defaults.getInstance().getControlHeight()));
    wcbQuellKundeTeillieferungen.setShort(Helper.boolean2Short(false));
    wcbQuellKundeTeillieferungen.setActivatable(false);
    vQuellfelder.addElement(wcbQuellKundeTeillieferungen);

    // Lsgewicht - nicht editierbar, nur auswaehlbar
    wlaLsgewicht = new WrapperLabel();
    wlaLsgewicht.setText(LPMain.getInstance().getTextRespectUISPr(
        "part.ls_gewicht_angeben") + ": ");
    wlaLsgewicht.setMaximumSize(new Dimension(140,
        Defaults.getInstance().getControlHeight()));
    wlaLsgewicht.setPreferredSize(new Dimension(135,
        Defaults.getInstance().getControlHeight()));
    wlaLsgewicht.setMinimumSize(new Dimension(135,
        Defaults.getInstance().getControlHeight()));

    wbuLsgewichtDatenrichtung = new WrapperButtonZusammenfuehren();
    wbuLsgewichtDatenrichtung.addActionListener(this);
    wbuLsgewichtDatenrichtung.setActionCommand(ACTION_DATENRICHTUNG_WAEHLEN);
    vWrapperButtonsZusammenfuehren.addElement(wbuLsgewichtDatenrichtung);

    wcbZielKundeLsgewicht = new WrapperCheckBoxZusammenfuehren();
    wcbZielKundeLsgewicht.setPreferredSize(new Dimension(17, Defaults.getInstance().getControlHeight()));
    wcbZielKundeLsgewicht.setShort(Helper.boolean2Short(false));
    wcbZielKundeLsgewicht.setActivatable(false);
    vZielfelder.addElement(wcbZielKundeLsgewicht);

    wcbQuellKundeLsgewicht = new WrapperCheckBoxZusammenfuehren();
    wcbQuellKundeLsgewicht.setPreferredSize(new Dimension(17, Defaults.getInstance().getControlHeight()));
    wcbQuellKundeLsgewicht.setShort(Helper.boolean2Short(false));
    wcbQuellKundeLsgewicht.setActivatable(false);
    vQuellfelder.addElement(wcbQuellKundeLsgewicht);

    // Interessent - nicht editierbar, nur auswaehlbar
    wlaInteressent = new WrapperLabel();
    wlaInteressent.setText(LPMain.getInstance().getTextRespectUISPr(
        "lp.interessent") + ": ");
    wlaInteressent.setMaximumSize(new Dimension(140,
        Defaults.getInstance().getControlHeight()));
    wlaInteressent.setPreferredSize(new Dimension(135,
        Defaults.getInstance().getControlHeight()));
    wlaInteressent.setMinimumSize(new Dimension(135,
        Defaults.getInstance().getControlHeight()));

    wbuInteressentDatenrichtung = new WrapperButtonZusammenfuehren();
    wbuInteressentDatenrichtung.addActionListener(this);
    wbuInteressentDatenrichtung.setActionCommand(ACTION_DATENRICHTUNG_WAEHLEN);
    vWrapperButtonsZusammenfuehren.addElement(wbuInteressentDatenrichtung);

    wcbZielKundeInteressent = new WrapperCheckBoxZusammenfuehren();
    wcbZielKundeInteressent.setPreferredSize(new Dimension(17, Defaults.getInstance().getControlHeight()));
    wcbZielKundeInteressent.setShort(Helper.boolean2Short(false));
    wcbZielKundeInteressent.setActivatable(false);
    vZielfelder.addElement(wcbZielKundeInteressent);

    wcbQuellKundeInteressent = new WrapperCheckBoxZusammenfuehren();
    wcbQuellKundeInteressent.setPreferredSize(new Dimension(17, Defaults.getInstance().getControlHeight()));
    wcbQuellKundeInteressent.setShort(Helper.boolean2Short(false));
    wcbQuellKundeInteressent.setActivatable(false);
    vQuellfelder.addElement(wcbQuellKundeInteressent);

    // Versteckt - nicht editierbar, nur auswaehlbar
    wlaVersteckt = new WrapperLabel();
    wlaVersteckt.setText(LPMain.getInstance().getTextRespectUISPr(
        "lp.versteckt") + ": ");
    wlaVersteckt.setMaximumSize(new Dimension(140,
        Defaults.getInstance().getControlHeight()));
    wlaVersteckt.setPreferredSize(new Dimension(135,
        Defaults.getInstance().getControlHeight()));
    wlaVersteckt.setMinimumSize(new Dimension(135,
        Defaults.getInstance().getControlHeight()));

    wbuVerstecktDatenrichtung = new WrapperButtonZusammenfuehren();
    wbuVerstecktDatenrichtung.addActionListener(this);
    wbuVerstecktDatenrichtung.setActionCommand(ACTION_DATENRICHTUNG_WAEHLEN);
    vWrapperButtonsZusammenfuehren.addElement(wbuVerstecktDatenrichtung);

    wcbZielKundeVersteckt = new WrapperCheckBoxZusammenfuehren();
    wcbZielKundeVersteckt.setPreferredSize(new Dimension(17, Defaults.getInstance().getControlHeight()));
    wcbZielKundeVersteckt.setShort(Helper.boolean2Short(false));
    wcbZielKundeVersteckt.setActivatable(false);
    vZielfelder.addElement(wcbZielKundeVersteckt);

    wcbQuellKundeVersteckt = new WrapperCheckBoxZusammenfuehren();
    wcbQuellKundeVersteckt.setPreferredSize(new Dimension(17, Defaults.getInstance().getControlHeight()));
    wcbQuellKundeVersteckt.setShort(Helper.boolean2Short(false));
    wcbQuellKundeVersteckt.setActivatable(false);
    vQuellfelder.addElement(wcbQuellKundeVersteckt);

    // Reversecharge - nicht editierbar, nur auswaehlbar
    wlaReversecharge = new WrapperLabel();
    wlaReversecharge.setText(LPMain.getInstance().getTextRespectUISPr(
        "lp.reversecharge") + ": ");
    wlaReversecharge.setMaximumSize(new Dimension(140,
        Defaults.getInstance().getControlHeight()));
    wlaReversecharge.setPreferredSize(new Dimension(135,
        Defaults.getInstance().getControlHeight()));
    wlaReversecharge.setMinimumSize(new Dimension(135,
        Defaults.getInstance().getControlHeight()));

    wbuReversechargeDatenrichtung = new WrapperButtonZusammenfuehren();
    wbuReversechargeDatenrichtung.addActionListener(this);
    wbuReversechargeDatenrichtung.setActionCommand(ACTION_DATENRICHTUNG_WAEHLEN);
    vWrapperButtonsZusammenfuehren.addElement(wbuReversechargeDatenrichtung);

    wcbZielKundeReversecharge = new WrapperCheckBoxZusammenfuehren();
    wcbZielKundeReversecharge.setPreferredSize(new Dimension(17, Defaults.getInstance().getControlHeight()));
    wcbZielKundeReversecharge.setShort(Helper.boolean2Short(false));
    wcbZielKundeReversecharge.setActivatable(false);
    vZielfelder.addElement(wcbZielKundeReversecharge);

    wcbQuellKundeReversecharge = new WrapperCheckBoxZusammenfuehren();
    wcbQuellKundeReversecharge.setPreferredSize(new Dimension(17, Defaults.getInstance().getControlHeight()));
    wcbQuellKundeReversecharge.setShort(Helper.boolean2Short(false));
    wcbQuellKundeReversecharge.setActivatable(false);
    vQuellfelder.addElement(wcbQuellKundeReversecharge);


    /*
    jpaWorkingOnScroll.add(wbuZielKundeAuswaehlen,
                           new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.FIRST_LINE_END,
                                                  GridBagConstraints.NONE,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    jpaWorkingOnScroll.add(wbuAuswahlReset,
                           new GridBagConstraints(2, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    jpaWorkingOnScroll.add(wbuQuellKundeAuswaehlen,
                           new GridBagConstraints(3, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.FIRST_LINE_END,
                                                  GridBagConstraints.NONE,
                                                  new Insets(0, 0, 0, 0), 0, 0));
*/

    jpaWorkingOnScroll.add(wbuZielKundeAuswaehlen,
                         new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0
                                                , GridBagConstraints.CENTER,
                                                GridBagConstraints.HORIZONTAL,
                                                new Insets(0, 0, 0, 0), 0, 0));

    jpaWorkingOnScroll.add(wbuAuswahlReset,
                         new GridBagConstraints(2, iZeile, 1, 1, 0.0, 0.0
                                                , GridBagConstraints.CENTER,
                                                GridBagConstraints.HORIZONTAL,
                                                new Insets(0, 0, 0, 0), 0, 0));

    jpaWorkingOnScroll.add(wbuQuellKundeAuswaehlen,
                         new GridBagConstraints(3, iZeile, 1, 1, 0.0, 0.0
                                                , GridBagConstraints.CENTER,
                                                GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    // Zeile
    iZeile++;

    jpaWorkingOnScroll.add(wlaPartnername,
                           new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    jpaWorkingOnScroll.add(wtfZielKundePartnername,
                           new GridBagConstraints(1, iZeile, 1, 1, 0.1, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    jpaWorkingOnScroll.add(wbuPartnernameDatenrichtung,
                           new GridBagConstraints(2, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    jpaWorkingOnScroll.add(wtfQuellKundePartnername,
                           new GridBagConstraints(3, iZeile, 1, 1, 0.1, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    // Zeile
    iZeile++;

    jpaWorkingOnScroll.add(wlaMwst, new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0
        , GridBagConstraints.CENTER,
        GridBagConstraints.HORIZONTAL,
        new Insets(0, 0, 0, 0), 0, 0));

    jpaWorkingOnScroll.add(wtfZielKundeMwst,
                           new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

   /* jpaWorkingOnScroll.add(wbuMwstDatenrichtung,
                           new GridBagConstraints(2, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));*/

    jpaWorkingOnScroll.add(wtfQuellKundeMwst,
                           new GridBagConstraints(3, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    // Zeile
    iZeile++;

    jpaWorkingOnScroll.add(wlaWaehrung, new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0
        , GridBagConstraints.CENTER,
        GridBagConstraints.HORIZONTAL,
        new Insets(0, 0, 0, 0), 0, 0));

    jpaWorkingOnScroll.add(wtfZielKundeWaehrung,
                           new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    jpaWorkingOnScroll.add(wbuWaehrungDatenrichtung,
                           new GridBagConstraints(2, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    jpaWorkingOnScroll.add(wtfQuellKundeWaehrung,
                           new GridBagConstraints(3, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    // Zeile
    iZeile++;

    jpaWorkingOnScroll.add(wlaLieferart, new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0
        , GridBagConstraints.CENTER,
        GridBagConstraints.HORIZONTAL,
        new Insets(0, 0, 0, 0), 0, 0));

    jpaWorkingOnScroll.add(wtfZielKundeLieferart,
                           new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    jpaWorkingOnScroll.add(wbuLieferartDatenrichtung,
                           new GridBagConstraints(2, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    jpaWorkingOnScroll.add(wtfQuellKundeLieferart,
                           new GridBagConstraints(3, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    // Zeile
    iZeile++;

    jpaWorkingOnScroll.add(wlaSpediteur, new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0
        , GridBagConstraints.CENTER,
        GridBagConstraints.HORIZONTAL,
        new Insets(0, 0, 0, 0), 0, 0));

    jpaWorkingOnScroll.add(wtfZielKundeSpediteur,
                           new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    jpaWorkingOnScroll.add(wbuSpediteurDatenrichtung,
                           new GridBagConstraints(2, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    jpaWorkingOnScroll.add(wtfQuellKundeSpediteur,
                           new GridBagConstraints(3, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    // Zeile
    iZeile++;

    jpaWorkingOnScroll.add(wlaZahlungsziel,
                           new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    jpaWorkingOnScroll.add(wtfZielKundeZahlungsziel,
                           new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    jpaWorkingOnScroll.add(wbuZahlungszielDatenrichtung,
                           new GridBagConstraints(2, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    jpaWorkingOnScroll.add(wtfQuellKundeZahlungsziel,
                           new GridBagConstraints(3, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    // Zeile
    iZeile++;

    jpaWorkingOnScroll.add(wlaKostenstelle,
                           new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    jpaWorkingOnScroll.add(wtfZielKundeKostenstelle,
                           new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    jpaWorkingOnScroll.add(wbuKostenstelleDatenrichtung,
                           new GridBagConstraints(2, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    jpaWorkingOnScroll.add(wtfQuellKundeKostenstelle,
                           new GridBagConstraints(3, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    // Zeile
    iZeile++;

    jpaWorkingOnScroll.add(wlaProvisionsempfaenger,
                           new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    jpaWorkingOnScroll.add(wtfZielKundeProvisionsempfaenger,
                           new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    jpaWorkingOnScroll.add(wbuProvisionsempfaengerDatenrichtung,
                           new GridBagConstraints(2, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    jpaWorkingOnScroll.add(wtfQuellKundeProvisionsempfaenger,
                           new GridBagConstraints(3, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    // Zeile
    iZeile++;

    jpaWorkingOnScroll.add(wlaRabattsatz,
                           new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    jpaWorkingOnScroll.add(wtfZielKundeRabattsatz,
                           new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    jpaWorkingOnScroll.add(wbuRabattsatzDatenrichtung,
                           new GridBagConstraints(2, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    jpaWorkingOnScroll.add(wtfQuellKundeRabattsatz,
                           new GridBagConstraints(3, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    // Zeile
    iZeile++;

    jpaWorkingOnScroll.add(wlaGarantie, new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0
        , GridBagConstraints.CENTER,
        GridBagConstraints.HORIZONTAL,
        new Insets(0, 0, 0, 0), 0, 0));

    jpaWorkingOnScroll.add(wtfZielKundeGarantie,
                           new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    jpaWorkingOnScroll.add(wbuGarantieDatenrichtung,
                           new GridBagConstraints(2, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    jpaWorkingOnScroll.add(wtfQuellKundeGarantie,
                           new GridBagConstraints(3, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    // Zeile
    iZeile++;

    jpaWorkingOnScroll.add(wlaKommentar, new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0
        , GridBagConstraints.CENTER,
        GridBagConstraints.HORIZONTAL,
        new Insets(0, 0, 0, 0), 0, 0));

    jpaWorkingOnScroll.add(jScrollPaneZielKundeKommentar,
                           new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    jpaWorkingOnScroll.add(wbuKommentarDatenrichtung,
                           new GridBagConstraints(2, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    jpaWorkingOnScroll.add(jScrollPaneQuellKundeKommentar,
                           new GridBagConstraints(3, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    // Zeile
    iZeile++;

    jpaWorkingOnScroll.add(wlaKurznr, new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0
        , GridBagConstraints.CENTER,
        GridBagConstraints.HORIZONTAL,
        new Insets(0, 0, 0, 0), 0, 0));

    jpaWorkingOnScroll.add(wtfZielKundeKurznr,
                           new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    jpaWorkingOnScroll.add(wbuKurznrDatenrichtung,
                           new GridBagConstraints(2, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    jpaWorkingOnScroll.add(wtfQuellKundeKurznr,
                           new GridBagConstraints(3, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    // Zeile
    iZeile++;

    jpaWorkingOnScroll.add(wlaKreditlimit,
                           new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    jpaWorkingOnScroll.add(wtfZielKundeKreditlimit,
                           new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    jpaWorkingOnScroll.add(wbuKreditlimitDatenrichtung,
                           new GridBagConstraints(2, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    jpaWorkingOnScroll.add(wtfQuellKundeKreditlimit,
                           new GridBagConstraints(3, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    // Zeile
    iZeile++;

    jpaWorkingOnScroll.add(wlaBonitaetspruefung,
                           new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    jpaWorkingOnScroll.add(wtfZielKundeBonitaetspruefung,
                           new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    jpaWorkingOnScroll.add(wbuBonitaetspruefungDatenrichtung,
                           new GridBagConstraints(2, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    jpaWorkingOnScroll.add(wtfQuellKundeBonitaetspruefung,
                           new GridBagConstraints(3, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    // Zeile
    iZeile++;

    jpaWorkingOnScroll.add(wlaLiefersperre,
                           new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    jpaWorkingOnScroll.add(wtfZielKundeLiefersperre,
                           new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    jpaWorkingOnScroll.add(wbuLiefersperreDatenrichtung,
                           new GridBagConstraints(2, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    jpaWorkingOnScroll.add(wtfQuellKundeLiefersperre,
                           new GridBagConstraints(3, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    // Zeile
    iZeile++;

    jpaWorkingOnScroll.add(wlaRechnungskopien,
                           new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    jpaWorkingOnScroll.add(wtfZielKundeRechnungskopien,
                           new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    jpaWorkingOnScroll.add(wbuRechnungskopienDatenrichtung,
                           new GridBagConstraints(2, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    jpaWorkingOnScroll.add(wtfQuellKundeRechnungskopien,
                           new GridBagConstraints(3, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    // Zeile
    iZeile++;

    jpaWorkingOnScroll.add(wlaLieferscheinkopien,
                           new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    jpaWorkingOnScroll.add(wtfZielKundeLieferscheinkopien,
                           new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    jpaWorkingOnScroll.add(wbuLieferscheinkopienDatenrichtung,
                           new GridBagConstraints(2, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    jpaWorkingOnScroll.add(wtfQuellKundeLieferscheinkopien,
                           new GridBagConstraints(3, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    // Zeile
    iZeile++;

    jpaWorkingOnScroll.add(wlaMitarbeiter,
                           new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    jpaWorkingOnScroll.add(wtfZielKundeMitarbeiter,
                           new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    jpaWorkingOnScroll.add(wbuMitarbeiterDatenrichtung,
                           new GridBagConstraints(2, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    jpaWorkingOnScroll.add(wtfQuellKundeMitarbeiter,
                           new GridBagConstraints(3, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    // Zeile
    iZeile++;

    jpaWorkingOnScroll.add(wlaTour, new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0
        , GridBagConstraints.CENTER,
        GridBagConstraints.HORIZONTAL,
        new Insets(0, 0, 0, 0), 0, 0));

    jpaWorkingOnScroll.add(wtfZielKundeTour,
                           new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    jpaWorkingOnScroll.add(wbuTourDatenrichtung,
                           new GridBagConstraints(2, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    jpaWorkingOnScroll.add(wtfQuellKundeTour,
                           new GridBagConstraints(3, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    // Zeile
    iZeile++;

    jpaWorkingOnScroll.add(wlaLieferantennr,
                           new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    jpaWorkingOnScroll.add(wtfZielKundeLieferantennr,
                           new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    jpaWorkingOnScroll.add(wbuLieferantennrDatenrichtung,
                           new GridBagConstraints(2, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    jpaWorkingOnScroll.add(wtfQuellKundeLieferantennr,
                           new GridBagConstraints(3, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    // Zeile
    iZeile++;

    jpaWorkingOnScroll.add(wlaAbc, new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0
        , GridBagConstraints.CENTER,
        GridBagConstraints.HORIZONTAL,
        new Insets(0, 0, 0, 0), 0, 0));

    jpaWorkingOnScroll.add(wtfZielKundeAbc,
                           new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    jpaWorkingOnScroll.add(wbuAbcDatenrichtung,
                           new GridBagConstraints(2, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    jpaWorkingOnScroll.add(wtfQuellKundeAbc,
                           new GridBagConstraints(3, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    // Zeile
    iZeile++;

    jpaWorkingOnScroll.add(wlaAgb, new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0
        , GridBagConstraints.CENTER,
        GridBagConstraints.HORIZONTAL,
        new Insets(0, 0, 0, 0), 0, 0));

    jpaWorkingOnScroll.add(wtfZielKundeAgb,
                           new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    jpaWorkingOnScroll.add(wbuAgbDatenrichtung,
                           new GridBagConstraints(2, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    jpaWorkingOnScroll.add(wtfQuellKundeAgb,
                           new GridBagConstraints(3, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    // Zeile
    iZeile++;

    jpaWorkingOnScroll.add(wlaFremdsystemnr,
                           new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    jpaWorkingOnScroll.add(wtfZielKundeFremdsystemnr,
                           new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    jpaWorkingOnScroll.add(wbuFremdsystemnrDatenrichtung,
                           new GridBagConstraints(2, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    jpaWorkingOnScroll.add(wtfQuellKundeFremdsystemnr,
                           new GridBagConstraints(3, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    // Zeile
    iZeile++;

    jpaWorkingOnScroll.add(wlaErloeskonto,
                           new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    jpaWorkingOnScroll.add(wtfZielKundeErloeskonto,
                           new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    jpaWorkingOnScroll.add(wbuErloeskontoDatenrichtung,
                           new GridBagConstraints(2, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    jpaWorkingOnScroll.add(wtfQuellKundeErloeskonto,
                           new GridBagConstraints(3, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    // Zeile
    iZeile++;

    jpaWorkingOnScroll.add(wlaDebitorenkonto,
                           new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    jpaWorkingOnScroll.add(wtfZielKundeDebitorenkonto,
                           new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

  /*  jpaWorkingOnScroll.add(wbuDebitorenkontoDatenrichtung,
                           new GridBagConstraints(2, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));*/

    jpaWorkingOnScroll.add(wtfQuellKundeDebitorenkonto,
                           new GridBagConstraints(3, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    // Zeile
    iZeile++;

    jpaWorkingOnScroll.add(wlaRechnungsadresspartner,
                           new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    jpaWorkingOnScroll.add(wtfZielKundeRechnungsadresspartner,
                           new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    jpaWorkingOnScroll.add(wbuRechnungsadresspartnerDatenrichtung,
                           new GridBagConstraints(2, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    jpaWorkingOnScroll.add(wtfQuellKundeRechnungsadresspartner,
                           new GridBagConstraints(3, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    // Zeile
    iZeile++;

    jpaWorkingOnScroll.add(wlaHinweisInt,
                           new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    jpaWorkingOnScroll.add(wtfZielKundeHinweisInt,
                           new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    jpaWorkingOnScroll.add(wbuHinweisIntDatenrichtung,
                           new GridBagConstraints(2, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    jpaWorkingOnScroll.add(wtfQuellKundeHinweisInt,
                           new GridBagConstraints(3, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    // Zeile
    iZeile++;

    jpaWorkingOnScroll.add(wlaHinweisExt,
                           new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    jpaWorkingOnScroll.add(wtfZielKundeHinweisExt,
                           new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    jpaWorkingOnScroll.add(wbuHinweisExtDatenrichtung,
                           new GridBagConstraints(2, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    jpaWorkingOnScroll.add(wtfQuellKundeHinweisExt,
                           new GridBagConstraints(3, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    // Zeile
    iZeile++;

    jpaWorkingOnScroll.add(wlaZessionsfaktor,
                           new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    jpaWorkingOnScroll.add(wtfZielKundeZessionsfaktor,
                           new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    jpaWorkingOnScroll.add(wbuZessionsfaktorDatenrichtung,
                           new GridBagConstraints(2, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    jpaWorkingOnScroll.add(wtfQuellKundeZessionsfaktor,
                           new GridBagConstraints(3, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    // Zeile
    iZeile++;

    jpaWorkingOnScroll.add(wlaPreisliste,
                           new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    jpaWorkingOnScroll.add(wtfZielKundePreisliste,
                           new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    jpaWorkingOnScroll.add(wbuPreislisteDatenrichtung,
                           new GridBagConstraints(2, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    jpaWorkingOnScroll.add(wtfQuellKundePreisliste,
                           new GridBagConstraints(3, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    // Zeile
    iZeile++;

    jpaWorkingOnScroll.add(wlaKundennr, new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0
        , GridBagConstraints.CENTER,
        GridBagConstraints.HORIZONTAL,
        new Insets(0, 0, 0, 0), 0, 0));

    jpaWorkingOnScroll.add(wtfZielKundeKundennr,
                           new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    jpaWorkingOnScroll.add(wbuKundennrDatenrichtung,
                           new GridBagConstraints(2, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    jpaWorkingOnScroll.add(wtfQuellKundeKundennr,
                           new GridBagConstraints(3, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    // Zeile
    iZeile++;

    jpaWorkingOnScroll.add(wlaKundensoko, new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0
        , GridBagConstraints.CENTER,
        GridBagConstraints.HORIZONTAL,
        new Insets(0, 0, 0, 0), 0, 0));

    jpaWorkingOnScroll.add(wtfZielKundeKundensoko,
                           new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    jpaWorkingOnScroll.add(wbuKundensokoDatenrichtung,
                           new GridBagConstraints(2, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    jpaWorkingOnScroll.add(wtfQuellKundeKundensoko,
                           new GridBagConstraints(3, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    // Zeile
    iZeile++;

    jpaWorkingOnScroll.add(wlaMindermengenzuschlag,
                           new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    jpaWorkingOnScroll.add(wcbZielKundeMindermengenzuschlag,
                           new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.NONE,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    jpaWorkingOnScroll.add(wbuMindermengenzuschlagDatenrichtung,
                           new GridBagConstraints(2, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    jpaWorkingOnScroll.add(wcbQuellKundeMindermengenzuschlag,
                           new GridBagConstraints(3, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.NONE,
                                                  new Insets(0, 0, 0, 0), 0, 0));


    // Zeile
    iZeile++;

    jpaWorkingOnScroll.add(wlaMonatsrechnung,
                           new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    jpaWorkingOnScroll.add(wcbZielKundeMonatsrechnung,
                           new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.NONE,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    jpaWorkingOnScroll.add(wbuMonatsrechnungDatenrichtung,
                           new GridBagConstraints(2, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    jpaWorkingOnScroll.add(wcbQuellKundeMonatsrechnung,
                           new GridBagConstraints(3, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.NONE,
                                                  new Insets(0, 0, 0, 0), 0, 0));



    // Zeile
    iZeile++;

    jpaWorkingOnScroll.add(wlaSammelrechnung,
                           new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    jpaWorkingOnScroll.add(wcbZielKundeSammelrechnung,
                           new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.NONE,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    jpaWorkingOnScroll.add(wbuSammelrechnungDatenrichtung,
                           new GridBagConstraints(2, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    jpaWorkingOnScroll.add(wcbQuellKundeSammelrechnung,
                           new GridBagConstraints(3, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.NONE,
                                                  new Insets(0, 0, 0, 0), 0, 0));



    // Zeile
    iZeile++;

    jpaWorkingOnScroll.add(wlaRechnungsempfaenger,
                           new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    jpaWorkingOnScroll.add(wcbZielKundeRechnungsempfaenger,
                           new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.NONE,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    jpaWorkingOnScroll.add(wbuRechnungsempfaengerDatenrichtung,
                           new GridBagConstraints(2, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    jpaWorkingOnScroll.add(wcbQuellKundeRechnungsempfaenger,
                           new GridBagConstraints(3, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.NONE,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    // Zeile
    iZeile++;

    jpaWorkingOnScroll.add(wlaPreiseAmLs,
                           new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    jpaWorkingOnScroll.add(wcbZielKundePreiseAmLs,
                           new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.NONE,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    jpaWorkingOnScroll.add(wbuPreiseAmLsDatenrichtung,
                           new GridBagConstraints(2, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    jpaWorkingOnScroll.add(wcbQuellKundePreiseAmLs,
                           new GridBagConstraints(3, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.NONE,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    // Zeile
    iZeile++;

    jpaWorkingOnScroll.add(wlaBelegdruckMitRabatt,
                           new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    jpaWorkingOnScroll.add(wcbZielKundeBelegdruckMitRabatt,
                           new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.NONE,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    jpaWorkingOnScroll.add(wbuBelegdruckMitRabattDatenrichtung,
                           new GridBagConstraints(2, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    jpaWorkingOnScroll.add(wcbQuellKundeBelegdruckMitRabatt,
                           new GridBagConstraints(3, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.NONE,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    // Zeile
    iZeile++;

    jpaWorkingOnScroll.add(wlaDistributor,
                           new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    jpaWorkingOnScroll.add(wcbZielKundeDistributor,
                           new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.NONE,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    jpaWorkingOnScroll.add(wbuDistributorDatenrichtung,
                           new GridBagConstraints(2, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    jpaWorkingOnScroll.add(wcbQuellKundeDistributor,
                           new GridBagConstraints(3, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.NONE,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    // Zeile
    iZeile++;

    jpaWorkingOnScroll.add(wlaTeillieferungen,
                           new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    jpaWorkingOnScroll.add(wcbZielKundeTeillieferungen,
                           new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.NONE,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    jpaWorkingOnScroll.add(wbuTeillieferungenDatenrichtung,
                           new GridBagConstraints(2, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    jpaWorkingOnScroll.add(wcbQuellKundeTeillieferungen,
                           new GridBagConstraints(3, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.NONE,
                                                  new Insets(0, 0, 0, 0), 0, 0));


    // Zeile
    iZeile++;

    jpaWorkingOnScroll.add(wlaLsgewicht,
                           new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    jpaWorkingOnScroll.add(wcbZielKundeLsgewicht,
                           new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.NONE,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    jpaWorkingOnScroll.add(wbuLsgewichtDatenrichtung,
                           new GridBagConstraints(2, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    jpaWorkingOnScroll.add(wcbQuellKundeLsgewicht,
                           new GridBagConstraints(3, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.NONE,
                                                  new Insets(0, 0, 0, 0), 0, 0));



    // Zeile
    iZeile++;

    jpaWorkingOnScroll.add(wlaInteressent,
                           new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    jpaWorkingOnScroll.add(wcbZielKundeInteressent,
                           new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.NONE,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    jpaWorkingOnScroll.add(wbuInteressentDatenrichtung,
                           new GridBagConstraints(2, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    jpaWorkingOnScroll.add(wcbQuellKundeInteressent,
                           new GridBagConstraints(3, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.NONE,
                                                  new Insets(0, 0, 0, 0), 0, 0));
    // Zeile
    iZeile++;

    jpaWorkingOnScroll.add(wlaVersteckt,
                           new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    jpaWorkingOnScroll.add(wcbZielKundeVersteckt,
                           new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.NONE,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    jpaWorkingOnScroll.add(wbuVerstecktDatenrichtung,
                           new GridBagConstraints(2, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    jpaWorkingOnScroll.add(wcbQuellKundeVersteckt,
                           new GridBagConstraints(3, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.NONE,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    // Zeile
    iZeile++;

    jpaWorkingOnScroll.add(wlaReversecharge,
                           new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    jpaWorkingOnScroll.add(wcbZielKundeReversecharge,
                           new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.NONE,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    jpaWorkingOnScroll.add(wbuReversechargeDatenrichtung,
                           new GridBagConstraints(2, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    jpaWorkingOnScroll.add(wcbQuellKundeReversecharge,
                           new GridBagConstraints(3, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.NONE,
                                                  new Insets(0, 0, 0, 0), 0, 0));


    jScrollPaneKunde.setViewportView(jpaWorkingOnScroll);
    jScrollPaneKunde.setAutoscrolls(true);

    jpaWorkingOn.add(jScrollPaneKunde, new GridBagConstraints(0, iZeile, 1, 1, 1.0, 1.0
        , GridBagConstraints.CENTER,
        GridBagConstraints.BOTH,
        new Insets(0, 0, 0, 0), 0, 0));

    saveButton = createAndSaveButton("/com/lp/client/res/check2.png",
                                     LPMain.getTextRespectUISPr(
        "part.kundenzusammenfuehren.ausfuehren"),
                                     ACTION_SPECIAL_SAVE,
                                     KeyStroke.getKeyStroke('S',
        java.awt.event.InputEvent.CTRL_MASK),
                                     null);

    String[] aWhichButtonIUse = {
        ACTION_SPECIAL_SAVE};

    enableButtonAction(aWhichButtonIUse);

    /*ToDo: weiss nicht, was da hin muss*/
    LockStateValue lockstateValue = new LockStateValue(
        null,
        null,
        PanelBasis.LOCK_NO_LOCKING);
    updateButtons(lockstateValue);

    befuelleFelderMitKundeZielDto(this.kundeZielDto);
    setzeFarbenVonTextfeldern(this.vWrapperButtonsZusammenfuehren, this.vZielfelder, this.vQuellfelder, true);
  }

  /**
   * wird ein Zielkunde (=Kunde auf der linken Seite) ausgewaehlt, so werden die Felder mit dessen Daten gefuellt
   * @param kndZDto KundeDto
   * @throws Throwable
   */
  private void befuelleFelderMitKundeZielDto(KundeDto kndZDto)
      throws Throwable {

    if (kndZDto.getPartnerDto().formatName() != null &&
        kndZDto.getPartnerDto().formatName() != "") {
      wtfZielKundePartnername.setText(kndZDto.getPartnerDto().formatName());
      wtfZielKundePartnername.setCaretPosition(0);
      wtfZielKundePartnername.setToolTipText(kndZDto.getPartnerDto().formatName());
    }
    else {
      wtfZielKundePartnername.setText("");
      wtfZielKundePartnername.setToolTipText("");
    }

    if (kndZDto.getMwstsatzbezIId() != null) {
      wtfZielKundeMwst.setText(DelegateFactory.getInstance().getMandantDelegate().
                               mwstsatzbezFindByPrimaryKey(kndZDto.getMwstsatzbezIId()).
                               getCBezeichnung());
      wtfZielKundeMwst.setCaretPosition(0);
      wtfZielKundeMwst.setToolTipText(DelegateFactory.getInstance().getMandantDelegate().
                                      mwstsatzbezFindByPrimaryKey(kndZDto.
          getMwstsatzbezIId()).getCBezeichnung());
    }
    else {
      wtfZielKundeMwst.setText("");
      wtfZielKundeMwst.setToolTipText("");
    }

    if (kndZDto.getCWaehrung() != null) {
      wtfZielKundeWaehrung.setText(kndZDto.getCWaehrung());
      wtfZielKundeWaehrung.setCaretPosition(0);
      wtfZielKundeWaehrung.setToolTipText(kndZDto.getCWaehrung());
    }
    else {
      wtfZielKundeWaehrung.setText("");
      wtfZielKundeWaehrung.setToolTipText("");
    }

    if (kndZDto.getLieferartIId() != null) {
      wtfZielKundeLieferart.setText(DelegateFactory.getInstance().getLocaleDelegate().
                                    lieferartFindByPrimaryKey(kndZDto.getLieferartIId()).
                                    formatBez());
      wtfZielKundeLieferart.setCaretPosition(0);
      wtfZielKundeLieferart.setToolTipText(DelegateFactory.getInstance().
                                           getLocaleDelegate().lieferartFindByPrimaryKey(
          kndZDto.getLieferartIId()).formatBez());
    }
    else {
      wtfZielKundeLieferart.setText("");
      wtfZielKundeLieferart.setToolTipText("");
    }

    if (kndZDto.getSpediteurIId() != null) {
      SpediteurDto spediteurDto = DelegateFactory.getInstance().getMandantDelegate().
          spediteurFindByPrimaryKey(kndZDto.getSpediteurIId());
      wtfZielKundeSpediteur.setText(spediteurDto.getCNamedesspediteurs());
      wtfZielKundeSpediteur.setCaretPosition(0);
      wtfZielKundeSpediteur.setToolTipText(spediteurDto.getCNamedesspediteurs());
    }
    else {
      wtfZielKundeSpediteur.setText("");
      wtfZielKundeSpediteur.setToolTipText("");
    }

    if (kndZDto.getZahlungszielIId() != null) {
      ZahlungszielDto zahlungszielDto = DelegateFactory.getInstance().getMandantDelegate().
          zahlungszielFindByPrimaryKey(kndZDto.getZahlungszielIId());
      wtfZielKundeZahlungsziel.setText(zahlungszielDto.getCBez());
      wtfZielKundeZahlungsziel.setCaretPosition(0);
      wtfZielKundeZahlungsziel.setToolTipText(zahlungszielDto.getCBez());
    }
    else {
      wtfZielKundeZahlungsziel.setText("");
      wtfZielKundeZahlungsziel.setToolTipText("");
    }

    if (kndZDto.getKostenstelleIId() != null) {
      KostenstelleDto kostenstelleDto = DelegateFactory.getInstance().getSystemDelegate().
          kostenstelleFindByPrimaryKey(kndZDto.getKostenstelleIId());
      wtfZielKundeKostenstelle.setText(kostenstelleDto.formatKostenstellenbezeichnung());
      wtfZielKundeKostenstelle.setCaretPosition(0);
      wtfZielKundeKostenstelle.setToolTipText(kostenstelleDto.
                                              formatKostenstellenbezeichnung());
    }
    else {
      wtfZielKundeKostenstelle.setText("");
      wtfZielKundeKostenstelle.setToolTipText("");
    }

    if (kndZDto.getPersonaliIdProvisionsempfaenger() != null) {
      PersonalDto personalDto = DelegateFactory.getInstance().getPersonalDelegate().
          personalFindByPrimaryKey(kndZDto.getPersonaliIdProvisionsempfaenger());
      wtfZielKundeProvisionsempfaenger.setText(personalDto.formatFixUFTitelName2Name1());
      wtfZielKundeProvisionsempfaenger.setCaretPosition(0);
      wtfZielKundeProvisionsempfaenger.setToolTipText(personalDto.
          formatFixUFTitelName2Name1());
    }
    else {
      wtfZielKundeProvisionsempfaenger.setText("");
      wtfZielKundeProvisionsempfaenger.setToolTipText("");
    }

    if (kndZDto.getFRabattsatz() != null) {
      BigDecimal bdRabatt = new BigDecimal(kndZDto.getFRabattsatz());
      wtfZielKundeRabattsatz.setText(Helper.formatZahl(bdRabatt, Locale.getDefault()) +
                                     " %");
      wtfZielKundeRabattsatz.setCaretPosition(0);
      wtfZielKundeRabattsatz.setToolTipText(Helper.formatZahl(bdRabatt, Locale.getDefault()) +
                                            " %");
    }
    else {
      wtfZielKundeRabattsatz.setText("");
      wtfZielKundeRabattsatz.setToolTipText("");
    }

    if (kndZDto.getIGarantieinmonaten() != null) {
      wtfZielKundeGarantie.setText(Integer.toString(kndZDto.getIGarantieinmonaten()) +
                                   " " +
                                   LPMain.getInstance().getTextRespectUISPr("lp.monat"));
      wtfZielKundeGarantie.setCaretPosition(0);
      wtfZielKundeGarantie.setToolTipText(Integer.toString(kndZDto.getIGarantieinmonaten()) +
                                          " " +
                                          LPMain.getInstance().getTextRespectUISPr("lp.monat"));
    }
    else {
      wtfZielKundeGarantie.setText("");
      wtfZielKundeGarantie.setToolTipText("");
    }

    if (kndZDto.getXKommentar() != null) {
      wtaZielKundeKommentar.setText(kndZDto.getXKommentar());
      wtaZielKundeKommentar.setCaretPosition(0);
      wtaZielKundeKommentar.setToolTipText(kndZDto.getXKommentar());
    }
    else {
      wtaZielKundeKommentar.setText("");
      wtaZielKundeKommentar.setToolTipText("");
    }

    if (kndZDto.getCKurznr() != null) {
      wtfZielKundeKurznr.setText(kndZDto.getCKurznr());
      wtfZielKundeKurznr.setCaretPosition(0);
      wtfZielKundeKurznr.setToolTipText(kndZDto.getCKurznr());
    }
    else {
      wtfZielKundeKurznr.setText("");
      wtfZielKundeKurznr.setToolTipText("");
    }

    if (kndZDto.getNKreditlimit() != null) {
      BigDecimal bdLimit = kndZDto.getNKreditlimit();
      wtfZielKundeKreditlimit.setText(kndZDto.getCWaehrung() + " " +
                                      Helper.formatZahl(bdLimit, Locale.getDefault()));
      wtfZielKundeKreditlimit.setCaretPosition(0);
      wtfZielKundeKreditlimit.setToolTipText(kndZDto.getCWaehrung() + " " +
                                             Helper.formatZahl(bdLimit, Locale.getDefault()));
    }
    else {
      wtfZielKundeKreditlimit.setText("");
      wtfZielKundeKreditlimit.setToolTipText("");
    }

    if (kndZDto.getTBonitaet() != null) {
      Date dBonitaet = kndZDto.getTBonitaet();
      wtfZielKundeBonitaetspruefung.setText(Helper.formatDatum(dBonitaet,
          Locale.getDefault()));
      wtfZielKundeBonitaetspruefung.setCaretPosition(0);
      wtfZielKundeBonitaetspruefung.setToolTipText(Helper.formatDatum(dBonitaet,
          Locale.getDefault()));
    }
    else {
      wtfZielKundeBonitaetspruefung.setText("");
      wtfZielKundeBonitaetspruefung.setToolTipText("");
    }

    if (kndZDto.getTLiefersperream() != null) {
      Date dLiefersperre = kndZDto.getTLiefersperream();
      wtfZielKundeLiefersperre.setText(Helper.formatDatum(dLiefersperre,
          Locale.getDefault()));
      wtfZielKundeLiefersperre.setCaretPosition(0);
      wtfZielKundeLiefersperre.setToolTipText(Helper.formatDatum(dLiefersperre,
          Locale.getDefault()));
    }
    else {
      wtfZielKundeLiefersperre.setText("");
      wtfZielKundeLiefersperre.setToolTipText("");
    }

    if (kndZDto.getIDefaultrekopiendrucken() != null) {
      wtfZielKundeRechnungskopien.setText(kndZDto.getIDefaultrekopiendrucken().toString());
      wtfZielKundeRechnungskopien.setCaretPosition(0);
      wtfZielKundeRechnungskopien.setToolTipText(kndZDto.getIDefaultrekopiendrucken().
                                                 toString());
    }
    else {
      wtfZielKundeRechnungskopien.setText("");
      wtfZielKundeRechnungskopien.setToolTipText("");
    }

    if (kndZDto.getIDefaultlskopiendrucken() != null) {
      wtfZielKundeLieferscheinkopien.setText(kndZDto.getIDefaultlskopiendrucken().
                                             toString());
      wtfZielKundeLieferscheinkopien.setCaretPosition(0);
      wtfZielKundeLieferscheinkopien.setToolTipText(kndZDto.getIDefaultlskopiendrucken().
          toString());
    }
    else {
      wtfZielKundeLieferscheinkopien.setText("");
      wtfZielKundeLieferscheinkopien.setToolTipText("");
    }

    if (kndZDto.getIMitarbeiteranzahl() != null) {
      wtfZielKundeMitarbeiter.setText(kndZDto.getIMitarbeiteranzahl().toString());
      wtfZielKundeMitarbeiter.setCaretPosition(0);
      wtfZielKundeMitarbeiter.setToolTipText(kndZDto.getIMitarbeiteranzahl().toString());
    }
    else {
      wtfZielKundeMitarbeiter.setText("");
      wtfZielKundeMitarbeiter.setToolTipText("");
    }

    if (kndZDto.getCTour() != null) {
      wtfZielKundeTour.setText(kndZDto.getCTour());
      wtfZielKundeTour.setCaretPosition(0);
      wtfZielKundeTour.setToolTipText(kndZDto.getCTour());
    }
    else {
      wtfZielKundeTour.setText("");
      wtfZielKundeTour.setToolTipText("");
    }

    if (kndZDto.getCLieferantennr() != null) {
      wtfZielKundeLieferantennr.setText(kndZDto.getCLieferantennr());
      wtfZielKundeLieferantennr.setCaretPosition(0);
      wtfZielKundeLieferantennr.setToolTipText(kndZDto.getCLieferantennr());
    }
    else {
      wtfZielKundeLieferantennr.setText("");
      wtfZielKundeLieferantennr.setToolTipText("");
    }

    if (kndZDto.getCAbc() != null) {
      wtfZielKundeAbc.setText(kndZDto.getCAbc());
      wtfZielKundeAbc.setCaretPosition(0);
      wtfZielKundeAbc.setToolTipText(kndZDto.getCAbc());
    }
    else {
      wtfZielKundeAbc.setText("");
      wtfZielKundeAbc.setToolTipText("");
    }

    if (kndZDto.getTAgbuebermittelung() != null) {
      Date dAgb = kndZDto.getTAgbuebermittelung();
      wtfZielKundeAgb.setText(Helper.formatDatum(dAgb, Locale.getDefault()));
      wtfZielKundeAgb.setCaretPosition(0);
      wtfZielKundeAgb.setToolTipText(Helper.formatDatum(dAgb, Locale.getDefault()));
    }
    else {
      wtfZielKundeAgb.setText("");
      wtfZielKundeAgb.setToolTipText("");
    }

    if (kndZDto.getCFremdsystemnr() != null) {
      wtfZielKundeFremdsystemnr.setText(kndZDto.getCFremdsystemnr());
      wtfZielKundeFremdsystemnr.setCaretPosition(0);
      wtfZielKundeFremdsystemnr.setToolTipText(kndZDto.getCFremdsystemnr());
    }
    else {
      wtfZielKundeFremdsystemnr.setText("");
      wtfZielKundeFremdsystemnr.setToolTipText("");
    }

    if (kndZDto.getIidErloeseKonto() != null) {
      KontoDtoSmall kontoDtoSmall = DelegateFactory.getInstance().getFinanzDelegate().
          kontoFindByPrimaryKeySmall(kndZDto.getIidErloeseKonto());
      wtfZielKundeErloeskonto.setText(kontoDtoSmall.getCNr());
      wtfZielKundeErloeskonto.setCaretPosition(0);
      wtfZielKundeErloeskonto.setToolTipText(kontoDtoSmall.getCNr() + " " +
                                             kontoDtoSmall.getCBez());
    }
    else {
      wtfZielKundeErloeskonto.setText("");
      wtfZielKundeErloeskonto.setToolTipText("");
    }

    if (kndZDto.getIidDebitorenkonto() != null) {
      KontoDto kontoDto = DelegateFactory.getInstance().getFinanzDelegate().
          kontoFindByPrimaryKey(kndZDto.getIidDebitorenkonto());
      wtfZielKundeDebitorenkonto.setText(kontoDto.getCNr());
      wtfZielKundeDebitorenkonto.setCaretPosition(0);
      wtfZielKundeDebitorenkonto.setToolTipText(kontoDto.getCNr() + " " +
                                                kontoDto.getCBez());
    }
    else {
      wtfZielKundeDebitorenkonto.setText("");
      wtfZielKundeDebitorenkonto.setToolTipText("");
    }

    if (kndZDto.getPartnerIIdRechnungsadresse() != null) {
      PartnerDto partnerDto = null;
      if (kndZDto.getPartnerRechnungsadresseDto() != null) {
        partnerDto = kndZDto.getPartnerRechnungsadresseDto();
      }
      else {
        partnerDto = DelegateFactory.getInstance().getPartnerDelegate().
            partnerFindByPrimaryKey(kndZDto.getPartnerIIdRechnungsadresse());
      }
      wtfZielKundeRechnungsadresspartner.setText(partnerDto.formatAdresse());
      wtfZielKundeRechnungsadresspartner.setCaretPosition(0);
      wtfZielKundeRechnungsadresspartner.setToolTipText(partnerDto.formatAdresse());
    }
    else {
      wtfZielKundeRechnungsadresspartner.setText("");
      wtfZielKundeRechnungsadresspartner.setToolTipText("");
    }

    if (kndZDto.getSHinweisintern() != null) {
      wtfZielKundeHinweisInt.setText(kndZDto.getSHinweisintern());
      wtfZielKundeHinweisInt.setCaretPosition(0);
      wtfZielKundeHinweisInt.setToolTipText(kndZDto.getSHinweisintern());
    }
    else {
      wtfZielKundeHinweisInt.setText("");
      wtfZielKundeHinweisInt.setToolTipText("");
    }

    if (kndZDto.getSHinweisextern() != null) {
      wtfZielKundeHinweisExt.setText(kndZDto.getSHinweisextern());
      wtfZielKundeHinweisExt.setCaretPosition(0);
      wtfZielKundeHinweisExt.setToolTipText(kndZDto.getSHinweisextern());
    }
    else {
      wtfZielKundeHinweisExt.setText("");
      wtfZielKundeHinweisExt.setToolTipText("");
    }

    if (kndZDto.getFZessionsfaktor() != null) {
      BigDecimal bdZession = new BigDecimal(kndZDto.getFZessionsfaktor());
      wtfZielKundeZessionsfaktor.setText(Helper.formatZahl(bdZession, Locale.getDefault()) +
                                         " %");
      wtfZielKundeZessionsfaktor.setCaretPosition(0);
      wtfZielKundeZessionsfaktor.setToolTipText(Helper.formatZahl(bdZession,
          Locale.getDefault()) + " %");
    }
    else {
      wtfZielKundeZessionsfaktor.setText("");
      wtfZielKundeZessionsfaktor.setToolTipText("");
    }

    if (kndZDto.getVkpfArtikelpreislisteIIdStdpreisliste() != null) {
      VkpfartikelpreislisteDto oVkpfartikelpreislisteDto = DelegateFactory.getInstance().
          getVkPreisfindungDelegate().vkpfartikelpreislisteFindByPrimaryKey(kndZDto.
          getVkpfArtikelpreislisteIIdStdpreisliste());
      wtfZielKundePreisliste.setText(oVkpfartikelpreislisteDto.getCNr());
      wtfZielKundePreisliste.setCaretPosition(0);
      wtfZielKundePreisliste.setToolTipText(oVkpfartikelpreislisteDto.getCNr());
    }
    else {
      wtfZielKundePreisliste.setText("");
      wtfZielKundePreisliste.setToolTipText("");
    }

    if (kndZDto.getIKundennummer() != null) {
      wtfZielKundeKundennr.setText(Helper.formatZahl(kndZDto.getIKundennummer(),
          Locale.getDefault()));
      wtfZielKundeKundennr.setCaretPosition(0);
      wtfZielKundeKundennr.setToolTipText(Helper.formatZahl(kndZDto.getIKundennummer(),
          Locale.getDefault()));
    }
    else {
      wtfZielKundeKundennr.setText("");
      wtfZielKundeKundennr.setToolTipText("");
    }

    // Kundensoko Anzahl der Eintraege zaehlen
    KundesokoDto[] aKundesokoDtos = DelegateFactory.getInstance().getKundesokoDelegate().kundesokoFindByKundeIIdOhneExc(kundeZielDto.getIId());
    Integer iAnzahlKundensokoeintraegeZiel = aKundesokoDtos.length;
    if (iAnzahlKundensokoeintraegeZiel != null) {
      wtfZielKundeKundensoko.setText(iAnzahlKundensokoeintraegeZiel.toString() + " " + LPMain.getInstance().getTextRespectUISPr("part.kunde.zusammenfuehre.eintraege"));
      wtfZielKundeKundensoko.setCaretPosition(0);
      wtfZielKundeKundensoko.setToolTipText(iAnzahlKundensokoeintraegeZiel.toString() + " " + LPMain.getInstance().getTextRespectUISPr("part.kunde.zusammenfuehre.eintraege"));
    }
    else {
      wtfZielKundeKundensoko.setText("");
      wtfZielKundeKundensoko.setToolTipText("");
    }

    if (kndZDto.getBMindermengenzuschlag() != null) {
      wcbZielKundeMindermengenzuschlag.setShort(kndZDto.getBMindermengenzuschlag());
    }
    else {
      wcbZielKundeMindermengenzuschlag.setText("");
    }

    if (kndZDto.getBMonatsrechnung() != null) {
      wcbZielKundeMonatsrechnung.setShort(kndZDto.getBMonatsrechnung());
    }
    else {
      wcbZielKundeMonatsrechnung.setText("");
    }

    if (kndZDto.getBSammelrechnung() != null) {
      wcbZielKundeSammelrechnung.setShort(kndZDto.getBSammelrechnung());
    }
    else {
      wcbZielKundeSammelrechnung.setText("");
    }

    if (kndZDto.getBIstreempfaenger() != null) {
      wcbZielKundeRechnungsempfaenger.setShort(kndZDto.getBIstreempfaenger());
    }
    else {
      wcbZielKundeRechnungsempfaenger.setText("");
    }

    if (kndZDto.getBPreiseanlsandrucken() != null) {
      wcbZielKundePreiseAmLs.setShort(kndZDto.getBPreiseanlsandrucken());
    }
    else {
      wcbZielKundePreiseAmLs.setText("");
    }

    if (kndZDto.getBRechnungsdruckmitrabatt() != null) {
      wcbZielKundeBelegdruckMitRabatt.setShort(kndZDto.getBRechnungsdruckmitrabatt());
    }
    else {
      wcbZielKundeBelegdruckMitRabatt.setText("");
    }

    if (kndZDto.getBDistributor() != null) {
      wcbZielKundeDistributor.setShort(kndZDto.getBDistributor());
    }
    else {
      wcbZielKundeDistributor.setText("");
    }

    if (kndZDto.getBAkzeptiertteillieferung() != null) {
      wcbZielKundeTeillieferungen.setShort(kndZDto.getBAkzeptiertteillieferung());
    }
    else {
      wcbZielKundeTeillieferungen.setText("");
    }

    if (kndZDto.getBLsgewichtangeben() != null) {
      wcbZielKundeLsgewicht.setShort(kndZDto.getBLsgewichtangeben());
    }
    else {
      wcbZielKundeLsgewicht.setText("");
    }

    if (kndZDto.getbIstinteressent() != null) {
      wcbZielKundeInteressent.setShort(kndZDto.getbIstinteressent());
    }
    else {
      wcbZielKundeInteressent.setText("");
    }

    if (kndZDto.getBVersteckterlieferant() != null) {
      wcbZielKundeVersteckt.setShort(kndZDto.getBVersteckterlieferant());
    }
    else {
      wcbZielKundeVersteckt.setText("");
    }

    if (kndZDto.getBReversecharge() != null) {
      wcbZielKundeReversecharge.setShort(kndZDto.getBReversecharge());
    }
    else {
      wcbZielKundeReversecharge.setText("");
    }

    setzeStatusVonButtonDatenrichtung();
    //setzeStatusVonTextfeldernMehrfach(this.vZielfelder, this.vQuellfelder);
    setzeStatusButtonResetDatenrichtung();
  }


  /**
   * wenn die daten einer seite ausgewaehlt werden, so werden sie farblich hinterlegt angezeigt, wenn nicht ausgewaehlt,
   * dann werden sie grau hinterlegt angezeigt
   * @param vButtons Vector etnhaelt alle Buttons, au&szlig;er dem zentralen, welche die Datenrichtung bestimmen
   * @param vZielfelder Vector enthaelt alle Felder und Checkboxen der linken Seite
   * @param vQuellfelder Vector enthaelt alle Felder und Checkboxen der rechten Seite
   * @param initial boolean beim initialen starten wird die linke seite ausgewaehlt
   */
  private void setzeFarbenVonTextfeldern(Vector<WrapperButtonZusammenfuehren> vButtons, Vector<JComponent> vZielfelder, Vector<JComponent> vQuellfelder, boolean initial) {
    int i = 0;
    for (Enumeration<JComponent> e2 = vQuellfelder.elements(); e2.hasMoreElements(); e2.nextElement()) {
      if (initial) {
        // nur die linken selektieren
        if (vZielfelder.get(i) instanceof WrapperTextFieldZusammenfuehren) {
          ( (WrapperTextFieldZusammenfuehren) vZielfelder.get(i)).setSelectedData(true);
          ( (WrapperTextFieldZusammenfuehren) vQuellfelder.get(i)).setSelectedData(false);
        }
        else if (vZielfelder.get(i) instanceof WrapperTextAreaZusammenfuehren) {
          ( (WrapperTextAreaZusammenfuehren) vZielfelder.get(i)).setSelectedData(true);
          ( (WrapperTextAreaZusammenfuehren) vQuellfelder.get(i)).setSelectedData(false);
        }
        else if (vZielfelder.get(i) instanceof WrapperCheckBoxZusammenfuehren) {
          ( (WrapperCheckBoxZusammenfuehren) vZielfelder.get(i)).setSelectedData(true);
          ( (WrapperCheckBoxZusammenfuehren) vQuellfelder.get(i)).setSelectedData(false);
        }
        else {
          // fehlt
        }
      }
      else {
        if (vButtons.get(i).getISelection() == WrapperButtonZusammenfuehren.SELECT_LINKEN_DATENINHALT) {
          if (vZielfelder.get(i) instanceof WrapperTextFieldZusammenfuehren) {
            ( (WrapperTextFieldZusammenfuehren) vZielfelder.get(i)).setSelectedData(true);
            ( (WrapperTextFieldZusammenfuehren) vQuellfelder.get(i)).setSelectedData(false);
          }
          else if (vZielfelder.get(i) instanceof WrapperTextAreaZusammenfuehren) {
            ( (WrapperTextAreaZusammenfuehren) vZielfelder.get(i)).setSelectedData(true);
            ( (WrapperTextAreaZusammenfuehren) vQuellfelder.get(i)).setSelectedData(false);
          }
          else if (vZielfelder.get(i) instanceof WrapperCheckBoxZusammenfuehren) {
            ( (WrapperCheckBoxZusammenfuehren) vZielfelder.get(i)).setSelectedData(true);
            ( (WrapperCheckBoxZusammenfuehren) vQuellfelder.get(i)).setSelectedData(false);
          }
          else {
            // fehlt
          }
        }
        else if (vButtons.get(i).getISelection() == WrapperButtonZusammenfuehren.SELECT_RECHTEN_DATENINHALT) {
          if (vZielfelder.get(i) instanceof WrapperTextFieldZusammenfuehren) {
            ( (WrapperTextFieldZusammenfuehren) vZielfelder.get(i)).setSelectedData(false);
            ( (WrapperTextFieldZusammenfuehren) vQuellfelder.get(i)).setSelectedData(true);
          }
          else if (vZielfelder.get(i) instanceof WrapperTextAreaZusammenfuehren) {
            ( (WrapperTextAreaZusammenfuehren) vZielfelder.get(i)).setSelectedData(false);
            ( (WrapperTextAreaZusammenfuehren) vQuellfelder.get(i)).setSelectedData(true);
          }
          else if (vZielfelder.get(i) instanceof WrapperCheckBoxZusammenfuehren) {
            ( (WrapperCheckBoxZusammenfuehren) vZielfelder.get(i)).setSelectedData(false);
            ( (WrapperCheckBoxZusammenfuehren) vQuellfelder.get(i)).setSelectedData(true);
          }
          else {
            // fehlt
          }
        }
        else if (vButtons.get(i).getISelection() ==  WrapperButtonZusammenfuehren.SELECT_KOMBINIERTE_DATENINHALTE) {
          if (vZielfelder.get(i) instanceof WrapperTextFieldZusammenfuehren) {
            ( (WrapperTextFieldZusammenfuehren) vZielfelder.get(i)).setSelectedData(true);
            ( (WrapperTextFieldZusammenfuehren) vQuellfelder.get(i)).setSelectedData(true);
          }
          else if (vZielfelder.get(i) instanceof WrapperTextAreaZusammenfuehren) {
            ( (WrapperTextAreaZusammenfuehren) vZielfelder.get(i)).setSelectedData(true);
            ( (WrapperTextAreaZusammenfuehren) vQuellfelder.get(i)).setSelectedData(true);
          }
          else if (vZielfelder.get(i) instanceof WrapperCheckBoxZusammenfuehren) {
            ( (WrapperCheckBoxZusammenfuehren) vZielfelder.get(i)).setSelectedData(true);
            ( (WrapperCheckBoxZusammenfuehren) vQuellfelder.get(i)).setSelectedData(true);
          }
          else {
            // fehlt
          }
        }
      }
      i++;
    }


  }

  /**
   * enabled/disabled abhaengig vom vorhandensein der quell/zieldtos alle datenrichtung-buttons
   * @param vButtons Vector
   */
  private void setzeStatusAllerButtonDatenrichtung(Vector<WrapperButtonZusammenfuehren> vButtons) {
    if (this.kundeQuellDto != null && this.kundeZielDto != null) {
      for (Enumeration<WrapperButtonZusammenfuehren> e = vButtons.elements(); e.hasMoreElements(); ) {
        ( (WrapperButtonZusammenfuehren) e.nextElement()).setEnabled(true);
      }
    }
    else {
      for (Enumeration<WrapperButtonZusammenfuehren> e = vButtons.elements(); e.hasMoreElements(); ) {
        ( (WrapperButtonZusammenfuehren) e.nextElement()).setEnabled(false);
      }
    }
  }

  /**
   * je nach KundenDtos wird der zentrale Datenrichtungsbutton enabled oder disabled gesetzt
   */
  private void setzeStatusButtonResetDatenrichtung() {
    if (this.kundeQuellDto != null && this.kundeZielDto != null && this.kundeQuellDto.getIId().intValue() != this.kundeZielDto.getIId().intValue()) {
      wbuAuswahlReset.setEnabled(true);
    }
    else {
      wbuAuswahlReset.setEnabled(false);
    }
  }

  /**
   * je nach inhaltsvergleich der textfelder/checkboxen wird der datenauswahlbutton
   * in der mitte aktiv oder deaktiv geschaltet
   */
  private void setzeStatusVonButtonDatenrichtung() {
     if (this.kundeQuellDto != null && this.kundeZielDto != null) {

       if ((wtfZielKundePartnername.getText() != null && wtfQuellKundePartnername.getText() != null &&
           wtfQuellKundePartnername.getText().equals(wtfZielKundePartnername.getText())) || (wtfZielKundePartnername.getText() == null && wtfQuellKundePartnername.getText() == null)) {
         wbuPartnernameDatenrichtung.setEnabled(false);
       }
       else {
         wbuPartnernameDatenrichtung.setEnabled(true);
       }

       if ((wtfZielKundeMwst.getText() != null && wtfQuellKundeMwst.getText() != null &&
           wtfQuellKundeMwst.getText().equals(wtfZielKundeMwst.getText())) || (wtfZielKundeMwst.getText() == null && wtfQuellKundeMwst.getText() == null)) {
         wbuMwstDatenrichtung.setEnabled(false);
       }
       else {
         wbuMwstDatenrichtung.setEnabled(true);
       }

       if ((wtfZielKundeWaehrung.getText() != null && wtfQuellKundeWaehrung.getText() != null &&
           wtfQuellKundeWaehrung.getText().equals(wtfZielKundeWaehrung.getText())) || (wtfZielKundeWaehrung.getText() == null && wtfQuellKundeWaehrung.getText() == null)) {
         wbuWaehrungDatenrichtung.setEnabled(false);
       }
       else {
         wbuWaehrungDatenrichtung.setEnabled(true);
       }

       if ((wtfZielKundeLieferart.getText() != null && wtfQuellKundeLieferart.getText() != null &&
           wtfQuellKundeLieferart.getText().equals(wtfZielKundeLieferart.getText())) || (wtfZielKundeLieferart.getText() == null && wtfQuellKundeLieferart.getText() == null)) {
         wbuLieferartDatenrichtung.setEnabled(false);
       }
       else {
         wbuLieferartDatenrichtung.setEnabled(true);
       }

       if ((wtfZielKundeSpediteur.getText() != null && wtfQuellKundeSpediteur.getText() != null &&
           wtfQuellKundeSpediteur.getText().equals(wtfZielKundeSpediteur.getText())) || (wtfZielKundeSpediteur.getText() == null && wtfQuellKundeSpediteur.getText() == null)) {
         wbuSpediteurDatenrichtung.setEnabled(false);
       }
       else {
         wbuSpediteurDatenrichtung.setEnabled(true);
       }

       if ((wtfZielKundeZahlungsziel.getText() != null &&
           wtfQuellKundeZahlungsziel.getText() != null &&
           wtfQuellKundeZahlungsziel.getText().equals(wtfZielKundeZahlungsziel.getText())) || (wtfZielKundeZahlungsziel.getText() == null &&
           wtfQuellKundeZahlungsziel.getText() == null)) {
         wbuZahlungszielDatenrichtung.setEnabled(false);
       }
       else {
         wbuZahlungszielDatenrichtung.setEnabled(true);
       }

       if ((wtfZielKundeKostenstelle.getText() != null &&
           wtfQuellKundeKostenstelle.getText() != null &&
           wtfQuellKundeKostenstelle.getText().equals(wtfZielKundeKostenstelle.getText())) || (wtfZielKundeKostenstelle.getText() == null &&
           wtfQuellKundeKostenstelle.getText() == null)) {
         wbuKostenstelleDatenrichtung.setEnabled(false);
       }
       else {
         wbuKostenstelleDatenrichtung.setEnabled(true);
       }

       if ((wtfZielKundeProvisionsempfaenger.getText() != null &&
           wtfQuellKundeProvisionsempfaenger.getText() != null &&
           wtfQuellKundeProvisionsempfaenger.getText().equals(
                wtfZielKundeProvisionsempfaenger.getText())) || (wtfZielKundeProvisionsempfaenger.getText() == null &&
           wtfQuellKundeProvisionsempfaenger.getText() == null)) {
         wbuProvisionsempfaengerDatenrichtung.setEnabled(false);
       }
       else {
         wbuProvisionsempfaengerDatenrichtung.setEnabled(true);
       }

       if ((wtfZielKundeRabattsatz.getText() != null && wtfQuellKundeRabattsatz.getText() != null &&
           wtfQuellKundeRabattsatz.getText().equals(wtfZielKundeRabattsatz.getText())) || (wtfZielKundeRabattsatz.getText() == null && wtfQuellKundeRabattsatz.getText() == null)) {
         wbuRabattsatzDatenrichtung.setEnabled(false);
       }
       else {
         wbuRabattsatzDatenrichtung.setEnabled(true);
       }

       if ((wtfZielKundeGarantie.getText() != null && wtfQuellKundeGarantie.getText() != null &&
           wtfQuellKundeGarantie.getText().equals(wtfZielKundeGarantie.getText())) || (wtfZielKundeGarantie.getText() == null && wtfQuellKundeGarantie.getText() == null)) {
         wbuGarantieDatenrichtung.setEnabled(false);
       }
       else {
         wbuGarantieDatenrichtung.setEnabled(true);
       }

       if ((wtaZielKundeKommentar.getText() != null && wtaQuellKundeKommentar.getText() != null &&
           wtaQuellKundeKommentar.getText().equals(wtaZielKundeKommentar.getText())) || (wtaZielKundeKommentar.getText() == null && wtaQuellKundeKommentar.getText() == null)) {
         wbuKommentarDatenrichtung.setEnabled(false);
       }
       else {
         wbuKommentarDatenrichtung.setEnabled(true);
       }

       if ((wtfZielKundeKurznr.getText() != null && wtfQuellKundeKurznr.getText() != null &&
           wtfQuellKundeKurznr.getText().equals(wtfZielKundeKurznr.getText())) || (wtfZielKundeKurznr.getText() == null && wtfQuellKundeKurznr.getText() == null)) {
         wbuKurznrDatenrichtung.setEnabled(false);
       }
       else {
         wbuKurznrDatenrichtung.setEnabled(true);
       }

       if ((wtfZielKundeKreditlimit.getText() != null && wtfQuellKundeKreditlimit.getText() != null &&
           wtfQuellKundeKreditlimit.getText().equals(wtfZielKundeKreditlimit.getText())) || (wtfZielKundeKreditlimit.getText() == null && wtfQuellKundeKreditlimit.getText() == null)) {
         wbuKreditlimitDatenrichtung.setEnabled(false);
       }
       else {
         wbuKreditlimitDatenrichtung.setEnabled(true);
       }

       if ((wtfZielKundeBonitaetspruefung.getText() != null &&
           wtfQuellKundeBonitaetspruefung.getText() != null &&
           wtfQuellKundeBonitaetspruefung.getText().equals(wtfZielKundeBonitaetspruefung.
          getText())) || (wtfZielKundeBonitaetspruefung.getText() == null &&
           wtfQuellKundeBonitaetspruefung.getText() == null)) {
         wbuBonitaetspruefungDatenrichtung.setEnabled(false);
       }
       else {
         wbuBonitaetspruefungDatenrichtung.setEnabled(true);
       }

       if ((wtfZielKundeLiefersperre.getText() != null &&
           wtfQuellKundeLiefersperre.getText() != null &&
           wtfQuellKundeLiefersperre.getText().equals(wtfZielKundeLiefersperre.getText())) || (wtfZielKundeLiefersperre.getText() == null &&
           wtfQuellKundeLiefersperre.getText() == null)) {
         wbuLiefersperreDatenrichtung.setEnabled(false);
       }
       else {
         wbuLiefersperreDatenrichtung.setEnabled(true);
       }

       if ((wtfZielKundeRechnungskopien.getText() != null &&
           wtfQuellKundeRechnungskopien.getText() != null &&
           wtfQuellKundeRechnungskopien.
           getText().equals(wtfZielKundeRechnungskopien.getText())) || (wtfZielKundeRechnungskopien.getText() == null &&
           wtfQuellKundeRechnungskopien.getText() == null)) {
         wbuRechnungskopienDatenrichtung.setEnabled(false);
       }
       else {
         wbuRechnungskopienDatenrichtung.setEnabled(true);
       }

       if ((wtfZielKundeLieferscheinkopien.getText() != null &&
           wtfQuellKundeLieferscheinkopien.getText() != null &&
           wtfQuellKundeLieferscheinkopien.getText().equals(
           wtfZielKundeLieferscheinkopien.
           getText())) || (wtfZielKundeLieferscheinkopien.getText() == null &&
           wtfQuellKundeLieferscheinkopien.getText() == null)) {
         wbuLieferscheinkopienDatenrichtung.setEnabled(false);
       }
       else {
         wbuLieferscheinkopienDatenrichtung.setEnabled(true);
       }

       if ((wtfZielKundeMitarbeiter.getText() != null && wtfQuellKundeMitarbeiter.getText() != null &&
           wtfQuellKundeMitarbeiter.getText().equals(wtfZielKundeMitarbeiter.getText())) || (wtfZielKundeMitarbeiter.getText() == null && wtfQuellKundeMitarbeiter.getText() == null)) {
         wbuMitarbeiterDatenrichtung.setEnabled(false);
       }
       else {
         wbuMitarbeiterDatenrichtung.setEnabled(true);
       }

       if ((wtfZielKundeTour.getText() != null && wtfQuellKundeTour.getText() != null &&
           wtfQuellKundeTour.getText().equals(wtfZielKundeTour.getText())) || (wtfZielKundeTour.getText() == null && wtfQuellKundeTour.getText() == null)) {
         wbuTourDatenrichtung.setEnabled(false);
       }
       else {
         wbuTourDatenrichtung.setEnabled(true);
       }

       if ((wtfZielKundeLieferantennr.getText() != null &&
           wtfQuellKundeLieferantennr.getText() != null &&
           wtfQuellKundeLieferantennr.getText().equals(wtfZielKundeLieferantennr.getText())) || (wtfZielKundeLieferantennr.getText() == null &&
           wtfQuellKundeLieferantennr.getText() == null)) {
         wbuLieferantennrDatenrichtung.setEnabled(false);
       }
       else {
         wbuLieferantennrDatenrichtung.setEnabled(true);
       }

       if ((wtfZielKundeAbc.getText() != null && wtfQuellKundeAbc.getText() != null &&
           wtfQuellKundeAbc.getText().equals(wtfZielKundeAbc.getText())) || (wtfZielKundeAbc.getText() == null && wtfQuellKundeAbc.getText() == null)) {
         wbuAbcDatenrichtung.setEnabled(false);
       }
       else {
         wbuAbcDatenrichtung.setEnabled(true);
       }

       if ((wtfZielKundeAgb.getText() != null && wtfQuellKundeAgb.getText() != null &&
           wtfQuellKundeAgb.getText().equals(wtfZielKundeAgb.getText())) || (wtfZielKundeAgb.getText() == null && wtfQuellKundeAgb.getText() == null)) {
         wbuAgbDatenrichtung.setEnabled(false);
       }
       else {
         wbuAgbDatenrichtung.setEnabled(true);
       }

       if ((wtfZielKundeFremdsystemnr.getText() != null &&
           wtfQuellKundeFremdsystemnr.getText() != null &&
           wtfQuellKundeFremdsystemnr.getText().equals(wtfZielKundeFremdsystemnr.getText())) || (wtfZielKundeFremdsystemnr.getText() == null &&
           wtfQuellKundeFremdsystemnr.getText() == null)) {
         wbuFremdsystemnrDatenrichtung.setEnabled(false);
       }
       else {
         wbuFremdsystemnrDatenrichtung.setEnabled(true);
       }

       if ((wtfZielKundeErloeskonto.getText() != null && wtfQuellKundeErloeskonto.getText() != null &&
           wtfQuellKundeErloeskonto.getText().equals(wtfZielKundeErloeskonto.getText())) || (wtfZielKundeErloeskonto.getText() == null && wtfQuellKundeErloeskonto.getText() == null)) {
         wbuErloeskontoDatenrichtung.setEnabled(false);
       }
       else {
         wbuErloeskontoDatenrichtung.setEnabled(true);
       }

       if ((wtfZielKundeDebitorenkonto.getText() != null &&
           wtfQuellKundeDebitorenkonto.getText() != null &&
           wtfQuellKundeDebitorenkonto.getText().equals(wtfZielKundeDebitorenkonto.getText())) || (wtfZielKundeDebitorenkonto.getText() == null &&
           wtfQuellKundeDebitorenkonto.getText() == null)) {
         wbuDebitorenkontoDatenrichtung.setEnabled(false);
       }
       else {
         wbuDebitorenkontoDatenrichtung.setEnabled(true);
       }

       if ((wtfZielKundeRechnungsadresspartner.getText() != null &&
           wtfQuellKundeRechnungsadresspartner.getText() != null &&
           wtfQuellKundeRechnungsadresspartner.getText().equals(
                wtfZielKundeRechnungsadresspartner.getText())) || (wtfZielKundeRechnungsadresspartner.getText() == null &&
           wtfQuellKundeRechnungsadresspartner.getText() == null)) {
         wbuRechnungsadresspartnerDatenrichtung.setEnabled(false);
       }
       else {
         wbuRechnungsadresspartnerDatenrichtung.setEnabled(true);
       }

       if ((wtfZielKundeHinweisInt.getText() != null && wtfQuellKundeHinweisInt.getText() != null &&
           wtfQuellKundeHinweisInt.getText().equals(wtfZielKundeHinweisInt.getText())) || (wtfZielKundeHinweisInt.getText() == null && wtfQuellKundeHinweisInt.getText() == null)) {
         wbuHinweisIntDatenrichtung.setEnabled(false);
       }
       else {
         wbuHinweisIntDatenrichtung.setEnabled(true);
       }

       if ((wtfZielKundeHinweisExt.getText() != null && wtfQuellKundeHinweisExt.getText() != null &&
           wtfQuellKundeHinweisExt.getText().equals(wtfZielKundeHinweisExt.getText())) || (wtfZielKundeHinweisExt.getText() == null && wtfQuellKundeHinweisExt.getText() == null)) {
         wbuHinweisExtDatenrichtung.setEnabled(false);
       }
       else {
         wbuHinweisExtDatenrichtung.setEnabled(true);
       }

       if ((wtfZielKundeZessionsfaktor.getText() != null &&
           wtfQuellKundeZessionsfaktor.getText() != null &&
           wtfQuellKundeZessionsfaktor.getText().equals(wtfZielKundeZessionsfaktor.
          getText())) || (wtfZielKundeZessionsfaktor.getText() == null &&
           wtfQuellKundeZessionsfaktor.getText() == null)) {
         wbuZessionsfaktorDatenrichtung.setEnabled(false);
       }
       else {
         wbuZessionsfaktorDatenrichtung.setEnabled(true);
       }

       if ((wtfZielKundePreisliste.getText() != null && wtfQuellKundePreisliste.getText() != null &&
           wtfQuellKundePreisliste.getText().equals(wtfZielKundePreisliste.getText())) || (wtfZielKundePreisliste.getText() == null && wtfQuellKundePreisliste.getText() == null)) {
         wbuPreislisteDatenrichtung.setEnabled(false);
       }
       else {
         wbuPreislisteDatenrichtung.setEnabled(true);
       }

       if ((wtfZielKundeKundennr.getText() != null && wtfQuellKundeKundennr.getText() != null &&
           wtfQuellKundeKundennr.getText().equals(wtfZielKundeKundennr.getText())) || (wtfZielKundeKundennr.getText() == null && wtfQuellKundeKundennr.getText() == null)) {
         wbuKundennrDatenrichtung.setEnabled(false);
       }
       else {
         wbuKundennrDatenrichtung.setEnabled(true);
       }

       if ((wtfZielKundeKundensoko.getText() != null && wtfQuellKundeKundensoko.getText() != null && wtfZielKundeKundensoko.getText().equals("") &&
           wtfQuellKundeKundensoko.getText().equals(wtfZielKundeKundensoko.getText())) || (wtfZielKundeKundensoko.getText() == null && wtfQuellKundeKundensoko.getText() == null) ||
           (wtfZielKundeKundensoko.getText() != null && wtfQuellKundeKundensoko.getText() != null && wtfZielKundeKundensoko.getText().equals(sKundesokoNullEintraege) &&
           wtfQuellKundeKundensoko.getText().equals(sKundesokoNullEintraege)) ) {
         wbuKundensokoDatenrichtung.setEnabled(false);
       }
       else {
         wbuKundensokoDatenrichtung.setEnabled(true);
       }

       if (wcbQuellKundeMindermengenzuschlag.getShort().equals(
           wcbZielKundeMindermengenzuschlag.getShort())) {
         wbuMindermengenzuschlagDatenrichtung.setEnabled(false);
       }
       else {
         wbuMindermengenzuschlagDatenrichtung.setEnabled(true);
       }

       if (wcbQuellKundeMonatsrechnung.getShort().equals(
           wcbZielKundeMonatsrechnung.getShort())) {
         wbuMonatsrechnungDatenrichtung.setEnabled(false);
       }
       else {
         wbuMonatsrechnungDatenrichtung.setEnabled(true);
       }

       if (wcbQuellKundeSammelrechnung.getShort().equals(
           wcbZielKundeSammelrechnung.getShort())) {
         wbuSammelrechnungDatenrichtung.setEnabled(false);
       }
       else {
         wbuSammelrechnungDatenrichtung.setEnabled(true);
       }

       if (wcbQuellKundeRechnungsempfaenger.getShort().equals(
           wcbZielKundeRechnungsempfaenger.getShort())) {
         wbuRechnungsempfaengerDatenrichtung.setEnabled(false);
       }
       else {
         wbuRechnungsempfaengerDatenrichtung.setEnabled(true);
       }

       if (wcbQuellKundePreiseAmLs.getShort().equals(
           wcbZielKundePreiseAmLs.getShort())) {
         wbuPreiseAmLsDatenrichtung.setEnabled(false);
       }
       else {
         wbuPreiseAmLsDatenrichtung.setEnabled(true);
       }

       if (wcbQuellKundeBelegdruckMitRabatt.getShort().equals(
           wcbZielKundeBelegdruckMitRabatt.getShort())) {
         wbuBelegdruckMitRabattDatenrichtung.setEnabled(false);
       }
       else {
         wbuBelegdruckMitRabattDatenrichtung.setEnabled(true);
       }

       if (wcbQuellKundeDistributor.getShort().equals(
           wcbZielKundeDistributor.getShort())) {
         wbuDistributorDatenrichtung.setEnabled(false);
       }
       else {
         wbuDistributorDatenrichtung.setEnabled(true);
       }

       if (wcbQuellKundeTeillieferungen.getShort().equals(
           wcbZielKundeTeillieferungen.getShort())) {
         wbuTeillieferungenDatenrichtung.setEnabled(false);
       }
       else {
         wbuTeillieferungenDatenrichtung.setEnabled(true);
       }

       if (wcbQuellKundeLsgewicht.getShort().equals(
           wcbZielKundeLsgewicht.getShort())) {
         wbuLsgewichtDatenrichtung.setEnabled(false);
       }
       else {
         wbuLsgewichtDatenrichtung.setEnabled(true);
       }

       if (wcbQuellKundeInteressent.getShort().equals(
           wcbZielKundeInteressent.getShort())) {
         wbuInteressentDatenrichtung.setEnabled(false);
       }
       else {
         wbuInteressentDatenrichtung.setEnabled(true);
       }

       if (wcbQuellKundeVersteckt.getShort().equals(
           wcbZielKundeVersteckt.getShort())) {
         wbuVerstecktDatenrichtung.setEnabled(false);
       }
       else {
         wbuVerstecktDatenrichtung.setEnabled(true);
       }

       if (wcbQuellKundeReversecharge.getShort().equals(
           wcbZielKundeReversecharge.getShort())) {
         wbuReversechargeDatenrichtung.setEnabled(false);
       }
       else {
         wbuReversechargeDatenrichtung.setEnabled(true);
       }

     }
     else {
       setzeStatusAllerButtonDatenrichtung(this.vWrapperButtonsZusammenfuehren);
     }
  }

  /**
   * wird ein Quellkunde (=Kunde auf der rechten Seite) ausgewaehlt, so werden die Felder mit dessen Daten gefuellt
   * @param kqDto KundeDto
   * @throws Throwable
   */
  private void befuelleFelderMitKundeQuellDto(KundeDto kqDto)
      throws Throwable {
    if (this.kundeQuellDto != null && this.kundeZielDto != null) {
      if (kqDto.getPartnerDto().formatName() != null) {
        wtfQuellKundePartnername.setText(kqDto.getPartnerDto().formatName());
        wtfQuellKundePartnername.setCaretPosition(0);
        wtfQuellKundePartnername.setToolTipText(kqDto.getPartnerDto().formatName());
      }
      else {
        wtfQuellKundePartnername.setText("");
        wtfQuellKundePartnername.setToolTipText("");
      }

      if (kqDto.getMwstsatzbezIId() != null) {
        wtfQuellKundeMwst.setText(DelegateFactory.getInstance().getMandantDelegate().
                                  mwstsatzbezFindByPrimaryKey(kqDto.getMwstsatzbezIId()).
                                  getCBezeichnung());
        wtfQuellKundeMwst.setCaretPosition(0);
        wtfQuellKundeMwst.setToolTipText(DelegateFactory.getInstance().getMandantDelegate().
                                         mwstsatzbezFindByPrimaryKey(kqDto.
            getMwstsatzbezIId()).getCBezeichnung());
      }
      else {
        wtfQuellKundeMwst.setText("");
        wtfQuellKundeMwst.setToolTipText("");
      }

      if (kqDto.getCWaehrung() != null) {
        wtfQuellKundeWaehrung.setText(kqDto.getCWaehrung());
        wtfQuellKundeWaehrung.setCaretPosition(0);
        wtfQuellKundeWaehrung.setToolTipText(kqDto.getCWaehrung());
      }
      else {
        wtfQuellKundeWaehrung.setText("");
        wtfQuellKundeWaehrung.setToolTipText("");
      }

      if (kqDto.getLieferartIId() != null) {
        wtfQuellKundeLieferart.setText(DelegateFactory.getInstance().getLocaleDelegate().
                                       lieferartFindByPrimaryKey(kqDto.getLieferartIId()).
                                       formatBez());
        wtfQuellKundeLieferart.setCaretPosition(0);
        wtfQuellKundeLieferart.setToolTipText(DelegateFactory.getInstance().
                                              getLocaleDelegate().
                                              lieferartFindByPrimaryKey(
                                                  kqDto.getLieferartIId()).formatBez());
      }
      else {
        wtfQuellKundeLieferart.setText("");
        wtfQuellKundeLieferart.setToolTipText("");
      }

      if (kqDto.getSpediteurIId() != null) {
        SpediteurDto spediteurDto = DelegateFactory.getInstance().getMandantDelegate().
            spediteurFindByPrimaryKey(kqDto.getSpediteurIId());
        wtfQuellKundeSpediteur.setText(spediteurDto.getCNamedesspediteurs());
        wtfQuellKundeSpediteur.setCaretPosition(0);
        wtfQuellKundeSpediteur.setToolTipText(spediteurDto.getCNamedesspediteurs());
      }
      else {
        wtfQuellKundeSpediteur.setText("");
        wtfQuellKundeSpediteur.setToolTipText("");
      }

      if (kqDto.getZahlungszielIId() != null) {
        ZahlungszielDto zahlungszielDto = DelegateFactory.getInstance().
            getMandantDelegate().
            zahlungszielFindByPrimaryKey(kqDto.getZahlungszielIId());
        wtfQuellKundeZahlungsziel.setText(zahlungszielDto.getCBez());
        wtfQuellKundeZahlungsziel.setCaretPosition(0);
        wtfQuellKundeZahlungsziel.setToolTipText(zahlungszielDto.getCBez());
      }
      else {
        wtfQuellKundeZahlungsziel.setText("");
        wtfQuellKundeZahlungsziel.setToolTipText("");
      }

      if (kqDto.getKostenstelleIId() != null) {
        KostenstelleDto kostenstelleDto = DelegateFactory.getInstance().getSystemDelegate().
            kostenstelleFindByPrimaryKey(kqDto.getKostenstelleIId());
        wtfQuellKundeKostenstelle.setText(kostenstelleDto.formatKostenstellenbezeichnung());
        wtfQuellKundeKostenstelle.setCaretPosition(0);
        wtfQuellKundeKostenstelle.setToolTipText(kostenstelleDto.
                                                 formatKostenstellenbezeichnung());
      }
      else {
        wtfQuellKundeKostenstelle.setText("");
        wtfQuellKundeKostenstelle.setToolTipText("");
      }

      if (kqDto.getPersonaliIdProvisionsempfaenger() != null) {
        PersonalDto personalDto = DelegateFactory.getInstance().getPersonalDelegate().
            personalFindByPrimaryKey(kqDto.getPersonaliIdProvisionsempfaenger());
        wtfQuellKundeProvisionsempfaenger.setText(personalDto.formatFixUFTitelName2Name1());
        wtfQuellKundeProvisionsempfaenger.setCaretPosition(0);
        wtfQuellKundeProvisionsempfaenger.setToolTipText(personalDto.
            formatFixUFTitelName2Name1());
      }
      else {
        wtfQuellKundeProvisionsempfaenger.setText("");
        wtfQuellKundeProvisionsempfaenger.setToolTipText("");
      }

      if (kqDto.getFRabattsatz() != null) {
        BigDecimal bdRabatt = new BigDecimal(kqDto.getFRabattsatz());
        wtfQuellKundeRabattsatz.setText(Helper.formatZahl(bdRabatt, Locale.getDefault()) +
                                        " %");
        wtfQuellKundeRabattsatz.setCaretPosition(0);
        wtfQuellKundeRabattsatz.setToolTipText(Helper.formatZahl(bdRabatt,
            Locale.getDefault()) + " %");
      }
      else {
        wtfQuellKundeRabattsatz.setText("");
        wtfQuellKundeRabattsatz.setToolTipText("");
      }

      if (kqDto.getIGarantieinmonaten() != null) {
        wtfQuellKundeGarantie.setText(Integer.toString(kqDto.getIGarantieinmonaten()) +
                                      " " +
                                      LPMain.getInstance().getTextRespectUISPr("lp.monat"));
        wtfQuellKundeGarantie.setCaretPosition(0);
        wtfQuellKundeGarantie.setToolTipText(Integer.toString(kqDto.getIGarantieinmonaten()) +
                                             " " +
                                             LPMain.getInstance().getTextRespectUISPr(
            "lp.monat"));
      }
      else {
        wtfQuellKundeGarantie.setText("");
        wtfQuellKundeGarantie.setToolTipText("");
      }

      if (kqDto.getXKommentar() != null) {
        wtaQuellKundeKommentar.setText(kqDto.getXKommentar());
        wtaQuellKundeKommentar.setToolTipText(kqDto.getXKommentar());
      }
      else {
        wtaQuellKundeKommentar.setText("");
        wtaQuellKundeKommentar.setToolTipText("");
      }

      if (kqDto.getCKurznr() != null) {
        wtfQuellKundeKurznr.setText(kqDto.getCKurznr());
        wtfQuellKundeKurznr.setCaretPosition(0);
        wtfQuellKundeKurznr.setToolTipText(kqDto.getCKurznr());
      }
      else {
        wtfQuellKundeKurznr.setText("");
        wtfQuellKundeKurznr.setToolTipText("");
      }

      if (kqDto.getNKreditlimit() != null) {
        BigDecimal bdLimit = kqDto.getNKreditlimit();
        wtfQuellKundeKreditlimit.setText(kqDto.getCWaehrung() + " " +
                                         Helper.formatZahl(bdLimit, Locale.getDefault()));
        wtfQuellKundeKreditlimit.setCaretPosition(0);
        wtfQuellKundeKreditlimit.setToolTipText(kqDto.getCWaehrung() + " " +
                                                Helper.
                                                formatZahl(bdLimit, Locale.getDefault()));
      }
      else {
        wtfQuellKundeKreditlimit.setText("");
        wtfQuellKundeKreditlimit.setToolTipText("");
      }

      if (kqDto.getTBonitaet() != null) {
        Date dBonitaet = kqDto.getTBonitaet();
        wtfQuellKundeBonitaetspruefung.setText(Helper.formatDatum(dBonitaet,
            Locale.getDefault()));
        wtfQuellKundeBonitaetspruefung.setCaretPosition(0);
        wtfQuellKundeBonitaetspruefung.setToolTipText(Helper.formatDatum(dBonitaet,
            Locale.getDefault()));
      }
      else {
        wtfQuellKundeBonitaetspruefung.setText("");
        wtfQuellKundeBonitaetspruefung.setToolTipText("");
      }

      if (kqDto.getTLiefersperream() != null) {
        Date dLiefersperre = kqDto.getTLiefersperream();
        wtfQuellKundeLiefersperre.setText(Helper.formatDatum(dLiefersperre,
            Locale.getDefault()));
        wtfQuellKundeLiefersperre.setCaretPosition(0);
        wtfQuellKundeLiefersperre.setToolTipText(Helper.formatDatum(dLiefersperre,
            Locale.getDefault()));
      }
      else {
        wtfQuellKundeLiefersperre.setText("");
        wtfQuellKundeLiefersperre.setToolTipText("");
      }

      if (kqDto.getIDefaultrekopiendrucken() != null) {
        wtfQuellKundeRechnungskopien.setText(kqDto.getIDefaultrekopiendrucken().toString());
        wtfQuellKundeRechnungskopien.setCaretPosition(0);
        wtfQuellKundeRechnungskopien.setToolTipText(kqDto.getIDefaultrekopiendrucken().
            toString());
      }
      else {
        wtfQuellKundeRechnungskopien.setText("");
        wtfQuellKundeRechnungskopien.setToolTipText("");
      }

      if (kqDto.getIDefaultlskopiendrucken() != null) {
        wtfQuellKundeLieferscheinkopien.setText(kqDto.getIDefaultlskopiendrucken().
                                                toString());
        wtfQuellKundeLieferscheinkopien.setCaretPosition(0);
        wtfQuellKundeLieferscheinkopien.setToolTipText(kqDto.getIDefaultlskopiendrucken().
            toString());
      }
      else {
        wtfQuellKundeLieferscheinkopien.setText("");
        wtfQuellKundeLieferscheinkopien.setToolTipText("");
      }

      if (kqDto.getIMitarbeiteranzahl() != null) {
        wtfQuellKundeMitarbeiter.setText(kqDto.getIMitarbeiteranzahl().toString());
        wtfQuellKundeMitarbeiter.setCaretPosition(0);
        wtfQuellKundeMitarbeiter.setToolTipText(kqDto.getIMitarbeiteranzahl().toString());
      }
      else {
        wtfQuellKundeMitarbeiter.setText("");
        wtfQuellKundeMitarbeiter.setToolTipText("");
      }

      if (kqDto.getCTour() != null) {
        wtfQuellKundeTour.setText(kqDto.getCTour());
        wtfQuellKundeTour.setCaretPosition(0);
        wtfQuellKundeTour.setToolTipText(kqDto.getCTour());
      }
      else {
        wtfQuellKundeTour.setText("");
        wtfQuellKundeTour.setToolTipText("");
      }

      if (kqDto.getCLieferantennr() != null) {
        wtfQuellKundeLieferantennr.setText(kqDto.getCLieferantennr());
        wtfQuellKundeLieferantennr.setCaretPosition(0);
        wtfQuellKundeLieferantennr.setToolTipText(kqDto.getCLieferantennr());
      }
      else {
        wtfQuellKundeLieferantennr.setText("");
        wtfQuellKundeLieferantennr.setToolTipText("");
      }

      if (kqDto.getCAbc() != null) {
        wtfQuellKundeAbc.setText(kqDto.getCAbc());
        wtfQuellKundeAbc.setCaretPosition(0);
        wtfQuellKundeAbc.setToolTipText(kqDto.getCAbc());
      }
      else {
        wtfQuellKundeAbc.setText("");
        wtfQuellKundeAbc.setToolTipText("");
      }

      if (kqDto.getTAgbuebermittelung() != null) {
        Date dAgb = kqDto.getTAgbuebermittelung();
        wtfQuellKundeAgb.setText(Helper.formatDatum(dAgb, Locale.getDefault()));
        wtfQuellKundeAgb.setCaretPosition(0);
        wtfQuellKundeAgb.setToolTipText(Helper.formatDatum(dAgb, Locale.getDefault()));
      }
      else {
        wtfQuellKundeAgb.setText("");
        wtfQuellKundeAgb.setToolTipText("");
      }

      if (kqDto.getCFremdsystemnr() != null) {
        wtfQuellKundeFremdsystemnr.setText(kqDto.getCFremdsystemnr());
        wtfQuellKundeFremdsystemnr.setCaretPosition(0);
        wtfQuellKundeFremdsystemnr.setToolTipText(kqDto.getCFremdsystemnr());
      }
      else {
        wtfQuellKundeFremdsystemnr.setText("");
        wtfQuellKundeFremdsystemnr.setToolTipText("");
      }

      if (kqDto.getIidErloeseKonto() != null) {
        KontoDtoSmall kontoDtoSmall = DelegateFactory.getInstance().getFinanzDelegate().
            kontoFindByPrimaryKeySmall(kqDto.getIidErloeseKonto());
        wtfQuellKundeErloeskonto.setText(kontoDtoSmall.getCNr());
        wtfQuellKundeErloeskonto.setCaretPosition(0);
        wtfQuellKundeErloeskonto.setToolTipText(kontoDtoSmall.getCNr() + " " +
                                                kontoDtoSmall.getCBez());
      }
      else {
        wtfQuellKundeErloeskonto.setText("");
        wtfQuellKundeErloeskonto.setToolTipText("");
      }

      if (kqDto.getIidDebitorenkonto() != null) {
        KontoDto kontoDto = DelegateFactory.getInstance().getFinanzDelegate().
            kontoFindByPrimaryKey(kqDto.getIidDebitorenkonto());
        wtfQuellKundeDebitorenkonto.setText(kontoDto.getCNr());
        wtfQuellKundeDebitorenkonto.setCaretPosition(0);
        wtfQuellKundeDebitorenkonto.setToolTipText(kontoDto.getCNr() + " " +
            kontoDto.getCBez());
      }
      else {
        wtfQuellKundeDebitorenkonto.setText("");
        wtfQuellKundeDebitorenkonto.setToolTipText("");
      }

      if (kqDto.getPartnerIIdRechnungsadresse() != null) {
        PartnerDto partnerDto = null;
        if (kqDto.getPartnerRechnungsadresseDto() != null) {
          partnerDto = kqDto.getPartnerRechnungsadresseDto();
        }
        else {
          partnerDto = DelegateFactory.getInstance().getPartnerDelegate().
              partnerFindByPrimaryKey(kqDto.getPartnerIIdRechnungsadresse());
        }
        wtfQuellKundeRechnungsadresspartner.setText(partnerDto.formatAdresse());
        wtfQuellKundeRechnungsadresspartner.setCaretPosition(0);
        wtfQuellKundeRechnungsadresspartner.setToolTipText(partnerDto.formatAdresse());
      }
      else {
        wtfQuellKundeRechnungsadresspartner.setText("");
        wtfQuellKundeRechnungsadresspartner.setToolTipText("");
      }

      if (kqDto.getSHinweisintern() != null) {
        wtfQuellKundeHinweisInt.setText(kqDto.getSHinweisintern());
        wtfQuellKundeHinweisInt.setCaretPosition(0);
        wtfQuellKundeHinweisInt.setToolTipText(kqDto.getSHinweisintern());
      }
      else {
        wtfQuellKundeHinweisInt.setText("");
        wtfQuellKundeHinweisInt.setToolTipText("");
      }

      if (kqDto.getSHinweisextern() != null) {
        wtfQuellKundeHinweisExt.setText(kqDto.getSHinweisextern());
        wtfQuellKundeHinweisExt.setCaretPosition(0);
        wtfQuellKundeHinweisExt.setToolTipText(kqDto.getSHinweisextern());
      }
      else {
        wtfQuellKundeHinweisExt.setText("");
        wtfQuellKundeHinweisExt.setToolTipText("");
      }

      if (kqDto.getFZessionsfaktor() != null) {
        BigDecimal bdZession = new BigDecimal(kqDto.getFZessionsfaktor());
        wtfQuellKundeZessionsfaktor.setText(Helper.formatZahl(bdZession,
            Locale.getDefault()) +
                                            " %");
        wtfQuellKundeZessionsfaktor.setCaretPosition(0);
        wtfQuellKundeZessionsfaktor.setToolTipText(Helper.formatZahl(bdZession,
            Locale.getDefault()) + " %");
      }
      else {
        wtfQuellKundeZessionsfaktor.setText("");
        wtfQuellKundeZessionsfaktor.setToolTipText("");
      }

      if (kqDto.getVkpfArtikelpreislisteIIdStdpreisliste() != null) {
        VkpfartikelpreislisteDto oVkpfartikelpreislisteDto = DelegateFactory.getInstance().
            getVkPreisfindungDelegate().vkpfartikelpreislisteFindByPrimaryKey(kqDto.
            getVkpfArtikelpreislisteIIdStdpreisliste());
        wtfQuellKundePreisliste.setText(oVkpfartikelpreislisteDto.getCNr());
        wtfQuellKundePreisliste.setCaretPosition(0);
        wtfQuellKundePreisliste.setToolTipText(oVkpfartikelpreislisteDto.getCNr());
      }
      else {
        wtfQuellKundePreisliste.setText("");
        wtfQuellKundePreisliste.setToolTipText("");
      }

      if (kqDto.getIKundennummer() != null) {
        wtfQuellKundeKundennr.setText(Helper.formatZahl(kqDto.getIKundennummer(),
            Locale.getDefault()));
        wtfQuellKundeKundennr.setCaretPosition(0);
        wtfQuellKundeKundennr.setToolTipText(Helper.formatZahl(kqDto.getIKundennummer(),
            Locale.getDefault()));
      }
      else {
        wtfQuellKundeKundennr.setText("");
        wtfQuellKundeKundennr.setToolTipText("");
      }

      KundesokoDto[] aKundesokoDtos = DelegateFactory.getInstance().getKundesokoDelegate().kundesokoFindByKundeIIdOhneExc(kundeQuellDto.getIId());
      Integer iAnzahlKundensokoeintraegeQuelle = aKundesokoDtos.length;
      if (iAnzahlKundensokoeintraegeQuelle != null) {
        wtfQuellKundeKundensoko.setText(iAnzahlKundensokoeintraegeQuelle.toString() + " " + LPMain.getInstance().getTextRespectUISPr("part.kunde.zusammenfuehre.eintraege"));
        wtfQuellKundeKundensoko.setCaretPosition(0);
        wtfQuellKundeKundensoko.setToolTipText(iAnzahlKundensokoeintraegeQuelle.toString() + " " + LPMain.getInstance().getTextRespectUISPr("part.kunde.zusammenfuehre.eintraege"));
      }
      else {
        wtfQuellKundeKundensoko.setText("");
        wtfQuellKundeKundensoko.setToolTipText("");
      }

      if (kqDto.getBMindermengenzuschlag() != null) {
        wcbQuellKundeMindermengenzuschlag.setShort(kqDto.getBMindermengenzuschlag());
      }
      else {
        wcbQuellKundeMindermengenzuschlag.setShort(Helper.boolean2Short(false));
      }

      if (kqDto.getBMonatsrechnung() != null) {
        wcbQuellKundeMonatsrechnung.setShort(kqDto.getBMonatsrechnung());
      }
      else {
        wcbQuellKundeMonatsrechnung.setShort(Helper.boolean2Short(false));
      }

      if (kqDto.getBSammelrechnung() != null) {
        wcbQuellKundeSammelrechnung.setShort(kqDto.getBSammelrechnung());
      }
      else {
        wcbQuellKundeSammelrechnung.setShort(Helper.boolean2Short(false));
      }

      if (kqDto.getBIstreempfaenger() != null) {
        wcbQuellKundeRechnungsempfaenger.setShort(kqDto.getBIstreempfaenger());
      }
      else {
        wcbQuellKundeRechnungsempfaenger.setShort(Helper.boolean2Short(false));
      }

      if (kqDto.getBPreiseanlsandrucken() != null) {
        wcbQuellKundePreiseAmLs.setShort(kqDto.getBPreiseanlsandrucken());
      }
      else {
        wcbQuellKundePreiseAmLs.setShort(Helper.boolean2Short(false));
      }

      if (kqDto.getBRechnungsdruckmitrabatt() != null) {
        wcbQuellKundeBelegdruckMitRabatt.setShort(kqDto.getBRechnungsdruckmitrabatt());
      }
      else {
        wcbQuellKundeBelegdruckMitRabatt.setShort(Helper.boolean2Short(false));
      }

      if (kqDto.getBDistributor() != null) {
        wcbQuellKundeDistributor.setShort(kqDto.getBDistributor());
      }
      else {
        wcbQuellKundeDistributor.setShort(Helper.boolean2Short(false));
      }

      if (kqDto.getBAkzeptiertteillieferung() != null) {
        wcbQuellKundeTeillieferungen.setShort(kqDto.getBAkzeptiertteillieferung());
      }
      else {
        wcbQuellKundeTeillieferungen.setShort(Helper.boolean2Short(false));
      }

      if (kqDto.getBLsgewichtangeben() != null) {
        wcbQuellKundeLsgewicht.setShort(kqDto.getBLsgewichtangeben());
      }
      else {
        wcbQuellKundeLsgewicht.setShort(Helper.boolean2Short(false));
      }

      if (kqDto.getbIstinteressent() != null) {
        wcbQuellKundeInteressent.setShort(kqDto.getbIstinteressent());
      }
      else {
        wcbQuellKundeInteressent.setShort(Helper.boolean2Short(false));
      }

      if (kqDto.getBVersteckterlieferant() != null) {
        wcbQuellKundeVersteckt.setShort(kqDto.getBVersteckterlieferant());
      }
      else {
        wcbQuellKundeVersteckt.setShort(Helper.boolean2Short(false));
      }

      if (kqDto.getBReversecharge() != null) {
        wcbQuellKundeReversecharge.setShort(kqDto.getBReversecharge());
      }
      else {
        wcbQuellKundeReversecharge.setShort(Helper.boolean2Short(false));
      }

      setzeStatusVonButtonDatenrichtung();
    }
    else {
      setzeStatusAllerButtonDatenrichtung(vWrapperButtonsZusammenfuehren);
    }
    setzeStatusButtonResetDatenrichtung();

  }


  protected void eventActionSpecial(ActionEvent e)
      throws Throwable {
    if (e.getActionCommand().equals(PanelBasis.ESC) ||
        e.getActionCommand().equals(ACTION_SPECIAL_CLOSE_PANELDIALOG)) {
      bHandleEventInSuperklasse = true;

      if (bHandleEventInSuperklasse) {
        super.eventActionSpecial(e); // close Dialog
      }
    }
    else if (e.getActionCommand().equals(ACTION_SPECIAL_SAVE)) {
      boolean erfolgreich = false;
      if (this.kundeQuellDto != null && this.kundeZielDto != null &&
          this.kundeQuellDto.getIId().intValue() != this.kundeZielDto.getIId().intValue()) {
        schreibeRichtungInZielKunde();
        try {
          DelegateFactory.getInstance().getKundeDelegate().zusammenfuehrenKunde(this.kundeZielDto, this.kundeQuellDto.getIId(), this.iKundensokoSelectedKundeIId.intValue(), this.iKundePartnerIId);
          erfolgreich = true;
        }
        catch (ExceptionLP ex) {
          boolean b = handleOwnException(ex);
        }
      }
      else {
        // warnung, quelle und ziel sind identisch
        String sText = LPMain.getInstance().getTextRespectUISPr(
            "part.zusammenfuehren.warnung.kunde.quelle.ziel.identisch");
        DialogFactory.showModalDialog(LPMain.getInstance().getTextRespectUISPr(
            "part.kundenzusammenfuehren"), sText);
      }
      if (erfolgreich) {
        // AuswahlFLR refreshen
        ( (InternalFrameKunde) getInternalFrame()).getTpKunde().getPanelKundeQP1().
            eventYouAreSelected(true);
        getInternalFrame().closePanelDialog();
      }
    }

    if (e.getActionCommand().equals(ACTION_ZIELKUNDE_AUSWAEHLEN)) {
      panelQueryKundeFlr = PartnerFilterFactory.getInstance().createPanelFLRKunde(
          getInternalFrame(), true, true);
      new DialogQuery(panelQueryKundeFlr);
      Object oKey = panelQueryKundeFlr.getSelectedId();
      KundeDto kundeDtoTmp = DelegateFactory.getInstance().getKundeDelegate().kundeFindByPrimaryKey( (Integer) oKey);
      if (this.kundeQuellDto != null && this.kundeQuellDto.getIId() != null && this.kundeQuellDto.getIId().intValue() == kundeDtoTmp.getIId().intValue()) {
        String sMsg = LPMain.getInstance().getTextRespectUISPr("part.zusammenfuehren.warnung.kunde.bereits.ausgewaehlt");
        DialogFactory.showModalDialog(LPMain.getInstance().getTextRespectUISPr("part.kundenzusammenfuehren"), sMsg);
      }
      else {
        this.kundeZielDto = kundeDtoTmp;
        befuelleFelderMitKundeZielDto(this.kundeZielDto);
        setSaveButtonStatus();
      }
    }

    if (e.getActionCommand().equals(ACTION_QUELLKUNDE_AUSWAEHLEN)) {
      panelQueryKundeFlr = PartnerFilterFactory.getInstance().createPanelFLRKunde(
          getInternalFrame(), true, true);
      new DialogQuery(panelQueryKundeFlr);
      Object oKey = panelQueryKundeFlr.getSelectedId();
      KundeDto kundeDtoTmp = DelegateFactory.getInstance().getKundeDelegate().kundeFindByPrimaryKey( (Integer) oKey);
      if (this.kundeZielDto != null && this.kundeZielDto.getIId() != null && this.kundeZielDto.getIId().intValue() == kundeDtoTmp.getIId().intValue()) {
        String sMsg = LPMain.getInstance().getTextRespectUISPr("part.zusammenfuehren.warnung.kunde.bereits.ausgewaehlt");
        DialogFactory.showModalDialog(LPMain.getInstance().getTextRespectUISPr("part.kundenzusammenfuehren"), sMsg);
      }
      else {
        this.kundeQuellDto = kundeDtoTmp;
        befuelleFelderMitKundeQuellDto(this.kundeQuellDto);
        setSaveButtonStatus();
      }
    }

    if (e.getActionCommand().equals(ACTION_DATENRICHTUNG_WAEHLEN)) {
      // ToDo: Pfeil nach links, Pfeil nach rechts oder Kombinationsringe (unendlich zeichen)
      if (e.getSource() instanceof WrapperButtonZusammenfuehren) {
        ( (WrapperButtonZusammenfuehren) e.getSource()).setNextSelection();
        setzeFarbenVonTextfeldern(this.vWrapperButtonsZusammenfuehren, this.vZielfelder, this.vQuellfelder, false);
      }
    }

    if (e.getActionCommand().equals(ACTION_ALLE_DATENRICHTUNGEN_WAEHLEN)) {
      // ToDo: Pfeil nach links, Pfeil nach rechts oder Kombinationsringe (unendlich zeichen)
      if (e.getSource() instanceof WrapperButtonZusammenfuehren) {
        ( (WrapperButtonZusammenfuehren) e.getSource()).setNextSelection();
        //hier alle weiteren zusammenfuehrenButtons auf die oben gesetzte nextSelection setzen
        System.out.println("ButtonVectorSize: " + vWrapperButtonsZusammenfuehren.size());
        int iAktSel = ( (WrapperButtonZusammenfuehren) e.getSource()).getISelection();
        for (Enumeration<WrapperButtonZusammenfuehren> el = vWrapperButtonsZusammenfuehren.elements();
             el.hasMoreElements(); ) {
          ( (WrapperButtonZusammenfuehren) el.nextElement()).setISelection(iAktSel);
        }
        setzeFarbenVonTextfeldern(this.vWrapperButtonsZusammenfuehren, this.vZielfelder, this.vQuellfelder, false);
      }
    }

  }

  /**
   * Schreibt die selektierten Kundendaten in die zielkundenDto
   * Einige Dateninhalte wie z.B. Anrede, Ort, etc... sind nicht kombinierbar
   * einige wie Kommentar und Hinweis sind kombinierbar
   * @throws Throwable
   */
  private void schreibeRichtungInZielKunde()
      throws Throwable {
    /* alle buttons vergleichen:
         wenn der btn nach li zeigt: nix
         wenn der button nach rechts zeigt & aktiv ist: schreibe diese daten von quelle in ziel
         wenn der button kombiniert zeigt & aktiv ist: fuege daten von quelle zu ziel hinzu (evtl. laenge pruefen)
     */
    String sTmp = "";

    if (wbuPartnernameDatenrichtung.isEnabled()) {
      if (wbuPartnernameDatenrichtung.getISelection() ==  WrapperButtonZusammenfuehren.SELECT_RECHTEN_DATENINHALT) {
        this.iKundePartnerIId = this.kundeQuellDto.getPartnerIId();
      }
      else if (wbuPartnernameDatenrichtung.getISelection() ==  WrapperButtonZusammenfuehren.SELECT_LINKEN_DATENINHALT) {
        this.iKundePartnerIId = null;
      }
    }

    if (wbuMwstDatenrichtung.isEnabled()) {
      if (wbuMwstDatenrichtung.getISelection() == WrapperButtonZusammenfuehren.SELECT_RECHTEN_DATENINHALT) {
        this.kundeZielDto.setMwstsatzbezIId(this.kundeQuellDto.getMwstsatzbezIId());
      }
    }

    if (wbuWaehrungDatenrichtung.isEnabled()) {
      if (wbuWaehrungDatenrichtung.getISelection() == WrapperButtonZusammenfuehren.SELECT_RECHTEN_DATENINHALT) {
        this.kundeZielDto.setCWaehrung(this.kundeQuellDto.getCWaehrung());
      }
    }

    if (wbuLieferartDatenrichtung.isEnabled()) {
      if (wbuLieferartDatenrichtung.getISelection() == WrapperButtonZusammenfuehren.SELECT_RECHTEN_DATENINHALT) {
        this.kundeZielDto.setLieferartIId(this.kundeQuellDto.getLieferartIId());
      }
    }

    if (wbuSpediteurDatenrichtung.isEnabled()) {
      if (wbuSpediteurDatenrichtung.getISelection() == WrapperButtonZusammenfuehren.SELECT_RECHTEN_DATENINHALT) {
        this.kundeZielDto.setSpediteurIId(this.kundeQuellDto.getSpediteurIId());
      }
    }

    if (wbuZahlungszielDatenrichtung.isEnabled()) {
      if (wbuZahlungszielDatenrichtung.getISelection() == WrapperButtonZusammenfuehren.SELECT_RECHTEN_DATENINHALT) {
        this.kundeZielDto.setZahlungszielIId(this.kundeQuellDto.getZahlungszielIId());
      }
    }

    if (wbuKostenstelleDatenrichtung.isEnabled()) {
      if (wbuKostenstelleDatenrichtung.getISelection() == WrapperButtonZusammenfuehren.SELECT_RECHTEN_DATENINHALT) {
        this.kundeZielDto.setKostenstelleIId(this.kundeQuellDto.getKostenstelleIId());
      }
    }

    if (wbuProvisionsempfaengerDatenrichtung.isEnabled()) {
      if (wbuProvisionsempfaengerDatenrichtung.getISelection() == WrapperButtonZusammenfuehren.SELECT_RECHTEN_DATENINHALT) {
        this.kundeZielDto.setPersonaliIdProvisionsempfaenger(this.kundeQuellDto.getPersonaliIdProvisionsempfaenger());
      }
    }

    if (wbuRabattsatzDatenrichtung.isEnabled()) {
      if (wbuRabattsatzDatenrichtung.getISelection() == WrapperButtonZusammenfuehren.SELECT_RECHTEN_DATENINHALT) {
        this.kundeZielDto.setFRabattsatz(this.kundeQuellDto.getFRabattsatz());
      }
    }

    if (wbuGarantieDatenrichtung.isEnabled()) {
      if (wbuGarantieDatenrichtung.getISelection() == WrapperButtonZusammenfuehren.SELECT_RECHTEN_DATENINHALT) {
        this.kundeZielDto.setIGarantieinmonaten(this.kundeQuellDto.getIGarantieinmonaten());
      }
    }

    if (wbuGarantieDatenrichtung.isEnabled()) {
      if (wbuGarantieDatenrichtung.getISelection() == WrapperButtonZusammenfuehren.SELECT_RECHTEN_DATENINHALT) {
        this.kundeZielDto.setIGarantieinmonaten(this.kundeQuellDto.getIGarantieinmonaten());
      }
    }

    if (wbuKommentarDatenrichtung.isEnabled()) {
      if (wbuKommentarDatenrichtung.getISelection() == WrapperButtonZusammenfuehren.SELECT_RECHTEN_DATENINHALT) {
        this.kundeZielDto.setXKommentar(this.kundeQuellDto.getXKommentar());
      }
      else if (wbuKommentarDatenrichtung.getISelection() == WrapperButtonZusammenfuehren.SELECT_KOMBINIERTE_DATENINHALTE) {
        String sTmpText = this.kundeZielDto.getXKommentar() + " " + this.kundeQuellDto.getXKommentar();
        if (sTmpText.length() > MAX_KUNDENKOMMENTAR) {
          sTmpText = sTmpText.substring(0, MAX_KUNDENKOMMENTAR);
        }
        this.kundeZielDto.setXKommentar(sTmpText);
      }
    }

    if (wbuKurznrDatenrichtung.isEnabled()) {
     if (wbuKurznrDatenrichtung.getISelection() == WrapperButtonZusammenfuehren.SELECT_RECHTEN_DATENINHALT) {
       this.kundeZielDto.setCKurznr(this.kundeQuellDto.getCKurznr());
     }
    }

    if (wbuKreditlimitDatenrichtung.isEnabled()) {
      if (wbuKreditlimitDatenrichtung.getISelection() == WrapperButtonZusammenfuehren.SELECT_RECHTEN_DATENINHALT) {
        this.kundeZielDto.setNKreditlimit(this.kundeQuellDto.getNKreditlimit());
      }
    }

    if (wbuBonitaetspruefungDatenrichtung.isEnabled()) {
      if (wbuBonitaetspruefungDatenrichtung.getISelection() == WrapperButtonZusammenfuehren.SELECT_RECHTEN_DATENINHALT) {
        this.kundeZielDto.setTBonitaet(this.kundeQuellDto.getTBonitaet());
      }
    }

    if (wbuLiefersperreDatenrichtung.isEnabled()) {
      if (wbuLiefersperreDatenrichtung.getISelection() == WrapperButtonZusammenfuehren.SELECT_RECHTEN_DATENINHALT) {
        this.kundeZielDto.setTLiefersperream(this.kundeQuellDto.getTLiefersperream());
      }
    }

    if (wbuRechnungskopienDatenrichtung.isEnabled()) {
      if (wbuRechnungskopienDatenrichtung.getISelection() == WrapperButtonZusammenfuehren.SELECT_RECHTEN_DATENINHALT) {
        this.kundeZielDto.setIDefaultrekopiendrucken(this.kundeQuellDto.getIDefaultrekopiendrucken());
      }
    }

    if (wbuLieferscheinkopienDatenrichtung.isEnabled()) {
      if (wbuLieferscheinkopienDatenrichtung.getISelection() == WrapperButtonZusammenfuehren.SELECT_RECHTEN_DATENINHALT) {
        this.kundeZielDto.setIDefaultlskopiendrucken(this.kundeQuellDto.getIDefaultlskopiendrucken());
      }
    }

    if (wbuMitarbeiterDatenrichtung.isEnabled()) {
      if (wbuMitarbeiterDatenrichtung.getISelection() == WrapperButtonZusammenfuehren.SELECT_RECHTEN_DATENINHALT) {
        this.kundeZielDto.setIMitarbeiteranzahl(this.kundeQuellDto.getIMitarbeiteranzahl());
      }
    }

    if (wbuTourDatenrichtung.isEnabled()) {
      if (wbuTourDatenrichtung.getISelection() == WrapperButtonZusammenfuehren.SELECT_RECHTEN_DATENINHALT) {
        this.kundeZielDto.setCTour(this.kundeQuellDto.getCTour());
      }
    }

    if (wbuLieferantennrDatenrichtung.isEnabled()) {
      if (wbuLieferantennrDatenrichtung.getISelection() == WrapperButtonZusammenfuehren.SELECT_RECHTEN_DATENINHALT) {
        this.kundeZielDto.setCLieferantennr(this.kundeQuellDto.getCLieferantennr());
      }
    }

    if (wbuAbcDatenrichtung.isEnabled()) {
      if (wbuAbcDatenrichtung.getISelection() == WrapperButtonZusammenfuehren.SELECT_RECHTEN_DATENINHALT) {
        this.kundeZielDto.setCAbc(this.kundeQuellDto.getCAbc());
      }
    }

    if (wbuAgbDatenrichtung.isEnabled()) {
      if (wbuAgbDatenrichtung.getISelection() == WrapperButtonZusammenfuehren.SELECT_RECHTEN_DATENINHALT) {
        this.kundeZielDto.setTAgbuebermittelung(this.kundeQuellDto.getTAgbuebermittelung());
      }
    }

    if (wbuFremdsystemnrDatenrichtung.isEnabled()) {
      if (wbuFremdsystemnrDatenrichtung.getISelection() == WrapperButtonZusammenfuehren.SELECT_RECHTEN_DATENINHALT) {
        this.kundeZielDto.setCFremdsystemnr(this.kundeQuellDto.getCFremdsystemnr());
      }
    }

    if (wbuErloeskontoDatenrichtung.isEnabled()) {
      if (wbuErloeskontoDatenrichtung.getISelection() == WrapperButtonZusammenfuehren.SELECT_RECHTEN_DATENINHALT) {
        this.kundeZielDto.setIidErloeseKonto(this.kundeQuellDto.getIidErloeseKonto());
      }
    }

    if (wbuDebitorenkontoDatenrichtung.isEnabled()) {
      if (wbuDebitorenkontoDatenrichtung.getISelection() == WrapperButtonZusammenfuehren.SELECT_RECHTEN_DATENINHALT) {
        this.kundeZielDto.setIidDebitorenkonto(this.kundeQuellDto.getIidDebitorenkonto());
      }
    }

    if (wbuRechnungsadresspartnerDatenrichtung.isEnabled()) {
      if (wbuRechnungsadresspartnerDatenrichtung.getISelection() == WrapperButtonZusammenfuehren.SELECT_RECHTEN_DATENINHALT) {
        this.kundeZielDto.setPartnerIIdRechnungsadresse(this.kundeQuellDto.getPartnerIIdRechnungsadresse());
      }
    }

    if (wbuHinweisIntDatenrichtung.isEnabled()) {
      if (wbuHinweisIntDatenrichtung.getISelection() == WrapperButtonZusammenfuehren.SELECT_RECHTEN_DATENINHALT) {
        this.kundeZielDto.setSHinweisintern(this.kundeQuellDto.getSHinweisintern());
      }
      else if (wbuHinweisIntDatenrichtung.getISelection() == WrapperButtonZusammenfuehren.SELECT_KOMBINIERTE_DATENINHALTE) {
        String sTmpText = this.kundeZielDto.getSHinweisintern() + " " + this.kundeQuellDto.getSHinweisintern();
        if (sTmpText.length() > MAX_HINWEISINTERN) {
          sTmpText = sTmpText.substring(0, MAX_HINWEISINTERN);
        }
        this.kundeZielDto.setSHinweisintern(sTmpText);
      }
    }

    if (wbuHinweisExtDatenrichtung.isEnabled()) {
          if (wbuHinweisExtDatenrichtung.getISelection() == WrapperButtonZusammenfuehren.SELECT_RECHTEN_DATENINHALT) {
            this.kundeZielDto.setSHinweisextern(this.kundeQuellDto.getSHinweisextern());
          }
          else if (wbuHinweisExtDatenrichtung.getISelection() == WrapperButtonZusammenfuehren.SELECT_KOMBINIERTE_DATENINHALTE) {
            String sTmpText = this.kundeZielDto.getSHinweisextern() + " " + this.kundeQuellDto.getSHinweisextern();
            if (sTmpText.length() > MAX_HINWEISEXTERN) {
              sTmpText = sTmpText.substring(0, MAX_HINWEISEXTERN);
            }
            this.kundeZielDto.setSHinweisextern(sTmpText);
          }
    }

    if (wbuZessionsfaktorDatenrichtung.isEnabled()) {
      if (wbuZessionsfaktorDatenrichtung.getISelection() == WrapperButtonZusammenfuehren.SELECT_RECHTEN_DATENINHALT) {
        this.kundeZielDto.setFZessionsfaktor(this.kundeQuellDto.getFZessionsfaktor());
      }
    }

    if (wbuPreislisteDatenrichtung.isEnabled()) {
      if (wbuPreislisteDatenrichtung.getISelection() == WrapperButtonZusammenfuehren.SELECT_RECHTEN_DATENINHALT) {
        this.kundeZielDto.setVkpfArtikelpreislisteIIdStdpreisliste(this.kundeQuellDto.getVkpfArtikelpreislisteIIdStdpreisliste());
      }
    }

    if (wbuKundennrDatenrichtung.isEnabled()) {
      if (wbuKundennrDatenrichtung.getISelection() == WrapperButtonZusammenfuehren.SELECT_RECHTEN_DATENINHALT) {
        this.kundeZielDto.setIKundennummer(this.kundeQuellDto.getIKundennummer());
      }
    }

    if (wbuKundensokoDatenrichtung.isEnabled()) {
      if (wbuKundensokoDatenrichtung.getISelection() == WrapperButtonZusammenfuehren.SELECT_RECHTEN_DATENINHALT) {
        iKundensokoSelectedKundeIId = kundeQuellDto.getIId();
      }
      else {
        iKundensokoSelectedKundeIId = kundeZielDto.getIId();
      }
    }
    else {
      iKundensokoSelectedKundeIId = kundeZielDto.getIId();
    }

    if (wbuMindermengenzuschlagDatenrichtung.isEnabled()) {
      if (wbuMindermengenzuschlagDatenrichtung.getISelection() == WrapperButtonZusammenfuehren.SELECT_RECHTEN_DATENINHALT) {
        this.kundeZielDto.setBMindermengenzuschlag(this.kundeQuellDto.getBMindermengenzuschlag());
      }
    }

    if (wbuMonatsrechnungDatenrichtung.isEnabled()) {
      if (wbuMonatsrechnungDatenrichtung.getISelection() == WrapperButtonZusammenfuehren.SELECT_RECHTEN_DATENINHALT) {
        this.kundeZielDto.setBMonatsrechnung(this.kundeQuellDto.getBMonatsrechnung());
      }
    }

    if (wbuSammelrechnungDatenrichtung.isEnabled()) {
      if (wbuSammelrechnungDatenrichtung.getISelection() == WrapperButtonZusammenfuehren.SELECT_RECHTEN_DATENINHALT) {
        this.kundeZielDto.setBSammelrechnung(this.kundeQuellDto.getBSammelrechnung());
      }
    }

    if (wbuRechnungsempfaengerDatenrichtung.isEnabled()) {
      if (wbuRechnungsempfaengerDatenrichtung.getISelection() == WrapperButtonZusammenfuehren.SELECT_RECHTEN_DATENINHALT) {
        this.kundeZielDto.setBIstreempfaenger(this.kundeQuellDto.getBIstreempfaenger());
      }
    }

    if (wbuPreiseAmLsDatenrichtung.isEnabled()) {
      if (wbuPreiseAmLsDatenrichtung.getISelection() == WrapperButtonZusammenfuehren.SELECT_RECHTEN_DATENINHALT) {
        this.kundeZielDto.setBPreiseanlsandrucken(this.kundeQuellDto.getBPreiseanlsandrucken());
      }
    }

    if (wbuBelegdruckMitRabattDatenrichtung.isEnabled()) {
      if (wbuBelegdruckMitRabattDatenrichtung.getISelection() == WrapperButtonZusammenfuehren.SELECT_RECHTEN_DATENINHALT) {
        this.kundeZielDto.setBRechnungsdruckmitrabatt(this.kundeQuellDto.getBRechnungsdruckmitrabatt());
      }
    }

    if (wbuDistributorDatenrichtung.isEnabled()) {
      if (wbuDistributorDatenrichtung.getISelection() == WrapperButtonZusammenfuehren.SELECT_RECHTEN_DATENINHALT) {
        this.kundeZielDto.setBDistributor(this.kundeQuellDto.getBDistributor());
      }
    }

    if (wbuTeillieferungenDatenrichtung.isEnabled()) {
      if (wbuTeillieferungenDatenrichtung.getISelection() == WrapperButtonZusammenfuehren.SELECT_RECHTEN_DATENINHALT) {
        this.kundeZielDto.setBAkzeptiertteillieferung(this.kundeQuellDto.getBAkzeptiertteillieferung());
      }
    }

    if (wbuLsgewichtDatenrichtung.isEnabled()) {
      if (wbuLsgewichtDatenrichtung.getISelection() == WrapperButtonZusammenfuehren.SELECT_RECHTEN_DATENINHALT) {
        this.kundeZielDto.setBLsgewichtangeben(this.kundeQuellDto.getBLsgewichtangeben());
      }
    }

    if (wbuInteressentDatenrichtung.isEnabled()) {
      if (wbuInteressentDatenrichtung.getISelection() == WrapperButtonZusammenfuehren.SELECT_RECHTEN_DATENINHALT) {
        this.kundeZielDto.setbIstinteressent(this.kundeQuellDto.getbIstinteressent());
      }
    }

    if (wbuVerstecktDatenrichtung.isEnabled()) {
      if (wbuVerstecktDatenrichtung.getISelection() == WrapperButtonZusammenfuehren.SELECT_RECHTEN_DATENINHALT) {
        this.kundeZielDto.setBVersteckterlieferant(this.kundeQuellDto.getBVersteckterlieferant());
      }
    }

    if (wbuReversechargeDatenrichtung.isEnabled()) {
      if (wbuReversechargeDatenrichtung.getISelection() == WrapperButtonZusammenfuehren.SELECT_RECHTEN_DATENINHALT) {
        this.kundeZielDto.setBReversecharge(this.kundeQuellDto.getBReversecharge());
      }
    }

  }


  public boolean handleOwnException(ExceptionLP exfc) {
    boolean bErrorErkannt = true;
    int code = exfc.getICode();
    new DialogError(LPMain.getInstance().getDesktop(), exfc, DialogError.TYPE_INFORMATION);
    return bErrorErkannt;
  }

}
