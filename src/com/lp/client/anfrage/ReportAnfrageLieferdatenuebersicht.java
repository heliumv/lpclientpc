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
package com.lp.client.anfrage;


import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.EventObject;

import javax.swing.JPanel;

import com.lp.client.artikel.ArtikelFilterFactory;
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
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.report.PanelReportIfJRDS;
import com.lp.client.frame.report.PanelReportKriterien;
import com.lp.client.pc.LPMain;
import com.lp.server.anfrage.service.AnfrageDto;
import com.lp.server.anfrage.service.AnfrageReportFac;
import com.lp.server.anfrage.service.ReportAnfragelieferdatenuebersichtKriterienDto;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.system.service.MailtextDto;
import com.lp.server.util.report.JasperPrintLP;


public class ReportAnfrageLieferdatenuebersicht
    extends PanelBasis implements PanelReportIfJRDS
{
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

/** Die aktuell gewaehlte Anfrage. */
  private final AnfrageDto anfrageDto;

  protected JPanel jpaWorkingOn = new JPanel();
  private GridBagLayout gridBagLayout2 = new GridBagLayout();
  private GridBagLayout gridBagLayout1 = new GridBagLayout();

  private WrapperLabel wlaZeitraum = null;
  private WrapperLabel wlaVon = null;
  private WrapperDateField wdfVon = null;
  private WrapperLabel wlaBis = null;
  private WrapperDateField wdfBis = null;
  private WrapperDateRangeController wdrBereich = null;

  private WrapperButton wbuArtikelvon = null;
  private WrapperButton wbuArtikelbis = null;
  private WrapperTextField wtfArtikelvon = null;
  private WrapperTextField wtfArtikelbis = null;

  private ArtikelDto artikelDtoVon = null;
  private PanelQueryFLR panelQueryFLRArtikelVon = null;

  private ArtikelDto artikelDtoBis = null;
  private PanelQueryFLR panelQueryFLRArtikelBis = null;

  private static final String ACTION_SPECIAL_ARTIKEL_VON = "action_special_artikel_von";
  private static final String ACTION_SPECIAL_ARTIKEL_BIS = "action_special_artikel_bis";

  private WrapperCheckBox wcbSortierungProjekt = null;
  private WrapperCheckBox wcbNurProjekt = null;
  private WrapperCheckBox wcbMitLiefermengenNull = null;

  public ReportAnfrageLieferdatenuebersicht(InternalFrame internalFrame, String add2Title,
                                            AnfrageDto anfrageDtoI)
      throws Throwable {
    super(internalFrame, add2Title);
    anfrageDto = anfrageDtoI;
    jbInit();
    initComponents();
  }


  private void jbInit()
      throws Exception {
    setLayout(gridBagLayout1);
    jpaWorkingOn.setLayout(gridBagLayout2);
    add(jpaWorkingOn,
        new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.WEST,
                               GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

    // wegen Dialogauswahl auf FLR events hoeren
    getInternalFrame().addItemChangedListener(this);

    wlaZeitraum = new WrapperLabel(LPMain.getTextRespectUISPr("anf.zeitraum"));
    wlaVon = new WrapperLabel(LPMain.getTextRespectUISPr("lp.von"));
    wdfVon = new WrapperDateField();
    wlaBis = new WrapperLabel(LPMain.getTextRespectUISPr("lp.bis"));
    wdfBis = new WrapperDateField();

    wdrBereich = new WrapperDateRangeController(wdfVon, wdfBis);
    wdrBereich.doClickUp();

    wbuArtikelvon = new WrapperButton(LPMain.getTextRespectUISPr("button.identvon"));
    wbuArtikelvon.setActionCommand(ACTION_SPECIAL_ARTIKEL_VON);
    wbuArtikelvon.addActionListener(this);

    wbuArtikelbis = new WrapperButton(LPMain.getTextRespectUISPr("button.identbis"));
    wbuArtikelbis.setActionCommand(ACTION_SPECIAL_ARTIKEL_BIS);
    wbuArtikelbis.addActionListener(this);

    wtfArtikelvon = new WrapperTextField();
    artikelDtoVon = new ArtikelDto();

    wtfArtikelbis = new WrapperTextField();
    artikelDtoBis = new ArtikelDto();

    wcbSortierungProjekt = new WrapperCheckBox(LPMain.getTextRespectUISPr(
        "anf.sortierungnachprojekt"));
    wcbSortierungProjekt.setSelected(false);

    if (anfrageDto.getCBez() != null) {
    	wcbNurProjekt = new WrapperCheckBox(LPMain.getTextRespectUISPr("anf.nurprojekt") + " " +
                                        anfrageDto.getCBez());
    }
    else {
        wcbNurProjekt = new WrapperCheckBox(LPMain.getTextRespectUISPr("anf.nurprojekt"));
        wcbNurProjekt.setEnabled(false);
    }
    wcbNurProjekt.setSelected(false);

    wcbMitLiefermengenNull = new WrapperCheckBox(LPMain.getTextRespectUISPr(
        "anf.liefermengenmitnull"));
    wcbMitLiefermengenNull.setSelected(false);

    jpaWorkingOn.add(wlaZeitraum, new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
        0, 0));

    iZeile++;
    jpaWorkingOn.add(wlaVon,
                     new GridBagConstraints(0, iZeile, 1, 1, 0.1, 0.0,
                                            GridBagConstraints.CENTER,
                                            GridBagConstraints.HORIZONTAL,
                                            new Insets(2, 2, 2, 2), 0, 0));
    jpaWorkingOn.add(wdfVon, new GridBagConstraints(1, iZeile, 1, 1, 0.1, 0.0,
        GridBagConstraints.CENTER,
        GridBagConstraints.HORIZONTAL,
        new Insets(2, 2, 2, 2), 0, 0));
    jpaWorkingOn.add(wlaBis, new GridBagConstraints(2, iZeile, 1, 1, 0.1, 0.0,
        GridBagConstraints.CENTER,
        GridBagConstraints.HORIZONTAL,
        new Insets(2, 2, 2, 2), 0, 0));
    jpaWorkingOn.add(wdfBis, new GridBagConstraints(3, iZeile, 1, 1, 0.1, 0.0,
        GridBagConstraints.CENTER,
        GridBagConstraints.HORIZONTAL,
        new Insets(2, 2, 2, 2), 0, 0));
    jpaWorkingOn.add(wdrBereich, new GridBagConstraints(4, iZeile, 1, 1, 0.0, 0.0,
        GridBagConstraints.WEST,
        GridBagConstraints.BOTH,
        new Insets(2, 2, 2, 2),
        0, 0));

    iZeile++;
    jpaWorkingOn.add(wbuArtikelvon,
                     new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0,
                                            GridBagConstraints.CENTER,
                                            GridBagConstraints.HORIZONTAL,
                                            new Insets(2, 2, 2, 2), 0, 0));
    jpaWorkingOn.add(wtfArtikelvon, new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0,
        GridBagConstraints.CENTER,
        GridBagConstraints.HORIZONTAL,
        new Insets(2, 2, 2, 2), 0, 0));
    jpaWorkingOn.add(wbuArtikelbis, new GridBagConstraints(2, iZeile, 1, 1, 0.0, 0.0,
        GridBagConstraints.CENTER,
        GridBagConstraints.HORIZONTAL,
        new Insets(2, 2, 2, 2), 0, 0));
    jpaWorkingOn.add(wtfArtikelbis, new GridBagConstraints(3, iZeile, 1, 1, 0.0, 0.0,
        GridBagConstraints.CENTER,
        GridBagConstraints.HORIZONTAL,
        new Insets(2, 2, 2, 2), 0, 0));

    iZeile++;
    jpaWorkingOn.add(wcbSortierungProjekt,
                     new GridBagConstraints(1, iZeile, 3, 1, 0.0, 0.0,
                                            GridBagConstraints.CENTER,
                                            GridBagConstraints.HORIZONTAL,
                                            new Insets(2, 2, 2, 2), 0, 0));

    iZeile++;
    jpaWorkingOn.add(wcbNurProjekt,
                     new GridBagConstraints(1, iZeile, 3, 1, 0.0, 0.0,
                                            GridBagConstraints.CENTER,
                                            GridBagConstraints.HORIZONTAL,
                                            new Insets(2, 2, 2, 2), 0, 0));

    iZeile++;
    jpaWorkingOn.add(wcbMitLiefermengenNull,
                     new GridBagConstraints(1, iZeile, 3, 1, 0.0, 0.0,
                                            GridBagConstraints.CENTER,
                                            GridBagConstraints.HORIZONTAL,
                                            new Insets(2, 2, 2, 2), 0, 0));
  }


  protected void eventActionSpecial(ActionEvent e)
      throws Throwable {
    if (e.getActionCommand().equals(ACTION_SPECIAL_ARTIKEL_VON)) {
      dialogQueryArtikelVon();
    }
    if (e.getActionCommand().equals(ACTION_SPECIAL_ARTIKEL_BIS)) {
      dialogQueryArtikelBis();
    }
  }


  private void dialogQueryArtikelVon()
      throws Throwable {
    panelQueryFLRArtikelVon = ArtikelFilterFactory.getInstance().createPanelFLRArtikel(
        getInternalFrame(), true);
    if (artikelDtoVon != null && artikelDtoVon.getIId() != null) {
      panelQueryFLRArtikelVon.setSelectedId(artikelDtoVon.getIId());
    }
    new DialogQuery(panelQueryFLRArtikelVon);
  }


  private void dialogQueryArtikelBis()
      throws Throwable {
    panelQueryFLRArtikelBis = ArtikelFilterFactory.getInstance().createPanelFLRArtikel(
        getInternalFrame(), true);
    if (artikelDtoBis != null && artikelDtoBis.getIId() != null) {
      panelQueryFLRArtikelBis.setSelectedId(artikelDtoBis.getIId());
    }
    new DialogQuery(panelQueryFLRArtikelBis);
  }


  protected void eventItemchanged(EventObject eI)
      throws Throwable {
    ItemChangedEvent e = (ItemChangedEvent) eI;
    if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
      if (e.getSource() == panelQueryFLRArtikelVon) {
        Integer iIdArtikelvon = (Integer) ( (ISourceEvent) e.getSource()).getIdSelected();
        artikelDtoVon = DelegateFactory.getInstance().getArtikelDelegate().
            artikelFindByPrimaryKey(iIdArtikelvon);
        wtfArtikelvon.setText(artikelDtoVon.getCNr());
      }
      else if (e.getSource() == panelQueryFLRArtikelBis) {
        Integer iIdArtikelbis = (Integer) ( (ISourceEvent) e.getSource()).getIdSelected();
        artikelDtoBis = DelegateFactory.getInstance().getArtikelDelegate().
            artikelFindByPrimaryKey(iIdArtikelbis);
        wtfArtikelbis.setText(artikelDtoBis.getCNr());
      }
    }
    else if (e.getID() == ItemChangedEvent.ACTION_LEEREN) {
      if (e.getSource() == panelQueryFLRArtikelVon) {
        artikelDtoVon = new ArtikelDto();
        wtfArtikelvon.setText("");
      }
      else if (e.getSource() == panelQueryFLRArtikelBis) {
        artikelDtoBis = new ArtikelDto();
        wtfArtikelbis.setText("");
      }
    }
  }


  public String getModul() {
    return AnfrageReportFac.REPORT_MODUL;
  }


  public String getReportname() {
    return AnfrageReportFac.REPORT_ANFRAGE_LIEFERDATENUEBERSICHT;
  }


  public JasperPrintLP getReport(String sDrucktype)
      throws Throwable {
    ReportAnfragelieferdatenuebersichtKriterienDto kritDto =
        new ReportAnfragelieferdatenuebersichtKriterienDto();

    kritDto.setDVon(wdfVon.getDate());
    kritDto.setDBis(wdfBis.getDate());
    kritDto.setArtikelCNrVon(wtfArtikelvon.getText());
    kritDto.setArtikelCNrBis(wtfArtikelbis.getText());

    if (wcbNurProjekt.isSelected()) {
      kritDto.setBNurProjekt(true);
      kritDto.setProjektCBez(anfrageDto.getCBez());
    }

    if (wcbMitLiefermengenNull.isSelected()) {
      kritDto.setBMitLiefermengenNull(true);
    }

    if (wcbSortierungProjekt.isSelected()) {
      kritDto.setBSortierungNachProjekt(true);
    }

    return DelegateFactory.getInstance().getAnfrageReportDelegate().printLieferdatenuebersicht(
        kritDto);
  }


  public boolean getBErstelleReportSofort() {
    return false;
  }


  public MailtextDto getMailtextDto()
      throws Throwable {
    MailtextDto mailtextDto = PanelReportKriterien.getDefaultMailtextDto(this);
    return mailtextDto;
  }
}
