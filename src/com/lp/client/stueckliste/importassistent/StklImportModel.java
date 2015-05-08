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
import java.util.List;
import java.util.Map;

import javax.swing.table.TableModel;

import com.lp.server.angebotstkl.service.AgstklImportSpezifikation;
import com.lp.server.angebotstkl.service.EinkaufsagstklImportSpezifikation;
import com.lp.server.stueckliste.service.IStklImportResult;
import com.lp.server.stueckliste.service.MontageartDto;
import com.lp.server.stueckliste.service.FertigungsStklImportSpezifikation;
import com.lp.service.StklImportSpezifikation;

public class StklImportModel {
	
	private File importFile;
	
	private Map<String, StklImportSpezifikation> importSpezifikationen;
	private StklImportSpezifikation selectedSpezifikation;
	private TableModel tableModel;
	private List<String> importLines;
	private List<IStklImportResult> results;
	private int stklIId;
	private Integer anzahlImportiertePositionen;
	private MontageartDto[] montagearten;
	private String savingName = null;
	private boolean save = true;
	private int stklTyp;
	
	private Boolean kundeIsGesetzt = false;
	private Boolean updateArtikel = false;

	/**
	 * 
	 * @return die Datei, die Importiert werden soll, kann null sein
	 */
	public File getImportFile() {
		return importFile;
	}

	/**
	 * setzt die Datei die Importiert werden soll
	 * @param importFile
	 */
	public void setImportFile(File importFile) {
		this.importFile = importFile;
	}
	
	/**
	 * Gibt die Map der ausw&auml;hlbaren gespeicherten
	 * Import-Spezifikationen zur&uuml;ck.
	 * Der Key ist der Name der Spezifikation.
	 * @return eine Map mit Importspezifikationen, kann null sein
	 */
	public Map<String, StklImportSpezifikation> getImportSpezifikationen() {
		return importSpezifikationen;
	}
	
	/**
	 * Setzt die auf dem Server gespeicherten Import-Spezifikationen.
	 * Der Key ist der Name der Spezifikation.
	 * @param importSpezifikationen eine Liste mit Importspezifikationen
	 */
	public void setImportSpezifikationen(
			Map<String, StklImportSpezifikation> importSpezifikationen) {
		this.importSpezifikationen = importSpezifikationen;
	}

	/**
	 * 
	 * @return die ausgew&auuml;hlte Import-Spezifikation.
	 */
	public StklImportSpezifikation getSelectedSpezifikation() {
		if(selectedSpezifikation == null) {
			if(isStuecklisteTypeOf(StklImportSpezifikation.SpezifikationsTyp.FERTIGUNGSSTKL_SPEZ)) {
				selectedSpezifikation = new FertigungsStklImportSpezifikation();
			} else if(isStuecklisteTypeOf(StklImportSpezifikation.SpezifikationsTyp.ANGEBOTSSTKL_SPEZ)) {
				selectedSpezifikation = new AgstklImportSpezifikation();
			} else if(isStuecklisteTypeOf(StklImportSpezifikation.SpezifikationsTyp.EINKAUFSANGEBOTSSTKL_SPEZ)) {
				selectedSpezifikation = new EinkaufsagstklImportSpezifikation();
			}
		}
		return selectedSpezifikation;
	}

	/**
	 * setzt die ausgew&auml;hlte Import-Spezifikation
	 * @param selectedSpezifikation die Import-Spezifikation
	 */
	public void setSelectedSpezifikation(StklImportSpezifikation selectedSpezifikation) {
		this.selectedSpezifikation = selectedSpezifikation;
	}

	/**
	 * 
	 * @param typ St&uuml;cklistentyp
	 * @return true, wenn die aktuelle St&uuml;cklisten dem &uuml;bergebenen Typ entspricht
	 */
	public boolean isStuecklisteTypeOf(int typ) {
		if(typ == getStklTyp())
			return true;
		
		return false;
	}
	
	/**
	 * Setzt das TableModel welches die Artikelzuordnung handlet.
	 * @return das TableModel, kann null sein
	 * @see ResultTableModel
	 */
	public TableModel getTableModel() {
		return tableModel;
	}

	/**
	 * Holt das TableModel der Tabelle auf der Artikelzuornungsseite.
	 * @param tableModel das TableModel, kann auch null sein
	 */
	public void setTableModel(TableModel tableModel) {
		this.tableModel = tableModel;
	}

	/**
	 * Gibt alle Zeilen der Import-Datei zur&uuml;ck,
	 * auch jene, welche als Header gekennzeichnet und
	 * beim Import ausgelassen werden.
	 * @return alle Zeilen der Importdatei als List, kann null sein
	 */
	public List<String> getImportLines() {
		return importLines;
	}

	/**
	 * Setzt die Zeilen des Importfiles, 
	 * auch jene, welche als Header gekennzeichnet und
	 * beim Import ausgelassen werden.
	 * @param importLines Eine Liste der einzelnen Zeilen
	 */
	public void setImportLines(List<String> importLines) {
		this.importLines = importLines;
	}

	/**
	 * die vom Server ermittelten Ergebnisse.
	 * @return eine Liste von {@link IStklImportResult}. Kann null sein
	 */
	public List<IStklImportResult> getResults() {
		return results;
	}

	/**
	 * setzt die Suchergebnisse des Servers.
	 * @param results
	 */
	public void setResults(List<IStklImportResult> results) {
		this.results = results;
	}

	/**
	 * 
	 * @return die IId der St&uumlckliste, in die importiert wird.
	 */
	public int getStklIId() {
		return stklIId;
	}

	/**
	 * setzt die IId der St&uuml;ckliste, in die importiert wird
	 * @param stklIId
	 */
	public void setStklIId(int stklIId) {
		this.stklIId = stklIId;
	}

	/**
	 * Wie viele Positionen wurden beim Import wirklich angelegt?
	 * @return die Anzahl der Positionen oder null, wenn ein Fehler aufgetreten ist.
	 */
	public Integer getAnzahlImportiertePositionen() {
		return anzahlImportiertePositionen;
	}

	public void setAnzahlImportiertePositionen(Integer anzahlImportiertePositionen) {
		this.anzahlImportiertePositionen = anzahlImportiertePositionen;
	}

	public MontageartDto[] getMontagearten() {
		return montagearten;
	}

	public void setMontagearten(MontageartDto[] montagearten) {
		this.montagearten = montagearten;
	}

	public String getSavingName() {
		return savingName;
	}

	public void setSavingName(String savingName) {
		this.savingName = savingName;
	}

	public boolean isSave() {
		return save;
	}

	public void setSave(boolean save) {
		this.save = save;
	}

	public int getStklTyp() {
		return stklTyp;
	}

	public void setStklTyp(int stklTyp) {
		this.stklTyp = stklTyp;
	}

	public Boolean isKundeGesetzt() {
		return kundeIsGesetzt;
	}

	public void setKundeIsGesetzt(Boolean kundeIsGesetzt) {
		this.kundeIsGesetzt = kundeIsGesetzt;
	}

	public Boolean getUpdateArtikel() {
		return updateArtikel;
	}

	public void setUpdateArtikel(Boolean updateArtikel) {
		this.updateArtikel = updateArtikel;
	}
	
}
