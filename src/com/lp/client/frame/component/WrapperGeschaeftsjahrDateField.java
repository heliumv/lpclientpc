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
package com.lp.client.frame.component;

import java.sql.Timestamp;
import java.util.Date;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.delegate.DelegateFactory;

public class WrapperGeschaeftsjahrDateField extends WrapperDateField {
	private static final long serialVersionUID = 1L;

	private Integer geschaeftsjahr ;

	public WrapperGeschaeftsjahrDateField(int geschaeftsjahr) {
		super() ;
		setGeschaeftsjahr(geschaeftsjahr) ;
	}

	/**
	 * Ein neues Geschaeftsjahr und somit auch Datumsbegrenzungen setzen
	 * 
	 * @param newGeschaeftsjahr
	 */
	public void setGeschaeftsjahr(int newGeschaeftsjahr) {
		try {
			Timestamp[] d = DelegateFactory.getInstance().getBuchenDelegate().getDatumVonBisGeschaeftsjahr(newGeschaeftsjahr) ;
			if(null != d) {
				this.geschaeftsjahr = newGeschaeftsjahr ;
				setMinimumValue(d[0]) ;
				setMaximumValue(d[1]) ;
			}
		} catch(ExceptionLP e) {
		} catch(Throwable t) {			
		}
	}
	
	public int getGeschaeftsjahr() {
		return geschaeftsjahr ;
	}

	@Override
	public void setDate(Date date) {
//		if(date != null) {
//			if(date.before(getMinSelectableDate())) {
//				date = getMinSelectableDate() ;
//			}
//			if(date.after(getMaxSelectableDate())) {
//				date = getMaxSelectableDate() ;
//			}
//		}
		
		super.setDate(date);
	}
	
	
	/**
	 * Das Default-Datum setzen
	 * 
	 * Das Default-Datum ist "heute", ausser heute > Geschaeftsjahresende, dann
	 * wird das Default-Datum auf das Geschaeftsjahresende gesetzt.
	 */
	public void setDefaultDate() {
		Date now = new java.util.Date(System.currentTimeMillis());
		if(now.after(getMaxSelectableDate())) {
			now = getMaxSelectableDate() ;
		}
		
		if(now.before(getMinSelectableDate())) {
			now = getMinSelectableDate() ;
		}
		
		super.setDate(now) ;
	}
}
