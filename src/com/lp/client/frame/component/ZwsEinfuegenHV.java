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
package com.lp.client.frame.component;

import com.lp.client.frame.ExceptionLP;
import com.lp.server.auftrag.service.AuftragServiceFac;
import com.lp.service.BelegpositionDto;
import com.lp.service.BelegpositionVerkaufDto;

public abstract class ZwsEinfuegenHV<T> {
	public void processZwsPositions(BelegpositionVerkaufDto[] newPositionsDto, BelegpositionDto[] source) throws ExceptionLP {
		for(int i = 0; i < source.length; i++) {
			if(!AuftragServiceFac
					.AUFTRAGPOSITIONART_INTELLIGENTE_ZWISCHENSUMME.equals(source[i].getPositionsartCNr())) continue ; 
			Integer vonId = ((BelegpositionVerkaufDto)source[i]).getZwsVonPosition() ;
			if(vonId == null) continue ;
			Integer vonIndex = findArrayIndexFor(vonId, source) ;
			if(vonIndex == null) continue ;
			
			newPositionsDto[i].setZwsVonPosition(newPositionsDto[vonIndex].getIId()) ;
			
			Integer bisId = ((BelegpositionVerkaufDto)source[i]).getZwsBisPosition() ;
			if(bisId == null) continue ;
			Integer bisIndex = findArrayIndexFor(bisId, source) ;
			if(bisIndex == null) continue ;
			
			newPositionsDto[i].setZwsBisPosition(newPositionsDto[bisIndex].getIId()) ;
			
			updateZwsPosition((T)newPositionsDto[i]) ;
		}
	}
	
	
	private Integer findArrayIndexFor(Integer iid, BelegpositionDto[] source) {
		for(int i = 0; i < source.length; i++) {
			if(source[i].getIId().equals(iid)) return i ;
		}
		return null ;
	}
	
	protected abstract void updateZwsPosition(T positionDto) throws ExceptionLP  ;
}
