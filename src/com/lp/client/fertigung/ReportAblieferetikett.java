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
package com.lp.client.fertigung;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.math.BigDecimal;
import java.util.EventObject;

import javax.swing.JPanel;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.report.PanelReportIfJRDS;
import com.lp.client.frame.report.PanelReportKriterien;
import com.lp.client.pc.LPMain;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.bestellung.service.BestellungReportFac;
import com.lp.server.fertigung.service.FertigungReportFac;
import com.lp.server.fertigung.service.LosDto;
import com.lp.server.fertigung.service.LosablieferungDto;
import com.lp.server.stueckliste.service.StuecklisteDto;
import com.lp.server.system.service.MailtextDto;
import com.lp.server.util.report.JasperPrintLP;

@SuppressWarnings("static-access")
/*
 * <p>Report ReportWepEtikett <p>Copyright Logistik Pur Software GmbH (c)
 * 2004-2008</p> <p>Erstellungsdatum 28.11.07</p> <p> </p>
 * 
 * @author Victor Finder
 * 
 * @version $Revision: 1.3 $
 */
public class ReportAblieferetikett extends PanelBasis implements
		PanelReportIfJRDS {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private WrapperLabel wlaHandmenge = null;
	private WrapperNumberField wnfHandmenge = null;

	private Integer losablieferungIId = null;

	protected JPanel jpaWorkingOn = new JPanel();
	private WrapperLabel wlaExemplare = null;
	protected WrapperNumberField wnfExemplare = null;

	public ReportAblieferetikett(InternalFrame internalFrame,
			Integer losablieferungIId, String sAdd2Title) throws Throwable {
		super(internalFrame, sAdd2Title);
		this.losablieferungIId = losablieferungIId;

		jbInit();
		setDefaults();
	}

	private void jbInit() throws Throwable {

		String[] aWhichButtonIUse = { ACTION_UPDATE, ACTION_SAVE,
				ACTION_DISCARD };
		enableToolsPanelButtons(aWhichButtonIUse);

		jpaWorkingOn.setLayout(new GridBagLayout());
		getInternalFrame().addItemChangedListener(this);
		wlaExemplare = new WrapperLabel();
		wlaExemplare.setText(LPMain.getInstance().getTextRespectUISPr(
				"report.exemplare"));
		wlaExemplare.setMinimumSize(new Dimension(100, Defaults.getInstance()
				.getControlHeight()));
		wlaExemplare.setPreferredSize(new Dimension(100, Defaults.getInstance()
				.getControlHeight()));
		wnfExemplare = new WrapperNumberField();
		wnfExemplare.setMinimumSize(new Dimension(30, Defaults.getInstance()
				.getControlHeight()));
		wnfExemplare.setPreferredSize(new Dimension(30, Defaults.getInstance()
				.getControlHeight()));
		wnfExemplare.setFractionDigits(0);
		wnfExemplare.setMaximumIntegerDigits(4);
		wnfExemplare.setMandatoryField(true);
		wnfExemplare.setMinimumValue(1);
		wnfExemplare.setInteger(1);

		// PJ17954
		if (losablieferungIId != null) {
			LosablieferungDto laDto = DelegateFactory.getInstance()
					.getFertigungDelegate()
					.losablieferungFindByPrimaryKey(losablieferungIId, false);
			LosDto lDto = DelegateFactory.getInstance().getFertigungDelegate()
					.losFindByPrimaryKey(laDto.getLosIId());
			if (lDto.getStuecklisteIId() != null) {
				StuecklisteDto stklDto = DelegateFactory.getInstance()
						.getStuecklisteDelegate()
						.stuecklisteFindByPrimaryKey(lDto.getStuecklisteIId());
				ArtikelDto aDto = DelegateFactory.getInstance()
						.getArtikelDelegate()
						.artikelFindByPrimaryKey(stklDto.getArtikelIId());

				if (aDto.getFVerpackungsmenge() != null
						&& aDto.getFVerpackungsmenge().doubleValue() != 0) {
					
					double d=lDto.getNLosgroesse().doubleValue()/aDto.getFVerpackungsmenge().doubleValue();
					int iExemplare =(int)Math.ceil(d);
					
				
					if (iExemplare > 1) {
						wnfExemplare.setInteger(iExemplare);
					}
				}
			}

		}

		wlaHandmenge = new WrapperLabel();
		wlaHandmenge.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.handmenge"));
		wnfHandmenge = new WrapperNumberField();

		this.setLayout(new GridBagLayout());
		jpaWorkingOn.add(wlaExemplare, new GridBagConstraints(0, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfExemplare, new GridBagConstraints(1, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(new WrapperLabel(), new GridBagConstraints(2, iZeile,
				1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;

		jpaWorkingOn.add(wlaHandmenge, new GridBagConstraints(0, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfHandmenge, new GridBagConstraints(1, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		this.add(jpaWorkingOn, new GridBagConstraints(0, iZeile, 1, 1, 1.0,
				1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));

	}

	private void setDefaults() throws Throwable {
		
	}

	public String getModul() {
		return FertigungReportFac.REPORT_MODUL;
	}

	public String getReportname() {
		return FertigungReportFac.REPORT_ABLIEFERETIKETT;
	}

	public JasperPrintLP getReport(String sDrucktype) throws Throwable {
		return DelegateFactory
				.getInstance()
				.getFertigungDelegate()
				.printAblieferEtikett(losablieferungIId,
						wnfExemplare.getInteger(), wnfHandmenge.getBigDecimal());
	}

	public boolean getBErstelleReportSofort() {
		return true;
	}

	public MailtextDto getMailtextDto() throws Throwable {
		MailtextDto mailtextDto = PanelReportKriterien
				.getDefaultMailtextDto(this);
		return mailtextDto;
	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
	}
}
