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
package com.lp.client.frame.component;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DropMode;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.TransferHandler;
import javax.swing.TransferHandler.TransferSupport;

import com.lp.client.pc.SystemProperties;

import de.wim.outldd.OutlookDD;

public class DragAndDropTarget extends JTextArea {

	private List<DropListener> dropListeners = new ArrayList<DropListener>();
	private boolean supportFiles = false;

	private JLabel centerText;

	private static final DataFlavor FILE_FLAVOR = DataFlavor.javaFileListFlavor;
	// private static final DataFlavor MAC_URL_FLAVOR = new
	// DataFlavor("application/x-java-url; class=java.net.URL; charset=Unicode");

	private static final long serialVersionUID = -3325565552616637854L;

	public DragAndDropTarget() {
		this("");
	}

	/**
	 * 
	 * @param text
	 *            der Text der auf der Komponente dargestellt werden soll.
	 */
	public DragAndDropTarget(String text) {
		if(SystemProperties.isWinOs())
			OutlookDD.init(OutlookDD.MODE_TEMP_FILES);
		initCenterText();
		setCenterText(text);
		setFocusable(false);
		setEditable(false);
		setEnabled(false);
		setDragEnabled(false);
		setDropMode(DropMode.INSERT);
		setTransferHandler(new TransferHandler(null) {
			private static final long serialVersionUID = -1325964636851599066L;

			
			@Override
			public boolean canImport(TransferSupport support) {
				if (!support.isDrop())
					return false;

				boolean copySupported = (COPY & support.getSourceDropActions()) == COPY;
				if (!copySupported) {
					return false;
				}
				support.setDropAction(COPY);

				if (isSupportFiles()
						&& support.isDataFlavorSupported(FILE_FLAVOR))
					return true;
				return false;
			}

			@Override
			public boolean importData(TransferSupport support) {
				if (!canImport(support))
					return false;

				List<File> files = null;
				if (isSupportFiles()) {
					files = tryImportFiles(support);
				}

				if (files != null) {
					fireFileDropEvent(files);
					return true;
				}

				return false;
			}
		});
	}

	@SuppressWarnings("unchecked")
	private List<File> tryImportFiles(TransferSupport support) {
		List<File> files = null;
		if (support.isDataFlavorSupported(FILE_FLAVOR)) {
			try {
				files = (List<File>) support.getTransferable().getTransferData(
						FILE_FLAVOR);
			} catch (UnsupportedFlavorException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return files;
	}

	private void fireFileDropEvent(final List<File> files) {
		for (DropListener l : dropListeners) {
			l.filesDropped(this, files);
		}
	}

	public void addDropListener(DropListener listener) {
		dropListeners.add(listener);
	}

	public void removeDropListener(DropListener listener) {
		dropListeners.remove(listener);
	}

	@Override
	protected void finalize() throws Throwable {
		dropListeners.clear();
		super.finalize();
	}

	// Fuer transparenten Background
	// @Override
	// protected void paintComponent(Graphics g) {
	// g.setColor(getBackground());
	// g.fillRect(0, 0, getWidth(), getHeight());
	// super.paintComponent(g);
	// }

	public boolean isSupportFiles() {
		return supportFiles;
	}

	public void setSupportFiles(boolean supportFiles) {
		this.supportFiles = supportFiles;
	}

	public void setCenterText(String text) {
		centerText.setText(text);
	}

	public String getCenterText() {
		return centerText.getText();
	}

	private void initCenterText() {
		setLayout(new GridBagLayout());
		centerText = new JLabel();
		centerText.setHorizontalAlignment(JLabel.CENTER);
		add(centerText, new GridBagConstraints(1, 1, 1, 1, 1.0, 1.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						0, 0, 0, 0), 0, 0));
	}
}
