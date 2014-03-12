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
package com.lp.client.benutzer;

import java.util.EventObject;

import javax.swing.ImageIcon;
import javax.swing.JTabbedPane;

import com.lp.client.frame.component.InternalFrame;
import com.lp.client.pc.LPMain;
import com.lp.server.benutzer.service.SystemrolleDto;

@SuppressWarnings("static-access")
/*
 * <p>&UUml;berschrift: </p> <p>Beschreibung: </p> <p>Copyright: Copyright (c)
 * 2004</p> <p>Organisation: </p>
 * 
 * @author Christian Kollmann
 * 
 * @version $Revision: 1.4 $
 */
public class InternalFrameBenutzer extends InternalFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private TabbedPaneBenutzermandantsystemrolle tabbedPaneBenutzermandantsystemrolle = null;
	private TabbedPaneBenutzer tabbedPaneBenutzer = null;
	private TabbedPaneRollen tabbedPaneRollen = null;
	private TabbedPaneNachrichten tabbedPaneNachrichten = null;

	private final int IDX_TABBED_PANE_BENUTZER = 0;
	private final int IDX_TABBED_PANE_BENUTZERMANDANTSYSTEMROLLE = 1;
	private final int IDX_TABBED_PANE_SYSTEMROLLE = 2;
	private final int IDX_TABBED_PANE_NACHRICHTEN = 3;

	private SystemrolleDto systemrolleDto = null;

	public SystemrolleDto getSystemrolleDto() {
		return systemrolleDto;
	}

	public void setSystemrolleDto(SystemrolleDto systemrolleDto) {
		this.systemrolleDto = systemrolleDto;
	}

	public TabbedPaneBenutzermandantsystemrolle getTabbedPanePersonal() {
		return tabbedPaneBenutzermandantsystemrolle;
	}

	public InternalFrameBenutzer(String title, String belegartCNr,
			String sRechtModulweitI) throws Throwable {

		super(title, belegartCNr, sRechtModulweitI);
		jbInit();
		initComponents();
	}

	private void jbInit() throws Throwable {
		tabbedPaneRoot.insertTab(LPMain.getInstance().getTextRespectUISPr(
				"lp.benutzer"), null, null, LPMain.getInstance()
				.getTextRespectUISPr("lp.benutzer"), IDX_TABBED_PANE_BENUTZER);
		tabbedPaneRoot.insertTab(LPMain.getInstance().getTextRespectUISPr(
				"benutzer.title.tab.benutzermandant"), null, null, LPMain
				.getInstance().getTextRespectUISPr(
						"benutzer.title.tab.benutzermandant"),
				IDX_TABBED_PANE_BENUTZERMANDANTSYSTEMROLLE);
		tabbedPaneRoot.insertTab(LPMain.getInstance().getTextRespectUISPr(
				"lp.systemrolle"), null, null, LPMain.getInstance()
				.getTextRespectUISPr("lp.systemrolle"),
				IDX_TABBED_PANE_SYSTEMROLLE);
		tabbedPaneRoot.insertTab(LPMain.getInstance().getTextRespectUISPr(
				"ben.nachrichten"), null, null, LPMain.getInstance()
				.getTextRespectUISPr("ben.nachrichten"),
				IDX_TABBED_PANE_NACHRICHTEN);

		registerChangeListeners();
		createTabbedPaneBenutzer(null);
		tabbedPaneBenutzer.lPEventObjectChanged(null);
		tabbedPaneRoot.setSelectedComponent(tabbedPaneBenutzer);
		// iicon: hier das li/on icon gemacht
		ImageIcon iicon = new javax.swing.ImageIcon(getClass().getResource(
				"/com/lp/client/res/user1_monitor16x16.png"));
		setFrameIcon(iicon);

	}

	private void createTabbedPaneBenutzermandant(JTabbedPane tabbedPane)
			throws Throwable {
		if (tabbedPane == null) {
			// lazy loading
			tabbedPaneBenutzermandantsystemrolle = new TabbedPaneBenutzermandantsystemrolle(
					this);
			tabbedPaneRoot.setComponentAt(
					IDX_TABBED_PANE_BENUTZERMANDANTSYSTEMROLLE,
					tabbedPaneBenutzermandantsystemrolle);
			initComponents();
		}
	}

	private void createTabbedPaneNachrichten(JTabbedPane tabbedPane)
			throws Throwable {
		if (tabbedPane == null) {
			// lazy loading
			tabbedPaneNachrichten = new TabbedPaneNachrichten(this);
			tabbedPaneRoot.setComponentAt(IDX_TABBED_PANE_NACHRICHTEN,
					tabbedPaneNachrichten);
			initComponents();
		}
	}

	private void createTabbedPaneRollen(JTabbedPane tabbedPane)
			throws Throwable {
		if (tabbedPane == null) {
			// lazy loading
			tabbedPaneRollen = new TabbedPaneRollen(this);
			tabbedPaneRoot.setComponentAt(IDX_TABBED_PANE_SYSTEMROLLE,
					tabbedPaneRollen);
			initComponents();
		}
	}

	private void createTabbedPaneBenutzer(JTabbedPane tabbedPane)
			throws Throwable {
		if (tabbedPane == null) {
			// lazy loading
			tabbedPaneBenutzer = new TabbedPaneBenutzer(this);
			tabbedPaneRoot.setComponentAt(IDX_TABBED_PANE_BENUTZER,
					tabbedPaneBenutzer);
			initComponents();
		}
	}

	public void lPStateChanged(EventObject e) throws Throwable {
		JTabbedPane tabbedPane = (JTabbedPane) ((JTabbedPane) e.getSource())
				.getSelectedComponent();
		int selectedCur = ((JTabbedPane) e.getSource()).getSelectedIndex();

		if (selectedCur == IDX_TABBED_PANE_BENUTZERMANDANTSYSTEMROLLE) {
			createTabbedPaneBenutzermandant(tabbedPane);
			// Info an Tabbedpane, bist selektiert worden.
			tabbedPaneBenutzermandantsystemrolle.lPEventObjectChanged(null);
		} else if (selectedCur == IDX_TABBED_PANE_BENUTZER) {
			createTabbedPaneBenutzer(tabbedPane);
			// Info an Tabbedpane, bist selektiert worden.
			tabbedPaneBenutzer.lPEventObjectChanged(null);
		} else if (selectedCur == IDX_TABBED_PANE_SYSTEMROLLE) {
			createTabbedPaneRollen(tabbedPane);
			// Info an Tabbedpane, bist selektiert worden.
			tabbedPaneRollen.lPEventObjectChanged(null);
		} else if (selectedCur == IDX_TABBED_PANE_NACHRICHTEN) {
			createTabbedPaneNachrichten(tabbedPane);
			// Info an Tabbedpane, bist selektiert worden.
			tabbedPaneNachrichten.lPEventObjectChanged(null);
		}

	}

}
