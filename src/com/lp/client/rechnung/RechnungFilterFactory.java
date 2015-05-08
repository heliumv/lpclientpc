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
package com.lp.client.rechnung;

import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.system.SystemFilterFactory;
import com.lp.client.util.fastlanereader.gui.QueryType;
import com.lp.server.auftrag.service.AuftragServiceFac;
import com.lp.server.auftrag.service.AuftragpositionFac;
import com.lp.server.partner.service.KundeFac;
import com.lp.server.partner.service.PartnerFac;
import com.lp.server.rechnung.service.RechnungDto;
import com.lp.server.rechnung.service.RechnungFac;
import com.lp.server.rechnung.service.RechnungServiceFac;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.util.Facade;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.FilterKriteriumDirekt;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.util.Helper;

/**
 * <p>
 * Diese Klasse ist ein Singleton und kuemmert sich um alle Filter in der
 * Rechnung.
 * </p>
 * 
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2008
 * </p>
 * 
 * <p>
 * Erstellung: 07. 05. 2005
 * </p>
 * 
 * <p>
 * 
 * @author Martin Bluehweis
 *         </p>
 * 
 * @version $Revision: 1.18 $ Date $Date: 2012/11/14 08:16:48 $
 */

public class RechnungFilterFactory {

	private static RechnungFilterFactory filterFactory = null;

	private RechnungFilterFactory() {
		// Singleton.
	}

	/**
	 * Hole das Singelton RechnungFilterFactory.
	 * 
	 * @return RechnungFilterFactory
	 */
	static public RechnungFilterFactory getInstance() {
		if (filterFactory == null) {
			filterFactory = new RechnungFilterFactory();
		}
		return filterFactory;
	}

	/**
	 * Direktes Filter Kriterium Rechnungnummer fuer das PanelQueryRechnung.
	 * 
	 * @return FilterKriteriumDirekt
	 * @throws Throwable
	 */
	public FilterKriteriumDirekt createFKDRechnungnummer() throws Throwable {
		return new FilterKriteriumDirekt(RechnungFac.FLR_RECHNUNG_C_NR, "",
				FilterKriterium.OPERATOR_LIKE, "Rechnung",
				FilterKriteriumDirekt.PROZENT_LEADING, // Auswertung als '%XX'
				true, // wrapWithSingleQuotes
				false, Facade.MAX_UNBESCHRAENKT);
	}

	protected QueryType[] createQTPanelRechnungauswahl() {
		QueryType[] types = new QueryType[1];

		FilterKriterium f4 = new FilterKriterium(
				RechnungFac.FLR_RECHNUNG_FLRKOSTENSTELLE + ".c_nr", true, "",
				FilterKriterium.OPERATOR_LIKE, false);

		types[0] = new QueryType(LPMain
				.getTextRespectUISPr("label.kostenstelle"), f4,
				new String[] { FilterKriterium.OPERATOR_LIKE }, true, // eingabeformat
				// 10.12.2004
				true, false);

		/*
		 * FilterKriterium f4 = new FilterKriterium(
		 * LieferscheinFac.FLR_LIEFERSCHEIN_C_BEZ_PROJEKTBEZEICHNUNG, true, "",
		 * FilterKriterium.OPERATOR_LIKE, false);
		 * 
		 * types[3] = new QueryType(
		 * LPMain.getInstance().getTextRespectUISPr("label.projekt"), f4, new
		 * String[] {FilterKriterium.OPERATOR_EQUAL} , true, true);
		 */

		return types;
	}

	/**
	 * Direktes Filter Kriterium Rechnungnummer fuer das PanelQueryGutschrift.
	 * 
	 * @return FilterKriteriumDirekt
	 * @throws Throwable
	 */
	public FilterKriteriumDirekt createFKDGutschriftnummer() throws Throwable {
		return new FilterKriteriumDirekt(RechnungFac.FLR_RECHNUNG_C_NR, "",
				FilterKriterium.OPERATOR_LIKE, "Gutschrift",
				FilterKriteriumDirekt.PROZENT_LEADING, // Auswertung als '%XX'
				true, // wrapWithSingleQuotes
				false, Facade.MAX_UNBESCHRAENKT);
	}

	/**
	 * Direktes Filter Kriterium Kundenname fuer das PanelRechnungAuswahl.
	 * 
	 * @return FilterKriteriumDirekt
	 */
	public FilterKriteriumDirekt createFKDKundename() throws Throwable {
		ParametermandantDto parameter = (ParametermandantDto) DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getParametermandant(
						ParameterFac.PARAMETER_PARTNERSUCHE_WILDCARD_BEIDSEITIG,
						ParameterFac.KATEGORIE_PARTNER,
						LPMain.getTheClient().getMandant());
		if (((Boolean) parameter.getCWertAsObject() == true)) {
			return new FilterKriteriumDirekt(RechnungFac.FLR_RECHNUNG_FLRKUNDE
					+ "." + KundeFac.FLR_PARTNER + "."
					+ PartnerFac.FLR_PARTNER_NAME1NACHNAMEFIRMAZEILE1, "",
					FilterKriterium.OPERATOR_LIKE, LPMain
							.getTextRespectUISPr("label.kunde"),
					FilterKriteriumDirekt.PROZENT_BOTH, // Auswertung als 'XX%'
					true, // wrapWithSingleQuotes
					true, Facade.MAX_UNBESCHRAENKT);
		} else {
			return new FilterKriteriumDirekt(RechnungFac.FLR_RECHNUNG_FLRKUNDE
					+ "." + KundeFac.FLR_PARTNER + "."
					+ PartnerFac.FLR_PARTNER_NAME1NACHNAMEFIRMAZEILE1, "",
					FilterKriterium.OPERATOR_LIKE, LPMain
							.getTextRespectUISPr("label.kunde"),
					FilterKriteriumDirekt.PROZENT_TRAILING, // Auswertung als
															// 'XX%'
					true, // wrapWithSingleQuotes
					true, Facade.MAX_UNBESCHRAENKT);
		}
	}

	
	public FilterKriteriumDirekt createFKDProjekt() throws Throwable {
		return new FilterKriteriumDirekt("c_bez", "",
				FilterKriterium.OPERATOR_LIKE, LPMain
						.getTextRespectUISPr("label.projekt"),
				FilterKriteriumDirekt.PROZENT_BOTH, // Auswertung als '%XX'
				true, // wrapWithSingleQuotes
				false, Facade.MAX_UNBESCHRAENKT); // ignore case
	}
	
	public FilterKriterium[] createFKRechnungKey(Integer artikelIId)
			throws Throwable {
		// Handartikel nicht anzeigen
		FilterKriterium[] kriterien = new FilterKriterium[1];
		kriterien[0] = new FilterKriterium("i_id", true, artikelIId + "",
				FilterKriterium.OPERATOR_EQUAL, false);

		return kriterien;
	}

	/**
	 * Direktes Filter Kriterium Kundenname fuer das PanelRechnungAuswahl.
	 * 
	 * @return FilterKriteriumDirekt
	 */
	public FilterKriteriumDirekt createFKDKundestatistikadresse() {
		return new FilterKriteriumDirekt(
				RechnungFac.FLR_RECHNUNG_FLRSTATISTIKADRESSE + "."
						+ KundeFac.FLR_PARTNER + "."
						+ PartnerFac.FLR_PARTNER_NAME1NACHNAMEFIRMAZEILE1, "",
				FilterKriterium.OPERATOR_LIKE, LPMain
						.getTextRespectUISPr("label.statistikadresse"),
				FilterKriteriumDirekt.PROZENT_TRAILING, // Auswertung als 'XX%'
				true, // wrapWithSingleQuotes
				true, Facade.MAX_UNBESCHRAENKT);
	}

	/**
	 * Default Filter Kriterien fuer eine Liste von Rechnungtexten
	 * 
	 * @return FilterKriterium[] Default Filter Kriterien
	 * @throws Throwable
	 *             Ausnahme
	 */
	public FilterKriterium[] createFKRechnungtext() throws Throwable {
		FilterKriterium[] kriterien = new FilterKriterium[2];

		FilterKriterium krit1 = new FilterKriterium(
				RechnungServiceFac.FLR_RECHNUNGTEXT_MANDANT_C_NR, true, "'"
						+ LPMain.getTheClient().getMandant() + "'",
				FilterKriterium.OPERATOR_EQUAL, false);
		kriterien[0] = krit1;

		FilterKriterium krit2 = new FilterKriterium(
				RechnungServiceFac.FLR_RECHNUNGTEXT_LOCALE_C_NR, true, "'"
						+ LPMain.getTheClient().getLocUiAsString() + "'",
				FilterKriterium.OPERATOR_EQUAL, false);

		kriterien[0] = krit1;
		kriterien[1] = krit2;

		return kriterien;
	}

	public FilterKriterium[] createFKGutschrifttext() throws Throwable {
		FilterKriterium[] kriterien = new FilterKriterium[2];

		FilterKriterium krit1 = new FilterKriterium(
				RechnungServiceFac.FLR_GUTSCHRIFTTEXT_MANDANT_C_NR, true, "'"
						+ LPMain.getTheClient().getMandant() + "'",
				FilterKriterium.OPERATOR_EQUAL, false);
		kriterien[0] = krit1;

		FilterKriterium krit2 = new FilterKriterium(
				RechnungServiceFac.FLR_GUTSCHRIFTTEXT_LOCALE_C_NR, true, "'"
						+ LPMain.getTheClient().getLocUiAsString() + "'",
				FilterKriterium.OPERATOR_EQUAL, false);

		kriterien[0] = krit1;
		kriterien[1] = krit2;

		return kriterien;
	}

	public FilterKriterium[] createFKRechnungen() throws Throwable {
		FilterKriterium[] filters = new FilterKriterium[2];
		filters[0] = SystemFilterFactory.getInstance().createFKMandantCNr()[0];
		filters[1] = new FilterKriterium(
				RechnungFac.FLR_RECHNUNG_FLRRECHNUNGART + "."
						+ RechnungFac.FLR_RECHNUNGART_RECHNUNGTYP_C_NR, true,
				"'" + RechnungFac.RECHNUNGTYP_RECHNUNG + "'",
				FilterKriterium.OPERATOR_EQUAL, false);
		return filters;
	}

	public FilterKriterium[] createFKRechnungenOffen() throws Throwable {
		FilterKriterium[] filters = new FilterKriterium[2];
		filters[0] = SystemFilterFactory.getInstance().createFKMandantCNr()[0];
		filters[1] = new FilterKriterium(
		/*
		 * RechnungFac.FLR_RECHNUNG_FLRRECHNUNGART + "." +
		 */RechnungFac.FLR_RECHNUNG_STATUS_C_NR, true, "'"
				+ RechnungFac.STATUS_OFFEN + "'",
				FilterKriterium.OPERATOR_EQUAL, false);
		return filters;
	}

	public FilterKriterium[] createFKRechnungenEinesKunden(Integer kundeIId)
			throws Throwable {
		FilterKriterium[] filters = new FilterKriterium[3];
		filters[0] = SystemFilterFactory.getInstance().createFKMandantCNr()[0];
		filters[1] = new FilterKriterium(RechnungFac.FLR_RECHNUNG_FLRKUNDE
				+ ".i_id", true, kundeIId + "", FilterKriterium.OPERATOR_EQUAL,
				false);

		filters[2] = new FilterKriterium(
				RechnungFac.FLR_RECHNUNG_FLRRECHNUNGART + "."
						+ RechnungFac.FLR_RECHNUNGART_RECHNUNGTYP_C_NR, true,
				"'" + RechnungFac.RECHNUNGTYP_RECHNUNG + "'",
				FilterKriterium.OPERATOR_EQUAL, false);
		return filters;
	}

	public FilterKriterium[] createFKGutschriften() throws Throwable {
		FilterKriterium[] filters = new FilterKriterium[2];
		filters[0] = SystemFilterFactory.getInstance().createFKMandantCNr()[0];
		filters[1] = new FilterKriterium(
				RechnungFac.FLR_RECHNUNG_FLRRECHNUNGART + "."
						+ RechnungFac.FLR_RECHNUNGART_RECHNUNGTYP_C_NR, true,
				"'" + RechnungFac.RECHNUNGTYP_GUTSCHRIFT + "'",
				FilterKriterium.OPERATOR_EQUAL, false);
		return filters;
	}
	public FilterKriterium[] createFKMahnsperren(java.sql.Date dateMahnlauf) throws Throwable {
		FilterKriterium[] filters = new FilterKriterium[2];
		filters[0] = SystemFilterFactory.getInstance().createFKMandantCNr()[0];
		filters[1] = new FilterKriterium(
				RechnungFac.FLR_RECHNUNG_T_MAHNSPERREBIS, true,
				"'" + Helper.formatDateWithSlashes(dateMahnlauf) + "'",
				FilterKriterium.OPERATOR_GT, false);
	
		return filters;
	}

	public FilterKriterium[] createFKProformarechnungen() throws Throwable {
		FilterKriterium[] filters = new FilterKriterium[2];
		filters[0] = SystemFilterFactory.getInstance().createFKMandantCNr()[0];
		filters[1] = new FilterKriterium(
				RechnungFac.FLR_RECHNUNG_FLRRECHNUNGART + "."
						+ RechnungFac.FLR_RECHNUNGART_RECHNUNGTYP_C_NR, true,
				"'" + RechnungFac.RECHNUNGTYP_PROFORMARECHNUNG + "'",
				FilterKriterium.OPERATOR_EQUAL, false);
		return filters;
	}

	public FilterKriterium[] createFKZahlungen(Integer rechnungIId)
			throws Throwable {
		FilterKriterium[] filters = new FilterKriterium[1];
		filters[0] = new FilterKriterium(
				RechnungFac.FLR_RECHNUNG_ZAHLUNG_RECHNUNG_I_ID, true,
				rechnungIId + "", FilterKriterium.OPERATOR_EQUAL, false);
		return filters;
	}

	public FilterKriterium[] createFKAuftrag(Integer auftragIId)
			throws Throwable {
		FilterKriterium[] filters = new FilterKriterium[1];
		filters[0] = new FilterKriterium(RechnungFac.FLR_RECHNUNG_AUFTRAG_I_ID,
				true, auftragIId + "", FilterKriterium.OPERATOR_EQUAL, false);
		return filters;
	}

	public FilterKriterium[] createFKRechnungpositionen(Integer rechnungIId)
			throws Throwable {
		FilterKriterium[] filters = new FilterKriterium[1];
		filters[0] = new FilterKriterium(
				RechnungFac.FLR_RECHNUNGPOSITION_RECHNUNG_I_ID, true, "'"
						+ rechnungIId + "'", FilterKriterium.OPERATOR_LIKE,
				false);
		return filters;
	}

	public FilterKriterium[] createFKRechnungpositionenQuery(Integer rechnungIId)
			throws Throwable {
		FilterKriterium[] filters = new FilterKriterium[1];
		filters[0] = new FilterKriterium(
				RechnungFac.FLR_RECHNUNGPOSITION_RECHNUNG_I_ID, true, ""
						+ rechnungIId, FilterKriterium.OPERATOR_EQUAL, false);
		return filters;
	}

	public FilterKriterium[] createFKRechnungkontierung(Integer rechnungIId)
			throws Throwable {
		FilterKriterium[] filters = new FilterKriterium[1];
		filters[0] = new FilterKriterium(
				RechnungFac.FLR_KONTIERUNG_RECHNUNG_I_ID, true, rechnungIId
						+ "", FilterKriterium.OPERATOR_EQUAL, false);
		return filters;
	}

	public QueryType[] createQTRechnungpositionen() {
		QueryType[] types = new QueryType[2];

		FilterKriterium f1 = new FilterKriterium(
				RechnungFac.FLR_RECHNUNGPOSITION_C_BEZ, true, "",
				FilterKriterium.OPERATOR_LIKE, false);

		types[0] = new QueryType(LPMain
				.getTextRespectUISPr("rechnung.bezeichnung"), f1,
				new String[] { FilterKriterium.OPERATOR_EQUAL }, true, true);

		FilterKriterium f2 = new FilterKriterium(
				RechnungFac.FLR_RECHNUNGPOSITION_C_ZUSATZBEZEICHNUNG, true, "",
				FilterKriterium.OPERATOR_LIKE, false);
		types[1] = new QueryType(LPMain
				.getTextRespectUISPr("rechnung.lieferbezeichnung"), f2,
				new String[] { FilterKriterium.OPERATOR_EQUAL }, true, true);
		return types;
	}

	public FilterKriterium createFKKriteriumKalenderjahr(boolean isSelectedI,
			String sJahrI) throws Throwable {
		FilterKriterium[] filter = new FilterKriterium[1];
		filter[0] = new FilterKriterium(RechnungFac.KRIT_JAHR_KALENDERJAHR,
				isSelectedI, sJahrI, FilterKriterium.OPERATOR_EQUAL, false);

		return filter[0];
	}

	public FilterKriterium createFKKriteriumGeschaeftsjahr(boolean isSelectedI,
			String sJahrI) throws Throwable {
		FilterKriterium[] filter = new FilterKriterium[1];
		filter[0] = new FilterKriterium(RechnungFac.KRIT_JAHR_GESCHAEFTSJAHR,
				isSelectedI, sJahrI, FilterKriterium.OPERATOR_EQUAL, false);

		return filter[0];
	}

	public FilterKriterium createFKKriteriumMitGutschriften(
			boolean isSelectedI, String sJahrI) throws Throwable {
		FilterKriterium[] filter = new FilterKriterium[1];
		filter[0] = new FilterKriterium(RechnungFac.KRIT_MIT_GUTSCHRIFTEN,
				isSelectedI, sJahrI, FilterKriterium.OPERATOR_EQUAL, false);

		return filter[0];
	}

	public FilterKriterium createFKKriteriumOhneGutschriften(
			boolean isSelectedI, String sJahrI) throws Throwable {
		FilterKriterium[] filter = new FilterKriterium[1];
		filter[0] = new FilterKriterium(RechnungFac.KRIT_OHNE_GUTSCHRIFTEN,
				isSelectedI, sJahrI, FilterKriterium.OPERATOR_EQUAL, false);

		return filter[0];
	}

	protected FilterKriterium[] createFKSichtAuftragOffeneAnzeigen() {

		FilterKriterium[] fk = new FilterKriterium[1];

		fk[0] = new FilterKriterium(
				AuftragpositionFac.FLR_AUFTRAGPOSITIONSICHTAUFTRAG_AUFTRAGPOSITIONSTATUS_C_NR,
				true, "'" + AuftragServiceFac.AUFTRAGPOSITIONSTATUS_ERLEDIGT
						+ "'", FilterKriterium.OPERATOR_NOT_EQUAL, false);
		return fk;
	}

	protected FilterKriterium[] createFKRechnungSichtAuftrag(
			RechnungDto oRechnungDtoI, Integer iIdAuftragI) {
		FilterKriterium[] kriterien = null;

		if (oRechnungDtoI != null && oRechnungDtoI.getIId() != null) {
			if (iIdAuftragI != null) {
				kriterien = new FilterKriterium[1];

				FilterKriterium krit1 = new FilterKriterium(
						AuftragpositionFac.FLR_AUFTRAGPOSITIONSICHTAUFTRAG_AUFTRAG_I_ID,
						true, iIdAuftragI.toString(),
						FilterKriterium.OPERATOR_EQUAL, false);

				kriterien[0] = krit1;

			} else {
				// der FLR muss immer eine leere Liste liefern
				kriterien = new FilterKriterium[1];

				FilterKriterium krit1 = new FilterKriterium(
						AuftragpositionFac.FLR_AUFTRAGPOSITIONSICHTAUFTRAG_AUFTRAG_I_ID,
						true, String.valueOf(-1),
						FilterKriterium.OPERATOR_EQUAL, false);

				kriterien[0] = krit1;
			}
		}

		return kriterien;
	}

	/**
	 * @param iIdRechnungI
	 *            PK der Rechnung
	 * @return FilterKriterium[] die Kriterien
	 * @throws java.lang.Throwable
	 *             Ausnahme
	 */
	public FilterKriterium[] createFKAuftraegeEinerRechnung(Integer iIdRechnungI)
			throws Throwable {
		FilterKriterium[] kriterien = null;

		if (iIdRechnungI != null) {
			kriterien = new FilterKriterium[2];

			kriterien[0] = new FilterKriterium("rechnung_i_id", true, iIdRechnungI
							.toString(), FilterKriterium.OPERATOR_EQUAL, false);
			
			kriterien[1] = new FilterKriterium("auftragposition_i_id", true, "", FilterKriterium.OPERATOR_IS + " "
							+ FilterKriterium.OPERATOR_NOT_NULL, false);
		}

		return kriterien;
	}

	public PanelQueryFLR createPanelFLRRechnung(InternalFrame internalFrameI,
			Integer rechnungIId) throws Throwable {
		// String[] aWhichButtonIUse = {
		// PanelBasis.ACTION_REFRESH,
		// };
		String[] aWhichButtonIUse = SystemFilterFactory.getInstance()
				.createButtonArray(false, true);

		FilterKriterium[] filters = createFKRechnungen();
		PanelQueryFLR panelQueryFLRRechnung = new PanelQueryFLR(null, filters,
				QueryParameters.UC_ID_RECHNUNG, aWhichButtonIUse,
				internalFrameI, "Liste der Rechnungen");

		panelQueryFLRRechnung.befuellePanelFilterkriterienDirekt(
				createFKDRechnungnummer(), createFKDKundename());
		panelQueryFLRRechnung.addDirektFilter(createFKDKundestatistikadresse());

		if (rechnungIId != null) {
			panelQueryFLRRechnung.setSelectedId(rechnungIId);
		}
		return panelQueryFLRRechnung;
	}

	public PanelQueryFLR createPanelFLRRechnungOffen(
			InternalFrame internalFrameI, Integer rechnungIId) throws Throwable {
		// String[] aWhichButtonIUse = {
		// PanelBasis.ACTION_REFRESH,
		// };
		String[] aWhichButtonIUse = SystemFilterFactory.getInstance()
				.createButtonArray(false, true);

		FilterKriterium[] filters = createFKRechnungen();
		PanelQueryFLR panelQueryFLRRechnung = new PanelQueryFLR(null, filters,
				QueryParameters.UC_ID_RECHNUNG, aWhichButtonIUse,
				internalFrameI, "Liste der Rechnungen");
		panelQueryFLRRechnung
				.befuelleFilterkriteriumSchnellansicht(RechnungFilterFactory
						.getInstance().createFKSchnellansicht());
		panelQueryFLRRechnung.befuellePanelFilterkriterienDirekt(
				createFKDRechnungnummer(), createFKDKundename());
		panelQueryFLRRechnung.addDirektFilter(createFKDKundestatistikadresse());
		panelQueryFLRRechnung.eventActionRefresh(null, true);

		if (rechnungIId != null) {
			panelQueryFLRRechnung.setSelectedId(rechnungIId);
		}
		return panelQueryFLRRechnung;
	}

	public PanelQueryFLR createPanelFLRRechnungenEinesKunden(
			InternalFrame internalFrameI, Integer kundeIId_Rechnungsadresse,
			Integer rechnungIId) throws Throwable {
		String[] aWhichButtonIUse = { PanelBasis.ACTION_REFRESH, };
		FilterKriterium[] filters = createFKRechnungenEinesKunden(kundeIId_Rechnungsadresse);
		PanelQueryFLR panelQueryFLRRechnung = new PanelQueryFLR(null, filters,
				QueryParameters.UC_ID_RECHNUNG, aWhichButtonIUse,
				internalFrameI, "Liste der Rechnungen");

		panelQueryFLRRechnung.befuellePanelFilterkriterienDirekt(
				createFKDRechnungnummer(), createFKDKundename());
		panelQueryFLRRechnung.addDirektFilter(createFKDKundestatistikadresse());

		if (rechnungIId != null) {
			panelQueryFLRRechnung.setSelectedId(rechnungIId);
		}
		return panelQueryFLRRechnung;
	}

	public PanelQueryFLR createPanelFLRGutschriftsgrund(
			InternalFrame internalFrame, Integer integer) throws Throwable {

		PanelQueryFLR panelQueryGutschriftgrund = new PanelQueryFLR(null, null,
				QueryParameters.UC_ID_GUTSCHRIFTGRUND, null, internalFrame,
				LPMain.getTextRespectUISPr("rechnung.gutschriftsgrund"), true); // liste
		// refresh
		// wenn
		// lasche
		// geklickt
		// wurde
		if (integer != null) {
			panelQueryGutschriftgrund.setSelectedId(integer);
		}

		return panelQueryGutschriftgrund;
	}

	public FilterKriterium[] createFKSchnellansicht() throws Throwable {
		FilterKriterium[] filters = new FilterKriterium[1];
		filters[0] = new FilterKriterium(RechnungFac.FLR_RECHNUNG_STATUS_C_NR,
				true, "('" + RechnungFac.STATUS_STORNIERT + "','"
						+ RechnungFac.STATUS_BEZAHLT + "')",
				FilterKriterium.OPERATOR_NOT_IN, false);

		return filters;
	}

}
