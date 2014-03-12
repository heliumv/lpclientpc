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
package com.lp.client.angebotstkl;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.math.BigDecimal;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import javax.swing.event.ChangeEvent;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.ICopyPaste;
import com.lp.client.frame.LockStateValue;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQuery;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.PanelSplit;
import com.lp.client.frame.component.TabbedPane;
import com.lp.client.frame.component.WrapperMenuBar;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.stueckliste.StuecklisteFilterFactory;
import com.lp.client.system.SystemFilterFactory;
import com.lp.client.util.fastlanereader.gui.QueryType;
import com.lp.server.angebotstkl.service.AgstklpositionDto;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.LagerDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.service.BelegpositionDto;
import com.lp.service.POSDocument2POSDto;
import com.lp.util.Helper;

@SuppressWarnings("static-access")
public class TabbedPaneAngebotstkl extends TabbedPane implements ICopyPaste {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private PanelQuery panelQueryAgstkl = null;
	private PanelBasis panelDetailAgstkl = null;
	private PanelBasis panelDetailAgstklAufschlaege = null;

	private PanelQuery panelQueryAngStklPos = null;
	private PanelBasis panelDetailAngStklPos = null;
	private PanelSplit panelSplitAngStk = null; // FLR 1:n Liste

	private static int IDX_PANEL_AUSWAHL = 0;
	private static int IDX_PANEL_KOPFDATEN = 1;
	private static int IDX_PANEL_POSITIONEN = 2;
	private static int IDX_PANEL_AUFSCHLAEGE = 3;

	private PanelQueryFLR panelStueckliste = null;

	private PanelQueryFLR panelAgstkl = null;

	private static final String ACTION_SPECIAL_IMPORT = "action_special_import";
	private static final String ACTION_SPECIAL_IMPORT_STKL = "action_special_import_stkl";
	private static final String ACTION_SPECIAL_PREISE_NEU_RECHNEN_KALK2 = "action_special_preise_neu_rechnen_klak2";

	private final String BUTTON_IMPORTAGSTUECKLISTEPOSITIONEN = PanelBasis.ACTION_MY_OWN_NEW
			+ ACTION_SPECIAL_IMPORT;

	private final String BUTTON_IMPORTSTUECKLISTEPOSITIONEN = PanelBasis.ACTION_MY_OWN_NEW
			+ ACTION_SPECIAL_IMPORT_STKL;

	private final String BUTTON_PREISE_NEU_RECHNEN = PanelBasis.ACTION_MY_OWN_NEW
			+ ACTION_SPECIAL_PREISE_NEU_RECHNEN_KALK2;

	private final String MENU_DATEI_DRUCKEN = "MENU_DATEI_DRUCKEN";

	private WrapperMenuBar wrapperMenuBar = null;

	public TabbedPaneAngebotstkl(InternalFrame internalFrameI) throws Throwable {

		super(internalFrameI, LPMain.getInstance().getTextRespectUISPr(
				"lp.angebotsstueckliste"));

		jbInit();
		initComponents();
	}

	public PanelQuery getPanelQueryPersonal() {
		return panelQueryAgstkl;
	}

	public PanelBasis getAngebotstklPositionenBottom() {
		return panelDetailAngStklPos;
	}

	private void dialogAgstkl(ItemChangedEvent e) throws Throwable {
		panelAgstkl = AngebotstklFilterFactory.getInstance()
				.createPanelFLRAgstkl(getInternalFrame(), true, false);
		new DialogQuery(panelAgstkl);
	}

	public PanelQuery getAngebotstklPositionenTop() {
		return panelQueryAngStklPos;
	}

	private void refreshAuswahl() throws Throwable {
		if (panelQueryAgstkl == null) {

			String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW,
					PanelBasis.ACTION_FILTER };
			panelQueryAgstkl = new PanelQuery(AngebotstklFilterFactory
					.getInstance().createQTAgstkl(), SystemFilterFactory
					.getInstance().createFKMandantCNr(),
					QueryParameters.UC_ID_AGSTKL, aWhichButtonIUse,
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("auft.title.panel.auswahl"),
					true);

			panelQueryAgstkl
					.befuellePanelFilterkriterienDirektUndVersteckte(
							AngebotstklFilterFactory.getInstance()
									.createFKDAgstklbelegnumer(),
							AngebotstklFilterFactory.getInstance()
									.createFKDKundeName(), null);

			panelQueryAgstkl.addDirektFilter(AngebotstklFilterFactory
					.getInstance().createFKDAgstklprojekt());

			setComponentAt(IDX_PANEL_AUSWAHL, panelQueryAgstkl);
		}
	}

	private void createKopfdaten(Integer key) throws Throwable {
		if (panelDetailAgstkl == null) {
			panelDetailAgstkl = new PanelAngebotstklKopfdaten(
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("lp.kopfdaten"), key);
			setComponentAt(IDX_PANEL_KOPFDATEN, panelDetailAgstkl);
		}
	}

	private void createAufschlaege(Integer key) throws Throwable {
		if (panelDetailAgstklAufschlaege != null) {
			panelDetailAgstklAufschlaege = null;
		}
		panelDetailAgstklAufschlaege = new PanelAngebotstklAufschlaege(
				getInternalFrame(), LPMain.getInstance().getTextRespectUISPr(
						"as.aufschlag"), key);
		setComponentAt(IDX_PANEL_AUFSCHLAEGE, panelDetailAgstklAufschlaege);

	}

	private PanelBasis getAngebotstklKopfdaten() throws Throwable {
		Integer iIdAngebot = null;

		if (panelDetailAgstkl == null) {

			// Die Angebot hat einen Key vom Typ Integer
			if (getInternalFrame().getKeyWasForLockMe() != null) {
				iIdAngebot = new Integer(Integer.parseInt(getInternalFrame()
						.getKeyWasForLockMe()));
			}

			panelDetailAgstkl = new PanelAngebotstklKopfdaten(
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("angb.panel.kopfdaten"),
					iIdAngebot); // empty bei leerer angebotsliste

			setComponentAt(IDX_PANEL_KOPFDATEN, panelDetailAgstkl);
		}

		return panelDetailAgstkl;
	}

	public void printAngebotstkl() throws Throwable {
		if (!getAngebotstklKopfdaten().isLockedDlg()) {
			if (getInternalFrameAngebotstkl().getAgstklDto() != null) {
				// das Angebot drucken
				getInternalFrame().showReportKriterien(
						new ReportAngebotstkl(getInternalFrame(),
								getInternalFrameAngebotstkl().getAgstklDto()
										.getIId(),
								getInternalFrameAngebotstkl().getAgstklDto()
										.getCNr()));
			} else {
				// es ist keine Angbotsstueckliste ausgewaehlt
				DialogFactory.showModalDialog(LPMain.getInstance()
						.getTextRespectUISPr("lp.error"), LPMain.getInstance()
						.getTextRespectUISPr("as.warning.keineas"));
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

		if (getInternalFrameAngebotstkl().iKalkulationsart == 2
				|| getInternalFrameAngebotstkl().iKalkulationsart == 3) {
			insertTab(LPMain.getInstance().getTextRespectUISPr("as.aufschlag"),
					null, null,
					LPMain.getInstance().getTextRespectUISPr("as.aufschlag"),
					IDX_PANEL_AUFSCHLAEGE);
		}

		refreshAuswahl();

		panelQueryAgstkl.eventYouAreSelected(false);

		if ((Integer) panelQueryAgstkl.getSelectedId() != null) {
			getInternalFrameAngebotstkl()
					.setAgstklDto(
							DelegateFactory
									.getInstance()
									.getAngebotstklDelegate()
									.agstklFindByPrimaryKey(
											(Integer) panelQueryAgstkl
													.getSelectedId()));
		}

		if ((Integer) panelQueryAgstkl.getSelectedId() != null) {
			getInternalFrameAngebotstkl()
					.setAgstklDto(
							DelegateFactory
									.getInstance()
									.getAngebotstklDelegate()
									.agstklFindByPrimaryKey(
											(Integer) panelQueryAgstkl
													.getSelectedId()));
		}

		// damit D2 einen aktuellen hat.
		ItemChangedEvent it = new ItemChangedEvent(panelQueryAgstkl,
				ItemChangedEvent.ITEM_CHANGED);
		lPEventItemChanged(it);

		this.addChangeListener(this);
		this.getInternalFrame().addItemChangedListener(this);

	}

	public void setKeyWasForLockMe() {
		Object oKey = panelQueryAgstkl.getSelectedId();

		if (oKey != null) {
			getInternalFrame().setKeyWasForLockMe(oKey.toString());
		} else {
			getInternalFrame().setKeyWasForLockMe(null);
		}
	}

	private void refreshTitle() {

		getInternalFrame().setLpTitle(
				InternalFrame.TITLE_IDX_OHRWASCHLUNTEN,
				LPMain.getInstance().getTextRespectUISPr(
						"lp.angebotsstueckliste"));
		getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_OHRWASCHLOBEN,
				((PanelBasis) this.getSelectedComponent()).getAdd2Title());

		if (getInternalFrameAngebotstkl().getAgstklDto() != null) {
			String sBezeichnung = "";
			if (getInternalFrameAngebotstkl().getAgstklDto().getCBez() != null) {
				sBezeichnung = getInternalFrameAngebotstkl().getAgstklDto()
						.getCBez();
			}
			getInternalFrame().setLpTitle(
					InternalFrame.TITLE_IDX_AS_I_LIKE,
					getInternalFrameAngebotstkl().getAgstklDto().getCNr()
							+ ", " + sBezeichnung);
		}
	}

	public InternalFrameAngebotstkl getInternalFrameAngebotstkl() {
		return (InternalFrameAngebotstkl) getInternalFrame();
	}

	protected void lPActionEvent(ActionEvent e) throws Throwable {
		if (e.getActionCommand().equals(MENU_DATEI_DRUCKEN)) {
			printAngebotstkl();
		}

	}

	public void lPEventItemChanged(ItemChangedEvent eI) throws Throwable {

		if (eI.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			if (eI.getSource() == panelQueryAgstkl) {
				refreshAngebotstklPositionen();
				setSelectedComponent(panelSplitAngStk);
				// panelDetailAgstkl.eventYouAreSelected(false);
			} else if (eI.getSource() == panelAgstkl) {
				DelegateFactory
						.getInstance()
						.getAngebotstklpositionDelegate()
						.kopiereAgstklPositionen(
								(Integer) panelAgstkl.getSelectedId(),
								getInternalFrameAngebotstkl().getAgstklDto()
										.getIId());
				panelSplitAngStk.eventYouAreSelected(false);
			} else if (eI.getSource() == panelStueckliste) {
				DelegateFactory
						.getInstance()
						.getAngebotstklDelegate()
						.kopierePositionenAusStueckliste(
								(Integer) panelStueckliste.getSelectedId(),
								getInternalFrameAngebotstkl().getAgstklDto()
										.getIId());
				panelSplitAngStk.eventYouAreSelected(false);
			}

		} else if (eI.getID() == ItemChangedEvent.ACTION_MY_OWN_NEW) {
			String sAspectInfo = ((ISourceEvent) eI.getSource()).getAspect();
			if (sAspectInfo.endsWith(ACTION_SPECIAL_IMPORT)) {
				dialogAgstkl(eI);
			} else if (sAspectInfo.endsWith(ACTION_SPECIAL_IMPORT_STKL)) {
				dialogStueckliste(eI);
			} else if (sAspectInfo
					.endsWith(ACTION_SPECIAL_PREISE_NEU_RECHNEN_KALK2)) {
				DelegateFactory
						.getInstance()
						.getAngebotstklpositionDelegate()
						.preiseGemaessKalkulationsart2Updaten(
								getInternalFrameAngebotstkl().getAgstklDto()
										.getIId());
				panelQueryAngStklPos.eventYouAreSelected(false);
			} else if (sAspectInfo.equals(PanelBasis.ACTION_EINFUEGEN_LIKE_NEW)) {
				if (eI.getSource().equals(panelQueryAngStklPos)) {
					einfuegenHV();
				}
			}
		}

		else if (eI.getID() == ItemChangedEvent.ACTION_SAVE) {
			if (eI.getSource() == panelDetailAgstkl) {
				panelQueryAgstkl.clearDirektFilter();
				Object oKey = panelDetailAgstkl.getKeyWhenDetailPanel();

				panelQueryAgstkl.setSelectedId(oKey);
			} else if (eI.getSource() == panelDetailAngStklPos) {
				// den Key des Datensatzes merken
				Object oKey = panelDetailAngStklPos.getKeyWhenDetailPanel();

				// wenn der neue Datensatz wirklich angelegt wurde (Abbruch
				// moeglich durch Pruefung auf Unterpreisigkeit)
				if (!oKey.equals(LPMain.getLockMeForNew())) {
					// die Liste neu aufbauen
					panelQueryAngStklPos.eventYouAreSelected(false);

					// den Datensatz in der Liste selektieren
					panelQueryAngStklPos.setSelectedId(oKey);
				}

				// im Detail den selektierten anzeigen
				panelSplitAngStk.eventYouAreSelected(false);
			}
		}
		if (eI.getID() == ItemChangedEvent.ACTION_POSITION_VONNNACHNMINUS1) {
			if (eI.getSource() == panelQueryAngStklPos) {
				int iPos = panelQueryAngStklPos.getTable().getSelectedRow();

				// wenn die Position nicht die erste ist
				if (iPos > 0) {
					Integer iIdPosition = (Integer) panelQueryAngStklPos
							.getSelectedId();

					Integer iIdPositionMinus1 = (Integer) panelQueryAngStklPos
							.getTable().getValueAt(iPos - 1, 0);

					DelegateFactory
							.getInstance()
							.getAngebotstklpositionDelegate()
							.vertauscheAgstklpositionen(iIdPosition,
									iIdPositionMinus1);

					// die Liste neu anzeigen und den richtigen Datensatz
					// markieren
					panelQueryAngStklPos.setSelectedId(iIdPosition);
				}
			}
		} else if (eI.getID() == ItemChangedEvent.ACTION_POSITION_VONNNACHNPLUS1) {
			if (eI.getSource() == panelQueryAngStklPos) {
				int iPos = panelQueryAngStklPos.getTable().getSelectedRow();

				// wenn die Position nicht die letzte ist
				if (iPos < panelQueryAngStklPos.getTable().getRowCount() - 1) {
					Integer iIdPosition = (Integer) panelQueryAngStklPos
							.getSelectedId();

					Integer iIdPositionPlus1 = (Integer) panelQueryAngStklPos
							.getTable().getValueAt(iPos + 1, 0);

					DelegateFactory
							.getInstance()
							.getAngebotstklpositionDelegate()
							.vertauscheAgstklpositionen(iIdPosition,
									iIdPositionPlus1);

					// die Liste neu anzeigen und den richtigen Datensatz
					// markieren
					panelQueryAngStklPos.setSelectedId(iIdPosition);
				}
			}
		} else if (eI.getID() == ItemChangedEvent.ACTION_POSITION_VORPOSITIONEINFUEGEN) {
			if (eI.getSource() == panelQueryAngStklPos) {
				panelDetailAngStklPos.eventActionNew(eI, true, false);
				panelDetailAngStklPos.eventYouAreSelected(false); // Buttons
																	// schalten
			}
		}

		else if (eI.getID() == ItemChangedEvent.ITEM_CHANGED) {
			if (eI.getSource() == panelQueryAgstkl) {

				if (panelQueryAgstkl.getSelectedId() != null) {
					getInternalFrameAngebotstkl().setKeyWasForLockMe(
							panelQueryAgstkl.getSelectedId() + "");
					getInternalFrameAngebotstkl().setAgstklDto(
							DelegateFactory
									.getInstance()
									.getAngebotstklDelegate()
									.agstklFindByPrimaryKey(
											(Integer) panelQueryAgstkl
													.getSelectedId()));

					if (getInternalFrameAngebotstkl().getAgstklDto() != null) {

						String sBezeichnung = "";
						if (getInternalFrameAngebotstkl().getAgstklDto()
								.getCBez() != null) {
							sBezeichnung = getInternalFrameAngebotstkl()
									.getAgstklDto().getCBez();
						}

						getInternalFrame().setLpTitle(
								InternalFrame.TITLE_IDX_AS_I_LIKE,
								getInternalFrameAngebotstkl().getAgstklDto()
										.getCNr() + ", " + sBezeichnung);
					}
				} else {

					getInternalFrame().enableAllOberePanelsExceptMe(this,
							IDX_PANEL_AUSWAHL, (false));
					getInternalFrame().setLpTitle(
							InternalFrame.TITLE_IDX_AS_I_LIKE, "");

				}
			} else if (eI.getSource() == panelQueryAngStklPos) {
				Object key = ((ISourceEvent) eI.getSource()).getIdSelected();
				panelDetailAngStklPos.setKeyWhenDetailPanel(key);
				panelDetailAngStklPos.eventYouAreSelected(false);

				// im QP die Buttons in den Zustand nolocking/save setzen.
				panelQueryAngStklPos.updateButtons(panelDetailAngStklPos
						.getLockedstateDetailMainKey());
			}
		} else if (eI.getID() == ItemChangedEvent.ACTION_UPDATE) {
			// hier kommt man nach upd im D bei einem 1:n hin.
			if (eI.getSource() == panelDetailAngStklPos) {
				// im QP die Buttons in den Zustand neu setzen.
				panelQueryAngStklPos.updateButtons(new LockStateValue(
						PanelBasis.LOCK_FOR_NEW));
				;
			}
		} else if (eI.getID() == ItemChangedEvent.ACTION_GOTO_MY_DEFAULT_QP) {
			// aktiviere ein QP ...
			if (eI.getSource() == panelDetailAgstkl) {

				panelQueryAgstkl.eventYouAreSelected(false);
				getInternalFrameAngebotstkl().getAgstklDto().setIId(
						(Integer) panelQueryAgstkl.getSelectedId());
				panelDetailAgstkl.setKeyWhenDetailPanel(panelQueryAgstkl
						.getSelectedId());
				this.setSelectedComponent(panelQueryAgstkl);
				if (panelQueryAgstkl.getSelectedId() == null) {
					getInternalFrame().enableAllOberePanelsExceptMe(this,
							IDX_PANEL_AUSWAHL, false);
				}

			} else if (eI.getSource() == panelDetailAngStklPos) {
				// bei einem Neu im 1:n Panel wurde der KeyForLockMe
				// ueberschrieben
				setKeyWasForLockMe();
				if (panelDetailAngStklPos.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryAngStklPos
							.getId2SelectAfterDelete();
					panelQueryAngStklPos.setSelectedId(oNaechster);
				}
				panelSplitAngStk.eventYouAreSelected(false); // refresh auf das
																// gesamte 1:n
																// panel
			}
		} else if (eI.getID() == ItemChangedEvent.ACTION_YOU_ARE_SELECTED) {
			refreshTitle();
			super.lPEventItemChanged(eI);
		} else if (eI.getID() == ItemChangedEvent.ACTION_DISCARD) {
			if (eI.getSource() == panelDetailAngStklPos) {
				panelSplitAngStk.eventYouAreSelected(false); // das 1:n Panel
																// neu aufbauen
			}
		} else if (eI.getID() == ItemChangedEvent.ACTION_NEW) {
			if (eI.getSource() == panelQueryAgstkl) {
				createKopfdaten(null);
				// wenn es bisher keinen eintrag gibt, die restlichen
				// panels aktivieren
				if (panelQueryAgstkl.getSelectedId() == null) {
					getInternalFrame().enableAllOberePanelsExceptMe(this,
							IDX_PANEL_AUSWAHL, true);
				}

				panelDetailAgstkl.eventActionNew(eI, true, false);
				setSelectedComponent(panelDetailAgstkl);

			} else if (eI.getSource() == panelQueryAngStklPos) {
				panelDetailAngStklPos.eventActionNew(eI, true, false);
				panelDetailAngStklPos.eventYouAreSelected(false);
				setSelectedComponent(panelSplitAngStk); // ui
			}
		} else if (eI.getID() == ItemChangedEvent.ACTION_KOPIEREN) {
			if (eI.getSource().equals(panelQueryAngStklPos)) {
				copyHV();
			}
		}
	}

	/**
	 * einfuegenHV
	 * 
	 * @throws IOException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws Throwable
	 */
	public void einfuegenHV() throws IOException, ParserConfigurationException,
			SAXException, Throwable {

		Object o = LPMain.getInstance().getPasteBuffer()
				.readObjectFromPasteBuffer();

		if (o instanceof BelegpositionDto[]) {
			AgstklpositionDto[] agstklpositionDtos = DelegateFactory
					.getInstance()
					.getBelegpostionkonvertierungDelegate()
					.konvertiereNachAgstklpositionDto(
							(BelegpositionDto[]) o,
							getInternalFrameAngebotstkl().getAgstklDto()
									.getIId());

			if (agstklpositionDtos != null) {
				Integer iId = null;
				Boolean b = positionAmEndeEinfuegen();
				if (b != null) {
					for (int i = 0; i < agstklpositionDtos.length; i++) {
						AgstklpositionDto agstklpositionDto = agstklpositionDtos[i];
						try {
							agstklpositionDto.setIId(null);

							agstklpositionDto
									.setBelegIId(getInternalFrameAngebotstkl()
											.getAgstklDto().getIId());
							if (b == false) {

								// Zusatzbezeichnung
								if (agstklpositionDto.getArtikelIId() != null) {
									ArtikelDto artikelDto = DelegateFactory
											.getInstance()
											.getArtikelDelegate()
											.artikelFindByPrimaryKey(
													agstklpositionDto
															.getArtikelIId());
									if (artikelDto.getArtikelsprDto() != null) {
										agstklpositionDto
												.setCZusatzbez(artikelDto
														.getArtikelsprDto()
														.getCZbez());
									}
								}

								Integer iIdAktuellePosition = (Integer) panelQueryAngStklPos
										.getSelectedId();

								// erstepos: 0 die erste Position steht an
								// der Stelle 1
								Integer iSortAktuellePosition = new Integer(1);

								// erstepos: 1 die erste Position steht an
								// der Stelle 1
								if (iIdAktuellePosition != null) {
									iSortAktuellePosition = DelegateFactory
											.getInstance()
											.getAngebotstklpositionDelegate()
											.agstklpositionFindByPrimaryKey(
													iIdAktuellePosition)
											.getISort();

									// Die bestehenden Positionen muessen
									// Platz fuer die neue schaffen
									DelegateFactory
											.getInstance()
											.getAngebotstklpositionDelegate()
											.sortierungAnpassenBeiEinfuegenEinerPositionVorPosition(
													getInternalFrameAngebotstkl()
															.getAgstklDto()
															.getIId(),
													iSortAktuellePosition
															.intValue());

									// Die neue Position wird an frei gemachte
									// Position gesetzt
									agstklpositionDto
											.setISort(iSortAktuellePosition);
								} else {
									agstklpositionDto.setISort(null);
								}
								// wir legen eine neue position an

							} else {
								agstklpositionDto.setISort(null);
							}

							if (iId == null) {
								iId = DelegateFactory
										.getInstance()
										.getAngebotstklpositionDelegate()
										.createAgstklposition(agstklpositionDto);
							} else {
								DelegateFactory
										.getInstance()
										.getAngebotstklpositionDelegate()
										.createAgstklposition(agstklpositionDto);
							}

						} catch (Throwable t) {
							// nur loggen!
							myLogger.error(t.getMessage(), t);
						}
					}
				}
				// die Liste neu aufbauen
				panelQueryAngStklPos.eventYouAreSelected(false);
				// den Datensatz in der Liste selektieren
				panelQueryAngStklPos.setSelectedId(iId);
				// im Detail den selektierten anzeigen
				panelSplitAngStk.eventYouAreSelected(false);
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
			panelQueryAgstkl.eventYouAreSelected(false);
			panelQueryAgstkl.updateButtons();
			if (panelQueryAgstkl.getSelectedId() == null) {
				getInternalFrame().enableAllOberePanelsExceptMe(this,
						IDX_PANEL_AUSWAHL, false);
			}

		} else if (selectedIndex == IDX_PANEL_KOPFDATEN) {
			Integer key = null;
			if (getInternalFrameAngebotstkl().getAgstklDto() != null) {
				key = getInternalFrameAngebotstkl().getAgstklDto().getIId();
			}
			createKopfdaten(key);
			panelDetailAgstkl.eventYouAreSelected(false);
		} else if (selectedIndex == IDX_PANEL_AUFSCHLAEGE) {
			Integer key = null;
			if (getInternalFrameAngebotstkl().getAgstklDto() != null) {
				key = getInternalFrameAngebotstkl().getAgstklDto().getIId();
			}
			createAufschlaege(key);
			panelDetailAgstklAufschlaege.eventYouAreSelected(false);
		} else if (selectedIndex == IDX_PANEL_POSITIONEN) {
			refreshAngebotstklPositionen();

			Integer key = null;
			if (getInternalFrameAngebotstkl().getAgstklDto() != null) {
				key = getInternalFrameAngebotstkl().getAgstklDto().getIId();

				FilterKriterium[] defaultfk = AngebotstklFilterFactory
						.getInstance().createFKAgstkl(key);
				panelQueryAngStklPos.setDefaultFilter(defaultfk);
			}

			panelSplitAngStk.eventYouAreSelected(false);

			panelQueryAngStklPos.updateButtons(panelDetailAngStklPos
					.getLockedstateDetailMainKey());
		}
	}

	/**
	 * kopiere die selektierten positionen in den pastebuffer.
	 * 
	 * @throws ExceptionLP
	 * @throws Throwable
	 */
	public void copyHV() throws ExceptionLP, Throwable {

		Object aoIIdPosition[] = panelQueryAngStklPos.getSelectedIds();

		if (aoIIdPosition != null && (aoIIdPosition.length > 0)) {
			AgstklpositionDto[] dtos = new AgstklpositionDto[aoIIdPosition.length];
			Integer aIIdPosition[] = new Integer[aoIIdPosition.length];
			for (int i = 0; i < aIIdPosition.length; i++) {
				aIIdPosition[i] = (Integer) aoIIdPosition[i];
				dtos[i] = DelegateFactory
						.getInstance()
						.getAngebotstklpositionDelegate()
						.agstklpositionFindByPrimaryKey(
								(Integer) aoIIdPosition[i]);
			}
			LPMain.getInstance().getPasteBuffer()
					.writeObjectToPasteBuffer(dtos);
		}

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

	private void dialogStueckliste(ItemChangedEvent e) throws Throwable {
		panelStueckliste = StuecklisteFilterFactory.getInstance()
				.createPanelFLRStueckliste(getInternalFrame(), null, false);
		new DialogQuery(panelStueckliste);
	}

	private PanelSplit refreshAngebotstklPositionen() throws Throwable {
		if (panelSplitAngStk == null) {

			if (getInternalFrameAngebotstkl().iKalkulationsart == 2
					|| getInternalFrameAngebotstkl().iKalkulationsart == 3) {
				panelDetailAngStklPos = new PanelAgstklPositionenEinkauf(
						getInternalFrame(), LPMain.getInstance()
								.getTextRespectUISPr("angb.panel.positionen"),
						null); // eventuell
								// gibt
								// es
								// noch
								// keine
								// position

			} else {
				panelDetailAngStklPos = new PanelAngebotstklPositionen(
						getInternalFrame(), LPMain.getInstance()
								.getTextRespectUISPr("angb.panel.positionen"),
						null); // eventuell
								// gibt
								// es
								// noch
								// keine
								// position
			}

			QueryType[] qtPositionen = null;
			FilterKriterium[] filtersPositionen = AngebotstklFilterFactory
					.getInstance().createFKAgstkl(
							getInternalFrameAngebotstkl().getAgstklDto()
									.getIId());

			String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW,
					PanelBasis.ACTION_POSITION_VONNNACHNMINUS1,
					PanelBasis.ACTION_POSITION_VONNNACHNPLUS1,
					PanelBasis.ACTION_POSITION_VORPOSITIONEINFUEGEN,
					PanelBasis.ACTION_KOPIEREN,
					PanelBasis.ACTION_EINFUEGEN_LIKE_NEW, };

			panelQueryAngStklPos = new PanelQuery(qtPositionen,
					filtersPositionen, QueryParameters.UC_ID_AGSTKLPOSITION,
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

			panelQueryAngStklPos.createAndSaveAndShowButton(
					"/com/lp/client/res/text_code_colored16x16.png",
					LPMain.getInstance().getTextRespectUISPr(
							"stkl.positionen.importausandererstueckliste"),
					BUTTON_IMPORTSTUECKLISTEPOSITIONEN, null);

			panelQueryAngStklPos.createAndSaveAndShowButton(
					"/com/lp/client/res/calculator16x16.png",
					LPMain.getInstance().getTextRespectUISPr(
							"agstkl.preiseneurechnen.kalk2"),
					BUTTON_PREISE_NEU_RECHNEN, null);

			panelQueryAngStklPos.createAndSaveAndShowButton(
					"/com/lp/client/res/note_add16x16.png",
					LPMain.getInstance().getTextRespectUISPr(
							"as.positionen.importausandererstueckliste"),
					BUTTON_IMPORTAGSTUECKLISTEPOSITIONEN, null);

			panelQueryAngStklPos.setMultipleRowSelectionEnabled(true);

			panelSplitAngStk = new PanelSplit(getInternalFrame(),
					panelDetailAngStklPos, panelQueryAngStklPos, 180);

			setComponentAt(IDX_PANEL_POSITIONEN, panelSplitAngStk);
		}

		return panelSplitAngStk;
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
