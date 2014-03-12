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
import com.lp.server.benutzer.service.NachrichtartDto;
import com.lp.server.benutzer.service.SystemrolleDto;
import com.lp.server.benutzer.service.ThemaDto;
import com.lp.server.benutzer.service.ThemarolleDto;

@SuppressWarnings("static-access")
public class PanelThemarolle extends PanelBasis {

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

	private ThemarolleDto themarolleDto = null;
	private WrapperButton wbuSystemrolle = new WrapperButton();
	private WrapperTextField wtfSystemrolle = new WrapperTextField();
	private WrapperButton wbuThema = new WrapperButton();
	private WrapperTextField wtfThema = new WrapperTextField();

	private PanelQueryFLR panelQueryFLRThema = null;
	private PanelQueryFLR panelQueryFLRSystemrolle = null;

	static final public String ACTION_SPECIAL_THEMA_FROM_LISTE = "action_thema_from_liste";
	static final public String ACTION_SPECIAL_SYSTEMROLLE_FROM_LISTE = "action_systemrolle_from_liste";

	public PanelThemarolle(InternalFrame internalFrame, String add2TitleI,
			Object pk) throws Throwable {
		super(internalFrame, add2TitleI, pk);
		jbInit();
		setDefaults();
		initComponents();
		enableAllComponents(this, false);
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wtfSystemrolle;
	}

	protected void setDefaults() {
	}

	public void eventActionNew(EventObject eventObject, boolean bLockMeI,
			boolean bNeedNoNewI) throws Throwable {
		super.eventActionNew(eventObject, true, false);
		themarolleDto = new ThemarolleDto();
		leereAlleFelder(this);
	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		if (e.getActionCommand().equals(ACTION_SPECIAL_THEMA_FROM_LISTE)) {
			dialogQueryThemaFromListe(e);
		}
		else if (e.getActionCommand().equals(ACTION_SPECIAL_SYSTEMROLLE_FROM_LISTE)) {
			dialogQuerySystemrolleFromListe(e);
		}
	}

	protected void eventActionDelete(ActionEvent e,
			boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {
		DelegateFactory.getInstance().getBenutzerDelegate().removeThemarolle(
				themarolleDto.getIId());
		this.setKeyWhenDetailPanel(null);
		super.eventActionDelete(e, false, false);
	}

	protected void components2Dto() {
	}

	protected void dto2Components() throws ExceptionLP, Throwable {

		SystemrolleDto systemrolleDto = DelegateFactory.getInstance()
				.getBenutzerDelegate().systemrolleFindByPrimaryKey(
						themarolleDto.getSystemrolleIId());
		wtfSystemrolle.setText(systemrolleDto.getCBez());
		ThemaDto themaDto = DelegateFactory.getInstance().getBenutzerDelegate()
				.themaFindByPrimaryKey(themarolleDto.getThemaCNr());
		wtfThema.setText(themaDto.getBezeichnung());
	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {
		if (allMandatoryFieldsSetDlg()) {

			components2Dto();

			if (themarolleDto.getIId() == null) {
				themarolleDto.setIId(DelegateFactory.getInstance()
						.getBenutzerDelegate()
						.createThemarolle(themarolleDto));
				setKeyWhenDetailPanel(themarolleDto.getIId());
			} else {
				DelegateFactory.getInstance().getBenutzerDelegate()
						.updateThemarolle(themarolleDto);
			}
			super.eventActionSave(e, true);
			if (getInternalFrame().getKeyWasForLockMe() == null) {
				getInternalFrame().setKeyWasForLockMe(
						themarolleDto.getIId() + "");
			}
			eventYouAreSelected(false);
		}
	}

	void dialogQueryThemaFromListe(ActionEvent e) throws Throwable {
		panelQueryFLRThema = BenutzerFilterFactory.getInstance()
				.createPanelFLRThema(getInternalFrame(),
						themarolleDto.getThemaCNr(), false);

		new DialogQuery(panelQueryFLRThema);
	}
	void dialogQuerySystemrolleFromListe(ActionEvent e) throws Throwable {
		panelQueryFLRSystemrolle = BenutzerFilterFactory.getInstance()
				.createPanelFLRSystemrolle(getInternalFrame(),
						themarolleDto.getSystemrolleIId(), false);

		new DialogQuery(panelQueryFLRSystemrolle);
	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;
		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			if (e.getSource() == panelQueryFLRThema) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();

				ThemaDto themaDto = DelegateFactory.getInstance()
						.getBenutzerDelegate().themaFindByPrimaryKey(key + "");
				wtfThema.setText(themaDto.getBezeichnung());
				themarolleDto.setThemaCNr(themaDto.getCNr());
			}
			else if (e.getSource() == panelQueryFLRSystemrolle) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();

				SystemrolleDto systemrolleDto = DelegateFactory.getInstance()
				.getBenutzerDelegate().systemrolleFindByPrimaryKey((Integer)key);
	        	wtfSystemrolle.setText(systemrolleDto.getCBez());
				themarolleDto.setSystemrolleIId(systemrolleDto.getIId());
			}
		}
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

		wtfSystemrolle.setActivatable(false);
		wtfSystemrolle.setMandatoryField(true);

		getInternalFrame().addItemChangedListener(this);

		wbuSystemrolle.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.systemrolle")+ "...");
		wbuSystemrolle.setActionCommand(ACTION_SPECIAL_SYSTEMROLLE_FROM_LISTE);
		wbuSystemrolle.addActionListener(this);

		wbuThema.setText(LPMain.getInstance().getTextRespectUISPr("ben.thema")
				+ "...");
		wbuThema.setActionCommand(ACTION_SPECIAL_THEMA_FROM_LISTE);
		wbuThema.addActionListener(this);

		wtfThema.setActivatable(false);
		wtfThema.setMandatoryField(true);

		wtfThema.setText("");

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

		iZeile++;
		jpaWorkingOn.add(wbuThema, new GridBagConstraints(0, iZeile, 1, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfThema, new GridBagConstraints(1, iZeile, 1, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;

		jpaWorkingOn.add(wtfSystemrolle, new GridBagConstraints(1, iZeile, 1,
				1, 0.5, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wbuSystemrolle, new GridBagConstraints(0, iZeile, 1,
				1, 0.3, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		String[] aWhichButtonIUse = { ACTION_UPDATE, ACTION_SAVE,
				ACTION_DELETE, ACTION_DISCARD, };

		enableToolsPanelButtons(aWhichButtonIUse);

	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_THEMAROLLE;
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {
		super.eventYouAreSelected(false);
		Object key = getKeyWhenDetailPanel();
		if (key == null || (key.equals(LPMain.getLockMeForNew()))) {
			leereAlleFelder(this);
			clearStatusbar();
		} else {
			themarolleDto = DelegateFactory.getInstance().getBenutzerDelegate()
					.themarolleFindByPrimaryKey((Integer) key);
			dto2Components();
		}
	}

	protected void eventActionUpdate(ActionEvent aE, boolean bNeedNoUpdateI)
			throws Throwable {
		super.eventActionUpdate(aE, bNeedNoUpdateI);

	}

}
