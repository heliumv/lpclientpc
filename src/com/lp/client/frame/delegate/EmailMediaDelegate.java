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
 *******************************************************************************/
package com.lp.client.frame.delegate;

import javax.naming.Context;
import javax.naming.InitialContext;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.pc.LPMain;
import com.lp.server.media.service.EmailMediaFac;
import com.lp.server.media.service.MediaEmailMetaDto;
import com.lp.server.media.service.MediaStoreBelegDto;
import com.lp.server.system.service.TheClientDto;

public class EmailMediaDelegate extends Delegate {
	private EmailMediaFac emailMediaFac ;
	private Context context ;
	
	public EmailMediaDelegate() throws ExceptionLP {
		try {
			context = new InitialContext();
			emailMediaFac = (EmailMediaFac) context.lookup("lpserver/EmailMediaFacBean/remote");
		} catch (Throwable t) {
			handleThrowable(t);
		}		
	}
	
	public MediaEmailMetaDto emailMetaFindByInboxId(Integer inboxId) throws ExceptionLP {
		MediaEmailMetaDto emailDto = null ;
		try {
			emailDto = emailMediaFac.emailMetaFindByInboxId(inboxId) ;
		} catch(Throwable t) {
			handleThrowable(t) ;
		}
		
		return emailDto ;
	}
	
	public void retrieveEmails() throws ExceptionLP {
		try {
			emailMediaFac.retrieveEmails(LPMain.getTheClient());
		} catch(Throwable t) {
			handleThrowable(t) ;
		}
	}
	
	public void versteckeInboxEntry(Integer inboxId) throws ExceptionLP {
		try {
			emailMediaFac.versteckeInboxEntry(inboxId) ;
		} catch(Throwable t) {
			handleThrowable(t);
		}
	}

	public void removeInboxEntry(Integer inboxId) throws ExceptionLP {
		try {
			emailMediaFac.removeInboxEntry(inboxId) ;
		} catch(Throwable t) {
			handleThrowable(t);
		}
	}
	
	public void removeEmail(String uuid) throws ExceptionLP {
		try {
			emailMediaFac.removeEmail(uuid, LPMain.getTheClient());
		} catch(Throwable t) {
			handleThrowable(t) ;
		}
	}
	
	public void moveInboxEntryTo(Integer inboxId, Integer personalId) throws ExceptionLP {
		try {
			emailMediaFac.moveInboxEntryTo(inboxId, personalId, LPMain.getTheClient());
		} catch(Throwable t) {
			handleThrowable(t) ;
		}
	}
	
	public Integer createKurzbriefFromEmail(Integer partnerId, 
			String belegartCnr, MediaEmailMetaDto emailMetaDto) throws ExceptionLP  {
		try {
			return emailMediaFac.createKurzbriefFromEmail(partnerId, belegartCnr, emailMetaDto, LPMain.getTheClient());
		} catch(Throwable t) {
			handleThrowable(t);
			return null ;
		}
	}
	
	public boolean hasKurzbriefEmailReferenz(Integer kurzbriefId, Integer partnerId) throws ExceptionLP {
		try {
			return emailMediaFac.hasKurzbriefEmailReferenz(kurzbriefId, partnerId, LPMain.getTheClient()) ;
		} catch(Throwable t) {
			handleThrowable(t) ;
			return false ;
		}
	}
	
	public Integer createHistoryFromEmail(Integer projektId, MediaEmailMetaDto emailMetaDto) throws ExceptionLP {
		try {
			return emailMediaFac.createHistoryFromEmail(projektId, emailMetaDto, LPMain.getTheClient()) ;
		} catch(Throwable t) {
			handleThrowable(t) ;
			return null ;
		}
	}

	public boolean hasHistoryEmailReferenz(Integer historyId, Integer projektId) throws ExceptionLP {
		try {
			return emailMediaFac.hasHistoryEmailReferenz(
					historyId, projektId, LPMain.getTheClient()) ;
		} catch(Throwable t) {
			handleThrowable(t) ;
			return false ;
		}
	}
	
	public MediaStoreBelegDto belegReferenzFindByReferenceId(Integer referenceId, boolean loadEmailMeta) throws ExceptionLP {
		try {
			return emailMediaFac.belegReferenzFindByReferenceId(
					referenceId, loadEmailMeta, LPMain.getTheClient()) ;
		} catch(Throwable t) {
			handleThrowable(t) ;
			return null ;
		}
	}
}
