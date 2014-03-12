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
import com.lp.service.BelegVerkaufDto;
import com.lp.service.BelegpositionVerkaufDto;

public class IntelligenteZwischensummeController {
	private IPositionNumberHelper positionNumberHelper ;
	private BelegVerkaufDto belegDto ;
	private BelegpositionVerkaufDto belegPositionDto ;
	private Integer selectedPositionIId ;
	
	private boolean changed = false ;
	
	public IntelligenteZwischensummeController() {		
	}
	
	public IntelligenteZwischensummeController(IPositionNumberHelper positionNumberHelper, BelegVerkaufDto belegDto) {
		this.positionNumberHelper = positionNumberHelper ;
		this.belegDto = belegDto ;
	}

	public BelegVerkaufDto getBelegDto() {
		return belegDto;
	}

	public void setBelegDto(BelegVerkaufDto belegDto) {
		this.belegDto = belegDto;
	}

	public void setPositionNumberHelper(IPositionNumberHelper positionNumberHelper) {
		this.positionNumberHelper = positionNumberHelper ;	
	}
	
	public void setBelegPositionDto(BelegpositionVerkaufDto rechnungPosDto) {
		this.belegPositionDto = rechnungPosDto ;
	}
	
	public void setSelectedPositionIId (Integer selectedRechnungPositionIId) {
		this.selectedPositionIId = selectedRechnungPositionIId ;
	}

	
	public void setChanged(boolean changed) {
		this.changed = changed ;
	}

	public boolean getChanged() {
		return changed ;
	}
	
	
	public Integer getPositionIIdFromPositionNummer(Integer belegIId, Integer positionNumber) throws Throwable, ExceptionLP {
		return positionNumberHelper.getPositionIIdFromPositionNummer(belegIId, positionNumber) ;
	}

	public Integer getPositionNummer(Integer positionIId) throws Throwable, ExceptionLP {
		return positionNumberHelper.getPositionNummer(positionIId)  ;
		
	}
	public boolean isValidPositionNumber(int vonPositionNumber, int bisPositionNumber) throws Throwable, ExceptionLP {
		if(null == belegDto) return false ;

		Integer posNumber = null ;
		if(null == belegPositionDto.getIId()) {
			if(null == selectedPositionIId) {
				posNumber = positionNumberHelper.getHighestPositionNumber(belegDto.getIId()) ;				
			} else {
				posNumber = positionNumberHelper.getLastPositionNummer(selectedPositionIId) ;
			}
			
			// Eine neue Position am Ende aller bekannten Pos anlegen
			if((vonPositionNumber > posNumber) || (bisPositionNumber > posNumber)) return false ;
		} else {
			posNumber = positionNumberHelper.getLastPositionNummer(belegPositionDto.getIId()) ;
			if((vonPositionNumber >= posNumber) || (bisPositionNumber >= posNumber)) return false ;
		}

		
		return true ;
	}
	
	
	public boolean isValidMwstSatz(int vonPositionNumber, int bisPositionNumber) throws Throwable, ExceptionLP {
		if(null == belegDto) return false ;
		return positionNumberHelper.pruefeAufGleichenMwstSatz(belegDto.getIId(), vonPositionNumber, bisPositionNumber) ;
	}
	
	
	public boolean isValidPositionNumber(Integer positionNumber, BelegpositionVerkaufDto belegPosDto) {
		if(positionNumber < 1) return false ;

		try {
			if(!existsPositionNumber(positionNumber)) return false ;

			if(null == belegDto) return false ;
			
			if(null == belegPosDto.getIId()) {
				
			} else {
				Integer posIId = positionNumberHelper.getPositionIIdFromPositionNummer(belegDto.getIId(), positionNumber) ;
				if(posIId == belegPosDto.getIId()) return false ;
				
				Integer posNumber = positionNumberHelper.getPositionNummer(belegPosDto.getIId()) ;
				if(positionNumber >= posNumber) return false ;
				
			}
		} catch(Throwable t) {
			return false ;
		}
		
		return true ;
	}

	protected boolean existsPositionNumber(Integer positionNumber) throws ExceptionLP, Throwable {
		if(null == belegDto) return false ;
		
		Integer positionId = positionNumberHelper.getPositionIIdFromPositionNummer(belegDto.getIId(), positionNumber) ;
		return positionId != null ;
	}
}
