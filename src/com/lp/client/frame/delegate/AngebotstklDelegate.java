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
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.pc.LPMain;
import com.lp.server.angebotstkl.service.AgstklDto;
import com.lp.server.angebotstkl.service.AgstklarbeitsplanDto;
import com.lp.server.angebotstkl.service.AgstklmengenstaffelDto;
import com.lp.server.angebotstkl.service.AngebotstklFac;
import com.lp.server.angebotstkl.service.AufschlagDto;
import com.lp.server.angebotstkl.service.EinkaufsangebotDto;
import com.lp.server.angebotstkl.service.EinkaufsangebotpositionDto;
import com.lp.server.util.report.JasperPrintLP;

public class AngebotstklDelegate extends Delegate {
	private Context context;
	private AngebotstklFac stuecklisteFac;

	public AngebotstklDelegate() throws Exception {
		context = new InitialContext();
		stuecklisteFac = (AngebotstklFac) context
				.lookup("lpserver/AngebotstklFacBean/remote");
	}

	public AufschlagDto[] aufschlagFindByBMaterial(Integer agstklIId,
			Short bMaterial) throws ExceptionLP {
		try {
			return stuecklisteFac.aufschlagFindByBMaterial(agstklIId,
					bMaterial, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Integer createAgstkl(AgstklDto agstklDto) throws ExceptionLP {
		try {
			return stuecklisteFac
					.createAgstkl(agstklDto, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}

	public Integer createAgstklarbeitsplan(
			AgstklarbeitsplanDto agstklarbeitsplanDto) throws ExceptionLP {
		try {
			return stuecklisteFac.createAgstklarbeitsplan(agstklarbeitsplanDto,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}

	public Integer createEinkaufsangebot(EinkaufsangebotDto einkaufsangebotDto)
			throws ExceptionLP {
		try {
			return stuecklisteFac.createEinkaufsangebot(einkaufsangebotDto,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}

	public Integer createAufschlag(AufschlagDto aufschlagDto)
			throws ExceptionLP {
		try {
			return stuecklisteFac.createAufschlag(aufschlagDto,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}

	public Integer createAgstklmengenstaffel(
			AgstklmengenstaffelDto agstklmengenstaffelDto) throws ExceptionLP {
		try {
			return stuecklisteFac.createAgstklmengenstaffel(
					agstklmengenstaffelDto, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}

	public Integer createEinkaufsangebotposition(
			EinkaufsangebotpositionDto einkaufsangebotpositionDto)
			throws ExceptionLP {
		try {
			return stuecklisteFac.createEinkaufsangebotposition(
					einkaufsangebotpositionDto, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}

	public Integer createEinkaufsangebotpositions(
			EinkaufsangebotpositionDto[] einkaufsangebotpositionDto)
			throws ExceptionLP {
		try {
			return stuecklisteFac.createEinkaufsangebotpositions(
					einkaufsangebotpositionDto, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}

	/**
	 * Eine Angebotst&uuml;ckliste drucken.
	 * 
	 * @param iIdAngebotstklI
	 *            PK der Angebotstkl
	 * @return JasperPrint der Druck
	 * @throws ExceptionLP
	 *             Ausnahme
	 */
	public JasperPrintLP printAngebotstkl(Integer iIdAngebotstklI)
			throws ExceptionLP {
		JasperPrintLP oPrint = null;

		try {
			oPrint = stuecklisteFac.printAngebotstkl(iIdAngebotstklI,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
		return oPrint;
	}

	public JasperPrintLP printAngebotstklmenenstaffel(Integer iIdAngebotstklI)
			throws ExceptionLP {
		JasperPrintLP oPrint = null;

		try {
			oPrint = stuecklisteFac.printAngebotstklmenenstaffel(
					iIdAngebotstklI, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
		return oPrint;
	}

	public JasperPrintLP printEinkaufsangebot(Integer einkaufsangebotIId,
			int iSortierung) throws ExceptionLP {
		JasperPrintLP oPrint = null;

		try {
			oPrint = stuecklisteFac.printEinkaufsangebot(einkaufsangebotIId,
					iSortierung, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
		return oPrint;
	}

	public void removeAgstkl(AgstklDto agstklDto) throws ExceptionLP {
		try {
			stuecklisteFac.removeAgstkl(agstklDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void removeAgstklmengenstaffel(Integer agstklmengenstaffelIId)
			throws ExceptionLP {
		try {
			stuecklisteFac.removeAgstklmengenstaffel(agstklmengenstaffelIId);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void removeAgstklarbeitsplan(
			AgstklarbeitsplanDto agstklarbeitsplanDto) throws ExceptionLP {
		try {
			stuecklisteFac.removeAgstklarbeitsplan(agstklarbeitsplanDto,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void removeAufschlag(AufschlagDto aufschlagDto) throws ExceptionLP {
		try {
			stuecklisteFac.removeAufschlag(aufschlagDto.getIId());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void removeEinkaufsangebotposition(
			EinkaufsangebotpositionDto einkaufsangebotpositionDto)
			throws ExceptionLP {
		try {
			stuecklisteFac
					.removeEinkaufsangebotposition(einkaufsangebotpositionDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updateAgstkl(AgstklDto agstklDto) throws ExceptionLP {
		try {
			stuecklisteFac.updateAgstkl(agstklDto, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}

	}

	public void updateAgstklarbeitsplan(
			AgstklarbeitsplanDto agstklarbeitsplanDto) throws ExceptionLP {
		try {
			stuecklisteFac.updateAgstklarbeitsplan(agstklarbeitsplanDto,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}

	}

	public void updateAufschlag(AufschlagDto aufschlagDto) throws ExceptionLP {
		try {
			stuecklisteFac.updateAufschlag(aufschlagDto, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}

	}

	public void updateAgstklmengenstaffel(
			AgstklmengenstaffelDto agstklmengenstaffelDto) throws ExceptionLP {
		try {
			stuecklisteFac.updateAgstklmengenstaffel(agstklmengenstaffelDto,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}

	}

	public void kopierePositionenAusStueckliste(Integer stuecklisteIId,
			Integer agstklIId) throws ExceptionLP {
		try {
			stuecklisteFac.kopierePositionenAusStueckliste(stuecklisteIId,
					agstklIId, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}

	}

	public void kopiereArbeitsplanAusStuecklisteInArbeitsplan(Integer stuecklisteIId,
			Integer agstklIId) throws ExceptionLP {
		try {
			stuecklisteFac.kopiereArbeitsplanAusStuecklisteInArbeitsplan(stuecklisteIId,
					agstklIId, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}

	}
	public void kopiereArbeitsplanAusStuecklisteInPositionen(Integer stuecklisteIId,
			Integer agstklIId) throws ExceptionLP {
		try {
			stuecklisteFac.kopiereArbeitsplanAusStuecklisteInPositionen(stuecklisteIId,
					agstklIId, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}

	}

	public void updateAgstklaufschlag(Integer agstklIId,
			AufschlagDto[] aufschlagDtos) throws ExceptionLP {
		try {
			stuecklisteFac.updateAgstklaufschlag(agstklIId, aufschlagDtos,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}

	}

	public void updateEinkaufsangebot(EinkaufsangebotDto einkaufsangebotDto)
			throws ExceptionLP {
		try {
			stuecklisteFac.updateEinkaufsangebot(einkaufsangebotDto,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}

	}

	public void updateEinkaufsangebotposition(
			EinkaufsangebotpositionDto einkaufsangebotpositionDto)
			throws ExceptionLP {
		try {
			stuecklisteFac
					.updateEinkaufsangebotposition(einkaufsangebotpositionDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}

	}

	public void vertauscheEinkausangebotpositionen(Integer iIdPosition1I,
			Integer iIdPosition2I) throws ExceptionLP {
		try {
			stuecklisteFac.vertauscheEinkausangebotpositionen(iIdPosition1I,
					iIdPosition2I);
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public void sortierungAnpassenBeiEinfuegenEinerPositionVorPosition(
			Integer einkaufsangebotIId, int iSortierungNeuePositionI)
			throws ExceptionLP {
		try {
			stuecklisteFac
					.sortierungAnpassenBeiEinfuegenEinerPositionVorPosition(
							einkaufsangebotIId, iSortierungNeuePositionI);
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public AgstklDto agstklFindByPrimaryKey(Integer iId) throws ExceptionLP {
		try {
			return stuecklisteFac.agstklFindByPrimaryKey(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}

	public AufschlagDto aufschlagFindByPrimaryKey(Integer iId)
			throws ExceptionLP {
		try {
			return stuecklisteFac.aufschlagFindByPrimaryKey(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}

	public AgstklmengenstaffelDto agstklmengenstaffelFindByPrimaryKey(
			Integer iId) throws ExceptionLP {
		try {
			return stuecklisteFac.agstklmengenstaffelFindByPrimaryKey(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}

	public AgstklarbeitsplanDto agstklarbeitsplanFindByPrimaryKey(Integer iId)
			throws ExceptionLP {
		try {
			return stuecklisteFac.agstklarbeitsplanFindByPrimaryKey(iId,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}

	public EinkaufsangebotDto einkaufsangebotFindByPrimaryKey(Integer iId)
			throws ExceptionLP {
		try {
			return stuecklisteFac.einkaufsangebotFindByPrimaryKey(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}

	public EinkaufsangebotpositionDto einkaufsangebotpositionFindByPrimaryKey(
			Integer iId) throws ExceptionLP {
		try {
			return stuecklisteFac.einkaufsangebotpositionFindByPrimaryKey(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}

	public String getAngeboteDieBestimmteAngebotsstuecklisteVerwenden(
			Integer agstklId) throws ExceptionLP {
		try {
			return stuecklisteFac
					.getAngeboteDieBestimmteAngebotsstuecklisteVerwenden(
							agstklId, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Map<String, String> getAllAgstklpositionsart() throws ExceptionLP {
		try {
			return stuecklisteFac.getAllAgstklpositionsart();
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	/**
	 * Liefert den kalkulatorischen Wert einer Angebotsstueckliste.
	 * 
	 * @param iIdAgstklI
	 *            PK der Agstkl
	 * @param cNrWaehrungI
	 *            die gewuenschte Waehrung
	 * @return BigDecimal der kalkulatorische Wert
	 * @throws ExceptionLP
	 */
	public BigDecimal berechneKalkulatorischenAgstklwert(Integer iIdAgstklI,BigDecimal nMengenstaffel,
			String cNrWaehrungI) throws ExceptionLP {
		BigDecimal nWert = new BigDecimal(0);

		try {
			nWert = stuecklisteFac.berechneKalkulatorischenAgstklwert(
					iIdAgstklI, nMengenstaffel, cNrWaehrungI, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}

		return nWert;
	}

	public BigDecimal[] berechneAgstklMaterialwertUndArbeitszeitwert(
			Integer iIdAgstklI) throws ExceptionLP {
		BigDecimal[] nWerte = null;

		try {
			nWerte = stuecklisteFac
					.berechneAgstklMaterialwertUndArbeitszeitwert(iIdAgstklI,
							LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}

		return nWerte;
	}

	public Integer getNextArbeitsgang(Integer agstklIId) throws ExceptionLP {
		try {
			return stuecklisteFac.getNextArbeitsgang(agstklIId,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public BigDecimal getWareneinsatzLief1(BigDecimal bdMenge, Integer agstklIId)
			throws ExceptionLP {
		try {
			return stuecklisteFac.getWareneinsatzLief1(bdMenge, agstklIId,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public BigDecimal getAZeinsatzLief1(BigDecimal bdMenge, Integer agstklIId)
			throws ExceptionLP {
		try {
			return stuecklisteFac.getAZeinsatzLief1(bdMenge, agstklIId,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public BigDecimal[] getVKPreis(BigDecimal bdMenge, Integer agstklIId)
			throws ExceptionLP {
		try {
			return stuecklisteFac.getVKPreis(bdMenge, agstklIId,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public BigDecimal getVKPreisGewaehlt(BigDecimal bdMenge, Integer agstklIId)
			throws ExceptionLP {
		try {
			return stuecklisteFac.getVKPreisGewaehlt(bdMenge, agstklIId,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public void kopiereAgstklArbeitsplan(Integer agstklIId_Quelle,
			Integer agstklIId_Ziel) throws ExceptionLP {
		try {
			stuecklisteFac.kopiereAgstklArbeitsplan(agstklIId_Quelle,
					agstklIId_Ziel, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}

	}
}
