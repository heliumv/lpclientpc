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

import java.awt.event.ActionEvent;
import java.math.BigDecimal;
import java.util.EventObject;

import com.lp.client.artikel.ArtikelFilterFactory;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQuery;
import com.lp.client.frame.component.WrapperTable;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.util.fastlanereader.gui.QueryType;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.util.Helper;

@SuppressWarnings("static-access")
/*
 * <p>In diesem Detailfenster bekommt man einen Blick auf alle mengenbehafteten
 * Auftragpositionen jenes Auftrags, zu dem dieser Lieferschein gehoert.
 * <br>Alle SNR- und chargennummernbehafteten Artikel werden mit entsprechenden
 * Eingabefeldern versehen, diese Auftragpositionen koennen nur erledigt werden,
 * wenn eine ANR bzw. Seriennummer fuer die Lagerabbuchung der Menge einegegeben
 * wird.</p> <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 * <p>Erstellungsdatum 13. 03. 2005</p> <p> </p>
 * 
 * @author Uli Walch
 * 
 * @version $Revision: 1.5 $
 */
public abstract class PanelPositionenSichtAuftragSNR extends
		PanelPositionenSichtAuftrag {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected Integer iIdLager = null;

	/**
	 * Konstruktor.
	 * 
	 * @param internalFrame
	 *            der InternalFrame auf dem das Panel sitzt
	 * @param add2TitleI
	 *            der default Titel des Panels
	 * @param key
	 *            PK des Auftrags
	 * @param iIdLagerI
	 *            von diesem Lager soll abgebucht werden
	 * @throws java.lang.Throwable
	 *             Ausnahme
	 */
	public PanelPositionenSichtAuftragSNR(InternalFrame internalFrame,
			String add2TitleI, Object key, Integer iIdLagerI) throws Throwable {
		super(internalFrame, add2TitleI, key); // VKPF

		iIdLager = iIdLagerI;

		jbInit();
		initComponents();
	}

	private void jbInit() throws Throwable {
	}

	public void setLagerIId(Integer lagerIId) {
		iIdLager = lagerIId;
	}

	/**
	 * Hier kommen die events meiner speziellen Buttons an.
	 * 
	 * @param e
	 *            ActionEvent
	 * @throws Throwable
	 */
	protected void eventActionSpecial(ActionEvent e) throws Throwable {
	}

	/**
	 * Alle Serien- oder Chargennummern in einer Tabelle sammeln.
	 * 
	 * @param iIdArtikelI
	 *            PK des Artikels
	 * @return JTable die Tabelle
	 * @throws java.lang.Throwable
	 *             Ausnahme
	 */
	protected WrapperTable getTableSerienchargennummer(Integer iIdArtikelI)
			throws Throwable {
		QueryType[] qt = null;
		FilterKriterium[] fk = ArtikelFilterFactory.getInstance()
				.createFKArtikelSNR(iIdArtikelI, iIdLager);

		int iIdUsecase = -1;
		String sTitle = null;

		if (Helper.short2boolean(panelArtikel.getArtikelDto()
				.getBSeriennrtragend())) {
			iIdUsecase = QueryParameters.UC_ID_SERIENNUMMERNCHARGENNUMMERNAUFLAGER;
			sTitle = LPMain.getInstance().getTextRespectUISPr(
					"artikel.seriennummer");
		} else if (Helper.short2boolean(panelArtikel.getArtikelDto()
				.getBChargennrtragend())) {
			iIdUsecase = QueryParameters.UC_ID_CHARGENAUFLAGER;
			sTitle = LPMain.getInstance().getTextRespectUISPr(
					"artikel.chargennummer");
		}

		String[] aWhichButtonIUse = { PanelBasis.ACTION_LEEREN };

		PanelQuery pqDummy = new PanelQuery(qt, fk, iIdUsecase,
				aWhichButtonIUse, getInternalFrame(), sTitle, iIdArtikelI);

		return (WrapperTable) pqDummy.getTable();
	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;

	}

	/**
	 * Das Eingabefeld fuer die Menge wird mit einem Vorschlagswert befuellt.
	 * 
	 * @throws Throwable
	 */
	protected void befuelleMengeMitVorschlagswert() throws Throwable {
		if (!Helper.short2boolean(panelArtikel.getArtikelDto()
				.getBChargennrtragend())
				&& !Helper.short2boolean(panelArtikel.getArtikelDto()
						.getBSeriennrtragend())) {
			if (Helper.short2boolean(panelArtikel.getArtikelDto()
					.getBLagerbewirtschaftet())) {
				BigDecimal nMengeAufLager = null;

				// PJ18931
				ParametermandantDto parameter = (ParametermandantDto) DelegateFactory
						.getInstance()
						.getParameterDelegate()
						.getParametermandant(
								ParameterFac.PARAMETER_LAGER_IMMER_AUSREICHEND_VERFUEGBAR,
								ParameterFac.KATEGORIE_ARTIKEL,
								LPMain.getTheClient().getMandant());

				boolean bImmerAusreichendVerfuegbar = (Boolean) parameter
						.getCWertAsObject();
				if (bImmerAusreichendVerfuegbar == true) {
					nMengeAufLager = oAuftragpositionDto.getNOffeneMenge();
				} else {
					nMengeAufLager = DelegateFactory
							.getInstance()
							.getLagerDelegate()
							.getLagerstand(
									panelArtikel.getArtikelDto().getIId(),
									iIdLager);
				}

				if (nMengeAufLager.doubleValue() >= oAuftragpositionDto
						.getNOffeneMenge().doubleValue()) {
					panelArtikel.wnfMenge.setBigDecimal(oAuftragpositionDto
							.getNOffeneMenge());
				} else {
					// es kann nicht mehr geliefert werden als auf dem Lager
					// vorhanden ist
					panelArtikel.wnfMenge.setBigDecimal(nMengeAufLager);
				}
			} else {
				panelArtikel.wnfMenge.setBigDecimal(oAuftragpositionDto
						.getNOffeneMenge());
			}
		} else {
			panelArtikel.wnfMenge.setBigDecimal(oAuftragpositionDto
					.getNOffeneMenge());
		}
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {
		super.eventYouAreSelected(false); // buttons schalten

	}

	/**
	 * In Spalte 5 der Statusbar wird der neben dem Lager auch der Lagerstand
	 * angezeigt, wenn dafuer genug Information vorhanden ist.
	 * 
	 * @return String
	 * @throws Throwable
	 */
	protected String getLagerstandFuerStatusbarSpalte5() throws Throwable {
		String sLagerinfoO = null;
		sLagerinfoO = DelegateFactory.getInstance().getLagerDelegate()
				.lagerFindByPrimaryKey(iIdLager).getCNr();

		if (panelArtikel.getArtikelDto() != null
				&& panelArtikel.getArtikelDto().getIId() != null) {
			String serienchargennummer = null;

			if ((!Helper.short2boolean(panelArtikel.getArtikelDto()
					.getBSeriennrtragend()) && !Helper
					.short2boolean(panelArtikel.getArtikelDto()
							.getBChargennrtragend()))
					|| serienchargennummer != null) {
				if (panelArtikel.getArtikelDto() != null
						&& panelArtikel.getArtikelDto().getIId() != null) {
					BigDecimal ddMenge = DelegateFactory
							.getInstance()
							.getLagerDelegate()
							.getMengeAufLager(
									panelArtikel.getArtikelDto().getIId(),
									iIdLager, serienchargennummer);

					sLagerinfoO += ": ";
					sLagerinfoO += ddMenge;
				}
			}
		}
		return sLagerinfoO;
	}
}
