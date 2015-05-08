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

import com.lp.client.frame.HelperClient;
import com.lp.client.frame.LockStateValue;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.PanelDialogKommentar;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.system.service.LockMeDto;


/**
 * <p><I>Dialog zur Eingabe des internen Kommentars fuer einen Auftrag.</I> </p>
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 * <p>Erstellungsdatum <I>29.03.06</I></p>
 * <p> </p>
 * @author Uli Walch
 * @version $Revision: 1.3 $
 */
public class PanelDialogAuftragKommentar
    extends PanelDialogKommentar
{
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
/** Cache for convenience. */
  private InternalFrameAuftrag intFrame = null;
  /** Cache for convenience. */
  private TabbedPaneAuftrag tpAuftrag = null;

  /** Wenn der Dialog von aussen geoeffnet wird, muss ein Lock gesetzt werden. */
  private LockMeDto lockMeAuftrag = null;

  /** Der Kommentar kann intern oder extern sein. */
  private boolean bInternerKommentar = false;

  public PanelDialogAuftragKommentar(InternalFrame internalFrame,
                                             String add2TitleI, boolean bInternerKommentarI)
      throws Throwable {
    super(internalFrame, add2TitleI);

    intFrame = (InternalFrameAuftrag) getInternalFrame();
    tpAuftrag = intFrame.getTabbedPaneAuftrag();
    bInternerKommentar = bInternerKommentarI;

    jbInit();
    initComponents();
    dto2Components();
  }


  private void jbInit()
      throws Throwable {
    if (tpAuftrag.getAuftragDto() != null) {
      lockMeAuftrag = new LockMeDto(
          HelperClient.LOCKME_AUFTRAG,
          tpAuftrag.getAuftragDto().getIId() + "",
          LPMain.getInstance().getCNrUser());
    }

    // explizit ausloesen, weil weder new noch update aufgerufen werden
    eventActionLock(null);
  }


  private void dto2Components()
      throws Throwable {
    if (bInternerKommentar) {
      getLpEditor().setText(tpAuftrag.getAuftragDto().getXInternerkommentar());
      setCBestehenderKommentar(tpAuftrag.getAuftragDto().getXInternerkommentar());
    }
    else {
      getLpEditor().setText(tpAuftrag.getAuftragDto().getXExternerkommentar());
      setCBestehenderKommentar(tpAuftrag.getAuftragDto().getXExternerkommentar());
    }
  }


  protected void eventActionSpecial(ActionEvent e)
      throws Throwable {
    super.eventActionSpecial(e);
    if(super.bHandleEventInSuperklasse) {
      // explizit aufrufen, weil weder save noch discard aufgerufen werden
      eventActionUnlock(null);
      tpAuftrag.refreshAktuellesPanel(); // den Titel des aktuellen Panels setzen
    }
  }

  /**
   * Den Kommentar im Auftrag abspeichern.
   * @throws Throwable Ausnahme
   */
  public void saveKommentar() throws Throwable {
    if (lockMeAuftrag != null) {
      checkLockedDlg(lockMeAuftrag);
    }

    if (this.bInternerKommentar) {
      tpAuftrag.getAuftragDto().setXInternerkommentar(getLpEditor().getText());
    }
    else {
      tpAuftrag.getAuftragDto().setXExternerkommentar(getLpEditor().getText());
    }

    DelegateFactory.getInstance().getAuftragDelegate().updateAuftragOhneWeitereAktion(tpAuftrag.
        getAuftragDto());
  }


  protected String getLockMeWer() {
    return HelperClient.LOCKME_AUFTRAG;
  }


  protected void eventActionLock(ActionEvent e)
      throws Throwable {
    if (lockMeAuftrag != null) {
      // Zugehoeriges Auftrag locken.
      super.lock(lockMeAuftrag);
    }
  }


  protected void eventActionUnlock(ActionEvent e)
      throws Throwable {
    if (lockMeAuftrag != null) {
      // Zugehoeriges Auftrag locken.
      super.unlock(lockMeAuftrag);
    }
  }


  public LockStateValue getLockedstateDetailMainKey()
      throws Throwable {

    LockStateValue lockstateValue = super.getLockedstateDetailMainKey();
    if (lockstateValue.getIState() == LOCK_IS_NOT_LOCKED && lockMeAuftrag != null) {
      int iLockstate = getLockedByWerWas(lockMeAuftrag);

      if (iLockstate == LOCK_IS_LOCKED_BY_ME) {
        iLockstate = LOCK_IS_LOCKED_BY_OTHER_USER; // anderes Modul hat gelock zB Partner
      }

      lockstateValue.setIState(iLockstate);
    }

    return lockstateValue;
  }
}
