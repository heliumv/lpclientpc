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
package com.lp.client.angebot;

import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.system.SystemFilterFactory;
import com.lp.client.util.fastlanereader.gui.QueryType;
import com.lp.server.angebot.service.AngebotFac;
import com.lp.server.angebot.service.AngebotServiceFac;
import com.lp.server.angebot.service.AngebotpositionFac;
import com.lp.server.auftrag.service.AuftragFac;
import com.lp.server.auftrag.service.AuftragServiceFac;
import com.lp.server.partner.service.KundeFac;
import com.lp.server.partner.service.PartnerFac;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.util.Facade;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.FilterKriteriumDirekt;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;

@SuppressWarnings("static-access")
/*
 * <p> Diese Klasse ist ein Singleton und kuemmert sich um alle Filter in der
 * Angebot.</p>
 * 
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 * 
 * <p>Erstellung: 04.07.05</p>
 * 
 * <p>@author Uli Walch</p>
 * 
 * @version $Revision: 1.9 $ Date $Date: 2013/01/17 09:02:25 $
 */
public class AngebotFilterFactory {
	private static AngebotFilterFactory filterFactory = null;

	private AngebotFilterFactory() {
		// Singleton.
	}

	/**
	 * Hole das Singelton AngebotFilterFactory.
	 * 
	 * @return LPMain
	 */
	static public AngebotFilterFactory getInstance() {
		if (filterFactory == null) {
			filterFactory = new AngebotFilterFactory();
		}

		return filterFactory;
	}

	/**
	 * Der PK der aktuellen Angebot wird als Filter verwendet.
	 * 
	 * @param iIdAngebotI
	 *            PK der Angebot
	 * @return FilterKriterium[]
	 */
	protected FilterKriterium[] createFKAngebotiid(Integer iIdAngebotI) {
		FilterKriterium[] krit = null;

		if (iIdAngebotI != null) {
			krit = new FilterKriterium[1];

			krit[0] = new FilterKriterium("i_id", true, iIdAngebotI.toString(),
					FilterKriterium.OPERATOR_EQUAL, false);
		}

		return krit;
	}

	/**
	 * Direktes Filter Kriterium TextSuche.
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
	 * 1:n Listen in der Angebot wie Angebotpositionen werden per default nach
	 * der referenzierten Angebot gefiltert.
	 * 
	 * @param iIdAngebotI
	 *            PK der Angebot
	 * @return FilterKriterium[]
	 */
	protected FilterKriterium[] createFKFlrangebotiid(Integer iIdAngebotI) {
		FilterKriterium[] aFilterKrit = null;

		if (iIdAngebotI != null) {
			aFilterKrit = new FilterKriterium[1];

			FilterKriterium krit1 = new FilterKriterium(
					AngebotpositionFac.FLR_ANGEBOTPOSITION_FLRANGEBOT + ".i_id",
					true, iIdAngebotI.toString(),
					FilterKriterium.OPERATOR_EQUAL, false);

			aFilterKrit[0] = krit1;
		}

		return aFilterKrit;
	}

	/**
	 * Default Filter Kriterien fuer eine Liste von Angebottexten.
	 * 
	 * @return FilterKriterium[] Default Filter Kriterien
	 * @throws Throwable
	 *             Ausnahme
	 */
	public FilterKriterium[] createFKAngebottext() throws Throwable {
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

	public FilterKriteriumDirekt createFKDAngebotnummer() throws Throwable {
		return new FilterKriteriumDirekt("c_nr", "",
				FilterKriterium.OPERATOR_LIKE, LPMain.getInstance()
						.getTextRespectUISPr("angb.angebotsnummershort"),
				FilterKriteriumDirekt.PROZENT_LEADING, // Auswertung als '%XX'
				true, // wrapWithSingleQuotes
				false, Facade.MAX_UNBESCHRAENKT); // ignore case
	}

	public FilterKriteriumDirekt createFKDProjekt() throws Throwable {
		return new FilterKriteriumDirekt("c_bez", "",
				FilterKriterium.OPERATOR_LIKE, LPMain.getInstance()
						.getTextRespectUISPr("ang.filter.projanf"),
				FilterKriteriumDirekt.PROZENT_TRAILING, // Auswertung als '%XX'
				true, // wrapWithSingleQuotes
				true, // ignore case
				Facade.MAX_UNBESCHRAENKT);
	}

	public FilterKriteriumDirekt createFKDKundeName() throws Throwable {

		ParametermandantDto parameter = (ParametermandantDto) DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getParametermandant(
						ParameterFac.PARAMETER_PARTNERSUCHE_WILDCARD_BEIDSEITIG,
						ParameterFac.KATEGORIE_PARTNER,
						LPMain.getInstance().getTheClient().getMandant());
		if (((Boolean) parameter.getCWertAsObject() == true)) {
			FilterKriteriumDirekt fkDirekt1 = new FilterKriteriumDirekt(
					AngebotFac.FLR_ANGEBOT_FLRKUNDE + "."
							+ KundeFac.FLR_PARTNER + "."
							+ PartnerFac.FLR_PARTNER_NAME1NACHNAMEFIRMAZEILE1,
					"", FilterKriterium.OPERATOR_LIKE, LPMain.getInstance()
							.getTextRespectUISPr("lp.kunde.modulname"),
					FilterKriteriumDirekt.PROZENT_BOTH, // Auswertung als
															// 'XX%'
					true, // wrapWithSingleQuotes
					true, Facade.MAX_UNBESCHRAENKT); // ignore case

			return fkDirekt1;
		} else {
			FilterKriteriumDirekt fkDirekt1 = new FilterKriteriumDirekt(
					AngebotFac.FLR_ANGEBOT_FLRKUNDE + "."
							+ KundeFac.FLR_PARTNER + "."
							+ PartnerFac.FLR_PARTNER_NAME1NACHNAMEFIRMAZEILE1,
					"", FilterKriterium.OPERATOR_LIKE, LPMain.getInstance()
							.getTextRespectUISPr("lp.kunde.modulname"),
					FilterKriteriumDirekt.PROZENT_TRAILING, // Auswertung als
															// 'XX%'
					true, // wrapWithSingleQuotes
					true, Facade.MAX_UNBESCHRAENKT); // ignore case

			return fkDirekt1;
		}

	}

	/**
	 * Die UI FilterKriterien am Panel Angebotauswahl. <br>
	 * - Auswahl nach Angebotnummer <br>
	 * - Auswahl nach Kundenname <br>
	 * - Auswahl nach Belegdatum <br>
	 * - Auswahl nach Projektbezeichnung
	 * 
	 * @return QueryType[]
	 */
	protected QueryType[] createQTPanelAngebotauswahl() {
		QueryType[] types = new QueryType[4];

		FilterKriterium f1 = new FilterKriterium("c_nr", true, "",
				FilterKriterium.OPERATOR_LIKE, false);

		types[0] = new QueryType(LPMain.getInstance().getTextRespectUISPr(
				"angb.angebotsnummer"), f1,
				new String[] { FilterKriterium.OPERATOR_EQUAL }, true, true);

		FilterKriterium f2 = new FilterKriterium(
				AngebotFac.FLR_ANGEBOT_FLRKUNDE + "." + KundeFac.FLR_PARTNER
						+ "." + PartnerFac.FLR_PARTNER_NAME1NACHNAMEFIRMAZEILE1,
				true, "", FilterKriterium.OPERATOR_LIKE, true);

		types[1] = new QueryType(LPMain.getInstance().getTextRespectUISPr(
				"lp.kunde.modulname"), f2,
				new String[] { FilterKriterium.OPERATOR_EQUAL }, true, true);

		FilterKriterium f3 = new FilterKriterium(
				AngebotFac.FLR_ANGEBOT_T_BELEGDATUM, true, "",
				FilterKriterium.OPERATOR_EQUAL, false);

		types[2] = new QueryType(LPMain.getInstance().getTextRespectUISPr(
				"lp.belegdatum"), f3, new String[] {
				FilterKriterium.OPERATOR_EQUAL, FilterKriterium.OPERATOR_GTE,
				FilterKriterium.OPERATOR_LTE }, true, false, false);

		FilterKriterium f4 = new FilterKriterium("c_bez", true, "",
				FilterKriterium.OPERATOR_LIKE, true);

		types[3] = new QueryType(LPMain.getInstance().getTextRespectUISPr(
				"label.projekt"), f4,
				new String[] { FilterKriterium.OPERATOR_EQUAL }, true, true);

		return types;
	}

	/**
	 * Angebotauswahlliste mit Direktfilter. <br>
	 * Der Filter zur Vorselektion kann von aussen gesetzt werden. <br>
	 * Per default wird nach dem aktuellen Mandanten gefiltert.
	 * 
	 * @param internalFrameI
	 *            InternalFrame
	 * @param bShowFilterButton
	 *            den New Button anzeigen
	 * @param bShowLeerenButton
	 *            den Leeren Button anzeigen
	 * @param fkI
	 *            der Filter
	 * @return PanelQueryFLR die Auswahlliste
	 * @throws Throwable
	 *             Ausnahme
	 */
	public PanelQueryFLR createPanelFLRAngebot(InternalFrame internalFrameI,
			boolean bShowFilterButton, boolean bShowLeerenButton,
			FilterKriterium[] fkI) throws Throwable {
		PanelQueryFLR panelQueryFLRAngebot = createPanelFLRAngebotOhneDirektFilter(
				internalFrameI, bShowFilterButton, bShowLeerenButton, fkI);

		panelQueryFLRAngebot.befuellePanelFilterkriterienDirekt(
				AngebotFilterFactory.getInstance().createFKDAngebotnummer(),
				AngebotFilterFactory.getInstance().createFKDKundeName());

		return panelQueryFLRAngebot;
	}

	public PanelQueryFLR createPanelFLRAngebotErledigteVersteckt(
			InternalFrame internalFrameI, boolean bShowFilterButton,
			boolean bShowLeerenButton) throws Throwable {
		PanelQueryFLR panelQueryFLRAngebot = createPanelFLRAngebotOhneDirektFilter(
				internalFrameI, bShowFilterButton, bShowLeerenButton, null);

		panelQueryFLRAngebot.befuellePanelFilterkriterienDirektUndVersteckte(
				createFKDAngebotnummer(), createFKDKundeName(),
				createFKVAngebotOffene(), LPMain.getInstance()
						.getTextRespectUISPr("angb.print.alle"));

		return panelQueryFLRAngebot;
	}

	private PanelQueryFLR createPanelFLRAngebotOhneDirektFilter(
			InternalFrame internalFrameI, boolean bShowFilterButton,
			boolean bShowLeerenButton, FilterKriterium[] fkI) throws Throwable {
		String[] aWhichButtonIUse = SystemFilterFactory.getInstance()
				.createButtonArray(bShowFilterButton, bShowLeerenButton);

		FilterKriterium[] fk = SystemFilterFactory.getInstance()
				.createFKMandantCNr();

		if (fkI != null) {
			fk = fkI;
		}

		PanelQueryFLR panelQueryFLRAngebot = new PanelQueryFLR(
				AngebotFilterFactory.getInstance()
						.createQTPanelAngebotauswahl(), fk,
				QueryParameters.UC_ID_ANGEBOT, aWhichButtonIUse,
				internalFrameI, LPMain.getInstance().getTextRespectUISPr(
						"title.angebotauswahlliste"));

		return panelQueryFLRAngebot;
	}

	/**
	 * Angebotauswahlliste mit Direktfilter. <br>
	 * Der Filter zur Vorselektion kann von aussen gesetzt werden. <br>
	 * Per default wird nach dem aktuellen Mandanten gefiltert.
	 * 
	 * @param internalFrameI
	 *            InternalFrame
	 * @param bShowFilterButton
	 *            den New Button anzeigen
	 * @param bShowLeerenButton
	 *            den Leeren Button anzeigen
	 * @param fkI
	 *            der Filter
	 * @param selectedIIdI
	 *            die iId des in der Liste selektierten Angebots
	 * @return PanelQueryFLR die Auswahlliste
	 * @throws Throwable
	 *             Ausnahme
	 */
	public PanelQueryFLR createPanelFLRAngebot(InternalFrame internalFrameI,
			boolean bShowFilterButton, boolean bShowLeerenButton,
			FilterKriterium[] fkI, Integer selectedIIdI) throws Throwable {
		PanelQueryFLR panelQueryFLRAngebot = createPanelFLRAngebot(
				internalFrameI, bShowFilterButton, bShowLeerenButton, fkI);

		if (selectedIIdI != null) {
			panelQueryFLRAngebot.setSelectedId(selectedIIdI);
		}

		return panelQueryFLRAngebot;
	}

	public FilterKriterium[] createFKAngeboterledigungsgrund() throws Throwable {
		FilterKriterium[] krit = new FilterKriterium[2];

		krit[0] = new FilterKriterium("c_nr", true, "('"
				+ AngebotServiceFac.ANGEBOTERLEDIGUNGSGRUND_AUFTRAGERHALTEN
				+ "')", FilterKriterium.OPERATOR_NOT_IN, false);
		krit[1] = SystemFilterFactory.getInstance().createFKMandantCNr()[0];
		return krit;
	}

	/**
	 * Auswahlliste fuer alle wartbaren Erledigungsgruende.
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
	public PanelQueryFLR createPanelFLRAngeboterledigungsgrund(
			InternalFrame internalFrameI, boolean bShowFilterButton,
			boolean bShowLeerenButton) throws Throwable {
		String[] aWhichButtonIUse = SystemFilterFactory.getInstance()
				.createButtonArray(bShowFilterButton, bShowLeerenButton);

		FilterKriterium[] fk = null;

		PanelQueryFLR panelQueryFLRAngeboterledigungsgrund = new PanelQueryFLR(
				null, AngebotFilterFactory.getInstance()
						.createFKAngeboterledigungsgrund(),
				QueryParameters.UC_ID_ANGEBOTERLEDIGUNGSGRUND,
				aWhichButtonIUse, internalFrameI, LPMain.getInstance()
						.getTextRespectUISPr(
								"title.auswahllisteangeboterledigungsgrund"));

		return panelQueryFLRAngeboterledigungsgrund;
	}

	/**
	 * FilterKriterien fuer alle offenen Angebote eines Mandanten.
	 * 
	 * @return FilterKriterium[] die FilterKriterien
	 * @throws Throwable
	 *             Ausnahme
	 */
	public FilterKriterium[] createFKAngebotOffene() throws Throwable {
		FilterKriterium[] fk = new FilterKriterium[2];

		fk[0] = SystemFilterFactory.getInstance().createFKMandantCNr()[0];
		fk[1] = new FilterKriterium(AngebotFac.FLR_ANGEBOT_ANGEBOTSTATUS_C_NR,
				true, "'" + AngebotServiceFac.ANGEBOTSTATUS_OFFEN + "'",
				FilterKriterium.OPERATOR_EQUAL, false);

		return fk;
	}

	/**
	 * FilterKriterien fuer alle offenen Angebote eines Mandanten.
	 * 
	 * @return FilterKriterium[] die FilterKriterien
	 * @throws Throwable
	 *             Ausnahme
	 */
	public FilterKriterium[] createFKAngebotNichtAngelegt() throws Throwable {
		FilterKriterium[] fk = new FilterKriterium[2];

		fk[0] = SystemFilterFactory.getInstance().createFKMandantCNr()[0];
		fk[1] = new FilterKriterium(AngebotFac.FLR_ANGEBOT_ANGEBOTSTATUS_C_NR,
				true, "'" + AngebotServiceFac.ANGEBOTSTATUS_ANGELEGT + "'",
				FilterKriterium.OPERATOR_NOT_EQUAL, false);

		return fk;
	}

	public FilterKriterium createFKVAngebotOffene() {
		FilterKriterium fkVersteckt = new FilterKriterium(
				AngebotFac.FLR_ANGEBOT_ANGEBOTSTATUS_C_NR, true, "'"
						+ AngebotServiceFac.ANGEBOTSTATUS_OFFEN + "'",
				FilterKriterium.OPERATOR_EQUAL, false);

		return fkVersteckt;
	}

	/**
	 * FilterKriterien fuer alle erledigten Angebote eines Mandanten.
	 * 
	 * @return FilterKriterium[] die FilterKriterien
	 * @throws Throwable
	 *             Ausnahme
	 */
	public FilterKriterium[] createFKAngebotErledigt() throws Throwable {
		FilterKriterium[] fk = new FilterKriterium[2];

		fk[0] = SystemFilterFactory.getInstance().createFKMandantCNr()[0];
		fk[1] = new FilterKriterium(AngebotFac.FLR_ANGEBOT_ANGEBOTSTATUS_C_NR,
				true, "'" + AngebotServiceFac.ANGEBOTSTATUS_ERLEDIGT + "'",
				FilterKriterium.OPERATOR_EQUAL, false);

		return fk;
	}

	/**
	 * FilterKriterien fuer alle angelegten Angebote eines Mandanten.
	 * 
	 * @return FilterKriterium[] die FilterKriterien
	 * @throws Throwable
	 *             Ausnahme
	 */
	public FilterKriterium[] createFKAngebotAngelegte() throws Throwable {
		FilterKriterium[] fk = new FilterKriterium[2];

		fk[0] = SystemFilterFactory.getInstance().createFKMandantCNr()[0];
		fk[1] = new FilterKriterium(AngebotFac.FLR_ANGEBOT_ANGEBOTSTATUS_C_NR,
				true, "'" + AngebotServiceFac.ANGEBOTSTATUS_ANGELEGT + "'",
				FilterKriterium.OPERATOR_EQUAL, false);

		return fk;
	}

	/**
	 * FilterKriterien fuer alle offenen Angebote eines Vertreters eines
	 * Mandanten.
	 * 
	 * @param iIdPersonalI
	 *            PK des Vertreters
	 * @return FilterKriterium[] die FilterKriterien
	 * @throws Throwable
	 *             Ausnahme
	 */
	public FilterKriterium[] createFKAngebotOffeneVertreter(Integer iIdPersonalI)
			throws Throwable {
		FilterKriterium[] fk = new FilterKriterium[3];

		fk[0] = SystemFilterFactory.getInstance().createFKMandantCNr()[0];
		fk[1] = new FilterKriterium(AngebotFac.FLR_ANGEBOT_ANGEBOTSTATUS_C_NR,
				true, "'" + AngebotServiceFac.ANGEBOTSTATUS_OFFEN + "'",
				FilterKriterium.OPERATOR_EQUAL, false);
		fk[2] = new FilterKriterium(AngebotFac.FLR_ANGEBOT_VERTRETER_I_ID,
				true, iIdPersonalI.toString(), FilterKriterium.OPERATOR_EQUAL,
				false);

		return fk;
	}

	/**
	 * FilterKriterien fuer alle angelegten Angebote eines Vertreters eines
	 * Mandanten.
	 * 
	 * @param iIdPersonalI
	 *            PK des Vertreters
	 * @return FilterKriterium[] die FilterKriterien
	 * @throws Throwable
	 *             Ausnahme
	 */
	public FilterKriterium[] createFKAngebotAngelegteVertreter(
			Integer iIdPersonalI) throws Throwable {
		FilterKriterium[] fk = new FilterKriterium[3];

		fk[0] = SystemFilterFactory.getInstance().createFKMandantCNr()[0];
		fk[1] = new FilterKriterium(AngebotFac.FLR_ANGEBOT_ANGEBOTSTATUS_C_NR,
				true, "'" + AngebotServiceFac.ANGEBOTSTATUS_ANGELEGT + "'",
				FilterKriterium.OPERATOR_EQUAL, false);
		fk[2] = new FilterKriterium(AngebotFac.FLR_ANGEBOT_VERTRETER_I_ID,
				true, iIdPersonalI.toString(), FilterKriterium.OPERATOR_EQUAL,
				false);

		return fk;
	}

	public FilterKriterium[] createFKSchnellansicht() throws Throwable {
		FilterKriterium[] filters = new FilterKriterium[1];
		filters[0] = new FilterKriterium(
				AngebotFac.FLR_ANGEBOT_ANGEBOTSTATUS_C_NR, true, "('"
						+ AngebotServiceFac.ANGEBOTSTATUS_STORNIERT + "','"
						+ AngebotServiceFac.ANGEBOTSTATUS_ERLEDIGT + "')",
				FilterKriterium.OPERATOR_NOT_IN, false);

		return filters;
	}
}
