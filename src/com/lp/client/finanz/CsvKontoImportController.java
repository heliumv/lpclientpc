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
package com.lp.client.finanz;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.server.finanz.service.KontoImporterResult;
import com.lp.util.EJBLineNumberExceptionLP;
import com.lp.util.csv.LPCSVReader;

public class CsvKontoImportController implements ICsvKontoImportController {

	private File importFile ;
	
	public CsvKontoImportController() {
	}

	
	@Override
	public KontoImporterResult importCsv(List<String[]> allLines, boolean checkOnly) {
		try {
			return DelegateFactory.getInstance().getFinanzDelegate().importCsv(allLines, checkOnly) ;
		} catch(ExceptionLP ex) {
			return new KontoImporterResult(new ArrayList<EJBLineNumberExceptionLP>()) ;
		}
	}

	@Override
	public KontoImporterResult importCsvFile(String filename) throws FileNotFoundException, IOException {
		return importCsvFile(new File(filename)) ;
	}
	
	public KontoImporterResult importCsvFile(File file) throws FileNotFoundException, IOException {
		LPCSVReader reader = getDefaultCSVReader(file) ;
		List<String[]> lines = reader.readAll() ;
		return importCsv(lines, false) ;
	}

	@Override
	public void setImportFile(File fileToImport) {
		importFile = fileToImport ;
	}
	
	@Override
	public File getImportFile() {
		return importFile ;
	}
	
	public KontoImporterResult checkImport() throws FileNotFoundException, IOException {
		LPCSVReader reader = getDefaultCSVReader(importFile) ;
		List<String[]> lines = reader.readAll() ;
		return importCsv(lines, true) ;		
	}
	
	public KontoImporterResult doImport() throws FileNotFoundException, IOException {
		return importCsvFile(importFile) ;
	}
	
	protected LPCSVReader getDefaultCSVReader(File file) throws FileNotFoundException {
		return new LPCSVReader(new FileReader(file), '\t', new Character('"')) ;
	}
}
