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
package com.lp.client.frame.component.frameposition;

import java.awt.Font;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lp.server.util.fastlanereader.service.query.SortierKriterium;
/**
 * Dieses Interface stellt die Schnittstelle zwischen der Ein-/Ausgabeschicht
 * und dem <code>ClientPerspecitveManager</code> dar. Das Speichern kann zB. im Filesystem erfolgen,
 * aber auch in er DB.<br>
 * Die Layouts sollten abh&auml;ngig von Mandant und User gespeichert werden.
 * @author robert
 *
 */
/**
 * Dieses Interface stellt die Schnittstelle zwischen der Ein-/Ausgabeschicht
 * und dem <code>ClientPerspecitveManager</code> dar. Das Speichern kann zB. im Filesystem erfolgen,
 * aber auch in er DB.<br>
 * Die Layouts sollten abh&auml;ngig von Mandant und User gespeichert werden.
 * @author robert
 *
 */

public interface IClientPerspectiveIO {

	/**
	 * Speichert die Map, in der die Framedarstellungsinformation enthalten ist.
	 * Der Key der Map, ist die BelegCNr des jeweiligen Frames.
	 * @param positionMap
	 * @throws Exception wenn ein Fehler beim Speichern auftritt.
	 */
	public void persistFramePositionMap(Map<String,FramePositionData> positionMap) throws Exception;
	
	/**
	 * Liest die Framedarstellungseigentschaften f&uuml;r den jeweiligen Key aus.
	 * @param key die BelegartCNr des Moduls
	 * @return Framedarstellungseigenschaften-Objekt
	 * @throws Exception wenn ein Fehler beim Lesen auftritt
	 */
	public FramePositionData readFramePosition(String key) throws Exception;
	
	/**
	 * Speichert die Liste der (beim n&auml;chsten laden des Layouts) zu startenden Module.
	 * Inhalt der String-Liste sind die BelegartCNr's der erw&auml;hnten Module.
	 * @param startupModule
	 * @throws Exception wenn ein Fehler beim Speichern auftritt.
	 */
	public void persistStartupModule(List<String> startupModule) throws Exception;
	
	/**
	 * Liest die Liste der (beim n&auml;chsten laden des Layouts) zu startenden Module.
	 * Inhalt der String-Liste sind die BelegartCNr's der erw&auml;hnten Module.
	 * @return eine Liste von Strings
	 * @throws Exception
	 */
	public List<String> readStartupModule() throws Exception;
	
	/**
	 * L&ouml;scht alle Einstellungen des Layouts.
	 * @throws Exception wenn das l&ouml;schen scheitert.
	 */
	public void resetAllLayout() throws Exception;
	
	/**
	 * Pr&uuml;ft ob ein gespeichertes Layout existiert.
	 * @return true wenn ein Layout existiert.
	 */
	public boolean doSavedSettingsExist();
	
	/**
	 * Liest eine IntegerListe aus, welche die Spaltenbreiten beinhaltet.
	 * @param usecaseId die UsecaseId der Tabelle
	 * @return die Breiten in einer Liste
	 * @throws Exception wenn das lesen scheitert
	 */
	public List<Integer> readQueryColumnWidth(int usecaseId) throws Exception;
	
	/**
	 * Speichert die Spaltenbreiten einer Tabelle.
	 * @param usecaseId die UsecaseId der Tabelle
	 * @param widths die Liste mit den Spaltenbreiten
	 * @throws Exception wenn das speichern scheitert.
	 */
	public void persistQueryColumnWidth(int usecaseId, List<Integer> widths) throws Exception;

	/**
	 * Speichert die Sortierkriterien einer Tabelle.
	 * @param usecaseId die UsecaseId der Tabelle
	 * @param kriterien die Liste mit den SortierKriterien
	 * @throws Exception wenn das speichern scheitert.
	 */
	public void persistQueryColumnSorting(int usecaseId, List<SortierKriterium> kriterien) throws Exception;
	
	/**
	 * Liest die Sortierkriterien einer Tabelle.
	 * @param usecaseId die UsecaseId der Tabelle
	 * @return die Sortierkriterien
	 * @throws Exception wenn das lesen scheitert.
	 */
	public List<SortierKriterium> readQueryColumnSorting(int usecaseId) throws Exception;
	
	public Font readClientFont() throws Exception;

	public void persistClientFont(Font font) throws Exception;
	
	public void resetClientFont() throws Exception;
	
	public void persistPropertyMap(HashMap<String,String> properties) throws Exception;
	
	public HashMap<String,String> readPropertyMap() throws Exception;
}
