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

import com.lp.client.frame.assistent.AssistentController;
import com.lp.client.frame.component.InternalFrame;

public class StklImportController extends AssistentController{
	
	private StklImportModel model;
	
	public StklImportController(int stklIId, int stklTyp, InternalFrame iFrame) throws Throwable {
		model = new StklImportModel();
		model.setStklIId(stklIId);
		model.setStklTyp(stklTyp);
		StklImportPage1Ctrl ctrl1 = new StklImportPage1Ctrl(model);
		registerPage(new StklImportPage1View(ctrl1, iFrame));
		StklImportPage2Ctrl ctrl2 = new StklImportPage2Ctrl(model);
		registerPage(new StklImportPage2View(ctrl2, iFrame));
		StklImportPage3Ctrl ctrl3 = new StklImportPage3Ctrl(model);
		registerPage(new StklImportPage3View(ctrl3, iFrame));
		StklImportPage4Ctrl ctrl4 = new StklImportPage4Ctrl(model);
		registerPage(new StklImportPage4View(ctrl4, iFrame));
	}

}
