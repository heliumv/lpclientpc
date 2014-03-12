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
package com.lp.client.rechnung;

import java.util.EventObject;

import javax.swing.ImageIcon;
import javax.swing.JTabbedPane;

import com.lp.client.finanz.TabbedPaneMahnwesen;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.Desktop;
import com.lp.client.pc.LPMain;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.finanz.service.MahnstufeDto;
import com.lp.server.finanz.service.MahntextDto;
import com.lp.server.rechnung.service.GutschriftpositionsartDto;
import com.lp.server.rechnung.service.GutschriftsgrundDto;
import com.lp.server.rechnung.service.GutschrifttextDto;
import com.lp.server.rechnung.service.ProformarechnungpositionsartDto;
import com.lp.server.rechnung.service.RechnungDto;
import com.lp.server.rechnung.service.RechnungFac;
import com.lp.server.rechnung.service.RechnungartDto;
import com.lp.server.rechnung.service.RechnungpositionsartDto;
import com.lp.server.rechnung.service.RechnungstatusDto;
import com.lp.server.rechnung.service.RechnungtextDto;
import com.lp.server.rechnung.service.RechnungtypDto;
import com.lp.server.rechnung.service.ZahlungsartDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.ModulberechtigungDto;

@SuppressWarnings("static-access")
/*
 * 
 * <p>Diese Klasse kuemmert sich um das Modul Rechnung</p> <p>Copyright Logistik
 * Pur Software GmbH (c) 2004-2008</p> <p>Erstellungsdatum 20.11.2004</p> <p>
 * </p>
 * 
 * @author Martin Bluehweis
 * 
 * @version $Revision: 1.12 $
 */
public class InternalFrameRechnung extends InternalFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	java.sql.Date neuDatum = null;

	private TabbedPaneRechnung tabbedPaneRechnung = null;
	private TabbedPaneGutschrift tabbedPaneGutschrift = null;
	private TabbedPaneProformarechnung tabbedPaneProformarechnung = null;
	private TabbedPaneMahnwesen tabbedPaneMahnwesen = null;
	private TabbedPaneGrunddaten tabbedPaneGrunddaten = null;

	public static final int IDX_TABBED_PANE_RECHNUNG = 0;
	// da das gutschriftsmodul optional ist, sind die weiteren indizes flexibel
	public int index = 0;
	public static int IDX_TABBED_PANE_GUTSCHRIFT = -1;
	public static int IDX_TABBED_PANE_MAHNWESEN = -1;
	public static int IDX_TABBED_PANE_GRUNDDATEN = -1;
	/**
	 * @todo reihenfolgen aendern, wenn proformarechnung wieder einschalten PJ
	 *       4750
	 */

	private int IDX_TABBED_PANE_PROFORMARECHNUNG = -1;

	private RechnungtextDto rechnungtextDto = new RechnungtextDto();
	private GutschrifttextDto gutschrifttextDto = new GutschrifttextDto();
	private MahntextDto mahntextDto = new MahntextDto();
	private RechnungartDto rechnungartDto = new RechnungartDto();
	private RechnungstatusDto rechnungstatusDto = new RechnungstatusDto();
	private RechnungpositionsartDto rechnungpositionsartDto = new RechnungpositionsartDto();
	private GutschriftpositionsartDto gutschriftpositionsartDto = new GutschriftpositionsartDto();
	private ProformarechnungpositionsartDto proformarechnungpositionsartDto = new ProformarechnungpositionsartDto();
	private RechnungtypDto rechnungtypDto = new RechnungtypDto();
	private ZahlungsartDto zahlungartDto = new ZahlungsartDto();
	private MahnstufeDto mahnstufeDto = new MahnstufeDto();
	private GutschriftsgrundDto gutschriftsgrundDto = new GutschriftsgrundDto();

	public InternalFrameRechnung(String title, String belegartCNr,
			String sRechtModulweitI) throws Throwable {
		super(title, belegartCNr, sRechtModulweitI);
		jbInit();
		initComponents();
	}

	private void jbInit() throws Throwable {
		boolean bMitGutschrift = false;
		boolean bMitProformarechnung = false;
		// Berechtigung pruefen
		ModulberechtigungDto[] modulberechtigungDtos = DelegateFactory
				.getInstance().getMandantDelegate()
				.modulberechtigungFindByMandantCNr();
		for (int i = 0; i < modulberechtigungDtos.length; i++) {
			if (modulberechtigungDtos[i].getBelegartCNr().equals(
					LocaleFac.BELEGART_GUTSCHRIFT)) {
				bMitGutschrift = true;
			}
			if (modulberechtigungDtos[i].getBelegartCNr().trim()
					.equals(LocaleFac.BELEGART_PROFORMARECHNUNG)) {
				bMitProformarechnung = true;
			}
		}

		index = 0;
		// 1 tab unten: Rechnungen
		tabbedPaneRoot.insertTab(
				LPMain.getInstance().getTextRespectUISPr(
						"rechnung.tab.unten.rechnung.title"),
				null,
				null,
				LPMain.getInstance().getTextRespectUISPr(
						"rechnung.tab.unten.rechnung.tooltip"),
				IDX_TABBED_PANE_RECHNUNG);
		index++;
		if (bMitGutschrift) {
			// 2 tab unten: Gutschriften
			IDX_TABBED_PANE_GUTSCHRIFT = index;
			tabbedPaneRoot.insertTab(
					LPMain.getInstance().getTextRespectUISPr(
							"rechnung.tab.unten.gutschrift.title"),
					null,
					null,
					LPMain.getInstance().getTextRespectUISPr(
							"rechnung.tab.unten.gutschrift.tooltip"),
					IDX_TABBED_PANE_GUTSCHRIFT);
			index++;
		}
		if (bMitProformarechnung) {
			// 3 tab unten: Proformarechnungen
			IDX_TABBED_PANE_PROFORMARECHNUNG = index;
			tabbedPaneRoot.insertTab(
					LPMain.getInstance().getTextRespectUISPr(
							"rechnung.tab.unten.proformarechnung.title"),
					null,
					null,
					LPMain.getInstance().getTextRespectUISPr(
							"rechnung.tab.unten.proformarechnung.tooltip"),
					IDX_TABBED_PANE_PROFORMARECHNUNG);
			index++;
		}
		// 4 tab unten: Mahnwesen
		IDX_TABBED_PANE_MAHNWESEN = index;
		tabbedPaneRoot.insertTab(
				LPMain.getInstance().getTextRespectUISPr(
						"finanz.tab.unten.mahnwesen.title"),
				null,
				null,
				LPMain.getInstance().getTextRespectUISPr(
						"finanz.tab.unten.mahnwesen.tooltip"),
				IDX_TABBED_PANE_MAHNWESEN);
		index++;

		// 4 tab unten: Grunddaten
		// nur anzeigen wenn Benutzer Recht dazu hat
		if (DelegateFactory.getInstance().getTheJudgeDelegate()
				.hatRecht(RechteFac.RECHT_LP_DARF_GRUNDDATEN_SEHEN)) {
			IDX_TABBED_PANE_GRUNDDATEN = index;
			tabbedPaneRoot.insertTab(
					LPMain.getInstance().getTextRespectUISPr(
							"pers.title.tab.grunddaten"),
					null,
					null,
					LPMain.getInstance().getTextRespectUISPr(
							"pers.title.tab.grunddaten"),
					IDX_TABBED_PANE_GRUNDDATEN);
		}
		index++;

		// default
		getTabbedPaneRechnung();

		tabbedPaneRechnung.lPEventObjectChanged(null);

		// initJMenu();
		// dem frame das icon setzen
		ImageIcon iicon = new javax.swing.ImageIcon(getClass().getResource(
				"/com/lp/client/res/calculator16x16.png"));
		setFrameIcon(iicon);

		// listener bin auch ich
		registerChangeListeners();
		tabbedPaneRoot.setSelectedComponent(getTabbedPaneRechnung());

		// Grunddaten enablen.
		// Nur wenn vorhanden durch das Recht
		if (DelegateFactory.getInstance().getTheJudgeDelegate()
				.hatRecht(RechteFac.RECHT_LP_DARF_GRUNDDATEN_SEHEN)) {
			tabbedPaneRoot.setEnabledAt(IDX_TABBED_PANE_GRUNDDATEN, true);
		}
	}

	public void lPStateChanged(EventObject e) throws Throwable {
		// TODO-AGILCHANGES
		/**
		 * AGILPRO CHANGES BEGIN
		 * 
		 * @author Lukas Lisowski
		 */
		int selectedCur = 0;

		try {
			selectedCur = ((JTabbedPane) e.getSource()).getSelectedIndex();
		} catch (Exception ex) {

			selectedCur = ((Desktop) e.getSource()).getSelectedIndex();
		}
		/**
		 * AGILPRO CHANGES END
		 */
		if (selectedCur == IDX_TABBED_PANE_RECHNUNG) {
			getTabbedPaneRechnung().lPEventObjectChanged(null);
		} else if (selectedCur == IDX_TABBED_PANE_GUTSCHRIFT) {
			getTabbedPaneGutschrift().lPEventObjectChanged(null);
		} else if (selectedCur == IDX_TABBED_PANE_PROFORMARECHNUNG) {
			getTabbedPaneProformarechnung().lPEventObjectChanged(null);
		} else if (selectedCur == IDX_TABBED_PANE_MAHNWESEN) {
			getTabbedPaneMahnwesen().lPEventObjectChanged(null);
		} else if (selectedCur == IDX_TABBED_PANE_GRUNDDATEN) {
			getTabbedPaneGrunddaten();
			tabbedPaneRoot.setSelectedComponent(tabbedPaneGrunddaten);
			// Info an Tabbedpane, bist selektiert worden.
			tabbedPaneGrunddaten.lPEventObjectChanged(null);
		}
	}

	public TabbedPaneRechnung getTabbedPaneRechnung() throws Throwable {
		if (tabbedPaneRechnung == null) {
			// lazy loading
			tabbedPaneRechnung = new TabbedPaneRechnung(this);
			tabbedPaneRoot.setComponentAt(IDX_TABBED_PANE_RECHNUNG,
					tabbedPaneRechnung);
			// if (tabbedPaneRechnung.getSelectedIIdRechnung() == null) {
			// enableAllPanelsExcept(false);
			// }
			initComponents();
		}
		return tabbedPaneRechnung;
	}

	private TabbedPaneGutschrift getTabbedPaneGutschrift() throws Throwable {
		if (tabbedPaneGutschrift == null) {
			// lazy loading
			tabbedPaneGutschrift = new TabbedPaneGutschrift(this);
			tabbedPaneRoot.setComponentAt(IDX_TABBED_PANE_GUTSCHRIFT,
					tabbedPaneGutschrift);
			// if (tabbedPaneGutschrift.getSelectedIIdRechnung() == null) {
			// enableAllPanelsExcept(false);
			// }
			initComponents();
		}
		return tabbedPaneGutschrift;
	}

	private TabbedPaneProformarechnung getTabbedPaneProformarechnung()
			throws Throwable {
		if (tabbedPaneProformarechnung == null) {
			// lazy loading
			tabbedPaneProformarechnung = new TabbedPaneProformarechnung(this);
			tabbedPaneRoot.setComponentAt(IDX_TABBED_PANE_PROFORMARECHNUNG,
					tabbedPaneProformarechnung);
			// if (tabbedPaneProformarechnung.getSelectedIIdRechnung() == null)
			// {
			// enableAllPanelsExcept(false);
			// }
			initComponents();
		}
		return tabbedPaneProformarechnung;
	}

	private TabbedPaneMahnwesen getTabbedPaneMahnwesen() throws Throwable {
		if (tabbedPaneMahnwesen == null) {
			// lazy loading
			tabbedPaneMahnwesen = new TabbedPaneMahnwesen(this);
			tabbedPaneRoot.setComponentAt(IDX_TABBED_PANE_MAHNWESEN,
					tabbedPaneMahnwesen);
			initComponents();
		}
		return tabbedPaneMahnwesen;
	}

	private TabbedPaneGrunddaten getTabbedPaneGrunddaten() throws Throwable {
		if (tabbedPaneGrunddaten == null) {
			// lazy loading
			tabbedPaneGrunddaten = new TabbedPaneGrunddaten(this);
			tabbedPaneRoot.setComponentAt(IDX_TABBED_PANE_GRUNDDATEN,
					tabbedPaneGrunddaten);
			initComponents();
		}
		return null; // tabbedPaneGrunddaten;
	}

	public boolean isUpdateAllowedForRechnungDto(RechnungDto rechnungDto)
			throws Throwable {
		if (rechnungDto != null) {
			
			Integer aktuelleMahnstufeIId=DelegateFactory.getInstance()
			.getMahnwesenDelegate()
			.getAktuelleMahnstufeEinerRechnung(rechnungDto.getIId());
			
			if (aktuelleMahnstufeIId != null) {
				DialogFactory
						.showModalDialog(
								LPMain.getInstance().getTextRespectUISPr(
										"lp.hint"),
								LPMain.getInstance().getTextRespectUISPr(
										"rechnung.rechnungbereitsgemahnt"));
				return false;
			}
			if (rechnungDto.getRechnungartCNr() != null
					&& rechnungDto.getRechnungartCNr().equals(
							RechnungFac.RECHNUNGART_ANZAHLUNG)
					&& rechnungDto.getAuftragIId() != null) {

				boolean bSchlussrechnung = DelegateFactory
						.getInstance()
						.getRechnungDelegate()
						.gibtEsBereitsEineSchlussrechnungZuEinemAuftrag(
								rechnungDto.getAuftragIId());

				if (bSchlussrechnung == true) {
					DialogFactory
							.showModalDialog(
									LPMain.getInstance().getTextRespectUISPr(
											"lp.hint"),
									LPMain.getInstance().getTextRespectUISPr(
											"rechnung.schlussrechnungbereitsvorhanden"));
					return false;
				}

			}
			if (rechnungDto.getStatusCNr().equals(RechnungFac.STATUS_ANGELEGT)) {
				return true;
			} else if (rechnungDto.getStatusCNr().equals(
					RechnungFac.STATUS_STORNIERT)) {
				boolean bStornieren = (DialogFactory.showMeldung(
						LPMain.getInstance().getTextRespectUISPr(
								"rechnung.stornoaufheben"), LPMain
								.getInstance().getTextRespectUISPr("lp.frage"),
						javax.swing.JOptionPane.YES_NO_OPTION) == javax.swing.JOptionPane.YES_OPTION);
				if (bStornieren == true) {
					DelegateFactory
							.getInstance()
							.getRechnungDelegate()
							.storniereRechnungRueckgaengig(rechnungDto.getIId());
					getTabbedPaneRechnung().reloadRechnungDto();
				}
				return bStornieren;
			} else if (rechnungDto.getStatusCNr().equals(
					RechnungFac.STATUS_OFFEN)
					|| rechnungDto.getStatusCNr().equals(
							RechnungFac.STATUS_VERBUCHT)) {
				String sText;
				RechnungartDto rechnungartDto = DelegateFactory
						.getInstance()
						.getRechnungServiceDelegate()
						.rechnungartFindByPrimaryKey(
								rechnungDto.getRechnungartCNr());
				if (rechnungartDto.getRechnungtypCNr().equals(
						RechnungFac.RECHNUNGTYP_GUTSCHRIFT)) {
					sText = LPMain.getInstance().getTextRespectUISPr(
							"rechnung.bereitsaktiviert.gutschrift");
				} else if (rechnungartDto.getRechnungtypCNr().equals(
						RechnungFac.RECHNUNGTYP_RECHNUNG)) {
					sText = LPMain.getInstance().getTextRespectUISPr(
							"rechnung.bereitsaktiviert.rechnung");
				} else {
					sText = LPMain.getInstance().getTextRespectUISPr(
							"rechnung.bereitsaktiviert.proformarechnung");
				}
				boolean bZuruecknehmen = (DialogFactory.showMeldung(sText,
						LPMain.getInstance().getTextRespectUISPr("lp.frage"),
						javax.swing.JOptionPane.YES_NO_OPTION) == javax.swing.JOptionPane.YES_OPTION);
				if (bZuruecknehmen == true) {
					DelegateFactory.getInstance().getRechnungDelegate()
							.setRechnungStatusAufAngelegt(rechnungDto.getIId());
					getTabbedPaneRechnung().reloadRechnungDto();
				}
				return bZuruecknehmen;
			} else if (rechnungDto.getStatusCNr().equals(
					RechnungFac.STATUS_TEILBEZAHLT)) {
				DialogFactory.showModalDialog(
						LPMain.getInstance().getTextRespectUISPr("lp.hint"),
						LPMain.getInstance().getTextRespectUISPr(
								"rechnung.essindbereitszahlungeneingetragen"));
				return false;
			} else if (rechnungDto.getStatusCNr().equals(
					RechnungFac.STATUS_BEZAHLT)) {
				if (DialogFactory.showMeldung(
						LPMain.getInstance().getTextRespectUISPr(
								"rech.rechnungistbereitserledigt"), LPMain
								.getInstance().getTextRespectUISPr("lp.hint"),
						javax.swing.JOptionPane.YES_NO_OPTION) == javax.swing.JOptionPane.YES_OPTION) {
					DelegateFactory.getInstance().getRechnungDelegate()
							.erledigungAufheben(rechnungDto.getIId());
				}
				return false;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	public void setRechnungartDto(RechnungartDto rechnungartDto) {
		this.rechnungartDto = rechnungartDto;
	}

	public void setRechnungstatusDto(RechnungstatusDto rechnungstatusDto) {
		this.rechnungstatusDto = rechnungstatusDto;
	}

	public void setRechnungpositionsartDto(
			RechnungpositionsartDto rechnungpositionsartDto) {
		this.rechnungpositionsartDto = rechnungpositionsartDto;
	}

	public void setGutschriftpositionsartDto(
			GutschriftpositionsartDto gutschriftpositionsartDto) {
		this.gutschriftpositionsartDto = gutschriftpositionsartDto;
	}

	public void setProformarechnungpositionsartDto(
			ProformarechnungpositionsartDto proformarechnungpositionsartDto) {
		this.proformarechnungpositionsartDto = proformarechnungpositionsartDto;
	}

	public void setRechnungtypDto(RechnungtypDto rechnungtypDto) {
		this.rechnungtypDto = rechnungtypDto;
	}

	public void setZahlungsartDto(ZahlungsartDto zahlungartsDto) {
		this.zahlungartDto = zahlungartsDto;
	}

	public RechnungartDto getRechnungartDto() {
		return rechnungartDto;
	}

	public RechnungstatusDto getRechnungstatusDto() {
		return rechnungstatusDto;
	}

	public RechnungpositionsartDto getRechnungpositionsartDto() {
		return rechnungpositionsartDto;
	}

	public GutschriftpositionsartDto getGutschriftpositionsartDto() {
		return gutschriftpositionsartDto;
	}

	public ProformarechnungpositionsartDto getProformarechnungpositionsartDto() {
		return proformarechnungpositionsartDto;
	}

	public RechnungtypDto getRechnungtypDto() {
		return rechnungtypDto;
	}

	public ZahlungsartDto getZahlungsartDto() {
		return zahlungartDto;
	}

	public RechnungtextDto getRechnungtextDto() {
		return rechnungtextDto;
	}

	public void setRechnungtextDto(RechnungtextDto rechnungtextDtoI) {
		this.rechnungtextDto = rechnungtextDtoI;
	}

	public GutschrifttextDto getGutschrifttextDto() {
		return gutschrifttextDto;
	}

	public void setGutschrifttextDto(GutschrifttextDto gutschrifttextDto) {
		this.gutschrifttextDto = gutschrifttextDto;
	}

	public GutschriftsgrundDto getGutschriftsgrundDto() {
		return gutschriftsgrundDto;
	}

	public void setGutschriftsgrundDto(GutschriftsgrundDto gutschriftsgrundDto) {
		this.gutschriftsgrundDto = gutschriftsgrundDto;
	}

	public MahntextDto getMahntextDto() {
		return mahntextDto;
	}

	public void setMahntextDto(MahntextDto mahntextDtoI) {
		this.mahntextDto = mahntextDtoI;
	}

	public MahnstufeDto getMahnstufeDto() {
		return mahnstufeDto;
	}

	public void setMahnstufeDto(MahnstufeDto mahnstufeDtoI) {
		this.mahnstufeDto = mahnstufeDtoI;
	}

	public java.sql.Date getNeuDatum() {
		return neuDatum;
	}
}
