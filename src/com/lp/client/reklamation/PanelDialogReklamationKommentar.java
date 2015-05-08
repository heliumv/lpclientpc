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
 * <p><I>Dialog zur Eingabe des internen Kommentars fuer eines Personals.</I> </p>
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2007</p>
 * <p>Erstellungsdatum <I>30.09.05</I></p>
 * <p> </p>
 * @author Christian Kollmann
 * @version $Revision: 1.3 $
 */
public class PanelDialogReklamationKommentar
    extends PanelDialogKommentar
{
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

/** Cache for convenience. */
  private InternalFrameReklamation intFrame = null;

  /** Wenn der Dialog von aussen geoeffnet wird, muss ein Lock gesetzt werden. */
  private LockMeDto lockMePersonal = null;

  /** Der Kommentar kann intern oder extern sein. */
  private boolean bInternerKommentar = false;

  public PanelDialogReklamationKommentar(InternalFrame internalFrame,
                                      String add2TitleI, boolean bInternerKommentarI)
      throws Throwable {
    super(internalFrame, add2TitleI);

    intFrame = (InternalFrameReklamation) getInternalFrame();
    bInternerKommentar = bInternerKommentarI;

    jbInit();
    initComponents();
    dto2Components();
  }

  protected JComponent getFirstFocusableComponent()
      throws Exception {
    return NO_VALUE_THATS_OK_JCOMPONENT;
  }

  private void jbInit()
      throws Throwable {
    if (intFrame.getReklamationDto() != null) {
      lockMePersonal = new LockMeDto(
          HelperClient.LOCKME_REKLAMATION,
          intFrame.getReklamationDto().getIId() + "",
          LPMain.getInstance().getCNrUser());
    }

    eventActionLock(null);
  }


  private void dto2Components()
      throws Throwable {
    if (this.bInternerKommentar) {
      getLpEditor().setText(intFrame.getReklamationDto().getXKommentar());
      setCBestehenderKommentar(intFrame.getReklamationDto().getXKommentar());
    }
    else {
      getLpEditor().setText(intFrame.getReklamationDto().getXKommentar());
      setCBestehenderKommentar(intFrame.getReklamationDto().getXKommentar());
    }
  }


  protected void eventActionSpecial(ActionEvent e)
      throws Throwable {
    super.eventActionSpecial(e);
    if(super.bHandleEventInSuperklasse) {
      // explizit aufrufen, weil weder save noch discard aufgerufen werden
      eventActionUnlock(null);
    }
  }


  /**
   * Den Kommentar  abspeichern.
   * @throws Throwable Ausnahme
   */
  public void saveKommentar()
      throws Throwable {
    if (lockMePersonal != null) {
      checkLockedDlg(lockMePersonal);
    }

    if (this.bInternerKommentar) {
      intFrame.getReklamationDto().setXKommentar(getLpEditor().getText());
    }
    else {
      intFrame.getReklamationDto().setXKommentar(getLpEditor().getText());
    }

    DelegateFactory.getInstance().getReklamationDelegate().updateReklamationKommentar(intFrame.getReklamationDto());
  }


  protected String getLockMeWer() {
    return HelperClient.LOCKME_REKLAMATION;
  }


  protected void eventActionLock(ActionEvent e)
      throws Throwable {
    if (lockMePersonal != null) {
      // Zugehoeriges Angebot locken.
      super.lock(lockMePersonal);
    }
  }


  protected void eventActionUnlock(ActionEvent e)
      throws Throwable {
    if (lockMePersonal != null) {
      // Zugehoeriges Angebot locken.
      super.unlock(lockMePersonal);
    }
  }


  public LockStateValue getLockedstateDetailMainKey()
      throws Throwable {

    LockStateValue lockstateValue = super.getLockedstateDetailMainKey();
    if (lockstateValue.getIState() == LOCK_IS_NOT_LOCKED && lockMePersonal != null) {
      int iLockstate = getLockedByWerWas(lockMePersonal);

      if (iLockstate == LOCK_IS_LOCKED_BY_ME) {
        iLockstate = LOCK_IS_LOCKED_BY_OTHER_USER; // anderes Modul hat gelock zB Partner
      }

      lockstateValue.setIState(iLockstate);
    }

    return lockstateValue;
  }
}
