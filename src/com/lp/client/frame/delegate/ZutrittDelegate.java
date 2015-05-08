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

import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.pc.LPMain;
import com.lp.server.personal.service.FingerartDto;
import com.lp.server.personal.service.PersonalfingerDto;
import com.lp.server.personal.service.PersonalzutrittsklasseDto;
import com.lp.server.personal.service.ZutrittdaueroffenDto;
import com.lp.server.personal.service.ZutrittonlinecheckDto;
import com.lp.server.personal.service.ZutrittscontrollerDto;
import com.lp.server.personal.service.ZutrittscontrollerFac;
import com.lp.server.personal.service.ZutrittsklasseDto;
import com.lp.server.personal.service.ZutrittsklasseobjektDto;
import com.lp.server.personal.service.ZutrittsmodellDto;
import com.lp.server.personal.service.ZutrittsmodelltagDto;
import com.lp.server.personal.service.ZutrittsmodelltagdetailDto;
import com.lp.server.personal.service.ZutrittsobjektDto;
import com.lp.server.personal.service.ZutrittsobjektverwendungDto;

public class ZutrittDelegate extends Delegate {
	private ZutrittscontrollerFac zutrittscontrollerFac;

	private Context context;

	public ZutrittDelegate() throws ExceptionLP {
		try {
	        context = new InitialContext();
			zutrittscontrollerFac = (ZutrittscontrollerFac) context.lookup("lpserver/ZutrittscontrollerFacBean/remote");
		} catch (Throwable t) {
			handleThrowable(t);
		}

	}

	public Integer createZutrittsklasse(ZutrittsklasseDto zutrittsklasseDto)
			throws ExceptionLP {
		try {
			return zutrittscontrollerFac
					.createZutrittsklasse(zutrittsklasseDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Integer createZutrittsmodell(ZutrittsmodellDto zutrittsmodellDto)
			throws ExceptionLP {
		try {
			return zutrittscontrollerFac
					.createZutrittsmodell(zutrittsmodellDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Integer createZutrittsmodelltag(
			ZutrittsmodelltagDto zutrittsmodelltagDto) throws ExceptionLP {
		try {
			return zutrittscontrollerFac
					.createZutrittsmodelltag(zutrittsmodelltagDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Integer createZutrittsmodelltagdetail(
			ZutrittsmodelltagdetailDto zutrittsmodelltagdetailDto)
			throws ExceptionLP {
		try {
			return zutrittscontrollerFac
					.createZutrittsmodelltagdetail(zutrittsmodelltagdetailDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Integer createZutrittsobjekt(ZutrittsobjektDto zutrittsobjektDto)
			throws ExceptionLP {
		try {
			return zutrittscontrollerFac
					.createZutrittsobjekt(zutrittsobjektDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Integer createZutrittsobjektverwendung(
			ZutrittsobjektverwendungDto zutrittsobjektverwendungDto)
			throws ExceptionLP {
		try {
			return zutrittscontrollerFac
					.createZutrittsobjektverwendung(zutrittsobjektverwendungDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Map<?, ?> getAllZutrittsleser() throws ExceptionLP {
		try {
			return zutrittscontrollerFac.getAllZutrittsleser();
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Map<?, ?> getAllZutrittsoeffnungsarten() throws ExceptionLP {
		try {
			return zutrittscontrollerFac.getAllZutrittsoeffnungsarten();
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Integer createPersonalzutrittsklasse(
			PersonalzutrittsklasseDto personalzutrittsklasseDto)
			throws ExceptionLP {
		try {
			return zutrittscontrollerFac.createPersonalzutrittsklasse(
					personalzutrittsklasseDto, LPMain
							.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Integer createPersonalfinger(PersonalfingerDto personalfingerDto)
			throws ExceptionLP {
		try {
			return zutrittscontrollerFac.createPersonalfinger(
					personalfingerDto, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Integer createZutrittdaueroffen(
			ZutrittdaueroffenDto zutrittdaueroffenDto) throws ExceptionLP {
		try {
			return zutrittscontrollerFac
					.createZutrittdaueroffen(zutrittdaueroffenDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Integer createZutrittsklasseobjekt(
			ZutrittsklasseobjektDto zutrittsklasseobjektDto) throws ExceptionLP {
		try {
			return zutrittscontrollerFac.createZutrittsklasseobjekt(
					zutrittsklasseobjektDto, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Integer createZutrittscontroller(
			ZutrittscontrollerDto zutrittscontrollerDto) throws ExceptionLP {
		try {
			return zutrittscontrollerFac
					.createZutrittscontroller(zutrittscontrollerDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Integer createZutrittonlinecheck(
			ZutrittonlinecheckDto zutrittonlinecheckDto) throws ExceptionLP {
		try {
			return zutrittscontrollerFac.createZutrittonlinecheck(
					zutrittonlinecheckDto, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public void removePersonalzutrittsklasse(
			PersonalzutrittsklasseDto personalzutrittsklasseDto)
			throws ExceptionLP {
		try {
			zutrittscontrollerFac
					.removePersonalzutrittsklasse(personalzutrittsklasseDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void removePersonalfinger(PersonalfingerDto personalfingerDto)
			throws ExceptionLP {
		try {
			zutrittscontrollerFac.removePersonalfinger(personalfingerDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void removeZutrittdaueroffen(
			ZutrittdaueroffenDto zutrittdaueroffenDto) throws ExceptionLP {
		try {
			zutrittscontrollerFac.removeZutrittdaueroffen(zutrittdaueroffenDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void removeZutrittonlinecheck(
			ZutrittonlinecheckDto zutrittonlinecheckDto) throws ExceptionLP {
		try {
			zutrittscontrollerFac
					.removeZutrittonlinecheck(zutrittonlinecheckDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void removeZutrittscontroller(
			ZutrittscontrollerDto zutrittscontrollerDto) throws ExceptionLP {
		try {
			zutrittscontrollerFac
					.removeZutrittscontroller(zutrittscontrollerDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void removeZutrittsklasse(ZutrittsklasseDto zutrittsklasseDto)
			throws ExceptionLP {
		try {
			zutrittscontrollerFac.removeZutrittsklasse(zutrittsklasseDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void removeZutrittsklasseobjekt(
			ZutrittsklasseobjektDto zutrittsklasseobjektDto) throws ExceptionLP {
		try {
			zutrittscontrollerFac
					.removeZutrittsklasseobjekt(zutrittsklasseobjektDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void kopiereRestlicheZutrittsmodelltage(Integer zutrittsmodellIId)
			throws ExceptionLP {
		try {
			zutrittscontrollerFac
					.kopiereRestlicheZutrittsmodelltage(zutrittsmodellIId);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void removeZutrittsmodell(ZutrittsmodellDto zutrittsmodellDto)
			throws ExceptionLP {
		try {
			zutrittscontrollerFac.removeZutrittsmodell(zutrittsmodellDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void removeZutrittsmodelltag(
			ZutrittsmodelltagDto zutrittsmodelltagDto) throws ExceptionLP {
		try {
			zutrittscontrollerFac.removeZutrittsmodelltag(zutrittsmodelltagDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void removeZutrittsmodelltagdetail(
			ZutrittsmodelltagdetailDto zutrittsmodelltagdetailDto)
			throws ExceptionLP {
		try {
			zutrittscontrollerFac
					.removeZutrittsmodelltagdetail(zutrittsmodelltagdetailDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void removeZutrittsobjekt(ZutrittsobjektDto zutrittsobjektDto)
			throws ExceptionLP {
		try {
			zutrittscontrollerFac.removeZutrittsobjekt(zutrittsobjektDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void removeZutrittsobjektverwendung(
			ZutrittsobjektverwendungDto zutrittsobjektverwendungDto)
			throws ExceptionLP {
		try {
			zutrittscontrollerFac
					.removeZutrittsobjektverwendung(zutrittsobjektverwendungDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updatePersonalzutrittsklasse(
			PersonalzutrittsklasseDto personalzutrittsklasseDto)
			throws ExceptionLP {
		try {
			zutrittscontrollerFac.updatePersonalzutrittsklasse(
					personalzutrittsklasseDto, LPMain
							.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updatePersonalfinger(PersonalfingerDto personalfingerDto)
			throws ExceptionLP {
		try {
			zutrittscontrollerFac.updatePersonalfinger(personalfingerDto,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updateZutrittscontroller(
			ZutrittscontrollerDto zutrittscontrollerDto) throws ExceptionLP {
		try {
			zutrittscontrollerFac
					.updateZutrittscontroller(zutrittscontrollerDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updateZutrittonlinecheck(
			ZutrittonlinecheckDto zutrittonlinecheckDto) throws ExceptionLP {
		try {
			zutrittscontrollerFac
					.updateZutrittonlinecheck(zutrittonlinecheckDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updateZutrittsklasse(ZutrittsklasseDto zutrittsklasseDto)
			throws ExceptionLP {
		try {
			zutrittscontrollerFac.updateZutrittsklasse(zutrittsklasseDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updateZutrittdaueroffen(
			ZutrittdaueroffenDto zutrittdaueroffenDto) throws ExceptionLP {
		try {
			zutrittscontrollerFac.updateZutrittdaueroffen(zutrittdaueroffenDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updateZutrittsklasseobjekt(
			ZutrittsklasseobjektDto zutrittsklasseobjektDto) throws ExceptionLP {
		try {
			zutrittscontrollerFac.updateZutrittsklasseobjekt(
					zutrittsklasseobjektDto, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updateZutrittsmodell(ZutrittsmodellDto zutrittsmodellDto)
			throws ExceptionLP {
		try {
			zutrittscontrollerFac.updateZutrittsmodell(zutrittsmodellDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updateZutrittsmodelltag(
			ZutrittsmodelltagDto zutrittsmodelltagDto) throws ExceptionLP {
		try {
			zutrittscontrollerFac.updateZutrittsmodelltag(zutrittsmodelltagDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updateZutrittsmodelltagdetail(
			ZutrittsmodelltagdetailDto zutrittsmodelltagdetailDto)
			throws ExceptionLP {
		try {
			zutrittscontrollerFac
					.updateZutrittsmodelltagdetail(zutrittsmodelltagdetailDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updateZutrittsobjekt(ZutrittsobjektDto zutrittsobjektDto)
			throws ExceptionLP {
		try {
			zutrittscontrollerFac.updateZutrittsobjekt(zutrittsobjektDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updateZutrittsobjektverwendung(
			ZutrittsobjektverwendungDto zutrittsobjektverwendungDto)
			throws ExceptionLP {
		try {
			zutrittscontrollerFac
					.updateZutrittsobjektverwendung(zutrittsobjektverwendungDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public ZutrittscontrollerDto zutrittscontrollerFindByPrimaryKey(Integer iId)
			throws ExceptionLP {
		try {
			return zutrittscontrollerFac
					.zutrittscontrollerFindByPrimaryKey(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public ZutrittsklasseDto zutrittsklasseFindByPrimaryKey(Integer iId)
			throws ExceptionLP {
		try {
			return zutrittscontrollerFac.zutrittsklasseFindByPrimaryKey(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public ZutrittsklasseDto zutrittsklasseFindByCNr(String cNr)
			throws ExceptionLP {
		try {
			return zutrittscontrollerFac.zutrittsklasseFindByCNr(cNr);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public ZutrittsklasseobjektDto zutrittsklasseobjektFindByPrimaryKey(
			Integer iId) throws ExceptionLP {
		try {
			return zutrittscontrollerFac
					.zutrittsklasseobjektFindByPrimaryKey(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public ZutrittonlinecheckDto zutrittonlinecheckFindByPrimaryKey(Integer iId)
			throws ExceptionLP {
		try {
			return zutrittscontrollerFac
					.zutrittonlinecheckFindByPrimaryKey(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public ZutrittsmodellDto zutrittsmodellFindByPrimaryKey(Integer iId)
			throws ExceptionLP {
		try {
			return zutrittscontrollerFac.zutrittsmodellFindByPrimaryKey(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public ZutrittsmodelltagDto zutrittsmodelltagFindByPrimaryKey(Integer iId)
			throws ExceptionLP {
		try {
			return zutrittscontrollerFac.zutrittsmodelltagFindByPrimaryKey(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public ZutrittsmodelltagdetailDto zutrittsmodelltagdetailFindByPrimaryKey(
			Integer iId) throws ExceptionLP {
		try {
			return zutrittscontrollerFac
					.zutrittsmodelltagdetailFindByPrimaryKey(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public ZutrittsobjektDto zutrittsobjektFindByPrimaryKey(Integer iId)
			throws ExceptionLP {
		try {
			return zutrittscontrollerFac.zutrittsobjektFindByPrimaryKey(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public ZutrittdaueroffenDto zutrittdaueroffenFindByPrimaryKey(Integer iId)
			throws ExceptionLP {
		try {
			return zutrittscontrollerFac.zutrittdaueroffenFindByPrimaryKey(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public ZutrittsobjektverwendungDto[] zutrittsobjektverwendungFindByZutrittsobjektIId(
			Integer zutrittscontrollerIId) throws ExceptionLP {
		try {
			return zutrittscontrollerFac
					.zutrittsobjektverwendungFindByZutrittsobjektIId(zutrittscontrollerIId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public ZutrittsobjektverwendungDto zutrittsobjektverwendungFindByPrimaryKey(
			Integer iId) throws ExceptionLP {
		try {
			return zutrittscontrollerFac
					.zutrittsobjektverwendungFindByPrimaryKey(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public PersonalzutrittsklasseDto personalzutrittsklasseFindByPrimaryKey(
			Integer iId) throws ExceptionLP {
		try {
			return zutrittscontrollerFac
					.personalzutrittsklasseFindByPrimaryKey(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public FingerartDto fingerartFindByPrimaryKey(Integer iId)
			throws ExceptionLP {
		try {
			return zutrittscontrollerFac.fingerartFindByPrimaryKey(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public PersonalfingerDto personalfingerFindByPrimaryKey(Integer iId)
			throws ExceptionLP {
		try {
			return zutrittscontrollerFac.personalfingerFindByPrimaryKey(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}
}
