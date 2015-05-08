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

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.EventObject;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;

import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.util.fastlanereader.gui.QueryType;
import com.lp.server.system.service.LandDto;
import com.lp.server.system.service.LandplzortDto;
import com.lp.server.system.service.MandantDto;
import com.lp.server.system.service.OrtDto;
import com.lp.server.system.service.SystemFac;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;

@SuppressWarnings("static-access")
/**
 * <p>
 * <I>Diese Klasse kuemmert sich ...</I>
 * </p>
 * 
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2008
 * </p>
 * 
 * <p>
 * Erstellungsdatum <I>18.02.05</I>
 * </p>
 * 
 * <p>
 * </p>
 * 
 * @author josef erlinger
 * @version $Revision: 1.3 $
 */

public class PanelLandPlzOrt extends PanelBasis {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private LandplzortDto landplzortDto = null;
	private LandDto landDto = null;
	private OrtDto ortDto = null;

	private JPanel jPanelWorkingOn = new JPanel();
	private JPanel panelButtonAction = null;
	private WrapperLabel wlaPLZ = new WrapperLabel();
	private WrapperTextField wtfPLZ = new WrapperTextField();
	private WrapperTextField wtfOrt = new WrapperTextField();
	private WrapperButton wbuLKZ = new WrapperButton();
	private WrapperTextField wtfLKZ = new WrapperTextField();
	private WrapperTextField wtfLand = new WrapperTextField();
	private WrapperButton wbuOrt = new WrapperButton();
	private static final String ACTION_SPECIAL_FLR_ORT = "action_special_flr_ort";
	private static final String ACTION_SPECIAL_FLR_PLZ = "action_special_flr_plz";
	private static final String ACTION_SPECIAL_FLR_LKZ = "action_special_flr_lkz";
	private PanelQueryFLR panelQueryFLROrt = null;
	private PanelQueryFLR panelQueryFLRLand = null;

	public PanelLandPlzOrt(InternalFrame internalFrame, String add2TitleI,
			Object pk) throws Throwable {
		super(internalFrame, add2TitleI, pk);
		jbInit();
		initComponents();
	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {

		if (e.getActionCommand().equals(ACTION_SPECIAL_FLR_ORT)) {
			QueryType[] querytypes = null;
			FilterKriterium[] filters = null;

			String[] aWhichButtonIUse = null;
			panelQueryFLROrt = new PanelQueryFLR(querytypes, filters,
					QueryParameters.UC_ID_ORT, aWhichButtonIUse,
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("menueentry.ort"));
			panelQueryFLROrt.befuellePanelFilterkriterienDirekt(
					SystemFilterFactory.getInstance().createFKDOrt(), null);
			if (ortDto != null && ortDto.getIId() != null) {
				panelQueryFLROrt.setSelectedId(ortDto.getIId());
			}
			new DialogQuery(panelQueryFLROrt);
		}

		// neue Tabelle

		else if (e.getActionCommand().equals(ACTION_SPECIAL_FLR_LKZ)) {

			QueryType[] querytypes = null;
			FilterKriterium[] filters = null;

			String[] aWhichButtonIUse = null;

			panelQueryFLRLand = new PanelQueryFLR(querytypes, filters,
					QueryParameters.UC_ID_LAND, aWhichButtonIUse,
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("menueentry.lkz"));

			panelQueryFLRLand.befuellePanelFilterkriterienDirekt(
					SystemFilterFactory.getInstance().createFKDLandLKZ(),
					SystemFilterFactory.getInstance().createFKDLandName());

			if (landDto != null && landDto.getIID() != null) {
				panelQueryFLRLand.setSelectedId(landDto.getIID());
			}
			new DialogQuery(panelQueryFLRLand);
		}
	}

	public void eventActionNew(EventObject eventObject, boolean bLockMeI,
			boolean bNeedNoNewI) throws Throwable {
		super.eventActionNew(eventObject, true, false);
		landplzortDto = null;
		landDto = null;
		ortDto = null;
	}

	protected void eventActionDelete(ActionEvent e,
			boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {
		if (landplzortDto != null) {
			DelegateFactory.getInstance().getSystemDelegate().removeLandplzort(
					landplzortDto);
		}
		super.eventActionDelete(e, false, false);
	}

	protected void components2Dto() throws Throwable {
		if (landplzortDto == null) {
			landplzortDto = new LandplzortDto();
		}
		landplzortDto.setLandDto(landDto);
		landplzortDto.setIlandID(landDto.getIID());

		landplzortDto.setCPlz(wtfPLZ.getText());

		if (ortDto != null) {
			// ein ort wurde ausgewaehlt
			landplzortDto.setOrtDto(ortDto);
			landplzortDto.setOrtIId(ortDto.getIId());
		} else {
			// Ort manuell eingegeben
			landplzortDto.setOrtDto(new OrtDto());
		}
		// Damit der Ort auch direkt angelegt werden kann. (passiert dann am
		// Server)
		landplzortDto.getOrtDto().setCName(wtfOrt.getText());
	}

	protected void dto2Components() {
		// Land
		wtfLKZ.setText(landplzortDto.getLandDto().getCLkz());
		wtfLand.setText(landplzortDto.getLandDto().getCName());
		landDto = landplzortDto.getLandDto();
		// PLZ
		wtfPLZ.setText(landplzortDto.getCPlz());
		// Ort
		wtfOrt.setText(landplzortDto.getOrtDto().getCName());
		ortDto = landplzortDto.getOrtDto();
	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {
		if (allMandatoryFieldsSetDlg()) {
			components2Dto();
			if (landplzortDto.getIId() == null) {
				// create.
				Integer iIdLandPlzOrt = DelegateFactory.getInstance()
						.getSystemDelegate().createLandplzort(landplzortDto);
				// Dto neu laden
				landplzortDto = DelegateFactory.getInstance()
						.getSystemDelegate().landplzortFindByPrimaryKey(
								iIdLandPlzOrt);
				// diesem panel den key setzen.
				setKeyWhenDetailPanel(iIdLandPlzOrt);
			} else {
				// update
				DelegateFactory.getInstance().getSystemDelegate()
						.updateLandplzort(landplzortDto);
			}
			super.eventActionSave(e, true);
			eventYouAreSelected(false);
		}
	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;
		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			if (e.getSource() == panelQueryFLROrt) {
				Integer iIdOrt = (Integer) ((ISourceEvent) e.getSource())
						.getIdSelected();
				// hier wird die Ort id gesetzt
				ortDto = DelegateFactory.getInstance().getSystemDelegate()
						.ortFindByPrimaryKey(iIdOrt);
				// Anzeigen
				wtfOrt.setText(ortDto.getCName());
			} else if (e.getSource() == panelQueryFLRLand) {
				Integer iId = (Integer) ((ISourceEvent) e.getSource())
						.getIdSelected();
				// Land holen
				landDto = DelegateFactory.getInstance().getSystemDelegate()
						.landFindByPrimaryKey(iId);
				// Anzeigen
				wtfLKZ.setText(landDto.getCLkz());
				wtfLand.setText(landDto.getCName());
			}
		}
	}

	private void jbInit() throws Throwable {
		getInternalFrame().addItemChangedListener(this);

		String[] aWhichButtonIUse = { ACTION_UPDATE, ACTION_SAVE,
				ACTION_DELETE, ACTION_DISCARD, };

		enableToolsPanelButtons(aWhichButtonIUse);

		setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		// das Aussenpanel hat immer das Gridbaglayout.
		setLayout(new GridBagLayout());

		// Actionpanel von Oberklasse holen und anhaengen.
		panelButtonAction = getToolsPanel();
		this.add(panelButtonAction, new GridBagConstraints(0, 0, 1, 1, 0.0,
				0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(0, 0, 0, 0), 0, 0));

		wlaPLZ.setText(LPMain.getInstance().getTextRespectUISPr("lp.plz"));

		wtfPLZ.setColumnsMax(SystemFac.MAX_PLZ);
		wtfPLZ.setMandatoryFieldDB(true);

		wbuLKZ.setText(LPMain.getInstance().getTextRespectUISPr("lp.lkz"));
		wbuLKZ.setActionCommand(ACTION_SPECIAL_FLR_LKZ);
		wbuLKZ.addActionListener(this);

		wtfLKZ.setColumnsMax(SystemFac.MAX_LKZ);
		wtfLKZ.setMandatoryFieldDB(true);
		wtfLKZ.setActivatable(false);

		wtfLand.setColumnsMax(SystemFac.MAX_LAND);
		wtfLand.setMandatoryFieldDB(true);
		wtfLand.setActivatable(false);

		wbuOrt.setText(LPMain.getInstance().getTextRespectUISPr("lp.ort1"));
		wbuOrt.setActionCommand(ACTION_SPECIAL_FLR_ORT);
		wbuOrt.addActionListener(this);

		wtfOrt.setColumnsMax(SystemFac.MAX_ORT);
		wtfOrt.setActivatable(true);
		wtfOrt.setEditable(true);
		wtfOrt.setMandatoryField(true);

		// jetzt meine felder
		jPanelWorkingOn = new JPanel(new GridBagLayout());
		add(jPanelWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
				GridBagConstraints.SOUTHEAST, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
		add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						0, 0, 0, 0), 0, 0));

		jPanelWorkingOn.add(wlaPLZ, new GridBagConstraints(0, 1, 1, 1, 0.1,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wtfPLZ, new GridBagConstraints(1, 1, 1, 1, 0.1,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wbuLKZ, new GridBagConstraints(0, 0, 1, 1, 0.1,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wtfLKZ, new GridBagConstraints(1, 0, 1, 1, 0.1,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));

		jPanelWorkingOn.add(wtfLand, new GridBagConstraints(2, 0, 1, 1, 0.1,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));

		jPanelWorkingOn.add(wbuOrt, new GridBagConstraints(0, 2, 1, 1, 0.1,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wtfOrt, new GridBagConstraints(1, 2, 1, 1, 0.1,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_LANDPLZORT;
	}

	private void setDefaults() throws Throwable {
		// Land PLZ Ort des Mandanten vorbesetzen
		MandantDto mandantDto = DelegateFactory.getInstance()
				.getMandantDelegate().mandantFindByPrimaryKey(
						LPMain.getInstance().getTheClient().getMandant());
		if (mandantDto.getPartnerDto().getLandplzortDto() != null) {
			landDto = mandantDto.getPartnerDto().getLandplzortDto()
					.getLandDto();
			wtfLand.setText(landDto.getCName());
			wtfLKZ.setText(landDto.getCLkz());
		}
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {
		super.eventYouAreSelected(false);
		Object key = getKeyWhenDetailPanel();
		if (key == null
				|| (key != null && key.equals(LPMain.getLockMeForNew()))) {
			leereAlleFelder(this);
			// vorbesetzen
			setDefaults();
			// Statusbar leeren
			clearStatusbar();
		} else {
			landplzortDto = DelegateFactory.getInstance().getSystemDelegate()
					.landplzortFindByPrimaryKey((Integer) key);
			dto2Components();
		}
	}

	public LandplzortDto getLandplzortDto() {
		return landplzortDto;
	}

	protected JComponent getFirstFocusableComponent() {
		return wtfPLZ;
	}

	public void setLandplzortDto(LandplzortDto landplzortDto) {
		this.landplzortDto = landplzortDto;
	}

}
