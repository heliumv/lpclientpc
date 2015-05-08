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
package com.lp.client.inserat;

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
import com.lp.client.frame.LockStateValue;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperGotoButton;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.partner.PartnerFilterFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.inserat.service.InseratrechnungDto;
import com.lp.server.partner.service.AnsprechpartnerDto;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.rechnung.service.RechnungDto;
import com.lp.server.rechnung.service.RechnungPositionDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.util.Facade;

@SuppressWarnings("static-access")
public class PanelInseratKunden extends PanelBasis {

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
	private InternalFrameInserat internalFrameInserat = null;
	private InseratrechnungDto inseratkundebnDto = null;

	private WrapperGotoButton wbuRechnung = new WrapperGotoButton(
			WrapperGotoButton.GOTO_RECHNUNG_AUSWAHL);
	private WrapperTextField wtfRechnung = new WrapperTextField();
	private WrapperButton wbuKunde = new WrapperButton();
	private WrapperTextField wtfKunde = new WrapperTextField();
	private WrapperButton wbuAnsprechpartner = new WrapperButton();
	private WrapperTextField wtfAnsprechpartner = new WrapperTextField();

	private PanelQueryFLR panelQueryFLRKunde = null;
	private PanelQueryFLR panelQueryFLRAnsprechpartner = null;

	static final public String ACTION_SPECIAL_KUNDE_FROM_LISTE = "ACTION_SPECIAL_KUNDE_FROM_LISTE";
	static final public String ACTION_SPECIAL_ANSPRECHPARTNER_FROM_LISTE = "ACTION_SPECIAL_ANSPRECHPARTNER_FROM_LISTE";

	public PanelInseratKunden(InternalFrameInserat internalFrame,
			String add2TitleI, Object pk) throws Throwable {
		super(internalFrame, add2TitleI, pk);
		this.internalFrameInserat = internalFrame;

		jbInit();
		setDefaults();
		initComponents();
		enableAllComponents(this, false);
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return null;
	}

	protected void setDefaults() {

	}

	private void dialogQueryKunde(ActionEvent e) throws Throwable {
		panelQueryFLRKunde = PartnerFilterFactory.getInstance()
				.createPanelFLRKunde(getInternalFrame(), true, true,
						inseratkundebnDto.getKundeIId());

		new DialogQuery(panelQueryFLRKunde);
	}

	private void dialogQueryAnsprechpartner(ActionEvent e) throws Throwable {
		// String key = (String) wcoReklamationart.getKeyOfSelectedItem();

		if (inseratkundebnDto.getKundeIId() == null) {
			DialogFactory.showModalDialog(
					LPMain.getTextRespectUISPr("lp.error"),
					LPMain.getTextRespectUISPr("lp.error.kundenichtgewaehlt"));
		} else {
			KundeDto kundeDto = DelegateFactory.getInstance()
					.getKundeDelegate()
					.kundeFindByPrimaryKey(inseratkundebnDto.getKundeIId());
			panelQueryFLRAnsprechpartner = PartnerFilterFactory.getInstance()
					.createPanelFLRAnsprechpartner(getInternalFrame(),
							kundeDto.getPartnerIId(),
							inseratkundebnDto.getAnsprechpartnerIId(), true,
							true);
			new DialogQuery(panelQueryFLRAnsprechpartner);
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
			inseratkundebnDto = DelegateFactory.getInstance()
					.getInseratDelegate()
					.inseratrechnungFindByPrimaryKey((Integer) key);

			dto2Components();

		}
	}

	public void eventActionNew(EventObject eventObject, boolean bLockMeI,
			boolean bNeedNoNewI) throws Throwable {

		if (internalFrameInserat.getTabbedPaneInserat()
				.darfInseratGeaendertWerden()) {

			super.eventActionNew(eventObject, true, false);
			leereAlleFelder(this);
			inseratkundebnDto = new InseratrechnungDto();
		}

	}

	public LockStateValue getLockedstateDetailMainKey() throws Throwable {
		LockStateValue lockStateValue = super.getLockedstateDetailMainKey();

		if (internalFrameInserat.getTabbedPaneInserat().getDto()
				.getIId() != null) {
			if (internalFrameInserat.getTabbedPaneInserat().getDto()
					.getStatusCNr().equals(LocaleFac.STATUS_VERRECHNET)
					|| internalFrameInserat.getTabbedPaneInserat()
							.getDto().getStatusCNr()
							.equals(LocaleFac.STATUS_TEILBEZAHLT)
					|| internalFrameInserat.getTabbedPaneInserat()
							.getDto().getStatusCNr()
							.equals(LocaleFac.STATUS_BEZAHLT)|| internalFrameInserat.getTabbedPaneInserat()
							.getDto().getStatusCNr()
							.equals(LocaleFac.STATUS_ERLEDIGT)) {
				lockStateValue = new LockStateValue(
						PanelBasis.LOCK_ENABLE_REFRESHANDPRINT_ONLY);
			}
		}

		return lockStateValue;
	}

	protected void dto2Components() throws Throwable {

		if (inseratkundebnDto.getRechnungpositionIId() != null) {
			RechnungPositionDto reposDto = DelegateFactory
					.getInstance()
					.getRechnungDelegate()
					.rechnungPositionFindByPrimaryKey(
							inseratkundebnDto.getRechnungpositionIId());
			RechnungDto rdDto = DelegateFactory.getInstance()
					.getRechnungDelegate()
					.rechnungFindByPrimaryKey(reposDto.getRechnungIId());
			wtfRechnung.setText(rdDto.getCNr());
			wbuRechnung.setOKey(rdDto.getIId());
		} else {
			wtfRechnung.setText(null);
			wbuRechnung.setOKey(null);
		}

		if (inseratkundebnDto.getKundeIId() != null) {
			KundeDto kundeDto = DelegateFactory.getInstance()
					.getKundeDelegate()
					.kundeFindByPrimaryKey(inseratkundebnDto.getKundeIId());
			wtfKunde.setText(kundeDto.getPartnerDto().formatTitelAnrede());
		} else {
			wtfKunde.setText(null);
		}

		if (inseratkundebnDto.getAnsprechpartnerIId() != null) {
			AnsprechpartnerDto ansprechpartnerDto = DelegateFactory
					.getInstance()
					.getAnsprechpartnerDelegate()
					.ansprechpartnerFindByPrimaryKey(
							inseratkundebnDto.getAnsprechpartnerIId());

			wtfAnsprechpartner.setText(ansprechpartnerDto.getPartnerDto()
					.formatTitelAnrede());
		} else {
			wtfAnsprechpartner.setText(null);
		}

	}

	protected void components2Dto() throws Throwable {
		inseratkundebnDto.setInseratIId(internalFrameInserat
				.getTabbedPaneInserat().getDto().getIId());
	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;
		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			if (e.getSource() == panelQueryFLRKunde) {
				Integer iIdKunde = (Integer) ((ISourceEvent) e.getSource())
						.getIdSelected();
				KundeDto kundeDto = DelegateFactory.getInstance()
						.getKundeDelegate().kundeFindByPrimaryKey(iIdKunde);

				DelegateFactory.getInstance().getKundeDelegate()
						.pruefeKunde(iIdKunde,null,getInternalFrame());

				wtfKunde.setText(kundeDto.getPartnerDto().formatTitelAnrede());
				inseratkundebnDto.setKundeIId(iIdKunde);

				wtfAnsprechpartner.setText(null);
				inseratkundebnDto.setAnsprechpartnerIId(null);
			} else if (e.getSource() == panelQueryFLRAnsprechpartner) {
				Integer iIdAnsprechpartner = (Integer) ((ISourceEvent) e
						.getSource()).getIdSelected();
				AnsprechpartnerDto ansprechpartnerDto = DelegateFactory
						.getInstance().getAnsprechpartnerDelegate()
						.ansprechpartnerFindByPrimaryKey(iIdAnsprechpartner);

				wtfAnsprechpartner.setText(ansprechpartnerDto.getPartnerDto()
						.formatTitelAnrede());
				inseratkundebnDto.setAnsprechpartnerIId(iIdAnsprechpartner);
			}

		} else if (e.getID() == ItemChangedEvent.ITEM_CHANGED) {

		} else if (e.getID() == ItemChangedEvent.ACTION_LEEREN) {
			if (e.getSource() == panelQueryFLRAnsprechpartner) {
				wtfAnsprechpartner.setText(null);
				inseratkundebnDto.setAnsprechpartnerIId(null);
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

		getInternalFrame().addItemChangedListener(this);

		wbuRechnung
				.setText(LPMain.getTextRespectUISPr("lp.rechnung.modulname"));
		wbuKunde.setText(LPMain.getTextRespectUISPr("button.kunde"));
		wbuKunde.setActionCommand(ACTION_SPECIAL_KUNDE_FROM_LISTE);
		wbuKunde.addActionListener(this);
		wtfKunde.setMandatoryField(true);
		wbuRechnung.setActivatable(false);
		wtfRechnung.setActivatable(false);
		// wtfKunde.setMandatoryField(true);
		wtfKunde.setColumnsMax(Facade.MAX_UNBESCHRAENKT);

		wbuAnsprechpartner.setText(LPMain
				.getTextRespectUISPr("button.ansprechpartner"));
		wbuAnsprechpartner
				.setActionCommand(ACTION_SPECIAL_ANSPRECHPARTNER_FROM_LISTE);
		wbuAnsprechpartner.addActionListener(this);

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

		jpaWorkingOn.add(wbuKunde, new GridBagConstraints(0, 0, 1, 1, 1, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wtfKunde, new GridBagConstraints(1, 0, 1, 1, 2, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wbuAnsprechpartner, new GridBagConstraints(0, 1, 1, 1,
				0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfAnsprechpartner, new GridBagConstraints(1, 1, 2, 1,
				0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wbuRechnung, new GridBagConstraints(0, 2, 1, 1, 0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfRechnung, new GridBagConstraints(1, 2, 2, 1, 0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));

		String[] aWhichButtonIUse = { ACTION_UPDATE, ACTION_SAVE,
				ACTION_DELETE, ACTION_DISCARD, };

		enableToolsPanelButtons(aWhichButtonIUse);

	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_INSERAT;
	}

	protected void eventActionDelete(ActionEvent e,
			boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable, Throwable {

		DelegateFactory.getInstance().getInseratDelegate()
				.removeInseratrechnung(inseratkundebnDto.getIId());
		this.setKeyWhenDetailPanel(null);
		super.eventActionDelete(e, false, false);

	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		if (e.getActionCommand().equals(ACTION_SPECIAL_KUNDE_FROM_LISTE)) {
			dialogQueryKunde(e);
		} else if (e.getActionCommand().equals(
				ACTION_SPECIAL_ANSPRECHPARTNER_FROM_LISTE)) {
			dialogQueryAnsprechpartner(e);
		}
	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {
		if (allMandatoryFieldsSetDlg()) {
			components2Dto();

			if (inseratkundebnDto.getIId() == null) {

				inseratkundebnDto.setIId(DelegateFactory.getInstance()
						.getInseratDelegate()
						.createInseratrechnung(inseratkundebnDto));

				setKeyWhenDetailPanel(inseratkundebnDto.getIId());
			} else {
				DelegateFactory.getInstance().getInseratDelegate()
						.updateInseratrechnung(inseratkundebnDto);

			}

			super.eventActionSave(e, true);
			if (getInternalFrame().getKeyWasForLockMe() == null) {
				getInternalFrame().setKeyWasForLockMe(
						inseratkundebnDto.getInseratIId().toString());
			}
			eventYouAreSelected(false);

		}
	}

}
