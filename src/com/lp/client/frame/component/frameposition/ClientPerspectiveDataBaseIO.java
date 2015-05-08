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

import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.fastlanereader.service.query.SortierKriterium;

public class ClientPerspectiveDataBaseIO implements IClientPerspectiveIO {

//	private TheClientDto theClientDto;
	public ClientPerspectiveDataBaseIO(TheClientDto theClientDto) {
//		this.theClientDto = theClientDto;
	}
	@Override
	public void persistFramePositionMap(
			Map<String, FramePositionData> positionMap) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public FramePositionData readFramePosition(String key) throws Exception {
		return null;
	}

	@Override
	public void persistStartupModule(List<String> startupModule)
			throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public List<String> readStartupModule() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void resetAllLayout() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean doSavedSettingsExist() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<Integer> readQueryColumnWidth(int usecaseId) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void persistQueryColumnWidth(int usecaseId, List<Integer> widths)
			throws Exception {
		// TODO Auto-generated method stub

	}
	@Override
	public void persistQueryColumnSorting(int usecaseId,
			List<SortierKriterium> kriterien) throws Exception {
		// TODO Auto-generated method stub
		
	}
	@Override
	public List<SortierKriterium> readQueryColumnSorting(int usecaseId)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Font readClientFont() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void persistClientFont(Font font) throws Exception {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void resetClientFont() throws Exception {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void persistPropertyMap(HashMap<String, String> properties)
			throws Exception {
		// TODO Auto-generated method stub
		
	}
	@Override
	public HashMap<String, String> readPropertyMap() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
}
