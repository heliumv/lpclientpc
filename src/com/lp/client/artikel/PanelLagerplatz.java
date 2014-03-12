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
package com.lp.client.artikel;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.EventObject;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.border.Border;

import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.reklamation.ReportReklamation;
import com.lp.server.artikel.service.ArtikelFac;
import com.lp.server.artikel.service.LagerDto;
import com.lp.server.artikel.service.LagerplatzDto;
import com.lp.server.artikel.service.PaternosterDto;

@SuppressWarnings("static-access")
public class PanelLagerplatz extends PanelBasis {

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
	private InternalFrameArtikel internalFrameArtikel = null;
	private WrapperLabel wlaLagerplatz = new WrapperLabel();
	private WrapperTextField wtfLagerplatz = new WrapperTextField();

	private LagerplatzDto lagerplatzDto = null;
	private WrapperButton wbuLager = new WrapperButton();
	private WrapperTextField wtfLager = new WrapperTextField();
	private WrapperButton wbuPaternoster = new WrapperButton();
	private WrapperTextField wtfPaternoster = new WrapperTextField();
	private PanelQueryFLR panelQueryFLRLager = null;
	static final public String ACTION_SPECIAL_LAGER_FROM_LISTE = "action_lager_from_liste";
	private PanelQueryFLR panelQueryFLRPaternoster = null;
	static final public String ACTION_SPECIAL_PATERNOSTER_FROM_LISTE = "action_paternoster_from_liste";

	public PanelLagerplatz(InternalFrame internalFrame, String add2TitleI,
			Object pk) throws Throwable {
		super(internalFrame, add2TitleI, pk);
		internalFrameArtikel = (InternalFrameArtikel) internalFrame;
		jbInit();
		setDefaults();
		initComponents();
		enableAllComponents(this, false);
	}

	protected void setDefaults() {

	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wtfLagerplatz;
	}

	public void eventActionNew(EventObject eventObject, boolean bLockMeI,
			boolean bNeedNoNewI) throws Throwable {
		super.eventActionNew(eventObject, true, false);
		leereAlleFelder(this);
		lagerplatzDto = new LagerplatzDto();
	}

	protected void dto2Components() throws Throwable {

		wtfLagerplatz.setText(lagerplatzDto.getCLagerplatz());
		LagerDto lagerDto = DelegateFactory.getInstance().getLagerDelegate()
				.lagerFindByPrimaryKey(lagerplatzDto.getLagerIId());

		wtfLager.setText(lagerDto.getCNr());

		if (lagerplatzDto.getPaternosterIId() != null) {
			PaternosterDto paternosterDto = DelegateFactory.getInstance()
					.getAutoPaternosterDelegate().paternosterFindByPrimaryKey(
							lagerplatzDto.getPaternosterIId());
			wtfPaternoster.setText(paternosterDto.getCNr());
		} else {
			wtfPaternoster.setText(null);
		}

		this.setStatusbarPersonalIIdAendern(lagerplatzDto
				.getPersonalIIdAendern());
		this.setStatusbarTAendern(lagerplatzDto.getTAendern());

	}

	protected void components2Dto() throws Throwable {
		lagerplatzDto.setCLagerplatz(wtfLagerplatz.getText());

	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;
		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			if (e.getSource() == panelQueryFLRLager) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				LagerDto lagerDto = DelegateFactory.getInstance()
						.getLagerDelegate()
						.lagerFindByPrimaryKey((Integer) key);
				wtfLager.setText(lagerDto.getCNr());
				lagerplatzDto.setLagerIId(lagerDto.getIId());
			} else if (e.getSource() == panelQueryFLRPaternoster) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				PaternosterDto paternosterDto = DelegateFactory.getInstance()
						.getAutoPaternosterDelegate()
						.paternosterFindByPrimaryKey((Integer) key);
				wtfPaternoster.setText(paternosterDto.getCNr());
				lagerplatzDto.setPaternosterIId(paternosterDto.getIId());
			}

		}else if (e.getID() == ItemChangedEvent.ACTION_LEEREN) {
			if (e.getSource() == panelQueryFLRPaternoster) {
				wtfPaternoster.setText(null);
				lagerplatzDto.setPaternosterIId(null);
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

		wlaLagerplatz.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.lagerplatz"));
		/*
		 * wbuLagerplatz.setActionCommand(this.
		 * ACTION_SPECIAL_LAGERPLATZ_FROM_LISTE);
		 * wbuLagerplatz.addActionListener(this);
		 */
		wtfLagerplatz.setColumnsMax(ArtikelFac.MAX_LAGERPLATZ_NAME);
		wtfLagerplatz.setMandatoryField(true);
		getInternalFrame().addItemChangedListener(this);

		wbuLager.setText(LPMain.getInstance().getTextRespectUISPr(
				"button.lager"));
		wbuLager.setActionCommand(ACTION_SPECIAL_LAGER_FROM_LISTE);
		wbuLager.addActionListener(this);

		wbuPaternoster.setText(LPMain.getInstance().getTextRespectUISPr(
				"artikel.paternoster")
				+ "...");
		wbuPaternoster.setActionCommand(ACTION_SPECIAL_PATERNOSTER_FROM_LISTE);
		wbuPaternoster.addActionListener(this);
		wtfPaternoster.setActivatable(false);

		wtfLager.setColumnsMax(ArtikelFac.MAX_LAGER_NAME);
		wtfLager.setMandatoryField(true);
		wtfLager.setActivatable(false);
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
		jpaWorkingOn.add(wlaLagerplatz, new GridBagConstraints(0, 0, 1, 1,
				0.05, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfLagerplatz, new GridBagConstraints(1, 0, 1, 1, 0.1,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wbuLager, new GridBagConstraints(0, 1, 1, 1, 0.05,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfLager, new GridBagConstraints(1, 1, 1, 1, 0.1, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wbuPaternoster, new GridBagConstraints(0, 2, 1, 1, 0.05,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfPaternoster, new GridBagConstraints(1, 2, 1, 1, 0.1, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		String[] aWhichButtonIUse = { ACTION_UPDATE, ACTION_SAVE,
				ACTION_DELETE, ACTION_DISCARD, PanelBasis.ACTION_PRINT, };

		enableToolsPanelButtons(aWhichButtonIUse);

	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_LAGERPLATZ;
	}

	protected void eventActionDelete(ActionEvent e,
			boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {
		DelegateFactory.getInstance().getLagerDelegate().removeLagerplatz(
				lagerplatzDto);
		this.setKeyWhenDetailPanel(null);
		super.eventActionDelete(e, false, false);
	}

	void dialogQueryLagerFromListe(ActionEvent e) throws Throwable {
		panelQueryFLRLager = ArtikelFilterFactory.getInstance()
				.createPanelFLRLager(getInternalFrame(),
						lagerplatzDto.getLagerIId());

		new DialogQuery(panelQueryFLRLager);
	}

	void dialogQueryPaternosterFromListe(ActionEvent e) throws Throwable {

		if (lagerplatzDto.getLagerIId() != null) {

			panelQueryFLRPaternoster = ArtikelFilterFactory.getInstance()
					.createPanelFLRPaternoster(getInternalFrame(),
							lagerplatzDto.getPaternosterIId(),
							lagerplatzDto.getLagerIId());

			new DialogQuery(panelQueryFLRPaternoster);
		} else {
			DialogFactory.showModalDialog(LPMain.getInstance()
					.getTextRespectUISPr("lp.error"), LPMain.getInstance()
					.getTextRespectUISPr("artikel.paternoster.error.lager"));
			return;
		}
	}

	protected void eventActionPrint(ActionEvent e) throws Throwable {
		getInternalFrame().showReportKriterien(
				new ReportLagerplatz(internalFrameArtikel, "", lagerplatzDto
						.getIId()));

	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		if (e.getActionCommand().equals(ACTION_SPECIAL_LAGER_FROM_LISTE)) {
			dialogQueryLagerFromListe(e);
		} else if (e.getActionCommand().equals(
				ACTION_SPECIAL_PATERNOSTER_FROM_LISTE)) {
			dialogQueryPaternosterFromListe(e);
		}
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {
		super.eventYouAreSelected(false);
		Object key = getKeyWhenDetailPanel();

		if (key == null || (key.equals(LPMain.getLockMeForNew()))) {
			leereAlleFelder(this);
			clearStatusbar();
		} else {
			lagerplatzDto = DelegateFactory.getInstance().getLagerDelegate()
					.lagerplatzFindByPrimaryKey((Integer) key);
			dto2Components();
		}
	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {
		if (allMandatoryFieldsSetDlg()) {
			components2Dto();
			if (lagerplatzDto.getIId() == null) {

				lagerplatzDto.setIId(DelegateFactory.getInstance()
						.getLagerDelegate().createLagerplatz(lagerplatzDto));
				setKeyWhenDetailPanel(lagerplatzDto.getIId());
			} else {
				DelegateFactory.getInstance().getLagerDelegate()
						.updateLagerplatz(lagerplatzDto);
			}
			super.eventActionSave(e, true);

			if (getInternalFrame().getKeyWasForLockMe() == null) {
				getInternalFrame().setKeyWasForLockMe(
						lagerplatzDto.getIId() + "");
			}
			eventYouAreSelected(false);
		}
	}
}
