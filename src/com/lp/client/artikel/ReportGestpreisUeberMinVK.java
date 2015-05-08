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
package com.lp.client.artikel;

import java.util.*;

import java.awt.*;
import java.awt.event.ActionEvent;

import javax.swing.*;

import com.lp.client.frame.component.*;
import com.lp.client.frame.delegate.*;
import com.lp.client.frame.report.*;
import com.lp.client.pc.*;
import com.lp.server.artikel.service.*;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.system.service.*;
import com.lp.server.util.report.*;

public class ReportGestpreisUeberMinVK extends PanelBasis implements
		PanelReportIfJRDS {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected JPanel jpaWorkingOn = new JPanel();
	private GridBagLayout gridBagLayout2 = new GridBagLayout();
	private GridBagLayout gridBagLayout1 = new GridBagLayout();

	private WrapperSelectField wsfLager = null;
	private WrapperSelectField wsfVkpreisliste = null;

	private WrapperRadioButton wrbPreisVKPreisliste = new WrapperRadioButton();
	private WrapperRadioButton wrbPreisPreisbasis = new WrapperRadioButton();
	private ButtonGroup buttonGroupPreis = new ButtonGroup();
	
	
	
	private WrapperLabel wlaAusgehendVon = new WrapperLabel();
	private WrapperRadioButton wrbAusgehendVonGestpreis = new WrapperRadioButton();
	private WrapperRadioButton wrbAusgehendVonMinVKPreis = new WrapperRadioButton();
	private ButtonGroup buttonGroupAusgehendVon = new ButtonGroup();
	

	private WrapperCheckBox wcbVersteckte = null;
	private WrapperCheckBox wcbMitStuecklisten = null;

	public ReportGestpreisUeberMinVK(InternalFrameArtikel internalFrame,
			String add2Title) throws Throwable {
		super(internalFrame, add2Title);
		LPMain.getTextRespectUISPr("artikel.report.gestpreisueberminvk");

		jbInit();
		initComponents();

	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wsfLager;
	}

	private void jbInit() throws Throwable {
		this.setLayout(gridBagLayout1);

		jpaWorkingOn.setLayout(gridBagLayout2);

		wsfLager = new WrapperSelectField(WrapperSelectField.LAGER,
				getInternalFrame(), true);

		wsfLager.setMandatoryField(true);

		wsfLager.setKey(DelegateFactory.getInstance().getLagerDelegate()
				.getHauptlagerDesMandanten().getIId());

		wsfVkpreisliste = new WrapperSelectField(
				WrapperSelectField.VKPREISLISTE, getInternalFrame(), true);
		wsfVkpreisliste.setEnabled(false);

		buttonGroupPreis.add(wrbPreisPreisbasis);
		buttonGroupPreis.add(wrbPreisVKPreisliste);
		wrbPreisPreisbasis.setText(LPMain
				.getTextRespectUISPr("artikel.label.einzelverkaufspreis"));
		wrbPreisVKPreisliste
				.setText(LPMain
						.getTextRespectUISPr("artikel.handlagerbewegung.verkaufspreis"));

		wcbMitStuecklisten=new WrapperCheckBox(LPMain
				.getTextRespectUISPr("artikel.report.gestpreisueberminvk.mitstuecklisten")); 
		
		wcbMitStuecklisten.setSelected(true);
		
		
		buttonGroupAusgehendVon.add(wrbAusgehendVonGestpreis);
		buttonGroupAusgehendVon.add(wrbAusgehendVonMinVKPreis);
		
		wrbAusgehendVonMinVKPreis.setText(LPMain
				.getTextRespectUISPr("lp.mindestverkaufspreis"));
		wrbAusgehendVonGestpreis.setText(LPMain
				.getTextRespectUISPr("lp.gestehungspreis"));
		
		wrbAusgehendVonMinVKPreis.setSelected(true);
		
		wlaAusgehendVon.setText(LPMain
				.getTextRespectUISPr("artikel.report.ausgehendvon"));
		
		wcbVersteckte = new WrapperCheckBox(LPMain
				.getTextRespectUISPr("lp.versteckte"));

		wrbPreisPreisbasis.addActionListener(this);
		wrbPreisVKPreisliste.addActionListener(this);

		wrbPreisPreisbasis.setSelected(true);

		getInternalFrame().addItemChangedListener(this);
		this.add(jpaWorkingOn, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
				GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0,
						0, 0, 0), 0, 0));

		int iZeile = 0;
		jpaWorkingOn.add(wsfLager.getWrapperTextField(),
				new GridBagConstraints(1, iZeile, 1, 1, 0, 0.0,
						GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
						new Insets(2, 2, 2, 2), 200, 0));
		jpaWorkingOn.add(wsfLager, new GridBagConstraints(0, iZeile, 1, 1, 0,
				0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 100, 0));

		if (DelegateFactory.getInstance().getTheJudgeDelegate().hatRecht(
				RechteFac.RECHT_LP_DARF_VERSTECKTE_SEHEN)) {
			jpaWorkingOn.add(wcbVersteckte, new GridBagConstraints(3, iZeile,
					1, 1, 0, 0.0, GridBagConstraints.WEST,
					GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 200,
					0));
		}

		iZeile++;

		jpaWorkingOn.add(wlaAusgehendVon, new GridBagConstraints(0, iZeile, 1,
				1, 0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		
		
		jpaWorkingOn.add(wrbAusgehendVonGestpreis, new GridBagConstraints(1, iZeile, 1,
				1, 0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		
		jpaWorkingOn.add(wrbAusgehendVonMinVKPreis, new GridBagConstraints(2, iZeile, 1,
				1, 0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		
		jpaWorkingOn.add(wcbMitStuecklisten, new GridBagConstraints(3, iZeile,
				1, 1, 0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 200,
				0));
		
		iZeile++;
		jpaWorkingOn.add(wsfVkpreisliste, new GridBagConstraints(0, iZeile, 1,
				1, 0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 100, 0));
		jpaWorkingOn.add(wsfVkpreisliste.getWrapperTextField(),
				new GridBagConstraints(1, iZeile, 1, 1, 0, 0.0,
						GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
						new Insets(2, 2, 2, 2), 200, 0));
		jpaWorkingOn.add(wrbPreisVKPreisliste, new GridBagConstraints(2,
				iZeile, 1, 1, 0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 200, 0));
		jpaWorkingOn.add(wrbPreisPreisbasis, new GridBagConstraints(3, iZeile,
				1, 1, 0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 200, 0));

	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {

		if (e.getSource().equals(wrbPreisPreisbasis)) {
			wsfVkpreisliste.setKey(null);
			wsfVkpreisliste.setEnabled(false);
			wsfVkpreisliste.setMandatoryField(false);
		} else if (e.getSource().equals(wrbPreisVKPreisliste)) {
			wsfVkpreisliste.setEnabled(true);
			wsfVkpreisliste.setMandatoryField(true);
		}

	}

	public String getModul() {
		return ArtikelFac.REPORT_MODUL;
	}

	public String getReportname() {
		return LagerReportFac.REPORT_GESTPREISUEBERMINVK;
	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;
		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {

		}
	}

	public JasperPrintLP getReport(String sDrucktype) throws Throwable {

		return DelegateFactory.getInstance().getLagerReportDelegate()
				.printGestpreisUnterMinVK(wsfLager.getIKey(),
						wsfVkpreisliste.getIKey(), wcbVersteckte.isSelected(),wrbAusgehendVonMinVKPreis.isSelected(),wcbMitStuecklisten.isSelected());
	}

	public boolean getBErstelleReportSofort() {
		return false;
	}

	public MailtextDto getMailtextDto() throws Throwable {
		MailtextDto mailtextDto = PanelReportKriterien
				.getDefaultMailtextDto(this);
		return mailtextDto;
	}
}
