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
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.system.SystemFilterFactory;
import com.lp.client.util.fastlanereader.gui.QueryType;
import com.lp.server.artikel.service.ArtikelFac;
import com.lp.server.artikel.service.ArtikelkommentarartDto;
import com.lp.server.artikel.service.ArtikelkommentarartsprDto;
import com.lp.server.partner.service.BrancheDto;
import com.lp.server.partner.service.PartnerFac;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;

@SuppressWarnings("static-access")
public class PanelArtikelkommentarart extends PanelBasis {

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
	private WrapperLabel wlaKennung = new WrapperLabel();
	private WrapperTextField wtfKennung = new WrapperTextField();
	private ArtikelkommentarartDto artikelkommentarartDto = null;
	private WrapperLabel wlaBezeichnung = new WrapperLabel();
	private WrapperTextField wtfBezeichnung = new WrapperTextField();
	protected PanelQueryFLR panelQueryFLRBranche = null;
	private WrapperCheckBox wcbWebshop = new WrapperCheckBox();
	private WrapperCheckBox wcbTooltip = new WrapperCheckBox();

	static final private String ACTION_SPECIAL_FLR_BRANCHE = "action_special_flr_branche";
	protected WrapperButton wbuBranche = null;
	protected WrapperTextField wtfBranche = null;

	public PanelArtikelkommentarart(InternalFrame internalFrame,
			String add2TitleI, Object pk) throws Throwable {
		super(internalFrame, add2TitleI, pk);
		jbInit();
		setDefaults();
		initComponents();
		enableAllComponents(this, false);
	}

	protected void setDefaults() {
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wtfKennung;
	}

	public void eventActionNew(EventObject eventObject, boolean bLockMeI,
			boolean bNeedNoNewI) throws Throwable {
		super.eventActionNew(eventObject, true, false);

		artikelkommentarartDto = new ArtikelkommentarartDto();

		leereAlleFelder(this);
	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		if (e.getActionCommand().equals(ACTION_SPECIAL_FLR_BRANCHE)) {
			String[] aWhichButtonIUse = SystemFilterFactory.getInstance()
					.createButtonArray(false, true);

			final QueryType[] querytypes = null;
			final FilterKriterium[] filters = null;
			panelQueryFLRBranche = new PanelQueryFLR(querytypes, filters,
					QueryParameters.UC_ID_BRANCHE, aWhichButtonIUse,
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("lp.branche"));

			panelQueryFLRBranche.setSelectedId(artikelkommentarartDto
					.getBrancheId());

			new DialogQuery(panelQueryFLRBranche);
		}
	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;
		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			if (e.getSource() == panelQueryFLRBranche) {
				Integer key = (Integer) ((ISourceEvent) e.getSource())
						.getIdSelected();
				if (key != null) {
					BrancheDto brancheDto = DelegateFactory.getInstance()
							.getPartnerDelegate().brancheFindByPrimaryKey(key);
					artikelkommentarartDto.setBrancheId(key);
					wtfBranche.setText(brancheDto.getBezeichnung());
				}
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_LEEREN) {
			if (e.getSource() == panelQueryFLRBranche) {
				artikelkommentarartDto.setBrancheId(null);
				wtfBranche.setText(null);
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

		wlaKennung.setText(LPMain.getInstance().getTextRespectUISPr(
				"label.kennung"));
		wtfKennung.setColumnsMax(ArtikelFac.MAX_ARTIKELGRUPPE_NAME);
		wtfKennung.setText("");
		wtfKennung.setMandatoryField(true);

		wlaBezeichnung.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.bezeichnung"));
		wtfBezeichnung.setToolTipText("");
		wtfBezeichnung.setColumnsMax(ArtikelFac.MAX_ARTIKELGRUPPE_BEZEICHNUNG);
		wtfBezeichnung.setText("");

		wcbWebshop.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.webshop"));
		wcbTooltip.setText(LPMain.getInstance().getTextRespectUISPr(
				"artikel.artikelkommentarart.tooltip"));

		wbuBranche = new WrapperButton();
		wbuBranche.setActionCommand(ACTION_SPECIAL_FLR_BRANCHE);

		wbuBranche.addActionListener(this);

		wbuBranche.setText(LPMain.getInstance().getTextRespectUISPr(
				"button.branche"));

		wtfBranche = new WrapperTextField(PartnerFac.MAX_BRANCHE);
		wtfBranche.setActivatable(false);

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
		jpaWorkingOn.add(wlaKennung, new GridBagConstraints(0, 0, 1, 1, 0.1,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfKennung, new GridBagConstraints(1, 0, 1, 1, 0.1,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaBezeichnung, new GridBagConstraints(2, 0, 1, 1,
				0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfBezeichnung, new GridBagConstraints(3, 0, 1, 1,
				0.2, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		
		jpaWorkingOn.add(wcbWebshop, new GridBagConstraints(1, 1, 1, 1, 0.1,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		
		jpaWorkingOn.add(wbuBranche, new GridBagConstraints(2, 1, 1, 1, 0.0,
				0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfBranche, new GridBagConstraints(3, 1, 1, 1, 0.0,
				0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		
		
		jpaWorkingOn.add(wcbTooltip, new GridBagConstraints(1, 2, 2, 1, 0.1,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		

		String[] aWhichButtonIUse = { ACTION_UPDATE, ACTION_SAVE,
				ACTION_DELETE, ACTION_DISCARD, };

		enableToolsPanelButtons(aWhichButtonIUse);

	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_ARTIKELGRUPPE;
	}

	protected void eventActionDelete(ActionEvent e,
			boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {
		DelegateFactory.getInstance().getArtikelkommentarDelegate()
				.removeArtikelkommentarart(artikelkommentarartDto);
		this.setKeyWhenDetailPanel(null);
		super.eventActionDelete(e, false, false);
	}

	protected void components2Dto() {
		artikelkommentarartDto.setCNr(wtfKennung.getText());
		if (artikelkommentarartDto.getArtikelkommentartartsprDto() == null) {
			artikelkommentarartDto
					.setArtikelkommentartartsprDto(new ArtikelkommentarartsprDto());
		}
		artikelkommentarartDto.getArtikelkommentartartsprDto().setCBez(
				wtfBezeichnung.getText());
		artikelkommentarartDto.setBWebshop(wcbWebshop.getShort());
		artikelkommentarartDto.setBTooltip(wcbTooltip.getShort());
	}

	protected void dto2Components() throws Throwable {
		wtfKennung.setText(artikelkommentarartDto.getCNr());
		if (artikelkommentarartDto.getArtikelkommentartartsprDto() != null) {
			wtfBezeichnung.setText(artikelkommentarartDto
					.getArtikelkommentartartsprDto().getCBez());
		} else {
			wtfBezeichnung.setText("");
		}
		wcbWebshop.setShort(artikelkommentarartDto.getBWebshop());
		wcbTooltip.setShort(artikelkommentarartDto.getBTooltip());

		if (artikelkommentarartDto.getBrancheId() != null) {
			BrancheDto brancheDto = DelegateFactory
					.getInstance()
					.getPartnerDelegate()
					.brancheFindByPrimaryKey(
							artikelkommentarartDto.getBrancheId());
			wtfBranche.setText(brancheDto.getBezeichnung());
		} else {
			wtfBranche.setText(null);
		}

	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {
		if (allMandatoryFieldsSetDlg()) {
			components2Dto();
			if (artikelkommentarartDto.getIId() == null) {
				artikelkommentarartDto.setIId(DelegateFactory.getInstance()
						.getArtikelkommentarDelegate()
						.createArtikelkommentarart(artikelkommentarartDto));
				setKeyWhenDetailPanel(artikelkommentarartDto.getIId());
			} else {
				DelegateFactory.getInstance().getArtikelkommentarDelegate()
						.updateArtikelkommentarart(artikelkommentarartDto);
			}
			super.eventActionSave(e, true);
			if (getInternalFrame().getKeyWasForLockMe() == null) {
				getInternalFrame().setKeyWasForLockMe(
						artikelkommentarartDto.getIId() + "");
			}
			eventYouAreSelected(false);
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
			artikelkommentarartDto = DelegateFactory.getInstance()
					.getArtikelkommentarDelegate()
					.artikelkommentarartFindByPrimaryKey((Integer) key);
			dto2Components();
		}
	}
}
