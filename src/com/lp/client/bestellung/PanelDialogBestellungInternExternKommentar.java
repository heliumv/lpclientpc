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


import java.awt.event.ActionEvent;

import javax.swing.JComponent;

import com.lp.client.frame.HelperClient;
import com.lp.client.frame.LockStateValue;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.PanelDialogKommentar;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.system.service.LockMeDto;


/**
 * <p><I>Dialog zur Eingabe des internen Kommentars fuer ein Angebot.</I> </p>
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 * <p>Erstellungsdatum <I>30.09.05</I></p>
 * <p> </p>
 * @author Josef Erlinger
 * @version $Revision: 1.7 $
 */
public class PanelDialogBestellungInternExternKommentar
    extends PanelDialogKommentar
{
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
/** Cache for convenience. */
  private InternalFrameBestellung intFrame = null;
  /** Cache for convenience. */
  private TabbedPaneBestellung tpBestellung = null;

  /** Wenn der Dialog von aussen geoeffnet wird, muss ein Lock gesetzt werden. */
  private LockMeDto lockMeBestellung = null;

  /** Der Kommentar kann intern oder extern sein. */
  private boolean bInternerKommentar = false;

  public PanelDialogBestellungInternExternKommentar(InternalFrame internalFrame,
                                              String add2TitleI,
                                              boolean bInternerKommentarI)
      throws Throwable {
    super(internalFrame, add2TitleI);

    intFrame = (InternalFrameBestellung) getInternalFrame();
    tpBestellung = intFrame.getTabbedPaneBestellung();
    bInternerKommentar = bInternerKommentarI;

    jbInit();
    initComponents();
    dto2Components();
  }


  private void jbInit()
      throws Throwable {
    if (tpBestellung.getBesDto() != null) {
      lockMeBestellung = new LockMeDto(
          HelperClient.LOCKME_BESTELLUNG,
          tpBestellung.getBesDto().getIId() + "",
          LPMain.getInstance().getCNrUser());
    }

    // explizit ausloesen, weil weder new noch update aufgerufen werden
    eventActionLock(null);
  }


  private void dto2Components()
      throws Throwable {
    if (this.bInternerKommentar) {
      getLpEditor().setText(tpBestellung.getBesDto().getXInternerKommentar());
      setCBestehenderKommentar(tpBestellung.getBesDto().getXInternerKommentar());
    }
    else {
      getLpEditor().setText(tpBestellung.getBesDto().getXExternerKommentar());
      setCBestehenderKommentar(tpBestellung.getBesDto().getXExternerKommentar());
    }
  }


  protected void eventActionSpecial(ActionEvent e)
      throws Throwable {
    super.eventActionSpecial(e);
    if(super.bHandleEventInSuperklasse) {
      // explizit aufrufen, weil weder save noch discard aufgerufen werden
      eventActionUnlock(null);
      tpBestellung.refreshAktuellesPanel(); // den Titel des aktuellen Panels setzen
    }
  }
  
 /* protected void eventActionEscape(ActionEvent e)
  throws Throwable {
	  if (e.getActionCommand().equals(PanelBasis.ESC)||
			  e.getActionCommand().equals(ACTION_SPECIAL_CLOSE_PANELDIALOG)) {
		  eventActionSpecial(e);
	  }
  }*/

  /**
   * Den Kommentar in der Bestellung abspeichern.
   * @throws Throwable Ausnahme
   */
  public void saveKommentar() throws Throwable {
    if (lockMeBestellung != null) {
      checkLockedDlg(lockMeBestellung);
    }

    if (this.bInternerKommentar) {
      tpBestellung.getBesDto().setXInternerKommentar(getLpEditor().getText());
    }
    else {
      tpBestellung.getBesDto().setXExternerKommentar(getLpEditor().getText());
    }

    DelegateFactory.getInstance().getBestellungDelegate().updateBestellung(tpBestellung.getBesDto());
  }

  protected String getLockMeWer() {
    return HelperClient.LOCKME_BESTELLUNG;
  }


  protected void eventActionLock(ActionEvent e)
      throws Throwable {
    if (lockMeBestellung != null) {
      // Zugehoeriges Angebot locken.
      super.lock(lockMeBestellung);
    }
  }


  protected void eventActionUnlock(ActionEvent e)
      throws Throwable {
    if (lockMeBestellung != null) {
      // Zugehoeriges Angebot locken.
      super.unlock(lockMeBestellung);
    }
  }


  public LockStateValue getLockedstateDetailMainKey()
      throws Throwable {

    LockStateValue lockstateValue = super.getLockedstateDetailMainKey();
    if (lockstateValue.getIState() == LOCK_IS_NOT_LOCKED && lockMeBestellung != null) {
      int iLockstate = getLockedByWerWas(lockMeBestellung);

      if (iLockstate == LOCK_IS_LOCKED_BY_ME) {
        iLockstate = LOCK_IS_LOCKED_BY_OTHER_USER; // anderes Modul hat gelock zB Partner
      }

      lockstateValue.setIState(iLockstate);
    }

    return lockstateValue;
  }


  protected JComponent getFirstFocusableComponent()
      throws Exception {
    return NO_VALUE_THATS_OK_JCOMPONENT;
  }
}
