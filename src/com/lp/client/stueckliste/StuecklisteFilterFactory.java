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
package com.lp.client.stueckliste;

import java.util.Map;

import com.lp.client.artikel.ArtikelFilterFactory;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.util.fastlanereader.gui.QueryType;
import com.lp.server.artikel.service.ArtikelFac;
import com.lp.server.benutzer.service.BenutzerFac;
import com.lp.server.fertigung.service.FertigungFac;
import com.lp.server.stueckliste.service.StuecklisteFac;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.util.Facade;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.FilterKriteriumDirekt;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;

@SuppressWarnings("static-access")
public class StuecklisteFilterFactory {
	private static StuecklisteFilterFactory filterFactory = null;

	private StuecklisteFilterFactory() {
		// Singleton.
	}

	static public StuecklisteFilterFactory getInstance() {
		if (filterFactory == null) {
			filterFactory = new StuecklisteFilterFactory();
		}

		return filterFactory;
	}

	public FilterKriterium[] createFKStuecklisteKey(Integer artikelIId)
			throws Throwable {
		// Handartikel nicht anzeigen
		FilterKriterium[] kriterien = new FilterKriterium[1];
		kriterien[0] = new FilterKriterium("stueckliste.i_id", true, artikelIId
				+ "", FilterKriterium.OPERATOR_EQUAL, false);

		return kriterien;
	}

	public FilterKriterium createFKVStuecklisteArtikel() {
		FilterKriterium fkVersteckt = new FilterKriterium("stueckliste."
				+ StuecklisteFac.FLR_STUECKLISTE_FLRARTIKEL + "."
				+ ArtikelFac.FLR_ARTIKELLISTE_B_VERSTECKT, true, "(1)", // wenn
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

	public FilterKriterium[] createStuecklisteMandantCNr() throws Throwable {
		FilterKriterium[] kriterien = new FilterKriterium[1];
		kriterien[0] = new FilterKriterium("stueckliste.mandant_c_nr", true,
				"'" + LPMain.getInstance().getTheClient().getMandant() + "'",
				FilterKriterium.OPERATOR_EQUAL, false);
		return kriterien;
	}

	public PanelQueryFLR createPanelFLRMontageart(InternalFrame internalFrameI,
			Integer selectedId) throws Throwable {
		String[] aWhichButtonIUse = { PanelBasis.ACTION_REFRESH,
				PanelBasis.ACTION_FILTER };

		PanelQueryFLR panelQueryFLRLager = new PanelQueryFLR(
				createQTMontageart(), com.lp.client.system.SystemFilterFactory
						.getInstance().createFKMandantCNr(),
				QueryParameters.UC_ID_STUECKLISTEMONTAGEART, aWhichButtonIUse,
				internalFrameI, LPMain.getInstance().getTextRespectUISPr(
						"title.montageartauswahlliste"));

		panelQueryFLRLager.setSelectedId(selectedId);

		return panelQueryFLRLager;
	}

	public PanelQueryFLR createPanelFLRFertigungsgruppe(
			InternalFrame internalFrameI, Integer selectedId,
			boolean bMitLeerenButton) throws Throwable {
		String[] aWhichButtonIUse = null;

		if (bMitLeerenButton) {
			aWhichButtonIUse = new String[] { PanelBasis.ACTION_REFRESH,
					PanelBasis.ACTION_LEEREN };
		} else {
			aWhichButtonIUse = new String[] { PanelBasis.ACTION_REFRESH };
		}

		Map mEingeschraenkteFertigungsgruppen = DelegateFactory.getInstance()
				.getStuecklisteDelegate().getEingeschraenkteFertigungsgruppen();

		if (mEingeschraenkteFertigungsgruppen != null) {
			PanelQueryFLR panelQueryFLRFertigungsgruppe = new PanelQueryFLR(
					createQTFertigungsgruppe(),
					createFKFertigungsgruppeeingescharenkt(),
					QueryParameters.UC_ID_FERTIGUNGSGRUPPE_EINGESCHRAENKT,
					aWhichButtonIUse, internalFrameI, LPMain.getInstance()
							.getTextRespectUISPr(
									"title.fertigungsgruppeauswahlliste"));
			if (selectedId != null) {
				panelQueryFLRFertigungsgruppe.setSelectedId(selectedId);
			}
			return panelQueryFLRFertigungsgruppe;
		} else {
			PanelQueryFLR panelQueryFLRFertigungsgruppe = new PanelQueryFLR(
					createQTFertigungsgruppe(),
					com.lp.client.system.SystemFilterFactory.getInstance()
							.createFKMandantCNr(),
					QueryParameters.UC_ID_FERTIGUNGSGRUPPE, aWhichButtonIUse,
					internalFrameI, LPMain.getInstance().getTextRespectUISPr(
							"title.fertigungsgruppeauswahlliste"));
			if (selectedId != null) {
				panelQueryFLRFertigungsgruppe.setSelectedId(selectedId);
			}
			return panelQueryFLRFertigungsgruppe;
		}

	}

	public PanelQueryFLR createPanelFLRFertigungsgruppeAlle(
			InternalFrame internalFrameI, Integer selectedId,
			boolean bMitLeerenButton) throws Throwable {
		String[] aWhichButtonIUse = null;

		if (bMitLeerenButton) {
			aWhichButtonIUse = new String[] { PanelBasis.ACTION_REFRESH,
					PanelBasis.ACTION_LEEREN };
		} else {
			aWhichButtonIUse = new String[] { PanelBasis.ACTION_REFRESH };
		}
		PanelQueryFLR panelQueryFLRFertigungsgruppe = new PanelQueryFLR(
				createQTFertigungsgruppe(), null,
				QueryParameters.UC_ID_FERTIGUNGSGRUPPE, aWhichButtonIUse,
				internalFrameI, LPMain.getInstance().getTextRespectUISPr(
						"title.fertigungsgruppeauswahlliste"));
		if (selectedId != null) {
			panelQueryFLRFertigungsgruppe.setSelectedId(selectedId);
		}
		return panelQueryFLRFertigungsgruppe;
	}

	public PanelQueryFLR createPanelFLREigenschaftart(
			InternalFrame internalFrameI, Integer selectedId) throws Throwable {
		String[] aWhichButtonIUse = { PanelBasis.ACTION_REFRESH,
				PanelBasis.ACTION_FILTER };

		PanelQueryFLR panelQueryFLREigenschaftart = new PanelQueryFLR(
				createQTEigenschaftart(), null,
				QueryParameters.UC_ID_STUECKLISTEEIGENSCHAFTART,
				aWhichButtonIUse, internalFrameI,
				LPMain.getInstance().getTextRespectUISPr(
						"title.eigenschaftartauswahlliste"));
		panelQueryFLREigenschaftart.setSelectedId(selectedId);
		return panelQueryFLREigenschaftart;
	}

	public FilterKriterium[] createFKStueckliste(Integer stuecklisteIIid) {

		FilterKriterium[] kriterien = new FilterKriterium[1];
		FilterKriterium krit1 = new FilterKriterium(
				StuecklisteFac.FLR_STUECKLISTEPOSITION_FLRSTUECKLISTE + ".i_id",
				true, stuecklisteIIid + "", FilterKriterium.OPERATOR_EQUAL,
				false);
		kriterien[0] = krit1;
		return kriterien;

	}

	public FilterKriterium[] createFKFertigungsgruppeeingescharenkt()
			throws Throwable {

		FilterKriterium[] kriterien = new FilterKriterium[2];
		FilterKriterium krit1 = new FilterKriterium(
				BenutzerFac.FLR_FERTIGUNGSGRUPPEROLLE_FLRSYSTEMROLLE + ".i_id",
				true, LPMain.getInstance().getTheClient().getSystemrolleIId()
						+ "", FilterKriterium.OPERATOR_EQUAL, false);
		kriterien[0] = krit1;
		FilterKriterium krit2 = new FilterKriterium(
				BenutzerFac.FLR_FERTIGUNGSGRUPPEROLLE_FLRFERTIGUNGSGRUPPE
						+ ".mandant_c_nr", true, "'"
						+ LPMain.getInstance().getTheClient().getMandant()
						+ "'", FilterKriterium.OPERATOR_EQUAL, false);
		kriterien[1] = krit2;
		return kriterien;

	}

	public FilterKriterium[] createFKStuecklistePosersatz(
			Integer stuecklistepositionIIid) {

		FilterKriterium[] kriterien = new FilterKriterium[1];
		FilterKriterium krit1 = new FilterKriterium(
				StuecklisteFac.FLR_POSERSATZ_FLRSTUECKLISTEPOSITION + ".i_id",
				true, stuecklistepositionIIid + "",
				FilterKriterium.OPERATOR_EQUAL, false);
		kriterien[0] = krit1;
		return kriterien;

	}

	public FilterKriteriumDirekt createFKDArtikelnummer() throws Throwable {

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

		return new FilterKriteriumDirekt("stueckliste."
				+ StuecklisteFac.FLR_STUECKLISTE_FLRARTIKEL + ".c_nr", "",
				FilterKriterium.OPERATOR_LIKE, LPMain.getInstance()
						.getTextRespectUISPr("artikel.artikelnummer"),
				FilterKriteriumDirekt.PROZENT_TRAILING, true, true, iLaenge);

	}
	public FilterKriteriumDirekt createFKDErweiterteTextsuche() throws Throwable {

		

		return new FilterKriteriumDirekt("erweiterte_textsuche", "",
				FilterKriterium.OPERATOR_LIKE, LPMain.getInstance()
						.getTextRespectUISPr("stkl.erweiterte.textsuche"),
				FilterKriteriumDirekt.PROZENT_BOTH, true, true, Facade.MAX_UNBESCHRAENKT);

	}

	public FilterKriteriumDirekt createFKDBezeichnung() throws Throwable {

		return new FilterKriteriumDirekt("aspr.c_bez", "",
				FilterKriterium.OPERATOR_LIKE, LPMain.getInstance()
						.getTextRespectUISPr("lp.bezeichnung"),
				FilterKriteriumDirekt.PROZENT_BOTH, true, true,
				Facade.MAX_UNBESCHRAENKT);

	}

	public FilterKriteriumDirekt createFKDBezeichnungAllgemein()
			throws Throwable {

		return new FilterKriteriumDirekt("c_bez", "",
				FilterKriterium.OPERATOR_LIKE, LPMain.getInstance()
						.getTextRespectUISPr("lp.bezeichnung"),
				FilterKriteriumDirekt.PROZENT_TRAILING, true, true,
				Facade.MAX_UNBESCHRAENKT);

	}

	public QueryType[] createQTStuekliseeigenschaftart() {
		QueryType[] types = new QueryType[1];

		FilterKriterium f1 = new FilterKriterium(
				StuecklisteFac.FLR_STUECKLISTEEIGENSCHAFTART_C_BEZ, true, "",
				FilterKriterium.OPERATOR_LIKE, true);

		types[0] = new QueryType(LPMain.getInstance().getTextRespectUISPr(
				"stkl.stuecklisteeigenschaftart"), f1,
				new String[] { FilterKriterium.OPERATOR_EQUAL }, true, true);

		return types;
	}

	public QueryType[] createQTMontageart() {
		QueryType[] types = new QueryType[1];

		FilterKriterium f1 = new FilterKriterium(
				StuecklisteFac.FLR_MONTAGEART_C_BEZ, true, "",
				FilterKriterium.OPERATOR_LIKE, true);

		types[0] = new QueryType(LPMain.getInstance().getTextRespectUISPr(
				"lp.bezeichnung"), f1,
				new String[] { FilterKriterium.OPERATOR_EQUAL }, true, true);

		return types;
	}

	public QueryType[] createQTFertigungsgruppe() {
		QueryType[] types = new QueryType[1];

		FilterKriterium f1 = new FilterKriterium("c_bez", true, "",
				FilterKriterium.OPERATOR_LIKE, true);

		types[0] = new QueryType(LPMain.getInstance().getTextRespectUISPr(
				"lp.bezeichnung"), f1,
				new String[] { FilterKriterium.OPERATOR_EQUAL }, true, true);

		return types;
	}

	public QueryType[] createQTEigenschaftart() {
		QueryType[] types = new QueryType[1];

		FilterKriterium f1 = new FilterKriterium(
				StuecklisteFac.FLR_STUECKLISTEEIGENSCHAFTART_C_BEZ, true, "",
				FilterKriterium.OPERATOR_LIKE, true);

		types[0] = new QueryType(LPMain.getInstance().getTextRespectUISPr(
				"lp.bezeichnung"), f1,
				new String[] { FilterKriterium.OPERATOR_EQUAL }, true, true);

		return types;
	}

	public PanelQueryFLR createPanelFLRStuecklisteposition(
			InternalFrame internalFrameI, Integer stuecklisteIId,
			Integer stuecklistepositionIId) throws Throwable {
		String[] aWhichButtonIUse = { PanelBasis.ACTION_REFRESH,
				PanelBasis.ACTION_LEEREN };
		FilterKriterium[] krit = new FilterKriterium[1];
		krit[0] = new FilterKriterium(
				StuecklisteFac.FLR_STUECKLISTEPOSITION_STUECKLISTE_I_ID, true,
				stuecklisteIId.toString() + "", FilterKriterium.OPERATOR_EQUAL,
				false);
		PanelQueryFLR panelQueryFLRStuecklisteposition = new PanelQueryFLR(
				null, krit, QueryParameters.UC_ID_STUECKLISTEPOSITION,
				aWhichButtonIUse, internalFrameI,
				LPMain.getTextRespectUISPr("lp.positionen"));
		if (stuecklistepositionIId != null) {
			panelQueryFLRStuecklisteposition
					.setSelectedId(stuecklistepositionIId);
		}
		return panelQueryFLRStuecklisteposition;
	}

	public PanelQueryFLR createPanelFLRStueckliste(
			InternalFrame internalFrameI, Integer selectedId,
			boolean bMitLeerenButton) throws Throwable {
		String[] aWhichButtonIUse = null;

		if (bMitLeerenButton) {
			aWhichButtonIUse = new String[] { PanelBasis.ACTION_LEEREN };
		}

		PanelQueryFLR panelQueryFLRStueckliste = new PanelQueryFLR(null,
				StuecklisteFilterFactory.getInstance()
						.createStuecklisteMandantCNr(),
				QueryParameters.UC_ID_STUECKLISTE, aWhichButtonIUse,
				internalFrameI,
				LPMain.getTextRespectUISPr("title.stuecklisteauswahlliste"));
		if (selectedId != null) {
			panelQueryFLRStueckliste.setSelectedId(selectedId);
		}
		panelQueryFLRStueckliste
				.befuellePanelFilterkriterienDirektUndVersteckte(
						StuecklisteFilterFactory.getInstance()
								.createFKDArtikelnummer(), ArtikelFilterFactory
								.getInstance().createFKDVolltextsuche(),
						createFKVStuecklisteArtikel());
		panelQueryFLRStueckliste.addDirektFilter(StuecklisteFilterFactory
				.getInstance().createFKDErweiterteTextsuche());
		return panelQueryFLRStueckliste;
	}

}
