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

import com.lp.client.benutzer.InternalFrameBenutzer;
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
import com.lp.client.system.SystemFilterFactory;
import com.lp.server.artikel.service.ShopgruppewebshopDto;
import com.lp.server.artikel.service.WebshopDto;
import com.lp.server.benutzer.service.RollerechtDto;
import com.lp.server.util.Facade;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.FilterKriteriumDirekt;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;

@SuppressWarnings("static-access")
public class PanelShopgruppewebshop extends PanelBasis {

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
	
	private ShopgruppewebshopDto shopgruppewebshopDto = null;
	private WrapperButton wbuWebshop = new WrapperButton();
	private WrapperTextField wtfWebshop = new WrapperTextField();

	private PanelQueryFLR panelQueryFLRWebshop = null;

	static final public String ACTION_SPECIAL_WEBSHOP_FROM_LISTE = "action_webshop_from_liste";

	public PanelShopgruppewebshop(InternalFrame internalFrame,
			String add2TitleI, Object pk) throws Throwable {
		super(internalFrame, add2TitleI, pk);
		internalFrameArtikel = (InternalFrameArtikel) internalFrame;
		jbInit();
		setDefaults();
		initComponents();
		enableAllComponents(this, false);
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wbuWebshop;
	}

	protected void setDefaults() {

	}

	void dialogQueryWebshopFromListe(ActionEvent e) throws Throwable {
		String[] aWhichButtonIUse = { PanelBasis.ACTION_REFRESH };

		panelQueryFLRWebshop = new PanelQueryFLR(null, SystemFilterFactory
				.getInstance().createFKMandantCNr(),
				QueryParameters.UC_ID_WEBSHOP, aWhichButtonIUse,
				getInternalFrame(), LPMain.getInstance().getTextRespectUISPr(
						"lp.webshop"));

		panelQueryFLRWebshop.befuellePanelFilterkriterienDirekt(
				new FilterKriteriumDirekt("c_bez", "",
						FilterKriterium.OPERATOR_LIKE, LPMain.getInstance()
								.getTextRespectUISPr("lp.webshop"),
						FilterKriteriumDirekt.PROZENT_TRAILING, true, true,
						Facade.MAX_UNBESCHRAENKT), null);

		panelQueryFLRWebshop
				.setSelectedId(shopgruppewebshopDto.getWebshopIId());

		new DialogQuery(panelQueryFLRWebshop);
	}

	public void eventActionNew(EventObject eventObject, boolean bLockMeI,
			boolean bNeedNoNewI) throws Throwable {
		super.eventActionNew(eventObject, true, false);
		leereAlleFelder(this);
		shopgruppewebshopDto = new ShopgruppewebshopDto();
	}

	protected void dto2Components() throws ExceptionLP, Throwable {
		WebshopDto webshopDto = DelegateFactory.getInstance()
				.getArtikelDelegate()
				.webshopFindByPrimaryKey(shopgruppewebshopDto.getWebshopIId());

		wtfWebshop.setText(webshopDto.getCBez());

	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {

		super.eventYouAreSelected(false);
		Object key = getKeyWhenDetailPanel();

		if (key == null || (key.equals(LPMain.getLockMeForNew()))) {
			leereAlleFelder(this);
			clearStatusbar();
		} else {
			shopgruppewebshopDto = DelegateFactory.getInstance()
					.getArtikelDelegate()
					.shopgruppewebshopFindByPrimaryKey((Integer) key);
			dto2Components();

		}
	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;
		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			if (e.getSource() == panelQueryFLRWebshop) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();

				WebshopDto webshopDto = DelegateFactory.getInstance()
						.getArtikelDelegate()
						.webshopFindByPrimaryKey((Integer) key);

				wtfWebshop.setText(webshopDto.getCBez());
				shopgruppewebshopDto.setWebshopIId((Integer) key);
			}
		}

	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		if (e.getActionCommand().equals(ACTION_SPECIAL_WEBSHOP_FROM_LISTE)) {
			dialogQueryWebshopFromListe(e);
		}
	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {
		if (allMandatoryFieldsSetDlg()) {
			components2Dto();
			if (shopgruppewebshopDto.getIId() == null) {
				shopgruppewebshopDto.setShopgruppeIId(internalFrameArtikel
						.getTabbedPaneShopgruppe().getShopgruppeDto().getIId());
				shopgruppewebshopDto.setIId(DelegateFactory.getInstance()
						.getArtikelDelegate()
						.createShopgruppewebshop(shopgruppewebshopDto));
				setKeyWhenDetailPanel(shopgruppewebshopDto.getIId());
			} else {
				DelegateFactory.getInstance().getArtikelDelegate()
						.updateShopgruppewebshop(shopgruppewebshopDto);
			}
			super.eventActionSave(e, true);

			if (getInternalFrame().getKeyWasForLockMe() == null) {
				getInternalFrame().setKeyWasForLockMe(
						internalFrameArtikel.getTabbedPaneShopgruppe()
								.getShopgruppeDto().getIId().toString());
			}
			eventYouAreSelected(false);
		}
	}

	protected void components2Dto() {
	}

	protected void eventActionDelete(ActionEvent e,
			boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {
		
		DelegateFactory.getInstance().getArtikelDelegate().removeShopgruppewebshop(shopgruppewebshopDto);
		this.setKeyWhenDetailPanel(null);
		super.eventActionDelete(e, false, false);
		shopgruppewebshopDto = new ShopgruppewebshopDto();
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

		wbuWebshop.setText(LPMain.getInstance().getTextRespectUISPr("lp.webshop")
				+ "...");
		wbuWebshop.setActionCommand(PanelShopgruppewebshop.ACTION_SPECIAL_WEBSHOP_FROM_LISTE);
		wbuWebshop.addActionListener(this);

		

		wtfWebshop.setMandatoryField(true);
		wtfWebshop.setActivatable(false);
		wtfWebshop.setColumnsMax(100);
		
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
		jpaWorkingOn.add(wbuWebshop, new GridBagConstraints(0, 0, 1, 1, 0.05,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfWebshop, new GridBagConstraints(1, 0, 1, 1, 0.2, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(0, 0, 0, 0), 0, 0));
		String[] aWhichButtonIUse = { ACTION_UPDATE, ACTION_SAVE,
				ACTION_DELETE, ACTION_DISCARD, };

		enableToolsPanelButtons(aWhichButtonIUse);

	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_SHOPGRUPPE;
	}

}
