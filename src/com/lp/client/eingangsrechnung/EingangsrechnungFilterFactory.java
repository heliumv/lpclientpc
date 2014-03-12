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
package com.lp.client.eingangsrechnung;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.PanelQueryFLRGoto;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.system.SystemFilterFactory;
import com.lp.server.eingangsrechnung.service.EingangsrechnungFac;
import com.lp.server.inserat.service.InseratFac;
import com.lp.server.partner.service.LieferantFac;
import com.lp.server.partner.service.PartnerFac;
import com.lp.server.system.service.LocaleFac;
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
 * Eingangsrechnung.
 * </p>
 * 
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2008
 * </p>
 * 
 * <p>
 * Erstellung: 29. 04. 2005
 * </p>
 * 
 * <p>
 * 
 * @author Martin Bluehweis
 *         </p>
 * 
 * @version $Revision: 1.9 $ Date $Date: 2012/08/23 11:09:58 $
 */

public class EingangsrechnungFilterFactory {

	private static EingangsrechnungFilterFactory filterFactory = null;

	private EingangsrechnungFilterFactory() {
		// Singleton.
	}

	/**
	 * Hole das Singelton EingangsrechnungFilterFactory.
	 * 
	 * @return EingangsrechnungFilterFactory
	 */
	static public EingangsrechnungFilterFactory getInstance() {
		if (filterFactory == null) {
			filterFactory = new EingangsrechnungFilterFactory();
		}
		return filterFactory;
	}

	public FilterKriterium[] createFKEingangsrechnungSchnellansicht() {
		FilterKriterium[] filters = new FilterKriterium[1];
		filters[0] = new FilterKriterium(
				EingangsrechnungFac.FLR_EINGANGSRECHNUNGSSTATUS_STATUS_C_NR,
				true, "('" + EingangsrechnungFac.STATUS_ERLEDIGT + "','"
						+ EingangsrechnungFac.STATUS_STORNIERT + "')",
				FilterKriterium.OPERATOR_NOT_IN, false);

		return filters;
	}

	/**
	 * Direktes Filter Kriterium Eingangsrechnungnummer fuer das
	 * PanelQueryEingangsrechnung.
	 * 
	 * @return FilterKriteriumDirekt
	 * @throws Throwable
	 */
	protected FilterKriteriumDirekt createFKDEingangsrechnungnummer()
			throws Throwable {
		return new FilterKriteriumDirekt(EingangsrechnungFac.FLR_ER_C_NR, "",
				FilterKriterium.OPERATOR_LIKE, "ER-Nummer",
				FilterKriteriumDirekt.PROZENT_LEADING, // Auswertung als '%XX'
				true, // wrapWithSingleQuotes
				false, Facade.MAX_UNBESCHRAENKT);
	}

	/**
	 * Direktes Filter Kriterium TextSuche PanelAuftragAuswahl.
	 * 
	 * @return FilterKriteriumDirekt
	 * @throws Throwable
	 */
	public FilterKriteriumDirekt createFKDTextSuchen() throws Throwable {

		return new FilterKriteriumDirekt("c_suche", "",
				FilterKriterium.OPERATOR_LIKE, LPMain
						.getTextRespectUISPr("lp.textsuche"),
				FilterKriteriumDirekt.PROZENT_BOTH, // Auswertung als '%XX'
				true, // wrapWithSingleQuotes
				true, Facade.MAX_UNBESCHRAENKT); // ignore case
	}

	/**
	 * Direktes Filter Kriterium Kundenname fuer das PanelAuftragAuswahl.
	 * 
	 * @return FilterKriteriumDirekt
	 */
	protected FilterKriteriumDirekt createFKDLieferantname() throws Throwable {
		ParametermandantDto parameter = (ParametermandantDto) DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getParametermandant(
						ParameterFac.PARAMETER_PARTNERSUCHE_WILDCARD_BEIDSEITIG,
						ParameterFac.KATEGORIE_PARTNER,
						LPMain.getTheClient().getMandant());
		if (((Boolean) parameter.getCWertAsObject() == true)) {
			FilterKriteriumDirekt fkd = new FilterKriteriumDirekt(
					EingangsrechnungFac.FLR_ER_FLRLIEFERANT + "."
							+ LieferantFac.FLR_PARTNER + "."
							+ PartnerFac.FLR_PARTNER_NAME1NACHNAMEFIRMAZEILE1,
					"", FilterKriterium.OPERATOR_LIKE, LPMain
							.getTextRespectUISPr("label.lieferant"),
					FilterKriteriumDirekt.PROZENT_BOTH, // Auswertung als
															// 'XX%'
					true, // wrapWithSingleQuotes
					true, Facade.MAX_UNBESCHRAENKT);
			return fkd;
		} else {
			FilterKriteriumDirekt fkd = new FilterKriteriumDirekt(
					EingangsrechnungFac.FLR_ER_FLRLIEFERANT + "."
							+ LieferantFac.FLR_PARTNER + "."
							+ PartnerFac.FLR_PARTNER_NAME1NACHNAMEFIRMAZEILE1,
					"", FilterKriterium.OPERATOR_LIKE, LPMain
							.getTextRespectUISPr("label.lieferant"),
					FilterKriteriumDirekt.PROZENT_TRAILING, // Auswertung als
															// 'XX%'
					true, // wrapWithSingleQuotes
					true, Facade.MAX_UNBESCHRAENKT);
			return fkd;
		}
	}

	public FilterKriterium[] createFKEingangsrechnungAuswahl(
			boolean bZusatzkosten) throws Throwable {
		FilterKriterium[] kriterien = new FilterKriterium[2];
		kriterien[0] = new FilterKriterium("mandant_c_nr", true, "'"
				+ LPMain.getTheClient().getMandant() + "'",
				FilterKriterium.OPERATOR_EQUAL, false);

		if (bZusatzkosten) {
			kriterien[1] = new FilterKriterium("eingangsrechnungart_c_nr",
					true,
					"'" + EingangsrechnungFac.EINGANGSRECHNUNGART_ZUSATZKOSTEN
							+ "'", FilterKriterium.OPERATOR_EQUAL, false);

		} else {
			kriterien[1] = new FilterKriterium("eingangsrechnungart_c_nr",
					true,
					"'" + EingangsrechnungFac.EINGANGSRECHNUNGART_ZUSATZKOSTEN
							+ "'", FilterKriterium.OPERATOR_NOT_EQUAL, false);
		}

		return kriterien;
	}

	public FilterKriterium[] createFKAuftragszuordnung(
			Integer eingangsrechnungIId) {
		FilterKriterium[] filters = new FilterKriterium[1];
		filters[0] = new FilterKriterium(
				EingangsrechnungFac.FLR_AZ_EINGANGSRECHNUNG_I_ID, true,
				eingangsrechnungIId + "", FilterKriterium.OPERATOR_EQUAL, false);
		return filters;
	}
	public FilterKriterium[] createFKInseratzuordnung(
			Integer eingangsrechnungIId) {
		FilterKriterium[] filters = new FilterKriterium[1];
		filters[0] = new FilterKriterium(
				InseratFac.FLR_INSERATER_EINGANGSRECHNUNG_I_ID, true,
				eingangsrechnungIId + "", FilterKriterium.OPERATOR_EQUAL, false);
		return filters;
	}

	public FilterKriterium[] createFKKontierung(Integer eingangsrechnungIId) {
		FilterKriterium[] filters = new FilterKriterium[1];
		filters[0] = new FilterKriterium(
				EingangsrechnungFac.FLR_KONTIERUNG_EINGANGRECHNUNG_I_ID, true,
				eingangsrechnungIId + "", FilterKriterium.OPERATOR_EQUAL, false);
		return filters;
	}

	public FilterKriterium[] createFKLieferant(Integer lieferantIId) {
		FilterKriterium[] filters = new FilterKriterium[1];
		filters[0] = new FilterKriterium(
				EingangsrechnungFac.FLR_ER_FLRLIEFERANT + "."
						+ LieferantFac.FLR_LIEFERANT_I_ID, true, +lieferantIId
						+ "", FilterKriterium.OPERATOR_EQUAL, false);
		return filters;
	}

	public FilterKriterium[] createFKEingangsrechnungStatus(String... statusCNr) {
		FilterKriterium[] filters = new FilterKriterium[statusCNr.length];
		String s = Helper.arrayToSqlInList(statusCNr);
		filters[0] = new FilterKriterium(
				EingangsrechnungFac.FLR_ER_STATUS_C_NR, true, s, FilterKriterium.OPERATOR_IN, false);
		return filters;
	}

	public FilterKriterium[] createFKZahlungen(Integer eingangsrechnungIId) {
		FilterKriterium[] filters = new FilterKriterium[1];
		filters[0] = new FilterKriterium(
				EingangsrechnungFac.FLR_KONTIERUNG_EINGANGRECHNUNG_I_ID, true,
				eingangsrechnungIId + "", FilterKriterium.OPERATOR_EQUAL, false);
		return filters;
	}

	public FilterKriterium createFKKriteriumBelegdatum(boolean isSelectedI,
			String sWcoGeschaeftsjahrI) {
		FilterKriterium[] filter = new FilterKriterium[1];
		filter[0] = new FilterKriterium(
				EingangsrechnungFac.KRIT_DATUM_BELEGDATUM, isSelectedI,
				sWcoGeschaeftsjahrI, FilterKriterium.OPERATOR_EQUAL, false);
		return filter[0];
	}

	public FilterKriterium createFKKriteriumFreigabedatum(boolean isSelectedI,
			String sWcoGeschaeftsjahrI) {
		FilterKriterium[] filter = new FilterKriterium[1];
		filter[0] = new FilterKriterium(
				EingangsrechnungFac.KRIT_DATUM_FREIGABEDATUM, isSelectedI,
				sWcoGeschaeftsjahrI, FilterKriterium.OPERATOR_EQUAL, false);
		return filter[0];
	}

	public FilterKriterium createFKKriteriumGeschaeftsjahr(boolean isSelectedI,
			String sWcoGeschaeftsjahrI) {
		FilterKriterium[] filter = new FilterKriterium[1];
		filter[0] = new FilterKriterium(
				EingangsrechnungFac.KRIT_JAHR_GESCHAEFTSJAHR, isSelectedI,
				sWcoGeschaeftsjahrI, FilterKriterium.OPERATOR_EQUAL, false);
		return filter[0];
	}

	public FilterKriterium createFKKriteriumKalenderjahr(boolean isSelectedI,
			String sWcoGeschaeftsjahrI) {
		FilterKriterium[] filter = new FilterKriterium[1];
		filter[0] = new FilterKriterium(
				EingangsrechnungFac.KRIT_JAHR_KALENDERJAHR, isSelectedI,
				sWcoGeschaeftsjahrI, FilterKriterium.OPERATOR_EQUAL, false);
		return filter[0];
	}

	public FilterKriterium createFKKriteriumWaehrung(boolean isSelectedI,
			String sWcoWaehrungI) {
		FilterKriterium[] filter = new FilterKriterium[1];
		filter[0] = new FilterKriterium(EingangsrechnungFac.KRIT_WAEHRUNG,
				isSelectedI, sWcoWaehrungI, FilterKriterium.OPERATOR_EQUAL,
				false);
		return filter[0];
	}

	public PanelQueryFLRGoto createPanelFLREingangsrechnungGoto(
			InternalFrame internalFrameI, Integer eingangsrechnungIIdI,
			Integer lieferantIId, boolean bShowFilterButton,
			boolean bShowLeerenButton) throws Throwable {
		return createPanelFLREingangsrechnungGoto(internalFrameI, eingangsrechnungIIdI, lieferantIId, bShowFilterButton, bShowLeerenButton, false);
	}

	public PanelQueryFLRGoto createPanelFLREingangsrechnungWareneingangGoto(
			InternalFrame internalFrameI, Integer eingangsrechnungIIdI,
			Integer lieferantIId, boolean bShowFilterButton,
			boolean bShowLeerenButton) throws Throwable {
		bShowFilterButton = false; // zzt keine QueryTypes
		// Buttons zusammenstellen.
		String[] aWhichButtonIUse = SystemFilterFactory.getInstance()
				.createButtonArray(bShowFilterButton, bShowLeerenButton);

		FilterKriterium[] fk = null;

		if (lieferantIId != null) {
			fk = new FilterKriterium[2];
			fk[0] = SystemFilterFactory.getInstance().createFKMandantCNr()[0];
			fk[1] = createFKLieferant(lieferantIId)[0];
		} else {
			fk = new FilterKriterium[1];
			fk[0] = SystemFilterFactory.getInstance().createFKMandantCNr()[0];

		}

		PanelQueryFLRGoto panelQueryFLREingangsrechnung = new PanelQueryFLRGoto(
				null,
				fk,
				QueryParameters.UC_ID_EINGANGSRECHNUNG,
				aWhichButtonIUse,
				internalFrameI,
				LocaleFac.BELEGART_EINGANGSRECHNUNG,
				LPMain
						.getTextRespectUISPr("title.eingangsrechnungenauswahlliste"),
				eingangsrechnungIIdI);
		panelQueryFLREingangsrechnung
				.befuellePanelFilterkriterienDirektUndVersteckte(
						createFKDEingangsrechnungnummer(),
						createFKDLieferantname(),
						createFKEingangsrechnungStatus(EingangsrechnungFac.STATUS_ANGELEGT)[0],
						LPMain.getTextRespectUISPr("lp.alle"));
		panelQueryFLREingangsrechnung.addDirektFilter(createFKDTextSuchen());
		return panelQueryFLREingangsrechnung;
	}
	
	public PanelQueryFLRGoto createPanelFLREingangsrechnungGoto(
			InternalFrame internalFrameI, Integer eingangsrechnungIIdI,
			Integer lieferantIId, boolean bShowFilterButton,
			boolean bShowLeerenButton, boolean bTeilbezahlte) throws Throwable {
		bShowFilterButton = false; // zzt keine QueryTypes
		// Buttons zusammenstellen.
		String[] aWhichButtonIUse = SystemFilterFactory.getInstance()
				.createButtonArray(bShowFilterButton, bShowLeerenButton);


		List<FilterKriterium> kritList = new ArrayList<FilterKriterium>();
		kritList.addAll(Arrays.asList(createFKEingangsrechnungAuswahl(false)));
		if (lieferantIId != null) {
			kritList.add(createFKLieferant(lieferantIId)[0]);
		}
		kritList.add(bTeilbezahlte ?
				createFKEingangsrechnungStatus(EingangsrechnungFac.STATUS_ANGELEGT, EingangsrechnungFac.STATUS_TEILBEZAHLT)[0] :
				createFKEingangsrechnungStatus(EingangsrechnungFac.STATUS_ANGELEGT)[0]);

		PanelQueryFLRGoto panelQueryFLREingangsrechnung = new PanelQueryFLRGoto(
				null,
				kritList.toArray(new FilterKriterium[0]),
				QueryParameters.UC_ID_EINGANGSRECHNUNG,
				aWhichButtonIUse,
				internalFrameI,
				LocaleFac.BELEGART_EINGANGSRECHNUNG,
				LPMain.getTextRespectUISPr("title.eingangsrechnungenauswahlliste"),
				eingangsrechnungIIdI);
		
		panelQueryFLREingangsrechnung
				.befuellePanelFilterkriterienDirekt(
						createFKDEingangsrechnungnummer(),
						createFKDLieferantname());
		panelQueryFLREingangsrechnung.addDirektFilter(createFKDTextSuchen());
		return panelQueryFLREingangsrechnung;
	}
}
