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
package com.lp.client.frame.component;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.HashMap;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.HelperClient;
import com.lp.client.pc.LPButtonAction;

public class ToolBar {

	private PanelBasis panelBasis = null;
	private JPanel panelButtonAction = null;
	private JPanel panelButtonActionLeft = null;
	private JPanel panelButtonActionCenter = null;
	private JPanel panelButtonActionRight = null;

	private HashMap<String, LPButtonAction> hmOfButtons = new HashMap<String, LPButtonAction>();

	/**
	 * Erzeugt eine neue Toolbar. Sie k&uuml;mmert sich um die Buttons in den Panels
	 * wie zB.: ACTION_NEW, ACTION_UPDATE, usw.
	 * 
	 * @param panelBasis
	 *            Das Panel in dem die Toolbar sich befindet
	 */
	public ToolBar(PanelBasis panelBasis) {
		this.panelBasis = panelBasis;		
	}

	public JPanel getToolsPanel() {
		if (panelButtonAction == null) {
			panelButtonAction = new JPanel();
			panelButtonAction.setLayout(new GridBagLayout());

			panelButtonAction.add(getToolsPanelLeft(), new GridBagConstraints(
					0, 0, 1, 1, 1.0, 0, GridBagConstraints.WEST,
					GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			panelButtonAction.add(getToolsPanelCenter(),
					new GridBagConstraints(1, 0, 1, 1, 1.0, 0,
							GridBagConstraints.CENTER, GridBagConstraints.NONE,
							new Insets(0, 0, 0, 0), 0, 0));
			panelButtonAction.add(getToolsPanelRight(), new GridBagConstraints(
					2, 0, 1, 1, 1.0, 0, GridBagConstraints.EAST,
					GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		}
		return panelButtonAction;
	}

	public JPanel getToolsPanelLeft() {
		if (panelButtonActionLeft == null) {
			panelButtonActionLeft = new JPanel();
			FlowLayout flowLayout = new FlowLayout(FlowLayout.LEFT);
			panelButtonActionLeft.setLayout(flowLayout);
			
			// Hack fuer QF-Test damit wir kompatibel zum aufgenommen QF-Test bleiben
			panelButtonActionLeft.setName("panelButtonAction") ;
		}
		return panelButtonActionLeft;
	}

	public JPanel getToolsPanelCenter() {
		if (panelButtonActionCenter == null) {
			panelButtonActionCenter = new JPanel();
			FlowLayout flowLayout = new FlowLayout(FlowLayout.CENTER);
			panelButtonActionCenter.setLayout(flowLayout);
		}

		return panelButtonActionCenter;
	}

	public JPanel getToolsPanelRight() {
		if (panelButtonActionRight == null) {
			panelButtonActionRight = new JPanel();
			FlowLayout flowLayout = new FlowLayout(FlowLayout.RIGHT);
			panelButtonActionRight.setLayout(flowLayout);
		}

		return panelButtonActionRight;
	}

	public HashMap<String, LPButtonAction> getHmOfButtons() {
		return hmOfButtons;
	}
	
	
	/**
	 * Gibt den Button mit dem entsprechenden actionCommand zur&uuml;ck
	 * @param actionCommand ActionCommand des Buttons
	 */
	public JButton getButton(String actionCommand) {
		return hmOfButtons.get(actionCommand) != null ? hmOfButtons.get(actionCommand).getButton() : null;
	}

	/**
	 * Mache einen Button und f&uuml;ge ihn links im ToolsPanel hinzu.
	 * 
	 * @param rechtCNrI
	 *            String: erforderliches Benutzerrecht. (null = kein
	 *            zusaetzliches Recht erforderlich)
	 * @throws Exception
	 */
	public void addButtonLeft(String iconPath, String tooltip, String ac,
			KeyStroke accelKey, String rechtCNrI) {
		getToolsPanelLeft()
				.add(createAndSaveButton(iconPath, tooltip, ac, accelKey,
						rechtCNrI));
	}

	/**
	 * Mache einen Button und f&uuml;ge ihn mittig im ToolsPanel hinzu.
	 * 
	 * @param rechtCNrI
	 *            String: erforderliches Benutzerrecht. (null = kein
	 *            zusaetzliches Recht erforderlich)
	 * @throws Exception
	 */
	public void addButtonCenter(String iconPath, String tooltip, String ac,
			KeyStroke accelKey, String rechtCNrI) {
		getToolsPanelCenter()
				.add(createAndSaveButton(iconPath, tooltip, ac, accelKey,
						rechtCNrI));
	}

	/**
	 * Mache einen Button und f&uuml;ge ihn rechts im ToolsPanel hinzu.
	 * 
	 * @param rechtCNrI
	 *            String: erforderliches Benutzerrecht. (null = kein
	 *            zusaetzliches Recht erforderlich)
	 * @throws Exception
	 */
	public void addButtonRight(String iconPath, String tooltip, String ac,
			KeyStroke accelKey, String rechtCNrI) {
		getToolsPanelRight()
				.add(createAndSaveButton(iconPath, tooltip, ac, accelKey,
						rechtCNrI));
	}

	public void resetToolsPanel() {
		getToolsPanelLeft().removeAll();
		String[] aButton = { PanelBasis.ACTION_REFRESH, // muss-button.
		};
		enableToolsPanelButtons(aButton);
	}

	/**
	 * Aktiviere aWhichButtons Buttons.
	 * 
	 * @param aWhichButtons
	 *            String[]; zB. ACTION_SAVE, ACTION_LOCK
	 * @throws Throwable
	 */
	public void enableButtonAction(String[] aWhichButtons) {

		getToolsPanelLeft().removeAll();

		for (String buttonCmd : aWhichButtons) {
			LPButtonAction item = getHmOfButtons().get(buttonCmd);
			item.getButton().setMaximumSize(
					HelperClient.getToolsPanelButtonDimension());
			item.getButton().setMinimumSize(
					HelperClient.getToolsPanelButtonDimension());
			item.getButton().setPreferredSize(
					HelperClient.getToolsPanelButtonDimension());

			getToolsPanelLeft().add(item.getButton());
		}
	}

	/**
	 * Aktiviere aWhichButtons Buttons.
	 * 
	 * @param aWhichButtons
	 *            String[]; zB. ACTION_SAVE, ACTION_LOCK
	 * @throws ExceptionForLPClients
	 * @throws Exception
	 */
	public void enableToolsPanelButtons(String[] aWhichButtons) {

		for (String buttonCmd : aWhichButtons) {
			LPButtonAction item = (LPButtonAction) getHmOfButtons().get(
					buttonCmd);
			if (item != null) {
				item.getButton().setMaximumSize(
						HelperClient.getToolsPanelButtonDimension());
				item.getButton().setMinimumSize(
						HelperClient.getToolsPanelButtonDimension());
				item.getButton().setPreferredSize(
						HelperClient.getToolsPanelButtonDimension());
				getToolsPanelLeft().add(item.getButton());
			}
		}
		enableToolsPanelButtons(true, PanelBasis.ACTION_DELETE,
				PanelBasis.ACTION_DISCARD);
	}

	/**
	 * Enable oder disable Buttons, auch solche, die nicht LEAVEALONE sind!
	 * 
	 * @param enable
	 * @param which
	 *            die ActionCommands der Buttons
	 */
	public void enableToolsPanelButtons(boolean enable, String... which) {
		if (which == null)
			return;
		for (String button : which) {
			LPButtonAction item = getHmOfButtons().get(button);
			if (item != null)
				item.getButton().setEnabled(enable);
		}
	}

	/**
	 * Enable oder disable der LeaveAlone-Buttons.
	 * 
	 * @param aButtonStringI
	 *            die Bezeichnungen der Buttons
	 * @param bEnableI
	 *            boolean enable oder disble
	 * @throws Exception
	 */
	public void enableToolsPanelLeaveAloneButtons(String[] aButtonStringI,
			boolean bEnableI) throws Exception {
		for (int i = 0; i < aButtonStringI.length; i++) {
			LPButtonAction item = null;

			try {
				item = getHmOfButtons().get(aButtonStringI[i]);
				if (item.getButton().getActionCommand()
						.indexOf(PanelBasis.LEAVEALONE) != -1) {
					item.getButton().setEnabled(bEnableI);
				}
			} catch (Throwable t) {
				throw new Exception("Button " + aButtonStringI[i]
						+ " kann nicht gesetzt werden.");
			}
		}
	}

	/**
	 * Einen Button aufgrund seines ActionCommand vom Panel entfernen. <br>
	 * Der Button muss existieren!
	 * 
	 * @param ac
	 *            ActionCommand UW->JO
	 * @throws Exception
	 */
	public void removeButton(String ac) {
		if (getHmOfButtons().get(ac) != null) {
			JButton jButton = getHmOfButtons().get(ac).getButton();
			if (jButton.getParent() != null)
				jButton.getParent().remove(jButton);
		}
	}

	/**
	 * Mache einen Button und merke ihn dir.
	 * 
	 * @param sIconPathI
	 *            String
	 * @param sTooltipI
	 *            String
	 * @param sActionCommandI
	 *            String
	 * @param accelKey
	 *            char
	 * @param rechtCNrI
	 *            String: erforderliches Benutzerrecht. (null = kein
	 *            zusaetzliches Recht erforderlich)
	 * @return JButton
	 */
	public JButton createAndSaveButton(String sIconPathI, String sTooltipI,
			String sActionCommandI, KeyStroke accelKey, String rechtCNrI) {
		JButton button = createButtonActionListenerThis(sIconPathI, sTooltipI,
				sActionCommandI);
		button.setMinimumSize(HelperClient.getToolsPanelButtonDimension());
		button.setPreferredSize(HelperClient.getToolsPanelButtonDimension());

		// accel:

		if (accelKey != null) {
			button.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(accelKey,
					sActionCommandI);
			button.getActionMap().put(
					sActionCommandI,
					new ButtonAbstractAction(panelBasis, sActionCommandI));
		}
		getHmOfButtons().put(sActionCommandI,
				new LPButtonAction(button, rechtCNrI));
		// Component Name fuer den UI-Test ist der ActionCommand
		if (Defaults.getInstance().isComponentNamingEnabled()
				|| Defaults.getInstance().isOldComponentNamingEnabled()) {
			button.setName(sActionCommandI);
		}
		return button;
	}

	public JButton createButtonActionListenerThis(String iconPath,
			String tooltip, String ac) {
		ImageIcon ii = new ImageIcon(getClass().getResource(iconPath));

		JButton button = HelperClient.createButton(ii, tooltip, ac);

		button.addActionListener(panelBasis);
		return button;
	}

	public WrapperButton createWrapperButtonActionListenerThis(String iconPath,
			String tooltip, String ac) {
		ImageIcon ii = new ImageIcon(getClass().getResource(iconPath));

		WrapperButton button = HelperClient
				.createWrapperButton(ii, tooltip, ac);

		button.addActionListener(panelBasis);
		return button;
	}
}
