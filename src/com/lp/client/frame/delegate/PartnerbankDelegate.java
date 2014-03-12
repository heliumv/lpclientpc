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
package com.lp.client.frame.delegate;

import javax.naming.Context;
import javax.naming.InitialContext;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.pc.LPMain;
import com.lp.server.partner.service.BankDto;
import com.lp.server.partner.service.BankFac;
import com.lp.server.partner.service.PartnerbankDto;

public class PartnerbankDelegate extends Delegate {
	private Context context;
	private BankFac bankFac;

	public PartnerbankDelegate() throws Exception {

		context = new InitialContext();
		bankFac = (BankFac) context.lookup("lpserver/BankFacBean/remote");
	}

	public Integer createPartnerbank(PartnerbankDto partnerbankDto)
			throws ExceptionLP {
		Integer i = null;
		try {
			i = bankFac
					.createPartnerbank(partnerbankDto, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return i;
	}

	public void removePartnerbank(PartnerbankDto partnerbankDto)
			throws ExceptionLP {
		try {
			bankFac.removePartnerbank(partnerbankDto, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updatePartnerbank(PartnerbankDto partnerbankDto)
			throws ExceptionLP {
		try {
			bankFac.updatePartnerbank(partnerbankDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public PartnerbankDto partnerbankFindByPrimaryKey(Integer iId)
			throws ExceptionLP {

		PartnerbankDto p = null;
		try {
			p = bankFac.partnerbankFindByPrimaryKey(iId, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return p;
	}

	public Integer createBank(BankDto bankDto) throws ExceptionLP {

		Integer i = null;

		try {
			i = bankFac.createBank(bankDto, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return i;
	}

	public Integer getMaxISort(Integer partnerIId) throws ExceptionLP {

		Integer i = null;

		try {
			i = bankFac.getMaxISort(partnerIId);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return i;
	}

	public void removeBank(Integer partnerIId) throws ExceptionLP {

		try {
			bankFac.removeBank(partnerIId, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updateBank(BankDto bankDto) throws ExceptionLP {

		try {
			bankFac.updateBank(bankDto, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public BankDto bankFindByPrimaryKey(Integer partnerIId) throws ExceptionLP {
		BankDto b = null;
		try {
			b = bankFac.bankFindByPrimaryKey(partnerIId, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return b;
	}

	public BankDto bankFindByPrimaryKeyOhneExc(Integer partnerIId) throws ExceptionLP {
		BankDto b = null;
		try {
			b = bankFac.bankFindByPrimaryKeyOhneExc(partnerIId, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return b;
	}

}
