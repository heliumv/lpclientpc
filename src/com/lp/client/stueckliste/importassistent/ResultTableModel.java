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

import java.util.ArrayList;
import java.util.List;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import com.lp.client.frame.component.WrapperTristateCheckbox;
import com.lp.client.pc.LPMain;
import com.lp.server.stueckliste.service.IStklImportResult;
import com.lp.service.StklImportSpezifikation;

public class ResultTableModel implements TableModel {
	
	private List<IStklImportResult> list;
	private List<String> columnTypes;
	
	private List<TableModelListener> listeners = new ArrayList<TableModelListener>();
	
	private StklImportPage3Ctrl controller;
	/**
	 * Erzeugt das Model f&uuml;r die R&uuml;ckgabedaten der Artikelsuche beim Intelligenten Stkl. Import.
	 * Die drei letzten (rechtesten) Spalten zeigen immer die Menge, die Artikelauswahl und das Soko Update an.
	 * @param controller der Controller
	 * @param list die Liste der vom Server gelieferten <code>StklImportResult</code>s
	 * @param columnTypes die Spalten die angezeigt werden sollen.
	 */
	public ResultTableModel(StklImportPage3Ctrl controller, List<IStklImportResult> list, List<String> columnTypes) {
		if(controller == null) throw new NullPointerException("controller == null");
		if(list == null) throw new NullPointerException("list == null");
		if(columnTypes == null) throw new NullPointerException("columnTypes == null");
		this.controller = controller;
		this.columnTypes = new ArrayList<String>(columnTypes);
		this.columnTypes.remove(StklImportSpezifikation.MENGE);
		while(this.columnTypes.contains(StklImportSpezifikation.UNDEFINED))
			this.columnTypes.remove(StklImportSpezifikation.UNDEFINED);
		this.list = list;
	}

	@Override
	public void addTableModelListener(TableModelListener listener) {
		if(!listeners.contains(listener))
			listeners.add(listener);
	}

	protected int getSokoUpdateCheckBoxIndex() {
		return getMengeIndex() + 2;
	}
	
	public int getArtikelChooserIndex() {
		return getMengeIndex() + 1;
	}
	
	public int getKundenartikelnummerIndex() {
		return columnTypes.indexOf(StklImportSpezifikation.KUNDENARTIKELNUMMER);
	}
	
	protected int getMengeIndex() {
		return columnTypes.size();
	}

	@Override
	public Class<?> getColumnClass(int i) {
		if(i == getArtikelChooserIndex())
			return IStklImportResult.class;
		if(i == getSokoUpdateCheckBoxIndex())
			return Boolean.class;
		return String.class;
	}

	@Override
	/**
	 * Liefert die Anzahl der Spalten der Tabelle
	 * H&auml;ngt davon ab, ob die SokoUpdate Spalte angezeigt werden soll. Existiert
	 * keine Kundenartikelnummer-Spalte oder sind alle Eintr&auml;ge TotalMatches oder
	 * Handartikel (SokoUpdateCheckbox ist in diesem Fall auf <code>DISABLE</code>)
	 * dann ist die letzte (rechte) Spalte jene des ArtikelChoosers.
	 * 
	 * @return Anzahl der Spalten der Tabelle
	 */
	public int getColumnCount() {
		return controller.getSokoUpdateTristateCheckboxStatus() != WrapperTristateCheckbox.DISABLE
				? getSokoUpdateCheckBoxIndex() + 1 : getArtikelChooserIndex() + 1;
	}

	@Override
	public String getColumnName(int i) {
		if(i < columnTypes.size())
			return columnTypes.get(i);
		if(i == getMengeIndex())
			return LPMain.getTextRespectUISPr("lp.menge");
		if(i == getArtikelChooserIndex())
			return LPMain.getTextRespectUISPr("lp.artikel");
		if(i == getSokoUpdateCheckBoxIndex())
			return LPMain.getTextRespectUISPr("stkl.intelligenterstklimport.sokoupdate.spaltenkopf");;
		return "";
	}

	@Override
	public int getRowCount() {
		return list.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		IStklImportResult result = list.get(rowIndex);
		if(result.getValues() == null) return null;
		if(columnTypes.size() > columnIndex)
			return result.getValues().get(columnTypes.get(columnIndex));
		if(columnIndex == getMengeIndex())
			return result.getValues().get(StklImportSpezifikation.MENGE);
		if(columnIndex == getArtikelChooserIndex())
			return result;
		if(columnIndex == getSokoUpdateCheckBoxIndex())
			return list.get(rowIndex).isTotalMatch() ? false : result.getSokoUpdate();
		return null;
	}

	@Override
	/**
	 * Gibt an, ob eine Zelle in der Tabelle editierbar ist.
	 * Dies ist der Fall, wenn es sich um eine Auswahl der Artikel
	 * oder wenn es sich um eine Zelle f&uuml;r die Selektierung des 
	 * Soko Updates handelt, aber nur im Falle, dass das aktuelle Result 
	 * kein TotalMatch ist, f&uuml;r dieses Result kein Handartikel ausgew&auml;hlt
	 * wurde, oder die Kundeartikelnummer nicht vorhanden ist.
	 * 
	 * @param rowIndex, Zeilennummer
	 * @param columnIndex, Spaltennummer
	 * @return true, wenn die Zelle editierbar ist
	 */
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		IStklImportResult artikel = (IStklImportResult) getValueAt(rowIndex, getArtikelChooserIndex());
		String kundeartikelnummer = kundenartikelnummerColumnTypeExists() ? 
				(String) getValueAt(rowIndex, getKundenartikelnummerIndex()) : null;
		
		return columnIndex == getArtikelChooserIndex() 
				|| (columnIndex == getSokoUpdateCheckBoxIndex() 
					&& !(list.get(rowIndex).isTotalMatch() 
						|| artikel.getSelectedArtikelDto() == StklImportPage3Ctrl.HANDARTIKEL
						|| kundeartikelnummer == null || kundeartikelnummer.isEmpty()));
	}

	@Override
	public void removeTableModelListener(TableModelListener listener) {
		listeners.remove(listener);
	}

	@Override
	public void setValueAt(Object value, int rowIndex, int columnIndex) {
		if(columnIndex != getArtikelChooserIndex() && columnIndex != getSokoUpdateCheckBoxIndex()) return;
		if(value  instanceof IStklImportResult) {
			IStklImportResult result = (IStklImportResult) value;
			if(result.getSelectedArtikelDto() == StklImportPage3Ctrl.FLR_LISTE
					|| result.getSelectedArtikelDto() == StklImportPage3Ctrl.ZUVIELE_ARTIKEL_FLR_LISTE) {
				list.get(rowIndex).setSelectedIndex(-1);
				controller.setResultWaitingForArtikelIId(result);
			} else {
				list.get(rowIndex).setSelectedIndex(result.getSelectedIndex());
				controller.tableModelValueChanged();
			}
		} else if (value instanceof Boolean) {
			list.get(rowIndex).setSokoUpdate((Boolean)value);
			controller.tableModelValueChanged();
		}
	}
	
	public IStklImportResult getResultAtRow(int row) {
		return list.get(row);
	}
	
	/**
	 * Gibt an, ob die Kundenartikelnummer-Spalte in der Tabelle vorhanden ist.
	 * 
	 * @return true, wenn die Kundenartikelnummer-Spalte vorhanden ist
	 */
	public boolean kundenartikelnummerColumnTypeExists() {
		return getKundenartikelnummerIndex() < 0 ? false : true;
	}

}
