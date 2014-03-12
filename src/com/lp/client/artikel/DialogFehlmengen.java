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
package com.lp.client.artikel;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.WindowEvent;
import java.math.BigDecimal;

import javax.swing.JDialog;

import com.lp.client.fertigung.FertigungFilterFactory;
import com.lp.client.frame.LockStateValue;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQuery;
import com.lp.client.frame.component.PanelSplit;
import com.lp.client.pc.LPMain;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikelFac;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;

@SuppressWarnings("static-access")
public class DialogFehlmengen extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private PanelQuery panelQueryFehlmengen = null;
	private PanelBasis panelDetailFehlmengen = null;
	private PanelBasis panelSplitFehlmengen = null;
	private GridBagLayout gridBagLayout2 = new GridBagLayout();
	private InternalFrame internalFrame = null;
	private BigDecimal bdVerfuegbareMenge = null;
	private ArtikelDto artikelDto = null;
	private Integer lagerIId = null;
	private String[] cSeriennrchargennr = null;

	public DialogFehlmengen() {
		// nothing here
	}

	public DialogFehlmengen(ArtikelDto artikelDto, Integer lagerIId,
			String[] cSeriennrchargennr, BigDecimal bdVerfuegbareMenge,
			InternalFrame internalFrame) throws Throwable {
		super(LPMain.getInstance().getDesktop(), LPMain.getInstance()
				.getTextRespectUISPr("artikel.fehlmengen.aufloesen"), true);

		this.internalFrame = internalFrame;
		this.bdVerfuegbareMenge = bdVerfuegbareMenge;
		this.artikelDto = artikelDto;
		this.lagerIId = lagerIId;
		this.cSeriennrchargennr = cSeriennrchargennr;
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		jbInit();
		pack();

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

		FilterKriterium[] kriterien = new FilterKriterium[1];

		kriterien[0] = new FilterKriterium(
				ArtikelFac.FLR_FEHLMENGE_ARTIKEL_I_ID, true,
				artikelDto.getIId() + "", FilterKriterium.OPERATOR_EQUAL, false);

		panelQueryFehlmengen = new PanelQuery(null, kriterien,
				QueryParameters.UC_ID_FEHLMENGEAUFLOESEN, null, internalFrame,
				LPMain.getInstance().getTextRespectUISPr(
						"artikel.fehlmengen.aufloesen"), true);
		panelQueryFehlmengen.eventYouAreSelected(false);

		panelQueryFehlmengen.setMultipleRowSelectionEnabled(true);

		panelQueryFehlmengen.befuellePanelFilterkriterienDirekt(
				FertigungFilterFactory.getInstance()
						.createFKDFehlmengeAufloesenLosnummer(), null);

		panelDetailFehlmengen = new PanelFehlmengenAufloesen(internalFrame,
				artikelDto, LPMain.getInstance().getTextRespectUISPr(
						"artikel.fehlmengen.aufloesen"), bdVerfuegbareMenge,
				lagerIId, cSeriennrchargennr, panelQueryFehlmengen, this);

		panelSplitFehlmengen = new PanelSplit(internalFrame,
				panelDetailFehlmengen, panelQueryFehlmengen, 380);

		panelDetailFehlmengen.setKeyWhenDetailPanel(panelQueryFehlmengen
				.getSelectedId());
		panelDetailFehlmengen.eventYouAreSelected(false);
		panelQueryFehlmengen.eventYouAreSelected(false);
		this.getContentPane().setLayout(gridBagLayout2);

		this.getContentPane().add(
				panelSplitFehlmengen,
				new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(0, 0, 0, 0), 0, 0));

	}

}
