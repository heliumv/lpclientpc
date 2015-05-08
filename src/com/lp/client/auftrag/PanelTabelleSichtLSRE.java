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
package com.lp.client.auftrag;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.math.BigDecimal;
import java.util.ArrayList;

import javax.swing.KeyStroke;
import javax.swing.SwingConstants;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.LockStateValue;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelTabelle;
import com.lp.client.frame.component.WrapperGotoButton;
import com.lp.client.frame.component.WrapperKeyValueField;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.projekt.ReportProjektstatistik;
import com.lp.server.anfrage.service.AnfrageDto;
import com.lp.server.angebot.service.AngebotDto;
import com.lp.server.angebotstkl.service.AgstklDto;
import com.lp.server.auftrag.service.AuftragDto;
import com.lp.server.auftrag.service.AuftragFac;
import com.lp.server.bestellung.service.BestellungDto;
import com.lp.server.eingangsrechnung.service.EingangsrechnungAuftragszuordnungDto;
import com.lp.server.fertigung.service.LosDto;
import com.lp.server.lieferschein.service.LieferscheinDto;
import com.lp.server.personal.service.PersonalDto;
import com.lp.server.projekt.service.ProjektVerlaufHelperDto;
import com.lp.server.rechnung.service.RechnungDto;
import com.lp.server.rechnung.service.RechnungFac;
import com.lp.server.system.service.SystemFac;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

@SuppressWarnings("static-access")
/**
 * @author Martin Bluehweis
 */
public class PanelTabelleSichtLSRE extends PanelTabelle {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	static final public String GOTO_BELEG = LEAVEALONE + "GOTO_BELEG";

	WrapperGotoButton wbuGoto = new WrapperGotoButton(
			WrapperGotoButton.GOTO_LIEFERSCHEIN_AUSWAHL);

	public PanelTabelleSichtLSRE(int iUsecaseIdI, String sTitelTabbedPaneI,
			InternalFrame oInternalFrameI) throws Throwable {

		super(iUsecaseIdI, sTitelTabbedPaneI, oInternalFrameI);
		jbInit();
		initComponents();
	}

	private void jbInit() throws Throwable {

		setFirstColumnVisible(false);
		setColumnWidth(0, 0);
		this.table.setRowSelectionAllowed(true);

		/*String[] aWhichButtonIUse = { PanelBasis.ACTION_PRINT };
		this.enableToolsPanelButtons(aWhichButtonIUse);*/

		getToolBar()
				.addButtonLeft(
						"/com/lp/client/res/data_into.png",
						LPMain.getTextRespectUISPr("proj.projektverlauf.gehezubeleg"),
						GOTO_BELEG,
						KeyStroke.getKeyStroke('G',
								java.awt.event.InputEvent.CTRL_MASK), null);
		enableToolsPanelButtons(true, GOTO_BELEG);

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

	public Object getKeyDerZeile() {
		return table.getModel().getValueAt(table.getSelectedRow(), 0);
	}

	protected void eventActionPrint(ActionEvent e) throws Throwable {

		Object o = getKeyDerZeile();

	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		super.eventActionSpecial(e);

		if (e.getActionCommand().equals(GOTO_BELEG)) {
			int iZeileCursor = table.getSelectionModel()
					.getAnchorSelectionIndex();
			Object key = table.getValueAt(iZeileCursor, 0);

			Integer iKey = null;
			if (key instanceof RechnungDto) {
				iKey = ((RechnungDto) key).getIId();
				wbuGoto.setWhereToGo(WrapperGotoButton.GOTO_RECHNUNG_AUSWAHL);
			}
			if (key instanceof LieferscheinDto) {
				iKey = ((LieferscheinDto) key).getIId();
				wbuGoto.setWhereToGo(WrapperGotoButton.GOTO_LIEFERSCHEIN_AUSWAHL);
			}

			if (iKey != null) {
				wbuGoto.setOKey(iKey);
				wbuGoto.actionPerformed(new ActionEvent(wbuGoto, 0,
						WrapperGotoButton.ACTION_GOTO));
			}

		}
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {

		super.eventYouAreSelected(false);

	}
}
