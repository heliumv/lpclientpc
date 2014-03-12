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
package com.lp.client.frame.component;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.List;

import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.rechnung.TabbedPaneRechnung;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.SeriennrChargennrMitMengeDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.util.Helper;

@SuppressWarnings("static-access")
/**
 * <p>Diese Klasse ist stellt Basismethoden fuer Helium V TabbedPanes zur Verfuegung.</p>
 *
 * <p>Copyright: Copyright (c) 2004, 2005</p>
 *
 * <p>Erstellung: Uli Walch; 28.10.04</p>
 *
 * <p>@author $Author: christian $</p>
 *
 * @version $Revision: 1.6 $ Date $Date: 2013/01/15 13:58:39 $
 */
abstract public class TabbedPaneBasis extends TabbedPane {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TabbedPaneBasis(InternalFrame internalFrameI, String addTitleI) {
		super(internalFrameI, addTitleI);
	}

	/**
	 * Pruefen, ob die gewuenschte Menge eines Artikels in einem bestimmten
	 * Lager vorhanden ist.
	 * 
	 * @param iIdArtikelI
	 *            PK des Artikels
	 * @param iIdLagerI
	 *            PK des Lagers
	 * @param cSerienchargennummerI
	 *            bei einem serien- oder chargennummerbehafteten Artikel
	 * @param nMengeI
	 *            BigDecimal
	 * @return boolean true, wenn die gewuenschte Menge vorhanden ist
	 * @throws Throwable
	 *             Ausnahme
	 */
	public boolean istMengeAufLager(Integer iIdArtikelI, Integer iIdLagerI,
			List<SeriennrChargennrMitMengeDto> cSerienchargennummerI,
			BigDecimal nMengeI) throws Throwable {
		boolean bMengeIstAufLager = true;

		// PJ18290

		ParametermandantDto parameter = (ParametermandantDto) DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getParametermandant(
						ParameterFac.PARAMETER_LAGER_IMMER_AUSREICHEND_VERFUEGBAR,
						ParameterFac.KATEGORIE_ARTIKEL,
						LPMain.getTheClient().getMandant());

		boolean bImmerAusreichendVerfuegbar = (Boolean) parameter
				.getCWertAsObject();

		if (bImmerAusreichendVerfuegbar == false) {

			ArtikelDto artikelDto = DelegateFactory.getInstance()
					.getArtikelDelegate().artikelFindByPrimaryKey(iIdArtikelI);

			boolean bVerleihartikelInRechnung = false;

			if (Helper.short2boolean(artikelDto.getBVerleih())
					&& this instanceof TabbedPaneRechnung) {
				bVerleihartikelInRechnung = true;
			}

			if (Helper.short2boolean(artikelDto.getBLagerbewirtschaftet())
					&& !bVerleihartikelInRechnung) {

				// diese Methode muss auch keine, eine oder mehrere Serien- oder
				// Chargennummern verarbeiten koennen
				BigDecimal ddMengeAufLager = null;

				if (Helper.short2boolean(artikelDto.getBSeriennrtragend())
						|| Helper.short2boolean(artikelDto
								.getBChargennrtragend())) {
					ddMengeAufLager = DelegateFactory
							.getInstance()
							.getLagerDelegate()
							.getMengeMehrererSeriennummernChargennummernAufLager(
									iIdArtikelI, iIdLagerI,
									cSerienchargennummerI);

				} else {
					ddMengeAufLager = DelegateFactory
							.getInstance()
							.getLagerDelegate()
							.getMengeMehrererSeriennummernChargennummernAufLager(
									iIdArtikelI, iIdLagerI, null);

				}

				if (nMengeI.doubleValue() > ddMengeAufLager.doubleValue()) {
					MessageFormat mf = new MessageFormat(LPMain.getInstance()
							.getTextRespectUISPr("ls.error.mengeamlager"));
					mf.setLocale(LPMain.getInstance().getTheClient().getLocUi());

					String lagerCNr = DelegateFactory.getInstance()
							.getLagerDelegate()
							.lagerFindByPrimaryKey(iIdLagerI).getCNr();
					String cAnzahlEinheiten = ddMengeAufLager
							+ " "
							+ DelegateFactory.getInstance()
									.getArtikelDelegate()
									.artikelFindByPrimaryKey(iIdArtikelI)
									.getEinheitCNr();

					Object pattern[] = { lagerCNr, cAnzahlEinheiten };

					DialogFactory.showModalDialog(LPMain.getInstance()
							.getTextRespectUISPr("lp.error"), mf
							.format(pattern));
					bMengeIstAufLager = false;
				}
			}
		}
		return bMengeIstAufLager;
	}
}
