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
 *******************************************************************************/
package com.lp.client.util;

import java.awt.Component;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JOptionPane;

import com.lp.client.frame.component.frameposition.LocalSettingsPathGenerator;
import com.lp.client.pc.Desktop;
import com.lp.client.pc.LPMain;

public class ChangesShownController {
	public static final String FILENAME_SHOWN_CHANGES = "changes.shown";
	public static final String URL_CHANGELOG = "http://demo.heliumv.at/public/HeliumV/AenderungsProtokoll/";

	public void dialogChangeLogIfNeeded(Component parent) throws IOException {		
		if (!hasChanged()) return;
		if (!wantToViewChanges(parent)) return;
		Desktop.browseURI(URL_CHANGELOG);
		saveChangesViewed();
	}
	
	private File getChangesShownFile() {
		return new File(new LocalSettingsPathGenerator().getDefaultPath(), FILENAME_SHOWN_CHANGES);
	}
	
	/**
	 * Hat sich das &Auml;nderungsprotokoll seit der letzten Best&auml;tigung ge&auml;ndert?
	 * 
	 * @return true wenn eine &Auml;nderung seit der letzten Installation/Update auftrat und
	 * diese noch nicht best&auml;tigt wurde
	 * 
	 * @throws IOException
	 */
	public boolean hasChanged() throws IOException {
		File shownFile = getChangesShownFile();
		if(!shownFile.exists()) return true;

		BufferedReader br = null;
		try {
			FileReader reader = new FileReader(shownFile);
			br = new BufferedReader(reader);
			if(!br.ready()) return true;
			String changelogBuildNr = br.readLine();
			// wenn die BuildNr noch gleich ist, hat sich nichts geaendert
			return !changelogBuildNr.trim().equals(LPMain.getVersionHVBuildnr());
		} finally {
			if(br != null) br.close();
		}
	}

	/**
	 * Das &Auml;nderungsprotokoll als gelesen markieren </br>
	 * <p>Der &uuml;bliche Vorgang ist der Aufruf von {@link #dialogChangeLogIfNeeded(Component)}</p>
	 * 
	 * @throws IOException
	 */
	public void saveChangesViewed() throws IOException {
		File shownFile = getChangesShownFile();
		if(!shownFile.exists()) {
			shownFile.getParentFile().mkdirs();
			shownFile.createNewFile();
		}
		FileWriter writer = new FileWriter(shownFile);
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(writer);
			// BuildNr reinschreiben, da der Installer
			// nach einem Update im user.home Verzeichnis
			// die changes.shown nicht loescht und
			// die blosze Existenz des Files nichts 
			// darueber aussagt, ob es ein Update gegeben
			// hat.
			bw.write(LPMain.getVersionHVBuildnr());
			bw.flush();
		} finally {
			if(bw != null) bw.close();
		}
		
	}
	
	private boolean wantToViewChanges(Component parent) {
		return JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(
				parent, LPMain.getTextRespectUISPr("lp.desktop.aenderungslisteansehen")
				, "", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
	}
}
