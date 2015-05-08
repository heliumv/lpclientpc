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
import java.sql.Timestamp;
import java.util.ArrayList;

import javax.naming.Context;
import javax.naming.InitialContext;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.artikel.service.InventurDto;
import com.lp.server.artikel.service.InventurFac;
import com.lp.server.artikel.service.InventurlisteDto;
import com.lp.server.artikel.service.InventurprotokollDto;
import com.lp.server.artikel.service.InventurstandDto;
import com.lp.server.artikel.service.InvenurlisteImportDto;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.report.JasperPrintLP;
import com.lp.util.EJBExceptionLP;

@SuppressWarnings("static-access")
public class InventurDelegate extends Delegate {
	private Context context;
	private InventurFac inventurFac;

	public InventurDelegate() throws Exception {
		context = new InitialContext();
		inventurFac = (InventurFac) context
				.lookup("lpserver/InventurFacBean/remote");
	}

	public Integer createInventur(InventurDto inventurDto) throws ExceptionLP {
		try {
			return inventurFac.createInventur(inventurDto, LPMain.getInstance()
					.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}

	public Integer createInventurliste(InventurlisteDto inventurlisteDto)
			throws ExceptionLP {
		try {

			return inventurFac.createInventurliste(inventurlisteDto, true,
					LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			if (ex instanceof EJBExceptionLP) {
				if (((EJBExceptionLP) ex).getCode() == EJBExceptionLP.FEHLER_INVENTUR_MENGE_ZU_GROSS) {
					ArrayList al = ((EJBExceptionLP) ex)
							.getAlInfoForTheClient();

					String meldung = "Die Inventurmenge weicht um mehr als ";

					if (al != null && al.size() > 0) {
						meldung += al.get(0)
								+ " St\u00FCck vom Lagerstand ab. ";
					}
					meldung += "Trotzdem buchen?";

					boolean b = DialogFactory.showModalJaNeinDialog(null,
							meldung);

					if (b == true) {
						try {
							return inventurFac.createInventurliste(
									inventurlisteDto, false, LPMain
											.getInstance().getTheClient());
						} catch (Throwable ex2) {
							handleThrowable(ex2);
							return null;
						}
					}

					return null;
				} else {
					handleThrowable(ex);
					return null;
				}
			}
			handleThrowable(ex);
			return null;
		}

	}

	public void inventurlisteErfassenMitScanner(
			InventurlisteDto inventurlisteDto, boolean bKorrekturbuchung)
			throws ExceptionLP {

		try {

			inventurFac.inventurlisteErfassenMitScanner(inventurlisteDto,
					bKorrekturbuchung, true, LPMain.getInstance()
							.getTheClient());
		} catch (Throwable ex) {
			if (ex instanceof EJBExceptionLP) {
				if (((EJBExceptionLP) ex).getCode() == EJBExceptionLP.FEHLER_INVENTUR_MENGE_ZU_GROSS) {
					ArrayList al = ((EJBExceptionLP) ex)
							.getAlInfoForTheClient();

					String meldung = "Die Inventurmenge weicht um mehr als ";

					if (al != null && al.size() > 0) {
						meldung += al.get(0)
								+ " St\u00FCck vom Lagerstand ab. ";
					}
					meldung += "Trotzdem buchen?";

					boolean b = DialogFactory.showModalJaNeinDialog(null,
							meldung);

					if (b == true) {
						try {
							inventurFac.inventurlisteErfassenMitScanner(
									inventurlisteDto, bKorrekturbuchung, false,
									LPMain.getInstance().getTheClient());
						} catch (Throwable ex2) {
							handleThrowable(ex2);
						}
					}
				}
			} else {
				handleThrowable(ex);
			}

		}

	}

	public void removeInventur(InventurDto inventurDto) throws ExceptionLP {
		try {
			inventurFac.removeInventur(inventurDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void removeInventurliste(InventurlisteDto inventurlisteDto)
			throws ExceptionLP {
		try {
			inventurFac.removeInventurliste(inventurlisteDto, LPMain
					.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void inventurDurchfuehren(Integer inventurIId,
			boolean bNichtInventierteArtikelAufNullSetzen) throws ExceptionLP {
		try {
			inventurFac.inventurDurchfuehren(inventurIId,
					bNichtInventierteArtikelAufNullSetzen, LPMain.getInstance()
							.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void preiseAbwerten(Integer inventurIId, boolean bMitStuecklisten)
			throws ExceptionLP {
		try {
			inventurFac.preiseAbwerten(inventurIId, bMitStuecklisten, LPMain
					.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void inventurDurchfuehrungZuruecknehmen(Integer inventurIId)
			throws ExceptionLP {
		try {
			inventurFac.inventurDurchfuehrungZuruecknehmen(inventurIId, LPMain
					.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void invturprotokollZumStichtagZuruecknehmen(Integer inventurIId,
			java.sql.Date tAbStichtag) throws ExceptionLP {
		try {
			inventurFac.invturprotokollZumStichtagZuruecknehmen(inventurIId,
					tAbStichtag, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void inventurpreiseAktualisieren(Integer inventurIId,
			boolean bAufGestpreisZumInventurdatumAktualisieren)
			throws ExceptionLP {
		try {
			inventurFac.inventurpreiseAktualisieren(inventurIId,
					bAufGestpreisZumInventurdatumAktualisieren, LPMain
							.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void inventurpreiseAufEkPreisSetzen(Integer inventurIId)
			throws ExceptionLP {
		try {
			inventurFac.inventurpreiseAufEkPreisSetzen(inventurIId, LPMain
					.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void inventurlisteFuerSerienChargennummerAbschliessen(
			Integer inventurIId, Integer artikelIId, Integer lagerIId)
			throws ExceptionLP {
		try {
			inventurFac.inventurlisteFuerSerienChargennummerAbschliessen(
					inventurIId, artikelIId, lagerIId, LPMain.getInstance()
							.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updateInventur(InventurDto inventurDto) throws ExceptionLP {
		try {
			inventurFac.updateInventur(inventurDto, LPMain.getInstance()
					.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}

	}

	public Integer updateInventurliste(InventurlisteDto inventurlisteDto)
			throws ExceptionLP {

		try {

			return inventurFac.updateInventurliste(inventurlisteDto, true,
					LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			if (ex instanceof EJBExceptionLP) {
				if (((EJBExceptionLP) ex).getCode() == EJBExceptionLP.FEHLER_INVENTUR_MENGE_ZU_GROSS) {
					ArrayList al = ((EJBExceptionLP) ex)
							.getAlInfoForTheClient();

					String meldung = "Die Inventurmenge weicht um mehr als ";

					if (al != null && al.size() > 0) {
						meldung += al.get(0)
								+ " St\u00FCck vom Lagerstand ab. ";
					}
					meldung += "Trotzdem buchen?";

					boolean b = DialogFactory.showModalJaNeinDialog(null,
							meldung);

					if (b == true) {
						try {
							return inventurFac.updateInventurliste(
									inventurlisteDto, false, LPMain
											.getInstance().getTheClient());
						} catch (Throwable ex2) {
							handleThrowable(ex2);
							return null;
						}
					}

					return null;
				} else {
					handleThrowable(ex);
					return null;
				}
			}
			handleThrowable(ex);
			return null;
		}

	}

	public void updateInventurstand(InventurstandDto inventurstandDto)
			throws ExceptionLP {
		try {
			inventurFac.updateInventurstand(inventurstandDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}

	}

	public InventurDto inventurFindByPrimaryKey(Integer iId) throws ExceptionLP {
		try {
			return inventurFac.inventurFindByPrimaryKey(iId, LPMain
					.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public InventurstandDto inventurstandFindByPrimaryKey(Integer iId)
			throws ExceptionLP {
		try {
			return inventurFac.inventurstandFindByPrimaryKey(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public InventurlisteDto inventurlisteFindByPrimaryKey(Integer iId)
			throws ExceptionLP {
		try {
			return inventurFac.inventurlisteFindByPrimaryKey(iId, LPMain
					.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public InventurlisteDto inventurlisteFindByInventurIIdLagerIIdArtikelIIdCSeriennrchargennr(
			Integer inventurIId, Integer artikelIId, Integer lagerIId,
			String cSerienchargennr) throws ExceptionLP {
		try {
			return inventurFac
					.inventurlisteFindByInventurIIdLagerIIdArtikelIIdCSeriennrchargennr(
							inventurIId, artikelIId, lagerIId,
							cSerienchargennr, LPMain.getInstance()
									.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public InventurlisteDto[] inventurlisteFindByInventurIIdLagerIIdArtikelIId(
			Integer inventurIId, Integer artikelIId, Integer lagerIId)
			throws ExceptionLP {
		try {
			return inventurFac
					.inventurlisteFindByInventurIIdLagerIIdArtikelIId(
							inventurIId, lagerIId, artikelIId, LPMain
									.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public JasperPrintLP printInventurliste(Integer inventurIId,
			Integer lagerIId, boolean bInventurpreis, int iSortierung,Timestamp dVon, Timestamp dBis)
			throws Throwable {
		try {
			return inventurFac.printInventurliste(inventurIId, lagerIId,
					bInventurpreis, iSortierung,dVon,dBis, LPMain.getInstance()
							.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public JasperPrintLP printInventurstand(Integer inventurIId,
			Integer lagerIId, int iSortierung) throws Throwable {
		try {
			return inventurFac.printInventurstand(inventurIId, lagerIId,
					iSortierung, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public JasperPrintLP printNichterfassteartikel(Integer inventurIId,
			Integer lagerIId, boolean bNurArtikelMitLagerstand,
			boolean bSortiertNachLagerplatz, String lagerplatzVon,
			String lagerplatzBis,boolean bMitVersteckten) throws Throwable {
		try {
			return inventurFac.printNichterfassteartikel(inventurIId, lagerIId,
					bNurArtikelMitLagerstand, bSortiertNachLagerplatz,
					lagerplatzVon, lagerplatzBis, bMitVersteckten, LPMain.getInstance()
							.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public BigDecimal getInventurstand(Integer artikelIId, Integer lagerIId,
			Integer inventurIId, java.sql.Timestamp tDatum) throws Throwable {
		try {
			return inventurFac.getInventurstand(artikelIId, lagerIId,
					inventurIId, tDatum, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public JasperPrintLP printInventurprotokoll(Integer inventurIId,
			Integer lagerIId, boolean bSortiertNachLagerplatz,
			String lagerplatzVon, String lagerplatzBis) throws Throwable {
		try {
			return inventurFac.printInventurprotokoll(inventurIId, lagerIId,
					bSortiertNachLagerplatz, lagerplatzVon, lagerplatzBis,
					LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public String importiereInventurliste(Integer inventurIId,
			ArrayList<InvenurlisteImportDto> alImportdaten) throws Throwable {
		try {
			return inventurFac.importiereInventurliste(inventurIId,
					alImportdaten, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public InventurprotokollDto inventurprotokollFindByPrimaryKey(Integer iId)
			throws ExceptionLP {
		try {
			return inventurFac.inventurprotokollFindByPrimaryKey(iId, LPMain
					.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public InventurDto inventurFindByTInventurdatum(Timestamp tInventurdatum)
			throws Throwable {
		return inventurFac.findByTInventurdatumMandantCNr(tInventurdatum,
				LPMain.getInstance().getTheClient().getMandant());
	}

	public ArrayList<String> sindSeriennumernBereitsInventiert(
			InventurlisteDto inventurlisteDto, String[] snrs) throws Throwable {
		return inventurFac.sindSeriennumernBereitsInventiert(inventurlisteDto,
				snrs, LPMain.getInstance().getTheClient());
	}

	public void mehrereSeriennumernInventieren(
			InventurlisteDto inventurlisteDto, String[] snrs) throws Throwable {
		inventurFac.mehrereSeriennumernInventieren(inventurlisteDto, snrs,
				LPMain.getInstance().getTheClient());
	}

	public InventurDto[] inventurFindInventurenNachDatum(
			Timestamp tInventurdatum) throws Throwable {
		return inventurFac.inventurFindInventurenNachDatum(tInventurdatum,
				LPMain.getInstance().getTheClient().getMandant());
	}

}
