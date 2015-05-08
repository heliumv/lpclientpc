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

import java.util.Set;

import javax.naming.Context;
import javax.naming.InitialContext;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.pc.LPMain;
import com.lp.server.system.service.PflegeFac;
import com.lp.server.system.service.SystemFac;
import com.lp.server.system.service.SystemMultilanguageFac;
import com.lp.server.system.service.SystemServicesFac;

public class PflegeDelegate extends Delegate {
	private Context context;

	private PflegeFac pflegeFac;

	public PflegeDelegate() throws ExceptionLP {
		try {
			context = new InitialContext(); // getInitialContext();
			pflegeFac = (PflegeFac) context
					.lookup("lpserver/PflegeFacBean/remote");

		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public String sp2486(Set<Integer> artikelId) throws ExceptionLP {
		try {
			return pflegeFac.sp2486(artikelId, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public void sp2597() throws ExceptionLP {
		try {
			pflegeFac.sp2597(LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);

		}
	}

	public void pj18612() throws ExceptionLP {
		try {
			pflegeFac.pj18612(LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);

		}
	}
}
