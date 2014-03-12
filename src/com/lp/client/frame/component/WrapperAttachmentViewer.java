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

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JPanel;

import com.lp.client.frame.HelperClient;
import com.lp.client.pc.LPMain;
import com.lp.util.Helper;

@SuppressWarnings("static-access")
/*
 * Diese Klasse &ouml;ffnet Dateien mit ihrer Standardappliktion oder speichert
 * diese.
 */
public class WrapperAttachmentViewer extends PanelBasis {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private WrapperButton wbuShow = new WrapperButton();
	private WrapperComboBox wcbFileChooser = new WrapperComboBox();

	static final public String ACTION_SPECIAL_ANZEIGEN = "ACTION_SPECIAL_ANZEIGEN";

	private JPanel paWorkingOn = new JPanel();

	private File[] fFileList;

	public WrapperAttachmentViewer(InternalFrame internalFrameI,
			String addTitleI) throws Throwable {
		super(internalFrameI, addTitleI);
		jbInit();
		initComponents();
	}

	private void jbInit() {
		this.setLayout(new GridBagLayout());
		paWorkingOn.setLayout(new GridBagLayout());
		wbuShow.setActionCommand(ACTION_SPECIAL_ANZEIGEN);
		wbuShow.addActionListener(this);
		wbuShow.setText(LPMain.getInstance().getTextRespectUISPr("lp.anzeigen"));
		wbuShow.setEnabled(true);
		wcbFileChooser.setEnabled(true);
		this.add(paWorkingOn, new GridBagConstraints(0, 0, 2, 1, 2.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						0, 0, 0, 0), 0, 0));

		paWorkingOn.add(wcbFileChooser, new GridBagConstraints(0, 0, 2, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(0, 0, 0, 0), 150, 0));
		paWorkingOn.add(wbuShow, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(
						0, 0, 0, 0), 100, 0));

	}

	public WrapperAttachmentViewer(InternalFrame internalFrameI,
			String addTitleI, Object keyWhenDetailPanelI) throws Throwable {
		super(internalFrameI, addTitleI, keyWhenDetailPanelI);
	}

	public WrapperAttachmentViewer() {
		super();
	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		if (e.getActionCommand().equals(ACTION_SPECIAL_ANZEIGEN)) {
			if (fFileList != null) {
				try {
					File path = fFileList[wcbFileChooser.getSelectedIndex()];
					// PJ 15451
					HelperClient.desktopOpenEx(path);
					// java.awt.Desktop.getDesktop().open(path);
				} catch (IOException dex) {
					// catch Exception Windows throws if no application is
					// associated
					if (dex.getMessage().startsWith("Failed to open file")) {
						JFileChooser fc = new JFileChooser();
						fc.setDialogType(JFileChooser.SAVE_DIALOG);
						int selectedFile = wcbFileChooser.getSelectedIndex();
						fc.setSelectedFile(fFileList[selectedFile]);
						int returnVal = fc.showSaveDialog(getInternalFrame());
						if (returnVal == JFileChooser.APPROVE_OPTION) {
							File file = fc.getSelectedFile();
							FileOutputStream foStream = new FileOutputStream(
									file);
							foStream.write(Helper
									.getBytesFromFile(fFileList[selectedFile]));
							foStream.close();
						}
					} else {
						// catch exception thrown by mac if no application is
						// associated
						if (dex.getMessage()
								.startsWith(
										"Failed to launch the associated application with the specified file")) {
							JFileChooser fc = new JFileChooser();
							fc.setDialogType(JFileChooser.SAVE_DIALOG);
							int selectedFile = wcbFileChooser
									.getSelectedIndex();
							fc.setSelectedFile(fFileList[selectedFile]);
							int returnVal = fc
									.showSaveDialog(getInternalFrame());
							if (returnVal == JFileChooser.APPROVE_OPTION) {
								File file = fc.getSelectedFile();
								FileOutputStream foStream = new FileOutputStream(
										file);
								foStream.write(Helper
										.getBytesFromFile(fFileList[selectedFile]));
								foStream.close();
							}
						} else {
							throw dex;
						}
					}
				} catch (UnsatisfiedLinkError dex) {
					File path = fFileList[wcbFileChooser.getSelectedIndex()];
					String befehl = "open " + path.getPath();
					Runtime.getRuntime().exec(befehl);
				} catch (NoClassDefFoundError dex) {
					File path = fFileList[wcbFileChooser.getSelectedIndex()];
					String befehl = "open " + path.getPath();
					Runtime.getRuntime().exec(befehl);
				}
			}
		}
	}

	public void setFileList(File[] fileList) {
		if (fileList == null || fileList.length == 0)
			setEnabled(false);
		else
			setEnabled(true);
		fFileList = fileList;
		wcbFileChooser.removeAllItems();
		for (int i = 0; i < fFileList.length; i++) {
			if (fFileList[i] == null)
				continue;
			String toShow = fFileList[i].getName();
			wcbFileChooser.addItem(toShow.substring(0,
					toShow.lastIndexOf(".") - 5));
			fFileList[i].deleteOnExit();
		}
	}

	public boolean isEnabled() {
		return super.isEnabled() && wbuShow.isEnabled()
				&& wcbFileChooser.isEnabled();
	}

	public void setEnabled(boolean b) {
		super.setEnabled(b);
		wbuShow.setEnabled(b);
		wcbFileChooser.setEnabled(b);
	}

}
