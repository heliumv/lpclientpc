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
package com.lp.client.frame;


import java.util.HashMap;

import com.lp.client.bestellung.TabbedPaneBestellung;
import com.lp.client.eingangsrechnung.TabbedPaneEingangsrechnung;
import com.lp.client.partner.TabbedPaneLieferant;
import com.lp.client.system.TabbedPaneSystem;
import com.lp.server.system.service.LocaleFac;


/**
 * <p> cmd: 0 Diese Klasse kuemmert sich um Commands.</p>
 *
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 *
 * <p>Erstellung: Josef Ornetsmueller; 24.10.06</p>
 *
 * <p>@author $Author: heidi $</p>
 *
 * @version not attributable Date $Date: 2008/08/08 06:50:32 $
 */
abstract public class Command
{
  private String sInternalFrame = null;

  //hier folgen die Konstanten je Modul.
  //1) INTERNALFRAME_XY ist immer ein LocaleFac.XY.
  //2) TabbedPane ist immer die Class.
  //Bes
  public final static String S_INTERNALFRAME_BESTELLUNG = LocaleFac.BELEGART_BESTELLUNG;
  public static final Class<TabbedPaneBestellung> CLASS_TABBED_PANE_BESTELLUNG = TabbedPaneBestellung.class;
  public final static String PANELBESTELLUNG_POSITIONSICHT_RAHMEN =
      "PanelBestellungPositionSichtRahmen";
  public final static String PANEL_BESTELLUNG_POSITIONEN =
      "PanelBestellungPositionen";
  public final static String PANEL_QUERYFLR_LIEFERANTEN =
      "PanelQueryFLRGotoLieferant";

  //Sys
  public final static String S_INTERNALFRAME_SYSTEM = LocaleFac.BELEGART_SYSTEM;
  public static final Class<TabbedPaneSystem> CLASS_TABBED_PANE_SYSTEM = TabbedPaneSystem.class;
  public final static String PANEL_LAND_PLZ_ORT = "PanelLandPlzOrt";

  //Par
  public final static String S_INTERNALFRAME_PARTNER = LocaleFac.BELEGART_PARTNER;

  //Lieferant
  public final static String S_INTERNALFRAME_LIEFERANT = LocaleFac.BELEGART_LIEFERANT;
  public static final Class<TabbedPaneLieferant> CLASS_TABBED_PANE_LIEFERANT = TabbedPaneLieferant.class;
  public final static String PANEL_LIEFERANT_KOPFDATEN = "PanelLieferantkopfdaten";

  //Eingangsrechnung
  public final static String S_INTERNALFRAME_EINGANGSRECHNUNG = LocaleFac.BELEGART_EINGANGSRECHNUNG;
  public static final Class<TabbedPaneEingangsrechnung> CLASS_TABBED_PANE_EINGANGSRECHNUNG = TabbedPaneEingangsrechnung.class;
  public final static String PANEL_EINGANGSRECHNUNG_KOPFDATEN = "PanelEingangsrechnungKopfdaten";


  private HashMap<?, ?> hmOfExtraData = null;

  public Command(String sInternalFrameI) {
    sInternalFrame = sInternalFrameI;
  }


  public String getsInternalFrame() {
    return sInternalFrame;
  }


  public void setCInternalFrame(String sInternalFrameI) {
    this.sInternalFrame = sInternalFrameI;
  }


  public void setHmOfExtraData(HashMap<?, ?> hmOfExtraDataI) {
    this.hmOfExtraData = hmOfExtraDataI;
  }


  public HashMap<?, ?> getHmOfExtraData() {
    return hmOfExtraData;
  }

}
