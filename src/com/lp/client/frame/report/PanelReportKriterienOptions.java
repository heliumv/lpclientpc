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
package com.lp.client.frame.report;

import com.lp.client.frame.component.InternalFrame;
import com.lp.server.partner.service.PartnerDto;

public class PanelReportKriterienOptions {
	private InternalFrame internalFrame ;		
	private String addTitleI ;
	private PartnerDto partnerDtoEmpfaenger ;
	private Integer ansprechpartnerIId ;
	private boolean bDirekt ;
	private boolean bMitEmailFax ;
	private boolean bNurVorschau ;
	private boolean mitExitButton = true ;	

	public InternalFrame getInternalFrame() {
		return internalFrame;
	}
	public void setInternalFrame(InternalFrame internalFrame) {
		this.internalFrame = internalFrame;
	}
	public String getAddTitleI() {
		return addTitleI;
	}
	public void setAddTitleI(String addTitleI) {
		this.addTitleI = addTitleI;
	}
	public PartnerDto getPartnerDtoEmpfaenger() {
		return partnerDtoEmpfaenger;
	}
	public void setPartnerDtoEmpfaenger(PartnerDto partnerDtoEmpfaenger) {
		this.partnerDtoEmpfaenger = partnerDtoEmpfaenger;
	}
	public Integer getAnsprechpartnerIId() {
		return ansprechpartnerIId;
	}
	public void setAnsprechpartnerIId(Integer ansprechpartnerIId) {
		this.ansprechpartnerIId = ansprechpartnerIId;
	}
	public boolean isDirekt() {
		return bDirekt;
	}
	public void setDirekt(boolean bDirekt) {
		this.bDirekt = bDirekt;
		
		if(!bDirekt) {
			setMitEmailFax(false);
		}
	}
	public void beDirekt() {
		setDirekt(true) ;
	}
	
	public boolean isMitEmailFax() {
		return bMitEmailFax;
	}
	public void setMitEmailFax(boolean bMitEmailFax) {
		this.bMitEmailFax = bMitEmailFax;
	}
	public boolean isNurVorschau() {
		return bNurVorschau;
	}
	public void setNurVorschau(boolean bNurVorschau) {
		this.bNurVorschau = bNurVorschau;
	}
	public boolean isMitExitButton() {
		return mitExitButton;
	}
	public void setMitExitButton(boolean mitExitButton) {
		this.mitExitButton = mitExitButton;
	}	
}
