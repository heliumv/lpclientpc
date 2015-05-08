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
 package com.lp.client.stueckliste.importassistent;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import net.miginfocom.swing.MigLayout;

import com.lp.client.frame.HelperClient;
import com.lp.client.frame.assistent.view.AssistentPageView;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperRadioButton;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.pc.LPMain;

/**
 * View der ersten Seite des StklImports. Hier werden das File und die
 * Importspezifikation ausgew&auml;hlt.
 * 
 * @author robert
 * 
 */
public class StklImportPage1View extends AssistentPageView implements ActionListener, ListSelectionListener {

	private static final long serialVersionUID = -2881318613335847014L;

	private StklImportPage1Ctrl controller;

	private WrapperButton chooseFile;
	private WrapperButton loescheSpez;
	private WrapperTextField filePath;
	private JList spezList;
	private WrapperRadioButton gespeicherteSpez;
	private WrapperRadioButton neueSpez;
	private WrapperLabel kundeNichtGesetzt;

	public StklImportPage1View(StklImportPage1Ctrl controller, InternalFrame iFrame) {
		super(controller, iFrame);
		this.controller = controller;
	}

	@Override
	public StklImportPage1Ctrl getController() {
		return controller;
	}

	@Override
	protected void initViewImpl() {
		chooseFile = new WrapperButton(LPMain.getTextRespectUISPr("lp.datei"));
		filePath = new WrapperTextField(255);
		spezList = new JList(getController().getImportSpezNames().toArray());
		gespeicherteSpez = new WrapperRadioButton(
				LPMain.getTextRespectUISPr("stkl.intelligenterstklimport.gespeichertespezifikationen"));
		neueSpez = new WrapperRadioButton(
				LPMain.getTextRespectUISPr("stkl.intelligenterstklimport.neuespezifikationen"));
		
		kundeNichtGesetzt = new WrapperLabel(
				LPMain.getTextRespectUISPr("stkl.intelligenterstklimport.kundenichthinterlegt"));
		loescheSpez = new WrapperButton(LPMain.getTextRespectUISPr("lp.loeschen"));
		
		ButtonGroup bg = new ButtonGroup();
		bg.add(gespeicherteSpez);
		bg.add(neueSpez);

		gespeicherteSpez.addActionListener(this);
		neueSpez.addActionListener(this);
		spezList.addListSelectionListener(this);
		loescheSpez.addActionListener(this);
		
		spezList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		
		chooseFile.addActionListener(this);
		filePath.setMandatoryField(true);
		filePath.setEditable(false);
		
		setLayout(new MigLayout("wrap 2, fill", "[30%,fill|70%,fill]"));
		add(chooseFile);
		add(filePath);

		add(neueSpez, "wrap");
		
		add(gespeicherteSpez, "top");
		add(spezList, "pushy, grow, spany 2");
		
		add(loescheSpez, "bottom");
		
		if(!getController().isKundeGesetzt())
			add(kundeNichtGesetzt, "span");
		
	}

	@Override
	public void dataUpdated() {
		if(getController().getImportFile() != null)
			filePath.setText(getController().getImportFile().getAbsolutePath());
		else filePath.removeContent();
		String selectedSpezName = getController().getSelectedImportSpezName();
		
		DefaultListModel model = new DefaultListModel();
		for(String name : getController().getImportSpezNames()) {
			model.addElement(name);
		}
		spezList.setModel(model);
		if(getController().getImportSpezNames().size() == 0) {
			gespeicherteSpez.setEnabled(false);
			neueSpez.setSelected(true);
		} else {
			if(getController().getImportSpezNames() == null || selectedSpezName == null) {
				neueSpez.setSelected(true);
			} else {
				gespeicherteSpez.setSelected(true);
				
				spezList.setSelectedValue(selectedSpezName, true);
			}
		}
		spezList.setEnabled(gespeicherteSpez.isSelected());
		if(gespeicherteSpez.isSelected()) {
			if(spezList.getSelectedIndex() == -1)
				spezList.setSelectedIndex(0);
		} else {
			spezList.removeSelectionInterval(0, spezList.getModel().getSize());;
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == chooseFile) {
			List<File> files = HelperClient.showOpenFileDialog(getController().getImportFile(), this, false, ".csv", null);
			if(files != null && files.size() == 1)
				getController().setImportFile(files.get(0));
		} else if(e.getSource() == gespeicherteSpez || e.getSource() == neueSpez) {
			if(gespeicherteSpez.isSelected()) {
				if(spezList.getSelectedIndex() == -1)
					spezList.setSelectedIndex(0);
				getController().setImportSpezName((String)spezList.getSelectedValue());
			} else {
				spezList.removeSelectionInterval(0, spezList.getModel().getSize());;
				getController().setImportSpezName(null);
			}
		} else if(e.getSource() == loescheSpez) {
			getController().deleteImportSpez(spezList.getSelectedValue().toString());
		}
	}

	@Override
	public String getTitle() {
		return LPMain.getTextRespectUISPr("stkl.intelligenterstklimport");
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		getController().setImportSpezName((String)spezList.getSelectedValue());
		loescheSpez.setEnabled(spezList.getSelectedValue() != null);
		if(spezList.getSelectedValue() == null) {
			neueSpez.setSelected(true);
		}
		spezList.setEnabled(gespeicherteSpez.isSelected());
	}

}
