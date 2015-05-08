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
package com.lp.client.kueche;

import java.util.Calendar;

import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.pc.LPMain;
import com.lp.server.personal.service.ZeiterfassungFac;
import com.lp.server.personal.service.ZutrittscontrollerFac;
import com.lp.server.util.Facade;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.FilterKriteriumDirekt;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.util.Helper;

@SuppressWarnings("static-access")
public class KuecheFilterFactory {
	private static KuecheFilterFactory filterFactory = null;

	private KuecheFilterFactory() {
		// Singleton.
	}

	static public KuecheFilterFactory getInstance() {
		if (filterFactory == null) {
			filterFactory = new KuecheFilterFactory();
		}

		return filterFactory;
	}

	public PanelQueryFLR createPanelFLRKassaartikel(
			InternalFrame internalFrameI, Integer selectedId) throws Throwable {
		String[] aWhichButtonIUse = { PanelBasis.ACTION_REFRESH,
				PanelBasis.ACTION_FILTER };

		PanelQueryFLR panelQueryFLRSpeisekassa = new PanelQueryFLR(null, null,
				QueryParameters.UC_ID_KASSAARTIKEL, aWhichButtonIUse,
				internalFrameI, LPMain.getInstance().getTextRespectUISPr(
						"kue.kassaartikel"));
		if (selectedId != null) {
			panelQueryFLRSpeisekassa.setSelectedId(selectedId);
		}

		return panelQueryFLRSpeisekassa;
	}

	public FilterKriteriumDirekt createFKDKdc100logSeriennummer()
			throws Throwable {
		return new FilterKriteriumDirekt("c_seriennummer", "",
				FilterKriterium.OPERATOR_LIKE, LPMain.getInstance()
						.getTextRespectUISPr("auft.seriennummer"),
				FilterKriteriumDirekt.PROZENT_TRAILING, true, true,
				Facade.MAX_UNBESCHRAENKT);

	}

	public FilterKriterium[] createFKSpeiseplanZuDatum(java.sql.Date dDatum, String fertigungsgruppeIId)
			throws Throwable {
		
		if(fertigungsgruppeIId==null || fertigungsgruppeIId.length()==0){
			FilterKriterium[] kriterien = new FilterKriterium[3];

			FilterKriterium krit1 = new FilterKriterium("mandant_c_nr", true, "'"
					+ LPMain.getInstance().getTheClient().getMandant() + "'",
					FilterKriterium.OPERATOR_EQUAL, false);

			FilterKriterium krit2 = new FilterKriterium("t_datum", true, "'"
					+ Helper.formatDateWithSlashes(dDatum) + "'",
					FilterKriterium.OPERATOR_GTE, false);

			Calendar c = Calendar.getInstance();
			c.setTimeInMillis(dDatum.getTime());
			c.set(Calendar.DAY_OF_YEAR, c.get(Calendar.DAY_OF_YEAR) + 1);

			FilterKriterium krit3 = new FilterKriterium("t_datum", true, "'"
					+ Helper.formatDateWithSlashes(new java.sql.Date(c
							.getTimeInMillis())) + "'",
					FilterKriterium.OPERATOR_LT, false);

			kriterien[0] = krit1;
			kriterien[1] = krit2;
			kriterien[2] = krit3;
			return kriterien;
		} else {
			FilterKriterium[] kriterien = new FilterKriterium[4];

			FilterKriterium krit1 = new FilterKriterium("mandant_c_nr", true, "'"
					+ LPMain.getInstance().getTheClient().getMandant() + "'",
					FilterKriterium.OPERATOR_EQUAL, false);

			FilterKriterium krit2 = new FilterKriterium("t_datum", true, "'"
					+ Helper.formatDateWithSlashes(dDatum) + "'",
					FilterKriterium.OPERATOR_GTE, false);

			Calendar c = Calendar.getInstance();
			c.setTimeInMillis(dDatum.getTime());
			c.set(Calendar.DAY_OF_YEAR, c.get(Calendar.DAY_OF_YEAR) + 1);

			FilterKriterium krit3 = new FilterKriterium("t_datum", true, "'"
					+ Helper.formatDateWithSlashes(new java.sql.Date(c
							.getTimeInMillis())) + "'",
					FilterKriterium.OPERATOR_LT, false);
			
			FilterKriterium krit4 = new FilterKriterium("flrfertigungsgruppe.i_id", true, fertigungsgruppeIId,
					FilterKriterium.OPERATOR_EQUAL, false);
			
			kriterien[0] = krit1;
			kriterien[1] = krit2;
			kriterien[2] = krit3;
			kriterien[3] = krit4;
			return kriterien;
		}
		
		
	}

	public FilterKriteriumDirekt createFKDKdc100logKommentar() throws Throwable {
		return new FilterKriteriumDirekt("c_kommentar", "",
				FilterKriterium.OPERATOR_LIKE, LPMain.getInstance()
						.getTextRespectUISPr("lp.kommentar"),
				FilterKriteriumDirekt.PROZENT_BOTH, true, true,
				Facade.MAX_UNBESCHRAENKT);

	}

}
