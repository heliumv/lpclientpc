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
package com.lp.client.frame.delegate;

import java.util.ArrayList;
import java.util.List;

import javax.jcr.Node;
import javax.naming.Context;
import javax.naming.InitialContext;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.pc.LPMain;
import com.lp.server.system.jcr.ejb.DokumentbelegartPK;
import com.lp.server.system.jcr.ejb.DokumentgruppierungPK;
import com.lp.server.system.jcr.service.DocumentResult;
import com.lp.server.system.jcr.service.DokumentbelegartDto;
import com.lp.server.system.jcr.service.DokumentgruppierungDto;
import com.lp.server.system.jcr.service.DokumentnichtarchiviertDto;
import com.lp.server.system.jcr.service.JCRDocDto;
import com.lp.server.system.jcr.service.JCRDocFac;
import com.lp.server.system.jcr.service.JCRRepoInfo;
import com.lp.server.system.jcr.service.PrintInfoDto;
import com.lp.server.system.jcr.service.docnode.DocNodeBase;
import com.lp.server.system.jcr.service.docnode.DocNodeLiteral;
import com.lp.server.system.jcr.service.docnode.DocNodeVersion;
import com.lp.server.system.jcr.service.docnode.DocPath;
import com.lp.server.system.jcr.service.docnode.HeliumDocPath;
import com.lp.server.system.service.VersandanhangDto;
import com.lp.server.system.service.VersandauftragDto;

public class JCRDocDelegate extends Delegate {

	private Context context;
	private JCRDocFac jcrDocFac;
	private static Boolean jcrOnline;

	public JCRDocDelegate() throws ExceptionLP {
		try {
			context = new InitialContext();
			jcrDocFac = (JCRDocFac) context
					.lookup("lpserver/JCRDocFacBean/remote");
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}
	

	public boolean isOnline() throws ExceptionLP {
		try {
			if(jcrOnline == null)
				jcrOnline = jcrDocFac.isOnline();
			return jcrOnline;
		} catch (Throwable t) {
			handleThrowable(t);
		}
		return false;
	}

	public Node getNode(String sFullPath) throws ExceptionLP {
		Node node = null;
		try {
			node = jcrDocFac.getNode(sFullPath);
		} catch (Throwable t) {
			handleThrowable(t);
		}
		return node;
	}
	

	public JCRDocDto getJCRDocDtoFromNode(DocPath docPath) {
		JCRDocDto retValue = null;
		try {
			retValue = jcrDocFac.getJCRDocDtoFromNode(docPath);
		} catch (Throwable t) {
			// handleThrowable(t);
			return null;
		}
		return retValue;
	}

	public List<DocNodeBase> getDocNodeChildrenFromNode(DocPath docPath)
			throws ExceptionLP {
		List<DocNodeBase> nodes = null;
		try {
			nodes = jcrDocFac.getDocNodeChildrenFromNode(docPath,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
		return nodes;
	}

	public void setVisibilityOfDocument(String basePath, String versionPath, boolean hidden)
			throws ExceptionLP {
		try {
			jcrDocFac.setVisibilityOfDocument(basePath, versionPath, hidden);
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}
	
	public void addNewDocumentOrNewVersionOfDocument(JCRDocDto jcrDocDto)
			throws ExceptionLP {
		try {
			jcrDocFac.addNewDocumentOrNewVersionOfDocument(jcrDocDto,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public ArrayList<DocNodeVersion> getAllDocumentVersions(JCRDocDto jcrDocDto)
			throws ExceptionLP {
		ArrayList<DocNodeVersion> retVal = null;
		try {
			retVal = jcrDocFac.getAllVersionsSession(jcrDocDto);
		} catch (Throwable t) {
			handleThrowable(t);
		}
		return retVal;
	}

	public DokumentgruppierungDto[] dokumentgruppierungfindbyMandant(
			String mandantCNr) throws ExceptionLP {
		try {
			return jcrDocFac.dokumentgruppierungfindbyMandant(mandantCNr);
		} catch (Throwable t) {
			handleThrowable(t);
		}
		return null;
	}

	public DokumentbelegartDto[] dokumentbelegartfindbyMandant(String mandantCNr)
			throws ExceptionLP {
		try {
			return jcrDocFac.dokumentbelegartfindbyMandant(mandantCNr);
		} catch (Throwable t) {
			handleThrowable(t);
		}
		return null;
	}

	// public boolean checkIfNodeExists(String sFullPath) throws ExceptionLP{
	// try{
	// return jcrDocFac.checkIfNodeExists(sFullPath);
	// } catch(Throwable t){
	// handleThrowable(t);
	// }
	// return false;
	// }

	public JCRRepoInfo checkIfNodeExists(DocPath docPath) throws ExceptionLP {
		try {
			return jcrDocFac.checkIfNodeExists(docPath);
		} catch(Throwable t) {
			handleThrowable(t) ;
		}
		return new JCRRepoInfo() ;
	}

	public DokumentgruppierungDto dokumentgruppierungfindbyPrimaryKey(
			DokumentgruppierungPK dokumentgruppierungPK) throws ExceptionLP {
		try {
			return jcrDocFac
					.dokumentgruppierungfindbyPrimaryKey(dokumentgruppierungPK);
		} catch (Throwable t) {
			handleThrowable(t);
		}
		return null;
	}

	public DokumentbelegartDto dokumentbelegartfindbyPrimaryKey(
			DokumentbelegartPK dokumentbelegartPK) throws ExceptionLP {
		try {
			return jcrDocFac
					.dokumentbelegartfindbyPrimaryKey(dokumentbelegartPK);
		} catch (Throwable t) {
			handleThrowable(t);
		}
		return null;
	}

	public void createDokumentgruppierung(
			DokumentgruppierungDto dokumentgruppierungDto) throws ExceptionLP {
		try {
			jcrDocFac.createDokumentgruppierung(dokumentgruppierungDto);
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public void createDokumentbelegart(DokumentbelegartDto dokumentbelegartDto)
			throws ExceptionLP {
		try {
			jcrDocFac.createDokumentbelegart(dokumentbelegartDto);
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public long getNextVersionNumer(JCRDocDto jcrDocDto) throws ExceptionLP {
		try {
			return jcrDocFac.getNextVersionNumer(jcrDocDto);
		} catch (Throwable t) {
			handleThrowable(t);
		}
		return 0;
	}

	public DokumentnichtarchiviertDto dokumentnichtarchiviertfindbyMandantCReportname(
			String mandantCNr, String CReportname) throws ExceptionLP {
		try {
			return jcrDocFac.dokumentnichtarchiviertfindbyMandantCReportname(
					mandantCNr, CReportname);
		} catch (Throwable t) {
			handleThrowable(t);
		}
		return null;
	}

	public void deaktiviereArchivierung(String mandantCNr, String CReportname)
			throws ExceptionLP {
		try {
			jcrDocFac.deaktiviereArchivierung(mandantCNr, CReportname);
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public void kopiereAlteDokumenteInJCR() throws ExceptionLP {
		try {
			jcrDocFac.kopiereAlteDokumenteInJCR(LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public PrintInfoDto getPathAndPartnerAndTable(Object sKey, Integer idUsecase)
			throws ExceptionLP {
		try {
			return jcrDocFac.getPathAndPartnerAndTable(sKey, idUsecase,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
		return null;
	}

	public JCRDocDto getData(JCRDocDto jcrDocDto) throws ExceptionLP {
		try {
			return jcrDocFac.getData(jcrDocDto);
		} catch (Throwable t) {
			handleThrowable(t);
		}
		return null;
	}

	public void kopiereVersandauftraegeInJCR() throws ExceptionLP {
		try {
			jcrDocFac.kopiereVersandauftraegeInJCR(LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public void removeDokumentbelegart(DokumentbelegartDto dokumentbelegartDto)
			throws ExceptionLP {
		try {
			jcrDocFac.removeDokumentbelegart(dokumentbelegartDto);
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public void removeDokumentgruppierung(
			DokumentgruppierungDto dokumentgruppierungDto) throws ExceptionLP {
		try {
			jcrDocFac.removeDokumentgruppierung(dokumentgruppierungDto);
		} catch (Throwable t) {
			handleThrowable(t);
		}

	}

	public VersandauftragDto getDataForVersandauftragFromJCR(
			VersandauftragDto versandauftragDto) throws ExceptionLP {
		try {
			return jcrDocFac.getDataForVersandauftragFromJCR(versandauftragDto,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
		return versandauftragDto;
	}

	public VersandanhangDto getDataForVersandanhangFromJCR(
			VersandanhangDto versandanhangDto) throws ExceptionLP {
		try {
			return jcrDocFac.getDataForVersandanhangFromJCR(versandanhangDto,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
		return versandanhangDto;
	}

	public ArrayList<DocPath> searchDocNodes(String sToSearch)
			throws ExceptionLP {
		try {
			DocNodeBase mandantNode = new DocNodeLiteral(LPMain.getTheClient()
					.getMandant());
			return jcrDocFac.searchDocNodes(sToSearch,
					new HeliumDocPath().add(mandantNode));
		} catch (Throwable t) {
			handleThrowable(t);
		}
		return null;
	}

	public ArrayList<DocPath> searchDocNodes(String sToSearch,
			DocPath startDocPath) throws ExceptionLP {
		try {
			return jcrDocFac.searchDocNodes(sToSearch, startDocPath);
		} catch (Throwable t) {
			handleThrowable(t);
		}
		return null;
	}

	public DocPath getDocPathFromJCR(JCRDocDto jcrDocDto) throws ExceptionLP {
		try {
			return jcrDocFac.getDocPathFromJCR(jcrDocDto);
		} catch (Throwable t) {
			handleThrowable(t);
		}
		return null;
	}

	public DocumentResult runPflege(String[] args) {
		try {
			return jcrDocFac.runPflege(LPMain.getTheClient(), args);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return null;
	}

	public DocumentResult applyDtoTo(String path, List<String> documents,
			String belegart, Object dto) {
		try {
			return jcrDocFac.applyDtoTo(path, documents, belegart, dto,
					LPMain.getTheClient());
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return null;

	}

	public List<?> getDtoMatches(String path, String belegart, String searchKey) {
		try {
			return jcrDocFac.getDtoMatches(path, belegart, searchKey);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return null;
	}

	public int removeEmptyNodes() {
		try {
			return jcrDocFac.removeEmptyNodes();
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return -1;
	}

	public DocumentResult getAllDocuments() {
		try {
			return jcrDocFac.getAllDocuments();
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return null;
	}
}
