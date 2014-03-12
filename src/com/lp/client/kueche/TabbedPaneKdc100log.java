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
package com.lp.client.kueche;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.event.ChangeEvent;

import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelQuery;
import com.lp.client.frame.component.TabbedPane;
import com.lp.client.frame.component.WrapperComboBox;
import com.lp.client.frame.component.WrapperMenuBar;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.system.service.SystemFac;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;

@SuppressWarnings("static-access")
/*
 * <p>&UUml;berschrift: </p> <p>Beschreibung: </p> <p>Copyright: Copyright (c)
 * 2004</p> <p>Organisation: </p>
 * 
 * @author Christian Kollmann
 * 
 * @version $Revision: 1.3 $
 */
public class TabbedPaneKdc100log extends TabbedPane {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private PanelQuery panelQueryKdc10log = null;

	private final static int IDX_PANEL_LOG = 0;

	private WrapperMenuBar wrapperMenuBar = null;

	private WrapperComboBox wcbFilter = new WrapperComboBox();

	private static Integer FILTER_ALLE = 1;
	private static Integer FILTER_FEHLER = 2;

	public TabbedPaneKdc100log(InternalFrame internalFrameI) throws Throwable {
		super(internalFrameI, LPMain.getInstance().getTextRespectUISPr(
				"lp.protokoll"));

		jbInit();
		initComponents();
	}

	public InternalFrameKueche getInternalFrameZutritt() {
		return (InternalFrameKueche) getInternalFrame();
	}

	private void jbInit() throws Throwable {
		// Kollektiv

		// 1 tab oben: QP1 PartnerFLR; lazy loading
		insertTab(LPMain.getInstance().getTextRespectUISPr("lp.protokoll"),
				null, null, LPMain.getInstance().getTextRespectUISPr(
						"lp.protokoll"), IDX_PANEL_LOG);

		FilterKriterium[] kriterien = new FilterKriterium[1];
		kriterien[0] = new FilterKriterium("c_art", true,
				"('"+SystemFac.PROTOKOLL_ART_INFO+"')",
				FilterKriterium.OPERATOR_NOT + " "
						+ FilterKriterium.OPERATOR_IN, false);
		
		panelQueryKdc10log = new PanelQuery(null, kriterien,
				QueryParameters.UC_ID_KDC100LOG, null, getInternalFrame(),
				LPMain.getInstance().getTextRespectUISPr("kue.kdc100log"), true);

		panelQueryKdc10log.befuellePanelFilterkriterienDirekt(
				KuecheFilterFactory.getInstance()
						.createFKDKdc100logSeriennummer(), KuecheFilterFactory
						.getInstance().createFKDKdc100logKommentar());

		wcbFilter.setMandatoryField(true);
		Map m = new TreeMap();
		m.put(FILTER_ALLE, LPMain.getTextRespectUISPr("label.alle"));
		m.put(FILTER_FEHLER, LPMain.getTextRespectUISPr("lp.error"));

		wcbFilter.setMap(m);
		wcbFilter.setKeyOfSelectedItem(FILTER_FEHLER);

		wcbFilter.addActionListener(this);
		panelQueryKdc10log.getPanelButtons().add(
				wcbFilter,
				new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
						GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
						new Insets(4, 0, 0, 40), 200, 0));

		setComponentAt(IDX_PANEL_LOG, panelQueryKdc10log);

		// p//anelQueryZutrittsprotokoll.befuellePanelFilterkriterienDirekt(
		// ArtikelFilterFactory.
		// getInstance().createFKDArtikelnummer(getInternalFrame()),
		// ArtikelFilterFactory.getInstance().createFKDVolltextsuche());
		setComponentAt(IDX_PANEL_LOG, panelQueryKdc10log);

		// Itemevents an MEIN Detailpanel senden kann.
		this.addChangeListener(this);
		getInternalFrame().addItemChangedListener(this);

	}

	public void lPEventItemChanged(ItemChangedEvent e) throws Throwable {

		if (e.getID() == ItemChangedEvent.ITEM_CHANGED) {
		} else if (e.getID() == ItemChangedEvent.ACTION_YOU_ARE_SELECTED) {
		} else if (e.getID() == ItemChangedEvent.ACTION_NEW) {
		} else if (e.getID() == ItemChangedEvent.ACTION_UPDATE) {
		} else if (e.getID() == ItemChangedEvent.ACTION_DISCARD) {
		} else if (e.getID() == ItemChangedEvent.ACTION_SAVE) {
		} else if (e.getID() == ItemChangedEvent.ACTION_GOTO_MY_DEFAULT_QP) {

		}

	}

	public void lPEventObjectChanged(ChangeEvent e) throws Throwable {
		super.lPEventObjectChanged(e);
		int selectedIndex = this.getSelectedIndex();

		if (selectedIndex == IDX_PANEL_LOG) {
			panelQueryKdc10log.eventYouAreSelected(false);
			if (panelQueryKdc10log.getSelectedId() == null) {
				getInternalFrame().enableAllOberePanelsExceptMe(this,
						IDX_PANEL_LOG, false);
			}
			panelQueryKdc10log.updateButtons();

		}

	}

	protected void lPActionEvent(java.awt.event.ActionEvent e) throws Throwable {

		FilterKriterium[] kriterien = null;

		if (e.getSource().equals(wcbFilter)) {

			if (wcbFilter.getKeyOfSelectedItem().equals(FILTER_ALLE)) {
				kriterien = null;

			} else {
				kriterien = new FilterKriterium[1];
				kriterien[0] = new FilterKriterium("c_art", true,
						"('"+SystemFac.PROTOKOLL_ART_INFO+"')",
						FilterKriterium.OPERATOR_NOT + " "
								+ FilterKriterium.OPERATOR_IN, false);

			}

			panelQueryKdc10log.setDefaultFilter(kriterien);

			panelQueryKdc10log.eventYouAreSelected(false);

		}

	}

	public javax.swing.JMenuBar getJMenuBar() throws Throwable {
		if (wrapperMenuBar == null) {

			wrapperMenuBar = new WrapperMenuBar(this);

			JMenu journal = (JMenu) wrapperMenuBar
					.getComponent(WrapperMenuBar.MENU_JOURNAL);

		}
		return wrapperMenuBar;

	}

}
