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
package com.lp.client.frame.dynamisch;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.EventObject;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.border.Border;

import com.lp.client.artikel.InternalFrameArtikel;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelDynamischHelper;
import com.lp.client.frame.component.PanelQuery;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.server.system.service.PanelbeschreibungDto;
import com.lp.server.system.service.PaneldatenDto;

public class PanelDynamisch extends PanelBasis {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	PaneldatenDto[] paneldatenDtos = null;
	private PanelQuery panelQueryAuswahl = null;

	private JPanel panelButtonAction = new JPanel();
	private GridBagLayout gridBagLayoutAll = new GridBagLayout();
	private String paneltyp = "";
	ButtonGroup buttonGroup1 = new ButtonGroup();
	Border border1 = BorderFactory.createEmptyBorder(2, 2, 2, 2);

	PanelDynamischHelper phd = null;

	private String lockme = null;

	private PanelbeschreibungDto[] dtos = null;

	private String[] aWhichButtonIUse = null;

	public PanelDynamisch(InternalFrame internalFrame, String add2TitleI,
			PanelQuery panelQueryAuswahl, String paneltyp, String lockme,
			String[] buttons) throws Throwable {
		super(internalFrame, add2TitleI, panelQueryAuswahl.getSelectedId());

		if (getInternalFrame() instanceof InternalFrameArtikel) {
			setKeyWhenDetailPanel(((InternalFrameArtikel) getInternalFrame())
					.getArtikelDto().getIId() + "");
		}

		this.panelQueryAuswahl = panelQueryAuswahl;
		this.paneltyp = paneltyp;
		this.lockme = lockme;
		this.aWhichButtonIUse = buttons;
		if (aWhichButtonIUse == null) {
			aWhichButtonIUse = new String[0];
		}

		jbInit();
		setDefaults();
		initComponents();
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return NO_VALUE_THATS_OK_JCOMPONENT;
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {

		super.eventYouAreSelected(false);
		try {

			String oKey = panelQueryAuswahl.getSelectedId() + "";

			if (getInternalFrame() instanceof InternalFrameArtikel) {
				oKey = ((InternalFrameArtikel) getInternalFrame())
						.getArtikelDto().getIId() + "";
			}

			paneldatenDtos = DelegateFactory.getInstance().getPanelDelegate()
					.paneldatenFindByPanelCNrCKey(paneltyp, oKey);

			dto2Components();
		} catch (Throwable ex) {
			paneldatenDtos = new PaneldatenDto[dtos.length];
			leereAlleFelder(this);
		}

		phd.enablePrintButton();
		phd.registerPrintButtonActionListener(this);

	}

	protected void dto2Components() throws ExceptionLP, Throwable {
		leereAlleFelder(this);
		phd.dto2Components(panelQueryAuswahl.getSelectedId() + "");

	}

	private void jbInit() throws Throwable {
		// von hier ...
		// das Aussenpanel hat immer das Gridbaglayout.
		gridBagLayoutAll = new GridBagLayout();
		this.setLayout(gridBagLayoutAll);

		// Actionpanel von Oberklasse holen und anhaengen.
		panelButtonAction = getToolsPanel();
		this.setActionMap(null);

		getInternalFrame().addItemChangedListener(this);

		this.add(panelButtonAction, new GridBagConstraints(0, 0, 1, 1, 0.0,
				0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(0, 0, 0, 0), 0, 0));

		// jetzt meine felder

		phd = new PanelDynamischHelper(paneltyp,
				null, getInternalFrame());

		this.add(phd.panelErzeugen(), new GridBagConstraints(0, 1, 1, 1, 1.0,
				1.0, GridBagConstraints.SOUTHEAST, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
		this.add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 1.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));

		enableToolsPanelButtons(aWhichButtonIUse);

	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {

		String oKey = panelQueryAuswahl.getSelectedId() + "";

		if (getInternalFrame() instanceof InternalFrameArtikel) {
			oKey = ((InternalFrameArtikel) getInternalFrame()).getArtikelDto()
					.getIId() + "";
		}

		getInternalFrame().showReportKriterien(
				new ReportPanelDynamisch(getInternalFrame(), "", paneltyp, e
						.getActionCommand(), oKey));
	}

	protected String getLockMeWer() throws Exception {
		return lockme;
	}

	protected void setDefaults() throws Throwable {
	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {
		if (allMandatoryFieldsSetDlg()) {

			DelegateFactory
					.getInstance()
					.getPanelDelegate()
					.createPaneldaten(
							phd.components2Dto(panelQueryAuswahl
									.getSelectedId() + ""));
			super.eventActionSave(e, true);
			eventYouAreSelected(false);

		}
	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;
		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
		}
	}

}
