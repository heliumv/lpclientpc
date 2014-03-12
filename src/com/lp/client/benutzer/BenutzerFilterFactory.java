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
package com.lp.client.benutzer;

import com.lp.client.artikel.ArtikelFilterFactory;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.pc.LPMain;
import com.lp.server.benutzer.service.BenutzerFac;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.partner.service.PartnerFac;
import com.lp.server.personal.service.PersonalFac;
import com.lp.server.util.Facade;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.FilterKriteriumDirekt;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;

@SuppressWarnings("static-access")
public class BenutzerFilterFactory {
	private static BenutzerFilterFactory filterFactory = null;

	private BenutzerFilterFactory() {
		// Singleton.
	}

	static public BenutzerFilterFactory getInstance() {
		if (filterFactory == null) {
			filterFactory = new BenutzerFilterFactory();
		}

		return filterFactory;
	}

	public FilterKriteriumDirekt createFKDBenutzerkennung() throws Throwable {

		return new FilterKriteriumDirekt(
				BenutzerFac.FLR_BENUTZER_C_BENUTZERKENNUNG, "",
				FilterKriterium.OPERATOR_LIKE, LPMain.getInstance()
						.getTextRespectUISPr("pers.benutzer.benutzerkennung"),
				FilterKriteriumDirekt.PROZENT_TRAILING, true, true,
				Facade.MAX_UNBESCHRAENKT);

	}

	public PanelQueryFLR createPanelFLRThema(InternalFrame internalFrameI,
			String selectedId, boolean bMitLeerenButton) throws Throwable {

		String[] aWhichButtonIUse = null;
		if (bMitLeerenButton) {
			aWhichButtonIUse = new String[] { PanelBasis.ACTION_REFRESH,
					PanelBasis.ACTION_LEEREN };
		} else {
			aWhichButtonIUse = new String[] { PanelBasis.ACTION_REFRESH };
		}
		PanelQueryFLR panelQueryFLRThema = new PanelQueryFLR(null, null,
				QueryParameters.UC_ID_THEMA, aWhichButtonIUse, internalFrameI,
				LPMain.getTextRespectUISPr("ben.thema"));
		panelQueryFLRThema.befuellePanelFilterkriterienDirekt(createFKDThema(),
				null);
		panelQueryFLRThema.setSelectedId(selectedId);
		return panelQueryFLRThema;
	}
	
	
	public PanelQueryFLR createPanelFLRNachrichtarchiv(InternalFrame internalFrameI,
			Integer selectedId, boolean bMitLeerenButton) throws Throwable {

		String[] aWhichButtonIUse = null;
		if (bMitLeerenButton) {
			aWhichButtonIUse = new String[] { PanelBasis.ACTION_REFRESH,
					PanelBasis.ACTION_LEEREN };
		} else {
			aWhichButtonIUse = new String[] { PanelBasis.ACTION_REFRESH };
		}
		PanelQueryFLR panelQueryFLRThema = new PanelQueryFLR(null, null,
				QueryParameters.UC_ID_NACHRICHTARCHIV, aWhichButtonIUse, internalFrameI,
				LPMain.getTextRespectUISPr("ben.thema"));
		panelQueryFLRThema.befuellePanelFilterkriterienDirekt(createFKDThema(),
				null);
		panelQueryFLRThema.setSelectedId(selectedId);
		return panelQueryFLRThema;
	}
	
	
	

	
	public PanelQueryFLR createPanelFLRSystemrolle(InternalFrame internalFrameI,
			Integer selectedId, boolean bMitLeerenButton) throws Throwable {

		String[] aWhichButtonIUse = null;
		if (bMitLeerenButton) {
			aWhichButtonIUse = new String[] { PanelBasis.ACTION_REFRESH,
					PanelBasis.ACTION_LEEREN };
		} else {
			aWhichButtonIUse = new String[] { PanelBasis.ACTION_REFRESH };
		}
		PanelQueryFLR panelQueryFLRSystemrole = new PanelQueryFLR(null, null,
				QueryParameters.UC_ID_SYSTEMROLLE, aWhichButtonIUse, internalFrameI,
				LPMain.getTextRespectUISPr("lp.systemrolle"));
		panelQueryFLRSystemrole.befuellePanelFilterkriterienDirekt(null,
				null);
		panelQueryFLRSystemrole.setSelectedId(selectedId);
		return panelQueryFLRSystemrole;
	}

	
	
	public FilterKriteriumDirekt createFKDThema() throws Throwable {
		return new FilterKriteriumDirekt("c_nr", "",
				FilterKriterium.OPERATOR_LIKE, LPMain.getInstance()
						.getTextRespectUISPr("label.kennung"),
				FilterKriteriumDirekt.PROZENT_TRAILING, true, true,
				Facade.MAX_UNBESCHRAENKT);

	}

	public FilterKriteriumDirekt createFKDBenutzermandantBenutzerkennung()
			throws Throwable {

		return new FilterKriteriumDirekt(
				BenutzerFac.FLR_BENUTZERMANDANTSYSTEMROLLE_FLRBENUTZER + "."
						+ BenutzerFac.FLR_BENUTZER_C_BENUTZERKENNUNG, "",
				FilterKriterium.OPERATOR_LIKE, LPMain.getInstance()
						.getTextRespectUISPr("pers.benutzer.benutzerkennung"),
				FilterKriteriumDirekt.PROZENT_TRAILING, true, true,
				Facade.MAX_UNBESCHRAENKT);

	}

	public FilterKriteriumDirekt createFKDNachrichtartKennung()
			throws Throwable {

		return new FilterKriteriumDirekt("c_nr", "",
				FilterKriterium.OPERATOR_LIKE, LPMain.getInstance()
						.getTextRespectUISPr("label.kennung"),
				FilterKriteriumDirekt.PROZENT_TRAILING, true, true,
				Facade.MAX_UNBESCHRAENKT);

	}

	public FilterKriteriumDirekt createFKDThemarolle() throws Throwable {

		return new FilterKriteriumDirekt(BenutzerFac.FLR_THEMAROLLE_FLRTHEMA
				+ ".c_nr", "", FilterKriterium.OPERATOR_LIKE, LPMain
				.getInstance().getTextRespectUISPr("ben.thema"),
				FilterKriteriumDirekt.PROZENT_TRAILING, true, true,
				Facade.MAX_UNBESCHRAENKT);

	}

	public FilterKriteriumDirekt createFKDBenutzermandantSystemrolle()
			throws Throwable {

		return new FilterKriteriumDirekt(
				BenutzerFac.FLR_BENUTZERMANDANTSYSTEMROLLE_FLRSYSTEMROLLE + "."
						+ BenutzerFac.FLR_SYSTEMROLLE_C_BEZ, "",
				FilterKriterium.OPERATOR_LIKE, LPMain.getInstance()
						.getTextRespectUISPr("benutzer.title.tab.systemrolle"),
				FilterKriteriumDirekt.PROZENT_TRAILING, true, true,
				Facade.MAX_UNBESCHRAENKT);

	}

	public FilterKriteriumDirekt createFKDNachname() throws Throwable {

		return new FilterKriteriumDirekt(BenutzerFac.FLR_BENUTZER_FLRPERSONAL
				+ "." + PersonalFac.FLR_PERSONAL_FLRPARTNER + "."
				+ PartnerFac.FLR_PARTNER_NAME1NACHNAMEFIRMAZEILE1, "",
				FilterKriterium.OPERATOR_LIKE, LPMain.getInstance()
						.getTextRespectUISPr("lp.nachname"),
				FilterKriteriumDirekt.PROZENT_TRAILING, true, true,
				Facade.MAX_UNBESCHRAENKT);

	}

	public FilterKriterium[] createFKRollerecht(Integer materialIId)
			throws Throwable {
		FilterKriterium[] kriterien = new FilterKriterium[1];
		kriterien[0] = new FilterKriterium(
				RechteFac.FLR_ROLLERECHT_FLRSYSTEMROLLE + ".i_id", true,
				materialIId + "", FilterKriterium.OPERATOR_EQUAL, false);

		return kriterien;
	}

	public FilterKriterium[] createFKOhneLpwebappzemecs() throws Throwable {
		FilterKriterium[] kriterien = new FilterKriterium[1];
		kriterien[0] = new FilterKriterium(
				BenutzerFac.FLR_BENUTZER_C_BENUTZERKENNUNG, true,
				"'lpwebappzemecs'", FilterKriterium.OPERATOR_NOT_EQUAL, false);

		return kriterien;
	}

}
