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

import java.util.ArrayList;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.pc.LPMain;
import com.lp.server.kueche.service.BedienerlagerDto;
import com.lp.server.kueche.service.KassaimportDto;
import com.lp.server.kueche.service.Kdc100logDto;
import com.lp.server.kueche.service.KuecheFac;
import com.lp.server.kueche.service.KassaartikelDto;
import com.lp.server.kueche.service.KuecheumrechnungDto;
import com.lp.server.kueche.service.SpeiseplanDto;
import com.lp.server.kueche.service.TageslosDto;
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

public class KuecheDelegate extends Delegate {
	private Context context;
	private KuecheFac kuecheFac;

	public KuecheDelegate() throws Exception {
		context = new InitialContext();
		kuecheFac = (KuecheFac) context.lookup("lpserver/KuecheFacBean/remote");
	}

	public Integer createKassaartikel(KassaartikelDto speisekassaDto)
			throws ExceptionLP {
		try {
			return kuecheFac.createKassaartikel(speisekassaDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Integer createKuecheumrechnung(
			KuecheumrechnungDto kuecheumrechnungDto) throws ExceptionLP {
		try {
			return kuecheFac.createKuecheumrechnung(kuecheumrechnungDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Integer createSpeiseplan(SpeiseplanDto speiseplanDto)
			throws ExceptionLP {
		try {
			return kuecheFac.createSpeiseplan(speiseplanDto, LPMain
					.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}public Integer createBedienerlager(BedienerlagerDto bedienerlagerDto)
			throws ExceptionLP {
		try {
			return kuecheFac.createBedienerlager(bedienerlagerDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Integer createTageslos(TageslosDto tageslosDto) throws ExceptionLP {
		try {
			return kuecheFac.createTageslos(tageslosDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public void removeKassaartikel(KassaartikelDto speisekassaDto)
			throws ExceptionLP {
		try {
			kuecheFac.removeKassaartikel(speisekassaDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void removeKuecheumrechnung(KuecheumrechnungDto kuecheumrechnungDto)
			throws ExceptionLP {
		try {
			kuecheFac.removeKuecheumrechnung(kuecheumrechnungDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void removeSpeiseplan(SpeiseplanDto speiseplanDto)
			throws ExceptionLP {
		try {
			kuecheFac.removeSpeiseplan(speiseplanDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void importiereKassenfileOscar(ArrayList zeilen) throws ExceptionLP {
		try {
			kuecheFac.importiereKassenfileOscar(zeilen, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void importiereKassenfileGaestehaus(ArrayList zeilen)
			throws ExceptionLP {
		try {
			kuecheFac.importiereKassenfileGaestehaus(zeilen, LPMain
					.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void importiereAlleKassenfiles() throws ExceptionLP {
		try {
			kuecheFac.importiereAlleKassenfiles(LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public int stiftdatenImportieren(Kdc100logDto[] kdc100logDtos,
			Integer lagerIId_Ziel) throws ExceptionLP {
		try {
			return kuecheFac.stiftdatenImportieren(kdc100logDtos,
					lagerIId_Ziel, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return 0;
		}
	}

	public int getAnzahlKassaimportZuSpeiseplan(Integer speiseplanIId)
			throws ExceptionLP {
		try {
			return kuecheFac.getAnzahlKassaimportZuSpeiseplan(speiseplanIId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return 0;
		}
	}

	public void removeTageslos(TageslosDto tageslosDto) throws ExceptionLP {
		try {
			kuecheFac.removeTageslos(tageslosDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void removeBedienerlager(BedienerlagerDto bedienerlagerDto)
			throws ExceptionLP {
		try {
			kuecheFac.removeBedienerlager(bedienerlagerDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updateSpeisekassa(KassaartikelDto speisekassaDto)
			throws ExceptionLP {
		try {
			kuecheFac.updateKassaartikel(speisekassaDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updateBedienerlager(BedienerlagerDto bedienerlagerDto)
			throws ExceptionLP {
		try {
			kuecheFac.updateBedienerlager(bedienerlagerDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updateSpeiseplan(SpeiseplanDto speiseplanDto)
			throws ExceptionLP {
		try {
			kuecheFac.updateSpeiseplan(speiseplanDto, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updateKuecheumrechnung(KuecheumrechnungDto kuecheumrechnungDto)
			throws ExceptionLP {
		try {
			kuecheFac.updateKuecheumrechnung(kuecheumrechnungDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updateTageslos(TageslosDto tageslosDto) throws ExceptionLP {
		try {
			kuecheFac.updateTageslos(tageslosDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void vertauscheKassaartikel(Integer iId1I, Integer iId2I)
			throws ExceptionLP {
		try {
			kuecheFac.vertauscheKassaartikel(iId1I, iId2I);
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public TageslosDto tageslosFindByPrimaryKey(Integer iId) throws ExceptionLP {
		try {
			return kuecheFac.tageslosFindByPrimaryKey(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}
	public BedienerlagerDto bedienerlagerFindByPrimaryKey(Integer iId) throws ExceptionLP {
		try {
			return kuecheFac.bedienerlagerFindByPrimaryKey(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}
	public KuecheumrechnungDto kuecheumrechnungFindByPrimaryKey(Integer iId)
			throws ExceptionLP {
		try {
			return kuecheFac.kuecheumrechnungFindByPrimaryKey(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}

	public SpeiseplanDto speiseplanFindByPrimaryKey(Integer iId)
			throws ExceptionLP {
		try {
			return kuecheFac.speiseplanFindByPrimaryKey(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}

	public KassaartikelDto kassaartikelFindByPrimaryKey(Integer iId)
			throws ExceptionLP {
		try {
			return kuecheFac.kassaartikelFindByPrimaryKey(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}

}
