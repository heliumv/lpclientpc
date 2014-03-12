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
package com.lp.client.reklamation;

import java.awt.event.ActionEvent;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import javax.swing.event.ChangeEvent;

import com.lp.client.anfrage.AnfrageFilterFactory;
import com.lp.client.artikel.ArtikelFilterFactory;
import com.lp.client.artikel.PanelKatalogseite;
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
import com.lp.client.frame.component.WrapperMenuItem;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.personal.PanelPersonal;
import com.lp.client.system.SystemFilterFactory;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.reklamation.service.ReklamationDto;
import com.lp.server.reklamation.service.ReklamationFac;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MandantFac;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;

@SuppressWarnings("static-access")
public class TabbedPaneReklamation extends TabbedPane {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private PanelQuery panelQueryReklamation = null;
	private PanelBasis panelDetailReklamationErfassung = null;
	private PanelBasis panelDetailReklamationAnalyse = null;
	private PanelBasis panelDetailReklamationMassnahmen = null;

	private PanelQuery panelQueryBild = null;
	private PanelBasis panelDetailBild = null;
	private PanelBasis panelSplitBild = null;

	private PanelDialogReklamationKommentar pdReklamationKommentar = null;

	private static final String MENU_BEARBEITEN_KOMMENTAR = "MENU_BEARBEITEN_KOMMENTAR";

	private int IDX_PANEL_AUSWAHL = -1;
	private int IDX_PANEL_DETAIL = -1;
	private int IDX_PANEL_DATEN = -1;
	private int IDX_PANEL_MASSNAHMEN = -1;
	private int IDX_PANEL_BILDER = -1;

	private final String MENUE_JOURNAL_ACTION_REKLAMATIONSJOURNAL = "MENUE_JOURNAL_ACTION_REKLAMATIONSJOURNAL";
	private final String MENUE_JOURNAL_ACTION_LIEFERANTENTERMINTREUE = "MENUE_JOURNAL_ACTION_LIEFERANTENTERMINTREUE";
	private final String MENUE_JOURNAL_ACTION_LIEFERANTENBEURTEILUNG = "MENUE_JOURNAL_ACTION_LIEFERANTENBEURTEILUNG";
	private final String MENUE_JOURNAL_ACTION_FEHLERARTEN = "MENUE_JOURNAL_ACTION_FEHLERARTEN";
	private final String MENUE_JOURNAL_ACTION_MITARBEITERREKLAMATION = "MENUE_JOURNAL_ACTION_MITARBEITERREKLAMATION";
	private final String MENUE_JOURNAL_ACTION_MASCHINENREKLAMATION = "MENUE_JOURNAL_ACTION_MASCHINENREKLAMATION";

	private final static String MENU_BEARBEITEN_MANUELL_ERLEDIGEN = "MENU_BEARBEITEN_MANUELL_ERLEDIGEN";

	private final String MENU_ACTION_DATEI_DRUCKEN = "MENU_ACTION_DATEI_DRUCKEN";

	private PanelQueryFLR panelBeurteilung = null;

	private WrapperMenuBar wrapperMenuBar = null;

	public TabbedPaneReklamation(InternalFrame internalFrameI) throws Throwable {
		super(internalFrameI, LPMain.getInstance().getTextRespectUISPr(
				"rekla.modulname"));

		jbInit();
		initComponents();
	}

	public PanelQuery getPanelQueryReklamation() {
		return panelQueryReklamation;
	}

	private void createReklamationbild(Integer reklamationIId) throws Throwable {

		if (panelQueryBild == null) {
			String[] aWhichStandardButtonIUse = { PanelBasis.ACTION_NEW };

			FilterKriterium[] filters = ReklamationFilterFactory.getInstance()
					.createFKReklamation(reklamationIId);

			panelQueryBild = new PanelQuery(null, filters,
					QueryParameters.UC_ID_REKLAMATIONBILD,
					aWhichStandardButtonIUse, getInternalFrame(), LPMain
							.getInstance().getTextRespectUISPr("lp.bilder"),
					true);

			panelDetailBild = new PanelReklamationbild(
					getInternalFrameReklamation(), LPMain.getInstance()
							.getTextRespectUISPr("lp.bilder"), null);

			panelSplitBild = new PanelSplit(getInternalFrame(),
					panelDetailBild, panelQueryBild, 250);
			setComponentAt(IDX_PANEL_BILDER, panelSplitBild);
		} else {
			// filter refreshen.
			panelQueryBild.setDefaultFilter(ReklamationFilterFactory
					.getInstance().createFKReklamation(reklamationIId));
		}
	}

	private void createAuswahl() throws Throwable {
		if (panelQueryReklamation == null) {
			// MB 08.06.06 IMS 2173
			String[] aWhichButtonIUse = new String[] { PanelBasis.ACTION_NEW,
					PanelBasis.ACTION_FILTER };

			panelQueryReklamation = new PanelQuery(ReklamationFilterFactory
					.getInstance().createQTReklamationauswahl(),
					SystemFilterFactory.getInstance().createFKMandantCNr(),
					QueryParameters.UC_ID_REKLAMATION, aWhichButtonIUse,
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("auft.title.panel.auswahl"),
					true);

			panelQueryReklamation.befuellePanelFilterkriterienDirekt(
					ReklamationFilterFactory.getInstance()
							.createFKDReklamationsnummer(),
					ReklamationFilterFactory.getInstance()
							.createFKDReklamationArtikelnummer());
			panelQueryReklamation.addDirektFilter(ReklamationFilterFactory
					.getInstance().createFKDReklamationKdReklaNr());
			panelQueryReklamation.addDirektFilter(ReklamationFilterFactory
					.getInstance().createFKDMaschinengruppe());

			panelQueryReklamation.eventYouAreSelected(false);

			panelQueryReklamation
					.befuelleFilterkriteriumSchnellansicht(ReklamationFilterFactory
							.getInstance().createFKReklamationSchnellansicht());

			setComponentAt(IDX_PANEL_AUSWAHL, panelQueryReklamation);
			/*
			 * try { panelQueryReklamation.setSelectedId(LPMain.getInstance()
			 * .getTheClient().getIDPersonal()); } catch (Throwable ex) { //
			 * nothing here }
			 */
		}
	}

	private void createDetail(Integer key) throws Throwable {
		if (panelDetailReklamationErfassung == null) {
			panelDetailReklamationErfassung = new PanelReklamation(
					getInternalFrameReklamation(), LPMain.getInstance()
							.getTextRespectUISPr("lp.detail"), key);
			setComponentAt(IDX_PANEL_DETAIL, panelDetailReklamationErfassung);
		}
	}

	private void createAnalyse(Integer key) throws Throwable {
		if (panelDetailReklamationAnalyse == null) {
			panelDetailReklamationAnalyse = new PanelReklamationAnalyse(
					getInternalFrameReklamation(), LPMain.getInstance()
							.getTextRespectUISPr("rekla.analyse"), key);
			setComponentAt(IDX_PANEL_DATEN, panelDetailReklamationAnalyse);
		}
	}

	private void createMassnahmen(Integer key) throws Throwable {
		if (panelDetailReklamationMassnahmen == null) {
			panelDetailReklamationMassnahmen = new PanelReklamationMassnahmen(
					getInternalFrameReklamation(), LPMain.getInstance()
							.getTextRespectUISPr("rekla.massnahme"), key);
			setComponentAt(IDX_PANEL_MASSNAHMEN,
					panelDetailReklamationMassnahmen);
		}
	}

	/**
	 * jbInit
	 * 
	 * @throws Throwable
	 */
	private void jbInit() throws Throwable {

		int tabIndex = 0;
		IDX_PANEL_AUSWAHL = tabIndex;
		insertTab(LPMain.getInstance().getTextRespectUISPr("lp.auswahl"), null,
				null, LPMain.getInstance().getTextRespectUISPr("lp.auswahl"),
				IDX_PANEL_AUSWAHL);

		tabIndex++;
		IDX_PANEL_DETAIL = tabIndex;
		insertTab(LPMain.getInstance().getTextRespectUISPr("lp.detail"), null,
				null, LPMain.getInstance().getTextRespectUISPr("lp.detail"),
				IDX_PANEL_DETAIL);

		tabIndex++;
		IDX_PANEL_DATEN = tabIndex;
		insertTab(LPMain.getInstance().getTextRespectUISPr("rekla.analyse"),
				null, null,
				LPMain.getInstance().getTextRespectUISPr("rekla.analyse"),
				IDX_PANEL_DATEN);
		tabIndex++;
		IDX_PANEL_MASSNAHMEN = tabIndex;
		insertTab(LPMain.getInstance().getTextRespectUISPr("rekla.massnahme"),
				null, null,
				LPMain.getInstance().getTextRespectUISPr("rekla.massnahme"),
				IDX_PANEL_MASSNAHMEN);

		tabIndex++;
		IDX_PANEL_BILDER = tabIndex;
		insertTab(LPMain.getInstance().getTextRespectUISPr("lp.bilder"), null,
				null, LPMain.getInstance().getTextRespectUISPr("lp.bilder"),
				IDX_PANEL_BILDER);

		createAuswahl();

		if ((Integer) panelQueryReklamation.getSelectedId() != null) {
			getInternalFrameReklamation().setReklamationDto(
					DelegateFactory
							.getInstance()
							.getReklamationDelegate()
							.reklamationFindByPrimaryKey(
									(Integer) panelQueryReklamation
											.getSelectedId()));
		}

		// wenn es fuer das tabbed pane noch keinen eintrag gibt, die
		// restlichen panel deaktivieren
		if (panelQueryReklamation.getSelectedId() == null) {
			getInternalFrame().enableAllPanelsExcept(false);
		}

		// damit D2 einen aktuellen hat.
		ItemChangedEvent it = new ItemChangedEvent(panelQueryReklamation,
				ItemChangedEvent.ITEM_CHANGED);
		lPEventItemChanged(it);

		this.addChangeListener(this);
		this.getInternalFrame().addItemChangedListener(this);

	}

	public void setKeyWasForLockMe() {
		Object oKey = panelQueryReklamation.getSelectedId();

		if (oKey != null) {
			getInternalFrame().setKeyWasForLockMe(oKey.toString());
		} else {
			getInternalFrame().setKeyWasForLockMe(null);
		}
	}

	public void refreshTitle() {

		getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_OHRWASCHLUNTEN,
				LPMain.getInstance().getTextRespectUISPr("rekla.modulname"));
		getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_OHRWASCHLOBEN,
				((PanelBasis) this.getSelectedComponent()).getAdd2Title());
		if (getInternalFrameReklamation().getReklamationDto() != null) {

			if (getInternalFrameReklamation().getReklamationDto().getCNr() != null) {
				getInternalFrame().setLpTitle(
						InternalFrame.TITLE_IDX_AS_I_LIKE,
						getInternalFrameReklamation().getReklamationDto()
								.getCNr());
			} else {
				getInternalFrame().setLpTitle(
						InternalFrame.TITLE_IDX_AS_I_LIKE, "");
			}
		}
	}

	public InternalFrameReklamation getInternalFrameReklamation() {
		return (InternalFrameReklamation) getInternalFrame();
	}

	protected void lPActionEvent(ActionEvent e) throws Throwable {

		if (e.getActionCommand().equals(MENU_BEARBEITEN_KOMMENTAR)) {
			if (pruefeAktuellesReklamation()) {

				if (!getReklamationDetail().isLockedDlg()) {
					refreshPdKommentar();
					getInternalFrame().showPanelDialog(pdReklamationKommentar);

				}
			}
		} else if (e.getActionCommand().equals(
				MENUE_JOURNAL_ACTION_REKLAMATIONSJOURNAL)) {
			String add2Title = LPMain.getInstance().getTextRespectUISPr(
					"rekla.reklamationsjournal");
			getInternalFrame()
					.showReportKriterien(
							new ReportReklamationsjournal(getInternalFrame(),
									add2Title));

		} else if (e.getActionCommand().equals(
				MENUE_JOURNAL_ACTION_LIEFERANTENTERMINTREUE)) {
			String add2Title = LPMain.getInstance().getTextRespectUISPr(
					"rekla.lieferantentermintreue");
			getInternalFrame().showReportKriterien(
					new ReportLieferantentermintreue(getInternalFrame(),
							add2Title));

		} else if (e.getActionCommand().equals(
				MENUE_JOURNAL_ACTION_LIEFERANTENBEURTEILUNG)) {
			String add2Title = LPMain.getInstance().getTextRespectUISPr(
					"rekla.lieferantenbeurteilung");
			getInternalFrame().showReportKriterien(
					new ReportLieferantenbeurteilung(getInternalFrame(),
							add2Title));

		} else if (e.getActionCommand()
				.equals(MENUE_JOURNAL_ACTION_FEHLERARTEN)) {
			String add2Title = LPMain.getInstance().getTextRespectUISPr(
					"rekla.journal.fehlerarten");
			getInternalFrame().showReportKriterien(
					new ReportFehlerart(getInternalFrame(), add2Title));

		} else if (e.getActionCommand().equals(
				MENUE_JOURNAL_ACTION_MITARBEITERREKLAMATION)) {
			String add2Title = LPMain.getInstance().getTextRespectUISPr(
					"rekla.journal.mitarbeiterreklamation");
			getInternalFrame().showReportKriterien(
					new ReportMitarbeiterreklamation(getInternalFrame(),
							add2Title));

		} else if (e.getActionCommand().equals(
				MENUE_JOURNAL_ACTION_MASCHINENREKLAMATION)) {
			String add2Title = LPMain.getInstance().getTextRespectUISPr(
					"rekla.journal.maschinenreklamation");
			getInternalFrame().showReportKriterien(
					new ReportMaschinenreklamation(getInternalFrame(),
							add2Title));

		} else if (e.getActionCommand().equals(
				MENU_BEARBEITEN_MANUELL_ERLEDIGEN)) {
			if (getInternalFrameReklamation().getReklamationDto() != null) {

				if (getInternalFrameReklamation().getReklamationDto()
						.getTErledigt() == null) {
					if (DialogFactory
							.showModalJaNeinDialog(
									getInternalFrame(),
									LPMain.getTextRespectUISPr("fert.status.auferledigtsetzen"))) {

						// Beurteilung angeben
						panelBeurteilung = ReklamationFilterFactory
								.getInstance().createPanelFLRBehandlung(
										getInternalFrame(), null, false);
						new DialogQuery(panelBeurteilung);

					}
				} else {
					if (DialogFactory
							.showModalJaNeinDialog(
									getInternalFrame(),
									LPMain.getTextRespectUISPr("rekla.erledigtaufheben"))) {
						DelegateFactory
								.getInstance()
								.getReklamationDelegate()
								.reklamationErledigenOderAufheben(
										getInternalFrameReklamation()
												.getReklamationDto().getIId(),
										null);
						panelQueryReklamation.eventYouAreSelected(false);
					}

				}
			}
		} else if (e.getActionCommand().equals(MENU_ACTION_DATEI_DRUCKEN)) {
			printReklamation();
		}
	}

	public void printReklamation() throws Throwable {
		if (getInternalFrameReklamation().getReklamationDto() != null) {

			if (getInternalFrameReklamation().getReklamationDto()
					.getReklamationartCNr()
					.equals(ReklamationFac.REKLAMATIONART_KUNDE)
					&& getInternalFrameReklamation().getReklamationDto()
							.getIKundeunterart() != null
					&& getInternalFrameReklamation()
							.getReklamationDto()
							.getIKundeunterart()
							.equals(ReklamationFac.REKLAMATION_KUNDEUNTERART_LIEFERANT)) {
				getInternalFrame()
						.showReportKriterien(
								new ReportReklamationUnterartKundeLieferant(
										getInternalFrameReklamation(), "",
										getInternalFrameReklamation()
												.getReklamationDto().getIId()),
								DelegateFactory
										.getInstance()
										.getKundeDelegate()
										.kundeFindByPrimaryKey(
												getInternalFrameReklamation()
														.getReklamationDto()
														.getKundeIId())
										.getPartnerDto(),
								getInternalFrameReklamation()
										.getReklamationDto()
										.getAnsprechpartnerIId());
			} else {

				PartnerDto partnerDtoEmpfaenger = null;
				Integer ansprechprtnerIId = null;

				if (getInternalFrameReklamation().getReklamationDto()
						.getReklamationartCNr()
						.equals(ReklamationFac.REKLAMATIONART_KUNDE)) {

					partnerDtoEmpfaenger = DelegateFactory
							.getInstance()
							.getKundeDelegate()
							.kundeFindByPrimaryKey(
									getInternalFrameReklamation()
											.getReklamationDto().getKundeIId())
							.getPartnerDto();

					ansprechprtnerIId = getInternalFrameReklamation()
							.getReklamationDto().getAnsprechpartnerIId();

				} else if (getInternalFrameReklamation().getReklamationDto()
						.getReklamationartCNr()
						.equals(ReklamationFac.REKLAMATIONART_LIEFERANT)) {
					partnerDtoEmpfaenger = DelegateFactory
							.getInstance()
							.getLieferantDelegate()
							.lieferantFindByPrimaryKey(
									getInternalFrameReklamation()
											.getReklamationDto()
											.getLieferantIId()).getPartnerDto();

					ansprechprtnerIId = getInternalFrameReklamation()
							.getReklamationDto()
							.getAnsprechpartnerIIdLieferant();

				}

				getInternalFrame().showReportKriterien(
						new ReportReklamation(getInternalFrameReklamation(),
								"", getInternalFrameReklamation()
										.getReklamationDto().getIId()),
						partnerDtoEmpfaenger, ansprechprtnerIId);
			}
		}
	}

	private void refreshPdKommentar() throws Throwable {
		// das Panel immer neu anlegen, sonst funktioniert das Locken des
		// Angebots nicht richtig
		pdReklamationKommentar = new PanelDialogReklamationKommentar(
				getInternalFrame(), LPMain.getInstance().getTextRespectUISPr(
						"lp.kommentar"), true);
	}

	private PanelBasis getReklamationDetail() throws Throwable {
		Integer iIdPersonal = null;

		if (panelDetailReklamationErfassung == null) {

			// Die Angebot hat einen Key vom Typ Integer
			if (getInternalFrame().getKeyWasForLockMe() != null) {
				iIdPersonal = new Integer(Integer.parseInt(getInternalFrame()
						.getKeyWasForLockMe()));
			}

			panelDetailReklamationErfassung = new PanelPersonal(
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("lp.detail"), iIdPersonal); // empty
			// bei
			// leerer
			// angebotsliste

			setComponentAt(IDX_PANEL_DETAIL, panelDetailReklamationErfassung);
		}

		return panelDetailReklamationErfassung;
	}

	public boolean pruefeAktuellesReklamation() {
		boolean bIstGueltig = true;

		if (getInternalFrameReklamation().getReklamationDto() == null
				|| getInternalFrameReklamation().getReklamationDto().getIId() == null) {
			bIstGueltig = false;
			DialogFactory.showModalDialog(LPMain.getInstance()
					.getTextRespectUISPr("lp.warning"), LPMain.getInstance()
					.getTextRespectUISPr("pers.warning.keinereklamation"));
		}

		return bIstGueltig;
	}

	public void lPEventItemChanged(ItemChangedEvent e) throws Throwable {

		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			if (e.getSource() == panelQueryReklamation) {
				Integer iId = (Integer) panelQueryReklamation.getSelectedId();
				if (iId != null) {
					setSelectedComponent(panelDetailReklamationErfassung);
					panelDetailReklamationErfassung.eventYouAreSelected(false);
				}
			} else if (e.getSource() == panelBeurteilung) {
				Integer iId = (Integer) panelBeurteilung.getSelectedId();
				if (iId != null) {

					DelegateFactory
							.getInstance()
							.getReklamationDelegate()
							.reklamationErledigenOderAufheben(
									getInternalFrameReklamation()
											.getReklamationDto().getIId(), iId);

				}
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_SAVE) {
			if (e.getSource() == panelDetailReklamationErfassung) {
				panelQueryReklamation.clearDirektFilter();
				Object oKey = panelDetailReklamationErfassung
						.getKeyWhenDetailPanel();

				panelQueryReklamation.setSelectedId(oKey);
			} else if (e.getSource() == panelDetailBild) {
				Object oKey = panelDetailBild.getKeyWhenDetailPanel();
				panelQueryBild.eventYouAreSelected(false);
				panelQueryBild.setSelectedId(oKey);
				panelSplitBild.eventYouAreSelected(false);

			}
		} else if (e.getID() == ItemChangedEvent.ITEM_CHANGED) {
			if (e.getSource() == panelQueryReklamation) {
				if (panelQueryReklamation.getSelectedId() != null) {
					getInternalFrameReklamation().setKeyWasForLockMe(
							panelQueryReklamation.getSelectedId() + "");
					createDetail((Integer) panelQueryReklamation
							.getSelectedId());
					panelDetailReklamationErfassung
							.setKeyWhenDetailPanel(panelQueryReklamation
									.getSelectedId());

					getInternalFrameReklamation().setReklamationDto(
							DelegateFactory
									.getInstance()
									.getReklamationDelegate()
									.reklamationFindByPrimaryKey(
											(Integer) panelQueryReklamation
													.getSelectedId()));

					if (getInternalFrameReklamation().getReklamationDto() != null) {
						getInternalFrame().setLpTitle(
								InternalFrame.TITLE_IDX_AS_I_LIKE,
								getInternalFrameReklamation()
										.getReklamationDto().getCNr());
					}

					getInternalFrame()
							.enableAllOberePanelsExceptMe(
									this,
									IDX_PANEL_AUSWAHL,
									((ISourceEvent) e.getSource())
											.getIdSelected() != null);
				} else {
					getInternalFrame().enableAllOberePanelsExceptMe(this,
							IDX_PANEL_AUSWAHL, false);
				}

			} else if (e.getSource() == panelQueryBild) {

				Integer iId = (Integer) panelQueryBild.getSelectedId();
				panelDetailBild.setKeyWhenDetailPanel(iId);
				panelDetailBild.eventYouAreSelected(false);
				panelQueryBild.updateButtons();
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_UPDATE) {
			if (e.getSource() == panelDetailBild) {
				panelQueryBild.updateButtons(new LockStateValue(
						PanelBasis.LOCK_FOR_NEW));
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_GOTO_MY_DEFAULT_QP) {
			// aktiviere ein QP ...
			if (e.getSource() == panelDetailReklamationErfassung) {
				panelQueryReklamation.eventYouAreSelected(false);
				getInternalFrameReklamation().getReklamationDto().setIId(
						(Integer) panelQueryReklamation.getSelectedId());
				getInternalFrame().setKeyWasForLockMe(
						panelQueryReklamation.getSelectedId() + "");
				this.setSelectedComponent(panelQueryReklamation);
			} else if (e.getSource() == panelDetailBild) {
				setKeyWasForLockMe();
				if (panelDetailBild.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryBild
							.getId2SelectAfterDelete();
					panelQueryBild.setSelectedId(oNaechster);
				}
				panelSplitBild.eventYouAreSelected(false);
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_YOU_ARE_SELECTED) {
			refreshTitle();
			super.lPEventItemChanged(e);
		} else if (e.getID() == ItemChangedEvent.ACTION_DISCARD
				|| e.getID() == ItemChangedEvent.ACTION_DELETE) {
			if (e.getSource() == panelDetailBild) {
				panelSplitBild.eventYouAreSelected(false);
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_NEW) {
			if (e.getSource() == panelQueryReklamation) {

				createDetail(null);
				panelDetailReklamationErfassung.eventActionNew(e, true, false);
				this.setSelectedComponent(panelDetailReklamationErfassung);

			} else if (e.getSource() == panelQueryBild) {
				panelDetailBild.eventActionNew(e, true, false);
				panelDetailBild.eventYouAreSelected(false);
				this.setSelectedComponent(panelSplitBild);
			}
		}

	}

	public boolean pruefeObReklamationAenderbar() {
		if (getInternalFrameReklamation().getReklamationDto() != null
				&& getInternalFrameReklamation().getReklamationDto()
						.getStatusCNr() != null
				&& getInternalFrameReklamation().getReklamationDto()
						.getStatusCNr().equals(LocaleFac.STATUS_ERLEDIGT)) {

			DialogFactory.showModalDialog(
					LPMain.getInstance().getTextRespectUISPr("lp.warning"),
					LPMain.getInstance().getTextRespectUISPr(
							"rekla.warning.reklakannnichtgeaendertwerden"));

			return false;
		} else {
			return true;
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
			createAuswahl();
			panelQueryReklamation.eventYouAreSelected(false);
			panelQueryReklamation.updateButtons();
		} else if (selectedIndex == IDX_PANEL_DETAIL) {
			createDetail((Integer) panelQueryReklamation.getSelectedId());
			panelDetailReklamationErfassung.eventYouAreSelected(false);
		} else if (selectedIndex == IDX_PANEL_DATEN) {
			createAnalyse(getInternalFrameReklamation().getReklamationDto()
					.getIId());
			panelDetailReklamationAnalyse.eventYouAreSelected(false);
		} else if (selectedIndex == IDX_PANEL_MASSNAHMEN) {
			createMassnahmen(getInternalFrameReklamation().getReklamationDto()
					.getIId());
			panelDetailReklamationMassnahmen.eventYouAreSelected(false);
		} else if (selectedIndex == IDX_PANEL_BILDER) {
			createReklamationbild(getInternalFrameReklamation()
					.getReklamationDto().getIId());
			panelSplitBild.eventYouAreSelected(false);
			panelQueryBild.updateButtons();
		}

		refreshTitle();
	}

	public javax.swing.JMenuBar getJMenuBar() throws Throwable {
		if (wrapperMenuBar == null) {
			wrapperMenuBar = new WrapperMenuBar(this);

			JMenu jmModul = (JMenu) wrapperMenuBar
					.getComponent(WrapperMenuBar.MENU_MODUL);
			JMenuItem menuItemDateiDrucken = new JMenuItem(LPMain.getInstance()
					.getTextRespectUISPr("lp.menu.drucken"));
			menuItemDateiDrucken.addActionListener(this);
			menuItemDateiDrucken.setActionCommand(MENU_ACTION_DATEI_DRUCKEN);
			jmModul.add(new JSeparator(), 0);
			jmModul.add(menuItemDateiDrucken, 0);
			// Menue Bearbeiten
			JMenu jmBearbeiten = (JMenu) wrapperMenuBar
					.getComponent(WrapperMenuBar.MENU_BEARBEITEN);

			JMenuItem menuItemBearbeitenExternerKommentar = new JMenuItem(
					LPMain.getInstance().getTextRespectUISPr("lp.kommentar"));
			menuItemBearbeitenExternerKommentar.addActionListener(this);
			menuItemBearbeitenExternerKommentar
					.setActionCommand(MENU_BEARBEITEN_KOMMENTAR);
			jmBearbeiten.add(menuItemBearbeitenExternerKommentar);
			jmBearbeiten.add(new JSeparator());

			WrapperMenuItem menuItemBearbeitenManuellErledigen = new WrapperMenuItem(
					LPMain.getTextRespectUISPr("lp.menu.menuellerledigen"),
					RechteFac.RECHT_REKLA_REKLAMATION_CUD);
			menuItemBearbeitenManuellErledigen.addActionListener(this);
			menuItemBearbeitenManuellErledigen
					.setActionCommand(MENU_BEARBEITEN_MANUELL_ERLEDIGEN);
			jmBearbeiten.add(menuItemBearbeitenManuellErledigen);

			JMenu journal = (JMenu) wrapperMenuBar
					.getComponent(WrapperMenuBar.MENU_JOURNAL);
			JMenuItem menuItemReklamationsjournal = new JMenuItem(LPMain
					.getInstance().getTextRespectUISPr(
							"rekla.reklamationsjournal"));
			menuItemReklamationsjournal.addActionListener(this);
			menuItemReklamationsjournal
					.setActionCommand(MENUE_JOURNAL_ACTION_REKLAMATIONSJOURNAL);
			journal.add(menuItemReklamationsjournal);

			JMenuItem menuItemLieferantentermintreue = new JMenuItem(LPMain
					.getInstance().getTextRespectUISPr(
							"rekla.lieferantentermintreue"));
			menuItemLieferantentermintreue.addActionListener(this);
			menuItemLieferantentermintreue
					.setActionCommand(MENUE_JOURNAL_ACTION_LIEFERANTENTERMINTREUE);
			journal.add(menuItemLieferantentermintreue);

			if (LPMain
					.getInstance()
					.getDesktop()
					.darfAnwenderAufZusatzfunktionZugreifen(
							MandantFac.ZUSATZFUNKTION_LIEFERANTENBEURTEILUNG)) {
				if (DelegateFactory
						.getInstance()
						.getTheJudgeDelegate()
						.hatRecht(
								com.lp.server.benutzer.service.RechteFac.RECHT_REKLA_QUALITAETSSICHERUNG_CUD)) {
					JMenuItem menuItemLieferantenbeurteilung = new JMenuItem(
							LPMain.getInstance().getTextRespectUISPr(
									"rekla.lieferantenbeurteilung"));
					menuItemLieferantenbeurteilung.addActionListener(this);
					menuItemLieferantenbeurteilung
							.setActionCommand(MENUE_JOURNAL_ACTION_LIEFERANTENBEURTEILUNG);
					journal.add(menuItemLieferantenbeurteilung);
				}
			}

			JMenuItem menuItemFehlerarten = new JMenuItem(LPMain.getInstance()
					.getTextRespectUISPr("rekla.journal.fehlerarten"));
			menuItemFehlerarten.addActionListener(this);
			menuItemFehlerarten
					.setActionCommand(MENUE_JOURNAL_ACTION_FEHLERARTEN);
			journal.add(menuItemFehlerarten);

			JMenuItem menuItemMitarbeitereklamation = new JMenuItem(LPMain
					.getInstance().getTextRespectUISPr(
							"rekla.journal.mitarbeiterreklamation"));
			menuItemMitarbeitereklamation.addActionListener(this);
			menuItemMitarbeitereklamation
					.setActionCommand(MENUE_JOURNAL_ACTION_MITARBEITERREKLAMATION);
			journal.add(menuItemMitarbeitereklamation);

			JMenuItem menuItemMaschinenreklamation = new JMenuItem(LPMain
					.getInstance().getTextRespectUISPr(
							"rekla.journal.maschinenreklamation"));
			menuItemMaschinenreklamation.addActionListener(this);
			menuItemMaschinenreklamation
					.setActionCommand(MENUE_JOURNAL_ACTION_MASCHINENREKLAMATION);
			journal.add(menuItemMaschinenreklamation);

		}

		return wrapperMenuBar;
	}

}
