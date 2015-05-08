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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lp.server.util.fastlanereader.service.query.SortierKriterium;

public class ClientPerspectiveFileIO implements IClientPerspectiveIO {

	public static final String LAYOUT_DIR = "Layout";
	public static final String FONT_DIR = "Font";
	public static final String QUERY_DIR = "Query";
	public static final String SEP = System.getProperty("file.separator");
	
	private static final String FILEEX = ".ser";
	private static final String MODULSSTARTUPLIST = "modulsstartup";
	private static final String WIDTHSFILE = "columnwidth_";
	private static final String SORTINGFILE = "sorting_";
	private static final String FONTFILE = "font";
	private static final String PROPERTIES = "properties";

	private String layoutPath;
	private String fontPath;
	private String queryPath;
	private String propertiesPath;
	
	public ClientPerspectiveFileIO(String path) {
		layoutPath = path + LAYOUT_DIR + SEP;
		queryPath = path + QUERY_DIR + SEP;
		fontPath = path + FONT_DIR + SEP;
		propertiesPath = path;
	}
	
	@Override
	public boolean doSavedSettingsExist() {
		return new File(layoutPath + MODULSSTARTUPLIST + FILEEX).exists();
	}
	
	@Override
	public void persistFramePositionMap(Map<String, FramePositionData> positionMap) throws FileNotFoundException, IOException, ClassNotFoundException {
		for(String token : positionMap.keySet()) {
			saveLayoutObject(FramePositionExternalizer.getLatestExternalizer(positionMap.get(token)), token.trim());
		}
		
	}
	
	@Override
	public void persistStartupModule(List<String> startupModule) throws FileNotFoundException, IOException {
		saveLayoutObject(new ModulStartupListExternalizer(startupModule), MODULSSTARTUPLIST);
	}

	@Override
	public List<String> readStartupModule() throws FileNotFoundException, ClassNotFoundException, IOException {
		ModulStartupListExternalizer msle;
		msle = (ModulStartupListExternalizer)readLayoutObject(MODULSSTARTUPLIST);
		return msle.getStartupModule();
	}

	@Override
	public void resetAllLayout() throws IOException {
		File layoutDir = new File(layoutPath);
		for(File file : layoutDir.listFiles()) {
			file.delete();
		}
		layoutDir.delete();
	}

	private void saveLayoutObject(Object object, String filename)  throws FileNotFoundException, IOException{
		saveObject(object, layoutPath, filename);
	}

	private void saveQueryObject(Object object, String filename)  throws FileNotFoundException, IOException{
		saveObject(object, queryPath, filename);
	}
	
	private void saveObject(Object object, String filepath, String filename) throws FileNotFoundException, IOException  {
		ObjectOutputStream objectOutputStream = null;
		try {
			new File(filepath).mkdirs();
			objectOutputStream = new ObjectOutputStream(new FileOutputStream(filepath + filename + FILEEX));
			
			objectOutputStream.writeObject(object);
			objectOutputStream.flush();
		} finally {
			if(objectOutputStream != null) {
				try {
					objectOutputStream.close();
				} catch (IOException e) {
					System.err.println("Konnte Datei " + filename + " nicht closen!\n" + e.getMessage());
				}
			}
		}
	}
	
	private Object readLayoutObject(String filename) throws FileNotFoundException, IOException, ClassNotFoundException {
		return readObject(layoutPath, filename);
	}

	
	private Object readQueryObject(String filename) throws FileNotFoundException, IOException, ClassNotFoundException {
		return readObject(queryPath, filename);
	}
	
	private Object readObject(String filepath, String filename) throws FileNotFoundException, IOException, ClassNotFoundException {
		ObjectInputStream inputStream = null; 
		try {
			inputStream = new ObjectInputStream(new FileInputStream(filepath + filename + FILEEX));
			return inputStream.readObject();
		} finally {
			try {
				if(inputStream != null)
				inputStream.close();
			} catch (IOException e) {
				System.err.println("Konnte Datei " + filepath + filename + " nicht closen!\n" + e.getMessage());
			}
		}
	}

	@Override
	public FramePositionData readFramePosition(String key) throws FileNotFoundException, ClassNotFoundException, IOException {
		return ((FramePositionExternalizer)readLayoutObject(key.trim())).getFpData();
	}

	@Override
	public List<Integer> readQueryColumnWidth(int usecaseId) throws Exception {
		return Arrays.asList((Integer[])readQueryObject(WIDTHSFILE + usecaseId));
	}

	@Override
	public void persistQueryColumnWidth(int usecaseId, List<Integer> widths)
			throws Exception {
		saveQueryObject(widths.toArray(new Integer[0]), WIDTHSFILE + usecaseId);
	}

	@Override
	public void persistQueryColumnSorting(int usecaseId,
			List<SortierKriterium> kriterien) throws Exception {
		saveQueryObject(kriterien.toArray(new SortierKriterium[0]), SORTINGFILE + usecaseId);
	}

	@Override
	public List<SortierKriterium> readQueryColumnSorting(int usecaseId)
			throws Exception {
		return Arrays.asList((SortierKriterium[])readQueryObject(SORTINGFILE +usecaseId));
	}

	@Override
	public Font readClientFont() throws Exception {
		return (Font) readObject(fontPath, FONTFILE);
	}

	@Override
	public void persistClientFont(Font font) throws Exception {
		saveObject(font, fontPath, FONTFILE);
	}

	@Override
	public void resetClientFont() throws Exception {
		File layoutDir = new File(fontPath);
		for(File file : layoutDir.listFiles()) {
			file.delete();
		}
		layoutDir.delete();
	}

	@Override
	public void persistPropertyMap(HashMap<String, String> properties)
			throws Exception {
		saveObject(properties, propertiesPath, PROPERTIES);
	}

	@SuppressWarnings("unchecked")
	@Override
	public HashMap<String, String> readPropertyMap() throws Exception {
		return (HashMap<String, String>)readObject(propertiesPath, PROPERTIES);
	}
}
