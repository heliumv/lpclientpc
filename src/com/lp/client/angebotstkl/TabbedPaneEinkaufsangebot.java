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
package com.lp.client.angebotstkl;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import javax.swing.event.ChangeEvent;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.LockStateValue;
import com.lp.client.frame.assistent.view.AssistentView;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQuery;
import com.lp.client.frame.component.PanelSplit;
import com.lp.client.frame.component.TabbedPane;
import com.lp.client.frame.component.WrapperMenuBar;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.stueckliste.importassistent.StklImportController;
import com.lp.client.system.SystemFilterFactory;
import com.lp.client.util.fastlanereader.gui.QueryType;
import com.lp.server.angebotstkl.service.AgstklpositionDto;
import com.lp.server.angebotstkl.service.EinkaufsangebotpositionDto;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.LagerDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MandantFac;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.service.BelegpositionDto;
import com.lp.service.POSDocument2POSDto;
import com.lp.service.StklImportSpezifikation;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;
import com.lp.util.csv.LPCSVReader;

@SuppressWarnings("static-access")
public class TabbedPaneEinkaufsangebot extends TabbedPane {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private PanelQuery panelQueryEinkaufsangebot = null;
	private PanelBasis panelDetailEinkaufsangebot = null;

	private PanelQuery panelQueryEinkaufsangebotpositionen = null;
	private PanelEinkaufsangebotpositionen panelDetailEinkaufsangebotpositionen = null;
	private PanelSplit panelSplitEinkaufsangebotpositionen = null; // FLR 1:n

	private PanelBasis panelDetailEinkaufsangebotKommentar = null;

	private static final String ACTION_SPECIAL_CSVIMPORT_POSITIONEN = "ACTION_SPECIAL_CSVIMPORT_POSITIONEN";

	private final String BUTTON_IMPORTCSV_EKAGSTKLPOSITIONEN = PanelBasis.ACTION_MY_OWN_NEW
			+ ACTION_SPECIAL_CSVIMPORT_POSITIONEN;

	public static int IDX_PANEL_AUSWAHL = 0;
	public static int IDX_PANEL_KOPFDATEN = 1;
	public static int IDX_PANEL_POSITIONEN = 2;
	public static int IDX_PANEL_KOMMENTAR = 3;

	private final String MENU_DATEI_DRUCKEN = "MENU_DATEI_DRUCKEN";

	private WrapperMenuBar wrapperMenuBar = null;

	public TabbedPaneEinkaufsangebot(InternalFrame internalFrameI)
			throws Throwable {

		super(internalFrameI, LPMain.getInstance().getTextRespectUISPr(
				"lp.einkaufsangebot"));

		jbInit();
		initComponents();
	}

	public PanelQuery getPanelQueryEinkaufsangebot() {
		return panelQueryEinkaufsangebot;
	}

	public PanelQuery getPanelQueryEinkaufsangebotpositionen() {
		return panelQueryEinkaufsangebotpositionen;
	}

	public PanelBasis getAngebotstklPositionenBottom() {
		return panelDetailEinkaufsangebotpositionen;
	}

	public PanelQuery getAngebotstklPositionenTop() {
		return panelQueryEinkaufsangebotpositionen;
	}

	private void refreshAuswahl() throws Throwable {
		if (panelQueryEinkaufsangebot == null) {

			String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW,
					PanelBasis.ACTION_FILTER };
			panelQueryEinkaufsangebot = new PanelQuery(AngebotstklFilterFactory
					.getInstance().createQTAgstkl(), SystemFilterFactory
					.getInstance().createFKMandantCNr(),
					QueryParameters.UC_ID_EINKAUFSANGEBOT, aWhichButtonIUse,
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("auft.title.panel.auswahl"),
					true);

			panelQueryEinkaufsangebot
					.befuellePanelFilterkriterienDirektUndVersteckte(
							AngebotstklFilterFactory.getInstance()
									.createFKDEinakufsangebotbelegnumer(),
							AngebotstklFilterFactory.getInstance()
									.createFKDKundeName(), null);

			setComponentAt(IDX_PANEL_AUSWAHL, panelQueryEinkaufsangebot);
		}
	}

	private void createKopfdaten(Integer key) throws Throwable {
		if (panelDetailEinkaufsangebot == null) {
			panelDetailEinkaufsangebot = new PanelEinkaufsangebot(
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("lp.kopfdaten"), key);
			setComponentAt(IDX_PANEL_KOPFDATEN, panelDetailEinkaufsangebot);
		}
	}

	private void createKommentar(Integer key) throws Throwable {
		if (panelDetailEinkaufsangebotKommentar == null) {
			panelDetailEinkaufsangebotKommentar = new PanelEinkaufsangebotpositionenKommentare(
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("lp.kommentar"), key);
			setComponentAt(IDX_PANEL_KOMMENTAR,
					panelDetailEinkaufsangebotKommentar);
		}
	}

	private PanelBasis getEinkaufsangebotKopfdaten() throws Throwable {
		Integer iIdAngebot = null;

		if (panelDetailEinkaufsangebot == null) {

			// Die Angebot hat einen Key vom Typ Integer
			if (getInternalFrame().getKeyWasForLockMe() != null) {
				iIdAngebot = new Integer(Integer.parseInt(getInternalFrame()
						.getKeyWasForLockMe()));
			}

			panelDetailEinkaufsangebot = new PanelEinkaufsangebot(
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("angb.panel.kopfdaten"),
					iIdAngebot); // empty bei leerer angebotsliste

			setComponentAt(IDX_PANEL_KOPFDATEN, panelDetailEinkaufsangebot);
		}

		return panelDetailEinkaufsangebot;
	}

	public void printEinkaufsangebot() throws Throwable {
		if (!getEinkaufsangebotKopfdaten().isLockedDlg()) {
			if (getInternalFrameAngebotstkl().getEinkaufsangebotDto() != null) {
				// das Angebot drucken
				getInternalFrame().showReportKriterien(
						new ReportEinkaufsangebot(
								getInternalFrameAngebotstkl(),
								getInternalFrameAngebotstkl()
										.getEinkaufsangebotDto().getCNr(),
								getInternalFrameAngebotstkl()
										.getEinkaufsangebotDto().getIId()));
			} else {
				// es ist keine Angbotsstueckliste ausgewaehlt
				DialogFactory.showModalDialog(LPMain.getInstance()
						.getTextRespectUISPr("lp.error"), LPMain.getInstance()
						.getTextRespectUISPr("as.warning.keineinkaufsangebot"));
			}
		}
	}

	/**
	 * jbInit
	 * 
	 * @throws Throwable
	 */
	private void jbInit() throws Throwable {
		insertTab(LPMain.getInstance().getTextRespectUISPr("lp.auswahl"), null,
				null, LPMain.getInstance().getTextRespectUISPr("lp.auswahl"),
				IDX_PANEL_AUSWAHL);

		insertTab(LPMain.getInstance().getTextRespectUISPr("lp.kopfdaten"),
				null, null,
				LPMain.getInstance().getTextRespectUISPr("lp.kopfdaten"),
				IDX_PANEL_KOPFDATEN);

		insertTab(LPMain.getInstance().getTextRespectUISPr("lp.positionen"),
				null, null,
				LPMain.getInstance().getTextRespectUISPr("lp.positionen"),
				IDX_PANEL_POSITIONEN);
		insertTab(LPMain.getInstance().getTextRespectUISPr("lp.kommentar"),
				null, null,
				LPMain.getInstance().getTextRespectUISPr("lp.kommentar"),
				IDX_PANEL_KOMMENTAR);

		refreshAuswahl();

		panelQueryEinkaufsangebot.eventYouAreSelected(false);

		if ((Integer) panelQueryEinkaufsangebot.getSelectedId() != null) {
			getInternalFrameAngebotstkl().setEinkaufsangebotDto(
					DelegateFactory
							.getInstance()
							.getAngebotstklDelegate()
							.einkaufsangebotFindByPrimaryKey(
									(Integer) panelQueryEinkaufsangebot
											.getSelectedId()));
		}

		// damit D2 einen aktuellen hat.
		ItemChangedEvent it = new ItemChangedEvent(panelQueryEinkaufsangebot,
				ItemChangedEvent.ITEM_CHANGED);
		lPEventItemChanged(it);

		this.addChangeListener(this);
		this.getInternalFrame().addItemChangedListener(this);

	}

	public void setKeyWasForLockMe() {
		Object oKey = panelQueryEinkaufsangebot.getSelectedId();

		if (oKey != null) {
			getInternalFrame().setKeyWasForLockMe(oKey.toString());
		} else {
			getInternalFrame().setKeyWasForLockMe(null);
		}
	}

	private void refreshTitle() {

		getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_OHRWASCHLUNTEN,
				LPMain.getInstance().getTextRespectUISPr("lp.einkaufsangebot"));
		getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_OHRWASCHLOBEN,
				((PanelBasis) this.getSelectedComponent()).getAdd2Title());

		if (getInternalFrameAngebotstkl().getEinkaufsangebotDto() != null) {
			String sBezeichnung = "";
			if (getInternalFrameAngebotstkl().getEinkaufsangebotDto()
					.getCProjekt() != null) {
				sBezeichnung = getInternalFrameAngebotstkl()
						.getEinkaufsangebotDto().getCProjekt();
			}
			getInternalFrame().setLpTitle(
					InternalFrame.TITLE_IDX_AS_I_LIKE,
					getInternalFrameAngebotstkl().getEinkaufsangebotDto()
							.getCNr() + ", " + sBezeichnung);
		}
	}

	public InternalFrameAngebotstkl getInternalFrameAngebotstkl() {
		return (InternalFrameAngebotstkl) getInternalFrame();
	}

	protected void lPActionEvent(ActionEvent e) throws Throwable {
		if (e.getActionCommand().equals(MENU_DATEI_DRUCKEN)) {
			printEinkaufsangebot();
		}

	}

	public void lPEventItemChanged(ItemChangedEvent eI) throws Throwable {

		if (eI.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			if (eI.getSource() == panelQueryEinkaufsangebot) {
				refreshAngebotstklPositionen();
				setSelectedComponent(panelSplitEinkaufsangebotpositionen);
				// panelDetailAgstkl.eventYouAreSelected(false);
			}

		} else if (eI.getID() == ItemChangedEvent.ACTION_MY_OWN_NEW) {
			String sAspectInfo = ((ISourceEvent) eI.getSource()).getAspect();
			if (sAspectInfo.equals(PanelBasis.ACTION_EINFUEGEN_LIKE_NEW)) {
				if (eI.getSource().equals(panelQueryEinkaufsangebotpositionen)) {
					einfuegenHV();
				}
			}

			else if (sAspectInfo.equals(PanelBasis.ACTION_KOPIEREN)) {
				if (eI.getSource().equals(panelQueryEinkaufsangebotpositionen)) {
					copyHV();
				}
			}
			else if (sAspectInfo.equals(BUTTON_IMPORTCSV_EKAGSTKLPOSITIONEN)) {
				if(LPMain.getInstance().getDesktop()
					.darfAnwenderAufZusatzfunktionZugreifen(
							MandantFac.ZUSATZFUNKTION_INTELLIGENTER_STUECKLISTENIMPORT)) {
					if(LPMain.getInstance().getDesktop()
							.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_KUNDESONDERKONDITIONEN)) {
						AssistentView av = new AssistentView(
						getInternalFrameAngebotstkl(),
						LPMain.getTextRespectUISPr("stkl.intelligenterstklimport"),
						new StklImportController(
								getInternalFrameAngebotstkl().getEinkaufsangebotDto().getIId(),
								StklImportSpezifikation.SpezifikationsTyp.EINKAUFSANGEBOTSSTKL_SPEZ,
								getInternalFrame()));
						getInternalFrame().showPanelDialog(av);
					} else {
						DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.hint"), 
								LPMain.getTextRespectUISPr("stkl.error.keinekundesokoberechtigung"));
					}
				} else {
					// PJ 14984
					File[] files = HelperClient.chooseFile(this,
							HelperClient.FILE_FILTER_CSV, false);
					File file = null;
					if (files != null && files.length > 0) {
						file = files[0];
					}
					if (file != null) {
						// Tab-getrenntes File einlesen.
						LPCSVReader reader = new LPCSVReader(new FileReader(file),
								(char) KeyEvent.VK_SEMICOLON);
	
						if (sAspectInfo.equals(BUTTON_IMPORTCSV_EKAGSTKLPOSITIONEN)) {
							// CSV-Format fuer Arbeitsplan:
							// Spalte1: ARTIKELNUMMER
							// Spalte2: MENGE
							// PJ 13479: Damit man Handartikel importieren kann
	
							// Spalte3: BEZEICHNUNG-WENN HANDARTIKEL
							// Spalte4: EINHEIT-WENN HANDARTIKEL
							// Spalte5: C_POSITION
							// Spalte6: C_KOMMENTAR
							if (panelDetailEinkaufsangebotpositionen.defaultMontageartDto == null) {
								// DefaultMontageart==null
								return;
							}
	
							String[] sLine;
							int iZeile = 0;
							ArrayList<EinkaufsangebotpositionDto> list = new ArrayList<EinkaufsangebotpositionDto>();
							do {
								// zeilenweise einlesen.
								sLine = reader.readNext();
								iZeile++;
								if (sLine != null) {
									if (sLine.length < 6) {
										DialogFactory
												.showModalDialog(
														LPMain.getInstance()
																.getTextRespectUISPr(
																		"lp.error"),
														"CSV-Datei muss genau 6 Spalten beinhalten.");
										return;
									}
	
									EinkaufsangebotpositionDto dto = new EinkaufsangebotpositionDto();
									dto.setBelegIId((Integer) panelQueryEinkaufsangebot
											.getSelectedId());
									dto.setBMitdrucken(Helper.boolean2Short(false));
									dto.setBArtikelbezeichnunguebersteuert(Helper
											.boolean2Short(false));
									ArtikelDto artikelDto = null;
									try {
										artikelDto = DelegateFactory.getInstance()
												.getArtikelDelegate()
												.artikelFindByCNr(sLine[0]);
										dto.setArtikelIId(artikelDto.getIId());
										dto.setEinheitCNr(artikelDto
												.getEinheitCNr());
										dto.setPositionsartCNr(LocaleFac.POSITIONSART_IDENT);
										dto.setCPosition(sLine[4]);
										dto.setCBemerkung(sLine[5]);
										try {
											dto.setNMenge(new BigDecimal(sLine[1]));
											list.add(dto);
										} catch (NumberFormatException ex) {
											DialogFactory
													.showModalDialog(
															LPMain.getInstance()
																	.getTextRespectUISPr(
																			"lp.error"),
															"Keine g\u00FCltige Zahl in Zeile "
																	+ iZeile
																	+ ", Spalte 2. Zeile wird \u00FCbersprungen.");
										}
									} catch (ExceptionLP ex1) {
										if (ex1.getICode() == EJBExceptionLP.FEHLER_BEI_FIND) {
	
											if (sLine[2] != null
													&& sLine[2].length() > 0) {
												// Handartikel anlegen
												dto.setPositionsartCNr(LocaleFac.POSITIONSART_HANDEINGABE);
												dto.setCBez(sLine[2]);
												try {
													dto.setNMenge(new BigDecimal(
															sLine[1]));
													dto.setCPosition(sLine[4]);
													dto.setCBemerkung(sLine[5]);
	
													try {
														DelegateFactory
																.getInstance()
																.getSystemDelegate()
																.einheitFindByPrimaryKey(
																		sLine[3]);
														dto.setEinheitCNr(sLine[3]);
														list.add(dto);
													} catch (ExceptionLP e) {
														if (ex1.getICode() == EJBExceptionLP.FEHLER_BEI_FIND) {
															DialogFactory
																	.showModalDialog(
																			LPMain.getInstance()
																					.getTextRespectUISPr(
																							"lp.error"),
																			"Keine g\u00FCltige Einheit "
																					+ sLine[3]
																					+ " in Zeile "
																					+ iZeile
																					+ ", Spalte 4. Zeile wird \u00FCbersprungen.");
														} else {
															handleException(e, true);
														}
													}
	
												} catch (NumberFormatException ex) {
													DialogFactory
															.showModalDialog(
																	LPMain.getInstance()
																			.getTextRespectUISPr(
																					"lp.error"),
																	"Keine g\u00FCltige Zahl in Zeile "
																			+ iZeile
																			+ ", Spalte 2. Zeile wird \u00FCbersprungen.");
												}
											}
	
										} else {
											handleException(ex1, true);
										}
									}
								}
							} while (sLine != null);
	
							if (list.size() > 0) {
								EinkaufsangebotpositionDto[] returnArray = new EinkaufsangebotpositionDto[list
										.size()];
								returnArray = (EinkaufsangebotpositionDto[]) list
										.toArray(returnArray);
								DelegateFactory
										.getInstance()
										.getAngebotstklDelegate()
										.createEinkaufsangebotpositions(returnArray);
								panelSplitEinkaufsangebotpositionen
										.eventYouAreSelected(false);
							}
						}
					} else {
						// keine auswahl
					}
				} 
			}
		}

		else if (eI.getID() == ItemChangedEvent.ACTION_SAVE) {
			if (eI.getSource() == panelDetailEinkaufsangebot) {
				panelQueryEinkaufsangebot.clearDirektFilter();
				Object oKey = panelDetailEinkaufsangebot
						.getKeyWhenDetailPanel();

				panelQueryEinkaufsangebot.setSelectedId(oKey);
			} else if (eI.getSource() == panelDetailEinkaufsangebotpositionen) {
				// den Key des Datensatzes merken
				Object oKey = panelDetailEinkaufsangebotpositionen
						.getKeyWhenDetailPanel();

				// wenn der neue Datensatz wirklich angelegt wurde (Abbruch
				// moeglich durch Pruefung auf Unterpreisigkeit)
				if (!oKey.equals(LPMain.getLockMeForNew())) {
					// die Liste neu aufbauen
					panelQueryEinkaufsangebotpositionen
							.eventYouAreSelected(false);

					// den Datensatz in der Liste selektieren
					panelQueryEinkaufsangebotpositionen.setSelectedId(oKey);
				}

				// im Detail den selektierten anzeigen
				panelSplitEinkaufsangebotpositionen.eventYouAreSelected(false);
			}
		}
		if (eI.getID() == ItemChangedEvent.ACTION_POSITION_VONNNACHNMINUS1) {
			if (eI.getSource() == panelQueryEinkaufsangebotpositionen) {
				int iPos = panelQueryEinkaufsangebotpositionen.getTable()
						.getSelectedRow();

				// wenn die Position nicht die erste ist
				if (iPos > 0) {
					Integer iIdPosition = (Integer) panelQueryEinkaufsangebotpositionen
							.getSelectedId();

					Integer iIdPositionMinus1 = (Integer) panelQueryEinkaufsangebotpositionen
							.getTable().getValueAt(iPos - 1, 0);

					DelegateFactory
							.getInstance()
							.getAngebotstklDelegate()
							.vertauscheEinkausangebotpositionen(iIdPosition,
									iIdPositionMinus1);

					// die Liste neu anzeigen und den richtigen Datensatz
					// markieren
					panelQueryEinkaufsangebotpositionen
							.setSelectedId(iIdPosition);
				}
			}
		} else if (eI.getID() == ItemChangedEvent.ACTION_POSITION_VONNNACHNPLUS1) {
			if (eI.getSource() == panelQueryEinkaufsangebotpositionen) {
				int iPos = panelQueryEinkaufsangebotpositionen.getTable()
						.getSelectedRow();

				// wenn die Position nicht die letzte ist
				if (iPos < panelQueryEinkaufsangebotpositionen.getTable()
						.getRowCount() - 1) {
					Integer iIdPosition = (Integer) panelQueryEinkaufsangebotpositionen
							.getSelectedId();

					Integer iIdPositionPlus1 = (Integer) panelQueryEinkaufsangebotpositionen
							.getTable().getValueAt(iPos + 1, 0);

					DelegateFactory
							.getInstance()
							.getAngebotstklDelegate()
							.vertauscheEinkausangebotpositionen(iIdPosition,
									iIdPositionPlus1);

					// die Liste neu anzeigen und den richtigen Datensatz
					// markieren
					panelQueryEinkaufsangebotpositionen
							.setSelectedId(iIdPosition);
				}
			}
		} else if (eI.getID() == ItemChangedEvent.ACTION_POSITION_VORPOSITIONEINFUEGEN) {
			if (eI.getSource() == panelQueryEinkaufsangebotpositionen) {
				panelDetailEinkaufsangebotpositionen.eventActionNew(eI, true,
						false);
				panelDetailEinkaufsangebotpositionen.eventYouAreSelected(false); // Buttons
				// schalten
			}
		}

		else if (eI.getID() == ItemChangedEvent.ITEM_CHANGED) {
			if (eI.getSource() == panelQueryEinkaufsangebot) {

				if (panelQueryEinkaufsangebot.getSelectedId() != null) {
					getInternalFrameAngebotstkl().setKeyWasForLockMe(
							panelQueryEinkaufsangebot.getSelectedId() + "");
					getInternalFrameAngebotstkl().setEinkaufsangebotDto(
							DelegateFactory
									.getInstance()
									.getAngebotstklDelegate()
									.einkaufsangebotFindByPrimaryKey(
											(Integer) panelQueryEinkaufsangebot
													.getSelectedId()));

					if (getInternalFrameAngebotstkl().getEinkaufsangebotDto() != null) {

						String sBezeichnung = "";
						if (getInternalFrameAngebotstkl()
								.getEinkaufsangebotDto().getCProjekt() != null) {
							sBezeichnung = getInternalFrameAngebotstkl()
									.getEinkaufsangebotDto().getCProjekt();
						}

						getInternalFrame().setLpTitle(
								InternalFrame.TITLE_IDX_AS_I_LIKE,
								getInternalFrameAngebotstkl()
										.getEinkaufsangebotDto().getCNr()
										+ ", " + sBezeichnung);
					}
				} else {

					getInternalFrame().enableAllOberePanelsExceptMe(this,
							IDX_PANEL_AUSWAHL, (false));
					getInternalFrame().setLpTitle(
							InternalFrame.TITLE_IDX_AS_I_LIKE, "");

				}
			} else if (eI.getSource() == panelQueryEinkaufsangebotpositionen) {
				Object key = ((ISourceEvent) eI.getSource()).getIdSelected();
				panelDetailEinkaufsangebotpositionen.setKeyWhenDetailPanel(key);
				panelDetailEinkaufsangebotpositionen.eventYouAreSelected(false);

				// im QP die Buttons in den Zustand nolocking/save setzen.
				panelQueryEinkaufsangebotpositionen
						.updateButtons(panelDetailEinkaufsangebotpositionen
								.getLockedstateDetailMainKey());
			}
		} else if (eI.getID() == ItemChangedEvent.ACTION_UPDATE) {
			// hier kommt man nach upd im D bei einem 1:n hin.
			if (eI.getSource() == panelDetailEinkaufsangebotpositionen) {
				// im QP die Buttons in den Zustand neu setzen.
				panelQueryEinkaufsangebotpositionen
						.updateButtons(new LockStateValue(
								PanelBasis.LOCK_FOR_NEW));
				;
			}
		} else if (eI.getID() == ItemChangedEvent.ACTION_GOTO_MY_DEFAULT_QP) {
			// aktiviere ein QP ...
			if (eI.getSource() == panelDetailEinkaufsangebot) {

				panelQueryEinkaufsangebot.eventYouAreSelected(false);
				getInternalFrameAngebotstkl().getEinkaufsangebotDto().setIId(
						(Integer) panelQueryEinkaufsangebot.getSelectedId());
				panelDetailEinkaufsangebot
						.setKeyWhenDetailPanel(panelQueryEinkaufsangebot
								.getSelectedId());
				this.setSelectedComponent(panelQueryEinkaufsangebot);
				if (panelQueryEinkaufsangebot.getSelectedId() == null) {
					getInternalFrame().enableAllOberePanelsExceptMe(this,
							IDX_PANEL_AUSWAHL, false);
				}

			} else if (eI.getSource() == panelDetailEinkaufsangebotpositionen) {
				// bei einem Neu im 1:n Panel wurde der KeyForLockMe
				// ueberschrieben
				setKeyWasForLockMe();
				if (panelDetailEinkaufsangebotpositionen
						.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryEinkaufsangebotpositionen
							.getId2SelectAfterDelete();
					panelQueryEinkaufsangebotpositionen
							.setSelectedId(oNaechster);
				}
				panelSplitEinkaufsangebotpositionen.eventYouAreSelected(false); // refresh
				// auf
				// das
				// gesamte
				// 1
				// :
				// n
				// panel
			}
		} else if (eI.getID() == ItemChangedEvent.ACTION_YOU_ARE_SELECTED) {
			if (eI.getSource() instanceof AssistentView) {
				getAktuellesPanel().eventYouAreSelected(false);
			}
			refreshTitle();
			super.lPEventItemChanged(eI);
		} else if (eI.getID() == ItemChangedEvent.ACTION_DISCARD) {
			if (eI.getSource() == panelDetailEinkaufsangebotpositionen) {
				panelSplitEinkaufsangebotpositionen.eventYouAreSelected(false); // das
				// 1
				// :
				// n
				// Panel
				// neu
				// aufbauen
			}
		} else if (eI.getID() == ItemChangedEvent.ACTION_NEW) {
			if (eI.getSource() == panelQueryEinkaufsangebot) {
				createKopfdaten(null);
				// wenn es bisher keinen eintrag gibt, die restlichen
				// panels aktivieren
				if (panelQueryEinkaufsangebot.getSelectedId() == null) {
					getInternalFrame().enableAllOberePanelsExceptMe(this,
							IDX_PANEL_AUSWAHL, true);
				}

				panelDetailEinkaufsangebot.eventActionNew(eI, true, false);
				setSelectedComponent(panelDetailEinkaufsangebot);

			} else if (eI.getSource() == panelQueryEinkaufsangebotpositionen) {
				panelDetailEinkaufsangebotpositionen.eventActionNew(eI, true,
						false);
				panelDetailEinkaufsangebotpositionen.eventYouAreSelected(false);
				setSelectedComponent(panelSplitEinkaufsangebotpositionen); // ui
			}
		} else if (eI.getID() == ItemChangedEvent.ACTION_KOPIEREN) {
			if (eI.getSource().equals(panelQueryEinkaufsangebotpositionen)) {
				copyHV();
			}
		}

	}

	/**
	 * Behandle ChangeEvent; zB Tabwechsel oben.
	 * 
	 * @param e
	 *            ChangeEvent
	 * @throws Throwable
	 */
	public void lPEventObjectChanged(ChangeEvent e) throws Throwable {

		super.lPEventObjectChanged(e);
		int selectedIndex = this.getSelectedIndex();

		if (selectedIndex == IDX_PANEL_AUSWAHL) {
			refreshAuswahl();
			panelQueryEinkaufsangebot.eventYouAreSelected(false);
			panelQueryEinkaufsangebot.updateButtons();
			if (panelQueryEinkaufsangebot.getSelectedId() == null) {
				getInternalFrame().enableAllOberePanelsExceptMe(this,
						IDX_PANEL_AUSWAHL, false);
			}

		} else if (selectedIndex == IDX_PANEL_KOPFDATEN) {
			Integer key = null;
			if (getInternalFrameAngebotstkl().getEinkaufsangebotDto() != null) {
				key = getInternalFrameAngebotstkl().getEinkaufsangebotDto()
						.getIId();
			}
			createKopfdaten(key);
			panelDetailEinkaufsangebot.eventYouAreSelected(false);
		} else if (selectedIndex == IDX_PANEL_KOMMENTAR) {
			Integer key = null;
			if (getInternalFrameAngebotstkl().getEinkaufsangebotDto() != null) {
				key = getInternalFrameAngebotstkl().getEinkaufsangebotDto()
						.getIId();
			}
			createKommentar(key);
			panelDetailEinkaufsangebotKommentar.eventYouAreSelected(false);
		} else if (selectedIndex == IDX_PANEL_POSITIONEN) {
			refreshAngebotstklPositionen();

			Integer key = null;
			if (getInternalFrameAngebotstkl().getEinkaufsangebotDto() != null) {
				key = getInternalFrameAngebotstkl().getEinkaufsangebotDto()
						.getIId();

				FilterKriterium[] defaultfk = AngebotstklFilterFactory
						.getInstance().createFKEinkaufsangebot(key);
				panelQueryEinkaufsangebotpositionen.setDefaultFilter(defaultfk);
			}

			panelSplitEinkaufsangebotpositionen.eventYouAreSelected(false);

			panelQueryEinkaufsangebotpositionen
					.updateButtons(panelDetailEinkaufsangebotpositionen
							.getLockedstateDetailMainKey());
		}
		refreshTitle();
	}

	public javax.swing.JMenuBar getJMenuBar() throws Throwable {
		if (wrapperMenuBar == null) {
			wrapperMenuBar = new WrapperMenuBar(this);

			// Menue Datei; ein neuer Eintrag wird immer oben im Menue
			// eingetragen
			JMenu jmModul = (JMenu) wrapperMenuBar
					.getComponent(WrapperMenuBar.MENU_MODUL);

			jmModul.add(new JSeparator(), 0);

			JMenuItem menuItemDateiDrucken = new JMenuItem(LPMain.getInstance()
					.getTextRespectUISPr("lp.menu.drucken"));
			menuItemDateiDrucken.addActionListener(this);
			menuItemDateiDrucken.setActionCommand(MENU_DATEI_DRUCKEN);
			jmModul.add(menuItemDateiDrucken, 0);

		}

		return wrapperMenuBar;
	}

	private PanelSplit refreshAngebotstklPositionen() throws Throwable {
		if (panelSplitEinkaufsangebotpositionen == null) {
			panelDetailEinkaufsangebotpositionen = new PanelEinkaufsangebotpositionen(
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("angb.panel.positionen"), null); // eventuell
			// gibt
			// es
			// noch
			// keine
			// position

			QueryType[] qtPositionen = null;
			FilterKriterium[] filtersPositionen = AngebotstklFilterFactory
					.getInstance().createFKEinkaufsangebot(
							getInternalFrameAngebotstkl()
									.getEinkaufsangebotDto().getIId());

			String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW,
					PanelBasis.ACTION_POSITION_VONNNACHNMINUS1,
					PanelBasis.ACTION_POSITION_VONNNACHNPLUS1,
					PanelBasis.ACTION_POSITION_VORPOSITIONEINFUEGEN,
					PanelBasis.ACTION_KOPIEREN,
					PanelBasis.ACTION_EINFUEGEN_LIKE_NEW

			};

			panelQueryEinkaufsangebotpositionen = new PanelQuery(qtPositionen,
					filtersPositionen,
					QueryParameters.UC_ID_EINKAUFSANGEBOTPOSITIONEN,
					aWhichButtonIUse, getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("angb.panel.positionen"), true); // flag
			// ,
			// damit
			// flr
			// erst
			// beim
			// aufruf
			// des
			// panels
			// loslaeuft

			panelSplitEinkaufsangebotpositionen = new PanelSplit(
					getInternalFrame(), panelDetailEinkaufsangebotpositionen,
					panelQueryEinkaufsangebotpositionen, 140);

			boolean intelligenterStklImport = LPMain
					.getInstance()
					.getDesktop()
					.darfAnwenderAufZusatzfunktionZugreifen(
							MandantFac.ZUSATZFUNKTION_INTELLIGENTER_STUECKLISTENIMPORT);

			panelQueryEinkaufsangebotpositionen.createAndSaveAndShowButton(
					"/com/lp/client/res/document_into.png", LPMain.getInstance()
							.getTextRespectUISPr(intelligenterStklImport ? 
									"stkl.intelligenterstklimport" : "stkl.positionen.cvsimport"),
					BUTTON_IMPORTCSV_EKAGSTKLPOSITIONEN, null);
			
			panelQueryEinkaufsangebotpositionen
					.setMultipleRowSelectionEnabled(true);

			setComponentAt(IDX_PANEL_POSITIONEN,
					panelSplitEinkaufsangebotpositionen);
		}

		return panelSplitEinkaufsangebotpositionen;
	}

	public void copyHV() throws ExceptionLP, Throwable {

		Object aoIIdPosition[] = panelQueryEinkaufsangebotpositionen
				.getSelectedIds();

		if (aoIIdPosition != null && (aoIIdPosition.length > 0)) {
			EinkaufsangebotpositionDto[] dtos = new EinkaufsangebotpositionDto[aoIIdPosition.length];
			Integer aIIdPosition[] = new Integer[aoIIdPosition.length];
			for (int i = 0; i < aIIdPosition.length; i++) {
				aIIdPosition[i] = (Integer) aoIIdPosition[i];
				dtos[i] = DelegateFactory
						.getInstance()
						.getAngebotstklDelegate()
						.einkaufsangebotpositionFindByPrimaryKey(
								(Integer) aoIIdPosition[i]);
			}
			LPMain.getInstance().getPasteBuffer()
					.writeObjectToPasteBuffer(dtos);
		}

	}

	public void einfuegenHV() throws IOException, ParserConfigurationException,
			SAXException, Throwable {

		Object o = LPMain.getInstance().getPasteBuffer()
				.readObjectFromPasteBuffer();

		if (o instanceof BelegpositionDto[]) {

			EinkaufsangebotpositionDto[] positionDtos = DelegateFactory
					.getInstance()
					.getBelegpostionkonvertierungDelegate()
					.konvertiereNachEinkaufsangebotpositionDto(
							(BelegpositionDto[]) o);

			if (positionDtos != null) {
				Integer iId = null;

				for (int i = 0; i < positionDtos.length; i++) {
					EinkaufsangebotpositionDto positionDto = positionDtos[i];
					try {
						positionDto.setIId(null);
						positionDto
								.setBelegIId((Integer) panelQueryEinkaufsangebot
										.getSelectedId());
						positionDto.setISort(null);

						// wir legen eine neue position an
						if (iId == null) {
							iId = DelegateFactory.getInstance()
									.getAngebotstklDelegate()
									.createEinkaufsangebotposition(positionDto);
						} else {
							DelegateFactory.getInstance()
									.getAngebotstklDelegate()
									.createEinkaufsangebotposition(positionDto);
						}
					} catch (Throwable t) {
						// nur loggen!
						myLogger.error(t.getMessage(), t);
					}
				}

				// die Liste neu aufbauen
				panelQueryEinkaufsangebotpositionen.eventYouAreSelected(false);

				// den Datensatz in der Liste selektieren
				panelQueryEinkaufsangebotpositionen.setSelectedId(iId);

				// im Detail den selektierten anzeigen
				panelQueryEinkaufsangebotpositionen.eventYouAreSelected(false);
			}

		}

	}

	public void fillMustFields(BelegpositionDto belegposDtoI, int xalOfBelegPosI)
			throws Throwable {
		AgstklpositionDto angStklPosDto = (AgstklpositionDto) belegposDtoI;

		String sPosArt = angStklPosDto.getPositionsartCNr();
		if (!LocaleFac.POSITIONSART_IDENT.startsWith(sPosArt)
				&& !LocaleFac.POSITIONSART_HANDEINGABE.startsWith(sPosArt)) {
			String sMsg = "LocaleFac.POSITIONSART_IDENT.startsWith(sPosArt) "
					+ "&& !LocaleFac.POSITIONSART_HANDEINGABE.startsWith(sPosArt)";
			throw new IllegalArgumentException(sMsg);
		}

		angStklPosDto.setBelegIId(getInternalFrameAngebotstkl().getAgstklDto()
				.getIId());
		angStklPosDto.setISort(xalOfBelegPosI + 1000);

		if (angStklPosDto.getNGestehungspreis() == null) {
			// WH: 2007-01-15: Gestpreis aus Hauptlager des Mandanten
			// uebernehmen
			if (angStklPosDto.getArtikelIId() != null) {
				LagerDto lagerDto = DelegateFactory.getInstance()
						.getLagerDelegate().getHauptlagerDesMandanten();

				BigDecimal preis = DelegateFactory
						.getInstance()
						.getLagerDelegate()
						.getGemittelterGestehungspreisEinesLagers(
								angStklPosDto.getArtikelIId(),
								lagerDto.getIId());
				angStklPosDto.setNGestehungspreis(preis);
			} else {
				angStklPosDto.setNGestehungspreis(new BigDecimal(0));
			}
		}
		if (angStklPosDto.getBDrucken() == null) {
			angStklPosDto.setBDrucken(Helper.boolean2Short(true));
		}
		if (angStklPosDto.getBRabattsatzuebersteuert() == null) {
			angStklPosDto.setBRabattsatzuebersteuert(Helper
					.boolean2Short(false));
		}
		if (angStklPosDto.getNNettoeinzelpreis() == null) {
			angStklPosDto.setNNettoeinzelpreis(new BigDecimal(0));
		}
		if (angStklPosDto.getNNettogesamtpreis() == null) {
			angStklPosDto.setNNettogesamtpreis(new BigDecimal(0));
		}
		if (angStklPosDto.getFRabattsatz() == null) {
			angStklPosDto.setFRabattsatz(0.0);
		}
		if (angStklPosDto.getBArtikelbezeichnunguebersteuert() == null) {
			angStklPosDto.setBArtikelbezeichnunguebersteuert(Helper
					.boolean2Short(false));
		}

	}

	public Integer saveBelegPosAsTextpos(
			POSDocument2POSDto pOSDocument2POSDtoI, int xalOfBelegPosI)
			throws ExceptionLP, Throwable {
		return null;
	}

}
