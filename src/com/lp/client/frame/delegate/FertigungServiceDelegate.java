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
package com.lp.client.frame.delegate;

import javax.naming.Context;
import javax.naming.InitialContext;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.pc.LPMain;
import com.lp.server.fertigung.service.FertigungServiceFac;
import com.lp.server.fertigung.service.LosbereichDto;
import com.lp.server.fertigung.service.LosklasseDto;
import com.lp.server.fertigung.service.LosklassesprDto;
import com.lp.server.fertigung.service.LosstatusDto;

/**
 * <p>
 * Diese Klasse kuemmert sich ...
 * </p>
 * 
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2008
 * </p>
 * 
 * <p>
 * Erstellung: Martin Bluehweis; 21.07.05
 * </p>
 * 
 * <p>
 * 
 * @author $Author: christian $
 *         </p>
 * 
 * @version not attributable Date $Date: 2012/07/26 07:51:56 $
 */
public class FertigungServiceDelegate extends Delegate {
	private Context context;
	private FertigungServiceFac fertigungServiceFac;

	public FertigungServiceDelegate() throws ExceptionLP {
		try {
			context = new InitialContext();
			fertigungServiceFac = (FertigungServiceFac) context
					.lookup("lpserver/FertigungServiceFacBean/remote");
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public LosstatusDto losstatusFindByPrimaryKey(String statusCNr)
			throws ExceptionLP {
		try {
			return fertigungServiceFac.losstatusFindByPrimaryKey(statusCNr);
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}
	}

	public Integer createLosklasse(LosklasseDto losklasseDto)
			throws ExceptionLP {
		try {
			return fertigungServiceFac.createLosklasse(losklasseDto,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Integer createLosbereich(LosbereichDto losbereichDto)
			throws ExceptionLP {
		try {
			return fertigungServiceFac.createLosbereich(losbereichDto,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public void removeLosklasse(LosklasseDto losklasseDto) throws ExceptionLP {
		try {
			fertigungServiceFac.removeLosklasse(losklasseDto,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void removeLosbereich(LosbereichDto losbereichDto)
			throws ExceptionLP {
		try {
			fertigungServiceFac.removeLosbereich(losbereichDto,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updateLosklasse(LosklasseDto losklasseDto) throws ExceptionLP {
		try {
			fertigungServiceFac.updateLosklasse(losklasseDto,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updateLosbereich(LosbereichDto losbereichDto)
			throws ExceptionLP {
		try {
			fertigungServiceFac.updateLosbereich(losbereichDto,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public LosklasseDto losklasseFindByPrimaryKey(Integer iId)
			throws ExceptionLP {
		try {
			return fertigungServiceFac.losklasseFindByPrimaryKey(iId,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public LosbereichDto losbereichFindByPrimaryKey(Integer iId)
			throws ExceptionLP {
		try {
			return fertigungServiceFac.losbereichFindByPrimaryKey(iId,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public LosklassesprDto losklassesprFindByPrimaryKey(Integer losklasseIId,
			String localeCNr) throws ExceptionLP {
		try {
			return fertigungServiceFac.losklassesprFindByPrimaryKey(
					losklasseIId, localeCNr);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}
}
