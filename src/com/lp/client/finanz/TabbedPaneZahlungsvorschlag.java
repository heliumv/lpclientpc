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
package com.lp.client.finanz;

import javax.swing.event.ChangeEvent;

import com.lp.client.frame.LockStateValue;
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
import com.lp.client.pc.LPButtonAction;
import com.lp.client.pc.LPMain;
import com.lp.client.system.SystemFilterFactory;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.eingangsrechnung.service.ZahlungsvorschlagkriterienDto;
import com.lp.server.eingangsrechnung.service.ZahlungsvorschlaglaufDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.util.Helper;

/**
 * <p>
 * <I>Diese Klasse kuemmert sich um den Zahlungsvorschlag.</I>
 * </p>
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2008
 * </p>
 * <p>
 * Erstellungsdatum <I>06.01.05</I>
 * </p>
 * <p>
 * </p>
 * 
 * @author Martin Bluehweis
 * @version $Revision: 1.5 $
 */

public class TabbedPaneZahlungsvorschlag extends TabbedPane {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private PanelQuery panelQueryZVLauf = null;
	private PanelQuery panelQueryZV = null;
	private PanelZahlungsvorschlag panelDetailZV = null;
	private PanelSplit panelSplitZV = null;

	private PanelDialogKriterienZahlungsvorschlag panelDialogKriterienZV = null;

	private final static int IDX_0_ZVLAUF = 0;
	private final static int IDX_1_ZV = 1;

	private ZahlungsvorschlaglaufDto zvlaufDto = null;

	private final static String ACTION_SPECIAL_REMOVE_ZV = PanelBasis.LEAVEALONE
			+ "_action_special_remove_zahlungsvorschlag";
	private final static String ACTION_SPECIAL_EXPORTIERE_ZV = PanelBasis.LEAVEALONE
			+ "_action_special_exportiere_zahlungsvorschlag";

	public TabbedPaneZahlungsvorschlag(InternalFrame internalFrameI)
			throws Throwable {
		super(
				internalFrameI,
				LPMain.getTextRespectUISPr("finanz.tab.unten.zahlungsvorschlag.title"));
		jbInit();
		initComponents();
	}

	public ZahlungsvorschlaglaufDto getZVlaufDto() {
		return zvlaufDto;
	}

	public void setZVlaufDto(ZahlungsvorschlaglaufDto zvlaufDto)
			throws Throwable {
		this.zvlaufDto = zvlaufDto;
		getPanelQueryZV().setDefaultFilter(
				FinanzFilterFactory.getInstance().createFKZahlungsvorschlag(
						getZVlaufDto()));
		String sTitle = null;
		if (getZVlaufDto() != null) {
			sTitle = Helper.formatTimestamp(getZVlaufDto().getTAnlegen(),
					LPMain.getTheClient().getLocUi());
			LPButtonAction item1 = (LPButtonAction) getPanelQueryZVlauf()
					.getHmOfButtons().get(ACTION_SPECIAL_REMOVE_ZV);
			item1.getButton().setEnabled(true);
			LPButtonAction item2 = (LPButtonAction) getPanelQueryZVlauf()
					.getHmOfButtons().get(ACTION_SPECIAL_EXPORTIERE_ZV);
			item2.getButton().setEnabled(true);
		} else {
			LPButtonAction item1 = (LPButtonAction) getPanelQueryZVlauf()
					.getHmOfButtons().get(ACTION_SPECIAL_REMOVE_ZV);
			item1.getButton().setEnabled(false);
			LPButtonAction item2 = (LPButtonAction) getPanelQueryZVlauf()
					.getHmOfButtons().get(ACTION_SPECIAL_EXPORTIERE_ZV);
			item2.getButton().setEnabled(false);
			sTitle = "";
		}
		getInternalFrame()
				.setLpTitle(InternalFrame.TITLE_IDX_AS_I_LIKE, sTitle);
	}

	/**
	 * jbInit.
	 * 
	 * @throws Throwable
	 */
	private void jbInit() throws Throwable {
		// 1 tab oben: ZV Laeufe; lazy loading
		insertTab(LPMain.getTextRespectUISPr("finanz.tab.oben.zvlaeufe.title"),
				null, null,
				LPMain.getTextRespectUISPr("finanz.tab.oben.zvlaeufe.tooltip"),
				IDX_0_ZVLAUF);
		// 2 tab oben: ER's; lazy loading
		insertTab(
				LPMain.getTextRespectUISPr("finanz.tab.oben.offeneposten.title"),
				null,
				null,
				LPMain.getTextRespectUISPr("finanz.tab.oben.offeneposten.tooltip"),
				IDX_1_ZV);
		// default selektierung
		setSelectedComponent(getPanelQueryZVlauf());
		// refresh
		getPanelQueryZVlauf().eventYouAreSelected(false);
		// Listener
		// damit gleich eine selektiert ist

		ItemChangedEvent it = new ItemChangedEvent(getPanelQueryZVlauf(),
				ItemChangedEvent.ITEM_CHANGED);
		lPEventItemChanged(it);
		addChangeListener(this);
		getInternalFrame().addItemChangedListener(this);
	}

	private PanelSplit getPanelSplitZV() throws Throwable {
		if (panelSplitZV == null) {
			panelSplitZV = new PanelSplit(getInternalFrame(),
					getPanelDetailZV(), getPanelQueryZV(), 250);
			setComponentAt(IDX_1_ZV, panelSplitZV);
		}
		return panelSplitZV;
	}

	private PanelQuery getPanelQueryZVlauf() throws Throwable {
		if (panelQueryZVLauf == null) {
			String[] aWhichButtonIUseZVlauf = { PanelBasis.ACTION_NEW };
			FilterKriterium[] filtersZVlauf = SystemFilterFactory.getInstance()
					.createFKMandantCNr();

			panelQueryZVLauf = new PanelQuery(null, filtersZVlauf,
					QueryParameters.UC_ID_ZAHLUNGSVORSCHLAGLAUF,
					aWhichButtonIUseZVlauf, getInternalFrame(),
					LPMain.getTextRespectUISPr("finanz.zahlungsvorschlag"),
					true);
			panelQueryZVLauf.createAndSaveAndShowButton(
					"/com/lp/client/res/delete2.png",
					"Zahlungsvorschlag l\u00F6schen", ACTION_SPECIAL_REMOVE_ZV,
					RechteFac.RECHT_FB_FINANZ_CUD);
			panelQueryZVLauf
					.createAndSaveAndShowButton(
							"/com/lp/client/res/disk_blue.png",
							"Zahlungsvorschlag exportieren",
							ACTION_SPECIAL_EXPORTIERE_ZV,
							RechteFac.RECHT_FB_FINANZ_CUD);
			setComponentAt(IDX_0_ZVLAUF, panelQueryZVLauf);
		}
		return panelQueryZVLauf;
	}

	public void lPEventItemChanged(ItemChangedEvent eI) throws Throwable {
		if (eI.getID() == ItemChangedEvent.ITEM_CHANGED) {
			if (eI.getSource() == getPanelQueryZVlauf()) {
				Object key = ((ISourceEvent) eI.getSource()).getIdSelected();
				holeZVlaufDto(key);
				if (key == null) {
					getInternalFrame().enableAllOberePanelsExceptMe(this,
							IDX_0_ZVLAUF, false);
					getPanelQueryZVlauf().updateButtons(
							new LockStateValue(PanelBasis.LOCK_FOR_EMPTY));
				} else {
					getInternalFrame().enableAllOberePanelsExceptMe(this,
							IDX_0_ZVLAUF, true);
					getPanelQueryZVlauf()
							.updateButtons(
									new LockStateValue(
											(PanelBasis.LOCK_IS_NOT_LOCKED)));
				}
			} else if (eI.getSource() == getPanelQueryZV()) {
				Object key = ((ISourceEvent) eI.getSource()).getIdSelected();
				getPanelDetailZV().setKeyWhenDetailPanel(key);
				getPanelDetailZV().eventYouAreSelected(false);
				getPanelQueryZV().updateButtons();
			}
		} else if (eI.getID() == ItemChangedEvent.ACTION_NEW) {
			if (eI.getSource() == getPanelQueryZVlauf()) {
				getInternalFrame().showPanelDialog(getPanelDialogKriterienZV());
			}
		} else if (eI.getID() == ItemChangedEvent.ACTION_SPECIAL_BUTTON) {
			String sAspectInfo = ((ISourceEvent) eI.getSource()).getAspect();
			if (eI.getSource() == getPanelQueryZVlauf()) {
				if (sAspectInfo.equals(ACTION_SPECIAL_REMOVE_ZV)) {
					if (getZVlaufDto() != null) {
						DelegateFactory
								.getInstance()
								.getEingangsrechnungDelegate()
								.removeZahlungsvorschlaglauf(
										getZVlaufDto().getIId());
						setZVlaufDto(null);
					}
					getPanelQueryZVlauf().eventYouAreSelected(false);
				} else if (sAspectInfo.equals(ACTION_SPECIAL_EXPORTIERE_ZV)) {
					if (getZVlaufDto() != null) {
						// Parameter holen
						ParametermandantDto parameter = DelegateFactory
								.getInstance()
								.getParameterDelegate()
								.getMandantparameter(
										LPMain.getTheClient().getMandant(),
										ParameterFac.KATEGORIE_EINGANGSRECHNUNG,
										ParameterFac.PARAMETER_ZAHLUNGSVORSCHLAG_EXPORTZIEL);
						// Exportdaten
						String sExport = DelegateFactory
								.getInstance()
								.getEingangsrechnungDelegate()
								.exportiereZahlungsvorschlaglauf(
										getZVlaufDto().getIId());
						if (sExport != null) {
							// und speichern
							LPMain.getInstance().saveFile(getInternalFrame(),
									parameter.getCWert(), sExport.getBytes(),
									false);
						} else {
							DialogFactory
									.showModalDialog(
											LPMain.getTextRespectUISPr("lp.hint"),
											LPMain.getTextRespectUISPr("fb.export.keinebelegezuexportieren"));
						}
					}
				}
			}
		} else if (eI.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			if (eI.getSource() == getPanelQueryZVlauf()) {
				setSelectedComponent(panelSplitZV);
			}
		} else if (eI.getID() == ItemChangedEvent.ACTION_GOTO_MY_DEFAULT_QP) {
			if (eI.getSource() == getPanelDetailZV()) {
				setKeyWasForLockMe();
				getPanelSplitZV().eventYouAreSelected(false);
			}
		} else if (eI.getID() == ItemChangedEvent.ACTION_DISCARD) {
			if (eI.getSource() == getPanelDetailZV()) {
				getPanelSplitZV().eventYouAreSelected(false);
			}
		} else if (eI.getID() == ItemChangedEvent.ACTION_SAVE) {
			if (eI.getSource() == panelDetailZV) {
				Object oKey = panelDetailZV.getKeyWhenDetailPanel();
				panelQueryZV.eventYouAreSelected(false);
				panelQueryZV.setSelectedId(oKey);
				panelSplitZV.eventYouAreSelected(false);
			}
		} else if (eI.getID() == ItemChangedEvent.ACTION_KRITERIEN_HAVE_BEEN_SELECTED) {
			if (eI.getSource() == getPanelDialogKriterienZV()) {
				// ZV durchfuehren
				ZahlungsvorschlagkriterienDto krit = getPanelDialogKriterienZV()
						.getKriterienDto();
				if (krit != null) {
					Integer iId = DelegateFactory.getInstance()
							.getEingangsrechnungDelegate()
							.createZahlungsvorschlag(krit);
					getPanelQueryZVlauf().eventYouAreSelected(false);
					getPanelQueryZVlauf().setSelectedId(iId);
					if (iId != null) {
						setZVlaufDto(DelegateFactory.getInstance()
								.getEingangsrechnungDelegate()
								.zahlungsvorschlaglaufFindByPrimaryKey(iId));
						// Filter am 2. panel aktualisieren
						getPanelQueryZV().setDefaultFilter(
								FinanzFilterFactory.getInstance()
										.createFKZahlungsvorschlag(
												getZVlaufDto()));
						// und aus 2. panel umschalten und dieses aktualisieren
						setSelectedComponent(getPanelSplitZV());
						getPanelSplitZV().eventYouAreSelected(false);
					} else {
						//Es wurden keine offenen ERs gefunden
						DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.info"), LPMain.getTextRespectUISPr("fb.zahlungsvorschlag.keineoffeneners"));
					}
				} else {
					getPanelQueryZVlauf().eventYouAreSelected(false);
				}
			}
		}
	}

	/**
	 * getPanelQueryZV.
	 * 
	 * @return PanelQuery
	 * @throws Throwable
	 */
	private PanelQuery getPanelQueryZV() throws Throwable {
		if (panelQueryZV == null) {
			String[] aWhichButtonIUseZV = {};
			FilterKriterium[] filtersZV = FinanzFilterFactory.getInstance()
					.createFKZahlungsvorschlag(getZVlaufDto());

			panelQueryZV = new PanelQuery(null, filtersZV,
					QueryParameters.UC_ID_ZAHLUNGSVORSCHLAG,
					aWhichButtonIUseZV, getInternalFrame(),
					LPMain.getTextRespectUISPr("finanz.zahlungsvorschlag"),
					true);
		}
		return panelQueryZV;
	}

	private PanelZahlungsvorschlag getPanelDetailZV() throws Throwable {
		if (panelDetailZV == null) {
			panelDetailZV = new PanelZahlungsvorschlag(getInternalFrame(),
					LPMain.getTextRespectUISPr("finanz.zahlungsvorschlag"),
					null, this);
		}
		return panelDetailZV;
	}

	public void lPEventObjectChanged(ChangeEvent e) throws Throwable {
		super.lPEventObjectChanged(e);
		int selectedIndex = this.getSelectedIndex();
		if (selectedIndex == IDX_0_ZVLAUF) {
			getPanelQueryZVlauf().eventYouAreSelected(false);
		} else if (selectedIndex == IDX_1_ZV) {
			getPanelSplitZV().eventYouAreSelected(false);
			// Gesamtwert aktualisieren
			getPanelDetailZV().updateGesamtwert();
		}
	}

	protected void refreshPanelQueryZV() throws Throwable {
		getPanelQueryZV().eventYouAreSelected(false);
	}

	protected void lPActionEvent(java.awt.event.ActionEvent e) throws Throwable {
	}

	public Integer getSelectedIIdZVlauf() {
		return (Integer) panelQueryZVLauf.getSelectedId();
	}

	/**
	 * Einen ausgewaehlten ZV lauf holen und die Panels aktualisieren
	 * 
	 * @param key
	 *            Object
	 * @throws Throwable
	 */
	private void holeZVlaufDto(Object key) throws Throwable {
		if (key != null) {
			ZahlungsvorschlaglaufDto dto = DelegateFactory.getInstance()
					.getEingangsrechnungDelegate()
					.zahlungsvorschlaglaufFindByPrimaryKey((Integer) key);
			setZVlaufDto(dto);
			getInternalFrame().setKeyWasForLockMe(key.toString());
		} else {
			setZVlaufDto(null);
		}
	}

	public void setKeyWasForLockMe() throws Throwable {
		Object oKey = getPanelQueryZVlauf().getSelectedId();
		if (oKey != null) {
			getInternalFrame().setKeyWasForLockMe(oKey.toString());
		} else {
			getInternalFrame().setKeyWasForLockMe(null);
		}
	}

	protected javax.swing.JMenuBar getJMenuBar() throws Throwable {
		WrapperMenuBar wmb = new WrapperMenuBar(this);
		return wmb;
	}

	private PanelDialogKriterienZahlungsvorschlag getPanelDialogKriterienZV()
			throws Throwable {
		if (panelDialogKriterienZV == null) {
			panelDialogKriterienZV = new PanelDialogKriterienZahlungsvorschlag(
					getInternalFrame(), "");
		}
		return panelDialogKriterienZV;
	}

	public Object getInseratDto() {
		return zvlaufDto;
	}

}
