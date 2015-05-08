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

import javax.swing.JComponent;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

import com.lp.client.frame.HelperClient;
import com.lp.client.frame.LockStateValue;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.benutzer.service.BenutzerFac;
import com.lp.server.benutzer.service.SystemrolleDto;

public class PanelSystemrolle extends PanelBasis {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// von hier ...
	private GridBagLayout gridBagLayoutAll = null;
	private JPanel jpaWorkingOn = new JPanel();
	private JPanel jpaButtonAction = null;
	private InternalFrameBenutzer internalFrameBenutzer = null;
	private WrapperLabel wlaKennung = new WrapperLabel();
	private WrapperTextField wtfKennung = new WrapperTextField();
	private WrapperLabel wlaBenutzeranzahl = new WrapperLabel();
	private WrapperNumberField wnfBenutzeranzahl = new WrapperNumberField();
	private SystemrolleDto systemrolleDto = null;

	public PanelSystemrolle(InternalFrame internalFrame, String add2TitleI,
			Object pk) throws Throwable {
		super(internalFrame, add2TitleI, pk);
		internalFrameBenutzer = (InternalFrameBenutzer) internalFrame;
		jbInit();
		setDefaults();
		initComponents();
		enableAllComponents(this, false);
	}

	protected void setDefaults() {
	}

	public void eventActionNew(EventObject eventObject, boolean bLockMeI,
			boolean bNeedNoNewI) throws Throwable {
		super.eventActionNew(eventObject, true, false);
		
		systemrolleDto = new SystemrolleDto();
		leereAlleFelder(this);
	}

	protected void eventActionDelete(ActionEvent e,
			boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {
		DelegateFactory.getInstance().getBenutzerDelegate().removeSystemrolle(
				systemrolleDto.getIId());
		super.eventActionDelete(e, false, false);
	}
	
	@Override
	public void updateButtons() throws Throwable {
		super.updateButtons();
		if(internalFrameBenutzer.getSystemrolleDto() != null
				&& internalFrameBenutzer.getSystemrolleDto().getIMaxUsers() != null)
			enableToolsPanelButtons(false, ACTION_UPDATE, ACTION_DELETE);
	}

	@Override
	public void updateButtons(LockStateValue lockstateValueI) throws Throwable {
		super.updateButtons(lockstateValueI);
		if(internalFrameBenutzer.getSystemrolleDto() != null
				&& internalFrameBenutzer.getSystemrolleDto().getIMaxUsers() != null)
			enableToolsPanelButtons(false, ACTION_UPDATE, ACTION_DELETE);
	}
	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {

		super.eventYouAreSelected(false);
		Object key = null;

		if (internalFrameBenutzer.getSystemrolleDto() != null) {
			key = internalFrameBenutzer.getSystemrolleDto().getIId();
			// getKeyWhenDetailPanel();
		}

		if (key == null || (key.equals(LPMain.getLockMeForNew()))) {
			leereAlleFelder(this);
			clearStatusbar();
		} else {
			systemrolleDto = DelegateFactory.getInstance()
					.getBenutzerDelegate().systemrolleFindByPrimaryKey(
							(Integer) key);
			wtfKennung.setText(systemrolleDto.getCBez());
			wnfBenutzeranzahl.setInteger(systemrolleDto.getIMaxUsers());
			getInternalFrame().setLpTitle(
					InternalFrame.TITLE_IDX_AS_I_LIKE,
					LPMain.getTextRespectUISPr("lp.systemrolle")
							+ ": "
							+ internalFrameBenutzer.getSystemrolleDto()
									.getCBez());

		}
	}

	protected void eventItemchanged(EventObject eI) {
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wtfKennung;
	}

	private void jbInit() throws Throwable {
		// das Aussenpanel hat immer das Gridbaglayout.
		gridBagLayoutAll = new GridBagLayout();
		this.setLayout(gridBagLayoutAll);

		// Actionpanel von Oberklasse holen und anhaengen.
		jpaButtonAction = getToolsPanel();
		this.setActionMap(null);

		wlaKennung.setText(LPMain.getTextRespectUISPr(
				"lp.systemrolle"));
		wtfKennung.setColumnsMax(BenutzerFac.MAX_SYSTEMROLLE_NAME);
		wtfKennung.setText("");
		wtfKennung.setMandatoryField(true);
		
		wlaBenutzeranzahl.setText(LPMain.getTextRespectUISPr("lp.benutzeranzahl"));
		wnfBenutzeranzahl.setActivatable(false);
		getInternalFrame().addItemChangedListener(this);

		this.add(jpaButtonAction, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0,
						0, 0, 0), 0, 0));

		jpaWorkingOn = new JPanel(new MigLayout("wrap 2", "[33%, fill| 66%, fill]"));
		this.add(jpaWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
				GridBagConstraints.SOUTHEAST, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
		this.add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 1.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
		jpaWorkingOn.add(wlaKennung);
		jpaWorkingOn.add(wtfKennung);
		jpaWorkingOn.add(wlaBenutzeranzahl);
		jpaWorkingOn.add(wnfBenutzeranzahl);
		String[] aWhichButtonIUse = { ACTION_UPDATE, ACTION_SAVE,
				ACTION_DELETE, ACTION_DISCARD, };

		enableToolsPanelButtons(aWhichButtonIUse);

	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_SYSTEMROLLE;
	}

	protected void components2Dto() {
		systemrolleDto.setCBez(wtfKennung.getText());
	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {
		if (allMandatoryFieldsSetDlg()) {
			components2Dto();

			if (systemrolleDto.getIId() == null) {
				// Create
				systemrolleDto.setIId(DelegateFactory.getInstance()
						.getBenutzerDelegate()
						.createSystemrolle(systemrolleDto));

				// diesem panel den key setzen.
				setKeyWhenDetailPanel(systemrolleDto.getIId());
				internalFrameBenutzer.setSystemrolleDto(systemrolleDto);
			} else {
				DelegateFactory.getInstance().getBenutzerDelegate()
						.updateSystemrolle(systemrolleDto);
			}

			super.eventActionSave(e, true);
			if (getInternalFrame().getKeyWasForLockMe() == null) {
				getInternalFrame().setKeyWasForLockMe(
						systemrolleDto.getIId().toString());
			}
			eventYouAreSelected(false);

		}
	}

}
