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
package com.lp.client.zeiterfassung;

import java.awt.event.ActionEvent;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.event.ChangeEvent;

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
import com.lp.client.pc.LPMain;
import com.lp.client.personal.PersonalFilterFactory;
import com.lp.client.personal.ReportMaschinenliste;
import com.lp.client.system.SystemFilterFactory;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;

/**
 * <p>
 * &UUml;berschrift:
 * </p>
 * <p>
 * Beschreibung:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Organisation:
 * </p>
 * 
 * @author Christian Kollmann
 * @version $Revision: 1.10 $
 */

public class TabbedPaneMaschinen extends TabbedPane {
	private PanelQuery panelQueryMaschinen = null;
	private PanelBasis panelSplitMaschinen = null;
	private PanelBasis panelBottomMaschinen = null;

	private PanelQuery panelQueryMaschinenkosten = null;
	private PanelBasis panelSplitMaschinenkosten = null;
	private PanelBasis panelBottomMaschinenkosten = null;

	private PanelQuery panelQueryZeitdaten = null;
	private PanelBasis panelSplitZeitdaten = null;
	private PanelBasis panelBottomZeitdaten = null;

	private WrapperMenuBar wrapperMenuBar = null;

	private final static int IDX_PANEL_AUSWAHL = 0;
	private final static int IDX_PANEL_ZEITDATEN = 1;
	private final static int IDX_PANEL_KOSTEN = 2;

	private final String MENUE_ACTION_MASCHINENLISTE = "MENUE_ACTION_MASCHINENLISTE";
	private final String MENUE_ACTION_PRODUKTIVITAET = "MENUE_ACTION_PRODUKTIVITAET";
	private final String MENUE_ACTION_MASCHINENZEITDATEN = "MENUE_ACTION_MASCHINENZEITDATEN";
	private final String MENUE_ACTION_MASCHINENBELEGUNG = "MENUE_ACTION_MASCHINENBELEGUNG";

	public TabbedPaneMaschinen(InternalFrame internalFrameI) throws Throwable {
		super(internalFrameI, LPMain
				.getTextRespectUISPr("zeiterfassung.title.tab.maschinen"));

		jbInit();
		initComponents();
	}

	public PanelQuery getPanelQueryMaschinenkosten() {
		return panelQueryMaschinenkosten;
	}

	public InternalFrameZeiterfassung getInternalFramePersonal() {
		return (InternalFrameZeiterfassung) getInternalFrame();
	}

	private void jbInit() throws Throwable {

		String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW };

		panelQueryMaschinen = new PanelQuery(
				null,
				SystemFilterFactory.getInstance().createFKMandantCNr(),
				QueryParameters.UC_ID_MASCHINE,
				aWhichButtonIUse,
				getInternalFrame(),
				LPMain.getTextRespectUISPr("zeiterfassung.title.tab.maschinen"),
				true);
		panelQueryMaschinen.eventYouAreSelected(false);

		panelQueryMaschinen.befuellePanelFilterkriterienDirektUndVersteckte(
				SystemFilterFactory.getInstance().createFKDBezeichnung(), null,ZeiterfassungFilterFactory.getInstance().createFKVMaschine());

		panelBottomMaschinen = new PanelMaschinen(getInternalFrame(), LPMain
				.getTextRespectUISPr("zeiterfassung.title.tab.maschinen"), null);
		panelSplitMaschinen = new PanelSplit(getInternalFrame(),
				panelBottomMaschinen, panelQueryMaschinen, 270);
		addTab(LPMain.getTextRespectUISPr("zeiterfassung.title.tab.maschinen"),
				panelSplitMaschinen);

		insertTab(LPMain.getTextRespectUISPr("zeiterfassung.title.tab.zeitdaten"),
				null, null, LPMain
						.getTextRespectUISPr("zeiterfassung.title.tab.zeitdaten"),
				IDX_PANEL_ZEITDATEN);

		insertTab(LPMain.getTextRespectUISPr("lp.kosten"), null, null, LPMain
				.getTextRespectUISPr("lp.kosten"), IDX_PANEL_KOSTEN);

		// Itemevents an MEIN Detailpanel senden kann.
		getInternalFrame().addItemChangedListener(this);
		this.addChangeListener(this);

	}

	private void createMaschinenkosten(Integer maschineIId) throws Throwable {

		if (panelQueryMaschinenkosten == null) {
			String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW };

			FilterKriterium[] filters = ZeiterfassungFilterFactory
					.getInstance().createFKMaschinenkosten(maschineIId);

			panelQueryMaschinenkosten = new PanelQuery(null, filters,
					QueryParameters.UC_ID_MASCHINENKOSTEN, aWhichButtonIUse,
					getInternalFrame(),
					LPMain.getTextRespectUISPr("lp.kosten"), true);

			panelBottomMaschinenkosten = new PanelMaschinenkosten(
					getInternalFrame(),
					LPMain.getTextRespectUISPr("lp.kosten"), null);

			panelSplitMaschinenkosten = new PanelSplit(getInternalFrame(),
					panelBottomMaschinenkosten, panelQueryMaschinenkosten, 350);

			setComponentAt(IDX_PANEL_KOSTEN, panelSplitMaschinenkosten);
		} else {
			// filter refreshen.
			panelQueryMaschinenkosten
					.setDefaultFilter(ZeiterfassungFilterFactory.getInstance()
							.createFKMaschinenkosten(maschineIId));
		}
	}

	private void createZeitdaten(Integer maschineIId) throws Throwable {

		if (panelQueryZeitdaten == null) {
			String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW };

			FilterKriterium[] filters = ZeiterfassungFilterFactory
					.getInstance().createFKMaschinenzeitdaten(maschineIId);

			panelQueryZeitdaten = new PanelQuery(
					null,
					filters,
					QueryParameters.UC_ID_MASCHINENZEITDATEN,
					aWhichButtonIUse,
					getInternalFrame(),
					LPMain.getTextRespectUISPr("fert.tab.oben.zeitdaten.title"),
					true);

			panelQueryZeitdaten.befuellePanelFilterkriterienDirekt(
					ZeiterfassungFilterFactory.
			          getInstance().createFKDMaschinenzeitdatenPersonalname(), ZeiterfassungFilterFactory.
			          getInstance().createMaschinenzeitdatenFKDLosnummer());
			
			panelBottomZeitdaten = new PanelMaschinenzeitdaten(
					getInternalFrame(),
					LPMain.getTextRespectUISPr("fert.tab.oben.zeitdaten.title"),
					null);

			panelSplitZeitdaten = new PanelSplit(getInternalFrame(),
					panelBottomZeitdaten, panelQueryZeitdaten, 320);

			setComponentAt(IDX_PANEL_ZEITDATEN, panelSplitZeitdaten);
		} else {
			// filter refreshen.
			panelQueryZeitdaten.setDefaultFilter(ZeiterfassungFilterFactory
					.getInstance().createFKMaschinenzeitdaten(maschineIId));
		}
	}

	public void lPEventItemChanged(ItemChangedEvent e) throws Throwable {

		if (e.getID() == ItemChangedEvent.ITEM_CHANGED) {
			if (e.getSource() == panelQueryMaschinenkosten) {
				Integer iId = (Integer) panelQueryMaschinenkosten
						.getSelectedId();
				panelBottomMaschinenkosten.setKeyWhenDetailPanel(iId);
				panelBottomMaschinenkosten.eventYouAreSelected(false);
				panelQueryMaschinenkosten.updateButtons();
			} else if (e.getSource() == panelQueryZeitdaten) {
				Integer iId = (Integer) panelQueryZeitdaten.getSelectedId();
				panelBottomZeitdaten.setKeyWhenDetailPanel(iId);
				panelBottomZeitdaten.eventYouAreSelected(false);
				panelQueryZeitdaten.updateButtons();
			} else if (e.getSource() == panelQueryMaschinen) {
				if (panelQueryMaschinen.getSelectedId() != null) {
					Object key = ((ISourceEvent) e.getSource()).getIdSelected();

					Integer iId = (Integer) panelQueryMaschinen.getSelectedId();
					panelBottomMaschinen.setKeyWhenDetailPanel(iId);
					panelBottomMaschinen.eventYouAreSelected(false);
					getInternalFramePersonal().setKeyWasForLockMe(key + "");

					getInternalFramePersonal().setMaschineDto(
							DelegateFactory.getInstance()
									.getZeiterfassungDelegate()
									.maschineFindByPrimaryKey((Integer) key));
					String sBezeichnung = "";
					if (getInternalFramePersonal().getMaschineDto()
							.getBezeichnung() != null) {
						sBezeichnung += " "
								+ getInternalFramePersonal().getMaschineDto()
										.getBezeichnung();
					}
					getInternalFrame().setLpTitle(
							InternalFrame.TITLE_IDX_AS_I_LIKE,
							"Maschine: " + sBezeichnung);

					if (panelQueryMaschinen.getSelectedId() == null) {
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
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_YOU_ARE_SELECTED) {
			refreshTitle();
			// panelSplitMaschinen.eventYouAreSelected(false);
		} else if (e.getID() == ItemChangedEvent.ACTION_NEW) {
			if (e.getSource() == panelQueryMaschinen) {
				panelBottomMaschinen.eventActionNew(e, true, false);
				panelBottomMaschinen.eventYouAreSelected(false);
			} else if (e.getSource() == panelQueryMaschinenkosten) {
				panelBottomMaschinenkosten.eventActionNew(e, true, false);
				panelBottomMaschinenkosten.eventYouAreSelected(false);
			} else if (e.getSource() == panelQueryZeitdaten) {
				panelBottomZeitdaten.eventActionNew(e, true, false);
				panelBottomZeitdaten.eventYouAreSelected(false);
			}

		} else if (e.getID() == ItemChangedEvent.ACTION_DISCARD
				|| e.getID() == ItemChangedEvent.ACTION_DELETE) {
			if (e.getSource() == panelBottomMaschinen) {
				panelSplitMaschinen.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomMaschinenkosten) {
				panelSplitMaschinenkosten.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomZeitdaten) {
				panelSplitZeitdaten.eventYouAreSelected(false);
			}
		}

		else if (e.getID() == ItemChangedEvent.ACTION_UPDATE) {
			if (e.getSource() == panelBottomMaschinen) {
				panelQueryMaschinen.updateButtons(new LockStateValue(
						PanelBasis.LOCK_FOR_NEW));
			} else if (e.getSource() == panelBottomMaschinenkosten) {
				panelQueryMaschinenkosten.updateButtons(new LockStateValue(
						PanelBasis.LOCK_FOR_NEW));
			} else if (e.getSource() == panelBottomZeitdaten) {
				panelQueryZeitdaten.updateButtons(new LockStateValue(
						PanelBasis.LOCK_FOR_NEW));
			}
		}

		else if (e.getID() == ItemChangedEvent.ACTION_SAVE) {
			if (e.getSource() == panelBottomMaschinen) {
				Object oKey = panelBottomMaschinen.getKeyWhenDetailPanel();
				panelQueryMaschinen.eventYouAreSelected(false);
				panelQueryMaschinen.setSelectedId(oKey);
				panelSplitMaschinen.eventYouAreSelected(false);
				panelQueryMaschinen.updateButtons();

			} else if (e.getSource() == panelBottomMaschinenkosten) {
				Object oKey = panelBottomMaschinenkosten
						.getKeyWhenDetailPanel();
				panelQueryMaschinenkosten.eventYouAreSelected(false);
				panelQueryMaschinenkosten.setSelectedId(oKey);
				panelSplitMaschinenkosten.eventYouAreSelected(false);

			} else if (e.getSource() == panelBottomZeitdaten) {
				Object oKey = panelBottomZeitdaten.getKeyWhenDetailPanel();
				panelQueryZeitdaten.eventYouAreSelected(false);
				panelQueryZeitdaten.setSelectedId(oKey);
				panelSplitZeitdaten.eventYouAreSelected(false);

			}

		} else if (e.getID() == ItemChangedEvent.ACTION_GOTO_MY_DEFAULT_QP) {
			if (e.getSource() == panelBottomMaschinen) {
				getInternalFrame().enableAllPanelsExcept(true);

				setKeyWasForLockMe();
				panelQueryMaschinen.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomMaschinenkosten) {
				setKeyWasForLockMe();
				if (panelBottomMaschinenkosten.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryMaschinenkosten
							.getId2SelectAfterDelete();
					panelQueryMaschinenkosten.setSelectedId(oNaechster);
				}
				panelSplitMaschinenkosten.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomZeitdaten) {
				setKeyWasForLockMe();
				if (panelBottomZeitdaten.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryZeitdaten
							.getId2SelectAfterDelete();
					panelQueryZeitdaten.setSelectedId(oNaechster);
				}
				panelSplitZeitdaten.eventYouAreSelected(false);
			}

		}

	}

	public void setKeyWasForLockMe() {
		Object oKey = panelQueryMaschinen.getSelectedId();

		if (oKey != null) {
			getInternalFrame().setKeyWasForLockMe(oKey.toString());
		} else {
			getInternalFrame().setKeyWasForLockMe(null);
		}
	}

	private void refreshTitle() {

		getInternalFrame()
				.setLpTitle(
						InternalFrame.TITLE_IDX_OHRWASCHLUNTEN,
						LPMain
								.getTextRespectUISPr("zeiterfassung.title.tab.maschinen"));
		getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_OHRWASCHLOBEN,
				((PanelBasis) this.getSelectedComponent()).getAdd2Title());
		if (getInternalFramePersonal().getMaschineDto() != null) {
			getInternalFrame().setLpTitle(
					3,
					getInternalFramePersonal().getMaschineDto()
							.getBezeichnung());
		}
	}

	public void lPEventObjectChanged(ChangeEvent e) throws Throwable {
		super.lPEventObjectChanged(e);

		int selectedIndex = this.getSelectedIndex();
		if (selectedIndex == IDX_PANEL_AUSWAHL) {
			panelQueryMaschinen.eventYouAreSelected(false);
			if (panelQueryMaschinen.getSelectedId() == null) {
				getInternalFrame().enableAllOberePanelsExceptMe(this,
						IDX_PANEL_AUSWAHL, false);
			}

			panelQueryMaschinen.updateButtons();
			panelSplitMaschinen.eventYouAreSelected(false);
		}

		else if (selectedIndex == IDX_PANEL_KOSTEN) {
			createMaschinenkosten(getInternalFramePersonal().getMaschineDto()
					.getIId());
			panelSplitMaschinenkosten.eventYouAreSelected(false);
			panelQueryMaschinenkosten.updateButtons();
		} else if (selectedIndex == IDX_PANEL_ZEITDATEN) {
			createZeitdaten(getInternalFramePersonal().getMaschineDto()
					.getIId());
			panelSplitZeitdaten.eventYouAreSelected(false);
			panelQueryZeitdaten.updateButtons();
		}

		refreshTitle();
	}

	protected void lPActionEvent(ActionEvent e) throws Throwable {
		if (e.getActionCommand().equals(MENUE_ACTION_PRODUKTIVITAET)) {
			String add2Title = LPMain
					.getTextRespectUISPr("zeiterfassung.report.produktivitaetsstatistik");
			getInternalFrame().showReportKriterien(
					new ReportMaschinenproduktivitaet(
							getInternalFramePersonal(), add2Title));

		} else if (e.getActionCommand().equals(MENUE_ACTION_MASCHINENLISTE)) {
			String add2Title = LPMain
					.getTextRespectUISPr("zeiterfassung.report.maschinenliste");
			getInternalFrame().showReportKriterien(
					new ReportMaschinenliste(getInternalFrame(), add2Title));

		} else if (e.getActionCommand().equals(MENUE_ACTION_MASCHINENZEITDATEN)) {
			String add2Title = LPMain
					.getTextRespectUISPr("zeiterfassung.report.maschinenzeitdaten");
			getInternalFrame().showReportKriterien(
					new ReportMaschinenzeitdaten(
							(InternalFrameZeiterfassung) getInternalFrame(),
							add2Title));

		} else if (e.getActionCommand().equals(MENUE_ACTION_MASCHINENBELEGUNG)) {
			String add2Title = LPMain
					.getTextRespectUISPr("pers.zeiterfassung.report.maschinenbelegung");
			getInternalFrame().showReportKriterien(
					new ReportMaschinenbelegung(
							(InternalFrameZeiterfassung) getInternalFrame(),
							add2Title,(Integer)panelQueryMaschinen.getSelectedId()));

		}
	}

	protected JMenuBar getJMenuBar() throws Throwable {
		if (wrapperMenuBar == null) {
			wrapperMenuBar = new WrapperMenuBar(this);
			// Produktivitaetisstatistik
			JMenu menuInfo = new WrapperMenu("lp.info", this);
			JMenuItem menuItemProd = new JMenuItem(
					LPMain
							.getTextRespectUISPr("zeiterfassung.report.produktivitaetsstatistik"));

			menuItemProd.addActionListener(this);

			menuItemProd.setActionCommand(MENUE_ACTION_PRODUKTIVITAET);
			menuInfo.add(menuItemProd);

			// Maschinenliste
			JMenuItem menuItemMaschinenliste = new JMenuItem(LPMain
					.getTextRespectUISPr("zeiterfassung.report.maschinenliste"));

			menuItemMaschinenliste.addActionListener(this);

			menuItemMaschinenliste
					.setActionCommand(MENUE_ACTION_MASCHINENLISTE);
			menuInfo.add(menuItemMaschinenliste);

			// Maschinenzeitdaten
			JMenuItem menuItemMaschinenzeitdaten = new JMenuItem(
					LPMain
							.getTextRespectUISPr("zeiterfassung.report.maschinenzeitdaten"));

			menuItemMaschinenzeitdaten.addActionListener(this);

			menuItemMaschinenzeitdaten
					.setActionCommand(MENUE_ACTION_MASCHINENZEITDATEN);
			menuInfo.add(menuItemMaschinenzeitdaten);

			wrapperMenuBar.addJMenuItem(menuInfo);

			JMenu modulJournal = (JMenu) wrapperMenuBar
					.getComponent(WrapperMenuBar.MENU_JOURNAL);
			// Maschinenzeitdaten
			JMenuItem menuItemMaschinenbelegung = new JMenuItem(
					LPMain
							.getTextRespectUISPr("pers.zeiterfassung.report.maschinenbelegung"));

			menuItemMaschinenbelegung.addActionListener(this);

			menuItemMaschinenbelegung
					.setActionCommand(MENUE_ACTION_MASCHINENBELEGUNG);
			modulJournal.add(menuItemMaschinenbelegung);

		}
		return wrapperMenuBar;

	}

}
