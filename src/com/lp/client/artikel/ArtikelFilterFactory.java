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
package com.lp.client.artikel;

import java.awt.event.ActionEvent;
import java.text.SimpleDateFormat;

import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.PanelQueryFLRArtikelShopgruppe;
import com.lp.client.frame.component.PanelQueryFLRLagerplatzAnlegen;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.system.SystemFilterFactory;
import com.lp.client.util.fastlanereader.gui.QueryType;
import com.lp.server.artikel.service.ArtikelFac;
import com.lp.server.artikel.service.ArtikelkommentarFac;
import com.lp.server.artikel.service.ArtikellieferantDto;
import com.lp.server.artikel.service.InventurFac;
import com.lp.server.artikel.service.LagerFac;
import com.lp.server.artikel.service.MaterialFac;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.fertigung.service.FertigungFac;
import com.lp.server.partner.service.LieferantFac;
import com.lp.server.partner.service.PartnerFac;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MandantFac;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.util.Facade;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.FilterKriteriumDirekt;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;

@SuppressWarnings("static-access")
public class ArtikelFilterFactory {
	private static ArtikelFilterFactory filterFactory = null;

	private ArtikelFilterFactory() {
		// Singleton.
	}

	static public ArtikelFilterFactory getInstance() {
		if (filterFactory == null) {
			filterFactory = new ArtikelFilterFactory();
		}

		return filterFactory;
	}

	public FilterKriterium[] createFKLagercockpitSchnellansicht()
			throws Throwable {
		FilterKriterium[] kriterien = new FilterKriterium[1];

		kriterien[0] = new FilterKriterium("lagerstand", true, "0",
				FilterKriterium.OPERATOR_GT, false);

		return kriterien;
	}

	public FilterKriterium[] createFKLagerliste() throws Throwable {
		FilterKriterium[] krit = new FilterKriterium[5];

		if (LPMain
				.getInstance()
				.getDesktop()
				.darfAnwenderAufZusatzfunktionZugreifen(
						MandantFac.ZUSATZFUNKTION_ZENTRALER_ARTIKELSTAMM)
				&& !LPMain
						.getInstance()
						.getDesktop()
						.darfAnwenderAufZusatzfunktionZugreifen(
								MandantFac.ZUSATZFUNKTION_GETRENNTE_LAGER)) {

			krit[0] = new FilterKriterium("flrlager.mandant_c_nr", true, "'"
					+ DelegateFactory.getInstance().getSystemDelegate()
							.getHauptmandant() + "'",
					FilterKriterium.OPERATOR_EQUAL, false);
		} else {
			krit[0] = new FilterKriterium("flrlager.mandant_c_nr", true, "'"
					+ LPMain.getInstance().getTheClient().getMandant() + "'",
					FilterKriterium.OPERATOR_EQUAL, false);
		}

		krit[1] = new FilterKriterium("flrlager.c_nr", true, "('"
				+ LagerFac.LAGER_KEINLAGER + "')",
				FilterKriterium.OPERATOR_NOT_IN, false);
		krit[2] = new FilterKriterium("flrlager.lagerart_c_nr", true, "('"
				+ LagerFac.LAGERART_WERTGUTSCHRIFT + "')",
				FilterKriterium.OPERATOR_NOT_IN, false);

		krit[3] = new FilterKriterium("flrlager.b_versteckt", true, "(1)",
				FilterKriterium.OPERATOR_NOT_IN, false);

		krit[4] = new FilterKriterium("flrsystemrolle.i_id", true,
				DelegateFactory.getInstance().getTheJudgeDelegate()
						.getSystemrolleIId()
						+ "", FilterKriterium.OPERATOR_EQUAL, false);

		return krit;
	}

	public FilterKriterium[] createFKLagerplatz() throws Throwable {
		FilterKriterium[] krit = new FilterKriterium[1];

		if (LPMain
				.getInstance()
				.getDesktop()
				.darfAnwenderAufZusatzfunktionZugreifen(
						MandantFac.ZUSATZFUNKTION_ZENTRALER_ARTIKELSTAMM)
				&& !LPMain
						.getInstance()
						.getDesktop()
						.darfAnwenderAufZusatzfunktionZugreifen(
								MandantFac.ZUSATZFUNKTION_GETRENNTE_LAGER)) {

			krit[0] = new FilterKriterium("flrlager.mandant_c_nr", true, "'"
					+ DelegateFactory.getInstance().getSystemDelegate()
							.getHauptmandant() + "'",
					FilterKriterium.OPERATOR_EQUAL, false);
		} else {
			krit[0] = new FilterKriterium("flrlager.mandant_c_nr", true, "'"
					+ LPMain.getInstance().getTheClient().getMandant() + "'",
					FilterKriterium.OPERATOR_EQUAL, false);
		}

		return krit;
	}

	public FilterKriterium[] createFKLagerlisteMitVersteckten()
			throws Throwable {
		FilterKriterium[] krit = new FilterKriterium[3];

		if (LPMain
				.getInstance()
				.getDesktop()
				.darfAnwenderAufZusatzfunktionZugreifen(
						MandantFac.ZUSATZFUNKTION_ZENTRALER_ARTIKELSTAMM)
				&& !LPMain
						.getInstance()
						.getDesktop()
						.darfAnwenderAufZusatzfunktionZugreifen(
								MandantFac.ZUSATZFUNKTION_GETRENNTE_LAGER)) {

			krit[0] = new FilterKriterium("mandant_c_nr", true, "'"
					+ DelegateFactory.getInstance().getSystemDelegate()
							.getHauptmandant() + "'",
					FilterKriterium.OPERATOR_EQUAL, false);
		} else {
			krit[0] = new FilterKriterium("mandant_c_nr", true, "'"
					+ LPMain.getInstance().getTheClient().getMandant() + "'",
					FilterKriterium.OPERATOR_EQUAL, false);
		}

		krit[1] = new FilterKriterium("c_nr", true, "('"
				+ LagerFac.LAGER_KEINLAGER + "')",
				FilterKriterium.OPERATOR_NOT_IN, false);
		krit[2] = new FilterKriterium("lagerart_c_nr", true, "('"
				+ LagerFac.LAGERART_WERTGUTSCHRIFT + "')",
				FilterKriterium.OPERATOR_NOT_IN, false);

		return krit;
	}

	public FilterKriterium[] createFKLagerlisteNurKeinLager() throws Throwable {
		FilterKriterium[] krit = new FilterKriterium[2];
		Integer lagerIId_KEIN_LAGER = DelegateFactory.getInstance()
				.getLagerDelegate()
				.lagerFindByCNrByMandantCNr(LagerFac.LAGER_KEINLAGER).getIId();

		krit[0] = new FilterKriterium("flrlager.i_id", true,
				lagerIId_KEIN_LAGER + "", FilterKriterium.OPERATOR_EQUAL, false);
		krit[1] = new FilterKriterium("flrsystemrolle.i_id", true,
				DelegateFactory.getInstance().getTheJudgeDelegate()
						.getSystemrolleIId()
						+ "", FilterKriterium.OPERATOR_EQUAL, false);
		return krit;
	}

	public FilterKriterium[] createFKInventurliste(Integer inventurIId)
			throws Throwable {
		FilterKriterium[] krit = new FilterKriterium[1];

		krit[0] = new FilterKriterium(InventurFac.FLR_INVENTURLISTE_FLRINVENTUR
				+ ".i_id", true, inventurIId + "",
				FilterKriterium.OPERATOR_EQUAL, false);

		return krit;
	}

	public FilterKriterium[] createFKMaterialzuschlag(Integer materialIId)
			throws Throwable {
		FilterKriterium[] kriterien = new FilterKriterium[2];
		kriterien[0] = new FilterKriterium(
				MaterialFac.FLR_MATERIALZUSCHLAG_MATERIAL_I_ID, true,
				materialIId + "", FilterKriterium.OPERATOR_EQUAL, false);

		kriterien[1] = new FilterKriterium("mandant_c_nr", true, "'"
				+ LPMain.getInstance().getTheClient().getMandant() + "'",
				FilterKriterium.OPERATOR_EQUAL, false);

		return kriterien;
	}

	public FilterKriterium createFKArtikeleinesLieferanten(Integer lieferantIId)
			throws Throwable {

		String s = "(";

		ArtikellieferantDto[] artlief = DelegateFactory.getInstance()
				.getArtikelDelegate()
				.artikellieferantFindByLieferantIId(lieferantIId);

		if (artlief != null && artlief.length > 0) {
			for (int i = 0; i < artlief.length; i++) {
				if (i != artlief.length - 1) {
					s += artlief[i].getArtikelIId() + ",";
				} else {
					s += artlief[i].getArtikelIId();
				}

			}
		} else {
			s += "NULL";
		}

		s += ")";
		return new FilterKriterium("artikelliste.i_id", true, s,
				FilterKriterium.OPERATOR_IN, false);
	}

	public FilterKriterium[] createFKArtikelliste() throws Throwable {
		// Handartikel nicht anzeigen
		FilterKriterium[] kriterien = new FilterKriterium[2];
		kriterien[0] = new FilterKriterium("artikelliste."
				+ ArtikelFac.FLR_ARTIKEL_ARTIKELART_C_NR, true, "('"
				+ ArtikelFac.ARTIKELART_HANDARTIKEL + "')",
				FilterKriterium.OPERATOR_NOT_IN, false);

		if (LPMain
				.getInstance()
				.getDesktop()
				.darfAnwenderAufZusatzfunktionZugreifen(
						MandantFac.ZUSATZFUNKTION_ZENTRALER_ARTIKELSTAMM)) {
			kriterien[1] = new FilterKriterium("artikelliste.mandant_c_nr",
					true, "'"
							+ DelegateFactory.getInstance().getSystemDelegate()
									.getHauptmandant() + "'",
					FilterKriterium.OPERATOR_EQUAL, false);
		} else {
			kriterien[1] = new FilterKriterium("artikelliste.mandant_c_nr",
					true, "'"
							+ LPMain.getInstance().getTheClient().getMandant()
							+ "'", FilterKriterium.OPERATOR_EQUAL, false);

		}

		return kriterien;
	}

	public FilterKriterium[] createFKLagercockpitArtikel(Integer artikelIId)
			throws Throwable {

		FilterKriterium[] kriterien = new FilterKriterium[1];

		kriterien[0] = new FilterKriterium("artikel_i_id", true, artikelIId
				+ "", FilterKriterium.OPERATOR_EQUAL, false);

		return kriterien;
	}

	public FilterKriterium[] createFKLagercockpitNichtlagerbewirtschafteteArtikel()
			throws Throwable {

		FilterKriterium[] kriterien = new FilterKriterium[3];

		kriterien[0] = new FilterKriterium("b_verraeumt", true, "0",
				FilterKriterium.OPERATOR_EQUAL, false);
		kriterien[1] = new FilterKriterium(
				"flrbestellposition.flrartikel.artikelart_c_nr", true, "'"
						+ ArtikelFac.ARTIKELART_HANDARTIKEL + "'",
				FilterKriterium.OPERATOR_NOT_EQUAL, false);
		kriterien[2] = new FilterKriterium(
				"flrbestellposition.flrartikel.b_lagerbewirtschaftet", true,
				"0", FilterKriterium.OPERATOR_EQUAL, false);

		return kriterien;
	}

	public FilterKriterium[] createFKLagercockpitNichtlagerbewirtschafteteArtikelAusblenden()
			throws Throwable {

		FilterKriterium[] kriterien = new FilterKriterium[1];

		kriterien[0] = new FilterKriterium("umbuchung.flrartikel.b_lagerbewirtschaftet",
				true, "1", FilterKriterium.OPERATOR_EQUAL, false);

		return kriterien;
	}

	public FilterKriterium[] createFKLagercockpitMaterialumbuchung()
			throws Throwable {

		FilterKriterium[] kriterien = new FilterKriterium[1];

		kriterien[0] = new FilterKriterium("flrlos.status_c_nr", true, "('"
				+ LocaleFac.STATUS_ERLEDIGT + "','"
				+ LocaleFac.STATUS_STORNIERT + "')",
				FilterKriterium.OPERATOR_NOT_IN, false);

		return kriterien;

	}

	public FilterKriterium[] createFKArtikellisteKey(Integer artikelIId)
			throws Throwable {
		// Handartikel nicht anzeigen
		FilterKriterium[] kriterien = new FilterKriterium[1];
		kriterien[0] = new FilterKriterium("artikelliste.i_id", true,
				artikelIId + "", FilterKriterium.OPERATOR_EQUAL, false);

		return kriterien;
	}

	public FilterKriterium[] createFKKatalog(Integer artikelIId)
			throws Throwable {
		FilterKriterium[] kriterien = new FilterKriterium[1];
		kriterien[0] = new FilterKriterium("artikel_i_id", true, artikelIId
				+ "", FilterKriterium.OPERATOR_EQUAL, false);
		return kriterien;
	}

	public FilterKriterium[] createFKLagercockpit() throws Throwable {
		FilterKriterium[] kriterien = new FilterKriterium[3];
		kriterien[0] = new FilterKriterium("flrartikelliste.mandant_c_nr",
				true, "'" + LPMain.getInstance().getTheClient().getMandant()
						+ "'", FilterKriterium.OPERATOR_EQUAL, false);
		kriterien[1] = new FilterKriterium("flrlager.lagerart_c_nr", true, "'"
				+ LagerFac.LAGERART_WARENEINGANG + "'",
				FilterKriterium.OPERATOR_EQUAL, false);
		kriterien[2] = new FilterKriterium("n_lagerstand", true, "0",
				FilterKriterium.OPERATOR_GT, false);
		return kriterien;
	}

	public FilterKriterium[] createFKVorschlagstext() throws Throwable {
		FilterKriterium[] kriterien = new FilterKriterium[1];
		kriterien[0] = new FilterKriterium("locale_c_nr", true, "'"
				+ LPMain.getTheClient().getLocUiAsString() + "'",
				FilterKriterium.OPERATOR_EQUAL, false);
		return kriterien;
	}

	public FilterKriterium[] createFKEinkaufsean(Integer artikelIId)
			throws Throwable {
		FilterKriterium[] kriterien = new FilterKriterium[1];
		kriterien[0] = new FilterKriterium("artikel_i_id", true, artikelIId
				+ "", FilterKriterium.OPERATOR_EQUAL, false);
		return kriterien;
	}

	public FilterKriterium[] createFKArtikellieferant(Integer artikelIId)
			throws Throwable {
		FilterKriterium[] kriterien = new FilterKriterium[1];
		kriterien[0] = new FilterKriterium("artikel_i_id", true, artikelIId
				+ "", FilterKriterium.OPERATOR_EQUAL, false);
		return kriterien;
	}

	public FilterKriterium[] createFKArtikelsperren(Integer artikelIId)
			throws Throwable {
		FilterKriterium[] kriterien = new FilterKriterium[1];
		kriterien[0] = new FilterKriterium("artikel_i_id", true, artikelIId
				+ "", FilterKriterium.OPERATOR_EQUAL, false);
		return kriterien;
	}

	public FilterKriterium[] createFKArtikelshopgruppen(Integer artikelIId)
			throws Throwable {
		FilterKriterium[] kriterien = new FilterKriterium[1];
		kriterien[0] = new FilterKriterium("artikel_i_id", true, artikelIId
				+ "", FilterKriterium.OPERATOR_EQUAL, false);
		return kriterien;
	}

	public FilterKriterium[] createFKZugehoerige(Integer artikelIId)
			throws Throwable {
		FilterKriterium[] kriterien = new FilterKriterium[1];
		kriterien[0] = new FilterKriterium("artikel_i_id", true, artikelIId
				+ "", FilterKriterium.OPERATOR_EQUAL, false);
		return kriterien;
	}

	public FilterKriterium createFKVArtikel() {
		FilterKriterium fkVersteckt = new FilterKriterium("artikelliste."
				+ ArtikelFac.FLR_ARTIKELLISTE_B_VERSTECKT, true, "(1)", // wenn
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

	public FilterKriterium createFKVArtikelLagercockpit() {
		FilterKriterium fkVersteckt = new FilterKriterium("flrartikel."
				+ ArtikelFac.FLR_ARTIKELLISTE_B_VERSTECKT, true, "(1)", // wenn
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

	public FilterKriterium createFKVLager() {
		FilterKriterium fkVersteckt = new FilterKriterium("flrlager."
				+ ArtikelFac.FLR_ARTIKELLISTE_B_VERSTECKT, true, "(1)", // wenn
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

	public FilterKriterium[] createFKVkpfStaffelmenge(Integer artikelIIdI)
			throws Throwable {
		SimpleDateFormat mSDF = new SimpleDateFormat("dd.MM.yyyy");

		/*
		 * if (datPreisgueltigkeitI == null) { datPreisgueltigkeitI = new
		 * Date(System.currentTimeMillis()); }
		 */

		FilterKriterium[] kriterien = new FilterKriterium[1];

		kriterien[0] = new FilterKriterium("artikel_i_id", true, artikelIIdI
				+ "", FilterKriterium.OPERATOR_EQUAL, false);
		/*
		 * kriterien[1] = new
		 * FilterKriterium(VkPreisfindungFac.FLR_VKPFSTAFFELMENGE_T_PREISGUELTIGAB
		 * , true, "'" +mSDF.format(datPreisgueltigkeitI) + "'",
		 * FilterKriterium.OPERATOR_NOT_EQUAL, false);
		 */
		return kriterien;
	}

	public FilterKriterium[] createFKArtikelkommentar(Integer artikelIId)
			throws Throwable {
		FilterKriterium[] kriterien = new FilterKriterium[1];
		kriterien[0] = new FilterKriterium("artikel_i_id", true, artikelIId
				+ "", FilterKriterium.OPERATOR_EQUAL, false);
		return kriterien;
	}

	public FilterKriterium[] createFKArtikellieferantstaffel(
			Integer artikellieferantIId) {
		FilterKriterium[] kriterien = new FilterKriterium[1];
		kriterien[0] = new FilterKriterium(
				ArtikelFac.FLR_ARTIKELLIEFERANTSTAFFEL_ARTIKELLIEFERANT_I_ID,
				true, artikellieferantIId + "", FilterKriterium.OPERATOR_EQUAL,
				false);

		return kriterien;
	}

	public FilterKriterium[] createFKArtikellieferantstaffelWennNULL() {
		FilterKriterium[] kriterien = new FilterKriterium[1];
		kriterien[0] = new FilterKriterium(
				ArtikelFac.FLR_ARTIKELLIEFERANTSTAFFEL_ARTIKELLIEFERANT_I_ID,
				true, "-3678924", FilterKriterium.OPERATOR_EQUAL, false);

		return kriterien;
	}

	public FilterKriterium[] createFKArtikellisteNurArbeitszeit()
			throws Throwable {
		FilterKriterium[] kriterien = new FilterKriterium[2];
		kriterien[0] = new FilterKriterium("artikelliste."
				+ ArtikelFac.FLR_ARTIKEL_ARTIKELART_C_NR, true, "'"
				+ ArtikelFac.ARTIKELART_ARBEITSZEIT + "'",
				FilterKriterium.OPERATOR_EQUAL, false);

		if (LPMain
				.getInstance()
				.getDesktop()
				.darfAnwenderAufZusatzfunktionZugreifen(
						MandantFac.ZUSATZFUNKTION_ZENTRALER_ARTIKELSTAMM)) {
			kriterien[1] = new FilterKriterium("artikelliste.mandant_c_nr",
					true, "'"
							+ DelegateFactory.getInstance().getSystemDelegate()
									.getHauptmandant() + "'",
					FilterKriterium.OPERATOR_EQUAL, false);
		} else {
			kriterien[1] = new FilterKriterium("artikelliste.mandant_c_nr",
					true, "'"
							+ LPMain.getInstance().getTheClient().getMandant()
							+ "'", FilterKriterium.OPERATOR_EQUAL, false);

		}
		return kriterien;
	}

	public FilterKriterium[] createFKArtikellisteOhneArbeitszeit()
			throws Throwable {
		FilterKriterium[] kriterien = new FilterKriterium[2];
		kriterien[0] = new FilterKriterium("artikelliste."
				+ ArtikelFac.FLR_ARTIKEL_ARTIKELART_C_NR, true, "'"
				+ ArtikelFac.ARTIKELART_ARTIKEL + "'",
				FilterKriterium.OPERATOR_EQUAL, false);

		if (LPMain
				.getInstance()
				.getDesktop()
				.darfAnwenderAufZusatzfunktionZugreifen(
						MandantFac.ZUSATZFUNKTION_ZENTRALER_ARTIKELSTAMM)) {
			kriterien[1] = new FilterKriterium("artikelliste.mandant_c_nr",
					true, "'"
							+ DelegateFactory.getInstance().getSystemDelegate()
									.getHauptmandant() + "'",
					FilterKriterium.OPERATOR_EQUAL, false);
		} else {
			kriterien[1] = new FilterKriterium("artikelliste.mandant_c_nr",
					true, "'"
							+ LPMain.getInstance().getTheClient().getMandant()
							+ "'", FilterKriterium.OPERATOR_EQUAL, false);

		}
		return kriterien;
	}

	public FilterKriterium[] createFKArtikellisteEinerShopgruppe(
			Integer shopgruppeIId) throws Throwable {
		FilterKriterium[] kriterien = new FilterKriterium[3];
		kriterien[0] = new FilterKriterium("artikelliste."
				+ ArtikelFac.FLR_ARTIKEL_ARTIKELART_C_NR, true, "'"
				+ ArtikelFac.ARTIKELART_HANDARTIKEL + "'",
				FilterKriterium.OPERATOR_NOT_EQUAL, false);

		if (LPMain
				.getInstance()
				.getDesktop()
				.darfAnwenderAufZusatzfunktionZugreifen(
						MandantFac.ZUSATZFUNKTION_ZENTRALER_ARTIKELSTAMM)) {
			kriterien[1] = new FilterKriterium("artikelliste.mandant_c_nr",
					true, "'"
							+ DelegateFactory.getInstance().getSystemDelegate()
									.getHauptmandant() + "'",
					FilterKriterium.OPERATOR_EQUAL, false);
		} else {
			kriterien[1] = new FilterKriterium("artikelliste.mandant_c_nr",
					true, "'"
							+ LPMain.getInstance().getTheClient().getMandant()
							+ "'", FilterKriterium.OPERATOR_EQUAL, false);

		}

		kriterien[2] = new FilterKriterium("artikelliste.flrshopgruppe.i_id",
				true, shopgruppeIId + "", FilterKriterium.OPERATOR_EQUAL, false);

		return kriterien;
	}

	public FilterKriteriumDirekt createFKDArtikelnummer(InternalFrame frame)
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

		return new FilterKriteriumDirekt("artikelliste.c_nr", "",
				FilterKriterium.OPERATOR_LIKE, LPMain.getInstance()
						.getTextRespectUISPr("artikel.artikelnummer"),
				FilterKriteriumDirekt.PROZENT_TRAILING, true, true, iLaenge);

	}

	public FilterKriteriumDirekt createFKDArtikelnummerLagercockpit(
			InternalFrame frame) throws Throwable {

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

		return new FilterKriteriumDirekt("flrartikelliste.c_nr", "",
				FilterKriterium.OPERATOR_LIKE, LPMain.getInstance()
						.getTextRespectUISPr("artikel.artikelnummer"),
				FilterKriteriumDirekt.PROZENT_TRAILING, true, true, iLaenge);

	}

	public FilterKriteriumDirekt createFKDArtikelnummerLagercockpitMaterialumlagerung(
			InternalFrame frame) throws Throwable {

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

	public FilterKriteriumDirekt createFKDArtikelnummerSnrChnrReklamation(
			InternalFrame frame) throws Throwable {

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

	public FilterKriteriumDirekt createFKDLagername() throws Throwable {

		return new FilterKriteriumDirekt("flrlager.c_nr", "",
				FilterKriterium.OPERATOR_LIKE, LPMain.getInstance()
						.getTextRespectUISPr("label.lager"),
				FilterKriteriumDirekt.PROZENT_TRAILING, true, true,
				Facade.MAX_UNBESCHRAENKT);

	}

	public FilterKriteriumDirekt createFKDSnrChnrReklamation() throws Throwable {

		return new FilterKriteriumDirekt(
				LagerFac.FLR_LAGERBEWEGUNG_C_SERIENNRCHARGENNR, "",
				FilterKriterium.OPERATOR_LIKE, LPMain.getInstance()
						.getTextRespectUISPr("rekla.snchrnauswahl"),
				FilterKriteriumDirekt.PROZENT_TRAILING, true, true,
				Facade.MAX_UNBESCHRAENKT);

	}

	public FilterKriteriumDirekt createFKDLagernameGrunddaten()
			throws Throwable {

		return new FilterKriteriumDirekt("c_nr", "",
				FilterKriterium.OPERATOR_LIKE, LPMain.getInstance()
						.getTextRespectUISPr("label.lager"),
				FilterKriteriumDirekt.PROZENT_TRAILING, true, true,
				Facade.MAX_UNBESCHRAENKT);

	}

	public FilterKriteriumDirekt createFKDLagerplatz() throws Throwable {

		return new FilterKriteriumDirekt("c_lagerplatz", "",
				FilterKriterium.OPERATOR_LIKE, LPMain.getInstance()
						.getTextRespectUISPr("lp.lagerplatz"),
				FilterKriteriumDirekt.PROZENT_TRAILING, true, true,
				Facade.MAX_UNBESCHRAENKT);

	}

	public FilterKriteriumDirekt createFKDVolltextsuche() throws Throwable {

		return new FilterKriteriumDirekt(
				ArtikelFac.FLR_ARTIKELLISTE_C_VOLLTEXT, "",
				FilterKriterium.OPERATOR_LIKE, LPMain.getInstance()
						.getTextRespectUISPr("lp.textsuche"),
				FilterKriteriumDirekt.EXTENDED_SEARCH, false, true,
				Facade.MAX_UNBESCHRAENKT); // wrapWithSingleQuotes

	}

	public FilterKriteriumDirekt createFKDArtikelgruppeKennung() {
		return new FilterKriteriumDirekt("artikelgruppe.c_nr", "",
				FilterKriterium.OPERATOR_LIKE, LPMain.getInstance()
						.getTextRespectUISPr("label.kennung"),
				FilterKriteriumDirekt.PROZENT_TRAILING, // Auswertung als 'XX%'
				true, true, Facade.MAX_UNBESCHRAENKT); // wrapWithSingleQuotes
	}

	public FilterKriteriumDirekt createFKDArtikelkommentarartKennung() {
		return new FilterKriteriumDirekt("artikelkommentarart.c_nr", "",
				FilterKriterium.OPERATOR_LIKE, LPMain.getInstance()
						.getTextRespectUISPr("label.kennung"),
				FilterKriteriumDirekt.PROZENT_TRAILING, // Auswertung als 'XX%'
				true, true, Facade.MAX_UNBESCHRAENKT); // wrapWithSingleQuotes
	}

	public FilterKriteriumDirekt createFKDArtikelklasseKennung() {
		return new FilterKriteriumDirekt("artikelklasse.c_nr", "",
				FilterKriterium.OPERATOR_LIKE, LPMain.getInstance()
						.getTextRespectUISPr("label.kennung"),
				FilterKriteriumDirekt.PROZENT_TRAILING, // Auswertung als 'XX%'
				true, true, Facade.MAX_UNBESCHRAENKT); // wrapWithSingleQuotes
	}

	public FilterKriteriumDirekt createFKDAKAG() throws Throwable {

		return new FilterKriteriumDirekt("akag", "",
				FilterKriterium.OPERATOR_LIKE, LPMain.getInstance()
						.getTextRespectUISPr("artikel.filter.akag"),
				FilterKriteriumDirekt.PROZENT_BOTH, true, true,
				Facade.MAX_UNBESCHRAENKT); // wrapWithSingleQuotes

	}

	public FilterKriteriumDirekt createFKDLieferantennrBezeichnung()
			throws Throwable {

		return new FilterKriteriumDirekt(
				ArtikelFac.FLR_ARTIKELLIEFERANT_C_ARTIKELNRLIEFERANT, "",
				FilterKriterium.OPERATOR_LIKE, LPMain.getInstance()
						.getTextRespectUISPr("artikel.filter.lieferantnrbez"),
				FilterKriteriumDirekt.PROZENT_BOTH, true, false,
				Facade.MAX_UNBESCHRAENKT); // wrapWithSingleQuotes

	}

	public FilterKriteriumDirekt createFKDReferenznr() throws Throwable {

		return new FilterKriteriumDirekt("artikelliste."
				+ ArtikelFac.FLR_ARTIKELLISTE_C_REFERENZNR, "",
				FilterKriterium.OPERATOR_LIKE, LPMain.getInstance()
						.getTextRespectUISPr("lp.referenznummer"),
				FilterKriteriumDirekt.PROZENT_BOTH, true, true,
				Facade.MAX_UNBESCHRAENKT); // wrapWithSingleQuotes

	}

	public FilterKriteriumDirekt createFKDLagerLagerart() throws Throwable {
		return new FilterKriteriumDirekt("flrlager."
				+ LagerFac.FLR_LAGER_LAGERART_C_NR, "",
				FilterKriterium.OPERATOR_LIKE, LPMain.getInstance()
						.getTextRespectUISPr("artikel.lagerart"),
				FilterKriteriumDirekt.PROZENT_TRAILING, true, true,
				Facade.MAX_UNBESCHRAENKT);
	}

	public FilterKriteriumDirekt createFKDLagerLagerartGrunddaten()
			throws Throwable {
		return new FilterKriteriumDirekt(LagerFac.FLR_LAGER_LAGERART_C_NR, "",
				FilterKriterium.OPERATOR_LIKE, LPMain.getInstance()
						.getTextRespectUISPr("artikel.lagerart"),
				FilterKriteriumDirekt.PROZENT_TRAILING, true, true,
				Facade.MAX_UNBESCHRAENKT);
	}

	public PanelQueryFLR createPanelFLRLager(InternalFrame internalFrameI,
			Integer selectedId) throws Throwable {
		return createPanelFLRLager(internalFrameI, selectedId, false, false);
	}

	public PanelQueryFLR createPanelFLRLager(InternalFrame internalFrameI,
			Integer selectedId, boolean bMitLeerenButton, boolean bNurKeinLager)
			throws Throwable {
		String[] aWhichButtonIUse = null;
		if (bMitLeerenButton) {

			aWhichButtonIUse = new String[] { PanelBasis.ACTION_REFRESH,
					PanelBasis.ACTION_LEEREN };
		} else {
			aWhichButtonIUse = new String[] { PanelBasis.ACTION_REFRESH };

		}
		FilterKriterium[] fk = null;
		if (bNurKeinLager == true) {
			fk = ArtikelFilterFactory.getInstance()
					.createFKLagerlisteNurKeinLager();

		} else {
			fk = ArtikelFilterFactory.getInstance().createFKLagerliste();

		}

		PanelQueryFLR panelQueryFLRLager = new PanelQueryFLR(createQTLager(),
				fk, QueryParameters.UC_ID_LAGER, aWhichButtonIUse,
				internalFrameI, LPMain.getInstance().getTextRespectUISPr(
						"title.lagerauswahlliste"));

		panelQueryFLRLager.befuellePanelFilterkriterienDirekt(
				createFKDLagername(), createFKDLagerLagerart());
		if (selectedId != null) {
			panelQueryFLRLager.setSelectedId(selectedId);
		}
		return panelQueryFLRLager;
	}

	public PanelQueryFLR createPanelFLRLagerAlle(InternalFrame internalFrameI,
			Integer selectedId, Integer systemrollIId,
			boolean bMitLeerenButton, boolean bNurKeinLager) throws Throwable {
		String[] aWhichButtonIUse = new String[] { PanelBasis.ACTION_REFRESH };

		Integer[] ids = DelegateFactory.getInstance().getBenutzerDelegate()
				.getBerechtigteLagerIIdsEinerSystemrolle(systemrollIId);
		// Ohne bereits vorhandene Laeger
		FilterKriterium[] kriterien = null;

		if (ids != null && ids.length > 0) {
			kriterien = new FilterKriterium[1];

			String s = "";

			for (int i = 0; i < ids.length; i++) {
				s += ids[i];
				if (i != ids.length - 1) {
					s += ",";
				}
			}

			kriterien[0] = new FilterKriterium("i_id", true, "(" + s + ")",
					FilterKriterium.OPERATOR_NOT_IN, false);
		}

		PanelQueryFLR panelQueryFLRLager = new PanelQueryFLR(null, kriterien,
				QueryParameters.UC_ID_LAGER_ALLE, aWhichButtonIUse,
				internalFrameI, LPMain.getInstance().getTextRespectUISPr(
						"title.lagerauswahlliste"));

		panelQueryFLRLager.befuellePanelFilterkriterienDirekt(
				createFKDLagernameGrunddaten(),
				createFKDLagerLagerartGrunddaten());
		if (selectedId != null) {
			panelQueryFLRLager.setSelectedId(selectedId);
		}
		return panelQueryFLRLager;
	}

	public FilterKriterium createFKVPreislistenAktive() {
		FilterKriterium fkVersteckt = new FilterKriterium("b_preislisteaktiv",
				true, "(0)", FilterKriterium.OPERATOR_NOT_IN, false);

		return fkVersteckt;
	}

	public PanelQueryFLR createPanelFLRPreisliste(InternalFrame internalFrameI,
			Integer selectedId) throws Throwable {
		String[] aWhichButtonIUse = { PanelBasis.ACTION_REFRESH,
				PanelBasis.ACTION_LEEREN };
		QueryType[] querytypes = null;
		FilterKriterium[] filters = SystemFilterFactory.getInstance()
				.createFKMandantCNr();
		PanelQueryFLR panelQueryFLRPreislisten = new PanelQueryFLR(querytypes,
				filters, QueryParameters.UC_ID_PREISLISTENNAME,
				aWhichButtonIUse, internalFrameI, LPMain.getInstance()
						.getTextRespectUISPr("vkpf.preislisten.title.tab"));
		panelQueryFLRPreislisten
				.befuellePanelFilterkriterienDirektUndVersteckte(
						null,
						null,
						createFKVPreislistenAktive(),
						LPMain.getInstance().getTextRespectUISPr(
								"artikel.mitinaktivenpreislisten"));

		panelQueryFLRPreislisten.setSelectedId(selectedId);

		return panelQueryFLRPreislisten;
	}

	public PanelQueryFLR createPanelFLRPaternoster(
			InternalFrame internalFrameI, Integer selectedId, Integer lagerIId)
			throws Throwable {
		String[] aWhichButtonIUse = { PanelBasis.ACTION_REFRESH,
				PanelBasis.ACTION_LEEREN };
		QueryType[] querytypes = null;
		FilterKriterium[] filters = null;
		if (lagerIId != null) {
			filters = ArtikelFilterFactory.getInstance()
					.createFKPaternosterLager(lagerIId);
		}

		PanelQueryFLR panelQueryFLRPreislisten = new PanelQueryFLR(querytypes,
				filters, QueryParameters.UC_ID_PATERNOSTER, aWhichButtonIUse,
				internalFrameI, LPMain.getInstance().getTextRespectUISPr(
						"artikel.paternoster"));

		panelQueryFLRPreislisten.setSelectedId(selectedId);

		return panelQueryFLRPreislisten;
	}

	public PanelQueryFLR createPanelFLRMaterial(InternalFrame internalFrameI,
			Integer selectedId) throws Throwable {
		String[] aWhichButtonIUse = { PanelBasis.ACTION_REFRESH,
				PanelBasis.ACTION_FILTER, PanelBasis.ACTION_LEEREN };

		PanelQueryFLR panelQueryFLRMaterial = new PanelQueryFLR(
				ArtikelFilterFactory.getInstance().createQTMaterial(), null,
				QueryParameters.UC_ID_MATERIAL, aWhichButtonIUse,
				internalFrameI, LPMain.getInstance().getTextRespectUISPr(
						"title.materialauswahlliste"));
		panelQueryFLRMaterial.befuellePanelFilterkriterienDirekt(
				SystemFilterFactory.getInstance().createFKDKennung(),
				SystemFilterFactory.getInstance()
						.createFKDSprTabelleBezeichnung(
								MaterialFac.FLR_MATERIAL_MATERIALSPRSET));
		if (selectedId != null) {
			panelQueryFLRMaterial.setSelectedId(selectedId);
		}

		return panelQueryFLRMaterial;

	}

	public PanelQueryFLR createPanelFLRHersteller(InternalFrame internalFrameI,
			Integer selectedId, boolean bMitLeerenButton) throws Throwable {

		String[] aWhichButtonIUse = null;
		if (bMitLeerenButton) {
			aWhichButtonIUse = new String[] { PanelBasis.ACTION_REFRESH,
					PanelBasis.ACTION_LEEREN };
		} else {
			aWhichButtonIUse = new String[] { PanelBasis.ACTION_REFRESH };
		}
		PanelQueryFLR panelQueryFLRHersteller = new PanelQueryFLR(null, null,
				QueryParameters.UC_ID_ARTIKELHERSTELLER, aWhichButtonIUse,
				internalFrameI,
				LPMain.getTextRespectUISPr("title.herstellerauswahlliste"));
		panelQueryFLRHersteller
				.befuellePanelFilterkriterienDirekt(ArtikelFilterFactory
						.getInstance().createFKDHersteller(),
						ArtikelFilterFactory.getInstance()
								.createFKDHerstellerPartner());
		panelQueryFLRHersteller.setSelectedId(selectedId);
		return panelQueryFLRHersteller;
	}

	public PanelQueryFLR createPanelFLRArtikelkommentarart(
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
				QueryParameters.UC_ID_ARTIKELKOMMENTARART, aWhichButtonIUse,
				internalFrameI, LPMain.getInstance().getTextRespectUISPr(
						"lp.kommentarart"));

		panelQueryFLRKommentarart
				.befuellePanelFilterkriterienDirekt(
						SystemFilterFactory.getInstance().createFKDKennung(),
						SystemFilterFactory
								.getInstance()
								.createFKDSprTabelleBezeichnung(
										ArtikelkommentarFac.FLR_ARTIKELKOMMENTARART_ARTIKELKOMMENTARARTSPRSET));
		panelQueryFLRKommentarart.setSelectedId(selectedId);
		return panelQueryFLRKommentarart;

	}

	public FilterKriterium[] createFKShopgruppeMandantCNr(
			Integer[] ausgeschlosseneIds) throws Throwable {

		if (ausgeschlosseneIds != null && ausgeschlosseneIds.length > 0) {
			FilterKriterium[] kriterien = new FilterKriterium[2];
			kriterien[0] = new FilterKriterium("shopgruppe.mandant_c_nr", true,
					"'" + LPMain.getInstance().getTheClient().getMandant()
							+ "'", FilterKriterium.OPERATOR_EQUAL, false);
			String notIn = "(";
			for (int i = 0; i < ausgeschlosseneIds.length; i++) {
				notIn += ausgeschlosseneIds[i];

				if (i != ausgeschlosseneIds.length - 1) {
					notIn += ",";
				}
			}
			notIn += ")";
			kriterien[1] = new FilterKriterium("shopgruppe.i_id", true, notIn,
					FilterKriterium.OPERATOR_NOT_IN, false);
			return kriterien;
		} else {
			FilterKriterium[] kriterien = new FilterKriterium[1];
			kriterien[0] = new FilterKriterium("shopgruppe.mandant_c_nr", true,
					"'" + LPMain.getInstance().getTheClient().getMandant()
							+ "'", FilterKriterium.OPERATOR_EQUAL, false);
			return kriterien;
		}

	}

	public PanelQueryFLR createPanelFLRShopgruppe(InternalFrame internalFrameI,
			Integer selectedId, boolean bShowLeerenButton,
			Integer[] ausgeschlosseneIds) throws Throwable {

		String[] aWhichButtonIUse;
		if (bShowLeerenButton) {
			aWhichButtonIUse = new String[] { PanelBasis.ACTION_REFRESH,
					PanelBasis.ACTION_LEEREN };
		} else {
			aWhichButtonIUse = new String[] { PanelBasis.ACTION_REFRESH };
		}

		PanelQueryFLR panelQuery = new PanelQueryFLR(null,
				createFKShopgruppeMandantCNr(ausgeschlosseneIds),
				QueryParameters.UC_ID_SHOPGRUPPE, aWhichButtonIUse,
				internalFrameI, LPMain.getTextRespectUISPr("lp.shopgruppe"));

		panelQuery.befuellePanelFilterkriterienDirekt(
				new FilterKriteriumDirekt("shopgruppe.c_nr", "",
						FilterKriterium.OPERATOR_LIKE, LPMain.getInstance()
								.getTextRespectUISPr("lp.shopgruppe"),
						FilterKriteriumDirekt.PROZENT_TRAILING, true, true,
						Facade.MAX_UNBESCHRAENKT), new FilterKriteriumDirekt(
						"shopgruppesprset.c_bez", "",
						FilterKriterium.OPERATOR_LIKE, LPMain.getInstance()
								.getTextRespectUISPr("lp.bezeichnung"),
						FilterKriteriumDirekt.PROZENT_BOTH, true, true,
						Facade.MAX_UNBESCHRAENKT));

		panelQuery.setSelectedId(selectedId);

		return panelQuery;
	}

	public PanelQueryFLR createPanelFLRLagerplatz(InternalFrame internalFrameI,
			Integer selectedId, boolean bMitLeerenButton) throws Throwable {

		String[] aWhichButtonIUse = null;
		if (bMitLeerenButton) {
			aWhichButtonIUse = new String[] { PanelBasis.ACTION_REFRESH,
					PanelBasis.ACTION_LEEREN, PanelBasis.ACTION_NEW };
		} else {
			aWhichButtonIUse = new String[] { PanelBasis.ACTION_REFRESH,
					PanelBasis.ACTION_NEW };
		}

		PanelQueryFLR panelQueryFLRLagerplatz = new PanelQueryFLRLagerplatzAnlegen(
				ArtikelFilterFactory.getInstance().createQTMaterial(),
				ArtikelFilterFactory.getInstance().createFKLagerplatz(),
				aWhichButtonIUse, internalFrameI, LPMain.getInstance()
						.getTextRespectUISPr("lp.lagerplatz"));

		panelQueryFLRLagerplatz.befuellePanelFilterkriterienDirekt(
				ArtikelFilterFactory.getInstance().createFKDLagerplatz(), null);

		if (selectedId != null) {
			panelQueryFLRLagerplatz.setSelectedId(selectedId);
		}

		return panelQueryFLRLagerplatz;

	}

	public PanelQueryFLR createPanelFLRLager_MitLeerenButton(
			InternalFrame internalFrameI) throws Throwable {
		String[] aWhichButtonIUse = { PanelBasis.ACTION_REFRESH,
				PanelBasis.ACTION_FILTER, PanelBasis.ACTION_LEEREN };

		FilterKriterium[] fk = ArtikelFilterFactory.getInstance()
				.createFKLagerliste();

		PanelQueryFLR panelQueryFLRLager = new PanelQueryFLR(createQTLager(),
				fk, QueryParameters.UC_ID_LAGER, aWhichButtonIUse,
				internalFrameI, LPMain.getInstance().getTextRespectUISPr(
						"title.lagerauswahlliste"));
		panelQueryFLRLager.befuellePanelFilterkriterienDirekt(
				createFKDLagername(), createFKDLagerLagerart());

		return panelQueryFLRLager;
	}

	protected QueryType[] createQTLager() {
		QueryType[] types = new QueryType[2];

		FilterKriterium f1 = new FilterKriterium("c_nr", true, "",
				FilterKriterium.OPERATOR_LIKE, true);

		types[0] = new QueryType(LPMain.getInstance().getTextRespectUISPr(
				"label.lager"), f1,
				new String[] { FilterKriterium.OPERATOR_EQUAL }, true, true);
		FilterKriterium f2 = new FilterKriterium(
				LagerFac.FLR_LAGER_LAGERART_C_NR, true, "",
				FilterKriterium.OPERATOR_LIKE, true);

		types[1] = new QueryType(LPMain.getInstance().getTextRespectUISPr(
				"artikel.lagerart"), f2,
				new String[] { FilterKriterium.OPERATOR_EQUAL }, true, true);

		return types;
	}

	protected QueryType[] createQTArtikelauswahl() {
		QueryType[] types = new QueryType[2];

		FilterKriterium f1 = new FilterKriterium(
				"artikelliste.flrartikelgruppe.c_nr", true, "",
				FilterKriterium.OPERATOR_LIKE, true);

		types[0] = new QueryType(LPMain.getInstance().getTextRespectUISPr(
				"lp.artikelgruppe"), f1,
				new String[] { FilterKriterium.OPERATOR_EQUAL }, true, true);
		FilterKriterium f2 = new FilterKriterium(
				"artikelliste.flrartikelklasse.c_nr", true, "",
				FilterKriterium.OPERATOR_LIKE, true);

		types[1] = new QueryType(LPMain.getInstance().getTextRespectUISPr(
				"lp.artikelklasse"), f2,
				new String[] { FilterKriterium.OPERATOR_EQUAL }, true, true);
		return types;
	}

	protected QueryType[] createQTHersteller() {
		QueryType[] types = new QueryType[2];

		FilterKriterium f1 = new FilterKriterium("c_nr", true, "",
				FilterKriterium.OPERATOR_LIKE, true);

		types[0] = new QueryType(LPMain.getInstance().getTextRespectUISPr(
				"lp.hersteller"), f1,
				new String[] { FilterKriterium.OPERATOR_EQUAL }, true, true);
		FilterKriterium f2 = new FilterKriterium(
				ArtikelFac.FLR_HERSTELLER_FLRPARTNER + "."
						+ PartnerFac.FLR_PARTNER_NAME1NACHNAMEFIRMAZEILE1,
				true, "", FilterKriterium.OPERATOR_LIKE, true);

		types[1] = new QueryType(LPMain.getInstance().getTextRespectUISPr(
				"lp.name"), f2,
				new String[] { FilterKriterium.OPERATOR_EQUAL }, true, true);

		return types;
	}

	public FilterKriteriumDirekt createFKDHersteller() throws Throwable {
		return new FilterKriteriumDirekt("c_nr", "",
				FilterKriterium.OPERATOR_LIKE, LPMain.getInstance()
						.getTextRespectUISPr("lp.hersteller"),
				FilterKriteriumDirekt.PROZENT_TRAILING, true, true,
				Facade.MAX_UNBESCHRAENKT);

	}

	public FilterKriteriumDirekt createFKDArtikellieferantPartner()
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
					ArtikelFac.FLR_ARTIKELLIEFERANT_FLRLIEFERANT + "."
							+ LieferantFac.FLR_PARTNER_NAME1NACHNAMEFIRMAZEILE1,
					"", FilterKriterium.OPERATOR_LIKE, LPMain.getInstance()
							.getTextRespectUISPr("lp.firma"),
					FilterKriteriumDirekt.PROZENT_BOTH, true, true,
					Facade.MAX_UNBESCHRAENKT); // wrapWithSingleQuotes
		} else {
			fKDPartnername = new FilterKriteriumDirekt(
					ArtikelFac.FLR_ARTIKELLIEFERANT_FLRLIEFERANT + "."
							+ LieferantFac.FLR_PARTNER_NAME1NACHNAMEFIRMAZEILE1,
					"", FilterKriterium.OPERATOR_LIKE, LPMain.getInstance()
							.getTextRespectUISPr("lp.firma"),
					FilterKriteriumDirekt.PROZENT_TRAILING, true, true,
					Facade.MAX_UNBESCHRAENKT); // wrapWithSingleQuotes

		}
		return fKDPartnername;
	}

	public FilterKriteriumDirekt createFKDArtikellieferantPartnerOrt() {

		FilterKriteriumDirekt fKDPartnerOrt = new FilterKriteriumDirekt(
				ArtikelFac.FLR_ARTIKELLIEFERANT_FLRLIEFERANT + "."
						+ LieferantFac.FLR_PARTNER_LANDPLZORT_ORT_NAME, "",
				FilterKriterium.OPERATOR_LIKE, LPMain.getInstance()
						.getTextRespectUISPr("lp.ort"),
				FilterKriteriumDirekt.PROZENT_BOTH, true, // wrapWithSingleQuotes
				true, Facade.MAX_UNBESCHRAENKT);
		return fKDPartnerOrt;
	}

	public FilterKriteriumDirekt createFKDHerstellerPartner() throws Throwable {
		return new FilterKriteriumDirekt(ArtikelFac.FLR_HERSTELLER_FLRPARTNER
				+ "." + PartnerFac.FLR_PARTNER_NAME1NACHNAMEFIRMAZEILE1, "",
				FilterKriterium.OPERATOR_LIKE, LPMain.getInstance()
						.getTextRespectUISPr("lp.name"),
				FilterKriteriumDirekt.PROZENT_TRAILING, true, true,
				Facade.MAX_UNBESCHRAENKT);
	}

	public FilterKriterium[] createFKArtikellager(Integer artikelIId)
			throws Throwable {
		FilterKriterium[] kriterien = null;

		if (LPMain
				.getInstance()
				.getDesktop()
				.darfAnwenderAufZusatzfunktionZugreifen(
						MandantFac.ZUSATZFUNKTION_ZENTRALER_ARTIKELSTAMM)
				&& !LPMain
						.getInstance()
						.getDesktop()
						.darfAnwenderAufZusatzfunktionZugreifen(
								MandantFac.ZUSATZFUNKTION_GETRENNTE_LAGER)) {

			kriterien = new FilterKriterium[2];

			FilterKriterium krit1 = new FilterKriterium(
					LagerFac.FLR_ARTIKELLAGER_ARTIKEL_I_ID, true, artikelIId
							+ "", FilterKriterium.OPERATOR_EQUAL, false);
			kriterien[0] = krit1;

			krit1 = new FilterKriterium("flrlager.lagerart_c_nr", true, "'"
					+ LagerFac.LAGERART_WERTGUTSCHRIFT + "'",
					FilterKriterium.OPERATOR_NOT_EQUAL, false);
			kriterien[1] = krit1;

		} else {

			kriterien = new FilterKriterium[3];
			FilterKriterium krit1 = new FilterKriterium(
					LagerFac.FLR_ARTIKELLAGER_ARTIKEL_I_ID, true, artikelIId
							+ "", FilterKriterium.OPERATOR_EQUAL, false);
			kriterien[0] = krit1;
			krit1 = new FilterKriterium("mandant_c_nr", true, "'"
					+ LPMain.getInstance().getTheClient().getMandant() + "'",
					FilterKriterium.OPERATOR_EQUAL, false);
			kriterien[1] = krit1;
			krit1 = new FilterKriterium("flrlager.lagerart_c_nr", true, "'"
					+ LagerFac.LAGERART_WERTGUTSCHRIFT + "'",
					FilterKriterium.OPERATOR_NOT_EQUAL, false);
			kriterien[2] = krit1;

		}

		return kriterien;
	}

	public FilterKriterium[] createFKInventurprotkoll(Integer inventurIId)
			throws Throwable {
		FilterKriterium[] kriterien = new FilterKriterium[1];
		FilterKriterium krit1 = new FilterKriterium(
				InventurFac.FLR_INVENTURPROTOKOLL_FLRINVENTUR + ".i_id", true,
				inventurIId + "", FilterKriterium.OPERATOR_EQUAL, false);
		kriterien[0] = krit1;
		return kriterien;
	}

	public FilterKriterium[] createFKPaternosterLager(Integer lagerIId)
			throws Throwable {
		FilterKriterium[] kriterien = new FilterKriterium[1];
		FilterKriterium krit1 = new FilterKriterium("flrlager.i_id", true,
				lagerIId + "", FilterKriterium.OPERATOR_EQUAL, false);
		kriterien[0] = krit1;
		return kriterien;
	}

	public FilterKriterium[] createFKInventurstand(Integer inventurIId)
			throws Throwable {
		FilterKriterium[] kriterien = new FilterKriterium[1];
		FilterKriterium krit1 = new FilterKriterium(
				InventurFac.FLR_INVENTURPROTOKOLL_FLRINVENTUR + ".i_id", true,
				inventurIId + "", FilterKriterium.OPERATOR_EQUAL, false);
		kriterien[0] = krit1;
		return kriterien;
	}

	public FilterKriterium[] createFKArtikellagerplaetze(Integer artikelIId)
			throws Throwable {
		FilterKriterium[] kriterien = new FilterKriterium[1];
		FilterKriterium krit1 = new FilterKriterium(
				LagerFac.FLR_LAGERPLAETZE_ARTIKEL_I_ID, true, artikelIId + "",
				FilterKriterium.OPERATOR_EQUAL, false);
		kriterien[0] = krit1;

		return kriterien;
	}

	public QueryType[] createQTMaterial() {
		QueryType[] types = new QueryType[1];

		FilterKriterium f1 = new FilterKriterium("c_nr", true, "",
				FilterKriterium.OPERATOR_LIKE, true);

		types[0] = new QueryType(LPMain.getInstance().getTextRespectUISPr(
				"lp.material"), f1,
				new String[] { FilterKriterium.OPERATOR_EQUAL }, true, true);

		return types;
	}

	public QueryType[] createQTInventurliste() {
		QueryType[] types = new QueryType[2];

		FilterKriterium f1 = new FilterKriterium(
				InventurFac.FLR_INVENTURLISTE_FLRARTIKEL + ".c_nr", true, "",
				FilterKriterium.OPERATOR_LIKE, true);

		types[0] = new QueryType(LPMain.getInstance().getTextRespectUISPr(
				"artikel.artikelnummer"), f1,
				new String[] { FilterKriterium.OPERATOR_EQUAL }, true, true);

		FilterKriterium f2 = new FilterKriterium(
				InventurFac.FLR_INVENTURLISTE_FLRLAGER + ".c_nr", true, "",
				FilterKriterium.OPERATOR_LIKE, true);

		types[1] = new QueryType(LPMain.getInstance().getTextRespectUISPr(
				"label.lager"), f2,
				new String[] { FilterKriterium.OPERATOR_EQUAL }, true, true);

		return types;
	}

	public QueryType[] createQTArtikelgruppe() {

		QueryType[] types = new QueryType[2];

		FilterKriterium f1 = new FilterKriterium("c_nr", true, "",
				FilterKriterium.OPERATOR_LIKE, true);

		types[0] = new QueryType(LPMain.getInstance().getTextRespectUISPr(
				"lp.artikelgruppe"), f1,
				new String[] { FilterKriterium.OPERATOR_EQUAL }, true, true);
		FilterKriterium f2 = new FilterKriterium(
				ArtikelFac.FLR_ARTIKELGRUPPE_FLRARTIKELGRUPPE + ".c_nr", true,
				"", FilterKriterium.OPERATOR_LIKE, true);
		types[1] = new QueryType(LPMain.getInstance().getTextRespectUISPr(
				"artikel.vatergruppe"), f2,
				new String[] { FilterKriterium.OPERATOR_EQUAL }, true, true);
		return types;
	}

	public PanelQueryFLR createPanelFLRArtikelgruppe(
			InternalFrame internalFrameI, Integer selectedId) throws Throwable {
		String[] aWhichButtonIUse = { PanelBasis.ACTION_REFRESH,
				PanelBasis.ACTION_FILTER, PanelBasis.ACTION_LEEREN };

		PanelQueryFLR panelQueryFLRArtikelgruppe = new PanelQueryFLR(
				createQTArtikelgruppe(), createFKArtgruMandantCNr(),
				QueryParameters.UC_ID_ARTIKELGRUPPE, aWhichButtonIUse,
				internalFrameI, LPMain.getInstance().getTextRespectUISPr(
						"title.artikelgruppenauswahlliste"));
		panelQueryFLRArtikelgruppe
				.befuellePanelFilterkriterienDirekt(
						ArtikelFilterFactory.getInstance()
								.createFKDArtikelgruppeKennung(),
						SystemFilterFactory
								.getInstance()
								.createFKDSprTabelleBezeichnung(
										ArtikelFac.FLR_ARTIKELGRUPPE_ARTIKELGRUPPESPRSET));
		if (selectedId != null) {
			panelQueryFLRArtikelgruppe.setSelectedId(selectedId);
		}

		return panelQueryFLRArtikelgruppe;
	}

	public PanelQueryFLR createPanelFLRVorschlagstext(
			InternalFrame internalFrameI) throws Throwable {
		String[] aWhichButtonIUse = { PanelBasis.ACTION_REFRESH,
				PanelBasis.ACTION_FILTER, PanelBasis.ACTION_LEEREN };

		PanelQueryFLR panelQueryFLRVorschlagstexte = new PanelQueryFLR(null,
				createFKVorschlagstext(), QueryParameters.UC_ID_VORSCHLAGSTEXT,
				aWhichButtonIUse, internalFrameI, LPMain.getInstance()
						.getTextRespectUISPr("artikel.vorschlagstext"));
		panelQueryFLRVorschlagstexte.befuellePanelFilterkriterienDirekt(
				SystemFilterFactory.getInstance().createFKDBezeichnung(), null);

		return panelQueryFLRVorschlagstexte;
	}

	public QueryType[] createQTArtikelklasse() {
		QueryType[] types = new QueryType[2];

		FilterKriterium f1 = new FilterKriterium("c_nr", true, "",
				FilterKriterium.OPERATOR_LIKE, true);

		types[0] = new QueryType(LPMain.getInstance().getTextRespectUISPr(
				"lp.artikelklasse"), f1,
				new String[] { FilterKriterium.OPERATOR_EQUAL }, true, true);
		FilterKriterium f2 = new FilterKriterium(
				ArtikelFac.FLR_ARTIKELKLASSE_FLRARTIKELKLASSE + ".c_nr", true,
				"", FilterKriterium.OPERATOR_LIKE, true);

		types[1] = new QueryType(LPMain.getInstance().getTextRespectUISPr(
				"artikel.vaterklasse"), f2,
				new String[] { FilterKriterium.OPERATOR_EQUAL }, true, true);

		return types;
	}

	public QueryType[] createQTArtikelkommentarart() {
		QueryType[] types = new QueryType[1];

		FilterKriterium f1 = new FilterKriterium("c_nr", true, "",
				FilterKriterium.OPERATOR_LIKE, true);

		types[0] = new QueryType(LPMain.getInstance().getTextRespectUISPr(
				"lp.kommentarart"), f1,
				new String[] { FilterKriterium.OPERATOR_EQUAL }, true, true);

		return types;
	}

	public QueryType[] createQTLagerplatz() {
		QueryType[] types = new QueryType[1];

		FilterKriterium f1 = new FilterKriterium("c_lagerplatz", true, "",
				FilterKriterium.OPERATOR_LIKE, true);

		types[0] = new QueryType(LPMain.getInstance().getTextRespectUISPr(
				"lp.lagerplatz"), f1,
				new String[] { FilterKriterium.OPERATOR_EQUAL }, true, true);

		return types;
	}

	public FilterKriterium[] createFKArtgruMandantCNr() throws Throwable {
		FilterKriterium[] kriterien = new FilterKriterium[1];

		if (LPMain
				.getInstance()
				.getDesktop()
				.darfAnwenderAufZusatzfunktionZugreifen(
						MandantFac.ZUSATZFUNKTION_ZENTRALER_ARTIKELSTAMM)) {

			kriterien[0] = new FilterKriterium("artikelgruppe.mandant_c_nr",
					true, "'"
							+ DelegateFactory.getInstance().getSystemDelegate()
									.getHauptmandant() + "'",
					FilterKriterium.OPERATOR_EQUAL, false);
		} else {
			kriterien[0] = new FilterKriterium("artikelgruppe.mandant_c_nr",
					true, "'"
							+ LPMain.getInstance().getTheClient().getMandant()
							+ "'", FilterKriterium.OPERATOR_EQUAL, false);
		}
		return kriterien;
	}

	public FilterKriterium[] createFKArtklaMandantCNr() throws Throwable {
		FilterKriterium[] kriterien = new FilterKriterium[1];

		if (LPMain
				.getInstance()
				.getDesktop()
				.darfAnwenderAufZusatzfunktionZugreifen(
						MandantFac.ZUSATZFUNKTION_ZENTRALER_ARTIKELSTAMM)) {

			kriterien[0] = new FilterKriterium("artikelklasse.mandant_c_nr",
					true, "'"
							+ DelegateFactory.getInstance().getSystemDelegate()
									.getHauptmandant() + "'",
					FilterKriterium.OPERATOR_EQUAL, false);
		} else {
			kriterien[0] = new FilterKriterium("artikelklasse.mandant_c_nr",
					true, "'"
							+ LPMain.getInstance().getTheClient().getMandant()
							+ "'", FilterKriterium.OPERATOR_EQUAL, false);
		}

		return kriterien;
	}

	public PanelQueryFLR createPanelFLRArtikelklasse(
			InternalFrame internalFrameI, Integer selectedId) throws Throwable {
		String[] aWhichButtonIUse = { PanelBasis.ACTION_REFRESH,
				PanelBasis.ACTION_FILTER, PanelBasis.ACTION_LEEREN };

		PanelQueryFLR panelQueryFLRArtikelklasse = new PanelQueryFLR(
				createQTArtikelklasse(), createFKArtklaMandantCNr(),
				QueryParameters.UC_ID_ARTIKELKLASSE, aWhichButtonIUse,
				internalFrameI, LPMain.getInstance().getTextRespectUISPr(
						"title.artikelklassenauswahlliste"));
		panelQueryFLRArtikelklasse
				.befuellePanelFilterkriterienDirekt(
						ArtikelFilterFactory.getInstance()
								.createFKDArtikelklasseKennung(),
						SystemFilterFactory
								.getInstance()
								.createFKDSprTabelleBezeichnung(
										ArtikelFac.FLR_ARTIKELKLASSE_ARTIKELKLASSESPRSET));

		if (selectedId != null) {
			panelQueryFLRArtikelklasse.setSelectedId(selectedId);
		}

		return panelQueryFLRArtikelklasse;
	}

	public PanelQueryFLR createPanelFLRArtikel(InternalFrame internalFrameI,
			boolean bMitLeerenButton) throws Throwable {
		return createPanelFLRArtikel(internalFrameI, null, bMitLeerenButton);
	}

	public PanelQueryFLR createPanelFLRArtikel(InternalFrame internalFrameI,
			Integer selectedId, boolean bMitLeerenButton) throws Throwable {
		String[] aWhichButtonIUse = null;
		if (bMitLeerenButton) {
			aWhichButtonIUse = new String[] { PanelBasis.ACTION_LEEREN };
		}

		PanelQueryFLR panelQueryArtikel = new PanelQueryFLR(null,
				ArtikelFilterFactory.getInstance().createFKArtikelliste(),
				QueryParameters.UC_ID_ARTIKELLISTE, aWhichButtonIUse,
				internalFrameI, LPMain.getInstance().getTextRespectUISPr(
						"title.artikelauswahlliste"), createFKVArtikel());

		panelQueryArtikel.befuellePanelFilterkriterienDirekt(
				createFKDArtikelnummer(internalFrameI),
				createFKDVolltextsuche());
		panelQueryArtikel.addDirektFilter(ArtikelFilterFactory.getInstance()
				.createFKDLieferantennrBezeichnung());

		panelQueryArtikel.setFilterComboBox(DelegateFactory.getInstance()
				.getArtikelDelegate().getAllSprArtgru(),
				new FilterKriterium("ag.i_id", true, "" + "",
						FilterKriterium.OPERATOR_EQUAL, false), false, LPMain
						.getTextRespectUISPr("lp.alle"));

		ParametermandantDto parameter = DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getMandantparameter(
						LPMain.getInstance().getTheClient().getMandant(),
						ParameterFac.KATEGORIE_ARTIKEL,
						ParameterFac.PARAMETER_DIREKTFILTER_GRUPPE_KLASSE_STATT_REFERENZNUMMER);
		boolean bDirektfilterAGAKStattReferenznummer = (java.lang.Boolean) parameter
				.getCWertAsObject();

		if (bDirektfilterAGAKStattReferenznummer) {
			panelQueryArtikel.addDirektFilter(ArtikelFilterFactory
					.getInstance().createFKDAKAG());
		} else {
			panelQueryArtikel.addDirektFilter(ArtikelFilterFactory
					.getInstance().createFKDReferenznr());
		}
		if (selectedId != null) {
			panelQueryArtikel.setSelectedId(selectedId);
		}
		return panelQueryArtikel;
	}

	public PanelQueryFLR createPanelFLRArtikelOhneArbeitszeit(
			InternalFrame internalFrameI, Integer selectedId,
			boolean bMitLeerenButton) throws Throwable {
		String[] aWhichButtonIUse = null;
		if (bMitLeerenButton) {
			aWhichButtonIUse = new String[] { PanelBasis.ACTION_LEEREN };
		}

		PanelQueryFLR panelQueryArtikel = new PanelQueryFLR(null,
				ArtikelFilterFactory.getInstance()
						.createFKArtikellisteOhneArbeitszeit(),
				QueryParameters.UC_ID_ARTIKELLISTE, aWhichButtonIUse,
				internalFrameI, LPMain.getInstance().getTextRespectUISPr(
						"title.artikelauswahlliste"), createFKVArtikel());

		panelQueryArtikel.befuellePanelFilterkriterienDirekt(
				createFKDArtikelnummer(internalFrameI),
				createFKDVolltextsuche());
		panelQueryArtikel.setFilterComboBox(DelegateFactory.getInstance()
				.getArtikelDelegate().getAllSprArtgru(),
				new FilterKriterium("ag.i_id", true, "" + "",
						FilterKriterium.OPERATOR_EQUAL, false), false, LPMain
						.getTextRespectUISPr("lp.alle"));

		panelQueryArtikel.addDirektFilter(ArtikelFilterFactory.getInstance()
				.createFKDLieferantennrBezeichnung());
		ParametermandantDto parameter = DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getMandantparameter(
						LPMain.getInstance().getTheClient().getMandant(),
						ParameterFac.KATEGORIE_ARTIKEL,
						ParameterFac.PARAMETER_DIREKTFILTER_GRUPPE_KLASSE_STATT_REFERENZNUMMER);
		boolean bDirektfilterAGAKStattReferenznummer = (java.lang.Boolean) parameter
				.getCWertAsObject();

		if (bDirektfilterAGAKStattReferenznummer) {
			panelQueryArtikel.addDirektFilter(ArtikelFilterFactory
					.getInstance().createFKDAKAG());
		} else {
			panelQueryArtikel.addDirektFilter(ArtikelFilterFactory
					.getInstance().createFKDReferenznr());
		}
		if (selectedId != null) {
			panelQueryArtikel.setSelectedId(selectedId);
		}
		return panelQueryArtikel;
	}

	public PanelQueryFLR createPanelFLRArtikelEinerShopgruppe(
			InternalFrame internalFrameI, Integer shopgruppeId,
			Integer selectedId, boolean bMitLeerenButton) throws Throwable {
		String[] aWhichButtonIUse = null;
		if (bMitLeerenButton) {
			aWhichButtonIUse = new String[] { PanelBasis.ACTION_LEEREN };
		}

		// PanelQueryFLR panelQueryArtikel = new PanelQueryFLR(null,
		// ArtikelFilterFactory.getInstance()
		// .createFKArtikellisteEinerShopgruppe(shopgruppeId),
		// QueryParameters.UC_ID_ARTIKELLISTE, aWhichButtonIUse,
		// internalFrameI, LPMain.getInstance().getTextRespectUISPr(
		// "title.artikelauswahlliste"), createFKVArtikel());

		PanelQueryFLRArtikelShopgruppe panelQueryArtikel = new PanelQueryFLRArtikelShopgruppe(
				null, ArtikelFilterFactory.getInstance()
						.createFKArtikellisteEinerShopgruppe(shopgruppeId),
				QueryParameters.UC_ID_ARTIKELLISTE, aWhichButtonIUse,
				internalFrameI, LPMain.getInstance().getTextRespectUISPr(
						"title.artikelauswahlliste"), createFKVArtikel());

		panelQueryArtikel.befuellePanelFilterkriterienDirekt(
				createFKDArtikelnummer(internalFrameI),
				createFKDVolltextsuche());
		panelQueryArtikel.addDirektFilter(ArtikelFilterFactory.getInstance()
				.createFKDLieferantennrBezeichnung());

		panelQueryArtikel.setFilterComboBox(DelegateFactory.getInstance()
				.getArtikelDelegate().getAllSprArtgru(),
				new FilterKriterium("ag.i_id", true, "" + "",
						FilterKriterium.OPERATOR_EQUAL, false), false, LPMain
						.getTextRespectUISPr("lp.alle"));

		ParametermandantDto parameter = DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getMandantparameter(
						LPMain.getInstance().getTheClient().getMandant(),
						ParameterFac.KATEGORIE_ARTIKEL,
						ParameterFac.PARAMETER_DIREKTFILTER_GRUPPE_KLASSE_STATT_REFERENZNUMMER);
		boolean bDirektfilterAGAKStattReferenznummer = (java.lang.Boolean) parameter
				.getCWertAsObject();

		if (bDirektfilterAGAKStattReferenznummer) {
			panelQueryArtikel.addDirektFilter(ArtikelFilterFactory
					.getInstance().createFKDAKAG());
		} else {
			panelQueryArtikel.addDirektFilter(ArtikelFilterFactory
					.getInstance().createFKDReferenznr());
		}
		if (selectedId != null) {
			panelQueryArtikel.setSelectedId(selectedId);
		}
		return panelQueryArtikel;
	}

	public QueryType[] createQTHandlagerbewegung() {
		QueryType[] types = new QueryType[2];

		FilterKriterium f1 = new FilterKriterium(
				LagerFac.FLR_HANDLAGERBEWEGUNG_FLRARTIKEL + ".c_nr", true, "",
				FilterKriterium.OPERATOR_LIKE, true);
		FilterKriterium f2 = new FilterKriterium(
				LagerFac.FLR_HANDLAGERBEWEGUNG_FLRLAGER + ".c_nr", true, "",
				FilterKriterium.OPERATOR_LIKE, true);

		types[0] = new QueryType(LPMain.getInstance().getTextRespectUISPr(
				"lp.artikel"), f1,
				new String[] { FilterKriterium.OPERATOR_EQUAL }, true, true);
		types[1] = new QueryType(LPMain.getInstance().getTextRespectUISPr(
				"button.lager"), f2,
				new String[] { FilterKriterium.OPERATOR_EQUAL }, true, true);

		return types;

	}

	public FilterKriterium[] createFKAktivepreislisten() throws Throwable {
		FilterKriterium[] kriterien = new FilterKriterium[2];
		FilterKriterium krit1 = new FilterKriterium("mandant_c_nr", true, "'"
				+ LPMain.getInstance().getTheClient().getMandant() + "'",
				FilterKriterium.OPERATOR_EQUAL, false);
		kriterien[0] = krit1;
		FilterKriterium krit2 = new FilterKriterium(
				ArtikelFac.FLR_VKPFARTIKELPREISLISTE_B_PREISLISTEAKTIV, true,
				"1", FilterKriterium.OPERATOR_EQUAL, false);
		kriterien[1] = krit2;
		return kriterien;
	}

	/**
	 * Default Filterkriterien fuer SeriennummernchargennummernAufLagerHandler.
	 * 
	 * 
	 * <br>
	 * Der FLR soll alle Seriennummern zu einem Artikel auf einem bestimmten
	 * Lager enthalten.
	 * 
	 * @param iIdArtikelI
	 *            PK des Artikels
	 * @param iIdLagerI
	 *            PK des gewuenschten Lagers
	 * @return FilterKriterium[]
	 */
	public FilterKriterium[] createFKArtikelSNR(Integer iIdArtikelI,
			Integer iIdLagerI) {
		FilterKriterium[] kriterien = new FilterKriterium[2];

		FilterKriterium krit1 = new FilterKriterium(
				LagerFac.FLR_LAGERBEWEGUNG_FLRARTIKEL + ".i_id", true,
				iIdArtikelI.toString(), FilterKriterium.OPERATOR_EQUAL, false);

		FilterKriterium krit2 = new FilterKriterium(
				LagerFac.FLR_LAGERBEWEGUNG_FLRLAGER + ".i_id", true,
				iIdLagerI.toString(), FilterKriterium.OPERATOR_EQUAL, false);

		kriterien[0] = krit1;
		kriterien[1] = krit2;

		return kriterien;
	}

	public FilterKriterium[] createFKShopgruppewebshop(Integer shopgruppeIId)
			throws Throwable {
		FilterKriterium[] kriterien = new FilterKriterium[1];
		kriterien[0] = new FilterKriterium("shopgruppe_i_id", true,
				shopgruppeIId + "", FilterKriterium.OPERATOR_EQUAL, false);

		return kriterien;
	}

}
