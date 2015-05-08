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
import com.lp.server.system.jcr.service.JCRMediaDto;
import com.lp.server.system.jcr.service.JCRMediaFac;

public class JCRMediaDelegate extends Delegate {
	private JCRMediaFac jcrMediaFac ;
	private Context context ;

	public JCRMediaDelegate() throws ExceptionLP {
		try {
			context = new InitialContext();
			jcrMediaFac = (JCRMediaFac) context.lookup("lpserver/JCRMediaFacBean/remote");
		} catch (Throwable t) {
			handleThrowable(t);
		}		
	}

	public JCRMediaDto findEmailByUUID(String uuid, boolean withContent) throws ExceptionLP {
		JCRMediaDto jcrMediaDto = null ;
		try {
			jcrMediaDto = jcrMediaFac.findEmailByUUID(uuid, withContent, LPMain.getTheClient()) ;
		} catch(Throwable t) {
			handleThrowable(t) ;
		}
		
		return jcrMediaDto ;
	}
}
