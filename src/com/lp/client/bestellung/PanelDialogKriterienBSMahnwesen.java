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
package com.lp.client.bestellung;


import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.EventObject;

import javax.swing.ButtonGroup;
import javax.swing.JComponent;

import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelDialogKriterien;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperRadioButton;
import com.lp.client.pc.LPMain;
import com.lp.server.bestellung.service.BSMahnwesenFac;


/**
 * <p><I>Dialog zur Eingabe der Kriterien fuer die Bestellvorschlag Uebersicht.</I> </p>
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 * <p>Erstellungsdatum <I>28. 02. 2005</I></p>
 * <p> </p>
 * @author Josef Erlinger
 * @version $Revision: 1.4 $
 */
public class PanelDialogKriterienBSMahnwesen
    extends PanelDialogKriterien
{
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

/** Cache for convenience. */
  private TabbedPaneBESMahnwesen tpBSMahnwesen = null;

  private WrapperRadioButton wrbechteLiefermahnungen = null;
  private WrapperRadioButton wrbABMahnungen = null;
  private WrapperRadioButton wrbABundLieferMahnungen = null;
  private WrapperRadioButton wrbLieferinnerung = null;

  private ButtonGroup javabgGroup = null;

  static final private String ACTION_RADIOBUTTON_ECHTE_LIEFERMAHNUNGEN =
      "action_radiobutton_echte_liefermahnungen";
  static final private String ACTION_RADIOBUTTON_NUR_ABMAHNUNGEN =
      "action_radiobutton_nur_abmahnungen";
  static final private String ACTION_RADIOBUTTON_AB_UND_LIEFER_MAHNUNGEN =
      "action_radiobutton_ab_und_liefer_mahnungen";
  static final private String ACTION_RADIOBUTTON_NUR_LIEFERERINNERUNG =
      "action_radiobutton_nur_liefererinnerung";
  public PanelDialogKriterienBSMahnwesen(InternalFrame oInternalFrameI,
                                         String title,
                                         TabbedPaneBESMahnwesen
                                         tpBSMahnwesen)
      throws Throwable {
    super(oInternalFrameI, title);
    this.tpBSMahnwesen = tpBSMahnwesen;
    jbInitPanel();
    setDefaults();
    initComponents();
  }


  /**
   * Dialog initialisieren
   * @throws Throwable
   */
  private void jbInitPanel()
      throws Throwable {
    // die Gruppe mit nach Datum

    javabgGroup = new ButtonGroup();
    //1RadioButton
    wrbLieferinnerung = new WrapperRadioButton();
    wrbLieferinnerung.setText(LPMain.getTextRespectUISPr("bes.mahnart.liefererinnerung"));
    wrbLieferinnerung.setActionCommand(ACTION_RADIOBUTTON_NUR_LIEFERERINNERUNG);
    wrbLieferinnerung.addActionListener(this);
    //1RadioButton
    wrbechteLiefermahnungen = new WrapperRadioButton();
    wrbechteLiefermahnungen.setText(LPMain.getTextRespectUISPr("bes.mahnwesen.radiobutton1"));
    wrbechteLiefermahnungen.setActionCommand(ACTION_RADIOBUTTON_ECHTE_LIEFERMAHNUNGEN);
    wrbechteLiefermahnungen.addActionListener(this);
    //2RadioButton
    wrbABMahnungen = new WrapperRadioButton();
    wrbABMahnungen.setText(LPMain.getTextRespectUISPr("bes.mahnwesen.radiobutton2"));
    wrbABMahnungen.setActionCommand(ACTION_RADIOBUTTON_NUR_ABMAHNUNGEN);
    wrbABMahnungen.addActionListener(this);
    //3RadioButton
    wrbABundLieferMahnungen = new WrapperRadioButton();
    wrbABundLieferMahnungen.setText(LPMain.getTextRespectUISPr("bes.mahnwesen.radiobutton3"));
    wrbABundLieferMahnungen.setActionCommand(ACTION_RADIOBUTTON_AB_UND_LIEFER_MAHNUNGEN);
    wrbABundLieferMahnungen.addActionListener(this);

    //damit der event via internalframe direkt hierherkommt,
    //  nicht ueber tabbedpanes.
    getInternalFrame().addItemChangedListener(this);

    javabgGroup.add(this.wrbLieferinnerung);
    javabgGroup.add(this.wrbechteLiefermahnungen);
    javabgGroup.add(this.wrbABMahnungen);
    javabgGroup.add(this.wrbABundLieferMahnungen);

    jpaWorkingOn.add(new WrapperLabel(),
                     new GridBagConstraints(1, iZeile, 1, 1, 7.0, 0.0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.BOTH,
                                            new Insets(2, 2, 2, 2), 0, 0));

    jpaWorkingOn.add(new WrapperLabel(),
                     new GridBagConstraints(2, iZeile, 1, 1, 7.0, 0.0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.BOTH,
                                            new Insets(2, 2, 2, 2), 0, 0));

    jpaWorkingOn.add(new WrapperLabel(),
                     new GridBagConstraints(3, iZeile, 1, 1, 7.0, 0.0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.BOTH,
                                            new Insets(2, 2, 2, 2), 0, 0));

    jpaWorkingOn.add(new WrapperLabel(),
                     new GridBagConstraints(4, iZeile, 1, 1, 7.0, 0.0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.BOTH,
                                            new Insets(2, 2, 2, 2), 0, 0));

//eigentliche Steuerelemente
    jpaWorkingOn.add(wrbABMahnungen,
            new GridBagConstraints(0, iZeile, 4, 1, 0.0, 0.0
                                   , GridBagConstraints.CENTER,
                                   GridBagConstraints.BOTH,
                                   new Insets(2, 2, 2, 2), 0, 0));
    iZeile++;
    jpaWorkingOn.add(new WrapperLabel(),
                     new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.BOTH,
                                            new Insets(2, 2, 2, 2), 0, 0));
    iZeile++;
    jpaWorkingOn.add(wrbLieferinnerung,
                     new GridBagConstraints(0, iZeile, 4, 1, 0.0, 0.0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.BOTH,
                                            new Insets(2, 2, 2, 2), 0, 0));

    //Leerzeile
    iZeile++;
    jpaWorkingOn.add(new WrapperLabel(),
                     new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.BOTH,
                                            new Insets(2, 2, 2, 2), 0, 0));

    //Zeile
    iZeile++;
    jpaWorkingOn.add(wrbechteLiefermahnungen,
                     new GridBagConstraints(0, iZeile, 4, 1, 0.0, 0.0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.BOTH,
                                            new Insets(2, 2, 2, 2), 0, 0));

    //Leerzeile
    iZeile++;
    jpaWorkingOn.add(new WrapperLabel(),
                     new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.BOTH,
                                            new Insets(2, 2, 2, 2), 0, 0));

    //Zeile
    iZeile++;
    jpaWorkingOn.add(wrbABundLieferMahnungen,
                     new GridBagConstraints(0, iZeile, 3, 1, 0.0, 0.0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.BOTH,
                                            new Insets(0, 0, 0, 0), 0, 0));

    //Leerzeile
    iZeile++;
    jpaWorkingOn.add(new WrapperLabel(),
                     new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.BOTH,
                                            new Insets(2, 2, 2, 2), 0, 0));

    //Leerzeile
    iZeile++;
    jpaWorkingOn.add(new WrapperLabel(),
                     new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.BOTH,
                                            new Insets(2, 2, 2, 2), 0, 0));
  }


  private void setDefaults()
      throws Throwable {
    wrbABMahnungen.setSelected(true);
    tpBSMahnwesen.setwhichFilterAusBSMahnwesen(BSMahnwesenFac.MAHNART_AB_MAHNUNGEN);
  }


  protected void eventActionSpecial(ActionEvent e)
      throws Throwable {
    if (e.getActionCommand().equals(ACTION_SPECIAL_OK)) {
      if (!allMandatoryFieldsSetDlg()) {
        return;
      }
    }
    else if (e.getActionCommand().equals(ACTION_SPECIAL_CLOSE_PANELDIALOG)) {
    }
    //radiobutton schalten
    else if (e.getActionCommand().equals(ACTION_RADIOBUTTON_ECHTE_LIEFERMAHNUNGEN)) {

      tpBSMahnwesen.setwhichFilterAusBSMahnwesen(BSMahnwesenFac.MAHNART_LIEFER_MAHNUNGEN);

      this.wrbABMahnungen.setSelected(false);
      this.wrbechteLiefermahnungen.setSelected(false);
      this.wrbABundLieferMahnungen.setSelected(false);
    }
    else if (e.getActionCommand().equals(ACTION_RADIOBUTTON_NUR_ABMAHNUNGEN)) {

      tpBSMahnwesen.setwhichFilterAusBSMahnwesen(BSMahnwesenFac.MAHNART_AB_MAHNUNGEN);

      this.wrbechteLiefermahnungen.setSelected(false);
      this.wrbABundLieferMahnungen.setSelected(false);
      this.wrbLieferinnerung.setSelected(false);
    }
    else if (e.getActionCommand().equals(ACTION_RADIOBUTTON_AB_UND_LIEFER_MAHNUNGEN)) {

      tpBSMahnwesen.setwhichFilterAusBSMahnwesen(BSMahnwesenFac.
                                                 MAHNART_AB_UND_LIEFER_MAHNUNGEN);

      this.wrbechteLiefermahnungen.setSelected(false);
      this.wrbABMahnungen.setSelected(false);
      this.wrbLieferinnerung.setSelected(false);
    }
    else if (e.getActionCommand().equals(ACTION_RADIOBUTTON_NUR_LIEFERERINNERUNG)) {

        tpBSMahnwesen.setwhichFilterAusBSMahnwesen(BSMahnwesenFac.
                                                   MAHNART_LIEFERERINNERUNG);

        this.wrbechteLiefermahnungen.setSelected(false);
        this.wrbABMahnungen.setSelected(false);
        this.wrbABundLieferMahnungen.setSelected(false);
      }

    // den Dialog verlassen
    super.eventActionSpecial(e);
  }


  protected void eventItemchanged(EventObject eI)
      throws Throwable {
    ItemChangedEvent e = (ItemChangedEvent) eI;
  }


  protected JComponent getFirstFocusableComponent()
      throws Exception {
    return wrbABMahnungen;
  }
}
