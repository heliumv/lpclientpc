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
package com.lp.client.frame.component;

import javax.swing.JComponent;

import com.lp.client.frame.ExceptionLP;
import com.lp.server.util.fastlanereader.service.query.FilterKriteriumDirekt;

public class PanelFilterKriteriumDirektTyped extends PanelFilterKriteriumDirekt {
	private JComponent wrapperField ;

	public PanelFilterKriteriumDirektTyped (FilterKriteriumDirekt fkd) throws ExceptionLP {
		super(fkd) ;

		if (fkd.iTyp == FilterKriteriumDirekt.TYP_STRING) {
			wrapperField = wtfFkdirektValue1 ;
		} else if (fkd.iTyp == FilterKriteriumDirekt.TYP_DECIMAL) {
			wnfFkdirektValue1 = new WrapperNumberField();
			wrapperField = wnfFkdirektValue1 ;
		}
	}

	public PanelFilterKriteriumDirektTyped(PanelFilterKriteriumDirekt panelFkd) throws Exception {
		super(panelFkd.fkd) ;

		if (fkd.iTyp == FilterKriteriumDirekt.TYP_STRING) {
			wrapperField = wtfFkdirektValue1 ;
			wtfFkdirektValue1.setText(panelFkd.wtfFkdirektValue1.getText()) ;
		} else if (fkd.iTyp == FilterKriteriumDirekt.TYP_DECIMAL) {
			wnfFkdirektValue1 = new WrapperNumberField();
			wrapperField = wnfFkdirektValue1 ;
			wnfFkdirektValue1.setBigDecimal(panelFkd.wnfFkdirektValue1.getBigDecimal()) ;
		}		
	}
	
	public WrapperLabel getLabel() {
		return wlaFkdirektName1 ;
	}
	
	public WrapperLabel getLabelOp() {
		return wlaFkdirektOperator1 ;
	}
	
	public String getLabelWithOp() {
		return getLabel().getText() + " " + getLabelOp().getText() ; 
	}
	
	public JComponent getWrapperField() {
		return wrapperField ;
	}	
	
	public WrapperTextField asTextField() {
		return (WrapperTextField) wrapperField ;
	}

	public WrapperNumberField asNumberField() {
		return (WrapperNumberField) wrapperField ;
	}
}
