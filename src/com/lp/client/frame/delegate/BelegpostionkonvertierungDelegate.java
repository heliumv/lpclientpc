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
import com.lp.server.anfrage.service.AnfrageDto;
import com.lp.server.anfrage.service.AnfragepositionDto;
import com.lp.server.angebot.service.AngebotpositionDto;
import com.lp.server.angebotstkl.service.AgstklarbeitsplanDto;
import com.lp.server.angebotstkl.service.AgstklpositionDto;
import com.lp.server.angebotstkl.service.EinkaufsangebotpositionDto;
import com.lp.server.auftrag.service.AuftragpositionDto;
import com.lp.server.bestellung.service.BestellpositionDto;
import com.lp.server.bestellung.service.BestellungDto;
import com.lp.server.bestellung.service.BestellvorschlagDto;
import com.lp.server.fertigung.service.LossollarbeitsplanDto;
import com.lp.server.instandhaltung.service.WartungslisteDto;
import com.lp.server.instandhaltung.service.WartungsschritteDto;
import com.lp.server.lieferschein.service.LieferscheinpositionDto;
import com.lp.server.rechnung.service.RechnungPositionDto;
import com.lp.server.stueckliste.service.StuecklistearbeitsplanDto;
import com.lp.server.stueckliste.service.StuecklistepositionDto;
import com.lp.server.system.service.BelegpositionkonvertierungFac;
import com.lp.server.system.service.TheClientDto;
import com.lp.service.BelegpositionDto;
import com.lp.util.EJBExceptionLP;

public class BelegpostionkonvertierungDelegate extends Delegate {
	private Context context;
	private BelegpositionkonvertierungFac belegpositionkonvertierungFac;

	public BelegpostionkonvertierungDelegate() throws ExceptionLP {
		try {
			context = new InitialContext();
			belegpositionkonvertierungFac = (BelegpositionkonvertierungFac) context
					.lookup("lpserver/BelegpositionkonvertierungFacBean/remote");

		} catch (Throwable t) {
			throw new ExceptionLP(EJBExceptionLP.FEHLER, t);
		}

	}

	public AgstklpositionDto[] konvertiereNachAgstklpositionDto(
			BelegpositionDto[] belegpositionDto, Integer agstklIId) throws Throwable {
		try {
			return belegpositionkonvertierungFac
					.konvertiereNachAgstklpositionDto(belegpositionDto, agstklIId,
							LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}

	public LossollarbeitsplanDto[] konvertiereNachLossollarbeitsplanDto(
			BelegpositionDto[] belegpositionDto) throws Throwable {
		try {
			return belegpositionkonvertierungFac
					.konvertiereNachLossollarbeitsplanDto(belegpositionDto,
							LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}

	public AnfragepositionDto[] konvertiereNachAnfragepositionDto(
			AnfrageDto anfrageDto, BelegpositionDto[] belegpositionDto)
			throws Throwable {
		try {
			return belegpositionkonvertierungFac
					.konvertiereNachAnfragepositionDto(anfrageDto,
							belegpositionDto, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}

	public RechnungPositionDto[] konvertiereNachRechnungpositionDto(
			BelegpositionDto[] belegpositionDto) throws Throwable {
		try {
			return belegpositionkonvertierungFac
					.konvertiereNachRechnungpositionDto(belegpositionDto,
							LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}

	public AngebotpositionDto[] konvertiereNachAngebotpositionDto(
			BelegpositionDto[] belegpositionDto) throws Throwable {
		try {
			return belegpositionkonvertierungFac
					.konvertiereNachAngebotpositionDto(belegpositionDto,
							LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}

	public AuftragpositionDto[] konvertiereNachAuftragpositionDto(
			BelegpositionDto[] belegpositionDto) throws Throwable {
		try {
			return belegpositionkonvertierungFac
					.konvertiereNachAuftragpositionDto(belegpositionDto,
							LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}

	public LieferscheinpositionDto[] konvertiereNachLieferscheinpositionDto(
			BelegpositionDto[] belegpositionDto) throws Throwable {
		try {
			return belegpositionkonvertierungFac
					.konvertiereNachLieferscheinpositionDto(belegpositionDto,
							LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}

	public WartungsschritteDto[] konvertiereNachWartungsschritteDto(
			BelegpositionDto[] belegpositionDto) throws Throwable {
		try {
			return belegpositionkonvertierungFac
					.konvertiereNachWartungsschritteDto(belegpositionDto,
							LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}

	public WartungslisteDto[] konvertiereNachWartungslisteDto(
			BelegpositionDto[] belegpositionDto) throws Throwable {
		try {
			return belegpositionkonvertierungFac
					.konvertiereNachWartungslisteDto(belegpositionDto,
							LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}

	public StuecklistepositionDto[] konvertiereNachStklpositionDto(
			BelegpositionDto[] belegpositionDto) throws Throwable {
		try {
			return belegpositionkonvertierungFac
					.konvertiereNachStklpositionDto(belegpositionDto,
							LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}

	public StuecklistearbeitsplanDto[] konvertiereNachStklarbeitsplanDto(
			BelegpositionDto[] belegpositionDto) throws Throwable {
		try {
			return belegpositionkonvertierungFac
					.konvertiereNachStklarbeitsplanDto(belegpositionDto,
							LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}
	public AgstklarbeitsplanDto[] konvertiereNachAgstklarbeitsplanDto(
			BelegpositionDto[] belegpositionDto) throws Throwable {
		try {
			return belegpositionkonvertierungFac
					.konvertiereNachAgstklarbeitsplanDto(belegpositionDto,
							LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}

	public EinkaufsangebotpositionDto[] konvertiereNachEinkaufsangebotpositionDto(
			BelegpositionDto[] belegpositionDto) throws Throwable {
		try {
			return belegpositionkonvertierungFac
					.konvertiereNachEinkaufsangebotpositionDto(
							belegpositionDto, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}

	public BestellvorschlagDto[] konvertiereNachBestellvorschlagDto(
			BelegpositionDto[] belegpositionDto) throws ExceptionLP {
		try {
			return belegpositionkonvertierungFac
					.konvertiereNachBestellvorschlagDto(belegpositionDto,
							LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
		return null;
	}

	public BestellpositionDto[] konvertiereNachBestellpositionDto(
			BestellungDto bestellungDto, BelegpositionDto[] belegpositionDto)
			throws Throwable {
		try {
			return belegpositionkonvertierungFac
					.konvertiereNachBestellpositionDto(bestellungDto,
							belegpositionDto, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}
}
