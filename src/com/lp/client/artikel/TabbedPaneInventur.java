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

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.event.ChangeEvent;

import com.lp.client.frame.HelperClient;
import com.lp.client.frame.LockStateValue;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQuery;
import com.lp.client.frame.component.PanelSplit;
import com.lp.client.frame.component.TabbedPane;
import com.lp.client.frame.component.WrapperMenu;
import com.lp.client.frame.component.WrapperMenuBar;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.artikel.service.InventurDto;
import com.lp.server.artikel.service.InvenurlisteImportDto;
import com.lp.server.util.Facade;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.FilterKriteriumDirekt;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.util.Helper;
import com.lp.util.csv.LPCSVReader;

@SuppressWarnings("static-access")
/*
 * <p>&UUml;berschrift: </p> <p>Beschreibung: </p> <p>Copyright: Copyright (c)
 * 2004</p> <p>Organisation: </p>
 * 
 * @author Christian Kollmann
 * 
 * @version $Revision: 1.8 $
 */
public class TabbedPaneInventur extends TabbedPane {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private PanelQuery panelQueryInventur = null;
	private PanelBasis panelDetailInventur = null;

	private PanelSplit panelSplitInventurliste = null;
	private PanelQuery panelQueryInventurliste = null;
	private PanelInventurliste panelBottomInventurliste = null;

	private PanelSplit panelSplitInventurstand = null;
	private PanelInventurstand panelBottomInventurstand = null;
	private PanelQuery panelQueryInventurstand = null;

	private PanelQuery panelQueryInventurprotokoll = null;

	private final String MENUE_INVENTUR_ACTION_ZAEHLLISTE = "MENUE_INVENTUR_ACTION_ZAEHLLISTE";
	private final String MENUE_INVENTUR_ACTION_INVENTURSTAND = "MENUE_INVENTUR_ACTION_INVENTURSTAND";
	private final String MENUE_INVENTUR_ACTION_INVENTURPROTOKOLL = "MENUE_INVENTUR_ACTION_INVENTURPROTOKOLL";
	private final String MENUE_INVENTUR_ACTION_DELTALISTE = "MENUE_INVENTUR_ACTION_DELTALISTE";

	private final String MENUE_PFLEGE_DURCHFUEHRUNG_ZURUECKNEHMEN = "MENUE_PFLEGE_DURCHFUEHRUNG_ZURUECKNEHMEN";
	private final String MENUE_PFLEGE_IVENTURPROTOKOLL_AB_STICHTAG_ZURUECKNEHMEN = "MENUE_PFLEGE_IVENTURPROTOKOLL_AB_STICHTAG_ZURUECKNEHMEN";

	private final String MENUE_INVENTUR_ACTION_INVENTURLISTE = "MENUE_INVENTUR_ACTION_INVENTURLISTE";

	private static final String ACTION_SPECIAL_QUICKINSERT = "action_special_quickinsert";
	private static final String ACTION_SPECIAL_CSVIMPORT = "action_special_csvimport";

	private final String BUTTON_QUICKINSERT = PanelBasis.ALWAYSENABLED
			+ ACTION_SPECIAL_QUICKINSERT;

	private final String BUTTON_CSVIMPORT = PanelBasis.ALWAYSENABLED
			+ ACTION_SPECIAL_CSVIMPORT;

	private static final String ACTION_SPECIAL_PREISE_ABWERTEN = "action_special_preiseabwerten";
	private final String BUTTON_PREISEABWERTEN = PanelBasis.ALWAYSENABLED
			+ ACTION_SPECIAL_PREISE_ABWERTEN;

	private static final String ACTION_SPECIAL_INVENTURPREISE_AKTUALISIEREN = "action_special_inventurpreise_akutalisieren";
	private static final String ACTION_SPECIAL_INVENTURPREISE_AKTUALISIEREN_GESTPREIS_ZUM_INVENTURDATUM = "action_special_inventurpreise_akutalisieren_gestpreis_zum_inventurdatum";
	private static final String ACTION_SPECIAL_INVENTURPREISE_AKTUALISIEREN_EKPREIS = "action_special_inventurpreise_akutalisieren_ekpreis";

	private final String BUTTON_INVENTURPREISE_AKTUALISIEREN = PanelBasis.ALWAYSENABLED
			+ ACTION_SPECIAL_INVENTURPREISE_AKTUALISIEREN;
	private final String BUTTON_INVENTURPREISE_AKTUALISIEREN_GESTPREIS_ZUM_INVENTURDATUM = PanelBasis.ALWAYSENABLED
			+ ACTION_SPECIAL_INVENTURPREISE_AKTUALISIEREN_GESTPREIS_ZUM_INVENTURDATUM;

	private final String BUTTON_INVENTURPREISE_AKTUALISIEREN_EKPREIS = PanelBasis.ALWAYSENABLED
			+ ACTION_SPECIAL_INVENTURPREISE_AKTUALISIEREN_EKPREIS;

	private final static int IDX_PANEL_AUSWAHL = 0;
	private final static int IDX_PANEL_DETAIL = 1;
	private final static int IDX_PANEL_INVENTURLISTE = 2;
	private final static int IDX_PANEL_INVENTURPROTOKOLL = 3;
	private final static int IDX_PANEL_INVENTURSTAND = 4;

	private WrapperMenuBar wrapperManuBar = null;

	public TabbedPaneInventur(InternalFrame internalFrameI) throws Throwable {
		super(internalFrameI, LPMain.getInstance().getTextRespectUISPr(
				"artikel.inventur"));
		jbInit();
		initComponents();
	}

	public PanelQuery getPanelQueryInventur() {
		return panelQueryInventur;
	}

	private void jbInit() throws Throwable {

		insertTab(LPMain.getInstance().getTextRespectUISPr("lp.auswahl"), null,
				null, LPMain.getInstance().getTextRespectUISPr("lp.auswahl"),
				IDX_PANEL_AUSWAHL);

		insertTab(LPMain.getInstance().getTextRespectUISPr("lp.detail"), null,
				null, LPMain.getInstance().getTextRespectUISPr("lp.detail"),
				IDX_PANEL_DETAIL);
		insertTab(
				LPMain.getInstance().getTextRespectUISPr(
						"artikel.inventurliste"),
				null,
				null,
				LPMain.getInstance().getTextRespectUISPr(
						"artikel.inventurliste"), IDX_PANEL_INVENTURLISTE);
		insertTab(
				LPMain.getInstance().getTextRespectUISPr(
						"artikel.inventurprotokoll"),
				null,
				null,
				LPMain.getInstance().getTextRespectUISPr(
						"artikel.inventurprotokoll"),
				IDX_PANEL_INVENTURPROTOKOLL);
		insertTab(
				LPMain.getInstance().getTextRespectUISPr(
						"artikel.inventurstand"),
				null,
				null,
				LPMain.getInstance().getTextRespectUISPr(
						"artikel.inventurstand"), IDX_PANEL_INVENTURSTAND);

		createAuswahl();

		panelQueryInventur.eventYouAreSelected(false);
		if ((Integer) panelQueryInventur.getSelectedId() != null) {
			getInternalFrameArtikel().setInventurDto(
					DelegateFactory
							.getInstance()
							.getInventurDelegate()
							.inventurFindByPrimaryKey(
									(Integer) panelQueryInventur
											.getSelectedId()));
		}
		// damit D2 einen aktuellen hat.
		ItemChangedEvent it = new ItemChangedEvent(panelQueryInventur,
				ItemChangedEvent.ITEM_CHANGED);
		lPEventItemChanged(it);

		this.addChangeListener(this);
		this.getInternalFrame().addItemChangedListener(this);

	}

	private void createAuswahl() throws Throwable {
		if (panelQueryInventur == null) {
			// Artikelauswahlliste
			String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW };
			panelQueryInventur = new PanelQuery(null,
					com.lp.client.system.SystemFilterFactory.getInstance()
							.createFKMandantCNr(),
					QueryParameters.UC_ID_INVENTUR, aWhichButtonIUse,
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("lp.auswahl"), true);

			setComponentAt(IDX_PANEL_AUSWAHL, panelQueryInventur);

		}
	}

	private void createDetail(Integer key) throws Throwable {
		if (panelDetailInventur == null) {
			panelDetailInventur = new PanelInventur(getInternalFrame(), LPMain
					.getInstance().getTextRespectUISPr("lp.detail"), key);
			setComponentAt(IDX_PANEL_DETAIL, panelDetailInventur);
		}
	}

	private void createInventurprotokoll(Integer inventurIId) throws Throwable {

		if (panelQueryInventurprotokoll == null) {

			panelQueryInventurprotokoll = new PanelQuery(null,
					ArtikelFilterFactory.getInstance()
							.createFKInventurprotkoll(inventurIId),
					QueryParameters.UC_ID_INVENTURPROTOKOLL, null,
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("artikel.inventurprotokoll"),
					true);
			setComponentAt(IDX_PANEL_INVENTURPROTOKOLL,
					panelQueryInventurprotokoll);
			panelQueryInventurprotokoll.befuellePanelFilterkriterienDirekt(
					new FilterKriteriumDirekt(
							"flrinventurliste.flrartikel.c_nr", "",
							FilterKriterium.OPERATOR_LIKE, LPMain.getInstance()
									.getTextRespectUISPr(
											"artikel.artikelnummer"),
							FilterKriteriumDirekt.PROZENT_TRAILING, true, true,
							Facade.MAX_UNBESCHRAENKT)

					, null);

		} else {
			// filter refreshen.
			panelQueryInventurprotokoll.setDefaultFilter(ArtikelFilterFactory
					.getInstance().createFKInventurprotkoll(inventurIId));
		}
	}

	private void createInventurstand(Integer inventurIId) throws Throwable {

		if (panelQueryInventurstand == null) {

			panelQueryInventurstand = new PanelQuery(null, ArtikelFilterFactory
					.getInstance().createFKInventurprotkoll(inventurIId),
					QueryParameters.UC_ID_INVENTURSTAND, null,
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("artikel.inventurstand"), true);

			panelQueryInventurstand.createAndSaveAndShowButton(
					"/com/lp/client/res/selection_down.png",
					LPMain.getInstance().getTextRespectUISPr(
							"artikel.inventur.preiseabwerten"),
					BUTTON_PREISEABWERTEN, null);

			panelQueryInventurstand.createAndSaveAndShowButton(
					"/com/lp/client/res/selection_replace.png",
					LPMain.getInstance().getTextRespectUISPr(
							"artikel.inventur.inventurpreiseaktualisieren"),
					BUTTON_INVENTURPREISE_AKTUALISIEREN, null);
			panelQueryInventurstand
					.createAndSaveAndShowButton(
							"/com/lp/client/res/selection_replace.png",
							LPMain.getInstance()
									.getTextRespectUISPr(
											"artikel.inventur.inventurpreiseaktualisieren.gestpreisinventurdatum"),
							BUTTON_INVENTURPREISE_AKTUALISIEREN_GESTPREIS_ZUM_INVENTURDATUM,
							null);
			panelQueryInventurstand
					.createAndSaveAndShowButton(
							"/com/lp/client/res/selection_recycle.png",
							LPMain.getInstance()
									.getTextRespectUISPr(
											"artikel.inventur.inventurpreiseaktualisieren.ekpreisinventurdatum"),
							BUTTON_INVENTURPREISE_AKTUALISIEREN_EKPREIS, null);

			panelQueryInventurstand.befuellePanelFilterkriterienDirekt(
					new FilterKriteriumDirekt("flrartikel.c_nr", "",
							FilterKriterium.OPERATOR_LIKE, LPMain.getInstance()
									.getTextRespectUISPr(
											"artikel.artikelnummer"),
							FilterKriteriumDirekt.PROZENT_TRAILING, true, true,
							Facade.MAX_UNBESCHRAENKT)

					, null);

			panelBottomInventurstand = new PanelInventurstand(
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("artikel.inventurstand"), null);
			panelSplitInventurstand = new PanelSplit(getInternalFrame(),
					panelBottomInventurstand, panelQueryInventurstand, 330);
			setComponentAt(IDX_PANEL_INVENTURSTAND, panelSplitInventurstand);

		} else {
			// filter refreshen.
			panelQueryInventurstand.setDefaultFilter(ArtikelFilterFactory
					.getInstance().createFKInventurstand(inventurIId));
		}
	}

	private void createInventurliste(Integer key) throws Throwable {

		if (panelQueryInventurliste == null) {
			String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW,
					PanelBasis.ACTION_FILTER };

			FilterKriterium[] filters = ArtikelFilterFactory.getInstance()
					.createFKInventurliste(key);

			panelQueryInventurliste = new PanelQuery(ArtikelFilterFactory
					.getInstance().createQTInventurliste(), filters,
					QueryParameters.UC_ID_INVENTURLISTE, aWhichButtonIUse,
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("artikel.inventurliste"), true);

			panelBottomInventurliste = new PanelInventurliste(
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("artikel.inventurliste"), null);

			panelQueryInventurliste.createAndSaveAndShowButton(
					"/com/lp/client/res/scanner16x16.png",
					LPMain.getInstance().getTextRespectUISPr(
							"artikel.inventurliste.schnelleingabe"),
					BUTTON_QUICKINSERT, null);

			panelQueryInventurliste.createAndSaveAndShowButton(
					"/com/lp/client/res/document_into.png",
					LPMain.getInstance().getTextRespectUISPr(
							"artikel.inventurliste.csvimport"),
					BUTTON_CSVIMPORT, null);

			panelQueryInventurliste.befuellePanelFilterkriterienDirekt(
					new FilterKriteriumDirekt("flrartikel.c_nr", "",
							FilterKriterium.OPERATOR_LIKE, LPMain.getInstance()
									.getTextRespectUISPr(
											"artikel.artikelnummer"),
							FilterKriteriumDirekt.PROZENT_TRAILING, true, true,
							Facade.MAX_UNBESCHRAENKT)

					, null);

			panelSplitInventurliste = new PanelSplit(getInternalFrame(),
					panelBottomInventurliste, panelQueryInventurliste, 280);
			setComponentAt(IDX_PANEL_INVENTURLISTE, panelSplitInventurliste);
		} else {
			// filter refreshen.
			panelQueryInventurliste.setDefaultFilter(ArtikelFilterFactory
					.getInstance().createFKInventurliste(key));
		}
	}

	public InternalFrameArtikel getInternalFrameArtikel() {
		return (InternalFrameArtikel) getInternalFrame();
	}

	public void setKeyWasForLockMe() {
		Object oKey = panelQueryInventur.getSelectedId();

		if (oKey != null) {
			getInternalFrame().setKeyWasForLockMe(oKey.toString());
		} else {
			getInternalFrame().setKeyWasForLockMe(null);
		}
	}

	public void lPEventItemChanged(ItemChangedEvent e) throws Throwable {
		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			if (e.getSource() == panelQueryInventur) {
				Integer iId = (Integer) panelQueryInventur.getSelectedId();
				if (iId != null) {
					setSelectedComponent(panelDetailInventur);
				}
			}
		} else if (e.getID() == ItemChangedEvent.ITEM_CHANGED) {
			if (e.getSource() == panelQueryInventur) {
				if (panelQueryInventur.getSelectedId() != null) {
					getInternalFrameArtikel().setKeyWasForLockMe(
							panelQueryInventur.getSelectedId() + "");

					// Dto-setzen
					getInternalFrameArtikel().setInventurDto(
							DelegateFactory
									.getInstance()
									.getInventurDelegate()
									.inventurFindByPrimaryKey(
											(Integer) panelQueryInventur
													.getSelectedId()));
					String sBezeichnung = getInternalFrameArtikel()
							.getInventurDto().getCBez();
					getInternalFrame().setLpTitle(
							InternalFrame.TITLE_IDX_AS_I_LIKE, sBezeichnung);
					if (panelQueryInventur.getSelectedId() == null) {
						getInternalFrame().enableAllOberePanelsExceptMe(this,
								IDX_PANEL_AUSWAHL, false);
					} else {
						getInternalFrame().enableAllOberePanelsExceptMe(this,
								IDX_PANEL_AUSWAHL, true);
					}
				} else {
					getInternalFrame().enableAllOberePanelsExceptMe(this,
							IDX_PANEL_AUSWAHL, false);
				}

			} else if (e.getSource() == panelQueryInventurliste) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				panelBottomInventurliste.setKeyWhenDetailPanel(key);
				panelBottomInventurliste.eventYouAreSelected(false);
				panelQueryInventurliste.updateButtons();
			} else if (e.getSource() == panelQueryInventurstand) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				panelBottomInventurstand.setKeyWhenDetailPanel(key);
				panelBottomInventurstand.eventYouAreSelected(false);
				panelQueryInventurstand.updateButtons();
			}

		} else if (e.getID() == ItemChangedEvent.ACTION_DISCARD) {
			if (e.getSource() == panelBottomInventurliste) {
				panelSplitInventurliste.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomInventurstand) {
				panelSplitInventurstand.eventYouAreSelected(false);
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_YOU_ARE_SELECTED) {
			refreshTitle();
		} else if (e.getID() == ItemChangedEvent.ACTION_UPDATE) {
			if (e.getSource() == panelBottomInventurliste) {
				panelQueryInventurliste.updateButtons(new LockStateValue(
						PanelBasis.LOCK_FOR_NEW));
			} else if (e.getSource() == panelBottomInventurstand) {
				panelQueryInventurstand.updateButtons(new LockStateValue(
						PanelBasis.LOCK_FOR_NEW));
			}
		}

		else if (e.getID() == ItemChangedEvent.ACTION_NEW) {
			if (e.getSource() == panelQueryInventur) {
				createDetail((Integer) panelQueryInventur.getSelectedId());

				if (panelQueryInventur.getSelectedId() == null) {
					getInternalFrame().enableAllPanelsExcept(true);
				}
				panelDetailInventur.eventActionNew(null, true, false);
				setSelectedComponent(panelDetailInventur);
			} else if (e.getSource() == panelQueryInventurliste) {
				panelBottomInventurliste.eventActionNew(e, true, false);
				panelBottomInventurliste.eventYouAreSelected(false);
				this.setSelectedComponent(panelSplitInventurliste);
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_SAVE) {

			if (e.getSource() == panelDetailInventur) {
				panelQueryInventur.clearDirektFilter();
				Object oKey = panelDetailInventur.getKeyWhenDetailPanel();

				panelQueryInventur.setSelectedId(oKey);
			}
			if (e.getSource() == panelBottomInventurliste) {
				Object oKey = panelBottomInventurliste.getKeyWhenDetailPanel();
				panelQueryInventurliste.eventYouAreSelected(false);
				panelQueryInventurliste.setSelectedId(oKey);
				panelSplitInventurliste.eventYouAreSelected(false);

			}
			if (e.getSource() == panelBottomInventurstand) {
				Object oKey = panelBottomInventurstand.getKeyWhenDetailPanel();
				panelQueryInventurstand.eventYouAreSelected(false);
				panelQueryInventurstand.setSelectedId(oKey);
				panelSplitInventurstand.eventYouAreSelected(false);

			}
		} else if (e.getID() == ItemChangedEvent.ACTION_GOTO_MY_DEFAULT_QP) {
			if (e.getSource() == panelDetailInventur) {
				this.setSelectedComponent(panelQueryInventur);
				setKeyWasForLockMe();
				panelQueryInventur.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomInventurliste) {
				setKeyWasForLockMe();
				if (panelBottomInventurliste.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryInventurliste
							.getId2SelectAfterDelete();
					panelQueryInventurliste.setSelectedId(oNaechster);
				}
				panelSplitInventurliste.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomInventurstand) {
				setKeyWasForLockMe();
				panelSplitInventurstand.eventYouAreSelected(false);
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_SPECIAL_BUTTON) {
			String sAspectInfo = ((ISourceEvent) e.getSource()).getAspect();
			if (e.getSource() == panelQueryInventurliste) {
				if (!Helper.short2boolean(getInternalFrameArtikel()
						.getInventurDto().getBInventurdurchgefuehrt())) {
					if (sAspectInfo.endsWith(ACTION_SPECIAL_QUICKINSERT)) {

						DialogInventurlisteQuickInsert d = new DialogInventurlisteQuickInsert(
								panelBottomInventurliste,
								getInternalFrameArtikel().getInventurDto()
										.getIId());
						LPMain.getInstance().getDesktop()
								.platziereDialogInDerMitteDesFensters(d);
						d.setVisible(true);
						panelSplitInventurliste.eventYouAreSelected(false);
						panelQueryInventurliste.updateButtons();

					} else if (sAspectInfo.endsWith(ACTION_SPECIAL_CSVIMPORT)) {

						if (getInternalFrameArtikel().getInventurDto()
								.getLagerIId() != null) {

							File[] files = HelperClient.chooseFile(this,
									HelperClient.FILE_FILTER_CSV, false);

							if (files != null && files.length > 0) {

								LPCSVReader reader = new LPCSVReader(
										new FileReader(files[0]), ';');

								String[] sLine;

								ArrayList<InvenurlisteImportDto> alImportdaten = new ArrayList<InvenurlisteImportDto>();

								do {
									sLine = reader.readNext();

									if (sLine != null) {
										if (sLine.length < 2) {
											DialogFactory
													.showModalDialog(
															LPMain.getInstance()
																	.getTextRespectUISPr(
																			"lp.error"),
															"CSV-Datei muss genau 2 Spalten beinhalten.");
											return;
										}
										InvenurlisteImportDto invenurlisteImportDto = new InvenurlisteImportDto();
										invenurlisteImportDto
												.setCArtikelnummer(sLine[0]);
										invenurlisteImportDto
												.setCInventurmenge(sLine[1]);
										alImportdaten
												.add(invenurlisteImportDto);

									}

								} while (sLine != null);
								String s=DelegateFactory
										.getInstance()
										.getInventurDelegate()
										.importiereInventurliste(
												getInternalFrameArtikel()
														.getInventurDto()
														.getIId(),
												alImportdaten);
								
								
								
								if(s.length()>0){
									java.io.File ausgabedatei =new File("log", "error_csvimport_inventurliste.txt");
									java.io.FileWriter fw = new java.io.FileWriter(ausgabedatei);
									java.io.BufferedWriter bw = new java.io.BufferedWriter(fw);
									bw.write(s);
									bw.close();
									
									DialogFactory
									.showModalDialog(
											LPMain.getInstance()
													.getTextRespectUISPr(
															"lp.error"),
											"CSV-Import durchgef\u00FChrt, es sind jedoch Fehler aufgetreten ("+ausgabedatei.getAbsolutePath()+")");
									
								} else {
									DialogFactory
									.showModalDialog(
											LPMain.getInstance()
													.getTextRespectUISPr(
															"lp.error"),
											"CSV-Import fehlerfrei durchgef\u00FChrt!");
								}
								
								
								

								
							}
							
							panelSplitInventurliste.eventYouAreSelected(false);
							panelQueryInventurliste.updateButtons();
							
						} else {

							// Fehler
							DialogFactory
									.showModalDialog(
											LPMain.getInstance()
													.getTextRespectUISPr(
															"lp.error"),
											LPMain.getInstance()
													.getTextRespectUISPr(
															"artikel.inventurliste.csvimport.error"));
						}

					}
				} else {
					DialogFactory
							.showModalDialog(
									LPMain.getInstance().getTextRespectUISPr(
											"lp.error"),
									LPMain.getInstance()
											.getTextRespectUISPr(
													"artikel.error.inventur.bereitsdurchgefuehrt"));

				}
			} else if (e.getSource() == panelQueryInventurstand) {

				if (sAspectInfo.endsWith(ACTION_SPECIAL_PREISE_ABWERTEN)) {
					if (!Helper.short2boolean(getInternalFrameArtikel()
							.getInventurDto().getBAbwertungdurchgefuehrt())) {

						boolean b = DialogFactory
								.showModalJaNeinDialog(getInternalFrame(),
										"Sollen die St\u00FCcklisten auch abgewertet werden?");

						DelegateFactory
								.getInstance()
								.getInventurDelegate()
								.preiseAbwerten(
										getInternalFrameArtikel()
												.getInventurDto().getIId(), b);

						InventurDto inventurDto = DelegateFactory
								.getInstance()
								.getInventurDelegate()
								.inventurFindByPrimaryKey(
										getInternalFrameArtikel()
												.getInventurDto().getIId());
						getInternalFrameArtikel().setInventurDto(inventurDto);

					} else {
						DialogFactory
								.showModalDialog(
										LPMain.getInstance()
												.getTextRespectUISPr("lp.error"),
										LPMain.getInstance()
												.getTextRespectUISPr(
														"artikel.error.inventur.bereitsabgewertet"));
					}
				} else if (sAspectInfo
						.endsWith(ACTION_SPECIAL_INVENTURPREISE_AKTUALISIEREN)) {

					boolean b = DialogFactory
							.showModalJaNeinDialog(
									getInternalFrame(),
									"Aktualisieren der Inventurpreise anhand aktueller Gestehungspreise bzw. EK-Preise?");

					if (b == true) {

						DelegateFactory
								.getInstance()
								.getInventurDelegate()
								.inventurpreiseAktualisieren(
										getInternalFrameArtikel()
												.getInventurDto().getIId(),
										false);
					}
				} else if (sAspectInfo
						.endsWith(ACTION_SPECIAL_INVENTURPREISE_AKTUALISIEREN_EKPREIS)) {

					boolean b = DialogFactory
							.showModalJaNeinDialog(getInternalFrame(),
									"Aktualisieren der Inventurpreise anhand EK-Preise zum Inventurdatum?");

					if (b == true) {

						DelegateFactory
								.getInstance()
								.getInventurDelegate()
								.inventurpreiseAufEkPreisSetzen(
										getInternalFrameArtikel()
												.getInventurDto().getIId());
					}
				} else if (sAspectInfo
						.endsWith(ACTION_SPECIAL_INVENTURPREISE_AKTUALISIEREN_GESTPREIS_ZUM_INVENTURDATUM)) {

					boolean b = DialogFactory
							.showModalJaNeinDialog(getInternalFrame(),
									"Aktualisieren der Inventurpreise anhand Gestehungspreise zum Inventurdatum?");

					if (b == true) {

						DelegateFactory
								.getInstance()
								.getInventurDelegate()
								.inventurpreiseAktualisieren(
										getInternalFrameArtikel()
												.getInventurDto().getIId(),
										true);
					}
				}

			}
		}

	}

	private void refreshTitle() {
		getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_OHRWASCHLUNTEN,
				LPMain.getInstance().getTextRespectUISPr("artikel.inventur"));
		getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_OHRWASCHLOBEN,
				((PanelBasis) this.getSelectedComponent()).getAdd2Title());

		if (getInternalFrameArtikel().getInventurDto() != null) {

			getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_AS_I_LIKE,
					getInternalFrameArtikel().getInventurDto().getCBez());
		}
	}

	public void lPEventObjectChanged(ChangeEvent e) throws Throwable {
		super.lPEventObjectChanged(e);
		int selectedIndex = this.getSelectedIndex();

		if (selectedIndex == IDX_PANEL_AUSWAHL) {
			createAuswahl();
			panelQueryInventur.eventYouAreSelected(false);
			if (panelQueryInventur.getSelectedId() == null) {
				getInternalFrame().enableAllOberePanelsExceptMe(this,
						IDX_PANEL_AUSWAHL, false);
			}
			panelQueryInventur.updateButtons();
		} else if (selectedIndex == IDX_PANEL_DETAIL) {
			Integer key = null;
			if (getInternalFrameArtikel().getInventurDto() != null) {
				key = getInternalFrameArtikel().getInventurDto().getIId();
			}
			createDetail(key);
			panelDetailInventur.eventYouAreSelected(false);
		} else if (selectedIndex == IDX_PANEL_INVENTURLISTE) {
			createInventurliste(getInternalFrameArtikel().getInventurDto()
					.getIId());
			panelSplitInventurliste.eventYouAreSelected(false);
			panelQueryInventurliste.updateButtons();
		} else if (selectedIndex == IDX_PANEL_INVENTURPROTOKOLL) {
			createInventurprotokoll(getInternalFrameArtikel().getInventurDto()
					.getIId());
			panelQueryInventurprotokoll.eventYouAreSelected(false);
			panelQueryInventurprotokoll.updateButtons();
		} else if (selectedIndex == IDX_PANEL_INVENTURSTAND) {
			createInventurstand(getInternalFrameArtikel().getInventurDto()
					.getIId());
			panelQueryInventurstand.eventYouAreSelected(false);
			panelQueryInventurstand.updateButtons();
		}
		refreshTitle();
	}

	protected void lPActionEvent(java.awt.event.ActionEvent e) throws Throwable {
		if (e.getActionCommand().equals(MENUE_INVENTUR_ACTION_ZAEHLLISTE)) {
			String add2Title = LPMain.getInstance().getTextRespectUISPr(
					"artikel.report.zaehlliste");
			getInternalFrame().showReportKriterien(
					new ReportZaehlliste(getInternalFrameArtikel(), add2Title));
		} else if (e.getActionCommand().equals(
				MENUE_INVENTUR_ACTION_INVENTURSTAND)) {
			String add2Title = LPMain.getInstance().getTextRespectUISPr(
					"artikel.report.inventurstand");
			getInternalFrame().showReportKriterien(
					new ReportInventurstand(getInternalFrameArtikel(),
							add2Title, (Integer) panelQueryInventur
									.getSelectedId()));
		} else if (e.getActionCommand().equals(
				MENUE_INVENTUR_ACTION_INVENTURLISTE)) {
			String add2Title = LPMain.getInstance().getTextRespectUISPr(
					"artikel.report.inventurliste");
			getInternalFrame().showReportKriterien(
					new ReportInventurliste(getInternalFrameArtikel(),
							add2Title, (Integer) panelQueryInventur
									.getSelectedId()));
		} else if (e.getActionCommand().equals(
				MENUE_INVENTUR_ACTION_INVENTURPROTOKOLL)) {
			String add2Title = LPMain.getInstance().getTextRespectUISPr(
					"artikel.inventurprotokoll");
			getInternalFrame().showReportKriterien(
					new ReportInventurprotokoll(getInternalFrameArtikel(),
							add2Title, (Integer) panelQueryInventur
									.getSelectedId()));
		} else if (e.getActionCommand()
				.equals(MENUE_INVENTUR_ACTION_DELTALISTE)) {
			String add2Title = LPMain.getInstance().getTextRespectUISPr(
					"artikel.inventurdeltaliste");
			getInternalFrame().showReportKriterien(
					new ReportNichterfassteartikel(getInternalFrameArtikel(),
							add2Title, (Integer) panelQueryInventur
									.getSelectedId()));
		} else if (e.getActionCommand().equals(
				MENUE_PFLEGE_DURCHFUEHRUNG_ZURUECKNEHMEN)) {
			if (panelQueryInventur.getSelectedId() != null) {
				DelegateFactory
						.getInstance()
						.getInventurDelegate()
						.inventurDurchfuehrungZuruecknehmen(
								(Integer) panelQueryInventur.getSelectedId());
				panelQueryInventur.eventYouAreSelected(false);
			}
		} else if (e.getActionCommand().equals(
				MENUE_PFLEGE_IVENTURPROTOKOLL_AB_STICHTAG_ZURUECKNEHMEN)) {
			if (panelQueryInventur.getSelectedId() != null) {

				java.sql.Date dDatum = DialogFactory
						.showDatumseingabe("Ab welchem Datum sollen die Protokolleintr\u00E4ge zur\u00FCckgenommen werden?");
				if (dDatum != null) {
					DelegateFactory
							.getInstance()
							.getInventurDelegate()
							.invturprotokollZumStichtagZuruecknehmen(
									(Integer) panelQueryInventur
											.getSelectedId(),
									dDatum);
					panelQueryInventur.eventYouAreSelected(false);
				}
			}
		}
	}

	public javax.swing.JMenuBar getJMenuBar() throws Throwable {
		if (wrapperManuBar == null) {
			wrapperManuBar = new WrapperMenuBar(this);

			JMenu menuInfo = new WrapperMenu("lp.info", this);

			JMenuItem menuItemZaehlliste = new JMenuItem(LPMain.getInstance()
					.getTextRespectUISPr("artikel.report.zaehlliste"));
			menuItemZaehlliste.addActionListener(this);
			menuItemZaehlliste
					.setActionCommand(MENUE_INVENTUR_ACTION_ZAEHLLISTE);

			JMenuItem menuItemInventurstand = new JMenuItem(LPMain
					.getInstance().getTextRespectUISPr("artikel.inventurstand"));
			menuItemInventurstand.addActionListener(this);
			menuItemInventurstand
					.setActionCommand(MENUE_INVENTUR_ACTION_INVENTURSTAND);

			JMenuItem menuItemInventurliste = new JMenuItem(LPMain
					.getInstance().getTextRespectUISPr("artikel.inventurliste"));
			menuItemInventurliste.addActionListener(this);
			menuItemInventurliste
					.setActionCommand(MENUE_INVENTUR_ACTION_INVENTURLISTE);

			JMenuItem menuItemInventurprotokoll = new JMenuItem(LPMain
					.getInstance().getTextRespectUISPr(
							"artikel.inventurprotokoll"));
			menuItemInventurprotokoll.addActionListener(this);
			menuItemInventurprotokoll
					.setActionCommand(MENUE_INVENTUR_ACTION_INVENTURPROTOKOLL);

			JMenuItem menuItemDeltaliste = new JMenuItem(LPMain.getInstance()
					.getTextRespectUISPr("artikel.inventurdeltaliste"));
			menuItemDeltaliste.addActionListener(this);
			menuItemDeltaliste
					.setActionCommand(MENUE_INVENTUR_ACTION_DELTALISTE);

			menuInfo.add(menuItemZaehlliste);
			menuInfo.add(menuItemInventurliste);
			menuInfo.add(menuItemInventurstand);
			menuInfo.add(menuItemInventurprotokoll);
			menuInfo.add(menuItemDeltaliste);
			wrapperManuBar.addJMenuItem(menuInfo);

			JMenu menuPflege = new WrapperMenu("lp.pflege", this);

			JMenuItem menuItemdurchfuehrungZurucknehmen = new JMenuItem(LPMain
					.getInstance().getTextRespectUISPr(
							"artikel.inventur.zuruecknehmen"));
			menuItemdurchfuehrungZurucknehmen.addActionListener(this);
			menuItemdurchfuehrungZurucknehmen
					.setActionCommand(MENUE_PFLEGE_DURCHFUEHRUNG_ZURUECKNEHMEN);
			menuPflege.add(menuItemdurchfuehrungZurucknehmen);

			JMenuItem menuItemProtokolleintraegeZurucknehmen = new JMenuItem(
					LPMain.getInstance().getTextRespectUISPr(
							"artikel.inventurprotokoll.zuruecknehmen"));
			menuItemProtokolleintraegeZurucknehmen.addActionListener(this);
			menuItemProtokolleintraegeZurucknehmen
					.setActionCommand(MENUE_PFLEGE_IVENTURPROTOKOLL_AB_STICHTAG_ZURUECKNEHMEN);
			menuPflege.add(menuItemProtokolleintraegeZurucknehmen);

			wrapperManuBar.addJMenuItem(menuPflege);
		}
		return wrapperManuBar;

	}

}
