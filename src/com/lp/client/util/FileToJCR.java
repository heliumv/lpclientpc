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
import java.util.List;

import com.lp.server.system.jcr.service.JCRDocDto;
import com.lp.server.system.jcr.service.docnode.DocNodeBase;
import com.lp.server.system.jcr.service.docnode.DocNodeFile;
import com.lp.server.system.jcr.service.docnode.DocPath;
import com.lp.util.Helper;

public abstract class FileToJCR {
	public List<JCRDocDto> createJCR(File file, JCRDocDto vorlage) throws IOException {
		JCRDocDto jcr = new JCRDocDto();
		String name = file.getName();
		jcr.setsName(Helper.getName(name));
		jcr.setsMIME(Helper.getMime(name));
		jcr.setbVersteckt(vorlage.getbVersteckt());
		jcr.setsGruppierung(vorlage.getsGruppierung());
		jcr.setsFilename(file.getName());
		jcr.setsSchlagworte(vorlage.getsSchlagworte());
		jcr.setsBelegart(vorlage.getsBelegart());
		
		List<JCRDocDto> modified = createJCRImpl(file, jcr);
		if(modified == null) return null;
		
		for(JCRDocDto mod : modified) {

			if(mod.getsBelegart() == null)
				mod.setsBelegart(vorlage.getsBelegart());
			if(mod.getsGruppierung() == null) 
				mod.setsGruppierung(vorlage.getsGruppierung());
			if(mod.getsName() == null)
				mod.setsName(jcr.getsName());
			if(mod.getsMIME() == null)
				mod.setsMIME("");
			if(mod.getsFilename()==null)
				mod.setsFilename(mod.getsName()+mod.getsMIME());
			if(mod.getsSchlagworte() == null) {
				mod.setsSchlagworte("");
			}
			mod.setlPartner(vorlage.getlPartner());
			mod.setsBelegnummer(vorlage.getsBelegnummer());
			mod.setlAnleger(vorlage.getlAnleger());
			mod.setsRow(vorlage.getsRow());
			mod.setsTable(vorlage.getsTable());
			mod.setlSicherheitsstufe(vorlage.getlSicherheitsstufe());
		
			DocPath path = vorlage.getDocPath().getDeepCopy();
			path.asDocNodeList().remove(path.size()-1);
			if(mod.getDocPath() != null) {
				mod.setDocPath(path.add(mod.getDocPath().asDocNodeList()));
			} else {
				mod.setDocPath(path);
			}
			if(mod.getDocPath().getLastDocNode().getNodeType() != DocNodeBase.FILE &&
					mod.getDocPath().getLastDocNode().getNodeType() != DocNodeBase.MAIL) {
				mod.getDocPath().add(new DocNodeFile(mod.getsName()));
			}
		}
		return modified;
	}
	
	/**
	 * Hier werden die Informationen ins JCRDocDto geschrieben.
	 * Man kann &uuml;berschreiben: Name, MIME, Filename, Schlagworte, Belegart, Gruppierung, Versteckt.
	 * Data sollte gesetzt werden, da sonst kein Inhalt in der Datei landet.
	 * Der optional setzbare DocPath muss ein relativer Pfad sein. Ist der letzte DocNode kein DocNodeFile
	 * und kein DocNodeMail, wird automatisch ein DocNodeFile hinzugef&uuml;gt.
	 * Alle anderen Eigenschaften werden nicht ber&uuml;cksichtigt und von der aufrufenden Methode gesetzt.
	 * @param file die Datei, die in die Dokumentenablage wandern soll.
	 * @param jcr ein bereits mit den Defaultwerten vorbesetztes JCR.
	 * @return
	 * @throws IOException 
	 */
	protected abstract List<JCRDocDto> createJCRImpl(File file, JCRDocDto jcr) throws IOException;
}
