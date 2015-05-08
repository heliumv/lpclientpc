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
import java.awt.GridBagLayout;
import java.awt.Insets;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;

@SuppressWarnings("static-access")
/**
 * <p>Basisfenster fuer LP5 Positionen.</p>
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 * <p>Erstellungsdatum 2005-02-11</p>
 * <p> </p>
 * @author Uli Walch
 * @version $Revision: 1.9 $
 */
public class PanelPositionenHandeingabe extends PanelPositionenPreiseingabe {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int iYGridBagNext = 0;

	public WrapperLabel wlaBezeichnung = null;
	public WrapperTextField wtfBezeichnung = null;
	public WrapperTextField wtfZusatzbezeichnung = null;

	/**
	 * Konstruktor.
	 * 
	 * @param internalFrame
	 *            der InternalFrame auf dem das Panel sitzt
	 * @param add2TitleI
	 *            der default Titel des Panels
	 * @param key
	 *            PK der Position
	 * @param sLockMeWer
	 *            String
	 * @param iSpaltenbreite1I
	 *            die Breites der ersten Spalte
	 * @param bDarfPreiseSehen
	 *            boolean
	 * @throws Throwable
	 *             Ausnahme
	 */
	public PanelPositionenHandeingabe(InternalFrame internalFrame,
			String add2TitleI, Object key, String sLockMeWer,
			int iSpaltenbreite1I, boolean bDarfPreiseSehen, boolean bDarfPreiseAendern) throws Throwable {
		/**
		 * @todo MR/MB: bDarfPreiseSehenI muss richtig gesetzt werden
		 */
		super(internalFrame, add2TitleI, key, sLockMeWer, bDarfPreiseSehen,bDarfPreiseAendern,
				iSpaltenbreite1I);

		jbInit();
	}

	private void jbInit() throws Throwable {
		setLayout(new GridBagLayout());

		wlaBezeichnung = new WrapperLabel(LPMain.getInstance()
				.getTextRespectUISPr("label.bezeichnung"));
		wlaBezeichnung.setMaximumSize(new Dimension(iSpaltenbreite1, Defaults
				.getInstance().getControlHeight()));
		wlaBezeichnung.setMinimumSize(new Dimension(iSpaltenbreite1, Defaults
				.getInstance().getControlHeight()));
		wlaBezeichnung.setPreferredSize(new Dimension(iSpaltenbreite1, Defaults
				.getInstance().getControlHeight()));

		wtfBezeichnung = new WrapperTextField();

		wtfZusatzbezeichnung = new WrapperTextField();

		add(wlaBezeichnung, new GridBagConstraints(2, iYGridBagNext, 1, 1, 0.0,
				0.0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		add(wtfBezeichnung, new GridBagConstraints(3, iYGridBagNext, 5, 1, 0.0,
				0.0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));

		iYGridBagNext++;
		ParametermandantDto parametermandantDto = DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getMandantparameter(LPMain.getTheClient().getMandant(),
						ParameterFac.KATEGORIE_ALLGEMEIN,
						ParameterFac.PARAMETER_LV_POSITION);

		boolean bMitLVPositionen = ((Boolean) parametermandantDto
				.getCWertAsObject()).booleanValue();
		if (bMitLVPositionen == true) {
			wlaLVPosition.setText(LPMain.getTextRespectUISPr("lp.lvposition"));
			add(wlaLVPosition,
					new GridBagConstraints(0, iYGridBagNext, 1, 1, 0.0, 0.0,
							GridBagConstraints.NORTH,
							GridBagConstraints.HORIZONTAL, new Insets(1, 2, 1,
									2), 0, 0));
			add(wtfLVPosition,
					new GridBagConstraints(1, iYGridBagNext, 1, 1, 0.0, 0.0,
							GridBagConstraints.NORTH,
							GridBagConstraints.HORIZONTAL, new Insets(1, 2, 1,
									2), 0, 0));
		}

		add(wtfZusatzbezeichnung, new GridBagConstraints(3, iYGridBagNext, 5,
				1, 0.0, 0.0, GridBagConstraints.NORTH,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		iYGridBagNext++;
		addFormatierungszeileNettoeinzelpreis(this, iYGridBagNext);

		iYGridBagNext++;
		addZeileRabattsumme(this, iYGridBagNext);

		iYGridBagNext++;
		addZeileZusatzrabattsumme(this, iYGridBagNext);

		iYGridBagNext++;

		if (getInternalFrame().getBelegartCNr().equals(
				LocaleFac.BELEGART_ANGEBOT)
				|| getInternalFrame().getBelegartCNr().equals(
						LocaleFac.BELEGART_AUFTRAG)
				|| getInternalFrame().getBelegartCNr().equals(
						LocaleFac.BELEGART_LIEFERSCHEIN)
				|| getInternalFrame().getBelegartCNr().equals(
						LocaleFac.BELEGART_RECHNUNG)) {
			addZeileNettogesamtpreis(this, iYGridBagNext, true);
		} else {
			addZeileNettogesamtpreis(this, iYGridBagNext, false);
		}

		iYGridBagNext++;
		addZeileMwstsumme(this, iYGridBagNext);

		iYGridBagNext++;

		addZeileBruttogesamtpreis(this, iYGridBagNext);

		iYGridBagNext++;
		addZeileLieferterminposition(this, iYGridBagNext);

	}
}
