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
package com.lp.client.auftrag;

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
import com.lp.server.eingangsrechnung.service.EingangsrechnungFac;
import com.lp.server.lieferschein.service.LieferscheinFac;
import com.lp.server.lieferschein.service.LieferscheinpositionFac;
import com.lp.server.partner.service.KundeFac;
import com.lp.server.partner.service.PartnerFac;
import com.lp.server.rechnung.service.RechnungFac;
import com.lp.server.system.service.PanelFac;
import com.lp.server.system.service.PanelbeschreibungDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.util.Facade;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.FilterKriteriumDirekt;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;

@SuppressWarnings("static-access")
/*
 * <p> Diese Klasse ist ein Singleton und kuemmert sich um alle Filter im
 * Auftrag.</p>
 * 
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 * 
 * <p>Erstellung: 26. 04. 2005</p>
 * 
 * <p>@author Uli Walch</p>
 * 
 * @version $Revision: 1.15 $ Date $Date: 2012/11/29 15:24:06 $
 */
public class AuftragFilterFactory {
	private static AuftragFilterFactory filterFactory = null;

	private AuftragFilterFactory() {
		// Singleton.
	}

	/**
	 * Hole das Singelton AuftragFilterFactory.
	 * 
	 * @return LPMain
	 */
	static public AuftragFilterFactory getInstance() {
		if (filterFactory == null) {
			filterFactory = new AuftragFilterFactory();
		}

		return filterFactory;
	}

	/**
	 * Direktes Filter Kriterium Auftragnummer fuer das PanelAuftragAuswahl.
	 * 
	 * @return FilterKriteriumDirekt
	 * @throws Throwable
	 */
	public FilterKriteriumDirekt createFKDAuftragnummer() throws Throwable {
		return new FilterKriteriumDirekt("c_nr", "",
				FilterKriterium.OPERATOR_LIKE, LPMain.getInstance()
						.getTextRespectUISPr("label.auftragnummershort"),
				FilterKriteriumDirekt.PROZENT_LEADING, // Auswertung als '%XX'
				true, // wrapWithSingleQuotes
				false, Facade.MAX_UNBESCHRAENKT); // ignore case
	}

	public FilterKriteriumDirekt createFKDAuftragdokumentKennung()
			throws Throwable {
		return new FilterKriteriumDirekt("c_nr", "",
				FilterKriterium.OPERATOR_LIKE, LPMain.getInstance()
						.getTextRespectUISPr("label.kennung"),
				FilterKriteriumDirekt.PROZENT_LEADING, // Auswertung als '%XX'
				true, // wrapWithSingleQuotes
				false, Facade.MAX_UNBESCHRAENKT); // ignore case
	}

	public FilterKriterium createFKVAuftragdokument() {
		FilterKriterium fkVersteckt = new FilterKriterium("b_versteckt", true,
				"(1)", // wenn

				FilterKriterium.OPERATOR_NOT_IN, false);

		return fkVersteckt;
	}

	public FilterKriterium createFKVAuftrag() {
		FilterKriterium fkVersteckt = new FilterKriterium(""
				+ AuftragFac.FLR_AUFTRAG_B_VERSTECKT, true, "(1)", // wenn
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

	public FilterKriterium[] createFKAuftragKey(Integer artikelIId)
			throws Throwable {
		// Handartikel nicht anzeigen
		FilterKriterium[] kriterien = new FilterKriterium[1];
		kriterien[0] = new FilterKriterium("i_id", true, artikelIId + "",
				FilterKriterium.OPERATOR_EQUAL, false);

		return kriterien;
	}

	/**
	 * Direktes Filter Kriterium Kundenname fuer das PanelAuftragAuswahl.
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
			return new FilterKriteriumDirekt(AuftragFac.FLR_AUFTRAG_FLRKUNDE
					+ "." + KundeFac.FLR_PARTNER + "."
					+ PartnerFac.FLR_PARTNER_NAME1NACHNAMEFIRMAZEILE1, "",
					FilterKriterium.OPERATOR_LIKE, LPMain.getInstance()
							.getTextRespectUISPr("label.kunde"),
					FilterKriteriumDirekt.PROZENT_BOTH, // Auswertung als 'XX%'
					true, // wrapWithSingleQuotes
					true, Facade.MAX_UNBESCHRAENKT); // ignore case
		} else {
			return new FilterKriteriumDirekt(AuftragFac.FLR_AUFTRAG_FLRKUNDE
					+ "." + KundeFac.FLR_PARTNER + "."
					+ PartnerFac.FLR_PARTNER_NAME1NACHNAMEFIRMAZEILE1, "",
					FilterKriterium.OPERATOR_LIKE, LPMain.getInstance()
							.getTextRespectUISPr("label.kunde"),
					FilterKriteriumDirekt.PROZENT_TRAILING, // Auswertung als
															// 'XX%'
					true, // wrapWithSingleQuotes
					true, Facade.MAX_UNBESCHRAENKT); // ignore case
		}
	}

	public FilterKriterium[] createFKAuftragAuswahlOffene() throws Throwable {
		FilterKriterium[] kriterien = new FilterKriterium[2];

		kriterien[0] = new FilterKriterium("mandant_c_nr", true, "'"
				+ LPMain.getInstance().getTheClient().getMandant() + "'",
				FilterKriterium.OPERATOR_EQUAL, false);

		kriterien[1] = new FilterKriterium(
				AuftragFac.FLR_AUFTRAG_AUFTRAGSTATUS_C_NR, true, "('"
						+ AuftragServiceFac.AUFTRAGSTATUS_ERLEDIGT + "','"
						+ AuftragServiceFac.AUFTRAGSTATUS_STORNIERT + "')",
				FilterKriterium.OPERATOR_NOT_IN, false);

		return kriterien;
	}

	public FilterKriteriumDirekt createFKDProjekt() throws Throwable {
		return new FilterKriteriumDirekt("c_bez", "",
				FilterKriterium.OPERATOR_LIKE, LPMain.getInstance()
						.getTextRespectUISPr("label.projektbestellnr"),
				FilterKriteriumDirekt.PROZENT_BOTH, // Auswertung als '%XX'
				true, // wrapWithSingleQuotes
				true, // ignore case
				Facade.MAX_UNBESCHRAENKT);
	}

	public FilterKriteriumDirekt createFKDBelegLSRE() throws Throwable {
		return new FilterKriteriumDirekt(RechnungFac.FLR_RECHNUNG_C_NR, "",
				FilterKriterium.OPERATOR_LIKE, "Belegnummer",
				FilterKriteriumDirekt.PROZENT_LEADING, // Auswertung als '%XX'
				true, // wrapWithSingleQuotes
				false, Facade.MAX_UNBESCHRAENKT);
	}

	/**
	 * Der PK des aktuellen Auftrags wird als Filter verwendet.
	 * 
	 * @param iIdAuftragI
	 *            PK des Auftrags
	 * @return FilterKriterium[]
	 */
	protected FilterKriterium[] createFKAuftragiid(Integer iIdAuftragI) {
		FilterKriterium[] krit = null;

		if (iIdAuftragI != null) {
			krit = new FilterKriterium[1];

			krit[0] = new FilterKriterium(AuftragFac.FLR_AUFTRAG_I_ID, true,
					iIdAuftragI.toString(), FilterKriterium.OPERATOR_EQUAL,
					false);
		}

		return krit;
	}

	public FilterKriterium[] createFKAuftragEingansrechnungen(
			Integer iIdAuftragI) {
		FilterKriterium[] krit = null;

		if (iIdAuftragI != null) {
			krit = new FilterKriterium[1];

			krit[0] = new FilterKriterium(EingangsrechnungFac.FLR_AZ_FLRAUFTRAG
					+ ".i_id", true, iIdAuftragI.toString(),
					FilterKriterium.OPERATOR_EQUAL, false);
		}

		return krit;
	}

	/**
	 * Der PK des aktuellen Auftrags wird als Filter verwendet.
	 * 
	 * @param iIdAuftragpositionI
	 *            PK des Auftrags
	 * @return FilterKriterium[]
	 */
	public FilterKriterium[] createFKAuftragpositioniid(
			Integer iIdAuftragpositionI) {
		FilterKriterium[] krit = null;

		if (iIdAuftragpositionI != null) {
			krit = new FilterKriterium[1];

			krit[0] = new FilterKriterium("auftragposition_i_id", true,
					iIdAuftragpositionI.toString(),
					FilterKriterium.OPERATOR_EQUAL, false);
		}

		return krit;
	}

	/**
	 * 1:n Listen im Auftrag wie Auftragpositionen oder Auftragteilnehmer werden
	 * per default nach dem referenzierten Auftrag gefiltert.
	 * 
	 * @param iIdAuftragI
	 *            PK des Auftrags
	 * @return FilterKriterium[]
	 */
	public FilterKriterium[] createFKFlrauftragiid(Integer iIdAuftragI) {
		FilterKriterium[] aFilterKrit = null;

		if (iIdAuftragI != null) {
			aFilterKrit = new FilterKriterium[1];

			aFilterKrit[0] = new FilterKriterium(
					AuftragpositionFac.FLR_AUFTRAGPOSITION_FLRAUFTRAG + "."
							+ AuftragFac.FLR_AUFTRAG_I_ID, true,
					iIdAuftragI.toString(), FilterKriterium.OPERATOR_EQUAL,
					false);
		}

		return aFilterKrit;
	}
	public FilterKriterium[] createFKZahlungsplanmeilenstein(Integer zahlungsplanIId) {
		FilterKriterium[] aFilterKrit = null;

		
			aFilterKrit = new FilterKriterium[1];

			aFilterKrit[0] = new FilterKriterium("zahlungsplan_i_id", true,
					zahlungsplanIId.toString(), FilterKriterium.OPERATOR_EQUAL,
					false);
		

		return aFilterKrit;
	}

	public FilterKriterium[] createFKOhneStornierten(Integer projektIId)
			throws Throwable {

		if (projektIId == null) {
			FilterKriterium[] krit1 = null;
			krit1 = new FilterKriterium[2];
			krit1[0] = new FilterKriterium("mandant_c_nr", true, "'"
					+ LPMain.getInstance().getTheClient().getMandant() + "'",
					FilterKriterium.OPERATOR_EQUAL, false);
			krit1[1] = new FilterKriterium(
					AuftragFac.FLR_AUFTRAG_AUFTRAGSTATUS_C_NR, true, "'"
							+ AuftragServiceFac.AUFTRAGSTATUS_STORNIERT + "'",
					FilterKriterium.OPERATOR_NOT_EQUAL, false);
			return krit1;
		} else {
			FilterKriterium[] krit1 = null;
			krit1 = new FilterKriterium[3];
			krit1[0] = new FilterKriterium("mandant_c_nr", true, "'"
					+ LPMain.getInstance().getTheClient().getMandant() + "'",
					FilterKriterium.OPERATOR_EQUAL, false);
			krit1[1] = new FilterKriterium(
					AuftragFac.FLR_AUFTRAG_AUFTRAGSTATUS_C_NR, true, "'"
							+ AuftragServiceFac.AUFTRAGSTATUS_STORNIERT + "'",
					FilterKriterium.OPERATOR_NOT_EQUAL, false);
			
			krit1[2] = new FilterKriterium("projekt_i_id", true, projektIId+"",
					FilterKriterium.OPERATOR_EQUAL, false);
			return krit1;
		}
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
	 * Default Filter fuer 1:n Panel Sicht der Auftragposition auf die
	 * liefernden Lieferscheine.
	 * 
	 * @param iIdAuftragpositionI
	 *            PK der Auftragposition
	 * @return FilterKriterium[]
	 */
	protected FilterKriterium[] createFKSichtAuftragpositionAufLieferscheine(
			Integer iIdAuftragpositionI) {
		FilterKriterium[] kriterien = null;

		if (iIdAuftragpositionI != null) {
			kriterien = new FilterKriterium[2];

			kriterien[0] = new FilterKriterium(
					LieferscheinpositionFac.FLR_LIEFERSCHEINPOSITION_FLRPOSITIONENSICHTAUFTRAG
							+ "."
							+ AuftragpositionFac.FLR_AUFTRAGPOSITIONSICHTAUFTRAG_I_ID,
					true, iIdAuftragpositionI.toString(),
					FilterKriterium.OPERATOR_EQUAL, false);

			kriterien[1] = new FilterKriterium(
					LieferscheinpositionFac.FLR_LIEFERSCHEINPOSITION_FLRLIEFERSCHEIN
							+ "."
							+ LieferscheinFac.FLR_LIEFERSCHEIN_LIEFERSCHEINSTATUS_STATUS_C_NR,
					true, "'" + LieferscheinFac.LSSTATUS_STORNIERT + "'",
					FilterKriterium.OPERATOR_NOT_EQUAL, false);
		}

		return kriterien;
	}

	/**
	 * Die UI FilterKriterien am Panel Auftragauswahl. <br>
	 * - Auswahl nach Belegdatum <br>
	 * - Auswahl nach Bestellnummer <br>
	 * - Auswahl nach Eigenschaften
	 * 
	 * @return QueryType[]
	 */
	public QueryType[] createQTPanelAuftragAuswahl() {
		QueryType[] types = new QueryType[0];
		boolean bEigenschaftenVorhanden = false;
		try {
			PanelbeschreibungDto[] dtos = DelegateFactory
					.getInstance()
					.getPanelDelegate()
					.panelbeschreibungFindByPanelCNrMandantCNr(
							PanelFac.PANEL_AUFTRAGSEIGENSCHAFTEN, null);
			if (dtos != null && dtos.length > 0) {
				// Comment out till implemetation of search finished
				bEigenschaftenVorhanden = true;
			}
		} catch (Throwable ex) {
			// nothing to do here.. we may got no Eigenschaften
		}
		if (bEigenschaftenVorhanden) {
			types = new QueryType[3];
		} else {
			types = new QueryType[2];
		}
		FilterKriterium f1 = new FilterKriterium(
				AuftragFac.FLR_AUFTRAG_D_BELEGDATUM, true, "",
				FilterKriterium.OPERATOR_EQUAL, false);

		types[0] = new QueryType(LPMain.getInstance().getTextRespectUISPr(
				"label.belegdatum"), f1, new String[] {
				FilterKriterium.OPERATOR_EQUAL, FilterKriterium.OPERATOR_GTE,
				FilterKriterium.OPERATOR_LTE }, true, // flrdate: eingabeformat
				// 10.12.2004
				false, false);

		FilterKriterium f2 = new FilterKriterium(
				AuftragFac.FLR_AUFTRAG_C_BESTELLNUMMER, true, "",
				FilterKriterium.OPERATOR_LIKE, true);

		types[1] = new QueryType(LPMain.getInstance().getTextRespectUISPr(
				"label.bestellnummer"), f2,
				new String[] { FilterKriterium.OPERATOR_EQUAL }, true, true);

		// Wenn Eigenschaften vorhanden auch nach denen suchen lassen
		if (bEigenschaftenVorhanden) {
			FilterKriterium f3 = new FilterKriterium("flrpaneldatenckey."
					+ PanelFac.FLR_PANELDATEN_X_INHALT, true, "",
					FilterKriterium.OPERATOR_LIKE, true);

			types[2] = new QueryType(LPMain.getInstance().getTextRespectUISPr(
					"lp.eigenschaften"), f3,
					new String[] { FilterKriterium.OPERATOR_EQUAL }, true, true);
		}
		return types;
	}

	/**
	 * FilterKriterien fuer die umsatzrelevanten Positionen eines Auftrags.
	 * 
	 * @param iIdAuftragI
	 *            PK des Auftrags
	 * @return FilterKriterium[]
	 */
	public FilterKriterium[] createFKUmsatzrelevantePositionen(
			Integer iIdAuftragI) {
		FilterKriterium[] kriterien = new FilterKriterium[2];

		FilterKriterium krit1 = new FilterKriterium(
				AuftragpositionFac.FLR_AUFTRAGPOSITION_FLRAUFTRAG + ".i_id",
				true, iIdAuftragI.toString(), FilterKriterium.OPERATOR_EQUAL,
				false);

		FilterKriterium krit2 = new FilterKriterium(
				AuftragpositionFac.FLR_AUFTRAGPOSITION_AUFTRAGPOSITIONSTATUS_C_NR,
				true, "", FilterKriterium.OPERATOR_IS + " "
						+ FilterKriterium.OPERATOR_NOT_NULL, false);

		kriterien[0] = krit1;
		kriterien[1] = krit2;

		return kriterien;
	}

	/**
	 * Default Filter Kriterien fuer eine Liste von Auftragtexten.
	 * 
	 * @return FilterKriterium[] Default Filter Kriterien
	 * @throws Throwable
	 *             Ausnahme
	 */
	public FilterKriterium[] createFKAuftragtext() throws Throwable {
		FilterKriterium[] kriterien = new FilterKriterium[2];

		FilterKriterium krit1 = new FilterKriterium("mandant_c_nr", true, "'"
				+ LPMain.getInstance().getTheClient().getMandant() + "'",
				FilterKriterium.OPERATOR_EQUAL, false);
		kriterien[0] = krit1;

		FilterKriterium krit2 = new FilterKriterium(
				AuftragServiceFac.FLR_AUFTRAGTEXT_LOCALE_C_NR, true, "'"
						+ LPMain.getInstance().getTheClient()
								.getLocUiAsString() + "'",
				FilterKriterium.OPERATOR_EQUAL, false);

		kriterien[0] = krit1;
		kriterien[1] = krit2;

		return kriterien;
	}

	public FilterKriterium[] createFKAuftragMandantCNrOhneRahmen()
			throws Throwable {
		FilterKriterium[] kriterien = new FilterKriterium[2];

		FilterKriterium krit1 = SystemFilterFactory.getInstance()
				.createFKMandantCNr()[0];

		FilterKriterium krit2 = new FilterKriterium(
				AuftragFac.FLR_AUFTRAG_AUFTRAGART_C_NR, true, "('"
						+ AuftragServiceFac.AUFTRAGART_RAHMEN + "')",
				FilterKriterium.OPERATOR_NOT_IN, false);

		kriterien[0] = krit1;
		kriterien[1] = krit2;

		return kriterien;
	}

	/**
	 * Auftragauswahlliste mit Direktfilter. <br>
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
	public PanelQueryFLR createPanelFLRAuftrag(InternalFrame internalFrameI,
			boolean bShowFilterButton, boolean bShowLeerenButton,
			FilterKriterium[] fkI) throws Throwable {
		String[] aWhichButtonIUse = SystemFilterFactory.getInstance()
				.createButtonArray(bShowFilterButton, bShowLeerenButton);

		FilterKriterium[] fk = createFKAuftragMandantCNrOhneRahmen();

		if (fkI != null) {
			fk = fkI;
		}

		PanelQueryFLR panelQueryFLRAuftrag = new PanelQueryFLR(
				AuftragFilterFactory.getInstance()
						.createQTPanelAuftragAuswahl(), fk,
				QueryParameters.UC_ID_AUFTRAG, aWhichButtonIUse,
				internalFrameI,
				LPMain.getTextRespectUISPr("title.auftragauswahlliste"));

		panelQueryFLRAuftrag.befuellePanelFilterkriterienDirektUndVersteckte(
				createFKDAuftragnummer(), createFKDKundenname(),
				AuftragFilterFactory.getInstance().createFKVAuftrag());
		panelQueryFLRAuftrag.addDirektFilter(AuftragFilterFactory.getInstance()
				.createFKDProjekt());
		panelQueryFLRAuftrag.addDirektFilter(AuftragFilterFactory.getInstance()
				.createFKDTextSuchen());
		return panelQueryFLRAuftrag;
	}

	/**
	 * Auftragauswahlliste mit Direktfilter. <br>
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
	public PanelQueryFLR createPanelFLRAuftragMitRahmen(
			InternalFrame internalFrameI, boolean bShowFilterButton,
			boolean bShowLeerenButton, FilterKriterium[] fkI) throws Throwable {
		String[] aWhichButtonIUse = SystemFilterFactory.getInstance()
				.createButtonArray(bShowFilterButton, bShowLeerenButton);

		FilterKriterium[] fk = SystemFilterFactory.getInstance()
				.createFKMandantCNr();

		if (fkI != null) {
			fk = fkI;
		}

		PanelQueryFLR panelQueryFLRAuftrag = new PanelQueryFLR(
				AuftragFilterFactory.getInstance()
						.createQTPanelAuftragAuswahl(), fk,
				QueryParameters.UC_ID_AUFTRAG, aWhichButtonIUse,
				internalFrameI,
				LPMain.getTextRespectUISPr("title.auftragauswahlliste"));

		panelQueryFLRAuftrag.befuellePanelFilterkriterienDirektUndVersteckte(
				createFKDAuftragnummer(), createFKDKundenname(),
				createFKVAuftrag());
		panelQueryFLRAuftrag.addDirektFilter(AuftragFilterFactory.getInstance()
				.createFKDProjekt());
		panelQueryFLRAuftrag.addDirektFilter(AuftragFilterFactory.getInstance()
				.createFKDTextSuchen());
		return panelQueryFLRAuftrag;
	}

	/**
	 * Auftragauswahlliste mit Direktfilter. <br>
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
	 *            die iId des in der Liste selektierten Auftrags
	 * @return PanelQueryFLR die Auswahlliste
	 * @throws Throwable
	 *             Ausnahme
	 */
	public PanelQueryFLR createPanelFLRAuftrag(InternalFrame internalFrameI,
			boolean bShowFilterButton, boolean bShowLeerenButton,
			FilterKriterium[] fkI, Integer selectedIIdI) throws Throwable {
		PanelQueryFLR panelQueryFLRAuftrag = createPanelFLRAuftrag(
				internalFrameI, bShowFilterButton, bShowLeerenButton, fkI);

		if (selectedIIdI != null) {
			panelQueryFLRAuftrag.setSelectedId(selectedIIdI);
		}

		return panelQueryFLRAuftrag;
	}

	/**
	 * Liste von Filterkriterien fuer die Liste aller nicht erledigten
	 * Rahmenauftraege eines Mandanten.
	 * 
	 * @return FilterKriterium[] Liste der Filterkriterien
	 * @throws Throwable
	 */
	public FilterKriterium[] createFKRahmenauftraegeEinesMandanten()
			throws Throwable {
		FilterKriterium[] fk = new FilterKriterium[5];

		fk[0] = SystemFilterFactory.getInstance().createFKMandantCNr()[0];
		fk[1] = new FilterKriterium(AuftragFac.FLR_AUFTRAG_AUFTRAGART_C_NR,
				true, "'" + AuftragServiceFac.AUFTRAGART_RAHMEN + "'",
				FilterKriterium.OPERATOR_EQUAL, false);
		fk[2] = new FilterKriterium(AuftragFac.FLR_AUFTRAG_AUFTRAGSTATUS_C_NR,
				true, "('" + AuftragServiceFac.AUFTRAGSTATUS_ANGELEGT + "')",
				FilterKriterium.OPERATOR_NOT_IN, false);
		fk[3] = new FilterKriterium(AuftragFac.FLR_AUFTRAG_AUFTRAGSTATUS_C_NR,
				true, "('" + AuftragServiceFac.AUFTRAGSTATUS_STORNIERT + "')",
				FilterKriterium.OPERATOR_NOT_IN, false);
		fk[4] = new FilterKriterium(AuftragFac.FLR_AUFTRAG_AUFTRAGSTATUS_C_NR,
				true, "('" + AuftragServiceFac.AUFTRAGSTATUS_ERLEDIGT + "')",
				FilterKriterium.OPERATOR_NOT_IN, false);
		return fk;
	}

	/**
	 * Auswahlliste fuer alle nicht erledigten Rahmenauftraege mit Direktfilter. <br>
	 * Der Filter zur Vorselektion kann von aussen gesetzt werden. <br>
	 * Per default wird nach dem aktuellen Mandanten gefiltert.
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
	public PanelQueryFLR createPanelFLRRahmenauftrag(
			InternalFrame internalFrameI, boolean bShowFilterButton,
			boolean bShowLeerenButton) throws Throwable {
		String[] aWhichButtonIUse = SystemFilterFactory.getInstance()
				.createButtonArray(bShowFilterButton, bShowLeerenButton);

		FilterKriterium[] fk = createFKRahmenauftraegeEinesMandanten();

		PanelQueryFLR panelQueryFLRAuftrag = new PanelQueryFLR(
				AuftragFilterFactory.getInstance()
						.createQTPanelAuftragAuswahl(), fk,
				QueryParameters.UC_ID_AUFTRAG, aWhichButtonIUse,
				internalFrameI,
				LPMain.getTextRespectUISPr("title.rahmenauftragauswahlliste"));

		panelQueryFLRAuftrag.befuellePanelFilterkriterienDirektUndVersteckte(
				AuftragFilterFactory.getInstance().createFKDAuftragnummer(),
				AuftragFilterFactory.getInstance().createFKDKundenname(),
				AuftragFilterFactory.getInstance().createFKVAuftrag());
		panelQueryFLRAuftrag.addDirektFilter(AuftragFilterFactory.getInstance()
				.createFKDProjekt());
		panelQueryFLRAuftrag.addDirektFilter(AuftragFilterFactory.getInstance()
				.createFKDTextSuchen());
		return panelQueryFLRAuftrag;
	}

	/**
	 * Auswahlliste fuer alle nicht erledigten Rahmenauftraege mit Direktfilter. <br>
	 * Der Filter zur Vorselektion kann von aussen gesetzt werden. <br>
	 * Per default wird nach dem aktuellen Mandanten gefiltert.
	 * 
	 * @param internalFrameI
	 *            InternalFrame
	 * @param bShowFilterButton
	 *            den New Button anzeigen
	 * @param bShowLeerenButton
	 *            den Leeren Button anzeigen
	 * @param selectedIIdI
	 *            der aktuell gewaehlte Rahmenauftrag
	 * @return PanelQueryFLR die Auswahlliste
	 * @throws Throwable
	 *             Ausnahme
	 */
	public PanelQueryFLR createPanelFLRRahmenauftrag(
			InternalFrame internalFrameI, boolean bShowFilterButton,
			boolean bShowLeerenButton, Integer selectedIIdI) throws Throwable {
		PanelQueryFLR panelQueryFLRAuftrag = createPanelFLRRahmenauftrag(
				internalFrameI, bShowFilterButton, bShowLeerenButton);

		if (selectedIIdI != null) {
			panelQueryFLRAuftrag.setSelectedId(selectedIIdI);
		}

		return panelQueryFLRAuftrag;
	}

	/**
	 * Filterkriterien fuer eine Liste von Auftragpositionen zu einem bestimmten
	 * Auftrag, bei denen die offene Menge != null ist.
	 * 
	 * @param iIdAuftragI
	 *            PK des Auftrags
	 * @return FilterKriterium[] die Filterkriterien
	 * @throws Throwable
	 *             Ausnahme
	 */
	public FilterKriterium[] createFKAuftragpositionSichtRahmen(
			Integer iIdAuftragI) throws Throwable {
		FilterKriterium[] kriterien = null;

		if (iIdAuftragI != null) {
			kriterien = new FilterKriterium[2];

			kriterien[0] = new FilterKriterium(
					AuftragpositionFac.FLR_AUFTRAGPOSITION_FLRAUFTRAG + ".i_id",
					true, iIdAuftragI.toString(),
					FilterKriterium.OPERATOR_EQUAL, false);

			kriterien[1] = new FilterKriterium(
					AuftragpositionFac.FLR_AUFTRAGPOSITION_N_OFFENEMENGE, true,
					"", FilterKriterium.OPERATOR_IS + " "
							+ FilterKriterium.OPERATOR_NOT_NULL, false);
		}

		return kriterien;
	}

	/**
	 * Filterkriterien fuer eine Liste von Auftragpositionen zu einem bestimmten
	 * Auftrag, bei denen die offene Menge != null und > 0 ist.
	 * 
	 * @param iIdAuftragI
	 *            PK des Auftrags
	 * @return FilterKriterium[] die Filterkriterien
	 * @throws Throwable
	 *             Ausnahme
	 */
	public FilterKriterium[] createFKAuftragpositionSichtRahmenAusAbruf(
			Integer iIdAuftragI) throws Throwable {
		FilterKriterium[] kriterien = null;

		if (iIdAuftragI != null) {
			kriterien = new FilterKriterium[3];

			kriterien[0] = new FilterKriterium(
					AuftragpositionFac.FLR_AUFTRAGPOSITION_FLRAUFTRAG + ".i_id",
					true, iIdAuftragI.toString(),
					FilterKriterium.OPERATOR_EQUAL, false);

			kriterien[1] = new FilterKriterium(
					AuftragpositionFac.FLR_AUFTRAGPOSITION_N_OFFENEMENGE, true,
					"", FilterKriterium.OPERATOR_IS + " "
							+ FilterKriterium.OPERATOR_NOT_NULL, false);

			kriterien[2] = new FilterKriterium(
					AuftragpositionFac.FLR_AUFTRAGPOSITION_N_OFFENEMENGE, true,
					String.valueOf(0), FilterKriterium.OPERATOR_GT, false);
		}

		return kriterien;
	}

	public FilterKriterium[] createFKAuftraegeEinesKunden(Integer kundeIId)
			throws Throwable {
		FilterKriterium[] fk = new FilterKriterium[2];

		fk[0] = SystemFilterFactory.getInstance().createFKMandantCNr()[0];
		fk[1] = new FilterKriterium(
				AuftragFac.FLR_AUFTRAG_KUNDE_I_ID_AUFTRAGSADRESSE, true, "'"
						+ kundeIId.toString() + "'",
				FilterKriterium.OPERATOR_EQUAL, false);
		return fk;
	}

	public FilterKriterium[] createFKSchnellansicht() throws Throwable {
		FilterKriterium[] filters = new FilterKriterium[1];
		filters[0] = new FilterKriterium(
				AuftragFac.FLR_AUFTRAG_AUFTRAGSTATUS_C_NR, true, "('"
						+ AuftragServiceFac.AUFTRAGSTATUS_STORNIERT + "','"
						+ AuftragServiceFac.AUFTRAGSTATUS_ERLEDIGT + "')",
				FilterKriterium.OPERATOR_NOT_IN, false);

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
				QueryParameters.UC_ID_AUFTRAGBEGRUENDUNG, aWhichButtonIUse,
				internalFrameI, LPMain.getTextRespectUISPr("ls.begruendung"));

		panelQueryFLRBegruendung.setSelectedId(selectedId);
		return panelQueryFLRBegruendung;
	}

}
