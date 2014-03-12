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

import javax.swing.event.ChangeEvent;

import com.lp.client.frame.LockStateValue;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQuery;
import com.lp.client.frame.component.PanelSplit;
import com.lp.client.frame.component.TabbedPane;
import com.lp.client.frame.component.WrapperMenuBar;
import com.lp.client.pc.LPMain;
import com.lp.client.system.SystemFilterFactory;
import com.lp.server.artikel.service.ArtikelFac;
import com.lp.server.artikel.service.ArtikelkommentarFac;
import com.lp.server.system.service.MandantFac;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;

@SuppressWarnings("static-access")
/**
 * <p>&UUml;berschrift: </p>
 * <p>Beschreibung: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Organisation: </p>
 * @author Christian Kollmann
 * @version $Revision: 1.18 $
 */
public class TabbedPaneArtikelgrunddaten extends TabbedPane {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private PanelQuery panelQueryArtikelklassen = null;
	private PanelBasis panelSplitArtikelklassen = null;
	private PanelBasis panelBottomArtikelklassen = null;

	private PanelQuery panelQueryArtikelgruppen = null;
	private PanelBasis panelSplitArtikelgruppen = null;
	private PanelBasis panelBottomArtikelgruppen = null;

	private PanelQuery panelQueryLager = null;
	private PanelBasis panelSplitLager = null;
	private PanelBasis panelBottomLager = null;

	private PanelQuery panelQueryHersteller = null;
	private PanelBasis panelSplitHersteller = null;
	private PanelBasis panelBottomHersteller = null;

	private PanelQuery panelQueryArtikelkommentarart = null;
	private PanelBasis panelSplitArtikelkommentarart = null;
	private PanelBasis panelBottomArtikelkommentarart = null;

	private PanelQuery panelQueryFarbcode = null;
	private PanelBasis panelSplitFarbcode = null;
	private PanelBasis panelBottomFarbcode = null;

	private PanelQuery panelQuerySperren = null;
	private PanelBasis panelSplitSperren = null;
	private PanelBasis panelBottomSperren = null;

	private PanelQuery panelQueryLagerplatz = null;
	private PanelBasis panelSplitLagerplatz = null;
	private PanelBasis panelBottomLagerplatz = null;

	private PanelQuery panelQueryVerleih = null;
	private PanelBasis panelSplitVerleih = null;
	private PanelBasis panelBottomVerleih = null;

	private PanelQuery panelQueryVorschlagstext = null;
	private PanelBasis panelSplitVorschlagstext = null;
	private PanelBasis panelBottomVorschlagstext = null;

	private PanelQuery panelQueryWebshop = null;
	private PanelBasis panelSplitWebshop = null;
	private PanelBasis panelBottomWebshop = null;

	private int IDX_PANEL_ARTIKELKLASSEN = -1;
	private int IDX_PANEL_ARTIKELGRUPPEN = -1;
	private int IDX_PANEL_LAGER = -1;
	private int IDX_PANEL_HERSTELLER = -1;
	private int IDX_PANEL_ARTIKELKOMMENTARART = -1;
	private int IDX_PANEL_FARBCODE = -1;
	private int IDX_PANEL_SPERREN = -1;
	private int IDX_PANEL_LAGERPLATZ = -1;
	private int IDX_PANEL_VERLEIH = -1;
	private int IDX_PANEL_VORSCHLAGSTEXT = -1;
	private int IDX_PANEL_WEBSHOP = -1;

	private WrapperMenuBar wrapperManuBar = null;

	public TabbedPaneArtikelgrunddaten(InternalFrame internalFrameI)
			throws Throwable {
		super(internalFrameI, LPMain.getInstance().getTextRespectUISPr(
				"lp.grunddaten"));

		jbInit();
		initComponents();
	}

	public InternalFrameArtikel getInternalFrameArtikel() {
		return (InternalFrameArtikel) getInternalFrame();
	}

	private void createArtikelklassen() throws Throwable {
		if (panelSplitArtikelklassen == null) {
			String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW,
					PanelBasis.ACTION_FILTER };

			panelQueryArtikelklassen = new PanelQuery(ArtikelFilterFactory
					.getInstance().createQTArtikelklasse(),
					ArtikelFilterFactory.getInstance()
							.createFKArtklaMandantCNr(),
					QueryParameters.UC_ID_ARTIKELKLASSE, aWhichButtonIUse,
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr(
									"artikel.title.tab.artikelklassen"), true);

			panelQueryArtikelklassen
					.befuellePanelFilterkriterienDirekt(
							ArtikelFilterFactory.getInstance()
									.createFKDArtikelklasseKennung(),
							SystemFilterFactory
									.getInstance()
									.createFKDSprTabelleBezeichnung(
											ArtikelFac.FLR_ARTIKELKLASSE_ARTIKELKLASSESPRSET));

			panelBottomArtikelklassen = new PanelArtikelklassen(
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr(
									"artikel.title.tab.artikelklassen"), null);
			panelSplitArtikelklassen = new PanelSplit(getInternalFrame(),
					panelBottomArtikelklassen, panelQueryArtikelklassen, 380);

			setComponentAt(IDX_PANEL_ARTIKELKLASSEN, panelSplitArtikelklassen);
		}
	}

	private void createArtikelkommentarart() throws Throwable {
		if (panelSplitArtikelkommentarart == null) {
			String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW,
					PanelBasis.ACTION_FILTER };

			panelQueryArtikelkommentarart = new PanelQuery(ArtikelFilterFactory
					.getInstance().createQTArtikelkommentarart(), null,
					QueryParameters.UC_ID_ARTIKELKOMMENTARART,
					aWhichButtonIUse, getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("lp.kommentarart"), true);

			panelQueryArtikelkommentarart
					.befuellePanelFilterkriterienDirekt(
							ArtikelFilterFactory.getInstance()
									.createFKDArtikelkommentarartKennung(),
							SystemFilterFactory
									.getInstance()
									.createFKDSprTabelleBezeichnung(
											ArtikelkommentarFac.FLR_ARTIKELKOMMENTARART_ARTIKELKOMMENTARARTSPRSET));

			panelBottomArtikelkommentarart = new PanelArtikelkommentarart(
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("lp.kommentarart"), null);
			panelSplitArtikelkommentarart = new PanelSplit(getInternalFrame(),
					panelBottomArtikelkommentarart,
					panelQueryArtikelkommentarart, 350);

			setComponentAt(IDX_PANEL_ARTIKELKOMMENTARART,
					panelSplitArtikelkommentarart);
		}
	}

	private void createFarbcode() throws Throwable {
		if (panelSplitFarbcode == null) {
			String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW,
					PanelBasis.ACTION_FILTER };

			panelQueryFarbcode = new PanelQuery(ArtikelFilterFactory
					.getInstance().createQTArtikelkommentarart(), null,
					QueryParameters.UC_ID_FARBCODE, aWhichButtonIUse,
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("artikel.farbcode"), true);

			panelQueryFarbcode.befuellePanelFilterkriterienDirekt(
					SystemFilterFactory.getInstance().createFKDKennung(),
					SystemFilterFactory.getInstance().createFKDBezeichnung());

			panelBottomFarbcode = new PanelFarbcode(getInternalFrame(), LPMain
					.getInstance().getTextRespectUISPr("artikel.farbcode"),
					null);
			panelSplitFarbcode = new PanelSplit(getInternalFrame(),
					panelBottomFarbcode, panelQueryFarbcode, 380);

			setComponentAt(IDX_PANEL_FARBCODE, panelSplitFarbcode);
		}
	}

	private void createVorschlagstext() throws Throwable {
		if (panelSplitVorschlagstext == null) {
			String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW,
					PanelBasis.ACTION_FILTER };

			panelQueryVorschlagstext = new PanelQuery(
					null,
					ArtikelFilterFactory.getInstance().createFKVorschlagstext(),
					QueryParameters.UC_ID_VORSCHLAGSTEXT, aWhichButtonIUse,
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("artikel.vorschlagstext"),
					true);

			panelQueryVorschlagstext.befuellePanelFilterkriterienDirekt(
					SystemFilterFactory.getInstance().createFKDBezeichnung(),
					null);

			panelBottomVorschlagstext = new PanelVorschlagstext(
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("artikel.vorschlagstext"),
					null);
			panelSplitVorschlagstext = new PanelSplit(getInternalFrame(),
					panelBottomVorschlagstext, panelQueryVorschlagstext, 380);

			setComponentAt(IDX_PANEL_VORSCHLAGSTEXT, panelSplitVorschlagstext);
		}
	}

	private void createWebshop() throws Throwable {
		if (panelSplitWebshop == null) {
			String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW,
					PanelBasis.ACTION_FILTER };

			panelQueryWebshop = new PanelQuery(null, SystemFilterFactory
					.getInstance().createFKMandantCNr(),
					QueryParameters.UC_ID_WEBSHOP, aWhichButtonIUse,
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("lp.webshop"), true);

			panelQueryWebshop.befuellePanelFilterkriterienDirekt(
					SystemFilterFactory.getInstance().createFKDBezeichnung(),
					null);

			panelBottomWebshop = new PanelWebshop(getInternalFrame(), LPMain
					.getInstance().getTextRespectUISPr("lp.webshop"), null);
			panelSplitWebshop = new PanelSplit(getInternalFrame(),
					panelBottomWebshop, panelQueryWebshop, 380);

			setComponentAt(IDX_PANEL_WEBSHOP, panelSplitWebshop);
		}
	}

	private void createVerleih() throws Throwable {
		if (panelSplitVerleih == null) {
			String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW,
					PanelBasis.ACTION_FILTER };

			panelQueryVerleih = new PanelQuery(null, null,
					QueryParameters.UC_ID_VERLEIH, aWhichButtonIUse,
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("artikel.verleih"), true);

			panelBottomVerleih = new PanelVerleih(getInternalFrame(), LPMain
					.getInstance().getTextRespectUISPr("artikel.verleih"), null);
			panelSplitVerleih = new PanelSplit(getInternalFrame(),
					panelBottomVerleih, panelQueryVerleih, 330);

			setComponentAt(IDX_PANEL_VERLEIH, panelSplitVerleih);
		}
	}

	private void createLagerplatz() throws Throwable {
		if (panelSplitLagerplatz == null) {
			String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW,
					PanelBasis.ACTION_FILTER };

			panelQueryLagerplatz = new PanelQuery(ArtikelFilterFactory
					.getInstance().createQTLagerplatz(), ArtikelFilterFactory
					.getInstance().createFKLagerplatz(),
					QueryParameters.UC_ID_LAGERPLATZ, aWhichButtonIUse,
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("lp.lagerplatz"), true);

			panelQueryLagerplatz.befuellePanelFilterkriterienDirekt(
					ArtikelFilterFactory.getInstance().createFKDLagerplatz(),
					null);

			panelBottomLagerplatz = new PanelLagerplatz(getInternalFrame(),
					LPMain.getInstance().getTextRespectUISPr("lp.lagerplatz"),
					null);
			panelSplitLagerplatz = new PanelSplit(getInternalFrame(),
					panelBottomLagerplatz, panelQueryLagerplatz, 330);

			setComponentAt(IDX_PANEL_LAGERPLATZ, panelSplitLagerplatz);
		}
	}

	private void createSperren() throws Throwable {
		if (panelSplitSperren == null) {
			String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW };

			panelQuerySperren = new PanelQuery(null, SystemFilterFactory
					.getInstance().createFKMandantCNr(),
					QueryParameters.UC_ID_SPERREN, aWhichButtonIUse,
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("lp.sperren"), true);

			panelQuerySperren.befuellePanelFilterkriterienDirekt(
					SystemFilterFactory.getInstance().createFKDBezeichnung(),
					null);

			panelBottomSperren = new PanelSperren(getInternalFrame(), LPMain
					.getInstance().getTextRespectUISPr("lp.sperren"), null);
			panelSplitSperren = new PanelSplit(getInternalFrame(),
					panelBottomSperren, panelQuerySperren, 230);

			setComponentAt(IDX_PANEL_SPERREN, panelSplitSperren);
		}
	}

	private void createArtikelgruppen() throws Throwable {
		if (panelSplitArtikelgruppen == null) {
			String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW,
					PanelBasis.ACTION_FILTER };

			panelQueryArtikelgruppen = new PanelQuery(ArtikelFilterFactory
					.getInstance().createQTArtikelgruppe(),
					ArtikelFilterFactory.getInstance()
							.createFKArtgruMandantCNr(),
					QueryParameters.UC_ID_ARTIKELGRUPPE, aWhichButtonIUse,
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr(
									"artikel.title.tab.artikelgruppen"), true);

			panelQueryArtikelgruppen
					.befuellePanelFilterkriterienDirekt(
							ArtikelFilterFactory.getInstance()
									.createFKDArtikelgruppeKennung(),
							SystemFilterFactory
									.getInstance()
									.createFKDSprTabelleBezeichnung(
											ArtikelFac.FLR_ARTIKELGRUPPE_ARTIKELGRUPPESPRSET));

			panelBottomArtikelgruppen = new PanelArtikelgruppen(
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr(
									"artikel.title.tab.artikelgruppen"), null);

			panelSplitArtikelgruppen = new PanelSplit(getInternalFrame(),
					panelBottomArtikelgruppen, panelQueryArtikelgruppen, 350);

			setComponentAt(IDX_PANEL_ARTIKELGRUPPEN, panelSplitArtikelgruppen);
		}
	}

	private void createHersteller() throws Throwable {
		if (panelSplitHersteller == null) {
			String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW };
			panelQueryHersteller = new PanelQuery(null, null,
					QueryParameters.UC_ID_ARTIKELHERSTELLER, aWhichButtonIUse,
					getInternalFrame(),
					LPMain.getInstance().getTextRespectUISPr(
							"artikel.title.tab.hersteller"), true);
			panelQueryHersteller.befuellePanelFilterkriterienDirekt(
					ArtikelFilterFactory.getInstance().createFKDHersteller(),
					ArtikelFilterFactory.getInstance()
							.createFKDHerstellerPartner());

			panelBottomHersteller = new PanelHersteller(getInternalFrame(),
					LPMain.getInstance().getTextRespectUISPr(
							"artikel.title.tab.hersteller"), null);

			panelSplitHersteller = new PanelSplit(getInternalFrame(),
					panelBottomHersteller, panelQueryHersteller, 380);

			setComponentAt(IDX_PANEL_HERSTELLER, panelSplitHersteller);
		}
	}

	private void createLager() throws Throwable {
		if (panelSplitLager == null) {
			String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW };

			panelQueryLager = new PanelQuery(ArtikelFilterFactory.getInstance()
					.createQTLager(), ArtikelFilterFactory.getInstance()
					.createFKLagerlisteMitVersteckten(),
					QueryParameters.UC_ID_LAGER_ALLE, aWhichButtonIUse,
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("button.lager"), true);
			panelQueryLager.befuellePanelFilterkriterienDirekt(
					ArtikelFilterFactory.getInstance()
							.createFKDLagernameGrunddaten(),
					ArtikelFilterFactory.getInstance()
							.createFKDLagerLagerartGrunddaten());

			panelBottomLager = new PanelLager(getInternalFrame(), LPMain
					.getInstance().getTextRespectUISPr("button.lager"), null);

			panelSplitLager = new PanelSplit(getInternalFrame(),
					panelBottomLager, panelQueryLager, 320);

			setComponentAt(IDX_PANEL_LAGER, panelSplitLager);
		}
	}

	private void jbInit() throws Throwable {

		boolean bZentralerArtikelstamm = LPMain
				.getInstance()
				.getDesktop()
				.darfAnwenderAufZusatzfunktionZugreifen(
						MandantFac.ZUSATZFUNKTION_ZENTRALER_ARTIKELSTAMM);
		boolean bGetrennteLager = LPMain
				.getInstance()
				.getDesktop()
				.darfAnwenderAufZusatzfunktionZugreifen(
						MandantFac.ZUSATZFUNKTION_GETRENNTE_LAGER);

		int tabIndex = 0;

		if (bZentralerArtikelstamm && bGetrennteLager) {
			if (LPMain
					.getInstance()
					.getDesktop()
					.darfAnwenderAufZusatzfunktionZugreifen(
							MandantFac.ZUSATZFUNKTION_MEHRLAGERVERWALTUNG)) {

				IDX_PANEL_LAGER = tabIndex;
				insertTab(
						LPMain.getInstance().getTextRespectUISPr("label.lager"),
						null,
						null,
						LPMain.getInstance().getTextRespectUISPr("label.lager"),
						IDX_PANEL_LAGER);
				tabIndex++;
				createLager();

			}

			IDX_PANEL_LAGERPLATZ = tabIndex;
			insertTab(
					LPMain.getInstance().getTextRespectUISPr("lp.lagerplatz"),
					null, null,
					LPMain.getInstance().getTextRespectUISPr("lp.lagerplatz"),
					IDX_PANEL_LAGERPLATZ);

			tabIndex++;
			createLagerplatz();
		} else {

			IDX_PANEL_ARTIKELKLASSEN = tabIndex;
			// 1 tab oben: QP1 PartnerFLR; lazy loading
			insertTab(
					LPMain.getInstance().getTextRespectUISPr(
							"artikel.title.tab.artikelklassen"),
					null,
					null,
					LPMain.getInstance().getTextRespectUISPr(
							"artikel.title.tab.artikelklassen"),
					IDX_PANEL_ARTIKELKLASSEN);
			tabIndex++;
			IDX_PANEL_ARTIKELGRUPPEN = tabIndex;
			insertTab(
					LPMain.getInstance().getTextRespectUISPr(
							"artikel.title.tab.artikelgruppen"),
					null,
					null,
					LPMain.getInstance().getTextRespectUISPr(
							"artikel.title.tab.artikelgruppen"),
					IDX_PANEL_ARTIKELGRUPPEN);

			if (LPMain
					.getInstance()
					.getDesktop()
					.darfAnwenderAufZusatzfunktionZugreifen(
							MandantFac.ZUSATZFUNKTION_MEHRLAGERVERWALTUNG)) {
				tabIndex++;
				IDX_PANEL_LAGER = tabIndex;
				insertTab(
						LPMain.getInstance().getTextRespectUISPr("label.lager"),
						null,
						null,
						LPMain.getInstance().getTextRespectUISPr("label.lager"),
						IDX_PANEL_LAGER);
			}
			tabIndex++;
			IDX_PANEL_HERSTELLER = tabIndex;
			insertTab(
					LPMain.getInstance().getTextRespectUISPr(
							"artikel.title.tab.hersteller"),
					null,
					null,
					LPMain.getInstance().getTextRespectUISPr(
							"artikel.title.tab.hersteller"),
					IDX_PANEL_HERSTELLER);

			tabIndex++;
			IDX_PANEL_ARTIKELKOMMENTARART = tabIndex;
			insertTab(
					LPMain.getInstance().getTextRespectUISPr("lp.kommentarart"),
					null,
					null,
					LPMain.getInstance().getTextRespectUISPr("lp.kommentarart"),
					IDX_PANEL_ARTIKELKOMMENTARART);

			tabIndex++;
			IDX_PANEL_FARBCODE = tabIndex;
			insertTab(
					LPMain.getInstance()
							.getTextRespectUISPr("artikel.farbcode"), null,
					null,
					LPMain.getInstance()
							.getTextRespectUISPr("artikel.farbcode"),
					IDX_PANEL_FARBCODE);
			tabIndex++;
			IDX_PANEL_SPERREN = tabIndex;
			insertTab(LPMain.getInstance().getTextRespectUISPr("lp.sperren"),
					null, null,
					LPMain.getInstance().getTextRespectUISPr("lp.sperren"),
					IDX_PANEL_SPERREN);
			tabIndex++;
			IDX_PANEL_LAGERPLATZ = tabIndex;
			insertTab(
					LPMain.getInstance().getTextRespectUISPr("lp.lagerplatz"),
					null, null,
					LPMain.getInstance().getTextRespectUISPr("lp.lagerplatz"),
					IDX_PANEL_LAGERPLATZ);

			if (LPMain
					.getInstance()
					.getDesktop()
					.darfAnwenderAufZusatzfunktionZugreifen(
							MandantFac.ZUSATZFUNKTION_VERLEIH)) {
				tabIndex++;
				IDX_PANEL_VERLEIH = tabIndex;
				insertTab(
						LPMain.getInstance().getTextRespectUISPr(
								"artikel.verleih"),
						null,
						null,
						LPMain.getInstance().getTextRespectUISPr(
								"artikel.verleih"), IDX_PANEL_VERLEIH);
			}

			tabIndex++;
			IDX_PANEL_VORSCHLAGSTEXT = tabIndex;
			insertTab(
					LPMain.getInstance().getTextRespectUISPr(
							"artikel.vorschlagstext"),
					null,
					null,
					LPMain.getInstance().getTextRespectUISPr(
							"artikel.vorschlagstext"), IDX_PANEL_VORSCHLAGSTEXT);
			tabIndex++;
			IDX_PANEL_WEBSHOP = tabIndex;
			insertTab(LPMain.getInstance().getTextRespectUISPr("lp.webshop"),
					null, null,
					LPMain.getInstance().getTextRespectUISPr("lp.webshop"),
					IDX_PANEL_WEBSHOP);

			createArtikelklassen();

		}
		// Itemevents an MEIN Detailpanel senden kann.
		this.addChangeListener(this);
		getInternalFrame().addItemChangedListener(this);
	}

	public void lPEventItemChanged(ItemChangedEvent e) throws Throwable {

		if (e.getID() == ItemChangedEvent.ITEM_CHANGED) {
			if (e.getSource() == panelQueryArtikelklassen) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				panelBottomArtikelklassen.setKeyWhenDetailPanel(key);
				panelBottomArtikelklassen.eventYouAreSelected(false);
				panelQueryArtikelklassen.updateButtons();

			} else if (e.getSource() == panelQueryArtikelgruppen) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				panelBottomArtikelgruppen.setKeyWhenDetailPanel(key);
				panelBottomArtikelgruppen.eventYouAreSelected(false);
				panelQueryArtikelgruppen.updateButtons();

			} else if (e.getSource() == panelQueryVerleih) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				panelBottomVerleih.setKeyWhenDetailPanel(key);
				panelBottomVerleih.eventYouAreSelected(false);
				panelQueryVerleih.updateButtons();

			} else if (e.getSource() == panelQueryHersteller) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				panelBottomHersteller.setKeyWhenDetailPanel(key);
				panelBottomHersteller.eventYouAreSelected(false);
				panelQueryHersteller.updateButtons();

			} else if (e.getSource() == panelQueryLager) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				panelBottomLager.setKeyWhenDetailPanel(key);
				panelBottomLager.eventYouAreSelected(false);
				panelQueryLager.updateButtons();

			} else if (e.getSource() == panelQueryArtikelkommentarart) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				panelBottomArtikelkommentarart.setKeyWhenDetailPanel(key);
				panelBottomArtikelkommentarart.eventYouAreSelected(false);
				panelQueryArtikelkommentarart.updateButtons();

			} else if (e.getSource() == panelQueryFarbcode) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				panelBottomFarbcode.setKeyWhenDetailPanel(key);
				panelBottomFarbcode.eventYouAreSelected(false);
				panelQueryFarbcode.updateButtons();

			} else if (e.getSource() == panelQueryLagerplatz) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				panelBottomLagerplatz.setKeyWhenDetailPanel(key);
				panelBottomLagerplatz.eventYouAreSelected(false);
				panelQueryLagerplatz.updateButtons();

			} else if (e.getSource() == panelQuerySperren) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				panelBottomSperren.setKeyWhenDetailPanel(key);
				panelBottomSperren.eventYouAreSelected(false);
				panelQuerySperren.updateButtons();

			} else if (e.getSource() == panelQueryVorschlagstext) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				panelBottomVorschlagstext.setKeyWhenDetailPanel(key);
				panelBottomVorschlagstext.eventYouAreSelected(false);
				panelQueryVorschlagstext.updateButtons();

			} else if (e.getSource() == panelQueryWebshop) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				panelBottomWebshop.setKeyWhenDetailPanel(key);
				panelBottomWebshop.eventYouAreSelected(false);
				panelQueryWebshop.updateButtons();

			}

		} else if (e.getID() == ItemChangedEvent.ACTION_UPDATE) {
			if (e.getSource() == panelBottomArtikelklassen) {
				panelQueryArtikelklassen.updateButtons(new LockStateValue(
						PanelBasis.LOCK_FOR_NEW));
			} else if (e.getSource() == panelBottomArtikelgruppen) {
				panelQueryArtikelgruppen.updateButtons(new LockStateValue(
						PanelBasis.LOCK_FOR_NEW));
			} else if (e.getSource() == panelBottomHersteller) {
				panelQueryHersteller.updateButtons(new LockStateValue(
						PanelBasis.LOCK_FOR_NEW));
			} else if (e.getSource() == panelBottomVerleih) {
				panelQueryVerleih.updateButtons(new LockStateValue(
						PanelBasis.LOCK_FOR_NEW));
			} else if (e.getSource() == panelBottomLager) {
				panelQueryLager.updateButtons(new LockStateValue(
						PanelBasis.LOCK_FOR_NEW));
			} else if (e.getSource() == panelBottomArtikelkommentarart) {
				panelQueryArtikelkommentarart.updateButtons(new LockStateValue(
						PanelBasis.LOCK_FOR_NEW));
			} else if (e.getSource() == panelBottomFarbcode) {
				panelQueryFarbcode.updateButtons(new LockStateValue(
						PanelBasis.LOCK_FOR_NEW));
			} else if (e.getSource() == panelBottomSperren) {
				panelQuerySperren.updateButtons(new LockStateValue(
						PanelBasis.LOCK_FOR_NEW));
			} else if (e.getSource() == panelBottomLagerplatz) {
				panelQueryLagerplatz.updateButtons(new LockStateValue(
						PanelBasis.LOCK_FOR_NEW));
			} else if (e.getSource() == panelBottomVorschlagstext) {
				panelQueryVorschlagstext.updateButtons(new LockStateValue(
						PanelBasis.LOCK_FOR_NEW));
			} else if (e.getSource() == panelBottomWebshop) {
				panelQueryWebshop.updateButtons(new LockStateValue(
						PanelBasis.LOCK_FOR_NEW));
			}
		}

		else if (e.getID() == ItemChangedEvent.ACTION_YOU_ARE_SELECTED) {
			refreshTitle();
			panelSplitArtikelklassen.eventYouAreSelected(false);
		} else if (e.getID() == ItemChangedEvent.ACTION_NEW) {
			if (e.getSource() == panelQueryArtikelklassen) {
				panelBottomArtikelklassen.eventActionNew(e, true, false);
				panelBottomArtikelklassen.eventYouAreSelected(false);
			} else if (e.getSource() == panelQueryHersteller) {
				panelBottomHersteller.eventActionNew(e, true, false);
				panelBottomHersteller.eventYouAreSelected(false);
			} else if (e.getSource() == panelQueryArtikelgruppen) {
				panelBottomArtikelgruppen.eventActionNew(e, true, false);
				panelBottomArtikelgruppen.eventYouAreSelected(false);
			} else if (e.getSource() == panelQueryLager) {
				panelBottomLager.eventActionNew(e, true, false);
				panelBottomLager.eventYouAreSelected(false);
			} else if (e.getSource() == panelQueryArtikelkommentarart) {
				panelBottomArtikelkommentarart.eventActionNew(e, true, false);
				panelBottomArtikelkommentarart.eventYouAreSelected(false);
			} else if (e.getSource() == panelQueryFarbcode) {
				panelBottomFarbcode.eventActionNew(e, true, false);
				panelBottomFarbcode.eventYouAreSelected(false);
			} else if (e.getSource() == panelQueryLagerplatz) {
				panelBottomLagerplatz.eventActionNew(e, true, false);
				panelBottomLagerplatz.eventYouAreSelected(false);
			} else if (e.getSource() == panelQueryVerleih) {
				panelBottomVerleih.eventActionNew(e, true, false);
				panelBottomVerleih.eventYouAreSelected(false);
			} else if (e.getSource() == panelQuerySperren) {
				panelBottomSperren.eventActionNew(e, true, false);
				panelBottomSperren.eventYouAreSelected(false);
			} else if (e.getSource() == panelQueryVorschlagstext) {
				panelBottomVorschlagstext.eventActionNew(e, true, false);
				panelBottomVorschlagstext.eventYouAreSelected(false);
			} else if (e.getSource() == panelQueryWebshop) {
				panelBottomWebshop.eventActionNew(e, true, false);
				panelBottomWebshop.eventYouAreSelected(false);
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_DISCARD) {
			if (e.getSource() == panelBottomArtikelklassen) {
				panelSplitArtikelklassen.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomArtikelgruppen) {
				panelSplitArtikelgruppen.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomHersteller) {
				panelSplitHersteller.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomLager) {
				panelSplitLager.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomLagerplatz) {
				panelSplitLagerplatz.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomArtikelkommentarart) {
				panelSplitArtikelkommentarart.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomFarbcode) {
				panelSplitFarbcode.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomVerleih) {
				panelSplitVerleih.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomSperren) {
				panelSplitSperren.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomVorschlagstext) {
				panelSplitVorschlagstext.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomWebshop) {
				panelSplitWebshop.eventYouAreSelected(false);
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_SAVE) {
			if (e.getSource() == panelBottomArtikelklassen) {
				Object oKey = panelBottomArtikelklassen.getKeyWhenDetailPanel();
				panelQueryArtikelklassen.eventYouAreSelected(false);
				panelQueryArtikelklassen.setSelectedId(oKey);
				panelSplitArtikelklassen.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomArtikelgruppen) {
				Object oKey = panelBottomArtikelgruppen.getKeyWhenDetailPanel();
				panelQueryArtikelgruppen.eventYouAreSelected(false);
				panelQueryArtikelgruppen.setSelectedId(oKey);
				panelSplitArtikelgruppen.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomHersteller) {
				Object oKey = panelBottomHersteller.getKeyWhenDetailPanel();
				panelQueryHersteller.eventYouAreSelected(false);
				panelQueryHersteller.setSelectedId(oKey);
				panelSplitHersteller.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomLager) {
				Object oKey = panelBottomLager.getKeyWhenDetailPanel();
				panelQueryLager.eventYouAreSelected(false);
				panelQueryLager.setSelectedId(oKey);
				panelSplitLager.eventYouAreSelected(false);

			} else if (e.getSource() == panelBottomVerleih) {
				Object oKey = panelBottomVerleih.getKeyWhenDetailPanel();
				panelQueryVerleih.eventYouAreSelected(false);
				panelQueryVerleih.setSelectedId(oKey);
				panelSplitVerleih.eventYouAreSelected(false);

			} else if (e.getSource() == panelBottomArtikelkommentarart) {
				Object oKey = panelBottomArtikelkommentarart
						.getKeyWhenDetailPanel();
				panelQueryArtikelkommentarart.eventYouAreSelected(false);
				panelQueryArtikelkommentarart.setSelectedId(oKey);
				panelSplitArtikelkommentarart.eventYouAreSelected(false);

			} else if (e.getSource() == panelBottomFarbcode) {
				Object oKey = panelBottomFarbcode.getKeyWhenDetailPanel();
				panelQueryFarbcode.eventYouAreSelected(false);
				panelQueryFarbcode.setSelectedId(oKey);
				panelSplitFarbcode.eventYouAreSelected(false);

			} else if (e.getSource() == panelBottomSperren) {
				Object oKey = panelBottomSperren.getKeyWhenDetailPanel();
				panelQuerySperren.eventYouAreSelected(false);
				panelQuerySperren.setSelectedId(oKey);
				panelSplitSperren.eventYouAreSelected(false);

			} else if (e.getSource() == panelBottomLagerplatz) {
				Object oKey = panelBottomLagerplatz.getKeyWhenDetailPanel();
				panelQueryLagerplatz.eventYouAreSelected(false);
				panelQueryLagerplatz.setSelectedId(oKey);
				panelSplitLagerplatz.eventYouAreSelected(false);

			} else if (e.getSource() == panelBottomVorschlagstext) {
				Object oKey = panelBottomVorschlagstext.getKeyWhenDetailPanel();
				panelQueryVorschlagstext.eventYouAreSelected(false);
				panelQueryVorschlagstext.setSelectedId(oKey);
				panelSplitVorschlagstext.eventYouAreSelected(false);

			} else if (e.getSource() == panelBottomWebshop) {
				Object oKey = panelBottomWebshop.getKeyWhenDetailPanel();
				panelQueryWebshop.eventYouAreSelected(false);
				panelQueryWebshop.setSelectedId(oKey);
				panelSplitWebshop.eventYouAreSelected(false);

			}
		} else if (e.getID() == ItemChangedEvent.ACTION_GOTO_MY_DEFAULT_QP) {
			if (e.getSource() == panelBottomArtikelklassen) {
				Object oKey = panelQueryArtikelklassen.getSelectedId();
				if (oKey != null) {
					getInternalFrame().setKeyWasForLockMe(oKey.toString());
				} else {
					getInternalFrame().setKeyWasForLockMe(null);
				}
				if (panelBottomArtikelklassen.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryArtikelklassen
							.getId2SelectAfterDelete();
					panelQueryArtikelklassen.setSelectedId(oNaechster);
				}
				panelSplitArtikelklassen.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomArtikelgruppen) {
				Object oKey = panelQueryArtikelgruppen.getSelectedId();
				if (oKey != null) {
					getInternalFrame().setKeyWasForLockMe(oKey.toString());
				} else {
					getInternalFrame().setKeyWasForLockMe(null);
				}
				if (panelBottomArtikelgruppen.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryArtikelgruppen
							.getId2SelectAfterDelete();
					panelQueryArtikelgruppen.setSelectedId(oNaechster);
				}
				panelSplitArtikelgruppen.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomHersteller) {
				Object oKey = panelQueryHersteller.getSelectedId();
				if (oKey != null) {
					getInternalFrame().setKeyWasForLockMe(oKey.toString());
				} else {
					getInternalFrame().setKeyWasForLockMe(null);
				}
				if (panelBottomHersteller.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryHersteller
							.getId2SelectAfterDelete();
					panelQueryHersteller.setSelectedId(oNaechster);
				}
				panelSplitHersteller.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomVerleih) {
				Object oKey = panelQueryVerleih.getSelectedId();
				if (oKey != null) {
					getInternalFrame().setKeyWasForLockMe(oKey.toString());
				} else {
					getInternalFrame().setKeyWasForLockMe(null);
				}
				if (panelBottomVerleih.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryVerleih
							.getId2SelectAfterDelete();
					panelQueryVerleih.setSelectedId(oNaechster);
				}
				panelSplitVerleih.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomLager) {
				Object oKey = panelQueryLager.getSelectedId();
				if (oKey != null) {
					getInternalFrame().setKeyWasForLockMe(oKey.toString());
				} else {
					getInternalFrame().setKeyWasForLockMe(null);
				}
				if (panelBottomLager.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryLager
							.getId2SelectAfterDelete();
					panelQueryLager.setSelectedId(oNaechster);
				}
				panelSplitLager.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomArtikelkommentarart) {
				Object oKey = panelQueryArtikelkommentarart.getSelectedId();
				if (oKey != null) {
					getInternalFrame().setKeyWasForLockMe(oKey.toString());
				} else {
					getInternalFrame().setKeyWasForLockMe(null);
				}
				if (panelBottomArtikelkommentarart.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryArtikelkommentarart
							.getId2SelectAfterDelete();
					panelQueryArtikelkommentarart.setSelectedId(oNaechster);
				}
				panelSplitArtikelkommentarart.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomFarbcode) {
				Object oKey = panelQueryFarbcode.getSelectedId();
				if (oKey != null) {
					getInternalFrame().setKeyWasForLockMe(oKey.toString());
				} else {
					getInternalFrame().setKeyWasForLockMe(null);
				}
				if (panelBottomFarbcode.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryFarbcode
							.getId2SelectAfterDelete();
					panelQueryFarbcode.setSelectedId(oNaechster);
				}
				panelSplitFarbcode.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomSperren) {
				Object oKey = panelQuerySperren.getSelectedId();
				if (oKey != null) {
					getInternalFrame().setKeyWasForLockMe(oKey.toString());
				} else {
					getInternalFrame().setKeyWasForLockMe(null);
				}
				if (panelBottomSperren.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQuerySperren
							.getId2SelectAfterDelete();
					panelQuerySperren.setSelectedId(oNaechster);
				}
				panelSplitSperren.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomLagerplatz) {
				Object oKey = panelQueryLagerplatz.getSelectedId();
				if (oKey != null) {
					getInternalFrame().setKeyWasForLockMe(oKey.toString());
				} else {
					getInternalFrame().setKeyWasForLockMe(null);
				}
				if (panelBottomLagerplatz.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryLagerplatz
							.getId2SelectAfterDelete();
					panelQueryLagerplatz.setSelectedId(oNaechster);
				}
				panelSplitLagerplatz.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomVorschlagstext) {
				Object oKey = panelQueryVorschlagstext.getSelectedId();
				if (oKey != null) {
					getInternalFrame().setKeyWasForLockMe(oKey.toString());
				} else {
					getInternalFrame().setKeyWasForLockMe(null);
				}
				if (panelBottomVorschlagstext.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryVorschlagstext
							.getId2SelectAfterDelete();
					panelQueryVorschlagstext.setSelectedId(oNaechster);
				}
				panelSplitVorschlagstext.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomWebshop) {
				Object oKey = panelQueryWebshop.getSelectedId();
				if (oKey != null) {
					getInternalFrame().setKeyWasForLockMe(oKey.toString());
				} else {
					getInternalFrame().setKeyWasForLockMe(null);
				}
				if (panelBottomWebshop.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryWebshop
							.getId2SelectAfterDelete();
					panelQueryWebshop.setSelectedId(oNaechster);
				}
				panelSplitWebshop.eventYouAreSelected(false);
			}
		}

	}

	private void refreshTitle() {

		getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_OHRWASCHLUNTEN,
				LPMain.getInstance().getTextRespectUISPr("lp.grunddaten"));
		getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_OHRWASCHLOBEN,
				((PanelBasis) this.getSelectedComponent()).getAdd2Title());
		getInternalFrame().setLpTitle(3, "");

	}

	public void lPEventObjectChanged(ChangeEvent e) throws Throwable {
		super.lPEventObjectChanged(e);
		int selectedIndex = this.getSelectedIndex();

		if (selectedIndex == IDX_PANEL_ARTIKELKLASSEN) {
			createArtikelklassen();
			panelSplitArtikelklassen.eventYouAreSelected(false);
			panelQueryArtikelklassen.updateButtons();
		}

		else if (selectedIndex == IDX_PANEL_ARTIKELGRUPPEN) {
			createArtikelgruppen();
			panelSplitArtikelgruppen.eventYouAreSelected(false);
			panelQueryArtikelgruppen.updateButtons();
		}

		else if (selectedIndex == IDX_PANEL_HERSTELLER) {
			createHersteller();
			panelSplitHersteller.eventYouAreSelected(false);
			panelQueryHersteller.updateButtons();
		} else if (selectedIndex == IDX_PANEL_LAGER) {
			createLager();
			panelSplitLager.eventYouAreSelected(false);
			panelQueryLager.updateButtons();
		} else if (selectedIndex == IDX_PANEL_ARTIKELKOMMENTARART) {
			createArtikelkommentarart();
			panelSplitArtikelkommentarart.eventYouAreSelected(false);
			panelQueryArtikelkommentarart.updateButtons();
		} else if (selectedIndex == IDX_PANEL_FARBCODE) {
			createFarbcode();
			panelSplitFarbcode.eventYouAreSelected(false);
			panelQueryFarbcode.updateButtons();
		} else if (selectedIndex == IDX_PANEL_SPERREN) {
			createSperren();
			panelSplitSperren.eventYouAreSelected(false);
			panelQuerySperren.updateButtons();
		} else if (selectedIndex == IDX_PANEL_LAGERPLATZ) {
			createLagerplatz();
			panelSplitLagerplatz.eventYouAreSelected(false);
			panelQueryLagerplatz.updateButtons();
		} else if (selectedIndex == IDX_PANEL_VERLEIH) {
			createVerleih();
			panelSplitVerleih.eventYouAreSelected(false);
			panelQueryVerleih.updateButtons();
		} else if (selectedIndex == IDX_PANEL_VORSCHLAGSTEXT) {
			createVorschlagstext();
			panelSplitVorschlagstext.eventYouAreSelected(false);
			panelQueryVorschlagstext.updateButtons();
		} else if (selectedIndex == IDX_PANEL_WEBSHOP) {
			createWebshop();
			panelSplitWebshop.eventYouAreSelected(false);
			panelQueryWebshop.updateButtons();
		}

	}

	protected void lPActionEvent(java.awt.event.ActionEvent e) {

	}

	public javax.swing.JMenuBar getJMenuBar() throws Throwable {
		if (wrapperManuBar == null) {
			wrapperManuBar = new WrapperMenuBar(this);
		}
		return wrapperManuBar;
	}

}
