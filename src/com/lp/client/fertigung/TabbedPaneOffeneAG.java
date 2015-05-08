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
package com.lp.client.fertigung;

import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.event.ChangeEvent;

import com.lp.client.eingangsrechnung.InternalFrameEingangsrechnung;
import com.lp.client.frame.Defaults;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQuery;
import com.lp.client.frame.component.TabbedPane;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperComboBox;
import com.lp.client.frame.component.WrapperMenuBar;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPButtonAction;
import com.lp.client.pc.LPMain;
import com.lp.client.personal.ReportPersonalliste;
import com.lp.client.system.SystemFilterFactory;
import com.lp.server.fertigung.service.FertigungFac;
import com.lp.server.fertigung.service.LossollarbeitsplanDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.util.Facade;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.FilterKriteriumDirekt;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.server.util.fastlanereader.service.query.SortierKriterium;

@SuppressWarnings("static-access")
/**
 * <p>&UUml;berschrift: </p>
 * <p>Beschreibung: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Organisation: </p>
 * @author Christian Kollmann
 * @version $Revision: 1.5 $
 */
public class TabbedPaneOffeneAG extends TabbedPane {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private PanelQuery panelQueryOffeneAGs = null;

	private final static int IDX_PANEL_AUSWAHL = 0;

	private final static String EXTRA_GOTO_AG = "goto_ag";

	private WrapperMenuBar wrapperMenuBar = null;

	private WrapperCheckBox wcbReihen = new WrapperCheckBox();

	private static final String MENUE_ACTION_MASCHINE_MATERIAL = "MENUE_ACTION_MASCHINE_MATERIAL";

	public TabbedPaneOffeneAG(InternalFrame internalFrameI) throws Throwable {
		super(internalFrameI, LPMain.getInstance().getTextRespectUISPr(
				"fert.offeneags"));
		jbInit();
		initComponents();
		plusMinusButtonsSchalten(false);
	}

	public PanelQuery getPanelQueryWiederholende() {
		return panelQueryOffeneAGs;
	}

	private void jbInit() throws Throwable {

		insertTab(LPMain.getInstance().getTextRespectUISPr("fert.offeneags"),
				null, null,
				LPMain.getInstance().getTextRespectUISPr("fert.offeneags"),
				IDX_PANEL_AUSWAHL);

		createAuswahl();

		panelQueryOffeneAGs.eventYouAreSelected(false);

		// damit D2 einen aktuellen hat.
		ItemChangedEvent it = new ItemChangedEvent(panelQueryOffeneAGs,
				ItemChangedEvent.ITEM_CHANGED);
		lPEventItemChanged(it);

		this.addChangeListener(this);
		this.getInternalFrame().addItemChangedListener(this);

	}

	private void createAuswahl() throws Throwable {
		if (panelQueryOffeneAGs == null) {

			String[] aWhichButtonIUse = new String[] {
					PanelBasis.ACTION_POSITION_VONNNACHNMINUS1,
					PanelBasis.ACTION_POSITION_VONNNACHNPLUS1 };

			// Filter
			FilterKriterium[] kriterien = new FilterKriterium[1];
			kriterien[0] = new FilterKriterium("flroffeneags.mandant_c_nr",
					true, "'"
							+ LPMain.getInstance().getTheClient().getMandant()
							+ "'", FilterKriterium.OPERATOR_EQUAL, false);

			panelQueryOffeneAGs = new PanelQuery(null, kriterien,
					QueryParameters.UC_ID_OFFENE_AGS, aWhichButtonIUse,
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("fert.offeneags"), true);
			plusMinusButtonsSchalten(false);

			panelQueryOffeneAGs.createAndSaveAndShowButton(
					"/com/lp/client/res/data_into.png", LPMain.getInstance()
							.getTextRespectUISPr("fert.offeneags.gotoag"),
					PanelBasis.ACTION_MY_OWN_NEW + EXTRA_GOTO_AG, null);

			panelQueryOffeneAGs.setFilterComboBox(DelegateFactory.getInstance()
					.getZeiterfassungDelegate().getAllMaschinen(),
					new FilterKriterium("MASCHINE_GRUPPE", true, "" + "",
							FilterKriterium.OPERATOR_EQUAL, false), false,
					LPMain.getTextRespectUISPr("lp.alle"),false);

			panelQueryOffeneAGs
					.befuellePanelFilterkriterienDirekt(
							new FilterKriteriumDirekt(
									"sollmaterial.c_nr",
									"",
									FilterKriterium.OPERATOR_LIKE,
									LPMain.getTextRespectUISPr("fert.offeneags.filter.material"),
									FilterKriteriumDirekt.PROZENT_TRAILING, // Auswertung
																			// als
																			// '%XX'
									true, // wrapWithSingleQuotes
									true, Facade.MAX_UNBESCHRAENKT),
							new FilterKriteriumDirekt(
									"taetigkeit.c_nr",
									"",
									FilterKriterium.OPERATOR_LIKE,
									LPMain.getTextRespectUISPr("fert.offeneags.filter.taetigkeit"),
									FilterKriteriumDirekt.PROZENT_TRAILING, // Auswertung
																			// als
																			// '%XX'
									true, // wrapWithSingleQuotes
									true, Facade.MAX_UNBESCHRAENKT));
			panelQueryOffeneAGs.addDirektFilter(new FilterKriteriumDirekt(
					"flrkunde.flrpartner.c_kbez", "",
					FilterKriterium.OPERATOR_LIKE,
					LPMain.getTextRespectUISPr("fert.offeneags.filter.kunde"),
					FilterKriteriumDirekt.PROZENT_TRAILING, // Auswertung als
															// '%XX'
					true, // wrapWithSingleQuotes
					true, Facade.MAX_UNBESCHRAENKT));

			panelQueryOffeneAGs.addDirektFilter(new FilterKriteriumDirekt(
					"flrmaschine.c_identifikationsnr", "",
					FilterKriterium.OPERATOR_LIKE, LPMain
							.getTextRespectUISPr("lp.maschine"),
					FilterKriteriumDirekt.PROZENT_TRAILING, // Auswertung als
															// '%XX'
					true, // wrapWithSingleQuotes
					true, Facade.MAX_UNBESCHRAENKT));

			FilterKriteriumDirekt fkDirekt = FertigungFilterFactory
					.getInstance().createFKDLosnummerOffeneAGs();
			panelQueryOffeneAGs.addDirektFilter(fkDirekt);

			wcbReihen.setText(LPMain
					.getTextRespectUISPr("fert.offeneags.reihen"));
			wcbReihen.setToolTipText(LPMain
					.getTextRespectUISPr("fert.offeneags.reihen.tooltip"));

			wcbReihen.addActionListener(this);
			wcbReihen.setMinimumSize(new Dimension(200, Defaults.getInstance()
					.getControlHeight()));
			wcbReihen.setPreferredSize(new Dimension(200, Defaults
					.getInstance().getControlHeight()));

			panelQueryOffeneAGs.getToolBar().getToolsPanelCenter()
					.add(wcbReihen);
			setComponentAt(IDX_PANEL_AUSWAHL, panelQueryOffeneAGs);

		}
	}

	public InternalFrameFertigung getInternalFrameFertigung() {
		return (InternalFrameFertigung) getInternalFrame();
	}

	public void setKeyWasForLockMe() {
		Object oKey = panelQueryOffeneAGs.getSelectedId();

		if (oKey != null) {
			getInternalFrame().setKeyWasForLockMe(oKey.toString());
		} else {
			getInternalFrame().setKeyWasForLockMe(null);
		}
	}

	public void lPEventItemChanged(ItemChangedEvent e) throws Throwable {
		if (e.getID() == ItemChangedEvent.ITEM_CHANGED) {
			if (e.getSource() == panelQueryOffeneAGs) {
				if (panelQueryOffeneAGs.getSelectedId() != null) {
					getInternalFrameFertigung().setKeyWasForLockMe(
							panelQueryOffeneAGs.getSelectedId() + "");

					if (panelQueryOffeneAGs.getSelectedId() == null) {
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
		}

		else if (e.getID() == ItemChangedEvent.ACTION_SPECIAL_BUTTON) {

		} else if ((e.getID() == ItemChangedEvent.ACTION_NEW)
				|| (e.getID() == ItemChangedEvent.ACTION_MY_OWN_NEW)) {
			// btnnew: einen neuen machen.
			if (e.getSource() == panelQueryOffeneAGs) {
				// goto AG

				if (panelQueryOffeneAGs.getSelectedId() != null) {

					LossollarbeitsplanDto saDto = DelegateFactory
							.getInstance()
							.getFertigungDelegate()
							.lossollarbeitsplanFindByPrimaryKey(
									(Integer) panelQueryOffeneAGs
											.getSelectedId());

					FilterKriterium[] kriterien = new FilterKriterium[1];
					kriterien[0] = new FilterKriterium("flrlos.i_id", true,
							saDto.getLosIId() + "",
							FilterKriterium.OPERATOR_EQUAL, false);

					getInternalFrameFertigung()
							.geheZu(InternalFrameFertigung.IDX_TABBED_PANE_LOS,
									getInternalFrameFertigung()
											.getTabbedPaneLos().IDX_ARBEITSPLAN,
									saDto.getLosIId(), saDto.getIId(),
									kriterien);

				}

			}
		}

		else if (e.getID() == ItemChangedEvent.ACTION_TABLE_SELECTION_CHANGED) {
			if (wcbReihen.isSelected()) {
				if (panelQueryOffeneAGs.getCurrentSortierKriterien() != null
						&& vergleicheSortReihung(
								panelQueryOffeneAGs
										.getCurrentSortierKriterien(),
								getAlSortFuerReihung()) == true) {

				} else {
					wcbReihen.setSelected(false);
					plusMinusButtonsSchalten(false);
				}
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_POSITION_VONNNACHNMINUS1
				|| e.getID() == ItemChangedEvent.ACTION_POSITION_VONNNACHNPLUS1) {
			if (e.getSource() == panelQueryOffeneAGs) {
				int iPos = panelQueryOffeneAGs.getTable().getSelectedRow();

				// wenn die Position nicht die erste ist
				if (iPos > 0) {
					Integer iIdPosition = (Integer) panelQueryOffeneAGs
							.getSelectedId();

					if (e.getID() == ItemChangedEvent.ACTION_POSITION_VONNNACHNMINUS1) {
						DelegateFactory.getInstance().getFertigungDelegate()
								.offenAgsUmreihen(iIdPosition, false);
					} else {
						DelegateFactory.getInstance().getFertigungDelegate()
								.offenAgsUmreihen(iIdPosition, true);
					}

					// die Liste neu anzeigen und den richtigen Datensatz
					// markieren

					panelQueryOffeneAGs.setSelectedId(iIdPosition);

					panelQueryOffeneAGs
							.setCurrentSortierKriterien(getAlSortFuerReihung());
					panelQueryOffeneAGs.eventYouAreSelected(false);

				}

			}
		}

	}

	private boolean vergleicheSortReihung(ArrayList<SortierKriterium> al1,
			ArrayList<SortierKriterium> al2) {

		if (al1.size() == al2.size()) {

			for (int i = 0; i < al1.size(); i++) {

				if (!al1.get(i).kritName.equals(al2.get(i).kritName)) {
					return false;
				}

			}
			return true;

		} else {
			return false;
		}

	}

	private void refreshTitle() {
		getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_OHRWASCHLUNTEN,
				LPMain.getInstance().getTextRespectUISPr("fert.offeneags"));
		getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_OHRWASCHLOBEN,
				((PanelBasis) this.getSelectedComponent()).getAdd2Title());

	}

	public void lPEventObjectChanged(ChangeEvent e) throws Throwable {
		super.lPEventObjectChanged(e);
		int selectedIndex = this.getSelectedIndex();

		if (selectedIndex == IDX_PANEL_AUSWAHL) {
			createAuswahl();
			panelQueryOffeneAGs.eventYouAreSelected(false);
			if (panelQueryOffeneAGs.getSelectedId() == null) {
				getInternalFrame().enableAllOberePanelsExceptMe(this,
						IDX_PANEL_AUSWAHL, false);
			}
			panelQueryOffeneAGs.updateButtons();
		}

		refreshTitle();
	}

	private String sortReihung1 = "flrmaschine.c_identifikationsnr";
	private String sortReihung2 = "flroffeneags.t_agbeginn";
	private String sortReihung3 = "flroffeneags.i_maschinenversatz_ms";
	private String sortReihung4 = "flroffeneags.flrlos.c_nr";

	private ArrayList<SortierKriterium> getAlSortFuerReihung() {

		ArrayList<SortierKriterium> alSortFuerReihung = new ArrayList<SortierKriterium>();

		SortierKriterium sk1 = new SortierKriterium(sortReihung1, true, "ASC");
		alSortFuerReihung.add(sk1);
		SortierKriterium sk2 = new SortierKriterium(sortReihung2, true, "ASC");
		alSortFuerReihung.add(sk2);
		SortierKriterium sk3 = new SortierKriterium(sortReihung3, true, "ASC");
		alSortFuerReihung.add(sk3);
		SortierKriterium sk4 = new SortierKriterium(sortReihung4, true, "ASC");
		alSortFuerReihung.add(sk4);

		return alSortFuerReihung;
	}

	protected void lPActionEvent(java.awt.event.ActionEvent e) throws Throwable {
		if (e.getActionCommand().equals(MENUE_ACTION_MASCHINE_MATERIAL)) {
			String add2Title = "Personalliste";
			getInternalFrame().showReportKriterien(
					new ReportMaschineUndMaterial(getInternalFrameFertigung(),
							add2Title));

		} else if (e.getSource().equals(wcbReihen)) {

			if (wcbReihen.isSelected()) {
				// PJ18715 Nach Maschine/AG-Beginn/VersatzMS/Losnr sortieren und
				// Pfeil auf/ab anzeigen

				panelQueryOffeneAGs
						.setCurrentSortierKriterien(getAlSortFuerReihung());
				panelQueryOffeneAGs.eventYouAreSelected(false);
				plusMinusButtonsSchalten(true);

			} else {
				plusMinusButtonsSchalten(false);
			}

		}
	}

	public void plusMinusButtonsSchalten(boolean bEnabled) {
		LPButtonAction minus = panelQueryOffeneAGs.getHmOfButtons().get(
				PanelBasis.ACTION_POSITION_VONNNACHNMINUS1);
		LPButtonAction plus = panelQueryOffeneAGs.getHmOfButtons().get(
				PanelBasis.ACTION_POSITION_VONNNACHNPLUS1);
		minus.getButton().setEnabled(bEnabled);
		plus.getButton().setEnabled(bEnabled);
		minus.getButton().setVisible(bEnabled);
		plus.getButton().setVisible(bEnabled);

	}

	public javax.swing.JMenuBar getJMenuBar() throws Throwable {
		setName("TabbedPaneOffeneAG");
		if (wrapperMenuBar == null) {

			wrapperMenuBar = new WrapperMenuBar(this);

			JMenuItem menuItemMaschineMaterial = new JMenuItem(LPMain
					.getInstance().getTextRespectUISPr(
							"fert.report.maschineundmaterial"));

			menuItemMaschineMaterial.addActionListener(this);

			menuItemMaschineMaterial
					.setActionCommand(MENUE_ACTION_MASCHINE_MATERIAL);
			JMenu jmJournal = (JMenu) wrapperMenuBar
					.getComponent(WrapperMenuBar.MENU_JOURNAL);
			jmJournal.add(menuItemMaschineMaterial);

		}

		return wrapperMenuBar;

	}

}
