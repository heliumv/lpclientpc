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

import java.math.BigDecimal;
import java.sql.Date;

import javax.naming.Context;
import javax.naming.InitialContext;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.pc.LPMain;
import com.lp.server.artikel.service.MaterialDto;
import com.lp.server.artikel.service.MaterialFac;
import com.lp.server.artikel.service.MaterialzuschlagDto;
import com.lp.server.system.service.TheClientDto;

public class MaterialDelegate extends Delegate {
	private Context context;
	public MaterialFac materialFac;

	public MaterialDelegate() throws Exception {
		context = new InitialContext();
		materialFac = (MaterialFac) context
				.lookup("lpserver/MaterialFacBean/remote");
	}

	public Integer createMaterial(MaterialDto materialDto) throws ExceptionLP {
		try {
			return materialFac.createMaterial(materialDto,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public BigDecimal getKupferzuschlagInLieferantenwaehrung(
			Integer artikelIId, Integer lieferantIId) throws ExceptionLP {
		try {
			return materialFac.getKupferzuschlagInLieferantenwaehrung(
					artikelIId, lieferantIId, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public BigDecimal getMaterialzuschlagVKInZielwaehrung(Integer artikelIId,
			Date datGueltigkeitsdatumI, String waehrungCNrZielwaehrung)
			throws ExceptionLP {
		try {
			return materialFac.getMaterialzuschlagVKInZielwaehrung(artikelIId,
					datGueltigkeitsdatumI, waehrungCNrZielwaehrung,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public BigDecimal getMaterialzuschlagEKInZielwaehrung(Integer artikelIId,
			Integer lieferantIId, Date datGueltigkeitsdatumI,
			String waehrungCNrZielwaehrung) throws ExceptionLP {
		try {
			return materialFac.getMaterialzuschlagEKInZielwaehrung(artikelIId,
					lieferantIId, datGueltigkeitsdatumI,
					waehrungCNrZielwaehrung, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Integer materialFindByCNrOhneExc(String materialCNr)
			throws ExceptionLP {
		try {
			return materialFac.materialFindByCNrOhneExc(materialCNr);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public void removeMaterial(Integer iId) throws ExceptionLP {
		try {
			materialFac.removeMaterial(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updateMaterial(MaterialDto materialDto) throws ExceptionLP {
		try {
			materialFac.updateMaterial(materialDto, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public MaterialDto materialFindByPrimaryKey(Integer iId) throws ExceptionLP {
		try {
			return materialFac.materialFindByPrimaryKey(iId,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Integer createMaterialzuschlag(
			MaterialzuschlagDto materialzuschlagDto) throws ExceptionLP {
		try {
			return materialFac.createMaterialzuschlag(materialzuschlagDto,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public void removeMaterialzuschlag(MaterialzuschlagDto materialzuschlagDto)
			throws ExceptionLP {
		try {
			materialFac.removeMaterialzuschlag(materialzuschlagDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void pflegeMaterialzuschlagsKursUndDatumNachtragen()
			throws ExceptionLP {
		try {
			materialFac.pflegeMaterialzuschlagsKursUndDatumNachtragen(LPMain
					.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updateMaterialzuschlag(MaterialzuschlagDto materialzuschlagDto)
			throws ExceptionLP {
		try {
			materialFac.updateMaterialzuschlag(materialzuschlagDto,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public MaterialzuschlagDto materialzuschlagFindByPrimaryKey(Integer id)
			throws ExceptionLP {
		try {
			return materialFac.materialzuschlagFindByPrimaryKey(id);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public BigDecimal materialzuschlagFindAktuellenzuschlag(Integer id)
			throws ExceptionLP {
		try {
			return materialFac.materialzuschlagFindAktuellenzuschlag(id,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

}
