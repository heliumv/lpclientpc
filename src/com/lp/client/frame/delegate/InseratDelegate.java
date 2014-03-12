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

import java.math.BigDecimal;
import java.util.ArrayList;

import javax.naming.Context;
import javax.naming.InitialContext;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.pc.LPMain;
import com.lp.server.inserat.service.InseratDto;
import com.lp.server.inserat.service.InseratFac;
import com.lp.server.inserat.service.InseratReportFac;
import com.lp.server.inserat.service.InseratartikelDto;
import com.lp.server.inserat.service.InseraterDto;
import com.lp.server.inserat.service.InseratrechnungDto;
import com.lp.server.inserat.service.ReportJournalInseratDto;
import com.lp.server.rechnung.service.RechnungDto;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.report.JasperPrintLP;
import com.lp.util.EJBExceptionLP;

@SuppressWarnings("static-access")
public class InseratDelegate extends Delegate {
	private Context context;
	private InseratFac inseratFac;
	private InseratReportFac inseratReportFac;

	public InseratDelegate() throws ExceptionLP {
		try {
			context = new InitialContext();
			inseratFac = (InseratFac) context
					.lookup("lpserver/InseratFacBean/remote");
			inseratReportFac = (InseratReportFac) context
					.lookup("lpserver/InseratReportFacBean/remote");
		} catch (Throwable t) {
			throw new ExceptionLP(EJBExceptionLP.FEHLER, t);
		}

	}

	public Integer createInserat(InseratDto inseratDto) throws ExceptionLP {
		try {
			return inseratFac.createInserat(inseratDto, LPMain.getInstance()
					.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}



	public JasperPrintLP printZuverrechnen() throws ExceptionLP {
		try {
			return inseratReportFac.printZuverrechnen(LPMain.getInstance()
					.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public JasperPrintLP printAlle(ReportJournalInseratDto krit)
			throws ExceptionLP {
		try {
			return inseratReportFac.printAlle(krit, LPMain.getInstance()
					.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public JasperPrintLP printDBAuswertung(ReportJournalInseratDto krit,
			int iOptionDatum) throws ExceptionLP {
		try {
			return inseratReportFac.printDBAuswertung(krit, iOptionDatum,
					LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public JasperPrintLP printOffene(ReportJournalInseratDto krit)
			throws ExceptionLP {
		try {
			return inseratReportFac.printOffene(krit, LPMain.getInstance()
					.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public JasperPrintLP[] printInserat(Integer inseratIId, Boolean bMitLogo,
			Integer iAnzahlKopien) throws ExceptionLP {
		try {
			return inseratReportFac.printInserat(inseratIId, bMitLogo,
					iAnzahlKopien, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public ArrayList<Integer> getAllLieferantIIdsAusInseratenOhneBestellung(
			Integer kundeIId) throws ExceptionLP {
		try {
			return inseratFac.getAllLieferantIIdsAusInseratenOhneBestellung(
					kundeIId, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public ArrayList<Integer> getAllKundeIIdsAusInseratenOhneBestellung()
			throws ExceptionLP {
		try {
			return inseratFac.getAllKundeIIdsAusInseratenOhneBestellung(LPMain
					.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public ArrayList<Integer> getAllLieferantIIdsAusOffenenInseraten()
			throws ExceptionLP {
		try {
			return inseratFac.getAllLieferantIIdsAusOffenenInseraten(LPMain
					.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public BigDecimal berechneWerbeabgabeLFEinesInserates(Integer inseratIId)
			throws ExceptionLP {
		try {
			return inseratFac.berechneWerbeabgabeLFEinesInserates(inseratIId,
					LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public ArrayList<RechnungDto> gibtEsNochWeitereRechnungenFuerdiesesInserat(
			Integer inseratIId) throws ExceptionLP {
		try {
			return inseratFac.gibtEsNochWeitereRechnungenFuerdiesesInserat(
					inseratIId, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public ArrayList<Integer> eingangsrechnungsIIdsEinesInserates(
			Integer inseratIId) throws ExceptionLP {
		try {
			return inseratFac.eingangsrechnungsIIdsEinesInserates(inseratIId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Integer createInseratrechnung(InseratrechnungDto inseratrechnungDto)
			throws ExceptionLP {
		try {
			return inseratFac.createInseratrechnung(inseratrechnungDto, LPMain
					.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Integer createInserater(InseraterDto inseraterDto)
			throws ExceptionLP {
		try {
			return inseratFac.createInserater(inseraterDto, LPMain
					.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Integer createInseratartikel(InseratartikelDto inseratartikelDto)
			throws ExceptionLP {
		try {
			return inseratFac.createInseratartikel(inseratartikelDto, LPMain
					.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public void updateInserat(InseratDto inseratDto) throws ExceptionLP {
		try {
			inseratFac.updateInserat(inseratDto, LPMain.getInstance()
					.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);

		}
	}

	public void toggleErschienen(Integer inseratIId) throws ExceptionLP {
		try {
			inseratFac.toggleErschienen(inseratIId, LPMain.getInstance()
					.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);

		}
	}

	public void toggleGestoppt(Integer inseratIId, String cBegruendung)
			throws ExceptionLP {
		try {
			inseratFac.toggleGestoppt(inseratIId, cBegruendung, LPMain
					.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);

		}
	}

	public boolean istInseratInEinerRechnungEnthalten(Integer rechnungIId) {
		return inseratFac.istInseratInEinerRechnungEnthalten(rechnungIId);
	}

	public void toggleManuellerledigt(Integer inseratIId) throws ExceptionLP {
		try {
			inseratFac.toggleManuellerledigt(inseratIId, LPMain.getInstance()
					.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);

		}
	}

	public void toggleVerrechenbar(Integer inseratIId) throws ExceptionLP {
		try {
			inseratFac.toggleVerrechenbar(inseratIId, LPMain.getInstance()
					.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);

		}
	}

	public void updateInseratrechnung(InseratrechnungDto inseratrechnungDto)
			throws ExceptionLP {
		try {
			inseratFac.updateInseratrechnung(inseratrechnungDto, LPMain
					.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);

		}
	}

	public void updateInserater(InseraterDto inseraterDto) throws ExceptionLP {
		try {
			inseratFac.updateInserater(inseraterDto, LPMain.getInstance()
					.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);

		}
	}

	public void updateInseratartikel(InseratartikelDto inseratartikelDto)
			throws ExceptionLP {
		try {
			inseratFac.updateInseratartikel(inseratartikelDto, LPMain
					.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);

		}
	}

	public void vertauscheInseratrechnung(Integer inseratrechnungIId1,
			Integer inseratrechnungIId2) throws ExceptionLP {
		try {
			inseratFac.vertauscheInseratrechnung(inseratrechnungIId1,
					inseratrechnungIId2);
		} catch (Throwable ex) {
			handleThrowable(ex);

		}
	}

	public void storniereInserat(Integer inseratIId) throws ExceptionLP {
		try {
			inseratFac.storniereInserat(inseratIId, LPMain.getInstance()
					.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);

		}
	}

	public InseratDto inseratFindByPrimaryKey(Integer inseratIId)
			throws ExceptionLP {
		try {
			return inseratFac.inseratFindByPrimaryKey(inseratIId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public InseraterDto inseraterFindByPrimaryKey(Integer inseraterIId)
			throws ExceptionLP {
		try {
			return inseratFac.inseraterFindByPrimaryKey(inseraterIId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public InseratDto istIseratAufRechnungspositionVorhanden(
			Integer rechnungspositionIId) throws ExceptionLP {
		try {
			return inseratFac
					.istIseratAufRechnungspositionVorhanden(rechnungspositionIId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public InseratDto istIseratAufBestellpositionVorhanden(
			Integer bestellpositionIId) throws ExceptionLP {
		try {
			return inseratFac
					.istInseratAufBestellpositionVorhanden(bestellpositionIId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public void beziehungZuRechnungspositionAufloesenUndRechnungspositionenLoeschen(
			Integer rechnungspositionIId) throws ExceptionLP {
		try {
			inseratFac
					.beziehungZuRechnungspositionAufloesenUndRechnungspositionenLoeschen(
							rechnungspositionIId, LPMain.getInstance()
									.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);

		}
	}

	public void beziehungZuBestellpositionAufloesenUndBestellpositionenLoeschen(
			Integer bestellpositionIId) throws ExceptionLP {
		try {
			inseratFac
					.beziehungZuBestellpositionAufloesenUndBestellpositionenLoeschen(
							bestellpositionIId, LPMain.getInstance()
									.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);

		}
	}

	public ArrayList<Integer> bestellungenAusloesen(Integer lieferantIId,
			Integer kundeIId) throws ExceptionLP {
		try {
			return inseratFac.bestellungenAusloesen(lieferantIId, kundeIId,
					LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;

		}
	}

	public BigDecimal getZuEingangsrechnungenZugeordnetenWert(Integer inseratIId)
			throws ExceptionLP {
		try {
			return inseratFac.getZuEingangsrechnungenZugeordnetenWert(
					inseratIId, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;

		}
	}

	public int rechnungenAusloesen(Integer kundeIId, Integer inseratIId,
			java.sql.Date neuDatum) throws ExceptionLP {
		try {
			return inseratFac.rechnungenAusloesen(kundeIId, inseratIId,
					neuDatum, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return 0;
		}
	}

	public void eingangsrechnungZuordnen(Integer inseratIId,
			Integer eingangsrechnungIId, BigDecimal nBetrag) throws ExceptionLP {
		try {
			inseratFac.eingangsrechnungZuordnen(inseratIId,
					eingangsrechnungIId, nBetrag, LPMain.getInstance()
							.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);

		}
	}

	public void removeInseratrechnung(Integer inseratrechnungIId)
			throws ExceptionLP {
		try {
			inseratFac.removeInseratrechnung(inseratrechnungIId);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void removeInserater(Integer inseraterIId) throws ExceptionLP {
		try {
			inseratFac.removeInserater(inseraterIId, LPMain.getInstance()
					.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void removeInseratartikel(Integer inseratartikelIId)
			throws ExceptionLP {
		try {
			inseratFac.removeInseratartikel(inseratartikelIId);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void inseratVertreterAendern(Integer inseratIId,
			Integer personalId_Vertreter) throws ExceptionLP {
		try {
			inseratFac.inseratVertreterAendern(inseratIId,
					personalId_Vertreter, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void storniertAufheben(Integer inseratIId) throws ExceptionLP {
		try {
			inseratFac.storniertAufheben(inseratIId, LPMain.getInstance()
					.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public InseratrechnungDto inseratrechnungFindByPrimaryKey(Integer inseratIId)
			throws ExceptionLP {
		try {
			return inseratFac.inseratrechnungFindByPrimaryKey(inseratIId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public InseratartikelDto inseratartikelFindByPrimaryKey(Integer iId)
			throws ExceptionLP {
		try {
			return inseratFac.inseratartikelFindByPrimaryKey(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public InseraterDto[] inseraterFindByEingangsrechnungIId(
			Integer eingansrechnungIId) throws ExceptionLP {
		try {
			return inseratFac
					.inseraterFindByEingangsrechnungIId(eingansrechnungIId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public InseraterDto[] inseraterFindByInseratIId(Integer eingansrechnungIId)
			throws ExceptionLP {
		try {
			return inseratFac.inseraterFindByInseratIId(eingansrechnungIId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public InseratartikelDto[] inseratartikelFindByInseratIId(Integer inseratIId)
			throws ExceptionLP {
		try {
			return inseratFac.inseratartikelFindByInseratIId(inseratIId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

}
