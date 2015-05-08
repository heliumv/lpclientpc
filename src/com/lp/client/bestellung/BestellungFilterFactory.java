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
package com.lp.client.bestellung;

import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.system.SystemFilterFactory;
import com.lp.client.util.fastlanereader.gui.QueryType;
import com.lp.server.artikel.service.ArtikelFac;
import com.lp.server.bestellung.service.BSMahnlaufDto;
import com.lp.server.bestellung.service.BSMahnwesenFac;
import com.lp.server.bestellung.service.BestellpositionFac;
import com.lp.server.bestellung.service.BestellungFac;
import com.lp.server.bestellung.service.BestellungServiceFac;
import com.lp.server.bestellung.service.BestellvorschlagFac;
import com.lp.server.bestellung.service.WareneingangFac;
import com.lp.server.partner.service.KundeFac;
import com.lp.server.partner.service.LieferantFac;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.util.Facade;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.FilterKriteriumDirekt;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.server.util.fastlanereader.service.query.SortierKriterium;

/**
 * <p>
 * Singelton f&uuml;r alle Bestellungfilter
 * </p>
 * 
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2008
 * </p>
 * 
 * <p>
 * Erstellung: Josef Erlinger; 5.05.05
 * </p>
 * 
 * <p>
 * 
 * @author $Author: christian $
 *         </p>
 * 
 * @version $Revision: 1.21 $ Date $Date: 2012/08/23 11:09:58 $
 */
public class BestellungFilterFactory {
	private static BestellungFilterFactory filterFactory = null;

	private BestellungFilterFactory() {
		// Singleton.
	}

	/**
	 * Singelton
	 * 
	 * @return BestellungFilterFactory
	 */
	public static BestellungFilterFactory getInstance() {
		if (filterFactory == null) {
			filterFactory = new BestellungFilterFactory();
		}
		return filterFactory;
	}

	public FilterKriterium[] createFKBestellungKey(Integer iId)
			throws Throwable {
		// Handartikel nicht anzeigen
		FilterKriterium[] kriterien = new FilterKriterium[1];
		kriterien[0] = new FilterKriterium("i_id", true, iId + "",
				FilterKriterium.OPERATOR_EQUAL, false);

		return kriterien;
	}

	// hier kommen die Filter rein

	public FilterKriterium[] createFKBestellungSchnellansicht() {
		FilterKriterium[] filters = new FilterKriterium[1];
		filters[0] = new FilterKriterium(
				BestellungFac.FLR_BESTELLUNG_BESTELLUNGSTATUS_C_NR, true, "('"
						+ BestellungFac.BESTELLSTATUS_STORNIERT + "','"
						+ BestellungFac.BESTELLSTATUS_ERLEDIGT + "')",
				FilterKriterium.OPERATOR_NOT_IN, false);

		return filters;
	}

	public FilterKriteriumDirekt createFKDLieferantPartnerName()
			throws Throwable {
		ParametermandantDto parameter = (ParametermandantDto) DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getParametermandant(
						ParameterFac.PARAMETER_PARTNERSUCHE_WILDCARD_BEIDSEITIG,
						ParameterFac.KATEGORIE_PARTNER,
						LPMain.getInstance().getTheClient().getMandant());
		if (((Boolean) parameter.getCWertAsObject() == true)) {
			FilterKriteriumDirekt fKDPartnername = new FilterKriteriumDirekt(
					BestellungFac.FLR_BESTELLUNG_FLRLIEFERANT + "."
							+ LieferantFac.FLR_PARTNER_NAME1NACHNAMEFIRMAZEILE1,
					"", FilterKriterium.OPERATOR_LIKE, LPMain
							.getTextRespectUISPr("lp.firma"),
					FilterKriteriumDirekt.PROZENT_BOTH, true, true,
					Facade.MAX_UNBESCHRAENKT); // wrapWithSingleQuotes
			return fKDPartnername;
		} else {
			FilterKriteriumDirekt fKDPartnername = new FilterKriteriumDirekt(
					BestellungFac.FLR_BESTELLUNG_FLRLIEFERANT + "."
							+ LieferantFac.FLR_PARTNER_NAME1NACHNAMEFIRMAZEILE1,
					"", FilterKriterium.OPERATOR_LIKE, LPMain
							.getTextRespectUISPr("lp.firma"),
					FilterKriteriumDirekt.PROZENT_TRAILING, true, true,
					Facade.MAX_UNBESCHRAENKT); // wrapWithSingleQuotes
			return fKDPartnername;
		}
	}

	public FilterKriteriumDirekt createFKDLieferantPartnerOrt() {
		FilterKriteriumDirekt fKDPartnerOrt = new FilterKriteriumDirekt(
				BestellungFac.FLR_BESTELLUNG_FLRLIEFERANT + "."
						+ LieferantFac.FLR_PARTNER_LANDPLZORT_ORT_NAME, "",
				FilterKriterium.OPERATOR_LIKE, LPMain
						.getTextRespectUISPr("lp.ort"),
				FilterKriteriumDirekt.PROZENT_BOTH, true, // wrapWithSingleQuotes
				true, Facade.MAX_UNBESCHRAENKT);
		return fKDPartnerOrt;
	}

	public FilterKriterium[] createFKMahnung(BSMahnlaufDto bsmahnlaufDto) {
		FilterKriterium[] filter = null;
		Integer bsmahnlaufIId = null;
		if (bsmahnlaufDto != null) {
			bsmahnlaufIId = bsmahnlaufDto.getIId();
		} else {
			// keine anzeigen
			bsmahnlaufIId = new Integer(-1);
		}

		if (bsmahnlaufIId != null) {
			filter = new FilterKriterium[1];
			filter[0] = new FilterKriterium(
					BSMahnwesenFac.FLR_MAHNUNG_MAHNLAUF_I_ID, true, "'"
							+ bsmahnlaufIId.toString() + "'",
					FilterKriterium.OPERATOR_EQUAL, false);
		}
		return filter;
	}

	public FilterKriterium[] createFKLieferantenoptimierenMandantCNr() throws Throwable {
		FilterKriterium[] kriterien = new FilterKriterium[1];
		kriterien[0] = new FilterKriterium("flrLieferantenoptimieren.flrbestellvorschlag.mandant_c_nr", true, "'"
				+ LPMain.getTheClient().getMandant() + "'",
				FilterKriterium.OPERATOR_EQUAL, false);
		return kriterien;
	}
	
	public FilterKriterium[] createFKBestellvorschlagArtikelIIdLieferantIId(
			Integer iArtikelId, Integer iLieferantId) {
		/**
		 * @todo MB->MB was hat der fuer einen sinn? finde keine referenzen
		 */
		FilterKriterium[] filter = new FilterKriterium[2];
		filter[0] = new FilterKriterium("artikelIId", true, iArtikelId
				.toString(), FilterKriterium.OPERATOR_EQUAL, false);

		filter[1] = new FilterKriterium("lieferant_i_id", true, iLieferantId
				.toString(), FilterKriterium.OPERATOR_EQUAL, false);

		return filter;
	}

	public FilterKriterium[] getDefaultFilterBestellung(Integer iIdBestellungI) {
		FilterKriterium[] aFilterKrit = new FilterKriterium[1];
		aFilterKrit[0] = new FilterKriterium(
				BestellpositionFac.FLR_BESTELLPOSITION_FLRBESTELLUNG + "."
						+ BestellungFac.FLR_BESTELLUNG_I_ID, true, "'"
						+ iIdBestellungI + "'", FilterKriterium.OPERATOR_EQUAL,
				false);
		return aFilterKrit;
	}

	public FilterKriterium[] getDefaultFilterWareneingangQP4(
			Integer iIdBestellungI) {
		FilterKriterium[] kriterien = new FilterKriterium[1];
		kriterien[0] = new FilterKriterium(
				BestellpositionFac.FLR_BESTELLPOSITION_FLRBESTELLUNG + "."
						+ BestellungFac.FLR_BESTELLUNG_I_ID, true, "'"
						+ iIdBestellungI + "'", FilterKriterium.OPERATOR_EQUAL,
				false);
		return kriterien;
	}

	public FilterKriterium[] getDefaultFilterPositionNichtErledigt(
			Integer iIdBestellungI) {
		FilterKriterium[] kriterien = new FilterKriterium[2];
		kriterien[0] = new FilterKriterium(
				BestellpositionFac.FLR_BESTELLPOSITION_FLRBESTELLUNG + "."
						+ BestellungFac.FLR_BESTELLUNG_I_ID, true, "'"
						+ iIdBestellungI + "'", FilterKriterium.OPERATOR_EQUAL,
				false);
		kriterien[1] = new FilterKriterium(
				BestellpositionFac.FLR_BESTELLPOSITION_BESTELLPOSITIONSTATUS_C_NR,
				true, "'" + BestellpositionFac.BESTELLPOSITIONSTATUS_ERLEDIGT
						+ "'", FilterKriterium.OPERATOR_NOT_EQUAL, false);
		return kriterien;
	}

	public FilterKriterium[] getDefaultFilterWEPQP5(Integer iIdBestellungI)
			throws Throwable {

		ParametermandantDto parameter = DelegateFactory.getInstance()
				.getParameterDelegate().getMandantparameter(
						LPMain.getTheClient().getMandant(),
						ParameterFac.KATEGORIE_BESTELLUNG,
						ParameterFac.PARAMETER_IN_WEP_TEXTEINGABEN_ANZEIGEN);

		if (((Boolean) parameter.getCWertAsObject()) == false) {

			FilterKriterium[] kriterien = new FilterKriterium[6];

			kriterien[0] = new FilterKriterium(
					BestellpositionFac.FLR_BESTELLPOSITION_FLRBESTELLUNG + "."
							+ BestellungFac.FLR_BESTELLUNG_I_ID, true, ""
							+ iIdBestellungI, FilterKriterium.OPERATOR_EQUAL,
					false);

			kriterien[1] = new FilterKriterium(
					BestellpositionFac.FLR_BESTELLPOSITION_BESTELLPOSITIONART_C_NR,
					true, "'" + BestellpositionFac.BESTELLPOSITIONART_BETRIFFT
							+ "'", FilterKriterium.OPERATOR_NOT_EQUAL, false);

			kriterien[2] = new FilterKriterium(
					BestellpositionFac.FLR_BESTELLPOSITION_BESTELLPOSITIONART_C_NR,
					true, "'" + BestellpositionFac.BESTELLPOSITIONART_LEERZEILE
							+ "'", FilterKriterium.OPERATOR_NOT_EQUAL, false);

			kriterien[3] = new FilterKriterium(
					BestellpositionFac.FLR_BESTELLPOSITION_BESTELLPOSITIONART_C_NR,
					true,
					"'" + BestellpositionFac.BESTELLPOSITIONART_SEITENUMBRUCH
							+ "'", FilterKriterium.OPERATOR_NOT_EQUAL, false);

			kriterien[4] = new FilterKriterium(
					BestellpositionFac.FLR_BESTELLPOSITION_BESTELLPOSITIONART_C_NR,
					true,
					"'" + BestellpositionFac.BESTELLPOSITIONART_TEXTBAUSTEIN
							+ "'", FilterKriterium.OPERATOR_NOT_EQUAL, false);

			kriterien[5] = new FilterKriterium(
					BestellpositionFac.FLR_BESTELLPOSITION_BESTELLPOSITIONART_C_NR,
					true, "'"
							+ BestellpositionFac.BESTELLPOSITIONART_TEXTEINGABE
							+ "'", FilterKriterium.OPERATOR_NOT_EQUAL, false);
			return kriterien;
		} else {
			FilterKriterium[] kriterien = new FilterKriterium[5];

			kriterien[0] = new FilterKriterium(
					BestellpositionFac.FLR_BESTELLPOSITION_FLRBESTELLUNG + "."
							+ BestellungFac.FLR_BESTELLUNG_I_ID, true, ""
							+ iIdBestellungI, FilterKriterium.OPERATOR_EQUAL,
					false);

			kriterien[1] = new FilterKriterium(
					BestellpositionFac.FLR_BESTELLPOSITION_BESTELLPOSITIONART_C_NR,
					true, "'" + BestellpositionFac.BESTELLPOSITIONART_BETRIFFT
							+ "'", FilterKriterium.OPERATOR_NOT_EQUAL, false);

			kriterien[2] = new FilterKriterium(
					BestellpositionFac.FLR_BESTELLPOSITION_BESTELLPOSITIONART_C_NR,
					true, "'" + BestellpositionFac.BESTELLPOSITIONART_LEERZEILE
							+ "'", FilterKriterium.OPERATOR_NOT_EQUAL, false);

			kriterien[3] = new FilterKriterium(
					BestellpositionFac.FLR_BESTELLPOSITION_BESTELLPOSITIONART_C_NR,
					true,
					"'" + BestellpositionFac.BESTELLPOSITIONART_SEITENUMBRUCH
							+ "'", FilterKriterium.OPERATOR_NOT_EQUAL, false);

			kriterien[4] = new FilterKriterium(
					BestellpositionFac.FLR_BESTELLPOSITION_BESTELLPOSITIONART_C_NR,
					true,
					"'" + BestellpositionFac.BESTELLPOSITIONART_TEXTBAUSTEIN
							+ "'", FilterKriterium.OPERATOR_NOT_EQUAL, false);

			return kriterien;
		}

	}

	public FilterKriteriumDirekt createFKDBestellvorschlagArtikelnummer()
			throws Throwable {
		int iLaenge = ArtikelFac.MAX_ARTIKEL_ARTIKELNUMMER;
		ParametermandantDto parameter = (ParametermandantDto) DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getParametermandant(
						ParameterFac.PARAMETER_ARTIKEL_MAXIMALELAENGE_ARTIKELNUMMER,
						ParameterFac.KATEGORIE_ARTIKEL,
						LPMain.getTheClient().getMandant());

		if (parameter.getCWertAsObject() != null) {
			iLaenge = ((Integer) parameter.getCWertAsObject()).intValue();
		}
		return new FilterKriteriumDirekt(
				BestellvorschlagFac.FLR_BESTELLVORSCHLAG_FLRARTIKEL + "."
						+ ArtikelFac.FLR_ARTIKEL_C_NR, "",
				FilterKriterium.OPERATOR_LIKE, LPMain
						.getTextRespectUISPr("artikel.artikelnummer"),
				FilterKriteriumDirekt.PROZENT_TRAILING, true, true, iLaenge);
	}

	public FilterKriteriumDirekt createFKDLieferantenoptimierenTextsuche()
			throws Throwable {
		
		return new FilterKriteriumDirekt(
				ArtikelFac.FLR_ARTIKELLISTE_C_VOLLTEXT, "",
				FilterKriterium.OPERATOR_LIKE, LPMain
						.getTextRespectUISPr("lp.textsuche"),
				FilterKriteriumDirekt.EXTENDED_SEARCH, false, true,
				Facade.MAX_UNBESCHRAENKT); // wrapWithSingleQuotes
		
		
	}
	public FilterKriteriumDirekt createFKDBestellvorschlagTextsuche()
			throws Throwable {
		
		return new FilterKriteriumDirekt(
				ArtikelFac.FLR_ARTIKELLISTE_C_VOLLTEXT, "",
				FilterKriterium.OPERATOR_LIKE, LPMain
						.getTextRespectUISPr("lp.textsuche"),
				FilterKriteriumDirekt.EXTENDED_SEARCH, false, true,
				Facade.MAX_UNBESCHRAENKT); // wrapWithSingleQuotes
		
		
	}
	
	public FilterKriteriumDirekt createFKDLieferantenoptimierenArtikelnummer()
			throws Throwable {
		int iLaenge = ArtikelFac.MAX_ARTIKEL_ARTIKELNUMMER;
		ParametermandantDto parameter = (ParametermandantDto) DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getParametermandant(
						ParameterFac.PARAMETER_ARTIKEL_MAXIMALELAENGE_ARTIKELNUMMER,
						ParameterFac.KATEGORIE_ARTIKEL,
						LPMain.getTheClient().getMandant());

		if (parameter.getCWertAsObject() != null) {
			iLaenge = ((Integer) parameter.getCWertAsObject()).intValue();
		}
		return new FilterKriteriumDirekt(
				"flrLieferantenoptimieren.flrartikelliste.c_nr", "",
				FilterKriterium.OPERATOR_LIKE, LPMain
						.getTextRespectUISPr("artikel.artikelnummer"),
				FilterKriteriumDirekt.PROZENT_TRAILING, true, true, iLaenge);
	}

	
	public FilterKriteriumDirekt getFilterkriteriumDirekt1() {
		// 0 dem PanelQuery die direkten FilterKriterien setzen
		FilterKriteriumDirekt fkDirekt1 = new FilterKriteriumDirekt(
				BestellungFac.FLR_BESTELLUNG_C_NR, "",
				FilterKriterium.OPERATOR_LIKE, LPMain
						.getTextRespectUISPr("label.bestellnummer"),
				FilterKriteriumDirekt.PROZENT_LEADING, // Auswertung als '%XX'
				true, // wrapWithSingleQuotes
				false, Facade.MAX_UNBESCHRAENKT); // ignore case

		return fkDirekt1;
	}

	public FilterKriteriumDirekt getFilterkriteriumDirekt2() throws Throwable {
		ParametermandantDto parameter = (ParametermandantDto) DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getParametermandant(
						ParameterFac.PARAMETER_PARTNERSUCHE_WILDCARD_BEIDSEITIG,
						ParameterFac.KATEGORIE_PARTNER,
						LPMain.getTheClient().getMandant());
		if (((Boolean) parameter.getCWertAsObject() == true)) {
			FilterKriteriumDirekt fkDirekt2 = new FilterKriteriumDirekt(
					ArtikelFac.FLR_ARTIKELLIEFERANT_FLRLIEFERANT + "."
							+ KundeFac.FLR_PARTNER_NAME1NACHNAMEFIRMAZEILE1,
					"", FilterKriterium.OPERATOR_LIKE, LPMain
							.getTextRespectUISPr("label.lieferant"),
					FilterKriteriumDirekt.PROZENT_BOTH, // Auswertung als 'XX%'
					true, true, Facade.MAX_UNBESCHRAENKT);
			return fkDirekt2;
		} else {
			FilterKriteriumDirekt fkDirekt2 = new FilterKriteriumDirekt(
					ArtikelFac.FLR_ARTIKELLIEFERANT_FLRLIEFERANT + "."
							+ KundeFac.FLR_PARTNER_NAME1NACHNAMEFIRMAZEILE1,
					"", FilterKriterium.OPERATOR_LIKE, LPMain
							.getTextRespectUISPr("label.lieferant"),
					FilterKriteriumDirekt.PROZENT_TRAILING, // Auswertung als
					// 'XX%'
					true, true, Facade.MAX_UNBESCHRAENKT);
			return fkDirekt2;
		}
	}

	/**
	 * Filtert nach Mandant
	 * 
	 * @return FilterKriterium[]
	 * @throws Throwable
	 */
	public FilterKriterium[] getDefaultFilterBestellungAuswahl()
			throws Throwable {
		FilterKriterium[] aFilterKrit = new FilterKriterium[1];
		aFilterKrit[0] = new FilterKriterium(
				BestellungFac.FLR_BESTELLUNG_MANDANT_C_NR, true, "'"
						+ LPMain.getTheClient().getMandant() + "'",
				FilterKriterium.OPERATOR_EQUAL, false);
		return aFilterKrit;
	}

	/**
	 * Default Filter Kriterien fuer eine Liste von Bestellungtexten.
	 * 
	 * @return FilterKriterium[] Default Filter Kriterien
	 * @throws Throwable
	 *             Ausnahme
	 */
	public FilterKriterium[] createFKBestellungtext() throws Throwable {
		FilterKriterium[] kriterien = new FilterKriterium[2];
		kriterien[0] = new FilterKriterium(
				BestellungServiceFac.FLR_BESTELLUNGTEXT_MANDANT_C_NR, true, "'"
						+ LPMain.getTheClient().getMandant() + "'",
				FilterKriterium.OPERATOR_EQUAL, false);
		kriterien[1] = new FilterKriterium(
				BestellungServiceFac.FLR_BESTELLUNGTEXT_LOCALE_C_NR, true, "'"
						+ LPMain.getTheClient().getLocUiAsString() + "'",
				FilterKriterium.OPERATOR_EQUAL, false);
		return kriterien;
	}

	/**
	 * Default Filter Kriterien fuer eine Liste von Mahntexten
	 * 
	 * @return FilterKriterium[] Default Filter Kriterien
	 * @throws Throwable
	 *             Ausnahme
	 */
	public FilterKriterium[] createFKMahntext() throws Throwable {
		FilterKriterium[] kriterien = new FilterKriterium[2];

		FilterKriterium krit1 = new FilterKriterium(
				BestellungServiceFac.FLR_MAHNTEXT_MANDANT_C_NR, true, "'"
						+ LPMain.getTheClient().getMandant() + "'",
				FilterKriterium.OPERATOR_EQUAL, false);
		kriterien[0] = krit1;

		FilterKriterium krit2 = new FilterKriterium(
				BestellungServiceFac.FLR_MAHNTEXT_LOCALE_C_NR, true, "'"
						+ LPMain.getTheClient().getLocUiAsString() + "'",
				FilterKriterium.OPERATOR_EQUAL, false);
		kriterien[0] = krit1;
		kriterien[1] = krit2;

		return kriterien;
	}

	public FilterKriterium[] createFKMahngruppe() throws Throwable {
		FilterKriterium[] kriterien = new FilterKriterium[1];

		FilterKriterium krit1 = new FilterKriterium(
				"flrartikelgruppe.mandant_c_nr", true, "'"
						+ LPMain.getTheClient().getMandant() + "'",
				FilterKriterium.OPERATOR_EQUAL, false);
		kriterien[0] = krit1;

		return kriterien;
	}

	/**
	 * Filterkriterien fuer FLR auf Panel Auftrag Auswahl. <br>
	 * - Auswahl nach Bestellnummer <br>
	 * - Auswahl nach Lieferant <br>
	 * - Auswahl nach Belegdatum <br>
	 * - Auswahl nach Liefertermin <br>
	 * - Auswahl nach Bestellwert
	 * 
	 * @return QueryType[]
	 */
	protected QueryType[] buildQueryTypesBestellungAuswahl() {
		QueryType[] types = new QueryType[5];

		FilterKriterium f1 = new FilterKriterium(
				BestellungFac.FLR_BESTELLUNG_FLRKOSTENSTELLE + ".c_nr", true,
				"", FilterKriterium.OPERATOR_LIKE, false);

		types[0] = new QueryType(LPMain
				.getTextRespectUISPr("label.kostenstelle"), f1,
				new String[] { FilterKriterium.OPERATOR_EQUAL }, true, true);

		FilterKriterium f3 = new FilterKriterium(
				BestellungFac.FLR_BESTELLUNG_T_BELEGDATUM, true, "",
				FilterKriterium.OPERATOR_EQUAL, false);

		types[1] = new QueryType(
				LPMain.getTextRespectUISPr("label.belegdatum"), f3,
				new String[] { FilterKriterium.OPERATOR_EQUAL,
						FilterKriterium.OPERATOR_GTE,
						FilterKriterium.OPERATOR_LTE }, true, // flrdate:
				// eingabeformat
				// 10.12.2004
				false, false);

		FilterKriterium f4 = new FilterKriterium(
				BestellungFac.FLR_BESTELLUNG_T_LIEFERTERMIN, true, "",
				FilterKriterium.OPERATOR_EQUAL, false);

		types[2] = new QueryType(LPMain.getTextRespectUISPr("lp.liefertermin"),
				f4, new String[] { FilterKriterium.OPERATOR_EQUAL,
						FilterKriterium.OPERATOR_GTE,
						FilterKriterium.OPERATOR_LTE }, true, false, false);

		FilterKriterium f5 = new FilterKriterium("flrauftrag.c_nr", true, "",
				FilterKriterium.OPERATOR_EQUAL, true);
		types[3] = new QueryType(
				// LPMain.getTextRespectUISPr("label.lieferant"),
				LPMain.getTextRespectUISPr("auft.auftrag"), f5,
				new String[] { FilterKriterium.OPERATOR_EQUAL }, true, true);

		FilterKriterium f6 = new FilterKriterium(
				"bestpos.flrlossollmaterial.flrlos.c_nr", true, "",
				FilterKriterium.OPERATOR_LIKE, false);

		types[4] = new QueryType(LPMain.getTextRespectUISPr("label.losnummer"),
				f6, new String[] { FilterKriterium.OPERATOR_EQUAL }, true, true);

		return types;
	}

	public FilterKriterium[] createArtikellieferantForThisArtikel(
			Integer artikelIId) throws Throwable {
		FilterKriterium[] krit = new FilterKriterium[1];

		krit[0] = new FilterKriterium(ArtikelFac.FLR_KATALOG_ARTIKEL_I_ID,
				true, artikelIId + "", FilterKriterium.OPERATOR_EQUAL, false);

		// JO 26.4.06 artikel ist jetzt mandantabh.
		// krit[1] = new FilterKriterium( //UW->JE der aktuelle Mandant...
		// "mandant_c_nr",
		// true,
		// "'" + LPMain.getInstance().getTheClient().getMandant() + "'",
		// FilterKriterium.OPERATOR_EQUAL,
		// false);

		return krit;
	}

	// public FilterKriterium[] createFKLieferant()
	// throws Throwable {
	// FilterKriterium[] krit = new FilterKriterium[1];
	//
	// krit[0] = new FilterKriterium(
	// "lieferant_i_id",
	// true,
	// "",
	// FilterKriterium.OPERATOR_IS + " " + FilterKriterium.OPERATOR_NOT_NULL,
	// false);
	//
	// return krit;
	// }

	public FilterKriterium[] createFKLieferantUndFKMandantCNR()
			throws Throwable {
		FilterKriterium[] krit = new FilterKriterium[2];

		krit[0] = new FilterKriterium(
				BestellvorschlagFac.FLR_BESTELLVORSCHLAG_LIEFERANT_I_ID, true,
				"", FilterKriterium.OPERATOR_IS + " "
						+ FilterKriterium.OPERATOR_NOT_NULL, false);

		FilterKriterium krit1 = new FilterKriterium(
				BestellvorschlagFac.FLR_BESTELLVORSCHLAG_MANDANT_C_NR, true,
				"'" + LPMain.getTheClient().getMandant() + "'",
				FilterKriterium.OPERATOR_EQUAL, false);

		krit[1] = krit1;

		return krit;
	}

	// public FilterKriterium[] createFKBestelldatum()
	// throws Throwable {
	// FilterKriterium[] krit = new FilterKriterium[1];
	//
	// krit[0] = new FilterKriterium(
	// "t_bestelltermin",
	// true,
	// "",
	// FilterKriterium.OPERATOR_IS + " " + FilterKriterium.OPERATOR_NOT_NULL,
	// false);
	//
	// return krit;
	// }

	public FilterKriterium[] createFKBestelldatumAndFKMandantCNR()
			throws Throwable {
		FilterKriterium[] krit = new FilterKriterium[2];

		krit[0] = new FilterKriterium(
				BestellvorschlagFac.FLR_BESTELLVORSCHLAG_T_LIEFERTERMIN, true,
				"", FilterKriterium.OPERATOR_IS + " "
						+ FilterKriterium.OPERATOR_NOT_NULL, false);

		FilterKriterium krit1 = new FilterKriterium(
				BestellvorschlagFac.FLR_BESTELLVORSCHLAG_MANDANT_C_NR, true,
				"'" + LPMain.getTheClient().getMandant() + "'",
				FilterKriterium.OPERATOR_EQUAL, false);

		krit[1] = krit1;

		return krit;
	}

	public FilterKriterium[] createFKAuftrag(Integer auftragIId)
			throws Throwable {
		FilterKriterium[] filters = new FilterKriterium[1];
		filters[0] = new FilterKriterium("auftrag_i_id", true, auftragIId + "",
				FilterKriterium.OPERATOR_EQUAL, false);
		return filters;
	}

	protected FilterKriterium[] getFKBestellvorschlagFilter3(
			Integer lieferantIId, String date) throws Throwable {

		FilterKriterium[] kriterien = null;

		kriterien = new FilterKriterium[2];

		kriterien[0] = new FilterKriterium(
				BestellungFac.FLR_BESTELLUNG_FLRLIEFERANT + "."
						+ LieferantFac.FLR_LIEFERANT_I_ID, true, "'"
						+ lieferantIId.toString() + "'",
				FilterKriterium.OPERATOR_EQUAL, false);
		kriterien[1] = new FilterKriterium(
				BestellvorschlagFac.FLR_BESTELLVORSCHLAG_T_LIEFERTERMIN, true,
				"'" + date + "'", FilterKriterium.OPERATOR_EQUAL, false);

		return kriterien;

	}

	protected FilterKriterium[] getFKBestellvorschlagFilter4(
			Integer lieferantIId) {
		FilterKriterium[] kriterium = null;

		kriterium = new FilterKriterium[1];

		kriterium[0] = new FilterKriterium(
				BestellungFac.FLR_BESTELLUNG_FLRLIEFERANT + "."
						+ LieferantFac.FLR_LIEFERANT_I_ID, true, lieferantIId
						.toString(), FilterKriterium.OPERATOR_EQUAL, false);

		return kriterium;

	}

	public FilterKriterium[] getFKBestellungenEinesLieferanten(
			Integer lieferantIId) throws Throwable {
		FilterKriterium[] fk = new FilterKriterium[2];
		fk[0] = SystemFilterFactory.getInstance().createFKMandantCNr()[0];
		fk[1] = new FilterKriterium(BestellungFac.FLR_BESTELLUNG_FLRLIEFERANT
				+ "." + LieferantFac.FLR_LIEFERANT_I_ID, true, "'"
				+ lieferantIId + "'", FilterKriterium.OPERATOR_EQUAL, false);
		return fk;
	}

	public SortierKriterium[] sortBestellvorschlag() {
		SortierKriterium krit[] = new SortierKriterium[1];

		krit[BestellvorschlagFac.SORT_KRITERIUM_BESTELLVORSCHLAG] = new SortierKriterium(
				BestellvorschlagFac.FLR_BESTELLVORSCHLAG_T_LIEFERTERMIN, true,
				"false");

		return krit;

	}

	public FilterKriterium[] createFKBestellpositionSichtRahmen(
			Integer iIdBestellungI) throws Throwable {
		FilterKriterium[] kriterien = null;
		if (iIdBestellungI != null) {
			kriterien = new FilterKriterium[2];
			kriterien[0] = new FilterKriterium(
					BestellpositionFac.FLR_BESTELLPOSITION_FLRBESTELLUNG + "."
							+ BestellungFac.FLR_BESTELLUNG_I_ID, true,
					iIdBestellungI.toString(), FilterKriterium.OPERATOR_EQUAL,
					false);
			kriterien[1] = new FilterKriterium(
					BestellpositionFac.FLR_BESTELLPOSITION_N_OFFENEMENGE, true,
					"", FilterKriterium.OPERATOR_IS + " "
							+ FilterKriterium.OPERATOR_NOT_NULL, false);
		}
		return kriterien;
	}

	public FilterKriterium[] createFKBestellpositionSichtRahmenAusAbruf(
			Integer iIdBestellungI) throws Throwable {
		FilterKriterium[] kriterien = null;
		if (iIdBestellungI != null) {
			kriterien = new FilterKriterium[2];
			kriterien[0] = new FilterKriterium(
					BestellpositionFac.FLR_BESTELLPOSITION_FLRBESTELLUNG + "."
							+ BestellungFac.FLR_BESTELLUNG_I_ID, true,
					iIdBestellungI.toString(), FilterKriterium.OPERATOR_EQUAL,
					false);
			kriterien[1] = new FilterKriterium(
					BestellpositionFac.FLR_BESTELLPOSITION_N_OFFENEMENGE, true,
					"", FilterKriterium.OPERATOR_IS + " "
							+ FilterKriterium.OPERATOR_NOT_NULL, false);
			/*
			 * kriterien[2] = new
			 * FilterKriterium(BestellpositionFac.FLR_BESTELLPOSITION_N_OFFENEMENGE
			 * , true, String.valueOf(0), FilterKriterium.OPERATOR_GT, false);
			 */
		}
		return kriterien;
	}

	public FilterKriterium[] getDefaultFilterBestellvorschlagLieferanten()
			throws Throwable {
		FilterKriterium[] kriterium = new FilterKriterium[1];
		FilterKriterium krit = new FilterKriterium(
				BestellvorschlagFac.FLR_BESTELLVORSCHLAG_FLRLIEFERANT + "."
						+ LieferantFac.FLR_LIEFERANT_I_ID, true, "",
				FilterKriterium.OPERATOR_NOT_EQUAL, false);
		kriterium[0] = krit;

		return kriterium;

	}

	public FilterKriterium[] createFKRahmenbestellungenEinesMandanten()
			throws Throwable {
		FilterKriterium[] fk = new FilterKriterium[3];

		fk[0] = SystemFilterFactory.getInstance().createFKMandantCNr()[0];
		fk[1] = new FilterKriterium(
				BestellungFac.FLR_BESTELLUNG_BESTELLUNGART_C_NR, true, "'"
						+ BestellungFac.BESTELLUNGART_RAHMENBESTELLUNG_C_NR
						+ "'", FilterKriterium.OPERATOR_EQUAL, false);

		fk[2] = new FilterKriterium(
				BestellungFac.FLR_BESTELLUNG_BESTELLUNGSTATUS_C_NR, true, "('"
						+ BestellungFac.BESTELLSTATUS_OFFEN + "','"
						+ BestellungFac.BESTELLSTATUS_BESTAETIGT + "')",
				FilterKriterium.OPERATOR_IN, false);

		// fk[2] = new
		// FilterKriterium(BestellungFac.FLRSPALTE_BESTELLSTATUS_C_NR,
		// true,
		// "('" + BestellungFac.BESTELLSTATUS_ANGELEGT + "')",
		// FilterKriterium.OPERATOR_NOT_IN,
		// false);
		// fk[3] = new
		// FilterKriterium(BestellungFac.FLRSPALTE_BESTELLSTATUS_C_NR,
		// true,
		// "('" + BestellungFac.BESTELLSTATUS_STORNIERT + "')",
		// FilterKriterium.OPERATOR_NOT_IN,
		// false);
		return fk;
	}

	public FilterKriterium[] createFKOffenBestellungenOhneRahmenEinesMandanten()
			throws Throwable {
		FilterKriterium[] fk = new FilterKriterium[3];

		fk[0] = SystemFilterFactory.getInstance().createFKMandantCNr()[0];
		fk[1] = new FilterKriterium(
				BestellungFac.FLR_BESTELLUNG_BESTELLUNGART_C_NR, true, "'"
						+ BestellungFac.BESTELLUNGART_RAHMENBESTELLUNG_C_NR
						+ "'", FilterKriterium.OPERATOR_NOT_EQUAL, false);

		fk[2] = new FilterKriterium(
				BestellungFac.FLR_BESTELLUNG_BESTELLUNGSTATUS_C_NR, true, "('"
						+ BestellungFac.BESTELLSTATUS_OFFEN + "','"
						+ BestellungFac.BESTELLSTATUS_TEILERLEDIGT + "')",
				FilterKriterium.OPERATOR_IN, false);

		return fk;
	}

	public FilterKriterium[] createFKOffenBestellungenOhneRahmenMitGelifertenEinesMandanten()
			throws Throwable {
		FilterKriterium[] fk = new FilterKriterium[3];

		fk[0] = SystemFilterFactory.getInstance().createFKMandantCNr()[0];
		fk[1] = new FilterKriterium(
				BestellungFac.FLR_BESTELLUNG_BESTELLUNGART_C_NR, true, "'"
						+ BestellungFac.BESTELLUNGART_RAHMENBESTELLUNG_C_NR
						+ "'", FilterKriterium.OPERATOR_NOT_EQUAL, false);

		fk[2] = new FilterKriterium(
				BestellungFac.FLR_BESTELLUNG_BESTELLUNGSTATUS_C_NR, true, "('"
						+ BestellungFac.BESTELLSTATUS_OFFEN + "','"
						+ BestellungFac.BESTELLSTATUS_TEILERLEDIGT + "','"
						+ BestellungFac.BESTELLSTATUS_GELIEFERT + "')",
				FilterKriterium.OPERATOR_IN, false);

		return fk;
	}

	public PanelQueryFLR createPanelFLRBestellung(InternalFrame internalFrameI,
			boolean bShowFilterButton, boolean bShowLeerenButton,
			FilterKriterium[] fkI, Integer bestellungIId) throws Throwable {
		String[] aWhichButtonIUse = SystemFilterFactory.getInstance()
				.createButtonArray(bShowFilterButton, bShowLeerenButton);

		QueryType[] qtAuswahl = buildQueryTypesBestellungAuswahl();
		if (fkI == null) {
			fkI = getDefaultFilterBestellungAuswahl();
		}
		PanelQueryFLR panelQueryFLRBestellung = new PanelQueryFLR(qtAuswahl,
				fkI, QueryParameters.UC_ID_BESTELLUNG, aWhichButtonIUse,
				internalFrameI, LPMain
						.getTextRespectUISPr("bes.liste.bestellungen"));
		panelQueryFLRBestellung.befuellePanelFilterkriterienDirekt(
				getFilterkriteriumDirekt1(), getFilterkriteriumDirekt2());
		if (bestellungIId != null) {
			panelQueryFLRBestellung.setSelectedId(bestellungIId);
		}
		return panelQueryFLRBestellung;
	}

	public FilterKriterium[] createFKWareneingangER(Integer eingangsrechnungIId) {
		FilterKriterium[] filters = new FilterKriterium[1];
		filters[0] = new FilterKriterium(
				WareneingangFac.FLR_WE_EINGANGSRECHNUNG_I_ID, true, ""
						+ eingangsrechnungIId, FilterKriterium.OPERATOR_EQUAL,
				false);
		return filters;
	}

	public PanelQueryFLR createPanelFLRBestellvorschlagAlleLieferanten(
			InternalFrame internalFrameI) throws Throwable {
		FilterKriterium[] filters = BestellungFilterFactory.getInstance()
				.createFKLieferantUndFKMandantCNR();
		PanelQueryFLR panelQueryBelegeinlieferant = new PanelQueryFLR(
				null,
				filters,
				QueryParameters.UC_ID_BESTELLVORSCHLAGALLELIEFERANTEN,
				null,
				internalFrameI,
				LPMain
						.getTextRespectUISPr("menueentry.lieferantendesbestellvorschlag"));
		panelQueryBelegeinlieferant.befuellePanelFilterkriterienDirekt(
				BestellungFilterFactory.getInstance()
						.createFKDLieferantPartnerName(),
				BestellungFilterFactory.getInstance()
						.createFKDLieferantPartnerOrt());
		return panelQueryBelegeinlieferant;
	}

	public FilterKriteriumDirekt createFKDProjekt() throws Throwable {
		return new FilterKriteriumDirekt("c_bezprojektbezeichnung", "",
				FilterKriterium.OPERATOR_LIKE, LPMain.getInstance()
						.getTextRespectUISPr("label.projekt"),
				FilterKriteriumDirekt.PROZENT_TRAILING, // Auswertung als '%XX'
				true, // wrapWithSingleQuotes
				true, // ignore case
				Facade.MAX_UNBESCHRAENKT);
	}

	public FilterKriteriumDirekt createFKDWepText() {
		return new FilterKriteriumDirekt("textsuche", "",
				FilterKriterium.OPERATOR_LIKE, LPMain.getInstance()
						.getTextRespectUISPr("label.text"),
				FilterKriteriumDirekt.PROZENT_BOTH, true, true,
				Facade.MAX_UNBESCHRAENKT);
	}

	public FilterKriteriumDirekt createFKDTextSuchen() throws Throwable {

		return new FilterKriteriumDirekt("c_suche", "",
				FilterKriterium.OPERATOR_LIKE, LPMain.getInstance()
						.getTextRespectUISPr("lp.textsuche"),
				FilterKriteriumDirekt.PROZENT_BOTH, // Auswertung als '%XX'
				true, // wrapWithSingleQuotes
				true, Facade.MAX_UNBESCHRAENKT); // ignore case
	}

	public PanelQueryFLR createPanelFLRWareneingang(
			InternalFrame internalFrameI, Integer bestellungIId)
			throws Throwable {
		FilterKriterium[] filters = BestellungFilterFactory.getInstance()
				.getDefaultFilterWareneingangQP4(bestellungIId);
		PanelQueryFLR panelQueryBelegeinlieferant = new PanelQueryFLR(null,
				filters, QueryParameters.UC_ID_BESTELLUNGWARENEINGANG, null,
				internalFrameI, LPMain
						.getTextRespectUISPr("menueentry.wareneingang"));
		return panelQueryBelegeinlieferant;
	}

	public PanelQueryFLR createPanelFLRBestellvorschlagBelegeInLieferant(
			InternalFrame internalFrameI) throws Throwable {
		PanelQueryFLR panelQueryBelegeinlieferanteinterminTermin = new PanelQueryFLR(
				null,
				BestellungFilterFactory.getInstance()
						.createFKBestelldatumAndFKMandantCNR(),
				QueryParameters.UC_ID_BESTELLVORSCHLAGALLETERMINE,
				null,
				internalFrameI,
				LPMain
						.getTextRespectUISPr("menueentry.terminedesbestellvorschlag"));
		return panelQueryBelegeinlieferanteinterminTermin;
	}
}
