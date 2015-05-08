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

import javax.swing.event.ChangeEvent;

import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQuery;
import com.lp.client.frame.component.TabbedPane;
import com.lp.client.frame.component.WrapperMenuBar;
import com.lp.client.pc.LPMain;
import com.lp.client.system.pflege.PanelPflegeNew;
import com.lp.client.util.fastlanereader.gui.QueryType;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;

@SuppressWarnings("static-access")
/**
 * <p>Ueberschrift: </p>
 * <p>Beschreibung: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Organisation: </p>
 * @author unbekannt
 * @version $Revision: 1.3 $
 */
public class TabbedPanePflege extends TabbedPane {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private PanelQuery panelQueryTheJudge = null;

	private final static int IDX_PFLEGENEW = 1;
	private final static int IDX_PFLEGE = 0;
	private final static int IDX_PFLEGE_INTERN = 2;

	private PanelPflegeNew panelPflegeNew = null;
	private PanelPflege panelPflege = null;
	private PanelPflegeIntern panelPflegeIntern = null;

	public TabbedPanePflege(InternalFrame internalFrameI) throws Throwable {
		super(internalFrameI, LPMain.getInstance().getTextRespectUISPr(
				"lp.pflege"));
		jbInit();
		initComponents();
	}

	private void jbInit() throws Throwable {
		// Tab 1: Rechte
		insertTab(LPMain.getInstance().getTextRespectUISPr("lp.pflege"), null,
				null, LPMain.getInstance().getTextRespectUISPr("lp.pflege"),
				IDX_PFLEGE);
		insertTab(LPMain.getInstance().getTextRespectUISPr("lp.pflege.neu"),
				null, null,
				LPMain.getInstance().getTextRespectUISPr("lp.pflege.neu"),
				IDX_PFLEGENEW);

		if (LPMain.getInstance().isLPAdmin()) {
			insertTab(
					LPMain.getInstance()
							.getTextRespectUISPr("lp.pflege.intern"), null,
					null,
					LPMain.getInstance()
							.getTextRespectUISPr("lp.pflege.intern"),
					IDX_PFLEGE_INTERN);
		}
		setSelectedComponent(getPanelQueryTheJudge());
		// refresh
		getPanelQueryTheJudge().eventYouAreSelected(false);
		// damit gleich eine selektiert ist
		ItemChangedEvent it = new ItemChangedEvent(getPanelQueryTheJudge(),
				ItemChangedEvent.ITEM_CHANGED);
		lPEventItemChanged(it);
		// Listener
		addChangeListener(this);
		getInternalFrame().addItemChangedListener(this);
	}

	private PanelQuery getPanelQueryTheJudge() throws Throwable {
		if (panelQueryTheJudge == null) {
			String[] aWhichButtonIUseQPTheJudge = { PanelBasis.ACTION_REFRESH };

			QueryType[] qtTheJudge = null;
			FilterKriterium[] filtersTheJudge = null;

			panelQueryTheJudge = new PanelQuery(qtTheJudge, filtersTheJudge,
					QueryParameters.UC_ID_THEJUDGE, aWhichButtonIUseQPTheJudge,
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("pers.benutzer.gesperrt"),
					true);
			this.setComponentAt(IDX_PFLEGE, panelQueryTheJudge);
		}
		return panelQueryTheJudge;
	}

	public void lPEventItemChanged(ItemChangedEvent eI) throws Throwable {

	}

	public void lPEventObjectChanged(ChangeEvent e) throws Throwable {
		super.lPEventObjectChanged(e);
		int selectedIndex = this.getSelectedIndex();

		switch (selectedIndex) {
		case IDX_PFLEGENEW: {
			refreshPanelPflegeNew();
			panelPflegeNew.eventYouAreSelected(false);

			// im QP die Buttons setzen.
			panelPflegeNew.updateButtons();

			break;
		}
		case IDX_PFLEGE: {
			refreshPanelPflege();
			panelPflege.eventYouAreSelected(false);

			// im QP die Buttons setzen.
			panelPflege.updateButtons();

			break;
		}
		case IDX_PFLEGE_INTERN: {
			refreshPanelPflegeIntern();
			panelPflegeIntern.eventYouAreSelected(false);

			// im QP die Buttons setzen.
			panelPflegeIntern.updateButtons();

			break;
		}
		}
	}

	protected void lPActionEvent(java.awt.event.ActionEvent e) {
		// nothing here
	}

	private void refreshPanelPflege() throws Throwable {
		if (panelPflege == null) {
			panelPflege = new PanelPflege(getInternalFrame(), LPMain
					.getInstance().getTextRespectUISPr("lp.pflege"));
			setComponentAt(IDX_PFLEGE, panelPflege);

			// liste soll sofort angezeigt werden
			panelPflege.eventYouAreSelected(true);
		}
	}

	private void refreshPanelPflegeNew() throws Throwable {
		if (panelPflegeNew == null) {
			panelPflegeNew = new PanelPflegeNew(getInternalFrame(), LPMain
					.getInstance().getTextRespectUISPr("lp.pflege.neu"));
			setComponentAt(IDX_PFLEGENEW, panelPflegeNew);

			// liste soll sofort angezeigt werden
			panelPflegeNew.eventYouAreSelected(true);
		}
	}

	private void refreshPanelPflegeIntern() throws Throwable {
		if (panelPflegeIntern == null) {
			panelPflegeIntern = new PanelPflegeIntern(getInternalFrame(), LPMain
					.getInstance().getTextRespectUISPr("lp.pflege.intern"));
			setComponentAt(IDX_PFLEGE_INTERN, panelPflegeIntern);

			// liste soll sofort angezeigt werden
			panelPflegeIntern.eventYouAreSelected(true);
		}
	}

	protected javax.swing.JMenuBar getJMenuBar() throws Throwable {
		return new WrapperMenuBar(this);
	}
}
