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
import com.lp.client.util.logger.LpLogger;
import com.lp.server.system.ejb.ParameteranwenderPK;
import com.lp.server.system.ejb.ParametermandantPK;
import com.lp.server.system.service.ArbeitsplatzDto;
import com.lp.server.system.service.ArbeitsplatzparameterDto;
import com.lp.server.system.service.MandantFac;
import com.lp.server.system.service.ParameterDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParameteranwenderDto;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.util.Helper;

@SuppressWarnings("static-access")
public class ParameterDelegate extends Delegate {
	private Context context;
	private ParameterFac parameterFac;
//	private MandantFac mandantFac;
//	private LpLogger myLogger = null;

	public ParameterDelegate() throws Exception {
		context = new InitialContext();
//		mandantFac = (MandantFac) context
//				.lookup("lpserver/MandantFacBean/remote");
		parameterFac = (ParameterFac) context
				.lookup("lpserver/ParameterFacBean/remote");
	}

	/**
	 * Das aktuelle Geschaeftsjahr holen
	 * 
	 * @return Integer
	 * @throws ExceptionLP
	 */
	public Integer getGeschaeftsjahr() throws ExceptionLP {
		try {
			return parameterFac.getGeschaeftsjahr(LPMain.getTheClient()
					.getMandant());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public ParameteranwenderPK createParameteranwender(
			ParameteranwenderDto parameteranwenderDtoI) throws ExceptionLP {
		ParameteranwenderPK pkParameteranwender = null;

		try {
			pkParameteranwender = parameterFac.createParameteranwender(
					parameteranwenderDtoI, LPMain.getInstance().getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return pkParameteranwender;
	}

	public Integer createArbeitsplatzparameter(
			ArbeitsplatzparameterDto arbeitsplatzparameterDto)
			throws ExceptionLP {
		try {
			return parameterFac
					.createArbeitsplatzparameter(arbeitsplatzparameterDto);
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}

	}

	public Integer createArbeitsplatz(ArbeitsplatzDto arbeitsplatzDto)
			throws ExceptionLP {
		try {
			return parameterFac.createArbeitsplatz(arbeitsplatzDto);
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}

	}

	public void removeParameteranwender(
			ParameteranwenderDto parameteranwenderDto) throws ExceptionLP {
		try {
			parameterFac.removeParameteranwender(parameteranwenderDto);
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public void updateParameteranwender(
			ParameteranwenderDto parameteranwenderDtoI) throws ExceptionLP {
		try {
			parameterFac.updateParameteranwender(parameteranwenderDtoI, LPMain
					.getInstance().getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public void updateArbeitsplatz(ArbeitsplatzDto arbeitsplatzDto)
			throws ExceptionLP {
		try {
			parameterFac.updateArbeitsplatz(arbeitsplatzDto);
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public void updateArbeitsplatzparameter(
			ArbeitsplatzparameterDto arbeitsplatzparameterDto)
			throws ExceptionLP {
		try {
			parameterFac.updateArbeitsplatzparameter(arbeitsplatzparameterDto);
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public ParameteranwenderDto parameteranwenderFindByPrimaryKey(
			ParameteranwenderPK pkParameteranwenderI) throws ExceptionLP {
		ParameteranwenderDto parameteranwenderDto = null;

		try {
			parameteranwenderDto = parameterFac
					.parameteranwenderFindByPrimaryKey(pkParameteranwenderI);
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return parameteranwenderDto;
	}

	public ArbeitsplatzDto arbeitsplatzFindByPrimaryKey(Integer IId)
			throws ExceptionLP {
		ArbeitsplatzDto arbeitsplatzDto = null;

		try {
			arbeitsplatzDto = parameterFac.arbeitsplatzFindByPrimaryKey(IId);
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return arbeitsplatzDto;
	}

	public ArbeitsplatzDto arbeitsplatzFindByCPcname(String pcname)
			throws ExceptionLP {
		ArbeitsplatzDto arbeitsplatzDto = null;

		try {
			arbeitsplatzDto = parameterFac.arbeitsplatzFindByCPcname(pcname);
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return arbeitsplatzDto;
	}

	public ArbeitsplatzDto arbeitsplatzFindByCTypCGeraetecode(String cTyp,
			String cGeraetecode) throws ExceptionLP {
		ArbeitsplatzDto arbeitsplatzDto = null;

		try {
			arbeitsplatzDto = parameterFac.arbeitsplatzFindByCTypCGeraetecode(
					cTyp, cGeraetecode);
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return arbeitsplatzDto;
	}

	public ParameterDto parameterFindByPrimaryKey(String cNr)
			throws ExceptionLP {
		ParameterDto parameterDto = null;

		try {
			parameterDto = parameterFac.parameterFindByPrimaryKey(cNr);
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return parameterDto;
	}

	public ArbeitsplatzparameterDto arbeitsplatzparameterFindByPrimaryKey(
			Integer IId) throws ExceptionLP {
		ArbeitsplatzparameterDto arbeitsplatzparameterDto = null;

		try {
			arbeitsplatzparameterDto = parameterFac
					.arbeitsplatzparameterFindByPrimaryKey(IId);
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return arbeitsplatzparameterDto;
	}

	public ArbeitsplatzparameterDto holeArbeitsplatzparameter(
			String parameterCNr) throws ExceptionLP {
		ArbeitsplatzparameterDto arbeitsplatzparameterDto = null;
		try {
			arbeitsplatzparameterDto = holeArbeitsplatzparameterImpl(
					Helper.getPCName(), parameterCNr);
			// arbeitsplatzparameterDto =
			// parameterFac.holeArbeitsplatzparameter(Helper.getPCName(),
			// parameterCNr);
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return arbeitsplatzparameterDto;
	}

	public ArbeitsplatzparameterDto holeArbeitsplatzparameter(String pcName,
			String parameterCNr) throws ExceptionLP {
		ArbeitsplatzparameterDto arbeitsplatzparameterDto = null;
		try {
			arbeitsplatzparameterDto = holeArbeitsplatzparameterImpl(pcName,
					parameterCNr);
			// arbeitsplatzparameterDto =
			// parameterFac.holeArbeitsplatzparameter(pcName,
			// parameterCNr);
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return arbeitsplatzparameterDto;
	}

	private ArbeitsplatzparameterDto holeArbeitsplatzparameterImpl(
			String pcName, String parameterCNr) throws Throwable {
		if (Helper.isStringEmpty(pcName))
			return null;

		String pc = pcName;
		if (pc.length() > 20) {
			// Die Parameter werden von der UI auf 20 Stellen (17 Stellen +
			// "...") gekuerzt gespeichert
			pc = pc.substring(0, 17) + "...";
			// System.out.println("Shortend Arbeitsplatzname is now <" + pc +
			// "> with a length of " + pc.length() + " chars.") ;
		}

		return parameterFac.holeArbeitsplatzparameter(pc, parameterCNr);
	}

	public ParametermandantPK createParametermandant(
			ParametermandantDto parametermandantDtoI) throws ExceptionLP {
		ParametermandantPK pkParametermandant = null;

		try {
			pkParametermandant = parameterFac.createParametermandant(
					parametermandantDtoI, LPMain.getInstance().getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return pkParametermandant;
	}

	public void removeParametermandant(ParametermandantDto parametermandantDtoI)
			throws ExceptionLP {
		try {
			parameterFac.removeParametermandant(parametermandantDtoI);
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public void removeArbeitsplatz(ArbeitsplatzDto arbeitsplatzDto)
			throws ExceptionLP {
		try {
			parameterFac.removeArbeitsplatz(arbeitsplatzDto);
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public void removeArbeitsplatzparameter(
			ArbeitsplatzparameterDto arbeitsplatzparameterDto)
			throws ExceptionLP {
		try {
			parameterFac.removeArbeitsplatzparameter(arbeitsplatzparameterDto);
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public void updateParametermandant(ParametermandantDto parametermandantDtoI)
			throws ExceptionLP {
		try {
			parameterFac.updateParametermandant(parametermandantDtoI, LPMain
					.getInstance().getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public ParametermandantDto parametermandantFindByPrimaryKey(
			ParametermandantPK pkParametermandantI) throws ExceptionLP {
		ParametermandantDto parametermandantDto = null;

		try {
			pkParametermandantI.setMandantCNr(LPMain.getInstance()
					.getTheClient().getMandant());

			parametermandantDto = parameterFac
					.parametermandantFindByPrimaryKey(pkParametermandantI);
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return parametermandantDto;
	}

	public Object getParametermandant(String mandantparameter_c_nr,
			String cKategorieI, String mandant_c_nr) throws ExceptionLP {
		Object parameter = null;

		try {
			parameter = parameterFac.getMandantparameter(mandant_c_nr,
					cKategorieI, mandantparameter_c_nr);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}

		return parameter;
	}

	public ParametermandantDto getMandantparameter(String mandant_c_nr,
			String cKategorieI, String mandantparameter_c_nr)
			throws ExceptionLP {
		ParametermandantDto parametermandantDto = null;

		try {
			parametermandantDto = parameterFac.getMandantparameter(
					mandant_c_nr, cKategorieI, mandantparameter_c_nr);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}

		return parametermandantDto;
	}

	public ParametermandantDto getMandantparameterReturnsNull(String cKategorieI, String mandantparameter_c_nr)
			throws ExceptionLP {
		try {
			return parameterFac.getMandantparameter(
					LPMain.getTheClient().getMandant(), cKategorieI, mandantparameter_c_nr);
		} catch (Throwable ex) {
			return null;
		}
	}

	public void createFixverdrahteteParametermandant() throws ExceptionLP {
		try {
			parameterFac.createFixverdrahteteParametermandant(LPMain
					.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}
}
