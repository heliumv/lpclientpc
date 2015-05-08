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
package com.lp.client.finanz;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.finanz.service.FinanzamtDto;
import com.lp.server.system.service.TheClientDto;

public class FinanzamtController implements IFinanzamtController {

	private Integer finanzamtIId ;
	private Integer steuerkategorieIId ;
	private TheClientDto theClientDto ;
	
	public FinanzamtController(TheClientDto theClientDto) {
		this.theClientDto = theClientDto ;
	}
	
	@Override
	public void setFinanzamtIId(Integer finanzamtIId) {
		this.finanzamtIId = finanzamtIId ;
		setSteuerkategorieIId(null) ;
	}

	@Override
	public Integer getFinanzamtIId() {
		return finanzamtIId ;
	}

	@Override
	public FinanzamtDto getFinanzamtDto() throws ExceptionLP {
		if(null == getFinanzamtIId()) return null ;
		
		return DelegateFactory.getInstance().getFinanzDelegate().
				finanzamtFindByPrimaryKey(getFinanzamtIId(), theClientDto.getMandant()) ;
	}

	
	@Override
	public Integer getSteuerkategorieIId() {
		return steuerkategorieIId;
	}

	@Override
	public void setSteuerkategorieIId(Integer steuerkategorieIId) {
		this.steuerkategorieIId = steuerkategorieIId;
	}

	@Override
	public boolean createDefaultSteuerkategorie(Integer finanzamtIId) {
		if(null == finanzamtIId) return false ;

		boolean result = false ;
		try {
			DelegateFactory.getInstance().getFinanzServiceDelegate()
				.createIfNeededSteuerkategorieForFinanzamtIId(finanzamtIId) ;
			result = true ;
		} catch(ExceptionLP e) {
		} catch(Throwable t) {
		}
		
		return result ;
	}

	@Override
	public boolean createDefaultSteuerkategoriekonto(Integer finanzamtIId) {
		if(null == finanzamtIId) return false ;

		boolean result = false ;
		try {
			DelegateFactory.getInstance().getFinanzServiceDelegate()
				.createIfNeededSteuerkategoriekontoForFinanzamtIId(finanzamtIId) ;
			result = true ;
		} catch(ExceptionLP e) {
		} catch(Throwable t) {
		}
		
		return result ;
	}

	@Override
	public boolean createDefaultSteuerkategorie() {
		return createDefaultSteuerkategorie(getFinanzamtIId()) ;
	}

	
	@Override
	public boolean createDefaultSteuerkategoriekonto() {
		return createDefaultSteuerkategoriekonto(getFinanzamtIId()) ;
	}

}
