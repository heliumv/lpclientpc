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
package com.lp.client.eingangsrechnung;

import java.util.EventObject;

import javax.swing.ImageIcon;
import javax.swing.JTabbedPane;

import com.lp.client.frame.Command;
import com.lp.client.frame.CommandGoto;
import com.lp.client.frame.ICommand;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.eingangsrechnung.service.EingangsrechnungDto;
import com.lp.server.eingangsrechnung.service.EingangsrechnungartDto;
import com.lp.server.eingangsrechnung.service.EingangsrechnungstatusDto;
import com.lp.server.system.service.MandantFac;

@SuppressWarnings("static-access")
/**
 *
 * <p>Diese Klasse kuemmert sich um das Modul Eingangsrechnung</p>
 *
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 *
 * <p>Erstellung: Martin Bluehweis; 13.2.2005</p>
 *
 * @author  Martin Bluehweis
 *
 * @version $Revision: 1.5 $
 */
public class InternalFrameEingangsrechnung extends InternalFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static String I_ID_EINGANGSRECHNUNG = "iIdEingangsrechnung";

	private TabbedPaneEingangsrechnung tabbedPaneEingangsrechnung = null;
	private TabbedPaneGrunddaten tabbedPaneGrunddaten = null;
	private TabbedPaneEingangsrechnung tabbedPaneEingangsrechnungZusatzkosten = null;
	private boolean bZusatzkosten = false;

	public static int IDX_TABBED_PANE_EINGANGSRECHNUNG = -1;
	public static int IDX_TABBED_PANE_ZUSATZKOSTEN = -1;
	public static int IDX_TABBED_PANE_GRUNDDATEN = -1;

	private EingangsrechnungartDto eingangsrechnungartDto = null;
	private EingangsrechnungstatusDto eingangsrechnungsstatusDto = null;

	public InternalFrameEingangsrechnung(String title, String belegartCNr,
			String sRechtModulweitI) throws Throwable {

		super(title, belegartCNr, sRechtModulweitI);
		jbInit();
		initComponents();
	}

	private void jbInit() throws Throwable {
		int tabIndex = 0;
		// 1 tab unten: ER
		tabbedPaneRoot.insertTab(
				LPMain.getInstance().getTextRespectUISPr(
						"er.tab.unten.eingangsrechnung.title"),
				null,
				null,
				LPMain.getInstance().getTextRespectUISPr(
						"er.tab.unten.eingangsrechnung.tooltip"), tabIndex);
		IDX_TABBED_PANE_EINGANGSRECHNUNG = tabIndex;
		tabIndex++;
		if (LPMain
				.getInstance()
				.getDesktop()
				.darfAnwenderAufZusatzfunktionZugreifen(
						MandantFac.ZUSATZFUNKTION_ER_ZUSATZKOSTEN)
				&& DelegateFactory.getInstance().getTheJudgeDelegate()
						.hatRecht(RechteFac.RECHT_FB_CHEFBUCHHALTER)) {
			bZusatzkosten = true;
			tabbedPaneRoot
					.insertTab(
							LPMain.getInstance().getTextRespectUISPr(
									"er.zusatzkosten"),
							null,
							null,
							LPMain.getInstance().getTextRespectUISPr(
									"er.zusatzkosten"), tabIndex);
			IDX_TABBED_PANE_ZUSATZKOSTEN = tabIndex;
			tabIndex++;
		}

		// 2 tab unten: Grunddaten
		// nur anzeigen wenn Benutzer Recht dazu hat
		if (DelegateFactory.getInstance().getTheJudgeDelegate()
				.hatRecht(RechteFac.RECHT_LP_DARF_GRUNDDATEN_SEHEN)) {
			tabbedPaneRoot.insertTab(
					LPMain.getInstance().getTextRespectUISPr(
							"pers.title.tab.grunddaten"),
					null,
					null,
					LPMain.getInstance().getTextRespectUISPr(
							"pers.title.tab.grunddaten"), tabIndex);
			IDX_TABBED_PANE_GRUNDDATEN = tabIndex;
			tabIndex++;
		}
		getTabbedPaneEingangsrechnung().lPEventObjectChanged(null);
		tabbedPaneRoot.setSelectedComponent(tabbedPaneEingangsrechnung);

		// ich selbst moechte informiert werden.
		addItemChangedListener(this);
		// awt: listener bin auch ich.
		registerChangeListeners();
		// das icon setzen.
		ImageIcon iicon = new javax.swing.ImageIcon(getClass().getResource(
				"/com/lp/client/res/hand_money16x16.png"));
		setFrameIcon(iicon);
	}

	public void lPStateChanged(EventObject e) throws Throwable {
		int selectedCur = ((JTabbedPane) e.getSource()).getSelectedIndex();

		if (selectedCur == IDX_TABBED_PANE_EINGANGSRECHNUNG) {
			getTabbedPaneEingangsrechnung().lPEventObjectChanged(null);
		}

		else if (selectedCur == IDX_TABBED_PANE_ZUSATZKOSTEN) {
			tabbedPaneRoot
					.setSelectedComponent(getTabbedPaneEingangsrechnungZusatzkosten());
			// Info an Tabbedpane, bist selektiert worden.
			tabbedPaneEingangsrechnungZusatzkosten.lPEventObjectChanged(null);
		} else if (selectedCur == IDX_TABBED_PANE_GRUNDDATEN) {
			tabbedPaneRoot.setSelectedComponent(getTabbedPaneGrunddaten());
			// Info an Tabbedpane, bist selektiert worden.
			tabbedPaneGrunddaten.lPEventObjectChanged(null);
			setLpTitle(InternalFrame.TITLE_IDX_AS_I_LIKE, "");
		}
	}

	public TabbedPaneEingangsrechnung getTabbedPaneEingangsrechnung()
			throws Throwable {
		if (tabbedPaneEingangsrechnung == null) {
			// lazy loading
			tabbedPaneEingangsrechnung = new TabbedPaneEingangsrechnung(this,
					false,LPMain
					.getTextRespectUISPr("er.tab.unten.eingangsrechnung.title"));
			tabbedPaneRoot.setComponentAt(IDX_TABBED_PANE_EINGANGSRECHNUNG,
					tabbedPaneEingangsrechnung);

			initComponents();
		}
		return tabbedPaneEingangsrechnung;
	}

	public TabbedPaneEingangsrechnung getTabbedPaneEingangsrechnungZusatzkosten()
			throws Throwable {
		if (tabbedPaneEingangsrechnungZusatzkosten == null) {
			// lazy loading
			tabbedPaneEingangsrechnungZusatzkosten = new TabbedPaneEingangsrechnung(
					this, true,LPMain
					.getTextRespectUISPr("er.zusatzkosten"));
			tabbedPaneRoot.setComponentAt(IDX_TABBED_PANE_ZUSATZKOSTEN,
					tabbedPaneEingangsrechnungZusatzkosten);

			initComponents();
		}
		return tabbedPaneEingangsrechnungZusatzkosten;
	}

	private TabbedPaneGrunddaten getTabbedPaneGrunddaten() throws Throwable {
		if (tabbedPaneGrunddaten == null) {
			// lazy loading
			tabbedPaneGrunddaten = new TabbedPaneGrunddaten(this);
			tabbedPaneRoot.setComponentAt(IDX_TABBED_PANE_GRUNDDATEN,
					tabbedPaneGrunddaten);
			initComponents();
		}
		return tabbedPaneGrunddaten;
	}

	protected void lPEventItemChanged(ItemChangedEvent eI) {
		// nothing here.
	}

	public void setEingangsrechnungartDto(
			EingangsrechnungartDto EingangsrechnungsartDto) {
		this.eingangsrechnungartDto = EingangsrechnungsartDto;
	}

	public void setEingangsrechnungsstatusDto(
			EingangsrechnungstatusDto eingangsrechnungsstatusDto) {
		this.eingangsrechnungsstatusDto = eingangsrechnungsstatusDto;
	}

	public EingangsrechnungartDto getEingangsrechnungartDto() {
		return eingangsrechnungartDto;
	}

	public EingangsrechnungstatusDto getEingangsrechnungsstatusDto() {
		return eingangsrechnungsstatusDto;
	}

	public int execute(Command commandI) throws Throwable {

		int iRetCmd = ICommand.COMMAND_DONE;

		if (commandI instanceof CommandGoto) {
			CommandGoto gotoCommand = (CommandGoto) commandI;

			if (gotoCommand.getsInternalFrame().equals(
					Command.S_INTERNALFRAME_EINGANGSRECHNUNG)) {
				if (gotoCommand.getTabbedPaneAsClass() == Command.CLASS_TABBED_PANE_EINGANGSRECHNUNG) {
					if (gotoCommand.getsPanel().equals(
							Command.PANEL_EINGANGSRECHNUNG_KOPFDATEN)) {
						Integer iId = (Integer) gotoCommand.getHmOfExtraData()
								.get(I_ID_EINGANGSRECHNUNG);
						getTabbedPaneEingangsrechnung()
								.getPanelQueryEingangsrechnung(true)
								.setSelectedId(iId);
						getTabbedPaneEingangsrechnung().setSelectedIndex(1);
						EingangsrechnungDto l = DelegateFactory.getInstance()
								.getEingangsrechnungDelegate()
								.eingangsrechnungFindByPrimaryKey(iId);
						getTabbedPaneEingangsrechnung().setEingangsrechnungDto(
								l);
						if (gotoCommand.getSAction().equals(
								PanelBasis.ACTION_UPDATE)) {
							this.fireItemChanged(this,
									ItemChangedEvent.ACTION_UPDATE);
							getTabbedPaneEingangsrechnung()
									.getPanelEingangsrechnungKopfdaten(true)
									.setKeyWhenDetailPanel(iId);
							getTabbedPaneEingangsrechnung()
									.getPanelEingangsrechnungKopfdaten(true)
									.eventYouAreSelected(false);
						}
					}
				}
			}
		} else {
			iRetCmd = ICommand.COMMAND_NOT_DONE;
		}

		return iRetCmd;

	}
}
