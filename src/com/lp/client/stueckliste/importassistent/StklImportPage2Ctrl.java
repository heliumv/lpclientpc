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

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.assistent.AssistentPageController;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.server.stueckliste.service.MontageartDto;
import com.lp.server.stueckliste.service.FertigungsStklImportSpezifikation;
import com.lp.service.StklImportSpezifikation;

public class StklImportPage2Ctrl extends AssistentPageController {
	
	private StklImportModel model;
	private static final int NUMBER_OF_LINES_PREVIEW = 200;
	private List<String> columnTypes = null;
	
	public StklImportPage2Ctrl(StklImportModel model) {
		this.model = model;
	}
	
	@Override
	public boolean isNextAllowed() {
		StklImportSpezifikation spez = model.getSelectedSpezifikation();
		if(spez == null) return false;
		if(!spez.isFixedWidth()) {
			if(spez.getSeparator().isEmpty())
				return false;
		} else {
			if(spez.getWidths().size() == 0) return false;
		}
		if(showMontagearten() && getMontageart() == null) return false;
		
		int defined = 0;
		for(String type : spez.getColumnTypes()) {
			if(!StklImportSpezifikation.UNDEFINED.equals(type))
				defined++;
		}
		if(defined == 0) return false;
		
		return true;
	}

	@Override
	public boolean isPrevAllowed() {
		return true;
	}

	@Override
	public boolean isCancelAllowed() {
		return true;
	}

	@Override
	public void activateByNext() throws Throwable {
		model.getSelectedSpezifikation().setStklIId(model.getStklIId());
		if(!model.isKundeGesetzt()) {
			model.getSelectedSpezifikation().removeKundenartikelnrColumnType();
		}
		if(model.isStuecklisteTypeOf(StklImportSpezifikation.SpezifikationsTyp.FERTIGUNGSSTKL_SPEZ) 
				&& ((FertigungsStklImportSpezifikation) model.getSelectedSpezifikation()).getMontageartIId() == 0
				&& getMontagearten() != null && getMontagearten().length > 0) {
			// wenn noch keine Montageart in der Spezifikation gesetzt,
			// dann die erste aller Montagearten auswaehlen
			((FertigungsStklImportSpezifikation) model.getSelectedSpezifikation())
				.setMontageartIId(getMontagearten()[0].getIId());
		}
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(model.getImportFile()));
			String line;
			List<String> lines = new ArrayList<String>();
			while((line = reader.readLine()) != null) {
				lines.add(line);
			}
			model.setImportLines(lines);
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		updateTableModel();
	}
	
	protected void updateTableModel() {
		List<String> lines = model.getImportLines();
		final StklImportSpezifikation spez = model.getSelectedSpezifikation();

		if(spez.isFixedWidth()) {
			while(spez.getWidths().size() > spez.getColumnTypes().size()) {
				spez.getColumnTypes().add(StklImportSpezifikation.UNDEFINED);
			}
		}
		
		TableModel m = new DefaultTableModel();
		int prevLines = NUMBER_OF_LINES_PREVIEW > lines.size() ? lines.size() : NUMBER_OF_LINES_PREVIEW;
		if(lines.size() > 0) {
			if (!spez.isFixedWidth()) {
				if(spez.getSeparator().isEmpty()) {
					m = new DefaultTableModel(prevLines, 1);
					for(int row = 0; row < prevLines; row++) {
						String line = lines.get(row);
						m.setValueAt(line, row, 0);
					}
				} else {
					List<List<String>> splitedLines = new ArrayList<List<String>>();
					for(String line : lines) {
						List<String> fieldsWithQuoMark = Arrays.asList(line.split(spez.getSeparator() + "(?=([^\"]*\"[^\"]*\")*[^\"]*$)"));
						List<String> fields = new ArrayList<String>();
						for(String field : fieldsWithQuoMark) {
							fields.add(field.replaceAll("\"(?!\")", ""));
						}
						splitedLines.add(fields);
					}
					int maxWidth = Collections.max(splitedLines, new Comparator<List<String>>() {
						@Override
						public int compare(List<String> o1, List<String> o2) {
							return new Integer(o1.size()).compareTo(o2.size());
						}
					}).size();
					
					m = new DefaultTableModel(prevLines, maxWidth);
					if(spez.getColumnTypes().size() < maxWidth) {
						for(int i = spez.getColumnTypes().size(); i < maxWidth; i++) {
							spez.getColumnTypes().add(StklImportSpezifikation.UNDEFINED);
						}
					}
					
					for(int row = 0; row < prevLines; row++) {
						List<String> splits = splitedLines.get(row);
						for(int col = 0; col < splits.size(); col++) {
							m.setValueAt(splits.get(col), row, col);
						}
					}
				}
					
			}
		
		}
		model.setTableModel(m);
		fireDataUpdateEvent();
		fireNavigationUpdateEvent();
	}

	@Override
	public void activateByPrev() {
		fireDataUpdateEvent();
		fireNavigationUpdateEvent();
	}

	@Override
	public boolean nextPageIfPossible() {
		return true;
	}

	@Override
	public boolean prevPageIfPossible() {
		return true;
	}
	
	@Override
	public boolean cancelIfPossible() {
		return true;
	}
	
	public StklImportSpezifikation getImportSpezifikation() {
		return model.getSelectedSpezifikation().getDeepCopy();
	}
	
	public void setSeparatorChar(String separator) {
		model.getSelectedSpezifikation().setSeparator(separator);
		updateTableModel();
	}
	
	public void setFixedWidth(boolean b) {
		StklImportSpezifikation spez = model.getSelectedSpezifikation();
		spez.setFixedWidth(b);
		if(b) {
			spez.setColumnTypes(
					new ArrayList<String>(spez.getColumnTypes().subList(0, spez.getWidths().size())));
		}
		updateTableModel();
	}
	
	public void setFromRow(int fromRow) {
		model.getSelectedSpezifikation().setFromRow(fromRow);
		updateTableModel();
	}
	
	public TableModel getTableModel() {
		return model.getTableModel();
	}
	
	protected void removeColumn(Integer column) {
		List<Integer> widths = model.getSelectedSpezifikation().getWidths();
		List<String> types = model.getSelectedSpezifikation().getColumnTypes();
		int index = widths.indexOf(column);
		if(index >= types.size()-1)
			index = types.size()-1;
		else index++;
		types.remove(index);
		widths.remove(column);
	}
	
	protected void addColumn(Integer column) {
		List<Integer> widths = model.getSelectedSpezifikation().getWidths();
		List<String> types = model.getSelectedSpezifikation().getColumnTypes();
		widths.add(column);
		Collections.sort(widths);
		int index = widths.indexOf(column);
		if(index >= types.size())
			index = types.size();
		else index++;
		types.add(index, StklImportSpezifikation.UNDEFINED);
	}
	
	public void toggleColumnWidth(Integer column) {
		List<Integer> widths = model.getSelectedSpezifikation().getWidths();
		if(widths.contains(column))
			removeColumn(column);
		else
			addColumn(column);
		updateTableModel();
	}
	
	public List<String> getImportLines() {
		return model.getImportLines();
	}
	
	public List<String> getAvailableColumnTypes() throws ExceptionLP, Throwable {
		if(columnTypes == null) {
			columnTypes = model.getSelectedSpezifikation().getAllImportColumnTypes();
		}
		List<String> unusedTypes = new ArrayList<String>(columnTypes);
		for(String usedTypes : getImportSpezifikation().getColumnTypes()) {
			unusedTypes.remove(usedTypes);
		}
		unusedTypes.add(StklImportSpezifikation.UNDEFINED);
		return unusedTypes;
	}
	
	public void setColumnType(int index, String type) {
		if(model.getSelectedSpezifikation().getColumnTypes().size() <= index) return;
		model.getSelectedSpezifikation().getColumnTypes().set(index, type);
		updateTableModel();
	}
	
	public void setMontageart(MontageartDto montageartDto) {
		if(showMontagearten()) {
			((FertigungsStklImportSpezifikation) model.getSelectedSpezifikation()).setMontageartIId(montageartDto.getIId());
			fireNavigationUpdateEvent();
		}
	}
	
	public MontageartDto getMontageart() {
		if(showMontagearten()) {
			int iid = ((FertigungsStklImportSpezifikation) model.getSelectedSpezifikation()).getMontageartIId();
			if(iid == 0) return null;
			try {
				for(MontageartDto m : getMontagearten()) {
					if(m.getIId() == iid) return m;
				}
			} catch (Throwable e) {
				return null;
			}
		}
		return null;
	}
	public MontageartDto[] getMontagearten() throws Throwable {
		if (model.getMontagearten() == null)
			model.setMontagearten(DelegateFactory.getInstance()
					.getStuecklisteDelegate().montageartFindByMandantCNr());
		return model.getMontagearten();
	}
	
	public boolean showMontagearten() {
		if(model.isStuecklisteTypeOf(StklImportSpezifikation.SpezifikationsTyp.FERTIGUNGSSTKL_SPEZ))
			return true;
		
		return false;
	}
}
