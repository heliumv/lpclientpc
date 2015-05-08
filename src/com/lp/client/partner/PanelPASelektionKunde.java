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
package com.lp.client.partner;

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
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.system.SystemFilterFactory;
import com.lp.client.util.fastlanereader.gui.QueryType;
import com.lp.server.partner.ejb.PASelektionPK;
import com.lp.server.partner.fastlanereader.generated.service.FLRPASelektionPK;
import com.lp.server.partner.service.PASelektionDto;
import com.lp.server.partner.service.SelektionDto;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;

@SuppressWarnings("static-access")
/*
 * <p> Diese Klasse kuemmert sich Partnerselektion.</p>
 * 
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 * 
 * <p>Erstellung: Josef Ornetsmueller; dd.12.05</p>
 * 
 * <p>@author $Author: christian $</p>
 * 
 * @version not attributable Date $Date: 2009/07/13 09:42:41 $
 */
public class PanelPASelektionKunde extends PanelBasis {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private TabbedPaneKunde tpPartner = null;

	private JPanel jpaWorking = null;
	private GridBagLayout gblWorkingPanel = null;
	private Border border = null;
	private GridBagLayout gblAussen = null;

	private WrapperButton wbuSelektion = null;
	static final private String ACTION_SPECIAL_FLR_SELEKTION = "action_special_flr_selektion";
	private PanelQueryFLR panelQueryFLRSelektionAuswahl = null;
	private WrapperTextField wtfSelektion = null;
	private WrapperLabel wlaBemerkung = null;
	private WrapperTextField wtfBemerkung = null;

	private PASelektionDto pASelektionDto = new PASelektionDto();

	public PanelPASelektionKunde(InternalFrame internalFrame,
			String add2TitleI, Object keyI, TabbedPaneKunde tpPartner)
			throws Throwable {
		super(internalFrame, add2TitleI, keyI);
		this.tpPartner = tpPartner;
		jbInit();
		initComponents();
		initPanel();
	}

	/**
	 * jbInit
	 * 
	 * @throws Throwable
	 */
	private void jbInit() throws Throwable {
		getInternalFrame().addItemChangedListener(this);

		// buttons.
		resetToolsPanel();

		String[] aButton = { PanelBasis.ACTION_UPDATE, PanelBasis.ACTION_SAVE,
				PanelBasis.ACTION_DELETE, PanelBasis.ACTION_DISCARD };
		enableToolsPanelButtons(aButton);

		// von hier ...
		border = BorderFactory.createEmptyBorder(10, 10, 10, 10);
		setBorder(border);

		wbuSelektion = new WrapperButton();
		wbuSelektion.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.button.selektion"));
		wbuSelektion.setActionCommand(ACTION_SPECIAL_FLR_SELEKTION);
		wbuSelektion.addActionListener(this);

		wtfSelektion = new WrapperTextField();
		wtfSelektion.setActivatable(false);
		wtfSelektion.setMandatoryFieldDB(true);

		wlaBemerkung = new WrapperLabel();
		wlaBemerkung.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.bemerkung"));
		wtfBemerkung = new WrapperTextField();

		// das aussenpanel hat immer das gridbaglayout.
		gblAussen = new GridBagLayout();
		setLayout(gblAussen);

		// actionpanel von Oberklasse holen und anhaengen.
		add(getToolsPanel(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
				new Insets(0, 0, 0, 0), 0, 0));

		// Statusbar an den unteren Rand des Panels haengen.
		add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						0, 0, 0, 0), 0, 0));

		// Workingpanel
		jpaWorking = new JPanel();
		gblWorkingPanel = new GridBagLayout();
		jpaWorking.setLayout(gblWorkingPanel);
		add(jpaWorking, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
				GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(
						0, 0, 0, 0), 0, 0));

		// Ab hier einhaengen.
		// Zeile
		jpaWorking.add(wbuSelektion, new GridBagConstraints(0, iZeile, 1, 1,
				0.2, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorking.add(wtfSelektion, new GridBagConstraints(1, iZeile, 1, 1,
				0.2, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		// Zeile
		iZeile++;
		jpaWorking.add(wlaBemerkung, new GridBagConstraints(0, iZeile, 1, 1,
				0.2, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorking.add(wtfBemerkung, new GridBagConstraints(1, iZeile, 1, 1,
				0.2, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {

		super.eventYouAreSelected(false);

		Object key = getKeyWhenDetailPanel();

		String sT = null;

		if (key == null
				|| (key != null && key.equals(LPMain.getLockMeForNew()))) {
			// Neu.
			leereAlleFelder(this);
			clearStatusbar();
			pASelektionDto.setBIsNew(true);
			sT = getTabbedPaneKunde().getKundeDto().getPartnerDto()
					.formatFixTitelName1Name2();
		} else {
			// Update.
			FLRPASelektionPK pASelektionPK = (FLRPASelektionPK) key;
			pASelektionDto = DelegateFactory.getInstance().getPartnerDelegate()
					.pASelektionFindByPrimaryKey(
							new PASelektionPK(pASelektionPK.getPartner_i_id(),
									pASelektionPK.getSelektion_i_id()));
			dto2Components();

			pASelektionDto.setBIsNew(false);

			String sB = pASelektionDto.getCBemerkung();
			sT = getTabbedPaneKunde().getKundeDto().getPartnerDto()
					.formatFixTitelName1Name2()
					+ (sB == null ? "" : " | " + sB);
		}
		getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_AS_I_LIKE, sT);
	}

	/**
	 * dto2Components
	 * 
	 * @throws Throwable
	 */
	private void dto2Components() throws Throwable {
		wtfBemerkung.setText(pASelektionDto.getCBemerkung());
		SelektionDto selektionDto = DelegateFactory.getInstance()
				.getPartnerServicesDelegate().selektionFindByPrimaryKey(
						pASelektionDto.getSelektionIId());
		wtfSelektion.setText(selektionDto != null ? selektionDto.getCNr()
				: null);
	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {

		if (allMandatoryFieldsSetDlg()) {

			checkLockedDlg();

			components2Dto();
			if (pASelektionDto.isBIsNew()) {
				// Create.
				PASelektionPK pASelektionPK = DelegateFactory.getInstance()
						.getPartnerDelegate().createPASelektion(pASelektionDto);

				setKeyWhenDetailPanel(pASelektionPK);
			} else {
				// Update.
				DelegateFactory.getInstance().getPartnerDelegate()
						.updatePASelektion(pASelektionDto);
			}
			super.eventActionSave(e, false);
			eventYouAreSelected(false);
		}
	}

	protected void eventActionDelete(ActionEvent e,
			boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {

		if (!isLockedDlg()) {
			DelegateFactory.getInstance().getPartnerDelegate()
					.removePASelektion(
							new PASelektionPK(pASelektionDto.getPartnerIId(),
									pASelektionDto.getSelektionIId()));
			pASelektionDto = new PASelektionDto();

			super.eventActionDelete(e, false, false);
		}
	}

	private void components2Dto() {
		pASelektionDto.setPartnerIId(getTabbedPaneKunde().getKundeDto()
				.getPartnerIId());
		pASelektionDto.setCBemerkung(wtfBemerkung.getText());
	}

	private void initPanel() throws Throwable {
		// nothing here
	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_PARTNER;
	}

	private TabbedPaneKunde getTabbedPaneKunde() {
		return tpPartner;
	}

	public void eventActionNew(EventObject eventObject, boolean bLockMeI,
			boolean bNeedNoNewI) throws Throwable {

		super.eventActionNew(eventObject, true, false);

		if (!bNeedNoNewI) {
			pASelektionDto = new PASelektionDto();
			setDefaults();
		}
	}

	/**
	 * setDefaults
	 * 
	 * @throws Throwable
	 */
	private void setDefaults() throws Throwable {
		// nothing here
	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {

		if (e.getActionCommand().equals(ACTION_SPECIAL_FLR_SELEKTION)) {

			panelQueryFLRSelektionAuswahl = PartnerFilterFactory.getInstance()
					.createPanelFLRSelektion(
							getInternalFrame(),
							false,
							pASelektionDto.getSelektionIId());
			new DialogQuery(
					panelQueryFLRSelektionAuswahl);
		} 
		
	}

	protected void eventItemchanged(EventObject eI) throws ExceptionLP,
			Throwable {

		ItemChangedEvent e = (ItemChangedEvent) eI;

		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {

			if (e.getSource() == panelQueryFLRSelektionAuswahl) {
				Integer iId = (Integer) ((ISourceEvent) e.getSource())
						.getIdSelected();
				pASelektionDto.setSelektionIId(iId);

				SelektionDto selektionDto = null;
				if (iId != null) {
					selektionDto = DelegateFactory.getInstance()
							.getPartnerServicesDelegate()
							.selektionFindByPrimaryKey(iId);
					wtfSelektion.setText(selektionDto != null ? selektionDto
							.getCNr() : null);
				}
			}
		}
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wbuSelektion;
	}
}
