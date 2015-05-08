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
import java.util.Locale;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.pc.LPMain;
import com.lp.server.auftrag.service.AuftragServiceFac;
import com.lp.server.auftrag.service.AuftragartDto;
import com.lp.server.auftrag.service.AuftragauftragdokumentDto;
import com.lp.server.auftrag.service.AuftragbegruendungDto;
import com.lp.server.auftrag.service.AuftragdokumentDto;
import com.lp.server.auftrag.service.AuftragpositionArtDto;
import com.lp.server.auftrag.service.AuftragtextDto;
import com.lp.server.auftrag.service.MeilensteinDto;
import com.lp.server.auftrag.service.ZahlungsplanDto;
import com.lp.server.auftrag.service.ZahlungsplanmeilensteinDto;
import com.lp.server.auftrag.service.ZeitplanDto;
import com.lp.server.lieferschein.service.BegruendungDto;
import com.lp.server.system.service.MediaFac;
import com.lp.server.system.service.TheClientDto;

@SuppressWarnings("static-access")
/*
 * <p>Delegate fuer Auftrag Services.</p>
 * 
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 * 
 * <p>Erstellung: Uli Walch, 27. 04. 2005</p>
 * 
 * <p>@author Uli Walch</p>
 * 
 * @version 1.0
 * 
 * @version $Revision: 1.12 $ Date $Date: 2011/03/22 14:22:19 $
 */
public class AuftragServiceDelegate extends Delegate {
	private Context context = null;
	private AuftragServiceFac auftragServiceFac = null;

	public AuftragServiceDelegate() throws ExceptionLP {
		try {
			context = new InitialContext();
			auftragServiceFac = (AuftragServiceFac) context
					.lookup("lpserver/AuftragServiceFacBean/remote");
		} catch (Throwable t) {
			handleThrowable(t);
		}

	}

	public Integer createAuftragbegruendung(AuftragbegruendungDto begruendungDto)
			throws ExceptionLP {
		try {
			return auftragServiceFac.createAuftragbegruendung(begruendungDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}

	public void updateAuftragbegruendung(AuftragbegruendungDto begruendungDto)
			throws ExceptionLP {
		try {
			auftragServiceFac.updateAuftragbegruendung(begruendungDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void removeAuftragbegruendung(Integer iId) throws ExceptionLP {
		try {
			auftragServiceFac.removeAuftragbegruendung(iId);
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public void removeMeilenstein(MeilensteinDto meilensteinDto)
			throws ExceptionLP {
		try {
			auftragServiceFac.removeMeilenstein(meilensteinDto);
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public AuftragbegruendungDto auftragbegruendungFindByPrimaryKey(
			Integer begruendungIId) throws ExceptionLP {
		AuftragbegruendungDto oLieferscheintextDto = null;

		try {
			oLieferscheintextDto = auftragServiceFac
					.auftragbegruendungFindByPrimaryKey(begruendungIId);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}

		return oLieferscheintextDto;
	}

	public AuftragtextDto auftragtextFindByPrimaryKey(Integer iId)
			throws ExceptionLP {
		AuftragtextDto oAuftragtextDto = null;

		try {
			oAuftragtextDto = auftragServiceFac
					.auftragtextFindByPrimaryKey(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}

		return oAuftragtextDto;
	}

	public ZeitplanDto zeitplanFindByPrimaryKey(Integer iId) throws ExceptionLP {

		try {
			return auftragServiceFac.zeitplanFindByPrimaryKey(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}

	public ZahlungsplanDto zahlungsplanFindByPrimaryKey(Integer iId)
			throws ExceptionLP {

		try {
			return auftragServiceFac.zahlungsplanFindByPrimaryKey(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}

	public ZahlungsplanmeilensteinDto zahlungsplanmeilensteinFindByPrimaryKey(
			Integer iId) throws ExceptionLP {

		try {
			return auftragServiceFac
					.zahlungsplanmeilensteinFindByPrimaryKey(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}

	public void toggleZahlungplanmeilensteinErledigt(
			Integer zahlungsplanmeilensteinIId) throws ExceptionLP {

		try {
			auftragServiceFac.toggleZahlungplanmeilensteinErledigt(
					zahlungsplanmeilensteinIId, LPMain.getInstance()
							.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);

		}

	}

	public MeilensteinDto meilensteinFindByPrimaryKey(Integer iId)
			throws ExceptionLP {
		MeilensteinDto meilensteinDto = null;

		try {
			meilensteinDto = auftragServiceFac.meilensteinFindByPrimaryKey(iId,
					LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}

		return meilensteinDto;
	}

	public AuftragdokumentDto auftragdokumentFindByPrimaryKey(Integer iId)
			throws ExceptionLP {
		AuftragdokumentDto auftragdokumentDto = null;

		try {
			auftragdokumentDto = auftragServiceFac
					.auftragdokumentFindByPrimaryKey(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}

		return auftragdokumentDto;
	}

	public AuftragdokumentDto[] auftragdokumentFindByBVersteckt()
			throws ExceptionLP {
		AuftragdokumentDto[] auftragdokumentDto = null;

		try {
			auftragdokumentDto = auftragServiceFac
					.auftragdokumentFindByBVersteckt();
		} catch (Throwable ex) {
			handleThrowable(ex);
		}

		return auftragdokumentDto;
	}

	/**
	 * Den default Kopftext eines Auftrags holen.
	 * 
	 * @param pLocKunde
	 *            die Sprache des Kunden
	 * @return AuftragtextDto der Kopftext
	 * @throws ExceptionLP
	 */
	public AuftragtextDto getAufragkopfDefault(String pLocKunde)
			throws ExceptionLP {
		AuftragtextDto oKoftextDto = null;

		try {
			oKoftextDto = auftragServiceFac.auftragtextFindByMandantLocaleCNr(
					LPMain.getInstance().getTheClient().getMandant(),
					pLocKunde, MediaFac.MEDIAART_KOPFTEXT,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return oKoftextDto;
	}

	/**
	 * Den default Fusstext eines Auftrags holen.
	 * 
	 * @param pLocKunde
	 *            die Sprache des Kunden
	 * @return AuftragtextDto der Fusstext
	 * @throws ExceptionLP
	 */
	public AuftragtextDto getAufragfussDefault(String pLocKunde)
			throws ExceptionLP {
		AuftragtextDto oFusstextDto = null;

		try {
			oFusstextDto = auftragServiceFac.auftragtextFindByMandantLocaleCNr(
					LPMain.getInstance().getTheClient().getMandant(),
					pLocKunde, MediaFac.MEDIAART_FUSSTEXT,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return oFusstextDto;
	}

	public Integer createAuftragtext(AuftragtextDto auftragtextDto)
			throws ExceptionLP {
		Integer iId = null;

		try {
			// fuer StammdatenCRUD : alle Felder, die in der UI nicht vorhanden
			// sind selbst befuellen
			auftragtextDto.setMandantCNr(LPMain.getInstance().getTheClient()
					.getMandant());
			auftragtextDto.setLocaleCNr(LPMain.getInstance().getTheClient()
					.getLocUiAsString());

			iId = auftragServiceFac.createAuftragtext(auftragtextDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}

		return iId;
	}

	public Integer createZeitplan(ZeitplanDto zeitplanDto) throws ExceptionLP {
		Integer iId = null;

		try {

			iId = auftragServiceFac.createZeitplan(zeitplanDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}

		return iId;
	}

	public Integer createZahlungsplan(ZahlungsplanDto zahlungsplanDto)
			throws ExceptionLP {
		Integer iId = null;

		try {

			iId = auftragServiceFac.createZahlungsplan(zahlungsplanDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}

		return iId;
	}

	public Integer createZahlungsplanmeilenstein(
			ZahlungsplanmeilensteinDto zahlungsplanmeilensteinDto)
			throws ExceptionLP {
		Integer iId = null;

		try {

			iId = auftragServiceFac.createZahlungsplanmeilenstein(
					zahlungsplanmeilensteinDto, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}

		return iId;
	}

	public void removeAuftragtext(AuftragtextDto auftragtextDto)
			throws ExceptionLP {
		try {
			auftragServiceFac.removeAuftragtext(auftragtextDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void removeZeitplan(ZeitplanDto zeitplanDto) throws ExceptionLP {
		try {
			auftragServiceFac.removeZeitplan(zeitplanDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void removeZahlungsplan(ZahlungsplanDto zahlungsplanDto)
			throws ExceptionLP {
		try {
			auftragServiceFac.removeZahlungsplan(zahlungsplanDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void removeZahlungsplanmeilenstein(
			ZahlungsplanmeilensteinDto zahlungsplanmeilensteinDto)
			throws ExceptionLP {
		try {
			auftragServiceFac
					.removeZahlungsplanmeilenstein(zahlungsplanmeilensteinDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updateAuftragtext(AuftragtextDto auftragtextDto)
			throws ExceptionLP {
		try {
			auftragServiceFac.updateAuftragtext(auftragtextDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public AuftragtextDto auftragtextFindByMandantLocaleCNr(String sLocaleI,
			String sCNrI) throws ExceptionLP {
		AuftragtextDto auftragtextDto = null;

		try {
			auftragtextDto = auftragServiceFac
					.auftragtextFindByMandantLocaleCNr(LPMain.getInstance()
							.getTheClient().getMandant(), sLocaleI, sCNrI,
							LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}

		return auftragtextDto;
	}

	/**
	 * Alle Auftragarten in bestmoeglicher Uebersetzung von der DB holen.
	 * 
	 * @param pLocKunde
	 *            das Locale des Kunden
	 * @return Map die Auftragarten mit ihrer Uebersetzung
	 * @throws ExceptionLP
	 */
	public Map<?, ?> getAuftragart(Locale pLocKunde) throws ExceptionLP {

		Map<?, ?> arten = null;

		try {
			arten = auftragServiceFac.getAuftragart(pLocKunde, LPMain
					.getInstance().getUISprLocale());
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return arten;
	}

	/**
	 * Alle Auftragwiderholungsintervall bestmoeglicher Uebersetzung von der DB
	 * holen.
	 * 
	 * @param pLocKunde
	 *            das Locale des Kunden
	 * @return Map die Auftragarten mit ihrer Uebersetzung
	 * @throws ExceptionLP
	 */
	public Map<?, ?> getAuftragwiederholungsintervall(Locale pLocKunde)
			throws ExceptionLP {

		Map<?, ?> arten = null;

		try {
			arten = auftragServiceFac
					.getAuftragwiederholungsintervall(pLocKunde);
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return arten;
	}

	public String createAuftragart(AuftragartDto auftragartDtoI)
			throws ExceptionLP {
		String cNr = null;

		try {
			cNr = auftragServiceFac.createAuftragart(auftragartDtoI,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return cNr;
	}

	public Integer createMeilenstein(MeilensteinDto meilensteinDto)
			throws ExceptionLP {
		Integer iId = null;

		try {
			iId = auftragServiceFac.createMeilenstein(meilensteinDto,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return iId;
	}

	public Integer createAuftragdokument(AuftragdokumentDto auftragdokumentDto)
			throws ExceptionLP {
		Integer iId = null;

		try {
			iId = auftragServiceFac.createAuftragdokument(auftragdokumentDto);
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return iId;
	}

	public void removeAuftragart(AuftragartDto auftragartDtoI, String cNrUserI)
			throws ExceptionLP {
		try {
			auftragServiceFac.removeAuftragart(auftragartDtoI,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public void removeAuftragdokument(AuftragdokumentDto auftragdokumentDto)
			throws ExceptionLP {
		try {
			auftragServiceFac.removeAuftragdokument(auftragdokumentDto);
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public void updateAuftragart(AuftragartDto auftragartDtoI)
			throws ExceptionLP {
		try {
			auftragServiceFac.updateAuftragart(auftragartDtoI,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public void updateMeilenstein(MeilensteinDto meilensteinDto)
			throws ExceptionLP {
		try {
			auftragServiceFac.updateMeilenstein(meilensteinDto,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public void updateZeitplan(ZeitplanDto zeitplanDto) throws ExceptionLP {
		try {
			auftragServiceFac.updateZeitplan(zeitplanDto);
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public void updateZahlungsplan(ZahlungsplanDto zahlungsplanDto)
			throws ExceptionLP {
		try {
			auftragServiceFac.updateZahlungsplan(zahlungsplanDto);
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public void updateZahlungsplanmeilenstein(
			ZahlungsplanmeilensteinDto zahlungsplanmeilensteinDto)
			throws ExceptionLP {
		try {
			auftragServiceFac.updateZahlungsplanmeilenstein(
					zahlungsplanmeilensteinDto, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public void updateAuftragdokument(AuftragdokumentDto auftragdokumentDto)
			throws ExceptionLP {
		try {
			auftragServiceFac.updateAuftragdokument(auftragdokumentDto);
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public void updateAuftragdokumente(Integer auftragIId,
			ArrayList<AuftragdokumentDto> dtos) throws ExceptionLP {
		try {
			auftragServiceFac.updateAuftragdokumente(auftragIId, dtos);
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public AuftragartDto auftragartFindByPrimaryKey(String cNrI)
			throws ExceptionLP {
		AuftragartDto auftragartDto = null;

		try {
			auftragartDto = auftragServiceFac.auftragartFindByPrimaryKey(cNrI,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return auftragartDto;
	}

	public AuftragauftragdokumentDto[] auftragauftragdokumentFindByAuftragIId(
			Integer auftragIId) throws ExceptionLP {
		AuftragauftragdokumentDto[] auftragauftragdokumentDtos = null;

		try {
			auftragauftragdokumentDtos = auftragServiceFac
					.auftragauftragdokumentFindByAuftragIId(auftragIId);
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return auftragauftragdokumentDtos;
	}

	public Map<String, String> auftragpositionartFindAll(Locale locKundeI)
			throws ExceptionLP {
		Map<String, String> map = null;
		try {
			map = auftragServiceFac.auftragpositionartFindAll(locKundeI, LPMain
					.getInstance().getUISprLocale(), LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return map;
	}

	public String createAuftragpositionArt(
			AuftragpositionArtDto auftragpositionartDtoI) throws ExceptionLP {
		String auftragpositionartCNr = null;

		try {
			auftragpositionartCNr = auftragServiceFac.createAuftragpositionArt(
					auftragpositionartDtoI, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return auftragpositionartCNr;
	}

	public void updateAuftragpositionArt(
			AuftragpositionArtDto auftragpositionartDtoI) throws ExceptionLP {
		try {
			auftragServiceFac.updateAuftragpositionArt(auftragpositionartDtoI,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public AuftragpositionArtDto auftragpositionartFindByPrimaryKey(
			String cNrAuftragpositionartI) throws ExceptionLP {
		AuftragpositionArtDto auftragpositionartDto = null;

		try {
			auftragpositionartDto = auftragServiceFac
					.auftragpositionartFindByPrimaryKey(cNrAuftragpositionartI,
							LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return auftragpositionartDto;
	}

	/**
	 * Alle verfuegbaren Auftragspositionsarten in bestmoeglicher Uebersetzung
	 * aus der DB holen.
	 * 
	 * @param pLocKunde
	 *            Locale
	 * @return Map
	 * @throws ExceptionLP
	 */
	public Map<?, ?> getAuftragpositionart(Locale pLocKunde) throws ExceptionLP {
		Map<?, ?> posarten = null;

		try {
			posarten = auftragServiceFac.getAuftragpositionart(pLocKunde,
					LPMain.getInstance().getUISprLocale());
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return posarten;
	}

	public AuftragartDto[] auftragartFindAll() throws ExceptionLP {
		AuftragartDto[] toReturn = null;
		try {
			toReturn = auftragServiceFac.auftragartFindAll();
		} catch (Throwable t) {
			handleThrowable(t);
		}
		return toReturn;
	}
}
