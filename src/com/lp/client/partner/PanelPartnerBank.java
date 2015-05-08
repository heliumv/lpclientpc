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
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperIBANField;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.component.WrapperTextNumberField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.util.fastlanereader.gui.QueryType;
import com.lp.server.partner.service.BankDto;
import com.lp.server.partner.service.BankFac;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.partner.service.PartnerbankDto;
import com.lp.server.system.jcr.service.JCRDocFac;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.util.Facade;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;

@SuppressWarnings("static-access")
/**
 * <p> Diese Klasse kuemmert sich um Partner und deren Banken.</p>
 *
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 *
 * <p>Erstellung: Josef Ornetsmueller; 24.03.05</p>
 *
 * <p>@author $Author: christian $</p>
 *
 * @version $Revision: 1.8 $
 * Date $Date: 2012/08/09 08:02:26 $
 */
abstract public class PanelPartnerBank extends PanelBasis {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Border border = null;
	private GridBagLayout gridBagLayoutAll = null;
	private PanelQueryFLR panelQueryFLRBanken = null;
	private JPanel jpaWorkingOn = null;
	private GridBagLayout gridBagLayout = null;
	private WrapperButton wbuBanken = null;
	private WrapperTextField wtfBank = null;
	private WrapperLabel wlaIBAN = null;
	private WrapperIBANField wtfIBAN = null;
	private WrapperLabel wlaBIC = null;
	private WrapperTextField wtfBIC = null;
	private WrapperLabel wlaKtonr = null;
	private WrapperTextField wtfKtonr = null;
	private WrapperLabel wlaSort = null;
	private WrapperLabel wlaLandplzort = new WrapperLabel("");

	private PartnerbankDto partnerbankDto = null;

	protected WrapperTextNumberField wtfSort = null;

	static final public String ACTION_SPECIAL_FLR_BANKEN = "action_special_flr_banken";

	public PanelPartnerBank(InternalFrame internalFrame, String add2TitleI,
			Object keyI) throws Throwable {
		super(internalFrame, add2TitleI, keyI);
		jbInit();
		initComponents();
	}

	abstract protected PartnerDto getPartnerDto();

	private void jbInit() throws Throwable {
		getInternalFrame().addItemChangedListener(this);

		// Buttons.
		String[] aButton = { PanelBasis.ACTION_UPDATE, PanelBasis.ACTION_SAVE,
				PanelBasis.ACTION_DELETE, PanelBasis.ACTION_DISCARD };
		enableToolsPanelButtons(aButton);

		border = BorderFactory.createEmptyBorder(10, 10, 10, 10);
		setBorder(border);

		// Das Aussenpanel hat immer das Gridbaglayout.
		gridBagLayoutAll = new GridBagLayout();
		setLayout(gridBagLayoutAll);

		// Actionpanel von Oberklasse holen und einhaengen.
		add(getToolsPanel(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
				new Insets(0, 0, 0, 0), 0, 0));

		jpaWorkingOn = new JPanel();
		gridBagLayout = new GridBagLayout();
		jpaWorkingOn.setLayout(gridBagLayout);
		add(jpaWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
				GridBagConstraints.SOUTH, GridBagConstraints.BOTH, new Insets(
						0, 0, 0, 0), 0, 0));

		// Statusbar an den unteren Rand des Panels haengen.
		add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						0, 0, 0, 0), 0, 0));

		wbuBanken = new WrapperButton();
		wbuBanken.setText(LPMain.getInstance().getTextRespectUISPr(
				"button.bank"));
		wbuBanken.setActionCommand(ACTION_SPECIAL_FLR_BANKEN);
		wbuBanken.addActionListener(this);

		wtfBank = new WrapperTextField(Facade.MAX_UNBESCHRAENKT);
		wtfBank.setActivatable(false);
		wtfBank.setMandatoryFieldDB(true);

		wtfIBAN = new WrapperIBANField();
		wlaIBAN = new WrapperLabel(LPMain.getInstance().getTextRespectUISPr(
				"lp.iban"));

		wtfBIC = new WrapperTextField(BankFac.MAX_IBAN);
		wtfBIC.setActivatable(false);
		wlaBIC = new WrapperLabel(LPMain.getInstance().getTextRespectUISPr(
				"lp.bic"));

		wtfKtonr = new WrapperTextField(BankFac.MAX_KTONR);
		wlaKtonr = new WrapperLabel(LPMain.getInstance().getTextRespectUISPr(
				"lp.kontonr"));
		wlaSort = new WrapperLabel();
		wlaSort = new WrapperLabel(LPMain.getInstance().getTextRespectUISPr(
				"label.sortierung"));
		wtfSort = new WrapperTextNumberField();
		wtfSort.setMandatoryFieldDB(true);
		wtfSort.setMinimumValue(new Integer(0));
		wtfSort.setMaximumDigits(4);
		wtfSort.setMaximumValue(new Integer(9999));

		wlaLandplzort.setHorizontalAlignment(SwingConstants.LEFT);

		// Ab hier einhaengen.
		// Zeile
		jpaWorkingOn.add(wbuBanken, new GridBagConstraints(0, iZeile, 1, 1,
				0.2, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfBank, new GridBagConstraints(1, iZeile, 1, 1, 0.4,
				0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));

		// Zeile
		iZeile++;
		jpaWorkingOn.add(wlaKtonr, new GridBagConstraints(0, iZeile, 1, 1, 0,
				0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfKtonr, new GridBagConstraints(1, iZeile, 1, 1, 0,
				0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		// Zeile; ich bestimme die Breiten.
		iZeile++;
		jpaWorkingOn.add(wlaIBAN, new GridBagConstraints(0, iZeile, 1, 1, 0, 0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfIBAN, new GridBagConstraints(1, iZeile, 1, 1, 0, 0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wlaBIC, new GridBagConstraints(0, iZeile, 1, 1, 0, 0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfBIC, new GridBagConstraints(1, iZeile, 1, 1, 0, 0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						2, 2, 2, 2), 0, 0));

		// Zeile
		iZeile++;
		jpaWorkingOn.add(wlaSort, new GridBagConstraints(0, iZeile, 1, 1, 0.0,
				0.0, GridBagConstraints.NORTH, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfSort, new GridBagConstraints(1, iZeile, 1, 1, 0.0,
				0.0, GridBagConstraints.NORTH, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		// Zeile
		iZeile++;
		jpaWorkingOn.add(wlaLandplzort, new GridBagConstraints(1, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));

	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {

		if (e.getActionCommand().equals(ACTION_SPECIAL_FLR_BANKEN)) {
			String[] aWhichButtonIUse = null;

			QueryType[] querytypes = null;
			FilterKriterium[] filter = null;

			panelQueryFLRBanken = new PanelQueryFLR(querytypes, filter,
					QueryParameters.UC_ID_BANK, aWhichButtonIUse,
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("part.kund.banken"));

			panelQueryFLRBanken.befuellePanelFilterkriterienDirekt(
					PartnerFilterFactory.getInstance().createFKDBankName(),
					PartnerFilterFactory.getInstance().createFKDBankBLZ());

			DialogQuery dialogQueryAnsprechpartner = new DialogQuery(
					panelQueryFLRBanken);
		}
	}

	protected void eventItemchanged(EventObject eI) throws ExceptionLP,
			Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;

		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			if (e.getSource() == panelQueryFLRBanken) {
				Integer iId = (Integer) ((ISourceEvent) e.getSource())
						.getIdSelected();
				if (iId != null) {
					partnerbankDto.setBankPartnerIId(iId);
					BankDto bankDto = DelegateFactory.getInstance()
							.getPartnerbankDelegate().bankFindByPrimaryKey(iId);
					wtfBank.setText(bankDto.getPartnerDto().formatAnrede());
					wtfBIC.setText(bankDto.getCBic());
				}
			}
		}
	}

	protected void setDefaults() throws Throwable {
		// Vorbelegungen.
		partnerbankDto.setPartnerIId(getPartnerDto().getIId());
	}

	public void eventActionNew(EventObject eventObject, boolean bLockMeI,
			boolean bNeedNoNewI) throws Throwable {

		super.eventActionNew(eventObject, true, false);
		wlaLandplzort.setText("");
		partnerbankDto = new PartnerbankDto();
		setDefaults();
	}

	protected void dto2Components() throws Throwable {
		wtfIBAN.setIban(partnerbankDto.getCIban());
		wtfIBAN.pruefeIBAN();
		wtfKtonr.setText(partnerbankDto.getCKtonr());

		BankDto bankDto = DelegateFactory.getInstance()
				.getPartnerbankDelegate()
				.bankFindByPrimaryKey(partnerbankDto.getBankPartnerIId());
		wtfBank.setText(bankDto.getPartnerDto().formatAnrede());
		wtfBIC.setText(bankDto.getCBic());
		wtfSort.setInteger(partnerbankDto.getISort());

		if (bankDto.getPartnerDto() != null) {
			if (bankDto.getPartnerDto().getLandplzortDto() != null) {
				wlaLandplzort.setText(bankDto.getPartnerDto()
						.getLandplzortDto().formatLandPlzOrt());
			} else {
				wlaLandplzort.setText("");
			}
		} else {
			wlaLandplzort.setText("");
		}

	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {

		super.eventYouAreSelected(false);

		Object iId = getKeyWhenDetailPanel();
		if (iId == null
				|| (iId != null && iId.equals(LPMain.getLockMeForNew()))) {
			// Neu.
			leereAlleFelder(this);
			clearStatusbar();
			getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_AS_I_LIKE,
					getPartnerDto().formatFixTitelName1Name2());

			Integer iSort = DelegateFactory.getInstance()
					.getPartnerbankDelegate()
					.getMaxISort(getPartnerDto().getIId());
			iSort = new Integer(iSort + 1);
			wtfSort.setInteger(iSort);
		} else {
			// Update.
			partnerbankDto = DelegateFactory.getInstance()
					.getPartnerbankDelegate()
					.partnerbankFindByPrimaryKey((Integer) iId);

			BankDto bankDto = DelegateFactory.getInstance()
					.getPartnerbankDelegate()
					.bankFindByPrimaryKey(partnerbankDto.getBankPartnerIId());
			dto2Components();

			getInternalFrame().setLpTitle(
					InternalFrame.TITLE_IDX_AS_I_LIKE,
					getPartnerDto().formatFixTitelName1Name2() + " | "
							+ bankDto.getPartnerDto().formatAnrede());
		}
	}

	protected void components2Dto() throws Throwable {
		partnerbankDto.setPartnerIId(getPartnerDto().getIId());
		partnerbankDto.setCIban(wtfIBAN.getIban());
		partnerbankDto.setCKtonr(wtfKtonr.getText());
		partnerbankDto.setISort(wtfSort.getInteger());
	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {

		if (allMandatoryFieldsSetDlg()) {
			components2Dto();
			BankDto bankDto = DelegateFactory.getInstance()
					.getPartnerbankDelegate()
					.bankFindByPrimaryKey(partnerbankDto.getBankPartnerIId());

			if (wtfIBAN.getFormattedText() != null
					&& wtfIBAN.pruefeIBAN() == false) {

				return;
			}

			if (bankDto.getCBlz() == null && partnerbankDto.getCIban() == null) {
				DialogFactory.showModalDialog(LPMain.getInstance()
						.getTextRespectUISPr("lp.error"), LPMain.getInstance()
						.getTextRespectUISPr("part.blznichtangegeben.error"));
				wtfKtonr.setText(null);
				return;
			}

			if (partnerbankDto.getCKtonr() == null
					&& partnerbankDto.getCIban() == null) {
				DialogFactory.showModalDialog(LPMain.getInstance()
						.getTextRespectUISPr("lp.error"), LPMain.getInstance()
						.getTextRespectUISPr("part.ktoiban.error"));
				return;

			}

			Integer iId = null;
			if (partnerbankDto.getIId() == null) {
				iId = DelegateFactory.getInstance().getPartnerbankDelegate()
						.createPartnerbank(partnerbankDto);
				;
				partnerbankDto.setIId(iId);
				setKeyWhenDetailPanel(iId);
			} else {
				DelegateFactory.getInstance().getPartnerbankDelegate()
						.updatePartnerbank(partnerbankDto);
			}
			super.eventActionSave(e, false);
			eventYouAreSelected(false);
		}
	}

	protected void eventActionDelete(ActionEvent e,
			boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {

		if (!isLockedDlg()) {
			DelegateFactory.getInstance().getPartnerbankDelegate()
					.removePartnerbank(partnerbankDto);

			partnerbankDto = new PartnerbankDto();
			this.setKeyWhenDetailPanel(null);

			super.eventActionDelete(e, false, false);
		}
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wbuBanken;
	}
}
