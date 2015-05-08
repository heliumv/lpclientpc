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
package com.lp.client.lieferschein;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;

import javax.swing.ButtonGroup;
import javax.swing.SwingConstants;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.PanelDialogKriterien;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperRadioButton;
import com.lp.client.pc.LPMain;
import com.lp.server.lieferschein.service.LieferscheinFac;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;

@SuppressWarnings("static-access") 
/**
 * <p><I>Dialog zur Eingabe der Kriterien fuer die Lieferschein Umsatz.</I></p>
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 * <p>Erstellungsdatum <I>13. 03. 2005</I></p>
 * <p> </p>
 * @author Uli Walch
 * @version $Revision: 1.3 $
 */
public class PanelDialogKriterienLieferscheinUmsatz
    extends PanelDialogKriterien {
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
/** Cache for convenience. */
  private InternalFrameLieferschein intFrame = null;
  /** Cache for convenience. */
  private TabbedPaneLieferschein tpLieferschein = null;

  private WrapperLabel wlaNachzeiteinheit = null;
  private ButtonGroup jbgNachzeiteinheit = null;
  private WrapperLabel wlaEmptyLabel1 = null;
  private WrapperRadioButton wrbTag = null;
  private WrapperRadioButton wrbWoche = null;
  private WrapperRadioButton wrbMonat = null;

  public PanelDialogKriterienLieferscheinUmsatz(InternalFrame oInternalFrameI,
                                               String title) throws
      Throwable {
    super(oInternalFrameI, title);

      intFrame = (InternalFrameLieferschein) getInternalFrame();
      tpLieferschein = intFrame.getTabbedPaneLieferschein();

      jbInit();
      setDefaults();
      initComponents();
  }

  /**
   * Dialog initialisieren
   * @throws Throwable
   */
  private void jbInit() throws Throwable {
    // die Gruppe mit nach Zeiteinheit
    wlaNachzeiteinheit = new WrapperLabel(LPMain.getTextRespectUISPr("label.auswertung"));
    wlaNachzeiteinheit.setMaximumSize(new Dimension(120, Defaults.getInstance().getControlHeight()));
    wlaNachzeiteinheit.setMinimumSize(new Dimension(120, Defaults.getInstance().getControlHeight()));
    wlaNachzeiteinheit.setPreferredSize(new Dimension(120, Defaults.getInstance().getControlHeight()));
    wlaNachzeiteinheit.setHorizontalAlignment(SwingConstants.LEADING);

    wlaEmptyLabel1 = new WrapperLabel();
    wlaEmptyLabel1.setMaximumSize(new Dimension(10, Defaults.getInstance().getControlHeight()));
    wlaEmptyLabel1.setMinimumSize(new Dimension(10, Defaults.getInstance().getControlHeight()));
    wlaEmptyLabel1.setPreferredSize(new Dimension(10, Defaults.getInstance().getControlHeight()));

    jbgNachzeiteinheit = new ButtonGroup();

    wrbTag = new WrapperRadioButton();
    wrbTag.setText(
        LPMain.getInstance().getTextRespectUISPr("lp.tag"));

    wrbWoche = new WrapperRadioButton();
    wrbWoche.setText(
        LPMain.getInstance().getTextRespectUISPr("lp.woche"));

    wrbMonat = new WrapperRadioButton();
    wrbMonat.setText(
        LPMain.getInstance().getTextRespectUISPr("lp.monat1"));

    jbgNachzeiteinheit.add(wrbTag);
    jbgNachzeiteinheit.add(wrbWoche);
    jbgNachzeiteinheit.add(wrbMonat);

    // Zeile
    iZeile++;
    jpaWorkingOn.add(wlaNachzeiteinheit,
                        new GridBagConstraints(0, iZeile, 2, 1, 0.0, 0.0
                                               , GridBagConstraints.CENTER,
                                               GridBagConstraints.BOTH,
                                               new Insets(2, 2, 2, 2), 0, 0));

    // Zeile
    iZeile++;
    jpaWorkingOn.add(wlaEmptyLabel1,
                        new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0
                                               , GridBagConstraints.CENTER,
                                               GridBagConstraints.BOTH,
                                               new Insets(2, 2, 2, 2), 0, 0));
    jpaWorkingOn.add(wrbTag,
                        new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0
                                               , GridBagConstraints.CENTER,
                                               GridBagConstraints.BOTH,
                                               new Insets(0, 0, 0, 0), 0, 0));

    // Zeile
    iZeile++;
    jpaWorkingOn.add(wrbWoche,
                        new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0
                                               , GridBagConstraints.CENTER,
                                               GridBagConstraints.BOTH,
                                               new Insets(0, 0, 0, 0), 0, 0));

    // Zeile
    iZeile++;
    jpaWorkingOn.add(wrbMonat,
                        new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0
                                               , GridBagConstraints.CENTER,
                                               GridBagConstraints.BOTH,
                                               new Insets(0, 0, 0, 0), 0, 0));
  }

  private void setDefaults() throws Throwable {
    wrbTag.setSelected(true);
  }

  /**
   * Die gewaehlten Kriterien zusammenbauen.
   * <br>Es gilt fuer Lieferschein Uebersicht:
   * <br>Krit1 : Auswertung (Tag oder Woche oder Monat) = true
   * @return FilterKriterium[]
   * @throws java.lang.Throwable Ausnahme
   */
  public FilterKriterium[] buildFilterKriterien() throws Throwable {
    aAlleKriterien = new FilterKriterium[LieferscheinFac.LS_UMSATZ_ANZAHL_KRITERIEN];

    FilterKriterium fkAuswertung = null;

    if (wrbTag.isSelected()) {
      // Auswertung nach Tag
      fkAuswertung = new FilterKriterium(
          LieferscheinFac.KRIT_TAG,
          wrbTag.isSelected(),
          Boolean.TRUE.toString(),
          FilterKriterium.OPERATOR_EQUAL, false);
    }
    else
    if (wrbWoche.isSelected()) {
      // Auswertung nach Woche
      fkAuswertung = new FilterKriterium(
          LieferscheinFac.KRIT_WOCHE,
          wrbWoche.isSelected(),
          Boolean.TRUE.toString(),
          FilterKriterium.OPERATOR_EQUAL, false);
    }
    else
    if (wrbMonat.isSelected()) {
      // Auswertung nach Monat
      fkAuswertung = new FilterKriterium(
          LieferscheinFac.KRIT_MONAT,
          wrbMonat.isSelected(),
          Boolean.TRUE.toString(),
          FilterKriterium.OPERATOR_EQUAL, false);
    }

    aAlleKriterien[LieferscheinFac.LS_UMSATZ_IDX_KRIT_ZEITEINHEIT] = fkAuswertung;

    return aAlleKriterien;
  }

  protected void eventActionSpecial(ActionEvent e) throws Throwable {
      if (e.getActionCommand().equals(ACTION_SPECIAL_OK)) {
        buildFilterKriterien();
      }

      // den Dialog verlassen
      super.eventActionSpecial(e);

      if (e.getActionCommand().equals(ACTION_SPECIAL_CLOSE_PANELDIALOG)) {
        tpLieferschein.setBKriterienLsUmsatzUeberMenueAufgerufen(false);
        tpLieferschein.gotoAuswahl();
    }
  }

  public FilterKriterium[] getAlleFilterKriterien() throws Throwable {
    return buildFilterKriterien();
  }
}
