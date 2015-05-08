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
package com.lp.client.lieferschein;

import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.system.SystemFilterFactory;
import com.lp.client.util.fastlanereader.gui.QueryType;
import com.lp.server.artikel.service.ArtikelFac;
import com.lp.server.auftrag.service.AuftragFac;
import com.lp.server.auftrag.service.AuftragServiceFac;
import com.lp.server.auftrag.service.AuftragpositionFac;
import com.lp.server.lieferschein.service.LieferscheinDto;
import com.lp.server.lieferschein.service.LieferscheinFac;
import com.lp.server.lieferschein.service.LieferscheinServiceFac;
import com.lp.server.lieferschein.service.LieferscheinpositionFac;
import com.lp.server.partner.service.KundeFac;
import com.lp.server.partner.service.PartnerFac;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.system.service.SystemFac;
import com.lp.server.util.Facade;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.FilterKriteriumDirekt;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;

@SuppressWarnings("static-access")
/*
 * <p> Diese Klasse ist ein Singleton und kuemmert sich um alle Filter im
 * Lieferschein.</p>
 * 
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 * 
 * <p>Erstellung: 26. 04. 2005</p>
 * 
 * <p>@author Uli Walch</p>
 * 
 * @version $Revision: 1.20 $ Date $Date: 2013/01/15 14:18:00 $
 */
public class LieferscheinFilterFactory {
	private static LieferscheinFilterFactory filterFactory = null;

	private LieferscheinFilterFactory() {
		// Singleton.
	}

	/**
	 * Hole das Singelton LieferscheinFilterFactory.
	 * 
	 * @return LieferscheinFilterFactory
	 */
	static public LieferscheinFilterFactory getInstance() {
		if (filterFactory == null) {
			filterFactory = new LieferscheinFilterFactory();
		}

		return filterFactory;
	}

	/**
	 * Wenn ein Lieferschein auf Basis eines Auftrags angelegt wird, koennen die
	 * verfuegbaren Auftraege in einem FLR ausgewaehlt werden. <br>
	 * Die Default Kriterien fuer diese Auswahlliste werden von dieser Methode
	 * geliefert.
	 * 
	 * @return FilterKriterium[] Filter Kriterien
	 * @throws java.lang.Throwable
	 *             Ausnahme
	 */
	public FilterKriterium[] createFKPanelQueryFLRAuftragAuswahl()
			throws Throwable {
		return createFKPanelQueryFLRAuftragAuswahl(false);
	}

	public FilterKriterium[] createFKPanelQueryFLRAuftragAuswahl(
			boolean bMitRahmenauftraegen) throws Throwable {
		FilterKriterium krit1 = new FilterKriterium(
				AuftragFac.FLR_AUFTRAG_MANDANT_C_NR, true, "'"
						+ LPMain.getInstance().getTheClient().getMandant()
						+ "'", FilterKriterium.OPERATOR_EQUAL, false);

		// unser FilterBlock verknuepft alle Kriterien mit AND, daher bedeutet
		// OFFEN oder TEILERLIDGT : nicht ANGELEGT und nicht ERLEDIGT und nicht
		// STORNIERT
		FilterKriterium krit2 = new FilterKriterium(
				AuftragFac.FLR_AUFTRAG_AUFTRAGSTATUS_C_NR, true, "'"
						+ AuftragServiceFac.AUFTRAGSTATUS_ANGELEGT + "'",
				FilterKriterium.OPERATOR_NOT_EQUAL, false);

		FilterKriterium krit3 = new FilterKriterium(
				AuftragFac.FLR_AUFTRAG_AUFTRAGSTATUS_C_NR, true, "'"
						+ AuftragServiceFac.AUFTRAGSTATUS_ERLEDIGT + "'",
				FilterKriterium.OPERATOR_NOT_EQUAL, false);

		FilterKriterium krit4 = new FilterKriterium(
				AuftragFac.FLR_AUFTRAG_AUFTRAGSTATUS_C_NR, true, "'"
						+ AuftragServiceFac.AUFTRAGSTATUS_STORNIERT + "'",
				FilterKriterium.OPERATOR_NOT_EQUAL, false);
		FilterKriterium[] kriterien = null;
		if (bMitRahmenauftraegen == true) {

			kriterien = new FilterKriterium[4];

			kriterien[0] = krit1;
			kriterien[1] = krit2;
			kriterien[2] = krit3;
			kriterien[3] = krit4;

		} else {
			FilterKriterium krit5 = new FilterKriterium(
					AuftragFac.FLR_AUFTRAG_AUFTRAGART_C_NR, true, "('"
							+ AuftragServiceFac.AUFTRAGART_RAHMEN + "')",
					FilterKriterium.OPERATOR_NOT_IN, false);

			kriterien = new FilterKriterium[5];

			kriterien[0] = krit1;
			kriterien[1] = krit2;
			kriterien[2] = krit3;
			kriterien[3] = krit4;
			kriterien[4] = krit5;
		}

		return kriterien;
	}

	public FilterKriterium[] createFKLieferscheinKey(Integer artikelIId)
			throws Throwable {
		// Handartikel nicht anzeigen
		FilterKriterium[] kriterien = new FilterKriterium[1];
		kriterien[0] = new FilterKriterium("i_id", true, artikelIId + "",
				FilterKriterium.OPERATOR_EQUAL, false);

		return kriterien;
	}

	public FilterKriterium[] createFKPanelQueryFLRAuftragAuswahl(
			Integer kundeIIdLieferadresse, Integer kundeIIdRechnungsadresse,
			Integer kundeIIdAuftragsadresse, Integer[] auftragIIdNichtAnzeigen)
			throws Throwable {
		FilterKriterium[] basiskrit = createFKPanelQueryFLRAuftragAuswahl();
		FilterKriterium[] krit = new FilterKriterium[basiskrit.length + 3
				+ auftragIIdNichtAnzeigen.length];
		for (int i = 0; i < basiskrit.length; i++) {
			krit[i] = basiskrit[i];
		}
		// Zusaetzliche Kriterien
		FilterKriterium krit1 = new FilterKriterium(
				AuftragFac.FLR_AUFTRAG_KUNDE_I_ID_LIEFERADRESSE, true, "'"
						+ kundeIIdLieferadresse + "'",
				FilterKriterium.OPERATOR_EQUAL, false);
		FilterKriterium krit2 = new FilterKriterium(
				AuftragFac.FLR_AUFTRAG_KUNDE_I_ID_AUFTRAGSADRESSE, true, "'"
						+ kundeIIdAuftragsadresse + "'",
				FilterKriterium.OPERATOR_EQUAL, false);
		FilterKriterium krit3 = new FilterKriterium(
				AuftragFac.FLR_AUFTRAG_KUNDE_I_ID_RECHNUNGSADRESSE, true, "'"
						+ kundeIIdRechnungsadresse + "'",
				FilterKriterium.OPERATOR_EQUAL, false);
		krit[basiskrit.length + 0] = krit1;
		krit[basiskrit.length + 1] = krit2;
		krit[basiskrit.length + 2] = krit3;
		// nicht anzuzeigende auftraege
		for (int i = 0; i < auftragIIdNichtAnzeigen.length; i++) {
			FilterKriterium kritNA = new FilterKriterium(
					AuftragFac.FLR_AUFTRAG_I_ID, true, "'"
							+ auftragIIdNichtAnzeigen[i] + "'",
					FilterKriterium.OPERATOR_NOT_EQUAL, false);
			krit[basiskrit.length + 3 + i] = kritNA;
		}
		return krit;
	}

	/**
	 * Einschraenken der 1:n Listen im Lieferschein auf den aktuellen
	 * Lieferschein.
	 * 
	 * @param iIdLieferscheinI
	 *            PK des Lieferscheins
	 * @return FilterKriterium[] die Kriterien
	 * @throws java.lang.Throwable
	 *             Ausnahme
	 */
	public FilterKriterium[] createFKFlrlieferscheiniid(Integer iIdLieferscheinI)
			throws Throwable {
		FilterKriterium[] kriterien = null;

		if (iIdLieferscheinI != null) {
			kriterien = new FilterKriterium[1];

			kriterien[0] = new FilterKriterium(
					LieferscheinpositionFac.FLR_LIEFERSCHEINPOSITION_FLRLIEFERSCHEIN
							+ "." + LieferscheinFac.FLR_LIEFERSCHEIN_I_ID,
					true, iIdLieferscheinI.toString(),
					FilterKriterium.OPERATOR_EQUAL, false);
		}

		return kriterien;
	}
	public FilterKriterium[] createFKVerkettet(Integer iIdLieferscheinI)
			throws Throwable {
		FilterKriterium[] kriterien = null;

		if (iIdLieferscheinI != null) {
			kriterien = new FilterKriterium[1];

			kriterien[0] = new FilterKriterium("lieferschein_i_id",
					true, iIdLieferscheinI.toString(),
					FilterKriterium.OPERATOR_EQUAL, false);
		}

		return kriterien;
	}

	/**
	 * Die UI FilterKriterien am Panel Lieferscheinauswahl. <br>
	 * - Auswahl nach Lieferscheinnummer
	 * 
	 * @return QueryType[]
	 */
	protected QueryType[] createQTPanelLieferscheinauswahl() {
		QueryType[] types = new QueryType[4];

		FilterKriterium f1 = new FilterKriterium(
				LieferscheinFac.FLR_LIEFERSCHEIN_C_NR, true, "",
				FilterKriterium.OPERATOR_LIKE, false);

		types[0] = new QueryType(LPMain.getInstance().getTextRespectUISPr(
				"label.lieferscheinnummer"), f1,
				new String[] { FilterKriterium.OPERATOR_EQUAL }, true, true);

		FilterKriterium f2 = new FilterKriterium(
				LieferscheinFac.FLR_LIEFERSCHEIN_FLRKUNDE + "."
						+ KundeFac.FLR_PARTNER + "."
						+ PartnerFac.FLR_PARTNER_NAME1NACHNAMEFIRMAZEILE1,
				true, "", FilterKriterium.OPERATOR_LIKE, false);

		types[1] = new QueryType(LPMain.getInstance().getTextRespectUISPr(
				"label.kunde"), f2,
				new String[] { FilterKriterium.OPERATOR_EQUAL }, true, true);

		FilterKriterium f3 = new FilterKriterium(
				LieferscheinFac.FLR_LIEFERSCHEIN_D_BELEGDATUM, true, "",
				FilterKriterium.OPERATOR_EQUAL, false);

		types[2] = new QueryType(LPMain.getInstance().getTextRespectUISPr(
				"label.belegdatum"), f3, new String[] {
				FilterKriterium.OPERATOR_EQUAL, FilterKriterium.OPERATOR_GTE,
				FilterKriterium.OPERATOR_LTE }, true, // eingabeformat
				// 10.12.2004
				false, false);

		FilterKriterium f4 = new FilterKriterium(
				LieferscheinFac.FLR_LIEFERSCHEIN_FLRKOSTENSTELLE + ".c_nr",
				true, "", FilterKriterium.OPERATOR_LIKE, false);

		types[3] = new QueryType(LPMain.getInstance().getTextRespectUISPr(
				"label.kostenstelle"), f4,
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
	 * Sicht des Lieferscheins auf den Auftrag. <br>
	 * Es sollen alle Positionen angzeigt werden. Wenn eine Position den Status
	 * 'erledigt' hat, muss die offene Menge mit 0 angezeigt werden.
	 * 
	 * @param oLieferscheinDtoI
	 *            der aktuelle Lieferschein
	 * @param iIdAuftragI
	 *            Integer
	 * @return FilterKriterium[] die default Filter Kriterien
	 */

	protected FilterKriterium[] createFKSichtAuftragOffeneAnzeigen() {

		FilterKriterium[] fk = new FilterKriterium[1];

		fk[0] = new FilterKriterium(
				AuftragpositionFac.FLR_AUFTRAGPOSITIONSICHTAUFTRAG_AUFTRAGPOSITIONSTATUS_C_NR,
				true, "'" + AuftragServiceFac.AUFTRAGPOSITIONSTATUS_ERLEDIGT
						+ "'", FilterKriterium.OPERATOR_NOT_EQUAL, false);
		return fk;
	}

	protected FilterKriterium[] createFKLieferscheinSichtAuftrag(
			LieferscheinDto oLieferscheinDtoI, Integer iIdAuftragI) {
		FilterKriterium[] kriterien = null;

		if (oLieferscheinDtoI != null && oLieferscheinDtoI.getIId() != null) {
			if (iIdAuftragI != null) {
				kriterien = new FilterKriterium[1];

				FilterKriterium krit1 = new FilterKriterium(
						AuftragpositionFac.FLR_AUFTRAGPOSITIONSICHTAUFTRAG_AUFTRAG_I_ID,
						true, iIdAuftragI.toString(),
						FilterKriterium.OPERATOR_EQUAL, false);

				/*
				 * FilterKriterium krit3 = new FilterKriterium(
				 * AuftragpositionFac
				 * .FLR_AUFTRAGPOSITIONSICHTAUFTRAG_N_OFFENE_MENGE, true,
				 * String.valueOf(0), FilterKriterium.OPERATOR_NOT_EQUAL,
				 * false);
				 */
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
	 * Default Filter Kriterien fuer eine Liste von Lieferscheintexten
	 * 
	 * @return FilterKriterium[] Default Filter Kriterien
	 * @throws Throwable
	 *             Ausnahme
	 */
	public FilterKriterium[] createFKLieferscheintext() throws Throwable {
		FilterKriterium[] kriterien = new FilterKriterium[2];

		FilterKriterium krit1 = new FilterKriterium("mandant_c_nr", true, "'"
				+ LPMain.getInstance().getTheClient().getMandant() + "'",
				FilterKriterium.OPERATOR_EQUAL, false);
		kriterien[0] = krit1;

		FilterKriterium krit2 = new FilterKriterium(
				LieferscheinServiceFac.FLR_LIEFERSCHEINTEXT_LOCALE_C_NR, true,
				"'" + LPMain.getInstance().getTheClient().getLocUiAsString()
						+ "'", FilterKriterium.OPERATOR_EQUAL, false);

		kriterien[0] = krit1;
		kriterien[1] = krit2;

		return kriterien;
	}

	/**
	 * Direktes Filter Kriterium Lieferschein fuer die Lieferscheinauswahl.
	 * 
	 * @return FilterKriteriumDirekt
	 * @throws Throwable
	 */
	public FilterKriteriumDirekt createFKDLieferscheinnummer() throws Throwable {
		return new FilterKriteriumDirekt("c_nr", "",
				FilterKriterium.OPERATOR_LIKE, LPMain.getInstance()
						.getTextRespectUISPr("ls.lieferscheinnummershort"),
				FilterKriteriumDirekt.PROZENT_LEADING, // Auswertung als '%XX'
				true, // wrapWithSingleQuotes
				false, Facade.MAX_UNBESCHRAENKT); // ignore case
	}

	public FilterKriteriumDirekt createFKDArtikelnummer()
			throws Throwable {

		int iLaenge = ArtikelFac.MAX_ARTIKEL_ARTIKELNUMMER;

		ParametermandantDto parameter = (ParametermandantDto) DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getParametermandant(
						ParameterFac.PARAMETER_ARTIKEL_MAXIMALELAENGE_ARTIKELNUMMER,
						ParameterFac.KATEGORIE_ARTIKEL,
						LPMain.getInstance().getTheClient().getMandant());

		if (parameter.getCWertAsObject() != null) {
			iLaenge = ((Integer) parameter.getCWertAsObject()).intValue();
		}

		return new FilterKriteriumDirekt("flrartikel.c_nr", "",
				FilterKriterium.OPERATOR_LIKE, LPMain.getInstance()
						.getTextRespectUISPr("artikel.artikelnummer"),
				FilterKriteriumDirekt.PROZENT_TRAILING, true, true, iLaenge);

	}

	/**
	 * Direktes Filter Kriterium Lieferschein fuer die Lieferscheinauswahl.
	 * 
	 * @return FilterKriteriumDirekt
	 * @throws Throwable
	 */
	public FilterKriteriumDirekt createFKDProjekt() throws Throwable {
		return new FilterKriteriumDirekt("c_bez_projektbezeichnung", "",
				FilterKriterium.OPERATOR_LIKE, LPMain.getInstance()
						.getTextRespectUISPr("label.projekt"),
				FilterKriteriumDirekt.PROZENT_BOTH, // Auswertung als '%XX'
				true, // wrapWithSingleQuotes
				false, Facade.MAX_UNBESCHRAENKT); // ignore case
	}

	public FilterKriteriumDirekt createFKDAuftragsnummmer() throws Throwable {
		return new FilterKriteriumDirekt("flrauftrag.c_nr", "",
				FilterKriterium.OPERATOR_LIKE, LPMain.getInstance()
						.getTextRespectUISPr("label.auftragnummer"),
				FilterKriteriumDirekt.PROZENT_BOTH, // Auswertung als '%XX'
				true, // wrapWithSingleQuotes
				false, Facade.MAX_UNBESCHRAENKT); // ignore case
	}

	/**
	 * Direktes Filter Kriterium Kundenname fuer die Lieferscheinauswahl.
	 * 
	 * @return FilterKriteriumDirekt
	 */
	public FilterKriteriumDirekt createFKDKundenname() throws Throwable {
		ParametermandantDto parameter = (ParametermandantDto) DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getParametermandant(
						ParameterFac.PARAMETER_PARTNERSUCHE_WILDCARD_BEIDSEITIG,
						ParameterFac.KATEGORIE_PARTNER,
						LPMain.getInstance().getTheClient().getMandant());
		if (((Boolean) parameter.getCWertAsObject() == true)) {
			return new FilterKriteriumDirekt(
					LieferscheinFac.FLR_LIEFERSCHEIN_FLRKUNDE + "."
							+ KundeFac.FLR_PARTNER + "."
							+ PartnerFac.FLR_PARTNER_NAME1NACHNAMEFIRMAZEILE1,
					"", FilterKriterium.OPERATOR_LIKE, LPMain.getInstance()
							.getTextRespectUISPr("label.kunde"),
					FilterKriteriumDirekt.PROZENT_BOTH, // Auswertung als 'XX%'
					true, // wrapWithSingleQuotes
					true, Facade.MAX_UNBESCHRAENKT); // ignore case
		} else {
			return new FilterKriteriumDirekt(
					LieferscheinFac.FLR_LIEFERSCHEIN_FLRKUNDE + "."
							+ KundeFac.FLR_PARTNER + "."
							+ PartnerFac.FLR_PARTNER_NAME1NACHNAMEFIRMAZEILE1,
					"", FilterKriterium.OPERATOR_LIKE, LPMain.getInstance()
							.getTextRespectUISPr("label.kunde"),
					FilterKriteriumDirekt.PROZENT_TRAILING, // Auswertung als
															// 'XX%'
					true, // wrapWithSingleQuotes
					true, Facade.MAX_UNBESCHRAENKT); // ignore case
		}
	}

	public FilterKriterium[] createFKGelieferteLieferscheineEinesKunden(
			Integer kundeIId) throws Throwable {
		FilterKriterium[] filters = new FilterKriterium[3];
		filters[0] = createFKGelieferteLieferscheine()[0];
		filters[1] = createFKGelieferteLieferscheine()[1];
		filters[2] = new FilterKriterium(
				LieferscheinFac.FLR_LIEFERSCHEIN_KUNDE_I_ID_RECHNUNGSADRESSE,
				true, kundeIId.toString(), FilterKriterium.OPERATOR_EQUAL,
				false);
		/** @todo nur verrechenbare PJ 4759 */
		return filters;
	}

	public FilterKriterium[] createFKGelieferteLieferscheineEinesKundenInland(
			Integer kundeIId, Integer landIId) throws Throwable {
		FilterKriterium[] filters = new FilterKriterium[4];
		filters[0] = createFKGelieferteLieferscheine()[0];
		filters[1] = createFKGelieferteLieferscheine()[1];
		filters[2] = new FilterKriterium(/*
										 * LieferscheinFac.FLR_LIEFERSCHEIN_FLRKUNDE
										 */"flrkunderechnungsadresse" + "."
				+ KundeFac.FLR_PARTNER + "."
				+ PartnerFac.FLR_PARTNER_FLRLANDPLZORT + "."
				+ SystemFac.FLR_LP_FLRLAND + "." + SystemFac.FLR_LP_LANDID,
				true, landIId.toString(), FilterKriterium.OPERATOR_EQUAL, false);
		filters[3] = new FilterKriterium(
				LieferscheinFac.FLR_LIEFERSCHEIN_KUNDE_I_ID_RECHNUNGSADRESSE,
				true, kundeIId.toString(), FilterKriterium.OPERATOR_EQUAL,
				false);

		return filters;
	}

	public FilterKriterium[] createFKGelieferteLieferscheine() throws Throwable {
		FilterKriterium[] filters = new FilterKriterium[2];
		filters[0] = SystemFilterFactory.getInstance().createFKMandantCNr()[0];
		filters[1] = new FilterKriterium(
				LieferscheinFac.FLR_LIEFERSCHEIN_LIEFERSCHEINSTATUS_STATUS_C_NR,
				true, "'" + LieferscheinFac.LSSTATUS_GELIEFERT + "'",
				FilterKriterium.OPERATOR_EQUAL, false);
		return filters;
	}

	public FilterKriterium[] createFKLieferscheineSchnellansicht()
			throws Throwable {
		FilterKriterium[] filters = new FilterKriterium[1];
		filters[0] = new FilterKriterium(
				LieferscheinFac.FLR_LIEFERSCHEIN_LIEFERSCHEINSTATUS_STATUS_C_NR,
				true, "('" + LieferscheinFac.LSSTATUS_STORNIERT + "','"
						+ LieferscheinFac.LSSTATUS_ERLEDIGT + "','"
						+ LieferscheinFac.LSSTATUS_VERRECHNET + "')",
				FilterKriterium.OPERATOR_NOT_IN, false);

		return filters;
	}

	/**
	 * Einschraenken der 1:n Listen im Lieferschein auf den aktuellen
	 * Lieferschein.
	 * 
	 * @param iIdLieferscheinI
	 *            PK des Lieferscheins
	 * @return FilterKriterium[] die Kriterien
	 * @throws java.lang.Throwable
	 *             Ausnahme
	 */
	public FilterKriterium[] createFKAuftraegeEinesLieferscheins(
			Integer iIdLieferscheinI) throws Throwable {
		FilterKriterium[] kriterien = null;

		if (iIdLieferscheinI != null) {
			kriterien = new FilterKriterium[1];

			kriterien[0] = new FilterKriterium(
					LieferscheinpositionFac.FLR_LIEFERSCHEINPOSITION_FLRLIEFERSCHEIN
							+ "." + LieferscheinFac.FLR_LIEFERSCHEIN_I_ID,
					true, iIdLieferscheinI.toString(),
					FilterKriterium.OPERATOR_EQUAL, false);
		}

		return kriterien;
	}

	public FilterKriterium[] createFKAuftrag(Integer auftragIId)
			throws Throwable {
		FilterKriterium[] filters = new FilterKriterium[1];
		filters[0] = new FilterKriterium(
				LieferscheinFac.FLR_LIEFERSCHEIN_AUFTRAG_I_ID, true, ""
						+ auftragIId, FilterKriterium.OPERATOR_EQUAL, false);
		return filters;
	}

	public PanelQueryFLR createPanelFLRBegruendung(
			InternalFrame internalFrameI, Integer selectedId,
			boolean bMitLeerenButton) throws Throwable {

		String[] aWhichButtonIUse = null;
		if (bMitLeerenButton) {
			aWhichButtonIUse = new String[] { PanelBasis.ACTION_REFRESH,
					PanelBasis.ACTION_LEEREN };
		} else {
			aWhichButtonIUse = new String[] { PanelBasis.ACTION_REFRESH };
		}
		PanelQueryFLR panelQueryFLRBegruendung = new PanelQueryFLR(null, null,
				QueryParameters.UC_ID_BEGRUENDUNG, aWhichButtonIUse,
				internalFrameI, LPMain.getTextRespectUISPr("ls.begruendung"));

		panelQueryFLRBegruendung.setSelectedId(selectedId);
		return panelQueryFLRBegruendung;
	}

	public PanelQueryFLR createPanelQueryFLRLieferschein(
			InternalFrame internalFrameI, FilterKriterium[] fk, String sTitle, FilterKriterium fkVersteckt)
			throws Throwable {
		String[] aWhichButtonIUse = { PanelBasis.ACTION_REFRESH, };

		QueryType[] qt = null;
		PanelQueryFLR panelQueryFLRLieferschein = new PanelQueryFLR(qt, fk,
				QueryParameters.UC_ID_LIEFERSCHEIN, aWhichButtonIUse,
				internalFrameI, sTitle,fkVersteckt,null);
		panelQueryFLRLieferschein.befuellePanelFilterkriterienDirekt(
				createFKDLieferscheinnummer(), createFKDKundenname());
		panelQueryFLRLieferschein.addDirektFilter(LieferscheinFilterFactory
				.getInstance().createFKDProjekt());

		panelQueryFLRLieferschein.addDirektFilter(LieferscheinFilterFactory
				.getInstance().createFKDAuftragsnummmer());

		return panelQueryFLRLieferschein;

	}
}
