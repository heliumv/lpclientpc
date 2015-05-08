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
package com.lp.client.partner;

import java.util.Map;
import java.util.TreeMap;

import com.lp.client.artikel.ArtikelFilterFactory;
import com.lp.client.frame.component.*;
import com.lp.client.pc.*;
import com.lp.client.util.fastlanereader.gui.*;
import com.lp.server.artikel.service.ArtikelFac;
import com.lp.server.artikel.service.ArtikelkommentarFac;
import com.lp.server.partner.service.*;
import com.lp.server.system.service.*;
import com.lp.server.util.fastlanereader.service.query.*;
import com.lp.util.*;
import com.lp.client.system.SystemFilterFactory;
import com.lp.client.frame.ExceptionLP;
import com.lp.server.util.Facade;
import com.lp.client.frame.delegate.DelegateFactory;

/**
 * <p>
 * Diese Klasse ist ein Singleton und kuemmert sich um alle Partnerfilter.
 * </p>
 * 
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2008
 * </p>
 * 
 * <p>
 * Erstellung: Josef Ornetsmueller; 18.04.05
 * </p>
 * 
 * <p>
 * 
 * @author $Author: christian $
 *         </p>
 * 
 * @version $Revision: 1.18 $ Date $Date: 2012/11/07 09:41:44 $
 */
public class PartnerFilterFactory {
	private static PartnerFilterFactory filterFactory = null;

	private PartnerFilterFactory() {
		// Singleton.
	}

	/**
	 * Hole das Singelton FilterFactory.
	 * 
	 * @return LPMain
	 */
	static public PartnerFilterFactory getInstance() {
		if (filterFactory == null) {
			filterFactory = new PartnerFilterFactory();
		}
		return filterFactory;
	}

	public FilterKriterium[] createFVersteckt(boolean bVersteckt) {
		FilterKriterium[] aKriterien = new FilterKriterium[1];
		/*
		 * PJ 09/0014039 aKriterien[0] = new FilterKriterium(
		 * SystemFac.FLR_LP_VERSTECKT, true, "'" +
		 * Helper.boolean2Short(bVersteckt) + "'",
		 * FilterKriterium.OPERATOR_LIKE, false);
		 */

		aKriterien[0] = new FilterKriterium(SystemFac.FLR_LP_VERSTECKT, true,
				"" + Helper.boolean2Short(bVersteckt),
				FilterKriterium.OPERATOR_EQUAL, false);

		return aKriterien;
	}

	public FilterKriterium[] createFKPartnerKey(Integer artikelIId)
			throws Throwable {
		// Handartikel nicht anzeigen
		FilterKriterium[] kriterien = new FilterKriterium[1];
		kriterien[0] = new FilterKriterium("i_id", true, artikelIId + "",
				FilterKriterium.OPERATOR_EQUAL, false);

		return kriterien;
	}

	public QueryType[] createFFirmaNNPartnerart() {
		QueryType[] types = new QueryType[2];

		FilterKriterium f1 = new FilterKriterium(
				PartnerFac.FLR_PARTNER_NAME1NACHNAMEFIRMAZEILE1, true, "",
				FilterKriterium.OPERATOR_LIKE, false);

		types[0] = new QueryType(LPMain.getInstance().getTextRespectUISPr(
				"part.firma_nachname"), f1,
				new String[] { FilterKriterium.OPERATOR_LIKE }, true, true);

		FilterKriterium f2 = new FilterKriterium(
				PartnerFac.FLR_PARTNER_PARTNERART, true, "",
				FilterKriterium.OPERATOR_LIKE, false);

		types[1] = new QueryType(LPMain.getInstance().getTextRespectUISPr(
				"part.partnerart"), f2,
				new String[] { FilterKriterium.OPERATOR_LIKE }, true, true);

		return types;
	}

	public PanelQueryFLR createPanelFLRPartner(InternalFrame internalFrameI)
			throws Throwable {
		String[] aWhichButtonIUse = { PanelBasis.ACTION_FILTER };

		PanelQueryFLR plPartner = new PanelQueryFLR(createFFirmaNNPartnerart(),
				null, QueryParameters.UC_ID_PARTNER, aWhichButtonIUse,
				internalFrameI,
				LPMain.getTextRespectUISPr("title.partnerauswahlliste"));

		plPartner
				.befuellePanelFilterkriterienDirektUndVersteckte(
						PartnerFilterFactory.getInstance()
								.createFKDPartnerName(), PartnerFilterFactory
								.getInstance().createFKDPartnerLandPLZOrt(),
						PartnerFilterFactory.getInstance().createFKVPartner());

		plPartner.addDirektFilter(PartnerFilterFactory.getInstance()
				.createFKDPartnerErweiterteSuche());

		ParametermandantDto parameter = DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getMandantparameter(LPMain.getTheClient().getMandant(),
						ParameterFac.KATEGORIE_ALLGEMEIN,
						ParameterFac.PARAMETER_SUCHEN_INKLUSIVE_KBEZ);
		boolean bSuchenInklusiveKbez = (java.lang.Boolean) parameter
				.getCWertAsObject();

		if (!bSuchenInklusiveKbez) {
			plPartner.addDirektFilter(PartnerFilterFactory.getInstance()
					.createFKDPartnerKurzbezeichnung());
		}

		return plPartner;
	}

	public PanelQueryFLR createPanelFLRPartnerPartnerklassen(
			InternalFrame internalFrameI, boolean bShowFilterButton,
			boolean bShowLeerenButton, Integer selectedId) throws Throwable {

		String[] aWhichButtonIUse = SystemFilterFactory.getInstance()
				.createButtonArray(bShowFilterButton, bShowLeerenButton);

		aWhichButtonIUse = new String[] { PanelBasis.ACTION_FILTER,
				PanelBasis.ACTION_LEEREN };

		PanelQueryFLR plPartnerklassen = new PanelQueryFLR(null, null,
				QueryParameters.UC_ID_PARTNERKLASSE, aWhichButtonIUse,
				internalFrameI,
				LPMain.getTextRespectUISPr("auftrag.partnerklassen"));
		plPartnerklassen.setSelectedId(selectedId);

		return plPartnerklassen;
	}

	public PanelQueryFLR createPanelFLRPartner(InternalFrame internalFrameI,
			Integer selectedId, boolean bMitLeerenButton) throws Throwable {
		String[] aWhichButtonIUse = null;

		if (bMitLeerenButton) {
			aWhichButtonIUse = new String[] { PanelBasis.ACTION_FILTER,
					PanelBasis.ACTION_LEEREN };
		} else {
			aWhichButtonIUse = new String[] { PanelBasis.ACTION_FILTER };
		}

		PanelQueryFLR plPartner = new PanelQueryFLR(createFFirmaNNPartnerart(),
				null, QueryParameters.UC_ID_PARTNER, aWhichButtonIUse,
				internalFrameI, LPMain.getInstance().getTextRespectUISPr(
						"title.partnerauswahlliste"));

		plPartner
				.befuellePanelFilterkriterienDirektUndVersteckte(
						PartnerFilterFactory.getInstance()
								.createFKDPartnerName(), PartnerFilterFactory
								.getInstance().createFKDPartnerLandPLZOrt(),
						PartnerFilterFactory.getInstance().createFKVPartner());

		plPartner.addDirektFilter(PartnerFilterFactory.getInstance()
				.createFKDPartnerErweiterteSuche());
		if (selectedId != null) {
			plPartner.setSelectedId(selectedId);
		}

		return plPartner;
	}

	public FilterKriteriumDirekt createFKDKundePartnerName(String sLabel)
			throws Throwable {
		FilterKriteriumDirekt fKDPartnername = null;
		ParametermandantDto parameter = (ParametermandantDto) DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getParametermandant(
						ParameterFac.PARAMETER_PARTNERSUCHE_WILDCARD_BEIDSEITIG,
						ParameterFac.KATEGORIE_PARTNER,
						LPMain.getInstance().getTheClient().getMandant());
		if (((Boolean) parameter.getCWertAsObject() == true)) {

			fKDPartnername = new FilterKriteriumDirekt(
					KundeFac.FLR_PARTNER_NAME1NACHNAMEFIRMAZEILE1, "",
					FilterKriterium.OPERATOR_LIKE, sLabel,
					FilterKriteriumDirekt.PROZENT_BOTH, true, true,
					Facade.MAX_UNBESCHRAENKT); // wrapWithSingleQuotes
		} else {
			fKDPartnername = new FilterKriteriumDirekt(
					KundeFac.FLR_PARTNER_NAME1NACHNAMEFIRMAZEILE1, "",
					FilterKriterium.OPERATOR_LIKE, sLabel,
					FilterKriteriumDirekt.PROZENT_TRAILING, true, true,
					Facade.MAX_UNBESCHRAENKT); // wrapWithSingleQuotes

		}
		return fKDPartnername;
	}

	public FilterKriteriumDirekt createFKDLieferantPartnerName()
			throws Throwable {
		FilterKriteriumDirekt fKDPartnername = null;
		ParametermandantDto parameter = (ParametermandantDto) DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getParametermandant(
						ParameterFac.PARAMETER_PARTNERSUCHE_WILDCARD_BEIDSEITIG,
						ParameterFac.KATEGORIE_PARTNER,
						LPMain.getInstance().getTheClient().getMandant());
		if (((Boolean) parameter.getCWertAsObject() == true)) {
			fKDPartnername = new FilterKriteriumDirekt(
					LieferantFac.FLR_PARTNER_NAME1NACHNAMEFIRMAZEILE1, "",
					FilterKriterium.OPERATOR_LIKE, LPMain.getInstance()
							.getTextRespectUISPr("lp.firma"),
					FilterKriteriumDirekt.PROZENT_BOTH, true, true,
					Facade.MAX_UNBESCHRAENKT); // wrapWithSingleQuotes
		} else {
			fKDPartnername = new FilterKriteriumDirekt(
					LieferantFac.FLR_PARTNER_NAME1NACHNAMEFIRMAZEILE1, "",
					FilterKriterium.OPERATOR_LIKE, LPMain.getInstance()
							.getTextRespectUISPr("lp.firma"),
					FilterKriteriumDirekt.PROZENT_TRAILING, true, true,
					Facade.MAX_UNBESCHRAENKT); // wrapWithSingleQuotes

		}
		return fKDPartnername;
	}

	public FilterKriteriumDirekt createFKDLieferantPartnerKurzbezeichnung() {

		FilterKriteriumDirekt fKDPartnername = new FilterKriteriumDirekt(
				LieferantFac.FLR_PARTNER + "." + PartnerFac.FLR_PARTNER_C_KBEZ,
				"", FilterKriterium.OPERATOR_LIKE, LPMain.getInstance()
						.getTextRespectUISPr("label.kurzbezeichnung"),
				FilterKriteriumDirekt.PROZENT_TRAILING, true, true,
				Facade.MAX_UNBESCHRAENKT); // wrapWithSingleQuotes
		return fKDPartnername;
	}

	
	public FilterKriteriumDirekt createFKDPartnerKurzbezeichnung() {

		FilterKriteriumDirekt fKDPartnername = new FilterKriteriumDirekt(
				PartnerFac.FLR_PARTNER_C_KBEZ, "",
				FilterKriterium.OPERATOR_LIKE, LPMain.getInstance()
						.getTextRespectUISPr("label.kurzbezeichnung"),
				FilterKriteriumDirekt.PROZENT_TRAILING, true, true,
				Facade.MAX_UNBESCHRAENKT); // wrapWithSingleQuotes
		return fKDPartnername;
	}

	public FilterKriteriumDirekt createFKDPartnerErweiterteSuche() {

		FilterKriteriumDirekt fKDPartnername = new FilterKriteriumDirekt(
				PartnerFac.PARTNERQP1_ERWEITERTE_SUCHE, "",
				FilterKriterium.OPERATOR_LIKE, LPMain.getInstance()
						.getTextRespectUISPr("part.erweitertesuche"),
				FilterKriteriumDirekt.EXTENDED_SEARCH, false, true,
				Facade.MAX_UNBESCHRAENKT); // wrapWithSingleQuotes
		return fKDPartnername;
	}

	public FilterKriteriumDirekt createFKDPartnersucheNachTelefonnummer() {

		FilterKriteriumDirekt fKDPartnername = new FilterKriteriumDirekt(
				PartnerFac.PARTNERQP1_TELEFONNUMMERN_SUCHE, "",
				FilterKriterium.OPERATOR_LIKE, LPMain.getInstance()
						.getTextRespectUISPr("part.telefonnummernsuche"),
				FilterKriteriumDirekt.PROZENT_BOTH, true, true,
				Facade.MAX_UNBESCHRAENKT); // wrapWithSingleQuotes
		return fKDPartnername;
	}

	
	public FilterKriteriumDirekt createFKDKundePartnerKurzbezeichnung() {

		FilterKriteriumDirekt fKDPartnername = new FilterKriteriumDirekt(
				KundeFac.FLR_PARTNER + "." + PartnerFac.FLR_PARTNER_C_KBEZ, "",
				FilterKriterium.OPERATOR_LIKE, LPMain.getInstance()
						.getTextRespectUISPr("label.kurzbezeichnung"),
				FilterKriteriumDirekt.PROZENT_TRAILING, true, true,
				Facade.MAX_UNBESCHRAENKT); // wrapWithSingleQuotes
		return fKDPartnername;
	}

	public FilterKriteriumDirekt createFKDKundeKundennummer() {

		FilterKriteriumDirekt fKDPartnername = new FilterKriteriumDirekt(
				KundeFac.FLR_KUNDE_I_KUNDENNUMMER, "",
				FilterKriterium.OPERATOR_LIKE, LPMain.getInstance()
						.getTextRespectUISPr("lp.kundennummer"),
				FilterKriteriumDirekt.PROZENT_TRAILING, true, true,
				Facade.MAX_UNBESCHRAENKT); // wrapWithSingleQuotes
		return fKDPartnername;
	}

	public FilterKriterium[] createFKLieferantMandantPartner() throws Throwable {

		FilterKriterium[] krit = new FilterKriterium[1];
		krit[0] = new FilterKriterium("mandant_c_nr", true, "'"
				+ LPMain.getInstance().getTheClient().getMandant() + "'",
				FilterKriterium.OPERATOR_LIKE, false);

		return krit;
	}

	public FilterKriterium[] createFKPartnerPartnerart(String cNrListNotIn)
			throws Throwable {

		FilterKriterium[] krit = new FilterKriterium[1];
		krit[0] = new FilterKriterium("partnerart_c_nr", true, "("
				+ cNrListNotIn + ")", FilterKriterium.OPERATOR_NOT_IN, false);

		return krit;
	}

	public FilterKriterium[] createFKLieferantMandantPartnerVersteckt()
			throws Throwable {

		FilterKriterium[] krit = new FilterKriterium[2];
		krit[0] = new FilterKriterium("mandant_c_nr", true, "'"
				+ LPMain.getInstance().getTheClient().getMandant() + "'",
				FilterKriterium.OPERATOR_LIKE, false);

		krit[1] = new FilterKriterium(LieferantFac.FLR_PARTNER + "."
				+ PartnerFac.FLR_PARTNER_VERSTECKT, true,
				Helper.boolean2Short(false) + "",
				FilterKriterium.OPERATOR_EQUAL, false);

		return krit;
	}

	public FilterKriteriumDirekt createFKDKundePartnerOrt() {

		FilterKriteriumDirekt fKDPartnerOrt = new FilterKriteriumDirekt(
				KundeFac.FLR_PARTNER_LANDPLZORT_ORT_NAME, "",
				FilterKriterium.OPERATOR_LIKE, LPMain.getInstance()
						.getTextRespectUISPr("lp.ort"),
				FilterKriteriumDirekt.PROZENT_BOTH, true, true,
				Facade.MAX_UNBESCHRAENKT);
		return fKDPartnerOrt;
	}

	public FilterKriteriumDirekt createFKDLieferantPartnerOrt() {

		FilterKriteriumDirekt fKDPartnerOrt = new FilterKriteriumDirekt(
				LieferantFac.FLR_PARTNER_LANDPLZORT_ORT_NAME, "",
				FilterKriterium.OPERATOR_LIKE, LPMain.getInstance()
						.getTextRespectUISPr("lp.ort"),
				FilterKriteriumDirekt.PROZENT_BOTH, true, // wrapWithSingleQuotes
				true, Facade.MAX_UNBESCHRAENKT);
		return fKDPartnerOrt;
	}

	public FilterKriterium[] createFKKunde(Integer iIdKundeI) {

		FilterKriterium[] aKriterien = new FilterKriterium[1];

		aKriterien[0] = new FilterKriterium(
				KundesachbearbeiterFac.FLR_KUNDE_I_ID, true, "'" + iIdKundeI
						+ "'", FilterKriterium.OPERATOR_EQUAL, false);

		return aKriterien;
	}

	public FilterKriterium[] createFKKundeMandantPartner() throws Throwable {

		FilterKriterium[] krit = new FilterKriterium[2];
		krit[0] = new FilterKriterium("mandant_c_nr", true, "'"
				+ LPMain.getInstance().getTheClient().getMandant() + "'",
				FilterKriterium.OPERATOR_LIKE, false);

		krit[1] = new FilterKriterium(
				KundeFac.FLR_KUNDE_B_VERSTECKTERLIEFERANT, true,
				Helper.boolean2Short(false) + "",
				FilterKriterium.OPERATOR_EQUAL, false);

		return krit;
	}

	public QueryType[] createQTKundeABC() {
		QueryType[] types = new QueryType[1];
		types[0] = new QueryType("ABC", createFKABC(types),
				new String[] { FilterKriterium.OPERATOR_LIKE }, true, true);

		return types;
	}

	public FilterKriterium[] createFKKundeLieferstatistik(Integer kundeIId)
			throws Throwable {

		FilterKriterium[] krit = new FilterKriterium[2];
		krit[0] = new FilterKriterium(KundeFac.FLR_KUNDE_I_ID, true,
				kundeIId.toString(), FilterKriterium.OPERATOR_LIKE, false);
		krit[1] = new FilterKriterium("mandant_c_nr", true, "'"
				+ LPMain.getInstance().getTheClient().getMandant() + "'",
				FilterKriterium.OPERATOR_EQUAL, false);
		return krit;
	}

	private FilterKriterium createFKABC(QueryType[] types) {
		return new FilterKriterium(KundeFac.FLR_KUNDE_C_ABC, // flrres: sind
																// alles aliase
				true, "", FilterKriterium.OPERATOR_LIKE, false);
	}

	public QueryType[] createQTKundeABCDebitoren() {
		QueryType[] types = new QueryType[4];

		FilterKriterium f1 = new FilterKriterium(KundeFac.FLR_KONTO + ".c_nr",
				true, "", FilterKriterium.OPERATOR_LIKE, false);

		types[0] = new QueryType(
				LPMain.getTextRespectUISPr("lp.debitorenkonto"), f1,
				new String[] { FilterKriterium.OPERATOR_LIKE }, true, true);

		types[1] = createQTKundeABC()[0];

		types[2] = new QueryType(LPMain.getTextRespectUISPr("lp.lkz1"),
				new FilterKriterium("flrpartner."
						+ PartnerFac.FLR_PARTNER_FLRLANDPLZORT
						+ ".flrland.c_lkz", true, "",
						FilterKriterium.OPERATOR_LIKE, true),
				new String[] { FilterKriterium.OPERATOR_LIKE }, true, true);

		types[3] = new QueryType(LPMain.getTextRespectUISPr("menueentry.plz"),
				new FilterKriterium("flrpartner."
						+ PartnerFac.FLR_PARTNER_FLRLANDPLZORT + ".c_plz",
						true, "", FilterKriterium.OPERATOR_LIKE, true),
				new String[] { FilterKriterium.OPERATOR_LIKE }, true, true);

		return types;
	}

	public QueryType[] createQTPartnerart() {
		QueryType[] types = new QueryType[1];

		FilterKriterium f2 = new FilterKriterium(
				PartnerFac.FLR_PARTNER_PARTNERART, true, "",
				FilterKriterium.OPERATOR_LIKE, false);

		types[0] = new QueryType("Partnerart", f2,
				new String[] { FilterKriterium.OPERATOR_LIKE }, true, true);

		return types;
	}

	public FilterKriteriumDirekt createFKDPartnerName() throws Throwable {
		FilterKriteriumDirekt fKDPartnername = null;
		ParametermandantDto parameter = (ParametermandantDto) DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getParametermandant(
						ParameterFac.PARAMETER_PARTNERSUCHE_WILDCARD_BEIDSEITIG,
						ParameterFac.KATEGORIE_PARTNER,
						LPMain.getInstance().getTheClient().getMandant());
		if (((Boolean) parameter.getCWertAsObject() == true)) {

			fKDPartnername = new FilterKriteriumDirekt(
					PartnerFac.FLR_PARTNER_NAME1NACHNAMEFIRMAZEILE1, "",
					FilterKriterium.OPERATOR_LIKE, LPMain.getInstance()
							.getTextRespectUISPr("lp.name"),
					FilterKriteriumDirekt.PROZENT_BOTH, true, // wrapWithSingleQuotes
					true, Facade.MAX_UNBESCHRAENKT); // ignorecase: 0 Ignoriere
														// Grosskleinschreibung
		} else {
			fKDPartnername = new FilterKriteriumDirekt(
					PartnerFac.FLR_PARTNER_NAME1NACHNAMEFIRMAZEILE1, "",
					FilterKriterium.OPERATOR_LIKE, LPMain.getInstance()
							.getTextRespectUISPr("lp.name"),
					FilterKriteriumDirekt.PROZENT_TRAILING, true, // wrapWithSingleQuotes
					true, Facade.MAX_UNBESCHRAENKT);

		}
		return fKDPartnername;
	}

	public FilterKriteriumDirekt createFKDAnsprechpartnerPartnerName() {
		FilterKriteriumDirekt fkDirekt1 = new FilterKriteriumDirekt(
				AnsprechpartnerFac.FLR_ANSPRECHPARTNER_PARTNERANSPRECHPARTNER
						+ "." + PartnerFac.FLR_PARTNER_NAME1NACHNAMEFIRMAZEILE1,
				"", FilterKriterium.OPERATOR_LIKE, LPMain.getInstance()
						.getTextRespectUISPr("lp.name"),
				FilterKriteriumDirekt.PROZENT_TRAILING, true, // wrapWithSingleQuotes
				true, Facade.MAX_UNBESCHRAENKT); // ignorecase: 0 Ignoriere
													// Grosskleinschreibung
		return fkDirekt1;
	}

	public FilterKriteriumDirekt createFKDPartnerLandPLZOrt() {
		FilterKriteriumDirekt fkDirekt2 = new FilterKriteriumDirekt(
				PartnerFac.FLR_PARTNER_LANDPLZORT_ORT_NAME, "",
				FilterKriterium.OPERATOR_LIKE, LPMain.getInstance()
						.getTextRespectUISPr("lp.ort"),
				FilterKriteriumDirekt.PROZENT_TRAILING, true, true,
				Facade.MAX_UNBESCHRAENKT);
		return fkDirekt2;
	}

	// filterkritversteckt: 0
	public FilterKriterium createFKVPartner() {
		FilterKriterium fkVersteckt = new FilterKriterium(
				PartnerFac.FLR_PARTNER_VERSTECKT, true, "(1)", // wenn das
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

	public FilterKriterium createFKVKunde() {
		FilterKriterium fkVersteckt = new FilterKriterium(KundeFac.FLR_PARTNER
				+ "." + PartnerFac.FLR_PARTNER_VERSTECKT, true, "(1)", // wenn
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

	public FilterKriterium createFKVAnsprechpartner() {
		FilterKriterium fkVersteckt = new FilterKriterium(
				AnsprechpartnerFac.FLR_ANSPRECHPARTNER_VERSTECKT, true, "(1)", // wenn
																				// das
																				// Kriterium
																				// verwendet
																				// wird
																				// ,
																				// sollen
																				// die
																				// versteckten
																				// nicht
																				// mitangezeigt
																				// werden
				FilterKriterium.OPERATOR_NOT_IN, false);

		return fkVersteckt;
	}

	public FilterKriterium createFKVLieferant() {
		FilterKriterium fkVersteckt = new FilterKriterium(
				LieferantFac.FLR_PARTNER + "."
						+ PartnerFac.FLR_PARTNER_VERSTECKT, true, "(1)", // wenn
																			// das
																			// Kriterium
																			// verwendet
																			// wird
																			// ,
																			// sollen
																			// die
																			// versteckten
																			// nicht
																			// mitangezeigt
																			// werden
				FilterKriterium.OPERATOR_NOT_IN, false);

		return fkVersteckt;
	}

	/**
	 * Default Filterkriterium fuer Filter nach PASelektion. <br>
	 * Bedingung: Attributname im FLR ist
	 * PartnerFac.FLR_PASELEKTION_PARTNER_I_ID.
	 * 
	 * @param iiPAPartnerI
	 *            Integer
	 * @return FilterKriterium[]
	 */
	public FilterKriterium[] createFKPASelektion(Integer iiPAPartnerI) {
		FilterKriterium[] kriterien = new FilterKriterium[1];
		FilterKriterium krit1 = new FilterKriterium(
				PartnerFac.FLR_PASELEKTION_PARTNER_I_ID, true, "'"
						+ iiPAPartnerI.toString() + "'",
				FilterKriterium.OPERATOR_EQUAL, false);
		kriterien[0] = krit1;
		return kriterien;
	}
	public FilterKriterium[] createFKPartnerkommentar(Integer iiPAPartnerI, boolean bKunde) {
		FilterKriterium[] kriterien = new FilterKriterium[2];
		FilterKriterium krit1 = new FilterKriterium(
				PartnerServicesFac.FLR_PARTNERKOMMENTAR_PARTNER_I_ID, true, ""
						+ iiPAPartnerI.toString() + "",
				FilterKriterium.OPERATOR_EQUAL, false);
		
		kriterien[0] = krit1;
		
		FilterKriterium krit2 = new FilterKriterium(
				PartnerServicesFac.FLR_PARTNERKOMMENTAR_B_KUNDE, true, ""+ Helper.boolean2Short(bKunde),
				FilterKriterium.OPERATOR_EQUAL, false);
		kriterien[1] = krit2;
		
		return kriterien;
	}

	/**
	 * Default Filterkriterium fuer Filter nach Partner. <br>
	 * Bedingung: Attributname im FLR ist PartnerFac.FLR_PARTNER_I_ID.
	 * 
	 * @param iiPartnerI
	 *            Integer
	 * @return FilterKriterium[]
	 */
	public FilterKriterium[] createFKPartner(Integer iiPartnerI) {
		FilterKriterium[] kriterien = new FilterKriterium[1];
		FilterKriterium krit1 = new FilterKriterium(
				PartnerFac.FLR_PARTNER_I_ID, true, "'" + iiPartnerI.toString()
						+ "'", FilterKriterium.OPERATOR_EQUAL, false);
		kriterien[0] = krit1;
		return kriterien;
	}

	public FilterKriterium[] createFKSerienbriefSelektion(Integer iiSerienbriefI) {
		FilterKriterium[] kriterien = new FilterKriterium[1];
		FilterKriterium krit1 = new FilterKriterium(
				PartnerFac.FLR_SERIENBRIEFSELEKTION_SERIENBRIEF_I_ID, true, "'"
						+ iiSerienbriefI.toString() + "'",
				FilterKriterium.OPERATOR_EQUAL, false);
		kriterien[0] = krit1;
		return kriterien;
	}

	/**
	 * Default Filterkriterium fuer Filter nach Partner. <br>
	 * Bedingung: Attributname im FLR ist
	 * PartnerFac.FLR_PARTNER_KOMMUNIKATION_PARTNER_I_ID.
	 * 
	 * @param iiKommunikationPartnerI
	 *            Integer
	 * @return FilterKriterium[]
	 */
	public FilterKriterium[] createFKPartnerKommunikationPartner(
			Integer iiKommunikationPartnerI) {
		FilterKriterium[] kriterien = new FilterKriterium[1];
		FilterKriterium krit1 = new FilterKriterium(
				PartnerFac.FLR_PARTNER_KOMMUNIKATION_PARTNER_I_ID, true, "'"
						+ iiKommunikationPartnerI.toString() + "'",
				FilterKriterium.OPERATOR_EQUAL, false);
		kriterien[0] = krit1;
		return kriterien;
	}

	/**
	 * Default Filterkriterium fuer Filter nach Partnerbank. <br>
	 * Bedingung: Attributname im FLR ist BankFac.FLR_PARTNERBANK_PARTNER_I_ID.
	 * 
	 * @param iiPartnerbankPartnerI
	 *            Integer
	 * @return FilterKriterium[]
	 */
	public FilterKriterium[] createFKPartnerbank(Integer iiPartnerbankPartnerI) {
		FilterKriterium[] kriterien = new FilterKriterium[1];
		FilterKriterium krit1 = new FilterKriterium(
				BankFac.FLR_PARTNERBANK_PARTNER_I_ID, true, "'"
						+ iiPartnerbankPartnerI.toString() + "'",
				FilterKriterium.OPERATOR_EQUAL, false);
		kriterien[0] = krit1;
		return kriterien;
	}

	/**
	 * Default Filterkriterium fuer Filter nach Ansprechpartner. <br>
	 * Bedingung: Attributname im FLR ist AnsprechpartnerFac.FLR_PARTNER_I_ID.
	 * 
	 * @param iIdAnsprechpartner
	 *            Integer
	 * @return FilterKriterium[]
	 */
	public FilterKriterium[] createFKAnsprechpartner(Integer iIdAnsprechpartner) {
		FilterKriterium[] kriterien = new FilterKriterium[1];
		FilterKriterium krit1 = new FilterKriterium(
				AnsprechpartnerFac.FLR_ANSPRECHPARTNER_PARTNER_I_ID, true, "'"
						+ iIdAnsprechpartner.toString() + "'",
				FilterKriterium.OPERATOR_EQUAL, false);
		kriterien[0] = krit1;
		return kriterien;
	}

	public FilterKriterium[] createFKKontakt(Integer iIdPartner) {
		FilterKriterium[] kriterien = new FilterKriterium[1];
		FilterKriterium krit1 = new FilterKriterium("flrpartner.i_id", true,
				iIdPartner + "", FilterKriterium.OPERATOR_EQUAL, false);
		kriterien[0] = krit1;
		return kriterien;
	}

	public FilterKriterium[] createFKWiedervorlage(Integer iIdPartner) {
		FilterKriterium[] kriterien = new FilterKriterium[1];
		FilterKriterium krit1 = new FilterKriterium("t_erledigt", true, "",
				FilterKriterium.OPERATOR_NOT_NULL, false);
		kriterien[0] = krit1;
		return kriterien;
	}

	/**
	 * Default Filterkriterium fuer Filter nach Kurzbrief. <br>
	 * Bedingung: Attributname im FLR ist
	 * PartnerFac.FLR_PARTNER_KURZBRIEF_PARTNER_I_ID.
	 * 
	 * @param iIdKurzbriefpartner
	 *            Integer
	 * @param belegartCNr
	 *            String
	 * @return FilterKriterium[]
	 */
	public FilterKriterium[] createFKKurzbriefpartner(
			Integer iIdKurzbriefpartner, String belegartCNr) {
		FilterKriterium[] kriterien = new FilterKriterium[2];
		FilterKriterium krit1 = new FilterKriterium(
				PartnerFac.FLR_PARTNER_KURZBRIEF_PARTNER_I_ID, true, "'"
						+ iIdKurzbriefpartner.toString() + "'",
				FilterKriterium.OPERATOR_EQUAL, false);
		kriterien[0] = krit1;
		FilterKriterium krit2 = new FilterKriterium("belegart_c_nr", true, "'"
				+ belegartCNr + "'", FilterKriterium.OPERATOR_EQUAL, false);
		kriterien[1] = krit2;
		return kriterien;
	}

	/**
	 * Default Filterkriterium fuer Filter nach Partner. <br>
	 * Bedingung: Attributname im FLR ist
	 * PartnerFac.FLR_SERIENBRIEFSELEKTION_SERIENBRIEF_I_ID.
	 * 
	 * @param iIdSerienbriefI
	 *            Integer
	 * @return FilterKriterium[]
	 */
	public FilterKriterium[] createFKSerienbrief(Integer iIdSerienbriefI) {
		FilterKriterium[] kriterien = new FilterKriterium[1];
		FilterKriterium krit1 = new FilterKriterium(
				PartnerFac.FLR_SERIENBRIEFSELEKTION_SERIENBRIEF_I_ID, true, "'"
						+ iIdSerienbriefI + "'",
				FilterKriterium.OPERATOR_EQUAL, false);
		kriterien[0] = krit1;
		return kriterien;
	}

	/**
	 * Default Filterkriterium fuer Filter nach Lieferanten. <br>
	 * Bedingung: Attributname im FLR ist
	 * LieferantFac.FLR_ID_COMP_LF_LIEFERANT_I_ID.
	 * 
	 * @param iIdLieferantI
	 *            Integer
	 * @return FilterKriterium[]
	 */
	public FilterKriterium[] createFKLieferant(Integer iIdLieferantI) {
		FilterKriterium[] kriterien = new FilterKriterium[1];
		FilterKriterium krit1 = new FilterKriterium(
				LieferantFac.FLR_ID_COMP_LF_LIEFERANT_I_ID, true, "'"
						+ iIdLieferantI.toString() + "'",
				FilterKriterium.OPERATOR_EQUAL, false);
		kriterien[0] = krit1;
		return kriterien;
	}

	/**
	 * Default Filterkriterium fuer Filter nach Lieferanten. <br>
	 * Bedingung: Attributname im FLR ist
	 * LieferantFac.FLR_ID_COMP_LF_LIEFERANT_I_ID.
	 * 
	 * @param iIdLieferantI
	 *            Integer
	 * @return FilterKriterium[]
	 */
	public FilterKriterium[] createFKLieferantbeurteilung(Integer iIdLieferantI) {
		FilterKriterium[] kriterien = new FilterKriterium[1];
		FilterKriterium krit1 = new FilterKriterium("flrlieferant.i_id", true,
				"'" + iIdLieferantI.toString() + "'",
				FilterKriterium.OPERATOR_EQUAL, false);
		kriterien[0] = krit1;
		return kriterien;
	}

	public QueryType[] createQTPLZ() {
		QueryType[] types = new QueryType[1];

		FilterKriterium f1 = new FilterKriterium(
				LieferantFac.FLR_PARTNER_LANDPLZORT_PLZ, // flrres: sind alles
															// aliase
				true, "", FilterKriterium.OPERATOR_LIKE, false);

		types[0] = new QueryType(LPMain.getInstance().getTextRespectUISPr(
				"lp.plz"), f1, new String[] { FilterKriterium.OPERATOR_LIKE },
				true, true);

		return types;
	}

	public FilterKriteriumDirekt createFKDBankLandPLZOrt() {
		FilterKriteriumDirekt fkDirekt2 = new FilterKriteriumDirekt(
				BankFac.FLR_PARTNERBANK_LANDPLZORT_ORT_NAME, "",
				FilterKriterium.OPERATOR_LIKE,
				LPMain.getTextRespectUISPr("lp.ort"),
				FilterKriteriumDirekt.PROZENT_BOTH, true, false,
				Facade.MAX_UNBESCHRAENKT);
		return fkDirekt2;
	}

	public FilterKriteriumDirekt createFKDBankBLZ() {
		FilterKriteriumDirekt fkDirekt = new FilterKriteriumDirekt(
				BankFac.FLR_PARTNERBANK_BLZ, "", FilterKriterium.OPERATOR_LIKE,
				LPMain.getTextRespectUISPr("lp.blz") + "/"
						+ LPMain.getTextRespectUISPr("lp.bic"),
				FilterKriteriumDirekt.PROZENT_BOTH, true, false,
				Facade.MAX_UNBESCHRAENKT);
		return fkDirekt;
	}

	/**
	 * PanelFLRAnsprechpartner erzeugen
	 * 
	 * @param internalFrameI
	 *            InternalFrame
	 * @param partnerIId
	 *            Integer
	 * @param selectedId
	 *            Integer
	 * @param bMitNewButton
	 *            boolean
	 * @param bMitLeerenButton
	 *            boolean
	 * @return PanelQueryFLR
	 * @throws Throwable
	 */
	public PanelQueryFLR createPanelFLRAnsprechpartner(
			InternalFrame internalFrameI, Integer partnerIId,
			Integer selectedId, boolean bMitNewButton, boolean bMitLeerenButton)
			throws Throwable {
		String[] aWhichButtonIUse = null;
		if (bMitLeerenButton) {
			if (bMitNewButton) {
				aWhichButtonIUse = new String[] { PanelBasis.ACTION_REFRESH,
						PanelBasis.ACTION_LEEREN, PanelBasis.ACTION_NEW };
			} else {
				aWhichButtonIUse = new String[] { PanelBasis.ACTION_REFRESH,
						PanelBasis.ACTION_LEEREN };
			}
		} else {
			if (bMitNewButton) {
				aWhichButtonIUse = new String[] { PanelBasis.ACTION_REFRESH,
						PanelBasis.ACTION_NEW };
			} else {
				aWhichButtonIUse = new String[] { PanelBasis.ACTION_REFRESH };
			}
		}

		QueryType[] types = new QueryType[0];

		PanelQueryFLR panelQueryFLRAnsprechpartner = new PanelQueryFLRAnsprechpartnerAnlegen(
				types, PartnerFilterFactory.getInstance()
						.createFKAnsprechpartner(partnerIId), aWhichButtonIUse,
				internalFrameI, LPMain.getInstance().getTextRespectUISPr(
						"title.ansprechpartnerauswahlliste"), partnerIId);
		// Direktfilter gleich einbauen
		panelQueryFLRAnsprechpartner
				.befuellePanelFilterkriterienDirektUndVersteckte(
						createFKDAnsprechpartnerPartnerName(), null,
						createFKVAnsprechpartner());

		// vorbesetzen falls gewuenscht
		if (selectedId != null) {
			panelQueryFLRAnsprechpartner.setSelectedId(selectedId);
		}

		return panelQueryFLRAnsprechpartner;
	}

	public PanelQueryFLR createPanelFLRPartnerkommentarart(
			InternalFrame internalFrameI, Integer selectedId,
			boolean bMitLeerenButton) throws Throwable {

		String[] aWhichButtonIUse = null;
		if (bMitLeerenButton) {
			aWhichButtonIUse = new String[] { PanelBasis.ACTION_REFRESH,
					PanelBasis.ACTION_LEEREN };
		} else {
			aWhichButtonIUse = new String[] { PanelBasis.ACTION_REFRESH };
		}
		PanelQueryFLR panelQueryFLRKommentarart = new PanelQueryFLR(
				ArtikelFilterFactory.getInstance()
						.createQTArtikelkommentarart(), null,
				QueryParameters.UC_ID_PARTNERKOMMENTARART, aWhichButtonIUse,
				internalFrameI, LPMain.getTextRespectUISPr(
						"lp.kommentarart"));

		panelQueryFLRKommentarart
				.befuellePanelFilterkriterienDirekt(
						SystemFilterFactory.getInstance().createFKDBezeichnung(),null);
		panelQueryFLRKommentarart.setSelectedId(selectedId);
		return panelQueryFLRKommentarart;

	}

	
	public Map getMapKundeInteressent() {
		Map m = new TreeMap();
		m.put(1, LPMain.getTextRespectUISPr("kunde.filter.alle"));
		m.put(2, LPMain.getTextRespectUISPr("kunde.filter.kunde"));
		m.put(3, LPMain.getTextRespectUISPr("kunde.filter.interessent"));
		return m;
	}

	public FilterKriteriumDirekt createFKDBankName() {
		FilterKriteriumDirekt fkDirekt1 = new FilterKriteriumDirekt(
				BankFac.FLR_PARTNER_NAME1NACHNAMEFIRMAZEILE1, "",
				FilterKriterium.OPERATOR_LIKE, LPMain.getInstance()
						.getTextRespectUISPr("lp.name"),
				FilterKriteriumDirekt.PROZENT_BOTH, true, true,
				Facade.MAX_UNBESCHRAENKT);
		return fkDirekt1;
	}

	public FilterKriterium createFKKriteriumGeschaeftsjahr(boolean isSelectedI)
			throws Throwable {
		FilterKriterium[] krit = new FilterKriterium[1];
		krit[0] = new FilterKriterium(KundeFac.KRIT_JAHR_GESCHAEFTSJAHR,
				isSelectedI, KundeFac.KRIT_JAHR_GESCHAEFTSJAHR,
				FilterKriterium.OPERATOR_EQUAL, false);
		return krit[0];
	}

	public FilterKriterium createFKKriteriumKalenderjahr(boolean isSelectedI)
			throws Throwable {
		FilterKriterium[] krit = new FilterKriterium[1];
		krit[0] = new FilterKriterium(KundeFac.KRIT_JAHR_KALENDERJAHR,
				isSelectedI, KundeFac.KRIT_JAHR_KALENDERJAHR,
				FilterKriterium.OPERATOR_EQUAL, false);
		return krit[0];
	}

	public FilterKriterium createFKKriteriumIId(Integer iIdI) throws Throwable {

		FilterKriterium[] krit = new FilterKriterium[1];
		krit[0] = new FilterKriterium("", true, iIdI.toString(),
				FilterKriterium.OPERATOR_EQUAL, false);
		return krit[0];
	}

	/**
	 * Kundenauswahlliste mit Filter & Direktfilter.
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
	public PanelQueryFLR createPanelFLRKunde(InternalFrame internalFrameI,
			boolean bShowFilterButton, boolean bShowLeerenButton)
			throws Throwable {
		String[] aWhichButtonIUse = SystemFilterFactory.getInstance()
				.createButtonArray(bShowFilterButton, bShowLeerenButton);

		PanelQueryFLR panelQueryFLRKunde = new PanelQueryFLR(
				PartnerFilterFactory.getInstance().createQTKundeABCDebitoren(),
				PartnerFilterFactory.getInstance()
						.createFKKundeMandantPartner(),
				QueryParameters.UC_ID_KUNDE2, aWhichButtonIUse, internalFrameI,
				LPMain.getTextRespectUISPr("title.kundenauswahlliste"));

		panelQueryFLRKunde.befuellePanelFilterkriterienDirektUndVersteckte(
				PartnerFilterFactory.getInstance().createFKDKundePartnerName(
						LPMain.getTextRespectUISPr("lp.firma")),
				PartnerFilterFactory.getInstance().createFKDKundePartnerOrt(),
				PartnerFilterFactory.getInstance().createFKVKunde());

		panelQueryFLRKunde.setFilterComboBox(PartnerFilterFactory.getInstance()
				.getMapKundeInteressent(), new FilterKriterium(
				"KUNDE_INTERESSENT", true, "" + "",
				FilterKriterium.OPERATOR_EQUAL, false), true);

		ParametermandantDto parameter = DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getMandantparameter(
						LPMain.getInstance().getTheClient().getMandant(),
						ParameterFac.KATEGORIE_KUNDEN,
						ParameterFac.PARAMETER_DEFAULT_KUNDENAUSWAHL);
		Integer iAuswahl = (java.lang.Integer) parameter.getCWertAsObject();

		panelQueryFLRKunde.setKeyOfFilterComboBox(iAuswahl);

		parameter = DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getMandantparameter(LPMain.getTheClient().getMandant(),
						ParameterFac.KATEGORIE_ALLGEMEIN,
						ParameterFac.PARAMETER_SUCHEN_INKLUSIVE_KBEZ);
		boolean bSuchenInklusiveKbez = (java.lang.Boolean) parameter
				.getCWertAsObject();

		if (!bSuchenInklusiveKbez) {

			panelQueryFLRKunde
					.addDirektFilter(createFKDKundePartnerKurzbezeichnung());
		}
		return panelQueryFLRKunde;
	}

	public PanelQueryFLR createPanelFLRKundenidentnummer(
			InternalFrame internalFrameI, boolean bShowLeerenButton,
			Integer kundeIId) throws Throwable {
		String[] aWhichButtonIUse = { PanelBasis.ACTION_REFRESH,
				PanelBasis.ACTION_LEEREN };

		if (bShowLeerenButton == false) {
			aWhichButtonIUse = new String[] { PanelBasis.ACTION_REFRESH };
		}
		FilterKriterium[] kriterien = null;
		if (kundeIId != null) {
			kriterien = new FilterKriterium[2];
			kriterien[0] = new FilterKriterium(
					"kundesoko.flrkunde.mandant_c_nr", true, "'"
							+ LPMain.getInstance().getTheClient().getMandant()
							+ "'", FilterKriterium.OPERATOR_EQUAL, false);
			kriterien[1] = new FilterKriterium("kundesoko.flrkunde.i_id", true,
					kundeIId + "", FilterKriterium.OPERATOR_EQUAL, false);

		} else {
			kriterien = new FilterKriterium[1];

			kriterien[0] = new FilterKriterium(
					"kundesoko.flrkunde.mandant_c_nr", true, "'"
							+ LPMain.getInstance().getTheClient().getMandant()
							+ "'", FilterKriterium.OPERATOR_EQUAL, false);

		}

		PanelQueryFLR panelQueryFLRKundenidentnummer = new PanelQueryFLR(null,
				kriterien, QueryParameters.UC_ID_KUNDENIDENTNUMMER,
				aWhichButtonIUse, internalFrameI, LPMain.getInstance()
						.getTextRespectUISPr("auft.title.panel.auswahl"));

		panelQueryFLRKundenidentnummer.befuellePanelFilterkriterienDirekt(
				new FilterKriteriumDirekt("kundesoko.c_kundeartikelnummer", "",
						FilterKriterium.OPERATOR_LIKE, LPMain.getInstance()
								.getTextRespectUISPr(
										"kunde.soko.kundeartikelnummer"),
						FilterKriteriumDirekt.PROZENT_TRAILING, // Auswertung
																// als '%XX'
						true, // wrapWithSingleQuotes
						true, Facade.MAX_UNBESCHRAENKT),
				new FilterKriteriumDirekt("kundesoko.flrkunde."
						+ KundeFac.FLR_PARTNER_NAME1NACHNAMEFIRMAZEILE1, "",
						FilterKriterium.OPERATOR_LIKE, LPMain.getInstance()
								.getTextRespectUISPr("lp.firma"),
						FilterKriteriumDirekt.PROZENT_TRAILING, true, true,
						Facade.MAX_UNBESCHRAENKT));

		panelQueryFLRKundenidentnummer
				.addDirektFilter(new FilterKriteriumDirekt(
						ArtikelFac.FLR_ARTIKELLISTE_C_VOLLTEXT, "",
						FilterKriterium.OPERATOR_LIKE, LPMain.getInstance()
								.getTextRespectUISPr("lp.textsuche"),
						FilterKriteriumDirekt.EXTENDED_SEARCH, false, true,
						Facade.MAX_UNBESCHRAENKT));

		return panelQueryFLRKundenidentnummer;

	}

	/**
	 * Kundenauswahlliste mit Filter & Direktfilter.
	 * 
	 * @param internalFrameI
	 *            InternalFrame
	 * @param bShowFilterButton
	 *            den New Button anzeigen
	 * @param bShowLeerenButton
	 *            den Leeren Button anzeigen
	 * @param selectedIIdI
	 *            die iId des in der Liste selektierten Kunden
	 * @return PanelQueryFLR die Auswahlliste
	 * @throws Throwable
	 *             Ausnahme
	 */
	public PanelQueryFLR createPanelFLRKunde(InternalFrame internalFrameI,
			boolean bShowFilterButton, boolean bShowLeerenButton,
			Integer selectedIIdI) throws Throwable {
		PanelQueryFLR panelQueryFLRKunde = createPanelFLRKunde(internalFrameI,
				bShowFilterButton, bShowLeerenButton);

		if (selectedIIdI != null) {
			panelQueryFLRKunde.setSelectedId(selectedIIdI);
		}

		return panelQueryFLRKunde;
	}

	/**
	 * Filterkriterium fuer Filter PartnerFac.FLR_PARTNER_I_ID.
	 * 
	 * @param iIdPartnerI
	 *            PK des Partners
	 * @return FilterKriterium[]
	 * @throws ExceptionLP
	 */
	public FilterKriterium[] createFKPartnerIId(Integer iIdPartnerI)
			throws ExceptionLP {
		if (iIdPartnerI == null) {
			throw new ExceptionLP(EJBExceptionLP.FEHLER_KEIN_PARTNER_GEWAEHLT,
					new Exception("iIdPartnerI == null"));
		}

		FilterKriterium[] kriterien = new FilterKriterium[1];

		FilterKriterium krit1 = new FilterKriterium(
				PartnerFac.FLR_PARTNER_I_ID, true, iIdPartnerI.toString(),
				FilterKriterium.OPERATOR_EQUAL, false);

		kriterien[0] = krit1;

		return kriterien;
	}

	/**
	 * Die UI Filterkriterien fuer ein PanelQueryFLR Kundenauswahl bauen.
	 * 
	 * @return QueryType[] die moeglichen Filterkriterien
	 */
	public QueryType[] createQTPanelQueryFLRKundenauswahl() {
		QueryType[] types = new QueryType[3];

		FilterKriterium f1 = new FilterKriterium(KundeFac.FLR_PARTNER + "."
				+ PartnerFac.FLR_PARTNER_NAME1NACHNAMEFIRMAZEILE1, true, "",
				FilterKriterium.OPERATOR_LIKE, false);

		types[0] = new QueryType(LPMain.getInstance().getTextRespectUISPr(
				"label.kunde"), f1,
				new String[] { FilterKriterium.OPERATOR_EQUAL }, true, true);

		FilterKriterium f2 = new FilterKriterium(KundeFac.FLR_KUNDE_C_KURZNR,
				true, "", FilterKriterium.OPERATOR_LIKE, false);

		types[1] = new QueryType(LPMain.getInstance().getTextRespectUISPr(
				"lp.kurzbez"), f1,
				new String[] { FilterKriterium.OPERATOR_EQUAL }, true, true);

		FilterKriterium f3 = new FilterKriterium(KundeFac.FLR_PARTNER + "."
				+ PartnerFac.FLR_PARTNER_FLRLANDPLZORT + "."
				+ SystemFac.FLR_LP_FLRORT + "."
				+ SystemFac.FLR_LP_LANDPLZORTPLZ, true, "",
				FilterKriterium.OPERATOR_LIKE, false);

		types[2] = new QueryType(LPMain.getInstance().getTextRespectUISPr(
				"lp.plz"), f1, new String[] { FilterKriterium.OPERATOR_EQUAL },
				true, true);

		// @todo Debitorenkonto PJ 5140

		return types;
	}

	/**
	 * Liefergruppenauswahlliste mit Filter & Direktfilter.
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
	public PanelQueryFLR createPanelFLRLiefergruppe(
			InternalFrame internalFrameI, boolean bShowFilterButton,
			boolean bShowLeerenButton) throws Throwable {
		String[] aWhichButtonIUse = SystemFilterFactory.getInstance()
				.createButtonArray(bShowFilterButton, bShowLeerenButton);
		FilterKriterium[] f = SystemFilterFactory.getInstance()
				.createFKMandantCNr();
		PanelQueryFLR panelQueryFLRLiefergruppe = new PanelQueryFLR(null, f,
				QueryParameters.UC_ID_LIEFERGRUPPEN, aWhichButtonIUse,
				internalFrameI,
				LPMain.getTextRespectUISPr("title.liefergruppenauswahlliste"));

		return panelQueryFLRLiefergruppe;
	}

	public PanelQueryFLR createPanelFLRBranche(InternalFrame internalFrameI,
			boolean bShowFilterButton, boolean bShowLeerenButton,
			Integer selectedId) throws Throwable {

		String[] aWhichButtonIUse = SystemFilterFactory.getInstance()
				.createButtonArray(bShowFilterButton, bShowLeerenButton);

		final QueryType[] querytypes = null;
		final FilterKriterium[] filters = null;
		PanelQueryFLR panelQueryFLRBranche = new PanelQueryFLR(querytypes,
				filters, QueryParameters.UC_ID_BRANCHE, aWhichButtonIUse,
				internalFrameI, LPMain.getTextRespectUISPr("lp.branche"));

		panelQueryFLRBranche.setSelectedId(selectedId);

		return panelQueryFLRBranche;
	}

	public PanelQueryFLR createPanelFLRSelektion(InternalFrame internalFrameI,
			boolean bShowLeerenButton, Integer selectedId) throws Throwable {

		String[] aWhichButtonIUse = SystemFilterFactory.getInstance()
				.createButtonArray(false, bShowLeerenButton);

		FilterKriterium[] f = null;

		ParametermandantDto parameter = DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getMandantparameter(LPMain.getTheClient().getMandant(),
						ParameterFac.KATEGORIE_PARTNER,
						ParameterFac.PARAMETER_SELEKTIONEN_MANDANTENABHAENGIG);
		boolean bSelektionenMandantenabhaengig = (java.lang.Boolean) parameter
				.getCWertAsObject();

		if (bSelektionenMandantenabhaengig == true) {
			f = SystemFilterFactory.getInstance().createFKMandantCNr();
		}

		PanelQueryFLR panelQueryFLRSelektionAuswahl = new PanelQueryFLR(null,
				f, QueryParameters.UC_ID_SELEKTION, aWhichButtonIUse,
				internalFrameI, LPMain.getTextRespectUISPr("lp.selektion"));

		panelQueryFLRSelektionAuswahl.setSelectedId(selectedId);

		return panelQueryFLRSelektionAuswahl;
	}

	/**
	 * Liefergruppenauswahlliste mit Filter & Direktfilter.
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
	public PanelQueryFLR createPanelFLRLiefergruppe(
			InternalFrame internalFrameI, boolean bShowFilterButton,
			boolean bShowLeerenButton, Integer selectedIIdI) throws Throwable {
		PanelQueryFLR panelQueryFLRLiefergruppe = createPanelFLRLiefergruppe(
				internalFrameI, bShowFilterButton, bShowLeerenButton);

		if (selectedIIdI != null) {
			panelQueryFLRLiefergruppe.setSelectedId(selectedIIdI);
		}

		return panelQueryFLRLiefergruppe;
	}

	/**
	 * Liefergruppenauswahlliste, enthaelt alle Liefergruppen zum aktuellen
	 * Mandanten, die mindestens einen Lieferanten enthalten.
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
	public PanelQueryFLR createPanelFLRLiefergruppeMindestensEinLieferant(
			InternalFrame internalFrameI, boolean bShowFilterButton,
			boolean bShowLeerenButton) throws Throwable {
		String[] aWhichButtonIUse = SystemFilterFactory.getInstance()
				.createButtonArray(bShowFilterButton, bShowLeerenButton);
		FilterKriterium[] f = SystemFilterFactory.getInstance()
				.createFKMandantCNr();
		PanelQueryFLR panelQueryFLRLiefergruppe = new PanelQueryFLR(null, f,
				QueryParameters.UC_ID_LFLIEFERGRUPPENONELF, aWhichButtonIUse,
				internalFrameI, LPMain.getInstance().getTextRespectUISPr(
						"title.liefergruppenauswahlliste"));

		return panelQueryFLRLiefergruppe;
	}

	/**
	 * Liefergruppenauswahlliste, enthaelt alle Liefergruppen zum aktuellen
	 * Mandanten, die mindestens einen Lieferanten enthalten.
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
	public PanelQueryFLR createPanelFLRLiefergruppeMindestensEinLieferant(
			InternalFrame internalFrameI, boolean bShowFilterButton,
			boolean bShowLeerenButton, Integer selectedIIdI) throws Throwable {
		PanelQueryFLR panelQueryFLRLiefergruppe = createPanelFLRLiefergruppeMindestensEinLieferant(
				internalFrameI, bShowFilterButton, bShowLeerenButton);

		if (selectedIIdI != null) {
			panelQueryFLRLiefergruppe.setSelectedId(selectedIIdI);
		}

		return panelQueryFLRLiefergruppe;
	}

	public FilterKriterium[] createFKKundesoko(Integer iIdKundeI)
			throws Throwable {
		FilterKriterium[] kriterien = new FilterKriterium[1];

		FilterKriterium krit1 = new FilterKriterium(
				KundesokoFac.FLR_KUNDESOKO_KUNDE_I_ID, true, "'" + iIdKundeI
						+ "'", FilterKriterium.OPERATOR_EQUAL, false);
		kriterien[0] = krit1;

		return kriterien;
	}

	public FilterKriterium[] createFKKundesokomengenstaffel(
			Integer iIdKundesokoI) throws Throwable {
		FilterKriterium[] kriterien = new FilterKriterium[1];

		FilterKriterium krit1 = new FilterKriterium("kundesoko_i_id", true, "'"
				+ iIdKundesokoI + "'", FilterKriterium.OPERATOR_EQUAL, false);
		kriterien[0] = krit1;

		return kriterien;
	}

	public PanelQueryFLRGoto createPanelFLRLieferantGoto(
			InternalFrame internalFrameI, Integer lieferantIIdI,
			boolean bShowFilterButton, boolean bShowLeerenButton)
			throws Throwable {
		return createPanelFLRLieferantGoto(internalFrameI, lieferantIIdI,
				bShowFilterButton, bShowLeerenButton,
				LPMain.getTextRespectUISPr("title.lieferantenauswahlliste"));
	}

	public PanelQueryFLRGoto createPanelFLRLieferantGoto(
			InternalFrame internalFrameI, Integer lieferantIIdI,
			boolean bShowFilterButton, boolean bShowLeerenButton,
			String sOwnTitleI) throws Throwable {
		bShowFilterButton = false; // zzt keine QueryTypes
		// Buttons zusammenstellen.
		String[] aWhichButtonIUse = SystemFilterFactory.getInstance()
				.createButtonArray(bShowFilterButton, bShowLeerenButton);

		FilterKriterium[] fk = PartnerFilterFactory.getInstance()
				.createFKLieferantMandantPartner();
		PanelQueryFLRGoto panelQueryFLRLieferant = new PanelQueryFLRGoto(null,
				fk, QueryParameters.UC_ID_LIEFERANTEN, aWhichButtonIUse,
				internalFrameI, LocaleFac.BELEGART_LIEFERANT, sOwnTitleI,
				lieferantIIdI);
		panelQueryFLRLieferant.befuellePanelFilterkriterienDirektUndVersteckte(
				PartnerFilterFactory.getInstance()
						.createFKDLieferantPartnerName(), PartnerFilterFactory
						.getInstance().createFKDLieferantPartnerOrt(),
				PartnerFilterFactory.getInstance().createFKVLieferant());

		panelQueryFLRLieferant.addDirektFilter(PartnerFilterFactory
				.getInstance().createFKDPartnerErweiterteSuche());

		ParametermandantDto parameter = DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getMandantparameter(
						LPMain.getInstance().getTheClient().getMandant(),
						ParameterFac.KATEGORIE_ALLGEMEIN,
						ParameterFac.PARAMETER_SUCHEN_INKLUSIVE_KBEZ);
		boolean bSuchenInklusiveKbez = (java.lang.Boolean) parameter
				.getCWertAsObject();

		if (!bSuchenInklusiveKbez) {
			panelQueryFLRLieferant.addDirektFilter(PartnerFilterFactory
					.getInstance().createFKDLieferantPartnerKurzbezeichnung());
		}

		return panelQueryFLRLieferant;
	}
}
