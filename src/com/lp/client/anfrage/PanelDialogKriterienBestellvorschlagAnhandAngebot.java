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
package com.lp.client.anfrage;

import java.awt.GridBagConstraints;
import java.awt.HeadlessException;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.sql.Date;
import java.util.ArrayList;
import java.util.EventObject;

import com.lp.client.angebot.AngebotFilterFactory;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelDialogKriterien;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperDateField;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.angebot.service.AngebotDto;
import com.lp.server.fertigung.service.LosDto;

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
public class PanelDialogKriterienBestellvorschlagAnhandAngebot extends
		PanelDialogKriterien {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	public Integer angebotIId = null;
	
	private WrapperLabel wlaLiefertermin = null;
	private WrapperDateField wdfLiefertermin = null;

	private PanelQueryFLR panelQueryFLRAngebot = null;

	private WrapperButton wbuAngebot = new WrapperButton();
	private WrapperTextField wtfAngebot = new WrapperTextField();
	static final public String ACTION_SPECIAL_ANGEBOT_FROM_LISTE = "action_angebot_auftrag_liste";

	private static final String ACTION_SPECIAL_LEEREN = "action_special_leeren";

	public PanelDialogKriterienBestellvorschlagAnhandAngebot(
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

	public void dialogQueryAngebotFromListe(ActionEvent e) throws Throwable {
		panelQueryFLRAngebot = AngebotFilterFactory.getInstance()
				.createPanelFLRAngebotErledigteVersteckt(getInternalFrame(),
						true, false);
		panelQueryFLRAngebot.setMultipleRowSelectionEnabled(false);

		new DialogQuery(panelQueryFLRAngebot);
	}

	private void jbInit() throws Throwable {

		wlaLiefertermin = new WrapperLabel();
		wlaLiefertermin.setText(LPMain.getInstance().getTextRespectUISPr(
				"label.liefertermin"));
		wdfLiefertermin = new WrapperDateField();

		wbuAngebot.setText(LPMain.getInstance().getTextRespectUISPr(
				"angb.angebot")
				+ "...");
		wbuAngebot.setActionCommand(ACTION_SPECIAL_ANGEBOT_FROM_LISTE);
		wbuAngebot.addActionListener(this);
		wtfAngebot.setColumnsMax(com.lp.server.util.Facade.MAX_UNBESCHRAENKT);
		wtfAngebot.setMandatoryField(true);
		wtfAngebot.setActivatable(false);

		iZeile++;
		jpaWorkingOn.add(wlaLiefertermin, new GridBagConstraints(1, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 100, 0));

		jpaWorkingOn.add(wdfLiefertermin, new GridBagConstraints(2, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wbuAngebot, new GridBagConstraints(1, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wtfAngebot, new GridBagConstraints(2, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));

	}

	private void setDefaults() throws Throwable {

		wdfLiefertermin.setDate(new Date(System.currentTimeMillis()));

	}

	public Date getLiefertermin() {
		return wdfLiefertermin.getDate();
	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		
		 if (e.getActionCommand().equals(ACTION_SPECIAL_OK) && angebotIId == null) {
			 //Pflichtfelder
			 showDialogPflichtfelderAusfuellen();
			 return;
		 }
		
		if (e.getActionCommand().equals(ACTION_SPECIAL_ANGEBOT_FROM_LISTE)) {
			dialogQueryAngebotFromListe(e);
		}
		else if (e.getActionCommand().equals(ACTION_SPECIAL_LEEREN)) {

		} else {
			super.eventActionSpecial(e);
		}

	}

	public void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;
		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			if (e.getSource() == panelQueryFLRAngebot) {
				angebotIId = (Integer)panelQueryFLRAngebot.getSelectedId();

				AngebotDto angebotDto = DelegateFactory.getInstance()
						.getAngebotDelegate()
						.angebotFindByPrimaryKey(angebotIId);
			
			
				wtfAngebot.setText(angebotDto.getCNr());
			} 
		} else if (e.getID() == ItemChangedEvent.ACTION_LEEREN) {

		}
	}

}
