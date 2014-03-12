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
package com.lp.client.fertigung;

import java.util.Map;

import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQuery;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.system.SystemFilterFactory;
import com.lp.server.artikel.service.ArtikelFac;
import com.lp.server.auftrag.service.AuftragFac;
import com.lp.server.fertigung.service.FertigungFac;
import com.lp.server.fertigung.service.InternebestellungFac;
import com.lp.server.projekt.service.ProjektFac;
import com.lp.server.projekt.service.ProjektServiceFac;
import com.lp.server.stueckliste.service.StuecklisteFac;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.util.Facade;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.FilterKriteriumDirekt;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;

@SuppressWarnings("static-access")
/*
 * <p> Diese Klasse ist ein Singleton und kuemmert sich um alle Filter in der
 * Fertigung.</p>
 * 
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 * 
 * <p>Erstellung: 21. 07. 2005</p>
 * 
 * <p>@author Martin Bluehweis</p>
 * 
 * @version $Revision: 1.32 $ Date $Date: 2012/12/13 11:09:16 $
 */
public class FertigungFilterFactory {

	private static FertigungFilterFactory filterFactory = null;

	private FertigungFilterFactory() {
		// Singleton.
	}

	/**
	 * Hole das Singelton FertigungFilterFactory.
	 * 
	 * @return EingangsrechnungFilterFactory
	 */
	static public FertigungFilterFactory getInstance() {
		if (filterFactory == null) {
			filterFactory = new FertigungFilterFactory();
		}
		return filterFactory;
	}

	/**
	 * Direktes Filter Kriterium Kundenname fuer das PanelRechnungAuswahl.
	 * 
	 * @return FilterKriteriumDirekt
	 * @throws Throwable
	 */
	public FilterKriteriumDirekt createFKDArtikelnummer() throws Throwable {
		int iLaenge = ArtikelFac.MAX_ARTIKEL_ARTIKELNUMMER;
		// Eingabelaenge auf die maximale Stellenanzahl beschraenken
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
		return new FilterKriteriumDirekt("flrlos."
				+ FertigungFac.FLR_LOS_FLRSTUECKLISTE + "."
				+ StuecklisteFac.FLR_STUECKLISTE_FLRARTIKEL + ".c_nr", "",
				FilterKriterium.OPERATOR_LIKE,
				LPMain.getTextRespectUISPr("artikel.artikelnummer"),
				FilterKriteriumDirekt.PROZENT_TRAILING, // Auswertung als 'XX%'
				true, // wrapWithSingleQuotes
				true, iLaenge);
	}

	public FilterKriteriumDirekt createFKDArtikelnummerInterneBestellung()
			throws Throwable {
		int iLaenge = ArtikelFac.MAX_ARTIKEL_ARTIKELNUMMER;
		// Eingabelaenge auf die maximale Stellenanzahl beschraenken
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
		return new FilterKriteriumDirekt(FertigungFac.FLR_LOS_FLRSTUECKLISTE
				+ "." + StuecklisteFac.FLR_STUECKLISTE_FLRARTIKEL + ".c_nr",
				"", FilterKriterium.OPERATOR_LIKE,
				LPMain.getTextRespectUISPr("artikel.artikelnummer"),
				FilterKriteriumDirekt.PROZENT_TRAILING, // Auswertung als 'XX%'
				true, // wrapWithSingleQuotes
				true, iLaenge);
	}

	/**
	 * Direktes Filter Kriterium Rechnungnummer fuer das PanelQueryRechnung.
	 * 
	 * @return FilterKriteriumDirekt
	 * @throws Throwable
	 */
	public FilterKriteriumDirekt createFKDLosnummer() throws Throwable {
		return new FilterKriteriumDirekt("flrlos." + FertigungFac.FLR_LOS_C_NR,
				"", FilterKriterium.OPERATOR_LIKE, "Los Nr.",
				FilterKriteriumDirekt.PROZENT_LEADING, // Auswertung als '%XX'
				true, // wrapWithSingleQuotes
				false, Facade.MAX_UNBESCHRAENKT);
	}

	public FilterKriteriumDirekt createFKDProjektDesLoses() throws Throwable {
		return new FilterKriteriumDirekt("flrlos."
				+ FertigungFac.FLR_LOS_C_PROJEKT, "",
				FilterKriterium.OPERATOR_LIKE,
				LPMain.getTextRespectUISPr("fert.los.projekt"),
				FilterKriteriumDirekt.PROZENT_BOTH, // Auswertung als '%XX'
				true, // wrapWithSingleQuotes
				true, Facade.MAX_UNBESCHRAENKT);
	}

	public FilterKriteriumDirekt createFKDLosAuftagsnummer() throws Throwable {
		return new FilterKriteriumDirekt("flrlos."
				+ FertigungFac.FLR_LOSREPORT_FLRAUFTRAG + ".c_nr", "",
				FilterKriterium.OPERATOR_LIKE,
				LPMain.getTextRespectUISPr("label.auftragnummer"),
				FilterKriteriumDirekt.PROZENT_LEADING, // Auswertung als '%XX'
				true, // wrapWithSingleQuotes
				true, Facade.MAX_UNBESCHRAENKT);
	}

	
	public FilterKriteriumDirekt createFKDFehlmengeAufloesenLosnummer() throws Throwable {
		return new FilterKriteriumDirekt("flrlos.c_nr", "",
				FilterKriterium.OPERATOR_LIKE,
				LPMain.getTextRespectUISPr("label.losnummer"),
				FilterKriteriumDirekt.PROZENT_LEADING, // Auswertung als '%XX'
				true, // wrapWithSingleQuotes
				true, Facade.MAX_UNBESCHRAENKT);
	}
	
	public FilterKriteriumDirekt createFKDLosKunde() throws Throwable {
		return new FilterKriteriumDirekt("flrlos."
				+ FertigungFac.FLR_LOSREPORT_FLRAUFTRAG
				+ ".flrkunde.flrpartner.c_name1nachnamefirmazeile1", "",
				FilterKriterium.OPERATOR_LIKE, LPMain.getInstance()
						.getTextRespectUISPr("part.firma_nachname"),
				FilterKriteriumDirekt.PROZENT_TRAILING, // Auswertung als '%XX'
				true, // wrapWithSingleQuotes
				true, Facade.MAX_UNBESCHRAENKT); // ignore case
	}

	public FilterKriterium[] createFKSollmaterial(Integer losIId)
			throws Throwable {
		FilterKriterium[] filters = new FilterKriterium[1];
		filters[0] = new FilterKriterium(
				FertigungFac.FLR_LOSSOLLMATERIAL_LOS_I_ID, true, "'" + losIId
						+ "'", FilterKriterium.OPERATOR_EQUAL, false);
		return filters;
	}

	public FilterKriterium createFKVWiederholende() {
		FilterKriterium fkVersteckt = new FilterKriterium(
				FertigungFac.FLR_WIEDERHOLENDELOSE_B_VERSTECKT, true, "(1)", // wenn
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

	public FilterKriterium[] createFKBebuchbareLose(boolean bMitErledigten,
			boolean bMitAusgegebenen, boolean bMitAngelegten) throws Throwable {
		// Handartikel nicht anzeigen
		FilterKriterium[] kriterien = new FilterKriterium[2];

		kriterien[0] = new FilterKriterium("flrlos.mandant_c_nr", true, "'"
				+ LPMain.getTheClient().getMandant() + "'",
				FilterKriterium.OPERATOR_EQUAL, false);

		String s = "('";
		if (bMitAngelegten == false) {
			s += FertigungFac.STATUS_ANGELEGT + "','";
		}
		if (bMitAusgegebenen == false) {
			s += FertigungFac.STATUS_AUSGEGEBEN + "','";
		}
		s += FertigungFac.STATUS_GESTOPPT + "','";
		if (bMitErledigten == false) {
			s += FertigungFac.STATUS_ERLEDIGT + "','";
		}
		s += FertigungFac.STATUS_STORNIERT + "')";

		kriterien[1] = new FilterKriterium("flrlos."
				+ FertigungFac.FLR_LOS_STATUS_C_NR, true, s,
				FilterKriterium.OPERATOR_NOT_IN, false);

		return kriterien;
	}

	public FilterKriterium[] createFKLosKey(Integer artikelIId)
			throws Throwable {
		// Handartikel nicht anzeigen
		FilterKriterium[] kriterien = new FilterKriterium[1];
		kriterien[0] = new FilterKriterium("flrlos.i_id", true,
				artikelIId + "", FilterKriterium.OPERATOR_EQUAL, false);

		return kriterien;
	}

	public FilterKriterium[] createFKLosAuswahlOffene() throws Throwable {
		FilterKriterium[] kriterien = new FilterKriterium[2];

		kriterien[0] = new FilterKriterium("flrlos." + "mandant_c_nr", true,
				"'" + LPMain.getTheClient().getMandant() + "'",
				FilterKriterium.OPERATOR_EQUAL, false);

		kriterien[1] = new FilterKriterium("flrlos."
				+ FertigungFac.FLR_LOS_STATUS_C_NR, true, "('"
				+ FertigungFac.STATUS_ERLEDIGT + "','"
				+ FertigungFac.STATUS_STORNIERT + "')",
				FilterKriterium.OPERATOR_NOT_IN, false);

		return kriterien;
	}

	public FilterKriterium[] createFKLosAuswahl() throws Throwable {
		FilterKriterium[] kriterien = new FilterKriterium[1];

		kriterien[0] = new FilterKriterium("flrlos." + "mandant_c_nr", true,
				"'" + LPMain.getTheClient().getMandant() + "'",
				FilterKriterium.OPERATOR_EQUAL, false);

		return kriterien;
	}

	public PanelQueryFLR createPanelFLRBebuchbareLose(
			InternalFrame internalFrameI, boolean bMitErledigten,
			boolean bMitAusgegebenen, boolean bMitAngelegten,
			Integer selectedId, boolean bMitLeerenButton) throws Throwable {

		String[] aWhichButtonIUse = null;
		if (bMitLeerenButton) {
			aWhichButtonIUse = new String[] { PanelBasis.ACTION_LEEREN };
		}

		PanelQueryFLR panelQueryLose = new PanelQueryFLR(null,
				createFKBebuchbareLose(bMitErledigten, bMitAusgegebenen,
						bMitAngelegten), QueryParameters.UC_ID_LOS,
				aWhichButtonIUse, internalFrameI,
				LPMain.getTextRespectUISPr("lp.auswahl"));

		FilterKriteriumDirekt fkDirekt1 = FertigungFilterFactory.getInstance()
				.createFKDLosnummer();
		FilterKriteriumDirekt fkDirekt2 = null;

		ParametermandantDto parameterAuftragStattbezeichnung = (ParametermandantDto) DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getParametermandant(
						ParameterFac.PARAMETER_AUFTRAG_STATT_ARTIKEL_IN_AUSWAHLLISTE,
						ParameterFac.KATEGORIE_FERTIGUNG,
						LPMain.getTheClient().getMandant());
		if ((Boolean) parameterAuftragStattbezeichnung.getCWertAsObject()) {
			fkDirekt2 = FertigungFilterFactory.getInstance()
					.createFKDProjektDesLoses();
		} else {
			fkDirekt2 = FertigungFilterFactory.getInstance()
					.createFKDArtikelnummer();
		}
		panelQueryLose.befuellePanelFilterkriterienDirekt(fkDirekt1, fkDirekt2);
		panelQueryLose.addDirektFilter(FertigungFilterFactory.getInstance()
				.createFKDLosAuftagsnummer());
		panelQueryLose.addDirektFilter(FertigungFilterFactory.getInstance()
				.createFKDLosKunde());

		panelQueryLose.setSelectedId(selectedId);
		return panelQueryLose;
	}

	public PanelQueryFLR createPanelFLRLoseEinesTechnikers(
			InternalFrame internalFrameI, boolean bMitErledigten,
			boolean bMitAusgegebenen, boolean bMitAngelegten,
			Integer personalIIdTechniker, Integer selectedId) throws Throwable {

		PanelQueryFLR panelQueryLose = new PanelQueryFLR(null,
				createFKBebuchbareLose(bMitErledigten, bMitAusgegebenen,
						bMitAngelegten), QueryParameters.UC_ID_LOS, null,
				internalFrameI, LPMain.getTextRespectUISPr("lp.auswahl"));

		FilterKriteriumDirekt fkDirekt1 = FertigungFilterFactory.getInstance()
				.createFKDLosnummer();
		FilterKriteriumDirekt fkDirekt2 = null;

		ParametermandantDto parameterAuftragStattbezeichnung = (ParametermandantDto) DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getParametermandant(
						ParameterFac.PARAMETER_AUFTRAG_STATT_ARTIKEL_IN_AUSWAHLLISTE,
						ParameterFac.KATEGORIE_FERTIGUNG,
						LPMain.getTheClient().getMandant());
		if ((Boolean) parameterAuftragStattbezeichnung.getCWertAsObject()) {
			fkDirekt2 = FertigungFilterFactory.getInstance()
					.createFKDProjektDesLoses();
		} else {
			fkDirekt2 = FertigungFilterFactory.getInstance()
					.createFKDArtikelnummer();
		}
		panelQueryLose.befuellePanelFilterkriterienDirektUndVersteckte(
				fkDirekt1, fkDirekt2,
				createFKVLoseEinesTechnikers(personalIIdTechniker),
				LPMain.getTextRespectUISPr("fert.los.allelose"));

		panelQueryLose.addDirektFilter(FertigungFilterFactory.getInstance()
				.createFKDLosAuftagsnummer());
		panelQueryLose.addDirektFilter(FertigungFilterFactory.getInstance()
				.createFKDLosKunde());

		panelQueryLose.setSelectedId(selectedId);
		return panelQueryLose;
	}

	public FilterKriterium createFKVLoseEinesTechnikers(
			Integer personalIIdTechniker) throws Throwable {

		FilterKriterium krit1 = new FilterKriterium(
				FertigungFac.FLR_LOS_PERSONAL_I_ID_TECHNIKER, true,
				personalIIdTechniker + "", FilterKriterium.OPERATOR_EQUAL,
				false);

		return krit1;
	}

	public PanelQueryFLR createPanelFLRLose(InternalFrame internalFrameI,
			Integer artikelIId, boolean bMitLeerenButton) throws Throwable {

		String[] aWhichButtonIUse = null;
		if (bMitLeerenButton) {
			aWhichButtonIUse = new String[] { PanelBasis.ACTION_LEEREN };
		}

		FilterKriterium[] kriterien = null;

		if (artikelIId == null) {
			kriterien = new FilterKriterium[1];

			kriterien[0] = new FilterKriterium("flrlos.mandant_c_nr", true, "'"
					+ LPMain.getTheClient().getMandant() + "'",
					FilterKriterium.OPERATOR_EQUAL, false);
		} else {
			kriterien = new FilterKriterium[2];

			kriterien[0] = new FilterKriterium("flrlos.mandant_c_nr", true, "'"
					+ LPMain.getTheClient().getMandant() + "'",
					FilterKriterium.OPERATOR_EQUAL, false);
			kriterien[1] = new FilterKriterium(
					"flrlos.flrstueckliste.artikel_i_id", true, "" + artikelIId
							+ "", FilterKriterium.OPERATOR_EQUAL, false);
		}

		PanelQueryFLR panelQueryLose = new PanelQueryFLR(null, kriterien,
				QueryParameters.UC_ID_LOS, aWhichButtonIUse, internalFrameI,
				LPMain.getTextRespectUISPr("lp.auswahl"));

		FilterKriteriumDirekt fkDirekt1 = FertigungFilterFactory.getInstance()
				.createFKDLosnummer();
		FilterKriteriumDirekt fkDirekt2 = null;

		ParametermandantDto parameterAuftragStattbezeichnung = (ParametermandantDto) DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getParametermandant(
						ParameterFac.PARAMETER_AUFTRAG_STATT_ARTIKEL_IN_AUSWAHLLISTE,
						ParameterFac.KATEGORIE_FERTIGUNG,
						LPMain.getTheClient().getMandant());
		if ((Boolean) parameterAuftragStattbezeichnung.getCWertAsObject()) {
			fkDirekt2 = FertigungFilterFactory.getInstance()
					.createFKDProjektDesLoses();
		} else {
			fkDirekt2 = FertigungFilterFactory.getInstance()
					.createFKDArtikelnummer();
		}

		// PJ17681

		Map mEingeschraenkteFertigungsgruppen = DelegateFactory.getInstance()
				.getStuecklisteDelegate().getEingeschraenkteFertigungsgruppen();

		if (mEingeschraenkteFertigungsgruppen != null) {
			panelQueryLose.setFilterComboBox(mEingeschraenkteFertigungsgruppen,
					new FilterKriterium("flrlos.fertigungsgruppe_i_id", true,
							"" + "", FilterKriterium.OPERATOR_EQUAL, false),
					true);
			panelQueryLose.eventActionRefresh(null, true);
		} else {

			panelQueryLose.setFilterComboBox(DelegateFactory.getInstance()
					.getStuecklisteDelegate().getAllFertigungsgrupe(),
					new FilterKriterium("flrlos.fertigungsgruppe_i_id", true,
							"" + "", FilterKriterium.OPERATOR_EQUAL, false),
					false, LPMain.getTextRespectUISPr("lp.alle"));
		}

		panelQueryLose.befuellePanelFilterkriterienDirekt(fkDirekt1, fkDirekt2);
		panelQueryLose.addDirektFilter(FertigungFilterFactory.getInstance()
				.createFKDLosAuftagsnummer());
		panelQueryLose.addDirektFilter(FertigungFilterFactory.getInstance()
				.createFKDLosKunde());

		return panelQueryLose;
	}

	public FilterKriterium[] createFKZeitdaten(Integer losIId) throws Throwable {
		FilterKriterium[] filters = new FilterKriterium[1];
		filters[0] = new FilterKriterium(
				FertigungFac.FLR_LOSSOLLARBEITSPLAN_LOS_I_ID, true,
				losIId + "", FilterKriterium.OPERATOR_EQUAL, false);
		return filters;
	}

	public FilterKriterium[] createFKLagerentnahme(Integer losIId)
			throws Throwable {
		FilterKriterium[] filters = new FilterKriterium[1];
		filters[0] = new FilterKriterium(
				FertigungFac.FLR_LOSLAGERENTNAHME_LOS_I_ID, true, losIId + "",
				FilterKriterium.OPERATOR_EQUAL, false);
		return filters;
	}

	public FilterKriterium[] createFKLoszusazustatus(Integer losIId)
			throws Throwable {
		FilterKriterium[] filters = new FilterKriterium[1];
		filters[0] = new FilterKriterium(
				FertigungFac.FLR_LOSZUSATZSTATUS_LOS_I_ID, true, +losIId + "",
				FilterKriterium.OPERATOR_EQUAL, false);
		return filters;
	}

	public FilterKriterium[] createFKLostechniker(Integer losIId)
			throws Throwable {
		FilterKriterium[] filters = new FilterKriterium[1];
		filters[0] = new FilterKriterium(FertigungFac.FLR_LOSTECHNIKER_FLRLOS
				+ ".i_id", true, losIId + "", FilterKriterium.OPERATOR_EQUAL,
				false);
		return filters;
	}

	public FilterKriterium[] createFKLosgutschlecht(Integer losIId)
			throws Throwable {
		FilterKriterium[] filters = new FilterKriterium[1];
		filters[0] = new FilterKriterium(
				FertigungFac.FLR_LOSGUTSCHLECHT_LOSSOLLARBEITSPLAN_I_ID, true,
				losIId + "", FilterKriterium.OPERATOR_EQUAL, false);
		return filters;
	}

	public FilterKriterium[] createFKLoslosklasse(Integer losIId)
			throws Throwable {
		FilterKriterium[] filters = new FilterKriterium[1];
		filters[0] = new FilterKriterium(
				FertigungFac.FLR_LOSLOSKLASSE_LOS_I_ID, true, losIId + "",
				FilterKriterium.OPERATOR_EQUAL, false);
		return filters;
	}

	public FilterKriterium[] createFKLosablieferung(Integer losIId)
			throws Throwable {
		FilterKriterium[] filters = new FilterKriterium[1];
		filters[0] = new FilterKriterium(
				FertigungFac.FLR_LOSABLIEFERUNG_LOS_I_ID, true, losIId + "",
				FilterKriterium.OPERATOR_EQUAL, false);
		return filters;
	}

	public PanelQuery createPanelFehlmengen(InternalFrame internalFrameI,
			Integer losIId) throws Throwable {
		PanelQuery panelQueryFehlmengen = new PanelQuery(null,
				createFKFehlmengen(losIId), QueryParameters.UC_ID_FEHLMENGE,
				null, internalFrameI,
				LPMain.getTextRespectUISPr("fert.tab.oben.fehlmengen.title"),
				true);
		return panelQueryFehlmengen;
	}

	public FilterKriterium[] createFKFehlmengen(Integer losIId)
			throws Throwable {
		// Handartikel nicht anzeigen
		FilterKriterium[] kriterien = new FilterKriterium[1];

		kriterien[0] = new FilterKriterium(
				ArtikelFac.FLR_FEHLMENGE_FLRLOSSOLLMATERIAL + "."
						+ FertigungFac.FLR_LOSSOLLMATERIAL_LOS_I_ID, true, "'"
						+ losIId + "'", FilterKriterium.OPERATOR_EQUAL, false);
		return kriterien;
	}

	public PanelQueryFLR createPanelFLRLosklassen(InternalFrame internalFrameI)
			throws Throwable {
		PanelQueryFLR panelQueryLosklassen = new PanelQueryFLR(null, null,
				QueryParameters.UC_ID_LOSKLASSE, null, internalFrameI,
				LPMain.getTextRespectUISPr("lp.auswahl"));
		return panelQueryLosklassen;
	}

	public PanelQueryFLR createPanelFLRLosbereich(InternalFrame internalFrameI,
			Integer selectedId, boolean bShowLeerenButton) throws Throwable {

		String[] aWhichButtonIUse;
		if (bShowLeerenButton) {
			aWhichButtonIUse = new String[] { PanelBasis.ACTION_REFRESH,
					PanelBasis.ACTION_LEEREN };
		} else {
			aWhichButtonIUse = new String[] { PanelBasis.ACTION_REFRESH };
		}

		PanelQueryFLR panelQueryLosbereich = new PanelQueryFLR(null, null,
				QueryParameters.UC_ID_LOSBEREICH, aWhichButtonIUse,
				internalFrameI, LPMain.getTextRespectUISPr("fert.bereich"));

		panelQueryLosbereich.setSelectedId(selectedId);

		return panelQueryLosbereich;
	}

	public FilterKriterium[] createFKKriteriumNachkalkulation(Integer losIId) {
		FilterKriterium[] filter = new FilterKriterium[1];
		filter[0] = new FilterKriterium("", true, losIId.toString(),
				FilterKriterium.OPERATOR_EQUAL, false);
		return filter;
	}

	/**
	 * createPanelFLRLossollmaterial
	 * 
	 * @param internalFrame
	 *            InternalFrame
	 * @param losIId
	 *            Integer
	 * @param selectedId
	 *            Integer
	 * @param bShowLeerenButton
	 *            boolean
	 * @return PanelQueryFLR
	 * @throws Throwable
	 */
	public PanelQueryFLR createPanelFLRLossollmaterial(
			InternalFrame internalFrame, Integer losIId, Integer selectedId,
			boolean bShowLeerenButton) throws Throwable {
		String[] aWhichButtonIUse;
		if (bShowLeerenButton) {
			aWhichButtonIUse = new String[] { PanelBasis.ACTION_REFRESH,
					PanelBasis.ACTION_LEEREN };
		} else {
			aWhichButtonIUse = new String[] { PanelBasis.ACTION_REFRESH };
		}
		FilterKriterium[] fk = FertigungFilterFactory.getInstance()
				.createFKSollmaterial(losIId);

		PanelQueryFLR panelQueryFLRLossollmaterial = new PanelQueryFLR(null,
				fk, QueryParameters.UC_ID_LOSSOLLMATERIAL, aWhichButtonIUse,
				internalFrame,
				LPMain.getTextRespectUISPr("title.sollpositionliste"));

		if (selectedId != null) {
			panelQueryFLRLossollmaterial.setSelectedId(selectedId);
		}
		return panelQueryFLRLossollmaterial;
	}

	/**
	 * createFKBewegungsvorschau
	 * 
	 * @param artikelIId
	 *            Integer
	 * @return FilterKriterium[]
	 */
	public FilterKriterium[] createFKBewegungsvorschau(Integer artikelIId,
			Boolean bInterneBestellung) {
		FilterKriterium[] filters = new FilterKriterium[3];
		filters[0] = new FilterKriterium(
				InternebestellungFac.KRIT_ARTIKEL_I_ID, true,
				artikelIId.toString(), FilterKriterium.OPERATOR_LIKE, false);
		filters[1] = new FilterKriterium(
				InternebestellungFac.KRIT_INTERNEBESTELLUNG_BERUECKSICHTIGEN,
				true, Boolean.TRUE.toString(), FilterKriterium.OPERATOR_LIKE,
				false);
		if (bInterneBestellung) {
			filters[2] = new FilterKriterium(
					InternebestellungFac.KRIT_INTERNEBESTELLUNG, false,
					Boolean.TRUE.toString(), null, false);
		} else {
			filters[2] = new FilterKriterium(
					InternebestellungFac.KRIT_INTERNEBESTELLUNG, false,
					Boolean.FALSE.toString(), null, false);
		}
		return filters;
	}

	public FilterKriterium[] createFKAuftrag(Integer auftragIId)
			throws Throwable {
		FilterKriterium[] filters = new FilterKriterium[1];
		filters[0] = new FilterKriterium("flrlos."
				+ FertigungFac.FLR_LOSREPORT_FLRAUFTRAG + "."
				+ AuftragFac.FLR_AUFTRAG_I_ID, true, auftragIId + "",
				FilterKriterium.OPERATOR_EQUAL, false);
		return filters;
	}

	public PanelQueryFLR createPanelFLRLossollarbeitsplan(
			InternalFrame internalFrameI, Integer losIId,
			Integer lossollarbeitsplanIId) throws Throwable {
		String[] aWhichButtonIUse = { PanelBasis.ACTION_REFRESH };
		FilterKriterium[] krit = new FilterKriterium[1];
		krit[0] = new FilterKriterium(
				FertigungFac.FLR_LOSSOLLARBEITSPLAN_LOS_I_ID, true,
				losIId.toString() + "", FilterKriterium.OPERATOR_EQUAL, false);
		PanelQueryFLR panelQueryFLRLosposition = new PanelQueryFLR(null, krit,
				QueryParameters.UC_ID_LOSSOLLARBEITSPLAN, aWhichButtonIUse,
				internalFrameI, LPMain.getTextRespectUISPr("lp.positionen"));
		if (lossollarbeitsplanIId != null) {
			panelQueryFLRLosposition.setSelectedId(lossollarbeitsplanIId);
		}
		return panelQueryFLRLosposition;
	}
}
