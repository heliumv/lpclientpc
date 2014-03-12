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
package com.lp.client.system;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.EventObject;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.Border;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.WrapperEditorField;
import com.lp.client.frame.component.WrapperEditorFieldKommentar;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.benutzer.service.NachrichtarchivDto;

public class PanelNachrichtarchiv extends PanelBasis {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private GridBagLayout gridBagLayoutAll = null;
	private JPanel jPanelWorkingOn = new JPanel();
	private JPanel panelButtonAction = null;
	private Border border = null;

	private String EXTRA_NEU_UEBERNEHMEN = "extra_neu_uebernehmen";
	private String MY_OWN_NEW_UEBERNEHMEN = PanelBasis.ACTION_MY_OWN_NEW
			+ EXTRA_NEU_UEBERNEHMEN;

	private String EXTRA_NEU_ERLEDIGEN = "extra_neu_erledigen";
	private String MY_OWN_NEW_ERLEDIGEN = PanelBasis.ACTION_MY_OWN_NEW
			+ EXTRA_NEU_ERLEDIGEN;

	private GridBagLayout gridBagLayoutWorkingPanel = null;

	private WrapperEditorField wefKommentar = new WrapperEditorFieldKommentar(
			getInternalFrame(), LPMain.getTextRespectUISPr("lp.bemerkung"));

	NachrichtarchivDto nachrichtarchivDto = null;

	public PanelNachrichtarchiv(InternalFrame internalFrame, String add2TitleI)
			throws Throwable {
		super(internalFrame, add2TitleI);

		jbInit();
		setDefaults();
		initComponents();
		enableAllComponents(this, false);
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return NO_VALUE_THATS_OK_JCOMPONENT;
	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_NACHRICHTARCHIV;
	}

	protected void dto2Components() throws ExceptionLP {

	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;
		if (e.getID() == ItemChangedEvent.ITEM_CHANGED) {

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

		this.add(panelButtonAction, new GridBagConstraints(0, 0, 1, 1, 0.0,
				0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(0, 0, 0, 0), 0, 0));
		this.add(jPanelWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
				GridBagConstraints.NORTHEAST, GridBagConstraints.BOTH,
				new Insets(-9, 0, 9, 0), 0, 0));
		this.add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 1.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
		jPanelWorkingOn.add(wefKommentar, new GridBagConstraints(0, 0, 1, 1,
				0.3, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(10, 2, 2, 2), 0, 0));
		String[] aWhichButtonIUse = {};

		enableToolsPanelButtons(aWhichButtonIUse);

		createAndSaveAndShowButton("/com/lp/client/res/import1.png", LPMain
				.getTextRespectUISPr("jms.frage.uebernehmen"),
				MY_OWN_NEW_UEBERNEHMEN, null);

		createAndSaveAndShowButton("/com/lp/client/res/check2.png", LPMain
				.getTextRespectUISPr("jms.frage.erledigen"),
				MY_OWN_NEW_ERLEDIGEN, null);

	}

	protected void eventActionLock(ActionEvent e) throws Throwable {

	}

	protected void eventActionPrint(ActionEvent e) throws Throwable {

	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {

		if (nachrichtarchivDto != null) {
			if (e.getActionCommand().equals(MY_OWN_NEW_ERLEDIGEN)) {

				String response = JOptionPane
						.showInputDialog(
								null,
								LPMain
										.getTextRespectUISPr("system.nachrichtarchiv.erledigungsgrund"),
								LPMain
										.getTextRespectUISPr("system.nachrichtarchiv.erledigungsgrund"),
								JOptionPane.QUESTION_MESSAGE);

				if (response != null && response.length() > 0) {

					if (response.length() > 80) {
						response = response.substring(0, 79);
					}

					DelegateFactory.getInstance().getBenutzerDelegate()
							.erledigeNachricht(nachrichtarchivDto.getIId(),
									response);
				} else {

					DialogFactory
							.showModalDialog(
									LPMain.getTextRespectUISPr("lp.error"),
									LPMain
											.getTextRespectUISPr("system.nachrichtarchiv.erledigungsgrund.pflichtfeld"));

					return;
				}

				((InternalFrameSystem) getInternalFrame())
						.getTabbedPaneNachrichtarchiv()
						.getPanelNachrichtenarchiv().eventYouAreSelected(false);

			} else if (e.getActionCommand().equals(MY_OWN_NEW_UEBERNEHMEN)) {
				DelegateFactory.getInstance().getBenutzerDelegate()
						.weiseNachrichtPersonZu(nachrichtarchivDto.getIId());
				((InternalFrameSystem) getInternalFrame())
						.getTabbedPaneNachrichtarchiv()
						.getPanelNachrichtenarchiv().eventYouAreSelected(false);
			}
			
			LPMain.getInstance().getDesktop().aktualisiereAnzahlJMSMessages();
			
		}

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

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {

		super.eventYouAreSelected(false);
		Object key = getKeyWhenDetailPanel();

		if (key == null || (key.equals(LPMain.getLockMeForNew()))) {

			leereAlleFelder(this);
			clearStatusbar();
			nachrichtarchivDto = null;
		} else {
			nachrichtarchivDto = DelegateFactory.getInstance()
					.getBenutzerDelegate().nachrichtarchivFindByPrimaryKey(
							(Integer) key);

			wefKommentar.setText(nachrichtarchivDto.getCNachricht());

		}
	}

}
