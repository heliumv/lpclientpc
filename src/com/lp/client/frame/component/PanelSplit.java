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
package com.lp.client.frame.component;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.beans.PropertyVetoException;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import com.lp.client.frame.LockStateValue;

/**
 *
 * <p>
 * Diese Klasse kuemmert sich ein QP mit einem D-Panel.
 * </p>
 *
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2008
 * </p>
 *
 * <p>
 * Erstellung: Vorname Nachname; dd.mm.05
 * </p>
 *
 * <p>
 * @author $Author: christian $
 * </p>
 *
 * @version not attributable Date $Date: 2009/08/11 13:34:37 $
 */
public class PanelSplit extends PanelBasis // implements ItemChangedListener UW
											// am 16.9.05 alle raus
{
	private static final long serialVersionUID = 1L;
	private PanelBasis panelDetail = null;
	private PanelQuery panelQuery = null;
	private JSplitPane paneSplit = null;
	private GridBagLayout gridBagLayoutAll = null;
	private GridBagLayout gridBagLayoutWorkingOn = null;
	private JPanel panelWorkingOn = null;
	private int dividerLocation = 200;
	private InternalFrame internalFrameI = null;

	public PanelSplit(InternalFrame internalFrame, PanelBasis panelDetailI, PanelQuery panelQueryI,
			int dividerLocationI) throws Throwable {

		// titlp: das Toppanel gewinnt hier. ;-)
		super(internalFrame, panelQueryI.getAdd2Title());

		internalFrame = internalFrameI;

		panelDetail = panelDetailI;
		panelQuery = panelQueryI;
		dividerLocation = dividerLocationI;

		jbInit();
		initComponents();
	}

	private void jbInit() throws Exception {
		// panelall
		gridBagLayoutAll = new GridBagLayout();
		this.setLayout(gridBagLayoutAll);

		// panelall.panelworking
		panelWorkingOn = new JPanel();
		gridBagLayoutWorkingOn = new GridBagLayout();
		panelWorkingOn.setLayout(gridBagLayoutWorkingOn);

		this.add(panelWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0, GridBagConstraints.NORTH,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

		// panelall.panelworking.panesplit
		paneSplit = new JSplitPane();
		paneSplit.setDividerSize(3);
		paneSplit.setDividerLocation(dividerLocation);
		paneSplit.setOrientation(JSplitPane.VERTICAL_SPLIT);
		// panelall.panelworking.panesplit
		panelWorkingOn.add(paneSplit, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.NORTH,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

		// panelall.panelworking.panesplit.paneldetail
		paneSplit.setTopComponent(panelQuery);
		// panelall.panelworking.panesplit.panelquery
		paneSplit.setBottomComponent(panelDetail);
	}

	public void enableOneTouchExpandle(boolean enable) {
		if (!enable) {
			paneSplit.setDividerSize(3);
			paneSplit.setOneTouchExpandable(false);
		} else {
			paneSplit.setDividerSize(10);
			paneSplit.setOneTouchExpandable(true);
		}
	}

	public void beOneTouchExpandable() {
		enableOneTouchExpandle(true);
	}

	public JSplitPane getPanelSplit() {
		return paneSplit;
	}

	public void updateButtons(int iAspectI, LockStateValue lockstateValueI) throws Exception {
		// nothing here
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI) throws Throwable {
		// weiterrouten.
		panelQuery.eventYouAreSelected(false);

		// keyemp: hole key, key == null
		Object key = panelQuery.getSelectedId();
		panelDetail.setKeyWhenDetailPanel(key);
		// key setzen wegen lockmeabfrage.
		setKeyWhenDetailPanel(key);
		panelDetail.eventYouAreSelected(bNeedNoYouAreSelectedI);
	}

	protected String getLockMeWer() throws Exception {
		return panelDetail.getLockMeWer();
	}

	public PanelQuery getPanelQuery() {
		return panelQuery;
	}

	public PanelBasis getPanelDetail() {
		return panelDetail;
	}

	/**
	 * btnsave: 0 behandle das ereignis save.
	 *
	 * @param e
	 *            ActionEvent der Event.
	 * @param bNeedNoSaveI
	 *            boolean
	 * @throws Throwable
	 */
	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI) throws Throwable {
		// weiterrouten
		panelDetail.eventActionSave(e, bNeedNoSaveI);
	}

	public void eventActionEscape(ActionEvent e, boolean bNeedNoSaveI) throws Throwable {

		// weiterrouten
		// panelDetail.eventActionEscape(e, bNeedNoSaveI);
	}

	/**
	 * evtvet: Event "Vetoable Window close"; wird null zurueckgegeben, } * so
	 * wird das Modul via dicard beendet, wird ein PropertyVetoException
	 * zurueckgegeben, bleibt das Modul "erhalten".
	 *
	 * @return PropertyVetoException
	 * @throws Throwable
	 */
	protected PropertyVetoException eventActionVetoableChangeLP() throws Throwable {
		return panelDetail.eventActionVetoableChangeLP();
	}

	/**
	 *
	 * @return int
	 * @throws Throwable
	 */
	public LockStateValue getLockedstateDetailMainKey() throws Throwable {
		// hier fix, damit er in's eventActionVetoableChangeLP geht.
		// return panelDetail.getLockedstateDetailMainKey();
		return new LockStateValue(null, null, LOCK_IS_LOCKED_BY_ME);
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return panelQuery.getFirstFocusableComponent();
	}

	public PanelSplit setSelectedIdFromDetailPanel() throws Throwable {
		Object oKey = panelDetail.getKeyWhenDetailPanel();
		panelQuery.setSelectedId(oKey);
		return this;
	}

	public PanelSplit youAreSelectedFromDetailPanel() throws Throwable {
		return youAreSelectedFromDetailPanel(false);
	}

	public PanelSplit youAreSelectedFromDetailPanel(boolean noNeedYouAreSelected) throws Throwable {
		setSelectedIdFromDetailPanel();
		eventYouAreSelected(noNeedYouAreSelected);
		return this;
	}
}
