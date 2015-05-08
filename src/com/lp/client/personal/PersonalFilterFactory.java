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
package com.lp.client.personal;

import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.system.SystemFilterFactory;
import com.lp.client.util.fastlanereader.gui.QueryType;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.partner.service.PartnerFac;
import com.lp.server.personal.service.PersonalDto;
import com.lp.server.personal.service.PersonalFac;
import com.lp.server.personal.service.ZeiterfassungFac;
import com.lp.server.util.Facade;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.FilterKriteriumDirekt;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;

@SuppressWarnings("static-access")
public class PersonalFilterFactory {
	private static PersonalFilterFactory filterFactory = null;

	private PersonalFilterFactory() {
		// Singleton.
	}

	static public PersonalFilterFactory getInstance() {
		if (filterFactory == null) {
			filterFactory = new PersonalFilterFactory();
		}

		return filterFactory;
	}

	public QueryType[] createQTPersonal() {

		QueryType[] types = new QueryType[3];

		FilterKriterium f1 = new FilterKriterium(
				PersonalFac.FLR_PERSONAL_FLRKOSTENSTELLEABTEILUNG + ".c_nr",
				true, "", FilterKriterium.OPERATOR_LIKE, true);

		types[0] = new QueryType(LPMain.getInstance().getTextRespectUISPr(
				"lp.abteilung"), f1,
				new String[] { FilterKriterium.OPERATOR_EQUAL }, true, true);

		FilterKriterium f2 = new FilterKriterium(
				PersonalFac.FLR_PERSONAL_FLRKOSTENSTELLESTAMM + ".c_nr", true,
				"", FilterKriterium.OPERATOR_LIKE, true);

		types[1] = new QueryType(LPMain.getInstance().getTextRespectUISPr(
				"pers.personal.heimatkostenstelle"), f2,
				new String[] { FilterKriterium.OPERATOR_EQUAL }, true, true);

		FilterKriterium f3 = new FilterKriterium(
				PersonalFac.FLR_PERSONAL_FLRPERSONALGRUPPE + ".c_bez", true,
				"", FilterKriterium.OPERATOR_LIKE, true);

		types[2] = new QueryType(LPMain.getInstance().getTextRespectUISPr(
				"pers.personalgruppe"), f3,
				new String[] { FilterKriterium.OPERATOR_LIKE }, true, true);

		return types;
	}

	public FilterKriterium[] createFKPersonalgruppekosten(Integer maschineIId)
			throws Throwable {
		FilterKriterium[] kriterien = new FilterKriterium[1];
		kriterien[0] = new FilterKriterium("flrpersonalgruppe.i_id", true,
				maschineIId + "", FilterKriterium.OPERATOR_EQUAL, false);

		return kriterien;
	}

	public PanelQueryFLR createPanelFLRReligion(InternalFrame internalFrameI,
			Integer selectedId) throws Throwable {
		String[] aWhichButtonIUse = { PanelBasis.ACTION_REFRESH,
				PanelBasis.ACTION_FILTER, PanelBasis.ACTION_LEEREN };

		PanelQueryFLR panelQueryFLRReligion = new PanelQueryFLR(
				PersonalFilterFactory.getInstance().createQTReligion(), null,
				QueryParameters.UC_ID_RELIGION, aWhichButtonIUse,
				internalFrameI, LPMain.getInstance().getTextRespectUISPr(
						"title.religionauswahlliste"));
		panelQueryFLRReligion.befuellePanelFilterkriterienDirekt(
				SystemFilterFactory.getInstance().createFKDKennung(),
				SystemFilterFactory.getInstance()
						.createFKDSprTabelleBezeichnung(
								PersonalFac.FLR_RELIGION_RELIGIONSPRSET));
		if (selectedId != null) {
			panelQueryFLRReligion.setSelectedId(selectedId);
		}
		return panelQueryFLRReligion;

	}

	public PanelQueryFLR createPanelFLRPersonalgruppe(
			InternalFrame internalFrameI, Integer selectedId,
			boolean bMitLeerenButton) throws Throwable {
		String[] aWhichButtonIUse = null;
		if (bMitLeerenButton == true) {
			aWhichButtonIUse = new String[] { PanelBasis.ACTION_REFRESH,
					PanelBasis.ACTION_FILTER, PanelBasis.ACTION_LEEREN };
		} else {
			aWhichButtonIUse = new String[] { PanelBasis.ACTION_REFRESH,
					PanelBasis.ACTION_FILTER };
		}

		PanelQueryFLR panelQueryFLRPg = new PanelQueryFLR(null, null,
				QueryParameters.UC_ID_PERSONALGRUPPE, aWhichButtonIUse,
				internalFrameI, LPMain.getInstance().getTextRespectUISPr(
						"pers.personalgruppe"));
		panelQueryFLRPg.befuellePanelFilterkriterienDirekt(SystemFilterFactory
				.getInstance().createFKDBezeichnung(), null);
		if (selectedId != null) {
			panelQueryFLRPg.setSelectedId(selectedId);
		}
		return panelQueryFLRPg;

	}

	public FilterKriterium[] createFKFahrzeug() throws Throwable {

		FilterKriterium[] kriterien = new FilterKriterium[1];
		kriterien[0] = new FilterKriterium("mandant_c_nr", true, "'"
				+ LPMain.getInstance().getTheClient().getMandant() + "'",
				FilterKriterium.OPERATOR_EQUAL, false);
		return kriterien;

	}

	public PanelQueryFLR createPanelFLRFahrzeug(InternalFrame internalFrameI,
			Integer selectedId, boolean bMitLeerenButton) throws Throwable {
		String[] aWhichButtonIUse = null;
		if (bMitLeerenButton == true) {
			aWhichButtonIUse = new String[] { PanelBasis.ACTION_REFRESH,
					PanelBasis.ACTION_FILTER, PanelBasis.ACTION_LEEREN };
		} else {
			aWhichButtonIUse = new String[] { PanelBasis.ACTION_REFRESH,
					PanelBasis.ACTION_FILTER };
		}

		PanelQueryFLR panelQueryFLRPg = new PanelQueryFLR(null,
				createFKFahrzeug(), QueryParameters.UC_ID_FAHRZEUG,
				aWhichButtonIUse, internalFrameI, LPMain.getInstance()
						.getTextRespectUISPr("pers.fahrzeug"));
		panelQueryFLRPg.befuellePanelFilterkriterienDirekt(SystemFilterFactory
				.getInstance().createFKDBezeichnung(), null);
		if (selectedId != null) {
			panelQueryFLRPg.setSelectedId(selectedId);
		}
		return panelQueryFLRPg;

	}

	public PanelQueryFLR createPanelFLRTagesart(InternalFrame internalFrameI,
			Integer selectedId, boolean bMitLeerenButton) throws Throwable {
		String[] aWhichButtonIUse = null;
		if (bMitLeerenButton == true) {
			aWhichButtonIUse = new String[] { PanelBasis.ACTION_REFRESH,
					PanelBasis.ACTION_FILTER, PanelBasis.ACTION_LEEREN };
		} else {
			aWhichButtonIUse = new String[] { PanelBasis.ACTION_REFRESH,
					PanelBasis.ACTION_FILTER };
		}

		PanelQueryFLR panelQueryFLRPg = new PanelQueryFLR(null, null,
				QueryParameters.UC_ID_TAGESART, aWhichButtonIUse,
				internalFrameI, LPMain.getInstance().getTextRespectUISPr(
						"lp.tagesart"));
		panelQueryFLRPg.befuellePanelFilterkriterienDirekt(SystemFilterFactory
				.getInstance().createFKDBezeichnung(), null);
		if (selectedId != null) {
			panelQueryFLRPg.setSelectedId(selectedId);
		}
		return panelQueryFLRPg;

	}

	public PanelQueryFLR createPanelFLRLohnart(InternalFrame internalFrameI,
			Integer selectedId, boolean bShowLeerenButton) throws Throwable {

		String[] aWhichButtonIUse = SystemFilterFactory.getInstance()
				.createButtonArray(false, bShowLeerenButton);

		PanelQueryFLR panelQueryFLRPg = new PanelQueryFLR(null, null,
				QueryParameters.UC_ID_LOHNART, aWhichButtonIUse,
				internalFrameI, LPMain.getInstance().getTextRespectUISPr(
						"pers.lohnart"));
		panelQueryFLRPg.befuellePanelFilterkriterienDirekt(SystemFilterFactory
				.getInstance().createFKDBezeichnung(), null);
		if (selectedId != null) {
			panelQueryFLRPg.setSelectedId(selectedId);
		}
		return panelQueryFLRPg;

	}

	public PanelQueryFLR createPanelFLRZeitmodell(InternalFrame internalFrameI,
			Integer selectedId, boolean bMitLeerenButton) throws Throwable {
		FilterKriterium[] filtersMandant = new FilterKriterium[1];
		String mandant = "'" + LPMain.getInstance().getTheClient().getMandant()
				+ "'";
		FilterKriterium krit1 = new FilterKriterium("zeitmodell.mandant_c_nr",
				true, mandant, FilterKriterium.OPERATOR_EQUAL, false);
		filtersMandant[0] = krit1;
		String[] aWhichButtonIUse = null;

		if (bMitLeerenButton) {
			aWhichButtonIUse = new String[] { PanelBasis.ACTION_REFRESH,
					PanelBasis.ACTION_LEEREN };
		} else {
			aWhichButtonIUse = new String[] { PanelBasis.ACTION_REFRESH };
		}
		PanelQueryFLR panelQueryFLRZeitmodell = new PanelQueryFLR(null,
				filtersMandant, QueryParameters.UC_ID_ZEITMODELL,
				aWhichButtonIUse, internalFrameI, LPMain.getInstance()
						.getTextRespectUISPr("title.zeitmodellauswahlliste"));
		panelQueryFLRZeitmodell
				.befuellePanelFilterkriterienDirekt(
						new FilterKriteriumDirekt("zeitmodell.c_nr", "",
								FilterKriterium.OPERATOR_LIKE, LPMain
										.getInstance().getTextRespectUISPr(
												"label.kennung"),
								FilterKriteriumDirekt.PROZENT_TRAILING, // Auswertung
																		// als
																		// 'XX%'
								true, true, Facade.MAX_UNBESCHRAENKT),
						SystemFilterFactory
								.getInstance()
								.createFKDSprTabelleBezeichnung(
										ZeiterfassungFac.FLR_ZEITMODELL_ZEITMODELLSPRSET));

		panelQueryFLRZeitmodell.setSelectedId(selectedId);

		return panelQueryFLRZeitmodell;

	}

	public PanelQueryFLR createPanelFLRMaschinezm(
			InternalFrame internalFrameI, Integer selectedId,
			boolean bMitLeerenButton) throws Throwable {
		FilterKriterium[] filtersMandant = new FilterKriterium[1];
		String mandant = "'" + LPMain.getInstance().getTheClient().getMandant()
				+ "'";
		FilterKriterium krit1 = new FilterKriterium("mandant_c_nr",
				true, mandant, FilterKriterium.OPERATOR_EQUAL, false);
		filtersMandant[0] = krit1;
		String[] aWhichButtonIUse = null;

		if (bMitLeerenButton) {
			aWhichButtonIUse = new String[] { PanelBasis.ACTION_REFRESH,
					PanelBasis.ACTION_LEEREN };
		} else {
			aWhichButtonIUse = new String[] { PanelBasis.ACTION_REFRESH };
		}
		PanelQueryFLR panelQueryFLRZeitmodell = new PanelQueryFLR(null,
				filtersMandant, QueryParameters.UC_ID_MASCHINEZM,
				aWhichButtonIUse, internalFrameI, LPMain.getInstance()
						.getTextRespectUISPr("title.zeitmodellauswahlliste"));
		panelQueryFLRZeitmodell.befuellePanelFilterkriterienDirekt(
				new FilterKriteriumDirekt("c_bez", "",
						FilterKriterium.OPERATOR_LIKE, LPMain.getInstance()
								.getTextRespectUISPr("lp.bezeichnung"),
						FilterKriteriumDirekt.PROZENT_TRAILING, // Auswertung
																// als 'XX%'
						true, true, Facade.MAX_UNBESCHRAENKT), null);

		panelQueryFLRZeitmodell.setSelectedId(selectedId);

		return panelQueryFLRZeitmodell;

	}

	public FilterKriteriumDirekt createFKDPersonalnummer() throws Throwable {

		return new FilterKriteriumDirekt(
				PersonalFac.FLR_PERSONAL_C_PERSONALNUMMER, "",
				FilterKriterium.OPERATOR_LIKE, LPMain.getInstance()
						.getTextRespectUISPr("pers.personal.personalnummer"),
				FilterKriteriumDirekt.PROZENT_TRAILING, true, false,
				Facade.MAX_UNBESCHRAENKT);

	}

	public FilterKriteriumDirekt createFKDPersonalname() throws Throwable {

		return new FilterKriteriumDirekt(PersonalFac.FLR_PERSONAL_FLRPARTNER
				+ "." + PartnerFac.FLR_PARTNER_NAME1NACHNAMEFIRMAZEILE1, "",
				FilterKriterium.OPERATOR_LIKE, LPMain.getInstance()
						.getTextRespectUISPr("lp.nachname"),
				FilterKriteriumDirekt.PROZENT_TRAILING, true, true,
				Facade.MAX_UNBESCHRAENKT);

	}

	public FilterKriteriumDirekt createFKDAusweis() throws Throwable {

		return new FilterKriteriumDirekt(PersonalFac.FLR_PERSONAL_C_AUSWEIS,
				"", FilterKriterium.OPERATOR_LIKE, LPMain.getInstance()
						.getTextRespectUISPr("pers.personal.ausweis"),
				FilterKriteriumDirekt.PROZENT_TRAILING, true, true,
				Facade.MAX_UNBESCHRAENKT);

	}

	public FilterKriteriumDirekt createFKDLohnart() throws Throwable {

		return new FilterKriteriumDirekt("flrlohnart.i_lohnart", "",
				FilterKriterium.OPERATOR_LIKE, LPMain.getInstance()
						.getTextRespectUISPr("pers.lohnart"),
				FilterKriteriumDirekt.PROZENT_TRAILING, true, true,
				Facade.MAX_UNBESCHRAENKT);

	}

	public FilterKriterium[] createFKDPersonalauswahl(InternalFrame frame)
			throws Throwable {
		// Wenn sichtbarkeit = ALLE, dann nur nach Mandant filtern
		if (DelegateFactory.getInstance().getTheJudgeDelegate()
				.hatRecht(RechteFac.RECHT_PERS_SICHTBARKEIT_ALLE)) {
			FilterKriterium[] kriterien = new FilterKriterium[1];
			FilterKriterium krit1 = new FilterKriterium("mandant_c_nr", true,
					"'" + LPMain.getInstance().getTheClient().getMandant()
							+ "'", FilterKriterium.OPERATOR_EQUAL, false);
			kriterien[0] = krit1;
			return kriterien;
		}
		// Wenn Sichtbarkeit = ABTEILUNG, dann nach Mandant filtern und
		// Abteilung filtern
		// Wenn keine Abteilung hinterlegt, dann sieht er nur sich selbst
		else if (DelegateFactory.getInstance().getTheJudgeDelegate()
				.hatRecht(RechteFac.RECHT_PERS_SICHTBARKEIT_ABTEILUNG)) {
			FilterKriterium[] kriterien = new FilterKriterium[2];
			FilterKriterium krit1 = new FilterKriterium("mandant_c_nr", true,
					"'" + LPMain.getInstance().getTheClient().getMandant()
							+ "'", FilterKriterium.OPERATOR_EQUAL, false);
			kriterien[0] = krit1;

			PersonalDto personalDto = DelegateFactory
					.getInstance()
					.getPersonalDelegate()
					.personalFindByPrimaryKey(
							LPMain.getInstance().getTheClient().getIDPersonal());

			if (personalDto.getKostenstelleIIdAbteilung() != null) {
				FilterKriterium krit2 = new FilterKriterium(
						"flrkostenstelleabteilung.i_id", true,
						personalDto.getKostenstelleIIdAbteilung() + "",
						FilterKriterium.OPERATOR_EQUAL, false);
				kriterien[1] = krit2;
				return kriterien;
			} else {
				FilterKriterium krit2 = new FilterKriterium("i_id", true,
						LPMain.getInstance().getTheClient().getIDPersonal()
								+ "", FilterKriterium.OPERATOR_EQUAL, false);
				kriterien[1] = krit2;
				return kriterien;
			}

		}
		// Wenn keine Sichtbarkeit, dann nach sieht er nur sich selbst
		else {
			FilterKriterium[] kriterien = new FilterKriterium[2];
			FilterKriterium krit1 = new FilterKriterium("mandant_c_nr", true,
					"'" + LPMain.getInstance().getTheClient().getMandant()
							+ "'", FilterKriterium.OPERATOR_EQUAL, false);
			kriterien[0] = krit1;

			FilterKriterium krit2 = new FilterKriterium("i_id", true, LPMain
					.getInstance().getTheClient().getIDPersonal()
					+ "", FilterKriterium.OPERATOR_EQUAL, false);
			kriterien[1] = krit2;

			return kriterien;

		}
	}

	public FilterKriteriumDirekt createFKDBetriebskalenderBezeichnung()
			throws Throwable {

		return new FilterKriteriumDirekt(
				PersonalFac.FLR_BETRIEBSKALENDER_C_BEZ, "",
				FilterKriterium.OPERATOR_LIKE, LPMain.getInstance()
						.getTextRespectUISPr("lp.bezeichnung"),
				FilterKriteriumDirekt.PROZENT_TRAILING, true, true,
				Facade.MAX_UNBESCHRAENKT);

	}

	public FilterKriterium[] createFKPersonal(Integer personalIId) {

		FilterKriterium[] kriterien = new FilterKriterium[1];
		FilterKriterium krit1 = new FilterKriterium("personal_i_id", true,
				personalIId + "", FilterKriterium.OPERATOR_EQUAL, false);
		kriterien[0] = krit1;
		return kriterien;

	}

	public FilterKriterium[] createFKBereitschaftPersonal(Integer personalIId) {

		FilterKriterium[] kriterien = new FilterKriterium[1];
		FilterKriterium krit1 = new FilterKriterium("flrpersonal.i_id", true,
				personalIId + "", FilterKriterium.OPERATOR_EQUAL, false);
		kriterien[0] = krit1;
		return kriterien;

	}

	public FilterKriterium[] createFKArtikelzulage() throws Throwable {

		FilterKriterium[] kriterien = new FilterKriterium[1];
		FilterKriterium krit1 = new FilterKriterium(
				PersonalFac.FLR_ARTKELZULAGE_FLRARTIKEL + ".mandant_c_nr",
				true, "'" + LPMain.getInstance().getTheClient().getMandant()
						+ "'", FilterKriterium.OPERATOR_EQUAL, false);
		kriterien[0] = krit1;
		return kriterien;

	}

	public FilterKriterium[] createFKZeitmodelltag(Integer zeitmodell_i_id) {
		FilterKriterium[] kriterien = new FilterKriterium[1];
		FilterKriterium krit1 = new FilterKriterium(
				ZeiterfassungFac.FLR_ZEITMODELLTAG_FLRZEITMODELL + ".i_id",
				true, zeitmodell_i_id + "", FilterKriterium.OPERATOR_EQUAL,
				false);
		kriterien[0] = krit1;
		return kriterien;
	}
	public FilterKriterium[] createFKMaschinezmtagesart(Integer zeitmodell_i_id) {
		FilterKriterium[] kriterien = new FilterKriterium[1];
		FilterKriterium krit1 = new FilterKriterium(
				"flrmaschinenzm.i_id",
				true, zeitmodell_i_id + "", FilterKriterium.OPERATOR_EQUAL,
				false);
		kriterien[0] = krit1;
		return kriterien;
	}

	public FilterKriterium[] createFKBereitschafttag(
			Integer bereitschaftart_i_id) {
		FilterKriterium[] kriterien = new FilterKriterium[1];
		FilterKriterium krit1 = new FilterKriterium("flrbereitschaftart.i_id",
				true, bereitschaftart_i_id + "",
				FilterKriterium.OPERATOR_EQUAL, false);
		kriterien[0] = krit1;
		return kriterien;
	}

	public FilterKriterium[] createFKKollektivuestd(Integer kollektiv_i_id) {
		FilterKriterium[] kriterien = new FilterKriterium[1];
		FilterKriterium krit1 = new FilterKriterium(
				PersonalFac.FLR_KOLLEKTIVUESTD_FLRKOLLEKTIV + ".i_id", true,
				kollektiv_i_id + "", FilterKriterium.OPERATOR_EQUAL, false);
		kriterien[0] = krit1;
		return kriterien;
	}

	public FilterKriterium[] createFKFahrzeugkosten(Integer fahrzeug_i_id) {
		FilterKriterium[] kriterien = new FilterKriterium[1];
		FilterKriterium krit1 = new FilterKriterium("flrfahrzeug.i_id", true,
				fahrzeug_i_id + "", FilterKriterium.OPERATOR_EQUAL, false);
		kriterien[0] = krit1;
		return kriterien;
	}

	public FilterKriterium[] createFKPersonalKey(Integer personalIId)
			throws Throwable {

		FilterKriterium[] kriterien = new FilterKriterium[1];
		kriterien[0] = new FilterKriterium("i_id", true, personalIId + "",
				FilterKriterium.OPERATOR_EQUAL, false);

		return kriterien;
	}

	public FilterKriterium[] createFKZeitmodelltagpause(
			Integer zeitmodelltag_i_id) {
		FilterKriterium[] kriterien = new FilterKriterium[1];
		FilterKriterium krit1 = new FilterKriterium(
				ZeiterfassungFac.FLR_ZEITMODELLTAGPAUSE_FLRZEITMODELLTAG
						+ ".i_id", true, zeitmodelltag_i_id + "",
				FilterKriterium.OPERATOR_EQUAL, false);
		kriterien[0] = krit1;
		return kriterien;
	}

	public FilterKriterium[] createFKZusaetzlicheTagesarten() {

		FilterKriterium[] kriterien = new FilterKriterium[1];
		kriterien[0] = new FilterKriterium("c_nr", true, "('"
				+ ZeiterfassungFac.TAGESART_MONTAG + "','"
				+ ZeiterfassungFac.TAGESART_DIENSTAG + "','"
				+ ZeiterfassungFac.TAGESART_MITTWOCH + "','"
				+ ZeiterfassungFac.TAGESART_DONNERSTAG + "','"
				+ ZeiterfassungFac.TAGESART_FREITAG + "','"
				+ ZeiterfassungFac.TAGESART_SAMSTAG + "','"
				+ ZeiterfassungFac.TAGESART_SONNTAG + "','"
				+ ZeiterfassungFac.TAGESART_HALBTAG + "','"
				+ ZeiterfassungFac.TAGESART_FEIERTAG + "')",
				FilterKriterium.OPERATOR_NOT_IN, false);
		return kriterien;
	}

	public QueryType[] createQTBetriebskalender() {
		QueryType[] types = new QueryType[3];

		FilterKriterium f1 = new FilterKriterium(
				PersonalFac.FLR_BETRIEBSKALENDER_C_BEZ, true, "",
				FilterKriterium.OPERATOR_LIKE, true);

		types[0] = new QueryType(LPMain.getInstance().getTextRespectUISPr(
				"lp.bezeichnung"), f1,
				new String[] { FilterKriterium.OPERATOR_EQUAL }, true, true);
		FilterKriterium f2 = new FilterKriterium(
				PersonalFac.FLR_BETRIEBSKALENDER_FLRTAGESART + ".c_nr", true,
				"", FilterKriterium.OPERATOR_LIKE, true);

		types[1] = new QueryType(LPMain.getInstance().getTextRespectUISPr(
				"lp.tagesart"), f2,
				new String[] { FilterKriterium.OPERATOR_EQUAL }, true, true);

		FilterKriterium f3 = new FilterKriterium(
				PersonalFac.FLR_BETRIEBSKALENDER_FLRRELIGION + ".c_nr", true,
				"", FilterKriterium.OPERATOR_LIKE, true);

		types[2] = new QueryType(LPMain.getInstance().getTextRespectUISPr(
				"lp.religion"), f3,
				new String[] { FilterKriterium.OPERATOR_EQUAL }, true, true);

		return types;
	}

	public QueryType[] createQTReligion() {
		QueryType[] types = new QueryType[1];

		FilterKriterium f1 = new FilterKriterium("c_nr", true, "",
				FilterKriterium.OPERATOR_LIKE, true);

		types[0] = new QueryType(LPMain.getInstance().getTextRespectUISPr(
				"lp.religion"), f1,
				new String[] { FilterKriterium.OPERATOR_EQUAL }, true, true);

		return types;
	}

	public QueryType[] createQTZeitmodell() {
		QueryType[] types = new QueryType[2];

		FilterKriterium f1 = new FilterKriterium("c_nr", true, "",
				FilterKriterium.OPERATOR_LIKE, true);

		types[0] = new QueryType(LPMain.getInstance().getTextRespectUISPr(
				"lp.zeitmodell"), f1,
				new String[] { FilterKriterium.OPERATOR_EQUAL }, true, true);
		FilterKriterium f2 = new FilterKriterium(
				ZeiterfassungFac.FLR_ZEITMODELL_ZEITMODELLSPRSET + ".c_bez",
				true, "", FilterKriterium.OPERATOR_LIKE, true);

		types[1] = new QueryType(LPMain.getInstance().getTextRespectUISPr(
				"lp.bezeichnung"), f2,
				new String[] { FilterKriterium.OPERATOR_EQUAL }, true, true);
		return types;
	}

	/**
	 * Personalauswahlliste mit Filter & Direktfilter.
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
	public PanelQueryFLR createPanelFLRPersonal(InternalFrame internalFrameI,
			boolean bShowFilterButton, boolean bShowLeerenButton)
			throws Throwable {
		return createPanelFLRPersonal(internalFrameI, bShowFilterButton,
				bShowLeerenButton, null);
	}

	public FilterKriterium createFKVPersonal() {
		FilterKriterium fkVersteckt = new FilterKriterium(
				PersonalFac.FLR_PERSONAL_B_VERSTECKT, true, "(1)", // wenn das
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

	public FilterKriteriumDirekt createFKDZeitmodellKennung() {
		return new FilterKriteriumDirekt("zeitmodell.c_nr", "",
				FilterKriterium.OPERATOR_LIKE, LPMain.getInstance()
						.getTextRespectUISPr("label.kennung"),
				FilterKriteriumDirekt.PROZENT_TRAILING, // Auswertung als 'XX%'
				true, true, Facade.MAX_UNBESCHRAENKT); // wrapWithSingleQuotes
	}
	public FilterKriteriumDirekt createFKDMaschinenzeitmodellBezeichnung() {
		return new FilterKriteriumDirekt("maschinenzm.c_bez", "",
				FilterKriterium.OPERATOR_LIKE, LPMain.getInstance()
						.getTextRespectUISPr("lp.bezeichnung"),
				FilterKriteriumDirekt.PROZENT_TRAILING, // Auswertung als 'XX%'
				true, true, Facade.MAX_UNBESCHRAENKT); // wrapWithSingleQuotes
	}

	public FilterKriteriumDirekt createFKDZeitmodellSpr() {
		return new FilterKriteriumDirekt("zeitmodellsprset.c_bez", "",
				FilterKriterium.OPERATOR_LIKE, LPMain.getInstance()
						.getTextRespectUISPr("lp.bezeichnung"),
				FilterKriteriumDirekt.PROZENT_TRAILING, // Auswertung als 'XX%'
				true, true, Facade.MAX_UNBESCHRAENKT); // wrapWithSingleQuotes
	}

	public FilterKriteriumDirekt createFKDBereitschaftartBezeichnung() {
		return new FilterKriteriumDirekt("c_bez", "",
				FilterKriterium.OPERATOR_LIKE, LPMain.getInstance()
						.getTextRespectUISPr("lp.bezeichnung"),
				FilterKriteriumDirekt.PROZENT_TRAILING, // Auswertung als 'XX%'
				true, true, Facade.MAX_UNBESCHRAENKT); // wrapWithSingleQuotes
	}

	public FilterKriterium createFKVZeitmodell() {
		FilterKriterium fkVersteckt = new FilterKriterium("zeitmodell."
				+ ZeiterfassungFac.FLR_ZEITMODELL_B_VERSTECKT, true, "(1)", // wenn
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

	/**
	 * Personalauswahlliste mit Filter & Direktfilter.
	 * 
	 * @param internalFrameI
	 *            InternalFrame
	 * @param bShowFilterButton
	 *            den New Button anzeigen
	 * @param bShowLeerenButton
	 *            den Leeren Button anzeigen
	 * @param selectedIIdI
	 *            die iId des in der Liste selektierten Personals
	 * @return PanelQueryFLR die Auswahlliste
	 * @throws Throwable
	 *             Ausnahme
	 */
	public PanelQueryFLR createPanelFLRPersonal(InternalFrame internalFrameI,
			boolean bShowFilterButton, boolean bShowLeerenButton,
			Integer selectedIIdI) throws Throwable {

		String[] aWhichButtonIUse = SystemFilterFactory.getInstance()
				.createButtonArray(bShowFilterButton, bShowLeerenButton);

		PanelQueryFLR panelQueryFLRPersonal = new PanelQueryFLR(
				PersonalFilterFactory.getInstance().createQTPersonal(),
				SystemFilterFactory.getInstance().createFKMandantCNr(
						LPMain.getInstance().getTheClient().getMandant()),
				QueryParameters.UC_ID_PERSONAL, aWhichButtonIUse,
				internalFrameI, LPMain.getInstance().getTextRespectUISPr(
						"title.personalauswahlliste"));

		panelQueryFLRPersonal.befuellePanelFilterkriterienDirektUndVersteckte(
				createFKDPersonalname(), createFKDPersonalnummer(),
				createFKVPersonal());
		panelQueryFLRPersonal.addDirektFilter(PersonalFilterFactory
				.getInstance().createFKDAusweis());
		if (selectedIIdI != null) {
			panelQueryFLRPersonal.setSelectedId(selectedIIdI);
		}

		return panelQueryFLRPersonal;
	}

	public PanelQueryFLR createPanelFLRPersonalMitKostenstelle(
			InternalFrame internalFrameI, boolean bShowFilterButton,
			boolean bShowLeerenButton, Integer selectedIIdI) throws Throwable {

		String[] aWhichButtonIUse = SystemFilterFactory.getInstance()
				.createButtonArray(bShowFilterButton, bShowLeerenButton);

		PanelQueryFLR panelQueryFLRPersonal = new PanelQueryFLR(
				PersonalFilterFactory.getInstance().createQTPersonal(),
				SystemFilterFactory.getInstance().createFKMandantCNr(
						LPMain.getInstance().getTheClient().getMandant()),
				QueryParameters.UC_ID_PERSONAL_ZEITERFASSUNG, aWhichButtonIUse,
				internalFrameI, LPMain.getInstance().getTextRespectUISPr(
						"title.personalauswahlliste"));

		panelQueryFLRPersonal.befuellePanelFilterkriterienDirektUndVersteckte(
				createFKDPersonalname(), createFKDPersonalnummer(),
				createFKVPersonal());
		panelQueryFLRPersonal.addDirektFilter(PersonalFilterFactory
				.getInstance().createFKDAusweis());
		if (selectedIIdI != null) {
			panelQueryFLRPersonal.setSelectedId(selectedIIdI);
		}

		return panelQueryFLRPersonal;
	}

	public FilterKriterium createFKHasEmailAddress() throws Throwable {
		return new FilterKriterium("flrpartner.c_email", true, "",
				FilterKriterium.OPERATOR_IS + " "
						+ FilterKriterium.OPERATOR_NOT_NULL, false);
	}

	public PanelQueryFLR createPanelFLRPersonalMitEmailAdresse(
			InternalFrame internalFrameI, boolean bShowFilterButton,
			boolean bShowLeerenButton, Integer selectedIIdI) throws Throwable {
		String[] aWhichButtonIUse = SystemFilterFactory.getInstance()
				.createButtonArray(bShowFilterButton, bShowLeerenButton);

		FilterKriterium[] mandantFilter = SystemFilterFactory.getInstance()
				.createFKMandantCNr(LPMain.getTheClient().getMandant());
		FilterKriterium[] otherFilters = new FilterKriterium[2];
		otherFilters[0] = createFKHasEmailAddress();
		otherFilters[1] = createFKVPersonal();
		FilterKriterium[] allFilters = FilterKriterium.concat(mandantFilter,
				otherFilters);

		PanelQueryFLR panelQueryFLRPersonal = new PanelQueryFLR(
				PersonalFilterFactory.getInstance().createQTPersonal(),
				allFilters, QueryParameters.UC_ID_PERSONAL, aWhichButtonIUse,
				internalFrameI, LPMain.getInstance().getTextRespectUISPr(
						"title.personalauswahlliste"));

		panelQueryFLRPersonal.addDirektFilter(createFKDPersonalname());
		panelQueryFLRPersonal.addDirektFilter(createFKDPersonalnummer());
		panelQueryFLRPersonal.addDirektFilter(createFKDAusweis());

		if (selectedIIdI != null) {
			panelQueryFLRPersonal.setSelectedId(selectedIIdI);
		}

		return panelQueryFLRPersonal;
	}

}
