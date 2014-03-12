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
package com.lp.client.util;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.system.jcr.service.JCRDocDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.util.Helper;

public class JCRFileUploader {

	private static Map<String, FileToJCR> fileToJCR = new HashMap<String, FileToJCR>() {
		private static final long serialVersionUID = -2834908572406505278L;

		{
			put(EmailToJCR.MIME, new EmailToJCR());
			put(OutlookToJCR.MIME, new OutlookToJCR());
		}
	};

	public static void uploadFiles(List<File> files, JCRDocDto vorlage)
			throws ExceptionLP, Throwable {
		for (File file : files) {
			if (file.isDirectory())
				continue;
			if (checkFileSize(file)) {
				List<JCRDocDto> jcrs = createJCRDoc(file, vorlage);
				for (JCRDocDto jcr : jcrs) {
					DelegateFactory.getInstance().getJCRDocDelegate()
							.addNewDocumentOrNewVersionOfDocument(jcr);
				}
			}
		}
	}

	/**
	 * Pr&uuml;ft die gr&ouml;&szlig;e der Datei gegen die Berechtigung.
	 * 
	 * @param file
	 * @return true wenn gespeichert werden darf, sonst false
	 * @throws ExceptionLP
	 * @throws Throwable
	 */
	private static boolean checkFileSize(File file) throws ExceptionLP,
			Throwable {

		ParametermandantDto param = DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getMandantparameter(
						LPMain.getTheClient().getMandant(),
						ParameterFac.KATEGORIE_ALLGEMEIN,
						ParameterFac.PARAMETER_ALLGEMEIN_DOKUMENTE_MAXIMALE_GROESSE);
		Integer lFileSize = (Integer) param.getCWertAsObject();
		// Umrechnung in Byte
		Integer lFileSizeByte = lFileSize * 1024;
		boolean bSpeichern = true;
		if (file.length() > lFileSizeByte) {
			boolean bHatRechtImmerZuSpeichern = DelegateFactory
					.getInstance()
					.getTheJudgeDelegate()
					.hatRecht(
							RechteFac.RECHT_DOKUMENTE_DARF_DOKUMENTE_GROESSER_ALS_MAX_ZULAESSIG_SPEICHERN);
			if (bHatRechtImmerZuSpeichern) {
				if (!DialogFactory
						.showModalJaNeinDialog(
								null,
								LPMain.getTextRespectUISPr("lp.dokumente.datei.zugros.trotzdem.speichern")))
					bSpeichern = false;
			} else {
				bSpeichern = false;
				DialogFactory
						.showModalDialog(
								LPMain.getTextRespectUISPr("lp.hinweis"),
								LPMain.getTextRespectUISPr("lp.error.dateizugross")
										+ "\n "
										+ LPMain.getTextRespectUISPr("lp.datei.maxgroesse")
										+ ": "
										+ lFileSize
										+ "Kb "
										+ LPMain.getTextRespectUISPr("lp.datei.dateigroesse")
										+ ": " + (file.length() / 1024) + "Kb");
			}
		}
		if (bSpeichern && Helper.getMime(file.getName()).isEmpty()) {
			bSpeichern = DialogFactory
					.showModalJaNeinDialog(
							null,
							LPMain.getTextRespectUISPr("lp.dokumente.keinedateiendung"));
		}
		return bSpeichern;
	}

	private static FileToJCR getFileToJCRForMime(String mime) {
		FileToJCR ftj = fileToJCR.get(mime);
		if (ftj == null) {
			return new DefaultFileToJCR();
		}
		return ftj;
	}

	private static List<JCRDocDto> createJCRDoc(File file, JCRDocDto vorlage)
			throws IOException {
		return getFileToJCRForMime(Helper.getMime(file.getName())).createJCR(file, vorlage);
	}
}
