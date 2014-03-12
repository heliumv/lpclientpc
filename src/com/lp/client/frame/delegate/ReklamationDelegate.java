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

import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.pc.LPMain;
import com.lp.server.reklamation.service.AufnahmeartDto;
import com.lp.server.reklamation.service.BehandlungDto;
import com.lp.server.reklamation.service.FehlerDto;
import com.lp.server.reklamation.service.FehlerangabeDto;
import com.lp.server.reklamation.service.MassnahmeDto;
import com.lp.server.reklamation.service.ReklamationDto;
import com.lp.server.reklamation.service.ReklamationFac;
import com.lp.server.reklamation.service.ReklamationbildDto;
import com.lp.server.reklamation.service.SchwereDto;
import com.lp.server.reklamation.service.TermintreueDto;
import com.lp.server.reklamation.service.WirksamkeitDto;

public class ReklamationDelegate extends Delegate {
	private Context context;
	private ReklamationFac reklamationFac;

	public ReklamationDelegate() throws Exception {
		context = new InitialContext();
		reklamationFac = (ReklamationFac) context
				.lookup("lpserver/ReklamationFacBean/remote");
	}

	public Integer createFehlerangabe(FehlerangabeDto fehlerangabeDto)
			throws ExceptionLP {
		try {
			return reklamationFac.createFehlerangabe(fehlerangabeDto, LPMain
					.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Integer getNextReklamationbild(Integer reklamationIId)
			throws ExceptionLP {
		try {
			return reklamationFac.getNextReklamationbild(reklamationIId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Integer createFehler(FehlerDto fehlerDto) throws ExceptionLP {
		try {
			return reklamationFac.createFehler(fehlerDto, LPMain
					.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Map<?, ?> getAllReklamationart() throws ExceptionLP {
		try {
			return reklamationFac.getAllReklamationart();
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Integer createAufnahmeart(AufnahmeartDto aufnahmeartDto)
			throws ExceptionLP {
		try {
			return reklamationFac.createAufnahmeart(aufnahmeartDto, LPMain
					.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Integer createMassnahme(MassnahmeDto massnahmeDto)
			throws ExceptionLP {
		try {
			return reklamationFac.createMassnahme(massnahmeDto, LPMain
					.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Integer createSchwere(SchwereDto schwereDto) throws ExceptionLP {
		try {
			return reklamationFac.createSchwere(schwereDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Integer createTermintreue(TermintreueDto termintreueDto)
			throws ExceptionLP {
		try {
			return reklamationFac.createTermintreue(termintreueDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Integer createBehandlung(BehandlungDto beurteilungDto)
			throws ExceptionLP {
		try {
			return reklamationFac.createBehandlung(beurteilungDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Integer createReklamation(ReklamationDto reklamationDto)
			throws ExceptionLP {
		try {
			return reklamationFac.createReklamation(reklamationDto, LPMain
					.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}

	public Integer createReklamationbild(ReklamationbildDto reklamationbildDto)
			throws ExceptionLP {
		try {
			return reklamationFac.createReklamationbild(reklamationbildDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}

	public Integer createWirksamkeit(WirksamkeitDto wirksamkeitDto)
			throws ExceptionLP {
		try {
			return reklamationFac.createWirksamkeit(wirksamkeitDto, LPMain
					.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}

	public void removeFehlerangabe(FehlerangabeDto fehlerangabeDto)
			throws ExceptionLP {
		try {
			reklamationFac.removeFehlerangabe(fehlerangabeDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void removeTermintreue(TermintreueDto termintreueDto)
			throws ExceptionLP {
		try {
			reklamationFac.removeTermintreue(termintreueDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void removeFehler(FehlerDto fehlerDto) throws ExceptionLP {
		try {
			reklamationFac.removeFehler(fehlerDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void removeBehandlung(BehandlungDto beurteilungDto)
			throws ExceptionLP {
		try {
			reklamationFac.removeBehandlung(beurteilungDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void removeSchwere(SchwereDto schwereDto) throws ExceptionLP {
		try {
			reklamationFac.removeSchwere(schwereDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void removeReklamationbild(ReklamationbildDto reklamationbildDto)
			throws ExceptionLP {
		try {
			reklamationFac.removeReklamationbild(reklamationbildDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updateReklamationKommentar(ReklamationDto reklamationDto)
			throws ExceptionLP {
		try {
			reklamationFac.updateReklamationKommentar(reklamationDto, LPMain
					.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updateReklamationbild(ReklamationbildDto reklamationbildDto)
			throws ExceptionLP {
		try {
			reklamationFac.updateReklamationbild(reklamationbildDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updateTermintreue(TermintreueDto termintreueDto)
			throws ExceptionLP {
		try {
			reklamationFac.updateTermintreue(termintreueDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updateReklamation(ReklamationDto reklamationDto)
			throws ExceptionLP {
		try {
			reklamationFac.updateReklamation(reklamationDto, LPMain
					.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updateBehandlung(BehandlungDto beurteilungDto)
			throws ExceptionLP {
		try {
			reklamationFac.updateBehandlung(beurteilungDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void removeAufnahmeart(AufnahmeartDto aufnahmeartDto)
			throws ExceptionLP {
		try {
			reklamationFac.removeAufnahmeart(aufnahmeartDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void removeMassnahme(MassnahmeDto massnahmeDto) throws ExceptionLP {
		try {
			reklamationFac.removeMassnahme(massnahmeDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void removeWirksamkeit(WirksamkeitDto wirksamkeitDto)
			throws ExceptionLP {
		try {
			reklamationFac.removeWirksamkeit(wirksamkeitDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updateFehlerangabe(FehlerangabeDto fehlerangabeDto)
			throws ExceptionLP {
		try {
			reklamationFac.updateFehlerangabe(fehlerangabeDto, LPMain
					.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}

	}

	public void updateFehler(FehlerDto fehlerDto) throws ExceptionLP {
		try {
			reklamationFac.updateFehler(fehlerDto, LPMain
					.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}

	}

	public void updateSchwere(SchwereDto schwereDto) throws ExceptionLP {
		try {
			reklamationFac.updateSchwere(schwereDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}

	}

	public void reklamationErledigenOderAufheben(Integer reklamationIId,
			Integer beurteilungIId) throws ExceptionLP {
		try {
			reklamationFac.reklamationErledigenOderAufheben(reklamationIId,
					beurteilungIId, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}

	}

	public void updateAufnahmeart(AufnahmeartDto aufnahmeartDto)
			throws ExceptionLP {
		try {
			reklamationFac.updateAufnahmeart(aufnahmeartDto, LPMain
					.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}

	}

	public void updateMassnahme(MassnahmeDto massnahmeDto) throws ExceptionLP {
		try {
			reklamationFac.updateMassnahme(massnahmeDto, LPMain
					.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}

	}

	public void updateWirksamkeit(WirksamkeitDto wirksamkeitDto)
			throws ExceptionLP {
		try {
			reklamationFac.updateWirksamkeit(wirksamkeitDto, LPMain
					.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}

	}

	public FehlerangabeDto fehlerangabeFindByPrimaryKey(Integer iId)
			throws ExceptionLP {
		try {
			return reklamationFac.fehlerangabeFindByPrimaryKey(iId, LPMain
					.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}

	public TermintreueDto termintreueFindByPrimaryKey(Integer iId)
			throws ExceptionLP {
		try {
			return reklamationFac.termintreueFindByPrimaryKey(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}

	public BehandlungDto behandlungFindByPrimaryKey(Integer iId)
			throws ExceptionLP {
		try {
			return reklamationFac.behandlungFindByPrimaryKey(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}

	public FehlerDto fehlerFindByPrimaryKey(Integer iId) throws ExceptionLP {
		try {
			return reklamationFac.fehlerFindByPrimaryKey(iId, LPMain
					.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}

	public SchwereDto schwereFindByPrimaryKey(Integer iId) throws ExceptionLP {
		try {
			return reklamationFac.schwereFindByPrimaryKey(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}

	public MassnahmeDto massnahmeFindByPrimaryKey(Integer iId)
			throws ExceptionLP {
		try {
			return reklamationFac.massnahmeFindByPrimaryKey(iId, LPMain
					.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}

	public ReklamationDto reklamationFindByPrimaryKey(Integer iId)
			throws ExceptionLP {
		try {
			return reklamationFac.reklamationFindByPrimaryKey(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}

	public ReklamationDto[] reklamationfindOffeneReklamationenEinesArtikels(Integer artikelIId)
			throws ExceptionLP {
		try {
			return reklamationFac.reklamationfindOffeneReklamationenEinesArtikels(artikelIId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}

	public ReklamationbildDto reklamationbildFindByPrimaryKey(Integer iId)
			throws ExceptionLP {
		try {
			return reklamationFac.reklamationbildFindByPrimaryKey(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}

	public AufnahmeartDto aufnahmeartFindByPrimaryKey(Integer iId)
			throws ExceptionLP {
		try {
			return reklamationFac.aufnahmeartFindByPrimaryKey(iId, LPMain
					.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}

	public WirksamkeitDto wirksamkeitFindByPrimaryKey(Integer iId)
			throws ExceptionLP {
		try {
			return reklamationFac.wirksamkeitFindByPrimaryKey(iId, LPMain
					.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}

}
