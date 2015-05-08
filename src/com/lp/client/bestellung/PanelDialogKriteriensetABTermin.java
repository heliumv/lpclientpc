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
package com.lp.client.bestellung;


import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.Date;
import java.util.EventObject;

import javax.swing.ButtonGroup;
import javax.swing.JComponent;

import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelDialogKriterien;
import com.lp.client.frame.component.WrapperDateField;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperRadioButton;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.pc.LPMain;
import com.lp.server.bestellung.service.BestellpositionFac;


/**
 * <p><I>Dialog zur Eingabe der Kriterien fuer die Ueberleitung des Bestellvorschlags in Belege.</I> </p>
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 * <p>Erstellungsdatum <I>26.10.05</I></p>
 * <p> </p>
 * @author Josef Erlinger
 * @version $Revision: 1.5 $
 */
public class PanelDialogKriteriensetABTermin
    extends PanelDialogKriterien
{
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private ButtonGroup jbgKriterien = null;
  private WrapperLabel wlaEmpty = null; // fuer die Formatierung
  private WrapperLabel wlaABTermin = null;
  private WrapperDateField wdfABTermin = null;
  private WrapperLabel wlaABNummer = null;
  private WrapperTextField wtfABNummer = null;
  private WrapperLabel wlaAlleBesPosSetzen = null;
  private WrapperLabel wlaLeereBesPosSetzen = null;
  private WrapperLabel wlaMarkiertePosSetzen = null;
  private WrapperRadioButton wrbAlleBestellpositionSetzen = null;
  private WrapperRadioButton wrbLeereBestellpositionSetzen = null;
  private WrapperRadioButton wrbMarkierteBestellpositionSetzen = null;

 
  private TabbedPaneBestellung tpBestellung = null;

  public PanelDialogKriteriensetABTermin(InternalFrame oInternalFrameI,
                                         String title,
                                         TabbedPaneBestellung
                                         tpBestellung)
      throws Throwable {
    super(oInternalFrameI, title);
    this.tpBestellung = tpBestellung;
    jbInitPanel();
    setDefaults();
    initComponents();
  }


  private void jbInitPanel()
      throws Throwable {

    jbgKriterien = new ButtonGroup();
    getInternalFrame().addItemChangedListener(this);
    wlaEmpty = new WrapperLabel();
    wlaABTermin = new WrapperLabel(LPMain.getTextRespectUISPr("bes.abtermin"));
    wdfABTermin = new WrapperDateField();
    wdfABTermin.setMandatoryField(true);

    wlaABNummer = new WrapperLabel(LPMain.getTextRespectUISPr("bes.abnummer"));
    wtfABNummer = new WrapperTextField();
    wtfABNummer.setEditable(true);
    wtfABNummer.setColumnsMax(20);
    wtfABNummer.setMandatoryField(true);

    wlaAlleBesPosSetzen = new WrapperLabel(LPMain.getTextRespectUISPr("bes.allepositionensetzen"));
    wlaLeereBesPosSetzen = new WrapperLabel(LPMain.getTextRespectUISPr("bes.leerepositionensetzen"));
    wlaMarkiertePosSetzen = new WrapperLabel(LPMain.getTextRespectUISPr("bes.markiertepositionensetzen"));
    wrbAlleBestellpositionSetzen = new WrapperRadioButton();
   

    wrbLeereBestellpositionSetzen = new WrapperRadioButton();
    wrbMarkierteBestellpositionSetzen = new WrapperRadioButton();
  

    jbgKriterien.add(wrbAlleBestellpositionSetzen);
    jbgKriterien.add(wrbLeereBestellpositionSetzen);
    jbgKriterien.add(wrbMarkierteBestellpositionSetzen);

    iZeile++;
    jpaWorkingOn.add(new WrapperLabel(),
                     new GridBagConstraints(0, iZeile, 1, 1, 7.0, 0.0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.BOTH,
                                            new Insets(2, 2, 2, 2), 0, 0));

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

    iZeile++;
    jpaWorkingOn.add(wlaEmpty,
                     new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.BOTH,
                                            new Insets(2, 2, 2, 2), 0, 0));
    iZeile++;
    jpaWorkingOn.add(wlaABTermin,
                     new GridBagConstraints(1, iZeile, 2, 1, 0.0, 0.0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.BOTH,
                                            new Insets(2, 2, 2, 2), 0, 0));

    jpaWorkingOn.add(wdfABTermin,
                     new GridBagConstraints(3, iZeile, 2, 1, 0.0, 0.0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.BOTH,
                                            new Insets(2, 2, 2, 2), 0, 0));
    iZeile++;
    jpaWorkingOn.add(wlaABNummer,
                     new GridBagConstraints(1, iZeile, 2, 1, 0.0, 0.0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.BOTH,
                                            new Insets(2, 2, 2, 2), 0, 0));

    jpaWorkingOn.add(wtfABNummer,
                     new GridBagConstraints(3, iZeile, 2, 1, 0.0, 0.0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.BOTH,
                                            new Insets(2, 2, 2, 2), 0, 0));
    iZeile++;
    jpaWorkingOn.add(wlaLeereBesPosSetzen,
                     new GridBagConstraints(1, iZeile, 2, 1, 0.0, 0.0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.BOTH,
                                            new Insets(2, 2, 2, 2), 0, 0));

    jpaWorkingOn.add(wrbLeereBestellpositionSetzen,
                     new GridBagConstraints(3, iZeile, 2, 1, 0.0, 0.0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.BOTH,
                                            new Insets(2, 2, 2, 2), 0, 0));
    iZeile++;
    jpaWorkingOn.add(wlaAlleBesPosSetzen,
                     new GridBagConstraints(1, iZeile, 2, 1, 0.0, 0.0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.BOTH,
                                            new Insets(2, 2, 2, 2), 0, 0));

    jpaWorkingOn.add(wrbAlleBestellpositionSetzen,
                     new GridBagConstraints(3, iZeile, 2, 1, 0.0, 0.0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.BOTH,
                                            new Insets(2, 2, 2, 2), 0, 0));
    iZeile++;
    jpaWorkingOn.add(wlaMarkiertePosSetzen,
                     new GridBagConstraints(1, iZeile, 2, 1, 0.0, 0.0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.BOTH,
                                            new Insets(2, 2, 2, 2), 0, 0));

    jpaWorkingOn.add(wrbMarkierteBestellpositionSetzen,
                     new GridBagConstraints(3, iZeile, 2, 1, 0.0, 0.0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.BOTH,
                                            new Insets(2, 2, 2, 2), 0, 0));

  }


  private void setDefaults()
      throws Throwable {
    wdfABTermin.setEditable(false);
    wdfABTermin.setDate(new Date(System.currentTimeMillis()));
    wrbAlleBestellpositionSetzen.setSelected(true);
   
  }


  public int getOptionAbTerminSetzen(){
	  if(wrbAlleBestellpositionSetzen.isSelected()){
		  return BestellpositionFac.SICHT_LIEFERANTENTERMINE_ABTERMIN_SETZEN_OPTION_ALLE;
	  }else if(wrbMarkierteBestellpositionSetzen.isSelected()){
		  return BestellpositionFac.SICHT_LIEFERANTENTERMINE_ABTERMIN_SETZEN_OPTION_MARKIERTE;
	  }else if(wrbLeereBestellpositionSetzen.isSelected()){
		  return BestellpositionFac.SICHT_LIEFERANTENTERMINE_ABTERMIN_SETZEN_OPTION_LEERE;
	  } 
	  return -1;
  }
  
  protected void eventActionSpecial(ActionEvent e)
      throws Throwable {
    if (e.getActionCommand().equals(ACTION_SPECIAL_OK)) {
      if (allMandatoryFieldsSetDlg()) {
        setComponents();
      }
    }
   

    super.eventActionSpecial(e);
  }


  protected void eventItemchanged(EventObject eI)
      throws Throwable {
    ItemChangedEvent e = (ItemChangedEvent) eI;

    if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {

    }
  }


  private void setComponents()
      throws Throwable {
    tpBestellung.setABDate(wdfABTermin.getDate());
    tpBestellung.setABNummer(wtfABNummer.getText());
  }


  protected JComponent getFirstFocusableComponent()
      throws Exception {
    return wtfABNummer;
  }

}
