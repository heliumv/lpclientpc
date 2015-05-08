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
import com.lp.server.instandhaltung.service.AnlageDto;
import com.lp.server.instandhaltung.service.GeraetDto;
import com.lp.server.instandhaltung.service.GeraetehistorieDto;
import com.lp.server.instandhaltung.service.GeraetetypDto;
import com.lp.server.instandhaltung.service.GewerkDto;
import com.lp.server.instandhaltung.service.HalleDto;
import com.lp.server.instandhaltung.service.InstandhaltungDto;
import com.lp.server.instandhaltung.service.InstandhaltungFac;
import com.lp.server.instandhaltung.service.IskategorieDto;
import com.lp.server.instandhaltung.service.IsmaschineDto;
import com.lp.server.instandhaltung.service.StandortDto;
import com.lp.server.instandhaltung.service.StandorttechnikerDto;
import com.lp.server.instandhaltung.service.WartungslisteDto;
import com.lp.server.instandhaltung.service.WartungsschritteDto;

public class InstandhaltungDelegate extends Delegate {
	private Context context = null;
	private InstandhaltungFac instandhaltungFac = null;

	public InstandhaltungDelegate() throws Exception {
		context = new InitialContext();
		instandhaltungFac = (InstandhaltungFac) context
				.lookup("lpserver/InstandhaltungFacBean/remote");

	}

	public Integer createInstandhaltung(InstandhaltungDto instandhaltungDto)
			throws ExceptionLP {
		Integer iId = null;
		try {
			iId = instandhaltungFac.createInstandhaltung(instandhaltungDto);
		} catch (Throwable t) {
			handleThrowable(t);
		}
		return iId;
	}

	public Integer createHalle(HalleDto halleDto) throws ExceptionLP {
		Integer iId = null;
		try {
			iId = instandhaltungFac.createHalle(halleDto);
		} catch (Throwable t) {
			handleThrowable(t);
		}
		return iId;
	}

	public Map getAllKategorieren() throws ExceptionLP {
		Map iId = null;
		try {
			iId = instandhaltungFac.getAllKategorieren(LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
		return iId;
	}

	public Integer createIsmaschine(IsmaschineDto ismaschineDto)
			throws ExceptionLP {
		Integer iId = null;
		try {
			iId = instandhaltungFac.createIsmaschine(ismaschineDto);
		} catch (Throwable t) {
			handleThrowable(t);
		}
		return iId;
	}

	public Integer createGeraetetyp(GeraetetypDto geraetetypDto)
			throws ExceptionLP {
		Integer iId = null;
		try {
			iId = instandhaltungFac.createGeraetetyp(geraetetypDto);
		} catch (Throwable t) {
			handleThrowable(t);
		}
		return iId;
	}

	public Integer createGeraet(GeraetDto geraetDto) throws ExceptionLP {
		Integer iId = null;
		try {
			iId = instandhaltungFac.createGeraet(geraetDto);
		} catch (Throwable t) {
			handleThrowable(t);
		}
		return iId;
	}

	public Integer createGeraetehistorie(GeraetehistorieDto geraetehistorieDto)
			throws ExceptionLP {
		Integer iId = null;
		try {
			iId = instandhaltungFac.createGeraetehistorie(geraetehistorieDto,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
		return iId;
	}

	public Integer createWartungsliste(WartungslisteDto wartungslisteDto)
			throws Throwable {
		return instandhaltungFac.createWartungsliste(wartungslisteDto,
				LPMain.getTheClient());
	}

	public Integer createWartungsschritte(
			WartungsschritteDto wartungsschritteDto) throws Throwable {
		return instandhaltungFac.createWartungsschritte(wartungsschritteDto,
				LPMain.getTheClient());
	}

	public void vertauscheWartungsliste(Integer iIdPosition1I,
			Integer iIdPosition2I) {

		instandhaltungFac.vertauscheWartungsliste(iIdPosition1I, iIdPosition2I);

	}

	public void vertauscheWartungsschritte(Integer iIdPosition1I,
			Integer iIdPosition2I) {

		instandhaltungFac.vertauscheWartungsschritte(iIdPosition1I,
				iIdPosition2I);

	}

	public void sortierungWartungslisteAnpassenBeiEinfuegenEinerPositionVorPosition(
			Integer stueckliste, int iSortierungNeuePositionI) {

		instandhaltungFac
				.sortierungWartungslisteAnpassenBeiEinfuegenEinerPositionVorPosition(
						stueckliste, iSortierungNeuePositionI);

	}

	public void sortierungWartungsschritteAnpassenBeiEinfuegenEinerPositionVorPosition(
			Integer stueckliste, int iSortierungNeuePositionI) {

		instandhaltungFac
				.sortierungWartungsschritteAnpassenBeiEinfuegenEinerPositionVorPosition(
						stueckliste, iSortierungNeuePositionI);

	}

	public Integer createAnlage(AnlageDto anlageDto) throws ExceptionLP {
		Integer iId = null;
		try {
			iId = instandhaltungFac.createAnlage(anlageDto);
		} catch (Throwable t) {
			handleThrowable(t);
		}
		return iId;
	}

	public Integer createStandort(StandortDto standortDto) throws ExceptionLP {
		Integer iId = null;
		try {
			iId = instandhaltungFac.createStandort(standortDto);
		} catch (Throwable t) {
			handleThrowable(t);
		}
		return iId;
	}

	public Integer createStandorttechniker(StandorttechnikerDto standorttechnikerDto) throws ExceptionLP {
		Integer iId = null;
		try {
			iId = instandhaltungFac.createStandorttechniker(standorttechnikerDto,LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
		return iId;
	}

	public void updateStandorttechniker(StandorttechnikerDto standorttechnikerDto) throws ExceptionLP {
		try {
			instandhaltungFac.updateStandorttechniker(standorttechnikerDto);
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public Integer createIskategorie(IskategorieDto iskategorieDto)
			throws ExceptionLP {
		Integer iId = null;
		try {
			iId = instandhaltungFac.createIskategorie(iskategorieDto);
		} catch (Throwable t) {
			handleThrowable(t);
		}
		return iId;
	}

	public Integer createGewerk(GewerkDto dto) throws ExceptionLP {
		Integer iId = null;
		try {
			iId = instandhaltungFac.createGewerk(dto);
		} catch (Throwable t) {
			handleThrowable(t);
		}
		return iId;
	}

	public InstandhaltungDto instandhaltungFindByPrimaryKey(Integer iId)
			throws ExceptionLP {

		InstandhaltungDto k = null;
		try {
			k = instandhaltungFac.instandhaltungFindByPrimaryKey(iId);
		} catch (Throwable t) {
			handleThrowable(t);
		}
		return k;
	}

	public IskategorieDto iskategorieFindByPrimaryKey(Integer iId)
			throws ExceptionLP {

		IskategorieDto k = null;
		try {
			k = instandhaltungFac.iskategorieFindByPrimaryKey(iId);
		} catch (Throwable t) {
			handleThrowable(t);
		}
		return k;
	}

	public GewerkDto gewerkFindByPrimaryKey(Integer iId)
			throws ExceptionLP {

		GewerkDto k = null;
		try {
			k = instandhaltungFac.gewerkFindByPrimaryKey(iId);
		} catch (Throwable t) {
			handleThrowable(t);
		}
		return k;
	}

	public HalleDto halleFindByPrimaryKey(Integer iId) throws ExceptionLP {

		HalleDto k = null;
		try {
			k = instandhaltungFac.halleFindByPrimaryKey(iId);
		} catch (Throwable t) {
			handleThrowable(t);
		}
		return k;
	}

	public IsmaschineDto ismaschineFindByPrimaryKey(Integer iId)
			throws ExceptionLP {

		IsmaschineDto k = null;
		try {
			k = instandhaltungFac.ismaschineFindByPrimaryKey(iId);
		} catch (Throwable t) {
			handleThrowable(t);
		}
		return k;
	}

	public GeraetetypDto geraetetypFindByPrimaryKey(Integer iId)
			throws ExceptionLP {

		GeraetetypDto k = null;
		try {
			k = instandhaltungFac.geraetetypFindByPrimaryKey(iId);
		} catch (Throwable t) {
			handleThrowable(t);
		}
		return k;
	}

	public GeraetehistorieDto[] geraetehistorieFindByGeraetIIdPersonalIIdTechnikerTWartung(
			Integer geraetIId, Integer personalIIdTechniker,
			java.sql.Timestamp tWartung) throws ExceptionLP {

		GeraetehistorieDto[] k = null;
		try {
			k = instandhaltungFac
					.geraetehistorieFindByGeraetIIdPersonalIIdTechnikerTWartung(
							geraetIId, personalIIdTechniker, tWartung);
		} catch (Throwable t) {
			handleThrowable(t);
		}
		return k;
	}

	public GeraetDto geraetFindByPrimaryKey(Integer iId) throws ExceptionLP {

		GeraetDto k = null;
		try {
			k = instandhaltungFac.geraetFindByPrimaryKey(iId);
		} catch (Throwable t) {
			handleThrowable(t);
		}
		return k;
	}

	public GeraetehistorieDto geraetehistorieFindByPrimaryKey(Integer iId)
			throws ExceptionLP {
		GeraetehistorieDto k = null;
		try {
			k = instandhaltungFac.geraetehistorieFindByPrimaryKey(iId);
		} catch (Throwable t) {
			handleThrowable(t);
		}
		return k;
	}

	public WartungslisteDto wartungslisteFindByPrimaryKey(Integer iId)
			throws ExceptionLP {

		WartungslisteDto k = null;
		try {
			k = instandhaltungFac.wartungslisteFindByPrimaryKey(iId);
		} catch (Throwable t) {
			handleThrowable(t);
		}
		return k;
	}

	public WartungsschritteDto wartungsschritteFindByPrimaryKey(Integer iId)
			throws ExceptionLP {

		WartungsschritteDto k = null;
		try {
			k = instandhaltungFac.wartungsschritteFindByPrimaryKey(iId);
		} catch (Throwable t) {
			handleThrowable(t);
		}
		return k;
	}

	public AnlageDto anlageFindByPrimaryKey(Integer iId) throws ExceptionLP {

		AnlageDto k = null;
		try {
			k = instandhaltungFac.anlageFindByPrimaryKey(iId);
		} catch (Throwable t) {
			handleThrowable(t);
		}
		return k;
	}

	public StandortDto standortFindByPrimaryKey(Integer iId) throws ExceptionLP {

		StandortDto k = null;
		try {
			k = instandhaltungFac.standortFindByPrimaryKey(iId);
		} catch (Throwable t) {
			handleThrowable(t);
		}
		return k;
	}

	public StandorttechnikerDto standorttechnikerFindByPrimaryKey(Integer iId) throws ExceptionLP {

		StandorttechnikerDto k = null;
		try {
			k = instandhaltungFac.standorttechnikerFindByPrimaryKey(iId);
		} catch (Throwable t) {
			handleThrowable(t);
		}
		return k;
	}

	public void updateInstandhaltung(InstandhaltungDto instandhaltungDto)
			throws ExceptionLP {
		try {
			instandhaltungFac.updateInstandhaltung(instandhaltungDto);
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public void updateHalle(HalleDto halleDto) throws ExceptionLP {
		try {
			instandhaltungFac.updateHalle(halleDto);
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public void updateIsmaschine(IsmaschineDto ismaschineDto)
			throws ExceptionLP {
		try {
			instandhaltungFac.updateIsmaschine(ismaschineDto);
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public void updateGeraetetyp(GeraetetypDto geraetetypDto)
			throws ExceptionLP {
		try {
			instandhaltungFac.updateGeraetetyp(geraetetypDto);
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public void updateIskategorie(IskategorieDto iskategorieDto)
			throws ExceptionLP {
		try {
			instandhaltungFac.updateIskategorie(iskategorieDto);
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public void updateGewerk(GewerkDto gewerkDto) throws ExceptionLP {
		try {
			instandhaltungFac.updateGewerk(gewerkDto);
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public void updateGeraet(GeraetDto geraetDto) throws ExceptionLP {
		try {
			instandhaltungFac.updateGeraet(geraetDto);
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public void updateGeraetehistorie(GeraetehistorieDto geraetehistorieDto)
			throws ExceptionLP {
		try {
			instandhaltungFac.updateGeraetehistorie(geraetehistorieDto,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public void updateWartungsliste(WartungslisteDto wartungslisteDto)
			throws ExceptionLP {
		try {
			instandhaltungFac.updateWartungsliste(wartungslisteDto,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public void updateWartungsschritte(WartungsschritteDto wartungsschritteDto)
			throws ExceptionLP {
		try {
			instandhaltungFac.updateWartungsschritte(wartungsschritteDto,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public void updateAnlage(AnlageDto anlageDto) throws ExceptionLP {
		try {
			instandhaltungFac.updateAnlage(anlageDto);
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public void updateStandort(StandortDto standortDto) throws ExceptionLP {
		try {
			instandhaltungFac.updateStandort(standortDto);
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public void removeInstandhaltung(InstandhaltungDto instandhaltungDto)
			throws ExceptionLP {
		try {
			instandhaltungFac.removeInstandhaltung(instandhaltungDto);
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public void removeHalle(HalleDto halleDto) throws ExceptionLP {
		try {
			instandhaltungFac.removeHalle(halleDto);
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public void removeIsmaschine(IsmaschineDto ismaschineDto)
			throws ExceptionLP {
		try {
			instandhaltungFac.removeIsmaschine(ismaschineDto);
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public void removeAnlage(AnlageDto anlageDto) throws ExceptionLP {
		try {
			instandhaltungFac.removeAnlage(anlageDto);
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public void removeStandort(StandortDto standortDto) throws ExceptionLP {
		try {
			instandhaltungFac.removeStandort(standortDto);
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public void removeStandorttechniker(StandorttechnikerDto standorttechnikerDto) throws ExceptionLP {
		try {
			instandhaltungFac.removeStandorttechniker(standorttechnikerDto);
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public void removeGeraetetyp(GeraetetypDto geraetetypDto)
			throws ExceptionLP {
		try {
			instandhaltungFac.removeGeraetetyp(geraetetypDto);
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public void removeIskategorie(IskategorieDto iskategorieDto)
			throws ExceptionLP {
		try {
			instandhaltungFac.removeIskategorie(iskategorieDto);
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public void removeGewerk(GewerkDto dto) throws ExceptionLP {
		try {
			instandhaltungFac.removeGewerk(dto);
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public void removeGeraet(GeraetDto geraetDto) throws ExceptionLP {
		try {
			instandhaltungFac.removeGeraet(geraetDto);
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public void removeGeraetehistorie(GeraetehistorieDto geraetehistorieDto)
			throws ExceptionLP {
		try {
			instandhaltungFac.removeGeraetehistorie(geraetehistorieDto);
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public void removeWartungsliste(WartungslisteDto wartungslisteDto)
			throws ExceptionLP {
		try {
			instandhaltungFac.removeWartungsliste(wartungslisteDto);
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public void removeWartungsschritte(WartungsschritteDto wartungsschritteDto)
			throws ExceptionLP {
		try {
			instandhaltungFac.removeWartungsschritte(wartungsschritteDto);
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}
}
