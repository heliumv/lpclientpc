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
package com.lp.client.eingangsrechnung;


import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.EventObject;

import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperDateField;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperRadioButton;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.report.PanelReportIfJRDS;
import com.lp.client.frame.report.PanelReportKriterien;
import com.lp.client.partner.PartnerFilterFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.eingangsrechnung.service.EingangsrechnungReportFac;
import com.lp.server.partner.service.LieferantDto;
import com.lp.server.system.service.MailtextDto;
import com.lp.server.util.report.JasperPrintLP;


public class ReportEingangsrechnungOffene
    extends PanelBasis implements PanelReportIfJRDS
{
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

private LieferantDto lieferantDto = null;

  private final static String ACTION_SPECIAL_LIEFERANT =
      "action_special_eroffene_lieferant";
  private final static String ACTION_SPECIAL_LIEFERANT_EINER =
      "action_special_eroffene_lieferant_einer";
  private final static String ACTION_SPECIAL_LIEFERANT_ALLE =
      "action_special_eroffene_lieferant_alle";
  private final static String ACTION_SPECIAL_LIEFERANTEN =
      "action_special_eroffene_lieferanten";
  private final static String ACTION_SPECIAL_RECHNUNGSNUMMER =
      "action_special_eroffene_rechnungsnummer";

  private PanelQueryFLR panelQueryFLRLieferant = null;

  protected JPanel jpaWorkingOn = new JPanel();
  private GridBagLayout gridBagLayout2 = new GridBagLayout();
  private GridBagLayout gridBagLayout1 = new GridBagLayout();
  private WrapperDateField wdfDatum = new WrapperDateField();
  private WrapperRadioButton wrbRechnungsnummer = new WrapperRadioButton();
  private WrapperRadioButton wrbFaelligkeit = new WrapperRadioButton();
  private WrapperRadioButton wrbFaelligkeitSkonto1 = new WrapperRadioButton();
  private WrapperRadioButton wrbFaelligkeitSkonto2 = new WrapperRadioButton();
  private WrapperRadioButton wrbLieferanten = new WrapperRadioButton();
  private WrapperRadioButton wrbLieferantAlle = new WrapperRadioButton();
  private WrapperRadioButton wrbLieferantEiner = new WrapperRadioButton();
  private WrapperRadioButton wrbStichtagRechnungsdatum = new WrapperRadioButton();
  private WrapperRadioButton wrbStichtagFreigabedatum = new WrapperRadioButton();
  private ButtonGroup bgSortierung = new ButtonGroup();
  private ButtonGroup bgLieferant = new ButtonGroup();
  private ButtonGroup bgStichtag = new ButtonGroup();
  private WrapperButton wbuLieferant = new WrapperButton();
  private WrapperTextField wtfLieferant = new WrapperTextField();
  private WrapperLabel wlaSortierung = new WrapperLabel();
  private WrapperLabel wlaStichtag = new WrapperLabel();
	private WrapperCheckBox wcbNichtZugeordneteBelege = null;

	private TabbedPaneEingangsrechnung tabbedPaneEingangsrechnung = null;
  
  public ReportEingangsrechnungOffene(TabbedPaneEingangsrechnung tabbedPaneEingangsrechnung,
                                      String add2Title)
      throws Throwable {
    super(tabbedPaneEingangsrechnung.getInternalFrame(), add2Title);
    this.tabbedPaneEingangsrechnung = tabbedPaneEingangsrechnung;
    jbInit();
    setDefaults();
    initComponents();
  }


  /**
   * setDefaults
   */
  private void setDefaults() {
    wdfDatum.setDate(new java.sql.Date(System.currentTimeMillis()));
    wrbRechnungsnummer.setSelected(true);
    wrbLieferantAlle.setSelected(true);
    wrbLieferantAlle.setVisible(false);
    wrbLieferantEiner.setVisible(false);
    wbuLieferant.setVisible(false);
    wtfLieferant.setVisible(false);
    wrbStichtagFreigabedatum.setSelected(true);
    wrbStichtagRechnungsdatum.setSelected(false);
  }


  private void jbInit()
      throws Throwable {
    this.setLayout(gridBagLayout1);
    jpaWorkingOn.setLayout(gridBagLayout2);
	wcbNichtZugeordneteBelege = new WrapperCheckBox(
			LPMain.getTextRespectUISPr("rech.offene.nichtzugeordnetebelegemitdrucken"));
	wcbNichtZugeordneteBelege.setVisible(false);
	wrbLieferanten.addItemListener(new ItemListener() {
		
		@Override
		public void itemStateChanged(ItemEvent e) {
			wcbNichtZugeordneteBelege.setVisible(wrbLieferanten.isSelected());
		}
	});
	
    wdfDatum.setMandatoryField(true);
    wlaSortierung.setText(LPMain.getTextRespectUISPr("label.sortierung"));
    wlaSortierung.setHorizontalAlignment(SwingConstants.LEFT);
    wrbRechnungsnummer.setText(LPMain.getTextRespectUISPr("rechnung.rechnungsnummer"));
    wrbFaelligkeit.setText(LPMain.getTextRespectUISPr("lp.faelligkeit"));
    wrbFaelligkeitSkonto1.setText(LPMain.getTextRespectUISPr("er.faelligkeitskonto1"));
    wrbFaelligkeitSkonto2.setText(LPMain.getTextRespectUISPr("er.faelligkeitskonto2"));
    wrbLieferanten.setText(LPMain.getTextRespectUISPr("lp.lieferant.tooltip"));
    wrbLieferantAlle.setText(LPMain.getTextRespectUISPr("label.alle"));
    wrbLieferantEiner.setText(LPMain.getTextRespectUISPr("label.einer"));
    wbuLieferant.setText(LPMain.getTextRespectUISPr("button.lieferant"));
    wbuLieferant.setToolTipText(LPMain.getTextRespectUISPr("button.lieferant.tooltip"));
    wlaStichtag.setText(LPMain.getTextRespectUISPr("lp.stichtag"));
    wlaStichtag.setHorizontalAlignment(SwingConstants.LEFT);
    wrbStichtagFreigabedatum.setText(LPMain.getTextRespectUISPr("label.freigabedatum"));
    wrbStichtagRechnungsdatum.setText(LPMain.getTextRespectUISPr("label.belegdatum"));
    
    wtfLieferant.setActivatable(false);
    wrbRechnungsnummer.setMinimumSize(new Dimension(120,
        Defaults.getInstance().getControlHeight()));
    wrbRechnungsnummer.setPreferredSize(new Dimension(120,
        Defaults.getInstance().getControlHeight()));
    wrbFaelligkeit.setMinimumSize(new Dimension(120,
            Defaults.getInstance().getControlHeight()));
    wrbFaelligkeit.setPreferredSize(new Dimension(120,
            Defaults.getInstance().getControlHeight()));
    wrbFaelligkeitSkonto1.setMinimumSize(new Dimension(120,
                Defaults.getInstance().getControlHeight()));
    wrbFaelligkeitSkonto1.setPreferredSize(new Dimension(120,
                Defaults.getInstance().getControlHeight()));
    wrbFaelligkeitSkonto2.setMinimumSize(new Dimension(120,
                    Defaults.getInstance().getControlHeight()));
    wrbFaelligkeitSkonto2.setPreferredSize(new Dimension(120,
                    Defaults.getInstance().getControlHeight()));
    
    wrbLieferantAlle.setMinimumSize(new Dimension(60,
                                                  Defaults.getInstance().getControlHeight()));
    wrbLieferantAlle.setPreferredSize(new Dimension(60,
        Defaults.getInstance().getControlHeight()));
    wbuLieferant.setMinimumSize(new Dimension(100,
                                              Defaults.getInstance().getControlHeight()));
    wbuLieferant.setPreferredSize(new Dimension(100,
                                                Defaults.getInstance().getControlHeight()));

    bgLieferant.add(wrbLieferantAlle);
    bgLieferant.add(wrbLieferantEiner);
    bgSortierung.add(wrbLieferanten);
    bgSortierung.add(wrbRechnungsnummer);
    bgSortierung.add(wrbFaelligkeit);
    bgSortierung.add(wrbFaelligkeitSkonto1);
    bgSortierung.add(wrbFaelligkeitSkonto2);
    bgStichtag.add(wrbStichtagFreigabedatum);
    bgStichtag.add(wrbStichtagRechnungsdatum);

    wbuLieferant.addActionListener(this);
    wrbLieferantAlle.addActionListener(this);
    wrbLieferantEiner.addActionListener(this);
    wrbLieferanten.addActionListener(this);
    wrbRechnungsnummer.addActionListener(this);
    wrbFaelligkeit.addActionListener(this);
    wrbFaelligkeitSkonto1.addActionListener(this);
    wrbFaelligkeitSkonto2.addActionListener(this);
    wbuLieferant.setActionCommand(ACTION_SPECIAL_LIEFERANT);
    wrbLieferantAlle.setActionCommand(ACTION_SPECIAL_LIEFERANT_ALLE);
    wrbLieferantEiner.setActionCommand(ACTION_SPECIAL_LIEFERANT_EINER);
    wrbLieferanten.setActionCommand(ACTION_SPECIAL_LIEFERANTEN);
    wrbRechnungsnummer.setActionCommand(ACTION_SPECIAL_RECHNUNGSNUMMER);
    wrbFaelligkeit.setActionCommand(ACTION_SPECIAL_RECHNUNGSNUMMER);
    wrbFaelligkeitSkonto1.setActionCommand(ACTION_SPECIAL_RECHNUNGSNUMMER);
    wrbFaelligkeitSkonto2.setActionCommand(ACTION_SPECIAL_RECHNUNGSNUMMER);
    //wegen dialogFLR
    getInternalFrame().addItemChangedListener(this);

    this.add(jpaWorkingOn,
             new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
                                    GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    jpaWorkingOn.add(wlaStichtag,
                     new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0,
                                            GridBagConstraints.CENTER,
                                            GridBagConstraints.HORIZONTAL,
                                            new Insets(2, 2, 2, 2), 0, 0));
    jpaWorkingOn.add(wdfDatum,
            new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0,
                                   GridBagConstraints.CENTER,
                                   GridBagConstraints.HORIZONTAL,
                                   new Insets(2, 2, 2, 2), 0, 0));
    // damit alles stimmt ein dummy
    jpaWorkingOn.add(new WrapperLabel(),
                     new GridBagConstraints(2, iZeile, 3, 1, 1.0, 0.0,
                                            GridBagConstraints.CENTER,
                                            GridBagConstraints.HORIZONTAL,
                                            new Insets(2, 2, 2, 2), 0, 0));
    iZeile++;
    jpaWorkingOn.add(wrbStichtagFreigabedatum,
    		new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0,
    				GridBagConstraints.CENTER,
    				GridBagConstraints.HORIZONTAL,
    				new Insets(2, 2, 2, 2), 0, 0));
    jpaWorkingOn.add(wrbStichtagRechnungsdatum,
    		new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0,
    				GridBagConstraints.CENTER,
    				GridBagConstraints.HORIZONTAL,
    				new Insets(2, 2, 2, 2), 0, 0));
    jpaWorkingOn.add(wcbNichtZugeordneteBelege,
            new GridBagConstraints(2, iZeile, 3, 1, 0.0, 0.0,
                                   GridBagConstraints.CENTER,
                                   GridBagConstraints.HORIZONTAL,
                                   new Insets(2, 2, 2, 2), 0, 0));
    iZeile++;
    jpaWorkingOn.add(wlaSortierung,
                     new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0,
                                            GridBagConstraints.CENTER,
                                            GridBagConstraints.BOTH,
                                            new Insets(2, 2, 2, 2), 0, 0));
    iZeile++;
    jpaWorkingOn.add(wrbRechnungsnummer,
                     new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0,
                                            GridBagConstraints.CENTER,
                                            GridBagConstraints.HORIZONTAL,
                                            new Insets(2, 2, 2, 2), 0, 0));
    iZeile++;
    jpaWorkingOn.add(wrbFaelligkeit,
                     new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0,
                                            GridBagConstraints.CENTER,
                                            GridBagConstraints.HORIZONTAL,
                                            new Insets(2, 2, 2, 2), 0, 0));
    iZeile++;
    jpaWorkingOn.add(wrbFaelligkeitSkonto1,
                     new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0,
                                            GridBagConstraints.CENTER,
                                            GridBagConstraints.HORIZONTAL,
                                            new Insets(2, 2, 2, 2), 0, 0));
    iZeile++;
    jpaWorkingOn.add(wrbFaelligkeitSkonto2,
                     new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0,
                                            GridBagConstraints.CENTER,
                                            GridBagConstraints.HORIZONTAL,
                                            new Insets(2, 2, 2, 2), 0, 0));
    iZeile++;
    jpaWorkingOn.add(wrbLieferanten,
                     new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0,
                                            GridBagConstraints.CENTER,
                                            GridBagConstraints.HORIZONTAL,
                                            new Insets(2, 2, 2, 2), 0, 0));
    jpaWorkingOn.add(wrbLieferantAlle,
                     new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0,
                                            GridBagConstraints.CENTER,
                                            GridBagConstraints.HORIZONTAL,
                                            new Insets(2, 2, 2, 2), 0, 0));
    iZeile++;
    jpaWorkingOn.add(new WrapperLabel(),
                     new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0,
                                            GridBagConstraints.CENTER,
                                            GridBagConstraints.HORIZONTAL,
                                            new Insets(2, 2, 2, 2), 0, 0));
    jpaWorkingOn.add(wrbLieferantEiner,
                     new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0,
                                            GridBagConstraints.CENTER,
                                            GridBagConstraints.HORIZONTAL,
                                            new Insets(2, 2, 2, 2), 0, 0));
    jpaWorkingOn.add(wbuLieferant,
                     new GridBagConstraints(2, iZeile, 1, 1, 0.0, 0.0,
                                            GridBagConstraints.CENTER,
                                            GridBagConstraints.HORIZONTAL,
                                            new Insets(2, 2, 2, 2), 0, 0));
    jpaWorkingOn.add(wtfLieferant,
                     new GridBagConstraints(3, iZeile, 1, 1, 1.0, 0.0,
                                            GridBagConstraints.CENTER,
                                            GridBagConstraints.HORIZONTAL,
                                            new Insets(2, 2, 2, 2), 0, 0));
  }


  public String getModul() {
    return EingangsrechnungReportFac.REPORT_MODUL;
  }


  public String getReportname() {
    return EingangsrechnungReportFac.REPORT_EINGANGSRECHNUNG_OFFENE;
  }


  public JasperPrintLP getReport(String sDrucktype)
      throws Throwable {
    Integer lieferantIId = null;
    if (wrbLieferanten.isSelected() &&
        wrbLieferantEiner.isSelected() &&
        lieferantDto != null) {
      lieferantIId = lieferantDto.getIId();
    }
    int iSortierung=0;
    if (wrbRechnungsnummer.isSelected()) {
      iSortierung = EingangsrechnungReportFac.REPORT_OFFENE_SORT_RECHNUNGSNUMMER;
    }
    else if(wrbLieferanten.isSelected()){
      iSortierung = EingangsrechnungReportFac.REPORT_OFFENE_SORT_LIEFERANT;
    } else if(wrbFaelligkeit.isSelected()){
    	iSortierung = EingangsrechnungReportFac.REPORT_OFFENE_SORT_FAELLIGKEIT;
    } else if(wrbFaelligkeitSkonto1.isSelected()){
    	iSortierung = EingangsrechnungReportFac.REPORT_OFFENE_SORT_FAELLIGKEIT_SKONTO1;
    } else if(wrbFaelligkeitSkonto2.isSelected()){
    	iSortierung = EingangsrechnungReportFac.REPORT_OFFENE_SORT_FAELLIGKEIT_SKONTO2;
    }
    return DelegateFactory.getInstance().getEingangsrechnungDelegate().printOffene(
        iSortierung, lieferantIId,
        wdfDatum.getDate(),wrbStichtagFreigabedatum.isSelected(),tabbedPaneEingangsrechnung.isBZusatzkosten(), wcbNichtZugeordneteBelege.isSelected());
  }


  protected void eventActionSpecial(ActionEvent e)
      throws Throwable {
    if (e.getActionCommand().equals(ACTION_SPECIAL_LIEFERANT)) {
      dialogQueryLieferant();
    }
    else if (e.getActionCommand().equals(ACTION_SPECIAL_LIEFERANT_ALLE)) {
      wbuLieferant.setVisible(false);
      wtfLieferant.setVisible(false);
    }
    else if (e.getActionCommand().equals(ACTION_SPECIAL_LIEFERANT_EINER)) {
      wbuLieferant.setVisible(true);
      wtfLieferant.setVisible(true);
      if (lieferantDto == null) {
        wbuLieferant.doClick();
      }
    }
    else if (e.getActionCommand().equals(ACTION_SPECIAL_LIEFERANTEN)) {
      wrbLieferantAlle.setVisible(true);
      wrbLieferantEiner.setVisible(true);
      if (wrbLieferantEiner.isSelected()) {
        wbuLieferant.setVisible(true);
        wtfLieferant.setVisible(true);
      }
    }
    else if (e.getActionCommand().equals(ACTION_SPECIAL_RECHNUNGSNUMMER)) {
      wbuLieferant.setVisible(false);
      wtfLieferant.setVisible(false);
      wrbLieferantAlle.setVisible(false);
      wrbLieferantEiner.setVisible(false);
    }
  }


  private void dialogQueryLieferant()
      throws Throwable {
    panelQueryFLRLieferant = PartnerFilterFactory.getInstance().createPanelFLRLieferantGoto(
        getInternalFrame(),
        (lieferantDto != null) ? lieferantDto.getIId() : null,
        true,
        false);
    new DialogQuery(panelQueryFLRLieferant);
  }


  /**
   * eventItemchanged.
   *
   * @param eI EventObject
   * @throws Throwable
   */
  protected void eventItemchanged(EventObject eI)
      throws Throwable {
    ItemChangedEvent e = (ItemChangedEvent) eI;
    if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
      if (e.getSource() == panelQueryFLRLieferant) {
        try {
          Object key = ( (ISourceEvent) e.getSource()).getIdSelected();
          holeLieferant( (Integer) key);
        }
        catch (Throwable t) {
          LPMain.getInstance().exitFrame(getInternalFrame(), t);
        }
      }
    }
  }


  /**
   * Die Lieferantendaten in die Felder schreiben
   *
   * @throws Exception
   */
  private void dto2ComponentsLieferant() {
    if (lieferantDto != null) {
      this.wtfLieferant.setText(lieferantDto.getPartnerDto().formatFixTitelName1Name2());
    }
    else {
      this.wtfLieferant.setText(null);
    }
  }


  private void holeLieferant(Integer key)
      throws Throwable {
    if (key != null) {
      lieferantDto = DelegateFactory.getInstance().getLieferantDelegate().
          lieferantFindByPrimaryKey(key);
      dto2ComponentsLieferant();
    }
  }


  public boolean getBErstelleReportSofort() {
    return false;
  }


  public MailtextDto getMailtextDto()
      throws Throwable {
    MailtextDto mailtextDto = PanelReportKriterien.getDefaultMailtextDto(this);
    return mailtextDto;
  }


  protected JComponent getFirstFocusableComponent()
      throws Exception {
    return wdfDatum;
  }
}
