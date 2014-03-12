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
package com.lp.client.instandhaltung;

import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.stueckliste.StuecklisteFilterFactory;
import com.lp.client.system.SystemFilterFactory;
import com.lp.server.partner.service.KundeFac;
import com.lp.server.partner.service.PartnerFac;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.util.Facade;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.FilterKriteriumDirekt;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;

public class InstandhaltungFilterFactory {
	private static InstandhaltungFilterFactory filterFactory = null;

	private InstandhaltungFilterFactory() {
		// Singleton.
	}

	static public InstandhaltungFilterFactory getInstance() {
		if (filterFactory == null) {
			filterFactory = new InstandhaltungFilterFactory();
		}

		return filterFactory;
	}

	public FilterKriterium[] createFKInstandhaltungMandant() throws Throwable {

		FilterKriterium[] krit = new FilterKriterium[1];
		krit[0] = new FilterKriterium("instandhaltung.flrkunde.mandant_c_nr",
				true, "'" + LPMain.getTheClient().getMandant() + "'",
				FilterKriterium.OPERATOR_EQUAL, false);

		return krit;
	}

	public FilterKriterium createFKVGeraet() {
		FilterKriterium fkVersteckt = new FilterKriterium("b_versteckt", true,
				"(1)", // wenn

				FilterKriterium.OPERATOR_NOT_IN, false);

		return fkVersteckt;
	}

	public FilterKriterium createFKVInstandhaltung() {
		FilterKriterium fkVersteckt = new FilterKriterium(
				"instandhaltung.b_versteckt", true, "(1)", // wenn

				FilterKriterium.OPERATOR_NOT_IN, false);

		return fkVersteckt;
	}

	public FilterKriterium createFKVStandort() {
		FilterKriterium fkVersteckt = new FilterKriterium("b_versteckt", true,
				"(1)", // wenn

				FilterKriterium.OPERATOR_NOT_IN, false);

		return fkVersteckt;
	}

	public FilterKriterium[] createFKStandortInstandhaltung(
			Integer instandhaltungIId) throws Throwable {

		FilterKriterium[] krit = new FilterKriterium[1];
		krit[0] = new FilterKriterium("flrinstandhaltung.i_id", true,
				instandhaltungIId + "", FilterKriterium.OPERATOR_EQUAL, false);

		return krit;
	}

	public FilterKriterium[] createFKGeraetehistorie(Integer geraetIId)
			throws Throwable {

		FilterKriterium[] krit = new FilterKriterium[1];
		krit[0] = new FilterKriterium("geraet_i_id", true, geraetIId + "",
				FilterKriterium.OPERATOR_EQUAL, false);

		return krit;
	}

	public FilterKriterium[] createFKStandorttechnikerStandort(Integer standortIId)
			throws Throwable {

		FilterKriterium[] krit = new FilterKriterium[1];
		krit[0] = new FilterKriterium("flrstandort.i_id", true, standortIId
				+ "", FilterKriterium.OPERATOR_EQUAL, false);

		return krit;
	}

	public FilterKriterium[] createFKGeraetStandort(Integer standortIId)
			throws Throwable {

		FilterKriterium[] krit = new FilterKriterium[1];
		krit[0] = new FilterKriterium("flrstandort.i_id", true, standortIId
				+ "", FilterKriterium.OPERATOR_EQUAL, false);

		return krit;
	}

	public FilterKriterium[] createFKAnlagenEinerHalle(Integer halleIId)
			throws Throwable {

		FilterKriterium[] krit = new FilterKriterium[1];
		krit[0] = new FilterKriterium("flrhalle.i_id", true, halleIId + "",
				FilterKriterium.OPERATOR_EQUAL, false);

		return krit;
	}

	public FilterKriterium[] createFKHallenEinesStandorts(Integer standortIId)
			throws Throwable {

		FilterKriterium[] krit = new FilterKriterium[1];
		krit[0] = new FilterKriterium("flrstandort.i_id", true, standortIId
				+ "", FilterKriterium.OPERATOR_EQUAL, false);

		return krit;
	}

	public FilterKriterium[] createFKIsmaschinenEinerAnlage(Integer anlageIId)
			throws Throwable {

		FilterKriterium[] krit = new FilterKriterium[1];
		krit[0] = new FilterKriterium("flranlage.i_id", true, anlageIId + "",
				FilterKriterium.OPERATOR_EQUAL, false);

		return krit;
	}

	public FilterKriterium[] createFKGeraetestuecklisteEinesGeraets(
			Integer geraetIId) throws Throwable {

		FilterKriterium[] krit = new FilterKriterium[1];
		krit[0] = new FilterKriterium("flrgeraet.i_id", true, geraetIId + "",
				FilterKriterium.OPERATOR_EQUAL, false);

		return krit;
	}

	public FilterKriterium[] createFKLoseEinesGeraets(Integer geraetIId)
			throws Throwable {

		FilterKriterium[] krit = new FilterKriterium[1];
		krit[0] = new FilterKriterium("flrgeraet.i_id", true, geraetIId + "",
				FilterKriterium.OPERATOR_EQUAL, false);

		return krit;
	}

	public PanelQueryFLR createPanelFLRHalleNeu(
			InternalFrameInstandhaltung internalFrameI, Integer selectedId,
			boolean bMitLeerenButton) throws Throwable {
		String[] aWhichButtonIUse = null;
		if (bMitLeerenButton) {
			aWhichButtonIUse = new String[] { PanelBasis.ACTION_REFRESH,
					PanelBasis.ACTION_FILTER, PanelBasis.ACTION_LEEREN,
					PanelBasis.ACTION_NEW };
		} else {
			aWhichButtonIUse = new String[] { PanelBasis.ACTION_REFRESH,
					PanelBasis.ACTION_FILTER, PanelBasis.ACTION_NEW };
		}

		PanelQueryFLR panelQueryFLR = new PanelQueryFLRHalleAnlegen(null,
				createFKHallenEinesStandorts(internalFrameI
						.getTabbedPaneInstandhaltung().getStandortDto()
						.getIId()), aWhichButtonIUse, internalFrameI,
				LPMain.getTextRespectUISPr("is.halle"));
		// Direktfilter gleich einbauen
		panelQueryFLR.befuellePanelFilterkriterienDirekt(
				StuecklisteFilterFactory.getInstance()
						.createFKDBezeichnungAllgemein(), null);
		// vorbesetzen falls gewuenscht
		if (selectedId != null) {
			panelQueryFLR.setSelectedId(selectedId);
		}
		return panelQueryFLR;
	}

	public PanelQueryFLR createPanelFLRAnlageNeu(
			InternalFrameInstandhaltung internalFrameI, Integer selectedId,
			boolean bMitLeerenButton, Integer halleIId) throws Throwable {
		String[] aWhichButtonIUse = null;
		if (bMitLeerenButton) {
			aWhichButtonIUse = new String[] { PanelBasis.ACTION_REFRESH,
					PanelBasis.ACTION_FILTER, PanelBasis.ACTION_LEEREN,
					PanelBasis.ACTION_NEW };
		} else {
			aWhichButtonIUse = new String[] { PanelBasis.ACTION_REFRESH,
					PanelBasis.ACTION_FILTER, PanelBasis.ACTION_NEW };
		}

		PanelQueryFLR panelQueryFLR = new PanelQueryFLRAnlageAnlagen(null,
				createFKAnlagenEinerHalle(halleIId), aWhichButtonIUse,
				internalFrameI, LPMain.getTextRespectUISPr("is.anlage"),
				selectedId, halleIId);
		// Direktfilter gleich einbauen
		panelQueryFLR.befuellePanelFilterkriterienDirekt(
				StuecklisteFilterFactory.getInstance()
						.createFKDBezeichnungAllgemein(), null);
		// vorbesetzen falls gewuenscht
		if (selectedId != null) {
			panelQueryFLR.setSelectedId(selectedId);
		}

		Integer i = (Integer) panelQueryFLR.getSelectedId();
		return panelQueryFLR;
	}

	public PanelQueryFLR createPanelFLRMaschineNeu(
			InternalFrameInstandhaltung internalFrameI, Integer selectedId,
			boolean bMitLeerenButton, Integer anlageIId) throws Throwable {
		String[] aWhichButtonIUse = null;
		if (bMitLeerenButton) {
			aWhichButtonIUse = new String[] { PanelBasis.ACTION_REFRESH,
					PanelBasis.ACTION_FILTER, PanelBasis.ACTION_LEEREN,
					PanelBasis.ACTION_NEW };
		} else {
			aWhichButtonIUse = new String[] { PanelBasis.ACTION_REFRESH,
					PanelBasis.ACTION_FILTER, PanelBasis.ACTION_NEW };
		}

		PanelQueryFLR panelQueryFLR = new PanelQueryFLRIsmaschineAnlegen(null,
				createFKIsmaschinenEinerAnlage(anlageIId), aWhichButtonIUse,
				internalFrameI, LPMain.getTextRespectUISPr("is.maschine"),
				selectedId, anlageIId);
		// Direktfilter gleich einbauen
		panelQueryFLR.befuellePanelFilterkriterienDirekt(
				StuecklisteFilterFactory.getInstance()
						.createFKDBezeichnungAllgemein(), null);
		// vorbesetzen falls gewuenscht
		if (selectedId != null) {
			panelQueryFLR.setSelectedId(selectedId);
		}
		return panelQueryFLR;
	}

	public FilterKriteriumDirekt createFKDInstandhaltungName(String sLabel)
			throws Throwable {
		FilterKriteriumDirekt fKDPartnername = null;
		ParametermandantDto parameter = (ParametermandantDto) DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getParametermandant(
						ParameterFac.PARAMETER_PARTNERSUCHE_WILDCARD_BEIDSEITIG,
						ParameterFac.KATEGORIE_PARTNER,
						LPMain.getTheClient().getMandant());
		if (((Boolean) parameter.getCWertAsObject() == true)) {

			fKDPartnername = new FilterKriteriumDirekt(
					"instandhaltung.flrkunde."
							+ KundeFac.FLR_PARTNER_NAME1NACHNAMEFIRMAZEILE1,
					"", FilterKriterium.OPERATOR_LIKE, sLabel,
					FilterKriteriumDirekt.PROZENT_BOTH, true, true,
					Facade.MAX_UNBESCHRAENKT); // wrapWithSingleQuotes
		} else {
			fKDPartnername = new FilterKriteriumDirekt(
					"instandhaltung.flrkunde."
							+ KundeFac.FLR_PARTNER_NAME1NACHNAMEFIRMAZEILE1,
					"", FilterKriterium.OPERATOR_LIKE, sLabel,
					FilterKriteriumDirekt.PROZENT_TRAILING, true, true,
					Facade.MAX_UNBESCHRAENKT); // wrapWithSingleQuotes

		}
		return fKDPartnername;
	}

	public FilterKriteriumDirekt createFKDStandortName(String sLabel)
			throws Throwable {
		FilterKriteriumDirekt fKDPartnername = null;
		ParametermandantDto parameter = (ParametermandantDto) DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getParametermandant(
						ParameterFac.PARAMETER_PARTNERSUCHE_WILDCARD_BEIDSEITIG,
						ParameterFac.KATEGORIE_PARTNER,
						LPMain.getTheClient().getMandant());
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

	public FilterKriteriumDirekt createFKDStandortPartnerOrt() {

		FilterKriteriumDirekt fKDPartnerOrt = new FilterKriteriumDirekt(
				KundeFac.FLR_PARTNER_LANDPLZORT_ORT_NAME, "",
				FilterKriterium.OPERATOR_LIKE,
				LPMain.getTextRespectUISPr("lp.ort"),
				FilterKriteriumDirekt.PROZENT_BOTH, true, true,
				Facade.MAX_UNBESCHRAENKT);
		return fKDPartnerOrt;
	}

	public FilterKriteriumDirekt createFKDNameStrasseStandort() {

		FilterKriteriumDirekt fKDPartnerOrt = new FilterKriteriumDirekt(
				"partner.c_strasse", "", FilterKriterium.OPERATOR_LIKE,
				LPMain.getTextRespectUISPr("is.filter.standortstrasse"),
				FilterKriteriumDirekt.PROZENT_BOTH, true, true,
				Facade.MAX_UNBESCHRAENKT);
		return fKDPartnerOrt;
	}

	public FilterKriteriumDirekt createFKDStandortOrt() {

		FilterKriteriumDirekt fKDPartnerOrt = new FilterKriteriumDirekt(
				"ort.c_name", "", FilterKriterium.OPERATOR_LIKE,
				LPMain.getTextRespectUISPr("is.filter.standortort"),
				FilterKriteriumDirekt.PROZENT_BOTH, true, true,
				Facade.MAX_UNBESCHRAENKT);
		return fKDPartnerOrt;
	}

	public FilterKriteriumDirekt createFKDStandortPartnerStrasse() {

		FilterKriteriumDirekt fKDPartnerOrt = new FilterKriteriumDirekt(
				"flrpartner." + PartnerFac.FLR_PARTNER_C_STRASSE, "",
				FilterKriterium.OPERATOR_LIKE,
				LPMain.getTextRespectUISPr("lp.strasse"),
				FilterKriteriumDirekt.PROZENT_BOTH, true, true,
				Facade.MAX_UNBESCHRAENKT);
		return fKDPartnerOrt;
	}

	public FilterKriteriumDirekt createFKDGeraetMaschine() {

		FilterKriteriumDirekt fKDPartnerOrt = new FilterKriteriumDirekt(
				"flrismaschine.c_bez", "", FilterKriterium.OPERATOR_LIKE,
				LPMain.getTextRespectUISPr("is.maschine"),
				FilterKriteriumDirekt.PROZENT_BOTH, true, true,
				Facade.MAX_UNBESCHRAENKT);
		return fKDPartnerOrt;
	}

	public FilterKriteriumDirekt createFKDGeraetHalle() {

		FilterKriteriumDirekt fKDPartnerOrt = new FilterKriteriumDirekt(
				"flrhalle.c_bez", "", FilterKriterium.OPERATOR_LIKE,
				LPMain.getTextRespectUISPr("is.halle"),
				FilterKriteriumDirekt.PROZENT_BOTH, true, true,
				Facade.MAX_UNBESCHRAENKT);
		return fKDPartnerOrt;
	}

	public FilterKriteriumDirekt createFKDGeraetAnlage() {

		FilterKriteriumDirekt fKDPartnerOrt = new FilterKriteriumDirekt(
				"flranlage.c_bez", "", FilterKriterium.OPERATOR_LIKE,
				LPMain.getTextRespectUISPr("is.anlage"),
				FilterKriteriumDirekt.PROZENT_BOTH, true, true,
				Facade.MAX_UNBESCHRAENKT);
		return fKDPartnerOrt;
	}

	public FilterKriterium[] createFKGeraete(Integer standortIId)
			throws Throwable {

		FilterKriterium[] krit = new FilterKriterium[1];
		krit[0] = new FilterKriterium("flrstandort.i_id", true, standortIId
				+ "", FilterKriterium.OPERATOR_EQUAL, false);

		return krit;
	}

	public FilterKriterium[] createFKStandorteEinesMandanten() throws Throwable {

		FilterKriterium[] krit = new FilterKriterium[1];
		krit[0] = new FilterKriterium(
				"flrinstandhaltung.flrkunde.mandant_c_nr", true, "'"
						+ LPMain.getTheClient().getMandant() + "'",
				FilterKriterium.OPERATOR_EQUAL, false);

		return krit;
	}

	public FilterKriterium[] createFKHallenEinesMandanten() throws Throwable {

		FilterKriterium[] krit = new FilterKriterium[1];
		krit[0] = new FilterKriterium(
				"flrstandort.flrinstandhaltung.flrkunde.mandant_c_nr", true,
				"'" + LPMain.getTheClient().getMandant() + "'",
				FilterKriterium.OPERATOR_EQUAL, false);

		return krit;
	}

	public FilterKriterium[] createFKAnlageEinesMandanten() throws Throwable {

		FilterKriterium[] krit = new FilterKriterium[1];
		krit[0] = new FilterKriterium(
				"flrhalle.flrstandort.flrinstandhaltung.flrkunde.mandant_c_nr",
				true, "'" + LPMain.getTheClient().getMandant() + "'",
				FilterKriterium.OPERATOR_EQUAL, false);

		return krit;
	}

	public FilterKriterium[] createFKIsmaschineEinesMandanten()
			throws Throwable {

		FilterKriterium[] krit = new FilterKriterium[1];
		krit[0] = new FilterKriterium(
				"flranlage.flrhalle.flrstandort.flrinstandhaltung.flrkunde.mandant_c_nr",
				true, "'" + LPMain.getTheClient().getMandant() + "'",
				FilterKriterium.OPERATOR_EQUAL, false);

		return krit;
	}

	public FilterKriterium[] createFKGeraetetypEinesMandanten()
			throws Throwable {

		FilterKriterium[] krit = new FilterKriterium[1];
		krit[0] = new FilterKriterium("mandant_c_nr", true, "'"
				+ LPMain.getTheClient().getMandant() + "'",
				FilterKriterium.OPERATOR_EQUAL, false);

		return krit;
	}

	public PanelQueryFLR createPanelFLRStandort(InternalFrame internalFrameI,
			Integer selectedId, boolean bMitLeerenButton) throws Throwable {
		String[] aWhichButtonIUse = null;

		if (bMitLeerenButton) {
			aWhichButtonIUse = new String[] { PanelBasis.ACTION_FILTER,
					PanelBasis.ACTION_LEEREN };
		} else {
			aWhichButtonIUse = new String[] { PanelBasis.ACTION_FILTER };
		}

		PanelQueryFLR plPartner = new PanelQueryFLR(null,
				createFKStandorteEinesMandanten(),
				QueryParameters.UC_ID_STANDORT, aWhichButtonIUse,
				internalFrameI, LPMain.getTextRespectUISPr("is.standort"));

		if (selectedId != null) {
			plPartner.setSelectedId(selectedId);
		}

		return plPartner;
	}

	public PanelQueryFLR createPanelFLRHalle(InternalFrame internalFrameI,
			Integer selectedId, boolean bMitLeerenButton) throws Throwable {
		String[] aWhichButtonIUse = null;

		if (bMitLeerenButton) {
			aWhichButtonIUse = new String[] { PanelBasis.ACTION_FILTER,
					PanelBasis.ACTION_LEEREN };
		} else {
			aWhichButtonIUse = new String[] { PanelBasis.ACTION_FILTER };
		}

		PanelQueryFLR plPartner = new PanelQueryFLR(null,
				createFKHallenEinesMandanten(), QueryParameters.UC_ID_HALLE,
				aWhichButtonIUse, internalFrameI,
				LPMain.getTextRespectUISPr("is.halle"));

		if (selectedId != null) {
			plPartner.setSelectedId(selectedId);
		}

		return plPartner;
	}

	public PanelQueryFLR createPanelFLRIskategorie(
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
				QueryParameters.UC_ID_ISKATEGORIE, aWhichButtonIUse,
				internalFrameI, LPMain.getTextRespectUISPr("is.kategorie"));

		if (selectedId != null) {
			plPartner.setSelectedId(selectedId);
		}

		return plPartner;
	}

	public PanelQueryFLR createPanelFLRGewerk(InternalFrame internalFrameI,
			Integer selectedId, boolean bMitLeerenButton) throws Throwable {
		String[] aWhichButtonIUse = null;

		if (bMitLeerenButton) {
			aWhichButtonIUse = new String[] { PanelBasis.ACTION_FILTER,
					PanelBasis.ACTION_LEEREN };
		} else {
			aWhichButtonIUse = new String[] { PanelBasis.ACTION_FILTER };
		}

		PanelQueryFLR plPartner = new PanelQueryFLR(null, SystemFilterFactory
				.getInstance().createFKMandantCNr(),
				QueryParameters.UC_ID_GEWERK, aWhichButtonIUse, internalFrameI,
				LPMain.getTextRespectUISPr("is.gewerk"));

		if (selectedId != null) {
			plPartner.setSelectedId(selectedId);
		}

		return plPartner;
	}

	public PanelQueryFLR createPanelFLRAnlage(InternalFrame internalFrameI,
			Integer selectedId, boolean bMitLeerenButton) throws Throwable {
		String[] aWhichButtonIUse = null;

		if (bMitLeerenButton) {
			aWhichButtonIUse = new String[] { PanelBasis.ACTION_FILTER,
					PanelBasis.ACTION_LEEREN };
		} else {
			aWhichButtonIUse = new String[] { PanelBasis.ACTION_FILTER };
		}

		PanelQueryFLR plPartner = new PanelQueryFLR(null,
				createFKAnlageEinesMandanten(), QueryParameters.UC_ID_ANLAGE,
				aWhichButtonIUse, internalFrameI,
				LPMain.getTextRespectUISPr("is.anlage"));

		if (selectedId != null) {
			plPartner.setSelectedId(selectedId);
		}

		return plPartner;
	}

	public PanelQueryFLR createPanelFLRIsmaschine(InternalFrame internalFrameI,
			Integer selectedId, boolean bMitLeerenButton) throws Throwable {
		String[] aWhichButtonIUse = null;

		if (bMitLeerenButton) {
			aWhichButtonIUse = new String[] { PanelBasis.ACTION_FILTER,
					PanelBasis.ACTION_LEEREN };
		} else {
			aWhichButtonIUse = new String[] { PanelBasis.ACTION_FILTER };
		}

		PanelQueryFLR plPartner = new PanelQueryFLR(null,
				createFKIsmaschineEinesMandanten(),
				QueryParameters.UC_ID_ISMASCHINE, aWhichButtonIUse,
				internalFrameI, LPMain.getTextRespectUISPr("is.maschine"));

		if (selectedId != null) {
			plPartner.setSelectedId(selectedId);
		}

		return plPartner;
	}

	public PanelQueryFLR createPanelFLRGeraetetyp(InternalFrame internalFrameI,
			Integer selectedId, boolean bMitLeerenButton) throws Throwable {
		String[] aWhichButtonIUse = null;

		if (bMitLeerenButton) {
			aWhichButtonIUse = new String[] { PanelBasis.ACTION_FILTER,
					PanelBasis.ACTION_LEEREN };
		} else {
			aWhichButtonIUse = new String[] { PanelBasis.ACTION_FILTER };
		}

		PanelQueryFLR plPartner = new PanelQueryFLR(null,
				createFKGeraetetypEinesMandanten(),
				QueryParameters.UC_ID_GERAETETYP, aWhichButtonIUse,
				internalFrameI, LPMain.getTextRespectUISPr("is.geraetetyp"));

		if (selectedId != null) {
			plPartner.setSelectedId(selectedId);
		}

		return plPartner;
	}

}
