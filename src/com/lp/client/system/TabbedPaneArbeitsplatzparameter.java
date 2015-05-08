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
package com.lp.client.system;

import java.awt.event.ActionEvent;

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
import com.lp.client.pc.LPMain;
import com.lp.server.system.service.ArbeitsplatzDto;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;

@SuppressWarnings("static-access")
/**
 * <p>Diese Klasse kuemmert sich um Parameter.</p>
 *
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 *
 * <p>Erstellungsdatum <I>01. 06. 05</I></p>
 *
 * @author  Uli Walch
 * @version $Revision: 1.4 $
 */
public class TabbedPaneArbeitsplatzparameter extends TabbedPane {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private PanelQuery panelQueryAuswahl = null;

	private PanelArbeitsplatz panelDetailArbeitsplatz = null;

	private PanelSplit panelSplitArbeitsplatzparameter = null;
	private PanelQuery panelQueryArbeitsplatzparameter = null;
	private PanelBasis panelDetailArbeitsplatzparameter = null;

	// Reihenfolge der Panels
	private static final int IDX_PANEL_AUSWAHL = 0;
	private static final int IDX_PANEL_BESCHREIBUNG = 1;
	private static final int IDX_PANEL_ARBEITSPLATZPARAMETER = 2;

	public TabbedPaneArbeitsplatzparameter(InternalFrame internalFrameI)
			throws Throwable {

		super(internalFrameI, LPMain.getInstance().getTextRespectUISPr(
				"lp.arbeitsplatzparameter"));

		jbInit();
		initComponents();
	}

	private void jbInit() throws Throwable {
		insertTab(LPMain.getInstance().getTextRespectUISPr("lp.auswahl"), null,
				null, LPMain.getInstance().getTextRespectUISPr("lp.auswahl"),
				IDX_PANEL_AUSWAHL);

		insertTab(LPMain.getInstance().getTextRespectUISPr("lp.detail"), null,
				null, LPMain.getInstance().getTextRespectUISPr("lp.detail"),
				IDX_PANEL_BESCHREIBUNG);
		insertTab(LPMain.getInstance().getTextRespectUISPr("lp.parameter"),
				null, null,
				LPMain.getInstance().getTextRespectUISPr("lp.parameter"),
				IDX_PANEL_ARBEITSPLATZPARAMETER);

		createAuswahl();

		panelQueryAuswahl.eventYouAreSelected(false);

		try {
			ArbeitsplatzDto arbeitsplatzDto = DelegateFactory
					.getInstance()
					.getParameterDelegate()
					.arbeitsplatzFindByCPcname(
							java.net.InetAddress.getLocalHost().getHostName());
			panelQueryAuswahl.setSelectedId(arbeitsplatzDto.getIId());
		} catch (Exception e) {
			// nicht gefunden
		}

		if ((Integer) panelQueryAuswahl.getSelectedId() != null) {
			getInternalFrameSystem()
					.setArbeitsplatzDto(
							DelegateFactory
									.getInstance()
									.getParameterDelegate()
									.arbeitsplatzFindByPrimaryKey(
											(Integer) panelQueryAuswahl
													.getSelectedId()));
		}
		// damit D2 einen aktuellen hat.
		ItemChangedEvent it = new ItemChangedEvent(panelQueryAuswahl,
				ItemChangedEvent.ITEM_CHANGED);
		lPEventItemChanged(it);

		this.addChangeListener(this);
		this.getInternalFrame().addItemChangedListener(this);

	}

	private void createAuswahl() throws Throwable {
		if (panelQueryAuswahl == null) {
			// Artikelauswahlliste
			String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW };
			panelQueryAuswahl = new PanelQuery(null, null,
					QueryParameters.UC_ID_ARBEITSPLATZ, aWhichButtonIUse,
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("lp.auswahl"), true);

			panelQueryAuswahl.befuellePanelFilterkriterienDirekt(
					SystemFilterFactory.getInstance().createFKDPCName(), null);

			setComponentAt(IDX_PANEL_AUSWAHL, panelQueryAuswahl);

		}
	}

	protected void lPActionEvent(ActionEvent e) {
		// nothing here.
	}

	/**
	 * changed: hier wird alles durchlaufen und abgefragt zb. save event,
	 * discard event, wenn ein panel refresht werden soll...
	 * 
	 * @param eI
	 *            ItemChangedEvent
	 * @throws Throwable
	 */
	public void lPEventItemChanged(ItemChangedEvent eI) throws Throwable {

		ItemChangedEvent e = (ItemChangedEvent) eI;

		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			if (e.getSource() == panelQueryAuswahl) {
				Integer iId = (Integer) panelQueryAuswahl.getSelectedId();
				if (iId != null) {
					setSelectedComponent(panelDetailArbeitsplatz);
				}
			}
		} else if (e.getID() == ItemChangedEvent.ITEM_CHANGED) {
			if (e.getSource() == panelQueryArbeitsplatzparameter) {
				Integer iId = (Integer) panelQueryArbeitsplatzparameter
						.getSelectedId();
				panelDetailArbeitsplatzparameter.setKeyWhenDetailPanel(iId);
				panelDetailArbeitsplatzparameter.eventYouAreSelected(false);
				panelQueryArbeitsplatzparameter.updateButtons();
			} else if (e.getSource() == panelQueryAuswahl) {
				if (panelQueryAuswahl.getSelectedId() != null) {
					Object key = ((ISourceEvent) e.getSource()).getIdSelected();

					getInternalFrameSystem().setKeyWasForLockMe(key + "");

					getInternalFrameSystem()
							.setArbeitsplatzDto(
									DelegateFactory
											.getInstance()
											.getParameterDelegate()
											.arbeitsplatzFindByPrimaryKey(
													(Integer) key));
					getInternalFrame().setLpTitle(
							InternalFrame.TITLE_IDX_AS_I_LIKE,
							LPMain.getInstance().getTextRespectUISPr(
									"system.pcname")
									+ ": "
									+ getInternalFrameSystem()
											.getArbeitsplatzDto().getCPcname());

					if (panelQueryAuswahl.getSelectedId() == null) {
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

		}

		else if (eI.getID() == ItemChangedEvent.ACTION_UPDATE) {
			// hier kommt man nach upd im D bei einem 1:n hin.
			if (eI.getSource() == this.panelDetailArbeitsplatzparameter) {
				// im QP die Buttons in den Zustand neu setzen.
				this.panelQueryArbeitsplatzparameter
						.updateButtons(new LockStateValue(
								PanelBasis.LOCK_FOR_NEW));
				;
			}
		}

		else if (e.getID() == ItemChangedEvent.ACTION_DISCARD) {
			if (e.getSource() == panelDetailArbeitsplatz) {
				panelDetailArbeitsplatz.eventYouAreSelected(false);
			} else if(e.getSource() == panelDetailArbeitsplatzparameter) {
				panelQueryArbeitsplatzparameter.eventYouAreSelected(false);
			}
		}

		else if (e.getID() == ItemChangedEvent.ACTION_SAVE) {
			if (e.getSource() == panelDetailArbeitsplatz) {
				panelQueryAuswahl.clearDirektFilter();
				Object oKey = panelDetailArbeitsplatz.getKeyWhenDetailPanel();
				panelQueryAuswahl.eventYouAreSelected(false);
				panelQueryAuswahl.setSelectedId(oKey);
				panelQueryAuswahl.eventYouAreSelected(false);
			} else if (e.getSource() == panelDetailArbeitsplatzparameter) {
				Object oKey = panelDetailArbeitsplatzparameter
						.getKeyWhenDetailPanel();
				panelQueryArbeitsplatzparameter.eventYouAreSelected(false);
				panelQueryArbeitsplatzparameter.setSelectedId(oKey);
				panelSplitArbeitsplatzparameter.eventYouAreSelected(false);

			}
		}

		else if (e.getID() == ItemChangedEvent.ACTION_GOTO_MY_DEFAULT_QP) {
			// aktiviere ein QP ...
			if (e.getSource() == panelDetailArbeitsplatz) {
				getInternalFrame().enableAllPanelsExcept(true);
				this.setSelectedComponent(panelQueryAuswahl);
				setKeyWasForLockMe();
				panelQueryAuswahl.eventYouAreSelected(false);
			} else if (e.getSource() == panelDetailArbeitsplatzparameter) {
				setKeyWasForLockMe();
				if (panelDetailArbeitsplatzparameter.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryArbeitsplatzparameter
							.getId2SelectAfterDelete();
					panelQueryArbeitsplatzparameter.setSelectedId(oNaechster);
				}
				panelSplitArbeitsplatzparameter.eventYouAreSelected(false);
			}
		}

		else if (eI.getID() == ItemChangedEvent.ACTION_NEW) {
			if (e.getSource() == panelQueryAuswahl) {

				createDetail((Integer) panelQueryAuswahl.getSelectedId());
				if (panelQueryAuswahl.getSelectedId() == null) {
					getInternalFrame().enableAllPanelsExcept(true);
				}
				panelDetailArbeitsplatz.eventActionNew(null, true, false);
				setSelectedComponent(panelDetailArbeitsplatz);
			}

			else if (eI.getSource() == panelQueryArbeitsplatzparameter) {
				if (panelQueryArbeitsplatzparameter.getSelectedId() == null) {
					getInternalFrame().enableAllPanelsExcept(true);
				}
				panelDetailArbeitsplatzparameter
						.eventActionNew(eI, true, false);
				panelDetailArbeitsplatzparameter.eventYouAreSelected(false);
				setSelectedComponent(panelSplitArbeitsplatzparameter);
			}
		}
	}

	private InternalFrameSystem getInternalFrameSystem() {
		return (InternalFrameSystem) getInternalFrame();
	}

	public void setKeyWasForLockMe() {
		Object oKey = panelQueryAuswahl.getSelectedId();

		if (oKey != null) {
			getInternalFrame().setKeyWasForLockMe(oKey.toString());
		} else {
			getInternalFrame().setKeyWasForLockMe(null);
		}
	}

	public void lPEventObjectChanged(ChangeEvent e) throws Throwable {
		super.lPEventObjectChanged(e);

		int selectedIndex = this.getSelectedIndex();

		if (selectedIndex == IDX_PANEL_AUSWAHL) {
			createAuswahl();
			panelQueryAuswahl.eventYouAreSelected(false);
			if (panelQueryAuswahl.getSelectedId() == null) {
				getInternalFrame().enableAllOberePanelsExceptMe(this,
						IDX_PANEL_AUSWAHL, false);
			}

			panelQueryAuswahl.updateButtons();
		}

		else if (selectedIndex == IDX_PANEL_BESCHREIBUNG) {
			Integer key = null;
			if (getInternalFrameSystem().getArbeitsplatzDto() != null) {
				key = getInternalFrameSystem().getArbeitsplatzDto().getIId();
			}
			createDetail(key);
			panelDetailArbeitsplatz.eventYouAreSelected(false);

		} else if (selectedIndex == IDX_PANEL_ARBEITSPLATZPARAMETER) {
			createArbeitsplatzparameter((Integer) panelQueryAuswahl
					.getSelectedId());
			panelSplitArbeitsplatzparameter.eventYouAreSelected(false);
			panelQueryArbeitsplatzparameter.updateButtons();
		}
	}

	private void createDetail(Integer key) throws Throwable {
		if (panelDetailArbeitsplatz == null) {
			panelDetailArbeitsplatz = new PanelArbeitsplatz(getInternalFrame(),
					LPMain.getInstance().getTextRespectUISPr("lp.detail"), key);
			setComponentAt(IDX_PANEL_BESCHREIBUNG, panelDetailArbeitsplatz);
		}
	}

	private void createArbeitsplatzparameter(Integer key) throws Throwable {

		if (panelQueryArbeitsplatzparameter == null) {
			String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW };

			FilterKriterium[] filters = SystemFilterFactory.getInstance()
					.createFKArbeitsplatzparameter(key);

			panelQueryArbeitsplatzparameter = new PanelQuery(null, filters,
					QueryParameters.UC_ID_ARBEITSPLATZPARAMETER,
					aWhichButtonIUse, getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("lp.detail"), true);

			panelDetailArbeitsplatzparameter = new PanelArbeitsplatzparameter(
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("lp.detail"), null);

			panelSplitArbeitsplatzparameter = new PanelSplit(
					getInternalFrame(), panelDetailArbeitsplatzparameter,
					panelQueryArbeitsplatzparameter, 180);

			setComponentAt(IDX_PANEL_ARBEITSPLATZPARAMETER,
					panelSplitArbeitsplatzparameter);
		} else {
			// filter refreshen.
			panelQueryArbeitsplatzparameter
					.setDefaultFilter(SystemFilterFactory.getInstance()
							.createFKArbeitsplatzparameter(key));
		}
	}

	protected javax.swing.JMenuBar getJMenuBar() throws Throwable {
		return new WrapperMenuBar(this);
	}

}
