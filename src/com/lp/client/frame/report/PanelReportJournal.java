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
package com.lp.client.frame.report;


import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.EventObject;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperDateField;
import com.lp.client.frame.component.WrapperDateRangeController;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperRadioButton;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.system.SystemFilterFactory;
import com.lp.server.system.service.KostenstelleDto;
import com.lp.server.system.service.ReportJournalKriterienDto;
import com.lp.server.util.Facade;


/**
 * <p><I>Diese Klasse kuemmert sich ...</I> </p>
 *
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 *
 * <p>Erstellungsdatum <I>dd.mm.05</I></p>
 *
 * <p> </p>
 *
 * @author  Martin Bluehweis
 * @version $Revision: 1.4 $
 */

public abstract class PanelReportJournal
    extends PanelBasis implements PropertyChangeListener
{
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private final static String ACTION_SPECIAL_KOSTENSTELLE_ALLE =
      "action_special_kostenstelle_alle";
  private final static String ACTION_SPECIAL_KOSTENSTELLE_EINE =
      "action_special_kostenstelle_eine";
  private final static String ACTION_SPECIAL_SORTIERUNG_BELEGNUMMER =
      "action_special_sortierung_belegnummer";
  private final static String ACTION_SPECIAL_SORTIERUNG_IDENT =
      "action_special_sortierung_ident";
  private final static String ACTION_SPECIAL_KOSTENSTELLE_AUSWAHL =
      "action_special_kostenstelle_auswahl";
  private final static String ACTION_SPECIAL_SORTIERUNG_PARTNER =
      "action_special_sortierung_partner";
  protected final static String ACTION_SPECIAL_SORTIERUNG_BEREICH_DATUM =
      "action_special_sortierung_bereich_datum";
  protected final static String ACTION_SPECIAL_SORTIERUNG_BEREICH_NUMMER =
      "action_special_sortierung_bereich_nummer";
  protected final static String ACTION_SPECIAL_PARTNER_AUSWAHL =
      "action_special_partner_auswahl";
  protected final static String ACTION_SPECIAL_PARTNER_ALLE =
      "action_special_partner_alle";
  protected final static String ACTION_SPECIAL_PARTNER_EINER =
      "action_special_partner_einer";

  private boolean bKostenstelle = false;

  private final static int BREITE_SPALTE2 = 80;
  protected final static int BREITE_BUTTONS = 110;

  private PanelQueryFLR panelQueryFLRKostenstelle = null;

  protected WrapperLabel wlaKostenstellen = new WrapperLabel();
  private Border border1;
  private WrapperLabel wlaSortierung = new WrapperLabel();
  protected WrapperButton wbuKostenstelle = new WrapperButton();
  protected WrapperTextField wtfKostenstelleNummer = new WrapperTextField();
  protected WrapperTextField wtfKostenstelleBezeichnung = new WrapperTextField();
  private ButtonGroup buttonGroupKostenstelle = new ButtonGroup();
  private WrapperLabel wlaBereich = new WrapperLabel();
  protected ButtonGroup buttonGroupSortierung = new ButtonGroup();
  private ButtonGroup buttonGroupBereich = new ButtonGroup();
  private ButtonGroup buttonGroupPartner = new ButtonGroup();
  protected WrapperLabel wlaVon1 = new WrapperLabel();
  protected WrapperLabel wlaVon2 = new WrapperLabel();
  protected WrapperLabel wlaBis1 = new WrapperLabel();
  protected WrapperLabel wlaBis2 = new WrapperLabel();
  private JPanel jpaKostenstelle = null;
  protected JPanel jpaPartner = null;

  protected WrapperDateField wdfVon = new WrapperDateField();
  protected WrapperDateField wdfBis = new WrapperDateField();
  protected WrapperTextField wtfVon = new WrapperTextField();
  protected WrapperTextField wtfBis = new WrapperTextField();
  protected WrapperButton wbuPartner = new WrapperButton();
  protected WrapperTextField wtfPartner = new WrapperTextField();
  protected KostenstelleDto kostenstelleDto = null;
  protected WrapperRadioButton wrbSortierungBelegnummer = new WrapperRadioButton();
  protected WrapperRadioButton wrbSortierungPartner = new WrapperRadioButton();
  protected WrapperRadioButton wrbSortierungIdentNr = new WrapperRadioButton();
  protected WrapperRadioButton wrbKostenstelleAlle = new WrapperRadioButton();
  protected WrapperRadioButton wrbKostenstelleEine = new WrapperRadioButton();
  protected JPanel jpaWorkingOn = new JPanel();
  protected WrapperCheckBox wcbSortiereNachKostenstelle = new WrapperCheckBox();
  protected WrapperRadioButton wrbBereichDatum = new WrapperRadioButton();
  protected WrapperRadioButton wrbBereichNummer = new WrapperRadioButton();
  protected WrapperRadioButton wrbPartnerAlle = new WrapperRadioButton();
  protected WrapperRadioButton wrbPartnerEiner = new WrapperRadioButton();
  // wdrc: 1 Deklaration
  protected WrapperDateRangeController wdrBereich = null;

  protected JPanel jpaBereichDatum = null;

  public PanelReportJournal(InternalFrame internalFrame, String add2Title)
      throws Throwable {
    super(internalFrame, add2Title);
    jbInit();
    initPanel();
    setDefaults();
    initComponents();
  }


  private final void initPanel()
      throws Throwable {
    // rechte
    bKostenstelle = true;
    wlaKostenstellen.setVisible(bKostenstelle);
    wrbKostenstelleAlle.setVisible(bKostenstelle);
    wrbKostenstelleEine.setVisible(bKostenstelle);
    wcbSortiereNachKostenstelle.setVisible(bKostenstelle);
    wbuKostenstelle.setVisible(bKostenstelle);
    wtfKostenstelleBezeichnung.setVisible(bKostenstelle);
    wtfKostenstelleNummer.setVisible(bKostenstelle);
  }


  private final void setDefaults() {
    setVisibilityKostenstelle(true);
    wrbSortierungBelegnummer.setSelected(true);
    wrbSortierungIdentNr.setVisible(false);
    // datumseinschraenkung ist default sichtbar
    wrbBereichDatum.setSelected(true);
    wlaVon2.setVisible(false);
    wlaBis2.setVisible(false);
    wtfVon.setVisible(false);
    wtfBis.setVisible(false);
    // default alle Partner
    wrbPartnerAlle.setSelected(true);
    wbuPartner.setVisible(false);
    wtfPartner.setVisible(false);
    // wdrc: 4 default ist z.b. das vormonat
    wdrBereich.doClickUp();
  }


  private final void jbInit()
      throws Throwable {
    border1 = BorderFactory.createEmptyBorder(10, 10, 10, 10);
    this.setLayout(new GridBagLayout());
    wlaKostenstellen.setHorizontalAlignment(SwingConstants.LEFT);
    wlaKostenstellen.setText(LPMain.getTextRespectUISPr("label.kostenstelle"));
    wbuKostenstelle.setText(LPMain.getTextRespectUISPr("button.kostenstelle"));
    wbuKostenstelle.setToolTipText(LPMain.getTextRespectUISPr("button.kostenstelle.tooltip"));
    wbuPartner.setMinimumSize(new Dimension(BREITE_BUTTONS,
                                            Defaults.getInstance().getControlHeight()));
    wbuPartner.setPreferredSize(new Dimension(BREITE_BUTTONS,
                                              Defaults.getInstance().getControlHeight()));
    wrbKostenstelleAlle.setMinimumSize(new Dimension(BREITE_SPALTE2,
        Defaults.getInstance().getControlHeight()));
    wrbKostenstelleAlle.setPreferredSize(new Dimension(BREITE_SPALTE2,
        Defaults.getInstance().getControlHeight()));
    wrbBereichDatum.setMinimumSize(new Dimension(BREITE_SPALTE2,
                                                 Defaults.getInstance().getControlHeight()));
    wrbBereichDatum.setPreferredSize(new Dimension(BREITE_SPALTE2,
        Defaults.getInstance().getControlHeight()));

    wbuKostenstelle.setMinimumSize(new Dimension(BREITE_BUTTONS,
                                                 Defaults.getInstance().getControlHeight()));
    wbuKostenstelle.setPreferredSize(new Dimension(BREITE_BUTTONS,
        Defaults.getInstance().getControlHeight()));
    wtfKostenstelleNummer.setMinimumSize(new Dimension(50,
        Defaults.getInstance().getControlHeight()));
    wtfKostenstelleNummer.setPreferredSize(new Dimension(50,
        Defaults.getInstance().getControlHeight()));
    jpaWorkingOn.setLayout(new GridBagLayout());
    wrbKostenstelleAlle.setSelected(true);
    wrbKostenstelleAlle.setText(LPMain.getTextRespectUISPr("label.alle"));
    wrbKostenstelleEine.setText(LPMain.getTextRespectUISPr("label.eine"));
    wrbPartnerAlle.setText(LPMain.getTextRespectUISPr("label.alle"));
    wrbPartnerEiner.setText(LPMain.getTextRespectUISPr("label.einer"));
    wlaVon1.setText(LPMain.getTextRespectUISPr("lp.von"));
    wlaVon2.setText(LPMain.getTextRespectUISPr("lp.von"));
    wlaBis1.setText(LPMain.getTextRespectUISPr("lp.bis"));
    wlaBis2.setText(LPMain.getTextRespectUISPr("lp.bis"));
    wlaVon1.setMinimumSize(new Dimension(25, Defaults.getInstance().getControlHeight()));
    wlaVon1.setPreferredSize(new Dimension(25, Defaults.getInstance().getControlHeight()));
    wlaVon2.setMinimumSize(new Dimension(25, Defaults.getInstance().getControlHeight()));
    wlaVon2.setPreferredSize(new Dimension(25, Defaults.getInstance().getControlHeight()));
    wlaBis1.setMinimumSize(new Dimension(25, Defaults.getInstance().getControlHeight()));
    wlaBis1.setPreferredSize(new Dimension(25, Defaults.getInstance().getControlHeight()));
    wlaBis2.setMinimumSize(new Dimension(25, Defaults.getInstance().getControlHeight()));
    wlaBis2.setPreferredSize(new Dimension(25, Defaults.getInstance().getControlHeight()));

    wlaSortierung.setMinimumSize(new Dimension(90,
                                               Defaults.getInstance().getControlHeight()));
    wlaSortierung.setPreferredSize(new Dimension(90,
                                                 Defaults.getInstance().getControlHeight()));
    wlaSortierung.setHorizontalAlignment(SwingConstants.LEFT);
    wlaSortierung.setText(LPMain.getTextRespectUISPr("lp.sortierung"));
    wlaBereich.setHorizontalAlignment(SwingConstants.LEFT);
    wlaBereich.setText(LPMain.getTextRespectUISPr("label.bereich"));
    jpaWorkingOn.setBorder(border1);
    wcbSortiereNachKostenstelle.setText(LPMain.getTextRespectUISPr(
        "label.sortierungnachkostenstellen"));
    wrbSortierungBelegnummer.setText(LPMain.getTextRespectUISPr("label.belegnummer"));
    wrbSortierungIdentNr.setText(LPMain.getTextRespectUISPr("auftrag.ident"));
    wrbBereichDatum.setText(LPMain.getTextRespectUISPr("lp.datum"));
    wrbBereichNummer.setText(LPMain.getTextRespectUISPr("label.belegnummer"));
    wtfPartner.setActivatable(false);
    wtfKostenstelleBezeichnung.setActivatable(false);
    wtfKostenstelleNummer.setActivatable(false);
    wtfPartner.setEditable(false);
    wtfPartner.setColumnsMax(Facade.MAX_UNBESCHRAENKT);
    wtfKostenstelleBezeichnung.setEditable(false);
    wtfKostenstelleNummer.setEditable(false);

    wrbKostenstelleAlle.setActionCommand(ACTION_SPECIAL_KOSTENSTELLE_ALLE);
    wrbKostenstelleEine.setActionCommand(ACTION_SPECIAL_KOSTENSTELLE_EINE);
    wrbSortierungBelegnummer.setActionCommand(ACTION_SPECIAL_SORTIERUNG_BELEGNUMMER);
    wrbSortierungIdentNr.setActionCommand(ACTION_SPECIAL_SORTIERUNG_IDENT);
    wrbSortierungPartner.setActionCommand(ACTION_SPECIAL_SORTIERUNG_PARTNER);
    wbuKostenstelle.setActionCommand(ACTION_SPECIAL_KOSTENSTELLE_AUSWAHL);
    wrbBereichDatum.setActionCommand(ACTION_SPECIAL_SORTIERUNG_BEREICH_DATUM);
    wrbBereichNummer.setActionCommand(ACTION_SPECIAL_SORTIERUNG_BEREICH_NUMMER);
    wrbPartnerAlle.setActionCommand(ACTION_SPECIAL_PARTNER_ALLE);
    wrbPartnerEiner.setActionCommand(ACTION_SPECIAL_PARTNER_EINER);
    wbuPartner.setActionCommand(ACTION_SPECIAL_PARTNER_AUSWAHL);

    wrbKostenstelleAlle.addActionListener(this);
    wrbKostenstelleEine.addActionListener(this);
    wrbSortierungBelegnummer.addActionListener(this);
    wrbSortierungIdentNr.addActionListener(this);
    wrbSortierungPartner.addActionListener(this);
    wbuKostenstelle.addActionListener(this);
    wrbBereichDatum.addActionListener(this);
    wrbBereichNummer.addActionListener(this);
    wrbPartnerAlle.addActionListener(this);
    wrbPartnerEiner.addActionListener(this);
    wbuPartner.addActionListener(this);

//    wdfVon.getDisplay().addPropertyChangeListener(this);
//    wdfBis.getDisplay().addPropertyChangeListener(this);

    // wdrc: 2 der DateRangeController muss die beiden DateFields kennen
    wdrBereich = new WrapperDateRangeController(wdfVon, wdfBis);

    //PJ 14057
    wdfVon.setName("wdfVon");
    wdfBis.setName("wdfBis");
    wcbSortiereNachKostenstelle.setName("wcbSortiereNachKostenstelle");
    wrbBereichDatum.setName("wrbBereichDatum");
    wrbBereichNummer.setName("wrbBereichDatum");
    wrbKostenstelleAlle.setName("wrbKostenstelleAlle");
    wrbKostenstelleEine.setName("wrbKostenstelleEine");
    wrbPartnerAlle.setName("wrbPartnerAlle");
    wrbPartnerEiner.setName("wrbPartnerEiner");
    wrbSortierungBelegnummer.setName("wrbSortierungBelegnummer");
    wrbSortierungIdentNr.setName("wrbSortierungIdentNr");
    wrbSortierungPartner.setName("wrbSortierungPartner");
    
    //wegen dialogFLR
    getInternalFrame().addItemChangedListener(this);

    this.add(jpaWorkingOn,
             new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.WEST,
                                    GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    jpaWorkingOn.add(wlaKostenstellen,
                     new GridBagConstraints(0, iZeile, 4, 1, 1.0, 0.0,
                                            GridBagConstraints.WEST,
                                            GridBagConstraints.BOTH,
                                            new Insets(2, 2, 2, 2),
                                            0, 0));
    jpaWorkingOn.add(wrbKostenstelleAlle,
                     new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0,
                                            GridBagConstraints.WEST,
                                            GridBagConstraints.BOTH,
                                            new Insets(2, 2, 2, 2),
                                            0, 0));
    jpaWorkingOn.add(wcbSortiereNachKostenstelle,
                     new GridBagConstraints(2, iZeile, 5, 1, 0.0, 0.0,
                                            GridBagConstraints.WEST,
                                            GridBagConstraints.BOTH,
                                            new Insets(2, 2, 2, 2),
                                            0, 0));
    iZeile++;
    jpaWorkingOn.add(wrbKostenstelleEine,
                     new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0,
                                            GridBagConstraints.WEST,
                                            GridBagConstraints.BOTH,
                                            new Insets(2, 2, 2, 2),
                                            0, 0));
    jpaWorkingOn.add(getPanelKostenstelle(),
                     new GridBagConstraints(2, iZeile, 4, 1, 0.0, 0.0,
                                            GridBagConstraints.WEST,
                                            GridBagConstraints.BOTH,
                                            new Insets(0, 0, 0, 0),
                                            0, 0));
    iZeile++;
    jpaWorkingOn.add(wlaSortierung,
                     new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0,
                                            GridBagConstraints.WEST,
                                            GridBagConstraints.BOTH,
                                            new Insets(2, 2, 2, 2),
                                            0, 0));
    jpaWorkingOn.add(wlaBereich,
                     new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0,
                                            GridBagConstraints.WEST,
                                            GridBagConstraints.BOTH,
                                            new Insets(2, 2, 2, 2),
                                            0, 0));
    iZeile++;
    jpaWorkingOn.add(wrbSortierungBelegnummer,
                     new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0,
                                            GridBagConstraints.WEST,
                                            GridBagConstraints.BOTH,
                                            new Insets(2, 2, 2, 2),
                                            0, 0));
    jpaWorkingOn.add(wrbBereichDatum,
                     new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0,
                                            GridBagConstraints.WEST,
                                            GridBagConstraints.BOTH,
                                            new Insets(2, 2, 2, 2),
                                            0, 0));
    jpaWorkingOn.add(getJPanelBereichDatum(),
                     new GridBagConstraints(2, iZeile, 5, 1, 0.0, 0.0,
                                            GridBagConstraints.WEST,
                                            GridBagConstraints.BOTH,
                                            new Insets(0, 0, 0, 0),
                                            0, 0));
    iZeile++;
    jpaWorkingOn.add(wrbBereichNummer,
                     new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0,
                                            GridBagConstraints.WEST,
                                            GridBagConstraints.BOTH,
                                            new Insets(2, 2, 2, 2),
                                            0, 0));
    jpaWorkingOn.add(wlaVon2,
                     new GridBagConstraints(2, iZeile, 1, 1, 0.0, 0.0,
                                            GridBagConstraints.WEST,
                                            GridBagConstraints.BOTH,
                                            new Insets(2, 2, 2, 2),
                                            0, 0));
    jpaWorkingOn.add(wtfVon,
                     new GridBagConstraints(3, iZeile, 1, 1, 1.0, 0.0,
                                            GridBagConstraints.WEST,
                                            GridBagConstraints.BOTH,
                                            new Insets(2, 2, 2, 2),
                                            0, 0));
    jpaWorkingOn.add(wlaBis2,
                     new GridBagConstraints(4, iZeile, 1, 1, 0.0, 0.0,
                                            GridBagConstraints.WEST,
                                            GridBagConstraints.BOTH,
                                            new Insets(2, 2, 2, 2),
                                            0, 0));
    jpaWorkingOn.add(wtfBis,
                     new GridBagConstraints(5, iZeile, 1, 1, 1.0, 0.0,
                                            GridBagConstraints.WEST,
                                            GridBagConstraints.BOTH,
                                            new Insets(2, 2, 2, 2),
                                            0, 0));
    iZeile++;
    jpaWorkingOn.add(wrbSortierungPartner,
                     new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0,
                                            GridBagConstraints.WEST,
                                            GridBagConstraints.BOTH,
                                            new Insets(2, 2, 2, 2),
                                            0, 0));
    jpaWorkingOn.add(wrbPartnerAlle,
                     new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0,
                                            GridBagConstraints.WEST,
                                            GridBagConstraints.BOTH,
                                            new Insets(2, 2, 2, 2),
                                            0, 0));
    iZeile++;
    jpaWorkingOn.add(wrbPartnerEiner,
                     new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0,
                                            GridBagConstraints.WEST,
                                            GridBagConstraints.BOTH,
                                            new Insets(2, 2, 2, 2),
                                            0, 0));
    jpaWorkingOn.add(getPanelPartner(),
                     new GridBagConstraints(2, iZeile, 4, 1, 0.0, 0.0,
                                            GridBagConstraints.WEST,
                                            GridBagConstraints.BOTH,
                                            new Insets(0, 0, 0, 0),
                                            0, 0));
    iZeile++;
    jpaWorkingOn.add(wrbSortierungIdentNr,
                     new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0,
                                            GridBagConstraints.WEST,
                                            GridBagConstraints.BOTH,
                                            new Insets(2, 2, 2, 2),
                                            0, 0));

    iZeile++;
    buttonGroupKostenstelle.add(wrbKostenstelleAlle);
    buttonGroupKostenstelle.add(wrbKostenstelleEine);
    buttonGroupSortierung.add(wrbSortierungBelegnummer);
    buttonGroupSortierung.add(wrbSortierungPartner);
    buttonGroupSortierung.add(wrbSortierungIdentNr);
    buttonGroupBereich.add(wrbBereichDatum);
    buttonGroupBereich.add(wrbBereichNummer);
    buttonGroupPartner.add(wrbPartnerAlle);
    buttonGroupPartner.add(wrbPartnerEiner);
  }


  protected void eventActionSpecial(ActionEvent e)
      throws Throwable {
    if (e.getActionCommand().equals(ACTION_SPECIAL_KOSTENSTELLE_ALLE)) {
      setVisibilityKostenstelle(true);
    }
    else if (e.getActionCommand().equals(ACTION_SPECIAL_KOSTENSTELLE_EINE)) {
      setVisibilityKostenstelle(false);
      // wenn noch keine gewaehlt, dann geht der dialog auf
      if (kostenstelleDto == null) {
        wbuKostenstelle.doClick();
      }
    }
    else if (e.getActionCommand().equals(ACTION_SPECIAL_SORTIERUNG_BELEGNUMMER)) {
      // nix tun
    }
    else if (e.getActionCommand().equals(ACTION_SPECIAL_SORTIERUNG_PARTNER)) {
      // nix tun
    }
    else if (e.getActionCommand().equals(ACTION_SPECIAL_SORTIERUNG_IDENT)) {
      // nix tun
    }
    else if (e.getActionCommand().equals(ACTION_SPECIAL_KOSTENSTELLE_AUSWAHL)) {
      dialogQueryKostenstelle();
    }
    else if (e.getActionCommand().equals(ACTION_SPECIAL_PARTNER_ALLE)) {
      wbuPartner.setVisible(false);
      wtfPartner.setVisible(false);
      wtfPartner.setMandatoryField(false);
    }
    else if (e.getActionCommand().equals(ACTION_SPECIAL_PARTNER_EINER)) {
      wbuPartner.setVisible(true);
      wtfPartner.setVisible(true);
      wtfPartner.setMandatoryField(true);
    }
    else if (e.getActionCommand().equals(ACTION_SPECIAL_SORTIERUNG_BEREICH_DATUM)) {
      wlaVon1.setVisible(true);
      wlaBis1.setVisible(true);
      wdfVon.setVisible(true);
      wdfBis.setVisible(true);
      wdrBereich.setVisible(true);
      wlaVon2.setVisible(false);
      wlaBis2.setVisible(false);
      wtfVon.setVisible(false);
      wtfBis.setVisible(false);
    }
    else if (e.getActionCommand().equals(ACTION_SPECIAL_SORTIERUNG_BEREICH_NUMMER)) {
      wlaVon1.setVisible(false);
      wlaBis1.setVisible(false);
      wdfVon.setVisible(false);
      wdfBis.setVisible(false);
      wdrBereich.setVisible(false);
      wlaVon2.setVisible(true);
      wlaBis2.setVisible(true);
      wtfVon.setVisible(true);
      wtfBis.setVisible(true);
    }
  }


  private void setVisibilityKostenstelle(boolean alle) {
    wcbSortiereNachKostenstelle.setVisible(alle && bKostenstelle);
    wbuKostenstelle.setVisible(!alle && bKostenstelle);
    wtfKostenstelleNummer.setVisible(!alle && bKostenstelle);
    wtfKostenstelleNummer.setMandatoryField(!alle && bKostenstelle);
    wtfKostenstelleBezeichnung.setVisible(!alle && bKostenstelle);
  }


  private void dialogQueryKostenstelle()
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
        if (key != null) {
          kostenstelleDto = DelegateFactory.getInstance().getSystemDelegate().
              kostenstelleFindByPrimaryKey( (Integer) key);
          dto2ComponentsKostenstelle();
        }
      }
    }
  }


  /**
   * Traegt die Daten fuer die Kostenstelle ein.
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


  public void propertyChange(PropertyChangeEvent evt) {
    if (evt.getSource() == wdfVon.getDisplay() && evt.getPropertyName().equals("date")) {
      wdfBis.setMinimumValue(wdfVon.getDate());
    }
    else if (evt.getSource() == wdfBis.getDisplay() &&
             evt.getPropertyName().equals("date")) {
      wdfVon.setMaximumValue(wdfBis.getDate());
    }
  }


  private JPanel getPanelKostenstelle() {
    if (jpaKostenstelle == null) {
      jpaKostenstelle = new JPanel(new GridBagLayout());
      jpaKostenstelle.add(wbuKostenstelle,
                          new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                                                 GridBagConstraints.WEST,
                                                 GridBagConstraints.BOTH,
                                                 new Insets(2, 2, 2, 2),
                                                 0, 0));
      jpaKostenstelle.add(wtfKostenstelleNummer,
                          new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                                                 GridBagConstraints.WEST,
                                                 GridBagConstraints.BOTH,
                                                 new Insets(2, 2, 2, 2),
                                                 0, 0));
      jpaKostenstelle.add(wtfKostenstelleBezeichnung,
                          new GridBagConstraints(2, 0, 1, 1, 0.1, 0.0,
                                                 GridBagConstraints.WEST,
                                                 GridBagConstraints.BOTH,
                                                 new Insets(2, 2, 2, 2),
                                                 0, 0));
    }
    return jpaKostenstelle;
  }


  private JPanel getPanelPartner() {
    if (jpaPartner == null) {
      jpaPartner = new JPanel(new GridBagLayout());
      jpaPartner.add(wbuPartner,
                     new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                                            GridBagConstraints.WEST,
                                            GridBagConstraints.BOTH,
                                            new Insets(2, 2, 2, 2),
                                            0, 0));
      jpaPartner.add(wtfPartner,
                     new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0,
                                            GridBagConstraints.WEST,
                                            GridBagConstraints.BOTH,
                                            new Insets(2, 2, 2, 2),
                                            0, 0));
    }
    return jpaPartner;
  }


  protected void befuelleKriterien(ReportJournalKriterienDto krit) {
    if (wrbKostenstelleAlle.isSelected()) {
      krit.bSortiereNachKostenstelle = wcbSortiereNachKostenstelle.isSelected();
    }
    else {
      krit.bSortiereNachKostenstelle = false;
    }
    if (wrbKostenstelleEine.isSelected()) {
      krit.kostenstelleIId = kostenstelleDto.getIId();
    }
    if (wrbBereichDatum.isSelected()) {
      krit.dVon = wdfVon.getDate();
      krit.dBis = wdfBis.getDate();
    }
    else if (wrbBereichNummer.isSelected()) {
      krit.sBelegnummerVon = wtfVon.getText();
      krit.sBelegnummerBis = wtfBis.getText();
    }
    if (wrbSortierungBelegnummer.isSelected()) {
      krit.iSortierung = ReportJournalKriterienDto.KRIT_SORT_NACH_BELEGNUMMER;
    }
    else if (wrbSortierungPartner.isSelected()) {
      krit.iSortierung = ReportJournalKriterienDto.KRIT_SORT_NACH_PARTNER;
    }
    else if (wrbSortierungIdentNr.isSelected()) {
      krit.iSortierung = ReportJournalKriterienDto.KRIT_SORT_NACH_IDENT;
    }
  }


  protected void setEinschraenkungDatumBelegnummerSichtbar(boolean bSichtbar) {
    wrbBereichDatum.setVisible(bSichtbar);
    wrbBereichNummer.setVisible(bSichtbar);
    wlaBis1.setVisible(bSichtbar);
    wlaVon1.setVisible(bSichtbar);
    wlaBis2.setVisible(bSichtbar);
    wlaVon2.setVisible(bSichtbar);
    wdfBis.setVisible(bSichtbar);
    wdfVon.setVisible(bSichtbar);
    wtfBis.setVisible(bSichtbar);
    wtfVon.setVisible(bSichtbar);
    wdrBereich.setVisible(bSichtbar);
    wlaBereich.setVisible(bSichtbar);
    if (!bSichtbar) {
      wdfBis.setDate(null);
      wdfVon.setDate(null);
      wtfBis.setText(null);
      wtfVon.setText(null);
    }
  }


  protected void setEinschraenkungKostenstelleSichtbar(boolean bSichtbar) {
    wlaKostenstellen.setVisible(bSichtbar);
    wbuKostenstelle.setVisible(bSichtbar);
    wtfKostenstelleNummer.setVisible(bSichtbar);
    wtfKostenstelleBezeichnung.setVisible(bSichtbar);
    wrbKostenstelleAlle.setVisible(bSichtbar);
    wrbKostenstelleEine.setVisible(bSichtbar);
    wcbSortiereNachKostenstelle.setVisible(bSichtbar);

  }

  protected JComponent getFirstFocusableComponent()
      throws Exception {
    return wrbKostenstelleAlle;
  }


  private JPanel getJPanelBereichDatum() {
    jpaBereichDatum = new JPanel(new GridBagLayout());
    jpaBereichDatum.add(wlaVon1,
                        new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0,
                                               GridBagConstraints.WEST,
                                               GridBagConstraints.BOTH,
                                               new Insets(2, 2, 2, 2),
                                               0, 0));
    jpaBereichDatum.add(wdfVon,
                        new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0,
                                               GridBagConstraints.WEST,
                                               GridBagConstraints.BOTH,
                                               new Insets(2, 2, 2, 2),
                                               0, 0));
    jpaBereichDatum.add(wlaBis1,
                        new GridBagConstraints(2, iZeile, 1, 1, 0.0, 0.0,
                                               GridBagConstraints.WEST,
                                               GridBagConstraints.BOTH,
                                               new Insets(2, 2, 2, 2),
                                               0, 0));
    jpaBereichDatum.add(wdfBis,
                        new GridBagConstraints(3, iZeile, 1, 1, 0.0, 0.0,
                                               GridBagConstraints.WEST,
                                               GridBagConstraints.BOTH,
                                               new Insets(2, 2, 2, 2),
                                               0, 0));
    // wdrc: 3 position rechts neben den datefields
    jpaBereichDatum.add(wdrBereich,
                        new GridBagConstraints(4, iZeile, 1, 1, 0.0, 0.0,
                                               GridBagConstraints.WEST,
                                               GridBagConstraints.BOTH,
                                               new Insets(2, 2, 2, 2),
                                               0, 0));
    // rechts mit einem Label auffuellen, damit alles links ausgerichtet ist
    jpaBereichDatum.add(new JLabel(),
                        new GridBagConstraints(5, iZeile, 1, 1, 1.0, 0.0,
                                               GridBagConstraints.WEST,
                                               GridBagConstraints.BOTH,
                                               new Insets(2, 2, 2, 2),
                                               0, 0));

    return jpaBereichDatum;
  }
}
