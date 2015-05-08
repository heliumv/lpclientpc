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
import javax.swing.border.Border;

import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperGotoButton;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperSelectField;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.partner.PartnerFilterFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.partner.service.AnsprechpartnerDto;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.system.service.SpediteurDto;
import com.lp.server.util.Facade;

@SuppressWarnings("static-access")
public class PanelSpediteur extends PanelBasis {

	private static final String ACTION_SPECIAL_PARTNER = "action_special_partner";
	private static final String ACTION_SPECIAL_ANSPRECHPARTNER = "action_special_ansprechpartner";

	private WrapperGotoButton wbuPartner = new WrapperGotoButton(
			WrapperGotoButton.GOTO_PARTNER_AUSWAHL);
	private WrapperTextField wtfPartner = null;
	private PanelQueryFLR panelQueryFLRPartner = null;
	private PanelQueryFLR panelQueryFLRAnsprechpartner = null;
	private WrapperButton wbuAnsprechpartner = null;
	private WrapperTextField wtfAnsprechpartner = null;

	private static final long serialVersionUID = 1L;
	private GridBagLayout gridBagLayoutAll = null;
	private JPanel jpaWorkingOn = new JPanel();
	private JPanel jpaButtonAction = null;
	private Border border = null;
	private GridBagLayout gridBagLayoutWorkingPanel = null;
	private InternalFrameSystem internalFramePersonal = null;
	private SpediteurDto spediteurDto = null;
	private WrapperLabel wlaSpediteur = new WrapperLabel();
	private WrapperTextField wtfSpediteur = new WrapperTextField();
	private WrapperLabel wlaEmail = new WrapperLabel();
	private WrapperTextField wtfEmail = new WrapperTextField();
	private WrapperCheckBox wcbVersteckt = new WrapperCheckBox();

	public PanelSpediteur(InternalFrame internalFrame, String add2TitleI,
			Object pk) throws Throwable {
		super(internalFrame, add2TitleI, pk);
		internalFramePersonal = (InternalFrameSystem) internalFrame;
		jbInit();
		setDefaults();
		initComponents();
		enableAllComponents(this, false);
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wtfSpediteur;
	}

	protected void setDefaults() throws Throwable {

	}

	public void eventActionNew(EventObject eventObject, boolean bLockMeI,
			boolean bNeedNoNewI) throws Throwable {
		super.eventActionNew(eventObject, true, false);
		spediteurDto = new SpediteurDto();
		// getInternalFrame().setKeyWasForLockMe(LPMain.getLockMeForNew());

		leereAlleFelder(this);
	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		 if (e.getActionCommand().equals(ACTION_SPECIAL_PARTNER)) {
				panelQueryFLRPartner = PartnerFilterFactory.getInstance()
						.createPanelFLRPartner(getInternalFrame(),
								spediteurDto.getPartnerIId(), true);
				new DialogQuery(panelQueryFLRPartner);
			} else if (e.getActionCommand().equals(ACTION_SPECIAL_ANSPRECHPARTNER)) {
				if (spediteurDto.getPartnerIId() != null) {
					
					panelQueryFLRAnsprechpartner = PartnerFilterFactory
							.getInstance().createPanelFLRAnsprechpartner(
									getInternalFrame(),
									spediteurDto.getPartnerIId(),
									spediteurDto.getAnsprechpartnerIId(), true, true);
					new DialogQuery(panelQueryFLRAnsprechpartner);
				}
			}
	}

	protected void eventActionDelete(ActionEvent e,
			boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {
		DelegateFactory.getInstance().getMandantDelegate()
				.removeSpediteur(spediteurDto);
		this.setKeyWhenDetailPanel(null);
		super.eventActionDelete(e, false, false);
	}

	protected void components2Dto() {

		spediteurDto.setCNamedesspediteurs(wtfSpediteur.getText());
		spediteurDto.setCEmail(wtfEmail.getText());
		spediteurDto.setBVersteckt(wcbVersteckt.getShort());
		
	}

	protected void dto2Components() throws Throwable {

		wtfSpediteur.setText(spediteurDto.getCNamedesspediteurs());
		wtfEmail.setText(spediteurDto.getCEmail());

		if (spediteurDto.getPartnerIId() != null) {
			PartnerDto partnerDto = DelegateFactory.getInstance()
					.getPartnerDelegate()
					.partnerFindByPrimaryKey(spediteurDto.getPartnerIId());

			wtfPartner.setText(partnerDto.formatFixTitelName1Name2());
		} else {
			wtfPartner.setText(null);
		}
		if (spediteurDto.getAnsprechpartnerIId() != null) {
			AnsprechpartnerDto ansprechpartnerDto = DelegateFactory
					.getInstance()
					.getAnsprechpartnerDelegate()
					.ansprechpartnerFindByPrimaryKey(
							spediteurDto.getAnsprechpartnerIId());
			wtfAnsprechpartner.setText(ansprechpartnerDto.getPartnerDto()
					.formatAnrede());
		} else {
			wtfAnsprechpartner.setText(null);
		}

		wcbVersteckt.setShort(spediteurDto.getBVersteckt());

	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {
		if (allMandatoryFieldsSetDlg()) {
			components2Dto();
			if (spediteurDto.getIId() == null) {
				spediteurDto.setIId(DelegateFactory.getInstance()
						.getMandantDelegate().createSpediteur(spediteurDto));
				setKeyWhenDetailPanel(spediteurDto.getIId());

			} else {
				DelegateFactory.getInstance().getMandantDelegate()
						.updateSpediteur(spediteurDto);
			}
			super.eventActionSave(e, true);

			if (getInternalFrame().getKeyWasForLockMe() == null) {
				getInternalFrame().setKeyWasForLockMe(
						spediteurDto.getIId() + "");
			}
			eventYouAreSelected(false);
		}
	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;
		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			if (e.getSource() == panelQueryFLRPartner) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				if (key != null) {
					PartnerDto partnerDto = DelegateFactory.getInstance()
							.getPartnerDelegate()
							.partnerFindByPrimaryKey((Integer) key);

					wtfPartner.setText(partnerDto.formatFixTitelName1Name2());
					spediteurDto.setPartnerIId(partnerDto.getIId());
					spediteurDto.setAnsprechpartnerIId(null);
					wtfAnsprechpartner.setText(null);

				}
			} else if (e.getSource() == panelQueryFLRAnsprechpartner) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				if (key != null) {
					AnsprechpartnerDto ansprechpartnerDto = DelegateFactory
							.getInstance().getAnsprechpartnerDelegate()
							.ansprechpartnerFindByPrimaryKey((Integer) key);
					wtfAnsprechpartner.setText(ansprechpartnerDto
							.getPartnerDto().formatAnrede());
					spediteurDto.setAnsprechpartnerIId(ansprechpartnerDto
							.getIId());
				}
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_LEEREN) {

			if (e.getSource() == panelQueryFLRPartner) {
				spediteurDto.setPartnerIId(null);
				wtfPartner.setText(null);
				spediteurDto.setAnsprechpartnerIId(null);
				wtfAnsprechpartner.setText(null);
			}

			if (e.getSource() == panelQueryFLRAnsprechpartner) {
				spediteurDto.setAnsprechpartnerIId(null);
				wtfAnsprechpartner.setText(null);
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

		wcbVersteckt.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.versteckt"));

		wlaSpediteur.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.spediteur"));

		wtfSpediteur.setMandatoryField(true);
		wtfSpediteur.setColumnsMax(40);

		wlaEmail.setText(LPMain.getInstance().getTextRespectUISPr("lp.email"));
		wtfEmail.setColumnsMax(80);

		wbuPartner.setText(LPMain.getTextRespectUISPr("button.partner"));
		wbuPartner.setToolTipText(LPMain
				.getTextRespectUISPr("button.partner.tooltip"));

		wbuPartner.setActionCommand(ACTION_SPECIAL_PARTNER);
		wbuPartner.addActionListener(this);

		wbuAnsprechpartner = new WrapperButton();

		wbuAnsprechpartner.setText(LPMain
				.getTextRespectUISPr("button.ansprechpartner"));
		wbuAnsprechpartner.setToolTipText(LPMain
				.getTextRespectUISPr("button.ansprechpartner.tooltip"));

		wbuAnsprechpartner.setActionCommand(ACTION_SPECIAL_ANSPRECHPARTNER);
		wbuAnsprechpartner.addActionListener(this);

		wtfAnsprechpartner = new WrapperTextField();
		wtfAnsprechpartner.setActivatable(false);

		wtfPartner = new WrapperTextField();
		wtfPartner.setColumnsMax(Facade.MAX_UNBESCHRAENKT);
		
		wtfPartner.setActivatable(false);

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

		iZeile++;

		jpaWorkingOn.add(wlaSpediteur, new GridBagConstraints(0, iZeile, 1, 1,
				0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfSpediteur, new GridBagConstraints(1, iZeile, 1, 1,
				0.2, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;

		jpaWorkingOn.add(wbuPartner, new GridBagConstraints(0, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wtfPartner, new GridBagConstraints(1, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;

		jpaWorkingOn.add(wbuAnsprechpartner, new GridBagConstraints(0, iZeile,
				1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wtfAnsprechpartner, new GridBagConstraints(1, iZeile,
				1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wlaEmail, new GridBagConstraints(0, iZeile, 1, 1, 0.1,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfEmail, new GridBagConstraints(1, iZeile, 1, 1, 0.0,
				0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		jpaWorkingOn.add(wcbVersteckt, new GridBagConstraints(1, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		String[] aWhichButtonIUse = { ACTION_UPDATE, ACTION_SAVE,
				ACTION_DELETE, ACTION_DISCARD, };

		enableToolsPanelButtons(aWhichButtonIUse);

	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_MANDANT;
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {

		super.eventYouAreSelected(false);

		Object key = getKeyWhenDetailPanel();

		if (key == null || (key.equals(LPMain.getLockMeForNew()))) {

			leereAlleFelder(this);

			clearStatusbar();
		} else {
			spediteurDto = DelegateFactory.getInstance().getMandantDelegate()
					.spediteurFindByPrimaryKey((Integer) key);

			dto2Components();

		}
	}

}
