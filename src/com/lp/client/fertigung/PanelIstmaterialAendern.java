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
package com.lp.client.fertigung;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.EventObject;
import java.util.Iterator;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.border.Border;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.FehlmengenAufloesen;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQuery;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPButtonAction;
import com.lp.client.pc.LPMain;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.LagerDto;
import com.lp.server.artikel.service.SeriennrChargennrMitMengeDto;
import com.lp.server.fertigung.service.LossollmaterialDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.util.Helper;

@SuppressWarnings("static-access")
public class PanelIstmaterialAendern extends PanelBasis {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private GridBagLayout gridBagLayoutAll = null;
	private JPanel jPanelWorkingOn = new JPanel();
	private JPanel panelButtonAction = null;
	private Border border = null;

	private GridBagLayout gridBagLayoutWorkingPanel = null;

	WrapperLabel wlaArtikel = new WrapperLabel(LPMain.getInstance()
			.getTextRespectUISPr("lp.artikel"));
	WrapperTextField wtfArtikel = new WrapperTextField();

	WrapperLabel wlaLager = new WrapperLabel(LPMain.getInstance()
			.getTextRespectUISPr("label.lager"));
	WrapperTextField wtfLager = new WrapperTextField();

	WrapperLabel wlaSnrChnr = new WrapperLabel(LPMain.getInstance()
			.getTextRespectUISPr("artikel.handlagerbewegung.seriennrchargennr"));
	WrapperTextField wtfSnrChnr = new WrapperTextField();

	WrapperLabel wlaMenge = new WrapperLabel("Menge");

	WrapperNumberField wnfMenge = new WrapperNumberField();

	Integer lossollmaterialIId = null;
	PanelQuery panelQuery = null;

	com.lp.server.fertigung.service.LosistmaterialDto losistmaterialDto = null;
	com.lp.server.fertigung.service.LosDto losDto = null;

	public PanelIstmaterialAendern(InternalFrame internalFrame,
			Integer lossollmaterialIId, String add2TitleI, PanelQuery panelQuery)
			throws Throwable {
		super(internalFrame, add2TitleI);
		this.lossollmaterialIId = lossollmaterialIId;
		this.panelQuery = panelQuery;
		jbInit();
		setDefaults();
		initComponents();
		enableAllComponents(this, false);
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return NO_VALUE_THATS_OK_JCOMPONENT;
	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_ARTIKELFEHLMENGE;
	}

	protected void dto2Components() throws Throwable {
		wnfMenge.setBigDecimal(losistmaterialDto.getNMenge());

		LossollmaterialDto lossollmaterialDto = DelegateFactory.getInstance()
				.getFertigungDelegate()
				.lossollmaterialFindByPrimaryKey(lossollmaterialIId);

		ArtikelDto artikelDto = DelegateFactory.getInstance()
				.getArtikelDelegate()
				.artikelFindByPrimaryKey(lossollmaterialDto.getArtikelIId());

		wtfArtikel.setText(artikelDto.formatArtikelbezeichnung());

		LagerDto lagerDto = DelegateFactory.getInstance().getLagerDelegate()
				.lagerFindByPrimaryKey(losistmaterialDto.getLagerIId());

		wtfLager.setText(lagerDto.getCNr());

		List<SeriennrChargennrMitMengeDto> dtos = DelegateFactory
				.getInstance()
				.getLagerDelegate()
				.getAllSeriennrchargennrEinerBelegartposition(
						LocaleFac.BELEGART_LOS, losistmaterialDto.getIId());

		if (dtos != null && dtos.size() > 0) {
			wtfSnrChnr.setText(dtos.get(0).getCSeriennrChargennr());
		} else {
			wtfSnrChnr.setText(null);
		}

	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;
		if (e.getID() == ItemChangedEvent.ITEM_CHANGED) {
			if (e.getSource().equals(panelQuery)) {
				Integer key = (Integer) ((PanelQuery) e.getSource())
						.getSelectedId();
				if (key != null) {
					losistmaterialDto = DelegateFactory.getInstance()
							.getFertigungDelegate()
							.losistmaterialFindByPrimaryKey(key);

					dto2Components();

					enableAllComponents(this, false);
					setzeButton(PanelBasis.ACTION_SAVE, false, false);
					setzeButton(PanelBasis.ACTION_DISCARD, false, false);
					setzeButton(PanelBasis.ACTION_REFRESH, true, false);
					setzeButton(PanelBasis.ACTION_UPDATE, true, false);
					setzeButton(PanelBasis.ACTION_REFRESH, true, true);
					// setzeButton(PanelBasis.ACTION_PRINT, true, true);

				} else {
					setzeButton(PanelBasis.ACTION_SAVE, false, false);
					setzeButton(PanelBasis.ACTION_DISCARD, false, false);
					setzeButton(PanelBasis.ACTION_REFRESH, false, false);
					setzeButton(PanelBasis.ACTION_UPDATE, false, false);
					// setzeButton(PanelBasis.ACTION_PRINT, true, true);

				}

			}
		}
	}

	private void jbInit() throws Throwable {
		border = BorderFactory.createEmptyBorder(10, 10, 10, 10);
		setBorder(border);
		// das Aussenpanel hat immer das Gridbaglayout.
		gridBagLayoutAll = new GridBagLayout();
		this.setLayout(gridBagLayoutAll);
		getInternalFrame().addItemChangedListener(this);

		// Actionpanel von Oberklasse holen und anhaengen.
		panelButtonAction = getToolsPanel();
		this.setActionMap(null);
		jPanelWorkingOn = new JPanel();
		gridBagLayoutWorkingPanel = new GridBagLayout();
		jPanelWorkingOn.setLayout(gridBagLayoutWorkingPanel);

		wtfArtikel.setColumnsMax(com.lp.server.util.Facade.MAX_UNBESCHRAENKT);
		wnfMenge.setMandatoryField(true);
		wnfMenge.setFractionDigits(3);
		wtfArtikel.setActivatable(false);
		wtfSnrChnr.setActivatable(false);
		wtfSnrChnr.setColumnsMax(com.lp.server.util.Facade.MAX_UNBESCHRAENKT);

		wnfMenge.setMinimumValue(0);
		wtfLager.setActivatable(false);
		this.add(panelButtonAction, new GridBagConstraints(0, 0, 1, 1, 0.0,
				0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(0, 0, 0, 0), 0, 0));
		this.add(jPanelWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
				GridBagConstraints.NORTHEAST, GridBagConstraints.BOTH,
				new Insets(-9, 0, 9, 0), 0, 0));
		this.add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 1.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
		jPanelWorkingOn.add(wlaArtikel, new GridBagConstraints(0, 0, 1, 1, 0.3,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wtfArtikel, new GridBagConstraints(1, 0, 1, 1, 0.3,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wlaSnrChnr, new GridBagConstraints(0, 1, 1, 1, 0.3,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wtfSnrChnr, new GridBagConstraints(1, 1, 1, 1, 0.3,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wlaLager, new GridBagConstraints(0, 2, 1, 1, 0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wtfLager, new GridBagConstraints(1, 2, 1, 1, 0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wlaMenge, new GridBagConstraints(0, 3, 1, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wnfMenge, new GridBagConstraints(1, 3, 1, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));

		String[] aWhichButtonIUse = { ACTION_UPDATE, ACTION_SAVE,
				ACTION_DISCARD,
		// ACTION_PRINT,
		};

		enableToolsPanelButtons(aWhichButtonIUse);

	}

	protected void eventActionLock(ActionEvent e) throws Throwable {

	}

	protected void eventActionPrint(ActionEvent e) throws Throwable {

	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {

	}

	protected void setDefaults() throws Throwable {

	}

	protected void eventActionDelete(ActionEvent e,
			boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {
		super.eventActionDelete(e, false, false);
	}

	protected void eventActionRefresh(ActionEvent e, boolean bNeedNoRefreshI)
			throws Throwable {
		super.eventActionRefresh(e, bNeedNoRefreshI);

	}

	public void eventActionNew(EventObject eventObject, boolean bLockMeI,
			boolean bNeedNoNewI) throws Throwable {
		super.eventActionNew(eventObject, true, false);
		leereAlleFelder(this);
	}

	protected void components2Dto() throws ExceptionLP {

	}

	private void setzeButton(String button, boolean bEnabled,
			boolean bPanelQuery) {
		Collection<?> buttons = getHmOfButtons().values();
		if (bPanelQuery == true) {
			buttons = panelQuery.getHmOfButtons().values();
		}
		for (Iterator<?> iter = buttons.iterator(); iter.hasNext();) {
			LPButtonAction item = (LPButtonAction) iter.next();
			if (item.getButton().getActionCommand().equals(button)) {
				item.getButton().setEnabled(bEnabled);
			}

		}
	}

	protected void eventActionUnlock(ActionEvent e) throws Throwable {

	}

	protected void eventActionDiscard(ActionEvent e) throws Throwable {
		eventYouAreSelected(false);
		enableAllComponents(this, false);

		setzeButton(PanelBasis.ACTION_SAVE, false, false);
		setzeButton(PanelBasis.ACTION_DISCARD, false, false);
		setzeButton(PanelBasis.ACTION_REFRESH, true, false);
		setzeButton(PanelBasis.ACTION_UPDATE, true, false);
		setzeButton(PanelBasis.ACTION_REFRESH, true, true);
		// setzeButton(PanelBasis.ACTION_PRINT, true, true);

	}

	protected void eventActionUpdate(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {
		// super.eventActionUpdate(e, false);

		if (panelQuery.getSelectedId() != null) {
			enableAllComponents(this, true);
			setzeButton(PanelBasis.ACTION_SAVE, true, false);
			setzeButton(PanelBasis.ACTION_DISCARD, true, false);
			setzeButton(PanelBasis.ACTION_REFRESH, false, false);
			setzeButton(PanelBasis.ACTION_UPDATE, false, false);
			setzeButton(PanelBasis.ACTION_REFRESH, false, true);
			// setzeButton(PanelBasis.ACTION_PRINT, false, true);
		} else {
			enableAllComponents(this, false);
			setzeButton(PanelBasis.ACTION_SAVE, false, false);
			setzeButton(PanelBasis.ACTION_DISCARD, false, false);
			setzeButton(PanelBasis.ACTION_REFRESH, false, false);
			setzeButton(PanelBasis.ACTION_UPDATE, false, false);
			// setzeButton(PanelBasis.ACTION_PRINT, true, true);

		}
	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {
		if (allMandatoryFieldsSetDlg()) {

			if (losistmaterialDto != null) {

				BigDecimal diff = losistmaterialDto.getNMenge();

				DelegateFactory
						.getInstance()
						.getFertigungDelegate()
						.updateLosistmaterialMenge(losistmaterialDto.getIId(),
								wnfMenge.getBigDecimal());
				losistmaterialDto = DelegateFactory
						.getInstance()
						.getFertigungDelegate()
						.losistmaterialFindByPrimaryKey(
								losistmaterialDto.getIId());

				if (Helper.short2Boolean(losistmaterialDto.getBAbgang())) {
					diff = diff.subtract(losistmaterialDto.getNMenge());

					if (diff.doubleValue() > 0) {

						LossollmaterialDto lossollmaterialDto = DelegateFactory
								.getInstance()
								.getFertigungDelegate()
								.lossollmaterialFindByPrimaryKey(
										lossollmaterialIId);
						if (wtfSnrChnr.getText() == null) {
							FehlmengenAufloesen.fehlmengenAufloesen(
									getInternalFrame(),
									lossollmaterialDto.getArtikelIId(),
									losistmaterialDto.getLagerIId(),
									(String[]) null, diff);
						} else {
							FehlmengenAufloesen
									.fehlmengenAufloesen(
											getInternalFrame(),
											lossollmaterialDto.getArtikelIId(),
											losistmaterialDto.getLagerIId(),
											new String[] { wtfSnrChnr.getText() },
											diff);
						}

					}
				}

				dto2Components();

			}
			panelQuery.eventYouAreSelected(false);
			super.eventActionSave(e, true);
			eventYouAreSelected(false);

		}

		enableAllComponents(this, false);
		setzeButton(PanelBasis.ACTION_SAVE, false, false);
		setzeButton(PanelBasis.ACTION_DISCARD, false, false);
		setzeButton(PanelBasis.ACTION_REFRESH, true, false);
		setzeButton(PanelBasis.ACTION_UPDATE, true, false);
		setzeButton(PanelBasis.ACTION_REFRESH, true, true);
		setzeButton(PanelBasis.ACTION_PRINT, true, true);
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {

		super.eventYouAreSelected(false);
		Object key = panelQuery.getSelectedId();

		if (key == null || (key.equals(LPMain.getLockMeForNew()))) {

			leereAlleFelder(this);
			clearStatusbar();
		} else {
			losistmaterialDto = DelegateFactory.getInstance()
					.getFertigungDelegate()
					.losistmaterialFindByPrimaryKey((Integer) key);
			dto2Components();

		}
	}

}
