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
package com.lp.client.bestellung;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.WindowEvent;

import javax.swing.JDialog;

import com.lp.client.frame.LockStateValue;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQuery;
import com.lp.client.frame.component.PanelSplit;
import com.lp.client.pc.LPMain;
import com.lp.server.artikel.service.ArtikelFac;
import com.lp.server.bestellung.service.BestellpositionFac;
import com.lp.server.bestellung.service.BestellungFac;
import com.lp.server.util.Facade;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.FilterKriteriumDirekt;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;

@SuppressWarnings("static-access")
public class DialogOffeneWEPos extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private PanelQuery panelQueryFehlmengen = null;
	private PanelBasis panelDetailFehlmengen = null;
	private PanelBasis panelSplitFehlmengen = null;
	private GridBagLayout gridBagLayout2 = new GridBagLayout();
	private InternalFrame internalFrame = null;

	public DialogOffeneWEPos() {
		// nothing here
	}

	public DialogOffeneWEPos(InternalFrame internalFrame) throws Throwable {
		super(LPMain.getInstance().getDesktop(), LPMain.getInstance()
				.getTextRespectUISPr("bes.offenewepos.uebersichtoffene"), true);

		this.internalFrame = internalFrame;

		setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		jbInit();
		pack();
		this.setSize(800, 600);
		panelQueryFehlmengen.updateButtons(new LockStateValue(
				PanelBasis.LOCK_IS_NOT_LOCKED));
		panelDetailFehlmengen.updateButtons(new LockStateValue(
				PanelBasis.LOCK_IS_NOT_LOCKED));

	}

	protected void processWindowEvent(WindowEvent e) {
		if (e.getID() == WindowEvent.WINDOW_CLOSING) {
			internalFrame.removeItemChangedListener(panelDetailFehlmengen);
			panelDetailFehlmengen = null;
			this.dispose();
		}
	}

	private void jbInit() throws Throwable {

		FilterKriterium[] kriterien = new FilterKriterium[6];

		kriterien[0] = new FilterKriterium("position."
				+ BestellpositionFac.FLR_BESTELLPOSITION_FLRBESTELLUNG
				+ ".mandant_c_nr", true, "'"
				+ LPMain.getTheClient().getMandant() + "'",
				FilterKriterium.OPERATOR_EQUAL, false);
		kriterien[1] = new FilterKriterium(
				"position."
						+ BestellpositionFac.FLR_BESTELLPOSITION_BESTELLPOSITIONSTATUS_C_NR,
				true, "('" + BestellpositionFac.BESTELLPOSITIONSTATUS_ERLEDIGT
						+ "','"
						+ BestellpositionFac.BESTELLPOSITIONSTATUS_GELIEFERT
						+ "','"
						+ BestellpositionFac.BESTELLPOSITIONSTATUS_STORNIERT
						+ "')", FilterKriterium.OPERATOR_NOT_IN, false);
		kriterien[2] = new FilterKriterium(
				"position."
						+ BestellpositionFac.FLR_BESTELLPOSITION_FLRBESTELLUNG+"."+BestellungFac.FLR_BESTELLUNG_BESTELLUNGSTATUS_C_NR,
				true, "('" + BestellungFac.BESTELLSTATUS_BESTAETIGT
						+ "','"
						+ BestellungFac.BESTELLSTATUS_OFFEN
						+ "','"
						+ BestellungFac.BESTELLSTATUS_TEILERLEDIGT
						+ "')", FilterKriterium.OPERATOR_IN, false);

		kriterien[3] = new FilterKriterium("position.position_i_id_artikelset",
				true, "", FilterKriterium.OPERATOR_IS + " "
						+ FilterKriterium.OPERATOR_NULL, false);

		kriterien[4] = new FilterKriterium("position."
				+ BestellpositionFac.FLR_BESTELLPOSITION_FLRBESTELLUNG + "."
				+ BestellungFac.FLR_BESTELLUNG_BESTELLUNGART_C_NR, true, "'"
				+ BestellungFac.BESTELLUNGART_RAHMENBESTELLUNG_C_NR + "'",
				FilterKriterium.OPERATOR_NOT_EQUAL, false);
		
		kriterien[5] = new FilterKriterium("position."
				+ BestellpositionFac.FLR_BESTELLPOSITION_FLRARTIKEL + ".i_id", true,"",
				FilterKriterium.OPERATOR_IS +" "+FilterKriterium.OPERATOR_NOT_NULL, false);
		

		panelQueryFehlmengen = new PanelQuery(null, kriterien,
				QueryParameters.UC_ID_OFFENEWEPOS, null, internalFrame, LPMain
						.getInstance().getTextRespectUISPr(
								"bes.offenewepos.uebersichtoffene"), true);
		panelQueryFehlmengen.eventYouAreSelected(false);

		panelQueryFehlmengen.setMultipleRowSelectionEnabled(true);

		panelQueryFehlmengen.befuellePanelFilterkriterienDirekt(
				new FilterKriteriumDirekt("position.flrartikel.c_nr", "",
						FilterKriterium.OPERATOR_LIKE, LPMain.getInstance()
								.getTextRespectUISPr("artikel.artikelnummer"),
						FilterKriteriumDirekt.PROZENT_TRAILING, true, true,
						Facade.MAX_UNBESCHRAENKT),
				new FilterKriteriumDirekt("aspr.c_bez", "",
						FilterKriterium.OPERATOR_LIKE, LPMain.getInstance()
								.getTextRespectUISPr("lp.bezeichnung"),
						FilterKriteriumDirekt.PROZENT_BOTH, true, true,
						Facade.MAX_UNBESCHRAENKT));

		panelDetailFehlmengen = new PanelOffeneWEPos(internalFrame,
				(Integer) panelQueryFehlmengen.getSelectedId(),
				panelQueryFehlmengen,this);

		panelSplitFehlmengen = new PanelSplit(internalFrame,
				panelDetailFehlmengen, panelQueryFehlmengen, 300);

		panelDetailFehlmengen.setKeyWhenDetailPanel(panelQueryFehlmengen
				.getSelectedId());
		panelDetailFehlmengen.eventYouAreSelected(false);

		this.getContentPane().setLayout(gridBagLayout2);

		this.getContentPane().add(
				panelSplitFehlmengen,
				new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(0, 0, 0, 0), 0, 0));

	}

}
