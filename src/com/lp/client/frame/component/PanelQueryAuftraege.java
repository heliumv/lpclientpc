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

import com.lp.client.util.fastlanereader.gui.QueryType;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;

public class PanelQueryAuftraege extends PanelQuery {

	private static final long serialVersionUID = 1L;
	
	/**
	 * Gibt an, ob der Benutzer einen Auftrag in der Auftrags&uuml;bersicht
	 * selektiert hat. Wichtig f&uuml;r die Aktivierung des Tabs Sicht Auftrag
	 */
	boolean auftragSelectedOnTable = false;

	public boolean isAuftragSelectedOnTable() {
		return auftragSelectedOnTable;
	}

	public void setAuftragSelectedOnTable(boolean auftragSelectedOnTable) {
		this.auftragSelectedOnTable = auftragSelectedOnTable;
	}

	public PanelQueryAuftraege(QueryType[] typesI, FilterKriterium[] filtersI,
			int idUsecaseI, String[] aWhichButtonIUseI,
			InternalFrame internalFrameI, String add2TitleI,
			boolean refreshWhenYouAreSelectedI) throws Throwable {
		super(typesI, filtersI, idUsecaseI, aWhichButtonIUseI, internalFrameI,
				add2TitleI, refreshWhenYouAreSelectedI);
		
	}

	@Override
	/**
	 * Liefert die Id der Zeile auf der sich der Cursor befindet.
	 * Falls der Cursor im Table nicht gesetzt ist, so wird null retourniert.
	 * Dies ist wichtig f&uuml;r die &Uuml;berpr&uuml;fung, ob im Auftr&auml;ege Tab
	 * in der Tabelle bereits ein Auftrag selektiert wurde, um den Tab Sicht 
	 * Auftrag zu aktivieren.
	 * 
	 * @return Id der Cursor-Zeile
	 */
	protected Object getIdFromCursorPositionInTable() {
		Object selectedId = null;
		
		if (table.getRowCount() >= 1) {
			int iZeileCursor = table.getSelectionModel()
					.getAnchorSelectionIndex();
			if (iZeileCursor != -1) { 
				selectedId = table.getValueAt(iZeileCursor, 0);
			} 
		}
		
		return selectedId;
	}

	@Override
	protected void setRowSelection(int row) {

		if(isAuftragSelectedOnTable()) {
			super.setRowSelection(row);
		}
	}

}
