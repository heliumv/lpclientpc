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

/**
 * <p><I>Diese Klasse kuemmert sich um den Versand</I> </p>
 *
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 *
 * <p>Erstellungsdatum <I>09.03.05</I></p>
 *
 * <p> </p>
 *
 * @author  Martin Bluehweis
 * @version $Revision: 1.8 $
 */
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.ArrayList;

import javax.naming.Context;
import javax.naming.InitialContext;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.pc.LPMain;
import com.lp.server.system.service.MailtextDto;
import com.lp.server.system.service.VersandFac;
import com.lp.server.system.service.VersandanhangDto;
import com.lp.server.system.service.VersandauftragDto;
import com.lp.util.EJBExceptionLP;

@SuppressWarnings("static-access")
public class VersandDelegate extends Delegate {
	private VersandFac versandFac;
	private Context context;

	public VersandDelegate() throws ExceptionLP {
		try {
			context = new InitialContext();
			versandFac = (VersandFac) context
					.lookup("lpserver/VersandFacBean/remote");
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public VersandauftragDto updateVersandauftrag(
			VersandauftragDto versandauftragDto, boolean bDokumenteanhangen)
			throws ExceptionLP {
		try {
			if (versandauftragDto.getIId() == null) {
				return versandFac
						.createVersandauftrag(versandauftragDto,
								bDokumenteanhangen, LPMain.getInstance()
										.getTheClient());
			} else {
				return versandFac.updateVersandauftrag(versandauftragDto,
						LPMain.getInstance().getTheClient());
			}
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public VersandanhangDto createVersandanhang(
			VersandanhangDto versandanhangDto) throws ExceptionLP {
		try {
			return versandFac.createVersandanhang(versandanhangDto, LPMain
					.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public void createVersandanhaenge(ArrayList<VersandanhangDto> alAnhaenge)
			throws ExceptionLP {
		try {
			versandFac.createVersandanhaenge(alAnhaenge, LPMain.getInstance()
					.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);

		}
	}

	public VersandanhangDto[] VersandanhangFindByVersandauftragIID(
			Integer versandauftragIID) throws RemoteException, EJBExceptionLP {
		return versandFac
				.VersandanhangFindByVersandauftragIID(versandauftragIID);
	}

	public VersandauftragDto versandauftragFindByPrimaryKey(Integer iId)
			throws ExceptionLP {
		try {
			return versandFac.versandauftragFindByPrimaryKey(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Integer versandauftragFindFehlgeschlagenen() throws ExceptionLP {
		try {
			return versandFac.versandauftragFindFehlgeschlagenen(LPMain
					.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public void sendeVersandauftragErneut(Integer versandauftragIId,
			Timestamp tWunschSendeZeitpunkt) throws ExceptionLP {
		try {
			versandFac.sendeVersandauftragErneut(versandauftragIId,
					tWunschSendeZeitpunkt, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void storniereVersandauftrag(Integer versandauftragIId)
			throws ExceptionLP {
		try {
			versandFac.storniereVersandauftrag(versandauftragIId, LPMain
					.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void removeVersandauftrag(Integer versandauftragIId)
			throws ExceptionLP {
		try {
			versandFac.removeVersandauftrag(versandauftragIId, LPMain
					.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public String getDefaultTextForBelegEmail(MailtextDto mailtextDto)
			throws ExceptionLP {
		try {
			return versandFac.getDefaultTextForBelegEmail(mailtextDto, LPMain
					.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public String getDefaultTextForBelegHtmlEmail(MailtextDto mailtextDto)
			throws ExceptionLP {
		try {
			return versandFac.getDefaultTextForBelegHtmlEmail(mailtextDto,
					LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public String getDefaultDateinameForBelegEmail(String beleggartCNr,
			Integer belegIId) throws ExceptionLP {
		try {
			return versandFac.getDefaultDateinameForBelegEmail(beleggartCNr,
					belegIId, LPMain.getInstance().getTheClient().getLocUi(),
					LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public String getDefaultBetreffForBelegEmail(MailtextDto mailtextDto,
			String beleggartCNr, Integer belegIId) throws ExceptionLP {
		try {
			return versandFac.getDefaultBetreffForBelegEmail(mailtextDto,
					beleggartCNr, belegIId, LPMain.getInstance().getTheClient()
							.getLocUi(), LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public void sendeVersandauftragSofort(Integer versandauftragIId)
			throws ExceptionLP {
		try {
			versandFac.sendeVersandauftragSofort(versandauftragIId, LPMain
					.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public String getVersandstatus(String belegartCNr, Integer i_belegIId)
			throws ExceptionLP {
		try {
			return versandFac.getVersandstatus(belegartCNr, i_belegIId, LPMain
					.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return null;
	}

	public String getFormattedVersandstatus(String belegartCNr,
			Integer i_belegIId) throws ExceptionLP {
		try {
			return versandFac.getFormattedVersandstatus(belegartCNr,
					i_belegIId, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return null;
	}

}
