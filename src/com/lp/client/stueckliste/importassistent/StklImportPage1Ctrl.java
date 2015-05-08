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
 package com.lp.client.stueckliste.importassistent;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.assistent.AssistentPageController;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.angebotstkl.service.AgstklDto;
import com.lp.server.angebotstkl.service.EinkaufsangebotDto;
import com.lp.server.stueckliste.service.StuecklisteDto;
import com.lp.service.StklImportSpezifikation;

public class StklImportPage1Ctrl extends AssistentPageController {
	
	private StklImportModel model;
	
	public StklImportPage1Ctrl(StklImportModel model) {
		this.model = model;	
	}

	@Override
	public boolean isNextAllowed() {
		return model.getImportFile() != null;
	}

	@Override
	public boolean isPrevAllowed() {
		return false;
	}
	
	@Override
	public boolean isCancelAllowed() {
		return true;
	}

	@Override
	public void activateByNext() throws ExceptionLP, Throwable {
		fireDataUpdateEvent();
	}

	@Override
	public void activateByPrev() {
		fireDataUpdateEvent();
	}

	@Override
	public boolean nextPageIfPossible() {
		return true;
	}

	@Override
	public boolean prevPageIfPossible() {
		return false;
	}
	
	@Override
	public boolean cancelIfPossible() {
		return true;
	}
	
	public void setImportFile(File file) {
		model.setImportFile(file);
		fireDataUpdateEvent();
		fireNavigationUpdateEvent();
	}
	
	public File getImportFile() {
		return model.getImportFile();
	}
	
	public List<String> getImportSpezNames() {
		if(model.getImportSpezifikationen() == null)
			try {
				model.setImportSpezifikationen(DelegateFactory.getInstance().getStuecklisteDelegate()
						.stklImportSpezFindAll(model.getStklTyp(), LPMain.getTheClient()));
			} catch (Throwable e) {
				model.setImportSpezifikationen(new HashMap<String, StklImportSpezifikation>());
			}
		Set<String> set  = model.getImportSpezifikationen().keySet();
		return set == null ? new ArrayList<String>() : new ArrayList<String>(set);
	}
	
	public void deleteImportSpez(String name) {
		try {
			StklImportSpezifikation spez = model.getImportSpezifikationen().get(name);
			if(spez != null) {
				DelegateFactory.getInstance().getStuecklisteDelegate().removeStklImportSpez(spez);
				model.setImportSpezifikationen(null);
				fireDataUpdateEvent();
			}
		} catch (Throwable e) {
		}
	}
	
	/**
	 * Setzt den Namen der vorhandenen Spezifikation, die geladen werden soll.
	 * @param name der Name der Spezifikation oder null f&uuml;r neu.
	 */
	public void setImportSpezName(String name) {
		StklImportSpezifikation spez = model.getImportSpezifikationen().get(name);
		model.setSelectedSpezifikation(spez);
	}
	
	public String getSelectedImportSpezName() {
		return model.getSelectedSpezifikation() == null ? null : model.getSelectedSpezifikation().getName();
	}
	
	public boolean isKundeGesetzt() {
		try {
			if(model.isStuecklisteTypeOf(StklImportSpezifikation.SpezifikationsTyp.ANGEBOTSSTKL_SPEZ)) {
				AgstklDto agstklDto = DelegateFactory.getInstance().getAngebotstklDelegate()
						.agstklFindByPrimaryKey(model.getStklIId());
				model.setKundeIsGesetzt(agstklDto.getKundeIId() != null);
			} else if(model.isStuecklisteTypeOf(StklImportSpezifikation.SpezifikationsTyp.EINKAUFSANGEBOTSSTKL_SPEZ)) {
				EinkaufsangebotDto einkaufsAgDto = DelegateFactory.getInstance().getAngebotstklDelegate()
						.einkaufsangebotFindByPrimaryKey(model.getStklIId());
				model.setKundeIsGesetzt(einkaufsAgDto.getKundeIId() != null);
			} else if(model.isStuecklisteTypeOf(StklImportSpezifikation.SpezifikationsTyp.FERTIGUNGSSTKL_SPEZ)) {
				StuecklisteDto stklDto = DelegateFactory.getInstance().getStuecklisteDelegate()
						.stuecklisteFindByPrimaryKey(model.getStklIId());
				model.setKundeIsGesetzt(stklDto.getPartnerIId() != null);
			}
		} catch (Throwable e) {
			e.printStackTrace();
			return false;
		}
		return model.isKundeGesetzt();
	}

}
