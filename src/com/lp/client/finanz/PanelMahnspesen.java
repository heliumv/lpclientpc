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
package com.lp.client.finanz;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.math.BigDecimal;
import java.util.EventObject;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperSelectField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.finanz.service.MahnspesenDto;

@SuppressWarnings("static-access")
/**
 * <p> Diese Klasse kuemmert sich um die Wechselkurse</p>
 *
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 *
 * <p>Erstellung: Martin Bluehweis; 22.06.05</p>
 *
 * <p>@author $Author: christian $</p>
 *
 * @version not attributable Date $Date: 2012/08/06 07:42:38 $
 */
public class PanelMahnspesen extends PanelBasis {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private GridBagLayout gridBagLayoutAll = null;
	private JPanel jPanelWorkingOn = new JPanel();
	private JPanel panelButtonAction = null;
	private Border border = null;
	private GridBagLayout gridBagLayoutWorkingPanel = null;
	private WrapperLabel wlaMahnspesen = new WrapperLabel();
	private WrapperLabel wlaWaehrung = new WrapperLabel();
	private WrapperNumberField wnfMahnspesen = new WrapperNumberField();
	private WrapperSelectField wsfMahnstufe = null;
	TabbedPaneWaehrung tpWaehrung = null;

	private MahnspesenDto mahnspesenDto = null;

	public PanelMahnspesen(InternalFrame internalFrame, String add2TitleI,
			Object pk, TabbedPaneWaehrung tpWaehrung) throws Throwable {
		super(internalFrame, add2TitleI, pk);
		this.tpWaehrung = tpWaehrung;
		jbInit();
		setDefaults();
		initComponents();
	}

	private void setDefaults() throws Throwable {

	}

	public void eventActionNew(EventObject eventObject, boolean bLockMeI,
			boolean bNeedNoNewI) throws Throwable {
		super.eventActionNew(eventObject, true, false);
		mahnspesenDto = new MahnspesenDto();
		leereAlleFelder(this);
		setDefaults();
		clearStatusbar();

	}

	protected void dto2Components() throws Throwable {
		if (mahnspesenDto != null) {

			wnfMahnspesen.setBigDecimal(mahnspesenDto.getNMahnspesen());
			wsfMahnstufe.setKey(mahnspesenDto.getIMahnstufe());

		}
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {
		super.eventYouAreSelected(false);
		Object key = getKeyWhenDetailPanel();

		wlaWaehrung.setText(tpWaehrung.getWaehrungDto().getCNr());

		if (key == null
				|| (key != null && key.equals(LPMain.getLockMeForNew()))) {
			leereAlleFelder(this);
			clearStatusbar();

		} else {
			mahnspesenDto = DelegateFactory.getInstance().getFinanzServiceDelegate()
			.mahnspesenFindByPrimaryKey((Integer) key);
			dto2Components();
		}

	}

	protected void components2Dto() throws Throwable {
		mahnspesenDto.setMandantCNr(LPMain.getTheClient().getMandant());
		mahnspesenDto.setWaehrungCNr(tpWaehrung.getWaehrungDto().getCNr());
		mahnspesenDto.setNMahnspesen(wnfMahnspesen.getBigDecimal());
		mahnspesenDto.setIMahnstufe(wsfMahnstufe.getIKey());

	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {
		if (allMandatoryFieldsSetDlg()) {

			components2Dto();

			if (mahnspesenDto.getIId() == null) {
				mahnspesenDto.setIId(DelegateFactory.getInstance()
						.getFinanzServiceDelegate()
						.createMahnspesen(mahnspesenDto));
				setKeyWhenDetailPanel(mahnspesenDto.getIId());
			} else {
				DelegateFactory.getInstance().getFinanzServiceDelegate()
						.updateMahnspesen(mahnspesenDto);
			}
			super.eventActionSave(e, true);
			if (getInternalFrame().getKeyWasForLockMe() == null) {
				getInternalFrame().setKeyWasForLockMe(mahnspesenDto.getIId()+"");
			}
			
			eventYouAreSelected(false);

		}
	}

	protected void eventActionDelete(ActionEvent e,
			boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {
		DelegateFactory.getInstance().getFinanzServiceDelegate()
				.removeMahnspesen(mahnspesenDto);
		super.eventActionDelete(e, false, false);
	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
	}

	private void jbInit() throws Throwable {
		// von hier ...
		border = BorderFactory.createEmptyBorder(10, 10, 10, 10);
		setBorder(border);
		// das Aussenpanel hat immer das Gridbaglayout.
		gridBagLayoutAll = new GridBagLayout();
		this.setLayout(gridBagLayoutAll);

		// Actionpanel von Oberklasse holen und anhaengen.
		panelButtonAction = getToolsPanel();
		this.setActionMap(null);

		wsfMahnstufe = new WrapperSelectField(WrapperSelectField.MAHNSTUFE,
				getInternalFrame(), true);

		getInternalFrame().addItemChangedListener(this);

		wnfMahnspesen.setMandatoryFieldDB(true);

		wlaMahnspesen.setText(LPMain.getInstance().getTextRespectUISPr(
				"fb.mahnspesen"));

		wlaWaehrung.setHorizontalAlignment(SwingConstants.LEFT);

		this.add(panelButtonAction, new GridBagConstraints(0, 0, 1, 1, 0.0,
				0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(0, 0, 0, 0), 0, 0));
		jPanelWorkingOn = new JPanel();
		gridBagLayoutWorkingPanel = new GridBagLayout();
		jPanelWorkingOn.setLayout(gridBagLayoutWorkingPanel);
		this.add(jPanelWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
				GridBagConstraints.SOUTHEAST, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
		this.add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 1.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));

		jPanelWorkingOn.add(wsfMahnstufe.getWrapperButton(),
				new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
						100, 0));
		jPanelWorkingOn.add(wsfMahnstufe.getWrapperTextField(),
				new GridBagConstraints(1, iZeile, 2, 1, 0.0, 0.0,
						GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
						new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jPanelWorkingOn.add(wlaMahnspesen, new GridBagConstraints(0, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wnfMahnspesen, new GridBagConstraints(1, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wlaWaehrung, new GridBagConstraints(2, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 50, 0));

		String[] aWhichButtonIUse = { ACTION_UPDATE, ACTION_SAVE,
				ACTION_DELETE, ACTION_DISCARD, };

		enableToolsPanelButtons(aWhichButtonIUse);

	}

	protected String getLockMeWer() {
		return HelperClient.LOCKME_WAEHRUNG;
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wsfMahnstufe;
	}
}
