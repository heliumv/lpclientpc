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
package com.lp.client.anfrage;

import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.system.SystemFilterFactory;
import com.lp.client.util.fastlanereader.gui.QueryType;
import com.lp.server.anfrage.service.AnfrageFac;
import com.lp.server.anfrage.service.AnfrageServiceFac;
import com.lp.server.anfrage.service.AnfragepositionFac;
import com.lp.server.partner.service.LieferantFac;
import com.lp.server.partner.service.PartnerFac;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.util.Facade;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.FilterKriteriumDirekt;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;

/**
 * <p>
 * Diese Klasse ist ein Singleton und kuemmert sich um alle Filter in der
 * Anfrage.
 * </p>
 * 
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2008
 * </p>
 * 
 * <p>
 * Erstellung: 14.06.05
 * </p>
 * 
 * <p>
 * 
 * @author Uli Walch
 *         </p>
 * 
 * @version $Revision: 1.9 $ Date $Date: 2012/08/23 11:09:58 $
 */
@SuppressWarnings("static-access")
public class AnfrageFilterFactory {
	private static AnfrageFilterFactory filterFactory = null;

	private AnfrageFilterFactory() {
		// Singleton.
	}

	public FilterKriterium[] createFKAnfrageSchnellansicht() {
		FilterKriterium[] filters = new FilterKriterium[1];
		filters[0] = new FilterKriterium(
				AnfrageFac.FLR_ANFRAGE_ANFRAGESTATUS_C_NR, true, "('"
						+ AnfrageServiceFac.ANFRAGESTATUS_ERLEDIGT + "','"
						+ AnfrageServiceFac.ANFRAGESTATUS_STORNIERT + "')",
				FilterKriterium.OPERATOR_NOT_IN, false);

		return filters;
	}

	/**
	 * Hole das Singelton AnfrageFilterFactory.
	 * 
	 * @return LPMain
	 */
	static public AnfrageFilterFactory getInstance() {
		if (filterFactory == null) {
			filterFactory = new AnfrageFilterFactory();
		}

		return filterFactory;
	}

	/**
	 * Der PK der aktuellen Anfrage wird als Filter verwendet.
	 * 
	 * @param iIdAnfrageI
	 *            PK der Anfrage
	 * @return FilterKriterium[]
	 */
	protected FilterKriterium[] createFKAnfrageiid(Integer iIdAnfrageI) {
		FilterKriterium[] krit = null;

		if (iIdAnfrageI != null) {
			krit = new FilterKriterium[1];

			krit[0] = new FilterKriterium("i_id", true, iIdAnfrageI.toString(),
					FilterKriterium.OPERATOR_EQUAL, false);
		}

		return krit;
	}

	/**
	 * 1:n Listen in der Anfrage wie Anfragepositionen werden per default nach
	 * der referenzierten Anfrage gefiltert.
	 * 
	 * @param iIdAnfrageI
	 *            PK der Anfrage
	 * @return FilterKriterium[]
	 */
	protected FilterKriterium[] createFKFlranfrageiid(Integer iIdAnfrageI) {
		FilterKriterium[] aFilterKrit = null;

		if (iIdAnfrageI != null) {
			aFilterKrit = new FilterKriterium[1];

			FilterKriterium krit1 = new FilterKriterium(
					AnfragepositionFac.FLR_ANFRAGEPOSITION_FLRANFRAGE + ".i_id",
					true, iIdAnfrageI.toString(),
					FilterKriterium.OPERATOR_EQUAL, false);

			aFilterKrit[0] = krit1;
		}

		return aFilterKrit;
	}

	public FilterKriteriumDirekt createFKDProjekt() throws Throwable {
		return new FilterKriteriumDirekt("c_bez", "",
				FilterKriterium.OPERATOR_LIKE, LPMain.getInstance()
						.getTextRespectUISPr("label.projekt"),
				FilterKriteriumDirekt.PROZENT_TRAILING, // Auswertung als '%XX'
				true, // wrapWithSingleQuotes
				true, // ignore case
				Facade.MAX_UNBESCHRAENKT);
	}

	/**
	 * 1:n Liste Anfragepositionlieferdaten wird per default nach der
	 * referenzierten Anfrage gefiltert.
	 * 
	 * @param iIdAnfrageI
	 *            PK der Anfrage
	 * @return FilterKriterium[]
	 */
	protected FilterKriterium[] createFKFlranfragepositionflranfrageiid(
			Integer iIdAnfrageI) {
		FilterKriterium[] aFilterKrit = null;

		if (iIdAnfrageI != null) {
			aFilterKrit = new FilterKriterium[1];

			FilterKriterium krit1 = new FilterKriterium(
					AnfragepositionFac.FLR_ANFRAGEPOSITIONLIEFERDATEN_FLRANFRAGEPOSITION
							+ "."
							+ AnfragepositionFac.FLR_ANFRAGEPOSITION_FLRANFRAGE
							+ ".i_id", true, iIdAnfrageI.toString(),
					FilterKriterium.OPERATOR_EQUAL, false);

			aFilterKrit[0] = krit1;
		}

		return aFilterKrit;
	}

	/**
	 * Default Filter Kriterien fuer eine Liste von Anfragetexten.
	 * 
	 * @return FilterKriterium[] Default Filter Kriterien
	 * @throws Throwable
	 *             Ausnahme
	 */
	public FilterKriterium[] createFKAnfragetext() throws Throwable {
		FilterKriterium[] kriterien = new FilterKriterium[2];

		FilterKriterium krit1 = new FilterKriterium("mandant_c_nr", true, "'"
				+ LPMain.getInstance().getTheClient().getMandant() + "'",
				FilterKriterium.OPERATOR_EQUAL, false);
		kriterien[0] = krit1;

		FilterKriterium krit2 = new FilterKriterium("locale_c_nr", true, "'"
				+ LPMain.getInstance().getTheClient().getLocUiAsString() + "'",
				FilterKriterium.OPERATOR_EQUAL, false);

		kriterien[0] = krit1;
		kriterien[1] = krit2;

		return kriterien;
	}

	public FilterKriteriumDirekt createFKDAnfragenummer() throws Throwable {
		return new FilterKriteriumDirekt("c_nr", "",
				FilterKriterium.OPERATOR_LIKE, LPMain.getInstance()
						.getTextRespectUISPr("anf.anfragenummershort"),
				FilterKriteriumDirekt.PROZENT_LEADING, // Auswertung als '%XX'
				true, // wrapWithSingleQuotes
				false, Facade.MAX_UNBESCHRAENKT); // ignore case
	}

	public FilterKriteriumDirekt createFKDLieferantName() throws Throwable {
		ParametermandantDto parameter = (ParametermandantDto) DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getParametermandant(
						ParameterFac.PARAMETER_PARTNERSUCHE_WILDCARD_BEIDSEITIG,
						ParameterFac.KATEGORIE_PARTNER,
						LPMain.getInstance().getTheClient().getMandant());
		if (((Boolean) parameter.getCWertAsObject() == true)) {
			FilterKriteriumDirekt fkDirekt1 = new FilterKriteriumDirekt(
					AnfrageFac.FLR_ANFRAGE_FLRLIEFERANT + "."
							+ LieferantFac.FLR_PARTNER + "."
							+ PartnerFac.FLR_PARTNER_NAME1NACHNAMEFIRMAZEILE1,
					"", FilterKriterium.OPERATOR_LIKE, LPMain.getInstance()
							.getTextRespectUISPr("lp.lieferant"),
					FilterKriteriumDirekt.PROZENT_BOTH, // Auswertung als
					// 'XX%'
					true, // wrapWithSingleQuotes
					true, Facade.MAX_UNBESCHRAENKT); // ignore case
			return fkDirekt1;
		} else {
			FilterKriteriumDirekt fkDirekt1 = new FilterKriteriumDirekt(
					AnfrageFac.FLR_ANFRAGE_FLRLIEFERANT + "."
							+ LieferantFac.FLR_PARTNER + "."
							+ PartnerFac.FLR_PARTNER_NAME1NACHNAMEFIRMAZEILE1,
					"", FilterKriterium.OPERATOR_LIKE, LPMain.getInstance()
							.getTextRespectUISPr("lp.lieferant"),
					FilterKriteriumDirekt.PROZENT_TRAILING, // Auswertung als
					// 'XX%'
					true, // wrapWithSingleQuotes
					true, Facade.MAX_UNBESCHRAENKT); // ignore case
			return fkDirekt1;
		}

	}

	/**
	 * Die UI FilterKriterien am Panel Anfrageauswahl. <br>
	 * - Auswahl nach Anfragenummer <br>
	 * - Auswahl nach Lieferantenname <br>
	 * - Auswahl nach Belegdatum <br>
	 * - Auswahl nach Projektbezeichnung
	 * 
	 * @return QueryType[]
	 */
	protected QueryType[] createQTPanelAnfrageauswahl() {
		QueryType[] types = new QueryType[4];

		FilterKriterium f1 = new FilterKriterium("c_nr", true, "",
				FilterKriterium.OPERATOR_LIKE, false);

		types[0] = new QueryType(LPMain.getInstance().getTextRespectUISPr(
				"anf.anfragenummer"), f1,
				new String[] { FilterKriterium.OPERATOR_EQUAL }, true, true);

		FilterKriterium f2 = new FilterKriterium(
				AnfrageFac.FLR_ANFRAGE_FLRLIEFERANT + "."
						+ LieferantFac.FLR_PARTNER + "."
						+ PartnerFac.FLR_PARTNER_NAME1NACHNAMEFIRMAZEILE1,
				true, "", FilterKriterium.OPERATOR_LIKE, false);

		types[1] = new QueryType(LPMain.getInstance().getTextRespectUISPr(
				"lp.lieferant"), f2,
				new String[] { FilterKriterium.OPERATOR_EQUAL }, true, true);

		FilterKriterium f3 = new FilterKriterium(
				AnfrageFac.FLR_ANFRAGE_T_BELEGDATUM, true, "",
				FilterKriterium.OPERATOR_EQUAL, false);

		types[2] = new QueryType(LPMain.getInstance().getTextRespectUISPr(
				"lp.belegdatum"), f3, new String[] {
				FilterKriterium.OPERATOR_EQUAL, FilterKriterium.OPERATOR_GTE,
				FilterKriterium.OPERATOR_LTE }, true, false, false);

		FilterKriterium f4 = new FilterKriterium("c_bez", true, "",
				FilterKriterium.OPERATOR_LIKE, false);

		types[3] = new QueryType(LPMain.getInstance().getTextRespectUISPr(
				"label.projekt"), f4,
				new String[] { FilterKriterium.OPERATOR_EQUAL }, true, true);

		return types;
	}

	private FilterKriterium[] createFKMandantCNrNichtangelegtNichtstorniertKeineLiefergruppen()
			throws Throwable {
		FilterKriterium[] aFilterKriterium = new FilterKriterium[4];

		aFilterKriterium[0] = SystemFilterFactory.getInstance()
				.createFKMandantCNr()[0];

		aFilterKriterium[1] = new FilterKriterium(
				AnfrageFac.FLR_ANFRAGE_ANFRAGESTATUS_C_NR, true, "('"
						+ AnfrageServiceFac.ANFRAGESTATUS_ANGELEGT + "')",
				FilterKriterium.OPERATOR_NOT_IN, false);

		aFilterKriterium[2] = new FilterKriterium(
				AnfrageFac.FLR_ANFRAGE_ANFRAGESTATUS_C_NR, true, "('"
						+ AnfrageServiceFac.ANFRAGESTATUS_STORNIERT + "')",
				FilterKriterium.OPERATOR_NOT_IN, false);

		aFilterKriterium[3] = new FilterKriterium(
				AnfrageFac.FLR_ANFRAGE_ANFRAGEART_C_NR, true, "('"
						+ AnfrageServiceFac.ANFRAGEART_LIEFERGRUPPE + "')",
				FilterKriterium.OPERATOR_NOT_IN, false);

		return aFilterKriterium;
	}

	/**
	 * Anfrageauswahlliste mit Filter & Direktfilter.
	 * 
	 * @param internalFrameI
	 *            InternalFrame
	 * @param bShowFilterButton
	 *            den New Button anzeigen
	 * @param bShowLeerenButton
	 *            den Leeren Button anzeigen
	 * @return PanelQueryFLR die Auswahlliste
	 * @throws Throwable
	 *             Ausnahme
	 */
	public PanelQueryFLR createPanelFLRAnfrage(InternalFrame internalFrameI,
			boolean bShowFilterButton, boolean bShowLeerenButton)
			throws Throwable {
		String[] aWhichButtonIUse = SystemFilterFactory.getInstance()
				.createButtonArray(bShowFilterButton, bShowLeerenButton);
		FilterKriterium[] f = createFKMandantCNrNichtangelegtNichtstorniertKeineLiefergruppen();
		PanelQueryFLR panelQueryFLRAnfrage = new PanelQueryFLR(null, f,
				QueryParameters.UC_ID_ANFRAGE, aWhichButtonIUse,
				internalFrameI, LPMain
						.getTextRespectUISPr("title.anfrageauswahlliste"));
		panelQueryFLRAnfrage.befuellePanelFilterkriterienDirekt(
				createFKDAnfragenummer(), createFKDLieferantName());
		return panelQueryFLRAnfrage;
	}

	public PanelQueryFLR createPanelFLRZertifikatart(
			InternalFrame internalFrameI, Integer selectedId,
			boolean bMitLeerenButton) throws Throwable {
		String[] aWhichButtonIUse = null;
		if (bMitLeerenButton) {
			aWhichButtonIUse = new String[] { PanelBasis.ACTION_REFRESH,
					PanelBasis.ACTION_LEEREN };
		} else {
			aWhichButtonIUse = new String[] { PanelBasis.ACTION_REFRESH };
		}

		PanelQueryFLR panelQueryFLR = new PanelQueryFLR(null,
				SystemFilterFactory.getInstance().createFKMandantCNr(),
				QueryParameters.UC_ID_ZERTIFIKATART, aWhichButtonIUse,
				internalFrameI, LPMain.getInstance().getTextRespectUISPr(
						"anf.zertifikatart"));
		if (selectedId != null) {
			panelQueryFLR.setSelectedId(selectedId);
		}

		return panelQueryFLR;
	}

	/**
	 * Anfrageauswahlliste mit Filter & Direktfilter.
	 * 
	 * @param internalFrameI
	 *            InternalFrame
	 * @param bShowFilterButton
	 *            den New Button anzeigen
	 * @param bShowLeerenButton
	 *            den Leeren Button anzeigen
	 * @param selectedIIdI
	 *            die iId der in der Liste selektierten Liefergruppe
	 * @return PanelQueryFLR die Auswahlliste
	 * @throws Throwable
	 *             Ausnahme
	 */
	public PanelQueryFLR createPanelFLRAnfrage(InternalFrame internalFrameI,
			boolean bShowFilterButton, boolean bShowLeerenButton,
			Integer selectedIIdI) throws Throwable {
		PanelQueryFLR panelQueryFLRAnfrage = createPanelFLRAnfrage(
				internalFrameI, bShowFilterButton, bShowLeerenButton);

		if (selectedIIdI != null) {
			panelQueryFLRAnfrage.setSelectedId(selectedIIdI);
		}

		return panelQueryFLRAnfrage;
	}
}
