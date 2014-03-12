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
package com.lp.client.system;

import java.util.Calendar;

import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQuery;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.PanelQueryFLROrtAnlegen;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.util.fastlanereader.gui.QueryType;
import com.lp.server.artikel.service.ArtikelFac;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.system.service.DokumenteFac;
import com.lp.server.system.service.MandantFac;
import com.lp.server.system.service.MediaFac;
import com.lp.server.system.service.PanelFac;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.system.service.SystemFac;
import com.lp.server.system.service.VersandFac;
import com.lp.server.util.Facade;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.FilterKriteriumDirekt;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.util.Helper;

@SuppressWarnings("static-access")
/*
 * <p> Diese Klasse ist ein Singleton und kuemmert sich Filter im Modul
 * System.</p>
 * 
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 * 
 * <p>Erstellung: 17. 08. 2005</p>
 * 
 * <p>@author Martin Bluehweis</p>
 * 
 * @version 1.0
 * 
 * @version $Revision: 1.17 $ Date $Date: 2012/09/25 15:57:51 $
 */
public class SystemFilterFactory {
	private static SystemFilterFactory filterFactory = null;

	private SystemFilterFactory() {
		// Singleton.
	}

	/**
	 * Hole das Singelton SystemFilterFactory.
	 * 
	 * @return SystemFilterFactory
	 */
	static public SystemFilterFactory getInstance() {
		if (filterFactory == null) {
			filterFactory = new SystemFilterFactory();
		}
		return filterFactory;
	}

	public FilterKriteriumDirekt createFKDLandPlzOrtOrt() {
		// 0 dem PanelQuery die direkten FilterKriterien setzen
		FilterKriteriumDirekt fkDirekt = new FilterKriteriumDirekt(
				SystemFac.FLR_LP_FLRORT + "." + SystemFac.FLR_LP_ORTNAME, "",
				FilterKriterium.OPERATOR_LIKE, LPMain.getInstance()
						.getTextRespectUISPr("lp.label.ort"),
				FilterKriteriumDirekt.PROZENT_TRAILING, // Auswertung als '%XX'
				true, // wrapWithSingleQuotes
				true, Facade.MAX_UNBESCHRAENKT); // ignore case

		return fkDirekt;
	}

	public FilterKriteriumDirekt createFKDPlzOderOrt() {
		FilterKriteriumDirekt fkDirekt = new FilterKriteriumDirekt(
				SystemFac.FLR_LP_LANDPLZORT_ORTPLZ, "",
				FilterKriterium.OPERATOR_LIKE, LPMain.getInstance()
						.getTextRespectUISPr("lp.ortplz"),
				FilterKriteriumDirekt.PROZENT_TRAILING, true, // wrapWithSingleQuotes
				true, Facade.MAX_UNBESCHRAENKT); // ignore case

		return fkDirekt;
	}

	public FilterKriteriumDirekt createFKDOrt() {
		// 0 dem PanelQuery die direkten FilterKriterien setzen
		FilterKriteriumDirekt fkDirekt = new FilterKriteriumDirekt(
				SystemFac.FLR_LP_ORTNAME, "", FilterKriterium.OPERATOR_LIKE,
				LPMain.getInstance().getTextRespectUISPr("lp.label.ort"),
				FilterKriteriumDirekt.PROZENT_TRAILING, // Auswertung als '%XX'
				true, // wrapWithSingleQuotes
				true, Facade.MAX_UNBESCHRAENKT); // ignore case

		return fkDirekt;
	}

	public FilterKriteriumDirekt createFKDLandLKZ() {
		// 0 dem PanelQuery die direkten FilterKriterien setzen
		FilterKriteriumDirekt fkDirekt = new FilterKriteriumDirekt(
				SystemFac.FLR_LP_LANDLKZ, "", FilterKriterium.OPERATOR_LIKE,
				LPMain.getInstance().getTextRespectUISPr("lp.lkz1"),
				FilterKriteriumDirekt.PROZENT_TRAILING, // Auswertung als '%XX'
				true, // wrapWithSingleQuotes
				true, Facade.MAX_UNBESCHRAENKT); // ignore case

		return fkDirekt;
	}

	public FilterKriterium[] createFKDokumente(String belegartCNr,
			Integer belegartIId) {
		FilterKriterium[] filters = new FilterKriterium[2];
		filters[0] = new FilterKriterium(
				DokumenteFac.FLR_BELEGARTDOKUMENT_BELEGART_C_NR, true, "'"
						+ belegartCNr + "'", FilterKriterium.OPERATOR_LIKE,
				false);
		filters[1] = new FilterKriterium(
				DokumenteFac.FLR_BELEGARTDOKUMENT_I_BELEGARTID, true,
				belegartIId + "", FilterKriterium.OPERATOR_IS, false);
		return filters;
	}

	public FilterKriteriumDirekt createFKDLandName() {
		// 0 dem PanelQuery die direkten FilterKriterien setzen
		FilterKriteriumDirekt fkDirekt = new FilterKriteriumDirekt(
				SystemFac.FLR_LP_LANDNAME, "", FilterKriterium.OPERATOR_LIKE,
				LPMain.getInstance().getTextRespectUISPr("lp.land"),
				FilterKriteriumDirekt.PROZENT_TRAILING, // Auswertung als '%XX'
				true, // wrapWithSingleQuotes
				true, Facade.MAX_UNBESCHRAENKT); // ignore case

		return fkDirekt;
	}

	public FilterKriteriumDirekt createFKDLandPlzOrtPLZ() {
		// 0 dem PanelQuery die direkten FilterKriterien setzen
		FilterKriteriumDirekt fkDirekt = new FilterKriteriumDirekt(
				SystemFac.FLR_LP_LANDPLZORTPLZ, "",
				FilterKriterium.OPERATOR_LIKE, LPMain.getInstance()
						.getTextRespectUISPr("lp.label.plz"),
				FilterKriteriumDirekt.PROZENT_TRAILING, // Auswertung als '%XX'
				true, // wrapWithSingleQuotes
				true, Facade.MAX_UNBESCHRAENKT); // ignore case

		return fkDirekt;
	}

	public FilterKriterium[] createFKVersandauftrag(String versandstatusCNr)
			throws Throwable {
		FilterKriterium[] filters;
		boolean bDarfAlleSehen = DelegateFactory.getInstance()
				.getTheJudgeDelegate()
				.hatRecht(RechteFac.RECHT_LP_ALLE_VERSANDAUFTRAEGE_R)
				|| DelegateFactory.getInstance().getTheJudgeDelegate()
						.hatRecht(RechteFac.RECHT_LP_ALLE_VERSANDAUFTRAEGE_UD);
		if (bDarfAlleSehen) {
			filters = new FilterKriterium[1];
		} else {
			filters = new FilterKriterium[2];
			filters[1] = new FilterKriterium(
					VersandFac.FLR_VERSANDAUFTRAG_PERSONAL_I_ID, true, LPMain
							.getInstance().getTheClient().getIDPersonal()
							.toString(), FilterKriterium.OPERATOR_EQUAL, false);
		}
		filters[0] = new FilterKriterium(
				VersandFac.FLR_VERSANDAUFTRAG_STATUS_C_NR, true,
				versandstatusCNr != null ? "'" + versandstatusCNr + "'"
						: FilterKriterium.OPERATOR_NULL,
				FilterKriterium.OPERATOR_EQUAL, false);
		return filters;
	}

	public FilterKriterium createFKVKostenstelle() {
		FilterKriterium fkVersteckt = new FilterKriterium(
				SystemFac.FLR_KOSTENSTELLE_B_VERSTECKT, true, "(1)", // wenn das
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

	public FilterKriterium createFKVSpediteur() {
		FilterKriterium fkVersteckt = new FilterKriterium(
				SystemFac.FLR_SPEDITEUR_B_VERSTECKT, true, "(1)", // wenn das
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

	public FilterKriterium createFKVLieferart() {
		FilterKriterium fkVersteckt = new FilterKriterium(
				SystemFac.FLR_LIEFERART_B_VERSTECKT, true, "(1)", // wenn das
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

	public FilterKriterium createFKVZahlungsziel() {
		FilterKriterium fkVersteckt = new FilterKriterium(
				SystemFac.FLR_ZAHLUNGSZIEL_B_VERSTECKT, true, "(1)", // wenn das
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

	public FilterKriterium createFKVMediastandard() {
		FilterKriterium fkVersteckt = new FilterKriterium(
				MediaFac.FLR_MEDIASTANDARD_B_VERSTECKT, true, "(1)", // wenn das
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

	public PanelQueryFLR createPanelFLRKostenstelle(
			InternalFrame internalFrameI, boolean bShowNewButton,
			boolean bShowLeerenButton) throws Throwable {
		return createPanelFLRKostenstelle(internalFrameI, LPMain.getInstance()
				.getTheClient().getMandant(), bShowNewButton, bShowLeerenButton);
	}

	public PanelQueryFLR createPanelFLRKostenstelle(
			InternalFrame internalFrameI, String mandantCNr,
			boolean bShowNewButton, boolean bShowLeerenButton) throws Throwable {
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
				createQTKostenstelle(), SystemFilterFactory.getInstance()
						.createFKMandantCNr(mandantCNr),
				QueryParameters.UC_ID_KOSTENSTELLE, aWhichButtonIUse,
				internalFrameI, LPMain.getInstance().getTextRespectUISPr(
						"title.kostenstelleauswahlliste"));

		panelQueryBankverbindung
				.befuellePanelFilterkriterienDirektUndVersteckte(
						createFKDKostenstelleNummer(),
						createFKDKostenstelleBezeichnung(),
						createFKVKostenstelle());
		return panelQueryBankverbindung;
	}

	public PanelQueryFLR createPanelFLRKostenstelle(
			InternalFrame internalFrameI, boolean bShowNewButton,
			boolean bShowLeerenButton, Integer selectedIIdI) throws Throwable {
		PanelQueryFLR panelQueryFLR = createPanelFLRKostenstelle(
				internalFrameI, bShowNewButton, bShowLeerenButton);
		if (selectedIIdI != null) {
			panelQueryFLR.setSelectedId(selectedIIdI);
		}
		return panelQueryFLR;
	}

	/**
	 * Direktes Filter Kriterium KENNUNG f&uuml;r jede Spr-Tabelle.
	 * 
	 * @return FilterKriteriumDirekt
	 */
	public FilterKriteriumDirekt createFKDKennung() {
		return new FilterKriteriumDirekt("c_nr", "",
				FilterKriterium.OPERATOR_LIKE, LPMain.getInstance()
						.getTextRespectUISPr("label.kennung"),
				FilterKriteriumDirekt.PROZENT_TRAILING, // Auswertung als 'XX%'
				true, true, Facade.MAX_UNBESCHRAENKT); // wrapWithSingleQuotes
	}

	/**
	 * Direktes Filter Kriterium KENNUNG f&uuml;r jede Spr-Tabelle.
	 * 
	 * @return FilterKriteriumDirekt
	 */
	public FilterKriteriumDirekt createFKDPCName() {
		return new FilterKriteriumDirekt(SystemFac.FLR_ARBEITSPLATZ_C_PCNAME,
				"", FilterKriterium.OPERATOR_LIKE, LPMain.getInstance()
						.getTextRespectUISPr("system.pcname"),
				FilterKriteriumDirekt.PROZENT_TRAILING, // Auswertung als 'XX%'
				true, true, Facade.MAX_UNBESCHRAENKT); // wrapWithSingleQuotes
	}

	public FilterKriteriumDirekt createFKDNachrichtarchivNachricht() {
		return new FilterKriteriumDirekt("nachrichtarchiv.c_nachricht", "",
				FilterKriterium.OPERATOR_LIKE, LPMain.getInstance()
						.getTextRespectUISPr("ben.nachricht"),
				FilterKriteriumDirekt.PROZENT_BOTH, // Auswertung als 'XX%'
				true, true, Facade.MAX_UNBESCHRAENKT); // wrapWithSingleQuotes
	}

	/**
	 * Direktes Filter Kriterium KENNUNG f&uuml;r jede Tabelle mit C_BEZ
	 * 
	 * @return FilterKriteriumDirekt
	 */
	public FilterKriteriumDirekt createFKDBezeichnung() {
		return new FilterKriteriumDirekt("c_bez", "",
				FilterKriterium.OPERATOR_LIKE, LPMain.getInstance()
						.getTextRespectUISPr("lp.bezeichnung"),
				FilterKriteriumDirekt.PROZENT_TRAILING, // Auswertung als 'XX%'
				true, true, Facade.MAX_UNBESCHRAENKT); // wrapWithSingleQuotes
	}

	/**
	 * Direktes Filter Kriterium Bezeichnung
	 * 
	 * @return FilterKriteriumDirekt
	 * @param set
	 *            String
	 */
	public FilterKriteriumDirekt createFKDSprTabelleBezeichnung(String set) {
		return new FilterKriteriumDirekt(set + ".c_bez", "",
				FilterKriterium.OPERATOR_LIKE, LPMain.getInstance()
						.getTextRespectUISPr("lp.bezeichnung"),
				FilterKriteriumDirekt.PROZENT_TRAILING, // Auswertung als 'XX%'
				true, true, Facade.MAX_UNBESCHRAENKT); // wrapWithSingleQuotes
	}

	/**
	 * Direktes Filter Kriterium Kundenname fuer das PanelRechnungAuswahl.
	 * 
	 * @return FilterKriteriumDirekt
	 */
	private FilterKriteriumDirekt createFKDKostenstelleNummer() {
		return new FilterKriteriumDirekt("c_nr", "",
				FilterKriterium.OPERATOR_LIKE, LPMain.getInstance()
						.getTextRespectUISPr("label.kostenstelle"),
				FilterKriteriumDirekt.PROZENT_TRAILING, // Auswertung als 'XX%'
				true, false, Facade.MAX_UNBESCHRAENKT); // wrapWithSingleQuotes
	}

	/**
	 * Direktes Filter Kriterium Kundenname fuer das PanelRechnungAuswahl.
	 * 
	 * @return FilterKriteriumDirekt
	 */
	private FilterKriteriumDirekt createFKDKostenstelleBezeichnung() {
		return new FilterKriteriumDirekt(SystemFac.FLR_KOSTENSTELLE_C_BEZ, "",
				FilterKriterium.OPERATOR_LIKE, LPMain.getInstance()
						.getTextRespectUISPr("label.bezeichnung"),
				FilterKriteriumDirekt.PROZENT_TRAILING, // Auswertung als 'XX%'
				true, true, Facade.MAX_UNBESCHRAENKT); // wrapWithSingleQuotes
	}

	private QueryType[] createQTKostenstelle() {
		QueryType[] types = new QueryType[1];

		FilterKriterium f1 = new FilterKriterium("c_nr", true, "",
				FilterKriterium.OPERATOR_LIKE, true);

		types[0] = new QueryType(LPMain.getInstance().getTextRespectUISPr(
				"label.kostenstelle"), f1,
				new String[] { FilterKriterium.OPERATOR_EQUAL }, true, true);
		return types;
	}

	public FilterKriterium[] createFKMandantCNr(String cNrMandant) {
		FilterKriterium filter[] = new FilterKriterium[1];
		filter[0] = new FilterKriterium("mandant_c_nr", true, "'" + cNrMandant
				+ "'", FilterKriterium.OPERATOR_EQUAL, false);

		return filter;
	}

	public String[] createButtonArray(boolean bShowFilterButtonI,
			boolean bShowLeerenButtonI) {
		boolean[] b = new boolean[2];
		b[0] = bShowLeerenButtonI;
		b[1] = bShowFilterButtonI;

		String[] aWhichButtonIUse = null;

		switch (Helper.booleanArray2int(b)) {
		case 0: {
			aWhichButtonIUse = new String[] {};
		}
			break;
		case 1: {
			aWhichButtonIUse = new String[] { PanelBasis.ACTION_LEEREN };
		}
			break;
		case 2: {
			aWhichButtonIUse = new String[] { PanelBasis.ACTION_FILTER };
		}
			break;
		case 3: {
			aWhichButtonIUse = new String[] { PanelBasis.ACTION_FILTER,
					PanelBasis.ACTION_LEEREN };
		}
			break;
		}

		return aWhichButtonIUse;
	}

	/**
	 * Filterkriterium fuer Filter "mandant_c_nr".
	 * 
	 * @return FilterKriterium[] die Filter Kriterien
	 * @throws Throwable
	 */
	public FilterKriterium[] createFKMandantCNr() throws Throwable {
		FilterKriterium[] kriterien = new FilterKriterium[1];
		kriterien[0] = new FilterKriterium("mandant_c_nr", true, "'"
				+ LPMain.getInstance().getTheClient().getMandant() + "'",
				FilterKriterium.OPERATOR_EQUAL, false);
		return kriterien;
	}

	public FilterKriterium[] createFKKeyAuswahlliste(Integer key)
			throws Throwable {
		FilterKriterium[] kriterien = new FilterKriterium[1];
		kriterien[0] = new FilterKriterium("i_id", true, key + "",
				FilterKriterium.OPERATOR_EQUAL, false);
		return kriterien;
	}

	/**
	 * Filterkriterium fuer Filter "mandant_c_nr" und "locale_c_nr".
	 * 
	 * @return FilterKriterium[] die Filter Kriterien
	 * @throws Throwable
	 */
	public FilterKriterium[] createFKMandantCNrLocaleCNr() throws Throwable {
		FilterKriterium[] kriterien = new FilterKriterium[2];
		FilterKriterium krit1 = new FilterKriterium("mandant_c_nr", true, "'"
				+ LPMain.getInstance().getTheClient().getMandant() + "'",
				FilterKriterium.OPERATOR_EQUAL, false);
		FilterKriterium krit2 = new FilterKriterium("locale_c_nr", true, "'"
				+ LPMain.getInstance().getTheClient().getLocUiAsString() + "'",
				FilterKriterium.OPERATOR_EQUAL, false);
		kriterien[0] = krit1;
		kriterien[1] = krit2;
		return kriterien;
	}

	public FilterKriterium[] createFKPanelGesperrt() throws Throwable {
		FilterKriterium[] kriterien = new FilterKriterium[1];
		kriterien[0] = new FilterKriterium("c_benutzername", true,
				"'lpwebappzemecs%'", FilterKriterium.OPERATOR_NOT + " "
						+ FilterKriterium.OPERATOR_LIKE, false);

		return kriterien;
	}

	public FilterKriterium[] createFKPanelOnlyLoggedIn() throws Throwable {
		FilterKriterium[] kriterien = new FilterKriterium[2];
		kriterien[0] = new FilterKriterium("c_benutzername", true,
				"'lpwebappzemecs%'", FilterKriterium.OPERATOR_NOT + " "
						+ FilterKriterium.OPERATOR_LIKE, false);
		kriterien[1] = new FilterKriterium("t_loggedout", 
				true, "NULL", FilterKriterium.OPERATOR_IS, false) ;
		return kriterien;
	}
	
	public FilterKriterium[] createFKPanelbeschreibung(String panelCNr)
			throws Throwable {
		FilterKriterium[] kriterien = new FilterKriterium[2];
		kriterien[0] = new FilterKriterium(
				PanelFac.FLR_PANELBESCHREIBUNG_PANEL_C_NR, true, "'" + panelCNr
						+ "'", FilterKriterium.OPERATOR_EQUAL, false);

		kriterien[1] = new FilterKriterium("mandant_c_nr", true, "'"
				+ LPMain.getInstance().getTheClient().getMandant() + "'",
				FilterKriterium.OPERATOR_EQUAL, false);

		return kriterien;
	}

	public FilterKriterium[] createFKPaneldokumenteBelegart() throws Throwable {
		FilterKriterium[] kriterien = new FilterKriterium[1];
		kriterien[0] = new FilterKriterium("id_comp.mandant_c_nr", true, "'"
				+ LPMain.getInstance().getTheClient().getMandant() + "'",
				FilterKriterium.OPERATOR_EQUAL, false);

		return kriterien;
	}

	public FilterKriterium[] createFKPaneldokumenteGruppe() throws Throwable {
		FilterKriterium[] kriterien = new FilterKriterium[1];
		kriterien[0] = new FilterKriterium("id_comp.mandant_c_nr", true, "'"
				+ LPMain.getInstance().getTheClient().getMandant() + "'",
				FilterKriterium.OPERATOR_EQUAL, false);

		return kriterien;
	}

	public FilterKriterium[] createFKArbeitsplatzparameter(
			Integer arbeitsplatzIId) throws Throwable {
		FilterKriterium[] kriterien = new FilterKriterium[1];
		kriterien[0] = new FilterKriterium(
				SystemFac.FLR_ARBEITSPLATZPARAMETER_ARBEITSPLATZ_I_ID, true,
				arbeitsplatzIId + "", FilterKriterium.OPERATOR_EQUAL, false);

		return kriterien;
	}

	/**
	 * Filterkriterium fuer Filter "c_nr".
	 * 
	 * @return FilterKriterium[] die Filter Kriterien
	 * @throws Throwable
	 */
	public FilterKriterium[] createFKMandantFixCNr() throws Throwable {
		FilterKriterium[] kriterien = new FilterKriterium[1];

		FilterKriterium krit1 = new FilterKriterium("c_nr", true, "'"
				+ LPMain.getInstance().getTheClient().getMandant() + "'",
				FilterKriterium.OPERATOR_EQUAL, false);

		kriterien[0] = krit1;

		return kriterien;
	}

	public FilterKriterium[] createFKSchnellansichtNachrichtarchiv() {
		FilterKriterium[] fkVersteckt = new FilterKriterium[] { new FilterKriterium(
				"nachrichtarchiv.t_erledigt", true, "NULL" + "",
				FilterKriterium.OPERATOR_IS, false) };

		return fkVersteckt;
	}

	public FilterKriterium[] createFKNachrichtarchiv() throws Throwable {
		FilterKriterium[] kriterien = new FilterKriterium[1];

		FilterKriterium krit1 = new FilterKriterium("tr.i_id", true,
				DelegateFactory.getInstance().getTheJudgeDelegate()
						.getSystemrolleIId()
						+ "", FilterKriterium.OPERATOR_EQUAL, false);

		kriterien[0] = krit1;
		return kriterien;
	}

	public PanelQueryFLR createPanelFLRMwstbez(InternalFrame internalFrameI,
			boolean bShowFilterButton, boolean bShowLeerenButton,
			Integer selectedIIdI) throws Throwable {

		String[] aWhichButtonIUse = null;

		PanelQueryFLR panelQueryFLRMwstbez = new PanelQueryFLR(null,
				SystemFilterFactory.getInstance().createFKMandantCNr(
						LPMain.getInstance().getTheClient().getMandant()),
				QueryParameters.UC_ID_MWSTSATZBEZ, aWhichButtonIUse,
				internalFrameI, LPMain.getInstance().getTextRespectUISPr(
						"lp.mwstbezeichnung"));

		if (selectedIIdI != null) {
			panelQueryFLRMwstbez.setSelectedId(selectedIIdI);
		}

		return panelQueryFLRMwstbez;
	}

	/**
	 * Filterkriterium fuer Filter "id_comp.mandant_c_nr".
	 * 
	 * @return FilterKriterium[] die Filter Kriterien
	 * @throws Throwable
	 */
	public FilterKriterium[] createFKIdCompMandantcnr() throws Throwable {
		FilterKriterium[] kriterien = null;

		if (LPMain
				.getInstance()
				.getDesktop()
				.darfAnwenderAufZusatzfunktionZugreifen(
						MandantFac.ZUSATZFUNKTION_INTERNER_VERSAND)) {
			kriterien = new FilterKriterium[1];

			FilterKriterium krit1 = new FilterKriterium("id_comp.mandant_c_nr",
					true, "'"
							+ LPMain.getInstance().getTheClient().getMandant()
							+ "'", FilterKriterium.OPERATOR_EQUAL, false);

			kriterien[0] = krit1;
		} else {
			kriterien = new FilterKriterium[2];

			kriterien[0] = new FilterKriterium("id_comp.mandant_c_nr", true,
					"'" + LPMain.getInstance().getTheClient().getMandant()
							+ "'", FilterKriterium.OPERATOR_EQUAL, false);
			kriterien[1] = new FilterKriterium("id_comp.c_kategorie", true, "'"
					+ ParameterFac.KATEGORIE_VERSANDAUFTRAG + "'",
					FilterKriterium.OPERATOR_NOT_EQUAL, false);
		}

		return kriterien;
	}

	/**
	 * Direktes Filterkriterium fuer "%id_comp.c_nr%".
	 * 
	 * @return FilterKriterium[] die Filter Kriterien
	 * @throws Throwable
	 */
	public FilterKriteriumDirekt createFKDIdCompCNr() throws Throwable {
		return new FilterKriteriumDirekt("id_comp.c_nr", "",
				FilterKriterium.OPERATOR_LIKE, LPMain.getInstance()
						.getTextRespectUISPr("lp.parameter"),
				FilterKriteriumDirekt.PROZENT_BOTH, // Auswertung als '%X%'
				true, // wrapWithSingleQuotes
				true, Facade.MAX_UNBESCHRAENKT); // ignore case
	}

	/**
	 * Direktes Filterkriterium fuer "id_comp.c_kategorie%".
	 * 
	 * @return FilterKriterium[] die Filter Kriterien
	 * @throws Throwable
	 */
	public FilterKriteriumDirekt createFKDIdCompCKategorie() throws Throwable {
		return new FilterKriteriumDirekt("id_comp."
				+ ParameterFac.FLR_PARAMETERMANDANT_C_KATEGORIE, "",
				FilterKriterium.OPERATOR_LIKE, LPMain.getInstance()
						.getTextRespectUISPr("lp.kategorie"),
				FilterKriteriumDirekt.PROZENT_TRAILING, // Auswertung als 'X%'
				true, // wrapWithSingleQuotes
				true, Facade.MAX_UNBESCHRAENKT); // ignore case
	}

	public PanelQueryFLR createPanelFLRLandplzort(InternalFrame internalFrameI,
			Integer selectedId, boolean bMitLeerenButton) throws Throwable {
		String[] aWhichButtonIUse = null;
		if (bMitLeerenButton) {
			aWhichButtonIUse = new String[] { PanelBasis.ACTION_REFRESH,
					PanelBasis.ACTION_FILTER, PanelBasis.ACTION_LEEREN,
					PanelBasis.ACTION_NEW };
		} else {
			aWhichButtonIUse = new String[] { PanelBasis.ACTION_REFRESH,
					PanelBasis.ACTION_FILTER, PanelBasis.ACTION_NEW };
		}

		QueryType[] types = new QueryType[3];

		// Land
		FilterKriterium f1 = new FilterKriterium(SystemFac.FLR_LP_FLRLAND + "."
				+ SystemFac.FLR_LP_LANDLKZ, true, "",
				FilterKriterium.OPERATOR_LIKE, true);

		types[0] = new QueryType(LPMain.getInstance().getTextRespectUISPr(
				"lp.land"), f1,
				new String[] { FilterKriterium.OPERATOR_EQUAL }, true, true);
		// Plz
		FilterKriterium f2 = new FilterKriterium(
				SystemFac.FLR_LP_LANDPLZORTPLZ, true, "",
				FilterKriterium.OPERATOR_LIKE, true);

		types[1] = new QueryType(LPMain.getInstance().getTextRespectUISPr(
				"lp.plz"), f2, new String[] { FilterKriterium.OPERATOR_EQUAL },
				true, true);
		// Ort
		FilterKriterium f3 = new FilterKriterium(SystemFac.FLR_LP_FLRORT + "."
				+ SystemFac.FLR_LP_ORTNAME, true, "",
				FilterKriterium.OPERATOR_LIKE, true);

		types[2] = new QueryType(LPMain.getInstance().getTextRespectUISPr(
				"lp.ort"), f3, new String[] { FilterKriterium.OPERATOR_EQUAL },
				true, true);

		PanelQueryFLR panelQueryFLRLandplzort = new PanelQueryFLROrtAnlegen(
				types, null, aWhichButtonIUse, internalFrameI, LPMain
						.getInstance().getTextRespectUISPr("lp.ort"));
		// Direktfilter gleich einbauen
		panelQueryFLRLandplzort.befuellePanelFilterkriterienDirekt(
				createFKDPlzOderOrt(), createFKDLandPlzOrtPLZ());

		// vorbesetzen falls gewuenscht
		if (selectedId != null) {
			panelQueryFLRLandplzort.setSelectedId(selectedId);
		}

		Integer i = (Integer) panelQueryFLRLandplzort.getSelectedId();
		return panelQueryFLRLandplzort;
	}

	public PanelQueryFLR createPanelFLRMediastandard(
			InternalFrame internalFrameI) throws Throwable {
		String[] aWhichButtonIUse = {};

		QueryType[] qt = null;
		FilterKriterium[] fk = createFKMandantCNr();
		PanelQueryFLR panelQueryFLRMediastandard = new PanelQueryFLR(qt, fk,
				QueryParameters.UC_ID_MEDIASTANDARD, aWhichButtonIUse,
				internalFrameI, LPMain.getInstance().getTextRespectUISPr(
						"title.medienauswahlliste"));

		panelQueryFLRMediastandard.befuellePanelFilterkriterienDirekt(
				createFKDKennung(), null);

		return panelQueryFLRMediastandard;
	}

	public PanelQueryFLR createPanelFLRParameter(InternalFrame internalFrameI,
			String selectedCNr) throws Throwable {
		String[] aWhichButtonIUse = {};

		PanelQueryFLR panelQueryFLRParameter = new PanelQueryFLR(null, null,
				QueryParameters.UC_ID_PARAMETER, aWhichButtonIUse,
				internalFrameI, LPMain.getInstance().getTextRespectUISPr(
						"lp.parameter"));

		panelQueryFLRParameter.befuellePanelFilterkriterienDirekt(
				createFKDKennung(), null);

		if (selectedCNr != null) {
			panelQueryFLRParameter.setSelectedId(selectedCNr);
		}

		return panelQueryFLRParameter;
	}

	public PanelQueryFLR createPanelFLRZahlungsziel(
			InternalFrame internalFrameI, Integer iIdZahlungsziel)
			throws Throwable {
		return createPanelFLRZahlungsziel(internalFrameI, iIdZahlungsziel,
				LPMain.getTheClient().getMandant());
	}

	public PanelQueryFLR createPanelFLRZahlungsziel(
			InternalFrame internalFrameI, Integer iIdZahlungszielI,
			String mandantCNrI) throws Throwable {
		FilterKriterium[] filters = createFKMandantCNr(mandantCNrI);
		PanelQueryFLR panelQueryFLRZahlungsziel = new PanelQueryFLR(null,
				filters, QueryParameters.UC_ID_ZAHLUNGSZIEL, null,
				internalFrameI,
				LPMain.getTextRespectUISPr("title.zahlungszielauswahlliste"));
		panelQueryFLRZahlungsziel
				.befuellePanelFilterkriterienDirektUndVersteckte(null, null,
						createFKVSpediteur());
		if (iIdZahlungszielI != null) {
			panelQueryFLRZahlungsziel.setSelectedId(iIdZahlungszielI);
		}
		return panelQueryFLRZahlungsziel;
	}

	public PanelQueryFLR createPanelFLRLieferart(InternalFrame internalFrameI,
			Integer iIdLieferartI) throws Throwable {
		return createPanelFLRLieferart(internalFrameI, iIdLieferartI, LPMain
				.getTheClient().getMandant());
	}

	public PanelQueryFLR createPanelFLRLieferart(InternalFrame internalFrameI,
			Integer iIdLieferartI, String mandantCNrI) throws Throwable {
		FilterKriterium[] filters = createFKMandantCNr(mandantCNrI);
		PanelQueryFLR panelQueryFLRLieferart = new PanelQueryFLR(null, filters,
				QueryParameters.UC_ID_LIEFERART, null, internalFrameI,
				LPMain.getTextRespectUISPr("title.lieferartauswahlliste"));
		panelQueryFLRLieferart.befuellePanelFilterkriterienDirektUndVersteckte(
				null, null, createFKVSpediteur());
		if (iIdLieferartI != null) {
			panelQueryFLRLieferart.setSelectedId(iIdLieferartI);
		}
		return panelQueryFLRLieferart;
	}

	public PanelQueryFLR createPanelFLRLand(InternalFrame internalFrameI,
			Integer iIdLandI, boolean bMitLeerenButton) throws Throwable {
		String[] aWhichButtonIUse = null;
		if (bMitLeerenButton) {
			aWhichButtonIUse = new String[] { PanelBasis.ACTION_REFRESH,
					PanelBasis.ACTION_LEEREN };
		} else {
			aWhichButtonIUse = new String[] { PanelBasis.ACTION_REFRESH };
		}

		PanelQueryFLR panelQueryFLRLand = new PanelQueryFLR(null, null,
				QueryParameters.UC_ID_LAND, aWhichButtonIUse, internalFrameI,
				LPMain.getInstance().getTextRespectUISPr("lp.land"));
		panelQueryFLRLand.befuellePanelFilterkriterienDirekt(
				SystemFilterFactory.getInstance().createFKDLandLKZ(),
				SystemFilterFactory.getInstance().createFKDLandName());
		if (iIdLandI != null) {
			panelQueryFLRLand.setSelectedId(iIdLandI);
		}
		return panelQueryFLRLand;
	}

	public PanelQueryFLR createPanelFLRSpediteur(InternalFrame internalFrameI,
			Integer iIdSpediteurI) throws Throwable {
		return createPanelFLRSpediteur(internalFrameI, iIdSpediteurI, LPMain
				.getTheClient().getMandant());
	}

	public PanelQueryFLR createPanelFLRSpediteur(InternalFrame internalFrameI,
			Integer iIdSpediteurI, String mandantCNrI) throws Throwable {
		FilterKriterium[] filters = createFKMandantCNr(mandantCNrI);
		PanelQueryFLR panelQueryFLRSpediteur = new PanelQueryFLR(null, filters,
				QueryParameters.UC_ID_SPEDITEUR, null, internalFrameI,
				LPMain.getTextRespectUISPr("title.spediteurauswahlliste"));

		panelQueryFLRSpediteur.befuellePanelFilterkriterienDirektUndVersteckte(
				null, null, createFKVSpediteur());

		if (iIdSpediteurI != null) {
			panelQueryFLRSpediteur.setSelectedId(iIdSpediteurI);
		}
		return panelQueryFLRSpediteur;
	}

	public PanelQueryFLR createPanelFLRBelegart(InternalFrame internalFrameI,
			String belegartCNr) throws Throwable {
		PanelQueryFLR panelQueryFLR = new PanelQueryFLR(null, null,
				QueryParameters.UC_ID_BELEGART, null, internalFrameI,
				LPMain.getTextRespectUISPr("lp.belegart"));
		if (belegartCNr != null) {
			panelQueryFLR.setSelectedId(belegartCNr);
		}
		return panelQueryFLR;
	}

	public PanelQueryFLR createPanelFLRMahnstufe(InternalFrame internalFrameI,
			Integer mahnstufeIId, boolean bMitLeerenButton) throws Throwable {
		
		String[] aWhichButtonIUse = null;
		if (bMitLeerenButton) {
			aWhichButtonIUse = new String[] { PanelBasis.ACTION_REFRESH,
					PanelBasis.ACTION_LEEREN };
		} else {
			aWhichButtonIUse = new String[] { PanelBasis.ACTION_REFRESH };
		}
		
		PanelQueryFLR panelQueryFLR = new PanelQueryFLR(null,SystemFilterFactory.getInstance().createFKMandantCNr(), 
				QueryParameters.UC_ID_MAHNSTUFE, aWhichButtonIUse, internalFrameI, LPMain
						.getInstance().getTextRespectUISPr(
								"rechnung.tab.oben.mahnstufe"));
		if (mahnstufeIId != null) {
			panelQueryFLR.setSelectedId(mahnstufeIId);
		}
		return panelQueryFLR;
	}

	public FilterKriteriumDirekt createFKDBetreff() throws Throwable {
		return new FilterKriteriumDirekt("c_betreff", "",
				FilterKriterium.OPERATOR_LIKE, LPMain.getInstance()
						.getTextRespectUISPr("label.betreff"),
				FilterKriteriumDirekt.PROZENT_BOTH, // Auswertung als '%XX'
				true, // wrapWithSingleQuotes
				true, Facade.MAX_UNBESCHRAENKT); // ignore case
	}
	public FilterKriteriumDirekt createFKDEmpfaenger() throws Throwable {
		return new FilterKriteriumDirekt("c_empfaenger", "",
				FilterKriterium.OPERATOR_LIKE, LPMain.getInstance()
						.getTextRespectUISPr("label.empfaenger"),
				FilterKriteriumDirekt.PROZENT_BOTH, // Auswertung als '%XX'
				true, // wrapWithSingleQuotes
				true, Facade.MAX_UNBESCHRAENKT); // ignore case
	}
	
	public PanelQueryFLR createPanelFLRKostentraeger(
			InternalFrame internalFrameI, Integer selectedId,
			boolean bMitLeerenButton) throws Throwable {
		String[] aWhichButtonIUse = null;

		if (bMitLeerenButton) {
			aWhichButtonIUse = new String[] { PanelBasis.ACTION_FILTER,
					PanelBasis.ACTION_LEEREN };
		} else {
			aWhichButtonIUse = new String[] { PanelBasis.ACTION_FILTER };
		}

		PanelQueryFLR plPartner = new PanelQueryFLR(null, SystemFilterFactory
				.getInstance().createFKMandantCNr(),
				QueryParameters.UC_ID_KOSTENTRAEGER, aWhichButtonIUse,
				internalFrameI, LPMain.getTextRespectUISPr("lp.kostentraeger"));

		if (selectedId != null) {
			plPartner.setSelectedId(selectedId);
		}

		return plPartner;
	}

}
