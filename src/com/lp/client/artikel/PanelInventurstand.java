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
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.InventurstandDto;
import com.lp.server.artikel.service.LagerDto;

@SuppressWarnings("static-access")
public class PanelInventurstand extends PanelBasis {

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
	private WrapperLabel wlaArtikel = new WrapperLabel();
	private WrapperLabel wlaArtikelLabel = new WrapperLabel();
	private WrapperLabel wlaLager = new WrapperLabel();
	private WrapperLabel wlaLagerLabel = new WrapperLabel();
	private InventurstandDto inventurstandDto = null;

	private WrapperLabel wlaInventurmenge = new WrapperLabel();
	private WrapperLabel wlaInventurpreis = new WrapperLabel();

	private WrapperNumberField wnfInventurmenge = new WrapperNumberField();
	private WrapperNumberField wnfInventurpreis = new WrapperNumberField();

	private WrapperLabel wlaWaehrungPreis = new WrapperLabel();
	private WrapperLabel wlaEinheitMenge = new WrapperLabel();

	public PanelInventurstand(InternalFrame internalFrame, String add2TitleI,
			Object pk) throws Throwable {
		super(internalFrame, add2TitleI, pk);
		jbInit();
		setDefaults();
		initComponents();
		enableAllComponents(this, false);
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wnfInventurmenge;
	}

	protected void setDefaults() throws Throwable {
		wlaWaehrungPreis.setText(LPMain.getInstance().getTheClient()
				.getSMandantenwaehrung());

	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {

		super.eventYouAreSelected(false);
		Object key = getKeyWhenDetailPanel();

		if (key == null || (key.equals(LPMain.getLockMeForNew()))) {

			leereAlleFelder(this);
			clearStatusbar();

		} else {
			inventurstandDto = DelegateFactory.getInstance()
					.getInventurDelegate().inventurstandFindByPrimaryKey(
							(Integer) key);
			dto2Components();
		}
	}

	public void eventActionNew(EventObject eventObject, boolean bLockMeI,
			boolean bNeedNoNewI) throws Throwable {
	}

	protected void dto2Components() throws Throwable {
		ArtikelDto aDto = DelegateFactory.getInstance().getArtikelDelegate()
				.artikelFindByPrimaryKey(inventurstandDto.getArtikelIId());
		wlaArtikel.setText(aDto.formatArtikelbezeichnung());

		LagerDto lDto = DelegateFactory.getInstance().getLagerDelegate()
				.lagerFindByPrimaryKey(inventurstandDto.getLagerIId());

		wlaLager.setText(lDto.getCNr());

		wlaEinheitMenge.setText(aDto.getEinheitCNr().trim());

		wnfInventurmenge.setBigDecimal(inventurstandDto.getNInventurmenge());
		wnfInventurpreis.setBigDecimal(inventurstandDto.getNInventurpreis());
	}

	protected void components2Dto() throws ExceptionLP {
		inventurstandDto.setNInventurmenge(wnfInventurmenge.getBigDecimal());
		inventurstandDto.setNInventurpreis(wnfInventurpreis.getBigDecimal());
	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;
		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
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

		wnfInventurmenge.setMandatoryField(true);
		wnfInventurmenge.setMinimumValue(0);
		wnfInventurpreis.setMandatoryField(true);
		wnfInventurpreis.setMinimumValue(0);

		int iNachkommastellen = Defaults.getInstance()
				.getIUINachkommastellenPreiseAllgemein();
		wnfInventurpreis.setFractionDigits(iNachkommastellen);

		int iNachkommastellenMenge = Defaults.getInstance()
		.getIUINachkommastellenMenge();
		wnfInventurmenge.setFractionDigits(iNachkommastellenMenge);
		
		
		wlaArtikelLabel.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.artikel")
				+ ":");
		wlaLagerLabel.setText(LPMain.getInstance().getTextRespectUISPr(
				"label.lager")
				+ ":");

		wlaArtikel.setHorizontalAlignment(SwingConstants.LEFT);
		wlaLager.setHorizontalAlignment(SwingConstants.LEFT);

		wlaEinheitMenge.setHorizontalAlignment(SwingConstants.LEFT);
		wlaWaehrungPreis.setHorizontalAlignment(SwingConstants.LEFT);

		wlaInventurmenge.setText(LPMain.getInstance().getTextRespectUISPr(
				"artikel.inventurmenge"));
		wlaInventurpreis.setText(LPMain.getInstance().getTextRespectUISPr(
				"artikel.inventurpreis"));

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
		jpaWorkingOn.add(wlaArtikelLabel, new GridBagConstraints(0, 0, 1, 1,
				0.05, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaArtikel, new GridBagConstraints(1, 0, 1, 1, 0.10,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaLagerLabel, new GridBagConstraints(0, 1, 1, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaLager, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wlaInventurmenge, new GridBagConstraints(0, 2, 1, 1,
				0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfInventurmenge, new GridBagConstraints(1, 2, 1, 1,
				0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaEinheitMenge, new GridBagConstraints(2, 2, 1, 1, 0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 15, 0));

		jpaWorkingOn.add(wlaInventurpreis, new GridBagConstraints(0, 3, 1, 1,
				0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfInventurpreis, new GridBagConstraints(1, 3, 1, 1,
				0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaWaehrungPreis, new GridBagConstraints(2, 3, 1, 1,
				0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		String[] aWhichButtonIUse = { ACTION_UPDATE, ACTION_SAVE,
				ACTION_DISCARD, };

		enableToolsPanelButtons(aWhichButtonIUse);

	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_INVENTUR;
	}

	protected void eventActionDelete(ActionEvent e,
			boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable, Throwable {
	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {
		if (allMandatoryFieldsSetDlg()) {
			components2Dto();
			DelegateFactory.getInstance().getInventurDelegate()
					.updateInventurstand(inventurstandDto);
			super.eventActionSave(e, true);
			if (getInternalFrame().getKeyWasForLockMe() == null) {
				getInternalFrame().setKeyWasForLockMe(
						inventurstandDto.getIId() + "");
			}
			eventYouAreSelected(false);
		}

	}

}
