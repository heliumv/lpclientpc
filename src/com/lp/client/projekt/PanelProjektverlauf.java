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
package com.lp.client.projekt;

import java.awt.Dimension;
import java.awt.event.ActionEvent;

import javax.swing.KeyStroke;

import com.lp.client.auftrag.ReportAuftragstatistik;
import com.lp.client.fertigung.InternalFrameFertigung;
import com.lp.client.fertigung.ReportNachkalkulation;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.LockStateValue;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelTabelle;
import com.lp.client.frame.component.WrapperGotoButton;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.anfrage.service.AnfrageDto;
import com.lp.server.angebot.service.AngebotDto;
import com.lp.server.auftrag.service.AuftragDto;
import com.lp.server.bestellung.service.BestellungDto;
import com.lp.server.fertigung.service.LosDto;
import com.lp.server.lieferschein.service.LieferscheinDto;
import com.lp.server.projekt.service.ProjektVerlaufHelperDto;
import com.lp.server.rechnung.service.RechnungDto;
import com.lp.server.rechnung.service.RechnungFac;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MandantFac;

public class PanelProjektverlauf extends PanelTabelle {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	static final public String NACHKALKULATION_PRINT = LEAVEALONE
			+ "NACHKALKULATION_PRINT";

	static final public String GOTO_BELEG = LEAVEALONE + "GOTO_BELEG";

	public final static String MY_OWN_NEW_AUFTRAG = PanelBasis.ACTION_MY_OWN_NEW
			+ "MY_OWN_NEW_AUFTRAG";

	WrapperGotoButton wbuGoto = new WrapperGotoButton(
			WrapperGotoButton.GOTO_STUECKLISTE_DETAIL);

	InternalFrameProjekt internalFrameProjekt = null;

	public PanelProjektverlauf(int iUsecaseIdI, String sTitelTabbedPaneI,
			InternalFrameProjekt oInternalFrameI) throws Throwable {

		super(iUsecaseIdI, sTitelTabbedPaneI, oInternalFrameI);
		internalFrameProjekt = oInternalFrameI;
		jbInit();
		initComponents();
	}

	private void jbInit() throws Throwable {

		setFirstColumnVisible(false);
		setColumnWidth(0, 0);
		this.table.setRowSelectionAllowed(true);

		getToolBar()
				.addButtonLeft(
						"/com/lp/client/res/printer.png",
						LPMain.getTextRespectUISPr("lp.printer"),
						NACHKALKULATION_PRINT,
						KeyStroke.getKeyStroke('P',
								java.awt.event.InputEvent.CTRL_MASK), null);

		enableToolsPanelButtons(true, NACHKALKULATION_PRINT);
		getToolBar()
				.addButtonLeft(
						"/com/lp/client/res/data_into.png",
						LPMain.getTextRespectUISPr("proj.projektverlauf.gehezubeleg"),
						GOTO_BELEG,
						KeyStroke.getKeyStroke('G',
								java.awt.event.InputEvent.CTRL_MASK), null);
		enableToolsPanelButtons(true, GOTO_BELEG);

		if (LPMain
				.getInstance()
				.getDesktop()
				.darfAnwenderAufZusatzfunktionZugreifen(
						MandantFac.ZUSATZFUNKTION_PROJEKTKLAMMER)) {

			if (LPMain.getInstance().getDesktop()
					.darfAnwenderAufModulZugreifen(LocaleFac.BELEGART_AUFTRAG)) {
				getToolBar()
						.addButtonLeft(
								"/com/lp/client/res/auftrag16x16.png",
								LPMain.getTextRespectUISPr("proj.neuer.auftragausangebot"),
								MY_OWN_NEW_AUFTRAG, null, null);
			}
		}

	}

	/**
	 * eventActionRefresh
	 * 
	 * @param e
	 *            ActionEvent
	 * @param bNeedNoRefreshI
	 *            boolean
	 * @throws Throwable
	 */
	protected void eventActionRefresh(ActionEvent e, boolean bNeedNoRefreshI)
			throws Throwable {

		super.eventActionRefresh(e, bNeedNoRefreshI);
	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		super.eventActionSpecial(e);
		if (e.getActionCommand().equals(NACHKALKULATION_PRINT)) {
			String add2Title = "";
			getInternalFrame()
					.showReportKriterien(
							new ReportProjektstatistik(internalFrameProjekt,
									add2Title));
		} else if (e.getActionCommand().equals(MY_OWN_NEW_AUFTRAG)) {
			int iZeileCursor = table.getSelectionModel()
					.getAnchorSelectionIndex();
			Object key = table.getValueAt(iZeileCursor, 0);
			if (key != null && key instanceof ProjektVerlaufHelperDto) {
				ProjektVerlaufHelperDto pvDto = (ProjektVerlaufHelperDto) key;

				if (pvDto.getBelegDto() != null) {
					if (pvDto.getBelegDto() instanceof AngebotDto) {
						Integer iKey = ((AngebotDto) pvDto.getBelegDto())
								.getIId();
						DelegateFactory.getInstance().getAngebotDelegate()
								.erzeugeAuftragAusAngebot(iKey, false);
						internalFrameProjekt.getTabbedPaneProjekt()
								.projektverlauf
								.eventYouAreSelected(false);
						internalFrameProjekt.getTabbedPaneProjekt()
						.projektverlauf.updateButtons(new LockStateValue(PanelBasis.LOCK_IS_NOT_LOCKED));
					} else {

						DialogFactory
								.showModalDialog(
										LPMain.getTextRespectUISPr("lp.info"),
										LPMain.getTextRespectUISPr("proj.neuer.auftrag.nurbeiangebotmoeglich"));
					}
				}
			}
		}
		if (e.getActionCommand().equals(GOTO_BELEG)) {
			int iZeileCursor = table.getSelectionModel()
					.getAnchorSelectionIndex();
			Object key = table.getValueAt(iZeileCursor, 0);
			if (key != null && key instanceof ProjektVerlaufHelperDto) {
				ProjektVerlaufHelperDto pvDto = (ProjektVerlaufHelperDto) key;

				if (pvDto.getBelegDto() != null) {

					Integer iKey = null;
					if (pvDto.getBelegDto() instanceof AngebotDto) {
						iKey = ((AngebotDto) pvDto.getBelegDto()).getIId();
						wbuGoto.setWhereToGo(WrapperGotoButton.GOTO_ANGEBOT_AUSWAHL);
					} else if (pvDto.getBelegDto() instanceof AuftragDto) {
						iKey = ((AuftragDto) pvDto.getBelegDto()).getIId();
						wbuGoto.setWhereToGo(WrapperGotoButton.GOTO_AUFTRAG_AUSWAHL);
					} else if (pvDto.getBelegDto() instanceof LieferscheinDto) {
						iKey = ((LieferscheinDto) pvDto.getBelegDto()).getIId();
						wbuGoto.setWhereToGo(WrapperGotoButton.GOTO_LIEFERSCHEIN_AUSWAHL);
					} else if (pvDto.getBelegDto() instanceof RechnungDto) {
						RechnungDto rDto = (RechnungDto) pvDto.getBelegDto();
						iKey = rDto.getIId();

						if (rDto.getRechnungartCNr().equals(
								RechnungFac.RECHNUNGART_GUTSCHRIFT)) {
							wbuGoto.setWhereToGo(WrapperGotoButton.GOTO_GUTSCHRIFT_AUSWAHL);
						} else {
							wbuGoto.setWhereToGo(WrapperGotoButton.GOTO_RECHNUNG_AUSWAHL);
						}

					} else if (pvDto.getBelegDto() instanceof LosDto) {
						iKey = ((LosDto) pvDto.getBelegDto()).getIId();
						wbuGoto.setWhereToGo(WrapperGotoButton.GOTO_FERTIGUNG_AUSWAHL);
					} else if (pvDto.getBelegDto() instanceof BestellungDto) {
						iKey = ((BestellungDto) pvDto.getBelegDto()).getIId();
						wbuGoto.setWhereToGo(WrapperGotoButton.GOTO_BESTELLUNG_AUSWAHL);
					} else if (pvDto.getBelegDto() instanceof AnfrageDto) {
						iKey = ((AnfrageDto) pvDto.getBelegDto()).getIId();
						wbuGoto.setWhereToGo(WrapperGotoButton.GOTO_ANFRAGE_AUSWAHL);
					}

					if (iKey != null) {
						wbuGoto.setOKey(iKey);
						wbuGoto.actionPerformed(new ActionEvent(wbuGoto, 0,
								WrapperGotoButton.ACTION_GOTO));
					}

				}

			}
		}
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {

		super.eventYouAreSelected(false);

	}
}
