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

import javax.swing.ButtonGroup;
import javax.swing.JList;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.ListSelectionModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import net.miginfocom.swing.MigLayout;

import com.lp.client.frame.assistent.view.AssistentPageView;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperRadioButton;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.pc.LPMain;

public class StklImportPage4View extends AssistentPageView {
	
	private static final long serialVersionUID = -561649063399967056L;
	
	private StklImportPage4Ctrl controller;
	private JProgressBar progressBar;
	private WrapperTextField gesamtePositionen;
	private WrapperTextField importiertePositionen;
	private JList spezList;
	private WrapperRadioButton nichtSpeichern;
	private WrapperRadioButton speichernUnter;
	private WrapperTextField spezName;

	public StklImportPage4View(StklImportPage4Ctrl controller,
			InternalFrame iFrame) {
		super(controller, iFrame);
		this.controller = controller;
	}

	@Override
	public void dataUpdated() {
		if(getController().getImportThrowable() != null)
			getInternalFrame().handleException(getController().getImportThrowable(), false);
		progressBar.setVisible(getController().isImporting());
		String gesamt = getController().getAnzahlGesamtePositionen() == null ?
				"?" : getController().getAnzahlGesamtePositionen().toString();
		String importiert = getController().getAnzahlImportiertePositionen() == null ?
				"?" : getController().getAnzahlImportiertePositionen().toString();
		gesamtePositionen.setText(gesamt);
		importiertePositionen.setText(importiert);
		
		boolean saving = getController().isSaving();
		boolean importing = getController().isImporting();
		spezName.setEditable(saving && !importing);
		spezList.setEnabled(saving && !importing);
		speichernUnter.setEnabled(!importing);
		nichtSpeichern.setEnabled(!importing);
		speichernUnter.setSelected(getController().isSaving());
		if(spezName.getText() == null || !spezName.getText().equals(getController().getSpezName())) {
			spezName.setText(getController().getSpezName());
		}
		if(getController().getImportSpezNames().contains(getController().getSpezName()))
			spezList.setSelectedValue(getController().getSpezName(), true);
		else
			spezList.removeSelectionInterval(0, spezList.getModel().getSize());
		
	}

	@Override
	public StklImportPage4Ctrl getController() {
		return controller;
	}

	@Override
	public String getTitle() {
		return LPMain.getTextRespectUISPr("stkl.intelligenterstklimport");
	}

	@Override
	protected void initViewImpl() {
		Listener l = new Listener();
		progressBar = new JProgressBar(getController().getProgressModel());
		gesamtePositionen = new WrapperTextField();
		importiertePositionen = new WrapperTextField();
		nichtSpeichern = new WrapperRadioButton(LPMain.getTextRespectUISPr("lp.nichtspeichern"));
		speichernUnter = new WrapperRadioButton(LPMain.getTextRespectUISPr("lp.speichernunter"));
		spezName = new WrapperTextField("");
		spezList = new JList(getController().getImportSpezNames().toArray());

		ButtonGroup bg = new ButtonGroup();
		bg.add(nichtSpeichern);
		bg.add(speichernUnter);
		
		nichtSpeichern.addActionListener(l);
		speichernUnter.addActionListener(l);
		spezList.addListSelectionListener(l);
		spezName.getDocument().addDocumentListener(l);
		
		spezList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		progressBar.setStringPainted(true);
		gesamtePositionen.setEditable(false);
		importiertePositionen.setEditable(false);
		
		setLayout(new MigLayout("wrap 2, fill, hidemode 2", "[fill,30%|fill,70%]", "[fill|fill|fill|fill]"));
		add(progressBar, "span");

		add(new WrapperLabel(LPMain.getTextRespectUISPr("stkl.intelligenterstklimport.gesamtepositionen")));
		add(gesamtePositionen, "wrap");
		
		add(new WrapperLabel(LPMain.getTextRespectUISPr("stkl.intelligenterstklimport.importiertepositionen")));
		add(importiertePositionen, "wrap");
		
		add(new JSeparator(), "span");
		add(nichtSpeichern, "wrap");
		add(speichernUnter);
		add(spezName);
		
		add(new JScrollPane(spezList), "skip, pushy");
	}
	
	private class Listener implements ActionListener, ListSelectionListener, DocumentListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if(e.getSource() == nichtSpeichern || e.getSource() == speichernUnter) {
				getController().setSave(speichernUnter.isSelected());
			}
		}

		@Override
		public void valueChanged(ListSelectionEvent e) {
			if(spezList.getSelectedValue() != null)
				getController().setSaveByName((String)spezList.getSelectedValue());
		}

		@Override
		public void changedUpdate(DocumentEvent e) {
			getController().setSaveByName(spezName.getText());
		}

		@Override
		public void insertUpdate(DocumentEvent e) {
			getController().setSaveByName(spezName.getText());
		}

		@Override
		public void removeUpdate(DocumentEvent e) {
			getController().setSaveByName(spezName.getText());
		}
	}

}
