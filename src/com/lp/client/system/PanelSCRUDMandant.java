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
package com.lp.client.system;


import java.awt.event.ActionEvent;

import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.stammdatencrud.PanelStammdatenCRUD;
import com.lp.client.pc.LPButtonAction;
import com.lp.client.pc.LPMain;

@SuppressWarnings("static-access")
/**
 * <p> Diese Klasse kuemmert sich ...</p>
 *
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 *
 * <p>Erstellung: Vorname Nachname; dd.mm.05</p>
 *
 * <p>@author $Author: valentin $</p>
 *
 * @version not attributable Date $Date: 2008/08/11 10:28:44 $
 */
public class PanelSCRUDMandant
    extends PanelStammdatenCRUD
{
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;


public PanelSCRUDMandant(InternalFrame internalFrameI,
                           String add2TitleI,
                           Object pkI,
                           String xmlFilenameI,
                           Object dtoZugriffsKlasseI,
                           String lockMeI)
      throws Throwable {
    super(internalFrameI, add2TitleI, pkI, xmlFilenameI, dtoZugriffsKlasseI, lockMeI);
  }


  public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
      throws Throwable {

    super.eventYouAreSelected(false);

    this.checkMandantLoggedInEqualsMandantSelected(LPMain.getInstance().getTheClient().
        getMandant(), ( (InternalFrameSystem) getInternalFrame()).getMandantDto().getCNr());

  }


  protected void eventActionRefresh(ActionEvent e, boolean bNeedNoRefreshI)
      throws Throwable {
    super.eventActionRefresh(e, bNeedNoRefreshI);

    this.checkMandantLoggedInEqualsMandantSelected(LPMain.getInstance().getTheClient().
        getMandant(), ( (InternalFrameSystem) getInternalFrame()).getMandantDto().getCNr());
  }


  /**
   *
   * @param loggedinMandant String
   * @param selectedMandant String
   * @throws Throwable
   */
  private void checkMandantLoggedInEqualsMandantSelected(String loggedinMandant,
      String selectedMandant) throws Throwable {

    if (!loggedinMandant.equals(selectedMandant)) {

      LPButtonAction item = (LPButtonAction) getHmOfButtons().get(PanelBasis.
          ACTION_UPDATE);
      LPButtonAction item1 = (LPButtonAction) getHmOfButtons().get(PanelBasis.
          ACTION_DELETE);

      item.getButton().setEnabled(false);
      item1.getButton().setEnabled(false);

      getPanelStatusbar().setLockField(LPMain.getInstance().getTextRespectUISPr(
          "system.nurleserecht"));
    }
  }

}
