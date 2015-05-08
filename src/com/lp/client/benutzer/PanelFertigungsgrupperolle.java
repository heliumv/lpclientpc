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
package com.lp.client.benutzer;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.EventObject;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.border.Border;

import com.lp.client.artikel.ArtikelFilterFactory;
import com.lp.client.artikel.PanelHandlagerbewegung;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.stueckliste.StuecklisteFilterFactory;
import com.lp.server.artikel.service.ArtikelFac;
import com.lp.server.artikel.service.LagerDto;
import com.lp.server.benutzer.service.FertigungsgrupperolleDto;
import com.lp.server.benutzer.service.LagerrolleDto;
import com.lp.server.benutzer.service.RollerechtDto;
import com.lp.server.stueckliste.service.FertigungsgruppeDto;
import com.lp.server.util.Facade;

public class PanelFertigungsgrupperolle extends PanelBasis {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// von hier ...
	private GridBagLayout gridBagLayoutAll = null;
	private JPanel jpaWorkingOn = new JPanel();
	private JPanel jpaButtonAction = null;
	private Border border = null;
	private GridBagLayout gridBagLayoutWorkingPanel = null;
	private InternalFrameBenutzer internalFrameBenutzer = null;

	private FertigungsgrupperolleDto fertigungsgrupperolleDto = null;

	static final public String ACTION_SPECIAL_FERTIGUNGSGRUPPE_FROM_LISTE = "action_fertigungsgruppe_from_liste";
	private PanelQueryFLR panelQueryFLRFertigungsgruppe = null;
	private WrapperButton wbuFertigungsgruppe = new WrapperButton();
	private WrapperTextField wtfFertigungsgruppe = new WrapperTextField();

	public PanelFertigungsgrupperolle(InternalFrame internalFrame,
			String add2TitleI, Object pk) throws Throwable {
		super(internalFrame, add2TitleI, pk);
		internalFrameBenutzer = (InternalFrameBenutzer) internalFrame;
		jbInit();
		setDefaults();
		initComponents();
		enableAllComponents(this, false);
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wbuFertigungsgruppe;
	}

	protected void setDefaults() {

	}

	public void eventActionNew(EventObject eventObject, boolean bLockMeI,
			boolean bNeedNoNewI) throws Throwable {
		super.eventActionNew(eventObject, true, false);
		leereAlleFelder(this);
		fertigungsgrupperolleDto = new FertigungsgrupperolleDto();
	}

	void dialogQueryFertigungsgruppeFromListe(ActionEvent e) throws Throwable {

		panelQueryFLRFertigungsgruppe = StuecklisteFilterFactory.getInstance()
				.createPanelFLRFertigungsgruppeAlle(getInternalFrame(),
						fertigungsgrupperolleDto.getFertigungsgruppeIId(), false);

		new DialogQuery(panelQueryFLRFertigungsgruppe);
	}

	protected void dto2Components() throws ExceptionLP, Throwable {
		FertigungsgruppeDto fertigungsgruppeDto = DelegateFactory
				.getInstance()
				.getStuecklisteDelegate()
				.fertigungsgruppeFindByPrimaryKey(
						fertigungsgrupperolleDto.getFertigungsgruppeIId());

		wtfFertigungsgruppe.setText(fertigungsgruppeDto.getCBez());
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {

		super.eventYouAreSelected(false);
		Object key = getKeyWhenDetailPanel();

		if (key == null || (key.equals(LPMain.getLockMeForNew()))) {
			leereAlleFelder(this);
			clearStatusbar();
		} else {
			fertigungsgrupperolleDto = DelegateFactory.getInstance().getBenutzerDelegate()
					.fertigungsgrupperolleFindByPrimaryKey((Integer) key);
			dto2Components();

		}
	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;
		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			if (e.getSource() == panelQueryFLRFertigungsgruppe) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				FertigungsgruppeDto fertigungsgruppeDto = DelegateFactory
						.getInstance().getStuecklisteDelegate()
						.fertigungsgruppeFindByPrimaryKey((Integer) key);

				wtfFertigungsgruppe.setText(fertigungsgruppeDto.getCBez());
				fertigungsgrupperolleDto.setFertigungsgruppeIId(fertigungsgruppeDto
						.getIId());
			}
		}

	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		if (e.getActionCommand().equals(
				ACTION_SPECIAL_FERTIGUNGSGRUPPE_FROM_LISTE)) {
			dialogQueryFertigungsgruppeFromListe(e);
		}
	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {
		if (allMandatoryFieldsSetDlg()) {
			components2Dto();
			if (fertigungsgrupperolleDto.getIId() == null) {
				fertigungsgrupperolleDto.setSystemrolleIId(internalFrameBenutzer
						.getSystemrolleDto().getIId());
				fertigungsgrupperolleDto.setIId(DelegateFactory.getInstance()
						.getBenutzerDelegate()
						.createFertigungsgrupperolle(fertigungsgrupperolleDto));
				setKeyWhenDetailPanel(fertigungsgrupperolleDto.getIId());
			} else {
				DelegateFactory.getInstance().getBenutzerDelegate()
						.updateFertigungsgrupperolle(fertigungsgrupperolleDto);
			}
			super.eventActionSave(e, true);

			if (getInternalFrame().getKeyWasForLockMe() == null) {
				getInternalFrame().setKeyWasForLockMe(
						internalFrameBenutzer.getSystemrolleDto().getIId()
								.toString());
			}
			eventYouAreSelected(false);
		}
	}

	protected void components2Dto() {

		fertigungsgrupperolleDto.setSystemrolleIId(internalFrameBenutzer
				.getSystemrolleDto().getIId());
	}

	protected void eventActionDelete(ActionEvent e,
			boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {

		DelegateFactory.getInstance().getBenutzerDelegate()
				.removeFertigungsgrupperolle((Integer) getKeyWhenDetailPanel());
		this.setKeyWhenDetailPanel(null);
		super.eventActionDelete(e, false, false);
		fertigungsgrupperolleDto = new FertigungsgrupperolleDto();
	}

	private void jbInit() throws Throwable {
		// von hier ...
		border = BorderFactory.createEmptyBorder(10, 10, 10, 10);
		setBorder(border);
		// das Aussenpanel hat immer das Gridbaglayout.
		gridBagLayoutAll = new GridBagLayout();
		this.setLayout(gridBagLayoutAll);

		// Actionpanel von Oberklasse holen und anhaengen.
		jpaButtonAction = getToolsPanel();
		this.setActionMap(null);

		wbuFertigungsgruppe.setText(LPMain.getTextRespectUISPr("stkl.fertigungsgruppe")+"...");
		wbuFertigungsgruppe.setActionCommand(ACTION_SPECIAL_FERTIGUNGSGRUPPE_FROM_LISTE);
		wbuFertigungsgruppe.addActionListener(this);

		wtfFertigungsgruppe.setColumnsMax(ArtikelFac.MAX_KATALOG_KATALOG);
		wtfFertigungsgruppe.setMandatoryField(true);
		wtfFertigungsgruppe.setActivatable(false);
		wtfFertigungsgruppe.setColumnsMax(Facade.MAX_UNBESCHRAENKT);

		getInternalFrame().addItemChangedListener(this);

		this.add(jpaButtonAction, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0,
						0, 0, 0), 0, 0));

		// jetzt meine felder
		jpaWorkingOn = new JPanel();
		gridBagLayoutWorkingPanel = new GridBagLayout();
		jpaWorkingOn.setLayout(gridBagLayoutWorkingPanel);
		this.add(jpaWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
				GridBagConstraints.SOUTHEAST, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
		this.add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 1.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
		jpaWorkingOn.add(wbuFertigungsgruppe, new GridBagConstraints(0, 0, 1, 1, 0.05,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfFertigungsgruppe, new GridBagConstraints(1, 0, 1, 1, 0.2, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(0, 0, 0, 0), 0, 0));
		String[] aWhichButtonIUse = { ACTION_UPDATE, ACTION_SAVE,
				ACTION_DELETE, ACTION_DISCARD, };

		enableToolsPanelButtons(aWhichButtonIUse);

	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_SYSTEMROLLE;
	}

}
