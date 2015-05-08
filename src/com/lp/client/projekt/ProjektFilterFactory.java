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
package com.lp.client.projekt;

import com.lp.client.angebot.AngebotFilterFactory;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.system.SystemFilterFactory;
import com.lp.client.util.fastlanereader.gui.QueryType;
import com.lp.server.partner.service.KundeFac;
import com.lp.server.partner.service.PartnerFac;
import com.lp.server.personal.service.PersonalFac;
import com.lp.server.personal.service.ZeiterfassungFac;
import com.lp.server.projekt.service.ProjektFac;
import com.lp.server.projekt.service.ProjektServiceFac;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.system.service.SystemFac;
import com.lp.server.util.Facade;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.FilterKriteriumDirekt;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;

@SuppressWarnings("static-access")
/**
 * <p> Diese Klasse ist ein Singleton und kuemmert sich um alle Partnerfilter.</p>
 *
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 *
 * <p>Erstellung: Josef Ornetsmueller; 18.04.05</p>
 *
 * <p>@author $Author: christian $</p>
 *
 * @version $Revision: 1.10 $ Date $Date: 2013/01/17 09:02:25 $
 */
public class ProjektFilterFactory {
	private static ProjektFilterFactory filterFactory = null;

	private ProjektFilterFactory() {
		// Singleton.
	}

	/**
	 * Hole das Singelton FilterFactory.
	 * 
	 * @return LPMain
	 */
	static public ProjektFilterFactory getInstance() {
		if (filterFactory == null) {
			filterFactory = new ProjektFilterFactory();
		}
		return filterFactory;
	}

	public PanelQueryFLR createPanelFLRProjekt(InternalFrame internalFrameI)
			throws Throwable {
		return createPanelFLRProjekt(internalFrameI, null, false);
	}

	public PanelQueryFLR createPanelFLRProjekt(InternalFrame internalFrameI,
			Integer selectedId, boolean bMitLeerenButton) throws Throwable {

		String[] aWhichButtonIUse = SystemFilterFactory.getInstance()
				.createButtonArray(true, bMitLeerenButton);

		QueryType[] querytypes = ProjektFilterFactory.getInstance()
				.createQTPanelProjektAuswahl();
		PanelQueryFLR plProjekt = new PanelQueryFLR(querytypes,
				SystemFilterFactory.getInstance().createFKMandantCNr(),
				QueryParameters.UC_ID_PROJEKT, aWhichButtonIUse,
				internalFrameI,
				LPMain.getTextRespectUISPr("title.projektauswahlliste"));

		plProjekt.setFilterComboBox(DelegateFactory.getInstance()
				.getProjektServiceDelegate().getAllBereich(),
				new FilterKriterium(ProjektFac.FLR_PROJEKT_BEREICH_I_ID, true,
						"" + "", FilterKriterium.OPERATOR_EQUAL, false), true);

		FilterKriteriumDirekt fkDirekt1 = ProjektFilterFactory.getInstance()
				.createFKDPartnerIId();

		FilterKriteriumDirekt fkDirekt2 = ProjektFilterFactory.getInstance()
				.createFKDTitelVolltextsuche();

		plProjekt.befuellePanelFilterkriterienDirektUndVersteckte(
				ProjektFilterFactory.getInstance().createFKDProjektnummer(),
				fkDirekt1, createFKVAlleOffene(),
				LPMain.getTextRespectUISPr("proj.alle"));
		plProjekt.addDirektFilter(fkDirekt2);
		plProjekt.addDirektFilter(ProjektFilterFactory.getInstance()
				.createFKDTextSuchen());

		if (selectedId != null) {
			plProjekt.setSelectedId(selectedId);
		}

		return plProjekt;
	}

	public PanelQueryFLR createPanelFLRProjekterledigungsgrund(
			InternalFrame internalFrameI, boolean bShowFilterButton,
			boolean bShowLeerenButton) throws Throwable {
		String[] aWhichButtonIUse = SystemFilterFactory.getInstance()
				.createButtonArray(bShowFilterButton, bShowLeerenButton);

		PanelQueryFLR panelQueryFLRErledigungsgrund = new PanelQueryFLR(null,
				ProjektFilterFactory.getInstance().createFKMandantCNr(),
				QueryParameters.UC_ID_PROJEKTERLEDIGUNGSGRUND,
				aWhichButtonIUse, internalFrameI, LPMain.getInstance()
						.getTextRespectUISPr("proj.erledigungsgrund"));

		return panelQueryFLRErledigungsgrund;
	}

	/**
	 * Direktes Filter Kriterium Projektnummer fuer das PanelProjektAuswahl.
	 * 
	 * @return FilterKriteriumDirekt
	 * @throws Throwable
	 */
	public FilterKriteriumDirekt createFKDProjektnummer() throws Throwable {
		return new FilterKriteriumDirekt("c_nr", "",
				FilterKriterium.OPERATOR_LIKE, LPMain.getInstance()
						.getTextRespectUISPr("proj.label.nr"),
				FilterKriteriumDirekt.PROZENT_LEADING, // Auswertung als '%XX'
				true, // wrapWithSingleQuotes
				false, Facade.MAX_UNBESCHRAENKT); // ignore case
	}

	/**
	 * Filterkriterium fuer Filter "mandant_c_nr".
	 * 
	 * @return FilterKriterium[] die Filter Kriterien
	 * @throws Throwable
	 */
	public FilterKriterium[] createFKMandantCNr() throws Throwable {
		FilterKriterium[] kriterien = new FilterKriterium[1];

		FilterKriterium krit1 = new FilterKriterium(
				ProjektFac.FLR_PROJEKT_MANDANT_C_NR, true, "'"
						+ LPMain.getInstance().getTheClient().getMandant()
						+ "'", FilterKriterium.OPERATOR_EQUAL, false);

		kriterien[0] = krit1;

		return kriterien;
	}

	public FilterKriterium[] createFKProjektauswahl() throws Throwable {

		FilterKriterium[] kriterien = new FilterKriterium[1];

		FilterKriterium krit1 = new FilterKriterium(
				ProjektFac.FLR_PROJEKT_MANDANT_C_NR, true, "'"
						+ LPMain.getInstance().getTheClient().getMandant()
						+ "'", FilterKriterium.OPERATOR_EQUAL, false);

		kriterien[0] = krit1;

		return kriterien;

	}

	public FilterKriterium[] createFKPersonalIId() throws Throwable {

		FilterKriterium[] kriterien = new FilterKriterium[2];
		FilterKriterium krit0 = new FilterKriterium(
				ProjektFac.FLR_PROJEKT_MANDANT_C_NR, true, "'"
						+ LPMain.getInstance().getTheClient().getMandant()
						+ "'", FilterKriterium.OPERATOR_EQUAL, false);
		FilterKriterium krit1 = new FilterKriterium(
				ProjektFac.FLR_PROJEKT_PERSONAL_I_ID_ZUGEWIESENER, true, "'"
						+ LPMain.getInstance().getTheClient().getIDPersonal()
						+ "'", FilterKriterium.OPERATOR_EQUAL, false);

		kriterien[0] = krit0;
		kriterien[1] = krit1;

		return kriterien;

	}

	public FilterKriteriumDirekt createFKDTitelVolltextsuche() throws Throwable {

		return new FilterKriteriumDirekt(ProjektFac.FLR_PROJEKT_C_TITEL, "",
				FilterKriterium.OPERATOR_LIKE, LPMain.getInstance()
						.getTextRespectUISPr("proj.label.titel"),
				FilterKriteriumDirekt.PROZENT_BOTH, true, true,
				Facade.MAX_UNBESCHRAENKT); // wrapWithSingleQuotes

	}

	public FilterKriteriumDirekt createFKDHistoryVolltextsuche()
			throws Throwable {

		return new FilterKriteriumDirekt(ProjektFac.FLR_PROJEKT_C_TITEL, "",
				FilterKriterium.OPERATOR_LIKE, LPMain.getInstance()
						.getTextRespectUISPr("proj.label.titel"),
				FilterKriteriumDirekt.PROZENT_BOTH, true, false,
				Facade.MAX_UNBESCHRAENKT); // wrapWithSingleQuotes

	}

	public FilterKriterium[] createFKMeineOffene() throws Throwable {

		FilterKriterium[] kriterien = new FilterKriterium[4];

		FilterKriterium krit0 = new FilterKriterium(
				ProjektFac.FLR_PROJEKT_MANDANT_C_NR, true, "'"
						+ LPMain.getInstance().getTheClient().getMandant()
						+ "'", FilterKriterium.OPERATOR_EQUAL, false);

		FilterKriterium krit1 = new FilterKriterium(
				ProjektFac.FLR_PROJEKT_PERSONAL_I_ID_ZUGEWIESENER, true, "'"
						+ LPMain.getInstance().getTheClient().getIDPersonal()
						+ "'", FilterKriterium.OPERATOR_EQUAL, false);

		FilterKriterium krit2 = new FilterKriterium(
				ProjektFac.FLR_PROJEKT_FLRPROJEKTSTATUS + ".b_erledigt", true,
				"0", FilterKriterium.OPERATOR_EQUAL, false);

		FilterKriterium krit3 = new FilterKriterium(
				ProjektFac.FLR_PROJEKT_STATUS_C_NR, true, "'"
						+ ProjektServiceFac.PROJEKT_STATUS_STORNIERT + "'",
				FilterKriterium.OPERATOR_NOT_EQUAL, false);

		kriterien[0] = krit0;
		kriterien[1] = krit1;
		kriterien[2] = krit2;
		kriterien[3] = krit3;

		return kriterien;

	}

	public FilterKriterium[] createFKAlleOffene() throws Throwable {

		FilterKriterium[] kriterien = new FilterKriterium[3];

		FilterKriterium krit0 = new FilterKriterium(
				ProjektFac.FLR_PROJEKT_MANDANT_C_NR, true, "'"
						+ LPMain.getInstance().getTheClient().getMandant()
						+ "'", FilterKriterium.OPERATOR_EQUAL, false);

		FilterKriterium krit1 = new FilterKriterium(
				ProjektFac.FLR_PROJEKT_FLRPROJEKTSTATUS + ".b_erledigt", true,
				"0", FilterKriterium.OPERATOR_EQUAL, false);

		FilterKriterium krit2 = new FilterKriterium(
				ProjektFac.FLR_PROJEKT_STATUS_C_NR, true, "'"
						+ ProjektServiceFac.PROJEKT_STATUS_STORNIERT + "'",
				FilterKriterium.OPERATOR_NOT_EQUAL, false);

		kriterien[0] = krit0;
		kriterien[1] = krit1;
		kriterien[2] = krit2;

		return kriterien;

	}

	public FilterKriterium createFKVAlleOffene() throws Throwable {

		FilterKriterium krit1 = new FilterKriterium(
				ProjektFac.FLR_PROJEKT_STATUS_C_NR, true, "('"
						+ LocaleFac.STATUS_FERTIG + "','"
						+ LocaleFac.STATUS_STORNIERT + "','"
						+ LocaleFac.STATUS_ERLEDIGT + "','"
						+ ProjektServiceFac.PROJEKT_STATUS_GETESTET + "')",
				FilterKriterium.OPERATOR_NOT_IN, false);

		return krit1;
	}

	public FilterKriterium[] createFKProjekt(Integer iIdProjekt) {
		FilterKriterium[] kriterien = new FilterKriterium[1];
		FilterKriterium krit1 = new FilterKriterium(
				ProjektFac.FLR_HISTORY_PROJEKT_I_ID, true, "'"
						+ iIdProjekt.toString() + "'",
				FilterKriterium.OPERATOR_EQUAL, false);
		kriterien[0] = krit1;

		return kriterien;
	}

	public FilterKriterium[] createFKProjektQueue(Integer iIdPersonal) {
		FilterKriterium[] kriterien = new FilterKriterium[2];
		FilterKriterium krit1 = new FilterKriterium(
				ProjektFac.FLR_PROJEKT_PERSONAL_I_ID_ZUGEWIESENER, true, "'"
						+ iIdPersonal.toString() + "'",
				FilterKriterium.OPERATOR_EQUAL, false);
		FilterKriterium krit2 = new FilterKriterium(
				ProjektFac.FLR_PROJEKT_I_SORT, true, "",
				FilterKriterium.OPERATOR_IS + " "
						+ FilterKriterium.OPERATOR_NOT_NULL, false);

		kriterien[0] = krit1;
		kriterien[1] = krit2;

		return kriterien;
	}

	public FilterKriterium[] createFKProjektTelefonzeiten(Integer projektIId) {
		FilterKriterium[] kriterien = new FilterKriterium[1];
		FilterKriterium krit1 = new FilterKriterium("projekt_i_id", true, ""
				+ projektIId.toString() + "", FilterKriterium.OPERATOR_EQUAL,
				false);

		kriterien[0] = krit1;

		return kriterien;
	}

	/**
	 * Direktes Filter Kriterium TextSuche PanelAuftragAuswahl.
	 * 
	 * @return FilterKriteriumDirekt
	 * @throws Throwable
	 */
	public FilterKriteriumDirekt createFKDTextSuchen() throws Throwable {

		return new FilterKriteriumDirekt("c_suche", "",
				FilterKriterium.OPERATOR_LIKE, LPMain.getInstance()
						.getTextRespectUISPr("lp.textsuche"),
				FilterKriteriumDirekt.PROZENT_BOTH, // Auswertung als '%XX'
				true, // wrapWithSingleQuotes
				true, Facade.MAX_UNBESCHRAENKT); // ignore case
	}

	/**
	 * Direktes Filter Kriterium ProjektIid fuer das PanelProjektAuswahl.
	 * 
	 * @return FilterKriteriumDirekt
	 * @throws Throwable
	 */
	public FilterKriteriumDirekt createFKDProjektIId() throws Throwable {
		return new FilterKriteriumDirekt(ProjektFac.FLR_PROJEKT_I_ID, "",
				FilterKriterium.OPERATOR_LIKE, LPMain.getInstance()
						.getTextRespectUISPr("proj.label.nr"),
				FilterKriteriumDirekt.PROZENT_TRAILING, // Auswertung als '%XX'
				true, // wrapWithSingleQuotes
				false, Facade.MAX_UNBESCHRAENKT); // ignore case
	}

	/**
	 * Direktes Filter Kriterium ProjektIid fuer das PanelProjektAuswahl.
	 * 
	 * @return FilterKriteriumDirekt
	 * @throws Throwable
	 */
	public FilterKriteriumDirekt createFKDPartnerIId() throws Throwable {
		return new FilterKriteriumDirekt(
				"flrpartner.c_name1nachnamefirmazeile1", "",
				FilterKriterium.OPERATOR_LIKE, LPMain.getInstance()
						.getTextRespectUISPr("part.firma_nachname"),
				FilterKriteriumDirekt.PROZENT_TRAILING, // Auswertung als '%XX'
				true, // wrapWithSingleQuotes
				true, Facade.MAX_UNBESCHRAENKT); // ignore case
	}

	/**
	 * Direktes Filter Kriterium Titel fuer das PanelProjektAuswahl.
	 * 
	 * @return FilterKriteriumDirekt
	 * @throws Throwable
	 */
	public FilterKriteriumDirekt createFKDTitel() throws Throwable {
		return new FilterKriteriumDirekt(ProjektFac.FLR_PROJEKT_C_TITEL, "",
				FilterKriterium.OPERATOR_LIKE, LPMain.getInstance()
						.getTextRespectUISPr("proj.label.titel"),
				FilterKriteriumDirekt.PROZENT_TRAILING, // Auswertung als '%XX'
				true, // wrapWithSingleQuotes
				true, Facade.MAX_UNBESCHRAENKT); // ignore case
	}

	/**
	 * Die UI FilterKriterien am Panel Projektauswahl. <br>
	 * - Auswahl nach Kategorie <br>
	 * - Auswahl nach Erzeuger <br>
	 * - Auswahl nach Typ <br>
	 * - Auswahl nach Zugewiesener/Fuer <br>
	 * - Auswahl nach Prioritaet <br>
	 * - Auswahl nach Status <br>
	 * - Auswahl nach Zieltermin
	 * 
	 * @return QueryType[]
	 */
	public QueryType[] createQTPanelProjektAuswahl() throws Throwable {
		QueryType[] types = new QueryType[9];

		FilterKriterium f1 = new FilterKriterium(
				ProjektFac.FLR_PROJEKT_KATEGORIE_C_NR, true, "",
				FilterKriterium.OPERATOR_LIKE, true);

		types[0] = new QueryType(LPMain.getInstance().getTextRespectUISPr(
				"lp.kategorie"), f1,
				new String[] { FilterKriterium.OPERATOR_EQUAL }, true, // wrapValueWithSingleQuotes
				true);

		ParametermandantDto parameter = (ParametermandantDto) DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getParametermandant(
						ParameterFac.PARAMETER_KURZZEICHEN_STATT_NAME_IN_AUSWAHLLISTE,
						ParameterFac.KATEGORIE_PROJEKT,
						LPMain.getTheClient().getMandant());
		boolean bKurzzeichenStattName = (Boolean) parameter.getCWertAsObject();

		FilterKriterium f2 = null;
		if (bKurzzeichenStattName) {
			f2 = new FilterKriterium(ProjektFac.FLR_PROJEKT_FLRPERSONALERZEUGER
					+ "." + PersonalFac.FLR_PERSONAL_C_KURZZEICHEN, true, "",
					FilterKriterium.OPERATOR_LIKE, true);
		} else {
			f2 = new FilterKriterium(ProjektFac.FLR_PROJEKT_FLRPERSONALERZEUGER
					+ "." + PersonalFac.FLR_PERSONAL_FLRPARTNER + "."
					+ PartnerFac.FLR_PARTNER_NAME1NACHNAMEFIRMAZEILE1, true,
					"", FilterKriterium.OPERATOR_LIKE, true);
		}

		types[1] = new QueryType(LPMain.getInstance().getTextRespectUISPr(
				"proj.personal.erzeuger"), f2,
				new String[] { FilterKriterium.OPERATOR_EQUAL }, true, true);

		FilterKriterium f3 = new FilterKriterium(
				ProjektFac.FLR_PROJEKT_TYP_C_NR, true, "",
				FilterKriterium.OPERATOR_LIKE, true);

		types[2] = new QueryType(LPMain.getInstance().getTextRespectUISPr(
				"proj.projekt.label.typ"), f3,
				new String[] { FilterKriterium.OPERATOR_EQUAL }, true, // flrdate:
																		// eingabeformat
																		// 10.12.2004
				true, true);

		FilterKriterium f4 = null;
		if (bKurzzeichenStattName) {
			f4 = new FilterKriterium(
					ProjektFac.FLR_PROJEKT_FLRPERSONALZUGEWIESENER + "."
							+ PersonalFac.FLR_PERSONAL_C_KURZZEICHEN, true, "",
					FilterKriterium.OPERATOR_LIKE, true);
		} else {
			f4 = new FilterKriterium(
					ProjektFac.FLR_PROJEKT_FLRPERSONALZUGEWIESENER + "."
							+ PersonalFac.FLR_PERSONAL_FLRPARTNER + "."
							+ PartnerFac.FLR_PARTNER_NAME1NACHNAMEFIRMAZEILE1,
					true, "", FilterKriterium.OPERATOR_LIKE, true);
		}

		types[3] = new QueryType(LPMain.getInstance().getTextRespectUISPr(
				"proj.personal.fuer"), f4,
				new String[] { FilterKriterium.OPERATOR_EQUAL }, true, true);

		FilterKriterium f5 = new FilterKriterium(ProjektFac.FLR_PROJEKT_I_PRIO,
				true, "", FilterKriterium.OPERATOR_EQUAL, false);

		types[4] = new QueryType(LPMain.getInstance().getTextRespectUISPr(
				"proj.projekt.label.prio"), f5, new String[] {
				FilterKriterium.OPERATOR_EQUAL, FilterKriterium.OPERATOR_GTE,
				FilterKriterium.OPERATOR_LTE }, false, false, false);

		FilterKriterium f6 = new FilterKriterium(
				ProjektFac.FLR_PROJEKT_STATUS_C_NR, true, "",
				FilterKriterium.OPERATOR_LIKE, true);

		types[5] = new QueryType(LPMain.getInstance().getTextRespectUISPr(
				"lp.status"), f6,
				new String[] { FilterKriterium.OPERATOR_EQUAL }, true, // wrapValueWithSingleQuotes
				true);

		FilterKriterium f7 = new FilterKriterium(
				ProjektFac.FLR_PROJEKT_T_ZIELDATUM, true, "",
				FilterKriterium.OPERATOR_EQUAL, false);

		types[6] = new QueryType(LPMain.getInstance().getTextRespectUISPr(
				"proj.zeiltermin"), f7, new String[] {
				FilterKriterium.OPERATOR_EQUAL, FilterKriterium.OPERATOR_GTE,
				FilterKriterium.OPERATOR_LTE }, true, false, false);

		FilterKriterium f8 = new FilterKriterium(KundeFac.FLR_PARTNER + "."
				+ PartnerFac.FLR_PARTNER_FLRLANDPLZORT + "."
				+ SystemFac.FLR_LP_FLRLAND + "." + SystemFac.FLR_LP_LANDLKZ,
				true, "", FilterKriterium.OPERATOR_LIKE, false);

		types[7] = new QueryType(LPMain.getInstance().getTextRespectUISPr(
				"lp.lkz1"), f8, new String[] { FilterKriterium.OPERATOR_LIKE },
				true, false, false);

		FilterKriterium f9 = new FilterKriterium(KundeFac.FLR_PARTNER + "."
				+ PartnerFac.FLR_PARTNER_FLRLANDPLZORT + "."
				+ SystemFac.FLR_LP_LANDPLZORTPLZ, true, "",
				FilterKriterium.OPERATOR_LIKE, false);

		types[8] = new QueryType(LPMain.getInstance().getTextRespectUISPr(
				"lp.plz"), f9, new String[] { FilterKriterium.OPERATOR_LIKE },
				true, false, false);

		return types;
	}

}
