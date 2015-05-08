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


import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.math.BigDecimal;
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
import com.lp.server.finanz.service.KontoDto;
import com.lp.server.finanz.service.KontoDtoSmall;
import com.lp.server.partner.service.LieferantDto;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.system.service.KostenstelleDto;
import com.lp.server.system.service.SpediteurDto;
import com.lp.server.system.service.ZahlungszielDto;
import com.lp.util.Helper;

@SuppressWarnings("static-access")
/**
 * <p> Diese Klasse kuemmert sich um das Zusammenf&uuml;hren von Lieferanten</p>
 *
 * <p>Copyright Logistik Pur Software GmbH (c) 2004, 2005, 2006</p>
 *
 * <p>Erstellung: Vorname Nachname; 08.05.08</p>
 *
 * <p>@author $Author: christian $</p>
 *
 * @version not attributable Date $Date: 2012/09/14 08:09:46 $
 */
public class PanelDialogLieferantenZusammenfuehren
    extends PanelDialog {

  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

public static final String ACTION_SPECIAL_SAVE = "action_" + ALWAYSENABLED + "save";

  private final String ACTION_QUELLLIEFERANT_AUSWAEHLEN = "ACTION_QUELLLIEFERANT_AUSWAEHLEN";
  private final String ACTION_ZIELLIEFERANT_AUSWAEHLEN = "ACTION_ZIELLIEFERANT_AUSWAEHLEN";

  private final String ACTION_ALLE_DATENRICHTUNGEN_WAEHLEN = "ACTION_ALLE_DATENRICHTUNGEN_WAEHLEN";
  private final String ACTION_DATENRICHTUNG_WAEHLEN = "ACTION_DATENRICHTUNG_WAEHLEN";

  private final int MAX_KOMMENTAR = 3000;

  // mind. alle Texte, die nvarchar > 40 haben, hier definieren bzw. Felder, deren Inhalt kombiniert werden kann
  private final int MAX_HINWEISEXTERN = 80;
  private final int MAX_HINWEISINTERN = 80;

  private JButton saveButton = null;

  // in diesen vector schreiben wir die wrapperbuttonzusammenfuehren, die lieferantendetails beinhalten
  private Vector<WrapperButtonZusammenfuehren> vWrapperButtonsZusammenfuehren = new Vector<WrapperButtonZusammenfuehren> ();
  private Vector<JComponent> vZielfelder = new Vector<JComponent>();
  private Vector<JComponent> vQuellfelder = new Vector<JComponent>();

  private LieferantDto lieferantZielDto = null;
  private LieferantDto lieferantQuellDto = null;

  // soll der Partner des rechten Lieferanten genommen werden, so steht diese PartnerIId in iLieferantPartnerIId
  private Integer iLieferantPartnerIId = null;

  private PanelQueryFLR panelQueryLieferantFlr = null;


  protected boolean bHandleEventInSuperklasse = true;

  protected JPanel jpaWorkingOnScroll = null;
  private JScrollPane jScrollPaneLieferant = null;

  private WrapperButton wbuZielLieferantAuswaehlen = null;
  private WrapperButton wbuQuellLieferantAuswaehlen = null;

  private WrapperButtonZusammenfuehren wbuAuswahlReset = null;

  // Partnername
  private WrapperLabel wlaPartnername = null;
  private WrapperButtonZusammenfuehren wbuPartnernameDatenrichtung = null;
  private WrapperTextFieldZusammenfuehren wtfZielLieferantPartnername = null;
  private WrapperTextFieldZusammenfuehren wtfQuellLieferantPartnername = null;

  // Mehrwertsteuer
  private WrapperLabel wlaMwst = null;
  private WrapperButtonZusammenfuehren wbuMwstDatenrichtung = null;
  private WrapperTextFieldZusammenfuehren wtfZielLieferantMwst = null;
  private WrapperTextFieldZusammenfuehren wtfQuellLieferantMwst = null;

  // Kundennr
  private WrapperLabel wlaKundennr = null;
  private WrapperButtonZusammenfuehren wbuKundennrDatenrichtung = null;
  private WrapperTextFieldZusammenfuehren wtfZielLieferantKundennr = null;
  private WrapperTextFieldZusammenfuehren wtfQuellLieferantKundennr = null;

  // MoeglicherLieferant
  private WrapperLabel wlaMoeglLieferant = null;
  private WrapperButtonZusammenfuehren wbuMoeglLieferantDatenrichtung = null;
  private WrapperCheckBoxZusammenfuehren wcbZielLieferantMoeglLieferant = null;
  private WrapperCheckBoxZusammenfuehren wcbQuellLieferantMoeglLieferant = null;

  // Waehrung
  private WrapperLabel wlaWaehrung = null;
  private WrapperButtonZusammenfuehren wbuWaehrungDatenrichtung = null;
  private WrapperTextFieldZusammenfuehren wtfZielLieferantWaehrung = null;
  private WrapperTextFieldZusammenfuehren wtfQuellLieferantWaehrung = null;

  // Kostenstelle
  private WrapperLabel wlaKostenstelle = null;
  private WrapperButtonZusammenfuehren wbuKostenstelleDatenrichtung = null;
  private WrapperTextFieldZusammenfuehren wtfZielLieferantKostenstelle = null;
  private WrapperTextFieldZusammenfuehren wtfQuellLieferantKostenstelle = null;

  // Warenkonto
  private WrapperLabel wlaWarenkonto = null;
  private WrapperButtonZusammenfuehren wbuWarenkontoDatenrichtung = null;
  private WrapperTextFieldZusammenfuehren wtfZielLieferantWarenkonto = null;
  private WrapperTextFieldZusammenfuehren wtfQuellLieferantWarenkonto = null;

  // Kreditorenkonto
  private WrapperLabel wlaKreditorenkonto = null;
  private WrapperButtonZusammenfuehren wbuKreditorenkontoDatenrichtung = null;
  private WrapperTextFieldZusammenfuehren wtfZielLieferantKreditorenkonto = null;
  private WrapperTextFieldZusammenfuehren wtfQuellLieferantKreditorenkonto = null;

  // Beurteilen
  private WrapperLabel wlaBeurteilen = null;
  private WrapperButtonZusammenfuehren wbuBeurteilenDatenrichtung = null;
  private WrapperCheckBoxZusammenfuehren wcbZielLieferantBeurteilen = null;
  private WrapperCheckBoxZusammenfuehren wcbQuellLieferantBeurteilen = null;

  // Beurteilung
  private WrapperLabel wlaBeurteilung = null;
  private WrapperButtonZusammenfuehren wbuBeurteilungDatenrichtung = null;
  private WrapperTextFieldZusammenfuehren wtfZielLieferantBeurteilung = null;
  private WrapperTextFieldZusammenfuehren wtfQuellLieferantBeurteilung = null;

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

  // XKommentar
  private WrapperLabel wlaKommentar = null;
  private WrapperButtonZusammenfuehren wbuKommentarDatenrichtung = null;
  private WrapperTextAreaZusammenfuehren wtaZielLieferantKommentar = null;
  private WrapperTextAreaZusammenfuehren wtaQuellLieferantKommentar = null;
  private JScrollPane jScrollPaneZielLieferantKommentar = null;
  private JScrollPane jScrollPaneQuellLieferantKommentar = null;

  // Zahlungsziel
  private WrapperLabel wlaZahlungsziel = null;
  private WrapperButtonZusammenfuehren wbuZahlungszielDatenrichtung = null;
  private WrapperTextFieldZusammenfuehren wtfZielLieferantZahlungsziel = null;
  private WrapperTextFieldZusammenfuehren wtfQuellLieferantZahlungsziel = null;

  // Lieferart
  private WrapperLabel wlaLieferart = null;
  private WrapperButtonZusammenfuehren wbuLieferartDatenrichtung = null;
  private WrapperTextFieldZusammenfuehren wtfZielLieferantLieferart = null;
  private WrapperTextFieldZusammenfuehren wtfQuellLieferantLieferart = null;

  // Spediteur
  private WrapperLabel wlaSpediteur = null;
  private WrapperButtonZusammenfuehren wbuSpediteurDatenrichtung = null;
  private WrapperTextFieldZusammenfuehren wtfZielLieferantSpediteur = null;
  private WrapperTextFieldZusammenfuehren wtfQuellLieferantSpediteur = null;

  // Rabatt
  private WrapperLabel wlaRabatt = null;
  private WrapperButtonZusammenfuehren wbuRabattDatenrichtung = null;
  private WrapperTextFieldZusammenfuehren wtfZielLieferantRabatt = null;
  private WrapperTextFieldZusammenfuehren wtfQuellLieferantRabatt = null;

  // Jahresbonus
  private WrapperLabel wlaJahresbonus = null;
  private WrapperButtonZusammenfuehren wbuJahresbonusDatenrichtung = null;
  private WrapperTextFieldZusammenfuehren wtfZielLieferantJahresbonus = null;
  private WrapperTextFieldZusammenfuehren wtfQuellLieferantJahresbonus = null;

  // Kreditlimit
  private WrapperLabel wlaKreditlimit = null;
  private WrapperButtonZusammenfuehren wbuKreditlimitDatenrichtung = null;
  private WrapperTextFieldZusammenfuehren wtfZielLieferantKreditlimit = null;
  private WrapperTextFieldZusammenfuehren wtfQuellLieferantKreditlimit = null;

  // Umsatzgrenze
  private WrapperLabel wlaUmsatzgrenze = null;
  private WrapperButtonZusammenfuehren wbuUmsatzgrenzeDatenrichtung = null;
  private WrapperTextFieldZusammenfuehren wtfZielLieferantUmsatzgrenze = null;
  private WrapperTextFieldZusammenfuehren wtfQuellLieferantUmsatzgrenze = null;

  // Mindestbestellwert
  private WrapperLabel wlaMindestbestellwert = null;
  private WrapperButtonZusammenfuehren wbuMindestbestellwertDatenrichtung = null;
  private WrapperTextFieldZusammenfuehren wtfZielLieferantMindestbestellwert = null;
  private WrapperTextFieldZusammenfuehren wtfQuellLieferantMindestbestellwert = null;

  // Mindermengenzuschlagbis
  private WrapperLabel wlaMindermengenzuschlagbis = null;
  private WrapperButtonZusammenfuehren wbuMindermengenzuschlagbisDatenrichtung = null;
  private WrapperTextFieldZusammenfuehren wtfZielLieferantMindermengenzuschlagbis = null;
  private WrapperTextFieldZusammenfuehren wtfQuellLieferantMindermengenzuschlagbis = null;

  // Rechnungsadresspartner
  private WrapperLabel wlaRechnungsadresspartner = null;
  private WrapperButtonZusammenfuehren wbuRechnungsadresspartnerDatenrichtung = null;
  private WrapperTextFieldZusammenfuehren wtfZielLieferantRechnungsadresspartner = null;
  private WrapperTextFieldZusammenfuehren wtfQuellLieferantRechnungsadresspartner = null;


  public PanelDialogLieferantenZusammenfuehren(LieferantDto lieferantZielDto,
                                          InternalFrame internalFrame,
                                          String add2TitleI)
      throws Throwable {

    super(internalFrame,
          add2TitleI + " | " + lieferantZielDto.getPartnerDto().formatName());

    this.lieferantZielDto = lieferantZielDto;
    jbInit(); // zuerst QuellFelder leer, erst nach Quellauswahl Werte drin
    initComponents();
    setSaveButtonStatus();
  }

  /**
   * setzt den Button fuer das endgueltige zusammenfuehren aktiv oder inaktiv, je nach Vorhandensein des
   * linken und rechten Lieferanten
   * @throws Throwable
   */
  private void setSaveButtonStatus()
      throws Throwable {
    if (this.lieferantQuellDto != null && this.lieferantZielDto != null) {
      this.saveButton.setEnabled(true);
    }
    else {
      this.saveButton.setEnabled(false);
    }
  }


  private void jbInit()
      throws Throwable {

    jScrollPaneLieferant = new javax.swing.JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
        JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

    jpaWorkingOnScroll = new JPanel();
    jpaWorkingOnScroll.setLayout(new GridBagLayout());


    // Ziellieferant-Auswahlbutton
    wbuZielLieferantAuswaehlen = new WrapperButtonZusammenfuehren();
    wbuZielLieferantAuswaehlen.setText(LPMain.getInstance().getTextRespectUISPr(
        "lp.lieferant") + "...");
    wbuZielLieferantAuswaehlen.setToolTipText(LPMain.getInstance().getTextRespectUISPr(
        "button.lieferant.tooltip"));
    wbuZielLieferantAuswaehlen.addActionListener(this);
    wbuZielLieferantAuswaehlen.setActionCommand(ACTION_ZIELLIEFERANT_AUSWAEHLEN);
    wbuZielLieferantAuswaehlen.setMaximumSize(new Dimension(125,
        Defaults.getInstance().getControlHeight()));
    wbuZielLieferantAuswaehlen.setPreferredSize(new Dimension(115,
        Defaults.getInstance().getControlHeight()));
    wbuZielLieferantAuswaehlen.setMinimumSize(new Dimension(110,
        Defaults.getInstance().getControlHeight()));
    wbuZielLieferantAuswaehlen.setFocusable(true);


    // Quelllieferant-Auswahlbutton
    wbuQuellLieferantAuswaehlen = new WrapperButtonZusammenfuehren();
    wbuQuellLieferantAuswaehlen.setText(LPMain.getInstance().getTextRespectUISPr(
        "lp.lieferant") + "...");
    wbuQuellLieferantAuswaehlen.setToolTipText(LPMain.getInstance().getTextRespectUISPr(
        "button.lieferant.tooltip"));
    wbuQuellLieferantAuswaehlen.addActionListener(this);
    wbuQuellLieferantAuswaehlen.setActionCommand(ACTION_QUELLLIEFERANT_AUSWAEHLEN);
    wbuQuellLieferantAuswaehlen.setMaximumSize(new Dimension(125,
        Defaults.getInstance().getControlHeight()));
    wbuQuellLieferantAuswaehlen.setPreferredSize(new Dimension(115,
        Defaults.getInstance().getControlHeight()));
    wbuQuellLieferantAuswaehlen.setMinimumSize(new Dimension(110,
        Defaults.getInstance().getControlHeight()));
    wbuQuellLieferantAuswaehlen.setFocusable(true);


    // AuswahlReset der Datenrichtung
    wbuAuswahlReset = new WrapperButtonZusammenfuehren();
    wbuAuswahlReset.setButtonIsAlle(true);
    wbuAuswahlReset.addActionListener(this);
    wbuAuswahlReset.setActionCommand(ACTION_ALLE_DATENRICHTUNGEN_WAEHLEN);


    // Partnername
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

    wtfZielLieferantPartnername = new WrapperTextFieldZusammenfuehren();
    wtfZielLieferantPartnername.setText("");
    wtfZielLieferantPartnername.setColumnsMax(80);
    wtfZielLieferantPartnername.setMaximumSize(new Dimension(180,
        Defaults.getInstance().getControlHeight()));
    wtfZielLieferantPartnername.setPreferredSize(new Dimension(150,
        Defaults.getInstance().getControlHeight()));
    wtfZielLieferantPartnername.setMinimumSize(new Dimension(150,
        Defaults.getInstance().getControlHeight()));
    wtfZielLieferantPartnername.setActivatable(false);
    vZielfelder.addElement(wtfZielLieferantPartnername);

    wtfQuellLieferantPartnername = new WrapperTextFieldZusammenfuehren();
    wtfQuellLieferantPartnername.setText("");
    wtfQuellLieferantPartnername.setColumnsMax(80);
    wtfQuellLieferantPartnername.setMaximumSize(new Dimension(180,
        Defaults.getInstance().getControlHeight()));
    wtfQuellLieferantPartnername.setPreferredSize(new Dimension(150,
        Defaults.getInstance().getControlHeight()));
    wtfQuellLieferantPartnername.setMinimumSize(new Dimension(150,
        Defaults.getInstance().getControlHeight()));
    wtfQuellLieferantPartnername.setActivatable(false);
    vQuellfelder.addElement(wtfQuellLieferantPartnername);


    // Mwst
    wlaMwst = new WrapperLabel();
    wlaMwst.setText(LPMain.getInstance().getTextRespectUISPr("lp.mwst") + ": ");
    wlaMwst.setMaximumSize(new Dimension(115, Defaults.getInstance().getControlHeight()));
    wlaMwst.setPreferredSize(new Dimension(100, Defaults.getInstance().getControlHeight()));
    wlaMwst.setMinimumSize(new Dimension(100, Defaults.getInstance().getControlHeight()));

    wbuMwstDatenrichtung = new WrapperButtonZusammenfuehren();
    wbuMwstDatenrichtung.addActionListener(this);
    wbuMwstDatenrichtung.setActionCommand(ACTION_DATENRICHTUNG_WAEHLEN);
    vWrapperButtonsZusammenfuehren.addElement(wbuMwstDatenrichtung);

    wtfZielLieferantMwst = new WrapperTextFieldZusammenfuehren();
    wtfZielLieferantMwst.setText("");
    wtfZielLieferantMwst.setMaximumSize(new Dimension(180,
                                                  Defaults.getInstance().getControlHeight()));
    wtfZielLieferantMwst.setPreferredSize(new Dimension(150,
        Defaults.getInstance().getControlHeight()));
    wtfZielLieferantMwst.setMinimumSize(new Dimension(150,
                                                  Defaults.getInstance().getControlHeight()));
    wtfZielLieferantMwst.setActivatable(false);
    vZielfelder.addElement(wtfZielLieferantMwst);

    wtfQuellLieferantMwst = new WrapperTextFieldZusammenfuehren();
    wtfQuellLieferantMwst.setText("");
    wtfQuellLieferantMwst.setMaximumSize(new Dimension(180,
        Defaults.getInstance().getControlHeight()));
    wtfQuellLieferantMwst.setPreferredSize(new Dimension(150,
        Defaults.getInstance().getControlHeight()));
    wtfQuellLieferantMwst.setMinimumSize(new Dimension(150,
        Defaults.getInstance().getControlHeight()));
    wtfQuellLieferantMwst.setActivatable(false);
    vQuellfelder.addElement(wtfQuellLieferantMwst);


    // Kundennr
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

    wtfZielLieferantKundennr = new WrapperTextFieldZusammenfuehren();
    wtfZielLieferantKundennr.setText("");
    wtfZielLieferantKundennr.setMaximumSize(new Dimension(180,
        Defaults.getInstance().getControlHeight()));
    wtfZielLieferantKundennr.setPreferredSize(new Dimension(150,
        Defaults.getInstance().getControlHeight()));
    wtfZielLieferantKundennr.setMinimumSize(new Dimension(150,
        Defaults.getInstance().getControlHeight()));
    wtfZielLieferantKundennr.setActivatable(false);
    vZielfelder.addElement(wtfZielLieferantKundennr);

    wtfQuellLieferantKundennr = new WrapperTextFieldZusammenfuehren();
    wtfQuellLieferantKundennr.setText("");
    wtfQuellLieferantKundennr.setMaximumSize(new Dimension(180,
        Defaults.getInstance().getControlHeight()));
    wtfQuellLieferantKundennr.setPreferredSize(new Dimension(150,
        Defaults.getInstance().getControlHeight()));
    wtfQuellLieferantKundennr.setMinimumSize(new Dimension(150,
        Defaults.getInstance().getControlHeight()));
    wtfQuellLieferantKundennr.setActivatable(false);
    vQuellfelder.addElement(wtfQuellLieferantKundennr);


    // moeglicher Lieferant
    wlaMoeglLieferant = new WrapperLabel();
    wlaMoeglLieferant.setText(LPMain.getInstance().getTextRespectUISPr("part.moeglicher_lieferant") + ": ");
    wlaMoeglLieferant.setMaximumSize(new Dimension(140,
        Defaults.getInstance().getControlHeight()));
    wlaMoeglLieferant.setPreferredSize(new Dimension(135,
        Defaults.getInstance().getControlHeight()));
    wlaMoeglLieferant.setMinimumSize(new Dimension(135,
        Defaults.getInstance().getControlHeight()));

    wbuMoeglLieferantDatenrichtung = new WrapperButtonZusammenfuehren();
    wbuMoeglLieferantDatenrichtung.addActionListener(this);
    wbuMoeglLieferantDatenrichtung.setActionCommand(ACTION_DATENRICHTUNG_WAEHLEN);
    vWrapperButtonsZusammenfuehren.addElement(wbuMoeglLieferantDatenrichtung);

    wcbZielLieferantMoeglLieferant = new WrapperCheckBoxZusammenfuehren();
    wcbZielLieferantMoeglLieferant.setPreferredSize(new Dimension(17, Defaults.getInstance().getControlHeight()));
    wcbZielLieferantMoeglLieferant.setShort(Helper.boolean2Short(false));
    wcbZielLieferantMoeglLieferant.setActivatable(false);
    vZielfelder.addElement(wcbZielLieferantMoeglLieferant);

    wcbQuellLieferantMoeglLieferant = new WrapperCheckBoxZusammenfuehren();
    wcbQuellLieferantMoeglLieferant.setPreferredSize(new Dimension(17, Defaults.getInstance().getControlHeight()));
    wcbQuellLieferantMoeglLieferant.setShort(Helper.boolean2Short(false));
    wcbQuellLieferantMoeglLieferant.setActivatable(false);
    vQuellfelder.addElement(wcbQuellLieferantMoeglLieferant);


    // Waehrung
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

    wtfZielLieferantWaehrung = new WrapperTextFieldZusammenfuehren();
    wtfZielLieferantWaehrung.setText("");
    wtfZielLieferantWaehrung.setMaximumSize(new Dimension(180,
        Defaults.getInstance().getControlHeight()));
    wtfZielLieferantWaehrung.setPreferredSize(new Dimension(150,
        Defaults.getInstance().getControlHeight()));
    wtfZielLieferantWaehrung.setMinimumSize(new Dimension(150,
        Defaults.getInstance().getControlHeight()));
    wtfZielLieferantWaehrung.setActivatable(false);
    vZielfelder.addElement(wtfZielLieferantWaehrung);

    wtfQuellLieferantWaehrung = new WrapperTextFieldZusammenfuehren();
    wtfQuellLieferantWaehrung.setText("");
    wtfQuellLieferantWaehrung.setMaximumSize(new Dimension(180,
        Defaults.getInstance().getControlHeight()));
    wtfQuellLieferantWaehrung.setPreferredSize(new Dimension(150,
        Defaults.getInstance().getControlHeight()));
    wtfQuellLieferantWaehrung.setMinimumSize(new Dimension(150,
        Defaults.getInstance().getControlHeight()));
    wtfQuellLieferantWaehrung.setActivatable(false);
    vQuellfelder.addElement(wtfQuellLieferantWaehrung);


    // Kostenstelle
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

    wtfZielLieferantKostenstelle = new WrapperTextFieldZusammenfuehren();
    wtfZielLieferantKostenstelle.setText("");
    wtfZielLieferantKostenstelle.setMaximumSize(new Dimension(180,
        Defaults.getInstance().getControlHeight()));
    wtfZielLieferantKostenstelle.setPreferredSize(new Dimension(150,
        Defaults.getInstance().getControlHeight()));
    wtfZielLieferantKostenstelle.setMinimumSize(new Dimension(150,
        Defaults.getInstance().getControlHeight()));
    wtfZielLieferantKostenstelle.setActivatable(false);
    vZielfelder.addElement(wtfZielLieferantKostenstelle);

    wtfQuellLieferantKostenstelle = new WrapperTextFieldZusammenfuehren();
    wtfQuellLieferantKostenstelle.setText("");
    wtfQuellLieferantKostenstelle.setMaximumSize(new Dimension(180,
        Defaults.getInstance().getControlHeight()));
    wtfQuellLieferantKostenstelle.setPreferredSize(new Dimension(150,
        Defaults.getInstance().getControlHeight()));
    wtfQuellLieferantKostenstelle.setMinimumSize(new Dimension(150,
        Defaults.getInstance().getControlHeight()));
    wtfQuellLieferantKostenstelle.setActivatable(false);
    vQuellfelder.addElement(wtfQuellLieferantKostenstelle);


    // Warenkonto
    wlaWarenkonto = new WrapperLabel();
    wlaWarenkonto.setText(LPMain.getInstance().getTextRespectUISPr("lp.konto.waren") +
                           ": ");
    wlaWarenkonto.setMaximumSize(new Dimension(130,
                                                Defaults.getInstance().getControlHeight()));
    wlaWarenkonto.setPreferredSize(new Dimension(125,
                                                  Defaults.getInstance().getControlHeight()));
    wlaWarenkonto.setMinimumSize(new Dimension(125,
                                                Defaults.getInstance().getControlHeight()));

    wbuWarenkontoDatenrichtung = new WrapperButtonZusammenfuehren();
    wbuWarenkontoDatenrichtung.addActionListener(this);
    wbuWarenkontoDatenrichtung.setActionCommand(ACTION_DATENRICHTUNG_WAEHLEN);
    vWrapperButtonsZusammenfuehren.addElement(wbuWarenkontoDatenrichtung);

    wtfZielLieferantWarenkonto = new WrapperTextFieldZusammenfuehren();
    wtfZielLieferantWarenkonto.setText("");
    wtfZielLieferantWarenkonto.setMaximumSize(new Dimension(180,
        Defaults.getInstance().getControlHeight()));
    wtfZielLieferantWarenkonto.setPreferredSize(new Dimension(150,
        Defaults.getInstance().getControlHeight()));
    wtfZielLieferantWarenkonto.setMinimumSize(new Dimension(150,
        Defaults.getInstance().getControlHeight()));
    wtfZielLieferantWarenkonto.setActivatable(false);
    vZielfelder.addElement(wtfZielLieferantWarenkonto);

    wtfQuellLieferantWarenkonto = new WrapperTextFieldZusammenfuehren();
    wtfQuellLieferantWarenkonto.setText("");
    wtfQuellLieferantWarenkonto.setMaximumSize(new Dimension(180,
        Defaults.getInstance().getControlHeight()));
    wtfQuellLieferantWarenkonto.setPreferredSize(new Dimension(150,
        Defaults.getInstance().getControlHeight()));
    wtfQuellLieferantWarenkonto.setMinimumSize(new Dimension(150,
        Defaults.getInstance().getControlHeight()));
    wtfQuellLieferantWarenkonto.setActivatable(false);
    vQuellfelder.addElement(wtfQuellLieferantWarenkonto);


    // Kreditorenkonto
    wlaKreditorenkonto = new WrapperLabel();
    wlaKreditorenkonto.setText(LPMain.getInstance().getTextRespectUISPr(
        "lp.kreditorenkonto") + ": ");
    wlaKreditorenkonto.setMaximumSize(new Dimension(130,
        Defaults.getInstance().getControlHeight()));
    wlaKreditorenkonto.setPreferredSize(new Dimension(125,
        Defaults.getInstance().getControlHeight()));
    wlaKreditorenkonto.setMinimumSize(new Dimension(125,
        Defaults.getInstance().getControlHeight()));

    wbuKreditorenkontoDatenrichtung = new WrapperButtonZusammenfuehren();
    wbuKreditorenkontoDatenrichtung.addActionListener(this);
    wbuKreditorenkontoDatenrichtung.setActionCommand(ACTION_DATENRICHTUNG_WAEHLEN);
    vWrapperButtonsZusammenfuehren.addElement(wbuKreditorenkontoDatenrichtung);

    wtfZielLieferantKreditorenkonto = new WrapperTextFieldZusammenfuehren();
    wtfZielLieferantKreditorenkonto.setText("");
    wtfZielLieferantKreditorenkonto.setMaximumSize(new Dimension(180,
        Defaults.getInstance().getControlHeight()));
    wtfZielLieferantKreditorenkonto.setPreferredSize(new Dimension(150,
        Defaults.getInstance().getControlHeight()));
    wtfZielLieferantKreditorenkonto.setMinimumSize(new Dimension(150,
        Defaults.getInstance().getControlHeight()));
    wtfZielLieferantKreditorenkonto.setActivatable(false);
    vZielfelder.addElement(wtfZielLieferantKreditorenkonto);

    wtfQuellLieferantKreditorenkonto = new WrapperTextFieldZusammenfuehren();
    wtfQuellLieferantKreditorenkonto.setText("");
    wtfQuellLieferantKreditorenkonto.setMaximumSize(new Dimension(180,
        Defaults.getInstance().getControlHeight()));
    wtfQuellLieferantKreditorenkonto.setPreferredSize(new Dimension(150,
        Defaults.getInstance().getControlHeight()));
    wtfQuellLieferantKreditorenkonto.setMinimumSize(new Dimension(150,
        Defaults.getInstance().getControlHeight()));
    wtfQuellLieferantKreditorenkonto.setActivatable(false);
    vQuellfelder.addElement(wtfQuellLieferantKreditorenkonto);


    // Beurteilen
    wlaBeurteilen = new WrapperLabel();
    wlaBeurteilen.setText(LPMain.getInstance().getTextRespectUISPr("lp.beurteilen") + ": ");
    wlaBeurteilen.setMaximumSize(new Dimension(140,
        Defaults.getInstance().getControlHeight()));
    wlaBeurteilen.setPreferredSize(new Dimension(135,
        Defaults.getInstance().getControlHeight()));
    wlaBeurteilen.setMinimumSize(new Dimension(135,
        Defaults.getInstance().getControlHeight()));

    wbuBeurteilenDatenrichtung = new WrapperButtonZusammenfuehren();
    wbuBeurteilenDatenrichtung.addActionListener(this);
    wbuBeurteilenDatenrichtung.setActionCommand(ACTION_DATENRICHTUNG_WAEHLEN);
    vWrapperButtonsZusammenfuehren.addElement(wbuBeurteilenDatenrichtung);

    wcbZielLieferantBeurteilen = new WrapperCheckBoxZusammenfuehren();
    wcbZielLieferantBeurteilen.setPreferredSize(new Dimension(17, Defaults.getInstance().getControlHeight()));
    wcbZielLieferantBeurteilen.setShort(Helper.boolean2Short(false));
    wcbZielLieferantBeurteilen.setActivatable(false);
    vZielfelder.addElement(wcbZielLieferantBeurteilen);

    wcbQuellLieferantBeurteilen = new WrapperCheckBoxZusammenfuehren();
    wcbQuellLieferantBeurteilen.setPreferredSize(new Dimension(17, Defaults.getInstance().getControlHeight()));
    wcbQuellLieferantBeurteilen.setShort(Helper.boolean2Short(false));
    wcbQuellLieferantBeurteilen.setActivatable(false);
    vQuellfelder.addElement(wcbQuellLieferantBeurteilen);


    // Beurteilung
    wlaBeurteilung = new WrapperLabel();
    wlaBeurteilung.setText(LPMain.getInstance().getTextRespectUISPr("lp.beurteilung") +
                        ": ");
    wlaBeurteilung.setMaximumSize(new Dimension(130, Defaults.getInstance().getControlHeight()));
    wlaBeurteilung.setPreferredSize(new Dimension(125,
                                               Defaults.getInstance().getControlHeight()));
    wlaBeurteilung.setMinimumSize(new Dimension(125, Defaults.getInstance().getControlHeight()));

    wbuBeurteilungDatenrichtung = new WrapperButtonZusammenfuehren();
    wbuBeurteilungDatenrichtung.addActionListener(this);
    wbuBeurteilungDatenrichtung.setActionCommand(ACTION_DATENRICHTUNG_WAEHLEN);
    vWrapperButtonsZusammenfuehren.addElement(wbuBeurteilungDatenrichtung);

    wtfZielLieferantBeurteilung = new WrapperTextFieldZusammenfuehren();
    wtfZielLieferantBeurteilung.setText("");
    wtfZielLieferantBeurteilung.setMaximumSize(new Dimension(180,
        Defaults.getInstance().getControlHeight()));
    wtfZielLieferantBeurteilung.setPreferredSize(new Dimension(150,
        Defaults.getInstance().getControlHeight()));
    wtfZielLieferantBeurteilung.setMinimumSize(new Dimension(150,
        Defaults.getInstance().getControlHeight()));
    wtfZielLieferantBeurteilung.setActivatable(false);
    vZielfelder.addElement(wtfZielLieferantBeurteilung);

    wtfQuellLieferantBeurteilung = new WrapperTextFieldZusammenfuehren();
    wtfQuellLieferantBeurteilung.setText("");
    wtfQuellLieferantBeurteilung.setMaximumSize(new Dimension(180,
        Defaults.getInstance().getControlHeight()));
    wtfQuellLieferantBeurteilung.setPreferredSize(new Dimension(150,
        Defaults.getInstance().getControlHeight()));
    wtfQuellLieferantBeurteilung.setMinimumSize(new Dimension(150,
        Defaults.getInstance().getControlHeight()));
    wtfQuellLieferantBeurteilung.setActivatable(false);
    vQuellfelder.addElement(wtfQuellLieferantBeurteilung);


    // HinweisInt
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

    // HinweisExt
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
    wtaZielLieferantKommentar = new WrapperTextAreaZusammenfuehren();
    wtaZielLieferantKommentar.setAutoscrolls(true);
    wtaZielLieferantKommentar.setRows(3);
    wtaZielLieferantKommentar.setColumns(15);
    wtaZielLieferantKommentar.setEditable(false);
    wtaZielLieferantKommentar.setActivatable(false);
    wtaZielLieferantKommentar.setCaretPosition(0);
    vZielfelder.addElement(wtaZielLieferantKommentar);
    jScrollPaneZielLieferantKommentar = new javax.swing.JScrollPane();
    jScrollPaneZielLieferantKommentar.setViewportView(wtaZielLieferantKommentar);

    wtaQuellLieferantKommentar = new WrapperTextAreaZusammenfuehren();
    wtaQuellLieferantKommentar.setAutoscrolls(true);
    wtaQuellLieferantKommentar.setRows(3);
    wtaQuellLieferantKommentar.setColumns(15);
    wtaQuellLieferantKommentar.setEditable(false);
    wtaQuellLieferantKommentar.setActivatable(false);
    wtaQuellLieferantKommentar.setCaretPosition(0);
    vQuellfelder.addElement(wtaQuellLieferantKommentar);
    jScrollPaneQuellLieferantKommentar = new javax.swing.JScrollPane();
    jScrollPaneQuellLieferantKommentar.setViewportView(wtaQuellLieferantKommentar);


    // Zahlungsziel
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

    wtfZielLieferantZahlungsziel = new WrapperTextFieldZusammenfuehren();
    wtfZielLieferantZahlungsziel.setText("");
    wtfZielLieferantZahlungsziel.setMaximumSize(new Dimension(180,
        Defaults.getInstance().getControlHeight()));
    wtfZielLieferantZahlungsziel.setPreferredSize(new Dimension(150,
        Defaults.getInstance().getControlHeight()));
    wtfZielLieferantZahlungsziel.setMinimumSize(new Dimension(150,
        Defaults.getInstance().getControlHeight()));
    wtfZielLieferantZahlungsziel.setActivatable(false);
    vZielfelder.addElement(wtfZielLieferantZahlungsziel);

    wtfQuellLieferantZahlungsziel = new WrapperTextFieldZusammenfuehren();
    wtfQuellLieferantZahlungsziel.setText("");
    wtfQuellLieferantZahlungsziel.setMaximumSize(new Dimension(180,
        Defaults.getInstance().getControlHeight()));
    wtfQuellLieferantZahlungsziel.setPreferredSize(new Dimension(150,
        Defaults.getInstance().getControlHeight()));
    wtfQuellLieferantZahlungsziel.setMinimumSize(new Dimension(150,
        Defaults.getInstance().getControlHeight()));
    wtfQuellLieferantZahlungsziel.setActivatable(false);
    vQuellfelder.addElement(wtfQuellLieferantZahlungsziel);


    // Lieferart
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

    wtfZielLieferantLieferart = new WrapperTextFieldZusammenfuehren();
    wtfZielLieferantLieferart.setText("");
    wtfZielLieferantLieferart.setMaximumSize(new Dimension(180,
        Defaults.getInstance().getControlHeight()));
    wtfZielLieferantLieferart.setPreferredSize(new Dimension(150,
        Defaults.getInstance().getControlHeight()));
    wtfZielLieferantLieferart.setMinimumSize(new Dimension(150,
        Defaults.getInstance().getControlHeight()));
    wtfZielLieferantLieferart.setActivatable(false);
    vZielfelder.addElement(wtfZielLieferantLieferart);

    wtfQuellLieferantLieferart = new WrapperTextFieldZusammenfuehren();
    wtfQuellLieferantLieferart.setText("");
    wtfQuellLieferantLieferart.setMaximumSize(new Dimension(180,
        Defaults.getInstance().getControlHeight()));
    wtfQuellLieferantLieferart.setPreferredSize(new Dimension(150,
        Defaults.getInstance().getControlHeight()));
    wtfQuellLieferantLieferart.setMinimumSize(new Dimension(150,
        Defaults.getInstance().getControlHeight()));
    wtfQuellLieferantLieferart.setActivatable(false);
    vQuellfelder.addElement(wtfQuellLieferantLieferart);

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

    wtfZielLieferantSpediteur = new WrapperTextFieldZusammenfuehren();
    wtfZielLieferantSpediteur.setText("");
    wtfZielLieferantSpediteur.setMaximumSize(new Dimension(180,
        Defaults.getInstance().getControlHeight()));
    wtfZielLieferantSpediteur.setPreferredSize(new Dimension(150,
        Defaults.getInstance().getControlHeight()));
    wtfZielLieferantSpediteur.setMinimumSize(new Dimension(150,
        Defaults.getInstance().getControlHeight()));
    wtfZielLieferantSpediteur.setActivatable(false);
    vZielfelder.addElement(wtfZielLieferantSpediteur);

    wtfQuellLieferantSpediteur = new WrapperTextFieldZusammenfuehren();
    wtfQuellLieferantSpediteur.setText("");
    wtfQuellLieferantSpediteur.setMaximumSize(new Dimension(180,
        Defaults.getInstance().getControlHeight()));
    wtfQuellLieferantSpediteur.setPreferredSize(new Dimension(150,
        Defaults.getInstance().getControlHeight()));
    wtfQuellLieferantSpediteur.setMinimumSize(new Dimension(150,
        Defaults.getInstance().getControlHeight()));
    wtfQuellLieferantSpediteur.setActivatable(false);
    vQuellfelder.addElement(wtfQuellLieferantSpediteur);

    // Rabatt
    wlaRabatt = new WrapperLabel();
    wlaRabatt.setText(LPMain.getInstance().getTextRespectUISPr("label.rabatt") +
                          ": ");
    wlaRabatt.setMaximumSize(new Dimension(115,
                                               Defaults.getInstance().getControlHeight()));
    wlaRabatt.setPreferredSize(new Dimension(100,
                                                 Defaults.getInstance().getControlHeight()));
    wlaRabatt.setMinimumSize(new Dimension(100,
                                               Defaults.getInstance().getControlHeight()));

    wbuRabattDatenrichtung = new WrapperButtonZusammenfuehren();
    wbuRabattDatenrichtung.addActionListener(this);
    wbuRabattDatenrichtung.setActionCommand(ACTION_DATENRICHTUNG_WAEHLEN);
    vWrapperButtonsZusammenfuehren.addElement(wbuRabattDatenrichtung);

    wtfZielLieferantRabatt = new WrapperTextFieldZusammenfuehren();
    wtfZielLieferantRabatt.setText("");
    wtfZielLieferantRabatt.setMaximumSize(new Dimension(180,
        Defaults.getInstance().getControlHeight()));
    wtfZielLieferantRabatt.setPreferredSize(new Dimension(150,
        Defaults.getInstance().getControlHeight()));
    wtfZielLieferantRabatt.setMinimumSize(new Dimension(150,
        Defaults.getInstance().getControlHeight()));
    wtfZielLieferantRabatt.setActivatable(false);
    vZielfelder.addElement(wtfZielLieferantRabatt);

    wtfQuellLieferantRabatt = new WrapperTextFieldZusammenfuehren();
    wtfQuellLieferantRabatt.setText("");
    wtfQuellLieferantRabatt.setMaximumSize(new Dimension(180,
        Defaults.getInstance().getControlHeight()));
    wtfQuellLieferantRabatt.setPreferredSize(new Dimension(150,
        Defaults.getInstance().getControlHeight()));
    wtfQuellLieferantRabatt.setMinimumSize(new Dimension(150,
        Defaults.getInstance().getControlHeight()));
    wtfQuellLieferantRabatt.setActivatable(false);
    vQuellfelder.addElement(wtfQuellLieferantRabatt);

    // Jahresbonus
    wlaJahresbonus = new WrapperLabel();
    wlaJahresbonus.setText(LPMain.getInstance().getTextRespectUISPr("lp.jahresbonus") +
                           ": ");
    wlaJahresbonus.setMaximumSize(new Dimension(115,
                                                Defaults.getInstance().getControlHeight()));
    wlaJahresbonus.setPreferredSize(new Dimension(100,
                                                  Defaults.getInstance().getControlHeight()));
    wlaJahresbonus.setMinimumSize(new Dimension(100,
                                                Defaults.getInstance().getControlHeight()));

    wbuJahresbonusDatenrichtung = new WrapperButtonZusammenfuehren();
    wbuJahresbonusDatenrichtung.addActionListener(this);
    wbuJahresbonusDatenrichtung.setActionCommand(ACTION_DATENRICHTUNG_WAEHLEN);
    vWrapperButtonsZusammenfuehren.addElement(wbuJahresbonusDatenrichtung);

    wtfZielLieferantJahresbonus = new WrapperTextFieldZusammenfuehren();
    wtfZielLieferantJahresbonus.setText("");
    wtfZielLieferantJahresbonus.setMaximumSize(new Dimension(180,
        Defaults.getInstance().getControlHeight()));
    wtfZielLieferantJahresbonus.setPreferredSize(new Dimension(150,
        Defaults.getInstance().getControlHeight()));
    wtfZielLieferantJahresbonus.setMinimumSize(new Dimension(150,
        Defaults.getInstance().getControlHeight()));
    wtfZielLieferantJahresbonus.setActivatable(false);
    vZielfelder.addElement(wtfZielLieferantJahresbonus);

    wtfQuellLieferantJahresbonus = new WrapperTextFieldZusammenfuehren();
    wtfQuellLieferantJahresbonus.setText("");
    wtfQuellLieferantJahresbonus.setMaximumSize(new Dimension(180,
        Defaults.getInstance().getControlHeight()));
    wtfQuellLieferantJahresbonus.setPreferredSize(new Dimension(150,
        Defaults.getInstance().getControlHeight()));
    wtfQuellLieferantJahresbonus.setMinimumSize(new Dimension(150,
        Defaults.getInstance().getControlHeight()));
    wtfQuellLieferantJahresbonus.setActivatable(false);
    vQuellfelder.addElement(wtfQuellLieferantJahresbonus);

    // Kreditlimit
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

    wtfZielLieferantKreditlimit = new WrapperTextFieldZusammenfuehren();
    wtfZielLieferantKreditlimit.setText("");
    wtfZielLieferantKreditlimit.setMaximumSize(new Dimension(180,
        Defaults.getInstance().getControlHeight()));
    wtfZielLieferantKreditlimit.setPreferredSize(new Dimension(150,
        Defaults.getInstance().getControlHeight()));
    wtfZielLieferantKreditlimit.setMinimumSize(new Dimension(150,
        Defaults.getInstance().getControlHeight()));
    wtfZielLieferantKreditlimit.setActivatable(false);
    vZielfelder.addElement(wtfZielLieferantKreditlimit);

    wtfQuellLieferantKreditlimit = new WrapperTextFieldZusammenfuehren();
    wtfQuellLieferantKreditlimit.setText("");
    wtfQuellLieferantKreditlimit.setMaximumSize(new Dimension(180,
        Defaults.getInstance().getControlHeight()));
    wtfQuellLieferantKreditlimit.setPreferredSize(new Dimension(150,
        Defaults.getInstance().getControlHeight()));
    wtfQuellLieferantKreditlimit.setMinimumSize(new Dimension(150,
        Defaults.getInstance().getControlHeight()));
    wtfQuellLieferantKreditlimit.setActivatable(false);
    vQuellfelder.addElement(wtfQuellLieferantKreditlimit);


    // Umsatzgrenze
    wlaUmsatzgrenze = new WrapperLabel();
    wlaUmsatzgrenze.setText(LPMain.getInstance().getTextRespectUISPr("part.ab_umsatz") +
                           ": ");
    wlaUmsatzgrenze.setMaximumSize(new Dimension(115,
                                                Defaults.getInstance().getControlHeight()));
    wlaUmsatzgrenze.setPreferredSize(new Dimension(100,
                                                  Defaults.getInstance().getControlHeight()));
    wlaUmsatzgrenze.setMinimumSize(new Dimension(100,
                                                Defaults.getInstance().getControlHeight()));

    wbuUmsatzgrenzeDatenrichtung = new WrapperButtonZusammenfuehren();
    wbuUmsatzgrenzeDatenrichtung.addActionListener(this);
    wbuUmsatzgrenzeDatenrichtung.setActionCommand(ACTION_DATENRICHTUNG_WAEHLEN);
    vWrapperButtonsZusammenfuehren.addElement(wbuUmsatzgrenzeDatenrichtung);

    wtfZielLieferantUmsatzgrenze = new WrapperTextFieldZusammenfuehren();
    wtfZielLieferantUmsatzgrenze.setText("");
    wtfZielLieferantUmsatzgrenze.setMaximumSize(new Dimension(180,
        Defaults.getInstance().getControlHeight()));
    wtfZielLieferantUmsatzgrenze.setPreferredSize(new Dimension(150,
        Defaults.getInstance().getControlHeight()));
    wtfZielLieferantUmsatzgrenze.setMinimumSize(new Dimension(150,
        Defaults.getInstance().getControlHeight()));
    wtfZielLieferantUmsatzgrenze.setActivatable(false);
    vZielfelder.addElement(wtfZielLieferantUmsatzgrenze);

    wtfQuellLieferantUmsatzgrenze = new WrapperTextFieldZusammenfuehren();
    wtfQuellLieferantUmsatzgrenze.setText("");
    wtfQuellLieferantUmsatzgrenze.setMaximumSize(new Dimension(180,
        Defaults.getInstance().getControlHeight()));
    wtfQuellLieferantUmsatzgrenze.setPreferredSize(new Dimension(150,
        Defaults.getInstance().getControlHeight()));
    wtfQuellLieferantUmsatzgrenze.setMinimumSize(new Dimension(150,
        Defaults.getInstance().getControlHeight()));
    wtfQuellLieferantUmsatzgrenze.setActivatable(false);
    vQuellfelder.addElement(wtfQuellLieferantUmsatzgrenze);

    // Mindestbestellwert
    wlaMindestbestellwert = new WrapperLabel();
    wlaMindestbestellwert.setText(LPMain.getInstance().getTextRespectUISPr("lp.mindestbestellwert") +
                           ": ");
    wlaMindestbestellwert.setMaximumSize(new Dimension(115,
                                                Defaults.getInstance().getControlHeight()));
    wlaMindestbestellwert.setPreferredSize(new Dimension(100,
                                                  Defaults.getInstance().getControlHeight()));
    wlaMindestbestellwert.setMinimumSize(new Dimension(100,
                                                Defaults.getInstance().getControlHeight()));

    wbuMindestbestellwertDatenrichtung = new WrapperButtonZusammenfuehren();
    wbuMindestbestellwertDatenrichtung.addActionListener(this);
    wbuMindestbestellwertDatenrichtung.setActionCommand(ACTION_DATENRICHTUNG_WAEHLEN);
    vWrapperButtonsZusammenfuehren.addElement(wbuMindestbestellwertDatenrichtung);

    wtfZielLieferantMindestbestellwert = new WrapperTextFieldZusammenfuehren();
    wtfZielLieferantMindestbestellwert.setText("");
    wtfZielLieferantMindestbestellwert.setMaximumSize(new Dimension(180,
        Defaults.getInstance().getControlHeight()));
    wtfZielLieferantMindestbestellwert.setPreferredSize(new Dimension(150,
        Defaults.getInstance().getControlHeight()));
    wtfZielLieferantMindestbestellwert.setMinimumSize(new Dimension(150,
        Defaults.getInstance().getControlHeight()));
    wtfZielLieferantMindestbestellwert.setActivatable(false);
    vZielfelder.addElement(wtfZielLieferantMindestbestellwert);

    wtfQuellLieferantMindestbestellwert = new WrapperTextFieldZusammenfuehren();
    wtfQuellLieferantMindestbestellwert.setText("");
    wtfQuellLieferantMindestbestellwert.setMaximumSize(new Dimension(180,
        Defaults.getInstance().getControlHeight()));
    wtfQuellLieferantMindestbestellwert.setPreferredSize(new Dimension(150,
        Defaults.getInstance().getControlHeight()));
    wtfQuellLieferantMindestbestellwert.setMinimumSize(new Dimension(150,
        Defaults.getInstance().getControlHeight()));
    wtfQuellLieferantMindestbestellwert.setActivatable(false);
    vQuellfelder.addElement(wtfQuellLieferantMindestbestellwert);

    // Mindermengenzuschlagbis
    wlaMindermengenzuschlagbis = new WrapperLabel();
    wlaMindermengenzuschlagbis.setText(LPMain.getInstance().getTextRespectUISPr("part.mindermengenzuschlag") +
                           ": ");
    wlaMindermengenzuschlagbis.setMaximumSize(new Dimension(115,
                                                Defaults.getInstance().getControlHeight()));
    wlaMindermengenzuschlagbis.setPreferredSize(new Dimension(100,
                                                  Defaults.getInstance().getControlHeight()));
    wlaMindermengenzuschlagbis.setMinimumSize(new Dimension(100,
                                                Defaults.getInstance().getControlHeight()));

    wbuMindermengenzuschlagbisDatenrichtung = new WrapperButtonZusammenfuehren();
    wbuMindermengenzuschlagbisDatenrichtung.addActionListener(this);
    wbuMindermengenzuschlagbisDatenrichtung.setActionCommand(ACTION_DATENRICHTUNG_WAEHLEN);
    vWrapperButtonsZusammenfuehren.addElement(wbuMindermengenzuschlagbisDatenrichtung);

    wtfZielLieferantMindermengenzuschlagbis = new WrapperTextFieldZusammenfuehren();
    wtfZielLieferantMindermengenzuschlagbis.setText("");
    wtfZielLieferantMindermengenzuschlagbis.setMaximumSize(new Dimension(180,
        Defaults.getInstance().getControlHeight()));
    wtfZielLieferantMindermengenzuschlagbis.setPreferredSize(new Dimension(150,
        Defaults.getInstance().getControlHeight()));
    wtfZielLieferantMindermengenzuschlagbis.setMinimumSize(new Dimension(150,
        Defaults.getInstance().getControlHeight()));
    wtfZielLieferantMindermengenzuschlagbis.setActivatable(false);
    vZielfelder.addElement(wtfZielLieferantMindermengenzuschlagbis);

    wtfQuellLieferantMindermengenzuschlagbis = new WrapperTextFieldZusammenfuehren();
    wtfQuellLieferantMindermengenzuschlagbis.setText("");
    wtfQuellLieferantMindermengenzuschlagbis.setMaximumSize(new Dimension(180,
        Defaults.getInstance().getControlHeight()));
    wtfQuellLieferantMindermengenzuschlagbis.setPreferredSize(new Dimension(150,
        Defaults.getInstance().getControlHeight()));
    wtfQuellLieferantMindermengenzuschlagbis.setMinimumSize(new Dimension(150,
        Defaults.getInstance().getControlHeight()));
    wtfQuellLieferantMindermengenzuschlagbis.setActivatable(false);
    vQuellfelder.addElement(wtfQuellLieferantMindermengenzuschlagbis);

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

    wtfZielLieferantRechnungsadresspartner = new WrapperTextFieldZusammenfuehren();
    wtfZielLieferantRechnungsadresspartner.setText("");
    wtfZielLieferantRechnungsadresspartner.setMaximumSize(new Dimension(180,
        Defaults.getInstance().getControlHeight()));
    wtfZielLieferantRechnungsadresspartner.setPreferredSize(new Dimension(150,
        Defaults.getInstance().getControlHeight()));
    wtfZielLieferantRechnungsadresspartner.setMinimumSize(new Dimension(150,
        Defaults.getInstance().getControlHeight()));
    wtfZielLieferantRechnungsadresspartner.setActivatable(false);
    vZielfelder.addElement(wtfZielLieferantRechnungsadresspartner);

    wtfQuellLieferantRechnungsadresspartner = new WrapperTextFieldZusammenfuehren();
    wtfQuellLieferantRechnungsadresspartner.setText("");
    wtfQuellLieferantRechnungsadresspartner.setMaximumSize(new Dimension(180,
        Defaults.getInstance().getControlHeight()));
    wtfQuellLieferantRechnungsadresspartner.setPreferredSize(new Dimension(150,
        Defaults.getInstance().getControlHeight()));
    wtfQuellLieferantRechnungsadresspartner.setMinimumSize(new Dimension(150,
        Defaults.getInstance().getControlHeight()));
    wtfQuellLieferantRechnungsadresspartner.setActivatable(false);
    vQuellfelder.addElement(wtfQuellLieferantRechnungsadresspartner);


    jpaWorkingOnScroll.add(wbuZielLieferantAuswaehlen,
                         new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0
                                                , GridBagConstraints.CENTER,
                                                GridBagConstraints.HORIZONTAL,
                                                new Insets(0, 0, 0, 0), 0, 0));

    jpaWorkingOnScroll.add(wbuAuswahlReset,
                         new GridBagConstraints(2, iZeile, 1, 1, 0.0, 0.0
                                                , GridBagConstraints.CENTER,
                                                GridBagConstraints.HORIZONTAL,
                                                new Insets(0, 0, 0, 0), 0, 0));

    jpaWorkingOnScroll.add(wbuQuellLieferantAuswaehlen,
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

    jpaWorkingOnScroll.add(wtfZielLieferantPartnername,
                           new GridBagConstraints(1, iZeile, 1, 1, 0.1, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    jpaWorkingOnScroll.add(wbuPartnernameDatenrichtung,
                           new GridBagConstraints(2, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    jpaWorkingOnScroll.add(wtfQuellLieferantPartnername,
                           new GridBagConstraints(3, iZeile, 1, 1, 0.1, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    // Zeile
    iZeile++;

    jpaWorkingOnScroll.add(wlaKundennr, new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0
        , GridBagConstraints.CENTER,
        GridBagConstraints.HORIZONTAL,
        new Insets(0, 0, 0, 0), 0, 0));

    jpaWorkingOnScroll.add(wtfZielLieferantKundennr,
                           new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    jpaWorkingOnScroll.add(wbuKundennrDatenrichtung,
                           new GridBagConstraints(2, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    jpaWorkingOnScroll.add(wtfQuellLieferantKundennr,
                           new GridBagConstraints(3, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    // Zeile
    iZeile++;

    jpaWorkingOnScroll.add(wlaMwst, new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0
        , GridBagConstraints.CENTER,
        GridBagConstraints.HORIZONTAL,
        new Insets(0, 0, 0, 0), 0, 0));

    jpaWorkingOnScroll.add(wtfZielLieferantMwst,
                           new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

   /* jpaWorkingOnScroll.add(wbuMwstDatenrichtung,
                           new GridBagConstraints(2, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));*/

    jpaWorkingOnScroll.add(wtfQuellLieferantMwst,
                           new GridBagConstraints(3, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    // Zeile
    iZeile++;

    jpaWorkingOnScroll.add(wlaMoeglLieferant,
                           new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    jpaWorkingOnScroll.add(wcbZielLieferantMoeglLieferant,
                           new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.NONE,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    jpaWorkingOnScroll.add(wbuMoeglLieferantDatenrichtung,
                           new GridBagConstraints(2, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    jpaWorkingOnScroll.add(wcbQuellLieferantMoeglLieferant,
                           new GridBagConstraints(3, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.NONE,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    // Zeile
    iZeile++;

    jpaWorkingOnScroll.add(wlaWarenkonto,
                           new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    jpaWorkingOnScroll.add(wtfZielLieferantWarenkonto,
                           new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    jpaWorkingOnScroll.add(wbuWarenkontoDatenrichtung,
                           new GridBagConstraints(2, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    jpaWorkingOnScroll.add(wtfQuellLieferantWarenkonto,
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

    jpaWorkingOnScroll.add(wtfZielLieferantWaehrung,
                           new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    jpaWorkingOnScroll.add(wbuWaehrungDatenrichtung,
                           new GridBagConstraints(2, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    jpaWorkingOnScroll.add(wtfQuellLieferantWaehrung,
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

    jpaWorkingOnScroll.add(wtfZielLieferantKostenstelle,
                           new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    jpaWorkingOnScroll.add(wbuKostenstelleDatenrichtung,
                           new GridBagConstraints(2, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    jpaWorkingOnScroll.add(wtfQuellLieferantKostenstelle,
                           new GridBagConstraints(3, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    // Zeile
    iZeile++;

    jpaWorkingOnScroll.add(wlaKreditorenkonto,
                           new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    jpaWorkingOnScroll.add(wtfZielLieferantKreditorenkonto,
                           new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

   /* jpaWorkingOnScroll.add(wbuKreditorenkontoDatenrichtung,
                           new GridBagConstraints(2, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));*/

    jpaWorkingOnScroll.add(wtfQuellLieferantKreditorenkonto,
                           new GridBagConstraints(3, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    // Zeile
    iZeile++;

    jpaWorkingOnScroll.add(wlaBeurteilen,
                           new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    jpaWorkingOnScroll.add(wcbZielLieferantBeurteilen,
                           new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.NONE,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    jpaWorkingOnScroll.add(wbuBeurteilenDatenrichtung,
                           new GridBagConstraints(2, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    jpaWorkingOnScroll.add(wcbQuellLieferantBeurteilen,
                           new GridBagConstraints(3, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.NONE,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    // Zeile
    iZeile++;

    jpaWorkingOnScroll.add(wlaBeurteilung, new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0
        , GridBagConstraints.CENTER,
        GridBagConstraints.HORIZONTAL,
        new Insets(0, 0, 0, 0), 0, 0));

    jpaWorkingOnScroll.add(wtfZielLieferantBeurteilung,
                           new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    jpaWorkingOnScroll.add(wbuBeurteilungDatenrichtung,
                           new GridBagConstraints(2, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    jpaWorkingOnScroll.add(wtfQuellLieferantBeurteilung,
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

    jpaWorkingOnScroll.add(wlaKommentar, new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0
        , GridBagConstraints.CENTER,
        GridBagConstraints.HORIZONTAL,
        new Insets(0, 0, 0, 0), 0, 0));

    jpaWorkingOnScroll.add(jScrollPaneZielLieferantKommentar,
                           new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    jpaWorkingOnScroll.add(wbuKommentarDatenrichtung,
                           new GridBagConstraints(2, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    jpaWorkingOnScroll.add(jScrollPaneQuellLieferantKommentar,
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

    jpaWorkingOnScroll.add(wtfZielLieferantZahlungsziel,
                           new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    jpaWorkingOnScroll.add(wbuZahlungszielDatenrichtung,
                           new GridBagConstraints(2, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    jpaWorkingOnScroll.add(wtfQuellLieferantZahlungsziel,
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

    jpaWorkingOnScroll.add(wtfZielLieferantLieferart,
                           new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    jpaWorkingOnScroll.add(wbuLieferartDatenrichtung,
                           new GridBagConstraints(2, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    jpaWorkingOnScroll.add(wtfQuellLieferantLieferart,
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

    jpaWorkingOnScroll.add(wtfZielLieferantSpediteur,
                           new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    jpaWorkingOnScroll.add(wbuSpediteurDatenrichtung,
                           new GridBagConstraints(2, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    jpaWorkingOnScroll.add(wtfQuellLieferantSpediteur,
                           new GridBagConstraints(3, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    // Zeile
    iZeile++;

    jpaWorkingOnScroll.add(wlaRabatt,
                           new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    jpaWorkingOnScroll.add(wtfZielLieferantRabatt,
                           new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    jpaWorkingOnScroll.add(wbuRabattDatenrichtung,
                           new GridBagConstraints(2, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    jpaWorkingOnScroll.add(wtfQuellLieferantRabatt,
                           new GridBagConstraints(3, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    // Zeile
    iZeile++;

    jpaWorkingOnScroll.add(wlaJahresbonus,
                           new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    jpaWorkingOnScroll.add(wtfZielLieferantJahresbonus,
                           new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    jpaWorkingOnScroll.add(wbuJahresbonusDatenrichtung,
                           new GridBagConstraints(2, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    jpaWorkingOnScroll.add(wtfQuellLieferantJahresbonus,
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

    jpaWorkingOnScroll.add(wtfZielLieferantKreditlimit,
                           new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    jpaWorkingOnScroll.add(wbuKreditlimitDatenrichtung,
                           new GridBagConstraints(2, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    jpaWorkingOnScroll.add(wtfQuellLieferantKreditlimit,
                           new GridBagConstraints(3, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    // Zeile
    iZeile++;

    jpaWorkingOnScroll.add(wlaUmsatzgrenze,
                           new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    jpaWorkingOnScroll.add(wtfZielLieferantUmsatzgrenze,
                           new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    jpaWorkingOnScroll.add(wbuUmsatzgrenzeDatenrichtung,
                           new GridBagConstraints(2, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    jpaWorkingOnScroll.add(wtfQuellLieferantUmsatzgrenze,
                           new GridBagConstraints(3, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));
    // Zeile
    iZeile++;

    jpaWorkingOnScroll.add(wlaMindestbestellwert,
                           new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    jpaWorkingOnScroll.add(wtfZielLieferantMindestbestellwert,
                           new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    jpaWorkingOnScroll.add(wbuMindestbestellwertDatenrichtung,
                           new GridBagConstraints(2, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    jpaWorkingOnScroll.add(wtfQuellLieferantMindestbestellwert,
                           new GridBagConstraints(3, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    // Zeile
    iZeile++;

    jpaWorkingOnScroll.add(wlaMindermengenzuschlagbis,
                           new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    jpaWorkingOnScroll.add(wtfZielLieferantMindermengenzuschlagbis,
                           new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    jpaWorkingOnScroll.add(wbuMindermengenzuschlagbisDatenrichtung,
                           new GridBagConstraints(2, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    jpaWorkingOnScroll.add(wtfQuellLieferantMindermengenzuschlagbis,
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

    jpaWorkingOnScroll.add(wtfZielLieferantRechnungsadresspartner,
                           new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    jpaWorkingOnScroll.add(wbuRechnungsadresspartnerDatenrichtung,
                           new GridBagConstraints(2, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));

    jpaWorkingOnScroll.add(wtfQuellLieferantRechnungsadresspartner,
                           new GridBagConstraints(3, iZeile, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.HORIZONTAL,
                                                  new Insets(0, 0, 0, 0), 0, 0));


    // Zeile
    iZeile++;

    jScrollPaneLieferant.setViewportView(jpaWorkingOnScroll);
    jScrollPaneLieferant.setAutoscrolls(true);

    jpaWorkingOn.add(jScrollPaneLieferant, new GridBagConstraints(0, iZeile, 1, 1, 1.0, 1.0
        , GridBagConstraints.CENTER,
        GridBagConstraints.BOTH,
        new Insets(0, 0, 0, 0), 0, 0));

    saveButton = createAndSaveButton("/com/lp/client/res/check2.png",
                                     LPMain.getTextRespectUISPr(
        "part.lieferantenzusammenfuehren.ausfuehren"),
                                     ACTION_SPECIAL_SAVE,
                                     KeyStroke.getKeyStroke('S', java.awt.event.InputEvent.CTRL_MASK), null);

    String[] aWhichButtonIUse = {
        ACTION_SPECIAL_SAVE};

    enableButtonAction(aWhichButtonIUse);

    /*ToDo: weiss nicht, was da hin muss*/
    LockStateValue lockstateValue = new LockStateValue(
        null,
        null,
        PanelBasis.LOCK_NO_LOCKING);
    updateButtons(lockstateValue);

    befuelleFelderMitLieferantZielDto(this.lieferantZielDto);
    setzeFarbenVonTextfeldern(this.vWrapperButtonsZusammenfuehren, this.vZielfelder, this.vQuellfelder, true);
  }

  /**
   * wird ein Ziellieferant (= Lieferant auf der linken Seite) ausgewaehlt, so werden die Felder mit dessen Daten gefuellt
   * @param liefZDto LieferantDto
   * @throws Throwable
   */
  private void befuelleFelderMitLieferantZielDto(LieferantDto liefZDto)
      throws Throwable {

    if (liefZDto.getPartnerDto().formatName() != null &&
        liefZDto.getPartnerDto().formatName() != "") {
      wtfZielLieferantPartnername.setText(liefZDto.getPartnerDto().formatName());
      wtfZielLieferantPartnername.setCaretPosition(0);
      wtfZielLieferantPartnername.setToolTipText(liefZDto.getPartnerDto().formatName());
    }
    else {
      wtfZielLieferantPartnername.setText("");
      wtfZielLieferantPartnername.setToolTipText("");
    }

    if (liefZDto.getMwstsatzbezIId() != null) {
      wtfZielLieferantMwst.setText(DelegateFactory.getInstance().getMandantDelegate().
                               mwstsatzbezFindByPrimaryKey(liefZDto.getMwstsatzbezIId()).
                               getCBezeichnung());
      wtfZielLieferantMwst.setCaretPosition(0);
      wtfZielLieferantMwst.setToolTipText(DelegateFactory.getInstance().getMandantDelegate().
                                      mwstsatzbezFindByPrimaryKey(liefZDto.getMwstsatzbezIId()).getCBezeichnung());
    }
    else {
      wtfZielLieferantMwst.setText("");
      wtfZielLieferantMwst.setToolTipText("");
    }

    if (liefZDto.getCKundennr() != null && liefZDto.getCKundennr() != "") {
      wtfZielLieferantKundennr.setText(liefZDto.getCKundennr());
      wtfZielLieferantKundennr.setCaretPosition(0);
      wtfZielLieferantKundennr.setToolTipText(liefZDto.getCKundennr());
    }
    else {
      wtfZielLieferantKundennr.setText("");
      wtfZielLieferantKundennr.setToolTipText("");
    }

    if (liefZDto.getBMoeglicherLieferant() != null) {
      wcbZielLieferantMoeglLieferant.setShort(liefZDto.getBMoeglicherLieferant());
    }
    else {
      wcbZielLieferantMoeglLieferant.setText("");
    }

    if (liefZDto.getWaehrungCNr() != null) {
      wtfZielLieferantWaehrung.setText(liefZDto.getWaehrungCNr());
      wtfZielLieferantWaehrung.setCaretPosition(0);
      wtfZielLieferantWaehrung.setToolTipText(liefZDto.getWaehrungCNr());
    }
    else {
      wtfZielLieferantWaehrung.setText("");
      wtfZielLieferantWaehrung.setToolTipText("");
    }

    if (liefZDto.getIIdKostenstelle() != null) {
      KostenstelleDto kostenstelleDto = DelegateFactory.getInstance().getSystemDelegate().
          kostenstelleFindByPrimaryKey(liefZDto.getIIdKostenstelle());
      wtfZielLieferantKostenstelle.setText(kostenstelleDto.formatKostenstellenbezeichnung());
      wtfZielLieferantKostenstelle.setCaretPosition(0);
      wtfZielLieferantKostenstelle.setToolTipText(kostenstelleDto.
                                              formatKostenstellenbezeichnung());
    }
    else {
      wtfZielLieferantKostenstelle.setText("");
      wtfZielLieferantKostenstelle.setToolTipText("");
    }


    if (liefZDto.getKontoIIdWarenkonto() != null) {
      KontoDtoSmall kontoDtoSmall = DelegateFactory.getInstance().getFinanzDelegate().
          kontoFindByPrimaryKeySmall(liefZDto.getKontoIIdWarenkonto());
      wtfZielLieferantWarenkonto.setText(kontoDtoSmall.getCNr());
      wtfZielLieferantWarenkonto.setCaretPosition(0);
      wtfZielLieferantWarenkonto.setToolTipText(kontoDtoSmall.getCNr() + " " +
                                             kontoDtoSmall.getCBez());
    }
    else {
      wtfZielLieferantWarenkonto.setText("");
      wtfZielLieferantWarenkonto.setToolTipText("");
    }


    if (liefZDto.getKontoIIdKreditorenkonto() != null) {
      KontoDto kontoDto = DelegateFactory.getInstance().getFinanzDelegate().
          kontoFindByPrimaryKey(liefZDto.getKontoIIdKreditorenkonto());
      wtfZielLieferantKreditorenkonto.setText(kontoDto.getCNr());
      wtfZielLieferantKreditorenkonto.setCaretPosition(0);
      wtfZielLieferantKreditorenkonto.setToolTipText(kontoDto.getCNr() + " " +
                                                kontoDto.getCBez());
    }
    else {
      wtfZielLieferantKreditorenkonto.setText("");
      wtfZielLieferantKreditorenkonto.setToolTipText("");
    }

    if (liefZDto.getBBeurteilen() != null) {
      wcbZielLieferantBeurteilen.setShort(liefZDto.getBBeurteilen());
    }
    else {
      wcbZielLieferantBeurteilen.setText("");
    }


    if (liefZDto.getIBeurteilung() != null) {
      wtfZielLieferantBeurteilung.setText(liefZDto.getIBeurteilung().toString());
      wtfZielLieferantBeurteilung.setCaretPosition(0);
      wtfZielLieferantBeurteilung.setToolTipText(liefZDto.getIBeurteilung().toString());
    }
    else {
      wtfZielLieferantBeurteilung.setText("");
      wtfZielLieferantBeurteilung.setToolTipText("");
    }


    if (liefZDto.getCHinweisintern() != null) {
      wtfZielKundeHinweisInt.setText(liefZDto.getCHinweisintern());
      wtfZielKundeHinweisInt.setCaretPosition(0);
      wtfZielKundeHinweisInt.setToolTipText(liefZDto.getCHinweisintern());
    }
    else {
      wtfZielKundeHinweisInt.setText("");
      wtfZielKundeHinweisInt.setToolTipText("");
    }

    if (liefZDto.getCHinweisextern() != null) {
      wtfZielKundeHinweisExt.setText(liefZDto.getCHinweisextern());
      wtfZielKundeHinweisExt.setCaretPosition(0);
      wtfZielKundeHinweisExt.setToolTipText(liefZDto.getCHinweisextern());
    }
    else {
      wtfZielKundeHinweisExt.setText("");
      wtfZielKundeHinweisExt.setToolTipText("");
    }

    if (liefZDto.getXKommentar() != null) {
      wtaZielLieferantKommentar.setText(liefZDto.getXKommentar());
      wtaZielLieferantKommentar.setCaretPosition(0);
      wtaZielLieferantKommentar.setToolTipText(liefZDto.getXKommentar());
    }
    else {
      wtaZielLieferantKommentar.setText("");
      wtaZielLieferantKommentar.setToolTipText("");
    }

    if (liefZDto.getZahlungszielIId() != null) {
      ZahlungszielDto zahlungszielDto = DelegateFactory.getInstance().getMandantDelegate().
          zahlungszielFindByPrimaryKey(liefZDto.getZahlungszielIId());
      wtfZielLieferantZahlungsziel.setText(zahlungszielDto.getCBez());
      wtfZielLieferantZahlungsziel.setCaretPosition(0);
      wtfZielLieferantZahlungsziel.setToolTipText(zahlungszielDto.getCBez());
    }
    else {
      wtfZielLieferantZahlungsziel.setText("");
      wtfZielLieferantZahlungsziel.setToolTipText("");
    }

    if (liefZDto.getLieferartIId() != null) {
      wtfZielLieferantLieferart.setText(DelegateFactory.getInstance().getLocaleDelegate().
                                    lieferartFindByPrimaryKey(liefZDto.getLieferartIId()).
                                    formatBez());
      wtfZielLieferantLieferart.setCaretPosition(0);
      wtfZielLieferantLieferart.setToolTipText(DelegateFactory.getInstance().
                                           getLocaleDelegate().lieferartFindByPrimaryKey(
          liefZDto.getLieferartIId()).formatBez());
    }
    else {
      wtfZielLieferantLieferart.setText("");
      wtfZielLieferantLieferart.setToolTipText("");
    }

    if (liefZDto.getIdSpediteur() != null) {
      SpediteurDto spediteurDto = DelegateFactory.getInstance().getMandantDelegate().
          spediteurFindByPrimaryKey(liefZDto.getIdSpediteur());
      wtfZielLieferantSpediteur.setText(spediteurDto.getCNamedesspediteurs());
      wtfZielLieferantSpediteur.setCaretPosition(0);
      wtfZielLieferantSpediteur.setToolTipText(spediteurDto.getCNamedesspediteurs());
    }
    else {
      wtfZielLieferantSpediteur.setText("");
      wtfZielLieferantSpediteur.setToolTipText("");
    }

    if (liefZDto.getNRabatt() != null) {
      BigDecimal bdRabatt = new BigDecimal(liefZDto.getNRabatt());
      wtfZielLieferantRabatt.setText(Helper.formatZahl(bdRabatt, Locale.getDefault()) +
                                     " %");
      wtfZielLieferantRabatt.setCaretPosition(0);
      wtfZielLieferantRabatt.setToolTipText(Helper.formatZahl(bdRabatt, Locale.getDefault()) +
                                            " %");
    }
    else {
      wtfZielLieferantRabatt.setText("");
      wtfZielLieferantRabatt.setToolTipText("");
    }

    if (liefZDto.getNJahrbonus() != null) {
      BigDecimal bdLimit = liefZDto.getNJahrbonus();
      wtfZielLieferantJahresbonus.setText(liefZDto.getWaehrungCNr() + " " +
                                      Helper.formatZahl(bdLimit, Locale.getDefault()));
      wtfZielLieferantJahresbonus.setCaretPosition(0);
      wtfZielLieferantJahresbonus.setToolTipText(liefZDto.getWaehrungCNr() + " " +
                                             Helper.formatZahl(bdLimit, Locale.getDefault()));
    }
    else {
      wtfZielLieferantJahresbonus.setText("");
      wtfZielLieferantJahresbonus.setToolTipText("");
    }

    if (liefZDto.getNKredit() != null) {
      BigDecimal bdLimit = liefZDto.getNKredit();
      wtfZielLieferantKreditlimit.setText(liefZDto.getWaehrungCNr() + " " +
                                      Helper.formatZahl(bdLimit, Locale.getDefault()));
      wtfZielLieferantKreditlimit.setCaretPosition(0);
      wtfZielLieferantKreditlimit.setToolTipText(liefZDto.getWaehrungCNr() + " " +
                                             Helper.formatZahl(bdLimit, Locale.getDefault()));
    }
    else {
      wtfZielLieferantKreditlimit.setText("");
      wtfZielLieferantKreditlimit.setToolTipText("");
    }

    if (liefZDto.getNAbumsatz() != null) {
      BigDecimal bdLimit = liefZDto.getNAbumsatz();
      wtfZielLieferantUmsatzgrenze.setText(liefZDto.getWaehrungCNr() + " " +
                                      Helper.formatZahl(bdLimit, Locale.getDefault()));
      wtfZielLieferantUmsatzgrenze.setCaretPosition(0);
      wtfZielLieferantUmsatzgrenze.setToolTipText(liefZDto.getWaehrungCNr() + " " +
                                             Helper.formatZahl(bdLimit, Locale.getDefault()));
    }
    else {
      wtfZielLieferantUmsatzgrenze.setText("");
      wtfZielLieferantUmsatzgrenze.setToolTipText("");
    }

    if (liefZDto.getNMindestbestellwert() != null) {
      BigDecimal bdLimit = liefZDto.getNMindestbestellwert();
      wtfZielLieferantMindestbestellwert.setText(liefZDto.getWaehrungCNr() + " " +
                                      Helper.formatZahl(bdLimit, Locale.getDefault()));
      wtfZielLieferantMindestbestellwert.setCaretPosition(0);
      wtfZielLieferantMindestbestellwert.setToolTipText(liefZDto.getWaehrungCNr() + " " +
                                             Helper.formatZahl(bdLimit, Locale.getDefault()));
    }
    else {
      wtfZielLieferantMindestbestellwert.setText("");
      wtfZielLieferantMindestbestellwert.setToolTipText("");
    }

    if (liefZDto.getNMindermengenzuschlag() != null) {
      BigDecimal bdLimit = liefZDto.getNMindermengenzuschlag();
      wtfZielLieferantMindermengenzuschlagbis.setText(liefZDto.getWaehrungCNr() + " " +
                                      Helper.formatZahl(bdLimit, Locale.getDefault()));
      wtfZielLieferantMindermengenzuschlagbis.setCaretPosition(0);
      wtfZielLieferantMindermengenzuschlagbis.setToolTipText(liefZDto.getWaehrungCNr() + " " +
                                             Helper.formatZahl(bdLimit, Locale.getDefault()));
    }
    else {
      wtfZielLieferantMindermengenzuschlagbis.setText("");
      wtfZielLieferantMindermengenzuschlagbis.setToolTipText("");
    }

    if (liefZDto.getPartnerIIdRechnungsadresse() != null) {
      PartnerDto partnerDto = null;
      if (liefZDto.getPartnerRechnungsadresseDto() != null) {
        partnerDto = liefZDto.getPartnerRechnungsadresseDto();
      }
      else {
        partnerDto = DelegateFactory.getInstance().getPartnerDelegate().
            partnerFindByPrimaryKey(liefZDto.getPartnerIIdRechnungsadresse());
      }
      wtfZielLieferantRechnungsadresspartner.setText(partnerDto.formatAdresse());
      wtfZielLieferantRechnungsadresspartner.setCaretPosition(0);
      wtfZielLieferantRechnungsadresspartner.setToolTipText(partnerDto.formatAdresse());
    }
    else {
      wtfZielLieferantRechnungsadresspartner.setText("");
      wtfZielLieferantRechnungsadresspartner.setToolTipText("");
    }


    setzeStatusVonButtonDatenrichtung();
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
    if (this.lieferantQuellDto != null && this.lieferantZielDto != null) {
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
   * je nach LieferantDtos wird der zentrale Datenrichtungsbutton enabled oder disabled gesetzt
   */
  private void setzeStatusButtonResetDatenrichtung() {
    if (this.lieferantQuellDto != null && this.lieferantZielDto != null && this.lieferantQuellDto.getIId().intValue() != this.lieferantZielDto.getIId().intValue()) {
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
     if (this.lieferantQuellDto != null && this.lieferantZielDto != null) {

       if ((wtfZielLieferantPartnername.getText() != null && wtfQuellLieferantPartnername.getText() != null &&
           wtfQuellLieferantPartnername.getText().equals(wtfZielLieferantPartnername.getText())) || (wtfZielLieferantPartnername.getText() == null && wtfQuellLieferantPartnername.getText() == null)) {
         wbuPartnernameDatenrichtung.setEnabled(false);
       }
       else {
         wbuPartnernameDatenrichtung.setEnabled(true);
       }

       if ((wtfZielLieferantMwst.getText() != null && wtfQuellLieferantMwst.getText() != null &&
           wtfQuellLieferantMwst.getText().equals(wtfZielLieferantMwst.getText())) || (wtfZielLieferantMwst.getText() == null && wtfQuellLieferantMwst.getText() == null)) {
         wbuMwstDatenrichtung.setEnabled(false);
       }
       else {
         wbuMwstDatenrichtung.setEnabled(true);
       }

       if ((wtfZielLieferantKundennr.getText() != null && wtfQuellLieferantKundennr.getText() != null &&
           wtfQuellLieferantKundennr.getText().equals(wtfZielLieferantKundennr.getText())) || (wtfZielLieferantKundennr.getText() == null && wtfQuellLieferantKundennr.getText() == null)) {
         wbuKundennrDatenrichtung.setEnabled(false);
       }
       else {
         wbuKundennrDatenrichtung.setEnabled(true);
       }

       if (wcbQuellLieferantMoeglLieferant.getShort().equals(wcbZielLieferantMoeglLieferant.getShort())) {
         wbuMoeglLieferantDatenrichtung.setEnabled(false);
       }
       else {
         wbuMoeglLieferantDatenrichtung.setEnabled(true);
       }

       if ((wtfZielLieferantWaehrung.getText() != null && wtfQuellLieferantWaehrung.getText() != null &&
           wtfQuellLieferantWaehrung.getText().equals(wtfZielLieferantWaehrung.getText())) || (wtfZielLieferantWaehrung.getText() == null && wtfQuellLieferantWaehrung.getText() == null)) {
         wbuWaehrungDatenrichtung.setEnabled(false);
       }
       else {
         wbuWaehrungDatenrichtung.setEnabled(true);
       }

       if ((wtfZielLieferantKostenstelle.getText() != null &&
           wtfQuellLieferantKostenstelle.getText() != null &&
           wtfQuellLieferantKostenstelle.getText().equals(wtfZielLieferantKostenstelle.getText())) || (wtfZielLieferantKostenstelle.getText() == null &&
           wtfQuellLieferantKostenstelle.getText() == null)) {
         wbuKostenstelleDatenrichtung.setEnabled(false);
       }
       else {
         wbuKostenstelleDatenrichtung.setEnabled(true);
       }

       if ((wtfZielLieferantWarenkonto.getText() != null && wtfQuellLieferantWarenkonto.getText() != null &&
           wtfQuellLieferantWarenkonto.getText().equals(wtfZielLieferantWarenkonto.getText())) || (wtfZielLieferantWarenkonto.getText() == null && wtfQuellLieferantWarenkonto.getText() == null)) {
         wbuWarenkontoDatenrichtung.setEnabled(false);
       }
       else {
         wbuWarenkontoDatenrichtung.setEnabled(true);
       }

       if ((wtfZielLieferantKreditorenkonto.getText() != null &&
           wtfQuellLieferantKreditorenkonto.getText() != null &&
           wtfQuellLieferantKreditorenkonto.getText().equals(wtfZielLieferantKreditorenkonto.getText())) || (wtfZielLieferantKreditorenkonto.getText() == null &&
           wtfQuellLieferantKreditorenkonto.getText() == null)) {
         wbuKreditorenkontoDatenrichtung.setEnabled(false);
       }
       else {
         wbuKreditorenkontoDatenrichtung.setEnabled(true);
       }

       if (wcbQuellLieferantBeurteilen.getShort().equals(wcbZielLieferantBeurteilen.getShort())) {
         wbuBeurteilenDatenrichtung.setEnabled(false);
       }
       else {
         wbuBeurteilenDatenrichtung.setEnabled(true);
       }

       if ((wtfZielLieferantBeurteilung.getText() != null && wtfQuellLieferantBeurteilung.getText() != null &&
           wtfQuellLieferantBeurteilung.getText().equals(wtfZielLieferantBeurteilung.getText())) || (wtfZielLieferantBeurteilung.getText() == null && wtfQuellLieferantBeurteilung.getText() == null)) {
         wbuBeurteilungDatenrichtung.setEnabled(false);
       }
       else {
         wbuBeurteilungDatenrichtung.setEnabled(true);
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

       if ((wtaZielLieferantKommentar.getText() != null && wtaQuellLieferantKommentar.getText() != null &&
           wtaQuellLieferantKommentar.getText().equals(wtaZielLieferantKommentar.getText())) || (wtaZielLieferantKommentar.getText() == null && wtaQuellLieferantKommentar.getText() == null)) {
         wbuKommentarDatenrichtung.setEnabled(false);
       }
       else {
         wbuKommentarDatenrichtung.setEnabled(true);
       }

       if ((wtfZielLieferantZahlungsziel.getText() != null &&
           wtfQuellLieferantZahlungsziel.getText() != null &&
           wtfQuellLieferantZahlungsziel.getText().equals(wtfZielLieferantZahlungsziel.getText())) || (wtfZielLieferantZahlungsziel.getText() == null &&
           wtfQuellLieferantZahlungsziel.getText() == null)) {
         wbuZahlungszielDatenrichtung.setEnabled(false);
       }
       else {
         wbuZahlungszielDatenrichtung.setEnabled(true);
       }

       if ((wtfZielLieferantLieferart.getText() != null && wtfQuellLieferantLieferart.getText() != null &&
           wtfQuellLieferantLieferart.getText().equals(wtfZielLieferantLieferart.getText())) || (wtfZielLieferantLieferart.getText() == null && wtfQuellLieferantLieferart.getText() == null)) {
         wbuLieferartDatenrichtung.setEnabled(false);
       }
       else {
         wbuLieferartDatenrichtung.setEnabled(true);
       }

       if ((wtfZielLieferantSpediteur.getText() != null && wtfQuellLieferantSpediteur.getText() != null &&
           wtfQuellLieferantSpediteur.getText().equals(wtfZielLieferantSpediteur.getText())) || (wtfZielLieferantSpediteur.getText() == null && wtfQuellLieferantSpediteur.getText() == null)) {
         wbuSpediteurDatenrichtung.setEnabled(false);
       }
       else {
         wbuSpediteurDatenrichtung.setEnabled(true);
       }

       if ((wtfZielLieferantRabatt.getText() != null && wtfQuellLieferantRabatt.getText() != null &&
           wtfQuellLieferantRabatt.getText().equals(wtfZielLieferantRabatt.getText())) || (wtfZielLieferantRabatt.getText() == null && wtfQuellLieferantRabatt.getText() == null)) {
         wbuRabattDatenrichtung.setEnabled(false);
       }
       else {
         wbuRabattDatenrichtung.setEnabled(true);
       }

       if ((wtfZielLieferantJahresbonus.getText() != null && wtfQuellLieferantJahresbonus.getText() != null &&
           wtfQuellLieferantJahresbonus.getText().equals(wtfZielLieferantJahresbonus.getText())) || (wtfZielLieferantJahresbonus.getText() == null && wtfQuellLieferantJahresbonus.getText() == null)) {
         wbuJahresbonusDatenrichtung.setEnabled(false);
       }
       else {
         wbuJahresbonusDatenrichtung.setEnabled(true);
       }

       if ((wtfZielLieferantKreditlimit.getText() != null && wtfQuellLieferantKreditlimit.getText() != null &&
           wtfQuellLieferantKreditlimit.getText().equals(wtfZielLieferantKreditlimit.getText())) || (wtfZielLieferantKreditlimit.getText() == null && wtfQuellLieferantKreditlimit.getText() == null)) {
         wbuKreditlimitDatenrichtung.setEnabled(false);
       }
       else {
         wbuKreditlimitDatenrichtung.setEnabled(true);
       }

       if ((wtfZielLieferantUmsatzgrenze.getText() != null && wtfQuellLieferantUmsatzgrenze.getText() != null &&
           wtfQuellLieferantUmsatzgrenze.getText().equals(wtfZielLieferantUmsatzgrenze.getText())) || (wtfZielLieferantUmsatzgrenze.getText() == null && wtfQuellLieferantUmsatzgrenze.getText() == null)) {
         wbuUmsatzgrenzeDatenrichtung.setEnabled(false);
       }
       else {
         wbuUmsatzgrenzeDatenrichtung.setEnabled(true);
       }

       if ((wtfZielLieferantMindestbestellwert.getText() != null && wtfQuellLieferantMindestbestellwert.getText() != null &&
           wtfQuellLieferantMindestbestellwert.getText().equals(wtfZielLieferantMindestbestellwert.getText())) || (wtfZielLieferantMindestbestellwert.getText() == null && wtfQuellLieferantMindestbestellwert.getText() == null)) {
         wbuMindestbestellwertDatenrichtung.setEnabled(false);
       }
       else {
         wbuMindestbestellwertDatenrichtung.setEnabled(true);
       }

       if ((wtfZielLieferantMindermengenzuschlagbis.getText() != null && wtfQuellLieferantMindermengenzuschlagbis.getText() != null &&
            wtfQuellLieferantMindermengenzuschlagbis.getText().equals(wtfZielLieferantMindermengenzuschlagbis.getText())) || (wtfZielLieferantMindermengenzuschlagbis.getText() == null && wtfQuellLieferantMindermengenzuschlagbis.getText() == null)) {
          wbuMindermengenzuschlagbisDatenrichtung.setEnabled(false);
       }
       else {
          wbuMindermengenzuschlagbisDatenrichtung.setEnabled(true);
       }

       if ((wtfZielLieferantRechnungsadresspartner.getText() != null &&
           wtfQuellLieferantRechnungsadresspartner.getText() != null &&
           wtfQuellLieferantRechnungsadresspartner.getText().equals(
                wtfZielLieferantRechnungsadresspartner.getText())) || (wtfZielLieferantRechnungsadresspartner.getText() == null &&
           wtfQuellLieferantRechnungsadresspartner.getText() == null)) {
         wbuRechnungsadresspartnerDatenrichtung.setEnabled(false);
       }
       else {
         wbuRechnungsadresspartnerDatenrichtung.setEnabled(true);
       }


     }
     else {
       setzeStatusAllerButtonDatenrichtung(this.vWrapperButtonsZusammenfuehren);
     }
  }

  /**
   * wird ein Quelllieferant (=Lieferant auf der rechten Seite) ausgewaehlt, so werden die Felder mit dessen Daten gefuellt
   * @param liefQDto LieferantDto
   * @throws Throwable
   */
  private void befuelleFelderMitLieferantQuellDto(LieferantDto liefQDto)
      throws Throwable {

    if (this.lieferantQuellDto != null && this.lieferantZielDto != null) {

      if (liefQDto.getPartnerDto().formatName() != null) {
        wtfQuellLieferantPartnername.setText(liefQDto.getPartnerDto().formatName());
        wtfQuellLieferantPartnername.setCaretPosition(0);
        wtfQuellLieferantPartnername.setToolTipText(liefQDto.getPartnerDto().formatName());
      }
      else {
        wtfQuellLieferantPartnername.setText("");
        wtfQuellLieferantPartnername.setToolTipText("");
      }

      if (liefQDto.getMwstsatzbezIId() != null) {
        wtfQuellLieferantMwst.setText(DelegateFactory.getInstance().getMandantDelegate().
                                  mwstsatzbezFindByPrimaryKey(liefQDto.getMwstsatzbezIId()).
                                  getCBezeichnung());
        wtfQuellLieferantMwst.setCaretPosition(0);
        wtfQuellLieferantMwst.setToolTipText(DelegateFactory.getInstance().getMandantDelegate().
                                         mwstsatzbezFindByPrimaryKey(liefQDto.getMwstsatzbezIId()).getCBezeichnung());
      }
      else {
        wtfQuellLieferantMwst.setText("");
        wtfQuellLieferantMwst.setToolTipText("");
      }

      if (liefQDto.getCKundennr() != null) {
        wtfQuellLieferantKundennr.setText(liefQDto.getCKundennr());
        wtfQuellLieferantKundennr.setCaretPosition(0);
        wtfQuellLieferantKundennr.setToolTipText(liefQDto.getCKundennr());
      }
      else {
        wtfQuellLieferantKundennr.setText("");
        wtfQuellLieferantKundennr.setToolTipText("");
      }

      if (liefQDto.getBMoeglicherLieferant() != null) {
        wcbQuellLieferantMoeglLieferant.setShort(liefQDto.getBMoeglicherLieferant());
      }
      else {
        wcbQuellLieferantMoeglLieferant.setShort(Helper.boolean2Short(false));
      }

      if (liefQDto.getWaehrungCNr() != null) {
        wtfQuellLieferantWaehrung.setText(liefQDto.getWaehrungCNr());
        wtfQuellLieferantWaehrung.setCaretPosition(0);
        wtfQuellLieferantWaehrung.setToolTipText(liefQDto.getWaehrungCNr());
      }
      else {
        wtfQuellLieferantWaehrung.setText("");
        wtfQuellLieferantWaehrung.setToolTipText("");
      }

      if (liefQDto.getIIdKostenstelle() != null) {
        KostenstelleDto kostenstelleDto = DelegateFactory.getInstance().getSystemDelegate().
            kostenstelleFindByPrimaryKey(liefQDto.getIIdKostenstelle());
        wtfQuellLieferantKostenstelle.setText(kostenstelleDto.formatKostenstellenbezeichnung());
        wtfQuellLieferantKostenstelle.setCaretPosition(0);
        wtfQuellLieferantKostenstelle.setToolTipText(kostenstelleDto.
                                                 formatKostenstellenbezeichnung());
      }
      else {
        wtfQuellLieferantKostenstelle.setText("");
        wtfQuellLieferantKostenstelle.setToolTipText("");
      }

      if (liefQDto.getKontoIIdWarenkonto() != null) {
        KontoDtoSmall kontoDtoSmall = DelegateFactory.getInstance().getFinanzDelegate().
            kontoFindByPrimaryKeySmall(liefQDto.getKontoIIdWarenkonto());
        wtfQuellLieferantWarenkonto.setText(kontoDtoSmall.getCNr());
        wtfQuellLieferantWarenkonto.setCaretPosition(0);
        wtfQuellLieferantWarenkonto.setToolTipText(kontoDtoSmall.getCNr() + " " +
                                                kontoDtoSmall.getCBez());
      }
      else {
        wtfQuellLieferantWarenkonto.setText("");
        wtfQuellLieferantWarenkonto.setToolTipText("");
      }

      if (liefQDto.getKontoIIdKreditorenkonto() != null) {
        KontoDto kontoDto = DelegateFactory.getInstance().getFinanzDelegate().
            kontoFindByPrimaryKey(liefQDto.getKontoIIdKreditorenkonto());
        wtfQuellLieferantKreditorenkonto.setText(kontoDto.getCNr());
        wtfQuellLieferantKreditorenkonto.setCaretPosition(0);
        wtfQuellLieferantKreditorenkonto.setToolTipText(kontoDto.getCNr() + " " +
            kontoDto.getCBez());
      }
      else {
        wtfQuellLieferantKreditorenkonto.setText("");
        wtfQuellLieferantKreditorenkonto.setToolTipText("");
      }

      if (liefQDto.getBBeurteilen() != null) {
        wcbQuellLieferantBeurteilen.setShort(liefQDto.getBBeurteilen());
      }
      else {
        wcbQuellLieferantBeurteilen.setShort(Helper.boolean2Short(false));
      }

      if (liefQDto.getIBeurteilung() != null) {
        wtfQuellLieferantBeurteilung.setText(liefQDto.getIBeurteilung().toString());
        wtfQuellLieferantBeurteilung.setCaretPosition(0);
        wtfQuellLieferantBeurteilung.setToolTipText(liefQDto.getIBeurteilung().toString());
      }
      else {
        wtfQuellLieferantBeurteilung.setText("");
        wtfQuellLieferantBeurteilung.setToolTipText("");
      }

      if (liefQDto.getCHinweisintern() != null) {
        wtfQuellKundeHinweisInt.setText(liefQDto.getCHinweisintern());
        wtfQuellKundeHinweisInt.setCaretPosition(0);
        wtfQuellKundeHinweisInt.setToolTipText(liefQDto.getCHinweisintern());
      }
      else {
        wtfQuellKundeHinweisInt.setText("");
        wtfQuellKundeHinweisInt.setToolTipText("");
      }

      if (liefQDto.getCHinweisextern()!= null) {
        wtfQuellKundeHinweisExt.setText(liefQDto.getCHinweisextern());
        wtfQuellKundeHinweisExt.setCaretPosition(0);
        wtfQuellKundeHinweisExt.setToolTipText(liefQDto.getCHinweisextern());
      }
      else {
        wtfQuellKundeHinweisExt.setText("");
        wtfQuellKundeHinweisExt.setToolTipText("");
      }

      if (liefQDto.getXKommentar() != null) {
        wtaQuellLieferantKommentar.setText(liefQDto.getXKommentar());
        wtaQuellLieferantKommentar.setToolTipText(liefQDto.getXKommentar());
      }
      else {
        wtaQuellLieferantKommentar.setText("");
        wtaQuellLieferantKommentar.setToolTipText("");
      }

      if (liefQDto.getZahlungszielIId() != null) {
        ZahlungszielDto zahlungszielDto = DelegateFactory.getInstance().
            getMandantDelegate().
            zahlungszielFindByPrimaryKey(liefQDto.getZahlungszielIId());
        wtfQuellLieferantZahlungsziel.setText(zahlungszielDto.getCBez());
        wtfQuellLieferantZahlungsziel.setCaretPosition(0);
        wtfQuellLieferantZahlungsziel.setToolTipText(zahlungszielDto.getCBez());
      }
      else {
        wtfQuellLieferantZahlungsziel.setText("");
        wtfQuellLieferantZahlungsziel.setToolTipText("");
      }

      if (liefQDto.getLieferartIId() != null) {
        wtfQuellLieferantLieferart.setText(DelegateFactory.getInstance().getLocaleDelegate().
                                       lieferartFindByPrimaryKey(liefQDto.getLieferartIId()).
                                       formatBez());
        wtfQuellLieferantLieferart.setCaretPosition(0);
        wtfQuellLieferantLieferart.setToolTipText(DelegateFactory.getInstance().
                                              getLocaleDelegate().
                                              lieferartFindByPrimaryKey(
                                                  liefQDto.getLieferartIId()).formatBez());
      }
      else {
        wtfQuellLieferantLieferart.setText("");
        wtfQuellLieferantLieferart.setToolTipText("");
      }

      if (liefQDto.getIdSpediteur() != null) {
        SpediteurDto spediteurDto = DelegateFactory.getInstance().getMandantDelegate().
            spediteurFindByPrimaryKey(liefQDto.getIdSpediteur());
        wtfQuellLieferantSpediteur.setText(spediteurDto.getCNamedesspediteurs());
        wtfQuellLieferantSpediteur.setCaretPosition(0);
        wtfQuellLieferantSpediteur.setToolTipText(spediteurDto.getCNamedesspediteurs());
      }
      else {
        wtfQuellLieferantSpediteur.setText("");
        wtfQuellLieferantSpediteur.setToolTipText("");
      }

      if (liefQDto.getNRabatt() != null) {
        BigDecimal bdRabatt = new BigDecimal(liefQDto.getNRabatt());
        wtfQuellLieferantRabatt.setText(Helper.formatZahl(bdRabatt, Locale.getDefault()) +
                                        " %");
        wtfQuellLieferantRabatt.setCaretPosition(0);
        wtfQuellLieferantRabatt.setToolTipText(Helper.formatZahl(bdRabatt,
            Locale.getDefault()) + " %");
      }
      else {
        wtfQuellLieferantRabatt.setText("");
        wtfQuellLieferantRabatt.setToolTipText("");
      }

      if (liefQDto.getNJahrbonus() != null) {
        BigDecimal bdLimit = liefQDto.getNJahrbonus();
        wtfQuellLieferantJahresbonus.setText(liefQDto.getWaehrungCNr() + " " +
                                         Helper.formatZahl(bdLimit, Locale.getDefault()));
        wtfQuellLieferantJahresbonus.setCaretPosition(0);
        wtfQuellLieferantJahresbonus.setToolTipText(liefQDto.getWaehrungCNr() + " " +
                                                Helper.
                                                formatZahl(bdLimit, Locale.getDefault()));
      }
      else {
        wtfQuellLieferantJahresbonus.setText("");
        wtfQuellLieferantJahresbonus.setToolTipText("");
      }

      if (liefQDto.getNKredit() != null) {
        BigDecimal bdLimit = liefQDto.getNKredit();
        wtfQuellLieferantKreditlimit.setText(liefQDto.getWaehrungCNr() + " " +
                                         Helper.formatZahl(bdLimit, Locale.getDefault()));
        wtfQuellLieferantKreditlimit.setCaretPosition(0);
        wtfQuellLieferantKreditlimit.setToolTipText(liefQDto.getWaehrungCNr() + " " +
                                                Helper.
                                                formatZahl(bdLimit, Locale.getDefault()));
      }
      else {
        wtfQuellLieferantKreditlimit.setText("");
        wtfQuellLieferantKreditlimit.setToolTipText("");
      }

      if (liefQDto.getNAbumsatz() != null) {
        BigDecimal bdLimit = liefQDto.getNAbumsatz();
        wtfQuellLieferantUmsatzgrenze.setText(liefQDto.getWaehrungCNr() + " " +
                                         Helper.formatZahl(bdLimit, Locale.getDefault()));
        wtfQuellLieferantUmsatzgrenze.setCaretPosition(0);
        wtfQuellLieferantUmsatzgrenze.setToolTipText(liefQDto.getWaehrungCNr() + " " +
                                                Helper.
                                                formatZahl(bdLimit, Locale.getDefault()));
      }
      else {
        wtfQuellLieferantUmsatzgrenze.setText("");
        wtfQuellLieferantUmsatzgrenze.setToolTipText("");
      }

      if (liefQDto.getNMindestbestellwert() != null) {
        BigDecimal bdLimit = liefQDto.getNMindestbestellwert();
        wtfQuellLieferantMindestbestellwert.setText(liefQDto.getWaehrungCNr() + " " +
                                         Helper.formatZahl(bdLimit, Locale.getDefault()));
        wtfQuellLieferantMindestbestellwert.setCaretPosition(0);
        wtfQuellLieferantMindestbestellwert.setToolTipText(liefQDto.getWaehrungCNr() + " " +
                                                Helper.
                                                formatZahl(bdLimit, Locale.getDefault()));
      }
      else {
        wtfQuellLieferantMindestbestellwert.setText("");
        wtfQuellLieferantMindestbestellwert.setToolTipText("");
      }

      if (liefQDto.getNMindermengenzuschlag() != null) {
        BigDecimal bdLimit = liefQDto.getNMindermengenzuschlag();
        wtfQuellLieferantMindermengenzuschlagbis.setText(liefQDto.getWaehrungCNr() + " " +
                                         Helper.formatZahl(bdLimit, Locale.getDefault()));
        wtfQuellLieferantMindermengenzuschlagbis.setCaretPosition(0);
        wtfQuellLieferantMindermengenzuschlagbis.setToolTipText(liefQDto.getWaehrungCNr() + " " +
                                                Helper.
                                                formatZahl(bdLimit, Locale.getDefault()));
      }
      else {
        wtfQuellLieferantMindermengenzuschlagbis.setText("");
        wtfQuellLieferantMindermengenzuschlagbis.setToolTipText("");
      }

      if (liefQDto.getPartnerIIdRechnungsadresse() != null) {
        PartnerDto partnerDto = null;
        if (liefQDto.getPartnerRechnungsadresseDto() != null) {
          partnerDto = liefQDto.getPartnerRechnungsadresseDto();
        }
        else {
          partnerDto = DelegateFactory.getInstance().getPartnerDelegate().
              partnerFindByPrimaryKey(liefQDto.getPartnerIIdRechnungsadresse());
        }
        wtfQuellLieferantRechnungsadresspartner.setText(partnerDto.formatAdresse());
        wtfQuellLieferantRechnungsadresspartner.setCaretPosition(0);
        wtfQuellLieferantRechnungsadresspartner.setToolTipText(partnerDto.formatAdresse());
      }
      else {
        wtfQuellLieferantRechnungsadresspartner.setText("");
        wtfQuellLieferantRechnungsadresspartner.setToolTipText("");
      }


      setzeStatusVonButtonDatenrichtung();
    }
    else {
      setzeStatusAllerButtonDatenrichtung(vWrapperButtonsZusammenfuehren);
    }
    setzeStatusButtonResetDatenrichtung();

  }

  public boolean handleOwnException(ExceptionLP exfc) {
     boolean bErrorErkannt = true;
     int code = exfc.getICode();
     new DialogError(LPMain.getInstance().getDesktop(), exfc, DialogError.TYPE_INFORMATION);
     return bErrorErkannt;
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
      if (this.lieferantQuellDto != null && this.lieferantZielDto != null &&
          this.lieferantQuellDto.getIId().intValue() != this.lieferantZielDto.getIId().intValue()) {
        schreibeRichtungInZielLieferant();
        try {
          DelegateFactory.getInstance().getLieferantDelegate().zusammenfuehrenLieferant(this.lieferantZielDto, this.lieferantQuellDto.getIId(), this.iLieferantPartnerIId);
          erfolgreich = true;
        }
        catch (ExceptionLP ex) {
          boolean b = handleOwnException(ex);
        }
      }
      else {
        // warnung, quelle und ziel sind identisch
        String sText = LPMain.getInstance().getTextRespectUISPr(
            "part.zusammenfuehren.warnung.lieferant.quelle.ziel.identisch");
        DialogFactory.showModalDialog(LPMain.getInstance().getTextRespectUISPr(
            "part.lieferantenzusammenfuehren"), sText);
      }
      if (erfolgreich) {
        // AuswahlFLR refreshen
        ( (InternalFrameLieferant) getInternalFrame()).getTpLieferant().getPanelLieferantenQP1().eventYouAreSelected(true);
        getInternalFrame().closePanelDialog();
      }
    }

    if (e.getActionCommand().equals(ACTION_ZIELLIEFERANT_AUSWAEHLEN)) {
      if (this.lieferantZielDto != null && this.lieferantZielDto.getIId() != null) {
        panelQueryLieferantFlr = PartnerFilterFactory.getInstance().createPanelFLRLieferantGoto(getInternalFrame(), this.lieferantZielDto.getIId(), true, false);
      }
      else if (this.lieferantQuellDto != null && this.lieferantQuellDto.getIId() != null) {
        panelQueryLieferantFlr = PartnerFilterFactory.getInstance().createPanelFLRLieferantGoto(getInternalFrame(), this.lieferantQuellDto.getIId(), true, false);
      }
      else { // selektiere ersten lieferanten
        /* ToDo: wie finde ich den ersten lieferanten in der liste oder gibt es noch ein anderes panel flr? */
      }
      new DialogQuery(panelQueryLieferantFlr);
      Object oKey = panelQueryLieferantFlr.getSelectedId();
      LieferantDto lieferantDtoTmp = DelegateFactory.getInstance().getLieferantDelegate().lieferantFindByPrimaryKey( (Integer) oKey);
      if (this.lieferantQuellDto != null && this.lieferantQuellDto.getIId() != null && this.lieferantQuellDto.getIId().intValue() == lieferantDtoTmp.getIId().intValue()) {
        String sMsg = LPMain.getInstance().getTextRespectUISPr("part.zusammenfuehren.warnung.lieferant.bereits.ausgewaehlt");
        DialogFactory.showModalDialog(LPMain.getInstance().getTextRespectUISPr("part.lieferantenzusammenfuehren"), sMsg);
      }
      else {
        this.lieferantZielDto = lieferantDtoTmp;
        befuelleFelderMitLieferantZielDto(this.lieferantZielDto);
        setSaveButtonStatus();
      }
    }

    if (e.getActionCommand().equals(ACTION_QUELLLIEFERANT_AUSWAEHLEN)) {
      if (((this.lieferantQuellDto != null && this.lieferantQuellDto.getIId() == null) || (this.lieferantQuellDto == null)) && this.lieferantZielDto != null && this.lieferantZielDto.getIId() != null) {
        panelQueryLieferantFlr = PartnerFilterFactory.getInstance().createPanelFLRLieferantGoto(getInternalFrame(), this.lieferantZielDto.getIId(), true, false);
      }
      else if (this.lieferantQuellDto.getIId() != null) {
        panelQueryLieferantFlr = PartnerFilterFactory.getInstance().createPanelFLRLieferantGoto(getInternalFrame(), this.lieferantQuellDto.getIId(), true, false);
      }
      else { // selektiere ersten lieferanten
        /* ToDo: wie finde ich den ersten lieferanten in der liste oder gibt es noch ein anderes panel flr? */
      }
      new DialogQuery(panelQueryLieferantFlr);
      Object oKey = panelQueryLieferantFlr.getSelectedId();
      LieferantDto lieferantDtoTmp = DelegateFactory.getInstance().getLieferantDelegate().lieferantFindByPrimaryKey( (Integer) oKey);
      if (this.lieferantZielDto != null && this.lieferantZielDto.getIId() != null && this.lieferantZielDto.getIId().intValue() == lieferantDtoTmp.getIId().intValue()) {
        String sMsg = LPMain.getInstance().getTextRespectUISPr("part.zusammenfuehren.warnung.lieferant.bereits.ausgewaehlt");
        DialogFactory.showModalDialog(LPMain.getInstance().getTextRespectUISPr("part.lieferantenzusammenfuehren"), sMsg);
      }
      else {
        this.lieferantQuellDto = lieferantDtoTmp;
        befuelleFelderMitLieferantQuellDto(this.lieferantQuellDto);
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
   * Schreibt die selektierten Lieferantendaten in die lieferantenZielDto
   * Einige Dateninhalte wie z.B. Anrede, Ort, etc... sind nicht kombinierbar
   * einige wie Kommentar und Hinweis sind kombinierbar
   * @throws Throwable
   */
  private void schreibeRichtungInZielLieferant() throws Throwable {
    /* alle buttons vergleichen:
         wenn der btn nach li zeigt: nix
         wenn der button nach rechts zeigt & aktiv ist: schreibe diese daten von quelle in ziel
         wenn der button kombiniert zeigt & aktiv ist: fuege daten von quelle zu ziel hinzu (evtl. laenge pruefen)
     */
    String sTmp = "";

    if (wbuPartnernameDatenrichtung.isEnabled()) {
      if (wbuPartnernameDatenrichtung.getISelection() ==  WrapperButtonZusammenfuehren.SELECT_RECHTEN_DATENINHALT) {
        this.iLieferantPartnerIId = this.lieferantQuellDto.getPartnerIId();
      }
      else if (wbuPartnernameDatenrichtung.getISelection() ==  WrapperButtonZusammenfuehren.SELECT_LINKEN_DATENINHALT) {
        this.iLieferantPartnerIId = null;
      }
    }


    if (wbuMwstDatenrichtung.isEnabled()) {
      if (wbuMwstDatenrichtung.getISelection() == WrapperButtonZusammenfuehren.SELECT_RECHTEN_DATENINHALT) {
        this.lieferantZielDto.setMwstsatzbezIId(this.lieferantQuellDto.getMwstsatzbezIId());
      }
    }


    if (wbuKundennrDatenrichtung.isEnabled()) {
      if (wbuKundennrDatenrichtung.getISelection() == WrapperButtonZusammenfuehren.SELECT_RECHTEN_DATENINHALT) {
        this.lieferantZielDto.setCKundennr(this.lieferantQuellDto.getCKundennr());
      }
    }


    if (wbuMoeglLieferantDatenrichtung.isEnabled()) {
      if (wbuMoeglLieferantDatenrichtung.getISelection() == WrapperButtonZusammenfuehren.SELECT_RECHTEN_DATENINHALT) {
        this.lieferantZielDto.setBMoeglicherLieferant(this.lieferantQuellDto.getBMoeglicherLieferant());
      }
    }


    if (wbuWaehrungDatenrichtung.isEnabled()) {
      if (wbuWaehrungDatenrichtung.getISelection() == WrapperButtonZusammenfuehren.SELECT_RECHTEN_DATENINHALT) {
        this.lieferantZielDto.setWaehrungCNr(this.lieferantQuellDto.getWaehrungCNr());
      }
    }


    if (wbuKostenstelleDatenrichtung.isEnabled()) {
      if (wbuKostenstelleDatenrichtung.getISelection() == WrapperButtonZusammenfuehren.SELECT_RECHTEN_DATENINHALT) {
        this.lieferantZielDto.setIIdKostenstelle(this.lieferantQuellDto.getIIdKostenstelle());
      }
    }


    if (wbuWarenkontoDatenrichtung.isEnabled()) {
      if (wbuWarenkontoDatenrichtung.getISelection() == WrapperButtonZusammenfuehren.SELECT_RECHTEN_DATENINHALT) {
        this.lieferantZielDto.setKontoIIdWarenkonto(this.lieferantQuellDto.getKontoIIdWarenkonto());
      }
    }


    if (wbuKreditorenkontoDatenrichtung.isEnabled()) {
      if (wbuKreditorenkontoDatenrichtung.getISelection() == WrapperButtonZusammenfuehren.SELECT_RECHTEN_DATENINHALT) {
        this.lieferantZielDto.setKontoIIdKreditorenkonto(this.lieferantQuellDto.getKontoIIdKreditorenkonto());
      }
    }


    if (wbuBeurteilenDatenrichtung.isEnabled()) {
      if (wbuBeurteilenDatenrichtung.getISelection() == WrapperButtonZusammenfuehren.SELECT_RECHTEN_DATENINHALT) {
        this.lieferantZielDto.setBBeurteilen(this.lieferantQuellDto.getBBeurteilen());
      }
    }

    if (wbuBeurteilungDatenrichtung.isEnabled()) {
      if (wbuBeurteilungDatenrichtung.getISelection() == WrapperButtonZusammenfuehren.SELECT_RECHTEN_DATENINHALT) {
        this.lieferantZielDto.setIBeurteilung(this.lieferantQuellDto.getIBeurteilung());
      }
    }

    if (wbuHinweisIntDatenrichtung.isEnabled()) {
      if (wbuHinweisIntDatenrichtung.getISelection() == WrapperButtonZusammenfuehren.SELECT_RECHTEN_DATENINHALT) {
        this.lieferantZielDto.setCHinweisintern(this.lieferantQuellDto.getCHinweisintern());
      }
      else if (wbuHinweisIntDatenrichtung.getISelection() == WrapperButtonZusammenfuehren.SELECT_KOMBINIERTE_DATENINHALTE) {
        String sTmpText = this.lieferantZielDto.getCHinweisintern() + " " + this.lieferantQuellDto.getCHinweisintern();
        if (sTmpText.length() > MAX_HINWEISINTERN) {
          sTmpText = sTmpText.substring(0, MAX_HINWEISINTERN);
        }
        this.lieferantZielDto.setCHinweisintern(sTmpText);
      }
    }

    if (wbuHinweisExtDatenrichtung.isEnabled()) {
          if (wbuHinweisExtDatenrichtung.getISelection() == WrapperButtonZusammenfuehren.SELECT_RECHTEN_DATENINHALT) {
            this.lieferantZielDto.setCHinweisextern(this.lieferantQuellDto.getCHinweisextern());
          }
          else if (wbuHinweisExtDatenrichtung.getISelection() == WrapperButtonZusammenfuehren.SELECT_KOMBINIERTE_DATENINHALTE) {
            String sTmpText = this.lieferantZielDto.getCHinweisextern() + " " + this.lieferantQuellDto.getCHinweisextern();
            if (sTmpText.length() > MAX_HINWEISEXTERN) {
              sTmpText = sTmpText.substring(0, MAX_HINWEISEXTERN);
            }
            this.lieferantZielDto.setCHinweisextern(sTmpText);
          }
    }

    if (wbuKommentarDatenrichtung.isEnabled()) {
      if (wbuKommentarDatenrichtung.getISelection() == WrapperButtonZusammenfuehren.SELECT_RECHTEN_DATENINHALT) {
        this.lieferantZielDto.setXKommentar(this.lieferantQuellDto.getXKommentar());
      }
      else if (wbuKommentarDatenrichtung.getISelection() == WrapperButtonZusammenfuehren.SELECT_KOMBINIERTE_DATENINHALTE) {
        String sTmpText = this.lieferantZielDto.getXKommentar() + " " + this.lieferantQuellDto.getXKommentar();
        if (sTmpText.length() > MAX_KOMMENTAR) {
          sTmpText = sTmpText.substring(0, MAX_KOMMENTAR);
        }
        this.lieferantZielDto.setXKommentar(sTmpText);
      }
    }

    if (wbuZahlungszielDatenrichtung.isEnabled()) {
      if (wbuZahlungszielDatenrichtung.getISelection() == WrapperButtonZusammenfuehren.SELECT_RECHTEN_DATENINHALT) {
        this.lieferantZielDto.setZahlungszielIId(this.lieferantQuellDto.getZahlungszielIId());
      }
    }

    if (wbuLieferartDatenrichtung.isEnabled()) {
      if (wbuLieferartDatenrichtung.getISelection() == WrapperButtonZusammenfuehren.SELECT_RECHTEN_DATENINHALT) {
        this.lieferantZielDto.setLieferartIId(this.lieferantQuellDto.getLieferartIId());
      }
    }

    if (wbuSpediteurDatenrichtung.isEnabled()) {
      if (wbuSpediteurDatenrichtung.getISelection() == WrapperButtonZusammenfuehren.SELECT_RECHTEN_DATENINHALT) {
        this.lieferantZielDto.setIdSpediteur(this.lieferantQuellDto.getIdSpediteur());
      }
    }

    if (wbuRabattDatenrichtung.isEnabled()) {
      if (wbuRabattDatenrichtung.getISelection() == WrapperButtonZusammenfuehren.SELECT_RECHTEN_DATENINHALT) {
        this.lieferantZielDto.setNRabatt(this.lieferantQuellDto.getNRabatt());
      }
    }

    if (wbuJahresbonusDatenrichtung.isEnabled()) {
      if (wbuJahresbonusDatenrichtung.getISelection() == WrapperButtonZusammenfuehren.SELECT_RECHTEN_DATENINHALT) {
        this.lieferantZielDto.setNJahrbonus(this.lieferantQuellDto.getNJahrbonus());
      }
    }

    if (wbuKreditlimitDatenrichtung.isEnabled()) {
      if (wbuKreditlimitDatenrichtung.getISelection() == WrapperButtonZusammenfuehren.SELECT_RECHTEN_DATENINHALT) {
        this.lieferantZielDto.setNKredit(this.lieferantQuellDto.getNKredit());
      }
    }

    if (wbuUmsatzgrenzeDatenrichtung.isEnabled()) {
      if (wbuUmsatzgrenzeDatenrichtung.getISelection() == WrapperButtonZusammenfuehren.SELECT_RECHTEN_DATENINHALT) {
        this.lieferantZielDto.setNAbumsatz(this.lieferantQuellDto.getNAbumsatz());
      }
    }

    if (wbuMindestbestellwertDatenrichtung.isEnabled()) {
      if (wbuMindestbestellwertDatenrichtung.getISelection() == WrapperButtonZusammenfuehren.SELECT_RECHTEN_DATENINHALT) {
        this.lieferantZielDto.setNMindestbestellwert(this.lieferantQuellDto.getNMindestbestellwert());
      }
    }

    if (wbuMindermengenzuschlagbisDatenrichtung.isEnabled()) {
      if (wbuMindermengenzuschlagbisDatenrichtung.getISelection() == WrapperButtonZusammenfuehren.SELECT_RECHTEN_DATENINHALT) {
        this.lieferantZielDto.setNMindermengenzuschlag(this.lieferantQuellDto.getNMindermengenzuschlag());
      }
    }

    if (wbuRechnungsadresspartnerDatenrichtung.isEnabled()) {
      if (wbuRechnungsadresspartnerDatenrichtung.getISelection() == WrapperButtonZusammenfuehren.SELECT_RECHTEN_DATENINHALT) {
        this.lieferantZielDto.setPartnerIIdRechnungsadresse(this.lieferantQuellDto.getPartnerIIdRechnungsadresse());
      }
    }


  }
}
