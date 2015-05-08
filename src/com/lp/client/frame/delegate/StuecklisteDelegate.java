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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.naming.Context;
import javax.naming.InitialContext;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.pc.LPMain;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.stueckliste.service.FertigungsgruppeDto;
import com.lp.server.stueckliste.service.IStklImportResult;
import com.lp.server.stueckliste.service.KommentarimportDto;
import com.lp.server.stueckliste.service.MontageartDto;
import com.lp.server.stueckliste.service.PosersatzDto;
import com.lp.server.stueckliste.service.StklagerentnahmeDto;
import com.lp.server.stueckliste.service.StrukturierterImportDto;
import com.lp.server.stueckliste.service.StrukturierterImportSiemensNXDto;
import com.lp.server.stueckliste.service.StuecklisteDto;
import com.lp.server.stueckliste.service.StuecklisteFac;
import com.lp.server.stueckliste.service.StuecklistearbeitsplanDto;
import com.lp.server.stueckliste.service.StuecklisteeigenschaftDto;
import com.lp.server.stueckliste.service.StuecklisteeigenschaftartDto;
import com.lp.server.stueckliste.service.StuecklisteimportFac;
import com.lp.server.stueckliste.service.StuecklistepositionDto;
import com.lp.server.system.service.IntelligenterStklImportFac;
import com.lp.server.system.service.TheClientDto;
import com.lp.service.StklImportSpezifikation;

@SuppressWarnings("static-access")
public class StuecklisteDelegate extends Delegate {
	private Context context;
	private StuecklisteFac stuecklisteFac;
	private StuecklisteimportFac stuecklisteimportFac;
	private IntelligenterStklImportFac iStklImportFac;

	public StuecklisteDelegate() throws Exception {
		context = new InitialContext();
		stuecklisteFac = (StuecklisteFac) context
				.lookup("lpserver/StuecklisteFacBean/remote");
		stuecklisteimportFac = (StuecklisteimportFac) context
				.lookup("lpserver/StuecklisteimportFacBean/remote");
		iStklImportFac = (IntelligenterStklImportFac) context
				.lookup("lpserver/IntelligenterStklImportFacBean/remote");
	}

	public String pruefeUndImportiereArbeitsplanXLS(byte[] xlsDatei,
			String einheitStueckRuestZeit, boolean bImportierenWennKeinFehler)
			throws ExceptionLP {
		try {
			return stuecklisteimportFac.pruefeUndImportiereArbeitsplanXLS(
					xlsDatei, einheitStueckRuestZeit,
					bImportierenWennKeinFehler, LPMain.getInstance()
							.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public String pruefeUndImportiereMaterialXLS(byte[] xlsDatei,
			boolean bImportierenWennKeinFehler) throws ExceptionLP {
		try {
			return stuecklisteimportFac.pruefeUndImportiereMaterialXLS(
					xlsDatei, bImportierenWennKeinFehler, LPMain.getInstance()
							.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Integer createMontageart(MontageartDto montageartDto)
			throws ExceptionLP {
		try {
			return stuecklisteFac.createMontageart(montageartDto, LPMain
					.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Integer createPosersatz(PosersatzDto posersatzDtio)
			throws ExceptionLP {
		try {
			return stuecklisteFac.createPosersatz(posersatzDtio, LPMain
					.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Integer createKommentarimport(KommentarimportDto kommentarimportDto)
			throws ExceptionLP {
		try {
			return stuecklisteFac.createKommentarimport(kommentarimportDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public void updateKommentarimport(KommentarimportDto kommentarimportDto)
			throws ExceptionLP {
		try {
			stuecklisteFac.updateKommentarimport(kommentarimportDto);
		} catch (Throwable ex) {
			handleThrowable(ex);

		}
	}

	public Integer createFertigungsgruppe(FertigungsgruppeDto fertigungsgruppe)
			throws ExceptionLP {
		try {
			return stuecklisteFac.createFertigungsgruppe(fertigungsgruppe,
					LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Integer createStueckliste(StuecklisteDto stuecklisteDto)
			throws ExceptionLP {
		try {
			return stuecklisteFac.createStueckliste(stuecklisteDto, LPMain
					.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}

	public void removeMontageart(MontageartDto montageartDto)
			throws ExceptionLP {
		try {
			stuecklisteFac.removeMontageart(montageartDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void removePosersatz(PosersatzDto posersatzDto) throws ExceptionLP {
		try {
			stuecklisteFac.removePosersatz(posersatzDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void removeKommentarimport(KommentarimportDto dto)
			throws ExceptionLP {
		try {
			stuecklisteFac.removeKommentarimport(dto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void removeFertigungsgruppe(FertigungsgruppeDto fertigungsgruppeDto)
			throws ExceptionLP {
		try {
			stuecklisteFac.removeFertigungsgruppe(fertigungsgruppeDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void sortierungAnpassenBeiEinfuegenEinerPositionVorPosition(
			Integer stueckliste, int iSortierungNeuePositionI)
			throws ExceptionLP {
		try {
			stuecklisteFac
					.sortierungAnpassenBeiEinfuegenEinerPositionVorPosition(
							stueckliste, iSortierungNeuePositionI);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public ArrayList<?> importiereStuecklistenstruktur(
			ArrayList<StrukturierterImportDto> struktur,
			boolean bAnfragevorschlagErzeugen,
			java.sql.Timestamp tLieferterminfuerAnfrageVorschlag)
			throws ExceptionLP {
		try {
			return stuecklisteFac.importiereStuecklistenstruktur(struktur,
					null, LPMain.getInstance().getTheClient(),
					bAnfragevorschlagErzeugen,
					tLieferterminfuerAnfrageVorschlag);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public TreeMap<String, Integer> holeAlleWurzelstuecklisten()
			throws ExceptionLP {
		try {
			return stuecklisteFac.holeAlleWurzelstuecklisten(LPMain
					.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public TreeMap<String, Integer> holeNaechsteEbene(Integer stuecklisteIId)
			throws ExceptionLP {
		try {
			return stuecklisteFac.holeNaechsteEbene(stuecklisteIId, LPMain
					.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public ArrayList<ArtikelDto> importiereStuecklistenstrukturSiemensNX(
			ArrayList<StrukturierterImportSiemensNXDto> stueckliste,
			ArrayList<StrukturierterImportSiemensNXDto> listeFlach,
			Integer stuecklisteIIdKopf) throws ExceptionLP {
		try {
			return stuecklisteFac.importiereStuecklistenstrukturSiemensNX(
					stueckliste, listeFlach, stuecklisteIIdKopf, LPMain
							.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public void kopiereStuecklistenPositionen(Integer stuecklisteIId_Quelle,
			Integer stuecklisteIId_Ziel) throws ExceptionLP {
		try {
			stuecklisteFac.kopiereStuecklistenPositionen(stuecklisteIId_Quelle,
					stuecklisteIId_Ziel, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void kopiereStuecklisteArbeitsplan(Integer stuecklisteIId_Quelle,
			Integer stuecklisteIId_Ziel) throws ExceptionLP {
		try {
			stuecklisteFac.kopiereStuecklisteArbeitsplan(stuecklisteIId_Quelle,
					stuecklisteIId_Ziel, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void kopiereAusAgstkl(Integer agstklIId, Integer stuecklisteIId)
			throws ExceptionLP {
		try {
			stuecklisteFac.kopiereAusAgstkl(agstklIId, stuecklisteIId, LPMain
					.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updateMontageart(MontageartDto montageartDto)
			throws ExceptionLP {
		try {
			stuecklisteFac.updateMontageart(montageartDto, LPMain.getInstance()
					.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}

	}

	public void updatePosersatz(PosersatzDto posersatzDto) throws ExceptionLP {
		try {
			stuecklisteFac.updatePosersatz(posersatzDto, LPMain.getInstance()
					.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}

	}

	public void updateFertigungsgruppe(FertigungsgruppeDto fertigungsgruppeDto)
			throws ExceptionLP {
		try {
			stuecklisteFac.updateFertigungsgruppe(fertigungsgruppeDto, LPMain
					.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}

	}

	public void vertauscheStuecklisteposition(Integer iIdPosition1I,
			Integer iIdPosition2I) throws ExceptionLP {
		try {
			stuecklisteFac.vertauscheStuecklisteposition(iIdPosition1I,
					iIdPosition2I);
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public void vertauscheMontageart(Integer iIdMontageart1I,
			Integer iIdMontageart2I) throws ExceptionLP {
		try {
			stuecklisteFac.vertauscheMontageart(iIdMontageart1I,
					iIdMontageart2I);
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public void vertauschePosersatz(Integer iIdPosersatz1I,
			Integer iIdPosersatz2I) throws ExceptionLP {
		try {
			stuecklisteFac.vertauschePosersatz(iIdPosersatz1I, iIdPosersatz2I);
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public void vertauscheStuecklisteeigenschaftart(
			Integer iIdStuecklisteeigenschaftart1I,
			Integer iIdStuecklisteeigenschaftart2I) throws ExceptionLP {
		try {
			stuecklisteFac.vertauscheStuecklisteeigenschaftart(
					iIdStuecklisteeigenschaftart1I,
					iIdStuecklisteeigenschaftart2I);
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public MontageartDto montageartFindByPrimaryKey(Integer iId)
			throws ExceptionLP {
		try {
			return stuecklisteFac.montageartFindByPrimaryKey(iId, LPMain
					.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}

	public PosersatzDto posersatzFindByPrimaryKey(Integer iId)
			throws ExceptionLP {
		try {
			return stuecklisteFac.posersatzFindByPrimaryKey(iId, LPMain
					.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}

	public FertigungsgruppeDto fertigungsgruppeFindByPrimaryKey(Integer iId)
			throws ExceptionLP {
		try {
			return stuecklisteFac.fertigungsgruppeFindByPrimaryKey(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}

	public KommentarimportDto kommentarimportFindByPrimaryKey(Integer iId)
			throws ExceptionLP {
		try {
			return stuecklisteFac.kommentarimportFindByPrimaryKey(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}

	public FertigungsgruppeDto[] fertigungsgruppeFindByMandantCNr()
			throws ExceptionLP {
		try {
			return stuecklisteFac.fertigungsgruppeFindByMandantCNr(LPMain
					.getInstance().getTheClient().getMandant(), LPMain
					.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}

	public StuecklisteDto stuecklisteFindByPrimaryKey(Integer iId)
			throws ExceptionLP {
		try {
			return stuecklisteFac.stuecklisteFindByPrimaryKey(iId, LPMain
					.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}

	public StuecklisteDto[] unterstuecklistenFindByStuecklisteIId(
			Integer stuecklisteIId) throws ExceptionLP {
		try {
			return stuecklisteFac.unterstuecklistenFindByStuecklisteIId(
					stuecklisteIId, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}

	public StuecklistepositionDto stuecklistepositionFindByPrimaryKey(
			Integer iId) throws ExceptionLP {
		try {
			return stuecklisteFac.stuecklistepositionFindByPrimaryKey(iId,
					LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}

	public StuecklistepositionDto[] stuecklistepositionFindByStuecklisteIId(
			Integer stuecklisteIId) throws ExceptionLP {
		try {
			return stuecklisteFac.stuecklistepositionFindByStuecklisteIId(
					stuecklisteIId, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}

	public StuecklistearbeitsplanDto[] stuecklistearbeitsplanFindByStuecklisteIId(
			Integer stuecklisteIId) throws ExceptionLP {
		try {
			return stuecklisteFac.stuecklistearbeitsplanFindByStuecklisteIId(
					stuecklisteIId, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}

	public MontageartDto montageartFindByMandantCNrCBez(String cBez)
			throws Throwable {
		return stuecklisteFac.montageartFindByMandantCNrCBez(cBez, LPMain
				.getInstance().getTheClient());
	}

	public MontageartDto[] montageartFindByMandantCNr() throws Throwable {
		return stuecklisteFac.montageartFindByMandantCNr(LPMain.getInstance()
				.getTheClient());
	}

	public Integer createStuecklistearbeitsplan(
			StuecklistearbeitsplanDto stuecklistearbeitsplanDto)
			throws ExceptionLP {

		try {
			return stuecklisteFac.createStuecklistearbeitsplan(
					stuecklistearbeitsplanDto, LPMain.getInstance()
							.getTheClient());

		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}

	public Integer createStuecklistearbeitsplans(
			StuecklistearbeitsplanDto[] stuecklistearbeitsplanDtos)
			throws ExceptionLP {
		try {
			return stuecklisteFac.createStuecklistearbeitsplans(
					stuecklistearbeitsplanDtos, LPMain.getInstance()
							.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public BigDecimal berechneZielmenge(Integer stuecklistepositionIId)
			throws ExceptionLP {

		try {
			return stuecklisteFac.berechneZielmenge(stuecklistepositionIId,
					LPMain.getInstance().getTheClient());

		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}

	public void removeStuecklistearbeitsplan(
			StuecklistearbeitsplanDto stuecklistearbeitsplanDto)
			throws ExceptionLP {
		try {
			stuecklisteFac.removeStuecklistearbeitsplan(
					stuecklistearbeitsplanDto, LPMain.getInstance()
							.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updateStuecklistearbeitsplan(
			StuecklistearbeitsplanDto stuecklistearbeitsplanDto)
			throws ExceptionLP {

		try {
			stuecklisteFac.updateStuecklistearbeitsplan(
					stuecklistearbeitsplanDto, LPMain.getInstance()
							.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}

	}

	public void updateStueckliste(StuecklisteDto stuecklisteDto)
			throws ExceptionLP {
		try {
			stuecklisteFac.updateStueckliste(stuecklisteDto, LPMain
					.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}

	}

	public void updateStuecklisteKommentar(StuecklisteDto stuecklisteDto)
			throws ExceptionLP {
		try {
			stuecklisteFac.updateStuecklisteKommentar(stuecklisteDto, LPMain
					.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}

	}

	public StuecklistearbeitsplanDto stuecklistearbeitsplanFindByPrimaryKey(
			Integer iId) throws ExceptionLP {
		try {

			return stuecklisteFac.stuecklistearbeitsplanFindByPrimaryKey(iId,
					LPMain.getInstance().getTheClient());

		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}

	public Integer getNextArbeitsgang(Integer stuecklisteId) throws ExceptionLP {
		try {
			return stuecklisteFac.getNextArbeitsgang(stuecklisteId, LPMain
					.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}

	public void artikelErsetzten(Integer artikelIIdVon, Integer artikelIIdDurch)
			throws ExceptionLP {
		try {
			stuecklisteFac.artikelErsetzten(artikelIIdVon, artikelIIdDurch,
					LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}

	}

	public Integer getNextFertigungsgruppe() throws ExceptionLP {
		try {
			return stuecklisteFac.getNextFertigungsgruppe(LPMain.getInstance()
					.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Map<?, ?> getAllStuecklisteart() throws ExceptionLP {
		try {
			return stuecklisteFac.getAllStuecklisteart(LPMain.getInstance()
					.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public HashMap<Integer, String> getAlleStuecklistenIIdsFuerVerwendungsnachweis(
			Integer artikelIId) throws ExceptionLP {
		try {
			return stuecklisteFac
					.getAlleStuecklistenIIdsFuerVerwendungsnachweis(artikelIId,
							LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Map<?, ?> getAllFertigungsgrupe() throws ExceptionLP {
		try {
			return stuecklisteFac.getAllFertigungsgrupe(LPMain.getInstance()
					.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Map<?, ?> getEingeschraenkteFertigungsgruppen() throws ExceptionLP {
		try {
			return stuecklisteFac.getEingeschraenkteFertigungsgruppen(LPMain
					.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Integer createStuecklisteposition(
			StuecklistepositionDto stuecklistepositionDto) throws ExceptionLP {
		try {
			return stuecklisteFac
					.createStuecklisteposition(stuecklistepositionDto, LPMain
							.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}

	public Integer createStuecklistepositions(
			StuecklistepositionDto[] stuecklistepositionDtos)
			throws ExceptionLP {
		try {
			return stuecklisteFac.createStuecklistepositions(
					stuecklistepositionDtos, LPMain.getInstance()
							.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}

	public void removeStuecklisteposition(
			StuecklistepositionDto stuecklistepositionDto) throws ExceptionLP {
		try {
			stuecklisteFac.removeStuecklisteposition(stuecklistepositionDto,
					LPMain.getInstance().getTheClient());

		} catch (Throwable ex) {
			handleThrowable(ex);
		}

	}

	public void removeAlleStuecklistenpositionen(Integer stuecklisteIId)
			throws ExceptionLP {
		try {
			stuecklisteFac.removeAlleStuecklistenpositionen(stuecklisteIId);

		} catch (Throwable ex) {
			handleThrowable(ex);
		}

	}

	public void removeStueckliste(StuecklisteDto stuecklisteDto)
			throws ExceptionLP {
		try {
			stuecklisteFac.removeStueckliste(stuecklisteDto,
					LPMain.getTheClient());

		} catch (Throwable ex) {
			handleThrowable(ex);
		}

	}

	public void updateStuecklisteposition(
			StuecklistepositionDto stuecklistepositionDto) throws ExceptionLP {
		try {
			stuecklisteFac.updateStuecklisteposition(stuecklistepositionDto,
					LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}

	}

	public Integer createStuecklisteeigenschaft(
			StuecklisteeigenschaftDto stuecklisteeigenschaftDto)
			throws ExceptionLP {

		try {
			return stuecklisteFac.createStuecklisteeigenschaft(
					stuecklisteeigenschaftDto, LPMain.getInstance()
							.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}

	public void removeStuecklisteeigenschaft(
			StuecklisteeigenschaftDto stuecklisteeigenschaftDto)
			throws ExceptionLP {
		try {
			stuecklisteFac
					.removeStuecklisteeigenschaft(stuecklisteeigenschaftDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updateStuecklisteeigenschaft(
			StuecklisteeigenschaftDto stuecklisteeigenschaftDto)
			throws Throwable {
		stuecklisteFac.updateStuecklisteeigenschaft(stuecklisteeigenschaftDto,
				LPMain.getInstance().getTheClient());
	}

	public StuecklisteeigenschaftDto stuecklisteeigenschaftFindByPrimaryKey(
			Integer iId) throws ExceptionLP {
		try {
			return stuecklisteFac.stuecklisteeigenschaftFindByPrimaryKey(iId,
					LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}

	public StuecklisteeigenschaftDto stuecklisteeigenschaftFindByStuecklisteIIdStuecklisteeigenschaftartIId(
			Integer stuecklisteIId, Integer stuecklisteeigenschaftartIId)
			throws Exception {
		return stuecklisteFac
				.stuecklisteeigenschaftFindByStuecklisteIIdStuecklisteeigenschaftartIId(
						stuecklisteIId, stuecklisteeigenschaftartIId);
	}

	public Integer createStuecklisteeigenschaftart(
			StuecklisteeigenschaftartDto stuecklisteeigenschaftartDto)
			throws ExceptionLP {
		try {
			return stuecklisteFac.createStuecklisteeigenschaftart(
					stuecklisteeigenschaftartDto, LPMain.getInstance()
							.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public void removeStuecklisteeigenschaftart(
			StuecklisteeigenschaftartDto stuecklisteeigenschaftartDto)
			throws ExceptionLP {
		try {
			stuecklisteFac
					.removeStuecklisteeigenschaftart(stuecklisteeigenschaftartDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updateStuecklisteeigenschaftart(
			StuecklisteeigenschaftartDto stuecklisteeigenschaftartDto)
			throws ExceptionLP {
		try {
			stuecklisteFac.updateStuecklisteeigenschaftart(
					stuecklisteeigenschaftartDto, LPMain.getInstance()
							.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public StuecklisteeigenschaftartDto stuecklisteeigenschaftartFindByPrimaryKey(
			Integer iId) throws ExceptionLP {
		try {
			return stuecklisteFac.stuecklisteeigenschaftartFindByPrimaryKey(
					iId, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public StuecklisteeigenschaftartDto stuecklisteeigenschaftartFindByCBez(
			String cBez) throws ExceptionLP {
		try {
			return stuecklisteFac.stuecklisteeigenschaftartFindByCBez(cBez);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public StuecklisteDto stuecklisteFindByMandantCNrArtikelIIdOhneExc(
			Integer artikelIId) throws ExceptionLP {
		try {
			return stuecklisteFac.stuecklisteFindByMandantCNrArtikelIIdOhneExc(
					artikelIId, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	/**
	 * Saemtliche ArtikelIIds einer Stueckliste zurueckliefern, die entweder
	 * Serien- oder Chargennummerntragend sind. Eine ArtikelIId kann mehrfach
	 * vorkommen wenn der gleiche Artikel mehrfach in der Stueckliste
	 * aufgefuehrt ist.
	 * 
	 * @param stuecklisteIId
	 *            ist die Stueckliste des Artikelsets
	 * @param nmenge
	 *            ist die zu erfuellende Menge des Artikelsets
	 * @return eine (leere) Liste von ArtikelIIds die serien- oder
	 *         chargennummerntragend sind.
	 * @throws ExceptionLP
	 */
	public List<Integer> getSeriennrChargennrArtikelIIdsFromArtikelset(
			Integer stuecklisteIId, BigDecimal nmenge) throws ExceptionLP {

		try {
			return stuecklisteFac
					.getSeriennrChargennrArtikelIIdsFromStueckliste(
							stuecklisteIId, nmenge, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
			return new ArrayList<Integer>();
		}
	}

	/**
	 * Sucht nach Artikeln f&uuml;r den intelligenten Stkl. Import
	 * 
	 * @param spez
	 *            die Importspezifikation
	 * @param importLines
	 *            die Zeilen der Importdatei als Rohdaten, also nicht
	 *            umformatiert
	 * @param rowIndex
	 *            = die Nummer der Zeile in der Datei, welche
	 *            <code>importLines.get(0)</code> entspricht.
	 * @return eine Liste von {@link IStklImportResult}
	 * @throws ExceptionLP
	 */
	public List<IStklImportResult> searchForImportMatches(
			StklImportSpezifikation spez, List<String> importLines, int rowIndex)
			throws ExceptionLP {
		try {
			return iStklImportFac.searchForImportMatches(spez, importLines,
					rowIndex, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
			return new ArrayList<IStklImportResult>();
		}
	}

	/**
	 * Importiert die selektierten Artikel in den <code>results</code> in die
	 * Stueckliste.<br>
	 * Hat ein {@link IStklImportResult} keinen Artikel gesetzt (
	 * <code>{@link IStklImportResult#getSelectedArtikelDto()} == null</code>),
	 * wird ein Handartikel angelegt.
	 * 
	 * @param spez
	 *            die Importspezifikation (<code>spez.getStklIId()</code> darf
	 *            nicht null sein!)
	 * @param results
	 *            Liste der Ergebnisse der clientseitigen Artikelzuordnung
	 * @param updateArtikel true, wenn der Artikelstamm aktualisiert werden
	 * soll
	 * @return die Anzahl der neu angelegten Positionen
	 * @throws ExceptionLP
	 */
	public int importiereStklImportResults(StklImportSpezifikation spez,
			List<IStklImportResult> results, Boolean updateArtikel) throws ExceptionLP {
		try {
			return iStklImportFac.importiereImportResultsAlsBelegpositionen(
					spez, results, updateArtikel, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
		return 0;
	}

	public void createStklImportSpez(StklImportSpezifikation spez)
			throws ExceptionLP {
		try {
			iStklImportFac.createStklImportSpezifikation(spez);
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public void updateStklImportSpez(StklImportSpezifikation spez)
			throws ExceptionLP {
		try {
			iStklImportFac.updateStklImportSpezifikation(spez);
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public void removeStklImportSpez(StklImportSpezifikation spez)
			throws ExceptionLP {
		try {
			iStklImportFac.removeStklImportSpezifikation(spez);
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public Map<String, StklImportSpezifikation> stklImportSpezFindAll(int stklTyp, TheClientDto theClientDto)
			throws ExceptionLP {
		try {
			return iStklImportFac.stklImportSpezifikationenFindAll(stklTyp, theClientDto);
		} catch (Throwable t) {
			handleThrowable(t);
		}
		return null;
	}

	public void importiereStuecklistenINFRA(
			HashMap<String, HashMap<String, byte[]>> dateien)
			throws ExceptionLP {
		try {
			stuecklisteFac.importiereStuecklistenINFRA(dateien,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public void removeStklagerentnahme(StklagerentnahmeDto stklagerentnahmeDto)
			throws ExceptionLP {
		try {
			stuecklisteFac.removeStklagerentnahme(stklagerentnahmeDto,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public StklagerentnahmeDto updateStklagerentnahme(
			StklagerentnahmeDto stklagerentnahmeDto) throws ExceptionLP {
		try {
			if (stklagerentnahmeDto.getIId() == null) {
				return stuecklisteFac.createStklagerentnahme(
						stklagerentnahmeDto, LPMain.getTheClient());
			} else {
				return stuecklisteFac.updateStklagerentnahme(
						stklagerentnahmeDto, LPMain.getTheClient());
			}
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}
	}

	public StklagerentnahmeDto stklagerentnahmeFindByPrimaryKey(Integer iId)
			throws ExceptionLP {
		try {
			return stuecklisteFac.stklagerentnahmeFindByPrimaryKey(iId);
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}
	}

	public void vertauscheStklagerentnahme(Integer iiDLagerentnahme1,
			Integer iIdLagerentnahme2) throws ExceptionLP {
		try {
			stuecklisteFac.vertauscheStklagerentnahme(iiDLagerentnahme1,
					iIdLagerentnahme2);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void toggleFreigabe(Integer stuecklisteIId) throws ExceptionLP {
		try {
			stuecklisteFac
					.toggleFreigabe(stuecklisteIId, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}
}
