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
package com.lp.client.projekt;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.EventObject;

import javax.swing.BorderFactory;
import javax.swing.JColorChooser;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;

import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperComboBox;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.projekt.service.HistoryartDto;

@SuppressWarnings("static-access")
public class PanelHistoryart extends PanelBasis {

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
	private HistoryartDto historyartDto = null;
	private WrapperLabel wlaBezeichnung = new WrapperLabel();
	private WrapperTextField wtfBezeichnung = new WrapperTextField();
	private WrapperCheckBox wcbAktualisiereZieltermin = new WrapperCheckBox();

	private WrapperButton wbuColor = new WrapperButton();
	private JTextField wtfColor = new JTextField();

	static final public String ACTION_SPECIAL_COLOR = "ACTION_SPECIAL_COLOR";

	public PanelHistoryart(InternalFrame internalFrame, String add2TitleI,
			Object pk) throws Throwable {
		super(internalFrame, add2TitleI, pk);
		jbInit();
		setDefaults();
		initComponents();
		enableAllComponents(this, false);
	}

	protected void setDefaults() {
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wtfBezeichnung;
	}

	public void eventActionNew(EventObject eventObject, boolean bLockMeI,
			boolean bNeedNoNewI) throws Throwable {
		super.eventActionNew(eventObject, true, false);

		historyartDto = new HistoryartDto();
		historyartDto.setIRot(255);
		historyartDto.setIBlau(255);
		historyartDto.setIGruen(255);

		leereAlleFelder(this);
		wtfColor.setBackground(new JTextField().getBackground());

	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		if (e.getActionCommand().equals(ACTION_SPECIAL_COLOR)) {
			Color cNewColor = JColorChooser.showDialog(
					this,
					LPMain.getInstance().getTextRespectUISPr(
							"proj.historyart.farbe"), wtfColor.getBackground());
			if (cNewColor != null) {
				wtfColor.setBackground(cNewColor);
			}

			historyartDto.setIRot(wtfColor.getBackground().getRed());
			historyartDto.setIBlau(wtfColor.getBackground().getBlue());
			historyartDto.setIGruen(wtfColor.getBackground().getGreen());

		}
	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;
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

		wtfColor.setEnabled(false);

		wbuColor.setText(LPMain.getInstance().getTextRespectUISPr(
				"proj.historyart.farbe"));
		wbuColor.setActionCommand(ACTION_SPECIAL_COLOR);
		wbuColor.addActionListener(this);

		wcbAktualisiereZieltermin.setText(LPMain.getInstance()
				.getTextRespectUISPr("proj.aktualisierezieltermin"));

		wlaBezeichnung.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.bezeichnung"));
		wtfBezeichnung.setMandatoryField(true);
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

		jpaWorkingOn.add(wlaBezeichnung, new GridBagConstraints(0, 0, 1, 1,
				0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfBezeichnung, new GridBagConstraints(1, 0, 1, 1,
				0.2, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wbuColor, new GridBagConstraints(0, 1, 1, 1, 0.1, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfColor, new GridBagConstraints(1, 1, 1, 1, 0.2, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		
		jpaWorkingOn.add(wcbAktualisiereZieltermin, new GridBagConstraints(1, 2, 1, 1, 0.2, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		
		String[] aWhichButtonIUse = { ACTION_UPDATE, ACTION_SAVE,
				ACTION_DELETE, ACTION_DISCARD, };

		enableToolsPanelButtons(aWhichButtonIUse);

	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_HISTORYART;
	}

	protected void eventActionDelete(ActionEvent e,
			boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {
		DelegateFactory.getInstance().getProjektServiceDelegate()
				.removeHistoryart(historyartDto);
		this.setKeyWhenDetailPanel(null);
		super.eventActionDelete(e, false, false);
	}

	protected void components2Dto() {
		historyartDto.setCBez(wtfBezeichnung.getText());
		historyartDto.setBAktualisierezieltermin(wcbAktualisiereZieltermin
				.getShort());

	}

	protected void dto2Components() throws Throwable {
		wtfBezeichnung.setText(historyartDto.getCBez());

		Color c = new Color(historyartDto.getIRot(), historyartDto.getIGruen(),
				historyartDto.getIBlau());
		wtfColor.setBackground(c);
		wcbAktualisiereZieltermin.setShort(historyartDto
				.getBAktualisierezieltermin());

	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {
		if (allMandatoryFieldsSetDlg()) {
			components2Dto();
			if (historyartDto.getIId() == null) {
				historyartDto.setIId(DelegateFactory.getInstance()
						.getProjektServiceDelegate()
						.createHistoryart(historyartDto));
				setKeyWhenDetailPanel(historyartDto.getIId());
			} else {
				DelegateFactory.getInstance().getProjektServiceDelegate()
						.updateHistoryart(historyartDto);
			}
			super.eventActionSave(e, true);
			if (getInternalFrame().getKeyWasForLockMe() == null) {
				getInternalFrame().setKeyWasForLockMe(
						historyartDto.getIId() + "");
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
			historyartDto = DelegateFactory.getInstance()
					.getProjektServiceDelegate()
					.historyartFindByPrimaryKey((Integer) key);
			dto2Components();
		}
	}
}
