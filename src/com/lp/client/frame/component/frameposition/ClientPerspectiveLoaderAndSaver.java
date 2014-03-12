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
package com.lp.client.frame.component.frameposition;

import java.awt.Font;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lp.client.frame.component.InternalFrame;
import com.lp.server.util.fastlanereader.service.query.SortierKriterium;

/**
 * L&auml;dt die ClientPerspectiveSettings und k&uuml;mmert sich um
 * dessen lazy-loading und File/DB-Anbindung
 * @author robert
 *
 */
public class ClientPerspectiveLoaderAndSaver {
	
	private static final String DESKTOP = "Desktop";
	
	private ClientPerspectiveSettings cpSettings;
	private IClientPerspectiveIO clientPerspectiveIO;
	
	public ClientPerspectiveLoaderAndSaver(IClientPerspectiveIO framePositionIO) {
		this.clientPerspectiveIO = framePositionIO;
		cpSettings = new ClientPerspectiveSettings();
	}
	
	public boolean doSavedSettingsExist() {
		return clientPerspectiveIO.doSavedSettingsExist();
	}
	
	public void persistAllSettings() throws Exception {
		clientPerspectiveIO.persistFramePositionMap(cpSettings.getPositionMap());
		clientPerspectiveIO.persistStartupModule(getStartupModule());
	}
	
	public List<String> getStartupModule() throws Exception {
		if(cpSettings.getStartupModule() == null)
			loadStartupModule();
		return cpSettings.getStartupModule();
	}
	
	public void setStartupModule(List<String> startupModule) {
		cpSettings.setStartupModule(startupModule);
	}
	
	public void setDesktopPositionData(FramePositionData fpData) {
		setFramePositionData(DESKTOP, fpData);
	}

	public void setFramePositionData(String key, FramePositionData fpData) {
		cpSettings.getPositionMap().put(key, fpData);
	}
	
	public void saveFramePosition(String key) throws Exception {
		Map<String, FramePositionData> map = new HashMap<String, FramePositionData>();
		map.put(key, cpSettings.getPositionMap().get(key));
		clientPerspectiveIO.persistFramePositionMap(map);
	}
	
	public FramePositionData getDesktopPositionData() throws Exception {
		return getFramePositionData(DESKTOP);
	}

	public void applyFramePosition(InternalFrame iFrame) throws Exception {
		FramePositionData fpData = getFramePositionData(iFrame.getBelegartCNr());
		if(fpData != null) fpData.applyto(iFrame);
	}
	
	public void updateFramePosition(InternalFrame iFrame) {
		setFramePositionData(iFrame.getBelegartCNr(),
				new FramePositionData(iFrame));
	}
	
	public FramePositionData getFramePositionData(String key) throws Exception {
		FramePositionData fpData = cpSettings.getPositionMap().get(key);
		if (fpData == null)
			fpData = clientPerspectiveIO.readFramePosition(key);
		cpSettings.getPositionMap().put(key, fpData);
		return fpData;
	}

	public List<Integer> loadQueryColumnWidths(int usecaseId) throws Exception{
		return clientPerspectiveIO.readQueryColumnWidth(usecaseId);
	}

	public void saveQueryColumnWidths(int usecaseId, List<Integer> widths) throws Exception{
		clientPerspectiveIO.persistQueryColumnWidth(usecaseId, widths);
	}
	
	public List<SortierKriterium> loadQueryColumnSorting(int usecaseId) throws Exception{
		return clientPerspectiveIO.readQueryColumnSorting(usecaseId);
	}
	
	public void saveQueryColumnSorting(int usecaseId, List<SortierKriterium> widths) throws Exception{
		clientPerspectiveIO.persistQueryColumnSorting(usecaseId, widths);
	}
	
	private void loadStartupModule() throws Exception {
		List<String> startupModule = clientPerspectiveIO.readStartupModule();
		if(startupModule == null)
			startupModule = new ArrayList<String>();
		cpSettings.setStartupModule(startupModule);
	}
	
	public void deleteSettings() throws Exception {
		clientPerspectiveIO.resetAllLayout();
	}
	
	public void saveFont(Font font) throws Exception {
		clientPerspectiveIO.persistClientFont(font);
	}
	
	public Font readFont() throws Exception {
		return clientPerspectiveIO.readClientFont();
	}
	
	public void resetFont() throws Exception {
		clientPerspectiveIO.resetClientFont();
	}
}
