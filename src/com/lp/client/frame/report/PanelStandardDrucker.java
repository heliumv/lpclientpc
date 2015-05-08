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
package com.lp.client.frame.report;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.LinkedHashMap;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.swing.JPanel;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperComboBox;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.pc.LPMain;
import com.lp.util.EJBExceptionLP;

@SuppressWarnings("static-access")
class PanelStandardDrucker extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected JPanel jpaWorkingOn = null;
	private WrapperLabel wlaDrucker = null;
	private WrapperComboBox wcoDrucker = null;
	private LinkedHashMap<String, String> mapPrintServices = null;
	private PrintService printServiceDefault = null;
	private PrintService printServicesAlle[] = null;
	private WrapperButton wbuSpeichern = null;
	private WrapperButton wbuLoeschen = null;
	private WrapperComboBox wcoVariante = null;

	public PanelStandardDrucker() throws Throwable {
		jbInit();
		setDefaults();
	}

	private void jbInit() throws Throwable {
		this.setLayout(new GridBagLayout());
		jpaWorkingOn = new JPanel();
		jpaWorkingOn.setLayout(new GridBagLayout());
		wlaDrucker = new WrapperLabel();
		wcoDrucker = new WrapperComboBox();
		wcoDrucker.setMandatoryField(true);

		wcoVariante = new WrapperComboBox();
		wcoVariante.setMandatoryField(true);
	
		wlaDrucker.setText(LPMain.getInstance().getTextRespectUISPr(
				"label.drucker"));
		wlaDrucker.setMinimumSize(new Dimension(100, Defaults.getInstance()
				.getControlHeight()));
		wlaDrucker.setPreferredSize(new Dimension(100, Defaults.getInstance()
				.getControlHeight()));
		wbuSpeichern = new WrapperButton();
		wbuSpeichern.setMinimumSize(new Dimension(100, Defaults.getInstance()
				.getControlHeight()));
		wbuSpeichern.setPreferredSize(new Dimension(100, Defaults.getInstance()
				.getControlHeight()));
		wbuSpeichern.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.speichern"));
		wbuSpeichern.setToolTipText(LPMain.getInstance().getTextRespectUISPr(
				"lp.speichern.reportkonf.tooltip"));
		wbuLoeschen = new WrapperButton();
		wbuLoeschen.setMinimumSize(new Dimension(100, Defaults.getInstance()
				.getControlHeight()));
		wbuLoeschen.setPreferredSize(new Dimension(100, Defaults.getInstance()
				.getControlHeight()));
		wbuLoeschen.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.loeschen"));
		wbuLoeschen.setToolTipText(LPMain.getInstance().getTextRespectUISPr(
				"lp.loeschen.reportkonf.tooltip"));

	//	wcoVariante
	//			.addActionListener(new PanelStandarddrucker_wcoVariante_actionAdapter(
	//					this));

		this.add(jpaWorkingOn, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						0, 0, 0, 0), 0, 0));
		jpaWorkingOn.add(wlaDrucker, new GridBagConstraints(0, 0, 1, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wcoDrucker, new GridBagConstraints(1, 0, 1, 1, 1.0,
				0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wcoVariante, new GridBagConstraints(2, 0, 1, 1, 0.5,
				0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wbuSpeichern, new GridBagConstraints(3, 0, 1, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wbuLoeschen, new GridBagConstraints(4, 0, 1, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
	}

	public WrapperComboBox getWcoVariante() {
		return wcoVariante;
	}

	private void setDefaults() {
		// Standarddrucker
		printServiceDefault = PrintServiceLookup.lookupDefaultPrintService();
		// Alle verfuegbaren Drucker
		printServicesAlle = PrintServiceLookup.lookupPrintServices(null, null);
		mapPrintServices = new LinkedHashMap<String, String>();
		for (int i = 0; i < printServicesAlle.length; i++) {
			String sName = printServicesAlle[i].getName();
			if (printServiceDefault != null && sName.equals(printServiceDefault.getName())) {
				sName = sName
						+ " ("
						+ LPMain.getInstance().getTextRespectUISPr(
								"lp.drucken.standarddrucker") + ")";
			}
			mapPrintServices.put(printServicesAlle[i].getName(), sName);
		}
		wcoDrucker.setMap(mapPrintServices);
		// Default ist der Standarddrucker (wenn er vorhanden ist)
		if (printServiceDefault != null) {
			wcoDrucker.setKeyOfSelectedItem(printServiceDefault.getName());
		}
	}

	public Integer getVariante() {
		
		Object o=wcoVariante.getKeyOfSelectedItem();
		if(o!=null){
			if(((Integer)wcoVariante.getKeyOfSelectedItem()).intValue()==-999){
				return null;
			} else {
				return (Integer)wcoVariante.getKeyOfSelectedItem();
			}
		} else {
			return null;
		}
		
	}

	public String getSelectedPrinter() {
		return (String) wcoDrucker.getKeyOfSelectedItem();
	}

	public PrintService getSelectedPrintService() throws Exception {
		if (wcoDrucker.getItemCount() > 0) {
			return printServicesAlle[wcoDrucker.getSelectedIndex()];
		} else {
			throw new ExceptionLP(
					EJBExceptionLP.FEHLER_DRUCKEN_KEINE_DRUCKER_INSTALLIERT,
					new Exception());
		}
	}

	/*public void wcoVariante_actionPerformed(ActionEvent e) {

		try {

			if (getVariante() != null) {
				ReportvarianteDto rpvDto = DelegateFactory.getInstance()
						.getDruckerDelegate().reportvarianteFindByPrimaryKey(
								getVariante());
				if (rpvDto.getCDrucker() != null) {
					setSelectedPrinter(rpvDto.getCDrucker());
				}
			} 
		} catch (Throwable ex) {
			ex.printStackTrace();

		}
	}*/

	
	
	public void setSelectedPrinter(String sPrinterName) {
		// Wenn es diesen Drucker auch gibt
		if (mapPrintServices.containsKey(sPrinterName)) {
			wcoDrucker.setKeyOfSelectedItem(sPrinterName);
		}
		// sonst waehle den Standarddrucker
		else {
			if (printServiceDefault != null) {
				wcoDrucker.setKeyOfSelectedItem(printServiceDefault.getName());
			}
		}
	}

	public WrapperButton getWbuSpeichern() {
		return wbuSpeichern;
	}

	public WrapperButton getWbuLoeschen() {
		return wbuLoeschen;
	}
}
/*
class PanelStandarddrucker_wcoVariante_actionAdapter implements ActionListener {
	private PanelStandardDrucker adaptee;

	PanelStandarddrucker_wcoVariante_actionAdapter(PanelStandardDrucker adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		adaptee.wcoVariante_actionPerformed(e);
	}
}*/
