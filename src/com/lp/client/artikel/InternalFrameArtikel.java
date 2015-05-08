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
package com.lp.client.artikel;

import java.text.MessageFormat;
import java.util.EventObject;

import javax.swing.ImageIcon;
import javax.swing.JTabbedPane;

import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.InventurDto;
import com.lp.server.artikel.service.LagerDto;
import com.lp.server.artikel.service.LagerFac;
import com.lp.server.artikel.service.MaterialDto;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.rechnung.service.RechnungDto;
import com.lp.server.system.service.MandantFac;

@SuppressWarnings("static-access")
/*
 * <p>Rahmenfenster fuer das Modul Artikel.</p> <p>Copyright Logistik Pur
 * Software GmbH (c) 2004-2008</p> <p>Erstellungsdatum 2004-11-25</p>j <p> </p>
 * 
 * @author Christian Kollmann
 * 
 * @version $Revision: 1.11 $
 */
public class InternalFrameArtikel extends InternalFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private TabbedPaneArtikel tabbedPaneArtikel = null;
	private TabbedPaneHandlagerbewegung tabbedPaneHandlagerbewegung = null;
	private TabbedPaneMaterial tabbedPaneMaterial = null;
	private TabbedPaneInventur tabbedPaneInventur = null;
	private TabbedPaneVkpfPreislisten tabbedPaneVkpfPreislistenname = null;
	private TabbedPaneArtikelgrunddaten tabbedPaneGrunddaten = null;
	private TabbedPaneShopgruppe tabbedPaneShopgruppe = null;
	private TabbedPaneLagercockpit tabbedPaneLagercockpit = null;
	private TabbedPaneKundenartikelnummern tabbedPaneKundenartikelnummern = null;

	public static int IDX_TABBED_PANE_ARTIKEL = -1;
	public int IDX_TABBED_PANE_HANDLAGERBEWEGUNG = -1;
	public int IDX_TABBED_PANE_MATERIAL = -1;
	public int IDX_TABBED_PANE_INVENTUR = -1;
	public int IDX_TABBED_PANE_VKPF_PREISLISTEN = -1;
	public int IDX_TABBED_PANE_SHOPGRUPPE = -1;
	public int IDX_TABBED_PANE_LAGERCOCKPIT = -1;
	public int IDX_TABBED_PANE_GRUNDDATEN = -1;
	public static int IDX_TABBED_PANE_KUNDENARTIKELNUMMERN = -1;

	public String sRechtModulweit = null;

	private ArtikelDto artikelDto = null;
	private InventurDto inventurDto;

	public MaterialDto materialDto = null;

	public void setTabbedPaneArtikel(TabbedPaneArtikel tabbedPaneArtikel) {
		this.tabbedPaneArtikel = tabbedPaneArtikel;
	}

	public void setArtikelDto(ArtikelDto artikelDto) {
		this.artikelDto = artikelDto;
	}

	public void setInventurDto(InventurDto inventurDto) {
		this.inventurDto = inventurDto;
	}

	public TabbedPaneArtikel getTabbedPaneArtikel() {
		return tabbedPaneArtikel;
	}
	public TabbedPaneKundenartikelnummern getTabbedPaneKundenartikelnummern() {
		return tabbedPaneKundenartikelnummern;
	}

	public TabbedPaneShopgruppe getTabbedPaneShopgruppe() {
		return tabbedPaneShopgruppe;
	}

	public TabbedPaneInventur getTabbedPaneInventur() {
		return tabbedPaneInventur;
	}

	public TabbedPaneVkpfPreislisten getTabbedPaneVkpfPreislistenname() {
		return tabbedPaneVkpfPreislistenname;
	}

	public ArtikelDto getArtikelDto() {
		return artikelDto;
	}

	public InventurDto getInventurDto() {
		return inventurDto;
	}

	// SP3188
	private void pruefeHauptlager() throws Throwable {

		String mandantCNr = LPMain.getTheClient().getMandant();

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
			mandantCNr= DelegateFactory.getInstance().getSystemDelegate().getHauptmandant();
		} else {

		}

		LagerDto[] lagerDtos = DelegateFactory.getInstance().getLagerDelegate()
				.lagerFindByMandantCNr(mandantCNr);

		boolean bHauptlagervorhanden = false;
		for (LagerDto laDto : lagerDtos) {
			if (laDto.getLagerartCNr().equals(LagerFac.LAGERART_HAUPTLAGER)) {
				bHauptlagervorhanden = true;
			}

		}

		if (bHauptlagervorhanden == false && lagerDtos.length > 0) {
			// Das erste Lager als Hauptlager deklarieren
			lagerDtos[0].setLagerartCNr(LagerFac.LAGERART_HAUPTLAGER);
			DelegateFactory.getInstance().getLagerDelegate()
					.updateLager(lagerDtos[0]);

			MessageFormat mf = new MessageFormat(
					LPMain.getTextRespectUISPr("auft.mandant.hauptlager.fehlt.ergaenzt"));
			mf.setLocale(LPMain.getTheClient().getLocUi());

			Object[] pattern = new Object[] { "\"" + lagerDtos[0].getCNr()
					+ "\"" };
			String sMsg = mf.format(pattern);

			DialogFactory.showModalDialog(LPMain.getInstance()
					.getTextRespectUISPr("lp.hint"), sMsg);
		}

	}

	public InternalFrameArtikel(String title, String belegartCNr,
			String sRechtModulweitI) throws Throwable {

		super(title, belegartCNr, sRechtModulweitI);
		sRechtModulweit = sRechtModulweitI;
		pruefeHauptlager();
		jbInit();
		initComponents();
	}

	private void jbInit() throws Throwable {
		// TabbedPane Artikel

		int tabIndex = 0;
		IDX_TABBED_PANE_ARTIKEL = tabIndex;

		tabbedPaneRoot.insertTab(
				LPMain.getInstance().getTextRespectUISPr("lp.artikel"), null,
				null, LPMain.getInstance().getTextRespectUISPr("lp.artikel"),
				IDX_TABBED_PANE_ARTIKEL);

		tabIndex++;
		IDX_TABBED_PANE_HANDLAGERBEWEGUNG = tabIndex;
		tabbedPaneRoot.insertTab(
				LPMain.getInstance().getTextRespectUISPr(
						"artikel.title.panel.handlagerbewegungen"),
				null,
				null,
				LPMain.getInstance().getTextRespectUISPr(
						"artikel.title.panel.handlagerbewegungen"),
				IDX_TABBED_PANE_HANDLAGERBEWEGUNG);
		if (!LPMain
				.getInstance()
				.getDesktop()
				.darfAnwenderAufZusatzfunktionZugreifen(
						MandantFac.ZUSATZFUNKTION_ZENTRALER_ARTIKELSTAMM)
				|| LPMain
						.getTheClient()
						.getMandant()
						.equalsIgnoreCase(
								DelegateFactory.getInstance()
										.getSystemDelegate().getHauptmandant())) {
			tabIndex++;
			IDX_TABBED_PANE_MATERIAL = tabIndex;
			tabbedPaneRoot.insertTab(
					LPMain.getInstance().getTextRespectUISPr("label.material"),
					null, null,
					LPMain.getInstance().getTextRespectUISPr("label.material"),
					IDX_TABBED_PANE_MATERIAL);
			tabIndex++;
			IDX_TABBED_PANE_INVENTUR = tabIndex;
			tabbedPaneRoot.insertTab(
					LPMain.getInstance()
							.getTextRespectUISPr("artikel.inventur"), null,
					null,
					LPMain.getInstance()
							.getTextRespectUISPr("artikel.inventur"),
					IDX_TABBED_PANE_INVENTUR);
			// Komponente Preislisten vkpf:

			if (bRechtDarfPreiseSehenVerkauf) {

				tabIndex++;
				IDX_TABBED_PANE_VKPF_PREISLISTEN = tabIndex;
				tabbedPaneRoot.insertTab(
						LPMain.getInstance().getTextRespectUISPr(
								"vkpf.preislisten.title.tab"),
						null,
						null,
						LPMain.getInstance().getTextRespectUISPr(
								"vkpf.preislisten.title.tooltip"),
						IDX_TABBED_PANE_VKPF_PREISLISTEN);
			}
			tabIndex++;
			IDX_TABBED_PANE_SHOPGRUPPE = tabIndex;
			tabbedPaneRoot.insertTab(
					LPMain.getInstance().getTextRespectUISPr("lp.shopgruppe"),
					null, null,
					LPMain.getInstance().getTextRespectUISPr("lp.shopgruppe"),
					IDX_TABBED_PANE_SHOPGRUPPE);

			if (LPMain
					.getInstance()
					.getDesktop()
					.darfAnwenderAufZusatzfunktionZugreifen(
							MandantFac.ZUSATZFUNKTION_LAGERCOCKPIT)) {
				if (DelegateFactory.getInstance().getTheJudgeDelegate()
						.hatRecht(RechteFac.RECHT_WW_LAGERCOCKPIT)) {
					tabIndex++;
					IDX_TABBED_PANE_LAGERCOCKPIT = tabIndex;
					tabbedPaneRoot.insertTab(
							LPMain.getInstance().getTextRespectUISPr(
									"ww.lagercockpit"),
							null,
							null,
							LPMain.getInstance().getTextRespectUISPr(
									"ww.lagercockpit"),
							IDX_TABBED_PANE_LAGERCOCKPIT);
				}
			}

			// nur anzeigen wenn SOKOs
			if (LPMain
					.getInstance()
					.getDesktop()
					.darfAnwenderAufZusatzfunktionZugreifen(
							MandantFac.ZUSATZFUNKTION_KUNDESONDERKONDITIONEN)
					&& bRechtDarfPreiseSehenVerkauf) {
				tabIndex++;
				IDX_TABBED_PANE_KUNDENARTIKELNUMMERN = tabIndex;
				tabbedPaneRoot.insertTab(
						LPMain.getInstance().getTextRespectUISPr(
								"artikel.kundenartikelnummern"),
						null,
						null,
						LPMain.getInstance().getTextRespectUISPr(
								"artikel.kundenartikelnummern"),
						IDX_TABBED_PANE_KUNDENARTIKELNUMMERN);
			}

			// nur anzeigen wenn Benutzer Recht dazu hat
			if (DelegateFactory.getInstance().getTheJudgeDelegate()
					.hatRecht(RechteFac.RECHT_LP_DARF_GRUNDDATEN_SEHEN)) {
				tabIndex++;
				IDX_TABBED_PANE_GRUNDDATEN = tabIndex;
				tabbedPaneRoot.insertTab(
						LPMain.getInstance().getTextRespectUISPr(
								"lp.grunddaten"),
						null,
						null,
						LPMain.getInstance().getTextRespectUISPr(
								"lp.grunddaten"), IDX_TABBED_PANE_GRUNDDATEN);
			}

		} else {

			if (LPMain
					.getInstance()
					.getDesktop()
					.darfAnwenderAufZusatzfunktionZugreifen(
							MandantFac.ZUSATZFUNKTION_ZENTRALER_ARTIKELSTAMM)
					&& LPMain
							.getInstance()
							.getDesktop()
							.darfAnwenderAufZusatzfunktionZugreifen(
									MandantFac.ZUSATZFUNKTION_GETRENNTE_LAGER)) {
				tabIndex++;
				IDX_TABBED_PANE_INVENTUR = tabIndex;
				tabbedPaneRoot.insertTab(
						LPMain.getInstance().getTextRespectUISPr(
								"artikel.inventur"),
						null,
						null,
						LPMain.getInstance().getTextRespectUISPr(
								"artikel.inventur"), IDX_TABBED_PANE_INVENTUR);
			}

			if (bRechtDarfPreiseSehenVerkauf) {

				tabIndex++;
				IDX_TABBED_PANE_VKPF_PREISLISTEN = tabIndex;
				tabbedPaneRoot.insertTab(
						LPMain.getInstance().getTextRespectUISPr(
								"vkpf.preislisten.title.tab"),
						null,
						null,
						LPMain.getInstance().getTextRespectUISPr(
								"vkpf.preislisten.title.tooltip"),
						IDX_TABBED_PANE_VKPF_PREISLISTEN);
			}

			// nur anzeigen wenn SOKOs und VK-Preis-Recht
			if (LPMain
					.getInstance()
					.getDesktop()
					.darfAnwenderAufZusatzfunktionZugreifen(
							MandantFac.ZUSATZFUNKTION_KUNDESONDERKONDITIONEN)
					&& bRechtDarfPreiseSehenVerkauf) {
				tabIndex++;
				IDX_TABBED_PANE_KUNDENARTIKELNUMMERN = tabIndex;
				tabbedPaneRoot.insertTab(
						LPMain.getInstance().getTextRespectUISPr(
								"artikel.kundenartikelnummern"),
						null,
						null,
						LPMain.getInstance().getTextRespectUISPr(
								"artikel.kundenartikelnummern"),
						IDX_TABBED_PANE_KUNDENARTIKELNUMMERN);
			}

			if (LPMain
					.getInstance()
					.getDesktop()
					.darfAnwenderAufZusatzfunktionZugreifen(
							MandantFac.ZUSATZFUNKTION_ZENTRALER_ARTIKELSTAMM)
					&& LPMain
							.getInstance()
							.getDesktop()
							.darfAnwenderAufZusatzfunktionZugreifen(
									MandantFac.ZUSATZFUNKTION_GETRENNTE_LAGER)) {
				if (DelegateFactory.getInstance().getTheJudgeDelegate()
						.hatRecht(RechteFac.RECHT_LP_DARF_GRUNDDATEN_SEHEN)) {
					tabIndex++;
					IDX_TABBED_PANE_GRUNDDATEN = tabIndex;
					tabbedPaneRoot.insertTab(
							LPMain.getInstance().getTextRespectUISPr(
									"lp.grunddaten"),
							null,
							null,
							LPMain.getInstance().getTextRespectUISPr(
									"vkpf.preislisten.title.tooltip"),
							IDX_TABBED_PANE_GRUNDDATEN);
				}
			}
		}
		// ich selbst moechte informiert werden.
		addItemChangedListener(this);
		registerChangeListeners();
		createTabbedPaneArtikel(null);

		// usemenubar 2: Nachfolgende Zeile muss an dieser Stelle im jbInit des
		// InternalFrame vorhanden sein
		tabbedPaneArtikel.lPEventObjectChanged(null);

		tabbedPaneRoot.setSelectedComponent(tabbedPaneArtikel);
		// iicon: hier das li/on icon gemacht
		ImageIcon iicon = new javax.swing.ImageIcon(getClass().getResource(
				"/com/lp/client/res/nut_and_bolt16x16.png"));
		setFrameIcon(iicon);
	}

	public void lPEventItemChanged(ItemChangedEvent e) {
		// nothing here
	}

	private void createTabbedPanePreislisten(JTabbedPane tabbedPane)
			throws Throwable { // vkpf:
		if (tabbedPane == null) {
			// lazy loading
			tabbedPaneVkpfPreislistenname = new TabbedPaneVkpfPreislisten(this);
			tabbedPaneRoot.setComponentAt(IDX_TABBED_PANE_VKPF_PREISLISTEN,
					tabbedPaneVkpfPreislistenname);
			initComponents();
		}
	}

	private void createTabbedPaneKundenartikelnummern(JTabbedPane tabbedPane)
			throws Throwable { // vkpf:
		if (tabbedPane == null) {
			// lazy loading
			tabbedPaneKundenartikelnummern = new TabbedPaneKundenartikelnummern(
					this);
			tabbedPaneRoot.setComponentAt(IDX_TABBED_PANE_KUNDENARTIKELNUMMERN,
					tabbedPaneKundenartikelnummern);
			initComponents();
		}
	}

	public void lPStateChanged(EventObject e) throws Throwable {
		JTabbedPane tabbedPane = (JTabbedPane) ((JTabbedPane) e.getSource())
				.getSelectedComponent();
		int selectedCur = ((JTabbedPane) e.getSource()).getSelectedIndex();
		setRechtModulweit(sRechtModulweit);
		if (selectedCur == IDX_TABBED_PANE_ARTIKEL) {
			createTabbedPaneArtikel(tabbedPane);
			// Info an Tabbedpane, bist selektiert worden.
			tabbedPaneArtikel.lPEventObjectChanged(null);
			// tabbedPaneArtikel.changed(new ItemChangedEvent(this,
			// ItemChangedEvent.ACTION_YOU_ARE_SELECTED));
		} else if (selectedCur == IDX_TABBED_PANE_HANDLAGERBEWEGUNG) {

			boolean hatRecht = !DelegateFactory.getInstance()
					.getTheJudgeDelegate()
					.hatRecht(RechteFac.RECHT_WW_ARTIKEL_CUD);
			if (hatRecht == true) {
				setRechtModulweit(RechteFac.RECHT_MODULWEIT_UPDATE);
			}

			createTabbedPaneHandlagerbewegung(tabbedPane);
			// Info an Tabbedpane, bist selektiert worden.
			tabbedPaneHandlagerbewegung.lPEventObjectChanged(null);
		} else if (selectedCur == IDX_TABBED_PANE_MATERIAL) {
			createTabbedPaneMaterial(tabbedPane);
			// Info an Tabbedpane, bist selektiert worden.
			tabbedPaneMaterial.lPEventObjectChanged(null);
		} else if (selectedCur == IDX_TABBED_PANE_INVENTUR) {
			createTabbedPaneInventur(tabbedPane);
			// Info an Tabbedpane, bist selektiert worden.
			tabbedPaneInventur.lPEventObjectChanged(null);
		} else if (selectedCur == IDX_TABBED_PANE_VKPF_PREISLISTEN) {
			createTabbedPanePreislisten(tabbedPane);
			// Info an Tabbedpane, bist selektiert worden.
			tabbedPaneVkpfPreislistenname.lPEventObjectChanged(null);
		} else if (selectedCur == IDX_TABBED_PANE_GRUNDDATEN) {
			createTabbedPaneGrunddaten(tabbedPane);
			// Info an Tabbedpane, bist selektiert worden.
			tabbedPaneGrunddaten.lPEventObjectChanged(null);
		} else if (selectedCur == IDX_TABBED_PANE_SHOPGRUPPE) {
			createTabbedPaneShopgruppe(tabbedPane);
			// Info an Tabbedpane, bist selektiert worden.
			tabbedPaneShopgruppe.lPEventObjectChanged(null);
		} else if (selectedCur == IDX_TABBED_PANE_LAGERCOCKPIT) {
			createTabbedPaneLagercockpit(tabbedPane);
			// Info an Tabbedpane, bist selektiert worden.
			tabbedPaneLagercockpit.lPEventObjectChanged(null);
		} else if (selectedCur == IDX_TABBED_PANE_KUNDENARTIKELNUMMERN) {
			createTabbedPaneKundenartikelnummern(tabbedPane);
			// Info an Tabbedpane, bist selektiert worden.
			tabbedPaneKundenartikelnummern.lPEventObjectChanged(null);
		}
	}

	private void createTabbedPaneHandlagerbewegung(JTabbedPane tabbedPane)
			throws Throwable {
		if (tabbedPane == null) {

			// lazy loading
			tabbedPaneHandlagerbewegung = new TabbedPaneHandlagerbewegung(this);
			tabbedPaneRoot.setComponentAt(IDX_TABBED_PANE_HANDLAGERBEWEGUNG,
					tabbedPaneHandlagerbewegung);
			initComponents();
		}
	}

	private void createTabbedPaneMaterial(JTabbedPane tabbedPane)
			throws Throwable {
		if (tabbedPane == null) {
			// lazy loading
			tabbedPaneMaterial = new TabbedPaneMaterial(this);
			tabbedPaneRoot.setComponentAt(IDX_TABBED_PANE_MATERIAL,
					tabbedPaneMaterial);
			if (tabbedPaneMaterial.getPanelQueryMaterial().getSelectedId() == null) {
				enableAllOberePanelsExceptMe(tabbedPaneMaterial, 0, false);
			}
			initComponents();
		}

	}

	private void createTabbedPaneShopgruppe(JTabbedPane tabbedPane)
			throws Throwable {
		if (tabbedPane == null) {
			// lazy loading
			tabbedPaneShopgruppe = new TabbedPaneShopgruppe(this);
			tabbedPaneRoot.setComponentAt(IDX_TABBED_PANE_SHOPGRUPPE,
					tabbedPaneShopgruppe);
			if (tabbedPaneShopgruppe.getPanelQueryShopgruppe().getSelectedId() == null) {
				enableAllOberePanelsExceptMe(tabbedPaneShopgruppe, 0, false);
			}
			initComponents();
		}

	}

	private void createTabbedPaneLagercockpit(JTabbedPane tabbedPane)
			throws Throwable {
		if (tabbedPane == null) {
			// lazy loading
			tabbedPaneLagercockpit = new TabbedPaneLagercockpit(this);
			tabbedPaneRoot.setComponentAt(IDX_TABBED_PANE_LAGERCOCKPIT,
					tabbedPaneLagercockpit);
			if (tabbedPaneLagercockpit.getPanelQueryArtikel().getSelectedId() == null) {
				enableAllOberePanelsExceptMe(tabbedPaneLagercockpit, 0, false);
			}
			initComponents();
		}

	}

	private void createTabbedPaneGrunddaten(JTabbedPane tabbedPane)
			throws Throwable {
		if (tabbedPane == null) {
			// lazy loading
			tabbedPaneGrunddaten = new TabbedPaneArtikelgrunddaten(this);
			tabbedPaneRoot.setComponentAt(IDX_TABBED_PANE_GRUNDDATEN,
					tabbedPaneGrunddaten);
			initComponents();
		}
	}

	private void createTabbedPaneArtikel(JTabbedPane tabbedPane)
			throws Throwable {
		if (tabbedPane == null) {
			// lazy loading
			tabbedPaneArtikel = new TabbedPaneArtikel(this);
			tabbedPaneRoot.setComponentAt(IDX_TABBED_PANE_ARTIKEL,
					tabbedPaneArtikel);
			if (tabbedPaneArtikel.getPanelQueryArtikel().getSelectedId() == null) {
				enableAllOberePanelsExceptMe(tabbedPaneArtikel, 0, false);
			}
			initComponents();
		}
	}

	private void createTabbedPaneInventur(JTabbedPane tabbedPane)
			throws Throwable {
		if (tabbedPane == null) {
			// lazy loading
			tabbedPaneInventur = new TabbedPaneInventur(this);
			tabbedPaneRoot.setComponentAt(IDX_TABBED_PANE_INVENTUR,
					tabbedPaneInventur);
			initComponents();
		}
	}
}
