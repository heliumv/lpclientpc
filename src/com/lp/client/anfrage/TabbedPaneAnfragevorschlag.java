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
package com.lp.client.anfrage;

import java.io.File;
import java.util.ArrayList;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.event.ChangeEvent;

import jxl.CellType;
import jxl.LabelCell;
import jxl.NumberCell;
import jxl.Sheet;
import jxl.Workbook;

import com.lp.client.bestellung.ReportBestellVorschlag;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.LockStateValue;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelDialogKriterien;
import com.lp.client.frame.component.PanelDialogKriterienBestellvorschlag2;
import com.lp.client.frame.component.PanelDialogKriterienBestellvorschlagAnhandStuecklistenmindestlagerstand;
import com.lp.client.frame.component.PanelDialogKriterienBestellvorschlagUeberleitung2;
import com.lp.client.frame.component.PanelDialogKriterienBestellvorschlagverdichtung;
import com.lp.client.frame.component.PanelPositionenBestellvorschlag;
import com.lp.client.frame.component.PanelQuery;
import com.lp.client.frame.component.PanelSplit;
import com.lp.client.frame.component.TabbedPane;
import com.lp.client.frame.component.WrapperMenuBar;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.stueckliste.SheetImportController;
import com.lp.client.system.SystemFilterFactory;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.stueckliste.service.StrukturierterImportDto;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.util.EJBExceptionLP;

/**
 * <p>
 * TabbedPane fuer Modul Anfrage/Anfragevorschlag.
 * </p>
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2008
 * </p>
 * <p>
 * Erstellungsdatum 21.10.05
 * </p>
 * 
 * @todo MB. das ist eigentlich das gleiche wie TPBestellvorschlag -> eine
 *       Klasse daraus machen
 * 
 *       <p>
 *       </p>
 * @author Uli Walch
 * @version $Revision: 1.14 $
 */
public class TabbedPaneAnfragevorschlag extends TabbedPane {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private PanelSplit panelAnfragevorschlagSP1 = null; // FLR 1:n Liste
	private PanelQuery panelAnfragevorschlagQP1 = null;
	private PanelPositionenBestellvorschlag panelAnfragevorschlagD1 = null;

	protected final static int IDX_PANEL_ANFRAGEVORSCHLAG = 0;

	private PanelDialogKriterienBestellvorschlag2 pdKriterienAnfragevorschlag = null;
	private PanelDialogKriterienBestellvorschlagUeberleitung2 pdKriterienBestellvorschlagUeberleitung = null;
	private PanelDialogKriterienBestellvorschlagverdichtung pdKritBestellvorschlagverdichtung = null;
	private PanelDialogKriterienBestellvorschlagAnhandAngebot pdKritBestellvorschlagAnhandAngebot = null;

	private static final String ACTION_SPECIAL_NEUER_BESTELLVORSCHLAG = PanelBasis.ALWAYSENABLED
			+ "action_special_neuer_bestellvorschlag";
	private static final String ACTION_SPECIAL_ANFRAGEN_AUS_BESTELLVORSCHLAG = PanelBasis.ALWAYSENABLED
			+ "action_special_anfragen_aus_bestellvorschlag";
	private static final String ACTION_SPECIAL_ANFRAGEN_IN_LIEFERGRUPPENANFRAGEN = PanelBasis.ALWAYSENABLED
			+ "action_special_anfragen_in_liefergruppenafragen_ueberleiten";
	private static final String ACTION_SPECIAL_VERDICHTEN = PanelBasis.ALWAYSENABLED
			+ "action_special_verdichten";
	private static final String ACTION_SPECIAL_IMPORT = PanelBasis.ALWAYSENABLED
			+ "action_special_import";
	private static final String ACTION_SPECIAL_NEUER_BESTELLVORSCHLAG_ANHAND_ANGEBOT = PanelBasis.ALWAYSENABLED
			+ "action_special_neuer_bestellvorschlag_anhand_angebot";

	private static final String MENU_ACTION_JOURNAL_BESTELLVORSCHLAG = "MENU_ACTION_JOURNAL_BESTELLVORSCHLAG";

	public TabbedPaneAnfragevorschlag(InternalFrame internalFrameI)
			throws Throwable {
		super(internalFrameI, LPMain
				.getTextRespectUISPr("anf.anfragevorschlag"));
		jbInit();
		initComponents();
	}

	private void jbInit() throws Throwable {
		insertTab(LPMain.getTextRespectUISPr("anf.anfragevorschlag"), null,
				null, LPMain.getTextRespectUISPr("anf.anfragevorschlag"),
				IDX_PANEL_ANFRAGEVORSCHLAG);

		refreshPanelAnfragevorschlag();

		addChangeListener(this);

		getInternalFrame().addItemChangedListener(this);
	}

	private PanelSplit refreshPanelAnfragevorschlag() throws Throwable {
		if (panelAnfragevorschlagSP1 == null) {
			panelAnfragevorschlagD1 = new PanelPositionenBestellvorschlag(
					getInternalFrame(),
					LPMain.getTextRespectUISPr("anf.anfragevorschlag"), null, // eventuell
																				// gibt
																				// es
																				// noch
																				// keine
																				// position
					100);
			FilterKriterium[] fkPositionen = SystemFilterFactory.getInstance()
					.createFKMandantCNr();

			String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW };

			panelAnfragevorschlagQP1 = new PanelQuery(null, fkPositionen,
					QueryParameters.UC_ID_BESTELLVORSCHLAG, aWhichButtonIUse,
					getInternalFrame(),
					LPMain.getTextRespectUISPr("anf.anfragevorschlag"), true); // flag,
																				// damit
																				// flr
																				// erst
																				// beim
																				// aufruf
																				// des
																				// panels
																				// loslaeuft

			panelAnfragevorschlagQP1.createAndSaveAndShowButton(
					"/com/lp/client/res/clipboard.png",
					LPMain.getTextRespectUISPr("anf.tooltip.anfragevorschlag"),
					ACTION_SPECIAL_NEUER_BESTELLVORSCHLAG,
					RechteFac.RECHT_ANF_ANFRAGE_CUD);

			panelAnfragevorschlagQP1
					.createAndSaveAndShowButton(
							"/com/lp/client/res/clipboard_next_down.png",
							LPMain.getTextRespectUISPr("bes.tooltip.bestellvorschlag.anhandangebot"),
							ACTION_SPECIAL_NEUER_BESTELLVORSCHLAG_ANHAND_ANGEBOT,
							RechteFac.RECHT_BES_BESTELLUNG_CUD);

			panelAnfragevorschlagQP1
					.createAndSaveAndShowButton(
							"/com/lp/client/res/clipboard_next.png",
							LPMain.getTextRespectUISPr("anf.tooltip.anfragenausbestellvorschlag"),
							ACTION_SPECIAL_ANFRAGEN_AUS_BESTELLVORSCHLAG,
							RechteFac.RECHT_ANF_ANFRAGE_CUD);
			panelAnfragevorschlagQP1
					.createAndSaveAndShowButton(
							"/com/lp/client/res/index_new.png",
							LPMain.getTextRespectUISPr("anf.tooltip.liefergruppenanfragenausbestellvorschlag"),
							ACTION_SPECIAL_ANFRAGEN_IN_LIEFERGRUPPENANFRAGEN,
							RechteFac.RECHT_ANF_ANFRAGE_CUD);

			panelAnfragevorschlagQP1
					.createAndSaveAndShowButton(
							"/com/lp/client/res/branch.png",
							LPMain.getTextRespectUISPr("anf.anfragevorschlagverdichten"),
							ACTION_SPECIAL_VERDICHTEN,
							RechteFac.RECHT_BES_BESTELLUNG_CUD);

			panelAnfragevorschlagSP1 = new PanelSplit(getInternalFrame(),
					panelAnfragevorschlagD1, panelAnfragevorschlagQP1, 200);

			setComponentAt(IDX_PANEL_ANFRAGEVORSCHLAG, panelAnfragevorschlagSP1);
		}

		return panelAnfragevorschlagSP1;
	}

	public void lPEventObjectChanged(ChangeEvent e) throws Throwable {
		super.lPEventObjectChanged(e);

		int selectedIndex = getSelectedIndex();

		switch (selectedIndex) {
		case IDX_PANEL_ANFRAGEVORSCHLAG:
			refreshPanelAnfragevorschlag();
			panelAnfragevorschlagSP1.eventYouAreSelected(false);

			// wenn es fuer das tabbed pane noch keinen Eintrag gibt,
			// die restlichen oberen Laschen deaktivieren, ausser ...
			if (panelAnfragevorschlagQP1.getSelectedId() == null) {
				getInternalFrame().enableAllOberePanelsExceptMe(this,
						IDX_PANEL_ANFRAGEVORSCHLAG, false);
			}

			panelAnfragevorschlagQP1.updateButtons(panelAnfragevorschlagD1
					.getLockedstateDetailMainKey());
			break;
		}
	}

	public void lPEventItemChanged(ItemChangedEvent e) throws Throwable {
		if (pdKriterienAnfragevorschlag != null) {
			pdKriterienAnfragevorschlag.eventItemchanged(e);
		}

		if (pdKritBestellvorschlagAnhandAngebot != null) {
			pdKritBestellvorschlagAnhandAngebot.eventItemchanged(e);
		}

		if (e.getID() == ItemChangedEvent.ITEM_CHANGED) {
			if (e.getSource() == panelAnfragevorschlagQP1) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				panelAnfragevorschlagD1.setKeyWhenDetailPanel(key);
				panelAnfragevorschlagD1.eventYouAreSelected(false);
				panelAnfragevorschlagQP1.updateButtons(panelAnfragevorschlagD1
						.getLockedstateDetailMainKey());
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_UPDATE) {
			// hier kommt man nach update im D bei einem 1:n hin.
			if (e.getSource() == panelAnfragevorschlagD1) {
				// im QP die Buttons in den Zustand neu setzen.
				panelAnfragevorschlagQP1.updateButtons(new LockStateValue(
						PanelBasis.LOCK_FOR_NEW));
			}
		} else if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			// Zeile doppelklicken
		} else if (e.getID() == ItemChangedEvent.ACTION_NEW) {
			if (e.getSource() == panelAnfragevorschlagQP1) {
				panelAnfragevorschlagD1.eventActionNew(e, true, false);
				panelAnfragevorschlagD1.eventYouAreSelected(false);
				setSelectedComponent(panelAnfragevorschlagSP1); // ui
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_DISCARD) {
			if (e.getSource() == panelAnfragevorschlagD1) {
				panelAnfragevorschlagSP1.eventYouAreSelected(false); // das 1:n
																		// Panel
																		// neu
																		// aufbauen
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_SAVE) {
			if (e.getSource() == panelAnfragevorschlagD1) {
				Object oKey = panelAnfragevorschlagD1.getKeyWhenDetailPanel();
				panelAnfragevorschlagQP1.eventYouAreSelected(false);
				panelAnfragevorschlagQP1.setSelectedId(oKey);
				panelAnfragevorschlagSP1.eventYouAreSelected(false);
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_GOTO_MY_DEFAULT_QP) {
			// wir landen hier nach der Abfolge Button Neu -> xx -> Button
			// Discard
			if (e.getSource() == panelAnfragevorschlagD1) {
				// bei einem Neu im 1:n Panel wurde der KeyForLockMe
				// ueberschrieben
				setKeyWasForLockMe();

				panelAnfragevorschlagSP1.eventYouAreSelected(false); // refresh
																		// auf
																		// das
																		// gesamte
																		// 1:n
																		// panel
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_KRITERIEN_HAVE_BEEN_SELECTED) {
			if (e.getSource() == pdKriterienAnfragevorschlag) {
				try {
					// einen neuen Bestellvorschlag erstellen
					if (pdKriterienAnfragevorschlag
							.getVorhandeneBestellvorschlagEintrageLoeschen()) {
						DelegateFactory
								.getInstance()
								.getBestellvorschlagDelegate()
								.erstelleBestellvorschlag(
										pdKriterienAnfragevorschlag
												.getAuftragsvorlaufzeit(),
										pdKriterienAnfragevorschlag
												.getToleranz(),
										pdKriterienAnfragevorschlag
												.getLieferterminFuerArtikelOhneReservierung(),
										pdKriterienAnfragevorschlag.getLosIId(),
										pdKriterienAnfragevorschlag
												.getAuftragIId(),
										pdKriterienAnfragevorschlag
												.isbMitNichtlagerbeweirtschaftetenArtikeln(),
										pdKriterienAnfragevorschlag
												.isbNurBetroffeneLospositionen());
					}
					panelAnfragevorschlagSP1.eventYouAreSelected(false);
				} catch (Throwable t) {
					ExceptionLP efc = (ExceptionLP) t;

					if (efc != null
							&& efc.getICode() == EJBExceptionLP.FEHLER_BESTELLVORSCHLAG_IST_GESPERRT) {
						DialogFactory.showModalDialog(
								LPMain.getTextRespectUISPr("lp.warning"),
								LPMain.getInstance().getMsg(efc));
					} else {
						throw new ExceptionLP(EJBExceptionLP.FEHLER,
								t.toString(), null);
					}
				}
			} else if (e.getSource() == pdKriterienBestellvorschlagUeberleitung) {
				// Anfrage aus dem Bestellvorschlag erzeugen
				DelegateFactory
						.getInstance()
						.getAnfrageDelegate()
						.erzeugeAnfragenAusBestellvorschlag(
								pdKriterienBestellvorschlagUeberleitung
										.getBestellvorschlagUeberleitungKriterienDto());
				panelAnfragevorschlagSP1.eventYouAreSelected(false);
			} else if (e.getSource() == pdKritBestellvorschlagAnhandAngebot) {

				// PJ18230
				DelegateFactory
						.getInstance()
						.getBestellvorschlagDelegate()
						.erstelleBestellvorschlagAnhandEinesAngebots(
								pdKritBestellvorschlagAnhandAngebot.angebotIId,
								pdKritBestellvorschlagAnhandAngebot
										.getLiefertermin());
				panelAnfragevorschlagSP1.eventYouAreSelected(false);
			} else if (e.getSource() == pdKritBestellvorschlagverdichtung) {
				// Bestellvorschlag verdichten
				DelegateFactory
						.getInstance()
						.getBestellvorschlagDelegate()
						.verdichteBestellvorschlag(
								pdKritBestellvorschlagverdichtung
										.getVerdichtungszeitraum(),
								pdKritBestellvorschlagverdichtung
										.getMindestbestellmengeBeruecksichtigen(),
								pdKritBestellvorschlagverdichtung
										.getProjektklammerBeruecksichtigen());
				panelAnfragevorschlagSP1.eventYouAreSelected(false);
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_SPECIAL_BUTTON) {
			String sAspectInfo = ((ISourceEvent) e.getSource()).getAspect();

			if (sAspectInfo.equals(ACTION_SPECIAL_NEUER_BESTELLVORSCHLAG)) {
				refreshPdKriterienAnfragevorschlag();
				getInternalFrame().showPanelDialog(pdKriterienAnfragevorschlag);
			} else if (sAspectInfo
					.equals(ACTION_SPECIAL_ANFRAGEN_AUS_BESTELLVORSCHLAG)) {
				refreshPdKriterienBestellvorschlagUeberleitung();
				getInternalFrame().showPanelDialog(
						pdKriterienBestellvorschlagUeberleitung);

			} else if (sAspectInfo
					.equals(ACTION_SPECIAL_ANFRAGEN_IN_LIEFERGRUPPENANFRAGEN)) {
				String projekt = (String) JOptionPane
						.showInputDialog(
								this,
								LPMain.getTextRespectUISPr("anf.anfragevorschlagueberleiten.projekt"),
								LPMain.getTextRespectUISPr("lp.frage"),
								JOptionPane.QUESTION_MESSAGE);
				DelegateFactory
						.getInstance()
						.getBestellvorschlagDelegate()
						.bestellvorschlagInLiefergruppenanfragenUmwandeln(
								projekt);
				panelAnfragevorschlagSP1.eventYouAreSelected(false);
			} else if (sAspectInfo.equals(ACTION_SPECIAL_VERDICHTEN)) {
				// DelegateFactory.getInstance().getBestellvorschlagDelegate().
				// verdichteBestellvorschlag();
				// panelBestellungVorschlagSP1.eventYouAreSelected(false);
				refreshPdKriterienBestellvorschlagverdichtung();
				getInternalFrame().showPanelDialog(
						pdKritBestellvorschlagverdichtung);
			} else if (sAspectInfo
					.equals(ACTION_SPECIAL_NEUER_BESTELLVORSCHLAG_ANHAND_ANGEBOT)) {
				refreshPdKriterienBestellvorschlagAnhandAngebot();
				getInternalFrame().showPanelDialog(
						pdKritBestellvorschlagAnhandAngebot);
			}
		}
	}

	private PanelDialogKriterien refreshPdKriterienBestellvorschlagAnhandAngebot()
			throws Throwable {
		pdKritBestellvorschlagAnhandAngebot = new PanelDialogKriterienBestellvorschlagAnhandAngebot(
				getInternalFrame(),
				LPMain.getTextRespectUISPr("bes.tooltip.bestellvorschlag.anhandangebot"));
		return pdKritBestellvorschlagAnhandAngebot;
	}

	private PanelDialogKriterien refreshPdKriterienBestellvorschlagverdichtung()
			throws Throwable {
		pdKritBestellvorschlagverdichtung = new PanelDialogKriterienBestellvorschlagverdichtung(
				getInternalFrame(),
				LPMain.getTextRespectUISPr("bes.title.panelbestellvorschlagverdichten"));
		return pdKritBestellvorschlagverdichtung;
	}

	/**
	 * Den Text der Titelleiste ueberschreiben.
	 * 
	 * @param panelTitle
	 *            der Title des aktuellen panel
	 * @throws Throwable
	 */
	public void setTitleAnfrage(String panelTitle) throws Throwable {
		// aktuellen Artikel bestimmen
		StringBuffer artikelBezeichnung = new StringBuffer("");

		if (panelAnfragevorschlagD1.getBestellvorschlagDto() != null
				&& panelAnfragevorschlagD1.getBestellvorschlagDto().getIId() != null) {
			ArtikelDto artikelDto = DelegateFactory
					.getInstance()
					.getArtikelDelegate()
					.artikelFindByPrimaryKey(
							panelAnfragevorschlagD1.getBestellvorschlagDto()
									.getIArtikelId());

			artikelBezeichnung.append(artikelDto.formatArtikelbezeichnung());
		}

		// dem Panel die Titel setzen
		getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_OHRWASCHLOBEN,
				panelTitle);
		getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_AS_I_LIKE,
				artikelBezeichnung.toString());
	}

	public void setKeyWasForLockMe() throws Throwable {
		// es koennte die letzte Zeile geloescht worden sein
		panelAnfragevorschlagQP1.eventYouAreSelected(false);

		Object oKey = panelAnfragevorschlagQP1.getSelectedId();

		if (oKey != null) {
			getInternalFrame().setKeyWasForLockMe(oKey.toString());
		} else {
			getInternalFrame().setKeyWasForLockMe(null);
		}
	}

	

	protected void lPActionEvent(java.awt.event.ActionEvent e) throws Throwable {

		if (e.getActionCommand().equals(MENU_ACTION_JOURNAL_BESTELLVORSCHLAG)) {
			getInternalFrame().showReportKriterien(
					new ReportBestellVorschlag(getInternalFrame(), LPMain
							.getTextRespectUISPr("anf.anfragevorschlag")));
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_IMPORT)) {

			// Dateiauswahldialog
			File[] files = HelperClient.chooseFile(this,
					HelperClient.FILE_FILTER_XLS, false);
			File f = null;
			if (files != null && files.length > 0) {
				f = files[0];
			}
			if (f != null) {
				File[] fileArray = new File(f.getParent()).listFiles();
				Workbook workbook = Workbook.getWorkbook(f);
				Sheet sheet = workbook.getSheet(0);

				ArrayList<StrukturierterImportDto> gesamtliste = new SheetImportController()
						.importSheet(sheet, fileArray);

				for (int i = 1; i < gesamtliste.size(); i++) {
					StrukturierterImportDto dto = gesamtliste.get(i);

					if (dto.getLiefergruppe() != null
							&& dto.getArtikelnr() != null
							&& dto.getArtikelnr().length() > 0) {

						try {
							Integer liefergruppe = new Integer(
									dto.getLiefergruppe());
							if (liefergruppe <= 400) {
								gesamtliste.set(i, dto);
							}

						} catch (NumberFormatException e1) {
							gesamtliste.set(i, dto);
						}

					}

				}

				java.sql.Date datum = DialogFactory.showDatumseingabe(LPMain
						.getTextRespectUISPr("label.liefertermin"));
				if (datum != null) {

					ArrayList alAngelegteArtikel = DelegateFactory
							.getInstance()
							.getStuecklisteDelegate()
							.importiereStuecklistenstruktur(gesamtliste, true,
									new java.sql.Timestamp(datum.getTime()));

					if (alAngelegteArtikel.size() > 0) {
						StringBuffer sb = new StringBuffer();
						for (int i = 0; i < alAngelegteArtikel.size(); i++) {
							sb.append("\n");
							sb.append(alAngelegteArtikel.get(i));
						}

						DialogFactory
								.showMessageMitScrollbar(
										LPMain.getTextRespectUISPr("stkl.import.typ1.artikel.neuangelegt"),
										sb.toString());

					}

					panelAnfragevorschlagSP1.eventYouAreSelected(false);
				}

				workbook.close();

			}
		}
	}

	protected javax.swing.JMenuBar getJMenuBar() throws Throwable {
		WrapperMenuBar wrapperMenuBar = new WrapperMenuBar(this);
		JMenu jmModul = (JMenu) wrapperMenuBar
				.getComponent(WrapperMenuBar.MENU_MODUL);

		JMenuItem menuItemImport = new JMenuItem(
				LPMain.getTextRespectUISPr("lp.import"));
		menuItemImport.addActionListener(this);

		menuItemImport.setActionCommand(ACTION_SPECIAL_IMPORT);
		jmModul.add(menuItemImport, 0);

		JMenu journal = (JMenu) wrapperMenuBar
				.getComponent(WrapperMenuBar.MENU_JOURNAL);
		JMenuItem menuItemBestellVorschlag = new JMenuItem(
				LPMain.getTextRespectUISPr("bes.menu.bestellvorschlag"));
		menuItemBestellVorschlag.addActionListener(this);
		menuItemBestellVorschlag
				.setActionCommand(MENU_ACTION_JOURNAL_BESTELLVORSCHLAG);
		journal.add(menuItemBestellVorschlag);

		return wrapperMenuBar;

	}

	private void refreshPdKriterienAnfragevorschlag() throws Throwable {
		pdKriterienAnfragevorschlag = new PanelDialogKriterienBestellvorschlag2(
				getInternalFrame(),
				LPMain.getTextRespectUISPr("anf.kriterien.anfragevorschlag"));
	}

	private void refreshPdKriterienBestellvorschlagUeberleitung()
			throws Throwable {
		pdKriterienBestellvorschlagUeberleitung = new PanelDialogKriterienBestellvorschlagUeberleitung2(
				true,
				getInternalFrame(),
				LPMain.getTextRespectUISPr("anf.kriterien.bestellvorschlagueberleitung"));
	}
}
