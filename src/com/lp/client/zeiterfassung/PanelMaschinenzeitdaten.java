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
package com.lp.client.zeiterfassung;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.EventObject;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.border.Border;

import com.lp.client.fertigung.FertigungFilterFactory;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.WrapperDateField;
import com.lp.client.frame.component.WrapperGotoButton;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperSelectField;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.component.WrapperTimestampField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.fertigung.service.LosDto;
import com.lp.server.fertigung.service.LossollarbeitsplanDto;
import com.lp.server.personal.service.MaschinenkostenDto;
import com.lp.server.personal.service.MaschinenzeitdatenDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.util.Helper;

public class PanelMaschinenzeitdaten extends PanelBasis {

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
	private InternalFrameZeiterfassung internalFrameZeiterfassung = null;
	private MaschinenzeitdatenDto maschinenzeitdatenDto = null;
	private WrapperGotoButton wbuLos = new WrapperGotoButton(
			WrapperGotoButton.GOTO_FERTIGUNG_AUSWAHL);

	private WrapperLabel wlaVon = new WrapperLabel();
	private WrapperTimestampField wdfVon = new WrapperTimestampField();
	private WrapperLabel wlaBis = new WrapperLabel();
	private WrapperTimestampField wdfBis = new WrapperTimestampField();
	private WrapperLabel wlaBemerkung = new WrapperLabel();
	private WrapperTextField wtfBemerkung = new WrapperTextField();
	private WrapperTextField wtfLos = new WrapperTextField();
	private WrapperSelectField wsfPersonal = null;

	private PanelQueryFLR panelQueryFLRLos = null;
	private PanelQueryFLR panelQueryFLRLossollarbeitsplan = null;
	private boolean bZeitdatenAufErledigteBuchbar = false;
	private boolean bZeitdatenAufAngelegteLoseBuchbar = false;

	static final public String ACTION_SPECIAL_LOS_FROM_LISTE = "action_los_from_liste";

	public PanelMaschinenzeitdaten(InternalFrame internalFrame,
			String add2TitleI, Object pk) throws Throwable {
		super(internalFrame, add2TitleI, pk);
		internalFrameZeiterfassung = (InternalFrameZeiterfassung) internalFrame;
		jbInit();
		setDefaults();
		initComponents();
		enableAllComponents(this, false);
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wdfVon;
	}

	protected void setDefaults() {

	}

	void dialogQueryLosFromListe(ActionEvent e) throws Throwable {

		panelQueryFLRLos = FertigungFilterFactory.getInstance()
				.createPanelFLRBebuchbareLose(getInternalFrame(),
						bZeitdatenAufErledigteBuchbar, true, bZeitdatenAufAngelegteLoseBuchbar, null, false);
		new DialogQuery(panelQueryFLRLos);

	}

	void dialogQueryLossollarbeitsplanFromListe(Integer selectedLosIId)
			throws Throwable {
		if (selectedLosIId != null) {
			panelQueryFLRLossollarbeitsplan = FertigungFilterFactory
					.getInstance().createPanelFLRLossollarbeitsplan(
							getInternalFrame(), selectedLosIId,
							maschinenzeitdatenDto.getLossollarbeitsplanIId());
			new DialogQuery(panelQueryFLRLossollarbeitsplan);
		}

	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {

		super.eventYouAreSelected(false);
		Object key = getKeyWhenDetailPanel();

		if (key == null || (key.equals(LPMain.getLockMeForNew()))) {

			leereAlleFelder(this);
			clearStatusbar();
			wdfVon.setTimestamp(new java.sql.Timestamp(System
					.currentTimeMillis()));

		} else {
			maschinenzeitdatenDto = DelegateFactory.getInstance()
					.getZeiterfassungDelegate()
					.maschinenzeitdatenFindByPrimaryKey((Integer) key);

			dto2Components();

		}

	}

	public void eventActionNew(EventObject eventObject, boolean bLockMeI,
			boolean bNeedNoNewI) throws Throwable {
		super.eventActionNew(eventObject, true, false);
		leereAlleFelder(this);
		maschinenzeitdatenDto = new MaschinenzeitdatenDto();

	}

	protected void dto2Components() throws Throwable {
		wdfVon.setTimestamp(maschinenzeitdatenDto.getTVon());
		wdfBis.setTimestamp(maschinenzeitdatenDto.getTBis());
		wtfBemerkung.setText(maschinenzeitdatenDto.getCBemerkung());

		wsfPersonal.setKey(maschinenzeitdatenDto.getPersonalIIdGestartet());

		LossollarbeitsplanDto sollarbeitsplanDto = DelegateFactory
				.getInstance().getFertigungDelegate()
				.lossollarbeitsplanFindByPrimaryKey(
						maschinenzeitdatenDto.getLossollarbeitsplanIId());

		LosDto losDto = DelegateFactory.getInstance().getFertigungDelegate()
				.losFindByPrimaryKey(sollarbeitsplanDto.getLosIId());
		
		String los=losDto.getCNr() + " AG:"
		+ sollarbeitsplanDto.getIArbeitsgangnummer();
		if(sollarbeitsplanDto.getIUnterarbeitsgang()!=null){
			los+=" UAG:"+sollarbeitsplanDto.getIUnterarbeitsgang();
		}
		wtfLos.setText(los);
		
		
		

		this.setStatusbarPersonalIIdAendern(maschinenzeitdatenDto
				.getPersonalIIdAendern());
		this.setStatusbarPersonalIIdAnlegen(maschinenzeitdatenDto
				.getPersonalIIdAnlegen());
		this.setStatusbarTAnlegen(maschinenzeitdatenDto.getTAnlegen());
		this.setStatusbarTAendern(maschinenzeitdatenDto.getTAendern());
	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;
		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			if (e.getSource() == panelQueryFLRLos) {
				Integer key = (Integer) ((ISourceEvent) e.getSource())
						.getIdSelected();
				if (key != null) {
					dialogQueryLossollarbeitsplanFromListe(key);
				}
			} else if (e.getSource() == panelQueryFLRLossollarbeitsplan) {
				Integer key = (Integer) ((ISourceEvent) e.getSource())
						.getIdSelected();
				LossollarbeitsplanDto sollarbeitsplanDto = DelegateFactory
						.getInstance().getFertigungDelegate()
						.lossollarbeitsplanFindByPrimaryKey(key);

				LosDto losDto = DelegateFactory.getInstance()
						.getFertigungDelegate().losFindByPrimaryKey(
								sollarbeitsplanDto.getLosIId());
				maschinenzeitdatenDto.setLossollarbeitsplanIId(key);
				wtfLos.setText(losDto.getCNr() + " AG:"
						+ sollarbeitsplanDto.getIArbeitsgangnummer());
			}
		}
	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {
		boolean bDatumgueltig = true;
		if (wdfVon.getTimestamp() != null) {
			if (wdfBis.getTimestamp() != null) {

				if (wdfBis.getTimestamp().getTime() < wdfVon.getTimestamp()
						.getTime()) {
					bDatumgueltig = false;
				}
			}
		}
		if (bDatumgueltig) {
			if (allMandatoryFieldsSetDlg()) {
				components2Dto();
				if (maschinenzeitdatenDto.getIId() == null) {
					maschinenzeitdatenDto
							.setMaschineIId(internalFrameZeiterfassung
									.getMaschineDto().getIId());
					maschinenzeitdatenDto.setIId(DelegateFactory.getInstance()
							.getZeiterfassungDelegate()
							.createMaschinenzeitdaten(maschinenzeitdatenDto));
					setKeyWhenDetailPanel(maschinenzeitdatenDto.getIId());
				} else {
					DelegateFactory.getInstance().getZeiterfassungDelegate()
							.updateMaschinenzeitdaten(maschinenzeitdatenDto);
				}
				super.eventActionSave(e, true);

				if (getInternalFrame().getKeyWasForLockMe() == null) {
					getInternalFrame().setKeyWasForLockMe(
							internalFrameZeiterfassung.getMaschineDto()
									.getIId().toString());
				}
				eventYouAreSelected(false);
			}
		} else {
			DialogFactory.showModalDialog(LPMain
					.getTextRespectUISPr("lp.error"), LPMain
					.getTextRespectUISPr("lp.error.bisvorvon"));
		}
	}

	protected void components2Dto() throws ExceptionLP {
		maschinenzeitdatenDto.setTVon(wdfVon.getTimestamp());
		maschinenzeitdatenDto.setCBemerkung(wtfBemerkung.getText());
		maschinenzeitdatenDto.setTBis(wdfBis.getTimestamp());

		maschinenzeitdatenDto.setMaschineIId(internalFrameZeiterfassung
				.getMaschineDto().getIId());
		maschinenzeitdatenDto.setPersonalIIdGestartet(wsfPersonal.getIKey());

	}

	protected void eventActionDelete(ActionEvent e,
			boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {

		DelegateFactory.getInstance().getZeiterfassungDelegate()
				.removeMaschinenzeitdaten(maschinenzeitdatenDto);
		this.setKeyWhenDetailPanel(null);
		super.eventActionDelete(e, false, false);

	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		if (e.getActionCommand().equals(ACTION_SPECIAL_LOS_FROM_LISTE)) {
			dialogQueryLosFromListe(e);
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

		wlaVon.setText(LPMain.getTextRespectUISPr("lp.von"));
		wlaBis.setText(LPMain.getTextRespectUISPr("lp.bis"));

		wlaBemerkung.setText(LPMain.getTextRespectUISPr("lp.bemerkung"));

		wbuLos.setText(LPMain.getTextRespectUISPr("fert.tab.unten.los.title")
				+ "...");
		wbuLos.setActionCommand(ACTION_SPECIAL_LOS_FROM_LISTE);
		wbuLos.addActionListener(this);

		wsfPersonal = new WrapperSelectField(WrapperSelectField.PERSONAL,
				getInternalFrame(), false);
		wsfPersonal.setMandatoryField(true);

		ParametermandantDto parameter = (ParametermandantDto) DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getParametermandant(
						ParameterFac.PARAMETER_ZEITBUCHUNG_AUF_ERLEDIGTE_MOEGLICH,
						ParameterFac.KATEGORIE_PERSONAL,
						LPMain.getTheClient().getMandant());

		if (parameter.getCWert() != null && parameter.getCWert().equals("1")) {
			bZeitdatenAufErledigteBuchbar = true;
		}
		parameter = (ParametermandantDto) DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getParametermandant(
						ParameterFac.PARAMETER_ZEITBUCHUNG_AUF_ANGELEGTE_LOSE_MOEGLICH,
						ParameterFac.KATEGORIE_PERSONAL,
						LPMain.getTheClient().getMandant());

		if (parameter.getCWert() != null && parameter.getCWert().equals("1")) {
			bZeitdatenAufAngelegteLoseBuchbar = true;
		}

		getInternalFrame().addItemChangedListener(this);

		wdfVon.setMandatoryField(true);
		wtfLos.setMandatoryField(true);
		wtfLos.setActivatable(false);
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

		jpaWorkingOn.add(wlaVon, new GridBagConstraints(0, iZeile, 1, 1, 0.05,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wdfVon, new GridBagConstraints(1, iZeile, 1, 1, 0.1,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(0, 0, 0, 0), 0, 0));
		jpaWorkingOn.add(wlaBis, new GridBagConstraints(2, iZeile, 1, 1, 0.05,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wdfBis, new GridBagConstraints(3, iZeile, 1, 1, 0.1,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(0, 0, 0, 0), 0, 0));

		iZeile++;

		jpaWorkingOn.add(wbuLos, new GridBagConstraints(0, iZeile, 1, 1, 0.05,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wtfLos, new GridBagConstraints(1, iZeile, 1, 1, 0.1,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(0, 0, 0, 0), 0, 0));

		iZeile++;

		jpaWorkingOn.add(wsfPersonal.getWrapperButton(),
				new GridBagConstraints(0, iZeile, 1, 1, 0.05, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
						0, 0));

		jpaWorkingOn.add(wsfPersonal.getWrapperTextField(),
				new GridBagConstraints(1, iZeile, 1, 1, 0.1, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0),
						0, 0));
		iZeile++;

		jpaWorkingOn.add(wlaBemerkung, new GridBagConstraints(0, iZeile, 1, 1,
				0.05, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wtfBemerkung, new GridBagConstraints(1, iZeile, 3, 1,
				0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

		String[] aWhichButtonIUse = { ACTION_UPDATE, ACTION_SAVE,
				ACTION_DELETE, ACTION_DISCARD, };

		enableToolsPanelButtons(aWhichButtonIUse);

	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_MASCHINE;
	}

}
