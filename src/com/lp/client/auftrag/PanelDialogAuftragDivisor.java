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
package com.lp.client.auftrag;

import java.awt.event.ActionEvent;

import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.PanelDialogDivisor;


/**
 * <p><I>Dialog zur Eingabe des Divisors.</I> </p>
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 * <p>Erstellungsdatum <I>04. 08. 2005</I></p>
 * <p> </p>
 * @author Uli Walch
 * @version $Revision: 1.3 $
 */
public class PanelDialogAuftragDivisor
    extends PanelDialogDivisor {
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

public PanelDialogAuftragDivisor(InternalFrame oInternalFrameI,
                                               String title) throws
      Throwable {
    super(oInternalFrameI, title);
  }

  protected void eventActionSpecial(ActionEvent e) throws Throwable {
    if (e.getActionCommand().equals(ACTION_SPECIAL_OK)) {
      buildFilterKriterien();
    }

    // Frame Klasse aufrufen
    super.eventActionSpecial(e);

    if (e.getActionCommand().equals(ACTION_SPECIAL_CLOSE_PANELDIALOG)) {
      // Sonderfall: Wechsel in Sicht Rahmenpositionen
      ((InternalFrameAuftrag) getInternalFrame()).getTabbedPaneAuftrag().gotoSichtRahmen();
    }
  }
}
