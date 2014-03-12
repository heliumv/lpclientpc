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
package com.lp.client.frame.component;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

public class ToolTipFileWriter {

	private static final String FILE = "tooltips";
	private static final String MIME = ".txt";

	private static int savesNeededForBackup = 5;
	private static int savesSinceLastBackup = 0;
	private static int backupIndex = -1;
	private static int maxBackUps = 100;

	private static Map<String, String> toolTipTexte;

	public static void putToolTip(String token, String text) {
		if (toolTipTexte == null) {
			readToolTips();
		}
		if (text == null || text.isEmpty())
			toolTipTexte.remove(token);
		else
			toolTipTexte.put(token, text.replaceAll("[\r\n]", ""));
	}

	public static void removeToolTip(String token) {
		if (toolTipTexte == null) {
			readToolTips();
		}
		toolTipTexte.remove(token);
	}

	public static String getToolTip(String token) {
		if (toolTipTexte == null) {
			readToolTips();
		}
		return toolTipTexte.get(token);
	}

	private static void readToolTips() {
		toolTipTexte = new HashMap<String, String>();
		File file = new File(FILE+MIME).getAbsoluteFile();
		try {
			FileReader fileReader = new FileReader(file);
			BufferedReader reader = new BufferedReader(fileReader);
			while (true) {
				String line = reader.readLine();
				if (line == null)
					break;
				String[] split = line.split(" = ");
				if (split.length != 2)
					continue;
				toolTipTexte.put(split[0].trim(), split[1].trim());
			}
			reader.close();
			fileReader.close();
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
		}
	}
	
	private static String formatFileName(int backupIndex) {
		return FILE + String.format("%02d", backupIndex) + MIME;
	}
	
	private static void backupFile() {
		if(backupIndex == -1) {
			//Niedrigsten nicht verwendeten Index finden
			for(int i = 0; i < maxBackUps; i++) {
				File backupFile = new File(formatFileName(i));
				if(!backupFile.exists()) {
					backupIndex = i;
					System.out.println("backup file " + backupFile.getAbsolutePath()
							+ " not found.\nWill be next backup file!");
					break;
				}
			}
		}
		if(backupIndex == -1 || backupIndex == maxBackUps) {
			backupIndex = 0;
		}
		File file = new File(FILE + MIME);
		
		File backupFile = new File(formatFileName(backupIndex));
		if(backupFile.exists()) {
			System.out.println("Deleting file: " + backupFile.getAbsolutePath());
			backupFile.delete();
		}
		System.out.println("Moving file " + file.getAbsolutePath() + " to " + backupFile.getAbsolutePath());
		file.renameTo(backupFile);
		backupIndex++;
	}

	public static boolean saveToolTips() {
		if (toolTipTexte == null)
			return false;

		savesSinceLastBackup++;
		if(savesSinceLastBackup>savesNeededForBackup) {
			backupFile();
			savesSinceLastBackup = 1;
		}
		
		File file = new File(FILE + MIME);
		Writer writer = null;
		try {
			if (Charset.isSupported("UTF-8")) {
				writer = new OutputStreamWriter(new FileOutputStream(file),
						Charset.forName("UTF-8"));
			} else {
				FileWriter fileWriter = new FileWriter(file);
				writer = new BufferedWriter(fileWriter);
			}
			for (String token : toolTipTexte.keySet()) {
				writer.write(token + " = " + toolTipTexte.get(token) + "\n");
			}
			writer.close();
			System.out.println("Created file " + file.getAbsolutePath());
			return true;
		} catch (FileNotFoundException e) {

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return false;
	}
}
