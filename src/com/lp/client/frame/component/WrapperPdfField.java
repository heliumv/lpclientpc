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

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.filechooser.FileFilter;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.util.Helper;

@SuppressWarnings("static-access")
/*
 * <p> Diese Klasse kuemmert sich ...</p>
 * 
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 * 
 * <p>Erstellung: Vorname Nachname; dd.mm.06</p>
 * 
 * <p>@author $Author: christian $</p>
 * 
 * @version not attributable Date $Date: 2010/11/05 07:51:33 $
 * 
 * @todo scrollpane auf bild PJ 3416
 * 
 * @todo texte uebersetzen PJ 3416
 */
public class WrapperPdfField extends PanelBasis {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final static String ACTION_SPECIAL_DATEI = "action_special_datei";
	static final public String ACTION_SPECIAL_ANZEIGEN = "ACTION_SPECIAL_ANZEIGEN";
	private WrapperLabel wlaDatei = new WrapperLabel();
	private WrapperButton wbuDatei = new WrapperButton();
	private JButton wbuAnzeigen = new JButton();
	private WrapperTextField fieldToDisplayFileName = null;
	private File sLetzteDatei = null;

	private String pdfExtension = new String(".pdf");
	private JPanel jpaWorkingOn = new JPanel();
	byte[] pdf = null;

	public WrapperButton getButtonDatei() {
		return wbuDatei;
	}

	public WrapperLabel getLabelDatei() {
		return wlaDatei;
	}

	public WrapperPdfField(InternalFrame internalFrame, String addTitel)
			throws Throwable {
		super(internalFrame, addTitel);
		jbInit();
		initComponents();
	}

	public WrapperPdfField(InternalFrame internalFrame, String addTitel,
			WrapperTextField fieldToDisplayFileName) throws Throwable {
		super(internalFrame, addTitel);
		this.fieldToDisplayFileName = fieldToDisplayFileName;
		jbInit();
		initComponents();
	}

	private void jbInit() throws Throwable {
		this.setLayout(new GridBagLayout());
		wlaDatei.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.dateiname"));
		wbuDatei.setText(LPMain.getInstance().getTextRespectUISPr("lp.datei"));
		jpaWorkingOn.setLayout(new GridBagLayout());
		wbuDatei.setMinimumSize(new Dimension(80, Defaults.getInstance()
				.getControlHeight()));
		wbuDatei.setPreferredSize(new Dimension(80, Defaults.getInstance()
				.getControlHeight()));
		wbuDatei.setActionCommand(ACTION_SPECIAL_DATEI);
		wbuDatei.addActionListener(this);
		wbuAnzeigen.setActionCommand(ACTION_SPECIAL_ANZEIGEN);
		wbuAnzeigen.addActionListener(this);
		wbuAnzeigen.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.anzeigen"));

		this.add(jpaWorkingOn, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						0, 0, 0, 0), 0, 0));

		jpaWorkingOn.add(wbuDatei, new GridBagConstraints(0, 1, 1, 1, 1, 0,
				GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(
						2, 2, 2, 2), 0, 0));
		wbuAnzeigen.setEnabled(true);
		wbuDatei.setEnabled(true);

		jpaWorkingOn.add(wbuAnzeigen, new GridBagConstraints(0, 4, 2, 1, 1, 1,
				GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(
						2, 2, 2, 2), 100, 0));

	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		if (e.getActionCommand().equalsIgnoreCase(ACTION_SPECIAL_DATEI)) {
			JFileChooser fc = new JFileChooser();

			if (sLetzteDatei != null) {
				fc.setCurrentDirectory(sLetzteDatei);
			}
			fc.setFileFilter(new FileFilter() {
				public boolean accept(File f) {
					return f.getName().toLowerCase().endsWith(pdfExtension)
							|| f.isDirectory();
				}

				public String getDescription() {
					return "PDF";
				}
			});

			int returnVal = fc.showOpenDialog(null);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = fc.getSelectedFile();
				sLetzteDatei = file;
				pdf = Helper.getBytesFromFile(file);
				double groesseInKB = ((double) file.length()) / ((double) 1024);
				ParametermandantDto parameter = DelegateFactory
						.getInstance()
						.getParameterDelegate()
						.getMandantparameter(
								LPMain.getInstance().getTheClient()
										.getMandant(),
								ParameterFac.KATEGORIE_ALLGEMEIN,
								ParameterFac.PARAMETER_ALLGEMEIN_DOKUMENTE_MAXIMALE_GROESSE);
				if (groesseInKB > (Integer) parameter.getCWertAsObject()) {
					DialogFactory.showModalDialog(LPMain.getInstance()
							.getTextRespectUISPr("lp.error"), LPMain
							.getInstance().getTextRespectUISPr(
									"lp.error.dateizugross"));
				} else {
					// darstellen
					wlaDatei.setToolTipText(file.getName());
					if (fieldToDisplayFileName != null) {
						fieldToDisplayFileName.setText(file.getName());

					}

					wbuDatei.setToolTipText(file.getAbsolutePath());
				}
			} else {
				// keine auswahl
			}
		} else if (e.getSource().equals(wbuAnzeigen)) {
			if (pdf != null) {
				java.io.File f = File.createTempFile("hvd", ".pdf");
				try {

					java.io.FileOutputStream out = new java.io.FileOutputStream(
							f);
					out.write(pdf);
					out.close();
				} catch (FileNotFoundException ex) {
					ex.printStackTrace();
				} catch (IOException ex) {
				}
				// PJ 15451
				HelperClient.desktopOpenEx(f);
				//java.awt.Desktop.getDesktop().open(f);
				f.deleteOnExit();

			}
		}
	}

	public String getDateiname() {
		return wlaDatei.getToolTipText();
	}

	public void setDateiname(String dateiname) {
		wlaDatei.setToolTipText(dateiname);
	}

	public byte[] getPdf() {
		return pdf;
	}

	public void setPdf(byte[] pdf) {
		this.pdf = pdf;

	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wbuDatei;
	}

}
