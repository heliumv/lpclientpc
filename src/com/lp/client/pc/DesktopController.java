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
package com.lp.client.pc;

import java.util.HashMap;
import java.util.List;

import com.lp.client.frame.component.FehlmengenAufloesen;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ReportAufgeloestefehlmengen;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.frame.report.PanelReportKriterienOptions;
import com.lp.client.pc.Desktop.ModulStatus;
import com.lp.client.util.logger.LpLogger;
import com.lp.server.system.service.ModulberechtigungDto;
import com.lp.server.system.service.ZusatzfunktionberechtigungDto;

public class DesktopController implements IDesktopController {
	private final LpLogger log = (LpLogger) com.lp.client.util.logger.LpLogger
			.getInstance(this.getClass());

	private HashMap<String, String> moduls ;
	private HashMap<String, String> functions ;
	private boolean darfDirekthilfeTexteBearbeiten;
	
	@Override
	public void setModulBerechtigung(ModulberechtigungDto[] modulBerechtigungen) {
		moduls = new HashMap<String, String>() ;
		if(null == modulBerechtigungen) return ;
		
		for (int i = 0; i < modulBerechtigungen.length; i++) {
			moduls.put(modulBerechtigungen[i].getBelegartCNr().trim(),
					modulBerechtigungen[i].getBelegartCNr().trim()) ;
		}
	}

	@Override
	public boolean darfAnwenderAufModulZugreifen(String whichModul) {
		return moduls.containsKey(whichModul.trim()) ;
	}

	@Override
	public void setZusatzFunktionen(
			ZusatzfunktionberechtigungDto[] zusatzFunktionen) {		
		functions = new HashMap<String, String>() ;
		if(null == zusatzFunktionen) return ;
		
		for (int i = 0; i < zusatzFunktionen.length; i++) {
			functions.put(zusatzFunktionen[i].getZusatzfunktionCNr().trim(),
					zusatzFunktionen[i].getZusatzfunktionCNr().trim()) ;
		}
	}

	@Override
	public boolean darfAnwenderAufZusatzfunktionZugreifen(
			String whichZusatzfunktion) {
		return functions.containsKey(whichZusatzfunktion.trim()) ;
	}

	@Override
	public boolean darfDirekthilfeTexteEditieren() {
		return darfDirekthilfeTexteBearbeiten;
	}

	@Override
	public void setDarfDirekthilfeTexteEditieren(boolean b) {
		darfDirekthilfeTexteBearbeiten = b;
	}
	
	@Override
	public boolean hatAufgeloesteFehlmengen() {
		return FehlmengenAufloesen.getAufgeloesteFehlmengen().size() > 0 ;
	}
	
	@Override
	public boolean hatOffeneAenderungen() {
		return hatAufgeloesteFehlmengen() ;
	}

	@Override
	public void behandleOffeneAenderungen(List<ModulStatus> offeneModule) throws Throwable {
		if(!hatOffeneAenderungen()) return ;

		if(offeneModule == null || offeneModule.size() == 0) {
			throw new IllegalArgumentException(
				"Die Liste der offenen Module darf nicht null sein und es wird erwartet, " +
				"dass mindestens 1 Modul vorhanden ist, sonst macht die Fehlmengenaufloesung keinen Sinn!") ;
		}
		behandleOffeneFehlmengen(
			(InternalFrame)offeneModule.get(0).getLpModule().getLPModule());
	}
	
	public void behandleOffeneFehlmengen(InternalFrame internalFrame) throws Throwable {
		if (FehlmengenAufloesen.getAufgeloesteFehlmengen().size() > 0) {
			boolean bOption = DialogFactory
					.showModalJaNeinDialog(
							internalFrame,
							LPMain.getTextRespectUISPr("lp.frage.fehlmengenaufloesendrucken"),
							LPMain.getTextRespectUISPr("lp.hint"));
			if (bOption) {
				PanelReportKriterienOptions options = new PanelReportKriterienOptions();
				options.setInternalFrame(internalFrame);
				options.setMitEmailFax(true);
				internalFrame.showReportKriterienDialog(
						new ReportAufgeloestefehlmengen(internalFrame,
								FehlmengenAufloesen
										.getAufgeloesteFehlmengen()),
						options);

//				exc = new PropertyVetoException("", null);
			}
			
			log.warn("Es gab " 
					+ FehlmengenAufloesen.getAufgeloesteFehlmengen().size() 
					+ " aufgeloeste Fehlmenge(n), die " 
					+ (!bOption ? "nicht" : "") + " gedruckt wurde(n).");
			FehlmengenAufloesen.loescheAufgeloesteFehlmengen();
		}		
	}
}
