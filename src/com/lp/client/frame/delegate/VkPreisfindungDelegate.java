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
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.pc.LPMain;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.VerkaufspreisDto;
import com.lp.server.artikel.service.VkPreisfindungEinzelverkaufspreisDto;
import com.lp.server.artikel.service.VkPreisfindungFac;
import com.lp.server.artikel.service.VkPreisfindungPreislisteDto;
import com.lp.server.artikel.service.VkpfMengenstaffelDto;
import com.lp.server.artikel.service.VkpfartikelpreislisteDto;
import com.lp.server.artikel.service.VkpreisfindungDto;
import com.lp.server.partner.service.KundesokomengenstaffelDto;
import com.lp.server.system.service.MwstsatzDto;
import com.lp.server.system.service.TheClientDto;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

/**
 * <p>
 * Delegate fuer Kundensonderkonditionen.
 * </p>
 * 
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2008
 * </p>
 * 
 * <p>
 * Erstellung: Uli Walch, 21. 04. 2005
 * </p>
 * 
 * <p>
 * 
 * @author Uli Walch
 *         </p>
 * 
 * @version 1.0
 * 
 * @version $Revision: 1.17 $ Date $Date: 2012/04/19 13:03:23 $
 */
public class VkPreisfindungDelegate extends Delegate {
	private VkPreisfindungFac vkPreisfindungFac;
	private Context context;

	public VkPreisfindungDelegate() throws ExceptionLP {

		try {
			context = new InitialContext();
			vkPreisfindungFac = (VkPreisfindungFac) context
					.lookup("lpserver/VkPreisfindungFacBean/remote");
		} catch (Throwable t) {
			throw new ExceptionLP(EJBExceptionLP.FEHLER, t);
		}
	}

	public Integer createVkPreisfindungPreisliste(
			VkPreisfindungPreislisteDto vkPreisfindungPreislisteDto)
			throws ExceptionLP {
		Integer iIdPreislisteO = null;

		try {
			iIdPreislisteO = vkPreisfindungFac.createVkPreisfindungPreisliste(
					vkPreisfindungPreislisteDto, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}

		return iIdPreislisteO;
	}

	public void removeVkPreisfindungPreisliste(Integer iId) throws ExceptionLP {
		try {
			vkPreisfindungFac.removeVkPreisfindungPreisliste(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void removeVkPreisfindungPreisliste(
			VkPreisfindungPreislisteDto vkPreisfindungPreislisteDto)
			throws ExceptionLP {
		try {
			vkPreisfindungFac
					.removeVkPreisfindungPreisliste(vkPreisfindungPreislisteDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void pflegeRabattsaetzeNachpflegen(Integer preislisteIId,
			Date tPreisgueltigab) throws ExceptionLP {
		try {
			vkPreisfindungFac.pflegeRabattsaetzeNachpflegen(preislisteIId,
					tPreisgueltigab, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updateVkPreisfindungPreisliste(
			VkPreisfindungPreislisteDto vkPreisfindungPreislisteDto)
			throws ExceptionLP {
		try {
			vkPreisfindungFac.updateVkPreisfindungPreisliste(
					vkPreisfindungPreislisteDto, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	/**
	 * Anlegen einer neuen Artikelpreisliste.
	 * 
	 * @param vkpfartikelpreislisteDto
	 *            die Preisliste
	 * @return Integer PK der neuen Preisliste
	 * @throws ExceptionLP
	 *             Ausnahme
	 */
	public Integer createVkpfartikelpreisliste(
			VkpfartikelpreislisteDto vkpfartikelpreislisteDto)
			throws ExceptionLP {
		Integer iIdPreislisteO = null;

		try {
			iIdPreislisteO = vkPreisfindungFac.createVkpfartikelpreisliste(
					vkpfartikelpreislisteDto, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}

		return iIdPreislisteO;
	}

	/**
	 * Loeschen einer Preisliste.
	 * 
	 * @param vkpfartikelpreislisteDto
	 *            die Preisliste
	 * @throws ExceptionLP
	 *             Ausnahme
	 */
	public void removeVkpfartikelpreisliste(
			VkpfartikelpreislisteDto vkpfartikelpreislisteDto)
			throws ExceptionLP {
		try {
			vkPreisfindungFac.removeVkpfartikelpreisliste(
					vkpfartikelpreislisteDto, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	/**
	 * Aktualisieren einer bestehenden Preisliste
	 * 
	 * @param vkpfartikelpreislisteDto
	 *            die Preisliste
	 * @throws ExceptionLP
	 *             Ausnahme
	 */
	public void updateVkpfartikelpreisliste(
			VkpfartikelpreislisteDto vkpfartikelpreislisteDto,
			boolean bRabattsatzAendern, java.sql.Date datumGueltigNeu)
			throws ExceptionLP {
		try {
			vkPreisfindungFac.updateVkpfartikelpreisliste(
					vkpfartikelpreislisteDto, bRabattsatzAendern,
					datumGueltigNeu, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public VkpfartikelpreislisteDto vkpfartikelpreislisteFindByPrimaryKey(
			Integer iId) throws ExceptionLP {
		VkpfartikelpreislisteDto oDtoO = null;

		try {
			oDtoO = vkPreisfindungFac
					.vkpfartikelpreislisteFindByPrimaryKey(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}

		return oDtoO;
	}

	/**
	 * Preisliste mit aktuellem Gueltigkeitsdatum suchen.
	 * 
	 * @param pKey
	 *            PK der Preisliste
	 * @return VkPreisfindungPreislisteDto
	 * @throws ExceptionLP
	 */
	public VkPreisfindungPreislisteDto getGueltigePreislisteAktuell(Integer pKey)
			throws ExceptionLP {
		VkPreisfindungPreislisteDto dto = null;

		try {
			dto = this.vkPreisfindungFac
					.getAktuellePreislisteByPreislistenname(pKey);
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return dto;
	}

	public Integer createVkPreisfindungEinzelverkaufspreis(
			VkPreisfindungEinzelverkaufspreisDto vkPreisfindungEinzelverkaufspreisDto)
			throws ExceptionLP {
		Integer iIdPreisO = null;

		try {
			iIdPreisO = vkPreisfindungFac
					.createVkPreisfindungEinzelverkaufspreis(
							vkPreisfindungEinzelverkaufspreisDto,
							LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return iIdPreisO;
	}

	public void removeVkPreisfindungEinzelverkaufspreis(
			VkPreisfindungEinzelverkaufspreisDto vkPreisfindungEinzelverkaufspreisDto)
			throws ExceptionLP {
		try {
			vkPreisfindungFac
					.removeVkPreisfindungEinzelverkaufspreis(
							vkPreisfindungEinzelverkaufspreisDto,
							LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public void updateVkPreisfindungEinzelverkaufspreis(
			VkPreisfindungEinzelverkaufspreisDto vkPreisfindungEinzelverkaufspreisDto)
			throws ExceptionLP {
		try {
			vkPreisfindungFac
					.updateVkPreisfindungEinzelverkaufspreis(
							vkPreisfindungEinzelverkaufspreisDto,
							LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public VkPreisfindungEinzelverkaufspreisDto einzelverkaufspreisFindByPrimaryKey(
			Integer iId) throws ExceptionLP {
		VkPreisfindungEinzelverkaufspreisDto oPreisDtoO = null;

		try {
			oPreisDtoO = vkPreisfindungFac.einzelverkaufspreisFindByPrimaryKey(
					iId, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return oPreisDtoO;
	}

	public VkPreisfindungEinzelverkaufspreisDto[] einzelverkaufspreisFindByArtikelPrimaryKey(
			Integer pkArtikel) throws ExceptionLP {
		VkPreisfindungEinzelverkaufspreisDto[] aDtosO = null;

		try {
			aDtosO = vkPreisfindungFac
					.einzelverkaufspreisFindByMandantCNrArtikelIId(pkArtikel,
							LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return aDtosO;
	}

	public VkPreisfindungEinzelverkaufspreisDto einzelverkaufspreisFindByUniqueyKey(
			Integer iiArtikelI, Date datGueltigabI) throws ExceptionLP {
		VkPreisfindungEinzelverkaufspreisDto oPreisDtoO = null;

		try {
			oPreisDtoO = vkPreisfindungFac
					.einzelverkaufspreisFindByMandantCNrArtikelIIdGueltigab(
							iiArtikelI, datGueltigabI, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return oPreisDtoO;
	}

	/**
	 * Den Einzelverkaufpreis fuer einen Artikel holen, der zu einem bestimmten
	 * Datum gueltig ist.
	 * 
	 * @param iIdArtikelI
	 *            PK des Artikels
	 * @param datGueltikeitsdatumI
	 *            Gueltigkeitsdatum, wenn null wird das aktuelle Datum verwendet
	 * @throws ExceptionLP
	 * @return VkPreisfindungEinzelverkaufspreisDto der Einzelverkaufspreis
	 */
	public VkPreisfindungEinzelverkaufspreisDto getArtikeleinzelverkaufspreis(
			Integer iIdArtikelI, Date datGueltikeitsdatumI,
			String waehrungCNrZielwaehrung) throws ExceptionLP {
		VkPreisfindungEinzelverkaufspreisDto oVkPreisfindungEinzelverkaufspreisDto = null;

		try {
			myLogger.error(
					"ART>VK-Preise getArtikeleinzelverkaufspreis delegate before: "
							+ (System.currentTimeMillis() - Defaults.lUhrQuickDirtyART),
					null);
			Defaults.lUhrQuickDirtyART = System.currentTimeMillis();

			oVkPreisfindungEinzelverkaufspreisDto = vkPreisfindungFac
					.getArtikeleinzelverkaufspreis(iIdArtikelI,
							datGueltikeitsdatumI, waehrungCNrZielwaehrung,
							LPMain.getTheClient());

			myLogger.error(
					"ART>VK-Preise getArtikeleinzelverkaufspreis delegate after: "
							+ (System.currentTimeMillis() - Defaults.lUhrQuickDirtyART),
					null);
			Defaults.lUhrQuickDirtyART = System.currentTimeMillis();
			myLogger.error(
					"ART>VK-Preise getArtikeleinzelverkaufspreis delegate aftertime: "
							+ System.currentTimeMillis(), null);
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return oVkPreisfindungEinzelverkaufspreisDto;
	}

	public VkPreisfindungEinzelverkaufspreisDto getNeuestenArtikeleinzelverkaufspreis(
			Integer iiArtikelI) throws ExceptionLP {
		VkPreisfindungEinzelverkaufspreisDto dto = null;

		try {
			dto = this.vkPreisfindungFac.getNeuestenArtikeleinzelverkaufspreis(
					iiArtikelI, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return dto;
	}

	public VkPreisfindungPreislisteDto getNeuestePreislisteByArtikelPreislistenname(
			Integer iiArtikelI, Integer iiNameI) throws ExceptionLP {
		VkPreisfindungPreislisteDto dto = null;

		try {
			dto = this.vkPreisfindungFac
					.getNeuestePreislisteByArtikelPreislistenname(iiArtikelI,
							iiNameI);
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return dto;
	}

	/**
	 * Holen der aktuell gueltigen Preisliste zu einem bestimmten Artikel. Gibt
	 * null zurueck, wenn der Artikel in keiner Preisliste enthalten ist.
	 * 
	 * @param iIdArtikelI
	 *            PK des Artikels
	 * @param iIdVkpfartikelpreislisteI
	 *            PK der Preisliste
	 * @param datGueltigabI
	 *            gewuenschtes Guetligkeitsdatum
	 * @throws ExceptionLP
	 * @return VkPreisfindungPreislisteDto
	 */
	public VkPreisfindungPreislisteDto getAktuellePreislisteByArtikelIIdPreislisteIId(
			Integer iIdArtikelI, Integer iIdVkpfartikelpreislisteI,
			Date datGueltigabI, String waehrungCNrZielwaehrung)
			throws ExceptionLP {

		VkPreisfindungPreislisteDto dto = null;

		try {
			myLogger.error(
					"ART>VK-Preise getAktuellePreislisteByArtikelIIdPreislisteIId delegate before: "
							+ (System.currentTimeMillis() - Defaults.lUhrQuickDirtyART),
					null);
			Defaults.lUhrQuickDirtyART = System.currentTimeMillis();

			dto = this.vkPreisfindungFac
					.getAktuellePreislisteByArtikelIIdPreislisteIId(
							iIdArtikelI, iIdVkpfartikelpreislisteI,
							datGueltigabI, waehrungCNrZielwaehrung,
							LPMain.getTheClient());

			myLogger.error(
					"ART>VK-Preise getAktuellePreislisteByArtikelIIdPreislisteIIdv delegate after: "
							+ (System.currentTimeMillis() - Defaults.lUhrQuickDirtyART),
					null);
			Defaults.lUhrQuickDirtyART = System.currentTimeMillis();
			myLogger.error(
					"ART>VK-Preise getAktuellePreislisteByArtikelIIdPreislisteIId delegate aftertime: "
							+ System.currentTimeMillis(), null);
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return dto;
	}

	/**
	 * Aufruf der Helium V Verkaufspreisfindung. Ueber einen Algorithmus wird
	 * der Vorschlagswert fuer den Verkaufspreis eines Artikels bestimmt. Die
	 * VKPF besteht aus drei Stufen, das Ergebnis aller drei Stufen wird
	 * bestimmt, da der Benutzer alternativ einen VK-Preis manuell waehlen kann.
	 * 
	 * @param iIdArtikelI
	 *            PK des Artikels
	 * @param iIdKundeI
	 *            PK des Kunden, fuer den ein Preis ermittelt wird
	 * @param nMengeI
	 *            welche Menge des Artikls wird verkauft
	 * @param datGueltigkeitsdatumI
	 *            zu welchem Datum soll der VKP ermittelt werden
	 * @param iIdPreislisteI
	 *            PK der Preisliste, die der Benutzer gewaehlt hat
	 * @param iIdMwstsatzI
	 *            PK der Mwst, die der Benutzer gewaehlt hat
	 * @return VkpreisfindungDto die ermittelten VK Informationen
	 * @throws ExceptionLP
	 *             Ausnahme
	 */
	public VkpreisfindungDto verkaufspreisfindung(Integer iIdArtikelI,
			Integer iIdKundeI, BigDecimal nMengeI, Date datGueltigkeitsdatumI,
			Integer iIdPreislisteI, Integer iIdMwstsatzI,
			String waehrungCNrZielwaehrung) throws ExceptionLP {
		VkpreisfindungDto vkpreisfindungDto = null;

		try {
			vkpreisfindungDto = vkPreisfindungFac.verkaufspreisfindung(
					iIdArtikelI, iIdKundeI, nMengeI, datGueltigkeitsdatumI,
					iIdPreislisteI, iIdMwstsatzI, waehrungCNrZielwaehrung,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return vkpreisfindungDto;
	}

	/**
	 * Ausgehende vom Verkaufspreis in Mandantenwaehrung wird ueber den
	 * Umrechnungsfaktor der Verkaufspreis in Fremdwaehrung bestimmt.
	 * 
	 * @param pPreisdetailsInMandantenwaehrung
	 *            in Mandantenwaehrung VerkaufspreisDto
	 * @param pKurs
	 *            fuer Umrechnung Mandantenwaherung nach Fremdwaehrung
	 *            BigDecimal
	 * @return VerkaufspreisDto in Fremdwaehrung
	 * @throws ExceptionLP
	 */
	public VerkaufspreisDto getPreisdetailsInFremdwaehrung(
			VerkaufspreisDto pPreisdetailsInMandantenwaehrung, BigDecimal pKurs)
			throws ExceptionLP {
		VerkaufspreisDto preisDto = null;

		try {
			preisDto = this.vkPreisfindungFac.getVerkaufspreisInFremdwaehrung(
					pPreisdetailsInMandantenwaehrung, pKurs);
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return preisDto;
	}

	/**
	 * Ausgehende vom Verkaufspreis in Fredmwaehrung wird ueber den
	 * Umrechnungsfaktor der Verkaufspreis in Mandantenwaehrung bestimmt.
	 * 
	 * @param pPreisdetailsInFremdwaehrung
	 *            in Fredmwaehrung VerkaufspreisDto
	 * @param pKurs
	 *            fuer Umrechnung Mandantenwaherung nach Fremdwaehrung
	 *            BigDecimal
	 * @return VerkaufspreisDto in Mandantenwaehrung
	 * @throws ExceptionLP
	 */
	public VerkaufspreisDto getPreisdetailsInMandantenwaehrung(
			VerkaufspreisDto pPreisdetailsInFremdwaehrung, BigDecimal pKurs)
			throws ExceptionLP {
		VerkaufspreisDto preisDto = null;

		try {
			preisDto = this.vkPreisfindungFac
					.getVerkaufspreisInMandantenwaehrung(
							pPreisdetailsInFremdwaehrung, pKurs);
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return preisDto;
	}

	/**
	 * Methode zur Preisberechnung. Wird ueber Events ausgeloest und kommt erst
	 * zum Einsatz, nachdem die Preisdetails bereits in Fremdwaehrung angezeigt
	 * wurden.
	 * 
	 * @param bdEinzelpreisI
	 *            Nettoeinzelpreis
	 * @param ddRabattsatzI
	 *            Rabattsatz in Prozent z.B. 20
	 * @param ddZusatzrabattsatzI
	 *            Zusatzrabattsatz in Prozent z.B. 10
	 * @param iIdMwstsatzI
	 *            pk des Mwstsatzes
	 * @throws ExceptionLP
	 * @return VerkaufspreisDto der berechnete Verkaufspreis
	 */
	public VerkaufspreisDto berechneVerkaufspreis(BigDecimal bdEinzelpreisI,
			Double ddRabattsatzI, Double ddZusatzrabattsatzI,
			Integer iIdMwstsatzI, BigDecimal nMaterialzuschlag)
			throws ExceptionLP {
		if (bdEinzelpreisI == null) {
			throw new ExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					"bdEinzelpreisI == null", null);
		}

		if (ddRabattsatzI == null) {
			throw new ExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					"ddRabattsatzI == null", null);
		}

		if (ddZusatzrabattsatzI == null) {
			throw new ExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					"ddZusatzrabattsatzI == null", null);
		}

		if (iIdMwstsatzI == null) {
			throw new ExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					"iIdMwstsatzI == null", null);
		}

		VerkaufspreisDto oPreisDto = new VerkaufspreisDto();

		try {
			oPreisDto.einzelpreis = bdEinzelpreisI;
			oPreisDto.rabattsatz = ddRabattsatzI;
			oPreisDto.rabattsumme = oPreisDto.einzelpreis
					.multiply(new BigDecimal(ddRabattsatzI.doubleValue())
							.movePointLeft(2));
			oPreisDto.setDdZusatzrabattsatz(ddZusatzrabattsatzI);

			BigDecimal nZusatzrabattsumme = oPreisDto.einzelpreis
					.subtract(oPreisDto.rabattsumme)
					.multiply(
							new BigDecimal(oPreisDto.getDdZusatzrabattsatz()
									.doubleValue())).movePointLeft(2);

			oPreisDto.setNZusatzrabattsumme(nZusatzrabattsumme);

			if (nMaterialzuschlag != null) {
				oPreisDto.nettopreis = oPreisDto.einzelpreis
						.subtract(oPreisDto.rabattsumme)
						.subtract(nZusatzrabattsumme).add(nMaterialzuschlag);
				oPreisDto.bdMaterialzuschlag = nMaterialzuschlag;
			} else {
				oPreisDto.nettopreis = oPreisDto.einzelpreis.subtract(
						oPreisDto.rabattsumme).subtract(nZusatzrabattsumme);
			}

			oPreisDto.mwstsatzIId = iIdMwstsatzI;

			// @todo Zugriff auf Mwstsatz PJ 5320
			MandantDelegate oMandantDelegate = new MandantDelegate();
			MwstsatzDto oMwstsatzDto = oMandantDelegate
					.mwstsatzFindByPrimaryKey(iIdMwstsatzI);

			oPreisDto.mwstsumme = oPreisDto.nettopreis
					.multiply(new BigDecimal(oMwstsatzDto.getFMwstsatz()
							.doubleValue()).movePointLeft(2));
			oPreisDto.bruttopreis = oPreisDto.nettopreis
					.add(oPreisDto.mwstsumme);
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return oPreisDto;
	}

	/**
	 * Methode zur Preisberechnung. Wird ueber Events ausgeloest und kommt erst
	 * zum Einsatz, nachdem die Preisdetails bereits in Fremdwaehrung angezeigt
	 * wurden.
	 * 
	 * @param bdEinzelpreisI
	 *            Nettoeinzelpreis
	 * @param bdNettopreisI
	 *            Nettogesamtpreis
	 * @param iIdMwstsatzI
	 *            pk des Mwstsatzes
	 * @throws ExceptionLP
	 * @return VerkaufspreisDto der berechnete Verkaufspreis
	 */
	public VerkaufspreisDto berechneVerkaufspreis(BigDecimal bdEinzelpreisI,
			BigDecimal bdNettopreisI, BigDecimal bdMaterialzuschlag,
			Integer iIdMwstsatzI) throws ExceptionLP {
		if (bdEinzelpreisI == null) {
			throw new ExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					"bdEinzelpreisI == null", null);
		}

		if (bdNettopreisI == null) {
			throw new ExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					"bdNettopreisI == null", null);
		}

		if (iIdMwstsatzI == null) {
			throw new ExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					"iIdMwstsatzI == null", null);
		}

		VerkaufspreisDto oPreisDto = new VerkaufspreisDto();

		try {
			if (bdMaterialzuschlag != null) {
				oPreisDto.bdMaterialzuschlag = bdMaterialzuschlag;
			}

			oPreisDto.einzelpreis = bdEinzelpreisI;

			oPreisDto.nettopreis = bdNettopreisI;

			oPreisDto.rabattsumme = oPreisDto.nettopreis.subtract(
					oPreisDto.einzelpreis).negate();

			if (oPreisDto.einzelpreis.doubleValue() != 0) {
				oPreisDto.rabattsatz = new Double(oPreisDto.rabattsumme
						.divide(oPreisDto.einzelpreis, 4,
								BigDecimal.ROUND_HALF_EVEN).movePointRight(2)
						.doubleValue());
			}

			if (bdMaterialzuschlag != null) {
				oPreisDto.nettopreis = bdNettopreisI.add(bdMaterialzuschlag);
			}

			// Zusatzrabattsatz ist immer 0

			oPreisDto.mwstsatzIId = iIdMwstsatzI;

			// @todo Zugriff auf Mwstsatz PJ 5320
			MandantDelegate oMandantDelegate = new MandantDelegate();
			MwstsatzDto oMwstsatzDto = oMandantDelegate
					.mwstsatzFindByPrimaryKey(iIdMwstsatzI);

			oPreisDto.mwstsumme = oPreisDto.nettopreis
					.multiply(new BigDecimal(oMwstsatzDto.getFMwstsatz()
							.doubleValue()).movePointLeft(2));
			oPreisDto.bruttopreis = oPreisDto.nettopreis
					.add(oPreisDto.mwstsumme);
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return oPreisDto;
	}

	public VkpfartikelpreislisteDto[]

	vkpfartikelpreislisteFindByMandantCNr() throws ExceptionLP {
		VkpfartikelpreislisteDto[] dtos = null;

		try {
			dtos = vkPreisfindungFac
					.vkpfartikelpreislisteFindByMandantCNr(LPMain
							.getTheClient().getMandant());
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return dtos;
	}

	/**
	 * Eine Preisliste ueber ihren unique key finden.
	 * 
	 * @param iiPreislisteI
	 *            Integer
	 * @param iiArtikelI
	 *            Integer
	 * @param datGueltigabI
	 *            Date
	 * @throws ExceptionLP
	 * @return VkPreisfindungPreislisteDto
	 */
	public VkPreisfindungPreislisteDto preislisteFindByUniqueyKey(
			Integer iiPreislisteI, Integer iiArtikelI, Date datGueltigabI)
			throws ExceptionLP {
		VkPreisfindungPreislisteDto dto = null;

		try {
			dto = this.vkPreisfindungFac.preislisteFindByUniqueKey(
					iiPreislisteI, iiArtikelI, datGueltigabI);
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return dto;
	}

	public void pflegeVkpreise(Integer artikelgruppeIId,
			Integer vkpfartikelpreisliste, Date gueltigAb, BigDecimal nProzent)
			throws ExceptionLP {

		try {
			vkPreisfindungFac.pflegeVkpreise(artikelgruppeIId,
					vkpfartikelpreisliste, gueltigAb, nProzent,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	/**
	 * posisort: 0 Zugriff auf das maximale iSort in der Tabelle pro Mandant
	 * 
	 * @param sMandantI
	 *            der aktuelle Mandant
	 * @return Integer das maximale iSort
	 * @throws ExceptionLP
	 *             Ausnahme
	 */
	public Integer getMaxISort(String sMandantI) throws ExceptionLP {
		Integer iSortO = null;

		try {
			iSortO = vkPreisfindungFac.getMaxISort(sMandantI);
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return iSortO;
	}

	/**
	 * Zwei bestehende Preislisten in Bezug auf ihr iSort umreihen.
	 * 
	 * @param iIdPreisliste1I
	 *            PK der ersten Preisliste
	 * @param iIdPreisliste2I
	 *            PK der zweiten Preisliste
	 * @throws ExceptionLP
	 *             Ausnahme
	 */
	public void vertauscheVkpfartikelpreisliste(Integer iIdPreisliste1I,
			Integer iIdPreisliste2I) throws ExceptionLP {
		try {
			// posreihung: 5 Eine Methode zum Vertauschen zweier Positionen
			// implementieren
			vkPreisfindungFac.vertauscheVkpfartikelpreisliste(iIdPreisliste1I,
					iIdPreisliste2I);
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	/**
	 * Wenn eine neue Preisliste im Hinblick auf iSort vor einer bestehenden
	 * eingefuegt werden soll, dann schafft diese Methode Platz fuer den neuen
	 * Datensatz. <br>
	 * Diese Methode wird am Client aufgerufen, bevor die neue Position
	 * abgespeichert wird.
	 * 
	 * @param iSortierungNeuePositionI
	 *            die Stelle, an der eingefuegt werden soll
	 * @throws ExceptionLP
	 *             Ausnahme
	 */
	public void sortierungAnpassenBeiEinfuegenEinerPositionVorPosition(
			int iSortierungNeuePositionI) throws ExceptionLP {
		try {
			vkPreisfindungFac
					.sortierungAnpassenBeiEinfuegenEinerPositionVorPosition(
							LPMain.getTheClient().getMandant(),
							iSortierungNeuePositionI);
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	/**
	 * Einen Artikelpreis daraufhin pruefen, ob er unter dem minimalen
	 * Verkaufspreis auf einem bestimmten Lager liegt.
	 * 
	 * @param iIdArtikelI
	 *            PK des Artikels
	 * @param iIdLagerI
	 *            PK des Lagers
	 * @param ddWechselkursMandantZuBelegwaehrungI
	 *            Wechselkurs
	 * @param bdPreisI
	 *            BigDecimal der vorgeschlagene Verkaufpreis
	 * @return boolean true, wenn der Verkaufspreis unter dem minimalen
	 *         Verkaufspreis liegt
	 * @throws ExceptionLP
	 *             Ausnahme
	 */
	public boolean liegtVerkaufspreisUnterMinVerkaufspreis(Integer iIdArtikelI,
			Integer iIdLagerI, BigDecimal bdPreisI,
			Double ddWechselkursMandantZuBelegwaehrungI, BigDecimal nMengeI)
			throws ExceptionLP {
		boolean bUnterpreisig = false;

		try {
			bUnterpreisig = vkPreisfindungFac
					.liegtVerkaufspreisUnterMinVerkaufspreis(iIdArtikelI,
							iIdLagerI, bdPreisI,
							ddWechselkursMandantZuBelegwaehrungI, nMengeI,
							LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return bUnterpreisig;
	}

	public VkpfartikelpreislisteDto[] getAlleAktivenPreislistenMitHinterlegtemArtikelpreis(
			Integer iIdArtikelI, String waehrungCNrZielwaehrung)
			throws ExceptionLP {
		VkpfartikelpreislisteDto[] aDtos = null;

		try {
			aDtos = vkPreisfindungFac
					.getAlleAktivenPreislistenMitHinterlegtemArtikelpreis(
							iIdArtikelI, Helper.boolean2Short(true),
							waehrungCNrZielwaehrung, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return aDtos;
	}

	public Map getAlleAktivenPreislistenMitVkPreisbasis() throws ExceptionLP {
		Map m = null;

		try {
			m = vkPreisfindungFac
					.getAlleAktivenPreislistenMitVkPreisbasis(LPMain
							.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}

		return m;
	}

	public VkpfartikelpreislisteDto[] getAlleAktivenPreislisten()
			throws ExceptionLP {
		VkpfartikelpreislisteDto[] aDtos = null;

		try {
			myLogger.error(
					"ART>VK-Preise getAlleAktivenPreislisten delegate before: "
							+ (System.currentTimeMillis() - Defaults.lUhrQuickDirtyART),
					null);
			Defaults.lUhrQuickDirtyART = System.currentTimeMillis();

			aDtos = vkPreisfindungFac.getAlleAktivenPreislisten(
					Helper.boolean2Short(true), LPMain.getTheClient());

			myLogger.error(
					"ART>VK-Preise getAlleAktivenPreislisten delegate after: "
							+ (System.currentTimeMillis() - Defaults.lUhrQuickDirtyART),
					null);
			Defaults.lUhrQuickDirtyART = System.currentTimeMillis();
			myLogger.error(
					"ART>VK-Preise getAlleAktivenPreislisten delegate aftertime: "
							+ System.currentTimeMillis(), null);
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return aDtos;
	}

	public Integer createVkpfMengenstaffel(
			VkpfMengenstaffelDto vkpfMengenstaffelDtoI) throws ExceptionLP {
		Integer iId = null;

		try {
			iId = vkPreisfindungFac.createVkpfMengenstaffel(
					vkpfMengenstaffelDtoI, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}

		return iId;
	}

	public void removeVkpfMengenstaffel(
			VkpfMengenstaffelDto vkpfMengenstaffelDtoI) throws ExceptionLP {
		try {
			vkPreisfindungFac.removeVkpfMengenstaffel(vkpfMengenstaffelDtoI,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updateVkpfMengenstaffel(
			VkpfMengenstaffelDto vkpfMengenstaffelDtoI) throws ExceptionLP {
		try {
			vkPreisfindungFac.updateVkpfMengenstaffel(vkpfMengenstaffelDtoI,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public VkpfMengenstaffelDto vkpfMengenstaffelFindByPrimaryKey(Integer iId)
			throws ExceptionLP {
		VkpfMengenstaffelDto dto = null;

		try {
			dto = vkPreisfindungFac.vkpfMengenstaffelFindByPrimaryKey(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}

		return dto;
	}

	public VkpfMengenstaffelDto vkpfMengenstaffelFindByArtikelIIdNMengeGueltigkeitsdatum(
			Integer iIdArtikelI, BigDecimal nMengeI,
			Date datGueltigkeitsdatumI, Integer preislisteIId)
			throws ExceptionLP {
		VkpfMengenstaffelDto dto = null;

		try {
			dto = vkPreisfindungFac
					.vkpfMengenstaffelFindByArtikelIIdNMengeGueltigkeitsdatum(
							iIdArtikelI, nMengeI, datGueltigkeitsdatumI,
							preislisteIId, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}

		return dto;
	}

	public VkpfMengenstaffelDto[] vkpfMengenstaffelFindByArtikelIIdGueltigkeitsdatum(
			Integer iIdArtikelI, Date datGueltigkeitsdatumI,
			Integer preislisteIId) throws ExceptionLP {
		VkpfMengenstaffelDto[] dtos = null;

		try {
			dtos = vkPreisfindungFac
					.vkpfMengenstaffelFindByArtikelIIdGueltigkeitsdatum(
							iIdArtikelI, datGueltigkeitsdatumI, preislisteIId,
							LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}

		return dtos;
	}

	public BigDecimal ermittlePreisbasis(Integer iIdArtikelI,
			Date datGueltigkeitsdatumVkbasisII, Integer preislisteIId,
			String waehrungCNrZielwaehrung) throws ExceptionLP {
		BigDecimal nPreisbasis = null;

		try {
			nPreisbasis = vkPreisfindungFac.ermittlePreisbasis(iIdArtikelI,
					datGueltigkeitsdatumVkbasisII, preislisteIId,
					waehrungCNrZielwaehrung, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}

		return nPreisbasis;
	}

	public BigDecimal ermittlePreisbasis(Integer iIdArtikelI,
			Date datGueltigkeitsdatumVkbasisII, Integer preislisteIId,
			BigDecimal nMengeI, String waehrungCNrZielwaehrung)
			throws ExceptionLP {
		BigDecimal nPreisbasis = null;

		try {
			nPreisbasis = vkPreisfindungFac.ermittlePreisbasis(iIdArtikelI,
					datGueltigkeitsdatumVkbasisII, nMengeI, preislisteIId,
					waehrungCNrZielwaehrung, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}

		return nPreisbasis;
	}

	public BigDecimal ermittlePreisbasisStaffelmenge(Integer iIdArtikelI,
			Date datGueltigkeitsdatumVkbasisII, BigDecimal nMengeI)
			throws ExceptionLP {
		BigDecimal nStaffelmenge = null;

		try {
			nStaffelmenge = vkPreisfindungFac.ermittlePreisbasisStaffelmenge(
					iIdArtikelI, datGueltigkeitsdatumVkbasisII, nMengeI,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}

		return nStaffelmenge;
	}

	public VerkaufspreisDto berechneVerkaufspreis(BigDecimal nPreisbasisI,
			Double dRabattsatzI) throws ExceptionLP {
		VerkaufspreisDto verkaufspreisDto = null;

		try {
			verkaufspreisDto = vkPreisfindungFac.berechneVerkaufspreis(
					nPreisbasisI, dRabattsatzI);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}

		return verkaufspreisDto;
	}

	public VerkaufspreisDto berechneVerkaufspreis(BigDecimal nPreisbasisI,
			Double dRabattsatzI, BigDecimal nMaterialzuschlag)
			throws ExceptionLP {
		VerkaufspreisDto verkaufspreisDto = null;

		try {
			verkaufspreisDto = vkPreisfindungFac.berechneVerkaufspreis(
					nPreisbasisI, dRabattsatzI, nMaterialzuschlag);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}

		return verkaufspreisDto;
	}

	/**
	 * Verkaufspreisdfindung Stufe 3. <br>
	 * Aufgrund des Kunden bzw. seiner Rechnungsadresse wird der VKP ermittelt.
	 * KundeSOKOs koennen fuer einzelne Artikel oder Artikelgruppen erfasst
	 * werden und werden in Form von Mengenstaffeln hinterlegt.
	 * 
	 * @param artikelDtoI
	 *            fuer welchen Artikel wird der VKP ermittelt
	 * @param iIdKundeI
	 *            PK des Kunden, fuer den der VKP ermittelt wird
	 * @param nMengeI
	 *            die Menge des Artikels, die verkauft werden soll
	 * @param tGueltigkeitsdatumI
	 *            zu welchem Zeitpunkt soll der VKP ermittelt werden
	 * @param vkpreisfindungDtoI
	 *            der Ergebniscontainer fuer die VKPF
	 * @param iIdMwstsatzI
	 *            der zu beruecksichtigende Mwstsatz
	 * @return VerkaufspreisDto der ermittelte VKP
	 * @throws ExceptionLP
	 */
	public VkpreisfindungDto verkaufspreisfindungStufe3(ArtikelDto artikelDtoI,
			Integer iIdKundeI, BigDecimal nMengeI, Date tGueltigkeitsdatumI,
			VkpreisfindungDto vkpreisfindungDtoI, Integer iIdMwstsatzI,
			Integer preislisteIId, String waehrungCNrZielwaehrung)
			throws ExceptionLP {
		VkpreisfindungDto vkpreisfindungDto = null;

		try {
			vkpreisfindungDto = vkPreisfindungFac.verkaufspreisfindungStufe3(
					artikelDtoI, iIdKundeI, nMengeI, tGueltigkeitsdatumI,
					new VkpreisfindungDto(LPMain.getTheClient().getLocUi()),
					iIdMwstsatzI, preislisteIId, waehrungCNrZielwaehrung,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}

		return vkpreisfindungDto;
	}

	public KundesokomengenstaffelDto ermittleKundesokomengenstaffel(
			ArtikelDto artikelDtoI, Integer iIdKundeI, BigDecimal nMengeI,
			Date tGueltigkeitsdatumI) throws ExceptionLP {
		KundesokomengenstaffelDto mengenstaffelDto = null;

		try {
			mengenstaffelDto = vkPreisfindungFac
					.ermittleKundesokomengenstaffel(artikelDtoI, iIdKundeI,
							nMengeI, tGueltigkeitsdatumI, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}

		return mengenstaffelDto;
	}

	public VerkaufspreisDto getVkpreisMinimal(
			VkpreisfindungDto vkpreisfindungDtoI) {
		VerkaufspreisDto verkaufspreisDto = null;

		if (vkpreisfindungDtoI.getVkpreisminimalStufe().equals(
				VkpreisfindungDto.VKPFPREISBASIS)) {
			verkaufspreisDto = vkpreisfindungDtoI.getVkpPreisbasis();
		} else if (vkpreisfindungDtoI.getVkpreisminimalStufe().equals(
				VkpreisfindungDto.VKPFSTUFE1)) {
			verkaufspreisDto = vkpreisfindungDtoI.getVkpStufe1();
		} else if (vkpreisfindungDtoI.getVkpreisminimalStufe().equals(
				VkpreisfindungDto.VKPFSTUFE2)) {
			verkaufspreisDto = vkpreisfindungDtoI.getVkpStufe2();
		} else if (vkpreisfindungDtoI.getVkpreisminimalStufe().equals(
				VkpreisfindungDto.VKPFSTUFE3)) {
			verkaufspreisDto = vkpreisfindungDtoI.getVkpStufe3();
		}

		return verkaufspreisDto;
	}

	/**
	 * Gegeben ist eine Preisbasis (= Einzelpreis) und ein Fixpreis (=
	 * Nettoeinzelpreis). Zu berechnen ist der Rabattsatz, der Zusatzrabattsatz
	 * bleibt auf 0.
	 * 
	 * @param nPreisbasisI
	 *            Einzelpreis
	 * @param nFixpreisI
	 *            Nettoeinzelpreis
	 * @return VerkaufspreisDto VKP
	 * @throws ExceptionLP
	 */
	public VerkaufspreisDto berechneVerkaufspreis(BigDecimal nPreisbasisI,
			BigDecimal nFixpreisI) throws ExceptionLP {
		VerkaufspreisDto verkaufspreisDto = null;

		try {
			verkaufspreisDto = vkPreisfindungFac.berechneVerkaufspreis(
					nPreisbasisI, nFixpreisI);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}

		return verkaufspreisDto;
	}

	public VerkaufspreisDto berechneVerkaufspreis(BigDecimal nPreisbasisI,
			BigDecimal nFixpreisI, BigDecimal materialzuschlag)
			throws ExceptionLP {
		VerkaufspreisDto verkaufspreisDto = null;

		try {
			verkaufspreisDto = vkPreisfindungFac.berechneVerkaufspreis(
					nPreisbasisI, nFixpreisI, materialzuschlag);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}

		return verkaufspreisDto;
	}

	/**
	 * Pruefen, ob der Verkaufspreis unter dem minimalen Verkaufspreis des
	 * Artikels am Hauptlager des Mandanten liegt.
	 * 
	 * @param iIdArtikelI
	 *            PK des Artikels
	 * @param bdVerkaufspreisI
	 *            der vorgeschlagene Verkaufspreis
	 * @param ddWechselkursMandantZuBelegwaehrungI
	 *            der Wechselkurs
	 * @return boolean true, wenn der Verkaufspreis unter dem minimalen
	 *         Verkaufspreis liegt
	 * @throws ExceptionLP
	 *             Ausnahme
	 */
	public boolean liegtVerkaufpreisUnterMinVerkaufspreis(Integer iIdArtikelI,
			BigDecimal bdVerkaufspreisI,
			Double ddWechselkursMandantZuBelegwaehrungI, BigDecimal nMengeI)
			throws ExceptionLP {
		boolean bUnterpreisig = false;

		try {
			bUnterpreisig = vkPreisfindungFac
					.liegtVerkaufpreisUnterMinVerkaufpsreis(iIdArtikelI,
							bdVerkaufspreisI,
							ddWechselkursMandantZuBelegwaehrungI, nMengeI,
							LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return bUnterpreisig;
	}

	public void pruefeVkpfStaffelmenge() throws ExceptionLP {
		try {
			vkPreisfindungFac.pruefeVkpfStaffelmenge(LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public VkPreisfindungPreislisteDto getVkPreisfindungPreislisteFindByArtikelIIdPreislisteIIdTPreisgueltigab(
			Integer artikelIId, Integer preislisteIId, Date datGueltikeitsdatumI)
			throws ExceptionLP {
		try {
			return vkPreisfindungFac
					.getVkPreisfindungPreislisteFindByArtikelIIdPreislisteIIdTPreisgueltigab(
							artikelIId, preislisteIId, datGueltikeitsdatumI,
							LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

}
