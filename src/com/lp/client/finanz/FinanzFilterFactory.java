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
package com.lp.client.finanz;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.pc.LPMain;
import com.lp.client.system.SystemFilterFactory;
import com.lp.client.util.fastlanereader.gui.QueryType;
import com.lp.server.artikel.service.ArtikelFac;
import com.lp.server.eingangsrechnung.service.EingangsrechnungFac;
import com.lp.server.eingangsrechnung.service.ZahlungsvorschlaglaufDto;
import com.lp.server.finanz.service.FinanzFac;
import com.lp.server.finanz.service.FinanzServiceFac;
import com.lp.server.finanz.service.MahnlaufDto;
import com.lp.server.partner.service.BankFac;
import com.lp.server.partner.service.PartnerFac;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.SystemFac;
import com.lp.server.util.Facade;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.FilterKriteriumDirekt;
import com.lp.server.util.fastlanereader.service.query.FilterKriteriumSchnellansicht;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.util.Helper;

/**
 * <p>
 * Diese Klasse ist ein Singleton und kuemmert sich um alle Filter in der FiBu.
 * </p>
 * 
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2008
 * </p>
 * 
 * <p>
 * Erstellung: 29. 04. 2005
 * </p>
 * 
 * <p>
 * 
 * @author Martin Bluehweis
 *         </p>
 * 
 * @version $Revision: 1.19 $ Date $Date: 2012/12/05 14:10:28 $
 */

@SuppressWarnings("static-access")
public class FinanzFilterFactory {

	private static FinanzFilterFactory filterFactory = null;

	private FinanzFilterFactory() {
		// Singleton.
	}

	/**
	 * Hole das Singelton EingangsrechnungFilterFactory.
	 * 
	 * @return EingangsrechnungFilterFactory
	 */
	static public FinanzFilterFactory getInstance() {
		if (filterFactory == null) {
			filterFactory = new FinanzFilterFactory();
		}
		return filterFactory;
	}

	/**
	 * Direktes Filter Kriterium Kontonummer fuer das PanelQueryKonto.
	 * 
	 * @return FilterKriteriumDirekt
	 * @throws Throwable
	 */
	public FilterKriteriumDirekt createFKDKontonummer() throws Throwable {
		return new FilterKriteriumDirekt(FinanzFac.FLR_KONTO_C_NR, "",
				FilterKriterium.OPERATOR_LIKE,
				LPMain.getTextRespectUISPr("finanz.kontonummer"),
				FilterKriteriumDirekt.PROZENT_TRAILING, // Auswertung als 'XX%'
				true, // wrapWithSingleQuotes
				false, Facade.MAX_UNBESCHRAENKT);
	}

	public FilterKriterium[] createFKKontoKey(Integer artikelIId)
			throws Throwable {
		FilterKriterium[] kriterien = new FilterKriterium[1];
		kriterien[0] = new FilterKriterium("i_id", true, artikelIId + "",
				FilterKriterium.OPERATOR_EQUAL, false);

		return kriterien;
	}

	/**
	 * Direktes Filter Kriterium Kontonummer fuer das PanelQueryKonto.
	 * 
	 * @return FilterKriteriumDirekt
	 * @throws Throwable
	 */
	public FilterKriteriumDirekt createFKDWechselkursWaehrung()
			throws Throwable {
		return new FilterKriteriumDirekt(
				SystemFac.FLR_WECHSELKURS_WAEHRUNG_C_NR_ZU, "",
				FilterKriterium.OPERATOR_LIKE,
				LPMain.getTextRespectUISPr("label.waehrung"),
				FilterKriteriumDirekt.PROZENT_TRAILING, // Auswertung als 'XX%'
				true, // wrapWithSingleQuotes
				true, Facade.MAX_UNBESCHRAENKT);
	}

	/**
	 * Direktes Filter Kriterium Kontobezeichnung fuer das PanelQueryKonto.
	 * 
	 * @return FilterKriteriumDirekt
	 * @throws Throwable
	 */
	public FilterKriteriumDirekt createFKDKontobezeichnung() throws Throwable {
		return new FilterKriteriumDirekt(FinanzFac.FLR_KONTO_C_BEZ, "",
				FilterKriterium.OPERATOR_LIKE,
				LPMain.getTextRespectUISPr("lp.bezeichnung"),
				FilterKriteriumDirekt.PROZENT_TRAILING, // Auswertung als 'XX%'
				true, // wrapWithSingleQuotes
				true, Facade.MAX_UNBESCHRAENKT);
	}

	/**
	 * Direktes Filter Kriterium Kontonummer fuer das PanelQueryKonto.
	 * 
	 * @return FilterKriteriumDirekt
	 * @throws Throwable
	 */
	public FilterKriteriumDirekt createFKDKassenbuchbezeichnung()
			throws Throwable {
		return new FilterKriteriumDirekt(FinanzFac.FLR_KASSENBUCH_C_BEZ, "",
				FilterKriterium.OPERATOR_LIKE,
				LPMain.getTextRespectUISPr("lp.bezeichnung"),
				FilterKriteriumDirekt.PROZENT_TRAILING, // Auswertung als 'XX%'
				true, // wrapWithSingleQuotes
				true, Facade.MAX_UNBESCHRAENKT);
	}

	public FilterKriterium[] createFKSummengruppe(Integer ergebnisgruppeIId,
			boolean bBilanzgruppe) throws Throwable {

		FilterKriterium[] kriterien = null;

		if (ergebnisgruppeIId != null) {
			kriterien = new FilterKriterium[3];
			kriterien[0] = new FilterKriterium("mandant_c_nr", true, "'"
					+ LPMain.getInstance().getTheClient().getMandant() + "'",
					FilterKriterium.OPERATOR_EQUAL, false);
			kriterien[1] = new FilterKriterium("i_id", true, "("
					+ ergebnisgruppeIId + ")", FilterKriterium.OPERATOR_NOT_IN,
					false);

			if (bBilanzgruppe == true) {

				kriterien[2] = new FilterKriterium(
						FinanzFac.FLR_ERGEBNISGRUPPE_B_BILANZGRUPPE, true, "1",
						FilterKriterium.OPERATOR_EQUAL, false);

			} else {
				kriterien[2] = new FilterKriterium(
						FinanzFac.FLR_ERGEBNISGRUPPE_B_BILANZGRUPPE, true, "0",
						FilterKriterium.OPERATOR_EQUAL, false);

			}

		} else {
			kriterien = new FilterKriterium[2];
			kriterien[0] = new FilterKriterium("mandant_c_nr", true, "'"
					+ LPMain.getInstance().getTheClient().getMandant() + "'",
					FilterKriterium.OPERATOR_EQUAL, false);

			if (bBilanzgruppe == true) {

				kriterien[1] = new FilterKriterium(
						FinanzFac.FLR_ERGEBNISGRUPPE_B_BILANZGRUPPE, true, "1",
						FilterKriterium.OPERATOR_EQUAL, false);

			} else {
				kriterien[1] = new FilterKriterium(
						FinanzFac.FLR_ERGEBNISGRUPPE_B_BILANZGRUPPE, true, "0",
						FilterKriterium.OPERATOR_EQUAL, false);

			}

		}

		return kriterien;
	}

	public FilterKriterium[] createFKErgebnisgruppe(boolean bBilanzgruppe)
			throws Throwable {

		FilterKriterium[] kriterien = new FilterKriterium[2];

		kriterien[0] = new FilterKriterium("mandant_c_nr", true, "'"
				+ LPMain.getInstance().getTheClient().getMandant() + "'",
				FilterKriterium.OPERATOR_EQUAL, false);

		if (bBilanzgruppe == true) {

			kriterien[1] = new FilterKriterium(
					FinanzFac.FLR_ERGEBNISGRUPPE_B_BILANZGRUPPE, true, "1",
					FilterKriterium.OPERATOR_EQUAL, false);

		} else {
			kriterien[1] = new FilterKriterium(
					FinanzFac.FLR_ERGEBNISGRUPPE_B_BILANZGRUPPE, true, "0",
					FilterKriterium.OPERATOR_EQUAL, false);

		}

		return kriterien;
	}

	public FilterKriterium[] createFKBilanzgruppenNegativ() throws Throwable {

		FilterKriterium[] kriterien = new FilterKriterium[3];

		kriterien[0] = new FilterKriterium("mandant_c_nr", true, "'"
				+ LPMain.getInstance().getTheClient().getMandant() + "'",
				FilterKriterium.OPERATOR_EQUAL, false);

		kriterien[1] = new FilterKriterium(
				FinanzFac.FLR_ERGEBNISGRUPPE_B_BILANZGRUPPE, true, "1",
				FilterKriterium.OPERATOR_EQUAL, false);

		kriterien[2] = new FilterKriterium(FinanzFac.FLR_ERGEBNISGRUPPE_I_TYP,
				true, FinanzFac.ERGEBNISGRUPPE_TYP_BILANZGRUPPE_NEGATIV + "",
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
				FinanzServiceFac.FLR_MAHNTEXT_MANDANT_C_NR, true, "'"
						+ LPMain.getTheClient().getMandant() + "'",
				FilterKriterium.OPERATOR_EQUAL, false);
		kriterien[0] = krit1;

		FilterKriterium krit2 = new FilterKriterium(
				FinanzServiceFac.FLR_MAHNTEXT_LOCALE_C_NR, true, "'"
						+ LPMain.getTheClient().getLocUiAsString() + "'",
				FilterKriterium.OPERATOR_EQUAL, false);
		kriterien[0] = krit1;
		kriterien[1] = krit2;

		return kriterien;
	}

	public FilterKriterium[] createFKMahnspesen(String waehrungCNr,
			String mandantCNr) throws Throwable {
		FilterKriterium[] kriterien = new FilterKriterium[2];
		kriterien[0] = new FilterKriterium("waehrung_c_nr", true, "'"
				+ waehrungCNr + "'" + "", FilterKriterium.OPERATOR_EQUAL, false);
		kriterien[1] = new FilterKriterium("mandant_c_nr", true, "'"
				+ mandantCNr + "'" + "", FilterKriterium.OPERATOR_EQUAL, false);
		return kriterien;
	}

	public FilterKriterium[] createFKMahnung(MahnlaufDto mahnlaufDto) {
		FilterKriterium[] filter = null;
		Integer mahnlaufIId = null;
		if (mahnlaufDto != null) {
			mahnlaufIId = mahnlaufDto.getIId();
		} else {
			// keine anzeigen
			mahnlaufIId = new Integer(-1);
		}

		if (mahnlaufIId != null) {
			filter = new FilterKriterium[1];
			filter[0] = new FilterKriterium(
					FinanzFac.FLR_MAHNUNG_MAHNLAUF_I_ID, true, "'"
							+ mahnlaufIId.toString() + "'",
					FilterKriterium.OPERATOR_EQUAL, false);
		}
		return filter;
	}

	/**
	 * Filter auf Waehrung
	 * @param waehrungCNr 
	 * 
	 * @return FilterKriterium[] Default Filter Kriterien
	 * @throws Throwable
	 *             Ausnahme
	 */
	public FilterKriterium[] createFKWechselkurs(String waehrungCNr)
			throws Throwable {
		FilterKriterium[] kriterien = new FilterKriterium[1];

		FilterKriterium krit1 = new FilterKriterium(
				FinanzFac.FILTER_WECHSELKURS_VON_ZU, true, "'" + waehrungCNr
						+ "'", FilterKriterium.OPERATOR_EQUAL, false);
		kriterien[0] = krit1;
		return kriterien;
	}

	public FilterKriterium[] createFKSachkonten() throws Throwable {
		return this.createFKKonten(FinanzServiceFac.KONTOTYP_SACHKONTO);
	}

	public FilterKriterium[] createFKSachkontenInklMitlaufende() throws Throwable {
		return this.createFKKontenInklMitlaufende(FinanzServiceFac.KONTOTYP_SACHKONTO);
	}
	
	/**
	 * Dieser Filter entfernt alle Ust- und Vstkonten aus dem FLR, welche bei bei anderen MwstS&auml;tzen als den angegebenen
	 * in einer Steuerkategorie hinterlegt sind. Nicht zugewiesene Steuerkonten werden nicht gefiltert.
	 * Konten welche nicht von der Art UstKonto bzw VstKonto sind, werden ebenfalls gefiltert. 
	 * 
	 * @param mwstSatzBezIId die Mwst, in denen die Ust- und Vstkonten hinterlegt sein d&uuml;rfen
	 * @return die Filterkriterien
	 * @throws Throwable
	 */
	protected List<FilterKriterium> createFKKontenInMwstSatzBez(Integer mwstSatzBezIId) throws Throwable {
		List<FilterKriterium> list = new ArrayList<FilterKriterium>(Arrays.asList(createFKSachkonten()));
		list.add(new FilterKriterium(FinanzFac.FILTER_KONTO_OHNE_UST_VST_KONTEN_AUSSER_MWSTSATZBEZ, true, mwstSatzBezIId.toString(), "", false));
		return list;
	}
	
	protected FilterKriterium createFKKontoart(String... kontoarten) {
		return new FilterKriterium(FinanzFac.FLR_KONTO_FLRKONTOART, true,
				Helper.arrayToSqlInList(kontoarten),
				FilterKriterium.OPERATOR_IN, false);
	}
	
	public FilterKriterium[] createFKVstKontenInMwstSatzBez(Integer mwstSatzBezIId) throws Throwable {
		List<FilterKriterium> list = createFKKontenInMwstSatzBez(mwstSatzBezIId);
		list.add(createFKKontoart(FinanzServiceFac.KONTOART_VST));
		return list.toArray(new FilterKriterium[0]);
	}
	
	public FilterKriterium[] createFKUstKontenInMwstSatzBez(Integer mwstSatzBezIId) throws Throwable {
		List<FilterKriterium> list = createFKKontenInMwstSatzBez(mwstSatzBezIId);
		list.add(createFKKontoart(FinanzServiceFac.KONTOART_UST));
		return list.toArray(new FilterKriterium[0]);
	}
	
	public FilterKriterium[] createFKSteuerkontenInMwstSatzBez(Integer mwstSatzBezIId) throws Throwable {
		List<FilterKriterium> list = createFKKontenInMwstSatzBez(mwstSatzBezIId);
		list.add(createFKKontoart(FinanzServiceFac.KONTOART_UST, FinanzServiceFac.KONTOART_VST));
		return list.toArray(new FilterKriterium[0]);
	}

	public FilterKriterium[] createFKDebitorenkonten() throws Throwable {
		return this.createFKKonten(FinanzServiceFac.KONTOTYP_DEBITOR);
	}

	public FilterKriterium[] createFKKreditorenkonten() throws Throwable {
		return this.createFKKonten(FinanzServiceFac.KONTOTYP_KREDITOR);
	}

	public FilterKriterium createFKKontonummerGroesserAls(String sKontonummer)
			throws Throwable {
		return new FilterKriterium(FinanzFac.FLR_KONTO_C_NR, true, "'"
				+ sKontonummer + "'", FilterKriterium.OPERATOR_GTE, false);
	}

	public FilterKriterium createFKKontonummerKleinerAls(String sKontonummer)
			throws Throwable {
		return new FilterKriterium(FinanzFac.FLR_KONTO_C_NR, true, "'"
				+ sKontonummer + "'", FilterKriterium.OPERATOR_LTE, false);
	}
	
	/**
	 * Filter auf Kontotyp.&nbsp;
	 * Bei Sachkonten: Mitlaufende Konten werden nicht angezeigt.
	 * @param kontotypCNr
	 * @return die Filterkriterien
	 * @throws Throwable
	 */
	public FilterKriterium[] createFKKonten(String kontotypCNr) throws Throwable {
		return createFKKonten(kontotypCNr, false);
	}

	/**
	 * Filter auf Kontotyp.&nbsp;
	 * @param kontotypCNr
	 * @return die Filterkriterien
	 * @throws Throwable
	 */
	public FilterKriterium[] createFKKontenInklMitlaufende(String kontotypCNr) throws Throwable {
		return createFKKonten(kontotypCNr, true);
	}
	
	private FilterKriterium[] createFKKonten(String kontotypCNr, boolean mitlaufende)
			throws Throwable {
		List<FilterKriterium> filters = new ArrayList<FilterKriterium>();
		filters.add(new FilterKriterium(FinanzFac.FLR_KONTO_KONTOTYP_C_NR,
				true, "'" + kontotypCNr + "'", FilterKriterium.OPERATOR_EQUAL,
				false));
		filters.add(new FilterKriterium(FinanzFac.FLR_KONTO_MANDANT_C_NR,
				true, "'" + LPMain.getTheClient().getMandant() + "'",
				FilterKriterium.OPERATOR_EQUAL, false));
		if(!mitlaufende)
			filters.add(createFKKontenOhneMitlaufende());
		return filters.toArray(new FilterKriterium[0]);
	}
	
	public FilterKriterium createFKKontenOhneMitlaufende() throws Throwable {
		return new FilterKriterium(FinanzFac.FILTER_KONTO_OHNE_MITLAUFENDE, true, "", "", false);
	}

	public FilterKriterium[] createFKBuchungDetail(Integer kontoIId)
			throws Throwable {
		FilterKriterium[] filter = new FilterKriterium[1];
		filter[0] = new FilterKriterium(FinanzFac.FLR_BUCHUNGDETAIL_KONTO_I_ID,
				true, "'" + kontoIId.toString() + "'",
				FilterKriterium.OPERATOR_EQUAL, false);
		return filter;
	}

	public FilterKriterium[] createFKBuchungDetailGegenkonto(
			Integer gegenkontoIId) throws Throwable {
		FilterKriterium[] filter = new FilterKriterium[1];
		filter[0] = new FilterKriterium(
				FinanzFac.FLR_BUCHUNGDETAIL_FLRGEGENKONTO + ".i_id", true, "'"
						+ gegenkontoIId.toString() + "'",
				FilterKriterium.OPERATOR_EQUAL, false);
		return filter;
	}

	public FilterKriterium[] createFKBuchungsdetail(Integer buchungIId)
			throws Throwable {
		FilterKriterium[] filter = new FilterKriterium[1];
		filter[0] = new FilterKriterium(
				FinanzFac.FLR_BUCHUNGDETAIL_BUCHUNG_I_ID, true,
				buchungIId + "", FilterKriterium.OPERATOR_EQUAL, false);
		return filter;
	}

	public FilterKriterium[] createFKKontolaenderart(Integer kontoIId)
			throws Throwable {
		FilterKriterium[] filter = new FilterKriterium[1];
		filter[0] = new FilterKriterium(FinanzFac.FLR_KONTOLAENDERART_ID_COMP
				+ "." + FinanzFac.FLR_KONTOLAENDERART_KONTO_I_ID, true, "'"
				+ kontoIId.toString() + "'", FilterKriterium.OPERATOR_EQUAL,
				false);
		return filter;
	}

	public FilterKriterium[] createFKKontoland(Integer kontoIId)
			throws Throwable {
		FilterKriterium[] filter = new FilterKriterium[1];
		filter[0] = new FilterKriterium(FinanzFac.FLR_KONTOLAND_ID_COMP + "."
				+ FinanzFac.FLR_KONTOLAND_KONTO_I_ID, true, "'"
				+ kontoIId.toString() + "'", FilterKriterium.OPERATOR_EQUAL,
				false);
		return filter;
	}

	protected QueryType[] createQTBankverbindung() throws Throwable {
		QueryType[] types = new QueryType[2];
		// Suche nach Bezeichnung
		FilterKriterium f1 = new FilterKriterium(FinanzFac.FLR_BANKKONTO_C_BEZ,
				true, "", FilterKriterium.OPERATOR_LIKE, false);

		types[0] = new QueryType(LPMain.getTextRespectUISPr("lp.bezeichnung"),
				f1, new String[] { FilterKriterium.OPERATOR_EQUAL }, true, true);
		// Suche nach Bezeichnung
		FilterKriterium f2 = new FilterKriterium(
				FinanzFac.FLR_BANKKONTO_C_KONTONUMMER, true, "",
				FilterKriterium.OPERATOR_LIKE, false);

		types[1] = new QueryType(
				LPMain.getTextRespectUISPr("finanz.kontonummer"), f2,
				new String[] { FilterKriterium.OPERATOR_EQUAL }, true, true);
		return types;
	}

	protected QueryType[] createQTBuchungDetail() {
		QueryType[] types = new QueryType[5];
		// Suche nach Kontonummer
		FilterKriterium f1 = new FilterKriterium(
				FinanzFac.FLR_BUCHUNGDETAIL_FLRGEGENKONTO + "."
						+ FinanzFac.FLR_KONTO_C_NR, true, "",
				FilterKriterium.OPERATOR_LIKE, false);

		types[0] = new QueryType(
				LPMain.getTextRespectUISPr("finanz.gegenkonto"), f1,
				new String[] { FilterKriterium.OPERATOR_EQUAL }, true, true);
		FilterKriterium f2 = new FilterKriterium(
				FinanzFac.FLR_BUCHUNGDETAIL_FLRBUCHUNG + "."
						+ FinanzFac.FLR_BUCHUNG_C_TEXT, true, "",
				FilterKriterium.OPERATOR_LIKE, false);

		types[1] = new QueryType(LPMain.getTextRespectUISPr("label.text"), f2,
				new String[] { FilterKriterium.OPERATOR_EQUAL }, true, true);

		FilterKriterium f3 = new FilterKriterium(
				FinanzFac.FLR_BUCHUNGDETAIL_N_BETRAG, true, "",
				FilterKriterium.OPERATOR_LIKE, false);

		types[2] = new QueryType(LPMain.getTextRespectUISPr("finanz.soll"), f3,
				new String[] { FilterKriterium.OPERATOR_EQUAL,
						FilterKriterium.OPERATOR_GT,
						FilterKriterium.OPERATOR_GTE,
						FilterKriterium.OPERATOR_LT,
						FilterKriterium.OPERATOR_LTE }, true, true);

		FilterKriterium f4 = new FilterKriterium(
				FinanzFac.FLR_BUCHUNGDETAIL_N_BETRAG, true, "",
				FilterKriterium.OPERATOR_LIKE, false);

		types[3] = new QueryType(LPMain.getTextRespectUISPr("finanz.haben"),
				f4, new String[] { FilterKriterium.OPERATOR_EQUAL,
						FilterKriterium.OPERATOR_GT,
						FilterKriterium.OPERATOR_GTE,
						FilterKriterium.OPERATOR_LT,
						FilterKriterium.OPERATOR_LTE }, true, true);

		FilterKriterium f5 = new FilterKriterium(
				FinanzFac.FLR_BUCHUNGDETAIL_N_UST, true, "",
				FilterKriterium.OPERATOR_LIKE, false);

		types[4] = new QueryType(LPMain.getTextRespectUISPr("label.mwst"), f5,
				new String[] { FilterKriterium.OPERATOR_EQUAL,
						FilterKriterium.OPERATOR_GT,
						FilterKriterium.OPERATOR_GTE,
						FilterKriterium.OPERATOR_LT,
						FilterKriterium.OPERATOR_LTE }, true, true);
		return types;
	}

	protected QueryType[] createQTBuchungen() {
		QueryType[] types = new QueryType[2];

		FilterKriterium f1 = new FilterKriterium(
				FinanzFac.FLR_BUCHUNG_BUCHUNGSART_C_NR, true, "''",
				FilterKriterium.OPERATOR_LIKE, false);

		types[0] = new QueryType(
				LPMain.getTextRespectUISPr("finanz.buchungsart"), f1,
				new String[] { FilterKriterium.OPERATOR_EQUAL }, true, true);
		FilterKriterium f2 = new FilterKriterium(FinanzFac.FLR_BUCHUNG_C_TEXT,
				true, "''", FilterKriterium.OPERATOR_LIKE, false);

		types[1] = new QueryType(LPMain.getTextRespectUISPr("lp.text"), f2,
				new String[] { FilterKriterium.OPERATOR_EQUAL }, true, true);
		return types;
	}

	protected QueryType[] createQTBuchungsjournale() {
		QueryType[] types = new QueryType[1];
		FilterKriterium f1 = new FilterKriterium(
				FinanzFac.FLR_BUCHUNGSJOURNAL_T_ANLEGEN, true, "''",
				FilterKriterium.OPERATOR_LIKE, false);
		types[0] = new QueryType(LPMain.getTextRespectUISPr("lp.datum"), f1,
				new String[] { FilterKriterium.OPERATOR_EQUAL }, true, true);
		return types;
	}

	public FilterKriterium[] createFKBuchungsregelGegenkonten(
			Integer buchungsregelIId) {
		FilterKriterium[] filters = new FilterKriterium[1];
		filters[0] = new FilterKriterium(
				FinanzFac.FLR_BUCHUNGSREGELGEGENKONTO_BUCHUNGSREGEL_I_ID, true,
				"'" + buchungsregelIId.toString() + "'",
				FilterKriterium.OPERATOR_EQUAL, false);
		return filters;
	}

	public FilterKriterium[] createFKKontenEinerErgebnisgruppe(
			Integer ergebnisgruppeIId) throws Throwable {
		FilterKriterium[] filters = new FilterKriterium[2];
		filters[0] = SystemFilterFactory.getInstance().createFKMandantCNr()[0];
		filters[1] = new FilterKriterium(
				FinanzFac.FLR_KONTO_ERGEBNISGRUPPE_I_ID, true, "'"
						+ ergebnisgruppeIId.intValue() + "'",
				FilterKriterium.OPERATOR_EQUAL, false);
		return filters;
	}

	public PanelQueryFLR createPanelFLRBankverbindung(
			InternalFrame internalFrameI, boolean bShowNewButton,
			boolean bShowLeerenButton) throws Throwable {
		// ffcreatespanel: panel kreieren
		boolean[] b = new boolean[2];
		b[0] = bShowNewButton;
		b[1] = bShowLeerenButton;
		String[] aWhichButtonIUse = null;
		switch (Helper.booleanArray2int(b)) {
		case 0: {
			aWhichButtonIUse = new String[] { PanelBasis.ACTION_FILTER };
		}
			break;
		case 1: {
			aWhichButtonIUse = new String[] { PanelBasis.ACTION_NEW,
					PanelBasis.ACTION_FILTER };
		}
			break;
		case 2: {
			aWhichButtonIUse = new String[] { PanelBasis.ACTION_FILTER,
					PanelBasis.ACTION_LEEREN, };
		}
			break;
		case 3: {
			aWhichButtonIUse = new String[] { PanelBasis.ACTION_NEW,
					PanelBasis.ACTION_FILTER, PanelBasis.ACTION_LEEREN };
		}
			break;
		}
		
		
		PanelQueryFLR panelQueryBankverbindung = new PanelQueryFLR(
				createQTBankverbindung(), SystemFilterFactory.getInstance()
						.createFKMandantCNr(), QueryParameters.UC_ID_BANKKONTO,
				aWhichButtonIUse, internalFrameI,
				LPMain.getTextRespectUISPr("finanz.liste.bankverbindungen"),FinanzFilterFactory.getInstance().createFKVBankverbindung(),null);
		panelQueryBankverbindung.befuellePanelFilterkriterienDirekt(
				createFKDBankverbindungBank(),
				createFKDBankverbindungKontonummer());
		return panelQueryBankverbindung;
	}

	public PanelQueryFLR createPanelFLRGutschriftER(
			InternalFrame internalFrameI, Integer lieferantIId)
			throws Throwable {
		// ffcreatespanel: panel kreieren
		PanelQueryFLR panelQueryGutschrift = new PanelQueryFLR(null,
				getFKGutschriftEr(lieferantIId),
				QueryParameters.UC_ID_EINGANGSRECHNUNG, new String[] {},
				internalFrameI,
				LPMain.getTextRespectUISPr("rechnung.gutschrift"));
		return panelQueryGutschrift;
	}

	public PanelQueryFLR createPanelFLRGutschriftRE(
			InternalFrame internalFrameI, Integer kundeIId) throws Throwable {
		// ffcreatespanel: panel kreieren
		PanelQueryFLR panelQueryGutschrift = new PanelQueryFLR(null,
				getFKGutschriftRe(kundeIId), QueryParameters.UC_ID_RECHNUNG,
				new String[] {}, internalFrameI,
				LPMain.getTextRespectUISPr("rechnung.gutschrift"));
		return panelQueryGutschrift;
	}

	private FilterKriterium[] getFKGutschriftEr(Integer lieferantIId)
			throws Throwable {
		FilterKriterium[] kriterien = new FilterKriterium[4];
		// Nur die Eingangsrechnungen des Mandanten
		kriterien[0] = new FilterKriterium("mandant_c_nr", true, "'"
				+ LPMain.getInstance().getTheClient().getMandant() + "'",
				FilterKriterium.OPERATOR_EQUAL, false);
		// Nur Gutschriften
		kriterien[1] = new FilterKriterium("eingangsrechnungart_c_nr", true,
				"'Gutschrift'", FilterKriterium.OPERATOR_EQUAL, true);
		// nur Gutschriften die angelegt oder Teilbezahlt sind sind
		kriterien[2] = new FilterKriterium("status_c_nr", true, "'Erledigt'",
				FilterKriterium.OPERATOR_NOT_EQUAL, false);
		// nur Gutschriften des Lieferanten
		kriterien[3] = new FilterKriterium("flrlieferant.i_id", true,
				lieferantIId.toString(), FilterKriterium.OPERATOR_EQUAL, false);
		return kriterien;
	}

	private FilterKriterium[] getFKGutschriftRe(Integer kundeIId)
			throws Throwable {
		FilterKriterium[] kriterien = new FilterKriterium[4];
		// Nur die Eingangsrechnungen des Mandanten
		kriterien[0] = new FilterKriterium("mandant_c_nr", true, "'"
				+ LPMain.getInstance().getTheClient().getMandant() + "'",
				FilterKriterium.OPERATOR_EQUAL, false);
		// Nur Gutschriften
		kriterien[1] = new FilterKriterium("flrrechnungart.rechnungtyp_c_nr",
				true, "'Gutschrift'", FilterKriterium.OPERATOR_EQUAL, true);
		// nur Gutschriften die angelegt oder Teilbezahlt sind sind
		kriterien[2] = new FilterKriterium("status_c_nr", true, "('Offen', 'Teilbezahlt')",
				FilterKriterium.OPERATOR_IN, true);
		// nur Gutschriften des Lieferanten
		kriterien[3] = new FilterKriterium("flrkunde.i_id", true,
				kundeIId.toString(), FilterKriterium.OPERATOR_EQUAL, false);
		return kriterien;
	}

	/**
	 * Direktes Filter Kriterium.
	 * 
	 * @return FilterKriteriumDirekt
	 * @throws Throwable
	 */
	protected FilterKriteriumDirekt createFKDBankverbindungBank()
			throws Throwable {
		return new FilterKriteriumDirekt(FinanzFac.FLR_BANKKONTO_FLRBANK + "."
				+ BankFac.FLR_PARTNERBANK_FLRPARTNER + "."
				+ PartnerFac.FLR_PARTNER_NAME1NACHNAMEFIRMAZEILE1, "",
				FilterKriterium.OPERATOR_LIKE,
				LPMain.getTextRespectUISPr("lp.bank"),
				FilterKriteriumDirekt.PROZENT_TRAILING, // Auswertung als 'XX%'
				true, true, Facade.MAX_UNBESCHRAENKT); // wrapWithSingleQuotes
	}

	/**
	 * Direktes Filter Kriterium.
	 * 
	 * @return FilterKriteriumDirekt
	 * @throws Throwable
	 */
	protected FilterKriteriumDirekt createFKDBankverbindungKontonummer()
			throws Throwable {
		return new FilterKriteriumDirekt(FinanzFac.FLR_BANKKONTO_C_KONTONUMMER,
				"", FilterKriterium.OPERATOR_LIKE,
				LPMain.getTextRespectUISPr("finanz.kontonummer"),
				FilterKriteriumDirekt.PROZENT_TRAILING, // Auswertung als 'XX%'
				true, false, Facade.MAX_UNBESCHRAENKT); // wrapWithSingleQuotes
	}

	public FilterKriterium[] createFKExportdaten(Integer exportlaufIId)
			throws Throwable {
		FilterKriterium[] filter = new FilterKriterium[1];
		filter[0] = new FilterKriterium(FinanzFac.FLR_EXPORTDATEN_FLREXPORTLAUF
				+ "." + FinanzFac.FLR_EXPORTLAUF_I_ID, true, "'"
				+ exportlaufIId.toString() + "'",
				FilterKriterium.OPERATOR_EQUAL, false);
		return filter;
	}

	public PanelQueryFLR createPanelFLRLaenderarten(InternalFrame internalFrameI)
			throws Throwable {
		PanelQueryFLR panelQueryLaenderarten = new PanelQueryFLR(null, null,
				QueryParameters.UC_ID_LAENDERART, null, internalFrameI,
				LPMain.getTextRespectUISPr("lp.auswahl"));
		return panelQueryLaenderarten;
	}

	private FilterKriterium[] buildFiltersErloesKonto() throws Throwable {
		FilterKriterium[] filter = null;
		filter = new FilterKriterium[2];

		filter[0] = new FilterKriterium(FinanzFac.FLR_KONTO_KONTOTYP_C_NR,
				true, "'" + FinanzServiceFac.KONTOTYP_SACHKONTO + "'",
				FilterKriterium.OPERATOR_EQUAL, false);
		filter[1] = new FilterKriterium(FinanzFac.FLR_KONTO_MANDANT_C_NR, true,
				"'" + LPMain.getInstance().getTheClient().getMandant() + "'",
				FilterKriterium.OPERATOR_EQUAL, false);
		return filter;
	}

	public PanelQueryFLR createPanelFLRFinanzKonto(
			InternalFrame internalFrameI, Integer selectedId,
			boolean bShowLeerenButton) throws Throwable {
		FilterKriterium[] filters = buildFiltersErloesKonto();

		String[] aWhichButtonIUse = null;
		if (bShowLeerenButton == true) {
			aWhichButtonIUse = new String[] { PanelBasis.ACTION_LEEREN };

		}

		PanelQueryFLR panelQueryFLRKontoerloese = new PanelQueryFLR(null,
				filters, QueryParameters.UC_ID_FINANZKONTEN, aWhichButtonIUse,
				internalFrameI, LPMain.getInstance().getTextRespectUISPr(
						"finanz.erloesekonten"));
		FilterKriteriumDirekt fkDirekt1 = FinanzFilterFactory.getInstance()
				.createFKDKontonummer();
		FilterKriteriumDirekt fkDirekt2 = FinanzFilterFactory.getInstance()
				.createFKDKontobezeichnung();
		panelQueryFLRKontoerloese.befuellePanelFilterkriterienDirekt(fkDirekt1,
				fkDirekt2);
		// vorbesetzen
		if (selectedId != null) {
			panelQueryFLRKontoerloese.setSelectedId(selectedId);
		}
		return panelQueryFLRKontoerloese;
	}

	public PanelQueryFLR createPanelFLRFinanzamt(InternalFrame internalFrameI,
			Integer selectedId, boolean bShowLeerenButton) throws Throwable {

		String[] aWhichButtonIUse = null;

		if (bShowLeerenButton == true) {
			aWhichButtonIUse = new String[] { PanelBasis.ACTION_LEEREN };

		}

		QueryType[] qt = null;
		// Filter nach Mandant
		FilterKriterium[] filters = SystemFilterFactory.getInstance()
				.createFKMandantCNr();

		PanelQueryFLR panelQueryFLRFinanzamt = new PanelQueryFLR(qt, filters,
				QueryParameters.UC_ID_FINANZAMT, aWhichButtonIUse,
				internalFrameI, LPMain.getInstance().getTextRespectUISPr(
						"finanz.liste.finanzaemter"));

		panelQueryFLRFinanzamt.setSelectedId(selectedId);

		return panelQueryFLRFinanzamt;
	}

	public PanelQueryFLR createPanelFLRSachKonto(InternalFrame internalFrameI,
			Integer selectedId, boolean bShowLeerenButton) throws Throwable {
		String[] aWhichButtonIUse = null;
		if (bShowLeerenButton == true) {
			aWhichButtonIUse = new String[] { PanelBasis.ACTION_LEEREN };

		}
		QueryType[] qt = null;
		// nur Sachkonten dieses Mandanten
		FilterKriterium[] filters = FinanzFilterFactory.getInstance()
				.createFKSachkonten();
		PanelQueryFLR panelQueryFLRKonto = new PanelQueryFLR(qt, filters,
				QueryParameters.UC_ID_FINANZKONTEN, aWhichButtonIUse,
				internalFrameI, LPMain.getInstance().getTextRespectUISPr(
						"finanz.liste.sachkonten"));
		FilterKriteriumDirekt fkDirekt1 = FinanzFilterFactory.getInstance()
				.createFKDKontonummer();
		FilterKriteriumDirekt fkDirekt2 = FinanzFilterFactory.getInstance()
				.createFKDKontobezeichnung();
		FilterKriterium fkVersteckt = FinanzFilterFactory.getInstance()
				.createFKVKonto();
		panelQueryFLRKonto.befuellePanelFilterkriterienDirektUndVersteckte(
				fkDirekt1, fkDirekt2, fkVersteckt);
		if (selectedId != null) {
			panelQueryFLRKonto.setSelectedId(selectedId);
		}

		return panelQueryFLRKonto;
	}

	public FilterKriterium[] createFKLaenderartOhneInland() throws Throwable {
		FilterKriterium[] kriterien = new FilterKriterium[1];
		FilterKriterium krit1 = new FilterKriterium(
				FinanzFac.FLR_LAENDERART_C_NR, true, "'"
						+ FinanzFac.LAENDERART_INLAND + "'",
				FilterKriterium.OPERATOR_NOT_EQUAL, false);
		kriterien[0] = krit1;
		return kriterien;
	}

	public FilterKriterium[] createFKZahlungsvorschlag(
			ZahlungsvorschlaglaufDto zvlaufDto) {
		FilterKriterium[] filter = null;
		Integer zvlaufIId = null;
		if (zvlaufDto != null) {
			zvlaufIId = zvlaufDto.getIId();
		} else {
			// keine anzeigen
			zvlaufIId = new Integer(-1);
		}

		if (zvlaufIId != null) {
			filter = new FilterKriterium[1];
			filter[0] = new FilterKriterium(
					EingangsrechnungFac.FLR_ZV_ZAHLUNGSVORSCHLAGLAUF_I_ID,
					true, "'" + zvlaufIId.toString() + "'",
					FilterKriterium.OPERATOR_EQUAL, false);
		}
		return filter;
	}

	public FilterKriterium createFKVKonto() {
		FilterKriterium fkVersteckt = new FilterKriterium(
				FinanzFac.FLR_KONTO_B_VERSTECKT, true, "(1)", // wenn das
																// Kriterium
																// verwendet
																// wird, sollen
																// die
																// versteckten
																// nicht
																// mitangezeigt
																// werden
				FilterKriterium.OPERATOR_NOT_IN, false);
		return fkVersteckt;
	}

	public FilterKriterium createFKVBuchungStorno() {
		FilterKriterium fkVersteckt = new FilterKriterium("flrbuchung."
				+ FinanzFac.FLR_BUCHUNG_T_STORNIERT, true, "",
				FilterKriterium.OPERATOR_IS + " "
						+ FilterKriterium.OPERATOR_NULL, false);

		return fkVersteckt;
	}

	public FilterKriterium createFKVStorno() {
		FilterKriterium fkVersteckt = new FilterKriterium(
				FinanzFac.FLR_BUCHUNG_T_STORNIERT, true, "",
				FilterKriterium.OPERATOR_IS + " "
						+ FilterKriterium.OPERATOR_NULL, false);

		return fkVersteckt;
	}

	public FilterKriterium createFKVBankverbindung() {
		FilterKriterium fkVersteckt = new FilterKriterium(
				FinanzFac.FLR_BANKKONTO_FLRKONTO+".b_versteckt", true, "(1)", // wenn
				// das
				// Kriterium
				// verwendet
				// wird,
				// sollen
				// die
				// versteckten
				// nicht
				// mitangezeigt
				// werden
				FilterKriterium.OPERATOR_NOT_IN, false);

		return fkVersteckt;
	}
	
	public FilterKriterium createFKVStornoDetailliert() {
		FilterKriterium fkVersteckt = new FilterKriterium(
				FinanzFac.FLR_BUCHUNGDETAIL_FLRBUCHUNG + "."
						+ FinanzFac.FLR_BUCHUNG_T_STORNIERT, true, "",
				FilterKriterium.OPERATOR_IS + " "
						+ FilterKriterium.OPERATOR_NULL, false);

		return fkVersteckt;
	}

	public FilterKriteriumDirekt createFKDBelegnummer() throws Throwable {
		return new FilterKriteriumDirekt(FinanzFac.FLR_BUCHUNG_C_BELEGNUMMER,
				"", FilterKriterium.OPERATOR_LIKE,
				LPMain.getTextRespectUISPr("lp.beleg"),
				FilterKriteriumDirekt.PROZENT_LEADING, // Auswertung als '%XX%'
				true, // wrapWithSingleQuotes
				false, Facade.MAX_UNBESCHRAENKT);
	}
	
	public FilterKriteriumDirekt createFKDTextsuche() throws Throwable {
		return new FilterKriteriumDirekt(FinanzFac.FLR_BUCHUNGDETAIL_FLRBUCHUNG + "." + FinanzFac.FLR_BUCHUNG_C_TEXT,
				"", FilterKriterium.OPERATOR_LIKE,
				LPMain.getTextRespectUISPr("lp.textsuche"),
				FilterKriteriumDirekt.PROZENT_BOTH, // Auswertung als 'XX%'
				true, // wrapWithSingleQuotes
				true, Facade.MAX_UNBESCHRAENKT);
	}
	
	public FilterKriterium[] createFKSchnellansicht() throws Throwable {
		FilterKriterium[] filters = new FilterKriterium[1];
		filters[0] = new FilterKriteriumSchnellansicht(
				FinanzFac.FILTER_BUCHUNGDETAILS_NUR_OFFENE, true, "",
				FilterKriterium.OPERATOR_NOT_IN, false,
				ParameterFac.KATEGORIE_FINANZ,
				ParameterFac.PARAMETER_FINANZ_BUCHUNGEN_NUR_OFFENE_DEFAULT);

		return filters;
	}

	public FilterKriteriumDirekt createFKDBetragssuche() throws Throwable {
		return new FilterKriteriumDirekt(FinanzFac.FLR_BUCHUNGDETAIL_N_BETRAG,
				"",
				FilterKriterium.OPERATOR_EQUAL,
				LPMain.getTextRespectUISPr("label.betrag"),
				FilterKriteriumDirekt.PROZENT_NONE, // Auswertung als 'XX%'
				false, // wrapWithSingleQuotes
				false, Facade.MAX_UNBESCHRAENKT,
				FilterKriteriumDirekt.TYP_DECIMAL);
	}

	public FilterKriteriumDirekt createFKDBuchungsjournalText()
			throws Throwable {
		return new FilterKriteriumDirekt(FinanzFac.FLR_BUCHUNG_C_TEXT, "",
				FilterKriterium.OPERATOR_LIKE,
				LPMain.getTextRespectUISPr("label.text"),
				FilterKriteriumDirekt.PROZENT_BOTH, // Auswertung als 'XX%'
				true, // wrapWithSingleQuotes
				true, Facade.MAX_UNBESCHRAENKT);
	}

	public FilterKriteriumDirekt createFKDBuchungsjournalDetailliertText()
			throws Throwable {
		return new FilterKriteriumDirekt(FinanzFac.FLR_BUCHUNGDETAIL_FLRBUCHUNG
				+ "." + FinanzFac.FLR_BUCHUNG_C_TEXT, "",
				FilterKriterium.OPERATOR_LIKE,
				LPMain.getTextRespectUISPr("label.text"),
				FilterKriteriumDirekt.PROZENT_BOTH, // Auswertung als 'XX%'
				true, // wrapWithSingleQuotes
				true, Facade.MAX_UNBESCHRAENKT);
	}
	
	/**
	 * Direktes Filter Kriterium Kontonummer fuer das PanelQueryKonto.
	 * 
	 * @return FilterKriteriumDirekt
	 * @throws Throwable
	 */
	public FilterKriteriumDirekt createFKBuchungsjournalDetailiertKontonummer() throws Throwable {
		return new FilterKriteriumDirekt(FinanzFac.FLR_BUCHUNGDETAIL_FLRKONTO + ".c_nr", "",
				FilterKriterium.OPERATOR_LIKE,
				LPMain.getTextRespectUISPr("finanz.kontonummer"),
				FilterKriteriumDirekt.PROZENT_TRAILING, // Auswertung als 'XX%'
				true, // wrapWithSingleQuotes
				false, Facade.MAX_UNBESCHRAENKT);
	}


	public PanelQueryFLR createPanelFLRWarenverkehrsnummer(
			InternalFrame internalFrameI, String selectedCNr) throws Throwable {
		PanelQueryFLR panelQueryIDEP = new PanelQueryFLR(null, null,
				QueryParameters.UC_ID_WARENVERKEHRSNUMMER, null,
				internalFrameI,
				LPMain.getTextRespectUISPr("finanz.liste.warenverkehrsnummern"));
		panelQueryIDEP.befuellePanelFilterkriterienDirekt(
				createFKDWarenverkehrsnummerCNr(),
				createFKDWarenverkehrsnummerCBez());
		if (selectedCNr != null) {
			panelQueryIDEP.setSelectedId(selectedCNr);
		}
		return panelQueryIDEP;
	}

	public FilterKriteriumDirekt createFKDWarenverkehrsnummerCNr()
			throws Throwable {
		return new FilterKriteriumDirekt(
				FinanzServiceFac.FLR_WARENVERKEHRSNUMMER_C_NR,
				"",
				FilterKriterium.OPERATOR_LIKE,
				LPMain.getTextRespectUISPr("artikel.sonstiges.warenverkehrsnummer"),
				FilterKriteriumDirekt.PROZENT_TRAILING, // Auswertung als 'XX%'
				true, // wrapWithSingleQuotes
				false, ArtikelFac.MAX_ARTIKEL_WARENVERKEHRSNUMMER);
	}

	public FilterKriteriumDirekt createFKDWarenverkehrsnummerCBez()
			throws Throwable {
		return new FilterKriteriumDirekt(
				FinanzServiceFac.FLR_WARENVERKEHRSNUMMER_C_BEZ, "",
				FilterKriterium.OPERATOR_LIKE,
				LPMain.getTextRespectUISPr("lp.bezeichnung"),
				FilterKriteriumDirekt.PROZENT_BOTH, // Auswertung als '%XX%'
				true, // wrapWithSingleQuotes
				true, Facade.MAX_UNBESCHRAENKT);
	}

	/**
	 * @deprecated MB. Wann vereinheitlichen wir endlich die Waehrungsauswahl?
	 * 
	 * @param internalFrameI
	 *            InternalFrame
	 * @param selectedCNr
	 *            String
	 * @return PanelQueryFLR
	 * @throws Throwable
	 */
	public PanelQueryFLR createPanelFLRWaehrung(InternalFrame internalFrameI,
			String selectedCNr) throws Throwable {
		PanelQueryFLR panelQueryWaehrung = new PanelQueryFLR(null, null,
				QueryParameters.UC_ID_WAEHRUNG, null, internalFrameI,
				LPMain.getTextRespectUISPr("title.waehrungauswahlliste"));
		if (selectedCNr != null) {
			panelQueryWaehrung.setSelectedId(selectedCNr);
		}
		panelQueryWaehrung.addDirektFilter(createFKDWaehrung());
		return panelQueryWaehrung;
	}

	/**
	 * Direktes Filter Kriterium Kontonummer fuer das PanelQueryKonto.
	 * 
	 * @return FilterKriteriumDirekt
	 * @throws Throwable
	 */
	public FilterKriteriumDirekt createFKDWaehrung() throws Throwable {
		return new FilterKriteriumDirekt("c_nr", "",
				FilterKriterium.OPERATOR_LIKE,
				LPMain.getTextRespectUISPr("label.waehrung"),
				FilterKriteriumDirekt.PROZENT_TRAILING, // Auswertung als 'XX%'
				true, // wrapWithSingleQuotes
				true, 3);
	}
	
	public FilterKriteriumDirekt createFKDBuchungsart()
			throws Throwable {
		return new FilterKriteriumDirekt(FinanzFac.FLR_BUCHUNGDETAIL_BUCHUNGART, "",
				FilterKriterium.OPERATOR_LIKE,
				LPMain.getTextRespectUISPr("label.fbbuchungsart"),
				FilterKriteriumDirekt.PROZENT_BOTH, // Auswertung als 'XX%'
				true, // wrapWithSingleQuotes
				true, Facade.MAX_UNBESCHRAENKT);
	}
	
	public FilterKriteriumDirekt createFKDBelegart()
			throws Throwable {
		return new FilterKriteriumDirekt(FinanzFac.FLR_BUCHUNGDETAIL_BELEGART, "",
				FilterKriterium.OPERATOR_LIKE,
				LPMain.getTextRespectUISPr("label.fbbelegart"),
				FilterKriteriumDirekt.PROZENT_BOTH, // Auswertung als 'XX%'
				true, // wrapWithSingleQuotes
				true, Facade.MAX_UNBESCHRAENKT);
	}
	
	
}
