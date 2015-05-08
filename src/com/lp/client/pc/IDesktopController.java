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

import java.util.List;

import com.lp.client.frame.component.InternalFrame;
import com.lp.client.pc.Desktop.ModulStatus;
import com.lp.server.system.service.ModulberechtigungDto;
import com.lp.server.system.service.ZusatzfunktionberechtigungDto;

public interface IDesktopController {

	/**
	 * Neue Modulberechtigungen setzen
	 * 
	 * #see darfAnwenderAufModulZugreifen
	 * @param modulBerechtigungen
	 */
	public void setModulBerechtigung(ModulberechtigungDto[] modulBerechtigungen) ;
	
	/**
	 * Hat Anwender die entsprechende Modulberechtigung?
	 * @param whichModul
	 * @return true wenn die Modulberechtigung vergeben ist, ansonsten false
	 */
	public boolean darfAnwenderAufModulZugreifen(String whichModul) ;
	
	
	/** 
	 * Neue Zusatzberechtigungen setzen
	 * 
	 * @param zusatzFunktionen
	 */
	public void setZusatzFunktionen(ZusatzfunktionberechtigungDto[] zusatzFunktionen) ;
	
	/**
	 * Hat Anwender die entsprechende Berechtigung f&uuml;r die Zusatzfunktion?
	 * 
	 * @param whichZusatzfunktion
	 * @return true wenn die Zusatzfunktionsberechtigung vorhanden ist
	 */
	public boolean darfAnwenderAufZusatzfunktionZugreifen(String whichZusatzfunktion) ;

	/**
	 * Hat der Anwender das Recht RECHT_LP_DARF_DIREKTHILFETEXTE_BEARBEITEN?
	 * @return true wenn er es hat, sonst false
	 */
	public boolean darfDirekthilfeTexteEditieren();
	
	public void setDarfDirekthilfeTexteEditieren(boolean b);

	/**
	 * Gibt es bereits aufgeloeste Fehlmengen die noch gedruckt werden sollen?
	 * 
	 * @return true wenn es noch Fehlmengen gibt, die gedruckt werden sollen
	 */
	boolean hatAufgeloesteFehlmengen() ;
	
	/**
	 * Gibt es im gesamten System &Auml;nderungen (an Daten) die vor der Beendigung behandelt werden m&uuml;ssen?</br>
	 * <p> Die Behandlung von offenen Modulen und deren damit verbundenen Daten ist nicht im Scope, das macht das 
	 * #see {@link Desktop#doLogout()}</p>
	 * 
	 * @return true wenn es noch zu behandelnde &Auml;nderungen gibt.
	 */
	boolean hatOffeneAenderungen() ;
	
	/**
	 * Im System befindliche Daten&auml;nderungen "persistieren"/behandlen/verwerfen/...
	 * 
	 * @throws Throwable 
	 */
	void behandleOffeneAenderungen(List<ModulStatus> offeneModule) throws Throwable ;
	
	void behandleOffeneFehlmengen(InternalFrame internalFrame) throws Throwable ;
}
