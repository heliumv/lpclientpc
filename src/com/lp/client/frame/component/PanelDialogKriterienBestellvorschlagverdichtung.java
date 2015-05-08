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

import java.awt.GridBagConstraints;
import java.awt.Insets;

import com.lp.client.frame.HelperClient;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.system.service.MandantFac;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;

@SuppressWarnings("static-access")
/**
 * <p> Diese Klasse kuemmert sich ...</p>
 *
 * <p>Copyright Logistik Pur Software GmbH (c) 2004, 2005, 2006</p>
 *
 * <p>Erstellung: Vorname Nachname; dd.mm.06</p>
 *
 * <p>@author $Author: valentin $</p>
 *
 * @version unbekannt Date $Date: 2008/08/11 08:39:23 $
 */
public class PanelDialogKriterienBestellvorschlagverdichtung extends
		PanelDialogKriterien {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private WrapperLabel wlaVerdichtungsZeitraum = null;
	private WrapperNumberField wnfVerdichtungsZeitraum = null;

	
	private WrapperCheckBox wcbMindestbestellmengeBeruecksichtigen = null;
	private WrapperCheckBox wcbProjektklammerBeruecksichtigen = null;

	public PanelDialogKriterienBestellvorschlagverdichtung(
			InternalFrame oInternalFrameI, String add2Title) throws Throwable {
		super(oInternalFrameI, add2Title);

		try {
			jbInit();
			setDefaults();
			initComponents();
		} catch (Throwable t) {
			LPMain.getInstance().exitFrame(getInternalFrame());
		}
	}

	private void jbInit() throws Throwable {
		wlaVerdichtungsZeitraum = new WrapperLabel(LPMain.getInstance()
				.getTextRespectUISPr("bes.verdichtungszeitraum"));

		wnfVerdichtungsZeitraum = new WrapperNumberField();
		wnfVerdichtungsZeitraum.setMandatoryField(true);
		wnfVerdichtungsZeitraum.setMaximumIntegerDigits(3);
		wnfVerdichtungsZeitraum.setFractionDigits(0);
		wnfVerdichtungsZeitraum.setMinimumValue(1);
		HelperClient.setDefaultsToComponent(wnfVerdichtungsZeitraum, 120);

		
		wcbMindestbestellmengeBeruecksichtigen = new WrapperCheckBox(LPMain.getInstance().getTextRespectUISPr(
				"bes.mindestbestellmengenberuecksichtigen"));
		wcbMindestbestellmengeBeruecksichtigen.setEnabled(true);
		wcbMindestbestellmengeBeruecksichtigen.setSelected(true);

		
		wcbProjektklammerBeruecksichtigen = new WrapperCheckBox(LPMain
				.getInstance().getTextRespectUISPr(
						"bes.bestellvorschlag.projektklammerberuecksichtigen"));
		wcbProjektklammerBeruecksichtigen.setEnabled(true);
		wcbProjektklammerBeruecksichtigen.setSelected(false);

		iZeile++;
		jpaWorkingOn.add(wlaVerdichtungsZeitraum, new GridBagConstraints(0,
				iZeile, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wnfVerdichtungsZeitraum, new GridBagConstraints(1,
				iZeile, 1, 1, 0.5, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		
		jpaWorkingOn.add(wcbMindestbestellmengeBeruecksichtigen,
				new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;

		if (LPMain
				.getInstance()
				.getDesktop()
				.darfAnwenderAufZusatzfunktionZugreifen(
						MandantFac.ZUSATZFUNKTION_PROJEKTKLAMMER)) {
			wcbProjektklammerBeruecksichtigen.setSelected(true);
			jpaWorkingOn.add(wcbProjektklammerBeruecksichtigen,
					new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0,
							GridBagConstraints.CENTER, GridBagConstraints.BOTH,
							new Insets(2, 2, 2, 2), 0, 0));
		}

	}

	private void setDefaults() throws Throwable {
		// den Vorschlagswert fuer die Auftragsvorlaufdauer bestimmen
		ParametermandantDto parametermandantDto = DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getMandantparameter(
						LPMain.getInstance().getTheClient().getMandant(),
						ParameterFac.KATEGORIE_BESTELLUNG,
						ParameterFac.BESTELLVORSCHLAG_VERDICHTUNGSZEITRAUM_TAGE);

		wnfVerdichtungsZeitraum.setInteger((Integer) parametermandantDto
				.getCWertAsObject());
		wcbMindestbestellmengeBeruecksichtigen.setSelected(true);
	}

	public Long getVerdichtungszeitraum() throws Throwable {
		return Long.parseLong(wnfVerdichtungsZeitraum.getInteger().toString());
	}

	public boolean getMindestbestellmengeBeruecksichtigen() {
		return wcbMindestbestellmengeBeruecksichtigen.isSelected();
	}

	public boolean getProjektklammerBeruecksichtigen() {
		return wcbProjektklammerBeruecksichtigen.isSelected();
	}

}
