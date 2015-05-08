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
package com.lp.client.reklamation;


import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.EventObject;

import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

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
import com.lp.client.frame.report.PanelReportIfJRDS;
import com.lp.client.frame.report.PanelReportKriterien;
import com.lp.client.pc.LPMain;
import com.lp.client.system.SystemFilterFactory;
import com.lp.server.reklamation.service.ReklamationReportFac;
import com.lp.server.system.service.KostenstelleDto;
import com.lp.server.system.service.MailtextDto;
import com.lp.server.util.report.JasperPrintLP;

@SuppressWarnings("static-access")
public class ReportReklamationsjournal
    extends PanelBasis implements PanelReportIfJRDS
{
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
protected JPanel jpaWorkingOn = new JPanel();
  private GridBagLayout gridBagLayout2 = new GridBagLayout();
  private GridBagLayout gridBagLayout1 = new GridBagLayout();
  static final public String ACTION_SPECIAL_KOSTENSTELLE_FROM_LISTE =
      "ACTION_SPECIAL_KOSTENSTELLE_FROM_LISTE";

  private PanelQueryFLR panelQueryFLRKostenstelle = null;

  private Integer kostenstelleIId = null;
  private WrapperButton wbuKostenstelle = new WrapperButton();
  private WrapperTextField wtfKostenstelle = new WrapperTextField();

  private WrapperLabel wlaSortierung = new WrapperLabel();

  private WrapperLabel wlaDatumVon = new WrapperLabel();
  private WrapperDateField wdfDatumVon = new WrapperDateField();

  private WrapperLabel wlaDatumBis = new WrapperLabel();
  private WrapperDateField wdfDatumBis = new WrapperDateField();

  private ButtonGroup buttonGroupSortierung = new ButtonGroup();
  private WrapperRadioButton wrbKundeLieferant = new WrapperRadioButton();
  private WrapperRadioButton wrBelegnummer = new WrapperRadioButton();
  private WrapperRadioButton wrbFehler = new WrapperRadioButton();

  private WrapperCheckBox wcbNurOffene = new WrapperCheckBox();

  private WrapperCheckBox wcbKunde = new WrapperCheckBox();
  private WrapperCheckBox wcbLieferant = new WrapperCheckBox();
  private WrapperCheckBox wcbLos = new WrapperCheckBox();

  private WrapperDateRangeController wdrBereich = null;

  public ReportReklamationsjournal(InternalFrame internalFrame, String add2Title)
      throws Throwable {
    super(internalFrame, add2Title);
    LPMain.getInstance().getTextRespectUISPr("rekla.reklamationsjournal");
    jbInit();
    initComponents();
    wdrBereich.doClickUp();
  }


  protected JComponent getFirstFocusableComponent()
      throws Exception {
    return wdfDatumVon;
  }


  private void jbInit()
      throws Throwable {
    this.setLayout(gridBagLayout1);
    jpaWorkingOn.setLayout(gridBagLayout2);

    wlaSortierung.setText(LPMain.getInstance().getTextRespectUISPr("lp.sortierung"));
    wlaSortierung.setHorizontalAlignment(SwingConstants.LEFT);
    wlaDatumVon.setText(LPMain.getInstance().getTextRespectUISPr("lp.von"));
    wlaDatumBis.setText(LPMain.getInstance().getTextRespectUISPr("lp.bis"));
    wdfDatumVon.setTimestamp(new java.sql.Timestamp(System.currentTimeMillis()));
    wdfDatumVon.setMandatoryField(true);
    wdfDatumBis.setMandatoryField(true);

    wbuKostenstelle.setText(LPMain.getInstance().getTextRespectUISPr(
        "button.kostenstelle"));

    wtfKostenstelle.setEditable(false);

    wdrBereich = new WrapperDateRangeController(wdfDatumVon, wdfDatumBis);

    wrbFehler.setText(LPMain.getInstance().getTextRespectUISPr("rekla.fehler"));
    wrbKundeLieferant.setText(LPMain.getInstance().getTextRespectUISPr("lp.kunde.modulname")+"/ "+LPMain.getInstance().getTextRespectUISPr("label.lieferant"));
    wrBelegnummer.setText(LPMain.getInstance().getTextRespectUISPr("label.belegnummer"));

    wcbNurOffene.setText(LPMain.getInstance().getTextRespectUISPr("lp.nuroffene"));

    wcbKunde.setText(LPMain.getInstance().getTextRespectUISPr("lp.kunde"));
    wcbLieferant.setText(LPMain.getInstance().getTextRespectUISPr("lp.lieferant"));
    wcbLos.setText(LPMain.getInstance().getTextRespectUISPr("fert.modulname"));

    wcbKunde.setSelected(true);
    wcbLieferant.setSelected(true);
    wcbLos.setSelected(true);

    wrBelegnummer.setSelected(true);

    wbuKostenstelle.setActionCommand(this.
                                     ACTION_SPECIAL_KOSTENSTELLE_FROM_LISTE);
    wbuKostenstelle.addActionListener(this);
    getInternalFrame().addItemChangedListener(this);
    this.add(jpaWorkingOn,
             new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.WEST,
                                    GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    int iZeile = 0;
    jpaWorkingOn.add(wbuKostenstelle, new GridBagConstraints(0, iZeile, 1, 1, 0.15, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
        0, 0));
    jpaWorkingOn.add(wtfKostenstelle, new GridBagConstraints(1, iZeile, 1, 1, 0.2, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
        0, 0));
    jpaWorkingOn.add(wlaSortierung, new GridBagConstraints(2, iZeile, 1, 1, 0.1, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
        0, 0));
    jpaWorkingOn.add(wrBelegnummer, new GridBagConstraints(3, iZeile, 1, 1, 0.2, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
        0, 0));
    iZeile++;
    jpaWorkingOn.add(wcbKunde, new GridBagConstraints(0, iZeile, 1, 1, 0, 0.0
         , GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
     0, 0));
  jpaWorkingOn.add(wcbLieferant, new GridBagConstraints(1, iZeile, 1, 1, 0, 0.0
         , GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
     0, 0));
  jpaWorkingOn.add(wcbLos, new GridBagConstraints(2, iZeile, 1, 1, 0, 0.0
         , GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
     0, 0));


    jpaWorkingOn.add(wrbKundeLieferant, new GridBagConstraints(3, iZeile, 1, 1, 0, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
        0, 0));

    iZeile++;
    jpaWorkingOn.add(wcbNurOffene, new GridBagConstraints(1, iZeile, 1, 1, 0, 0.0
            , GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
        0, 0));


    jpaWorkingOn.add(wrbFehler, new GridBagConstraints(3, iZeile, 1, 1, 0, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
        0, 0));





    iZeile++;
    jpaWorkingOn.add(wlaDatumVon, new GridBagConstraints(0, iZeile, 1, 1, 0, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
        0, 0));
    jpaWorkingOn.add(wdfDatumVon, new GridBagConstraints(1, iZeile, 1, 1, 0, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
        0, 0));
    jpaWorkingOn.add(wlaDatumBis, new GridBagConstraints(2, iZeile, 1, 1, 0, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
        0, 0));
    jpaWorkingOn.add(wdfDatumBis, new GridBagConstraints(3, iZeile, 1, 1, 0, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
        0, 0));

    jpaWorkingOn.add(wdrBereich,
                       new GridBagConstraints(4, iZeile, 1, 1, 0.0, 0.0,
                                              GridBagConstraints.WEST,
                                              GridBagConstraints.BOTH,
                                              new Insets(2, 2, 2, 2),
                                            0, 0));

    buttonGroupSortierung.add(wrbFehler);
    buttonGroupSortierung.add(wrbKundeLieferant);
    buttonGroupSortierung.add(wrBelegnummer);

  }


  public String getModul() {
    return ReklamationReportFac.REPORT_MODUL;
  }


  public String getReportname() {
    return ReklamationReportFac.REPORT_REKLAMATIONSJOURNAL;
  }


  protected void eventItemchanged(EventObject eI)
      throws Throwable {
    ItemChangedEvent e = (ItemChangedEvent) eI;

    if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {

      if (e.getSource() == panelQueryFLRKostenstelle) {
        Object key = ( (ISourceEvent) e.getSource()).getIdSelected();
        KostenstelleDto kostenstelleDto = DelegateFactory.getInstance().getSystemDelegate().
            kostenstelleFindByPrimaryKey( (Integer) key);
        wtfKostenstelle.setText(kostenstelleDto.formatKostenstellenbezeichnung());
        kostenstelleIId = kostenstelleDto.getIId();
      }
    }
    else if (e.getID() == ItemChangedEvent.ACTION_LEEREN) {
      if (e.getSource() == panelQueryFLRKostenstelle) {
        wtfKostenstelle.setText(null);
        kostenstelleIId = null;
      }

    }
  }


  void dialogQueryKostenstelleFromListe(ActionEvent e)
      throws Throwable {
    panelQueryFLRKostenstelle = SystemFilterFactory.getInstance().
        createPanelFLRKostenstelle(getInternalFrame(), false, true,
                                   kostenstelleIId);
    new DialogQuery(panelQueryFLRKostenstelle);
  }


  protected void eventActionSpecial(ActionEvent e)
      throws Throwable {
    if (e.getActionCommand().equals(
        ACTION_SPECIAL_KOSTENSTELLE_FROM_LISTE)) {
      dialogQueryKostenstelleFromListe(e);
    }
  }


  public JasperPrintLP getReport(String sDrucktype)
      throws Throwable {

    int iOptionSortierung = -1;

    if (wrbFehler.isSelected()) {
      iOptionSortierung = ReklamationReportFac.SORTIERGUN_REKLAMATIONSJOURNAL_FEHLER;
    }
    else if (wrbKundeLieferant.isSelected()) {
      iOptionSortierung = ReklamationReportFac.SORTIERGUN_REKLAMATIONSJOURNAL_KUNDELIEFERANT;
    }
    else if (wrBelegnummer.isSelected()) {
      iOptionSortierung = ReklamationReportFac.SORTIERGUN_REKLAMATIONSJOURNAL_BELEGNR;
    }
    return DelegateFactory.getInstance().getReklamationReportDelegate().
        printReklamationsjournal(kostenstelleIId, wdfDatumVon.getTimestamp(), wdfDatumBis.getTimestamp(), wcbKunde.isSelected(), wcbLieferant.isSelected(), wcbLos.isSelected(), wcbNurOffene.isSelected(),
                                 iOptionSortierung);
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
