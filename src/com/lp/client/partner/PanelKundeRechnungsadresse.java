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
package com.lp.client.partner;


import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.EventObject;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.LockStateValue;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.WrapperGotoButton;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPButtonAction;
import com.lp.client.pc.LPMain;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.system.service.LockMeDto;
import com.lp.util.EJBExceptionLP;


/**
 * <p> Diese Klasse kuemmert sich die Kundenrechnungsadresse.</p>
 *
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 *
 * <p>Erstellung: Josef Ornetsmueller; 07.04.05</p>
 *
 * <p>@author $Author: Gerold $</p>
 *
 * @version $Revision: 1.5 $
 * Date $Date: 2012/09/14 13:15:31 $
 */
public class PanelKundeRechnungsadresse
    extends PanelPartnerDetail
{
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final static String EXTRA_NEU_AUS_KUNDE = "neu_aus_kunde";
	private PanelQueryFLR panelKunde = null;
	protected LockMeDto lockMePartner = null;
	private LockMeDto lockMePartnerRE = null;
	private LockMeDto lockMePartnerZuKunde = null;

	private WrapperGotoButton wbuKunde = new WrapperGotoButton(
			WrapperGotoButton.GOTO_KUNDE_AUSWAHL) ;
	
	public PanelKundeRechnungsadresse(InternalFrame internalFrame,
			String add2TitleI, Object keyI) throws Throwable {

		super(internalFrame, add2TitleI, keyI);

		jbInit();
		initComponents();

		disableUpdateButton();
	}

	private void jbInit() throws Throwable {
		createAndSaveAndShowButton(
				// "/com/lp/client/res/businessmen16x16.png",
				"/com/lp/client/res/handshake16x16.png",
				LPMain.getInstance().getTextRespectUISPr(
						"part.tooltip.rechnungsadr_from_kunde"),
				PanelBasis.ACTION_MY_OWN_NEW + EXTRA_NEU_AUS_KUNDE);
		
		wbuKunde.setText(LPMain.getTextRespectUISPr("button.kunde"));
		wbuKunde.setToolTipText(LPMain
				.getTextRespectUISPr("button.kunde.tooltip"));
		wbuKunde.setVisible(true) ;
		
		jpaWorkingOn.add(wbuKunde, new GridBagConstraints(2, 0, 1, 1,
				0.4, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(1, 2, 1, 2), 0, 0));		
	}

	protected void setDefaults() throws Throwable {
		super.setDefaults();
		beReadonly();
	}

	/**
	 * Den Update-Button im Toolspanel sperren
	 */
	protected void disableUpdateButton() {
		LPButtonAction buttonItem = getHmOfButtons().get(ACTION_UPDATE);
		buttonItem.setEnable(false);
		buttonItem.getButton().setEnabled(false);
	}

  	protected void synchronizeKundeGotoButton(Integer newValue) {
  		wbuKunde.setOKey(newValue) ;
		wbuKunde.setActivatable(null != newValue) ;
		wbuKunde.setEnabled(false) ;
  	}

  	protected void setPartnerDto(PartnerDto partnerDto) {
		getInternalFrameKunde().getKundeDto().setPartnerRechnungsadresseDto(
				partnerDto);
	}

	protected PartnerDto getPartnerDto() {
		return getInternalFrameKunde().getKundeDto().getPartnerRechnungsadresseDto();
	}

	private InternalFrameKunde getInternalFrameKunde() {
		return (InternalFrameKunde) getInternalFrame();
	}

  	protected Integer getKundeIIdFromRechnungsadresseIId(Integer rechnungsadresseIId) {
  		try {
			KundeDto kundeDto = DelegateFactory
					.getInstance()
					.getKundeDelegate()
					.kundeFindByiIdPartnercNrMandantOhneExc(
							(Integer) rechnungsadresseIId,
							LPMain.getTheClient().getMandant());
			return kundeDto.getIId() ;
  		} catch(Throwable t) {
  		}

  		return null ;
  	}
  	
	@Override
	protected void dto2Components() throws Throwable {
		super.dto2Components();
		synchronizeKundeGotoButton(getKundeIIdFromRechnungsadresseIId(getPartnerDto().getIId())) ;
	}

	
public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
      throws Throwable {

    super.eventYouAreSelected(true);

    disableUpdateButton() ;
    
    if (!bNeedNoYouAreSelectedI) {
      Object iId = getInternalFrameKunde().getKundeDto().getIId();

      // Kunde lesen.
      getInternalFrameKunde().setKundeDto(
          (DelegateFactory.getInstance().getKundeDelegate().kundeFindByPrimaryKey( (
              Integer) iId)));

      getInternalFrame().setLpTitle(
          InternalFrame.TITLE_IDX_AS_I_LIKE,
          getInternalFrameKunde().getKundeDto().getPartnerDto().formatFixTitelName1Name2());

      if (getInternalFrameKunde().getKundeDto().getPartnerIIdRechnungsadresse() == null) {
        leereAlleFelder(this);
        setDefaults();
        clearStatusbar();
        ( (LPButtonAction) getHmOfButtons().get(ACTION_DELETE)).getButton().setEnabled(false);
        wbuKunde.setActivatable(false) ;
      }
      else {
        getInternalFrame().setLpTitle(
            InternalFrame.TITLE_IDX_AS_I_LIKE,
            getInternalFrameKunde().getKundeDto().getPartnerDto().formatTitelAnrede());

        wbuKunde.setActivatable(true) ;
        
        setStatusbar();

        String t1 = getInternalFrameKunde().getKundeDto().getPartnerDto().
            formatFixTitelName1Name2();
        getInternalFrame().setLpTitle(
            InternalFrame.TITLE_IDX_AS_I_LIKE,
            t1 + " | " + getInternalFrameKunde().getKundeDto().
            getPartnerRechnungsadresseDto().formatAnrede());

        dto2Components();
      }
    }
  }


  protected void eventActionLock(ActionEvent e)
      throws Throwable {

    // Kunde locken.
    super.eventActionLock(e);

    // Partner RE locken.
    lockMePartnerRE = new LockMeDto(
        HelperClient.LOCKME_PARTNER,
        getInternalFrameKunde().getKundeDto().getPartnerIIdRechnungsadresse() + "",
        LPMain.getInstance().getCNrUser());
    if (getLockedByWerWas(lockMePartnerRE) == LOCK_IS_NOT_LOCKED) {
      super.lock(lockMePartnerRE);
    }
    else {
      clearRE();
      throw new ExceptionLP(EJBExceptionLP.FEHLER_IS_ALREADY_LOCKED, null);
    }

    // Zugehoerigen Partner zum Kunden locken.
    lockMePartnerZuKunde = new LockMeDto(
        HelperClient.LOCKME_PARTNER,
        getInternalFrameKunde().getKundeDto().getPartnerDto().getIId() + "",
        LPMain.getInstance().getCNrUser());
    if (getLockedByWerWas(lockMePartnerZuKunde) == LOCK_IS_NOT_LOCKED) {
      super.lock(lockMePartnerZuKunde);
    }
    else {
      clearRE();
      super.unlock(lockMePartnerRE);
      throw new ExceptionLP(EJBExceptionLP.FEHLER_IS_ALREADY_LOCKED, null);
    }
  }


  private void clearRE() throws Throwable {
    setPartnerDto(null);
    getInternalFrameKunde().getKundeDto().setPartnerIIdRechnungsadresse(null);
    synchronizeKundeGotoButton(null) ; 	
    
    super.eventActionUnlock(null);
    super.eventActionRefresh(null, false);

    LockStateValue lockstateValue = new LockStateValue(
        null,
        null,
        PanelBasis.LOCK_IS_NOT_LOCKED);
    updateButtons(lockstateValue);
  }


  protected void eventActionUnlock(ActionEvent e)
      throws Throwable {

    // Kunde unlocken.
    super.eventActionUnlock(e);

    // Partner RE unlocken.
    if (getLockedByWerWas(lockMePartnerRE) == LOCK_IS_LOCKED_BY_ME) {
      super.unlock(lockMePartnerRE);
    }
    else {
      throw new ExceptionLP(EJBExceptionLP.FEHLER_LOCK_NOTFOUND, null);
    }

    // Zugehoerigen Partner zum Kunden unlocken.
    if (getLockedByWerWas(lockMePartnerZuKunde) == LOCK_IS_LOCKED_BY_ME) {
      super.unlock(lockMePartnerZuKunde);
    }
    else {
      throw new ExceptionLP(EJBExceptionLP.FEHLER_LOCK_NOTFOUND, null);
    }
  }


  /**
   * Behandle Ereignis Save.
   *
   * @param e Ereignis
   * @param bNeedNoSaveI boolean
   * @throws Throwable
   */
  public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
      throws Throwable {

    if (allMandatoryFieldsSetDlg()) {
      components2Dto();

      KundeDto kundeDto = getInternalFrameKunde().getKundeDto();
      //damit die Debitorenkto. nicht anschlaegt.
      getInternalFrameKunde().getKundeDto().setUpdateModeDebitorenkonto(KundeDto.
          I_UPD_DEBITORENKONTO_KEIN_UPDATE);

      if (kundeDto.getIId() == null) {
        // Create.
        throw new Exception("kundeDto.getIId() == null");
      }
      else {
        // Update.
        DelegateFactory.getInstance().getKundeDelegate().updateKundeRechnungsadresse(
            kundeDto);
      }

      super.eventActionSave(e, true);

      if (getInternalFrame().getKeyWasForLockMe() == null) {
        // Der erste Eintrag wurde angelegt.
        getInternalFrame().setKeyWasForLockMe(getKeyWhenDetailPanel() + "");
      }

      eventYouAreSelected(false);
      setStatusbar();
      dto2Components();
    }
  }


	protected void eventActionDelete(ActionEvent e,
			boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {

		if (getLockedstateDetailMainKey().getIState() != PanelBasis.LOCK_IS_LOCKED_BY_ME) {
			DelegateFactory
					.getInstance()
					.getKundeDelegate()
					.removeKundeRechnungsadresse(
							getInternalFrameKunde().getKundeDto());

			super.eventActionDelete(e, false, true);
		}
	}

	protected void eventActionRefresh(ActionEvent e, boolean bNeedNoRefreshI)
			throws Throwable {
		super.eventActionRefresh(e, bNeedNoRefreshI);

		if (getInternalFrameKunde().getKundeDto()
				.getPartnerIIdRechnungsadresse() == null) {
			((LPButtonAction) getHmOfButtons().get(ACTION_DELETE)).getButton()
					.setEnabled(false);
			synchronizeKundeGotoButton(null) ;
		}

		disableUpdateButton();
	}

	

	protected void eventItemchanged(EventObject eI)
      throws Throwable {

    ItemChangedEvent e = (ItemChangedEvent) eI;

    super.eventItemchanged(eI);

    if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {

      if (e.getSource() == panelKunde) {
    	  // Neu aus Partner.
          beReadonly() ;
    	  
        Integer iIdKundeRE = (Integer) panelKunde.getSelectedId();

        eventYouAreSelected(false);

        KundeDto kundeDto = DelegateFactory.getInstance().getKundeDelegate().
            kundeFindByPrimaryKey(
            		iIdKundeRE);

        setPartnerDto(kundeDto.getPartnerDto());
        getInternalFrameKunde().getKundeDto().setPartnerIIdRechnungsadresse(kundeDto.getPartnerIId());

        LockStateValue lockstateValue = new LockStateValue(
            null,
            null,
            PanelBasis.LOCK_IS_NOT_LOCKED);
        updateButtons(lockstateValue);

        eventActionUpdate(null, false);

        setPartnerDto(kundeDto.getPartnerDto());
        getInternalFrameKunde().getKundeDto().setPartnerIIdRechnungsadresse(kundeDto.getPartnerIId());
        getInternalFrameKunde().getKundeDto().setPartnerRechnungsadresseDto(kundeDto.getPartnerDto());

        dto2Components();
        
        // Goto soll noch nicht aktivierbar sein, da Satz noch nicht gespeichert.
        wbuKunde.getWrapperButtonGoTo().setEnabled(false) ; 
        beReadonly() ;
      }
      clearStatusbar();
    }
  }


  protected void eventActionSpecial(ActionEvent e)
      throws Throwable {

    super.eventActionSpecial(e);

    if (e.getActionCommand().endsWith(EXTRA_NEU_AUS_KUNDE)) {
    	panelKunde = PartnerFilterFactory.getInstance().createPanelFLRKunde(
          getInternalFrame(),true,false);
      DialogQuery dialogQueryKunde = new DialogQuery(panelKunde);
    }
  }


  protected String getLockMeWer()
      throws Exception {
    // Kunde ist hier richtig, getLockMe holt die Kundenid.
    return HelperClient.LOCKME_KUNDE;
  }
}
