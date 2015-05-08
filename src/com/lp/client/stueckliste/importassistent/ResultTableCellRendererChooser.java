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

import com.lp.client.pc.LPMain;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikelFac;
import com.lp.server.stueckliste.service.IStklImportResult;
import com.lp.server.system.service.IntelligenterStklImportFac;

public class ResultTableCellRendererChooser extends ResultTableCellRenderer {
	
	private static final long serialVersionUID = 3515105436033557610L;
	
	@Override
	protected String getValue(Object value) {
		if(value instanceof IStklImportResult) {
			IStklImportResult result = (IStklImportResult) value;
			if(result.getSelectedIndex() == null || result.getSelectedIndex() == -1) {
				return null;
			} else {
				ArtikelDto dto = result.getFoundItems().get(result.getSelectedIndex());
//				if(dto == StklImportPage3Ctrl.NEU_LADEN) return null;
				if(dto == StklImportPage3Ctrl.HANDARTIKEL) return ArtikelFac.ARTIKELART_HANDARTIKEL;
				if(dto == StklImportPage3Ctrl.FLR_LISTE) return LPMain.getTextRespectUISPr("title.artikelauswahlliste");
				if(dto == StklImportPage3Ctrl.ZUVIELE_ARTIKEL_FLR_LISTE) return LPMain.getTextRespectUISPr("stkl.intelligenterstklimport.zuvieletreffer");
				return dto.getCNr() + ", " + dto.getArtikelsprDto().getCBez();
			}
		} else {
			return value == null ? null : value.toString();
		}
	}
}
