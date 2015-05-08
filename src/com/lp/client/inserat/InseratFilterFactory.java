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
package com.lp.client.inserat;

import java.util.ArrayList;

import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.system.SystemFilterFactory;
import com.lp.server.auftrag.service.AuftragFac;
import com.lp.server.auftrag.service.AuftragServiceFac;
import com.lp.server.inserat.service.InseratFac;
import com.lp.server.partner.service.KundeFac;
import com.lp.server.partner.service.LieferantDto;
import com.lp.server.partner.service.PartnerFac;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.util.Facade;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.FilterKriteriumDirekt;

@SuppressWarnings("static-access")
/*
 * <p> Diese Klasse ist ein Singleton und kuemmert sich um alle Filter im
 * Auftrag.</p>
 * 
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 * 
 * <p>Erstellung: 26. 04. 2005</p>
 * 
 * <p>@author Uli Walch</p>
 * 
 * @version $Revision: 1.15 $ Date $Date: 2012/11/29 15:24:06 $
 */
public class InseratFilterFactory {
	private static InseratFilterFactory filterFactory = null;

	private InseratFilterFactory() {
		// Singleton.
	}

	/**
	 * Hole das Singelton AuftragFilterFactory.
	 * 
	 * @return LPMain
	 */
	static public InseratFilterFactory getInstance() {
		if (filterFactory == null) {
			filterFactory = new InseratFilterFactory();
		}

		return filterFactory;
	}

	/**
	 * Direktes Filter Kriterium Auftragnummer fuer das PanelAuftragAuswahl.
	 * 
	 * @return FilterKriteriumDirekt
	 * @throws Throwable
	 */
	public FilterKriteriumDirekt createFKDInseratnummer() throws Throwable {
		return new FilterKriteriumDirekt("c_nr", "",
				FilterKriterium.OPERATOR_LIKE, LPMain.getInstance()
						.getTextRespectUISPr("iv.inserat.inseratnummer"),
				FilterKriteriumDirekt.PROZENT_LEADING, // Auswertung als '%XX'
				true, // wrapWithSingleQuotes
				false, Facade.MAX_UNBESCHRAENKT); // ignore case
	}
	
	public FilterKriteriumDirekt createFKDBestellnummer() throws Throwable {
		return new FilterKriteriumDirekt("flrinserat.flrbestellposition.flrbestellung.c_nr", "",
				FilterKriterium.OPERATOR_LIKE, LPMain.getInstance()
						.getTextRespectUISPr("label.bestellnummer"),
				FilterKriteriumDirekt.PROZENT_LEADING, // Auswertung als '%XX'
				true, // wrapWithSingleQuotes
				false, Facade.MAX_UNBESCHRAENKT); // ignore case
	}

	public FilterKriteriumDirekt createFKDStichwort() throws Throwable {
		return new FilterKriteriumDirekt(InseratFac.FLR_INSERAT_C_STICHWORT,
				"", FilterKriterium.OPERATOR_LIKE, LPMain.getInstance()
						.getTextRespectUISPr("iv.stichwort"),
				FilterKriteriumDirekt.PROZENT_BOTH, // Auswertung als '%XX'
				true, // wrapWithSingleQuotes
				true, Facade.MAX_UNBESCHRAENKT); // ignore case
	}

	public FilterKriteriumDirekt createFKDLieferant() throws Throwable {
		ParametermandantDto parameter = (ParametermandantDto) DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getParametermandant(
						ParameterFac.PARAMETER_PARTNERSUCHE_WILDCARD_BEIDSEITIG,
						ParameterFac.KATEGORIE_PARTNER,
						LPMain.getInstance().getTheClient().getMandant());
		if (((Boolean) parameter.getCWertAsObject() == true)) {
			return new FilterKriteriumDirekt(
					InseratFac.FLR_INSERAT_FLRLIEFERANT + "."
							+ KundeFac.FLR_PARTNER + "."
							+ PartnerFac.FLR_PARTNER_NAME1NACHNAMEFIRMAZEILE1,
					"", FilterKriterium.OPERATOR_LIKE, LPMain.getInstance()
							.getTextRespectUISPr("label.lieferant"),
					FilterKriteriumDirekt.PROZENT_BOTH, // Auswertung als 'XX%'
					true, // wrapWithSingleQuotes
					true, Facade.MAX_UNBESCHRAENKT); // ignore case
		} else {
			return new FilterKriteriumDirekt(
					InseratFac.FLR_INSERAT_FLRLIEFERANT + "."
							+ KundeFac.FLR_PARTNER + "."
							+ PartnerFac.FLR_PARTNER_NAME1NACHNAMEFIRMAZEILE1,
					"", FilterKriterium.OPERATOR_LIKE, LPMain.getInstance()
							.getTextRespectUISPr("label.lieferant"),
					FilterKriteriumDirekt.PROZENT_TRAILING, // Auswertung als
															// 'XX%'
					true, // wrapWithSingleQuotes
					true, Facade.MAX_UNBESCHRAENKT); // ignore case
		}
	}

	public FilterKriteriumDirekt createFKDKunde() throws Throwable {
		ParametermandantDto parameter = (ParametermandantDto) DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getParametermandant(
						ParameterFac.PARAMETER_PARTNERSUCHE_WILDCARD_BEIDSEITIG,
						ParameterFac.KATEGORIE_PARTNER,
						LPMain.getInstance().getTheClient().getMandant());
		if (((Boolean) parameter.getCWertAsObject() == true)) {
			return new FilterKriteriumDirekt("filter_kunde", "",
					FilterKriterium.OPERATOR_LIKE, LPMain.getInstance()
							.getTextRespectUISPr("label.kunde"),
					FilterKriteriumDirekt.PROZENT_BOTH, // Auswertung als 'XX%'
					true, // wrapWithSingleQuotes
					true, Facade.MAX_UNBESCHRAENKT); // ignore case
		} else {
			return new FilterKriteriumDirekt("filter_kunde", "",
					FilterKriterium.OPERATOR_LIKE, LPMain.getInstance()
							.getTextRespectUISPr("label.kunde"),
					FilterKriteriumDirekt.PROZENT_TRAILING, // Auswertung als
															// 'XX%'
					true, // wrapWithSingleQuotes
					true, Facade.MAX_UNBESCHRAENKT); // ignore case
		}
	}

	public FilterKriterium[] createFKAuftragAuswahlOffene() throws Throwable {
		FilterKriterium[] kriterien = new FilterKriterium[2];

		kriterien[0] = new FilterKriterium("mandant_c_nr", true, "'"
				+ LPMain.getInstance().getTheClient().getMandant() + "'",
				FilterKriterium.OPERATOR_EQUAL, false);

		kriterien[1] = new FilterKriterium(
				AuftragFac.FLR_AUFTRAG_AUFTRAGSTATUS_C_NR, true, "('"
						+ AuftragServiceFac.AUFTRAGSTATUS_ERLEDIGT + "','"
						+ AuftragServiceFac.AUFTRAGSTATUS_STORNIERT + "')",
				FilterKriterium.OPERATOR_NOT_IN, false);

		return kriterien;
	}

	public FilterKriterium[] createFKInseratKey(Integer inseratIId)
			throws Throwable {
		// Handartikel nicht anzeigen
		FilterKriterium[] kriterien = new FilterKriterium[1];
		kriterien[0] = new FilterKriterium("i_id", true, inseratIId + "",
				FilterKriterium.OPERATOR_EQUAL, false);

		return kriterien;
	}

	
	public FilterKriterium[] createFKInseratrechnung(Integer inseratIId)
			throws Throwable {
		FilterKriterium[] kriterien = new FilterKriterium[1];

		kriterien[0] = new FilterKriterium("inserat_i_id", true, inseratIId
				+ "", FilterKriterium.OPERATOR_EQUAL, false);

		return kriterien;
	}

	public FilterKriterium[] createFKInseratartikel(Integer inseratIId)
			throws Throwable {
		FilterKriterium[] kriterien = new FilterKriterium[1];

		kriterien[0] = new FilterKriterium("inserat_i_id", true, inseratIId
				+ "", FilterKriterium.OPERATOR_EQUAL, false);

		return kriterien;
	}

	public FilterKriterium[] createFKInserater(Integer inseratIId)
			throws Throwable {
		FilterKriterium[] kriterien = new FilterKriterium[1];

		kriterien[0] = new FilterKriterium("inserat_i_id", true, inseratIId
				+ "", FilterKriterium.OPERATOR_EQUAL, false);

		return kriterien;
	}

	public FilterKriterium[] createFKLieferantenOhneBestellungen(
			ArrayList<Integer> liferantIIds) throws Throwable {
		FilterKriterium[] kriterien = new FilterKriterium[1];

		String lieferantIId = "";
		for (int i = 0; i < liferantIIds.size(); i++) {
			lieferantIId += liferantIIds.get(i);
			if (i != liferantIIds.size() - 1) {
				lieferantIId += ",";
			}
		}
		kriterien[0] = new FilterKriterium("i_id", true, "(" + lieferantIId
				+ ")", FilterKriterium.OPERATOR_IN, false);

		return kriterien;
	}

	public FilterKriterium[] createFKInserateEinesLieferantenOhneER(
			Integer lieferantIId) throws Throwable {
		FilterKriterium[] kriterien = new FilterKriterium[2];

		kriterien[0] = new FilterKriterium("flrinserat.lieferant_i_id", true,
				lieferantIId + "", FilterKriterium.OPERATOR_EQUAL, false);

		kriterien[1] = new FilterKriterium("flrinserat.status_c_nr", true, "'"
				+ LocaleFac.STATUS_ERSCHIENEN + "'",
				FilterKriterium.OPERATOR_EQUAL, false);

		return kriterien;
	}

	public FilterKriterium[] createFKInserateEinesLieferantenUndDessenRechungsadresseOhneER(
			Integer lieferantIId) throws Throwable {
		FilterKriterium[] kriterien = new FilterKriterium[2];
		String in = "(" + lieferantIId;

		LieferantDto lDto = DelegateFactory.getInstance()
				.getLieferantDelegate().lieferantFindByPrimaryKey(lieferantIId);

		Integer partnerIIdRechnungsadresse = lDto
				.getPartnerIIdRechnungsadresse();
		if (partnerIIdRechnungsadresse != null) {
			LieferantDto lDtoRech = DelegateFactory
					.getInstance()
					.getLieferantDelegate()
					.lieferantFindByiIdPartnercNrMandantOhneExc(
							partnerIIdRechnungsadresse,
							LPMain.getTheClient().getMandant());

			if (lDtoRech != null) {
				in += "," + lDtoRech.getIId();
			}

		}

		in += ")";

		kriterien[0] = new FilterKriterium("flrinserat.lieferant_i_id", true,
				in, FilterKriterium.OPERATOR_IN, false);

		kriterien[1] = new FilterKriterium("erset.inserat_i_id", true, "", FilterKriterium.OPERATOR_IS+" "+FilterKriterium.OPERATOR_NULL, false);

		
		//wg. SP1672 auskommentiert
		/*kriterien[2] = new FilterKriterium("flrinserat.t_erschienen", true, "",
				FilterKriterium.OPERATOR_IS + " "
						+ FilterKriterium.OPERATOR_NOT_NULL, false);*/

		return kriterien;
	}

	public FilterKriterium[] createFKInserateEinesLieferantenOhneERSchnellansicht()
			throws Throwable {
		FilterKriterium[] kriterien = new FilterKriterium[1];

		kriterien[0] = new FilterKriterium("erset.i_id", true, "",
				FilterKriterium.OPERATOR_IS + " "
						+ FilterKriterium.OPERATOR_NULL, false);

		return kriterien;
	}

	public FilterKriterium[] createFKAlleLieferantenOffeneInserate(
			ArrayList<Integer> liferantIIds) throws Throwable {
		FilterKriterium[] kriterien = new FilterKriterium[1];

		String lieferantIId = "";
		for (int i = 0; i < liferantIIds.size(); i++) {
			lieferantIId += liferantIIds.get(i);
			if (i != liferantIIds.size() - 1) {
				lieferantIId += ",";
			}
		}
		kriterien[0] = new FilterKriterium("i_id", true, "(" + lieferantIId
				+ ")", FilterKriterium.OPERATOR_IN, false);

		return kriterien;
	}

	public FilterKriterium[] createFKSchnellansicht() throws Throwable {
		FilterKriterium[] filters = new FilterKriterium[1];
		filters[0] = new FilterKriterium(InseratFac.FLR_INSERAT_STATUS_C_NR,
				true, "('" + LocaleFac.STATUS_STORNIERT + "','"
						+ LocaleFac.STATUS_VERRECHNET + "','"
						+ LocaleFac.STATUS_ERLEDIGT + "')",
				FilterKriterium.OPERATOR_NOT_IN, false);

		return filters;
	}

}
