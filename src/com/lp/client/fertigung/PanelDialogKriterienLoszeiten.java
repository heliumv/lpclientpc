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

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;

import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.SwingConstants;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.PanelDialogKriterien;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperRadioButton;
import com.lp.client.pc.LPMain;
import com.lp.server.fertigung.service.FertigungFac;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;

@SuppressWarnings("static-access") 
/**
 * <p><I>Dialog zur Eingabe der Kriterien fuer die Anzeige der Loszeiten.</I> </p>
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 * <p>Erstellungsdatum <I>07. 11. 2005</I></p>
 * <p> </p>
 * @author Uli Walch
 * @version $Revision: 1.3 $
 */
public class PanelDialogKriterienLoszeiten
    extends PanelDialogKriterien {

  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private WrapperLabel wlaNach = null;
  private ButtonGroup jbgNach = null;
  private WrapperLabel wlaEmptyLabel1 = null;
  private WrapperRadioButton wrbNachPersonal = null;
  private WrapperRadioButton wrbNachIdent = null;
  private TabbedPaneLos tabbedPaneLos=null;

  public PanelDialogKriterienLoszeiten(InternalFrame internalFrame, TabbedPaneLos tabbedPaneLos, String title) throws
      Throwable {
    super(internalFrame, title);
    this.tabbedPaneLos=tabbedPaneLos;
    jbInit();
    setDefaults();
    initComponents();
  }

  /**
   * Dialog initialisieren
   * @throws Throwable
   */
  private void jbInit() throws Throwable {
    // die Gruppe mit nach Datum
    wlaNach = new WrapperLabel(LPMain.getTextRespectUISPr("label.auswertung"));
    wlaNach.setMaximumSize(new Dimension(150, Defaults.getInstance().getControlHeight()));
    wlaNach.setMinimumSize(new Dimension(150, Defaults.getInstance().getControlHeight()));
    wlaNach.setPreferredSize(new Dimension(150, Defaults.getInstance().getControlHeight()));
    wlaNach.setHorizontalAlignment(SwingConstants.LEADING);

    wlaEmptyLabel1 = new WrapperLabel();
    wlaEmptyLabel1.setMaximumSize(new Dimension(10, Defaults.getInstance().getControlHeight()));
    wlaEmptyLabel1.setMinimumSize(new Dimension(10, Defaults.getInstance().getControlHeight()));
    wlaEmptyLabel1.setPreferredSize(new Dimension(10, Defaults.getInstance().getControlHeight()));

    jbgNach = new ButtonGroup();

    wrbNachPersonal = new WrapperRadioButton();
    wrbNachPersonal.setText(
        LPMain.getInstance().getTextRespectUISPr("menueentry.personal") +"/"+LPMain.getInstance().getTextRespectUISPr("lp.maschine"));

    wrbNachIdent = new WrapperRadioButton();
    wrbNachIdent.setText(
        LPMain.getInstance().getTextRespectUISPr("label.identnummer"));

    jbgNach.add(wrbNachPersonal);
    jbgNach.add(wrbNachIdent);

    // Zeile
    jpaWorkingOn.add(wlaNach,
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
    jpaWorkingOn.add(wrbNachPersonal,
                        new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0
                                               , GridBagConstraints.CENTER,
                                               GridBagConstraints.BOTH,
                                               new Insets(0, 0, 0, 0), 90, 0));

      // Zeile
    iZeile++;
    jpaWorkingOn.add(wrbNachIdent,
                        new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0
                                               , GridBagConstraints.CENTER,
                                               GridBagConstraints.BOTH,
                                               new Insets(0, 0, 0, 0), 0, 0));
  }

  private void setDefaults() throws Throwable {
    wrbNachPersonal.setSelected(true);
  }

  /**
   * Die gewaehlten Kriterien zusammenbauen.
   * <br>Es gilt fuer Auftragzeiten:
   * <br>Krit1 : Auswertung (Personal oder Ident) = true
   * @return FilterKriterium[] die Kriterien
   * @throws java.lang.Throwable Ausnahme
   */
  public FilterKriterium[] buildFilterKriterien() throws Throwable {
    aAlleKriterien = new FilterKriterium[FertigungFac.ANZAHL_KRITERIEN_LOSZEITEN];

    FilterKriterium fkAuswertung = null;

    if (wrbNachPersonal.isSelected()) {
      // Auswertung nach Personal
      fkAuswertung = new FilterKriterium(
          FertigungFac.KRIT_PERSONAL,
          wrbNachPersonal.isSelected(),
          "true",
          FilterKriterium.OPERATOR_EQUAL, false);
    }
    else
    if (wrbNachIdent.isSelected()) {
      // Auswertung nach Liefertermin
      fkAuswertung = new FilterKriterium(
          FertigungFac.KRIT_IDENT,
          wrbNachIdent.isSelected(),
          "true",
          FilterKriterium.OPERATOR_EQUAL, false);
    }

    FilterKriterium fkAuftrag = new FilterKriterium(
          FertigungFac.KRIT_LOS_I_ID,
          true,
          tabbedPaneLos.getLosDto().getIId().toString(),
          FilterKriterium.OPERATOR_EQUAL, false);

    aAlleKriterien[FertigungFac.IDX_KRIT_AUSWERTUNG] = fkAuswertung;
    aAlleKriterien[FertigungFac.IDX_KRIT_LOS] = fkAuftrag;

    return aAlleKriterien;
  }

  protected void eventActionSpecial(ActionEvent e) throws Throwable {
    if (e.getActionCommand().equals(ACTION_SPECIAL_OK)) {
      buildFilterKriterien();
    }

    // den Dialog verlassen
    super.eventActionSpecial(e);

    if (e.getActionCommand().equals(ACTION_SPECIAL_CLOSE_PANELDIALOG)) {
      tabbedPaneLos.gotoAuswahl();
    }
  }

  public FilterKriterium[] getAlleFilterKriterien() throws Throwable {
    return buildFilterKriterien();
  }


  protected JComponent getFirstFocusableComponent()
      throws Exception {
    return wrbNachPersonal;
  }
}
