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
package com.lp.client.frame.component;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.HeadlessException;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.sql.Date;
import java.util.ArrayList;
import java.util.EventObject;

import javax.swing.ImageIcon;
import javax.swing.JButton;

import com.lp.client.auftrag.AuftragFilterFactory;
import com.lp.client.fertigung.FertigungFilterFactory;
import com.lp.client.frame.Defaults;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.lieferschein.LieferscheinFilterFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.auftrag.service.AuftragDto;
import com.lp.server.fertigung.service.LosDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;

@SuppressWarnings("static-access")
/**
 * <p><I>Dialog zur Eingabe der Kriterien fuer den Bestellvorschlag.</I></p>
 * <p>Dieser Dialog wird aus den folgenden Modulen aufgerufen:</p>
 * <ul>
 * <li>Bestellung/Bestellvorschlag
 * <li>Anfrage/Anfragevorschlag
 * </ul>
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 * <p>Erstellungsdatum <I>22.11.05</I></p>
 * <p> </p>
 * @author Uli Walch
 * @version $Revision: 1.9 $
 */
public class PanelDialogKriterienBestellvorschlagAnhandStuecklistenmindestlagerstand
		extends PanelDialogKriterien {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private WrapperLabel wlaLiefertermin = null;
	private WrapperDateField wdfLiefertermin = null;
	private WrapperCheckBox wcbVormerklisteLoeschen = null;

	private static final String ACTION_SPECIAL_LEEREN = "action_special_leeren";

	public PanelDialogKriterienBestellvorschlagAnhandStuecklistenmindestlagerstand(
			InternalFrame oInternalFrameI, String title)
			throws HeadlessException, Throwable {
		super(oInternalFrameI, title);

		try {
			jbInit();
			setDefaults();
			initComponents();
		} catch (Throwable t) {
			LPMain.getInstance().exitFrame(getInternalFrame());
		}
	}

	private void jbInit() throws Throwable {

		wlaLiefertermin = new WrapperLabel();
		wlaLiefertermin.setText(LPMain.getInstance()
				.getTextRespectUISPr(
						"label.liefertermin"));
		wdfLiefertermin = new WrapperDateField();

		wcbVormerklisteLoeschen = new WrapperCheckBox(LPMain
				.getInstance().getTextRespectUISPr(
						"bes.vormerklisteLoeschen"));
		
		iZeile++;
		jpaWorkingOn.add(wlaLiefertermin,
				new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(2, 2, 2, 2), 100, 0));

		jpaWorkingOn.add(wdfLiefertermin,
				new GridBagConstraints(2, iZeile, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(2, 2, 2, 2), 0, 0));
		
		iZeile++;
		jpaWorkingOn.add(wcbVormerklisteLoeschen,
				new GridBagConstraints(2, iZeile,2, 1, 0.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(2, 2, 2, 2), 150, 0));
		

	}

	private void setDefaults() throws Throwable {

		wdfLiefertermin.setDate(new Date(System
				.currentTimeMillis()));

	}

	public Date getLiefertermin() {
		return wdfLiefertermin.getDate();
	}

	public boolean isbVormerklisteLoeschen() {
		return wcbVormerklisteLoeschen.isSelected();
	}
	
	protected void eventActionSpecial(ActionEvent e) throws Throwable {

		if (e.getActionCommand().equals(ACTION_SPECIAL_LEEREN)) {

		} else {
			super.eventActionSpecial(e);
		}

	}

	public void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;
		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {

		} else if (e.getID() == ItemChangedEvent.ACTION_LEEREN) {

		}
	}

}
