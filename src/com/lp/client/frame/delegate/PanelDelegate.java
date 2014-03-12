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
package com.lp.client.frame.delegate;

import javax.naming.Context;
import javax.naming.InitialContext;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.pc.LPMain;
import com.lp.server.system.service.PanelDto;
import com.lp.server.system.service.PanelFac;
import com.lp.server.system.service.PanelbeschreibungDto;
import com.lp.server.system.service.PaneldatenDto;
import com.lp.server.util.report.JasperPrintLP;

@SuppressWarnings("static-access")
public class PanelDelegate extends Delegate {
	private Context context;
	private PanelFac panelFac;

	public PanelDelegate() throws Exception {
		context = new InitialContext();
		panelFac = (PanelFac) context.lookup("lpserver/PanelFacBean/remote");
	}

	public void createPaneldaten(PaneldatenDto[] paneldatenDto)
			throws ExceptionLP {
		try {
			panelFac.createPaneldaten(paneldatenDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public Integer createPanelbeschreibung(
			PanelbeschreibungDto paanelbeschreibungDto,
			boolean bFuegeNeuePositionVorDerSelektiertenEin) throws ExceptionLP {
		try {
			return panelFac.createPanelbeschreibung(paanelbeschreibungDto,
					bFuegeNeuePositionVorDerSelektiertenEin);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public PanelbeschreibungDto[] panelbeschreibungFindByPanelCNrMandantCNr(
			String panelCNr, Integer artgruIId) throws Throwable {
		return panelFac.panelbeschreibungFindByPanelCNrMandantCNr(panelCNr,
				LPMain.getInstance().getTheClient().getMandant(), artgruIId);
	}

	public PanelbeschreibungDto[] panelbeschreibungFindByPanelCNrMandantCNrPartnerklasseIId(
			String panelCNr, Integer partnerklasseIId) throws Throwable {
		return panelFac
				.panelbeschreibungFindByPanelCNrMandantCNrPartnerklasseIId(
						panelCNr, LPMain.getInstance().getTheClient()
								.getMandant(), partnerklasseIId);
	}

	public PaneldatenDto paneldatenFindByPrimaryKey(Integer iId)
			throws Throwable {
		try {
			return panelFac.paneldatenFindByPrimaryKey(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}

	public PanelDto panelFindByPrimaryKey(String cNr) throws Throwable {
		try {
			return panelFac.panelFindByPrimaryKey(cNr);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}

	public boolean panelbeschreibungVorhanden(String cNr) throws Throwable {
		try {
			return panelFac.panelbeschreibungVorhanden(cNr, LPMain
					.getInstance().getTheClient().getMandant());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return false;
		}

	}

	public PanelbeschreibungDto panelbeschreibungFindByPrimaryKey(Integer iId)
			throws Throwable {
		try {
			return panelFac.panelbeschreibungFindByPrimaryKey(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}

	public PaneldatenDto[] paneldatenFindByPanelCNrCKey(String panelCNr,
			String cKey) throws Throwable {
		try {
			return panelFac.paneldatenFindByPanelCNrCKey(panelCNr, cKey);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public PaneldatenDto findByPanelCNrPanelbeschreibungIIdCKey(
			String panelCNr, Integer panelbeschreibungIId, String cKey)
			throws Throwable {
		try {
			return panelFac.findByPanelCNrPanelbeschreibungIIdCKey(panelCNr,
					panelbeschreibungIId, cKey);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}

	public void updatePaneldaten(PaneldatenDto paneldatenDto) throws Throwable {
		try {
			panelFac.updatePaneldaten(paneldatenDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}

	}

	public void removePanelbeschreibung(Integer iId) throws Throwable {

		try {
			panelFac.removePanelbeschreibung(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}

	}

	public JasperPrintLP printPanel(String panelCNr, String report, String cKey)
			throws Throwable {
		try {
			return panelFac.printPanel(panelCNr, report, cKey, LPMain
					.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public void updatePanelbeschreibung(
			PanelbeschreibungDto panelbeschreibungDto) throws Throwable {
		try {
			panelFac.updatePanelbeschreibung(panelbeschreibungDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

}
