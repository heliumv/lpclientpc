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
package com.lp.client.instandhaltung;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.Map;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.event.ChangeEvent;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.LockStateValue;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQuery;
import com.lp.client.frame.component.PanelSplit;
import com.lp.client.frame.component.TabbedPane;
import com.lp.client.frame.component.WrapperComboBox;
import com.lp.client.frame.component.WrapperMenuBar;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.stueckliste.StuecklisteFilterFactory;
import com.lp.server.instandhaltung.service.GeraetDto;
import com.lp.server.instandhaltung.service.InstandhaltungDto;
import com.lp.server.instandhaltung.service.StandortDto;
import com.lp.server.instandhaltung.service.WartungslisteDto;
import com.lp.server.instandhaltung.service.WartungsschritteDto;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.service.BelegpositionDto;

@SuppressWarnings("static-access")
/*
 * <p>&UUml;berschrift: </p> <p>Beschreibung: </p> <p>Copyright: Copyright (c)
 * 2004</p> <p>Organisation: </p>
 * 
 * @author Christian Kollmann
 * 
 * @version $Revision: 1.28 $
 */
public class TabbedPaneInstandhaltung extends TabbedPane {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private PanelQuery panelQueryInstandhaltung = null;
	private PanelBasis panelSplitInstandhaltung = null;
	private PanelInstandhaltung panelBottomInstandhaltung = null;

	private PanelQuery panelQueryStandort = null;
	private PanelBasis panelSplitStandort = null;
	private PanelStandort panelBottomStandort = null;

	private PanelQuery panelQueryStandorttechniker = null;
	private PanelBasis panelSplitStandorttechniker = null;
	private PanelBasis panelBottomStandorttechniker = null;

	private PanelQuery panelQueryGeraet = null;
	private PanelBasis panelSplitGeraet = null;
	private PanelGeraet panelBottomGeraet = null;

	private PanelQuery panelQueryWartungsliste = null;
	private PanelBasis panelSplitWartungsliste = null;
	private PanelWartungsliste panelBottomWartungsliste = null;

	private PanelQuery panelQueryWartungsschritte = null;
	private PanelBasis panelSplitWartungsschritte = null;
	private PanelWartungsschritte panelBottomWartungsschritte = null;

	private PanelQuery panelQueryHistorie = null;
	private PanelBasis panelSplitHistorie = null;
	private PanelGeraetehistorie panelBottomHistorie = null;

	private InstandhaltungDto instandhaltungDto = null;

	private WrapperComboBox wcbKategorie = new WrapperComboBox();

	private final String MENUE_JOURNAL_ACTION_WARTUNGSPLAN = "MENUE_JOURNAL_ACTION_WARTUNGSPLAN";

	private final String MENUE_BEARBEITEN_WARTUNGEN_ERFASSEN = "MENUE_BEARBEITEN_WARTUNGEN_ERFASSEN";

	public InstandhaltungDto getInstandhaltungDto() {
		return instandhaltungDto;
	}

	public void setInstandhaltungDto(InstandhaltungDto instandhaltungDto) {
		this.instandhaltungDto = instandhaltungDto;
	}

	private StandortDto standortDto = null;

	public StandortDto getStandortDto() {
		return standortDto;
	}

	public void setStandortDto(StandortDto standortDto) {
		this.standortDto = standortDto;
	}

	private GeraetDto geraetDto = null;

	public GeraetDto getGeraetDto() {
		return geraetDto;
	}

	public void setGeraetDto(GeraetDto geraetDto) {
		this.geraetDto = geraetDto;
	}

	JRadioButtonMenuItem[] menuItemsAnsicht = null;

	private static int IDX_PANEL_INSTANDHALTUNG = 0;
	private static int IDX_PANEL_STANDORT = 1;
	private static int IDX_PANEL_GERAET = 2;
	private static int IDX_PANEL_WARTUNGSSCHRITTE = 3;
	private static int IDX_PANEL_WARTUNGSLISTE = 4;
	private static int IDX_PANEL_HISTORIE = 5;
	private static int IDX_PANEL_STANDORTTECHNIKER = 6;

	private WrapperMenuBar wrapperMenuBar = null;

	public TabbedPaneInstandhaltung(InternalFrame internalFrameI)
			throws Throwable {
		super(internalFrameI, LPMain.getInstance().getTextRespectUISPr(
				"is.instandhaltung"));

		jbInit();
		initComponents();
	}

	public PanelQuery getPanelQuerySpeiseplan() {
		return panelQueryInstandhaltung;
	}

	public PanelQuery getPanelQueryWartungsliste() {
		return panelQueryWartungsliste;
	}

	public void copyHV() throws ExceptionLP, Throwable {
		int selectedPanelIndex = this.getSelectedIndex();

		if (selectedPanelIndex == IDX_PANEL_WARTUNGSSCHRITTE) {
			Object aoIIdPosition[] = panelQueryWartungsschritte
					.getSelectedIds();

			if (aoIIdPosition != null && (aoIIdPosition.length > 0)) {
				WartungsschritteDto[] dtos = new WartungsschritteDto[aoIIdPosition.length];
				Integer aIIdPosition[] = new Integer[aoIIdPosition.length];
				for (int i = 0; i < aIIdPosition.length; i++) {
					aIIdPosition[i] = (Integer) aoIIdPosition[i];
					dtos[i] = DelegateFactory
							.getInstance()
							.getInstandhaltungDelegate()
							.wartungsschritteFindByPrimaryKey(
									(Integer) aoIIdPosition[i]);
				}
				LPMain.getInstance().getPasteBuffer()
						.writeObjectToPasteBuffer(dtos);
			}
		} else if (selectedPanelIndex == IDX_PANEL_WARTUNGSLISTE) {
			Object aoIIdPosition[] = panelQueryWartungsliste.getSelectedIds();

			if (aoIIdPosition != null && (aoIIdPosition.length > 0)) {
				WartungslisteDto[] dtos = new WartungslisteDto[aoIIdPosition.length];
				Integer aIIdPosition[] = new Integer[aoIIdPosition.length];
				for (int i = 0; i < aIIdPosition.length; i++) {
					aIIdPosition[i] = (Integer) aoIIdPosition[i];
					dtos[i] = DelegateFactory
							.getInstance()
							.getInstandhaltungDelegate()
							.wartungslisteFindByPrimaryKey(
									(Integer) aoIIdPosition[i]);
				}
				LPMain.getInstance().getPasteBuffer()
						.writeObjectToPasteBuffer(dtos);
			}
		}
	}

	public void einfuegenHV() throws IOException, ParserConfigurationException,
			SAXException, Throwable {

		Object o = LPMain.getInstance().getPasteBuffer()
				.readObjectFromPasteBuffer();

		int selectedPanelIndex = this.getSelectedIndex();
		if (o instanceof BelegpositionDto[]) {
			if (selectedPanelIndex == IDX_PANEL_WARTUNGSLISTE) {
				WartungslisteDto[] positionDtos = DelegateFactory
						.getInstance()
						.getBelegpostionkonvertierungDelegate()
						.konvertiereNachWartungslisteDto((BelegpositionDto[]) o);

				if (positionDtos != null) {
					Integer iId = null;
					Boolean b = positionAmEndeEinfuegen();
					if (b != null) {

						for (int i = 0; i < positionDtos.length; i++) {
							WartungslisteDto positionDto = positionDtos[i];
							try {
								positionDto.setIId(null);
								positionDto.setGeraetIId(getGeraetDto()
										.getIId());

								if (b == false) {
									Integer iIdAktuellePosition = (Integer) panelQueryWartungsliste
											.getSelectedId();

									// erstepos: 0 die erste Position steht an
									// der Stelle 1
									Integer iSortAktuellePosition = new Integer(
											1);

									// erstepos: 1 die erste Position steht an
									// der Stelle 1
									if (iIdAktuellePosition != null) {
										iSortAktuellePosition = DelegateFactory
												.getInstance()
												.getInstandhaltungDelegate()
												.wartungslisteFindByPrimaryKey(
														iIdAktuellePosition)
												.getISort();

										// Die bestehenden Positionen muessen
										// Platz fuer die neue schaffen
										DelegateFactory
												.getInstance()
												.getInstandhaltungDelegate()
												.sortierungWartungslisteAnpassenBeiEinfuegenEinerPositionVorPosition(
														getGeraetDto().getIId(),
														iSortAktuellePosition
																.intValue());
									}
									// Die neue Position wird an frei gemachte
									// Position gesetzt
									positionDto.setISort(iSortAktuellePosition);
								} else {
									positionDto.setISort(null);
								}

								// wir legen eine neue position an
								if (iId == null) {
									iId = DelegateFactory.getInstance()
											.getInstandhaltungDelegate()
											.createWartungsliste(positionDto);
								} else {
									DelegateFactory.getInstance()
											.getInstandhaltungDelegate()
											.createWartungsliste(positionDto);
								}
							} catch (Throwable t) {
								// nur loggen!
								myLogger.error(t.getMessage(), t);
							}
						}
					}

					// die Liste neu aufbauen
					panelQueryWartungsliste.eventYouAreSelected(false);
					// den Datensatz in der Liste selektieren
					panelQueryWartungsliste.setSelectedId(iId);

					// im Detail den selektierten anzeigen
					panelSplitWartungsliste.eventYouAreSelected(false);
				}

			} else if (selectedPanelIndex == IDX_PANEL_WARTUNGSSCHRITTE) {
				WartungsschritteDto[] positionDtos = DelegateFactory
						.getInstance()
						.getBelegpostionkonvertierungDelegate()
						.konvertiereNachWartungsschritteDto(
								(BelegpositionDto[]) o);

				if (positionDtos != null) {
					Integer iId = null;
					Boolean b = positionAmEndeEinfuegen();
					if (b != null) {

						for (int i = 0; i < positionDtos.length; i++) {
							WartungsschritteDto positionDto = positionDtos[i];
							try {
								positionDto.setIId(null);
								positionDto.setGeraetIId(getGeraetDto()
										.getIId());

								if (b == false) {
									Integer iIdAktuellePosition = (Integer) panelQueryWartungsschritte
											.getSelectedId();

									// erstepos: 0 die erste Position steht an
									// der Stelle 1
									Integer iSortAktuellePosition = new Integer(
											1);

									// erstepos: 1 die erste Position steht an
									// der Stelle 1
									if (iIdAktuellePosition != null) {
										iSortAktuellePosition = DelegateFactory
												.getInstance()
												.getInstandhaltungDelegate()
												.wartungsschritteFindByPrimaryKey(
														iIdAktuellePosition)
												.getISort();

										// Die bestehenden Positionen muessen
										// Platz fuer die neue schaffen
										DelegateFactory
												.getInstance()
												.getInstandhaltungDelegate()
												.sortierungWartungsschritteAnpassenBeiEinfuegenEinerPositionVorPosition(
														getGeraetDto().getIId(),
														iSortAktuellePosition
																.intValue());
									}
									// Die neue Position wird an frei gemachte
									// Position gesetzt
									positionDto.setISort(iSortAktuellePosition);
								} else {
									positionDto.setISort(null);
								}
								if (iId == null) {
									iId = DelegateFactory
											.getInstance()
											.getInstandhaltungDelegate()
											.createWartungsschritte(positionDto);
								} else {
									DelegateFactory
											.getInstance()
											.getInstandhaltungDelegate()
											.createWartungsschritte(positionDto);
								}
								// wir legen eine neue position an
							} catch (Throwable t) {
								// nur loggen!
								myLogger.error(t.getMessage(), t);
							}
						}
					}

					// die Liste neu aufbauen
					panelQueryWartungsschritte.eventYouAreSelected(false);
					// den Datensatz in der Liste selektieren
					panelQueryWartungsschritte.setSelectedId(iId);

					// im Detail den selektierten anzeigen
					panelSplitWartungsschritte.eventYouAreSelected(false);
				}

			}
		}
	}

	private void createInstandhaltung() throws Throwable {
		if (panelSplitInstandhaltung == null) {
			String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW };
			panelQueryInstandhaltung = new PanelQuery(null,
					InstandhaltungFilterFactory.getInstance()
							.createFKInstandhaltungMandant(),
					QueryParameters.UC_ID_INSTANDHALTUNG, aWhichButtonIUse,
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("is.instandhaltung"), true);

			panelBottomInstandhaltung = new PanelInstandhaltung(
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("is.instandhaltung"),
					panelQueryInstandhaltung.getSelectedId());
			panelSplitInstandhaltung = new PanelSplit(getInternalFrame(),
					panelBottomInstandhaltung, panelQueryInstandhaltung, 350);

			panelQueryInstandhaltung
					.befuellePanelFilterkriterienDirektUndVersteckte(
							InstandhaltungFilterFactory.getInstance()
									.createFKDInstandhaltungName(
											LPMain.getInstance()
													.getTextRespectUISPr(
															"lp.name")),
							InstandhaltungFilterFactory.getInstance()
									.createFKDNameStrasseStandort(),
							InstandhaltungFilterFactory.getInstance()
									.createFKVInstandhaltung());
			panelQueryInstandhaltung
					.addDirektFilter(InstandhaltungFilterFactory.getInstance()
							.createFKDStandortOrt());

			wcbKategorie.setMandatoryField(true);
			Map<?, ?> m = DelegateFactory.getInstance().getInstandhaltungDelegate()
					.getAllKategorieren();
			wcbKategorie.setMap(m);
			wcbKategorie.addActionListener(this);
			wcbKategorie.setPreferredSize(new Dimension(150, 21));

			panelQueryInstandhaltung.getToolBar().getToolsPanelCenter().add(
					wcbKategorie);

			setComponentAt(IDX_PANEL_INSTANDHALTUNG, panelSplitInstandhaltung);
		}
	}

	private void createStandort(Integer instandhaltungIId) throws Throwable {
		if (panelSplitStandort == null) {
			String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW };
			panelQueryStandort = new PanelQuery(null,
					InstandhaltungFilterFactory.getInstance()
							.createFKStandortInstandhaltung(instandhaltungIId),
					QueryParameters.UC_ID_STANDORT, aWhichButtonIUse,
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("is.standort"), true);

			panelBottomStandort = new PanelStandort(getInternalFrame(), LPMain
					.getInstance().getTextRespectUISPr("is.standort"),
					panelQueryStandort.getSelectedId());
			panelSplitStandort = new PanelSplit(getInternalFrame(),
					panelBottomStandort, panelQueryStandort, 200);

			panelQueryStandort.befuellePanelFilterkriterienDirektUndVersteckte(
					InstandhaltungFilterFactory.getInstance()
							.createFKDStandortName(
									LPMain.getInstance().getTextRespectUISPr(
											"is.standort")),
					InstandhaltungFilterFactory.getInstance()
							.createFKDStandortPartnerOrt(),
					InstandhaltungFilterFactory.getInstance()
							.createFKVStandort());

			panelQueryStandort.addDirektFilter(InstandhaltungFilterFactory
					.getInstance().createFKDStandortPartnerStrasse());

			setComponentAt(IDX_PANEL_STANDORT, panelSplitStandort);
		} else {
			// filter refreshen.
			panelQueryStandort.setDefaultFilter(InstandhaltungFilterFactory
					.getInstance().createFKStandortInstandhaltung(
							instandhaltungIId));
		}
	}

	private void createHistorie(Integer geraetIId) throws Throwable {
		if (panelSplitHistorie == null) {
			String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW };
			panelQueryHistorie = new PanelQuery(null,
					InstandhaltungFilterFactory.getInstance()
							.createFKGeraetehistorie(geraetIId),
					QueryParameters.UC_ID_GERAETEHISTORIE, aWhichButtonIUse,
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("is.geraetehistorie"), true);

			panelBottomHistorie = new PanelGeraetehistorie(getInternalFrame(),
					LPMain.getInstance().getTextRespectUISPr(
							"is.geraetehistorie"),
					panelQueryHistorie.getSelectedId());
			panelSplitHistorie = new PanelSplit(getInternalFrame(),
					panelBottomHistorie, panelQueryHistorie, 350);

			setComponentAt(IDX_PANEL_HISTORIE, panelSplitHistorie);
		} else {
			// filter refreshen.
			panelQueryHistorie.setDefaultFilter(InstandhaltungFilterFactory
					.getInstance().createFKGeraetehistorie(geraetIId));
		}
	}
	private void createStandorttechniker(Integer standortIId) throws Throwable {
		if (panelSplitStandorttechniker == null) {
			String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW };
			panelQueryStandorttechniker = new PanelQuery(null,
					InstandhaltungFilterFactory.getInstance()
							.createFKStandorttechnikerStandort(standortIId),
					QueryParameters.UC_ID_STANDORTTECHNIKER, aWhichButtonIUse,
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("is.standorttechniker"), true);

			panelBottomStandorttechniker = new PanelStandorttechniker(getInternalFrame(),
					LPMain.getInstance().getTextRespectUISPr(
							"is.standorttechniker"),
					panelQueryStandorttechniker.getSelectedId());
			panelSplitStandorttechniker = new PanelSplit(getInternalFrame(),
					panelBottomStandorttechniker, panelQueryStandorttechniker, 300);

			setComponentAt(IDX_PANEL_STANDORTTECHNIKER, panelSplitStandorttechniker);
		} else {
			// filter refreshen.
			panelQueryStandorttechniker.setDefaultFilter(InstandhaltungFilterFactory
					.getInstance().createFKStandorttechnikerStandort(standortIId));
		}
	}

	private void createGeraet(Integer standortIId) throws Throwable {
		if (panelSplitGeraet == null) {
			String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW };
			panelQueryGeraet = new PanelQuery(null, InstandhaltungFilterFactory
					.getInstance().createFKGeraetStandort(standortIId),
					QueryParameters.UC_ID_GERAET, aWhichButtonIUse,
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("is.geraet"), true);

			panelBottomGeraet = new PanelGeraet(getInternalFrame(), LPMain
					.getInstance().getTextRespectUISPr("is.geraet"),
					panelQueryGeraet.getSelectedId());
			panelSplitGeraet = new PanelSplit(getInternalFrame(),
					panelBottomGeraet, panelQueryGeraet, 190);

			panelQueryGeraet
					.befuellePanelFilterkriterienDirektUndVersteckte(
							StuecklisteFilterFactory.getInstance()
									.createFKDBezeichnungAllgemein(),
							InstandhaltungFilterFactory.getInstance()
									.createFKDGeraetHalle(),
							InstandhaltungFilterFactory.getInstance()
									.createFKVGeraet());
			panelQueryGeraet.addDirektFilter(InstandhaltungFilterFactory
					.getInstance().createFKDGeraetAnlage());
			panelQueryGeraet.addDirektFilter(InstandhaltungFilterFactory
					.getInstance().createFKDGeraetMaschine());

			setComponentAt(IDX_PANEL_GERAET, panelSplitGeraet);
		} else {
			// filter refreshen.
			panelQueryGeraet.setDefaultFilter(InstandhaltungFilterFactory
					.getInstance().createFKGeraetStandort(standortIId));
		}
	}

	private void createWartgunsliste(Integer geraetIId) throws Throwable {
		if (panelSplitWartungsliste == null) {
			String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW,
					PanelBasis.ACTION_POSITION_VONNNACHNMINUS1,
					PanelBasis.ACTION_POSITION_VONNNACHNPLUS1,
					PanelBasis.ACTION_POSITION_VORPOSITIONEINFUEGEN,
					PanelBasis.ACTION_KOPIEREN,
					PanelBasis.ACTION_EINFUEGEN_LIKE_NEW };
			panelQueryWartungsliste = new PanelQuery(null,
					InstandhaltungFilterFactory.getInstance()
							.createFKGeraetestuecklisteEinesGeraets(geraetIId),
					QueryParameters.UC_ID_WARTUNGSLISTE, aWhichButtonIUse,
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("is.wartungsliste"), true);

			panelBottomWartungsliste = new PanelWartungsliste(
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("is.wartungsliste"),
					panelQueryGeraet.getSelectedId());

			panelQueryWartungsliste.setMultipleRowSelectionEnabled(true);
			panelSplitWartungsliste = new PanelSplit(getInternalFrame(),
					panelBottomWartungsliste, panelQueryWartungsliste, 250);

			setComponentAt(IDX_PANEL_WARTUNGSLISTE, panelSplitWartungsliste);
		} else {
			// filter refreshen.
			panelQueryWartungsliste
					.setDefaultFilter(InstandhaltungFilterFactory.getInstance()
							.createFKGeraetestuecklisteEinesGeraets(geraetIId));
		}
	}

	private void createWartgunsschritte(Integer geraetIId) throws Throwable {
		if (panelSplitWartungsschritte == null) {
			String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW,
					PanelBasis.ACTION_POSITION_VONNNACHNMINUS1,
					PanelBasis.ACTION_POSITION_VONNNACHNPLUS1,
					PanelBasis.ACTION_POSITION_VORPOSITIONEINFUEGEN,
					PanelBasis.ACTION_KOPIEREN,
					PanelBasis.ACTION_EINFUEGEN_LIKE_NEW };
			panelQueryWartungsschritte = new PanelQuery(null,
					InstandhaltungFilterFactory.getInstance()
							.createFKGeraetestuecklisteEinesGeraets(geraetIId),
					QueryParameters.UC_ID_WARTUNGSSCHRITTE, aWhichButtonIUse,
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("is.wartungsschritte"), true);

			panelBottomWartungsschritte = new PanelWartungsschritte(
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("is.wartungsschritte"),
					panelQueryGeraet.getSelectedId());

			panelQueryWartungsschritte.setMultipleRowSelectionEnabled(true);
			panelSplitWartungsschritte = new PanelSplit(getInternalFrame(),
					panelBottomWartungsschritte, panelQueryWartungsschritte,
					230);

			setComponentAt(IDX_PANEL_WARTUNGSSCHRITTE,
					panelSplitWartungsschritte);
		} else {
			// filter refreshen.
			panelQueryWartungsschritte
					.setDefaultFilter(InstandhaltungFilterFactory.getInstance()
							.createFKGeraetestuecklisteEinesGeraets(geraetIId));
		}
	}

	private void jbInit() throws Throwable {
		// Kollektiv

		// 1 tab oben: QP1 PartnerFLR; lazy loading
		insertTab(LPMain.getInstance().getTextRespectUISPr("lp.auswahl"), null,
				null, LPMain.getInstance().getTextRespectUISPr("lp.auswahl"),
				IDX_PANEL_INSTANDHALTUNG);
		insertTab(LPMain.getInstance().getTextRespectUISPr("is.standort"),
				null, null,
				LPMain.getInstance().getTextRespectUISPr("is.standort"),
				IDX_PANEL_STANDORT);
		insertTab(LPMain.getInstance().getTextRespectUISPr("is.geraet"), null,
				null, LPMain.getInstance().getTextRespectUISPr("is.geraet"),
				IDX_PANEL_GERAET);

		insertTab(
				LPMain.getInstance().getTextRespectUISPr("is.wartungsschritte"),
				null,
				null,
				LPMain.getInstance().getTextRespectUISPr("is.wartungsschritte"),
				IDX_PANEL_WARTUNGSSCHRITTE);
		insertTab(LPMain.getInstance().getTextRespectUISPr("is.wartungsliste"),
				null, null,
				LPMain.getInstance().getTextRespectUISPr("is.wartungsliste"),
				IDX_PANEL_WARTUNGSLISTE);
		insertTab(LPMain.getInstance()
				.getTextRespectUISPr("is.geraetehistorie"), null, null, LPMain
				.getInstance().getTextRespectUISPr("is.geraetehistorie"),
				IDX_PANEL_HISTORIE);
		insertTab(LPMain.getInstance()
				.getTextRespectUISPr("is.standorttechniker"), null, null, LPMain
				.getInstance().getTextRespectUISPr("is.standorttechniker"),
				IDX_PANEL_STANDORTTECHNIKER);

		createInstandhaltung();

		// Itemevents an MEIN Detailpanel senden kann.
		refreshTitle();
		this.addChangeListener(this);
		getInternalFrame().addItemChangedListener(this);

	}

	public void lPEventItemChanged(ItemChangedEvent e) throws Throwable {

		if (e.getID() == ItemChangedEvent.ITEM_CHANGED) {
			if (e.getSource() == panelQueryInstandhaltung) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				if (key != null) {
					getInternalFrame().setKeyWasForLockMe(key.toString());
				} else {
					getInternalFrame().setKeyWasForLockMe(null);
				}

				panelBottomInstandhaltung.setKeyWhenDetailPanel(key);
				panelBottomInstandhaltung.eventYouAreSelected(false);
				panelQueryInstandhaltung.updateButtons();
				standortDto = null;
				geraetDto = null;
			} else if (e.getSource() == panelQueryStandort) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				if (key != null) {
					getInternalFrame().setKeyWasForLockMe(key.toString());
				} else {
					getInternalFrame().setKeyWasForLockMe(null);
				}

				panelBottomStandort.setKeyWhenDetailPanel(key);
				panelBottomStandort.eventYouAreSelected(false);
				panelQueryStandort.updateButtons();
				geraetDto = null;
			} else if (e.getSource() == panelQueryStandorttechniker) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				if (key != null) {
					getInternalFrame().setKeyWasForLockMe(key.toString());
				} else {
					getInternalFrame().setKeyWasForLockMe(null);
				}

				panelBottomStandorttechniker.setKeyWhenDetailPanel(key);
				panelBottomStandorttechniker.eventYouAreSelected(false);
				panelQueryStandorttechniker.updateButtons();
			} else if (e.getSource() == panelQueryGeraet) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				if (key != null) {
					getInternalFrame().setKeyWasForLockMe(key.toString());
				} else {
					getInternalFrame().setKeyWasForLockMe(null);
				}

				panelBottomGeraet.setKeyWhenDetailPanel(key);
				panelBottomGeraet.eventYouAreSelected(false);
				panelQueryGeraet.updateButtons();
			} else if (e.getSource() == panelQueryWartungsliste) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				if (key != null) {
					getInternalFrame().setKeyWasForLockMe(key.toString());
				} else {
					getInternalFrame().setKeyWasForLockMe(null);
				}

				panelBottomWartungsliste.setKeyWhenDetailPanel(key);
				panelBottomWartungsliste.eventYouAreSelected(false);
				panelQueryWartungsliste.updateButtons();
			} else if (e.getSource() == panelQueryWartungsschritte) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				if (key != null) {
					getInternalFrame().setKeyWasForLockMe(key.toString());
				} else {
					getInternalFrame().setKeyWasForLockMe(null);
				}

				panelBottomWartungsschritte.setKeyWhenDetailPanel(key);
				panelBottomWartungsschritte.eventYouAreSelected(false);
				panelQueryWartungsschritte.updateButtons();
			} else if (e.getSource() == panelQueryHistorie) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				if (key != null) {
					getInternalFrame().setKeyWasForLockMe(key.toString());
				} else {
					getInternalFrame().setKeyWasForLockMe(null);
				}

				panelBottomHistorie.setKeyWhenDetailPanel(key);
				panelBottomHistorie.eventYouAreSelected(false);
				panelQueryHistorie.updateButtons();
			}
			refreshTitle();
		} else if (e.getID() == ItemChangedEvent.ACTION_MY_OWN_NEW) {
			String sAspectInfo = ((ISourceEvent) e.getSource()).getAspect();
			if (sAspectInfo.equals(PanelBasis.ACTION_EINFUEGEN_LIKE_NEW)) {
				einfuegenHV();
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_YOU_ARE_SELECTED) {
			refreshTitle();
			if (e.getSource() == panelQueryInstandhaltung) {
				panelSplitInstandhaltung.eventYouAreSelected(false);
			} else if (e.getSource() == panelQueryStandort) {
				panelSplitStandort.eventYouAreSelected(false);
			} else if (e.getSource() == panelQueryStandorttechniker) {
				panelSplitStandorttechniker.eventYouAreSelected(false);
			} else if (e.getSource() == panelQueryGeraet) {
				panelSplitGeraet.eventYouAreSelected(false);
			} else if (e.getSource() == panelQueryWartungsliste) {
				panelSplitWartungsliste.eventYouAreSelected(false);
			} else if (e.getSource() == panelQueryWartungsschritte) {
				panelSplitWartungsschritte.eventYouAreSelected(false);
			} else if (e.getSource() == panelQueryHistorie) {
				panelSplitHistorie.eventYouAreSelected(false);
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_UPDATE) {
			if (e.getSource() == panelBottomInstandhaltung) {
				panelQueryInstandhaltung.updateButtons(new LockStateValue(
						PanelBasis.LOCK_FOR_NEW));
			} else if (e.getSource() == panelBottomStandort) {
				panelQueryStandort.updateButtons(new LockStateValue(
						PanelBasis.LOCK_FOR_NEW));
			} else if (e.getSource() == panelBottomStandorttechniker) {
				panelQueryStandorttechniker.updateButtons(new LockStateValue(
						PanelBasis.LOCK_FOR_NEW));
			} else if (e.getSource() == panelBottomGeraet) {
				panelQueryGeraet.updateButtons(new LockStateValue(
						PanelBasis.LOCK_FOR_NEW));
			} else if (e.getSource() == panelBottomWartungsliste) {
				panelQueryWartungsliste.updateButtons(new LockStateValue(
						PanelBasis.LOCK_FOR_NEW));
			} else if (e.getSource() == panelBottomWartungsschritte) {
				panelQueryWartungsschritte.updateButtons(new LockStateValue(
						PanelBasis.LOCK_FOR_NEW));
			} else if (e.getSource() == panelBottomHistorie) {
				panelQueryHistorie.updateButtons(new LockStateValue(
						PanelBasis.LOCK_FOR_NEW));
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_POSITION_VONNNACHNMINUS1) {
			if (e.getSource() == panelQueryWartungsliste) {
				int iPos = panelQueryWartungsliste.getTable().getSelectedRow();

				// wenn die Position nicht die erste ist
				if (iPos > 0) {
					Integer iIdPosition = (Integer) panelQueryWartungsliste
							.getSelectedId();

					Integer iIdPositionMinus1 = (Integer) panelQueryWartungsliste
							.getTable().getValueAt(iPos - 1, 0);

					DelegateFactory
							.getInstance()
							.getInstandhaltungDelegate()
							.vertauscheWartungsliste(iIdPosition,
									iIdPositionMinus1);

					// die Liste neu anzeigen und den richtigen Datensatz
					// markieren
					panelQueryWartungsliste.setSelectedId(iIdPosition);
				}
			} else if (e.getSource() == panelQueryWartungsschritte) {
				int iPos = panelQueryWartungsschritte.getTable()
						.getSelectedRow();

				// wenn die Position nicht die erste ist
				if (iPos > 0) {
					Integer iIdPosition = (Integer) panelQueryWartungsschritte
							.getSelectedId();

					Integer iIdPositionMinus1 = (Integer) panelQueryWartungsschritte
							.getTable().getValueAt(iPos - 1, 0);

					DelegateFactory
							.getInstance()
							.getInstandhaltungDelegate()
							.vertauscheWartungsschritte(iIdPosition,
									iIdPositionMinus1);

					// die Liste neu anzeigen und den richtigen Datensatz
					// markieren
					panelQueryWartungsschritte.setSelectedId(iIdPosition);
				}
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_POSITION_VONNNACHNPLUS1) {
			if (e.getSource() == panelQueryWartungsliste) {
				int iPos = panelQueryWartungsliste.getTable().getSelectedRow();

				// wenn die Position nicht die letzte ist
				if (iPos < panelQueryWartungsliste.getTable().getRowCount() - 1) {
					Integer iIdPosition = (Integer) panelQueryWartungsliste
							.getSelectedId();

					Integer iIdPositionPlus1 = (Integer) panelQueryWartungsliste
							.getTable().getValueAt(iPos + 1, 0);

					DelegateFactory
							.getInstance()
							.getInstandhaltungDelegate()
							.vertauscheWartungsliste(iIdPosition,
									iIdPositionPlus1);

					// die Liste neu anzeigen und den richtigen Datensatz
					// markieren
					panelQueryWartungsliste.setSelectedId(iIdPosition);
				}
			} else if (e.getSource() == panelQueryWartungsschritte) {
				int iPos = panelQueryWartungsschritte.getTable()
						.getSelectedRow();

				// wenn die Position nicht die letzte ist
				if (iPos < panelQueryWartungsschritte.getTable().getRowCount() - 1) {
					Integer iIdPosition = (Integer) panelQueryWartungsschritte
							.getSelectedId();

					Integer iIdPositionPlus1 = (Integer) panelQueryWartungsschritte
							.getTable().getValueAt(iPos + 1, 0);

					DelegateFactory
							.getInstance()
							.getInstandhaltungDelegate()
							.vertauscheWartungsschritte(iIdPosition,
									iIdPositionPlus1);

					// die Liste neu anzeigen und den richtigen Datensatz
					// markieren
					panelQueryWartungsschritte.setSelectedId(iIdPosition);
				}
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_POSITION_VORPOSITIONEINFUEGEN) {
			if (e.getSource() == panelQueryWartungsliste) {
				panelQueryWartungsliste.eventActionNew(e, true, false);
				panelQueryWartungsliste.eventYouAreSelected(false); // Buttons
				// schalten
			}
		}

		else if (e.getID() == ItemChangedEvent.ACTION_NEW) {
			if (e.getSource() == panelQueryInstandhaltung) {
				panelBottomInstandhaltung.eventActionNew(e, true, false);
				panelBottomInstandhaltung.eventYouAreSelected(false);
			} else if (e.getSource() == panelQueryStandort) {
				panelBottomStandort.eventActionNew(e, true, false);
				panelBottomStandort.eventYouAreSelected(false);
			} else if (e.getSource() == panelQueryStandorttechniker) {
				panelBottomStandorttechniker.eventActionNew(e, true, false);
				panelBottomStandorttechniker.eventYouAreSelected(false);
			} else if (e.getSource() == panelQueryGeraet) {
				panelBottomGeraet.eventActionNew(e, true, false);
				panelBottomGeraet.eventYouAreSelected(false);
			} else if (e.getSource() == panelQueryWartungsliste) {
				panelBottomWartungsliste.eventActionNew(e, true, false);
				panelBottomWartungsliste.eventYouAreSelected(false);
			} else if (e.getSource() == panelQueryWartungsschritte) {
				panelBottomWartungsschritte.eventActionNew(e, true, false);
				panelBottomWartungsschritte.eventYouAreSelected(false);
			} else if (e.getSource() == panelQueryHistorie) {
				panelBottomHistorie.eventActionNew(e, true, false);
				panelBottomHistorie.eventYouAreSelected(false);
			}

		} else if (e.getID() == ItemChangedEvent.ACTION_DISCARD) {
			if (e.getSource() == panelBottomInstandhaltung) {
				panelSplitInstandhaltung.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomStandort) {
				panelSplitStandort.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomStandorttechniker) {
				panelSplitStandorttechniker.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomGeraet) {
				panelSplitGeraet.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomWartungsliste) {
				panelSplitWartungsliste.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomWartungsschritte) {
				panelSplitWartungsschritte.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomHistorie) {
				panelSplitHistorie.eventYouAreSelected(false);
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_SAVE) {
			if (e.getSource() == panelBottomInstandhaltung) {
				Object oKey = panelBottomInstandhaltung.getKeyWhenDetailPanel();
				panelQueryInstandhaltung.eventYouAreSelected(false);
				panelQueryInstandhaltung.setSelectedId(oKey);
				panelSplitInstandhaltung.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomStandort) {
				Object oKey = panelBottomStandort.getKeyWhenDetailPanel();
				panelQueryStandort.eventYouAreSelected(false);
				panelQueryStandort.setSelectedId(oKey);
				panelSplitStandort.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomStandorttechniker) {
				Object oKey = panelBottomStandorttechniker.getKeyWhenDetailPanel();
				panelQueryStandorttechniker.eventYouAreSelected(false);
				panelQueryStandorttechniker.setSelectedId(oKey);
				panelSplitStandorttechniker.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomGeraet) {
				Object oKey = panelBottomGeraet.getKeyWhenDetailPanel();
				panelQueryGeraet.eventYouAreSelected(false);
				panelQueryGeraet.setSelectedId(oKey);
				panelSplitGeraet.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomWartungsliste) {
				Object oKey = panelBottomWartungsliste.getKeyWhenDetailPanel();
				panelQueryWartungsliste.eventYouAreSelected(false);
				panelQueryWartungsliste.setSelectedId(oKey);
				panelSplitWartungsliste.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomWartungsschritte) {
				Object oKey = panelBottomWartungsschritte
						.getKeyWhenDetailPanel();
				panelQueryWartungsschritte.eventYouAreSelected(false);
				panelQueryWartungsschritte.setSelectedId(oKey);
				panelSplitWartungsschritte.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomHistorie) {
				Object oKey = panelBottomHistorie.getKeyWhenDetailPanel();
				panelQueryHistorie.eventYouAreSelected(false);
				panelQueryHistorie.setSelectedId(oKey);
				panelSplitHistorie.eventYouAreSelected(false);
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_GOTO_MY_DEFAULT_QP) {
			if (e.getSource() == panelBottomInstandhaltung) {
				Object oKey = panelQueryInstandhaltung.getSelectedId();
				if (oKey != null) {
					getInternalFrame().setKeyWasForLockMe(oKey.toString());
				} else {
					getInternalFrame().setKeyWasForLockMe(null);
				}
				if (panelBottomInstandhaltung.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryInstandhaltung
							.getId2SelectAfterDelete();
					panelQueryInstandhaltung.setSelectedId(oNaechster);
				}
				panelSplitInstandhaltung.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomStandort) {
				Object oKey = panelQueryStandort.getSelectedId();
				if (oKey != null) {
					getInternalFrame().setKeyWasForLockMe(oKey.toString());
				} else {
					getInternalFrame().setKeyWasForLockMe(null);
				}
				if (panelBottomStandort.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryStandort
							.getId2SelectAfterDelete();
					panelQueryStandort.setSelectedId(oNaechster);
				}
				panelSplitStandort.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomStandorttechniker) {
				Object oKey = panelQueryStandorttechniker.getSelectedId();
				if (oKey != null) {
					getInternalFrame().setKeyWasForLockMe(oKey.toString());
				} else {
					getInternalFrame().setKeyWasForLockMe(null);
				}
				if (panelBottomStandorttechniker.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryStandorttechniker
							.getId2SelectAfterDelete();
					panelQueryStandorttechniker.setSelectedId(oNaechster);
				}
				panelSplitStandorttechniker.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomGeraet) {
				Object oKey = panelQueryGeraet.getSelectedId();
				if (oKey != null) {
					getInternalFrame().setKeyWasForLockMe(oKey.toString());
				} else {
					getInternalFrame().setKeyWasForLockMe(null);
				}
				if (panelBottomGeraet.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryGeraet
							.getId2SelectAfterDelete();
					panelQueryGeraet.setSelectedId(oNaechster);
				}
				panelSplitGeraet.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomWartungsliste) {
				Object oKey = panelQueryWartungsliste.getSelectedId();
				if (oKey != null) {
					getInternalFrame().setKeyWasForLockMe(oKey.toString());
				} else {
					getInternalFrame().setKeyWasForLockMe(null);
				}
				if (panelBottomWartungsliste.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryWartungsliste
							.getId2SelectAfterDelete();
					panelQueryWartungsliste.setSelectedId(oNaechster);
				}
				panelSplitWartungsliste.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomWartungsschritte) {
				Object oKey = panelQueryWartungsschritte.getSelectedId();
				if (oKey != null) {
					getInternalFrame().setKeyWasForLockMe(oKey.toString());
				} else {
					getInternalFrame().setKeyWasForLockMe(null);
				}
				if (panelBottomWartungsschritte.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryWartungsschritte
							.getId2SelectAfterDelete();
					panelQueryWartungsschritte.setSelectedId(oNaechster);
				}
				panelSplitWartungsschritte.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomHistorie) {
				Object oKey = panelQueryHistorie.getSelectedId();
				if (oKey != null) {
					getInternalFrame().setKeyWasForLockMe(oKey.toString());
				} else {
					getInternalFrame().setKeyWasForLockMe(null);
				}
				if (panelBottomHistorie.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryHistorie
							.getId2SelectAfterDelete();
					panelQueryHistorie.setSelectedId(oNaechster);
				}
				panelSplitHistorie.eventYouAreSelected(false);
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_KOPIEREN) {
			copyHV();
		}

	}

	private void refreshTitle() throws Throwable {

		getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_OHRWASCHLUNTEN,
				LPMain.getInstance().getTextRespectUISPr("is.instandhaltung"));
		// getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_OHRWASCHLOBEN,
		// ((PanelBasis) this.getSelectedComponent()).getAdd2Title());
		getInternalFrame().setLpTitle(3, "");

		String kunde = "";

		if (getInstandhaltungDto() != null
				&& getInstandhaltungDto().getKundeIId() != null) {

			KundeDto kundeDto = DelegateFactory
					.getInstance()
					.getKundeDelegate()
					.kundeFindByPrimaryKey(getInstandhaltungDto().getKundeIId());
			kunde = kundeDto.getPartnerDto().formatAnrede();
		}

		if (this.getSelectedIndex() >= IDX_PANEL_GERAET) {

			PartnerDto partnerDto = DelegateFactory.getInstance()
					.getPartnerDelegate()
					.partnerFindByPrimaryKey(getStandortDto().getPartnerIId());

			kunde += " | " + partnerDto.formatAnrede();
			if (this.getSelectedIndex() > IDX_PANEL_GERAET && this.getSelectedIndex()!=IDX_PANEL_STANDORTTECHNIKER) {

				kunde += " | " + getGeraetDto().getCBez();
			}

		}

		getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_AS_I_LIKE, kunde);
	}

	public PanelBasis getPanelDetailSpeiseplan() {
		return panelBottomInstandhaltung;
	}

	public void lPEventObjectChanged(ChangeEvent e) throws Throwable {
		super.lPEventObjectChanged(e);
		int selectedIndex = this.getSelectedIndex();

		if (selectedIndex == IDX_PANEL_INSTANDHALTUNG) {
			createInstandhaltung();
			panelSplitInstandhaltung.eventYouAreSelected(false);
			panelQueryInstandhaltung.updateButtons();
			if (panelQueryInstandhaltung.getSelectedId() == null) {
				getInternalFrame().enableAllOberePanelsExceptMe(this,
						IDX_PANEL_INSTANDHALTUNG, false);
			}
		} else if (selectedIndex == IDX_PANEL_STANDORT) {
			if (instandhaltungDto != null) {
				createStandort(instandhaltungDto.getIId());
				panelSplitStandort.eventYouAreSelected(false);
				panelQueryStandort.updateButtons();
			}
		} else if (selectedIndex == IDX_PANEL_GERAET) {
			if (standortDto != null) {
				createGeraet(standortDto.getIId());
				panelSplitGeraet.eventYouAreSelected(false);
				panelQueryGeraet.updateButtons();
			} else {
				DialogFactory.showModalDialog(LPMain.getInstance()
						.getTextRespectUISPr("lp.info"), LPMain.getInstance()
						.getTextRespectUISPr("is.zuerststandortwaehlen"));

				setSelectedIndex(IDX_PANEL_STANDORT);
				lPEventObjectChanged(e);
			}
		}else if (selectedIndex == IDX_PANEL_STANDORTTECHNIKER) {
			if (standortDto != null) {
				createStandorttechniker(standortDto.getIId());
				panelSplitStandorttechniker.eventYouAreSelected(false);
				panelQueryStandorttechniker.updateButtons();
			} else {
				DialogFactory.showModalDialog(LPMain.getInstance()
						.getTextRespectUISPr("lp.info"), LPMain.getInstance()
						.getTextRespectUISPr("is.zuerststandortwaehlen"));

				setSelectedIndex(IDX_PANEL_STANDORT);
				lPEventObjectChanged(e);
			}
		} else if (selectedIndex == IDX_PANEL_WARTUNGSLISTE) {
			if (geraetDto != null) {
				createWartgunsliste(geraetDto.getIId());
				panelSplitWartungsliste.eventYouAreSelected(false);
				panelQueryWartungsliste.updateButtons();
			} else {
				DialogFactory.showModalDialog(LPMain.getInstance()
						.getTextRespectUISPr("lp.info"), LPMain.getInstance()
						.getTextRespectUISPr("is.zuerstgeratwaehlen"));
				setSelectedIndex(IDX_PANEL_GERAET);
				lPEventObjectChanged(e);
			}
		} else if (selectedIndex == IDX_PANEL_WARTUNGSSCHRITTE) {
			if (geraetDto != null) {
				createWartgunsschritte(geraetDto.getIId());
				panelSplitWartungsschritte.eventYouAreSelected(false);
				panelQueryWartungsschritte.updateButtons();
			} else {
				DialogFactory.showModalDialog(LPMain.getInstance()
						.getTextRespectUISPr("lp.info"), LPMain.getInstance()
						.getTextRespectUISPr("is.zuerstgeratwaehlen"));
				setSelectedIndex(IDX_PANEL_GERAET);
				lPEventObjectChanged(e);
			}
		} else if (selectedIndex == IDX_PANEL_HISTORIE) {
			if (geraetDto != null) {
				createHistorie(geraetDto.getIId());
				panelSplitHistorie.eventYouAreSelected(false);
				panelQueryHistorie.updateButtons();
			} else {
				DialogFactory.showModalDialog(LPMain.getInstance()
						.getTextRespectUISPr("lp.info"), LPMain.getInstance()
						.getTextRespectUISPr("is.zuerstgeratwaehlen"));
				setSelectedIndex(IDX_PANEL_GERAET);
				lPEventObjectChanged(e);
			}
		}

		refreshTitle();
	}

	protected void lPActionEvent(ActionEvent e) throws Throwable {
		if (e.getActionCommand().equals(MENUE_JOURNAL_ACTION_WARTUNGSPLAN)) {
			String add2Title = LPMain.getInstance().getTextRespectUISPr(
					"is.journal.wartungsplan");
			getInternalFrame().showReportKriterien(
					new ReportWartungsplan((InternalFrameInstandhaltung)getInternalFrame(), add2Title));
		} else if (e.getActionCommand().equals(
				MENUE_BEARBEITEN_WARTUNGEN_ERFASSEN)) {
			DialogWartungenerfassen d = new DialogWartungenerfassen(this);
			LPMain.getInstance().getDesktop()
					.platziereDialogInDerMitteDesFensters(d);
			d.setVisible(true);
		} else if (e.getSource().equals(wcbKategorie)) {
			FilterKriterium[] kriterien = null;
			if (((Integer) wcbKategorie.getKeyOfSelectedItem()) < 0) {
				kriterien = new FilterKriterium[1];
				kriterien[0] = new FilterKriterium(
						"instandhaltung.flrkunde.mandant_c_nr", true, "'"
								+ LPMain.getTheClient().getMandant() + "'",
						FilterKriterium.OPERATOR_EQUAL, false);
			} else {
				kriterien = new FilterKriterium[2];
				kriterien[0] = new FilterKriterium(
						"instandhaltung.flrkunde.mandant_c_nr", true, "'"
								+ LPMain.getTheClient().getMandant() + "'",
						FilterKriterium.OPERATOR_EQUAL, false);
				kriterien[1] = new FilterKriterium(
						"instandhaltung.kategorie_i_id", true,
						wcbKategorie.getKeyOfSelectedItem() + "",
						FilterKriterium.OPERATOR_EQUAL, false);

			}
			panelQueryInstandhaltung.setDefaultFilter(kriterien);

			panelQueryInstandhaltung.eventYouAreSelected(false);

		}
	}

	public javax.swing.JMenuBar getJMenuBar() throws Throwable {
		if (wrapperMenuBar == null) {
			wrapperMenuBar = new WrapperMenuBar(this);
			JMenu journal = (JMenu) wrapperMenuBar
					.getComponent(WrapperMenuBar.MENU_JOURNAL);
			JMenuItem menuItemWartungsplan = new JMenuItem(LPMain.getInstance()
					.getTextRespectUISPr("is.journal.wartungsplan"));
			menuItemWartungsplan.addActionListener(this);
			menuItemWartungsplan
					.setActionCommand(MENUE_JOURNAL_ACTION_WARTUNGSPLAN);
			journal.add(menuItemWartungsplan);

			JMenu jmBearbeiten = (JMenu) wrapperMenuBar
					.getComponent(WrapperMenuBar.MENU_BEARBEITEN);
			JMenuItem menuItemWartungenerfassen = new JMenuItem(LPMain
					.getInstance().getTextRespectUISPr("is.wartungenerfassen"));
			menuItemWartungenerfassen.addActionListener(this);
			menuItemWartungenerfassen
					.setActionCommand(MENUE_BEARBEITEN_WARTUNGEN_ERFASSEN);
			jmBearbeiten.add(menuItemWartungenerfassen);

		}

		return wrapperMenuBar;
	}

}
